import type { HeightGrid } from './description.ts';

/**
 * Bends a location to the ground under it as the client's `JavaModel.p` does, in the client's
 * integer arithmetic.
 *
 * The client keeps a vertex as integers in 512ths of a tile, with y down, and reads the height of
 * the ground under it from the heights of the four corners of the tile it is over, each weighted
 * by how near the vertex is to it. Each kind of bend moves a vertex's y by what it finds:
 *
 * - 1 moves every vertex by the ground's height under it, so the whole model follows the slope.
 * - 2 does the same, scaled down for the vertices nearer the top of the model than the skew says,
 *   so a tree's trunk follows the slope and its canopy does not.
 * - 4 moves every vertex by the height of the level above, plus the model's height, so a model
 *   hangs from the ceiling.
 * - 5 stretches the model between the ground and the level above, less the skew.
 * - 3 tilts the model to the slope rather than bending it, and is not done here yet.
 *
 * JavaScript's `>>`, `&` and `Math.trunc` do what Java's `>>`, `&` and `/` do on the integers
 * involved, which stay well within 32 bits.
 */

const TILE_SHIFT = 9;
const FINE_PER_TILE = 1 << TILE_SHIFT;

/** A vertex in the client's units, which the bend changes the y of. */
export interface ClientVertex {
    readonly x: number;
    y: number;
    readonly z: number;
}

/**
 * The heights a bend reads: a grid of tile corners starting `firstTile` tiles before the origin.
 */
export class Ground {

    private readonly grid: HeightGrid;
    private readonly firstTile: number;

    constructor(grid: HeightGrid, firstTile: number) {
        this.grid = grid;
        this.firstTile = firstTile;
    }

    /** Whether a tile corner is in the grid. */
    has(tileX: number, tileZ: number): boolean {
        const column = this.grid[tileX - this.firstTile];
        return column !== undefined && column[tileZ - this.firstTile] !== undefined;
    }

    at(tileX: number, tileZ: number): number {
        const height = this.grid[tileX - this.firstTile]?.[tileZ - this.firstTile];
        if (height === undefined) {
            throw new Error(`No height for tile corner ${tileX},${tileZ}.`);
        }
        return height;
    }

    /**
     * The ground's height under a point, from the corners of the tile it is in, as the client
     * weighs them.
     */
    under(x: number, z: number): number {
        const inX = x & (FINE_PER_TILE - 1);
        const inZ = z & (FINE_PER_TILE - 1);
        const tileX = x >> TILE_SHIFT;
        const tileZ = z >> TILE_SHIFT;
        const south = (this.at(tileX, tileZ) * (FINE_PER_TILE - inX) + this.at(tileX + 1, tileZ) * inX) >> TILE_SHIFT;
        const north = (this.at(tileX, tileZ + 1) * (FINE_PER_TILE - inX) + this.at(tileX + 1, tileZ + 1) * inX) >> TILE_SHIFT;
        return (south * (FINE_PER_TILE - inZ) + north * inZ) >> TILE_SHIFT;
    }

    /** Whether the four corners around a point are in the grid. */
    covers(x: number, z: number): boolean {
        const tileX = x >> TILE_SHIFT;
        const tileZ = z >> TILE_SHIFT;
        return this.has(tileX, tileZ) && this.has(tileX + 1, tileZ + 1);
    }
}

/**
 * The top and bottom of a model as the client measures them, in the client's units with y down,
 * so the top is the lesser.
 */
export interface Bounds {
    readonly top: number;
    readonly bottom: number;
}

/**
 * @param vertices the model's vertices after it has been turned, scaled and moved, in the
 *     client's units relative to the placement, which are changed in place.
 * @param bounds the client's top and bottom of the model after the same steps, which it takes
 *     over every vertex, drawn or not.
 * @param x where the placement stands, in the client's units from the map square's corner.
 * @param y the height the bend measures from, which is the ground's at the placement.
 */
export function bend(vertices: readonly ClientVertex[], bounds: Bounds, kind: number, skew: number,
                     floor: Ground | null, ceiling: Ground | null, x: number, y: number, z: number): void {
    if (vertices.length === 0 || kind === 0 || kind === 3) {
        return;
    }
    const lowest = bounds.bottom;
    const highest = bounds.top;

    if (kind === 1 && floor !== null) {
        for (const vertex of vertices) {
            if (floor.covers(vertex.x + x, vertex.z + z)) {
                vertex.y += floor.under(vertex.x + x, vertex.z + z) - y;
            }
        }
    } else if (kind === 2 && floor !== null) {
        for (const vertex of vertices) {
            const depth = Math.trunc((vertex.y << 16) / highest);
            if (depth < skew && floor.covers(vertex.x + x, vertex.z + z)) {
                const under = floor.under(vertex.x + x, vertex.z + z);
                vertex.y += Math.trunc((under - y) * (skew - depth) / skew);
            }
        }
    } else if (kind === 4 && ceiling !== null) {
        const height = lowest - highest;
        for (const vertex of vertices) {
            if (ceiling.covers(vertex.x + x, vertex.z + z)) {
                vertex.y += ceiling.under(vertex.x + x, vertex.z + z) + height - y;
            }
        }
    } else if (kind === 5 && floor !== null && ceiling !== null) {
        const height = lowest - highest;
        for (const vertex of vertices) {
            if (floor.covers(vertex.x + x, vertex.z + z) && ceiling.covers(vertex.x + x, vertex.z + z)) {
                const under = floor.under(vertex.x + x, vertex.z + z);
                const above = ceiling.under(vertex.x + x, vertex.z + z);
                const span = under - above - skew;
                vertex.y = ((Math.trunc((vertex.y << 8) / height) * span) >> 8) - (y - under);
            }
        }
    }
}

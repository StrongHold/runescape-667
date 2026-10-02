/**
 * What the export module writes beside a map square's ground: where each location stands, and
 * the heights a location is bent against. The export module's README lays it out, and every
 * number is in the client's units: 512 to a tile, x east, y down and z north, measured from the
 * map square's south west corner.
 */
export interface MapSquareDescription {
    readonly mapSquareX: number;
    readonly mapSquareZ: number;
    readonly tileX: number;
    readonly tileZ: number;
    readonly tilesAcross: number;
    readonly unitsPerTile: number;
    /** The ground file, relative to the description. */
    readonly ground: string;
    /** The location library, relative to the description. */
    readonly locs: string;
    readonly textures: string;
    readonly heights: Heights;
    readonly environment: Environment;
    readonly lights: readonly MapLight[];
    readonly placements: readonly Placement[];
}

/**
 * How the map square is lit, as the client's file gives it. The sun is the direction its light
 * comes from, in the client's frame with y down. The colours are packed sRGB.
 */
export interface Environment {
    readonly sun: readonly [number, number, number];
    readonly sunColour: number;
    readonly sunIntensity: number;
    readonly reverseSunIntensity: number;
    readonly ambient: number;
    readonly fogColour: number;
    readonly fogRange: number;
}

/** One light placed on the map square, in the client's units from its south west corner. */
export interface MapLight {
    readonly level: number;
    readonly x: number;
    readonly y: number;
    readonly z: number;
    readonly radius: number;
    readonly colour: number;
}

/**
 * The height of every tile corner of each level, as [x][z], from `firstTile` before the map
 * square, which is negative, to as far after it.
 */
export interface Heights {
    readonly firstTile: number;
    readonly levels: readonly HeightGrid[];
    readonly underwater?: HeightGrid;
}

export type HeightGrid = readonly (readonly number[])[];

export interface Placement {
    readonly loc: number;
    /** The shape the client builds the model as. */
    readonly shape: number;
    /** The rotation the client builds it with, above 3 for a diagonal. */
    readonly rotation: number;
    readonly level: number;
    /** The level whose ground it is bent against. */
    readonly virtualLevel: number;
    readonly underwater: boolean;
    readonly x: number;
    readonly y: number;
    readonly z: number;
    readonly part: string;
    readonly sequencesOf?: number;
}

/**
 * What a location's file carries in its root node, for placing it as the README's steps say.
 */
export interface LocExtras {
    readonly loc: number;
    readonly name: string | null;
    readonly shapes: readonly number[];
    readonly mirrored: boolean;
    readonly resize: readonly [number, number, number];
    readonly offset: readonly [number, number, number];
    readonly translate: readonly [number, number, number];
    readonly hillchange: number;
    readonly hillskew: number;
    readonly sequences?: readonly number[];
    readonly sequenceWeights?: readonly number[];
    readonly randomStartFrame?: boolean;
}

/**
 * What each mesh node of a location's file carries: its shape, whether it is the mesh turned for
 * a diagonal placement, and the client's own top and bottom of the model in the client's units,
 * with y down, which the bend measures the model by.
 */
export interface ShapeExtras {
    readonly shape: number;
    readonly turned: boolean;
    readonly minY: number;
    readonly maxY: number;
}

export function isLocExtras(value: unknown): value is LocExtras {
    return typeof value === 'object' && value !== null && 'loc' in value && 'hillchange' in value;
}

export function isShapeExtras(value: unknown): value is ShapeExtras {
    return typeof value === 'object' && value !== null && 'shape' in value && 'turned' in value && 'minY' in value;
}

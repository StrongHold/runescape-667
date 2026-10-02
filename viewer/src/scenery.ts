import { GridHelper, Mesh, PlaneGeometry, Scene, ShadowMaterial } from 'three';

/**
 * The game's own ground is y = 0: a model stands on it as it was authored, and the exporter keeps
 * that. So the grid and the ground that catches shadows sit there, not under the lowest vertex.
 */
const GROUND = 0;

/** A map square is 64 tiles across, its south west corner at the origin and north along -z. */
const MAP_SQUARE_TILES = 64;

/**
 * The grid and the ground around what is open. A model stands on a plane that catches its shadow,
 * with a grid of one tile squares. A map square has ground of its own that takes the shadows, so
 * it gets a grid with a line for every tile instead, drawn faintly at its lowest point, and as
 * that ground is seldom flat the grid starts hidden.
 */
export interface Scenery {
    /**
     * Shows the scenery that suits what is open.
     *
     * @param lowest the height of the lowest point of what is open, where a map square's grid is drawn.
     */
    readonly show: (mapSquare: boolean, lowest: number, gridOn: boolean) => void;
    readonly showGrid: (on: boolean) => void;
}

export function createScenery(scene: Scene): Scenery {
    const grid = tileGrid(10, 0.35);
    grid.position.y = GROUND;

    const mapSquareGrid = tileGrid(MAP_SQUARE_TILES, 0.2);
    mapSquareGrid.position.set(MAP_SQUARE_TILES / 2, GROUND, -MAP_SQUARE_TILES / 2);
    mapSquareGrid.visible = false;

    const ground = new Mesh(new PlaneGeometry(40, 40), new ShadowMaterial({ opacity: 0.3 }));
    ground.rotation.x = -Math.PI / 2;
    ground.position.y = GROUND;
    ground.receiveShadow = true;

    scene.add(grid, mapSquareGrid, ground);
    let showingMapSquare = false;

    const showGrid = (on: boolean): void => {
        grid.visible = on && !showingMapSquare;
        mapSquareGrid.visible = on && showingMapSquare;
    };

    return {
        show: (mapSquare, lowest, gridOn) => {
            showingMapSquare = mapSquare;
            ground.visible = !mapSquare;
            mapSquareGrid.position.y = lowest;
            showGrid(gridOn);
        },
        showGrid
    };
}

function tileGrid(tiles: number, opacity: number): GridHelper {
    const helper = new GridHelper(tiles, tiles, 0x8899aa, 0x8899aa);
    helper.material.transparent = true;
    helper.material.opacity = opacity;
    return helper;
}

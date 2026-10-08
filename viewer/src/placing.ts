import { Matrix4 } from 'three';
import type { Bounds, ClientVertex } from './bend.ts';
import type { LocExtras, Placement, ShapeExtras } from './description.ts';

/**
 * Places a location's asset as the export module's README says an importer places it: the
 * steps that turn, scale and move the asset become one matrix, in the viewer's frame of metres
 * with y up and north along -z, and the bend between them is a separate step on the vertices.
 *
 * The client's frame is the mirror of the viewer's in y and z, so a turn the client makes by a
 * positive angle about y is a turn by the negative of it here, and a move of (x, y, z) in the
 * client's units is (x, -y, -z) over 512 here.
 *
 * A placement that is bent takes the same steps in the client's integer arithmetic instead, by
 * {@link placeVertices}, because the bend reads the integers the client would hold and a turn or
 * a scale done in floating point lands a vertex a unit away from them.
 */

const FINE_PER_METRE = 512;
const FULL_SCALE = 128;
const WALL_L = 2;
const WALL_DECORATION = 4;
const CENTREPIECE = 10;
const EIGHTH_TURN_RADIANS = Math.PI / 4;
const QUARTER_TURN_RADIANS = Math.PI / 2;
const WALL_DECORATION_TURNED_MOVE_FINE: readonly [number, number, number] = [180, 0, -180];

/** Whether a placement uses the asset that is turned 45 degrees already. */
export function usesTurnedMesh(extras: LocExtras, placement: Placement): boolean {
    return placement.rotation > 3 && placement.shape === WALL_DECORATION && extras.sequences !== undefined;
}

/**
 * The transform of the steps before the bend: the mirror of an L-shaped wall, the turn of a
 * diagonal wall decoration, the quarter turns, the scale, the offset, and the turn of a diagonal
 * centrepiece, in that order.
 */
export function placementMatrix(extras: LocExtras, placement: Placement, turned: boolean): Matrix4 {
    const matrix = new Matrix4();
    const rotation = placement.rotation;

    if (placement.shape === WALL_L && rotation > 3) {
        matrix.premultiply(new Matrix4().makeScale(1, 1, -1));
    }
    if (placement.shape === WALL_DECORATION && rotation > 3) {
        if (!turned) {
            matrix.premultiply(new Matrix4().makeRotationY(-EIGHTH_TURN_RADIANS));
        }
        matrix.premultiply(translation(WALL_DECORATION_TURNED_MOVE_FINE));
    }
    if ((rotation & 3) !== 0) {
        matrix.premultiply(new Matrix4().makeRotationY(-(rotation & 3) * QUARTER_TURN_RADIANS));
    }
    const [scaleX, scaleY, scaleZ] = extras.resize;
    if (scaleX !== FULL_SCALE || scaleY !== FULL_SCALE || scaleZ !== FULL_SCALE) {
        matrix.premultiply(new Matrix4().makeScale(scaleX / FULL_SCALE, scaleY / FULL_SCALE, scaleZ / FULL_SCALE));
    }
    if (extras.offset.some(part => part !== 0)) {
        matrix.premultiply(translation(extras.offset));
    }
    if (placement.shape === CENTREPIECE && rotation > 3) {
        matrix.premultiply(new Matrix4().makeRotationY(-EIGHTH_TURN_RADIANS));
    }
    return matrix;
}

/**
 * The transform after the bend: the type's translation, and the placement's position.
 */
export function standingMatrix(extras: LocExtras, placement: Placement): Matrix4 {
    const [translateX, translateY, translateZ] = extras.translate;
    return translation([placement.x + translateX, placement.y + translateY, placement.z + translateZ]);
}

/** A move in the client's units, as a transform in the viewer's frame. */
function translation(move: readonly [number, number, number]): Matrix4 {
    const [x, y, z] = move;
    return new Matrix4().makeTranslation(x / FINE_PER_METRE, -y / FINE_PER_METRE, -z / FINE_PER_METRE);
}

/**
 * The sine and cosine of 45 degrees as the client's tables hold them, out of 16384.
 */
const EIGHTH_TURN_SIN = 11585;
const EIGHTH_TURN_COS = 11585;
const TRIG_SHIFT = 14;
const SCALE_SHIFT = 7;

/**
 * The steps before the bend, done to vertices in the client's units with the client's integer
 * arithmetic: the same steps as {@link placementMatrix}, with the same results the client gets.
 */
export function placeVertices(vertices: readonly ClientVertex[], extras: LocExtras, placement: Placement,
                              turned: boolean): ClientVertex[] {
    const rotation = placement.rotation;
    let placed = vertices.map(vertex => ({ x: vertex.x, y: vertex.y, z: vertex.z }));

    if (placement.shape === WALL_L && rotation > 3) {
        placed = placed.map(vertex => ({ x: vertex.x, y: vertex.y, z: -vertex.z }));
    }
    if (placement.shape === WALL_DECORATION && rotation > 3) {
        if (!turned) {
            placed = placed.map(eighthTurn);
        }
        const [moveX, moveY, moveZ] = WALL_DECORATION_TURNED_MOVE_FINE;
        placed = placed.map(vertex => ({ x: vertex.x + moveX, y: vertex.y + moveY, z: vertex.z + moveZ }));
    }
    for (let turns = 0; turns < (rotation & 3); turns++) {
        placed = placed.map(vertex => ({ x: vertex.z, y: vertex.y, z: -vertex.x }));
    }
    const [scaleX, scaleY, scaleZ] = extras.resize;
    if (scaleX !== FULL_SCALE || scaleY !== FULL_SCALE || scaleZ !== FULL_SCALE) {
        placed = placed.map(vertex => ({
            x: (vertex.x * scaleX) >> SCALE_SHIFT,
            y: (vertex.y * scaleY) >> SCALE_SHIFT,
            z: (vertex.z * scaleZ) >> SCALE_SHIFT
        }));
    }
    const [offsetX, offsetY, offsetZ] = extras.offset;
    if (offsetX !== 0 || offsetY !== 0 || offsetZ !== 0) {
        placed = placed.map(vertex => ({ x: vertex.x + offsetX, y: vertex.y + offsetY, z: vertex.z + offsetZ }));
    }
    if (placement.shape === CENTREPIECE && rotation > 3) {
        placed = placed.map(eighthTurn);
    }
    return placed;
}

/**
 * The client's top and bottom of a model after the steps before the bend, which only the scale
 * changes, as the client's integer scale changes them.
 */
export function placeBounds(shape: ShapeExtras, extras: LocExtras): Bounds {
    const scaleY = extras.resize[1];
    if (scaleY === FULL_SCALE) {
        return { top: shape.minY, bottom: shape.maxY };
    } else {
        return { top: (shape.minY * scaleY) >> SCALE_SHIFT, bottom: (shape.maxY * scaleY) >> SCALE_SHIFT };
    }
}

/** A turn of 45 degrees about y as the client's `JavaModel.k` does it, with its tables. */
function eighthTurn(vertex: ClientVertex): ClientVertex {
    return {
        x: (vertex.z * EIGHTH_TURN_SIN + vertex.x * EIGHTH_TURN_COS) >> TRIG_SHIFT,
        y: vertex.y,
        z: (vertex.z * EIGHTH_TURN_COS - vertex.x * EIGHTH_TURN_SIN) >> TRIG_SHIFT
    };
}

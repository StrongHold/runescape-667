import {
    AnimationClip,
    BufferGeometry,
    Cache,
    Group,
    KeyframeTrack,
    Matrix3,
    Matrix4,
    Mesh,
    Object3D,
    PropertyBinding,
    Vector3
} from 'three';
import { GLTFLoader, type GLTF } from 'three/addons/loaders/GLTFLoader.js';
import { clone as cloneWithSkeleton } from 'three/addons/utils/SkeletonUtils.js';
import { bend, Ground, type Bounds, type ClientVertex } from './bend.ts';
import {
    isLocExtras,
    isShapeExtras,
    type LocExtras,
    type MapSquareDescription,
    type Placement,
    type ShapeExtras
} from './description.ts';
import { completeMorphTargets } from './meshes.ts';
import { placeBounds, placementMatrix, placeVertices, standingMatrix, usesTurnedMesh } from './placing.ts';

/**
 * Builds a map square from what the export module writes: its ground, and its description,
 * which names a location from the library for each placement. Each location's file is loaded
 * once, and each placement is a copy of the right shape of it, placed as the README's steps say.
 *
 * This is the viewer's import of the format, and the reference for any other engine's.
 */
export interface LoadedMapSquare {
    readonly root: Object3D;
    readonly description: MapSquareDescription;
    /** One clip for each placement that animates, with the tracks bound to its own copy. */
    readonly clips: readonly AnimationClip[];
    readonly placements: number;
    readonly kinds: number;
    readonly missing: readonly string[];
}

const UNITS_PER_METRE = 512;

/**
 * How many location files are fetched at once. The files of a map square refer to the same few
 * textures over and over, and a browser given hundreds of files at once gives up on some of the
 * requests, so the files are fetched a few at a time and each texture is kept once it is seen.
 */
const FETCHED_AT_ONCE = 16;

export async function loadMapSquare(url: string, loader: GLTFLoader): Promise<LoadedMapSquare> {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`${url} could not be fetched: ${response.status}.`);
    }
    const description: MapSquareDescription = await response.json();
    const base = url.slice(0, url.lastIndexOf('/') + 1);

    const ground = await loader.loadAsync(base + description.ground);
    const locs = await loadLocs(base + description.locs + '/', description, loader);

    const root = new Group();
    root.name = `mapsquare ${description.mapSquareX}_${description.mapSquareZ}`;
    root.userData = { mapSquareX: description.mapSquareX, mapSquareZ: description.mapSquareZ };
    root.add(ground.scene);

    const placed = new Group();
    placed.name = 'locations';
    root.add(placed);

    const clips: AnimationClip[] = [];
    const missing: string[] = [];
    for (const placement of description.placements) {
        const loc = locs.get(placement.loc);
        const built = loc === undefined ? null : place(loc, placement, description);
        if (built === null) {
            missing.push(`location ${placement.loc} shape ${placement.shape}`);
        } else {
            placed.add(built.object);
            clips.push(...built.clips);
        }
    }

    return { root, description, clips, placements: placed.children.length, kinds: locs.size, missing };
}

async function loadLocs(directory: string, description: MapSquareDescription,
                        loader: GLTFLoader): Promise<Map<number, GLTF>> {
    Cache.enabled = true;
    const ids = [...new Set(description.placements.map(placement => placement.loc))];
    const locs = new Map<number, GLTF>();
    for (let from = 0; from < ids.length; from += FETCHED_AT_ONCE) {
        const batch = ids.slice(from, from + FETCHED_AT_ONCE);
        const loaded = await Promise.all(batch.map(id => loader.loadAsync(`${directory}${id}.glb`)));
        const types = await Promise.all(batch.map(id => fetchType(`${directory}${id}.json`)));
        batch.forEach((id, index) => {
            completeMorphTargets(loaded[index].scene);
            loaded[index].scene.userData = types[index];
            locs.set(id, loaded[index]);
        });
    }
    return locs;
}

/**
 * A location type's data, which the export writes beside its mesh file. The viewer hangs it on
 * the file's scene, where the placing reads it.
 */
async function fetchType(url: string): Promise<Record<string, unknown>> {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`${url} could not be fetched: ${response.status}.`);
    }
    return await response.json() as Record<string, unknown>;
}

interface Built {
    readonly object: Object3D;
    readonly clips: readonly AnimationClip[];
}

/**
 * One placement: a copy of the location's shape, under a node for the steps before the bend and
 * a node for the steps after it, bent between them where the type asks for that.
 */
function place(loc: GLTF, placement: Placement, description: MapSquareDescription): Built | null {
    const extras = locExtras(loc);
    if (extras === null) {
        return null;
    }
    const turned = usesTurnedMesh(extras, placement);
    const source = shapeNode(loc, placement.shape, turned);
    if (source === null) {
        return null;
    }

    const copy = cloneWithSkeleton(source);
    const inner = new Object3D();
    inner.matrixAutoUpdate = false;
    inner.add(copy);

    if (extras.hillchange === 0) {
        inner.matrix.copy(placementMatrix(extras, placement, turned));
    } else {
        bakeBent(copy, placeBounds(source.userData as ShapeExtras, extras), extras, placement, turned, description);
    }

    const outer = new Object3D();
    outer.name = `${extras.name === null || extras.name === 'null' ? 'location' : extras.name} ${placement.loc}`;
    outer.userData = { ...placement, randomStartFrame: extras.randomStartFrame === true };
    outer.matrixAutoUpdate = false;
    outer.matrix.copy(standingMatrix(extras, placement));
    outer.add(inner);

    return { object: outer, clips: rebindClips(loc, source, copy, outer.name) };
}

/**
 * The location's type data, which the export writes beside its file and the viewer hangs on its scene.
 */
function locExtras(loc: GLTF): LocExtras | null {
    return isLocExtras(loc.scene.userData) ? loc.scene.userData : null;
}

function shapeNode(loc: GLTF, shape: number, turned: boolean): Object3D | null {
    let found: Object3D | null = null;
    loc.scene.traverse(node => {
        const extras = node.userData;
        if (found === null && isShapeExtras(extras) && extras.shape === shape && extras.turned === turned) {
            found = node;
        }
    });
    return found;
}

/**
 * Bakes the steps before the bend and the bend itself into every mesh of a copy, in the client's
 * integer arithmetic, so that each vertex is where the client holds it. The normals and the morph
 * targets are turned and scaled with the steps' transform, as the bend leaves them to be.
 */
function bakeBent(copy: Object3D, bounds: Bounds, extras: LocExtras, placement: Placement, turned: boolean,
                  description: MapSquareDescription): void {
    const matrix = placementMatrix(extras, placement, turned);
    const grids = description.heights;
    const floor = placement.underwater
        ? grids.underwater === undefined ? null : new Ground(grids.underwater, grids.firstTile)
        : new Ground(grids.levels[placement.virtualLevel], grids.firstTile);
    const above = placement.underwater ? grids.levels[0] : grids.levels[placement.virtualLevel + 1];
    const ceiling = above === undefined ? null : new Ground(above, grids.firstTile);

    copy.traverse(node => {
        if (node instanceof Mesh) {
            node.geometry = bentGeometry(node.geometry, matrix, bounds, extras, placement, turned, floor, ceiling);
        }
    });
}

function bentGeometry(geometry: BufferGeometry, matrix: Matrix4, bounds: Bounds, extras: LocExtras,
                      placement: Placement, turned: boolean, floor: Ground | null,
                      ceiling: Ground | null): BufferGeometry {
    const bent = geometry.clone();
    const positions = bent.attributes.position;
    const held: ClientVertex[] = [];
    for (let at = 0; at < positions.count; at++) {
        held.push({
            x: Math.round(positions.getX(at) * UNITS_PER_METRE),
            y: Math.round(-positions.getY(at) * UNITS_PER_METRE),
            z: Math.round(-positions.getZ(at) * UNITS_PER_METRE)
        });
    }

    const vertices = placeVertices(held, extras, placement, turned);
    bend(vertices, bounds, extras.hillchange, extras.hillskew, floor, ceiling, placement.x, placement.y, placement.z);
    for (let at = 0; at < positions.count; at++) {
        positions.setXYZ(at, vertices[at].x / UNITS_PER_METRE, -vertices[at].y / UNITS_PER_METRE,
            -vertices[at].z / UNITS_PER_METRE);
    }
    positions.needsUpdate = true;

    const linear = new Matrix3().setFromMatrix4(matrix);
    const normals = bent.attributes.normal;
    if (normals !== undefined) {
        const normal = new Vector3();
        const normalMatrix = new Matrix3().getNormalMatrix(matrix);
        for (let at = 0; at < normals.count; at++) {
            normal.fromBufferAttribute(normals, at).applyMatrix3(normalMatrix).normalize();
            normals.setXYZ(at, normal.x, normal.y, normal.z);
        }
        normals.needsUpdate = true;
    }
    for (const target of bent.morphAttributes.position ?? []) {
        const moved = new Vector3();
        for (let at = 0; at < target.count; at++) {
            moved.fromBufferAttribute(target, at).applyMatrix3(linear);
            target.setXYZ(at, moved.x, moved.y, moved.z);
        }
        target.needsUpdate = true;
    }

    bent.computeBoundingBox();
    bent.computeBoundingSphere();
    return bent;
}

/**
 * The location's animations, bound to the copy. The loader names each track after the node it
 * moves, a mesh for its morph targets or a bone, and the copy's nodes are named by their own ids
 * instead, in the order the source's were, so that each placement animates on its own.
 */
function rebindClips(loc: GLTF, source: Object3D, copy: Object3D, label: string): AnimationClip[] {
    const sourceNodes = descendants(source);
    const copyNodes = descendants(copy);
    const clips: AnimationClip[] = [];

    for (const clip of loc.animations) {
        const tracks: KeyframeTrack[] = [];
        for (const track of clip.tracks) {
            const name = PropertyBinding.parseTrackName(track.name);
            const target = PropertyBinding.findNode(loc.scene, name.nodeName);
            const index = sourceNodes.findIndex(node => node === target);
            if (index !== -1) {
                const bound = track.clone();
                bound.name = `${copyNodes[index].uuid}.${name.propertyName}`;
                tracks.push(bound);
            }
        }
        if (tracks.length > 0) {
            clips.push(new AnimationClip(`${label} ${clip.name}`, clip.duration, tracks));
        }
    }
    return clips;
}

function descendants(object: Object3D): Object3D[] {
    const nodes: Object3D[] = [];
    object.traverse(node => {
        nodes.push(node);
    });
    return nodes;
}

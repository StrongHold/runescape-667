import { Box3, Mesh, Object3D, Vector3 } from 'three';

export function meshesOf(object: Object3D): Mesh[] {
    const meshes: Mesh[] = [];
    object.traverse(node => {
        if (node instanceof Mesh) {
            meshes.push(node);
        }
    });
    return meshes;
}

/**
 * Whether a mesh is blended over what is behind it, such as water or a layer of a ground
 * texture. Such a mesh casts no shadow, as light passes through it.
 */
export function isTransparent(mesh: Mesh): boolean {
    return [mesh.material].flat().some(material => material.transparent);
}

export function setWireframe(object: Object3D, on: boolean): void {
    for (const mesh of meshesOf(object)) {
        for (const material of [mesh.material].flat()) {
            if ('wireframe' in material) {
                material.wireframe = on;
            }
        }
    }
}

export function triangleCount(meshes: readonly Mesh[]): number {
    return meshes.reduce((sum, mesh) => {
        const geometry = mesh.geometry;
        return sum + (geometry.index?.count ?? geometry.attributes.position.count) / 3;
    }, 0);
}

/**
 * The box the model fills in its base pose. three.js widens a box for morph targets by adding the
 * largest change of any vertex to the extreme of every other, which for an animated NPC reaches far
 * below its feet, so the box is taken from the base positions alone.
 */
export function baseBox(object: Object3D): Box3 {
    object.updateMatrixWorld(true);
    const box = new Box3();
    const point = new Vector3();
    for (const mesh of meshesOf(object)) {
        const positions = mesh.geometry.attributes.position;
        for (let at = 0; at < positions.count; at++) {
            box.expandByPoint(point.fromBufferAttribute(positions, at).applyMatrix4(mesh.matrixWorld));
        }
    }
    return box;
}

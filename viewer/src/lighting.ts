import { DirectionalLight, HemisphereLight, Scene, Vector3 } from 'three';

/**
 * Where the sun is, as a direction from what it lights: high in the south east, so the shadows
 * fall north west and the faces of a model that look at the camera are lit.
 */
const SUN_DIRECTION = new Vector3(1.5, 3, 2).normalize();

/**
 * The sun, whose shadow is fitted to whatever is open.
 */
export interface Lighting {
    /**
     * Puts the sun up the sky from what is open, and fits its shadow camera to a box around it.
     *
     * @param reach how far what is open stretches from its centre, which the sun stands back from.
     * @param extent how far the shadow camera reaches either side of the centre.
     * @param size how many texels a side the shadow map has.
     */
    readonly castShadow: (centre: Vector3, reach: number, extent: number, size: number) => void;
}

export function createLighting(scene: Scene): Lighting {
    scene.add(new HemisphereLight(0xffffff, 0x445566, 1.6));
    const sun = new DirectionalLight(0xffffff, 1.8);
    sun.castShadow = true;
    sun.shadow.bias = -0.0005;
    scene.add(sun, sun.target);

    return {
        castShadow: (centre, reach, extent, size) => {
            if (sun.shadow.mapSize.x !== size) {
                sun.shadow.mapSize.set(size, size);
                sun.shadow.map?.dispose();
                sun.shadow.map = null;
            }

            sun.position.copy(centre).addScaledVector(SUN_DIRECTION, reach * 2);
            sun.target.position.copy(centre);
            const shadow = sun.shadow.camera;
            shadow.left = -extent;
            shadow.right = extent;
            shadow.top = extent;
            shadow.bottom = -extent;
            shadow.near = reach * 0.1;
            shadow.far = reach * 4;
            shadow.updateProjectionMatrix();
        }
    };
}

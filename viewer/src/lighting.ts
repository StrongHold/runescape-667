import { DirectionalLight, HemisphereLight, Scene, Vector3 } from 'three';

/**
 * Where the sun is, as a direction from what it lights: high in the south east, so the shadows
 * fall north west and the faces of a model that look at the camera are lit.
 */
const SUN_DIRECTION = new Vector3(1.5, 3, 2).normalize();

/** A direction the sun's light comes from, in the viewer's frame, and how strong and what colour it is. */
export interface Sun {
    readonly direction: Vector3;
    readonly colour: number;
    readonly intensity: number;
}

/**
 * The sun and the sky light, whose shadow is fitted to whatever is open.
 */
export interface Lighting {
    /** Lights the scene as a map square's file says, or as the viewer does by default. */
    readonly setSun: (sun: Sun, ambient: number) => void;
    readonly resetSun: () => void;
    /**
     * Puts the sun up the sky from what is open, and fits its shadow camera to a box around it.
     *
     * @param reachMetres how far what is open stretches from its centre, which the sun stands back from.
     * @param extentMetres how far the shadow camera reaches either side of the centre.
     * @param sizeTexels how many texels a side the shadow map has.
     */
    readonly castShadow: (centre: Vector3, reachMetres: number, extentMetres: number, sizeTexels: number) => void;
}

/** The viewer's own light, for a model or an NPC, which no file lights. */
const DEFAULT_SUN: Sun = { direction: SUN_DIRECTION, colour: 0xffffff, intensity: 1.8 };
const DEFAULT_AMBIENT = 1.6;

export function createLighting(scene: Scene): Lighting {
    const sky = new HemisphereLight(0xffffff, 0x445566, DEFAULT_AMBIENT);
    scene.add(sky);
    const sun = new DirectionalLight(0xffffff, 1.8);
    sun.castShadow = true;
    sun.shadow.bias = -0.0005;
    scene.add(sun, sun.target);
    let direction = SUN_DIRECTION.clone();
    let centre = new Vector3();
    let reachMetres = 1;

    const setSun = (given: Sun, ambient: number): void => {
        direction = given.direction.clone().normalize();
        sun.color.set(given.colour);
        sun.intensity = given.intensity;
        sky.intensity = ambient;
        sun.position.copy(centre).addScaledVector(direction, reachMetres * 2);
    };

    return {
        setSun,
        resetSun: () => setSun(DEFAULT_SUN, DEFAULT_AMBIENT),
        castShadow: (at, farMetres, extentMetres, sizeTexels) => {
            centre = at.clone();
            reachMetres = farMetres;
            if (sun.shadow.mapSize.x !== sizeTexels) {
                sun.shadow.mapSize.set(sizeTexels, sizeTexels);
                sun.shadow.map?.dispose();
                sun.shadow.map = null;
            }

            sun.position.copy(centre).addScaledVector(direction, reachMetres * 2);
            sun.target.position.copy(centre);
            const shadow = sun.shadow.camera;
            shadow.left = -extentMetres;
            shadow.right = extentMetres;
            shadow.top = extentMetres;
            shadow.bottom = -extentMetres;
            shadow.near = reachMetres * 0.1;
            shadow.far = reachMetres * 4;
            shadow.updateProjectionMatrix();
        }
    };
}

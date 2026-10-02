import { Box3, PerspectiveCamera, Vector3 } from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import type { Lighting } from './lighting.ts';

/**
 * How many texels a side the shadow map has: enough for one model, and twice that for a map
 * square, which is 64 m across and would otherwise cast shadows a hand's width wide.
 */
const MODEL_SHADOW_SIZE = 2048;
const SQUARE_SHADOW_SIZE = 4096;

/**
 * Points the camera at the box what is open fills, and casts the sun's shadow over it. A model is
 * seen from the front and to one side, and its shadow falls on the ground around it. A map square
 * is seen from high over its south edge, looking north and down, as the game sees it, and its own
 * ground takes every shadow, so the shadow is fitted to the square alone.
 */
export function frame(camera: PerspectiveCamera, controls: OrbitControls, lighting: Lighting, box: Box3,
                      square: boolean): void {
    const centre = box.getCenter(new Vector3());
    controls.target.copy(centre);

    if (square) {
        const radius = box.getSize(new Vector3()).length() / 2;
        camera.position.set(centre.x, centre.y + radius * 1.3, centre.z + radius * 1.05);
        camera.near = radius / 500;
        camera.far = radius * 40;
        lighting.castShadow(centre, radius, radius, SQUARE_SHADOW_SIZE);
    } else {
        const size = box.getSize(new Vector3());
        const largest = Math.max(size.x, size.y, size.z);
        const reach = largest > 0 ? largest : 1;
        camera.position.set(centre.x + reach * 1.1, centre.y + reach * 0.6, centre.z + reach * 1.3);
        camera.near = reach / 500;
        camera.far = reach * 50;
        lighting.castShadow(centre, reach, reach * 1.5, MODEL_SHADOW_SIZE);
    }
    camera.updateProjectionMatrix();
}

/**
 * The four sides a thing can be looked at from, as directions from its centre towards the camera,
 * each raised a little so the camera looks down on it.
 */
export const SIDES: readonly { readonly name: string; readonly direction: Vector3 }[] = [
    { name: 'the south', direction: new Vector3(0, 0.5, 1).normalize() },
    { name: 'the east', direction: new Vector3(1, 0.5, 0).normalize() },
    { name: 'the north', direction: new Vector3(0, 0.5, -1).normalize() },
    { name: 'the west', direction: new Vector3(-1, 0.5, 0).normalize() }
];

/**
 * Points the camera at the box one node fills, from one side of it, close enough to fill the
 * view, and casts the sun's shadow over it as for a model.
 */
export function frameFrom(camera: PerspectiveCamera, controls: OrbitControls, lighting: Lighting, box: Box3,
                          direction: Vector3): void {
    const centre = box.getCenter(new Vector3());
    const size = box.getSize(new Vector3());
    const largest = Math.max(size.x, size.y, size.z);
    const reach = largest > 0 ? largest : 1;
    controls.target.copy(centre);
    camera.position.copy(centre).addScaledVector(direction, reach * 1.8);
    camera.near = reach / 500;
    camera.far = reach * 50 + 500;
    camera.updateProjectionMatrix();
    lighting.castShadow(centre, reach, reach * 1.5, MODEL_SHADOW_SIZE);
}

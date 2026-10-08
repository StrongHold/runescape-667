import { Box3, PerspectiveCamera, Vector3 } from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import type { Lighting } from './lighting.ts';

/**
 * How many texels a side the shadow map has: enough for one model, and twice that for a map
 * map square, which is 64 m across and would otherwise cast shadows a hand's width wide.
 */
const MODEL_SHADOW_SIZE_TEXELS = 2048;
const MAP_SQUARE_SHADOW_SIZE_TEXELS = 4096;

/**
 * Points the camera at the box what is open fills, and casts the sun's shadow over it. A model is
 * seen from the front and to one side, and its shadow falls on the ground around it. A map square
 * is seen from high over its south edge, looking north and down, as the game sees it, and its own
 * ground takes every shadow, so the shadow is fitted to the map square alone.
 */
export function frame(camera: PerspectiveCamera, controls: OrbitControls, lighting: Lighting, box: Box3,
                      mapSquare: boolean): void {
    const centre = box.getCenter(new Vector3());
    controls.target.copy(centre);

    if (mapSquare) {
        const radiusMetres = box.getSize(new Vector3()).length() / 2;
        camera.position.set(centre.x, centre.y + radiusMetres * 1.3, centre.z + radiusMetres * 1.05);
        camera.near = radiusMetres / 500;
        camera.far = radiusMetres * 40;
        lighting.castShadow(centre, radiusMetres, radiusMetres, MAP_SQUARE_SHADOW_SIZE_TEXELS);
    } else {
        const size = box.getSize(new Vector3());
        const largest = Math.max(size.x, size.y, size.z);
        const reachMetres = largest > 0 ? largest : 1;
        camera.position.set(centre.x + reachMetres * 1.1, centre.y + reachMetres * 0.6, centre.z + reachMetres * 1.3);
        camera.near = reachMetres / 500;
        camera.far = reachMetres * 50;
        lighting.castShadow(centre, reachMetres, reachMetres * 1.5, MODEL_SHADOW_SIZE_TEXELS);
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
    const reachMetres = largest > 0 ? largest : 1;
    controls.target.copy(centre);
    camera.position.copy(centre).addScaledVector(direction, reachMetres * 1.8);
    camera.near = reachMetres / 500;
    camera.far = reachMetres * 50 + 500;
    camera.updateProjectionMatrix();
    lighting.castShadow(centre, reachMetres, reachMetres * 1.5, MODEL_SHADOW_SIZE_TEXELS);
}

import { Fog, Scene, Vector3 } from 'three';
import type { Environment } from './description.ts';
import type { Lighting } from './lighting.ts';

/**
 * Lights a map square and fogs it as its file says.
 *
 * The client's sun direction is in its own frame, with y down and north along +z, and is turned
 * into the viewer's frame by negating y and z. Its intensity is the client's factor for faces that
 * look at the sun, and its ambient factor becomes the sky light's strength, both against the
 * viewer's own defaults so that a map square lit as the client lights it looks about as bright as
 * a model lit by the viewer. The client's fog is a band before the far plane, and here it runs
 * over the last part of the view, in the file's colour.
 */

/** The viewer's own sun strength and sky light, which the client's factors are measured against. */
const SUN_SCALE = 1.8 / 0.69921875;
const AMBIENT_SCALE = 1.6 / 1.1523438;

/** How much of the view distance the fog covers, from where it starts to the far plane. */
const FOG_PART = 0.4;

export function applyEnvironment(scene: Scene, lighting: Lighting, environment: Environment, far: number): void {
    const [x, y, z] = environment.sun;
    lighting.setSun({
        direction: new Vector3(x, -y, -z),
        colour: environment.sunColour,
        intensity: environment.sunIntensity * SUN_SCALE
    }, environment.ambient * AMBIENT_SCALE);
    scene.fog = new Fog(environment.fogColour, far * (1 - FOG_PART), far);
}

export function clearEnvironment(scene: Scene, lighting: Lighting): void {
    lighting.resetSun();
    scene.fog = null;
}

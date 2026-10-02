import { AnimationClip, Object3D } from 'three';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';
import { createAnimationPlayer } from './animation.ts';
import { bindBackdrop } from './backdrop.ts';
import { bindDrop } from './drop.ts';
import { bindFocus } from './focus.ts';
import { fetchExported, listExported, shortName, SINGULAR, type ExportedFile } from './exported.ts';
import { describe, isMapSquare } from './extras.ts';
import { loadMapSquare } from './mapsquare.ts';
import { frame, frameFrom, SIDES } from './framing.ts';
import { createLighting } from './lighting.ts';
import { baseBox, completeMorphTargets, isTransparent, meshesOf, setWireframe, triangleCount } from './meshes.ts';
import { readPage } from './page.ts';
import { createScenery } from './scenery.ts';
import { createStage } from './stage.ts';

/** Where a dropped file is taken to be, so that `../textures/<id>.png` finds the served textures. */
const DROPPED_PATH = '/exports/dropped/';

const page = readPage();
const stage = createStage(page.stage);
const lighting = createLighting(stage.scene);
const scenery = createScenery(stage.scene);
const player = createAnimationPlayer(page.animation, page.play);
const loader = new GLTFLoader();
let current: Object3D | null = null;

/**
 * Shows an object that was loaded, in place of what was open before.
 */
function show(object: Object3D, animations: readonly AnimationClip[], label: string, contents: string): void {
    if (current !== null) {
        stage.scene.remove(current);
    }
    current = object;
    const mapSquare = isMapSquare(current);
    for (const mesh of meshesOf(current)) {
        mesh.castShadow = !isTransparent(mesh);
        mesh.receiveShadow = mapSquare;
    }
    stage.scene.add(current);
    player.load(current, animations, mapSquare);
    setWireframe(current, page.wire.checked);

    const box = baseBox(current);
    frame(stage.camera, stage.controls, lighting, box, mapSquare);
    if (mapSquare) {
        page.grid.checked = false;
    }
    scenery.show(mapSquare, box.min.y, page.grid.checked);

    const meshes = meshesOf(current);
    page.status.textContent = `${describe(current, label)}: ${triangleCount(meshes).toLocaleString()} triangles in `
        + `${meshes.length.toLocaleString()} primitives, ${contents}.`;
}

function failed(label: string, failure: unknown): void {
    page.status.textContent = `${label} could not be read: ${failure instanceof Error ? failure.message : failure}`;
}

/**
 * Opens a file's bytes.
 *
 * @param path where the file is served from, which the textures it refers to by relative paths
 *     are resolved against. A dropped file has no path, and is given one beside the exported
 *     files, so that it finds the same textures.
 */
async function open(data: ArrayBuffer, label: string, path: string): Promise<void> {
    try {
        const gltf = await loader.parseAsync(data, path);
        completeMorphTargets(gltf.scene);
        show(gltf.scene, gltf.animations, label, `${gltf.animations.length} animations`);
    } catch (failure) {
        failed(label, failure);
    }
}

/**
 * Opens a map square by its description, which names its ground and every location on it.
 */
async function openMapSquare(url: string, label: string): Promise<void> {
    try {
        const loaded = await loadMapSquare(url, loader);
        const contents = `${loaded.placements} locations of ${loaded.kinds} kinds`
            + (loaded.missing.length > 0 ? `, ${loaded.missing.length} placements missing` : '');
        show(loaded.root, loaded.clips, label, contents);
    } catch (failure) {
        failed(label, failure);
    }
}

function servedFrom(path: string): string {
    return path.slice(0, path.lastIndexOf('/') + 1);
}

async function openExported(file: ExportedFile): Promise<void> {
    const label = `${SINGULAR[file.kind]} ${shortName(file)}`;
    page.status.textContent = `Loading ${label}...`;
    if (file.kind === 'mapsquares') {
        await openMapSquare(file.path, label);
    } else {
        const response = await fetch(file.path);
        await open(await response.arrayBuffer(), label, servedFrom(file.path));
    }
}

async function start(): Promise<void> {
    const exported = await fetchExported();
    page.empty.hidden = exported.length > 0;
    const first = listExported(page.files, exported, file => void openExported(file));
    first?.click();
}

bindBackdrop(page.stage, page.backdrop);
bindDrop(page.stage, (data, name) => void open(data, name, DROPPED_PATH));
bindFocus(page.focus, () => current,
    (node, again) => {
        const side = SIDES[again % SIDES.length];
        frameFrom(stage.camera, stage.controls, lighting, baseBox(node), side.direction);
        page.status.textContent = `Looking at ${node.name} from ${side.name}.`;
    },
    name => {
        page.status.textContent = `Nothing open is named ${name}.`;
    });
page.wire.addEventListener('change', () => {
    if (current !== null) {
        setWireframe(current, page.wire.checked);
    }
});
page.grid.addEventListener('change', () => scenery.showGrid(page.grid.checked));

stage.start(seconds => player.advance(seconds));
void start();

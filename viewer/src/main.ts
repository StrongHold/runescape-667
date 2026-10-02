import { Object3D } from 'three';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';
import { createAnimationPlayer } from './animation.ts';
import { bindBackdrop } from './backdrop.ts';
import { bindDrop } from './drop.ts';
import { fetchExported, listExported, shortName, SINGULAR, type ExportedFile } from './exported.ts';
import { describe, isSquare, placements } from './extras.ts';
import { frame } from './framing.ts';
import { createLighting } from './lighting.ts';
import { baseBox, isTransparent, meshesOf, setWireframe, triangleCount } from './meshes.ts';
import { readPage } from './page.ts';
import { createScenery } from './scenery.ts';
import { createStage } from './stage.ts';

const page = readPage();
const stage = createStage(page.stage);
const lighting = createLighting(stage.scene);
const scenery = createScenery(stage.scene);
const player = createAnimationPlayer(page.animation, page.play);
const loader = new GLTFLoader();
let current: Object3D | null = null;

async function open(data: ArrayBuffer, label: string): Promise<void> {
    try {
        const gltf = await loader.parseAsync(data, '');
        if (current !== null) {
            stage.scene.remove(current);
        }
        current = gltf.scene;
        const square = isSquare(current);
        for (const mesh of meshesOf(current)) {
            mesh.castShadow = !isTransparent(mesh);
            mesh.receiveShadow = square;
        }
        stage.scene.add(current);
        player.load(current, gltf.animations);
        setWireframe(current, page.wire.checked);

        const box = baseBox(current);
        frame(stage.camera, stage.controls, lighting, box, square);
        if (square) {
            page.grid.checked = false;
        }
        scenery.show(square, box.min.y, page.grid.checked);

        const meshes = meshesOf(current);
        const contents = square
            ? `${placements(current)} locations`
            : `${gltf.animations.length} animations`;
        page.status.textContent = `${describe(current, label)}: ${triangleCount(meshes).toLocaleString()} triangles in `
            + `${meshes.length.toLocaleString()} primitives, ${contents}.`;
    } catch (failure) {
        page.status.textContent = `${label} could not be read: ${failure instanceof Error ? failure.message : failure}`;
    }
}

async function openExported(file: ExportedFile): Promise<void> {
    page.status.textContent = `Loading ${SINGULAR[file.kind]} ${shortName(file)}...`;
    const response = await fetch(file.path);
    await open(await response.arrayBuffer(), `${SINGULAR[file.kind]} ${file.name}`);
}

async function start(): Promise<void> {
    const exported = await fetchExported();
    page.empty.hidden = exported.length > 0;
    const first = listExported(page.files, exported, file => void openExported(file));
    first?.click();
}

bindBackdrop(page.stage, page.backdrop);
bindDrop(page.stage, (data, name) => void open(data, name));
page.wire.addEventListener('change', () => {
    if (current !== null) {
        setWireframe(current, page.wire.checked);
    }
});
page.grid.addEventListener('change', () => scenery.showGrid(page.grid.checked));

stage.start(seconds => player.advance(seconds));
void start();

import {
    AnimationAction,
    AnimationClip,
    AnimationMixer,
    Box3,
    Clock,
    DirectionalLight,
    GridHelper,
    HemisphereLight,
    Mesh,
    Object3D,
    PerspectiveCamera,
    Scene,
    Vector3,
    WebGLRenderer
} from 'three';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import type { ExportedFile } from './exported.ts';

function element<T extends HTMLElement>(id: string): T {
    const found = document.getElementById(id);
    if (found === null) {
        throw new Error(`The page has no element #${id}.`);
    }
    return found as T;
}

const stage = element<HTMLDivElement>('stage');
const files = element<HTMLElement>('files');
const empty = element<HTMLElement>('empty');
const picker = element<HTMLSelectElement>('animation');
const play = element<HTMLButtonElement>('play');
const wire = element<HTMLInputElement>('wire');
const gridToggle = element<HTMLInputElement>('grid');
const status = element<HTMLElement>('status');

const renderer = new WebGLRenderer({ antialias: true, alpha: true });
renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
stage.appendChild(renderer.domElement);

const scene = new Scene();
const camera = new PerspectiveCamera(40, 1, 0.01, 500);
const controls = new OrbitControls(camera, renderer.domElement);
controls.enableDamping = true;

scene.add(new HemisphereLight(0xffffff, 0x445566, 1.6));
const sun = new DirectionalLight(0xffffff, 1.8);
sun.position.set(3, 5, 4);
scene.add(sun);

/** One square of the grid is one tile, which the exporter makes one metre. */
const grid = new GridHelper(10, 10, 0x8899aa, 0x8899aa);
grid.material.transparent = true;
grid.material.opacity = 0.35;
scene.add(grid);

const loader = new GLTFLoader();
const clock = new Clock();
let current: Object3D | null = null;
let mixer: AnimationMixer | null = null;
let clips: AnimationClip[] = [];
let action: AnimationAction | null = null;

function resize(): void {
    const width = stage.clientWidth;
    const height = stage.clientHeight;
    renderer.setSize(width, height, false);
    camera.aspect = width / Math.max(height, 1);
    camera.updateProjectionMatrix();
}

function meshesOf(object: Object3D): Mesh[] {
    const meshes: Mesh[] = [];
    object.traverse(node => {
        if (node instanceof Mesh) {
            meshes.push(node);
        }
    });
    return meshes;
}

function setWireframe(on: boolean): void {
    if (current === null) {
        return;
    }
    for (const mesh of meshesOf(current)) {
        for (const material of [mesh.material].flat()) {
            if ('wireframe' in material) {
                material.wireframe = on;
            }
        }
    }
}

function frame(object: Object3D): void {
    const box = new Box3().setFromObject(object);
    const size = box.getSize(new Vector3());
    const centre = box.getCenter(new Vector3());
    const largest = Math.max(size.x, size.y, size.z);
    const reach = largest > 0 ? largest : 1;
    controls.target.copy(centre);
    camera.position.set(centre.x + reach * 1.1, centre.y + reach * 0.6, centre.z + reach * 1.3);
    camera.near = reach / 500;
    camera.far = reach * 50;
    camera.updateProjectionMatrix();
    grid.position.y = box.min.y;
}

function choose(index: number): void {
    mixer?.stopAllAction();
    action = mixer !== null && clips[index] !== undefined ? mixer.clipAction(clips[index]) : null;
    action?.play();
    play.textContent = 'Pause';
    play.hidden = action === null;
}

function listAnimations(animations: AnimationClip[]): void {
    clips = animations;
    picker.replaceChildren(...clips.map((clip, index) =>
        new Option(`${clip.name} (${clip.duration.toFixed(2)} s)`, String(index))));
    picker.hidden = clips.length === 0;
    choose(0);
}

/** What the exporter wrote about the model into the node, such as an NPC's name. */
function describe(object: Object3D, label: string): string {
    let named = label;
    object.traverse(node => {
        const extras = node.userData;
        if (typeof extras.name === 'string' && typeof extras.npc === 'number') {
            named = `NPC ${extras.npc}, ${extras.name}`;
        }
    });
    return named;
}

async function open(data: ArrayBuffer, label: string): Promise<void> {
    try {
        const gltf = await loader.parseAsync(data, '');
        if (current !== null) {
            scene.remove(current);
        }
        current = gltf.scene;
        scene.add(current);
        mixer = new AnimationMixer(current);
        listAnimations(gltf.animations);
        setWireframe(wire.checked);
        frame(current);

        const meshes = meshesOf(current);
        const triangles = meshes.reduce((sum, mesh) => {
            const geometry = mesh.geometry;
            return sum + (geometry.index?.count ?? geometry.attributes.position.count) / 3;
        }, 0);
        status.textContent = `${describe(current, label)}: ${triangles} triangles in ${meshes.length} primitives, `
            + `${gltf.animations.length} animations.`;
    } catch (failure) {
        status.textContent = `${label} could not be read: ${failure instanceof Error ? failure.message : failure}`;
    }
}

async function openExported(file: ExportedFile, button: HTMLButtonElement): Promise<void> {
    for (const other of files.querySelectorAll('.file')) {
        other.setAttribute('aria-pressed', String(other === button));
    }
    const response = await fetch(file.path);
    await open(await response.arrayBuffer(), `${file.kind === 'npcs' ? 'NPC' : 'Model'} ${file.name}`);
}

function kilobytes(bytes: number): string {
    return `${Math.round(bytes / 1024).toLocaleString()} KB`;
}

async function listExported(): Promise<void> {
    const response = await fetch('/exports/index.json');
    const exported: ExportedFile[] = response.ok ? await response.json() : [];
    empty.hidden = exported.length > 0;

    const headings: Record<ExportedFile['kind'], string> = { models: 'Models', npcs: 'NPCs' };
    let first: (() => Promise<void>) | null = null;

    for (const kind of ['npcs', 'models'] as const) {
        const ofKind = exported.filter(file => file.kind === kind);
        if (ofKind.length > 0) {
            const heading = document.createElement('h2');
            heading.textContent = headings[kind];
            files.append(heading);
        }

        for (const file of ofKind) {
            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'file';
            button.innerHTML = `<span></span><span class="size"></span>`;
            button.children[0].textContent = file.name.replace(/\.glb$/, '');
            button.children[1].textContent = kilobytes(file.bytes);
            button.addEventListener('click', () => void openExported(file, button));
            files.append(button);
            first ??= () => openExported(file, button);
        }
    }

    await first?.();
}

picker.addEventListener('change', () => choose(Number(picker.value)));
play.addEventListener('click', () => {
    if (action !== null) {
        action.paused = !action.paused;
        play.textContent = action.paused ? 'Play' : 'Pause';
    }
});
wire.addEventListener('change', () => setWireframe(wire.checked));
gridToggle.addEventListener('change', () => {
    grid.visible = gridToggle.checked;
});

stage.addEventListener('dragover', event => {
    event.preventDefault();
    stage.classList.add('dropping');
});
stage.addEventListener('dragleave', () => stage.classList.remove('dropping'));
stage.addEventListener('drop', event => {
    event.preventDefault();
    stage.classList.remove('dropping');
    const dropped = event.dataTransfer?.files[0];
    if (dropped !== undefined) {
        void dropped.arrayBuffer().then(data => open(data, dropped.name));
    }
});

new ResizeObserver(resize).observe(stage);
resize();
renderer.setAnimationLoop(() => {
    mixer?.update(clock.getDelta());
    controls.update();
    renderer.render(scene, camera);
});

void listExported();

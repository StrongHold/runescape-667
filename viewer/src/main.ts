import {
    AnimationAction,
    AnimationClip,
    AnimationMixer,
    Box3,
    Clock,
    DirectionalLight,
    PCFSoftShadowMap,
    PlaneGeometry,
    ShadowMaterial,
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
import type { ExportedFile, ExportKind } from './exported.ts';

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
const backdrop = element<HTMLSelectElement>('backdrop');
const status = element<HTMLElement>('status');

const renderer = new WebGLRenderer({ antialias: true, alpha: true });
renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
renderer.shadowMap.enabled = true;
renderer.shadowMap.type = PCFSoftShadowMap;
stage.appendChild(renderer.domElement);

const scene = new Scene();
const camera = new PerspectiveCamera(40, 1, 0.01, 500);
const controls = new OrbitControls(camera, renderer.domElement);
controls.enableDamping = true;

scene.add(new HemisphereLight(0xffffff, 0x445566, 1.6));
const sun = new DirectionalLight(0xffffff, 1.8);
sun.castShadow = true;
sun.shadow.bias = -0.0005;
scene.add(sun, sun.target);

/**
 * Where the sun is, as a direction from what it lights: high in the south east, so the shadows
 * fall north west and the faces of a model that look at the camera are lit.
 */
const SUN_DIRECTION = new Vector3(1.5, 3, 2).normalize();

/**
 * How many texels a side the shadow map has: enough for one model, and twice that for a map
 * square, which is 64 m across and would otherwise cast shadows a hand's width wide.
 */
const MODEL_SHADOW_SIZE = 2048;
const SQUARE_SHADOW_SIZE = 4096;

/**
 * The game's own ground is y = 0: a model stands on it as it was authored, and the exporter keeps
 * that. So the grid and the ground that catches shadows sit there, not under the lowest vertex.
 */
const GROUND = 0;

/** One square of the grid is one tile, which the exporter makes one metre. */
const grid = tileGrid(10, 0.35);
grid.position.y = GROUND;
scene.add(grid);

/**
 * A map square is 64 tiles across, its south west corner at the origin and north along -z. Its
 * ground is not flat, so this grid is hidden until asked for, and then drawn faintly at the
 * height of the square's lowest point.
 */
const SQUARE_TILES = 64;
const squareGrid = tileGrid(SQUARE_TILES, 0.2);
squareGrid.position.set(SQUARE_TILES / 2, GROUND, -SQUARE_TILES / 2);
squareGrid.visible = false;
scene.add(squareGrid);

function tileGrid(tiles: number, opacity: number): GridHelper {
    const helper = new GridHelper(tiles, tiles, 0x8899aa, 0x8899aa);
    helper.material.transparent = true;
    helper.material.opacity = opacity;
    return helper;
}

const ground = new Mesh(new PlaneGeometry(40, 40), new ShadowMaterial({ opacity: 0.3 }));
ground.rotation.x = -Math.PI / 2;
ground.position.y = GROUND;
ground.receiveShadow = true;
scene.add(ground);

const loader = new GLTFLoader();
const clock = new Clock();
let current: Object3D | null = null;
let showingSquare = false;
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

/**
 * The box the model fills in its base pose. three.js widens a box for morph targets by adding the
 * largest change of any vertex to the extreme of every other, which for an animated NPC reaches far
 * below its feet, so the box is taken from the base positions alone.
 */
function baseBox(object: Object3D): Box3 {
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

/**
 * Points the camera at what is open and casts the sun's shadow over it. A model is seen from the
 * front and to one side, and its shadow falls on the ground around it. A map square is seen from
 * high over its south edge, looking north and down, as the game sees it, and its own ground takes
 * every shadow, so the shadow is fitted to the square alone.
 */
function frame(object: Object3D, square: boolean): void {
    const box = baseBox(object);
    const centre = box.getCenter(new Vector3());
    controls.target.copy(centre);

    if (square) {
        const radius = box.getSize(new Vector3()).length() / 2;
        camera.position.set(centre.x, centre.y + radius * 1.3, centre.z + radius * 1.05);
        camera.near = radius / 500;
        camera.far = radius * 40;
        castShadow(centre, radius, radius, SQUARE_SHADOW_SIZE);
    } else {
        const size = box.getSize(new Vector3());
        const largest = Math.max(size.x, size.y, size.z);
        const reach = largest > 0 ? largest : 1;
        camera.position.set(centre.x + reach * 1.1, centre.y + reach * 0.6, centre.z + reach * 1.3);
        camera.near = reach / 500;
        camera.far = reach * 50;
        castShadow(centre, reach, reach * 1.5, MODEL_SHADOW_SIZE);
    }
    camera.updateProjectionMatrix();
}

/**
 * Puts the sun up the sky from what is open, and fits its shadow camera to a box around it.
 *
 * @param reach how far what is open stretches from its centre, which the sun stands back from.
 * @param extent how far the shadow camera reaches either side of the centre.
 * @param size how many texels a side the shadow map has.
 */
function castShadow(centre: Vector3, reach: number, extent: number, size: number): void {
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

/**
 * Shows the grid and the ground that catches shadows that suit what is open. A square has ground
 * of its own, which takes the shadows instead.
 */
function setScenery(square: boolean, box: Box3): void {
    showingSquare = square;
    ground.visible = !square;
    squareGrid.position.y = box.min.y;
    if (square) {
        gridToggle.checked = false;
    }
    showGrid(gridToggle.checked);
}

function showGrid(on: boolean): void {
    grid.visible = on && !showingSquare;
    squareGrid.visible = on && showingSquare;
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
        } else if (typeof extras.squareX === 'number' && typeof extras.squareZ === 'number') {
            named = `Square ${extras.squareX}_${extras.squareZ}`;
        }
    });
    return named;
}

/**
 * Whether a file is a map square, which the exporter marks on its root node, so that a square
 * dropped on the page is shown as one too.
 */
function isSquare(object: Object3D): boolean {
    let square = false;
    object.traverse(node => {
        if (typeof node.userData.squareX === 'number') {
            square = true;
        }
    });
    return square;
}

/** How many locations a square places, each of which is a node of its own. */
function placements(object: Object3D): number {
    let count = 0;
    object.traverse(node => {
        if (typeof node.userData.loc === 'number') {
            count++;
        }
    });
    return count;
}

async function open(data: ArrayBuffer, label: string): Promise<void> {
    try {
        const gltf = await loader.parseAsync(data, '');
        if (current !== null) {
            scene.remove(current);
        }
        current = gltf.scene;
        const square = isSquare(current);
        for (const mesh of meshesOf(current)) {
            mesh.castShadow = !isTransparent(mesh);
            mesh.receiveShadow = square;
        }
        scene.add(current);
        mixer = new AnimationMixer(current);
        listAnimations(gltf.animations);
        setWireframe(wire.checked);
        frame(current, square);
        setScenery(square, baseBox(current));

        const meshes = meshesOf(current);
        const triangles = meshes.reduce((sum, mesh) => {
            const geometry = mesh.geometry;
            return sum + (geometry.index?.count ?? geometry.attributes.position.count) / 3;
        }, 0);
        const contents = square
            ? `${placements(current)} locations`
            : `${gltf.animations.length} animations`;
        status.textContent = `${describe(current, label)}: ${triangles.toLocaleString()} triangles in `
            + `${meshes.length.toLocaleString()} primitives, ${contents}.`;
    } catch (failure) {
        status.textContent = `${label} could not be read: ${failure instanceof Error ? failure.message : failure}`;
    }
}

/**
 * Whether a mesh is blended over what is behind it, such as water or a layer of a ground
 * texture. Such a mesh casts no shadow, as light passes through it.
 */
function isTransparent(mesh: Mesh): boolean {
    return [mesh.material].flat().some(material => material.transparent);
}

/** What one file of each kind is called on the page. */
const SINGULAR: Record<ExportKind, string> = { models: 'Model', npcs: 'NPC', squares: 'Square' };

async function openExported(file: ExportedFile, button: HTMLButtonElement): Promise<void> {
    for (const other of files.querySelectorAll('.file')) {
        other.setAttribute('aria-pressed', String(other === button));
    }
    status.textContent = `Loading ${SINGULAR[file.kind]} ${file.name.replace(/\.glb$/, '')}...`;
    const response = await fetch(file.path);
    await open(await response.arrayBuffer(), `${SINGULAR[file.kind]} ${file.name}`);
}

function kilobytes(bytes: number): string {
    return `${Math.round(bytes / 1024).toLocaleString()} KB`;
}

async function listExported(): Promise<void> {
    const response = await fetch('/exports/index.json');
    const exported: ExportedFile[] = response.ok ? await response.json() : [];
    empty.hidden = exported.length > 0;

    const headings: Record<ExportKind, string> = { models: 'Models', npcs: 'NPCs', squares: 'Map squares' };
    let first: (() => Promise<void>) | null = null;

    for (const kind of ['npcs', 'models', 'squares'] as const) {
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

/** The viewer's choice of backdrop, kept in this browser between visits where it may be. */
const BACKDROP_KEY = 'viewer.backdrop';

function setBackdrop(name: string): void {
    stage.dataset.backdrop = name;
    backdrop.value = name;
    try {
        localStorage.setItem(BACKDROP_KEY, name);
    } catch {
        /* empty */
    }
}

function rememberedBackdrop(): string {
    try {
        return localStorage.getItem(BACKDROP_KEY) ?? 'theme';
    } catch {
        return 'theme';
    }
}

backdrop.addEventListener('change', () => setBackdrop(backdrop.value));
setBackdrop(rememberedBackdrop());

picker.addEventListener('change', () => choose(Number(picker.value)));
play.addEventListener('click', () => {
    if (action !== null) {
        action.paused = !action.paused;
        play.textContent = action.paused ? 'Play' : 'Pause';
    }
});
wire.addEventListener('change', () => setWireframe(wire.checked));
gridToggle.addEventListener('change', () => showGrid(gridToggle.checked));

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

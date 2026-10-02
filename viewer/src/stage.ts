import { PCFShadowMap, PerspectiveCamera, Scene, WebGLRenderer } from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';

/**
 * The renderer, the scene it draws, the camera and the orbit controls, sized to the element they
 * draw into.
 */
export interface Stage {
    readonly scene: Scene;
    readonly camera: PerspectiveCamera;
    readonly controls: OrbitControls;
    /** Draws every frame from now on, calling back first with the seconds since the last one. */
    readonly start: (beforeFrame: (seconds: number) => void) => void;
}

export function createStage(container: HTMLElement): Stage {
    const renderer = new WebGLRenderer({ antialias: true, alpha: true });
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    renderer.shadowMap.enabled = true;
    renderer.shadowMap.type = PCFShadowMap;
    container.appendChild(renderer.domElement);

    const scene = new Scene();
    const camera = new PerspectiveCamera(40, 1, 0.01, 500);
    const controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;

    const resize = (): void => {
        const width = container.clientWidth;
        const height = container.clientHeight;
        renderer.setSize(width, height, false);
        camera.aspect = width / Math.max(height, 1);
        camera.updateProjectionMatrix();
    };
    new ResizeObserver(resize).observe(container);
    resize();

    return {
        scene,
        camera,
        controls,
        start: beforeFrame => {
            let last = performance.now();
            renderer.setAnimationLoop(now => {
                beforeFrame((now - last) / 1000);
                last = now;
                controls.update();
                renderer.render(scene, camera);
            });
        }
    };
}

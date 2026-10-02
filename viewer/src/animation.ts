import { AnimationAction, AnimationClip, AnimationMixer, Object3D } from 'three';

/**
 * Plays the animations of what is open: one at a time, chosen from a list, with a button that
 * pauses it.
 */
export interface AnimationPlayer {
    /** Takes over the animations of a newly opened object, and plays the first. */
    readonly load: (root: Object3D, clips: readonly AnimationClip[]) => void;
    /** Moves every playing animation on by some seconds. */
    readonly advance: (seconds: number) => void;
}

export function createAnimationPlayer(picker: HTMLSelectElement, play: HTMLButtonElement): AnimationPlayer {
    let mixer: AnimationMixer | null = null;
    let clips: readonly AnimationClip[] = [];
    let action: AnimationAction | null = null;

    const choose = (index: number): void => {
        mixer?.stopAllAction();
        const clip = clips[index];
        action = mixer !== null && clip !== undefined ? mixer.clipAction(clip) : null;
        action?.play();
        play.textContent = 'Pause';
        play.hidden = action === null;
    };

    picker.addEventListener('change', () => choose(Number(picker.value)));
    play.addEventListener('click', () => {
        if (action !== null) {
            action.paused = !action.paused;
            play.textContent = action.paused ? 'Play' : 'Pause';
        }
    });

    return {
        load: (root, loaded) => {
            mixer = new AnimationMixer(root);
            clips = loaded;
            picker.replaceChildren(...clips.map((clip, index) =>
                new Option(`${clip.name} (${clip.duration.toFixed(2)} s)`, String(index))));
            picker.hidden = clips.length === 0;
            choose(0);
        },
        advance: seconds => mixer?.update(seconds)
    };
}

import { AnimationAction, AnimationClip, AnimationMixer, Object3D, PropertyBinding } from 'three';

/**
 * Plays the animations of what is open, chosen from a list, with a button that pauses them.
 *
 * An NPC's animations are its ways of moving, and only one plays at a time. A map square's are
 * the sequences of its locations, which all play at once in the game, so the list offers every
 * animation together, and starts there for a map square.
 */
export interface AnimationPlayer {
    /**
     * Takes over the animations of a newly opened object.
     *
     * @param together whether the animations play at once, as a map square's do, which is offered
     *     and started with, rather than one at a time, as an NPC's do.
     */
    readonly load: (root: Object3D, clips: readonly AnimationClip[], together: boolean) => void;
    /** Moves every playing animation on by some seconds. */
    readonly advance: (seconds: number) => void;
}

/** The choice in the list that plays every animation together. */
const EVERY = 'every';

export function createAnimationPlayer(picker: HTMLSelectElement, play: HTMLButtonElement): AnimationPlayer {
    let mixer: AnimationMixer | null = null;
    let root: Object3D | null = null;
    let clips: readonly AnimationClip[] = [];
    let actions: AnimationAction[] = [];

    const start = (chosen: readonly AnimationClip[]): void => {
        const playing = mixer;
        const played = root;
        playing?.stopAllAction();
        for (const action of actions) {
            playing?.uncacheClip(action.getClip());
        }
        actions = playing !== null && played !== null ? chosen.flatMap(clip => staggered(playing, played, clip)) : [];
        for (const action of actions) {
            action.play();
        }
        play.textContent = 'Pause';
        play.hidden = actions.length === 0;
    };

    const choose = (value: string): void => {
        if (value === EVERY) {
            start(clips);
        } else {
            const clip = clips[Number(value)];
            start(clip === undefined ? [] : [clip]);
        }
    };

    picker.addEventListener('change', () => choose(picker.value));
    play.addEventListener('click', () => {
        const paused = actions.some(action => action.paused);
        for (const action of actions) {
            action.paused = !paused;
        }
        play.textContent = paused ? 'Pause' : 'Play';
    });

    return {
        load: (opened, loaded, together) => {
            root = opened;
            mixer = new AnimationMixer(opened);
            clips = loaded;
            const options = clips.map((clip, index) =>
                new Option(`${clip.name} (${clip.duration.toFixed(2)} s)`, String(index)));
            if (together && clips.length > 1) {
                options.unshift(new Option(`Every animation (${clips.length})`, EVERY));
            }
            picker.replaceChildren(...options);
            picker.hidden = clips.length === 0;
            picker.value = together && clips.length > 1 ? EVERY : '0';
            choose(picker.value);
        },
        advance: seconds => mixer?.update(seconds)
    };
}

/**
 * One action for each node a clip moves, so that a node the exporter marks as starting at a
 * random frame can start somewhere of its own, the way the game staggers the flags and fires of
 * one kind. A node not so marked keeps the clip's own timing, and shares one action with every
 * other such node.
 */
function staggered(mixer: AnimationMixer, root: Object3D, clip: AnimationClip): AnimationAction[] {
    const inStep = clip.tracks.filter(track => !startsAtRandom(root, track.name));
    const randomised = clip.tracks.filter(track => startsAtRandom(root, track.name));

    const actions = randomised.map(track => {
        const action = mixer.clipAction(new AnimationClip(`${clip.name} ${track.name}`, clip.duration, [track]));
        action.time = Math.random() * clip.duration;
        return action;
    });
    if (inStep.length > 0) {
        actions.push(mixer.clipAction(new AnimationClip(clip.name, clip.duration, inStep)));
    }
    return actions;
}

function startsAtRandom(root: Object3D, trackName: string): boolean {
    const node = PropertyBinding.findNode(root, PropertyBinding.parseTrackName(trackName).nodeName);
    return node instanceof Object3D && node.userData.randomStartFrame === true;
}

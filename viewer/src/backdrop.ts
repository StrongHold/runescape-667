/** The viewer's choice of backdrop, kept in this browser between visits where it may be. */
const BACKDROP_KEY = 'viewer.backdrop';
const DEFAULT_BACKDROP = 'theme';

/**
 * Lets the backdrop behind what is open be chosen, and keeps the choice.
 */
export function bindBackdrop(stage: HTMLElement, picker: HTMLSelectElement): void {
    const set = (name: string): void => {
        stage.dataset.backdrop = name;
        picker.value = name;
        try {
            localStorage.setItem(BACKDROP_KEY, name);
        } catch {
            /* empty */
        }
    };

    picker.addEventListener('change', () => set(picker.value));
    set(rememberedBackdrop());
}

function rememberedBackdrop(): string {
    try {
        return localStorage.getItem(BACKDROP_KEY) ?? DEFAULT_BACKDROP;
    } catch {
        return DEFAULT_BACKDROP;
    }
}

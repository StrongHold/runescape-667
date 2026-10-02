/**
 * The kinds of file the export module writes, each in a directory of its own under its build
 * directory: single models, NPCs with their animations, and map squares, which are listed by
 * their descriptions.
 */
export type ExportKind = 'models' | 'npcs' | 'mapsquares';

/** What a listed file of each kind ends with. */
export const LISTED_EXTENSION: Readonly<Record<ExportKind, string>> = { models: '.glb', npcs: '.glb', mapsquares: '.json' };

/**
 * One file the export module has written, as the viewer's server lists it.
 */
export interface ExportedFile {
    readonly kind: ExportKind;
    readonly name: string;
    readonly path: string;
    readonly bytes: number;
}

/** What one file of each kind is called on the page. */
export const SINGULAR: Readonly<Record<ExportKind, string>> = { models: 'Model', npcs: 'NPC', mapsquares: 'Map square' };

const HEADINGS: Readonly<Record<ExportKind, string>> = { models: 'Models', npcs: 'NPCs', mapsquares: 'Map squares' };

/** The order the kinds are listed in. */
const LISTED: readonly ExportKind[] = ['npcs', 'models', 'mapsquares'];

/**
 * Everything the export module has written, as the viewer's server lists it.
 */
export async function fetchExported(): Promise<ExportedFile[]> {
    const response = await fetch('/exports/index.json');
    return response.ok ? await response.json() : [];
}

/** The name of a file as it is shown, without its extension. */
export function shortName(file: ExportedFile): string {
    return file.name.replace(/\.(glb|json)$/, '');
}

/**
 * Lists the exported files under a heading for each kind, as buttons that open them.
 *
 * @returns the first button listed, which is pressed to open something at the start.
 */
export function listExported(into: HTMLElement, exported: readonly ExportedFile[],
                             onChosen: (file: ExportedFile) => void): HTMLButtonElement | undefined {
    let first: HTMLButtonElement | undefined;

    for (const kind of LISTED) {
        const ofKind = exported.filter(file => file.kind === kind);
        if (ofKind.length > 0) {
            const heading = document.createElement('h2');
            heading.textContent = HEADINGS[kind];
            into.append(heading);
        }

        for (const file of ofKind) {
            const button = fileButton(file);
            button.addEventListener('click', () => {
                press(into, button);
                onChosen(file);
            });
            into.append(button);
            first ??= button;
        }
    }

    return first;
}

function fileButton(file: ExportedFile): HTMLButtonElement {
    const button = document.createElement('button');
    button.type = 'button';
    button.className = 'file';
    button.innerHTML = `<span></span><span class="size"></span>`;
    button.children[0].textContent = shortName(file);
    button.children[1].textContent = kilobytes(file.bytes);
    return button;
}

function press(list: HTMLElement, button: HTMLButtonElement): void {
    for (const other of list.querySelectorAll('.file')) {
        other.setAttribute('aria-pressed', String(other === button));
    }
}

function kilobytes(bytes: number): string {
    return `${Math.round(bytes / 1024).toLocaleString()} KB`;
}

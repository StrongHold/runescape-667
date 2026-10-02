/**
 * The elements of the page that the viewer drives, found once so that a missing one fails at
 * start rather than on first use.
 */
export interface Page {
    readonly stage: HTMLDivElement;
    readonly files: HTMLElement;
    readonly empty: HTMLElement;
    readonly animation: HTMLSelectElement;
    readonly play: HTMLButtonElement;
    readonly wire: HTMLInputElement;
    readonly grid: HTMLInputElement;
    readonly backdrop: HTMLSelectElement;
    readonly status: HTMLElement;
}

export function readPage(): Page {
    return {
        stage: element<HTMLDivElement>('stage'),
        files: element<HTMLElement>('files'),
        empty: element<HTMLElement>('empty'),
        animation: element<HTMLSelectElement>('animation'),
        play: element<HTMLButtonElement>('play'),
        wire: element<HTMLInputElement>('wire'),
        grid: element<HTMLInputElement>('grid'),
        backdrop: element<HTMLSelectElement>('backdrop'),
        status: element<HTMLElement>('status')
    };
}

function element<T extends HTMLElement>(id: string): T {
    const found = document.getElementById(id);
    if (found === null) {
        throw new Error(`The page has no element #${id}.`);
    }
    return found as T;
}

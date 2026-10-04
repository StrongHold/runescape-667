/**
 * Lets files be dropped on an element, and hands them over: a glTF file with the buffer and any
 * textures dropped with it.
 */
export function bindDrop(target: HTMLElement, onDropped: (files: readonly File[]) => void): void {
    target.addEventListener('dragover', event => {
        event.preventDefault();
        target.classList.add('dropping');
    });
    target.addEventListener('dragleave', () => target.classList.remove('dropping'));
    target.addEventListener('drop', event => {
        event.preventDefault();
        target.classList.remove('dropping');
        const dropped = [...event.dataTransfer?.files ?? []];
        if (dropped.length > 0) {
            onDropped(dropped);
        }
    });
}

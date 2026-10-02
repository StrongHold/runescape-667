/**
 * Lets a file be dropped on an element, and hands over what it holds.
 */
export function bindDrop(target: HTMLElement, onDropped: (data: ArrayBuffer, name: string) => void): void {
    target.addEventListener('dragover', event => {
        event.preventDefault();
        target.classList.add('dropping');
    });
    target.addEventListener('dragleave', () => target.classList.remove('dropping'));
    target.addEventListener('drop', event => {
        event.preventDefault();
        target.classList.remove('dropping');
        const dropped = event.dataTransfer?.files[0];
        if (dropped !== undefined) {
            void dropped.arrayBuffer().then(data => onDropped(data, dropped.name));
        }
    });
}

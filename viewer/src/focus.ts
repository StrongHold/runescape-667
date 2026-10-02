import { Object3D, PropertyBinding } from 'three';

/**
 * Lets a node of what is open be found by name, so that one location on a map square can be
 * looked at without hunting for it among thousands. The first node whose name starts with the
 * text typed is chosen when Enter is pressed, and the page says which one it was. Pressing Enter
 * again on the same text looks at the same node from its next side, so that a decoration on a
 * wall can be seen from in front of the wall. The loader rewrites a node's name so that an
 * animation track can name it, with an underscore for each space, and the text typed is
 * rewritten the same way before it is matched.
 *
 * @param onFound the node found, and how many times in a row it has been.
 */
export function bindFocus(input: HTMLInputElement, root: () => Object3D | null,
                          onFound: (node: Object3D, again: number) => void, onMissing: (name: string) => void): void {
    let last: Object3D | null = null;
    let again = 0;

    input.addEventListener('keydown', event => {
        if (event.key === 'Enter') {
            const opened = root();
            const found = opened === null ? null : nodeNamed(opened, input.value.trim());
            if (found === null) {
                onMissing(input.value.trim());
            } else {
                again = found === last ? again + 1 : 0;
                last = found;
                onFound(found, again);
            }
        }
    });
}

function nodeNamed(root: Object3D, typed: string): Object3D | null {
    const prefix = PropertyBinding.sanitizeNodeName(typed);
    let found: Object3D | null = null;
    if (prefix !== '') {
        root.traverse(node => {
            if (found === null && node.name.startsWith(prefix)) {
                found = node;
            }
        });
    }
    return found;
}

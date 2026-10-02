import { Object3D } from 'three';

/**
 * What the exporter wrote into a file's nodes, beyond what glTF itself says. Each exporter marks
 * its root node: an NPC with its id and name, and a map square with its position.
 */

/** What the file shows, such as "NPC 9, Hans", or the label given when it says nothing. */
export function describe(object: Object3D, label: string): string {
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
 * Whether a file is a map square, so that a square dropped on the page is shown as one too.
 */
export function isSquare(object: Object3D): boolean {
    let square = false;
    object.traverse(node => {
        if (typeof node.userData.squareX === 'number') {
            square = true;
        }
    });
    return square;
}

/** How many locations a square places, each of which is a node of its own. */
export function placements(object: Object3D): number {
    let count = 0;
    object.traverse(node => {
        if (typeof node.userData.loc === 'number') {
            count++;
        }
    });
    return count;
}

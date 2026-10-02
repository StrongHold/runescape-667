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
        } else if (typeof extras.mapSquareX === 'number' && typeof extras.mapSquareZ === 'number') {
            named = `Map square ${extras.mapSquareX}_${extras.mapSquareZ}`;
        }
    });
    return named;
}

/**
 * Whether a file is a map square, so that a map square dropped on the page is shown as one too.
 */
export function isMapSquare(object: Object3D): boolean {
    let mapSquare = false;
    object.traverse(node => {
        if (typeof node.userData.mapSquareX === 'number') {
            mapSquare = true;
        }
    });
    return mapSquare;
}

/** How many locations a map square places, each of which is a node of its own. */
export function placements(object: Object3D): number {
    let count = 0;
    object.traverse(node => {
        if (typeof node.userData.loc === 'number') {
            count++;
        }
    });
    return count;
}

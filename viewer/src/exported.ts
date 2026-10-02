/**
 * The kinds of file the export module writes, each in a directory of its own under its build
 * directory: single models, NPCs with their animations, and whole map squares.
 */
export type ExportKind = 'models' | 'npcs' | 'squares';

/**
 * One file the export module has written, as the viewer's server lists it.
 */
export interface ExportedFile {
    readonly kind: ExportKind;
    readonly name: string;
    readonly path: string;
    readonly bytes: number;
}

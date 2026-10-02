/**
 * One file the export module has written, as the viewer's server lists it.
 */
export interface ExportedFile {
    readonly kind: 'models' | 'npcs';
    readonly name: string;
    readonly path: string;
    readonly bytes: number;
}

import { createReadStream } from 'node:fs';
import { readdir, stat } from 'node:fs/promises';
import { extname, join, normalize, sep } from 'node:path';
import type { Plugin } from 'vite';
import type { ExportedFile, ExportKind } from '../src/exported.ts';

/**
 * The kinds of file the export module writes, each in a directory of its own under its build
 * directory.
 */
const KINDS: readonly ExportKind[] = ['models', 'npcs', 'squares'];

/** What is served from the export directory: the files, and the textures they refer to. */
const SERVED_TYPES: Readonly<Record<string, string>> = { '.glb': 'model/gltf-binary', '.png': 'image/png' };

/**
 * Serves what the export module has written, so the viewer lists every model, NPC and map square
 * without anything being copied, and the textures those files refer to by relative paths. The
 * list is read on every request, so a file exported while the viewer is open shows up on the
 * next reload.
 */
export function exportsServed(directory: string): Plugin {
    return {
        name: 'exports-served',
        configureServer(server) {
            server.middlewares.use('/exports/index.json', async (_request, response) => {
                response.setHeader('Content-Type', 'application/json');
                response.end(JSON.stringify(await listed(directory)));
            });

            server.middlewares.use('/exports/', (request, response, next) => {
                const wanted = normalize(join(directory, decodeURIComponent(request.url ?? '')));
                const type = SERVED_TYPES[extname(wanted)];
                if (!wanted.startsWith(directory + sep) || type === undefined) {
                    next();
                    return;
                }

                response.setHeader('Content-Type', type);
                createReadStream(wanted)
                    .on('error', () => {
                        response.statusCode = 404;
                        response.end();
                    })
                    .pipe(response);
            });
        }
    };
}

async function listed(directory: string): Promise<ExportedFile[]> {
    const files: ExportedFile[] = [];

    for (const kind of KINDS) {
        const names = await readdir(join(directory, kind)).catch(() => [] as string[]);
        for (const name of names.filter(name => name.endsWith('.glb'))) {
            const { size } = await stat(join(directory, kind, name));
            files.push({ kind, name, path: `/exports/${kind}/${encodeURIComponent(name)}`, bytes: size });
        }
    }

    return files.sort((a, b) => a.kind.localeCompare(b.kind) || byNumbers(a.name, b.name));
}

/**
 * Orders names by the numbers in them, so model 9 comes before model 10 and square 50_49 before
 * square 50_50.
 */
function byNumbers(a: string, b: string): number {
    return a.localeCompare(b, 'en', { numeric: true });
}

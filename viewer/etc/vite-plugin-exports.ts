import { createReadStream } from 'node:fs';
import { readdir, stat } from 'node:fs/promises';
import { join, normalize, sep } from 'node:path';
import type { Plugin } from 'vite';
import type { ExportedFile } from '../src/exported.ts';

/**
 * The kinds of file the export module writes, each in a directory of its own under its build
 * directory.
 */
const KINDS: readonly ExportedFile['kind'][] = ['models', 'npcs'];

/**
 * Serves what the export module has written, so the viewer lists every model and NPC without
 * anything being copied. The list is read on every request, so a file exported while the viewer
 * is open shows up on the next reload.
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
                if (!wanted.startsWith(directory + sep) || !wanted.endsWith('.glb')) {
                    next();
                    return;
                }

                response.setHeader('Content-Type', 'model/gltf-binary');
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

    return files.sort((a, b) => a.kind.localeCompare(b.kind) || idOf(a.name) - idOf(b.name));
}

function idOf(name: string): number {
    const id = Number.parseInt(name, 10);
    return Number.isNaN(id) ? Number.MAX_SAFE_INTEGER : id;
}

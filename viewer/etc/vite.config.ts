import { resolve } from 'node:path';
import { defineConfig } from 'vite';
import { exportsServed } from './vite-plugin-exports.ts';

const root = resolve(import.meta.dirname, '..');

export default defineConfig({
    root: resolve(root, 'src'),
    build: {
        outDir: resolve(root, 'build/dist'),
        emptyOutDir: true,
        // three.js on its own is about 650 kB once minified, and it is the whole of the bundle.
        chunkSizeWarningLimit: 1024
    },
    plugins: [
        exportsServed(resolve(root, '../export/build'))
    ]
});

import { readdir, readFile } from 'node:fs/promises';
import { join, relative, resolve } from 'node:path';
import { validateBytes, type ValidationMessage } from 'gltf-validator';

/**
 * Checks every .glb the export module has written against the Khronos glTF validator, or those
 * named on the command line, and fails on any error or warning.
 *
 * The validator has no command line of its own, so it is driven here. Every issue is counted by
 * its code, and the first few errors are printed in full with the part of the file they point at.
 */

const SEVERITIES = ['error', 'warning', 'info', 'hint'] as const;
const ERROR = 0;
const SHOWN_IN_FULL = 10;

const exportDirectory = resolve(import.meta.dirname, '../../export/build');
const named = process.argv.slice(2);
const files = named.length > 0 ? named : await exportedFiles(exportDirectory);

let failed = false;
for (const file of files) {
    const report = await validateBytes(new Uint8Array(await readFile(file)), { maxIssues: 0 });
    const issues = report.issues;
    console.log(`${relative(process.cwd(), file)}: ${issues.numErrors} errors, ${issues.numWarnings} warnings, `
        + `${issues.numInfos} infos, ${issues.numHints} hints`);

    for (const [code, count] of countedByCode(issues.messages)) {
        console.log(`  ${count} x ${code}`);
    }
    for (const message of issues.messages.filter(message => message.severity === ERROR).slice(0, SHOWN_IN_FULL)) {
        console.log(`  ${message.pointer ?? ''}: ${message.message}`);
    }

    failed ||= issues.numErrors > 0 || issues.numWarnings > 0;
}

if (files.length === 0) {
    console.log(`Nothing to check under ${exportDirectory}.`);
}
process.exitCode = failed ? 1 : 0;

async function exportedFiles(directory: string): Promise<string[]> {
    const entries = await readdir(directory, { withFileTypes: true, recursive: true }).catch(() => []);
    return entries
        .filter(entry => entry.isFile() && entry.name.endsWith('.glb'))
        .map(entry => join(entry.parentPath, entry.name))
        .sort();
}

function countedByCode(messages: readonly ValidationMessage[]): Map<string, number> {
    const counts = new Map<string, number>();
    for (const message of messages) {
        const key = `${SEVERITIES[message.severity]} ${message.code}`;
        counts.set(key, (counts.get(key) ?? 0) + 1);
    }
    return counts;
}

/**
 * The part of the Khronos glTF validator's report that the check reads. The package ships no
 * types of its own.
 */
declare module 'gltf-validator' {
    export interface ValidationMessage {
        readonly code: string;
        readonly message: string;
        readonly severity: 0 | 1 | 2 | 3;
        readonly pointer?: string;
    }

    export interface ValidationIssues {
        readonly numErrors: number;
        readonly numWarnings: number;
        readonly numInfos: number;
        readonly numHints: number;
        readonly messages: readonly ValidationMessage[];
    }

    export interface ValidationReport {
        readonly issues: ValidationIssues;
    }

    export interface ValidationOptions {
        readonly uri?: string;
        readonly maxIssues?: number;
        /** Fetches a resource the file refers to by a URI relative to itself. */
        readonly externalResourceFunction?: (uri: string) => Promise<Uint8Array>;
    }

    export function validateBytes(data: Uint8Array, options?: ValidationOptions): Promise<ValidationReport>;
}

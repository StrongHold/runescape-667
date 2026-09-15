package rs2.client.loading.library;

import java.io.File;

/**
 * Answers which file the client should load for a library.
 *
 * This interface is not part of the original client.
 */
@FunctionalInterface
public interface LibrarySource {

    /**
     * The file to load for this library, or null to load the one that was downloaded.
     */
    File libraryFor(String name);
}

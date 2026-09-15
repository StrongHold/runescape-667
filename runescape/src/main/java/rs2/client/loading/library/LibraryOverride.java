package rs2.client.loading.library;

import java.io.File;

/**
 * Loads a library written for this client in place of the one downloaded for it.
 *
 * No source is installed unless the host application installs one, and a source may decline any
 * library, so by default every library is loaded exactly as it was downloaded. A library is
 * offered at the point the client loads it rather than at startup, because a library that is
 * derived from a download cannot be prepared until that download has landed.
 *
 * This class is not part of the original client.
 */
public final class LibraryOverride {

    private static volatile LibrarySource source = name -> null;

    /**
     * Installs the source consulted for every library the client loads.
     */
    public static void supply(LibrarySource source) {
        LibraryOverride.source = source;
    }

    /**
     * Registers whichever file the source answers for this library, where it answers one.
     */
    public static void apply(String name) {
        File file = source.libraryFor(name);
        if (file != null) {
            LibraryManager.putLibrary(file, name);
        }
    }

    private LibraryOverride() {
        /* empty */
    }
}

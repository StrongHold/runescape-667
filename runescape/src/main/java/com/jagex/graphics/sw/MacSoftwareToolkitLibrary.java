package com.jagex.graphics.sw;

import rs2.client.loading.library.LibraryManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Supplies the software toolkit with a drawing surface on macOS.
 *
 * The toolkit asks JavaVM.framework for a surface of a version that framework no longer serves, so
 * on macOS it can never obtain one and the client falls back to the Java toolkit. The toolkit takes
 * only one symbol from that framework, so a copy of it that resolves the symbol against a library
 * shipped with the client works instead.
 *
 * The library shipped with the client is named by the {@value #SURFACE_PROPERTY} system property.
 * Without that property nothing happens here and the toolkit loads exactly as it always did. The
 * copy is written beside that library because it refers to it relative to its own location.
 *
 * The file downloaded for the toolkit is never modified.
 */
public final class MacSoftwareToolkitLibrary {

    /**
     * The name the toolkit registers its library under.
     */
    private static final String LIBRARY = "sw3d";

    /**
     * Names the library that provides the drawing surface.
     */
    private static final String SURFACE_PROPERTY = "sw3d.surface.library";

    private static final String COPY_NAME = "libsw3d-surface.dylib";

    private static final byte[] JAVA_VM = path("/System/Library/Frameworks/JavaVM.framework/Versions/A/JavaVM");

    /**
     * Registers a copy of the toolkit that draws through the surface library, if this platform
     * needs one and one was supplied.
     */
    public static void substitute(String name) {
        File surface = surfaceLibrary();
        if (LIBRARY.equals(name) && surface != null) {
            File shipped = (File) LibraryManager.libraries.get(LIBRARY);
            if (shipped != null) {
                File copy = new File(surface.getParentFile(), COPY_NAME);
                if (writeCopy(shipped, copy, surface.getName())) {
                    LibraryManager.putLibrary(copy, LIBRARY);
                }
            }
        }
    }

    /**
     * Whether this client is supplying the toolkit's drawing surface, which is the one switch that
     * turns on everything macOS needs from the software toolkit.
     */
    public static boolean isSupplyingSurface() {
        return surfaceLibrary() != null;
    }

    private static File surfaceLibrary() {
        String configured = System.getProperty(SURFACE_PROPERTY);
        File surface = configured == null ? null : new File(configured);
        if (surface != null && surface.isFile()) {
            return surface;
        } else {
            return null;
        }
    }

    /**
     * Rewrites every reference to JavaVM.framework so that each names the surface library instead.
     * The replacement is shorter than the original, so it is padded and the file keeps its shape.
     *
     * A library built for several architectures carries one such reference per architecture, and
     * the one that matters is whichever slice the running JVM uses, so all of them are rewritten.
     */
    private static boolean writeCopy(File shipped, File copy, String surfaceName) {
        byte[] replacement = path("@loader_path/" + surfaceName);
        if (replacement.length > JAVA_VM.length) {
            return false;
        }

        try {
            byte[] image = Files.readAllBytes(shipped.toPath());
            int rewritten = 0;
            int at = indexOf(image, JAVA_VM, 0);
            while (at >= 0) {
                System.arraycopy(replacement, 0, image, at, replacement.length);
                for (int i = at + replacement.length; i < at + JAVA_VM.length; i++) {
                    image[i] = 0;
                }
                rewritten++;
                at = indexOf(image, JAVA_VM, at + JAVA_VM.length);
            }

            if (rewritten == 0) {
                return false;
            }

            Files.write(copy.toPath(), image);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private static int indexOf(byte[] image, byte[] wanted, int from) {
        int limit = image.length - wanted.length;
        int start = from;
        while (start <= limit && !matches(image, wanted, start)) {
            start++;
        }
        return start > limit ? -1 : start;
    }

    private static boolean matches(byte[] image, byte[] wanted, int start) {
        int i = 0;
        while (i < wanted.length && image[start + i] == wanted[i]) {
            i++;
        }
        return i == wanted.length;
    }

    private static byte[] path(String value) {
        return value.getBytes(StandardCharsets.US_ASCII);
    }

    private MacSoftwareToolkitLibrary() {
        /* empty */
    }
}

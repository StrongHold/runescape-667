package com.jagex.graphics.sw;

import rs2.client.loading.library.LibraryManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Supplies the toolkit libraries with a drawing surface on macOS.
 *
 * Both the software and the hardware toolkit ask JavaVM.framework for a surface, and that
 * framework no longer serves the versions they ask for, so on macOS neither can obtain one and the
 * client falls back to the Java toolkit. Each takes only one symbol from that framework, so a copy
 * that resolves the symbol against a library shipped with the client works instead.
 *
 * The library shipped with the client is named by the {@value #SURFACE_PROPERTY} system property.
 * Without that property nothing happens here and the toolkit loads exactly as it always did. The
 * copy is written beside that library because it refers to it relative to its own location.
 *
 * The file downloaded for the toolkit is never modified.
 */
public final class MacToolkitLibrary {

    /**
     * The libraries that ask JavaVM.framework for a drawing surface, by the name each is
     * registered under.
     */
    private static final List<String> LIBRARIES = List.of("sw3d", "jaggl");

    /**
     * Names the library that provides the drawing surface.
     */
    private static final String SURFACE_PROPERTY = "toolkit.surface.library";

    /**
     * Names a library written for this client, to be loaded in place of the one downloaded for it.
     */
    private static final String REPLACEMENT_PROPERTY = "toolkit.%s.library";

    private static final byte[] JAVA_VM = path("/System/Library/Frameworks/JavaVM.framework/Versions/A/JavaVM");

    /**
     * Registers whichever library the client should load for this name.
     *
     * A library written for this client is preferred, where one is supplied. Failing that, and
     * where the drawing surface is supplied, the downloaded library is copied and the copy is
     * pointed at that surface. Failing both, the client loads what it downloaded.
     */
    public static void substitute(String name) {
        File replacement = fileNamed(String.format(REPLACEMENT_PROPERTY, name));
        if (replacement != null) {
            LibraryManager.putLibrary(replacement, name);
            return;
        }

        File surface = surfaceLibrary();
        if (LIBRARIES.contains(name) && surface != null) {
            File shipped = (File) LibraryManager.libraries.get(name);
            if (shipped != null) {
                File copy = new File(surface.getParentFile(), "lib" + name + "-surface.dylib");
                if (writeCopy(shipped, copy, surface.getName())) {
                    LibraryManager.putLibrary(copy, name);
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
        return fileNamed(SURFACE_PROPERTY);
    }

    private static File fileNamed(String property) {
        String configured = System.getProperty(property);
        File file = configured == null ? null : new File(configured);
        if (file != null && file.isFile()) {
            return file;
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

    private MacToolkitLibrary() {
        /* empty */
    }
}

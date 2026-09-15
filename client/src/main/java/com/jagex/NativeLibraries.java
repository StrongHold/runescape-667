package com.jagex;

import com.jagex.graphics.sw.SoftwareToolkitLifetime;
import rs2.client.loading.library.LibraryManager;
import rs2.client.loading.library.LibraryOverride;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads libraries written for this client in place of the ones downloaded for it.
 *
 * Two kinds of replacement are offered. A library written for this client is named by the
 * {@code toolkit.<name>.library} system property and is loaded as it stands. A library that has no
 * replacement yet but that asks JavaVM.framework for a drawing surface is copied and the copy is
 * pointed at a surface shipped with this client, because that framework no longer serves the
 * versions those libraries ask for and macOS therefore denies them a surface.
 *
 * Neither path modifies a downloaded library. The copy is written beside the surface library,
 * because it refers to that library relative to its own location.
 */
public final class NativeLibraries {

    /**
     * The library that asks JavaVM.framework for a drawing surface, by the name it is registered
     * under. Only the software toolkit is left: everything else that wanted one is ours now and
     * takes its surface from Cocoa directly.
     */
    private static final String SURFACE_DEPENDENT = "sw3d";

    /**
     * Names the library that provides the drawing surface.
     */
    private static final String SURFACE_PROPERTY = "toolkit.surface.library";

    /**
     * Names a library written for this client, to be loaded in place of the one downloaded for it.
     */
    private static final String REPLACEMENT_PROPERTY = "toolkit.%s.library";

    private static final byte[] JAVA_VM = path("/System/Library/Frameworks/JavaVM.framework/Versions/A/JavaVM");

    private static final Map<String, File> supplied = new ConcurrentHashMap<>();

    /**
     * Takes over the loading of any library this client replaces.
     *
     * Supplying the drawing surface is the one switch that turns on everything macOS needs from
     * the software toolkit, so the toolkits are held for the life of the client only then.
     */
    public static void install() {
        LibraryOverride.supply(NativeLibraries::libraryFor);

        if (surfaceLibrary() != null) {
            SoftwareToolkitLifetime.retainAll();
        }
    }

    /**
     * Answers whichever library this client should load for this name, or null to load the one
     * that was downloaded.
     *
     * A library written for this client is preferred. Failing that, and where the drawing surface
     * is supplied, the downloaded library is copied and the copy is pointed at that surface. The
     * copy is made once, because the download is registered before the first library is loaded and
     * does not change afterwards.
     */
    private static File libraryFor(String name) {
        File replacement = fileNamed(String.format(REPLACEMENT_PROPERTY, name));
        if (replacement != null) {
            return replacement;
        }

        File already = supplied.get(name);
        if (already != null) {
            return already;
        }

        File surface = surfaceLibrary();
        if (!SURFACE_DEPENDENT.equals(name) || surface == null) {
            return null;
        }

        File shipped = (File) LibraryManager.libraries.get(name);
        if (shipped == null) {
            return null;
        }

        File copy = new File(surface.getParentFile(), "lib" + name + "-surface.dylib");
        if (!writeCopy(shipped, copy, surface.getName())) {
            return null;
        }

        supplied.put(name, copy);
        return copy;
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

    private NativeLibraries() {
        /* empty */
    }
}

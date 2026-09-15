package com.jagex.graphics.sw;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps software toolkits alive on macOS.
 *
 * The software toolkit's canvas takes an autorelease pool when it creates its back buffer and
 * drains that pool when it is destroyed. A pool belongs to the thread that created it, and the
 * canvas is built on the thread that draws but destroyed by the collector, so a toolkit torn down
 * through finalization drains a pool on the wrong thread and ends the process. Choosing a graphics
 * profile builds several toolkits in turn and discards all but one, which is the quickest way to
 * meet this.
 *
 * Holding every toolkit means none is ever finalized. A discarded toolkit then keeps its back
 * buffer until the client exits, which is a few megabytes each and only for the handful of
 * toolkits a session builds.
 *
 * This applies only where the drawing surface has been supplied, so no other platform is affected.
 */
public final class MacSoftwareToolkitLifetime {

    private static final List<Object> held = new ArrayList<>();

    /**
     * Holds a toolkit for the life of the client, if this platform needs it held.
     */
    public static void hold(Object toolkit) {
        if (toolkit != null && MacSoftwareToolkitLibrary.isSupplyingSurface()) {
            keep(toolkit);
        }
    }

    /**
     * Whether an object should be kept rather than released where it stands.
     *
     * An object released this way is released on whichever thread the collector happens to be
     * running, and a canvas released there drains a pool belonging to another thread. Keeping the
     * object instead costs its memory and nothing else, because everything released this way is
     * on its way out anyway.
     */
    public static boolean holdRatherThanRelease(Object object) {
        if (object == null || !MacSoftwareToolkitLibrary.isSupplyingSurface()) {
            return false;
        }

        keep(object);
        return true;
    }

    private static void keep(Object object) {
        synchronized (held) {
            held.add(object);
        }
    }

    private MacSoftwareToolkitLifetime() {
        /* empty */
    }
}

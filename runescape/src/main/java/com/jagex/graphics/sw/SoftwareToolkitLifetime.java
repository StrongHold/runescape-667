package com.jagex.graphics.sw;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps software toolkits alive where releasing one would end the process.
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
 * Nothing is held until the host application asks for it, so a platform that does not need this
 * releases its toolkits as it always did.
 *
 * This class is not part of the original client.
 */
public final class SoftwareToolkitLifetime {

    private static final List<Object> held = new ArrayList<>();

    private static volatile boolean retaining = false;

    /**
     * Holds every software toolkit and every object released outside an instance count for the
     * life of the client.
     */
    public static void retainAll() {
        retaining = true;
    }

    /**
     * Holds a toolkit for the life of the client, where this platform needs it held.
     */
    public static void hold(Object toolkit) {
        if (toolkit != null && retaining) {
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
        if (object == null || !retaining) {
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

    private SoftwareToolkitLifetime() {
        /* empty */
    }
}

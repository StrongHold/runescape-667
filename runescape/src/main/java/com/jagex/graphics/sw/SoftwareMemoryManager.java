package com.jagex.graphics.sw;

import com.jagex.core.datastruct.key.Deque;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!jo")
public final class SoftwareMemoryManager {

    @OriginalMember(owner = "client!jo", name = "c", descriptor = "Lclient!sia;")
    private static final Deque objects = new Deque();

    @OriginalMember(owner = "client!jo", name = "a", descriptor = "I")
    private static int instances = 0;

    @OriginalMember(owner = "client!jo", name = "b", descriptor = "Z")
    private static boolean shutdown = false;

    @OriginalMember(owner = "client!jo", name = "a", descriptor = "(ILclient!e;)V")
    public static synchronized void free(@OriginalArg(1) SoftwareObject object) {
        if (shutdown) {
            return;
        }

        if (instances <= 0) {
            // Not part of the original client.
            if (SoftwareToolkitLifetime.holdRatherThanRelease(object)) {
                return;
            }
            object.w(false);
        } else {
            @Pc(15) SoftwareObjectNode node = new SoftwareObjectNode();
            node.object = object;
            objects.addLast(node);
        }
    }

    @OriginalMember(owner = "client!jo", name = "a", descriptor = "(ZI)V")
    public static synchronized void shutdown() {
        shutdown = true;
    }

    @OriginalMember(owner = "client!jo", name = "b", descriptor = "(I)V")
    public static synchronized void instanceCreated() {
        instances++;
    }

    @OriginalMember(owner = "client!jo", name = "a", descriptor = "(I)V")
    public static synchronized void instanceDestroyed() {
        instances--;
        if (instances == 0) {
            freeAll();
        }
    }

    @OriginalMember(owner = "client!jo", name = "c", descriptor = "(I)V")
    public static synchronized void freeAll() {
        while (true) {
            @Pc(4) SoftwareObjectNode node = (SoftwareObjectNode) objects.removeFirst();
            if (node == null) {
                return;
            }
            node.object.w(true);
            node.unlink();
        }
    }

    private SoftwareMemoryManager() {
        /* empty */
    }
}

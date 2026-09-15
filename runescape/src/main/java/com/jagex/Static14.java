package com.jagex;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import rs2.client.loading.library.LibraryManager;
import rs2.client.loading.library.LibraryOverride;

public final class Static14 {

    // $FF: synthetic field
    @OriginalMember(owner = "client!ai", name = "G", descriptor = "Ljava/lang/Class;")
    public static Class aClass1;

    @OriginalMember(owner = "client!ai", name = "L", descriptor = "[I")
    public static int[] anIntArray25 = new int[]{36064, 36065, 36066, 36067, 36068, 36069, 36070, 36071, 36096};

    @OriginalMember(owner = "client!ai", name = "a", descriptor = "(ILjava/lang/String;)Z")
    public static boolean loadNativeLibrary(@OriginalArg(1) String arg0) {
        LibraryOverride.apply(arg0); // Not part of the original client.
        return LibraryManager.loadNative(aClass1 == null ? (aClass1 = getClass("rs2.client.loading.library.NativeLibrary")) : aClass1, arg0);
    }

    static Class getClass(String name) {
        Class instance;
        try {
            instance = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw (NoClassDefFoundError) new NoClassDefFoundError().initCause(ex);
        }
        return instance;
    }
}

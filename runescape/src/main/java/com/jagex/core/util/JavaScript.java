package com.jagex.core.util;

import netscape.javascript.JSObject;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

import java.applet.Applet;
import java.lang.reflect.Method;

public final class JavaScript {

    @OriginalMember(owner = "client!ac", name = "a", descriptor = "(BLjava/applet/Applet;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;")
    public static Object call(@OriginalArg(1) Applet applet, @OriginalArg(2) String arg1, @OriginalArg(3) Object[] arg2) throws Throwable {
        // todo: use reflection so LiveConnect can still function in a compatible JVM
        // return JSObject.getWindow(applet).call(arg1, arg2);
        return invokeOnWindow(applet, "call", new Class<?>[] { String.class, Object[].class }, new Object[] { arg1, arg2 });
    }

    @OriginalMember(owner = "client!ac", name = "a", descriptor = "(Ljava/lang/String;Ljava/applet/Applet;B)Ljava/lang/Object;")
    public static Object call(@OriginalArg(0) String arg0, @OriginalArg(1) Applet applet) throws Throwable {
        // todo: use reflection so LiveConnect can still function in a compatible JVM
        // return JSObject.getWindow(applet).call(arg0, (Object[]) null);
        return invokeOnWindow(applet, "call", new Class<?>[] { String.class, Object[].class }, new Object[] { arg0, null });
    }

    @OriginalMember(owner = "client!ac", name = "a", descriptor = "(Ljava/lang/String;Ljava/applet/Applet;I)V")
    public static void eval(@OriginalArg(0) String string, @OriginalArg(1) Applet applet) throws Throwable {
        // todo: use reflection so LiveConnect can still function in a compatible JVM
        // JSObject.getWindow(applet).eval(string);
        invokeOnWindow(applet, "eval", new Class<?>[] { String.class }, new Object[] { string });
    }

    /**
     * The browser window is reached reflectively because {@code JSObject.getWindow} is only present
     * in a JVM hosting the client as an applet. A direct call stops the client building anywhere
     * else.
     */
    private static Object invokeOnWindow(Applet applet, String name, Class<?>[] types, Object[] args) throws Throwable {
        Class<?> clazz = Class.forName("netscape.javascript.JSObject");
        Method getWindow = clazz.getMethod("getWindow", Applet.class);
        Method method = clazz.getMethod(name, types);
        return method.invoke(getWindow.invoke(null, applet), args);
    }

    private JavaScript() {
        /* empty */
    }
}

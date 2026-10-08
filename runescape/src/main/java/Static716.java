import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.lang.reflect.Method;

public final class Static716 {

    // $FF: synthetic field
    @OriginalMember(owner = "client!wm", name = "b", descriptor = "Ljava/lang/Class;")
    public static Class aClass29;

    // $FF: synthetic field
    @OriginalMember(owner = "client!wm", name = "c", descriptor = "Ljava/lang/Class;")
    public static Class aClass30;

    @OriginalMember(owner = "client!wm", name = "a", descriptor = "(I)V")
    public static synchronized void method9349() {
        if (Static210.anObject8 != null) {
            return;
        }
        try {
            @Pc(20) Class local20 = Class.forName("java.lang.management.ManagementFactory");
            @Pc(27) Method local27 = local20.getDeclaredMethod("getPlatformMBeanServer", (Class[]) null);
            @Pc(32) Object local32 = local27.invoke(null, (Object[]) null);
            @Pc(75) Method local75 = local20.getMethod("newPlatformMXBeanProxy", Class.forName("javax.management.MBeanServerConnection"), aClass29 == null ? (aClass29 = Class.forName("java.lang.String")) : aClass29, aClass30 == null ? (aClass30 = Class.forName("java.lang.Class")) : aClass30);
            Static210.anObject8 = local75.invoke(null, local32, "com.sun.management:type=HotSpotDiagnostic", Class.forName("com.sun.management.HotSpotDiagnosticMXBean"));
        } catch (@Pc(100) Exception local100) {
            System.out.println("HeapDump setup error:");
            local100.printStackTrace();
        }
    }

    @OriginalMember(owner = "client!wm", name = "a", descriptor = "(IILclient!cg;BIII)V")
    public static void method9350(@OriginalArg(2) PathingEntity arg0, @OriginalArg(4) int arg1, @OriginalArg(5) int arg2, @OriginalArg(6) int arg3) {
        Static418.projectFromGround(arg0.level, arg0.z, arg2, arg0.x, 0);
    }
}

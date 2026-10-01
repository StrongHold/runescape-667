import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static501 {

    @OriginalMember(owner = "client!pq", name = "v", descriptor = "[I")
    public static int[] lightnessColumns;

    @OriginalMember(owner = "client!pq", name = "z", descriptor = "I")
    public static int mapLength = 104;

    @OriginalMember(owner = "client!pq", name = "t", descriptor = "Z")
    public static boolean aBoolean575 = false;

    @OriginalMember(owner = "client!pq", name = "u", descriptor = "[Lclient!lca;")
    public static final PointLight[] aClass2_Sub7Array4 = new PointLight[8];

    @OriginalMember(owner = "client!pq", name = "a", descriptor = "(BI)Z")
    public static boolean method6715(@OriginalArg(1) int arg0) {
        return arg0 != 1 && arg0 != 7;
    }

    @OriginalMember(owner = "client!pq", name = "a", descriptor = "(ZI)V")
    public static void method6716(@OriginalArg(0) boolean arg0) {
        @Pc(5) int local5 = Static329.anInt1752;
        @Pc(7) int local7 = Static32.anInt775;
        if (arg0 && OrthoMode.enabled) {
            local5 <<= 0x1;
            local7 = -local5;
        }
        Toolkit.active.f(local7, local5);
    }
}

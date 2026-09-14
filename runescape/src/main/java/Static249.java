import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static249 {

    @OriginalMember(owner = "client!hma", name = "B", descriptor = "I")
    public static int rebootTimer = 0;

    @OriginalMember(owner = "client!hma", name = "a", descriptor = "(IBIIIII)V")
    public static void method3535(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        @Pc(11) int local11 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg1);
        @Pc(17) int local17 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg3);
        @Pc(23) int local23 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg4);
        @Pc(29) int local29 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg2);
        @Pc(38) int local38 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg1 + arg0);
        @Pc(47) int local47 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg3 - arg0);
        for (@Pc(49) int local49 = local11; local49 < local38; local49++) {
            Static696.fillHorizontalSpan(local29, arg5, local23, Static723.anIntArrayArray266[local49]);
        }
        for (@Pc(78) int local78 = local17; local78 > local47; local78--) {
            Static696.fillHorizontalSpan(local29, arg5, local23, Static723.anIntArrayArray266[local78]);
        }
        @Pc(104) int local104 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg4 + arg0);
        @Pc(113) int local113 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg2 - arg0);
        for (@Pc(115) int local115 = local38; local115 <= local47; local115++) {
            @Pc(123) int[] local123 = Static723.anIntArrayArray266[local115];
            Static696.fillHorizontalSpan(local104, arg5, local23, local123);
            Static696.fillHorizontalSpan(local29, arg5, local113, local123);
        }
    }

    @OriginalMember(owner = "client!hma", name = "a", descriptor = "(BZ)Z")
    public static boolean setBloom(@OriginalArg(1) boolean bloom) {
        @Pc(13) boolean current = Toolkit.active.bloom();
        if (current == bloom) {
            return true;
        }

        if (!bloom) {
            Toolkit.active.stopBloom();
        } else if (!Toolkit.active.hasBloom()) {
            bloom = false;
        }

        if (bloom == current) {
            return false;
        } else {
            ClientOptions.instance.update(bloom ? 1 : 0, ClientOptions.instance.bloom);
            ClientOptions.save();
            return true;
        }
    }

    @OriginalMember(owner = "client!hma", name = "a", descriptor = "(Z)V")
    public static void method3538() {
        Static408.method5632();
    }
}

import com.jagex.core.datastruct.key.Deque;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static230 {

    @OriginalMember(owner = "client!hca", name = "a", descriptor = "(IIIIII)V")
    public static void method3370(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4) {
        @Pc(11) int local11 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg3);
        @Pc(19) int local19 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg4);
        @Pc(25) int local25 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0);
        @Pc(31) int local31 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg1);
        for (@Pc(37) int local37 = local11; local37 <= local19; local37++) {
            Static696.fillHorizontalSpan(local31, arg2, local25, Static723.anIntArrayArray266[local37]);
        }
    }

    @OriginalMember(owner = "client!hca", name = "a", descriptor = "(III)Z")
    public static boolean method3372(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x800) != 0;
    }

    @OriginalMember(owner = "client!hca", name = "a", descriptor = "(I)V")
    public static void method3374() {
        Static631.reflectionChecks = new Deque();
    }
}

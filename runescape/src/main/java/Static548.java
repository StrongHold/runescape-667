import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static548 {

    @OriginalMember(owner = "client!re", name = "a", descriptor = "(IIIIIIB)V")
    public static void method7253(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(8) int local8 = arg3 - arg4;
        @Pc(13) int local13 = arg5 + arg4;
        for (@Pc(15) int local15 = arg5; local15 < local13; local15++) {
            Static696.fillHorizontalSpan(arg2, arg0, arg1, Static723.anIntArrayArray266[local15]);
        }
        for (@Pc(36) int local36 = arg3; local36 > local8; local36--) {
            Static696.fillHorizontalSpan(arg2, arg0, arg1, Static723.anIntArrayArray266[local36]);
        }
        @Pc(57) int local57 = arg2 - arg4;
        @Pc(62) int local62 = arg1 + arg4;
        for (@Pc(64) int local64 = local13; local64 <= local8; local64++) {
            @Pc(69) int[] local69 = Static723.anIntArrayArray266[local64];
            Static696.fillHorizontalSpan(local62, arg0, arg1, local69);
            Static696.fillHorizontalSpan(arg2, arg0, local57, local69);
        }
    }
}

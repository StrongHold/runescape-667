import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static265 {

    @OriginalMember(owner = "client!iea", name = "a", descriptor = "[I")
    public static final int[] varclanUpdates = new int[32];

    @OriginalMember(owner = "client!iea", name = "a", descriptor = "(IIIII)V")
    public static void method3858(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        @Pc(7) int local7 = 0;
        @Pc(14) int local14 = arg2;
        @Pc(17) int local17 = -arg2;
        Static696.fillHorizontalSpan(arg2 + arg0, arg1, arg0 - arg2, Static723.anIntArrayArray266[arg3]);
        @Pc(31) int local31 = -1;
        while (local7 < local14) {
            local31 += 2;
            local17 += local31;
            local7++;
            if (local17 >= 0) {
                local14--;
                local17 -= local14 << 1;
                @Pc(57) int[] local57 = Static723.anIntArrayArray266[local14 + arg3];
                @Pc(64) int[] local64 = Static723.anIntArrayArray266[arg3 - local14];
                @Pc(68) int local68 = local7 + arg0;
                @Pc(73) int local73 = arg0 - local7;
                Static696.fillHorizontalSpan(local68, arg1, local73, local57);
                Static696.fillHorizontalSpan(local68, arg1, local73, local64);
            }
            @Pc(91) int local91 = local14 + arg0;
            @Pc(96) int local96 = arg0 - local14;
            @Pc(102) int[] local102 = Static723.anIntArrayArray266[local7 + arg3];
            @Pc(109) int[] local109 = Static723.anIntArrayArray266[arg3 - local7];
            Static696.fillHorizontalSpan(local91, arg1, local96, local102);
            Static696.fillHorizontalSpan(local91, arg1, local96, local109);
        }
    }
}

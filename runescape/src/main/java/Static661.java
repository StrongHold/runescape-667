import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static661 {

    @OriginalMember(owner = "client!uw", name = "A", descriptor = "I")
    public static int anInt6055;

    @OriginalMember(owner = "client!uw", name = "u", descriptor = "Z")
    public static boolean aBoolean457 = false;

    @OriginalMember(owner = "client!uw", name = "x", descriptor = "Lclient!eba;")
    public static final Class92 aClass92_10 = new Class92(1);

    @OriginalMember(owner = "client!uw", name = "a", descriptor = "(IIIIIIII)V")
    public static void method5433(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6) {
        @Pc(7) int local7 = 0;
        @Pc(9) int local9 = arg3;
        @Pc(11) int local11 = 0;
        @Pc(16) int local16 = arg1 - arg4;
        @Pc(21) int local21 = arg3 - arg4;
        @Pc(25) int local25 = arg1 * arg1;
        @Pc(29) int local29 = arg3 * arg3;
        @Pc(33) int local33 = local16 * local16;
        @Pc(37) int local37 = local21 * local21;
        @Pc(41) int local41 = local29 << 1;
        @Pc(45) int local45 = local25 << 1;
        @Pc(49) int local49 = local37 << 1;
        @Pc(53) int local53 = local33 << 1;
        @Pc(57) int local57 = arg3 << 1;
        @Pc(61) int local61 = local21 << 1;
        @Pc(71) int local71 = (1 - local57) * local25 + local41;
        @Pc(80) int local80 = local29 - local45 * (local57 - 1);
        @Pc(89) int local89 = (1 - local61) * local33 + local49;
        @Pc(98) int local98 = local37 - local53 * (local61 - 1);
        @Pc(102) int local102 = local25 << 2;
        @Pc(106) int local106 = local29 << 2;
        @Pc(110) int local110 = local33 << 2;
        @Pc(114) int local114 = local37 << 2;
        @Pc(118) int local118 = local41 * 3;
        @Pc(124) int local124 = (local57 - 3) * local45;
        @Pc(128) int local128 = local49 * 3;
        @Pc(134) int local134 = (local61 - 3) * local53;
        @Pc(136) int local136 = local106;
        @Pc(142) int local142 = (arg3 - 1) * local102;
        @Pc(144) int local144 = local114;
        @Pc(150) int local150 = (local21 - 1) * local110;
        @Pc(154) int[] local154 = Static723.anIntArrayArray266[arg5];
        Static696.fillHorizontalSpan(arg6 - local16, arg2, arg6 - arg1, local154);
        Static696.fillHorizontalSpan(local16 + arg6, arg0, arg6 - local16, local154);
        Static696.fillHorizontalSpan(arg6 + arg1, arg2, local16 + arg6, local154);
        while (local9 > 0) {
            @Pc(198) boolean local198 = local9 <= local21;
            if (local198) {
                if (local89 < 0) {
                    while (local89 < 0) {
                        local89 += local128;
                        local98 += local144;
                        local11++;
                        local144 += local114;
                        local128 += local114;
                    }
                }
                if (local98 < 0) {
                    local89 += local128;
                    local98 += local144;
                    local144 += local114;
                    local128 += local114;
                    local11++;
                }
                local98 += -local134;
                local89 += -local150;
                local150 -= local110;
                local134 -= local110;
            }
            if (local71 < 0) {
                while (local71 < 0) {
                    local71 += local118;
                    local80 += local136;
                    local7++;
                    local136 += local106;
                    local118 += local106;
                }
            }
            if (local80 < 0) {
                local80 += local136;
                local71 += local118;
                local136 += local106;
                local7++;
                local118 += local106;
            }
            local71 += -local142;
            local80 += -local124;
            local9--;
            local142 -= local102;
            local124 -= local102;
            @Pc(345) int local345 = arg5 - local9;
            @Pc(349) int local349 = local9 + arg5;
            @Pc(353) int local353 = local7 + arg6;
            @Pc(358) int local358 = arg6 - local7;
            if (local198) {
                @Pc(364) int local364 = local11 + arg6;
                @Pc(369) int local369 = arg6 - local11;
                Static696.fillHorizontalSpan(local369, arg2, local358, Static723.anIntArrayArray266[local345]);
                Static696.fillHorizontalSpan(local364, arg0, local369, Static723.anIntArrayArray266[local345]);
                Static696.fillHorizontalSpan(local353, arg2, local364, Static723.anIntArrayArray266[local345]);
                Static696.fillHorizontalSpan(local369, arg2, local358, Static723.anIntArrayArray266[local349]);
                Static696.fillHorizontalSpan(local364, arg0, local369, Static723.anIntArrayArray266[local349]);
                Static696.fillHorizontalSpan(local353, arg2, local364, Static723.anIntArrayArray266[local349]);
            } else {
                Static696.fillHorizontalSpan(local353, arg2, local358, Static723.anIntArrayArray266[local345]);
                Static696.fillHorizontalSpan(local353, arg2, local358, Static723.anIntArrayArray266[local349]);
            }
        }
    }
}

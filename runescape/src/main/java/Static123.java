import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static123 {

    @OriginalMember(owner = "client!dr", name = "a", descriptor = "(IIIIBII)V")
    public static void method2210(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        Static315.method4574(arg3);
        @Pc(10) int local10 = 0;
        @Pc(15) int local15 = arg3 - arg5;
        if (local15 < 0) {
            local15 = 0;
        }
        @Pc(21) int local21 = arg3;
        @Pc(24) int local24 = -arg3;
        @Pc(26) int local26 = local15;
        @Pc(36) int local36 = -local15;
        @Pc(38) int local38 = -1;
        @Pc(40) int local40 = -1;
        @Pc(44) int[] local44 = Static723.anIntArrayArray266[arg2];
        @Pc(49) int local49 = arg4 - local15;
        Static696.fillHorizontalSpan(local49, arg0, arg4 - arg3, local44);
        @Pc(62) int local62 = arg4 + local15;
        Static696.fillHorizontalSpan(local62, arg1, local49, local44);
        Static696.fillHorizontalSpan(arg4 + arg3, arg0, local62, local44);
        while (local10 < local21) {
            local38 += 2;
            local40 += 2;
            local36 += local40;
            local24 += local38;
            if (local36 >= 0 && local26 >= 1) {
                Static430.anIntArray519[local26] = local10;
                local26--;
                local36 -= local26 << 1;
            }
            local10++;
            @Pc(132) int[] local132;
            @Pc(139) int[] local139;
            @Pc(143) int local143;
            @Pc(147) int local147;
            @Pc(152) int local152;
            @Pc(156) int local156;
            @Pc(161) int local161;
            if (local24 >= 0) {
                local21--;
                if (local21 < local15) {
                    local132 = Static723.anIntArrayArray266[local21 + arg2];
                    local139 = Static723.anIntArrayArray266[arg2 - local21];
                    local143 = Static430.anIntArray519[local21];
                    local147 = local10 + arg4;
                    local152 = arg4 - local10;
                    local156 = local143 + arg4;
                    local161 = arg4 - local143;
                    Static696.fillHorizontalSpan(local161, arg0, local152, local132);
                    Static696.fillHorizontalSpan(local156, arg1, local161, local132);
                    Static696.fillHorizontalSpan(local147, arg0, local156, local132);
                    Static696.fillHorizontalSpan(local161, arg0, local152, local139);
                    Static696.fillHorizontalSpan(local156, arg1, local161, local139);
                    Static696.fillHorizontalSpan(local147, arg0, local156, local139);
                } else {
                    local132 = Static723.anIntArrayArray266[arg2 + local21];
                    local139 = Static723.anIntArrayArray266[arg2 - local21];
                    local143 = arg4 + local10;
                    local147 = arg4 - local10;
                    Static696.fillHorizontalSpan(local143, arg0, local147, local132);
                    Static696.fillHorizontalSpan(local143, arg0, local147, local139);
                }
                local24 -= local21 << 1;
            }
            local132 = Static723.anIntArrayArray266[arg2 + local10];
            local139 = Static723.anIntArrayArray266[arg2 - local10];
            local143 = local21 + arg4;
            local147 = arg4 - local21;
            if (local15 > local10) {
                local152 = local26 < local10 ? Static430.anIntArray519[local10] : local26;
                local156 = arg4 + local152;
                local161 = arg4 - local152;
                Static696.fillHorizontalSpan(local161, arg0, local147, local132);
                Static696.fillHorizontalSpan(local156, arg1, local161, local132);
                Static696.fillHorizontalSpan(local143, arg0, local156, local132);
                Static696.fillHorizontalSpan(local161, arg0, local147, local139);
                Static696.fillHorizontalSpan(local156, arg1, local161, local139);
                Static696.fillHorizontalSpan(local143, arg0, local156, local139);
            } else {
                Static696.fillHorizontalSpan(local143, arg0, local147, local132);
                Static696.fillHorizontalSpan(local143, arg0, local147, local139);
            }
        }
    }
}

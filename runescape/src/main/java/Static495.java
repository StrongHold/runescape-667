import com.jagex.graphics.skybox.SkyBox;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static495 {

    @OriginalMember(owner = "client!pk", name = "f", descriptor = "Lclient!gm;")
    public static SkyBox aSkyBox_4;

    @OriginalMember(owner = "client!pk", name = "e", descriptor = "Lclient!fma;")
    public static final Class131 aClass131_5 = new Class131();

    @OriginalMember(owner = "client!pk", name = "a", descriptor = "(IIIIIIII)V")
    public static void method6610(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6) {
        @Pc(7) int local7 = 0;
        @Pc(9) int local9 = arg2;
        @Pc(11) int local11 = 0;
        @Pc(16) int local16 = arg1 - arg6;
        @Pc(20) int local20 = arg2 - arg6;
        @Pc(24) int local24 = arg1 * arg1;
        @Pc(28) int local28 = arg2 * arg2;
        @Pc(39) int local39 = local16 * local16;
        @Pc(43) int local43 = local20 * local20;
        @Pc(47) int local47 = local28 << 1;
        @Pc(51) int local51 = local24 << 1;
        @Pc(55) int local55 = local43 << 1;
        @Pc(59) int local59 = local39 << 1;
        @Pc(63) int local63 = arg2 << 1;
        @Pc(67) int local67 = local20 << 1;
        @Pc(76) int local76 = (1 - local63) * local24 + local47;
        @Pc(84) int local84 = local28 - (local63 - 1) * local51;
        @Pc(93) int local93 = local55 + local39 * (1 - local67);
        @Pc(102) int local102 = local43 - (local67 - 1) * local59;
        @Pc(106) int local106 = local24 << 2;
        @Pc(110) int local110 = local28 << 2;
        @Pc(114) int local114 = local39 << 2;
        @Pc(118) int local118 = local43 << 2;
        @Pc(122) int local122 = local47 * 3;
        @Pc(128) int local128 = local51 * (local63 - 3);
        @Pc(132) int local132 = local55 * 3;
        @Pc(138) int local138 = (local67 - 3) * local59;
        @Pc(140) int local140 = local110;
        @Pc(146) int local146 = (arg2 - 1) * local106;
        @Pc(148) int local148 = local118;
        @Pc(154) int local154 = local114 * (local20 - 1);
        @Pc(179) int local179;
        @Pc(190) int local190;
        @Pc(198) int local198;
        @Pc(206) int local206;
        if (Static724.anInt10930 <= arg4 && arg4 <= Static273.anInt4395) {
            @Pc(170) int[] local170 = Static723.anIntArrayArray266[arg4];
            local179 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 - arg1);
            local190 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 + arg1);
            local198 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 - local16);
            local206 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 + local16);
            Static696.fillHorizontalSpan(local198, arg3, local179, local170);
            Static696.fillHorizontalSpan(local206, arg5, local198, local170);
            Static696.fillHorizontalSpan(local190, arg3, local206, local170);
        }
        while (local9 > 0) {
            @Pc(239) boolean local239 = local20 >= local9;
            if (local239) {
                if (local93 < 0) {
                    while (local93 < 0) {
                        local102 += local148;
                        local93 += local132;
                        local148 += local118;
                        local132 += local118;
                        local11++;
                    }
                }
                if (local102 < 0) {
                    local93 += local132;
                    local102 += local148;
                    local132 += local118;
                    local11++;
                    local148 += local118;
                }
                local93 += -local154;
                local102 += -local138;
                local154 -= local114;
                local138 -= local114;
            }
            if (local76 < 0) {
                while (local76 < 0) {
                    local76 += local122;
                    local84 += local140;
                    local7++;
                    local122 += local110;
                    local140 += local110;
                }
            }
            if (local84 < 0) {
                local84 += local140;
                local76 += local122;
                local122 += local110;
                local140 += local110;
                local7++;
            }
            local84 += -local128;
            local76 += -local146;
            local146 -= local106;
            local128 -= local106;
            local9--;
            local179 = arg4 - local9;
            local190 = arg4 + local9;
            if (local190 >= Static724.anInt10930 && local179 <= Static273.anInt4395) {
                local198 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 + local7);
                local206 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 - local7);
                if (local239) {
                    @Pc(442) int local442 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 + local11);
                    @Pc(452) int local452 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg0 - local11);
                    @Pc(459) int[] local459;
                    if (Static724.anInt10930 <= local179) {
                        local459 = Static723.anIntArrayArray266[local179];
                        Static696.fillHorizontalSpan(local452, arg3, local206, local459);
                        Static696.fillHorizontalSpan(local442, arg5, local452, local459);
                        Static696.fillHorizontalSpan(local198, arg3, local442, local459);
                    }
                    if (Static273.anInt4395 >= local190) {
                        local459 = Static723.anIntArrayArray266[local190];
                        Static696.fillHorizontalSpan(local452, arg3, local206, local459);
                        Static696.fillHorizontalSpan(local442, arg5, local452, local459);
                        Static696.fillHorizontalSpan(local198, arg3, local442, local459);
                    }
                } else {
                    if (local179 >= Static724.anInt10930) {
                        Static696.fillHorizontalSpan(local198, arg3, local206, Static723.anIntArrayArray266[local179]);
                    }
                    if (local190 <= Static273.anInt4395) {
                        Static696.fillHorizontalSpan(local198, arg3, local206, Static723.anIntArrayArray266[local190]);
                    }
                }
            }
        }
    }
}

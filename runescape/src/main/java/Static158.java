import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static158 {

    @OriginalMember(owner = "client!ev", name = "a", descriptor = "Lclient!ha;")
    public static Toolkit objSpriteToolkit;

    @OriginalMember(owner = "client!ev", name = "g", descriptor = "Ljava/lang/Object;")
    public static Object anObject5;

    @OriginalMember(owner = "client!ev", name = "a", descriptor = "(IIIII)V")
    public static void method2571(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(14) int local14 = 0;
        @Pc(16) int local16 = arg1;
        @Pc(19) int local19 = -arg1;
        @Pc(21) int local21 = -1;
        @Pc(29) int local29 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg1 + arg3);
        @Pc(38) int local38 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg3 - arg1);
        Static696.fillHorizontalSpan(local29, arg2, local38, Static723.anIntArrayArray266[arg0]);
        while (local14 < local16) {
            local21 += 2;
            local19 += local21;
            @Pc(69) int local69;
            @Pc(73) int local73;
            @Pc(94) int local94;
            @Pc(103) int local103;
            if (local19 > 0) {
                local16--;
                local19 -= local16 << 1;
                local69 = arg0 - local16;
                local73 = local16 + arg0;
                if (local73 >= Static724.anInt10930 && local69 <= Static273.anInt4395) {
                    local94 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg3 + local14);
                    local103 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg3 - local14);
                    if (local73 <= Static273.anInt4395) {
                        Static696.fillHorizontalSpan(local94, arg2, local103, Static723.anIntArrayArray266[local73]);
                    }
                    if (Static724.anInt10930 <= local69) {
                        Static696.fillHorizontalSpan(local94, arg2, local103, Static723.anIntArrayArray266[local69]);
                    }
                }
            }
            local14++;
            local69 = arg0 - local14;
            local73 = local14 + arg0;
            if (Static724.anInt10930 <= local73 && local69 <= Static273.anInt4395) {
                local94 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg3 + local16);
                local103 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, arg3 - local16);
                if (Static273.anInt4395 >= local73) {
                    Static696.fillHorizontalSpan(local94, arg2, local103, Static723.anIntArrayArray266[local73]);
                }
                if (Static724.anInt10930 <= local69) {
                    Static696.fillHorizontalSpan(local94, arg2, local103, Static723.anIntArrayArray266[local69]);
                }
            }
        }
    }
}

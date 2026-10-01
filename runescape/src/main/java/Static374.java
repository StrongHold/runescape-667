import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Shadow;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static374 {

    @OriginalMember(owner = "client!lp", name = "c", descriptor = "I")
    public static int anInt5906 = 2;

    @OriginalMember(owner = "client!lp", name = "d", descriptor = "[I")
    public static final int[] anIntArray457 = new int[1];

    @OriginalMember(owner = "client!lp", name = "a", descriptor = "(II)V")
    public static void method5303(@OriginalArg(1) int arg0) {
        @Pc(1) Shadow local1 = null;
        for (@Pc(3) int local3 = 1; local3 < arg0; local3++) {
            @Pc(8) Ground local8 = Static706.floor[local3];
            if (local8 != null) {
                for (@Pc(12) int local12 = 0; local12 < Static662.tileMaxZ; local12++) {
                    for (@Pc(15) int local15 = 0; local15 < Static619.tileMaxX; local15++) {
                        local1 = local8.fa(local15, local12, local1);
                        if (local1 != null) {
                            @Pc(28) int local28 = local15 << EnvironmentLight.tileShift;
                            @Pc(32) int local32 = local12 << EnvironmentLight.tileShift;
                            for (@Pc(36) int local36 = local3 - 1; local36 >= 0; local36--) {
                                @Pc(41) Ground local41 = Static706.floor[local36];
                                if (local41 != null) {
                                    @Pc(55) int local55 = local8.getHeight(local15, local12) - local41.getHeight(local15, local12);
                                    @Pc(71) int local71 = local8.getHeight(local15 + 1, local12) - local41.getHeight(local15 + 1, local12);
                                    @Pc(91) int local91 = local8.getHeight(local15 + 1, local12 + 1) - local41.getHeight(local15 + 1, local12 + 1);
                                    @Pc(107) int local107 = local8.getHeight(local15, local12 + 1) - local41.getHeight(local15, local12 + 1);
                                    local41.CA(local1, local28, (local55 + local71 + local91 + local107) / 4, local32, 0, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

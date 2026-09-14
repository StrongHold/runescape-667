import com.jagex.core.constants.TileFlag;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static696 {

    @OriginalMember(owner = "client!w", name = "a", descriptor = "(Z)I")
    public static int method9034() {
        return LoginManager.anInt7113;
    }

    @OriginalMember(owner = "client!w", name = "a", descriptor = "(IIIII)Z")
    public static boolean isTileVisibleFrom(@OriginalArg(0) int z, @OriginalArg(2) int otherLevel, @OriginalArg(3) int x, @OriginalArg(4) int level) {
        if ((Static280.tileFlags[0][x][z] & TileFlag.BRIDGE) != 0) {
            return true;
        } else if ((Static280.tileFlags[level][x][z] & TileFlag.INVISIBLE) != 0) {
            return false;
        } else {
            return Static705.method9198(z, x, level) == otherLevel;
        }
    }

    @OriginalMember(owner = "client!w", name = "a", descriptor = "(III[II)V")
    public static void fillHorizontalSpan(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3) {
        arg2--;
        @Pc(12) int local12 = arg0 - 1;
        @Pc(15) int local15 = local12 - 7;
        while (local15 > arg2) {
            @Pc(18) int local18 = arg2 + 1;
            arg3[local18] = arg1;
            @Pc(23) int local23 = local18 + 1;
            arg3[local23] = arg1;
            @Pc(28) int local28 = local23 + 1;
            arg3[local28] = arg1;
            @Pc(33) int local33 = local28 + 1;
            arg3[local33] = arg1;
            @Pc(38) int local38 = local33 + 1;
            arg3[local38] = arg1;
            @Pc(43) int local43 = local38 + 1;
            arg3[local43] = arg1;
            @Pc(48) int local48 = local43 + 1;
            arg3[local48] = arg1;
            arg2 = local48 + 1;
            arg3[arg2] = arg1;
        }
        while (local12 > arg2) {
            arg2++;
            arg3[arg2] = arg1;
        }
    }
}

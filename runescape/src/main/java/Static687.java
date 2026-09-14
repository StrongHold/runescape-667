import com.jagex.core.util.Arrays;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static687 {

    @OriginalMember(owner = "client!vo", name = "a", descriptor = "(IIBII)V")
    public static void removeLocationOccluder(@OriginalArg(0) int x, @OriginalArg(1) int level, @OriginalArg(3) int type, @OriginalArg(4) int z) {
        if (type != 8 && type != 16) {
            @Pc(28) Tile tile = Static334.activeTiles[level][x][z];
            if (tile != null) {
                if (type == 1) {
                    tile.aShort84 = 0;
                } else if (type == 2) {
                    tile.aShort83 = 0;
                }
            }
            Static416.method5705();
            return;
        }
        for (@Pc(61) int i = 0; i < Static150.anInt2634; i++) {
            @Pc(67) LocOccluder occluder = Static285.aLocOccluderArray1[i];
            if (occluder.aByte43 == type && x == occluder.aShort26 && z == occluder.aShort23 || occluder.aShort24 == x && z == occluder.aShort23) {
                if (i != Static150.anInt2634) {
                    Arrays.copy(Static285.aLocOccluderArray1, i + 1, Static285.aLocOccluderArray1, i, Static285.aLocOccluderArray1.length - i - 1);
                }
                Static150.anInt2634--;
                return;
            }
        }
    }

    @OriginalMember(owner = "client!vo", name = "a", descriptor = "(III)Lclient!eia;")
    public static GroundDecor getGroundDecor(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        return tile == null || tile.groundDecor == null ? null : tile.groundDecor;
    }
}

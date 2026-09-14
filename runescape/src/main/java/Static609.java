import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static609 {

    @OriginalMember(owner = "client!tea", name = "b", descriptor = "[I")
    public static final int[] anIntArray716 = new int[256];

    @OriginalMember(owner = "client!tea", name = "a", descriptor = "(III)V")
    public static void clearGroundDecor(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        if (tile != null) {
            Static109.hide(tile.groundDecor);
            if (tile.groundDecor != null) {
                tile.groundDecor = null;
            }
        }
    }

}

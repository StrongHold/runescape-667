import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static467 {

    @OriginalMember(owner = "client!oo", name = "m", descriptor = "[I")
    public static int[] saturationColumns;

    @OriginalMember(owner = "client!oo", name = "a", descriptor = "(III)Lclient!pba;")
    public static PositionEntityNode getHead(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        return tile == null ? null : tile.head;
    }
}

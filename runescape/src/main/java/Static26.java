import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static26 {

    @OriginalMember(owner = "client!at", name = "a", descriptor = "(III)V")
    public static void method717(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        if (tile == null) {
            return;
        }
        Static109.hide(tile.wall);
        Static109.hide(tile.adjacentWall);
        if (tile.wall != null) {
            tile.wall = null;
        }
        if (tile.adjacentWall != null) {
            tile.adjacentWall = null;
        }
    }
}

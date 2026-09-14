import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static114 {

    @OriginalMember(owner = "client!dk", name = "a", descriptor = "(III)Lclient!tla;")
    public static WallDecor getWallDecor(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        return tile == null ? null : tile.wallDecor;
    }
}

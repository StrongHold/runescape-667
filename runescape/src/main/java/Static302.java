import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static302 {

    @OriginalMember(owner = "client!jk", name = "c", descriptor = "(III)Lclient!kp;")
    public static Wall getWall(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        return tile == null ? null : tile.wall;
    }
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static347 {

    @OriginalMember(owner = "client!ku", name = "a", descriptor = "(III)Lclient!pha;")
    public static Tile getTile(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        if (Static334.activeTiles[level][x][z] == null) {
            @Pc(33) boolean bridge = Static334.activeTiles[0][x][z] != null && Static334.activeTiles[0][x][z].tile != null;
            if (bridge && level >= Static299.tileMaxLevel - 1) {
                return null;
            }
            Static527.method7084(level, x, z);
        }
        return Static334.activeTiles[level][x][z];
    }
}

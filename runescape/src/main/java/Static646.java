import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static646 {

    @OriginalMember(owner = "client!uga", name = "a", descriptor = "(II)V")
    public static void applyBridge(@OriginalArg(0) int x, @OriginalArg(1) int z) {
        @Pc(7) Tile tile0 = Static334.activeTiles[0][x][z];

        for (@Pc(9) int level = 0; level < 3; level++) {
            @Pc(28) Tile tile = Static334.activeTiles[level][x][z] = Static334.activeTiles[level + 1][x][z];
            if (tile == null) {
                continue;
            }

            for (@Pc(33) PositionEntityNode node = tile.head; node != null; node = node.node) {
                @Pc(37) PositionEntity entity = node.entity;
                if (entity.x1 == x && entity.z1 == z) {
                    entity.level--;
                }
            }

            if (tile.groundDecor != null) {
                tile.groundDecor.level--;
            }
            if (tile.wall != null) {
                tile.wall.level--;
            }
            if (tile.adjacentWall != null) {
                tile.adjacentWall.level--;
            }
            if (tile.wallDecor != null) {
                tile.wallDecor.level--;
            }
            if (tile.wallDecor2 != null) {
                tile.wallDecor2.level--;
            }
        }

        if (Static334.activeTiles[0][x][z] == null) {
            Static334.activeTiles[0][x][z] = new Tile(0);
            Static334.activeTiles[0][x][z].level = 1;
        }

        Static334.activeTiles[0][x][z].tile = tile0;
        Static334.activeTiles[3][x][z] = null;
    }

    @OriginalMember(owner = "client!uga", name = "a", descriptor = "(BII)Z")
    public static boolean method8457(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x21) != 0;
    }

    @OriginalMember(owner = "client!uga", name = "a", descriptor = "(IB)I")
    public static int method8458(@OriginalArg(0) int arg0) {
        if (arg0 == 6406) {
            return 1;
        } else if (arg0 == 6409) {
            return 1;
        } else if (arg0 == 32841) {
            return 1;
        } else if (arg0 == 6410) {
            return 2;
        } else if (arg0 == 6407) {
            return 3;
        } else if (arg0 == 6408) {
            return 4;
        } else {
            throw new IllegalArgumentException("");
        }
    }
}

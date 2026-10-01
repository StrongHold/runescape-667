import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static323 {

    @OriginalMember(owner = "client!kda", name = "h", descriptor = "I")
    public static int anInt5120;

    @OriginalMember(owner = "client!kda", name = "a", descriptor = "()V")
    public static void shareLights() {
        for (@Pc(1) int level = Static296.tileMinLevel; level < Static299.tileMaxLevel; level++) {
            for (@Pc(4) int x = 0; x < Static619.tileMaxX; x++) {
                for (@Pc(7) int z = 0; z < Static662.tileMaxZ; z++) {
                    @Pc(16) Tile tile = Static334.activeTiles[level][x][z];
                    if (tile == null) {
                        continue;
                    }

                    @Pc(21) Wall wall = tile.wall;
                    @Pc(24) Wall adjacentWall = tile.adjacentWall;
                    if (wall != null && wall.method9290(0)) {
                        Static535.method7148(wall, level, x, z, 1, 1);

                        if (adjacentWall != null && adjacentWall.method9290(0)) {
                            Static535.method7148(adjacentWall, level, x, z, 1, 1);
                            adjacentWall.shareLight(0, false, Static665.aToolkit_15, 0, (byte) 126, 0, wall);
                            adjacentWall.stopSharingLight(27811);
                        }

                        wall.stopSharingLight(27811);
                    }

                    for (@Pc(76) PositionEntityNode node = tile.head; node != null; node = node.node) {
                        @Pc(80) PositionEntity entity = node.entity;
                        if (entity != null && entity.method9290(0)) {
                            Static535.method7148(entity, level, x, z, entity.x2 + 1 - entity.x1, entity.z2 - entity.z1 + 1);
                            entity.stopSharingLight(27811);
                        }
                    }

                    @Pc(120) GroundDecor groundDecor = tile.groundDecor;
                    if (groundDecor != null && groundDecor.method9290(0)) {
                        Static453.shareLight(groundDecor, level, x, z);
                        groundDecor.stopSharingLight(27811);
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!kda", name = "a", descriptor = "(IIBIII)I")
    public static int method4626(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4) {
        if (Static246.ground == null) {
            return 0;
        } else if (arg1 < 3) {
            @Pc(24) int local24 = arg4 >> 9;
            @Pc(28) int local28 = arg0 >> 9;
            if (arg2 < 0 || arg3 < 0 || Static720.mapWidth - 1 < arg2 || Static501.mapLength - 1 < arg3) {
                return 0;
            } else if (local24 >= 1 && local28 >= 1 && local24 <= Static720.mapWidth - 1 && local28 <= Static501.mapLength - 1) {
                @Pc(108) boolean local108 = (Static280.tileFlags[1][arg4 >> 9][arg0 >> 9] & 0x2) != 0;
                @Pc(136) boolean local136;
                @Pc(153) boolean local153;
                if ((arg4 & 0x1FF) == 0) {
                    local136 = (Static280.tileFlags[1][local24 - 1][arg0 >> 9] & 0x2) != 0;
                    local153 = (Static280.tileFlags[1][local24][arg0 >> 9] & 0x2) != 0;
                    if (local136 != local153) {
                        local108 = (Static280.tileFlags[1][arg2][arg3] & 0x2) != 0;
                    }
                }
                if ((arg0 & 0x1FF) == 0) {
                    local136 = (Static280.tileFlags[1][arg4 >> 9][local28 - 1] & 0x2) != 0;
                    local153 = (Static280.tileFlags[1][arg4 >> 9][local28] & 0x2) != 0;
                    if (local153 != local136) {
                        local108 = (Static280.tileFlags[1][arg2][arg3] & 0x2) != 0;
                    }
                }
                if (local108) {
                    arg1++;
                }
                return Static246.ground[arg1].averageHeight(arg4, arg0);
            } else {
                return 0;
            }
        } else {
            return Static246.ground[arg1].averageHeight(arg4, arg0);
        }
    }
}

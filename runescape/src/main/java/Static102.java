import com.jagex.core.constants.TileFlag;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static102 {

    @OriginalMember(owner = "client!dd", name = "O", descriptor = "[I")
    public static int[] anIntArray184;

    @OriginalMember(owner = "client!dd", name = "G", descriptor = "I")
    public static int lastAreaMode = 0;

    @OriginalMember(owner = "client!dd", name = "a", descriptor = "()V")
    public static void method2021() {
        Static514.activePickList = Static514.scenePickList;
    }

    @OriginalMember(owner = "client!dd", name = "d", descriptor = "(B)V")
    public static void method2022() {
        for (@Pc(15) int local15 = 0; local15 < 100; local15++) {
            ChatHistory.lines[local15] = null;
        }
        ChatHistory.length = 0;
    }

    @OriginalMember(owner = "client!dd", name = "b", descriptor = "(IIII)I")
    public static int averageHeight(@OriginalArg(0) int level, @OriginalArg(3) int x, @OriginalArg(2) int z) {
        if (Static246.ground == null) {
            return 0;
        }

        @Pc(21) int localX = x >> 9;
        @Pc(25) int localZ = z >> 9;
        if (localX < 0 || localZ < 0 || Static720.mapWidth - 1 < localX || Static501.mapLength - 1 < localZ) {
            return 0;
        }

        @Pc(56) int virtualLevel = level;
        if (level < 3 && (Static280.tileFlags[1][localX][localZ] & TileFlag.BRIDGE) != 0) {
            virtualLevel = level + 1;
        }

        return Static246.ground[virtualLevel].averageHeight(x, z);
    }

    @OriginalMember(owner = "client!dd", name = "a", descriptor = "(Lclient!qf;Z)Z")
    public static boolean method2026(@OriginalArg(0) PositionEntity entity, @OriginalArg(1) boolean dynamic) {
        @Pc(7) boolean underwater = Static246.ground == Static693.underwaterGround;
        @Pc(9) int colour = 0;
        @Pc(11) short depth = 0;
        @Pc(13) byte bias = 0;

        entity.updateBounds();

        if (entity.x1 < 0 || entity.z1 < 0 || entity.x2 >= Static619.tileMaxX || entity.z2 >= Static662.tileMaxZ) {
            return false;
        }

        @Pc(41) short offsetY = 0;
        for (@Pc(44) int x = entity.x1; x <= entity.x2; x++) {
            for (@Pc(48) int z = entity.z1; z <= entity.z2; z++) {
                @Pc(55) Tile tile = Static347.getTile(entity.level, x, z);

                if (tile != null) {
                    @Pc(61) PositionEntityNode node = PositionEntityNode.create(entity);

                    @Pc(64) PositionEntityNode head = tile.head;
                    if (head == null) {
                        tile.head = node;
                    } else {
                        while (head.node != null) {
                            head = head.node;
                        }

                        head.node = node;
                    }

                    if (underwater && (Static62.waterColour[x][z] & 0xFF000000) != 0) {
                        colour = Static62.waterColour[x][z];
                        depth = Static272.waterDepth[x][z];
                        bias = Static421.waterBias[x][z];
                    }

                    if (!dynamic && tile.groundDecor != null && offsetY < tile.groundDecor.offsetY) {
                        offsetY = tile.groundDecor.offsetY;
                    }
                }
            }
        }

        if (underwater && (colour & 0xFF000000) != 0) {
            for (@Pc(48) int x = entity.x1; x <= entity.x2; x++) {
                for (@Pc(159) int y = entity.z1; y <= entity.z2; y++) {
                    if ((Static62.waterColour[x][y] & 0xFF000000) == 0) {
                        Static62.waterColour[x][y] = colour;
                        Static272.waterDepth[x][y] = depth;
                        Static421.waterBias[x][y] = bias;
                    }
                }
            }
        }

        if (dynamic) {
            Static679.aPositionEntity[Static125.dynamicEntityCount++] = entity;
        } else {
            @Pc(48) int ground = Static246.ground == Static693.underwaterGround ? 1 : 0;

            if (!entity.isStationary()) {
                entity.nextEntity = Static468.dynamicEntities[ground];
                Static468.dynamicEntities[ground] = entity;
            } else if (entity.isTransparent(0)) {
                entity.nextEntity = Static398.transparentStationaryEntities[ground];
                Static398.transparentStationaryEntities[ground] = entity;
            } else {
                entity.nextEntity = Static576.opaqueStationaryEntities[ground];
                Static576.opaqueStationaryEntities[ground] = entity;
                Static75.hasOpaqueStationaryEntities = true;
            }
        }

        if (dynamic) {
            entity.y -= offsetY;
        }

        return true;
    }

}

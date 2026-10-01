import com.jagex.Entity;
import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static535 {

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(Lclient!eo;IIIII)V")
    public static void method7148(@OriginalArg(0) Entity entity, @OriginalArg(1) int level, @OriginalArg(2) int x, @OriginalArg(3) int z, @OriginalArg(4) int width, @OriginalArg(5) int height) {
        @Pc(1) boolean local1 = true;
        @Pc(3) int x1 = x;
        @Pc(7) int x2 = x + width;
        @Pc(11) int z1 = z - 1;
        @Pc(15) int z2 = z + height;

        for (@Pc(17) int curLevel = level; curLevel <= level + 1; curLevel++) {
            if (curLevel == Static299.tileMaxLevel) {
                continue;
            }

            for (@Pc(23) int curX = x1; curX <= x2; curX++) {
                if (curX < 0 || curX >= Static619.tileMaxX) {
                    continue;
                }

                for (@Pc(35) int curZ = z1; curZ <= z2; curZ++) {
                    if (curZ < 0 || curZ >= Static662.tileMaxZ || (local1 && curX < x2 && curZ < z2 && (curZ >= z || curX == x))) {
                        continue;
                    }

                    @Pc(77) Tile tile = Static334.activeTiles[curLevel][curX][curZ];
                    if (tile == null) {
                        continue;
                    }

                    @Pc(163) int tileHeight = (Static246.ground[curLevel].getHeight(curX, curZ) + Static246.ground[curLevel].getHeight(curX + 1, curZ) + Static246.ground[curLevel].getHeight(curX, curZ + 1) + Static246.ground[curLevel].getHeight(curX + 1, curZ + 1)) / 4 - (Static246.ground[level].getHeight(x, z) + Static246.ground[level].getHeight(x + 1, z) + Static246.ground[level].getHeight(x, z + 1) + Static246.ground[level].getHeight(x + 1, z + 1)) / 4;
                    @Pc(166) Wall wall = tile.wall;
                    @Pc(169) Wall adjacentWall = tile.adjacentWall;
                    if (wall != null && wall.method9290(0)) {
                        entity.shareLight(tileHeight, local1, Static665.aToolkit_15, (curZ - z) * Static340.anInt5586 + (1 - height) * EnvironmentLight.halfTileSize, (byte) 110, (curX - x) * Static340.anInt5586 + (1 - width) * EnvironmentLight.halfTileSize, wall);
                    }
                    if (adjacentWall != null && adjacentWall.method9290(0)) {
                        entity.shareLight(tileHeight, local1, Static665.aToolkit_15, (curZ - z) * Static340.anInt5586 + (1 - height) * EnvironmentLight.halfTileSize, (byte) 115, (curX - x) * Static340.anInt5586 + (1 - width) * EnvironmentLight.halfTileSize, adjacentWall);
                    }

                    for (@Pc(250) PositionEntityNode node = tile.head; node != null; node = node.node) {
                        @Pc(254) PositionEntity positionEntity = node.entity;

                        if (positionEntity != null && positionEntity.method9290(0) && (curX == positionEntity.x1 || curX == x1) && (curZ == positionEntity.z1 || curZ == z1)) {
                            @Pc(294) int local294 = positionEntity.x2 + 1 - positionEntity.x1;
                            @Pc(302) int local302 = positionEntity.z2 + 1 - positionEntity.z1;
                            entity.shareLight(tileHeight, local1, Static665.aToolkit_15, (positionEntity.z1 - z) * Static340.anInt5586 + (local302 - height) * EnvironmentLight.halfTileSize, (byte) 114, (positionEntity.x1 - x) * Static340.anInt5586 + (local294 - width) * EnvironmentLight.halfTileSize, positionEntity);
                        }
                    }
                }
            }

            x1--;
            local1 = false;
        }
    }
}

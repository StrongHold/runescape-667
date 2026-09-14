import com.jagex.core.constants.LocShapes;
import com.jagex.core.constants.TileFlag;
import com.jagex.game.Location;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static409 {

    @OriginalMember(owner = "client!mt", name = "P", descriptor = "F")
    public static float aFloat118;

    /**
     * Flood fills the roof removal stamp outward from one tile, so that every tile of the roof the
     * player or the camera is standing under is written with {@code roofStamp} and skipped by the
     * scene renderer. The fill stops at tiles that are not flagged {@link TileFlag#REMOVE_ROOF} and
     * at the roof edge locs packed into the queue entries, which keep the outer wall of the roof
     * standing. The world space bounds of what was hidden are accumulated into slot
     * {@code boundsIndex} of the five bounds arrays the renderer reads.
     *
     * <p>A queue entry is a pair of packed ints, one in each of the two queue arrays. The low 16
     * bits hold the tile coordinate, and each remaining byte holds one roof edge loc packed as
     * {@code shape | rotation << 6}, which {@link Static239#roofEdgeSideMask} turns back into a
     * wall side mask. The three locs of an entry are the roof edges that would seal the step the
     * entry came from, so a tile reached over any of them is left roofed.
     *
     * @return whether any roof was hidden.
     */
    @OriginalMember(owner = "client!mt", name = "a", descriptor = "(II[[[Lclient!pha;IIZ)Z")
    public static boolean hideRoofsFrom(@OriginalArg(1) int z, @OriginalArg(2) Tile[][][] tiles, @OriginalArg(3) int boundsIndex, @OriginalArg(4) int x, @OriginalArg(5) boolean permanent) {
        @Pc(21) byte roofStamp = permanent ? 1 : (byte) (Static198.anInt3276 & 0xFF);
        if (Static328.aByteArrayArrayArray4[Camera.renderingLevel][x][z] == roofStamp) {
            return false;
        } else if ((Static280.tileFlags[Camera.renderingLevel][x][z] & TileFlag.REMOVE_ROOF) == 0) {
            return false;
        } else {
            @Pc(52) byte queueStart = 0;
            Static278.anIntArray351[0] = x;
            @Pc(58) int readIndex = 0;
            @Pc(61) int writeIndex = queueStart + 1;
            Static98.anIntArray176[0] = z;
            Static328.aByteArrayArrayArray4[Camera.renderingLevel][x][z] = roofStamp;
            while (readIndex != writeIndex) {
                @Pc(78) int tileX = Static278.anIntArray351[readIndex] & 0xFFFF;
                @Pc(86) int edgeLoc0 = Static278.anIntArray351[readIndex] >> 16 & 0xFF;
                @Pc(94) int edgeLoc1 = Static278.anIntArray351[readIndex] >> 24 & 0xFF;
                @Pc(100) int tileZ = Static98.anIntArray176[readIndex] & 0xFFFF;
                @Pc(108) int edgeLoc2 = Static98.anIntArray176[readIndex] >> 16 & 0xFF;
                readIndex = readIndex + 1 & 0xFFF;
                @Pc(116) boolean outsideRoof = false;
                if ((Static280.tileFlags[Camera.renderingLevel][tileX][tileZ] & TileFlag.REMOVE_ROOF) == 0) {
                    outsideRoof = true;
                }
                @Pc(133) boolean stamped = false;
                @Pc(139) int local139;
                @Pc(185) int local185;
                @Pc(235) int local235;
                if (tiles != null) {
                    nextLevel:
                    for (local139 = Camera.renderingLevel + 1; local139 <= 3; local139++) {
                        if (tiles[local139] != null && (Static280.tileFlags[local139][tileX][tileZ] & TileFlag.ZERO_LEVEL) == 0) {
                            @Pc(341) PositionEntity entity;
                            @Pc(351) int local351;
                            @Pc(331) Tile tile;
                            @Pc(337) PositionEntityNode node;
                            if (outsideRoof && tiles[local139][tileX][tileZ] != null) {
                                if (tiles[local139][tileX][tileZ].wall != null) {
                                    local185 = Static239.roofEdgeSideMask(edgeLoc0);
                                    if (tiles[local139][tileX][tileZ].wall.sideMask == local185 || tiles[local139][tileX][tileZ].adjacentWall != null && local185 == tiles[local139][tileX][tileZ].adjacentWall.sideMask) {
                                        continue;
                                    }
                                    if (edgeLoc1 != 0) {
                                        local235 = Static239.roofEdgeSideMask(edgeLoc1);
                                        if (tiles[local139][tileX][tileZ].wall.sideMask == local235 || tiles[local139][tileX][tileZ].adjacentWall != null && local235 == tiles[local139][tileX][tileZ].adjacentWall.sideMask) {
                                            continue;
                                        }
                                    }
                                    if (edgeLoc2 != 0) {
                                        local235 = Static239.roofEdgeSideMask(edgeLoc2);
                                        if (tiles[local139][tileX][tileZ].wall.sideMask == local235 || tiles[local139][tileX][tileZ].adjacentWall != null && tiles[local139][tileX][tileZ].adjacentWall.sideMask == local235) {
                                            continue;
                                        }
                                    }
                                }
                                tile = tiles[local139][tileX][tileZ];
                                if (tile.head != null) {
                                    for (node = tile.head; node != null; node = node.node) {
                                        entity = node.entity;
                                        if (entity instanceof Location) {
                                            @Pc(347) Location loc = (Location) entity;
                                            local351 = loc.getShape();
                                            @Pc(355) int rotation = loc.getRotation();
                                            if (local351 == LocShapes.ROOFEDGE_SQUARECORNER) {
                                                local351 = LocShapes.ROOFEDGE_DIAGONALCORNER;
                                            }
                                            @Pc(368) int locCode = local351 | rotation << 6;
                                            if (locCode == edgeLoc0 || edgeLoc1 != 0 && edgeLoc1 == locCode || edgeLoc2 != 0 && locCode == edgeLoc2) {
                                                continue nextLevel;
                                            }
                                        }
                                    }
                                }
                            }
                            tile = tiles[local139][tileX][tileZ];
                            if (tile != null && tile.head != null) {
                                for (node = tile.head; node != null; node = node.node) {
                                    entity = node.entity;
                                    if (entity.x2 != entity.x1 || entity.z1 != entity.z2) {
                                        for (@Pc(444) int entityX = entity.x1; entityX <= entity.x2; entityX++) {
                                            for (local351 = entity.z1; local351 <= entity.z2; local351++) {
                                                Static328.aByteArrayArrayArray4[local139][entityX][local351] = roofStamp;
                                            }
                                        }
                                    }
                                }
                            }
                            Static328.aByteArrayArrayArray4[local139][tileX][tileZ] = roofStamp;
                            stamped = true;
                        }
                    }
                }
                if (stamped) {
                    local139 = Static246.ground[Camera.renderingLevel + 1].getHeight(tileX, tileZ);
                    if (Static482.anIntArray588[boundsIndex] < local139) {
                        Static482.anIntArray588[boundsIndex] = local139;
                    }
                    local185 = tileX << 9;
                    local235 = tileZ << 9;
                    if (Static9.anIntArray18[boundsIndex] > local185) {
                        Static9.anIntArray18[boundsIndex] = local185;
                    } else if (Static457.anIntArray552[boundsIndex] < local185) {
                        Static457.anIntArray552[boundsIndex] = local185;
                    }
                    if (local235 < Static682.anIntArray817[boundsIndex]) {
                        Static682.anIntArray817[boundsIndex] = local235;
                    } else if (local235 > Static153.anIntArray235[boundsIndex]) {
                        Static153.anIntArray235[boundsIndex] = local235;
                    }
                }
                if (!outsideRoof) {
                    if (tileX >= 1 && roofStamp != Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX - 1][tileZ]) {
                        Static278.anIntArray351[writeIndex] = 0xD3000000 | 0x120000 | tileX - 1;
                        Static98.anIntArray176[writeIndex] = tileZ | 0x130000;
                        Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX - 1][tileZ] = roofStamp;
                        writeIndex = writeIndex + 1 & 0xFFF;
                    }
                    tileZ++;
                    if (tileZ < Static501.mapLength) {
                        if (tileX - 1 >= 0 && roofStamp != Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX - 1][tileZ] && (Static280.tileFlags[Camera.renderingLevel][tileX][tileZ] & TileFlag.REMOVE_ROOF) == 0 && (Static280.tileFlags[Camera.renderingLevel][tileX - 1][tileZ - 1] & TileFlag.REMOVE_ROOF) == 0) {
                            Static278.anIntArray351[writeIndex] = tileX - 1 | 0x120000 | 0x52000000;
                            Static98.anIntArray176[writeIndex] = tileZ | 0x130000;
                            writeIndex = writeIndex + 1 & 0xFFF;
                            Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX - 1][tileZ] = roofStamp;
                        }
                        if (roofStamp != Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX][tileZ]) {
                            Static278.anIntArray351[writeIndex] = 0x13000000 | 0x520000 | tileX;
                            Static98.anIntArray176[writeIndex] = tileZ | 0x530000;
                            Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX][tileZ] = roofStamp;
                            writeIndex = writeIndex + 1 & 0xFFF;
                        }
                        if (Static720.mapWidth > tileX + 1 && roofStamp != Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX + 1][tileZ] && (Static280.tileFlags[Camera.renderingLevel][tileX][tileZ] & TileFlag.REMOVE_ROOF) == 0 && (Static280.tileFlags[Camera.renderingLevel][tileX + 1][tileZ - 1] & TileFlag.REMOVE_ROOF) == 0) {
                            Static278.anIntArray351[writeIndex] = 0x92000000 | 0x520000 | tileX + 1;
                            Static98.anIntArray176[writeIndex] = tileZ | 0x530000;
                            Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX + 1][tileZ] = roofStamp;
                            writeIndex = writeIndex + 1 & 0xFFF;
                        }
                    }
                    tileZ--;
                    if (tileX + 1 < Static720.mapWidth && Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX + 1][tileZ] != roofStamp) {
                        Static278.anIntArray351[writeIndex] = tileX + 1 | 0x920000 | 0x53000000;
                        Static98.anIntArray176[writeIndex] = tileZ | 0x930000;
                        Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX + 1][tileZ] = roofStamp;
                        writeIndex = writeIndex + 1 & 0xFFF;
                    }
                    tileZ--;
                    if (tileZ >= 0) {
                        if (tileX - 1 >= 0 && Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX - 1][tileZ] != roofStamp && (Static280.tileFlags[Camera.renderingLevel][tileX][tileZ] & TileFlag.REMOVE_ROOF) == 0 && (Static280.tileFlags[Camera.renderingLevel][tileX - 1][tileZ + 1] & TileFlag.REMOVE_ROOF) == 0) {
                            Static278.anIntArray351[writeIndex] = tileX - 1 | 0xD20000 | 0x12000000;
                            Static98.anIntArray176[writeIndex] = tileZ | 0xD30000;
                            writeIndex = writeIndex + 1 & 0xFFF;
                            Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX - 1][tileZ] = roofStamp;
                        }
                        if (Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX][tileZ] != roofStamp) {
                            Static278.anIntArray351[writeIndex] = 0x93000000 | 0xD20000 | tileX;
                            Static98.anIntArray176[writeIndex] = tileZ | 0xD30000;
                            Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX][tileZ] = roofStamp;
                            writeIndex = writeIndex + 1 & 0xFFF;
                        }
                        if (tileX + 1 < Static720.mapWidth && Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX + 1][tileZ] != roofStamp && (Static280.tileFlags[Camera.renderingLevel][tileX][tileZ] & TileFlag.REMOVE_ROOF) == 0 && (Static280.tileFlags[Camera.renderingLevel][tileX + 1][tileZ + 1] & TileFlag.REMOVE_ROOF) == 0) {
                            Static278.anIntArray351[writeIndex] = tileX + 1 | 0x920000 | 0xD2000000;
                            Static98.anIntArray176[writeIndex] = tileZ | 0x930000;
                            Static328.aByteArrayArrayArray4[Camera.renderingLevel][tileX + 1][tileZ] = roofStamp;
                            writeIndex = writeIndex + 1 & 0xFFF;
                        }
                    }
                }
            }
            if (Static482.anIntArray588[boundsIndex] != -1000000) {
                Static482.anIntArray588[boundsIndex] += 40;
                Static9.anIntArray18[boundsIndex] -= 512;
                Static457.anIntArray552[boundsIndex] += 512;
                Static153.anIntArray235[boundsIndex] += 512;
                Static682.anIntArray817[boundsIndex] -= 512;
            }
            return true;
        }
    }

    /**
     * Draws a one pixel Bresenham line into the texture plane rows. Both endpoints must already lie
     * inside the clip bounds, because nothing here is clipped.
     */
    @OriginalMember(owner = "client!mt", name = "a", descriptor = "(IIIBII)V")
    public static void drawLineUnclipped(@OriginalArg(0) int x0, @OriginalArg(1) int x1, @OriginalArg(2) int rgb, @OriginalArg(4) int y1, @OriginalArg(5) int y0) {
        @Pc(8) int dy = y1 - y0;
        @Pc(13) int dx = x1 - x0;
        if (dx == 0) {
            if (dy != 0) {
                Static87.drawVerticalLineUnclipped(y1, y0, rgb, x0);
            }
        } else if (dy == 0) {
            Static297.drawHorizontalLineUnclipped(y0, x1, rgb, x0);
        } else {
            if (dx < 0) {
                dx = -dx;
            }
            if (dy < 0) {
                dy = -dy;
            }
            @Pc(62) boolean steep = dy > dx;
            @Pc(66) int local66;
            @Pc(68) int local68;
            if (steep) {
                local66 = x0;
                local68 = x1;
                x0 = y0;
                y0 = local66;
                x1 = y1;
                y1 = local68;
            }
            if (x0 > x1) {
                local66 = x0;
                x0 = x1;
                local68 = y0;
                y0 = y1;
                x1 = local66;
                y1 = local68;
            }
            local66 = y0;
            local68 = x1 - x0;
            @Pc(111) int minorSpan = y1 - y0;
            @Pc(116) int error = -(local68 >> 1);
            @Pc(124) int step = y1 > y0 ? 1 : -1;
            if (minorSpan < 0) {
                minorSpan = -minorSpan;
            }
            @Pc(133) int major;
            if (steep) {
                for (major = x0; major <= x1; major++) {
                    Static723.anIntArrayArray266[major][local66] = rgb;
                    error += minorSpan;
                    if (error > 0) {
                        error -= local68;
                        local66 += step;
                    }
                }
            } else {
                for (major = x0; major <= x1; major++) {
                    Static723.anIntArrayArray266[local66][major] = rgb;
                    error += minorSpan;
                    if (error > 0) {
                        error -= local68;
                        local66 += step;
                    }
                }
            }
        }
    }
}

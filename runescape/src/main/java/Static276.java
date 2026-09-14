import com.jagex.core.constants.TileFlag;
import com.jagex.core.util.JagException;
import com.jagex.game.camera.CameraMode;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static276 {

    /**
     * Discards the occluders and the per tile occlusion state left over from the previous scene, and
     * turns occlusion culling back on for every toolkit but the software one.
     */
    @OriginalMember(owner = "client!ila", name = "a", descriptor = "(I)V")
    public static void resetOccluders() {
        Static317.anInt5046 = 0;
        Static442.occludersDirty = false;
        Static384.aLocOccluderArray2 = new LocOccluder[500];
        Static150.locOccluderCount = 0;
        Static446.tileOcclusionCache = new int[Static299.tileMaxLevel][Static619.tileMaxX + 1][Static662.tileMaxZ + 1];
        Static663.anInt9874 = Static340.anInt5586;
        Static86.anInt1803 = Static340.anInt5586;
        Static444.anInt6751 = 0;
        Static607.aLocOccluderArray4 = new LocOccluder[2000];
        Static285.locOccluders = new LocOccluder[1000];
        Static469.activeOccluderCount = 0;
        Static560.aLocOccluderArray3 = new LocOccluder[500];
        if (Static665.aToolkit_15 instanceof oa) {
            Static18.occlude = false;
        } else {
            Static18.occlude = true;
        }
    }

    /**
     * Hides the roofs the player is under and the roofs the camera looks through, for the frame about
     * to be drawn. One column of the stamp array is aged out per frame so that stamps from earlier
     * frames stop matching. Slot 0 of the bounds arrays holds the roof over the player, slot 1 the
     * first roof the camera ray crosses on its way in.
     */
    @OriginalMember(owner = "client!ila", name = "b", descriptor = "(I)V")
    public static void hideRoofsForFrame() {
        if (ClientOptions.instance.removeRoofsOverride.getValue() != 2) {
            return;
        }
        @Pc(21) byte staleStamp = (byte) (Static198.anInt3276 - 4 & 0xFF);
        @Pc(25) int resetTileX = Static198.anInt3276 % Static720.mapWidth;
        @Pc(30) int i;
        for (@Pc(27) int level = 0; level < 4; level++) {
            for (i = 0; i < Static501.mapLength; i++) {
                Static328.aByteArrayArrayArray4[level][resetTileX][i] = staleStamp;
            }
        }
        if (Camera.renderingLevel == 3) {
            return;
        }
        for (i = 0; i < 2; i++) {
            Static482.anIntArray588[i] = -1000000;
            Static9.anIntArray18[i] = 1000000;
            Static457.anIntArray552[i] = 0;
            Static682.anIntArray817[i] = 1000000;
            Static153.anIntArray235[i] = 0;
        }
        @Pc(92) int targetX = PlayerEntity.self.x;
        @Pc(95) int targetZ = PlayerEntity.self.z;
        @Pc(149) int local149;
        if (Camera.mode != CameraMode.MODE_DEFAULT && Camera.anInt10376 == -1) {
            local149 = Static102.averageHeight(Camera.renderingLevel, Camera.x, Camera.z);
            if (local149 - Camera.y < 3200 && (Static280.tileFlags[Camera.renderingLevel][Camera.x >> 9][Camera.z >> 9] & TileFlag.REMOVE_ROOF) != 0) {
                Static409.hideRoofsFrom(Camera.z >> 9, Static334.activeTiles, 1, Camera.x >> 9, false);
                return;
            }
            return;
        }
        if (Camera.mode != CameraMode.MODE_DEFAULT) {
            targetX = Camera.anInt10376;
            targetZ = Camera.anInt10383;
        }
        if ((Static280.tileFlags[Camera.renderingLevel][targetX >> 9][targetZ >> 9] & TileFlag.REMOVE_ROOF) != 0) {
            Static409.hideRoofsFrom(targetZ >> 9, Static334.activeTiles, 0, targetX >> 9, false);
        }
        if (Camera.pitch >= 2560) {
            return;
        }
        local149 = Camera.x >> 9;
        @Pc(153) int rayZ = Camera.z >> 9;
        @Pc(157) int targetTileX = targetX >> 9;
        @Pc(161) int targetTileZ = targetZ >> 9;
        @Pc(169) int deltaX;
        if (targetTileX > local149) {
            deltaX = targetTileX - local149;
        } else {
            deltaX = local149 - targetTileX;
        }
        @Pc(186) int deltaZ;
        if (rayZ < targetTileZ) {
            deltaZ = targetTileZ - rayZ;
        } else {
            deltaZ = rayZ - targetTileZ;
        }
        if ((deltaX != 0 || deltaZ != 0) && deltaX > (-Static720.mapWidth) && deltaX < Static720.mapWidth && -Static501.mapLength < deltaZ && Static501.mapLength > deltaZ) {
            @Pc(278) int slope;
            @Pc(280) int error;
            if (deltaX <= deltaZ) {
                slope = deltaX * 65536 / deltaZ;
                error = 32768;
                while (targetTileZ != rayZ) {
                    if (targetTileZ > rayZ) {
                        rayZ++;
                    } else if (targetTileZ < rayZ) {
                        rayZ--;
                    }
                    if ((Static280.tileFlags[Camera.renderingLevel][local149][rayZ] & TileFlag.REMOVE_ROOF) != 0) {
                        Static409.hideRoofsFrom(rayZ, Static334.activeTiles, 1, local149, false);
                        return;
                    }
                    error += slope;
                    if (error >= 65536) {
                        error -= 65536;
                        if (local149 < targetTileX) {
                            local149++;
                        } else if (local149 > targetTileX) {
                            local149--;
                        }
                        if ((Static280.tileFlags[Camera.renderingLevel][local149][rayZ] & TileFlag.REMOVE_ROOF) != 0) {
                            Static409.hideRoofsFrom(rayZ, Static334.activeTiles, 1, local149, false);
                            return;
                        }
                    }
                }
                return;
            }
            slope = deltaZ * 65536 / deltaX;
            error = 32768;
            while (targetTileX != local149) {
                if (local149 < targetTileX) {
                    local149++;
                } else if (targetTileX < local149) {
                    local149--;
                }
                if ((Static280.tileFlags[Camera.renderingLevel][local149][rayZ] & TileFlag.REMOVE_ROOF) != 0) {
                    Static409.hideRoofsFrom(rayZ, Static334.activeTiles, 1, local149, false);
                    return;
                }
                error += slope;
                if (error >= 65536) {
                    if (targetTileZ > rayZ) {
                        rayZ++;
                    } else if (targetTileZ < rayZ) {
                        rayZ--;
                    }
                    error -= 65536;
                    if ((Static280.tileFlags[Camera.renderingLevel][local149][rayZ] & TileFlag.REMOVE_ROOF) != 0) {
                        Static409.hideRoofsFrom(rayZ, Static334.activeTiles, 1, local149, false);
                        return;
                    }
                }
            }
            return;
        }
        JagException.sendTrace(null, "RC: " + local149 + "," + rayZ + " " + targetTileX + "," + targetTileZ + " " + WorldMap.areaBaseX + "," + WorldMap.areaBaseZ);
        return;
    }
}

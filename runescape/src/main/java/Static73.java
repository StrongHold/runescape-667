import com.jagex.Client;
import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static73 {

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIILclient!kp;I)Z")
    public static boolean isWallOccluded(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(3) Wall wall, @OriginalArg(4) int level) {
        if (!Static18.occlude || !Static29.aBoolean60) {
            return false;
        } else if (Static432.occludedPixelCount < 100) {
            return false;
        } else if (Static588.method7714(z, level, x)) {
            @Pc(31) int worldX = x << EnvironmentLight.anInt1066;
            @Pc(35) int worldZ = z << EnvironmentLight.anInt1066;
            @Pc(45) int baseY = Static246.ground[level].getHeight(x, z) - 1;
            @Pc(51) int topY = wall.getMinY(2) + baseY;
            if (wall.aShort58 == 1) {
                if (!Static172.isTriangleOccluded(worldX, worldZ, topY, topY, baseY, worldZ, Static340.anInt5586 + worldZ, worldX, worldX)) {
                    return false;
                } else if (Static172.isTriangleOccluded(worldX, worldZ, topY, baseY, baseY, worldZ + Static340.anInt5586, worldZ + Static340.anInt5586, worldX, worldX)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (wall.aShort58 == 2) {
                if (!Static172.isTriangleOccluded(worldX + Static340.anInt5586, worldZ - -Static340.anInt5586, topY, topY, baseY, worldZ + Static340.anInt5586, Static340.anInt5586 + worldZ, worldX, worldX)) {
                    return false;
                } else if (Static172.isTriangleOccluded(worldX + Static340.anInt5586, worldZ - -Static340.anInt5586, baseY, topY, baseY, Static340.anInt5586 + worldZ, worldZ + Static340.anInt5586, worldX, worldX + Static340.anInt5586)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (wall.aShort58 == 4) {
                if (!Static172.isTriangleOccluded(Static340.anInt5586 + worldX, worldZ, topY, topY, baseY, worldZ, Static340.anInt5586 + worldZ, worldX - -Static340.anInt5586, Static340.anInt5586 + worldX)) {
                    return false;
                } else if (Static172.isTriangleOccluded(worldX + Static340.anInt5586, worldZ, topY, baseY, baseY, Static340.anInt5586 + worldZ, Static340.anInt5586 + worldZ, worldX + Static340.anInt5586, Static340.anInt5586 + worldX)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (wall.aShort58 == 8) {
                if (!Static172.isTriangleOccluded(Static340.anInt5586 + worldX, worldZ, topY, topY, baseY, worldZ, worldZ, worldX, worldX)) {
                    return false;
                } else if (Static172.isTriangleOccluded(Static340.anInt5586 + worldX, worldZ, baseY, topY, baseY, worldZ, worldZ, worldX, worldX + Static340.anInt5586)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (wall.aShort58 == 16) {
                if (Static318.method8557(EnvironmentLight.anInt3993, topY, worldX, EnvironmentLight.anInt3993 + worldZ, baseY, EnvironmentLight.anInt3993)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (wall.aShort58 == 32) {
                if (Static318.method8557(EnvironmentLight.anInt3993, topY, worldX + EnvironmentLight.anInt3993, EnvironmentLight.anInt3993 + worldZ, baseY, EnvironmentLight.anInt3993)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (wall.aShort58 == 64) {
                if (Static318.method8557(EnvironmentLight.anInt3993, topY, EnvironmentLight.anInt3993 + worldX, worldZ, baseY, EnvironmentLight.anInt3993)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (wall.aShort58 != 128) {
                return true;
            } else if (Static318.method8557(EnvironmentLight.anInt3993, topY, worldX, worldZ, baseY, EnvironmentLight.anInt3993)) {
                Static679.occludedWallCount++;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "([[BBLclient!taa;)V")
    public static void decodeStaticArea(@OriginalArg(0) byte[][] data, @OriginalArg(2) MapRegion region) {
        @Pc(6) int length = data.length;

        for (@Pc(8) int i = 0; i < length; i++) {
            @Pc(13) byte[] chunkData = data[i];

            if (chunkData != null) {
                @Pc(20) Packet packet = new Packet(chunkData);
                @Pc(26) int zoneX = Static89.zoneIds[i] >> 8;
                @Pc(32) int zoneZ = Static89.zoneIds[i] & 0xFF;
                @Pc(38) int absX = (zoneX * 64) - WorldMap.areaBaseX;
                @Pc(45) int absZ = (zoneZ * 64) - WorldMap.areaBaseZ;
                Static557.method7331();
                region.decodeMapSquare(packet, Client.collisionMaps, absX, absZ, WorldMap.areaBaseX, WorldMap.areaBaseZ);
                region.decodeStaticEnvironment(absZ, packet, absX, Toolkit.active);
            }
        }

        for (@Pc(78) int i = 0; i < length; i++) {
            @Pc(90) int x = (Static89.zoneIds[i] >> 8) * 64 - WorldMap.areaBaseX;
            @Pc(26) int z = (Static89.zoneIds[i] & 0xFF) * 64 - WorldMap.areaBaseZ;
            @Pc(105) byte[] chunkData = data[i];
            if (chunkData == null && Static525.areaCenterZ < 800) {
                Static557.method7331();
                region.method7880(x, z);
            }
        }
    }
}

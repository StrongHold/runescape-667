import com.jagex.Client;
import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static73 {

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIILclient!kp;I)Z")
    public static boolean method9308(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) Wall arg2, @OriginalArg(4) int arg3) {
        if (!Static18.occlude || !Static29.aBoolean60) {
            return false;
        } else if (Static432.occludedPixelCount < 100) {
            return false;
        } else if (Static588.method7714(arg1, arg3, arg0)) {
            @Pc(31) int local31 = arg0 << EnvironmentLight.anInt1066;
            @Pc(35) int local35 = arg1 << EnvironmentLight.anInt1066;
            @Pc(45) int local45 = Static246.ground[arg3].getHeight(arg0, arg1) - 1;
            @Pc(51) int local51 = arg2.getMinY(2) + local45;
            if (arg2.aShort58 == 1) {
                if (!Static172.isTriangleOccluded(local31, local35, local51, local51, local45, local35, Static340.anInt5586 + local35, local31, local31)) {
                    return false;
                } else if (Static172.isTriangleOccluded(local31, local35, local51, local45, local45, local35 + Static340.anInt5586, local35 + Static340.anInt5586, local31, local31)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (arg2.aShort58 == 2) {
                if (!Static172.isTriangleOccluded(local31 + Static340.anInt5586, local35 - -Static340.anInt5586, local51, local51, local45, local35 + Static340.anInt5586, Static340.anInt5586 + local35, local31, local31)) {
                    return false;
                } else if (Static172.isTriangleOccluded(local31 + Static340.anInt5586, local35 - -Static340.anInt5586, local45, local51, local45, Static340.anInt5586 + local35, local35 + Static340.anInt5586, local31, local31 + Static340.anInt5586)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (arg2.aShort58 == 4) {
                if (!Static172.isTriangleOccluded(Static340.anInt5586 + local31, local35, local51, local51, local45, local35, Static340.anInt5586 + local35, local31 - -Static340.anInt5586, Static340.anInt5586 + local31)) {
                    return false;
                } else if (Static172.isTriangleOccluded(local31 + Static340.anInt5586, local35, local51, local45, local45, Static340.anInt5586 + local35, Static340.anInt5586 + local35, local31 + Static340.anInt5586, Static340.anInt5586 + local31)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (arg2.aShort58 == 8) {
                if (!Static172.isTriangleOccluded(Static340.anInt5586 + local31, local35, local51, local51, local45, local35, local35, local31, local31)) {
                    return false;
                } else if (Static172.isTriangleOccluded(Static340.anInt5586 + local31, local35, local45, local51, local45, local35, local35, local31, local31 + Static340.anInt5586)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (arg2.aShort58 == 16) {
                if (Static318.method8557(EnvironmentLight.anInt3993, local51, local31, EnvironmentLight.anInt3993 + local35, local45, EnvironmentLight.anInt3993)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (arg2.aShort58 == 32) {
                if (Static318.method8557(EnvironmentLight.anInt3993, local51, local31 + EnvironmentLight.anInt3993, EnvironmentLight.anInt3993 + local35, local45, EnvironmentLight.anInt3993)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (arg2.aShort58 == 64) {
                if (Static318.method8557(EnvironmentLight.anInt3993, local51, EnvironmentLight.anInt3993 + local31, local35, local45, EnvironmentLight.anInt3993)) {
                    Static679.occludedWallCount++;
                    return true;
                } else {
                    return false;
                }
            } else if (arg2.aShort58 != 128) {
                return true;
            } else if (Static318.method8557(EnvironmentLight.anInt3993, local51, local31, local35, local45, EnvironmentLight.anInt3993)) {
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
            @Pc(13) byte[] b = data[i];

            if (b != null) {
                @Pc(20) Packet packet = new Packet(b);
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
            @Pc(105) byte[] local105 = data[i];
            if (local105 == null && Static525.areaCenterZ < 800) {
                Static557.method7331();
                region.method7880(x, z);
            }
        }
    }
}

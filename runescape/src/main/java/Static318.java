import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static318 {

    @OriginalMember(owner = "client!kb", name = "q", descriptor = "F")
    public static float aFloat210;

    @OriginalMember(owner = "client!kb", name = "a", descriptor = "(IIZI)I")
    public static int method8555(@OriginalArg(0) int lightness, @OriginalArg(1) int saturation, @OriginalArg(3) int hue) {
        if (lightness > 243) {
            saturation >>= 0x4;
        } else if (lightness > 217) {
            saturation >>= 0x3;
        } else if (lightness > 192) {
            saturation >>= 0x2;
        } else if (lightness > 179) {
            saturation >>= 0x1;
        }
        return (lightness >> 1) + (saturation >> 5 << 7) + ((hue >> 2 & 0x3F) << 10);
    }

    @OriginalMember(owner = "client!kb", name = "a", descriptor = "(IIIIIII)Z")
    public static boolean method8557(@OriginalArg(0) int sizeX, @OriginalArg(1) int sizeY, @OriginalArg(2) int minX, @OriginalArg(4) int minZ, @OriginalArg(5) int minY, @OriginalArg(6) int sizeZ) {
        @Pc(8) int maxX = minX + sizeX;
        @Pc(12) int maxY = sizeY + minY;
        @Pc(16) int maxZ = sizeZ + minZ;
        if (!Static172.isTriangleOccluded(maxX, minZ, maxY, maxY, maxY, maxZ, maxZ, minX, minX)) {
            return false;
        } else if (Static172.isTriangleOccluded(maxX, minZ, maxY, maxY, maxY, minZ, maxZ, minX, maxX)) {
            if (minX >= Static499.anInt7492) {
                if (!Static172.isTriangleOccluded(maxX, maxZ, maxY, maxY, minY, minZ, maxZ, maxX, maxX)) {
                    return false;
                }
                if (!Static172.isTriangleOccluded(maxX, maxZ, minY, maxY, minY, minZ, minZ, maxX, maxX)) {
                    return false;
                }
            } else if (!Static172.isTriangleOccluded(minX, maxZ, maxY, maxY, minY, minZ, maxZ, minX, minX)) {
                return false;
            } else if (!Static172.isTriangleOccluded(minX, maxZ, minY, maxY, minY, minZ, minZ, minX, minX)) {
                return false;
            }
            if (minZ >= Static715.cameraZ) {
                if (!Static172.isTriangleOccluded(maxX, maxZ, maxY, maxY, minY, maxZ, maxZ, minX, minX)) {
                    return false;
                }
                if (!Static172.isTriangleOccluded(maxX, maxZ, minY, maxY, minY, maxZ, maxZ, minX, maxX)) {
                    return false;
                }
            } else if (!Static172.isTriangleOccluded(maxX, minZ, maxY, maxY, minY, minZ, minZ, minX, minX)) {
                return false;
            } else if (!Static172.isTriangleOccluded(maxX, minZ, minY, maxY, minY, minZ, minZ, minX, maxX)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}

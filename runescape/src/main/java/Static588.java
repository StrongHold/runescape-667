import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static588 {

    @OriginalMember(owner = "client!sj", name = "a", descriptor = "(ZZ)Z")
    public static boolean or(@OriginalArg(0) boolean arg0, @OriginalArg(1) boolean arg1) {
        return arg0 | arg1;
    }

    @OriginalMember(owner = "client!sj", name = "a", descriptor = "(I)V")
    public static void method7713() {
        Static425.toolkit.xa(((float) ClientOptions.instance.brightness.getValue() * 0.1F + 0.7F) * Static318.aFloat210);
        Static425.toolkit.ZA(Static448.anInt6801, Static688.aFloat216, Static683.aFloat215, (float) (Static344.anInt5617 << 2), (float) (Static417.anInt6400 << 2), (float) (Static331.anInt5441 << 2));
        Static425.toolkit.method7973(Static425.aClass67_6);
    }

    /**
     * Answers whether the occluders hide the ground tile at x, z on the given level, and remembers
     * the answer in {@link Static446#tileOcclusionCache} for the rest of the frame. The tile counts
     * as hidden only when both of the triangles its ground quad is split into rasterise as hidden.
     */
    @OriginalMember(owner = "client!sj", name = "a", descriptor = "(ZIII)Z")
    public static boolean method7714(@OriginalArg(1) int z, @OriginalArg(2) int level, @OriginalArg(3) int x) {
        if (!Static18.occlude || !Static29.aBoolean60) {
            return false;
        } else if (Static432.occludedPixelCount < 100) {
            return false;
        } else {
            @Pc(37) int cached = Static446.tileOcclusionCache[level][x][z];
            if (-Static675.anInt10155 == cached) {
                return false;
            } else if (Static675.anInt10155 == cached) {
                return true;
            } else if (Static693.underwaterGround == Static246.ground) {
                return false;
            } else {
                @Pc(64) int worldX = x << EnvironmentLight.anInt1066;
                @Pc(68) int worldZ = z << EnvironmentLight.anInt1066;
                if (Static172.isTriangleOccluded(worldX + Static340.anInt5586 - 1, worldZ + 1, Static246.ground[level].getHeight(x + 1, z + 1), Static246.ground[level].getHeight(x, z + 1), Static246.ground[level].getHeight(x, z), worldZ + Static340.anInt5586 - 1, Static340.anInt5586 + worldZ + -1, worldX + 1, worldX + 1) && Static172.isTriangleOccluded(Static340.anInt5586 + worldX - 1, worldZ + 1, Static246.ground[level].getHeight(x + 1, z), Static246.ground[level].getHeight(x + 1, z + 1), Static246.ground[level].getHeight(x, z), worldZ + 1, Static340.anInt5586 + worldZ + -1, worldX + 1, Static340.anInt5586 + -1 + worldX)) {
                    Static298.occludedGroundCount++;
                    Static446.tileOcclusionCache[level][x][z] = Static675.anInt10155;
                    return true;
                } else {
                    Static446.tileOcclusionCache[level][x][z] = -Static675.anInt10155;
                    return false;
                }
            }
        }
    }

}

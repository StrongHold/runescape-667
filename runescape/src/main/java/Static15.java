import com.jagex.graphics.BoundingCylinder;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static15 {

    /**
     * Answers whether the occluders hide an entity that stands on the tile range from minX, minZ to
     * maxX, maxZ. A range of one tile defers to the cached per tile answer; a wider range is only
     * hidden when no tile in it is already known to be visible and the entity's own bounding
     * cylinder rasterises as hidden.
     */
    @OriginalMember(owner = "client!aia", name = "a", descriptor = "(IIBLclient!ke;III)Z")
    public static boolean isTileRangeOccluded(@OriginalArg(0) int minZ, @OriginalArg(1) int maxX, @OriginalArg(3) BoundingCylinder cylinder, @OriginalArg(4) int maxZ, @OriginalArg(5) int minX, @OriginalArg(6) int level) {
        if (!Static18.occlude || !Static29.aBoolean60) {
            return false;
        } else if (Static432.occludedPixelCount < 100) {
            return false;
        } else if (minX != maxX || maxZ != minZ) {
            for (@Pc(72) int x = minX; x <= maxX; x++) {
                for (@Pc(75) int z = minZ; z <= maxZ; z++) {
                    if (Static446.tileOcclusionCache[level][x][z] == -Static675.occlusionFrame) {
                        return false;
                    }
                }
            }
            if (Static342.method4463(cylinder)) {
                Static356.anInt5773++;
                return true;
            } else {
                return false;
            }
        } else if (!Static588.isTileOccluded(minZ, level, minX)) {
            return false;
        } else if (Static342.method4463(cylinder)) {
            Static356.anInt5773++;
            return true;
        } else {
            return false;
        }
    }

}

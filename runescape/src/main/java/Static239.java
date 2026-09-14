import com.jagex.core.constants.LocShapes;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static239 {

    /**
     * Maps a roof edge loc, packed as {@code shape | rotation << 6}, onto the
     * {@link Wall#sideMask} of the tile side or tile corner the edge covers. A straight roof edge
     * covers one side and yields a mask from {@link StaticWall#SIDE_MASKS}; a corner roof edge
     * covers one corner and yields a mask from {@link StaticWall#CORNER_MASKS}. Any other loc
     * yields 0, which matches no wall.
     *
     * @return the side mask of the wall the roof edge stands on, or 0 if the loc is not a roof edge.
     */
    @OriginalMember(owner = "client!hha", name = "a", descriptor = "(II)I")
    public static int roofEdgeSideMask(@OriginalArg(1) int locCode) {
        @Pc(7) int shape = locCode & 0x3F;
        @Pc(13) int rotation = locCode >> 6 & 0x3;
        if (shape == LocShapes.ROOFEDGE_STRAIGHT) {
            if (rotation == 0) {
                return 1;
            }
            if (rotation == 1) {
                return 2;
            }
            if (rotation == 2) {
                return 4;
            }
            if (rotation == 3) {
                return 8;
            }
        } else if (shape == LocShapes.ROOFEDGE_DIAGONALCORNER || shape == LocShapes.ROOFEDGE_SQUARECORNER) {
            if (rotation == 0) {
                return 16;
            }
            if (rotation == 1) {
                return 32;
            }
            if (rotation == 2) {
                return 64;
            }
            if (rotation == 3) {
                return 128;
            }
        }
        return 0;
    }
}

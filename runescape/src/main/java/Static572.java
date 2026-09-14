import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static572 {

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(IIII)I")
    public static int lerpRgb(@OriginalArg(0) int source, @OriginalArg(2) int destination, @OriginalArg(3) int alpha) {
        @Pc(15) int local15 = 255 - alpha;
        @Pc(33) int local33 = ((source & 0xFF00) * alpha & 0xFF0000 | (source & 0xFF00FF) * alpha & 0xFF00FF00) >>> 8;
        return local33 + ((local15 * (destination & 0xFF00) & 0xFF0000 | (destination & 0xFF00FF) * local15 & 0xFF00FF00) >>> 8);
    }

}

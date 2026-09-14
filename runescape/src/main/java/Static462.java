import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static462 {

    @OriginalMember(owner = "client!ok", name = "a", descriptor = "(IIB)I")
    public static int blendArgb(@OriginalArg(0) int source, @OriginalArg(1) int destination) {
        @Pc(7) int local7 = source >>> 24;
        @Pc(25) int local25 = ((source & 0xFF00FF) * local7 & 0xFF00FF00 | (source & 0xFF00) * local7 & 0xFF0000) >>> 8;
        @Pc(40) int local40 = 255 - local7;
        return local25 + ((local40 * (destination & 0xFF00) & 0xFF0000 | local40 * (destination & 0xFF00FF) & 0xFF00FF00) >>> 8);
    }
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static705 {

    @OriginalMember(owner = "client!wf", name = "a", descriptor = "(IIII)I")
    public static int getMapLevel(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        if ((Static280.tileFlags[arg2][arg1][arg0] & 0x8) == 0) {
            return arg2 <= 0 || (Static280.tileFlags[1][arg1][arg0] & 0x2) == 0 ? arg2 : arg2 - 1;
        } else {
            return 0;
        }
    }
}

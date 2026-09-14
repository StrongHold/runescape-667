import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static526 {

    @OriginalMember(owner = "client!qk", name = "a", descriptor = "(IIB)Z")
    public static boolean normalsMutable(@OriginalArg(0) int flags, @OriginalArg(1) int functionMask) {
        return (functionMask & 0x220) == 544 | (functionMask & 0x18) != 0;
    }
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static335 {

    @OriginalMember(owner = "client!kk", name = "a", descriptor = "(IIZ)Z")
    public static boolean faceIndicesMutable(@OriginalArg(0) int flags, @OriginalArg(1) int functionMask) {
        return (functionMask & 0x10) != 0;
    }
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static69 {

    @OriginalMember(owner = "client!cda", name = "a", descriptor = "(III)Z")
    public static boolean indexBufferMutable(@OriginalArg(1) int functionMask, @OriginalArg(2) int flags) {
        return Static335.faceIndicesMutable(flags, functionMask) & Static652.method8532(functionMask, flags);
    }

    @OriginalMember(owner = "client!cda", name = "b", descriptor = "(III)Z")
    public static boolean usesNormalBuffer(@OriginalArg(1) int functionMask, @OriginalArg(2) int flags) {
        return (functionMask & 0x800) != 0 && (flags & 0x37) != 0;
    }
}

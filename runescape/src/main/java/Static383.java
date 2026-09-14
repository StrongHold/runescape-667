import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static383 {

    @OriginalMember(owner = "client!ma", name = "a", descriptor = "(BII)Z")
    public static boolean method5381(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x800) != 0 | Static555.method7297(arg1, arg0) || Static519.colourBufferMutable(0xFFFFFF9F ^ 0x5, arg1, arg0);
    }
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static135 {

    @OriginalMember(owner = "client!ee", name = "P", descriptor = "[F")
    public static final float[] aFloatArray56 = new float[16];

    @OriginalMember(owner = "client!ee", name = "a", descriptor = "(IZI)Z")
    public static boolean retainNormals(@OriginalArg(0) int functionMask, @OriginalArg(2) int flags) {
        @Pc(28) boolean local28 = (flags & 0x37) == 0 ? Static519.colourBufferMutable(-125, functionMask, flags) : Static576.normalBufferMutable(flags, functionMask);
        return local28 | Static526.normalsMutable(flags, functionMask) | (functionMask & 0x10000) != 0;
    }

}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!mf")
public final class JavaBillboardFace {

    @OriginalMember(owner = "client!mf", name = "l", descriptor = "I")
    public final int face;

    @OriginalMember(owner = "client!mf", name = "j", descriptor = "S")
    public final short texture;

    @OriginalMember(owner = "client!mf", name = "g", descriptor = "B")
    public final byte blendMode;

    @OriginalMember(owner = "client!mf", name = "f", descriptor = "B")
    public final byte colourOp;

    @OriginalMember(owner = "client!mf", name = "i", descriptor = "I")
    public final int distance;

    @OriginalMember(owner = "client!mf", name = "m", descriptor = "Z")
    public final boolean hideFace;

    @OriginalMember(owner = "client!mf", name = "e", descriptor = "S")
    public final short width;

    @OriginalMember(owner = "client!mf", name = "h", descriptor = "S")
    public final short height;

    @OriginalMember(owner = "client!mf", name = "<init>", descriptor = "(IIIIIIIIIZI)V")
    public JavaBillboardFace(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) boolean arg9, @OriginalArg(10) int arg10) {
        this.face = arg0;
        this.texture = (short) arg6;
        this.blendMode = (byte) arg8;
        this.colourOp = (byte) arg7;
        this.distance = arg10;
        this.hideFace = arg9;
        this.width = (short) arg4;
        this.height = (short) arg5;
    }
}

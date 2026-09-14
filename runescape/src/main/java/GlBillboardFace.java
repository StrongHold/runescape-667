import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!efa")
public final class GlBillboardFace {

    @OriginalMember(owner = "client!efa", name = "b", descriptor = "I")
    public final int distance;

    @OriginalMember(owner = "client!efa", name = "a", descriptor = "S")
    public final short texture;

    @OriginalMember(owner = "client!efa", name = "k", descriptor = "I")
    public final int vertexC;

    @OriginalMember(owner = "client!efa", name = "l", descriptor = "I")
    public final int face;

    @OriginalMember(owner = "client!efa", name = "d", descriptor = "S")
    public final short height;

    @OriginalMember(owner = "client!efa", name = "f", descriptor = "Z")
    public final boolean hideWithBloom;

    @OriginalMember(owner = "client!efa", name = "h", descriptor = "B")
    public final byte blendMode;

    @OriginalMember(owner = "client!efa", name = "i", descriptor = "S")
    public final short width;

    @OriginalMember(owner = "client!efa", name = "g", descriptor = "I")
    public final int vertexB;

    @OriginalMember(owner = "client!efa", name = "j", descriptor = "I")
    public final int vertexA;

    @OriginalMember(owner = "client!efa", name = "<init>", descriptor = "(IIIIIIIIIZZI)V")
    public GlBillboardFace(@OriginalArg(0) int face, @OriginalArg(1) int vertexA, @OriginalArg(2) int vertexB, @OriginalArg(3) int vertexC, @OriginalArg(4) int width, @OriginalArg(5) int height, @OriginalArg(6) int texture, @OriginalArg(7) int arg7, @OriginalArg(8) int blendMode, @OriginalArg(9) boolean hideFace, @OriginalArg(10) boolean hideWithBloom, @OriginalArg(11) int distance) {
        this.distance = distance;
        this.texture = (short) texture;
        this.vertexC = vertexC;
        this.face = face;
        this.height = (short) height;
        this.hideWithBloom = hideWithBloom;
        this.blendMode = (byte) blendMode;
        this.width = (short) width;
        this.vertexB = vertexB;
        this.vertexA = vertexA;
    }
}

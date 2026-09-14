import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!pr")
public final class NativeBillboardFace {

    @OriginalMember(owner = "client!pr", name = "j", descriptor = "S")
    public final short texture;

    @OriginalMember(owner = "client!pr", name = "c", descriptor = "I")
    public final int vertexC;

    @OriginalMember(owner = "client!pr", name = "h", descriptor = "I")
    public final int face;

    @OriginalMember(owner = "client!pr", name = "e", descriptor = "S")
    public final short width;

    @OriginalMember(owner = "client!pr", name = "g", descriptor = "I")
    public final int vertexA;

    @OriginalMember(owner = "client!pr", name = "n", descriptor = "B")
    public final byte blendMode;

    @OriginalMember(owner = "client!pr", name = "b", descriptor = "I")
    public final int distance;

    @OriginalMember(owner = "client!pr", name = "m", descriptor = "S")
    public final short height;

    @OriginalMember(owner = "client!pr", name = "d", descriptor = "I")
    public final int vertexB;

    @OriginalMember(owner = "client!pr", name = "i", descriptor = "Z")
    public final boolean hideWithBloom;

    @OriginalMember(owner = "client!pr", name = "<init>", descriptor = "(IIIIIIIIIZZI)V")
    public NativeBillboardFace(@OriginalArg(0) int face, @OriginalArg(1) int vertexA, @OriginalArg(2) int vertexB, @OriginalArg(3) int vertexC, @OriginalArg(4) int width, @OriginalArg(5) int height, @OriginalArg(6) int texture, @OriginalArg(7) int arg7, @OriginalArg(8) int blendMode, @OriginalArg(9) boolean hideFace, @OriginalArg(10) boolean hideWithBloom, @OriginalArg(11) int distance) {
        this.texture = (short) texture;
        this.vertexC = vertexC;
        this.face = face;
        this.width = (short) width;
        this.vertexA = vertexA;
        this.blendMode = (byte) blendMode;
        this.distance = distance;
        this.height = (short) height;
        this.vertexB = vertexB;
        this.hideWithBloom = hideWithBloom;
    }
}

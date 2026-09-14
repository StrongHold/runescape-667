import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!vg")
public final class JavaComplexBlendedTile {

    @OriginalMember(owner = "client!vg", name = "g", descriptor = "[I")
    public int[] faceBlendedColours;

    @OriginalMember(owner = "client!vg", name = "i", descriptor = "[I")
    public int[] vertexColours;

    @OriginalMember(owner = "client!vg", name = "e", descriptor = "S")
    public short vertexCount;

    @OriginalMember(owner = "client!vg", name = "h", descriptor = "[S")
    public short[] waterDepths;

    @OriginalMember(owner = "client!vg", name = "b", descriptor = "[S")
    public short[] faceTextures;

    @OriginalMember(owner = "client!vg", name = "a", descriptor = "[S")
    public short[] faceSizes;

    @OriginalMember(owner = "client!vg", name = "c", descriptor = "[S")
    public short[] verticesZ;

    @OriginalMember(owner = "client!vg", name = "k", descriptor = "S")
    public short faceCount;

    @OriginalMember(owner = "client!vg", name = "f", descriptor = "[S")
    public short[] verticesX;

    @OriginalMember(owner = "client!vg", name = "j", descriptor = "[S")
    public short[] verticesY;

    @OriginalMember(owner = "client!vg", name = "d", descriptor = "B")
    public byte flags;
}

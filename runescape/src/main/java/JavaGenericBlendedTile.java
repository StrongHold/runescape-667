import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!rh")
public final class JavaGenericBlendedTile {

    @OriginalMember(owner = "client!rh", name = "g", descriptor = "[S")
    public short[] verticesZ;

    @OriginalMember(owner = "client!rh", name = "i", descriptor = "S")
    public short faceCount;

    @OriginalMember(owner = "client!rh", name = "b", descriptor = "[S")
    public short[] waterDepths;

    @OriginalMember(owner = "client!rh", name = "a", descriptor = "[B")
    public byte[] vertexLight;

    @OriginalMember(owner = "client!rh", name = "d", descriptor = "[S")
    public short[] vertexTextures;

    @OriginalMember(owner = "client!rh", name = "f", descriptor = "[I")
    public int[] vertexColours;

    @OriginalMember(owner = "client!rh", name = "h", descriptor = "[S")
    public short[] verticesX;

    @OriginalMember(owner = "client!rh", name = "k", descriptor = "[S")
    public short[] verticesY;

    @OriginalMember(owner = "client!rh", name = "c", descriptor = "[I")
    public int[] faceBlendedColours;

    @OriginalMember(owner = "client!rh", name = "e", descriptor = "S")
    public short vertexCount;

    @OriginalMember(owner = "client!rh", name = "j", descriptor = "[S")
    public short[] vertexSizes;
}

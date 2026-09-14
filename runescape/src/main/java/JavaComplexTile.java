import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!em")
public final class JavaComplexTile {

    @OriginalMember(owner = "client!em", name = "k", descriptor = "[S")
    public short[] faceTextures;

    @OriginalMember(owner = "client!em", name = "h", descriptor = "[S")
    public short[] faceSizes;

    @OriginalMember(owner = "client!em", name = "g", descriptor = "S")
    public short faceCount;

    @OriginalMember(owner = "client!em", name = "f", descriptor = "[S")
    public short[] verticesX;

    @OriginalMember(owner = "client!em", name = "c", descriptor = "[S")
    public short[] verticesY;

    @OriginalMember(owner = "client!em", name = "i", descriptor = "[S")
    public short[] faceA;

    @OriginalMember(owner = "client!em", name = "b", descriptor = "[I")
    public int[] faceColours;

    @OriginalMember(owner = "client!em", name = "a", descriptor = "[S")
    public short[] verticesLight;

    @OriginalMember(owner = "client!em", name = "m", descriptor = "[I")
    public int[] faceBlendedColours;

    @OriginalMember(owner = "client!em", name = "l", descriptor = "[S")
    public short[] faceC;

    @OriginalMember(owner = "client!em", name = "e", descriptor = "[S")
    public short[] faceB;

    @OriginalMember(owner = "client!em", name = "j", descriptor = "S")
    public short vertexCount;

    @OriginalMember(owner = "client!em", name = "n", descriptor = "[S")
    public short[] verticesZ;

    @OriginalMember(owner = "client!em", name = "d", descriptor = "B")
    public byte flags;
}

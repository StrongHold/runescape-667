import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!fg")
public final class JavaSimpleTile {

    @OriginalMember(owner = "client!fg", name = "c", descriptor = "S")
    public short texture;

    @OriginalMember(owner = "client!fg", name = "b", descriptor = "S")
    public short hslSe;

    @OriginalMember(owner = "client!fg", name = "g", descriptor = "S")
    public short hslNw;

    @OriginalMember(owner = "client!fg", name = "a", descriptor = "B")
    public byte flags;

    @OriginalMember(owner = "client!fg", name = "d", descriptor = "S")
    public short hslNe;

    @OriginalMember(owner = "client!fg", name = "e", descriptor = "S")
    public short hslSw;

    @OriginalMember(owner = "client!fg", name = "f", descriptor = "I")
    public int blendedColour = -1;
}

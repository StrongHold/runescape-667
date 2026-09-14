import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!qh")
public final class JavaSimpleBlendedTile {

    @OriginalMember(owner = "client!qh", name = "k", descriptor = "S")
    public short waterDepthSe;

    @OriginalMember(owner = "client!qh", name = "e", descriptor = "S")
    public short waterDepthNw;

    @OriginalMember(owner = "client!qh", name = "b", descriptor = "S")
    public short texture;

    @OriginalMember(owner = "client!qh", name = "f", descriptor = "I")
    public int colourNe;

    @OriginalMember(owner = "client!qh", name = "c", descriptor = "I")
    public int colourSw;

    @OriginalMember(owner = "client!qh", name = "d", descriptor = "B")
    public byte flags;

    @OriginalMember(owner = "client!qh", name = "j", descriptor = "I")
    public int colourNw;

    @OriginalMember(owner = "client!qh", name = "a", descriptor = "S")
    public short waterDepthNe;

    @OriginalMember(owner = "client!qh", name = "h", descriptor = "I")
    public int colourSe;

    @OriginalMember(owner = "client!qh", name = "i", descriptor = "S")
    public short waterDepthSw;

    @OriginalMember(owner = "client!qh", name = "g", descriptor = "I")
    public int blendedColour = 0;
}

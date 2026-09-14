import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!ala")
public final class NativeBillboardAttributes {

    @OriginalMember(owner = "client!ala", name = "i", descriptor = "I")
    public int offsetX;

    @OriginalMember(owner = "client!ala", name = "h", descriptor = "I")
    public int offsetY;

    @OriginalMember(owner = "client!ala", name = "e", descriptor = "I")
    public int angle;

    @OriginalMember(owner = "client!ala", name = "c", descriptor = "I")
    public int scaleY = 128;

    @OriginalMember(owner = "client!ala", name = "g", descriptor = "I")
    public int scaleX = 128;

    @OriginalMember(owner = "client!ala", name = "f", descriptor = "I")
    public int colour;

    @OriginalMember(owner = "client!ala", name = "<init>", descriptor = "(I)V")
    public NativeBillboardAttributes(@OriginalArg(0) int colour) {
        this.colour = colour;
    }

    @OriginalMember(owner = "client!ala", name = "<init>", descriptor = "(IIIIII)V")
    public NativeBillboardAttributes(@OriginalArg(0) int colour, @OriginalArg(1) int scaleX, @OriginalArg(2) int scaleY, @OriginalArg(3) int offsetX, @OriginalArg(4) int offsetY, @OriginalArg(5) int angle) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scaleY = scaleY;
        this.colour = colour;
        this.angle = angle;
        this.scaleX = scaleX;
    }

    @OriginalMember(owner = "client!ala", name = "a", descriptor = "(B)Lclient!ala;")
    public NativeBillboardAttributes copy() {
        return new NativeBillboardAttributes(this.colour, this.scaleX, this.scaleY, this.offsetX, this.offsetY, this.angle);
    }

    @OriginalMember(owner = "client!ala", name = "a", descriptor = "(ZLclient!ala;)V")
    public void set(@OriginalArg(1) NativeBillboardAttributes other) {
        this.offsetY = other.offsetY;
        this.colour = other.colour;
        this.angle = other.angle;
        this.scaleX = other.scaleX;
        this.scaleY = other.scaleY;
        this.offsetX = other.offsetX;
    }
}

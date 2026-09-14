import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!lfa")
public final class GlBillboardAttributes {

    @OriginalMember(owner = "client!lfa", name = "o", descriptor = "I")
    public int offsetY;

    @OriginalMember(owner = "client!lfa", name = "n", descriptor = "I")
    public int angle;

    @OriginalMember(owner = "client!lfa", name = "f", descriptor = "I")
    public int offsetX;

    @OriginalMember(owner = "client!lfa", name = "j", descriptor = "I")
    public int scaleY = 128;

    @OriginalMember(owner = "client!lfa", name = "e", descriptor = "I")
    public int scaleX = 128;

    @OriginalMember(owner = "client!lfa", name = "h", descriptor = "I")
    public int colour;

    @OriginalMember(owner = "client!lfa", name = "<init>", descriptor = "(I)V")
    public GlBillboardAttributes(@OriginalArg(0) int colour) {
        this.colour = colour;
    }

    @OriginalMember(owner = "client!lfa", name = "<init>", descriptor = "(IIIIII)V")
    public GlBillboardAttributes(@OriginalArg(0) int colour, @OriginalArg(1) int scaleX, @OriginalArg(2) int scaleY, @OriginalArg(3) int offsetX, @OriginalArg(4) int offsetY, @OriginalArg(5) int angle) {
        this.offsetY = offsetY;
        this.scaleX = scaleX;
        this.offsetX = offsetX;
        this.colour = colour;
        this.scaleY = scaleY;
        this.angle = angle;
    }

    @OriginalMember(owner = "client!lfa", name = "a", descriptor = "(Z)Lclient!lfa;")
    public GlBillboardAttributes copy() {
        return new GlBillboardAttributes(this.colour, this.scaleX, this.scaleY, this.offsetX, this.offsetY, this.angle);
    }

    @OriginalMember(owner = "client!lfa", name = "a", descriptor = "(BLclient!lfa;)V")
    public void set(@OriginalArg(1) GlBillboardAttributes other) {
        this.angle = other.angle;
        this.scaleY = other.scaleY;
        this.colour = other.colour;
        this.offsetX = other.offsetX;
        this.offsetY = other.offsetY;
        this.scaleX = other.scaleX;
    }
}

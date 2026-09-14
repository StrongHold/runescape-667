import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

/**
 * One primitive in a {@link TextureOpShape} drawing list.
 * <p>
 * Every coordinate is a signed 12 bit fraction of the texture size, so a subclass scales it by the
 * texture width or height and shifts right by 12 before handing pixels to the rasteriser.
 * <p>
 * A colour of -1 means absent, which is what selects between the three draw methods. Note that
 * {@link #fill} takes the width first while {@link #outline} and {@link #fillAndOutline} take the
 * height first.
 */
@OriginalClass("client!ifa")
public abstract class TextureShape {

    @OriginalMember(owner = "client!ifa", name = "e", descriptor = "I")
    protected final int lineWidth;

    @OriginalMember(owner = "client!ifa", name = "d", descriptor = "I")
    public final int lineColour;

    @OriginalMember(owner = "client!ifa", name = "f", descriptor = "I")
    public final int fillColour;

    @OriginalMember(owner = "client!ifa", name = "<init>", descriptor = "(III)V")
    protected TextureShape(@OriginalArg(0) int fillColour, @OriginalArg(1) int lineColour, @OriginalArg(2) int lineWidth) {
        this.lineWidth = lineWidth;
        this.lineColour = lineColour;
        this.fillColour = fillColour;
    }

    @OriginalMember(owner = "client!ifa", name = "b", descriptor = "(III)V")
    public abstract void fill(@OriginalArg(0) int width, @OriginalArg(1) int height);

    @OriginalMember(owner = "client!ifa", name = "c", descriptor = "(III)V")
    public abstract void outline(@OriginalArg(0) int height, @OriginalArg(2) int width);

    @OriginalMember(owner = "client!ifa", name = "a", descriptor = "(III)V")
    public abstract void fillAndOutline(@OriginalArg(0) int height, @OriginalArg(2) int width);
}

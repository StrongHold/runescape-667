import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

/**
 * Outputs a linear ramp that runs from zero at the left edge of the texture to full intensity at
 * the right edge, the same for every row.
 */
@OriginalClass("client!vea")
public final class TextureOpHorizontalGradient extends TextureOp {

    @OriginalMember(owner = "client!vea", name = "<init>", descriptor = "()V")
    public TextureOpHorizontalGradient() {
        super(0, true);
    }

    @OriginalMember(owner = "client!vea", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 <= 107) {
            this.monochromeOutput(49, -21);
        }
        return EnvironmentLight.anIntArray92;
    }
}

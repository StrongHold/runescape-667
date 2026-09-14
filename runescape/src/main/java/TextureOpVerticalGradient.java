import com.jagex.core.util.Arrays;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Generates a vertical gradient, holding every row at the intensity of its own y coordinate.
 */
@OriginalClass("client!qu")
public final class TextureOpVerticalGradient extends TextureOp {

    @OriginalMember(owner = "client!qu", name = "<init>", descriptor = "()V")
    public TextureOpVerticalGradient() {
        super(0, true);
    }

    @OriginalMember(owner = "client!qu", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(9) int[] output = super.monochromeCache.get(y);
        if (arg0 <= 107) {
            Static537.anIntArray633 = null;
        }
        if (super.monochromeCache.dirty) {
            Arrays.set(output, 0, EnvironmentLight.anInt9289, MonochromeImageCache.anIntArray341[y]);
        }
        return output;
    }
}

import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Flattens a colour source to a single intensity, by averaging its red, green and blue channels.
 */
@OriginalClass("client!ol")
public final class TextureOpGreyscale extends TextureOp {

    @OriginalMember(owner = "client!ol", name = "<init>", descriptor = "()V")
    public TextureOpGreyscale() {
        super(1, true);
    }

    @OriginalMember(owner = "client!ol", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 < 107) {
            return null;
        }
        @Pc(17) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(27) int[][] source = this.method9413(0, y);
            @Pc(31) int[] sourceRed = source[0];
            @Pc(35) int[] sourceGreen = source[1];
            @Pc(39) int[] sourceBlue = source[2];
            for (@Pc(41) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                output[x] = (sourceBlue[x] + sourceRed[x] + sourceGreen[x]) / 3;
            }
        }
        return output;
    }
}

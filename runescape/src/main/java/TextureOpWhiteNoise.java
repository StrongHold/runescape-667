import com.jagex.sign.SignLink;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Outputs white noise, hashing the coordinates of each texel into an independent pseudo random
 * intensity.
 */
@OriginalClass("client!vf")
public final class TextureOpWhiteNoise extends TextureOp {

    @OriginalMember(owner = "client!vf", name = "<init>", descriptor = "()V")
    public TextureOpWhiteNoise() {
        super(0, true);
    }

    @OriginalMember(owner = "client!vf", name = "c", descriptor = "(III)I")
    public int noise(@OriginalArg(1) int y, @OriginalArg(2) int x) {
        @Pc(9) int seed = x + y * 57;
        @Pc(15) int scrambled = seed << 1 ^ seed;
        return 4096 - ((scrambled * 15731 * scrambled + 789221) * scrambled + 1376312589 & Integer.MAX_VALUE) / 262144;
    }

    @OriginalMember(owner = "client!vf", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(28) int rowCoord = MonochromeImageCache.anIntArray341[y];
            for (@Pc(30) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                output[x] = this.noise(rowCoord, EnvironmentLight.anIntArray92[x]) % 4096;
            }
        }
        return output;
    }
}

import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Mirrors a source layer, about the vertical axis, the horizontal axis, or both.
 */
@OriginalClass("client!wh")
public final class TextureOpFlip extends TextureOp {

    @OriginalMember(owner = "client!wh", name = "P", descriptor = "Z")
    public boolean vertical = true;

    @OriginalMember(owner = "client!wh", name = "L", descriptor = "Z")
    public boolean horizontal = true;

    @OriginalMember(owner = "client!wh", name = "<init>", descriptor = "()V")
    public TextureOpFlip() {
        super(1, false);
    }

    @OriginalMember(owner = "client!wh", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.horizontal = arg1.g1() == 1;
        } else if (arg2 == 1) {
            this.vertical = arg1.g1() == 1;
        } else if (arg2 == 2) {
            super.monochrome = arg1.g1() == 1;
        }
        if (arg0) {
            this.method9414(121);
        }
    }

    @OriginalMember(owner = "client!wh", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(18) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(39) int[][] source = this.method9413(0, this.vertical ? EnvironmentLight.anInt7343 - y : y);
            @Pc(43) int[] sourceRed = source[0];
            @Pc(47) int[] sourceGreen = source[1];
            @Pc(51) int[] sourceBlue = source[2];
            @Pc(55) int[] outputRed = output[0];
            @Pc(59) int[] outputGreen = output[1];
            @Pc(63) int[] outputBlue = output[2];
            @Pc(68) int x;
            if (this.horizontal) {
                for (x = 0; x < EnvironmentLight.anInt9289; x++) {
                    outputRed[x] = sourceRed[EnvironmentLight.anInt8580 - x];
                    outputGreen[x] = sourceGreen[EnvironmentLight.anInt8580 - x];
                    outputBlue[x] = sourceBlue[EnvironmentLight.anInt8580 - x];
                }
            } else {
                for (x = 0; x < EnvironmentLight.anInt9289; x++) {
                    outputRed[x] = sourceRed[x];
                    outputGreen[x] = sourceGreen[x];
                    outputBlue[x] = sourceBlue[x];
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!wh", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (arg0 < 107) {
            return null;
        }
        if (super.monochromeCache.dirty) {
            @Pc(37) int[] source = this.method9422(this.vertical ? EnvironmentLight.anInt7343 - y : y, 0);
            if (this.horizontal) {
                for (@Pc(52) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                    output[x] = source[EnvironmentLight.anInt8580 - x];
                }
            } else {
                Arrays.copy(source, 0, output, 0, EnvironmentLight.anInt9289);
            }
        }
        return output;
    }
}

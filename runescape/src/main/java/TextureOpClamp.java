import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Clamps a source layer, holding every intensity below a minimum at that minimum and every
 * intensity above a maximum at that maximum.
 */
@OriginalClass("client!vi")
public final class TextureOpClamp extends TextureOp {

    @OriginalMember(owner = "client!vi", name = "G", descriptor = "I")
    public int min = 0;

    @OriginalMember(owner = "client!vi", name = "H", descriptor = "I")
    public int max = 4096;

    @OriginalMember(owner = "client!vi", name = "<init>", descriptor = "()V")
    public TextureOpClamp() {
        super(1, false);
    }

    @OriginalMember(owner = "client!vi", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(21) int[] source = this.method9422(y, 0);
            for (@Pc(23) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(29) int value = source[x];
                if (value < this.min) {
                    output[x] = this.min;
                } else if (this.max < value) {
                    output[x] = this.max;
                } else {
                    output[x] = value;
                }
            }
        }
        if (arg0 < 107) {
            Static677.anTextureSource_11 = null;
        }
        return output;
    }

    @OriginalMember(owner = "client!vi", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.min = arg1.g2();
        } else if (arg2 == 1) {
            this.max = arg1.g2();
        } else if (arg2 == 2) {
            super.monochrome = arg1.g1() == 1;
        }
        if (arg0) {
            this.method9414(0);
        }
    }

    @OriginalMember(owner = "client!vi", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(21) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(31) int[][] source = this.method9413(0, y);
            @Pc(35) int[] sourceRed = source[0];
            @Pc(39) int[] sourceGreen = source[1];
            @Pc(43) int[] sourceBlue = source[2];
            @Pc(47) int[] outputRed = output[0];
            @Pc(51) int[] outputGreen = output[1];
            @Pc(55) int[] outputBlue = output[2];
            for (@Pc(57) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(63) int red = sourceRed[x];
                @Pc(67) int green = sourceGreen[x];
                @Pc(71) int blue = sourceBlue[x];
                if (this.min > red) {
                    outputRed[x] = this.min;
                } else if (this.max >= red) {
                    outputRed[x] = red;
                } else {
                    outputRed[x] = this.max;
                }
                if (this.min > green) {
                    outputGreen[x] = this.min;
                } else if (green > this.max) {
                    outputGreen[x] = this.max;
                } else {
                    outputGreen[x] = green;
                }
                if (this.min > blue) {
                    outputBlue[x] = this.min;
                } else if (this.max >= blue) {
                    outputBlue[x] = blue;
                } else {
                    outputBlue[x] = this.max;
                }
            }
        }
        return output;
    }
}

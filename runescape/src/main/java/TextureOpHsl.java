import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Shifts the hue, saturation and lightness of a source layer.
 */
@OriginalClass("client!fp")
public final class TextureOpHsl extends TextureOp {

    @OriginalMember(owner = "client!fp", name = "U", descriptor = "I")
    public int saturation;

    @OriginalMember(owner = "client!fp", name = "L", descriptor = "I")
    public int lightness;

    @OriginalMember(owner = "client!fp", name = "P", descriptor = "I")
    public int green;

    @OriginalMember(owner = "client!fp", name = "I", descriptor = "I")
    public int red;

    @OriginalMember(owner = "client!fp", name = "Q", descriptor = "I")
    public int hue;

    @OriginalMember(owner = "client!fp", name = "W", descriptor = "I")
    public int blue;

    @OriginalMember(owner = "client!fp", name = "H", descriptor = "I")
    public int saturationOffset = 0;

    @OriginalMember(owner = "client!fp", name = "J", descriptor = "I")
    public int hueOffset = 0;

    @OriginalMember(owner = "client!fp", name = "V", descriptor = "I")
    public int lightnessOffset = 0;

    @OriginalMember(owner = "client!fp", name = "<init>", descriptor = "()V")
    public TextureOpHsl() {
        super(1, false);
    }

    @OriginalMember(owner = "client!fp", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.hueOffset = arg1.g2s();
        } else if (arg2 == 1) {
            this.saturationOffset = (arg1.g1b() << 12) / 100;
        } else if (arg2 == 2) {
            this.lightnessOffset = (arg1.g1b() << 12) / 100;
        }
        if (arg0) {
            this.red = -61;
        }
    }

    @OriginalMember(owner = "client!fp", name = "b", descriptor = "(IIII)V")
    public void hslToRgb(@OriginalArg(0) int hue, @OriginalArg(2) int saturation, @OriginalArg(3) int lightness) {
        @Pc(31) int max = lightness > 2048 ? lightness + saturation - (lightness * saturation >> 12) : (saturation + 4096) * lightness >> 12;
        if (max <= 0) {
            this.red = this.green = this.blue = lightness;
            return;
        }
        @Pc(37) int scaledHue = hue * 6;
        @Pc(44) int min = lightness + lightness - max;
        @Pc(52) int delta = (max - min << 12) / max;
        @Pc(56) int sector = scaledHue >> 12;
        @Pc(63) int remainder = scaledHue - (sector << 12);
        @Pc(71) int span = max * delta >> 12;
        @Pc(77) int step = remainder * span >> 12;
        @Pc(82) int rising = min + step;
        @Pc(87) int falling = max - step;
        if (sector == 0) {
            this.red = max;
            this.green = rising;
            this.blue = min;
            return;
        }
        if (sector == 1) {
            this.red = falling;
            this.blue = min;
            this.green = max;
            return;
        }
        if (sector == 2) {
            this.blue = rising;
            this.green = max;
            this.red = min;
            return;
        }
        if (sector == 3) {
            this.blue = max;
            this.red = min;
            this.green = falling;
            return;
        }
        if (sector == 4) {
            this.red = rising;
            this.blue = max;
            this.green = min;
            return;
        }
        if (sector == 5) {
            this.red = max;
            this.blue = falling;
            this.green = min;
            return;
        }
    }

    @OriginalMember(owner = "client!fp", name = "a", descriptor = "(IBII)V")
    public void rgbToHsl(@OriginalArg(0) int red, @OriginalArg(2) int blue, @OriginalArg(3) int green) {
        @Pc(17) int maxRedGreen = green < red ? red : green;
        @Pc(25) int minRedGreen = red >= green ? green : red;
        @Pc(33) int max = blue > maxRedGreen ? blue : maxRedGreen;
        @Pc(45) int min = blue >= minRedGreen ? minRedGreen : blue;
        this.lightness = (max + min) / 2;
        @Pc(57) int range = max - min;
        if (range > 0) {
            @Pc(76) int redDistance = (max - red << 12) / range;
            @Pc(85) int greenDistance = (max - green << 12) / range;
            @Pc(94) int blueDistance = (max - blue << 12) / range;
            if (max == red) {
                this.hue = green == min ? blueDistance + 20480 : -greenDistance + 4096;
            } else if (max == green) {
                this.hue = min == blue ? redDistance + 4096 : -blueDistance + 12288;
            } else {
                this.hue = min == red ? greenDistance + 12288 : -redDistance + 20480;
            }
            this.hue /= 6;
        } else {
            this.hue = 0;
        }
        if (this.lightness > 0 && this.lightness < 4096) {
            this.saturation = (range << 12) / (this.lightness > 2048 ? 8192 - this.lightness * 2 : this.lightness * 2);
        } else {
            this.saturation = 0;
        }
    }

    @OriginalMember(owner = "client!fp", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(22) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(32) int[][] source = this.method9413(0, y);
            @Pc(36) int[] sourceRed = source[0];
            @Pc(40) int[] sourceGreen = source[1];
            @Pc(44) int[] sourceBlue = source[2];
            @Pc(48) int[] outputRed = output[0];
            @Pc(52) int[] outputGreen = output[1];
            @Pc(56) int[] outputBlue = output[2];
            for (@Pc(58) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                this.rgbToHsl(sourceRed[x], sourceBlue[x], sourceGreen[x]);
                this.lightness += this.lightnessOffset;
                this.saturation += this.saturationOffset;
                for (this.hue += this.hueOffset; this.hue < 0; this.hue += 4096) {
                    /* empty */
                }
                while (this.hue > 4096) {
                    this.hue -= 4096;
                }
                if (this.saturation < 0) {
                    this.saturation = 0;
                }
                if (this.lightness < 0) {
                    this.lightness = 0;
                }
                if (this.saturation > 4096) {
                    this.saturation = 4096;
                }
                if (this.lightness > 4096) {
                    this.lightness = 4096;
                }
                this.hslToRgb(this.hue, this.saturation, this.lightness);
                outputRed[x] = this.red;
                outputGreen[x] = this.green;
                outputBlue[x] = this.blue;
            }
        }
        return output;
    }
}

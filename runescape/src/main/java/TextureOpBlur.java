import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Box blurs a source layer, with separate horizontal and vertical radii, wrapping at the edges of
 * the texture.
 */
@OriginalClass("client!jk")
public final class TextureOpBlur extends TextureOp {

    @OriginalMember(owner = "client!jk", name = "M", descriptor = "I")
    public int verticalRadius = 1;

    @OriginalMember(owner = "client!jk", name = "I", descriptor = "I")
    public int horizontalRadius = 1;

    @OriginalMember(owner = "client!jk", name = "<init>", descriptor = "()V")
    public TextureOpBlur() {
        super(1, false);
    }

    @OriginalMember(owner = "client!jk", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(11) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(30) int verticalSpan = this.verticalRadius + this.verticalRadius + 1;
            @Pc(34) int verticalWeight = 65536 / verticalSpan;
            @Pc(42) int horizontalSpan = this.horizontalRadius + this.horizontalRadius + 1;
            @Pc(46) int horizontalWeight = 65536 / horizontalSpan;
            @Pc(49) int[][][] rows = new int[verticalSpan][][];
            @Pc(75) int local75;
            @Pc(77) int local77;
            for (@Pc(55) int row = y - this.verticalRadius; row <= y + this.verticalRadius; row++) {
                @Pc(67) int[][] source = this.method9413(0, EnvironmentLight.anInt7343 & row);
                @Pc(71) int[][] blurred = new int[3][EnvironmentLight.anInt9289];
                @Pc(73) int local73 = 0;
                local75 = 0;
                local77 = 0;
                @Pc(81) int[] sourceRed = source[0];
                @Pc(85) int[] sourceGreen = source[1];
                @Pc(89) int[] sourceBlue = source[2];
                for (@Pc(93) int offset = -this.horizontalRadius; offset <= this.horizontalRadius; offset++) {
                    @Pc(101) int sampleX = offset & EnvironmentLight.anInt8580;
                    local77 += sourceBlue[sampleX];
                    local73 += sourceRed[sampleX];
                    local75 += sourceGreen[sampleX];
                }
                @Pc(132) int[] blurredRed = blurred[0];
                @Pc(136) int[] blurredGreen = blurred[1];
                @Pc(140) int[] blurredBlue = blurred[2];
                @Pc(144) int x = 0;
                while (EnvironmentLight.anInt9289 > x) {
                    blurredRed[x] = local73 * horizontalWeight >> 16;
                    blurredGreen[x] = local75 * horizontalWeight >> 16;
                    blurredBlue[x] = horizontalWeight * local77 >> 16;
                    @Pc(180) int trailingX = EnvironmentLight.anInt8580 & x - this.horizontalRadius;
                    x++;
                    local75 -= sourceGreen[trailingX];
                    local77 -= sourceBlue[trailingX];
                    local73 -= sourceRed[trailingX];
                    @Pc(206) int leadingX = EnvironmentLight.anInt8580 & x + this.horizontalRadius;
                    local73 += sourceRed[leadingX];
                    local77 += sourceBlue[leadingX];
                    local75 += sourceGreen[leadingX];
                }
                rows[this.verticalRadius + row - y] = blurred;
            }
            @Pc(256) int[] outputRed = output[0];
            @Pc(260) int[] outputGreen = output[1];
            @Pc(266) int[] outputBlue = output[2];
            for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                local77 = 0;
                @Pc(274) int greenSum = 0;
                @Pc(276) int blueSum = 0;
                for (@Pc(278) int row = 0; row < verticalSpan; row++) {
                    @Pc(284) int[][] blurred = rows[row];
                    local77 += blurred[0][local75];
                    greenSum += blurred[1][local75];
                    blueSum += blurred[2][local75];
                }
                outputRed[local75] = verticalWeight * local77 >> 16;
                outputGreen[local75] = verticalWeight * greenSum >> 16;
                outputBlue[local75] = blueSum * verticalWeight >> 16;
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!jk", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(23) int verticalSpan = this.verticalRadius + this.verticalRadius + 1;
            @Pc(27) int verticalWeight = 65536 / verticalSpan;
            @Pc(35) int horizontalSpan = this.horizontalRadius + this.horizontalRadius + 1;
            @Pc(39) int horizontalWeight = 65536 / horizontalSpan;
            @Pc(42) int[][] rows = new int[verticalSpan][];
            @Pc(63) int local63;
            for (@Pc(48) int row = y - this.verticalRadius; row <= y + this.verticalRadius; row++) {
                @Pc(58) int[] source = this.method9422(row & EnvironmentLight.anInt7343, 0);
                @Pc(61) int[] blurred = new int[EnvironmentLight.anInt9289];
                local63 = 0;
                for (@Pc(67) int offset = -this.horizontalRadius; offset <= this.horizontalRadius; offset++) {
                    local63 += source[offset & EnvironmentLight.anInt8580];
                }
                @Pc(84) int x = 0;
                while (x < EnvironmentLight.anInt9289) {
                    blurred[x] = horizontalWeight * local63 >> 16;
                    local63 -= source[EnvironmentLight.anInt8580 & x - this.horizontalRadius];
                    x++;
                    local63 += source[x + this.horizontalRadius & EnvironmentLight.anInt8580];
                }
                rows[row + this.verticalRadius - y] = blurred;
            }
            for (@Pc(146) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(150) int sum = 0;
                for (local63 = 0; local63 < verticalSpan; local63++) {
                    sum += rows[local63][x];
                }
                output[x] = sum * verticalWeight >> 16;
            }
        }
        if (arg0 < 107) {
            this.monochromeOutput(-16, 56);
        }
        return output;
    }

    @OriginalMember(owner = "client!jk", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            return;
        }
        if (arg2 == 0) {
            this.horizontalRadius = arg1.g1();
        } else if (arg2 == 1) {
            this.verticalRadius = arg1.g1();
        } else if (arg2 == 2) {
            super.monochrome = arg1.g1() == 1;
        }
    }
}

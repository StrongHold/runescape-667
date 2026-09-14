import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Generates two crossing families of diagonal bands, each band pinched into a repeating chain of
 * lens shapes by a cosine that varies its width along its length.
 */
@OriginalClass("client!nla")
public final class Node_Sub1_Sub20 extends TextureOp {

    @OriginalMember(owner = "client!nla", name = "K", descriptor = "I")
    public int thickness = 4096;

    @OriginalMember(owner = "client!nla", name = "R", descriptor = "I")
    public int frequency = 12288;

    @OriginalMember(owner = "client!nla", name = "I", descriptor = "I")
    public int widthScale = 8192;

    @OriginalMember(owner = "client!nla", name = "O", descriptor = "I")
    public int secondOffsetX = 0;

    @OriginalMember(owner = "client!nla", name = "S", descriptor = "I")
    public int secondOffsetY = 2048;

    @OriginalMember(owner = "client!nla", name = "N", descriptor = "I")
    public int firstOffsetX = 2048;

    @OriginalMember(owner = "client!nla", name = "W", descriptor = "I")
    public int firstOffsetY = 0;

    @OriginalMember(owner = "client!nla", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub20() {
        super(0, true);
    }

    @OriginalMember(owner = "client!nla", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            OrthoMode.cachedViewportWidth = -73;
        }
        if (arg2 == 0) {
            this.firstOffsetX = arg1.g2();
        } else if (arg2 == 1) {
            this.firstOffsetY = arg1.g2();
        } else if (arg2 == 2) {
            this.secondOffsetX = arg1.g2();
        } else if (arg2 == 3) {
            this.secondOffsetY = arg1.g2();
        } else if (arg2 == 4) {
            this.frequency = arg1.g2();
        } else if (arg2 == 5) {
            this.thickness = arg1.g2();
        } else if (arg2 == 6) {
            this.widthScale = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!nla", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        Static481.method6475();
    }

    @OriginalMember(owner = "client!nla", name = "c", descriptor = "(III)Z")
    public boolean inFirstBand(@OriginalArg(0) int y, @OriginalArg(1) int x) {
        @Pc(13) int phase = (y - x) * this.frequency >> 12;
        @Pc(33) int wave = Static24.anIntArray33[phase * 255 >> 12 & 0xFF];
        @Pc(40) int amplitude = (wave << 12) / this.frequency;
        @Pc(47) int scaledAmplitude = (amplitude << 12) / this.widthScale;
        @Pc(54) int halfWidth = this.thickness * scaledAmplitude >> 12;
        return y + x < halfWidth && -halfWidth < x + y;
    }

    @OriginalMember(owner = "client!nla", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 < 107) {
            return null;
        }
        @Pc(17) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(27) int centreOffsetY = MonochromeImageCache.anIntArray341[y] - 2048;
            for (@Pc(29) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(37) int centreOffsetX = EnvironmentLight.anIntArray92[x] - 2048;
                @Pc(42) int firstSumX = this.firstOffsetX + centreOffsetX;
                @Pc(52) int firstWrapX = firstSumX < -2048 ? firstSumX + 4096 : firstSumX;
                @Pc(64) int firstX = firstWrapX > 2048 ? firstWrapX - 4096 : firstWrapX;
                @Pc(69) int firstSumY = this.firstOffsetY + centreOffsetY;
                @Pc(81) int firstWrapY = firstSumY >= -2048 ? firstSumY : firstSumY + 4096;
                @Pc(91) int firstY = firstWrapY <= 2048 ? firstWrapY : firstWrapY - 4096;
                @Pc(96) int secondSumX = this.secondOffsetX + centreOffsetX;
                @Pc(108) int secondWrapX = secondSumX < -2048 ? secondSumX + 4096 : secondSumX;
                @Pc(120) int secondX = secondWrapX > 2048 ? secondWrapX - 4096 : secondWrapX;
                @Pc(126) int secondSumY = centreOffsetY + this.secondOffsetY;
                @Pc(136) int secondWrapY = secondSumY < -2048 ? secondSumY + 4096 : secondSumY;
                @Pc(146) int secondY = secondWrapY <= 2048 ? secondWrapY : secondWrapY - 4096;
                output[x] = this.inFirstBand(firstY, firstX) || this.inSecondBand(secondX, secondY) ? 4096 : 0;
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!nla", name = "d", descriptor = "(III)Z")
    public boolean inSecondBand(@OriginalArg(1) int x, @OriginalArg(2) int y) {
        @Pc(18) int phase = this.frequency * (x + y) >> 12;
        @Pc(28) int wave = Static24.anIntArray33[phase * 255 >> 12 & 0xFF];
        @Pc(35) int amplitude = (wave << 12) / this.frequency;
        @Pc(42) int scaledAmplitude = (amplitude << 12) / this.widthScale;
        @Pc(49) int halfWidth = scaledAmplitude * this.thickness >> 12;
        return y - x < halfWidth && -halfWidth < y - x;
    }
}

import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Inverts a source layer, so that full intensity becomes zero and zero becomes full intensity.
 */
@OriginalClass("client!ko")
public final class TextureOpInvert extends TextureOp {

    @OriginalMember(owner = "client!ko", name = "<init>", descriptor = "()V")
    public TextureOpInvert() {
        super(1, false);
    }

    @OriginalMember(owner = "client!ko", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(21) int[] source = this.method9422(y, 0);
            for (@Pc(23) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                output[x] = 4096 - source[x];
            }
        }
        return arg0 <= 107 ? null : output;
    }

    @OriginalMember(owner = "client!ko", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(18) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(28) int[][] source = this.method9413(0, y);
            @Pc(32) int[] sourceRed = source[0];
            @Pc(36) int[] sourceGreen = source[1];
            @Pc(40) int[] sourceBlue = source[2];
            @Pc(44) int[] outputRed = output[0];
            @Pc(48) int[] outputGreen = output[1];
            @Pc(52) int[] outputBlue = output[2];
            for (@Pc(54) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                outputRed[x] = 4096 - sourceRed[x];
                outputGreen[x] = 4096 - sourceGreen[x];
                outputBlue[x] = 4096 - sourceBlue[x];
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!ko", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            Static341.method5033(111);
        }
        if (arg2 == 0) {
            super.monochrome = arg1.g1() == 1;
        }
    }
}

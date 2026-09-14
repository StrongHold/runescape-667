import com.jagex.Static14;
import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Mixes two source layers, using a third source as the per texel blend weight.
 */
@OriginalClass("client!ai")
public final class TextureOpBlend extends TextureOp {

    @OriginalMember(owner = "client!ai", name = "<init>", descriptor = "()V")
    public TextureOpBlend() {
        super(3, false);
    }

    @OriginalMember(owner = "client!ai", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(11) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(27) int[] weights = this.method9422(y, 2);
            @Pc(33) int[][] first = this.method9413(0, y);
            @Pc(39) int[][] second = this.method9413(1, y);
            @Pc(43) int[] outputRed = output[0];
            @Pc(47) int[] outputGreen = output[1];
            @Pc(51) int[] outputBlue = output[2];
            @Pc(55) int[] firstRed = first[0];
            @Pc(59) int[] firstGreen = first[1];
            @Pc(63) int[] firstBlue = first[2];
            @Pc(67) int[] secondRed = second[0];
            @Pc(71) int[] secondGreen = second[1];
            @Pc(75) int[] secondBlue = second[2];
            for (@Pc(77) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(83) int weight = weights[x];
                if (weight == 4096) {
                    outputRed[x] = firstRed[x];
                    outputGreen[x] = firstGreen[x];
                    outputBlue[x] = firstBlue[x];
                } else if (weight == 0) {
                    outputRed[x] = secondRed[x];
                    outputGreen[x] = secondGreen[x];
                    outputBlue[x] = secondBlue[x];
                } else {
                    @Pc(97) int inverse = 4096 - weight;
                    outputRed[x] = inverse * secondRed[x] + firstRed[x] * weight >> 12;
                    outputGreen[x] = secondGreen[x] * inverse + weight * firstGreen[x] >> 12;
                    outputBlue[x] = secondBlue[x] * inverse + weight * firstBlue[x] >> 12;
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!ai", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(21) int[] first = this.method9422(y, 0);
            @Pc(27) int[] second = this.method9422(y, 1);
            @Pc(33) int[] weights = this.method9422(y, 2);
            for (@Pc(35) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(41) int weight = weights[x];
                if (weight == 4096) {
                    output[x] = first[x];
                } else if (weight == 0) {
                    output[x] = second[x];
                } else {
                    output[x] = weight * first[x] + (4096 - weight) * second[x] >> 12;
                }
            }
        }
        if (arg0 <= 107) {
            Static14.anIntArray25 = null;
        }
        return output;
    }

    @OriginalMember(owner = "client!ai", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            super.monochrome = arg1.g1() == 1;
        }
        if (arg0) {
            Static14.anIntArray25 = null;
        }
    }
}

import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Displaces a source layer, taking the direction of the offset from a second source and its length
 * from a third.
 */
@OriginalClass("client!gd")
public final class TextureOpDisplace extends TextureOp {

    @OriginalMember(owner = "client!gd", name = "L", descriptor = "I")
    public int distance = 32768;

    @OriginalMember(owner = "client!gd", name = "<init>", descriptor = "()V")
    public TextureOpDisplace() {
        super(3, false);
    }

    @OriginalMember(owner = "client!gd", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(19) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(29) int[] angles = this.method9422(y, 1);
            @Pc(35) int[] lengths = this.method9422(y, 2);
            @Pc(39) int[] outputRed = output[0];
            @Pc(43) int[] outputGreen = output[1];
            @Pc(47) int[] outputBlue = output[2];
            for (@Pc(49) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(61) int angle = angles[x] * 255 >> 12 & 0xFF;
                @Pc(70) int length = lengths[x] * this.distance >> 12;
                @Pc(78) int offsetX = Static24.anIntArray33[angle] * length >> 12;
                @Pc(86) int offsetY = Static222.anIntArray289[angle] * length >> 12;
                @Pc(95) int sampleX = EnvironmentLight.anInt8580 & x + (offsetX >> 12);
                @Pc(103) int sampleY = EnvironmentLight.anInt7343 & (offsetY >> 12) + y;
                @Pc(109) int[][] source = this.method9413(0, sampleY);
                outputRed[x] = source[0][sampleX];
                outputGreen[x] = source[1][sampleX];
                outputBlue[x] = source[2][sampleX];
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!gd", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.distance = arg1.g2() << 4;
        } else if (arg2 == 1) {
            super.monochrome = arg1.g1() == 1;
        }
        if (arg0) {
            js5.MATERIALS = null;
        }
    }

    @OriginalMember(owner = "client!gd", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 < 107) {
            this.distance = -107;
        }
        @Pc(19) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(29) int[] angles = this.method9422(y, 1);
            @Pc(35) int[] lengths = this.method9422(y, 2);
            for (@Pc(37) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(47) int angle = angles[x] >> 4 & 0xFF;
                @Pc(56) int length = this.distance * lengths[x] >> 12;
                @Pc(64) int offsetX = Static24.anIntArray33[angle] * length >> 12;
                @Pc(72) int offsetY = Static222.anIntArray289[angle] * length >> 12;
                @Pc(80) int sampleX = EnvironmentLight.anInt8580 & x + (offsetX >> 12);
                @Pc(88) int sampleY = y + (offsetY >> 12) & EnvironmentLight.anInt7343;
                @Pc(94) int[] source = this.method9422(sampleY, 0);
                output[x] = source[sampleX];
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!gd", name = "c", descriptor = "(I)V")
    @Override
    public void method9421() {
        Static481.method6475();
    }
}

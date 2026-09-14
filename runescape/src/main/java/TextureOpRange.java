import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Remaps the full intensity of a source layer onto the span between a minimum and a maximum.
 */
@OriginalClass("client!jr")
public final class TextureOpRange extends TextureOp {

    @OriginalMember(owner = "client!jr", name = "F", descriptor = "I")
    public int min = 1024;

    @OriginalMember(owner = "client!jr", name = "L", descriptor = "I")
    public int range = 2048;

    @OriginalMember(owner = "client!jr", name = "G", descriptor = "I")
    public int max = 3072;

    @OriginalMember(owner = "client!jr", name = "<init>", descriptor = "()V")
    public TextureOpRange() {
        super(1, false);
    }

    @OriginalMember(owner = "client!jr", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        this.range = this.max - this.min;
    }

    @OriginalMember(owner = "client!jr", name = "a", descriptor = "(IZ)[[I")
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
                outputRed[x] = this.min + (sourceRed[x] * this.range >> 12);
                outputGreen[x] = (this.range * sourceGreen[x] >> 12) + this.min;
                outputBlue[x] = (this.range * sourceBlue[x] >> 12) + this.min;
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!jr", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(21) int[] source = this.method9422(y, 0);
            for (@Pc(23) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                output[x] = (this.range * source[x] >> 12) + this.min;
            }
        }
        if (arg0 < 107) {
            this.range = -15;
        }
        return output;
    }

    @OriginalMember(owner = "client!jr", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            this.postDecode();
        }
        if (arg2 == 0) {
            this.min = arg1.g2();
        } else if (arg2 == 1) {
            this.max = arg1.g2();
        } else if (arg2 == 2) {
            super.monochrome = arg1.g1() == 1;
        }
    }
}

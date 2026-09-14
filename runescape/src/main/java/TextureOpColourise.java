import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Tints a greyscale source with a colour, replacing any texel that is already coloured with that
 * colour at full strength.
 */
@OriginalClass("client!be")
public final class TextureOpColourise extends TextureOp {

    @OriginalMember(owner = "client!be", name = "G", descriptor = "I")
    public int blue = 4096;

    @OriginalMember(owner = "client!be", name = "F", descriptor = "I")
    public int green = 4096;

    @OriginalMember(owner = "client!be", name = "J", descriptor = "I")
    public int red = 4096;

    @OriginalMember(owner = "client!be", name = "<init>", descriptor = "()V")
    public TextureOpColourise() {
        super(1, false);
    }

    @OriginalMember(owner = "client!be", name = "a", descriptor = "(IZ)[[I")
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
                @Pc(60) int sourceR = sourceRed[x];
                @Pc(64) int sourceB = sourceBlue[x];
                @Pc(68) int sourceG = sourceGreen[x];
                if (sourceR == sourceB && sourceG == sourceB) {
                    outputRed[x] = this.red * sourceR >> 12;
                    outputGreen[x] = sourceB * this.green >> 12;
                    outputBlue[x] = sourceG * this.blue >> 12;
                } else {
                    outputRed[x] = this.red;
                    outputGreen[x] = this.green;
                    outputBlue[x] = this.blue;
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!be", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            ChatLine.nextUid = -61;
        }
        if (arg2 == 0) {
            this.red = arg1.g2();
        } else if (arg2 == 1) {
            this.green = arg1.g2();
        } else if (arg2 == 2) {
            this.blue = arg1.g2();
        }
    }
}

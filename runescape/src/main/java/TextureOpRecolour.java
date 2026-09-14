import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Scales the channels of every texel that matches a key colour within a tolerance, and passes the
 * rest of the source through untouched.
 */
@OriginalClass("client!bw")
public final class TextureOpRecolour extends TextureOp {

    @OriginalMember(owner = "client!bw", name = "K", descriptor = "I")
    public int redScale = 4096;

    @OriginalMember(owner = "client!bw", name = "L", descriptor = "I")
    public int greenScale = 4096;

    @OriginalMember(owner = "client!bw", name = "N", descriptor = "I")
    public int tolerance = 409;

    @OriginalMember(owner = "client!bw", name = "O", descriptor = "[I")
    public final int[] keyColour = new int[3];

    @OriginalMember(owner = "client!bw", name = "R", descriptor = "I")
    public int blueScale = 4096;

    @OriginalMember(owner = "client!bw", name = "<init>", descriptor = "()V")
    public TextureOpRecolour() {
        super(1, false);
    }

    @OriginalMember(owner = "client!bw", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(17) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(27) int[][] source = this.method9413(0, y);
            @Pc(31) int[] sourceRed = source[0];
            @Pc(35) int[] sourceGreen = source[1];
            @Pc(39) int[] sourceBlue = source[2];
            @Pc(43) int[] outputRed = output[0];
            @Pc(47) int[] outputGreen = output[1];
            @Pc(51) int[] outputBlue = output[2];
            for (@Pc(53) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(59) int sourceR = sourceRed[x];
                @Pc(67) int difference = sourceR - this.keyColour[0];
                if (difference < 0) {
                    difference = -difference;
                }
                if (difference > this.tolerance) {
                    outputRed[x] = sourceR;
                    outputGreen[x] = sourceGreen[x];
                    outputBlue[x] = sourceBlue[x];
                } else {
                    @Pc(103) int sourceG = sourceGreen[x];
                    difference = sourceG - this.keyColour[1];
                    if (difference < 0) {
                        difference = -difference;
                    }
                    if (difference > this.tolerance) {
                        outputRed[x] = sourceR;
                        outputGreen[x] = sourceG;
                        outputBlue[x] = sourceBlue[x];
                    } else {
                        @Pc(149) int sourceB = sourceBlue[x];
                        difference = sourceB - this.keyColour[2];
                        if (difference < 0) {
                            difference = -difference;
                        }
                        if (this.tolerance < difference) {
                            outputRed[x] = sourceR;
                            outputGreen[x] = sourceG;
                            outputBlue[x] = sourceB;
                        } else {
                            outputRed[x] = sourceR * this.redScale >> 12;
                            outputGreen[x] = this.greenScale * sourceG >> 12;
                            outputBlue[x] = sourceB * this.blueScale >> 12;
                        }
                    }
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!bw", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.tolerance = arg1.g2();
        } else if (arg2 == 1) {
            this.blueScale = arg1.g2();
        } else if (arg2 == 2) {
            this.greenScale = arg1.g2();
        } else if (arg2 == 3) {
            this.redScale = arg1.g2();
        } else if (arg2 == 4) {
            @Pc(72) int rgb = arg1.g3();
            this.keyColour[1] = rgb >> 4 & 0xFF0;
            this.keyColour[2] = rgb >> 12 & 0x0;
            this.keyColour[0] = (rgb & 0xFF0000) << 4;
        }
    }
}

import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Turns a source layer into a normal map, by taking the gradient of its intensity along both axes
 * and writing the unit length surface normal into the red, green and blue channels.
 */
@OriginalClass("client!mt")
public final class TextureOpNormalMap extends TextureOp {

    @OriginalMember(owner = "client!mt", name = "I", descriptor = "I")
    public int scale = 4096;

    @OriginalMember(owner = "client!mt", name = "F", descriptor = "Z")
    public boolean unsigned = true;

    @OriginalMember(owner = "client!mt", name = "<init>", descriptor = "()V")
    public TextureOpNormalMap() {
        super(1, false);
    }

    @OriginalMember(owner = "client!mt", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            return;
        }
        if (arg2 == 0) {
            this.scale = arg1.g2();
        } else if (arg2 == 1) {
            this.unsigned = arg1.g1() == 1;
        }
    }

    @OriginalMember(owner = "client!mt", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(11) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(25) int[] above = this.method9422(y - 1 & EnvironmentLight.anInt7343, 0);
            @Pc(31) int[] row = this.method9422(y, 0);
            @Pc(41) int[] below = this.method9422(y + 1 & EnvironmentLight.anInt7343, 0);
            @Pc(45) int[] outputRed = output[0];
            @Pc(49) int[] outputGreen = output[1];
            @Pc(53) int[] outputBlue = output[2];
            for (@Pc(55) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(68) int deltaY = (below[x] - above[x]) * this.scale;
                @Pc(88) int deltaX = this.scale * (row[EnvironmentLight.anInt8580 & x + 1] - row[x - 1 & EnvironmentLight.anInt8580]);
                @Pc(92) int gradientX = deltaX >> 12;
                @Pc(96) int gradientY = deltaY >> 12;
                @Pc(102) int squareX = gradientX * gradientX >> 12;
                @Pc(108) int squareY = gradientY * gradientY >> 12;
                @Pc(122) int length = (int) (Math.sqrt((float) (squareY + squareX + 4096) / 4096.0F) * 4096.0D);
                @Pc(128) int normalX;
                @Pc(126) int normalY;
                @Pc(130) int normalZ;
                if (length == 0) {
                    normalY = 0;
                    normalX = 0;
                    normalZ = 0;
                } else {
                    normalZ = 16777216 / length;
                    normalY = deltaY / length;
                    normalX = deltaX / length;
                }
                if (this.unsigned) {
                    normalX = (normalX >> 1) + 2048;
                    normalZ = (normalZ >> 1) + 2048;
                    normalY = (normalY >> 1) + 2048;
                }
                outputRed[x] = normalX;
                outputGreen[x] = normalY;
                outputBlue[x] = normalZ;
            }
        }
        return output;
    }
}

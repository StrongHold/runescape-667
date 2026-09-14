import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Treats a source layer as a height field and shades it by the steepness of its slope, so flat
 * areas stay black and edges pick up brightness.
 */
@OriginalClass("client!ola")
public final class Node_Sub1_Sub22 extends TextureOp {

    @OriginalMember(owner = "client!ola", name = "G", descriptor = "I")
    public int amplitude = 4096;

    @OriginalMember(owner = "client!ola", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub22() {
        super(1, true);
    }

    @OriginalMember(owner = "client!ola", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (arg0) {
            this.method9416(true, null, 2);
        }
        if (opcode == 0) {
            this.amplitude = packet.g2();
        }
    }

    @OriginalMember(owner = "client!ola", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(18) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(32) int[] above = this.method9422(EnvironmentLight.anInt7343 & y - 1, 0);
            @Pc(38) int[] source = this.method9422(y, 0);
            @Pc(48) int[] below = this.method9422(y + 1 & EnvironmentLight.anInt7343, 0);
            for (@Pc(50) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(63) int scaledGradientY = (below[x] - above[x]) * this.amplitude;
                @Pc(83) int scaledGradientX = (source[x + 1 & EnvironmentLight.anInt8580] - source[EnvironmentLight.anInt8580 & x - 1]) * this.amplitude;
                @Pc(87) int gradientX = scaledGradientX >> 12;
                @Pc(91) int gradientY = scaledGradientY >> 12;
                @Pc(97) int squareX = gradientX * gradientX >> 12;
                @Pc(103) int squareY = gradientY * gradientY >> 12;
                @Pc(118) int normalLength = (int) (Math.sqrt((float) (squareX + squareY + 4096) / 4096.0F) * 4096.0D);
                @Pc(130) int flatness = normalLength == 0 ? 0 : 16777216 / normalLength;
                output[x] = 4096 - flatness;
            }
        }
        return output;
    }
}

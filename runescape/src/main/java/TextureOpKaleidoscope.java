import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Mirrors one eighth of a source layer into the other seven, so the texture reads as a
 * kaleidoscope around its centre.
 */
@OriginalClass("client!vba")
public final class TextureOpKaleidoscope extends TextureOp {

    @OriginalMember(owner = "client!vba", name = "<init>", descriptor = "()V")
    public TextureOpKaleidoscope() {
        super(1, false);
    }

    @OriginalMember(owner = "client!vba", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (opcode == 0) {
            super.monochrome = packet.g1() == 1;
        }
        if (arg0) {
            Component.interfacesJs5 = null;
        }
    }

    @OriginalMember(owner = "client!vba", name = "a", descriptor = "(IIB)V")
    public void reflect(@OriginalArg(0) int x, @OriginalArg(1) int y) {
        @Pc(9) int columnCoord = EnvironmentLight.anIntArray92[x];
        @Pc(13) int rowCoord = MonochromeImageCache.anIntArray341[y];
        @Pc(24) float angle = (float) Math.atan2(columnCoord - 2048, rowCoord - 2048);
        if ((double) angle >= -3.141592653589793D && (double) angle <= -2.356194490192345D) {
            Static187.anInt3093 = y;
            Static37.anInt916 = x;
        } else if ((double) angle <= -1.5707963267948966D && (double) angle >= -2.356194490192345D) {
            Static187.anInt3093 = x;
            Static37.anInt916 = y;
        } else if ((double) angle <= -0.7853981633974483D && (double) angle >= -1.5707963267948966D) {
            Static37.anInt916 = EnvironmentLight.anInt9289 - y;
            Static187.anInt3093 = x;
        } else if (angle <= 0.0F && (double) angle >= -0.7853981633974483D) {
            Static37.anInt916 = x;
            Static187.anInt3093 = EnvironmentLight.anInt53 - y;
        } else if (angle >= 0.0F && (double) angle <= 0.7853981633974483D) {
            Static187.anInt3093 = EnvironmentLight.anInt53 - y;
            Static37.anInt916 = EnvironmentLight.anInt9289 - x;
        } else if ((double) angle >= 0.7853981633974483D && (double) angle <= 1.5707963267948966D) {
            Static187.anInt3093 = EnvironmentLight.anInt53 - x;
            Static37.anInt916 = EnvironmentLight.anInt9289 - y;
        } else if ((double) angle >= 1.5707963267948966D && (double) angle <= 2.356194490192345D) {
            Static37.anInt916 = y;
            Static187.anInt3093 = EnvironmentLight.anInt53 - x;
        } else if ((double) angle >= 2.356194490192345D && (double) angle <= 3.141592653589793D) {
            Static187.anInt3093 = y;
            Static37.anInt916 = EnvironmentLight.anInt9289 - x;
        }
        Static187.anInt3093 &= EnvironmentLight.anInt7343;
        Static37.anInt916 &= EnvironmentLight.anInt8580;
    }

    @OriginalMember(owner = "client!vba", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(21) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(29) int[] outputRed = output[0];
            @Pc(33) int[] outputGreen = output[1];
            @Pc(37) int[] outputBlue = output[2];
            for (@Pc(39) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                this.reflect(x, y);
                @Pc(52) int[][] source = this.method9413(0, Static187.anInt3093);
                outputRed[x] = source[0][Static37.anInt916];
                outputGreen[x] = source[1][Static37.anInt916];
                outputBlue[x] = source[2][Static37.anInt916];
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!vba", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 <= 107) {
            ClientOptions.save();
        }
        @Pc(18) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            for (@Pc(24) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                this.reflect(x, y);
                @Pc(37) int[] source = this.method9422(Static187.anInt3093, 0);
                output[x] = source[Static37.anInt916];
            }
        }
        return output;
    }
}

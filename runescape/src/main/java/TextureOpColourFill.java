import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Fills the whole texture with one flat colour.
 */
@OriginalClass("client!ska")
public final class TextureOpColourFill extends TextureOp {

    @OriginalMember(owner = "client!ska", name = "Q", descriptor = "I")
    public int blue;

    @OriginalMember(owner = "client!ska", name = "K", descriptor = "I")
    public int green;

    @OriginalMember(owner = "client!ska", name = "J", descriptor = "I")
    public int red;

    @OriginalMember(owner = "client!ska", name = "<init>", descriptor = "()V")
    public TextureOpColourFill() {
        this(0);
    }

    @OriginalMember(owner = "client!ska", name = "<init>", descriptor = "(I)V")
    public TextureOpColourFill(@OriginalArg(0) int rgb) {
        super(0, false);
        this.setColour(rgb);
    }

    @OriginalMember(owner = "client!ska", name = "b", descriptor = "(II)V")
    public void setColour(@OriginalArg(1) int rgb) {
        this.green = rgb >> 4 & 0xFF0;
        this.blue = (rgb & 0xFF) << 4;
        this.red = rgb >> 12 & 0xFF0;
    }

    @OriginalMember(owner = "client!ska", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (opcode == 0) {
            this.setColour(packet.g3());
        }
        if (arg0) {
            Terrain.faceVertices = null;
        }
    }

    @OriginalMember(owner = "client!ska", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(17) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(25) int[] outputRed = output[0];
            @Pc(29) int[] outputGreen = output[1];
            @Pc(33) int[] outputBlue = output[2];
            for (@Pc(35) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                outputRed[x] = this.red;
                outputGreen[x] = this.green;
                outputBlue[x] = this.blue;
            }
        }
        return output;
    }
}

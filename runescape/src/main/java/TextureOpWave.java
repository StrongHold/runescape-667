import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Generates a repeating wave, either diagonal or radiating from the centre of the texture, shaped
 * as a sine, a sawtooth or a triangle.
 */
@OriginalClass("client!bu")
public final class TextureOpWave extends TextureOp {

    @OriginalMember(owner = "client!bu", name = "F", descriptor = "I")
    public int frequency = 1;

    @OriginalMember(owner = "client!bu", name = "N", descriptor = "I")
    public int shape = 0;

    @OriginalMember(owner = "client!bu", name = "P", descriptor = "I")
    public int waveform = 0;

    @OriginalMember(owner = "client!bu", name = "<init>", descriptor = "()V")
    public TextureOpWave() {
        super(0, true);
    }

    @OriginalMember(owner = "client!bu", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 <= 107) {
            this.frequency = -17;
        }
        @Pc(19) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(27) int rowCoord = MonochromeImageCache.anIntArray341[y];
            @Pc(33) int centreOffsetY = rowCoord - 2048 >> 1;
            for (@Pc(35) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(41) int columnCoord = EnvironmentLight.anIntArray92[x];
                @Pc(47) int centreOffsetX = columnCoord - 2048 >> 1;
                @Pc(58) int value;
                if (this.shape == 0) {
                    value = this.frequency * (columnCoord - rowCoord);
                } else {
                    @Pc(70) int squareDistance = centreOffsetX * centreOffsetX + centreOffsetY * centreOffsetY >> 12;
                    value = (int) (Math.sqrt((float) squareDistance / 4096.0F) * 4096.0D);
                    value = (int) ((double) (value * this.frequency) * 3.141592653589793D);
                }
                value -= value & 0xFFFFF000;
                if (this.waveform == 0) {
                    value = Static222.anIntArray289[value >> 4 & 0xFF] + 4096 >> 1;
                } else if (this.waveform == 2) {
                    value -= 2048;
                    if (value < 0) {
                        value = -value;
                    }
                    value = 2048 - value << 1;
                }
                output[x] = value;
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!bu", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        Static481.method6475();
    }

    @OriginalMember(owner = "client!bu", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            return;
        }
        if (arg2 == 0) {
            this.shape = arg1.g1();
        } else if (arg2 == 1) {
            this.waveform = arg1.g1();
        } else if (arg2 == 3) {
            this.frequency = arg1.g1();
        }
    }
}

import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Generates a square wave of hard edged stripes, running horizontally, vertically or along either
 * diagonal, with a settable stripe count and on to off ratio.
 */
@OriginalClass("client!tia")
public final class TextureOpSquareWave extends TextureOp {

    @OriginalMember(owner = "client!tia", name = "F", descriptor = "[I")
    public int[] bandStarts;

    @OriginalMember(owner = "client!tia", name = "O", descriptor = "[I")
    public int[] bandEnds;

    @OriginalMember(owner = "client!tia", name = "K", descriptor = "I")
    public int thickness = 2048;

    @OriginalMember(owner = "client!tia", name = "Q", descriptor = "I")
    public int direction = 0;

    @OriginalMember(owner = "client!tia", name = "N", descriptor = "I")
    public int bands = 10;

    @OriginalMember(owner = "client!tia", name = "<init>", descriptor = "()V")
    public TextureOpSquareWave() {
        super(0, true);
    }

    @OriginalMember(owner = "client!tia", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (arg0) {
            return;
        }
        if (opcode == 0) {
            this.bands = packet.g1();
        } else if (opcode == 1) {
            this.thickness = packet.g2();
        } else if (opcode == 2) {
            this.direction = packet.g1();
        }
    }

    @OriginalMember(owner = "client!tia", name = "g", descriptor = "(I)V")
    public void generateBands() {
        this.bandEnds = new int[this.bands + 1];
        this.bandStarts = new int[this.bands + 1];
        @Pc(21) int start = 0;
        @Pc(26) int spacing = 4096 / this.bands;
        @Pc(33) int width = spacing * this.thickness >> 12;
        for (@Pc(35) int band = 0; band < this.bands; band++) {
            this.bandStarts[band] = start;
            this.bandEnds[band] = start + width;
            start += spacing;
        }
        this.bandStarts[this.bands] = 4096;
        if (-3749 != -3749) {
            this.method9416(true, null, 96);
        }
        this.bandEnds[this.bands] = this.bandEnds[0] + 4096;
    }

    @OriginalMember(owner = "client!tia", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (arg0 <= 107) {
            InterfaceManager.rectangles = null;
        }
        if (super.monochromeCache.dirty) {
            @Pc(26) int rowCoord = MonochromeImageCache.anIntArray341[y];
            @Pc(35) int local35;
            if (this.direction == 0) {
                @Pc(153) short value = 0;
                for (local35 = 0; local35 < this.bands; local35++) {
                    if (this.bandStarts[local35] <= rowCoord && this.bandStarts[local35 + 1] > rowCoord) {
                        if (this.bandEnds[local35] > rowCoord) {
                            value = 4096;
                        }
                        break;
                    }
                }
                Arrays.set(output, 0, EnvironmentLight.anInt9289, value);
            } else {
                for (@Pc(31) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                    local35 = 0;
                    @Pc(37) short value = 0;
                    @Pc(41) int columnCoord = EnvironmentLight.anIntArray92[x];
                    @Pc(44) int local44 = this.direction;
                    if (local44 == 1) {
                        local35 = columnCoord;
                    } else if (local44 == 2) {
                        local35 = (columnCoord + rowCoord - 4096 >> 1) + 2048;
                    } else if (local44 == 3) {
                        local35 = (columnCoord - rowCoord >> 1) + 2048;
                    }
                    for (local44 = 0; local44 < this.bands; local44++) {
                        if (local35 >= this.bandStarts[local44] && local35 < this.bandStarts[local44 + 1]) {
                            if (this.bandEnds[local44] > local35) {
                                value = 4096;
                            }
                            break;
                        }
                    }
                    output[x] = value;
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!tia", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        this.generateBands();
    }
}

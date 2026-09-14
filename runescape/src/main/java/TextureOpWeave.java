import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Generates a tileable basket weave, a horizontal strand crossing a vertical strand, each shaded
 * from full intensity where it passes over to zero where it passes under.
 */
@OriginalClass("client!ud")
public final class TextureOpWeave extends TextureOp {

    @OriginalMember(owner = "client!ud", name = "F", descriptor = "I")
    public int halfWidth = 585;

    @OriginalMember(owner = "client!ud", name = "<init>", descriptor = "()V")
    public TextureOpWeave() {
        super(0, true);
    }

    @OriginalMember(owner = "client!ud", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (opcode == 0) {
            this.halfWidth = packet.g2();
        }
        if (arg0) {
            this.method9416(false, null, 11);
        }
    }

    @OriginalMember(owner = "client!ud", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 <= 107) {
            WorldComparator.compare(null, 60, -47, true, null, true);
        }
        @Pc(25) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(33) int rowCoord = MonochromeImageCache.anIntArray341[y];
            for (@Pc(35) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(41) int columnCoord = EnvironmentLight.anIntArray92[x];
                @Pc(80) int delta;
                if (columnCoord > this.halfWidth && columnCoord < 4096 - this.halfWidth && rowCoord > 2048 - this.halfWidth && rowCoord < this.halfWidth + 2048) {
                    delta = 2048 - columnCoord;
                    delta = delta >= 0 ? delta : -delta;
                    delta <<= 0xC;
                    delta /= 2048 - this.halfWidth;
                    output[x] = 4096 - delta;
                } else if (2048 - this.halfWidth < columnCoord && columnCoord < this.halfWidth + 2048) {
                    delta = rowCoord - 2048;
                    delta = delta < 0 ? -delta : delta;
                    delta -= this.halfWidth;
                    delta <<= 0xC;
                    output[x] = delta / (2048 - this.halfWidth);
                } else if (this.halfWidth > rowCoord || rowCoord > 4096 - this.halfWidth) {
                    delta = columnCoord - 2048;
                    @Pc(200) int distance = delta < 0 ? -delta : delta;
                    @Pc(205) int offset = distance - this.halfWidth;
                    @Pc(209) int scaled = offset << 12;
                    output[x] = scaled / (2048 - this.halfWidth);
                } else if (this.halfWidth <= columnCoord && columnCoord <= 4096 - this.halfWidth) {
                    output[x] = 0;
                } else {
                    delta = 2048 - rowCoord;
                    delta = delta >= 0 ? delta : -delta;
                    delta <<= 0xC;
                    delta /= 2048 - this.halfWidth;
                    output[x] = 4096 - delta;
                }
            }
        }
        return output;
    }
}

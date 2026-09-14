import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Draws a herringbone weave. The texture is divided into a grid of cells, and the edge between two
 * cells is left out on a four cell diagonal cycle, so that the cells pair up into upright and flat
 * tiles that interlock.
 */
@OriginalClass("client!ws")
public final class TextureOpHerringbone extends TextureOp {

    @OriginalMember(owner = "client!ws", name = "G", descriptor = "I")
    public int rows = 1;

    @OriginalMember(owner = "client!ws", name = "F", descriptor = "I")
    public int edgeWidth = 204;

    @OriginalMember(owner = "client!ws", name = "K", descriptor = "I")
    public int columns = 1;

    @OriginalMember(owner = "client!ws", name = "<init>", descriptor = "()V")
    public TextureOpHerringbone() {
        super(0, true);
    }

    @OriginalMember(owner = "client!ws", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.columns = arg1.g1();
        } else if (arg2 == 1) {
            this.rows = arg1.g1();
        } else if (arg2 == 2) {
            this.edgeWidth = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!ws", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            for (@Pc(17) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(23) int columnCoord = EnvironmentLight.anIntArray92[x];
                @Pc(27) int rowCoord = MonochromeImageCache.anIntArray341[y];
                @Pc(34) int column = columnCoord * this.columns >> 12;
                @Pc(41) int row = rowCoord * this.rows >> 12;
                @Pc(51) int offsetX = columnCoord % (4096 / this.columns) * this.columns;
                @Pc(61) int offsetY = this.rows * (rowCoord % (4096 / this.rows));
                if (offsetY < this.edgeWidth) {
                    for (column -= row; column < 0; column += 4) {
                    }
                    while (column > 3) {
                        column -= 4;
                    }
                    if (column != 1) {
                        output[x] = 0;
                        continue;
                    }
                    if (offsetX < this.edgeWidth) {
                        output[x] = 0;
                        continue;
                    }
                }
                if (offsetX < this.edgeWidth) {
                    for (column -= row; column < 0; column += 4) {
                    }
                    while (column > 3) {
                        column -= 4;
                    }
                    if (column > 0) {
                        output[x] = 0;
                        continue;
                    }
                }
                output[x] = 4096;
            }
        }
        return output;
    }
}

import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Repeats a shrunken copy of a source layer over a grid of columns and rows.
 */
@OriginalClass("client!ur")
public final class TextureOpTile extends TextureOp {

    @OriginalMember(owner = "client!ur", name = "G", descriptor = "I")
    public int columns = 4;

    @OriginalMember(owner = "client!ur", name = "J", descriptor = "I")
    public int rows = 4;

    @OriginalMember(owner = "client!ur", name = "<init>", descriptor = "()V")
    public TextureOpTile() {
        super(1, false);
    }

    @OriginalMember(owner = "client!ur", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(19) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(28) int cellWidth = EnvironmentLight.anInt9289 / this.columns;
            @Pc(33) int cellHeight = EnvironmentLight.anInt53 / this.rows;
            @Pc(49) int[][] source;
            if (cellHeight > 0) {
                @Pc(39) int cellY = y % cellHeight;
                source = this.method9413(0, cellY * EnvironmentLight.anInt53 / cellHeight);
            } else {
                source = this.method9413(0, 0);
            }
            @Pc(61) int[] sourceRed = source[0];
            @Pc(65) int[] sourceGreen = source[1];
            @Pc(69) int[] sourceBlue = source[2];
            @Pc(73) int[] outputRed = output[0];
            @Pc(77) int[] outputGreen = output[1];
            @Pc(81) int[] outputBlue = output[2];
            for (@Pc(83) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(91) int sourceX;
                if (cellWidth <= 0) {
                    sourceX = 0;
                } else {
                    @Pc(97) int cellX = x % cellWidth;
                    sourceX = cellX * EnvironmentLight.anInt9289 / cellWidth;
                }
                outputRed[x] = sourceRed[sourceX];
                outputGreen[x] = sourceGreen[sourceX];
                outputBlue[x] = sourceBlue[sourceX];
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!ur", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (opcode == 0) {
            this.columns = packet.g1();
        } else if (opcode == 1) {
            this.rows = packet.g1();
        }
        if (arg0) {
            this.method9416(true, null, -94);
        }
    }

    @OriginalMember(owner = "client!ur", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (arg0 < 107) {
            this.monochromeOutput(94, -126);
        }
        if (super.monochromeCache.dirty) {
            @Pc(30) int cellWidth = EnvironmentLight.anInt9289 / this.columns;
            @Pc(35) int cellHeight = EnvironmentLight.anInt53 / this.rows;
            @Pc(51) int[] source;
            @Pc(41) int local41;
            if (cellHeight > 0) {
                local41 = y % cellHeight;
                source = this.method9422(EnvironmentLight.anInt53 * local41 / cellHeight, 0);
            } else {
                source = this.method9422(0, 0);
            }
            for (local41 = 0; local41 < EnvironmentLight.anInt9289; local41++) {
                if (cellWidth > 0) {
                    @Pc(71) int cellX = local41 % cellWidth;
                    output[local41] = source[cellX * EnvironmentLight.anInt9289 / cellWidth];
                } else {
                    output[local41] = source[0];
                }
            }
        }
        return output;
    }
}

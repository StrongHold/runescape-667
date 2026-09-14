import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.util.Random;

/**
 * Generates a grid of bricks with randomised row heights, brick widths and shades, alternate rows
 * staggered sideways and a gap left between every brick for the mortar.
 */
@OriginalClass("client!in")
public final class TextureOpBrick extends TextureOp {

    @OriginalMember(owner = "client!in", name = "U", descriptor = "[[I")
    public int[][] columnBounds;

    @OriginalMember(owner = "client!in", name = "L", descriptor = "I")
    public int cellWidth;

    @OriginalMember(owner = "client!in", name = "S", descriptor = "[I")
    public int[] rowBounds;

    @OriginalMember(owner = "client!in", name = "ab", descriptor = "I")
    public int halfGap;

    @OriginalMember(owner = "client!in", name = "Z", descriptor = "I")
    public int rowHeight;

    @OriginalMember(owner = "client!in", name = "T", descriptor = "[[I")
    public int[][] shades;

    @OriginalMember(owner = "client!in", name = "F", descriptor = "I")
    public int rowVariance = 204;

    @OriginalMember(owner = "client!in", name = "V", descriptor = "I")
    public int stagger = 1024;

    @OriginalMember(owner = "client!in", name = "db", descriptor = "I")
    public int gap = 81;

    @OriginalMember(owner = "client!in", name = "G", descriptor = "I")
    public int shadeVariance = 1024;

    @OriginalMember(owner = "client!in", name = "P", descriptor = "I")
    public int verticalOffset = 0;

    @OriginalMember(owner = "client!in", name = "X", descriptor = "I")
    public int rows = 8;

    @OriginalMember(owner = "client!in", name = "bb", descriptor = "I")
    public int columnVariance = 409;

    @OriginalMember(owner = "client!in", name = "H", descriptor = "I")
    public int columns = 4;

    @OriginalMember(owner = "client!in", name = "<init>", descriptor = "()V")
    public TextureOpBrick() {
        super(0, true);
    }

    @OriginalMember(owner = "client!in", name = "g", descriptor = "(I)V")
    public void buildLayout(@OriginalArg(0) int arg0) {
        @Pc(12) Random random = new Random(this.rows);
        this.cellWidth = 4096 / this.columns;
        this.rowHeight = 4096 / this.rows;
        this.halfGap = this.gap / 2;
        @Pc(35) int halfCellWidth = this.cellWidth / 2;
        this.columnBounds = new int[this.rows][this.columns + 1];
        if (arg0 != 25428) {
            Static278.anIntArray351 = null;
        }
        this.rowBounds = new int[this.rows + 1];
        @Pc(63) int halfRowHeight = this.rowHeight / 2;
        this.shades = new int[this.rows][this.columns];
        this.rowBounds[0] = 0;
        for (@Pc(77) int row = 0; row < this.rows; row++) {
            @Pc(86) int local86;
            @Pc(98) int local98;
            if (row > 0) {
                local86 = this.rowHeight;
                local98 = (Node_Sub1_Sub27.method8326(-5208, 4096, random) - 2048) * this.rowVariance >> 12;
                @Pc(106) int height = local86 + (local98 * halfRowHeight >> 12);
                this.rowBounds[row] = this.rowBounds[row - 1] + height;
            }
            this.columnBounds[row][0] = 0;
            for (local86 = 0; local86 < this.columns; local86++) {
                if (local86 > 0) {
                    local98 = this.cellWidth;
                    @Pc(152) int noise = (Node_Sub1_Sub27.method8326(-5208, 4096, random) - 2048) * this.columnVariance >> 12;
                    local98 += halfCellWidth * noise >> 12;
                    this.columnBounds[row][local86] = local98 + this.columnBounds[row][local86 - 1];
                }
                this.shades[row][local86] = this.shadeVariance > 0 ? 4096 - Node_Sub1_Sub27.method8326(arg0 ^ 0xFFFF88FC, this.shadeVariance, random) : 4096;
            }
            this.columnBounds[row][this.columns] = 4096;
        }
        this.rowBounds[this.rows] = 4096;
    }

    @OriginalMember(owner = "client!in", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.columns = arg1.g1();
        } else if (arg2 == 1) {
            this.rows = arg1.g1();
        } else if (arg2 == 2) {
            this.columnVariance = arg1.g2();
        } else if (arg2 == 3) {
            this.rowVariance = arg1.g2();
        } else if (arg2 == 4) {
            this.stagger = arg1.g2();
        } else if (arg2 == 5) {
            this.verticalOffset = arg1.g2();
        } else if (arg2 == 6) {
            this.gap = arg1.g2();
        } else if (arg2 == 7) {
            this.shadeVariance = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!in", name = "c", descriptor = "(I)V")
    @Override
    public void method9421() {
        this.buildLayout(25428);
    }

    @OriginalMember(owner = "client!in", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 < 107) {
            Static278.anIntArray350 = null;
        }
        @Pc(18) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(24) int row = 0;
            @Pc(31) int rowCoord;
            for (rowCoord = this.verticalOffset + MonochromeImageCache.anIntArray341[y]; rowCoord < 0; rowCoord += 4096) {
                /* empty */
            }
            while (rowCoord > 4096) {
                rowCoord -= 4096;
            }
            while (row < this.rows && this.rowBounds[row] <= rowCoord) {
                row++;
            }
            @Pc(79) int rowIndex = row - 1;
            @Pc(90) boolean even = (row & 0x1) == 0;
            @Pc(95) int rowEnd = this.rowBounds[row];
            @Pc(102) int rowStart = this.rowBounds[row - 1];
            if (this.halfGap + rowStart < rowCoord && rowCoord < rowEnd - this.halfGap) {
                for (@Pc(123) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                    @Pc(127) int column = 0;
                    @Pc(137) int offset = even ? this.stagger : -this.stagger;
                    @Pc(148) int columnCoord;
                    for (columnCoord = EnvironmentLight.anIntArray92[x] + (offset * this.cellWidth >> 12); columnCoord < 0; columnCoord += 4096) {
                        /* empty */
                    }
                    while (columnCoord > 4096) {
                        columnCoord -= 4096;
                    }
                    while (column < this.columns && this.columnBounds[rowIndex][column] <= columnCoord) {
                        column++;
                    }
                    @Pc(199) int columnIndex = column - 1;
                    @Pc(206) int columnEnd = this.columnBounds[rowIndex][column];
                    @Pc(213) int columnStart = this.columnBounds[rowIndex][columnIndex];
                    if (columnCoord > this.halfGap + columnStart && columnCoord < columnEnd - this.halfGap) {
                        output[x] = this.shades[rowIndex][columnIndex];
                    } else {
                        output[x] = 0;
                    }
                }
            } else {
                Arrays.set(output, 0, EnvironmentLight.anInt9289, 0);
            }
        }
        return output;
    }
}

import com.jagex.core.io.Packet;
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
 * Generates cellular (Worley) noise. The texture is divided into a grid of cells, each cell holds
 * one randomly jittered feature point, and every pixel is shaded by its distance to the nearest
 * feature points under a selectable distance metric.
 */
@OriginalClass("client!or")
public final class Node_Sub1_Sub23 extends TextureOp {

    @OriginalMember(owner = "client!or", name = "ab", descriptor = "I")
    public int jitter = 2048;

    @OriginalMember(owner = "client!or", name = "G", descriptor = "I")
    public int outputMode = 2;

    @OriginalMember(owner = "client!or", name = "T", descriptor = "I")
    public int columns = 5;

    @OriginalMember(owner = "client!or", name = "I", descriptor = "[S")
    public short[] cellOffsets = new short[512];

    @OriginalMember(owner = "client!or", name = "M", descriptor = "I")
    public int distanceMetric = 1;

    @OriginalMember(owner = "client!or", name = "U", descriptor = "[B")
    public byte[] permutation = new byte[512];

    @OriginalMember(owner = "client!or", name = "V", descriptor = "I")
    public int rows = 5;

    @OriginalMember(owner = "client!or", name = "S", descriptor = "I")
    public int seed = 0;

    @OriginalMember(owner = "client!or", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub23() {
        super(0, true);
    }

    @OriginalMember(owner = "client!or", name = "c", descriptor = "(B)V")
    public void generateCellOffsets() {
        @Pc(12) Random random = new Random(this.seed);
        this.cellOffsets = new short[512];
        if (this.jitter > 0) {
            for (@Pc(31) int index = 0; index < 512; index++) {
                this.cellOffsets[index] = (short) Node_Sub1_Sub27.method8326(-5208, this.jitter, random);
            }
        }
    }

    @OriginalMember(owner = "client!or", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (arg0) {
            return;
        }
        if (opcode == 0) {
            this.columns = this.rows = packet.g1();
        } else if (opcode == 1) {
            this.seed = packet.g1();
        } else if (opcode == 2) {
            this.jitter = packet.g2();
        } else if (opcode == 3) {
            this.outputMode = packet.g1();
        } else if (opcode == 4) {
            this.distanceMetric = packet.g1();
        } else if (opcode == 5) {
            this.columns = packet.g1();
        } else if (opcode == 6) {
            this.rows = packet.g1();
        }
    }

    @OriginalMember(owner = "client!or", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        this.permutation = Node_Sub1_Sub27.method9027(this.seed);
        this.generateCellOffsets();
    }

    @OriginalMember(owner = "client!or", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (arg0 <= 107) {
            InterfaceManager.reposition(null, -42, 121);
        }
        if (super.monochromeCache.dirty) {
            @Pc(34) int rowCoord = MonochromeImageCache.anIntArray341[y] * this.rows + 2048;
            @Pc(38) int row = rowCoord >> 12;
            @Pc(42) int rowEnd = row + 1;
            for (@Pc(44) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                Static162.fourthNearestDistance = Integer.MAX_VALUE;
                Static109.thirdNearestDistance = Integer.MAX_VALUE;
                Static417.secondNearestDistance = Integer.MAX_VALUE;
                Static143.nearestDistance = Integer.MAX_VALUE;
                @Pc(63) int columnCoord = this.columns * EnvironmentLight.anIntArray92[x] + 2048;
                @Pc(67) int column = columnCoord >> 12;
                @Pc(71) int columnEnd = column + 1;
                @Pc(163) int mode;
                for (@Pc(75) int neighbourRow = row - 1; neighbourRow <= rowEnd; neighbourRow++) {
                    @Pc(99) int rowHash = this.permutation[(this.rows > neighbourRow ? neighbourRow : neighbourRow - this.rows) & 0xFF] & 0xFF;
                    for (@Pc(103) int neighbourColumn = column - 1; neighbourColumn <= columnEnd; neighbourColumn++) {
                        @Pc(135) int xOffsetIndex = (this.permutation[rowHash + (neighbourColumn >= this.columns ? neighbourColumn - this.columns : neighbourColumn) & 0xFF] & 0xFF) * 2;
                        @Pc(139) int columnBase = -(neighbourColumn << 12);
                        @Pc(143) int yOffsetIndex = xOffsetIndex + 1;
                        @Pc(148) int dx = columnBase + columnCoord - this.cellOffsets[xOffsetIndex];
                        @Pc(160) int dy = rowCoord - (neighbourRow << 12) - this.cellOffsets[yOffsetIndex];
                        mode = this.distanceMetric;
                        @Pc(206) int distance;
                        if (mode == 1) {
                            distance = dx * dx + dy * dy >> 12;
                        } else if (mode == 3) {
                            dx = dx >= 0 ? dx : -dx;
                            dy = dy >= 0 ? dy : -dy;
                            distance = dx <= dy ? dy : dx;
                        } else if (mode == 4) {
                            dx = (int) (Math.sqrt((float) (dx < 0 ? -dx : dx) / 4096.0F) * 4096.0D);
                            dy = (int) (Math.sqrt((float) (dy < 0 ? -dy : dy) / 4096.0F) * 4096.0D);
                            distance = dy + dx;
                            distance = distance * distance >> 12;
                        } else if (mode == 5) {
                            dy *= dy;
                            dx *= dx;
                            distance = (int) (Math.sqrt(Math.sqrt((float) (dy + dx) / 1.6777216E7F)) * 4096.0D);
                        } else if (mode == 2) {
                            distance = (dx >= 0 ? dx : -dx) + (dy < 0 ? -dy : dy);
                        } else {
                            distance = (int) (Math.sqrt((float) (dy * dy + dx * dx) / 1.6777216E7F) * 4096.0D);
                        }
                        if (distance < Static143.nearestDistance) {
                            Static162.fourthNearestDistance = Static109.thirdNearestDistance;
                            Static109.thirdNearestDistance = Static417.secondNearestDistance;
                            Static417.secondNearestDistance = Static143.nearestDistance;
                            Static143.nearestDistance = distance;
                        } else if (distance < Static417.secondNearestDistance) {
                            Static162.fourthNearestDistance = Static109.thirdNearestDistance;
                            Static109.thirdNearestDistance = Static417.secondNearestDistance;
                            Static417.secondNearestDistance = distance;
                        } else if (distance < Static109.thirdNearestDistance) {
                            Static162.fourthNearestDistance = Static109.thirdNearestDistance;
                            Static109.thirdNearestDistance = distance;
                        } else if (distance < Static162.fourthNearestDistance) {
                            Static162.fourthNearestDistance = distance;
                        }
                    }
                }
                mode = this.outputMode;
                if (mode == 0) {
                    output[x] = Static143.nearestDistance;
                } else if (mode == 1) {
                    output[x] = Static417.secondNearestDistance;
                } else if (mode == 3) {
                    output[x] = Static109.thirdNearestDistance;
                } else if (mode == 4) {
                    output[x] = Static162.fourthNearestDistance;
                } else if (mode == 2) {
                    output[x] = Static417.secondNearestDistance - Static143.nearestDistance;
                }
            }
        }
        return output;
    }
}

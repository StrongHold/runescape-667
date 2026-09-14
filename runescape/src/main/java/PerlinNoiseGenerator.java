import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.util.Random;

/**
 * Perlin's improved gradient noise in three dimensions, where the third dimension is the animation frame. The
 * permutation table is doubled so a lattice index and a hash can be added without wrapping, the eight gradients are
 * the corners of a cube, and the interpolant is the quintic fade curve.
 */
@OriginalClass("client!ce")
public final class PerlinNoiseGenerator extends NoiseGenerator {

    private static final int WIDTH = 128;

    private static final int HEIGHT = 128;

    private static final int DEPTH = 16;

    private static final int PERMUTATION_COUNT = 256;

    /**
     * Clamps a lattice period to the permutation table, so a hash and a lattice index always index the doubled table
     * in range.
     */
    private static final int PERMUTATION_MASK = 0xFF;

    /**
     * Selects one of the eight gradient vectors in {@link Static558#aFloatArrayArray2}.
     */
    private static final int GRADIENT_MASK = 0x7;

    @OriginalMember(owner = "client!ce", name = "i", descriptor = "[I")
    public final int[] permutations = new int[PERMUTATION_COUNT * 2];

    @OriginalMember(owner = "client!ce", name = "<init>", descriptor = "(I)V")
    public PerlinNoiseGenerator(@OriginalArg(0) int seed) {
        @Pc(11) Random random = new Random(seed);
        for (@Pc(13) int i = 0; i < PERMUTATION_COUNT; i++) {
            this.permutations[i] = this.permutations[i + PERMUTATION_COUNT] = i;
        }
        for (@Pc(34) int i = 0; i < PERMUTATION_COUNT; i++) {
            @Pc(40) int swap = random.nextInt() & PERMUTATION_MASK;
            @Pc(45) int value = this.permutations[swap];
            this.permutations[swap] = this.permutations[swap + PERMUTATION_COUNT] = this.permutations[i];
            this.permutations[i] = this.permutations[i + PERMUTATION_COUNT] = value;
        }
    }

    @OriginalMember(owner = "client!ce", name = "a", descriptor = "(FFI[FIFIFIBI)V")
    @Override
    public void generate(@OriginalArg(0) float yStep, @OriginalArg(1) float amplitude, @OriginalArg(3) float[] dest, @OriginalArg(4) int offset, @OriginalArg(5) float zStep, @OriginalArg(6) int frame, @OriginalArg(7) float xStep) {
        @Pc(13) int xWrap = (int) (xStep * (float) WIDTH - 1.0F);
        @Pc(17) int xMask = xWrap & PERMUTATION_MASK;
        @Pc(33) int yWrap = (int) ((float) HEIGHT * yStep - 1.0F);
        @Pc(37) int yMask = yWrap & PERMUTATION_MASK;
        @Pc(45) int zWrap = (int) ((float) DEPTH * zStep - 1.0F);
        @Pc(49) int zMask = zWrap & PERMUTATION_MASK;
        @Pc(54) float z = (float) frame * zStep;
        @Pc(57) int z0 = (int) z;
        @Pc(61) int z1 = z0 + 1;
        @Pc(66) float zFrac = z - (float) z0;
        @Pc(71) float zFracInv = 1.0F - zFrac;
        @Pc(75) int zIndex0 = z0 & zMask;
        @Pc(79) float zFade = Static272.method3936(zFrac);
        @Pc(83) int zIndex1 = z1 & zMask;
        @Pc(88) int z0Hash = this.permutations[zIndex0];
        @Pc(93) int z1Hash = this.permutations[zIndex1];
        for (@Pc(95) int y = 0; y < HEIGHT; y++) {
            @Pc(102) float yCoord = yStep * (float) y;
            @Pc(105) int y0 = (int) yCoord;
            @Pc(109) int y1 = y0 + 1;
            @Pc(114) float yFrac = yCoord - (float) y0;
            @Pc(118) float yFracInv = 1.0F - yFrac;
            @Pc(122) float yFade = Static272.method3936(yFrac);
            @Pc(126) int yIndex0 = y0 & yMask;
            @Pc(130) int yIndex1 = y1 & yMask;
            @Pc(137) int z0y0Hash = this.permutations[z0Hash + yIndex0];
            @Pc(144) int z0y1Hash = this.permutations[z0Hash + yIndex1];
            @Pc(152) int z1y0Hash = this.permutations[yIndex0 + z1Hash];
            @Pc(160) int z1y1Hash = this.permutations[yIndex1 + z1Hash];
            for (@Pc(162) int x = 0; x < WIDTH; x++) {
                @Pc(169) float xCoord = (float) x * xStep;
                @Pc(172) int x0 = (int) xCoord;
                @Pc(176) int x1 = x0 + 1;
                @Pc(182) float xFrac = (float) -x0 + xCoord;
                @Pc(186) float xFracInv = 1.0F - xFrac;
                @Pc(190) int xIndex1 = x1 & xMask;
                @Pc(194) int xIndex0 = x0 & xMask;
                @Pc(198) float xFade = Static272.method3936(xFrac);
                dest[offset++] = amplitude * Static226.method7999(Static226.method7999(Static226.method7999(Static282.method3978(zFrac, xFrac, yFrac, this.permutations[xIndex1 + z1y1Hash] & GRADIENT_MASK), Static282.method3978(zFrac, xFracInv, yFrac, this.permutations[xIndex0 + z1y1Hash] & GRADIENT_MASK), xFade), Static226.method7999(Static282.method3978(zFrac, xFrac, yFracInv, this.permutations[z1y0Hash + xIndex1] & GRADIENT_MASK), Static282.method3978(zFrac, xFracInv, yFracInv, this.permutations[xIndex0 + z1y0Hash] & GRADIENT_MASK), xFade), yFade), Static226.method7999(Static226.method7999(Static282.method3978(zFracInv, xFrac, yFrac, this.permutations[z0y1Hash + xIndex1] & GRADIENT_MASK), Static282.method3978(zFracInv, xFracInv, yFrac, this.permutations[z0y1Hash + xIndex0] & GRADIENT_MASK), xFade), Static226.method7999(Static282.method3978(zFracInv, xFrac, yFracInv, this.permutations[z0y0Hash + xIndex1] & GRADIENT_MASK), Static282.method3978(zFracInv, xFracInv, yFracInv, this.permutations[xIndex0 + z0y0Hash] & GRADIENT_MASK), xFade), yFade), zFade);
            }
        }
    }
}

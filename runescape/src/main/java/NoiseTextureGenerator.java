import com.jagex.graphics.texture.Node_Sub1_Sub27;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.util.Random;

/**
 * Bakes a three dimensional Perlin noise field into a 128 by 128 texture with 16 animation frames. The third noise
 * axis is the frame index, so the lattice wraps on all three axes and both the texture and the animation loop
 * without a seam. Arithmetic is fixed point with 12 fractional bits.
 *
 * <p>Subclasses decide how the octaves are combined and how the result is packed into texels, so the traversal
 * order is fixed here: frame, then row, then column, one texel emitted per column.
 */
@OriginalClass("client!lea")
public abstract class NoiseTextureGenerator {

    protected static final int WIDTH = 128;

    protected static final int HEIGHT = 128;

    protected static final int FRAME_COUNT = 16;

    protected static final int TEXEL_COUNT = WIDTH * HEIGHT * FRAME_COUNT;

    /**
     * Every subclass packs its output as GL_LUMINANCE_ALPHA, one byte of luminance followed by one of alpha.
     */
    protected static final int BYTES_PER_TEXEL = 2;

    protected static final int FRACTION_BITS = 12;

    protected static final int ONE = 1 << FRACTION_BITS;

    protected static final int FRACTION_TO_BYTE_SHIFT = FRACTION_BITS - 8;

    private static final int FRACTION_MASK = ONE - 1;

    private static final int LATTICE_SIZE = 256;

    private static final int LATTICE_MASK = LATTICE_SIZE - 1;

    /**
     * The permutation table is stored twice back to back so a lattice index plus a hashed index never needs a
     * second wrap.
     */
    private static final int PERMUTATION_SIZE = LATTICE_SIZE * 2;

    @OriginalMember(owner = "client!lea", name = "d", descriptor = "[S")
    public short[] octaveFrequencies;

    @OriginalMember(owner = "client!lea", name = "b", descriptor = "I")
    public int periodX = 4;

    @OriginalMember(owner = "client!lea", name = "a", descriptor = "[S")
    public final short[] permutations = new short[PERMUTATION_SIZE];

    @OriginalMember(owner = "client!lea", name = "n", descriptor = "I")
    protected int octaves = 4;

    @OriginalMember(owner = "client!lea", name = "e", descriptor = "I")
    public int periodY = 4;

    @OriginalMember(owner = "client!lea", name = "i", descriptor = "I")
    public int periodFrame = 4;

    @OriginalMember(owner = "client!lea", name = "k", descriptor = "I")
    public int seed = 0;

    @OriginalMember(owner = "client!lea", name = "<init>", descriptor = "(IIIII)V")
    protected NoiseTextureGenerator(@OriginalArg(0) int seed, @OriginalArg(1) int octaves, @OriginalArg(2) int periodX, @OriginalArg(3) int periodY, @OriginalArg(4) int periodFrame) {
        this.periodFrame = periodFrame;
        this.periodY = periodY;
        this.periodX = periodX;
        this.seed = seed;
        this.octaves = octaves;
        this.buildOctaveFrequencies();
        this.shufflePermutations();
    }

    @OriginalMember(owner = "client!lea", name = "a", descriptor = "(IBII)V")
    protected final void renderTexels() {
        @Pc(6) int[] xCoords = new int[WIDTH];
        @Pc(9) int[] yCoords = new int[HEIGHT];
        for (@Pc(11) int x = 0; x < WIDTH; x++) {
            xCoords[x] = (x << FRACTION_BITS) / WIDTH;
        }
        @Pc(27) int[] frameCoords = new int[FRAME_COUNT];
        for (@Pc(29) int y = 0; y < HEIGHT; y++) {
            yCoords[y] = (y << FRACTION_BITS) / HEIGHT;
        }
        for (@Pc(48) int frame = 0; frame < FRAME_COUNT; frame++) {
            frameCoords[frame] = (frame << FRACTION_BITS) / FRAME_COUNT;
        }
        this.reset();
        for (@Pc(74) int frame = 0; frame < FRAME_COUNT; frame++) {
            for (@Pc(77) int y = 0; y < HEIGHT; y++) {
                for (@Pc(80) int x = 0; x < WIDTH; x++) {
                    for (@Pc(83) int octave = 0; octave < this.octaves; octave++) {
                        @Pc(91) int frequency = this.octaveFrequencies[octave] << FRACTION_BITS;
                        @Pc(99) int frameFraction = frameCoords[frame] * frequency >> FRACTION_BITS;
                        @Pc(106) int yLimit = this.periodY * frequency >> FRACTION_BITS;
                        @Pc(114) int ySample = yCoords[y] * frequency >> FRACTION_BITS;
                        @Pc(122) int xFraction = frequency * xCoords[x] >> FRACTION_BITS;
                        @Pc(129) int xLimit = frequency * this.periodX >> FRACTION_BITS;
                        @Pc(136) int frameLimit = this.periodFrame * frequency >> FRACTION_BITS;
                        @Pc(141) int frameScaled = frameFraction * this.periodFrame;
                        @Pc(146) int yScaled = ySample * this.periodY;
                        @Pc(151) int xScaled = xFraction * this.periodX;
                        @Pc(155) int x0 = xScaled >> FRACTION_BITS;
                        @Pc(159) int x1 = x0 + 1;
                        @Pc(163) int y0 = yScaled >> FRACTION_BITS;
                        @Pc(167) int x0Wrapped = x0 & LATTICE_MASK;
                        @Pc(171) int y1 = y0 + 1;
                        @Pc(175) int frame0 = frameScaled >> FRACTION_BITS;
                        @Pc(179) int y0Wrapped = y0 & LATTICE_MASK;
                        @Pc(183) int frame1 = frame0 + 1;
                        @Pc(187) int yFraction = yScaled & FRACTION_MASK;
                        if (xLimit <= x1) {
                            x1 = 0;
                        } else {
                            x1 &= LATTICE_MASK;
                        }
                        xFraction = xScaled & FRACTION_MASK;
                        if (frame1 >= frameLimit) {
                            frame1 = 0;
                        } else {
                            frame1 &= LATTICE_MASK;
                        }
                        frameFraction = frameScaled & FRACTION_MASK;
                        if (yLimit <= y1) {
                            y1 = 0;
                        } else {
                            y1 &= LATTICE_MASK;
                        }
                        frame0 &= LATTICE_MASK;
                        @Pc(237) int xFade = Node_Sub1_Sub27.anIntArray768[xFraction];
                        @Pc(241) int yFade = Node_Sub1_Sub27.anIntArray768[yFraction];
                        @Pc(245) int frameFade = Node_Sub1_Sub27.anIntArray768[frameFraction];
                        @Pc(250) short frame1Hash = this.permutations[frame1];
                        @Pc(255) short frame0Hash = this.permutations[frame0];
                        @Pc(259) int yFractionMinusOne = yFraction - ONE;
                        @Pc(263) int xFractionMinusOne = xFraction - ONE;
                        @Pc(267) int frameFractionMinusOne = frameFraction - ONE;
                        @Pc(274) short frame0Y1Hash = this.permutations[y1 + frame0Hash];
                        @Pc(281) short frame1Y0Hash = this.permutations[frame1Hash + y0Wrapped];
                        @Pc(288) short frame1Y1Hash = this.permutations[frame1Hash + y1];
                        @Pc(295) short frame0Y0Hash = this.permutations[frame0Hash + y0Wrapped];
                        @Pc(307) int gradX0Y0F0 = Static487.method6515(frameFraction, this.permutations[x0Wrapped + frame0Y0Hash], xFraction, yFraction);
                        @Pc(319) int gradX1Y0F0 = Static487.method6515(frameFraction, this.permutations[frame0Y0Hash + x1], xFractionMinusOne, yFraction);
                        @Pc(329) int blendY0F0 = ((gradX1Y0F0 - gradX0Y0F0) * xFade >> FRACTION_BITS) + gradX0Y0F0;
                        @Pc(341) int gradX0Y1F0 = Static487.method6515(frameFraction, this.permutations[x0Wrapped + frame0Y1Hash], xFraction, yFractionMinusOne);
                        @Pc(353) int gradX1Y1F0 = Static487.method6515(frameFraction, this.permutations[x1 + frame0Y1Hash], xFractionMinusOne, yFractionMinusOne);
                        @Pc(364) int blendY1F0 = gradX0Y1F0 + (xFade * (gradX1Y1F0 - gradX0Y1F0) >> FRACTION_BITS);
                        @Pc(377) int gradX0Y0F1 = Static487.method6515(frameFractionMinusOne, this.permutations[x0Wrapped + frame1Y0Hash], xFraction, yFraction);
                        @Pc(388) int blendF0 = (yFade * (blendY1F0 - blendY0F0) >> FRACTION_BITS) + blendY0F0;
                        @Pc(400) int gradX1Y0F1 = Static487.method6515(frameFractionMinusOne, this.permutations[frame1Y0Hash + x1], xFractionMinusOne, yFraction);
                        @Pc(411) int blendY0F1 = gradX0Y0F1 + (xFade * (gradX1Y0F1 - gradX0Y0F1) >> FRACTION_BITS);
                        @Pc(424) int gradX0Y1F1 = Static487.method6515(frameFractionMinusOne, this.permutations[x0Wrapped + frame1Y1Hash], xFraction, yFractionMinusOne);
                        @Pc(436) int gradX1Y1F1 = Static487.method6515(frameFractionMinusOne, this.permutations[x1 + frame1Y1Hash], xFractionMinusOne, yFractionMinusOne);
                        @Pc(447) int blendY1F1 = ((gradX1Y1F1 - gradX0Y1F1) * xFade >> FRACTION_BITS) + gradX0Y1F1;
                        @Pc(458) int blendF1 = ((blendY1F1 - blendY0F1) * yFade >> FRACTION_BITS) + blendY0F1;
                        this.accumulateOctave((frameFade * (blendF1 - blendF0) >> FRACTION_BITS) + blendF0, octave);
                    }
                    this.emitTexel();
                }
            }
        }
    }

    @OriginalMember(owner = "client!lea", name = "a", descriptor = "(B)V")
    public void buildOctaveFrequencies() {
        this.octaveFrequencies = new short[this.octaves];
        for (@Pc(18) int octave = 0; octave < this.octaves; octave++) {
            this.octaveFrequencies[octave] = (short) (int) Math.pow(2.0D, octave);
        }
    }

    @OriginalMember(owner = "client!lea", name = "b", descriptor = "(I)V")
    protected abstract void emitTexel();

    @OriginalMember(owner = "client!lea", name = "a", descriptor = "(III)V")
    protected abstract void accumulateOctave(@OriginalArg(0) int noise, @OriginalArg(1) int octave);

    @OriginalMember(owner = "client!lea", name = "b", descriptor = "(B)V")
    public void shufflePermutations() {
        @Pc(10) Random random = new Random(this.seed);
        for (@Pc(12) int index = 0; index < LATTICE_SIZE - 1; index++) {
            this.permutations[index] = (short) index;
        }
        for (@Pc(25) int index = 0; index < LATTICE_SIZE - 1; index++) {
            @Pc(37) int last = LATTICE_SIZE - 1 - index;
            @Pc(42) int pick = Node_Sub1_Sub27.method8326(-5208, last, random);
            @Pc(47) short picked = this.permutations[pick];
            this.permutations[pick] = this.permutations[last];
            this.permutations[last] = this.permutations[last + LATTICE_SIZE] = picked;
        }
    }

    @OriginalMember(owner = "client!lea", name = "a", descriptor = "(I)V")
    protected abstract void reset();
}

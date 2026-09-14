import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Sums the octaves as fractional Brownian motion: each octave contributes at a geometrically decreasing amplitude
 * given by the persistence raised to the octave index. The signed total is folded with an absolute value before it
 * is written, which turns the zero crossings into the creases of a turbulence pattern.
 */
@OriginalClass("client!uw")
public class FractalNoiseTextureGenerator extends NoiseTextureGenerator {

    @OriginalMember(owner = "client!uw", name = "r", descriptor = "[B")
    public byte[] greyscaleTexels;

    @OriginalMember(owner = "client!uw", name = "v", descriptor = "I")
    public int texelIndex;

    @OriginalMember(owner = "client!uw", name = "t", descriptor = "I")
    public int total;

    @OriginalMember(owner = "client!uw", name = "p", descriptor = "[I")
    public final int[] octaveAmplitudes = new int[this.octaves];

    @OriginalMember(owner = "client!uw", name = "<init>", descriptor = "(IIIIIF)V")
    protected FractalNoiseTextureGenerator(@OriginalArg(0) int seed, @OriginalArg(1) int octaves, @OriginalArg(2) int periodX, @OriginalArg(3) int periodY, @OriginalArg(4) int periodFrame, @OriginalArg(5) float persistence) {
        super(seed, octaves, periodX, periodY, periodFrame);
        for (@Pc(13) int octave = 0; super.octaves > octave; octave++) {
            this.octaveAmplitudes[octave] = (short) (int) (Math.pow(persistence, octave) * (double) ONE);
        }
    }

    @OriginalMember(owner = "client!uw", name = "b", descriptor = "(I)V")
    @Override
    protected final void emitTexel() {
        this.total = Math.abs(this.total);
        if (this.total >= ONE) {
            this.total = ONE - 1;
        }
        this.writeTexel(this.texelIndex++, (byte) (this.total >> FRACTION_TO_BYTE_SHIFT));
        this.total = 0;
    }

    @OriginalMember(owner = "client!uw", name = "a", descriptor = "(I)V")
    @Override
    protected final void reset() {
        this.total = 0;
        this.texelIndex = 0;
    }

    @OriginalMember(owner = "client!uw", name = "a", descriptor = "(III)V")
    @Override
    protected final void accumulateOctave(@OriginalArg(0) int noise, @OriginalArg(1) int octave) {
        this.total += this.octaveAmplitudes[octave] * noise >> FRACTION_BITS;
    }

    /**
     * Writes one byte per texel. Every subclass overrides this to expand the value into its own texel format, so
     * the greyscale buffer only serves as the fallback sink.
     */
    @OriginalMember(owner = "client!uw", name = "a", descriptor = "(BIB)V")
    protected void writeTexel(@OriginalArg(1) int index, @OriginalArg(2) byte value) {
        this.greyscaleTexels[this.texelIndex++] = (byte) ((value >> 1 & 0x7F) + 127);
    }
}

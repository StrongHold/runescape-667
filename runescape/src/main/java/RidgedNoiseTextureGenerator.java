import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

/**
 * Sums the octaves as a ridged multifractal. Each octave is inverted about an offset, squared to sharpen the
 * crease into a ridge, and then multiplied by a weight carried over from the previous octave, so detail only
 * survives where the coarser octave already had a ridge. The amplitude falls off geometrically across the octaves.
 */
@OriginalClass("client!hga")
public class RidgedNoiseTextureGenerator extends NoiseTextureGenerator {

    private static final int MAX_LUMINANCE = 255;

    @OriginalMember(owner = "client!hga", name = "o", descriptor = "I")
    public int total;

    @OriginalMember(owner = "client!hga", name = "D", descriptor = "I")
    public int texelIndex;

    @OriginalMember(owner = "client!hga", name = "w", descriptor = "[B")
    public byte[] greyscaleTexels;

    @OriginalMember(owner = "client!hga", name = "z", descriptor = "I")
    public int weight;

    @OriginalMember(owner = "client!hga", name = "p", descriptor = "I")
    public int signal;

    @OriginalMember(owner = "client!hga", name = "B", descriptor = "I")
    public final int offset;

    @OriginalMember(owner = "client!hga", name = "E", descriptor = "I")
    public final int gain;

    @OriginalMember(owner = "client!hga", name = "x", descriptor = "I")
    public final int amplitudeStep;

    @OriginalMember(owner = "client!hga", name = "t", descriptor = "I")
    public int amplitude;

    @OriginalMember(owner = "client!hga", name = "<init>", descriptor = "(IIIIIFFF)V")
    protected RidgedNoiseTextureGenerator(@OriginalArg(0) int seed, @OriginalArg(1) int octaves, @OriginalArg(2) int periodX, @OriginalArg(3) int periodY, @OriginalArg(4) int periodFrame, @OriginalArg(5) float roughness, @OriginalArg(6) float offset, @OriginalArg(7) float gain) {
        super(seed, octaves, periodX, periodY, periodFrame);
        this.offset = (int) (offset * (float) ONE);
        this.gain = (int) (gain * (float) ONE);
        this.amplitude = this.amplitudeStep = (int) (Math.pow(0.5D, -roughness) * (double) ONE);
    }

    /**
     * Writes one byte per texel. Every subclass overrides this to expand the value into its own texel format, so
     * the greyscale buffer only serves as the fallback sink.
     */
    @OriginalMember(owner = "client!hga", name = "a", descriptor = "(IBB)V")
    protected void writeTexel(@OriginalArg(0) int index, @OriginalArg(1) byte value) {
        this.greyscaleTexels[index] = value;
    }

    @OriginalMember(owner = "client!hga", name = "a", descriptor = "(III)V")
    @Override
    protected final void accumulateOctave(@OriginalArg(0) int noise, @OriginalArg(1) int octave) {
        if (octave == 0) {
            this.signal = this.offset - (noise >= 0 ? noise : -noise);
            this.weight = ONE;
            this.signal = this.signal * this.signal >> FRACTION_BITS;
            this.total = this.signal;
        } else {
            this.weight = this.signal * this.gain >> FRACTION_BITS;
            this.signal = this.offset - (noise < 0 ? -noise : noise);
            if (this.weight < 0) {
                this.weight = 0;
            } else if (this.weight > ONE) {
                this.weight = ONE;
            }
            this.signal = this.signal * this.signal >> FRACTION_BITS;
            this.signal = this.signal * this.weight >> FRACTION_BITS;
            this.total += this.amplitude * this.signal >> FRACTION_BITS;
            this.amplitude = this.amplitudeStep * this.amplitude >> FRACTION_BITS;
        }
    }

    @OriginalMember(owner = "client!hga", name = "b", descriptor = "(I)V")
    @Override
    protected final void emitTexel() {
        this.amplitude = this.amplitudeStep;
        this.total >>= FRACTION_TO_BYTE_SHIFT;
        if (this.total < 0) {
            this.total = 0;
        } else if (this.total > MAX_LUMINANCE) {
            this.total = MAX_LUMINANCE;
        }
        this.writeTexel(this.texelIndex++, (byte) this.total);
        this.total = 0;
    }

    @OriginalMember(owner = "client!hga", name = "a", descriptor = "(I)V")
    @Override
    protected final void reset() {
        this.total = 0;
        this.texelIndex = 0;
    }
}

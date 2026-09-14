import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * The rippling water pattern for the native toolkit. Luminance is left fully lit and the ridges are carried in
 * alpha alone, so the pattern blends over the water colour instead of tinting it.
 */
@OriginalClass("client!kba")
public final class NativeRippleNoiseTexture extends RidgedNoiseTextureGenerator {

    private static final byte FULL_LUMINANCE = (byte) 0xFF;

    @OriginalMember(owner = "client!kba", name = "M", descriptor = "[B")
    public byte[] texels;

    @OriginalMember(owner = "client!kba", name = "<init>", descriptor = "()V")
    public NativeRippleNoiseTexture() {
        super(8, 5, 8, 8, 2, 0.1F, 0.55F, 3.0F);
    }

    @OriginalMember(owner = "client!kba", name = "a", descriptor = "(IBB)V")
    @Override
    protected void writeTexel(@OriginalArg(0) int index, @OriginalArg(1) byte value) {
        @Pc(7) int offset = index * BYTES_PER_TEXEL;
        @Pc(10) int luminanceIndex = offset;
        @Pc(11) int alphaIndex = offset + 1;
        this.texels[luminanceIndex] = FULL_LUMINANCE;
        @Pc(22) int noise = value & 0xFF;
        this.texels[alphaIndex] = (byte) (noise * 3 >> 5);
    }

    @OriginalMember(owner = "client!kba", name = "a", descriptor = "(IIII)[B")
    public byte[] generate() {
        this.texels = new byte[TEXEL_COUNT * BYTES_PER_TEXEL];
        this.renderTexels();
        return this.texels;
    }
}

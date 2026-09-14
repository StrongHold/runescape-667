import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * The rippling water pattern for the OpenGL toolkit. The attenuated ridges go into both the luminance and the
 * alpha channel, so the pattern darkens the water as well as fading it.
 */
@OriginalClass("client!nja")
public final class GlRippleNoiseTexture extends RidgedNoiseTextureGenerator {

    @OriginalMember(owner = "client!nja", name = "G", descriptor = "[B")
    public byte[] texels;

    @OriginalMember(owner = "client!nja", name = "<init>", descriptor = "()V")
    public GlRippleNoiseTexture() {
        super(8, 5, 8, 8, 2, 0.1F, 0.55F, 3.0F);
    }

    @OriginalMember(owner = "client!nja", name = "a", descriptor = "(IIII)[B")
    public byte[] generate() {
        this.texels = new byte[TEXEL_COUNT * BYTES_PER_TEXEL];
        this.renderTexels();
        return this.texels;
    }

    @OriginalMember(owner = "client!nja", name = "a", descriptor = "(IBB)V")
    @Override
    protected void writeTexel(@OriginalArg(0) int index, @OriginalArg(1) byte value) {
        @Pc(7) int offset = index * BYTES_PER_TEXEL;
        @Pc(11) int noise = value & 0xFF;
        @Pc(21) int luminanceIndex = offset;
        @Pc(22) int alphaIndex = offset + 1;
        this.texels[luminanceIndex] = (byte) (noise * 3 >> 5);
        this.texels[alphaIndex] = (byte) (noise * 3 >> 5);
    }
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * The flowing water pattern for the OpenGL toolkit. The same noise is written to both the luminance and the alpha
 * channel, so the texture reads the same whichever channel a fixed function texture environment samples.
 */
@OriginalClass("client!bda")
public final class GlFlowNoiseTexture extends FractalNoiseTextureGenerator {

    @OriginalMember(owner = "client!bda", name = "G", descriptor = "[B")
    public byte[] texels;

    @OriginalMember(owner = "client!bda", name = "<init>", descriptor = "()V")
    public GlFlowNoiseTexture() {
        super(12, 5, 16, 2, 2, 0.45F);
    }

    @OriginalMember(owner = "client!bda", name = "a", descriptor = "(BIB)V")
    @Override
    protected void writeTexel(@OriginalArg(1) int index, @OriginalArg(2) byte value) {
        @Pc(20) byte luminance = (byte) ((value >> 1 & 0x7F) + 127);
        @Pc(24) int offset = index * BYTES_PER_TEXEL;
        @Pc(27) int luminanceIndex = offset;
        @Pc(28) int alphaIndex = offset + 1;
        this.texels[luminanceIndex] = luminance;
        this.texels[alphaIndex] = luminance;
    }

    @OriginalMember(owner = "client!bda", name = "a", descriptor = "(IIII)[B")
    public byte[] generate() {
        this.texels = new byte[TEXEL_COUNT * BYTES_PER_TEXEL];
        this.renderTexels();
        return this.texels;
    }
}

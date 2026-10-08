import com.jagex.graphics.TextureMetrics;
import com.jagex.math.ColourUtils;

/**
 * The colour the GL toolkit gives a vertex before its lights fall on it, which is what it hands
 * the hardware to modulate the texel by.
 *
 * <p>An untextured vertex is its HSL colour with the lightness scaled by the light on it, 128
 * being full, held between 2 and 126, and taken through the palette, whose gamma comes after the
 * scaling ({@code Static468.shadeHsl}, {@code GlModel.shadeRgba}, and the same arithmetic in
 * {@code GlGround}). A model's light is its ambient; the ground's is 74 less the shadow on the
 * corner.
 *
 * <p>A textured vertex starts from that and is pulled towards a grey of the light alone by the
 * texture's alpha, out of 256, so that most textures show their own colour, and is then
 * brightened by the texture's brightness, out of 256 over one ({@code GlGroundLayer.setVertexColour},
 * {@code GlModel.shadeRgba}).
 */
public final class GlTint {

    /** The grey a light level makes: two steps of each channel a step of light, so 127 is near white. */
    private static final int GREY_PER_LIGHT = 0x020202;
    private static final int FULL_ALPHA = 256;

    public static int untextured(int hsl, int light) {
        return ColourUtils.HSL_TO_RGB[Static468.shadeHsl(hsl, light)];
    }

    public static int textured(int untextured, int light, TextureMetrics metrics) {
        var rgb = untextured;
        var alpha = metrics.alpha & 0xFF;
        if (alpha != 0) {
            var grey = light < 0 ? 0 : light <= 127 ? light * GREY_PER_LIGHT : 0xFFFFFF;
            rgb = alpha == FULL_ALPHA ? grey : lerp(rgb, grey, alpha);
        }
        var brightness = metrics.brightness & 0xFF;
        if (brightness != 0) {
            var scale = brightness + 256;
            rgb = channel(rgb >> 16, scale) << 16 | channel(rgb >> 8, scale) << 8 | channel(rgb, scale);
        }
        return rgb;
    }

    private static int lerp(int from, int to, int weight) {
        var rest = 256 - weight;
        return ((from & 0xFF00FF) * rest + (to & 0xFF00FF) * weight & 0xFF00FF00
            | (from & 0xFF00) * rest + (to & 0xFF00) * weight & 0xFF0000) >>> 8;
    }

    private static int channel(int value, int scale) {
        return Math.min(65535, (value & 0xFF) * scale) >> 8;
    }

    private GlTint() {
        /* empty */
    }
}

/**
 * Converts a colour channel the client draws to the screen into the linear light glTF expects of
 * a vertex colour.
 *
 * The client's palette and textures hold values that go to the screen as they are, which is what
 * sRGB describes. glTF multiplies vertex colours into lighting, which is done in linear light, so
 * a channel is taken through the sRGB transfer function on the way in.
 */
public final class Srgb {

    private static final float[] LINEAR = new float[256];

    static {
        for (var i = 0; i < LINEAR.length; i++) {
            var encoded = i / 255.0;
            LINEAR[i] = (float) (encoded <= 0.04045
                ? encoded / 12.92
                : Math.pow((encoded + 0.055) / 1.055, 2.4));
        }
    }

    /**
     * A channel of 0 to 255 in sRGB, as a linear value of 0 to 1.
     */
    public static float toLinear(int channel) {
        return LINEAR[channel];
    }

    private Srgb() {
        /* empty */
    }
}

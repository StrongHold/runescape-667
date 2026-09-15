/**
 * The test sprite every scene draws.
 *
 * It is neither square nor symmetrical on purpose. A square sprite cannot say which of the
 * toolkit's arguments it read as width and which as height, and a symmetrical one cannot say
 * which way round it drew either axis. Red rises to the right and green rises downwards, so an
 * axis drawn the wrong way round shows as a fall.
 */
public record GradientSprite(int width, int height) {

    public static final GradientSprite INSTANCE = new GradientSprite(96, 64);

    public int[] pixels() {
        var pixels = new int[width * height];

        for (var i = 0; i < pixels.length; i++) {
            var x = i % width;
            var y = i / width;
            pixels[i] = 0xFF000000 | (x * 2 << 16) | (y * 3 << 8) | 0x80;
        }

        return pixels;
    }
}

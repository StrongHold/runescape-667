import com.jagex.IndexedImage;

/**
 * A picture held the way the client holds its artwork: one byte per pixel standing for one of a
 * table of colours, with room cut off each side.
 *
 * The client's own pictures come out of the cache, and a check that needs one cannot depend on a
 * cache being there. This builds one with everything a check needs to tell a right answer from a
 * nearly right one: the byte nought in the middle of the picture rather than only at its edge, a
 * table whose first colour is not black, and room cut off all four sides by different amounts.
 */
public final class IndexedGlyph {

    private static final int WIDTH = 24;
    private static final int HEIGHT = 18;
    private static final int COLOURS = 16;

    /**
     * Without an alpha, the byte nought means nothing is there. With one, nought is a colour like
     * any other and the alpha decides on its own, so both are worth drawing.
     */
    public static IndexedImage solid() {
        return build(false);
    }

    public static IndexedImage translucent() {
        return build(true);
    }

    private static IndexedImage build(boolean withAlpha) {
        var image = new IndexedImage();
        image.width = WIDTH;
        image.height = HEIGHT;
        image.offX1 = 3;
        image.offY1 = 5;
        image.offX2 = 7;
        image.offY2 = 2;

        image.palette = new int[COLOURS];
        for (var colour = 0; colour < COLOURS; colour++) {
            image.palette[colour] = 0x112233 + colour * 0x0A1408;
        }

        image.raster = new byte[WIDTH * HEIGHT];
        for (var y = 0; y < HEIGHT; y++) {
            for (var x = 0; x < WIDTH; x++) {
                var hole = x > 8 && x < 14 && y > 6 && y < 11;
                image.raster[y * WIDTH + x] = (byte) (hole ? 0 : (x + y) % COLOURS);
            }
        }

        if (withAlpha) {
            image.alpha = new byte[WIDTH * HEIGHT];
            for (var i = 0; i < image.alpha.length; i++) {
                image.alpha[i] = (byte) (i * 7 % 256);
            }
        }

        return image;
    }

    private IndexedGlyph() {
        /* empty */
    }
}

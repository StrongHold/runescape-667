import com.jagex.IndexedImage;
import com.jagex.graphics.FontMetrics;

/**
 * A font built here rather than read out of the cache.
 *
 * A font is a set of letters with their own sizes and their own place against the line, plus a
 * table of how far the pen moves after each one. Every one of those can be read from the wrong
 * array without the text looking obviously broken, so the letters here are deliberately of
 * different sizes, sit at different heights, and have ink at their corners: a letter drawn one
 * pixel out, or measured by another letter's width, shows up.
 */
public final class HandFont {

    /** The letters the client can ask for. A letter is found by its own character code. */
    private static final int LETTERS = 128;

    private static final int COLOURS = 8;

    /** How the client's metrics file starts: a nought, then whether the widths vary. */
    private static final int FIXED_WIDTH = 0;

    private static final int LINE_HEIGHT = 14;
    private static final int PADDING_TOP = 2;
    private static final int PADDING_BOTTOM = 3;

    public static FontMetrics metrics() {
        var written = new byte[1 + 1 + 256 + 1 + 2 + 2];
        var at = 0;

        written[at++] = 0;
        written[at++] = FIXED_WIDTH;

        for (var letter = 0; letter < 256; letter++) {
            written[at++] = (byte) (widthOf(letter) + 1);
        }

        written[at++] = LINE_HEIGHT;
        written[at++] = 0;
        written[at++] = 0;
        written[at++] = PADDING_TOP;
        written[at++] = PADDING_BOTTOM;

        return new FontMetrics(written);
    }

    public static IndexedImage[] letters() {
        var letters = new IndexedImage[LETTERS];
        for (var letter = 0; letter < LETTERS; letter++) {
            letters[letter] = letterOf(letter);
        }
        return letters;
    }

    private static int widthOf(int letter) {
        return 4 + letter % 5;
    }

    private static int heightOf(int letter) {
        return 6 + letter % 4;
    }

    private static IndexedImage letterOf(int letter) {
        var image = new IndexedImage();
        image.width = widthOf(letter);
        image.height = heightOf(letter);
        image.offX1 = letter % 3;
        image.offY1 = letter % 4 - 1;
        image.offX2 = 1;
        image.offY2 = 0;

        image.palette = new int[COLOURS];
        for (var colour = 0; colour < COLOURS; colour++) {
            image.palette[colour] = 0x203050 + colour * 0x1A2C14;
        }

        image.raster = new byte[image.width * image.height];
        for (var y = 0; y < image.height; y++) {
            for (var x = 0; x < image.width; x++) {
                var edge = x == 0 || y == 0 || x == image.width - 1 || y == image.height - 1;
                var inside = (x + y + letter) % 3 == 0;
                image.raster[y * image.width + x] =
                    (byte) (edge || inside ? 1 + (x + y + letter) % (COLOURS - 1) : 0);
            }
        }

        return image;
    }

    private HandFont() {
        /* empty */
    }
}

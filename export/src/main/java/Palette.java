import com.jagex.math.ColourUtils;

/**
 * The client's colour tables, built the way the client builds them but with a fixed brightness.
 *
 * The client raises every channel to a power it picks at random between 0.685 and 0.715 each time
 * it starts, so two runs of the client never agree on a colour to within a few steps. An export
 * should come out the same every time, so the tables are filled here first with the middle of that
 * range. The client fills a table only while it is empty, so everything it works out from them
 * afterwards uses these.
 */
final class Palette {

    /** The middle of the range the client picks its brightness from. */
    private static final double BRIGHTNESS = 0.7;

    private static final int COLOURS = 65536;

    static void install() {
        ColourUtils.HSL_TO_RGB = hslToRgb();
        ColourUtils.HSV_TO_RGB = hsvToRgb();
    }

    private static int[] hslToRgb() {
        var table = new int[COLOURS];
        for (var colour = 0; colour < COLOURS; colour++) {
            var hue = (double) (colour >> 10 & 0x3F) / 64.0 + 0.0078125;
            var saturation = (double) (colour >> 7 & 0x7) / 8.0 + 0.0625;
            var lightness = (double) (colour & 0x7F) / 128.0;
            var red = lightness;
            var green = lightness;
            var blue = lightness;

            if (saturation != 0.0) {
                var high = lightness < 0.5
                    ? (saturation + 1.0) * lightness
                    : lightness + saturation - saturation * lightness;
                var low = lightness * 2.0 - high;
                var redHue = hue + 0.3333333333333333 > 1.0 ? hue + 0.3333333333333333 - 1.0 : hue + 0.3333333333333333;
                var blueHue = hue - 0.3333333333333333 < 0.0 ? hue - 0.3333333333333333 + 1.0 : hue - 0.3333333333333333;
                red = red(redHue, low, high);
                green = green(hue, low, high);
                blue = blue(blueHue, low, high);
            }

            table[colour] = ((int) (Math.pow(red, BRIGHTNESS) * 256.0) << 16)
                + ((int) (Math.pow(green, BRIGHTNESS) * 256.0) << 8)
                + (int) (Math.pow(blue, BRIGHTNESS) * 256.0);
        }
        return table;
    }

    /*
     * The three channels below are the same sum, each written in the order the client writes it,
     * because a different order can round to a different last digit and so to a different step.
     */

    private static double red(double hue, double low, double high) {
        if (hue * 6.0 < 1.0) {
            return hue * 6.0 * (high - low) + low;
        } else if (hue * 2.0 < 1.0) {
            return high;
        } else if (hue * 3.0 < 2.0) {
            return low + (high - low) * 6.0 * (0.6666666666666666 - hue);
        } else {
            return low;
        }
    }

    private static double green(double hue, double low, double high) {
        if (hue * 6.0 < 1.0) {
            return low + hue * 6.0 * (high - low);
        } else if (hue * 2.0 < 1.0) {
            return high;
        } else if (hue * 3.0 < 2.0) {
            return (high - low) * (-hue + 0.6666666666666666) * 6.0 + low;
        } else {
            return low;
        }
    }

    private static double blue(double hue, double low, double high) {
        if (hue * 6.0 < 1.0) {
            return low + (high - low) * 6.0 * hue;
        } else if (hue * 2.0 < 1.0) {
            return high;
        } else if (hue * 3.0 < 2.0) {
            return (0.6666666666666666 - hue) * (-low + high) * 6.0 + low;
        } else {
            return low;
        }
    }

    private static int[] hsvToRgb() {
        var table = new int[COLOURS];
        var at = 0;
        for (var hueAndSaturation = 0; hueAndSaturation < 512; hueAndSaturation++) {
            var hue = ((float) (hueAndSaturation >> 3) / 64.0F + 0.0078125F) * 360.0F;
            var saturation = (float) (hueAndSaturation & 0x7) / 8.0F + 0.0625F;

            for (var step = 0; step < 128; step++) {
                var value = (float) step / 128.0F;
                var sector = hue / 60.0F;
                var whole = (int) sector;
                var part = sector - (float) whole;
                var p = value * (1.0F - saturation);
                var q = value * (1.0F - part * saturation);
                var t = (1.0F - saturation * (1.0F - part)) * value;

                float[] rgb = switch (whole % 6) {
                    case 0 -> new float[] {value, t, p};
                    case 1 -> new float[] {q, value, p};
                    case 2 -> new float[] {p, value, t};
                    case 3 -> new float[] {p, q, value};
                    case 4 -> new float[] {t, p, value};
                    case 5 -> new float[] {value, p, q};
                    default -> new float[] {0.0F, 0.0F, 0.0F};
                };

                var red = (int) ((float) Math.pow(rgb[0], BRIGHTNESS) * 256.0F);
                var green = (int) ((float) Math.pow(rgb[1], BRIGHTNESS) * 256.0F);
                var blue = (int) ((float) Math.pow(rgb[2], BRIGHTNESS) * 256.0F);
                table[at++] = (green << 8) + (red << 16) + blue - 16777216;
            }
        }
        return table;
    }

    private Palette() {
        /* empty */
    }
}

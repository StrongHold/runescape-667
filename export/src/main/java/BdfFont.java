import com.jagex.IndexedImage;
import com.jagex.core.stringtools.general.Cp1252;
import com.jagex.graphics.FontMetrics;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * One of the client's fonts in the Glyph Bitmap Distribution Format (BDF 2.1), a text file that
 * holds every glyph's bitmap with the font's metrics.
 *
 * The client keeps a font as two halves under the same id. The sprite archive holds 256 glyph
 * images, one for each byte of code page 1252, and the font metrics archive holds the advance of
 * each glyph, the line spacing, the space above and below the baseline, and for some fonts a
 * table of how far to move each pair of glyphs together. Both halves are read with the client's
 * own readers, {@code IndexedImage.load} and {@code FontMetrics}.
 *
 * The client draws a glyph at its baseline less the line spacing, plus the offset the glyph image
 * carries ({@code Font.render} and {@code JavaMonoFont.fa}). BDF places a glyph's bitmap by the
 * offset of its bottom left corner from the pen on the baseline, upwards, so a glyph's vertical
 * offset is the line spacing less its own offset and its height.
 *
 * BDF holds one bit a pixel and no colour, and no kerning. Every font of this cache fits: no glyph
 * carries its own alpha, every glyph of a font has the one colour, and no font has a kerning
 * table. A font that does not fit is refused.
 */
public final class BdfFont {

    private static final int GLYPHS = 256;

    /**
     * The client never draws the space glyph, it only moves on by its advance
     * ({@code Font.render}).
     */
    private static final int SPACE = ' ';

    /**
     * The client turns a character it has no byte for into a question mark ({@code Cp1252.encode}).
     */
    private static final int QUESTION_MARK = '?';

    /**
     * BDF gives sizes in points at a resolution, and at 72 dots an inch a point is a pixel.
     */
    private static final int RESOLUTION = 72;

    /**
     * The scalable width is in thousandths of the point size.
     */
    private static final int SCALABLE_UNITS = 1000;

    private static final int BITS_A_BYTE = 8;

    private static final int RGB = 0xFFFFFF;

    /**
     * The font's BDF text.
     *
     * @param name what the font is called, its name where it is known and otherwise its id.
     */
    public static String of(String name, FontMetrics metrics, IndexedImage[] glyphs) {
        if (glyphs.length != GLYPHS) {
            throw new IllegalArgumentException(name + " has " + glyphs.length + " glyph images, not " + GLYPHS);
        }
        var characters = glyphs();
        refuseKerning(name, metrics, characters);
        var colour = colourOf(name, glyphs);

        var bitmaps = new ArrayList<String>();
        var least = new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE};
        var most = new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE};
        for (var character : characters) {
            var glyph = glyphs[character.code()];
            var drawn = character.code() != SPACE;
            var width = drawn ? glyph.width : 0;
            var height = drawn ? glyph.height : 0;
            var left = glyph.offX1;
            var bottom = metrics.verticalSpacing - glyph.offY1 - height;
            if (width > 0 && height > 0) {
                least[0] = Math.min(least[0], left);
                least[1] = Math.min(least[1], bottom);
                most[0] = Math.max(most[0], left + width);
                most[1] = Math.max(most[1], bottom + height);
            }
            var advance = metrics.glyphWidth(character.code());
            var out = new StringBuilder();
            out.append("STARTCHAR ").append(String.format("U+%04X", character.unicode())).append('\n');
            out.append("ENCODING ").append(character.unicode()).append('\n');
            out.append("SWIDTH ").append(Math.round(advance * (float) SCALABLE_UNITS / metrics.verticalSpacing)).append(" 0\n");
            out.append("DWIDTH ").append(advance).append(" 0\n");
            out.append("BBX ").append(width).append(' ').append(height).append(' ').append(left).append(' ').append(bottom).append('\n');
            out.append("BITMAP\n");
            for (var y = 0; y < height; y++) {
                out.append(row(glyph, y)).append('\n');
            }
            out.append("ENDCHAR\n");
            bitmaps.add(out.toString());
        }

        var out = new StringBuilder();
        out.append("STARTFONT 2.1\n");
        out.append("FONT ").append(name).append('\n');
        out.append("SIZE ").append(metrics.verticalSpacing).append(' ').append(RESOLUTION).append(' ').append(RESOLUTION).append('\n');
        out.append("FONTBOUNDINGBOX ").append(most[0] - least[0]).append(' ').append(most[1] - least[1])
            .append(' ').append(least[0]).append(' ').append(least[1]).append('\n');
        out.append("STARTPROPERTIES 4\n");
        out.append("FONT_ASCENT ").append(metrics.paddingTop).append('\n');
        out.append("FONT_DESCENT ").append(metrics.paddingBottom).append('\n');
        out.append("DEFAULT_CHAR ").append(QUESTION_MARK).append('\n');
        out.append("GLYPH_COLOUR ").append(colour).append('\n');
        out.append("ENDPROPERTIES\n");
        out.append("CHARS ").append(characters.size()).append('\n');
        bitmaps.forEach(out::append);
        out.append("ENDFONT\n");
        return out.toString();
    }

    /**
     * A glyph the client can be asked for: its byte in code page 1252, and the Unicode character
     * that the client turns into that byte ({@code Cp1252.encode}).
     */
    public record Glyph(int code, int unicode) {
        /* empty */
    }

    /**
     * Every glyph some character reaches, in the order of their bytes. The client turns any
     * character it has no byte for into a question mark, so byte 0 and the five bytes code page
     * 1252 leaves undefined are never drawn, and they are left out. The characters are tried from
     * the top down, so that the lowest character that reaches a byte is the one kept, and the
     * question mark is its own.
     */
    public static List<Glyph> glyphs() {
        var unicodes = new int[GLYPHS];
        for (int unicode = java.lang.Character.MAX_VALUE; unicode > 0; unicode--) {
            unicodes[Cp1252.encode((char) unicode) & 0xFF] = unicode;
        }
        var characters = new ArrayList<Glyph>();
        for (var code = 0; code < GLYPHS; code++) {
            if (unicodes[code] != 0) {
                characters.add(new Glyph(code, unicodes[code]));
            }
        }
        return characters;
    }

    /**
     * One row of a glyph's bitmap in hex, a bit a pixel from the left, padded to a whole byte. A
     * pixel is set where the client covers it: where the image is not 0.
     */
    private static String row(IndexedImage glyph, int y) {
        var bytes = new byte[(glyph.width + BITS_A_BYTE - 1) / BITS_A_BYTE];
        for (var x = 0; x < glyph.width; x++) {
            if (glyph.raster[x + y * glyph.width] != 0) {
                bytes[x / BITS_A_BYTE] |= (byte) (0x80 >> x % BITS_A_BYTE);
            }
        }
        return HexFormat.of().withUpperCase().formatHex(bytes);
    }

    /**
     * The one colour every glyph of the font shows, as a hex RGB number, which a component that asks
     * for the glyphs' own colours draws them in.
     */
    private static String colourOf(String name, IndexedImage[] glyphs) {
        var colour = -1;
        for (var glyph : glyphs) {
            if (glyph.alpha != null) {
                throw new IllegalStateException(name + " has a glyph with its own alpha, which BDF cannot hold");
            }
            for (var texel : glyph.raster) {
                var index = texel & 0xFF;
                if (index != 0) {
                    var shown = glyph.palette[index] & RGB;
                    if (colour != -1 && colour != shown) {
                        throw new IllegalStateException(name + " has glyphs of more than one colour, which BDF cannot hold");
                    }
                    colour = shown;
                }
            }
        }
        return String.format("\"%06X\"", Math.max(colour, 0));
    }

    private static void refuseKerning(String name, FontMetrics metrics, List<Glyph> characters) {
        for (var first : characters) {
            for (var second : characters) {
                if (metrics.glyphSpacing((char) second.code(), first.code()) != 0) {
                    throw new IllegalStateException(name + " has a kerning table, which BDF cannot hold");
                }
            }
        }
    }

    private BdfFont() {
        /* empty */
    }
}

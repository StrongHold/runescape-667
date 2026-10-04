import com.jagex.IndexedImage;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.TextureSource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Checks a written font by drawing text with it and with the client, and counting the texels
 * that differ.
 *
 * The written font is read back from its files as any BMFont reader would, and laid out by the
 * BMFont rules: each glyph at the pen plus its offsets, the pen moved on by its advance and by the
 * kerning of each pair. The client draws the same text with its own software fonts
 * ({@code JavaMonoFont}, {@code JavaMonoAlphaFont} and {@code JavaFont}), built from the cache by
 * the software toolkit as the client builds them, onto a surface of the same size. Both draw every
 * glyph a character reaches, and the text is drawn once in one colour and, for a font with no
 * alpha, once more in the glyphs' own colours, as a component may ask.
 */
public final class FontCheck {

    private static final int TEXT_COLOUR = 0xFFFFC020;
    private static final int NO_SHADOW = -1;
    private static final int MARGIN = 32;
    private static final int GLYPHS_A_LINE = 16;
    private static final String PANGRAM = "The quick brown fox jumps over the lazy dog. AVATAR Wavy, \"fly\"! T.J.'s 1234567890";

    private final JavaToolkit toolkit = new JavaToolkit((TextureSource) null);

    /**
     * How many texels of the text differ between the written font and the client's.
     */
    public int differences(Path descriptor, FontMetrics metrics, IndexedImage[] glyphs) throws IOException {
        var written = Written.read(descriptor);
        var lines = lines();
        var width = 2 * MARGIN + lines.stream().mapToInt(line -> line.size()).max().orElse(0) * 2 * maxAdvance(metrics);
        var height = 2 * MARGIN + lines.size() * 2 * Math.max(metrics.verticalSpacing, 1);

        var differ = compare(written, lines, toolkit.createFont(metrics, glyphs, true), width, height, false);
        if (!written.antialiased) {
            differ += compare(written, lines, toolkit.createFont(metrics, glyphs, false), width, height, true);
        }
        return differ;
    }

    private int compare(Written written, List<List<BmFont.Glyph>> lines, com.jagex.graphics.Font client, int width, int height, boolean ownColours) {
        var expected = new int[width * height];
        toolkit.swapSurface(toolkit.createOffscreenSurface(new JavaArgbSprite(toolkit, expected, width, height), null));

        var actual = new int[width * height];
        for (var line = 0; line < lines.size(); line++) {
            var top = MARGIN + line * 2 * written.lineHeight;
            client.render(clientText(lines.get(line)), MARGIN, top + written.base, NO_SHADOW, TEXT_COLOUR);
            written.draw(lines.get(line), MARGIN, top, actual, width, ownColours);
        }

        var differ = 0;
        for (var texel = 0; texel < expected.length; texel++) {
            differ += expected[texel] != actual[texel] ? 1 : 0;
        }
        return differ;
    }

    /**
     * Every glyph a character reaches, sixteen to a line, and a line of ordinary text in which
     * many pairs are kerned.
     */
    private static List<List<BmFont.Glyph>> lines() {
        var glyphs = BmFont.glyphs();
        var lines = new ArrayList<List<BmFont.Glyph>>();
        for (var from = 0; from < glyphs.size(); from += GLYPHS_A_LINE) {
            lines.add(glyphs.subList(from, Math.min(from + GLYPHS_A_LINE, glyphs.size())));
        }
        var byUnicode = new HashMap<Integer, BmFont.Glyph>();
        glyphs.forEach(glyph -> byUnicode.put(glyph.unicode(), glyph));
        lines.add(PANGRAM.chars().mapToObj(byUnicode::get).toList());
        return lines;
    }

    /**
     * The text as the client is given it. The client takes a {@code <} as the start of a tag, so
     * the two angle brackets are written as the tags that stand for them.
     */
    private static String clientText(List<BmFont.Glyph> glyphs) {
        var text = new StringBuilder();
        for (var glyph : glyphs) {
            switch (glyph.unicode()) {
                case '<' -> text.append("<lt>");
                case '>' -> text.append("<gt>");
                default -> text.append((char) glyph.unicode());
            }
        }
        return text.toString();
    }

    private static int maxAdvance(FontMetrics metrics) {
        var max = 1;
        for (var code = 0; code < 256; code++) {
            max = Math.max(max, metrics.glyphWidth(code));
        }
        return max;
    }

    /**
     * A font as its BMFont files describe it.
     */
    private record Written(int lineHeight, int base, boolean antialiased, Map<Integer, Map<String, Integer>> chars,
                           Map<Long, Integer> kernings, BufferedImage page) {

        static Written read(Path descriptor) throws IOException {
            var chars = new HashMap<Integer, Map<String, Integer>>();
            var kernings = new HashMap<Long, Integer>();
            Map<String, String> info = Map.of();
            Map<String, String> common = Map.of();
            String pageFile = null;

            for (var line : Files.readAllLines(descriptor)) {
                var tag = line.substring(0, line.indexOf(' '));
                var fields = fields(line);
                switch (tag) {
                    case "info" -> info = fields;
                    case "common" -> common = fields;
                    case "page" -> pageFile = fields.get("file");
                    case "char" -> {
                        var numbers = new HashMap<String, Integer>();
                        fields.forEach((key, value) -> numbers.put(key, Integer.parseInt(value)));
                        chars.put(numbers.get("id"), numbers);
                    }
                    case "kerning" -> kernings.put(pair(Integer.parseInt(fields.get("first")), Integer.parseInt(fields.get("second"))),
                        Integer.parseInt(fields.get("amount")));
                    default -> {
                        /* empty */
                    }
                }
            }

            var page = ImageIO.read(descriptor.resolveSibling(pageFile).toFile());
            return new Written(Integer.parseInt(common.get("lineHeight")), Integer.parseInt(common.get("base")),
                info.get("aa").equals("1"), chars, kernings, page);
        }

        /**
         * Draws a line of text with its top at {@code top}, blending as the client's software
         * fonts blend: an antialiased glyph mixes the colour with what is under it by its alpha
         * out of 256 ({@code JavaMonoAlphaFont.blit}), and any other glyph writes the colour, or
         * its own colour, wherever it covers at all ({@code JavaMonoFont} and {@code JavaFont}).
         */
        void draw(List<BmFont.Glyph> glyphs, int left, int top, int[] surface, int width, boolean ownColours) {
            var pen = left;
            var previous = -1;
            for (var glyph : glyphs) {
                var metrics = chars.get(glyph.unicode());
                if (previous != -1) {
                    pen += kernings.getOrDefault(pair(previous, glyph.unicode()), 0);
                }
                for (var y = 0; y < metrics.get("height"); y++) {
                    for (var x = 0; x < metrics.get("width"); x++) {
                        var texel = page.getRGB(metrics.get("x") + x, metrics.get("y") + y);
                        var alpha = texel >>> 24;
                        var at = pen + metrics.get("xoffset") + x + (top + metrics.get("yoffset") + y) * width;
                        if (alpha == 0) {
                            /* empty */
                        } else if (ownColours) {
                            surface[at] = texel & 0xFFFFFF;
                        } else if (antialiased) {
                            surface[at] = blend(TEXT_COLOUR, alpha) + blend(surface[at], 256 - alpha);
                        } else {
                            surface[at] = TEXT_COLOUR;
                        }
                    }
                }
                pen += metrics.get("xadvance");
                previous = glyph.unicode();
            }
        }

        private static int blend(int colour, int alpha) {
            var redBlue = (colour & 0xFF00FF) * alpha & 0xFF00FF00;
            var green = (colour & 0xFF00) * alpha & 0xFF0000;
            return (redBlue + green) >>> 8;
        }

        private static long pair(int first, int second) {
            return (long) first << 32 | second;
        }

        private static Map<String, String> fields(String line) {
            var fields = new HashMap<String, String>();
            var at = line.indexOf(' ');
            while (at < line.length()) {
                while (at < line.length() && line.charAt(at) == ' ') {
                    at++;
                }
                var equals = line.indexOf('=', at);
                if (equals < 0) {
                    at = line.length();
                } else {
                    var key = line.substring(at, equals);
                    var quoted = equals + 1 < line.length() && line.charAt(equals + 1) == '"';
                    var end = quoted ? line.indexOf('"', equals + 2) : line.indexOf(' ', equals);
                    end = end < 0 ? line.length() : end;
                    fields.put(key, line.substring(equals + (quoted ? 2 : 1), end));
                    at = end + (quoted ? 1 : 0);
                }
            }
            return fields;
        }
    }
}

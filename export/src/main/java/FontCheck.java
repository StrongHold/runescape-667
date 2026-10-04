import com.jagex.IndexedImage;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.TextureSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

/**
 * Checks a written font by drawing text with it and with the client, and counting the texels
 * that differ.
 *
 * The written font is read back from its file as any BDF reader would, and laid out by the BDF
 * rules: each glyph's bitmap with its bottom left corner at the pen on the baseline plus its
 * offsets, and the pen moved on by its advance. The client draws the same text with its own
 * software fonts ({@code JavaMonoFont} and {@code JavaFont}), built from the cache by the software
 * toolkit as the client builds them, onto a surface of the same size. Both draw every glyph a
 * character reaches, and the text is drawn once in one colour and once more in the glyphs' own
 * colour, as a component may ask.
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
    public int differences(Path file, FontMetrics metrics, IndexedImage[] glyphs) throws IOException {
        var written = Written.read(file);
        var lines = lines();
        var width = 2 * MARGIN + lines.stream().mapToInt(line -> line.size()).max().orElse(0) * 2 * maxAdvance(metrics);
        var height = 2 * MARGIN + lines.size() * 2 * Math.max(metrics.verticalSpacing, 1);

        var differ = compare(written, lines, toolkit.createFont(metrics, glyphs, true), width, height, false);
        return differ + compare(written, lines, toolkit.createFont(metrics, glyphs, false), width, height, true);
    }

    private int compare(Written written, List<List<BdfFont.Glyph>> lines, com.jagex.graphics.Font client, int width, int height, boolean ownColours) {
        var expected = new int[width * height];
        toolkit.swapSurface(toolkit.createOffscreenSurface(new JavaArgbSprite(toolkit, expected, width, height), null));

        var actual = new int[width * height];
        for (var line = 0; line < lines.size(); line++) {
            var baseline = MARGIN + line * 2 * written.size + written.ascent;
            client.render(clientText(lines.get(line)), MARGIN, baseline, NO_SHADOW, TEXT_COLOUR);
            written.draw(lines.get(line), MARGIN, baseline, actual, width, ownColours);
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
    private static List<List<BdfFont.Glyph>> lines() {
        var glyphs = BdfFont.glyphs();
        var lines = new ArrayList<List<BdfFont.Glyph>>();
        for (var from = 0; from < glyphs.size(); from += GLYPHS_A_LINE) {
            lines.add(glyphs.subList(from, Math.min(from + GLYPHS_A_LINE, glyphs.size())));
        }
        var byUnicode = new HashMap<Integer, BdfFont.Glyph>();
        glyphs.forEach(glyph -> byUnicode.put(glyph.unicode(), glyph));
        lines.add(PANGRAM.chars().mapToObj(byUnicode::get).toList());
        return lines;
    }

    /**
     * The text as the client is given it. The client takes a {@code <} as the start of a tag, so
     * the two angle brackets are written as the tags that stand for them.
     */
    private static String clientText(List<BdfFont.Glyph> glyphs) {
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
     * One glyph as its BDF entry describes it: its advance, its box, and its bitmap, a row at a
     * time from the top, a bit a pixel.
     */
    private record WrittenGlyph(int advance, int width, int height, int left, int bottom, List<byte[]> rows) {

        boolean covers(int x, int y) {
            return (rows.get(y)[x / 8] & 0x80 >> x % 8) != 0;
        }
    }

    /**
     * A font as its BDF file describes it: its size, its ascent, its glyphs' colour, and its glyphs
     * by their character.
     */
    private record Written(int size, int ascent, int colour, Map<Integer, WrittenGlyph> glyphs) {

        static Written read(Path file) throws IOException {
            var glyphs = new HashMap<Integer, WrittenGlyph>();
            var size = 0;
            var ascent = 0;
            var colour = 0;
            var encoding = -1;
            var advance = 0;
            int[] box = null;
            List<byte[]> rows = null;
            for (var line : Files.readAllLines(file)) {
                var words = line.split(" ");
                if (rows != null && !words[0].equals("ENDCHAR")) {
                    rows.add(HexFormat.of().parseHex(line));
                } else {
                    switch (words[0]) {
                        case "SIZE" -> size = Integer.parseInt(words[1]);
                        case "FONT_ASCENT" -> ascent = Integer.parseInt(words[1]);
                        case "GLYPH_COLOUR" -> colour = Integer.parseInt(words[1].replace("\"", ""), 16);
                        case "ENCODING" -> encoding = Integer.parseInt(words[1]);
                        case "DWIDTH" -> advance = Integer.parseInt(words[1]);
                        case "BBX" -> box = new int[] {Integer.parseInt(words[1]), Integer.parseInt(words[2]),
                            Integer.parseInt(words[3]), Integer.parseInt(words[4])};
                        case "BITMAP" -> rows = new ArrayList<>();
                        case "ENDCHAR" -> {
                            glyphs.put(encoding, new WrittenGlyph(advance, box[0], box[1], box[2], box[3], rows));
                            rows = null;
                        }
                        default -> {
                            /* empty */
                        }
                    }
                }
            }
            return new Written(size, ascent, colour, glyphs);
        }

        /**
         * Draws a line of text on its baseline as the client's software fonts draw a font of one
         * bit a pixel: the colour, or the glyphs' own colour, wherever a glyph covers
         * ({@code JavaMonoFont} and {@code JavaFont}).
         */
        void draw(List<BdfFont.Glyph> text, int left, int baseline, int[] surface, int width, boolean ownColours) {
            var pen = left;
            for (var character : text) {
                var glyph = glyphs.get(character.unicode());
                var top = baseline - glyph.bottom() - glyph.height();
                for (var y = 0; y < glyph.height(); y++) {
                    for (var x = 0; x < glyph.width(); x++) {
                        if (glyph.covers(x, y)) {
                            surface[pen + glyph.left() + x + (top + y) * width] = ownColours ? colour : TEXT_COLOUR;
                        }
                    }
                }
                pen += glyph.advance();
            }
        }
    }
}

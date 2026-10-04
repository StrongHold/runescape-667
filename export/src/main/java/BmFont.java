import com.jagex.IndexedImage;
import com.jagex.core.stringtools.general.Cp1252;
import com.jagex.graphics.FontMetrics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * One of the client's fonts as an AngelCode BMFont: a text descriptor and one atlas page.
 *
 * The client keeps a font as two halves under the same id. The sprite archive holds 256 glyph
 * images, one for each byte of code page 1252, and the font metrics archive holds the advance of
 * each glyph, the line spacing, the space above and below the baseline, and for some fonts a
 * table of how far to move each pair of glyphs together. Both halves are read with the client's
 * own readers, {@code IndexedImage.load} and {@code FontMetrics}.
 *
 * The client draws a glyph at its baseline less the line spacing, plus the offset the glyph image
 * carries ({@code Font.render} and {@code JavaMonoFont.fa}). BMFont measures a glyph from the top
 * of the line, and the top of a line is the baseline less the space above it, so a glyph's
 * {@code yoffset} is its own offset less the line spacing plus the space above.
 *
 * @param descriptor the {@code .fnt} file, in the BMFont text format.
 * @param page the atlas, with each glyph's colour and coverage as the client keeps them.
 * @param antialiased whether any glyph carries its own alpha, which the client then blends by.
 */
public record BmFont(String descriptor, BufferedImage page, boolean antialiased) {

    private static final int GLYPHS = 256;

    /**
     * The glyphs are laid out as the GL toolkit lays them out in its texture ({@code Font_Sub2}),
     * sixteen to a row in cells as large as the largest glyph, with one more texel between cells
     * so that a filtered sample never reaches a neighbour.
     */
    private static final int COLUMNS = 16;
    private static final int SPACING = 1;

    /**
     * The client never draws the space glyph, it only moves on by its advance
     * ({@code Font.render}).
     */
    private static final int SPACE = ' ';

    public static BmFont of(String face, String pageFile, FontMetrics metrics, IndexedImage[] glyphs) {
        if (glyphs.length != GLYPHS) {
            throw new IllegalArgumentException(face + " has " + glyphs.length + " glyph images, not " + GLYPHS);
        }

        var cellWidth = 0;
        var cellHeight = 0;
        for (var glyph : glyphs) {
            cellWidth = Math.max(cellWidth, glyph.width);
            cellHeight = Math.max(cellHeight, glyph.height);
        }
        cellWidth += SPACING;
        cellHeight += SPACING;

        var page = new BufferedImage(COLUMNS * cellWidth, GLYPHS / COLUMNS * cellHeight, BufferedImage.TYPE_INT_ARGB);
        for (var code = 0; code < GLYPHS; code++) {
            draw(glyphs[code], page, code % COLUMNS * cellWidth, code / COLUMNS * cellHeight);
        }

        var characters = glyphs();
        var antialiased = false;
        for (var glyph : glyphs) {
            antialiased |= glyph.alpha != null;
        }

        var out = new StringBuilder();
        out.append("info face=\"").append(face).append("\" size=").append(metrics.verticalSpacing)
            .append(" bold=0 italic=0 charset=\"\" unicode=1 stretchH=100 smooth=0 aa=").append(antialiased ? 1 : 0)
            .append(" padding=0,0,0,0 spacing=").append(SPACING).append(',').append(SPACING).append(" outline=0\n");
        out.append("common lineHeight=").append(metrics.verticalSpacing).append(" base=").append(metrics.paddingTop)
            .append(" scaleW=").append(page.getWidth()).append(" scaleH=").append(page.getHeight())
            .append(" pages=1 packed=0 alphaChnl=0 redChnl=0 greenChnl=0 blueChnl=0\n");
        out.append("page id=0 file=\"").append(pageFile).append("\"\n");
        out.append("chars count=").append(characters.size()).append('\n');

        for (var character : characters) {
            var glyph = glyphs[character.code()];
            var drawn = character.code() != SPACE;
            out.append("char id=").append(character.unicode())
                .append(" x=").append(character.code() % COLUMNS * cellWidth)
                .append(" y=").append(character.code() / COLUMNS * cellHeight)
                .append(" width=").append(drawn ? glyph.width : 0)
                .append(" height=").append(drawn ? glyph.height : 0)
                .append(" xoffset=").append(glyph.offX1)
                .append(" yoffset=").append(glyph.offY1 - metrics.verticalSpacing + metrics.paddingTop)
                .append(" xadvance=").append(metrics.glyphWidth(character.code()))
                .append(" page=0 chnl=15\n");
        }

        var kernings = new ArrayList<String>();
        for (var first : characters) {
            for (var second : characters) {
                var amount = metrics.glyphSpacing((char) second.code(), first.code());
                if (amount != 0) {
                    kernings.add("kerning first=" + first.unicode() + " second=" + second.unicode() + " amount=" + amount + "\n");
                }
            }
        }
        if (!kernings.isEmpty()) {
            out.append("kernings count=").append(kernings.size()).append('\n');
            kernings.forEach(out::append);
        }

        return new BmFont(out.toString(), page, antialiased);
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
     * Copies one glyph into the page. Its colour is the palette colour of each texel, and its
     * coverage is the glyph's own alpha where it has one and otherwise full wherever the texel is
     * not 0, which is what both toolkits read from it.
     */
    private static void draw(IndexedImage glyph, BufferedImage page, int left, int top) {
        for (var y = 0; y < glyph.height; y++) {
            for (var x = 0; x < glyph.width; x++) {
                var texel = x + y * glyph.width;
                var index = glyph.raster[texel] & 0xFF;
                var coverage = glyph.alpha != null ? glyph.alpha[texel] & 0xFF : index != 0 ? 0xFF : 0;
                var colour = coverage == 0 ? 0 : glyph.palette[index];
                page.setRGB(left + x, top + y, coverage << 24 | colour);
            }
        }
    }
}

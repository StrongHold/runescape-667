import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Writes what the GL toolkit's turbulent water effect draws with, beside the textures: the
 * sixteen frames of rippling noise it adds to the water, and the two tables of noise it offsets
 * the water's texture coordinates by.
 *
 * <p>The ripple frames are the client's own {@code GlRippleNoiseTexture}, 128 by 128 texels of
 * luminance and alpha, written as one strip of sixteen frames down a PNG with the luminance in
 * each colour channel. The turbulence is {@code Static490.method6551} at the effect's amplitude
 * of 0.4, a grid of 256 rows by 64 columns for x and another for y, in 4096ths.
 */
public final class WaterTextures {

    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;
    private static final int FRAMES = 16;
    private static final int BYTES_PER_TEXEL = 2;
    /** {@code TurbulentWaterEffect.NOISE_AMPLITUDE}. */
    private static final float TURBULENCE_AMPLITUDE = 0.4F;

    public static void write(Path directory) throws IOException {
        var water = directory.resolve("water");
        Files.createDirectories(water);
        ImageIO.write(rippleStrip(), "png", water.resolve("ripple.png").toFile());
        Files.writeString(water.resolve("turbulence.json"), Json.write(turbulence()));
    }

    private static BufferedImage rippleStrip() {
        var texels = new GlRippleNoiseTexture().generate();
        var image = new BufferedImage(WIDTH, HEIGHT * FRAMES, BufferedImage.TYPE_INT_ARGB);
        for (var frame = 0; frame < FRAMES; frame++) {
            for (var y = 0; y < HEIGHT; y++) {
                for (var x = 0; x < WIDTH; x++) {
                    var at = ((frame * HEIGHT + y) * WIDTH + x) * BYTES_PER_TEXEL;
                    var luminance = texels[at] & 0xFF;
                    var alpha = texels[at + 1] & 0xFF;
                    image.setRGB(x, frame * HEIGHT + y, alpha << 24 | luminance << 16 | luminance << 8 | luminance);
                }
            }
        }
        return image;
    }

    private static LinkedHashMap<String, Object> turbulence() {
        var table = new LinkedHashMap<String, Object>();
        table.put("x", rows(Static490.method6551(TURBULENCE_AMPLITUDE)));
        table.put("y", rows(Static490.method6551(TURBULENCE_AMPLITUDE)));
        return table;
    }

    private static List<List<Integer>> rows(int[][] grid) {
        var rows = new ArrayList<List<Integer>>();
        for (var row : grid) {
            var values = new ArrayList<Integer>(row.length);
            for (var value : row) {
                values.add(value);
            }
            rows.add(values);
        }
        return rows;
    }

    private WaterTextures() {
        /* empty */
    }
}

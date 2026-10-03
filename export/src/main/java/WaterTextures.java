import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes what the GL toolkit's fixed function water effect draws with, beside the textures: the
 * sixteen frames of rippling noise it lays over the water, the client's own
 * {@code GlRippleNoiseTexture}, 128 by 128 texels of luminance and alpha, written as one strip
 * of sixteen frames down a PNG with the luminance in each colour channel.
 */
public final class WaterTextures {

    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;
    private static final int FRAMES = 16;
    private static final int BYTES_PER_TEXEL = 2;

    public static void write(Path directory) throws IOException {
        var water = directory.resolve("water");
        Files.createDirectories(water);
        ImageIO.write(rippleStrip(), "png", water.resolve("ripple.png").toFile());
        Files.deleteIfExists(water.resolve("turbulence.json"));
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


    private WaterTextures() {
        /* empty */
    }
}

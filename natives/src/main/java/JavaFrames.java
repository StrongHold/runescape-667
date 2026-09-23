import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

/**
 * Writes what the toolkit written in Java drew, one picture a frame.
 *
 * The native toolkits hand their pictures to a surface, and the surface writes them out. The Java
 * toolkit keeps its picture in a buffer of its own, so it is read from there, and written under the
 * same names in the same place.
 */
public final class JavaFrames {

    public static void write(JavaToolkit toolkit, int frame) {
        var directory = FrameCapture.dumpDirectory();
        if (directory == null) {
            return;
        }

        var picture = new BufferedImage(Scene.WIDTH, Scene.HEIGHT, BufferedImage.TYPE_INT_RGB);
        picture.setRGB(0, 0, Scene.WIDTH, Scene.HEIGHT, toolkit.surfaceRaster, 0, Scene.WIDTH);

        try {
            ImageIO.write(picture, "png", Path.of(directory, "frame-%04d.png".formatted(frame)).toFile());
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
    }

    private JavaFrames() {
        /* empty */
    }
}

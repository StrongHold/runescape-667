import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Compares the frames our toolkit rasterised against the ones the shipped toolkit rasterised from
 * the same scene, and fails on any difference.
 *
 * This is the only thing that can say whether the replacement draws what the client expects. A
 * frame that merely looks right is not the same as one that is right, and a rasteriser has far too
 * many ways to be nearly correct.
 */
public final class FrameDiff {

    private static final int CHANNELS = 0xFFFFFF;

    public static void main(String[] args) {
        try {
            compare(new File(args[0]), new File(args[1]));
            System.exit(0);
        } catch (Throwable failure) {
            System.out.println(failure.getMessage());
            System.exit(1);
        }
    }

    private static void compare(File expected, File actual) throws Exception {
        File[] frames = expected.listFiles((directory, name) -> name.endsWith(".png"));
        if (frames == null || frames.length == 0) {
            throw new IllegalStateException("No frames to compare in " + expected + ".");
        }

        for (File frame : frames) {
            compareFrame(frame, new File(actual, frame.getName()));
        }

        System.out.println(frames.length + " frames identical to the shipped toolkit");
    }

    private static void compareFrame(File expected, File actual) throws Exception {
        if (!actual.isFile()) {
            throw new IllegalStateException("Our toolkit produced no " + actual.getName() + ".");
        }

        BufferedImage left = ImageIO.read(expected);
        BufferedImage right = ImageIO.read(actual);

        if (left.getWidth() != right.getWidth() || left.getHeight() != right.getHeight()) {
            throw new IllegalStateException(
                actual.getName() + " is " + right.getWidth() + "x" + right.getHeight()
                    + " but the shipped toolkit drew " + left.getWidth() + "x" + left.getHeight() + ".");
        }

        for (int y = 0; y < left.getHeight(); y++) {
            for (int x = 0; x < left.getWidth(); x++) {
                int wanted = left.getRGB(x, y) & CHANNELS;
                int got = right.getRGB(x, y) & CHANNELS;

                if (wanted != got) {
                    throw new IllegalStateException(String.format(
                        "%s differs at %d,%d: the shipped toolkit drew %06x and we drew %06x.",
                        actual.getName(), x, y, wanted, got));
                }
            }
        }
    }

    private FrameDiff() {
        /* empty */
    }
}

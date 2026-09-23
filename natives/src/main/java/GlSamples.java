import jaggl.OpenGL;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;

/**
 * Holds the OpenGL binding to drawing with as many samples a pixel as it was asked for, and to
 * still giving the picture back afterwards.
 *
 * The client draws into a buffer of this library's own rather than the one the window carries, so
 * how finely a pixel is drawn is settled by what is hung on that buffer and not by the format the
 * context was made with. Hanging several samples on it is also the one thing that can stop the
 * client reading its own picture back, because nothing may be read out of such a buffer directly.
 *
 * So both halves are asked here. Asking only how many samples the buffer has answers four while
 * the screen stays black, which is how that went in once.
 */
public final class GlSamples {

    private static final int SIDE = 256;

    /** None, and the two the client can ask for: its setting is nought, one or two, doubled. */
    private static final int[] ASKED = {0, 2, 4};

    private static final float RED = 0.2F;
    private static final float GREEN = 0.4F;
    private static final float BLUE = 0.8F;
    private static final int WANTED = 0xFFCC6633;

    private static final int SAMPLES = 0x80A9;
    private static final int RGBA = 0x1908;
    private static final int UNSIGNED_BYTE = 0x1401;
    private static final int COLOUR_BUFFER = 0x4000;

    public static void main(String[] arguments) {
        var parsed = CommandLine.parse("verifyOpenGlSamples", new LibraryArgs(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(LibraryArgs args) {
        try {
            Watchdog.arm("The OpenGL sample check", 120);
            LibraryManager.putLibrary(args.library(), "jaggl");
            LibraryManager.loadNative(GlSamples.class, "jaggl");

            var canvas = new Canvas();
            canvas.setSize(SIDE, SIDE);
            var window = new Frame("opengl sample check");
            window.add(canvas);
            window.pack();
            window.setVisible(true);
            Thread.sleep(1000);

            for (int asked : ASKED) {
                check(canvas, asked);
            }

            System.out.println("the binding drew and read back at " + ASKED.length + " sample counts");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void check(Canvas canvas, int asked) {
        var binding = new OpenGL();
        long surface = binding.init(canvas, 8, 8, 8, 24, 0, asked);
        if (surface == 0L || !binding.setSurface(surface)) {
            throw new IllegalStateException("no surface for " + asked + " samples");
        }

        int held = whole(SAMPLES);
        if (held != asked) {
            throw new IllegalStateException(
                "asked for " + asked + " samples a pixel and the buffer carries " + held);
        }

        OpenGL.glViewport(0, 0, SIDE, SIDE);
        OpenGL.glClearColor(RED, GREEN, BLUE, 1F);
        OpenGL.glClear(COLOUR_BUFFER);
        OpenGL.glFinish();

        int[] pixels = new int[SIDE * SIDE];
        OpenGL.glReadPixelsi(0, 0, SIDE, SIDE, RGBA, UNSIGNED_BYTE, pixels, 0);

        int error = OpenGL.glGetError();
        if (error != 0) {
            throw new IllegalStateException(
                "reading the picture back at " + asked + " samples a pixel failed with " + error);
        }

        int middle = pixels[SIDE / 2 * SIDE + SIDE / 2];
        if (middle != WANTED) {
            throw new IllegalStateException("at " + asked + " samples a pixel the middle came back "
                + Integer.toHexString(middle) + " rather than " + Integer.toHexString(WANTED));
        }

        binding.releaseSurface(canvas, surface);
        binding.release();
    }

    private static int whole(int name) {
        int[] held = new int[1];
        OpenGL.glGetIntegerv(name, held, 0);
        return held[0];
    }

    private GlSamples() {
        /* empty */
    }
}

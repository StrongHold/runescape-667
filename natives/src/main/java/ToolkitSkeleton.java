import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;

/**
 * Builds the software toolkit against an implementation that does nothing, on the architecture the
 * shipped one does not have.
 *
 * Nothing is drawn, so nothing about the picture can be checked. What this does prove is that the
 * set of natives the client calls is complete and that the toolkit's object model survives every
 * one of them returning nothing. Set SW3D_SKELETON_VERBOSE to see which natives the client reaches
 * and in what order, which is the order they are worth implementing in.
 */
public final class ToolkitSkeleton {

    private static final int WIDTH = 512;
    private static final int HEIGHT = 384;

    public static void main(String[] arguments) {
        var args = new LibraryArgs();

        if (!CommandLine.parsed("verifyToolkitSkeleton", args, arguments)) {
            return;
        }

        try {
            Watchdog.arm("The skeleton check", 120);
            build(args.library());
            System.out.println("the toolkit built against the skeleton on " + System.getProperty("os.arch"));
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void build(File library) throws Exception {
        if (!System.getProperty("os.arch").equals("aarch64")) {
            throw new IllegalStateException(
                "The skeleton is built for arm64, so this has to run on an arm64 machine, not "
                    + System.getProperty("os.arch") + ".");
        }

        LibraryManager.putLibrary(library, "sw3d");

        Frame frame = new Frame("toolkit skeleton");
        Canvas canvas = new Canvas();
        canvas.setSize(WIDTH, HEIGHT);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(500);

        Toolkit toolkit = oa.create(canvas, new StubTextureSource(), WIDTH, HEIGHT);
        if (toolkit == null) {
            throw new IllegalStateException("The toolkit did not build.");
        }
    }

    private ToolkitSkeleton() {
        /* empty */
    }
}

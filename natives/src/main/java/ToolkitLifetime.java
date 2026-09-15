import com.jagex.graphics.FlipException;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;

/**
 * Builds and discards software toolkits with collection forced in between, which is what choosing
 * a graphics profile does.
 *
 * The toolkit's canvas drains an autorelease pool it took on another thread when it is destroyed,
 * so a toolkit torn down through finalization ends the process. Nothing can be asserted here: the
 * failure is a dead virtual machine, so finishing at all is the result.
 */
public final class ToolkitLifetime {

    private static final int WIDTH = 512;
    private static final int HEIGHT = 384;
    private static final int ROUNDS = 6;

    public static void main(String[] args) {
        try {
            Watchdog.arm("The toolkit lifetime check", 120);
            run(args[0]);
            System.out.println("built and discarded " + ROUNDS + " toolkits");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void run(String library) throws Exception {
        LibraryManager.putLibrary(new File(library), "sw3d");

        Frame frame = new Frame("toolkit lifetime");
        Canvas canvas = new Canvas();
        canvas.setSize(WIDTH, HEIGHT);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(600);

        for (int round = 0; round < ROUNDS; round++) {
            discardToolkit(canvas, round % 2 == 0);
            System.gc();
            System.runFinalization();
            Thread.sleep(150);
        }
    }

    /**
     * Leaves nothing referring to the toolkit, so that collection can reach it.
     *
     * Releasing the toolkit is the harder case and is what switching to another one does. It
     * empties the toolkit of its surfaces and drops the count of live toolkits to zero in one
     * step, so the surfaces are left for the collector at exactly the moment releasing them where
     * they stand becomes unsafe.
     */
    private static void discardToolkit(Canvas canvas, boolean release) throws FlipException {
        Toolkit toolkit = Static226.create(ToolkitType.SSE, null, canvas, new StubTextureSource(), WIDTH, HEIGHT, 0);
        toolkit.GA(0);
        toolkit.ya();
        toolkit.flip(0, 0);

        if (release) {
            toolkit.free();
        }
    }

    private ToolkitLifetime() {
        /* empty */
    }
}

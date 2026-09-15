import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;

/**
 * Renders a fixed scene through the software toolkit and leaves the frames on disk, so that another
 * implementation of the toolkit can be compared against this one frame by frame.
 *
 * The client only reaches the toolkit after the user picks it, which makes it a poor place to test
 * rendering. This drives the toolkit directly instead.
 *
 * Set JAWTSHIM_DUMP to the directory the frames should go in. Run it on an x86_64 JVM, because the
 * toolkit has no arm64 build.
 */
public final class FrameCapture {

    private static final int WIDTH = 512;
    private static final int HEIGHT = 384;
    private static final int SPRITE_SIZE = 128;
    private static final int CLEAR_COLOUR = 0x202080;
    private static final int FRAMES = 3;

    public static void main(String[] args) throws Exception {
        LibraryManager.putLibrary(new File(args[0]), "sw3d");

        Frame frame = new Frame("software toolkit frame capture");
        Canvas canvas = new Canvas();
        canvas.setSize(WIDTH, HEIGHT);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(1000);

        Toolkit toolkit = oa.create(canvas, new StubTextureSource(), WIDTH, HEIGHT);
        Sprite sprite = toolkit.createSprite(SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, gradient());

        toolkit.la();
        toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
        toolkit.xa(1.0F);

        for (int i = 0; i < FRAMES; i++) {
            toolkit.GA(CLEAR_COLOUR);
            toolkit.ya();
            sprite.render(10, 10, 0, 0xFFFFFF, 0);
            sprite.render(10, 200, 3, 0xFFFFFF, 0);
            toolkit.flip(0, 0);
            Thread.sleep(200);
        }

        toolkit.method7950();
        System.out.println("captured " + FRAMES + " frames");
        System.exit(0);
    }

    private static int[] gradient() {
        int[] pixels = new int[SPRITE_SIZE * SPRITE_SIZE];
        for (int i = 0; i < pixels.length; i++) {
            int x = i % SPRITE_SIZE;
            int y = i / SPRITE_SIZE;
            pixels[i] = 0xFF000000 | (x * 2 << 16) | (y * 2 << 8) | 0x80;
        }
        return pixels;
    }
}

import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import javax.imageio.ImageIO;
import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;

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
    /**
     * The sprite is not square and not symmetrical, so a frame says which of its dimensions the
     * toolkit read as which, and which way round it drew both axes. A square sprite cannot.
     */
    private static final int SPRITE_WIDTH = 96;
    private static final int SPRITE_HEIGHT = 64;

    private static final int CLEAR_COLOUR = 0x202080;
    private static final int FRAMES = 3;
    private static final int GRADIENT_X = 10;
    private static final int GRADIENT_Y = 10;

    private static final int MATRIX_Y = 272;
    private static final int MATRIX_BANDS = 7;
    private static final int MATRIX_BAND_HEIGHT = 16;
    private static final int[] ALPHAS = {1, 64, 128, 129, 192, 254, 255};

    /**
     * The window keeps the virtual machine alive after this method returns, so every path out of
     * here ends in an explicit exit. Without that a failed check hangs instead of reporting.
     */
    public static void main(String[] args) {
        try {
            Watchdog.arm("The frame capture", 120);
            capture(args[0]);
            System.out.println("captured " + FRAMES + " frames");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void capture(String library) throws Exception {
        LibraryManager.putLibrary(new File(library), "sw3d");

        Frame frame = new Frame("software toolkit frame capture");
        Canvas canvas = new Canvas();
        canvas.setSize(WIDTH, HEIGHT);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(1000);

        Toolkit toolkit = oa.create(canvas, new StubTextureSource(), WIDTH, HEIGHT);
        Sprite sprite = toolkit.createSprite(SPRITE_WIDTH, SPRITE_WIDTH, SPRITE_HEIGHT, gradient());

        toolkit.la();
        toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
        toolkit.xa(1.0F);

        for (int i = 0; i < FRAMES; i++) {
            toolkit.GA(CLEAR_COLOUR);
            toolkit.ya();
            sprite.render(GRADIENT_X, GRADIENT_Y, 0, 0xFFFFFF, 0);
            sprite.render(GRADIENT_X, 200, 3, 0xFFFFFF, 0);
            drawFlatShapes(toolkit);
            toolkit.flip(0, 0);
            Thread.sleep(200);
        }

        toolkit.method7950();
        checkOrientation();
    }

    /**
     * The gradient sprite is drawn at a known place and its green channel rises with its row, so
     * the frame says both where the toolkit put it and which way up it is. Reading it back catches
     * a transform the surface failed to reset, which is invisible in a symmetrical test image.
     */
    private static void checkOrientation() throws IOException {
        String directory = dumpDirectory();
        if (directory == null) {
            return;
        }

        BufferedImage frame = ImageIO.read(new File(directory, "frame-0000.png"));

        int column = GRADIENT_X + SPRITE_WIDTH / 2;
        int top = frame.getRGB(column, GRADIENT_Y + 4) & 0xFFFFFF;
        int bottom = frame.getRGB(column, GRADIENT_Y + SPRITE_HEIGHT - 4) & 0xFFFFFF;
        requireGradient("top", top, GRADIENT_Y + 4);
        requireGradient("bottom", bottom, GRADIENT_Y + SPRITE_HEIGHT - 4);
        requireRise("Green rises down the source sprite", (top >> 8) & 0xFF, (bottom >> 8) & 0xFF);

        int row = GRADIENT_Y + SPRITE_HEIGHT / 2;
        int left = frame.getRGB(GRADIENT_X + 4, row) & 0xFFFFFF;
        int right = frame.getRGB(GRADIENT_X + SPRITE_WIDTH - 4, row) & 0xFFFFFF;
        requireGradient("left", left, row);
        requireGradient("right", right, row);
        requireRise("Red rises to the right of the source sprite", (left >> 16) & 0xFF, (right >> 16) & 0xFF);
    }

    /**
     * Where presented frames are written. The shipped toolkit is driven through the shim and ours
     * writes them itself, so either may name the directory.
     */
    private static String dumpDirectory() {
        String shim = System.getenv("JAWTSHIM_DUMP");
        return shim == null ? System.getenv("SW3D_DUMP") : shim;
    }

    private static void requireRise(String what, int first, int last) {
        if (first >= last) {
            throw new IllegalStateException(
                what + ", but the frame reads " + first + " then " + last + ".");
        }
    }

    /**
     * The clear colour where the sprite should be means the toolkit drew it somewhere else, which
     * a check on the gradient direction alone would read as a pass.
     */
    private static void requireGradient(String edge, int colour, int row) {
        if (colour == CLEAR_COLOUR) {
            throw new IllegalStateException(
                "Row " + row + " holds the clear colour, so the gradient is not where it was drawn "
                    + "and its " + edge + " cannot be read.");
        }
    }

    /**
     * The shapes the client's interfaces are made of, drawn where they cannot land on the sprites.
     *
     * Each is drawn twice, once inside a clip that cuts it in half, so a clip that is ignored or
     * applied to the wrong edge shows as a difference rather than as nothing.
     */
    private static void drawFlatShapes(Toolkit toolkit) {
        alphaSweep(toolkit);
        modes(toolkit);
        clipped(toolkit);
        blendMatrix(toolkit);
    }

    /**
     * Every source value blended over a spread of destination values, at one alpha per band.
     *
     * A blend can be wrong in a way that only shows at particular values, and two formulas that
     * agree on most inputs disagree on a handful. This leaves nowhere for that to hide: each
     * column is one source value and each band is one destination and alpha, so the frame carries
     * the whole function rather than a few samples of it.
     */
    private static void blendMatrix(Toolkit toolkit) {
        for (int band = 0; band < MATRIX_BANDS; band++) {
            int y = MATRIX_Y + band * MATRIX_BAND_HEIGHT;
            int destination = band * 36;
            int alpha = ALPHAS[band % ALPHAS.length];

            toolkit.aa(0, y, 256, MATRIX_BAND_HEIGHT, 0xFF000000 | grey(destination), 0);

            for (int source = 0; source < 256; source++) {
                toolkit.aa(source, y, 1, MATRIX_BAND_HEIGHT, (alpha << 24) | grey(source), 1);
            }
        }
    }

    private static int grey(int value) {
        return (value << 16) | (value << 8) | value;
    }

    /**
     * The same fill at every alpha the client can ask for, so the blend is pinned across its whole
     * range rather than at one or two points where more than one formula would fit.
     */
    private static void alphaSweep(Toolkit toolkit) {
        for (int step = 0; step <= 16; step++) {
            int alpha = step == 16 ? 0xFF : step * 16;
            toolkit.aa(130 + step * 22, 20, 20, 24, (alpha << 24) | 0xCC3311, 1);
        }
    }

    /**
     * Each way of putting a colour down, over a background and over an already drawn shape, so
     * that a blend which only looks right against the clear colour still fails.
     */
    private static void modes(Toolkit toolkit) {
        toolkit.aa(130, 60, 340, 24, 0xFF806040, 0);

        toolkit.aa(140, 66, 60, 12, 0xFFCC3311, 0);
        toolkit.aa(210, 66, 60, 12, 0x80CC3311, 1);
        toolkit.aa(280, 66, 60, 12, 0xFFCC3311, 2);
        toolkit.aa(350, 66, 60, 12, 0xFFF0F0F0, 2);

        toolkit.outlineRect(130, 100, 80, 30, 0xFF33CC11, 0);
        toolkit.outlineRect(220, 100, 80, 30, 0x8033CC11, 1);
        toolkit.outlineRect(310, 100, 80, 30, 0xFF33CC11, 2);

        toolkit.line(130, 150, 470, 170, 0xFF11CCCC, 0);
        toolkit.line(470, 155, 130, 175, 0x8011CCCC, 1);
        toolkit.line(130, 180, 140, 250, 0xFF11CCCC, 0);
        toolkit.line(160, 250, 150, 180, 0xFFCCCC11, 0);


    }

    /**
     * The same shapes again inside a clip that cuts each of them, so a clip applied to the wrong
     * edge or ignored altogether shows up rather than passing unnoticed.
     */
    private static void clipped(Toolkit toolkit) {
        toolkit.KA(330, 200, 450, 250);
        toolkit.aa(300, 190, 180, 70, 0xFF5D5447, 0);
        toolkit.outlineRect(320, 195, 150, 60, 0xFFCC11CC, 0);
        toolkit.line(300, 190, 480, 260, 0xFFFFFFFF, 0);
        toolkit.la();
    }

    /**
     * Red rises to the right and green rises downwards, so either axis drawn the wrong way round
     * shows up as a fall rather than a rise.
     */
    private static int[] gradient() {
        int[] pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
        for (int i = 0; i < pixels.length; i++) {
            int x = i % SPRITE_WIDTH;
            int y = i / SPRITE_WIDTH;
            pixels[i] = 0xFF000000 | (x * 2 << 16) | (y * 3 << 8) | 0x80;
        }
        return pixels;
    }
}

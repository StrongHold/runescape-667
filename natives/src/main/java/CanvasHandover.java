import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.graphics.sw.SoftwareToolkitLifetime;
import rs2.client.loading.library.LibraryManager;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * What the client does when it stops drawing with the software toolkit and starts drawing with the
 * Java one.
 *
 * The software toolkit presents through a layer hung off the canvas. The Java one presents by
 * drawing an image onto the canvas with a graphics of its own. Between the two the client throws
 * its canvas away and builds another, and whether the second renderer's picture then reaches the
 * window is what this asks.
 *
 * The screen is read back rather than looked at, because the failure is that nothing appears and
 * nothing appearing is not something the client can be asked about.
 */
public final class CanvasHandover {

    private static final int WIDE = 400;
    private static final int TALL = 300;

    /** What each renderer draws, so that whichever reached the window can be told from the other. */
    private static final int SOFTWARE_DREW = 0xFF0000;
    private static final int JAVA_DREW = 0x00C000;

    /** How far a channel may drift, because a screen shot comes back through a colour profile. */
    private static final int NEAR_ENOUGH = 24;

    private static Applet applet;
    private static Canvas canvas;

    public static void main(String[] args) throws Exception {
        Watchdog.arm("The canvas handover check", 120);
        SoftwareToolkitLifetime.retainAll();
        LibraryManager.putLibrary(new File(args[0]), "sw3d");

        Frame frame = new Frame("canvas handover");
        applet = new Applet();
        applet.setPreferredSize(new Dimension(WIDE, TALL));
        frame.add(applet);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(700);

        addcanvas();
        Thread.sleep(300);

        Toolkit software = Static226.create(ToolkitType.SSE, null, canvas,
            new StubTextureSource(), WIDE, TALL, 0);
        software.GA(SOFTWARE_DREW | 0xFF000000);
        software.flip(0, 0);
        Thread.sleep(400);
        say("the software toolkit drew", SOFTWARE_DREW);

        software.free();

        addcanvas();
        Thread.sleep(400);
        presentWithJava();
        Thread.sleep(400);
        say("the Java toolkit drew after the canvas was swapped", JAVA_DREW);

        System.exit(0);
    }

    /**
     * The sequence GameShell.addcanvas runs, on this thread rather than the event thread.
     */
    private static void addcanvas() {
        if (canvas != null) {
            canvas.getParent().setBackground(Color.black);
            canvas.getParent().remove(canvas);
        }

        applet.setLayout(null);
        canvas = new Canvas();
        applet.add(canvas);
        canvas.setSize(WIDE, TALL);
        canvas.setVisible(true);
        canvas.setLocation(0, 0);
        canvas.requestFocus();
    }

    /**
     * The way JavaDefaultSurface puts a frame on the screen.
     */
    private static void presentWithJava() {
        int[] raster = new int[WIDE * TALL];
        Arrays.fill(raster, JAVA_DREW);

        DirectColorModel model = new DirectColorModel(32, 0xFF0000, 0xFF00, 0xFF);
        DataBufferInt buffer = new DataBufferInt(raster, raster.length);
        BufferedImage image = new BufferedImage(model,
            Raster.createWritableRaster(model.createCompatibleSampleModel(WIDE, TALL), buffer, null),
            false, new Hashtable<>());

        Graphics graphics = canvas.getGraphics();
        if (graphics == null) {
            System.out.println("the canvas gave no graphics");
            return;
        }

        graphics.setClip(new Rectangle(0, 0, WIDE, TALL));
        graphics.drawImage(image, 0, 0, canvas);
        graphics.dispose();
    }

    private static void say(String what, int wanted) throws Exception {
        java.awt.Point where = canvas.getLocationOnScreen();
        BufferedImage shot = new Robot().createScreenCapture(
            new Rectangle(where.x + WIDE / 2, where.y + TALL / 2, 1, 1));
        int got = shot.getRGB(0, 0) & 0xFFFFFF;

        System.out.printf("%s: wanted %06x, screen shows %06x -> %s%n",
            what, wanted, got, near(got, wanted) ? "SHOWN" : "NOT SHOWN");
    }

    private static boolean near(int got, int wanted) {
        for (int shift = 0; shift <= 16; shift += 8) {
            if (Math.abs(((got >> shift) & 0xFF) - ((wanted >> shift) & 0xFF)) > NEAR_ENOUGH) {
                return false;
            }
        }
        return true;
    }

    private CanvasHandover() {
        /* empty */
    }
}

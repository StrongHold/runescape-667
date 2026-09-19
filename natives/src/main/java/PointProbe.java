import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Asks a software toolkit where points in the world land on the buffer, and writes down every
 * answer.
 *
 * None of this ends up on the screen, so no picture can check it. The client uses these answers to
 * place everything it draws for itself, so an answer that is one out puts a name plate one pixel
 * off every frame, and an answer that says a point is off the screen when it is not makes the
 * thing disappear.
 *
 * Each answer is driven through a spread of cameras, fields of view and clip rectangles, because
 * the answer is counted from the corner that may be drawn on and is therefore wrong in a way no
 * single clip rectangle can show.
 */
public final class PointProbe {

    private static final int WIDTH = 512;
    private static final int HEIGHT = 384;
    private static final int TURN = 16384;

    private static final List<int[]> POINTS = List.of(
        new int[] {0, 0, 0},
        new int[] {0, 0, 1},
        new int[] {0, 0, 900},
        new int[] {100, 200, 300},
        new int[] {-100, 200, -300},
        new int[] {4096, -4096, 8192},
        new int[] {1, 1, 49},
        new int[] {1, 1, 50},
        new int[] {1, 1, 51},
        new int[] {30000, 0, 600},
        new int[] {-30000, 0, 600},
        new int[] {0, 30000, 600},
        new int[] {32767, 32767, 32767},
        new int[] {-32768, 1, -1}
    );

    /** Clip rectangles as left, top, right and bottom, the last of them the whole buffer. */
    private static final List<int[]> CLIPS = List.of(
        new int[] {0, 0, WIDTH, HEIGHT},
        new int[] {40, 30, 400, 300},
        new int[] {200, 150, 260, 200}
    );

    private static final List<int[]> FIELDS = List.of(
        new int[] {WIDTH / 2, HEIGHT / 2, 512, 512},
        new int[] {0, 0, 512, 512},
        new int[] {WIDTH / 2, HEIGHT / 2, 256, 384},
        new int[] {100, 300, 1024, 64}
    );

    private static final List<int[]> PLANES = List.of(
        new int[] {50, Integer.MAX_VALUE},
        new int[] {1, 10000},
        new int[] {100, 500}
    );

    public static void main(String[] arguments) {
        var args = new ProbeArgs();

        if (!CommandLine.parsed("pointProbe", args, arguments)) {
            return;
        }

        try {
            Watchdog.arm("The point probe", 120);
            LibraryManager.putLibrary(args.library(), "sw3d");

            /*
             * A canvas, because what may be drawn on is the whole buffer only once there is a
             * buffer. With none the clip is empty, every point is answered as being off the
             * picture, and the check can no longer tell one answer from another.
             */
            var canvas = new Canvas();
            canvas.setSize(WIDTH, HEIGHT);

            var window = new Frame("point probe");
            window.add(canvas);
            window.pack();
            window.setVisible(true);
            Thread.sleep(1000);

            var toolkit = oa.create(canvas, new StubTextureSource(), WIDTH, HEIGHT);
            var lines = new ArrayList<String>();

            camerasAndFields(toolkit, lines);
            clips(toolkit, lines);
            planes(toolkit, lines);
            laidFlat(toolkit, lines);
            lines(toolkit, lines);

            Files.write(args.answers(), lines);
            System.out.println("recorded " + lines.size() + " projection answers");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Every point through every field of view, seen from several angles.
     */
    private static void camerasAndFields(Toolkit toolkit, List<String> lines) {
        var camera = toolkit.createMatrix();

        for (var field : FIELDS) {
            for (var step = 0; step < 8; step++) {
                camera.makeIdentity();
                camera.rotateAxisY(step * TURN / 8);
                camera.translate(0, 0, 700);
                toolkit.setCamera(camera);
                toolkit.la();
                toolkit.DA(field[0], field[1], field[2], field[3]);
                toolkit.f(50, Integer.MAX_VALUE);
                record(toolkit, lines, "field " + step, true, true);
            }
        }
    }

    /**
     * The same points inside clip rectangles that do not start at the corner of the buffer, which
     * is what the answer is counted from.
     */
    private static void clips(Toolkit toolkit, List<String> lines) {
        var camera = toolkit.createMatrix();
        camera.makeIdentity();
        camera.translate(0, 0, 700);
        toolkit.setCamera(camera);
        toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
        toolkit.f(50, Integer.MAX_VALUE);

        for (var clip : CLIPS) {
            toolkit.la();
            toolkit.T(clip[0], clip[1], clip[2], clip[3]);
            record(toolkit, lines, "clip " + clip[0] + "," + clip[1], true, true);
            lines.add("bounds " + answered(toolkit));
        }

        toolkit.la();
    }

    /**
     * Points on either side of the near and far planes, which decide whether a point is answered
     * at all.
     */
    private static void planes(Toolkit toolkit, List<String> lines) {
        var camera = toolkit.createMatrix();
        camera.makeIdentity();
        toolkit.setCamera(camera);
        toolkit.la();
        toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);

        for (var planes : PLANES) {
            toolkit.f(planes[0], planes[1]);
            record(toolkit, lines, "planes " + planes[0] + "," + planes[1], true, true);
            lines.add("planes back " + toolkit.i() + " " + toolkit.XA());
            lines.add("field back " + text(toolkit.Y()));
        }
    }

    /**
     * Points laid flat rather than seen in perspective, at several spreads.
     */
    private static void laidFlat(Toolkit toolkit, List<String> lines) {
        var camera = toolkit.createMatrix();
        camera.makeIdentity();
        camera.translate(0, 0, 700);
        toolkit.setCamera(camera);
        toolkit.la();
        toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
        toolkit.f(50, Integer.MAX_VALUE);

        for (var spread : new int[] {1, 2, 64, 700, 4096}) {
            var flat = new int[3];
            for (var point : POINTS) {
                toolkit.HA(point[0], point[1], point[2], spread, flat);
                lines.add("flat " + spread + " " + text(point) + " -> " + text(flat));
            }
        }
    }

    /**
     * Which edge of the picture a line in the world fell outside.
     *
     * The client asks this of an upright line at every corner of the ground before it draws, and
     * leaves a tile out when all four of its corners fell outside the same edge. An answer of
     * nothing where it should be something only costs time, but an answer of something where it
     * should be nothing takes a piece of the world away.
     */
    private static void lines(Toolkit toolkit, List<String> lines) {
        var camera = toolkit.createMatrix();
        camera.makeIdentity();
        camera.translate(0, 0, 700);
        toolkit.setCamera(camera);
        toolkit.la();
        toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
        toolkit.f(50, Integer.MAX_VALUE);

        for (var clip : CLIPS) {
            toolkit.la();
            toolkit.T(clip[0], clip[1], clip[2], clip[3]);
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);

            for (var point : POINTS) {
                var low = point[1] - 2000;
                var high = point[1] + 2000;
                lines.add("line " + clip[0] + "," + clip[1] + " " + text(point)
                    + " -> " + toolkit.JA(point[0], low, point[2], point[0], high, point[2])
                    + " " + toolkit.JA(point[0], high, point[2], point[0], low, point[2])
                    + " " + toolkit.r(point[0], low, point[2], point[0], high, point[2], 128)
                    + " " + toolkit.r(point[0], low, point[2], point[0], high, point[2], 4096));
            }
        }

        toolkit.la();
        toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
    }

    private static void record(Toolkit toolkit, List<String> lines, String what,
                               boolean bounded, boolean unbounded) {
        var landed = new int[3];

        for (var point : POINTS) {
            if (bounded) {
                toolkit.da(point[0], point[1], point[2], landed);
                lines.add(what + " seen " + text(point) + " -> " + text(landed));
            }
            if (unbounded) {
                toolkit.H(point[0], point[1], point[2], landed);
                lines.add(what + " anywhere " + text(point) + " -> " + text(landed));
            }
        }
    }

    private static String answered(Toolkit toolkit) {
        var clip = new int[4];
        toolkit.K(clip);
        return text(clip);
    }

    private static String text(int[] values) {
        var written = new StringBuilder();
        for (var value : values) {
            written.append(written.isEmpty() ? "" : " ").append(value);
        }
        return written.toString();
    }

    private PointProbe() {
        /* empty */
    }
}

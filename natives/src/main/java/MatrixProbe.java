import com.jagex.graphics.Matrix;
import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs a fixed script of matrix operations through a software toolkit and writes down what every
 * projection answered.
 *
 * Matrices need no pixels to check. They answer in integers, so the shipped toolkit and ours can
 * be compared exactly and over far more inputs than a picture could carry: every rotation the
 * client can ask for, rather than a handful that happened to be drawn.
 *
 * The toolkit is built with no canvas, which it allows, so this needs no window and no drawing
 * surface.
 */
public final class MatrixProbe {

    /**
     * The toolkit turns through 16384 steps rather than 360 degrees. A quarter turn about Z takes
     * (4096, -4096, 8192) to (4816, -3218, 8192), which puts the sine of 512 steps at 0.1951, so
     * 512 steps is a thirty second of the circle.
     */
    private static final int TURN = 16384;
    private static final int SWEEP = 64;

    private static final List<int[]> POINTS = List.of(
        new int[] {0, 0, 0},
        new int[] {1, 0, 0},
        new int[] {0, 1, 0},
        new int[] {0, 0, 1},
        new int[] {100, 200, 300},
        new int[] {-100, 200, -300},
        new int[] {4096, -4096, 8192},
        new int[] {32767, 32767, 32767},
        new int[] {-32768, 1, -1}
    );

    public static void main(String[] arguments) {
        var args = new ProbeArgs();

        if (!CommandLine.parsed("matrixProbe", args, arguments)) {
            return;
        }

        try {
            Watchdog.arm("The matrix probe", 120);
            LibraryManager.putLibrary(args.library(), "sw3d");

            var toolkit = oa.create(null, new StubTextureSource(), 512, 384);
            var lines = new ArrayList<String>();

            identity(toolkit, lines);
            chains(toolkit, lines);
            projectionVariants(toolkit, lines);
            sineTable(toolkit, lines);
            rotations(toolkit, lines);
            translations(toolkit, lines);
            cameras(toolkit, lines);
            composition(toolkit, lines);

            Files.write(args.answers(), lines);
            System.out.println("recorded " + lines.size() + " matrix answers");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The sine and cosine of every angle the toolkit can be given, read back off a rotation.
     *
     * A rotation is only as right as the table behind it, and a table that is rounded differently
     * is correct at the quarter turns and a unit out between them. One point through one rotation
     * per angle pins the whole table for the price of 16384 lines.
     */
    private static void sineTable(Toolkit toolkit, List<String> lines) {
        var matrix = toolkit.createMatrix();
        var destination = new int[4];

        for (var angle = 0; angle < TURN; angle++) {
            matrix.makeRotationZ(angle);
            matrix.project(1 << 15, 0, 0, destination);
            lines.add("table " + angle + " -> " + describe(destination));
        }
    }

    /**
     * Each way of projecting a point, each through a matrix of its own.
     *
     * The three are asked the same question separately because a matrix that is changed by being
     * asked would otherwise answer the second and third questions about a different matrix, and
     * the difference would look like a difference between the three.
     */
    private static void projectionVariants(Toolkit toolkit, List<String> lines) {
        for (var angle = 0; angle < TURN; angle += TURN / 8) {
            for (var point : POINTS) {
                var destination = new int[4];

                var forProject = toolkit.createMatrix();
                forProject.makeRotationZ(angle);
                forProject.project(point[0], point[1], point[2], destination);
                lines.add("alone " + angle + " | project " + describe(point) + " -> " + describe(destination));

                var forDirection = toolkit.createMatrix();
                forDirection.makeRotationZ(angle);
                forDirection.projectDirection(point[0], point[1], point[2], destination);
                lines.add("alone " + angle + " | projectDirection " + describe(point) + " -> " + describe(destination));

                var forRelative = toolkit.createMatrix();
                forRelative.makeRotationZ(angle);
                forRelative.projectRelative(point[0], point[1], point[2], destination);
                lines.add("alone " + angle + " | projectRelative " + describe(point) + " -> " + describe(destination));
            }
        }
    }

    /**
     * Turns applied one after another, which is how the client builds a model's transform.
     *
     * A turn applied to an identity matrix cannot say whether it replaced the matrix or combined
     * with it, because both give the same answer. Only a chain can, and the client uses chains.
     */
    private static void chains(Toolkit toolkit, List<String> lines) {
        for (var angle = TURN / 16; angle < TURN; angle += TURN / 8) {
            var chained = toolkit.createMatrix();
            chained.makeRotationZ(angle);
            chained.rotateAxisY(angle / 2);
            record(lines, "chain zy " + angle, chained);

            chained.makeRotationZ(angle);
            chained.rotateAxisY(angle / 2);
            chained.translate(10, 20, 30);
            chained.rotateAxisX(angle / 4);
            record(lines, "chain zyx " + angle, chained);

            var moved = toolkit.createMatrix();
            moved.applyTranslation(50, 60, 70);
            moved.rotateAxisY(angle);
            record(lines, "chain move then turn " + angle, moved);

            var turned = toolkit.createMatrix();
            turned.makeRotationX(angle);
            turned.rotate(angle / 2);
            record(lines, "chain x then rotate " + angle, turned);
        }
    }

    private static void identity(Toolkit toolkit, List<String> lines) {
        var matrix = toolkit.createMatrix();
        matrix.makeIdentity();
        record(lines, "identity", matrix);
    }

    private static void rotations(Toolkit toolkit, List<String> lines) {
        for (var angle = 0; angle < TURN; angle += SWEEP) {
            var made = toolkit.createMatrix();

            made.makeRotationX(angle);
            record(lines, "makeRotationX " + angle, made);

            made.makeRotationZ(angle);
            record(lines, "makeRotationZ " + angle, made);

            var turned = toolkit.createMatrix();
            turned.makeIdentity();
            turned.rotateAxisX(angle);
            record(lines, "rotateAxisX " + angle, turned);

            turned.makeIdentity();
            turned.rotateAxisY(angle);
            record(lines, "rotateAxisY " + angle, turned);

            turned.makeIdentity();
            turned.rotateAxisZ(angle);
            record(lines, "rotateAxisZ " + angle, turned);

            turned.makeIdentity();
            turned.rotate(angle);
            record(lines, "rotate " + angle, turned);
        }
    }

    private static void translations(Toolkit toolkit, List<String> lines) {
        for (var point : POINTS) {
            var moved = toolkit.createMatrix();
            moved.makeIdentity();
            moved.translate(point[0], point[1], point[2]);
            record(lines, "translate " + describe(point), moved);

            moved.makeIdentity();
            moved.applyTranslation(point[0], point[1], point[2]);
            record(lines, "applyTranslation " + describe(point), moved);
        }
    }

    /**
     * Cameras, with one axis turned at a time before any combination of them.
     *
     * A camera turns about three axes and the order they are applied in cannot be read off a case
     * where all three are turned together, because several orders fit. One axis at a time pins
     * each turn, and the combinations then pin the order.
     */
    private static void cameras(Toolkit toolkit, List<String> lines) {
        for (var angle = 0; angle < TURN; angle += TURN / 8) {
            var aboutX = toolkit.createMatrix();
            aboutX.createCamera(100, -200, 300, angle, 0, 0);
            record(lines, "cameraX " + angle, aboutX);

            var aboutY = toolkit.createMatrix();
            aboutY.createCamera(100, -200, 300, 0, angle, 0);
            record(lines, "cameraY " + angle, aboutY);

            var aboutZ = toolkit.createMatrix();
            aboutZ.createCamera(100, -200, 300, 0, 0, angle);
            record(lines, "cameraZ " + angle, aboutZ);

            var together = toolkit.createMatrix();
            together.createCamera(100, -200, 300, angle, angle / 2, angle / 4);
            record(lines, "camera " + angle, together);
        }
    }

    /**
     * One matrix applied to another, which is where an operation built the right transform but
     * multiplied it on the wrong side.
     */
    private static void composition(Toolkit toolkit, List<String> lines) {
        for (var angle = 0; angle < TURN; angle += TURN / 16) {
            var first = toolkit.createMatrix();
            first.makeRotationX(angle);

            var second = toolkit.createMatrix();
            second.makeRotationZ(TURN / 4);
            second.translate(10, 20, 30);

            first.apply(second);
            record(lines, "rotateX " + angle + " then rotateZ and translate", first);

            var copied = first.copy();
            record(lines, "copy of " + angle, copied);
        }
    }

    /**
     * Every way the matrix can be asked about a point, for every point.
     */
    private static void record(List<String> lines, String what, Matrix matrix) {
        var destination = new int[4];

        for (var point : POINTS) {
            matrix.project(point[0], point[1], point[2], destination);
            lines.add(what + " | project " + describe(point) + " -> " + describe(destination));

            matrix.projectDirection(point[0], point[1], point[2], destination);
            lines.add(what + " | projectDirection " + describe(point) + " -> " + describe(destination));

            matrix.projectRelative(point[0], point[1], point[2], destination);
            lines.add(what + " | projectRelative " + describe(point) + " -> " + describe(destination));
        }

        matrix.project(destination);
        lines.add(what + " | project -> " + describe(destination));

        /*
         * The client passes an array of three here as well as one of four, and the toolkit reads
         * four numbers out of whichever it is given. It gets away with reading past the end of a
         * three because it reads through a pinned pointer, and it gets away with the fourth number
         * being whatever happened to be there because it never adds it in. Both sizes are asked
         * for, because an implementation that reads the array properly throws on the smaller one.
         */
        var three = new int[] {destination[0], destination[1], destination[2]};
        matrix.project(three);
        lines.add(what + " | project into three -> " + describe(three));
    }

    private static String describe(int[] values) {
        var text = new StringBuilder();
        for (var value : values) {
            text.append(text.isEmpty() ? "" : ",").append(value);
        }
        return text.toString();
    }

    private MatrixProbe() {
        /* empty */
    }
}

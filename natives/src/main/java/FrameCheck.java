import com.beust.jcommander.Parameter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 * Checks the frames both toolkits drew from the same scenes.
 *
 * Two things can be wrong, and they fail in different ways. A toolkit that leaves state behind
 * draws a scene differently the second time, which shows as a scene disagreeing with itself. A
 * toolkit that rasterises differently disagrees with the other implementation. Both are checked
 * here, because a frame that merely looks right is not the same as one that is right and a
 * rasteriser has far too many ways to be nearly correct.
 *
 * Every scene is checked before anything is reported, so one report names everything that broke
 * rather than only the first thing. A scene that differs also gets an image written showing
 * where, because a coordinate and two colours rarely say what went wrong on their own.
 */
public final class FrameCheck {

    private static final int CHANNELS = 0xFFFFFF;
    private static final int SAMPLES = 4;

    /** Pixels that agree are dimmed rather than dropped, so a difference can be placed. */
    private static final int DIM = 4;

    private static final int MARK = 0xFFFF0000;

    /** What an undrawn pixel is, which is what the harness clears a frame to. */
    private static final int BLANK = Scene.CLEAR_COLOUR;

    private record Frames(String label, Path directory, List<String> scenes) {

        static Frames at(String label, Path directory) throws IOException {
            var scenes = Files.readAllLines(directory.resolve("scenes.txt"));
            if (scenes.isEmpty()) {
                throw new IllegalStateException("No scenes were drawn into " + directory + ".");
            }
            return new Frames(label, directory, scenes);
        }

        Path frame(int index) {
            return directory.resolve(String.format("frame-%04d.png", index));
        }
    }

    public static final class Args implements Helpable {

        @Parameter(names = "--shipped", description = "The frames the shipped toolkit drew", required = true)
        private Path shipped;

        @Parameter(names = "--ours", description = "The frames our toolkit drew", required = true)
        private Path ours;

        @Parameter(names = "--marks", description = "The directory to write marked differences into", required = true)
        private Path marks;

        @Parameter(names = "--outstanding", description = "How far each unfinished scene is allowed to be", required = true)
        private Path outstanding;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) {
        var args = new Args();

        if (!CommandLine.parsed("verifyToolkit", args, arguments)) {
            return;
        }

        try {
            var shipped = Frames.at("the shipped toolkit", args.shipped);
            var ours = Frames.at("our toolkit", args.ours);
            var marks = args.marks;
            Files.createDirectories(marks);
            var allowed = Allowance.read(args.outstanding);

            var report = new ArrayList<String>();
            report.addAll(checkRepeatsAgree(shipped, marks));
            report.addAll(checkRepeatsAgree(ours, marks));
            report.addAll(checkImplementationsAgree(shipped, ours, marks));

            measureOutstanding(shipped, ours).forEach(System.out::println);
            report.addAll(checkOutstandingHeldGround(shipped, ours, allowed));

            if (report.isEmpty()) {
                System.out.println(shipped.scenes().size() + " frames identical to the shipped toolkit");
            } else {
                report.add("Marked images are in " + marks + ".");
                report.forEach(System.out::println);
            }

            System.exit(report.isEmpty() ? 0 : 1);
        } catch (Exception failure) {
            System.out.println(failure.getMessage());
            System.exit(1);
        }
    }

    /**
     * A scene drawn twice has to come out the same both times. When it does not, the toolkit set
     * something while drawing it and did not put it back.
     */
    private static List<String> checkRepeatsAgree(Frames frames, Path marks) throws IOException {
        var report = new ArrayList<String>();

        for (var index = 1; index < frames.scenes().size(); index++) {
            var scene = frames.scenes().get(index);
            if (!scene.equals(frames.scenes().get(index - 1)) || outstanding(scene)) {
                continue;
            }

            var difference = compare(
                frames.frame(index - 1), frames.frame(index), marks,
                scene + " drawn twice by " + frames.label(),
                "the first time", "the second time");

            difference.ifPresent(report::add);
        }

        return report;
    }

    private static List<String> checkImplementationsAgree(Frames shipped, Frames ours, Path marks)
        throws IOException {
        if (!shipped.scenes().equals(ours.scenes())) {
            return List.of("The two toolkits were given different scenes.");
        }

        var report = new ArrayList<String>();

        for (var index = 0; index < shipped.scenes().size(); index++) {
            var scene = shipped.scenes().get(index);
            if (outstanding(scene)) {
                continue;
            }

            if (!scene.contains("(empty on purpose)")) {
                emptiness(shipped.frame(index), scene).ifPresent(report::add);
            }

            compare(shipped.frame(index), ours.frame(index), marks, scene, "the shipped toolkit", "we")
                .ifPresent(report::add);
        }

        return report;
    }

    /**
     * Complains about a scene that covers no pixels at all.
     *
     * Two toolkits that both draw nothing agree, so such a scene passes while checking nothing.
     * Three of them have been found that way, each after resting work on it for some time, so it
     * is worth a check of its own: a scene that is finished has to put something on the screen.
     */
    private static Optional<String> emptiness(Path frame, String scene) throws IOException {
        var picture = ImageIO.read(frame.toFile());

        for (var y = 0; y < picture.getHeight(); y++) {
            for (var x = 0; x < picture.getWidth(); x++) {
                if ((picture.getRGB(x, y) & 0xFFFFFF) != (BLANK & 0xFFFFFF)) {
                    return Optional.empty();
                }
            }
        }

        return Optional.of(scene + " covers no pixels at all, so it checks nothing. Either give it"
            + " something to draw or mark it as not yet drawn.");
    }

    /**
     * Says how close a scene that is still being worked on has got.
     *
     * A scene belonging to an unfinished native cannot pass or fail, but the distance between the
     * two pictures is the only measure of progress there is, so it is reported rather than
     * skipped. Pixels that only one side drew are counted apart from pixels both drew in different
     * shades, because the two mean different things: the first is a face in the wrong place and
     * the second is a face in the right place with the wrong light on it.
     */
    private static List<String> measureOutstanding(Frames shipped, Frames ours) throws IOException {
        /*
         * A scene that is checked reports its distance through its failure instead, so measuring
         * it again here would say the same thing twice.
         */
        var report = new ArrayList<String>();
        var seen = new ArrayList<String>();

        for (var index = 0; index < shipped.scenes().size(); index++) {
            var scene = shipped.scenes().get(index);
            if (!outstanding(scene) || seen.contains(scene) || !Files.isRegularFile(ours.frame(index))) {
                continue;
            }
            seen.add(scene);
            report.add(scene + ": " + distance(shipped.frame(index), ours.frame(index)));
        }

        return report;
    }

    /**
     * How far one unfinished scene is allowed to be from the shipped toolkit.
     *
     * A scene that cannot pass would otherwise go unchecked, and a change could take it from a few
     * pixels out to half the picture without anything noticing. What each one came to when it was
     * last looked at is written down instead, and a scene that grows past what is written down
     * fails the same way a finished one does.
     */
    private record Allowance(int onlyShipped, int onlyOurs, int shaded) {

        static Map<String, Allowance> read(Path path) throws IOException {
            var held = new TreeMap<String, Allowance>();

            for (var line : Files.readAllLines(path)) {
                var said = line.trim();
                if (said.isEmpty() || said.startsWith("#")) {
                    continue;
                }

                var halves = said.split("=", 2);
                var counts = halves[1].split(",");
                held.put(halves[0].trim(), new Allowance(
                    Integer.parseInt(counts[0].trim()),
                    Integer.parseInt(counts[1].trim()),
                    Integer.parseInt(counts[2].trim())));
            }

            return held;
        }

        boolean grewPast(Allowance now) {
            return now.onlyShipped > onlyShipped || now.onlyOurs > onlyOurs || now.shaded > shaded;
        }

        boolean shrankFrom(Allowance now) {
            return now.onlyShipped < onlyShipped || now.onlyOurs < onlyOurs || now.shaded < shaded;
        }

        @Override
        public String toString() {
            return onlyShipped + ", " + onlyOurs + ", " + shaded;
        }
    }

    /**
     * Checks that no unfinished scene has given back ground it had already gained.
     */
    private static List<String> checkOutstandingHeldGround(Frames shipped, Frames ours,
            Map<String, Allowance> allowed) throws IOException {
        var report = new ArrayList<String>();
        var seen = new ArrayList<String>();

        for (var index = 0; index < shipped.scenes().size(); index++) {
            var scene = shipped.scenes().get(index);
            if (!outstanding(scene) || seen.contains(scene)
                || !Files.isRegularFile(ours.frame(index))) {
                continue;
            }

            seen.add(scene);
            var title = scene.substring(0, scene.indexOf(" (outstanding)"));
            var now = measure(shipped.frame(index), ours.frame(index));
            var was = allowed.get(title);

            if (was == null) {
                report.add(title + " is outstanding and nothing says how far out it is allowed to"
                    + " be. Write " + title + " = " + now + " into the record.");
            } else if (was.grewPast(now)) {
                report.add(title + " was allowed to be " + was + " out and is now " + now + ".");
            } else if (was.shrankFrom(now)) {
                System.out.println(title + " came in from " + was + " to " + now
                    + ", so the record is out of date. Bring it down to hold the ground.");
            }
        }

        for (var title : allowed.keySet()) {
            if (!seen.contains(title + " (outstanding)")) {
                report.add(title + " is in the record of unfinished scenes but was not drawn as"
                    + " one. Take it out of the record.");
            }
        }

        return report;
    }

    /**
     * How far apart two pictures of the same scene are, counted the three ways that mean different
     * things: a face only one side drew, a face only the other drew, and a face both drew in
     * different shades.
     */
    private static Allowance measure(Path expected, Path actual) throws IOException {
        var left = ImageIO.read(expected.toFile());
        var right = ImageIO.read(actual.toFile());

        var onlyExpected = 0;
        var onlyActual = 0;
        var shade = 0;

        for (var y = 0; y < left.getHeight(); y++) {
            for (var x = 0; x < left.getWidth(); x++) {
                var wanted = left.getRGB(x, y) & CHANNELS;
                var got = right.getRGB(x, y) & CHANNELS;

                if (wanted != got) {
                    if (got == BLANK) {
                        onlyExpected++;
                    } else if (wanted == BLANK) {
                        onlyActual++;
                    } else {
                        shade++;
                    }
                }
            }
        }

        return new Allowance(onlyExpected, onlyActual, shade);
    }

    private static String distance(Path expected, Path actual) throws IOException {
        var left = ImageIO.read(expected.toFile());
        var right = ImageIO.read(actual.toFile());

        var drawn = 0;
        var onlyExpected = 0;
        var onlyActual = 0;
        var shade = 0;
        var worst = 0;
        var worstX = -1;
        var worstY = -1;

        for (var y = 0; y < left.getHeight(); y++) {
            for (var x = 0; x < left.getWidth(); x++) {
                var wanted = left.getRGB(x, y) & CHANNELS;
                var got = right.getRGB(x, y) & CHANNELS;

                if (wanted != BLANK || got != BLANK) {
                    drawn++;
                }

                if (wanted != got) {
                    if (got == BLANK) {
                        onlyExpected++;
                    } else if (wanted == BLANK) {
                        onlyActual++;
                    } else {
                        shade++;
                        if (apart(wanted, got) > worst) {
                            worst = apart(wanted, got);
                            worstX = x;
                            worstY = y;
                        }
                    }
                }
            }
        }

        return ("%d drawn, %d the shipped toolkit drew and we did not, %d we drew and it did not, "
            + "%d shaded differently, worst part off by %d at %d,%d")
            .formatted(drawn, onlyExpected, onlyActual, shade, worst, worstX, worstY);
    }

    /** How far apart two colours are, measured by the part of them that differs most. */
    private static int apart(int left, int right) {
        var most = 0;
        for (var shift = 0; shift < 24; shift += 8) {
            most = Math.max(most, Math.abs((left >> shift & 0xFF) - (right >> shift & 0xFF)));
        }
        return most;
    }

    private static Optional<String> compare(Path expected, Path actual, Path marks, String what,
                                            String expectedBy, String actualBy) throws IOException {
        if (!Files.isRegularFile(actual)) {
            return Optional.of(what + ": there is no " + actual.getFileName() + ".");
        }

        var left = ImageIO.read(expected.toFile());
        var right = ImageIO.read(actual.toFile());

        if (left.getWidth() != right.getWidth() || left.getHeight() != right.getHeight()) {
            return Optional.of("%s: %s drew %dx%d where %s drew %dx%d.".formatted(
                what, actualBy, right.getWidth(), right.getHeight(),
                expectedBy, left.getWidth(), left.getHeight()));
        }

        var marked = new BufferedImage(left.getWidth(), left.getHeight(), BufferedImage.TYPE_INT_RGB);
        var samples = new ArrayList<String>();
        var count = 0;
        var minX = left.getWidth();
        var minY = left.getHeight();
        var maxX = -1;
        var maxY = -1;

        for (var y = 0; y < left.getHeight(); y++) {
            for (var x = 0; x < left.getWidth(); x++) {
                var wanted = left.getRGB(x, y) & CHANNELS;
                var got = right.getRGB(x, y) & CHANNELS;

                if (wanted == got) {
                    marked.setRGB(x, y, dim(wanted));
                } else {
                    marked.setRGB(x, y, MARK);
                    count++;
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);

                    if (samples.size() < SAMPLES) {
                        samples.add("%d,%d %s drew %06x and %s drew %06x"
                            .formatted(x, y, expectedBy, wanted, actualBy, got));
                    }
                }
            }
        }

        if (count == 0) {
            return Optional.empty();
        }

        ImageIO.write(marked, "png", marks.resolve(what.replace(' ', '-') + ".png").toFile());

        return Optional.of("%s: %d of %d pixels differ, within %d,%d to %d,%d.%n    %s%n    %s".formatted(
            what, count, left.getWidth() * left.getHeight(), minX, minY, maxX, maxY,
            distance(expected, actual),
            String.join(System.lineSeparator() + "    ", samples)));
    }

    /**
     * A scene the capture marked as belonging to a native that is not written yet.
     */
    private static boolean outstanding(String scene) {
        return scene.endsWith("(outstanding)");
    }

    private static int dim(int colour) {
        return (((colour >> 16) & 0xFF) / DIM << 16)
            | (((colour >> 8) & 0xFF) / DIM << 8)
            | ((colour & 0xFF) / DIM);
    }

    private FrameCheck() {
        /* empty */
    }
}

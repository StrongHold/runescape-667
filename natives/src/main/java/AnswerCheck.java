import com.beust.jcommander.Parameter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Compares what a shipped library answered against what ours answered, line for line.
 *
 * Not everything a toolkit does ends up on the screen. A matrix, a projected point and a clip
 * rectangle are all answered as integers, which can be compared exactly and over far more inputs
 * than a picture could carry: every rotation the client can ask for rather than the handful that
 * happened to be drawn.
 *
 * There is no tolerance here and none is wanted. A matrix that is one out projects a model one
 * pixel out, and a rotation built the wrong way round is correct at the quarter turns and wrong
 * everywhere between them.
 *
 * The two files and a name for what is in them are the arguments.
 */
public final class AnswerCheck {

    private static final int SAMPLES = 8;

    public static final class Args implements Arguments {

        @Parameter(names = "--shipped", description = "The answers the shipped library gave", required = true)
        private Path shipped;

        @Parameter(names = "--ours", description = "The answers our library gave", required = true)
        private Path ours;

        @Parameter(names = "--what", description = "What the two libraries were asked about", required = true)
        private String what;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) {
        var parsed = CommandLine.parse("verifyAnswers", new Args(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(Args args) {
        try {
            var shipped = Files.readAllLines(args.shipped);
            var ours = Files.readAllLines(args.ours);
            var what = args.what;

            if (shipped.isEmpty()) {
                throw new IllegalStateException("The shipped library answered nothing.");
            }

            if (shipped.size() != ours.size()) {
                throw new IllegalStateException(
                    "The shipped library gave %d answers and we gave %d."
                        .formatted(shipped.size(), ours.size()));
            }

            var differences = new ArrayList<String>();
            for (var line = 0; line < shipped.size(); line++) {
                if (!shipped.get(line).equals(ours.get(line))) {
                    if (differences.size() < SAMPLES) {
                        differences.add("    shipped: " + shipped.get(line));
                        differences.add("    ours:    " + ours.get(line));
                    }
                }
            }

            var wrong = (int) IntStream.range(0, shipped.size())
                .filter(line -> !shipped.get(line).equals(ours.get(line)))
                .count();

            if (wrong == 0) {
                System.out.println(shipped.size() + " " + what + " identical to the shipped library");
                System.exit(0);
            }

            System.out.println("%d of %d %s differ.".formatted(wrong, shipped.size(), what));
            differences.forEach(System.out::println);
            System.exit(1);
        } catch (Exception failure) {
            System.out.println(failure.getMessage());
            System.exit(1);
        }
    }

    private AnswerCheck() {
        /* empty */
    }
}

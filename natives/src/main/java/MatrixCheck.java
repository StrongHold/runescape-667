import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Compares what the shipped toolkit's matrices answered against what ours answered.
 *
 * Every answer is an integer, so there is no tolerance here and none is wanted. A matrix that is
 * one out projects a model one pixel out, and a rotation built the wrong way round is correct at
 * the quarter turns and wrong everywhere between them.
 */
public final class MatrixCheck {

    private static final int SAMPLES = 8;

    public static void main(String[] args) {
        try {
            var shipped = Files.readAllLines(Path.of(args[0]));
            var ours = Files.readAllLines(Path.of(args[1]));

            if (shipped.isEmpty()) {
                throw new IllegalStateException("The shipped toolkit answered nothing.");
            }

            if (shipped.size() != ours.size()) {
                throw new IllegalStateException(
                    "The shipped toolkit gave %d answers and we gave %d."
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

            var wrong = (int) java.util.stream.IntStream.range(0, shipped.size())
                .filter(line -> !shipped.get(line).equals(ours.get(line)))
                .count();

            if (wrong == 0) {
                System.out.println(shipped.size() + " matrix answers identical to the shipped toolkit");
                System.exit(0);
            }

            System.out.println("%d of %d matrix answers differ.".formatted(wrong, shipped.size()));
            differences.forEach(System.out::println);
            System.exit(1);
        } catch (Exception failure) {
            System.out.println(failure.getMessage());
            System.exit(1);
        }
    }

    private MatrixCheck() {
        /* empty */
    }
}

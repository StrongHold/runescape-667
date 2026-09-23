import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * A file that a toolkit writes down what it did into, a line at a time, while the harness draws.
 *
 * Both toolkits can be made to write one: ours when it is started with SW3D_TRACE naming the file,
 * and the shipped one when the watcher is inserted into it. The harness writes a line into the same
 * file before each scene it draws, so what was written can be told apart by the scene that caused
 * it. Every writer appends, so the lines land in the order they were written.
 */
public final class Trace {

    private static final String VARIABLE = "SW3D_TRACE";

    private static final String SCENE = "SCENE";

    /**
     * Writes down that a scene is about to be drawn, when there is a trace to write it into.
     */
    public static void markScene(String title, int repeat) {
        var named = System.getenv(VARIABLE);

        if (named != null && !named.isEmpty()) {
            try {
                var line = SCENE + " " + title + " " + repeat + "\n";
                Files.writeString(Path.of(named), line, StandardCharsets.US_ASCII,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException failure) {
                throw new UncheckedIOException(failure);
            }
        }
    }

    /**
     * The lines written while one scene was drawn the given time, without the line that marks it.
     */
    public static List<String> linesOf(Path trace, String title, int repeat) throws IOException {
        var marker = SCENE + " " + title + " " + repeat;
        var lines = new ArrayList<String>();
        var inside = false;

        try (var reading = Files.lines(trace, StandardCharsets.US_ASCII)) {
            for (var line : (Iterable<String>) reading::iterator) {
                if (line.startsWith(SCENE + " ")) {
                    inside = line.equals(marker);
                } else if (inside) {
                    lines.add(line);
                }
            }
        }

        if (lines.isEmpty()) {
            throw new IllegalStateException(
                "Nothing was written into " + trace + " while " + title + " was drawn.");
        }
        return List.copyOf(lines);
    }

    private Trace() {
        /* empty */
    }
}

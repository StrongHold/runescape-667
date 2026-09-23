import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The record {@link NativeTrace} writes: one line for each call into the native toolkit.
 *
 * A line names the method, then gives every argument and the answer. Numbers are written so that
 * two runs can be compared to the bit: a float is written with its bits beside it. Arrays are
 * written out in full, up to a limit, because what the client hands the ground is mostly arrays.
 * An object the library holds, such as the toolkit or a shadow, is written as its class alone,
 * because where it sits in memory says nothing and differs every run. A long too big for an int is
 * the address of something the library holds, for the same reason, and is written as a handle.
 *
 * A method called every frame would fill the disk, so each method is written down only so many
 * times. The calls that build the ground are made once, when the area loads, and all fit.
 */
public final class NativeLog {

    /**
     * How many calls of one method are written down, and how many numbers of one array.
     */
    private static final int CALLS = Integer.getInteger("nativetrace.calls", 20000);
    private static final int NUMBERS = Integer.getInteger("nativetrace.numbers", 1 << 16);

    private static final Map<String, Integer> COUNTS = new HashMap<>();
    private static BufferedWriter out;

    static void open(String file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Name the file to write the record into, as -javaagent:native-trace.jar=<file>.");
        }

        try {
            out = Files.newBufferedWriter(Path.of(file), StandardCharsets.UTF_8);
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(NativeLog::close));

        var flusher = new Thread(NativeLog::flushEverySecond, "native trace flusher");
        flusher.setDaemon(true);
        flusher.start();
    }

    /**
     * Writes out what is held every second, so that a client that is stopped rather than closed
     * still leaves its record behind.
     */
    private static void flushEverySecond() {
        var running = true;
        while (running) {
            try {
                Thread.sleep(1000);
                flush();
            } catch (InterruptedException stopped) {
                running = false;
            }
        }
    }

    private static synchronized void flush() {
        try {
            out.flush();
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
    }

    public static synchronized void record(String method, Object[] arguments, Object answer) {
        var count = COUNTS.merge(method, 1, Integer::sum);
        if (count > CALLS) {
            return;
        }

        var line = new StringBuilder(method).append('(');
        for (var at = 0; at < arguments.length; at++) {
            if (at > 0) {
                line.append(", ");
            }
            write(line, arguments[at]);
        }
        line.append(')');
        if (answer != null) {
            line.append(" = ");
            write(line, answer);
        }
        line.append('\n');

        try {
            out.write(line.toString());
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
    }

    private static void write(StringBuilder line, Object value) {
        if (value == null) {
            line.append("null");
        } else if (value instanceof Float) {
            var number = (Float) value;
            line.append(number).append('#').append(Integer.toHexString(Float.floatToRawIntBits(number)));
        } else if (value instanceof Double) {
            var number = (Double) value;
            line.append(number).append('#').append(Long.toHexString(Double.doubleToRawLongBits(number)));
        } else if (value instanceof Long && ((Long) value > Integer.MAX_VALUE || (Long) value < Integer.MIN_VALUE)) {
            line.append("handle");
        } else if (value instanceof Number || value instanceof Boolean) {
            line.append(value);
        } else if (value instanceof Character) {
            line.append((int) (Character) value);
        } else if (value instanceof String) {
            line.append('"').append(value).append('"');
        } else if (value.getClass().isArray()) {
            array(line, value);
        } else {
            line.append(value.getClass().getName());
        }
    }

    private static void array(StringBuilder line, Object array) {
        var length = Array.getLength(array);
        line.append('[').append(length).append(':');
        var shown = Math.min(length, NUMBERS);
        for (var at = 0; at < shown; at++) {
            line.append(at == 0 ? " " : ",");
            write(line, Array.get(array, at));
        }
        if (shown < length) {
            line.append(",...");
        }
        line.append(']');
    }

    private static synchronized void close() {
        try {
            out.close();
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
    }

    private NativeLog() {
        /* empty */
    }
}

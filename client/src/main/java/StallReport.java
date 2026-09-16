import com.jagex.graphics.Toolkit;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Prints what every thread is doing, over and over, so that a client that stops responding says
 * where it stopped.
 *
 * A client that freezes gives nothing away on its own: the window stops repainting and there is
 * no exception to read. Running with this on turns that into a stack every few seconds, and the
 * frame that appears in all of them is the one to look at.
 *
 * Each report opens with the renderer that is drawing. A client that meets an exception while
 * drawing puts itself back on the Java renderer and carries on, so a client that is merely slow
 * from that point on looks the same from outside as one that is stuck.
 *
 * It is off unless asked for, because it prints a great deal:
 *
 *     ./gradlew client:run -Dclient.stalls=5 ...
 */
public final class StallReport {

    private static final String EVERY = "client.stalls";

    /**
     * Starts printing if the property asks for it, and does nothing otherwise.
     */
    public static void watchIfAsked() {
        var asked = System.getProperty(EVERY);
        if (asked == null || asked.isEmpty()) {
            return;
        }

        var seconds = Integer.parseInt(asked);
        var reporter = new Thread(() -> report(seconds), "stall report");
        reporter.setDaemon(true);
        reporter.start();

        System.out.println("stall report: every " + seconds + " seconds");
    }

    private static void report(int seconds) {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                return;
            }

            print();
        }
    }

    private static void print() {
        var threads = ManagementFactory.getThreadMXBean();
        var report = new StringBuilder("\n--- what every thread is doing ---\n")
            .append("drawing with: ")
            .append(renderer())
            .append('\n');

        for (var info : threads.dumpAllThreads(false, false)) {
            if (uninteresting(info.getThreadName())) {
                continue;
            }

            report.append(info.getThreadName())
                .append(' ')
                .append(info.getThreadState())
                .append('\n');

            Arrays.stream(info.getStackTrace())
                .limit(24)
                .forEach(frame -> report.append("    ").append(frame).append('\n'));
        }

        System.out.println(report);
    }

    private static String renderer() {
        var toolkit = Toolkit.active;
        if (toolkit == null) {
            return "nothing yet";
        } else {
            return toolkit.getClass().getName();
        }
    }

    /**
     * Threads that are always waiting for something and never the reason a frame does not arrive.
     */
    private static boolean uninteresting(String name) {
        return name.startsWith("Reference Handler")
            || name.startsWith("Finalizer")
            || name.startsWith("Signal Dispatcher")
            || name.startsWith("Notification Thread")
            || name.startsWith("Common-Cleaner")
            || name.equals("stall report");
    }

    private StallReport() {
        /* empty */
    }
}

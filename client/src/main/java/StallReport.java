import com.jagex.game.runetek6.client.GameShell;
import com.jagex.graphics.Toolkit;

import java.awt.Component;
import java.awt.Graphics;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
 *
 * A freeze too short to catch by hand is a different problem, and blind reports every few seconds
 * will not catch one that lasts half a second. For that, ask to be told when the thread that draws
 * stops moving for longer than it should:
 *
 *     ./gradlew client:run -Dclient.freezes=250 ...
 *
 * That watches where that thread is many times a second and prints once each time it has stood
 * still for longer than the number of milliseconds given. What it prints is where it stood.
 */
public final class StallReport {

    private static final String EVERY = "client.stalls";

    private static final String FREEZES = "client.freezes";

    /** How often every thread is looked at, which bounds how short a freeze can be seen. */
    private static final long LOOK_EVERY_MILLISECONDS = 20L;

    /** How much of a stack is kept, which is enough to tell one caller of a thing from another. */
    private static final int DEEP_ENOUGH = 3;

    /** How many of the places found are worth printing. */
    private static final int WORTH_SAYING = 12;

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

    /**
     * Says which renderer is drawing, and says so again every time it changes.
     *
     * The client puts itself back on the Java renderer whenever anything goes wrong while it is
     * building a toolkit or drawing a frame, and one of the paths that does it says nothing at
     * all: the failure is caught, the renderer is swapped, and the client carries on. From
     * outside, a client that has quietly dropped onto the Java renderer looks like a client whose
     * own renderer has become slow, and every reading taken afterwards is taken from the wrong
     * one.
     *
     * This is on always. It prints once at the start and once per change, and a renderer that
     * changes often is the thing worth knowing about anyway.
     */
    public static void watchTheRenderer() {
        var watcher = new Thread(StallReport::followTheRenderer, "renderer watch");
        watcher.setDaemon(true);
        watcher.start();
    }

    /**
     * How often the renderer is looked at.
     *
     * Often enough to still be inside the change when it is noticed. Swapping the renderer
     * rebuilds every font and sprite the client holds, which takes long enough to catch, and
     * catching it is the whole point: the swap says nothing about who asked for it, and who asked
     * for it is the fault.
     */
    private static final long WATCH_EVERY_MILLISECONDS = 1L;

    private static void followTheRenderer() {
        var before = "";

        while (true) {
            var now = renderer();
            if (!now.equals(before)) {
                var changed = !before.isEmpty();
                System.out.println("client: drawing with " + now + ", " + canvasState());
                before = now;
                if (changed) {
                    sayWhoAsked();
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(WATCH_EVERY_MILLISECONDS);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * Prints what the threads that draw are in the middle of, which is where the swap came from.
     */
    /**
     * What the game is drawing into.
     *
     * The Java renderer puts a frame on the screen by asking the canvas for a graphics of its own,
     * which a canvas that has not been given anything to draw on cannot answer. Every other
     * renderer here goes straight to the surface underneath and never asks. So a canvas that is
     * not displayable is invisible to one renderer and harmless to the rest, and the game swaps
     * its canvas every time it changes renderer.
     */
    private static String canvasState() {
        var canvas = GameShell.canvas;
        if (canvas == null) {
            return "no canvas";
        }

        var size = canvas.getSize();
        return "canvas " + size.width + "x" + size.height
            + (canvas.isDisplayable() ? " displayable" : " NOT displayable")
            + (canvas.isShowing() ? " showing" : " NOT showing")
            + (canvas.isValid() ? " valid" : " NOT valid")
            + ", " + graphicsState(canvas)
            + ", parent " + (canvas.getParent() == null ? "none" : canvas.getParent().getClass().getName());
    }

    /**
     * Whether the canvas will hand out something to draw on.
     *
     * This is the one question that decides whether a frame can reach the screen at all, and the
     * renderer that asks it throws the answer away: it catches the failure, asks for a repaint,
     * and tries again next frame. Asking it here is the only way to see the answer.
     */
    private static String graphicsState(Component canvas) {
        Graphics graphics = null;
        try {
            graphics = canvas.getGraphics();
            if (graphics == null) {
                return "gives no graphics";
            }
            return "gives graphics, clip " + graphics.getClipBounds();
        } catch (RuntimeException refused) {
            return "refuses graphics: " + refused;
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
    }

    private static void sayWhoAsked() {
        var threads = ManagementFactory.getThreadMXBean();
        var report = new StringBuilder("client: the renderer changed here\n")
            .append("    ").append(canvasState()).append('\n');

        for (var info : threads.dumpAllThreads(false, false)) {
            var stack = info.getStackTrace();
            if (uninteresting(info.getThreadName()) || stack.length == 0) {
                continue;
            }
            if (info.getThreadState() == Thread.State.WAITING
                || info.getThreadState() == Thread.State.TIMED_WAITING) {
                continue;
            }

            report.append(info.getThreadName()).append('\n');
            Arrays.stream(stack)
                .limit(WORTH_SAYING * 2)
                .forEach(frame -> report.append("    ").append(frame).append('\n'));
        }

        System.out.println(report);
    }

    /**
     * Starts taking samples if the property asks for it, and does nothing otherwise.
     */
    public static void watchForFreezesIfAsked() {
        var asked = System.getProperty(FREEZES);
        if (asked == null || asked.isEmpty()) {
            return;
        }

        var seconds = Integer.parseInt(asked);
        var watcher = new Thread(() -> sample(seconds), "freeze report");
        watcher.setDaemon(true);
        watcher.start();

        System.out.println("freeze report: what every thread is doing, counted every "
            + seconds + " seconds");
    }

    /**
     * Counts where each thread is, many times a second, and says what it found.
     *
     * A freeze that lasts half a second is not a thread standing still: it is a thread busy doing
     * one thing for half a second, and its stack changes the whole time it does it. Waiting for a
     * stack to stop moving never catches that. Counting where the threads are does, because
     * whatever ate the half second is in far more of the samples than anything else.
     *
     * What is counted is the top of the stack and the two frames below it, which is enough to tell
     * one caller of a thing from another without making every sample its own entry.
     */
    private static void sample(int seconds) {
        var threads = ManagementFactory.getThreadMXBean();
        var seen = new HashMap<String, Integer>();
        var taken = 0;
        var since = System.currentTimeMillis();

        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(LOOK_EVERY_MILLISECONDS);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                return;
            }

            for (var info : threads.dumpAllThreads(false, false)) {
                if (uninteresting(info.getThreadName()) || info.getStackTrace().length == 0) {
                    continue;
                }
                if (info.getThreadState() == Thread.State.WAITING
                    || info.getThreadState() == Thread.State.TIMED_WAITING) {
                    continue;
                }

                seen.merge(where(info), 1, Integer::sum);
            }

            taken++;
            var now = System.currentTimeMillis();
            if (now - since < seconds * 1000L) {
                continue;
            }

            say(taken, seen);
            seen.clear();
            taken = 0;
            since = now;
        }
    }

    private static String where(ThreadInfo info) {
        var stack = info.getStackTrace();
        var name = new StringBuilder(info.getThreadName()).append(": ");
        for (var deep = 0; deep < DEEP_ENOUGH && deep < stack.length; deep++) {
            if (deep > 0) {
                name.append(" <- ");
            }
            name.append(stack[deep].getClassName())
                .append('.')
                .append(stack[deep].getMethodName());
        }
        return name.toString();
    }

    private static void say(int taken, Map<String, Integer> seen) {
        var report = new StringBuilder("\n--- where the time went over ")
            .append(taken)
            .append(" looks, drawing with ")
            .append(renderer())
            .append(" ---\n");

        seen.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(WORTH_SAYING)
            .forEach(each -> report.append(String.format("%5.1f%%  %s%n",
                100.0 * each.getValue() / taken, each.getKey())));

        System.out.println(report);
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
            || name.equals("stall report")
            || name.equals("freeze report")
            || name.equals("renderer watch");
    }

    private StallReport() {
        /* empty */
    }
}

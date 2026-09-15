/**
 * Ends the process if the work it was given takes far longer than it ever should.
 *
 * These harnesses open a window, which keeps the virtual machine alive on its own. Anything that
 * stops them reaching their exit leaves a build waiting with no way to tell that it is stuck, so
 * they run under a deadline instead.
 */
public final class Watchdog {

    private static final int EXIT_CODE = 2;

    public static void arm(String what, long seconds) {
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                return;
            }

            System.err.println(what + " did not finish within " + seconds + " seconds.");
            Runtime.getRuntime().halt(EXIT_CODE);
        }, "watchdog");

        watchdog.setDaemon(true);
        watchdog.start();
    }

    private Watchdog() {
        /* empty */
    }
}

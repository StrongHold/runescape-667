import jagex3.jagmisc.jagmisc;
import rs2.client.loading.library.LibraryManager;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Holds the miscellaneous library to what the machine says by another route.
 *
 * The game's file store has jagmisc for Windows and for nothing else, so unlike every other
 * native here there is no shipped library to draw the same picture through and compare. What
 * there is instead is a second way to ask the same question: the kernel reports the size of
 * memory through sysctl, the virtual machine keeps a monotonic clock of its own, and an address
 * reserved for documentation can never answer a ping. Each answer is held against one of those.
 */
public final class Jagmisc {

    private static final long TIMEOUT = 1000L;
    private static final long MEGABYTE = 1024L * 1024L;

    /**
     * Two clocks counting the same second will not agree to the nanosecond, and neither will
     * sleep for exactly as long as it was asked to, so they are held to a tenth of the interval.
     */
    private static final long INTERVAL = 200L;
    private static final long DRIFT = INTERVAL / 10L;

    private static final byte[] LOOPBACK = {127, 0, 0, 1};

    /**
     * The first address of TEST-NET-1, which is set aside for documentation and is routed
     * nowhere, so nothing can ever reply from it.
     */
    private static final byte[] UNREACHABLE = {(byte) 192, 0, 2, 1};

    public static void main(String[] args) {
        try {
            Watchdog.arm("The jagmisc check", 120);
            LibraryManager.putLibrary(new File(args[0]), "jagmisc");
            LibraryManager.loadNative(Jagmisc.class, "jagmisc");

            checkInit();
            checkClockAdvances();
            checkClockKeepsTime();
            checkTotalMemory();
            checkAvailableMemory();
            checkLoopbackAnswers();
            checkUnreachableTimesOut();
            jagmisc.quit();

            System.out.println("jagmisc agreed with the machine on "
                    + System.getProperty("os.arch"));
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void checkInit() {
        if (!jagmisc.init()) {
            throw new IllegalStateException("the library reported it had nothing to offer");
        }
    }

    private static void checkClockAdvances() {
        long first = jagmisc.nanoTime();
        if (first == 0L) {
            throw new IllegalStateException("the clock read zero, which is how it says it is unusable");
        }
        long second = jagmisc.nanoTime();
        if (second < first) {
            throw new IllegalStateException("the clock went backwards, from " + first + " to " + second);
        }
    }

    private static void checkClockKeepsTime() throws InterruptedException {
        long ourStart = jagmisc.nanoTime();
        long theirStart = System.nanoTime();
        Thread.sleep(INTERVAL);
        long ours = jagmisc.nanoTime() - ourStart;
        long theirs = System.nanoTime() - theirStart;

        long apart = Math.abs(ours - theirs) / 1000000L;
        if (apart > DRIFT) {
            throw new IllegalStateException("the clock measured " + ours / 1000000L
                    + "ms where the virtual machine measured " + theirs / 1000000L + "ms");
        }
    }

    private static void checkTotalMemory() throws Exception {
        long ours = jagmisc.getTotalPhysicalMemory();
        long reported = Long.parseLong(ask("sysctl", "-n", "hw.memsize"));
        if (ours != reported) {
            throw new IllegalStateException("physical memory was " + ours
                    + " where the kernel reports " + reported);
        }
    }

    private static void checkAvailableMemory() {
        long available = jagmisc.getAvailablePhysicalMemory();
        long total = jagmisc.getTotalPhysicalMemory();
        if (available <= 0L || available > total) {
            throw new IllegalStateException("available memory was " + available / MEGABYTE
                    + "Mb of a total of " + total / MEGABYTE + "Mb");
        }
    }

    private static void checkLoopbackAnswers() throws Throwable {
        int ping = jagmisc.ping(LOOPBACK[0], LOOPBACK[1], LOOPBACK[2], LOOPBACK[3], TIMEOUT);
        if (ping < 0 || ping >= TIMEOUT) {
            throw new IllegalStateException("this machine took " + ping + "ms to answer itself");
        }
    }

    /**
     * A ping that cannot be answered has to give up of its own accord rather than be given up on,
     * so how long it took is checked as well as what it said.
     */
    private static void checkUnreachableTimesOut() {
        long started = System.nanoTime();
        int ping = jagmisc.ping0(UNREACHABLE[0], UNREACHABLE[1], UNREACHABLE[2], UNREACHABLE[3], TIMEOUT);
        long took = (System.nanoTime() - started) / 1000000L;

        if (ping >= 0) {
            throw new IllegalStateException("an address routed nowhere answered in " + ping + "ms");
        } else if (took < TIMEOUT || took > TIMEOUT * 2L) {
            throw new IllegalStateException("waiting " + TIMEOUT + "ms for nothing took " + took + "ms");
        }
    }

    private static String ask(String... command) throws Exception {
        Process process = new ProcessBuilder(command).start();
        String said = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (process.waitFor() != 0) {
            throw new IllegalStateException(String.join(" ", command) + " failed");
        }
        return said.trim();
    }

    private Jagmisc() {
        /* empty */
    }
}

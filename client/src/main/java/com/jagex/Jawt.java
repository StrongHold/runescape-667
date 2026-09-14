package com.jagex;

public final class Jawt {

    private static final String JAWT_FAILED =
        "Failed to load jawt.dll - only safe mode will function. Try reinstalling Java.";

    /**
     * Loads the AWT native toolkit, which only the hardware renderers need. A failure is fatal on
     * Windows alone, and even there the client still runs in safe mode, so it is reported rather
     * than thrown.
     */
    public static void tryLoad() {
        try {
            System.loadLibrary("jawt");
        } catch (Throwable t) {
            var os = System.getProperty("os.name");

            if (os.toLowerCase().contains("windows")) {
                System.err.println(JAWT_FAILED);
                t.printStackTrace();
            }
        }
    }

    private Jawt() {
        /* empty */
    }
}

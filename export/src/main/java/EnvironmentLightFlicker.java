/**
 * How a map light flickers, as the client's {@code EnvironmentLight.applyPreset} gives it for a
 * preset: the least it falls to, which of the client's patterns it follows, and how far and how
 * fast it swings. A preset the client has no case for flickers not at all.
 *
 * @param ambient the least of the flicker, out of 2048.
 * @param pattern which of the client's noise patterns drives it, 0 for none.
 * @param amplitude how far it swings, out of 2048.
 * @param frequency how fast it swings, out of 2048.
 */
public record EnvironmentLightFlicker(int ambient, int pattern, int amplitude, int frequency) {

    public EnvironmentLightFlicker(int preset) {
        this(ambientOf(preset), patternOf(preset), amplitudeOf(preset), frequencyOf(preset));
    }

    private static int ambientOf(int preset) {
        return switch (preset) {
            case 10, 11, 15 -> 1536;
            case 6, 7, 14 -> 1280;
            case 8, 9 -> 1024;
            case 16 -> 1792;
            default -> 0;
        };
    }

    private static int patternOf(int preset) {
        return switch (preset) {
            case 2, 3, 14, 15, 16 -> 1;
            case 12, 13 -> 2;
            case 6, 7, 8, 9, 10, 11 -> 3;
            case 4, 5 -> 4;
            default -> 0;
        };
    }

    private static int amplitudeOf(int preset) {
        return switch (preset) {
            case 10, 11, 15 -> 512;
            case 6, 7, 14 -> 768;
            case 8, 9 -> 1024;
            case 16 -> 256;
            default -> 2048;
        };
    }

    private static int frequencyOf(int preset) {
        return switch (preset) {
            case 3, 7, 9, 11, 15 -> 4096;
            case 5, 13, 16 -> 8192;
            default -> 2048;
        };
    }
}

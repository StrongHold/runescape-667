import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static364 {

    private static final int WIDTH = 128;

    private static final int HEIGHT = 128;

    private static final int DEPTH = 16;

    private static final int FRAME_SIZE = WIDTH * HEIGHT;

    private static final int OCTAVE_COUNT = 8;

    private static final float LACUNARITY = 2.0F;

    /**
     * The octaves are summed as signed bytes centred on zero, so the finished frame is biased by half the byte range
     * to leave it in the unsigned range the texture upload expects.
     */
    private static final int BYTE_MIDPOINT = 127;

    @OriginalMember(owner = "client!lia", name = "p", descriptor = "D")
    public static double aDouble17;

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "(IFFILclient!tk;[BFIBFIIIF)V")
    public static void generateNoiseFrame(@OriginalArg(1) float yFrequency, @OriginalArg(2) float xFrequency, @OriginalArg(3) int frame, @OriginalArg(4) NoiseGenerator noise, @OriginalArg(5) byte[] dest, @OriginalArg(6) float persistence, @OriginalArg(9) float zFrequency, @OriginalArg(12) int offset, @OriginalArg(13) float amplitude) {
        @Pc(17) float[] octave = new float[FRAME_SIZE];
        @Pc(22) int index;
        @Pc(48) int texel;
        for (@Pc(19) int i = 0; i < OCTAVE_COUNT; i++) {
            index = offset;
            noise.generate(yFrequency / (float) HEIGHT, amplitude * (float) BYTE_MIDPOINT, octave, 0, zFrequency / (float) DEPTH, frame, xFrequency / (float) WIDTH);
            for (texel = 0; texel < FRAME_SIZE; texel++) {
                dest[index] = (byte) (int) ((float) dest[index] + octave[texel]);
                index++;
            }
            xFrequency *= LACUNARITY;
            yFrequency *= LACUNARITY;
            zFrequency *= LACUNARITY;
            amplitude *= persistence;
        }
        index = offset;
        for (texel = 0; texel < FRAME_SIZE; texel++) {
            dest[index] = (byte) (dest[index] + BYTE_MIDPOINT);
            index++;
        }
    }

}

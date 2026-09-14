import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static448 {

    private static final int VOLUME_SIZE = 16 * 128 * 128;

    @OriginalMember(owner = "client!oc", name = "f", descriptor = "I")
    public static int anInt6801;

    @OriginalMember(owner = "client!oc", name = "a", descriptor = "(IIIFFFIIFFLclient!tk;)[B")
    public static byte[] generateNoiseVolume(@OriginalArg(3) float xFrequency, @OriginalArg(4) float yFrequency, @OriginalArg(5) float amplitude, @OriginalArg(8) float zFrequency, @OriginalArg(9) float persistence, @OriginalArg(10) NoiseGenerator noise) {
        @Pc(10) byte[] volume = new byte[VOLUME_SIZE];
        Static314.generateNoiseFrames(yFrequency, zFrequency, amplitude, xFrequency, volume, 0, persistence, noise);
        return volume;
    }
}

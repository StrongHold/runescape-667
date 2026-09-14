import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!tk")
public abstract class NoiseGenerator {

    @OriginalMember(owner = "client!tk", name = "<init>", descriptor = "()V")
    protected NoiseGenerator() {
    }

    /**
     * Fills a 128 by 128 slice of an animated noise volume, one texel per entry, starting at the given offset. The
     * step arguments are the distance the noise coordinate advances per texel along each axis, so a step of
     * frequency / 128 makes the slice tile seamlessly over that many lattice cells.
     */
    @OriginalMember(owner = "client!tk", name = "a", descriptor = "(FFI[FIFIFIBI)V")
    public abstract void generate(@OriginalArg(0) float yStep, @OriginalArg(1) float amplitude, @OriginalArg(3) float[] dest, @OriginalArg(4) int offset, @OriginalArg(5) float zStep, @OriginalArg(6) int frame, @OriginalArg(7) float xStep);
}

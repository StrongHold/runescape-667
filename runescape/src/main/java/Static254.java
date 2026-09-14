import org.openrs2.deob.annotation.OriginalMember;

public final class Static254 {

    /**
     * Selects what {@link Static264#rasteriseTriangle} and {@link Static34#rasteriseScanline} do
     * with the occlusion depth buffer: 1 writes the occluders into it, 2 tests geometry against the
     * depth already written there.
     */
    @OriginalMember(owner = "client!hr", name = "i", descriptor = "I")
    public static int occlusionMode;

}

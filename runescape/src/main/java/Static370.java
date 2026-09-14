import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static370 {

    /**
     * Releases the occluders and the occlusion depth buffer when the scene is torn down.
     */
    @OriginalMember(owner = "client!lm", name = "d", descriptor = "(I)V")
    public static void freeOccluders() {
        @Pc(12) int i;
        if (Static384.aLocOccluderArray2 != null) {
            for (i = 0; i < Static317.anInt5046; i++) {
                Static384.aLocOccluderArray2[i] = null;
            }
            Static384.aLocOccluderArray2 = null;
        }
        if (Static607.aLocOccluderArray4 != null) {
            for (i = 0; i < Static444.anInt6751; i++) {
                Static607.aLocOccluderArray4[i] = null;
            }
            Static607.aLocOccluderArray4 = null;
        }
        if (Static285.locOccluders != null) {
            for (i = 0; i < Static150.locOccluderCount; i++) {
                Static285.locOccluders[i] = null;
            }
            Static285.locOccluders = null;
        }
        Static560.aLocOccluderArray3 = null;
        Static446.tileOcclusionCache = null;
        Static485.occlusionDepthBuffer = null;
        Static624.anInt9461 = -1;
        Static228.anInt3709 = -1;
    }

}

import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static725 {

    @OriginalMember(owner = "client!ww", name = "f", descriptor = "Lclient!fba;")
    public static final Class121 aClass121_6 = new Class121();

    @OriginalMember(owner = "client!ww", name = "a", descriptor = "(Lclient!ha;IB)V")
    public static void buildOcclusionBuffer(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int level) {
        if (!Static18.occlude || !Static29.aBoolean60) {
            Static469.activeOccluderCount = 0;
            return;
        }
        if (Static703.aBoolean798) {
            Static415.aLong205 = Static272.aClass13_1.method5161();
        }
        Static298.occludedGroundCount = 0;
        Static356.anInt5773 = 0;
        Static679.occludedWallCount = 0;
        @Pc(32) int[] projection = toolkit.Y();
        Static1.anInt10797 = (int) ((float) projection[2] / 3.0F);
        Static412.anInt6357 = (int) ((float) projection[3] / 3.0F);
        toolkit.method7944(Static118.anIntArray198);
        if (Static228.anInt3709 != (int) ((float) Static118.anIntArray198[0] / 3.0F) || (int) ((float) Static118.anIntArray198[1] / 3.0F) != Static624.anInt9461) {
            Static228.anInt3709 = (int) ((float) Static118.anIntArray198[0] / 3.0F);
            Static624.anInt9461 = (int) ((float) Static118.anIntArray198[1] / 3.0F);
            Static407.anInt6286 = Static624.anInt9461 >> 1;
            Static460.anInt6970 = Static228.anInt3709 >> 1;
            Static485.anIntArray886 = new int[Static228.anInt3709 * Static624.anInt9461];
        }
        Static107.aMatrix_3 = toolkit.camera();
        Static469.activeOccluderCount = 0;
        for (@Pc(117) int i = 0; i < Static317.anInt5046; i++) {
            Static494.method6601(level, Static384.aLocOccluderArray2[i], toolkit);
        }
        for (@Pc(149) int i = 0; i < Static444.anInt6751; i++) {
            Static494.method6601(level, Static607.aLocOccluderArray4[i], toolkit);
        }
        for (@Pc(170) int i = 0; i < Static150.anInt2634; i++) {
            Static494.method6601(level, Static285.aLocOccluderArray1[i], toolkit);
        }
        Static432.occludedPixelCount = 0;
        if (Static469.activeOccluderCount > 0) {
            @Pc(205) int length = Static485.anIntArray886.length;
            @Pc(212) int unrolledLength = length - length & 0x7;
            @Pc(214) int index = 0;
            while (index < unrolledLength) {
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
            }
            while (index < length) {
                Static485.anIntArray886[index++] = Integer.MAX_VALUE;
            }
            Static254.anInt4115 = 1;
            for (@Pc(289) int i = 0; i < Static469.activeOccluderCount; i++) {
                @Pc(295) LocOccluder occluder = Static560.aLocOccluderArray3[i];
                Static264.rasteriseTriangle(occluder.aShortArray17[0], occluder.aShortArray18[0], occluder.aShortArray19[3], occluder.aShortArray19[1], occluder.aShortArray18[3], occluder.aShortArray17[3], occluder.aShortArray18[1], occluder.aShortArray19[0], occluder.aShortArray17[1]);
                Static264.rasteriseTriangle(occluder.aShortArray17[1], occluder.aShortArray18[1], occluder.aShortArray19[3], occluder.aShortArray19[2], occluder.aShortArray18[3], occluder.aShortArray17[3], occluder.aShortArray18[2], occluder.aShortArray19[1], occluder.aShortArray17[2]);
            }
            Static254.anInt4115 = 2;
        }
        if (Static703.aBoolean798) {
            Static666.occludeCalcElapsedMs = Static272.aClass13_1.method5161() - Static415.aLong205;
        }
    }

    @OriginalMember(owner = "client!ww", name = "a", descriptor = "(ZD)V")
    public static void setGamma(@OriginalArg(1) double gamma) {
        if (Static385.aDouble18 == gamma) {
            return;
        }
        for (@Pc(18) int i = 0; i < 256; i++) {
            @Pc(32) int value = (int) (Math.pow((double) i / 255.0D, gamma) * 255.0D);
            Static609.anIntArray716[i] = value > 255 ? 255 : value;
        }
        Static385.aDouble18 = gamma;
    }
}

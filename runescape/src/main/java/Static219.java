import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static219 {

    @OriginalMember(owner = "client!gp", name = "h", descriptor = "[I")
    public static int[] roofMaxZ;

    @OriginalMember(owner = "client!gp", name = "a", descriptor = "(IILclient!dda;)Z")
    public static boolean method3190(@OriginalArg(0) int arg0, @OriginalArg(2) LocOccluder arg1) {
        Static107.aMatrix_3.project(arg1.anIntArray185[arg0], arg1.anIntArray186[arg0], arg1.anIntArray188[arg0], Static35.anIntArray58);
        @Pc(22) int local22 = Static35.anIntArray58[2];
        if (local22 < 50) {
            return false;
        } else {
            arg1.aShortArray17[arg0] = (short) (Static1.anInt10797 * Static35.anIntArray58[0] / local22 + Static460.anInt6970);
            arg1.aShortArray18[arg0] = (short) (Static35.anIntArray58[1] * Static412.anInt6357 / local22 + Static407.anInt6286);
            arg1.aShortArray19[arg0] = (short) local22;
            return true;
        }
    }
}

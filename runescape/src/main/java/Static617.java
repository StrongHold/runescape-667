import com.jagex.core.util.Arrays;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static617 {

    @OriginalMember(owner = "client!tj", name = "B", descriptor = "[I")
    public static int[] roofMaxY;

    @OriginalMember(owner = "client!tj", name = "H", descriptor = "Z")
    public static boolean quickChatWorld = false;

    @OriginalMember(owner = "client!tj", name = "C", descriptor = "[F")
    public static final float[] aFloatArray69 = new float[4];

    @OriginalMember(owner = "client!tj", name = "a", descriptor = "(I[FZ)[F")
    public static float[] method8299(@OriginalArg(0) int arg0, @OriginalArg(1) float[] arg1) {
        @Pc(12) float[] local12 = new float[arg0];
        Arrays.copy(arg1, 0, local12, 0, arg0);
        return local12;
    }
}

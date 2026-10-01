import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static319 {

    @OriginalMember(owner = "client!kba", name = "I", descriptor = "[[B")
    public static byte[][] aByteArrayArray16;

    @OriginalMember(owner = "client!kba", name = "R", descriptor = "I")
    public static int environmentLightCount;

    @OriginalMember(owner = "client!kba", name = "N", descriptor = "[I")
    public static final int[] anIntArray384 = new int[6];

    @OriginalMember(owner = "client!kba", name = "J", descriptor = "[F")
    public static final float[] aFloatArray26 = new float[]{0.0F, -1.0F, 0.0F, 0.0F};

    @OriginalMember(owner = "client!kba", name = "K", descriptor = "I")
    public static int permVarcCount = 0;

    @OriginalMember(owner = "client!kba", name = "a", descriptor = "(IBI)Z")
    public static boolean method4594(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return Static483.method6488(arg1, arg0) & Static340.method5012(arg1, arg0);
    }

}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static399 {

    @OriginalMember(owner = "client!mja", name = "b", descriptor = "[I")
    public static final int[] boundsCornerZ = new int[8];

    @OriginalMember(owner = "client!mja", name = "a", descriptor = "(IIB)I")
    public static int autosetupHardware(@OriginalArg(0) int toolkit, @OriginalArg(1) int score) {
        @Pc(27) byte local27;
        if (score > 20000) {
            local27 = 4;
            Static395.method9162();
        } else if (score > 10000) {
            Static133.method2316();
            local27 = 3;
        } else if (score <= 5000) {
            local27 = 1;
            Static468.method7643();
        } else {
            local27 = 2;
            Static75.method6239();
        }
        if (toolkit != ClientOptions.instance.toolkit.getValue()) {
            ClientOptions.instance.update(toolkit, ClientOptions.instance.toolkitDefault);
            Static32.setToolkit(toolkit, false);
        }
        ClientOptions.save();
        return local27;
    }

    @OriginalMember(owner = "client!mja", name = "a", descriptor = "(III)Z")
    public static boolean retainTexCoords(@OriginalArg(1) int functionMask, @OriginalArg(2) int flags) {
        return Static407.method5627(functionMask, flags) || Static475.method6443(functionMask, flags);
    }
}

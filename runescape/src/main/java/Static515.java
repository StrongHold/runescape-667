import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static515 {

    @OriginalMember(owner = "client!qda", name = "l", descriptor = "[[Ljava/lang/String;")
    public static final String[][] renderingTaskNames = {
        {"M1", "M2", "S1", "F"},
        {"M1", "M2", "M3", "S1", "S2", "F"},
        {"M1", "M2", "M3", "M4", "S1", "S2", "S3", "F"}
    };

    @OriginalMember(owner = "client!qda", name = "a", descriptor = "(III)Z")
    public static boolean method6804(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return Static77.method1560(arg0, arg1) || Static519.colourBufferMutable(-97, arg1, arg0);
    }
}

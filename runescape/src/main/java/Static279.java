import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static279 {

    @OriginalMember(owner = "client!io", name = "c", descriptor = "[Z")
    public static boolean[] environmentLightApplied;

    @OriginalMember(owner = "client!io", name = "a", descriptor = "[Ljava/lang/Object;")
    public static Object[] clanVars;

    @OriginalMember(owner = "client!io", name = "e", descriptor = "F")
    public static float aFloat82;

    @OriginalMember(owner = "client!io", name = "a", descriptor = "(III)Z")
    public static boolean method4074(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x800) != 0 && (arg0 & 0x37) != 0;
    }
}

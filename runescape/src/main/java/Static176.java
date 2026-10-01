import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static176 {

    @OriginalMember(owner = "client!fi", name = "a", descriptor = "()V")
    public static void castFloorShadows() {
        Static374.method5303(Static299.tileMaxLevel);
    }

    @OriginalMember(owner = "client!fi", name = "a", descriptor = "(III)Z")
    public static boolean method6689(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        return (arg2 & 0x180) != 0;
    }

}

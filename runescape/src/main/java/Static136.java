import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static136 {

    @OriginalMember(owner = "client!eea", name = "a", descriptor = "(IIIIIIII)V")
    public static void method2347(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(7) int arg6) {
        if (arg6 == arg2) {
            Static412.fillAndOutlineCircle(arg1, arg6, arg4, arg0, arg3, arg5);
        } else if (Static180.anInt2995 <= arg3 - arg6 && arg6 + arg3 <= Static111.anInt2219 && Static724.anInt10930 <= arg0 - arg2 && Static273.anInt4395 >= arg2 + arg0) {
            Static661.method5433(arg5, arg6, arg4, arg2, arg1, arg0, arg3);
        } else {
            Static495.method6610(arg3, arg6, arg2, arg4, arg0, arg5, arg1);
        }
    }
}

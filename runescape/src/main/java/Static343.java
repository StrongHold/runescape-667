import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static343 {

    @OriginalMember(owner = "client!kq", name = "a", descriptor = "(III)I")
    public static int method5036(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        @Pc(41) int local41 = Static659.method8604(arg0 - 1, arg1 + -1) + Static659.method8604(arg0 - 1, arg1 + 1) + Static659.method8604(arg0 + 1, arg1 + -1) + Static659.method8604(arg0 + 1, arg1 + 1);
        @Pc(77) int local77 = Static659.method8604(arg0, arg1 - 1) + Static659.method8604(arg0, arg1 + 1) + Static659.method8604(arg0 + -1, arg1) + Static659.method8604(arg0 - -1, arg1);
        @Pc(82) int local82 = Static659.method8604(arg0, arg1);
        return local77 / 8 + local41 / 16 + local82 / 4;
    }
}

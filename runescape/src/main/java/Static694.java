import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static694 {

    @OriginalMember(owner = "client!vv", name = "z", descriptor = "I")
    public static int anInt10405;

    @OriginalMember(owner = "client!vv", name = "F", descriptor = "Lclient!fma;")
    public static final Class131 aClass131_6 = new Class131();

    @OriginalMember(owner = "client!vv", name = "c", descriptor = "(B)I")
    public static int advanceLoadState() {
        @Pc(9) int local9 = Loading.state.getStep();
        if (local9 < Loading.states.length - 1) {
            Loading.state = Loading.states[local9 + 1];
        }
        return 100;
    }

}

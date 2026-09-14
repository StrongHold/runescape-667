import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static92 {

    @OriginalMember(owner = "client!cs", name = "a", descriptor = "(IBI)V")
    public static void method1757(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        @Pc(18) Environment local18 = Static665.aEnvironmentArrayArray1[arg0][arg1];
        if (local18 != null) {
            Static344.anInt5617 = local18.sunX;
            Static331.anInt5441 = local18.sunZ;
            Static417.anInt6400 = local18.sunY;
        }
        Static588.method7713();
    }
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static296 {

    @OriginalMember(owner = "client!jh", name = "n", descriptor = "Lclient!sb;")
    public static Class330 aClass330_61;

    @OriginalMember(owner = "client!jh", name = "e", descriptor = "Lclient!om;")
    public static final Class280 aClass280_5 = new Class280(2);

    @OriginalMember(owner = "client!jh", name = "k", descriptor = "I")
    public static int anInt4792 = 0;

    @OriginalMember(owner = "client!jh", name = "a", descriptor = "(Lclient!gp;BIIII)V")
    public static void method4361(@OriginalArg(0) Class150 arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4) {
        @Pc(15) Node_Sub25 local15 = null;
        for (@Pc(20) Node_Sub25 local20 = (Node_Sub25) Static227.aDeque_18.first(65280); local20 != null; local20 = (Node_Sub25) Static227.aDeque_18.next()) {
            if (local20.anInt4010 == arg1 && local20.anInt4016 == arg3 && local20.anInt4006 == arg4 && arg2 == local20.anInt4011) {
                local15 = local20;
                break;
            }
        }
        if (local15 == null) {
            local15 = new Node_Sub25();
            local15.anInt4010 = arg1;
            local15.anInt4016 = arg3;
            local15.anInt4006 = arg4;
            local15.anInt4011 = arg2;
            Static227.aDeque_18.addLast(local15);
        }
        local15.aBoolean309 = false;
        local15.aClass150_1 = arg0;
        local15.aBoolean310 = true;
    }

    @OriginalMember(owner = "client!jh", name = "b", descriptor = "(I)V")
    public static void method4362() {
        @Pc(5) int local5 = 0;
        if (Static400.aClass2_Sub34_28.aClass57_Sub24_1.method7082() == 1) {
            local5 = 55;
        }
        if (Static400.aClass2_Sub34_28.aClass57_Sub30_1.method8356() == 0) {
            local5 |= 0x40;
        }
        Static335.method4945(local5);
        Static354.aClass142_4.method3065(local5);
        Static419.aObjTypeList_1.method2481(local5);
        Static690.aNPCTypeList_2.method7093(local5);
        Static23.aClass128_1.method2695(local5);
        Static41.method1029(local5);
        Static298.method4388(local5);
        Static534.method7122(local5);
        Static170.method2651(local5);
        Static449.method6116();
    }
}

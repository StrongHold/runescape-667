import org.openrs2.deob.annotation.OriginalMember;

public final class Static229 {

    @OriginalMember(owner = "client!hc", name = "d", descriptor = "Lclient!lga;")
    public static final Class225 aClass225_101 = new Class225(79, -2);

    @OriginalMember(owner = "client!hc", name = "a", descriptor = "(I)V")
    public static void method3368() {
        if (Static473.aLoadState_22 != null) {
            Static449.aClass364_1 = new Class364();
            Static449.aClass364_1.method8374(Static473.aLoadState_22.startPercentage, Static473.aLoadState_22.stalledText.localise(Static51.anInt1052), Static473.aLoadState_22, Static72.aLong52);
            Static242.aThread1 = new Thread(Static449.aClass364_1, "");
            Static242.aThread1.start();
        }
    }
}

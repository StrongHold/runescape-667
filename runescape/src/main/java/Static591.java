import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.keyboard.KeyLog;

public final class Static591 {

    @OriginalMember(owner = "client!sl", name = "C", descriptor = "[Lclient!wka;")
    public static final KeyLog[] AN_KEYBOARD_EVENT_ARRAY_2 = new KeyLog[75];

    @OriginalMember(owner = "client!sl", name = "b", descriptor = "(IIIII)V")
    public static void drawVerticalLineClipped(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(4) int arg3) {
        if (Static180.anInt2995 <= arg0 && Static111.anInt2219 >= arg0) {
            @Pc(30) int local30 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg2);
            @Pc(36) int local36 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, arg3);
            Static87.drawVerticalLineUnclipped(local36, local30, arg1, arg0);
        }
    }

}

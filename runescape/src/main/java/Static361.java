import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static361 {

    @OriginalMember(owner = "client!lga", name = "d", descriptor = "[Ljava/lang/String;")
    public static final String[] aStringArray29 = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @OriginalMember(owner = "client!lga", name = "a", descriptor = "(B)V")
    public static void method5240() {
        @Pc(5) int local5 = 0;
        for (@Pc(15) int local15 = 0; local15 < Static720.mapWidth; local15++) {
            for (@Pc(18) int local18 = 0; local18 < Static501.mapLength; local18++) {
                if (Static409.hideRoofsFrom(local18, Static334.activeTiles, local5, local15, true)) {
                    local5++;
                }
                if (local5 >= 512) {
                    return;
                }
            }
        }
    }
}

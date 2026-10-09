import com.jagex.game.runetek6.client.GameShell;
import com.jagex.graphics.ToolkitType;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static625 {

    @OriginalMember(owner = "client!tp", name = "a", descriptor = "I")
    public static int anInt9472 = 0;

    @OriginalMember(owner = "client!tp", name = "b", descriptor = "[I")
    public static final int[] anIntArray737 = new int[3];

    @OriginalMember(owner = "client!tp", name = "b", descriptor = "(I)I")
    public static int method8337() {
        @Pc(10) byte local10;
        if (GameShell.maxmemory < 96) {
            local10 = 1;
            Static468.applyMinDetailPreset();
        } else {
            @Pc(17) int local17 = Static65.profileCpu();
            if (local17 <= 100) {
                Static395.applyHighDetailPreset();
                local10 = 4;
            } else if (local17 <= 500) {
                local10 = 3;
                Static133.applyMediumDetailPreset();
            } else if (local17 > 1000) {
                Static468.applyMinDetailPreset();
                local10 = 1;
            } else {
                Static75.applyLowDetailPreset();
                local10 = 2;
            }
        }
        if (ClientOptions.instance.toolkit.getValue() != ToolkitType.JAVA) {
            ClientOptions.instance.update(ToolkitType.JAVA, ClientOptions.instance.toolkitDefault);
            Static32.setToolkit(ToolkitType.JAVA, false);
        }
        ClientOptions.save();
        return local10;
    }
}

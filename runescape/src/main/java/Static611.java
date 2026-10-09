import com.jagex.graphics.ToolkitType;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static611 {

    @OriginalMember(owner = "client!tfa", name = "i", descriptor = "I")
    public static int mouseWheelRotation = 0;

    @OriginalMember(owner = "client!tfa", name = "c", descriptor = "(ZI)I")
    public static int applyPresetForScore(@OriginalArg(1) int arg0) {
        @Pc(8) byte local8;
        if (arg0 > 12000) {
            local8 = 4;
            Static395.applyHighDetailPreset();
        } else if (arg0 > 5000) {
            Static133.applyMediumDetailPreset();
            local8 = 3;
        } else if (arg0 > 2000) {
            Static75.applyLowDetailPreset();
            local8 = 2;
        } else {
            local8 = 1;
            Static468.applyMinDetailPreset();
        }

        if (ClientOptions.instance.toolkit.getValue() != ToolkitType.SSE) {
            ClientOptions.instance.update(ToolkitType.SSE, ClientOptions.instance.toolkitDefault);
            Static32.setToolkit(ToolkitType.SSE, false);
        }

        ClientOptions.save();
        return local8;
    }

}

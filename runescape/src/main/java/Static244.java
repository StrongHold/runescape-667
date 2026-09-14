import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static244 {

    @OriginalMember(owner = "client!hk", name = "a", descriptor = "(BII)I")
    public static int scaleHslLightness(@OriginalArg(1) int light, @OriginalArg(2) int hsl) {
        light = (hsl & 0x7F) * light >> 7;
        if (light < 2) {
            light = 2;
        } else if (light > 126) {
            light = 126;
        }
        return light + (hsl & 0xFF80);
    }
}

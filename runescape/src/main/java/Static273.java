import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static273 {

    @OriginalMember(owner = "client!ik", name = "M", descriptor = "I")
    public static int anInt4395 = 100;

    @OriginalMember(owner = "client!ik", name = "a", descriptor = "(IIII)I")
    public static int interpolateHsl(@OriginalArg(0) int to, @OriginalArg(2) int weight, @OriginalArg(3) int from) {
        if (from == to) {
            return from;
        }
        @Pc(14) int inverseWeight = 128 - weight;
        @Pc(29) int lightness = inverseWeight * (from & 0x7F) + (to & 0x7F) * weight >> 7;
        @Pc(43) int saturation = (from & 0x380) * inverseWeight + (to & 0x380) * weight >> 7;
        @Pc(57) int hue = weight * (to & 0xFC00) + inverseWeight * (from & 0xFC00) >> 7;
        return saturation & 0x380 | hue & 0xFC00 | lightness & 0x7F;
    }
}

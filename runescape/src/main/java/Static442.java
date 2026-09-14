import org.openrs2.deob.annotation.OriginalMember;

import java.math.BigInteger;

public final class Static442 {

    @OriginalMember(owner = "client!nv", name = "p", descriptor = "Ljava/math/BigInteger;")
    public static BigInteger JS5_RSA_EXPONENT = new BigInteger("10001", 16);

    @OriginalMember(owner = "client!nv", name = "l", descriptor = "Z")
    public static boolean aBoolean500;

    @OriginalMember(owner = "client!nv", name = "h", descriptor = "Z")
    public static boolean occludersDirty = false;

    @OriginalMember(owner = "client!nv", name = "a", descriptor = "(Z)V")
    public static void method5969() {
        Static425.toolkit.L(Static251.anInt4037, ClientOptions.instance.fog.getValue() == 1 ? Static171.anInt2882 + 256 << 2 : -1, 0);
    }
}

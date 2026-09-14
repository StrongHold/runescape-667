import org.openrs2.deob.annotation.OriginalMember;

public final class Static457 {

    @OriginalMember(owner = "client!oha", name = "b", descriptor = "[I")
    public static int[] anIntArray552 = new int[2];

    @OriginalMember(owner = "client!oha", name = "c", descriptor = "I")
    public static int anInt6933 = 1;

    @OriginalMember(owner = "client!oha", name = "b", descriptor = "(B)V")
    public static void applyCutsceneFovClamps() {
        Static322.aShort135 = Static306.aShort59;
        Static470.aShort82 = Static552.aShort123;
        Static267.aShort47 = Static25.minFov;
        Static465.aShort81 = Static598.maxFov;
        Static354.aBoolean439 = true;
        if (Static134.anInt10330 != 0 && Static482.anInt7228 != 0) {
            Static25.minFov = 334;
            Static598.maxFov = 334;
            Static306.aShort59 = Static552.aShort123 = (short) (Static134.anInt10330 * 512 / Static482.anInt7228);
        }
    }

}

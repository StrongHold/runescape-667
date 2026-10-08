import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static209 {

    @OriginalMember(owner = "client!gi", name = "c", descriptor = "F")
    public static float aFloat70;

    @OriginalMember(owner = "client!gi", name = "a", descriptor = "Lclient!fba;")
    public static final Class121 aClass121_4 = new Class121();

    @OriginalMember(owner = "client!gi", name = "c", descriptor = "(I)V")
    public static void updateRenderDistance() {
        Static329.anInt1752 = (int) ((double) Static720.mapWidth * 34.46D);
        Static32.anInt775 = 200;
        Static329.anInt1752 <<= 0x2;
        if (Toolkit.active.increaseRenderDistance()) {
            Static329.anInt1752 += 512;
        }
        Static501.method6716(false);
    }
}

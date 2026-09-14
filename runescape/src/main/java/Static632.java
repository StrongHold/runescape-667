import com.jagex.Entity;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static632 {

    @OriginalMember(owner = "client!u", name = "e", descriptor = "[F")
    public static final float[] aFloatArray70 = new float[4];

    @OriginalMember(owner = "client!u", name = "b", descriptor = "(III)Z")
    public static boolean method8364(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return (arg0 & 0x800) != 0 | Static558.method2895(arg0, arg1) || Static198.method2957(arg0, arg1);
    }

    @OriginalMember(owner = "client!u", name = "a", descriptor = "(Lclient!eo;ZZ)V")
    public static void drawEntity(@OriginalArg(0) Entity entity, @OriginalArg(2) boolean trackOrthoTiles) {
        entity.aBoolean813 = trackOrthoTiles;
        if (Static661.aBoolean457) {
            MapArea.renderQueues[MapArea.renderQueues.length - 1].method6812(entity);
        } else {
            Static658.method8591(entity, Static501.aClass2_Sub7Array4);
        }
    }
}

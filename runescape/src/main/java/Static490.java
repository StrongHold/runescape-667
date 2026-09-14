import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static490 {

    @OriginalMember(owner = "client!pha", name = "a", descriptor = "(ZIIIIIIIF)[[I")
    public static int[][] method6551(@OriginalArg(8) float arg0) {
        @Pc(7) int[][] local7 = new int[256][64];
        @Pc(11) Node_Sub1_Sub27 local11 = new Node_Sub1_Sub27();
        local11.anInt8799 = (int) (arg0 * 4096.0F);
        local11.aBoolean667 = false;
        local11.anInt8805 = 4;
        local11.anInt8810 = 4;
        local11.anInt8803 = 3;
        local11.postDecode();
        EnvironmentLight.method2313(256, 64);
        for (@Pc(48) int local48 = 0; local48 < 256; local48++) {
            local11.method7809(local48, local7[local48]);
        }
        return local7;
    }

}

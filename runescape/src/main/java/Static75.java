import com.jagex.Client;
import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static75 {

    @OriginalMember(owner = "client!ch", name = "k", descriptor = "Z")
    public static boolean hasOpaqueStationaryEntities = true;

    @OriginalMember(owner = "client!ch", name = "b", descriptor = "(III)I")
    public static int method6238(@OriginalArg(2) int arg0) {
        if (arg0 == -1) {
            return 12345678;
        }
        @Pc(17) int local17 = (arg0 & 0x7F) * 96 >> 7;
        if (local17 < 2) {
            local17 = 2;
        } else if (local17 > 126) {
            local17 = 126;
        }
        return local17 + (arg0 & 0xFF80);
    }

    @OriginalMember(owner = "client!ch", name = "b", descriptor = "(B)V")
    public static void method6239() {
        ClientOptions.instance.update(1, ClientOptions.instance.animateBackgroundDefault);
        ClientOptions.instance.update(1, ClientOptions.instance.animateBackground);
        ClientOptions.instance.update(1, ClientOptions.instance.removeRoofs);
        ClientOptions.instance.update(1, ClientOptions.instance.removeRoofsOverride);
        ClientOptions.instance.update(1, ClientOptions.instance.groundDecor);
        ClientOptions.instance.update(1, ClientOptions.instance.groundBlending);
        ClientOptions.instance.update(0, ClientOptions.instance.idleAnimations);
        ClientOptions.instance.update(0, ClientOptions.instance.flickeringEffects);
        ClientOptions.instance.update(0, ClientOptions.instance.spotShadows);
        ClientOptions.instance.update(0, ClientOptions.instance.hardShadows);
        ClientOptions.instance.update(0, ClientOptions.instance.textures);
        ClientOptions.instance.update(0, ClientOptions.instance.lightDetail);
        ClientOptions.instance.update(0, ClientOptions.instance.waterDetail);
        ClientOptions.instance.update(0, ClientOptions.instance.fog);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingMode);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingQuality);
        ClientOptions.instance.update(0, ClientOptions.instance.particles);
        ClientOptions.instance.update(0, ClientOptions.instance.buildArea);
        ClientOptions.instance.update(0, ClientOptions.instance.bloom);
        ClientOptions.instance.update(0, ClientOptions.instance.skydetail);
        Static376.method5313();
        ClientOptions.instance.update(2, ClientOptions.instance.maxScreenSize);
        ClientOptions.instance.update(2, ClientOptions.instance.graphicsQuality);
        Static296.updateFeatureMask();
        InterfaceManager.loginOpened();
        Client.changingWindowMode = true;
    }

    @OriginalMember(owner = "client!ch", name = "a", descriptor = "(BLclient!ge;)Lclient!kk;")
    public static TextureShapeEllipse readEllipse(@OriginalArg(1) Packet packet) {
        return new TextureShapeEllipse(packet.g2s(), packet.g2s(), packet.g2s(), packet.g2s(), packet.g3(), packet.g3(), packet.g1());
    }
}

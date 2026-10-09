import com.jagex.Client;
import com.jagex.core.constants.MaxScreenSize;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static133 {

    @OriginalMember(owner = "client!ed", name = "e", descriptor = "F")
    public static float aFloat63;

    @OriginalMember(owner = "client!ed", name = "b", descriptor = "I")
    public static int publicChatFilter = 0;

    @OriginalMember(owner = "client!ed", name = "a", descriptor = "(I)V")
    public static void applyMediumDetailPreset() {
        ClientOptions.instance.update(1, ClientOptions.instance.animateBackgroundDefault);
        ClientOptions.instance.update(1, ClientOptions.instance.animateBackground);
        ClientOptions.instance.update(2, ClientOptions.instance.removeRoofs);
        ClientOptions.instance.update(2, ClientOptions.instance.removeRoofsOverride);
        ClientOptions.instance.update(1, ClientOptions.instance.groundDecor);
        ClientOptions.instance.update(1, ClientOptions.instance.groundBlending);
        ClientOptions.instance.update(1, ClientOptions.instance.idleAnimations);
        ClientOptions.instance.update(1, ClientOptions.instance.flickeringEffects);
        ClientOptions.instance.update(1, ClientOptions.instance.spotShadows);
        ClientOptions.instance.update(1, ClientOptions.instance.textures);
        ClientOptions.instance.update(1, ClientOptions.instance.hardShadows);
        ClientOptions.instance.update(1, ClientOptions.instance.lightDetail);
        ClientOptions.instance.update(0, ClientOptions.instance.waterDetail);
        ClientOptions.instance.update(1, ClientOptions.instance.fog);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingMode);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingQuality);
        ClientOptions.instance.update(1, ClientOptions.instance.particles);
        ClientOptions.instance.update(0, ClientOptions.instance.buildArea);
        ClientOptions.instance.update(0, ClientOptions.instance.bloom);
        ClientOptions.instance.update(1, ClientOptions.instance.skydetail);
        Static376.method5313();
        ClientOptions.instance.update(MaxScreenSize._1024x768, ClientOptions.instance.maxScreenSize);
        ClientOptions.instance.update(3, ClientOptions.instance.graphicsQuality);
        Static296.updateFeatureMask();
        InterfaceManager.loginOpened();
        Client.changingWindowMode = true;
    }
}

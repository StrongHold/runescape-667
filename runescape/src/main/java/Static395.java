import com.jagex.Client;
import com.jagex.Entity;
import com.jagex.core.constants.MaxScreenSize;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static395 {

    @OriginalMember(owner = "client!mha", name = "l", descriptor = "[Lclient!eo;")
    public static Entity[] aEntityArray11;

    @OriginalMember(owner = "client!mha", name = "j", descriptor = "[I")
    public static final int[] statUpdates = new int[32];

    @OriginalMember(owner = "client!mha", name = "c", descriptor = "(I)V")
    public static void applyHighDetailPreset() {
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
        ClientOptions.instance.update(2, ClientOptions.instance.hardShadows);
        ClientOptions.instance.update(1, ClientOptions.instance.lightDetail);
        ClientOptions.instance.update(2, ClientOptions.instance.waterDetail);
        ClientOptions.instance.update(1, ClientOptions.instance.fog);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingMode);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingQuality);
        ClientOptions.instance.update(2, ClientOptions.instance.particles);
        ClientOptions.instance.update(0, ClientOptions.instance.buildArea);
        ClientOptions.instance.update(0, ClientOptions.instance.bloom);
        ClientOptions.instance.update(1, ClientOptions.instance.skydetail);
        Static376.method5313();
        ClientOptions.instance.update(MaxScreenSize.ANY, ClientOptions.instance.maxScreenSize);
        ClientOptions.instance.update(4, ClientOptions.instance.graphicsQuality);
        Static296.updateFeatureMask();
        InterfaceManager.loginOpened();
        Client.changingWindowMode = true;
    }

    @OriginalMember(owner = "client!mha", name = "a", descriptor = "(FFFIIIII)[F")
    public static float[] method9163(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6) {
        @Pc(6) float[] local6 = new float[9];
        @Pc(9) float[] local9 = new float[9];
        @Pc(17) float local17 = (float) Math.cos((float) arg5 * 0.024543693F);
        @Pc(25) float local25 = (float) Math.sin((float) arg5 * 0.024543693F);
        local6[5] = 0.0F;
        local6[2] = local25;
        local6[6] = -local25;
        local6[7] = 0.0F;
        local6[3] = 0.0F;
        local6[8] = local17;
        local6[4] = 1.0F;
        local6[0] = local17;
        local6[1] = 0.0F;
        @Pc(70) float[] local70 = new float[9];
        @Pc(72) float local72 = 1.0F;
        @Pc(77) float local77 = (float) arg6 / 32767.0F;
        @Pc(79) float local79 = 0.0F;
        @Pc(89) float local89 = -((float) Math.sqrt(1.0F - local77 * local77));
        @Pc(94) float local94 = 1.0F - local77;
        @Pc(105) float local105 = (float) Math.sqrt(arg4 * arg4 + arg3 * arg3);
        if (local105 == 0.0F && local77 == 0.0F) {
            local9 = local6;
        } else {
            if (local105 != 0.0F) {
                local72 = (float) -arg4 / local105;
                local79 = (float) arg3 / local105;
            }
            local70[6] = local94 * local72 * local79;
            local70[3] = local89 * -local79;
            local70[8] = local77 + local94 * local79 * local79;
            local70[4] = local77;
            local70[0] = local77 + local72 * local72 * local94;
            local70[2] = local94 * local72 * local79;
            local70[7] = local89 * -local72;
            local70[1] = local79 * local89;
            local70[5] = local89 * local72;
            local9[0] = local6[1] * local70[3] + local6[0] * local70[0] + local70[6] * local6[2];
            local9[1] = local6[0] * local70[1] + local70[4] * local6[1] + local70[7] * local6[2];
            local9[2] = local70[2] * local6[0] + local70[5] * local6[1] + local70[8] * local6[2];
            local9[3] = local70[3] * local6[4] + local6[3] * local70[0] + local70[6] * local6[5];
            local9[4] = local70[1] * local6[3] + local6[4] * local70[4] + local6[5] * local70[7];
            local9[5] = local6[5] * local70[8] + local70[5] * local6[4] + local6[3] * local70[2];
            local9[6] = local6[7] * local70[3] + local70[0] * local6[6] + local6[8] * local70[6];
            local9[7] = local6[6] * local70[1] + local70[4] * local6[7] + local6[8] * local70[7];
            local9[8] = local6[8] * local70[8] + local70[2] * local6[6] + local70[5] * local6[7];
        }
        local9[1] *= arg0;
        local9[4] *= arg1;
        local9[5] *= arg1;
        local9[2] *= arg0;
        local9[3] *= arg1;
        local9[7] *= arg2;
        local9[0] *= arg0;
        local9[8] *= arg2;
        local9[6] *= arg2;
        return local9;
    }
}

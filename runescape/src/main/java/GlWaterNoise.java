import com.jagex.core.io.ByteArrayWrapper;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Lazily bakes the animated water noise volumes for the OpenGL toolkit, plus the height field that the normal map
 * generator turns into a bump map when the hardware supports it. The results are held as soft references so a
 * memory starved client can drop them and have them rebuilt on the next call.
 */
public final class GlWaterNoise {

    @OriginalMember(owner = "client!pi", name = "a", descriptor = "(Lclient!qha;Z)V")
    public static void ensureGenerated(@OriginalArg(0) GlToolkit toolkit) {
        @Pc(12) byte[] texels;
        if (Static599.anObject14 == null) {
            @Pc(5) GlRippleNoiseTexture ripple = new GlRippleNoiseTexture();
            texels = ripple.generate();
            Static599.anObject14 = ByteArrayWrapper.wrap(texels);
        }
        if (Static158.anObject5 == null) {
            @Pc(34) GlFlowNoiseTexture flow = new GlFlowNoiseTexture();
            texels = flow.generate();
            Static158.anObject5 = ByteArrayWrapper.wrap(texels);
        }
        @Pc(49) Class202 normalMapper = toolkit.aClass202_1;
        if (normalMapper.method4582() && Static71.anObject4 == null) {
            texels = Static448.method6106(4.0F, 4.0F, 0.5F, 16.0F, 0.6F, new Class59_Sub1(419684));
            Static71.anObject4 = ByteArrayWrapper.wrap(texels);
        }
    }
}

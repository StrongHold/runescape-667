import com.jagex.core.io.ByteArrayWrapper;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Lazily bakes the animated water noise volumes for the native toolkit. The results are held as soft references so
 * a memory starved client can drop them and have them rebuilt on the next call.
 */
public final class NativeWaterNoise {

    @OriginalMember(owner = "client!ec", name = "e", descriptor = "(I)V")
    public static void ensureGenerated() {
        @Pc(16) byte[] texels;
        if (Static177.anObject6 == null) {
            @Pc(9) NativeRippleNoiseTexture ripple = new NativeRippleNoiseTexture();
            texels = ripple.generate();
            Static177.anObject6 = ByteArrayWrapper.wrap(texels);
        }
        if (Static644.anObject18 == null) {
            @Pc(31) NativeFlowNoiseTexture flow = new NativeFlowNoiseTexture();
            texels = flow.generate();
            Static644.anObject18 = ByteArrayWrapper.wrap(texels);
        }
    }

}

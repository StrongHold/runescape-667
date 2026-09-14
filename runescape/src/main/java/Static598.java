import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static598 {

    @OriginalMember(owner = "client!ss", name = "i", descriptor = "S")
    public static short maxFov = 32767;

    @OriginalMember(owner = "client!ss", name = "a", descriptor = "(Z)V")
    public static void updateSpotAnimations() {
        for (@Pc(13) SpotAnimationNode node = (SpotAnimationNode) Static346.spotAnimations.first(); node != null; node = (SpotAnimationNode) Static346.spotAnimations.next()) {
            @Pc(18) SpotAnimation spotAnimation = node.spotAnimation;

            spotAnimation.tick();

            if (spotAnimation.isFinished()) {
                node.unlink();
                spotAnimation.stopParticleSystem();
            } else if (Static334.activeTiles != null && spotAnimation.isAnimating()) {
                Static102.method2026(spotAnimation, true);
            }
        }
    }

    @OriginalMember(owner = "client!ss", name = "a", descriptor = "(IBI)Z")
    public static boolean method7828(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return (Static646.method8457(arg1, arg0) | Static58.method1257(arg1, arg0) | Static340.method5011(arg0, arg1)) & Static277.method4044(arg0, arg1);
    }
}

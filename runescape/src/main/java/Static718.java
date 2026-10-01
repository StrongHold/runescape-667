import com.jagex.game.runetek6.config.flotype.FloorOverlayType;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static718 {

    @OriginalMember(owner = "client!wo", name = "r", descriptor = "Z")
    public static boolean groundBlending = false;

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(Lclient!re;BLclient!ha;)I")
    public static int blendColour(@OriginalArg(0) FloorOverlayType overlayType, @OriginalArg(2) Toolkit toolkit) {
        if (overlayType.blendColour != -1) {
            return overlayType.blendColour;
        }

        if (overlayType.texture != -1) {
            @Pc(31) TextureMetrics metrics = toolkit.textureSource.getMetrics(overlayType.texture);

            if (!metrics.disableable) {
                return metrics.averageColour;
            }
        }

        return overlayType.colour;
    }
}

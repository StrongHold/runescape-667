import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Outputs a sprite from the cache at its own size, repeated across the texture instead of
 * stretched to fill it.
 */
@OriginalClass("client!rca")
public final class TextureOpSpriteTiled extends TextureOpSprite {

    @OriginalMember(owner = "client!rca", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(20) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty && this.loadSprite()) {
            @Pc(34) int[] outputRed = output[0];
            @Pc(38) int[] outputGreen = output[1];
            @Pc(42) int[] outputBlue = output[2];
            @Pc(50) int rowOffset = super.height * (y % super.height);
            for (@Pc(52) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(64) int pixel = super.pixels[x % super.width + rowOffset];
                outputBlue[x] = (pixel & 0xFF) << 4;
                outputGreen[x] = pixel >> 4 & 0xFF0;
                outputRed[x] = pixel >> 12 & 0xFF0;
            }
        }
        return output;
    }
}

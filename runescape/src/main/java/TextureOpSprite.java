import com.jagex.IndexedImage;
import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Outputs a sprite from the cache, stretched to fill the texture.
 */
@OriginalClass("client!ee")
public class TextureOpSprite extends TextureOp {

    @OriginalMember(owner = "client!ee", name = "J", descriptor = "[I")
    protected int[] pixels;

    @OriginalMember(owner = "client!ee", name = "F", descriptor = "I")
    protected int width;

    @OriginalMember(owner = "client!ee", name = "R", descriptor = "I")
    protected int height;

    @OriginalMember(owner = "client!ee", name = "I", descriptor = "I")
    public int spriteId = -1;

    @OriginalMember(owner = "client!ee", name = "<init>", descriptor = "()V")
    public TextureOpSprite() {
        super(0, false);
    }

    @OriginalMember(owner = "client!ee", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(11) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty && this.loadSprite()) {
            @Pc(25) int[] outputRed = output[0];
            @Pc(29) int[] outputGreen = output[1];
            @Pc(33) int[] outputBlue = output[2];
            @Pc(50) int rowOffset = this.width * (EnvironmentLight.anInt53 == this.height ? y : y * this.height / EnvironmentLight.anInt53);
            @Pc(60) int x;
            @Pc(68) int local68;
            if (this.width == EnvironmentLight.anInt9289) {
                for (x = 0; x < EnvironmentLight.anInt9289; x++) {
                    local68 = this.pixels[rowOffset++];
                    outputBlue[x] = (local68 & 0xFF) << 4;
                    outputGreen[x] = local68 >> 4 & 0xFF0;
                    outputRed[x] = local68 >> 12 & 0xFF0;
                }
            } else {
                for (x = 0; x < EnvironmentLight.anInt9289; x++) {
                    local68 = this.width * x / EnvironmentLight.anInt9289;
                    @Pc(122) int pixel = this.pixels[local68 + rowOffset];
                    outputBlue[x] = (pixel & 0xFF) << 4;
                    outputGreen[x] = pixel >> 4 & 0xFF0;
                    outputRed[x] = pixel >> 12 & 0xFF0;
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!ee", name = "c", descriptor = "(B)Z")
    protected final boolean loadSprite() {
        if (this.pixels != null) {
            return true;
        } else if (this.spriteId >= 0) {
            @Pc(37) IndexedImage image = Static426.anInt940 >= 0 ? IndexedImage.loadFirst(Static582.aJs5_108, Static426.anInt940, this.spriteId) : IndexedImage.loadFirst(Static582.aJs5_108, this.spriteId);
            image.method9389();
            this.pixels = image.method9383();
            this.height = image.height;
            this.width = image.width;
            return true;
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!ee", name = "b", descriptor = "(I)V")
    @Override
    public final void cacheReset() {
        super.cacheReset();
        this.pixels = null;
    }

    @OriginalMember(owner = "client!ee", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public final void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (!arg0 && arg2 == 0) {
            this.spriteId = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!ee", name = "b", descriptor = "(B)I")
    @Override
    public final int method9412() {
        return this.spriteId;
    }
}

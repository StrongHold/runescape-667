import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!aaa")
public final class TextureOpTexture extends TextureOp {

    @OriginalMember(owner = "client!aaa", name = "G", descriptor = "I")
    public int height;

    @OriginalMember(owner = "client!aaa", name = "R", descriptor = "[I")
    public int[] pixels;

    @OriginalMember(owner = "client!aaa", name = "V", descriptor = "I")
    public int width;

    @OriginalMember(owner = "client!aaa", name = "J", descriptor = "I")
    public int textureId = -1;

    @OriginalMember(owner = "client!aaa", name = "<init>", descriptor = "()V")
    public TextureOpTexture() {
        super(0, false);
    }

    @OriginalMember(owner = "client!aaa", name = "b", descriptor = "(I)V")
    @Override
    public void cacheReset() {
        super.cacheReset();
        this.pixels = null;
    }

    @OriginalMember(owner = "client!aaa", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int arg0) {
        @Pc(17) int[][] local17 = super.colourCache.get(arg0);
        if (super.colourCache.dirty) {
            @Pc(44) int local44 = (this.height == EnvironmentLight.anInt53 ? arg0 : arg0 * this.height / EnvironmentLight.anInt53) * this.width;
            @Pc(48) int[] local48 = local17[0];
            @Pc(52) int[] local52 = local17[1];
            @Pc(56) int[] local56 = local17[2];
            @Pc(66) int local66;
            @Pc(75) int local75;
            if (this.width == EnvironmentLight.anInt9289) {
                for (local66 = 0; local66 < EnvironmentLight.anInt9289; local66++) {
                    local75 = this.pixels[local44++];
                    local56[local66] = (local75 & 0xFF) << 4;
                    local52[local66] = local75 >> 4 & 0xFF0;
                    local48[local66] = local75 >> 12 & 0xFF0;
                }
            } else {
                for (local66 = 0; local66 < EnvironmentLight.anInt9289; local66++) {
                    local75 = local66 * this.width / EnvironmentLight.anInt9289;
                    @Pc(82) int local82 = this.pixels[local75 + local44];
                    local56[local66] = (local82 & 0xFF) << 4;
                    local52[local66] = local82 >> 4 & 0xFF0;
                    local48[local66] = local82 >> 12 & 0xFF0;
                }
            }
        }
        return local17;
    }

    @OriginalMember(owner = "client!aaa", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            this.textureId = -36;
        }
        if (arg2 == 0) {
            this.textureId = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!aaa", name = "a", descriptor = "(IIZ)V")
    @Override
    public void initCache(@OriginalArg(0) int height, @OriginalArg(1) int width) {
        super.initCache(height, width);
        if (this.textureId >= 0 && Static677.anTextureSource_11 != null) {
            @Pc(40) int local40 = Static677.anTextureSource_11.getMetrics(this.textureId).small ? 64 : 128;
            this.pixels = Static677.anTextureSource_11.rgbOutput(local40, false, local40, this.textureId, 1.0F);
            this.height = local40;
            this.width = local40;
        }
    }

    @OriginalMember(owner = "client!aaa", name = "d", descriptor = "(I)I")
    @Override
    public int getTextureId() {
        return this.textureId;
    }
}

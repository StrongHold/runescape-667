import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Fills the whole texture with a single constant intensity.
 */
@OriginalClass("client!lu")
public final class TextureOpConstant extends TextureOp {

    @OriginalMember(owner = "client!lu", name = "J", descriptor = "I")
    public int intensity;

    @OriginalMember(owner = "client!lu", name = "<init>", descriptor = "()V")
    public TextureOpConstant() {
        this(4096);
    }

    @OriginalMember(owner = "client!lu", name = "<init>", descriptor = "(I)V")
    public TextureOpConstant(@OriginalArg(0) int intensity) {
        super(0, true);
        this.intensity = 4096;
        this.intensity = intensity;
    }

    @OriginalMember(owner = "client!lu", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            Static379.method5355(false);
        }
        if (arg2 == 0) {
            this.intensity = (arg1.g1() << 12) / 255;
        }
    }

    @OriginalMember(owner = "client!lu", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        if (arg0 < 107) {
            WorldList.checksum = -76;
        }
        @Pc(16) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            Arrays.set(output, 0, EnvironmentLight.anInt9289, this.intensity);
        }
        return output;
    }
}

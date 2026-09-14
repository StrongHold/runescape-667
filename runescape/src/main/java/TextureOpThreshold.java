import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Outputs full intensity where the source falls inside a range, and zero everywhere else.
 */
@OriginalClass("client!cma")
public final class TextureOpThreshold extends TextureOp {

    @OriginalMember(owner = "client!cma", name = "K", descriptor = "I")
    public int min = 0;

    @OriginalMember(owner = "client!cma", name = "F", descriptor = "I")
    public int max = 4096;

    @OriginalMember(owner = "client!cma", name = "<init>", descriptor = "()V")
    public TextureOpThreshold() {
        super(1, true);
    }

    @OriginalMember(owner = "client!cma", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(21) int[] source = this.method9422(y, 0);
            for (@Pc(23) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(29) int value = source[x];
                output[x] = this.min <= value && this.max >= value ? 4096 : 0;
            }
        }
        return arg0 < 107 ? null : output;
    }

    @OriginalMember(owner = "client!cma", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.min = arg1.g2();
        } else if (arg2 == 1) {
            this.max = arg1.g2();
        }
        if (!arg0) {
            /* empty */
        }
    }
}

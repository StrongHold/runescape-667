import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!tia")
public final class Node_Sub1_Sub28 extends TextureOp {

    @OriginalMember(owner = "client!tia", name = "F", descriptor = "[I")
    public int[] anIntArray724;

    @OriginalMember(owner = "client!tia", name = "O", descriptor = "[I")
    public int[] anIntArray725;

    @OriginalMember(owner = "client!tia", name = "K", descriptor = "I")
    public int anInt9408 = 2048;

    @OriginalMember(owner = "client!tia", name = "Q", descriptor = "I")
    public int anInt9409 = 0;

    @OriginalMember(owner = "client!tia", name = "N", descriptor = "I")
    public int anInt9415 = 10;

    @OriginalMember(owner = "client!tia", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub28() {
        super(0, true);
    }

    @OriginalMember(owner = "client!tia", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            return;
        }
        if (arg2 == 0) {
            this.anInt9415 = arg1.g1();
        } else if (arg2 == 1) {
            this.anInt9408 = arg1.g2();
        } else if (arg2 == 2) {
            this.anInt9409 = arg1.g1();
        }
    }

    @OriginalMember(owner = "client!tia", name = "g", descriptor = "(I)V")
    public void method8282() {
        this.anIntArray725 = new int[this.anInt9415 + 1];
        this.anIntArray724 = new int[this.anInt9415 + 1];
        @Pc(21) int local21 = 0;
        @Pc(26) int local26 = 4096 / this.anInt9415;
        @Pc(33) int local33 = local26 * this.anInt9408 >> 12;
        for (@Pc(35) int local35 = 0; local35 < this.anInt9415; local35++) {
            this.anIntArray724[local35] = local21;
            this.anIntArray725[local35] = local21 + local33;
            local21 += local26;
        }
        this.anIntArray724[this.anInt9415] = 4096;
        if (-3749 != -3749) {
            this.method9416(true, null, 96);
        }
        this.anIntArray725[this.anInt9415] = this.anIntArray725[0] + 4096;
    }

    @OriginalMember(owner = "client!tia", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(11) int[] local11 = super.monochromeCache.get(arg1);
        if (arg0 <= 107) {
            InterfaceManager.rectangles = null;
        }
        if (super.monochromeCache.dirty) {
            @Pc(26) int local26 = MonochromeImageCache.anIntArray341[arg1];
            @Pc(35) int local35;
            if (this.anInt9409 == 0) {
                @Pc(153) short local153 = 0;
                for (local35 = 0; local35 < this.anInt9415; local35++) {
                    if (this.anIntArray724[local35] <= local26 && this.anIntArray724[local35 + 1] > local26) {
                        if (this.anIntArray725[local35] > local26) {
                            local153 = 4096;
                        }
                        break;
                    }
                }
                Arrays.set(local11, 0, EnvironmentLight.anInt9289, local153);
            } else {
                for (@Pc(31) int local31 = 0; local31 < EnvironmentLight.anInt9289; local31++) {
                    local35 = 0;
                    @Pc(37) short local37 = 0;
                    @Pc(41) int local41 = EnvironmentLight.anIntArray92[local31];
                    @Pc(44) int local44 = this.anInt9409;
                    if (local44 == 1) {
                        local35 = local41;
                    } else if (local44 == 2) {
                        local35 = (local41 + local26 - 4096 >> 1) + 2048;
                    } else if (local44 == 3) {
                        local35 = (local41 - local26 >> 1) + 2048;
                    }
                    for (local44 = 0; local44 < this.anInt9415; local44++) {
                        if (local35 >= this.anIntArray724[local44] && local35 < this.anIntArray724[local44 + 1]) {
                            if (this.anIntArray725[local44] > local35) {
                                local37 = 4096;
                            }
                            break;
                        }
                    }
                    local11[local31] = local37;
                }
            }
        }
        return local11;
    }

    @OriginalMember(owner = "client!tia", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        this.method8282();
    }
}

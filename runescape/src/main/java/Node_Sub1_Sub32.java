import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!vda")
public final class Node_Sub1_Sub32 extends TextureOp {

    @OriginalMember(owner = "client!vda", name = "F", descriptor = "I")
    public int anInt10016 = 3216;

    @OriginalMember(owner = "client!vda", name = "G", descriptor = "I")
    public int anInt10017 = 3216;

    @OriginalMember(owner = "client!vda", name = "P", descriptor = "I")
    public int anInt10015 = 4096;

    @OriginalMember(owner = "client!vda", name = "S", descriptor = "[I")
    public final int[] anIntArray789 = new int[3];

    @OriginalMember(owner = "client!vda", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub32() {
        super(1, true);
    }

    @OriginalMember(owner = "client!vda", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            this.method9416(false, null, 34);
        }
        if (arg2 == 0) {
            this.anInt10015 = arg1.g2();
        } else if (arg2 == 1) {
            this.anInt10016 = arg1.g2();
        } else if (arg2 == 2) {
            this.anInt10017 = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!vda", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(11) int[] local11 = super.monochromeCache.get(arg1);
        if (super.monochromeCache.dirty) {
            @Pc(22) int local22 = EnvironmentLight.anInt10157 * this.anInt10015 >> 12;
            @Pc(32) int[] local32 = this.method9422(EnvironmentLight.anInt7343 & arg1 - 1, 0);
            @Pc(38) int[] local38 = this.method9422(arg1, 0);
            @Pc(48) int[] local48 = this.method9422(arg1 + 1 & EnvironmentLight.anInt7343, 0);
            for (@Pc(50) int local50 = 0; local50 < EnvironmentLight.anInt9289; local50++) {
                @Pc(65) int local65 = local22 * (local48[local50] - local32[local50]) >> 12;
                @Pc(86) int local86 = (local38[EnvironmentLight.anInt8580 & local50 - 1] - local38[EnvironmentLight.anInt8580 & local50 + 1]) * local22 >> 12;
                @Pc(90) int local90 = local86 >> 4;
                if (local90 < 0) {
                    local90 = -local90;
                }
                @Pc(99) int local99 = local65 >> 4;
                if (local90 > 255) {
                    local90 = 255;
                }
                if (local99 < 0) {
                    local99 = -local99;
                }
                if (local99 > 255) {
                    local99 = 255;
                }
                @Pc(133) int local133 = Static204.aByteArray103[local90 + ((local99 + 1) * local99 >> 1)] & 0xFF;
                @Pc(139) int local139 = local86 * local133 >> 8;
                @Pc(145) int local145 = local65 * local133 >> 8;
                @Pc(151) int local151 = local133 * 4096 >> 8;
                @Pc(160) int local160 = this.anIntArray789[0] * local139 >> 12;
                @Pc(169) int local169 = this.anIntArray789[1] * local145 >> 12;
                @Pc(178) int local178 = local151 * this.anIntArray789[2] >> 12;
                local11[local50] = local178 + local169 + local160;
            }
        }
        if (arg0 < 107) {
            this.method8734((byte) 87);
        }
        return local11;
    }

    @OriginalMember(owner = "client!vda", name = "c", descriptor = "(B)V")
    public void method8734(@OriginalArg(0) byte arg0) {
        @Pc(11) double local11 = Math.cos((float) this.anInt10017 / 4096.0F);
        this.anIntArray789[0] = (int) (local11 * Math.sin((float) this.anInt10016 / 4096.0F) * 4096.0D);
        this.anIntArray789[1] = (int) (local11 * 4096.0D * Math.cos((float) this.anInt10016 / 4096.0F));
        this.anIntArray789[2] = (int) (Math.sin((float) this.anInt10017 / 4096.0F) * 4096.0D);
        @Pc(76) int local76 = this.anIntArray789[0] * this.anIntArray789[0] >> 12;
        @Pc(88) int local88 = this.anIntArray789[1] * this.anIntArray789[1] >> 12;
        @Pc(100) int local100 = this.anIntArray789[2] * this.anIntArray789[2] >> 12;
        @Pc(113) int local113 = (int) (Math.sqrt(local76 + local88 + local100 >> 12) * 4096.0D);
        if (local113 != 0) {
            this.anIntArray789[0] = (this.anIntArray789[0] << 12) / local113;
            this.anIntArray789[1] = (this.anIntArray789[1] << 12) / local113;
            this.anIntArray789[2] = (this.anIntArray789[2] << 12) / local113;
        }
    }

    @OriginalMember(owner = "client!vda", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        this.method8734((byte) -119);
    }
}

import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.util.Random;

@OriginalClass("client!kga")
public final class Node_Sub1_Sub15 extends TextureOp {

    @OriginalMember(owner = "client!kga", name = "F", descriptor = "I")
    public int anInt5418 = 0;

    @OriginalMember(owner = "client!kga", name = "R", descriptor = "I")
    public int anInt5419 = 0;

    @OriginalMember(owner = "client!kga", name = "J", descriptor = "I")
    public int anInt5424 = 4096;

    @OriginalMember(owner = "client!kga", name = "H", descriptor = "I")
    public int anInt5422 = 16;

    @OriginalMember(owner = "client!kga", name = "P", descriptor = "I")
    public int anInt5420 = 2000;

    @OriginalMember(owner = "client!kga", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub15() {
        super(0, true);
    }

    @OriginalMember(owner = "client!kga", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.anInt5419 = arg1.g1();
        } else if (arg2 == 1) {
            this.anInt5420 = arg1.g2();
        } else if (arg2 == 2) {
            this.anInt5422 = arg1.g1();
        } else if (arg2 == 3) {
            this.anInt5418 = arg1.g2();
        } else if (arg2 == 4) {
            this.anInt5424 = arg1.g2();
        }
        if (arg0) {
            this.monochromeOutput(-52, 90);
        }
    }

    @OriginalMember(owner = "client!kga", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(11) int[] local11 = super.monochromeCache.get(arg1);
        if (super.monochromeCache.dirty) {
            @Pc(20) int local20 = this.anInt5424 >> 1;
            @Pc(25) int[][] local25 = super.monochromeCache.get();
            @Pc(32) Random local32 = new Random(this.anInt5419);
            for (@Pc(34) int local34 = 0; local34 < this.anInt5420; local34++) {
                @Pc(60) int local60 = this.anInt5424 > 0 ? this.anInt5418 + Node_Sub1_Sub27.method8326(-5208, this.anInt5424, local32) - local20 : this.anInt5418;
                @Pc(66) int local66 = local60 >> 4 & 0xFF;
                @Pc(71) int local71 = Node_Sub1_Sub27.method8326(-5208, EnvironmentLight.anInt9289, local32);
                @Pc(76) int local76 = Node_Sub1_Sub27.method8326(-5208, EnvironmentLight.anInt53, local32);
                @Pc(87) int local87 = local71 + (Static24.anIntArray33[local66] * this.anInt5422 >> 12);
                @Pc(98) int local98 = local76 + (this.anInt5422 * Static222.anIntArray289[local66] >> 12);
                @Pc(103) int local103 = local98 - local76;
                @Pc(108) int local108 = local87 - local71;
                if (local108 != 0 || local103 != 0) {
                    if (local103 < 0) {
                        local103 = -local103;
                    }
                    if (local108 < 0) {
                        local108 = -local108;
                    }
                    @Pc(134) boolean local134 = local103 > local108;
                    @Pc(138) int local138;
                    @Pc(140) int local140;
                    if (local134) {
                        local138 = local71;
                        local140 = local87;
                        local71 = local76;
                        local87 = local98;
                        local76 = local138;
                        local98 = local140;
                    }
                    if (local71 > local87) {
                        local138 = local71;
                        local140 = local76;
                        local71 = local87;
                        local87 = local138;
                        local76 = local98;
                        local98 = local140;
                    }
                    local138 = local76;
                    local140 = local87 - local71;
                    @Pc(179) int local179 = local98 - local76;
                    @Pc(184) int local184 = -local140 / 2;
                    @Pc(188) int local188 = 2048 / local140;
                    @Pc(198) int local198 = 1024 - (Node_Sub1_Sub27.method8326(-5208, 4096, local32) >> 2);
                    if (local179 < 0) {
                        local179 = -local179;
                    }
                    @Pc(214) int local214 = local98 > local76 ? 1 : -1;
                    for (@Pc(216) int local216 = local71; local216 < local87; local216++) {
                        @Pc(228) int local228 = local188 * (local216 - local71) + local198 + 1024;
                        @Pc(232) int local232 = EnvironmentLight.anInt8580 & local216;
                        @Pc(236) int local236 = local138 & EnvironmentLight.anInt7343;
                        if (local134) {
                            local25[local236][local232] = local228;
                        } else {
                            local25[local232][local236] = local228;
                        }
                        local184 += local179;
                        if (local184 > 0) {
                            local138 += local214;
                            local184 += -local140;
                        }
                    }
                }
            }
        }
        if (arg0 < 107) {
            this.anInt5424 = -21;
        }
        return local11;
    }

    @OriginalMember(owner = "client!kga", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        Static481.method6475();
    }
}

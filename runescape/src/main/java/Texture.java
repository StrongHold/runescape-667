import com.jagex.core.datastruct.key.Node2;
import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.texture.TextureOp;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!vm")
public final class Texture extends Node2 {

    @OriginalMember(owner = "client!vm", name = "E", descriptor = "[I")
    public final int[] anIntArray824;

    @OriginalMember(owner = "client!vm", name = "A", descriptor = "[I")
    public final int[] anIntArray823;

    @OriginalMember(owner = "client!vm", name = "t", descriptor = "Lclient!pf;")
    public final TextureOp aClass2_Sub1_2;

    @OriginalMember(owner = "client!vm", name = "D", descriptor = "Lclient!pf;")
    public final TextureOp aClass2_Sub1_3;

    @OriginalMember(owner = "client!vm", name = "v", descriptor = "Lclient!pf;")
    public final TextureOp aClass2_Sub1_1;

    @OriginalMember(owner = "client!vm", name = "F", descriptor = "[Lclient!pf;")
    public final TextureOp[] aClass2_Sub1Array39;

    @OriginalMember(owner = "client!vm", name = "<init>", descriptor = "()V")
    public Texture() {
        this.anIntArray824 = new int[0];
        this.anIntArray823 = new int[0];
        this.aClass2_Sub1_2 = new Node_Sub1_Sub17(0);
        this.aClass2_Sub1_2.cacheSize = 1;
        this.aClass2_Sub1_3 = new Node_Sub1_Sub17();
        this.aClass2_Sub1_3.cacheSize = 1;
        this.aClass2_Sub1_1 = new Node_Sub1_Sub17();
        this.aClass2_Sub1_1.cacheSize = 1;
        this.aClass2_Sub1Array39 = new TextureOp[]{this.aClass2_Sub1_3, this.aClass2_Sub1_1, this.aClass2_Sub1_2};
    }

    @OriginalMember(owner = "client!vm", name = "<init>", descriptor = "(Lclient!ge;)V")
    public Texture(@OriginalArg(0) Packet arg0) {
        @Pc(7) int local7 = arg0.g1();
        @Pc(9) int local9 = 0;
        @Pc(11) int local11 = 0;
        @Pc(14) int[][] local14 = new int[local7][];
        this.aClass2_Sub1Array39 = new TextureOp[local7];
        @Pc(56) int local56;
        for (@Pc(20) int local20 = 0; local20 < local7; local20++) {
            @Pc(28) TextureOp local28 = Static294.method4341(arg0);
            if (local28.getSpriteId() >= 0) {
                local9++;
            }
            if (local28.getTextureId() >= 0) {
                local11++;
            }
            @Pc(49) int local49 = local28.ops.length;
            local14[local20] = new int[local49];
            for (local56 = 0; local56 < local49; local56++) {
                local14[local20][local56] = arg0.g1();
            }
            this.aClass2_Sub1Array39[local20] = local28;
        }
        this.anIntArray824 = new int[local9];
        this.anIntArray823 = new int[local11];
        local9 = 0;
        local11 = 0;
        for (@Pc(105) int local105 = 0; local105 < local7; local105++) {
            @Pc(114) TextureOp local114 = this.aClass2_Sub1Array39[local105];
            local56 = local114.ops.length;
            for (@Pc(120) int local120 = 0; local120 < local56; local120++) {
                local114.ops[local120] = this.aClass2_Sub1Array39[local14[local105][local120]];
            }
            @Pc(148) int local148 = local114.getSpriteId();
            @Pc(152) int local152 = local114.getTextureId();
            if (local148 > 0) {
                this.anIntArray824[local9++] = local148;
            }
            if (local152 > 0) {
                this.anIntArray823[local11++] = local152;
            }
            local14[local105] = null;
        }
        this.aClass2_Sub1_3 = this.aClass2_Sub1Array39[arg0.g1()];
        this.aClass2_Sub1_1 = this.aClass2_Sub1Array39[arg0.g1()];
        this.aClass2_Sub1_2 = this.aClass2_Sub1Array39[arg0.g1()];
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(BILclient!d;Lclient!sb;ZI)[F")
    public float[] method8946(@OriginalArg(1) int arg0, @OriginalArg(2) TextureSource arg1, @OriginalArg(3) js5 arg2, @OriginalArg(4) boolean arg3, @OriginalArg(5) int arg4) {
        Static582.aJs5_108 = arg2;
        Static677.anTextureSource_11 = arg1;
        for (@Pc(25) int local25 = 0; local25 < this.aClass2_Sub1Array39.length; local25++) {
            this.aClass2_Sub1Array39[local25].initCache(arg0, arg4);
        }
        EnvironmentLight.method2313(arg0, arg4);
        @Pc(54) float[] local54 = new float[arg4 * 4 * arg0];
        @Pc(56) int local56 = 0;
        for (@Pc(58) int local58 = 0; local58 < arg0; local58++) {
            @Pc(78) int[] local78;
            @Pc(80) int[] local80;
            @Pc(76) int[] local76;
            if (this.aClass2_Sub1_3.monochrome) {
                @Pc(74) int[] local74 = this.aClass2_Sub1_3.monochromeOutput(117, local58);
                local76 = local74;
                local78 = local74;
                local80 = local74;
            } else {
                @Pc(88) int[][] local88 = this.aClass2_Sub1_3.method9414(local58);
                local78 = local88[0];
                local76 = local88[2];
                local80 = local88[1];
            }
            @Pc(110) int[] local110;
            if (this.aClass2_Sub1_1.monochrome) {
                local110 = this.aClass2_Sub1_1.monochromeOutput(114, local58);
            } else {
                local110 = this.aClass2_Sub1_1.method9414(local58)[0];
            }
            if (arg3) {
                local56 = local58 << 2;
            }
            @Pc(136) int[] local136;
            if (this.aClass2_Sub1_2.monochrome) {
                local136 = this.aClass2_Sub1_2.monochromeOutput(115, local58);
            } else {
                local136 = this.aClass2_Sub1_2.method9414(local58)[0];
            }
            for (@Pc(150) int local150 = arg4 - 1; local150 >= 0; local150--) {
                @Pc(159) float local159 = (float) local110[local150] / 4096.0F;
                if (local159 < 0.0F) {
                    local159 = 0.0F;
                } else if (local159 > 1.0F) {
                    local159 = 1.0F;
                }
                @Pc(188) float local188 = ((float) local136[local150] * 31.0F / 4096.0F + 1.0F) / 4096.0F;
                local54[local56++] = local188 * (float) local78[local150];
                local54[local56++] = local188 * (float) local80[local150];
                local54[local56++] = local188 * (float) local76[local150];
                local54[local56++] = local159;
                if (arg3) {
                    local56 += (arg4 << 2) - 4;
                }
            }
        }
        for (@Pc(244) int local244 = 0; local244 < this.aClass2_Sub1Array39.length; local244++) {
            this.aClass2_Sub1Array39[local244].cacheReset();
        }
        return local54;
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(Lclient!sb;Lclient!d;B)Z")
    public boolean available(@OriginalArg(0) js5 arg0, @OriginalArg(1) TextureSource arg1) {
        @Pc(12) int local12;
        if (Static426.anInt940 < 0) {
            for (local12 = 0; local12 < this.anIntArray824.length; local12++) {
                if (!arg0.fileready(this.anIntArray824[local12])) {
                    return false;
                }
            }
        } else {
            for (local12 = 0; local12 < this.anIntArray824.length; local12++) {
                if (!arg0.requestdownload(this.anIntArray824[local12], Static426.anInt940)) {
                    return false;
                }
            }
        }
        if (4 != 4) {
            return true;
        }
        for (local12 = 0; local12 < this.anIntArray823.length; local12++) {
            if (!arg1.textureAvailable(this.anIntArray823[local12])) {
                return false;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(IDZIBLclient!d;Lclient!sb;)[I")
    public int[] method8948(@OriginalArg(0) int arg0, @OriginalArg(1) double arg1, @OriginalArg(2) boolean arg2, @OriginalArg(3) int arg3, @OriginalArg(5) TextureSource arg4, @OriginalArg(6) js5 arg5) {
        Static582.aJs5_108 = arg5;
        Static677.anTextureSource_11 = arg4;
        for (@Pc(11) int local11 = 0; local11 < this.aClass2_Sub1Array39.length; local11++) {
            this.aClass2_Sub1Array39[local11].initCache(arg3, arg0);
        }
        Static725.method9455(arg1);
        EnvironmentLight.method2313(arg3, arg0);
        @Pc(53) int[] local53 = new int[arg0 * arg3];
        @Pc(55) int local55 = 0;
        for (@Pc(57) int local57 = 0; local57 < arg3; local57++) {
            @Pc(77) int[] local77;
            @Pc(85) int[] local85;
            @Pc(81) int[] local81;
            @Pc(93) int[] local93;
            if (this.aClass2_Sub1_3.monochrome) {
                local93 = this.aClass2_Sub1_3.monochromeOutput(117, local57);
                local81 = local93;
                local85 = local93;
                local77 = local93;
            } else {
                @Pc(73) int[][] local73 = this.aClass2_Sub1_3.method9414(local57);
                local77 = local73[0];
                local81 = local73[2];
                local85 = local73[1];
            }
            if (this.aClass2_Sub1_1.monochrome) {
                local93 = this.aClass2_Sub1_1.monochromeOutput(111, local57);
            } else {
                local93 = this.aClass2_Sub1_1.method9414(local57)[0];
            }
            if (arg2) {
                local55 = local57;
            }
            for (@Pc(127) int local127 = arg0 - 1; local127 >= 0; local127--) {
                @Pc(135) int local135 = local77[local127] >> 4;
                if (local135 > 255) {
                    local135 = 255;
                }
                if (local135 < 0) {
                    local135 = 0;
                }
                @Pc(150) int local150 = local85[local127] >> 4;
                if (local150 > 255) {
                    local150 = 255;
                }
                if (local150 < 0) {
                    local150 = 0;
                }
                @Pc(165) int local165 = local81[local127] >> 4;
                if (local165 > 255) {
                    local165 = 255;
                }
                local150 = Static609.anIntArray716[local150];
                if (local165 < 0) {
                    local165 = 0;
                }
                local135 = Static609.anIntArray716[local135];
                local165 = Static609.anIntArray716[local165];
                @Pc(209) int local209;
                if (local135 == 0 && local150 == 0 && local165 == 0) {
                    local209 = 0;
                } else {
                    local209 = local93[local127] >> 4;
                    if (local209 > 255) {
                        local209 = 255;
                    }
                    if (local209 < 0) {
                        local209 = 0;
                    }
                }
                local53[local55++] = (local150 << 8) + (local209 << 24) + (local135 << 16) + local165;
                if (arg2) {
                    local55 += arg0 - 1;
                }
            }
        }
        for (@Pc(268) int local268 = 0; local268 < this.aClass2_Sub1Array39.length; local268++) {
            this.aClass2_Sub1Array39[local268].cacheReset();
        }
        return local53;
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(Lclient!sb;DZLclient!d;ZIII)[I")
    public int[] method8951(@OriginalArg(0) js5 arg0, @OriginalArg(1) double arg1, @OriginalArg(2) boolean arg2, @OriginalArg(3) TextureSource arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        Static677.anTextureSource_11 = arg3;
        Static582.aJs5_108 = arg0;
        for (@Pc(11) int local11 = 0; local11 < this.aClass2_Sub1Array39.length; local11++) {
            this.aClass2_Sub1Array39[local11].initCache(arg5, arg6);
        }
        Static725.method9455(arg1);
        EnvironmentLight.method2313(arg5, arg6);
        @Pc(41) int[] local41 = new int[arg5 * arg6];
        @Pc(49) int local49;
        @Pc(47) int local47;
        @Pc(51) byte local51;
        if (arg4) {
            local47 = -1;
            local49 = arg6 - 1;
            local51 = -1;
        } else {
            local47 = arg6;
            local49 = 0;
            local51 = 1;
        }
        @Pc(63) int local63 = 0;
        for (@Pc(65) int local65 = 0; local65 < arg5; local65++) {
            @Pc(85) int[] local85;
            @Pc(81) int[] local81;
            @Pc(83) int[] local83;
            if (this.aClass2_Sub1_3.monochrome) {
                @Pc(79) int[] local79 = this.aClass2_Sub1_3.monochromeOutput(127, local65);
                local81 = local79;
                local83 = local79;
                local85 = local79;
            } else {
                @Pc(93) int[][] local93 = this.aClass2_Sub1_3.method9414(local65);
                local83 = local93[2];
                local81 = local93[1];
                local85 = local93[0];
            }
            if (arg2) {
                local63 = local65;
            }
            for (@Pc(111) int local111 = local49; local111 != local47; local111 += local51) {
                @Pc(119) int local119 = local85[local111] >> 4;
                if (local119 > 255) {
                    local119 = 255;
                }
                if (local119 < 0) {
                    local119 = 0;
                }
                @Pc(137) int local137 = local81[local111] >> 4;
                if (local137 > 255) {
                    local137 = 255;
                }
                if (local137 < 0) {
                    local137 = 0;
                }
                @Pc(154) int local154 = local83[local111] >> 4;
                if (local154 > 255) {
                    local154 = 255;
                }
                local137 = Static609.anIntArray716[local137];
                local119 = Static609.anIntArray716[local119];
                if (local154 < 0) {
                    local154 = 0;
                }
                local154 = Static609.anIntArray716[local154];
                @Pc(189) int local189 = (local119 << 16) + (local137 << 8) + local154;
                if (local189 != 0) {
                    local189 |= 0xFF000000;
                }
                local41[local63++] = local189;
                if (arg2) {
                    local63 += arg6 - 1;
                }
            }
        }
        for (@Pc(230) int local230 = 0; local230 < this.aClass2_Sub1Array39.length; local230++) {
            this.aClass2_Sub1Array39[local230].cacheReset();
        }
        return local41;
    }
}

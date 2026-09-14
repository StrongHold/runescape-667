import com.jagex.core.io.Packet;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.util.Random;

/**
 * Generates a wall of randomly sized stones, packed in rows that rest on the stones below them,
 * each with a bevelled edge and its own shade.
 */
@OriginalClass("client!dd")
public final class TextureOpStoneWall extends TextureOp {

    @OriginalMember(owner = "client!dd", name = "U", descriptor = "I")
    public int bevelSize;

    @OriginalMember(owner = "client!dd", name = "Z", descriptor = "I")
    public int maxWidth = 2048;

    @OriginalMember(owner = "client!dd", name = "N", descriptor = "I")
    public int bevel = 1024;

    @OriginalMember(owner = "client!dd", name = "T", descriptor = "I")
    public int bevelShape = 0;

    @OriginalMember(owner = "client!dd", name = "L", descriptor = "I")
    public int maxHeight = 819;

    @OriginalMember(owner = "client!dd", name = "J", descriptor = "I")
    public int seed = 0;

    @OriginalMember(owner = "client!dd", name = "I", descriptor = "I")
    public int bevelVariance = 1024;

    @OriginalMember(owner = "client!dd", name = "Y", descriptor = "I")
    public int minWidth = 1024;

    @OriginalMember(owner = "client!dd", name = "V", descriptor = "I")
    public int minHeight = 409;

    @OriginalMember(owner = "client!dd", name = "P", descriptor = "I")
    public int shadeVariance = 1024;

    @OriginalMember(owner = "client!dd", name = "<init>", descriptor = "()V")
    public TextureOpStoneWall() {
        super(0, true);
    }

    @OriginalMember(owner = "client!dd", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(11) int[] local11 = super.monochromeCache.get(arg1);
        if (arg0 <= 107) {
            StringTools.numberWithSign(-62, 43, true);
        }
        if (!super.monochromeCache.dirty) {
            return local11;
        }
        @Pc(31) int[][] local31 = super.monochromeCache.get();
        @Pc(33) int local33 = 0;
        @Pc(35) int local35 = 0;
        @Pc(37) int local37 = 0;
        @Pc(39) int local39 = 0;
        @Pc(41) int local41 = 0;
        @Pc(43) boolean local43 = true;
        @Pc(45) boolean local45 = true;
        @Pc(47) int local47 = 0;
        @Pc(49) int local49 = 0;
        @Pc(56) int local56 = EnvironmentLight.anInt9289 * this.minWidth >> 12;
        @Pc(63) int local63 = this.maxWidth * EnvironmentLight.anInt9289 >> 12;
        @Pc(70) int local70 = EnvironmentLight.anInt53 * this.minHeight >> 12;
        @Pc(77) int local77 = this.maxHeight * EnvironmentLight.anInt53 >> 12;
        if (local77 <= 1) {
            return local31[arg1];
        }
        this.bevelSize = EnvironmentLight.anInt9289 / 8 * this.bevel >> 12;
        @Pc(101) int local101 = EnvironmentLight.anInt9289 / local56 + 1;
        @Pc(105) int[][] local105 = new int[local101][3];
        @Pc(109) int[][] local109 = new int[local101][3];
        @Pc(116) Random local116 = new Random(this.seed);
        while (true) {
            while (true) {
                @Pc(126) int local126 = local56 + Node_Sub1_Sub27.method8326(-5208, local63 - local56, local116);
                @Pc(136) int local136 = local70 + Node_Sub1_Sub27.method8326(-5208, local77 - local70, local116);
                @Pc(141) int local141 = local39 + local126;
                if (local141 > EnvironmentLight.anInt9289) {
                    local126 = EnvironmentLight.anInt9289 - local39;
                    local141 = EnvironmentLight.anInt9289;
                }
                @Pc(157) int local157;
                @Pc(167) int local167;
                if (local45) {
                    local157 = 0;
                } else {
                    @Pc(161) int local161 = local41;
                    @Pc(165) int[] local165 = local109[local41];
                    local167 = 0;
                    @Pc(171) int local171 = local33 + local141;
                    if (local171 < 0) {
                        local171 += EnvironmentLight.anInt9289;
                    }
                    if (EnvironmentLight.anInt9289 < local171) {
                        local171 -= EnvironmentLight.anInt9289;
                    }
                    local157 = local165[2];
                    while (true) {
                        @Pc(192) int[] local192 = local109[local161];
                        if (local192[0] <= local171 && local192[1] >= local171) {
                            if (local161 != local41) {
                                @Pc(234) int local234 = local33 + local39;
                                if (local234 < 0) {
                                    local234 += EnvironmentLight.anInt9289;
                                }
                                if (EnvironmentLight.anInt9289 < local234) {
                                    local234 -= EnvironmentLight.anInt9289;
                                }
                                for (@Pc(249) int local249 = 1; local249 <= local167; local249++) {
                                    @Pc(259) int[] local259 = local109[(local249 + local41) % local47];
                                    local157 = Math.max(local157, local259[2]);
                                }
                                for (@Pc(275) int local275 = 0; local275 <= local167; local275++) {
                                    @Pc(285) int[] local285 = local109[(local275 + local41) % local47];
                                    @Pc(289) int local289 = local285[2];
                                    if (local289 != local157) {
                                        @Pc(300) int local300 = local285[0];
                                        @Pc(304) int local304 = local285[1];
                                        @Pc(311) int local311;
                                        @Pc(315) int local315;
                                        if (local234 < local171) {
                                            local311 = Math.max(local234, local300);
                                            local315 = Math.min(local171, local304);
                                        } else if (local300 == 0) {
                                            local311 = 0;
                                            local315 = Math.min(local171, local304);
                                        } else {
                                            local311 = Math.max(local234, local300);
                                            local315 = EnvironmentLight.anInt9289;
                                        }
                                        this.drawStone(local31, local157 - local289, local116, local289, local37 + local311, -local311 + local315, (byte) -58);
                                    }
                                }
                            }
                            local41 = local161;
                            break;
                        }
                        local167++;
                        @Pc(212) int local212 = ~local47;
                        local161++;
                        if (local212 >= ~local161) {
                            local161 = 0;
                        }
                    }
                }
                if (local157 + local136 > EnvironmentLight.anInt53) {
                    local136 = EnvironmentLight.anInt53 - local157;
                } else {
                    local43 = false;
                }
                @Pc(406) int[] local406;
                if (local141 == EnvironmentLight.anInt9289) {
                    this.drawStone(local31, local136, local116, local157, local35 + local39, local126, (byte) -58);
                    if (local43) {
                        return local11;
                    }
                    local43 = true;
                    local406 = local105[local49++];
                    local406[1] = local141;
                    local406[2] = local136 + local157;
                    local406[0] = local39;
                    @Pc(422) int[][] local422 = local109;
                    local109 = local105;
                    local47 = local49;
                    local105 = local422;
                    local49 = 0;
                    local37 = local35;
                    local35 = Node_Sub1_Sub27.method8326(-5208, EnvironmentLight.anInt9289, local116);
                    local33 = local35 - local37;
                    local39 = 0;
                    local167 = local33;
                    if (local33 < 0) {
                        local167 = local33 + EnvironmentLight.anInt9289;
                    }
                    if (EnvironmentLight.anInt9289 < local167) {
                        local167 -= EnvironmentLight.anInt9289;
                    }
                    local41 = 0;
                    while (true) {
                        @Pc(468) int[] local468 = local109[local41];
                        if (local167 >= local468[0] && local167 <= local468[1]) {
                            local45 = false;
                            break;
                        }
                        local41++;
                        if (local41 >= local47) {
                            local41 = 0;
                        }
                    }
                } else {
                    local406 = local105[local49++];
                    local406[2] = local136 + local157;
                    local406[1] = local141;
                    local406[0] = local39;
                    this.drawStone(local31, local136, local116, local157, local39 + local35, local126, (byte) -58);
                    local39 = local141;
                }
            }
        }
    }

    @OriginalMember(owner = "client!dd", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        /* empty */
    }

    @OriginalMember(owner = "client!dd", name = "a", descriptor = "([[IILjava/util/Random;IIIB)V")
    public void drawStone(@OriginalArg(0) int[][] dest, @OriginalArg(1) int height, @OriginalArg(2) Random random, @OriginalArg(3) int y, @OriginalArg(4) int x, @OriginalArg(5) int width, @OriginalArg(6) byte arg6) {
        @Pc(22) int local22 = this.shadeVariance <= 0 ? 4096 : 4096 - Node_Sub1_Sub27.method8326(arg6 - 5150, this.shadeVariance, random);
        @Pc(30) int local30 = this.bevelVariance * this.bevelSize >> 12;
        @Pc(49) int local49 = this.bevelSize - (local30 <= 0 ? 0 : Node_Sub1_Sub27.method8326(arg6 ^ 0x146E, local30, random));
        if (EnvironmentLight.anInt9289 <= x) {
            x -= EnvironmentLight.anInt9289;
        }
        if (arg6 != -58) {
            return;
        }
        @Pc(82) int local82;
        @Pc(84) int local84;
        if (local49 <= 0) {
            if (EnvironmentLight.anInt9289 < width + x) {
                local82 = EnvironmentLight.anInt9289 - x;
                for (local84 = 0; local84 < height; local84++) {
                    @Pc(92) int[] local92 = dest[local84 + y];
                    Arrays.set(local92, x, local82, local22);
                    Arrays.set(local92, 0, width - local82, local22);
                }
            } else {
                for (local82 = 0; local82 < height; local82++) {
                    Arrays.set(dest[local82 + y], x, width, local22);
                }
            }
        } else if (height > 0 && width > 0) {
            local82 = width / 2;
            local84 = height / 2;
            @Pc(166) int local166 = local49 > local82 ? local82 : local49;
            @Pc(178) int local178 = local84 >= local49 ? local49 : local84;
            @Pc(182) int local182 = local166 + x;
            @Pc(189) int local189 = width - local166 * 2;
            for (@Pc(191) int local191 = 0; local191 < height; local191++) {
                @Pc(199) int[] local199 = dest[y + local191];
                @Pc(212) int local212;
                @Pc(217) int local217;
                @Pc(225) int local225;
                if (local191 < local178) {
                    local212 = local22 * local191 / local178;
                    if (this.bevelShape == 0) {
                        for (local217 = 0; local217 < local166; local217++) {
                            local225 = local217 * local22 / local166;
                            local199[local217 + x & EnvironmentLight.anInt8580] = local199[EnvironmentLight.anInt8580 & width + x - local217 - 1] = local225 * local212 >> 12;
                        }
                    } else {
                        for (local217 = 0; local217 < local166; local217++) {
                            local225 = local217 * local22 / local166;
                            local199[EnvironmentLight.anInt8580 & local217 + x] = local199[EnvironmentLight.anInt8580 & x + width - local217 - 1] = local212 > local225 ? local225 : local212;
                        }
                    }
                    if (EnvironmentLight.anInt9289 < local182 + local189) {
                        local217 = EnvironmentLight.anInt9289 - local182;
                        Arrays.set(local199, local182, local217, local212);
                        Arrays.set(local199, 0, local189 - local217, local212);
                    } else {
                        Arrays.set(local199, local182, local189, local212);
                    }
                } else {
                    local212 = height - local191 - 1;
                    if (local212 < local178) {
                        local217 = local212 * local22 / local178;
                        @Pc(372) int local372;
                        if (this.bevelShape == 0) {
                            for (local225 = 0; local225 < local166; local225++) {
                                local372 = local22 * local225 / local166;
                                local199[x + local225 & EnvironmentLight.anInt8580] = local199[width + x - local225 - 1 & EnvironmentLight.anInt8580] = local217 * local372 >> 12;
                            }
                        } else {
                            for (local225 = 0; local225 < local166; local225++) {
                                local372 = local225 * local22 / local166;
                                local199[EnvironmentLight.anInt8580 & x + local225] = local199[x + width - local225 - 1 & EnvironmentLight.anInt8580] = local217 > local372 ? local372 : local217;
                            }
                        }
                        if (EnvironmentLight.anInt9289 >= local189 + local182) {
                            Arrays.set(local199, local182, local189, local217);
                        } else {
                            local225 = EnvironmentLight.anInt9289 - local182;
                            Arrays.set(local199, local182, local225, local217);
                            Arrays.set(local199, 0, local189 - local225, local217);
                        }
                    } else {
                        for (local217 = 0; local217 < local166; local217++) {
                            local199[EnvironmentLight.anInt8580 & x + local217] = local199[EnvironmentLight.anInt8580 & width + x - local217 - 1] = local217 * local22 / local166;
                        }
                        if (EnvironmentLight.anInt9289 >= local182 + local189) {
                            Arrays.set(local199, local182, local189, local22);
                        } else {
                            local225 = EnvironmentLight.anInt9289 - local182;
                            Arrays.set(local199, local182, local225, local22);
                            Arrays.set(local199, 0, local189 - local225, local22);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!dd", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            this.monochromeOutput(39, 29);
        }
        if (arg2 == 0) {
            this.seed = arg1.g1();
        } else if (arg2 == 1) {
            this.minWidth = arg1.g2();
        } else if (arg2 == 2) {
            this.maxWidth = arg1.g2();
        } else if (arg2 == 3) {
            this.minHeight = arg1.g2();
        } else if (arg2 == 4) {
            this.maxHeight = arg1.g2();
        } else if (arg2 == 5) {
            this.bevel = arg1.g2();
        } else if (arg2 == 6) {
            this.bevelShape = arg1.g1();
        } else if (arg2 == 7) {
            this.bevelVariance = arg1.g2();
        } else if (arg2 == 8) {
            this.shadeVariance = arg1.g2();
        }
    }
}

package com.jagex.graphics.texture;

import com.jagex.core.datastruct.key.LruCache;
import com.jagex.core.io.Packet;
import com.jagex.graphics.DoublyLinkedNode_Sub2_Sub7;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.util.Random;

@OriginalClass("client!so")
public final class Node_Sub1_Sub27 extends TextureOp {

    @OriginalMember(owner = "client!ut", name = "A", descriptor = "[I")
    public static final int[] anIntArray768 = new int[4096];

    @OriginalMember(owner = "client!ra", name = "p", descriptor = "Lclient!ts;")
    public static final LruCache A_DOUBLY_LINKED_LIST___4 = new LruCache(16);

    @OriginalMember(owner = "client!c", name = "a", descriptor = "(II)I")
    public static int method1310(@OriginalArg(0) int arg0) {
        @Pc(22) int local22 = (arg0 * arg0 >> 12) * arg0 >> 12;
        @Pc(28) int local28 = arg0 * 6 - 61440;
        @Pc(36) int local36 = (arg0 * local28 >> 12) + 40960;
        return local36 * local22 >> 12;
    }

    static {
        for (@Pc(123) int local123 = 0; local123 < 4096; local123++) {
            anIntArray768[local123] = method1310(local123);
        }
    }

    @OriginalMember(owner = "client!so", name = "Y", descriptor = "[S")
    public short[] aShortArray126;

    @OriginalMember(owner = "client!so", name = "U", descriptor = "[S")
    public short[] aShortArray127;

    @OriginalMember(owner = "client!so", name = "P", descriptor = "[B")
    public byte[] aByteArray97 = new byte[512];

    @OriginalMember(owner = "client!so", name = "I", descriptor = "Z")
    public boolean aBoolean667 = true;

    @OriginalMember(owner = "client!so", name = "V", descriptor = "I")
    public int anInt8803 = 4;

    @OriginalMember(owner = "client!so", name = "W", descriptor = "I")
    public int anInt8805 = 4;

    @OriginalMember(owner = "client!so", name = "Q", descriptor = "I")
    public int anInt8799 = 1638;

    @OriginalMember(owner = "client!so", name = "T", descriptor = "I")
    public int anInt8810 = 4;

    @OriginalMember(owner = "client!so", name = "M", descriptor = "I")
    public int anInt8809 = 0;

    @OriginalMember(owner = "client!so", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub27() {
        super(0, true);
    }

    @OriginalMember(owner = "client!vv", name = "a", descriptor = "(IZ)[B")
    public static byte[] method9027(@OriginalArg(0) int arg0) {
        @Pc(17) DoublyLinkedNode_Sub2_Sub7 local17 = (DoublyLinkedNode_Sub2_Sub7) A_DOUBLY_LINKED_LIST___4.get(arg0);
        if (local17 == null) {
            @Pc(22) byte[] local22 = new byte[512];
            @Pc(28) Random local28 = new Random(arg0);
            for (@Pc(30) int local30 = 0; local30 < 255; local30++) {
                local22[local30] = (byte) local30;
            }
            for (@Pc(42) int local42 = 0; local42 < 255; local42++) {
                @Pc(48) int local48 = 255 - local42;
                @Pc(53) int local53 = method8326(-5208, local48, local28);
                @Pc(57) byte local57 = local22[local53];
                local22[local53] = local22[local48];
                local22[local48] = local22[511 - local42] = local57;
            }
            local17 = new DoublyLinkedNode_Sub2_Sub7(local22);
            A_DOUBLY_LINKED_LIST___4.put(local17, arg0);
        }
        return local17.aByteArray21;
    }

    @OriginalMember(owner = "client!wq", name = "b", descriptor = "(III)I")
    public static int method9118(@OriginalArg(0) int arg0, @OriginalArg(2) int arg2) {
        @Pc(18) int local18 = arg2 - 1 & arg0 >> 31;
        return local18 + (arg0 + (arg0 >>> 31)) % arg2;
    }

    @OriginalMember(owner = "client!tm", name = "a", descriptor = "(IILjava/util/Random;)I")
    public static int method8326(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Random arg2) {
        if (arg1 <= 0) {
            throw new IllegalArgumentException();
        } else if (method9150(arg1)) {
            return (int) (((long) arg2.nextInt() & 0xFFFFFFFFL) * (long) arg1 >> 32);
        } else {
            @Pc(46) int local46 = Integer.MIN_VALUE - (int) (4294967296L % (long) arg1);
            @Pc(49) int local49;
            do {
                local49 = arg2.nextInt();
            } while (local49 >= local46);
            return method9118(local49, arg1);
        }
    }

    @OriginalMember(owner = "client!wca", name = "a", descriptor = "(IZ)Z")
    public static boolean method9150(@OriginalArg(0) int arg0) {
        return (arg0 & -arg0) == arg0;
    }

    @OriginalMember(owner = "client!so", name = "a", descriptor = "(I[II)V")
    public void method7809(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1) {
        @Pc(15) int local15 = this.anInt8805 * MonochromeImageCache.anIntArray341[arg0];
        @Pc(103) int local103;
        @Pc(117) int local117;
        @Pc(31) int local31;
        @Pc(24) short local24;
        @Pc(95) int local95;
        @Pc(37) int local37;
        @Pc(51) int local51;
        @Pc(44) int local44;
        @Pc(81) int local81;
        @Pc(55) int local55;
        @Pc(59) int local59;
        @Pc(77) int local77;
        @Pc(90) int local90;
        if (this.anInt8803 == 1) {
            local24 = this.aShortArray127[0];
            local31 = this.aShortArray126[0] << 12;
            local37 = local15 * local31 >> 12;
            local44 = this.anInt8805 * local31 >> 12;
            local51 = this.anInt8810 * local31 >> 12;
            local55 = local37 >> 12;
            local59 = local55 + 1;
            local37 &= 0xFFF;
            if (local44 <= local59) {
                local59 = 0;
            }
            local77 = this.aByteArray97[local55 & 0xFF] & 0xFF;
            local81 = anIntArray768[local37];
            local90 = this.aByteArray97[local59 & 0xFF] & 0xFF;
            if (this.aBoolean667) {
                for (local95 = 0; local95 < EnvironmentLight.anInt9289; local95++) {
                    local103 = this.anInt8810 * EnvironmentLight.anIntArray92[local95];
                    local117 = this.method7812(local51, local77, local31 * local103 >> 12, local37, local81, local90);
                    local117 = local117 * local24 >> 12;
                    arg1[local95] = (local117 >> 1) + 2048;
                }
            } else {
                for (local95 = 0; local95 < EnvironmentLight.anInt9289; local95++) {
                    local103 = EnvironmentLight.anIntArray92[local95] * this.anInt8810;
                    local117 = this.method7812(local51, local77, local31 * local103 >> 12, local37, local81, local90);
                    arg1[local95] = local117 * local24 >> 12;
                }
            }
            return;
        }
        local24 = this.aShortArray127[0];
        if (local24 > 8 || local24 < -8) {
            local31 = this.aShortArray126[0] << 12;
            local51 = local31 * this.anInt8810 >> 12;
            local37 = local31 * local15 >> 12;
            local44 = this.anInt8805 * local31 >> 12;
            local55 = local37 >> 12;
            local59 = local55 + 1;
            if (local44 <= local59) {
                local59 = 0;
            }
            local37 &= 0xFFF;
            local77 = this.aByteArray97[local55 & 0xFF] & 0xFF;
            local81 = anIntArray768[local37];
            local90 = this.aByteArray97[local59 & 0xFF] & 0xFF;
            for (local95 = 0; local95 < EnvironmentLight.anInt9289; local95++) {
                local103 = this.anInt8810 * EnvironmentLight.anIntArray92[local95];
                local117 = this.method7812(local51, local77, local103 * local31 >> 12, local37, local81, local90);
                arg1[local95] = local117 * local24 >> 12;
            }
        }
        for (@Pc(301) int local301 = 1; local301 < this.anInt8803; local301++) {
            local24 = this.aShortArray127[local301];
            if (local24 > 8 || local24 < -8) {
                local31 = this.aShortArray126[local301] << 12;
                local51 = local31 * this.anInt8810 >> 12;
                local44 = local31 * this.anInt8805 >> 12;
                local37 = local31 * local15 >> 12;
                local55 = local37 >> 12;
                local59 = local55 + 1;
                local37 &= 0xFFF;
                if (local44 <= local59) {
                    local59 = 0;
                }
                local90 = this.aByteArray97[local59 & 0xFF] & 0xFF;
                local81 = anIntArray768[local37];
                local77 = this.aByteArray97[local55 & 0xFF] & 0xFF;
                if (this.aBoolean667 && this.anInt8803 - 1 == local301) {
                    for (local95 = 0; local95 < EnvironmentLight.anInt9289; local95++) {
                        local103 = EnvironmentLight.anIntArray92[local95] * this.anInt8810;
                        local117 = this.method7812(local51, local77, local103 * local31 >> 12, local37, local81, local90);
                        local117 = (local117 * local24 >> 12) + arg1[local95];
                        arg1[local95] = (local117 >> 1) + 2048;
                    }
                } else {
                    for (local95 = 0; local95 < EnvironmentLight.anInt9289; local95++) {
                        local103 = EnvironmentLight.anIntArray92[local95] * this.anInt8810;
                        local117 = this.method7812(local51, local77, local31 * local103 >> 12, local37, local81, local90);
                        arg1[local95] += local117 * local24 >> 12;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!so", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(9) int[] local9 = super.monochromeCache.get(arg1);
        if (arg0 <= 107) {
            this.aShortArray126 = null;
        }
        if (super.monochromeCache.dirty) {
            this.method7809(arg1, local9);
        }
        return local9;
    }

    @OriginalMember(owner = "client!so", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        this.aByteArray97 = method9027(this.anInt8809);
        this.method7813(-1);
        @Pc(19) int local19 = this.anInt8803 - 1;
        while (local19 >= 1) {
            @Pc(29) short local29 = this.aShortArray127[local19];
            if (local29 > 8) {
                return;
            }
            if (local29 < -8) {
                return;
            }
            this.anInt8803--;
            local19--;
        }
    }

    @OriginalMember(owner = "client!so", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            this.monochromeOutput(17, -97);
        }
        if (arg2 == 0) {
            this.aBoolean667 = arg1.g1() == 1;
        } else if (arg2 == 1) {
            this.anInt8803 = arg1.g1();
        } else if (arg2 == 2) {
            this.anInt8799 = arg1.g2s();
            if (this.anInt8799 < 0) {
                this.aShortArray127 = new short[this.anInt8803];
                for (@Pc(103) int local103 = 0; local103 < this.anInt8803; local103++) {
                    this.aShortArray127[local103] = (short) arg1.g2s();
                }
                return;
            }
        } else if (arg2 == 3) {
            this.anInt8810 = this.anInt8805 = arg1.g1();
            return;
        } else if (arg2 == 4) {
            this.anInt8809 = arg1.g1();
            return;
        } else if (arg2 == 5) {
            this.anInt8810 = arg1.g1();
            return;
        } else if (arg2 == 6) {
            this.anInt8805 = arg1.g1();
            return;
        }
    }

    @OriginalMember(owner = "client!so", name = "a", descriptor = "(IIIIIII)I")
    public int method7812(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        @Pc(7) int local7 = arg2 >> 12;
        @Pc(11) int local11 = local7 + 1;
        @Pc(15) int local15 = arg2 & 0xFFF;
        if (arg0 <= local11) {
            local11 = 0;
        }
        local7 &= 0xFF;
        @Pc(32) int local32 = local15 - 4096;
        local11 &= 0xFF;
        @Pc(40) int local40 = arg3 - 4096;
        @Pc(44) int local44 = anIntArray768[local15];
        @Pc(63) int local63 = this.aByteArray97[arg1 + local7] & 0x3;
        @Pc(82) int local82;
        if (local63 <= 1) {
            local82 = local63 == 0 ? local15 + arg3 : -local15 + arg3;
        } else {
            local82 = local63 == 2 ? local15 - arg3 : -arg3 + -local15;
        }
        local63 = this.aByteArray97[local11 + arg1] & 0x3;
        @Pc(131) int local131;
        if (local63 > 1) {
            local131 = local63 == 2 ? local32 - arg3 : -arg3 + -local32;
        } else {
            local131 = local63 == 0 ? arg3 + local32 : -local32 + arg3;
        }
        local63 = this.aByteArray97[local7 + arg5] & 0x3;
        @Pc(164) int local164 = ((local131 - local82) * local44 >> 12) + local82;
        if (local63 <= 1) {
            local82 = local63 == 0 ? local40 + local15 : local40 + -local15;
        } else {
            local82 = local63 == 2 ? local15 - local40 : -local15 + -local40;
        }
        local63 = this.aByteArray97[arg5 + local11] & 0x3;
        if (local63 > 1) {
            local131 = local63 == 2 ? local32 - local40 : -local32 + -local40;
        } else {
            local131 = local63 == 0 ? local40 + local32 : -local32 + local40;
        }
        @Pc(259) int local259 = local82 + (local44 * (local131 - local82) >> 12);
        return (arg4 * (local259 - local164) >> 12) + local164;
    }

    @OriginalMember(owner = "client!so", name = "g", descriptor = "(I)V")
    public void method7813(@OriginalArg(0) int arg0) {
        @Pc(17) int local17;
        if (arg0 > ~this.anInt8799) {
            this.aShortArray126 = new short[this.anInt8803];
            this.aShortArray127 = new short[this.anInt8803];
            for (local17 = 0; local17 < this.anInt8803; local17++) {
                this.aShortArray127[local17] = (short) (int) (Math.pow((float) this.anInt8799 / 4096.0F, local17) * 4096.0D);
                this.aShortArray126[local17] = (short) (int) Math.pow(2.0D, local17);
            }
        } else if (this.aShortArray127 != null && this.anInt8803 == this.aShortArray127.length) {
            this.aShortArray126 = new short[this.anInt8803];
            for (local17 = 0; local17 < this.anInt8803; local17++) {
                this.aShortArray126[local17] = (short) (int) Math.pow(2.0D, local17);
            }
        }
    }
}

package com.jagex.graphics;

import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.math.ColourUtils;
import com.jagex.core.io.Packet;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!th")
public final class EnvironmentLight {

    @OriginalMember(owner = "client!vw", name = "u", descriptor = "[I")
    public static int[] anIntArray868;

    @OriginalMember(owner = "client!bo", name = "f", descriptor = "I")
    public static int anInt1066;

    @OriginalMember(owner = "client!hla", name = "d", descriptor = "I")
    public static int anInt3993;

    @OriginalMember(owner = "client!td", name = "j", descriptor = "I")
    public static int anInt9289;

    @OriginalMember(owner = "client!bq", name = "E", descriptor = "[I")
    public static int[] anIntArray92;

    @OriginalMember(owner = "client!sba", name = "g", descriptor = "I")
    public static int anInt8580;

    @OriginalMember(owner = "client!vga", name = "c", descriptor = "I")
    public static int anInt10157;

    @OriginalMember(owner = "client!aaa", name = "I", descriptor = "I")
    public static int anInt53;

    @OriginalMember(owner = "client!ph", name = "K", descriptor = "I")
    public static int anInt7343;

    @OriginalMember(owner = "client!iea", name = "o", descriptor = "[Lclient!th;")
    public static EnvironmentLight[] aEnvironmentLightArray1;

    @OriginalMember(owner = "client!th", name = "a", descriptor = "Z")
    public boolean aBoolean716;

    @OriginalMember(owner = "client!th", name = "t", descriptor = "I")
    public int level;

    @OriginalMember(owner = "client!th", name = "j", descriptor = "I")
    public int anInt9370;

    @OriginalMember(owner = "client!th", name = "h", descriptor = "I")
    public int anInt9371;

    @OriginalMember(owner = "client!th", name = "r", descriptor = "Lclient!lca;")
    public PointLight light;

    @OriginalMember(owner = "client!th", name = "v", descriptor = "Z")
    public boolean aBoolean717;

    @OriginalMember(owner = "client!th", name = "k", descriptor = "I")
    public int anInt9377;

    @OriginalMember(owner = "client!th", name = "u", descriptor = "[S")
    public short[] aShortArray131;

    @OriginalMember(owner = "client!th", name = "i", descriptor = "I")
    public int anInt9378;

    @OriginalMember(owner = "client!th", name = "l", descriptor = "I")
    public int anInt9379;

    @OriginalMember(owner = "client!th", name = "g", descriptor = "I")
    public int preset;

    @OriginalMember(owner = "client!th", name = "<init>", descriptor = "()V")
    public EnvironmentLight() {
        if (anIntArray868 == null) {
            method5043();
        }
        this.method8247();
    }

    @OriginalMember(owner = "client!th", name = "<init>", descriptor = "(Lclient!ha;Lclient!ge;I)V")
    public EnvironmentLight(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (anIntArray868 == null) {
            method5043();
        }
        this.level = arg1.g1();
        this.aBoolean716 = (this.level & 0x10) != 0;
        this.aBoolean717 = (this.level & 0x8) != 0;
        this.level &= 0x7;
        @Pc(47) int local47 = arg1.g2() << arg2;
        @Pc(53) int local53 = arg1.g2() << arg2;
        @Pc(59) int local59 = arg1.g2() << arg2;
        @Pc(63) int local63 = arg1.g1();
        @Pc(69) int local69 = local63 * 2 + 1;
        this.aShortArray131 = new short[local69];
        @Pc(85) int local85;
        for (@Pc(75) int local75 = 0; local75 < this.aShortArray131.length; local75++) {
            @Pc(81) short local81 = (short) arg1.g2();
            local85 = local81 >>> 8;
            if (local85 >= local69) {
                local85 = local69 - 1;
            }
            @Pc(100) int local100 = local81 & 0xFF;
            if (local100 > local69 - local85) {
                local100 = local69 - local85;
            }
            this.aShortArray131[local75] = (short) (local100 | local85 << 8);
        }
        local63 = (local63 << anInt1066) + anInt3993;
        @Pc(160) int local160 = ColourUtils.HSL_TO_RGB == null ? ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(arg1.g2()) & 0xFFFF] : ColourUtils.HSL_TO_RGB[arg1.g2()];
        local85 = arg1.g1();
        this.anInt9371 = (local85 & 0xE0) << 3;
        this.preset = local85 & 0x1F;
        if (this.preset != 31) {
            this.method8247();
        }
        this.method8246(arg0, local53, local59, local47, local160, local63);
    }

    @OriginalMember(owner = "client!kr", name = "a", descriptor = "(B)V")
    public static void method5043() {
        anIntArray868 = method3857(0.4F);
    }

    @OriginalMember(owner = "client!iea", name = "a", descriptor = "(IIZIIIFI)[I")
    public static int[] method3857(@OriginalArg(6) float arg0) {
        @Pc(6) int[] local6 = new int[2048];
        @Pc(10) Node_Sub1_Sub27 local10 = new Node_Sub1_Sub27();
        local10.anInt8799 = (int) (arg0 * 4096.0F);
        local10.aBoolean667 = true;
        local10.anInt8809 = 35;
        local10.anInt8805 = 8;
        local10.anInt8810 = 8;
        local10.anInt8803 = 4;
        local10.postDecode();
        method2313(1, 2048);
        local10.method7809(0, local6);
        return local6;
    }

    @OriginalMember(owner = "client!ec", name = "a", descriptor = "(III)V")
    public static void method2313(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        @Pc(7) int local7;
        if (anInt9289 != arg1) {
            anIntArray92 = new int[arg1];
            for (local7 = 0; local7 < arg1; local7++) {
                anIntArray92[local7] = (local7 << 12) / arg1;
            }
            anInt8580 = arg1 - 1;
            anInt9289 = arg1;
            anInt10157 = arg1 * 32;
        }
        if (arg0 == anInt53) {
            return;
        }
        if (anInt9289 == arg0) {
            MonochromeImageCache.anIntArray341 = anIntArray92;
        } else {
            MonochromeImageCache.anIntArray341 = new int[arg0];
            for (local7 = 0; local7 < arg0; local7++) {
                MonochromeImageCache.anIntArray341[local7] = (local7 << 12) / arg0;
            }
        }
        anInt7343 = arg0 - 1;
        anInt53 = arg0;
    }

    @OriginalMember(owner = "client!th", name = "a", descriptor = "(ZIB)V")
    public void method8241(@OriginalArg(0) boolean arg0, @OriginalArg(1) int arg1) {
        @Pc(71) int local71;
        if (arg0) {
            local71 = 2048;
        } else {
            @Pc(27) int local27 = arg1 * this.anInt9377 / 50 + this.anInt9371 & 0x7FF;
            @Pc(30) int local30 = this.anInt9379;
            if (local30 == 1) {
                local71 = (Trig1.SIN[local27 << 3] >> 4) + 1024;
            } else if (local30 == 3) {
                local71 = anIntArray868[local27] >> 1;
            } else if (local30 == 4) {
                local71 = local27 >> 10 << 11;
            } else if (local30 == 2) {
                local71 = local27;
            } else if (local30 == 5) {
                local71 = (local27 < 1024 ? local27 : 2048 - local27) << 1;
            } else {
                local71 = 2048;
            }
        }
        this.light.method8433((float) ((this.anInt9370 * local71 >> 11) + this.anInt9378) / 2048.0F);
    }

    @OriginalMember(owner = "client!th", name = "a", descriptor = "(IBIII)V")
    public void updateParameters(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        this.anInt9377 = arg3;
        this.anInt9378 = arg0;
        this.anInt9379 = arg1;
        this.anInt9370 = arg2;
    }

    @OriginalMember(owner = "client!th", name = "a", descriptor = "(Lclient!ha;IIIIII)V")
    public void method8246(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.light = arg0.method7941(arg3, arg2, arg1, arg5, arg4, (float) 1);
    }

    @OriginalMember(owner = "client!th", name = "c", descriptor = "(I)V")
    public void method8247() {
        @Pc(9) int local9 = this.preset;
        if (local9 == 2) {
            this.anInt9377 = 2048;
            this.anInt9370 = 2048;
            this.anInt9379 = 1;
            this.anInt9378 = 0;
        } else if (local9 == 3) {
            this.anInt9377 = 4096;
            this.anInt9378 = 0;
            this.anInt9370 = 2048;
            this.anInt9379 = 1;
        } else if (local9 == 4) {
            this.anInt9378 = 0;
            this.anInt9379 = 4;
            this.anInt9370 = 2048;
            this.anInt9377 = 2048;
        } else if (local9 == 5) {
            this.anInt9377 = 8192;
            this.anInt9379 = 4;
            this.anInt9378 = 0;
            this.anInt9370 = 2048;
        } else if (local9 == 12) {
            this.anInt9379 = 2;
            this.anInt9370 = 2048;
            this.anInt9377 = 2048;
            this.anInt9378 = 0;
        } else if (local9 == 13) {
            this.anInt9370 = 2048;
            this.anInt9379 = 2;
            this.anInt9378 = 0;
            this.anInt9377 = 8192;
        } else if (local9 == 10) {
            this.anInt9370 = 512;
            this.anInt9378 = 1536;
            this.anInt9377 = 2048;
            this.anInt9379 = 3;
        } else if (local9 == 11) {
            this.anInt9377 = 4096;
            this.anInt9379 = 3;
            this.anInt9378 = 1536;
            this.anInt9370 = 512;
        } else if (local9 == 6) {
            this.anInt9378 = 1280;
            this.anInt9377 = 2048;
            this.anInt9379 = 3;
            this.anInt9370 = 768;
        } else if (local9 == 7) {
            this.anInt9379 = 3;
            this.anInt9378 = 1280;
            this.anInt9370 = 768;
            this.anInt9377 = 4096;
        } else if (local9 == 8) {
            this.anInt9377 = 2048;
            this.anInt9378 = 1024;
            this.anInt9379 = 3;
            this.anInt9370 = 1024;
        } else if (local9 == 9) {
            this.anInt9377 = 4096;
            this.anInt9378 = 1024;
            this.anInt9370 = 1024;
            this.anInt9379 = 3;
        } else if (local9 == 14) {
            this.anInt9379 = 1;
            this.anInt9370 = 768;
            this.anInt9377 = 2048;
            this.anInt9378 = 1280;
        } else if (local9 == 15) {
            this.anInt9379 = 1;
            this.anInt9370 = 512;
            this.anInt9378 = 1536;
            this.anInt9377 = 4096;
        } else if (local9 == 16) {
            this.anInt9379 = 1;
            this.anInt9377 = 8192;
            this.anInt9378 = 1792;
            this.anInt9370 = 256;
        } else {
            this.anInt9378 = 0;
            this.anInt9370 = 2048;
            this.anInt9379 = 0;
            this.anInt9377 = 2048;
        }
    }
}

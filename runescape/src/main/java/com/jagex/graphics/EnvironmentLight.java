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
    public static int[] noise;

    @OriginalMember(owner = "client!bo", name = "f", descriptor = "I")
    public static int tileShift;

    @OriginalMember(owner = "client!hla", name = "d", descriptor = "I")
    public static int halfTileSize;

    @OriginalMember(owner = "client!td", name = "j", descriptor = "I")
    public static int textureOpWidth;

    @OriginalMember(owner = "client!bq", name = "E", descriptor = "[I")
    public static int[] textureOpColumns;

    @OriginalMember(owner = "client!sba", name = "g", descriptor = "I")
    public static int textureOpWidthMask;

    @OriginalMember(owner = "client!vga", name = "c", descriptor = "I")
    public static int textureOpDepthScale;

    @OriginalMember(owner = "client!aaa", name = "I", descriptor = "I")
    public static int textureOpHeight;

    @OriginalMember(owner = "client!ph", name = "K", descriptor = "I")
    public static int textureOpHeightMask;

    @OriginalMember(owner = "client!iea", name = "o", descriptor = "[Lclient!th;")
    public static EnvironmentLight[] lights;

    @OriginalMember(owner = "client!th", name = "a", descriptor = "Z")
    public boolean spansLevelsBelow;

    @OriginalMember(owner = "client!th", name = "t", descriptor = "I")
    public int level;

    @OriginalMember(owner = "client!th", name = "j", descriptor = "I")
    public int amplitude;

    @OriginalMember(owner = "client!th", name = "h", descriptor = "I")
    public int phase;

    @OriginalMember(owner = "client!th", name = "r", descriptor = "Lclient!lca;")
    public PointLight light;

    @OriginalMember(owner = "client!th", name = "v", descriptor = "Z")
    public boolean spansLevelsAbove;

    @OriginalMember(owner = "client!th", name = "k", descriptor = "I")
    public int frequency;

    @OriginalMember(owner = "client!th", name = "u", descriptor = "[S")
    public short[] rowSpans;

    @OriginalMember(owner = "client!th", name = "i", descriptor = "I")
    public int ambient;

    @OriginalMember(owner = "client!th", name = "l", descriptor = "I")
    public int pattern;

    @OriginalMember(owner = "client!th", name = "g", descriptor = "I")
    public int preset;

    @OriginalMember(owner = "client!th", name = "<init>", descriptor = "()V")
    public EnvironmentLight() {
        if (noise == null) {
            initNoise();
        }
        this.applyPreset();
    }

    @OriginalMember(owner = "client!th", name = "<init>", descriptor = "(Lclient!ha;Lclient!ge;I)V")
    public EnvironmentLight(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) Packet packet, @OriginalArg(2) int shift) {
        if (noise == null) {
            initNoise();
        }
        this.level = packet.g1();
        this.spansLevelsBelow = (this.level & 0x10) != 0;
        this.spansLevelsAbove = (this.level & 0x8) != 0;
        this.level &= 0x7;
        @Pc(47) int x = packet.g2() << shift;
        @Pc(53) int z = packet.g2() << shift;
        @Pc(59) int y = packet.g2() << shift;
        @Pc(63) int radius = packet.g1();
        @Pc(69) int rowCount = radius * 2 + 1;
        this.rowSpans = new short[rowCount];
        @Pc(85) int packed;
        for (@Pc(75) int row = 0; row < this.rowSpans.length; row++) {
            @Pc(81) short span = (short) packet.g2();
            packed = span >>> 8;
            if (packed >= rowCount) {
                packed = rowCount - 1;
            }
            @Pc(100) int length = span & 0xFF;
            if (length > rowCount - packed) {
                length = rowCount - packed;
            }
            this.rowSpans[row] = (short) (length | packed << 8);
        }
        radius = (radius << tileShift) + halfTileSize;
        @Pc(160) int colour = ColourUtils.HSL_TO_RGB == null ? ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(packet.g2()) & 0xFFFF] : ColourUtils.HSL_TO_RGB[packet.g2()];
        packed = packet.g1();
        this.phase = (packed & 0xE0) << 3;
        this.preset = packed & 0x1F;
        if (this.preset != 31) {
            this.applyPreset();
        }
        this.createLight(toolkit, z, y, x, colour, radius);
    }

    @OriginalMember(owner = "client!kr", name = "a", descriptor = "(B)V")
    public static void initNoise() {
        noise = generateNoise(0.4F);
    }

    @OriginalMember(owner = "client!iea", name = "a", descriptor = "(IIZIIIFI)[I")
    public static int[] generateNoise(@OriginalArg(6) float persistence) {
        @Pc(6) int[] values = new int[2048];
        @Pc(10) Node_Sub1_Sub27 op = new Node_Sub1_Sub27();
        op.anInt8799 = (int) (persistence * 4096.0F);
        op.aBoolean667 = true;
        op.anInt8809 = 35;
        op.anInt8805 = 8;
        op.anInt8810 = 8;
        op.anInt8803 = 4;
        op.postDecode();
        setTextureOpSize(1, 2048);
        op.method7809(0, values);
        return values;
    }

    @OriginalMember(owner = "client!ec", name = "a", descriptor = "(III)V")
    public static void setTextureOpSize(@OriginalArg(1) int height, @OriginalArg(2) int width) {
        @Pc(7) int i;
        if (textureOpWidth != width) {
            textureOpColumns = new int[width];
            for (i = 0; i < width; i++) {
                textureOpColumns[i] = (i << 12) / width;
            }
            textureOpWidthMask = width - 1;
            textureOpWidth = width;
            textureOpDepthScale = width * 32;
        }
        if (height == textureOpHeight) {
            return;
        }
        if (textureOpWidth == height) {
            MonochromeImageCache.anIntArray341 = textureOpColumns;
        } else {
            MonochromeImageCache.anIntArray341 = new int[height];
            for (i = 0; i < height; i++) {
                MonochromeImageCache.anIntArray341[i] = (i << 12) / height;
            }
        }
        textureOpHeightMask = height - 1;
        textureOpHeight = height;
    }

    @OriginalMember(owner = "client!th", name = "a", descriptor = "(ZIB)V")
    public void updateIntensity(@OriginalArg(0) boolean flickerDisabled, @OriginalArg(1) int clock) {
        @Pc(71) int wave;
        if (flickerDisabled) {
            wave = 2048;
        } else {
            @Pc(27) int phase = clock * this.frequency / 50 + this.phase & 0x7FF;
            @Pc(30) int pattern = this.pattern;
            if (pattern == 1) {
                wave = (Trig1.SIN[phase << 3] >> 4) + 1024;
            } else if (pattern == 3) {
                wave = noise[phase] >> 1;
            } else if (pattern == 4) {
                wave = phase >> 10 << 11;
            } else if (pattern == 2) {
                wave = phase;
            } else if (pattern == 5) {
                wave = (phase < 1024 ? phase : 2048 - phase) << 1;
            } else {
                wave = 2048;
            }
        }
        this.light.method8433((float) ((this.amplitude * wave >> 11) + this.ambient) / 2048.0F);
    }

    @OriginalMember(owner = "client!th", name = "a", descriptor = "(IBIII)V")
    public void updateParameters(@OriginalArg(0) int ambient, @OriginalArg(2) int pattern, @OriginalArg(3) int amplitude, @OriginalArg(4) int frequency) {
        this.frequency = frequency;
        this.ambient = ambient;
        this.pattern = pattern;
        this.amplitude = amplitude;
    }

    @OriginalMember(owner = "client!th", name = "a", descriptor = "(Lclient!ha;IIIIII)V")
    public void createLight(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int z, @OriginalArg(2) int y, @OriginalArg(3) int x, @OriginalArg(4) int colour, @OriginalArg(5) int range) {
        this.light = toolkit.method7941(x, y, z, range, colour, (float) 1);
    }

    @OriginalMember(owner = "client!th", name = "c", descriptor = "(I)V")
    public void applyPreset() {
        @Pc(9) int preset = this.preset;
        if (preset == 2) {
            this.frequency = 2048;
            this.amplitude = 2048;
            this.pattern = 1;
            this.ambient = 0;
        } else if (preset == 3) {
            this.frequency = 4096;
            this.ambient = 0;
            this.amplitude = 2048;
            this.pattern = 1;
        } else if (preset == 4) {
            this.ambient = 0;
            this.pattern = 4;
            this.amplitude = 2048;
            this.frequency = 2048;
        } else if (preset == 5) {
            this.frequency = 8192;
            this.pattern = 4;
            this.ambient = 0;
            this.amplitude = 2048;
        } else if (preset == 12) {
            this.pattern = 2;
            this.amplitude = 2048;
            this.frequency = 2048;
            this.ambient = 0;
        } else if (preset == 13) {
            this.amplitude = 2048;
            this.pattern = 2;
            this.ambient = 0;
            this.frequency = 8192;
        } else if (preset == 10) {
            this.amplitude = 512;
            this.ambient = 1536;
            this.frequency = 2048;
            this.pattern = 3;
        } else if (preset == 11) {
            this.frequency = 4096;
            this.pattern = 3;
            this.ambient = 1536;
            this.amplitude = 512;
        } else if (preset == 6) {
            this.ambient = 1280;
            this.frequency = 2048;
            this.pattern = 3;
            this.amplitude = 768;
        } else if (preset == 7) {
            this.pattern = 3;
            this.ambient = 1280;
            this.amplitude = 768;
            this.frequency = 4096;
        } else if (preset == 8) {
            this.frequency = 2048;
            this.ambient = 1024;
            this.pattern = 3;
            this.amplitude = 1024;
        } else if (preset == 9) {
            this.frequency = 4096;
            this.ambient = 1024;
            this.amplitude = 1024;
            this.pattern = 3;
        } else if (preset == 14) {
            this.pattern = 1;
            this.amplitude = 768;
            this.frequency = 2048;
            this.ambient = 1280;
        } else if (preset == 15) {
            this.pattern = 1;
            this.amplitude = 512;
            this.ambient = 1536;
            this.frequency = 4096;
        } else if (preset == 16) {
            this.pattern = 1;
            this.frequency = 8192;
            this.ambient = 1792;
            this.amplitude = 256;
        } else {
            this.ambient = 0;
            this.amplitude = 2048;
            this.pattern = 0;
            this.frequency = 2048;
        }
    }
}

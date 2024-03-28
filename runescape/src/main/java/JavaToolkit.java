import com.jagex.ParticleList;
import com.jagex.IndexedImage;
import com.jagex.Class67;
import com.jagex.Class84;
import com.jagex.Interface26;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.math.ColourUtils;
import com.jagex.core.datastruct.Node2;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.util.Arrays;
import com.jagex.core.util.SystemTimer;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Exception_Sub1;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Interface9;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Node_Sub13;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Surface;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

@OriginalClass("client!iaa")
public final class JavaToolkit extends Toolkit {

    @OriginalMember(owner = "client!ed", name = "g", descriptor = "Lclient!sia;")
    public static final Deque objSprites = new Deque();

    @OriginalMember(owner = "client!uf", name = "a", descriptor = "(IILclient!d;ILjava/awt/Canvas;)Lclient!ha;")
    public static Toolkit create(@OriginalArg(4) Canvas canvas, @OriginalArg(2) TextureSource textureSource, @OriginalArg(0) int width, @OriginalArg(3) int height) {
        return new JavaToolkit(canvas, textureSource, width, height);
    }

    @OriginalMember(owner = "client!iaa", name = "pb", descriptor = "I")
    public int anInt4183;

    @OriginalMember(owner = "client!iaa", name = "mb", descriptor = "Ljava/awt/Canvas;")
    public Canvas aCanvas3;

    @OriginalMember(owner = "client!iaa", name = "Q", descriptor = "Lclient!cda;")
    public Node_Sub10 aClass2_Sub10_1;

    @OriginalMember(owner = "client!iaa", name = "gb", descriptor = "I")
    public int anInt4185;

    @OriginalMember(owner = "client!iaa", name = "A", descriptor = "I")
    public int anInt4189;

    @OriginalMember(owner = "client!iaa", name = "hb", descriptor = "I")
    public int anInt4190;

    @OriginalMember(owner = "client!iaa", name = "x", descriptor = "I")
    public int anInt4193;

    @OriginalMember(owner = "client!iaa", name = "I", descriptor = "I")
    public int anInt4194;

    @OriginalMember(owner = "client!iaa", name = "K", descriptor = "I")
    public int anInt4195;

    @OriginalMember(owner = "client!iaa", name = "kb", descriptor = "Lclient!du;")
    public Class87 aClass87_1;

    @OriginalMember(owner = "client!iaa", name = "nb", descriptor = "I")
    public int anInt4197;

    @OriginalMember(owner = "client!iaa", name = "z", descriptor = "I")
    public int anInt4198;

    @OriginalMember(owner = "client!iaa", name = "cb", descriptor = "[Lclient!wf;")
    public Class399[] aClass399Array1;

    @OriginalMember(owner = "client!iaa", name = "ib", descriptor = "I")
    public int anInt4201;

    @OriginalMember(owner = "client!iaa", name = "O", descriptor = "[F")
    public float[] aFloatArray24;

    @OriginalMember(owner = "client!iaa", name = "R", descriptor = "I")
    public int anInt4203;

    @OriginalMember(owner = "client!iaa", name = "S", descriptor = "I")
    public int anInt4204;

    @OriginalMember(owner = "client!iaa", name = "w", descriptor = "I")
    public int anInt4206;

    @OriginalMember(owner = "client!iaa", name = "P", descriptor = "[I")
    public int[] anIntArray319;

    @OriginalMember(owner = "client!iaa", name = "jb", descriptor = "I")
    public int anInt4207;

    @OriginalMember(owner = "client!iaa", name = "Y", descriptor = "I")
    public int anInt4208;

    @OriginalMember(owner = "client!iaa", name = "v", descriptor = "I")
    public int anInt4209;

    @OriginalMember(owner = "client!iaa", name = "M", descriptor = "[F")
    public float[] aFloatArray25;

    @OriginalMember(owner = "client!iaa", name = "E", descriptor = "I")
    public int anInt4210;

    @OriginalMember(owner = "client!iaa", name = "qb", descriptor = "I")
    public int anInt4211;

    @OriginalMember(owner = "client!iaa", name = "H", descriptor = "Lclient!st;")
    public Sprite aSprite_17;

    @OriginalMember(owner = "client!iaa", name = "F", descriptor = "Z")
    public boolean aBoolean331;

    @OriginalMember(owner = "client!iaa", name = "u", descriptor = "Z")
    public boolean aBoolean330;

    @OriginalMember(owner = "client!iaa", name = "N", descriptor = "Lclient!av;")
    public IterableHashTable aIterableHashTable_20;

    @OriginalMember(owner = "client!iaa", name = "L", descriptor = "I")
    public int anInt4186;

    @OriginalMember(owner = "client!iaa", name = "C", descriptor = "I")
    public int anInt4188;

    @OriginalMember(owner = "client!iaa", name = "ab", descriptor = "I")
    public int anInt4202;

    @OriginalMember(owner = "client!iaa", name = "Z", descriptor = "I")
    public int anInt4200;

    @OriginalMember(owner = "client!iaa", name = "B", descriptor = "I")
    public int anInt4199;

    @OriginalMember(owner = "client!iaa", name = "lb", descriptor = "I")
    public int lb;

    @OriginalMember(owner = "client!iaa", name = "y", descriptor = "I")
    public int anInt4196;

    @OriginalMember(owner = "client!iaa", name = "D", descriptor = "I")
    public int anInt4191;

    @OriginalMember(owner = "client!iaa", name = "V", descriptor = "I")
    public int anInt4192;

    @OriginalMember(owner = "client!iaa", name = "bb", descriptor = "I")
    public int anInt4187;

    @OriginalMember(owner = "client!iaa", name = "U", descriptor = "Z")
    public boolean aBoolean332;

    @OriginalMember(owner = "client!iaa", name = "fb", descriptor = "I")
    public int anInt4212;

    @OriginalMember(owner = "client!iaa", name = "T", descriptor = "I")
    public int anInt4213;

    @OriginalMember(owner = "client!iaa", name = "X", descriptor = "I")
    public int anInt4214;

    @OriginalMember(owner = "client!iaa", name = "ob", descriptor = "I")
    public int anInt4205;

    @OriginalMember(owner = "client!iaa", name = "J", descriptor = "Lclient!dla;")
    public final ReferenceCache aReferenceCache_88;

    @OriginalMember(owner = "client!iaa", name = "G", descriptor = "I")
    public int anInt4215;

    @OriginalMember(owner = "client!iaa", name = "W", descriptor = "Lclient!dla;")
    public final ReferenceCache aReferenceCache_89;

    @OriginalMember(owner = "client!iaa", name = "db", descriptor = "Lclient!eaa;")
    public Matrix_Sub2 aClass73_Sub2_1;

    @OriginalMember(owner = "client!iaa", name = "eb", descriptor = "I")
    public int lastTickTime;

    @OriginalMember(owner = "client!iaa", name = "<init>", descriptor = "(Lclient!d;)V")
    public JavaToolkit(@OriginalArg(0) TextureSource arg0) {
        super(arg0);
        this.aBoolean331 = false;
        this.aBoolean330 = false;
        this.aIterableHashTable_20 = new IterableHashTable(4);
        this.anInt4186 = 0;
        this.anInt4188 = 512;
        this.anInt4202 = 75518;
        this.anInt4200 = 0;
        this.anInt4199 = 3500;
        this.lb = 128;
        this.anInt4196 = 0;
        this.anInt4191 = 45823;
        this.anInt4192 = 0;
        this.anInt4187 = 78642;
        this.aBoolean332 = false;
        this.anInt4212 = 0;
        this.anInt4213 = 0;
        this.anInt4214 = 50;
        this.anInt4205 = 512;
        this.aReferenceCache_88 = new ReferenceCache(16);
        this.anInt4215 = -1;
        try {
            this.aReferenceCache_89 = new ReferenceCache(256);
            this.aClass73_Sub2_1 = new Matrix_Sub2();
            this.method7956(1);
            this.method8020(0);
            ColourUtils.init(true, true);
            this.aBoolean330 = true;
            this.lastTickTime = (int) SystemTimer.safetime();
        } catch (@Pc(99) Throwable local99) {
            local99.printStackTrace();
            this.free();
            throw new RuntimeException("");
        }
    }

    @OriginalMember(owner = "client!iaa", name = "<init>", descriptor = "(Ljava/awt/Canvas;Lclient!d;II)V")
    public JavaToolkit(@OriginalArg(0) Canvas arg0, @OriginalArg(1) TextureSource arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        this(arg1);
        try {
            this.method8022(arg0, arg2, arg3);
            this.method8019(arg0);
        } catch (@Pc(12) Throwable local12) {
            local12.printStackTrace();
            this.free();
            throw new RuntimeException("");
        }
    }

    @OriginalMember(owner = "client!iaa", name = "u", descriptor = "()V")
    @Override
    protected void method7987() {
        if (this.aBoolean330) {
            ColourUtils.destroy(true, false);
            this.aBoolean330 = false;
        }
        this.aClass2_Sub10_1 = null;
        this.aCanvas3 = null;
        this.anInt4183 = 0;
        this.anInt4185 = 0;
        this.aIterableHashTable_20 = null;
        this.aBoolean331 = true;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIILclient!aa;II)V")
    @Override
    public void method7965(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(6) ClippingMask mask, @OriginalArg(7) int offsetX, @OriginalArg(8) int offsetY) {
        @Pc(2) ClippingMask_Sub1 local2 = (ClippingMask_Sub1) mask;
        @Pc(5) int[] local5 = local2.anIntArray334;
        @Pc(8) int[] local8 = local2.anIntArray335;
        @Pc(18) int local18 = this.anInt4186 > offsetY ? this.anInt4186 : offsetY;
        @Pc(34) int local34 = this.anInt4196 < offsetY + local5.length ? this.anInt4196 : offsetY + local5.length;
        x2 -= x1;
        y2 -= y1;
        if (x2 + y2 < 0) {
            x1 += x2;
            x2 = -x2;
            y1 += y2;
            y2 = -y2;
        }
        @Pc(85) int local85;
        @Pc(118) int local118;
        @Pc(136) int local136;
        @Pc(140) int local140;
        @Pc(154) int local154;
        @Pc(242) int local242;
        @Pc(261) int local261;
        @Pc(266) int local266;
        @Pc(215) int local215;
        if (x2 <= y2) {
            x1 <<= 0x10;
            x1 += 32768;
            @Pc(415) int local415 = x2 << 16;
            local85 = (int) Math.floor((double) local415 / (double) y2 + 0.5D);
            y2 += y1;
            if (y1 < local18) {
                x1 += local85 * (local18 - y1);
                y1 = local18;
            }
            if (y2 >= local34) {
                y2 = local34 - 1;
            }
            local118 = colour >>> 24;
            if (local118 == 255 && true) {
                while (y1 <= y2) {
                    local136 = x1 >> 16;
                    local140 = y1 - offsetY;
                    local154 = offsetX + local5[local140];
                    if (local136 >= this.anInt4192 && local136 < this.anInt4200 && local136 >= local154 && local136 < local154 + local8[local140]) {
                        this.anIntArray319[local136 + y1 * this.anInt4207] = colour;
                    }
                    x1 += local85;
                    y1++;
                }
            } else {
                local215 = ((colour & 0xFF00FF) * local118 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local118 >> 8 & 0xFF00) + (local118 << 24);
                local136 = 256 - local118;
                while (y1 <= y2) {
                    local140 = x1 >> 16;
                    local154 = y1 - offsetY;
                    local242 = offsetX + local5[local154];
                    if (local140 >= this.anInt4192 && local140 < this.anInt4200 && local140 >= local242 && local140 < local242 + local8[local154]) {
                        local261 = local140 + y1 * this.anInt4207;
                        local266 = this.anIntArray319[local261];
                        @Pc(629) int local629 = ((local266 & 0xFF00FF) * local136 >> 8 & 0xFF00FF) + ((local266 & 0xFF00) * local136 >> 8 & 0xFF00);
                        this.anIntArray319[local140 + y1 * this.anInt4207] = local215 + local629;
                    }
                    x1 += local85;
                    y1++;
                }
            }
            return;
        }
        y1 <<= 0x10;
        y1 += 32768;
        @Pc(75) int local75 = y2 << 16;
        local85 = (int) Math.floor((double) local75 / (double) x2 + 0.5D);
        x2 += x1;
        if (x1 < this.anInt4192) {
            y1 += local85 * (this.anInt4192 - x1);
            x1 = this.anInt4192;
        }
        if (x2 >= this.anInt4200) {
            x2 = this.anInt4200 - 1;
        }
        local118 = colour >>> 24;
        if (local118 == 255 && true) {
            while (x1 <= x2) {
                local136 = y1 >> 16;
                local140 = local136 - offsetY;
                if (local136 >= local18 && local136 < local34) {
                    local154 = offsetX + local5[local140];
                    if (x1 >= local154 && x1 < local154 + local8[local140]) {
                        this.anIntArray319[x1 + local136 * this.anInt4207] = colour;
                    }
                }
                y1 += local85;
                x1++;
            }
            return;
        }
        local215 = ((colour & 0xFF00FF) * local118 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local118 >> 8 & 0xFF00) + (local118 << 24);
        local136 = 256 - local118;
        while (x1 <= x2) {
            local140 = y1 >> 16;
            local154 = local140 - offsetY;
            if (local140 >= local18 && local140 < local34) {
                local242 = offsetX + local5[local154];
                if (x1 >= local242 && x1 < local242 + local8[local154]) {
                    local261 = x1 + local140 * this.anInt4207;
                    local266 = this.anIntArray319[local261];
                    local266 = ((local266 & 0xFF00FF) * local136 >> 8 & 0xFF00FF) + ((local266 & 0xFF00) * local136 >> 8 & 0xFF00);
                    this.anIntArray319[local261] = local215 + local266;
                }
            }
            y1 += local85;
            x1++;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "xa", descriptor = "(F)V")
    @Override
    public void xa(@OriginalArg(0) float globalAmbient) {
        this.anInt4202 = (int) (globalAmbient * 65535.0F);
    }

    @OriginalMember(owner = "client!iaa", name = "M", descriptor = "()I")
    @Override
    public int M() {
        @Pc(2) int local2 = this.anInt4213;
        this.anInt4213 = 0;
        return local2;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIII)V")
    @Override
    public void method7947(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        @Pc(6) Class219 local6 = local3.aClass219_2;
        @Pc(10) int local10 = arg2 - arg0;
        @Pc(14) int local14 = arg3 - arg1;
        @Pc(22) int local22 = local10 >= 0 ? local10 : -local10;
        @Pc(30) int local30 = local14 >= 0 ? local14 : -local14;
        @Pc(32) int local32 = local22;
        if (local22 < local30) {
            local32 = local30;
        }
        if (local32 == 0) {
            return;
        }
        @Pc(47) int local47 = (local10 << 16) / local32;
        @Pc(53) int local53 = (local14 << 16) / local32;
        local10 += local47 >> 16;
        local14 += local53 >> 16;
        if (local53 <= local47) {
            local47 = -local47;
        } else {
            local53 = -local53;
        }
        @Pc(81) int local81 = arg5 * local53 >> 17;
        @Pc(89) int local89 = arg5 * local53 + 1 >> 17;
        @Pc(95) int local95 = arg5 * local47 >> 17;
        @Pc(103) int local103 = arg5 * local47 + 1 >> 17;
        @Pc(108) int local108 = arg0 - local6.method5160();
        @Pc(113) int local113 = arg1 - local6.method5140();
        @Pc(117) int local117 = local108 + local81;
        @Pc(121) int local121 = local108 - local89;
        @Pc(127) int local127 = local108 + local10 - local89;
        @Pc(133) int local133 = local108 + local10 + local81;
        @Pc(137) int local137 = local113 + local95;
        @Pc(141) int local141 = local113 - local103;
        @Pc(147) int local147 = local113 + local14 - local103;
        @Pc(153) int local153 = local113 + local14 + local95;
        local6.anInt5724 = 0;
        this.C(false);
        local6.aBoolean434 = local117 < 0 || local117 > local6.anInt5725 || local121 < 0 || local121 > local6.anInt5725 || local127 < 0 || local127 > local6.anInt5725;
        local6.method5158((float) local137, (float) local141, (float) local147, (float) local117, (float) local121, (float) local127, 100.0F, 100.0F, 100.0F, arg4);
        local6.aBoolean434 = local117 < 0 || local117 > local6.anInt5725 || local127 < 0 || local127 > local6.anInt5725 || local133 < 0 || local133 > local6.anInt5725;
        local6.method5158((float) local137, (float) local147, (float) local153, (float) local117, (float) local127, (float) local133, 100.0F, 100.0F, 100.0F, arg4);
        this.C(true);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public void method8019(@OriginalArg(0) Canvas arg0) {
        if (arg0 == null) {
            this.aCanvas3 = null;
            this.aClass2_Sub10_1 = null;
            if (this.aClass87_1 == null) {
                this.anIntArray319 = null;
                this.anInt4207 = this.anInt4209 = 1;
                this.anInt4190 = this.anInt4203 = 1;
                this.method3789();
            }
            return;
        }
        @Pc(10) Node_Sub10 local10 = (Node_Sub10) this.aIterableHashTable_20.get(arg0.hashCode());
        if (local10 == null) {
            return;
        }
        this.aCanvas3 = arg0;
        @Pc(18) Dimension local18 = arg0.getSize();
        this.anInt4183 = local18.width;
        this.anInt4185 = local18.height;
        this.aClass2_Sub10_1 = local10;
        if (this.aClass87_1 != null) {
            return;
        }
        this.anIntArray319 = local10.anIntArray567;
        this.anInt4207 = local10.anInt7053;
        this.anInt4209 = local10.anInt7050;
        if (this.anInt4207 != this.anInt4190 || this.anInt4209 != this.anInt4203) {
            this.anInt4197 = this.anInt4190 = this.anInt4207;
            this.anInt4195 = this.anInt4203 = this.anInt4209;
            this.aFloatArray25 = this.aFloatArray24 = new float[this.anInt4190 * this.anInt4203];
        }
        this.method3789();
        return;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIIII)V")
    public void method3783(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6) {
        if (arg0 < this.anInt4192 || arg0 >= this.anInt4200) {
            return;
        }
        @Pc(18) int local18 = arg0 + arg1 * this.anInt4207;
        @Pc(22) int local22 = arg3 >>> 24;
        @Pc(26) int local26 = arg4 + arg5;
        @Pc(30) int local30 = arg6 % local26;
        @Pc(44) int local44;
        if (local22 == 255 && true) {
            local44 = 0;
            while (local44 < arg2) {
                if (arg1 + local44 >= this.anInt4186 && arg1 + local44 < this.anInt4196 && local30 < arg4) {
                    this.anIntArray319[local18 + local44 * this.anInt4207] = arg3;
                }
                local44++;
                local30++;
                local30 %= local26;
            }
            return;
        }
        @Pc(114) int local114 = ((arg3 & 0xFF00FF) * local22 >> 8 & 0xFF00FF) + ((arg3 & 0xFF00) * local22 >> 8 & 0xFF00) + (local22 << 24);
        local44 = 256 - local22;
        @Pc(120) int local120 = 0;
        while (local120 < arg2) {
            if (arg1 + local120 >= this.anInt4186 && arg1 + local120 < this.anInt4196 && local30 < arg4) {
                @Pc(147) int local147 = local18 + local120 * this.anInt4207;
                @Pc(152) int local152 = this.anIntArray319[local147];
                @Pc(172) int local172 = ((local152 & 0xFF00FF) * local44 >> 8 & 0xFF00FF) + ((local152 & 0xFF00) * local44 >> 8 & 0xFF00);
                this.anIntArray319[local147] = local114 + local172;
            }
            local120++;
            local30++;
            local30 %= local26;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "ya", descriptor = "()V")
    @Override
    public void ya() {
        @Pc(25) int local25;
        @Pc(31) int local31;
        @Pc(33) int local33;
        if (this.anInt4192 == 0 && this.anInt4200 == this.anInt4207 && this.anInt4186 == 0 && this.anInt4196 == this.anInt4209) {
            local25 = this.aFloatArray24.length;
            local31 = local25 - (local25 & 0x7);
            local33 = 0;
            while (local33 < local31) {
                this.aFloatArray24[local33++] = 2.14748365E9F;
                this.aFloatArray24[local33++] = 2.14748365E9F;
                this.aFloatArray24[local33++] = 2.14748365E9F;
                this.aFloatArray24[local33++] = 2.14748365E9F;
                this.aFloatArray24[local33++] = 2.14748365E9F;
                this.aFloatArray24[local33++] = 2.14748365E9F;
                this.aFloatArray24[local33++] = 2.14748365E9F;
                this.aFloatArray24[local33++] = 2.14748365E9F;
            }
            while (local33 < local25) {
                this.aFloatArray24[local33++] = 2.14748365E9F;
            }
            return;
        }
        local25 = this.anInt4200 - this.anInt4192;
        local31 = this.anInt4196 - this.anInt4186;
        local33 = this.anInt4207 - local25;
        @Pc(124) int local124 = this.anInt4192 + this.anInt4186 * this.anInt4207;
        @Pc(128) int local128 = local25 >> 3;
        @Pc(132) int local132 = local25 & 0x7;
        local25 = local124 - 1;
        for (@Pc(139) int local139 = -local31; local139 < 0; local139++) {
            @Pc(144) int local144;
            if (local128 > 0) {
                local144 = local128;
                do {
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local144--;
                } while (local144 > 0);
            }
            if (local132 > 0) {
                local144 = local132;
                do {
                    local25++;
                    this.aFloatArray24[local25] = 2.14748365E9F;
                    local144--;
                } while (local144 > 0);
            }
            local25 += local33;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!gaa;)V")
    @Override
    public void method7939(@OriginalArg(0) Interface9 arg0) {
        @Pc(2) Class87 local2 = (Class87) arg0;
        this.anInt4207 = local2.anInt2357;
        this.anInt4209 = local2.anInt2354;
        this.anIntArray319 = local2.anIntArray203;
        this.aClass87_1 = local2;
        this.anInt4190 = local2.anInt2357;
        this.anInt4203 = local2.anInt2354;
        this.aFloatArray24 = local2.aFloatArray16;
        this.method3789();
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "()Z")
    @Override
    public boolean method7983() {
        return true;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!lk;I)V")
    @Override
    public void renderOrtho(@OriginalArg(0) ParticleList arg0, @OriginalArg(1) int arg1) {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        @Pc(7) Node2 local7 = arg0.particles.sentinel;
        for (@Pc(10) Node2 local10 = local7.next2; local10 != local7; local10 = local10.next2) {
            @Pc(14) Particle local14 = (Particle) local10;
            @Pc(19) int local19 = local14.anInt7537 >> 12;
            @Pc(24) int local24 = local14.anInt7534 >> 12;
            @Pc(29) int local29 = local14.anInt7536 >> 12;
            @Pc(54) float local54 = this.aClass73_Sub2_1.aFloat62 + this.aClass73_Sub2_1.aFloat56 * (float) local19 + this.aClass73_Sub2_1.aFloat54 * (float) local24 + this.aClass73_Sub2_1.aFloat61 * (float) local29;
            if (!(local54 < (float) this.anInt4214) && !(local54 > (float) local3.anInt10601)) {
                @Pc(106) int local106 = this.anInt4206 + (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat60 + this.aClass73_Sub2_1.aFloat59 * (float) local19 + this.aClass73_Sub2_1.aFloat55 * (float) local24 + this.aClass73_Sub2_1.aFloat53 * (float) local29) / (float) arg1);
                @Pc(142) int local142 = this.anInt4194 + (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat58 + this.aClass73_Sub2_1.aFloat57 * (float) local19 + this.aClass73_Sub2_1.aFloat52 * (float) local24 + this.aClass73_Sub2_1.aFloat51 * (float) local29) / (float) arg1);
                if (local106 >= this.anInt4192 && local106 <= this.anInt4200 && local142 >= this.anInt4186 && local142 <= this.anInt4196) {
                    if (local54 == 0.0F) {
                        local54 = 1.0F;
                    }
                    this.method3784(local14, local106, local142, (int) local54, (local14.anInt7535 * this.anInt4205 >> 12) / arg1);
                }
            }
        }
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!up;IIII)V")
    public void method3784(@OriginalArg(0) Particle arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4) {
        @Pc(2) int local2 = arg0.anInt7540;
        @Pc(8) int local8 = arg4 << 1;
        if (local2 == -1) {
            this.method3790(arg1, arg2, arg3, arg4, arg0.anInt7539, 1);
            return;
        }
        if (this.anInt4215 != local2) {
            @Pc(34) Sprite local34 = (Sprite) this.aReferenceCache_88.get(local2);
            if (local34 == null) {
                @Pc(40) int[] local40 = this.method3788(local2);
                if (local40 == null) {
                    return;
                }
                @Pc(54) int local54 = this.method3798(local2) ? 64 : this.lb;
                local34 = this.createSprite(local54, local54, local54, local40);
                this.aReferenceCache_88.put(local34, local2);
            }
            this.anInt4215 = local2;
            this.aSprite_17 = local34;
        }
        local8++;
        ((Sprite_Sub1) this.aSprite_17).method8208(arg1 - arg4, arg2 - arg4, arg3, local8, local8, 0, arg0.anInt7539, 1);
    }

    @OriginalMember(owner = "client!iaa", name = "F", descriptor = "(II)V")
    @Override
    public void F(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(6) int local6 = arg1 * this.anInt4207 + arg0;
        @Pc(13) int local13 = arg1 * this.anInt4190 + arg0;
        if (local6 == 0 && local13 == 0) {
            return;
        }
        @Pc(24) int[] local24 = this.anIntArray319;
        @Pc(27) float[] local27 = this.aFloatArray24;
        @Pc(34) int local34;
        if (local6 < 0) {
            local34 = local24.length + local6;
            Arrays.copy(local24, -local6, local24, 0, local34);
        } else if (local6 > 0) {
            local34 = local24.length - local6;
            Arrays.copy(local24, 0, local24, local6, local34);
        }
        if (local13 < 0) {
            local34 = local27.length + local13;
            Arrays.copy(local27, -local13, local27, 0, local34);
        } else if (local13 > 0) {
            local34 = local27.length - local13;
            Arrays.copy(local27, 0, local27, local13, local34);
        }
    }

    @OriginalMember(owner = "client!iaa", name = "g", descriptor = "()Z")
    public boolean method3785() {
        return this.aBoolean331;
    }

    @OriginalMember(owner = "client!iaa", name = "za", descriptor = "(IIIII)V")
    @Override
    protected void za(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int radius, @OriginalArg(3) int colour, @OriginalArg(4) int mode) {
        if (radius < 0) {
            radius = -radius;
        }
        @Pc(8) int local8 = y - radius;
        if (local8 < this.anInt4186) {
            local8 = this.anInt4186;
        }
        @Pc(21) int local21 = y + radius + 1;
        if (local21 > this.anInt4196) {
            local21 = this.anInt4196;
        }
        @Pc(30) int local30 = local8;
        @Pc(34) int local34 = radius * radius;
        @Pc(36) int local36 = 0;
        @Pc(40) int local40 = y - local8;
        @Pc(44) int local44 = local40 * local40;
        @Pc(48) int local48 = local44 - local40;
        if (y > local21) {
            y = local21;
        }
        @Pc(57) int local57 = colour >>> 24;
        @Pc(98) int local98;
        @Pc(109) int local109;
        @Pc(123) int local123;
        @Pc(125) int local125;
        if (mode == 1 && local57 == 255) {
            while (local30 < y) {
                while (local48 <= local34 || local44 <= local34) {
                    local44 += local36 + local36;
                    local48 += local36++ + local36;
                }
                local98 = x + 1 - local36;
                if (local98 < this.anInt4192) {
                    local98 = this.anInt4192;
                }
                local109 = x + local36;
                if (local109 > this.anInt4200) {
                    local109 = this.anInt4200;
                }
                local123 = local98 + local30 * this.anInt4207;
                for (local125 = local98; local125 < local109; local125++) {
                    this.anIntArray319[local123++] = colour;
                }
                local30++;
                local44 -= local40-- + local40;
                local48 -= local40 + local40;
            }
            local36 = radius;
            local40 = local30 - y;
            local48 = local40 * local40 + local34;
            local44 = local48 - radius;
            local48 -= local40;
            while (local30 < local21) {
                while (local48 > local34 && local44 > local34) {
                    local48 -= local36-- + local36;
                    local44 -= local36 + local36;
                }
                local98 = x - local36;
                if (local98 < this.anInt4192) {
                    local98 = this.anInt4192;
                }
                local109 = x + local36;
                if (local109 > this.anInt4200 - 1) {
                    local109 = this.anInt4200 - 1;
                }
                local123 = local98 + local30 * this.anInt4207;
                for (local125 = local98; local125 <= local109; local125++) {
                    this.anIntArray319[local123++] = colour;
                }
                local30++;
                local48 += local40 + local40;
                local44 += local40++ + local40;
            }
            return;
        }
        @Pc(287) int local287 = ((colour & 0xFF00FF) * local57 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local57 >> 8 & 0xFF00) + (local57 << 24);
        local98 = 256 - local57;
        @Pc(346) int local346;
        @Pc(352) int local352;
        while (local30 < y) {
            while (local48 <= local34 || local44 <= local34) {
                local44 += local36 + local36;
                local48 += local36++ + local36;
            }
            local109 = x + 1 - local36;
            if (local109 < this.anInt4192) {
                local109 = this.anInt4192;
            }
            local123 = x + local36;
            if (local123 > this.anInt4200) {
                local123 = this.anInt4200;
            }
            local125 = local109 + local30 * this.anInt4207;
            for (local346 = local109; local346 < local123; local346++) {
                local352 = this.anIntArray319[local125];
                @Pc(372) int local372 = ((local352 & 0xFF00FF) * local98 >> 8 & 0xFF00FF) + ((local352 & 0xFF00) * local98 >> 8 & 0xFF00);
                this.anIntArray319[local125++] = local287 + local372;
            }
            local30++;
            local44 -= local40-- + local40;
            local48 -= local40 + local40;
        }
        local36 = radius;
        local40 = -local40;
        local48 = local40 * local40 + local34;
        local44 = local48 - radius;
        local48 -= local40;
        while (local30 < local21) {
            while (local48 > local34 && local44 > local34) {
                local48 -= local36-- + local36;
                local44 -= local36 + local36;
            }
            local109 = x - local36;
            if (local109 < this.anInt4192) {
                local109 = this.anInt4192;
            }
            local123 = x + local36;
            if (local123 > this.anInt4200 - 1) {
                local123 = this.anInt4200 - 1;
            }
            local125 = local109 + local30 * this.anInt4207;
            for (local346 = local109; local346 <= local123; local346++) {
                local352 = this.anIntArray319[local125];
                local352 = ((local352 & 0xFF00FF) * local98 >> 8 & 0xFF00FF) + ((local352 & 0xFF00) * local98 >> 8 & 0xFF00);
                this.anIntArray319[local125++] = local287 + local352;
            }
            local30++;
            local48 += local40 + local40;
            local44 += local40++ + local40;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "o", descriptor = "()Z")
    @Override
    public boolean method7936() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "l", descriptor = "()Z")
    @Override
    public boolean method7978() {
        return true;
    }

    @OriginalMember(owner = "client!iaa", name = "DA", descriptor = "(IIII)V")
    @Override
    public void DA(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height) {
        this.anInt4206 = x;
        this.anInt4194 = y;
        this.anInt4205 = width;
        this.anInt4188 = height;
        this.method3799();
    }

    @OriginalMember(owner = "client!iaa", name = "aa", descriptor = "(IIIIII)V")
    @Override
    public void aa(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        if (x < this.anInt4192) {
            width -= this.anInt4192 - x;
            x = this.anInt4192;
        }
        if (y < this.anInt4186) {
            height -= this.anInt4186 - y;
            y = this.anInt4186;
        }
        if (x + width > this.anInt4200) {
            width = this.anInt4200 - x;
        }
        if (y + height > this.anInt4196) {
            height = this.anInt4196 - y;
        }
        if (width <= 0 || height <= 0 || x > this.anInt4200 || y > this.anInt4196) {
            return;
        }
        @Pc(74) int local74 = this.anInt4207 - width;
        @Pc(81) int local81 = x + y * this.anInt4207;
        @Pc(85) int local85 = colour >>> 24;
        @Pc(101) int local101;
        @Pc(105) int local105;
        @Pc(112) int local112;
        if (mode != 0 && (mode != 1 || local85 != 255)) {
            @Pc(231) int local231;
            if (mode == 1) {
                @Pc(215) int local215 = ((colour & 0xFF00FF) * local85 >> 8 & 0xFF00FF) + (((colour & 0xFF00FF00) >>> 8) * local85 & 0xFF00FF00);
                local101 = 256 - local85;
                for (local105 = 0; local105 < height; local105++) {
                    for (local112 = -width; local112 < 0; local112++) {
                        local231 = this.anIntArray319[local81];
                        local231 = ((local231 & 0xFF00FF) * local101 >> 8 & 0xFF00FF) + (((local231 & 0xFF00FF00) >>> 8) * local101 & 0xFF00FF00);
                        this.anIntArray319[local81++] = local215 + local231;
                    }
                    local81 += local74;
                }
            } else if (mode == 2) {
                for (local101 = 0; local101 < height; local101++) {
                    for (local105 = -width; local105 < 0; local105++) {
                        local112 = this.anIntArray319[local81];
                        local231 = colour + local112;
                        @Pc(299) int local299 = (colour & 0xFF00FF) + (local112 & 0xFF00FF);
                        @Pc(309) int local309 = (local299 & 0x1000100) + (local231 - local299 & 0x10000);
                        this.anIntArray319[local81++] = local231 - local309 | local309 - (local309 >>> 8);
                    }
                    local81 += local74;
                }
            } else {
                throw new IllegalArgumentException();
            }
            return;
        }
        local101 = width >> 3;
        local105 = width & 0x7;
        width = local81 - 1;
        for (local112 = -height; local112 < 0; local112++) {
            if (local101 > 0) {
                x = local101;
                do {
                    width++;
                    this.anIntArray319[width] = colour;
                    width++;
                    this.anIntArray319[width] = colour;
                    width++;
                    this.anIntArray319[width] = colour;
                    width++;
                    this.anIntArray319[width] = colour;
                    width++;
                    this.anIntArray319[width] = colour;
                    width++;
                    this.anIntArray319[width] = colour;
                    width++;
                    this.anIntArray319[width] = colour;
                    width++;
                    this.anIntArray319[width] = colour;
                    x--;
                } while (x > 0);
            }
            if (local105 > 0) {
                x = local105;
                do {
                    width++;
                    this.anIntArray319[width] = colour;
                    x--;
                } while (x > 0);
            }
            width += local74;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "GA", descriptor = "(I)V")
    @Override
    public void GA(@OriginalArg(0) int colour) {
        this.aa(0, 0, this.anInt4207, this.anInt4209, colour, 0);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public void method7935(@OriginalArg(0) Canvas arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(8) Node_Sub10 local8 = (Node_Sub10) this.aIterableHashTable_20.get(arg0.hashCode());
        if (local8 == null) {
            return;
        }
        local8.unlink();
        local8 = Static538.method7192(arg2, arg0, arg1);
        this.aIterableHashTable_20.put(arg0.hashCode(), local8);
        if (this.aCanvas3 != arg0 || this.aClass87_1 != null) {
            return;
        }
        @Pc(39) Dimension local39 = arg0.getSize();
        this.anInt4183 = local39.width;
        this.anInt4185 = local39.height;
        this.aClass2_Sub10_1 = local8;
        this.anIntArray319 = local8.anIntArray567;
        this.anInt4207 = local8.anInt7053;
        this.anInt4209 = local8.anInt7050;
        if (this.anInt4207 != this.anInt4190 || this.anInt4209 != this.anInt4203) {
            this.anInt4197 = this.anInt4190 = this.anInt4207;
            this.anInt4195 = this.anInt4203 = this.anInt4209;
            this.aFloatArray25 = this.aFloatArray24 = new float[this.anInt4190 * this.anInt4203];
        }
        this.method3789();
    }

    @OriginalMember(owner = "client!iaa", name = "d", descriptor = "(II)Lclient!wja;")
    @Override
    public Interface26 method7986(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return new Class165(arg0, arg1);
    }

    @OriginalMember(owner = "client!iaa", name = "n", descriptor = "()Lclient!tt;")
    @Override
    public Matrix method8017() {
        return this.aClass73_Sub2_1;
    }

    @OriginalMember(owner = "client!iaa", name = "h", descriptor = "()V")
    @Override
    public void method7969() {
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIIIIIIIII)V")
    @Override
    public void method7994() {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        @Pc(6) Class219 local6 = local3.aClass219_2;
        local6.aBoolean436 = false;
        @Pc(14) int local14 = 5 - this.anInt4210;
        @Pc(19) int local19 = 75 - this.anInt4210;
        @Pc(24) int local24 = 15 - this.anInt4210;
        @Pc(29) int local29 = 10 - this.anInt4208;
        @Pc(34) int local34 = 50 - this.anInt4208;
        @Pc(39) int local39 = 90 - this.anInt4208;
        local6.aBoolean434 = local14 < 0 || local14 > local6.anInt5725 || local19 < 0 || local19 > local6.anInt5725 || local24 < 0 || local24 > local6.anInt5725;
        if (255 == 255 && true) {
            local6.anInt5724 = 0;
            local6.aBoolean433 = false;
            local6.method5141((float) local29, (float) local34, (float) local39, (float) local14, (float) local19, (float) local24, (float) 100, (float) 100, (float) 100, -65536, -65536, -65536);
        } else {
            local6.anInt5724 = 0;
            local6.aBoolean433 = false;
            local6.method5141((float) local29, (float) local34, (float) local39, (float) local14, (float) local19, (float) local24, (float) 100, (float) 100, (float) 100, -65536, -65536, -65536);
        }
        local6.aBoolean436 = true;
    }

    @OriginalMember(owner = "client!iaa", name = "XA", descriptor = "()I")
    @Override
    public int XA() {
        return this.anInt4199;
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(II)I")
    @Override
    public int compareFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        @Pc(3) int local3 = maskA | 0x20800;
        return local3 & maskB ^ maskB;
    }

    @OriginalMember(owner = "client!iaa", name = "Y", descriptor = "()[I")
    @Override
    public int[] Y() {
        return new int[]{this.anInt4206, this.anInt4194, this.anInt4205, this.anInt4188};
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!dv;IIII)Lclient!ka;")
    @Override
    public Model createModel(@OriginalArg(0) Mesh mesh, @OriginalArg(1) int functionMask, @OriginalArg(2) int featureMask, @OriginalArg(3) int ambient, @OriginalArg(4) int contrast) {
        return new Model_Sub3(this, mesh, functionMask, ambient, contrast, featureMask);
    }

    @OriginalMember(owner = "client!iaa", name = "B", descriptor = "()Z")
    @Override
    public boolean method8001() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "n", descriptor = "(I)Z")
    public boolean method3786(@OriginalArg(0) int arg0) {
        return super.textureSource.textureAvailable(arg0);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Ljava/lang/Runnable;)Lclient!wf;")
    public Class399 method3787(@OriginalArg(0) Runnable arg0) {
        for (@Pc(1) int local1 = 0; local1 < this.anInt4211; local1++) {
            if (this.aClass399Array1[local1].aRunnable2 == arg0) {
                return this.aClass399Array1[local1];
            }
        }
        return null;
    }

    @OriginalMember(owner = "client!iaa", name = "f", descriptor = "(II)V")
    @Override
    public void f(@OriginalArg(0) int near, @OriginalArg(1) int far) {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        this.anInt4214 = near;
        this.anInt4199 = far;
        local3.anInt10601 = this.anInt4199 - 255;
    }

    @OriginalMember(owner = "client!iaa", name = "P", descriptor = "(IIIII)V")
    @Override
    public void P(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int colour, @OriginalArg(4) int arg4) {
        if (x < this.anInt4192 || x >= this.anInt4200) {
            return;
        }
        if (y < this.anInt4186) {
            width -= this.anInt4186 - y;
            y = this.anInt4186;
        }
        if (y + width > this.anInt4196) {
            width = this.anInt4196 - y;
        }
        @Pc(43) int local43 = x + y * this.anInt4207;
        @Pc(47) int local47 = colour >>> 24;
        @Pc(61) int local61;
        if (arg4 == 0 || arg4 == 1 && local47 == 255) {
            for (local61 = 0; local61 < width; local61++) {
                this.anIntArray319[local43 + local61 * this.anInt4207] = colour;
            }
            return;
        }
        @Pc(111) int local111;
        @Pc(119) int local119;
        @Pc(124) int local124;
        if (arg4 == 1) {
            @Pc(105) int local105 = ((colour & 0xFF00FF) * local47 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local47 >> 8 & 0xFF00) + (local47 << 24);
            local61 = 256 - local47;
            for (local111 = 0; local111 < width; local111++) {
                local119 = local43 + local111 * this.anInt4207;
                local124 = this.anIntArray319[local119];
                local124 = ((local124 & 0xFF00FF) * local61 >> 8 & 0xFF00FF) + ((local124 & 0xFF00) * local61 >> 8 & 0xFF00);
                this.anIntArray319[local119] = local105 + local124;
            }
        } else if (arg4 == 2) {
            for (local61 = 0; local61 < width; local61++) {
                local111 = local43 + local61 * this.anInt4207;
                local119 = this.anIntArray319[local111];
                local124 = colour + local119;
                @Pc(187) int local187 = (colour & 0xFF00FF) + (local119 & 0xFF00FF);
                @Pc(197) int local197 = (local187 & 0x1000100) + (local124 - local187 & 0x10000);
                this.anIntArray319[local111] = local124 - local197 | local197 - (local197 >>> 8);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Z)V")
    @Override
    public void method7997(@OriginalArg(0) boolean arg0) {
        this.aBoolean332 = arg0;
        this.aReferenceCache_89.reset();
    }

    @OriginalMember(owner = "client!iaa", name = "A", descriptor = "()Lclient!tt;")
    @Override
    public Matrix scratchMatrix() {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        return local3.aClass73_Sub2_3;
    }

    @OriginalMember(owner = "client!iaa", name = "d", descriptor = "()V")
    @Override
    public void method7974() {
    }

    @OriginalMember(owner = "client!iaa", name = "d", descriptor = "(IIIIII)V")
    @Override
    public void outlineRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        this.U(x, y, width, colour, mode);
        this.U(x, y + height - 1, width, colour, mode);
        this.P(x, y + 1, height - 2, colour, mode);
        this.P(x + width - 1, y + 1, height - 2, colour, mode);
    }

    @OriginalMember(owner = "client!iaa", name = "U", descriptor = "(IIIII)V")
    @Override
    public void U(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int height, @OriginalArg(3) int colour, @OriginalArg(4) int arg4) {
        if (y < this.anInt4186 || y >= this.anInt4196) {
            return;
        }
        if (x < this.anInt4192) {
            height -= this.anInt4192 - x;
            x = this.anInt4192;
        }
        if (x + height > this.anInt4200) {
            height = this.anInt4200 - x;
        }
        @Pc(43) int local43 = x + y * this.anInt4207;
        @Pc(47) int local47 = colour >>> 24;
        @Pc(61) int local61;
        if (arg4 == 0 || arg4 == 1 && local47 == 255) {
            for (local61 = 0; local61 < height; local61++) {
                this.anIntArray319[local43 + local61] = colour;
            }
            return;
        }
        @Pc(108) int local108;
        @Pc(116) int local116;
        if (arg4 == 1) {
            @Pc(102) int local102 = ((colour & 0xFF00FF) * local47 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local47 >> 8 & 0xFF00) + (local47 << 24);
            local61 = 256 - local47;
            for (local108 = 0; local108 < height; local108++) {
                local116 = this.anIntArray319[local43 + local108];
                local116 = ((local116 & 0xFF00FF) * local61 >> 8 & 0xFF00FF) + ((local116 & 0xFF00) * local61 >> 8 & 0xFF00);
                this.anIntArray319[local43 + local108] = local102 + local116;
            }
        } else if (arg4 == 2) {
            for (local61 = 0; local61 < height; local61++) {
                local108 = this.anIntArray319[local43 + local61];
                local116 = colour + local108;
                @Pc(176) int local176 = (colour & 0xFF00FF) + (local108 & 0xFF00FF);
                @Pc(186) int local186 = (local176 & 0x1000100) + (local116 - local176 & 0x10000);
                this.anIntArray319[local43 + local61] = local116 - local186 | local186 - (local186 >>> 8);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!iaa", name = "ZA", descriptor = "(IFFFFF)V")
    @Override
    public void ZA(@OriginalArg(0) int arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2, @OriginalArg(3) float arg3, @OriginalArg(4) float arg4, @OriginalArg(5) float arg5) {
        this.anInt4191 = (int) (arg1 * 65535.0F);
        this.anInt4187 = (int) (arg2 * 65535.0F);
        @Pc(26) float local26 = (float) Math.sqrt(arg3 * arg3 + arg4 * arg4 + arg5 * arg5);
        this.anInt4189 = (int) (arg3 * 65535.0F / local26);
        this.anInt4204 = (int) (arg4 * 65535.0F / local26);
        this.anInt4198 = (int) (arg5 * 65535.0F / local26);
    }

    @OriginalMember(owner = "client!iaa", name = "C", descriptor = "(Z)V")
    @Override
    public void C(@OriginalArg(0) boolean arg0) {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        local3.aBoolean804 = arg0;
    }

    @OriginalMember(owner = "client!iaa", name = "q", descriptor = "()V")
    @Override
    public void cacheReset() {
        this.aReferenceCache_89.reset();
        this.aReferenceCache_88.reset();
    }

    @OriginalMember(owner = "client!iaa", name = "o", descriptor = "(I)[I")
    public int[] method3788(@OriginalArg(0) int arg0) {
        @Pc(2) ReferenceCache local2 = this.aReferenceCache_89;
        @Pc(14) Node_Sub29 local14;
        synchronized (this.aReferenceCache_89) {
            local14 = (Node_Sub29) this.aReferenceCache_89.get((long) arg0 | Long.MIN_VALUE);
            if (local14 == null) {
                if (!super.textureSource.textureAvailable(arg0)) {
                    return null;
                }
                @Pc(36) TextureMetrics local36 = super.textureSource.getMetrics(arg0);
                @Pc(50) int local50 = local36.small || this.aBoolean332 ? 64 : this.lb;
                local14 = new Node_Sub29(arg0, local50, super.textureSource.argbOutput(0.7F, arg0, local50, local50), local36.alphaBlendMode != 1);
                this.aReferenceCache_89.put(local14, (long) arg0 | Long.MIN_VALUE);
            }
        }
        local14.awaitingTick = true;
        return local14.method3972();
    }

    @OriginalMember(owner = "client!iaa", name = "f", descriptor = "()V")
    @Override
    public void method7980() {
    }

    @OriginalMember(owner = "client!iaa", name = "A", descriptor = "(ILclient!aa;II)V")
    @Override
    public void A(@OriginalArg(0) int colour, @OriginalArg(1) ClippingMask clippingMask, @OriginalArg(2) int x, @OriginalArg(3) int y) {
        @Pc(2) ClippingMask_Sub1 local2 = (ClippingMask_Sub1) clippingMask;
        @Pc(5) int[] local5 = local2.anIntArray334;
        @Pc(8) int[] local8 = local2.anIntArray335;
        @Pc(20) int local20;
        if (this.anInt4196 < y + local5.length) {
            local20 = this.anInt4196 - y;
        } else {
            local20 = local5.length;
        }
        @Pc(33) int local33;
        if (this.anInt4186 > y) {
            local33 = this.anInt4186 - y;
            y = this.anInt4186;
        } else {
            local33 = 0;
        }
        if (local20 - local33 <= 0) {
            return;
        }
        @Pc(50) int local50 = y * this.anInt4207;
        for (@Pc(52) int local52 = local33; local52 < local20; local52++) {
            @Pc(59) int local59 = x + local5[local52];
            @Pc(63) int local63 = local8[local52];
            if (this.anInt4192 > local59) {
                local63 -= this.anInt4192 - local59;
                local59 = this.anInt4192;
            }
            if (this.anInt4200 < local59 + local63) {
                local63 = this.anInt4200 - local59;
            }
            local59 += local50;
            for (@Pc(95) int local95 = -local63; local95 < 0; local95++) {
                this.anIntArray319[local59++] = colour;
            }
            local50 += this.anInt4207;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIIIII)V")
    @Override
    public void method7995(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7) {
        arg2 -= arg0;
        arg3 -= arg1;
        @Pc(31) int local31;
        @Pc(35) int local35;
        if (arg3 == 0) {
            if (arg2 >= 0) {
                this.method3794(arg0, arg1, arg2 + 1, arg4, arg5, arg6, arg7);
            } else {
                local31 = arg5 + arg6;
                local35 = arg7 % local31;
                local35 = local31 + arg5 - local35 - (-arg2 + 1) % local31;
                arg7 = local35 % local31;
                if (arg7 < 0) {
                    arg7 += local31;
                }
                this.method3794(arg0 + arg2, arg1, 1 - arg2, arg4, arg5, arg6, arg7);
            }
        } else if (arg2 != 0) {
            local35 = arg7 << 8;
            @Pc(149) int local149 = arg5 << 8;
            @Pc(153) int local153 = arg6 << 8;
            local31 = local149 + local153;
            arg7 = local35 % local31;
            @Pc(178) int local178;
            @Pc(182) int local182;
            if (arg2 + arg3 < 0) {
                local178 = (int) (Math.sqrt(arg2 * arg2 + arg3 * arg3) * 256.0D);
                local182 = local178 % local31;
                local35 = local31 + local149 - arg7 - local182;
                arg7 = local35 % local31;
                if (arg7 < 0) {
                    arg7 += local31;
                }
                arg0 += arg2;
                arg2 = -arg2;
                arg1 += arg3;
                arg3 = -arg3;
            }
            @Pc(260) int local260;
            @Pc(278) int local278;
            @Pc(371) int local371;
            @Pc(405) int local405;
            @Pc(410) int local410;
            @Pc(243) int local243;
            @Pc(229) int local229;
            @Pc(362) int local362;
            if (arg2 > arg3) {
                arg1 <<= 0x10;
                arg1 += 32768;
                local229 = arg3 << 16;
                local178 = (int) Math.floor((double) local229 / (double) arg2 + 0.5D);
                local243 = arg2 + arg0;
                local182 = arg4 >>> 24;
                local260 = (int) Math.sqrt((local178 >> 8) * (local178 >> 8) + 65536);
                if (local182 == 255 && true) {
                    while (arg0 <= local243) {
                        local278 = arg1 >> 16;
                        if (arg0 >= this.anInt4192 && arg0 < this.anInt4200 && local278 >= this.anInt4186 && local278 < this.anInt4196 && arg7 < local149) {
                            this.anIntArray319[arg0 + local278 * this.anInt4207] = arg4;
                        }
                        arg1 += local178;
                        arg0++;
                        local35 = arg7 + local260;
                        arg7 = local35 % local31;
                    }
                } else {
                    local362 = ((arg4 & 0xFF00FF) * local182 >> 8 & 0xFF00FF) + ((arg4 & 0xFF00) * local182 >> 8 & 0xFF00) + (local182 << 24);
                    local278 = 256 - local182;
                    while (arg0 <= local243) {
                        local371 = arg1 >> 16;
                        if (arg0 >= this.anInt4192 && arg0 < this.anInt4200 && local371 >= this.anInt4186 && local371 < this.anInt4196 && arg7 < local149) {
                            local405 = arg0 + local371 * this.anInt4207;
                            local410 = this.anIntArray319[local405];
                            local410 = ((local410 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local410 & 0xFF00) * local278 >> 8 & 0xFF00);
                            this.anIntArray319[local405] = local362 + local410;
                        }
                        arg1 += local178;
                        arg0++;
                        local35 = arg7 + local260;
                        arg7 = local35 % local31;
                    }
                }
            } else {
                arg0 <<= 0x10;
                arg0 += 32768;
                local243 = arg2 << 16;
                local178 = (int) Math.floor((double) local243 / (double) arg3 + 0.5D);
                local229 = arg3 + arg1;
                local182 = arg4 >>> 24;
                local260 = (int) Math.sqrt((local178 >> 8) * (local178 >> 8) + 65536);
                if (local182 == 255 && true) {
                    while (arg1 <= local229) {
                        local278 = arg0 >> 16;
                        if (arg1 >= this.anInt4186 && arg1 < this.anInt4196 && local278 >= this.anInt4192 && local278 < this.anInt4200 && arg7 < local149) {
                            this.anIntArray319[local278 + arg1 * this.anInt4207] = arg4;
                        }
                        arg0 += local178;
                        arg1++;
                        local35 = arg7 + local260;
                        arg7 = local35 % local31;
                    }
                } else {
                    local362 = ((arg4 & 0xFF00FF) * local182 >> 8 & 0xFF00FF) + ((arg4 & 0xFF00) * local182 >> 8 & 0xFF00) + (local182 << 24);
                    local278 = 256 - local182;
                    while (arg1 <= local229) {
                        local371 = arg0 >> 16;
                        if (arg1 >= this.anInt4186 && arg1 < this.anInt4196 && local371 >= this.anInt4192 && local371 < this.anInt4200 && arg7 < local149) {
                            local405 = local371 + arg1 * this.anInt4207;
                            local410 = this.anIntArray319[local405];
                            @Pc(773) int local773 = ((local410 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local410 & 0xFF00) * local278 >> 8 & 0xFF00);
                            this.anIntArray319[local371 + arg1 * this.anInt4207] = local362 + local773;
                        }
                        arg0 += local178;
                        arg1++;
                        local35 = arg7 + local260;
                        arg7 = local35 % local31;
                    }
                }
            }
        } else if (arg3 >= 0) {
            this.method3783(arg0, arg1, arg3 + 1, arg4, arg5, arg6, arg7);
        } else {
            local31 = arg5 + arg6;
            local35 = arg7 % local31;
            local35 = local31 + arg5 - local35 - (-arg3 + 1) % local31;
            arg7 = local35 % local31;
            if (arg7 < 0) {
                arg7 += local31;
            }
            this.method3783(arg0, arg1 + arg3, -arg3 + 1, arg4, arg5, arg6, arg7);
        }
    }

    @OriginalMember(owner = "client!iaa", name = "K", descriptor = "([I)V")
    @Override
    public void K(@OriginalArg(0) int[] arg0) {
        arg0[0] = this.anInt4192;
        arg0[1] = this.anInt4186;
        arg0[2] = this.anInt4200;
        arg0[3] = this.anInt4196;
    }

    @OriginalMember(owner = "client!iaa", name = "i", descriptor = "()I")
    @Override
    public int i() {
        return this.anInt4214;
    }

    @OriginalMember(owner = "client!iaa", name = "c", descriptor = "()Lclient!dp;")
    @Override
    public Class84 method7981() {
        return new Class84(0, "Pure Java", 1, "CPU", 0L);
    }

    @OriginalMember(owner = "client!iaa", name = "f", descriptor = "(I)V")
    @Override
    public void setTextureSize(@OriginalArg(0) int size) {
        this.lb = size;
        this.aReferenceCache_89.reset();
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "([Ljava/awt/Rectangle;III)V")
    @Override
    public void method8011(@OriginalArg(0) Rectangle[] rectangles, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) throws Exception_Sub1 {
        if (this.aCanvas3 == null || this.aClass2_Sub10_1 == null) {
            throw new IllegalStateException("off");
        }
        try {
            @Pc(19) Graphics local19 = this.aCanvas3.getGraphics();
            for (@Pc(21) int local21 = 0; local21 < arg1; local21++) {
                @Pc(26) Rectangle local26 = rectangles[local21];
                if (local26.x + arg2 <= this.anInt4207 && local26.y + arg3 <= this.anInt4209 && local26.x + arg2 + local26.width > 0 && local26.y + arg3 + local26.height > 0) {
                    this.aClass2_Sub10_1.method6334(local26.width, local26.x + arg2, local26.x, local19, local26.height, local26.y, local26.y + arg3);
                }
            }
        } catch (@Pc(91) Exception local91) {
            this.aCanvas3.repaint();
        }
    }

    @OriginalMember(owner = "client!iaa", name = "D", descriptor = "()V")
    public void method3789() {
        for (@Pc(1) int local1 = 0; local1 < this.anInt4211; local1++) {
            this.aClass399Array1[local1].method9194();
        }
        this.la();
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!za;)V")
    @Override
    public void method7938(@OriginalArg(0) Node_Sub13 arg0) {
    }

    @OriginalMember(owner = "client!iaa", name = "k", descriptor = "(I)V")
    @Override
    public void method8020(@OriginalArg(0) int arg0) {
        this.aClass399Array1[arg0].method9196(Thread.currentThread());
    }

    @OriginalMember(owner = "client!iaa", name = "p", descriptor = "()Z")
    @Override
    public boolean method7968() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "([I)V")
    @Override
    public void method7944(@OriginalArg(0) int[] arg0) {
        arg0[0] = this.anInt4207;
        arg0[1] = this.anInt4209;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIII)V")
    @Override
    public void method7959(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
    }

    @OriginalMember(owner = "client!iaa", name = "c", descriptor = "(II)I")
    @Override
    public int combineFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        return maskA | maskB;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(II)Lclient!eca;")
    @Override
    public Surface method7962(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return this.createSprite(arg0, arg1, false);
    }

    @OriginalMember(owner = "client!iaa", name = "H", descriptor = "(III[I)V")
    @Override
    public void H(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3) {
        @Pc(24) float local24 = this.aClass73_Sub2_1.aFloat62 + this.aClass73_Sub2_1.aFloat56 * (float) arg0 + this.aClass73_Sub2_1.aFloat54 * (float) arg1 + this.aClass73_Sub2_1.aFloat61 * (float) arg2;
        if (local24 == 0.0F) {
            arg3[0] = arg3[1] = arg3[2] = -1;
            return;
        }
        @Pc(74) int local74 = (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat60 + this.aClass73_Sub2_1.aFloat59 * (float) arg0 + this.aClass73_Sub2_1.aFloat55 * (float) arg1 + this.aClass73_Sub2_1.aFloat53 * (float) arg2) / local24);
        @Pc(106) int local106 = (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat58 + this.aClass73_Sub2_1.aFloat57 * (float) arg0 + this.aClass73_Sub2_1.aFloat52 * (float) arg1 + this.aClass73_Sub2_1.aFloat51 * (float) arg2) / local24);
        arg3[0] = local74 - this.anInt4210;
        arg3[1] = local106 - this.anInt4208;
        arg3[2] = (int) local24;
    }

    @OriginalMember(owner = "client!iaa", name = "x", descriptor = "()Z")
    @Override
    public boolean method7937() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "ra", descriptor = "(IIII)V")
    @Override
    public void ra(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        for (@Pc(1) int local1 = 0; local1 < this.aClass399Array1.length; local1++) {
            this.aClass399Array1[local1].anInt10600 = this.aClass399Array1[local1].anInt10597;
            this.aClass399Array1[local1].anInt10604 = arg0;
            this.aClass399Array1[local1].anInt10597 = arg1;
            this.aClass399Array1[local1].anInt10602 = arg2;
            this.aClass399Array1[local1].aBoolean805 = true;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "e", descriptor = "(IIIIII)V")
    public void method3790(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        if (arg3 < 0) {
            arg3 = -arg3;
        }
        @Pc(8) int local8 = arg1 - arg3;
        if (local8 < this.anInt4186) {
            local8 = this.anInt4186;
        }
        @Pc(21) int local21 = arg1 + arg3 + 1;
        if (local21 > this.anInt4196) {
            local21 = this.anInt4196;
        }
        @Pc(30) int local30 = local8;
        @Pc(34) int local34 = arg3 * arg3;
        @Pc(36) int local36 = 0;
        @Pc(40) int local40 = arg1 - local8;
        @Pc(44) int local44 = local40 * local40;
        @Pc(48) int local48 = local44 - local40;
        if (arg1 > local21) {
            arg1 = local21;
        }
        @Pc(57) int local57 = arg4 >>> 24;
        @Pc(98) int local98;
        @Pc(109) int local109;
        @Pc(123) int local123;
        @Pc(125) int local125;
        if (arg5 == 0 || arg5 == 1 && local57 == 255) {
            while (local30 < arg1) {
                while (local48 <= local34 || local44 <= local34) {
                    local44 += local36 + local36;
                    local48 += local36++ + local36;
                }
                local98 = arg0 + 1 - local36;
                if (local98 < this.anInt4192) {
                    local98 = this.anInt4192;
                }
                local109 = arg0 + local36;
                if (local109 > this.anInt4200) {
                    local109 = this.anInt4200;
                }
                local123 = local98 + local30 * this.anInt4207;
                for (local125 = local98; local125 < local109; local125++) {
                    if ((float) arg2 < this.aFloatArray24[local123]) {
                        this.anIntArray319[local123] = arg4;
                    }
                    local123++;
                }
                local30++;
                local44 -= local40-- + local40;
                local48 -= local40 + local40;
            }
            local36 = arg3;
            local40 = local30 - arg1;
            local48 = local40 * local40 + local34;
            local44 = local48 - arg3;
            local48 -= local40;
            while (local30 < local21) {
                while (local48 > local34 && local44 > local34) {
                    local48 -= local36-- + local36;
                    local44 -= local36 + local36;
                }
                local98 = arg0 - local36;
                if (local98 < this.anInt4192) {
                    local98 = this.anInt4192;
                }
                local109 = arg0 + local36;
                if (local109 > this.anInt4200 - 1) {
                    local109 = this.anInt4200 - 1;
                }
                local123 = local98 + local30 * this.anInt4207;
                for (local125 = local98; local125 <= local109; local125++) {
                    if ((float) arg2 < this.aFloatArray24[local123]) {
                        this.anIntArray319[local123] = arg4;
                    }
                    local123++;
                }
                local30++;
                local48 += local40 + local40;
                local44 += local40++ + local40;
            }
            return;
        }
        @Pc(366) int local366;
        @Pc(380) int local380;
        if (arg5 == 1) {
            @Pc(307) int local307 = ((arg4 & 0xFF00FF) * local57 >> 8 & 0xFF00FF) + ((arg4 & 0xFF00) * local57 >> 8 & 0xFF00) + (local57 << 24);
            local98 = 256 - local57;
            while (local30 < arg1) {
                while (local48 <= local34 || local44 <= local34) {
                    local44 += local36 + local36;
                    local48 += local36++ + local36;
                }
                local109 = arg0 + 1 - local36;
                if (local109 < this.anInt4192) {
                    local109 = this.anInt4192;
                }
                local123 = arg0 + local36;
                if (local123 > this.anInt4200) {
                    local123 = this.anInt4200;
                }
                local125 = local109 + local30 * this.anInt4207;
                for (local366 = local109; local366 < local123; local366++) {
                    if ((float) arg2 < this.aFloatArray24[local125]) {
                        local380 = this.anIntArray319[local125];
                        local380 = ((local380 & 0xFF00FF) * local98 >> 8 & 0xFF00FF) + ((local380 & 0xFF00) * local98 >> 8 & 0xFF00);
                        this.anIntArray319[local125] = local307 + local380;
                    }
                    local125++;
                }
                local30++;
                local44 -= local40-- + local40;
                local48 -= local40 + local40;
            }
            local36 = arg3;
            local40 = -local40;
            local48 = local40 * local40 + local34;
            local44 = local48 - arg3;
            local48 -= local40;
            while (local30 < local21) {
                while (local48 > local34 && local44 > local34) {
                    local48 -= local36-- + local36;
                    local44 -= local36 + local36;
                }
                local109 = arg0 - local36;
                if (local109 < this.anInt4192) {
                    local109 = this.anInt4192;
                }
                local123 = arg0 + local36;
                if (local123 > this.anInt4200 - 1) {
                    local123 = this.anInt4200 - 1;
                }
                local125 = local109 + local30 * this.anInt4207;
                for (local366 = local109; local366 <= local123; local366++) {
                    if ((float) arg2 < this.aFloatArray24[local125]) {
                        local380 = this.anIntArray319[local125];
                        local380 = ((local380 & 0xFF00FF) * local98 >> 8 & 0xFF00FF) + ((local380 & 0xFF00) * local98 >> 8 & 0xFF00);
                        this.anIntArray319[local125] = local307 + local380;
                    }
                    local125++;
                }
                local30++;
                local48 += local40 + local40;
                local44 += local40++ + local40;
            }
        } else if (arg5 == 2) {
            @Pc(655) int local655;
            while (local30 < arg1) {
                while (local48 <= local34 || local44 <= local34) {
                    local44 += local36 + local36;
                    local48 += local36++ + local36;
                }
                local98 = arg0 + 1 - local36;
                if (local98 < this.anInt4192) {
                    local98 = this.anInt4192;
                }
                local109 = arg0 + local36;
                if (local109 > this.anInt4200) {
                    local109 = this.anInt4200;
                }
                local123 = local98 + local30 * this.anInt4207;
                for (local125 = local98; local125 < local109; local125++) {
                    if ((float) arg2 < this.aFloatArray24[local123]) {
                        local366 = this.anIntArray319[local123];
                        local380 = arg4 + local366;
                        local655 = (arg4 & 0xFF00FF) + (local366 & 0xFF00FF);
                        @Pc(665) int local665 = (local655 & 0x1000100) + (local380 - local655 & 0x10000);
                        this.anIntArray319[local123] = local380 - local665 | local665 - (local665 >>> 8);
                    }
                    local123++;
                }
                local30++;
                local44 -= local40-- + local40;
                local48 -= local40 + local40;
            }
            local36 = arg3;
            local40 = -local40;
            local48 = local40 * local40 + local34;
            local44 = local48 - arg3;
            local48 -= local40;
            while (local30 < local21) {
                while (local48 > local34 && local44 > local34) {
                    local48 -= local36-- + local36;
                    local44 -= local36 + local36;
                }
                local98 = arg0 - local36;
                if (local98 < this.anInt4192) {
                    local98 = this.anInt4192;
                }
                local109 = arg0 + local36;
                if (local109 > this.anInt4200 - 1) {
                    local109 = this.anInt4200 - 1;
                }
                local123 = local98 + local30 * this.anInt4207;
                for (local125 = local98; local125 <= local109; local125++) {
                    if ((float) arg2 < this.aFloatArray24[local123]) {
                        local366 = this.anIntArray319[local123];
                        local380 = arg4 + local366;
                        local655 = (arg4 & 0xFF00FF) + (local366 & 0xFF00FF);
                        local366 = (local655 & 0x1000100) + (local380 - local655 & 0x10000);
                        this.anIntArray319[local123] = local380 - local366 | local366 - (local366 >>> 8);
                    }
                    local123++;
                }
                local30++;
                local48 += local40 + local40;
                local44 += local40++ + local40;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!iaa", name = "KA", descriptor = "(IIII)V")
    @Override
    public void KA(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2) {
        if (x1 < 0) {
            x1 = 0;
        }
        if (y1 < 0) {
            y1 = 0;
        }
        if (x2 > this.anInt4207) {
            x2 = this.anInt4207;
        }
        if (y2 > this.anInt4209) {
            y2 = this.anInt4209;
        }
        this.anInt4192 = x1;
        this.anInt4200 = x2;
        this.anInt4186 = y1;
        this.anInt4196 = y2;
        this.method3799();
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(II[I[I)Lclient!aa;")
    @Override
    public ClippingMask createMask(@OriginalArg(0) int width, @OriginalArg(1) int height, @OriginalArg(2) int[] offsets, @OriginalArg(3) int[] widths) {
        return new ClippingMask_Sub1(width, height, offsets, widths);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!pu;Lclient!pu;FLclient!pu;)Lclient!pu;")
    @Override
    public Class67 method8007(@OriginalArg(0) Class67 arg0, @OriginalArg(1) Class67 arg1, @OriginalArg(2) float arg2, @OriginalArg(3) Class67 arg3) {
        return null;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIF)Lclient!lca;")
    @Override
    public PointLight method7941(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) float arg5) {
        return null;
    }

    @OriginalMember(owner = "client!iaa", name = "da", descriptor = "(III[I)V")
    @Override
    public void da(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3) {
        @Pc(24) float local24 = this.aClass73_Sub2_1.aFloat62 + this.aClass73_Sub2_1.aFloat56 * (float) arg0 + this.aClass73_Sub2_1.aFloat54 * (float) arg1 + this.aClass73_Sub2_1.aFloat61 * (float) arg2;
        if (local24 < (float) this.anInt4214 || local24 > (float) this.anInt4199) {
            arg3[0] = arg3[1] = arg3[2] = -1;
            return;
        }
        @Pc(84) int local84 = (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat60 + this.aClass73_Sub2_1.aFloat59 * (float) arg0 + this.aClass73_Sub2_1.aFloat55 * (float) arg1 + this.aClass73_Sub2_1.aFloat53 * (float) arg2) / local24);
        @Pc(116) int local116 = (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat58 + this.aClass73_Sub2_1.aFloat57 * (float) arg0 + this.aClass73_Sub2_1.aFloat52 * (float) arg1 + this.aClass73_Sub2_1.aFloat51 * (float) arg2) / local24);
        if (local84 >= this.anInt4210 && local84 <= this.anInt4193 && local116 >= this.anInt4208 && local116 <= this.anInt4201) {
            arg3[0] = local84 - this.anInt4210;
            arg3[1] = local116 - this.anInt4208;
            arg3[2] = (int) local24;
        } else {
            arg3[0] = arg3[1] = arg3[2] = -1;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "HA", descriptor = "(IIII[I)V")
    @Override
    public void HA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int[] arg4) {
        @Pc(24) float local24 = this.aClass73_Sub2_1.aFloat62 + this.aClass73_Sub2_1.aFloat56 * (float) arg0 + this.aClass73_Sub2_1.aFloat54 * (float) arg1 + this.aClass73_Sub2_1.aFloat61 * (float) arg2;
        if (local24 < (float) this.anInt4214 || local24 > (float) this.anInt4199) {
            arg4[0] = arg4[1] = arg4[2] = -1;
            return;
        }
        @Pc(85) int local85 = (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat60 + this.aClass73_Sub2_1.aFloat59 * (float) arg0 + this.aClass73_Sub2_1.aFloat55 * (float) arg1 + this.aClass73_Sub2_1.aFloat53 * (float) arg2) / (float) arg3);
        @Pc(118) int local118 = (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat58 + this.aClass73_Sub2_1.aFloat57 * (float) arg0 + this.aClass73_Sub2_1.aFloat52 * (float) arg1 + this.aClass73_Sub2_1.aFloat51 * (float) arg2) / (float) arg3);
        if (local85 >= this.anInt4210 && local85 <= this.anInt4193 && local118 >= this.anInt4208 && local118 <= this.anInt4201) {
            arg4[0] = local85 - this.anInt4210;
            arg4[1] = local118 - this.anInt4208;
            arg4[2] = (int) local24;
        } else {
            arg4[0] = arg4[1] = arg4[2] = -1;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "k", descriptor = "()Z")
    @Override
    public boolean method7949() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "c", descriptor = "(I)V")
    @Override
    public void method8016(@OriginalArg(0) int arg0) {
        this.aClass399Array1[arg0].method9196(null);
    }

    @OriginalMember(owner = "client!iaa", name = "j", descriptor = "(I)V")
    @Override
    public void method7956(@OriginalArg(0) int arg0) {
        this.anInt4211 = arg0;
        this.aClass399Array1 = new Class399[this.anInt4211];
        for (@Pc(9) int local9 = 0; local9 < this.anInt4211; local9++) {
            this.aClass399Array1[local9] = new Class399(this);
        }
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(I)V")
    @Override
    public void method8003() {
        Static567.anInt8484 = 10000;
        Static567.anInt8486 = 10000;
        if (this.anInt4211 > 1) {
            throw new IllegalStateException("No MT");
        }
        this.method7956(this.anInt4211);
        this.method8020(0);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "([IIIIIZ)Lclient!st;")
    @Override
    public Sprite method7958(@OriginalArg(0) int[] arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) boolean arg4) {
        @Pc(1) boolean local1 = false;
        @Pc(3) int local3 = 0;
        for (@Pc(5) int local5 = 0; local5 < arg3; local5++) {
            for (@Pc(8) int local8 = 0; local8 < arg2; local8++) {
                @Pc(16) int local16 = arg0[local3++] >>> 24;
                if (local16 != 0 && local16 != 255) {
                    local1 = true;
                    return local1 ? new Sprite_Sub1_Sub2(this, arg0, 0, arg1, arg2, arg3, arg4) : new Sprite_Sub1_Sub1(this, arg0, 0, arg1, arg2, arg3, arg4);
                }
            }
        }
        return local1 ? new Sprite_Sub1_Sub2(this, arg0, 0, arg1, arg2, arg3, arg4) : new Sprite_Sub1_Sub1(this, arg0, 0, arg1, arg2, arg3, arg4);
    }

    @OriginalMember(owner = "client!iaa", name = "E", descriptor = "()I")
    @Override
    public int E() {
        return 0;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!pu;)V")
    @Override
    public void method7973(@OriginalArg(0) Class67 arg0) {
    }

    @OriginalMember(owner = "client!iaa", name = "s", descriptor = "()Z")
    @Override
    public boolean method7979() {
        return true;
    }

    @OriginalMember(owner = "client!iaa", name = "e", descriptor = "(I)V")
    @Override
    public void tick(@OriginalArg(0) int time) {
        @Pc(4) int elapsed = time - this.lastTickTime;

        for (@Pc(9) Object object = this.aReferenceCache_89.first(); object != null; object = this.aReferenceCache_89.next()) {
            @Pc(13) Node_Sub29 local13 = (Node_Sub29) object;

            if (local13.awaitingTick) {
                local13.runningTime += elapsed;

                @Pc(27) int frames = local13.runningTime / 20;
                if (frames > 0) {
                    @Pc(36) TextureMetrics metrics = super.textureSource.getMetrics(local13.anInt4408);
                    local13.method3973((metrics.speedU * elapsed * 50) / 1000, (metrics.speedV * elapsed * 50) / 1000);
                    local13.runningTime -= frames * 20;
                }

                local13.awaitingTick = false;
            }
        }

        this.lastTickTime = time;
        this.aReferenceCache_88.clean(5);
        this.aReferenceCache_89.clean(5);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(FFF)V")
    @Override
    public void method7993(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2) {
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(IIIIIIIIII)V")
    public void method3791(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) int arg9) {
        if (arg3 == 0 || arg4 == 0) {
            return;
        }
        if (arg6 == 65535 || super.textureSource.getMetrics(arg6).aBoolean240) {
            this.method3790(arg0, arg1, arg2, arg3, arg7, arg9);
            return;
        }
        if (this.anInt4215 != arg6) {
            @Pc(33) Sprite local33 = (Sprite) this.aReferenceCache_88.get(arg6);
            if (local33 == null) {
                @Pc(39) int[] local39 = this.method3788(arg6);
                if (local39 == null) {
                    return;
                }
                @Pc(53) int local53 = this.method3798(arg6) ? 64 : this.lb;
                local33 = this.createSprite(local53, local53, local53, local39);
                this.aReferenceCache_88.put(local33, arg6);
            }
            this.anInt4215 = arg6;
            this.aSprite_17 = local33;
        }
        ((Sprite_Sub1) this.aSprite_17).method8208(arg0 - arg3, arg1 - arg4, arg2, arg3 << 1, arg4 << 1, arg8, arg7, arg9);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIZ)Lclient!st;")
    @Override
    public Sprite createSprite(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        @Pc(4) int[] local4 = new int[arg2 * arg3];
        @Pc(11) int local11 = arg1 * this.anInt4207 + arg0;
        @Pc(16) int local16 = this.anInt4207 - arg2;
        for (@Pc(18) int local18 = 0; local18 < arg3; local18++) {
            @Pc(23) int local23 = local18 * arg2;
            for (@Pc(25) int local25 = 0; local25 < arg2; local25++) {
                local4[local23 + local25] = this.anIntArray319[local11++];
            }
            local11 += local16;
        }
        if (arg4) {
            return new Sprite_Sub1_Sub2(this, local4, arg2, arg3);
        } else {
            return new Sprite_Sub1_Sub1(this, local4, arg2, arg3);
        }
    }

    @OriginalMember(owner = "client!iaa", name = "T", descriptor = "(IIII)V")
    @Override
    public void T(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        if (this.anInt4192 < arg0) {
            this.anInt4192 = arg0;
        }
        if (this.anInt4186 < arg1) {
            this.anInt4186 = arg1;
        }
        if (this.anInt4200 > arg2) {
            this.anInt4200 = arg2;
        }
        if (this.anInt4196 > arg3) {
            this.anInt4196 = arg3;
        }
        this.method3799();
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!wp;Z)Lclient!st;")
    @Override
    public Sprite createSprite(@OriginalArg(0) IndexedImage arg0, @OriginalArg(1) boolean arg1) {
        @Pc(2) int[] local2 = arg0.palette;
        @Pc(5) byte[] local5 = arg0.raster;
        @Pc(8) int local8 = arg0.width;
        @Pc(11) int local11 = arg0.height;
        @Pc(80) Sprite_Sub1 local80;
        @Pc(22) int[] local22;
        @Pc(27) byte[] local27;
        @Pc(29) int local29;
        @Pc(34) int local34;
        @Pc(36) int local36;
        if (arg1 && arg0.alpha == null) {
            local22 = new int[local2.length];
            local27 = new byte[local8 * local11];
            for (local29 = 0; local29 < local11; local29++) {
                local34 = local29 * local8;
                for (local36 = 0; local36 < local8; local36++) {
                    local27[local34 + local36] = local5[local34 + local36];
                }
            }
            for (local34 = 0; local34 < local2.length; local34++) {
                local22[local34] = local2[local34];
            }
            local80 = new Sprite_Sub1_Sub3(this, local27, local22, local8, local11);
        } else {
            local22 = new int[local8 * local11];
            local27 = arg0.alpha;
            if (local27 == null) {
                for (local29 = 0; local29 < local11; local29++) {
                    local34 = local29 * local8;
                    for (local36 = 0; local36 < local8; local36++) {
                        @Pc(162) int local162 = local2[local5[local34 + local36] & 0xFF];
                        local22[local34 + local36] = local162 == 0 ? 0 : local162 | 0xFF000000;
                    }
                }
                local80 = new Sprite_Sub1_Sub1(this, local22, local8, local11);
            } else {
                for (local29 = 0; local29 < local11; local29++) {
                    local34 = local29 * local8;
                    for (local36 = 0; local36 < local8; local36++) {
                        local22[local34 + local36] = local2[local5[local34 + local36] & 0xFF] | local27[local34 + local36] << 24;
                    }
                }
                local80 = new Sprite_Sub1_Sub2(this, local22, local8, local11);
            }
        }
        local80.method8184(arg0.offX1, arg0.offY1, arg0.offX2, arg0.offY2);
        return local80;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(II[[I[[IIII)Lclient!s;")
    @Override
    public Ground createGround(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[][] arg2, @OriginalArg(3) int[][] arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        return new Ground_Sub3(this, arg4, arg5, arg0, arg1, arg2, arg3, 512);
    }

    @OriginalMember(owner = "client!iaa", name = "JA", descriptor = "(IIIIII)I")
    @Override
    public int JA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(1) int local1 = 0;
        @Pc(26) float local26 = this.aClass73_Sub2_1.aFloat56 * (float) arg0 + this.aClass73_Sub2_1.aFloat54 * (float) arg1 + this.aClass73_Sub2_1.aFloat61 * (float) arg2 + this.aClass73_Sub2_1.aFloat62;
        if (local26 < 1.0F) {
            local26 = 1.0F;
        }
        @Pc(57) float local57 = this.aClass73_Sub2_1.aFloat56 * (float) arg3 + this.aClass73_Sub2_1.aFloat54 * (float) arg4 + this.aClass73_Sub2_1.aFloat61 * (float) arg5 + this.aClass73_Sub2_1.aFloat62;
        if (local57 < 1.0F) {
            local57 = 1.0F;
        }
        if (local26 < (float) this.anInt4214 && local57 < (float) this.anInt4214) {
            local1 |= 0x10;
        } else if (local26 > (float) this.anInt4199 && local57 > (float) this.anInt4199) {
            local1 |= 0x20;
        }
        @Pc(132) int local132 = (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat59 * (float) arg0 + this.aClass73_Sub2_1.aFloat55 * (float) arg1 + this.aClass73_Sub2_1.aFloat53 * (float) arg2 + this.aClass73_Sub2_1.aFloat60) / local26);
        @Pc(164) int local164 = (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat59 * (float) arg3 + this.aClass73_Sub2_1.aFloat55 * (float) arg4 + this.aClass73_Sub2_1.aFloat53 * (float) arg5 + this.aClass73_Sub2_1.aFloat60) / local57);
        if (local132 < this.anInt4210 && local164 < this.anInt4210) {
            local1 |= 0x1;
        } else if (local132 > this.anInt4193 && local164 > this.anInt4193) {
            local1 |= 0x2;
        }
        @Pc(225) int local225 = (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat57 * (float) arg0 + this.aClass73_Sub2_1.aFloat52 * (float) arg1 + this.aClass73_Sub2_1.aFloat51 * (float) arg2 + this.aClass73_Sub2_1.aFloat58) / local26);
        @Pc(257) int local257 = (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat57 * (float) arg3 + this.aClass73_Sub2_1.aFloat52 * (float) arg4 + this.aClass73_Sub2_1.aFloat51 * (float) arg5 + this.aClass73_Sub2_1.aFloat58) / local57);
        if (local225 < this.anInt4208 && local257 < this.anInt4208) {
            local1 |= 0x4;
        } else if (local225 > this.anInt4201 && local257 > this.anInt4201) {
            local1 |= 0x8;
        }
        return local1;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void line(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        x2 -= x1;
        y2 -= y1;
        if (y2 == 0) {
            if (x2 >= 0) {
                this.U(x1, y1, x2 + 1, colour, mode);
            } else {
                this.U(x1 + x2, y1, 1 - x2, colour, mode);
            }
        } else if (x2 != 0) {
            if (x2 + y2 < 0) {
                x1 += x2;
                x2 = -x2;
                y1 += y2;
                y2 = -y2;
            }
            @Pc(110) int local110;
            @Pc(143) int local143;
            @Pc(161) int local161;
            @Pc(229) int local229;
            @Pc(246) int local246;
            @Pc(251) int local251;
            @Pc(331) int local331;
            @Pc(220) int local220;
            if (x2 > y2) {
                y1 <<= 0x10;
                y1 += 32768;
                @Pc(100) int local100 = y2 << 16;
                local110 = (int) Math.floor((double) local100 / (double) x2 + 0.5D);
                x2 += x1;
                if (x1 < this.anInt4192) {
                    y1 += local110 * (this.anInt4192 - x1);
                    x1 = this.anInt4192;
                }
                if (x2 >= this.anInt4200) {
                    x2 = this.anInt4200 - 1;
                }
                local143 = colour >>> 24;
                if (mode == 0 || mode == 1 && local143 == 255) {
                    while (x1 <= x2) {
                        local161 = y1 >> 16;
                        if (local161 >= this.anInt4186 && local161 < this.anInt4196) {
                            this.anIntArray319[x1 + local161 * this.anInt4207] = colour;
                        }
                        y1 += local110;
                        x1++;
                    }
                } else if (mode == 1) {
                    local220 = ((colour & 0xFF00FF) * local143 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local143 >> 8 & 0xFF00) + (local143 << 24);
                    local161 = 256 - local143;
                    while (x1 <= x2) {
                        local229 = y1 >> 16;
                        if (local229 >= this.anInt4186 && local229 < this.anInt4196) {
                            local246 = x1 + local229 * this.anInt4207;
                            local251 = this.anIntArray319[local246];
                            local251 = ((local251 & 0xFF00FF) * local161 >> 8 & 0xFF00FF) + ((local251 & 0xFF00) * local161 >> 8 & 0xFF00);
                            this.anIntArray319[local246] = local220 + local251;
                        }
                        y1 += local110;
                        x1++;
                    }
                } else if (mode == 2) {
                    while (x1 <= x2) {
                        local161 = y1 >> 16;
                        if (local161 >= this.anInt4186 && local161 < this.anInt4196) {
                            local229 = x1 + local161 * this.anInt4207;
                            local246 = this.anIntArray319[local229];
                            local251 = colour + local246;
                            local331 = (colour & 0xFF00FF) + (local246 & 0xFF00FF);
                            local246 = (local331 & 0x1000100) + (local251 - local331 & 0x10000);
                            this.anIntArray319[local229] = local251 - local246 | local246 - (local246 >>> 8);
                        }
                        y1 += local110;
                        x1++;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                x1 <<= 0x10;
                x1 += 32768;
                @Pc(380) int local380 = x2 << 16;
                local110 = (int) Math.floor((double) local380 / (double) y2 + 0.5D);
                y2 += y1;
                if (y1 < this.anInt4186) {
                    x1 += local110 * (this.anInt4186 - y1);
                    y1 = this.anInt4186;
                }
                if (y2 >= this.anInt4196) {
                    y2 = this.anInt4196 - 1;
                }
                local143 = colour >>> 24;
                if (mode == 0 || mode == 1 && local143 == 255) {
                    while (y1 <= y2) {
                        local161 = x1 >> 16;
                        if (local161 >= this.anInt4192 && local161 < this.anInt4200) {
                            this.anIntArray319[local161 + y1 * this.anInt4207] = colour;
                        }
                        x1 += local110;
                        y1++;
                    }
                } else if (mode == 1) {
                    local220 = ((colour & 0xFF00FF) * local143 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local143 >> 8 & 0xFF00) + (local143 << 24);
                    local161 = 256 - local143;
                    while (y1 <= y2) {
                        local229 = x1 >> 16;
                        if (local229 >= this.anInt4192 && local229 < this.anInt4200) {
                            local246 = local229 + y1 * this.anInt4207;
                            local251 = this.anIntArray319[local246];
                            local251 = ((local251 & 0xFF00FF) * local161 >> 8 & 0xFF00FF) + ((local251 & 0xFF00) * local161 >> 8 & 0xFF00);
                            this.anIntArray319[local229 + y1 * this.anInt4207] = local220 + local251;
                        }
                        x1 += local110;
                        y1++;
                    }
                } else if (mode == 2) {
                    while (y1 <= y2) {
                        local161 = x1 >> 16;
                        if (local161 >= this.anInt4192 && local161 < this.anInt4200) {
                            local229 = local161 + y1 * this.anInt4207;
                            local246 = this.anIntArray319[local229];
                            local251 = colour + local246;
                            local331 = (colour & 0xFF00FF) + (local246 & 0xFF00FF);
                            @Pc(626) int local626 = (local331 & 0x1000100) + (local251 - local331 & 0x10000);
                            this.anIntArray319[local229] = local251 - local626 | local626 - (local626 >>> 8);
                        }
                        x1 += local110;
                        y1++;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } else if (y2 >= 0) {
            this.P(x1, y1, y2 + 1, colour, mode);
        } else {
            this.P(x1, y1 + y2, -y2 + 1, colour, mode);
        }
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(Z)V")
    @Override
    public void method8018(@OriginalArg(0) boolean arg0) {
    }

    @OriginalMember(owner = "client!iaa", name = "l", descriptor = "(I)[I")
    public int[] method3792(@OriginalArg(0) int arg0) {
        @Pc(2) ReferenceCache local2 = this.aReferenceCache_89;
        @Pc(12) Node_Sub29 local12;
        synchronized (this.aReferenceCache_89) {
            local12 = (Node_Sub29) this.aReferenceCache_89.get(arg0);
            if (local12 == null) {
                if (!super.textureSource.textureAvailable(arg0)) {
                    return null;
                }
                @Pc(34) TextureMetrics local34 = super.textureSource.getMetrics(arg0);
                @Pc(48) int local48 = local34.small || this.aBoolean332 ? 64 : this.lb;
                local12 = new Node_Sub29(arg0, local48, super.textureSource.rgbOutput(local48, true, local48, arg0, 0.7F), local34.alphaBlendMode != 1);
                this.aReferenceCache_89.put(local12, arg0);
            }
        }
        local12.awaitingTick = true;
        return local12.method3972();
    }

    @OriginalMember(owner = "client!iaa", name = "r", descriptor = "()Z")
    @Override
    public boolean hardShadow() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "r", descriptor = "(IIIIIII)I")
    @Override
    public int r(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        @Pc(24) float local24 = this.aClass73_Sub2_1.aFloat56 * (float) arg0 + this.aClass73_Sub2_1.aFloat54 * (float) arg1 + this.aClass73_Sub2_1.aFloat61 * (float) arg2 + this.aClass73_Sub2_1.aFloat62;
        @Pc(49) float local49 = this.aClass73_Sub2_1.aFloat56 * (float) arg3 + this.aClass73_Sub2_1.aFloat54 * (float) arg4 + this.aClass73_Sub2_1.aFloat61 * (float) arg5 + this.aClass73_Sub2_1.aFloat62;
        @Pc(51) int local51 = 0;
        if (local24 < (float) this.anInt4214 && local49 < (float) this.anInt4214) {
            local51 |= 0x10;
        } else if (local24 > (float) this.anInt4199 && local49 > (float) this.anInt4199) {
            local51 |= 0x20;
        }
        @Pc(121) int local121 = (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat59 * (float) arg0 + this.aClass73_Sub2_1.aFloat55 * (float) arg1 + this.aClass73_Sub2_1.aFloat53 * (float) arg2 + this.aClass73_Sub2_1.aFloat60) / (float) arg6);
        @Pc(154) int local154 = (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat59 * (float) arg3 + this.aClass73_Sub2_1.aFloat55 * (float) arg4 + this.aClass73_Sub2_1.aFloat53 * (float) arg5 + this.aClass73_Sub2_1.aFloat60) / (float) arg6);
        if (local121 < this.anInt4210 && local154 < this.anInt4210) {
            local51 |= 0x1;
        } else if (local121 > this.anInt4193 && local154 > this.anInt4193) {
            local51 |= 0x2;
        }
        @Pc(216) int local216 = (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat57 * (float) arg0 + this.aClass73_Sub2_1.aFloat52 * (float) arg1 + this.aClass73_Sub2_1.aFloat51 * (float) arg2 + this.aClass73_Sub2_1.aFloat58) / (float) arg6);
        @Pc(249) int local249 = (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat57 * (float) arg3 + this.aClass73_Sub2_1.aFloat52 * (float) arg4 + this.aClass73_Sub2_1.aFloat51 * (float) arg5 + this.aClass73_Sub2_1.aFloat58) / (float) arg6);
        if (local216 < this.anInt4208 && local249 < this.anInt4208) {
            local51 |= 0x4;
        } else if (local216 > this.anInt4201 && local249 > this.anInt4201) {
            local51 |= 0x8;
        }
        return local51;
    }

    @OriginalMember(owner = "client!iaa", name = "pa", descriptor = "()V")
    @Override
    public void pa() {
        for (@Pc(1) int local1 = 0; local1 < this.aClass399Array1.length; local1++) {
            this.aClass399Array1[local1].anInt10597 = this.aClass399Array1[local1].anInt10600;
            this.aClass399Array1[local1].aBoolean805 = false;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "m", descriptor = "(I)I")
    public int method3793(@OriginalArg(0) int arg0) {
        return super.textureSource.getMetrics(arg0).aShort37 & 0xFFFF;
    }

    @OriginalMember(owner = "client!iaa", name = "X", descriptor = "(I)V")
    @Override
    public void X(@OriginalArg(0) int arg0) {
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(IIIIIIII)V")
    public void method3794(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6) {
        if (arg1 < this.anInt4186 || arg1 >= this.anInt4196) {
            return;
        }
        @Pc(18) int local18 = arg0 + arg1 * this.anInt4207;
        @Pc(22) int local22 = arg3 >>> 24;
        @Pc(26) int local26 = arg4 + arg5;
        @Pc(30) int local30 = arg6 % local26;
        @Pc(44) int local44;
        if (local22 == 255 && true) {
            local44 = 0;
            while (local44 < arg2) {
                if (arg0 + local44 >= this.anInt4192 && arg0 + local44 < this.anInt4200 && local30 < arg4) {
                    this.anIntArray319[local18 + local44] = arg3;
                }
                local44++;
                local30++;
                local30 %= local26;
            }
            return;
        }
        @Pc(111) int local111 = ((arg3 & 0xFF00FF) * local22 >> 8 & 0xFF00FF) + ((arg3 & 0xFF00) * local22 >> 8 & 0xFF00) + (local22 << 24);
        local44 = 256 - local22;
        @Pc(117) int local117 = 0;
        while (local117 < arg2) {
            if (arg0 + local117 >= this.anInt4192 && arg0 + local117 < this.anInt4200 && local30 < arg4) {
                @Pc(144) int local144 = this.anIntArray319[local18 + local117];
                @Pc(164) int local164 = ((local144 & 0xFF00FF) * local44 >> 8 & 0xFF00FF) + ((local144 & 0xFF00) * local44 >> 8 & 0xFF00);
                this.anIntArray319[local18 + local117] = local111 + local164;
            }
            local117++;
            local30++;
            local30 %= local26;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "e", descriptor = "(II)V")
    @Override
    public void flip(@OriginalArg(0) int x, @OriginalArg(1) int y) throws Exception_Sub1 {
        if (this.aCanvas3 == null || this.aClass2_Sub10_1 == null) {
            throw new IllegalStateException("off");
        }
        try {
            @Pc(19) Graphics local19 = this.aCanvas3.getGraphics();
            this.aClass2_Sub10_1.method6334(this.anInt4183, x, 0, local19, this.anInt4185, 0, y);
        } catch (@Pc(34) Exception local34) {
            this.aCanvas3.repaint();
        }
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(IIIID)V")
    @Override
    public void b(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) double arg4) {
        @Pc(4) int local4 = this.anInt4190 - arg2;
        @Pc(11) int local11 = arg1 * this.anInt4190 + arg0;
        @Pc(14) float[] local14 = this.aFloatArray24;
        @Pc(16) int local16 = 0;
        while (local16 < arg3) {
            @Pc(19) int local19 = 0;
            while (local19 < arg2) {
                @Pc(24) float local24 = local14[local11];
                if (local24 != 2.14748365E9F) {
                    local14[local11] = (float) ((double) local24 + arg4);
                }
                local19++;
                local11++;
            }
            local16++;
            local11 += local4;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!ve;[Lclient!wp;Z)Lclient!da;")
    @Override
    public Font createFont(@OriginalArg(0) FontMetrics arg0, @OriginalArg(1) IndexedImage[] arg1, @OriginalArg(2) boolean arg2) {
        @Pc(3) int[] local3 = new int[arg1.length];
        @Pc(7) int[] local7 = new int[arg1.length];
        @Pc(9) boolean local9 = false;
        for (@Pc(11) int local11 = 0; local11 < arg1.length; local11++) {
            local3[local11] = arg1[local11].width;
            local7[local11] = arg1[local11].height;
            if (arg1[local11].alpha != null) {
                local9 = true;
            }
        }
        if (arg2) {
            if (local9) {
                return new Font_Sub4(this, arg0, arg1, local3, local7);
            } else {
                return new Font_Sub5(this, arg0, arg1, local3, local7);
            }
        } else if (local9) {
            throw new IllegalArgumentException("");
        } else {
            return new Font_Sub3(this, arg0, arg1, local3, local7);
        }
    }

    @OriginalMember(owner = "client!iaa", name = "Q", descriptor = "(IIIIII[BII)V")
    @Override
    public void Q(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) byte[] arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8) {
        if (arg2 <= 0 || arg3 <= 0) {
            return;
        }
        @Pc(9) int local9 = 0;
        @Pc(11) int local11 = 0;
        @Pc(17) int local17 = (arg7 << 16) / arg2;
        @Pc(26) int local26 = (arg6.length / arg7 << 16) / arg3;
        @Pc(33) int local33 = arg0 + arg1 * this.anInt4207;
        @Pc(38) int local38 = this.anInt4207 - arg2;
        if (arg1 + arg3 > this.anInt4196) {
            arg3 -= arg1 + arg3 - this.anInt4196;
        }
        @Pc(62) int local62;
        if (arg1 < this.anInt4186) {
            local62 = this.anInt4186 - arg1;
            arg3 -= local62;
            local33 += local62 * this.anInt4207;
            local11 += local26 * local62;
        }
        if (arg0 + arg2 > this.anInt4200) {
            local62 = arg0 + arg2 - this.anInt4200;
            arg2 -= local62;
            local38 += local62;
        }
        if (arg0 < this.anInt4192) {
            local62 = this.anInt4192 - arg0;
            arg2 -= local62;
            local33 += local62;
            local9 += local17 * local62;
            local38 += local62;
        }
        local62 = arg4 >>> 24;
        @Pc(135) int local135 = arg5 >>> 24;
        @Pc(154) int local154;
        @Pc(157) int local157;
        @Pc(164) int local164;
        @Pc(167) int local167;
        if (arg8 == 0 || arg8 == 1 && local62 == 255 && local135 == 255) {
            local154 = local9;
            for (local157 = -arg3; local157 < 0; local157++) {
                local164 = (local11 >> 16) * arg7;
                for (local167 = -arg2; local167 < 0; local167++) {
                    if (arg6[(local9 >> 16) + local164] == 0) {
                        this.anIntArray319[local33++] = arg4;
                    } else {
                        this.anIntArray319[local33++] = arg5;
                    }
                    local9 += local17;
                }
                local11 += local26;
                local9 = local154;
                local33 += local38;
            }
            return;
        }
        @Pc(233) int local233;
        @Pc(247) int local247;
        @Pc(251) int local251;
        @Pc(256) int local256;
        if (arg8 == 1) {
            local154 = local9;
            for (local157 = -arg3; local157 < 0; local157++) {
                local164 = (local11 >> 16) * arg7;
                for (local167 = -arg2; local167 < 0; local167++) {
                    local233 = arg4;
                    if (arg6[(local9 >> 16) + local164] != 0) {
                        local233 = arg5;
                    }
                    local247 = local233 >>> 24;
                    local251 = 255 - local247;
                    local256 = this.anIntArray319[local33];
                    this.anIntArray319[local33++] = ((local233 & 0xFF00FF) * local247 + (local256 & 0xFF00FF) * local251 & 0xFF00FF00) + ((local233 & 0xFF00) * local247 + (local256 & 0xFF00) * local251 & 0xFF0000) >> 8;
                    local9 += local17;
                }
                local11 += local26;
                local9 = local154;
                local33 += local38;
            }
        } else if (arg8 == 2) {
            local154 = local9;
            for (local157 = -arg3; local157 < 0; local157++) {
                local164 = (local11 >> 16) * arg7;
                for (local167 = -arg2; local167 < 0; local167++) {
                    local233 = arg4;
                    if (arg6[(local9 >> 16) + local164] != 0) {
                        local233 = arg5;
                    }
                    if (local233 == 0) {
                        local33++;
                    } else {
                        local247 = this.anIntArray319[local33];
                        local251 = local233 + local247;
                        local256 = (local233 & 0xFF00FF) + (local247 & 0xFF00FF);
                        @Pc(372) int local372 = (local256 & 0x1000100) + (local251 - local256 & 0x10000);
                        this.anIntArray319[local33++] = local251 - local372 | local372 - (local372 >>> 8);
                    }
                    local9 += local17;
                }
                local11 += local26;
                local9 = local154;
                local33 += local38;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!iaa", name = "j", descriptor = "()V")
    @Override
    public void method7950() {
    }

    @OriginalMember(owner = "client!iaa", name = "m", descriptor = "()Z")
    @Override
    public boolean method7970() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIILclient!aa;IIIII)V")
    @Override
    public void method7942(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(6) ClippingMask mask, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7, @OriginalArg(9) int arg8, @OriginalArg(10) int arg9, @OriginalArg(11) int arg10) {
        @Pc(2) ClippingMask_Sub1 local2 = (ClippingMask_Sub1) mask;
        @Pc(5) int[] local5 = local2.anIntArray334;
        @Pc(8) int[] local8 = local2.anIntArray335;
        @Pc(18) int local18 = this.anInt4186 > arg7 ? this.anInt4186 : arg7;
        @Pc(34) int local34 = this.anInt4196 < arg7 + local5.length ? this.anInt4196 : arg7 + local5.length;
        @Pc(38) int local38 = arg10 << 8;
        @Pc(42) int local42 = arg8 << 8;
        @Pc(46) int local46 = arg9 << 8;
        @Pc(50) int local50 = local42 + local46;
        arg10 = local38 % local50;
        x2 -= x1;
        y2 -= y1;
        @Pc(79) int local79;
        @Pc(83) int local83;
        if (x2 + y2 < 0) {
            local79 = (int) (Math.sqrt(x2 * x2 + y2 * y2) * 256.0D);
            local83 = local79 % local50;
            local38 = local50 + local42 - arg10 - local83;
            arg10 = local38 % local50;
            if (arg10 < 0) {
                arg10 += local50;
            }
            x1 += x2;
            x2 = -x2;
            y1 += y2;
            y2 = -y2;
        }
        @Pc(161) int local161;
        @Pc(179) int local179;
        @Pc(183) int local183;
        @Pc(214) int local214;
        @Pc(327) int local327;
        @Pc(346) int local346;
        @Pc(144) int local144;
        @Pc(130) int local130;
        @Pc(283) int local283;
        if (x2 <= y2) {
            x1 <<= 0x10;
            x1 += 32768;
            local144 = x2 << 16;
            local79 = (int) Math.floor((double) local144 / (double) y2 + 0.5D);
            local83 = (int) Math.sqrt((local79 >> 8) * (local79 >> 8) + 65536);
            local130 = y2 + y1;
            local161 = colour >>> 24;
            if (local161 == 255 && true) {
                while (y1 <= local130) {
                    local179 = x1 >> 16;
                    local183 = y1 - arg7;
                    if (y1 >= local18 && y1 < local34 && local179 >= this.anInt4192 && local179 < this.anInt4200 && arg10 < local42 && local179 >= arg6 + local5[local183] && local179 < arg6 + local5[local183] + local8[local183]) {
                        this.anIntArray319[local179 + y1 * this.anInt4207] = colour;
                    }
                    x1 += local79;
                    y1++;
                    local38 = arg10 + local83;
                    arg10 = local38 % local50;
                }
            } else {
                local283 = ((colour & 0xFF00FF) * local161 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local161 >> 8 & 0xFF00) + (local161 << 24);
                local179 = 256 - local161;
                while (y1 <= local130) {
                    local183 = x1 >> 16;
                    local214 = y1 - arg7;
                    if (y1 >= local18 && y1 < local34 && local183 >= this.anInt4192 && local183 < this.anInt4200 && arg10 < local42 && local183 >= arg6 + local5[local214] && local183 < arg6 + local5[local214] + local8[local214]) {
                        local327 = local183 + y1 * this.anInt4207;
                        local346 = this.anIntArray319[local327];
                        @Pc(782) int local782 = ((local346 & 0xFF00FF) * local179 >> 8 & 0xFF00FF) + ((local346 & 0xFF00) * local179 >> 8 & 0xFF00);
                        this.anIntArray319[local183 + y1 * this.anInt4207] = local283 + local782;
                    }
                    x1 += local79;
                    y1++;
                    local38 = arg10 + local83;
                    arg10 = local38 % local50;
                }
            }
            return;
        }
        y1 <<= 0x10;
        y1 += 32768;
        local130 = y2 << 16;
        local79 = (int) Math.floor((double) local130 / (double) x2 + 0.5D);
        local144 = x2 + x1;
        local83 = colour >>> 24;
        local161 = (int) Math.sqrt((local79 >> 8) * (local79 >> 8) + 65536);
        if (local83 == 255 && true) {
            while (x1 <= local144) {
                local179 = y1 >> 16;
                local183 = local179 - arg7;
                if (x1 >= this.anInt4192 && x1 < this.anInt4200 && local179 >= local18 && local179 < local34 && arg10 < local42) {
                    local214 = arg6 + local5[local183];
                    if (x1 >= local214 && x1 < local214 + local8[local183]) {
                        this.anIntArray319[x1 + local179 * this.anInt4207] = colour;
                    }
                }
                y1 += local79;
                x1++;
                local38 = arg10 + local161;
                arg10 = local38 % local50;
            }
            return;
        }
        local283 = ((colour & 0xFF00FF) * local83 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local83 >> 8 & 0xFF00) + (local83 << 24);
        local179 = 256 - local83;
        while (x1 <= local144) {
            local183 = y1 >> 16;
            local214 = local183 - arg7;
            if (x1 >= this.anInt4192 && x1 < this.anInt4200 && local183 >= local18 && local183 < local34 && arg10 < local42) {
                local327 = arg6 + local5[local214];
                if (x1 >= local327 && x1 < local327 + local8[local214]) {
                    local346 = x1 + local183 * this.anInt4207;
                    @Pc(351) int local351 = this.anIntArray319[local346];
                    @Pc(371) int local371 = ((local351 & 0xFF00FF) * local179 >> 8 & 0xFF00FF) + ((local351 & 0xFF00) * local179 >> 8 & 0xFF00);
                    this.anIntArray319[local346] = local283 + local371;
                }
            }
            y1 += local79;
            x1++;
            local38 = arg10 + local161;
            arg10 = local38 % local50;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "p", descriptor = "(I)I")
    public int method3795(@OriginalArg(0) int arg0) {
        return super.textureSource.getMetrics(arg0).alphaBlendMode;
    }

    @OriginalMember(owner = "client!iaa", name = "z", descriptor = "()Z")
    @Override
    public boolean method7990() {
        return true;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!eca;Lclient!wja;)Lclient!gaa;")
    @Override
    public Interface9 method7988(@OriginalArg(0) Surface arg0, @OriginalArg(1) Interface26 arg1) {
        return new Class87(this, (Sprite) arg0, (Class165) arg1);
    }

    @OriginalMember(owner = "client!iaa", name = "e", descriptor = "()I")
    @Override
    public int getMaxLights() {
        return 0;
    }

    @OriginalMember(owner = "client!iaa", name = "t", descriptor = "()Z")
    @Override
    public boolean method8015() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIZ)Lclient!st;")
    @Override
    public Sprite createSprite(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2) {
        return arg2 ? new Sprite_Sub1_Sub2(this, arg0, arg1) : new Sprite_Sub1_Sub1(this, arg0, arg1);
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "()Z")
    @Override
    public boolean method7992() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "v", descriptor = "()V")
    @Override
    public void restoreSurface() {
        if (this.aCanvas3 == null) {
            this.anInt4207 = 1;
            this.anInt4209 = 1;
            this.anIntArray319 = null;
            this.anInt4190 = 1;
            this.anInt4203 = 1;
            this.aFloatArray24 = null;
        } else {
            this.anIntArray319 = this.aClass2_Sub10_1.anIntArray567;
            this.anInt4207 = this.aClass2_Sub10_1.anInt7053;
            this.anInt4209 = this.aClass2_Sub10_1.anInt7050;
            this.aFloatArray24 = this.aFloatArray25;
            this.anInt4190 = this.anInt4197;
            this.anInt4203 = this.anInt4195;
        }
        this.aClass87_1 = null;
        this.method3789();
    }

    @OriginalMember(owner = "client!iaa", name = "L", descriptor = "(III)V")
    @Override
    public void L(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        for (@Pc(1) int local1 = 0; local1 < this.aClass399Array1.length; local1++) {
            @Pc(7) Class399 local7 = this.aClass399Array1[local1];
            local7.anInt10597 = arg0 & 0xFFFFFF;
            @Pc(19) int local19 = local7.anInt10597 >>> 16 & 0xFF;
            if (local19 < 2) {
                local19 = 2;
            }
            @Pc(31) int local31 = local7.anInt10597 >> 8 & 0xFF;
            if (local31 < 2) {
                local31 = 2;
            }
            @Pc(41) int local41 = local7.anInt10597 & 0xFF;
            if (local41 < 2) {
                local41 = 2;
            }
            local7.anInt10597 = local19 << 16 | local31 << 8 | local41;
            if (arg1 < 0) {
                local7.aBoolean806 = false;
            } else {
                local7.aBoolean806 = true;
            }
        }
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(I[Lclient!lca;)V")
    @Override
    public void method8009(@OriginalArg(0) int arg0, @OriginalArg(1) PointLight[] arg1) {
    }

    @OriginalMember(owner = "client!iaa", name = "d", descriptor = "(I)Z")
    public boolean method3796(@OriginalArg(0) int arg0) {
        return super.textureSource.getMetrics(arg0).aBoolean236 || super.textureSource.getMetrics(arg0).aBoolean235;
    }

    @OriginalMember(owner = "client!iaa", name = "I", descriptor = "()I")
    @Override
    public int I() {
        @Pc(2) int local2 = this.anInt4212;
        this.anInt4212 = 0;
        return local2;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(IIIIIIIIII)V")
    public void method3797(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) int arg9) {
        if (arg3 == 0 || arg4 == 0) {
            return;
        }
        if (arg6 == 65535 || super.textureSource.getMetrics(arg6).aBoolean240) {
            this.method3790(arg0, arg1, arg2, arg3, arg7, arg9);
            return;
        }
        if (this.anInt4215 != arg6) {
            @Pc(33) Sprite local33 = (Sprite) this.aReferenceCache_88.get(arg6);
            if (local33 == null) {
                @Pc(39) int[] local39 = this.method3788(arg6);
                if (local39 == null) {
                    return;
                }
                @Pc(53) int local53 = this.method3798(arg6) ? 64 : this.lb;
                local33 = this.createSprite(local53, local53, local53, local39);
                this.aReferenceCache_88.put(local33, arg6);
            }
            this.anInt4215 = arg6;
            this.aSprite_17 = local33;
        }
        ((Sprite_Sub1) this.aSprite_17).method8207(arg0 - arg3, arg1 - arg4, arg2, arg3 << 1, arg4 << 1, arg8, arg7, arg9);
    }

    @OriginalMember(owner = "client!iaa", name = "la", descriptor = "()V")
    @Override
    public void la() {
        this.anInt4192 = 0;
        this.anInt4186 = 0;
        this.anInt4200 = this.anInt4207;
        this.anInt4196 = this.anInt4209;
        this.method3799();
    }

    @OriginalMember(owner = "client!iaa", name = "q", descriptor = "(I)Z")
    public boolean method3798(@OriginalArg(0) int arg0) {
        return this.aBoolean332 || super.textureSource.getMetrics(arg0).small;
    }

    @OriginalMember(owner = "client!iaa", name = "w", descriptor = "()Z")
    @Override
    public boolean method8014() {
        return false;
    }

    @OriginalMember(owner = "client!iaa", name = "C", descriptor = "()V")
    public void method3799() {
        this.anInt4210 = this.anInt4192 - this.anInt4206;
        this.anInt4193 = this.anInt4200 - this.anInt4206;
        this.anInt4208 = this.anInt4186 - this.anInt4194;
        this.anInt4201 = this.anInt4196 - this.anInt4194;
        for (@Pc(29) int local29 = 0; local29 < this.anInt4211; local29++) {
            @Pc(36) Class219 local36 = this.aClass399Array1[local29].aClass219_2;
            local36.anInt5723 = this.anInt4206 - this.anInt4192;
            local36.anInt5721 = this.anInt4194 - this.anInt4186;
            local36.anInt5725 = this.anInt4200 - this.anInt4192;
            local36.anInt5726 = this.anInt4196 - this.anInt4186;
        }
        @Pc(78) int local78 = this.anInt4186 * this.anInt4207 + this.anInt4192;
        for (@Pc(81) int local81 = this.anInt4186; local81 < this.anInt4196; local81++) {
            for (@Pc(84) int local84 = 0; local84 < this.anInt4211; local84++) {
                this.aClass399Array1[local84].aClass219_2.anIntArray439[local81 - this.anInt4186] = local78;
            }
            local78 += this.anInt4207;
        }
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public void method8022(@OriginalArg(0) Canvas arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(8) Node_Sub10 local8 = (Node_Sub10) this.aIterableHashTable_20.get(arg0.hashCode());
        if (local8 == null) {
            local8 = Static538.method7192(arg2, arg0, arg1);
            this.aIterableHashTable_20.put(arg0.hashCode(), local8);
        } else if (local8.anInt7053 != arg1 || local8.anInt7050 != arg2) {
            this.method7935(arg0, arg1, arg2);
        }
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void setCamera(@OriginalArg(0) Matrix matrix) {
        this.aClass73_Sub2_1 = (Matrix_Sub2) matrix;
    }

    @OriginalMember(owner = "client!iaa", name = "na", descriptor = "(IIII)[I")
    @Override
    public int[] na(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(4) int[] local4 = new int[arg2 * arg3];
        @Pc(6) int local6 = 0;
        for (@Pc(8) int local8 = 0; local8 < arg3; local8++) {
            @Pc(18) int local18 = (arg1 + local8) * this.anInt4207 + arg0;
            for (@Pc(20) int local20 = 0; local20 < arg2; local20++) {
                local4[local6++] = this.anIntArray319[local18 + local20];
            }
        }
        return local4;
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(Lclient!lk;)V")
    @Override
    public void render(@OriginalArg(0) ParticleList arg0) {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        @Pc(7) Node2 local7 = arg0.particles.sentinel;
        for (@Pc(10) Node2 local10 = local7.next2; local10 != local7; local10 = local10.next2) {
            @Pc(14) Particle local14 = (Particle) local10;
            @Pc(19) int local19 = local14.anInt7537 >> 12;
            @Pc(24) int local24 = local14.anInt7534 >> 12;
            @Pc(29) int local29 = local14.anInt7536 >> 12;
            @Pc(54) float local54 = this.aClass73_Sub2_1.aFloat62 + this.aClass73_Sub2_1.aFloat56 * (float) local19 + this.aClass73_Sub2_1.aFloat54 * (float) local24 + this.aClass73_Sub2_1.aFloat61 * (float) local29;
            if (!(local54 < (float) this.anInt4214) && !(local54 > (float) local3.anInt10601)) {
                @Pc(105) int local105 = this.anInt4206 + (int) ((float) this.anInt4205 * (this.aClass73_Sub2_1.aFloat60 + this.aClass73_Sub2_1.aFloat59 * (float) local19 + this.aClass73_Sub2_1.aFloat55 * (float) local24 + this.aClass73_Sub2_1.aFloat53 * (float) local29) / local54);
                @Pc(140) int local140 = this.anInt4194 + (int) ((float) this.anInt4188 * (this.aClass73_Sub2_1.aFloat58 + this.aClass73_Sub2_1.aFloat57 * (float) local19 + this.aClass73_Sub2_1.aFloat52 * (float) local24 + this.aClass73_Sub2_1.aFloat51 * (float) local29) / local54);
                if (local105 >= this.anInt4192 && local105 <= this.anInt4200 && local140 >= this.anInt4186 && local140 <= this.anInt4196) {
                    if (local54 == 0.0F) {
                        local54 = 1.0F;
                    }
                    this.method3784(local14, local105, local140, (int) local54, (int) ((float) (local14.anInt7535 * this.anInt4205 >> 12) / local54));
                }
            }
        }
    }

    @OriginalMember(owner = "client!iaa", name = "c", descriptor = "(IIIIII)Lclient!pu;")
    @Override
    public Class67 method8008(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        return null;
    }

    @OriginalMember(owner = "client!iaa", name = "b", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public void method7972(@OriginalArg(0) Canvas arg0) {
        if (this.aCanvas3 == arg0) {
            this.method8019(null);
        }
        @Pc(17) Node_Sub10 local17 = (Node_Sub10) this.aIterableHashTable_20.get(arg0.hashCode());
        if (local17 != null) {
            local17.unlink();
        }
    }

    @OriginalMember(owner = "client!iaa", name = "EA", descriptor = "(IIII)V")
    @Override
    public void EA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(3) Class399 local3 = this.method3787(Thread.currentThread());
        local3.anInt10604 = arg0;
        local3.anInt10597 = arg1;
        local3.anInt10602 = arg2;
    }

    @OriginalMember(owner = "client!iaa", name = "y", descriptor = "()Lclient!tt;")
    @Override
    public Matrix createMatrix() {
        return new Matrix_Sub2();
    }

    @OriginalMember(owner = "client!iaa", name = "a", descriptor = "(I)Lclient!za;")
    @Override
    public Node_Sub13 method7961(@OriginalArg(0) int arg0) {
        return null;
    }
}

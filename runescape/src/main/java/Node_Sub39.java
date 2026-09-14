import com.jagex.core.datastruct.key.Node;
import com.jagex.graphics.TextureMetrics;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeapBuffer;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nm")
public final class Node_Sub39 extends Node {

    @OriginalMember(owner = "client!nm", name = "u", descriptor = "Lclient!ed;")
    public Class94 aClass94_10;

    @OriginalMember(owner = "client!nm", name = "w", descriptor = "Lclient!jaclib/memory/heap/NativeHeapBuffer;")
    public NativeHeapBuffer aNativeHeapBuffer5;

    @OriginalMember(owner = "client!nm", name = "x", descriptor = "Lclient!jaclib/memory/Stream;")
    public Stream aStream5;

    @OriginalMember(owner = "client!nm", name = "z", descriptor = "Lclient!pn;")
    public final Ground_Sub2 aClass178_Sub2_3;

    @OriginalMember(owner = "client!nm", name = "n", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_31;

    @OriginalMember(owner = "client!nm", name = "s", descriptor = "I")
    public final int anInt6595;

    @OriginalMember(owner = "client!nm", name = "E", descriptor = "[I")
    public final int[] anIntArray525;

    @OriginalMember(owner = "client!nm", name = "q", descriptor = "I")
    public final int anInt6593;

    @OriginalMember(owner = "client!nm", name = "G", descriptor = "I")
    public final int anInt6585;

    @OriginalMember(owner = "client!nm", name = "p", descriptor = "I")
    public final int anInt6592;

    @OriginalMember(owner = "client!nm", name = "k", descriptor = "F")
    public final float aFloat121;

    @OriginalMember(owner = "client!nm", name = "C", descriptor = "Lclient!wn;")
    public final Class134_Sub2 aClass134_Sub2_1;

    @OriginalMember(owner = "client!nm", name = "<init>", descriptor = "(Lclient!pn;IIIII)V")
    public Node_Sub39(@OriginalArg(0) Ground_Sub2 arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.aClass178_Sub2_3 = arg0;
        this.aClass19_Sub3_31 = this.aClass178_Sub2_3.aClass19_Sub3_33;
        this.anInt6595 = arg4;
        this.anIntArray525 = new int[this.aClass178_Sub2_3.sizeX * this.aClass178_Sub2_3.sizeZ];
        this.anInt6593 = arg3;
        this.anInt6585 = arg5;
        this.anInt6592 = arg1;
        this.aFloat121 = (float) arg2;
        this.aClass134_Sub2_1 = new Class134_Sub2(this.aClass19_Sub3_31, 5123, null, 1);
    }

    @OriginalMember(owner = "client!nm", name = "a", descriptor = "(I[II)V")
    public void method5861(@OriginalArg(1) int[] arg0, @OriginalArg(2) int arg1) {
        @Pc(7) int local7 = 0;
        @Pc(11) Node_Sub21_Sub1 local11 = this.aClass19_Sub3_31.aClass2_Sub21_Sub1_3;
        local11.pos = 0;
        @Pc(32) short[] local32;
        @Pc(26) int local26;
        @Pc(37) int local37;
        @Pc(20) int local20;
        @Pc(48) int local48;
        @Pc(50) int local50;
        if (this.aClass19_Sub3_31.aBoolean618) {
            for (local20 = 0; local20 < arg1; local20++) {
                local26 = arg0[local20];
                local32 = this.aClass178_Sub2_3.aShortArrayArray6[local26];
                local37 = this.anIntArray525[local26];
                if (local37 != 0 && local32 != null) {
                    local48 = 0;
                    local50 = 0;
                    while (local32.length > local50) {
                        if ((local37 & 0x1 << local48++) == 0) {
                            local50 += 3;
                        } else {
                            local11.p2(local32[local50++] & 0xFFFF);
                            local7++;
                            local7++;
                            local11.p2(local32[local50++] & 0xFFFF);
                            local11.p2(local32[local50++] & 0xFFFF);
                            local7++;
                        }
                    }
                }
            }
        } else {
            for (local20 = 0; local20 < arg1; local20++) {
                local26 = arg0[local20];
                local37 = this.anIntArray525[local26];
                local32 = this.aClass178_Sub2_3.aShortArrayArray6[local26];
                if (local37 != 0 && local32 != null) {
                    local48 = 0;
                    local50 = 0;
                    while (local32.length > local50) {
                        if ((local37 & 0x1 << local48++) == 0) {
                            local50 += 3;
                        } else {
                            local7++;
                            local11.ip2(local32[local50++] & 0xFFFF);
                            local7++;
                            local11.ip2(local32[local50++] & 0xFFFF);
                            local11.ip2(local32[local50++] & 0xFFFF);
                            local7++;
                        }
                    }
                }
            }
        }
        if (local7 <= 0) {
            return;
        }
        this.aClass134_Sub2_1.method9355(local11.pos, local11.data);
        this.aClass19_Sub3_31.method7039(this.aClass94_10, this.aClass178_Sub2_3.aClass94_13, this.aClass178_Sub2_3.aClass94_11, this.aClass178_Sub2_3.aClass94_14);
        this.aClass19_Sub3_31.method7015(this.anInt6592, (this.aClass178_Sub2_3.anInt7486 & 0x7) != 0, (this.aClass178_Sub2_3.anInt7486 & 0x8) != 0);
        if (this.aClass19_Sub3_31.aBoolean605) {
            this.aClass19_Sub3_31.EA(Integer.MAX_VALUE, this.anInt6593, this.anInt6595, this.anInt6585);
        }
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glPushMatrix();
        OpenGL.glScalef(1.0F / this.aFloat121, 1.0F / this.aFloat121, 1.0F);
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        this.aClass19_Sub3_31.method7039(this.aClass94_10, this.aClass178_Sub2_3.aClass94_13, this.aClass178_Sub2_3.aClass94_11, this.aClass178_Sub2_3.aClass94_14);
        this.aClass19_Sub3_31.method7034(0, this.aClass134_Sub2_1, local7);
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glPopMatrix();
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
    }

    @OriginalMember(owner = "client!nm", name = "a", descriptor = "(IIFII)V")
    public void method5863(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) float arg2, @OriginalArg(3) int arg3) {
        @Pc(24) int local24;
        @Pc(49) int local49;
        if (this.anInt6592 != -1) {
            @Pc(19) TextureMetrics local19 = this.aClass19_Sub3_31.textureSource.getMetrics(this.anInt6592);
            local24 = local19.alpha & 0xFF;
            @Pc(68) int local68;
            if (local24 != 0 && local19.effectType != 4) {
                if (arg1 < 0) {
                    local49 = 0;
                } else if (arg1 <= 127) {
                    local49 = arg1 * 131586;
                } else {
                    local49 = 16777215;
                }
                if (local24 == 256) {
                    arg0 = local49;
                } else {
                    local68 = 256 - local24;
                    arg0 = (local24 * (local49 & 0xFF00FF) + local68 * (arg0 & 0xFF00FF) & 0xFF00FF00) + ((arg0 & 0xFF00) * local68 + (local49 & 0xFF00) * local24 & 0xFF0000) >> 8;
                }
            }
            local49 = local19.aByte57 & 0xFF;
            if (local49 != 0) {
                local49 += 256;
                @Pc(122) int local122 = (arg0 >> 16 & 0xFF) * local49;
                if (local122 > 65535) {
                    local122 = 65535;
                }
                local68 = local49 * (arg0 >> 8 & 0xFF);
                if (local68 > 65535) {
                    local68 = 65535;
                }
                @Pc(148) int local148 = local49 * (arg0 & 0xFF);
                if (local148 > 65535) {
                    local148 = 65535;
                }
                arg0 = (local68 & 0xFF00) + (((local122 & 0x5E00FF00) << 8) + (local148 >> 8));
            }
        }
        if (arg2 != 1.0F) {
            @Pc(178) int local178 = arg0 >> 16 & 0xFF;
            local24 = arg0 >> 8 & 0xFF;
            local178 = (int) ((float) local178 * arg2);
            local49 = arg0 & 0xFF;
            if (local178 < 0) {
                local178 = 0;
            } else if (local178 > 255) {
                local178 = 255;
            }
            local24 = (int) ((float) local24 * arg2);
            if (local24 < 0) {
                local24 = 0;
            } else if (local24 > 255) {
                local24 = 255;
            }
            local49 = (int) ((float) local49 * arg2);
            if (local49 < 0) {
                local49 = 0;
            } else if (local49 > 255) {
                local49 = 255;
            }
            arg0 = local49 | local24 << 8 | local178 << 16;
        }
        this.aStream5.f(arg3 * 4);
        this.aStream5.d((byte) (arg0 >> 16));
        this.aStream5.d((byte) (arg0 >> 8));
        this.aStream5.d((byte) arg0);
    }

    @OriginalMember(owner = "client!nm", name = "a", descriptor = "(BI)V")
    public void method5864(@OriginalArg(1) int arg0) {
        this.aStream5.c();
        @Pc(23) Interface12 local23 = this.aClass19_Sub3_31.method7016(4, this.aNativeHeapBuffer5, arg0 * 4);
        this.aClass94_10 = new Class94(local23, 5121, 4, 0);
        this.aStream5 = null;
        this.aNativeHeapBuffer5 = null;
    }

    @OriginalMember(owner = "client!nm", name = "b", descriptor = "(BI)V")
    public void method5866(@OriginalArg(1) int arg0) {
        this.aStream5.f(arg0 * 4 + 3);
        this.aStream5.d(-1);
    }

    @OriginalMember(owner = "client!nm", name = "a", descriptor = "(IIZI)V")
    public void method5867(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2) {
        this.anIntArray525[this.aClass178_Sub2_3.sizeX * arg2 + arg0] |= 0x1 << arg1;
    }

    @OriginalMember(owner = "client!nm", name = "a", descriptor = "(II)V")
    public void method5868(@OriginalArg(1) int arg0) {
        this.aNativeHeapBuffer5 = this.aClass19_Sub3_31.lb.a(arg0 * 4, true);
        this.aStream5 = new Stream(this.aNativeHeapBuffer5);
    }
}

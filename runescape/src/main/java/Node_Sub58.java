import com.jagex.core.datastruct.key.Node;
import com.jagex.graphics.TextureMetrics;
import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeapBuffer;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!wr")
public final class Node_Sub58 extends Node {

    @OriginalMember(owner = "client!wr", name = "o", descriptor = "Lclient!jaclib/memory/heap/NativeHeapBuffer;")
    public NativeHeapBuffer aNativeHeapBuffer8;

    @OriginalMember(owner = "client!wr", name = "z", descriptor = "Lclient!jaclib/memory/Stream;")
    public Stream aStream7;

    @OriginalMember(owner = "client!wr", name = "D", descriptor = "Lclient!mg;")
    public Interface16 anInterface16_18;

    @OriginalMember(owner = "client!wr", name = "v", descriptor = "I")
    public int anInt10871 = 0;

    @OriginalMember(owner = "client!wr", name = "A", descriptor = "Lclient!iga;")
    public final Ground_Sub1 aClass178_Sub1_3;

    @OriginalMember(owner = "client!wr", name = "C", descriptor = "I")
    public final int anInt10863;

    @OriginalMember(owner = "client!wr", name = "H", descriptor = "Lclient!am;")
    public final NativeToolkit aClass19_Sub1_23;

    @OriginalMember(owner = "client!wr", name = "y", descriptor = "F")
    public final float aFloat219;

    @OriginalMember(owner = "client!wr", name = "k", descriptor = "I")
    public final int anInt10860;

    @OriginalMember(owner = "client!wr", name = "w", descriptor = "I")
    public final int anInt10868;

    @OriginalMember(owner = "client!wr", name = "B", descriptor = "I")
    public final int anInt10870;

    @OriginalMember(owner = "client!wr", name = "p", descriptor = "[I")
    public final int[] anIntArray884;

    @OriginalMember(owner = "client!wr", name = "<init>", descriptor = "(Lclient!iga;IIIII)V")
    public Node_Sub58(@OriginalArg(0) Ground_Sub1 arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.aClass178_Sub1_3 = arg0;
        this.anInt10863 = arg3;
        this.aClass19_Sub1_23 = this.aClass178_Sub1_3.aClass19_Sub1_9;
        this.aFloat219 = (float) arg2;
        this.anInt10860 = arg4;
        this.anInt10868 = arg5;
        this.anInt10870 = arg1;
        this.anIntArray884 = new int[this.aClass178_Sub1_3.sizeZ * this.aClass178_Sub1_3.sizeX];
    }

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(II)V")
    public void method9395(@OriginalArg(0) int arg0) {
        this.aStream7.f(arg0 * 4 + 3);
        this.aStream7.d(-1);
    }

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(BI)V")
    public void method9399(@OriginalArg(1) int arg0) {
        this.aNativeHeapBuffer8 = this.aClass19_Sub1_23.method8106(true, arg0 * 4);
        this.aStream7 = new Stream(this.aNativeHeapBuffer8, 0, arg0 * 4);
    }

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(B[II)V")
    public void method9400(@OriginalArg(1) int[] arg0, @OriginalArg(2) int arg1) {
        @Pc(14) Interface20 local14 = this.aClass19_Sub1_23.method8074(this.anInt10871 * 3);
        @Pc(19) Buffer local19 = local14.method8547();
        if (local19 == null) {
            return;
        }
        @Pc(29) Stream local29 = this.aClass19_Sub1_23.method8100(local19);
        @Pc(31) int local31 = 0;
        @Pc(33) int local33 = 32767;
        @Pc(35) int local35 = -32768;
        @Pc(98) int local98;
        @Pc(63) short[] local63;
        @Pc(52) int local52;
        @Pc(57) int local57;
        @Pc(46) int local46;
        @Pc(71) int local71;
        @Pc(73) int local73;
        @Pc(89) int local89;
        if (Stream.b()) {
            label95:
            for (local46 = 0; local46 < arg1; local46++) {
                local52 = arg0[local46];
                local63 = this.aClass178_Sub1_3.aShortArrayArray4[local52];
                local57 = this.anIntArray884[local52];
                if (local57 != 0 && local63 != null) {
                    local71 = 0;
                    local73 = 0;
                    while (true) {
                        while (true) {
                            if (local73 >= local63.length) {
                                continue label95;
                            }
                            if ((local57 & 0x1 << local71++) == 0) {
                                local73 += 3;
                            } else {
                                local31++;
                                for (local89 = 0; local89 < 3; local89++) {
                                    local98 = local63[local73++] & 0xFFFF;
                                    local29.b(local98);
                                    if (local98 > local35) {
                                        local35 = local98;
                                    }
                                    if (local33 > local98) {
                                        local33 = local98;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            label121:
            for (local46 = 0; local46 < arg1; local46++) {
                local52 = arg0[local46];
                local57 = this.anIntArray884[local52];
                local63 = this.aClass178_Sub1_3.aShortArrayArray4[local52];
                if (local57 != 0 && local63 != null) {
                    local71 = 0;
                    local73 = 0;
                    while (true) {
                        while (true) {
                            if (local73 >= local63.length) {
                                continue label121;
                            }
                            if ((0x1 << local71++ & local57) == 0) {
                                local73 += 3;
                            } else {
                                for (local89 = 0; local89 < 3; local89++) {
                                    local98 = local63[local73++] & 0xFFFF;
                                    if (local98 < local33) {
                                        local33 = local98;
                                    }
                                    if (local35 < local98) {
                                        local35 = local98;
                                    }
                                    local29.c(local98);
                                }
                                local31++;
                            }
                        }
                    }
                }
            }
        }
        local29.c();
        if (!local14.method8546() || local31 <= 0) {
            return;
        }
        this.aClass19_Sub1_23.method8108((this.aClass178_Sub1_3.anInt4302 & 0x7) != 0, this.anInt10870, (this.aClass178_Sub1_3.anInt4302 & 0x8) != 0);
        if (this.aClass19_Sub1_23.aBoolean681) {
            this.aClass19_Sub1_23.EA(Integer.MAX_VALUE, this.anInt10863, this.anInt10860, this.anInt10868);
        }
        @Pc(315) Matrix_Sub1 local315 = this.aClass19_Sub1_23.method8082();
        local315.method1884(1.0F, 1.0F / this.aFloat219, 1.0F / this.aFloat219);
        this.aClass19_Sub1_23.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_23.method8130(1, this.anInterface16_18);
        this.aClass19_Sub1_23.method8114(this.aClass178_Sub1_3.aClass244_8);
        this.aClass19_Sub1_23.method8052(local33, 0, local14, local35 + 1 - local33, Static104.aClass131_2, local31);
        this.aClass19_Sub1_23.method8031();
    }

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(IIII)V")
    public void method9401(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        this.anIntArray884[this.aClass178_Sub1_3.sizeX * arg2 + arg0] |= 0x1 << arg1;
        this.anInt10871++;
    }

    @OriginalMember(owner = "client!wr", name = "b", descriptor = "(BI)V")
    public void method9403(@OriginalArg(1) int arg0) {
        this.aStream7.c();
        this.anInterface16_18 = this.aClass19_Sub1_23.method8156(false);
        this.anInterface16_18.method3152(4, arg0 * 4, this.aNativeHeapBuffer8);
        this.aNativeHeapBuffer8 = null;
        this.aStream7 = null;
    }

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(IIFII)V")
    public void method9404(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) float arg2, @OriginalArg(4) int arg3) {
        @Pc(31) int local31;
        @Pc(57) int local57;
        if (this.anInt10870 != -1) {
            @Pc(26) TextureMetrics local26 = this.aClass19_Sub1_23.textureSource.getMetrics(this.anInt10870);
            local31 = local26.alpha & 0xFF;
            @Pc(83) int local83;
            if (local31 != 0 && local26.effectType != 4) {
                if (arg1 < 0) {
                    local57 = 0;
                } else if (arg1 > 127) {
                    local57 = 16777215;
                } else {
                    local57 = arg1 * 131586;
                }
                if (local31 == 256) {
                    arg0 = local57;
                } else {
                    local83 = 256 - local31;
                    arg0 = (local31 * (local57 & 0xFF00) + local83 * (arg0 & 0xFF00) & 0xFF0000) + (local31 * (local57 & 0xFF00FF) + ((arg0 & 0xFF00FF) * local83) & 0xFF00FF00) >> 8;
                }
            }
            local57 = local26.aByte57 & 0xFF;
            if (local57 != 0) {
                local57 += 256;
                @Pc(133) int local133 = (arg0 >> 16 & 0xFF) * local57;
                if (local133 > 65535) {
                    local133 = 65535;
                }
                local83 = local57 * (arg0 >> 8 & 0xFF);
                if (local83 > 65535) {
                    local83 = 65535;
                }
                @Pc(159) int local159 = (arg0 & 0xFF) * local57;
                if (local159 > 65535) {
                    local159 = 65535;
                }
                arg0 = (local159 >> 8) + (local83 & 0xFF00) + ((local133 & 0x1600FF00) << 8);
            }
        }
        this.aStream7.f(arg3 * 4);
        if (arg2 != 1.0F) {
            @Pc(196) int local196 = arg0 >> 16 & 0xFF;
            local31 = arg0 >> 8 & 0xFF;
            local196 = (int) ((float) local196 * arg2);
            local57 = arg0 & 0xFF;
            if (local196 < 0) {
                local196 = 0;
            } else if (local196 > 255) {
                local196 = 255;
            }
            local31 = (int) ((float) local31 * arg2);
            local57 = (int) ((float) local57 * arg2);
            if (local31 < 0) {
                local31 = 0;
            } else if (local31 > 255) {
                local31 = 255;
            }
            if (local57 < 0) {
                local57 = 0;
            } else if (local57 > 255) {
                local57 = 255;
            }
            arg0 = local57 | local31 << 8 | local196 << 16;
        }
        if (this.aClass19_Sub1_23.anInt9178 == 0) {
            this.aStream7.d((byte) arg0);
            this.aStream7.d((byte) (arg0 >> 8));
            this.aStream7.d((byte) (arg0 >> 16));
        } else {
            this.aStream7.d((byte) (arg0 >> 16));
            this.aStream7.d((byte) (arg0 >> 8));
            this.aStream7.d((byte) arg0);
        }
    }
}

import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!oh")
public final class Class274 {

    @OriginalMember(owner = "client!oh", name = "o", descriptor = "Z")
    public boolean aBoolean520 = true;

    @OriginalMember(owner = "client!oh", name = "k", descriptor = "I")
    public int anInt6927 = -1;

    @OriginalMember(owner = "client!oh", name = "d", descriptor = "I")
    public final int anInt6925;

    @OriginalMember(owner = "client!oh", name = "a", descriptor = "Lclient!am;")
    public final NativeToolkit aClass19_Sub1_13;

    @OriginalMember(owner = "client!oh", name = "g", descriptor = "I")
    public final int anInt6926;

    @OriginalMember(owner = "client!oh", name = "m", descriptor = "Lclient!pk;")
    public Class293 aClass293_2;

    @OriginalMember(owner = "client!oh", name = "i", descriptor = "I")
    public int anInt6919;

    @OriginalMember(owner = "client!oh", name = "b", descriptor = "I")
    public int anInt6920;

    @OriginalMember(owner = "client!oh", name = "j", descriptor = "Lclient!ri;")
    public Interface20 anInterface20_4;

    @OriginalMember(owner = "client!oh", name = "f", descriptor = "I")
    public final int anInt6928;

    @OriginalMember(owner = "client!oh", name = "n", descriptor = "Lclient!og;")
    public Interface18 anInterface18_4;

    @OriginalMember(owner = "client!oh", name = "<init>", descriptor = "(Lclient!am;Lclient!pk;Lclient!iga;IIIII)V")
    public Class274(@OriginalArg(0) NativeToolkit arg0, @OriginalArg(1) Class293 arg1, @OriginalArg(2) Ground_Sub1 arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7) {
        this.anInt6925 = arg7;
        this.aClass19_Sub1_13 = arg0;
        this.anInt6926 = arg6;
        this.aClass293_2 = arg1;
        @Pc(25) int local25 = 0x1 << arg5;
        @Pc(27) int local27 = 0;
        @Pc(31) int local31 = arg3 << arg5;
        @Pc(35) int local35 = arg4 << arg5;
        @Pc(50) int local50;
        for (@Pc(37) int local37 = 0; local37 < local25; local37++) {
            local50 = local31 + (local37 + local35) * arg2.sizeX;
            for (@Pc(52) int local52 = 0; local52 < local25; local52++) {
                @Pc(60) short[] local60 = arg2.aShortArrayArray4[local50++];
                if (local60 != null) {
                    local27 += local60.length;
                }
            }
        }
        if (local27 <= 0) {
            this.anInterface18_4 = null;
            this.anInt6928 = 0;
        } else {
            this.anInt6919 = Integer.MIN_VALUE;
            this.anInt6920 = Integer.MAX_VALUE;
            this.anInterface20_4 = this.aClass19_Sub1_13.method8122(false);
            this.anInterface20_4.method8543(local27);
            for (local50 = 0; local50 < 4; local50++) {
                @Pc(120) Buffer local120 = this.anInterface20_4.method8547();
                if (local120 != null) {
                    @Pc(128) Stream local128 = this.aClass19_Sub1_13.method8100(local120);
                    @Pc(132) int local132;
                    @Pc(144) int local144;
                    @Pc(146) int local146;
                    @Pc(154) short[] local154;
                    @Pc(158) int local158;
                    @Pc(166) int local166;
                    if (Stream.b()) {
                        for (local132 = 0; local132 < local25; local132++) {
                            local144 = arg2.sizeX * (local132 + local35) + local31;
                            for (local146 = 0; local146 < local25; local146++) {
                                local154 = arg2.aShortArrayArray4[local144++];
                                if (local154 != null) {
                                    for (local158 = 0; local158 < local154.length; local158++) {
                                        local166 = local154[local158] & 0xFFFF;
                                        if (local166 < this.anInt6920) {
                                            this.anInt6920 = local166;
                                        }
                                        if (this.anInt6919 < local166) {
                                            this.anInt6919 = local166;
                                        }
                                        local128.b(local166);
                                    }
                                }
                            }
                        }
                    } else {
                        for (local132 = 0; local132 < local25; local132++) {
                            local144 = (local132 + local35) * arg2.sizeX + local31;
                            for (local146 = 0; local146 < local25; local146++) {
                                local154 = arg2.aShortArrayArray4[local144++];
                                if (local154 != null) {
                                    for (local158 = 0; local158 < local154.length; local158++) {
                                        local166 = local154[local158] & 0xFFFF;
                                        if (local166 > this.anInt6919) {
                                            this.anInt6919 = local166;
                                        }
                                        if (local166 < this.anInt6920) {
                                            this.anInt6920 = local166;
                                        }
                                        local128.c(local166);
                                    }
                                }
                            }
                        }
                    }
                    local128.c();
                    if (this.anInterface20_4.method8546()) {
                        break;
                    }
                }
            }
            this.anInt6928 = local27 / 3;
        }
    }

    @OriginalMember(owner = "client!oh", name = "a", descriptor = "(ILclient!ri;I)V")
    public void method6226(@OriginalArg(1) Interface20 arg0, @OriginalArg(2) int arg1) {
        if (arg1 > 0) {
            this.method6227();
            this.aClass19_Sub1_13.method8088(this.anInterface18_4);
            this.aClass19_Sub1_13.method8052(this.anInt6920, 0, arg0, this.anInt6919 + 1 - this.anInt6920, Static104.aClass131_2, arg1);
        }
    }

    @OriginalMember(owner = "client!oh", name = "a", descriptor = "(B)V")
    public void method6227() {
        if (!this.aBoolean520) {
            return;
        }
        this.aBoolean520 = false;
        @Pc(17) byte[] local17 = this.aClass293_2.aByteArray87;
        @Pc(19) int local19 = 0;
        @Pc(23) int local23 = this.aClass293_2.anInt7428;
        @Pc(33) int local33 = this.anInt6926 + this.anInt6925 * this.aClass293_2.anInt7428;
        @Pc(47) int local47;
        for (@Pc(35) int local35 = -128; local35 < 0; local35++) {
            local19 = (local19 << 8) - local19;
            for (local47 = -128; local47 < 0; local47++) {
                if (local17[local33++] != 0) {
                    local19++;
                }
            }
            local33 += local23 - 128;
        }
        if (108 != 108) {
            this.aClass293_2 = null;
        }
        if (this.anInterface18_4 != null && local19 == this.anInt6927) {
            this.aBoolean520 = false;
            return;
        }
        this.anInt6927 = local19;
        local47 = 0;
        local33 = this.anInt6925 * local23 + this.anInt6926;
        @Pc(142) int local142;
        @Pc(146) int local146;
        @Pc(168) int local168;
        if (!this.aClass19_Sub1_13.method8071(Static679.aClass92_15, Static702.aClass397_16)) {
            if (Static42.anIntArray69 == null) {
                Static42.anIntArray69 = new int[16384];
            }
            @Pc(140) int[] local140 = Static42.anIntArray69;
            for (local142 = -128; local142 < 0; local142++) {
                for (local146 = -128; local146 < 0; local146++) {
                    if (local17[local33] == 0) {
                        local168 = 0;
                        if (local17[local33 - 1] != 0) {
                            local168++;
                        }
                        if (local17[local33 + 1] != 0) {
                            local168++;
                        }
                        if (local17[local33 - local23] != 0) {
                            local168++;
                        }
                        if (local17[local33 + local23] != 0) {
                            local168++;
                        }
                        local140[local47++] = local168 * 17 << 24;
                    } else {
                        local140[local47++] = 1140850688;
                    }
                    local33++;
                }
                local33 += this.aClass293_2.anInt7428 - 128;
            }
            if (this.anInterface18_4 == null) {
                this.anInterface18_4 = this.aClass19_Sub1_13.method8034(false, 128, 128, Static42.anIntArray69);
                this.anInterface18_4.method9052(false, false);
            } else {
                this.anInterface18_4.method9048(Static42.anIntArray69, 0, 0, 128, 128, 128);
            }
            return;
        }
        if (Static369.aByteArray43 == null) {
            Static369.aByteArray43 = new byte[16384];
        }
        @Pc(282) byte[] local282 = Static369.aByteArray43;
        for (local142 = -128; local142 < 0; local142++) {
            for (local146 = -128; local146 < 0; local146++) {
                if (local17[local33] == 0) {
                    local168 = 0;
                    if (local17[local33 - 1] != 0) {
                        local168++;
                    }
                    if (local17[local33 + 1] != 0) {
                        local168++;
                    }
                    if (local17[local33 - local23] != 0) {
                        local168++;
                    }
                    if (local17[local33 + local23] != 0) {
                        local168++;
                    }
                    local282[local47++] = (byte) (local168 * 17);
                } else {
                    local282[local47++] = 68;
                }
                local33++;
            }
            local33 += this.aClass293_2.anInt7428 - 128;
        }
        if (this.anInterface18_4 == null) {
            this.anInterface18_4 = this.aClass19_Sub1_13.method8028(128, Static369.aByteArray43, Static679.aClass92_15, 128, false);
            this.anInterface18_4.method9052(false, false);
        } else {
            this.anInterface18_4.method9051(128, 128, Static369.aByteArray43, Static679.aClass92_15, 128);
        }
    }

    @OriginalMember(owner = "client!oh", name = "b", descriptor = "(I)V")
    public void method6229() {
        this.method6226(this.anInterface20_4, this.anInt6928);
    }
}

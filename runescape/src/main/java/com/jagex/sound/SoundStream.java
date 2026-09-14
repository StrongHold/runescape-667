package com.jagex.sound;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!haa")
public final class SoundStream extends AudioBuss {

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(Lclient!sq;III)Lclient!haa;")
    public static SoundStream create(@OriginalArg(0) VariableRateSoundPacket packet, @OriginalArg(1) int rate, @OriginalArg(2) int volume, @OriginalArg(3) int range) {
        return packet.data == null || packet.data.length == 0 ? null : new SoundStream(packet, rate, volume, range);
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "([B[IIIIIIILclient!haa;)I")
    public static int method3310(@OriginalArg(0) byte[] arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) SoundStream arg7) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg6 >> 8;
        @Pc(11) int local11 = arg4 << 2;
        @Pc(18) int local18;
        if ((local18 = arg3 + local7 - arg2) > arg5) {
            local18 = arg5;
        }
        local18 -= 3;
        @Pc(26) int local26;
        while (arg3 < local18) {
            local26 = arg3++;
            @Pc(29) int local29 = arg1[local26];
            @Pc(32) int local32 = arg2 + 1;
            arg1[local26] = local29 + arg0[arg2] * local11;
            @Pc(39) int local39 = arg3++;
            @Pc(42) int local42 = arg1[local39];
            @Pc(45) int local45 = local32 + 1;
            arg1[local39] = local42 + arg0[local32] * local11;
            @Pc(52) int local52 = arg3++;
            @Pc(55) int local55 = arg1[local52];
            @Pc(58) int local58 = local45 + 1;
            arg1[local52] = local55 + arg0[local45] * local11;
            @Pc(65) int local65 = arg3++;
            @Pc(68) int local68 = arg1[local65];
            arg2 = local58 + 1;
            arg1[local65] = local68 + arg0[local58] * local11;
        }
        local18 += 3;
        while (arg3 < local18) {
            local26 = arg3++;
            arg1[local26] += arg0[arg2++] * local11;
        }
        arg7.anInt3686 = arg2 << 8;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int method3312(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(7) int arg5, @OriginalArg(9) int arg6, @OriginalArg(10) int arg7, @OriginalArg(11) SoundStream arg8, @OriginalArg(12) int arg9, @OriginalArg(13) int arg10) {
        @Pc(14) int local14;
        if (arg9 == 0 || (local14 = arg3 + (arg7 + arg9 - arg2 - 257) / arg9) > arg6) {
            local14 = arg6;
        }
        arg3 <<= 0x1;
        local14 <<= 0x1;
        @Pc(35) byte local35;
        @Pc(54) int local54;
        @Pc(51) int local51;
        @Pc(53) int local53;
        while (arg3 < local14) {
            @Pc(31) int local31 = arg2 >> 8;
            local35 = arg0[local31];
            local51 = (local35 << 8) + (arg0[local31 + 1] - local35) * (arg2 & 0xFF);
            local53 = arg3;
            local54 = arg3 + 1;
            arg1[local53] += local51 * arg4 >> 6;
            @Pc(65) int local65 = local54;
            arg3 = local54 + 1;
            arg1[local65] += local51 * arg5 >> 6;
            arg2 += arg9;
        }
        if (arg9 == 0 || (local14 = (arg3 >> 1) + (arg7 + arg9 - arg2 - 1) / arg9) > arg6) {
            local14 = arg6;
        }
        local14 <<= 0x1;
        while (arg3 < local14) {
            local35 = arg0[arg2 >> 8];
            local51 = (local35 << 8) + (arg10 - local35) * (arg2 & 0xFF);
            local53 = arg3;
            local54 = arg3 + 1;
            arg1[local53] += local51 * arg4 >> 6;
            local53 = local54;
            arg3 = local54 + 1;
            arg1[local53] += local51 * arg5 >> 6;
            arg2 += arg9;
        }
        arg8.anInt3686 = arg2;
        return arg3 >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II[B[IIIIIIIIIILclient!haa;II)I")
    public static int method3313(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(7) int arg5, @OriginalArg(8) int arg6, @OriginalArg(9) int arg7, @OriginalArg(11) int arg8, @OriginalArg(12) int arg9, @OriginalArg(13) SoundStream arg10, @OriginalArg(14) int arg11, @OriginalArg(15) int arg12) {
        arg10.anInt3676 -= arg10.anInt3678 * arg3;
        @Pc(23) int local23;
        if (arg11 == 0 || (local23 = arg3 + (arg9 + arg11 - arg2 - 257) / arg11) > arg8) {
            local23 = arg8;
        }
        arg3 <<= 0x1;
        local23 <<= 0x1;
        @Pc(44) byte local44;
        @Pc(63) int local63;
        @Pc(60) int local60;
        @Pc(62) int local62;
        while (arg3 < local23) {
            @Pc(40) int local40 = arg2 >> 8;
            local44 = arg0[local40];
            local60 = (local44 << 8) + (arg0[local40 + 1] - local44) * (arg2 & 0xFF);
            local62 = arg3;
            local63 = arg3 + 1;
            arg1[local62] += local60 * arg4 >> 6;
            arg4 += arg6;
            @Pc(78) int local78 = local63;
            arg3 = local63 + 1;
            arg1[local78] += local60 * arg5 >> 6;
            arg5 += arg7;
            arg2 += arg11;
        }
        if (arg11 == 0 || (local23 = (arg3 >> 1) + (arg9 + arg11 - arg2 - 1) / arg11) > arg8) {
            local23 = arg8;
        }
        local23 <<= 0x1;
        while (arg3 < local23) {
            local44 = arg0[arg2 >> 8];
            local60 = (local44 << 8) + (arg12 - local44) * (arg2 & 0xFF);
            local62 = arg3;
            local63 = arg3 + 1;
            arg1[local62] += local60 * arg4 >> 6;
            arg4 += arg6;
            local62 = local63;
            arg3 = local63 + 1;
            arg1[local62] += local60 * arg5 >> 6;
            arg5 += arg7;
            arg2 += arg11;
        }
        local63 = arg3 >> 1;
        arg10.anInt3676 += arg10.anInt3678 * local63;
        arg10.anInt3680 = arg4;
        arg10.anInt3677 = arg5;
        arg10.anInt3686 = arg2;
        return local63;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II[B[IIIIIIILclient!haa;II)I")
    public static int method3316(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(8) int arg5, @OriginalArg(9) int arg6, @OriginalArg(10) SoundStream arg7, @OriginalArg(11) int arg8, @OriginalArg(12) int arg9) {
        @Pc(14) int local14;
        if (arg8 == 0 || (local14 = arg3 + (arg6 + arg8 - arg2 - 257) / arg8) > arg5) {
            local14 = arg5;
        }
        @Pc(27) byte local27;
        @Pc(29) int local29;
        while (arg3 < local14) {
            @Pc(23) int local23 = arg2 >> 8;
            local27 = arg0[local23];
            local29 = arg3++;
            arg1[local29] += ((local27 << 8) + (arg0[local23 + 1] - local27) * (arg2 & 0xFF)) * arg4 >> 6;
            arg2 += arg8;
        }
        if (arg8 == 0 || (local14 = arg3 + (arg6 + arg8 - arg2 - 1) / arg8) > arg5) {
            local14 = arg5;
        }
        while (arg3 < local14) {
            local27 = arg0[arg2 >> 8];
            local29 = arg3++;
            arg1[local29] += ((local27 << 8) + (arg9 - local27) * (arg2 & 0xFF)) * arg4 >> 6;
            arg2 += arg8;
        }
        arg7.anInt3686 = arg2;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([B[IIIIIIIILclient!haa;)I")
    public static int method3321(@OriginalArg(0) byte[] arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7, @OriginalArg(9) SoundStream arg8) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg7 >> 8;
        arg4 <<= 0x2;
        @Pc(15) int local15 = arg5 << 2;
        @Pc(22) int local22;
        if ((local22 = arg3 + local7 - arg2) > arg6) {
            local22 = arg6;
        }
        arg8.anInt3680 += arg8.anInt3683 * (local22 - arg3);
        arg8.anInt3677 += arg8.anInt3687 * (local22 - arg3);
        local22 -= 3;
        @Pc(52) int local52;
        while (arg3 < local22) {
            local52 = arg3++;
            @Pc(55) int local55 = arg1[local52];
            @Pc(58) int local58 = arg2 + 1;
            arg1[local52] = local55 + arg0[arg2] * arg4;
            @Pc(67) int local67 = arg4 + local15;
            @Pc(69) int local69 = arg3++;
            @Pc(72) int local72 = arg1[local69];
            @Pc(75) int local75 = local58 + 1;
            arg1[local69] = local72 + arg0[local58] * local67;
            @Pc(84) int local84 = local67 + local15;
            @Pc(86) int local86 = arg3++;
            @Pc(89) int local89 = arg1[local86];
            @Pc(92) int local92 = local75 + 1;
            arg1[local86] = local89 + arg0[local75] * local84;
            @Pc(101) int local101 = local84 + local15;
            @Pc(103) int local103 = arg3++;
            @Pc(106) int local106 = arg1[local103];
            arg2 = local92 + 1;
            arg1[local103] = local106 + arg0[local92] * local101;
            arg4 = local101 + local15;
        }
        local22 += 3;
        while (arg3 < local22) {
            local52 = arg3++;
            arg1[local52] += arg0[arg2++] * arg4;
            arg4 += local15;
        }
        arg8.anInt3676 = arg4 >> 2;
        arg8.anInt3686 = arg2 << 8;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "c", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int method3324(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(7) int arg5, @OriginalArg(9) int arg6, @OriginalArg(10) int arg7, @OriginalArg(11) SoundStream arg8, @OriginalArg(12) int arg9, @OriginalArg(13) int arg10) {
        @Pc(14) int local14;
        if (arg9 == 0 || (local14 = arg3 + (arg7 + arg9 + 256 - arg2) / arg9) > arg6) {
            local14 = arg6;
        }
        arg3 <<= 0x1;
        local14 <<= 0x1;
        @Pc(54) int local54;
        @Pc(51) int local51;
        @Pc(53) int local53;
        while (arg3 < local14) {
            @Pc(31) int local31 = arg2 >> 8;
            @Pc(37) byte local37 = arg0[local31 - 1];
            local51 = (local37 << 8) + (arg0[local31] - local37) * (arg2 & 0xFF);
            local53 = arg3;
            local54 = arg3 + 1;
            arg1[local53] += local51 * arg4 >> 6;
            @Pc(65) int local65 = local54;
            arg3 = local54 + 1;
            arg1[local65] += local51 * arg5 >> 6;
            arg2 += arg9;
        }
        if (arg9 == 0 || (local14 = (arg3 >> 1) + (arg7 + arg9 - arg2) / arg9) > arg6) {
            local14 = arg6;
        }
        local14 <<= 0x1;
        while (arg3 < local14) {
            local51 = (arg10 << 8) + (arg0[arg2 >> 8] - arg10) * (arg2 & 0xFF);
            local53 = arg3;
            local54 = arg3 + 1;
            arg1[local53] += local51 * arg4 >> 6;
            local53 = local54;
            arg3 = local54 + 1;
            arg1[local53] += local51 * arg5 >> 6;
            arg2 += arg9;
        }
        arg8.anInt3686 = arg2;
        return arg3 >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(I[B[IIIIIIIIIILclient!haa;)I")
    public static int method3325(@OriginalArg(1) byte[] arg0, @OriginalArg(2) int[] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7, @OriginalArg(10) int arg8, @OriginalArg(11) int arg9, @OriginalArg(12) SoundStream arg10) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg9 >> 8;
        arg4 <<= 0x2;
        arg5 <<= 0x2;
        @Pc(19) int local19 = arg6 << 2;
        @Pc(23) int local23 = arg7 << 2;
        @Pc(30) int local30;
        if ((local30 = arg3 + local7 - arg2) > arg8) {
            local30 = arg8;
        }
        arg10.anInt3676 += arg10.anInt3678 * (local30 - arg3);
        arg3 <<= 0x1;
        local30 <<= 0x1;
        local30 -= 6;
        @Pc(60) byte local60;
        @Pc(63) int local63;
        while (arg3 < local30) {
            @Pc(58) int local58 = arg2 + 1;
            local60 = arg0[arg2];
            local63 = arg3 + 1;
            arg1[arg3] += local60 * arg4;
            @Pc(74) int local74 = arg4 + local19;
            @Pc(77) int local77 = local63 + 1;
            arg1[local63] += local60 * arg5;
            @Pc(88) int local88 = arg5 + local23;
            @Pc(91) int local91 = local58 + 1;
            @Pc(93) byte local93 = arg0[local58];
            @Pc(96) int local96 = local77 + 1;
            arg1[local77] += local93 * local74;
            @Pc(107) int local107 = local74 + local19;
            @Pc(110) int local110 = local96 + 1;
            arg1[local96] += local93 * local88;
            @Pc(121) int local121 = local88 + local23;
            @Pc(124) int local124 = local91 + 1;
            @Pc(126) byte local126 = arg0[local91];
            @Pc(129) int local129 = local110 + 1;
            arg1[local110] += local126 * local107;
            @Pc(140) int local140 = local107 + local19;
            @Pc(143) int local143 = local129 + 1;
            arg1[local129] += local126 * local121;
            @Pc(154) int local154 = local121 + local23;
            arg2 = local124 + 1;
            @Pc(159) byte local159 = arg0[local124];
            @Pc(162) int local162 = local143 + 1;
            arg1[local143] += local159 * local140;
            arg4 = local140 + local19;
            arg3 = local162 + 1;
            arg1[local162] += local159 * local154;
            arg5 = local154 + local23;
        }
        local30 += 6;
        while (arg3 < local30) {
            local60 = arg0[arg2++];
            local63 = arg3 + 1;
            arg1[arg3] += local60 * arg4;
            arg4 += local19;
            arg3 = local63 + 1;
            arg1[local63] += local60 * arg5;
            arg5 += local23;
        }
        arg10.anInt3680 = arg4 >> 2;
        arg10.anInt3677 = arg5 >> 2;
        arg10.anInt3686 = arg2 << 8;
        return arg3 >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II[B[IIIIIIIIIILclient!haa;II)I")
    public static int method3326(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(7) int arg5, @OriginalArg(8) int arg6, @OriginalArg(9) int arg7, @OriginalArg(11) int arg8, @OriginalArg(12) int arg9, @OriginalArg(13) SoundStream arg10, @OriginalArg(14) int arg11, @OriginalArg(15) int arg12) {
        arg10.anInt3676 -= arg10.anInt3678 * arg3;
        @Pc(23) int local23;
        if (arg11 == 0 || (local23 = arg3 + (arg9 + arg11 + 256 - arg2) / arg11) > arg8) {
            local23 = arg8;
        }
        arg3 <<= 0x1;
        local23 <<= 0x1;
        @Pc(63) int local63;
        @Pc(60) int local60;
        @Pc(62) int local62;
        while (arg3 < local23) {
            @Pc(40) int local40 = arg2 >> 8;
            @Pc(46) byte local46 = arg0[local40 - 1];
            local60 = (local46 << 8) + (arg0[local40] - local46) * (arg2 & 0xFF);
            local62 = arg3;
            local63 = arg3 + 1;
            arg1[local62] += local60 * arg4 >> 6;
            arg4 += arg6;
            @Pc(78) int local78 = local63;
            arg3 = local63 + 1;
            arg1[local78] += local60 * arg5 >> 6;
            arg5 += arg7;
            arg2 += arg11;
        }
        if (arg11 == 0 || (local23 = (arg3 >> 1) + (arg9 + arg11 - arg2) / arg11) > arg8) {
            local23 = arg8;
        }
        local23 <<= 0x1;
        while (arg3 < local23) {
            local60 = (arg12 << 8) + (arg0[arg2 >> 8] - arg12) * (arg2 & 0xFF);
            local62 = arg3;
            local63 = arg3 + 1;
            arg1[local62] += local60 * arg4 >> 6;
            arg4 += arg6;
            local62 = local63;
            arg3 = local63 + 1;
            arg1[local62] += local60 * arg5 >> 6;
            arg5 += arg7;
            arg2 += arg11;
        }
        local63 = arg3 >> 1;
        arg10.anInt3676 += arg10.anInt3678 * local63;
        arg10.anInt3680 = arg4;
        arg10.anInt3677 = arg5;
        arg10.anInt3686 = arg2;
        return local63;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II)I")
    public static int method3328(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return arg1 < 0 ? -arg0 : (int) ((double) arg0 * Math.sqrt((double) arg1 * 1.220703125E-4D) + 0.5D);
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(I[B[IIIIIIIILclient!haa;)I")
    public static int method3329(@OriginalArg(1) byte[] arg0, @OriginalArg(2) int[] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(8) int arg6, @OriginalArg(9) int arg7, @OriginalArg(10) SoundStream arg8) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg7 >> 8;
        @Pc(11) int local11 = arg4 << 2;
        @Pc(15) int local15 = arg5 << 2;
        @Pc(22) int local22;
        if ((local22 = arg3 + local7 - arg2) > arg6) {
            local22 = arg6;
        }
        arg3 <<= 0x1;
        local22 <<= 0x1;
        local22 -= 6;
        @Pc(41) byte local41;
        @Pc(44) int local44;
        while (arg3 < local22) {
            @Pc(39) int local39 = arg2 + 1;
            local41 = arg0[arg2];
            local44 = arg3 + 1;
            arg1[arg3] += local41 * local11;
            @Pc(54) int local54 = local44 + 1;
            arg1[local44] += local41 * local15;
            @Pc(64) int local64 = local39 + 1;
            @Pc(66) byte local66 = arg0[local39];
            @Pc(69) int local69 = local54 + 1;
            arg1[local54] += local66 * local11;
            @Pc(79) int local79 = local69 + 1;
            arg1[local69] += local66 * local15;
            @Pc(89) int local89 = local64 + 1;
            @Pc(91) byte local91 = arg0[local64];
            @Pc(94) int local94 = local79 + 1;
            arg1[local79] += local91 * local11;
            @Pc(104) int local104 = local94 + 1;
            arg1[local94] += local91 * local15;
            arg2 = local89 + 1;
            @Pc(116) byte local116 = arg0[local89];
            @Pc(119) int local119 = local104 + 1;
            arg1[local104] += local116 * local11;
            arg3 = local119 + 1;
            arg1[local119] += local116 * local15;
        }
        local22 += 6;
        while (arg3 < local22) {
            local41 = arg0[arg2++];
            local44 = arg3 + 1;
            arg1[arg3] += local41 * local11;
            arg3 = local44 + 1;
            arg1[local44] += local41 * local15;
        }
        arg8.anInt3686 = arg2 << 8;
        return arg3 >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II)I")
    public static int method3330(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return arg1 < 0 ? arg0 : (int) ((double) arg0 * Math.sqrt((double) (16384 - arg1) * 1.220703125E-4D) + 0.5D);
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int method3331(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(7) int arg5, @OriginalArg(9) int arg6, @OriginalArg(10) int arg7, @OriginalArg(11) SoundStream arg8, @OriginalArg(12) int arg9, @OriginalArg(13) int arg10) {
        arg8.anInt3680 -= arg8.anInt3683 * arg3;
        arg8.anInt3677 -= arg8.anInt3687 * arg3;
        @Pc(32) int local32;
        if (arg9 == 0 || (local32 = arg3 + (arg7 + arg9 - arg2 - 257) / arg9) > arg6) {
            local32 = arg6;
        }
        @Pc(45) byte local45;
        @Pc(47) int local47;
        while (arg3 < local32) {
            @Pc(41) int local41 = arg2 >> 8;
            local45 = arg0[local41];
            local47 = arg3++;
            arg1[local47] += ((local45 << 8) + (arg0[local41 + 1] - local45) * (arg2 & 0xFF)) * arg4 >> 6;
            arg4 += arg5;
            arg2 += arg9;
        }
        if (arg9 == 0 || (local32 = arg3 + (arg7 + arg9 - arg2 - 1) / arg9) > arg6) {
            local32 = arg6;
        }
        while (arg3 < local32) {
            local45 = arg0[arg2 >> 8];
            local47 = arg3++;
            arg1[local47] += ((local45 << 8) + (arg10 - local45) * (arg2 & 0xFF)) * arg4 >> 6;
            arg4 += arg5;
            arg2 += arg9;
        }
        arg8.anInt3680 += arg8.anInt3683 * arg3;
        arg8.anInt3677 += arg8.anInt3687 * arg3;
        arg8.anInt3676 = arg4;
        arg8.anInt3686 = arg2;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "([B[IIIIIIIILclient!haa;)I")
    public static int method3332(@OriginalArg(0) byte[] arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7, @OriginalArg(9) SoundStream arg8) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg7 >> 8;
        arg4 <<= 0x2;
        @Pc(15) int local15 = arg5 << 2;
        @Pc(24) int local24;
        if ((local24 = arg3 + arg2 + 1 - local7) > arg6) {
            local24 = arg6;
        }
        arg8.anInt3680 += arg8.anInt3683 * (local24 - arg3);
        arg8.anInt3677 += arg8.anInt3687 * (local24 - arg3);
        local24 -= 3;
        @Pc(54) int local54;
        while (arg3 < local24) {
            local54 = arg3++;
            @Pc(57) int local57 = arg1[local54];
            @Pc(60) int local60 = arg2 - 1;
            arg1[local54] = local57 + arg0[arg2] * arg4;
            @Pc(69) int local69 = arg4 + local15;
            @Pc(71) int local71 = arg3++;
            @Pc(74) int local74 = arg1[local71];
            @Pc(77) int local77 = local60 - 1;
            arg1[local71] = local74 + arg0[local60] * local69;
            @Pc(86) int local86 = local69 + local15;
            @Pc(88) int local88 = arg3++;
            @Pc(91) int local91 = arg1[local88];
            @Pc(94) int local94 = local77 - 1;
            arg1[local88] = local91 + arg0[local77] * local86;
            @Pc(103) int local103 = local86 + local15;
            @Pc(105) int local105 = arg3++;
            @Pc(108) int local108 = arg1[local105];
            arg2 = local94 - 1;
            arg1[local105] = local108 + arg0[local94] * local103;
            arg4 = local103 + local15;
        }
        local24 += 3;
        while (arg3 < local24) {
            local54 = arg3++;
            arg1[local54] += arg0[arg2--] * arg4;
            arg4 += local15;
        }
        arg8.anInt3676 = arg4 >> 2;
        arg8.anInt3686 = arg2 << 8;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(I[B[IIIIIIIILclient!haa;)I")
    public static int method3334(@OriginalArg(1) byte[] arg0, @OriginalArg(2) int[] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(8) int arg6, @OriginalArg(9) int arg7, @OriginalArg(10) SoundStream arg8) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg7 >> 8;
        @Pc(11) int local11 = arg4 << 2;
        @Pc(15) int local15 = arg5 << 2;
        @Pc(24) int local24;
        if ((local24 = arg3 + arg2 + 1 - local7) > arg6) {
            local24 = arg6;
        }
        arg3 <<= 0x1;
        local24 <<= 0x1;
        local24 -= 6;
        @Pc(43) byte local43;
        @Pc(46) int local46;
        while (arg3 < local24) {
            @Pc(41) int local41 = arg2 - 1;
            local43 = arg0[arg2];
            local46 = arg3 + 1;
            arg1[arg3] += local43 * local11;
            @Pc(56) int local56 = local46 + 1;
            arg1[local46] += local43 * local15;
            @Pc(66) int local66 = local41 - 1;
            @Pc(68) byte local68 = arg0[local41];
            @Pc(71) int local71 = local56 + 1;
            arg1[local56] += local68 * local11;
            @Pc(81) int local81 = local71 + 1;
            arg1[local71] += local68 * local15;
            @Pc(91) int local91 = local66 - 1;
            @Pc(93) byte local93 = arg0[local66];
            @Pc(96) int local96 = local81 + 1;
            arg1[local81] += local93 * local11;
            @Pc(106) int local106 = local96 + 1;
            arg1[local96] += local93 * local15;
            arg2 = local91 - 1;
            @Pc(118) byte local118 = arg0[local91];
            @Pc(121) int local121 = local106 + 1;
            arg1[local106] += local118 * local11;
            arg3 = local121 + 1;
            arg1[local121] += local118 * local15;
        }
        local24 += 6;
        while (arg3 < local24) {
            local43 = arg0[arg2--];
            local46 = arg3 + 1;
            arg1[arg3] += local43 * local11;
            arg3 = local46 + 1;
            arg1[local46] += local43 * local15;
        }
        arg8.anInt3686 = arg2 << 8;
        return arg3 >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II[B[IIIIIIILclient!haa;II)I")
    public static int method3339(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(8) int arg5, @OriginalArg(9) int arg6, @OriginalArg(10) SoundStream arg7, @OriginalArg(11) int arg8, @OriginalArg(12) int arg9) {
        @Pc(14) int local14;
        if (arg8 == 0 || (local14 = arg3 + (arg6 + arg8 + 256 - arg2) / arg8) > arg5) {
            local14 = arg5;
        }
        @Pc(31) int local31;
        while (arg3 < local14) {
            @Pc(23) int local23 = arg2 >> 8;
            @Pc(29) byte local29 = arg0[local23 - 1];
            local31 = arg3++;
            arg1[local31] += ((local29 << 8) + (arg0[local23] - local29) * (arg2 & 0xFF)) * arg4 >> 6;
            arg2 += arg8;
        }
        if (arg8 == 0 || (local14 = arg3 + (arg6 + arg8 - arg2) / arg8) > arg5) {
            local14 = arg5;
        }
        while (arg3 < local14) {
            local31 = arg3++;
            arg1[local31] += ((arg9 << 8) + (arg0[arg2 >> 8] - arg9) * (arg2 & 0xFF)) * arg4 >> 6;
            arg2 += arg8;
        }
        arg7.anInt3686 = arg2;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(I[B[IIIIIIIIIILclient!haa;)I")
    public static int method3342(@OriginalArg(1) byte[] arg0, @OriginalArg(2) int[] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7, @OriginalArg(10) int arg8, @OriginalArg(11) int arg9, @OriginalArg(12) SoundStream arg10) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg9 >> 8;
        arg4 <<= 0x2;
        arg5 <<= 0x2;
        @Pc(19) int local19 = arg6 << 2;
        @Pc(23) int local23 = arg7 << 2;
        @Pc(32) int local32;
        if ((local32 = arg3 + arg2 + 1 - local7) > arg8) {
            local32 = arg8;
        }
        arg10.anInt3676 += arg10.anInt3678 * (local32 - arg3);
        arg3 <<= 0x1;
        local32 <<= 0x1;
        local32 -= 6;
        @Pc(62) byte local62;
        @Pc(65) int local65;
        while (arg3 < local32) {
            @Pc(60) int local60 = arg2 - 1;
            local62 = arg0[arg2];
            local65 = arg3 + 1;
            arg1[arg3] += local62 * arg4;
            @Pc(76) int local76 = arg4 + local19;
            @Pc(79) int local79 = local65 + 1;
            arg1[local65] += local62 * arg5;
            @Pc(90) int local90 = arg5 + local23;
            @Pc(93) int local93 = local60 - 1;
            @Pc(95) byte local95 = arg0[local60];
            @Pc(98) int local98 = local79 + 1;
            arg1[local79] += local95 * local76;
            @Pc(109) int local109 = local76 + local19;
            @Pc(112) int local112 = local98 + 1;
            arg1[local98] += local95 * local90;
            @Pc(123) int local123 = local90 + local23;
            @Pc(126) int local126 = local93 - 1;
            @Pc(128) byte local128 = arg0[local93];
            @Pc(131) int local131 = local112 + 1;
            arg1[local112] += local128 * local109;
            @Pc(142) int local142 = local109 + local19;
            @Pc(145) int local145 = local131 + 1;
            arg1[local131] += local128 * local123;
            @Pc(156) int local156 = local123 + local23;
            arg2 = local126 - 1;
            @Pc(161) byte local161 = arg0[local126];
            @Pc(164) int local164 = local145 + 1;
            arg1[local145] += local161 * local142;
            arg4 = local142 + local19;
            arg3 = local164 + 1;
            arg1[local164] += local161 * local156;
            arg5 = local156 + local23;
        }
        local32 += 6;
        while (arg3 < local32) {
            local62 = arg0[arg2--];
            local65 = arg3 + 1;
            arg1[arg3] += local62 * arg4;
            arg4 += local19;
            arg3 = local65 + 1;
            arg1[local65] += local62 * arg5;
            arg5 += local23;
        }
        arg10.anInt3680 = arg4 >> 2;
        arg10.anInt3677 = arg5 >> 2;
        arg10.anInt3686 = arg2 << 8;
        return arg3 >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([B[IIIIIIILclient!haa;)I")
    public static int method3343(@OriginalArg(0) byte[] arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) SoundStream arg7) {
        arg2 >>= 0x8;
        @Pc(7) int local7 = arg6 >> 8;
        @Pc(11) int local11 = arg4 << 2;
        @Pc(20) int local20;
        if ((local20 = arg3 + arg2 + 1 - local7) > arg5) {
            local20 = arg5;
        }
        local20 -= 3;
        @Pc(28) int local28;
        while (arg3 < local20) {
            local28 = arg3++;
            @Pc(31) int local31 = arg1[local28];
            @Pc(34) int local34 = arg2 - 1;
            arg1[local28] = local31 + arg0[arg2] * local11;
            @Pc(41) int local41 = arg3++;
            @Pc(44) int local44 = arg1[local41];
            @Pc(47) int local47 = local34 - 1;
            arg1[local41] = local44 + arg0[local34] * local11;
            @Pc(54) int local54 = arg3++;
            @Pc(57) int local57 = arg1[local54];
            @Pc(60) int local60 = local47 - 1;
            arg1[local54] = local57 + arg0[local47] * local11;
            @Pc(67) int local67 = arg3++;
            @Pc(70) int local70 = arg1[local67];
            arg2 = local60 - 1;
            arg1[local67] = local70 + arg0[local60] * local11;
        }
        local20 += 3;
        while (arg3 < local20) {
            local28 = arg3++;
            arg1[local28] += arg0[arg2--] * local11;
        }
        arg7.anInt3686 = arg2 << 8;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "d", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int method3347(@OriginalArg(2) byte[] arg0, @OriginalArg(3) int[] arg1, @OriginalArg(4) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(7) int arg5, @OriginalArg(9) int arg6, @OriginalArg(10) int arg7, @OriginalArg(11) SoundStream arg8, @OriginalArg(12) int arg9, @OriginalArg(13) int arg10) {
        arg8.anInt3680 -= arg8.anInt3683 * arg3;
        arg8.anInt3677 -= arg8.anInt3687 * arg3;
        @Pc(32) int local32;
        if (arg9 == 0 || (local32 = arg3 + (arg7 + arg9 + 256 - arg2) / arg9) > arg6) {
            local32 = arg6;
        }
        @Pc(49) int local49;
        while (arg3 < local32) {
            @Pc(41) int local41 = arg2 >> 8;
            @Pc(47) byte local47 = arg0[local41 - 1];
            local49 = arg3++;
            arg1[local49] += ((local47 << 8) + (arg0[local41] - local47) * (arg2 & 0xFF)) * arg4 >> 6;
            arg4 += arg5;
            arg2 += arg9;
        }
        if (arg9 == 0 || (local32 = arg3 + (arg7 + arg9 - arg2) / arg9) > arg6) {
            local32 = arg6;
        }
        while (arg3 < local32) {
            local49 = arg3++;
            arg1[local49] += ((arg10 << 8) + (arg0[arg2 >> 8] - arg10) * (arg2 & 0xFF)) * arg4 >> 6;
            arg4 += arg5;
            arg2 += arg9;
        }
        arg8.anInt3680 += arg8.anInt3683 * arg3;
        arg8.anInt3677 += arg8.anInt3687 * arg3;
        arg8.anInt3676 = arg4;
        arg8.anInt3686 = arg2;
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "A", descriptor = "I")
    public int anInt3676;

    @OriginalMember(owner = "client!haa", name = "w", descriptor = "I")
    public int anInt3677;

    @OriginalMember(owner = "client!haa", name = "C", descriptor = "I")
    public int anInt3678;

    @OriginalMember(owner = "client!haa", name = "s", descriptor = "I")
    public int anInt3680;

    @OriginalMember(owner = "client!haa", name = "r", descriptor = "I")
    public int anInt3683;

    @OriginalMember(owner = "client!haa", name = "q", descriptor = "I")
    public int anInt3684;

    @OriginalMember(owner = "client!haa", name = "p", descriptor = "I")
    public int anInt3687;

    @OriginalMember(owner = "client!haa", name = "B", descriptor = "I")
    public int loops;

    @OriginalMember(owner = "client!haa", name = "z", descriptor = "I")
    public final int nominalBitRate;

    @OriginalMember(owner = "client!haa", name = "o", descriptor = "I")
    public final int minBitRate;

    @OriginalMember(owner = "client!haa", name = "v", descriptor = "Z")
    public final boolean aBoolean281;

    @OriginalMember(owner = "client!haa", name = "u", descriptor = "I")
    public int rate;

    @OriginalMember(owner = "client!haa", name = "x", descriptor = "I")
    public int volume;

    @OriginalMember(owner = "client!haa", name = "t", descriptor = "I")
    public int range;

    @OriginalMember(owner = "client!haa", name = "y", descriptor = "I")
    public int anInt3686;

    @OriginalMember(owner = "client!haa", name = "<init>", descriptor = "(Lclient!sq;III)V")
    public SoundStream(@OriginalArg(0) VariableRateSoundPacket packet, @OriginalArg(1) int rate, @OriginalArg(2) int volume, @OriginalArg(3) int range) {
        super.aClass2_Sub49_6 = packet;
        this.nominalBitRate = packet.nominalBitRate;
        this.minBitRate = packet.minBitRate;
        this.aBoolean281 = packet.aBoolean668;
        this.rate = rate;
        this.volume = volume;
        this.range = range;
        this.anInt3686 = 0;
        this.method3314();
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "([IIIII)I")
    public int method3309(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4) {
        do {
            if (this.anInt3684 <= 0) {
                if (this.rate == -256 && (this.anInt3686 & 0xFF) == 0) {
                    if (QueueBuss.stereo) {
                        return method3334(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, arg3, arg2, this);
                    }
                    return method3343(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, arg3, arg2, this);
                }
                if (QueueBuss.stereo) {
                    return method3324(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, arg3, arg2, this, this.rate, arg4);
                }
                return method3339(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, arg3, arg2, this, this.rate, arg4);
            }
            @Pc(5) int local5 = arg1 + this.anInt3684;
            if (local5 > arg3) {
                local5 = arg3;
            }
            this.anInt3684 += arg1;
            if (this.rate == -256 && (this.anInt3686 & 0xFF) == 0) {
                if (QueueBuss.stereo) {
                    arg1 = method3342(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, this.anInt3683, this.anInt3687, local5, arg2, this);
                } else {
                    arg1 = method3332(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, this.anInt3678, local5, arg2, this);
                }
            } else if (QueueBuss.stereo) {
                arg1 = method3326(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, this.anInt3683, this.anInt3687, local5, arg2, this, this.rate, arg4);
            } else {
                arg1 = method3347(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, this.anInt3678, local5, arg2, this, this.rate, arg4);
            }
            this.anInt3684 -= arg1;
            if (this.anInt3684 != 0) {
                return arg1;
            }
        } while (!this.method3345());
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([III)V")
    @Override
    public synchronized void fill(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (this.volume == 0 && this.anInt3684 == 0) {
            this.skip(arg2);
            return;
        }
        @Pc(13) VariableRateSoundPacket local13 = (VariableRateSoundPacket) super.aClass2_Sub49_6;
        @Pc(18) int local18 = this.nominalBitRate << 8;
        @Pc(23) int local23 = this.minBitRate << 8;
        @Pc(29) int local29 = local13.data.length << 8;
        @Pc(33) int local33 = local23 - local18;
        if (local33 <= 0) {
            this.loops = 0;
        }
        @Pc(40) int local40 = arg1;
        @Pc(44) int local44 = arg2 + arg1;
        if (this.anInt3686 < 0) {
            if (this.rate <= 0) {
                this.method3335();
                this.unlink();
                return;
            }
            this.anInt3686 = 0;
        }
        if (this.anInt3686 >= local29) {
            if (this.rate >= 0) {
                this.method3335();
                this.unlink();
                return;
            }
            this.anInt3686 = local29 - 1;
        }
        if (this.loops >= 0) {
            if (this.loops > 0) {
                if (this.aBoolean281) {
                    label130:
                    {
                        if (this.rate < 0) {
                            local40 = this.method3309(arg0, arg1, local18, local44, local13.data[this.nominalBitRate]);
                            if (this.anInt3686 >= local18) {
                                return;
                            }
                            this.anInt3686 = local18 + local18 - this.anInt3686 - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break label130;
                            }
                        }
                        do {
                            local40 = this.method3317(arg0, local40, local23, local44, local13.data[this.minBitRate - 1]);
                            if (this.anInt3686 < local23) {
                                return;
                            }
                            this.anInt3686 = local23 + local23 - this.anInt3686 - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break;
                            }
                            local40 = this.method3309(arg0, local40, local18, local44, local13.data[this.nominalBitRate]);
                            if (this.anInt3686 >= local18) {
                                return;
                            }
                            this.anInt3686 = local18 + local18 - this.anInt3686 - 1;
                            this.rate = -this.rate;
                        } while (--this.loops != 0);
                    }
                } else {
                    @Pc(416) int local416;
                    if (this.rate < 0) {
                        while (true) {
                            local40 = this.method3309(arg0, local40, local18, local44, local13.data[this.minBitRate - 1]);
                            if (this.anInt3686 >= local18) {
                                return;
                            }
                            local416 = (local23 - this.anInt3686 - 1) / local33;
                            if (local416 >= this.loops) {
                                this.anInt3686 += local33 * this.loops;
                                this.loops = 0;
                                break;
                            }
                            this.anInt3686 += local33 * local416;
                            this.loops -= local416;
                        }
                    } else {
                        while (true) {
                            local40 = this.method3317(arg0, local40, local23, local44, local13.data[this.nominalBitRate]);
                            if (this.anInt3686 < local23) {
                                return;
                            }
                            local416 = (this.anInt3686 - local18) / local33;
                            if (local416 >= this.loops) {
                                this.anInt3686 -= local33 * this.loops;
                                this.loops = 0;
                                break;
                            }
                            this.anInt3686 -= local33 * local416;
                            this.loops -= local416;
                        }
                    }
                }
            }
            if (this.rate < 0) {
                this.method3309(arg0, local40, 0, local44, 0);
                if (this.anInt3686 < 0) {
                    this.anInt3686 = -1;
                    this.method3335();
                    this.unlink();
                    return;
                }
            } else {
                this.method3317(arg0, local40, local29, local44, 0);
                if (this.anInt3686 >= local29) {
                    this.anInt3686 = local29;
                    this.method3335();
                    this.unlink();
                }
            }
        } else if (this.aBoolean281) {
            if (this.rate < 0) {
                local40 = this.method3309(arg0, arg1, local18, local44, local13.data[this.nominalBitRate]);
                if (this.anInt3686 >= local18) {
                    return;
                }
                this.anInt3686 = local18 + local18 - this.anInt3686 - 1;
                this.rate = -this.rate;
            }
            while (true) {
                local40 = this.method3317(arg0, local40, local23, local44, local13.data[this.minBitRate - 1]);
                if (this.anInt3686 < local23) {
                    return;
                }
                this.anInt3686 = local23 + local23 - this.anInt3686 - 1;
                this.rate = -this.rate;
                local40 = this.method3309(arg0, local40, local18, local44, local13.data[this.nominalBitRate]);
                if (this.anInt3686 >= local18) {
                    return;
                }
                this.anInt3686 = local18 + local18 - this.anInt3686 - 1;
                this.rate = -this.rate;
            }
        } else if (this.rate < 0) {
            while (true) {
                local40 = this.method3309(arg0, local40, local18, local44, local13.data[this.minBitRate - 1]);
                if (this.anInt3686 >= local18) {
                    return;
                }
                this.anInt3686 = local23 - (local23 - 1 - this.anInt3686) % local33 - 1;
            }
        } else {
            while (true) {
                local40 = this.method3317(arg0, local40, local23, local44, local13.data[this.nominalBitRate]);
                if (this.anInt3686 < local23) {
                    return;
                }
                this.anInt3686 = local18 + (this.anInt3686 - local18) % local33;
            }
        }
    }

    @OriginalMember(owner = "client!haa", name = "e", descriptor = "()Z")
    public boolean method3311() {
        return this.anInt3686 < 0 || this.anInt3686 >= ((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length << 8;
    }

    @OriginalMember(owner = "client!haa", name = "j", descriptor = "()V")
    public void method3314() {
        this.anInt3676 = this.volume;
        this.anInt3680 = method3330(this.volume, this.range);
        this.anInt3677 = method3328(this.volume, this.range);
    }

    @OriginalMember(owner = "client!haa", name = "d", descriptor = "(II)V")
    public synchronized void method3315(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        this.method3338(arg0, arg1, this.getRange());
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([IIIII)I")
    public int method3317(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4) {
        do {
            if (this.anInt3684 <= 0) {
                if (this.rate == 256 && (this.anInt3686 & 0xFF) == 0) {
                    if (QueueBuss.stereo) {
                        return method3329(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, arg3, arg2, this);
                    }
                    return method3310(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, arg3, arg2, this);
                }
                if (QueueBuss.stereo) {
                    return method3312(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, arg3, arg2, this, this.rate, arg4);
                }
                return method3316(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, arg3, arg2, this, this.rate, arg4);
            }
            @Pc(5) int local5 = arg1 + this.anInt3684;
            if (local5 > arg3) {
                local5 = arg3;
            }
            this.anInt3684 += arg1;
            if (this.rate == 256 && (this.anInt3686 & 0xFF) == 0) {
                if (QueueBuss.stereo) {
                    arg1 = method3325(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, this.anInt3683, this.anInt3687, local5, arg2, this);
                } else {
                    arg1 = method3321(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, this.anInt3678, local5, arg2, this);
                }
            } else if (QueueBuss.stereo) {
                arg1 = method3313(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3680, this.anInt3677, this.anInt3683, this.anInt3687, local5, arg2, this, this.rate, arg4);
            } else {
                arg1 = method3331(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, arg0, this.anInt3686, arg1, this.anInt3676, this.anInt3678, local5, arg2, this, this.rate, arg4);
            }
            this.anInt3684 -= arg1;
            if (this.anInt3684 != 0) {
                return arg1;
            }
        } while (!this.method3345());
        return arg3;
    }

    @OriginalMember(owner = "client!haa", name = "j", descriptor = "(I)V")
    public synchronized void setLoops(@OriginalArg(0) int loops) {
        this.loops = loops;
    }

    @OriginalMember(owner = "client!haa", name = "i", descriptor = "(I)V")
    public synchronized void setRate(@OriginalArg(0) int rate) {
        if (this.rate < 0) {
            this.rate = -rate;
        } else {
            this.rate = rate;
        }
    }

    @OriginalMember(owner = "client!haa", name = "c", descriptor = "(I)V")
    public synchronized void method3320(@OriginalArg(0) int arg0) {
        if (arg0 == 0) {
            this.method3322();
            this.unlink();
        } else if (this.anInt3680 == 0 && this.anInt3677 == 0) {
            this.anInt3684 = 0;
            this.volume = 0;
            this.anInt3676 = 0;
            this.unlink();
        } else {
            @Pc(31) int local31 = -this.anInt3676;
            if (this.anInt3676 > local31) {
                local31 = this.anInt3676;
            }
            if (-this.anInt3680 > local31) {
                local31 = -this.anInt3680;
            }
            if (this.anInt3680 > local31) {
                local31 = this.anInt3680;
            }
            if (-this.anInt3677 > local31) {
                local31 = -this.anInt3677;
            }
            if (this.anInt3677 > local31) {
                local31 = this.anInt3677;
            }
            if (arg0 > local31) {
                arg0 = local31;
            }
            this.anInt3684 = arg0;
            this.volume = Integer.MIN_VALUE;
            this.anInt3678 = -this.anInt3676 / arg0;
            this.anInt3683 = -this.anInt3680 / arg0;
            this.anInt3687 = -this.anInt3677 / arg0;
        }
    }

    @OriginalMember(owner = "client!haa", name = "h", descriptor = "(I)V")
    public synchronized void method3322() {
        this.update(0, this.getRange());
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(Z)V")
    public synchronized void method3323() {
        this.rate = (this.rate ^ this.rate >> 31) + (this.rate >>> 31);
        this.rate = -this.rate;
    }

    @OriginalMember(owner = "client!haa", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss method9133() {
        return null;
    }

    @OriginalMember(owner = "client!haa", name = "g", descriptor = "(I)V")
    public synchronized void setVolume(@OriginalArg(0) int volume) {
        this.update(volume << 6, this.getRange());
    }

    @OriginalMember(owner = "client!haa", name = "d", descriptor = "(I)V")
    public synchronized void setRange(@OriginalArg(0) int range) {
        this.update(this.getVolume(), range);
    }

    @OriginalMember(owner = "client!haa", name = "i", descriptor = "()V")
    public void method3335() {
        if (this.anInt3684 == 0) {
            return;
        }
        if (this.volume == Integer.MIN_VALUE) {
            this.volume = 0;
        }
        this.anInt3684 = 0;
        this.method3314();
    }

    @OriginalMember(owner = "client!haa", name = "f", descriptor = "()Z")
    public boolean method3336() {
        return this.anInt3684 != 0;
    }

    @OriginalMember(owner = "client!haa", name = "c", descriptor = "(II)V")
    public synchronized void update(@OriginalArg(0) int volume, @OriginalArg(1) int range) {
        this.volume = volume;
        this.range = range;
        this.anInt3684 = 0;
        this.method3314();
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(III)V")
    public synchronized void method3338(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (arg0 == 0) {
            this.update(arg1, arg2);
            return;
        }
        @Pc(10) int local10 = method3330(arg1, arg2);
        @Pc(14) int local14 = method3328(arg1, arg2);
        if (this.anInt3680 == local10 && this.anInt3677 == local14) {
            this.anInt3684 = 0;
            return;
        }
        @Pc(31) int local31 = arg1 - this.anInt3676;
        if (this.anInt3676 - arg1 > local31) {
            local31 = this.anInt3676 - arg1;
        }
        if (local10 - this.anInt3680 > local31) {
            local31 = local10 - this.anInt3680;
        }
        if (this.anInt3680 - local10 > local31) {
            local31 = this.anInt3680 - local10;
        }
        if (local14 - this.anInt3677 > local31) {
            local31 = local14 - this.anInt3677;
        }
        if (this.anInt3677 - local14 > local31) {
            local31 = this.anInt3677 - local14;
        }
        if (arg0 > local31) {
            arg0 = local31;
        }
        this.anInt3684 = arg0;
        this.volume = arg1;
        this.range = arg2;
        this.anInt3678 = (arg1 - this.anInt3676) / arg0;
        this.anInt3683 = (local10 - this.anInt3680) / arg0;
        this.anInt3687 = (local14 - this.anInt3677) / arg0;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "()I")
    @Override
    public int method9132() {
        return this.volume == 0 && this.anInt3684 == 0 ? 0 : 1;
    }

    @OriginalMember(owner = "client!haa", name = "l", descriptor = "()I")
    public synchronized int method3340() {
        return this.rate < 0 ? -this.rate : this.rate;
    }

    @OriginalMember(owner = "client!haa", name = "h", descriptor = "()I")
    public synchronized int getVolume() {
        return this.volume == Integer.MIN_VALUE ? 0 : this.volume;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(I)V")
    @Override
    public synchronized void skip(@OriginalArg(0) int arg0) {
        if (this.anInt3684 > 0) {
            if (arg0 >= this.anInt3684) {
                if (this.volume == Integer.MIN_VALUE) {
                    this.volume = 0;
                    this.anInt3676 = this.anInt3680 = this.anInt3677 = 0;
                    this.unlink();
                    arg0 = this.anInt3684;
                }
                this.anInt3684 = 0;
                this.method3314();
            } else {
                this.anInt3676 += this.anInt3678 * arg0;
                this.anInt3680 += this.anInt3683 * arg0;
                this.anInt3677 += this.anInt3687 * arg0;
                this.anInt3684 -= arg0;
            }
        }
        @Pc(71) VariableRateSoundPacket local71 = (VariableRateSoundPacket) super.aClass2_Sub49_6;
        @Pc(76) int local76 = this.nominalBitRate << 8;
        @Pc(81) int local81 = this.minBitRate << 8;
        @Pc(87) int local87 = local71.data.length << 8;
        @Pc(91) int local91 = local81 - local76;
        if (local91 <= 0) {
            this.loops = 0;
        }
        if (this.anInt3686 < 0) {
            if (this.rate <= 0) {
                this.method3335();
                this.unlink();
                return;
            }
            this.anInt3686 = 0;
        }
        if (this.anInt3686 >= local87) {
            if (this.rate >= 0) {
                this.method3335();
                this.unlink();
                return;
            }
            this.anInt3686 = local87 - 1;
        }
        this.anInt3686 += this.rate * arg0;
        if (this.loops >= 0) {
            if (this.loops > 0) {
                if (this.aBoolean281) {
                    label125:
                    {
                        if (this.rate < 0) {
                            if (this.anInt3686 >= local76) {
                                return;
                            }
                            this.anInt3686 = local76 + local76 - this.anInt3686 - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break label125;
                            }
                        }
                        do {
                            if (this.anInt3686 < local81) {
                                return;
                            }
                            this.anInt3686 = local81 + local81 - this.anInt3686 - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break;
                            }
                            if (this.anInt3686 >= local76) {
                                return;
                            }
                            this.anInt3686 = local76 + local76 - this.anInt3686 - 1;
                            this.rate = -this.rate;
                        } while (--this.loops != 0);
                    }
                } else {
                    @Pc(361) int local361;
                    if (this.rate < 0) {
                        if (this.anInt3686 >= local76) {
                            return;
                        }
                        local361 = (local81 - this.anInt3686 - 1) / local91;
                        if (local361 < this.loops) {
                            this.anInt3686 += local91 * local361;
                            this.loops -= local361;
                            return;
                        }
                        this.anInt3686 += local91 * this.loops;
                        this.loops = 0;
                    } else if (this.anInt3686 >= local81) {
                        local361 = (this.anInt3686 - local76) / local91;
                        if (local361 < this.loops) {
                            this.anInt3686 -= local91 * local361;
                            this.loops -= local361;
                            return;
                        }
                        this.anInt3686 -= local91 * this.loops;
                        this.loops = 0;
                    } else {
                        return;
                    }
                }
            }
            if (this.rate < 0) {
                if (this.anInt3686 < 0) {
                    this.anInt3686 = -1;
                    this.method3335();
                    this.unlink();
                    return;
                }
            } else if (this.anInt3686 >= local87) {
                this.anInt3686 = local87;
                this.method3335();
                this.unlink();
            }
        } else if (this.aBoolean281) {
            if (this.rate < 0) {
                if (this.anInt3686 >= local76) {
                    return;
                }
                this.anInt3686 = local76 + local76 - this.anInt3686 - 1;
                this.rate = -this.rate;
            }
            while (this.anInt3686 >= local81) {
                this.anInt3686 = local81 + local81 - this.anInt3686 - 1;
                this.rate = -this.rate;
                if (this.anInt3686 >= local76) {
                    return;
                }
                this.anInt3686 = local76 + local76 - this.anInt3686 - 1;
                this.rate = -this.rate;
            }
        } else if (this.rate < 0) {
            if (this.anInt3686 < local76) {
                this.anInt3686 = local81 - (local81 - 1 - this.anInt3686) % local91 - 1;
            }
        } else if (this.anInt3686 >= local81) {
            this.anInt3686 = local76 + (this.anInt3686 - local76) % local91;
        }
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(I)V")
    public synchronized void method3344(@OriginalArg(0) int arg0) {
        @Pc(7) int local7 = ((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length << 8;
        if (arg0 < -1) {
            arg0 = -1;
        }
        if (arg0 > local7) {
            arg0 = local7;
        }
        this.anInt3686 = arg0;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss method9135() {
        return null;
    }

    @OriginalMember(owner = "client!haa", name = "g", descriptor = "()Z")
    public boolean method3345() {
        @Pc(2) int local2 = this.volume;
        @Pc(10) int local10;
        @Pc(8) int local8;
        if (local2 == Integer.MIN_VALUE) {
            local8 = 0;
            local10 = 0;
            local2 = 0;
        } else {
            local10 = method3330(local2, this.range);
            local8 = method3328(local2, this.range);
        }
        if (this.anInt3676 != local2 || this.anInt3680 != local10 || this.anInt3677 != local8) {
            if (this.anInt3676 < local2) {
                this.anInt3678 = 1;
                this.anInt3684 = local2 - this.anInt3676;
            } else if (this.anInt3676 > local2) {
                this.anInt3678 = -1;
                this.anInt3684 = this.anInt3676 - local2;
            } else {
                this.anInt3678 = 0;
            }
            if (this.anInt3680 < local10) {
                this.anInt3683 = 1;
                if (this.anInt3684 == 0 || this.anInt3684 > local10 - this.anInt3680) {
                    this.anInt3684 = local10 - this.anInt3680;
                }
            } else if (this.anInt3680 > local10) {
                this.anInt3683 = -1;
                if (this.anInt3684 == 0 || this.anInt3684 > this.anInt3680 - local10) {
                    this.anInt3684 = this.anInt3680 - local10;
                }
            } else {
                this.anInt3683 = 0;
            }
            if (this.anInt3677 < local8) {
                this.anInt3687 = 1;
                if (this.anInt3684 == 0 || this.anInt3684 > local8 - this.anInt3677) {
                    this.anInt3684 = local8 - this.anInt3677;
                }
            } else if (this.anInt3677 > local8) {
                this.anInt3687 = -1;
                if (this.anInt3684 == 0 || this.anInt3684 > this.anInt3677 - local8) {
                    this.anInt3684 = this.anInt3677 - local8;
                }
            } else {
                this.anInt3687 = 0;
            }
            return false;
        } else if (this.volume == Integer.MIN_VALUE) {
            this.volume = 0;
            this.anInt3676 = this.anInt3680 = this.anInt3677 = 0;
            this.unlink();
            return true;
        } else {
            this.method3314();
            return false;
        }
    }

    @OriginalMember(owner = "client!haa", name = "d", descriptor = "()I")
    @Override
    public int method9136() {
        @Pc(6) int local6 = this.anInt3676 * 3 >> 6;
        local6 = (local6 ^ local6 >> 31) + (local6 >>> 31);
        if (this.loops == 0) {
            local6 -= local6 * this.anInt3686 / (((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length << 8);
        } else if (this.loops >= 0) {
            local6 -= local6 * this.nominalBitRate / ((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length;
        }
        return local6 > 255 ? 255 : local6;
    }

    @OriginalMember(owner = "client!haa", name = "k", descriptor = "()I")
    public synchronized int getRange() {
        return this.range < 0 ? -1 : this.range;
    }
}

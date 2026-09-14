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
    public static int mixForwardMono(@OriginalArg(0) byte[] samples, @OriginalArg(1) int[] mix, @OriginalArg(2) int position, @OriginalArg(3) int offset, @OriginalArg(4) int gain, @OriginalArg(6) int end, @OriginalArg(7) int limit, @OriginalArg(8) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        @Pc(11) int scaledGain = gain << 2;
        @Pc(18) int stop;
        if ((stop = offset + limitIndex - position) > end) {
            stop = end;
        }
        stop -= 3;
        @Pc(26) int slot0;
        while (offset < stop) {
            slot0 = offset++;
            @Pc(29) int mixed0 = mix[slot0];
            @Pc(32) int next0 = position + 1;
            mix[slot0] = mixed0 + samples[position] * scaledGain;
            @Pc(39) int slot1 = offset++;
            @Pc(42) int mixed1 = mix[slot1];
            @Pc(45) int next1 = next0 + 1;
            mix[slot1] = mixed1 + samples[next0] * scaledGain;
            @Pc(52) int slot2 = offset++;
            @Pc(55) int mixed2 = mix[slot2];
            @Pc(58) int next2 = next1 + 1;
            mix[slot2] = mixed2 + samples[next1] * scaledGain;
            @Pc(65) int slot3 = offset++;
            @Pc(68) int mixed3 = mix[slot3];
            position = next2 + 1;
            mix[slot3] = mixed3 + samples[next2] * scaledGain;
        }
        stop += 3;
        while (offset < stop) {
            slot0 = offset++;
            mix[slot0] += samples[position++] * scaledGain;
        }
        stream.position = position << 8;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int mixForwardStereoResampled(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int leftGain, @OriginalArg(7) int rightGain, @OriginalArg(9) int end, @OriginalArg(10) int limit, @OriginalArg(11) SoundStream stream, @OriginalArg(12) int rate, @OriginalArg(13) int edgeSample) {
        @Pc(14) int stop;
        if (rate == 0 || (stop = offset + (limit + rate - position - 257) / rate) > end) {
            stop = end;
        }
        offset <<= 0x1;
        stop <<= 0x1;
        @Pc(35) byte sample;
        @Pc(54) int nextSlot;
        @Pc(51) int interpolated;
        @Pc(53) int slot;
        while (offset < stop) {
            @Pc(31) int base = position >> 8;
            sample = samples[base];
            interpolated = (sample << 8) + (samples[base + 1] - sample) * (position & 0xFF);
            slot = offset;
            nextSlot = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            @Pc(65) int rightSlot = nextSlot;
            offset = nextSlot + 1;
            mix[rightSlot] += interpolated * rightGain >> 6;
            position += rate;
        }
        if (rate == 0 || (stop = (offset >> 1) + (limit + rate - position - 1) / rate) > end) {
            stop = end;
        }
        stop <<= 0x1;
        while (offset < stop) {
            sample = samples[position >> 8];
            interpolated = (sample << 8) + (edgeSample - sample) * (position & 0xFF);
            slot = offset;
            nextSlot = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            slot = nextSlot;
            offset = nextSlot + 1;
            mix[slot] += interpolated * rightGain >> 6;
            position += rate;
        }
        stream.position = position;
        return offset >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II[B[IIIIIIIIIILclient!haa;II)I")
    public static int mixForwardStereoResampledRamped(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int leftGain, @OriginalArg(7) int rightGain, @OriginalArg(8) int leftGainStep, @OriginalArg(9) int rightGainStep, @OriginalArg(11) int end, @OriginalArg(12) int limit, @OriginalArg(13) SoundStream stream, @OriginalArg(14) int rate, @OriginalArg(15) int edgeSample) {
        stream.gain -= stream.gainStep * offset;
        @Pc(23) int stop;
        if (rate == 0 || (stop = offset + (limit + rate - position - 257) / rate) > end) {
            stop = end;
        }
        offset <<= 0x1;
        stop <<= 0x1;
        @Pc(44) byte sample;
        @Pc(63) int next;
        @Pc(60) int interpolated;
        @Pc(62) int slot;
        while (offset < stop) {
            @Pc(40) int base = position >> 8;
            sample = samples[base];
            interpolated = (sample << 8) + (samples[base + 1] - sample) * (position & 0xFF);
            slot = offset;
            next = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            leftGain += leftGainStep;
            @Pc(78) int rightSlot = next;
            offset = next + 1;
            mix[rightSlot] += interpolated * rightGain >> 6;
            rightGain += rightGainStep;
            position += rate;
        }
        if (rate == 0 || (stop = (offset >> 1) + (limit + rate - position - 1) / rate) > end) {
            stop = end;
        }
        stop <<= 0x1;
        while (offset < stop) {
            sample = samples[position >> 8];
            interpolated = (sample << 8) + (edgeSample - sample) * (position & 0xFF);
            slot = offset;
            next = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            leftGain += leftGainStep;
            slot = next;
            offset = next + 1;
            mix[slot] += interpolated * rightGain >> 6;
            rightGain += rightGainStep;
            position += rate;
        }
        next = offset >> 1;
        stream.gain += stream.gainStep * next;
        stream.leftGain = leftGain;
        stream.rightGain = rightGain;
        stream.position = position;
        return next;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II[B[IIIIIIILclient!haa;II)I")
    public static int mixForwardMonoResampled(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int gain, @OriginalArg(8) int end, @OriginalArg(9) int limit, @OriginalArg(10) SoundStream stream, @OriginalArg(11) int rate, @OriginalArg(12) int edgeSample) {
        @Pc(14) int stop;
        if (rate == 0 || (stop = offset + (limit + rate - position - 257) / rate) > end) {
            stop = end;
        }
        @Pc(27) byte sample;
        @Pc(29) int slot;
        while (offset < stop) {
            @Pc(23) int base = position >> 8;
            sample = samples[base];
            slot = offset++;
            mix[slot] += ((sample << 8) + (samples[base + 1] - sample) * (position & 0xFF)) * gain >> 6;
            position += rate;
        }
        if (rate == 0 || (stop = offset + (limit + rate - position - 1) / rate) > end) {
            stop = end;
        }
        while (offset < stop) {
            sample = samples[position >> 8];
            slot = offset++;
            mix[slot] += ((sample << 8) + (edgeSample - sample) * (position & 0xFF)) * gain >> 6;
            position += rate;
        }
        stream.position = position;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([B[IIIIIIIILclient!haa;)I")
    public static int mixForwardMonoRamped(@OriginalArg(0) byte[] samples, @OriginalArg(1) int[] mix, @OriginalArg(2) int position, @OriginalArg(3) int offset, @OriginalArg(4) int gain, @OriginalArg(5) int gainStep, @OriginalArg(7) int end, @OriginalArg(8) int limit, @OriginalArg(9) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        gain <<= 0x2;
        @Pc(15) int scaledStep = gainStep << 2;
        @Pc(22) int stop;
        if ((stop = offset + limitIndex - position) > end) {
            stop = end;
        }
        stream.leftGain += stream.leftGainStep * (stop - offset);
        stream.rightGain += stream.rightGainStep * (stop - offset);
        stop -= 3;
        @Pc(52) int slot0;
        while (offset < stop) {
            slot0 = offset++;
            @Pc(55) int mixed0 = mix[slot0];
            @Pc(58) int next0 = position + 1;
            mix[slot0] = mixed0 + samples[position] * gain;
            @Pc(67) int gain1 = gain + scaledStep;
            @Pc(69) int slot1 = offset++;
            @Pc(72) int mixed1 = mix[slot1];
            @Pc(75) int next1 = next0 + 1;
            mix[slot1] = mixed1 + samples[next0] * gain1;
            @Pc(84) int gain2 = gain1 + scaledStep;
            @Pc(86) int slot2 = offset++;
            @Pc(89) int mixed2 = mix[slot2];
            @Pc(92) int next2 = next1 + 1;
            mix[slot2] = mixed2 + samples[next1] * gain2;
            @Pc(101) int gain3 = gain2 + scaledStep;
            @Pc(103) int slot3 = offset++;
            @Pc(106) int mixed3 = mix[slot3];
            position = next2 + 1;
            mix[slot3] = mixed3 + samples[next2] * gain3;
            gain = gain3 + scaledStep;
        }
        stop += 3;
        while (offset < stop) {
            slot0 = offset++;
            mix[slot0] += samples[position++] * gain;
            gain += scaledStep;
        }
        stream.gain = gain >> 2;
        stream.position = position << 8;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "c", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int mixBackwardStereoResampled(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int leftGain, @OriginalArg(7) int rightGain, @OriginalArg(9) int end, @OriginalArg(10) int limit, @OriginalArg(11) SoundStream stream, @OriginalArg(12) int rate, @OriginalArg(13) int edgeSample) {
        @Pc(14) int stop;
        if (rate == 0 || (stop = offset + (limit + rate + 256 - position) / rate) > end) {
            stop = end;
        }
        offset <<= 0x1;
        stop <<= 0x1;
        @Pc(54) int nextSlot;
        @Pc(51) int interpolated;
        @Pc(53) int slot;
        while (offset < stop) {
            @Pc(31) int base = position >> 8;
            @Pc(37) byte sample = samples[base - 1];
            interpolated = (sample << 8) + (samples[base] - sample) * (position & 0xFF);
            slot = offset;
            nextSlot = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            @Pc(65) int rightSlot = nextSlot;
            offset = nextSlot + 1;
            mix[rightSlot] += interpolated * rightGain >> 6;
            position += rate;
        }
        if (rate == 0 || (stop = (offset >> 1) + (limit + rate - position) / rate) > end) {
            stop = end;
        }
        stop <<= 0x1;
        while (offset < stop) {
            interpolated = (edgeSample << 8) + (samples[position >> 8] - edgeSample) * (position & 0xFF);
            slot = offset;
            nextSlot = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            slot = nextSlot;
            offset = nextSlot + 1;
            mix[slot] += interpolated * rightGain >> 6;
            position += rate;
        }
        stream.position = position;
        return offset >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(I[B[IIIIIIIIIILclient!haa;)I")
    public static int mixForwardStereoRamped(@OriginalArg(1) byte[] samples, @OriginalArg(2) int[] mix, @OriginalArg(3) int position, @OriginalArg(4) int offset, @OriginalArg(5) int leftGain, @OriginalArg(6) int rightGain, @OriginalArg(7) int leftGainStep, @OriginalArg(8) int rightGainStep, @OriginalArg(10) int end, @OriginalArg(11) int limit, @OriginalArg(12) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        leftGain <<= 0x2;
        rightGain <<= 0x2;
        @Pc(19) int scaledLeftStep = leftGainStep << 2;
        @Pc(23) int scaledRightStep = rightGainStep << 2;
        @Pc(30) int stop;
        if ((stop = offset + limitIndex - position) > end) {
            stop = end;
        }
        stream.gain += stream.gainStep * (stop - offset);
        offset <<= 0x1;
        stop <<= 0x1;
        stop -= 6;
        @Pc(60) byte sample0;
        @Pc(63) int rightSlot0;
        while (offset < stop) {
            @Pc(58) int next0 = position + 1;
            sample0 = samples[position];
            rightSlot0 = offset + 1;
            mix[offset] += sample0 * leftGain;
            @Pc(74) int leftGain1 = leftGain + scaledLeftStep;
            @Pc(77) int leftSlot1 = rightSlot0 + 1;
            mix[rightSlot0] += sample0 * rightGain;
            @Pc(88) int rightGain1 = rightGain + scaledRightStep;
            @Pc(91) int next1 = next0 + 1;
            @Pc(93) byte sample1 = samples[next0];
            @Pc(96) int rightSlot1 = leftSlot1 + 1;
            mix[leftSlot1] += sample1 * leftGain1;
            @Pc(107) int leftGain2 = leftGain1 + scaledLeftStep;
            @Pc(110) int leftSlot2 = rightSlot1 + 1;
            mix[rightSlot1] += sample1 * rightGain1;
            @Pc(121) int rightGain2 = rightGain1 + scaledRightStep;
            @Pc(124) int next2 = next1 + 1;
            @Pc(126) byte sample2 = samples[next1];
            @Pc(129) int rightSlot2 = leftSlot2 + 1;
            mix[leftSlot2] += sample2 * leftGain2;
            @Pc(140) int leftGain3 = leftGain2 + scaledLeftStep;
            @Pc(143) int leftSlot3 = rightSlot2 + 1;
            mix[rightSlot2] += sample2 * rightGain2;
            @Pc(154) int rightGain3 = rightGain2 + scaledRightStep;
            position = next2 + 1;
            @Pc(159) byte sample3 = samples[next2];
            @Pc(162) int rightSlot3 = leftSlot3 + 1;
            mix[leftSlot3] += sample3 * leftGain3;
            leftGain = leftGain3 + scaledLeftStep;
            offset = rightSlot3 + 1;
            mix[rightSlot3] += sample3 * rightGain3;
            rightGain = rightGain3 + scaledRightStep;
        }
        stop += 6;
        while (offset < stop) {
            sample0 = samples[position++];
            rightSlot0 = offset + 1;
            mix[offset] += sample0 * leftGain;
            leftGain += scaledLeftStep;
            offset = rightSlot0 + 1;
            mix[rightSlot0] += sample0 * rightGain;
            rightGain += scaledRightStep;
        }
        stream.leftGain = leftGain >> 2;
        stream.rightGain = rightGain >> 2;
        stream.position = position << 8;
        return offset >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II[B[IIIIIIIIIILclient!haa;II)I")
    public static int mixBackwardStereoResampledRamped(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int leftGain, @OriginalArg(7) int rightGain, @OriginalArg(8) int leftGainStep, @OriginalArg(9) int rightGainStep, @OriginalArg(11) int end, @OriginalArg(12) int limit, @OriginalArg(13) SoundStream stream, @OriginalArg(14) int rate, @OriginalArg(15) int edgeSample) {
        stream.gain -= stream.gainStep * offset;
        @Pc(23) int stop;
        if (rate == 0 || (stop = offset + (limit + rate + 256 - position) / rate) > end) {
            stop = end;
        }
        offset <<= 0x1;
        stop <<= 0x1;
        @Pc(63) int next;
        @Pc(60) int interpolated;
        @Pc(62) int slot;
        while (offset < stop) {
            @Pc(40) int base = position >> 8;
            @Pc(46) byte sample = samples[base - 1];
            interpolated = (sample << 8) + (samples[base] - sample) * (position & 0xFF);
            slot = offset;
            next = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            leftGain += leftGainStep;
            @Pc(78) int rightSlot = next;
            offset = next + 1;
            mix[rightSlot] += interpolated * rightGain >> 6;
            rightGain += rightGainStep;
            position += rate;
        }
        if (rate == 0 || (stop = (offset >> 1) + (limit + rate - position) / rate) > end) {
            stop = end;
        }
        stop <<= 0x1;
        while (offset < stop) {
            interpolated = (edgeSample << 8) + (samples[position >> 8] - edgeSample) * (position & 0xFF);
            slot = offset;
            next = offset + 1;
            mix[slot] += interpolated * leftGain >> 6;
            leftGain += leftGainStep;
            slot = next;
            offset = next + 1;
            mix[slot] += interpolated * rightGain >> 6;
            rightGain += rightGainStep;
            position += rate;
        }
        next = offset >> 1;
        stream.gain += stream.gainStep * next;
        stream.leftGain = leftGain;
        stream.rightGain = rightGain;
        stream.position = position;
        return next;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(II)I")
    public static int rightGainFor(@OriginalArg(0) int volume, @OriginalArg(1) int range) {
        return range < 0 ? -volume : (int) ((double) volume * Math.sqrt((double) range * 1.220703125E-4D) + 0.5D);
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(I[B[IIIIIIIILclient!haa;)I")
    public static int mixForwardStereo(@OriginalArg(1) byte[] samples, @OriginalArg(2) int[] mix, @OriginalArg(3) int position, @OriginalArg(4) int offset, @OriginalArg(5) int leftGain, @OriginalArg(6) int rightGain, @OriginalArg(8) int end, @OriginalArg(9) int limit, @OriginalArg(10) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        @Pc(11) int scaledLeft = leftGain << 2;
        @Pc(15) int scaledRight = rightGain << 2;
        @Pc(22) int stop;
        if ((stop = offset + limitIndex - position) > end) {
            stop = end;
        }
        offset <<= 0x1;
        stop <<= 0x1;
        stop -= 6;
        @Pc(41) byte sample0;
        @Pc(44) int leftSlot0;
        while (offset < stop) {
            @Pc(39) int next0 = position + 1;
            sample0 = samples[position];
            leftSlot0 = offset + 1;
            mix[offset] += sample0 * scaledLeft;
            @Pc(54) int rightSlot0 = leftSlot0 + 1;
            mix[leftSlot0] += sample0 * scaledRight;
            @Pc(64) int next1 = next0 + 1;
            @Pc(66) byte sample1 = samples[next0];
            @Pc(69) int leftSlot1 = rightSlot0 + 1;
            mix[rightSlot0] += sample1 * scaledLeft;
            @Pc(79) int rightSlot1 = leftSlot1 + 1;
            mix[leftSlot1] += sample1 * scaledRight;
            @Pc(89) int next2 = next1 + 1;
            @Pc(91) byte sample2 = samples[next1];
            @Pc(94) int leftSlot2 = rightSlot1 + 1;
            mix[rightSlot1] += sample2 * scaledLeft;
            @Pc(104) int rightSlot2 = leftSlot2 + 1;
            mix[leftSlot2] += sample2 * scaledRight;
            position = next2 + 1;
            @Pc(116) byte sample3 = samples[next2];
            @Pc(119) int leftSlot3 = rightSlot2 + 1;
            mix[rightSlot2] += sample3 * scaledLeft;
            offset = leftSlot3 + 1;
            mix[leftSlot3] += sample3 * scaledRight;
        }
        stop += 6;
        while (offset < stop) {
            sample0 = samples[position++];
            leftSlot0 = offset + 1;
            mix[offset] += sample0 * scaledLeft;
            offset = leftSlot0 + 1;
            mix[leftSlot0] += sample0 * scaledRight;
        }
        stream.position = position << 8;
        return offset >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II)I")
    public static int leftGainFor(@OriginalArg(0) int volume, @OriginalArg(1) int range) {
        return range < 0 ? volume : (int) ((double) volume * Math.sqrt((double) (16384 - range) * 1.220703125E-4D) + 0.5D);
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int mixForwardMonoResampledRamped(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int gain, @OriginalArg(7) int gainStep, @OriginalArg(9) int end, @OriginalArg(10) int limit, @OriginalArg(11) SoundStream stream, @OriginalArg(12) int rate, @OriginalArg(13) int edgeSample) {
        stream.leftGain -= stream.leftGainStep * offset;
        stream.rightGain -= stream.rightGainStep * offset;
        @Pc(32) int stop;
        if (rate == 0 || (stop = offset + (limit + rate - position - 257) / rate) > end) {
            stop = end;
        }
        @Pc(45) byte sample;
        @Pc(47) int slot;
        while (offset < stop) {
            @Pc(41) int base = position >> 8;
            sample = samples[base];
            slot = offset++;
            mix[slot] += ((sample << 8) + (samples[base + 1] - sample) * (position & 0xFF)) * gain >> 6;
            gain += gainStep;
            position += rate;
        }
        if (rate == 0 || (stop = offset + (limit + rate - position - 1) / rate) > end) {
            stop = end;
        }
        while (offset < stop) {
            sample = samples[position >> 8];
            slot = offset++;
            mix[slot] += ((sample << 8) + (edgeSample - sample) * (position & 0xFF)) * gain >> 6;
            gain += gainStep;
            position += rate;
        }
        stream.leftGain += stream.leftGainStep * offset;
        stream.rightGain += stream.rightGainStep * offset;
        stream.gain = gain;
        stream.position = position;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "([B[IIIIIIIILclient!haa;)I")
    public static int mixBackwardMonoRamped(@OriginalArg(0) byte[] samples, @OriginalArg(1) int[] mix, @OriginalArg(2) int position, @OriginalArg(3) int offset, @OriginalArg(4) int gain, @OriginalArg(5) int gainStep, @OriginalArg(7) int end, @OriginalArg(8) int limit, @OriginalArg(9) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        gain <<= 0x2;
        @Pc(15) int scaledStep = gainStep << 2;
        @Pc(24) int stop;
        if ((stop = offset + position + 1 - limitIndex) > end) {
            stop = end;
        }
        stream.leftGain += stream.leftGainStep * (stop - offset);
        stream.rightGain += stream.rightGainStep * (stop - offset);
        stop -= 3;
        @Pc(54) int slot0;
        while (offset < stop) {
            slot0 = offset++;
            @Pc(57) int mixed0 = mix[slot0];
            @Pc(60) int prev0 = position - 1;
            mix[slot0] = mixed0 + samples[position] * gain;
            @Pc(69) int gain1 = gain + scaledStep;
            @Pc(71) int slot1 = offset++;
            @Pc(74) int mixed1 = mix[slot1];
            @Pc(77) int prev1 = prev0 - 1;
            mix[slot1] = mixed1 + samples[prev0] * gain1;
            @Pc(86) int gain2 = gain1 + scaledStep;
            @Pc(88) int slot2 = offset++;
            @Pc(91) int mixed2 = mix[slot2];
            @Pc(94) int prev2 = prev1 - 1;
            mix[slot2] = mixed2 + samples[prev1] * gain2;
            @Pc(103) int gain3 = gain2 + scaledStep;
            @Pc(105) int slot3 = offset++;
            @Pc(108) int mixed3 = mix[slot3];
            position = prev2 - 1;
            mix[slot3] = mixed3 + samples[prev2] * gain3;
            gain = gain3 + scaledStep;
        }
        stop += 3;
        while (offset < stop) {
            slot0 = offset++;
            mix[slot0] += samples[position--] * gain;
            gain += scaledStep;
        }
        stream.gain = gain >> 2;
        stream.position = position << 8;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(I[B[IIIIIIIILclient!haa;)I")
    public static int mixBackwardStereo(@OriginalArg(1) byte[] samples, @OriginalArg(2) int[] mix, @OriginalArg(3) int position, @OriginalArg(4) int offset, @OriginalArg(5) int leftGain, @OriginalArg(6) int rightGain, @OriginalArg(8) int end, @OriginalArg(9) int limit, @OriginalArg(10) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        @Pc(11) int scaledLeft = leftGain << 2;
        @Pc(15) int scaledRight = rightGain << 2;
        @Pc(24) int stop;
        if ((stop = offset + position + 1 - limitIndex) > end) {
            stop = end;
        }
        offset <<= 0x1;
        stop <<= 0x1;
        stop -= 6;
        @Pc(43) byte sample0;
        @Pc(46) int leftSlot0;
        while (offset < stop) {
            @Pc(41) int prev0 = position - 1;
            sample0 = samples[position];
            leftSlot0 = offset + 1;
            mix[offset] += sample0 * scaledLeft;
            @Pc(56) int rightSlot0 = leftSlot0 + 1;
            mix[leftSlot0] += sample0 * scaledRight;
            @Pc(66) int prev1 = prev0 - 1;
            @Pc(68) byte sample1 = samples[prev0];
            @Pc(71) int leftSlot1 = rightSlot0 + 1;
            mix[rightSlot0] += sample1 * scaledLeft;
            @Pc(81) int rightSlot1 = leftSlot1 + 1;
            mix[leftSlot1] += sample1 * scaledRight;
            @Pc(91) int prev2 = prev1 - 1;
            @Pc(93) byte sample2 = samples[prev1];
            @Pc(96) int leftSlot2 = rightSlot1 + 1;
            mix[rightSlot1] += sample2 * scaledLeft;
            @Pc(106) int rightSlot2 = leftSlot2 + 1;
            mix[leftSlot2] += sample2 * scaledRight;
            position = prev2 - 1;
            @Pc(118) byte sample3 = samples[prev2];
            @Pc(121) int leftSlot3 = rightSlot2 + 1;
            mix[rightSlot2] += sample3 * scaledLeft;
            offset = leftSlot3 + 1;
            mix[leftSlot3] += sample3 * scaledRight;
        }
        stop += 6;
        while (offset < stop) {
            sample0 = samples[position--];
            leftSlot0 = offset + 1;
            mix[offset] += sample0 * scaledLeft;
            offset = leftSlot0 + 1;
            mix[leftSlot0] += sample0 * scaledRight;
        }
        stream.position = position << 8;
        return offset >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(II[B[IIIIIIILclient!haa;II)I")
    public static int mixBackwardMonoResampled(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int gain, @OriginalArg(8) int end, @OriginalArg(9) int limit, @OriginalArg(10) SoundStream stream, @OriginalArg(11) int rate, @OriginalArg(12) int edgeSample) {
        @Pc(14) int stop;
        if (rate == 0 || (stop = offset + (limit + rate + 256 - position) / rate) > end) {
            stop = end;
        }
        @Pc(31) int slot;
        while (offset < stop) {
            @Pc(23) int base = position >> 8;
            @Pc(29) byte sample = samples[base - 1];
            slot = offset++;
            mix[slot] += ((sample << 8) + (samples[base] - sample) * (position & 0xFF)) * gain >> 6;
            position += rate;
        }
        if (rate == 0 || (stop = offset + (limit + rate - position) / rate) > end) {
            stop = end;
        }
        while (offset < stop) {
            slot = offset++;
            mix[slot] += ((edgeSample << 8) + (samples[position >> 8] - edgeSample) * (position & 0xFF)) * gain >> 6;
            position += rate;
        }
        stream.position = position;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(I[B[IIIIIIIIIILclient!haa;)I")
    public static int mixBackwardStereoRamped(@OriginalArg(1) byte[] samples, @OriginalArg(2) int[] mix, @OriginalArg(3) int position, @OriginalArg(4) int offset, @OriginalArg(5) int leftGain, @OriginalArg(6) int rightGain, @OriginalArg(7) int leftGainStep, @OriginalArg(8) int rightGainStep, @OriginalArg(10) int end, @OriginalArg(11) int limit, @OriginalArg(12) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        leftGain <<= 0x2;
        rightGain <<= 0x2;
        @Pc(19) int scaledLeftStep = leftGainStep << 2;
        @Pc(23) int scaledRightStep = rightGainStep << 2;
        @Pc(32) int stop;
        if ((stop = offset + position + 1 - limitIndex) > end) {
            stop = end;
        }
        stream.gain += stream.gainStep * (stop - offset);
        offset <<= 0x1;
        stop <<= 0x1;
        stop -= 6;
        @Pc(62) byte sample0;
        @Pc(65) int rightSlot0;
        while (offset < stop) {
            @Pc(60) int prev0 = position - 1;
            sample0 = samples[position];
            rightSlot0 = offset + 1;
            mix[offset] += sample0 * leftGain;
            @Pc(76) int leftGain1 = leftGain + scaledLeftStep;
            @Pc(79) int leftSlot1 = rightSlot0 + 1;
            mix[rightSlot0] += sample0 * rightGain;
            @Pc(90) int rightGain1 = rightGain + scaledRightStep;
            @Pc(93) int prev1 = prev0 - 1;
            @Pc(95) byte sample1 = samples[prev0];
            @Pc(98) int rightSlot1 = leftSlot1 + 1;
            mix[leftSlot1] += sample1 * leftGain1;
            @Pc(109) int leftGain2 = leftGain1 + scaledLeftStep;
            @Pc(112) int leftSlot2 = rightSlot1 + 1;
            mix[rightSlot1] += sample1 * rightGain1;
            @Pc(123) int rightGain2 = rightGain1 + scaledRightStep;
            @Pc(126) int prev2 = prev1 - 1;
            @Pc(128) byte sample2 = samples[prev1];
            @Pc(131) int rightSlot2 = leftSlot2 + 1;
            mix[leftSlot2] += sample2 * leftGain2;
            @Pc(142) int leftGain3 = leftGain2 + scaledLeftStep;
            @Pc(145) int leftSlot3 = rightSlot2 + 1;
            mix[rightSlot2] += sample2 * rightGain2;
            @Pc(156) int rightGain3 = rightGain2 + scaledRightStep;
            position = prev2 - 1;
            @Pc(161) byte sample3 = samples[prev2];
            @Pc(164) int rightSlot3 = leftSlot3 + 1;
            mix[leftSlot3] += sample3 * leftGain3;
            leftGain = leftGain3 + scaledLeftStep;
            offset = rightSlot3 + 1;
            mix[rightSlot3] += sample3 * rightGain3;
            rightGain = rightGain3 + scaledRightStep;
        }
        stop += 6;
        while (offset < stop) {
            sample0 = samples[position--];
            rightSlot0 = offset + 1;
            mix[offset] += sample0 * leftGain;
            leftGain += scaledLeftStep;
            offset = rightSlot0 + 1;
            mix[rightSlot0] += sample0 * rightGain;
            rightGain += scaledRightStep;
        }
        stream.leftGain = leftGain >> 2;
        stream.rightGain = rightGain >> 2;
        stream.position = position << 8;
        return offset >> 1;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([B[IIIIIIILclient!haa;)I")
    public static int mixBackwardMono(@OriginalArg(0) byte[] samples, @OriginalArg(1) int[] mix, @OriginalArg(2) int position, @OriginalArg(3) int offset, @OriginalArg(4) int gain, @OriginalArg(6) int end, @OriginalArg(7) int limit, @OriginalArg(8) SoundStream stream) {
        position >>= 0x8;
        @Pc(7) int limitIndex = limit >> 8;
        @Pc(11) int scaledGain = gain << 2;
        @Pc(20) int stop;
        if ((stop = offset + position + 1 - limitIndex) > end) {
            stop = end;
        }
        stop -= 3;
        @Pc(28) int slot0;
        while (offset < stop) {
            slot0 = offset++;
            @Pc(31) int mixed0 = mix[slot0];
            @Pc(34) int prev0 = position - 1;
            mix[slot0] = mixed0 + samples[position] * scaledGain;
            @Pc(41) int slot1 = offset++;
            @Pc(44) int mixed1 = mix[slot1];
            @Pc(47) int prev1 = prev0 - 1;
            mix[slot1] = mixed1 + samples[prev0] * scaledGain;
            @Pc(54) int slot2 = offset++;
            @Pc(57) int mixed2 = mix[slot2];
            @Pc(60) int prev2 = prev1 - 1;
            mix[slot2] = mixed2 + samples[prev1] * scaledGain;
            @Pc(67) int slot3 = offset++;
            @Pc(70) int mixed3 = mix[slot3];
            position = prev2 - 1;
            mix[slot3] = mixed3 + samples[prev2] * scaledGain;
        }
        stop += 3;
        while (offset < stop) {
            slot0 = offset++;
            mix[slot0] += samples[position--] * scaledGain;
        }
        stream.position = position << 8;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "d", descriptor = "(II[B[IIIIIIIILclient!haa;II)I")
    public static int mixBackwardMonoResampledRamped(@OriginalArg(2) byte[] samples, @OriginalArg(3) int[] mix, @OriginalArg(4) int position, @OriginalArg(5) int offset, @OriginalArg(6) int gain, @OriginalArg(7) int gainStep, @OriginalArg(9) int end, @OriginalArg(10) int limit, @OriginalArg(11) SoundStream stream, @OriginalArg(12) int rate, @OriginalArg(13) int edgeSample) {
        stream.leftGain -= stream.leftGainStep * offset;
        stream.rightGain -= stream.rightGainStep * offset;
        @Pc(32) int stop;
        if (rate == 0 || (stop = offset + (limit + rate + 256 - position) / rate) > end) {
            stop = end;
        }
        @Pc(49) int slot;
        while (offset < stop) {
            @Pc(41) int base = position >> 8;
            @Pc(47) byte sample = samples[base - 1];
            slot = offset++;
            mix[slot] += ((sample << 8) + (samples[base] - sample) * (position & 0xFF)) * gain >> 6;
            gain += gainStep;
            position += rate;
        }
        if (rate == 0 || (stop = offset + (limit + rate - position) / rate) > end) {
            stop = end;
        }
        while (offset < stop) {
            slot = offset++;
            mix[slot] += ((edgeSample << 8) + (samples[position >> 8] - edgeSample) * (position & 0xFF)) * gain >> 6;
            gain += gainStep;
            position += rate;
        }
        stream.leftGain += stream.leftGainStep * offset;
        stream.rightGain += stream.rightGainStep * offset;
        stream.gain = gain;
        stream.position = position;
        return offset;
    }

    @OriginalMember(owner = "client!haa", name = "A", descriptor = "I")
    public int gain;

    @OriginalMember(owner = "client!haa", name = "w", descriptor = "I")
    public int rightGain;

    @OriginalMember(owner = "client!haa", name = "C", descriptor = "I")
    public int gainStep;

    @OriginalMember(owner = "client!haa", name = "s", descriptor = "I")
    public int leftGain;

    @OriginalMember(owner = "client!haa", name = "r", descriptor = "I")
    public int leftGainStep;

    @OriginalMember(owner = "client!haa", name = "q", descriptor = "I")
    public int rampSamples;

    @OriginalMember(owner = "client!haa", name = "p", descriptor = "I")
    public int rightGainStep;

    @OriginalMember(owner = "client!haa", name = "B", descriptor = "I")
    public int loops;

    @OriginalMember(owner = "client!haa", name = "z", descriptor = "I")
    public final int nominalBitRate;

    @OriginalMember(owner = "client!haa", name = "o", descriptor = "I")
    public final int minBitRate;

    @OriginalMember(owner = "client!haa", name = "v", descriptor = "Z")
    public final boolean pingPong;

    @OriginalMember(owner = "client!haa", name = "u", descriptor = "I")
    public int rate;

    @OriginalMember(owner = "client!haa", name = "x", descriptor = "I")
    public int volume;

    @OriginalMember(owner = "client!haa", name = "t", descriptor = "I")
    public int range;

    @OriginalMember(owner = "client!haa", name = "y", descriptor = "I")
    public int position;

    @OriginalMember(owner = "client!haa", name = "<init>", descriptor = "(Lclient!sq;III)V")
    public SoundStream(@OriginalArg(0) VariableRateSoundPacket packet, @OriginalArg(1) int rate, @OriginalArg(2) int volume, @OriginalArg(3) int range) {
        super.aClass2_Sub49_6 = packet;
        this.nominalBitRate = packet.nominalBitRate;
        this.minBitRate = packet.minBitRate;
        this.pingPong = packet.pingPong;
        this.rate = rate;
        this.volume = volume;
        this.range = range;
        this.position = 0;
        this.resetGains();
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "([IIIII)I")
    public int fillBackward(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int limit, @OriginalArg(3) int end, @OriginalArg(4) int edgeSample) {
        do {
            if (this.rampSamples <= 0) {
                if (this.rate == -256 && (this.position & 0xFF) == 0) {
                    if (QueueBuss.stereo) {
                        return mixBackwardStereo(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, end, limit, this);
                    }
                    return mixBackwardMono(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, end, limit, this);
                }
                if (QueueBuss.stereo) {
                    return mixBackwardStereoResampled(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, end, limit, this, this.rate, edgeSample);
                }
                return mixBackwardMonoResampled(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, end, limit, this, this.rate, edgeSample);
            }
            @Pc(5) int stop = offset + this.rampSamples;
            if (stop > end) {
                stop = end;
            }
            this.rampSamples += offset;
            if (this.rate == -256 && (this.position & 0xFF) == 0) {
                if (QueueBuss.stereo) {
                    offset = mixBackwardStereoRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, this.leftGainStep, this.rightGainStep, stop, limit, this);
                } else {
                    offset = mixBackwardMonoRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, this.gainStep, stop, limit, this);
                }
            } else if (QueueBuss.stereo) {
                offset = mixBackwardStereoResampledRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, this.leftGainStep, this.rightGainStep, stop, limit, this, this.rate, edgeSample);
            } else {
                offset = mixBackwardMonoResampledRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, this.gainStep, stop, limit, this, this.rate, edgeSample);
            }
            this.rampSamples -= offset;
            if (this.rampSamples != 0) {
                return offset;
            }
        } while (!this.updateFade());
        return end;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([III)V")
    @Override
    public synchronized void fill(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int length) {
        if (this.volume == 0 && this.rampSamples == 0) {
            this.skip(length);
            return;
        }
        @Pc(13) VariableRateSoundPacket packet = (VariableRateSoundPacket) super.aClass2_Sub49_6;
        @Pc(18) int loopStart = this.nominalBitRate << 8;
        @Pc(23) int loopEnd = this.minBitRate << 8;
        @Pc(29) int endPosition = packet.data.length << 8;
        @Pc(33) int loopLength = loopEnd - loopStart;
        if (loopLength <= 0) {
            this.loops = 0;
        }
        @Pc(40) int cursor = offset;
        @Pc(44) int end = length + offset;
        if (this.position < 0) {
            if (this.rate <= 0) {
                this.endFade();
                this.unlink();
                return;
            }
            this.position = 0;
        }
        if (this.position >= endPosition) {
            if (this.rate >= 0) {
                this.endFade();
                this.unlink();
                return;
            }
            this.position = endPosition - 1;
        }
        if (this.loops >= 0) {
            if (this.loops > 0) {
                if (this.pingPong) {
                    label130:
                    {
                        if (this.rate < 0) {
                            cursor = this.fillBackward(mix, offset, loopStart, end, packet.data[this.nominalBitRate]);
                            if (this.position >= loopStart) {
                                return;
                            }
                            this.position = loopStart + loopStart - this.position - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break label130;
                            }
                        }
                        do {
                            cursor = this.fillForward(mix, cursor, loopEnd, end, packet.data[this.minBitRate - 1]);
                            if (this.position < loopEnd) {
                                return;
                            }
                            this.position = loopEnd + loopEnd - this.position - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break;
                            }
                            cursor = this.fillBackward(mix, cursor, loopStart, end, packet.data[this.nominalBitRate]);
                            if (this.position >= loopStart) {
                                return;
                            }
                            this.position = loopStart + loopStart - this.position - 1;
                            this.rate = -this.rate;
                        } while (--this.loops != 0);
                    }
                } else {
                    @Pc(416) int count;
                    if (this.rate < 0) {
                        while (true) {
                            cursor = this.fillBackward(mix, cursor, loopStart, end, packet.data[this.minBitRate - 1]);
                            if (this.position >= loopStart) {
                                return;
                            }
                            count = (loopEnd - this.position - 1) / loopLength;
                            if (count >= this.loops) {
                                this.position += loopLength * this.loops;
                                this.loops = 0;
                                break;
                            }
                            this.position += loopLength * count;
                            this.loops -= count;
                        }
                    } else {
                        while (true) {
                            cursor = this.fillForward(mix, cursor, loopEnd, end, packet.data[this.nominalBitRate]);
                            if (this.position < loopEnd) {
                                return;
                            }
                            count = (this.position - loopStart) / loopLength;
                            if (count >= this.loops) {
                                this.position -= loopLength * this.loops;
                                this.loops = 0;
                                break;
                            }
                            this.position -= loopLength * count;
                            this.loops -= count;
                        }
                    }
                }
            }
            if (this.rate < 0) {
                this.fillBackward(mix, cursor, 0, end, 0);
                if (this.position < 0) {
                    this.position = -1;
                    this.endFade();
                    this.unlink();
                    return;
                }
            } else {
                this.fillForward(mix, cursor, endPosition, end, 0);
                if (this.position >= endPosition) {
                    this.position = endPosition;
                    this.endFade();
                    this.unlink();
                }
            }
        } else if (this.pingPong) {
            if (this.rate < 0) {
                cursor = this.fillBackward(mix, offset, loopStart, end, packet.data[this.nominalBitRate]);
                if (this.position >= loopStart) {
                    return;
                }
                this.position = loopStart + loopStart - this.position - 1;
                this.rate = -this.rate;
            }
            while (true) {
                cursor = this.fillForward(mix, cursor, loopEnd, end, packet.data[this.minBitRate - 1]);
                if (this.position < loopEnd) {
                    return;
                }
                this.position = loopEnd + loopEnd - this.position - 1;
                this.rate = -this.rate;
                cursor = this.fillBackward(mix, cursor, loopStart, end, packet.data[this.nominalBitRate]);
                if (this.position >= loopStart) {
                    return;
                }
                this.position = loopStart + loopStart - this.position - 1;
                this.rate = -this.rate;
            }
        } else if (this.rate < 0) {
            while (true) {
                cursor = this.fillBackward(mix, cursor, loopStart, end, packet.data[this.minBitRate - 1]);
                if (this.position >= loopStart) {
                    return;
                }
                this.position = loopEnd - (loopEnd - 1 - this.position) % loopLength - 1;
            }
        } else {
            while (true) {
                cursor = this.fillForward(mix, cursor, loopEnd, end, packet.data[this.nominalBitRate]);
                if (this.position < loopEnd) {
                    return;
                }
                this.position = loopStart + (this.position - loopStart) % loopLength;
            }
        }
    }

    @OriginalMember(owner = "client!haa", name = "e", descriptor = "()Z")
    public boolean isFinished() {
        return this.position < 0 || this.position >= ((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length << 8;
    }

    @OriginalMember(owner = "client!haa", name = "j", descriptor = "()V")
    public void resetGains() {
        this.gain = this.volume;
        this.leftGain = leftGainFor(this.volume, this.range);
        this.rightGain = rightGainFor(this.volume, this.range);
    }

    @OriginalMember(owner = "client!haa", name = "d", descriptor = "(II)V")
    public synchronized void fadeToVolume(@OriginalArg(0) int samples, @OriginalArg(1) int volume) {
        this.fadeTo(samples, volume, this.getRange());
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "([IIIII)I")
    public int fillForward(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int limit, @OriginalArg(3) int end, @OriginalArg(4) int edgeSample) {
        do {
            if (this.rampSamples <= 0) {
                if (this.rate == 256 && (this.position & 0xFF) == 0) {
                    if (QueueBuss.stereo) {
                        return mixForwardStereo(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, end, limit, this);
                    }
                    return mixForwardMono(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, end, limit, this);
                }
                if (QueueBuss.stereo) {
                    return mixForwardStereoResampled(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, end, limit, this, this.rate, edgeSample);
                }
                return mixForwardMonoResampled(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, end, limit, this, this.rate, edgeSample);
            }
            @Pc(5) int stop = offset + this.rampSamples;
            if (stop > end) {
                stop = end;
            }
            this.rampSamples += offset;
            if (this.rate == 256 && (this.position & 0xFF) == 0) {
                if (QueueBuss.stereo) {
                    offset = mixForwardStereoRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, this.leftGainStep, this.rightGainStep, stop, limit, this);
                } else {
                    offset = mixForwardMonoRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, this.gainStep, stop, limit, this);
                }
            } else if (QueueBuss.stereo) {
                offset = mixForwardStereoResampledRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.leftGain, this.rightGain, this.leftGainStep, this.rightGainStep, stop, limit, this, this.rate, edgeSample);
            } else {
                offset = mixForwardMonoResampledRamped(((VariableRateSoundPacket) super.aClass2_Sub49_6).data, mix, this.position, offset, this.gain, this.gainStep, stop, limit, this, this.rate, edgeSample);
            }
            this.rampSamples -= offset;
            if (this.rampSamples != 0) {
                return offset;
            }
        } while (!this.updateFade());
        return end;
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
    public synchronized void fadeOut(@OriginalArg(0) int samples) {
        if (samples == 0) {
            this.mute();
            this.unlink();
        } else if (this.leftGain == 0 && this.rightGain == 0) {
            this.rampSamples = 0;
            this.volume = 0;
            this.gain = 0;
            this.unlink();
        } else {
            @Pc(31) int maxGain = -this.gain;
            if (this.gain > maxGain) {
                maxGain = this.gain;
            }
            if (-this.leftGain > maxGain) {
                maxGain = -this.leftGain;
            }
            if (this.leftGain > maxGain) {
                maxGain = this.leftGain;
            }
            if (-this.rightGain > maxGain) {
                maxGain = -this.rightGain;
            }
            if (this.rightGain > maxGain) {
                maxGain = this.rightGain;
            }
            if (samples > maxGain) {
                samples = maxGain;
            }
            this.rampSamples = samples;
            this.volume = Integer.MIN_VALUE;
            this.gainStep = -this.gain / samples;
            this.leftGainStep = -this.leftGain / samples;
            this.rightGainStep = -this.rightGain / samples;
        }
    }

    @OriginalMember(owner = "client!haa", name = "h", descriptor = "(I)V")
    public synchronized void mute() {
        this.update(0, this.getRange());
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(Z)V")
    public synchronized void playBackward() {
        this.rate = (this.rate ^ this.rate >> 31) + (this.rate >>> 31);
        this.rate = -this.rate;
    }

    @OriginalMember(owner = "client!haa", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss firstSubStream() {
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
    public void endFade() {
        if (this.rampSamples == 0) {
            return;
        }
        if (this.volume == Integer.MIN_VALUE) {
            this.volume = 0;
        }
        this.rampSamples = 0;
        this.resetGains();
    }

    @OriginalMember(owner = "client!haa", name = "f", descriptor = "()Z")
    public boolean isFading() {
        return this.rampSamples != 0;
    }

    @OriginalMember(owner = "client!haa", name = "c", descriptor = "(II)V")
    public synchronized void update(@OriginalArg(0) int volume, @OriginalArg(1) int range) {
        this.volume = volume;
        this.range = range;
        this.rampSamples = 0;
        this.resetGains();
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(III)V")
    public synchronized void fadeTo(@OriginalArg(0) int samples, @OriginalArg(1) int volume, @OriginalArg(2) int range) {
        if (samples == 0) {
            this.update(volume, range);
            return;
        }
        @Pc(10) int left = leftGainFor(volume, range);
        @Pc(14) int right = rightGainFor(volume, range);
        if (this.leftGain == left && this.rightGain == right) {
            this.rampSamples = 0;
            return;
        }
        @Pc(31) int maxDelta = volume - this.gain;
        if (this.gain - volume > maxDelta) {
            maxDelta = this.gain - volume;
        }
        if (left - this.leftGain > maxDelta) {
            maxDelta = left - this.leftGain;
        }
        if (this.leftGain - left > maxDelta) {
            maxDelta = this.leftGain - left;
        }
        if (right - this.rightGain > maxDelta) {
            maxDelta = right - this.rightGain;
        }
        if (this.rightGain - right > maxDelta) {
            maxDelta = this.rightGain - right;
        }
        if (samples > maxDelta) {
            samples = maxDelta;
        }
        this.rampSamples = samples;
        this.volume = volume;
        this.range = range;
        this.gainStep = (volume - this.gain) / samples;
        this.leftGainStep = (left - this.leftGain) / samples;
        this.rightGainStep = (right - this.rightGain) / samples;
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "()I")
    @Override
    public int method9132() {
        return this.volume == 0 && this.rampSamples == 0 ? 0 : 1;
    }

    @OriginalMember(owner = "client!haa", name = "l", descriptor = "()I")
    public synchronized int getRate() {
        return this.rate < 0 ? -this.rate : this.rate;
    }

    @OriginalMember(owner = "client!haa", name = "h", descriptor = "()I")
    public synchronized int getVolume() {
        return this.volume == Integer.MIN_VALUE ? 0 : this.volume;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "(I)V")
    @Override
    public synchronized void skip(@OriginalArg(0) int length) {
        if (this.rampSamples > 0) {
            if (length >= this.rampSamples) {
                if (this.volume == Integer.MIN_VALUE) {
                    this.volume = 0;
                    this.gain = this.leftGain = this.rightGain = 0;
                    this.unlink();
                    length = this.rampSamples;
                }
                this.rampSamples = 0;
                this.resetGains();
            } else {
                this.gain += this.gainStep * length;
                this.leftGain += this.leftGainStep * length;
                this.rightGain += this.rightGainStep * length;
                this.rampSamples -= length;
            }
        }
        @Pc(71) VariableRateSoundPacket packet = (VariableRateSoundPacket) super.aClass2_Sub49_6;
        @Pc(76) int loopStart = this.nominalBitRate << 8;
        @Pc(81) int loopEnd = this.minBitRate << 8;
        @Pc(87) int endPosition = packet.data.length << 8;
        @Pc(91) int loopLength = loopEnd - loopStart;
        if (loopLength <= 0) {
            this.loops = 0;
        }
        if (this.position < 0) {
            if (this.rate <= 0) {
                this.endFade();
                this.unlink();
                return;
            }
            this.position = 0;
        }
        if (this.position >= endPosition) {
            if (this.rate >= 0) {
                this.endFade();
                this.unlink();
                return;
            }
            this.position = endPosition - 1;
        }
        this.position += this.rate * length;
        if (this.loops >= 0) {
            if (this.loops > 0) {
                if (this.pingPong) {
                    label125:
                    {
                        if (this.rate < 0) {
                            if (this.position >= loopStart) {
                                return;
                            }
                            this.position = loopStart + loopStart - this.position - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break label125;
                            }
                        }
                        do {
                            if (this.position < loopEnd) {
                                return;
                            }
                            this.position = loopEnd + loopEnd - this.position - 1;
                            this.rate = -this.rate;
                            if (--this.loops == 0) {
                                break;
                            }
                            if (this.position >= loopStart) {
                                return;
                            }
                            this.position = loopStart + loopStart - this.position - 1;
                            this.rate = -this.rate;
                        } while (--this.loops != 0);
                    }
                } else {
                    @Pc(361) int count;
                    if (this.rate < 0) {
                        if (this.position >= loopStart) {
                            return;
                        }
                        count = (loopEnd - this.position - 1) / loopLength;
                        if (count < this.loops) {
                            this.position += loopLength * count;
                            this.loops -= count;
                            return;
                        }
                        this.position += loopLength * this.loops;
                        this.loops = 0;
                    } else if (this.position >= loopEnd) {
                        count = (this.position - loopStart) / loopLength;
                        if (count < this.loops) {
                            this.position -= loopLength * count;
                            this.loops -= count;
                            return;
                        }
                        this.position -= loopLength * this.loops;
                        this.loops = 0;
                    } else {
                        return;
                    }
                }
            }
            if (this.rate < 0) {
                if (this.position < 0) {
                    this.position = -1;
                    this.endFade();
                    this.unlink();
                    return;
                }
            } else if (this.position >= endPosition) {
                this.position = endPosition;
                this.endFade();
                this.unlink();
            }
        } else if (this.pingPong) {
            if (this.rate < 0) {
                if (this.position >= loopStart) {
                    return;
                }
                this.position = loopStart + loopStart - this.position - 1;
                this.rate = -this.rate;
            }
            while (this.position >= loopEnd) {
                this.position = loopEnd + loopEnd - this.position - 1;
                this.rate = -this.rate;
                if (this.position >= loopStart) {
                    return;
                }
                this.position = loopStart + loopStart - this.position - 1;
                this.rate = -this.rate;
            }
        } else if (this.rate < 0) {
            if (this.position < loopStart) {
                this.position = loopEnd - (loopEnd - 1 - this.position) % loopLength - 1;
            }
        } else if (this.position >= loopEnd) {
            this.position = loopStart + (this.position - loopStart) % loopLength;
        }
    }

    @OriginalMember(owner = "client!haa", name = "b", descriptor = "(I)V")
    public synchronized void setPosition(@OriginalArg(0) int position) {
        @Pc(7) int end = ((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length << 8;
        if (position < -1) {
            position = -1;
        }
        if (position > end) {
            position = end;
        }
        this.position = position;
    }

    @OriginalMember(owner = "client!haa", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss nextSubStream() {
        return null;
    }

    @OriginalMember(owner = "client!haa", name = "g", descriptor = "()Z")
    public boolean updateFade() {
        @Pc(2) int target = this.volume;
        @Pc(10) int targetLeft;
        @Pc(8) int targetRight;
        if (target == Integer.MIN_VALUE) {
            targetRight = 0;
            targetLeft = 0;
            target = 0;
        } else {
            targetLeft = leftGainFor(target, this.range);
            targetRight = rightGainFor(target, this.range);
        }
        if (this.gain != target || this.leftGain != targetLeft || this.rightGain != targetRight) {
            if (this.gain < target) {
                this.gainStep = 1;
                this.rampSamples = target - this.gain;
            } else if (this.gain > target) {
                this.gainStep = -1;
                this.rampSamples = this.gain - target;
            } else {
                this.gainStep = 0;
            }
            if (this.leftGain < targetLeft) {
                this.leftGainStep = 1;
                if (this.rampSamples == 0 || this.rampSamples > targetLeft - this.leftGain) {
                    this.rampSamples = targetLeft - this.leftGain;
                }
            } else if (this.leftGain > targetLeft) {
                this.leftGainStep = -1;
                if (this.rampSamples == 0 || this.rampSamples > this.leftGain - targetLeft) {
                    this.rampSamples = this.leftGain - targetLeft;
                }
            } else {
                this.leftGainStep = 0;
            }
            if (this.rightGain < targetRight) {
                this.rightGainStep = 1;
                if (this.rampSamples == 0 || this.rampSamples > targetRight - this.rightGain) {
                    this.rampSamples = targetRight - this.rightGain;
                }
            } else if (this.rightGain > targetRight) {
                this.rightGainStep = -1;
                if (this.rampSamples == 0 || this.rampSamples > this.rightGain - targetRight) {
                    this.rampSamples = this.rightGain - targetRight;
                }
            } else {
                this.rightGainStep = 0;
            }
            return false;
        } else if (this.volume == Integer.MIN_VALUE) {
            this.volume = 0;
            this.gain = this.leftGain = this.rightGain = 0;
            this.unlink();
            return true;
        } else {
            this.resetGains();
            return false;
        }
    }

    @OriginalMember(owner = "client!haa", name = "d", descriptor = "()I")
    @Override
    public int method9136() {
        @Pc(6) int level = this.gain * 3 >> 6;
        level = (level ^ level >> 31) + (level >>> 31);
        if (this.loops == 0) {
            level -= level * this.position / (((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length << 8);
        } else if (this.loops >= 0) {
            level -= level * this.nominalBitRate / ((VariableRateSoundPacket) super.aClass2_Sub49_6).data.length;
        }
        return level > 255 ? 255 : level;
    }

    @OriginalMember(owner = "client!haa", name = "k", descriptor = "()I")
    public synchronized int getRange() {
        return this.range < 0 ? -1 : this.range;
    }
}

package com.jagex.sound;

import com.jagex.math.IntMath;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!lg")
public final class SampleRateConverter {

    @OriginalMember(owner = "client!lg", name = "b", descriptor = "[[I")
    public int[][] filterWeights;

    @OriginalMember(owner = "client!lg", name = "c", descriptor = "I")
    public int baseRate;

    @OriginalMember(owner = "client!lg", name = "a", descriptor = "I")
    public int desiredRate;

    @OriginalMember(owner = "client!lg", name = "<init>", descriptor = "(II)V")
    public SampleRateConverter(@OriginalArg(0) int base, @OriginalArg(1) int desired) {
        if (desired != base) {
            @Pc(14) int divisor = IntMath.gcd(desired, base);
            @Pc(18) int dividedBase = base / divisor;
            @Pc(22) int dividedDesired = desired / divisor;
            this.filterWeights = new int[dividedBase][14];
            this.baseRate = dividedBase;
            this.desiredRate = dividedDesired;

            for (@Pc(35) int i = 0; i < dividedBase; i++) {
                @Pc(41) int[] weights = this.filterWeights[i];
                @Pc(49) double d = (double) i / (double) dividedBase + 6.0D;

                @Pc(57) int lo = (int) Math.floor(d - 7.0D + 1.0D);
                if (lo < 0) {
                    lo = 0;
                }

                @Pc(67) int hi = (int) Math.ceil(d + 7.0D);
                if (hi > 14) {
                    hi = 14;
                }

                @Pc(78) double ratio = (double) dividedDesired / (double) dividedBase;
                while (lo < hi) {
                    @Pc(86) double a = ((double) lo - d) * 3.141592653589793D;

                    @Pc(88) double currentRatio = ratio;
                    if (a < -1.0E-4D || a > 1.0E-4D) {
                        currentRatio = ratio * (Math.sin(a) / a);
                    }

                    currentRatio *= Math.cos(((double) lo - d) * 0.2243994752564138D) * 0.46D + 0.54D;
                    weights[lo] = (int) Math.floor(currentRatio * 65536.0D + 0.5D);
                    lo++;
                }
            }
        }
    }

    @OriginalMember(owner = "client!lg", name = "c", descriptor = "(II)I")
    public int convertBitRate(@OriginalArg(1) int rate) {
        if (this.filterWeights != null) {
            rate = (int) ((long) rate * (long) this.desiredRate / (long) this.baseRate) + 6;
        }
        return rate;
    }

    @OriginalMember(owner = "client!lg", name = "a", descriptor = "(II)I")
    public int convertSampleRate(@OriginalArg(1) int rate) {
        if (this.filterWeights != null) {
            rate = (int) ((long) this.desiredRate * (long) rate / (long) this.baseRate);
        }
        return rate;
    }

    @OriginalMember(owner = "client!lg", name = "a", descriptor = "(I[B)[B")
    public byte[] convert(@OriginalArg(1) byte[] data) {
        if (this.filterWeights == null) {
            return data;
        }

        @Pc(19) int sampleCount = (int) ((long) data.length * (long) this.desiredRate / (long) this.baseRate) + 14;
        @Pc(22) int[] samples = new int[sampleCount];
        @Pc(24) int offset = 0;
        @Pc(26) int rate = 0;

        for (@Pc(28) int i = 0; i < data.length; i++) {
            @Pc(34) byte v = data[i];
            @Pc(39) int[] weights = this.filterWeights[rate];

            for (@Pc(41) int j = 0; j < 14; j++) {
                samples[offset + j] += weights[j] * v;
            }

            rate += this.desiredRate;
            @Pc(74) int factor = rate / this.baseRate;
            rate -= this.baseRate * factor;
            offset += factor;
        }

        data = new byte[sampleCount];

        for (@Pc(97) int i = 0; i < sampleCount; i++) {
            @Pc(109) int v = (samples[i] + 32768) >> 16;

            if (v < Byte.MIN_VALUE) {
                data[i] = Byte.MIN_VALUE;
            } else if (v > Byte.MAX_VALUE) {
                data[i] = Byte.MAX_VALUE;
            } else {
                data[i] = (byte) v;
            }
        }

        return data;
    }

    @OriginalMember(owner = "client!lg", name = "a", descriptor = "([SI)[S")
    public short[] convert(@OriginalArg(0) short[] data) {
        if (this.filterWeights == null) {
            return data;
        }

        @Pc(23) int sampleCount = (int) ((long) this.desiredRate * (long) data.length / (long) this.baseRate) + 14;
        @Pc(26) int[] samples = new int[sampleCount];
        @Pc(28) int offset = 0;
        @Pc(30) int rate = 0;

        for (@Pc(32) int i = 0; i < data.length; i++) {
            @Pc(38) short v = data[i];
            @Pc(43) int[] weights = this.filterWeights[rate];

            for (@Pc(45) int j = 0; j < 14; j++) {
                samples[offset + j] += weights[j] * v >> 2;
            }

            rate += this.desiredRate;
            @Pc(80) int factor = rate / this.baseRate;
            offset += factor;
            rate -= this.baseRate * factor;
        }

        data = new short[sampleCount];

        for (@Pc(103) int i = 0; i < sampleCount; i++) {
            @Pc(115) int v = samples[i] + 8192 >> 14;

            if (v < Short.MIN_VALUE) {
                data[i] = Short.MIN_VALUE;
            } else if (v > Short.MAX_VALUE) {
                data[i] = Short.MAX_VALUE;
            } else {
                data[i] = (short) v;
            }
        }

        return data;
    }
}

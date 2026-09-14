package com.jagex.sound;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!sq")
public final class VariableRateSoundPacket extends SoundPacket {

    @OriginalMember(owner = "client!sq", name = "m", descriptor = "Z")
    public boolean pingPong;

    @OriginalMember(owner = "client!sq", name = "n", descriptor = "I")
    public int sampleRate;

    @OriginalMember(owner = "client!sq", name = "l", descriptor = "[B")
    public byte[] data;

    @OriginalMember(owner = "client!sq", name = "o", descriptor = "I")
    public int nominalBitRate;

    @OriginalMember(owner = "client!sq", name = "p", descriptor = "I")
    public int minBitRate;

    @OriginalMember(owner = "client!sq", name = "<init>", descriptor = "(I[BII)V")
    public VariableRateSoundPacket(@OriginalArg(0) int sampleRate, @OriginalArg(1) byte[] data, @OriginalArg(2) int nominalBitRate, @OriginalArg(3) int minBitRate) {
        this.sampleRate = sampleRate;
        this.data = data;
        this.nominalBitRate = nominalBitRate;
        this.minBitRate = minBitRate;
    }

    @OriginalMember(owner = "client!sq", name = "<init>", descriptor = "(I[BIIZ)V")
    public VariableRateSoundPacket(@OriginalArg(0) int sampleRate, @OriginalArg(1) byte[] data, @OriginalArg(2) int nominalBitRate, @OriginalArg(3) int minBitRate, @OriginalArg(4) boolean pingPong) {
        this.sampleRate = sampleRate;
        this.data = data;
        this.nominalBitRate = nominalBitRate;
        this.minBitRate = minBitRate;
        this.pingPong = pingPong;
    }

    @OriginalMember(owner = "client!sq", name = "a", descriptor = "(Lclient!lg;)Lclient!sq;")
    public VariableRateSoundPacket resample(@OriginalArg(0) SampleRateConverter converter) {
        this.data = converter.convert(this.data);
        this.sampleRate = converter.convertSampleRate(this.sampleRate);

        if (this.nominalBitRate == this.minBitRate) {
            this.nominalBitRate = this.minBitRate = converter.convertBitRate(this.nominalBitRate);
        } else {
            this.nominalBitRate = converter.convertBitRate(this.nominalBitRate);
            this.minBitRate = converter.convertBitRate(this.minBitRate);

            if (this.nominalBitRate == this.minBitRate) {
                this.nominalBitRate--;
            }
        }

        return this;
    }
}

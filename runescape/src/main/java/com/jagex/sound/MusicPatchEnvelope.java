package com.jagex.sound;

import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!oaa")
public final class MusicPatchEnvelope {

    @OriginalMember(owner = "client!oaa", name = "l", descriptor = "I")
    public int decay;

    @OriginalMember(owner = "client!oaa", name = "q", descriptor = "I")
    public int decayKeyScale;

    @OriginalMember(owner = "client!oaa", name = "m", descriptor = "I")
    public int vibratoDepth;

    @OriginalMember(owner = "client!oaa", name = "j", descriptor = "I")
    public int vibratoRate;

    @OriginalMember(owner = "client!oaa", name = "o", descriptor = "I")
    public int envelopeKeyScale;

    @OriginalMember(owner = "client!oaa", name = "k", descriptor = "I")
    public int releaseKeyScale;

    @OriginalMember(owner = "client!oaa", name = "i", descriptor = "[B")
    public byte[] release;

    @OriginalMember(owner = "client!oaa", name = "d", descriptor = "[B")
    public byte[] envelope;

    @OriginalMember(owner = "client!oaa", name = "e", descriptor = "I")
    public int vibratoDelay;
}

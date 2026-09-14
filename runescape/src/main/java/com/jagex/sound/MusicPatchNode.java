package com.jagex.sound;

import com.jagex.core.datastruct.key.Node;
import com.jagex.sound.MusicPatchEnvelope;
import com.jagex.sound.MusicPatch;
import com.jagex.sound.VariableRateSoundPacket;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!dha")
public final class MusicPatchNode extends Node {

    @OriginalMember(owner = "client!dha", name = "E", descriptor = "I")
    public int retriggerPhase;

    @OriginalMember(owner = "client!dha", name = "K", descriptor = "I")
    public int samplesUntilUpdate;

    @OriginalMember(owner = "client!dha", name = "J", descriptor = "I")
    public int envelopeIndex;

    @OriginalMember(owner = "client!dha", name = "A", descriptor = "I")
    public int channel;

    @OriginalMember(owner = "client!dha", name = "H", descriptor = "I")
    public int vibratoPhase;

    @OriginalMember(owner = "client!dha", name = "m", descriptor = "I")
    public int envelopePhase;

    @OriginalMember(owner = "client!dha", name = "C", descriptor = "I")
    public int pitch;

    @OriginalMember(owner = "client!dha", name = "p", descriptor = "Lclient!sq;")
    public VariableRateSoundPacket sound;

    @OriginalMember(owner = "client!dha", name = "r", descriptor = "I")
    public int pan;

    @OriginalMember(owner = "client!dha", name = "l", descriptor = "I")
    public int releaseIndex;

    @OriginalMember(owner = "client!dha", name = "D", descriptor = "I")
    public int key;

    @OriginalMember(owner = "client!dha", name = "x", descriptor = "I")
    public int portamentoOffset;

    @OriginalMember(owner = "client!dha", name = "z", descriptor = "I")
    public int exclusiveGroup;

    @OriginalMember(owner = "client!dha", name = "t", descriptor = "Lclient!oaa;")
    public MusicPatchEnvelope envelope;

    @OriginalMember(owner = "client!dha", name = "G", descriptor = "Lclient!haa;")
    public SoundStream stream;

    @OriginalMember(owner = "client!dha", name = "k", descriptor = "I")
    public int volume;

    @OriginalMember(owner = "client!dha", name = "F", descriptor = "Lclient!cea;")
    public MusicPatch patch;

    @OriginalMember(owner = "client!dha", name = "n", descriptor = "I")
    public int elapsed;

    @OriginalMember(owner = "client!dha", name = "I", descriptor = "I")
    public int releasePhase;

    @OriginalMember(owner = "client!dha", name = "q", descriptor = "I")
    public int portamentoFraction;

    @OriginalMember(owner = "client!dha", name = "o", descriptor = "I")
    public int decayPhase;

    @OriginalMember(owner = "client!dha", name = "a", descriptor = "(I)V")
    public void clear() {
        this.stream = null;
        this.envelope = null;
        this.sound = null;
        this.patch = null;
    }
}

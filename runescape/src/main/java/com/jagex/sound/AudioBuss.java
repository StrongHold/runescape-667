package com.jagex.sound;

import com.jagex.core.datastruct.key.Node;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!dea")
public abstract class AudioBuss extends Node {

    @OriginalMember(owner = "client!dea", name = "l", descriptor = "Lclient!dea;")
    public AudioBuss nextInBucket;

    @OriginalMember(owner = "client!dea", name = "n", descriptor = "I")
    public int priority;

    @OriginalMember(owner = "client!dea", name = "k", descriptor = "Lclient!rm;")
    public SoundPacket aClass2_Sub49_6;

    @OriginalMember(owner = "client!dea", name = "m", descriptor = "Z")
    public volatile boolean active = true;

    @OriginalMember(owner = "client!dea", name = "a", descriptor = "(I)V")
    public abstract void skip(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!dea", name = "b", descriptor = "([III)V")
    public abstract void fill(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2);

    @OriginalMember(owner = "client!dea", name = "b", descriptor = "()I")
    public abstract int method9132();

    @OriginalMember(owner = "client!dea", name = "c", descriptor = "()Lclient!dea;")
    public abstract AudioBuss firstSubStream();

    @OriginalMember(owner = "client!dea", name = "a", descriptor = "([III)V")
    protected final void mix(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (this.active) {
            this.fill(arg0, arg1, arg2);
        } else {
            this.skip(arg2);
        }
    }

    @OriginalMember(owner = "client!dea", name = "a", descriptor = "()Lclient!dea;")
    public abstract AudioBuss nextSubStream();

    @OriginalMember(owner = "client!dea", name = "d", descriptor = "()I")
    public int method9136() {
        return 255;
    }
}

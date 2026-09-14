package com.jagex.sound;

import com.jagex.core.datastruct.key.Node;
import com.jagex.sound.AudioBussMixer;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!ada")
public abstract class AudioBussTask extends Node {

    @OriginalMember(owner = "client!ada", name = "k", descriptor = "I")
    public int time;

    @OriginalMember(owner = "client!ada", name = "<init>", descriptor = "()V")
    public AudioBussTask() {
        /* empty */
    }

    @OriginalMember(owner = "client!ada", name = "a", descriptor = "()V")
    public abstract void close();

    @OriginalMember(owner = "client!ada", name = "a", descriptor = "(Lclient!nn;)I")
    public abstract int run(@OriginalArg(0) AudioBussMixer arg0);
}

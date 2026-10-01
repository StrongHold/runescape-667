package com.jagex.game.runetek6.config.lighttype;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!vt")
public final class LightType {

    @OriginalMember(owner = "client!vt", name = "i", descriptor = "I")
    public int pattern = 0;

    @OriginalMember(owner = "client!vt", name = "e", descriptor = "I")
    public int ambient = 0;

    @OriginalMember(owner = "client!vt", name = "k", descriptor = "I")
    public int amplitude = 2048;

    @OriginalMember(owner = "client!vt", name = "j", descriptor = "I")
    public int frequency = 2048;

    @OriginalMember(owner = "client!vt", name = "a", descriptor = "(Lclient!ge;I)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(15) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(code, packet);
        }
    }

    @OriginalMember(owner = "client!vt", name = "a", descriptor = "(ILclient!ge;B)V")
    public void decode(@OriginalArg(0) int code, @OriginalArg(1) Packet packet) {
        if (code == 1) {
            this.pattern = packet.g1();
        } else if (code == 2) {
            this.frequency = packet.g2();
        } else if (code == 3) {
            this.amplitude = packet.g2();
        } else if (code == 4) {
            this.ambient = packet.g2s();
        }
    }
}

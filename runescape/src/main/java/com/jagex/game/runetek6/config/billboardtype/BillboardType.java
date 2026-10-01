package com.jagex.game.runetek6.config.billboardtype;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!uja")
public final class BillboardType {

    @OriginalMember(owner = "client!uja", name = "l", descriptor = "I")
    public int blendMode = 2;

    @OriginalMember(owner = "client!uja", name = "m", descriptor = "I")
    public int texture = -1;

    @OriginalMember(owner = "client!uja", name = "q", descriptor = "I")
    public int height = 64;

    @OriginalMember(owner = "client!uja", name = "k", descriptor = "Z")
    public boolean hideFace = false;

    @OriginalMember(owner = "client!uja", name = "a", descriptor = "Z")
    public boolean hideWithBloom = false;

    @OriginalMember(owner = "client!uja", name = "i", descriptor = "I")
    public int colourOp = 1;

    @OriginalMember(owner = "client!uja", name = "c", descriptor = "I")
    public int width = 64;

    @OriginalMember(owner = "client!uja", name = "a", descriptor = "(IBLclient!ge;)V")
    public void decode(@OriginalArg(0) int id, @OriginalArg(2) Packet packet) {
        while (true) {
            @Pc(13) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(packet, id, code);
        }
    }

    @OriginalMember(owner = "client!uja", name = "a", descriptor = "(Lclient!ge;IBI)V")
    public void decode(@OriginalArg(0) Packet packet, @OriginalArg(1) int id, @OriginalArg(3) int code) {
        if (code == 1) {
            this.texture = packet.g2();
            if (this.texture == 65535) {
                this.texture = -1;
            }
        } else if (code == 2) {
            this.width = packet.g2() + 1;
            this.height = packet.g2() + 1;
        } else if (code == 3) {
            packet.g1b();
        } else if (code == 4) {
            this.blendMode = packet.g1();
        } else if (code == 5) {
            this.colourOp = packet.g1();
        } else if (code == 6) {
            this.hideWithBloom = true;
        } else if (code == 7) {
            this.hideFace = true;
        }
    }
}

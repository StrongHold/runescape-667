package com.jagex.game.runetek6.config.skyboxspheretype;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!afa")
public final class SkyBoxSphereType {

    @OriginalMember(owner = "client!afa", name = "n", descriptor = "I")
    public int contentId;

    @OriginalMember(owner = "client!afa", name = "o", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!afa", name = "f", descriptor = "I")
    public int rotateZ;

    @OriginalMember(owner = "client!afa", name = "l", descriptor = "I")
    public int rotateX;

    @OriginalMember(owner = "client!afa", name = "e", descriptor = "I")
    public int renderType;

    @OriginalMember(owner = "client!afa", name = "g", descriptor = "I")
    public int y;

    @OriginalMember(owner = "client!afa", name = "j", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!afa", name = "d", descriptor = "I")
    public int rotateY;

    @OriginalMember(owner = "client!afa", name = "m", descriptor = "Z")
    public boolean infinite;

    @OriginalMember(owner = "client!afa", name = "i", descriptor = "I")
    public int size = 8;

    @OriginalMember(owner = "client!afa", name = "k", descriptor = "I")
    public int colour = 0xFFFFFF;

    @OriginalMember(owner = "client!afa", name = "a", descriptor = "(ILclient!ge;I)V")
    public void decode(@OriginalArg(0) int code, @OriginalArg(1) Packet packet) {
        if (code == 1) {
            this.size = packet.g2();
        } else if (code == 2) {
            this.infinite = true;
        } else if (code == 3) {
            this.x = packet.g2s();
            this.y = packet.g2s();
            this.z = packet.g2s();
        } else if (code == 4) {
            this.renderType = packet.g1();
        } else if (code == 5) {
            this.contentId = packet.g2();
        } else if (code == 6) {
            this.colour = packet.g3();
        } else if (code == 7) {
            this.rotateX = packet.g2s();
            this.rotateY = packet.g2s();
            this.rotateZ = packet.g2s();
        }
    }

    @OriginalMember(owner = "client!afa", name = "a", descriptor = "(Lclient!ge;I)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(11) int code = packet.g1();
            if (code == 0) {
                return;
            }
            this.decode(code, packet);
        }
    }
}

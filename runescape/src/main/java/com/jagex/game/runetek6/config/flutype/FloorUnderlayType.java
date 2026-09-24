package com.jagex.game.runetek6.config.flutype;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nq")
public final class FloorUnderlayType {

    @OriginalMember(owner = "client!nq", name = "l", descriptor = "I")
    public int anInt6630;

    @OriginalMember(owner = "client!nq", name = "o", descriptor = "I")
    public int anInt6632;

    @OriginalMember(owner = "client!nq", name = "c", descriptor = "I")
    public int anInt6637;

    @OriginalMember(owner = "client!nq", name = "a", descriptor = "I")
    public int anInt6639;

    @OriginalMember(owner = "client!nq", name = "n", descriptor = "I")
    public int texture = -1;

    @OriginalMember(owner = "client!nq", name = "j", descriptor = "I")
    public int colour = 0;

    @OriginalMember(owner = "client!nq", name = "b", descriptor = "Z")
    public boolean occludes = true;

    @OriginalMember(owner = "client!nq", name = "m", descriptor = "I")
    public int size = 512;

    @OriginalMember(owner = "client!nq", name = "h", descriptor = "Z")
    public boolean allowShadow = true;

    @OriginalMember(owner = "client!nq", name = "a", descriptor = "(ILclient!ge;I)V")
    public void decode(@OriginalArg(0) int arg0, @OriginalArg(1) Packet arg1) {
        if (arg0 == 1) {
            this.colour = arg1.g3();
            this.computeHsl(this.colour);
        } else if (arg0 == 2) {
            this.texture = arg1.g2();
            if (this.texture == 65535) {
                this.texture = -1;
            }
        } else if (arg0 == 3) {
            this.size = arg1.g2() << 2;
        } else if (arg0 == 4) {
            this.allowShadow = false;
        } else if (arg0 == 5) {
            this.occludes = false;
        }
    }

    @OriginalMember(owner = "client!nq", name = "a", descriptor = "(Lclient!ge;I)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(3) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(code, packet);
        }
    }

    @OriginalMember(owner = "client!nq", name = "a", descriptor = "(II)V")
    public void computeHsl(@OriginalArg(0) int colour) {
        @Pc(12) double local12 = (double) (colour >> 16 & 0xFF) / 256.0D;
        @Pc(21) double local21 = (double) (colour >> 8 & 0xFF) / 256.0D;
        @Pc(28) double local28 = (double) (colour & 0xFF) / 256.0D;
        @Pc(30) double local30 = local12;
        if (local12 > local21) {
            local30 = local21;
        }
        if (local30 > local28) {
            local30 = local28;
        }
        @Pc(44) double local44 = local12;
        if (local21 > local12) {
            local44 = local21;
        }
        if (local28 > local44) {
            local44 = local28;
        }
        @Pc(58) double local58 = 0.0D;
        @Pc(60) double local60 = 0.0D;
        @Pc(66) double local66 = (local30 + local44) / 2.0D;
        if (local30 != local44) {
            if (local66 < 0.5D) {
                local60 = (local44 - local30) / (local30 + local44);
            }
            if (local12 == local44) {
                local58 = (local21 - local28) / (local44 - local30);
            } else if (local44 == local21) {
                local58 = (local28 - local12) / (local44 - local30) + 2.0D;
            } else if (local44 == local28) {
                local58 = (local12 - local21) / (-local30 + local44) + 4.0D;
            }
            if (local66 >= 0.5D) {
                local60 = (local44 - local30) / (2.0D - local44 - local30);
            }
        }
        this.anInt6637 = (int) (local60 * 256.0D);
        this.anInt6639 = (int) (local66 * 256.0D);
        local58 /= 6.0D;
        if (local66 > 0.5D) {
            this.anInt6632 = (int) (512.0D * ((1.0D - local66) * local60));
        } else {
            this.anInt6632 = (int) (512.0D * (local66 * local60));
        }
        if (this.anInt6639 < 0) {
            this.anInt6639 = 0;
        } else if (this.anInt6639 > 255) {
            this.anInt6639 = 255;
        }
        if (this.anInt6637 < 0) {
            this.anInt6637 = 0;
        } else if (this.anInt6637 > 255) {
            this.anInt6637 = 255;
        }
        if (this.anInt6632 < 1) {
            this.anInt6632 = 1;
        }
        this.anInt6630 = (int) (local58 * (double) this.anInt6632);
    }
}

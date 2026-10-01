package com.jagex.game.runetek6.config.flutype;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nq")
public final class FloorUnderlayType {

    /**
     * The hue multiplied by {@link #hueWeight}, so that a sum of hues over neighbouring tiles divided by the sum of
     * their weights gives a hue average that favours the more saturated colours.
     */
    @OriginalMember(owner = "client!nq", name = "l", descriptor = "I")
    public int hue;

    @OriginalMember(owner = "client!nq", name = "o", descriptor = "I")
    public int hueWeight;

    @OriginalMember(owner = "client!nq", name = "c", descriptor = "I")
    public int saturation;

    @OriginalMember(owner = "client!nq", name = "a", descriptor = "I")
    public int lightness;

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
    public void decode(@OriginalArg(0) int code, @OriginalArg(1) Packet packet) {
        if (code == 1) {
            this.colour = packet.g3();
            this.computeHsl(this.colour);
        } else if (code == 2) {
            this.texture = packet.g2();
            if (this.texture == 65535) {
                this.texture = -1;
            }
        } else if (code == 3) {
            this.size = packet.g2() << 2;
        } else if (code == 4) {
            this.allowShadow = false;
        } else if (code == 5) {
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
        @Pc(12) double red = (double) (colour >> 16 & 0xFF) / 256.0D;
        @Pc(21) double green = (double) (colour >> 8 & 0xFF) / 256.0D;
        @Pc(28) double blue = (double) (colour & 0xFF) / 256.0D;
        @Pc(30) double min = red;
        if (red > green) {
            min = green;
        }
        if (min > blue) {
            min = blue;
        }
        @Pc(44) double max = red;
        if (green > red) {
            max = green;
        }
        if (blue > max) {
            max = blue;
        }
        @Pc(58) double hue = 0.0D;
        @Pc(60) double saturation = 0.0D;
        @Pc(66) double lightness = (min + max) / 2.0D;
        if (min != max) {
            if (lightness < 0.5D) {
                saturation = (max - min) / (min + max);
            }
            if (red == max) {
                hue = (green - blue) / (max - min);
            } else if (max == green) {
                hue = (blue - red) / (max - min) + 2.0D;
            } else if (max == blue) {
                hue = (red - green) / (-min + max) + 4.0D;
            }
            if (lightness >= 0.5D) {
                saturation = (max - min) / (2.0D - max - min);
            }
        }
        this.saturation = (int) (saturation * 256.0D);
        this.lightness = (int) (lightness * 256.0D);
        hue /= 6.0D;
        if (lightness > 0.5D) {
            this.hueWeight = (int) (512.0D * ((1.0D - lightness) * saturation));
        } else {
            this.hueWeight = (int) (512.0D * (lightness * saturation));
        }
        if (this.lightness < 0) {
            this.lightness = 0;
        } else if (this.lightness > 255) {
            this.lightness = 255;
        }
        if (this.saturation < 0) {
            this.saturation = 0;
        } else if (this.saturation > 255) {
            this.saturation = 255;
        }
        if (this.hueWeight < 1) {
            this.hueWeight = 1;
        }
        this.hue = (int) (hue * (double) this.hueWeight);
    }
}

package com.jagex;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nb")
public final class AnimFrame {

    @OriginalMember(owner = "client!nb", name = "i", descriptor = "[S")
    public static final short[] tmpGroups = new short[500];

    @OriginalMember(owner = "client!nb", name = "d", descriptor = "[S")
    public static final short[] tmpX = new short[500];

    @OriginalMember(owner = "client!nb", name = "f", descriptor = "[S")
    public static final short[] tmpY = new short[500];

    @OriginalMember(owner = "client!nb", name = "q", descriptor = "[S")
    public static final short[] tmpZ = new short[500];

    @OriginalMember(owner = "client!nb", name = "a", descriptor = "[B")
    public static final byte[] tmpTweenFlags = new byte[500];

    @OriginalMember(owner = "client!nb", name = "g", descriptor = "[S")
    public static final short[] tmpOrigins = new short[500];

    @OriginalMember(owner = "client!nb", name = "l", descriptor = "Z")
    public boolean hasColourTransform = false;

    @OriginalMember(owner = "client!nb", name = "o", descriptor = "Lclient!qda;")
    public AnimBase base = null;

    @OriginalMember(owner = "client!nb", name = "e", descriptor = "Z")
    public boolean hasAlphaTransform = false;

    @OriginalMember(owner = "client!nb", name = "k", descriptor = "I")
    public int anInt6359 = 0;

    @OriginalMember(owner = "client!nb", name = "m", descriptor = "Z")
    public boolean hasBillboardTransform = false;

    @OriginalMember(owner = "client!nb", name = "b", descriptor = "[S")
    public short[] aShortArray87;

    @OriginalMember(owner = "client!nb", name = "j", descriptor = "[S")
    public short[] aShortArray93;

    @OriginalMember(owner = "client!nb", name = "p", descriptor = "[S")
    public short[] aShortArray94;

    @OriginalMember(owner = "client!nb", name = "c", descriptor = "[S")
    public short[] aShortArray89;

    @OriginalMember(owner = "client!nb", name = "h", descriptor = "[S")
    public short[] aShortArray86;

    @OriginalMember(owner = "client!nb", name = "n", descriptor = "[B")
    public byte[] aByteArray70;

    @OriginalMember(owner = "client!nb", name = "<init>", descriptor = "([BLclient!qda;)V")
    public AnimFrame(@OriginalArg(0) byte[] data, @OriginalArg(1) AnimBase base) {
        this.base = base;
        try {
            @Pc(24) Packet flags = new Packet(data);
            @Pc(29) Packet values = new Packet(data);
            flags.g1();
            flags.pos += 2;
            @Pc(43) int groupCount = flags.g1();
            @Pc(45) int count = 0;
            @Pc(47) int origin = -1;
            @Pc(49) int appliedOrigin = -1;
            values.pos = flags.pos + groupCount;
            @Pc(64) int local64;
            for (@Pc(57) int group = 0; group < groupCount; group++) {
                local64 = this.base.anIntArray619[group];
                if (local64 == 0) {
                    origin = group;
                }
                @Pc(72) int mask = flags.g1();
                if (mask > 0) {
                    if (local64 == 0) {
                        appliedOrigin = group;
                    }
                    tmpGroups[count] = (short) group;
                    @Pc(87) short defaultValue = 0;
                    if (local64 == 3 || local64 == 10) {
                        defaultValue = 128;
                    }
                    if ((mask & 0x1) == 0) {
                        tmpX[count] = defaultValue;
                    } else {
                        tmpX[count] = (short) values.gsmarts();
                    }
                    if ((mask & 0x2) == 0) {
                        tmpY[count] = defaultValue;
                    } else {
                        tmpY[count] = (short) values.gsmarts();
                    }
                    if ((mask & 0x4) == 0) {
                        tmpZ[count] = defaultValue;
                    } else {
                        tmpZ[count] = (short) values.gsmarts();
                    }
                    tmpTweenFlags[count] = (byte) (mask >>> 3 & 0x3);
                    if (local64 == 2 || local64 == 9) {
                        tmpX[count] = (short) (tmpX[count] << 2 & 0x3FFF);
                        tmpY[count] = (short) (tmpY[count] << 2 & 0x3FFF);
                        tmpZ[count] = (short) (tmpZ[count] << 2 & 0x3FFF);
                    }
                    tmpOrigins[count] = -1;
                    if (local64 == 1 || local64 == 2 || local64 == 3) {
                        if (origin > appliedOrigin) {
                            tmpOrigins[count] = (short) origin;
                            appliedOrigin = origin;
                        }
                    } else if (local64 == 5) {
                        this.hasAlphaTransform = true;
                    } else if (local64 == 7) {
                        this.hasColourTransform = true;
                    } else if (local64 == 9 || local64 == 10 || local64 == 8) {
                        this.hasBillboardTransform = true;
                    }
                    count++;
                }
            }
            if (values.pos != data.length) {
                throw new RuntimeException();
            }
            this.anInt6359 = count;
            this.aShortArray87 = new short[count];
            this.aShortArray93 = new short[count];
            this.aShortArray94 = new short[count];
            this.aShortArray89 = new short[count];
            this.aShortArray86 = new short[count];
            this.aByteArray70 = new byte[count];
            for (local64 = 0; local64 < count; local64++) {
                this.aShortArray87[local64] = tmpGroups[local64];
                this.aShortArray93[local64] = tmpX[local64];
                this.aShortArray94[local64] = tmpY[local64];
                this.aShortArray89[local64] = tmpZ[local64];
                this.aShortArray86[local64] = tmpOrigins[local64];
                this.aByteArray70[local64] = tmpTweenFlags[local64];
            }
        } catch (@Pc(359) Exception ex) {
            this.anInt6359 = 0;
            this.hasAlphaTransform = false;
            this.hasColourTransform = false;
        }
    }
}

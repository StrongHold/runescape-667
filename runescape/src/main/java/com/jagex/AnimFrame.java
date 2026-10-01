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
    public int transformCount = 0;

    @OriginalMember(owner = "client!nb", name = "m", descriptor = "Z")
    public boolean hasBillboardTransform = false;

    @OriginalMember(owner = "client!nb", name = "b", descriptor = "[S")
    public short[] groups;

    @OriginalMember(owner = "client!nb", name = "j", descriptor = "[S")
    public short[] xValues;

    @OriginalMember(owner = "client!nb", name = "p", descriptor = "[S")
    public short[] yValues;

    @OriginalMember(owner = "client!nb", name = "c", descriptor = "[S")
    public short[] zValues;

    @OriginalMember(owner = "client!nb", name = "h", descriptor = "[S")
    public short[] origins;

    @OriginalMember(owner = "client!nb", name = "n", descriptor = "[B")
    public byte[] tweenFlags;

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
            @Pc(64) int type;
            for (@Pc(57) int group = 0; group < groupCount; group++) {
                type = this.base.transformTypes[group];
                if (type == 0) {
                    origin = group;
                }
                @Pc(72) int mask = flags.g1();
                if (mask > 0) {
                    if (type == 0) {
                        appliedOrigin = group;
                    }
                    tmpGroups[count] = (short) group;
                    @Pc(87) short defaultValue = 0;
                    if (type == 3 || type == 10) {
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
                    if (type == 2 || type == 9) {
                        tmpX[count] = (short) (tmpX[count] << 2 & 0x3FFF);
                        tmpY[count] = (short) (tmpY[count] << 2 & 0x3FFF);
                        tmpZ[count] = (short) (tmpZ[count] << 2 & 0x3FFF);
                    }
                    tmpOrigins[count] = -1;
                    if (type == 1 || type == 2 || type == 3) {
                        if (origin > appliedOrigin) {
                            tmpOrigins[count] = (short) origin;
                            appliedOrigin = origin;
                        }
                    } else if (type == 5) {
                        this.hasAlphaTransform = true;
                    } else if (type == 7) {
                        this.hasColourTransform = true;
                    } else if (type == 9 || type == 10 || type == 8) {
                        this.hasBillboardTransform = true;
                    }
                    count++;
                }
            }
            if (values.pos != data.length) {
                throw new RuntimeException();
            }
            this.transformCount = count;
            this.groups = new short[count];
            this.xValues = new short[count];
            this.yValues = new short[count];
            this.zValues = new short[count];
            this.origins = new short[count];
            this.tweenFlags = new byte[count];
            for (type = 0; type < count; type++) {
                this.groups[type] = tmpGroups[type];
                this.xValues[type] = tmpX[type];
                this.yValues[type] = tmpY[type];
                this.zValues[type] = tmpZ[type];
                this.origins[type] = tmpOrigins[type];
                this.tweenFlags[type] = tmpTweenFlags[type];
            }
        } catch (@Pc(359) Exception ex) {
            this.transformCount = 0;
            this.hasAlphaTransform = false;
            this.hasColourTransform = false;
        }
    }
}

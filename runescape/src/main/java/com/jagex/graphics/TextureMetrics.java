package com.jagex.graphics;

import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!fa")
public final class TextureMetrics {

    @OriginalMember(owner = "client!fa", name = "r", descriptor = "I")
    public int colorOp;

    @OriginalMember(owner = "client!fa", name = "j", descriptor = "B")
    public byte speedV;

    @OriginalMember(owner = "client!fa", name = "a", descriptor = "Z")
    public boolean small;

    @OriginalMember(owner = "client!fa", name = "c", descriptor = "Z")
    public boolean skipFaces;

    @OriginalMember(owner = "client!fa", name = "f", descriptor = "I")
    public int effectParam2;

    @OriginalMember(owner = "client!fa", name = "z", descriptor = "B")
    public byte mipmap;

    @OriginalMember(owner = "client!fa", name = "u", descriptor = "Z")
    public boolean repeatsV;

    @OriginalMember(owner = "client!fa", name = "x", descriptor = "B")
    public byte speedU;

    @OriginalMember(owner = "client!fa", name = "A", descriptor = "Z")
    public boolean repeatsU;

    @OriginalMember(owner = "client!fa", name = "e", descriptor = "B")
    public byte effectParam1;

    @OriginalMember(owner = "client!fa", name = "w", descriptor = "B")
    public byte effectType;

    @OriginalMember(owner = "client!fa", name = "d", descriptor = "Z")
    public boolean hdr;

    @OriginalMember(owner = "client!fa", name = "o", descriptor = "Z")
    public boolean transposed;

    @OriginalMember(owner = "client!fa", name = "i", descriptor = "B")
    public byte brightness;

    @OriginalMember(owner = "client!fa", name = "h", descriptor = "S")
    public short averageColour;

    /**
     * Decoded and handed to the native toolkit with the rest of the metrics, but nothing in this
     * build reads it.
     */
    @OriginalMember(owner = "client!fa", name = "p", descriptor = "Z")
    public boolean unusedFlag;

    @OriginalMember(owner = "client!fa", name = "t", descriptor = "Z")
    public boolean disableable;

    @OriginalMember(owner = "client!fa", name = "m", descriptor = "B")
    public byte alpha;

    @OriginalMember(owner = "client!fa", name = "g", descriptor = "I")
    public int alphaBlendMode;
}

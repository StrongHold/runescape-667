package com.jagex.graphics.texture;

import com.jagex.core.datastruct.key.Node;
import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.ColourImageCache;
import com.jagex.graphics.MonochromeImageCache;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pf")
public abstract class TextureOp extends Node {

    @OriginalMember(owner = "client!pf", name = "l", descriptor = "I")
    public int cacheSize;

    @OriginalMember(owner = "client!pf", name = "D", descriptor = "Lclient!ug;")
    protected ColourImageCache colourCache;

    @OriginalMember(owner = "client!pf", name = "p", descriptor = "Lclient!ija;")
    protected MonochromeImageCache monochromeCache;

    @OriginalMember(owner = "client!pf", name = "v", descriptor = "Z")
    public boolean monochrome;

    @OriginalMember(owner = "client!pf", name = "w", descriptor = "[Lclient!pf;")
    public final TextureOp[] ops;

    @OriginalMember(owner = "client!pf", name = "<init>", descriptor = "(IZ)V")
    protected TextureOp(@OriginalArg(0) int count, @OriginalArg(1) boolean monochrome) {
        this.monochrome = monochrome;
        this.ops = new TextureOp[count];
    }

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(Z[FI)[F")
    public static float[] method9420(@OriginalArg(0) boolean arg0, @OriginalArg(1) float[] arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            return null;
        } else {
            @Pc(12) float[] local12 = new float[arg2];
            Arrays.copy(arg1, 0, local12, 0, arg2);
            return local12;
        }
    }

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(II)[I")
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        if (arg0 >= 107) {
            throw new IllegalStateException("This operation does not have a monochrome output");
        } else {
            return null;
        }
    }

    @OriginalMember(owner = "client!pf", name = "b", descriptor = "(B)I")
    public int getSpriteId() {
        return -1;
    }

    @OriginalMember(owner = "client!pf", name = "b", descriptor = "(III)[[I")
    protected final int[][] method9413(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        if (this.ops[arg0].monochrome) {
            @Pc(36) int[] local36 = this.ops[arg0].monochromeOutput(111, arg1);
            return new int[][]{local36, local36, local36};
        } else {
            return this.ops[arg0].method9414(arg1);
        }
    }

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(IZ)[[I")
    public int[][] method9414(@OriginalArg(0) int arg0) {
        throw new IllegalStateException("This operation does not have a colour output");
    }

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(ZLclient!ge;I)V")
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            method9420(true, null, 33);
        }
    }

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(IIZ)V")
    public void initCache(@OriginalArg(0) int height, @OriginalArg(1) int width) {
        @Pc(22) int size = this.cacheSize == 255 ? height : this.cacheSize;
        if (this.monochrome) {
            this.monochromeCache = new MonochromeImageCache(size, height, width);
        } else {
            this.colourCache = new ColourImageCache(size, height, width);
        }
    }

    @OriginalMember(owner = "client!pf", name = "d", descriptor = "(I)I")
    public int getTextureId() {
        return -1;
    }

    @OriginalMember(owner = "client!pf", name = "c", descriptor = "(I)V")
    public void postDecode() {
    }

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(III)[I")
    protected final int[] method9422(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return this.ops[arg1].monochrome ? this.ops[arg1].monochromeOutput(120, arg0) : this.ops[arg1].method9414(arg0)[0];
    }

    @OriginalMember(owner = "client!pf", name = "b", descriptor = "(I)V")
    public void cacheReset() {
        if (this.monochrome) {
            this.monochromeCache.reset();
            this.monochromeCache = null;
        } else {
            this.colourCache.reset();
            this.colourCache = null;
        }
    }
}

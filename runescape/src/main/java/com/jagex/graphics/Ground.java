package com.jagex.graphics;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!s")
public abstract class Ground {

    @OriginalMember(owner = "client!s", name = "t", descriptor = "I")
    public final int sizeX;

    @OriginalMember(owner = "client!s", name = "s", descriptor = "I")
    public final int sizeZ;

    @OriginalMember(owner = "client!s", name = "e", descriptor = "I")
    public final int tileSize;

    @OriginalMember(owner = "client!s", name = "d", descriptor = "I")
    public final int tileSizeShift;

    @OriginalMember(owner = "client!s", name = "i", descriptor = "[[I")
    public final int[][] tileHeights;

    @OriginalMember(owner = "client!s", name = "<init>", descriptor = "(III[[I)V")
    protected Ground(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[][] arg3) {
        this.sizeX = arg0;
        this.sizeZ = arg1;
        @Pc(11) int local11 = 0;
        while (arg2 > 1) {
            local11++;
            arg2 >>= 0x1;
        }
        this.tileSize = 0x1 << local11;
        this.tileSizeShift = local11;
        this.tileHeights = arg3;
    }

    @OriginalMember(owner = "client!s", name = "wa", descriptor = "(Lclient!r;IIIIZ)V")
    public abstract void wa(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5);

    @OriginalMember(owner = "client!s", name = "CA", descriptor = "(Lclient!r;IIIIZ)V")
    public abstract void CA(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5);

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(Lclient!lca;[I)V")
    public abstract void method7868(@OriginalArg(0) PointLight arg0, @OriginalArg(1) int[] arg1);

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(IIB)I")
    public final int getHeight(@OriginalArg(1) int x, @OriginalArg(0) int z) {
        return this.tileHeights[x][z];
    }

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(III[[ZZI)V")
    public abstract void renderTiles(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean[][] arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5);

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(II[I[I[I[I[I[I[I[I[I[I[IIIIZ)V")
    public abstract void addTile(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int[] arg4, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int[] arg7, @OriginalArg(8) int[] arg8, @OriginalArg(9) int[] arg9, @OriginalArg(10) int[] arg10, @OriginalArg(11) int[] arg11, @OriginalArg(12) int[] arg12, @OriginalArg(13) int arg13, @OriginalArg(14) int arg14, @OriginalArg(15) int arg15);

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(IIIIIII[[Z)V")
    public abstract void drawMinimap(@OriginalArg(3) int x1, @OriginalArg(4) int y1, @OriginalArg(5) int x2, @OriginalArg(6) int y2, @OriginalArg(7) boolean[][] visibility);

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(Lclient!r;IIIIZ)Z")
    public abstract boolean method7874(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!s", name = "ka", descriptor = "(III)V")
    public abstract void ka(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2);

    @OriginalMember(owner = "client!s", name = "fa", descriptor = "(IILclient!r;)Lclient!r;")
    public abstract Shadow fa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Shadow arg2);

    @OriginalMember(owner = "client!s", name = "U", descriptor = "(II[I[I[I[I[I[I[I[IIIIZ)V")
    public abstract void U(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int[] arg4, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int[] arg7, @OriginalArg(8) int[] arg8, @OriginalArg(9) int[] arg9, @OriginalArg(10) int arg10, @OriginalArg(11) int arg11, @OriginalArg(12) int arg12, @OriginalArg(13) boolean arg13);

    @OriginalMember(owner = "client!s", name = "YA", descriptor = "()V")
    public abstract void YA();

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(II)V")
    public abstract void renderTile(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1);

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(III[[ZZII)V")
    public abstract void renderTilesAtDepth(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean[][] arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6);

    @OriginalMember(owner = "client!s", name = "a", descriptor = "(III)I")
    public final int averageHeight(@OriginalArg(2) int x, @OriginalArg(0) int z) {
        @Pc(8) int x1 = x >> this.tileSizeShift;
        @Pc(13) int z1 = z >> this.tileSizeShift;
        if (x1 < 0 || z1 < 0 || this.sizeX - 1 < x1 || this.sizeZ - 1 < z1) {
            return 0;
        }
        @Pc(52) int local52 = this.tileSize - 1 & x;
        @Pc(70) int local70 = this.tileSize - 1 & z;
        @Pc(96) int local96 = local52 * this.tileHeights[x1 + 1][z1] + (this.tileSize - local52) * this.tileHeights[x1][z1] >> this.tileSizeShift;
        @Pc(128) int local128 = this.tileHeights[x1][z1 + 1] * (this.tileSize - local52) + local52 * this.tileHeights[x1 + 1][z1 + 1] >> this.tileSizeShift;
        return (this.tileSize - local70) * local96 + local70 * local128 >> this.tileSizeShift;
    }
}

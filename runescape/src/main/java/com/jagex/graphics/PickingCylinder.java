package com.jagex.graphics;

import com.jagex.core.datastruct.Node;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ima")
public final class PickingCylinder extends Node {

    @OriginalMember(owner = "client!ima", name = "f", descriptor = "I")
    public int bottomX;

    @OriginalMember(owner = "client!ima", name = "k", descriptor = "I")
    public int radiusPixels;

    @OriginalMember(owner = "client!ima", name = "i", descriptor = "I")
    public int bottomY;

    @OriginalMember(owner = "client!ima", name = "j", descriptor = "I")
    public int topX;

    @OriginalMember(owner = "client!ima", name = "g", descriptor = "I")
    public int topY;

    @OriginalMember(owner = "client!ima", name = "h", descriptor = "Z")
    public boolean visible = false;

    @OriginalMember(owner = "client!ima", name = "a", descriptor = "(II)Z")
    public boolean method4048(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        if (!this.visible) {
            return false;
        }
        @Pc(10) int local10 = this.bottomX - this.topX;
        @Pc(16) int local16 = this.bottomY - this.topY;
        @Pc(24) int local24 = local10 * local10 + local16 * local16;
        @Pc(42) int local42 = arg0 * local10 + arg1 * local16 - this.topX * local10 - this.topY * local16;
        @Pc(49) int local49;
        @Pc(54) int local54;
        if (local42 <= 0) {
            local49 = this.topX - arg0;
            local54 = this.topY - arg1;
            return local49 * local49 + local54 * local54 < this.radiusPixels * this.radiusPixels;
        } else if (local42 > local24) {
            local49 = this.bottomX - arg0;
            local54 = this.bottomY - arg1;
            return local49 * local49 + local54 * local54 < this.radiusPixels * this.radiusPixels;
        } else {
            local42 = (local42 << 10) / local24;
            local49 = this.topX + (local10 * local42 >> 10) - arg0;
            local54 = this.topY + (local16 * local42 >> 10) - arg1;
            return local49 * local49 + local54 * local54 < this.radiusPixels * this.radiusPixels;
        }
    }
}

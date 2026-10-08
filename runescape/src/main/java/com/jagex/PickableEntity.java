package com.jagex;

import com.jagex.Entity;
import com.jagex.core.datastruct.Node;
import com.jagex.graphics.PickingCylinder;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pea")
public final class PickableEntity extends Node {

    @OriginalMember(owner = "client!pea", name = "g", descriptor = "Lclient!eo;")
    public Entity aEntity_18;

    @OriginalMember(owner = "client!pea", name = "f", descriptor = "Z")
    public boolean interactive;

    @OriginalMember(owner = "client!pea", name = "j", descriptor = "[Lclient!ima;")
    public PickingCylinder[] pickingCylinders;

    @OriginalMember(owner = "client!pea", name = "a", descriptor = "(Lclient!ha;BII)Z")
    public boolean picked(@OriginalArg(0) Toolkit arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        @Pc(10) int local10 = this.aEntity_18.getPickSizeShift();
        if (this.pickingCylinders != null) {
            for (@Pc(15) int local15 = 0; local15 < this.pickingCylinders.length; local15++) {
                this.pickingCylinders[local15].radiusPixels <<= local10;
                if (this.pickingCylinders[local15].method4048(arg2, arg1) && this.aEntity_18.picked(arg1, arg2, false, arg0)) {
                    this.pickingCylinders[local15].radiusPixels >>= local10;
                    return true;
                }
                this.pickingCylinders[local15].radiusPixels >>= local10;
            }
        }
        return false;
    }
}

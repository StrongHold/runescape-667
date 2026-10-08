package com.jagex;

import com.jagex.core.datastruct.Node;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!eo")
public abstract class Entity extends Node {

    @OriginalMember(owner = "client!eo", name = "g", descriptor = "B")
    public byte virtualLevel;

    @OriginalMember(owner = "client!eo", name = "j", descriptor = "B")
    public byte level;

    @OriginalMember(owner = "client!eo", name = "f", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!eo", name = "q", descriptor = "Z")
    public boolean aBoolean812;

    @OriginalMember(owner = "client!eo", name = "r", descriptor = "I")
    public int y;

    @OriginalMember(owner = "client!eo", name = "u", descriptor = "I")
    public int screenX;

    @OriginalMember(owner = "client!eo", name = "v", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!eo", name = "t", descriptor = "Lclient!eo;")
    public Entity nextEntity;

    @OriginalMember(owner = "client!eo", name = "i", descriptor = "I")
    public int screenDepth;

    @OriginalMember(owner = "client!eo", name = "n", descriptor = "I")
    public int screenY;

    @OriginalMember(owner = "client!eo", name = "k", descriptor = "Z")
    public boolean aBoolean813 = false;

    @OriginalMember(owner = "client!eo", name = "<init>", descriptor = "()V")
    protected Entity() {
    }

    @OriginalMember(owner = "client!eo", name = "g", descriptor = "(I)Z")
    public abstract boolean method9275();

    @OriginalMember(owner = "client!eo", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    public abstract PickableEntity render(@OriginalArg(1) Toolkit arg0);

    @OriginalMember(owner = "client!eo", name = "a", descriptor = "(I[Lclient!lca;II)I")
    protected final int findLightsAt(@OriginalArg(1) PointLight[] pointLights, @OriginalArg(2) int z, @OriginalArg(3) int x) {
        @Pc(21) long flags = Client.tileLightFlags[this.level][x][z];
        @Pc(25) int n = 0;

        for (@Pc(23) long shift = 0L; shift <= 48L; shift += 16L) {
            @Pc(35) int id = (int) (flags >> (int) shift & 0xFFFFL);
            if (id <= 0) {
                break;
            }

            pointLights[n++] = EnvironmentLight.lights[id - 1].light;
        }

        for (@Pc(35) int level = n; level < 4; level++) {
            pointLights[level] = null;
        }

        return n;
    }

    @OriginalMember(owner = "client!eo", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    public abstract BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1);

    @OriginalMember(owner = "client!eo", name = "a", descriptor = "(IIZLclient!ha;)Z")
    public abstract boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit);

    @OriginalMember(owner = "client!eo", name = "j", descriptor = "(I)V")
    public abstract void stopSharingLight(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!eo", name = "h", descriptor = "(I)Z")
    public abstract boolean isTransparent(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!eo", name = "b", descriptor = "(B)Z")
    public abstract boolean isStationary();

    @OriginalMember(owner = "client!eo", name = "a", descriptor = "(BLclient!ha;)Z")
    public abstract boolean method9284(@OriginalArg(0) byte arg0, @OriginalArg(1) Toolkit arg1);

    @OriginalMember(owner = "client!eo", name = "a", descriptor = "(IZLclient!ha;IBILclient!eo;)V")
    public abstract void shareLight(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) Entity arg6);

    @OriginalMember(owner = "client!eo", name = "k", descriptor = "(I)I")
    public abstract int getMinY(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!eo", name = "d", descriptor = "(B)I")
    public int getPickSizeShift() {
        return 0;
    }

    @OriginalMember(owner = "client!eo", name = "a", descriptor = "([Lclient!lca;I)I")
    public abstract int findLights(@OriginalArg(0) PointLight[] arg0);

    @OriginalMember(owner = "client!eo", name = "d", descriptor = "(Lclient!ha;I)V")
    public abstract void method9289(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1);

    @OriginalMember(owner = "client!eo", name = "i", descriptor = "(I)Z")
    public abstract boolean method9290(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!eo", name = "c", descriptor = "(B)I")
    public abstract int getSphereRadius(@OriginalArg(0) byte arg0);
}

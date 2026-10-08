package com.jagex;

import com.jagex.core.datastruct.key.Node;
import com.jagex.game.runetek6.config.loctype.LocTypeCustomisation;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!hma")
public final class ChangeLocationRequest extends Node {

    @OriginalMember(owner = "client!hma", name = "u", descriptor = "I")
    public int originalId;

    @OriginalMember(owner = "client!hma", name = "D", descriptor = "Lclient!gp;")
    public LocTypeCustomisation customisation;

    @OriginalMember(owner = "client!hma", name = "t", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!hma", name = "s", descriptor = "I")
    public int originalShape;

    @OriginalMember(owner = "client!hma", name = "o", descriptor = "I")
    public int level;

    @OriginalMember(owner = "client!hma", name = "z", descriptor = "I")
    public int layer;

    @OriginalMember(owner = "client!hma", name = "w", descriptor = "I")
    public int rotation;

    @OriginalMember(owner = "client!hma", name = "p", descriptor = "I")
    public int shape;

    @OriginalMember(owner = "client!hma", name = "q", descriptor = "I")
    public int id;

    @OriginalMember(owner = "client!hma", name = "y", descriptor = "I")
    public int originalRotation;

    @OriginalMember(owner = "client!hma", name = "m", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!hma", name = "r", descriptor = "Z")
    public boolean pendingRemoval = false;

    @OriginalMember(owner = "client!hma", name = "C", descriptor = "Z")
    public boolean pendingApply = true;
}

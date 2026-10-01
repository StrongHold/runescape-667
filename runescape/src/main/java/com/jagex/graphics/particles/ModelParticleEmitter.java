package com.jagex.graphics.particles;

import com.jagex.game.runetek6.config.emittertype.ParticleEmitterType;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterTypeList;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!rv")
public final class ModelParticleEmitter {

    @OriginalMember(owner = "client!rv", name = "d", descriptor = "I")
    public int transformedAY;

    @OriginalMember(owner = "client!rv", name = "g", descriptor = "I")
    public int transformedCY;

    @OriginalMember(owner = "client!rv", name = "c", descriptor = "I")
    public int transformedAZ;

    @OriginalMember(owner = "client!rv", name = "x", descriptor = "I")
    public int transformedBY;

    @OriginalMember(owner = "client!rv", name = "u", descriptor = "I")
    public int transformedBZ;

    @OriginalMember(owner = "client!rv", name = "k", descriptor = "I")
    public int transformedCX;

    @OriginalMember(owner = "client!rv", name = "m", descriptor = "Lclient!rv;")
    public ModelParticleEmitter next;

    @OriginalMember(owner = "client!rv", name = "q", descriptor = "I")
    public int transformedBX;

    @OriginalMember(owner = "client!rv", name = "n", descriptor = "I")
    public int transformedAX;

    @OriginalMember(owner = "client!rv", name = "t", descriptor = "I")
    public int transformedCZ;

    @OriginalMember(owner = "client!rv", name = "b", descriptor = "B")
    public final byte priority;

    @OriginalMember(owner = "client!rv", name = "j", descriptor = "I")
    public final int vertexA;

    @OriginalMember(owner = "client!rv", name = "e", descriptor = "I")
    public final int vertexB;

    @OriginalMember(owner = "client!rv", name = "v", descriptor = "I")
    public final int vertexC;

    @OriginalMember(owner = "client!rv", name = "o", descriptor = "I")
    public final int id;

    @OriginalMember(owner = "client!rv", name = "<init>", descriptor = "(IIIIB)V")
    public ModelParticleEmitter(@OriginalArg(0) int id, @OriginalArg(1) int vertexA, @OriginalArg(2) int vertexB, @OriginalArg(3) int vertexC, @OriginalArg(4) byte priority) {
        this.priority = priority;
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.vertexC = vertexC;
        this.id = id;
    }

    @OriginalMember(owner = "client!rv", name = "d", descriptor = "(I)Lclient!vaa;")
    public ParticleEmitterType getType() {
        return ParticleEmitterTypeList.get(this.id);
    }

    @OriginalMember(owner = "client!rv", name = "a", descriptor = "(ZIII)Lclient!rv;")
    public ModelParticleEmitter copy(@OriginalArg(1) int vertexA, @OriginalArg(2) int vertexB, @OriginalArg(3) int vertexC) {
        return new ModelParticleEmitter(this.id, vertexA, vertexB, vertexC, this.priority);
    }
}

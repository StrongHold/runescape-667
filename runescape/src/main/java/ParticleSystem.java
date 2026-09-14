import com.jagex.ParticleList;
import com.jagex.core.datastruct.Node;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.particles.ParticleLimits;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!hv")
public final class ParticleSystem extends Node {

    @OriginalMember(owner = "client!hv", name = "t", descriptor = "[Z")
    public static final boolean[] activeEmitters = new boolean[32];

    @OriginalMember(owner = "client!hv", name = "f", descriptor = "[Z")
    public static final boolean[] activeEffectors = new boolean[8];

    @OriginalMember(owner = "client!hv", name = "b", descriptor = "(IZ)Lclient!hv;")
    public static ParticleSystem create(@OriginalArg(0) int clock, @OriginalArg(1) boolean arg1) {
        if (ParticleManager.systemFreePtr == ParticleManager.systemNextPtr) {
            return new ParticleSystem(clock, arg1);
        } else {
            @Pc(6) ParticleSystem system = ParticleManager.systems[ParticleManager.systemNextPtr];
            ParticleManager.systemNextPtr = ParticleManager.systemNextPtr + 1 & ParticleLimits.SYSTEMS[ParticleManager.option];
            system.init(clock, arg1);
            return system;
        }
    }

    @OriginalMember(owner = "client!hv", name = "u", descriptor = "J")
    public long lastTick;

    @OriginalMember(owner = "client!hv", name = "m", descriptor = "J")
    public long clock;

    @OriginalMember(owner = "client!hv", name = "i", descriptor = "I")
    public int level;

    @OriginalMember(owner = "client!hv", name = "q", descriptor = "Z")
    public boolean removed = false;

    @OriginalMember(owner = "client!hv", name = "s", descriptor = "Z")
    public boolean stopped = false;

    @OriginalMember(owner = "client!hv", name = "l", descriptor = "I")
    public int nextParticleSlot = 0;

    @OriginalMember(owner = "client!hv", name = "h", descriptor = "Lclient!fla;")
    public LinkedList emitterCache = new LinkedList();

    @OriginalMember(owner = "client!hv", name = "o", descriptor = "I")
    public int emitterCount = 0;

    @OriginalMember(owner = "client!hv", name = "k", descriptor = "Lclient!sia;")
    public Deque effectorCache = new Deque();

    @OriginalMember(owner = "client!hv", name = "j", descriptor = "Z")
    public boolean awaitingStartup = false;

    @OriginalMember(owner = "client!hv", name = "g", descriptor = "Z")
    public boolean aBoolean325 = false;

    @OriginalMember(owner = "client!hv", name = "r", descriptor = "I")
    public int effectorCount = 0;

    @OriginalMember(owner = "client!hv", name = "p", descriptor = "Lclient!lk;")
    public final ParticleList list = new ParticleList();

    @OriginalMember(owner = "client!hv", name = "n", descriptor = "[Lclient!pp;")
    public final MovingParticle[] movingParticles = new MovingParticle[8192];

    @OriginalMember(owner = "client!hv", name = "<init>", descriptor = "(IZ)V")
    public ParticleSystem(@OriginalArg(0) int clock, @OriginalArg(1) boolean arg1) {
        this.init(clock, arg1);
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "(Lclient!ha;J[Lclient!rv;[Lclient!mn;Z)V")
    public void update(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) long clock, @OriginalArg(2) ModelParticleEmitter[] emitters, @OriginalArg(3) ModelParticleEffector[] effectors) {
        if (!this.removed) {
            this.updateEmitters(toolkit, emitters);
            this.updateEffectors(effectors);
            this.clock = clock;
        }
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "()V")
    public void stopped() {
        this.stopped = true;
    }

    @OriginalMember(owner = "client!hv", name = "e", descriptor = "()Lclient!lk;")
    public ParticleList getList() {
        return this.list;
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "(Lclient!ha;)V")
    public void method3646(@OriginalArg(0) Toolkit toolkit) {
        this.list.particles.clear();
        for (@Pc(10) ParticleEmitter emitter = (ParticleEmitter) this.emitterCache.first(); emitter != null; emitter = (ParticleEmitter) this.emitterCache.next()) {
            emitter.collideParticles(this.lastTick, toolkit);
        }
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "([Lclient!mn;Z)V")
    public void updateEffectors(@OriginalArg(0) ModelParticleEffector[] effectors) {
        for (@Pc(1) int i = 0; i < 8; i++) {
            activeEffectors[i] = false;
        }

        label71:
        for (@Pc(16) ParticleEffector effector = (ParticleEffector) this.effectorCache.first(); effector != null; effector = (ParticleEffector) this.effectorCache.next()) {
            if (effectors != null) {
                for (@Pc(21) int i = 0; i < effectors.length; i++) {
                    if (effector.model == effectors[i] || effector.model == effectors[i].next) {
                        activeEffectors[i] = true;
                        effector.update();
                        continue label71;
                    }
                }
            }

            effector.unlink();
            this.effectorCount--;

            if (effector.isLinked2()) {
                effector.unlink2();
                ParticleManager.effectorCount--;
            }
        }

        if (effectors == null) {
            return;
        }

        for (@Pc(21) int i = 0; i < effectors.length && i != 8 && this.effectorCount != 8; i++) {
            if (!activeEffectors[i]) {
                @Pc(96) ParticleEffector effector = null;
                if (effectors[i].type().visibility == 1 && ParticleManager.effectorCount < 32) {
                    effector = new ParticleEffector(effectors[i], this);
                    ParticleManager.effectorsCache.put(effector, effectors[i].type);
                    ParticleManager.effectorCount++;
                }
                if (effector == null) {
                    effector = new ParticleEffector(effectors[i], this);
                }
                this.effectorCache.addLast(effector);
                this.effectorCount++;
                activeEffectors[i] = true;
            }
        }
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "(J)V")
    public void setClock(@OriginalArg(0) long clock) {
        this.clock = clock;
    }

    @OriginalMember(owner = "client!hv", name = "b", descriptor = "()Lclient!lk;")
    public ParticleList method3650() {
        this.list.particles.clear();
        for (@Pc(6) int i = 0; i < this.movingParticles.length; i++) {
            if (this.movingParticles[i] != null && this.movingParticles[i].emitter != null) {
                this.list.particles.add(this.movingParticles[i]);
            }
        }
        return this.list;
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "(Lclient!ha;[Lclient!rv;Z)V")
    public void updateEmitters(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) ModelParticleEmitter[] emitters) {
        for (@Pc(1) int i = 0; i < 32; i++) {
            activeEmitters[i] = false;
        }

        label62:
        for (@Pc(16) ParticleEmitter emitter = (ParticleEmitter) this.emitterCache.first(); emitter != null; emitter = (ParticleEmitter) this.emitterCache.next()) {
            if (emitters != null) {
                for (@Pc(21) int i = 0; i < emitters.length; i++) {
                    if (emitter.model == emitters[i] || emitter.model == emitters[i].next) {
                        activeEmitters[i] = true;
                        emitter.updateTriangle();
                        emitter.inactive = false;
                        continue label62;
                    }
                }
            }

            if (emitter.particleCount == 0) {
                emitter.unlink();
                this.emitterCount--;
            } else {
                emitter.inactive = true;
            }
        }

        if (emitters == null) {
            return;
        }

        for (@Pc(21) int i = 0; i < emitters.length && i != 32 && this.emitterCount != 32; i++) {
            if (!activeEmitters[i]) {
                @Pc(104) ParticleEmitter emitter = new ParticleEmitter(toolkit, emitters[i], this, this.clock);
                this.emitterCache.add(emitter);
                this.emitterCount++;
                activeEmitters[i] = true;
            }
        }
    }

    @OriginalMember(owner = "client!hv", name = "d", descriptor = "()V")
    public void remove() {
        this.removed = true;

        for (@Pc(8) ParticleEffector effector = (ParticleEffector) this.effectorCache.first(); effector != null; effector = (ParticleEffector) this.effectorCache.next()) {
            if (effector.type.visibility == 1) {
                effector.unlink2();
            }
        }

        for (@Pc(27) int i = 0; i < this.movingParticles.length; i++) {
            if (this.movingParticles[i] != null) {
                this.movingParticles[i].remove();
                this.movingParticles[i] = null;
            }
        }

        this.nextParticleSlot = 0;
        this.emitterCache = new LinkedList();
        this.emitterCount = 0;
        this.effectorCache = new Deque();
        this.effectorCount = 0;
        this.unlink();
        ParticleManager.systems[ParticleManager.systemFreePtr] = this;
        ParticleManager.systemFreePtr = ParticleManager.systemFreePtr + 1 & ParticleLimits.SYSTEMS[ParticleManager.option];
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "(Lclient!ha;J)Z")
    public boolean tick(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) long time) {
        if (this.clock == this.lastTick) {
            this.running();
        } else {
            this.stopped();
        }

        if (time - this.clock > 750L) {
            this.remove();
            return false;
        }

        @Pc(27) int elapsedTime = (int) (time - this.lastTick);
        @Pc(36) ParticleEmitter emitter;

        if (this.awaitingStartup) {
            for (emitter = (ParticleEmitter) this.emitterCache.first(); emitter != null; emitter = (ParticleEmitter) this.emitterCache.next()) {
                for (@Pc(39) int i = 0; i < emitter.type.startupTicks; i++) {
                    emitter.tick(1, !this.stopped, time, toolkit);
                }
            }
            this.awaitingStartup = false;
        }

        for (emitter = (ParticleEmitter) this.emitterCache.first(); emitter != null; emitter = (ParticleEmitter) this.emitterCache.next()) {
            emitter.tick(elapsedTime, !this.stopped, time, toolkit);
        }

        this.lastTick = time;
        return true;
    }

    @OriginalMember(owner = "client!hv", name = "c", descriptor = "()V")
    public void running() {
        this.stopped = false;
    }

    @OriginalMember(owner = "client!hv", name = "f", descriptor = "()V")
    public void restart() {
        this.awaitingStartup = true;
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "(IZ)V")
    public void init(@OriginalArg(0) int clock, @OriginalArg(1) boolean arg1) {
        ParticleManager.systemsCache.add(this);
        this.clock = clock;
        this.lastTick = clock;
        this.awaitingStartup = true;
        this.aBoolean325 = arg1;
    }

    @OriginalMember(owner = "client!hv", name = "a", descriptor = "(IIIII)V")
    public void updateBounds(@OriginalArg(0) int level, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4) {
        this.level = level;
    }
}

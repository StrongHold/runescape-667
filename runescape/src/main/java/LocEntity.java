import com.jagex.Entity;
import com.jagex.core.constants.LocShapes;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeCustomisation;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.Model;
import com.jagex.graphics.ModelAndShadow;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!sh")
public final class LocEntity {

    @OriginalMember(owner = "client!sh", name = "t", descriptor = "I")
    public int clock;

    @OriginalMember(owner = "client!sh", name = "G", descriptor = "Lclient!gp;")
    public LocTypeCustomisation customisation;

    @OriginalMember(owner = "client!sh", name = "r", descriptor = "Lclient!hv;")
    public ParticleSystem particleSystem;

    @OriginalMember(owner = "client!sh", name = "g", descriptor = "Lclient!ka;")
    public Model model;

    @OriginalMember(owner = "client!sh", name = "d", descriptor = "Lclient!r;")
    public Shadow shadow;

    @OriginalMember(owner = "client!sh", name = "b", descriptor = "[Z")
    public boolean[] aBooleanArray27;

    @OriginalMember(owner = "client!sh", name = "J", descriptor = "Z")
    public boolean animated = false;

    @OriginalMember(owner = "client!sh", name = "o", descriptor = "I")
    public int sphereRadius = 0;

    @OriginalMember(owner = "client!sh", name = "A", descriptor = "I")
    public int anInt8647 = -1;

    @OriginalMember(owner = "client!sh", name = "i", descriptor = "Z")
    public boolean aBoolean662 = false;

    @OriginalMember(owner = "client!sh", name = "D", descriptor = "I")
    public int minY = 0;

    @OriginalMember(owner = "client!sh", name = "x", descriptor = "Z")
    public boolean underwater = false;

    @OriginalMember(owner = "client!sh", name = "q", descriptor = "B")
    public final byte virtualLevel;

    @OriginalMember(owner = "client!sh", name = "p", descriptor = "I")
    public final int rotation;

    @OriginalMember(owner = "client!sh", name = "F", descriptor = "B")
    public final byte level;

    @OriginalMember(owner = "client!sh", name = "c", descriptor = "I")
    public final int id;

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "I")
    public final int shape;

    @OriginalMember(owner = "client!sh", name = "B", descriptor = "Lclient!eo;")
    public final Entity entity;

    @OriginalMember(owner = "client!sh", name = "H", descriptor = "Z")
    public final boolean hardShadow;

    @OriginalMember(owner = "client!sh", name = "v", descriptor = "Lclient!gu;")
    public final Animator animator;

    @OriginalMember(owner = "client!sh", name = "<init>", descriptor = "(Lclient!ha;Lclient!c;IIIILclient!eo;ZI)V")
    public LocEntity(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) LocType type, @OriginalArg(2) int shape, @OriginalArg(3) int rotation, @OriginalArg(4) int level, @OriginalArg(5) int virtualLevel, @OriginalArg(6) Entity entity, @OriginalArg(7) boolean underwater, @OriginalArg(8) int animation) {
        this.virtualLevel = (byte) virtualLevel;
        this.rotation = rotation;
        this.level = (byte) level;
        this.animated = animation != -1;
        this.id = type.id;
        this.shape = shape;
        this.entity = entity;
        this.underwater = underwater;
        this.hardShadow = toolkit.hardShadow() && type.hardshadow && !this.underwater;
        this.animator = new EntityAnimator(entity, false);
        this.animate(1, animation, false);
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(Lclient!ha;I)V")
    public void addShadow(@OriginalArg(0) Toolkit arg0) {
        this.model(arg0, true, true, true, 0x40000);
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(Lclient!ha;B)V")
    public void removeShadow(@OriginalArg(0) Toolkit arg0) {
        if (this.shadow != null) {
            Static292.method4618(this.shadow, this.virtualLevel, this.entity.x, this.entity.z, this.aBooleanArray27);
            this.aBooleanArray27 = null;
            this.shadow = null;
        }
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(B)I")
    public int getMinY() {
        return this.minY;
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(II)V")
    public void animate(@OriginalArg(0) int arg0) {
        this.animated = true;
        this.animate(1, arg0, false);
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(Z)I")
    public int getSphereRadius(@OriginalArg(0) boolean arg0) {
        if (!arg0) {
            this.model(null, false, false, false, 98);
        }
        return this.sphereRadius;
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(I)Z")
    public boolean hardShadow() {
        return this.hardShadow;
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(IIBZ)V")
    public void animate(@OriginalArg(0) int loopMode, @OriginalArg(1) int animation, @OriginalArg(3) boolean arg2) {
        @Pc(10) int animationId = animation;
        @Pc(12) boolean randomise = false;

        if (animation == -1) {
            @Pc(21) LocType multiloc = LocTypeList.instance.list(this.id);
            @Pc(23) LocType loc = multiloc;
            if (multiloc.multiloc != null) {
                multiloc = multiloc.getMultiLoc(CutsceneManager.state == 3 ? CutsceneVarDomain.instance : TimedVarDomain.instance);
            }
            if (multiloc == null) {
                return;
            }
            if (loc == multiloc) {
                loc = null;
            }

            if (multiloc.hasAnimations()) {
                if (arg2 && this.animator.isAnimating() && multiloc.hasAnimation(this.animator.getAnimationId())) {
                    return;
                }
                if (this.anInt8647 != multiloc.id) {
                    randomise = multiloc.randomanimframe;
                }

                animationId = multiloc.randomAnimation();

                if (multiloc.hasMultipleAnimations()) {
                    loopMode = 0;
                } else {
                    loopMode = 1;
                }
            } else if (loc != null && loc.hasAnimations()) {
                if (arg2 && this.animator.isAnimating() && loc.hasAnimation(this.animator.getAnimationId())) {
                    return;
                }

                if (this.anInt8647 != multiloc.id) {
                    randomise = loc.randomanimframe;
                }

                animationId = loc.randomAnimation();
                if (loc.hasMultipleAnimations()) {
                    loopMode = 0;
                } else {
                    loopMode = 1;
                }
            }
        }

        if (animationId == -1) {
            this.animator.method9104(false, -1, 838828768);
        } else {
            this.animator.update(animationId, 0, loopMode, randomise);
            this.model = null;
            this.aBoolean662 = false;
            this.clock = TimeUtils.clock;
        }
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(Lclient!ha;ZZZI)Lclient!ka;")
    public Model model(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) boolean arg1, @OriginalArg(2) boolean arg2, @OriginalArg(3) boolean addShadow, @OriginalArg(4) int functionMask) {
        @Pc(11) LocType type = LocTypeList.instance.list(this.id);
        if (type.multiloc != null) {
            type = type.getMultiLoc(CutsceneManager.state == 3 ? CutsceneVarDomain.instance : TimedVarDomain.instance);
        }

        if (type == null) {
            this.removeShadow(toolkit);
            this.anInt8647 = -1;
            return null;
        }

        if (!this.animated && this.anInt8647 != type.id) {
            this.animate(0, -1, true);
            this.model = null;
            this.aBoolean662 = false;
        }

        this.method7680(this.entity, arg2);
        if (addShadow) {
            addShadow &= this.hardShadow & !this.aBoolean662 & ClientOptions.instance.hardShadows.getValue() != 0;
        }

        if (arg1 && !addShadow) {
            this.anInt8647 = type.id;
            return null;
        }

        if (addShadow) {
            Static292.method4618(this.shadow, this.virtualLevel, this.entity.x, this.entity.z, this.aBooleanArray27);
            this.aBoolean662 = false;
        }

        @Pc(142) Ground floor = Static246.ground[this.virtualLevel];
        @Pc(163) Ground ceiling;
        if (this.underwater) {
            ceiling = Static706.floor[0];
        } else {
            ceiling = this.virtualLevel >= 3 ? null : Static246.ground[this.virtualLevel + 1];
        }

        @Pc(171) Model model = null;
        if (this.animator.isAnimating()) {
            if (addShadow) {
                functionMask |= 0x40000;
            }

            model = type.wallModel(this.shape == LocShapes.CENTREPIECE_DIAGONAL ? this.rotation + 4 : this.rotation, this.entity.z, this.shape == 11 ? 10 : this.shape, this.entity.x, ceiling, this.animator, toolkit, floor, this.customisation, functionMask, floor.averageHeight(this.entity.x, this.entity.z));

            if (model == null) {
                this.aBooleanArray27 = null;
                this.shadow = null;
                this.sphereRadius = 0;
                this.minY = 0;
            } else {
                if (addShadow) {
                    if (this.aBooleanArray27 == null) {
                        this.aBooleanArray27 = new boolean[4];
                    }
                    this.shadow = model.ba(this.shadow);
                    Static630.addShadow(this.shadow, this.virtualLevel, this.entity.x, this.entity.z, this.aBooleanArray27);
                    this.aBoolean662 = true;
                }
                this.minY = model.fa();
                this.sphereRadius = model.ma();
            }

            this.model = null;
        } else if (this.model != null && (functionMask & this.model.ua()) == functionMask && type.id == this.anInt8647) {
            model = this.model;
        } else {
            if (this.model != null) {
                functionMask |= this.model.ua();
            }

            @Pc(389) ModelAndShadow modelAndShadow = type.modelAndShadow(this.shape == LocShapes.CENTREPIECE_DIAGONAL ? this.rotation + 4 : this.rotation, this.entity.z, this.entity.x, floor, addShadow, floor.averageHeight(this.entity.x, this.entity.z), this.shape == LocShapes.CENTREPIECE_DIAGONAL ? LocShapes.CENTREPIECE_STRAIGHT : this.shape, toolkit, this.customisation, functionMask, ceiling);
            if (modelAndShadow == null) {
                this.sphereRadius = 0;
                this.aBooleanArray27 = null;
                this.minY = 0;
                this.shadow = null;
                this.model = null;
            } else {
                model = modelAndShadow.model;
                this.model = modelAndShadow.model;
                if (addShadow) {
                    this.shadow = modelAndShadow.shadow;
                    this.aBooleanArray27 = null;
                    Static630.addShadow(this.shadow, this.virtualLevel, this.entity.x, this.entity.z, null);
                    this.aBoolean662 = true;
                }
                this.minY = model.fa();
                this.sphereRadius = model.ma();
            }
        }

        this.anInt8647 = type.id;
        return model;
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(Lclient!gp;I)V")
    public void customise(@OriginalArg(0) LocTypeCustomisation customisation) {
        this.customisation = customisation;
        this.model = null;
    }

    @OriginalMember(owner = "client!sh", name = "finalize", descriptor = "()V")
    @Override
    public void finalize() {
        if (this.particleSystem != null) {
            this.particleSystem.stopped();
        }
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(Lclient!eo;Z)V")
    public void method7680(@OriginalArg(0) Entity arg0, @OriginalArg(1) boolean arg1) {
        if (!this.animator.isAnimating()) {
            this.animate(0, -1, false);
        } else if (this.animator.tick(TimeUtils.clock - this.clock)) {
            if (ClientOptions.instance.hardShadows.getValue() == 2) {
                this.aBoolean662 = false;
            }
            if (this.animator.isFinished()) {
                this.animator.update(arg1, -1);
                this.animated = false;
                this.animate(0, -1, false);
            }
        }
        if (arg1) {
            this.clock = TimeUtils.clock;
        }
    }

    @OriginalMember(owner = "client!sh", name = "a", descriptor = "(ILclient!ka;ZILclient!ha;IIILclient!tt;)V")
    public void method7681(@OriginalArg(0) int z2, @OriginalArg(1) Model model, @OriginalArg(2) boolean arg2, @OriginalArg(3) int arg3, @OriginalArg(4) Toolkit toolkit, @OriginalArg(5) int x2, @OriginalArg(6) int x1, @OriginalArg(7) int z1, @OriginalArg(8) Matrix matrix) {
        if (arg3 != -9827) {
            return;
        }

        @Pc(11) ModelParticleEmitter[] emitters = model.particleEmitters();
        @Pc(14) ModelParticleEffector[] effectors = model.particleEffectors();
        if ((this.particleSystem == null || this.particleSystem.removed) && (emitters != null || effectors != null)) {
            @Pc(37) LocType type = LocTypeList.instance.list(this.id);
            if (type.multiloc != null) {
                type = type.getMultiLoc(CutsceneManager.state == 3 ? CutsceneVarDomain.instance : TimedVarDomain.instance);
            }
            if (type != null) {
                this.particleSystem = ParticleSystem.create(TimeUtils.clock, true);
            }
        }

        if (this.particleSystem != null) {
            model.apply(matrix);

            if (arg2) {
                this.particleSystem.update(toolkit, TimeUtils.clock, emitters, effectors);
            } else {
                this.particleSystem.setClock(TimeUtils.clock);
            }

            this.particleSystem.updateBounds(this.level, x1, x2, z1, z2);
        }
    }
}

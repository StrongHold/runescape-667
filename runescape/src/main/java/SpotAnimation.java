import com.jagex.Entity;
import com.jagex.ParticleList;
import com.jagex.PickableEntity;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.seqtype.SeqReplayMode;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pja")
public final class SpotAnimation extends PositionEntity {

    @OriginalMember(owner = "client!pja", name = "qb", descriptor = "Lclient!hv;")
    public ParticleSystem particleSystem;

    @OriginalMember(owner = "client!pja", name = "O", descriptor = "I")
    public int rotation = 0;

    @OriginalMember(owner = "client!pja", name = "jb", descriptor = "I")
    public int sphereRadius = 0;

    @OriginalMember(owner = "client!pja", name = "gb", descriptor = "Z")
    public boolean transparent = true;

    @OriginalMember(owner = "client!pja", name = "Q", descriptor = "I")
    public int minY = 0;

    @OriginalMember(owner = "client!pja", name = "R", descriptor = "I")
    public final int id;

    @OriginalMember(owner = "client!pja", name = "V", descriptor = "Lclient!gu;")
    public Animator animator;

    @OriginalMember(owner = "client!pja", name = "<init>", descriptor = "(IIIIIIIIIIIIZ)V")
    public SpotAnimation(@OriginalArg(0) int id, @OriginalArg(1) int delay, @OriginalArg(2) int level, @OriginalArg(3) int virtualLevel, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z, @OriginalArg(7) int x1, @OriginalArg(8) int x2, @OriginalArg(9) int z1, @OriginalArg(10) int z2, @OriginalArg(11) int rotation, @OriginalArg(12) boolean multipleAnims) {
        super(level, virtualLevel, x, y, z, x1, x2, z1, z2, false, (byte) 0);
        this.id = id;
        this.rotation = rotation;

        @Pc(36) SpotAnimationType type = SpotAnimationTypeList.instance.list(this.id);
        @Pc(39) int animation = type.seq;
        if (animation != -1) {
            this.animator = new EntityAnimator(this, false);
            @Pc(59) int loopMode = type.loopSeq ? SeqReplayMode.STOP : SeqReplayMode.RESTART_LOOP;
            if (multipleAnims) {
                loopMode = SeqReplayMode.RESET;
            }

            this.animator.update(animation, delay, loopMode, false);
        }
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(Lclient!tt;Lclient!ka;ILclient!ha;)V")
    public void method6593(@OriginalArg(0) Matrix matrix, @OriginalArg(1) Model model, @OriginalArg(3) Toolkit arg2) {
        model.apply(matrix);

        @Pc(9) ModelParticleEmitter[] emitters = model.particleEmitters();
        @Pc(19) ModelParticleEffector[] effectors = model.particleEffectors();

        if ((this.particleSystem == null || this.particleSystem.removed) && (emitters != null || effectors != null)) {
            this.particleSystem = ParticleSystem.create(TimeUtils.clock, true);
        }

        if (this.particleSystem != null) {
            this.particleSystem.update(arg2, TimeUtils.clock, emitters, effectors);
            this.particleSystem.updateBounds(super.level, super.x1, super.x2, super.z1, super.z2);
        }
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(IILclient!ha;I)Lclient!ka;")
    public Model method6594(@OriginalArg(1) int id, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) int functionMask) {
        @Pc(8) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);
        @Pc(22) Ground floor = Static706.floor[super.level];
        @Pc(36) Ground ceiling = super.virtualLevel < 3 ? Static706.floor[super.virtualLevel + 1] : null;

        if (this.animator != null && !this.animator.isFinished()) {
            return type.model((byte) 2, true, super.z, this.animator, ceiling, super.x, floor, functionMask, super.y, toolkit);
        } else {
            return type.model((byte) 2, true, super.z, null, ceiling, super.x, floor, functionMask, super.y, toolkit);
        }
    }

    @OriginalMember(owner = "client!pja", name = "finalize", descriptor = "()V")
    @Override
    public void finalize() {
        if (this.particleSystem != null) {
            this.particleSystem.stopped();
        }
    }

    @OriginalMember(owner = "client!pja", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 != -5) {
            this.method9289(null, 72);
        }
        @Pc(20) Model model = this.method6594(this.id, toolkit, 0);
        if (model != null) {
            @Pc(25) Matrix matrix = toolkit.scratchMatrix();
            matrix.applyTranslation(super.x, super.y, super.z);
            this.method6593(matrix, model, toolkit);
        }
    }

    @OriginalMember(owner = "client!pja", name = "i", descriptor = "(I)Z")
    @Override
    public boolean method9290(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            Static494.anInt7409 = 119;
        }
        return false;
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(I)Z")
    public boolean isAnimating() {
        return this.animator != null && !this.animator.isDelayed();
    }

    @OriginalMember(owner = "client!pja", name = "j", descriptor = "(I)V")
    @Override
    public void stopSharingLight(@OriginalArg(0) int arg0) {
        if (arg0 == 27811) {
            throw new IllegalStateException();
        }
    }

    @OriginalMember(owner = "client!pja", name = "k", descriptor = "(I)I")
    @Override
    public int getMinY(@OriginalArg(0) int arg0) {
        if (arg0 != 2) {
            this.isTransparent(113);
        }
        return this.minY;
    }

    @OriginalMember(owner = "client!pja", name = "c", descriptor = "(B)I")
    @Override
    public int getSphereRadius(@OriginalArg(0) byte arg0) {
        if (arg0 != -21) {
            this.finalize();
        }
        return this.sphereRadius;
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(II)V")
    public void tick() {
        if (this.animator != null && !this.animator.isFinished()) {
            this.animator.tick(1);
        }
    }

    @OriginalMember(owner = "client!pja", name = "h", descriptor = "(I)Z")
    @Override
    public boolean isTransparent(@OriginalArg(0) int arg0) {
        return arg0 == 0 ? this.transparent : false;
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(IIZLclient!ha;)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit) {
        if (arg2) {
            this.getMinY(-24);
        }
        return false;
    }

    @OriginalMember(owner = "client!pja", name = "c", descriptor = "(I)V")
    public void stopParticleSystem() {
        if (this.particleSystem != null) {
            this.particleSystem.stopped();
        }
    }

    @OriginalMember(owner = "client!pja", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 >= -93) {
            this.picked(-78, 64, true, null);
        }
        return null;
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(IZLclient!ha;IBILclient!eo;)V")
    @Override
    public void shareLight(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) Entity arg6) {
        if (arg4 < 101) {
            this.render(null);
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit toolkit) {
        @Pc(24) Model local24 = this.method6594(this.id, toolkit, (this.rotation == 0 ? 0 : 5) | 0x800);
        if (local24 == null) {
            return null;
        }

        if (this.rotation != 0) {
            local24.a(this.rotation * 2048);
        }

        @Pc(43) Matrix matrix = toolkit.scratchMatrix();
        matrix.applyTranslation(super.x, super.y, super.z);
        this.method6593(matrix, local24, toolkit);

        @Pc(62) PickableEntity local62 = Static642.allocatePickableEntity(false, 1);
        if (OrthoMode.enabled) {
            local24.renderOrtho(matrix, local62.pickingCylinders[0], OrthoMode.renderZoom, 0);
        } else {
            local24.render(matrix, local62.pickingCylinders[0], 0);
        }

        if (this.particleSystem != null) {
            @Pc(102) ParticleList particles = this.particleSystem.getList();

            if (OrthoMode.enabled) {
                toolkit.renderOrtho(particles, OrthoMode.renderZoom);
            } else {
                toolkit.render(particles);
            }
        }

        this.transparent = local24.F();
        this.minY = local24.fa();
        this.sphereRadius = local24.ma();
        return local62;
    }

    @OriginalMember(owner = "client!pja", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isStationary() {
        return false;
    }

    @OriginalMember(owner = "client!pja", name = "d", descriptor = "(I)Z")
    public boolean isFinished() {
        return this.animator == null || this.animator.isFinished();
    }
}

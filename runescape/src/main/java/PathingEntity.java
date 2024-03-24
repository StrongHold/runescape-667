import com.jagex.core.util.Arrays;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.game.runetek6.config.defaults.WearposDefaults;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkType;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkTypeList;
import com.jagex.game.runetek6.config.seqtype.SeqReplayMode;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!cg")
public abstract class PathingEntity extends PositionEntity {

    @OriginalMember(owner = "client!cg", name = "Ac", descriptor = "I")
    public int anInt10704;

    @OriginalMember(owner = "client!cg", name = "fc", descriptor = "I")
    protected int modelTranslateY;

    @OriginalMember(owner = "client!cg", name = "qb", descriptor = "I")
    public int anInt10726;

    @OriginalMember(owner = "client!cg", name = "Kb", descriptor = "[I")
    public int[] wornTargets;

    @OriginalMember(owner = "client!cg", name = "zb", descriptor = "I")
    public int timerbarGranularity;

    @OriginalMember(owner = "client!cg", name = "T", descriptor = "I")
    public int timerbarDuration;

    @OriginalMember(owner = "client!cg", name = "yc", descriptor = "I")
    public int timerbarStart;

    @OriginalMember(owner = "client!cg", name = "Z", descriptor = "I")
    public int id;

    @OriginalMember(owner = "client!cg", name = "Pb", descriptor = "Lclient!dj;")
    protected ChatMessage message;

    @OriginalMember(owner = "client!cg", name = "Bb", descriptor = "I")
    protected int modelRotateZ;

    @OriginalMember(owner = "client!cg", name = "ic", descriptor = "I")
    protected int modelRotateX;

    @OriginalMember(owner = "client!cg", name = "Ob", descriptor = "B")
    public byte recolSaturation;

    @OriginalMember(owner = "client!cg", name = "db", descriptor = "I")
    public int exactMoveX1;

    @OriginalMember(owner = "client!cg", name = "vb", descriptor = "[I")
    public int[] anIntArray877;

    @OriginalMember(owner = "client!cg", name = "X", descriptor = "I")
    public int exactMoveZ1;

    @OriginalMember(owner = "client!cg", name = "hb", descriptor = "B")
    public byte recolLightness;

    @OriginalMember(owner = "client!cg", name = "O", descriptor = "I")
    public int exactMoveDirection;

    @OriginalMember(owner = "client!cg", name = "rc", descriptor = "I")
    public int exactMoveT2;

    @OriginalMember(owner = "client!cg", name = "jc", descriptor = "I")
    public int turnYaw;

    @OriginalMember(owner = "client!cg", name = "nb", descriptor = "I")
    public int exactMoveZ2;

    @OriginalMember(owner = "client!cg", name = "P", descriptor = "I")
    public int exactMoveT1;

    @OriginalMember(owner = "client!cg", name = "Gb", descriptor = "B")
    public byte recolHue;

    @OriginalMember(owner = "client!cg", name = "Jb", descriptor = "I")
    public int exactMoveX2;

    @OriginalMember(owner = "client!cg", name = "xc", descriptor = "Lclient!hv;")
    protected ParticleSystem particleSystem;

    @OriginalMember(owner = "client!cg", name = "zc", descriptor = "[I")
    public int[] actionAnimations;

    @OriginalMember(owner = "client!cg", name = "mb", descriptor = "[I")
    public final int[] hitAmounts;

    @OriginalMember(owner = "client!cg", name = "tb", descriptor = "I")
    protected int anInt10728;

    @OriginalMember(owner = "client!cg", name = "vc", descriptor = "I")
    public int anInt10719;

    @OriginalMember(owner = "client!cg", name = "gb", descriptor = "I")
    public int drawPriority;

    @OriginalMember(owner = "client!cg", name = "M", descriptor = "I")
    public int anInt10735;

    @OriginalMember(owner = "client!cg", name = "ec", descriptor = "Z")
    public boolean aBoolean816;

    @OriginalMember(owner = "client!cg", name = "Xb", descriptor = "B")
    public byte hitmarkPointer;

    @OriginalMember(owner = "client!cg", name = "R", descriptor = "I")
    public int size;

    @OriginalMember(owner = "client!cg", name = "eb", descriptor = "I")
    public int anInt10743;

    @OriginalMember(owner = "client!cg", name = "K", descriptor = "I")
    protected int anInt10732;

    @OriginalMember(owner = "client!cg", name = "pb", descriptor = "[I")
    public final int[] hitmarkEndTimes;

    @OriginalMember(owner = "client!cg", name = "tc", descriptor = "Z")
    public boolean ready;

    @OriginalMember(owner = "client!cg", name = "Yb", descriptor = "[I")
    public final int[] soakAmounts;

    @OriginalMember(owner = "client!cg", name = "bb", descriptor = "I")
    public int target;

    @OriginalMember(owner = "client!cg", name = "Ub", descriptor = "[I")
    public final int[] soakTypes;

    @OriginalMember(owner = "client!cg", name = "dc", descriptor = "[I")
    public final int[] healthPercentages;

    @OriginalMember(owner = "client!cg", name = "Vb", descriptor = "Z")
    public boolean timerbarSprite;

    @OriginalMember(owner = "client!cg", name = "uc", descriptor = "[I")
    public final int[] hitTypes;

    @OriginalMember(owner = "client!cg", name = "N", descriptor = "I")
    protected int anInt10748;

    @OriginalMember(owner = "client!cg", name = "Db", descriptor = "I")
    public int anInt10747;

    @OriginalMember(owner = "client!cg", name = "qc", descriptor = "Lclient!gu;")
    public final Animator animator;

    @OriginalMember(owner = "client!cg", name = "Ab", descriptor = "Lclient!gu;")
    public final Animator actionAnimator;

    @OriginalMember(owner = "client!cg", name = "Zb", descriptor = "I")
    public int anInt10749;

    @OriginalMember(owner = "client!cg", name = "Wb", descriptor = "I")
    public int yawSpeed;

    @OriginalMember(owner = "client!cg", name = "bc", descriptor = "I")
    public int recolEnd;

    @OriginalMember(owner = "client!cg", name = "yb", descriptor = "I")
    public int cutsceneClock;

    @OriginalMember(owner = "client!cg", name = "ac", descriptor = "B")
    public byte recolScale;

    @OriginalMember(owner = "client!cg", name = "sb", descriptor = "I")
    public int recolStart;

    @OriginalMember(owner = "client!cg", name = "Cb", descriptor = "Lclient!ffa;")
    public final Class126 yaw;

    @OriginalMember(owner = "client!cg", name = "Eb", descriptor = "Lclient!ffa;")
    public final Class126 aClass126_8;

    @OriginalMember(owner = "client!cg", name = "Mb", descriptor = "Lclient!ffa;")
    public final Class126 aClass126_9;

    @OriginalMember(owner = "client!cg", name = "xb", descriptor = "I")
    public int animationPathPointer;

    @OriginalMember(owner = "client!cg", name = "hc", descriptor = "I")
    public int anInt10763;

    @OriginalMember(owner = "client!cg", name = "ub", descriptor = "I")
    public int pathPointer;

    @OriginalMember(owner = "client!cg", name = "cc", descriptor = "I")
    public int anInt10765;

    @OriginalMember(owner = "client!cg", name = "Hb", descriptor = "Z")
    protected boolean aBoolean819;

    @OriginalMember(owner = "client!cg", name = "rb", descriptor = "Z")
    protected boolean aBoolean820;

    @OriginalMember(owner = "client!cg", name = "Lb", descriptor = "[I")
    public final int[] pathZ;

    @OriginalMember(owner = "client!cg", name = "L", descriptor = "[I")
    public final int[] pathX;

    @OriginalMember(owner = "client!cg", name = "Nb", descriptor = "[Lclient!jq;")
    public final EntitySpotAnimation[] spotAnims;

    @OriginalMember(owner = "client!cg", name = "gc", descriptor = "[B")
    public final byte[] pathSpeed;

    @OriginalMember(owner = "client!cg", name = "jb", descriptor = "[Lclient!ka;")
    public final Model[] aModelArray3;

    @OriginalMember(owner = "client!cg", name = "cb", descriptor = "[Lclient!wb;")
    public final DelayedEntityAnimator[] wornAnimators;

    @OriginalMember(owner = "client!cg", name = "<init>", descriptor = "(I)V")
    public PathingEntity(@OriginalArg(0) int arg0) {
        super(0, 0, 0, 0, 0, 0, 0, 0, 0, false, (byte) 0);
        this.actionAnimations = null;
        this.hitAmounts = new int[GraphicsDefaults.instance.maxhitmarks];
        this.anInt10728 = 0;
        this.anInt10719 = -1000;
        this.drawPriority = 0;
        this.anInt10735 = 0;
        this.aBoolean816 = true;
        this.hitmarkPointer = 0;
        this.size = 1;
        this.anInt10743 = -1;
        this.anInt10732 = 0;
        this.hitmarkEndTimes = new int[GraphicsDefaults.instance.maxhitmarks];
        this.ready = false;
        this.soakAmounts = new int[GraphicsDefaults.instance.maxhitmarks];
        this.target = -1;
        this.soakTypes = new int[GraphicsDefaults.instance.maxhitmarks];
        this.healthPercentages = new int[GraphicsDefaults.instance.maxhitmarks];
        this.timerbarSprite = false;
        this.hitTypes = new int[GraphicsDefaults.instance.maxhitmarks];
        this.anInt10748 = -32768;
        this.anInt10747 = -1000;
        this.animator = new EntityAnimator(this, false);
        this.actionAnimator = new EntityAnimator(this, false);
        this.anInt10749 = 0;
        this.yawSpeed = 256;
        this.recolEnd = -1;
        this.cutsceneClock = 0;
        this.recolScale = 0;
        this.recolStart = -1;
        this.yaw = new Class126();
        this.aClass126_8 = new Class126();
        this.aClass126_9 = new Class126();
        this.animationPathPointer = 0;
        this.anInt10763 = 0;
        this.pathPointer = 0;
        this.anInt10765 = 0;
        this.aBoolean819 = false;
        this.aBoolean820 = false;
        this.pathZ = new int[arg0];
        this.pathX = new int[arg0];
        this.spotAnims = new EntitySpotAnimation[4];
        this.pathSpeed = new byte[arg0];
        this.aModelArray3 = new Model[5];
        for (@Pc(174) int local174 = 0; local174 < 4; local174++) {
            this.spotAnims[local174] = new EntitySpotAnimation(this);
        }
        this.wornAnimators = new DelayedEntityAnimator[WearposDefaults.instance.hidden.length];
    }

    @OriginalMember(owner = "client!cg", name = "<init>", descriptor = "()V")
    public PathingEntity() {
        this(10);
    }

    @OriginalMember(owner = "client!cg", name = "b", descriptor = "(I)V")
    public final void method9296() {
        if (this.message != null && this.message.text != null) {
            this.message.remaining--;
            if (this.message.remaining == 0) {
                this.message.text = null;
            }
        }
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IILclient!ha;Lclient!pda;III)V")
    protected final void method9297(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) BASType arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        for (@Pc(13) int local13 = 0; local13 < this.spotAnims.length; local13++) {
            @Pc(16) byte local16 = 0;
            if (local13 == 0) {
                local16 = 2;
            } else if (local13 == 1) {
                local16 = 5;
            } else if (local13 == 2) {
                local16 = 1;
            } else if (local13 == 3) {
                local16 = 7;
            }
            @Pc(50) EntitySpotAnimation local50 = this.spotAnims[local13];
            if (local50.id == -1 || local50.animator.isDelayed()) {
                this.aModelArray3[local13 + 1] = null;
            } else {
                @Pc(76) SpotAnimationType local76 = SpotAnimationTypeList.instance.list(local50.id);
                @Pc(95) boolean local95 = local76.hillType == 3 && (arg5 != 0 || arg1 != 0);
                @Pc(97) int local97 = arg0;
                if (local95) {
                    local97 = arg0 | 0x7;
                } else {
                    if (local50.rotation != 0) {
                        local97 = arg0 | 0x5;
                    }
                    if (local50.height != 0) {
                        local97 |= 0x2;
                    }
                    if (local50.wornSlot >= 0) {
                        local97 |= 0x7;
                    }
                }
                @Pc(146) Model local146 = this.aModelArray3[local13 + 1] = local76.model(local50.animator, local16, local97, arg2);
                if (local146 != null) {
                    if (local50.wornSlot >= 0 && arg3.wornTransformations != null && arg3.wornTransformations[local50.wornSlot] != null) {
                        @Pc(171) int local171 = 0;
                        @Pc(173) int local173 = 0;
                        @Pc(175) int local175 = 0;
                        if (arg3.wornTransformations != null && arg3.wornTransformations[local50.wornSlot] != null) {
                            local173 = arg3.wornTransformations[local50.wornSlot][1];
                            local175 = arg3.wornTransformations[local50.wornSlot][2];
                            local171 = arg3.wornTransformations[local50.wornSlot][0];
                        }
                        if (arg3.graphicOffsets != null && arg3.graphicOffsets[local50.wornSlot] != null) {
                            local173 += arg3.graphicOffsets[local50.wornSlot][1];
                            local175 += arg3.graphicOffsets[local50.wornSlot][2];
                            local171 += arg3.graphicOffsets[local50.wornSlot][0];
                        }
                        if (local175 != 0 || local171 != 0) {
                            @Pc(268) int local268 = arg4;
                            if (this.anIntArray877 != null && this.anIntArray877[local50.wornSlot] != -1) {
                                local268 = this.anIntArray877[local50.wornSlot];
                            }
                            @Pc(299) int local299 = local268 + local50.rotation * 2048 - arg4 & 0x3FFF;
                            if (local299 != 0) {
                                local146.a(local299);
                            }
                            @Pc(310) int local310 = Trig1.SIN[local299];
                            @Pc(314) int local314 = Trig1.COS[local299];
                            @Pc(324) int local324 = local310 * local175 + local171 * local314 >> 14;
                            local175 = local175 * local314 - local171 * local310 >> 14;
                            local171 = local324;
                        }
                        local146.H(local171, local173, local175);
                    } else if (local50.rotation != 0) {
                        local146.a(local50.rotation * 2048);
                    }
                    if (local50.height != 0) {
                        local146.H(0, -local50.height << 2, 0);
                    }
                    if (local95) {
                        if (this.modelRotateX != 0) {
                            local146.FA(this.modelRotateX);
                        }
                        if (this.modelRotateZ != 0) {
                            local146.VA(this.modelRotateZ);
                        }
                        if (this.modelTranslateY != 0) {
                            local146.H(0, this.modelTranslateY, 0);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!cg", name = "l", descriptor = "(I)V")
    @Override
    public final void method9294() {
        @Pc(12) int local12 = (this.size - 1 << 8) + 240;
        super.aShort132 = (short) (super.z - local12 >> 9);
        super.aShort131 = (short) (super.x - local12 >> 9);
        super.aShort133 = (short) (super.z + local12 >> 9);
        super.aShort134 = (short) (super.x + local12 >> 9);
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIZ)V")
    public final void turn(@OriginalArg(0) int yaw, @OriginalArg(2) boolean force) {
        @Pc(15) BASType bas = this.getBASType();

        if (force || bas.yawAcceleration != 0 || this.yawSpeed != 0) {
            this.turnYaw = yaw & 0x3FFF;
            this.yaw.setValue(this.turnYaw);
        }
    }

    @OriginalMember(owner = "client!cg", name = "f", descriptor = "(B)I")
    public int method9299() {
        @Pc(17) BASType local17 = this.getBASType();
        @Pc(31) int local31;
        if (local17.characterHeight != -1) {
            local31 = local17.characterHeight;
        } else if (this.anInt10748 == -32768) {
            local31 = 200;
        } else {
            local31 = -this.anInt10748;
        }
        @Pc(55) Class291 local55 = Static334.activeTiles[super.level][super.x >> Static52.anInt1066][super.z >> Static52.anInt1066];
        return local55 == null || local55.aGroundDecor_1 == null ? local31 : local31 + local55.aGroundDecor_1.aShort46;
    }

    @OriginalMember(owner = "client!cg", name = "c", descriptor = "(B)I")
    @Override
    public final int method9292(@OriginalArg(0) byte arg0) {
        return arg0 == -21 ? this.anInt10728 : 44;
    }

    @OriginalMember(owner = "client!cg", name = "k", descriptor = "(I)I")
    @Override
    public final int method9286(@OriginalArg(0) int arg0) {
        if (arg0 == 2) {
            return this.anInt10748 == -32768 ? 0 : this.anInt10748;
        } else {
            return 16;
        }
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIIBIIII)V")
    public final void hit(@OriginalArg(0) int soakAmount, @OriginalArg(1) int delay, @OriginalArg(2) int healthPercentage, @OriginalArg(4) int hitAmount, @OriginalArg(5) int clock, @OriginalArg(6) int soakType, @OriginalArg(7) int hitType) {
        @Pc(5) boolean empty = true;
        @Pc(7) boolean fromStart = true;
        for (@Pc(21) int i = 0; i < GraphicsDefaults.instance.maxhitmarks; i++) {
            if (clock < this.hitmarkEndTimes[i]) {
                empty = false;
            } else {
                fromStart = false;
            }
        }

        @Pc(48) int index = -1;
        @Pc(50) int comparisonType = -1;
        @Pc(52) int duration = 0;
        if (hitType >= 0) {
            @Pc(59) HitmarkType type = HitmarkTypeList.instance.list(hitType);
            comparisonType = type.comparisonType;
            duration = type.duration;
        }

        if (fromStart) {
            if (comparisonType == -1) {
                return;
            }

            index = 0;

            @Pc(78) int comparisonValue = 0;
            if (comparisonType == 0) {
                comparisonValue = this.hitmarkEndTimes[0];
            } else if (comparisonType == 1) {
                comparisonValue = this.hitAmounts[0];
            }

            for (@Pc(98) int i = 1; i < GraphicsDefaults.instance.maxhitmarks; i++) {
                if (comparisonType == 0) {
                    if (comparisonValue > this.hitmarkEndTimes[i]) {
                        comparisonValue = this.hitmarkEndTimes[i];
                        index = i;
                    }
                } else if (comparisonType == 1 && comparisonValue > this.hitAmounts[i]) {
                    comparisonValue = this.hitAmounts[i];
                    index = i;
                }
            }

            if (comparisonType == 1 && comparisonValue >= hitAmount) {
                return;
            }
        } else {
            if (empty) {
                this.hitmarkPointer = 0;
            }

            for (@Pc(78) int i = 0; i < GraphicsDefaults.instance.maxhitmarks; i++) {
                @Pc(176) byte pointer = this.hitmarkPointer;
                this.hitmarkPointer = (byte) ((this.hitmarkPointer + 1) % GraphicsDefaults.instance.maxhitmarks);

                if (this.hitmarkEndTimes[pointer] <= clock) {
                    index = pointer;
                    break;
                }
            }
        }

        if (index < 0) {
            return;
        }

        this.hitTypes[index] = hitType;
        this.hitAmounts[index] = hitAmount;
        this.soakTypes[index] = soakType;
        this.soakAmounts[index] = soakAmount;
        this.hitmarkEndTimes[index] = duration + clock + delay;
        this.healthPercentages[index] = healthPercentage;
    }

    @OriginalMember(owner = "client!cg", name = "g", descriptor = "(B)I")
    public int getSize() {
        return this.size;
    }

    @OriginalMember(owner = "client!cg", name = "e", descriptor = "(I)I")
    public final int method9303() {
        @Pc(9) BASType local9 = this.getBASType();
        @Pc(13) int local13 = this.yaw.value;
        @Pc(30) boolean local30;
        if (local9.yawAcceleration == 0) {
            local30 = this.yaw.method2676(this.turnYaw, this.yawSpeed, -21712, this.yawSpeed);
        } else {
            local30 = this.yaw.method2676(this.turnYaw, local9.yawMaxSpeed, -21712, local9.yawAcceleration);
        }
        @Pc(55) int local55 = this.yaw.value - local13;
        if (local55 == 0) {
            this.anInt10749 = 0;
            this.yaw.setValue(this.turnYaw);
        } else {
            this.anInt10749++;
        }
        if (local30) {
            if (local9.rollAcceleration != 0) {
                if (local55 > 0) {
                    this.aClass126_8.method2676(local9.rollTargetAngle, local9.rollMaxSpeed, -21712, local9.rollAcceleration);
                } else {
                    this.aClass126_8.method2676(-local9.rollTargetAngle, local9.rollMaxSpeed, -21712, local9.rollAcceleration);
                }
            }
            if (local9.pitchAcceleration != 0) {
                this.aClass126_9.method2676(local9.pitchTargetAngle, local9.pitchMaxSpeed, -21712, local9.pitchAcceleration);
            }
        } else {
            if (local9.rollAcceleration == 0) {
                this.aClass126_8.setValue(0);
            } else {
                this.aClass126_8.method2676(0, local9.rollMaxSpeed, -21712, local9.rollAcceleration);
            }
            if (local9.pitchAcceleration == 0) {
                this.aClass126_9.setValue(0);
            } else {
                this.aClass126_9.method2676(0, local9.pitchMaxSpeed, -21712, local9.pitchAcceleration);
            }
        }
        return local55;
    }

    @OriginalMember(owner = "client!cg", name = "h", descriptor = "(B)I")
    public abstract int method9304(@OriginalArg(0) byte arg0);

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(BI)V")
    public final void method9305(@OriginalArg(1) int arg0) {
        @Pc(15) BASType local15 = this.getBASType();
        if (local15.yawAcceleration == 0 && this.yawSpeed == 0) {
            return;
        }
        this.yaw.normalizeValue();
        @Pc(37) int local37 = arg0 - this.yaw.value & 0x3FFF;
        if (local37 > 8192) {
            this.turnYaw = local37 + this.yaw.value - 16384;
        } else {
            this.turnYaw = local37 + this.yaw.value;
        }
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(Lclient!ka;Z)V")
    protected final void method9306(@OriginalArg(0) Model arg0) {
        @Pc(15) int local15 = this.aClass126_8.value;
        @Pc(19) int local19 = this.aClass126_9.value;
        if (local15 == 0 && local19 == 0) {
            return;
        }
        @Pc(33) int local33 = arg0.fa() / 2;
        arg0.H(0, -local33, 0);
        arg0.VA(local15 & 0x3FFF);
        arg0.FA(local19 & 0x3FFF);
        arg0.H(0, local33, 0);
    }

    @OriginalMember(owner = "client!cg", name = "h", descriptor = "(I)Z")
    @Override
    public final boolean method9282(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            this.anInt10749 = -63;
        }
        return this.aBoolean819;
    }

    @OriginalMember(owner = "client!cg", name = "b", descriptor = "(III)Z")
    public final boolean method9307(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        if (this.anIntArray877 == null) {
            if (arg1 == -1) {
                return true;
            }
            this.anIntArray877 = new int[WearposDefaults.instance.hidden.length];
            for (@Pc(24) int local24 = 0; local24 < WearposDefaults.instance.hidden.length; local24++) {
                this.anIntArray877[local24] = -1;
            }
        }
        @Pc(43) BASType local43 = this.getBASType();
        @Pc(45) int local45 = 256;
        if (local43.maxWornRotation != null && local43.maxWornRotation[arg0] > 0) {
            local45 = local43.maxWornRotation[arg0];
        }
        @Pc(82) int local82;
        @Pc(87) int local87;
        if (arg1 != -1) {
            if (this.anIntArray877[arg0] == -1) {
                this.anIntArray877[arg0] = this.yaw.getValue(16383);
            }
            local82 = this.anIntArray877[arg0];
            local87 = arg1 - local82;
            if (local87 >= -local45 && local45 >= local87) {
                this.anIntArray877[arg0] = arg1;
                return true;
            }
            if ((local87 <= 0 || local87 > 8192) && local87 > -8192) {
                this.anIntArray877[arg0] = local82 - local45 & 0x3FFF;
            } else {
                this.anIntArray877[arg0] = local82 + local45 & 0x3FFF;
            }
            return false;
        } else if (this.anIntArray877[arg0] == -1) {
            return true;
        } else {
            local82 = this.yaw.getValue(16383);
            local87 = this.anIntArray877[arg0];
            @Pc(92) int local92 = local82 - local87;
            if (-local45 > local92 || local45 < local92) {
                if ((local92 <= 0 || local92 > 8192) && local92 > -8192) {
                    this.anIntArray877[arg0] = local87 - local45 & 0x3FFF;
                } else {
                    this.anIntArray877[arg0] = local45 + local87 & 0x3FFF;
                }
                return false;
            }
            this.anIntArray877[arg0] = -1;
            for (@Pc(112) int local112 = 0; local112 < WearposDefaults.instance.hidden.length; local112++) {
                if (this.anIntArray877[local112] != -1) {
                    return true;
                }
            }
            this.anIntArray877 = null;
            return true;
        }
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIZIIII)V")
    public final void setSpotAnim(@OriginalArg(0) int index, @OriginalArg(1) int rotation, @OriginalArg(2) boolean loop, @OriginalArg(3) int heightAndDelay, @OriginalArg(4) int wornSlot, @OriginalArg(5) int id) {
        @Pc(16) EntitySpotAnimation spotAnim = this.spotAnims[index];
        @Pc(19) int currentId = spotAnim.id;

        if (id != -1 && currentId != -1) {
            if (currentId == id) {
                @Pc(38) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);

                if (type.loopSeq && type.seq != -1) {
                    @Pc(54) SeqType seqType = SeqTypeList.instance.list(type.seq);
                    @Pc(57) int replayMode = seqType.replayMode;

                    if (replayMode == SeqReplayMode.STOP) {
                        return;
                    }

                    if (replayMode == SeqReplayMode.RESTART_LOOP) {
                        spotAnim.animator.restartLoop();
                        return;
                    }
                }
            } else {
                @Pc(38) SpotAnimationType newType = SpotAnimationTypeList.instance.list(id);
                @Pc(86) SpotAnimationType currType = SpotAnimationTypeList.instance.list(currentId);

                if (newType.seq != -1 && currType.seq != -1) {
                    @Pc(103) SeqType newSeqType = SeqTypeList.instance.list(newType.seq);
                    @Pc(109) SeqType currSeqType = SeqTypeList.instance.list(currType.seq);

                    if (newSeqType.priority < currSeqType.priority) {
                        return;
                    }
                }
            }
        }

        @Pc(118) byte loopMode = 0;
        if (id != -1 && !SpotAnimationTypeList.instance.list(id).loopSeq) {
            loopMode = 2;
        }

        spotAnim.rotation = rotation;
        spotAnim.wornSlot = wornSlot;
        spotAnim.id = id;
        spotAnim.height = heightAndDelay >> 16;
        if (id != -1 && loop) {
            loopMode = 1;
        }

        spotAnim.animator.update(id == -1 ? -1 : SpotAnimationTypeList.instance.list(id).seq, heightAndDelay & 0xFFFF, loopMode, false);
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(II)V")
    public final void setSize(@OriginalArg(1) int size) {
        this.size = size;
    }

    @OriginalMember(owner = "client!cg", name = "e", descriptor = "(B)Z")
    public abstract boolean method9311();

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(ILjava/lang/String;ZII)V")
    public final void setChatMessage(@OriginalArg(0) int duration, @OriginalArg(1) String text, @OriginalArg(3) int effect, @OriginalArg(4) int colour) {
        if (this.message == null) {
            this.message = new ChatMessage();
        }
        this.message.effect = effect;
        this.message.remaining = this.message.duration = duration;
        this.message.colour = colour;
        this.message.text = text;
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIIIII)V")
    protected final void method9314(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(11) int local11 = super.aShort134 + super.aShort131 >> 1;
        @Pc(20) int local20 = super.aShort133 + super.aShort132 >> 1;
        @Pc(24) int local24 = Trig1.SIN[arg0];
        @Pc(28) int local28 = Trig1.COS[arg0];
        @Pc(33) int local33 = -arg3 / 2;
        @Pc(38) int local38 = -arg4 / 2;
        @Pc(48) int local48 = local24 * local38 + local33 * local28 >> 14;
        @Pc(59) int local59 = local38 * local28 - local33 * local24 >> 14;
        @Pc(74) int local74 = Static323.method4626(local59 + super.z, super.level, local11, local20, local48 + super.x);
        @Pc(78) int local78 = arg3 / 2;
        @Pc(83) int local83 = -arg4 / 2;
        @Pc(93) int local93 = local28 * local78 + local83 * local24 >> 14;
        @Pc(104) int local104 = local28 * local83 - local24 * local78 >> 14;
        @Pc(119) int local119 = Static323.method4626(local104 + super.z, super.level, local11, local20, local93 + super.x);
        @Pc(124) int local124 = -arg3 / 2;
        @Pc(128) int local128 = arg4 / 2;
        @Pc(138) int local138 = local24 * local128 + local124 * local28 >> 14;
        @Pc(149) int local149 = local128 * local28 - local124 * local24 >> 14;
        @Pc(165) int local165 = Static323.method4626(local149 + super.z, super.level, local11, local20, super.x + local138);
        @Pc(169) int local169 = arg3 / 2;
        @Pc(173) int local173 = arg4 / 2;
        @Pc(183) int local183 = local24 * local173 + local28 * local169 >> 14;
        @Pc(194) int local194 = local173 * local28 - local169 * local24 >> 14;
        @Pc(210) int local210 = Static323.method4626(local194 + super.z, super.level, local11, local20, super.x + local183);
        @Pc(218) int local218 = local74 < local119 ? local74 : local119;
        @Pc(226) int local226 = local210 > local165 ? local165 : local210;
        @Pc(234) int local234 = local119 >= local210 ? local210 : local119;
        this.modelRotateX = (int) (Math.atan2(local218 - local226, arg4) * 2607.5945876176133D) & 0x3FFF;
        @Pc(257) int local257 = local165 > local74 ? local74 : local165;
        this.modelRotateZ = (int) (Math.atan2(local257 - local234, arg3) * 2607.5945876176133D) & 0x3FFF;
        if (arg5 >= -78) {
            return;
        }
        @Pc(288) int local288;
        if (this.modelRotateX != 0 && arg1 != 0) {
            local288 = 16384 - arg1;
            if (this.modelRotateX > 8192) {
                if (local288 > this.modelRotateX) {
                    this.modelRotateX = local288;
                }
            } else if (this.modelRotateX > arg1) {
                this.modelRotateX = arg1;
            }
        }
        this.modelTranslateY = local210 + local74;
        if (this.modelRotateZ != 0 && arg2 != 0) {
            local288 = 16384 - arg2;
            if (this.modelRotateZ > 8192) {
                if (local288 > this.modelRotateZ) {
                    this.modelRotateZ = local288;
                }
            } else if (arg2 < this.modelRotateZ) {
                this.modelRotateZ = arg2;
            }
        }
        if (this.modelTranslateY > local119 + local165) {
            this.modelTranslateY = local119 + local165;
        }
        this.modelTranslateY = (this.modelTranslateY >> 1) - super.anInt10691;
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "([I[IB)V")
    public final void updateWornTargets(@OriginalArg(0) int[] flags, @OriginalArg(1) int[] targets) {
        if (this.wornTargets == null && targets != null) {
            this.wornTargets = new int[WearposDefaults.instance.hidden.length];
        } else if (targets == null) {
            this.wornTargets = null;
            return;
        }

        for (@Pc(35) int i = 0; i < this.wornTargets.length; i++) {
            this.wornTargets[i] = -1;
        }

        for (@Pc(53) int i = 0; i < targets.length; i++) {
            @Pc(58) int flag = flags[i];

            for (@Pc(60) int slot = 0; slot < this.wornTargets.length; slot++) {
                if ((flag & 0x1) != 0) {
                    this.wornTargets[slot] = targets[i];
                }

                flag >>= 0x1;
            }
        }
    }

    @OriginalMember(owner = "client!cg", name = "c", descriptor = "(I)V")
    public final void method9316() {
        this.pathPointer = 0;
        this.animationPathPointer = 0;
    }

    @OriginalMember(owner = "client!cg", name = "finalize", descriptor = "()V")
    @Override
    public final void finalize() {
        if (this.particleSystem != null) {
            this.particleSystem.method3644();
        }
    }

    @OriginalMember(owner = "client!cg", name = "i", descriptor = "(B)Lclient!pda;")
    public final BASType getBASType() {
        @Pc(13) int local13 = this.method9320(0);
        return local13 == -1 ? Static636.A_BAS_TYPE___1 : BASTypeList.instance.list(local13);
    }

    @OriginalMember(owner = "client!cg", name = "b", descriptor = "(B)Z")
    @Override
    public final boolean method9283() {
        return false;
    }

    @OriginalMember(owner = "client!cg", name = "d", descriptor = "(I)Lclient!dj;")
    public abstract ChatMessage method9318(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(Lclient!ha;BZ[Lclient!ka;Lclient!tt;)V")
    protected final void method9319(@OriginalArg(0) Toolkit arg0, @OriginalArg(2) boolean arg1, @OriginalArg(3) Model[] arg2, @OriginalArg(4) Matrix arg3) {
        if (!arg1) {
            @Pc(15) int local15 = 0;
            @Pc(17) int local17 = 0;
            @Pc(19) int local19 = 0;
            @Pc(21) int local21 = 0;
            @Pc(23) int local23 = -1;
            @Pc(25) int local25 = -1;
            @Pc(29) ModelParticleEmitter[][] local29 = new ModelParticleEmitter[arg2.length][];
            @Pc(33) ModelParticleEffector[][] local33 = new ModelParticleEffector[arg2.length][];
            for (@Pc(35) int local35 = 0; local35 < arg2.length; local35++) {
                if (arg2[local35] != null) {
                    arg2[local35].method7476(arg3);
                    local29[local35] = arg2[local35].particleEmitters();
                    local33[local35] = arg2[local35].particleEffectors();
                    if (local29[local35] != null) {
                        local23 = local35;
                        local15 += local29[local35].length;
                        local17++;
                    }
                    if (local33[local35] != null) {
                        local25 = local35;
                        local21++;
                        local19 += local33[local35].length;
                    }
                }
            }
            if ((this.particleSystem == null || this.particleSystem.aBoolean324) && (local17 > 0 || local21 > 0)) {
                this.particleSystem = ParticleSystem.create(TimeUtils.clock, true);
            }
            if (this.particleSystem != null) {
                @Pc(138) ModelParticleEmitter[] local138;
                @Pc(142) int local142;
                if (local17 == 1) {
                    local138 = local29[local23];
                } else {
                    local138 = new ModelParticleEmitter[local15];
                    @Pc(140) int local140 = 0;
                    for (local142 = 0; local142 < arg2.length; local142++) {
                        if (local29[local142] != null) {
                            Arrays.copy(local29[local142], 0, local138, local140, local29[local142].length);
                            local140 += local29[local142].length;
                        }
                    }
                }
                @Pc(191) ModelParticleEffector[] local191;
                if (local21 == 1) {
                    local191 = local33[local25];
                } else {
                    local191 = new ModelParticleEffector[local19];
                    local142 = 0;
                    for (@Pc(199) int local199 = 0; local199 < arg2.length; local199++) {
                        if (local33[local199] != null) {
                            Arrays.copy(local33[local199], 0, local191, local142, local33[local199].length);
                            local142 += local33[local199].length;
                        }
                    }
                }
                this.particleSystem.method3643(arg0, TimeUtils.clock, local138, local191);
                this.aBoolean820 = true;
            }
        } else if (this.particleSystem != null) {
            this.particleSystem.method3649(TimeUtils.clock);
        }
        if (this.particleSystem != null) {
            this.particleSystem.method3658(super.level, super.aShort131, super.aShort134, super.aShort132, super.aShort133);
        }
    }

    @OriginalMember(owner = "client!cg", name = "m", descriptor = "(I)I")
    protected abstract int method9320(@OriginalArg(0) int arg0);
}
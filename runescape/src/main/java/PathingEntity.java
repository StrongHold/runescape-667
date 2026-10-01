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
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!cg")
public abstract class PathingEntity extends PositionEntity {

    @OriginalMember(owner = "client!wda", name = "a", descriptor = "(ILclient!cg;I)V")
    public static void updateActionAnimator(@OriginalArg(1) PathingEntity entity, @OriginalArg(2) int speed) {
        if (entity.actionAnimations == null) {
            return;
        }

        @Pc(13) int animation = entity.actionAnimations[speed + 1];
        if (entity.actionAnimator.getAnimationId() != animation) {
            entity.actionAnimator.update(entity.actionAnimator.getDelay(), animation);
            entity.animationPathPointer = entity.pathPointer;
        }
    }

    @OriginalMember(owner = "client!uja", name = "a", descriptor = "([IIZLclient!cg;I)V")
    public static void animate(@OriginalArg(0) int[] animations, @OriginalArg(1) int delay, @OriginalArg(2) boolean updatePathPointer, @OriginalArg(3) PathingEntity entity) {
        if (entity.actionAnimations != null) {
            @Pc(8) boolean repeat = true;

            for (@Pc(10) int i = 0; i < entity.actionAnimations.length; i++) {
                if (animations[i] != entity.actionAnimations[i]) {
                    repeat = false;
                    break;
                }
            }

            @Pc(31) Animator animator = entity.actionAnimator;
            if (repeat && animator.isAnimating()) {
                @Pc(44) SeqType seqType = entity.actionAnimator.getAnimation();
                @Pc(47) int replayMode = seqType.replayMode;

                if (replayMode == SeqReplayMode.RESET) {
                    animator.reset(delay);
                }

                if (replayMode == SeqReplayMode.RESTART_LOOP) {
                    animator.restartLoop();
                }
            }
        }

        @Pc(8) boolean override = true;
        for (@Pc(10) int i = 0; i < animations.length; i++) {
            if (animations[i] != -1) {
                override = false;
            }

            if (entity.actionAnimations == null || entity.actionAnimations[i] == -1 || SeqTypeList.instance.list(animations[i]).priority >= SeqTypeList.instance.list(entity.actionAnimations[i]).priority) {
                entity.actionAnimations = animations;
                entity.actionAnimator.setDelay(delay);

                if (updatePathPointer) {
                    entity.animationPathPointer = entity.pathPointer;
                }
            }
        }

        if (override) {
            entity.actionAnimations = animations;
            entity.actionAnimator.setDelay(delay);

            if (updatePathPointer) {
                entity.animationPathPointer = entity.pathPointer;
            }
        }
    }

    @OriginalMember(owner = "client!cg", name = "Ac", descriptor = "I")
    public int anInt10704;

    @OriginalMember(owner = "client!cg", name = "fc", descriptor = "I")
    protected int modelTranslateY;

    @OriginalMember(owner = "client!cg", name = "qb", descriptor = "I")
    public int healthPercentage;

    @OriginalMember(owner = "client!cg", name = "Kb", descriptor = "[I")
    public int[] wornTargets;

    @OriginalMember(owner = "client!cg", name = "zb", descriptor = "I")
    public int timerbarGranularity;

    @OriginalMember(owner = "client!cg", name = "T", descriptor = "I")
    public int timerbarDuration;

    @OriginalMember(owner = "client!cg", name = "yc", descriptor = "I")
    public int timerbarStart;

    @OriginalMember(owner = "client!cg", name = "Z", descriptor = "I")
    public int slot;

    @OriginalMember(owner = "client!cg", name = "Pb", descriptor = "Lclient!dj;")
    protected EntityChatLine line;

    @OriginalMember(owner = "client!cg", name = "Bb", descriptor = "I")
    protected int modelRotateZ;

    @OriginalMember(owner = "client!cg", name = "ic", descriptor = "I")
    protected int modelRotateX;

    @OriginalMember(owner = "client!cg", name = "Ob", descriptor = "B")
    public byte recolSaturation;

    @OriginalMember(owner = "client!cg", name = "db", descriptor = "I")
    public int exactMoveX1;

    @OriginalMember(owner = "client!cg", name = "vb", descriptor = "[I")
    public int[] wornRotation;

    @OriginalMember(owner = "client!cg", name = "X", descriptor = "I")
    public int exactMoveZ1;

    @OriginalMember(owner = "client!cg", name = "hb", descriptor = "B")
    public byte recolLightness;

    @OriginalMember(owner = "client!cg", name = "O", descriptor = "I")
    public int exactMoveDirection;

    @OriginalMember(owner = "client!cg", name = "rc", descriptor = "I")
    public int exactMoveT2;

    @OriginalMember(owner = "client!cg", name = "jc", descriptor = "I")
    public int yawTarget;

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
    protected int sphereRadius;

    @OriginalMember(owner = "client!cg", name = "vc", descriptor = "I")
    public int timerbarEnd;

    @OriginalMember(owner = "client!cg", name = "gb", descriptor = "I")
    public int drawPriority;

    @OriginalMember(owner = "client!cg", name = "M", descriptor = "I")
    public int anInt10735;

    @OriginalMember(owner = "client!cg", name = "ec", descriptor = "Z")
    public boolean visible;

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
    public final int[] soakHitmarkTypes;

    @OriginalMember(owner = "client!cg", name = "dc", descriptor = "[I")
    public final int[] healthPercentages;

    @OriginalMember(owner = "client!cg", name = "Vb", descriptor = "Z")
    public boolean timerbarSprite;

    @OriginalMember(owner = "client!cg", name = "uc", descriptor = "[I")
    public final int[] damageHitmarkTypes;

    @OriginalMember(owner = "client!cg", name = "N", descriptor = "I")
    protected int minY;

    @OriginalMember(owner = "client!cg", name = "Db", descriptor = "I")
    public int healthClock;

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
    public final Orientation yaw;

    @OriginalMember(owner = "client!cg", name = "Eb", descriptor = "Lclient!ffa;")
    public final Orientation roll;

    @OriginalMember(owner = "client!cg", name = "Mb", descriptor = "Lclient!ffa;")
    public final Orientation pitch;

    @OriginalMember(owner = "client!cg", name = "xb", descriptor = "I")
    public int animationPathPointer;

    @OriginalMember(owner = "client!cg", name = "hc", descriptor = "I")
    public int delayedWalkingTicks;

    @OriginalMember(owner = "client!cg", name = "ub", descriptor = "I")
    public int pathPointer;

    @OriginalMember(owner = "client!cg", name = "cc", descriptor = "I")
    public int movementAcceleration;

    @OriginalMember(owner = "client!cg", name = "Hb", descriptor = "Z")
    protected boolean transparent;

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
    public PathingEntity(@OriginalArg(0) int pathLength) {
        super(0, 0, 0, 0, 0, 0, 0, 0, 0, false, (byte) 0);
        this.actionAnimations = null;
        this.hitAmounts = new int[GraphicsDefaults.instance.maxhitmarks];
        this.sphereRadius = 0;
        this.timerbarEnd = -1000;
        this.drawPriority = 0;
        this.anInt10735 = 0;
        this.visible = true;
        this.hitmarkPointer = 0;
        this.size = 1;
        this.anInt10743 = -1;
        this.anInt10732 = 0;
        this.hitmarkEndTimes = new int[GraphicsDefaults.instance.maxhitmarks];
        this.ready = false;
        this.soakAmounts = new int[GraphicsDefaults.instance.maxhitmarks];
        this.target = -1;
        this.soakHitmarkTypes = new int[GraphicsDefaults.instance.maxhitmarks];
        this.healthPercentages = new int[GraphicsDefaults.instance.maxhitmarks];
        this.timerbarSprite = false;
        this.damageHitmarkTypes = new int[GraphicsDefaults.instance.maxhitmarks];
        this.minY = -32768;
        this.healthClock = -1000;
        this.animator = new EntityAnimator(this, false);
        this.actionAnimator = new EntityAnimator(this, false);
        this.anInt10749 = 0;
        this.yawSpeed = 256;
        this.recolEnd = -1;
        this.cutsceneClock = 0;
        this.recolScale = 0;
        this.recolStart = -1;
        this.yaw = new Orientation();
        this.roll = new Orientation();
        this.pitch = new Orientation();
        this.animationPathPointer = 0;
        this.delayedWalkingTicks = 0;
        this.pathPointer = 0;
        this.movementAcceleration = 0;
        this.transparent = false;
        this.aBoolean820 = false;
        this.pathZ = new int[pathLength];
        this.pathX = new int[pathLength];
        this.spotAnims = new EntitySpotAnimation[4];
        this.pathSpeed = new byte[pathLength];
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
    public final void chatTick() {
        if (this.line != null && this.line.text != null) {
            this.line.remaining--;

            if (this.line.remaining == 0) {
                this.line.text = null;
            }
        }
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IILclient!ha;Lclient!pda;III)V")
    protected final void method9297(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) BASType basType, @OriginalArg(4) int rotation, @OriginalArg(5) int arg5) {
        for (@Pc(13) int i = 0; i < this.spotAnims.length; i++) {
            @Pc(16) byte local16 = 0;
            if (i == 0) {
                local16 = 2;
            } else if (i == 1) {
                local16 = 5;
            } else if (i == 2) {
                local16 = 1;
            } else if (i == 3) {
                local16 = 7;
            }

            @Pc(50) EntitySpotAnimation spotAnim = this.spotAnims[i];
            if (spotAnim.id == -1 || spotAnim.animator.isDelayed()) {
                this.aModelArray3[i + 1] = null;
            } else {
                @Pc(76) SpotAnimationType spotAnimationType = SpotAnimationTypeList.instance.list(spotAnim.id);
                @Pc(95) boolean local95 = spotAnimationType.hillType == 3 && (arg5 != 0 || arg1 != 0);

                @Pc(97) int local97 = arg0;
                if (local95) {
                    local97 = arg0 | 0x7;
                } else {
                    if (spotAnim.rotation != 0) {
                        local97 = arg0 | 0x5;
                    }
                    if (spotAnim.height != 0) {
                        local97 |= 0x2;
                    }
                    if (spotAnim.wornSlot >= 0) {
                        local97 |= 0x7;
                    }
                }

                @Pc(146) Model model = this.aModelArray3[i + 1] = spotAnimationType.model(spotAnim.animator, local16, local97, toolkit);
                if (model != null) {
                    if (spotAnim.wornSlot >= 0 && basType.wornTransformations != null && basType.wornTransformations[spotAnim.wornSlot] != null) {
                        @Pc(171) int translateX = 0;
                        @Pc(173) int translateY = 0;
                        @Pc(175) int translateZ = 0;

                        if (basType.wornTransformations != null && basType.wornTransformations[spotAnim.wornSlot] != null) {
                            translateY = basType.wornTransformations[spotAnim.wornSlot][1];
                            translateZ = basType.wornTransformations[spotAnim.wornSlot][2];
                            translateX = basType.wornTransformations[spotAnim.wornSlot][0];
                        }

                        if (basType.graphicOffsets != null && basType.graphicOffsets[spotAnim.wornSlot] != null) {
                            translateY += basType.graphicOffsets[spotAnim.wornSlot][1];
                            translateZ += basType.graphicOffsets[spotAnim.wornSlot][2];
                            translateX += basType.graphicOffsets[spotAnim.wornSlot][0];
                        }

                        if (translateZ != 0 || translateX != 0) {
                            @Pc(268) int wornRotation = rotation;
                            if (this.wornRotation != null && this.wornRotation[spotAnim.wornSlot] != -1) {
                                wornRotation = this.wornRotation[spotAnim.wornSlot];
                            }

                            @Pc(299) int rotateY = ((wornRotation + (spotAnim.rotation * 2048)) - rotation) & 0x3FFF;
                            if (rotateY != 0) {
                                model.a(rotateY);
                            }

                            @Pc(310) int local310 = Trig1.SIN[rotateY];
                            @Pc(314) int local314 = Trig1.COS[rotateY];
                            @Pc(324) int local324 = local310 * translateZ + translateX * local314 >> 14;
                            translateZ = translateZ * local314 - translateX * local310 >> 14;
                            translateX = local324;
                        }

                        model.H(translateX, translateY, translateZ);
                    } else if (spotAnim.rotation != 0) {
                        model.a(spotAnim.rotation * 2048);
                    }

                    if (spotAnim.height != 0) {
                        model.H(0, -spotAnim.height << 2, 0);
                    }

                    if (local95) {
                        if (this.modelRotateX != 0) {
                            model.FA(this.modelRotateX);
                        }
                        if (this.modelRotateZ != 0) {
                            model.VA(this.modelRotateZ);
                        }
                        if (this.modelTranslateY != 0) {
                            model.H(0, this.modelTranslateY, 0);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!cg", name = "l", descriptor = "(I)V")
    @Override
    public final void updateBounds() {
        @Pc(12) int local12 = (this.size - 1 << 8) + 240;
        super.z1 = (short) (super.z - local12 >> 9);
        super.x1 = (short) (super.x - local12 >> 9);
        super.z2 = (short) (super.z + local12 >> 9);
        super.x2 = (short) (super.x + local12 >> 9);
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIZ)V")
    public final void turn(@OriginalArg(0) int yaw, @OriginalArg(2) boolean force) {
        @Pc(15) BASType bas = this.getBASType();

        if (force || bas.yawAcceleration != 0 || this.yawSpeed != 0) {
            this.yawTarget = yaw & 0x3FFF;
            this.yaw.setValue(this.yawTarget);
        }
    }

    @OriginalMember(owner = "client!cg", name = "f", descriptor = "(B)I")
    public int getY() {
        @Pc(17) BASType local17 = this.getBASType();
        @Pc(31) int local31;
        if (local17.characterHeight != -1) {
            local31 = local17.characterHeight;
        } else if (this.minY == -32768) {
            local31 = 200;
        } else {
            local31 = -this.minY;
        }
        @Pc(55) Tile local55 = Static334.activeTiles[super.level][super.x >> EnvironmentLight.tileShift][super.z >> EnvironmentLight.tileShift];
        return local55 == null || local55.groundDecor == null ? local31 : local31 + local55.groundDecor.offsetY;
    }

    @OriginalMember(owner = "client!cg", name = "c", descriptor = "(B)I")
    @Override
    public final int getSphereRadius(@OriginalArg(0) byte arg0) {
        return arg0 == -21 ? this.sphereRadius : 44;
    }

    @OriginalMember(owner = "client!cg", name = "k", descriptor = "(I)I")
    @Override
    public final int getMinY(@OriginalArg(0) int arg0) {
        if (arg0 == 2) {
            return this.minY == -32768 ? 0 : this.minY;
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

        this.damageHitmarkTypes[index] = hitType;
        this.hitAmounts[index] = hitAmount;
        this.soakHitmarkTypes[index] = soakType;
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
        @Pc(9) BASType basType = this.getBASType();
        @Pc(13) int yaw = this.yaw.value;
        @Pc(30) boolean local30;
        if (basType.yawAcceleration != 0) {
            local30 = this.yaw.tick(this.yawTarget, basType.yawMaxSpeed, -21712, basType.yawAcceleration);
        } else {
            local30 = this.yaw.tick(this.yawTarget, this.yawSpeed, -21712, this.yawSpeed);
        }

        @Pc(55) int deltaYaw = this.yaw.value - yaw;
        if (deltaYaw == 0) {
            this.anInt10749 = 0;
            this.yaw.setValue(this.yawTarget);
        } else {
            this.anInt10749++;
        }

        if (local30) {
            if (basType.rollAcceleration != 0) {
                if (deltaYaw > 0) {
                    this.roll.tick(basType.rollTargetAngle, basType.rollMaxSpeed, -21712, basType.rollAcceleration);
                } else {
                    this.roll.tick(-basType.rollTargetAngle, basType.rollMaxSpeed, -21712, basType.rollAcceleration);
                }
            }

            if (basType.pitchAcceleration != 0) {
                this.pitch.tick(basType.pitchTargetAngle, basType.pitchMaxSpeed, -21712, basType.pitchAcceleration);
            }
        } else {
            if (basType.rollAcceleration == 0) {
                this.roll.setValue(0);
            } else {
                this.roll.tick(0, basType.rollMaxSpeed, -21712, basType.rollAcceleration);
            }

            if (basType.pitchAcceleration == 0) {
                this.pitch.setValue(0);
            } else {
                this.pitch.tick(0, basType.pitchMaxSpeed, -21712, basType.pitchAcceleration);
            }
        }

        return deltaYaw;
    }

    @OriginalMember(owner = "client!cg", name = "h", descriptor = "(B)I")
    public abstract int method9304(@OriginalArg(0) byte arg0);

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(BI)V")
    public final void turn(@OriginalArg(1) int yaw) {
        @Pc(15) BASType basType = this.getBASType();
        if (basType.yawAcceleration == 0 && this.yawSpeed == 0) {
            return;
        }

        this.yaw.normalize();

        @Pc(37) int delta = (yaw - this.yaw.value) & 0x3FFF;
        if (delta > 8192) {
            this.yawTarget = (delta + this.yaw.value) - 16384;
        } else {
            this.yawTarget = delta + this.yaw.value;
        }
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(Lclient!ka;Z)V")
    protected final void method9306(@OriginalArg(0) Model model) {
        @Pc(15) int rotateZ = this.roll.value;
        @Pc(19) int rotateX = this.pitch.value;
        if (rotateZ == 0 && rotateX == 0) {
            return;
        }

        @Pc(33) int translateY = model.fa() / 2;
        model.H(0, -translateY, 0);
        model.VA(rotateZ & 0x3FFF);
        model.FA(rotateX & 0x3FFF);
        model.H(0, translateY, 0);
    }

    @OriginalMember(owner = "client!cg", name = "h", descriptor = "(I)Z")
    @Override
    public final boolean isTransparent(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            this.anInt10749 = -63;
        }
        return this.transparent;
    }

    @OriginalMember(owner = "client!cg", name = "b", descriptor = "(III)Z")
    public final boolean method9307(@OriginalArg(1) int slot, @OriginalArg(2) int arg1) {
        if (this.wornRotation == null) {
            if (arg1 == -1) {
                return true;
            }

            this.wornRotation = new int[WearposDefaults.instance.hidden.length];

            for (@Pc(24) int i = 0; i < WearposDefaults.instance.hidden.length; i++) {
                this.wornRotation[i] = -1;
            }
        }

        @Pc(43) BASType basType = this.getBASType();
        @Pc(45) int maxWornRotation = 256;
        if (basType.maxWornRotation != null && basType.maxWornRotation[slot] > 0) {
            maxWornRotation = basType.maxWornRotation[slot];
        }

        if (arg1 != -1) {
            if (this.wornRotation[slot] == -1) {
                this.wornRotation[slot] = this.yaw.getValue(16383);
            }

            @Pc(82) int rotation = this.wornRotation[slot];
            @Pc(87) int wornRotation = arg1 - rotation;
            if (wornRotation >= -maxWornRotation && wornRotation <= maxWornRotation) {
                this.wornRotation[slot] = arg1;
                return true;
            }

            if ((wornRotation <= 0 || wornRotation > 8192) && wornRotation > -8192) {
                this.wornRotation[slot] = rotation - maxWornRotation & 0x3FFF;
            } else {
                this.wornRotation[slot] = rotation + maxWornRotation & 0x3FFF;
            }

            return false;
        } else if (this.wornRotation[slot] == -1) {
            return true;
        } else {
            @Pc(82) int local82 = this.yaw.getValue(16383);
            @Pc(87) int local87 = this.wornRotation[slot];

            @Pc(92) int local92 = local82 - local87;
            if (-maxWornRotation > local92 || maxWornRotation < local92) {
                if ((local92 <= 0 || local92 > 8192) && local92 > -8192) {
                    this.wornRotation[slot] = local87 - maxWornRotation & 0x3FFF;
                } else {
                    this.wornRotation[slot] = maxWornRotation + local87 & 0x3FFF;
                }
                return false;
            }

            this.wornRotation[slot] = -1;
            for (@Pc(112) int local112 = 0; local112 < WearposDefaults.instance.hidden.length; local112++) {
                if (this.wornRotation[local112] != -1) {
                    return true;
                }
            }

            this.wornRotation = null;
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
                    } else if (replayMode == SeqReplayMode.RESTART_LOOP) {
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
            loopMode =  2;
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
    public abstract boolean enableMessages();

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(ILjava/lang/String;ZII)V")
    public final void setChatLine(@OriginalArg(1) String text, @OriginalArg(3) int effect, @OriginalArg(4) int colour, @OriginalArg(0) int duration) {
        if (this.line == null) {
            this.line = new EntityChatLine();
        }

        this.line.effect = effect;
        this.line.remaining = this.line.duration = duration;
        this.line.colour = colour;
        this.line.text = text;
    }

    @OriginalMember(owner = "client!cg", name = "a", descriptor = "(IIIIII)V")
    protected final void method9314(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(11) int local11 = super.x2 + super.x1 >> 1;
        @Pc(20) int local20 = super.z2 + super.z1 >> 1;
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

        this.modelTranslateY = (this.modelTranslateY >> 1) - super.y;
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
    public final void stopMoving() {
        this.pathPointer = 0;
        this.animationPathPointer = 0;
    }

    @OriginalMember(owner = "client!cg", name = "finalize", descriptor = "()V")
    @Override
    public final void finalize() {
        if (this.particleSystem != null) {
            this.particleSystem.stopped();
        }
    }

    @OriginalMember(owner = "client!cg", name = "i", descriptor = "(B)Lclient!pda;")
    public final BASType getBASType() {
        @Pc(13) int local13 = this.getBasId(0);
        return local13 == -1 ? Static636.A_BAS_TYPE___1 : BASTypeList.instance.list(local13);
    }

    @OriginalMember(owner = "client!cg", name = "b", descriptor = "(B)Z")
    @Override
    public final boolean isStationary() {
        return false;
    }

    @OriginalMember(owner = "client!cg", name = "d", descriptor = "(I)Lclient!dj;")
    public abstract EntityChatLine getChatLine(@OriginalArg(0) int arg0);

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
                    arg2[local35].apply(arg3);
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
            if ((this.particleSystem == null || this.particleSystem.removed) && (local17 > 0 || local21 > 0)) {
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
                this.particleSystem.update(arg0, TimeUtils.clock, local138, local191);
                this.aBoolean820 = true;
            }
        } else if (this.particleSystem != null) {
            this.particleSystem.setClock(TimeUtils.clock);
        }
        if (this.particleSystem != null) {
            this.particleSystem.updateBounds(super.level, super.x1, super.x2, super.z1, super.z2);
        }
    }

    @OriginalMember(owner = "client!cg", name = "m", descriptor = "(I)I")
    protected abstract int getBasId(@OriginalArg(0) int arg0);
}

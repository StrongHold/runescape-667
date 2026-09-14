import com.jagex.Entity;
import com.jagex.ParticleList;
import com.jagex.PickableEntity;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import com.jagex.game.MoveSpeed;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.npctype.NPCTypeCustomisation;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!wj")
public final class NPCEntity extends PathingEntity {

    @OriginalMember(owner = "client!wj", name = "Qc", descriptor = "Ljava/lang/String;")
    public String name;

    @OriginalMember(owner = "client!wj", name = "Gc", descriptor = "Lclient!vk;")
    public NPCTypeCustomisation customisation;

    @OriginalMember(owner = "client!wj", name = "Yc", descriptor = "Lclient!o;")
    public NPCType type;

    @OriginalMember(owner = "client!wj", name = "Sc", descriptor = "I")
    public int combatLevel;

    @OriginalMember(owner = "client!wj", name = "ad", descriptor = "I")
    public int turnToX = -1;

    @OriginalMember(owner = "client!wj", name = "fd", descriptor = "I")
    public int turnToZ = -1;

    @OriginalMember(owner = "client!wj", name = "Jc", descriptor = "I")
    public int customiseHeadCount = 1;

    @OriginalMember(owner = "client!wj", name = "Dc", descriptor = "I")
    public int customiseCount = 1;

    @OriginalMember(owner = "client!wj", name = "<init>", descriptor = "()V")
    public NPCEntity() {
    }

    @OriginalMember(owner = "client!wj", name = "<init>", descriptor = "(I)V")
    public NPCEntity(@OriginalArg(0) int arg0) {
        super(arg0);
    }

    @OriginalMember(owner = "client!pla", name = "a", descriptor = "(ILclient!wj;)I")
    public static int currentSound(@OriginalArg(1) NPCEntity entity) {
        @Pc(6) NPCType type = entity.type;
        if (type.multinpcs != null) {
            type = type.getMultiNPC(TimedVarDomain.instance);
            if (type == null) {
                return -1;
            }
        }

        @Pc(22) int sound = type.walkSound;
        @Pc(32) BASType basType = entity.getBASType();
        @Pc(37) int animationId = entity.animator.getAnimationId();
        if (animationId == -1 || entity.ready) {
            sound = type.readySound;
        } else if (animationId == basType.run || animationId == basType.runFollowTurn180 || animationId == basType.runFollowTurnCw || animationId == basType.runFollowTurnCcw) {
            sound = type.runSound;
        } else if (animationId == basType.crawl || animationId == basType.crawlFollowTurn180 || animationId == basType.crawlFollowTurnCw || animationId == basType.crawlFollowTurnCcw) {
            sound = type.crawlSound;
        }
        return sound;
    }

    @OriginalMember(owner = "client!wj", name = "j", descriptor = "(I)V")
    @Override
    public void stopSharingLight(@OriginalArg(0) int arg0) {
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!wj", name = "j", descriptor = "(B)Z")
    public boolean hasType() {
        return this.type != null;
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(IZLclient!ha;IBILclient!eo;)V")
    @Override
    public void shareLight(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) Entity arg6) {
        if (arg4 <= 101) {
            Static712.method9329((byte) 99);
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(IIZLclient!ha;)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit) {
        if (this.type == null || !this.method9324(131072, toolkit)) {
            return false;
        }
        @Pc(20) Matrix local20 = toolkit.scratchMatrix();
        @Pc(25) int local25 = super.yaw.getValue(16383);
        local20.rotate(local25);
        local20.translate(super.x, super.y, super.z);
        @Pc(38) boolean local38 = arg2;
        for (@Pc(40) int local40 = 0; local40 < super.aModelArray3.length; local40++) {
            if (super.aModelArray3[local40] != null) {
                @Pc(71) boolean var10000;
                label50:
                {
                    if (this.type.pickSizeShift <= 0) {
                        label48:
                        {
                            if (this.type.anInt6706 == -1) {
                                if (this.type.size == 1) {
                                    break label48;
                                }
                                var10000 = false;
                            } else {
                                if (this.type.anInt6706 == 1) {
                                    break label48;
                                }
                                var10000 = false;
                            }
                            if (!var10000) {
                                var10000 = false;
                                break label50;
                            }
                        }
                    }
                    var10000 = true;
                }
                @Pc(89) boolean local89 = var10000;
                @Pc(105) boolean local105;
                if (OrthoMode.enabled) {
                    local105 = super.aModelArray3[local40].pickedOrtho(y, x, local20, local89, this.type.pickSizeShift, OrthoMode.renderZoom);
                } else {
                    local105 = super.aModelArray3[local40].picked(y, x, local20, local89, this.type.pickSizeShift);
                }
                if (local105) {
                    local38 = true;
                    break;
                }
            }
        }
        for (@Pc(136) int local136 = 0; local136 < super.aModelArray3.length; local136++) {
            super.aModelArray3[local136] = null;
        }
        return local38;
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit arg0) {
        if (this.type == null || !this.method9324(2048, arg0)) {
            return null;
        }
        @Pc(22) Matrix local22 = arg0.scratchMatrix();
        @Pc(27) int local27 = super.yaw.getValue(16383);
        local22.rotate(local27);
        @Pc(45) Tile local45 = Static334.activeTiles[super.level][super.x >> EnvironmentLight.anInt1066][super.z >> EnvironmentLight.anInt1066];
        if (local45 == null || local45.groundDecor == null) {
            super.anInt10732 = (int) ((float) super.anInt10732 - (float) super.anInt10732 / 10.0F);
        } else {
            @Pc(75) int local75 = super.anInt10732 - local45.groundDecor.offsetY;
            super.anInt10732 = (int) ((float) super.anInt10732 - (float) local75 / 10.0F);
        }
        local22.translate(super.x, -super.anInt10732 + super.y - 20, super.z);
        @Pc(104) BASType local104 = this.getBASType();
        @Pc(118) NPCType local118 = this.type.multinpcs == null ? this.type : this.type.getMultiNPC(TimedVarDomain.instance);
        super.transparent = false;
        @Pc(123) PickableEntity local123 = null;
        if (ClientOptions.instance.spotShadows.getValue() == 1 && local118.hasShadow && local104.animateShadow) {
            @Pc(159) Animator local159 = super.actionAnimator.isAnimating() && super.actionAnimator.isDelayed() ? super.actionAnimator : null;
            @Pc(179) Animator local179 = super.animator.isAnimating() && (!super.ready || local159 == null) ? super.animator : null;
            @Pc(223) Model local223 = ShadowList.model(this.type.shadowInnerAlpha & 0xFF, super.aModelArray3[0], super.modelRotateZ, this.type.shadowInnerColour & 0xFFFF, super.modelTranslateY, this.type.size, arg0, this.type.shadowOuterAlpha & 0xFF, local179 == null ? local159 : local179, super.modelRotateX, local27, this.type.shadowOuterColour & 0xFFFF);
            if (local223 != null) {
                local123 = Static642.allocatePickableEntity(this.method9330(), super.aModelArray3.length + 1);
                super.transparent = true;
                arg0.C(false);
                if (OrthoMode.enabled) {
                    local223.renderOrtho(local22, local123.pickingCylinders[super.aModelArray3.length], OrthoMode.renderZoom, 0);
                } else {
                    local223.render(local22, local123.pickingCylinders[super.aModelArray3.length], 0);
                }
                arg0.C(true);
            }
        }
        local22.rotate(local27);
        local22.translate(super.x, -super.anInt10732 + super.y - 5, super.z);
        if (local123 == null) {
            local123 = Static642.allocatePickableEntity(this.method9330(), super.aModelArray3.length);
        }
        this.method9319(arg0, false, super.aModelArray3, local22);
        @Pc(314) int local314;
        if (OrthoMode.enabled) {
            for (local314 = 0; local314 < super.aModelArray3.length; local314++) {
                if (super.aModelArray3[local314] != null) {
                    super.aModelArray3[local314].renderOrtho(local22, local123.pickingCylinders[local314], OrthoMode.renderZoom, 0);
                }
            }
        } else {
            for (local314 = 0; local314 < super.aModelArray3.length; local314++) {
                if (super.aModelArray3[local314] != null) {
                    super.aModelArray3[local314].render(local22, local123.pickingCylinders[local314], 0);
                }
            }
        }
        if (super.particleSystem != null) {
            @Pc(394) ParticleList local394 = super.particleSystem.getList();
            if (OrthoMode.enabled) {
                arg0.renderOrtho(local394, OrthoMode.renderZoom);
            } else {
                arg0.render(local394);
            }
        }
        for (@Pc(419) int local419 = 0; local419 < super.aModelArray3.length; local419++) {
            if (super.aModelArray3[local419] != null) {
                super.transparent |= super.aModelArray3[local419].F();
            }
            super.aModelArray3[local419] = null;
        }
        super.anInt10704 = Static198.anInt3276;
        return local123;
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(IZLclient!ha;)Z")
    public boolean method9324(@OriginalArg(0) int arg0, @OriginalArg(2) Toolkit toolkit) {
        @Pc(5) int local5 = arg0;
        @Pc(9) BASType basType = this.getBASType();
        @Pc(27) Animator local27 = super.actionAnimator.isAnimating() && !super.actionAnimator.isDelayed() ? super.actionAnimator : null;
        @Pc(47) Animator local47 = super.animator.isAnimating() && (!super.ready || local27 == null) ? super.animator : null;
        @Pc(50) int local50 = basType.hillWidth;
        @Pc(53) int local53 = basType.hillHeight;
        if (local50 != 0 || local53 != 0 || basType.rollTargetAngle != 0 || basType.pitchTargetAngle != 0) {
            arg0 |= 0x7;
        }
        @Pc(100) boolean local100 = super.recolScale != 0 && super.recolStart <= TimeUtils.clock && TimeUtils.clock < super.recolEnd;
        if (local100) {
            arg0 |= 0x80000;
        }
        @Pc(111) int yaw = super.yaw.getValue(16383);
        @Pc(134) Model local134 = super.aModelArray3[0] = this.type.getModel(TimedVarDomain.instance, toolkit, BASTypeList.instance, local27, yaw, super.wornRotation, this.customisation, local47, arg0, super.wornAnimators);
        if (local134 == null) {
            return false;
        }
        super.minY = local134.fa();
        super.sphereRadius = local134.ma();
        this.method9306(local134);
        if (local50 == 0 && local53 == 0) {
            this.method9314(yaw, 0, 0, this.getSize() << 9, this.getSize() << 9, -86);
        } else {
            this.method9314(yaw, basType.hillMaxAngleX, basType.hillMaxAngleY, local50, local53, -119);
            if (super.modelRotateX != 0) {
                super.aModelArray3[0].FA(super.modelRotateX);
            }
            if (super.modelRotateZ != 0) {
                super.aModelArray3[0].VA(super.modelRotateZ);
            }
            if (super.modelTranslateY != 0) {
                super.aModelArray3[0].H(0, super.modelTranslateY, 0);
            }
        }
        if (local100) {
            local134.adjustColours(super.recolHue, super.recolSaturation, super.recolLightness, super.recolScale & 0xFF);
        }
        this.method9297(local5, local53, toolkit, basType, yaw, local50);
        return true;
    }

    @OriginalMember(owner = "client!wj", name = "f", descriptor = "(B)I")
    @Override
    public int getY() {
        if (this.type.multinpcs != null) {
            @Pc(19) NPCType local19 = this.type.getMultiNPC(TimedVarDomain.instance);
            if (local19 != null && local19.height != -1) {
                return local19.height;
            }
        }
        return this.type.height == -1 ? super.getY() : this.type.height;
    }

    @OriginalMember(owner = "client!wj", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 >= -93) {
            this.getBasId(98);
        }
        return null;
    }

    @OriginalMember(owner = "client!wj", name = "d", descriptor = "(I)Lclient!dj;")
    @Override
    public EntityChatLine getChatLine(@OriginalArg(0) int arg0) {
        if (arg0 == -3109) {
            return super.line != null && super.line.text == null ? null : super.line;
        } else {
            return null;
        }
    }

    @OriginalMember(owner = "client!wj", name = "e", descriptor = "(B)Z")
    @Override
    public boolean enableMessages() {
        return GraphicsDefaults.instance.npcShouldDisplayChat;
    }

    @OriginalMember(owner = "client!wj", name = "h", descriptor = "(B)I")
    @Override
    public int method9304(@OriginalArg(0) byte arg0) {
        if (arg0 >= -48) {
            this.method9290(117);
        }
        if (this.type.multinpcs != null) {
            @Pc(22) NPCType local22 = this.type.getMultiNPC(TimedVarDomain.instance);
            if (local22 != null && local22.mobilisingArmiesIcon != -1) {
                return local22.mobilisingArmiesIcon;
            }
        }
        return this.type.mobilisingArmiesIcon;
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(IIB)V")
    public void move(@OriginalArg(0) int speed, @OriginalArg(1) int arg1) {
        @Pc(10) int local10 = super.pathX[0];
        @Pc(20) int local20 = super.pathZ[0];
        if (arg1 == 0) {
            local20++;
        }
        if (arg1 == 1) {
            local20++;
            local10++;
        }
        if (arg1 == 2) {
            local10++;
        }
        if (arg1 == 3) {
            local20--;
            local10++;
        }
        if (arg1 == 4) {
            local20--;
        }
        if (arg1 == 5) {
            local20--;
            local10--;
        }
        if (arg1 == 6) {
            local10--;
        }
        if (arg1 == 7) {
            local10--;
            local20++;
        }
        if (super.actionAnimator.isAnimating() && super.actionAnimator.getAnimation().walkingPrecedence == 1) {
            super.actionAnimations = null;
            super.actionAnimator.update(true, -1);
        }
        for (@Pc(110) int local110 = 0; local110 < super.spotAnims.length; local110++) {
            if (super.spotAnims[local110].id != -1) {
                @Pc(131) SpotAnimationType local131 = SpotAnimationTypeList.instance.list(super.spotAnims[local110].id);
                if (local131.loopSeq && local131.seq != -1 && SeqTypeList.instance.list(local131.seq).walkingPrecedence == 1) {
                    super.spotAnims[local110].animator.update(true, -1);
                    super.spotAnims[local110].id = -1;
                }
            }
        }
        if (super.pathPointer < super.pathX.length - 1) {
            super.pathPointer++;
        }
        for (@Pc(201) int local201 = super.pathPointer; local201 > 0; local201--) {
            super.pathX[local201] = super.pathX[local201 - 1];
            super.pathZ[local201] = super.pathZ[local201 - 1];
            super.pathSpeed[local201] = super.pathSpeed[local201 - 1];
        }
        super.pathX[0] = local10;
        super.pathZ[0] = local20;
        super.pathSpeed[0] = (byte) speed;
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(IZIIII)V")
    public void clearPath(@OriginalArg(1) boolean arg0, @OriginalArg(2) int z, @OriginalArg(3) int x, @OriginalArg(4) int level, @OriginalArg(5) int size) {
        super.level = super.virtualLevel = (byte) level;
        if (Static441.isBridgeAt(z, x)) {
            super.virtualLevel++;
        }

        if (super.actionAnimator.isAnimating() && super.actionAnimator.getAnimation().walkingPrecedence == 1) {
            super.actionAnimations = null;
            super.actionAnimator.update(true, -1);
        }

        for (@Pc(61) int i = 0; i < super.spotAnims.length; i++) {
            if (super.spotAnims[i].id != -1) {
                @Pc(81) SpotAnimationType type = SpotAnimationTypeList.instance.list(super.spotAnims[i].id);

                if (type.loopSeq && type.seq != -1 && SeqTypeList.instance.list(type.seq).walkingPrecedence == 1) {
                    super.spotAnims[i].animator.update(true, -1);
                    super.spotAnims[i].id = -1;
                }
            }
        }

        if (!arg0) {
            @Pc(134) int local134 = x - super.pathX[0];
            @Pc(142) int local142 = z - super.pathZ[0];

            if (local134 >= -8 && local134 <= 8 && local142 >= -8 && local142 <= 8) {
                if (super.pathX.length - 1 > super.pathPointer) {
                    super.pathPointer++;
                }

                for (@Pc(183) int i = super.pathPointer; i > 0; i--) {
                    super.pathX[i] = super.pathX[i - 1];
                    super.pathZ[i] = super.pathZ[i - 1];
                    super.pathSpeed[i] = super.pathSpeed[i - 1];
                }

                super.pathX[0] = x;
                super.pathSpeed[0] = MoveSpeed.WALK;
                super.pathZ[0] = z;
                return;
            }
        }

        super.pathPointer = 0;
        super.delayedWalkingTicks = 0;
        super.pathX[0] = x;
        super.animationPathPointer = 0;
        super.pathZ[0] = z;
        super.x = (super.pathX[0] << 9) + (size << 8);
        super.z = (super.pathZ[0] << 9) + (size << 8);

        if (super.particleSystem != null) {
            super.particleSystem.restart();
        }
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(IILjava/lang/String;B)V")
    public void chat(@OriginalArg(0) int colour, @OriginalArg(1) int effect, @OriginalArg(2) String text) {
        @Pc(17) int duration = GameShell.speed() * GraphicsDefaults.instance.npcChatTimeout;
        this.setChatLine(text, effect, colour, duration);
    }

    @OriginalMember(owner = "client!wj", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1) {
        if (this.type == null || !super.aBoolean820 && !this.method9324(0, arg0)) {
            return;
        }
        @Pc(26) Matrix local26 = arg0.scratchMatrix();
        local26.rotate(super.yaw.getValue(16383));
        local26.translate(super.x, super.y - 20, super.z);
        this.method9319(arg0, super.aBoolean820, super.aModelArray3, local26);
        for (@Pc(53) int local53 = 0; local53 < super.aModelArray3.length; local53++) {
            super.aModelArray3[local53] = null;
        }
        if (arg1 != -5) {
            this.getCylinder(null, -21);
        }
    }

    @OriginalMember(owner = "client!wj", name = "i", descriptor = "(I)Z")
    @Override
    public boolean method9290(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            this.hasType();
        }
        return false;
    }

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "(Lclient!o;Z)V")
    public void setupNewNPCType(@OriginalArg(0) NPCType type) {
        if (type != this.type && MiniMenu.open && MiniMenu.hasNpcOp(super.slot)) {
            MiniMenu.close();
        }

        this.type = type;

        if (this.type != null) {
            this.name = this.type.name;
            this.combatLevel = this.type.combatLevel;
        }

        if (super.particleSystem != null) {
            super.particleSystem.restart();
        }
    }

    @OriginalMember(owner = "client!wj", name = "m", descriptor = "(I)I")
    @Override
    protected int getBasId(@OriginalArg(0) int arg0) {
        if (this.type.multinpcs != null) {
            @Pc(15) NPCType local15 = this.type.getMultiNPC(TimedVarDomain.instance);
            if (local15 != null && local15.basId != -1) {
                return local15.basId;
            }
        }
        if (arg0 != 0) {
            this.turnToX = -66;
        }
        return this.type.basId;
    }

    @OriginalMember(owner = "client!wj", name = "n", descriptor = "(I)Z")
    public boolean method9330() {
        return this.type.interactive;
    }

    @OriginalMember(owner = "client!wj", name = "d", descriptor = "(B)I")
    @Override
    public int getPickSizeShift() {
        return this.type == null ? 0 : this.type.pickSizeShift;
    }
}

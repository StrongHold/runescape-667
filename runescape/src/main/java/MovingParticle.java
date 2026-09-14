import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterType;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorType;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Ground;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorTypeList;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pp")
public final class MovingParticle extends Particle {

    @OriginalMember(owner = "client!pp", name = "F", descriptor = "S")
    public short aShort91;

    @OriginalMember(owner = "client!pp", name = "x", descriptor = "I")
    public int anInt7541;

    @OriginalMember(owner = "client!pp", name = "D", descriptor = "Lclient!rf;")
    public ParticleEmitter emitter;

    @OriginalMember(owner = "client!pp", name = "z", descriptor = "S")
    public short aShort96;

    @OriginalMember(owner = "client!pp", name = "C", descriptor = "S")
    public short aShort93;

    @OriginalMember(owner = "client!pp", name = "B", descriptor = "S")
    public short aShort92;

    @OriginalMember(owner = "client!pp", name = "E", descriptor = "S")
    public short aShort95;

    @OriginalMember(owner = "client!pp", name = "y", descriptor = "S")
    public short aShort94;

    @OriginalMember(owner = "client!pp", name = "A", descriptor = "I")
    public int anInt7542;

    @OriginalMember(owner = "client!pp", name = "<init>", descriptor = "(Lclient!rf;IIIIIIIIIIIZZ)V")
    public MovingParticle(@OriginalArg(0) ParticleEmitter arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) int arg9, @OriginalArg(10) int arg10, @OriginalArg(11) int arg11, @OriginalArg(12) boolean arg12, @OriginalArg(13) boolean arg13) {
        this.emitter = arg0;
        super.x = arg1 << 12;
        super.y = arg2 << 12;
        super.z = arg3 << 12;
        super.colour = arg9;
        this.aShort93 = this.aShort96 = (short) arg8;
        super.size = arg10;
        super.texture = arg11;
        super.preserveAmbient = arg13;
        this.aShort92 = (short) arg4;
        this.aShort95 = (short) arg5;
        this.aShort94 = (short) arg6;
        this.anInt7542 = arg7;
        super.aByte122 = this.emitter.model.aByte130;
        this.method6693();
    }

    @OriginalMember(owner = "client!pp", name = "c", descriptor = "()V")
    public void method6693() {
        @Pc(4) int local4 = this.emitter.system.anInt4147;
        if (this.emitter.system.movingParticles[local4] != null) {
            this.emitter.system.movingParticles[local4].remove();
        }
        this.emitter.system.movingParticles[local4] = this;
        this.aShort91 = (short) this.emitter.system.anInt4147;
        this.emitter.system.anInt4147 = local4 + 1 & 0x1FFF;
        this.emitter.movingParticles.add(this);
    }

    @OriginalMember(owner = "client!pp", name = "a", descriptor = "(JI)V")
    public void method6694(@OriginalArg(0) long arg0, @OriginalArg(1) int elapsedTime) {
        this.aShort96 = (short) (this.aShort96 - elapsedTime);
        if (this.aShort96 <= 0) {
            this.remove();
            return;
        }
        @Pc(17) int local17 = super.x >> 12;
        @Pc(22) int local22 = super.y >> 12;
        @Pc(27) int local27 = super.z >> 12;
        @Pc(31) ParticleSystem local31 = this.emitter.system;
        @Pc(35) ParticleEmitterType local35 = this.emitter.type;
        if (local35.fadeColour != 0) {
            @Pc(65) int local65;
            if (this.aShort93 - this.aShort96 <= local35.anInt9901) {
                local65 = (super.colour >> 8 & 0xFF00) + (this.anInt7541 >> 16 & 0xFF) + local35.anInt9931 * elapsedTime;
                @Pc(82) int local82 = (super.colour & 0xFF00) + (this.anInt7541 >> 8 & 0xFF) + local35.anInt9920 * elapsedTime;
                @Pc(99) int local99 = ((super.colour & 0xFF) << 8) + (this.anInt7541 & 0xFF) + local35.anInt9916 * elapsedTime;
                if (local65 < 0) {
                    local65 = 0;
                } else if (local65 > 65535) {
                    local65 = 65535;
                }
                if (local82 < 0) {
                    local82 = 0;
                } else if (local82 > 65535) {
                    local82 = 65535;
                }
                if (local99 < 0) {
                    local99 = 0;
                } else if (local99 > 65535) {
                    local99 = 65535;
                }
                super.colour &= 0xFF000000;
                super.colour |= ((local65 & 0xFF00) << 8) + (local82 & 0xFF00) + (local99 >> 8 & 0xFF);
                this.anInt7541 &= 0xFF000000;
                this.anInt7541 |= ((local65 & 0xFF) << 16) + ((local82 & 0xFF) << 8) + (local99 & 0xFF);
            }
            if (this.aShort93 - this.aShort96 <= local35.anInt9925) {
                local65 = (super.colour >> 16 & 0xFF00) + (this.anInt7541 >> 24 & 0xFF) + local35.anInt9887 * elapsedTime;
                if (local65 < 0) {
                    local65 = 0;
                } else if (local65 > 65535) {
                    local65 = 65535;
                }
                super.colour &= 0xFFFFFF;
                super.colour |= (local65 & 0xFF00) << 16;
                this.anInt7541 &= 0xFFFFFF;
                this.anInt7541 |= (local65 & 0xFF) << 24;
            }
        }
        if (local35.endSpeed != -1 && this.aShort93 - this.aShort96 <= local35.lb) {
            this.anInt7542 += local35.anInt9889 * elapsedTime;
        }
        if (local35.endSize != -1 && this.aShort93 - this.aShort96 <= local35.anInt9909) {
            super.size += local35.anInt9910 * elapsedTime;
        }
        @Pc(296) double local296 = this.aShort92;
        @Pc(300) double local300 = this.aShort95;
        @Pc(304) double local304 = this.aShort94;
        @Pc(306) boolean local306 = false;
        @Pc(317) int local317;
        @Pc(324) int local324;
        @Pc(331) int local331;
        @Pc(348) int local348;
        @Pc(356) long local356;
        if (local35.decelerationType == 1) {
            local317 = local17 - this.emitter.related1.anInt4271;
            local324 = local22 - this.emitter.related1.anInt4278;
            local331 = local27 - this.emitter.related1.anInt4273;
            local348 = (int) Math.sqrt(local317 * local317 + local324 * local324 + local331 * local331) >> 2;
            local356 = local35.decelerationRate * local348 * elapsedTime;
            this.anInt7542 = (int) ((long) this.anInt7542 - ((long) this.anInt7542 * local356 >> 18));
        } else if (local35.decelerationType == 2) {
            local317 = local17 - this.emitter.related1.anInt4271;
            local324 = local22 - this.emitter.related1.anInt4278;
            local331 = local27 - this.emitter.related1.anInt4273;
            local348 = local317 * local317 + local324 * local324 + local331 * local331;
            local356 = local35.decelerationRate * local348 * elapsedTime;
            this.anInt7542 = (int) ((long) this.anInt7542 - ((long) this.anInt7542 * local356 >> 28));
        }
        if (local35.localEffectors != null) {
            @Pc(437) Node local437 = local31.effectorCache.sentinel;
            for (@Pc(440) Node local440 = local437.next; local440 != local437; local440 = local440.next) {
                @Pc(444) ParticleEffector local444 = (ParticleEffector) local440;
                @Pc(447) ParticleEffectorType local447 = local444.type;
                if (local447.visibility != 1) {
                    @Pc(453) boolean local453 = false;
                    for (@Pc(455) int local455 = 0; local455 < local35.localEffectors.length; local455++) {
                        if (local35.localEffectors[local455] == local447.id) {
                            local453 = true;
                            break;
                        }
                    }
                    if (local453) {
                        @Pc(480) double local480 = local17 - local444.x;
                        @Pc(486) double local486 = local22 - local444.y;
                        @Pc(492) double local492 = local27 - local444.z;
                        @Pc(504) double local504 = local480 * local480 + local486 * local486 + local492 * local492;
                        if (!(local504 > (double) local447.maxRange)) {
                            @Pc(513) double local513 = Math.sqrt(local504);
                            if (local513 == 0.0D) {
                                local513 = 1.0D;
                            }
                            @Pc(545) double local545 = (local480 * (double) local444.directionZ + local486 * (double) local447.dirY + local492 * (double) local444.directionX) * 65535.0D / ((double) local447.dirLength * local513);
                            if (!(local545 < (double) local447.cosTheta)) {
                                @Pc(553) double local553 = 0.0D;
                                if (local447.effectType == 1) {
                                    local553 = local513 / 16.0D * (double) local447.strength;
                                } else if (local447.effectType == 2) {
                                    local553 = local513 / 16.0D * (local513 / 16.0D) * (double) local447.strength;
                                }
                                if (local447.constantStrength != 0) {
                                    @Pc(678) double local678 = local480 / local513 * (double) local447.dirLength;
                                    @Pc(686) double local686 = local486 / local513 * (double) local447.dirLength;
                                    @Pc(694) double local694 = local492 / local513 * (double) local447.dirLength;
                                    if (local447.constantSpeed == 0) {
                                        local296 += local678 * (double) elapsedTime;
                                        local300 += local686 * (double) elapsedTime;
                                        local304 += local694 * (double) elapsedTime;
                                        local306 = true;
                                    } else {
                                        super.x = (int) ((double) super.x + local678 * (double) elapsedTime);
                                        super.y = (int) ((double) super.y + local686 * (double) elapsedTime);
                                        super.z = (int) ((double) super.z + local694 * (double) elapsedTime);
                                    }
                                } else if (local447.constantSpeed == 0) {
                                    local296 += ((double) local444.directionZ - local553) * (double) elapsedTime;
                                    local300 += ((double) local447.dirY - local553) * (double) elapsedTime;
                                    local304 += ((double) local444.directionX - local553) * (double) elapsedTime;
                                    local306 = true;
                                } else {
                                    super.x = (int) ((double) super.x + ((double) local444.directionZ - local553) * (double) elapsedTime);
                                    super.y = (int) ((double) super.y + ((double) local447.dirY - local553) * (double) elapsedTime);
                                    super.z = (int) ((double) super.z + ((double) local444.directionX - local553) * (double) elapsedTime);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (local35.generalEffectors != null) {
            for (local317 = 0; local317 < local35.generalEffectors.length; local317++) {
                @Pc(776) ParticleEffector local776 = (ParticleEffector) ParticleManager.effectorsCache.get(local35.generalEffectors[local317]);
                while (local776 != null) {
                    @Pc(780) ParticleEffectorType local780 = local776.type;
                    @Pc(786) double local786 = local17 - local776.x;
                    @Pc(792) double local792 = local22 - local776.y;
                    @Pc(798) double local798 = local27 - local776.z;
                    @Pc(810) double local810 = local786 * local786 + local792 * local792 + local798 * local798;
                    if (local810 > (double) local780.maxRange) {
                        local776 = (ParticleEffector) ParticleManager.effectorsCache.nextWithSameKey();
                    } else {
                        @Pc(825) double local825 = Math.sqrt(local810);
                        if (local825 == 0.0D) {
                            local825 = 1.0D;
                        }
                        @Pc(857) double local857 = (local786 * (double) local776.directionZ + local792 * (double) local780.dirY + local798 * (double) local776.directionX) * 65535.0D / ((double) local780.dirLength * local825);
                        if (local857 < (double) local780.cosTheta) {
                            local776 = (ParticleEffector) ParticleManager.effectorsCache.nextWithSameKey();
                        } else {
                            @Pc(871) double local871 = 0.0D;
                            if (local780.effectType == 1) {
                                local871 = local825 / 16.0D * (double) local780.strength;
                            } else if (local780.effectType == 2) {
                                local871 = local825 / 16.0D * (local825 / 16.0D) * (double) local780.strength;
                            }
                            if (local780.constantStrength != 0) {
                                @Pc(996) double local996 = local786 / local825 * (double) local780.dirLength;
                                @Pc(1004) double local1004 = local792 / local825 * (double) local780.dirLength;
                                @Pc(1012) double local1012 = local798 / local825 * (double) local780.dirLength;
                                if (local780.constantSpeed == 0) {
                                    local296 += local996 * (double) elapsedTime;
                                    local300 += local1004 * (double) elapsedTime;
                                    local304 += local1012 * (double) elapsedTime;
                                    local306 = true;
                                } else {
                                    super.x = (int) ((double) super.x + local996 * (double) elapsedTime);
                                    super.y = (int) ((double) super.y + local1004 * (double) elapsedTime);
                                    super.z = (int) ((double) super.z + local1012 * (double) elapsedTime);
                                }
                            } else if (local780.constantSpeed == 0) {
                                local296 += ((double) local776.directionZ - local871) * (double) elapsedTime;
                                local300 += ((double) local780.dirY - local871) * (double) elapsedTime;
                                local304 += ((double) local776.directionX - local871) * (double) elapsedTime;
                                local306 = true;
                            } else {
                                super.x = (int) ((double) super.x + ((double) local776.directionZ - local871) * (double) elapsedTime);
                                super.y = (int) ((double) super.y + ((double) local780.dirY - local871) * (double) elapsedTime);
                                super.z = (int) ((double) super.z + ((double) local776.directionX - local871) * (double) elapsedTime);
                            }
                            local776 = (ParticleEffector) ParticleManager.effectorsCache.nextWithSameKey();
                        }
                    }
                }
            }
        }
        if (local35.globalEffectors != null) {
            if (local35.anIntArray774 == null) {
                local35.anIntArray774 = new int[local35.globalEffectors.length];
                for (local317 = 0; local317 < local35.globalEffectors.length; local317++) {
                    ParticleEffectorTypeList.get(local35.globalEffectors[local317]);
                    local35.anIntArray774[local317] = ((IntNode) ParticleEffectorTypeList.table.get(local35.globalEffectors[local317])).value;
                }
            }
            for (local317 = 0; local317 < local35.anIntArray774.length; local317++) {
                @Pc(1137) ParticleEffectorType local1137 = ParticleEffectorTypeList.types[local35.anIntArray774[local317]];
                if (local1137.constantSpeed == 0) {
                    local296 += local1137.dirX * elapsedTime;
                    local300 += local1137.dirY * elapsedTime;
                    local304 += local1137.dirZ * elapsedTime;
                    local306 = true;
                } else {
                    super.x += local1137.dirX * elapsedTime;
                    super.y += local1137.dirY * elapsedTime;
                    super.z += local1137.dirZ * elapsedTime;
                }
            }
        }
        if (local306) {
            while (true) {
                if (!(local296 > 32767.0D) && !(local300 > 32767.0D) && !(local304 > 32767.0D) && !(local296 < -32767.0D) && !(local300 < -32767.0D) && !(local304 < -32767.0D)) {
                    this.aShort92 = (short) (int) local296;
                    this.aShort95 = (short) (int) local300;
                    this.aShort94 = (short) (int) local304;
                    break;
                }
                local296 /= 2.0D;
                local300 /= 2.0D;
                local304 /= 2.0D;
                this.anInt7542 <<= 0x1;
            }
        }
        super.x = (int) ((long) super.x + ((long) this.aShort92 * (long) (this.anInt7542 << 2) >> 23) * (long) elapsedTime);
        super.y = (int) ((long) super.y + ((long) this.aShort95 * (long) (this.anInt7542 << 2) >> 23) * (long) elapsedTime);
        super.z = (int) ((long) super.z + ((long) this.aShort94 * (long) (this.anInt7542 << 2) >> 23) * (long) elapsedTime);
    }

    @OriginalMember(owner = "client!pp", name = "a", descriptor = "(Lclient!ha;J)V")
    public void method6695(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) long arg1) {
        @Pc(6) int local6 = super.x >> EnvironmentLight.anInt1066 + 12;
        @Pc(13) int local13 = super.z >> EnvironmentLight.anInt1066 + 12;
        @Pc(18) int local18 = super.y >> 12;
        if (local18 > 0 || local18 < -262144 || local6 < 0 || local6 >= Static619.tileMaxX || local13 < 0 || local13 >= Static662.tileMaxZ) {
            this.remove();
            return;
        }
        @Pc(40) ParticleSystem local40 = this.emitter.system;
        @Pc(44) ParticleEmitterType local44 = this.emitter.type;
        @Pc(46) Ground[] local46 = Static246.ground;
        @Pc(49) int local49 = local40.level;
        @Pc(58) Tile local58 = Static334.activeTiles[local40.level][local6][local13];
        if (local58 != null) {
            local49 = local58.level;
        }
        @Pc(71) int local71 = local46[local49].getHeight(local6, local13);
        @Pc(86) int local86;
        if (local49 < Static299.tileMaxLevel - 1) {
            local86 = local46[local49 + 1].getHeight(local6, local13);
        } else {
            local86 = local71 - (0x8 << EnvironmentLight.anInt1066);
        }
        if (local44.aBoolean764) {
            if (local44.minHeightLevel == -1 && local18 > local71) {
                this.remove();
                return;
            }
            if (local44.minHeightLevel >= 0 && local18 > local46[local44.minHeightLevel].getHeight(local6, local13)) {
                this.remove();
                return;
            }
            if (local44.maxHeightLevel == -1 && local18 < local86) {
                this.remove();
                return;
            }
            if (local44.maxHeightLevel >= 0 && local18 < local46[local44.maxHeightLevel + 1].getHeight(local6, local13)) {
                this.remove();
                return;
            }
        }
        @Pc(154) int local154;
        for (local154 = Static299.tileMaxLevel - 1; local154 > 0 && local18 > local46[local154].getHeight(local6, local13); local154--) {
        }
        if (local44.collidesWithGround && local154 == 0 && local18 > local46[0].getHeight(local6, local13)) {
            this.remove();
        } else if (local154 == Static299.tileMaxLevel - 1 && local46[local154].getHeight(local6, local13) - local18 > 0x8 << EnvironmentLight.anInt1066) {
            this.remove();
        } else {
            local58 = Static334.activeTiles[local154][local6][local13];
            @Pc(261) int local261;
            if (local58 == null) {
                if (local154 == 0 || Static334.activeTiles[0][local6][local13] == null) {
                    local58 = Static334.activeTiles[0][local6][local13] = new Tile(0);
                }
                @Pc(251) boolean local251 = Static334.activeTiles[0][local6][local13].tile != null;
                if (local154 == 3 && local251) {
                    this.remove();
                    return;
                }
                for (local261 = 1; local261 <= local154; local261++) {
                    if (Static334.activeTiles[local261][local6][local13] == null) {
                        local58 = Static334.activeTiles[local261][local6][local13] = new Tile(local261);
                        if (local251) {
                            local58.level++;
                        }
                    }
                }
            }
            if (local44.collidesWithLocations) {
                @Pc(304) int local304 = super.x >> 12;
                local261 = super.z >> 12;
                @Pc(318) BoundingCylinder local318;
                if (local58.wall != null) {
                    local318 = local58.wall.getCylinder(arg0, -105);
                    if (local318 != null && local318.contains(local18, local261, local304)) {
                        this.remove();
                        return;
                    }
                }
                if (local58.adjacentWall != null) {
                    local318 = local58.adjacentWall.getCylinder(arg0, -120);
                    if (local318 != null && local318.contains(local18, local261, local304)) {
                        this.remove();
                        return;
                    }
                }
                if (local58.groundDecor != null) {
                    local318 = local58.groundDecor.getCylinder(arg0, -109);
                    if (local318 != null && local318.contains(local18, local261, local304)) {
                        this.remove();
                        return;
                    }
                }
                for (@Pc(375) PositionEntityNode local375 = local58.head; local375 != null; local375 = local375.node) {
                    @Pc(382) BoundingCylinder local382 = local375.entity.getCylinder(arg0, -117);
                    if (local382 != null && local382.contains(local18, local261, local304)) {
                        this.remove();
                        return;
                    }
                }
            }
            local40.list.particles.add(this);
        }
    }

    @OriginalMember(owner = "client!pp", name = "a", descriptor = "(Lclient!rf;IIIIIIIIIIIZZ)V")
    public void method6696(@OriginalArg(0) ParticleEmitter arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) int arg9, @OriginalArg(10) int arg10, @OriginalArg(11) int arg11, @OriginalArg(12) boolean arg12, @OriginalArg(13) boolean arg13) {
        this.emitter = arg0;
        super.x = arg1 << 12;
        super.y = arg2 << 12;
        super.z = arg3 << 12;
        super.colour = arg9;
        this.aShort93 = this.aShort96 = (short) arg8;
        super.size = arg10;
        super.texture = arg11;
        super.preserveAmbient = arg13;
        this.aShort92 = (short) arg4;
        this.aShort95 = (short) arg5;
        this.aShort94 = (short) arg6;
        this.anInt7542 = arg7;
        super.aByte122 = this.emitter.model.aByte130;
        this.method6693();
    }

    @OriginalMember(owner = "client!pp", name = "b", descriptor = "()V")
    public void remove() {
        this.emitter.system.movingParticles[this.aShort91] = null;
        ParticleManager.particles[ParticleManager.particleFreePtr] = this;
        ParticleManager.particleFreePtr = ParticleManager.particleFreePtr + 1 & 0x3FF;
        this.unlink();
        this.unlink2();
    }
}

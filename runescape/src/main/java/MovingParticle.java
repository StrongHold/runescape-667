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
    public short slot;

    @OriginalMember(owner = "client!pp", name = "x", descriptor = "I")
    public int colourFraction;

    @OriginalMember(owner = "client!pp", name = "D", descriptor = "Lclient!rf;")
    public ParticleEmitter emitter;

    @OriginalMember(owner = "client!pp", name = "z", descriptor = "S")
    public short remainingLifetime;

    @OriginalMember(owner = "client!pp", name = "C", descriptor = "S")
    public short lifetime;

    @OriginalMember(owner = "client!pp", name = "B", descriptor = "S")
    public short directionX;

    @OriginalMember(owner = "client!pp", name = "E", descriptor = "S")
    public short directionY;

    @OriginalMember(owner = "client!pp", name = "y", descriptor = "S")
    public short directionZ;

    @OriginalMember(owner = "client!pp", name = "A", descriptor = "I")
    public int speed;

    @OriginalMember(owner = "client!pp", name = "<init>", descriptor = "(Lclient!rf;IIIIIIIIIIIZZ)V")
    public MovingParticle(@OriginalArg(0) ParticleEmitter emitter, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int z, @OriginalArg(4) int directionX, @OriginalArg(5) int directionY, @OriginalArg(6) int directionZ, @OriginalArg(7) int speed, @OriginalArg(8) int lifetime, @OriginalArg(9) int colour, @OriginalArg(10) int size, @OriginalArg(11) int texture, @OriginalArg(12) boolean disableHdLighting, @OriginalArg(13) boolean preserveAmbient) {
        this.emitter = emitter;
        super.x = x << 12;
        super.y = y << 12;
        super.z = z << 12;
        super.colour = colour;
        this.lifetime = this.remainingLifetime = (short) lifetime;
        super.size = size;
        super.texture = texture;
        super.preserveAmbient = preserveAmbient;
        this.directionX = (short) directionX;
        this.directionY = (short) directionY;
        this.directionZ = (short) directionZ;
        this.speed = speed;
        super.aByte122 = this.emitter.model.aByte130;
        this.register();
    }

    @OriginalMember(owner = "client!pp", name = "c", descriptor = "()V")
    public void register() {
        @Pc(4) int slotIndex = this.emitter.system.nextParticleSlot;
        if (this.emitter.system.movingParticles[slotIndex] != null) {
            this.emitter.system.movingParticles[slotIndex].remove();
        }
        this.emitter.system.movingParticles[slotIndex] = this;
        this.slot = (short) this.emitter.system.nextParticleSlot;
        this.emitter.system.nextParticleSlot = slotIndex + 1 & 0x1FFF;
        this.emitter.movingParticles.add(this);
    }

    @OriginalMember(owner = "client!pp", name = "a", descriptor = "(JI)V")
    public void tick(@OriginalArg(0) long time, @OriginalArg(1) int elapsedTime) {
        this.remainingLifetime = (short) (this.remainingLifetime - elapsedTime);
        if (this.remainingLifetime <= 0) {
            this.remove();
            return;
        }
        @Pc(17) int worldX = super.x >> 12;
        @Pc(22) int worldY = super.y >> 12;
        @Pc(27) int worldZ = super.z >> 12;
        @Pc(31) ParticleSystem system = this.emitter.system;
        @Pc(35) ParticleEmitterType type = this.emitter.type;
        if (type.fadeColour != 0) {
            @Pc(65) int channel;
            if (this.lifetime - this.remainingLifetime <= type.colourFadeDuration) {
                channel = (super.colour >> 8 & 0xFF00) + (this.colourFraction >> 16 & 0xFF) + type.redFadeStep * elapsedTime;
                @Pc(82) int green = (super.colour & 0xFF00) + (this.colourFraction >> 8 & 0xFF) + type.greenFadeStep * elapsedTime;
                @Pc(99) int blue = ((super.colour & 0xFF) << 8) + (this.colourFraction & 0xFF) + type.blueFadeStep * elapsedTime;
                if (channel < 0) {
                    channel = 0;
                } else if (channel > 65535) {
                    channel = 65535;
                }
                if (green < 0) {
                    green = 0;
                } else if (green > 65535) {
                    green = 65535;
                }
                if (blue < 0) {
                    blue = 0;
                } else if (blue > 65535) {
                    blue = 65535;
                }
                super.colour &= 0xFF000000;
                super.colour |= ((channel & 0xFF00) << 8) + (green & 0xFF00) + (blue >> 8 & 0xFF);
                this.colourFraction &= 0xFF000000;
                this.colourFraction |= ((channel & 0xFF) << 16) + ((green & 0xFF) << 8) + (blue & 0xFF);
            }
            if (this.lifetime - this.remainingLifetime <= type.alphaFadeDuration) {
                channel = (super.colour >> 16 & 0xFF00) + (this.colourFraction >> 24 & 0xFF) + type.alphaFadeStep * elapsedTime;
                if (channel < 0) {
                    channel = 0;
                } else if (channel > 65535) {
                    channel = 65535;
                }
                super.colour &= 0xFFFFFF;
                super.colour |= (channel & 0xFF00) << 16;
                this.colourFraction &= 0xFFFFFF;
                this.colourFraction |= (channel & 0xFF) << 24;
            }
        }
        if (type.endSpeed != -1 && this.lifetime - this.remainingLifetime <= type.speedChangeDuration) {
            this.speed += type.speedChangeStep * elapsedTime;
        }
        if (type.endSize != -1 && this.lifetime - this.remainingLifetime <= type.sizeChangeDuration) {
            super.size += type.sizeChangeStep * elapsedTime;
        }
        @Pc(296) double velocityX = this.directionX;
        @Pc(300) double velocityY = this.directionY;
        @Pc(304) double velocityZ = this.directionZ;
        @Pc(306) boolean velocityChanged = false;
        @Pc(317) int local317;
        @Pc(324) int emitterDeltaY;
        @Pc(331) int emitterDeltaZ;
        @Pc(348) int local348;
        @Pc(356) long drag;
        if (type.decelerationType == 1) {
            local317 = worldX - this.emitter.triangle.anInt4271;
            emitterDeltaY = worldY - this.emitter.triangle.anInt4278;
            emitterDeltaZ = worldZ - this.emitter.triangle.anInt4273;
            local348 = (int) Math.sqrt(local317 * local317 + emitterDeltaY * emitterDeltaY + emitterDeltaZ * emitterDeltaZ) >> 2;
            drag = type.decelerationRate * local348 * elapsedTime;
            this.speed = (int) ((long) this.speed - ((long) this.speed * drag >> 18));
        } else if (type.decelerationType == 2) {
            local317 = worldX - this.emitter.triangle.anInt4271;
            emitterDeltaY = worldY - this.emitter.triangle.anInt4278;
            emitterDeltaZ = worldZ - this.emitter.triangle.anInt4273;
            local348 = local317 * local317 + emitterDeltaY * emitterDeltaY + emitterDeltaZ * emitterDeltaZ;
            drag = type.decelerationRate * local348 * elapsedTime;
            this.speed = (int) ((long) this.speed - ((long) this.speed * drag >> 28));
        }
        if (type.localEffectors != null) {
            @Pc(437) Node sentinel = system.effectorCache.sentinel;
            for (@Pc(440) Node node = sentinel.next; node != sentinel; node = node.next) {
                @Pc(444) ParticleEffector effector = (ParticleEffector) node;
                @Pc(447) ParticleEffectorType effectorType = effector.type;
                if (effectorType.visibility != 1) {
                    @Pc(453) boolean matched = false;
                    for (@Pc(455) int i = 0; i < type.localEffectors.length; i++) {
                        if (type.localEffectors[i] == effectorType.id) {
                            matched = true;
                            break;
                        }
                    }
                    if (matched) {
                        @Pc(480) double deltaX = worldX - effector.x;
                        @Pc(486) double deltaY = worldY - effector.y;
                        @Pc(492) double deltaZ = worldZ - effector.z;
                        @Pc(504) double distanceSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                        if (!(distanceSquared > (double) effectorType.maxRange)) {
                            @Pc(513) double distance = Math.sqrt(distanceSquared);
                            if (distance == 0.0D) {
                                distance = 1.0D;
                            }
                            @Pc(545) double cosine = (deltaX * (double) effector.directionX + deltaY * (double) effectorType.dirY + deltaZ * (double) effector.directionZ) * 65535.0D / ((double) effectorType.dirLength * distance);
                            if (!(cosine < (double) effectorType.cosTheta)) {
                                @Pc(553) double falloff = 0.0D;
                                if (effectorType.effectType == 1) {
                                    falloff = distance / 16.0D * (double) effectorType.strength;
                                } else if (effectorType.effectType == 2) {
                                    falloff = distance / 16.0D * (distance / 16.0D) * (double) effectorType.strength;
                                }
                                if (effectorType.constantStrength != 0) {
                                    @Pc(678) double forceX = deltaX / distance * (double) effectorType.dirLength;
                                    @Pc(686) double forceY = deltaY / distance * (double) effectorType.dirLength;
                                    @Pc(694) double forceZ = deltaZ / distance * (double) effectorType.dirLength;
                                    if (effectorType.constantSpeed == 0) {
                                        velocityX += forceX * (double) elapsedTime;
                                        velocityY += forceY * (double) elapsedTime;
                                        velocityZ += forceZ * (double) elapsedTime;
                                        velocityChanged = true;
                                    } else {
                                        super.x = (int) ((double) super.x + forceX * (double) elapsedTime);
                                        super.y = (int) ((double) super.y + forceY * (double) elapsedTime);
                                        super.z = (int) ((double) super.z + forceZ * (double) elapsedTime);
                                    }
                                } else if (effectorType.constantSpeed == 0) {
                                    velocityX += ((double) effector.directionX - falloff) * (double) elapsedTime;
                                    velocityY += ((double) effectorType.dirY - falloff) * (double) elapsedTime;
                                    velocityZ += ((double) effector.directionZ - falloff) * (double) elapsedTime;
                                    velocityChanged = true;
                                } else {
                                    super.x = (int) ((double) super.x + ((double) effector.directionX - falloff) * (double) elapsedTime);
                                    super.y = (int) ((double) super.y + ((double) effectorType.dirY - falloff) * (double) elapsedTime);
                                    super.z = (int) ((double) super.z + ((double) effector.directionZ - falloff) * (double) elapsedTime);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (type.generalEffectors != null) {
            for (local317 = 0; local317 < type.generalEffectors.length; local317++) {
                @Pc(776) ParticleEffector effector = (ParticleEffector) ParticleManager.effectorsCache.get(type.generalEffectors[local317]);
                while (effector != null) {
                    @Pc(780) ParticleEffectorType effectorType = effector.type;
                    @Pc(786) double deltaX = worldX - effector.x;
                    @Pc(792) double deltaY = worldY - effector.y;
                    @Pc(798) double deltaZ = worldZ - effector.z;
                    @Pc(810) double distanceSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                    if (distanceSquared > (double) effectorType.maxRange) {
                        effector = (ParticleEffector) ParticleManager.effectorsCache.nextWithSameKey();
                    } else {
                        @Pc(825) double distance = Math.sqrt(distanceSquared);
                        if (distance == 0.0D) {
                            distance = 1.0D;
                        }
                        @Pc(857) double cosine = (deltaX * (double) effector.directionX + deltaY * (double) effectorType.dirY + deltaZ * (double) effector.directionZ) * 65535.0D / ((double) effectorType.dirLength * distance);
                        if (cosine < (double) effectorType.cosTheta) {
                            effector = (ParticleEffector) ParticleManager.effectorsCache.nextWithSameKey();
                        } else {
                            @Pc(871) double falloff = 0.0D;
                            if (effectorType.effectType == 1) {
                                falloff = distance / 16.0D * (double) effectorType.strength;
                            } else if (effectorType.effectType == 2) {
                                falloff = distance / 16.0D * (distance / 16.0D) * (double) effectorType.strength;
                            }
                            if (effectorType.constantStrength != 0) {
                                @Pc(996) double forceX = deltaX / distance * (double) effectorType.dirLength;
                                @Pc(1004) double forceY = deltaY / distance * (double) effectorType.dirLength;
                                @Pc(1012) double forceZ = deltaZ / distance * (double) effectorType.dirLength;
                                if (effectorType.constantSpeed == 0) {
                                    velocityX += forceX * (double) elapsedTime;
                                    velocityY += forceY * (double) elapsedTime;
                                    velocityZ += forceZ * (double) elapsedTime;
                                    velocityChanged = true;
                                } else {
                                    super.x = (int) ((double) super.x + forceX * (double) elapsedTime);
                                    super.y = (int) ((double) super.y + forceY * (double) elapsedTime);
                                    super.z = (int) ((double) super.z + forceZ * (double) elapsedTime);
                                }
                            } else if (effectorType.constantSpeed == 0) {
                                velocityX += ((double) effector.directionX - falloff) * (double) elapsedTime;
                                velocityY += ((double) effectorType.dirY - falloff) * (double) elapsedTime;
                                velocityZ += ((double) effector.directionZ - falloff) * (double) elapsedTime;
                                velocityChanged = true;
                            } else {
                                super.x = (int) ((double) super.x + ((double) effector.directionX - falloff) * (double) elapsedTime);
                                super.y = (int) ((double) super.y + ((double) effectorType.dirY - falloff) * (double) elapsedTime);
                                super.z = (int) ((double) super.z + ((double) effector.directionZ - falloff) * (double) elapsedTime);
                            }
                            effector = (ParticleEffector) ParticleManager.effectorsCache.nextWithSameKey();
                        }
                    }
                }
            }
        }
        if (type.globalEffectors != null) {
            if (type.globalEffectorIndices == null) {
                type.globalEffectorIndices = new int[type.globalEffectors.length];
                for (local317 = 0; local317 < type.globalEffectors.length; local317++) {
                    ParticleEffectorTypeList.get(type.globalEffectors[local317]);
                    type.globalEffectorIndices[local317] = ((IntNode) ParticleEffectorTypeList.table.get(type.globalEffectors[local317])).value;
                }
            }
            for (local317 = 0; local317 < type.globalEffectorIndices.length; local317++) {
                @Pc(1137) ParticleEffectorType effectorType = ParticleEffectorTypeList.types[type.globalEffectorIndices[local317]];
                if (effectorType.constantSpeed == 0) {
                    velocityX += effectorType.dirX * elapsedTime;
                    velocityY += effectorType.dirY * elapsedTime;
                    velocityZ += effectorType.dirZ * elapsedTime;
                    velocityChanged = true;
                } else {
                    super.x += effectorType.dirX * elapsedTime;
                    super.y += effectorType.dirY * elapsedTime;
                    super.z += effectorType.dirZ * elapsedTime;
                }
            }
        }
        if (velocityChanged) {
            while (true) {
                if (!(velocityX > 32767.0D) && !(velocityY > 32767.0D) && !(velocityZ > 32767.0D) && !(velocityX < -32767.0D) && !(velocityY < -32767.0D) && !(velocityZ < -32767.0D)) {
                    this.directionX = (short) (int) velocityX;
                    this.directionY = (short) (int) velocityY;
                    this.directionZ = (short) (int) velocityZ;
                    break;
                }
                velocityX /= 2.0D;
                velocityY /= 2.0D;
                velocityZ /= 2.0D;
                this.speed <<= 0x1;
            }
        }
        super.x = (int) ((long) super.x + ((long) this.directionX * (long) (this.speed << 2) >> 23) * (long) elapsedTime);
        super.y = (int) ((long) super.y + ((long) this.directionY * (long) (this.speed << 2) >> 23) * (long) elapsedTime);
        super.z = (int) ((long) super.z + ((long) this.directionZ * (long) (this.speed << 2) >> 23) * (long) elapsedTime);
    }

    @OriginalMember(owner = "client!pp", name = "a", descriptor = "(Lclient!ha;J)V")
    public void collide(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) long time) {
        @Pc(6) int tileX = super.x >> EnvironmentLight.anInt1066 + 12;
        @Pc(13) int tileZ = super.z >> EnvironmentLight.anInt1066 + 12;
        @Pc(18) int worldY = super.y >> 12;
        if (worldY > 0 || worldY < -262144 || tileX < 0 || tileX >= Static619.tileMaxX || tileZ < 0 || tileZ >= Static662.tileMaxZ) {
            this.remove();
            return;
        }
        @Pc(40) ParticleSystem system = this.emitter.system;
        @Pc(44) ParticleEmitterType type = this.emitter.type;
        @Pc(46) Ground[] grounds = Static246.ground;
        @Pc(49) int level = system.level;
        @Pc(58) Tile tile = Static334.activeTiles[system.level][tileX][tileZ];
        if (tile != null) {
            level = tile.level;
        }
        @Pc(71) int height = grounds[level].getHeight(tileX, tileZ);
        @Pc(86) int heightAbove;
        if (level < Static299.tileMaxLevel - 1) {
            heightAbove = grounds[level + 1].getHeight(tileX, tileZ);
        } else {
            heightAbove = height - (0x8 << EnvironmentLight.anInt1066);
        }
        if (type.hasHeightLevelBounds) {
            if (type.minHeightLevel == -1 && worldY > height) {
                this.remove();
                return;
            }
            if (type.minHeightLevel >= 0 && worldY > grounds[type.minHeightLevel].getHeight(tileX, tileZ)) {
                this.remove();
                return;
            }
            if (type.maxHeightLevel == -1 && worldY < heightAbove) {
                this.remove();
                return;
            }
            if (type.maxHeightLevel >= 0 && worldY < grounds[type.maxHeightLevel + 1].getHeight(tileX, tileZ)) {
                this.remove();
                return;
            }
        }
        @Pc(154) int particleLevel;
        for (particleLevel = Static299.tileMaxLevel - 1; particleLevel > 0 && worldY > grounds[particleLevel].getHeight(tileX, tileZ); particleLevel--) {
        }
        if (type.collidesWithGround && particleLevel == 0 && worldY > grounds[0].getHeight(tileX, tileZ)) {
            this.remove();
        } else if (particleLevel == Static299.tileMaxLevel - 1 && grounds[particleLevel].getHeight(tileX, tileZ) - worldY > 0x8 << EnvironmentLight.anInt1066) {
            this.remove();
        } else {
            tile = Static334.activeTiles[particleLevel][tileX][tileZ];
            @Pc(261) int local261;
            if (tile == null) {
                if (particleLevel == 0 || Static334.activeTiles[0][tileX][tileZ] == null) {
                    tile = Static334.activeTiles[0][tileX][tileZ] = new Tile(0);
                }
                @Pc(251) boolean bridge = Static334.activeTiles[0][tileX][tileZ].tile != null;
                if (particleLevel == 3 && bridge) {
                    this.remove();
                    return;
                }
                for (local261 = 1; local261 <= particleLevel; local261++) {
                    if (Static334.activeTiles[local261][tileX][tileZ] == null) {
                        tile = Static334.activeTiles[local261][tileX][tileZ] = new Tile(local261);
                        if (bridge) {
                            tile.level++;
                        }
                    }
                }
            }
            if (type.collidesWithLocations) {
                @Pc(304) int worldX = super.x >> 12;
                local261 = super.z >> 12;
                @Pc(318) BoundingCylinder cylinder;
                if (tile.wall != null) {
                    cylinder = tile.wall.getCylinder(toolkit, -105);
                    if (cylinder != null && cylinder.contains(worldY, local261, worldX)) {
                        this.remove();
                        return;
                    }
                }
                if (tile.adjacentWall != null) {
                    cylinder = tile.adjacentWall.getCylinder(toolkit, -120);
                    if (cylinder != null && cylinder.contains(worldY, local261, worldX)) {
                        this.remove();
                        return;
                    }
                }
                if (tile.groundDecor != null) {
                    cylinder = tile.groundDecor.getCylinder(toolkit, -109);
                    if (cylinder != null && cylinder.contains(worldY, local261, worldX)) {
                        this.remove();
                        return;
                    }
                }
                for (@Pc(375) PositionEntityNode node = tile.head; node != null; node = node.node) {
                    @Pc(382) BoundingCylinder entityCylinder = node.entity.getCylinder(toolkit, -117);
                    if (entityCylinder != null && entityCylinder.contains(worldY, local261, worldX)) {
                        this.remove();
                        return;
                    }
                }
            }
            system.list.particles.add(this);
        }
    }

    @OriginalMember(owner = "client!pp", name = "a", descriptor = "(Lclient!rf;IIIIIIIIIIIZZ)V")
    public void init(@OriginalArg(0) ParticleEmitter emitter, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int z, @OriginalArg(4) int directionX, @OriginalArg(5) int directionY, @OriginalArg(6) int directionZ, @OriginalArg(7) int speed, @OriginalArg(8) int lifetime, @OriginalArg(9) int colour, @OriginalArg(10) int size, @OriginalArg(11) int texture, @OriginalArg(12) boolean disableHdLighting, @OriginalArg(13) boolean preserveAmbient) {
        this.emitter = emitter;
        super.x = x << 12;
        super.y = y << 12;
        super.z = z << 12;
        super.colour = colour;
        this.lifetime = this.remainingLifetime = (short) lifetime;
        super.size = size;
        super.texture = texture;
        super.preserveAmbient = preserveAmbient;
        this.directionX = (short) directionX;
        this.directionY = (short) directionY;
        this.directionZ = (short) directionZ;
        this.speed = speed;
        super.aByte122 = this.emitter.model.aByte130;
        this.register();
    }

    @OriginalMember(owner = "client!pp", name = "b", descriptor = "()V")
    public void remove() {
        this.emitter.system.movingParticles[this.slot] = null;
        ParticleManager.particles[ParticleManager.particleFreePtr] = this;
        ParticleManager.particleFreePtr = ParticleManager.particleFreePtr + 1 & 0x3FF;
        this.unlink();
        this.unlink2();
    }
}

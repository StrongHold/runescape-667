import com.jagex.core.datastruct.LinkedList;
import com.jagex.core.datastruct.Node;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterType;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterTypeList;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.particles.ParticleLimits;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!rf")
public final class ParticleEmitter extends Node {

    @OriginalMember(owner = "client!rf", name = "u", descriptor = "I")
    public int particleCount;

    @OriginalMember(owner = "client!rf", name = "x", descriptor = "I")
    public int baseAngleH;

    @OriginalMember(owner = "client!rf", name = "i", descriptor = "I")
    public int angleHRange;

    @OriginalMember(owner = "client!rf", name = "m", descriptor = "I")
    public int angleVRange;

    @OriginalMember(owner = "client!rf", name = "y", descriptor = "I")
    public int normalX;

    @OriginalMember(owner = "client!rf", name = "l", descriptor = "I")
    public int baseAngleV;

    @OriginalMember(owner = "client!rf", name = "A", descriptor = "I")
    public int normalY;

    @OriginalMember(owner = "client!rf", name = "B", descriptor = "I")
    public int normalZ;

    @OriginalMember(owner = "client!rf", name = "C", descriptor = "I")
    public int spawnAccumulator = 0;

    @OriginalMember(owner = "client!rf", name = "o", descriptor = "Z")
    public boolean inactive = false;

    @OriginalMember(owner = "client!rf", name = "s", descriptor = "Lclient!iea;")
    public ParticleEmitterRelated triangle = new ParticleEmitterRelated();

    @OriginalMember(owner = "client!rf", name = "h", descriptor = "Lclient!iea;")
    public ParticleEmitterRelated previousTriangle = new ParticleEmitterRelated();

    @OriginalMember(owner = "client!rf", name = "E", descriptor = "Z")
    public boolean finished = false;

    @OriginalMember(owner = "client!rf", name = "r", descriptor = "Lclient!rv;")
    public final ModelParticleEmitter model;

    @OriginalMember(owner = "client!rf", name = "g", descriptor = "J")
    public final long startTime;

    @OriginalMember(owner = "client!rf", name = "k", descriptor = "Lclient!hv;")
    public final ParticleSystem system;

    @OriginalMember(owner = "client!rf", name = "q", descriptor = "Lclient!vaa;")
    public ParticleEmitterType type;

    @OriginalMember(owner = "client!rf", name = "j", descriptor = "Lclient!fla;")
    public final LinkedList movingParticles;

    @OriginalMember(owner = "client!rf", name = "<init>", descriptor = "(Lclient!ha;Lclient!rv;Lclient!hv;J)V")
    public ParticleEmitter(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) ModelParticleEmitter model, @OriginalArg(2) ParticleSystem system, @OriginalArg(3) long startTime) {
        this.model = model;
        this.startTime = startTime;
        this.system = system;
        this.type = this.model.getType();
        if (!toolkit.method7937() && this.type.untextured != -1) {
            this.type = ParticleEmitterTypeList.get(this.type.untextured);
        }
        this.movingParticles = new LinkedList();
        this.spawnAccumulator = (int) ((double) this.spawnAccumulator + Math.random() * 64.0D);
        this.updateTriangle();
        this.previousTriangle.anInt4281 = this.triangle.anInt4281;
        this.previousTriangle.anInt4280 = this.triangle.anInt4280;
        this.previousTriangle.anInt4277 = this.triangle.anInt4277;
        this.previousTriangle.anInt4269 = this.triangle.anInt4269;
        this.previousTriangle.anInt4283 = this.triangle.anInt4283;
        this.previousTriangle.anInt4275 = this.triangle.anInt4275;
        this.previousTriangle.anInt4270 = this.triangle.anInt4270;
        this.previousTriangle.anInt4276 = this.triangle.anInt4276;
        this.previousTriangle.anInt4279 = this.triangle.anInt4279;
    }

    @OriginalMember(owner = "client!rf", name = "a", descriptor = "(IBZJLclient!ha;)V")
    public void tick(@OriginalArg(0) int elapsedTime, @OriginalArg(2) boolean running, @OriginalArg(3) long time, @OriginalArg(4) Toolkit toolkit) {
        if (this.inactive) {
            running = false;
        } else if (ParticleManager.option < this.type.minSetting) {
            running = false;
        } else if (ParticleManager.previousParticleCount > ParticleLimits.PARTICLES[ParticleManager.option]) {
            running = false;
        } else if (this.finished) {
            running = false;
        } else if (this.type.lifetime != -1) {
            @Pc(46) int timeSinceStart = (int) (time - this.startTime);
            if (this.type.periodic || this.type.lifetime >= timeSinceStart) {
                timeSinceStart %= this.type.lifetime;
            } else {
                running = false;
            }
            if (!this.type.activeFirst && timeSinceStart < this.type.activationAge) {
                running = false;
            }
            if (this.type.activeFirst && this.type.activationAge <= timeSinceStart) {
                running = false;
            }
        }

        if (running) {
            ParticleManager.emitterCount++;
            @Pc(46) int centroidX = (this.triangle.anInt4276 + this.triangle.anInt4279 + this.triangle.anInt4281) / 3;
            @Pc(147) int centroidY = (this.triangle.anInt4269 + this.triangle.anInt4280 + this.triangle.anInt4283) / 3;
            @Pc(162) int centroidZ = (this.triangle.anInt4277 + this.triangle.anInt4270 + this.triangle.anInt4275) / 3;
            @Pc(210) int local210;
            @Pc(218) int local218;
            @Pc(226) int local226;
            @Pc(235) int local235;
            @Pc(244) int local244;
            @Pc(252) int local252;
            @Pc(362) int local362;
            @Pc(414) int local414;
            @Pc(435) int local435;
            if (centroidX != this.triangle.anInt4271 || centroidY != this.triangle.anInt4278 || this.triangle.anInt4273 != centroidZ) {
                this.triangle.anInt4278 = centroidY;
                this.triangle.anInt4273 = centroidZ;
                this.triangle.anInt4271 = centroidX;
                local210 = this.triangle.anInt4281 - this.triangle.anInt4279;
                local218 = this.triangle.anInt4283 - this.triangle.anInt4280;
                local226 = this.triangle.anInt4277 - this.triangle.anInt4270;
                local235 = this.triangle.anInt4276 - this.triangle.anInt4279;
                local244 = this.triangle.anInt4269 - this.triangle.anInt4280;
                local252 = this.triangle.anInt4275 - this.triangle.anInt4270;
                this.normalZ = local210 * local244 - local218 * local235;
                this.normalX = local218 * local252 - local226 * local244;
                this.normalY = local235 * local226 - local252 * local210;
                while (true) {
                    if (this.normalX <= 32767 && this.normalY <= 32767 && this.normalZ <= 32767 && this.normalX >= -32767 && this.normalY >= -32767 && this.normalZ >= -32767) {
                        local362 = (int) Math.sqrt(this.normalX * this.normalX + this.normalY * this.normalY + this.normalZ * this.normalZ);
                        if (local362 <= 0) {
                            local362 = 1;
                        }
                        this.normalY = this.normalY * 32767 / local362;
                        this.normalX = this.normalX * 32767 / local362;
                        this.normalZ = this.normalZ * 32767 / local362;
                        if (this.type.maxAngleH > 0 || this.type.maxAngleV > 0) {
                            local414 = (int) (Math.atan2(this.normalZ, this.normalX) * 2607.5945876176133D);
                            local435 = (int) (Math.atan2(this.normalY, Math.sqrt(this.normalX * this.normalX + this.normalZ * this.normalZ)) * 2607.5945876176133D);
                            this.angleHRange = this.type.maxAngleH - this.type.minAngleH;
                            this.baseAngleH = this.type.minAngleH + local414 - (this.angleHRange >> 1);
                            this.angleVRange = this.type.maxAngleV - this.type.minAngleV;
                            this.baseAngleV = this.type.minAngleV + local435 - (this.angleVRange >> 1);
                        }
                        break;
                    }
                    this.normalZ >>= 0x1;
                    this.normalX >>= 0x1;
                    this.normalY >>= 0x1;
                }
            }
            this.spawnAccumulator += (int) (((double) this.type.minParticleRate + Math.random() * (double) (this.type.maxParticleRate - this.type.minParticleRate)) * (double) elapsedTime);
            if (this.spawnAccumulator > 63) {
                local210 = this.spawnAccumulator >> 6;
                this.spawnAccumulator &= 0x3F;
                for (local244 = 0; local244 < local210; local244++) {
                    @Pc(577) int local577;
                    @Pc(581) int local581;
                    if (this.type.maxAngleH <= 0 && this.type.maxAngleV <= 0) {
                        local218 = this.normalX;
                        local235 = this.normalZ;
                        local226 = this.normalY;
                    } else {
                        local252 = (int) ((double) this.angleHRange * Math.random()) + this.baseAngleH;
                        local252 &= 0x3FFF;
                        local362 = Trig1.SIN[local252];
                        local414 = Trig1.COS[local252];
                        local435 = this.baseAngleV + (int) ((double) this.angleVRange * Math.random());
                        local435 &= 0x1FFF;
                        local577 = Trig1.SIN[local435];
                        local581 = Trig1.COS[local435];
                        local218 = local414 * local577 >> 13;
                        local226 = (local581 << 1) * -1;
                        local235 = local577 * local362 >> 13;
                    }
                    @Pc(615) float weightU = (float) Math.random();
                    @Pc(618) float weightV = (float) Math.random();
                    if (weightV + weightU > 1.0F) {
                        weightU = 1.0F - weightU;
                        weightV = 1.0F - weightV;
                    }
                    @Pc(639) float weightW = 1.0F - (weightV + weightU);
                    local435 = (int) ((float) this.triangle.anInt4276 * weightW + ((float) this.triangle.anInt4281 * weightV + weightU * (float) this.triangle.anInt4279));
                    local577 = (int) ((float) this.triangle.anInt4283 * weightV + weightU * (float) this.triangle.anInt4280 + weightW * (float) this.triangle.anInt4269);
                    local581 = (int) ((float) this.triangle.anInt4270 * weightU + (float) this.triangle.anInt4277 * weightV + weightW * (float) this.triangle.anInt4275);
                    @Pc(727) int previousX = (int) (weightW * (float) this.previousTriangle.anInt4276 + ((float) this.previousTriangle.anInt4281 * weightV + (float) this.previousTriangle.anInt4279 * weightU));
                    @Pc(749) int previousY = (int) (weightW * (float) this.previousTriangle.anInt4269 + (weightV * (float) this.previousTriangle.anInt4283 + weightU * (float) this.previousTriangle.anInt4280));
                    @Pc(771) int previousZ = (int) (weightV * (float) this.previousTriangle.anInt4277 + (float) this.previousTriangle.anInt4270 * weightU + (float) this.previousTriangle.anInt4275 * weightW);
                    @Pc(776) int deltaX = local435 - previousX;
                    @Pc(780) int deltaY = local577 - previousY;
                    @Pc(785) int deltaZ = local581 - previousZ;
                    @Pc(794) int spawnX = (int) ((double) deltaX * Math.random() + (double) previousX);
                    @Pc(803) int spawnY = (int) ((double) previousY + (double) deltaY * Math.random());
                    @Pc(812) int spawnZ = (int) ((double) previousZ + Math.random() * (double) deltaZ);
                    @Pc(828) int speed = (int) (Math.random() * (double) (this.type.maxSpeed - this.type.minSpeed)) + this.type.minSpeed;
                    @Pc(845) int lifetime = (int) (Math.random() * (double) (this.type.maxLifetime - this.type.minLifetime)) + this.type.minLifetime;
                    @Pc(862) int size = (int) (Math.random() * (double) (this.type.maxSize - this.type.minSize)) + this.type.minSize;
                    @Pc(926) int colour;
                    if (this.type.uniformColourVariance) {
                        @Pc(868) double variance = Math.random();
                        colour = (int) ((double) this.type.minStartBlue + (double) this.type.startBlueRange * variance) | (int) ((double) this.type.minStartGreen + variance * (double) this.type.startGreenRange) << 8 | (int) ((double) this.type.minStartRed + (double) this.type.startRedRange * variance) << 16 | (int) (Math.random() * (double) this.type.startAlphaRange + (double) this.type.minStartAlpha) << 24;
                    } else {
                        colour = (int) ((double) this.type.minStartBlue + Math.random() * (double) this.type.startBlueRange) | (int) ((double) this.type.minStartGreen + Math.random() * (double) this.type.startGreenRange) << 8 | (int) (Math.random() * (double) this.type.startRedRange + (double) this.type.minStartRed) << 16 | (int) ((double) this.type.minStartAlpha + (double) this.type.startAlphaRange * Math.random()) << 24;
                    }
                    @Pc(990) int texture = this.type.texture;
                    if (!toolkit.method7937() && !this.type.softwareTextured) {
                        texture = -1;
                    }
                    if (ParticleManager.particleNextPtr == ParticleManager.particleFreePtr) {
                        new MovingParticle(this, spawnX, spawnY, spawnZ, local218, local226, local235, speed, lifetime, colour, size, texture, this.type.disableHdLighting, this.type.preserveAmbient);
                    } else {
                        @Pc(1032) MovingParticle particle = ParticleManager.particles[ParticleManager.particleNextPtr];
                        ParticleManager.particleNextPtr = ParticleManager.particleNextPtr + 1 & 0x3FF;
                        particle.init(this, spawnX, spawnY, spawnZ, local218, local226, local235, speed, lifetime, colour, size, texture, this.type.disableHdLighting, this.type.preserveAmbient);
                    }
                }
            }
        }
        if (!this.triangle.method3860(this.previousTriangle)) {
            @Pc(1078) ParticleEmitterRelated swap = this.previousTriangle;
            this.previousTriangle = this.triangle;
            this.triangle = swap;
            this.triangle.anInt4275 = this.model.anInt8520;
            this.triangle.anInt4276 = this.model.anInt8512;
            this.triangle.anInt4283 = this.model.anInt8507;
            this.triangle.anInt4281 = this.model.anInt8516;
            this.triangle.anInt4270 = this.model.anInt8504;
            this.triangle.anInt4280 = this.model.anInt8502;
            this.triangle.anInt4277 = this.model.anInt8509;
            this.triangle.anInt4278 = this.previousTriangle.anInt4278;
            this.triangle.anInt4273 = this.previousTriangle.anInt4273;
            this.triangle.anInt4279 = this.model.anInt8518;
            this.triangle.anInt4271 = this.previousTriangle.anInt4271;
            this.triangle.anInt4269 = this.model.anInt8503;
        }
        this.particleCount = 0;
        for (@Pc(1171) MovingParticle particle = (MovingParticle) this.movingParticles.first(); particle != null; particle = (MovingParticle) this.movingParticles.next()) {
            particle.tick(time, elapsedTime);
            this.particleCount++;
        }
        ParticleManager.particleCount += this.particleCount;
    }

    @OriginalMember(owner = "client!rf", name = "a", descriptor = "(JLclient!ha;I)V")
    public void collideParticles(@OriginalArg(0) long time, @OriginalArg(1) Toolkit toolkit) {
        for (@Pc(11) MovingParticle particle = (MovingParticle) this.movingParticles.first(); particle != null; particle = (MovingParticle) this.movingParticles.next()) {
            particle.collide(toolkit, time);
        }
    }

    @OriginalMember(owner = "client!rf", name = "a", descriptor = "(B)V")
    public void updateTriangle() {
        this.triangle.anInt4283 = this.model.anInt8507;
        this.triangle.anInt4275 = this.model.anInt8520;
        this.triangle.anInt4276 = this.model.anInt8512;
        this.triangle.anInt4280 = this.model.anInt8502;
        this.triangle.anInt4281 = this.model.anInt8516;
        this.triangle.anInt4270 = this.model.anInt8504;
        this.triangle.anInt4277 = this.model.anInt8509;
        this.triangle.anInt4279 = this.model.anInt8518;
        this.triangle.anInt4269 = this.model.anInt8503;

        if (this.triangle.anInt4281 == this.triangle.anInt4279 && this.triangle.anInt4276 == this.triangle.anInt4281 && this.triangle.anInt4280 == this.triangle.anInt4283 && this.triangle.anInt4283 == this.triangle.anInt4269 && this.triangle.anInt4270 == this.triangle.anInt4277 && this.triangle.anInt4275 == this.triangle.anInt4277) {
            this.finished = true;
        } else if (this.finished) {
            this.previousTriangle.anInt4280 = this.triangle.anInt4280;
            this.previousTriangle.anInt4270 = this.triangle.anInt4270;
            this.previousTriangle.anInt4279 = this.triangle.anInt4279;
            this.previousTriangle.anInt4283 = this.triangle.anInt4283;
            this.previousTriangle.anInt4277 = this.triangle.anInt4277;
            this.previousTriangle.anInt4269 = this.triangle.anInt4269;
            this.previousTriangle.anInt4275 = this.triangle.anInt4275;
            this.finished = false;
            this.previousTriangle.anInt4276 = this.triangle.anInt4276;
            this.previousTriangle.anInt4281 = this.triangle.anInt4281;
        }
    }
}

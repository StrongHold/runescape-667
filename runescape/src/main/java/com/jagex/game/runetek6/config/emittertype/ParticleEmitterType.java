package com.jagex.game.runetek6.config.emittertype;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!vaa")
public final class ParticleEmitterType {

    @OriginalMember(owner = "client!vaa", name = "w", descriptor = "I")
    public int startAlphaRange;

    @OriginalMember(owner = "client!vaa", name = "ub", descriptor = "I")
    public int maxStartBlue;

    @OriginalMember(owner = "client!vaa", name = "F", descriptor = "[I")
    public int[] globalEffectorIndices;

    @OriginalMember(owner = "client!vaa", name = "G", descriptor = "I")
    public int maxStartColour;

    @OriginalMember(owner = "client!vaa", name = "X", descriptor = "S")
    public short minAngleH;

    @OriginalMember(owner = "client!vaa", name = "B", descriptor = "I")
    public int minStartColour;

    @OriginalMember(owner = "client!vaa", name = "Q", descriptor = "I")
    public int minSize;

    @OriginalMember(owner = "client!vaa", name = "ib", descriptor = "[I")
    public int[] localEffectors;

    @OriginalMember(owner = "client!vaa", name = "Z", descriptor = "I")
    public int alphaFadeStep;

    @OriginalMember(owner = "client!vaa", name = "x", descriptor = "I")
    public int maxSpeed;

    @OriginalMember(owner = "client!vaa", name = "s", descriptor = "I")
    public int speedChangeStep;

    @OriginalMember(owner = "client!vaa", name = "f", descriptor = "S")
    public short maxAngleH;

    @OriginalMember(owner = "client!vaa", name = "S", descriptor = "I")
    public int maxStartAlpha;

    @OriginalMember(owner = "client!vaa", name = "mb", descriptor = "[I")
    public int[] generalEffectors;

    @OriginalMember(owner = "client!vaa", name = "D", descriptor = "I")
    public int minSpeed;

    @OriginalMember(owner = "client!vaa", name = "hb", descriptor = "I")
    public int maxParticleRate;

    @OriginalMember(owner = "client!vaa", name = "eb", descriptor = "S")
    public short maxAngleV;

    @OriginalMember(owner = "client!vaa", name = "cb", descriptor = "I")
    public int minStartRed;

    @OriginalMember(owner = "client!vaa", name = "lb", descriptor = "I")
    public int speedChangeDuration;

    @OriginalMember(owner = "client!vaa", name = "Y", descriptor = "S")
    public short minAngleV;

    @OriginalMember(owner = "client!vaa", name = "m", descriptor = "I")
    public int colourFadeDuration;

    @OriginalMember(owner = "client!vaa", name = "a", descriptor = "I")
    public int maxLifetime;

    @OriginalMember(owner = "client!vaa", name = "J", descriptor = "I")
    public int minStartBlue;

    @OriginalMember(owner = "client!vaa", name = "C", descriptor = "I")
    public int minStartAlpha;

    @OriginalMember(owner = "client!vaa", name = "db", descriptor = "I")
    public int sizeChangeDuration;

    @OriginalMember(owner = "client!vaa", name = "xb", descriptor = "I")
    public int sizeChangeStep;

    @OriginalMember(owner = "client!vaa", name = "tb", descriptor = "I")
    public int startGreenRange;

    @OriginalMember(owner = "client!vaa", name = "ob", descriptor = "I")
    public int minLifetime;

    @OriginalMember(owner = "client!vaa", name = "d", descriptor = "I")
    public int startBlueRange;

    @OriginalMember(owner = "client!vaa", name = "h", descriptor = "I")
    public int minStartGreen;

    @OriginalMember(owner = "client!vaa", name = "P", descriptor = "I")
    public int decelerationRate;

    @OriginalMember(owner = "client!vaa", name = "H", descriptor = "I")
    public int blueFadeStep;

    @OriginalMember(owner = "client!vaa", name = "rb", descriptor = "I")
    public int maxSize;

    @OriginalMember(owner = "client!vaa", name = "K", descriptor = "I")
    public int fadeColour;

    @OriginalMember(owner = "client!vaa", name = "R", descriptor = "I")
    public int greenFadeStep;

    @OriginalMember(owner = "client!vaa", name = "o", descriptor = "[I")
    public int[] globalEffectors;

    @OriginalMember(owner = "client!vaa", name = "O", descriptor = "I")
    public int maxStartRed;

    @OriginalMember(owner = "client!vaa", name = "bb", descriptor = "I")
    public int alphaFadeDuration;

    @OriginalMember(owner = "client!vaa", name = "j", descriptor = "I")
    public int maxStartGreen;

    @OriginalMember(owner = "client!vaa", name = "ab", descriptor = "I")
    public int startRedRange;

    @OriginalMember(owner = "client!vaa", name = "fb", descriptor = "I")
    public int redFadeStep;

    @OriginalMember(owner = "client!vaa", name = "k", descriptor = "I")
    public int minParticleRate;

    @OriginalMember(owner = "client!vaa", name = "i", descriptor = "I")
    public int activationAge = -1;

    @OriginalMember(owner = "client!vaa", name = "pb", descriptor = "I")
    public int untextured = -1;

    @OriginalMember(owner = "client!vaa", name = "T", descriptor = "I")
    public int startupTicks = 0;

    @OriginalMember(owner = "client!vaa", name = "p", descriptor = "I")
    public int alphaFadePercentage = 100;

    @OriginalMember(owner = "client!vaa", name = "r", descriptor = "I")
    public int minSetting = 0;

    @OriginalMember(owner = "client!vaa", name = "vb", descriptor = "Z")
    public boolean collidesWithGround = true;

    @OriginalMember(owner = "client!vaa", name = "I", descriptor = "Z")
    public boolean preserveAmbient = true;

    @OriginalMember(owner = "client!vaa", name = "q", descriptor = "I")
    public int texture = -1;

    @OriginalMember(owner = "client!vaa", name = "wb", descriptor = "I")
    public int decelerationType = 0;

    @OriginalMember(owner = "client!vaa", name = "L", descriptor = "Z")
    public boolean periodic = true;

    @OriginalMember(owner = "client!vaa", name = "c", descriptor = "Z")
    public boolean collidesWithLocations = false;

    @OriginalMember(owner = "client!vaa", name = "nb", descriptor = "I")
    public int maxHeightLevel = -2;

    @OriginalMember(owner = "client!vaa", name = "A", descriptor = "Z")
    public boolean uniformColourVariance = true;

    @OriginalMember(owner = "client!vaa", name = "u", descriptor = "Z")
    public boolean hasHeightLevelBounds = false;

    @OriginalMember(owner = "client!vaa", name = "kb", descriptor = "I")
    public int sizeChangePercentage = 100;

    @OriginalMember(owner = "client!vaa", name = "qb", descriptor = "I")
    public int minHeightLevel = -2;

    @OriginalMember(owner = "client!vaa", name = "g", descriptor = "I")
    public int speedChangePercentage = 100;

    @OriginalMember(owner = "client!vaa", name = "jb", descriptor = "Z")
    public boolean activeFirst = true;

    @OriginalMember(owner = "client!vaa", name = "y", descriptor = "I")
    public int endSpeed = -1;

    @OriginalMember(owner = "client!vaa", name = "V", descriptor = "Z")
    public boolean softwareTextured = false;

    @OriginalMember(owner = "client!vaa", name = "e", descriptor = "I")
    public int lifetime = -1;

    @OriginalMember(owner = "client!vaa", name = "l", descriptor = "I")
    public int endSize = -1;

    @OriginalMember(owner = "client!vaa", name = "W", descriptor = "Z")
    public boolean disableHdLighting = true;

    @OriginalMember(owner = "client!vaa", name = "E", descriptor = "I")
    public int colourFadePercentage = 100;

    @OriginalMember(owner = "client!vaa", name = "a", descriptor = "(BLclient!ge;I)V")
    public void decode(@OriginalArg(1) Packet packet, @OriginalArg(2) int code) {
        if (code == 1) {
            this.minAngleH = (short) packet.g2();
            this.maxAngleH = (short) packet.g2();
            this.minAngleV = (short) packet.g2();
            this.maxAngleV = (short) packet.g2();
            this.maxAngleV = (short) (this.maxAngleV << 3);
            this.minAngleV = (short) (this.minAngleV << 3);
            this.maxAngleH = (short) (this.maxAngleH << 3);
            this.minAngleH = (short) (this.minAngleH << 3);
        } else if (code == 2) {
            packet.g1();
        } else if (code == 3) {
            this.minSpeed = packet.g4();
            this.maxSpeed = packet.g4();
        } else if (code == 4) {
            this.decelerationType = packet.g1();
            this.decelerationRate = packet.g1b();
        } else if (code == 5) {
            this.minSize = this.maxSize = packet.g2() << 12 << 2;
        } else if (code == 6) {
            this.minStartColour = packet.g4();
            this.maxStartColour = packet.g4();
        } else if (code == 7) {
            this.minLifetime = packet.g2();
            this.maxLifetime = packet.g2();
        } else if (code == 8) {
            this.minParticleRate = packet.g2();
            this.maxParticleRate = packet.g2();
        } else if (code == 9) {
            @Pc(159) int count = packet.g1();
            this.localEffectors = new int[count];
            for (@Pc(165) int i = 0; i < count; i++) {
                this.localEffectors[i] = packet.g2();
            }
        } else if (code == 10) {
            @Pc(159) int count = packet.g1();
            this.globalEffectors = new int[count];
            for (@Pc(165) int i = 0; i < count; i++) {
                this.globalEffectors[i] = packet.g2();
            }
        } else if (code == 12) {
            this.minHeightLevel = packet.g1b();
        } else if (code == 13) {
            this.maxHeightLevel = packet.g1b();
        } else if (code == 14) {
            this.startupTicks = packet.g2();
        } else if (code == 15) {
            this.texture = packet.g2();
        } else if (code == 16) {
            this.activeFirst = packet.g1() == 1;
            this.activationAge = packet.g2();
            this.lifetime = packet.g2();
            this.periodic = packet.g1() == 1;
        } else if (code == 17) {
            this.untextured = packet.g2();
        } else if (code == 18) {
            this.fadeColour = packet.g4();
        } else if (code == 19) {
            this.minSetting = packet.g1();
        } else if (code == 20) {
            this.colourFadePercentage = packet.g1();
        } else if (code == 21) {
            this.alphaFadePercentage = packet.g1();
        } else if (code == 22) {
            this.endSpeed = packet.g4();
        } else if (code == 23) {
            this.speedChangePercentage = packet.g1();
        } else if (code == 24) {
            this.uniformColourVariance = false;
        } else if (code == 25) {
            @Pc(159) int count = packet.g1();
            this.generalEffectors = new int[count];
            for (@Pc(165) int i = 0; i < count; i++) {
                this.generalEffectors[i] = packet.g2();
            }
        } else if (code == 26) {
            this.disableHdLighting = false;
        } else if (code == 27) {
            this.endSize = packet.g2() << 12 << 2;
        } else if (code == 28) {
            this.sizeChangePercentage = packet.g1();
        } else if (code == 29) {
            packet.g2s();
        } else if (code == 30) {
            this.softwareTextured = true;
        } else if (code == 31) {
            this.minSize = packet.g2() << 12 << 2;
            this.maxSize = packet.g2() << 12 << 2;
        } else if (code == 32) {
            this.preserveAmbient = false;
        } else if (code == 33) {
            this.collidesWithLocations = true;
        } else if (code == 34) {
            this.collidesWithGround = false;
        }
    }

    @OriginalMember(owner = "client!vaa", name = "b", descriptor = "(I)V")
    public void postDecode() {
        this.minStartRed = this.minStartColour >> 16 & 0xFF;

        if (this.minHeightLevel > -2 || this.maxHeightLevel > -2) {
            this.hasHeightLevelBounds = true;
        }

        this.maxStartRed = this.maxStartColour >> 16 & 0xFF;
        this.startRedRange = this.maxStartRed - this.minStartRed;
        this.minStartGreen = this.minStartColour >> 8 & 0xFF;
        this.maxStartGreen = this.maxStartColour >> 8 & 0xFF;
        this.maxStartBlue = this.maxStartColour & 0xFF;
        this.startGreenRange = this.maxStartGreen - this.minStartGreen;
        this.minStartBlue = this.minStartColour & 0xFF;
        this.maxStartAlpha = this.maxStartColour >> 24 & 0xFF;
        this.minStartAlpha = this.minStartColour >> 24 & 0xFF;
        this.startBlueRange = this.maxStartBlue - this.minStartBlue;
        this.startAlphaRange = this.maxStartAlpha - this.minStartAlpha;

        if (this.endSize != -1) {
            this.sizeChangeDuration = this.sizeChangePercentage * this.maxLifetime / 100;
            if (this.sizeChangeDuration == 0) {
                this.sizeChangeDuration = 1;
            }
            this.sizeChangeStep = (this.endSize - (this.maxSize - this.minSize) / 2 - this.minSize) / this.sizeChangeDuration;
        }

        if (this.endSpeed != -1) {
            this.speedChangeDuration = this.maxLifetime * this.speedChangePercentage / 100;
            if (this.speedChangeDuration == 0) {
                this.speedChangeDuration = 1;
            }
            this.speedChangeStep = (this.endSpeed - (this.maxSpeed - this.minSpeed) / 2 - this.minSpeed) / this.speedChangeDuration;
        }

        if (this.fadeColour != 0) {
            this.alphaFadeDuration = this.alphaFadePercentage * this.maxLifetime / 100;
            this.colourFadeDuration = this.maxLifetime * this.colourFadePercentage / 100;

            if (this.colourFadeDuration == 0) {
                this.colourFadeDuration = 1;
            }

            if (this.alphaFadeDuration == 0) {
                this.alphaFadeDuration = 1;
            }

            this.redFadeStep = ((((this.fadeColour >> 16) & 0xFF) - (this.startRedRange / 2) - this.minStartRed) << 8) / this.colourFadeDuration;
            this.greenFadeStep = ((((this.fadeColour >> 8) & 0xFF) - this.minStartGreen - (this.startGreenRange / 2)) << 8) / this.colourFadeDuration;
            this.blueFadeStep = (((this.fadeColour & 0xFF) - (this.startBlueRange / 2) - this.minStartBlue) << 8) / this.colourFadeDuration;
            this.blueFadeStep += (this.blueFadeStep <= 0) ? 4 : -4;
            this.alphaFadeStep = ((((this.fadeColour >> 24) & 0xFF) - (this.startAlphaRange / 2) - this.minStartAlpha) << 8) / this.alphaFadeDuration;
            this.redFadeStep += (this.redFadeStep <= 0) ? 4 : -4;
            this.greenFadeStep += (this.greenFadeStep > 0) ? -4 : 4;
            this.alphaFadeStep += (this.alphaFadeStep <= 0) ? 4 : -4;
        }
    }

    @OriginalMember(owner = "client!vaa", name = "a", descriptor = "(BLclient!ge;)V")
    public void decode(@OriginalArg(1) Packet packet) {
        while (true) {
            @Pc(3) int type = packet.g1();
            if (type == 0) {
                return;
            }
            this.decode(packet, type);
        }
    }
}

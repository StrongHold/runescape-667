package com.jagex.graphics;

import com.jagex.AnimBase;
import com.jagex.AnimFrame;
import com.jagex.AnimFrameset;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ka")
public abstract class Model {

    @OriginalMember(owner = "client!ka", name = "j", descriptor = "Z")
    protected boolean locked = false;

    @OriginalMember(owner = "client!ka", name = "<init>", descriptor = "()V")
    protected Model() {
    }

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(Lclient!tt;Lclient!ima;I)V")
    public abstract void render(@OriginalArg(0) Matrix matrix, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int flags);

    /**
     * animationPartialTransform
     */
    @OriginalMember(owner = "client!ka", name = "I", descriptor = "(I[IIIIZI[I)V")
    protected abstract void I(@OriginalArg(0) int type, @OriginalArg(1) int[] labels, @OriginalArg(2) int x, @OriginalArg(3) int y, @OriginalArg(4) int z, @OriginalArg(5) boolean rotateNormals, @OriginalArg(6) int originMask, @OriginalArg(7) int[] matrix);

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "()Z")
    public abstract boolean loadedTextures();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(III[I[IZZIII)V")
    public void applyTransform(@OriginalArg(0) int z, @OriginalArg(1) int type, @OriginalArg(2) int originMask, @OriginalArg(3) int[] labels, @OriginalArg(4) int[] matrix, @OriginalArg(5) boolean rotateNormals, @OriginalArg(7) int rotation, @OriginalArg(8) int x, @OriginalArg(9) int y) {
        @Pc(18) int swap;
        if (rotation == 1) {
            if (type == 0 || type == 1) {
                swap = -x;
                x = z;
                z = swap;
            } else if (type == 3) {
                swap = x;
                x = z;
                z = swap;
            } else if (type == 2) {
                swap = x;
                x = -z & 0x3FFF;
                z = swap & 0x3FFF;
            }
        } else if (rotation == 2) {
            if (type == 0 || type == 1) {
                x = -x;
                z = -z;
            } else if (type == 2) {
                z = -z & 0x3FFF;
                x = -x & 0x3FFF;
            }
        } else if (rotation == 3) {
            if (type == 0 || type == 1) {
                swap = x;
                x = -z;
                z = swap;
            } else if (type == 3) {
                swap = x;
                x = z;
                z = swap;
            } else if (type == 2) {
                swap = x;
                x = z & 0x3FFF;
                z = -swap & 0x3FFF;
            }
        }
        if (originMask == 65535) {
            this.applyTransformUnmasked(type, labels, x, y, z, rotation, rotateNormals);
        } else {
            this.I(type, labels, x, y, z, rotateNormals, originMask, matrix);
        }
    }

    /**
     * isTransparent
     */
    @OriginalMember(owner = "client!ka", name = "F", descriptor = "()Z")
    public abstract boolean F();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(Lclient!tt;)V")
    public abstract void apply(@OriginalArg(0) Matrix matrix);

    /**
     * setContrast
     */
    @OriginalMember(owner = "client!ka", name = "LA", descriptor = "(I)V")
    public abstract void LA(@OriginalArg(0) int contrast);

    /**
     * retexture
     */
    @OriginalMember(owner = "client!ka", name = "aa", descriptor = "(SS)V")
    public abstract void aa(@OriginalArg(0) short src, @OriginalArg(1) short dest);

    /**
     * rotateZAxis
     */
    @OriginalMember(owner = "client!ka", name = "VA", descriptor = "(I)V")
    public abstract void VA(@OriginalArg(0) int angle);

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(IILclient!rw;IIILclient!rw;Lclient!rw;IZILclient!rw;[ZII)V")
    public final void animateBlended(@OriginalArg(0) int otherFrameOffset, @OriginalArg(1) int frame, @OriginalArg(2) AnimFrameset nextFrameset, @OriginalArg(3) int otherNextFrame, @OriginalArg(5) int nextFrame, @OriginalArg(6) AnimFrameset otherNextFrameset, @OriginalArg(7) AnimFrameset frameset, @OriginalArg(8) int frameOffset, @OriginalArg(9) boolean rotateNormals, @OriginalArg(10) int otherFrameDuration, @OriginalArg(11) AnimFrameset otherFrameset, @OriginalArg(12) boolean[] blendFlags, @OriginalArg(13) int frameDuration, @OriginalArg(14) int otherFrame) {
        if (frame == -1) {
            return;
        }
        if (blendFlags == null || otherFrame == -1) {
            this.animate(frameset, frameOffset, frameDuration, nextFrameset, frame, nextFrame, 0, rotateNormals);
            return;
        }
        this.lock();
        if (!this.NA()) {
            this.unlock();
            return;
        }
        @Pc(44) AnimFrame current = frameset.frames[frame];
        @Pc(47) AnimBase base = current.base;
        @Pc(49) AnimFrame next = null;
        if (nextFrameset != null) {
            next = nextFrameset.frames[nextFrame];
            if (next.base != base) {
                next = null;
            }
        }
        this.applyFrame(next, blendFlags, frameOffset, false, null, 0, frameDuration, current, base, 65535, rotateNormals);
        @Pc(81) AnimFrame otherCurrent = otherFrameset.frames[otherFrame];
        @Pc(83) AnimFrame otherNext = null;
        if (otherNextFrameset != null) {
            otherNext = otherNextFrameset.frames[otherNextFrame];
            if (base != otherNext.base) {
                otherNext = null;
            }
        }
        this.applyTransformUnmasked(0, new int[0], 0, 0, 0, 0, rotateNormals);
        this.applyFrame(otherNext, blendFlags, otherFrameOffset, true, null, 0, otherFrameDuration, otherCurrent, otherCurrent.base, 65535, rotateNormals);
        this.wa();
        this.unlock();
    }

    /**
     * updateShadow
     */
    @OriginalMember(owner = "client!ka", name = "ba", descriptor = "(Lclient!r;)Lclient!r;")
    public abstract Shadow ba(@OriginalArg(0) Shadow shadow);

    @OriginalMember(owner = "client!ka", name = "e", descriptor = "()V")
    public abstract void method7479();

    /**
     * setFunctionMask
     */
    @OriginalMember(owner = "client!ka", name = "s", descriptor = "(I)V")
    public abstract void s(@OriginalArg(0) int functionMask);

    @OriginalMember(owner = "client!ka", name = "c", descriptor = "()[Lclient!mn;")
    public abstract ModelParticleEffector[] particleEffectors();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(Lclient!ka;IIIZ)V")
    public abstract void shareLight(@OriginalArg(0) Model other, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int z, @OriginalArg(4) boolean hideSharedFaces);

    @OriginalMember(owner = "client!ka", name = "P", descriptor = "(IIII)V")
    protected abstract void P(@OriginalArg(0) int type, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int z);

    @OriginalMember(owner = "client!ka", name = "f", descriptor = "()[Lclient!rv;")
    public abstract ModelParticleEmitter[] particleEmitters();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(IILclient!tt;ZII)Z")
    public abstract boolean pickedOrtho(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift, @OriginalArg(5) int angle);

    /**
     * getMinZ
     */
    @OriginalMember(owner = "client!ka", name = "HA", descriptor = "()I")
    public abstract int HA();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(Lclient!tt;Lclient!ima;II)V")
    public abstract void renderOrtho(@OriginalArg(0) Matrix matrix, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int orthoDepth, @OriginalArg(3) int flags);

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(IILclient!tt;ZI)Z")
    public abstract boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift);

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(Lclient!rw;IILclient!rw;IIIIZ)V")
    public final void animate(@OriginalArg(0) AnimFrameset frameset, @OriginalArg(1) int frameOffset, @OriginalArg(2) int frameDuration, @OriginalArg(3) AnimFrameset nextFrameset, @OriginalArg(4) int frame, @OriginalArg(5) int nextFrame, @OriginalArg(6) int rotation, @OriginalArg(8) boolean rotateNormals) {
        if (frame == -1) {
            return;
        }
        this.lock();
        if (!this.NA()) {
            this.unlock();
            return;
        }
        @Pc(23) AnimFrame current = frameset.frames[frame];
        @Pc(26) AnimBase base = current.base;
        @Pc(28) AnimFrame next = null;
        if (nextFrameset != null) {
            next = nextFrameset.frames[nextFrame];
            if (next.base != base) {
                next = null;
            }
        }
        this.applyFrame(next, null, frameOffset, false, null, rotation, frameDuration, current, base, 65535, rotateNormals);
        this.wa();
        this.unlock();
    }

    /**
     * translate
     */
    @OriginalMember(owner = "client!ka", name = "H", descriptor = "(III)V")
    public abstract void H(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z);

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(IIII)V")
    public abstract void adjustColours(@OriginalArg(0) int hue, @OriginalArg(1) int saturation, @OriginalArg(2) int lightness, @OriginalArg(3) int scale);

    /**
     * endAnimation
     */
    @OriginalMember(owner = "client!ka", name = "wa", descriptor = "()V")
    protected abstract void wa();

    /**
     * getContrast
     */
    @OriginalMember(owner = "client!ka", name = "da", descriptor = "()I")
    public abstract int da();

    @OriginalMember(owner = "client!ka", name = "b", descriptor = "()[B")
    public abstract byte[] getFaceAlphas();

    /**
     * rotateYAxis
     */
    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(I)V")
    public abstract void a(@OriginalArg(0) int angle);

    /**
     * mirror
     */
    @OriginalMember(owner = "client!ka", name = "v", descriptor = "()V")
    public abstract void v();

    /**
     * functionMask
     */
    @OriginalMember(owner = "client!ka", name = "ua", descriptor = "()I")
    public abstract int ua();

    /**
     * startAnimation
     */
    @OriginalMember(owner = "client!ka", name = "NA", descriptor = "()Z")
    protected abstract boolean NA();

    /**
     * cylinderRadius
     */
    @OriginalMember(owner = "client!ka", name = "na", descriptor = "()I")
    public abstract int na();

    /**
     * getMaxZ
     */
    @OriginalMember(owner = "client!ka", name = "G", descriptor = "()I")
    public abstract int G();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(IIIIIIILclient!s;I)V")
    protected final void conformToGround(@OriginalArg(0) int y, @OriginalArg(1) int maxAngleX, @OriginalArg(2) int x, @OriginalArg(4) int sizeX, @OriginalArg(5) int z, @OriginalArg(6) int sizeZ, @OriginalArg(7) Ground floor, @OriginalArg(8) int maxAngleZ) {
        @Pc(24) int southWestX = -sizeX / 2;
        @Pc(29) int southWestZ = -sizeZ / 2;
        @Pc(40) int southWest = floor.averageHeight(x - -southWestX, southWestZ + z);
        @Pc(44) int southEastX = sizeX / 2;
        @Pc(49) int southEastZ = -sizeZ / 2;
        @Pc(61) int southEast = floor.averageHeight(x + southEastX, southEastZ + z);
        @Pc(66) int northWestX = -sizeX / 2;
        @Pc(70) int northWestZ = sizeZ / 2;
        @Pc(82) int northWest = floor.averageHeight(northWestX + x, northWestZ + z);
        @Pc(86) int northEastX = sizeX / 2;
        @Pc(90) int northEastZ = sizeZ / 2;
        @Pc(101) int northEast = floor.averageHeight(x - -northEastX, northEastZ + z);
        @Pc(113) int south = southEast > southWest ? southWest : southEast;
        @Pc(125) int north = northEast <= northWest ? northEast : northWest;
        @Pc(133) int east = southEast >= northEast ? northEast : southEast;
        @Pc(141) int west = northWest > southWest ? southWest : northWest;
        @Pc(171) int limit;
        if (sizeZ != 0) {
            @Pc(156) int angleX = (int) (Math.atan2(south - north, sizeZ) * 2607.5945876176133D) & 0x3FFF;
            if (angleX != 0) {
                if (maxAngleX != 0) {
                    if (angleX > 8192) {
                        limit = 16384 - maxAngleX;
                        if (angleX < limit) {
                            angleX = limit;
                        }
                    } else if (maxAngleX < angleX) {
                        angleX = maxAngleX;
                    }
                }
                this.FA(angleX);
            }
        }
        @Pc(192) int height = southWest + northEast;
        if (sizeX != 0) {
            @Pc(207) int angleZ = (int) (Math.atan2(west - east, sizeX) * 2607.5945876176133D) & 0x3FFF;
            if (angleZ != 0) {
                if (maxAngleZ != 0) {
                    if (angleZ > 8192) {
                        limit = 16384 - maxAngleZ;
                        if (limit > angleZ) {
                            angleZ = limit;
                        }
                    } else if (angleZ > maxAngleZ) {
                        angleZ = maxAngleZ;
                    }
                }
                this.VA(angleZ);
            }
        }
        if (height > southEast + northWest) {
            height = northWest + southEast;
        }
        height = (height >> 1) - y;
        if (height != 0) {
            this.H(0, height, 0);
        }
    }

    @OriginalMember(owner = "client!ka", name = "g", descriptor = "()V")
    protected abstract void lock();

    /**
     * recolour
     */
    @OriginalMember(owner = "client!ka", name = "ia", descriptor = "(SS)V")
    public abstract void ia(@OriginalArg(0) short src, @OriginalArg(1) short dest);

    /**
     * getMaxY
     */
    @OriginalMember(owner = "client!ka", name = "EA", descriptor = "()I")
    public abstract int EA();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(Lclient!tt;IZ)V")
    public abstract void transform(@OriginalArg(0) Matrix matrix, @OriginalArg(1) int originMask, @OriginalArg(2) boolean relative);

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(IILclient!rw;)V")
    public final void animateShadow(@OriginalArg(0) int frame, @OriginalArg(2) AnimFrameset frameset) {
        if (frame == -1) {
            return;
        }
        this.lock();
        if (!this.NA()) {
            this.unlock();
            return;
        }
        @Pc(33) AnimFrame current = frameset.frames[frame];
        @Pc(36) AnimBase base = current.base;
        for (@Pc(38) int i = 0; i < current.transformCount; i++) {
            @Pc(45) short group = current.groups[i];
            if (base.shadowed[group]) {
                if (current.origins[i] != -1) {
                    this.P(0, 0, 0, 0);
                }
                this.P(base.transformTypes[group], current.xValues[i], current.yValues[i], current.zValues[i]);
            }
        }
        this.wa();
        this.unlock();
    }

    @OriginalMember(owner = "client!ka", name = "d", descriptor = "()V")
    protected abstract void unlock();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(BIZ)Lclient!ka;")
    public abstract Model copy(@OriginalArg(0) byte slot, @OriginalArg(1) int functionMask, @OriginalArg(2) boolean ensureLit);

    /**
     * setAmbient
     */
    @OriginalMember(owner = "client!ka", name = "C", descriptor = "(I)V")
    public abstract void C(@OriginalArg(0) int ambient);

    /**
     * rotateXAxis
     */
    @OriginalMember(owner = "client!ka", name = "FA", descriptor = "(I)V")
    public abstract void FA(@OriginalArg(0) int angle);

    /**
     * getMovingTextures
     */
    @OriginalMember(owner = "client!ka", name = "r", descriptor = "()Z")
    public abstract boolean r();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(ILclient!rw;Lclient!rw;II[IBIZII)V")
    public final void animateMasked(@OriginalArg(0) int frameOffset, @OriginalArg(1) AnimFrameset frameset, @OriginalArg(2) AnimFrameset nextFrameset, @OriginalArg(3) int frame, @OriginalArg(5) int[] matrix, @OriginalArg(7) int frameDuration, @OriginalArg(8) boolean rotateNormals, @OriginalArg(9) int nextFrame, @OriginalArg(10) int originMask) {
        if (frame == -1) {
            return;
        }
        this.lock();
        if (!this.NA()) {
            this.unlock();
            return;
        }
        @Pc(29) AnimFrame current = frameset.frames[frame];
        @Pc(32) AnimBase base = current.base;
        @Pc(34) AnimFrame next = null;
        if (nextFrameset != null) {
            next = nextFrameset.frames[nextFrame];
            if (next.base != base) {
                next = null;
            }
        }
        this.applyFrame(next, null, frameOffset, false, matrix, 0, frameDuration, current, base, originMask, rotateNormals);
        this.wa();
        this.unlock();
    }

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(Lclient!nb;[ZIZB[IIILclient!nb;Lclient!qda;IZ)V")
    public void applyFrame(@OriginalArg(0) AnimFrame next, @OriginalArg(1) boolean[] blendFlags, @OriginalArg(2) int frameOffset, @OriginalArg(3) boolean blended, @OriginalArg(5) int[] matrix, @OriginalArg(6) int rotation, @OriginalArg(7) int frameDuration, @OriginalArg(8) AnimFrame frame, @OriginalArg(9) AnimBase base, @OriginalArg(10) int originMask, @OriginalArg(11) boolean rotateNormals) {
        @Pc(11) int index;
        if (next == null || frameOffset == 0) {
            for (index = 0; index < frame.transformCount; index++) {
                @Pc(17) short group = frame.groups[index];
                if (blendFlags == null || blended == blendFlags[group] || base.transformTypes[group] == 0) {
                    @Pc(43) short origin = frame.origins[index];
                    if (origin != -1) {
                        this.applyTransform(0, 0, originMask & base.originMasks[origin], base.transformLabels[origin], matrix, rotateNormals, rotation, 0, 0);
                    }
                    this.applyTransform(frame.zValues[index], base.transformTypes[group], originMask & base.originMasks[group], base.transformLabels[group], matrix, rotateNormals, rotation, frame.xValues[index], frame.yValues[index]);
                }
            }
            return;
        }
        index = 0;
        @Pc(116) int nextIndex = 0;
        for (@Pc(123) int group = 0; group < base.transformCount; group++) {
            @Pc(126) boolean inFrame = false;
            if (frame.transformCount > index && group == frame.groups[index]) {
                inFrame = true;
            }
            @Pc(146) boolean inNext = false;
            if (next.transformCount > nextIndex && group == next.groups[nextIndex]) {
                inNext = true;
            }
            if (inFrame || inNext) {
                if (blendFlags == null || blendFlags[group] == blended || base.transformTypes[group] == 0) {
                    @Pc(206) short defaultValue = 0;
                    @Pc(211) int type = base.transformTypes[group];
                    if (type == 3 || type == 10) {
                        defaultValue = 128;
                    }
                    @Pc(243) short x;
                    @Pc(238) short y;
                    @Pc(228) short z;
                    @Pc(233) short origin;
                    @Pc(248) byte flags;
                    if (inFrame) {
                        z = frame.zValues[index];
                        origin = frame.origins[index];
                        y = frame.yValues[index];
                        x = frame.xValues[index];
                        flags = frame.tweenFlags[index];
                        index++;
                    } else {
                        flags = 0;
                        z = defaultValue;
                        x = defaultValue;
                        origin = -1;
                        y = defaultValue;
                    }
                    @Pc(267) short nextX;
                    @Pc(282) short nextY;
                    @Pc(272) short nextZ;
                    @Pc(287) short nextOrigin;
                    @Pc(277) byte nextFlags;
                    if (inNext) {
                        nextX = next.xValues[nextIndex];
                        nextZ = next.zValues[nextIndex];
                        nextFlags = next.tweenFlags[nextIndex];
                        nextY = next.yValues[nextIndex];
                        nextOrigin = next.origins[nextIndex];
                        nextIndex++;
                    } else {
                        nextY = defaultValue;
                        nextZ = defaultValue;
                        nextFlags = 0;
                        nextOrigin = -1;
                        nextX = defaultValue;
                    }
                    if (origin != -1) {
                        this.applyTransform(0, 0, originMask & base.originMasks[origin], base.transformLabels[origin], matrix, rotateNormals, rotation, 0, 0);
                    } else if (nextOrigin != -1) {
                        this.applyTransform(0, 0, originMask & base.originMasks[nextOrigin], base.transformLabels[nextOrigin], matrix, rotateNormals, rotation, 0, 0);
                    }
                    @Pc(423) int tweenX;
                    @Pc(413) int tweenY;
                    @Pc(439) int tweenZ;
                    if ((flags & 0x2) == 0 && (nextFlags & 0x1) == 0) {
                        @Pc(376) int deltaX;
                        if (type == 2) {
                            deltaX = nextX - x & 0x3FFF;
                            @Pc(383) int deltaY = nextY - y & 0x3FFF;
                            @Pc(389) int deltaZ = nextZ - z & 0x3FFF;
                            if (deltaX >= 8192) {
                                deltaX -= 16384;
                            }
                            if (deltaY >= 8192) {
                                deltaY -= 16384;
                            }
                            tweenY = y + frameOffset * deltaY / frameDuration & 0x3FFF;
                            tweenX = deltaX * frameOffset / frameDuration + x & 0x3FFF;
                            if (deltaZ >= 8192) {
                                deltaZ -= 16384;
                            }
                            tweenZ = frameOffset * deltaZ / frameDuration + z & 0x3FFF;
                        } else if (type == 9) {
                            deltaX = nextX - x & 0x3FFF;
                            if (deltaX >= 8192) {
                                deltaX -= 16384;
                            }
                            tweenZ = 0;
                            tweenY = 0;
                            tweenX = x + frameOffset * deltaX / frameDuration & 0x3FFF;
                        } else if (type == 7) {
                            deltaX = nextX - x & 0x3F;
                            if (deltaX >= 32) {
                                deltaX -= 64;
                            }
                            tweenZ = (nextZ - z) * frameOffset / frameDuration + z;
                            tweenY = (nextY - y) * frameOffset / frameDuration + y;
                            tweenX = x + frameOffset * deltaX / frameDuration & 0x3F;
                        } else {
                            tweenZ = z + (nextZ - z) * frameOffset / frameDuration;
                            tweenY = (nextY - y) * frameOffset / frameDuration + y;
                            tweenX = frameOffset * (nextX - x) / frameDuration + x;
                        }
                    } else {
                        tweenY = y;
                        tweenX = x;
                        tweenZ = z;
                    }
                    this.applyTransform(tweenZ, type, base.originMasks[group] & originMask, base.transformLabels[group], matrix, rotateNormals, rotation, tweenX, tweenY);
                } else {
                    if (inNext) {
                        nextIndex++;
                    }
                    if (inFrame) {
                        index++;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(B[B)V")
    public abstract void updateAlphas(@OriginalArg(0) byte alpha, @OriginalArg(1) byte[] alphas);

    /**
     * getMaxX
     */
    @OriginalMember(owner = "client!ka", name = "RA", descriptor = "()I")
    public abstract int RA();

    /**
     * getMinY
     */
    @OriginalMember(owner = "client!ka", name = "fa", descriptor = "()I")
    public abstract int fa();

    /**
     * hillChange
     */
    @OriginalMember(owner = "client!ka", name = "p", descriptor = "(IILclient!s;Lclient!s;III)V")
    public abstract void p(@OriginalArg(0) int hillType, @OriginalArg(1) int hillValue, @OriginalArg(2) Ground floor, @OriginalArg(3) Ground ceiling, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z);

    /**
     * getAmbient
     */
    @OriginalMember(owner = "client!ka", name = "WA", descriptor = "()I")
    public abstract int WA();

    /**
     * getSphereRadius
     */
    @OriginalMember(owner = "client!ka", name = "ma", descriptor = "()I")
    public abstract int ma();

    /**
     * scale
     */
    @OriginalMember(owner = "client!ka", name = "O", descriptor = "(III)V")
    public abstract void O(@OriginalArg(0) int scaleX, @OriginalArg(1) int scaleY, @OriginalArg(2) int scaleZ);

    /**
     * getMinX
     */
    @OriginalMember(owner = "client!ka", name = "V", descriptor = "()I")
    public abstract int V();

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(I[IIIIIZ)V")
    protected abstract void applyTransformUnmasked(@OriginalArg(0) int type, @OriginalArg(1) int[] labels, @OriginalArg(2) int x, @OriginalArg(3) int y, @OriginalArg(4) int z, @OriginalArg(5) int rotation, @OriginalArg(6) boolean rotateNormals);

    /**
     * rotateYAxisWithNormals
     */
    @OriginalMember(owner = "client!ka", name = "k", descriptor = "(I)V")
    public abstract void k(@OriginalArg(0) int angle);
}

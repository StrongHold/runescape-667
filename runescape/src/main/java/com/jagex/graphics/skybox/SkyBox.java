package com.jagex.graphics.skybox;

import com.jagex.core.algorithms.Quicksort;
import com.jagex.core.util.Arrays;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!gm")
public final class SkyBox {

    @OriginalMember(owner = "client!gm", name = "w", descriptor = "Z")
    public boolean fading;

    @OriginalMember(owner = "client!gm", name = "o", descriptor = "I")
    public int topColour;

    @OriginalMember(owner = "client!gm", name = "t", descriptor = "I")
    public int modelAlpha;

    @OriginalMember(owner = "client!gm", name = "n", descriptor = "[B")
    public byte[] faceAlphas;

    @OriginalMember(owner = "client!gm", name = "z", descriptor = "I")
    public int fadeStartAlpha;

    @OriginalMember(owner = "client!gm", name = "G", descriptor = "Lclient!st;")
    public Sprite textureSprite;

    @OriginalMember(owner = "client!gm", name = "E", descriptor = "I")
    public int visibleSphereCount;

    @OriginalMember(owner = "client!gm", name = "i", descriptor = "I")
    public int bottomColour;

    @OriginalMember(owner = "client!gm", name = "D", descriptor = "Lclient!gm;")
    public SkyBox fadeTarget;

    @OriginalMember(owner = "client!gm", name = "C", descriptor = "Lclient!ka;")
    public Model model;

    @OriginalMember(owner = "client!gm", name = "e", descriptor = "I")
    public int fadeAlpha;

    @OriginalMember(owner = "client!gm", name = "K", descriptor = "I")
    public int textureSize;

    @OriginalMember(owner = "client!gm", name = "y", descriptor = "Z")
    public boolean spheresDirty = true;

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "I")
    public int layoutHeight = -1;

    @OriginalMember(owner = "client!gm", name = "J", descriptor = "I")
    public final int sphereOffsetY;

    @OriginalMember(owner = "client!gm", name = "A", descriptor = "I")
    public final int tileMode;

    @OriginalMember(owner = "client!gm", name = "F", descriptor = "[Lclient!ks;")
    public final SkyBoxSphere[] spheres;

    @OriginalMember(owner = "client!gm", name = "v", descriptor = "I")
    public final int sphereOffsetZ;

    @OriginalMember(owner = "client!gm", name = "m", descriptor = "I")
    public final int sphereOffsetX;

    @OriginalMember(owner = "client!gm", name = "k", descriptor = "I")
    public final int texture;

    @OriginalMember(owner = "client!gm", name = "c", descriptor = "[Lclient!ks;")
    public final SkyBoxSphere[] visibleSpheres;

    @OriginalMember(owner = "client!gm", name = "r", descriptor = "Lclient!ks;")
    public final SkyBoxSphere lightSphere;

    @OriginalMember(owner = "client!gm", name = "B", descriptor = "I")
    public final int meshId;

    @OriginalMember(owner = "client!gm", name = "<init>", descriptor = "(I[Lclient!ks;IIIIII)V")
    public SkyBox(@OriginalArg(0) int texture, @OriginalArg(1) SkyBoxSphere[] spheres, @OriginalArg(2) int lightSphereIndex, @OriginalArg(3) int sphereOffsetX, @OriginalArg(4) int sphereOffsetY, @OriginalArg(5) int sphereOffsetZ, @OriginalArg(6) int tileMode, @OriginalArg(7) int meshId) {
        this.sphereOffsetY = sphereOffsetY;
        this.tileMode = tileMode;
        this.spheres = spheres;
        this.sphereOffsetZ = sphereOffsetZ;
        this.sphereOffsetX = sphereOffsetX;
        this.texture = texture;
        if (spheres == null) {
            this.visibleSpheres = null;
            this.lightSphere = null;
        } else {
            this.visibleSpheres = new SkyBoxSphere[spheres.length];
            this.lightSphere = lightSphereIndex < 0 ? null : spheres[lightSphereIndex];
        }
        this.meshId = meshId;
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(ZILclient!ha;IIIIIIIIZB)V")
    public void method3159(@OriginalArg(1) int viewportY, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) int yaw, @OriginalArg(4) int viewportWidth, @OriginalArg(5) int fillColour, @OriginalArg(6) int roll, @OriginalArg(7) int viewportHeight, @OriginalArg(8) int viewportX, @OriginalArg(9) int yawOffset, @OriginalArg(10) int pitch) {
        @Pc(5) int alpha = 0;
        if (this.fading) {
            alpha = this.fadeAlpha;
        }
        if (this.fadeTarget == null) {
            this.renderLayer(roll, yawOffset, viewportHeight, toolkit, true, fillColour, viewportWidth, viewportX, yaw, alpha, pitch, viewportY);
            return;
        }
        @Pc(16) SkyBox first = this;
        @Pc(19) SkyBox second = this.fadeTarget;
        if (this.hashCode() > second.hashCode()) {
            alpha = 255 - alpha;
            second = this;
            first = this.fadeTarget;
        }
        first.renderLayer(roll, yawOffset, viewportHeight, toolkit, true, fillColour, viewportWidth, viewportX, yaw, alpha, pitch, viewportY);
        second.renderLayer(roll, yawOffset, viewportHeight, toolkit, false, fillColour, viewportWidth, viewportX, yaw, 255 - alpha, pitch, viewportY);
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(IIB)V")
    public void method3160(@OriginalArg(0) int targetAlpha, @OriginalArg(1) int elapsed) {
        this.fadeAlpha = this.fadeStartAlpha + (targetAlpha - this.fadeStartAlpha) * elapsed / 255;
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(IIILclient!ha;ZZIIIIIIII)V")
    public void renderLayer(@OriginalArg(0) int roll, @OriginalArg(1) int yawOffset, @OriginalArg(2) int viewportHeight, @OriginalArg(3) Toolkit toolkit, @OriginalArg(4) boolean clear, @OriginalArg(6) int fillColour, @OriginalArg(7) int viewportWidth, @OriginalArg(9) int viewportX, @OriginalArg(10) int yaw, @OriginalArg(11) int fade, @OriginalArg(12) int pitch, @OriginalArg(13) int viewportY) {
        @Pc(10) int alpha = 255 - fade;
        if (this.model == null) {
            yaw = yaw + yawOffset & 0x3FFF;
            toolkit.ya();
            if (this.texture == -1 || this.textureSize == 0) {
                toolkit.aa(viewportX, viewportY, viewportWidth, viewportHeight, alpha << 24 | fillColour, 1);
            } else {
                @Pc(74) TextureMetrics metrics = SkyBoxSphere.textureSource.getMetrics(this.texture);
                if (this.textureSprite == null && SkyBoxSphere.textureSource.textureAvailable(this.texture)) {
                    @Pc(120) int[] pixels = metrics.alphaBlendMode == 2 ? SkyBoxSphere.textureSource.argbOutput(0.7F, this.texture, this.textureSize, this.textureSize) : SkyBoxSphere.textureSource.rgbOutput(this.textureSize, false, this.textureSize, this.texture, 0.7F);
                    this.topColour = pixels[0];
                    this.bottomColour = pixels[pixels.length - 1];
                    this.textureSprite = toolkit.createSprite(this.textureSize, this.textureSize, this.textureSize, pixels);
                }
                @Pc(161) int mode = alpha == 255 ? (metrics.alphaBlendMode == 2 ? 1 : 0) : 1;
                if (mode == 1 && clear) {
                    toolkit.aa(viewportX, viewportY, viewportWidth, viewportHeight, fillColour, 0);
                }
                if (this.textureSprite != null) {
                    @Pc(187) int offsetY = viewportHeight * pitch / -4096;
                    @Pc(199) int offsetX;
                    for (offsetX = yaw * viewportHeight / 4096 + (viewportWidth - viewportHeight) / 2; offsetX > viewportHeight; offsetX -= viewportHeight) {
                        /* empty */
                    }
                    while (offsetX < 0) {
                        offsetX += viewportHeight;
                    }
                    @Pc(233) int x;
                    if (this.tileMode == 1) {
                        for (x = offsetX - viewportHeight; x < viewportWidth; x += viewportHeight) {
                            this.textureSprite.render(viewportX + x, offsetY + viewportY, viewportHeight, viewportHeight, 0, alpha << 24 | 0xFFFFFF, mode);
                        }
                        if ((this.topColour & 0xFF000000) != 0) {
                            toolkit.fillRect(0, 0, viewportWidth, offsetY + viewportY + 1, this.topColour);
                        }
                        if ((this.bottomColour & 0xFF000000) != 0) {
                            toolkit.fillRect(0, viewportHeight + viewportY + offsetY, viewportWidth, viewportHeight - viewportHeight - offsetY - viewportY, this.bottomColour);
                        }
                    } else {
                        while (viewportHeight < offsetY) {
                            offsetY -= viewportHeight;
                        }
                        while (offsetY < 0) {
                            offsetY += viewportHeight;
                        }
                        for (x = offsetX - viewportHeight; x < viewportWidth; x += viewportHeight) {
                            for (@Pc(360) int y = offsetY - viewportHeight; y < viewportHeight; y += viewportHeight) {
                                this.textureSprite.render(viewportX + x, y - -viewportY, viewportHeight, viewportHeight, 0, alpha << 24 | 0xFFFFFF, mode);
                            }
                        }
                    }
                }
            }
        } else {
            if (clear) {
                toolkit.GA(fillColour);
                toolkit.ya();
            }
            this.renderModel(yaw, fade, pitch, roll, toolkit);
        }
        for (@Pc(417) int i = this.visibleSphereCount - 1; i >= 0; i--) {
            this.visibleSpheres[i].render(toolkit, viewportX, viewportY, viewportWidth, viewportHeight, pitch, yaw, this.sphereOffsetX, this.sphereOffsetY, this.sphereOffsetZ, alpha);
        }
        toolkit.ya();
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(IILclient!ha;IIIIIIII)V")
    public void method3162(@OriginalArg(2) Toolkit toolkit, @OriginalArg(3) int viewportY, @OriginalArg(5) int viewportX, @OriginalArg(6) int viewportWidth, @OriginalArg(8) int pitch, @OriginalArg(9) int yaw, @OriginalArg(10) int viewportHeight) {
        this.method3159(viewportY, toolkit, yaw, viewportWidth, 0, 0, viewportHeight, viewportX, 0, pitch);
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(BLclient!gm;)V")
    public void method3163(@OriginalArg(1) SkyBox target) {
        if (this.fading) {
            this.fadeStartAlpha = this.fadeAlpha;
        } else if (target != null && target.fading) {
            this.fadeStartAlpha = 255 - target.fadeAlpha;
        } else {
            this.fadeStartAlpha = 0;
        }
        this.fadeAlpha = 0;
        this.fading = true;
        this.fadeTarget = target;
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(IIIIILclient!ha;)V")
    public void renderModel(@OriginalArg(0) int yaw, @OriginalArg(1) int alpha, @OriginalArg(2) int pitch, @OriginalArg(3) int roll, @OriginalArg(5) Toolkit toolkit) {
        @Pc(7) Matrix camera = toolkit.camera().copy();
        @Pc(10) Matrix skyCamera = toolkit.createMatrix();
        skyCamera.applyTranslation(0, 0, 0);
        skyCamera.rotateAxisY(yaw & 0x3FFF);
        skyCamera.rotateAxisX(pitch & 0x3FFF);
        skyCamera.rotateAxisZ(roll & 0x3FFF);
        toolkit.setCamera(skyCamera);
        @Pc(36) Matrix matrix = toolkit.createMatrix();
        matrix.makeIdentity();
        if (this.modelAlpha != alpha) {
            this.model.updateAlphas((byte) alpha, this.faceAlphas);
            this.modelAlpha = alpha;
        }
        this.model.render(matrix, null, 0);
        toolkit.setCamera(camera);
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(B)Z")
    public boolean method3165() {
        return this.fading;
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(ILclient!ha;)V")
    public void loadModel(@OriginalArg(1) Toolkit toolkit) {
        try {
            @Pc(9) js5 archive = SkyBoxSphere.modelJs5;
            @Pc(17) boolean downloaded = archive.requestgroupdownload(this.meshId);
            if (downloaded) {
                toolkit.ZA(16777215, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
                @Pc(36) Mesh mesh = Mesh.load(this.meshId, SkyBoxSphere.modelJs5);
                this.model = toolkit.createModel(mesh, 1099776, 0, 255, 1);
                @Pc(49) byte[] alphas = this.model.getFaceAlphas();
                if (alphas == null) {
                    this.faceAlphas = null;
                } else {
                    this.faceAlphas = new byte[alphas.length];
                    Arrays.copy(alphas, 0, this.faceAlphas, 0, alphas.length);
                }
            }
        } catch (@Pc(73) Exception ex) {
            /* empty */
        }
    }

    @OriginalMember(owner = "client!gm", name = "b", descriptor = "(B)Lclient!gm;")
    public SkyBox method3167() {
        return this.fadeTarget;
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(IIILclient!ha;)Z")
    public boolean method3168(@OriginalArg(0) int detail, @OriginalArg(2) int viewportHeight, @OriginalArg(3) Toolkit toolkit) {
        if (this.layoutHeight != viewportHeight) {
            this.layoutHeight = viewportHeight;
            @Pc(16) int size = SkyBoxSphere.method5587(viewportHeight);
            if (size > 512) {
                size = 512;
            }
            if (size <= 0) {
                size = 1;
            }
            if (size != this.textureSize) {
                this.textureSize = size;
                this.textureSprite = null;
            }
            if (this.spheres != null) {
                this.visibleSphereCount = 0;
                @Pc(57) int[] distances = new int[this.spheres.length];
                for (@Pc(59) int i = 0; i < this.spheres.length; i++) {
                    @Pc(68) SkyBoxSphere sphere = this.spheres[i];
                    if (sphere.update(this.sphereOffsetX, this.sphereOffsetY, this.sphereOffsetZ, this.layoutHeight)) {
                        distances[this.visibleSphereCount] = sphere.distance;
                        this.visibleSpheres[this.visibleSphereCount++] = sphere;
                    }
                }
                Quicksort.sort(this.visibleSpheres, distances, 0, this.visibleSphereCount - 1);
            }
            this.spheresDirty = true;
        }
        @Pc(131) boolean changed = false;
        if (this.spheresDirty) {
            this.spheresDirty = false;
            for (@Pc(142) int i = this.visibleSphereCount - 1; i >= 0; i--) {
                @Pc(155) boolean ready = this.visibleSpheres[i].prepareSprite(toolkit, this.lightSphere);
                this.spheresDirty |= !ready;
                changed |= ready;
            }
        }
        if (detail == 0 || !toolkit.method7992()) {
            this.model = null;
        } else if (this.model == null && this.meshId >= 0) {
            this.loadModel(toolkit);
        }
        if (this.fadeTarget != null && this.fadeTarget != this) {
            this.fadeTarget.method3169();
            changed |= this.fadeTarget.method3168(detail, viewportHeight, toolkit);
        }
        return changed;
    }

    @OriginalMember(owner = "client!gm", name = "a", descriptor = "(I)V")
    public void method3169() {
        this.fading = false;
        this.fadeAlpha = 0;
        this.fadeTarget = null;
    }
}

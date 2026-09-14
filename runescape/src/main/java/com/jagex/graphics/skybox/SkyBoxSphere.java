package com.jagex.graphics.skybox;

import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import com.jagex.math.IntMath;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ks")
public final class SkyBoxSphere {

    @OriginalMember(owner = "client!ks", name = "h", descriptor = "[I")
    public static final int[] savedClipping = new int[4];

    @OriginalMember(owner = "client!wga", name = "e", descriptor = "Lclient!d;")
    public static TextureSource textureSource;

    @OriginalMember(owner = "client!ks", name = "l", descriptor = "Lclient!ka;")
    public static Model sphereModel;

    @OriginalMember(owner = "client!ks", name = "j", descriptor = "Lclient!st;")
    public static Sprite shadingSprite;

    @OriginalMember(owner = "client!ks", name = "o", descriptor = "Lclient!st;")
    public static Sprite maskSprite;

    @OriginalMember(owner = "client!mba", name = "E", descriptor = "Lclient!sb;")
    public static js5 modelJs5;

    @OriginalMember(owner = "client!ks", name = "n", descriptor = "I")
    public int screenSize;

    @OriginalMember(owner = "client!ks", name = "e", descriptor = "I")
    public int yaw;

    @OriginalMember(owner = "client!ks", name = "d", descriptor = "I")
    public int distance;

    @OriginalMember(owner = "client!ks", name = "b", descriptor = "Lclient!st;")
    public Sprite sprite;

    @OriginalMember(owner = "client!ks", name = "m", descriptor = "I")
    public int textureSize;

    @OriginalMember(owner = "client!ks", name = "r", descriptor = "I")
    public int pitch;

    @OriginalMember(owner = "client!ks", name = "i", descriptor = "I")
    public final int x;

    @OriginalMember(owner = "client!ks", name = "k", descriptor = "I")
    public final int y;

    @OriginalMember(owner = "client!ks", name = "g", descriptor = "I")
    public final int z;

    @OriginalMember(owner = "client!ks", name = "f", descriptor = "Z")
    public final boolean infinite;

    @OriginalMember(owner = "client!ks", name = "p", descriptor = "I")
    public final int contentId;

    @OriginalMember(owner = "client!ks", name = "a", descriptor = "I")
    public final int renderType;

    @OriginalMember(owner = "client!ks", name = "q", descriptor = "I")
    public final int size;

    @OriginalMember(owner = "client!ks", name = "c", descriptor = "I")
    public final int colour;

    @OriginalMember(owner = "client!ks", name = "t", descriptor = "I")
    public final int rotateX;

    @OriginalMember(owner = "client!ks", name = "u", descriptor = "I")
    public final int rotateY;

    @OriginalMember(owner = "client!ks", name = "s", descriptor = "I")
    public final int rotateZ;

    @OriginalMember(owner = "client!ks", name = "<init>", descriptor = "(IIIIIIIZIII)V")
    public SkyBoxSphere(@OriginalArg(0) int renderType, @OriginalArg(1) int contentId, @OriginalArg(2) int x, @OriginalArg(3) int y, @OriginalArg(4) int z, @OriginalArg(5) int size, @OriginalArg(6) int colour, @OriginalArg(7) boolean infinite, @OriginalArg(8) int rotateX, @OriginalArg(9) int rotateY, @OriginalArg(10) int rotateZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.infinite = infinite;
        this.contentId = contentId;
        this.colour = colour;
        this.size = size;
        this.renderType = renderType;
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.rotateZ = rotateZ;
    }

    @OriginalMember(owner = "client!ks", name = "a", descriptor = "(Lclient!ha;)V")
    public static void createSphereModel(@OriginalArg(0) Toolkit toolkit) {
        if (sphereModel != null) {
            return;
        }
        @Pc(8) Mesh mesh = new Mesh(580, 1104, 1);
        mesh.addSphericalSpace();
        mesh.addVertex(0, 128, 0);
        mesh.addVertex(0, -128, 0);
        for (@Pc(37) int i = 0; i <= 24; i++) {
            @Pc(44) int longitude = i * 8192 / 24;
            @Pc(48) int sinLongitude = Trig1.SIN[longitude];
            @Pc(52) int cosLongitude = Trig1.COS[longitude];
            @Pc(61) int local61;
            @Pc(67) int local67;
            @Pc(75) int local75;
            @Pc(83) int local83;
            for (@Pc(54) int j = 1; j < 24; j++) {
                local61 = j * 8192 / 24;
                local67 = Trig1.COS[local61] >> 7;
                local75 = Trig1.SIN[local61] * sinLongitude >> 21;
                local83 = Trig1.SIN[local61] * cosLongitude >> 21;
                mesh.addVertex(-local75, local67, local83);
            }
            if (i > 0) {
                local61 = i * 23 + 2;
                local67 = local61 - 23;
                mesh.addFace(0, local61, local67, (short) 127, (short) 0, (byte) 0, (byte) 0, (byte) 0);
                for (local75 = 1; local75 < 23; local75++) {
                    local83 = local67 + 1;
                    @Pc(130) int local130 = local61 + 1;
                    mesh.addFace(local67, local61, local83, (short) 127, (short) 0, (byte) 0, (byte) 0, (byte) 0);
                    mesh.addFace(local83, local61, local130, (short) 127, (short) 0, (byte) 0, (byte) 0, (byte) 0);
                    local67 = local83;
                    local61 = local130;
                }
                mesh.addFace(local61, 1, local67, (short) 127, (short) 0, (byte) 0, (byte) 0, (byte) 0);
            }
        }
        mesh.maxVertex = mesh.vertexCount;
        mesh.faceLabel = null;
        mesh.vertexLabel = null;
        mesh.facePriority = null;
        sphereModel = toolkit.createModel(mesh, 51200, 33, 64, 768);
    }

    @OriginalMember(owner = "client!ks", name = "b", descriptor = "(Lclient!ha;)V")
    public static void createSprites(@OriginalArg(0) Toolkit toolkit) {
        if (maskSprite != null) {
            return;
        }
        @Pc(4) int[] shading = new int[16384];
        @Pc(7) int[] mask = new int[16384];
        for (@Pc(9) int i = 0; i < 64; i++) {
            @Pc(14) int dy = 64 - i;
            @Pc(18) int dySquared = dy * dy;
            @Pc(24) int mirrorY = 128 - i - 1;
            @Pc(28) int row = i * 128;
            @Pc(32) int mirrorRow = mirrorY * 128;
            for (@Pc(34) int j = 0; j < 64; j++) {
                @Pc(39) int dx = 64 - j;
                @Pc(43) int dxSquared = dx * dx;
                @Pc(49) int mirrorX = 128 - j - 1;
                @Pc(59) int intensity = 256 - (dxSquared + dySquared << 8) / 4096;
                intensity = intensity * 16 * 192 / 1536;
                if (intensity < 0) {
                    intensity = 0;
                } else if (intensity > 255) {
                    intensity = 255;
                }
                @Pc(81) int halfIntensity = intensity / 2;
                mask[row + j] = mask[row + mirrorX] = mask[mirrorRow + j] = mask[mirrorRow + mirrorX] = (intensity | 0xFF00) << 16;
                shading[row + j] = shading[row + mirrorX] = shading[mirrorRow + j] = shading[mirrorRow + mirrorX] = 127 - halfIntensity << 24 | 0xFFFFFF;
            }
        }
        maskSprite = toolkit.createSprite(128, 128, 128, mask);
        shadingSprite = toolkit.createSprite(128, 128, 128, shading);
    }

    @OriginalMember(owner = "client!ml", name = "a", descriptor = "(IZ)I")
    public static int method5587(@OriginalArg(0) int arg0) {
        @Pc(7) int local7 = arg0 >>> 1;
        @Pc(13) int local13 = local7 | local7 >>> 1;
        @Pc(19) int local19 = local13 | local13 >>> 2;
        @Pc(25) int local25 = local19 | local19 >>> 4;
        @Pc(40) int local40 = local25 | local25 >>> 8;
        @Pc(46) int local46 = local40 | local40 >>> 16;
        return ~local46 & arg0;
    }

    @OriginalMember(owner = "client!ks", name = "c", descriptor = "(Lclient!ha;Lclient!ks;)Z")
    public boolean prepareSprite(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) SkyBoxSphere light) {
        return this.sprite != null || this.buildSprite(toolkit, light);
    }

    @OriginalMember(owner = "client!ks", name = "d", descriptor = "(Lclient!ha;Lclient!ks;)V")
    public void renderSphereSprite(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) SkyBoxSphere light) {
        createSphereModel(toolkit);
        createSprites(toolkit);
        toolkit.K(savedClipping);
        toolkit.KA(0, 0, this.textureSize, this.textureSize);
        toolkit.ya();
        toolkit.aa(0, 0, this.textureSize, this.textureSize, this.colour | 0xFF000000, 0);
        @Pc(31) int lightX = 0;
        @Pc(33) int lightY = 0;
        @Pc(35) int lightZ = 256;
        if (light != null) {
            if (light.infinite) {
                lightX = -light.x;
                lightY = -light.y;
                lightZ = -light.z;
            } else {
                lightX = light.x - this.x;
                lightY = light.y - this.y;
                lightZ = light.z - this.z;
            }
        }
        @Pc(79) int local79;
        @Pc(84) int local84;
        @Pc(94) int local94;
        if (this.pitch != 0) {
            local79 = Trig1.SIN[this.pitch];
            local84 = Trig1.COS[this.pitch];
            local94 = lightY * local84 - lightZ * local79 >> 14;
            lightZ = lightY * local79 + lightZ * local84 >> 14;
            lightY = local94;
        }
        if (this.yaw != 0) {
            local79 = Trig1.SIN[this.yaw];
            local84 = Trig1.COS[this.yaw];
            local94 = lightZ * local79 + lightX * local84 >> 14;
            lightZ = lightZ * local84 - lightX * local79 >> 14;
            lightX = local94;
        }
        @Pc(147) Model sphere = sphereModel.copy((byte) 0, 51200, true);
        sphere.aa((short) 0, (short) this.contentId);
        toolkit.xa(1.0F);
        toolkit.ZA(16777215, 1.0F, 1.0F, (float) lightX, (float) lightY, (float) lightZ);
        local84 = this.textureSize * 1024 / (sphere.RA() - sphere.V());
        if (this.colour != 0) {
            local84 = local84 * 13 / 16;
        }
        @Pc(190) int[] savedProjection = toolkit.Y();
        toolkit.DA(this.textureSize / 2, this.textureSize / 2, local84, local84);
        toolkit.setCamera(toolkit.createMatrix());
        @Pc(209) Matrix matrix = toolkit.createMatrix();
        matrix.applyTranslation(0, 0, toolkit.i() - sphere.HA());
        sphere.renderOrtho(matrix, null, 1024, 1);
        @Pc(231) int shadingSize = this.textureSize * 13 / 16;
        @Pc(238) int shadingOffset = (this.textureSize - shadingSize) / 2;
        shadingSprite.render(shadingOffset, shadingOffset, shadingSize, shadingSize, 0, this.colour | 0xFF000000, 1);
        this.sprite = toolkit.createSprite(0, 0, this.textureSize, this.textureSize, true);
        toolkit.ya();
        toolkit.aa(0, 0, this.textureSize, this.textureSize, 0, 0);
        maskSprite.render(0, 0, this.textureSize, this.textureSize, 1, 0, 0);
        this.sprite.copyAlpha(0, 0, 3);
        toolkit.DA(savedProjection[0], savedProjection[1], savedProjection[2], savedProjection[3]);
        toolkit.KA(savedClipping[0], savedClipping[1], savedClipping[2], savedClipping[3]);
    }

    @OriginalMember(owner = "client!ks", name = "a", descriptor = "(Lclient!ha;Lclient!ks;)V")
    public void renderModelSprite(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) SkyBoxSphere light) {
        @Pc(6) Mesh mesh = Mesh.load(this.contentId, modelJs5);
        if (mesh == null) {
            return;
        }
        toolkit.K(savedClipping);
        toolkit.KA(0, 0, this.textureSize, this.textureSize);
        toolkit.ya();
        toolkit.aa(0, 0, this.textureSize, this.textureSize, 0, 0);
        @Pc(34) int lightX = 0;
        @Pc(36) int lightY = 0;
        @Pc(38) int lightZ = 256;
        if (light != null) {
            if (light.infinite) {
                lightX = -light.x;
                lightY = -light.y;
                lightZ = -light.z;
            } else {
                lightX = this.x - light.x;
                lightY = this.y - light.y;
                lightZ = this.z - light.z;
            }
        }
        @Pc(83) int local83;
        @Pc(87) int local87;
        @Pc(91) int local91;
        @Pc(101) int local101;
        if (this.pitch != 0) {
            local83 = -this.pitch & 0x3FFF;
            local87 = Trig1.SIN[local83];
            local91 = Trig1.COS[local83];
            local101 = lightY * local91 - lightZ * local87 >> 14;
            lightZ = lightY * local87 + lightZ * local91 >> 14;
            lightY = local101;
        }
        if (this.yaw != 0) {
            local83 = -this.yaw & 0x3FFF;
            local87 = Trig1.SIN[local83];
            local91 = Trig1.COS[local83];
            local101 = lightZ * local87 + lightX * local91 >> 14;
            lightZ = lightZ * local91 - lightX * local87 >> 14;
            lightX = local101;
        }
        toolkit.xa(1.0F);
        toolkit.ZA(this.colour, 1.0F, 1.0F, (float) lightX, (float) lightY, (float) lightZ);
        mesh.rotate(this.rotateZ & 0x3FFF, this.rotateX & 0x3FFF, this.rotateY & 0x3FFF);
        @Pc(190) Model model = toolkit.createModel(mesh, 2048, 0, 64, 768);
        local87 = model.RA() - model.V();
        local91 = model.EA() - model.fa();
        local101 = local87 > local91 ? local87 : local91;
        @Pc(216) int zoom = this.textureSize * 1024 / local101;
        @Pc(219) int[] savedProjection = toolkit.Y();
        toolkit.DA(this.textureSize / 2, this.textureSize / 2, zoom, zoom);
        toolkit.setCamera(toolkit.createMatrix());
        @Pc(238) Matrix matrix = toolkit.scratchMatrix();
        matrix.applyTranslation(0, 0, toolkit.i() - model.HA());
        model.renderOrtho(matrix, null, toolkit.i(), 1);
        this.sprite = toolkit.createSprite(0, 0, this.textureSize, this.textureSize, true);
        this.sprite.copyAlpha(0, 0, 3);
        toolkit.DA(savedProjection[0], savedProjection[1], savedProjection[2], savedProjection[3]);
        toolkit.KA(savedClipping[0], savedClipping[1], savedClipping[2], savedClipping[3]);
    }

    @OriginalMember(owner = "client!ks", name = "a", descriptor = "(Lclient!ha;IIIIIIIIII)V")
    public void render(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int viewportX, @OriginalArg(2) int viewportY, @OriginalArg(3) int viewportWidth, @OriginalArg(4) int viewportHeight, @OriginalArg(5) int cameraPitch, @OriginalArg(6) int cameraYaw, @OriginalArg(7) int offsetX, @OriginalArg(8) int offsetY, @OriginalArg(9) int offsetZ, @OriginalArg(10) int alpha) {
        if (this.sprite == null) {
            return;
        }
        @Pc(6) int[] projected = new int[3];
        @Pc(14) int relativeX = -(this.x - offsetX << 16);
        @Pc(21) int relativeY = this.y - offsetY << 15;
        @Pc(29) int relativeZ = -(this.z - offsetZ << 16);
        @Pc(32) Matrix camera = toolkit.camera();
        camera.project(0, 0, 0, projected);
        @Pc(44) int viewX = relativeX + projected[0];
        @Pc(50) int viewY = relativeY + projected[1];
        @Pc(56) int viewZ = relativeZ + projected[2];
        toolkit.H(viewX, viewY, viewZ, projected);
        if (projected[2] < 0) {
            return;
        }
        @Pc(75) int screenX = projected[0] - this.screenSize / 2;
        @Pc(84) int screenY = projected[1] - this.screenSize / 2;
        if (screenY < viewportHeight && screenY + this.screenSize > 0 && screenX < viewportWidth && screenX + this.screenSize > 0) {
            this.sprite.render(screenX, screenY, this.screenSize, this.screenSize, 0, alpha << 24 | 0xFFFFFF, 1);
        }
    }

    @OriginalMember(owner = "client!ks", name = "a", descriptor = "(IIII)Z")
    public boolean update(@OriginalArg(0) int offsetX, @OriginalArg(1) int offsetY, @OriginalArg(2) int offsetZ, @OriginalArg(3) int viewportHeight) {
        @Pc(7) int dx;
        @Pc(12) int dy;
        @Pc(17) int dz;
        if (this.infinite) {
            this.distance = 1073741823;
            dx = this.x;
            dy = this.y;
            dz = this.z;
        } else {
            dx = this.x - offsetX;
            dy = this.y - offsetY;
            dz = this.z - offsetZ;
            this.distance = (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (this.distance == 0) {
                this.distance = 1;
            }
            dx = (dx << 8) / this.distance;
            dy = (dy << 8) / this.distance;
            dz = (dz << 8) / this.distance;
        }
        @Pc(90) int length = (int) (Math.sqrt(dx * dx + dy * dy + dz * dz) * 256.0D);
        if (length > 128) {
            dx = (dx << 16) / length;
            dy = (dy << 16) / length;
            dz = (dz << 16) / length;
            this.screenSize = this.size * viewportHeight / (this.infinite ? 1024 : this.distance);
        } else {
            this.screenSize = 0;
        }
        if (this.screenSize < 8) {
            this.sprite = null;
            return false;
        }
        @Pc(143) int nextSize = IntMath.nextPow2(this.screenSize);
        if (nextSize > viewportHeight) {
            nextSize = method5587(viewportHeight);
        }
        if (nextSize > 512) {
            nextSize = 512;
        }
        if (nextSize != this.textureSize) {
            this.textureSize = nextSize;
        }
        this.pitch = (int) (Math.asin((float) dy / 256.0F) * 2607.5945876176133D) & 0x3FFF;
        this.yaw = (int) (Math.atan2(dx, -dz) * 2607.5945876176133D) & 0x3FFF;
        this.sprite = null;
        return true;
    }

    @OriginalMember(owner = "client!ks", name = "b", descriptor = "(Lclient!ha;Lclient!ks;)Z")
    public boolean buildSprite(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) SkyBoxSphere light) {
        if (this.sprite == null) {
            if (this.renderType == 0) {
                if (textureSource.textureAvailable(this.contentId)) {
                    @Pc(23) int[] pixels = textureSource.argbOutput(0.7F, this.contentId, this.textureSize, this.textureSize);
                    this.sprite = toolkit.createSprite(this.textureSize, this.textureSize, this.textureSize, pixels);
                }
            } else if (this.renderType == 2) {
                this.renderModelSprite(toolkit, light);
            } else if (this.renderType == 1) {
                this.renderSphereSprite(toolkit, light);
            }
        }
        return this.sprite != null;
    }
}

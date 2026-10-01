package com.jagex.graphics;

import com.jagex.core.io.Packet;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.js5.js5;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!dv")
public final class Mesh {

    @OriginalMember(owner = "client!dp", name = "a", descriptor = "(IIILclient!sb;)Lclient!dv;")
    public static Mesh load(@OriginalArg(0) int id, @OriginalArg(3) js5 js5) {
        @Pc(9) byte[] data = js5.getfile(0, id);
        return data == null ? null : new Mesh(data);
    }

    @OriginalMember(owner = "client!dv", name = "k", descriptor = "[I")
    public int[] vertexZ;

    @OriginalMember(owner = "client!dv", name = "m", descriptor = "[S")
    public short[] faceOriginModels;

    @OriginalMember(owner = "client!dv", name = "S", descriptor = "[I")
    public int[] vertexY;

    @OriginalMember(owner = "client!dv", name = "R", descriptor = "[S")
    public short[] texSpaceDefB;

    @OriginalMember(owner = "client!dv", name = "Y", descriptor = "[S")
    public short[] faceColour;

    @OriginalMember(owner = "client!dv", name = "O", descriptor = "[S")
    public short[] originModels;

    @OriginalMember(owner = "client!dv", name = "u", descriptor = "[S")
    public short[] faceB;

    @OriginalMember(owner = "client!dv", name = "t", descriptor = "[I")
    public int[] texOffsetZ;

    @OriginalMember(owner = "client!dv", name = "N", descriptor = "[B")
    public byte[] texDirection;

    @OriginalMember(owner = "client!dv", name = "I", descriptor = "[B")
    public byte[] texMappingType;

    @OriginalMember(owner = "client!dv", name = "Q", descriptor = "[S")
    public short[] faceTexture;

    @OriginalMember(owner = "client!dv", name = "e", descriptor = "[I")
    public int[] texSpaceScaleY;

    @OriginalMember(owner = "client!dv", name = "f", descriptor = "[S")
    public short[] faceC;

    @OriginalMember(owner = "client!dv", name = "r", descriptor = "[Lclient!rv;")
    public ModelParticleEmitter[] emitters;

    @OriginalMember(owner = "client!dv", name = "M", descriptor = "[I")
    public int[] texSpaceScaleZ;

    @OriginalMember(owner = "client!dv", name = "i", descriptor = "[B")
    public byte[] shadingType;

    @OriginalMember(owner = "client!dv", name = "W", descriptor = "[I")
    public int[] vertexLabel;

    @OriginalMember(owner = "client!dv", name = "Z", descriptor = "[S")
    public short[] faceA;

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "[I")
    public int[] faceLabel;

    @OriginalMember(owner = "client!dv", name = "l", descriptor = "[I")
    public int[] texSpaceScaleX;

    @OriginalMember(owner = "client!dv", name = "y", descriptor = "[S")
    public short[] texSpaceDefC;

    @OriginalMember(owner = "client!dv", name = "E", descriptor = "[B")
    public byte[] facePriority;

    @OriginalMember(owner = "client!dv", name = "X", descriptor = "[B")
    public byte[] texRotation;

    @OriginalMember(owner = "client!dv", name = "J", descriptor = "[B")
    public byte[] faceTexSpace;

    @OriginalMember(owner = "client!dv", name = "c", descriptor = "[S")
    public short[] texSpaceDefA;

    @OriginalMember(owner = "client!dv", name = "g", descriptor = "[I")
    public int[] texOffsetY;

    @OriginalMember(owner = "client!dv", name = "b", descriptor = "[I")
    public int[] vertexX;

    @OriginalMember(owner = "client!dv", name = "w", descriptor = "[I")
    public int[] texOffsetX;

    @OriginalMember(owner = "client!dv", name = "p", descriptor = "[Lclient!mn;")
    public ModelParticleEffector[] effectors;

    @OriginalMember(owner = "client!dv", name = "d", descriptor = "[Lclient!aq;")
    public MeshBillboard[] billboards;

    @OriginalMember(owner = "client!dv", name = "B", descriptor = "[B")
    public byte[] faceAlpha;

    @OriginalMember(owner = "client!dv", name = "n", descriptor = "I")
    public int faceCount = 0;

    @OriginalMember(owner = "client!dv", name = "z", descriptor = "I")
    public int vertexCount = 0;

    @OriginalMember(owner = "client!dv", name = "D", descriptor = "I")
    public int maxVertex = 0;

    @OriginalMember(owner = "client!dv", name = "L", descriptor = "I")
    public int texSpaceCount = 0;

    @OriginalMember(owner = "client!dv", name = "j", descriptor = "B")
    public byte globalPriority = 0;

    @OriginalMember(owner = "client!dv", name = "h", descriptor = "I")
    public int version = 12;

    @OriginalMember(owner = "client!dv", name = "<init>", descriptor = "()V")
    public Mesh() {
    }

    @OriginalMember(owner = "client!dv", name = "<init>", descriptor = "([B)V")
    public Mesh(@OriginalArg(0) byte[] data) {
        if (data[data.length - 1] == -1 && data[data.length - 2] == -1) {
            this.decodeNew(data);
        } else {
            this.decodeOld(data);
        }
    }

    @OriginalMember(owner = "client!dv", name = "<init>", descriptor = "(III)V")
    public Mesh(@OriginalArg(0) int vertexCount, @OriginalArg(1) int faceCount, @OriginalArg(2) int texCount) {
        this.faceLabel = new int[faceCount];
        this.faceTexture = new short[faceCount];
        this.faceColour = new short[faceCount];
        this.faceB = new short[faceCount];
        this.facePriority = new byte[faceCount];
        this.shadingType = new byte[faceCount];
        this.vertexY = new int[vertexCount];
        this.faceA = new short[faceCount];
        if (texCount > 0) {
            this.texSpaceDefB = new short[texCount];
            this.texSpaceScaleX = new int[texCount];
            this.texDirection = new byte[texCount];
            this.texOffsetX = new int[texCount];
            this.texSpaceDefA = new short[texCount];
            this.texSpaceDefC = new short[texCount];
            this.texMappingType = new byte[texCount];
            this.texSpaceScaleZ = new int[texCount];
            this.texRotation = new byte[texCount];
            this.texOffsetY = new int[texCount];
            this.texOffsetZ = new int[texCount];
            this.texSpaceScaleY = new int[texCount];
        }
        this.vertexX = new int[vertexCount];
        this.vertexLabel = new int[vertexCount];
        this.vertexZ = new int[vertexCount];
        this.faceTexSpace = new byte[faceCount];
        this.faceAlpha = new byte[faceCount];
        this.faceC = new short[faceCount];
    }

    @OriginalMember(owner = "client!dv", name = "<init>", descriptor = "([Lclient!dv;I)V")
    public Mesh(@OriginalArg(0) Mesh[] meshes, @OriginalArg(1) int meshCount) {
        this.texSpaceCount = 0;
        this.vertexCount = 0;
        this.faceCount = 0;
        @Pc(30) int emitterCount = 0;
        @Pc(32) int effectorCount = 0;
        @Pc(34) int billboardCount = 0;
        @Pc(36) boolean hasShadingTypes = false;
        @Pc(38) boolean hasPriorities = false;
        @Pc(40) boolean hasTexSpaces = false;
        @Pc(42) boolean hasAlpha = false;
        @Pc(44) boolean hasFaceTextures = false;
        @Pc(46) boolean hasFaceGroups = false;
        this.globalPriority = -1;

        for (@Pc(51) int i = 0; i < meshCount; i++) {
            @Pc(56) Mesh mesh = meshes[i];

            if (mesh != null) {
                this.faceCount += mesh.faceCount;
                this.texSpaceCount += mesh.texSpaceCount;
                this.vertexCount += mesh.vertexCount;

                if (mesh.effectors != null) {
                    effectorCount += mesh.effectors.length;
                }

                if (mesh.billboards != null) {
                    billboardCount += mesh.billboards.length;
                }
                if (mesh.emitters != null) {
                    emitterCount += mesh.emitters.length;
                }

                hasShadingTypes |= mesh.shadingType != null;
                hasTexSpaces |= mesh.faceAlpha != null;

                if (mesh.facePriority != null) {
                    hasPriorities = true;
                } else {
                    if (this.globalPriority == -1) {
                        this.globalPriority = mesh.globalPriority;
                    }

                    if (mesh.globalPriority != this.globalPriority) {
                        hasPriorities = true;
                    }
                }

                hasAlpha |= mesh.faceTexSpace != null;
                hasFaceGroups |= mesh.faceLabel != null;
                hasFaceTextures |= mesh.faceTexture != null;
            }
        }

        this.vertexX = new int[this.vertexCount];
        this.faceA = new short[this.faceCount];
        if (hasShadingTypes) {
            this.shadingType = new byte[this.faceCount];
        }
        if (effectorCount > 0) {
            this.effectors = new ModelParticleEffector[effectorCount];
        }
        this.originModels = new short[this.vertexCount];
        this.vertexY = new int[this.vertexCount];
        this.faceOriginModels = new short[this.faceCount];
        if (hasFaceGroups) {
            this.faceLabel = new int[this.faceCount];
        }
        this.faceColour = new short[this.faceCount];
        this.faceB = new short[this.faceCount];
        this.faceC = new short[this.faceCount];

        if (this.texSpaceCount > 0) {
            this.texSpaceScaleX = new int[this.texSpaceCount];
            this.texRotation = new byte[this.texSpaceCount];
            this.texDirection = new byte[this.texSpaceCount];
            this.texSpaceDefC = new short[this.texSpaceCount];
            this.texSpaceDefA = new short[this.texSpaceCount];
            this.texSpaceScaleZ = new int[this.texSpaceCount];
            this.texSpaceDefB = new short[this.texSpaceCount];
            this.texOffsetY = new int[this.texSpaceCount];
            this.texMappingType = new byte[this.texSpaceCount];
            this.texOffsetX = new int[this.texSpaceCount];
            this.texSpaceScaleY = new int[this.texSpaceCount];
            this.texOffsetZ = new int[this.texSpaceCount];
        }

        if (billboardCount > 0) {
            this.billboards = new MeshBillboard[billboardCount];
        }

        if (hasAlpha) {
            this.faceTexSpace = new byte[this.faceCount];
        }

        this.vertexLabel = new int[this.vertexCount];
        this.vertexZ = new int[this.vertexCount];

        if (hasFaceTextures) {
            this.faceTexture = new short[this.faceCount];
        }

        if (hasTexSpaces) {
            this.faceAlpha = new byte[this.faceCount];
        }

        if (emitterCount > 0) {
            this.emitters = new ModelParticleEmitter[emitterCount];
        }

        if (hasPriorities) {
            this.facePriority = new byte[this.faceCount];
        }

        effectorCount = 0;
        this.texSpaceCount = 0;
        emitterCount = 0;
        billboardCount = 0;
        this.faceCount = 0;
        this.vertexCount = 0;

        for (@Pc(401) int i = 0; i < meshCount; i++) {
            @Pc(407) short s = (short) (0x1 << i);
            @Pc(411) Mesh mesh = meshes[i];
            if (mesh != null) {

                if (mesh.billboards != null) {
                    for (@Pc(420) int j = 0; j < mesh.billboards.length; j++) {
                        @Pc(426) MeshBillboard billboard = mesh.billboards[j];
                        this.billboards[billboardCount++] = billboard.copy(billboard.face + this.faceCount);
                    }
                }

                for (@Pc(420) int j = 0; j < mesh.faceCount; j++) {
                    if (hasShadingTypes && mesh.shadingType != null) {
                        this.shadingType[this.faceCount] = mesh.shadingType[j];
                    }

                    if (hasPriorities) {
                        if (mesh.facePriority == null) {
                            this.facePriority[this.faceCount] = mesh.globalPriority;
                        } else {
                            this.facePriority[this.faceCount] = mesh.facePriority[j];
                        }
                    }

                    if (hasTexSpaces && mesh.faceAlpha != null) {
                        this.faceAlpha[this.faceCount] = mesh.faceAlpha[j];
                    }

                    if (hasFaceTextures) {
                        if (mesh.faceTexture == null) {
                            this.faceTexture[this.faceCount] = -1;
                        } else {
                            this.faceTexture[this.faceCount] = mesh.faceTexture[j];
                        }
                    }

                    if (hasFaceGroups) {
                        if (mesh.faceLabel == null) {
                            this.faceLabel[this.faceCount] = -1;
                        } else {
                            this.faceLabel[this.faceCount] = mesh.faceLabel[j];
                        }
                    }

                    this.faceA[this.faceCount] = (short) this.addVertex(mesh, mesh.faceA[j], s);
                    this.faceB[this.faceCount] = (short) this.addVertex(mesh, mesh.faceB[j], s);
                    this.faceC[this.faceCount] = (short) this.addVertex(mesh, mesh.faceC[j], s);
                    this.faceOriginModels[this.faceCount] = s;
                    this.faceColour[this.faceCount] = mesh.faceColour[j];
                    this.faceCount++;
                }

                if (mesh.emitters != null) {
                    for (@Pc(636) int j = 0; j < mesh.emitters.length; j++) {
                        @Pc(648) int a = this.addVertex(mesh, mesh.emitters[j].vertexA, s);
                        @Pc(659) int b = this.addVertex(mesh, mesh.emitters[j].vertexB, s);
                        @Pc(670) int c = this.addVertex(mesh, mesh.emitters[j].vertexC, s);
                        this.emitters[emitterCount] = mesh.emitters[j].copy(a, b, c);
                        emitterCount++;
                    }
                }

                if (mesh.effectors != null) {
                    for (@Pc(636) int j = 0; j < mesh.effectors.length; j++) {
                        @Pc(648) int v = this.addVertex(mesh, mesh.effectors[j].vertex, s);
                        this.effectors[effectorCount] = mesh.effectors[j].copy(v);
                        effectorCount++;
                    }
                }
            }
        }

        this.maxVertex = this.vertexCount;

        @Pc(747) int textSpaceCount = 0;
        for (@Pc(749) int meshIndex = 0; meshIndex < meshCount; meshIndex++) {
            @Pc(755) short modelFlag = (short) (0x1 << meshIndex);
            @Pc(759) Mesh mesh = meshes[meshIndex];

            if (mesh != null) {
                for (@Pc(648) int i = 0; i < mesh.faceCount; i++) {
                    if (hasAlpha) {
                        this.faceTexSpace[textSpaceCount++] = (byte) (mesh.faceTexSpace == null || mesh.faceTexSpace[i] == -1 ? -1 : mesh.faceTexSpace[i] + this.texSpaceCount);
                    }
                }

                for (@Pc(659) int i = 0; i < mesh.texSpaceCount; i++) {
                    @Pc(815) byte type = this.texMappingType[this.texSpaceCount] = mesh.texMappingType[i];
                    if (type == 0) {
                        this.texSpaceDefA[this.texSpaceCount] = (short) this.addVertex(mesh, mesh.texSpaceDefA[i], modelFlag);
                        this.texSpaceDefB[this.texSpaceCount] = (short) this.addVertex(mesh, mesh.texSpaceDefB[i], modelFlag);
                        this.texSpaceDefC[this.texSpaceCount] = (short) this.addVertex(mesh, mesh.texSpaceDefC[i], modelFlag);
                    }

                    if (type >= 1 && type <= 3) {
                        this.texSpaceDefA[this.texSpaceCount] = mesh.texSpaceDefA[i];
                        this.texSpaceDefB[this.texSpaceCount] = mesh.texSpaceDefB[i];
                        this.texSpaceDefC[this.texSpaceCount] = mesh.texSpaceDefC[i];
                        this.texSpaceScaleX[this.texSpaceCount] = mesh.texSpaceScaleX[i];
                        this.texSpaceScaleY[this.texSpaceCount] = mesh.texSpaceScaleY[i];
                        this.texSpaceScaleZ[this.texSpaceCount] = mesh.texSpaceScaleZ[i];
                        this.texRotation[this.texSpaceCount] = mesh.texRotation[i];
                        this.texDirection[this.texSpaceCount] = mesh.texDirection[i];
                        this.texOffsetX[this.texSpaceCount] = mesh.texOffsetX[i];
                    }

                    if (type == 2) {
                        this.texOffsetY[this.texSpaceCount] = mesh.texOffsetY[i];
                        this.texOffsetZ[this.texSpaceCount] = mesh.texOffsetZ[i];
                    }

                    this.texSpaceCount++;
                }
            }
        }
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(IIII)I")
    public int addVertex(@OriginalArg(1) int z, @OriginalArg(2) int y, @OriginalArg(3) int x) {
        for (@Pc(15) int i = 0; i < this.vertexCount; i++) {
            if (x == this.vertexX[i] && y == this.vertexY[i] && z == this.vertexZ[i]) {
                return i;
            }
        }

        this.vertexX[this.vertexCount] = x;
        this.vertexY[this.vertexCount] = y;
        this.vertexZ[this.vertexCount] = z;
        this.maxVertex = this.vertexCount + 1;
        return this.vertexCount++;
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(ZIIISSBBB)I")
    public int addFace(@OriginalArg(1) int a, @OriginalArg(2) int c, @OriginalArg(3) int b, @OriginalArg(4) short colour, @OriginalArg(5) short texture, @OriginalArg(6) byte space, @OriginalArg(7) byte shading, @OriginalArg(8) byte alpha) {
        this.faceA[this.faceCount] = (short) a;
        this.faceB[this.faceCount] = (short) b;
        this.faceC[this.faceCount] = (short) c;
        this.shadingType[this.faceCount] = shading;
        this.faceTexSpace[this.faceCount] = alpha;
        this.faceColour[this.faceCount] = colour;
        this.faceAlpha[this.faceCount] = space;
        this.faceTexture[this.faceCount] = texture;
        return this.faceCount++;
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(II)V")
    public void upscale() {
        for (@Pc(1) int i = 0; i < this.vertexCount; i++) {
            this.vertexX[i] <<= 0x2;
            this.vertexY[i] <<= 0x2;
            this.vertexZ[i] <<= 0x2;
        }

        if (this.texSpaceCount <= 0 || this.texSpaceScaleX == null) {
            return;
        }

        for (@Pc(51) int i = 0; i < this.texSpaceScaleX.length; i++) {
            this.texSpaceScaleX[i] <<= 0x2;
            this.texSpaceScaleY[i] <<= 0x2;

            if (this.texMappingType[i] != 1) {
                this.texSpaceScaleZ[i] <<= 0x2;
            }
        }
    }

    @OriginalMember(owner = "client!dv", name = "b", descriptor = "(IIII)V")
    public void translate(@OriginalArg(0) int tx, @OriginalArg(1) int ty, @OriginalArg(3) int tz) {
        for (@Pc(13) int i = 0; i < this.vertexCount; i++) {
            this.vertexX[i] += tx;
            this.vertexY[i] += ty;
            this.vertexZ[i] += tz;
        }
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(IZ)[[I")
    public int[][] getVertexLabels(@OriginalArg(1) boolean flag) {
        @Pc(8) int[] counts = new int[256];
        @Pc(10) int max = 0;
        @Pc(19) int count = flag ? this.vertexCount : this.maxVertex;

        for (@Pc(21) int i = 0; i < count; i++) {
            @Pc(30) int group = this.vertexLabel[i];
            if (group >= 0) {
                if (group > max) {
                    max = group;
                }

                @Pc(50) int previousCount = counts[group]++;
            }
        }

        @Pc(66) int[][] groups = new int[max + 1][];
        for (@Pc(70) int i = 0; i <= max; i++) {
            groups[i] = new int[counts[i]];
            counts[i] = 0;
        }

        for (@Pc(95) int i = 0; i < count; i++) {
            @Pc(104) int group = this.vertexLabel[i];

            if (group >= 0) {
                groups[group][counts[group]++] = i;
            }
        }

        return groups;
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "([BZ)V")
    public void decodeNew(@OriginalArg(0) byte[] data) {
        @Pc(8) Packet packet1 = new Packet(data);
        @Pc(13) Packet packet2 = new Packet(data);
        @Pc(18) Packet packet3 = new Packet(data);
        @Pc(23) Packet packet4 = new Packet(data);
        @Pc(28) Packet packet5 = new Packet(data);
        @Pc(33) Packet packet6 = new Packet(data);
        @Pc(38) Packet packet7 = new Packet(data);

        packet1.pos = data.length - 23;

        this.vertexCount = packet1.g2();
        this.faceCount = packet1.g2();
        this.texSpaceCount = packet1.g1();

        @Pc(63) int globalFlags = packet1.g1();
        @Pc(75) boolean hasFlatShading = (globalFlags & 0x1) == 1;
        @Pc(85) boolean hasParticleEffects = (globalFlags & 0x2) == 2;
        @Pc(97) boolean hasBillboards = (globalFlags & 0x4) == 4;
        @Pc(109) boolean hasVersion = (globalFlags & 0x8) == 8;

        if (hasVersion) {
            packet1.pos -= 7;
            this.version = packet1.g1();
            packet1.pos += 6;
        }

        @Pc(134) int priorityFlag = packet1.g1();
        @Pc(138) int faceAlphaFlag = packet1.g1();
        @Pc(142) int faceGroupFlag = packet1.g1();
        @Pc(146) int faceTextureFlag = packet1.g1();
        @Pc(150) int vertexLabelFlag = packet1.g1();
        @Pc(154) int vertexLengthX = packet1.g2();
        @Pc(158) int vertexLengthY = packet1.g2();
        @Pc(162) int vertexLengthZ = packet1.g2();
        @Pc(166) int faceDataSize = packet1.g2();
        @Pc(170) int texSpaceSize = packet1.g2();
        @Pc(172) int planarMappingCount = 0;
        @Pc(174) int complexMappingCount = 0;
        @Pc(176) int cubeMappingCount = 0;

        if (this.texSpaceCount > 0) {
            this.texMappingType = new byte[this.texSpaceCount];

            packet1.pos = 0;

            for (@Pc(192) int i = 0; i < this.texSpaceCount; i++) {
                @Pc(202) byte type = this.texMappingType[i] = packet1.g1b();

                if (type == 0) {
                    planarMappingCount++;
                }

                if (type == 2) {
                    cubeMappingCount++;
                }

                if (type >= 1 && type <= 3) {
                    complexMappingCount++;
                }
            }
        }

        @Pc(192) int ptr = this.texSpaceCount;

        @Pc(242) int vertexFlagsPtr = ptr;
        ptr += this.vertexCount;

        @Pc(249) int smoothingPtr = ptr;
        if (hasFlatShading) {
            ptr += this.faceCount;
        }

        @Pc(258) int faceTypePtr = ptr;
        ptr += this.faceCount;

        @Pc(265) int facePriPtr = ptr;
        if (priorityFlag == 255) {
            ptr += this.faceCount;
        }

        @Pc(277) int faceGroupPtr = ptr;
        if (faceGroupFlag == 1) {
            ptr += this.faceCount;
        }

        @Pc(287) int vertexLabelPtr = ptr;
        if (vertexLabelFlag == 1) {
            ptr += this.vertexCount;
        }

        @Pc(299) int faceAlphaPtr = ptr;
        if (faceAlphaFlag == 1) {
            ptr += this.faceCount;
        }

        @Pc(309) int faceDataSizePtr = ptr;
        ptr += faceDataSize;

        @Pc(315) int faceTextureFlagPtr = ptr;
        if (faceTextureFlag == 1) {
            ptr += this.faceCount * 2;
        }

        @Pc(329) int faceTextureSpacePtr = ptr;
        ptr += texSpaceSize;

        @Pc(335) int faceColourPtr = ptr;
        ptr += this.faceCount * 2;

        @Pc(344) int vertexXPtr = ptr;
        ptr += vertexLengthX;

        @Pc(350) int vertexYPtr = ptr;
        ptr += vertexLengthY;

        @Pc(356) int vertexZPtr = ptr;
        ptr += vertexLengthZ;

        @Pc(362) int planarMappingPtr = ptr;
        ptr += planarMappingCount * 6;

        @Pc(370) int complexMappingPtr = ptr;
        ptr += complexMappingCount * 6;

        @Pc(378) byte texSpaceScaleSize = 6;
        if (this.version == 14) {
            texSpaceScaleSize = 7;
        } else if (this.version >= 15) {
            texSpaceScaleSize = 9;
        }

        @Pc(395) int texSpaceScalePtr = ptr;
        ptr += complexMappingCount * texSpaceScaleSize;

        @Pc(403) int texSpaceRotationPtr = ptr;
        ptr += complexMappingCount;

        @Pc(409) int texSpaceOrientationPtr = ptr;
        ptr += complexMappingCount;

        @Pc(415) int texSpaceOffsetPtr = ptr;
        ptr += complexMappingCount + (cubeMappingCount * 2);

        this.faceColour = new short[this.faceCount];
        packet1.pos = vertexFlagsPtr;

        if (hasFlatShading) {
            this.shadingType = new byte[this.faceCount];
        }

        if (this.texSpaceCount > 0) {
            this.texSpaceDefA = new short[this.texSpaceCount];

            if (complexMappingCount > 0) {
                this.texSpaceScaleZ = new int[complexMappingCount];
                this.texDirection = new byte[complexMappingCount];
                this.texSpaceScaleX = new int[complexMappingCount];
                this.texRotation = new byte[complexMappingCount];
                this.texOffsetX = new int[complexMappingCount];
                this.texSpaceScaleY = new int[complexMappingCount];
            }

            if (cubeMappingCount > 0) {
                this.texOffsetY = new int[cubeMappingCount];
                this.texOffsetZ = new int[cubeMappingCount];
            }

            this.texSpaceDefB = new short[this.texSpaceCount];
            this.texSpaceDefC = new short[this.texSpaceCount];
        }

        this.vertexZ = new int[this.vertexCount];
        this.faceC = new short[this.faceCount];
        this.vertexY = new int[this.vertexCount];
        this.faceA = new short[this.faceCount];

        if (faceTextureFlag == 1) {
            this.faceTexture = new short[this.faceCount];
        }

        if (priorityFlag == 255) {
            this.facePriority = new byte[this.faceCount];
        } else {
            this.globalPriority = (byte) priorityFlag;
        }

        if (faceAlphaFlag == 1) {
            this.faceAlpha = new byte[this.faceCount];
        }

        if (vertexLabelFlag == 1) {
            this.vertexLabel = new int[this.vertexCount];
        }

        this.vertexX = new int[this.vertexCount];

        if (faceTextureFlag == 1 && this.texSpaceCount > 0) {
            this.faceTexSpace = new byte[this.faceCount];
        }

        if (faceGroupFlag == 1) {
            this.faceLabel = new int[this.faceCount];
        }

        this.faceB = new short[this.faceCount];

        packet2.pos = vertexXPtr;
        packet3.pos = vertexYPtr;
        packet4.pos = vertexZPtr;
        packet5.pos = vertexLabelPtr;

        @Pc(627) int pvx = 0;
        @Pc(629) int pvy = 0;
        @Pc(631) int pvz = 0;

        for (@Pc(633) int i = 0; i < this.vertexCount; i++) {
            @Pc(638) int vertexData = packet1.g1();

            @Pc(640) int x = 0;
            if ((vertexData & 0x1) != 0) {
                x = packet2.gsmarts();
            }

            @Pc(653) int y = 0;
            if ((vertexData & 0x2) != 0) {
                y = packet3.gsmarts();
            }

            @Pc(666) int z = 0;
            if ((vertexData & 0x4) != 0) {
                z = packet4.gsmarts();
            }

            this.vertexX[i] = x + pvx;
            this.vertexY[i] = pvy + y;
            this.vertexZ[i] = z + pvz;

            pvy = this.vertexY[i];
            pvz = this.vertexZ[i];
            pvx = this.vertexX[i];

            if (vertexLabelFlag == 1) {
                this.vertexLabel[i] = packet5.g1();
            }
        }

        packet1.pos = faceColourPtr;
        packet2.pos = smoothingPtr;
        packet3.pos = facePriPtr;
        packet4.pos = faceAlphaPtr;
        packet5.pos = faceGroupPtr;
        packet6.pos = faceTextureFlagPtr;
        packet7.pos = faceTextureSpacePtr;

        for (@Pc(638) int i = 0; i < this.faceCount; i++) {
            this.faceColour[i] = (short) packet1.g2();

            if (hasFlatShading) {
                this.shadingType[i] = packet2.g1b();
            }

            if (priorityFlag == 255) {
                this.facePriority[i] = packet3.g1b();
            }

            if (faceAlphaFlag == 1) {
                this.faceAlpha[i] = packet4.g1b();
            }

            if (faceGroupFlag == 1) {
                this.faceLabel[i] = packet5.g1();
            }

            if (faceTextureFlag == 1) {
                this.faceTexture[i] = (short) (packet6.g2() - 1);
            }

            if (this.faceTexSpace != null) {
                if (this.faceTexture[i] == -1) {
                    this.faceTexSpace[i] = -1;
                } else {
                    this.faceTexSpace[i] = (byte) (packet7.g1() - 1);
                }
            }
        }

        this.maxVertex = -1;
        packet1.pos = faceDataSizePtr;
        packet2.pos = faceTypePtr;

        @Pc(869) short faceA = 0;
        @Pc(871) short faceB = 0;
        @Pc(873) short faceC = 0;
        @Pc(875) short facePriority = 0;

        for (@Pc(877) int i = 0; i < this.faceCount; i++) {
            @Pc(882) int type = packet2.g1();

            if (type == 1) {
                faceA = (short) (packet1.gsmarts() + facePriority);
                facePriority = faceA;

                faceB = (short) (packet1.gsmarts() + facePriority);
                facePriority = faceB;

                faceC = (short) (packet1.gsmarts() + facePriority);
                facePriority = faceC;

                this.faceA[i] = faceA;
                this.faceB[i] = faceB;
                this.faceC[i] = faceC;

                if (this.maxVertex < faceA) {
                    this.maxVertex = faceA;
                }

                if (this.maxVertex < faceB) {
                    this.maxVertex = faceB;
                }

                if (this.maxVertex < faceC) {
                    this.maxVertex = faceC;
                }
            }

            if (type == 2) {
                faceB = faceC;
                faceC = (short) (packet1.gsmarts() + facePriority);

                this.faceA[i] = faceA;
                facePriority = faceC;
                this.faceB[i] = faceB;
                this.faceC[i] = faceC;

                if (this.maxVertex < faceC) {
                    this.maxVertex = faceC;
                }
            }

            if (type == 3) {
                faceA = faceC;
                faceC = (short) (packet1.gsmarts() + facePriority);

                this.faceA[i] = faceA;
                facePriority = faceC;
                this.faceB[i] = faceB;
                this.faceC[i] = faceC;

                if (this.maxVertex < faceC) {
                    this.maxVertex = faceC;
                }
            }

            if (type == 4) {
                @Pc(1055) short faceABefore = faceA;
                faceA = faceB;
                faceC = (short) (facePriority + packet1.gsmarts());
                faceB = faceABefore;

                this.faceA[i] = faceA;
                facePriority = faceC;
                this.faceB[i] = faceABefore;
                this.faceC[i] = faceC;

                if (this.maxVertex < faceC) {
                    this.maxVertex = faceC;
                }
            }
        }

        packet1.pos = planarMappingPtr;

        this.maxVertex++;

        packet2.pos = complexMappingPtr;
        packet3.pos = texSpaceScalePtr;
        packet4.pos = texSpaceRotationPtr;
        packet5.pos = texSpaceOrientationPtr;
        packet6.pos = texSpaceOffsetPtr;

        for (@Pc(882) int i = 0; i < this.texSpaceCount; i++) {
            @Pc(1142) int type = this.texMappingType[i] & 0xFF;

            if (type == 0) {
                this.texSpaceDefA[i] = (short) packet1.g2();
                this.texSpaceDefB[i] = (short) packet1.g2();
                this.texSpaceDefC[i] = (short) packet1.g2();
            }

            if (type == 1) {
                this.texSpaceDefA[i] = (short) packet2.g2();
                this.texSpaceDefB[i] = (short) packet2.g2();
                this.texSpaceDefC[i] = (short) packet2.g2();

                if (this.version >= 15) {
                    this.texSpaceScaleX[i] = packet3.g3();
                    this.texSpaceScaleY[i] = packet3.g3();
                    this.texSpaceScaleZ[i] = packet3.g3();
                } else {
                    this.texSpaceScaleX[i] = packet3.g2();
                    if (this.version >= 14) {
                        this.texSpaceScaleY[i] = packet3.g3();
                    } else {
                        this.texSpaceScaleY[i] = packet3.g2();
                    }
                    this.texSpaceScaleZ[i] = packet3.g2();
                }

                this.texRotation[i] = packet4.g1b();
                this.texDirection[i] = packet5.g1b();
                this.texOffsetX[i] = packet6.g1b();
            }
            if (type == 2) {
                this.texSpaceDefA[i] = (short) packet2.g2();
                this.texSpaceDefB[i] = (short) packet2.g2();
                this.texSpaceDefC[i] = (short) packet2.g2();

                if (this.version >= 15) {
                    this.texSpaceScaleX[i] = packet3.g3();
                    this.texSpaceScaleY[i] = packet3.g3();
                    this.texSpaceScaleZ[i] = packet3.g3();
                } else {
                    this.texSpaceScaleX[i] = packet3.g2();
                    if (this.version >= 14) {
                        this.texSpaceScaleY[i] = packet3.g3();
                    } else {
                        this.texSpaceScaleY[i] = packet3.g2();
                    }
                    this.texSpaceScaleZ[i] = packet3.g2();
                }

                this.texRotation[i] = packet4.g1b();
                this.texDirection[i] = packet5.g1b();
                this.texOffsetX[i] = packet6.g1b();
                this.texOffsetY[i] = packet6.g1b();
                this.texOffsetZ[i] = packet6.g1b();
            }

            if (type == 3) {
                this.texSpaceDefA[i] = (short) packet2.g2();
                this.texSpaceDefB[i] = (short) packet2.g2();
                this.texSpaceDefC[i] = (short) packet2.g2();

                if (this.version >= 15) {
                    this.texSpaceScaleX[i] = packet3.g3();
                    this.texSpaceScaleY[i] = packet3.g3();
                    this.texSpaceScaleZ[i] = packet3.g3();
                } else {
                    this.texSpaceScaleX[i] = packet3.g2();
                    if (this.version >= 14) {
                        this.texSpaceScaleY[i] = packet3.g3();
                    } else {
                        this.texSpaceScaleY[i] = packet3.g2();
                    }
                    this.texSpaceScaleZ[i] = packet3.g2();
                }

                this.texRotation[i] = packet4.g1b();
                this.texDirection[i] = packet5.g1b();
                this.texOffsetX[i] = packet6.g1b();
            }
        }

        packet1.pos = ptr;

        if (hasParticleEffects) {
            @Pc(1142) int emitterCount = packet1.g1();
            if (emitterCount > 0) {
                this.emitters = new ModelParticleEmitter[emitterCount];

                for (@Pc(1556) int i = 0; i < emitterCount; i++) {
                    @Pc(1561) int type = packet1.g2();
                    @Pc(1565) int face = packet1.g2();
                    @Pc(1573) byte priority;

                    if (priorityFlag == 255) {
                        priority = this.facePriority[face];
                    } else {
                        priority = (byte) priorityFlag;
                    }

                    this.emitters[i] = new ModelParticleEmitter(type, this.faceA[face], this.faceB[face], this.faceC[face], priority);
                }
            }

            @Pc(1556) int effectorCount = packet1.g1();
            if (effectorCount > 0) {
                this.effectors = new ModelParticleEffector[effectorCount];

                for (@Pc(1561) int i = 0; i < effectorCount; i++) {
                    @Pc(1565) int type = packet1.g2();
                    @Pc(1627) int vertex = packet1.g2();
                    this.effectors[i] = new ModelParticleEffector(type, vertex);
                }
            }
        }

        if (hasBillboards) {
            @Pc(1142) int billboardCount = packet1.g1();
            if (billboardCount <= 0) {
                return;
            }

            this.billboards = new MeshBillboard[billboardCount];
            for (@Pc(1556) int i = 0; i < billboardCount; i++) {
                @Pc(1561) int type = packet1.g2();
                @Pc(1565) int face = packet1.g2();
                @Pc(1627) int group = packet1.g1();
                @Pc(1676) byte priority = packet1.g1b();
                this.billboards[i] = new MeshBillboard(type, face, group, priority);
            }
        }
    }

    @OriginalMember(owner = "client!dv", name = "c", descriptor = "(IIII)V")
    public void rotate(@OriginalArg(0) int z, @OriginalArg(1) int x, @OriginalArg(3) int y) {
        if (z != 0) {
            @Pc(5) int sinZ = Trig1.SIN[z];
            @Pc(9) int cosZ = Trig1.COS[z];

            for (@Pc(11) int i = 0; i < this.vertexCount; i++) {
                @Pc(28) int t = ((cosZ * this.vertexX[i]) + (sinZ * this.vertexY[i])) >> 14;
                this.vertexY[i] = ((cosZ * this.vertexY[i]) - (this.vertexX[i] * sinZ)) >> 14;
                this.vertexX[i] = t;
            }
        }

        if (x != 0) {
            @Pc(5) int sinX = Trig1.SIN[x];
            @Pc(9) int cosX = Trig1.COS[x];

            for (@Pc(11) int i = 0; i < this.vertexCount; i++) {
                @Pc(28) int t = ((cosX * this.vertexY[i]) - (sinX * this.vertexZ[i])) >> 14;
                this.vertexZ[i] = ((sinX * this.vertexY[i]) + (this.vertexZ[i] * cosX)) >> 14;
                this.vertexY[i] = t;
            }
        }

        if (y != 0) {
            @Pc(5) int sinY = Trig1.SIN[y];
            @Pc(9) int cosY = Trig1.COS[y];

            for (@Pc(11) int i = 0; i < this.vertexCount; i++) {
                @Pc(28) int t = ((sinY * this.vertexZ[i]) + (cosY * this.vertexX[i])) >> 14;
                this.vertexZ[i] = ((this.vertexZ[i] * cosY) - (sinY * this.vertexX[i])) >> 14;
                this.vertexX[i] = t;
            }
        }
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(BBBSSSSSSB)B")
    public byte addSphericalSpace() {
        if (this.texSpaceCount >= 255) {
            throw new IllegalStateException();
        }

        this.texMappingType[this.texSpaceCount] = 3;
        this.texSpaceDefA[this.texSpaceCount] = 0;
        this.texSpaceDefB[this.texSpaceCount] = 32767;
        this.texSpaceDefC[this.texSpaceCount] = 0;
        this.texSpaceScaleX[this.texSpaceCount] = 1024;
        this.texSpaceScaleY[this.texSpaceCount] = 1024;
        this.texSpaceScaleZ[this.texSpaceCount] = 1024;
        this.texRotation[this.texSpaceCount] = 0;
        this.texDirection[this.texSpaceCount] = 0;
        this.texOffsetX[this.texSpaceCount] = 0;
        return (byte) this.texSpaceCount++;
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(SSI)V")
    public void retexture(@OriginalArg(0) short src, @OriginalArg(1) short dest) {
        if (this.faceTexture == null) {
            return;
        }

        for (@Pc(10) int i = 0; i < this.faceCount; i++) {
            if (this.faceTexture[i] == src) {
                this.faceTexture[i] = dest;
            }
        }
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(B)[[I")
    public int[][] getBillboardGroups() {
        @Pc(8) int[] counts = new int[256];

        @Pc(10) int max = 0;
        for (@Pc(12) int i = 0; i < this.billboards.length; i++) {
            @Pc(22) int group = this.billboards[i].group;

            if (group >= 0) {
                if (group > max) {
                    max = group;
                }

                @Pc(39) int previousCount = counts[group]++;
            }
        }

        @Pc(69) int[][] groups = new int[max + 1][];
        for (@Pc(71) int i = 0; i <= max; i++) {
            groups[i] = new int[counts[i]];
            counts[i] = 0;
        }

        for (@Pc(96) int i = 0; i < this.billboards.length; i++) {
            @Pc(106) int group = this.billboards[i].group;

            if (group >= 0) {
                groups[group][counts[group]++] = i;
            }
        }

        return groups;
    }

    @OriginalMember(owner = "client!dv", name = "b", descriptor = "(SSI)V")
    public void recolour(@OriginalArg(0) short src, @OriginalArg(1) short dest) {
        for (@Pc(5) int i = 0; i < this.faceCount; i++) {
            if (src == this.faceColour[i]) {
                this.faceColour[i] = dest;
            }
        }
    }

    @OriginalMember(owner = "client!dv", name = "b", descriptor = "(B)[[I")
    public int[][] getFaceLabels() {
        @Pc(16) int[] counts = new int[256];

        @Pc(18) int max = 0;
        for (@Pc(20) int i = 0; i < this.faceCount; i++) {
            @Pc(29) int group = this.faceLabel[i];

            if (group >= 0) {
                @Pc(38) int previousCount = counts[group]++;

                if (group > max) {
                    max = group;
                }
            }
        }

        @Pc(62) int[][] groups = new int[max + 1][];
        for (@Pc(66) int i = 0; i <= max; i++) {
            groups[i] = new int[counts[i]];
            counts[i] = 0;
        }

        for (@Pc(87) int i = 0; i < this.faceCount; i++) {
            @Pc(96) int group = this.faceLabel[i];

            if (group >= 0) {
                groups[group][counts[group]++] = i;
            }
        }

        return groups;
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(I[B)V")
    public void decodeOld(@OriginalArg(1) byte[] data) {
        @Pc(5) boolean hasFlatShading = false;
        @Pc(7) boolean hasTextures = false;
        @Pc(12) Packet packet1 = new Packet(data);
        @Pc(17) Packet packet2 = new Packet(data);
        @Pc(22) Packet packet3 = new Packet(data);
        @Pc(27) Packet packet4 = new Packet(data);
        @Pc(32) Packet packet5 = new Packet(data);

        packet1.pos = data.length - 18;

        this.vertexCount = packet1.g2();
        this.faceCount = packet1.g2();
        this.texSpaceCount = packet1.g1();

        @Pc(57) int customShadingFlag = packet1.g1();
        @Pc(61) int priorityFlag = packet1.g1();
        @Pc(65) int alphaFlag = packet1.g1();
        @Pc(69) int faceLabelFlag = packet1.g1();
        @Pc(73) int vertexLabelFlag = packet1.g1();
        @Pc(77) int xLen = packet1.g2();
        @Pc(81) int yLen = packet1.g2();
        @Pc(85) int zLen = packet1.g2();
        @Pc(89) int faceLen = packet1.g2();
        @Pc(98) int ptr = 0;

        int vTypePtr = ptr;
        ptr += this.vertexCount;

        @Pc(100) int fTypePtr = ptr;
        ptr += this.faceCount;

        @Pc(107) int priPtr = ptr;
        if (priorityFlag == 255) {
            ptr += this.faceCount;
        }

        @Pc(117) int fGroupPtr = ptr;
        if (faceLabelFlag == 1) {
            ptr += this.faceCount;
        }

        @Pc(129) int texPtr = ptr;
        if (customShadingFlag == 1) {
            ptr += this.faceCount;
        }

        @Pc(139) int vGroupPtr = ptr;
        if (vertexLabelFlag == 1) {
            ptr += this.vertexCount;
        }

        @Pc(151) int alphaPtr = ptr;
        if (alphaFlag == 1) {
            ptr += this.faceCount;
        }

        @Pc(163) int facePtr = ptr;
        ptr += faceLen;

        @Pc(169) int faceCountPtr = ptr;
        ptr += this.faceCount * 2;

        @Pc(178) int textFacePtr = ptr;
        ptr += this.texSpaceCount * 6;

        @Pc(187) int vxPtr = ptr;
        ptr += xLen;

        @Pc(198) int vyPtr = ptr;
        ptr += yLen;

        int vzPtr = ptr;
        ptr += zLen;

        if (priorityFlag == 255) {
            this.facePriority = new byte[this.faceCount];
        } else {
            this.globalPriority = (byte) priorityFlag;
        }

        if (faceLabelFlag == 1) {
            this.faceLabel = new int[this.faceCount];
        }

        this.faceColour = new short[this.faceCount];
        this.vertexZ = new int[this.vertexCount];

        if (vertexLabelFlag == 1) {
            this.vertexLabel = new int[this.vertexCount];
        }

        this.faceB = new short[this.faceCount];
        this.vertexY = new int[this.vertexCount];
        this.vertexX = new int[this.vertexCount];

        packet1.pos = vTypePtr;

        if (this.texSpaceCount > 0) {
            this.texSpaceDefB = new short[this.texSpaceCount];
            this.texSpaceDefC = new short[this.texSpaceCount];
            this.texMappingType = new byte[this.texSpaceCount];
            this.texSpaceDefA = new short[this.texSpaceCount];
        }

        if (customShadingFlag == 1) {
            this.shadingType = new byte[this.faceCount];
            this.faceTexture = new short[this.faceCount];
            this.faceTexSpace = new byte[this.faceCount];
        }

        if (alphaFlag == 1) {
            this.faceAlpha = new byte[this.faceCount];
        }

        this.faceA = new short[this.faceCount];
        this.faceC = new short[this.faceCount];

        packet2.pos = vxPtr;
        packet3.pos = vyPtr;
        packet4.pos = vzPtr;
        packet5.pos = vGroupPtr;

        @Pc(354) int pvx = 0;
        @Pc(356) int pvy = 0;
        @Pc(358) int pvz = 0;

        for (@Pc(360) int i = 0; i < this.vertexCount; i++) {
            @Pc(365) int vertexData = packet1.g1();

            @Pc(367) int x = 0;
            if ((vertexData & 0x1) != 0) {
                x = packet2.gsmarts();
            }

            @Pc(377) int y = 0;
            if ((vertexData & 0x2) != 0) {
                y = packet3.gsmarts();
            }

            @Pc(390) int z = 0;
            if ((vertexData & 0x4) != 0) {
                z = packet4.gsmarts();
            }

            this.vertexX[i] = pvx + x;
            this.vertexY[i] = pvy + y;
            this.vertexZ[i] = pvz + z;

            pvx = this.vertexX[i];
            pvy = this.vertexY[i];
            pvz = this.vertexZ[i];

            if (vertexLabelFlag == 1) {
                this.vertexLabel[i] = packet5.g1();
            }
        }

        packet1.pos = faceCountPtr;
        packet2.pos = texPtr;
        packet3.pos = priPtr;
        packet4.pos = alphaPtr;
        packet5.pos = fGroupPtr;

        for (@Pc(365) int i = 0; i < this.faceCount; i++) {
            this.faceColour[i] = (short) packet1.g2();

            if (customShadingFlag == 1) {
                @Pc(367) int type = packet2.g1();

                if ((type & 0x1) == 1) {
                    this.shadingType[i] = 1;
                    hasFlatShading = true;
                } else {
                    this.shadingType[i] = 0;
                }

                if ((type & 0x2) == 2) {
                    this.faceTexSpace[i] = (byte) (type >> 2);
                    this.faceTexture[i] = this.faceColour[i];
                    this.faceColour[i] = 127;

                    if (this.faceTexture[i] != -1) {
                        hasTextures = true;
                    }
                } else {
                    this.faceTexSpace[i] = -1;
                    this.faceTexture[i] = -1;
                }
            }

            if (priorityFlag == 255) {
                this.facePriority[i] = packet3.g1b();
            }

            if (alphaFlag == 1) {
                this.faceAlpha[i] = packet4.g1b();
            }

            if (faceLabelFlag == 1) {
                this.faceLabel[i] = packet5.g1();
            }
        }

        this.maxVertex = -1;
        packet1.pos = facePtr;
        packet2.pos = fTypePtr;

        @Pc(609) short fa = 0;
        @Pc(611) short fb = 0;
        @Pc(613) short fc = 0;
        @Pc(615) short pf = 0;

        for (@Pc(617) int i = 0; i < this.faceCount; i++) {
            @Pc(622) int type = packet2.g1();

            if (type == 1) {
                fa = (short) (pf + packet1.gsmarts());
                fb = (short) (packet1.gsmarts() + fa);
                fc = (short) (packet1.gsmarts() + fb);
                pf = fc;

                this.faceA[i] = fa;
                this.faceB[i] = fb;
                this.faceC[i] = fc;

                if (this.maxVertex < fa) {
                    this.maxVertex = fa;
                }

                if (this.maxVertex < fb) {
                    this.maxVertex = fb;
                }

                if (this.maxVertex < fc) {
                    this.maxVertex = fc;
                }
            }

            if (type == 2) {
                fb = fc;
                fc = (short) (packet1.gsmarts() + pf);
                pf = fc;

                this.faceA[i] = fa;
                this.faceB[i] = fb;
                this.faceC[i] = fc;

                if (this.maxVertex < fc) {
                    this.maxVertex = fc;
                }
            }

            if (type == 3) {
                fa = fc;
                fc = (short) (pf + packet1.gsmarts());
                this.faceA[i] = fa;
                pf = fc;
                this.faceB[i] = fb;
                this.faceC[i] = fc;

                if (this.maxVertex < fc) {
                    this.maxVertex = fc;
                }
            }

            if (type == 4) {
                @Pc(785) short faBefore = fa;
                fa = fb;
                fc = (short) (packet1.gsmarts() + pf);
                fb = faBefore;
                this.faceA[i] = fa;
                pf = fc;
                this.faceB[i] = faBefore;
                this.faceC[i] = fc;

                if (this.maxVertex < fc) {
                    this.maxVertex = fc;
                }
            }
        }

        packet1.pos = textFacePtr;
        this.maxVertex++;

        for (@Pc(622) int i = 0; i < this.texSpaceCount; i++) {
            this.texMappingType[i] = 0;
            this.texSpaceDefA[i] = (short) packet1.g2();
            this.texSpaceDefB[i] = (short) packet1.g2();
            this.texSpaceDefC[i] = (short) packet1.g2();
        }

        if (this.faceTexSpace != null) {
            @Pc(884) boolean hasTexSpaces = false;

            for (@Pc(886) int i = 0; i < this.faceCount; i++) {
                @Pc(894) int space = this.faceTexSpace[i] & 0xFF;
                if (space != 255) {
                    if ((this.faceA[i] == (this.texSpaceDefA[space] & 0xFFFF)) && (this.faceB[i] == (this.texSpaceDefB[space] & 0xFFFF)) && (this.faceC[i] == (this.texSpaceDefC[space] & 0xFFFF))) {
                        this.faceTexSpace[i] = -1;
                    } else {
                        hasTexSpaces = true;
                    }
                }
            }

            if (!hasTexSpaces) {
                this.faceTexSpace = null;
            }
        }

        if (!hasFlatShading) {
            this.shadingType = null;
        }

        if (!hasTextures) {
            this.faceTexture = null;
        }
    }

    @OriginalMember(owner = "client!dv", name = "a", descriptor = "(Lclient!dv;IIS)I")
    public int addVertex(@OriginalArg(0) Mesh mesh, @OriginalArg(2) int v, @OriginalArg(3) short modelFlag) {
        @Pc(8) int x = mesh.vertexX[v];
        @Pc(13) int y = mesh.vertexY[v];
        @Pc(18) int z = mesh.vertexZ[v];

        for (@Pc(20) int i = 0; i < this.vertexCount; i++) {
            if (this.vertexX[i] == x && this.vertexY[i] == y && z == this.vertexZ[i]) {
                this.originModels[i] |= modelFlag;
                return i;
            }
        }

        this.vertexX[this.vertexCount] = x;
        this.vertexY[this.vertexCount] = y;
        this.vertexZ[this.vertexCount] = z;

        this.originModels[this.vertexCount] = modelFlag;
        this.vertexLabel[this.vertexCount] = mesh.vertexLabel == null ? -1 : mesh.vertexLabel[v];

        return this.vertexCount++;
    }
}

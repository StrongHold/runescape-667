import com.jagex.core.algorithms.Quicksort;
import com.jagex.game.runetek6.config.billboardtype.BillboardType;
import com.jagex.game.runetek6.config.billboardtype.BillboardTypeList;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.MeshBillboard;
import com.jagex.graphics.Model;
import com.jagex.graphics.PickingCylinder;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.math.ColourUtils;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!rs")
public final class JavaModel extends Model {

    @OriginalMember(owner = "client!rs", name = "Fb", descriptor = "Lclient!lb;")
    public Rasterizer rasterizer;

    @OriginalMember(owner = "client!rs", name = "Db", descriptor = "[I")
    public int[] screenX;

    @OriginalMember(owner = "client!rs", name = "v", descriptor = "[I")
    public int[] cameraX;

    @OriginalMember(owner = "client!rs", name = "T", descriptor = "[I")
    public int[] screenY;

    @OriginalMember(owner = "client!rs", name = "X", descriptor = "[I")
    public int[] vertexZ;

    @OriginalMember(owner = "client!rs", name = "A", descriptor = "[S")
    public short[] faceIndices;

    @OriginalMember(owner = "client!rs", name = "gb", descriptor = "[I")
    public int[] cameraZ;

    @OriginalMember(owner = "client!rs", name = "Ab", descriptor = "[[I")
    public int[][] vertexLabels;

    @OriginalMember(owner = "client!rs", name = "S", descriptor = "[[I")
    public int[][] faceLabels;

    @OriginalMember(owner = "client!rs", name = "pb", descriptor = "Lclient!wf;")
    public JavaThreadResource copyThreadResource;

    @OriginalMember(owner = "client!rs", name = "Mb", descriptor = "I")
    public int contrast;

    @OriginalMember(owner = "client!rs", name = "I", descriptor = "[I")
    public int[] cameraY;

    @OriginalMember(owner = "client!rs", name = "Jb", descriptor = "[I")
    public int[] boundsCornerZ;

    @OriginalMember(owner = "client!rs", name = "R", descriptor = "S")
    public short maxZ;

    @OriginalMember(owner = "client!rs", name = "lb", descriptor = "[S")
    public short[] faceB;

    @OriginalMember(owner = "client!rs", name = "x", descriptor = "S")
    public short minZ;

    @OriginalMember(owner = "client!rs", name = "O", descriptor = "[Lclient!rs;")
    public JavaModel[] copyBuffers;

    @OriginalMember(owner = "client!rs", name = "Cb", descriptor = "[Lclient!um;")
    public VertexNormal[] vertexNormalsOffset;

    @OriginalMember(owner = "client!rs", name = "C", descriptor = "[B")
    public byte[] shadingType;

    @OriginalMember(owner = "client!rs", name = "qb", descriptor = "[I")
    public int[] boundsCornerX;

    @OriginalMember(owner = "client!rs", name = "U", descriptor = "[I")
    public int[] clippedX;

    @OriginalMember(owner = "client!rs", name = "ab", descriptor = "[I")
    public int[] faceColourB;

    @OriginalMember(owner = "client!rs", name = "q", descriptor = "[S")
    public short[] faceColour;

    @OriginalMember(owner = "client!rs", name = "eb", descriptor = "Lclient!wf;")
    public JavaThreadResource threadResource;

    @OriginalMember(owner = "client!rs", name = "V", descriptor = "I")
    public int pivotZ;

    @OriginalMember(owner = "client!rs", name = "nb", descriptor = "[I")
    public int[] boundsCornerY;

    @OriginalMember(owner = "client!rs", name = "z", descriptor = "I")
    public int functionMask;

    @OriginalMember(owner = "client!rs", name = "Lb", descriptor = "[S")
    public short[] originModels;

    @OriginalMember(owner = "client!rs", name = "xb", descriptor = "[I")
    public int[] vertexX;

    @OriginalMember(owner = "client!rs", name = "o", descriptor = "[I")
    public int[] otherMergeStamps;

    @OriginalMember(owner = "client!rs", name = "t", descriptor = "[Lclient!rs;")
    public JavaModel[] copyTargets;

    @OriginalMember(owner = "client!rs", name = "p", descriptor = "S")
    public short maxY;

    @OriginalMember(owner = "client!rs", name = "tb", descriptor = "I")
    public int pivotY;

    @OriginalMember(owner = "client!rs", name = "bb", descriptor = "[I")
    public int[] faceColourA;

    @OriginalMember(owner = "client!rs", name = "M", descriptor = "S")
    public short sphereRadius;

    @OriginalMember(owner = "client!rs", name = "Hb", descriptor = "[I")
    public int[] faceBillboard;

    @OriginalMember(owner = "client!rs", name = "P", descriptor = "S")
    public short minX;

    @OriginalMember(owner = "client!rs", name = "cb", descriptor = "[I")
    public int[] screenZ;

    @OriginalMember(owner = "client!rs", name = "vb", descriptor = "S")
    public short minY;

    @OriginalMember(owner = "client!rs", name = "w", descriptor = "[I")
    public int[] clippedZ;

    @OriginalMember(owner = "client!rs", name = "y", descriptor = "[I")
    public int[] faceColourC;

    @OriginalMember(owner = "client!rs", name = "yb", descriptor = "I")
    public int billboardCount;

    @OriginalMember(owner = "client!rs", name = "fb", descriptor = "[[F")
    public float[][] texCoordU;

    @OriginalMember(owner = "client!rs", name = "Ob", descriptor = "[Lclient!mn;")
    public ModelParticleEffector[] effectors;

    @OriginalMember(owner = "client!rs", name = "W", descriptor = "[Lclient!mka;")
    public JavaBillboardAttributes[] billboardAttributes;

    @OriginalMember(owner = "client!rs", name = "D", descriptor = "S")
    public short maxX;

    @OriginalMember(owner = "client!rs", name = "G", descriptor = "[S")
    public short[] faceA;

    @OriginalMember(owner = "client!rs", name = "H", descriptor = "[B")
    public byte[] facePriority;

    @OriginalMember(owner = "client!rs", name = "rb", descriptor = "[I")
    public int[] worldY;

    @OriginalMember(owner = "client!rs", name = "Bb", descriptor = "[I")
    public int[] mergeStamps;

    @OriginalMember(owner = "client!rs", name = "u", descriptor = "[I")
    public int[] vertexY;

    @OriginalMember(owner = "client!rs", name = "F", descriptor = "[S")
    public short[] faceTextures;

    @OriginalMember(owner = "client!rs", name = "r", descriptor = "[Lclient!um;")
    public VertexNormal[] vertexNormals;

    @OriginalMember(owner = "client!rs", name = "s", descriptor = "[Lclient!qd;")
    public FaceNormal[] faceNormals;

    @OriginalMember(owner = "client!rs", name = "Gb", descriptor = "[[F")
    public float[][] texCoordV;

    @OriginalMember(owner = "client!rs", name = "Q", descriptor = "S")
    public short radius;

    @OriginalMember(owner = "client!rs", name = "Y", descriptor = "Lclient!eaa;")
    public JavaMatrix modelMatrix;

    @OriginalMember(owner = "client!rs", name = "L", descriptor = "I")
    public int pivotX;

    @OriginalMember(owner = "client!rs", name = "Nb", descriptor = "[I")
    public int[] clippedY;

    @OriginalMember(owner = "client!rs", name = "mb", descriptor = "[Lclient!rv;")
    public ModelParticleEmitter[] emitters;

    @OriginalMember(owner = "client!rs", name = "n", descriptor = "[[I")
    public int[][] billboardLabels;

    @OriginalMember(owner = "client!rs", name = "Qb", descriptor = "[B")
    public byte[] faceAlpha;

    @OriginalMember(owner = "client!rs", name = "Z", descriptor = "[I")
    public int[] clippedColour;

    @OriginalMember(owner = "client!rs", name = "db", descriptor = "Z")
    public boolean recoloured;

    @OriginalMember(owner = "client!rs", name = "B", descriptor = "[S")
    public short[] faceOriginModels;

    @OriginalMember(owner = "client!rs", name = "E", descriptor = "[S")
    public short[] faceC;

    @OriginalMember(owner = "client!rs", name = "Ib", descriptor = "[Lclient!mf;")
    public JavaBillboardFace[] billboardFaces;

    @OriginalMember(owner = "client!rs", name = "J", descriptor = "I")
    public int ambient;

    @OriginalMember(owner = "client!rs", name = "jb", descriptor = "Z")
    public boolean rendering = false;

    @OriginalMember(owner = "client!rs", name = "sb", descriptor = "I")
    public int faceCount = 0;

    @OriginalMember(owner = "client!rs", name = "zb", descriptor = "I")
    public int maxVertex = 0;

    @OriginalMember(owner = "client!rs", name = "N", descriptor = "I")
    public int vertexCount = 0;

    @OriginalMember(owner = "client!rs", name = "hb", descriptor = "Z")
    public boolean pivotDirty = false;

    @OriginalMember(owner = "client!rs", name = "K", descriptor = "Z")
    public boolean transparent = false;

    @OriginalMember(owner = "client!rs", name = "ib", descriptor = "I")
    public int lightingState = 0;

    @OriginalMember(owner = "client!rs", name = "Kb", descriptor = "Z")
    public boolean highPrecisionVertices = false;

    @OriginalMember(owner = "client!rs", name = "kb", descriptor = "Z")
    public boolean boundsValid = false;

    @OriginalMember(owner = "client!rs", name = "Pb", descriptor = "Z")
    public boolean movingTextures = false;

    @OriginalMember(owner = "client!rs", name = "ub", descriptor = "Lclient!iaa;")
    public final JavaToolkit toolkit;

    @OriginalMember(owner = "client!rs", name = "<init>", descriptor = "(Lclient!iaa;)V")
    public JavaModel(@OriginalArg(0) JavaToolkit toolkit) {
        this.toolkit = toolkit;
    }

    @OriginalMember(owner = "client!rs", name = "<init>", descriptor = "(Lclient!iaa;Lclient!dv;IIII)V")
    public JavaModel(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) Mesh base, @OriginalArg(2) int functionMask, @OriginalArg(3) int ambient, @OriginalArg(4) int contrast, @OriginalArg(5) int featureMask) {
        this.toolkit = toolkit;
        this.functionMask = functionMask;
        this.ambient = ambient;
        this.contrast = contrast;
        @Pc(47) TextureSource source = this.toolkit.textureSource;
        this.vertexCount = base.vertexCount;
        this.maxVertex = base.maxVertex;
        this.vertexX = base.vertexX;
        this.vertexY = base.vertexY;
        this.vertexZ = base.vertexZ;
        this.faceCount = base.faceCount;
        this.faceA = base.faceA;
        this.faceB = base.faceB;
        this.faceC = base.faceC;
        this.facePriority = base.facePriority;
        this.faceColour = base.faceColour;
        this.faceAlpha = base.faceAlpha;
        this.faceOriginModels = base.aShortArray20;
        this.shadingType = base.shadingType;
        this.emitters = base.emitters;
        this.effectors = base.effectors;
        this.originModels = base.originModels;

        @Pc(119) int[] faceIndex = new int[this.faceCount];
        @Pc(121) int i = 0;
        while (i < this.faceCount) {
            faceIndex[i] = i++;
        }

        @Pc(135) long[] faceIds = new long[this.faceCount];
        @Pc(145) boolean transparentMesh = (this.functionMask & 0x100) != 0;

        for (@Pc(147) int j = 0; j < this.faceCount; j++) {
            @Pc(152) int index = faceIndex[j];
            @Pc(154) TextureMetrics metrics = null;
            @Pc(156) int idHi = 0;
            @Pc(335) int idLo;
            @Pc(158) byte local158 = 0;
            @Pc(160) byte effectType = 0;
            @Pc(162) byte effectParam1 = 0;

            if (base.billboards != null) {
                @Pc(167) boolean hideFace = false;

                for (@Pc(169) int k = 0; k < base.billboards.length; k++) {
                    @Pc(175) MeshBillboard billboard = base.billboards[k];

                    if (index == billboard.face) {
                        @Pc(184) BillboardType type = BillboardTypeList.list(billboard.id);

                        if (type.hideFace) {
                            hideFace = true;
                        }

                        if (type.texture != -1) {
                            @Pc(199) TextureMetrics local199 = source.getMetrics(type.texture);
                            if (local199.alphaBlendMode == 2) {
                                this.transparent = true;
                            }
                        }
                    }
                }

                if (hideFace) {
                    faceIds[j] = Long.MAX_VALUE;
                }
            }

            @Pc(226) short texture = -1;
            if (base.faceTexture != null) {
                texture = base.faceTexture[index];

                if (texture != -1) {
                    metrics = source.getMetrics(texture & 0xFFFF);

                    if ((featureMask & 0x40) != 0 && metrics.disableable) {
                        texture = -1;
                    } else {
                        effectType = metrics.effectType;
                        effectParam1 = metrics.effectParam1;
                    }
                }
            }

            @Pc(287) boolean transparentFace = this.faceAlpha != null && this.faceAlpha[index] != 0 || metrics != null && metrics.alphaBlendMode == 2;
            if ((transparentMesh || transparentFace) && this.facePriority != null) {
                idHi += this.facePriority[index] << 17;
            }
            if (transparentFace) {
                idHi += 65536;
            }
            idHi += (effectType & 0xFF) << 8;
            idHi += effectParam1 & 0xFF;
            idLo = local158 + ((texture & 0xFFFF) << 16);
            @Pc(341) int local341 = idLo + (j & 0xFFFF);
            faceIds[j] = ((long) idHi << 32) + (long) local341;
            this.transparent |= transparentFace;
        }

        Quicksort.sort(faceIds, faceIndex);

        if (base.billboards != null) {
            this.billboardCount = base.billboards.length;
            this.billboardFaces = new JavaBillboardFace[this.billboardCount];
            this.billboardAttributes = new JavaBillboardAttributes[this.billboardCount];
            for (@Pc(152) int index = 0; index < base.billboards.length; index++) {
                @Pc(394) MeshBillboard billboard = base.billboards[index];
                @Pc(399) BillboardType type = BillboardTypeList.list(billboard.id);
                @Pc(335) int color = ColourUtils.HSV_TO_RGB[base.faceColour[billboard.face] & 0xFFFF] & 0xFFFFFF;
                color |= 255 - (base.faceAlpha == null ? 0 : base.faceAlpha[billboard.face] & 0xFF) << 24;
                this.billboardFaces[index] = new JavaBillboardFace(billboard.face, base.faceA[billboard.face], base.faceB[billboard.face], base.faceC[billboard.face], type.width, type.height, type.texture, type.anInt9697, type.blendMode, type.hideFace, billboard.distance);
                this.billboardAttributes[index] = new JavaBillboardAttributes(color);
            }
        }

        this.texCoordU = new float[this.faceCount][];
        this.texCoordV = new float[this.faceCount][];
        @Pc(500) TextureUniverse universe = TextureUniverse.fromMesh(base, this.faceCount, faceIndex);
        @Pc(505) JavaThreadResource local505 = this.toolkit.threadResource(Thread.currentThread());
        @Pc(508) float[] fs = local505.texCoordScratch;
        @Pc(510) boolean hasTextureCoords = false;
        @Pc(517) int local517;
        @Pc(539) short tex;
        @Pc(555) TextureMetrics metrics;
        @Pc(616) int local616;
        for (@Pc(512) int j = 0; j < this.faceCount; j++) {
            local517 = faceIndex[j];
            @Pc(522) byte texSpace;
            if (base.faceTexSpace == null) {
                texSpace = -1;
            } else {
                texSpace = base.faceTexSpace[local517];
            }

            tex = base.faceTexture == null ? -1 : base.faceTexture[local517];

            if (tex != -1 && (featureMask & 0x40) != 0) {
                metrics = source.getMetrics(tex & 0xFFFF);

                if (metrics.disableable) {
                    tex = -1;
                }
            }

            if (tex != -1) {
                hasTextureCoords = true;
                @Pc(573) float[] us = this.texCoordU[local517] = new float[3];
                @Pc(581) float[] vs = this.texCoordV[local517] = new float[3];

                if (texSpace == -1) {
                    us[0] = 0.0F;
                    vs[0] = 1.0F;
                    us[1] = 1.0F;
                    vs[1] = 1.0F;
                    us[2] = 0.0F;
                    vs[2] = 0.0F;
                } else {
                    local616 = texSpace & 0xFF;
                    @Pc(621) byte mappingType = base.texMappingType[local616];

                    if (mappingType == 0) {
                        @Pc(628) short faceA = this.faceA[local517];
                        @Pc(633) short faceB = this.faceB[local517];
                        @Pc(638) short faceC = this.faceC[local517];

                        @Pc(643) short texSpaceDefA = base.texSpaceDefA[local616];
                        @Pc(648) short texSpaceDefB = base.texSpaceDefB[local616];
                        @Pc(653) short texSpaceDefC = base.texSpaceDefC[local616];

                        @Pc(659) float vertexX = (float) this.vertexX[texSpaceDefA];
                        @Pc(665) float vertexY = (float) this.vertexY[texSpaceDefA];
                        @Pc(671) float vertexZ = (float) this.vertexZ[texSpaceDefA];

                        @Pc(679) float relativeXSB = (float) this.vertexX[texSpaceDefB] - vertexX;
                        @Pc(687) float relativeYSB = (float) this.vertexY[texSpaceDefB] - vertexY;
                        @Pc(695) float relativeZSB = (float) this.vertexZ[texSpaceDefB] - vertexZ;

                        @Pc(703) float relativeXSC = (float) this.vertexX[texSpaceDefC] - vertexX;
                        @Pc(711) float relativeYSC = (float) this.vertexY[texSpaceDefC] - vertexY;
                        @Pc(719) float relativeZSC = (float) this.vertexZ[texSpaceDefC] - vertexZ;

                        @Pc(727) float relativeXFA = (float) this.vertexX[faceA] - vertexX;
                        @Pc(735) float relativeYFA = (float) this.vertexY[faceA] - vertexY;
                        @Pc(743) float relativeZFA = (float) this.vertexZ[faceA] - vertexZ;

                        @Pc(751) float relativeXFB = (float) this.vertexX[faceB] - vertexX;
                        @Pc(759) float relativeYFB = (float) this.vertexY[faceB] - vertexY;
                        @Pc(767) float relativeZFB = (float) this.vertexZ[faceB] - vertexZ;

                        @Pc(775) float relativeXFC = (float) this.vertexX[faceC] - vertexX;
                        @Pc(783) float relativeYFC = (float) this.vertexY[faceC] - vertexY;
                        @Pc(791) float relativeZFC = (float) this.vertexZ[faceC] - vertexZ;

                        @Pc(799) float local799 = (relativeYSB * relativeZSC) - (relativeZSB * relativeYSC);
                        @Pc(807) float local807 = (relativeZSB * relativeXSC) - (relativeXSB * relativeZSC);
                        @Pc(815) float local815 = (relativeXSB * relativeYSC) - (relativeYSB * relativeXSC);

                        @Pc(823) float local823 = (relativeYSC * local815) - (relativeZSC * local807);
                        @Pc(831) float local831 = (relativeZSC * local799) - (relativeXSC * local815);
                        @Pc(839) float local839 = (relativeXSC * local807) - (relativeYSC * local799);

                        @Pc(853) float scale = 1.0F / ((local823 * relativeXSB) + (local831 * relativeYSB) + (local839 * relativeZSB));

                        us[0] = ((local823 * relativeXFA) + (local831 * relativeYFA) + (local839 * relativeZFA)) * scale;
                        us[1] = ((local823 * relativeXFB) + (local831 * relativeYFB) + (local839 * relativeZFB)) * scale;
                        us[2] = ((local823 * relativeXFC) + (local831 * relativeYFC) + (local839 * relativeZFC)) * scale;

                        @Pc(909) float local909 = relativeYSB * local815 - relativeZSB * local807;
                        @Pc(917) float local917 = relativeZSB * local799 - relativeXSB * local815;

                        @Pc(925) float local925 = relativeXSB * local807 - relativeYSB * local799;
                        @Pc(939) float local939 = 1.0F / (local909 * relativeXSC + local917 * relativeYSC + local925 * relativeZSC);

                        vs[0] = ((local909 * relativeXFA) + (local917 * relativeYFA) + (local925 * relativeZFA)) * local939;
                        vs[1] = ((local909 * relativeXFB) + (local917 * relativeYFB) + (local925 * relativeZFB)) * local939;
                        vs[2] = ((local909 * relativeXFC) + (local917 * relativeYFC) + (local925 * relativeZFC)) * local939;
                    } else {
                        @Pc(628) short faceA = this.faceA[local517];
                        @Pc(633) short faceB = this.faceB[local517];
                        @Pc(638) short faceC = this.faceC[local517];

                        @Pc(1008) int originX = universe.originX[local616];
                        @Pc(1013) int originY = universe.originY[local616];
                        @Pc(1018) int originZ = universe.originZ[local616];

                        @Pc(1023) float[] matrix = universe.matrices[local616];
                        @Pc(1028) byte direction = base.texDirection[local616];
                        @Pc(671) float offsetX = (float) base.texOffsetX[local616] / 256.0F;

                        if (mappingType == 1) {
                            @Pc(679) float scaleZ = (float) base.texSpaceScaleZ[local616] / 1024.0F;

                            TextureMapping.cylinderMap(this.vertexX[faceA], this.vertexY[faceA], this.vertexZ[faceA], originX, originY, originZ, matrix, scaleZ, offsetX, direction, fs);
                            us[0] = fs[0];
                            vs[0] = fs[1];

                            TextureMapping.cylinderMap(this.vertexX[faceB], this.vertexY[faceB], this.vertexZ[faceB], originX, originY, originZ, matrix, scaleZ, offsetX, direction, fs);
                            us[1] = fs[0];
                            vs[1] = fs[1];

                            TextureMapping.cylinderMap(this.vertexX[faceC], this.vertexY[faceC], this.vertexZ[faceC], originX, originY, originZ, matrix, scaleZ, offsetX, direction, fs);
                            us[2] = fs[0];
                            vs[2] = fs[1];

                            @Pc(687) float scale = scaleZ / 2.0F;

                            if ((direction & 0x1) != 0) {
                                if (vs[1] - vs[0] > scale) {
                                    vs[1] -= scaleZ;
                                } else if (vs[0] - vs[1] > scale) {
                                    vs[1] += scaleZ;
                                }

                                if (vs[2] - vs[0] > scale) {
                                    vs[2] -= scaleZ;
                                } else if (vs[0] - vs[2] > scale) {
                                    vs[2] += scaleZ;
                                }
                            } else {
                                if (us[1] - us[0] > scale) {
                                    us[1] -= scaleZ;
                                } else if (us[0] - us[1] > scale) {
                                    us[1] += scaleZ;
                                }

                                if (us[2] - us[0] > scale) {
                                    us[2] -= scaleZ;
                                } else if (us[0] - us[2] > scale) {
                                    us[2] += scaleZ;
                                }
                            }
                        } else if (mappingType == 2) {
                            @Pc(679) float offsetY = (float) base.texOffsetY[local616] / 256.0F;
                            @Pc(687) float offsetZ = (float) base.texOffsetZ[local616] / 256.0F;

                            @Pc(1340) int deltaX1 = this.vertexX[faceB] - this.vertexX[faceA];
                            @Pc(1350) int deltaY1 = this.vertexY[faceB] - this.vertexY[faceA];
                            @Pc(1360) int deltaZ1 = this.vertexZ[faceB] - this.vertexZ[faceA];

                            @Pc(1370) int deltaX2 = this.vertexX[faceC] - this.vertexX[faceA];
                            @Pc(1380) int deltaY2 = this.vertexY[faceC] - this.vertexY[faceA];
                            @Pc(1390) int deltaZ2 = this.vertexZ[faceC] - this.vertexZ[faceA];

                            @Pc(1398) int relativeX = (deltaY1 * deltaZ2) - (deltaY2 * deltaZ1);
                            @Pc(1406) int relativeY = (deltaZ1 * deltaX2) - (deltaZ2 * deltaX1);
                            @Pc(1414) int relativeZ = (deltaX1 * deltaY2) - (deltaX2 * deltaY1);

                            @Pc(767) float scaleX = 64.0F / (float) base.texSpaceScaleX[local616];
                            @Pc(775) float scaleY = 64.0F / (float) base.texSpaceScaleY[local616];
                            @Pc(783) float scaleZ = 64.0F / (float) base.texSpaceScaleZ[local616];

                            @Pc(791) float x = ((float) relativeX * matrix[0] + (float) relativeY * matrix[1] + (float) relativeZ * matrix[2]) / scaleX;
                            @Pc(799) float y = ((float) relativeX * matrix[3] + (float) relativeY * matrix[4] + (float) relativeZ * matrix[5]) / scaleY;
                            @Pc(807) float z = ((float) relativeX * matrix[6] + (float) relativeY * matrix[7] + (float) relativeZ * matrix[8]) / scaleZ;

                            @Pc(1513) int cubeFace = TextureMapping.cubeFace(x, y, z);

                            TextureMapping.cubeMap(this.vertexX[faceA], this.vertexY[faceA], this.vertexZ[faceA], originX, originY, originZ, matrix, offsetX, offsetY, offsetZ, cubeFace, direction, fs);
                            us[0] = fs[0];
                            vs[0] = fs[1];

                            TextureMapping.cubeMap(this.vertexX[faceB], this.vertexY[faceB], this.vertexZ[faceB], originX, originY, originZ, matrix, offsetX, offsetY, offsetZ, cubeFace, direction, fs);
                            us[1] = fs[0];
                            vs[1] = fs[1];

                            TextureMapping.cubeMap(this.vertexX[faceC], this.vertexY[faceC], this.vertexZ[faceC], originX, originY, originZ, matrix, offsetX, offsetY, offsetZ, cubeFace, direction, fs);
                            us[2] = fs[0];
                            vs[2] = fs[1];
                        } else if (mappingType == 3) {
                            TextureMapping.sphereMap(this.vertexX[faceA], this.vertexY[faceA], this.vertexZ[faceA], originX, originY, originZ, matrix, offsetX, direction, fs);
                            us[0] = fs[0];
                            vs[0] = fs[1];

                            TextureMapping.sphereMap(this.vertexX[faceB], this.vertexY[faceB], this.vertexZ[faceB], originX, originY, originZ, matrix, offsetX, direction, fs);
                            us[1] = fs[0];
                            vs[1] = fs[1];

                            TextureMapping.sphereMap(this.vertexX[faceC], this.vertexY[faceC], this.vertexZ[faceC], originX, originY, originZ, matrix, offsetX, direction, fs);
                            us[2] = fs[0];
                            vs[2] = fs[1];

                            if ((direction & 0x1) != 0) {
                                if (vs[1] - vs[0] > 0.5F) {
                                    vs[1]--;
                                } else if (vs[0] - vs[1] > 0.5F) {
                                    vs[1]++;
                                }

                                if (vs[2] - vs[0] > 0.5F) {
                                    vs[2]--;
                                } else if (vs[0] - vs[2] > 0.5F) {
                                    vs[2]++;
                                }
                            } else {
                                if (us[1] - us[0] > 0.5F) {
                                    us[1]--;
                                } else if (us[0] - us[1] > 0.5F) {
                                    us[1]++;
                                }

                                if (us[2] - us[0] > 0.5F) {
                                    us[2]--;
                                } else if (us[0] - us[2] > 0.5F) {
                                    us[2]++;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!hasTextureCoords) {
            this.texCoordU = this.texCoordV = null;
        }

        if (base.vertexLabel != null && (this.functionMask & 0x20) != 0) {
            this.vertexLabels = base.getVertexLabels(true);
        }

        if (base.faceLabel != null && (this.functionMask & 0x180) != 0) {
            this.faceLabels = base.getFaceLabels();
        }

        if (base.billboards != null && (this.functionMask & 0x400) != 0) {
            this.billboardLabels = base.getBillboardGroups();
        }

        if (base.faceTexture == null) {
            this.faceTextures = null;
        } else {
            this.faceTextures = new short[this.faceCount];

            @Pc(1963) boolean hasTextures = false;
            for (local616 = 0; local616 < this.faceCount; local616++) {
                tex = base.faceTexture[local616];

                if (tex == -1) {
                    this.faceTextures[local616] = -1;
                } else {
                    metrics = this.toolkit.textureSource.getMetrics(tex);

                    if ((featureMask & 0x40) != 0 && metrics.disableable) {
                        this.faceTextures[local616] = -1;
                    } else {
                        this.faceTextures[local616] = tex;
                        hasTextures = true;

                        if (metrics.alphaBlendMode == 2) {
                            this.transparent = true;
                        }

                        if (metrics.speedU != 0 || metrics.speedV != 0) {
                            this.movingTextures = true;
                        }
                    }
                }
            }

            if (!hasTextures) {
                this.faceTextures = null;
            }
        }

        if (this.transparent || this.billboardFaces != null) {
            this.faceIndices = new short[this.faceCount];

            for (local517 = 0; local517 < this.faceCount; local517++) {
                this.faceIndices[local517] = (short) faceIndex[local517];
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "O", descriptor = "(III)V")
    @Override
    public void O(@OriginalArg(0) int scaleX, @OriginalArg(1) int scaleY, @OriginalArg(2) int scaleZ) {
        if (scaleX != 128 && (this.functionMask & 0x1) != 1) {
            throw new IllegalStateException();
        } else if (scaleY != 128 && (this.functionMask & 0x2) != 2) {
            throw new IllegalStateException();
        } else if (scaleZ == 128 || (this.functionMask & 0x4) == 4) {
            synchronized (this) {
                for (@Pc(53) int local53 = 0; local53 < this.vertexCount; local53++) {
                    this.vertexX[local53] = this.vertexX[local53] * scaleX >> 7;
                    this.vertexY[local53] = this.vertexY[local53] * scaleY >> 7;
                    this.vertexZ[local53] = this.vertexZ[local53] * scaleZ >> 7;
                }
                this.boundsValid = false;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @OriginalMember(owner = "client!rs", name = "na", descriptor = "()I")
    @Override
    public int na() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.radius;
    }

    @OriginalMember(owner = "client!rs", name = "m", descriptor = "()V")
    public void rotate180() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.vertexCount; local5++) {
                this.vertexX[local5] = -this.vertexX[local5];
                this.vertexZ[local5] = -this.vertexZ[local5];
            }
            this.invalidateNormals();
        }
    }

    @OriginalMember(owner = "client!rs", name = "v", descriptor = "()V")
    @Override
    public void v() {
        if ((this.functionMask & 0x10) != 16) {
            throw new IllegalStateException();
        }
        synchronized (this) {
            for (@Pc(16) int local16 = 0; local16 < this.vertexCount; local16++) {
                this.vertexZ[local16] = -this.vertexZ[local16];
            }
            @Pc(38) int local38;
            if (this.vertexNormals != null) {
                for (local38 = 0; local38 < this.maxVertex; local38++) {
                    if (this.vertexNormals[local38] != null) {
                        this.vertexNormals[local38].z = -this.vertexNormals[local38].z;
                    }
                }
            }
            if (this.vertexNormalsOffset != null) {
                for (local38 = 0; local38 < this.maxVertex; local38++) {
                    if (this.vertexNormalsOffset[local38] != null) {
                        this.vertexNormalsOffset[local38].z = -this.vertexNormalsOffset[local38].z;
                    }
                }
            }
            if (this.faceNormals != null) {
                for (local38 = 0; local38 < this.faceCount; local38++) {
                    if (this.faceNormals[local38] != null) {
                        this.faceNormals[local38].z = -this.faceNormals[local38].z;
                    }
                }
            }
            @Pc(127) short[] local127 = this.faceA;
            this.faceA = this.faceC;
            this.faceC = local127;
            if (this.texCoordU != null) {
                for (@Pc(139) int local139 = 0; local139 < this.faceCount; local139++) {
                    @Pc(152) float local152;
                    if (this.texCoordU[local139] != null) {
                        local152 = this.texCoordU[local139][0];
                        this.texCoordU[local139][0] = this.texCoordU[local139][2];
                        this.texCoordU[local139][2] = local152;
                    }
                    if (this.texCoordV[local139] != null) {
                        local152 = this.texCoordV[local139][0];
                        this.texCoordV[local139][0] = this.texCoordV[local139][2];
                        this.texCoordV[local139][2] = local152;
                    }
                }
            }
            this.boundsValid = false;
            this.lightingState = 0;
        }
    }

    @OriginalMember(owner = "client!rs", name = "HA", descriptor = "()I")
    @Override
    public int HA() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.minZ;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(I)V")
    @Override
    public void a(@OriginalArg(0) int angle) {
        if ((this.functionMask & 0x5) != 5) {
            throw new IllegalStateException();
        } else if (angle == 4096) {
            this.rotate90();
        } else if (angle == 8192) {
            this.rotate180();
        } else if (angle == 12288) {
            this.rotate270();
        } else {
            @Pc(35) int local35 = Trig1.SIN[angle];
            @Pc(39) int local39 = Trig1.COS[angle];
            synchronized (this) {
                for (@Pc(45) int local45 = 0; local45 < this.vertexCount; local45++) {
                    @Pc(62) int local62 = this.vertexZ[local45] * local35 + this.vertexX[local45] * local39 >> 14;
                    this.vertexZ[local45] = this.vertexZ[local45] * local39 - this.vertexX[local45] * local35 >> 14;
                    this.vertexX[local45] = local62;
                }
                this.invalidateNormals();
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "h", descriptor = "(I)V")
    public void drawFoggedTriangleArgb(@OriginalArg(0) int face) {
        @Pc(8) short a;
        @Pc(13) short b;
        @Pc(18) short c;
        @Pc(27) int fogA;
        @Pc(46) int fogB;
        @Pc(65) int fogC;
        @Pc(81) int local81;
        @Pc(333) int local333;
        if (this.threadResource.water) {
            a = this.faceA[face];
            b = this.faceB[face];
            c = this.faceC[face];
            fogA = 0;
            fogB = 0;
            fogC = 0;
            if (this.worldY[a] > this.threadResource.waterDepth) {
                fogA = 255;
            } else if (this.worldY[a] > this.threadResource.waterHeight) {
                fogA = (this.threadResource.waterHeight - this.worldY[a]) * 255 / (this.threadResource.waterHeight - this.threadResource.waterDepth);
            }
            if (this.worldY[b] > this.threadResource.waterDepth) {
                fogB = 255;
            } else if (this.worldY[b] > this.threadResource.waterHeight) {
                fogB = (this.threadResource.waterHeight - this.worldY[b]) * 255 / (this.threadResource.waterHeight - this.threadResource.waterDepth);
            }
            if (this.worldY[c] > this.threadResource.waterDepth) {
                fogC = 255;
            } else if (this.worldY[c] > this.threadResource.waterHeight) {
                fogC = (this.threadResource.waterHeight - this.worldY[c]) * 255 / (this.threadResource.waterHeight - this.threadResource.waterDepth);
            }
            if (this.faceAlpha == null) {
                this.rasterizer.alpha = 0;
            } else {
                this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
            }
            if (this.faceTextures != null && this.faceTextures[face] != -1) {
                local81 = -16777216;
                if (this.faceAlpha != null) {
                    local81 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
                }
                if (this.faceColourC[face] == -1) {
                    local333 = local81 | this.faceColourA[face] & 0xFFFFFF;
                    this.rasterizer.renderTexturedTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local333, local333, local333, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
                } else {
                    this.rasterizer.renderTexturedTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local81 | this.faceColourA[face] & 0xFFFFFF, local81 | this.faceColourB[face] & 0xFFFFFF, local81 | this.faceColourC[face] & 0xFFFFFF, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
                }
            } else if (this.faceColourC[face] == -1) {
                this.rasterizer.renderTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]));
            } else {
                this.rasterizer.renderTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourB[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourC[face] & 0xFFFF]));
            }
            return;
        }
        a = this.faceA[face];
        b = this.faceB[face];
        c = this.faceC[face];
        fogA = this.screenZ[a] - this.threadResource.fogPlane;
        if (fogA > 255) {
            fogA = 255;
        } else if (fogA < 0) {
            fogA = 0;
        }
        fogB = this.screenZ[b] - this.threadResource.fogPlane;
        if (fogB > 255) {
            fogB = 255;
        } else if (fogB < 0) {
            fogB = 0;
        }
        fogC = this.screenZ[c] - this.threadResource.fogPlane;
        if (fogC > 255) {
            fogC = 255;
        } else if (fogC < 0) {
            fogC = 0;
        }
        local81 = fogA + fogB + fogC;
        if (local81 == 765) {
            return;
        }
        if (local81 == 0) {
            this.drawTriangleArgb(face);
            return;
        }
        if (this.faceAlpha == null) {
            this.rasterizer.alpha = 0;
        } else {
            this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
        }
        if (this.faceTextures != null && this.faceTextures[face] != -1) {
            local333 = -16777216;
            if (this.faceAlpha != null) {
                local333 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
            }
            if (this.faceColourC[face] == -1) {
                @Pc(362) int local362 = local333 | this.faceColourA[face] & 0xFFFFFF;
                this.rasterizer.renderTexturedTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local362, local362, local362, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
            } else {
                this.rasterizer.renderTexturedTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local333 | this.faceColourA[face] & 0xFFFFFF, local333 | this.faceColourB[face] & 0xFFFFFF, local333 | this.faceColourC[face] & 0xFFFFFF, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
            }
        } else if (this.faceColourC[face] == -1) {
            this.rasterizer.renderTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]));
        } else {
            this.rasterizer.renderTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourB[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourC[face] & 0xFFFF]));
        }
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(Z)V")
    public void applyLighting(@OriginalArg(0) boolean discardShading) {
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.calculateLighting(discardShading);
            }
        } else {
            this.calculateLighting(discardShading);
        }
    }

    @OriginalMember(owner = "client!rs", name = "j", descriptor = "()V")
    public void invalidateNormals() {
        this.vertexNormals = null;
        this.vertexNormalsOffset = null;
        this.faceNormals = null;
        this.boundsValid = false;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(IIII)V")
    @Override
    public void adjustColours(@OriginalArg(0) int hue, @OriginalArg(1) int saturation, @OriginalArg(2) int lightness, @OriginalArg(3) int scale) {
        if ((this.functionMask & 0x80000) != 524288) {
            throw new IllegalStateException("FMT");
        }
        @Pc(21) int local21;
        for (@Pc(13) int local13 = 0; local13 < this.faceCount; local13++) {
            local21 = this.faceColour[local13] & 0xFFFF;
            @Pc(27) int local27 = local21 >> 10 & 0x3F;
            @Pc(33) int local33 = local21 >> 7 & 0x7;
            @Pc(37) int local37 = local21 & 0x7F;
            if (hue != -1) {
                local27 += (hue - local27) * scale >> 7;
            }
            if (saturation != -1) {
                local33 += (saturation - local33) * scale >> 7;
            }
            if (lightness != -1) {
                local37 += (lightness - local37) * scale >> 7;
            }
            this.faceColour[local13] = (short) (local27 << 10 | local33 << 7 | local37);
        }
        if (this.billboardFaces != null) {
            for (local21 = 0; local21 < this.billboardCount; local21++) {
                @Pc(108) JavaBillboardFace local108 = this.billboardFaces[local21];
                @Pc(113) JavaBillboardAttributes local113 = this.billboardAttributes[local21];
                local113.anInt6225 = local113.anInt6225 & 0xFF000000 | ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.faceColour[local108.anInt6139] & 0xFFFF) & 0xFFFF] & 0xFFFFFF;
            }
        }
        if (this.lightingState == 2) {
            this.lightingState = 1;
        }
    }

    @OriginalMember(owner = "client!rs", name = "EA", descriptor = "()I")
    @Override
    public int EA() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.maxY;
    }

    @OriginalMember(owner = "client!rs", name = "I", descriptor = "(I[IIIIZI[I)V")
    @Override
    protected void I(@OriginalArg(0) int type, @OriginalArg(1) int[] labels, @OriginalArg(2) int x, @OriginalArg(3) int y, @OriginalArg(4) int z, @OriginalArg(5) boolean arg5, @OriginalArg(6) int originMask, @OriginalArg(7) int[] matrix) {
        @Pc(2) int local2 = labels.length;
        @Pc(21) int local21;
        @Pc(69) int local69;
        @Pc(91) int local91;
        @Pc(74) int local74;
        @Pc(86) int local86;
        if (type == 0) {
            x <<= 0x4;
            y <<= 0x4;
            z <<= 0x4;
            if (!this.highPrecisionVertices) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.highPrecisionVertices = true;
            }
            local21 = 0;
            this.pivotX = 0;
            this.pivotY = 0;
            this.pivotZ = 0;
            for (local69 = 0; local69 < local2; local69++) {
                local74 = labels[local69];
                if (local74 < this.vertexLabels.length) {
                    @Pc(84) int[] local84 = this.vertexLabels[local74];
                    for (local86 = 0; local86 < local84.length; local86++) {
                        local91 = local84[local86];
                        if (this.originModels == null || (originMask & this.originModels[local91]) != 0) {
                            this.pivotX += this.vertexX[local91];
                            this.pivotY += this.vertexY[local91];
                            this.pivotZ += this.vertexZ[local91];
                            local21++;
                        }
                    }
                }
            }
            if (local21 > 0) {
                this.pivotX = this.pivotX / local21 + x;
                this.pivotY = this.pivotY / local21 + y;
                this.pivotZ = this.pivotZ / local21 + z;
                this.pivotDirty = true;
            } else {
                this.pivotX = x;
                this.pivotY = y;
                this.pivotZ = z;
            }
            return;
        }
        @Pc(335) int[] local335;
        @Pc(337) int local337;
        if (type == 1) {
            if (matrix != null) {
                local21 = matrix[0] * x + matrix[1] * y + matrix[2] * z + 8192 >> 14;
                local69 = matrix[3] * x + matrix[4] * y + matrix[5] * z + 8192 >> 14;
                local74 = matrix[6] * x + matrix[7] * y + matrix[8] * z + 8192 >> 14;
                x = local21;
                y = local69;
                z = local74;
            }
            x <<= 0x4;
            y <<= 0x4;
            z <<= 0x4;
            if (!this.highPrecisionVertices) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.highPrecisionVertices = true;
            }
            for (local21 = 0; local21 < local2; local21++) {
                local69 = labels[local21];
                if (local69 < this.vertexLabels.length) {
                    local335 = this.vertexLabels[local69];
                    for (local337 = 0; local337 < local335.length; local337++) {
                        local86 = local335[local337];
                        if (this.originModels == null || (originMask & this.originModels[local86]) != 0) {
                            this.vertexX[local86] += x;
                            this.vertexY[local86] += y;
                            this.vertexZ[local86] += z;
                        }
                    }
                }
            }
            return;
        }
        @Pc(506) int local506;
        @Pc(531) int local531;
        @Pc(556) int local556;
        @Pc(595) int local595;
        @Pc(599) int local599;
        @Pc(603) int local603;
        @Pc(607) int local607;
        @Pc(615) int local615;
        @Pc(623) int local623;
        @Pc(782) int local782;
        @Pc(810) int local810;
        @Pc(815) int local815;
        @Pc(825) int local825;
        @Pc(830) int local830;
        @Pc(833) int local833;
        @Pc(836) int local836;
        @Pc(838) int local838;
        @Pc(966) int[] local966;
        @Pc(968) int local968;
        @Pc(971) int local971;
        @Pc(974) int local974;
        @Pc(976) int local976;
        @Pc(1103) int local1103;
        if (type == 2) {
            if (matrix == null) {
                for (local21 = 0; local21 < local2; local21++) {
                    local69 = labels[local21];
                    if (local69 < this.vertexLabels.length) {
                        local335 = this.vertexLabels[local69];
                        for (local337 = 0; local337 < local335.length; local337++) {
                            local86 = local335[local337];
                            if (this.originModels == null || (originMask & this.originModels[local86]) != 0) {
                                this.vertexX[local86] -= this.pivotX;
                                this.vertexY[local86] -= this.pivotY;
                                this.vertexZ[local86] -= this.pivotZ;
                                if (z != 0) {
                                    local91 = Trig1.SIN[z];
                                    local506 = Trig1.COS[z];
                                    local531 = this.vertexY[local86] * local91 + this.vertexX[local86] * local506 + 16383 >> 14;
                                    this.vertexY[local86] = this.vertexY[local86] * local506 + 16383 - this.vertexX[local86] * local91 >> 14;
                                    this.vertexX[local86] = local531;
                                }
                                if (x != 0) {
                                    local91 = Trig1.SIN[x];
                                    local506 = Trig1.COS[x];
                                    local531 = this.vertexY[local86] * local506 + 16383 - this.vertexZ[local86] * local91 >> 14;
                                    this.vertexZ[local86] = this.vertexY[local86] * local91 + this.vertexZ[local86] * local506 + 16383 >> 14;
                                    this.vertexY[local86] = local531;
                                }
                                if (y != 0) {
                                    local91 = Trig1.SIN[y];
                                    local506 = Trig1.COS[y];
                                    local531 = this.vertexZ[local86] * local91 + this.vertexX[local86] * local506 + 16383 >> 14;
                                    this.vertexZ[local86] = this.vertexZ[local86] * local506 + 16383 - this.vertexX[local86] * local91 >> 14;
                                    this.vertexX[local86] = local531;
                                }
                                this.vertexX[local86] += this.pivotX;
                                this.vertexY[local86] += this.pivotY;
                                this.vertexZ[local86] += this.pivotZ;
                            }
                        }
                    }
                }
            } else {
                if (!this.highPrecisionVertices) {
                    for (local21 = 0; local21 < this.vertexCount; local21++) {
                        this.vertexX[local21] <<= 0x4;
                        this.vertexY[local21] <<= 0x4;
                        this.vertexZ[local21] <<= 0x4;
                    }
                    this.highPrecisionVertices = true;
                }
                local21 = matrix[9] << 4;
                local69 = matrix[10] << 4;
                local74 = matrix[11] << 4;
                local337 = matrix[12] << 4;
                local86 = matrix[13] << 4;
                local91 = matrix[14] << 4;
                if (this.pivotDirty) {
                    local506 = matrix[0] * this.pivotX + matrix[3] * this.pivotY + matrix[6] * this.pivotZ + 8192 >> 14;
                    local531 = matrix[1] * this.pivotX + matrix[4] * this.pivotY + matrix[7] * this.pivotZ + 8192 >> 14;
                    local556 = matrix[2] * this.pivotX + matrix[5] * this.pivotY + matrix[8] * this.pivotZ + 8192 >> 14;
                    local506 += local337;
                    local531 += local86;
                    local556 += local91;
                    this.pivotX = local506;
                    this.pivotY = local531;
                    this.pivotZ = local556;
                    this.pivotDirty = false;
                }
                @Pc(583) int[] local583 = new int[9];
                local531 = Trig1.COS[x];
                local556 = Trig1.SIN[x];
                local595 = Trig1.COS[y];
                local599 = Trig1.SIN[y];
                local603 = Trig1.COS[z];
                local607 = Trig1.SIN[z];
                local615 = local556 * local603 + 8192 >> 14;
                local623 = local556 * local607 + 8192 >> 14;
                local583[0] = local595 * local603 + local599 * local623 + 8192 >> 14;
                local583[1] = -local595 * local607 + local599 * local615 + 8192 >> 14;
                local583[2] = local599 * local531 + 8192 >> 14;
                local583[3] = local531 * local607 + 8192 >> 14;
                local583[4] = local531 * local603 + 8192 >> 14;
                local583[5] = -local556;
                local583[6] = -local599 * local603 + local595 * local623 + 8192 >> 14;
                local583[7] = local599 * local607 + local595 * local615 + 8192 >> 14;
                local583[8] = local595 * local531 + 8192 >> 14;
                @Pc(754) int local754 = local583[0] * -this.pivotX + local583[1] * -this.pivotY + local583[2] * -this.pivotZ + 8192 >> 14;
                local782 = local583[3] * -this.pivotX + local583[4] * -this.pivotY + local583[5] * -this.pivotZ + 8192 >> 14;
                local810 = local583[6] * -this.pivotX + local583[7] * -this.pivotY + local583[8] * -this.pivotZ + 8192 >> 14;
                local815 = local754 + this.pivotX;
                @Pc(820) int local820 = local782 + this.pivotY;
                local825 = local810 + this.pivotZ;
                @Pc(828) int[] local828 = new int[9];
                for (local830 = 0; local830 < 3; local830++) {
                    for (local833 = 0; local833 < 3; local833++) {
                        local836 = 0;
                        for (local838 = 0; local838 < 3; local838++) {
                            local836 += local583[local830 * 3 + local838] * matrix[local833 * 3 + local838];
                        }
                        local828[local830 * 3 + local833] = local836 + 8192 >> 14;
                    }
                }
                local833 = local583[0] * local337 + local583[1] * local86 + local583[2] * local91 + 8192 >> 14;
                local836 = local583[3] * local337 + local583[4] * local86 + local583[5] * local91 + 8192 >> 14;
                local838 = local583[6] * local337 + local583[7] * local86 + local583[8] * local91 + 8192 >> 14;
                local833 += local815;
                local836 += local820;
                local838 += local825;
                local966 = new int[9];
                for (local968 = 0; local968 < 3; local968++) {
                    for (local971 = 0; local971 < 3; local971++) {
                        local974 = 0;
                        for (local976 = 0; local976 < 3; local976++) {
                            local974 += matrix[local968 * 3 + local976] * local828[local971 + local976 * 3];
                        }
                        local966[local968 * 3 + local971] = local974 + 8192 >> 14;
                    }
                }
                local971 = matrix[0] * local833 + matrix[1] * local836 + matrix[2] * local838 + 8192 >> 14;
                local974 = matrix[3] * local833 + matrix[4] * local836 + matrix[5] * local838 + 8192 >> 14;
                local976 = matrix[6] * local833 + matrix[7] * local836 + matrix[8] * local838 + 8192 >> 14;
                local971 += local21;
                local974 += local69;
                local976 += local74;
                for (local1103 = 0; local1103 < local2; local1103++) {
                    @Pc(1108) int local1108 = labels[local1103];
                    if (local1108 < this.vertexLabels.length) {
                        @Pc(1118) int[] local1118 = this.vertexLabels[local1108];
                        for (@Pc(1120) int local1120 = 0; local1120 < local1118.length; local1120++) {
                            @Pc(1125) int local1125 = local1118[local1120];
                            if (this.originModels == null || (originMask & this.originModels[local1125]) != 0) {
                                @Pc(1168) int local1168 = local966[0] * this.vertexX[local1125] + local966[1] * this.vertexY[local1125] + local966[2] * this.vertexZ[local1125] + 8192 >> 14;
                                @Pc(1199) int local1199 = local966[3] * this.vertexX[local1125] + local966[4] * this.vertexY[local1125] + local966[5] * this.vertexZ[local1125] + 8192 >> 14;
                                @Pc(1230) int local1230 = local966[6] * this.vertexX[local1125] + local966[7] * this.vertexY[local1125] + local966[8] * this.vertexZ[local1125] + 8192 >> 14;
                                @Pc(1234) int local1234 = local1168 + local971;
                                @Pc(1238) int local1238 = local1199 + local974;
                                @Pc(1242) int local1242 = local1230 + local976;
                                this.vertexX[local1125] = local1234;
                                this.vertexY[local1125] = local1238;
                                this.vertexZ[local1125] = local1242;
                            }
                        }
                    }
                }
            }
        } else if (type != 3) {
            @Pc(2482) JavaBillboardFace local2482;
            @Pc(2487) JavaBillboardAttributes local2487;
            if (type == 5) {
                if (this.faceLabels != null && this.faceAlpha != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = labels[local21];
                        if (local69 < this.faceLabels.length) {
                            local335 = this.faceLabels[local69];
                            for (local337 = 0; local337 < local335.length; local337++) {
                                local86 = local335[local337];
                                if (this.faceOriginModels == null || (originMask & this.faceOriginModels[local86]) != 0) {
                                    local91 = (this.faceAlpha[local86] & 0xFF) + x * 8;
                                    if (local91 < 0) {
                                        local91 = 0;
                                    } else if (local91 > 255) {
                                        local91 = 255;
                                    }
                                    this.faceAlpha[local86] = (byte) local91;
                                }
                            }
                        }
                    }
                    if (this.billboardFaces != null) {
                        for (local69 = 0; local69 < this.billboardCount; local69++) {
                            local2482 = this.billboardFaces[local69];
                            local2487 = this.billboardAttributes[local69];
                            local2487.anInt6225 = local2487.anInt6225 & 0xFFFFFF | 255 - (this.faceAlpha[local2482.anInt6139] & 0xFF) << 24;
                        }
                    }
                }
            } else if (type != 7) {
                @Pc(2723) JavaBillboardAttributes local2723;
                if (type == 8) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = labels[local21];
                            if (local69 < this.billboardLabels.length) {
                                local335 = this.billboardLabels[local69];
                                for (local337 = 0; local337 < local335.length; local337++) {
                                    local2723 = this.billboardAttributes[local335[local337]];
                                    local2723.anInt6222 += x;
                                    local2723.anInt6229 += y;
                                }
                            }
                        }
                    }
                } else if (type == 10) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = labels[local21];
                            if (local69 < this.billboardLabels.length) {
                                local335 = this.billboardLabels[local69];
                                for (local337 = 0; local337 < local335.length; local337++) {
                                    local2723 = this.billboardAttributes[local335[local337]];
                                    local2723.anInt6223 = local2723.anInt6223 * x >> 7;
                                    local2723.anInt6226 = local2723.anInt6226 * y >> 7;
                                }
                            }
                        }
                    }
                } else if (type == 9 && this.billboardLabels != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = labels[local21];
                        if (local69 < this.billboardLabels.length) {
                            local335 = this.billboardLabels[local69];
                            for (local337 = 0; local337 < local335.length; local337++) {
                                local2723 = this.billboardAttributes[local335[local337]];
                                local2723.anInt6231 = local2723.anInt6231 + x & 0x3FFF;
                            }
                        }
                    }
                }
            } else if (this.faceLabels != null) {
                for (local21 = 0; local21 < local2; local21++) {
                    local69 = labels[local21];
                    if (local69 < this.faceLabels.length) {
                        local335 = this.faceLabels[local69];
                        for (local337 = 0; local337 < local335.length; local337++) {
                            local86 = local335[local337];
                            if (this.faceOriginModels == null || (originMask & this.faceOriginModels[local86]) != 0) {
                                local91 = this.faceColour[local86] & 0xFFFF;
                                local506 = local91 >> 10 & 0x3F;
                                local531 = local91 >> 7 & 0x7;
                                local556 = local91 & 0x7F;
                                @Pc(2585) int local2585 = local506 + x & 0x3F;
                                local531 += y;
                                if (local531 < 0) {
                                    local531 = 0;
                                } else if (local531 > 7) {
                                    local531 = 7;
                                }
                                local556 += z;
                                if (local556 < 0) {
                                    local556 = 0;
                                } else if (local556 > 127) {
                                    local556 = 127;
                                }
                                this.faceColour[local86] = (short) (local2585 << 10 | local531 << 7 | local556);
                            }
                        }
                        this.recoloured = true;
                    }
                }
                if (this.billboardFaces != null) {
                    for (local69 = 0; local69 < this.billboardCount; local69++) {
                        local2482 = this.billboardFaces[local69];
                        local2487 = this.billboardAttributes[local69];
                        local2487.anInt6225 = local2487.anInt6225 & 0xFF000000 | ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.faceColour[local2482.anInt6139] & 0xFFFF) & 0xFFFF] & 0xFFFFFF;
                    }
                }
            }
        } else if (matrix == null) {
            for (local21 = 0; local21 < local2; local21++) {
                local69 = labels[local21];
                if (local69 < this.vertexLabels.length) {
                    local335 = this.vertexLabels[local69];
                    for (local337 = 0; local337 < local335.length; local337++) {
                        local86 = local335[local337];
                        if (this.originModels == null || (originMask & this.originModels[local86]) != 0) {
                            this.vertexX[local86] -= this.pivotX;
                            this.vertexY[local86] -= this.pivotY;
                            this.vertexZ[local86] -= this.pivotZ;
                            this.vertexX[local86] = this.vertexX[local86] * x / 128;
                            this.vertexY[local86] = this.vertexY[local86] * y / 128;
                            this.vertexZ[local86] = this.vertexZ[local86] * z / 128;
                            this.vertexX[local86] += this.pivotX;
                            this.vertexY[local86] += this.pivotY;
                            this.vertexZ[local86] += this.pivotZ;
                        }
                    }
                }
            }
        } else {
            if (!this.highPrecisionVertices) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.highPrecisionVertices = true;
            }
            local21 = matrix[9] << 4;
            local69 = matrix[10] << 4;
            local74 = matrix[11] << 4;
            local337 = matrix[12] << 4;
            local86 = matrix[13] << 4;
            local91 = matrix[14] << 4;
            if (this.pivotDirty) {
                local506 = matrix[0] * this.pivotX + matrix[3] * this.pivotY + matrix[6] * this.pivotZ + 8192 >> 14;
                local531 = matrix[1] * this.pivotX + matrix[4] * this.pivotY + matrix[7] * this.pivotZ + 8192 >> 14;
                local556 = matrix[2] * this.pivotX + matrix[5] * this.pivotY + matrix[8] * this.pivotZ + 8192 >> 14;
                local506 += local337;
                local531 += local86;
                local556 += local91;
                this.pivotX = local506;
                this.pivotY = local531;
                this.pivotZ = local556;
                this.pivotDirty = false;
            }
            local506 = x << 15 >> 7;
            local531 = y << 15 >> 7;
            local556 = z << 15 >> 7;
            local595 = local506 * -this.pivotX + 8192 >> 14;
            local599 = local531 * -this.pivotY + 8192 >> 14;
            local603 = local556 * -this.pivotZ + 8192 >> 14;
            local607 = local595 + this.pivotX;
            local615 = local599 + this.pivotY;
            local623 = local603 + this.pivotZ;
            @Pc(1790) int[] local1790 = new int[]{local506 * matrix[0] + 8192 >> 14, local506 * matrix[3] + 8192 >> 14, local506 * matrix[6] + 8192 >> 14, local531 * matrix[1] + 8192 >> 14, local531 * matrix[4] + 8192 >> 14, local531 * matrix[7] + 8192 >> 14, local556 * matrix[2] + 8192 >> 14, local556 * matrix[5] + 8192 >> 14, local556 * matrix[8] + 8192 >> 14};
            local782 = local506 * local337 + 8192 >> 14;
            local810 = local531 * local86 + 8192 >> 14;
            local815 = local556 * local91 + 8192 >> 14;
            @Pc(1926) int local1926 = local782 + local607;
            @Pc(1930) int local1930 = local810 + local615;
            @Pc(1934) int local1934 = local815 + local623;
            @Pc(1937) int[] local1937 = new int[9];
            @Pc(1942) int local1942;
            for (local825 = 0; local825 < 3; local825++) {
                for (local1942 = 0; local1942 < 3; local1942++) {
                    local830 = 0;
                    for (local833 = 0; local833 < 3; local833++) {
                        local830 += matrix[local825 * 3 + local833] * local1790[local1942 + local833 * 3];
                    }
                    local1937[local825 * 3 + local1942] = local830 + 8192 >> 14;
                }
            }
            local1942 = matrix[0] * local1926 + matrix[1] * local1930 + matrix[2] * local1934 + 8192 >> 14;
            local830 = matrix[3] * local1926 + matrix[4] * local1930 + matrix[5] * local1934 + 8192 >> 14;
            local833 = matrix[6] * local1926 + matrix[7] * local1930 + matrix[8] * local1934 + 8192 >> 14;
            local1942 += local21;
            local830 += local69;
            local833 += local74;
            for (local836 = 0; local836 < local2; local836++) {
                local838 = labels[local836];
                if (local838 < this.vertexLabels.length) {
                    local966 = this.vertexLabels[local838];
                    for (local968 = 0; local968 < local966.length; local968++) {
                        local971 = local966[local968];
                        if (this.originModels == null || (originMask & this.originModels[local971]) != 0) {
                            local974 = local1937[0] * this.vertexX[local971] + local1937[1] * this.vertexY[local971] + local1937[2] * this.vertexZ[local971] + 8192 >> 14;
                            local976 = local1937[3] * this.vertexX[local971] + local1937[4] * this.vertexY[local971] + local1937[5] * this.vertexZ[local971] + 8192 >> 14;
                            local1103 = local1937[6] * this.vertexX[local971] + local1937[7] * this.vertexY[local971] + local1937[8] * this.vertexZ[local971] + 8192 >> 14;
                            @Pc(2205) int local2205 = local974 + local1942;
                            @Pc(2209) int local2209 = local976 + local830;
                            @Pc(2213) int local2213 = local1103 + local833;
                            this.vertexX[local971] = local2205;
                            this.vertexY[local971] = local2209;
                            this.vertexZ[local971] = local2213;
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(IILclient!tt;ZI)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift) {
        return this.pick(x, y, matrix, quick, sizeShift, -1);
    }

    @OriginalMember(owner = "client!rs", name = "o", descriptor = "()V")
    public void rotate90() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.vertexCount; local5++) {
                @Pc(11) int local11 = this.vertexX[local5];
                this.vertexX[local5] = this.vertexZ[local5];
                this.vertexZ[local5] = -local11;
            }
            this.invalidateNormals();
        }
    }

    @OriginalMember(owner = "client!rs", name = "ba", descriptor = "(Lclient!r;)Lclient!r;")
    @Override
    public Shadow ba(@OriginalArg(0) Shadow shadow) {
        return null;
    }

    @OriginalMember(owner = "client!rs", name = "g", descriptor = "(I)Z")
    public boolean isTranslucent(@OriginalArg(0) int face) {
        if (this.faceAlpha == null) {
            return false;
        } else {
            return this.faceAlpha[face] != 0;
        }
    }

    @OriginalMember(owner = "client!rs", name = "RA", descriptor = "()I")
    @Override
    public int RA() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.maxX;
    }

    @OriginalMember(owner = "client!rs", name = "c", descriptor = "(I)V")
    public void drawFoggedTriangleRgb(@OriginalArg(0) int face) {
        @Pc(8) short a;
        @Pc(13) short b;
        @Pc(18) short c;
        @Pc(27) int fogA;
        @Pc(46) int fogB;
        @Pc(65) int fogC;
        @Pc(81) int local81;
        @Pc(333) int local333;
        if (this.threadResource.water) {
            a = this.faceA[face];
            b = this.faceB[face];
            c = this.faceC[face];
            fogA = 0;
            fogB = 0;
            fogC = 0;
            if (this.worldY[a] > this.threadResource.waterDepth) {
                fogA = 255;
            } else if (this.worldY[a] > this.threadResource.waterHeight) {
                fogA = (this.threadResource.waterHeight - this.worldY[a]) * 255 / (this.threadResource.waterHeight - this.threadResource.waterDepth);
            }
            if (this.worldY[b] > this.threadResource.waterDepth) {
                fogB = 255;
            } else if (this.worldY[b] > this.threadResource.waterHeight) {
                fogB = (this.threadResource.waterHeight - this.worldY[b]) * 255 / (this.threadResource.waterHeight - this.threadResource.waterDepth);
            }
            if (this.worldY[c] > this.threadResource.waterDepth) {
                fogC = 255;
            } else if (this.worldY[c] > this.threadResource.waterHeight) {
                fogC = (this.threadResource.waterHeight - this.worldY[c]) * 255 / (this.threadResource.waterHeight - this.threadResource.waterDepth);
            }
            if (this.faceAlpha == null) {
                this.rasterizer.alpha = 0;
            } else {
                this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
            }
            if (this.faceTextures != null && this.faceTextures[face] != -1) {
                local81 = -16777216;
                if (this.faceAlpha != null) {
                    local81 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
                }
                if (this.faceColourC[face] == -1) {
                    local333 = local81 | this.faceColourA[face] & 0xFFFFFF;
                    this.rasterizer.renderTexturedTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local333, local333, local333, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
                } else {
                    this.rasterizer.renderTexturedTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local81 | this.faceColourA[face] & 0xFFFFFF, local81 | this.faceColourB[face] & 0xFFFFFF, local81 | this.faceColourC[face] & 0xFFFFFF, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
                }
            } else if (this.faceColourC[face] == -1) {
                this.rasterizer.renderTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]));
            } else {
                this.rasterizer.renderTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourB[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourC[face] & 0xFFFF]));
            }
            return;
        }
        a = this.faceA[face];
        b = this.faceB[face];
        c = this.faceC[face];
        fogA = this.screenZ[a] - this.threadResource.fogPlane;
        if (fogA > 255) {
            fogA = 255;
        } else if (fogA < 0) {
            fogA = 0;
        }
        fogB = this.screenZ[b] - this.threadResource.fogPlane;
        if (fogB > 255) {
            fogB = 255;
        } else if (fogB < 0) {
            fogB = 0;
        }
        fogC = this.screenZ[c] - this.threadResource.fogPlane;
        if (fogC > 255) {
            fogC = 255;
        } else if (fogC < 0) {
            fogC = 0;
        }
        local81 = fogA + fogB + fogC;
        if (local81 == 765) {
            return;
        }
        if (local81 == 0) {
            this.drawTriangleRgb(face);
            return;
        }
        if (this.faceAlpha == null) {
            this.rasterizer.alpha = 0;
        } else {
            this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
        }
        if (this.faceTextures != null && this.faceTextures[face] != -1) {
            local333 = -16777216;
            if (this.faceAlpha != null) {
                local333 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
            }
            if (this.faceColourC[face] == -1) {
                @Pc(362) int local362 = local333 | this.faceColourA[face] & 0xFFFFFF;
                this.rasterizer.renderTexturedTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local362, local362, local362, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
            } else {
                this.rasterizer.renderTexturedTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local333 | this.faceColourA[face] & 0xFFFFFF, local333 | this.faceColourB[face] & 0xFFFFFF, local333 | this.faceColourC[face] & 0xFFFFFF, this.threadResource.fogColour, fogA, fogB, fogC, this.faceTextures[face]);
            }
        } else if (this.faceColourC[face] == -1) {
            this.rasterizer.renderTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]));
        } else {
            this.rasterizer.renderTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], Static462.blendArgb(fogA << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]), Static462.blendArgb(fogB << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourB[face] & 0xFFFF]), Static462.blendArgb(fogC << 24 | this.threadResource.fogColour, ColourUtils.HSV_TO_RGB[this.faceColourC[face] & 0xFFFF]));
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(BIZ)Lclient!ka;")
    @Override
    public Model copy(@OriginalArg(0) byte slot, @OriginalArg(1) int functionMask, @OriginalArg(2) boolean ensureLit) {
        this.useThreadCopyCache(Thread.currentThread());
        @Pc(4) boolean local4 = false;
        @Pc(25) JavaModel local25;
        @Pc(18) JavaModel local18;
        if (slot > 0 && slot <= 7) {
            local18 = this.copyBuffers[slot - 1];
            local25 = this.copyTargets[slot - 1];
            local4 = true;
        } else {
            local25 = local18 = new JavaModel(this.toolkit);
        }
        return this.copyTo(local25, local18, functionMask, local4, ensureLit);
    }

    @OriginalMember(owner = "client!rs", name = "g", descriptor = "()V")
    @Override
    protected void method7491() {
        if (this.toolkit.threadCount <= 1) {
            return;
        }
        synchronized (this) {
            while (super.locked) {
                try {
                    this.wait();
                } catch (@Pc(13) InterruptedException local13) {
                    /* empty */
                }
            }
            super.locked = true;
        }
    }

    @OriginalMember(owner = "client!rs", name = "e", descriptor = "()V")
    @Override
    public void method7479() {
        /* empty */
    }

    @OriginalMember(owner = "client!rs", name = "P", descriptor = "(IIII)V")
    @Override
    protected void P(@OriginalArg(0) int type, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int z) {
        @Pc(3) int local3;
        @Pc(14) int local14;
        if (type == 0) {
            local3 = 0;
            this.pivotX = 0;
            this.pivotY = 0;
            this.pivotZ = 0;
            for (local14 = 0; local14 < this.vertexCount; local14++) {
                this.pivotX += this.vertexX[local14];
                this.pivotY += this.vertexY[local14];
                this.pivotZ += this.vertexZ[local14];
                local3++;
            }
            if (local3 > 0) {
                this.pivotX = this.pivotX / local3 + x;
                this.pivotY = this.pivotY / local3 + y;
                this.pivotZ = this.pivotZ / local3 + z;
            } else {
                this.pivotX = x;
                this.pivotY = y;
                this.pivotZ = z;
            }
        } else if (type == 1) {
            for (local3 = 0; local3 < this.vertexCount; local3++) {
                this.vertexX[local3] += x;
                this.vertexY[local3] += y;
                this.vertexZ[local3] += z;
            }
        } else {
            @Pc(168) int local168;
            @Pc(186) int local186;
            if (type == 2) {
                for (local3 = 0; local3 < this.vertexCount; local3++) {
                    this.vertexX[local3] -= this.pivotX;
                    this.vertexY[local3] -= this.pivotY;
                    this.vertexZ[local3] -= this.pivotZ;
                    if (z != 0) {
                        local14 = Trig1.SIN[z];
                        local168 = Trig1.COS[z];
                        local186 = this.vertexY[local3] * local14 + this.vertexX[local3] * local168 + 16383 >> 14;
                        this.vertexY[local3] = this.vertexY[local3] * local168 + 16383 - this.vertexX[local3] * local14 >> 14;
                        this.vertexX[local3] = local186;
                    }
                    if (x != 0) {
                        local14 = Trig1.SIN[x];
                        local168 = Trig1.COS[x];
                        local186 = this.vertexY[local3] * local168 + 16383 - this.vertexZ[local3] * local14 >> 14;
                        this.vertexZ[local3] = this.vertexY[local3] * local14 + this.vertexZ[local3] * local168 + 16383 >> 14;
                        this.vertexY[local3] = local186;
                    }
                    if (y != 0) {
                        local14 = Trig1.SIN[y];
                        local168 = Trig1.COS[y];
                        local186 = this.vertexZ[local3] * local14 + this.vertexX[local3] * local168 + 16383 >> 14;
                        this.vertexZ[local3] = this.vertexZ[local3] * local168 + 16383 - this.vertexX[local3] * local14 >> 14;
                        this.vertexX[local3] = local186;
                    }
                    this.vertexX[local3] += this.pivotX;
                    this.vertexY[local3] += this.pivotY;
                    this.vertexZ[local3] += this.pivotZ;
                }
            } else if (type == 3) {
                for (local3 = 0; local3 < this.vertexCount; local3++) {
                    this.vertexX[local3] -= this.pivotX;
                    this.vertexY[local3] -= this.pivotY;
                    this.vertexZ[local3] -= this.pivotZ;
                    this.vertexX[local3] = this.vertexX[local3] * x / 128;
                    this.vertexY[local3] = this.vertexY[local3] * y / 128;
                    this.vertexZ[local3] = this.vertexZ[local3] * z / 128;
                    this.vertexX[local3] += this.pivotX;
                    this.vertexY[local3] += this.pivotY;
                    this.vertexZ[local3] += this.pivotZ;
                }
            } else {
                @Pc(508) JavaBillboardFace local508;
                @Pc(513) JavaBillboardAttributes local513;
                if (type == 5) {
                    for (local3 = 0; local3 < this.faceCount; local3++) {
                        local14 = (this.faceAlpha[local3] & 0xFF) + x * 8;
                        if (local14 < 0) {
                            local14 = 0;
                        } else if (local14 > 255) {
                            local14 = 255;
                        }
                        this.faceAlpha[local3] = (byte) local14;
                    }
                    if (this.billboardFaces != null) {
                        for (local14 = 0; local14 < this.billboardCount; local14++) {
                            local508 = this.billboardFaces[local14];
                            local513 = this.billboardAttributes[local14];
                            local513.anInt6225 = local513.anInt6225 & 0xFFFFFF | 255 - (this.faceAlpha[local508.anInt6139] & 0xFF) << 24;
                        }
                    }
                } else if (type == 7) {
                    for (local3 = 0; local3 < this.faceCount; local3++) {
                        local14 = this.faceColour[local3] & 0xFFFF;
                        local168 = local14 >> 10 & 0x3F;
                        local186 = local14 >> 7 & 0x7;
                        @Pc(567) int local567 = local14 & 0x7F;
                        @Pc(573) int local573 = local168 + x & 0x3F;
                        local186 += y;
                        if (local186 < 0) {
                            local186 = 0;
                        } else if (local186 > 7) {
                            local186 = 7;
                        }
                        local567 += z;
                        if (local567 < 0) {
                            local567 = 0;
                        } else if (local567 > 127) {
                            local567 = 127;
                        }
                        this.faceColour[local3] = (short) (local573 << 10 | local186 << 7 | local567);
                    }
                    this.recoloured = true;
                    if (this.billboardFaces != null) {
                        for (local14 = 0; local14 < this.billboardCount; local14++) {
                            local508 = this.billboardFaces[local14];
                            local513 = this.billboardAttributes[local14];
                            local513.anInt6225 = local513.anInt6225 & 0xFF000000 | ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.faceColour[local508.anInt6139] & 0xFFFF) & 0xFFFF] & 0xFFFFFF;
                        }
                    }
                } else {
                    @Pc(681) JavaBillboardAttributes local681;
                    if (type == 8) {
                        for (local3 = 0; local3 < this.billboardCount; local3++) {
                            local681 = this.billboardAttributes[local3];
                            local681.anInt6222 += x;
                            local681.anInt6229 += y;
                        }
                    } else if (type == 10) {
                        for (local3 = 0; local3 < this.billboardCount; local3++) {
                            local681 = this.billboardAttributes[local3];
                            local681.anInt6223 = local681.anInt6223 * x >> 7;
                            local681.anInt6226 = local681.anInt6226 * y >> 7;
                        }
                    } else if (type == 9) {
                        for (local3 = 0; local3 < this.billboardCount; local3++) {
                            local681 = this.billboardAttributes[local3];
                            local681.anInt6231 = local681.anInt6231 + x & 0x3FFF;
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "LA", descriptor = "(I)V")
    @Override
    public void LA(@OriginalArg(0) int contrast) {
        if ((this.functionMask & 0x2000) != 8192) {
            throw new IllegalStateException();
        }
        this.contrast = contrast;
        this.lightingState = 0;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void apply(@OriginalArg(0) Matrix matrix) {
        @Pc(2) JavaMatrix local2 = (JavaMatrix) matrix;
        @Pc(7) int local7;
        if (this.emitters != null) {
            for (local7 = 0; local7 < this.emitters.length; local7++) {
                @Pc(13) ModelParticleEmitter local13 = this.emitters[local7];
                @Pc(15) ModelParticleEmitter local15 = local13;
                if (local13.next != null) {
                    local15 = local13.next;
                }
                local15.anInt8518 = (int) (local2.tx + local2.e1_1 * (float) this.vertexX[local13.anInt8514] + local2.e1_2 * (float) this.vertexY[local13.anInt8514] + local2.e1_3 * (float) this.vertexZ[local13.anInt8514]);
                local15.anInt8502 = (int) (local2.ty + local2.e2_1 * (float) this.vertexX[local13.anInt8514] + local2.e2_2 * (float) this.vertexY[local13.anInt8514] + local2.e2_3 * (float) this.vertexZ[local13.anInt8514]);
                local15.anInt8504 = (int) (local2.tz + local2.e3_1 * (float) this.vertexX[local13.anInt8514] + local2.e3_2 * (float) this.vertexY[local13.anInt8514] + local2.e3_3 * (float) this.vertexZ[local13.anInt8514]);
                local15.anInt8516 = (int) (local2.tx + local2.e1_1 * (float) this.vertexX[local13.anInt8508] + local2.e1_2 * (float) this.vertexY[local13.anInt8508] + local2.e1_3 * (float) this.vertexZ[local13.anInt8508]);
                local15.anInt8507 = (int) (local2.ty + local2.e2_1 * (float) this.vertexX[local13.anInt8508] + local2.e2_2 * (float) this.vertexY[local13.anInt8508] + local2.e2_3 * (float) this.vertexZ[local13.anInt8508]);
                local15.anInt8509 = (int) (local2.tz + local2.e3_1 * (float) this.vertexX[local13.anInt8508] + local2.e3_2 * (float) this.vertexY[local13.anInt8508] + local2.e3_3 * (float) this.vertexZ[local13.anInt8508]);
                local15.anInt8512 = (int) (local2.tx + local2.e1_1 * (float) this.vertexX[local13.anInt8505] + local2.e1_2 * (float) this.vertexY[local13.anInt8505] + local2.e1_3 * (float) this.vertexZ[local13.anInt8505]);
                local15.anInt8503 = (int) (local2.ty + local2.e2_1 * (float) this.vertexX[local13.anInt8505] + local2.e2_2 * (float) this.vertexY[local13.anInt8505] + local2.e2_3 * (float) this.vertexZ[local13.anInt8505]);
                local15.anInt8520 = (int) (local2.tz + local2.e3_1 * (float) this.vertexX[local13.anInt8505] + local2.e3_2 * (float) this.vertexY[local13.anInt8505] + local2.e3_3 * (float) this.vertexZ[local13.anInt8505]);
            }
        }
        if (this.effectors == null) {
            return;
        }
        for (local7 = 0; local7 < this.effectors.length; local7++) {
            @Pc(355) ModelParticleEffector local355 = this.effectors[local7];
            @Pc(357) ModelParticleEffector local357 = local355;
            if (local355.next != null) {
                local357 = local355.next;
            }
            if (local355.matrix == null) {
                local355.matrix = local2.copy();
            } else {
                local355.matrix.apply(local2);
            }
            local357.x = (int) (local2.tx + local2.e1_1 * (float) this.vertexX[local355.vertex] + local2.e1_2 * (float) this.vertexY[local355.vertex] + local2.e1_3 * (float) this.vertexZ[local355.vertex]);
            local357.y = (int) (local2.ty + local2.e2_1 * (float) this.vertexX[local355.vertex] + local2.e2_2 * (float) this.vertexY[local355.vertex] + local2.e2_3 * (float) this.vertexZ[local355.vertex]);
            local357.z = (int) (local2.tz + local2.e3_1 * (float) this.vertexX[local355.vertex] + local2.e3_2 * (float) this.vertexY[local355.vertex] + local2.e3_3 * (float) this.vertexZ[local355.vertex]);
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(IZZ)V")
    public void drawFaceArgb(@OriginalArg(0) int face, @OriginalArg(1) boolean nearClipped, @OriginalArg(2) boolean fogged) {
        if (this.faceColourC[face] == -2) {
            return;
        }
        @Pc(12) short a = this.faceA[face];
        @Pc(17) short b = this.faceB[face];
        @Pc(22) short c = this.faceC[face];
        @Pc(27) int screenXA = this.screenX[a];
        @Pc(32) int screenXB = this.screenX[b];
        @Pc(37) int screenXC = this.screenX[c];
        @Pc(59) int local59;
        if (nearClipped && (screenXA == -5000 || screenXB == -5000 || screenXC == -5000)) {
            local59 = this.cameraX[a];
            @Pc(64) int local64 = this.cameraX[b];
            @Pc(69) int local69 = this.cameraX[c];
            @Pc(74) int local74 = this.cameraY[a];
            @Pc(79) int local79 = this.cameraY[b];
            @Pc(84) int local84 = this.cameraY[c];
            @Pc(89) int local89 = this.cameraZ[a];
            @Pc(94) int local94 = this.cameraZ[b];
            @Pc(99) int local99 = this.cameraZ[c];
            @Pc(103) int local103 = local59 - local64;
            @Pc(107) int local107 = local69 - local64;
            @Pc(111) int local111 = local74 - local79;
            @Pc(115) int local115 = local84 - local79;
            @Pc(119) int local119 = local89 - local94;
            @Pc(123) int local123 = local99 - local94;
            @Pc(131) int local131 = local111 * local123 - local119 * local115;
            @Pc(139) int local139 = local119 * local107 - local103 * local123;
            @Pc(147) int local147 = local103 * local115 - local111 * local107;
            if (local64 * local131 + local79 * local139 + local94 * local147 > 0) {
                this.drawClippedTriangleArgb(face);
                return;
            }
        } else if (this.faceBillboard[face] != -1 || (screenXA - screenXB) * (this.screenY[c] - this.screenY[b]) - (this.screenY[a] - this.screenY[b]) * (screenXC - screenXB) > 0) {
            if (screenXA >= 0 && screenXB >= 0 && screenXC >= 0 && screenXA <= this.threadResource.rasterWidth && screenXB <= this.threadResource.rasterWidth && screenXC <= this.threadResource.rasterWidth) {
                this.rasterizer.clamp = false;
            } else {
                this.rasterizer.clamp = true;
            }
            if (fogged) {
                local59 = this.faceBillboard[face];
                if (local59 == -1 || !this.billboardFaces[local59].aBoolean464) {
                    this.drawFoggedTriangleArgb(face);
                }
                return;
            }
            local59 = this.faceBillboard[face];
            if (local59 != -1) {
                @Pc(280) JavaBillboardFace local280 = this.billboardFaces[local59];
                @Pc(285) JavaBillboardAttributes local285 = this.billboardAttributes[local59];
                if (!local280.aBoolean464) {
                    this.drawTriangleArgb(face);
                }
                this.toolkit.drawBillboardArgb(local285.anInt6221, local285.anInt6227, local285.anInt6224, local285.anInt6232, local285.anInt6220, local285.anInt6231, local280.aShort72 & 0xFFFF, local285.anInt6225, local280.aByte98, local280.aByte97);
                return;
            }
            this.drawTriangleArgb(face);
        }
    }

    @OriginalMember(owner = "client!rs", name = "p", descriptor = "()V")
    public void relight() {
        if (this.lightingState == 0) {
            this.applyLighting(false);
        } else if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.relightColours();
            }
        } else {
            this.relightColours();
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(B[B)V")
    @Override
    public void updateAlphas(@OriginalArg(0) byte alpha, @OriginalArg(1) byte[] alphas) {
        if ((this.functionMask & 0x100000) == 0) {
            throw new RuntimeException();
        }
        if (this.faceAlpha == null) {
            this.faceAlpha = new byte[this.faceCount];
        }
        @Pc(23) int local23;
        if (alphas == null) {
            for (local23 = 0; local23 < this.faceCount; local23++) {
                this.faceAlpha[local23] = alpha;
            }
        } else {
            for (local23 = 0; local23 < this.faceCount; local23++) {
                @Pc(57) int local57 = 255 - (255 - (alphas[local23] & 0xFF)) * (255 - (alpha & 0xFF)) / 255;
                this.faceAlpha[local23] = (byte) local57;
            }
        }
        if (this.lightingState == 2) {
            this.lightingState = 1;
        }
    }

    @OriginalMember(owner = "client!rs", name = "f", descriptor = "()[Lclient!rv;")
    @Override
    public ModelParticleEmitter[] particleEmitters() {
        return this.emitters;
    }

    @OriginalMember(owner = "client!rs", name = "ia", descriptor = "(SS)V")
    @Override
    public void ia(@OriginalArg(0) short src, @OriginalArg(1) short dest) {
        for (@Pc(1) int local1 = 0; local1 < this.faceCount; local1++) {
            if (this.faceColour[local1] == src) {
                this.faceColour[local1] = dest;
            }
        }
        if (this.billboardFaces != null) {
            for (@Pc(27) int local27 = 0; local27 < this.billboardCount; local27++) {
                @Pc(33) JavaBillboardFace local33 = this.billboardFaces[local27];
                @Pc(38) JavaBillboardAttributes local38 = this.billboardAttributes[local27];
                local38.anInt6225 = local38.anInt6225 & 0xFF000000 | ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.faceColour[local33.anInt6139]) & 0xFFFF] & 0xFFFFFF;
            }
        }
        if (this.lightingState == 2) {
            this.lightingState = 1;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(IILclient!tt;ZII)Z")
    @Override
    public boolean pickedOrtho(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift, @OriginalArg(5) int angle) {
        return this.pick(x, y, matrix, quick, sizeShift, angle);
    }

    @OriginalMember(owner = "client!rs", name = "i", descriptor = "()V")
    public void calculateNormals() {
        this.vertexNormals = new VertexNormal[this.maxVertex];
        for (@Pc(6) int i = 0; i < this.maxVertex; i++) {
            this.vertexNormals[i] = new VertexNormal();
        }
        for (@Pc(21) int i = 0; i < this.faceCount; i++) {
            @Pc(27) short a = this.faceA[i];
            @Pc(32) short b = this.faceB[i];
            @Pc(37) short c = this.faceC[i];
            @Pc(47) int dxAB = this.vertexX[b] - this.vertexX[a];
            @Pc(57) int dyAB = this.vertexY[b] - this.vertexY[a];
            @Pc(67) int dzAB = this.vertexZ[b] - this.vertexZ[a];
            @Pc(77) int dxAC = this.vertexX[c] - this.vertexX[a];
            @Pc(87) int dyAC = this.vertexY[c] - this.vertexY[a];
            @Pc(97) int dzAC = this.vertexZ[c] - this.vertexZ[a];
            @Pc(105) int nx = dyAB * dzAC - dyAC * dzAB;
            @Pc(113) int ny = dzAB * dxAC - dzAC * dxAB;
            @Pc(121) int nz;
            for (nz = dxAB * dyAC - dxAC * dyAB; nx > 8192 || ny > 8192 || nz > 8192 || nx < -8192 || ny < -8192 || nz < -8192; nz >>= 0x1) {
                nx >>= 0x1;
                ny >>= 0x1;
            }
            @Pc(169) int length = (int) Math.sqrt(nx * nx + ny * ny + nz * nz);
            if (length <= 0) {
                length = 1;
            }
            nx = nx * 256 / length;
            ny = ny * 256 / length;
            nz = nz * 256 / length;
            @Pc(196) byte shading;
            if (this.shadingType == null) {
                shading = 0;
            } else {
                shading = this.shadingType[i];
            }
            if (shading == 0) {
                @Pc(209) VertexNormal normalA = this.vertexNormals[a];
                normalA.x += nx;
                normalA.y += ny;
                normalA.z += nz;
                normalA.magnitude++;
                @Pc(238) VertexNormal normalB = this.vertexNormals[b];
                normalB.x += nx;
                normalB.y += ny;
                normalB.z += nz;
                normalB.magnitude++;
                @Pc(267) VertexNormal normalC = this.vertexNormals[c];
                normalC.x += nx;
                normalC.y += ny;
                normalC.z += nz;
                normalC.magnitude++;
            } else if (shading == 1) {
                if (this.faceNormals == null) {
                    this.faceNormals = new FaceNormal[this.faceCount];
                }
                @Pc(316) FaceNormal faceNormal = this.faceNormals[i] = new FaceNormal();
                faceNormal.x = nx;
                faceNormal.y = ny;
                faceNormal.z = nz;
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!tt;Lclient!ima;II)V")
    @Override
    public void renderOrtho(@OriginalArg(0) Matrix matrix, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int orthoDepth, @OriginalArg(3) int flags) {
        this.draw(matrix, cylinder, orthoDepth, flags);
    }

    @OriginalMember(owner = "client!rs", name = "c", descriptor = "()[Lclient!mn;")
    @Override
    public ModelParticleEffector[] particleEffectors() {
        return this.effectors;
    }

    @OriginalMember(owner = "client!rs", name = "l", descriptor = "()V")
    public void rotate90WithNormals() {
        synchronized (this) {
            @Pc(11) int local11;
            for (@Pc(5) int local5 = 0; local5 < this.maxVertex; local5++) {
                local11 = this.vertexX[local5];
                this.vertexX[local5] = this.vertexZ[local5];
                this.vertexZ[local5] = -local11;
                if (this.vertexNormals[local5] != null) {
                    local11 = this.vertexNormals[local5].x;
                    this.vertexNormals[local5].x = this.vertexNormals[local5].z;
                    this.vertexNormals[local5].z = -local11;
                }
            }
            @Pc(77) int local77;
            if (this.faceNormals != null) {
                for (local11 = 0; local11 < this.faceCount; local11++) {
                    if (this.faceNormals[local11] != null) {
                        local77 = this.faceNormals[local11].x;
                        this.faceNormals[local11].x = this.faceNormals[local11].z;
                        this.faceNormals[local11].z = -local77;
                    }
                }
            }
            for (local11 = this.maxVertex; local11 < this.vertexCount; local11++) {
                local77 = this.vertexX[local11];
                this.vertexX[local11] = this.vertexZ[local11];
                this.vertexZ[local11] = -local77;
            }
            this.lightingState = 0;
            this.boundsValid = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!ka;IIIZ)V")
    @Override
    public void method7481(@OriginalArg(0) Model other, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int z, @OriginalArg(4) boolean arg4) {
        @Pc(2) JavaModel local2 = (JavaModel) other;
        if ((this.functionMask & 0x10000) != 65536) {
            throw new IllegalStateException("");
        } else if ((local2.functionMask & 0x10000) == 65536) {
            this.useThreadBuffers(Thread.currentThread());
            this.calculateBounds();
            this.ensureNormals();
            local2.calculateBounds();
            local2.ensureNormals();
            Static567.anInt8494++;
            @Pc(43) int local43 = 0;
            @Pc(46) int[] local46 = local2.vertexX;
            @Pc(49) int local49 = local2.maxVertex;
            @Pc(67) int local67;
            for (@Pc(51) int local51 = 0; local51 < this.maxVertex; local51++) {
                @Pc(57) VertexNormal local57 = this.vertexNormals[local51];
                if (local57.magnitude != 0) {
                    local67 = this.vertexY[local51] - y;
                    if (local67 >= local2.minY && local67 <= local2.maxY) {
                        @Pc(86) int local86 = this.vertexX[local51] - x;
                        if (local86 >= local2.minX && local86 <= local2.maxX) {
                            @Pc(105) int local105 = this.vertexZ[local51] - z;
                            if (local105 >= local2.minZ && local105 <= local2.maxZ) {
                                for (@Pc(119) int local119 = 0; local119 < local49; local119++) {
                                    @Pc(125) VertexNormal local125 = local2.vertexNormals[local119];
                                    if (local86 == local46[local119] && local105 == local2.vertexZ[local119] && local67 == local2.vertexY[local119] && local125.magnitude != 0) {
                                        if (this.vertexNormalsOffset == null) {
                                            this.vertexNormalsOffset = new VertexNormal[this.maxVertex];
                                        }
                                        if (local2.vertexNormalsOffset == null) {
                                            local2.vertexNormalsOffset = new VertexNormal[local49];
                                        }
                                        @Pc(177) VertexNormal local177 = this.vertexNormalsOffset[local51];
                                        if (local177 == null) {
                                            local177 = this.vertexNormalsOffset[local51] = new VertexNormal(local57);
                                        }
                                        @Pc(194) VertexNormal local194 = local2.vertexNormalsOffset[local119];
                                        if (local194 == null) {
                                            local194 = local2.vertexNormalsOffset[local119] = new VertexNormal(local125);
                                        }
                                        local177.x += local125.x;
                                        local177.y += local125.y;
                                        local177.z += local125.z;
                                        local177.magnitude += local125.magnitude;
                                        local194.x += local57.x;
                                        local194.y += local57.y;
                                        local194.z += local57.z;
                                        local194.magnitude += local57.magnitude;
                                        local43++;
                                        this.mergeStamps[local51] = Static567.anInt8494;
                                        this.otherMergeStamps[local119] = Static567.anInt8494;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (local43 >= 3 && arg4) {
                for (@Pc(297) int local297 = 0; local297 < this.faceCount; local297++) {
                    if (this.mergeStamps[this.faceA[local297]] == Static567.anInt8494 && this.mergeStamps[this.faceB[local297]] == Static567.anInt8494 && this.mergeStamps[this.faceC[local297]] == Static567.anInt8494) {
                        if (this.shadingType == null) {
                            this.shadingType = new byte[this.faceCount];
                        }
                        this.shadingType[local297] = 2;
                    }
                }
                for (local67 = 0; local67 < local2.faceCount; local67++) {
                    if (this.otherMergeStamps[local2.faceA[local67]] == Static567.anInt8494 && this.otherMergeStamps[local2.faceB[local67]] == Static567.anInt8494 && this.otherMergeStamps[local2.faceC[local67]] == Static567.anInt8494) {
                        if (local2.shadingType == null) {
                            local2.shadingType = new byte[local2.faceCount];
                        }
                        local2.shadingType[local67] = 2;
                    }
                }
            }
        } else {
            throw new IllegalStateException("");
        }
    }

    @OriginalMember(owner = "client!rs", name = "F", descriptor = "()Z")
    @Override
    public boolean F() {
        return this.transparent;
    }

    @OriginalMember(owner = "client!rs", name = "H", descriptor = "(III)V")
    @Override
    public void H(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z) {
        if (x != 0 && (this.functionMask & 0x1) != 1) {
            throw new IllegalStateException();
        } else if (y != 0 && (this.functionMask & 0x2) != 2) {
            throw new IllegalStateException();
        } else if (z == 0 || (this.functionMask & 0x4) == 4) {
            synchronized (this) {
                for (@Pc(50) int local50 = 0; local50 < this.vertexCount; local50++) {
                    this.vertexX[local50] += x;
                    this.vertexY[local50] += y;
                    this.vertexZ[local50] += z;
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(I[IIIIIZ)V")
    @Override
    protected void method7499(@OriginalArg(0) int type, @OriginalArg(1) int[] labels, @OriginalArg(2) int x, @OriginalArg(3) int y, @OriginalArg(4) int z, @OriginalArg(5) int arg5, @OriginalArg(6) boolean arg6) {
        @Pc(2) int local2 = labels.length;
        @Pc(21) int local21;
        @Pc(69) int local69;
        @Pc(91) int local91;
        @Pc(8) int local8;
        @Pc(12) int local12;
        @Pc(16) int local16;
        @Pc(86) int local86;
        if (type == 0) {
            local8 = x << 4;
            local12 = y << 4;
            local16 = z << 4;
            if (!this.highPrecisionVertices) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.highPrecisionVertices = true;
            }
            local21 = 0;
            this.pivotX = 0;
            this.pivotY = 0;
            this.pivotZ = 0;
            for (local69 = 0; local69 < local2; local69++) {
                @Pc(74) int local74 = labels[local69];
                if (local74 < this.vertexLabels.length) {
                    @Pc(84) int[] local84 = this.vertexLabels[local74];
                    for (local86 = 0; local86 < local84.length; local86++) {
                        local91 = local84[local86];
                        this.pivotX += this.vertexX[local91];
                        this.pivotY += this.vertexY[local91];
                        this.pivotZ += this.vertexZ[local91];
                        local21++;
                    }
                }
            }
            if (local21 > 0) {
                this.pivotX = this.pivotX / local21 + local8;
                this.pivotY = this.pivotY / local21 + local12;
                this.pivotZ = this.pivotZ / local21 + local16;
            } else {
                this.pivotX = local8;
                this.pivotY = local12;
                this.pivotZ = local16;
            }
            return;
        }
        @Pc(242) int[] local242;
        @Pc(244) int local244;
        if (type == 1) {
            local8 = x << 4;
            local12 = y << 4;
            local16 = z << 4;
            if (!this.highPrecisionVertices) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.highPrecisionVertices = true;
            }
            for (local21 = 0; local21 < local2; local21++) {
                local69 = labels[local21];
                if (local69 < this.vertexLabels.length) {
                    local242 = this.vertexLabels[local69];
                    for (local244 = 0; local244 < local242.length; local244++) {
                        local86 = local242[local244];
                        this.vertexX[local86] += local8;
                        this.vertexY[local86] += local12;
                        this.vertexZ[local86] += local16;
                    }
                }
            }
            return;
        }
        @Pc(354) int local354;
        @Pc(372) int local372;
        if (type == 2) {
            for (local21 = 0; local21 < local2; local21++) {
                local69 = labels[local21];
                if (local69 < this.vertexLabels.length) {
                    local242 = this.vertexLabels[local69];
                    if ((arg5 & 0x1) == 0) {
                        for (local244 = 0; local244 < local242.length; local244++) {
                            local86 = local242[local244];
                            this.vertexX[local86] -= this.pivotX;
                            this.vertexY[local86] -= this.pivotY;
                            this.vertexZ[local86] -= this.pivotZ;
                            if (z != 0) {
                                local91 = Trig1.SIN[z];
                                local354 = Trig1.COS[z];
                                local372 = this.vertexY[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = this.vertexY[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            if (x != 0) {
                                local91 = Trig1.SIN[x];
                                local354 = Trig1.COS[x];
                                local372 = this.vertexY[local86] * local354 + 16383 - this.vertexZ[local86] * local91 >> 14;
                                this.vertexZ[local86] = this.vertexY[local86] * local91 + this.vertexZ[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = local372;
                            }
                            if (y != 0) {
                                local91 = Trig1.SIN[y];
                                local354 = Trig1.COS[y];
                                local372 = this.vertexZ[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexZ[local86] = this.vertexZ[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            this.vertexX[local86] += this.pivotX;
                            this.vertexY[local86] += this.pivotY;
                            this.vertexZ[local86] += this.pivotZ;
                        }
                    } else {
                        for (local244 = 0; local244 < local242.length; local244++) {
                            local86 = local242[local244];
                            this.vertexX[local86] -= this.pivotX;
                            this.vertexY[local86] -= this.pivotY;
                            this.vertexZ[local86] -= this.pivotZ;
                            if (x != 0) {
                                local91 = Trig1.SIN[x];
                                local354 = Trig1.COS[x];
                                local372 = this.vertexY[local86] * local354 + 16383 - this.vertexZ[local86] * local91 >> 14;
                                this.vertexZ[local86] = this.vertexY[local86] * local91 + this.vertexZ[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = local372;
                            }
                            if (z != 0) {
                                local91 = Trig1.SIN[z];
                                local354 = Trig1.COS[z];
                                local372 = this.vertexY[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = this.vertexY[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            if (y != 0) {
                                local91 = Trig1.SIN[y];
                                local354 = Trig1.COS[y];
                                local372 = this.vertexZ[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexZ[local86] = this.vertexZ[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            this.vertexX[local86] += this.pivotX;
                            this.vertexY[local86] += this.pivotY;
                            this.vertexZ[local86] += this.pivotZ;
                        }
                    }
                }
            }
        } else if (type == 3) {
            for (local21 = 0; local21 < local2; local21++) {
                local69 = labels[local21];
                if (local69 < this.vertexLabels.length) {
                    local242 = this.vertexLabels[local69];
                    for (local244 = 0; local244 < local242.length; local244++) {
                        local86 = local242[local244];
                        this.vertexX[local86] -= this.pivotX;
                        this.vertexY[local86] -= this.pivotY;
                        this.vertexZ[local86] -= this.pivotZ;
                        this.vertexX[local86] = this.vertexX[local86] * x / 128;
                        this.vertexY[local86] = this.vertexY[local86] * y / 128;
                        this.vertexZ[local86] = this.vertexZ[local86] * z / 128;
                        this.vertexX[local86] += this.pivotX;
                        this.vertexY[local86] += this.pivotY;
                        this.vertexZ[local86] += this.pivotZ;
                    }
                }
            }
        } else {
            @Pc(994) JavaBillboardFace local994;
            @Pc(999) JavaBillboardAttributes local999;
            if (type == 5) {
                if (this.faceLabels != null && this.faceAlpha != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = labels[local21];
                        if (local69 < this.faceLabels.length) {
                            local242 = this.faceLabels[local69];
                            for (local244 = 0; local244 < local242.length; local244++) {
                                local86 = local242[local244];
                                local91 = (this.faceAlpha[local86] & 0xFF) + x * 8;
                                if (local91 < 0) {
                                    local91 = 0;
                                } else if (local91 > 255) {
                                    local91 = 255;
                                }
                                this.faceAlpha[local86] = (byte) local91;
                            }
                        }
                    }
                    if (this.billboardFaces != null) {
                        for (local69 = 0; local69 < this.billboardCount; local69++) {
                            local994 = this.billboardFaces[local69];
                            local999 = this.billboardAttributes[local69];
                            local999.anInt6225 = local999.anInt6225 & 0xFFFFFF | 255 - (this.faceAlpha[local994.anInt6139] & 0xFF) << 24;
                        }
                    }
                }
            } else if (type != 7) {
                @Pc(1223) JavaBillboardAttributes local1223;
                if (type == 8) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = labels[local21];
                            if (local69 < this.billboardLabels.length) {
                                local242 = this.billboardLabels[local69];
                                for (local244 = 0; local244 < local242.length; local244++) {
                                    local1223 = this.billboardAttributes[local242[local244]];
                                    local1223.anInt6222 += x;
                                    local1223.anInt6229 += y;
                                }
                            }
                        }
                    }
                } else if (type == 10) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = labels[local21];
                            if (local69 < this.billboardLabels.length) {
                                local242 = this.billboardLabels[local69];
                                for (local244 = 0; local244 < local242.length; local244++) {
                                    local1223 = this.billboardAttributes[local242[local244]];
                                    local1223.anInt6223 = local1223.anInt6223 * x >> 7;
                                    local1223.anInt6226 = local1223.anInt6226 * y >> 7;
                                }
                            }
                        }
                    }
                } else if (type == 9 && this.billboardLabels != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = labels[local21];
                        if (local69 < this.billboardLabels.length) {
                            local242 = this.billboardLabels[local69];
                            for (local244 = 0; local244 < local242.length; local244++) {
                                local1223 = this.billboardAttributes[local242[local244]];
                                local1223.anInt6231 = local1223.anInt6231 + x & 0x3FFF;
                            }
                        }
                    }
                }
            } else if (this.faceLabels != null) {
                for (local21 = 0; local21 < local2; local21++) {
                    local69 = labels[local21];
                    if (local69 < this.faceLabels.length) {
                        local242 = this.faceLabels[local69];
                        for (local244 = 0; local244 < local242.length; local244++) {
                            local86 = local242[local244];
                            local91 = this.faceColour[local86] & 0xFFFF;
                            local354 = local91 >> 10 & 0x3F;
                            local372 = local91 >> 7 & 0x7;
                            @Pc(1079) int local1079 = local91 & 0x7F;
                            @Pc(1085) int local1085 = local354 + x & 0x3F;
                            local372 += y;
                            if (local372 < 0) {
                                local372 = 0;
                            } else if (local372 > 7) {
                                local372 = 7;
                            }
                            local1079 += z;
                            if (local1079 < 0) {
                                local1079 = 0;
                            } else if (local1079 > 127) {
                                local1079 = 127;
                            }
                            this.faceColour[local86] = (short) (local1085 << 10 | local372 << 7 | local1079);
                        }
                        this.recoloured = true;
                    }
                }
                if (this.billboardFaces != null) {
                    for (local69 = 0; local69 < this.billboardCount; local69++) {
                        local994 = this.billboardFaces[local69];
                        local999 = this.billboardAttributes[local69];
                        local999.anInt6225 = local999.anInt6225 & 0xFF000000 | ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.faceColour[local994.anInt6139] & 0xFFFF) & 0xFFFF] & 0xFFFFFF;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Z)V")
    public void calculateLighting(@OriginalArg(0) boolean discardShading) {
        if (this.lightingState == 1) {
            this.relight();
        } else if (this.lightingState == 2) {
            if ((this.functionMask & 0x97098) == 0 && this.texCoordU == null) {
                this.faceColour = null;
            }
            if (discardShading) {
                this.shadingType = null;
            }
        } else {
            this.ensureNormals();
            @Pc(42) int sunX = this.toolkit.sunX;
            @Pc(46) int sunY = this.toolkit.sunY;
            @Pc(50) int sunZ = this.toolkit.sunZ;
            @Pc(56) int ambientLight = this.toolkit.ambient >> 8;
            @Pc(65) int sunIntensity = this.toolkit.sunIntensity * 768 / this.contrast;
            @Pc(74) int reverseSunIntensity = this.toolkit.reverseSunIntensity * 768 / this.contrast;
            if (this.faceColourA == null) {
                this.faceColourA = new int[this.faceCount];
                this.faceColourB = new int[this.faceCount];
                this.faceColourC = new int[this.faceCount];
            }
            for (@Pc(96) int face = 0; face < this.faceCount; face++) {
                @Pc(102) byte shading;
                if (this.shadingType == null) {
                    shading = 0;
                } else {
                    shading = this.shadingType[face];
                }
                @Pc(113) byte alpha;
                if (this.faceAlpha == null) {
                    alpha = 0;
                } else {
                    alpha = this.faceAlpha[face];
                }
                @Pc(124) short texture;
                if (this.faceTextures == null) {
                    texture = -1;
                } else {
                    texture = this.faceTextures[face];
                }
                if (alpha == -2) {
                    shading = 3;
                }
                if (alpha == -1) {
                    shading = 2;
                }
                @Pc(154) int hsl;
                @Pc(239) int local239;
                if (texture == -1) {
                    @Pc(221) int local221;
                    @Pc(229) int local229;
                    @Pc(171) short hsv;
                    @Pc(163) int lightness;
                    if (shading == 0) {
                        hsl = this.faceColour[face] & 0xFFFF;
                        lightness = (hsl & 0x7F) * this.ambient >> 7;
                        hsv = ColourUtils.hslToHsv(hsl & 0xFFFFFF80 | lightness);
                        @Pc(192) VertexNormal normal;
                        if (this.vertexNormalsOffset == null || this.vertexNormalsOffset[this.faceA[face]] == null) {
                            normal = this.vertexNormals[this.faceA[face]];
                        } else {
                            normal = this.vertexNormalsOffset[this.faceA[face]];
                        }
                        local221 = (sunX * normal.x + sunY * normal.y + sunZ * normal.z) / normal.magnitude >> 16;
                        local229 = local221 > 256 ? sunIntensity : reverseSunIntensity;
                        local239 = (ambientLight >> 1) + (local229 * local221 >> 17);
                        this.faceColourA[face] = local239 << 17 | Static244.scaleHslLightness(local239, hsv);
                        if (this.vertexNormalsOffset == null || this.vertexNormalsOffset[this.faceB[face]] == null) {
                            normal = this.vertexNormals[this.faceB[face]];
                        } else {
                            normal = this.vertexNormalsOffset[this.faceB[face]];
                        }
                        local221 = (sunX * normal.x + sunY * normal.y + sunZ * normal.z) / normal.magnitude >> 16;
                        local229 = local221 > 256 ? sunIntensity : reverseSunIntensity;
                        local239 = (ambientLight >> 1) + (local229 * local221 >> 17);
                        this.faceColourB[face] = local239 << 17 | Static244.scaleHslLightness(local239, hsv);
                        if (this.vertexNormalsOffset == null || this.vertexNormalsOffset[this.faceC[face]] == null) {
                            normal = this.vertexNormals[this.faceC[face]];
                        } else {
                            normal = this.vertexNormalsOffset[this.faceC[face]];
                        }
                        local221 = (sunX * normal.x + sunY * normal.y + sunZ * normal.z) / normal.magnitude >> 16;
                        local229 = local221 > 256 ? sunIntensity : reverseSunIntensity;
                        local239 = (ambientLight >> 1) + (local229 * local221 >> 17);
                        this.faceColourC[face] = local239 << 17 | Static244.scaleHslLightness(local239, hsv);
                    } else if (shading == 1) {
                        hsl = this.faceColour[face] & 0xFFFF;
                        lightness = (hsl & 0x7F) * this.ambient >> 7;
                        hsv = ColourUtils.hslToHsv(hsl & 0xFFFFFF80 | lightness);
                        @Pc(444) FaceNormal faceNormal = this.faceNormals[face];
                        local239 = sunX * faceNormal.x + sunY * faceNormal.y + sunZ * faceNormal.z >> 16;
                        local221 = local239 > 256 ? sunIntensity : reverseSunIntensity;
                        local229 = (ambientLight >> 1) + (local221 * local239 >> 17);
                        this.faceColourA[face] = local229 << 17 | Static244.scaleHslLightness(local229, hsv);
                        this.faceColourC[face] = -1;
                    } else if (shading == 3) {
                        this.faceColourA[face] = 128;
                        this.faceColourC[face] = -1;
                    } else {
                        this.faceColourC[face] = -2;
                    }
                } else {
                    hsl = this.faceColour[face] & 0xFFFF;
                    @Pc(599) int local599;
                    @Pc(579) int local579;
                    if (shading == 0) {
                        @Pc(550) VertexNormal normal;
                        if (this.vertexNormalsOffset == null || this.vertexNormalsOffset[this.faceA[face]] == null) {
                            normal = this.vertexNormals[this.faceA[face]];
                        } else {
                            normal = this.vertexNormalsOffset[this.faceA[face]];
                        }
                        local579 = (sunX * normal.x + sunY * normal.y + sunZ * normal.z) / normal.magnitude >> 16;
                        local239 = local579 > 256 ? sunIntensity : reverseSunIntensity;
                        local599 = this.clampLightness((ambientLight >> 2) + (local239 * local579 >> 18));
                        this.faceColourA[face] = local599 << 24 | this.shadeTexturedRgb(hsl, texture, local599);
                        if (this.vertexNormalsOffset == null || this.vertexNormalsOffset[this.faceB[face]] == null) {
                            normal = this.vertexNormals[this.faceB[face]];
                        } else {
                            normal = this.vertexNormalsOffset[this.faceB[face]];
                        }
                        local579 = (sunX * normal.x + sunY * normal.y + sunZ * normal.z) / normal.magnitude >> 16;
                        local239 = local579 > 256 ? sunIntensity : reverseSunIntensity;
                        local599 = this.clampLightness((ambientLight >> 2) + (local239 * local579 >> 18));
                        this.faceColourB[face] = local599 << 24 | this.shadeTexturedRgb(hsl, texture, local599);
                        if (this.vertexNormalsOffset == null || this.vertexNormalsOffset[this.faceC[face]] == null) {
                            normal = this.vertexNormals[this.faceC[face]];
                        } else {
                            normal = this.vertexNormalsOffset[this.faceC[face]];
                        }
                        local579 = (sunX * normal.x + sunY * normal.y + sunZ * normal.z) / normal.magnitude >> 16;
                        local239 = local579 > 256 ? sunIntensity : reverseSunIntensity;
                        local599 = this.clampLightness((ambientLight >> 2) + (local239 * local579 >> 18));
                        this.faceColourC[face] = local599 << 24 | this.shadeTexturedRgb(hsl, texture, local599);
                    } else if (shading == 1) {
                        @Pc(787) FaceNormal faceNormal = this.faceNormals[face];
                        local599 = sunX * faceNormal.x + sunY * faceNormal.y + sunZ * faceNormal.z >> 16;
                        local579 = local599 > 256 ? sunIntensity : reverseSunIntensity;
                        local239 = this.clampLightness((ambientLight >> 2) + (local579 * local599 >> 18));
                        this.faceColourA[face] = local239 << 24 | this.shadeTexturedRgb(hsl, texture, local239);
                        this.faceColourC[face] = -1;
                    } else {
                        this.faceColourC[face] = -2;
                    }
                }
            }
            this.vertexNormals = null;
            this.vertexNormalsOffset = null;
            this.faceNormals = null;
            if ((this.functionMask & 0x97098) == 0 && this.texCoordU == null) {
                this.faceColour = null;
            }
            if (discardShading) {
                this.shadingType = null;
            }
            this.lightingState = 2;
        }
    }

    @OriginalMember(owner = "client!rs", name = "wa", descriptor = "()V")
    @Override
    protected void wa() {
        if (this.highPrecisionVertices) {
            for (@Pc(4) int local4 = 0; local4 < this.vertexCount; local4++) {
                this.vertexX[local4] = this.vertexX[local4] + 7 >> 4;
                this.vertexY[local4] = this.vertexY[local4] + 7 >> 4;
                this.vertexZ[local4] = this.vertexZ[local4] + 7 >> 4;
            }
            this.highPrecisionVertices = false;
        }
        if (this.recoloured) {
            this.relight();
            this.recoloured = false;
        }
        this.boundsValid = false;
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(IILclient!tt;ZII)Z")
    public boolean pick(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift, @OriginalArg(5) int orthoDepth) {
        this.modelMatrix = (JavaMatrix) matrix;
        @Pc(7) JavaMatrix local7 = this.toolkit.camera;
        @Pc(31) float local31 = local7.tx + local7.e1_1 * this.modelMatrix.tx + local7.e1_2 * this.modelMatrix.ty + local7.e1_3 * this.modelMatrix.tz;
        @Pc(55) float local55 = local7.ty + local7.e2_1 * this.modelMatrix.tx + local7.e2_2 * this.modelMatrix.ty + local7.e2_3 * this.modelMatrix.tz;
        @Pc(79) float local79 = local7.tz + local7.e3_1 * this.modelMatrix.tx + local7.e3_2 * this.modelMatrix.ty + local7.e3_3 * this.modelMatrix.tz;
        @Pc(100) float local100 = local7.e1_1 * this.modelMatrix.e1_1 + local7.e1_2 * this.modelMatrix.e2_1 + local7.e1_3 * this.modelMatrix.e3_1;
        @Pc(121) float local121 = local7.e1_1 * this.modelMatrix.e1_2 + local7.e1_2 * this.modelMatrix.e2_2 + local7.e1_3 * this.modelMatrix.e3_2;
        @Pc(142) float local142 = local7.e1_1 * this.modelMatrix.e1_3 + local7.e1_2 * this.modelMatrix.e2_3 + local7.e1_3 * this.modelMatrix.e3_3;
        @Pc(163) float local163 = local7.e2_1 * this.modelMatrix.e1_1 + local7.e2_2 * this.modelMatrix.e2_1 + local7.e2_3 * this.modelMatrix.e3_1;
        @Pc(184) float local184 = local7.e2_1 * this.modelMatrix.e1_2 + local7.e2_2 * this.modelMatrix.e2_2 + local7.e2_3 * this.modelMatrix.e3_2;
        @Pc(205) float local205 = local7.e2_1 * this.modelMatrix.e1_3 + local7.e2_2 * this.modelMatrix.e2_3 + local7.e2_3 * this.modelMatrix.e3_3;
        @Pc(226) float local226 = local7.e3_1 * this.modelMatrix.e1_1 + local7.e3_2 * this.modelMatrix.e2_1 + local7.e3_3 * this.modelMatrix.e3_1;
        @Pc(247) float local247 = local7.e3_1 * this.modelMatrix.e1_2 + local7.e3_2 * this.modelMatrix.e2_2 + local7.e3_3 * this.modelMatrix.e3_2;
        @Pc(268) float local268 = local7.e3_1 * this.modelMatrix.e1_3 + local7.e3_2 * this.modelMatrix.e2_3 + local7.e3_3 * this.modelMatrix.e3_3;
        @Pc(270) boolean local270 = false;
        @Pc(274) int local274 = this.toolkit.projectionCenterX;
        @Pc(278) int local278 = this.toolkit.projectionCenterY;
        @Pc(282) int local282 = this.toolkit.projectionScaleX;
        @Pc(286) int local286 = this.toolkit.projectionScaleY;
        @Pc(288) int local288 = Integer.MAX_VALUE;
        @Pc(290) int local290 = Integer.MIN_VALUE;
        @Pc(292) int local292 = Integer.MAX_VALUE;
        @Pc(294) int local294 = Integer.MIN_VALUE;
        this.useThreadBuffers(Thread.currentThread());
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        @Pc(312) int local312 = this.maxX - this.minX >> 1;
        @Pc(320) int local320 = this.maxY - this.minY >> 1;
        @Pc(328) int local328 = this.maxZ - this.minZ >> 1;
        @Pc(333) int local333 = this.minX + local312;
        @Pc(338) int local338 = this.minY + local320;
        @Pc(343) int local343 = this.minZ + local328;
        @Pc(349) int local349 = local333 - (local312 << sizeShift);
        @Pc(355) int local355 = local338 - (local320 << sizeShift);
        @Pc(361) int local361 = local343 - (local328 << sizeShift);
        @Pc(367) int local367 = local333 + (local312 << sizeShift);
        @Pc(373) int local373 = local338 + (local320 << sizeShift);
        @Pc(379) int local379 = local343 + (local328 << sizeShift);
        this.boundsCornerX[0] = local349;
        this.boundsCornerY[0] = local355;
        this.boundsCornerZ[0] = local361;
        this.boundsCornerX[1] = local367;
        this.boundsCornerY[1] = local355;
        this.boundsCornerZ[1] = local361;
        this.boundsCornerX[2] = local349;
        this.boundsCornerY[2] = local373;
        this.boundsCornerZ[2] = local361;
        this.boundsCornerX[3] = local367;
        this.boundsCornerY[3] = local373;
        this.boundsCornerZ[3] = local361;
        this.boundsCornerX[4] = local349;
        this.boundsCornerY[4] = local355;
        this.boundsCornerZ[4] = local379;
        this.boundsCornerX[5] = local367;
        this.boundsCornerY[5] = local355;
        this.boundsCornerZ[5] = local379;
        this.boundsCornerX[6] = local349;
        this.boundsCornerY[6] = local373;
        this.boundsCornerZ[6] = local379;
        this.boundsCornerX[7] = local367;
        this.boundsCornerY[7] = local373;
        this.boundsCornerZ[7] = local379;
        @Pc(534) float local534;
        @Pc(551) float local551;
        @Pc(568) float local568;
        @Pc(507) int local507;
        @Pc(512) int local512;
        @Pc(517) int local517;
        @Pc(592) int local592;
        @Pc(602) int local602;
        for (@Pc(501) int local501 = 0; local501 < 8; local501++) {
            local507 = this.boundsCornerX[local501];
            local512 = this.boundsCornerY[local501];
            local517 = this.boundsCornerZ[local501];
            local534 = local31 + local100 * (float) local507 + local121 * (float) local512 + local142 * (float) local517;
            local551 = local55 + local163 * (float) local507 + local184 * (float) local512 + local205 * (float) local517;
            local568 = local79 + local226 * (float) local507 + local247 * (float) local512 + local268 * (float) local517;
            if (local568 >= (float) this.toolkit.zNear) {
                if (orthoDepth > 0) {
                    local568 = (float) orthoDepth;
                }
                local592 = local274 + (int) (local534 * (float) local282 / local568);
                local602 = local278 + (int) (local551 * (float) local286 / local568);
                if (local592 < local288) {
                    local288 = local592;
                }
                if (local592 > local290) {
                    local290 = local592;
                }
                if (local602 < local292) {
                    local292 = local602;
                }
                if (local602 > local294) {
                    local294 = local602;
                }
                local270 = true;
            }
        }
        if (local270 && x > local288 && x < local290 && y > local292 && y < local294) {
            if (quick) {
                return true;
            }
            for (local592 = 0; local592 < this.vertexCount; local592++) {
                local507 = this.vertexX[local592];
                local512 = this.vertexY[local592];
                local517 = this.vertexZ[local592];
                local534 = local31 + local100 * (float) local507 + local121 * (float) local512 + local142 * (float) local517;
                local551 = local55 + local163 * (float) local507 + local184 * (float) local512 + local205 * (float) local517;
                local568 = local79 + local226 * (float) local507 + local247 * (float) local512 + local268 * (float) local517;
                if (local568 >= (float) this.toolkit.zNear) {
                    if (orthoDepth > 0) {
                        local568 = (float) orthoDepth;
                    }
                    this.screenX[local592] = local274 + (int) (local534 * (float) local282 / local568);
                    this.screenY[local592] = local278 + (int) (local551 * (float) local286 / local568);
                } else {
                    this.screenX[local592] = -999999;
                }
            }
            for (local602 = 0; local602 < this.faceCount; local602++) {
                if (this.screenX[this.faceA[local602]] != -999999 && this.screenX[this.faceB[local602]] != -999999 && this.screenX[this.faceC[local602]] != -999999 && this.pointInFaceBounds(x, y, this.screenY[this.faceA[local602]], this.screenY[this.faceB[local602]], this.screenY[this.faceC[local602]], this.screenX[this.faceA[local602]], this.screenX[this.faceB[local602]], this.screenX[this.faceC[local602]])) {
                    return true;
                }
            }
        }
        return false;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!rs;Lclient!rs;IZZ)Lclient!ka;")
    public Model copyTo(@OriginalArg(0) JavaModel target, @OriginalArg(1) JavaModel buffers, @OriginalArg(2) int functionMask, @OriginalArg(3) boolean reserve, @OriginalArg(4) boolean ensureLit) {
        target.boundsValid = this.boundsValid;
        if (this.boundsValid) {
            target.maxX = this.maxX;
            target.maxY = this.maxY;
            target.maxZ = this.maxZ;
            target.minX = this.minX;
            target.minY = this.minY;
            target.minZ = this.minZ;
            target.radius = this.radius;
            target.sphereRadius = this.sphereRadius;
        }
        target.ambient = this.ambient;
        target.contrast = this.contrast;
        target.vertexCount = this.vertexCount;
        target.maxVertex = this.maxVertex;
        target.faceCount = this.faceCount;
        target.billboardCount = this.billboardCount;
        if ((functionMask & 0x100) == 0) {
            target.transparent = this.transparent;
        } else {
            target.transparent = true;
        }
        target.movingTextures = this.movingTextures;
        @Pc(100) boolean local100 = (functionMask & 0x7) == 7 | (functionMask & 0x20) != 0;
        @Pc(113) boolean local113 = local100 || (functionMask & 0x1) != 0;
        @Pc(126) boolean local126 = local100 || (functionMask & 0x2) != 0;
        @Pc(145) boolean local145 = local100 || (functionMask & 0x4) != 0 || (functionMask & 0x10) != 0;
        @Pc(188) int local188;
        if (local113 || local126 || local145) {
            if (local113) {
                if (buffers.vertexX == null || buffers.vertexX.length < this.vertexCount) {
                    target.vertexX = buffers.vertexX = new int[this.vertexCount];
                } else {
                    target.vertexX = buffers.vertexX;
                }
                for (local188 = 0; local188 < this.vertexCount; local188++) {
                    target.vertexX[local188] = this.vertexX[local188];
                }
            } else {
                target.vertexX = this.vertexX;
            }
            if (local126) {
                if (buffers.vertexY == null || buffers.vertexY.length < this.vertexCount) {
                    target.vertexY = buffers.vertexY = new int[this.vertexCount];
                } else {
                    target.vertexY = buffers.vertexY;
                }
                for (local188 = 0; local188 < this.vertexCount; local188++) {
                    target.vertexY[local188] = this.vertexY[local188];
                }
            } else {
                target.vertexY = this.vertexY;
            }
            if (local145) {
                if (buffers.vertexZ == null || buffers.vertexZ.length < this.vertexCount) {
                    target.vertexZ = buffers.vertexZ = new int[this.vertexCount];
                } else {
                    target.vertexZ = buffers.vertexZ;
                }
                for (local188 = 0; local188 < this.vertexCount; local188++) {
                    target.vertexZ[local188] = this.vertexZ[local188];
                }
            } else {
                target.vertexZ = this.vertexZ;
            }
        } else {
            target.vertexX = this.vertexX;
            target.vertexY = this.vertexY;
            target.vertexZ = this.vertexZ;
        }
        if ((functionMask & 0x84080) == 0) {
            target.faceColour = this.faceColour;
        } else {
            if (buffers.faceColour == null || buffers.faceColour.length < this.faceCount) {
                local188 = this.faceCount;
                target.faceColour = buffers.faceColour = new short[local188];
            } else {
                target.faceColour = buffers.faceColour;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                target.faceColour[local188] = this.faceColour[local188];
            }
        }
        if ((functionMask & 0x97018) != 0) {
            target.lightingState = 0;
            target.faceColourA = target.faceColourB = target.faceColourC = null;
        } else if ((functionMask & 0x80) == 0) {
            if (ensureLit) {
                this.applyLighting(false);
            }
            target.faceColourA = this.faceColourA;
            target.faceColourB = this.faceColourB;
            target.faceColourC = this.faceColourC;
            target.lightingState = this.lightingState;
        } else {
            if (ensureLit) {
                this.applyLighting(false);
            }
            if (this.faceColourA != null) {
                if (buffers.faceColourA == null || buffers.faceColourA.length < this.faceCount) {
                    local188 = this.faceCount;
                    target.faceColourA = buffers.faceColourA = new int[local188];
                    target.faceColourB = buffers.faceColourB = new int[local188];
                    target.faceColourC = buffers.faceColourC = new int[local188];
                } else {
                    target.faceColourA = buffers.faceColourA;
                    target.faceColourB = buffers.faceColourB;
                    target.faceColourC = buffers.faceColourC;
                }
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    target.faceColourA[local188] = this.faceColourA[local188];
                    target.faceColourB[local188] = this.faceColourB[local188];
                    target.faceColourC[local188] = this.faceColourC[local188];
                }
            }
            target.lightingState = this.lightingState;
        }
        if ((functionMask & 0x100) == 0) {
            target.faceAlpha = this.faceAlpha;
        } else {
            if (buffers.faceAlpha == null || buffers.faceAlpha.length < this.faceCount) {
                local188 = this.faceCount;
                target.faceAlpha = buffers.faceAlpha = new byte[local188];
            } else {
                target.faceAlpha = buffers.faceAlpha;
            }
            if (this.faceAlpha == null) {
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    target.faceAlpha[local188] = 0;
                }
            } else {
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    target.faceAlpha[local188] = this.faceAlpha[local188];
                }
            }
        }
        if ((functionMask & 0x8) == 0 && (functionMask & 0x10) == 0) {
            if (ensureLit) {
                this.ensureNormals();
            }
            target.vertexNormals = this.vertexNormals;
            target.faceNormals = this.faceNormals;
        } else {
            if (buffers.vertexNormals == null || buffers.vertexNormals.length < this.maxVertex) {
                local188 = this.maxVertex;
                target.vertexNormals = buffers.vertexNormals = new VertexNormal[local188];
            } else {
                target.vertexNormals = buffers.vertexNormals;
            }
            if (this.vertexNormals == null) {
                target.vertexNormals = null;
            } else {
                for (local188 = 0; local188 < this.maxVertex; local188++) {
                    target.vertexNormals[local188] = new VertexNormal(this.vertexNormals[local188]);
                }
            }
            if (this.faceNormals == null) {
                target.faceNormals = null;
            } else {
                if (buffers.faceNormals == null || buffers.faceNormals.length < this.faceCount) {
                    local188 = this.faceCount;
                    target.faceNormals = buffers.faceNormals = new FaceNormal[local188];
                } else {
                    target.faceNormals = buffers.faceNormals;
                }
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    target.faceNormals[local188] = this.faceNormals[local188] == null ? null : new FaceNormal(this.faceNormals[local188]);
                }
            }
        }
        if ((functionMask & 0x8000) == 0) {
            target.faceTextures = this.faceTextures;
        } else if (this.faceTextures == null) {
            target.faceTextures = null;
        } else {
            if (buffers.faceTextures == null || buffers.faceTextures.length < this.faceCount) {
                local188 = this.faceCount;
                target.faceTextures = buffers.faceTextures = new short[local188];
            } else {
                target.faceTextures = buffers.faceTextures;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                target.faceTextures[local188] = this.faceTextures[local188];
            }
        }
        if ((functionMask & 0x10000) == 0) {
            target.shadingType = this.shadingType;
        } else if (this.shadingType == null) {
            target.shadingType = null;
        } else {
            if (buffers.shadingType == null || buffers.shadingType.length < this.faceCount) {
                local188 = reserve ? this.faceCount + 100 : this.faceCount;
                target.shadingType = buffers.shadingType = new byte[local188];
            } else {
                target.shadingType = buffers.shadingType;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                target.shadingType[local188] = this.shadingType[local188];
            }
        }
        @Pc(900) int local900;
        if ((functionMask & 0xC580) == 0) {
            target.billboardAttributes = this.billboardAttributes;
        } else if (buffers.billboardAttributes == null || buffers.billboardAttributes.length < this.billboardCount) {
            local188 = this.billboardCount;
            target.billboardAttributes = buffers.billboardAttributes = new JavaBillboardAttributes[local188];
            for (local900 = 0; local900 < this.billboardCount; local900++) {
                target.billboardAttributes[local900] = this.billboardAttributes[local900].method5574();
            }
        } else {
            target.billboardAttributes = buffers.billboardAttributes;
            for (local188 = 0; local188 < this.billboardCount; local188++) {
                target.billboardAttributes[local188].method5573(this.billboardAttributes[local188]);
            }
        }
        if (this.texCoordU == null || (functionMask & 0x10) == 0) {
            target.texCoordU = this.texCoordU;
            target.texCoordV = this.texCoordV;
        } else {
            if (buffers.texCoordU == null || buffers.texCoordU.length < this.faceCount) {
                local188 = reserve ? this.faceCount + 100 : this.faceCount;
                target.texCoordU = buffers.texCoordU = new float[local188][3];
            } else {
                target.texCoordU = buffers.texCoordU;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                if (this.texCoordU[local188] != null) {
                    target.texCoordU[local188][0] = this.texCoordU[local188][0];
                    target.texCoordU[local188][1] = this.texCoordU[local188][1];
                    target.texCoordU[local188][2] = this.texCoordU[local188][2];
                }
            }
            if (buffers.texCoordV == null || buffers.texCoordV.length < this.faceCount) {
                local900 = reserve ? this.faceCount + 100 : this.faceCount;
                target.texCoordV = buffers.texCoordV = new float[local900][3];
            } else {
                target.texCoordV = buffers.texCoordV;
            }
            for (local900 = 0; local900 < this.faceCount; local900++) {
                if (this.texCoordV[local900] != null) {
                    target.texCoordV[local900][0] = this.texCoordV[local900][0];
                    target.texCoordV[local900][1] = this.texCoordV[local900][1];
                    target.texCoordV[local900][2] = this.texCoordV[local900][2];
                }
            }
        }
        target.vertexLabels = this.vertexLabels;
        target.faceLabels = this.faceLabels;
        target.billboardLabels = this.billboardLabels;
        target.originModels = this.originModels;
        target.faceOriginModels = this.faceOriginModels;
        target.facePriority = this.facePriority;
        target.faceA = this.faceA;
        target.faceB = this.faceB;
        target.faceC = this.faceC;
        target.emitters = this.emitters;
        target.effectors = this.effectors;
        target.billboardFaces = this.billboardFaces;
        target.faceIndices = this.faceIndices;
        target.functionMask = functionMask;
        return target;
    }

    @OriginalMember(owner = "client!rs", name = "q", descriptor = "()V")
    public void rotate270() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.vertexCount; local5++) {
                @Pc(11) int local11 = this.vertexZ[local5];
                this.vertexZ[local5] = this.vertexX[local5];
                this.vertexX[local5] = -local11;
            }
            this.invalidateNormals();
        }
    }

    @OriginalMember(owner = "client!rs", name = "k", descriptor = "(I)V")
    @Override
    public void k(@OriginalArg(0) int angle) {
        if ((this.functionMask & 0xD) != 13) {
            throw new IllegalStateException();
        } else if (this.vertexNormals == null) {
            this.a(angle);
        } else if (angle == 4096) {
            this.rotate90WithNormals();
        } else if (angle == 8192) {
            this.rotate180WithNormals();
        } else if (angle == 12288) {
            this.rotate270WithNormals();
        } else {
            @Pc(40) int local40 = Trig1.SIN[angle];
            @Pc(44) int local44 = Trig1.COS[angle];
            synchronized (this) {
                @Pc(67) int local67;
                for (@Pc(50) int local50 = 0; local50 < this.maxVertex; local50++) {
                    local67 = this.vertexZ[local50] * local40 + this.vertexX[local50] * local44 >> 14;
                    this.vertexZ[local50] = this.vertexZ[local50] * local44 - this.vertexX[local50] * local40 >> 14;
                    this.vertexX[local50] = local67;
                    if (this.vertexNormals[local50] != null) {
                        local67 = this.vertexNormals[local50].z * local40 + this.vertexNormals[local50].x * local44 >> 14;
                        this.vertexNormals[local50].z = this.vertexNormals[local50].z * local44 - this.vertexNormals[local50].x * local40 >> 14;
                        this.vertexNormals[local50].x = local67;
                    }
                }
                @Pc(178) int local178;
                if (this.faceNormals != null) {
                    for (local67 = 0; local67 < this.faceCount; local67++) {
                        if (this.faceNormals[local67] != null) {
                            local178 = this.faceNormals[local67].z * local40 + this.faceNormals[local67].x * local44 >> 14;
                            this.faceNormals[local67].z = this.faceNormals[local67].z * local44 - this.faceNormals[local67].x * local40 >> 14;
                            this.faceNormals[local67].x = local178;
                        }
                    }
                }
                for (local67 = this.maxVertex; local67 < this.vertexCount; local67++) {
                    local178 = this.vertexZ[local67] * local40 + this.vertexX[local67] * local44 >> 14;
                    this.vertexZ[local67] = this.vertexZ[local67] * local44 - this.vertexX[local67] * local40 >> 14;
                    this.vertexX[local67] = local178;
                }
                this.lightingState = 0;
                this.boundsValid = false;
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(ZZII)V")
    public void drawFacesArgb(@OriginalArg(0) boolean nearClipped, @OriginalArg(1) boolean fogged, @OriginalArg(2) int minDepth, @OriginalArg(3) int depthRange) {
        @Pc(4) int i;
        if (this.billboardFaces != null) {
            i = 0;
            while (i < this.billboardCount) {
                @Pc(10) JavaBillboardFace billboard = this.billboardFaces[i];
                this.faceBillboard[billboard.anInt6139] = i++;
            }
        }
        if (!this.transparent && this.billboardFaces == null) {
            for (i = 0; i < this.faceCount; i++) {
                this.drawFaceArgb(i, nearClipped, fogged);
            }
        } else if ((this.functionMask & 0x100) == 0 && this.faceIndices != null) {
            for (i = 0; i < this.faceCount; i++) {
                @Pc(51) short face = this.faceIndices[i];
                this.drawFaceArgb(face, nearClipped, fogged);
            }
        } else {
            for (i = 0; i < this.faceCount; i++) {
                if (!this.isTranslucent(i) && !this.hasBillboard(i)) {
                    this.drawFaceArgb(i, nearClipped, fogged);
                }
            }
            @Pc(95) int local95;
            if (this.facePriority == null) {
                for (local95 = 0; local95 < this.faceCount; local95++) {
                    if (this.isTranslucent(local95) || this.hasBillboard(local95)) {
                        this.drawFaceArgb(local95, nearClipped, fogged);
                    }
                }
            } else {
                for (local95 = 0; local95 < 12; local95++) {
                    for (@Pc(125) int face = 0; face < this.faceCount; face++) {
                        if (this.facePriority[face] == local95 && (this.isTranslucent(face) || this.hasBillboard(face))) {
                            this.drawFaceArgb(face, nearClipped, fogged);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "f", descriptor = "(I)V")
    public void drawTriangleArgb(@OriginalArg(0) int face) {
        @Pc(4) short a = this.faceA[face];
        @Pc(9) short b = this.faceB[face];
        @Pc(14) short c = this.faceC[face];
        if (this.faceTextures != null && this.faceTextures[face] != -1) {
            @Pc(181) int alphaBits = -16777216;
            if (this.faceAlpha != null) {
                alphaBits = 255 - (this.faceAlpha[face] & 0xFF) << 24;
            }
            if (this.faceColourC[face] == -1) {
                @Pc(210) int argb = alphaBits | this.faceColourA[face] & 0xFFFFFF;
                this.rasterizer.renderTexturedTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], argb, argb, argb, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            } else {
                this.rasterizer.renderTexturedTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], alphaBits | this.faceColourA[face] & 0xFFFFFF, alphaBits | this.faceColourB[face] & 0xFFFFFF, alphaBits | this.faceColourC[face] & 0xFFFFFF, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            }
            return;
        }
        if (this.faceAlpha == null) {
            this.rasterizer.alpha = 0;
        } else {
            this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
        }
        if (this.faceColourC[face] == -1) {
            this.rasterizer.renderFlatTriangleArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]);
        } else {
            this.rasterizer.renderTriangleHslArgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], (float) (this.faceColourA[face] & 0xFFFF), (float) (this.faceColourB[face] & 0xFFFF), (float) (this.faceColourC[face] & 0xFFFF));
        }
    }

    @OriginalMember(owner = "client!rs", name = "WA", descriptor = "()I")
    @Override
    public int WA() {
        return this.ambient;
    }

    @OriginalMember(owner = "client!rs", name = "j", descriptor = "(I)V")
    public void drawClippedTriangleArgb(@OriginalArg(0) int face) {
        @Pc(1) int count = 0;
        @Pc(5) int zNear = this.toolkit.zNear;
        @Pc(10) short a = this.faceA[face];
        @Pc(15) short b = this.faceB[face];
        @Pc(20) short c = this.faceC[face];
        @Pc(25) int depthA = this.cameraZ[a];
        @Pc(30) int depthB = this.cameraZ[b];
        @Pc(35) int depthC = this.cameraZ[c];
        if (this.faceAlpha == null) {
            this.rasterizer.alpha = 0;
        } else {
            this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
        }
        @Pc(98) int local98;
        @Pc(103) int local103;
        @Pc(110) int local110;
        @Pc(123) int local123;
        if (depthA >= zNear) {
            this.clippedX[0] = this.screenX[a];
            this.clippedY[0] = this.screenY[a];
            this.clippedZ[0] = this.screenZ[a];
            count++;
            this.clippedColour[0] = this.faceColourA[face] & 0xFFFF;
        } else {
            local98 = this.cameraX[a];
            local103 = this.cameraY[a];
            local110 = this.faceColourA[face] & 0xFFFF;
            if (depthC >= zNear) {
                local123 = (zNear - depthA) * (65536 / (depthC - depthA));
                this.clippedX[0] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[c] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[0] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[c] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[0] = zNear;
                count++;
                this.clippedColour[0] = local110 + (((this.faceColourC[face] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (depthB >= zNear) {
                local123 = (zNear - depthA) * (65536 / (depthB - depthA));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[b] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[b] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourB[face] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (depthB >= zNear) {
            this.clippedX[count] = this.screenX[b];
            this.clippedY[count] = this.screenY[b];
            this.clippedZ[count] = this.screenZ[b];
            this.clippedColour[count++] = this.faceColourB[face] & 0xFFFF;
        } else {
            local98 = this.cameraX[b];
            local103 = this.cameraY[b];
            local110 = this.faceColourB[face] & 0xFFFF;
            if (depthA >= zNear) {
                local123 = (zNear - depthB) * (65536 / (depthA - depthB));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[a] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[a] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourA[face] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (depthC >= zNear) {
                local123 = (zNear - depthB) * (65536 / (depthC - depthB));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[c] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[c] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourC[face] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (depthC >= zNear) {
            this.clippedX[count] = this.screenX[c];
            this.clippedY[count] = this.screenY[c];
            this.clippedZ[count] = this.screenZ[c];
            this.clippedColour[count++] = this.faceColourC[face] & 0xFFFF;
        } else {
            local98 = this.cameraX[c];
            local103 = this.cameraY[c];
            local110 = this.faceColourC[face] & 0xFFFF;
            if (depthB >= zNear) {
                local123 = (zNear - depthC) * (65536 / (depthB - depthC));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[b] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[b] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourB[face] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (depthA >= zNear) {
                local123 = (zNear - depthC) * (65536 / (depthA - depthC));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[a] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[a] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourA[face] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        local98 = this.clippedX[0];
        local103 = this.clippedX[1];
        local110 = this.clippedX[2];
        local123 = this.clippedY[0];
        @Pc(783) int local783 = this.clippedY[1];
        @Pc(788) int local788 = this.clippedY[2];
        depthA = this.clippedZ[0];
        depthB = this.clippedZ[1];
        depthC = this.clippedZ[2];
        this.rasterizer.clamp = false;
        @Pc(938) int local938;
        @Pc(961) int local961;
        if (count == 3) {
            if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.threadResource.rasterWidth || local103 > this.threadResource.rasterWidth || local110 > this.threadResource.rasterWidth) {
                this.rasterizer.clamp = true;
            }
            if (this.faceTextures != null && this.faceTextures[face] != -1) {
                local938 = -16777216;
                if (this.faceAlpha != null) {
                    local938 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
                }
                local961 = local938 | this.faceColourA[face] & 0xFFFFFF;
                if (this.faceColourC[face] == -1) {
                    this.rasterizer.renderTexturedTriangleArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
                } else {
                    this.rasterizer.renderTexturedTriangleArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
                }
            } else if (this.faceColourC[face] == -1) {
                this.rasterizer.renderFlatTriangleArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]);
            } else {
                this.rasterizer.renderTriangleHslArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, (float) this.clippedColour[0], (float) this.clippedColour[1], (float) this.clippedColour[2]);
            }
        }
        if (count != 4) {
            return;
        }
        if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.threadResource.rasterWidth || local103 > this.threadResource.rasterWidth || local110 > this.threadResource.rasterWidth || this.clippedX[3] < 0 || this.clippedX[3] > this.threadResource.rasterWidth) {
            this.rasterizer.clamp = true;
        }
        if (this.faceTextures == null || this.faceTextures[face] == -1) {
            if (this.faceColourC[face] == -1) {
                local938 = ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF];
                this.rasterizer.renderFlatTriangleArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, local938);
                this.rasterizer.renderFlatTriangleArgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthB, (float) this.clippedZ[3], local938);
                return;
            } else {
                this.rasterizer.renderTriangleHslArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, (float) this.clippedColour[0], (float) this.clippedColour[1], (float) this.clippedColour[2]);
                this.rasterizer.renderTriangleHslArgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthB, (float) this.clippedZ[3], (float) this.clippedColour[0], (float) this.clippedColour[2], (float) this.clippedColour[3]);
                return;
            }
        }
        local938 = -16777216;
        if (this.faceAlpha != null) {
            local938 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
        }
        local961 = local938 | this.faceColourA[face] & 0xFFFFFF;
        if (this.faceColourC[face] == -1) {
            this.rasterizer.renderTexturedTriangleArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            this.rasterizer.renderTexturedTriangleArgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthC, (float) this.clippedZ[3], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            return;
        }
        this.rasterizer.renderTexturedTriangleArgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
        this.rasterizer.renderTexturedTriangleArgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthC, (float) this.clippedZ[3], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Ljava/lang/Thread;)V")
    public void useThreadBuffers(@OriginalArg(0) Thread thread) {
        @Pc(4) JavaThreadResource resource = this.toolkit.threadResource(thread);
        this.rasterizer = resource.rasterizer;
        if (resource == this.threadResource) {
            return;
        }
        this.threadResource = resource;
        this.worldY = this.threadResource.worldY;
        this.cameraX = this.threadResource.cameraX;
        this.cameraY = this.threadResource.cameraY;
        this.cameraZ = this.threadResource.cameraZ;
        this.screenX = this.threadResource.vertexScreenX;
        this.screenY = this.threadResource.vertexScreenY;
        this.screenZ = this.threadResource.vertexScreenZ;
        this.boundsCornerX = this.threadResource.boundsCornerX;
        this.boundsCornerY = this.threadResource.boundsCornerY;
        this.boundsCornerZ = this.threadResource.boundsCornerZ;
        this.clippedX = this.threadResource.clippedX;
        this.clippedY = this.threadResource.clippedY;
        this.clippedZ = this.threadResource.clippedZ;
        this.clippedColour = this.threadResource.clippedColour;
        this.mergeStamps = this.threadResource.mergeStamps;
        this.otherMergeStamps = this.threadResource.otherMergeStamps;
        this.faceBillboard = this.threadResource.faceBillboard;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(IIIIIIII)Z")
    public boolean pointInFaceBounds(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int y0, @OriginalArg(3) int y1, @OriginalArg(4) int y2, @OriginalArg(5) int x0, @OriginalArg(6) int x1, @OriginalArg(7) int x2) {
        if (y < y0 && y < y1 && y < y2) {
            return false;
        } else if (y > y0 && y > y1 && y > y2) {
            return false;
        } else if (x < x0 && x < x1 && x < x2) {
            return false;
        } else {
            return x <= x0 || x <= x1 || x <= x2;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!tt;IZ)V")
    @Override
    public void transform(@OriginalArg(0) Matrix matrix, @OriginalArg(1) int originMask, @OriginalArg(2) boolean relative) {
        if (this.originModels == null) {
            return;
        }
        @Pc(7) int[] local7 = new int[3];
        for (@Pc(9) int local9 = 0; local9 < this.maxVertex; local9++) {
            if ((originMask & this.originModels[local9]) != 0) {
                if (relative) {
                    matrix.projectRelative(this.vertexX[local9], this.vertexY[local9], this.vertexZ[local9], local7);
                } else {
                    matrix.project(this.vertexX[local9], this.vertexY[local9], this.vertexZ[local9], local7);
                }
                this.vertexX[local9] = local7[0];
                this.vertexY[local9] = local7[1];
                this.vertexZ[local9] = local7[2];
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "i", descriptor = "(I)V")
    public void drawClippedTriangleRgb(@OriginalArg(0) int face) {
        @Pc(1) int count = 0;
        @Pc(5) int zNear = this.toolkit.zNear;
        @Pc(10) short a = this.faceA[face];
        @Pc(15) short b = this.faceB[face];
        @Pc(20) short c = this.faceC[face];
        @Pc(25) int depthA = this.cameraZ[a];
        @Pc(30) int depthB = this.cameraZ[b];
        @Pc(35) int depthC = this.cameraZ[c];
        if (this.faceAlpha == null) {
            this.rasterizer.alpha = 0;
        } else {
            this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
        }
        @Pc(98) int local98;
        @Pc(103) int local103;
        @Pc(110) int local110;
        @Pc(123) int local123;
        if (depthA >= zNear) {
            this.clippedX[0] = this.screenX[a];
            this.clippedY[0] = this.screenY[a];
            this.clippedZ[0] = this.screenZ[a];
            count++;
            this.clippedColour[0] = this.faceColourA[face] & 0xFFFF;
        } else {
            local98 = this.cameraX[a];
            local103 = this.cameraY[a];
            local110 = this.faceColourA[face] & 0xFFFF;
            if (depthC >= zNear) {
                local123 = (zNear - depthA) * (65536 / (depthC - depthA));
                this.clippedX[0] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[c] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[0] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[c] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[0] = zNear;
                count++;
                this.clippedColour[0] = local110 + (((this.faceColourC[face] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (depthB >= zNear) {
                local123 = (zNear - depthA) * (65536 / (depthB - depthA));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[b] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[b] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourB[face] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (depthB >= zNear) {
            this.clippedX[count] = this.screenX[b];
            this.clippedY[count] = this.screenY[b];
            this.clippedZ[count] = this.screenZ[b];
            this.clippedColour[count++] = this.faceColourB[face] & 0xFFFF;
        } else {
            local98 = this.cameraX[b];
            local103 = this.cameraY[b];
            local110 = this.faceColourB[face] & 0xFFFF;
            if (depthA >= zNear) {
                local123 = (zNear - depthB) * (65536 / (depthA - depthB));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[a] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[a] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourA[face] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (depthC >= zNear) {
                local123 = (zNear - depthB) * (65536 / (depthC - depthB));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[c] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[c] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourC[face] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (depthC >= zNear) {
            this.clippedX[count] = this.screenX[c];
            this.clippedY[count] = this.screenY[c];
            this.clippedZ[count] = this.screenZ[c];
            this.clippedColour[count++] = this.faceColourC[face] & 0xFFFF;
        } else {
            local98 = this.cameraX[c];
            local103 = this.cameraY[c];
            local110 = this.faceColourC[face] & 0xFFFF;
            if (depthB >= zNear) {
                local123 = (zNear - depthC) * (65536 / (depthB - depthC));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[b] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[b] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourB[face] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (depthA >= zNear) {
                local123 = (zNear - depthC) * (65536 / (depthA - depthC));
                this.clippedX[count] = this.threadResource.rasterMinX + (local98 + ((this.cameraX[a] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / zNear;
                this.clippedY[count] = this.threadResource.rasterMinY + (local103 + ((this.cameraY[a] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / zNear;
                this.clippedZ[count] = zNear;
                this.clippedColour[count++] = local110 + (((this.faceColourA[face] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        local98 = this.clippedX[0];
        local103 = this.clippedX[1];
        local110 = this.clippedX[2];
        local123 = this.clippedY[0];
        @Pc(783) int local783 = this.clippedY[1];
        @Pc(788) int local788 = this.clippedY[2];
        depthA = this.clippedZ[0];
        depthB = this.clippedZ[1];
        depthC = this.clippedZ[2];
        this.rasterizer.clamp = false;
        @Pc(938) int local938;
        @Pc(961) int local961;
        if (count == 3) {
            if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.threadResource.rasterWidth || local103 > this.threadResource.rasterWidth || local110 > this.threadResource.rasterWidth) {
                this.rasterizer.clamp = true;
            }
            if (this.faceTextures != null && this.faceTextures[face] != -1) {
                local938 = -16777216;
                if (this.faceAlpha != null) {
                    local938 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
                }
                local961 = local938 | this.faceColourA[face] & 0xFFFFFF;
                if (this.faceColourC[face] == -1) {
                    this.rasterizer.renderTexturedTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
                } else {
                    this.rasterizer.renderTexturedTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
                }
            } else if (this.faceColourC[face] == -1) {
                this.rasterizer.renderFlatTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]);
            } else {
                this.rasterizer.renderTriangleHslRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, (float) this.clippedColour[0], (float) this.clippedColour[1], (float) this.clippedColour[2]);
            }
        }
        if (count != 4) {
            return;
        }
        if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.threadResource.rasterWidth || local103 > this.threadResource.rasterWidth || local110 > this.threadResource.rasterWidth || this.clippedX[3] < 0 || this.clippedX[3] > this.threadResource.rasterWidth) {
            this.rasterizer.clamp = true;
        }
        if (this.faceTextures == null || this.faceTextures[face] == -1) {
            if (this.faceColourC[face] == -1) {
                local938 = ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF];
                this.rasterizer.renderFlatTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, local938);
                this.rasterizer.renderFlatTriangleRgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthB, (float) this.clippedZ[3], local938);
                return;
            } else {
                this.rasterizer.renderTriangleHslRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, (float) this.clippedColour[0], (float) this.clippedColour[1], (float) this.clippedColour[2]);
                this.rasterizer.renderTriangleHslRgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthB, (float) this.clippedZ[3], (float) this.clippedColour[0], (float) this.clippedColour[2], (float) this.clippedColour[3]);
                return;
            }
        }
        local938 = -16777216;
        if (this.faceAlpha != null) {
            local938 = 255 - (this.faceAlpha[face] & 0xFF) << 24;
        }
        local961 = local938 | this.faceColourA[face] & 0xFFFFFF;
        if (this.faceColourC[face] == -1) {
            this.rasterizer.renderTexturedTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            this.rasterizer.renderTexturedTriangleRgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthC, (float) this.clippedZ[3], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            return;
        }
        this.rasterizer.renderTexturedTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) depthA, (float) depthB, (float) depthC, this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
        this.rasterizer.renderTexturedTriangleRgb((float) local123, (float) local788, (float) this.clippedY[3], (float) local98, (float) local110, (float) this.clippedX[3], (float) depthA, (float) depthC, (float) this.clippedZ[3], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], local961, local961, local961, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(IZZ)V")
    public void drawFaceRgb(@OriginalArg(0) int face, @OriginalArg(1) boolean nearClipped, @OriginalArg(2) boolean fogged) {
        if (this.faceColourC[face] == -2) {
            return;
        }
        @Pc(12) short a = this.faceA[face];
        @Pc(17) short b = this.faceB[face];
        @Pc(22) short c = this.faceC[face];
        @Pc(27) int screenXA = this.screenX[a];
        @Pc(32) int screenXB = this.screenX[b];
        @Pc(37) int screenXC = this.screenX[c];
        @Pc(59) int local59;
        if (nearClipped && (screenXA == -5000 || screenXB == -5000 || screenXC == -5000)) {
            local59 = this.cameraX[a];
            @Pc(64) int local64 = this.cameraX[b];
            @Pc(69) int local69 = this.cameraX[c];
            @Pc(74) int local74 = this.cameraY[a];
            @Pc(79) int local79 = this.cameraY[b];
            @Pc(84) int local84 = this.cameraY[c];
            @Pc(89) int local89 = this.cameraZ[a];
            @Pc(94) int local94 = this.cameraZ[b];
            @Pc(99) int local99 = this.cameraZ[c];
            @Pc(103) int local103 = local59 - local64;
            @Pc(107) int local107 = local69 - local64;
            @Pc(111) int local111 = local74 - local79;
            @Pc(115) int local115 = local84 - local79;
            @Pc(119) int local119 = local89 - local94;
            @Pc(123) int local123 = local99 - local94;
            @Pc(131) int local131 = local111 * local123 - local119 * local115;
            @Pc(139) int local139 = local119 * local107 - local103 * local123;
            @Pc(147) int local147 = local103 * local115 - local111 * local107;
            if (local64 * local131 + local79 * local139 + local94 * local147 > 0) {
                this.drawClippedTriangleRgb(face);
                return;
            }
        } else if (this.faceBillboard[face] != -1 || (screenXA - screenXB) * (this.screenY[c] - this.screenY[b]) - (this.screenY[a] - this.screenY[b]) * (screenXC - screenXB) > 0) {
            if (screenXA >= 0 && screenXB >= 0 && screenXC >= 0 && screenXA <= this.threadResource.rasterWidth && screenXB <= this.threadResource.rasterWidth && screenXC <= this.threadResource.rasterWidth) {
                this.rasterizer.clamp = false;
            } else {
                this.rasterizer.clamp = true;
            }
            if (fogged) {
                local59 = this.faceBillboard[face];
                if (local59 == -1 || !this.billboardFaces[local59].aBoolean464) {
                    this.drawFoggedTriangleRgb(face);
                }
                return;
            }
            local59 = this.faceBillboard[face];
            if (local59 != -1) {
                @Pc(280) JavaBillboardFace local280 = this.billboardFaces[local59];
                @Pc(285) JavaBillboardAttributes local285 = this.billboardAttributes[local59];
                if (!local280.aBoolean464) {
                    this.drawTriangleRgb(face);
                }
                this.toolkit.drawBillboardRgb(local285.anInt6221, local285.anInt6227, local285.anInt6224, local285.anInt6232, local285.anInt6220, local285.anInt6231, local280.aShort72 & 0xFFFF, local285.anInt6225, local280.aByte98, local280.aByte97);
                return;
            }
            this.drawTriangleRgb(face);
        }
    }

    @OriginalMember(owner = "client!rs", name = "C", descriptor = "(I)V")
    @Override
    public void C(@OriginalArg(0) int ambient) {
        if ((this.functionMask & 0x1000) != 4096) {
            throw new IllegalStateException();
        }
        this.ambient = ambient;
        this.lightingState = 0;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "()Z")
    @Override
    public boolean loadedTextures() {
        if (this.faceTextures == null) {
            return true;
        }
        for (@Pc(7) int local7 = 0; local7 < this.faceTextures.length; local7++) {
            if (this.faceTextures[local7] != -1 && !this.toolkit.textureAvailable(this.faceTextures[local7])) {
                return false;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!tt;Lclient!ima;I)V")
    @Override
    public void render(@OriginalArg(0) Matrix matrix, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int flags) {
        this.draw(matrix, cylinder, -1, flags);
    }

    @OriginalMember(owner = "client!rs", name = "FA", descriptor = "(I)V")
    @Override
    public void FA(@OriginalArg(0) int angle) {
        if ((this.functionMask & 0x6) != 6) {
            throw new IllegalStateException();
        }
        @Pc(14) int local14 = Trig1.SIN[angle];
        @Pc(18) int local18 = Trig1.COS[angle];
        synchronized (this) {
            for (@Pc(24) int local24 = 0; local24 < this.vertexCount; local24++) {
                @Pc(41) int local41 = this.vertexY[local24] * local18 - this.vertexZ[local24] * local14 >> 14;
                this.vertexZ[local24] = this.vertexY[local24] * local14 + this.vertexZ[local24] * local18 >> 14;
                this.vertexY[local24] = local41;
            }
            this.invalidateNormals();
        }
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "()[B")
    @Override
    public byte[] getFaceAlphas() {
        return this.faceAlpha;
    }

    @OriginalMember(owner = "client!rs", name = "t", descriptor = "()V")
    public void ensureNormals() {
        if (this.lightingState != 0 || this.vertexNormals != null) {
            return;
        }
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.calculateNormals();
            }
        } else {
            this.calculateNormals();
        }
    }

    @OriginalMember(owner = "client!rs", name = "da", descriptor = "()I")
    @Override
    public int da() {
        return this.contrast;
    }

    @OriginalMember(owner = "client!rs", name = "ua", descriptor = "()I")
    @Override
    public int ua() {
        return this.functionMask;
    }

    @OriginalMember(owner = "client!rs", name = "e", descriptor = "(I)I")
    public int clampLightness(@OriginalArg(0) int lightness) {
        if (lightness < 2) {
            lightness = 2;
        } else if (lightness > 126) {
            lightness = 126;
        }
        return lightness;
    }

    @OriginalMember(owner = "client!rs", name = "V", descriptor = "()I")
    @Override
    public int V() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.minX;
    }

    @OriginalMember(owner = "client!rs", name = "k", descriptor = "()V")
    public void calculateBounds() {
        if (this.boundsValid) {
            return;
        }
        @Pc(6) int maxRadiusSq = 0;
        @Pc(8) int maxSphereSq = 0;
        @Pc(10) int lowX = 32767;
        @Pc(12) int lowY = 32767;
        @Pc(14) int lowZ = 32767;
        @Pc(16) int highX = -32768;
        @Pc(18) int highY = -32768;
        @Pc(20) int highZ = -32768;
        for (@Pc(22) int i = 0; i < this.maxVertex; i++) {
            @Pc(28) int vx = this.vertexX[i];
            @Pc(33) int vy = this.vertexY[i];
            @Pc(38) int vz = this.vertexZ[i];
            if (vx < lowX) {
                lowX = vx;
            }
            if (vx > highX) {
                highX = vx;
            }
            if (vy < lowY) {
                lowY = vy;
            }
            if (vy > highY) {
                highY = vy;
            }
            if (vz < lowZ) {
                lowZ = vz;
            }
            if (vz > highZ) {
                highZ = vz;
            }
            @Pc(76) int distSq = vx * vx + vz * vz;
            if (distSq > maxRadiusSq) {
                maxRadiusSq = distSq;
            }
            distSq += vy * vy;
            if (distSq > maxSphereSq) {
                maxSphereSq = distSq;
            }
        }
        this.minX = (short) lowX;
        this.maxX = (short) highX;
        this.minY = (short) lowY;
        this.maxY = (short) highY;
        this.minZ = (short) lowZ;
        this.maxZ = (short) highZ;
        this.radius = (short) (int) (Math.sqrt(maxRadiusSq) + 0.99D);
        this.sphereRadius = (short) (int) (Math.sqrt(maxSphereSq) + 0.99D);
        this.boundsValid = true;
    }

    @OriginalMember(owner = "client!rs", name = "d", descriptor = "(I)Z")
    public boolean hasBillboard(@OriginalArg(0) int face) {
        if (this.faceBillboard == null) {
            return false;
        } else {
            return this.faceBillboard[face] != -1;
        }
    }

    @OriginalMember(owner = "client!rs", name = "h", descriptor = "()V")
    public void relightColours() {
        for (@Pc(1) int face = 0; face < this.faceCount; face++) {
            @Pc(13) short texture = this.faceTextures == null ? -1 : this.faceTextures[face];
            if (texture == -1) {
                @Pc(23) int hsl = this.faceColour[face] & 0xFFFF;
                @Pc(32) int lightness = (hsl & 0x7F) * this.ambient >> 7;
                @Pc(40) short hsv = ColourUtils.hslToHsv(hsl & 0xFFFFFF80 | lightness);
                @Pc(53) int shade;
                if (this.faceColourC[face] == -1) {
                    shade = this.faceColourA[face] & 0xFFFE0000;
                    this.faceColourA[face] = shade | Static244.scaleHslLightness(shade >> 17, hsv);
                } else if (this.faceColourC[face] != -2) {
                    shade = this.faceColourA[face] & 0xFFFE0000;
                    this.faceColourA[face] = shade | Static244.scaleHslLightness(shade >> 17, hsv);
                    shade = this.faceColourB[face] & 0xFFFE0000;
                    this.faceColourB[face] = shade | Static244.scaleHslLightness(shade >> 17, hsv);
                    shade = this.faceColourC[face] & 0xFFFE0000;
                    this.faceColourC[face] = shade | Static244.scaleHslLightness(shade >> 17, hsv);
                }
            }
        }
        this.lightingState = 2;
    }

    @OriginalMember(owner = "client!rs", name = "fa", descriptor = "()I")
    @Override
    public int fa() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.minY;
    }

    @OriginalMember(owner = "client!rs", name = "d", descriptor = "()V")
    @Override
    protected void method7494() {
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                super.locked = false;
                this.notifyAll();
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(Lclient!tt;Lclient!ima;II)V")
    public void draw(@OriginalArg(0) Matrix matrix, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int orthoDepth, @OriginalArg(3) int flags) {
        if (this.maxVertex < 1) {
            return;
        }
        this.modelMatrix = (JavaMatrix) matrix;
        @Pc(13) JavaMatrix local13 = this.toolkit.camera;
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        @Pc(22) boolean local22 = false;
        if (this.modelMatrix.e1_1 == 16384.0F && this.modelMatrix.e1_2 == 0.0F && this.modelMatrix.e1_3 == 0.0F && this.modelMatrix.e2_1 == 0.0F && this.modelMatrix.e2_2 == 16384.0F && this.modelMatrix.e2_3 == 0.0F && this.modelMatrix.e3_1 == 0.0F && this.modelMatrix.e3_2 == 0.0F && this.modelMatrix.e3_3 == 16384.0F) {
            local22 = true;
        }
        @Pc(118) float local118 = local13.tz + local13.e3_1 * this.modelMatrix.tx + local13.e3_2 * this.modelMatrix.ty + local13.e3_3 * this.modelMatrix.tz;
        @Pc(145) float local145 = local22 ? local13.e3_2 : local13.e3_1 * this.modelMatrix.e1_2 + local13.e3_2 * this.modelMatrix.e2_2 + local13.e3_3 * this.modelMatrix.e3_2;
        @Pc(154) int local154 = (int) (local118 + (float) this.minY * local145);
        @Pc(163) int local163 = (int) (local118 + (float) this.maxY * local145);
        @Pc(171) int local171;
        @Pc(176) int local176;
        if (local154 > local163) {
            local171 = local163 - this.radius;
            local176 = local154 + this.radius;
        } else {
            local171 = local154 - this.radius;
            local176 = local163 + this.radius;
        }
        if (local171 >= this.toolkit.zFar || local176 <= this.toolkit.zNear) {
            return;
        }
        @Pc(225) float local225 = local13.tx + local13.e1_1 * this.modelMatrix.tx + local13.e1_2 * this.modelMatrix.ty + local13.e1_3 * this.modelMatrix.tz;
        @Pc(252) float local252 = local22 ? local13.e1_2 : local13.e1_1 * this.modelMatrix.e1_2 + local13.e1_2 * this.modelMatrix.e2_2 + local13.e1_3 * this.modelMatrix.e3_2;
        @Pc(261) int local261 = (int) (local225 + (float) this.minY * local252);
        @Pc(270) int local270 = (int) (local225 + (float) this.maxY * local252);
        @Pc(282) int local282;
        @Pc(291) int local291;
        if (local261 > local270) {
            local282 = (local270 - this.radius) * this.toolkit.projectionScaleX;
            local291 = (local261 + this.radius) * this.toolkit.projectionScaleX;
        } else {
            local282 = (local261 - this.radius) * this.toolkit.projectionScaleX;
            local291 = (local270 + this.radius) * this.toolkit.projectionScaleX;
        }
        if (orthoDepth == -1) {
            if (local282 / local176 >= this.toolkit.viewX2) {
                return;
            }
            if (local291 / local176 <= this.toolkit.viewX1) {
                return;
            }
        } else if (local282 / orthoDepth >= this.toolkit.viewX2) {
            return;
        } else if (local291 / orthoDepth <= this.toolkit.viewX1) {
            return;
        }
        @Pc(375) float local375 = local13.ty + local13.e2_1 * this.modelMatrix.tx + local13.e2_2 * this.modelMatrix.ty + local13.e2_3 * this.modelMatrix.tz;
        @Pc(402) float local402 = local22 ? local13.e2_2 : local13.e2_1 * this.modelMatrix.e1_2 + local13.e2_2 * this.modelMatrix.e2_2 + local13.e2_3 * this.modelMatrix.e3_2;
        @Pc(411) int local411 = (int) (local375 + (float) this.minY * local402);
        @Pc(420) int local420 = (int) (local375 + (float) this.maxY * local402);
        @Pc(432) int local432;
        @Pc(441) int local441;
        if (local411 > local420) {
            local432 = (local420 - this.radius) * this.toolkit.projectionScaleY;
            local441 = (local411 + this.radius) * this.toolkit.projectionScaleY;
        } else {
            local432 = (local411 - this.radius) * this.toolkit.projectionScaleY;
            local441 = (local420 + this.radius) * this.toolkit.projectionScaleY;
        }
        if (orthoDepth == -1) {
            if (local432 / local176 >= this.toolkit.viewY2) {
                return;
            }
            if (local441 / local176 <= this.toolkit.viewY1) {
                return;
            }
        } else if (local432 / orthoDepth >= this.toolkit.viewY2) {
            return;
        } else if (local441 / orthoDepth <= this.toolkit.viewY1) {
            return;
        }
        @Pc(506) float local506;
        @Pc(509) float local509;
        @Pc(512) float local512;
        @Pc(515) float local515;
        @Pc(518) float local518;
        @Pc(521) float local521;
        if (local22) {
            local506 = local13.e1_1;
            local509 = local13.e2_1;
            local512 = local13.e3_1;
            local515 = local13.e1_3;
            local518 = local13.e2_3;
            local521 = local13.e3_3;
        } else {
            local506 = local13.e1_1 * this.modelMatrix.e1_1 + local13.e1_2 * this.modelMatrix.e2_1 + local13.e1_3 * this.modelMatrix.e3_1;
            local509 = local13.e2_1 * this.modelMatrix.e1_1 + local13.e2_2 * this.modelMatrix.e2_1 + local13.e2_3 * this.modelMatrix.e3_1;
            local512 = local13.e3_1 * this.modelMatrix.e1_1 + local13.e3_2 * this.modelMatrix.e2_1 + local13.e3_3 * this.modelMatrix.e3_1;
            local515 = local13.e1_1 * this.modelMatrix.e1_3 + local13.e1_2 * this.modelMatrix.e2_3 + local13.e1_3 * this.modelMatrix.e3_3;
            local518 = local13.e2_1 * this.modelMatrix.e1_3 + local13.e2_2 * this.modelMatrix.e2_3 + local13.e2_3 * this.modelMatrix.e3_3;
            local521 = local13.e3_1 * this.modelMatrix.e1_3 + local13.e3_2 * this.modelMatrix.e2_3 + local13.e3_3 * this.modelMatrix.e3_3;
        }
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                while (this.rendering) {
                    try {
                        this.wait();
                    } catch (@Pc(662) InterruptedException local662) {
                        /* empty */
                    }
                }
                this.rendering = true;
            }
        }
        this.useThreadBuffers(Thread.currentThread());
        if ((flags & 0x2) == 0) {
            this.rasterizer.setWireframe(false);
        } else {
            this.rasterizer.setWireframe(true);
        }
        @Pc(694) boolean local694 = false;
        @Pc(704) boolean local704 = local171 <= this.toolkit.zNear;
        @Pc(721) boolean local721 = local704 || this.emitters != null || this.effectors != null;
        this.threadResource.rasterWidth = this.rasterizer.width;
        this.threadResource.rasterMinX = this.rasterizer.minX;
        this.threadResource.rasterMinY = this.rasterizer.minY;
        @Pc(743) int local743 = this.toolkit.projectionScaleX;
        @Pc(747) int local747 = this.toolkit.projectionScaleY;
        @Pc(751) int local751 = this.toolkit.zNear;
        @Pc(789) float local789;
        @Pc(806) float local806;
        @Pc(823) float local823;
        @Pc(762) int local762;
        @Pc(767) int local767;
        @Pc(772) int local772;
        @Pc(756) int local756;
        @Pc(942) int local942;
        @Pc(948) JavaBillboardFace local948;
        @Pc(953) JavaBillboardAttributes local953;
        @Pc(959) short local959;
        @Pc(965) short local965;
        @Pc(971) short local971;
        if (orthoDepth == -1) {
            for (local756 = 0; local756 < this.vertexCount; local756++) {
                local762 = this.vertexX[local756];
                local767 = this.vertexY[local756];
                local772 = this.vertexZ[local756];
                local789 = local225 + local506 * (float) local762 + local252 * (float) local767 + local515 * (float) local772;
                local806 = local375 + local509 * (float) local762 + local402 * (float) local767 + local518 * (float) local772;
                local823 = local118 + local512 * (float) local762 + local145 * (float) local767 + local521 * (float) local772;
                this.screenZ[local756] = (int) local823;
                if (local823 >= (float) local751) {
                    this.screenX[local756] = this.threadResource.rasterMinX + (int) (local789 * (float) local743 / local823);
                    this.screenY[local756] = this.threadResource.rasterMinY + (int) (local806 * (float) local747 / local823);
                } else {
                    this.screenX[local756] = -5000;
                    local694 = true;
                }
                if (local721) {
                    this.cameraX[local756] = (int) local789;
                    this.cameraY[local756] = (int) local806;
                    this.cameraZ[local756] = (int) local823;
                }
                if (this.threadResource.water) {
                    this.worldY[local756] = (int) (this.modelMatrix.ty + this.modelMatrix.e2_1 * (float) local762 + this.modelMatrix.e2_2 * (float) local767 + this.modelMatrix.e2_3 * (float) local772);
                }
            }
            if (this.billboardFaces != null) {
                for (local942 = 0; local942 < this.billboardCount; local942++) {
                    local948 = this.billboardFaces[local942];
                    local953 = this.billboardAttributes[local942];
                    local959 = this.faceA[local948.anInt6139];
                    local965 = this.faceB[local948.anInt6139];
                    local971 = this.faceC[local948.anInt6139];
                    local762 = (this.vertexX[local959] + this.vertexX[local965] + this.vertexX[local971]) / 3;
                    local767 = (this.vertexY[local959] + this.vertexY[local965] + this.vertexY[local971]) / 3;
                    local772 = (this.vertexZ[local959] + this.vertexZ[local965] + this.vertexZ[local971]) / 3;
                    local789 = (float) local953.anInt6222 + local225 + local506 * (float) local762 + local252 * (float) local767 + local515 * (float) local772;
                    local806 = (float) local953.anInt6229 + local375 + local509 * (float) local762 + local402 * (float) local767 + local518 * (float) local772;
                    local823 = local118 + local512 * (float) local762 + local145 * (float) local767 + local521 * (float) local772;
                    if (local823 > (float) this.toolkit.zNear) {
                        local953.anInt6221 = this.toolkit.projectionCenterX + (int) (local789 * (float) local743 / local823);
                        local953.anInt6227 = this.toolkit.projectionCenterY + (int) (local806 * (float) local747 / local823);
                        local953.anInt6224 = (int) local823 - local948.anInt6140;
                        local953.anInt6232 = (int) ((float) (local953.anInt6223 * local948.aShort71 * local743) / (local823 * 128.0F));
                        local953.anInt6220 = (int) ((float) (local953.anInt6226 * local948.aShort73 * local747) / (local823 * 128.0F));
                    } else {
                        local953.anInt6232 = local953.anInt6220 = 0;
                    }
                }
            }
        } else {
            for (local756 = 0; local756 < this.vertexCount; local756++) {
                local762 = this.vertexX[local756];
                local767 = this.vertexY[local756];
                local772 = this.vertexZ[local756];
                local789 = local225 + local506 * (float) local762 + local252 * (float) local767 + local515 * (float) local772;
                local806 = local375 + local509 * (float) local762 + local402 * (float) local767 + local518 * (float) local772;
                local823 = local118 + local512 * (float) local762 + local145 * (float) local767 + local521 * (float) local772;
                this.screenZ[local756] = (int) local823;
                this.screenX[local756] = this.threadResource.rasterMinX + (int) (local789 * (float) local743 / (float) orthoDepth);
                this.screenY[local756] = this.threadResource.rasterMinY + (int) (local806 * (float) local747 / (float) orthoDepth);
                if (local721) {
                    this.cameraX[local756] = (int) local789;
                    this.cameraY[local756] = (int) local806;
                    this.cameraZ[local756] = orthoDepth;
                }
                if (this.threadResource.water) {
                    this.worldY[local756] = (int) (this.modelMatrix.ty + this.modelMatrix.e2_1 * (float) local762 + this.modelMatrix.e2_2 * (float) local767 + this.modelMatrix.e2_3 * (float) local772);
                }
            }
            if (this.billboardFaces != null) {
                for (local942 = 0; local942 < this.billboardCount; local942++) {
                    local948 = this.billboardFaces[local942];
                    local953 = this.billboardAttributes[local942];
                    local959 = this.faceA[local948.anInt6139];
                    local965 = this.faceB[local948.anInt6139];
                    local971 = this.faceC[local948.anInt6139];
                    local762 = (this.vertexX[local959] + this.vertexX[local965] + this.vertexX[local971]) / 3;
                    local767 = (this.vertexY[local959] + this.vertexY[local965] + this.vertexY[local971]) / 3;
                    local772 = (this.vertexZ[local959] + this.vertexZ[local965] + this.vertexZ[local971]) / 3;
                    local789 = local225 + local506 * (float) local762 + local252 * (float) local767 + local515 * (float) local772;
                    local806 = local375 + local509 * (float) local762 + local402 * (float) local767 + local518 * (float) local772;
                    local953.anInt6221 = this.toolkit.projectionCenterX + (int) (local789 * (float) local743 / (float) orthoDepth);
                    local953.anInt6227 = this.toolkit.projectionCenterY + (int) (local806 * (float) local747 / (float) orthoDepth);
                    local953.anInt6224 = orthoDepth - local948.anInt6140;
                    local953.anInt6232 = local953.anInt6223 * local948.aShort71 * local743 / (orthoDepth << 7);
                    local953.anInt6220 = local953.anInt6226 * local948.aShort73 * local747 / (orthoDepth << 7);
                }
            }
        }
        @Pc(1543) boolean local1543;
        if (cylinder != null) {
            local1543 = false;
            @Pc(1545) boolean local1545 = true;
            @Pc(1553) int local1553 = this.minX + this.maxX >> 1;
            @Pc(1561) int local1561 = this.minZ + this.maxZ >> 1;
            @Pc(1566) short local1566 = this.minY;
            local789 = local225 + local506 * (float) local1553 + local252 * (float) local1566 + local515 * (float) local1561;
            local806 = local375 + local509 * (float) local1553 + local402 * (float) local1566 + local518 * (float) local1561;
            local823 = local118 + local512 * (float) local1553 + local145 * (float) local1566 + local521 * (float) local1561;
            if (local823 >= (float) local751) {
                @Pc(1627) int local1627 = (int) local823;
                if (orthoDepth != -1) {
                    local1627 = orthoDepth;
                }
                cylinder.anInt4504 = this.toolkit.projectionCenterX + (int) (local789 * (float) local743 / (float) local1627);
                cylinder.anInt4505 = this.toolkit.projectionCenterY + (int) (local806 * (float) local747 / (float) local1627);
            } else {
                local1543 = true;
            }
            local1566 = this.maxY;
            @Pc(1687) float local1687 = local225 + local506 * (float) local1553 + local252 * (float) local1566 + local515 * (float) local1561;
            @Pc(1704) float local1704 = local375 + local509 * (float) local1553 + local402 * (float) local1566 + local518 * (float) local1561;
            @Pc(1721) float local1721 = local118 + local512 * (float) local1553 + local145 * (float) local1566 + local521 * (float) local1561;
            @Pc(1729) int local1729;
            if (local1721 >= (float) local751) {
                local1729 = (int) local1721;
                if (orthoDepth != -1) {
                    local1729 = orthoDepth;
                }
                cylinder.anInt4501 = this.toolkit.projectionCenterX + (int) (local1687 * (float) local743 / (float) local1729);
                cylinder.anInt4503 = this.toolkit.projectionCenterY + (int) (local1704 * (float) local747 / (float) local1729);
            } else {
                local1543 = true;
            }
            if (local1543) {
                if (local823 < (float) local751 && local1721 < (float) local751) {
                    local1545 = false;
                } else {
                    @Pc(1809) int local1809;
                    @Pc(1818) int local1818;
                    @Pc(1820) int local1820;
                    @Pc(1800) float local1800;
                    if (local823 < (float) local751) {
                        local1800 = (local1721 - (float) this.toolkit.zNear) / (local1721 - local823);
                        local1809 = (int) (local1687 + (local1687 - local789) * local1800);
                        local1818 = (int) (local1704 + (local1704 - local806) * local1800);
                        local1820 = local751;
                        if (orthoDepth != -1) {
                            local1820 = orthoDepth;
                        }
                        cylinder.anInt4504 = this.toolkit.projectionCenterX + local1809 * local743 / local1820;
                        cylinder.anInt4505 = this.toolkit.projectionCenterY + local1818 * local747 / local1820;
                    } else if (local1721 < (float) local751) {
                        local1800 = (local823 - (float) local751) / (local823 - local1721);
                        local1809 = (int) (local789 + (local789 - local1687) * local1800);
                        local1818 = (int) (local806 + (local806 - local1704) * local1800);
                        local1820 = local751;
                        if (orthoDepth != -1) {
                            local1820 = orthoDepth;
                        }
                        cylinder.anInt4504 = this.toolkit.projectionCenterX + local1809 * local743 / local1820;
                        cylinder.anInt4505 = this.toolkit.projectionCenterY + local1818 * local747 / local1820;
                    }
                }
            }
            if (local1545) {
                if (local823 > local1721) {
                    local1729 = (int) local823;
                    if (orthoDepth != -1) {
                        local1729 = orthoDepth;
                    }
                    cylinder.anInt4502 = this.toolkit.projectionCenterX + (int) ((local789 + (float) this.radius) * (float) local743 / (float) local1729) - cylinder.anInt4504;
                } else {
                    local1729 = (int) local1721;
                    if (orthoDepth != -1) {
                        local1729 = orthoDepth;
                    }
                    cylinder.anInt4502 = this.toolkit.projectionCenterX + (int) ((local1687 + (float) this.radius) * (float) local743 / (float) local1729) - cylinder.anInt4501;
                }
                cylinder.aBoolean352 = true;
            }
        }
        this.applyLighting(true);
        this.rasterizer.fastScanline = (flags & 0x1) == 0;
        this.rasterizer.halfBlend = false;
        try {
            local1543 = (flags & 0x4) != 0;
            if (local1543) {
                this.drawFacesArgb(local694, this.threadResource.fogActive && local176 > this.threadResource.fogPlane || this.threadResource.water, local171, local176 - local171);
            } else {
                this.drawFacesRgb(local694, this.threadResource.fogActive && local176 > this.threadResource.fogPlane || this.threadResource.water, local171, local176 - local171);
            }
        } catch (@Pc(2068) Exception local2068) {
            /* empty */
        }
        if (this.billboardFaces != null) {
            for (local756 = 0; local756 < this.faceCount; local756++) {
                this.faceBillboard[local756] = -1;
            }
        }
        this.rasterizer = null;
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.rendering = false;
                this.notifyAll();
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "s", descriptor = "(I)V")
    @Override
    public void s(@OriginalArg(0) int functionMask) {
        if (this.toolkit.threadCount <= 1) {
            if ((this.functionMask & 0x10000) == 65536 && (functionMask & 0x10000) == 0) {
                this.applyLighting(true);
            }
            this.functionMask = functionMask;
            return;
        }
        synchronized (this) {
            if ((this.functionMask & 0x10000) == 65536 && (functionMask & 0x10000) == 0) {
                this.applyLighting(true);
            }
            this.functionMask = functionMask;
        }
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(ZZII)V")
    public void drawFacesRgb(@OriginalArg(0) boolean nearClipped, @OriginalArg(1) boolean fogged, @OriginalArg(2) int minDepth, @OriginalArg(3) int depthRange) {
        @Pc(4) int i;
        if (this.billboardFaces != null) {
            i = 0;
            while (i < this.billboardCount) {
                @Pc(10) JavaBillboardFace billboard = this.billboardFaces[i];
                this.faceBillboard[billboard.anInt6139] = i++;
            }
        }
        if (!this.transparent && this.billboardFaces == null) {
            for (i = 0; i < this.faceCount; i++) {
                this.drawFaceRgb(i, nearClipped, fogged);
            }
        } else if ((this.functionMask & 0x100) == 0 && this.faceIndices != null) {
            for (i = 0; i < this.faceCount; i++) {
                @Pc(51) short face = this.faceIndices[i];
                this.drawFaceRgb(face, nearClipped, fogged);
            }
        } else {
            for (i = 0; i < this.faceCount; i++) {
                if (!this.isTranslucent(i) && !this.hasBillboard(i)) {
                    this.drawFaceRgb(i, nearClipped, fogged);
                }
            }
            @Pc(95) int local95;
            if (this.facePriority == null) {
                for (local95 = 0; local95 < this.faceCount; local95++) {
                    if (this.isTranslucent(local95) || this.hasBillboard(local95)) {
                        this.drawFaceRgb(local95, nearClipped, fogged);
                    }
                }
            } else {
                for (local95 = 0; local95 < 12; local95++) {
                    for (@Pc(125) int face = 0; face < this.faceCount; face++) {
                        if (this.facePriority[face] == local95 && (this.isTranslucent(face) || this.hasBillboard(face))) {
                            this.drawFaceRgb(face, nearClipped, fogged);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "ma", descriptor = "()I")
    @Override
    public int ma() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.sphereRadius;
    }

    @OriginalMember(owner = "client!rs", name = "aa", descriptor = "(SS)V")
    @Override
    public void aa(@OriginalArg(0) short src, @OriginalArg(1) short dest) {
        if (this.faceTextures == null) {
            return;
        }
        if (!this.movingTextures && dest >= 0) {
            @Pc(20) TextureMetrics local20 = this.toolkit.textureSource.getMetrics(dest & 0xFFFF);
            if (local20.speedU != 0 || local20.speedV != 0) {
                this.movingTextures = true;
            }
        }
        for (@Pc(35) int local35 = 0; local35 < this.faceCount; local35++) {
            if (this.faceTextures[local35] == src) {
                this.faceTextures[local35] = dest;
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "n", descriptor = "()V")
    public void rotate180WithNormals() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.maxVertex; local5++) {
                this.vertexX[local5] = -this.vertexX[local5];
                this.vertexZ[local5] = -this.vertexZ[local5];
                if (this.vertexNormals[local5] != null) {
                    this.vertexNormals[local5].x = -this.vertexNormals[local5].x;
                    this.vertexNormals[local5].z = -this.vertexNormals[local5].z;
                }
            }
            @Pc(65) int local65;
            if (this.faceNormals != null) {
                for (local65 = 0; local65 < this.faceCount; local65++) {
                    if (this.faceNormals[local65] != null) {
                        this.faceNormals[local65].x = -this.faceNormals[local65].x;
                        this.faceNormals[local65].z = -this.faceNormals[local65].z;
                    }
                }
            }
            for (local65 = this.maxVertex; local65 < this.vertexCount; local65++) {
                this.vertexX[local65] = -this.vertexX[local65];
                this.vertexZ[local65] = -this.vertexZ[local65];
            }
            this.lightingState = 0;
            this.boundsValid = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(ISI)I")
    public int shadeTexturedRgb(@OriginalArg(0) int hsl, @OriginalArg(1) short texture, @OriginalArg(2) int lightness) {
        @Pc(6) int rgb = ColourUtils.HSL_TO_RGB[this.shadeHsl(hsl, lightness)];
        @Pc(15) TextureMetrics metrics = this.toolkit.textureSource.getMetrics(texture & 0xFFFF);
        @Pc(20) int local20 = metrics.alpha & 0xFF;
        @Pc(26) int local26;
        @Pc(38) int local38;
        if (local20 != 0) {
            local26 = lightness * 131586;
            if (local20 == 256) {
                rgb = local26;
            } else {
                local38 = 256 - local20;
                rgb = ((local26 & 0xFF00FF) * local20 + (rgb & 0xFF00FF) * local38 & 0xFF00FF00) + ((local26 & 0xFF00) * local20 + (rgb & 0xFF00) * local38 & 0xFF0000) >> 8;
            }
        }
        local26 = metrics.aByte57 & 0xFF;
        if (local26 != 0) {
            local26 += 256;
            @Pc(84) int local84 = (rgb >> 16 & 0xFF) * local26;
            if (local84 > 65535) {
                local84 = 65535;
            }
            local38 = (rgb >> 8 & 0xFF) * local26;
            if (local38 > 65535) {
                local38 = 65535;
            }
            @Pc(108) int local108 = (rgb & 0xFF) * local26;
            if (local108 > 65535) {
                local108 = 65535;
            }
            rgb = ((local84 & 0xFF00) << 8) + (local38 & 0xFF00) + (local108 >> 8);
        }
        return rgb;
    }

    @OriginalMember(owner = "client!rs", name = "VA", descriptor = "(I)V")
    @Override
    public void VA(@OriginalArg(0) int angle) {
        if ((this.functionMask & 0x3) != 3) {
            throw new IllegalStateException();
        }
        @Pc(14) int local14 = Trig1.SIN[angle];
        @Pc(18) int local18 = Trig1.COS[angle];
        synchronized (this) {
            for (@Pc(24) int local24 = 0; local24 < this.vertexCount; local24++) {
                @Pc(41) int local41 = this.vertexY[local24] * local14 + this.vertexX[local24] * local18 >> 14;
                this.vertexY[local24] = this.vertexY[local24] * local18 - this.vertexX[local24] * local14 >> 14;
                this.vertexX[local24] = local41;
            }
            this.invalidateNormals();
        }
    }

    @OriginalMember(owner = "client!rs", name = "s", descriptor = "()V")
    public void rotate270WithNormals() {
        synchronized (this) {
            @Pc(11) int local11;
            for (@Pc(5) int local5 = 0; local5 < this.maxVertex; local5++) {
                local11 = this.vertexZ[local5];
                this.vertexZ[local5] = this.vertexX[local5];
                this.vertexX[local5] = -local11;
                if (this.vertexNormals[local5] != null) {
                    local11 = this.vertexNormals[local5].z;
                    this.vertexNormals[local5].z = this.vertexNormals[local5].x;
                    this.vertexNormals[local5].x = -local11;
                }
            }
            @Pc(77) int local77;
            if (this.faceNormals != null) {
                for (local11 = 0; local11 < this.faceCount; local11++) {
                    if (this.faceNormals[local11] != null) {
                        local77 = this.faceNormals[local11].z;
                        this.faceNormals[local11].z = this.faceNormals[local11].x;
                        this.faceNormals[local11].x = -local77;
                    }
                }
            }
            for (local11 = this.maxVertex; local11 < this.vertexCount; local11++) {
                local77 = this.vertexZ[local11];
                this.vertexZ[local11] = this.vertexX[local11];
                this.vertexX[local11] = -local77;
            }
            this.lightingState = 0;
            this.boundsValid = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "G", descriptor = "()I")
    @Override
    public int G() {
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        return this.maxZ;
    }

    @OriginalMember(owner = "client!rs", name = "l", descriptor = "(I)V")
    public void drawTriangleRgb(@OriginalArg(0) int face) {
        @Pc(4) short a = this.faceA[face];
        @Pc(9) short b = this.faceB[face];
        @Pc(14) short c = this.faceC[face];
        if (this.faceTextures != null && this.faceTextures[face] != -1) {
            @Pc(181) int alphaBits = -16777216;
            if (this.faceAlpha != null) {
                alphaBits = 255 - (this.faceAlpha[face] & 0xFF) << 24;
            }
            if (this.faceColourC[face] == -1) {
                @Pc(210) int argb = alphaBits | this.faceColourA[face] & 0xFFFFFF;
                this.rasterizer.renderTexturedTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], argb, argb, argb, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            } else {
                this.rasterizer.renderTexturedTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], this.texCoordU[face][0], this.texCoordU[face][1], this.texCoordU[face][2], this.texCoordV[face][0], this.texCoordV[face][1], this.texCoordV[face][2], alphaBits | this.faceColourA[face] & 0xFFFFFF, alphaBits | this.faceColourB[face] & 0xFFFFFF, alphaBits | this.faceColourC[face] & 0xFFFFFF, this.threadResource.fogColour, 0, 0, 0, this.faceTextures[face]);
            }
            return;
        }
        if (this.faceAlpha == null) {
            this.rasterizer.alpha = 0;
        } else {
            this.rasterizer.alpha = this.faceAlpha[face] & 0xFF;
        }
        if (this.faceColourC[face] == -1) {
            this.rasterizer.renderFlatTriangleRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], ColourUtils.HSV_TO_RGB[this.faceColourA[face] & 0xFFFF]);
        } else {
            this.rasterizer.renderTriangleHslRgb((float) this.screenY[a], (float) this.screenY[b], (float) this.screenY[c], (float) this.screenX[a], (float) this.screenX[b], (float) this.screenX[c], (float) this.screenZ[a], (float) this.screenZ[b], (float) this.screenZ[c], (float) (this.faceColourA[face] & 0xFFFF), (float) (this.faceColourB[face] & 0xFFFF), (float) (this.faceColourC[face] & 0xFFFF));
        }
    }

    @OriginalMember(owner = "client!rs", name = "r", descriptor = "()Z")
    @Override
    public boolean r() {
        return this.movingTextures;
    }

    @OriginalMember(owner = "client!rs", name = "NA", descriptor = "()Z")
    @Override
    protected boolean NA() {
        if (this.vertexLabels == null) {
            return false;
        } else {
            this.pivotX = 0;
            this.pivotY = 0;
            this.pivotZ = 0;
            return true;
        }
    }

    @OriginalMember(owner = "client!rs", name = "p", descriptor = "(IILclient!s;Lclient!s;III)V")
    @Override
    public void p(@OriginalArg(0) int hillType, @OriginalArg(1) int hillValue, @OriginalArg(2) Ground floor, @OriginalArg(3) Ground ceiling, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z) {
        if (hillType == 3) {
            if ((this.functionMask & 0x7) != 7) {
                throw new IllegalStateException();
            }
        } else if ((this.functionMask & 0x2) != 2) {
            throw new IllegalStateException();
        }
        if (!this.boundsValid) {
            this.calculateBounds();
        }
        @Pc(38) int local38 = x + this.minX;
        @Pc(43) int local43 = x + this.maxX;
        @Pc(48) int local48 = z + this.minZ;
        @Pc(53) int local53 = z + this.maxZ;
        if (hillType != 4 && (local38 < 0 || local43 + floor.tileSize >> floor.tileSizeShift >= floor.sizeX || local48 < 0 || local53 + floor.tileSize >> floor.tileSizeShift >= floor.sizeZ)) {
            return;
        }
        @Pc(94) int[][] local94 = floor.tileHeights;
        @Pc(96) int[][] local96 = null;
        if (ceiling != null) {
            local96 = ceiling.tileHeights;
        }
        if (hillType == 4 || hillType == 5) {
            if (ceiling == null) {
                return;
            }
            if (local38 < 0 || local43 + ceiling.tileSize >> ceiling.tileSizeShift >= ceiling.sizeX || local48 < 0 || local53 + ceiling.tileSize >> ceiling.tileSizeShift >= ceiling.sizeZ) {
                return;
            }
        } else {
            local38 >>= floor.tileSizeShift;
            local43 = local43 + floor.tileSize - 1 >> floor.tileSizeShift;
            local48 >>= floor.tileSizeShift;
            local53 = local53 + floor.tileSize - 1 >> floor.tileSizeShift;
            if (local94[local38][local48] == y && local94[local43][local48] == y && local94[local38][local53] == y && local94[local43][local53] == y) {
                return;
            }
        }
        synchronized (this) {
            @Pc(226) int local226;
            @Pc(228) int local228;
            @Pc(236) int local236;
            @Pc(243) int local243;
            @Pc(247) int local247;
            @Pc(251) int local251;
            @Pc(256) int local256;
            @Pc(261) int local261;
            @Pc(285) int local285;
            @Pc(313) int local313;
            @Pc(327) int local327;
            @Pc(472) int local472;
            if (hillType == 1) {
                local226 = floor.tileSize - 1;
                for (local228 = 0; local228 < this.maxVertex; local228++) {
                    local236 = this.vertexX[local228] + x;
                    local243 = this.vertexZ[local228] + z;
                    local247 = local236 & local226;
                    local251 = local243 & local226;
                    local256 = local236 >> floor.tileSizeShift;
                    local261 = local243 >> floor.tileSizeShift;
                    local285 = local94[local256][local261] * (floor.tileSize - local247) + local94[local256 + 1][local261] * local247 >> floor.tileSizeShift;
                    local313 = local94[local256][local261 + 1] * (floor.tileSize - local247) + local94[local256 + 1][local261 + 1] * local247 >> floor.tileSizeShift;
                    local327 = local285 * (floor.tileSize - local251) + local313 * local251 >> floor.tileSizeShift;
                    this.vertexY[local228] = this.vertexY[local228] + local327 - y;
                }
                for (local236 = this.maxVertex; local236 < this.vertexCount; local236++) {
                    local243 = this.vertexX[local236] + x;
                    local247 = this.vertexZ[local236] + z;
                    local251 = local243 & local226;
                    local256 = local247 & local226;
                    local261 = local243 >> floor.tileSizeShift;
                    local285 = local247 >> floor.tileSizeShift;
                    if (local261 >= 0 && local261 < local94.length - 1 && local285 >= 0 && local285 < local94[0].length - 1) {
                        local313 = local94[local261][local285] * (floor.tileSize - local251) + local94[local261 + 1][local285] * local251 >> floor.tileSizeShift;
                        local327 = local94[local261][local285 + 1] * (floor.tileSize - local251) + local94[local261 + 1][local285 + 1] * local251 >> floor.tileSizeShift;
                        local472 = local313 * (floor.tileSize - local256) + local327 * local256 >> floor.tileSizeShift;
                        this.vertexY[local236] = this.vertexY[local236] + local472 - y;
                    }
                }
            } else {
                @Pc(784) int local784;
                if (hillType == 2) {
                    local226 = floor.tileSize - 1;
                    for (local228 = 0; local228 < this.maxVertex; local228++) {
                        local236 = (this.vertexY[local228] << 16) / this.minY;
                        if (local236 < hillValue) {
                            local243 = this.vertexX[local228] + x;
                            local247 = this.vertexZ[local228] + z;
                            local251 = local243 & local226;
                            local256 = local247 & local226;
                            local261 = local243 >> floor.tileSizeShift;
                            local285 = local247 >> floor.tileSizeShift;
                            local313 = local94[local261][local285] * (floor.tileSize - local251) + local94[local261 + 1][local285] * local251 >> floor.tileSizeShift;
                            local327 = local94[local261][local285 + 1] * (floor.tileSize - local251) + local94[local261 + 1][local285 + 1] * local251 >> floor.tileSizeShift;
                            local472 = local313 * (floor.tileSize - local256) + local327 * local256 >> floor.tileSizeShift;
                            this.vertexY[local228] += (local472 - y) * (hillValue - local236) / hillValue;
                        } else {
                            this.vertexY[local228] = this.vertexY[local228];
                        }
                    }
                    for (local236 = this.maxVertex; local236 < this.vertexCount; local236++) {
                        local243 = (this.vertexY[local236] << 16) / this.minY;
                        if (local243 < hillValue) {
                            local247 = this.vertexX[local236] + x;
                            local251 = this.vertexZ[local236] + z;
                            local256 = local247 & local226;
                            local261 = local251 & local226;
                            local285 = local247 >> floor.tileSizeShift;
                            local313 = local251 >> floor.tileSizeShift;
                            if (local285 >= 0 && local285 < floor.sizeX - 1 && local313 >= 0 && local313 < floor.sizeZ - 1) {
                                local327 = local94[local285][local313] * (floor.tileSize - local256) + local94[local285 + 1][local313] * local256 >> floor.tileSizeShift;
                                local472 = local94[local285][local313 + 1] * (floor.tileSize - local256) + local94[local285 + 1][local313 + 1] * local256 >> floor.tileSizeShift;
                                local784 = local327 * (floor.tileSize - local261) + local472 * local261 >> floor.tileSizeShift;
                                this.vertexY[local236] += (local784 - y) * (hillValue - local243) / hillValue;
                            }
                        } else {
                            this.vertexY[local236] = this.vertexY[local236];
                        }
                    }
                } else if (hillType == 3) {
                    local226 = (hillValue & 0xFF) * 4;
                    local228 = (hillValue >> 8 & 0xFF) * 4;
                    local236 = (hillValue >> 16 & 0xFF) << 6;
                    local243 = (hillValue >> 24 & 0xFF) << 6;
                    if (x - (local226 >> 1) < 0 || x + (local226 >> 1) + floor.tileSize >= floor.sizeX << floor.tileSizeShift || z - (local228 >> 1) < 0 || z + (local228 >> 1) + floor.tileSize >= floor.sizeZ << floor.tileSizeShift) {
                        return;
                    }
                    this.method7490(y, local236, x, local226, z, local228, floor, local243);
                } else if (hillType == 4) {
                    local226 = ceiling.tileSize - 1;
                    local228 = this.maxY - this.minY;
                    for (local236 = 0; local236 < this.maxVertex; local236++) {
                        local243 = this.vertexX[local236] + x;
                        local247 = this.vertexZ[local236] + z;
                        local251 = local243 & local226;
                        local256 = local247 & local226;
                        local261 = local243 >> ceiling.tileSizeShift;
                        local285 = local247 >> ceiling.tileSizeShift;
                        local313 = local96[local261][local285] * (ceiling.tileSize - local251) + local96[local261 + 1][local285] * local251 >> ceiling.tileSizeShift;
                        local327 = local96[local261][local285 + 1] * (ceiling.tileSize - local251) + local96[local261 + 1][local285 + 1] * local251 >> ceiling.tileSizeShift;
                        local472 = local313 * (ceiling.tileSize - local256) + local327 * local256 >> ceiling.tileSizeShift;
                        this.vertexY[local236] = this.vertexY[local236] + local472 + local228 - y;
                    }
                    for (local243 = this.maxVertex; local243 < this.vertexCount; local243++) {
                        local247 = this.vertexX[local243] + x;
                        local251 = this.vertexZ[local243] + z;
                        local256 = local247 & local226;
                        local261 = local251 & local226;
                        local285 = local247 >> ceiling.tileSizeShift;
                        local313 = local251 >> ceiling.tileSizeShift;
                        if (local285 >= 0 && local285 < ceiling.sizeX - 1 && local313 >= 0 && local313 < ceiling.sizeZ - 1) {
                            local327 = local96[local285][local313] * (ceiling.tileSize - local256) + local96[local285 + 1][local313] * local256 >> ceiling.tileSizeShift;
                            local472 = local96[local285][local313 + 1] * (ceiling.tileSize - local256) + local96[local285 + 1][local313 + 1] * local256 >> ceiling.tileSizeShift;
                            local784 = local327 * (ceiling.tileSize - local261) + local472 * local261 >> ceiling.tileSizeShift;
                            this.vertexY[local243] = this.vertexY[local243] + local784 + local228 - y;
                        }
                    }
                } else if (hillType == 5) {
                    local226 = ceiling.tileSize - 1;
                    local228 = this.maxY - this.minY;
                    @Pc(1380) int local1380;
                    for (local236 = 0; local236 < this.maxVertex; local236++) {
                        local243 = this.vertexX[local236] + x;
                        local247 = this.vertexZ[local236] + z;
                        local251 = local243 & local226;
                        local256 = local247 & local226;
                        local261 = local243 >> floor.tileSizeShift;
                        local285 = local247 >> floor.tileSizeShift;
                        local313 = local94[local261][local285] * (floor.tileSize - local251) + local94[local261 + 1][local285] * local251 >> floor.tileSizeShift;
                        local327 = local94[local261][local285 + 1] * (floor.tileSize - local251) + local94[local261 + 1][local285 + 1] * local251 >> floor.tileSizeShift;
                        local472 = local313 * (floor.tileSize - local256) + local327 * local256 >> floor.tileSizeShift;
                        local313 = local96[local261][local285] * (ceiling.tileSize - local251) + local96[local261 + 1][local285] * local251 >> ceiling.tileSizeShift;
                        local327 = local96[local261][local285 + 1] * (ceiling.tileSize - local251) + local96[local261 + 1][local285 + 1] * local251 >> ceiling.tileSizeShift;
                        local784 = local313 * (ceiling.tileSize - local256) + local327 * local256 >> ceiling.tileSizeShift;
                        local1380 = local472 - local784 - hillValue;
                        this.vertexY[local236] = ((this.vertexY[local236] << 8) / local228 * local1380 >> 8) - (y - local472);
                    }
                    for (local243 = this.maxVertex; local243 < this.vertexCount; local243++) {
                        local247 = this.vertexX[local243] + x;
                        local251 = this.vertexZ[local243] + z;
                        local256 = local247 & local226;
                        local261 = local251 & local226;
                        local285 = local247 >> floor.tileSizeShift;
                        local313 = local251 >> floor.tileSizeShift;
                        if (local285 >= 0 && local285 < floor.sizeX - 1 && local285 < ceiling.sizeX - 1 && local313 >= 0 && local313 < floor.sizeZ - 1 && local313 < ceiling.sizeZ - 1) {
                            local327 = local94[local285][local313] * (floor.tileSize - local256) + local94[local285 + 1][local313] * local256 >> floor.tileSizeShift;
                            local472 = local94[local285][local313 + 1] * (floor.tileSize - local256) + local94[local285 + 1][local313 + 1] * local256 >> floor.tileSizeShift;
                            local784 = local327 * (floor.tileSize - local261) + local472 * local261 >> floor.tileSizeShift;
                            local327 = local96[local285][local313] * (ceiling.tileSize - local256) + local96[local285 + 1][local313] * local256 >> ceiling.tileSizeShift;
                            local472 = local96[local285][local313 + 1] * (ceiling.tileSize - local256) + local96[local285 + 1][local313 + 1] * local256 >> ceiling.tileSizeShift;
                            local1380 = local327 * (ceiling.tileSize - local261) + local472 * local261 >> ceiling.tileSizeShift;
                            @Pc(1619) int local1619 = local784 - local1380 - hillValue;
                            this.vertexY[local243] = ((this.vertexY[local243] << 8) / local228 * local1619 >> 8) - (y - local784);
                        }
                    }
                }
            }
            this.boundsValid = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(II)I")
    public int shadeHsl(@OriginalArg(0) int hsl, @OriginalArg(1) int lightness) {
        lightness = lightness * (hsl & 0x7F) >> 7;
        if (lightness < 2) {
            lightness = 2;
        } else if (lightness > 126) {
            lightness = 126;
        }
        return (hsl & 0xFF80) + lightness;
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(Ljava/lang/Thread;)V")
    public void useThreadCopyCache(@OriginalArg(0) Thread thread) {
        @Pc(4) JavaThreadResource resource = this.toolkit.threadResource(thread);
        if (resource != this.copyThreadResource) {
            this.copyThreadResource = resource;
            this.copyTargets = this.copyThreadResource.copyTargetPool;
            this.copyBuffers = this.copyThreadResource.copyBufferPool;
        }
    }
}

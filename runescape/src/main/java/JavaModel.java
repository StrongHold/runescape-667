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
    public Rasterizer aRasterizer_1;

    @OriginalMember(owner = "client!rs", name = "Db", descriptor = "[I")
    public int[] anIntArray655;

    @OriginalMember(owner = "client!rs", name = "v", descriptor = "[I")
    public int[] anIntArray656;

    @OriginalMember(owner = "client!rs", name = "T", descriptor = "[I")
    public int[] anIntArray657;

    @OriginalMember(owner = "client!rs", name = "X", descriptor = "[I")
    public int[] vertexZ;

    @OriginalMember(owner = "client!rs", name = "A", descriptor = "[S")
    public short[] faceIndices;

    @OriginalMember(owner = "client!rs", name = "gb", descriptor = "[I")
    public int[] anIntArray659;

    @OriginalMember(owner = "client!rs", name = "Ab", descriptor = "[[I")
    public int[][] vertexLabels;

    @OriginalMember(owner = "client!rs", name = "S", descriptor = "[[I")
    public int[][] faceLabels;

    @OriginalMember(owner = "client!rs", name = "pb", descriptor = "Lclient!wf;")
    public JavaThreadResource aJavaThreadResource_2;

    @OriginalMember(owner = "client!rs", name = "Mb", descriptor = "I")
    public int anInt8485;

    @OriginalMember(owner = "client!rs", name = "I", descriptor = "[I")
    public int[] anIntArray660;

    @OriginalMember(owner = "client!rs", name = "Jb", descriptor = "[I")
    public int[] anIntArray661;

    @OriginalMember(owner = "client!rs", name = "R", descriptor = "S")
    public short aShort111;

    @OriginalMember(owner = "client!rs", name = "lb", descriptor = "[S")
    public short[] faceB;

    @OriginalMember(owner = "client!rs", name = "x", descriptor = "S")
    public short aShort112;

    @OriginalMember(owner = "client!rs", name = "O", descriptor = "[Lclient!rs;")
    public JavaModel[] aClass114_Sub3Array1;

    @OriginalMember(owner = "client!rs", name = "Cb", descriptor = "[Lclient!um;")
    public Class378[] aClass378Array1;

    @OriginalMember(owner = "client!rs", name = "C", descriptor = "[B")
    public byte[] shadingType;

    @OriginalMember(owner = "client!rs", name = "qb", descriptor = "[I")
    public int[] anIntArray662;

    @OriginalMember(owner = "client!rs", name = "U", descriptor = "[I")
    public int[] anIntArray663;

    @OriginalMember(owner = "client!rs", name = "ab", descriptor = "[I")
    public int[] anIntArray664;

    @OriginalMember(owner = "client!rs", name = "q", descriptor = "[S")
    public short[] faceColour;

    @OriginalMember(owner = "client!rs", name = "eb", descriptor = "Lclient!wf;")
    public JavaThreadResource aJavaThreadResource_3;

    @OriginalMember(owner = "client!rs", name = "V", descriptor = "I")
    public int anInt8487;

    @OriginalMember(owner = "client!rs", name = "nb", descriptor = "[I")
    public int[] anIntArray665;

    @OriginalMember(owner = "client!rs", name = "z", descriptor = "I")
    public int functionMask;

    @OriginalMember(owner = "client!rs", name = "Lb", descriptor = "[S")
    public short[] originModels;

    @OriginalMember(owner = "client!rs", name = "xb", descriptor = "[I")
    public int[] vertexX;

    @OriginalMember(owner = "client!rs", name = "o", descriptor = "[I")
    public int[] anIntArray667;

    @OriginalMember(owner = "client!rs", name = "t", descriptor = "[Lclient!rs;")
    public JavaModel[] aClass114_Sub3Array2;

    @OriginalMember(owner = "client!rs", name = "p", descriptor = "S")
    public short aShort113;

    @OriginalMember(owner = "client!rs", name = "tb", descriptor = "I")
    public int anInt8490;

    @OriginalMember(owner = "client!rs", name = "bb", descriptor = "[I")
    public int[] anIntArray668;

    @OriginalMember(owner = "client!rs", name = "M", descriptor = "S")
    public short aShort114;

    @OriginalMember(owner = "client!rs", name = "Hb", descriptor = "[I")
    public int[] anIntArray669;

    @OriginalMember(owner = "client!rs", name = "P", descriptor = "S")
    public short aShort115;

    @OriginalMember(owner = "client!rs", name = "cb", descriptor = "[I")
    public int[] anIntArray670;

    @OriginalMember(owner = "client!rs", name = "vb", descriptor = "S")
    public short aShort116;

    @OriginalMember(owner = "client!rs", name = "w", descriptor = "[I")
    public int[] anIntArray671;

    @OriginalMember(owner = "client!rs", name = "y", descriptor = "[I")
    public int[] anIntArray672;

    @OriginalMember(owner = "client!rs", name = "yb", descriptor = "I")
    public int billboardCount;

    @OriginalMember(owner = "client!rs", name = "fb", descriptor = "[[F")
    public float[][] texCoordU;

    @OriginalMember(owner = "client!rs", name = "Ob", descriptor = "[Lclient!mn;")
    public ModelParticleEffector[] effectors;

    @OriginalMember(owner = "client!rs", name = "W", descriptor = "[Lclient!mka;")
    public JavaBillboardAttributes[] billboardAttributes;

    @OriginalMember(owner = "client!rs", name = "D", descriptor = "S")
    public short aShort117;

    @OriginalMember(owner = "client!rs", name = "G", descriptor = "[S")
    public short[] faceA;

    @OriginalMember(owner = "client!rs", name = "H", descriptor = "[B")
    public byte[] facePriority;

    @OriginalMember(owner = "client!rs", name = "rb", descriptor = "[I")
    public int[] anIntArray673;

    @OriginalMember(owner = "client!rs", name = "Bb", descriptor = "[I")
    public int[] anIntArray674;

    @OriginalMember(owner = "client!rs", name = "u", descriptor = "[I")
    public int[] vertexY;

    @OriginalMember(owner = "client!rs", name = "F", descriptor = "[S")
    public short[] faceTextures;

    @OriginalMember(owner = "client!rs", name = "r", descriptor = "[Lclient!um;")
    public Class378[] aClass378Array2;

    @OriginalMember(owner = "client!rs", name = "s", descriptor = "[Lclient!qd;")
    public Class301[] aClass301Array1;

    @OriginalMember(owner = "client!rs", name = "Gb", descriptor = "[[F")
    public float[][] texCoordV;

    @OriginalMember(owner = "client!rs", name = "Q", descriptor = "S")
    public short aShort118;

    @OriginalMember(owner = "client!rs", name = "Y", descriptor = "Lclient!eaa;")
    public JavaMatrix aClass73_Sub2_2;

    @OriginalMember(owner = "client!rs", name = "L", descriptor = "I")
    public int anInt8493;

    @OriginalMember(owner = "client!rs", name = "Nb", descriptor = "[I")
    public int[] anIntArray676;

    @OriginalMember(owner = "client!rs", name = "mb", descriptor = "[Lclient!rv;")
    public ModelParticleEmitter[] emitters;

    @OriginalMember(owner = "client!rs", name = "n", descriptor = "[[I")
    public int[][] billboardLabels;

    @OriginalMember(owner = "client!rs", name = "Qb", descriptor = "[B")
    public byte[] faceAlpha;

    @OriginalMember(owner = "client!rs", name = "Z", descriptor = "[I")
    public int[] anIntArray677;

    @OriginalMember(owner = "client!rs", name = "db", descriptor = "Z")
    public boolean aBoolean650;

    @OriginalMember(owner = "client!rs", name = "B", descriptor = "[S")
    public short[] aShortArray124;

    @OriginalMember(owner = "client!rs", name = "E", descriptor = "[S")
    public short[] faceC;

    @OriginalMember(owner = "client!rs", name = "Ib", descriptor = "[Lclient!mf;")
    public JavaBillboardFace[] billboardFaces;

    @OriginalMember(owner = "client!rs", name = "J", descriptor = "I")
    public int anInt8495;

    @OriginalMember(owner = "client!rs", name = "jb", descriptor = "Z")
    public boolean aBoolean646 = false;

    @OriginalMember(owner = "client!rs", name = "sb", descriptor = "I")
    public int faceCount = 0;

    @OriginalMember(owner = "client!rs", name = "zb", descriptor = "I")
    public int maxVertex = 0;

    @OriginalMember(owner = "client!rs", name = "N", descriptor = "I")
    public int vertexCount = 0;

    @OriginalMember(owner = "client!rs", name = "hb", descriptor = "Z")
    public boolean aBoolean649 = false;

    @OriginalMember(owner = "client!rs", name = "K", descriptor = "Z")
    public boolean transparent = false;

    @OriginalMember(owner = "client!rs", name = "ib", descriptor = "I")
    public int anInt8488 = 0;

    @OriginalMember(owner = "client!rs", name = "Kb", descriptor = "Z")
    public boolean aBoolean648 = false;

    @OriginalMember(owner = "client!rs", name = "kb", descriptor = "Z")
    public boolean aBoolean652 = false;

    @OriginalMember(owner = "client!rs", name = "Pb", descriptor = "Z")
    public boolean movingTextures = false;

    @OriginalMember(owner = "client!rs", name = "ub", descriptor = "Lclient!iaa;")
    public final JavaToolkit toolkit;

    @OriginalMember(owner = "client!rs", name = "<init>", descriptor = "(Lclient!iaa;)V")
    public JavaModel(@OriginalArg(0) JavaToolkit arg0) {
        this.toolkit = arg0;
    }

    @OriginalMember(owner = "client!rs", name = "<init>", descriptor = "(Lclient!iaa;Lclient!dv;IIII)V")
    public JavaModel(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) Mesh base, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int featureMask) {
        this.toolkit = toolkit;
        this.functionMask = arg2;
        this.anInt8495 = arg3;
        this.anInt8485 = arg4;
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
        this.aShortArray124 = base.aShortArray20;
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
        @Pc(508) float[] fs = local505.aFloatArray82;
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
    public void O(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (arg0 != 128 && (this.functionMask & 0x1) != 1) {
            throw new IllegalStateException();
        } else if (arg1 != 128 && (this.functionMask & 0x2) != 2) {
            throw new IllegalStateException();
        } else if (arg2 == 128 || (this.functionMask & 0x4) == 4) {
            synchronized (this) {
                for (@Pc(53) int local53 = 0; local53 < this.vertexCount; local53++) {
                    this.vertexX[local53] = this.vertexX[local53] * arg0 >> 7;
                    this.vertexY[local53] = this.vertexY[local53] * arg1 >> 7;
                    this.vertexZ[local53] = this.vertexZ[local53] * arg2 >> 7;
                }
                this.aBoolean652 = false;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @OriginalMember(owner = "client!rs", name = "na", descriptor = "()I")
    @Override
    public int na() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort118;
    }

    @OriginalMember(owner = "client!rs", name = "m", descriptor = "()V")
    public void method7501() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.vertexCount; local5++) {
                this.vertexX[local5] = -this.vertexX[local5];
                this.vertexZ[local5] = -this.vertexZ[local5];
            }
            this.method7504();
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
            if (this.aClass378Array2 != null) {
                for (local38 = 0; local38 < this.maxVertex; local38++) {
                    if (this.aClass378Array2[local38] != null) {
                        this.aClass378Array2[local38].anInt9745 = -this.aClass378Array2[local38].anInt9745;
                    }
                }
            }
            if (this.aClass378Array1 != null) {
                for (local38 = 0; local38 < this.maxVertex; local38++) {
                    if (this.aClass378Array1[local38] != null) {
                        this.aClass378Array1[local38].anInt9745 = -this.aClass378Array1[local38].anInt9745;
                    }
                }
            }
            if (this.aClass301Array1 != null) {
                for (local38 = 0; local38 < this.faceCount; local38++) {
                    if (this.aClass301Array1[local38] != null) {
                        this.aClass301Array1[local38].anInt7681 = -this.aClass301Array1[local38].anInt7681;
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
            this.aBoolean652 = false;
            this.anInt8488 = 0;
        }
    }

    @OriginalMember(owner = "client!rs", name = "HA", descriptor = "()I")
    @Override
    public int HA() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort112;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(I)V")
    @Override
    public void a(@OriginalArg(0) int arg0) {
        if ((this.functionMask & 0x5) != 5) {
            throw new IllegalStateException();
        } else if (arg0 == 4096) {
            this.method7505();
        } else if (arg0 == 8192) {
            this.method7501();
        } else if (arg0 == 12288) {
            this.method7515();
        } else {
            @Pc(35) int local35 = Trig1.SIN[arg0];
            @Pc(39) int local39 = Trig1.COS[arg0];
            synchronized (this) {
                for (@Pc(45) int local45 = 0; local45 < this.vertexCount; local45++) {
                    @Pc(62) int local62 = this.vertexZ[local45] * local35 + this.vertexX[local45] * local39 >> 14;
                    this.vertexZ[local45] = this.vertexZ[local45] * local39 - this.vertexX[local45] * local35 >> 14;
                    this.vertexX[local45] = local62;
                }
                this.method7504();
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "h", descriptor = "(I)V")
    public void method7502(@OriginalArg(0) int arg0) {
        @Pc(8) short local8;
        @Pc(13) short local13;
        @Pc(18) short local18;
        @Pc(27) int local27;
        @Pc(46) int local46;
        @Pc(65) int local65;
        @Pc(81) int local81;
        @Pc(333) int local333;
        if (this.aJavaThreadResource_3.water) {
            local8 = this.faceA[arg0];
            local13 = this.faceB[arg0];
            local18 = this.faceC[arg0];
            local27 = 0;
            local46 = 0;
            local65 = 0;
            if (this.anIntArray673[local8] > this.aJavaThreadResource_3.waterDepth) {
                local27 = 255;
            } else if (this.anIntArray673[local8] > this.aJavaThreadResource_3.waterHeight) {
                local27 = (this.aJavaThreadResource_3.waterHeight - this.anIntArray673[local8]) * 255 / (this.aJavaThreadResource_3.waterHeight - this.aJavaThreadResource_3.waterDepth);
            }
            if (this.anIntArray673[local13] > this.aJavaThreadResource_3.waterDepth) {
                local46 = 255;
            } else if (this.anIntArray673[local13] > this.aJavaThreadResource_3.waterHeight) {
                local46 = (this.aJavaThreadResource_3.waterHeight - this.anIntArray673[local13]) * 255 / (this.aJavaThreadResource_3.waterHeight - this.aJavaThreadResource_3.waterDepth);
            }
            if (this.anIntArray673[local18] > this.aJavaThreadResource_3.waterDepth) {
                local65 = 255;
            } else if (this.anIntArray673[local18] > this.aJavaThreadResource_3.waterHeight) {
                local65 = (this.aJavaThreadResource_3.waterHeight - this.anIntArray673[local18]) * 255 / (this.aJavaThreadResource_3.waterHeight - this.aJavaThreadResource_3.waterDepth);
            }
            if (this.faceAlpha == null) {
                this.aRasterizer_1.alpha = 0;
            } else {
                this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
            }
            if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
                local81 = -16777216;
                if (this.faceAlpha != null) {
                    local81 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
                }
                if (this.anIntArray672[arg0] == -1) {
                    local333 = local81 | this.anIntArray668[arg0] & 0xFFFFFF;
                    this.aRasterizer_1.method5154((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local333, local333, local333, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
                } else {
                    this.aRasterizer_1.method5154((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local81 | this.anIntArray668[arg0] & 0xFFFFFF, local81 | this.anIntArray664[arg0] & 0xFFFFFF, local81 | this.anIntArray672[arg0] & 0xFFFFFF, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
                }
            } else if (this.anIntArray672[arg0] == -1) {
                this.aRasterizer_1.method5143((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]));
            } else {
                this.aRasterizer_1.method5143((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray664[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray672[arg0] & 0xFFFF]));
            }
            return;
        }
        local8 = this.faceA[arg0];
        local13 = this.faceB[arg0];
        local18 = this.faceC[arg0];
        local27 = this.anIntArray670[local8] - this.aJavaThreadResource_3.fogPlane;
        if (local27 > 255) {
            local27 = 255;
        } else if (local27 < 0) {
            local27 = 0;
        }
        local46 = this.anIntArray670[local13] - this.aJavaThreadResource_3.fogPlane;
        if (local46 > 255) {
            local46 = 255;
        } else if (local46 < 0) {
            local46 = 0;
        }
        local65 = this.anIntArray670[local18] - this.aJavaThreadResource_3.fogPlane;
        if (local65 > 255) {
            local65 = 255;
        } else if (local65 < 0) {
            local65 = 0;
        }
        local81 = local27 + local46 + local65;
        if (local81 == 765) {
            return;
        }
        if (local81 == 0) {
            this.method7517(arg0);
            return;
        }
        if (this.faceAlpha == null) {
            this.aRasterizer_1.alpha = 0;
        } else {
            this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
        }
        if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
            local333 = -16777216;
            if (this.faceAlpha != null) {
                local333 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
            }
            if (this.anIntArray672[arg0] == -1) {
                @Pc(362) int local362 = local333 | this.anIntArray668[arg0] & 0xFFFFFF;
                this.aRasterizer_1.method5154((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local362, local362, local362, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
            } else {
                this.aRasterizer_1.method5154((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local333 | this.anIntArray668[arg0] & 0xFFFFFF, local333 | this.anIntArray664[arg0] & 0xFFFFFF, local333 | this.anIntArray672[arg0] & 0xFFFFFF, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
            }
        } else if (this.anIntArray672[arg0] == -1) {
            this.aRasterizer_1.method5143((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]));
        } else {
            this.aRasterizer_1.method5143((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray664[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray672[arg0] & 0xFFFF]));
        }
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(Z)V")
    public void method7503(@OriginalArg(0) boolean arg0) {
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.method7512(arg0);
            }
        } else {
            this.method7512(arg0);
        }
    }

    @OriginalMember(owner = "client!rs", name = "j", descriptor = "()V")
    public void method7504() {
        this.aClass378Array2 = null;
        this.aClass378Array1 = null;
        this.aClass301Array1 = null;
        this.aBoolean652 = false;
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
        if (this.anInt8488 == 2) {
            this.anInt8488 = 1;
        }
    }

    @OriginalMember(owner = "client!rs", name = "EA", descriptor = "()I")
    @Override
    public int EA() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort113;
    }

    @OriginalMember(owner = "client!rs", name = "I", descriptor = "(I[IIIIZI[I)V")
    @Override
    protected void I(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int[] arg7) {
        @Pc(2) int local2 = arg1.length;
        @Pc(21) int local21;
        @Pc(69) int local69;
        @Pc(91) int local91;
        @Pc(74) int local74;
        @Pc(86) int local86;
        if (arg0 == 0) {
            arg2 <<= 0x4;
            arg3 <<= 0x4;
            arg4 <<= 0x4;
            if (!this.aBoolean648) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.aBoolean648 = true;
            }
            local21 = 0;
            this.anInt8493 = 0;
            this.anInt8490 = 0;
            this.anInt8487 = 0;
            for (local69 = 0; local69 < local2; local69++) {
                local74 = arg1[local69];
                if (local74 < this.vertexLabels.length) {
                    @Pc(84) int[] local84 = this.vertexLabels[local74];
                    for (local86 = 0; local86 < local84.length; local86++) {
                        local91 = local84[local86];
                        if (this.originModels == null || (arg6 & this.originModels[local91]) != 0) {
                            this.anInt8493 += this.vertexX[local91];
                            this.anInt8490 += this.vertexY[local91];
                            this.anInt8487 += this.vertexZ[local91];
                            local21++;
                        }
                    }
                }
            }
            if (local21 > 0) {
                this.anInt8493 = this.anInt8493 / local21 + arg2;
                this.anInt8490 = this.anInt8490 / local21 + arg3;
                this.anInt8487 = this.anInt8487 / local21 + arg4;
                this.aBoolean649 = true;
            } else {
                this.anInt8493 = arg2;
                this.anInt8490 = arg3;
                this.anInt8487 = arg4;
            }
            return;
        }
        @Pc(335) int[] local335;
        @Pc(337) int local337;
        if (arg0 == 1) {
            if (arg7 != null) {
                local21 = arg7[0] * arg2 + arg7[1] * arg3 + arg7[2] * arg4 + 8192 >> 14;
                local69 = arg7[3] * arg2 + arg7[4] * arg3 + arg7[5] * arg4 + 8192 >> 14;
                local74 = arg7[6] * arg2 + arg7[7] * arg3 + arg7[8] * arg4 + 8192 >> 14;
                arg2 = local21;
                arg3 = local69;
                arg4 = local74;
            }
            arg2 <<= 0x4;
            arg3 <<= 0x4;
            arg4 <<= 0x4;
            if (!this.aBoolean648) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.aBoolean648 = true;
            }
            for (local21 = 0; local21 < local2; local21++) {
                local69 = arg1[local21];
                if (local69 < this.vertexLabels.length) {
                    local335 = this.vertexLabels[local69];
                    for (local337 = 0; local337 < local335.length; local337++) {
                        local86 = local335[local337];
                        if (this.originModels == null || (arg6 & this.originModels[local86]) != 0) {
                            this.vertexX[local86] += arg2;
                            this.vertexY[local86] += arg3;
                            this.vertexZ[local86] += arg4;
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
        if (arg0 == 2) {
            if (arg7 == null) {
                for (local21 = 0; local21 < local2; local21++) {
                    local69 = arg1[local21];
                    if (local69 < this.vertexLabels.length) {
                        local335 = this.vertexLabels[local69];
                        for (local337 = 0; local337 < local335.length; local337++) {
                            local86 = local335[local337];
                            if (this.originModels == null || (arg6 & this.originModels[local86]) != 0) {
                                this.vertexX[local86] -= this.anInt8493;
                                this.vertexY[local86] -= this.anInt8490;
                                this.vertexZ[local86] -= this.anInt8487;
                                if (arg4 != 0) {
                                    local91 = Trig1.SIN[arg4];
                                    local506 = Trig1.COS[arg4];
                                    local531 = this.vertexY[local86] * local91 + this.vertexX[local86] * local506 + 16383 >> 14;
                                    this.vertexY[local86] = this.vertexY[local86] * local506 + 16383 - this.vertexX[local86] * local91 >> 14;
                                    this.vertexX[local86] = local531;
                                }
                                if (arg2 != 0) {
                                    local91 = Trig1.SIN[arg2];
                                    local506 = Trig1.COS[arg2];
                                    local531 = this.vertexY[local86] * local506 + 16383 - this.vertexZ[local86] * local91 >> 14;
                                    this.vertexZ[local86] = this.vertexY[local86] * local91 + this.vertexZ[local86] * local506 + 16383 >> 14;
                                    this.vertexY[local86] = local531;
                                }
                                if (arg3 != 0) {
                                    local91 = Trig1.SIN[arg3];
                                    local506 = Trig1.COS[arg3];
                                    local531 = this.vertexZ[local86] * local91 + this.vertexX[local86] * local506 + 16383 >> 14;
                                    this.vertexZ[local86] = this.vertexZ[local86] * local506 + 16383 - this.vertexX[local86] * local91 >> 14;
                                    this.vertexX[local86] = local531;
                                }
                                this.vertexX[local86] += this.anInt8493;
                                this.vertexY[local86] += this.anInt8490;
                                this.vertexZ[local86] += this.anInt8487;
                            }
                        }
                    }
                }
            } else {
                if (!this.aBoolean648) {
                    for (local21 = 0; local21 < this.vertexCount; local21++) {
                        this.vertexX[local21] <<= 0x4;
                        this.vertexY[local21] <<= 0x4;
                        this.vertexZ[local21] <<= 0x4;
                    }
                    this.aBoolean648 = true;
                }
                local21 = arg7[9] << 4;
                local69 = arg7[10] << 4;
                local74 = arg7[11] << 4;
                local337 = arg7[12] << 4;
                local86 = arg7[13] << 4;
                local91 = arg7[14] << 4;
                if (this.aBoolean649) {
                    local506 = arg7[0] * this.anInt8493 + arg7[3] * this.anInt8490 + arg7[6] * this.anInt8487 + 8192 >> 14;
                    local531 = arg7[1] * this.anInt8493 + arg7[4] * this.anInt8490 + arg7[7] * this.anInt8487 + 8192 >> 14;
                    local556 = arg7[2] * this.anInt8493 + arg7[5] * this.anInt8490 + arg7[8] * this.anInt8487 + 8192 >> 14;
                    local506 += local337;
                    local531 += local86;
                    local556 += local91;
                    this.anInt8493 = local506;
                    this.anInt8490 = local531;
                    this.anInt8487 = local556;
                    this.aBoolean649 = false;
                }
                @Pc(583) int[] local583 = new int[9];
                local531 = Trig1.COS[arg2];
                local556 = Trig1.SIN[arg2];
                local595 = Trig1.COS[arg3];
                local599 = Trig1.SIN[arg3];
                local603 = Trig1.COS[arg4];
                local607 = Trig1.SIN[arg4];
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
                @Pc(754) int local754 = local583[0] * -this.anInt8493 + local583[1] * -this.anInt8490 + local583[2] * -this.anInt8487 + 8192 >> 14;
                local782 = local583[3] * -this.anInt8493 + local583[4] * -this.anInt8490 + local583[5] * -this.anInt8487 + 8192 >> 14;
                local810 = local583[6] * -this.anInt8493 + local583[7] * -this.anInt8490 + local583[8] * -this.anInt8487 + 8192 >> 14;
                local815 = local754 + this.anInt8493;
                @Pc(820) int local820 = local782 + this.anInt8490;
                local825 = local810 + this.anInt8487;
                @Pc(828) int[] local828 = new int[9];
                for (local830 = 0; local830 < 3; local830++) {
                    for (local833 = 0; local833 < 3; local833++) {
                        local836 = 0;
                        for (local838 = 0; local838 < 3; local838++) {
                            local836 += local583[local830 * 3 + local838] * arg7[local833 * 3 + local838];
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
                            local974 += arg7[local968 * 3 + local976] * local828[local971 + local976 * 3];
                        }
                        local966[local968 * 3 + local971] = local974 + 8192 >> 14;
                    }
                }
                local971 = arg7[0] * local833 + arg7[1] * local836 + arg7[2] * local838 + 8192 >> 14;
                local974 = arg7[3] * local833 + arg7[4] * local836 + arg7[5] * local838 + 8192 >> 14;
                local976 = arg7[6] * local833 + arg7[7] * local836 + arg7[8] * local838 + 8192 >> 14;
                local971 += local21;
                local974 += local69;
                local976 += local74;
                for (local1103 = 0; local1103 < local2; local1103++) {
                    @Pc(1108) int local1108 = arg1[local1103];
                    if (local1108 < this.vertexLabels.length) {
                        @Pc(1118) int[] local1118 = this.vertexLabels[local1108];
                        for (@Pc(1120) int local1120 = 0; local1120 < local1118.length; local1120++) {
                            @Pc(1125) int local1125 = local1118[local1120];
                            if (this.originModels == null || (arg6 & this.originModels[local1125]) != 0) {
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
        } else if (arg0 != 3) {
            @Pc(2482) JavaBillboardFace local2482;
            @Pc(2487) JavaBillboardAttributes local2487;
            if (arg0 == 5) {
                if (this.faceLabels != null && this.faceAlpha != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = arg1[local21];
                        if (local69 < this.faceLabels.length) {
                            local335 = this.faceLabels[local69];
                            for (local337 = 0; local337 < local335.length; local337++) {
                                local86 = local335[local337];
                                if (this.aShortArray124 == null || (arg6 & this.aShortArray124[local86]) != 0) {
                                    local91 = (this.faceAlpha[local86] & 0xFF) + arg2 * 8;
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
            } else if (arg0 != 7) {
                @Pc(2723) JavaBillboardAttributes local2723;
                if (arg0 == 8) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = arg1[local21];
                            if (local69 < this.billboardLabels.length) {
                                local335 = this.billboardLabels[local69];
                                for (local337 = 0; local337 < local335.length; local337++) {
                                    local2723 = this.billboardAttributes[local335[local337]];
                                    local2723.anInt6222 += arg2;
                                    local2723.anInt6229 += arg3;
                                }
                            }
                        }
                    }
                } else if (arg0 == 10) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = arg1[local21];
                            if (local69 < this.billboardLabels.length) {
                                local335 = this.billboardLabels[local69];
                                for (local337 = 0; local337 < local335.length; local337++) {
                                    local2723 = this.billboardAttributes[local335[local337]];
                                    local2723.anInt6223 = local2723.anInt6223 * arg2 >> 7;
                                    local2723.anInt6226 = local2723.anInt6226 * arg3 >> 7;
                                }
                            }
                        }
                    }
                } else if (arg0 == 9 && this.billboardLabels != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = arg1[local21];
                        if (local69 < this.billboardLabels.length) {
                            local335 = this.billboardLabels[local69];
                            for (local337 = 0; local337 < local335.length; local337++) {
                                local2723 = this.billboardAttributes[local335[local337]];
                                local2723.anInt6231 = local2723.anInt6231 + arg2 & 0x3FFF;
                            }
                        }
                    }
                }
            } else if (this.faceLabels != null) {
                for (local21 = 0; local21 < local2; local21++) {
                    local69 = arg1[local21];
                    if (local69 < this.faceLabels.length) {
                        local335 = this.faceLabels[local69];
                        for (local337 = 0; local337 < local335.length; local337++) {
                            local86 = local335[local337];
                            if (this.aShortArray124 == null || (arg6 & this.aShortArray124[local86]) != 0) {
                                local91 = this.faceColour[local86] & 0xFFFF;
                                local506 = local91 >> 10 & 0x3F;
                                local531 = local91 >> 7 & 0x7;
                                local556 = local91 & 0x7F;
                                @Pc(2585) int local2585 = local506 + arg2 & 0x3F;
                                local531 += arg3;
                                if (local531 < 0) {
                                    local531 = 0;
                                } else if (local531 > 7) {
                                    local531 = 7;
                                }
                                local556 += arg4;
                                if (local556 < 0) {
                                    local556 = 0;
                                } else if (local556 > 127) {
                                    local556 = 127;
                                }
                                this.faceColour[local86] = (short) (local2585 << 10 | local531 << 7 | local556);
                            }
                        }
                        this.aBoolean650 = true;
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
        } else if (arg7 == null) {
            for (local21 = 0; local21 < local2; local21++) {
                local69 = arg1[local21];
                if (local69 < this.vertexLabels.length) {
                    local335 = this.vertexLabels[local69];
                    for (local337 = 0; local337 < local335.length; local337++) {
                        local86 = local335[local337];
                        if (this.originModels == null || (arg6 & this.originModels[local86]) != 0) {
                            this.vertexX[local86] -= this.anInt8493;
                            this.vertexY[local86] -= this.anInt8490;
                            this.vertexZ[local86] -= this.anInt8487;
                            this.vertexX[local86] = this.vertexX[local86] * arg2 / 128;
                            this.vertexY[local86] = this.vertexY[local86] * arg3 / 128;
                            this.vertexZ[local86] = this.vertexZ[local86] * arg4 / 128;
                            this.vertexX[local86] += this.anInt8493;
                            this.vertexY[local86] += this.anInt8490;
                            this.vertexZ[local86] += this.anInt8487;
                        }
                    }
                }
            }
        } else {
            if (!this.aBoolean648) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.aBoolean648 = true;
            }
            local21 = arg7[9] << 4;
            local69 = arg7[10] << 4;
            local74 = arg7[11] << 4;
            local337 = arg7[12] << 4;
            local86 = arg7[13] << 4;
            local91 = arg7[14] << 4;
            if (this.aBoolean649) {
                local506 = arg7[0] * this.anInt8493 + arg7[3] * this.anInt8490 + arg7[6] * this.anInt8487 + 8192 >> 14;
                local531 = arg7[1] * this.anInt8493 + arg7[4] * this.anInt8490 + arg7[7] * this.anInt8487 + 8192 >> 14;
                local556 = arg7[2] * this.anInt8493 + arg7[5] * this.anInt8490 + arg7[8] * this.anInt8487 + 8192 >> 14;
                local506 += local337;
                local531 += local86;
                local556 += local91;
                this.anInt8493 = local506;
                this.anInt8490 = local531;
                this.anInt8487 = local556;
                this.aBoolean649 = false;
            }
            local506 = arg2 << 15 >> 7;
            local531 = arg3 << 15 >> 7;
            local556 = arg4 << 15 >> 7;
            local595 = local506 * -this.anInt8493 + 8192 >> 14;
            local599 = local531 * -this.anInt8490 + 8192 >> 14;
            local603 = local556 * -this.anInt8487 + 8192 >> 14;
            local607 = local595 + this.anInt8493;
            local615 = local599 + this.anInt8490;
            local623 = local603 + this.anInt8487;
            @Pc(1790) int[] local1790 = new int[]{local506 * arg7[0] + 8192 >> 14, local506 * arg7[3] + 8192 >> 14, local506 * arg7[6] + 8192 >> 14, local531 * arg7[1] + 8192 >> 14, local531 * arg7[4] + 8192 >> 14, local531 * arg7[7] + 8192 >> 14, local556 * arg7[2] + 8192 >> 14, local556 * arg7[5] + 8192 >> 14, local556 * arg7[8] + 8192 >> 14};
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
                        local830 += arg7[local825 * 3 + local833] * local1790[local1942 + local833 * 3];
                    }
                    local1937[local825 * 3 + local1942] = local830 + 8192 >> 14;
                }
            }
            local1942 = arg7[0] * local1926 + arg7[1] * local1930 + arg7[2] * local1934 + 8192 >> 14;
            local830 = arg7[3] * local1926 + arg7[4] * local1930 + arg7[5] * local1934 + 8192 >> 14;
            local833 = arg7[6] * local1926 + arg7[7] * local1930 + arg7[8] * local1934 + 8192 >> 14;
            local1942 += local21;
            local830 += local69;
            local833 += local74;
            for (local836 = 0; local836 < local2; local836++) {
                local838 = arg1[local836];
                if (local838 < this.vertexLabels.length) {
                    local966 = this.vertexLabels[local838];
                    for (local968 = 0; local968 < local966.length; local968++) {
                        local971 = local966[local968];
                        if (this.originModels == null || (arg6 & this.originModels[local971]) != 0) {
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
        return this.method7513(x, y, matrix, quick, sizeShift, -1);
    }

    @OriginalMember(owner = "client!rs", name = "o", descriptor = "()V")
    public void method7505() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.vertexCount; local5++) {
                @Pc(11) int local11 = this.vertexX[local5];
                this.vertexX[local5] = this.vertexZ[local5];
                this.vertexZ[local5] = -local11;
            }
            this.method7504();
        }
    }

    @OriginalMember(owner = "client!rs", name = "ba", descriptor = "(Lclient!r;)Lclient!r;")
    @Override
    public Shadow ba(@OriginalArg(0) Shadow shadow) {
        return null;
    }

    @OriginalMember(owner = "client!rs", name = "g", descriptor = "(I)Z")
    public boolean method7506(@OriginalArg(0) int arg0) {
        if (this.faceAlpha == null) {
            return false;
        } else {
            return this.faceAlpha[arg0] != 0;
        }
    }

    @OriginalMember(owner = "client!rs", name = "RA", descriptor = "()I")
    @Override
    public int RA() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort117;
    }

    @OriginalMember(owner = "client!rs", name = "c", descriptor = "(I)V")
    public void method7507(@OriginalArg(0) int arg0) {
        @Pc(8) short local8;
        @Pc(13) short local13;
        @Pc(18) short local18;
        @Pc(27) int local27;
        @Pc(46) int local46;
        @Pc(65) int local65;
        @Pc(81) int local81;
        @Pc(333) int local333;
        if (this.aJavaThreadResource_3.water) {
            local8 = this.faceA[arg0];
            local13 = this.faceB[arg0];
            local18 = this.faceC[arg0];
            local27 = 0;
            local46 = 0;
            local65 = 0;
            if (this.anIntArray673[local8] > this.aJavaThreadResource_3.waterDepth) {
                local27 = 255;
            } else if (this.anIntArray673[local8] > this.aJavaThreadResource_3.waterHeight) {
                local27 = (this.aJavaThreadResource_3.waterHeight - this.anIntArray673[local8]) * 255 / (this.aJavaThreadResource_3.waterHeight - this.aJavaThreadResource_3.waterDepth);
            }
            if (this.anIntArray673[local13] > this.aJavaThreadResource_3.waterDepth) {
                local46 = 255;
            } else if (this.anIntArray673[local13] > this.aJavaThreadResource_3.waterHeight) {
                local46 = (this.aJavaThreadResource_3.waterHeight - this.anIntArray673[local13]) * 255 / (this.aJavaThreadResource_3.waterHeight - this.aJavaThreadResource_3.waterDepth);
            }
            if (this.anIntArray673[local18] > this.aJavaThreadResource_3.waterDepth) {
                local65 = 255;
            } else if (this.anIntArray673[local18] > this.aJavaThreadResource_3.waterHeight) {
                local65 = (this.aJavaThreadResource_3.waterHeight - this.anIntArray673[local18]) * 255 / (this.aJavaThreadResource_3.waterHeight - this.aJavaThreadResource_3.waterDepth);
            }
            if (this.faceAlpha == null) {
                this.aRasterizer_1.alpha = 0;
            } else {
                this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
            }
            if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
                local81 = -16777216;
                if (this.faceAlpha != null) {
                    local81 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
                }
                if (this.anIntArray672[arg0] == -1) {
                    local333 = local81 | this.anIntArray668[arg0] & 0xFFFFFF;
                    this.aRasterizer_1.renderTexturedTriangle((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local333, local333, local333, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
                } else {
                    this.aRasterizer_1.renderTexturedTriangle((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local81 | this.anIntArray668[arg0] & 0xFFFFFF, local81 | this.anIntArray664[arg0] & 0xFFFFFF, local81 | this.anIntArray672[arg0] & 0xFFFFFF, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
                }
            } else if (this.anIntArray672[arg0] == -1) {
                this.aRasterizer_1.renderTriangleRgb((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]));
            } else {
                this.aRasterizer_1.renderTriangleRgb((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray664[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray672[arg0] & 0xFFFF]));
            }
            return;
        }
        local8 = this.faceA[arg0];
        local13 = this.faceB[arg0];
        local18 = this.faceC[arg0];
        local27 = this.anIntArray670[local8] - this.aJavaThreadResource_3.fogPlane;
        if (local27 > 255) {
            local27 = 255;
        } else if (local27 < 0) {
            local27 = 0;
        }
        local46 = this.anIntArray670[local13] - this.aJavaThreadResource_3.fogPlane;
        if (local46 > 255) {
            local46 = 255;
        } else if (local46 < 0) {
            local46 = 0;
        }
        local65 = this.anIntArray670[local18] - this.aJavaThreadResource_3.fogPlane;
        if (local65 > 255) {
            local65 = 255;
        } else if (local65 < 0) {
            local65 = 0;
        }
        local81 = local27 + local46 + local65;
        if (local81 == 765) {
            return;
        }
        if (local81 == 0) {
            this.method7533(arg0);
            return;
        }
        if (this.faceAlpha == null) {
            this.aRasterizer_1.alpha = 0;
        } else {
            this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
        }
        if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
            local333 = -16777216;
            if (this.faceAlpha != null) {
                local333 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
            }
            if (this.anIntArray672[arg0] == -1) {
                @Pc(362) int local362 = local333 | this.anIntArray668[arg0] & 0xFFFFFF;
                this.aRasterizer_1.renderTexturedTriangle((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local362, local362, local362, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
            } else {
                this.aRasterizer_1.renderTexturedTriangle((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local333 | this.anIntArray668[arg0] & 0xFFFFFF, local333 | this.anIntArray664[arg0] & 0xFFFFFF, local333 | this.anIntArray672[arg0] & 0xFFFFFF, this.aJavaThreadResource_3.fogColour, local27, local46, local65, this.faceTextures[arg0]);
            }
        } else if (this.anIntArray672[arg0] == -1) {
            this.aRasterizer_1.renderTriangleRgb((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]));
        } else {
            this.aRasterizer_1.renderTriangleRgb((float) this.anIntArray657[local8], (float) this.anIntArray657[local13], (float) this.anIntArray657[local18], (float) this.anIntArray655[local8], (float) this.anIntArray655[local13], (float) this.anIntArray655[local18], (float) this.anIntArray670[local8], (float) this.anIntArray670[local13], (float) this.anIntArray670[local18], Static462.blendArgb(local27 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]), Static462.blendArgb(local46 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray664[arg0] & 0xFFFF]), Static462.blendArgb(local65 << 24 | this.aJavaThreadResource_3.fogColour, ColourUtils.HSV_TO_RGB[this.anIntArray672[arg0] & 0xFFFF]));
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(BIZ)Lclient!ka;")
    @Override
    public Model copy(@OriginalArg(0) byte arg0, @OriginalArg(1) int functionMask, @OriginalArg(2) boolean arg2) {
        this.method7535(Thread.currentThread());
        @Pc(4) boolean local4 = false;
        @Pc(25) JavaModel local25;
        @Pc(18) JavaModel local18;
        if (arg0 > 0 && arg0 <= 7) {
            local18 = this.aClass114_Sub3Array1[arg0 - 1];
            local25 = this.aClass114_Sub3Array2[arg0 - 1];
            local4 = true;
        } else {
            local25 = local18 = new JavaModel(this.toolkit);
        }
        return this.method7514(local25, local18, functionMask, local4, arg2);
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
                }
            }
            super.locked = true;
        }
    }

    @OriginalMember(owner = "client!rs", name = "e", descriptor = "()V")
    @Override
    public void method7479() {
    }

    @OriginalMember(owner = "client!rs", name = "P", descriptor = "(IIII)V")
    @Override
    protected void P(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(3) int local3;
        @Pc(14) int local14;
        if (arg0 == 0) {
            local3 = 0;
            this.anInt8493 = 0;
            this.anInt8490 = 0;
            this.anInt8487 = 0;
            for (local14 = 0; local14 < this.vertexCount; local14++) {
                this.anInt8493 += this.vertexX[local14];
                this.anInt8490 += this.vertexY[local14];
                this.anInt8487 += this.vertexZ[local14];
                local3++;
            }
            if (local3 > 0) {
                this.anInt8493 = this.anInt8493 / local3 + arg1;
                this.anInt8490 = this.anInt8490 / local3 + arg2;
                this.anInt8487 = this.anInt8487 / local3 + arg3;
            } else {
                this.anInt8493 = arg1;
                this.anInt8490 = arg2;
                this.anInt8487 = arg3;
            }
        } else if (arg0 == 1) {
            for (local3 = 0; local3 < this.vertexCount; local3++) {
                this.vertexX[local3] += arg1;
                this.vertexY[local3] += arg2;
                this.vertexZ[local3] += arg3;
            }
        } else {
            @Pc(168) int local168;
            @Pc(186) int local186;
            if (arg0 == 2) {
                for (local3 = 0; local3 < this.vertexCount; local3++) {
                    this.vertexX[local3] -= this.anInt8493;
                    this.vertexY[local3] -= this.anInt8490;
                    this.vertexZ[local3] -= this.anInt8487;
                    if (arg3 != 0) {
                        local14 = Trig1.SIN[arg3];
                        local168 = Trig1.COS[arg3];
                        local186 = this.vertexY[local3] * local14 + this.vertexX[local3] * local168 + 16383 >> 14;
                        this.vertexY[local3] = this.vertexY[local3] * local168 + 16383 - this.vertexX[local3] * local14 >> 14;
                        this.vertexX[local3] = local186;
                    }
                    if (arg1 != 0) {
                        local14 = Trig1.SIN[arg1];
                        local168 = Trig1.COS[arg1];
                        local186 = this.vertexY[local3] * local168 + 16383 - this.vertexZ[local3] * local14 >> 14;
                        this.vertexZ[local3] = this.vertexY[local3] * local14 + this.vertexZ[local3] * local168 + 16383 >> 14;
                        this.vertexY[local3] = local186;
                    }
                    if (arg2 != 0) {
                        local14 = Trig1.SIN[arg2];
                        local168 = Trig1.COS[arg2];
                        local186 = this.vertexZ[local3] * local14 + this.vertexX[local3] * local168 + 16383 >> 14;
                        this.vertexZ[local3] = this.vertexZ[local3] * local168 + 16383 - this.vertexX[local3] * local14 >> 14;
                        this.vertexX[local3] = local186;
                    }
                    this.vertexX[local3] += this.anInt8493;
                    this.vertexY[local3] += this.anInt8490;
                    this.vertexZ[local3] += this.anInt8487;
                }
            } else if (arg0 == 3) {
                for (local3 = 0; local3 < this.vertexCount; local3++) {
                    this.vertexX[local3] -= this.anInt8493;
                    this.vertexY[local3] -= this.anInt8490;
                    this.vertexZ[local3] -= this.anInt8487;
                    this.vertexX[local3] = this.vertexX[local3] * arg1 / 128;
                    this.vertexY[local3] = this.vertexY[local3] * arg2 / 128;
                    this.vertexZ[local3] = this.vertexZ[local3] * arg3 / 128;
                    this.vertexX[local3] += this.anInt8493;
                    this.vertexY[local3] += this.anInt8490;
                    this.vertexZ[local3] += this.anInt8487;
                }
            } else {
                @Pc(508) JavaBillboardFace local508;
                @Pc(513) JavaBillboardAttributes local513;
                if (arg0 == 5) {
                    for (local3 = 0; local3 < this.faceCount; local3++) {
                        local14 = (this.faceAlpha[local3] & 0xFF) + arg1 * 8;
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
                } else if (arg0 == 7) {
                    for (local3 = 0; local3 < this.faceCount; local3++) {
                        local14 = this.faceColour[local3] & 0xFFFF;
                        local168 = local14 >> 10 & 0x3F;
                        local186 = local14 >> 7 & 0x7;
                        @Pc(567) int local567 = local14 & 0x7F;
                        @Pc(573) int local573 = local168 + arg1 & 0x3F;
                        local186 += arg2;
                        if (local186 < 0) {
                            local186 = 0;
                        } else if (local186 > 7) {
                            local186 = 7;
                        }
                        local567 += arg3;
                        if (local567 < 0) {
                            local567 = 0;
                        } else if (local567 > 127) {
                            local567 = 127;
                        }
                        this.faceColour[local3] = (short) (local573 << 10 | local186 << 7 | local567);
                    }
                    this.aBoolean650 = true;
                    if (this.billboardFaces != null) {
                        for (local14 = 0; local14 < this.billboardCount; local14++) {
                            local508 = this.billboardFaces[local14];
                            local513 = this.billboardAttributes[local14];
                            local513.anInt6225 = local513.anInt6225 & 0xFF000000 | ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.faceColour[local508.anInt6139] & 0xFFFF) & 0xFFFF] & 0xFFFFFF;
                        }
                    }
                } else {
                    @Pc(681) JavaBillboardAttributes local681;
                    if (arg0 == 8) {
                        for (local3 = 0; local3 < this.billboardCount; local3++) {
                            local681 = this.billboardAttributes[local3];
                            local681.anInt6222 += arg1;
                            local681.anInt6229 += arg2;
                        }
                    } else if (arg0 == 10) {
                        for (local3 = 0; local3 < this.billboardCount; local3++) {
                            local681 = this.billboardAttributes[local3];
                            local681.anInt6223 = local681.anInt6223 * arg1 >> 7;
                            local681.anInt6226 = local681.anInt6226 * arg2 >> 7;
                        }
                    } else if (arg0 == 9) {
                        for (local3 = 0; local3 < this.billboardCount; local3++) {
                            local681 = this.billboardAttributes[local3];
                            local681.anInt6231 = local681.anInt6231 + arg1 & 0x3FFF;
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
        this.anInt8485 = contrast;
        this.anInt8488 = 0;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void apply(@OriginalArg(0) Matrix arg0) {
        @Pc(2) JavaMatrix local2 = (JavaMatrix) arg0;
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
    public void method7508(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) boolean arg2) {
        if (this.anIntArray672[arg0] == -2) {
            return;
        }
        @Pc(12) short local12 = this.faceA[arg0];
        @Pc(17) short local17 = this.faceB[arg0];
        @Pc(22) short local22 = this.faceC[arg0];
        @Pc(27) int local27 = this.anIntArray655[local12];
        @Pc(32) int local32 = this.anIntArray655[local17];
        @Pc(37) int local37 = this.anIntArray655[local22];
        @Pc(59) int local59;
        if (arg1 && (local27 == -5000 || local32 == -5000 || local37 == -5000)) {
            local59 = this.anIntArray656[local12];
            @Pc(64) int local64 = this.anIntArray656[local17];
            @Pc(69) int local69 = this.anIntArray656[local22];
            @Pc(74) int local74 = this.anIntArray660[local12];
            @Pc(79) int local79 = this.anIntArray660[local17];
            @Pc(84) int local84 = this.anIntArray660[local22];
            @Pc(89) int local89 = this.anIntArray659[local12];
            @Pc(94) int local94 = this.anIntArray659[local17];
            @Pc(99) int local99 = this.anIntArray659[local22];
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
                this.method7518(arg0);
                return;
            }
        } else if (this.anIntArray669[arg0] != -1 || (local27 - local32) * (this.anIntArray657[local22] - this.anIntArray657[local17]) - (this.anIntArray657[local12] - this.anIntArray657[local17]) * (local37 - local32) > 0) {
            if (local27 >= 0 && local32 >= 0 && local37 >= 0 && local27 <= this.aJavaThreadResource_3.anInt10607 && local32 <= this.aJavaThreadResource_3.anInt10607 && local37 <= this.aJavaThreadResource_3.anInt10607) {
                this.aRasterizer_1.clamp = false;
            } else {
                this.aRasterizer_1.clamp = true;
            }
            if (arg2) {
                local59 = this.anIntArray669[arg0];
                if (local59 == -1 || !this.billboardFaces[local59].aBoolean464) {
                    this.method7502(arg0);
                }
                return;
            }
            local59 = this.anIntArray669[arg0];
            if (local59 != -1) {
                @Pc(280) JavaBillboardFace local280 = this.billboardFaces[local59];
                @Pc(285) JavaBillboardAttributes local285 = this.billboardAttributes[local59];
                if (!local280.aBoolean464) {
                    this.method7517(arg0);
                }
                this.toolkit.method3797(local285.anInt6221, local285.anInt6227, local285.anInt6224, local285.anInt6232, local285.anInt6220, local285.anInt6231, local280.aShort72 & 0xFFFF, local285.anInt6225, local280.aByte98, local280.aByte97);
                return;
            }
            this.method7517(arg0);
        }
    }

    @OriginalMember(owner = "client!rs", name = "p", descriptor = "()V")
    public void method7509() {
        if (this.anInt8488 == 0) {
            this.method7503(false);
        } else if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.method7527();
            }
        } else {
            this.method7527();
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(B[B)V")
    @Override
    public void updateAlphas(@OriginalArg(0) byte arg0, @OriginalArg(1) byte[] arg1) {
        if ((this.functionMask & 0x100000) == 0) {
            throw new RuntimeException();
        }
        if (this.faceAlpha == null) {
            this.faceAlpha = new byte[this.faceCount];
        }
        @Pc(23) int local23;
        if (arg1 == null) {
            for (local23 = 0; local23 < this.faceCount; local23++) {
                this.faceAlpha[local23] = arg0;
            }
        } else {
            for (local23 = 0; local23 < this.faceCount; local23++) {
                @Pc(57) int local57 = 255 - (255 - (arg1[local23] & 0xFF)) * (255 - (arg0 & 0xFF)) / 255;
                this.faceAlpha[local23] = (byte) local57;
            }
        }
        if (this.anInt8488 == 2) {
            this.anInt8488 = 1;
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
        if (this.anInt8488 == 2) {
            this.anInt8488 = 1;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(IILclient!tt;ZII)Z")
    @Override
    public boolean pickedOrtho(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift, @OriginalArg(5) int angle) {
        return this.method7513(x, y, matrix, quick, sizeShift, angle);
    }

    @OriginalMember(owner = "client!rs", name = "i", descriptor = "()V")
    public void method7510() {
        this.aClass378Array2 = new Class378[this.maxVertex];
        for (@Pc(6) int local6 = 0; local6 < this.maxVertex; local6++) {
            this.aClass378Array2[local6] = new Class378();
        }
        for (@Pc(21) int local21 = 0; local21 < this.faceCount; local21++) {
            @Pc(27) short local27 = this.faceA[local21];
            @Pc(32) short local32 = this.faceB[local21];
            @Pc(37) short local37 = this.faceC[local21];
            @Pc(47) int local47 = this.vertexX[local32] - this.vertexX[local27];
            @Pc(57) int local57 = this.vertexY[local32] - this.vertexY[local27];
            @Pc(67) int local67 = this.vertexZ[local32] - this.vertexZ[local27];
            @Pc(77) int local77 = this.vertexX[local37] - this.vertexX[local27];
            @Pc(87) int local87 = this.vertexY[local37] - this.vertexY[local27];
            @Pc(97) int local97 = this.vertexZ[local37] - this.vertexZ[local27];
            @Pc(105) int local105 = local57 * local97 - local87 * local67;
            @Pc(113) int local113 = local67 * local77 - local97 * local47;
            @Pc(121) int local121;
            for (local121 = local47 * local87 - local77 * local57; local105 > 8192 || local113 > 8192 || local121 > 8192 || local105 < -8192 || local113 < -8192 || local121 < -8192; local121 >>= 0x1) {
                local105 >>= 0x1;
                local113 >>= 0x1;
            }
            @Pc(169) int local169 = (int) Math.sqrt(local105 * local105 + local113 * local113 + local121 * local121);
            if (local169 <= 0) {
                local169 = 1;
            }
            local105 = local105 * 256 / local169;
            local113 = local113 * 256 / local169;
            local121 = local121 * 256 / local169;
            @Pc(196) byte local196;
            if (this.shadingType == null) {
                local196 = 0;
            } else {
                local196 = this.shadingType[local21];
            }
            if (local196 == 0) {
                @Pc(209) Class378 local209 = this.aClass378Array2[local27];
                local209.anInt9746 += local105;
                local209.anInt9747 += local113;
                local209.anInt9745 += local121;
                local209.anInt9744++;
                @Pc(238) Class378 local238 = this.aClass378Array2[local32];
                local238.anInt9746 += local105;
                local238.anInt9747 += local113;
                local238.anInt9745 += local121;
                local238.anInt9744++;
                @Pc(267) Class378 local267 = this.aClass378Array2[local37];
                local267.anInt9746 += local105;
                local267.anInt9747 += local113;
                local267.anInt9745 += local121;
                local267.anInt9744++;
            } else if (local196 == 1) {
                if (this.aClass301Array1 == null) {
                    this.aClass301Array1 = new Class301[this.faceCount];
                }
                @Pc(316) Class301 local316 = this.aClass301Array1[local21] = new Class301();
                local316.anInt7682 = local105;
                local316.anInt7683 = local113;
                local316.anInt7681 = local121;
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!tt;Lclient!ima;II)V")
    @Override
    public void renderOrtho(@OriginalArg(0) Matrix arg0, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        this.method7528(arg0, cylinder, arg2, arg3);
    }

    @OriginalMember(owner = "client!rs", name = "c", descriptor = "()[Lclient!mn;")
    @Override
    public ModelParticleEffector[] particleEffectors() {
        return this.effectors;
    }

    @OriginalMember(owner = "client!rs", name = "l", descriptor = "()V")
    public void method7511() {
        synchronized (this) {
            @Pc(11) int local11;
            for (@Pc(5) int local5 = 0; local5 < this.maxVertex; local5++) {
                local11 = this.vertexX[local5];
                this.vertexX[local5] = this.vertexZ[local5];
                this.vertexZ[local5] = -local11;
                if (this.aClass378Array2[local5] != null) {
                    local11 = this.aClass378Array2[local5].anInt9746;
                    this.aClass378Array2[local5].anInt9746 = this.aClass378Array2[local5].anInt9745;
                    this.aClass378Array2[local5].anInt9745 = -local11;
                }
            }
            @Pc(77) int local77;
            if (this.aClass301Array1 != null) {
                for (local11 = 0; local11 < this.faceCount; local11++) {
                    if (this.aClass301Array1[local11] != null) {
                        local77 = this.aClass301Array1[local11].anInt7682;
                        this.aClass301Array1[local11].anInt7682 = this.aClass301Array1[local11].anInt7681;
                        this.aClass301Array1[local11].anInt7681 = -local77;
                    }
                }
            }
            for (local11 = this.maxVertex; local11 < this.vertexCount; local11++) {
                local77 = this.vertexX[local11];
                this.vertexX[local11] = this.vertexZ[local11];
                this.vertexZ[local11] = -local77;
            }
            this.anInt8488 = 0;
            this.aBoolean652 = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!ka;IIIZ)V")
    @Override
    public void method7481(@OriginalArg(0) Model arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        @Pc(2) JavaModel local2 = (JavaModel) arg0;
        if ((this.functionMask & 0x10000) != 65536) {
            throw new IllegalStateException("");
        } else if ((local2.functionMask & 0x10000) == 65536) {
            this.method7519(Thread.currentThread());
            this.method7525();
            this.method7523();
            local2.method7525();
            local2.method7523();
            Static567.anInt8494++;
            @Pc(43) int local43 = 0;
            @Pc(46) int[] local46 = local2.vertexX;
            @Pc(49) int local49 = local2.maxVertex;
            @Pc(67) int local67;
            for (@Pc(51) int local51 = 0; local51 < this.maxVertex; local51++) {
                @Pc(57) Class378 local57 = this.aClass378Array2[local51];
                if (local57.anInt9744 != 0) {
                    local67 = this.vertexY[local51] - arg2;
                    if (local67 >= local2.aShort116 && local67 <= local2.aShort113) {
                        @Pc(86) int local86 = this.vertexX[local51] - arg1;
                        if (local86 >= local2.aShort115 && local86 <= local2.aShort117) {
                            @Pc(105) int local105 = this.vertexZ[local51] - arg3;
                            if (local105 >= local2.aShort112 && local105 <= local2.aShort111) {
                                for (@Pc(119) int local119 = 0; local119 < local49; local119++) {
                                    @Pc(125) Class378 local125 = local2.aClass378Array2[local119];
                                    if (local86 == local46[local119] && local105 == local2.vertexZ[local119] && local67 == local2.vertexY[local119] && local125.anInt9744 != 0) {
                                        if (this.aClass378Array1 == null) {
                                            this.aClass378Array1 = new Class378[this.maxVertex];
                                        }
                                        if (local2.aClass378Array1 == null) {
                                            local2.aClass378Array1 = new Class378[local49];
                                        }
                                        @Pc(177) Class378 local177 = this.aClass378Array1[local51];
                                        if (local177 == null) {
                                            local177 = this.aClass378Array1[local51] = new Class378(local57);
                                        }
                                        @Pc(194) Class378 local194 = local2.aClass378Array1[local119];
                                        if (local194 == null) {
                                            local194 = local2.aClass378Array1[local119] = new Class378(local125);
                                        }
                                        local177.anInt9746 += local125.anInt9746;
                                        local177.anInt9747 += local125.anInt9747;
                                        local177.anInt9745 += local125.anInt9745;
                                        local177.anInt9744 += local125.anInt9744;
                                        local194.anInt9746 += local57.anInt9746;
                                        local194.anInt9747 += local57.anInt9747;
                                        local194.anInt9745 += local57.anInt9745;
                                        local194.anInt9744 += local57.anInt9744;
                                        local43++;
                                        this.anIntArray674[local51] = Static567.anInt8494;
                                        this.anIntArray667[local119] = Static567.anInt8494;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (local43 >= 3 && arg4) {
                for (@Pc(297) int local297 = 0; local297 < this.faceCount; local297++) {
                    if (this.anIntArray674[this.faceA[local297]] == Static567.anInt8494 && this.anIntArray674[this.faceB[local297]] == Static567.anInt8494 && this.anIntArray674[this.faceC[local297]] == Static567.anInt8494) {
                        if (this.shadingType == null) {
                            this.shadingType = new byte[this.faceCount];
                        }
                        this.shadingType[local297] = 2;
                    }
                }
                for (local67 = 0; local67 < local2.faceCount; local67++) {
                    if (this.anIntArray667[local2.faceA[local67]] == Static567.anInt8494 && this.anIntArray667[local2.faceB[local67]] == Static567.anInt8494 && this.anIntArray667[local2.faceC[local67]] == Static567.anInt8494) {
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
    protected void method7499(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) boolean arg6) {
        @Pc(2) int local2 = arg1.length;
        @Pc(21) int local21;
        @Pc(69) int local69;
        @Pc(91) int local91;
        @Pc(8) int local8;
        @Pc(12) int local12;
        @Pc(16) int local16;
        @Pc(86) int local86;
        if (arg0 == 0) {
            local8 = arg2 << 4;
            local12 = arg3 << 4;
            local16 = arg4 << 4;
            if (!this.aBoolean648) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.aBoolean648 = true;
            }
            local21 = 0;
            this.anInt8493 = 0;
            this.anInt8490 = 0;
            this.anInt8487 = 0;
            for (local69 = 0; local69 < local2; local69++) {
                @Pc(74) int local74 = arg1[local69];
                if (local74 < this.vertexLabels.length) {
                    @Pc(84) int[] local84 = this.vertexLabels[local74];
                    for (local86 = 0; local86 < local84.length; local86++) {
                        local91 = local84[local86];
                        this.anInt8493 += this.vertexX[local91];
                        this.anInt8490 += this.vertexY[local91];
                        this.anInt8487 += this.vertexZ[local91];
                        local21++;
                    }
                }
            }
            if (local21 > 0) {
                this.anInt8493 = this.anInt8493 / local21 + local8;
                this.anInt8490 = this.anInt8490 / local21 + local12;
                this.anInt8487 = this.anInt8487 / local21 + local16;
            } else {
                this.anInt8493 = local8;
                this.anInt8490 = local12;
                this.anInt8487 = local16;
            }
            return;
        }
        @Pc(242) int[] local242;
        @Pc(244) int local244;
        if (arg0 == 1) {
            local8 = arg2 << 4;
            local12 = arg3 << 4;
            local16 = arg4 << 4;
            if (!this.aBoolean648) {
                for (local21 = 0; local21 < this.vertexCount; local21++) {
                    this.vertexX[local21] <<= 0x4;
                    this.vertexY[local21] <<= 0x4;
                    this.vertexZ[local21] <<= 0x4;
                }
                this.aBoolean648 = true;
            }
            for (local21 = 0; local21 < local2; local21++) {
                local69 = arg1[local21];
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
        if (arg0 == 2) {
            for (local21 = 0; local21 < local2; local21++) {
                local69 = arg1[local21];
                if (local69 < this.vertexLabels.length) {
                    local242 = this.vertexLabels[local69];
                    if ((arg5 & 0x1) == 0) {
                        for (local244 = 0; local244 < local242.length; local244++) {
                            local86 = local242[local244];
                            this.vertexX[local86] -= this.anInt8493;
                            this.vertexY[local86] -= this.anInt8490;
                            this.vertexZ[local86] -= this.anInt8487;
                            if (arg4 != 0) {
                                local91 = Trig1.SIN[arg4];
                                local354 = Trig1.COS[arg4];
                                local372 = this.vertexY[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = this.vertexY[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            if (arg2 != 0) {
                                local91 = Trig1.SIN[arg2];
                                local354 = Trig1.COS[arg2];
                                local372 = this.vertexY[local86] * local354 + 16383 - this.vertexZ[local86] * local91 >> 14;
                                this.vertexZ[local86] = this.vertexY[local86] * local91 + this.vertexZ[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = local372;
                            }
                            if (arg3 != 0) {
                                local91 = Trig1.SIN[arg3];
                                local354 = Trig1.COS[arg3];
                                local372 = this.vertexZ[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexZ[local86] = this.vertexZ[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            this.vertexX[local86] += this.anInt8493;
                            this.vertexY[local86] += this.anInt8490;
                            this.vertexZ[local86] += this.anInt8487;
                        }
                    } else {
                        for (local244 = 0; local244 < local242.length; local244++) {
                            local86 = local242[local244];
                            this.vertexX[local86] -= this.anInt8493;
                            this.vertexY[local86] -= this.anInt8490;
                            this.vertexZ[local86] -= this.anInt8487;
                            if (arg2 != 0) {
                                local91 = Trig1.SIN[arg2];
                                local354 = Trig1.COS[arg2];
                                local372 = this.vertexY[local86] * local354 + 16383 - this.vertexZ[local86] * local91 >> 14;
                                this.vertexZ[local86] = this.vertexY[local86] * local91 + this.vertexZ[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = local372;
                            }
                            if (arg4 != 0) {
                                local91 = Trig1.SIN[arg4];
                                local354 = Trig1.COS[arg4];
                                local372 = this.vertexY[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexY[local86] = this.vertexY[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            if (arg3 != 0) {
                                local91 = Trig1.SIN[arg3];
                                local354 = Trig1.COS[arg3];
                                local372 = this.vertexZ[local86] * local91 + this.vertexX[local86] * local354 + 16383 >> 14;
                                this.vertexZ[local86] = this.vertexZ[local86] * local354 + 16383 - this.vertexX[local86] * local91 >> 14;
                                this.vertexX[local86] = local372;
                            }
                            this.vertexX[local86] += this.anInt8493;
                            this.vertexY[local86] += this.anInt8490;
                            this.vertexZ[local86] += this.anInt8487;
                        }
                    }
                }
            }
        } else if (arg0 == 3) {
            for (local21 = 0; local21 < local2; local21++) {
                local69 = arg1[local21];
                if (local69 < this.vertexLabels.length) {
                    local242 = this.vertexLabels[local69];
                    for (local244 = 0; local244 < local242.length; local244++) {
                        local86 = local242[local244];
                        this.vertexX[local86] -= this.anInt8493;
                        this.vertexY[local86] -= this.anInt8490;
                        this.vertexZ[local86] -= this.anInt8487;
                        this.vertexX[local86] = this.vertexX[local86] * arg2 / 128;
                        this.vertexY[local86] = this.vertexY[local86] * arg3 / 128;
                        this.vertexZ[local86] = this.vertexZ[local86] * arg4 / 128;
                        this.vertexX[local86] += this.anInt8493;
                        this.vertexY[local86] += this.anInt8490;
                        this.vertexZ[local86] += this.anInt8487;
                    }
                }
            }
        } else {
            @Pc(994) JavaBillboardFace local994;
            @Pc(999) JavaBillboardAttributes local999;
            if (arg0 == 5) {
                if (this.faceLabels != null && this.faceAlpha != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = arg1[local21];
                        if (local69 < this.faceLabels.length) {
                            local242 = this.faceLabels[local69];
                            for (local244 = 0; local244 < local242.length; local244++) {
                                local86 = local242[local244];
                                local91 = (this.faceAlpha[local86] & 0xFF) + arg2 * 8;
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
            } else if (arg0 != 7) {
                @Pc(1223) JavaBillboardAttributes local1223;
                if (arg0 == 8) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = arg1[local21];
                            if (local69 < this.billboardLabels.length) {
                                local242 = this.billboardLabels[local69];
                                for (local244 = 0; local244 < local242.length; local244++) {
                                    local1223 = this.billboardAttributes[local242[local244]];
                                    local1223.anInt6222 += arg2;
                                    local1223.anInt6229 += arg3;
                                }
                            }
                        }
                    }
                } else if (arg0 == 10) {
                    if (this.billboardLabels != null) {
                        for (local21 = 0; local21 < local2; local21++) {
                            local69 = arg1[local21];
                            if (local69 < this.billboardLabels.length) {
                                local242 = this.billboardLabels[local69];
                                for (local244 = 0; local244 < local242.length; local244++) {
                                    local1223 = this.billboardAttributes[local242[local244]];
                                    local1223.anInt6223 = local1223.anInt6223 * arg2 >> 7;
                                    local1223.anInt6226 = local1223.anInt6226 * arg3 >> 7;
                                }
                            }
                        }
                    }
                } else if (arg0 == 9 && this.billboardLabels != null) {
                    for (local21 = 0; local21 < local2; local21++) {
                        local69 = arg1[local21];
                        if (local69 < this.billboardLabels.length) {
                            local242 = this.billboardLabels[local69];
                            for (local244 = 0; local244 < local242.length; local244++) {
                                local1223 = this.billboardAttributes[local242[local244]];
                                local1223.anInt6231 = local1223.anInt6231 + arg2 & 0x3FFF;
                            }
                        }
                    }
                }
            } else if (this.faceLabels != null) {
                for (local21 = 0; local21 < local2; local21++) {
                    local69 = arg1[local21];
                    if (local69 < this.faceLabels.length) {
                        local242 = this.faceLabels[local69];
                        for (local244 = 0; local244 < local242.length; local244++) {
                            local86 = local242[local244];
                            local91 = this.faceColour[local86] & 0xFFFF;
                            local354 = local91 >> 10 & 0x3F;
                            local372 = local91 >> 7 & 0x7;
                            @Pc(1079) int local1079 = local91 & 0x7F;
                            @Pc(1085) int local1085 = local354 + arg2 & 0x3F;
                            local372 += arg3;
                            if (local372 < 0) {
                                local372 = 0;
                            } else if (local372 > 7) {
                                local372 = 7;
                            }
                            local1079 += arg4;
                            if (local1079 < 0) {
                                local1079 = 0;
                            } else if (local1079 > 127) {
                                local1079 = 127;
                            }
                            this.faceColour[local86] = (short) (local1085 << 10 | local372 << 7 | local1079);
                        }
                        this.aBoolean650 = true;
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
    public void method7512(@OriginalArg(0) boolean arg0) {
        if (this.anInt8488 == 1) {
            this.method7509();
        } else if (this.anInt8488 == 2) {
            if ((this.functionMask & 0x97098) == 0 && this.texCoordU == null) {
                this.faceColour = null;
            }
            if (arg0) {
                this.shadingType = null;
            }
        } else {
            this.method7523();
            @Pc(42) int local42 = this.toolkit.sunX;
            @Pc(46) int local46 = this.toolkit.sunY;
            @Pc(50) int local50 = this.toolkit.sunZ;
            @Pc(56) int local56 = this.toolkit.ambient >> 8;
            @Pc(65) int local65 = this.toolkit.sunIntensity * 768 / this.anInt8485;
            @Pc(74) int local74 = this.toolkit.reverseSunIntensity * 768 / this.anInt8485;
            if (this.anIntArray668 == null) {
                this.anIntArray668 = new int[this.faceCount];
                this.anIntArray664 = new int[this.faceCount];
                this.anIntArray672 = new int[this.faceCount];
            }
            for (@Pc(96) int local96 = 0; local96 < this.faceCount; local96++) {
                @Pc(102) byte local102;
                if (this.shadingType == null) {
                    local102 = 0;
                } else {
                    local102 = this.shadingType[local96];
                }
                @Pc(113) byte local113;
                if (this.faceAlpha == null) {
                    local113 = 0;
                } else {
                    local113 = this.faceAlpha[local96];
                }
                @Pc(124) short local124;
                if (this.faceTextures == null) {
                    local124 = -1;
                } else {
                    local124 = this.faceTextures[local96];
                }
                if (local113 == -2) {
                    local102 = 3;
                }
                if (local113 == -1) {
                    local102 = 2;
                }
                @Pc(154) int local154;
                @Pc(239) int local239;
                if (local124 == -1) {
                    @Pc(221) int local221;
                    @Pc(229) int local229;
                    @Pc(171) short local171;
                    @Pc(163) int local163;
                    if (local102 == 0) {
                        local154 = this.faceColour[local96] & 0xFFFF;
                        local163 = (local154 & 0x7F) * this.anInt8495 >> 7;
                        local171 = ColourUtils.hslToHsv(local154 & 0xFFFFFF80 | local163);
                        @Pc(192) Class378 local192;
                        if (this.aClass378Array1 == null || this.aClass378Array1[this.faceA[local96]] == null) {
                            local192 = this.aClass378Array2[this.faceA[local96]];
                        } else {
                            local192 = this.aClass378Array1[this.faceA[local96]];
                        }
                        local221 = (local42 * local192.anInt9746 + local46 * local192.anInt9747 + local50 * local192.anInt9745) / local192.anInt9744 >> 16;
                        local229 = local221 > 256 ? local65 : local74;
                        local239 = (local56 >> 1) + (local229 * local221 >> 17);
                        this.anIntArray668[local96] = local239 << 17 | Static244.scaleHslLightness(local239, local171);
                        if (this.aClass378Array1 == null || this.aClass378Array1[this.faceB[local96]] == null) {
                            local192 = this.aClass378Array2[this.faceB[local96]];
                        } else {
                            local192 = this.aClass378Array1[this.faceB[local96]];
                        }
                        local221 = (local42 * local192.anInt9746 + local46 * local192.anInt9747 + local50 * local192.anInt9745) / local192.anInt9744 >> 16;
                        local229 = local221 > 256 ? local65 : local74;
                        local239 = (local56 >> 1) + (local229 * local221 >> 17);
                        this.anIntArray664[local96] = local239 << 17 | Static244.scaleHslLightness(local239, local171);
                        if (this.aClass378Array1 == null || this.aClass378Array1[this.faceC[local96]] == null) {
                            local192 = this.aClass378Array2[this.faceC[local96]];
                        } else {
                            local192 = this.aClass378Array1[this.faceC[local96]];
                        }
                        local221 = (local42 * local192.anInt9746 + local46 * local192.anInt9747 + local50 * local192.anInt9745) / local192.anInt9744 >> 16;
                        local229 = local221 > 256 ? local65 : local74;
                        local239 = (local56 >> 1) + (local229 * local221 >> 17);
                        this.anIntArray672[local96] = local239 << 17 | Static244.scaleHslLightness(local239, local171);
                    } else if (local102 == 1) {
                        local154 = this.faceColour[local96] & 0xFFFF;
                        local163 = (local154 & 0x7F) * this.anInt8495 >> 7;
                        local171 = ColourUtils.hslToHsv(local154 & 0xFFFFFF80 | local163);
                        @Pc(444) Class301 local444 = this.aClass301Array1[local96];
                        local239 = local42 * local444.anInt7682 + local46 * local444.anInt7683 + local50 * local444.anInt7681 >> 16;
                        local221 = local239 > 256 ? local65 : local74;
                        local229 = (local56 >> 1) + (local221 * local239 >> 17);
                        this.anIntArray668[local96] = local229 << 17 | Static244.scaleHslLightness(local229, local171);
                        this.anIntArray672[local96] = -1;
                    } else if (local102 == 3) {
                        this.anIntArray668[local96] = 128;
                        this.anIntArray672[local96] = -1;
                    } else {
                        this.anIntArray672[local96] = -2;
                    }
                } else {
                    local154 = this.faceColour[local96] & 0xFFFF;
                    @Pc(599) int local599;
                    @Pc(579) int local579;
                    if (local102 == 0) {
                        @Pc(550) Class378 local550;
                        if (this.aClass378Array1 == null || this.aClass378Array1[this.faceA[local96]] == null) {
                            local550 = this.aClass378Array2[this.faceA[local96]];
                        } else {
                            local550 = this.aClass378Array1[this.faceA[local96]];
                        }
                        local579 = (local42 * local550.anInt9746 + local46 * local550.anInt9747 + local50 * local550.anInt9745) / local550.anInt9744 >> 16;
                        local239 = local579 > 256 ? local65 : local74;
                        local599 = this.method7524((local56 >> 2) + (local239 * local579 >> 18));
                        this.anIntArray668[local96] = local599 << 24 | this.method7531(local154, local124, local599);
                        if (this.aClass378Array1 == null || this.aClass378Array1[this.faceB[local96]] == null) {
                            local550 = this.aClass378Array2[this.faceB[local96]];
                        } else {
                            local550 = this.aClass378Array1[this.faceB[local96]];
                        }
                        local579 = (local42 * local550.anInt9746 + local46 * local550.anInt9747 + local50 * local550.anInt9745) / local550.anInt9744 >> 16;
                        local239 = local579 > 256 ? local65 : local74;
                        local599 = this.method7524((local56 >> 2) + (local239 * local579 >> 18));
                        this.anIntArray664[local96] = local599 << 24 | this.method7531(local154, local124, local599);
                        if (this.aClass378Array1 == null || this.aClass378Array1[this.faceC[local96]] == null) {
                            local550 = this.aClass378Array2[this.faceC[local96]];
                        } else {
                            local550 = this.aClass378Array1[this.faceC[local96]];
                        }
                        local579 = (local42 * local550.anInt9746 + local46 * local550.anInt9747 + local50 * local550.anInt9745) / local550.anInt9744 >> 16;
                        local239 = local579 > 256 ? local65 : local74;
                        local599 = this.method7524((local56 >> 2) + (local239 * local579 >> 18));
                        this.anIntArray672[local96] = local599 << 24 | this.method7531(local154, local124, local599);
                    } else if (local102 == 1) {
                        @Pc(787) Class301 local787 = this.aClass301Array1[local96];
                        local599 = local42 * local787.anInt7682 + local46 * local787.anInt7683 + local50 * local787.anInt7681 >> 16;
                        local579 = local599 > 256 ? local65 : local74;
                        local239 = this.method7524((local56 >> 2) + (local579 * local599 >> 18));
                        this.anIntArray668[local96] = local239 << 24 | this.method7531(local154, local124, local239);
                        this.anIntArray672[local96] = -1;
                    } else {
                        this.anIntArray672[local96] = -2;
                    }
                }
            }
            this.aClass378Array2 = null;
            this.aClass378Array1 = null;
            this.aClass301Array1 = null;
            if ((this.functionMask & 0x97098) == 0 && this.texCoordU == null) {
                this.faceColour = null;
            }
            if (arg0) {
                this.shadingType = null;
            }
            this.anInt8488 = 2;
        }
    }

    @OriginalMember(owner = "client!rs", name = "wa", descriptor = "()V")
    @Override
    protected void wa() {
        if (this.aBoolean648) {
            for (@Pc(4) int local4 = 0; local4 < this.vertexCount; local4++) {
                this.vertexX[local4] = this.vertexX[local4] + 7 >> 4;
                this.vertexY[local4] = this.vertexY[local4] + 7 >> 4;
                this.vertexZ[local4] = this.vertexZ[local4] + 7 >> 4;
            }
            this.aBoolean648 = false;
        }
        if (this.aBoolean650) {
            this.method7509();
            this.aBoolean650 = false;
        }
        this.aBoolean652 = false;
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(IILclient!tt;ZII)Z")
    public boolean method7513(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Matrix arg2, @OriginalArg(3) boolean arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.aClass73_Sub2_2 = (JavaMatrix) arg2;
        @Pc(7) JavaMatrix local7 = this.toolkit.camera;
        @Pc(31) float local31 = local7.tx + local7.e1_1 * this.aClass73_Sub2_2.tx + local7.e1_2 * this.aClass73_Sub2_2.ty + local7.e1_3 * this.aClass73_Sub2_2.tz;
        @Pc(55) float local55 = local7.ty + local7.e2_1 * this.aClass73_Sub2_2.tx + local7.e2_2 * this.aClass73_Sub2_2.ty + local7.e2_3 * this.aClass73_Sub2_2.tz;
        @Pc(79) float local79 = local7.tz + local7.e3_1 * this.aClass73_Sub2_2.tx + local7.e3_2 * this.aClass73_Sub2_2.ty + local7.e3_3 * this.aClass73_Sub2_2.tz;
        @Pc(100) float local100 = local7.e1_1 * this.aClass73_Sub2_2.e1_1 + local7.e1_2 * this.aClass73_Sub2_2.e2_1 + local7.e1_3 * this.aClass73_Sub2_2.e3_1;
        @Pc(121) float local121 = local7.e1_1 * this.aClass73_Sub2_2.e1_2 + local7.e1_2 * this.aClass73_Sub2_2.e2_2 + local7.e1_3 * this.aClass73_Sub2_2.e3_2;
        @Pc(142) float local142 = local7.e1_1 * this.aClass73_Sub2_2.e1_3 + local7.e1_2 * this.aClass73_Sub2_2.e2_3 + local7.e1_3 * this.aClass73_Sub2_2.e3_3;
        @Pc(163) float local163 = local7.e2_1 * this.aClass73_Sub2_2.e1_1 + local7.e2_2 * this.aClass73_Sub2_2.e2_1 + local7.e2_3 * this.aClass73_Sub2_2.e3_1;
        @Pc(184) float local184 = local7.e2_1 * this.aClass73_Sub2_2.e1_2 + local7.e2_2 * this.aClass73_Sub2_2.e2_2 + local7.e2_3 * this.aClass73_Sub2_2.e3_2;
        @Pc(205) float local205 = local7.e2_1 * this.aClass73_Sub2_2.e1_3 + local7.e2_2 * this.aClass73_Sub2_2.e2_3 + local7.e2_3 * this.aClass73_Sub2_2.e3_3;
        @Pc(226) float local226 = local7.e3_1 * this.aClass73_Sub2_2.e1_1 + local7.e3_2 * this.aClass73_Sub2_2.e2_1 + local7.e3_3 * this.aClass73_Sub2_2.e3_1;
        @Pc(247) float local247 = local7.e3_1 * this.aClass73_Sub2_2.e1_2 + local7.e3_2 * this.aClass73_Sub2_2.e2_2 + local7.e3_3 * this.aClass73_Sub2_2.e3_2;
        @Pc(268) float local268 = local7.e3_1 * this.aClass73_Sub2_2.e1_3 + local7.e3_2 * this.aClass73_Sub2_2.e2_3 + local7.e3_3 * this.aClass73_Sub2_2.e3_3;
        @Pc(270) boolean local270 = false;
        @Pc(274) int local274 = this.toolkit.projectionCenterX;
        @Pc(278) int local278 = this.toolkit.projectionCenterY;
        @Pc(282) int local282 = this.toolkit.projectionScaleX;
        @Pc(286) int local286 = this.toolkit.projectionScaleY;
        @Pc(288) int local288 = Integer.MAX_VALUE;
        @Pc(290) int local290 = Integer.MIN_VALUE;
        @Pc(292) int local292 = Integer.MAX_VALUE;
        @Pc(294) int local294 = Integer.MIN_VALUE;
        this.method7519(Thread.currentThread());
        if (!this.aBoolean652) {
            this.method7525();
        }
        @Pc(312) int local312 = this.aShort117 - this.aShort115 >> 1;
        @Pc(320) int local320 = this.aShort113 - this.aShort116 >> 1;
        @Pc(328) int local328 = this.aShort111 - this.aShort112 >> 1;
        @Pc(333) int local333 = this.aShort115 + local312;
        @Pc(338) int local338 = this.aShort116 + local320;
        @Pc(343) int local343 = this.aShort112 + local328;
        @Pc(349) int local349 = local333 - (local312 << arg4);
        @Pc(355) int local355 = local338 - (local320 << arg4);
        @Pc(361) int local361 = local343 - (local328 << arg4);
        @Pc(367) int local367 = local333 + (local312 << arg4);
        @Pc(373) int local373 = local338 + (local320 << arg4);
        @Pc(379) int local379 = local343 + (local328 << arg4);
        this.anIntArray662[0] = local349;
        this.anIntArray665[0] = local355;
        this.anIntArray661[0] = local361;
        this.anIntArray662[1] = local367;
        this.anIntArray665[1] = local355;
        this.anIntArray661[1] = local361;
        this.anIntArray662[2] = local349;
        this.anIntArray665[2] = local373;
        this.anIntArray661[2] = local361;
        this.anIntArray662[3] = local367;
        this.anIntArray665[3] = local373;
        this.anIntArray661[3] = local361;
        this.anIntArray662[4] = local349;
        this.anIntArray665[4] = local355;
        this.anIntArray661[4] = local379;
        this.anIntArray662[5] = local367;
        this.anIntArray665[5] = local355;
        this.anIntArray661[5] = local379;
        this.anIntArray662[6] = local349;
        this.anIntArray665[6] = local373;
        this.anIntArray661[6] = local379;
        this.anIntArray662[7] = local367;
        this.anIntArray665[7] = local373;
        this.anIntArray661[7] = local379;
        @Pc(534) float local534;
        @Pc(551) float local551;
        @Pc(568) float local568;
        @Pc(507) int local507;
        @Pc(512) int local512;
        @Pc(517) int local517;
        @Pc(592) int local592;
        @Pc(602) int local602;
        for (@Pc(501) int local501 = 0; local501 < 8; local501++) {
            local507 = this.anIntArray662[local501];
            local512 = this.anIntArray665[local501];
            local517 = this.anIntArray661[local501];
            local534 = local31 + local100 * (float) local507 + local121 * (float) local512 + local142 * (float) local517;
            local551 = local55 + local163 * (float) local507 + local184 * (float) local512 + local205 * (float) local517;
            local568 = local79 + local226 * (float) local507 + local247 * (float) local512 + local268 * (float) local517;
            if (local568 >= (float) this.toolkit.zNear) {
                if (arg5 > 0) {
                    local568 = (float) arg5;
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
        if (local270 && arg0 > local288 && arg0 < local290 && arg1 > local292 && arg1 < local294) {
            if (arg3) {
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
                    if (arg5 > 0) {
                        local568 = (float) arg5;
                    }
                    this.anIntArray655[local592] = local274 + (int) (local534 * (float) local282 / local568);
                    this.anIntArray657[local592] = local278 + (int) (local551 * (float) local286 / local568);
                } else {
                    this.anIntArray655[local592] = -999999;
                }
            }
            for (local602 = 0; local602 < this.faceCount; local602++) {
                if (this.anIntArray655[this.faceA[local602]] != -999999 && this.anIntArray655[this.faceB[local602]] != -999999 && this.anIntArray655[this.faceC[local602]] != -999999 && this.method7520(arg0, arg1, this.anIntArray657[this.faceA[local602]], this.anIntArray657[this.faceB[local602]], this.anIntArray657[this.faceC[local602]], this.anIntArray655[this.faceA[local602]], this.anIntArray655[this.faceB[local602]], this.anIntArray655[this.faceC[local602]])) {
                    return true;
                }
            }
        }
        return false;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!rs;Lclient!rs;IZZ)Lclient!ka;")
    public Model method7514(@OriginalArg(0) JavaModel arg0, @OriginalArg(1) JavaModel arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean arg3, @OriginalArg(4) boolean arg4) {
        arg0.aBoolean652 = this.aBoolean652;
        if (this.aBoolean652) {
            arg0.aShort117 = this.aShort117;
            arg0.aShort113 = this.aShort113;
            arg0.aShort111 = this.aShort111;
            arg0.aShort115 = this.aShort115;
            arg0.aShort116 = this.aShort116;
            arg0.aShort112 = this.aShort112;
            arg0.aShort118 = this.aShort118;
            arg0.aShort114 = this.aShort114;
        }
        arg0.anInt8495 = this.anInt8495;
        arg0.anInt8485 = this.anInt8485;
        arg0.vertexCount = this.vertexCount;
        arg0.maxVertex = this.maxVertex;
        arg0.faceCount = this.faceCount;
        arg0.billboardCount = this.billboardCount;
        if ((arg2 & 0x100) == 0) {
            arg0.transparent = this.transparent;
        } else {
            arg0.transparent = true;
        }
        arg0.movingTextures = this.movingTextures;
        @Pc(100) boolean local100 = (arg2 & 0x7) == 7 | (arg2 & 0x20) != 0;
        @Pc(113) boolean local113 = local100 || (arg2 & 0x1) != 0;
        @Pc(126) boolean local126 = local100 || (arg2 & 0x2) != 0;
        @Pc(145) boolean local145 = local100 || (arg2 & 0x4) != 0 || (arg2 & 0x10) != 0;
        @Pc(188) int local188;
        if (local113 || local126 || local145) {
            if (local113) {
                if (arg1.vertexX == null || arg1.vertexX.length < this.vertexCount) {
                    arg0.vertexX = arg1.vertexX = new int[this.vertexCount];
                } else {
                    arg0.vertexX = arg1.vertexX;
                }
                for (local188 = 0; local188 < this.vertexCount; local188++) {
                    arg0.vertexX[local188] = this.vertexX[local188];
                }
            } else {
                arg0.vertexX = this.vertexX;
            }
            if (local126) {
                if (arg1.vertexY == null || arg1.vertexY.length < this.vertexCount) {
                    arg0.vertexY = arg1.vertexY = new int[this.vertexCount];
                } else {
                    arg0.vertexY = arg1.vertexY;
                }
                for (local188 = 0; local188 < this.vertexCount; local188++) {
                    arg0.vertexY[local188] = this.vertexY[local188];
                }
            } else {
                arg0.vertexY = this.vertexY;
            }
            if (local145) {
                if (arg1.vertexZ == null || arg1.vertexZ.length < this.vertexCount) {
                    arg0.vertexZ = arg1.vertexZ = new int[this.vertexCount];
                } else {
                    arg0.vertexZ = arg1.vertexZ;
                }
                for (local188 = 0; local188 < this.vertexCount; local188++) {
                    arg0.vertexZ[local188] = this.vertexZ[local188];
                }
            } else {
                arg0.vertexZ = this.vertexZ;
            }
        } else {
            arg0.vertexX = this.vertexX;
            arg0.vertexY = this.vertexY;
            arg0.vertexZ = this.vertexZ;
        }
        if ((arg2 & 0x84080) == 0) {
            arg0.faceColour = this.faceColour;
        } else {
            if (arg1.faceColour == null || arg1.faceColour.length < this.faceCount) {
                local188 = this.faceCount;
                arg0.faceColour = arg1.faceColour = new short[local188];
            } else {
                arg0.faceColour = arg1.faceColour;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                arg0.faceColour[local188] = this.faceColour[local188];
            }
        }
        if ((arg2 & 0x97018) != 0) {
            arg0.anInt8488 = 0;
            arg0.anIntArray668 = arg0.anIntArray664 = arg0.anIntArray672 = null;
        } else if ((arg2 & 0x80) == 0) {
            if (arg4) {
                this.method7503(false);
            }
            arg0.anIntArray668 = this.anIntArray668;
            arg0.anIntArray664 = this.anIntArray664;
            arg0.anIntArray672 = this.anIntArray672;
            arg0.anInt8488 = this.anInt8488;
        } else {
            if (arg4) {
                this.method7503(false);
            }
            if (this.anIntArray668 != null) {
                if (arg1.anIntArray668 == null || arg1.anIntArray668.length < this.faceCount) {
                    local188 = this.faceCount;
                    arg0.anIntArray668 = arg1.anIntArray668 = new int[local188];
                    arg0.anIntArray664 = arg1.anIntArray664 = new int[local188];
                    arg0.anIntArray672 = arg1.anIntArray672 = new int[local188];
                } else {
                    arg0.anIntArray668 = arg1.anIntArray668;
                    arg0.anIntArray664 = arg1.anIntArray664;
                    arg0.anIntArray672 = arg1.anIntArray672;
                }
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    arg0.anIntArray668[local188] = this.anIntArray668[local188];
                    arg0.anIntArray664[local188] = this.anIntArray664[local188];
                    arg0.anIntArray672[local188] = this.anIntArray672[local188];
                }
            }
            arg0.anInt8488 = this.anInt8488;
        }
        if ((arg2 & 0x100) == 0) {
            arg0.faceAlpha = this.faceAlpha;
        } else {
            if (arg1.faceAlpha == null || arg1.faceAlpha.length < this.faceCount) {
                local188 = this.faceCount;
                arg0.faceAlpha = arg1.faceAlpha = new byte[local188];
            } else {
                arg0.faceAlpha = arg1.faceAlpha;
            }
            if (this.faceAlpha == null) {
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    arg0.faceAlpha[local188] = 0;
                }
            } else {
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    arg0.faceAlpha[local188] = this.faceAlpha[local188];
                }
            }
        }
        if ((arg2 & 0x8) == 0 && (arg2 & 0x10) == 0) {
            if (arg4) {
                this.method7523();
            }
            arg0.aClass378Array2 = this.aClass378Array2;
            arg0.aClass301Array1 = this.aClass301Array1;
        } else {
            if (arg1.aClass378Array2 == null || arg1.aClass378Array2.length < this.maxVertex) {
                local188 = this.maxVertex;
                arg0.aClass378Array2 = arg1.aClass378Array2 = new Class378[local188];
            } else {
                arg0.aClass378Array2 = arg1.aClass378Array2;
            }
            if (this.aClass378Array2 == null) {
                arg0.aClass378Array2 = null;
            } else {
                for (local188 = 0; local188 < this.maxVertex; local188++) {
                    arg0.aClass378Array2[local188] = new Class378(this.aClass378Array2[local188]);
                }
            }
            if (this.aClass301Array1 == null) {
                arg0.aClass301Array1 = null;
            } else {
                if (arg1.aClass301Array1 == null || arg1.aClass301Array1.length < this.faceCount) {
                    local188 = this.faceCount;
                    arg0.aClass301Array1 = arg1.aClass301Array1 = new Class301[local188];
                } else {
                    arg0.aClass301Array1 = arg1.aClass301Array1;
                }
                for (local188 = 0; local188 < this.faceCount; local188++) {
                    arg0.aClass301Array1[local188] = this.aClass301Array1[local188] == null ? null : new Class301(this.aClass301Array1[local188]);
                }
            }
        }
        if ((arg2 & 0x8000) == 0) {
            arg0.faceTextures = this.faceTextures;
        } else if (this.faceTextures == null) {
            arg0.faceTextures = null;
        } else {
            if (arg1.faceTextures == null || arg1.faceTextures.length < this.faceCount) {
                local188 = this.faceCount;
                arg0.faceTextures = arg1.faceTextures = new short[local188];
            } else {
                arg0.faceTextures = arg1.faceTextures;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                arg0.faceTextures[local188] = this.faceTextures[local188];
            }
        }
        if ((arg2 & 0x10000) == 0) {
            arg0.shadingType = this.shadingType;
        } else if (this.shadingType == null) {
            arg0.shadingType = null;
        } else {
            if (arg1.shadingType == null || arg1.shadingType.length < this.faceCount) {
                local188 = arg3 ? this.faceCount + 100 : this.faceCount;
                arg0.shadingType = arg1.shadingType = new byte[local188];
            } else {
                arg0.shadingType = arg1.shadingType;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                arg0.shadingType[local188] = this.shadingType[local188];
            }
        }
        @Pc(900) int local900;
        if ((arg2 & 0xC580) == 0) {
            arg0.billboardAttributes = this.billboardAttributes;
        } else if (arg1.billboardAttributes == null || arg1.billboardAttributes.length < this.billboardCount) {
            local188 = this.billboardCount;
            arg0.billboardAttributes = arg1.billboardAttributes = new JavaBillboardAttributes[local188];
            for (local900 = 0; local900 < this.billboardCount; local900++) {
                arg0.billboardAttributes[local900] = this.billboardAttributes[local900].method5574();
            }
        } else {
            arg0.billboardAttributes = arg1.billboardAttributes;
            for (local188 = 0; local188 < this.billboardCount; local188++) {
                arg0.billboardAttributes[local188].method5573(this.billboardAttributes[local188]);
            }
        }
        if (this.texCoordU == null || (arg2 & 0x10) == 0) {
            arg0.texCoordU = this.texCoordU;
            arg0.texCoordV = this.texCoordV;
        } else {
            if (arg1.texCoordU == null || arg1.texCoordU.length < this.faceCount) {
                local188 = arg3 ? this.faceCount + 100 : this.faceCount;
                arg0.texCoordU = arg1.texCoordU = new float[local188][3];
            } else {
                arg0.texCoordU = arg1.texCoordU;
            }
            for (local188 = 0; local188 < this.faceCount; local188++) {
                if (this.texCoordU[local188] != null) {
                    arg0.texCoordU[local188][0] = this.texCoordU[local188][0];
                    arg0.texCoordU[local188][1] = this.texCoordU[local188][1];
                    arg0.texCoordU[local188][2] = this.texCoordU[local188][2];
                }
            }
            if (arg1.texCoordV == null || arg1.texCoordV.length < this.faceCount) {
                local900 = arg3 ? this.faceCount + 100 : this.faceCount;
                arg0.texCoordV = arg1.texCoordV = new float[local900][3];
            } else {
                arg0.texCoordV = arg1.texCoordV;
            }
            for (local900 = 0; local900 < this.faceCount; local900++) {
                if (this.texCoordV[local900] != null) {
                    arg0.texCoordV[local900][0] = this.texCoordV[local900][0];
                    arg0.texCoordV[local900][1] = this.texCoordV[local900][1];
                    arg0.texCoordV[local900][2] = this.texCoordV[local900][2];
                }
            }
        }
        arg0.vertexLabels = this.vertexLabels;
        arg0.faceLabels = this.faceLabels;
        arg0.billboardLabels = this.billboardLabels;
        arg0.originModels = this.originModels;
        arg0.aShortArray124 = this.aShortArray124;
        arg0.facePriority = this.facePriority;
        arg0.faceA = this.faceA;
        arg0.faceB = this.faceB;
        arg0.faceC = this.faceC;
        arg0.emitters = this.emitters;
        arg0.effectors = this.effectors;
        arg0.billboardFaces = this.billboardFaces;
        arg0.faceIndices = this.faceIndices;
        arg0.functionMask = arg2;
        return arg0;
    }

    @OriginalMember(owner = "client!rs", name = "q", descriptor = "()V")
    public void method7515() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.vertexCount; local5++) {
                @Pc(11) int local11 = this.vertexZ[local5];
                this.vertexZ[local5] = this.vertexX[local5];
                this.vertexX[local5] = -local11;
            }
            this.method7504();
        }
    }

    @OriginalMember(owner = "client!rs", name = "k", descriptor = "(I)V")
    @Override
    public void k(@OriginalArg(0) int arg0) {
        if ((this.functionMask & 0xD) != 13) {
            throw new IllegalStateException();
        } else if (this.aClass378Array2 == null) {
            this.a(arg0);
        } else if (arg0 == 4096) {
            this.method7511();
        } else if (arg0 == 8192) {
            this.method7530();
        } else if (arg0 == 12288) {
            this.method7532();
        } else {
            @Pc(40) int local40 = Trig1.SIN[arg0];
            @Pc(44) int local44 = Trig1.COS[arg0];
            synchronized (this) {
                @Pc(67) int local67;
                for (@Pc(50) int local50 = 0; local50 < this.maxVertex; local50++) {
                    local67 = this.vertexZ[local50] * local40 + this.vertexX[local50] * local44 >> 14;
                    this.vertexZ[local50] = this.vertexZ[local50] * local44 - this.vertexX[local50] * local40 >> 14;
                    this.vertexX[local50] = local67;
                    if (this.aClass378Array2[local50] != null) {
                        local67 = this.aClass378Array2[local50].anInt9745 * local40 + this.aClass378Array2[local50].anInt9746 * local44 >> 14;
                        this.aClass378Array2[local50].anInt9745 = this.aClass378Array2[local50].anInt9745 * local44 - this.aClass378Array2[local50].anInt9746 * local40 >> 14;
                        this.aClass378Array2[local50].anInt9746 = local67;
                    }
                }
                @Pc(178) int local178;
                if (this.aClass301Array1 != null) {
                    for (local67 = 0; local67 < this.faceCount; local67++) {
                        if (this.aClass301Array1[local67] != null) {
                            local178 = this.aClass301Array1[local67].anInt7681 * local40 + this.aClass301Array1[local67].anInt7682 * local44 >> 14;
                            this.aClass301Array1[local67].anInt7681 = this.aClass301Array1[local67].anInt7681 * local44 - this.aClass301Array1[local67].anInt7682 * local40 >> 14;
                            this.aClass301Array1[local67].anInt7682 = local178;
                        }
                    }
                }
                for (local67 = this.maxVertex; local67 < this.vertexCount; local67++) {
                    local178 = this.vertexZ[local67] * local40 + this.vertexX[local67] * local44 >> 14;
                    this.vertexZ[local67] = this.vertexZ[local67] * local44 - this.vertexX[local67] * local40 >> 14;
                    this.vertexX[local67] = local178;
                }
                this.anInt8488 = 0;
                this.aBoolean652 = false;
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(ZZII)V")
    public void method7516(@OriginalArg(0) boolean arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(4) int local4;
        if (this.billboardFaces != null) {
            local4 = 0;
            while (local4 < this.billboardCount) {
                @Pc(10) JavaBillboardFace local10 = this.billboardFaces[local4];
                this.anIntArray669[local10.anInt6139] = local4++;
            }
        }
        if (!this.transparent && this.billboardFaces == null) {
            for (local4 = 0; local4 < this.faceCount; local4++) {
                this.method7508(local4, arg0, arg1);
            }
        } else if ((this.functionMask & 0x100) == 0 && this.faceIndices != null) {
            for (local4 = 0; local4 < this.faceCount; local4++) {
                @Pc(51) short local51 = this.faceIndices[local4];
                this.method7508(local51, arg0, arg1);
            }
        } else {
            for (local4 = 0; local4 < this.faceCount; local4++) {
                if (!this.method7506(local4) && !this.method7526(local4)) {
                    this.method7508(local4, arg0, arg1);
                }
            }
            @Pc(95) int local95;
            if (this.facePriority == null) {
                for (local95 = 0; local95 < this.faceCount; local95++) {
                    if (this.method7506(local95) || this.method7526(local95)) {
                        this.method7508(local95, arg0, arg1);
                    }
                }
            } else {
                for (local95 = 0; local95 < 12; local95++) {
                    for (@Pc(125) int local125 = 0; local125 < this.faceCount; local125++) {
                        if (this.facePriority[local125] == local95 && (this.method7506(local125) || this.method7526(local125))) {
                            this.method7508(local125, arg0, arg1);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "f", descriptor = "(I)V")
    public void method7517(@OriginalArg(0) int arg0) {
        @Pc(4) short local4 = this.faceA[arg0];
        @Pc(9) short local9 = this.faceB[arg0];
        @Pc(14) short local14 = this.faceC[arg0];
        if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
            @Pc(181) int local181 = -16777216;
            if (this.faceAlpha != null) {
                local181 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
            }
            if (this.anIntArray672[arg0] == -1) {
                @Pc(210) int local210 = local181 | this.anIntArray668[arg0] & 0xFFFFFF;
                this.aRasterizer_1.method5154((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local210, local210, local210, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            } else {
                this.aRasterizer_1.method5154((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local181 | this.anIntArray668[arg0] & 0xFFFFFF, local181 | this.anIntArray664[arg0] & 0xFFFFFF, local181 | this.anIntArray672[arg0] & 0xFFFFFF, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            }
            return;
        }
        if (this.faceAlpha == null) {
            this.aRasterizer_1.alpha = 0;
        } else {
            this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
        }
        if (this.anIntArray672[arg0] == -1) {
            this.aRasterizer_1.method5144((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]);
        } else {
            this.aRasterizer_1.method5153((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], (float) (this.anIntArray668[arg0] & 0xFFFF), (float) (this.anIntArray664[arg0] & 0xFFFF), (float) (this.anIntArray672[arg0] & 0xFFFF));
        }
    }

    @OriginalMember(owner = "client!rs", name = "WA", descriptor = "()I")
    @Override
    public int WA() {
        return this.anInt8495;
    }

    @OriginalMember(owner = "client!rs", name = "j", descriptor = "(I)V")
    public void method7518(@OriginalArg(0) int arg0) {
        @Pc(1) int local1 = 0;
        @Pc(5) int local5 = this.toolkit.zNear;
        @Pc(10) short local10 = this.faceA[arg0];
        @Pc(15) short local15 = this.faceB[arg0];
        @Pc(20) short local20 = this.faceC[arg0];
        @Pc(25) int local25 = this.anIntArray659[local10];
        @Pc(30) int local30 = this.anIntArray659[local15];
        @Pc(35) int local35 = this.anIntArray659[local20];
        if (this.faceAlpha == null) {
            this.aRasterizer_1.alpha = 0;
        } else {
            this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
        }
        @Pc(98) int local98;
        @Pc(103) int local103;
        @Pc(110) int local110;
        @Pc(123) int local123;
        if (local25 >= local5) {
            this.anIntArray663[0] = this.anIntArray655[local10];
            this.anIntArray676[0] = this.anIntArray657[local10];
            this.anIntArray671[0] = this.anIntArray670[local10];
            local1++;
            this.anIntArray677[0] = this.anIntArray668[arg0] & 0xFFFF;
        } else {
            local98 = this.anIntArray656[local10];
            local103 = this.anIntArray660[local10];
            local110 = this.anIntArray668[arg0] & 0xFFFF;
            if (local35 >= local5) {
                local123 = (local5 - local25) * (65536 / (local35 - local25));
                this.anIntArray663[0] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local20] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[0] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local20] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[0] = local5;
                local1++;
                this.anIntArray677[0] = local110 + (((this.anIntArray672[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (local30 >= local5) {
                local123 = (local5 - local25) * (65536 / (local30 - local25));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local15] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local15] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray664[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (local30 >= local5) {
            this.anIntArray663[local1] = this.anIntArray655[local15];
            this.anIntArray676[local1] = this.anIntArray657[local15];
            this.anIntArray671[local1] = this.anIntArray670[local15];
            this.anIntArray677[local1++] = this.anIntArray664[arg0] & 0xFFFF;
        } else {
            local98 = this.anIntArray656[local15];
            local103 = this.anIntArray660[local15];
            local110 = this.anIntArray664[arg0] & 0xFFFF;
            if (local25 >= local5) {
                local123 = (local5 - local30) * (65536 / (local25 - local30));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local10] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local10] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray668[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (local35 >= local5) {
                local123 = (local5 - local30) * (65536 / (local35 - local30));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local20] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local20] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray672[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (local35 >= local5) {
            this.anIntArray663[local1] = this.anIntArray655[local20];
            this.anIntArray676[local1] = this.anIntArray657[local20];
            this.anIntArray671[local1] = this.anIntArray670[local20];
            this.anIntArray677[local1++] = this.anIntArray672[arg0] & 0xFFFF;
        } else {
            local98 = this.anIntArray656[local20];
            local103 = this.anIntArray660[local20];
            local110 = this.anIntArray672[arg0] & 0xFFFF;
            if (local30 >= local5) {
                local123 = (local5 - local35) * (65536 / (local30 - local35));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local15] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local15] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray664[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (local25 >= local5) {
                local123 = (local5 - local35) * (65536 / (local25 - local35));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local10] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local10] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray668[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        local98 = this.anIntArray663[0];
        local103 = this.anIntArray663[1];
        local110 = this.anIntArray663[2];
        local123 = this.anIntArray676[0];
        @Pc(783) int local783 = this.anIntArray676[1];
        @Pc(788) int local788 = this.anIntArray676[2];
        local25 = this.anIntArray671[0];
        local30 = this.anIntArray671[1];
        local35 = this.anIntArray671[2];
        this.aRasterizer_1.clamp = false;
        @Pc(938) int local938;
        @Pc(961) int local961;
        if (local1 == 3) {
            if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.aJavaThreadResource_3.anInt10607 || local103 > this.aJavaThreadResource_3.anInt10607 || local110 > this.aJavaThreadResource_3.anInt10607) {
                this.aRasterizer_1.clamp = true;
            }
            if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
                local938 = -16777216;
                if (this.faceAlpha != null) {
                    local938 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
                }
                local961 = local938 | this.anIntArray668[arg0] & 0xFFFFFF;
                if (this.anIntArray672[arg0] == -1) {
                    this.aRasterizer_1.method5154((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
                } else {
                    this.aRasterizer_1.method5154((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
                }
            } else if (this.anIntArray672[arg0] == -1) {
                this.aRasterizer_1.method5144((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]);
            } else {
                this.aRasterizer_1.method5153((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, (float) this.anIntArray677[0], (float) this.anIntArray677[1], (float) this.anIntArray677[2]);
            }
        }
        if (local1 != 4) {
            return;
        }
        if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.aJavaThreadResource_3.anInt10607 || local103 > this.aJavaThreadResource_3.anInt10607 || local110 > this.aJavaThreadResource_3.anInt10607 || this.anIntArray663[3] < 0 || this.anIntArray663[3] > this.aJavaThreadResource_3.anInt10607) {
            this.aRasterizer_1.clamp = true;
        }
        if (this.faceTextures == null || this.faceTextures[arg0] == -1) {
            if (this.anIntArray672[arg0] == -1) {
                local938 = ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF];
                this.aRasterizer_1.method5144((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, local938);
                this.aRasterizer_1.method5144((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local30, (float) this.anIntArray671[3], local938);
                return;
            } else {
                this.aRasterizer_1.method5153((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, (float) this.anIntArray677[0], (float) this.anIntArray677[1], (float) this.anIntArray677[2]);
                this.aRasterizer_1.method5153((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local30, (float) this.anIntArray671[3], (float) this.anIntArray677[0], (float) this.anIntArray677[2], (float) this.anIntArray677[3]);
                return;
            }
        }
        local938 = -16777216;
        if (this.faceAlpha != null) {
            local938 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
        }
        local961 = local938 | this.anIntArray668[arg0] & 0xFFFFFF;
        if (this.anIntArray672[arg0] == -1) {
            this.aRasterizer_1.method5154((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            this.aRasterizer_1.method5154((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local35, (float) this.anIntArray671[3], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            return;
        }
        this.aRasterizer_1.method5154((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
        this.aRasterizer_1.method5154((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local35, (float) this.anIntArray671[3], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Ljava/lang/Thread;)V")
    public void method7519(@OriginalArg(0) Thread arg0) {
        @Pc(4) JavaThreadResource local4 = this.toolkit.threadResource(arg0);
        this.aRasterizer_1 = local4.rasterizer;
        if (local4 == this.aJavaThreadResource_3) {
            return;
        }
        this.aJavaThreadResource_3 = local4;
        this.anIntArray673 = this.aJavaThreadResource_3.anIntArray845;
        this.anIntArray656 = this.aJavaThreadResource_3.anIntArray854;
        this.anIntArray660 = this.aJavaThreadResource_3.anIntArray849;
        this.anIntArray659 = this.aJavaThreadResource_3.anIntArray839;
        this.anIntArray655 = this.aJavaThreadResource_3.anIntArray851;
        this.anIntArray657 = this.aJavaThreadResource_3.anIntArray846;
        this.anIntArray670 = this.aJavaThreadResource_3.anIntArray838;
        this.anIntArray662 = this.aJavaThreadResource_3.anIntArray844;
        this.anIntArray665 = this.aJavaThreadResource_3.anIntArray842;
        this.anIntArray661 = this.aJavaThreadResource_3.anIntArray837;
        this.anIntArray663 = this.aJavaThreadResource_3.anIntArray847;
        this.anIntArray676 = this.aJavaThreadResource_3.anIntArray850;
        this.anIntArray671 = this.aJavaThreadResource_3.anIntArray841;
        this.anIntArray677 = this.aJavaThreadResource_3.anIntArray848;
        this.anIntArray674 = this.aJavaThreadResource_3.anIntArray835;
        this.anIntArray667 = this.aJavaThreadResource_3.anIntArray836;
        this.anIntArray669 = this.aJavaThreadResource_3.anIntArray853;
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(IIIIIIII)Z")
    public boolean method7520(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7) {
        if (arg1 < arg2 && arg1 < arg3 && arg1 < arg4) {
            return false;
        } else if (arg1 > arg2 && arg1 > arg3 && arg1 > arg4) {
            return false;
        } else if (arg0 < arg5 && arg0 < arg6 && arg0 < arg7) {
            return false;
        } else {
            return arg0 <= arg5 || arg0 <= arg6 || arg0 <= arg7;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(Lclient!tt;IZ)V")
    @Override
    public void transform(@OriginalArg(0) Matrix matrix, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2) {
        if (this.originModels == null) {
            return;
        }
        @Pc(7) int[] local7 = new int[3];
        for (@Pc(9) int local9 = 0; local9 < this.maxVertex; local9++) {
            if ((arg1 & this.originModels[local9]) != 0) {
                if (arg2) {
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
    public void method7521(@OriginalArg(0) int arg0) {
        @Pc(1) int local1 = 0;
        @Pc(5) int local5 = this.toolkit.zNear;
        @Pc(10) short local10 = this.faceA[arg0];
        @Pc(15) short local15 = this.faceB[arg0];
        @Pc(20) short local20 = this.faceC[arg0];
        @Pc(25) int local25 = this.anIntArray659[local10];
        @Pc(30) int local30 = this.anIntArray659[local15];
        @Pc(35) int local35 = this.anIntArray659[local20];
        if (this.faceAlpha == null) {
            this.aRasterizer_1.alpha = 0;
        } else {
            this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
        }
        @Pc(98) int local98;
        @Pc(103) int local103;
        @Pc(110) int local110;
        @Pc(123) int local123;
        if (local25 >= local5) {
            this.anIntArray663[0] = this.anIntArray655[local10];
            this.anIntArray676[0] = this.anIntArray657[local10];
            this.anIntArray671[0] = this.anIntArray670[local10];
            local1++;
            this.anIntArray677[0] = this.anIntArray668[arg0] & 0xFFFF;
        } else {
            local98 = this.anIntArray656[local10];
            local103 = this.anIntArray660[local10];
            local110 = this.anIntArray668[arg0] & 0xFFFF;
            if (local35 >= local5) {
                local123 = (local5 - local25) * (65536 / (local35 - local25));
                this.anIntArray663[0] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local20] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[0] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local20] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[0] = local5;
                local1++;
                this.anIntArray677[0] = local110 + (((this.anIntArray672[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (local30 >= local5) {
                local123 = (local5 - local25) * (65536 / (local30 - local25));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local15] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local15] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray664[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (local30 >= local5) {
            this.anIntArray663[local1] = this.anIntArray655[local15];
            this.anIntArray676[local1] = this.anIntArray657[local15];
            this.anIntArray671[local1] = this.anIntArray670[local15];
            this.anIntArray677[local1++] = this.anIntArray664[arg0] & 0xFFFF;
        } else {
            local98 = this.anIntArray656[local15];
            local103 = this.anIntArray660[local15];
            local110 = this.anIntArray664[arg0] & 0xFFFF;
            if (local25 >= local5) {
                local123 = (local5 - local30) * (65536 / (local25 - local30));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local10] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local10] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray668[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (local35 >= local5) {
                local123 = (local5 - local30) * (65536 / (local35 - local30));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local20] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local20] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray672[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        if (local35 >= local5) {
            this.anIntArray663[local1] = this.anIntArray655[local20];
            this.anIntArray676[local1] = this.anIntArray657[local20];
            this.anIntArray671[local1] = this.anIntArray670[local20];
            this.anIntArray677[local1++] = this.anIntArray672[arg0] & 0xFFFF;
        } else {
            local98 = this.anIntArray656[local20];
            local103 = this.anIntArray660[local20];
            local110 = this.anIntArray672[arg0] & 0xFFFF;
            if (local30 >= local5) {
                local123 = (local5 - local35) * (65536 / (local30 - local35));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local15] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local15] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray664[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
            if (local25 >= local5) {
                local123 = (local5 - local35) * (65536 / (local25 - local35));
                this.anIntArray663[local1] = this.aJavaThreadResource_3.anInt10606 + (local98 + ((this.anIntArray656[local10] - local98) * local123 >> 16)) * this.toolkit.projectionScaleX / local5;
                this.anIntArray676[local1] = this.aJavaThreadResource_3.anInt10608 + (local103 + ((this.anIntArray660[local10] - local103) * local123 >> 16)) * this.toolkit.projectionScaleY / local5;
                this.anIntArray671[local1] = local5;
                this.anIntArray677[local1++] = local110 + (((this.anIntArray668[arg0] & 0xFFFF) - local110) * local123 >> 16);
            }
        }
        local98 = this.anIntArray663[0];
        local103 = this.anIntArray663[1];
        local110 = this.anIntArray663[2];
        local123 = this.anIntArray676[0];
        @Pc(783) int local783 = this.anIntArray676[1];
        @Pc(788) int local788 = this.anIntArray676[2];
        local25 = this.anIntArray671[0];
        local30 = this.anIntArray671[1];
        local35 = this.anIntArray671[2];
        this.aRasterizer_1.clamp = false;
        @Pc(938) int local938;
        @Pc(961) int local961;
        if (local1 == 3) {
            if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.aJavaThreadResource_3.anInt10607 || local103 > this.aJavaThreadResource_3.anInt10607 || local110 > this.aJavaThreadResource_3.anInt10607) {
                this.aRasterizer_1.clamp = true;
            }
            if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
                local938 = -16777216;
                if (this.faceAlpha != null) {
                    local938 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
                }
                local961 = local938 | this.anIntArray668[arg0] & 0xFFFFFF;
                if (this.anIntArray672[arg0] == -1) {
                    this.aRasterizer_1.renderTexturedTriangle((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
                } else {
                    this.aRasterizer_1.renderTexturedTriangle((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
                }
            } else if (this.anIntArray672[arg0] == -1) {
                this.aRasterizer_1.renderFlatTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]);
            } else {
                this.aRasterizer_1.method5156((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, (float) this.anIntArray677[0], (float) this.anIntArray677[1], (float) this.anIntArray677[2]);
            }
        }
        if (local1 != 4) {
            return;
        }
        if (local98 < 0 || local103 < 0 || local110 < 0 || local98 > this.aJavaThreadResource_3.anInt10607 || local103 > this.aJavaThreadResource_3.anInt10607 || local110 > this.aJavaThreadResource_3.anInt10607 || this.anIntArray663[3] < 0 || this.anIntArray663[3] > this.aJavaThreadResource_3.anInt10607) {
            this.aRasterizer_1.clamp = true;
        }
        if (this.faceTextures == null || this.faceTextures[arg0] == -1) {
            if (this.anIntArray672[arg0] == -1) {
                local938 = ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF];
                this.aRasterizer_1.renderFlatTriangleRgb((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, local938);
                this.aRasterizer_1.renderFlatTriangleRgb((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local30, (float) this.anIntArray671[3], local938);
                return;
            } else {
                this.aRasterizer_1.method5156((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, (float) this.anIntArray677[0], (float) this.anIntArray677[1], (float) this.anIntArray677[2]);
                this.aRasterizer_1.method5156((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local30, (float) this.anIntArray671[3], (float) this.anIntArray677[0], (float) this.anIntArray677[2], (float) this.anIntArray677[3]);
                return;
            }
        }
        local938 = -16777216;
        if (this.faceAlpha != null) {
            local938 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
        }
        local961 = local938 | this.anIntArray668[arg0] & 0xFFFFFF;
        if (this.anIntArray672[arg0] == -1) {
            this.aRasterizer_1.renderTexturedTriangle((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            this.aRasterizer_1.renderTexturedTriangle((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local35, (float) this.anIntArray671[3], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            return;
        }
        this.aRasterizer_1.renderTexturedTriangle((float) local123, (float) local783, (float) local788, (float) local98, (float) local103, (float) local110, (float) local25, (float) local30, (float) local35, this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
        this.aRasterizer_1.renderTexturedTriangle((float) local123, (float) local788, (float) this.anIntArray676[3], (float) local98, (float) local110, (float) this.anIntArray663[3], (float) local25, (float) local35, (float) this.anIntArray671[3], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local961, local961, local961, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(IZZ)V")
    public void method7522(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) boolean arg2) {
        if (this.anIntArray672[arg0] == -2) {
            return;
        }
        @Pc(12) short local12 = this.faceA[arg0];
        @Pc(17) short local17 = this.faceB[arg0];
        @Pc(22) short local22 = this.faceC[arg0];
        @Pc(27) int local27 = this.anIntArray655[local12];
        @Pc(32) int local32 = this.anIntArray655[local17];
        @Pc(37) int local37 = this.anIntArray655[local22];
        @Pc(59) int local59;
        if (arg1 && (local27 == -5000 || local32 == -5000 || local37 == -5000)) {
            local59 = this.anIntArray656[local12];
            @Pc(64) int local64 = this.anIntArray656[local17];
            @Pc(69) int local69 = this.anIntArray656[local22];
            @Pc(74) int local74 = this.anIntArray660[local12];
            @Pc(79) int local79 = this.anIntArray660[local17];
            @Pc(84) int local84 = this.anIntArray660[local22];
            @Pc(89) int local89 = this.anIntArray659[local12];
            @Pc(94) int local94 = this.anIntArray659[local17];
            @Pc(99) int local99 = this.anIntArray659[local22];
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
                this.method7521(arg0);
                return;
            }
        } else if (this.anIntArray669[arg0] != -1 || (local27 - local32) * (this.anIntArray657[local22] - this.anIntArray657[local17]) - (this.anIntArray657[local12] - this.anIntArray657[local17]) * (local37 - local32) > 0) {
            if (local27 >= 0 && local32 >= 0 && local37 >= 0 && local27 <= this.aJavaThreadResource_3.anInt10607 && local32 <= this.aJavaThreadResource_3.anInt10607 && local37 <= this.aJavaThreadResource_3.anInt10607) {
                this.aRasterizer_1.clamp = false;
            } else {
                this.aRasterizer_1.clamp = true;
            }
            if (arg2) {
                local59 = this.anIntArray669[arg0];
                if (local59 == -1 || !this.billboardFaces[local59].aBoolean464) {
                    this.method7507(arg0);
                }
                return;
            }
            local59 = this.anIntArray669[arg0];
            if (local59 != -1) {
                @Pc(280) JavaBillboardFace local280 = this.billboardFaces[local59];
                @Pc(285) JavaBillboardAttributes local285 = this.billboardAttributes[local59];
                if (!local280.aBoolean464) {
                    this.method7533(arg0);
                }
                this.toolkit.method3791(local285.anInt6221, local285.anInt6227, local285.anInt6224, local285.anInt6232, local285.anInt6220, local285.anInt6231, local280.aShort72 & 0xFFFF, local285.anInt6225, local280.aByte98, local280.aByte97);
                return;
            }
            this.method7533(arg0);
        }
    }

    @OriginalMember(owner = "client!rs", name = "C", descriptor = "(I)V")
    @Override
    public void C(@OriginalArg(0) int ambient) {
        if ((this.functionMask & 0x1000) != 4096) {
            throw new IllegalStateException();
        }
        this.anInt8495 = ambient;
        this.anInt8488 = 0;
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
        this.method7528(matrix, cylinder, -1, flags);
    }

    @OriginalMember(owner = "client!rs", name = "FA", descriptor = "(I)V")
    @Override
    public void FA(@OriginalArg(0) int arg0) {
        if ((this.functionMask & 0x6) != 6) {
            throw new IllegalStateException();
        }
        @Pc(14) int local14 = Trig1.SIN[arg0];
        @Pc(18) int local18 = Trig1.COS[arg0];
        synchronized (this) {
            for (@Pc(24) int local24 = 0; local24 < this.vertexCount; local24++) {
                @Pc(41) int local41 = this.vertexY[local24] * local18 - this.vertexZ[local24] * local14 >> 14;
                this.vertexZ[local24] = this.vertexY[local24] * local14 + this.vertexZ[local24] * local18 >> 14;
                this.vertexY[local24] = local41;
            }
            this.method7504();
        }
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "()[B")
    @Override
    public byte[] getFaceAlphas() {
        return this.faceAlpha;
    }

    @OriginalMember(owner = "client!rs", name = "t", descriptor = "()V")
    public void method7523() {
        if (this.anInt8488 != 0 || this.aClass378Array2 != null) {
            return;
        }
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.method7510();
            }
        } else {
            this.method7510();
        }
    }

    @OriginalMember(owner = "client!rs", name = "da", descriptor = "()I")
    @Override
    public int da() {
        return this.anInt8485;
    }

    @OriginalMember(owner = "client!rs", name = "ua", descriptor = "()I")
    @Override
    public int ua() {
        return this.functionMask;
    }

    @OriginalMember(owner = "client!rs", name = "e", descriptor = "(I)I")
    public int method7524(@OriginalArg(0) int arg0) {
        if (arg0 < 2) {
            arg0 = 2;
        } else if (arg0 > 126) {
            arg0 = 126;
        }
        return arg0;
    }

    @OriginalMember(owner = "client!rs", name = "V", descriptor = "()I")
    @Override
    public int V() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort115;
    }

    @OriginalMember(owner = "client!rs", name = "k", descriptor = "()V")
    public void method7525() {
        if (this.aBoolean652) {
            return;
        }
        @Pc(6) int local6 = 0;
        @Pc(8) int local8 = 0;
        @Pc(10) int local10 = 32767;
        @Pc(12) int local12 = 32767;
        @Pc(14) int local14 = 32767;
        @Pc(16) int local16 = -32768;
        @Pc(18) int local18 = -32768;
        @Pc(20) int local20 = -32768;
        for (@Pc(22) int local22 = 0; local22 < this.maxVertex; local22++) {
            @Pc(28) int local28 = this.vertexX[local22];
            @Pc(33) int local33 = this.vertexY[local22];
            @Pc(38) int local38 = this.vertexZ[local22];
            if (local28 < local10) {
                local10 = local28;
            }
            if (local28 > local16) {
                local16 = local28;
            }
            if (local33 < local12) {
                local12 = local33;
            }
            if (local33 > local18) {
                local18 = local33;
            }
            if (local38 < local14) {
                local14 = local38;
            }
            if (local38 > local20) {
                local20 = local38;
            }
            @Pc(76) int local76 = local28 * local28 + local38 * local38;
            if (local76 > local6) {
                local6 = local76;
            }
            local76 += local33 * local33;
            if (local76 > local8) {
                local8 = local76;
            }
        }
        this.aShort115 = (short) local10;
        this.aShort117 = (short) local16;
        this.aShort116 = (short) local12;
        this.aShort113 = (short) local18;
        this.aShort112 = (short) local14;
        this.aShort111 = (short) local20;
        this.aShort118 = (short) (int) (Math.sqrt(local6) + 0.99D);
        this.aShort114 = (short) (int) (Math.sqrt(local8) + 0.99D);
        this.aBoolean652 = true;
    }

    @OriginalMember(owner = "client!rs", name = "d", descriptor = "(I)Z")
    public boolean method7526(@OriginalArg(0) int arg0) {
        if (this.anIntArray669 == null) {
            return false;
        } else {
            return this.anIntArray669[arg0] != -1;
        }
    }

    @OriginalMember(owner = "client!rs", name = "h", descriptor = "()V")
    public void method7527() {
        for (@Pc(1) int local1 = 0; local1 < this.faceCount; local1++) {
            @Pc(13) short local13 = this.faceTextures == null ? -1 : this.faceTextures[local1];
            if (local13 == -1) {
                @Pc(23) int local23 = this.faceColour[local1] & 0xFFFF;
                @Pc(32) int local32 = (local23 & 0x7F) * this.anInt8495 >> 7;
                @Pc(40) short local40 = ColourUtils.hslToHsv(local23 & 0xFFFFFF80 | local32);
                @Pc(53) int local53;
                if (this.anIntArray672[local1] == -1) {
                    local53 = this.anIntArray668[local1] & 0xFFFE0000;
                    this.anIntArray668[local1] = local53 | Static244.scaleHslLightness(local53 >> 17, local40);
                } else if (this.anIntArray672[local1] != -2) {
                    local53 = this.anIntArray668[local1] & 0xFFFE0000;
                    this.anIntArray668[local1] = local53 | Static244.scaleHslLightness(local53 >> 17, local40);
                    local53 = this.anIntArray664[local1] & 0xFFFE0000;
                    this.anIntArray664[local1] = local53 | Static244.scaleHslLightness(local53 >> 17, local40);
                    local53 = this.anIntArray672[local1] & 0xFFFE0000;
                    this.anIntArray672[local1] = local53 | Static244.scaleHslLightness(local53 >> 17, local40);
                }
            }
        }
        this.anInt8488 = 2;
    }

    @OriginalMember(owner = "client!rs", name = "fa", descriptor = "()I")
    @Override
    public int fa() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort116;
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
    public void method7528(@OriginalArg(0) Matrix arg0, @OriginalArg(1) PickingCylinder arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        if (this.maxVertex < 1) {
            return;
        }
        this.aClass73_Sub2_2 = (JavaMatrix) arg0;
        @Pc(13) JavaMatrix local13 = this.toolkit.camera;
        if (!this.aBoolean652) {
            this.method7525();
        }
        @Pc(22) boolean local22 = false;
        if (this.aClass73_Sub2_2.e1_1 == 16384.0F && this.aClass73_Sub2_2.e1_2 == 0.0F && this.aClass73_Sub2_2.e1_3 == 0.0F && this.aClass73_Sub2_2.e2_1 == 0.0F && this.aClass73_Sub2_2.e2_2 == 16384.0F && this.aClass73_Sub2_2.e2_3 == 0.0F && this.aClass73_Sub2_2.e3_1 == 0.0F && this.aClass73_Sub2_2.e3_2 == 0.0F && this.aClass73_Sub2_2.e3_3 == 16384.0F) {
            local22 = true;
        }
        @Pc(118) float local118 = local13.tz + local13.e3_1 * this.aClass73_Sub2_2.tx + local13.e3_2 * this.aClass73_Sub2_2.ty + local13.e3_3 * this.aClass73_Sub2_2.tz;
        @Pc(145) float local145 = local22 ? local13.e3_2 : local13.e3_1 * this.aClass73_Sub2_2.e1_2 + local13.e3_2 * this.aClass73_Sub2_2.e2_2 + local13.e3_3 * this.aClass73_Sub2_2.e3_2;
        @Pc(154) int local154 = (int) (local118 + (float) this.aShort116 * local145);
        @Pc(163) int local163 = (int) (local118 + (float) this.aShort113 * local145);
        @Pc(171) int local171;
        @Pc(176) int local176;
        if (local154 > local163) {
            local171 = local163 - this.aShort118;
            local176 = local154 + this.aShort118;
        } else {
            local171 = local154 - this.aShort118;
            local176 = local163 + this.aShort118;
        }
        if (local171 >= this.toolkit.zFar || local176 <= this.toolkit.zNear) {
            return;
        }
        @Pc(225) float local225 = local13.tx + local13.e1_1 * this.aClass73_Sub2_2.tx + local13.e1_2 * this.aClass73_Sub2_2.ty + local13.e1_3 * this.aClass73_Sub2_2.tz;
        @Pc(252) float local252 = local22 ? local13.e1_2 : local13.e1_1 * this.aClass73_Sub2_2.e1_2 + local13.e1_2 * this.aClass73_Sub2_2.e2_2 + local13.e1_3 * this.aClass73_Sub2_2.e3_2;
        @Pc(261) int local261 = (int) (local225 + (float) this.aShort116 * local252);
        @Pc(270) int local270 = (int) (local225 + (float) this.aShort113 * local252);
        @Pc(282) int local282;
        @Pc(291) int local291;
        if (local261 > local270) {
            local282 = (local270 - this.aShort118) * this.toolkit.projectionScaleX;
            local291 = (local261 + this.aShort118) * this.toolkit.projectionScaleX;
        } else {
            local282 = (local261 - this.aShort118) * this.toolkit.projectionScaleX;
            local291 = (local270 + this.aShort118) * this.toolkit.projectionScaleX;
        }
        if (arg2 == -1) {
            if (local282 / local176 >= this.toolkit.viewX2) {
                return;
            }
            if (local291 / local176 <= this.toolkit.viewX1) {
                return;
            }
        } else if (local282 / arg2 >= this.toolkit.viewX2) {
            return;
        } else if (local291 / arg2 <= this.toolkit.viewX1) {
            return;
        }
        @Pc(375) float local375 = local13.ty + local13.e2_1 * this.aClass73_Sub2_2.tx + local13.e2_2 * this.aClass73_Sub2_2.ty + local13.e2_3 * this.aClass73_Sub2_2.tz;
        @Pc(402) float local402 = local22 ? local13.e2_2 : local13.e2_1 * this.aClass73_Sub2_2.e1_2 + local13.e2_2 * this.aClass73_Sub2_2.e2_2 + local13.e2_3 * this.aClass73_Sub2_2.e3_2;
        @Pc(411) int local411 = (int) (local375 + (float) this.aShort116 * local402);
        @Pc(420) int local420 = (int) (local375 + (float) this.aShort113 * local402);
        @Pc(432) int local432;
        @Pc(441) int local441;
        if (local411 > local420) {
            local432 = (local420 - this.aShort118) * this.toolkit.projectionScaleY;
            local441 = (local411 + this.aShort118) * this.toolkit.projectionScaleY;
        } else {
            local432 = (local411 - this.aShort118) * this.toolkit.projectionScaleY;
            local441 = (local420 + this.aShort118) * this.toolkit.projectionScaleY;
        }
        if (arg2 == -1) {
            if (local432 / local176 >= this.toolkit.vewY2) {
                return;
            }
            if (local441 / local176 <= this.toolkit.viewY1) {
                return;
            }
        } else if (local432 / arg2 >= this.toolkit.vewY2) {
            return;
        } else if (local441 / arg2 <= this.toolkit.viewY1) {
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
            local506 = local13.e1_1 * this.aClass73_Sub2_2.e1_1 + local13.e1_2 * this.aClass73_Sub2_2.e2_1 + local13.e1_3 * this.aClass73_Sub2_2.e3_1;
            local509 = local13.e2_1 * this.aClass73_Sub2_2.e1_1 + local13.e2_2 * this.aClass73_Sub2_2.e2_1 + local13.e2_3 * this.aClass73_Sub2_2.e3_1;
            local512 = local13.e3_1 * this.aClass73_Sub2_2.e1_1 + local13.e3_2 * this.aClass73_Sub2_2.e2_1 + local13.e3_3 * this.aClass73_Sub2_2.e3_1;
            local515 = local13.e1_1 * this.aClass73_Sub2_2.e1_3 + local13.e1_2 * this.aClass73_Sub2_2.e2_3 + local13.e1_3 * this.aClass73_Sub2_2.e3_3;
            local518 = local13.e2_1 * this.aClass73_Sub2_2.e1_3 + local13.e2_2 * this.aClass73_Sub2_2.e2_3 + local13.e2_3 * this.aClass73_Sub2_2.e3_3;
            local521 = local13.e3_1 * this.aClass73_Sub2_2.e1_3 + local13.e3_2 * this.aClass73_Sub2_2.e2_3 + local13.e3_3 * this.aClass73_Sub2_2.e3_3;
        }
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                while (this.aBoolean646) {
                    try {
                        this.wait();
                    } catch (@Pc(662) InterruptedException local662) {
                    }
                }
                this.aBoolean646 = true;
            }
        }
        this.method7519(Thread.currentThread());
        if ((arg3 & 0x2) == 0) {
            this.aRasterizer_1.method5142(false);
        } else {
            this.aRasterizer_1.method5142(true);
        }
        @Pc(694) boolean local694 = false;
        @Pc(704) boolean local704 = local171 <= this.toolkit.zNear;
        @Pc(721) boolean local721 = local704 || this.emitters != null || this.effectors != null;
        this.aJavaThreadResource_3.anInt10607 = this.aRasterizer_1.width;
        this.aJavaThreadResource_3.anInt10606 = this.aRasterizer_1.minX;
        this.aJavaThreadResource_3.anInt10608 = this.aRasterizer_1.minY;
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
        if (arg2 == -1) {
            for (local756 = 0; local756 < this.vertexCount; local756++) {
                local762 = this.vertexX[local756];
                local767 = this.vertexY[local756];
                local772 = this.vertexZ[local756];
                local789 = local225 + local506 * (float) local762 + local252 * (float) local767 + local515 * (float) local772;
                local806 = local375 + local509 * (float) local762 + local402 * (float) local767 + local518 * (float) local772;
                local823 = local118 + local512 * (float) local762 + local145 * (float) local767 + local521 * (float) local772;
                this.anIntArray670[local756] = (int) local823;
                if (local823 >= (float) local751) {
                    this.anIntArray655[local756] = this.aJavaThreadResource_3.anInt10606 + (int) (local789 * (float) local743 / local823);
                    this.anIntArray657[local756] = this.aJavaThreadResource_3.anInt10608 + (int) (local806 * (float) local747 / local823);
                } else {
                    this.anIntArray655[local756] = -5000;
                    local694 = true;
                }
                if (local721) {
                    this.anIntArray656[local756] = (int) local789;
                    this.anIntArray660[local756] = (int) local806;
                    this.anIntArray659[local756] = (int) local823;
                }
                if (this.aJavaThreadResource_3.water) {
                    this.anIntArray673[local756] = (int) (this.aClass73_Sub2_2.ty + this.aClass73_Sub2_2.e2_1 * (float) local762 + this.aClass73_Sub2_2.e2_2 * (float) local767 + this.aClass73_Sub2_2.e2_3 * (float) local772);
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
                this.anIntArray670[local756] = (int) local823;
                this.anIntArray655[local756] = this.aJavaThreadResource_3.anInt10606 + (int) (local789 * (float) local743 / (float) arg2);
                this.anIntArray657[local756] = this.aJavaThreadResource_3.anInt10608 + (int) (local806 * (float) local747 / (float) arg2);
                if (local721) {
                    this.anIntArray656[local756] = (int) local789;
                    this.anIntArray660[local756] = (int) local806;
                    this.anIntArray659[local756] = arg2;
                }
                if (this.aJavaThreadResource_3.water) {
                    this.anIntArray673[local756] = (int) (this.aClass73_Sub2_2.ty + this.aClass73_Sub2_2.e2_1 * (float) local762 + this.aClass73_Sub2_2.e2_2 * (float) local767 + this.aClass73_Sub2_2.e2_3 * (float) local772);
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
                    local953.anInt6221 = this.toolkit.projectionCenterX + (int) (local789 * (float) local743 / (float) arg2);
                    local953.anInt6227 = this.toolkit.projectionCenterY + (int) (local806 * (float) local747 / (float) arg2);
                    local953.anInt6224 = arg2 - local948.anInt6140;
                    local953.anInt6232 = local953.anInt6223 * local948.aShort71 * local743 / (arg2 << 7);
                    local953.anInt6220 = local953.anInt6226 * local948.aShort73 * local747 / (arg2 << 7);
                }
            }
        }
        @Pc(1543) boolean local1543;
        if (arg1 != null) {
            local1543 = false;
            @Pc(1545) boolean local1545 = true;
            @Pc(1553) int local1553 = this.aShort115 + this.aShort117 >> 1;
            @Pc(1561) int local1561 = this.aShort112 + this.aShort111 >> 1;
            @Pc(1566) short local1566 = this.aShort116;
            local789 = local225 + local506 * (float) local1553 + local252 * (float) local1566 + local515 * (float) local1561;
            local806 = local375 + local509 * (float) local1553 + local402 * (float) local1566 + local518 * (float) local1561;
            local823 = local118 + local512 * (float) local1553 + local145 * (float) local1566 + local521 * (float) local1561;
            if (local823 >= (float) local751) {
                @Pc(1627) int local1627 = (int) local823;
                if (arg2 != -1) {
                    local1627 = arg2;
                }
                arg1.anInt4504 = this.toolkit.projectionCenterX + (int) (local789 * (float) local743 / (float) local1627);
                arg1.anInt4505 = this.toolkit.projectionCenterY + (int) (local806 * (float) local747 / (float) local1627);
            } else {
                local1543 = true;
            }
            local1566 = this.aShort113;
            @Pc(1687) float local1687 = local225 + local506 * (float) local1553 + local252 * (float) local1566 + local515 * (float) local1561;
            @Pc(1704) float local1704 = local375 + local509 * (float) local1553 + local402 * (float) local1566 + local518 * (float) local1561;
            @Pc(1721) float local1721 = local118 + local512 * (float) local1553 + local145 * (float) local1566 + local521 * (float) local1561;
            @Pc(1729) int local1729;
            if (local1721 >= (float) local751) {
                local1729 = (int) local1721;
                if (arg2 != -1) {
                    local1729 = arg2;
                }
                arg1.anInt4501 = this.toolkit.projectionCenterX + (int) (local1687 * (float) local743 / (float) local1729);
                arg1.anInt4503 = this.toolkit.projectionCenterY + (int) (local1704 * (float) local747 / (float) local1729);
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
                        if (arg2 != -1) {
                            local1820 = arg2;
                        }
                        arg1.anInt4504 = this.toolkit.projectionCenterX + local1809 * local743 / local1820;
                        arg1.anInt4505 = this.toolkit.projectionCenterY + local1818 * local747 / local1820;
                    } else if (local1721 < (float) local751) {
                        local1800 = (local823 - (float) local751) / (local823 - local1721);
                        local1809 = (int) (local789 + (local789 - local1687) * local1800);
                        local1818 = (int) (local806 + (local806 - local1704) * local1800);
                        local1820 = local751;
                        if (arg2 != -1) {
                            local1820 = arg2;
                        }
                        arg1.anInt4504 = this.toolkit.projectionCenterX + local1809 * local743 / local1820;
                        arg1.anInt4505 = this.toolkit.projectionCenterY + local1818 * local747 / local1820;
                    }
                }
            }
            if (local1545) {
                if (local823 > local1721) {
                    local1729 = (int) local823;
                    if (arg2 != -1) {
                        local1729 = arg2;
                    }
                    arg1.anInt4502 = this.toolkit.projectionCenterX + (int) ((local789 + (float) this.aShort118) * (float) local743 / (float) local1729) - arg1.anInt4504;
                } else {
                    local1729 = (int) local1721;
                    if (arg2 != -1) {
                        local1729 = arg2;
                    }
                    arg1.anInt4502 = this.toolkit.projectionCenterX + (int) ((local1687 + (float) this.aShort118) * (float) local743 / (float) local1729) - arg1.anInt4501;
                }
                arg1.aBoolean352 = true;
            }
        }
        this.method7503(true);
        this.aRasterizer_1.fastScanline = (arg3 & 0x1) == 0;
        this.aRasterizer_1.halfBlend = false;
        try {
            local1543 = (arg3 & 0x4) != 0;
            if (local1543) {
                this.method7516(local694, this.aJavaThreadResource_3.fogActive && local176 > this.aJavaThreadResource_3.fogPlane || this.aJavaThreadResource_3.water, local171, local176 - local171);
            } else {
                this.method7529(local694, this.aJavaThreadResource_3.fogActive && local176 > this.aJavaThreadResource_3.fogPlane || this.aJavaThreadResource_3.water, local171, local176 - local171);
            }
        } catch (@Pc(2068) Exception local2068) {
        }
        if (this.billboardFaces != null) {
            for (local756 = 0; local756 < this.faceCount; local756++) {
                this.anIntArray669[local756] = -1;
            }
        }
        this.aRasterizer_1 = null;
        if (this.toolkit.threadCount > 1) {
            synchronized (this) {
                this.aBoolean646 = false;
                this.notifyAll();
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "s", descriptor = "(I)V")
    @Override
    public void s(@OriginalArg(0) int functionMask) {
        if (this.toolkit.threadCount <= 1) {
            if ((this.functionMask & 0x10000) == 65536 && (functionMask & 0x10000) == 0) {
                this.method7503(true);
            }
            this.functionMask = functionMask;
            return;
        }
        synchronized (this) {
            if ((this.functionMask & 0x10000) == 65536 && (functionMask & 0x10000) == 0) {
                this.method7503(true);
            }
            this.functionMask = functionMask;
        }
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(ZZII)V")
    public void method7529(@OriginalArg(0) boolean arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(4) int local4;
        if (this.billboardFaces != null) {
            local4 = 0;
            while (local4 < this.billboardCount) {
                @Pc(10) JavaBillboardFace local10 = this.billboardFaces[local4];
                this.anIntArray669[local10.anInt6139] = local4++;
            }
        }
        if (!this.transparent && this.billboardFaces == null) {
            for (local4 = 0; local4 < this.faceCount; local4++) {
                this.method7522(local4, arg0, arg1);
            }
        } else if ((this.functionMask & 0x100) == 0 && this.faceIndices != null) {
            for (local4 = 0; local4 < this.faceCount; local4++) {
                @Pc(51) short local51 = this.faceIndices[local4];
                this.method7522(local51, arg0, arg1);
            }
        } else {
            for (local4 = 0; local4 < this.faceCount; local4++) {
                if (!this.method7506(local4) && !this.method7526(local4)) {
                    this.method7522(local4, arg0, arg1);
                }
            }
            @Pc(95) int local95;
            if (this.facePriority == null) {
                for (local95 = 0; local95 < this.faceCount; local95++) {
                    if (this.method7506(local95) || this.method7526(local95)) {
                        this.method7522(local95, arg0, arg1);
                    }
                }
            } else {
                for (local95 = 0; local95 < 12; local95++) {
                    for (@Pc(125) int local125 = 0; local125 < this.faceCount; local125++) {
                        if (this.facePriority[local125] == local95 && (this.method7506(local125) || this.method7526(local125))) {
                            this.method7522(local125, arg0, arg1);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!rs", name = "ma", descriptor = "()I")
    @Override
    public int ma() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort114;
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
    public void method7530() {
        synchronized (this) {
            for (@Pc(5) int local5 = 0; local5 < this.maxVertex; local5++) {
                this.vertexX[local5] = -this.vertexX[local5];
                this.vertexZ[local5] = -this.vertexZ[local5];
                if (this.aClass378Array2[local5] != null) {
                    this.aClass378Array2[local5].anInt9746 = -this.aClass378Array2[local5].anInt9746;
                    this.aClass378Array2[local5].anInt9745 = -this.aClass378Array2[local5].anInt9745;
                }
            }
            @Pc(65) int local65;
            if (this.aClass301Array1 != null) {
                for (local65 = 0; local65 < this.faceCount; local65++) {
                    if (this.aClass301Array1[local65] != null) {
                        this.aClass301Array1[local65].anInt7682 = -this.aClass301Array1[local65].anInt7682;
                        this.aClass301Array1[local65].anInt7681 = -this.aClass301Array1[local65].anInt7681;
                    }
                }
            }
            for (local65 = this.maxVertex; local65 < this.vertexCount; local65++) {
                this.vertexX[local65] = -this.vertexX[local65];
                this.vertexZ[local65] = -this.vertexZ[local65];
            }
            this.anInt8488 = 0;
            this.aBoolean652 = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(ISI)I")
    public int method7531(@OriginalArg(0) int arg0, @OriginalArg(1) short arg1, @OriginalArg(2) int arg2) {
        @Pc(6) int local6 = ColourUtils.HSL_TO_RGB[this.method7534(arg0, arg2)];
        @Pc(15) TextureMetrics local15 = this.toolkit.textureSource.getMetrics(arg1 & 0xFFFF);
        @Pc(20) int local20 = local15.alpha & 0xFF;
        @Pc(26) int local26;
        @Pc(38) int local38;
        if (local20 != 0) {
            local26 = arg2 * 131586;
            if (local20 == 256) {
                local6 = local26;
            } else {
                local38 = 256 - local20;
                local6 = ((local26 & 0xFF00FF) * local20 + (local6 & 0xFF00FF) * local38 & 0xFF00FF00) + ((local26 & 0xFF00) * local20 + (local6 & 0xFF00) * local38 & 0xFF0000) >> 8;
            }
        }
        local26 = local15.aByte57 & 0xFF;
        if (local26 != 0) {
            local26 += 256;
            @Pc(84) int local84 = (local6 >> 16 & 0xFF) * local26;
            if (local84 > 65535) {
                local84 = 65535;
            }
            local38 = (local6 >> 8 & 0xFF) * local26;
            if (local38 > 65535) {
                local38 = 65535;
            }
            @Pc(108) int local108 = (local6 & 0xFF) * local26;
            if (local108 > 65535) {
                local108 = 65535;
            }
            local6 = ((local84 & 0xFF00) << 8) + (local38 & 0xFF00) + (local108 >> 8);
        }
        return local6;
    }

    @OriginalMember(owner = "client!rs", name = "VA", descriptor = "(I)V")
    @Override
    public void VA(@OriginalArg(0) int arg0) {
        if ((this.functionMask & 0x3) != 3) {
            throw new IllegalStateException();
        }
        @Pc(14) int local14 = Trig1.SIN[arg0];
        @Pc(18) int local18 = Trig1.COS[arg0];
        synchronized (this) {
            for (@Pc(24) int local24 = 0; local24 < this.vertexCount; local24++) {
                @Pc(41) int local41 = this.vertexY[local24] * local14 + this.vertexX[local24] * local18 >> 14;
                this.vertexY[local24] = this.vertexY[local24] * local18 - this.vertexX[local24] * local14 >> 14;
                this.vertexX[local24] = local41;
            }
            this.method7504();
        }
    }

    @OriginalMember(owner = "client!rs", name = "s", descriptor = "()V")
    public void method7532() {
        synchronized (this) {
            @Pc(11) int local11;
            for (@Pc(5) int local5 = 0; local5 < this.maxVertex; local5++) {
                local11 = this.vertexZ[local5];
                this.vertexZ[local5] = this.vertexX[local5];
                this.vertexX[local5] = -local11;
                if (this.aClass378Array2[local5] != null) {
                    local11 = this.aClass378Array2[local5].anInt9745;
                    this.aClass378Array2[local5].anInt9745 = this.aClass378Array2[local5].anInt9746;
                    this.aClass378Array2[local5].anInt9746 = -local11;
                }
            }
            @Pc(77) int local77;
            if (this.aClass301Array1 != null) {
                for (local11 = 0; local11 < this.faceCount; local11++) {
                    if (this.aClass301Array1[local11] != null) {
                        local77 = this.aClass301Array1[local11].anInt7681;
                        this.aClass301Array1[local11].anInt7681 = this.aClass301Array1[local11].anInt7682;
                        this.aClass301Array1[local11].anInt7682 = -local77;
                    }
                }
            }
            for (local11 = this.maxVertex; local11 < this.vertexCount; local11++) {
                local77 = this.vertexZ[local11];
                this.vertexZ[local11] = this.vertexX[local11];
                this.vertexX[local11] = -local77;
            }
            this.anInt8488 = 0;
            this.aBoolean652 = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "G", descriptor = "()I")
    @Override
    public int G() {
        if (!this.aBoolean652) {
            this.method7525();
        }
        return this.aShort111;
    }

    @OriginalMember(owner = "client!rs", name = "l", descriptor = "(I)V")
    public void method7533(@OriginalArg(0) int arg0) {
        @Pc(4) short local4 = this.faceA[arg0];
        @Pc(9) short local9 = this.faceB[arg0];
        @Pc(14) short local14 = this.faceC[arg0];
        if (this.faceTextures != null && this.faceTextures[arg0] != -1) {
            @Pc(181) int local181 = -16777216;
            if (this.faceAlpha != null) {
                local181 = 255 - (this.faceAlpha[arg0] & 0xFF) << 24;
            }
            if (this.anIntArray672[arg0] == -1) {
                @Pc(210) int local210 = local181 | this.anIntArray668[arg0] & 0xFFFFFF;
                this.aRasterizer_1.renderTexturedTriangle((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local210, local210, local210, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            } else {
                this.aRasterizer_1.renderTexturedTriangle((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], this.texCoordU[arg0][0], this.texCoordU[arg0][1], this.texCoordU[arg0][2], this.texCoordV[arg0][0], this.texCoordV[arg0][1], this.texCoordV[arg0][2], local181 | this.anIntArray668[arg0] & 0xFFFFFF, local181 | this.anIntArray664[arg0] & 0xFFFFFF, local181 | this.anIntArray672[arg0] & 0xFFFFFF, this.aJavaThreadResource_3.fogColour, 0, 0, 0, this.faceTextures[arg0]);
            }
            return;
        }
        if (this.faceAlpha == null) {
            this.aRasterizer_1.alpha = 0;
        } else {
            this.aRasterizer_1.alpha = this.faceAlpha[arg0] & 0xFF;
        }
        if (this.anIntArray672[arg0] == -1) {
            this.aRasterizer_1.renderFlatTriangleRgb((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], ColourUtils.HSV_TO_RGB[this.anIntArray668[arg0] & 0xFFFF]);
        } else {
            this.aRasterizer_1.method5156((float) this.anIntArray657[local4], (float) this.anIntArray657[local9], (float) this.anIntArray657[local14], (float) this.anIntArray655[local4], (float) this.anIntArray655[local9], (float) this.anIntArray655[local14], (float) this.anIntArray670[local4], (float) this.anIntArray670[local9], (float) this.anIntArray670[local14], (float) (this.anIntArray668[arg0] & 0xFFFF), (float) (this.anIntArray664[arg0] & 0xFFFF), (float) (this.anIntArray672[arg0] & 0xFFFF));
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
            this.anInt8493 = 0;
            this.anInt8490 = 0;
            this.anInt8487 = 0;
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
        if (!this.aBoolean652) {
            this.method7525();
        }
        @Pc(38) int local38 = x + this.aShort115;
        @Pc(43) int local43 = x + this.aShort117;
        @Pc(48) int local48 = z + this.aShort112;
        @Pc(53) int local53 = z + this.aShort111;
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
                        local236 = (this.vertexY[local228] << 16) / this.aShort116;
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
                        local243 = (this.vertexY[local236] << 16) / this.aShort116;
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
                    local228 = this.aShort113 - this.aShort116;
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
                    local228 = this.aShort113 - this.aShort116;
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
            this.aBoolean652 = false;
        }
    }

    @OriginalMember(owner = "client!rs", name = "a", descriptor = "(II)I")
    public int method7534(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        arg1 = arg1 * (arg0 & 0x7F) >> 7;
        if (arg1 < 2) {
            arg1 = 2;
        } else if (arg1 > 126) {
            arg1 = 126;
        }
        return (arg0 & 0xFF80) + arg1;
    }

    @OriginalMember(owner = "client!rs", name = "b", descriptor = "(Ljava/lang/Thread;)V")
    public void method7535(@OriginalArg(0) Thread arg0) {
        @Pc(4) JavaThreadResource local4 = this.toolkit.threadResource(arg0);
        if (local4 != this.aJavaThreadResource_2) {
            this.aJavaThreadResource_2 = local4;
            this.aClass114_Sub3Array2 = this.aJavaThreadResource_2.aClass114_Sub3Array4;
            this.aClass114_Sub3Array1 = this.aJavaThreadResource_2.aClass114_Sub3Array3;
        }
    }
}

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
import com.jagex.graphics.texture.TextureOp;
import com.jagex.math.ColourUtils;
import com.jagex.math.Trig1;
import jaclib.memory.Stream;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kla")
public final class Model_Sub2 extends Model {

    @OriginalMember(owner = "client!kla", name = "sc", descriptor = "[I")
    public int[] anIntArray409;

    @OriginalMember(owner = "client!kla", name = "Lb", descriptor = "S")
    public short aShort61;

    @OriginalMember(owner = "client!kla", name = "hb", descriptor = "[[I")
    public int[][] anIntArrayArray129;

    @OriginalMember(owner = "client!kla", name = "z", descriptor = "Lclient!sw;")
    public Class346 aClass346_1;

    @OriginalMember(owner = "client!kla", name = "P", descriptor = "[I")
    public int[] anIntArray410;

    @OriginalMember(owner = "client!kla", name = "K", descriptor = "[Lclient!mn;")
    public ModelParticleEffector[] aModelParticleEffectorArray4;

    @OriginalMember(owner = "client!kla", name = "wb", descriptor = "S")
    public short aShort62;

    @OriginalMember(owner = "client!kla", name = "Bb", descriptor = "S")
    public short aShort63;

    @OriginalMember(owner = "client!kla", name = "H", descriptor = "S")
    public short aShort64;

    @OriginalMember(owner = "client!kla", name = "eb", descriptor = "[F")
    public float[] aFloatArray37;

    @OriginalMember(owner = "client!kla", name = "Pb", descriptor = "S")
    public short aShort65;

    @OriginalMember(owner = "client!kla", name = "O", descriptor = "[[I")
    public int[][] anIntArrayArray130;

    @OriginalMember(owner = "client!kla", name = "Cc", descriptor = "[S")
    public short[] aShortArray66;

    @OriginalMember(owner = "client!kla", name = "cc", descriptor = "S")
    public short aShort66;

    @OriginalMember(owner = "client!kla", name = "nb", descriptor = "[S")
    public short[] aShortArray67;

    @OriginalMember(owner = "client!kla", name = "n", descriptor = "[Lclient!efa;")
    public GlBillboardFace[] billboardFaces;

    @OriginalMember(owner = "client!kla", name = "Yb", descriptor = "[S")
    public short[] aShortArray68;

    @OriginalMember(owner = "client!kla", name = "jc", descriptor = "[I")
    public int[] anIntArray411;

    @OriginalMember(owner = "client!kla", name = "pc", descriptor = "[S")
    public short[] aShortArray69;

    @OriginalMember(owner = "client!kla", name = "Zb", descriptor = "[I")
    public int[] anIntArray412;

    @OriginalMember(owner = "client!kla", name = "X", descriptor = "I")
    public int anInt5520;

    @OriginalMember(owner = "client!kla", name = "Fb", descriptor = "[B")
    public byte[] aByteArray53;

    @OriginalMember(owner = "client!kla", name = "Kb", descriptor = "Lclient!ar;")
    public Interface1 anInterface1_3;

    @OriginalMember(owner = "client!kla", name = "B", descriptor = "[F")
    public float[] aFloatArray39;

    @OriginalMember(owner = "client!kla", name = "r", descriptor = "B")
    public byte aByte89;

    @OriginalMember(owner = "client!kla", name = "Db", descriptor = "I")
    public int anInt5529;

    @OriginalMember(owner = "client!kla", name = "S", descriptor = "[B")
    public byte[] aByteArray54;

    @OriginalMember(owner = "client!kla", name = "Bc", descriptor = "I")
    public int billboardCount;

    @OriginalMember(owner = "client!kla", name = "bb", descriptor = "S")
    public short aShort67;

    @OriginalMember(owner = "client!kla", name = "Cb", descriptor = "S")
    public short aShort68;

    @OriginalMember(owner = "client!kla", name = "dc", descriptor = "[Lclient!lfa;")
    public GlBillboardAttributes[] billboardAttributes;

    @OriginalMember(owner = "client!kla", name = "ec", descriptor = "S")
    public short aShort69;

    @OriginalMember(owner = "client!kla", name = "uc", descriptor = "[S")
    public short[] aShortArray70;

    @OriginalMember(owner = "client!kla", name = "Ac", descriptor = "[Lclient!rv;")
    public ModelParticleEmitter[] aModelParticleEmitterArray4;

    @OriginalMember(owner = "client!kla", name = "q", descriptor = "[S")
    public short[] aShortArray71;

    @OriginalMember(owner = "client!kla", name = "W", descriptor = "[[I")
    public int[][] anIntArrayArray131;

    @OriginalMember(owner = "client!kla", name = "ab", descriptor = "[S")
    public short[] aShortArray72;

    @OriginalMember(owner = "client!kla", name = "fb", descriptor = "S")
    public short aShort70;

    @OriginalMember(owner = "client!kla", name = "M", descriptor = "[I")
    public int[] anIntArray413;

    @OriginalMember(owner = "client!kla", name = "E", descriptor = "[S")
    public short[] aShortArray73;

    @OriginalMember(owner = "client!kla", name = "x", descriptor = "[S")
    public short[] aShortArray74;

    @OriginalMember(owner = "client!kla", name = "xb", descriptor = "Lclient!jc;")
    public Interface12 anInterface12_4;

    @OriginalMember(owner = "client!kla", name = "wc", descriptor = "[S")
    public short[] aShortArray75;

    @OriginalMember(owner = "client!kla", name = "mc", descriptor = "[S")
    public short[] aShortArray76;

    @OriginalMember(owner = "client!kla", name = "N", descriptor = "Z")
    public boolean aBoolean422 = true;

    @OriginalMember(owner = "client!kla", name = "ub", descriptor = "Z")
    public boolean aBoolean423 = false;

    @OriginalMember(owner = "client!kla", name = "Q", descriptor = "I")
    public int anInt5523 = 0;

    @OriginalMember(owner = "client!kla", name = "ic", descriptor = "Z")
    public boolean aBoolean421 = false;

    @OriginalMember(owner = "client!kla", name = "jb", descriptor = "I")
    public int anInt5543 = 0;

    @OriginalMember(owner = "client!kla", name = "A", descriptor = "I")
    public int anInt5538 = 0;

    @OriginalMember(owner = "client!kla", name = "Vb", descriptor = "I")
    public int anInt5506 = 0;

    @OriginalMember(owner = "client!kla", name = "J", descriptor = "Z")
    public boolean aBoolean424 = false;

    @OriginalMember(owner = "client!kla", name = "Eb", descriptor = "I")
    public int anInt5560 = 0;

    @OriginalMember(owner = "client!kla", name = "vc", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_24;

    @OriginalMember(owner = "client!kla", name = "xc", descriptor = "Lclient!ed;")
    public Class94 aClass94_8;

    @OriginalMember(owner = "client!kla", name = "ac", descriptor = "Lclient!ed;")
    public Class94 aClass94_9;

    @OriginalMember(owner = "client!kla", name = "gb", descriptor = "Lclient!ed;")
    public Class94 aClass94_7;

    @OriginalMember(owner = "client!kla", name = "qc", descriptor = "Lclient!ed;")
    public Class94 aClass94_6;

    @OriginalMember(owner = "client!kla", name = "ob", descriptor = "Lclient!fs;")
    public Class136 aClass136_1;

    @OriginalMember(owner = "client!kla", name = "<init>", descriptor = "(Lclient!qha;)V")
    public Model_Sub2(@OriginalArg(0) GlToolkit arg0) {
        this.aClass19_Sub3_24 = arg0;
        this.aClass94_8 = new Class94(null, 5126, 3, 0);
        this.aClass94_9 = new Class94(null, 5126, 2, 0);
        this.aClass94_7 = new Class94(null, 5126, 3, 0);
        this.aClass94_6 = new Class94(null, 5121, 4, 0);
        this.aClass136_1 = new Class136();
    }

    @OriginalMember(owner = "client!kla", name = "<init>", descriptor = "(Lclient!qha;Lclient!dv;IIII)V")
    public Model_Sub2(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) Mesh arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.aClass19_Sub3_24 = arg0;
        this.anInt5520 = arg2;
        this.anInt5529 = arg5;
        if (Static277.method4044(arg2, arg5)) {
            this.aClass94_8 = new Class94(null, 5126, 3, 0);
        }
        if (Static240.method3483(arg2, arg5)) {
            this.aClass94_9 = new Class94(null, 5126, 2, 0);
        }
        if (Static279.method4074(arg5, arg2)) {
            this.aClass94_7 = new Class94(null, 5126, 3, 0);
        }
        if (Static116.method2142(arg2, arg5)) {
            this.aClass94_6 = new Class94(null, 5121, 4, 0);
        }
        if (Static340.method5012(arg5, arg2)) {
            this.aClass136_1 = new Class136();
        }
        @Pc(116) TextureSource local116 = arg0.textureSource;
        @Pc(120) int[] local120 = new int[arg1.faceCount];
        this.anIntArray411 = new int[arg1.maxVertex + 1];
        for (@Pc(129) int local129 = 0; local129 < arg1.faceCount; local129++) {
            if (arg1.shadingType == null || arg1.shadingType[local129] != 2) {
                if (arg1.faceTexture != null && arg1.faceTexture[local129] != -1) {
                    @Pc(168) TextureMetrics local168 = local116.getMetrics(arg1.faceTexture[local129] & 0xFFFF);
                    if (((this.anInt5529 & 0x40) == 0 || !local168.disableable) && local168.aBoolean234) {
                        continue;
                    }
                }
                local120[this.anInt5560++] = local129;
                this.anIntArray411[arg1.faceA[local129]]++;
                this.anIntArray411[arg1.faceB[local129]]++;
                this.anIntArray411[arg1.faceC[local129]]++;
            }
        }
        this.anInt5506 = this.anInt5560;
        @Pc(252) long[] local252 = new long[this.anInt5560];
        @Pc(264) boolean local264 = (this.anInt5520 & 0x100) != 0;
        @Pc(278) int local278;
        @Pc(291) int local291;
        @Pc(481) int local481;
        for (@Pc(266) int local266 = 0; local266 < this.anInt5560; local266++) {
            @Pc(274) int local274 = local120[local266];
            @Pc(276) TextureMetrics local276 = null;
            local278 = 0;
            @Pc(280) byte local280 = 0;
            @Pc(282) byte local282 = 0;
            @Pc(284) byte local284 = 0;
            if (arg1.billboards != null) {
                @Pc(289) boolean local289 = false;
                for (local291 = 0; local291 < arg1.billboards.length; local291++) {
                    @Pc(298) MeshBillboard local298 = arg1.billboards[local291];
                    if (local298.face == local274) {
                        @Pc(307) BillboardType local307 = BillboardTypeList.list(local298.id);
                        if (local307.hideFace) {
                            local289 = true;
                        }
                        if (local307.texture != -1) {
                            @Pc(323) TextureMetrics local323 = local116.getMetrics(local307.texture);
                            if (local323.alphaBlendMode == 2) {
                                this.aBoolean421 = true;
                            }
                        }
                    }
                }
                if (local289) {
                    local252[local266] = Long.MAX_VALUE;
                    this.anInt5506--;
                    continue;
                }
            }
            @Pc(358) short local358 = -1;
            if (arg1.faceTexture != null) {
                local358 = arg1.faceTexture[local274];
                if (local358 != -1) {
                    local276 = local116.getMetrics(local358 & 0xFFFF);
                    if ((this.anInt5529 & 0x40) != 0 && local276.disableable) {
                        local358 = -1;
                        local276 = null;
                    } else {
                        local282 = local276.effectType;
                        local284 = local276.effectParam1;
                        if (local276.speedU != 0 || local276.speedV != 0) {
                            this.aBoolean423 = true;
                        }
                    }
                }
            }
            @Pc(439) boolean local439 = arg1.faceAlpha != null && arg1.faceAlpha[local274] != 0 || local276 != null && local276.alphaBlendMode != 0;
            if ((local264 || local439) && arg1.facePriority != null) {
                local278 += arg1.facePriority[local274] << 17;
            }
            if (local439) {
                local278 += 65536;
            }
            local278 += (local282 & 0xFF) << 8;
            local481 = local280 + ((local358 & 0xFFFF) << 16);
            local278 += local284 & 0xFF;
            @Pc(493) int local493 = local481 + (local266 & 0xFFFF);
            local252[local266] = ((long) local278 << 32) + ((long) local493);
            this.aBoolean421 |= local439;
            this.aBoolean423 |= local276 != null && (local276.speedU != 0 || local276.speedV != 0);
        }
        Quicksort.sort(local252, local120);
        this.anIntArray412 = arg1.vertexZ;
        this.anInt5523 = arg1.vertexCount;
        this.aShortArray66 = arg1.originModels;
        this.anInt5543 = arg1.maxVertex;
        this.anIntArray409 = arg1.vertexY;
        this.anIntArray410 = arg1.vertexX;
        @Pc(575) Class185[] local575 = new Class185[this.anInt5543];
        this.aModelParticleEmitterArray4 = arg1.emitters;
        this.aModelParticleEffectorArray4 = arg1.effectors;
        @Pc(603) int local603;
        @Pc(619) int local619;
        @Pc(674) int local674;
        if (arg1.billboards != null) {
            this.billboardCount = arg1.billboards.length;
            this.billboardFaces = new GlBillboardFace[this.billboardCount];
            this.billboardAttributes = new GlBillboardAttributes[this.billboardCount];
            for (local603 = 0; local603 < this.billboardCount; local603++) {
                @Pc(612) MeshBillboard local612 = arg1.billboards[local603];
                @Pc(617) BillboardType local617 = BillboardTypeList.list(local612.id);
                local619 = -1;
                for (@Pc(621) int local621 = 0; local621 < this.anInt5560; local621++) {
                    if (local612.face == local120[local621]) {
                        local619 = local621;
                        break;
                    }
                }
                if (local619 == -1) {
                    throw new RuntimeException();
                }
                local674 = ColourUtils.HSL_TO_RGB[arg1.faceColour[local612.face] & 0xFFFF] & 0xFFFFFF;
                @Pc(692) int local692 = local674 | 255 - (arg1.faceAlpha == null ? 0 : arg1.faceAlpha[local612.face]) << 24;
                this.billboardFaces[local603] = new GlBillboardFace(local619, arg1.faceA[local612.face], arg1.faceB[local612.face], arg1.faceC[local612.face], local617.width, local617.height, local617.texture, local617.anInt9697, local617.blendMode, local617.hideFace, local617.aBoolean748, local612.distance);
                this.billboardAttributes[local603] = new GlBillboardAttributes(local692);
            }
        }
        local603 = this.anInt5560 * 3;
        this.aShortArray70 = new short[local603];
        this.aShort64 = (short) arg3;
        if (arg1.aShortArray20 != null) {
            this.aShortArray68 = new short[this.anInt5560];
        }
        this.aShortArray69 = new short[local603];
        this.aShortArray74 = new short[this.anInt5560];
        this.aByteArray54 = new byte[this.anInt5560];
        Static350.aLongArray9 = new long[local603];
        this.aShortArray76 = new short[this.anInt5560];
        this.aFloatArray37 = new float[local603];
        this.aShortArray75 = new short[this.anInt5560];
        this.aShortArray73 = new short[local603];
        this.aShort67 = (short) arg4;
        this.aShortArray72 = new short[local603];
        this.aShortArray71 = new short[this.anInt5560];
        this.aShortArray67 = new short[this.anInt5560];
        this.aFloatArray39 = new float[local603];
        this.aByteArray53 = new byte[local603];
        local278 = 0;
        for (local481 = 0; local481 < arg1.maxVertex; local481++) {
            local619 = this.anIntArray411[local481];
            this.anIntArray411[local481] = local278;
            local278 += local619;
            local575[local481] = new Class185();
        }
        this.anIntArray411[arg1.maxVertex] = local278;
        @Pc(882) TextureUniverse local882 = TextureUniverse.fromMesh(arg1, this.anInt5560, local120);
        @Pc(886) Class166[] local886 = new Class166[arg1.faceCount];
        @Pc(928) int local928;
        @Pc(939) int local939;
        @Pc(961) int local961;
        @Pc(971) int local971;
        @Pc(980) int local980;
        @Pc(989) int local989;
        @Pc(907) short local907;
        @Pc(918) int local918;
        for (local674 = 0; local674 < arg1.faceCount; local674++) {
            @Pc(897) short local897 = arg1.faceA[local674];
            @Pc(902) short local902 = arg1.faceB[local674];
            local907 = arg1.faceC[local674];
            local918 = this.anIntArray410[local902] - this.anIntArray410[local897];
            local928 = this.anIntArray409[local902] - this.anIntArray409[local897];
            local939 = this.anIntArray412[local902] - this.anIntArray412[local897];
            @Pc(950) int local950 = this.anIntArray410[local907] - this.anIntArray410[local897];
            local961 = this.anIntArray409[local907] - this.anIntArray409[local897];
            local971 = this.anIntArray412[local907] - this.anIntArray412[local897];
            local980 = local971 * local928 - local939 * local961;
            local989 = local950 * local939 - local971 * local918;
            @Pc(998) int local998;
            for (local998 = local918 * local961 - local928 * local950; local980 > 8192 || local989 > 8192 || local998 > 8192 || local980 < -8192 || local989 < -8192 || local998 < -8192; local998 >>= 0x1) {
                local989 >>= 0x1;
                local980 >>= 0x1;
            }
            @Pc(1056) int local1056 = (int) Math.sqrt(local989 * local989 + local980 * local980 + local998 * local998);
            if (local1056 <= 0) {
                local1056 = 1;
            }
            local989 = local989 * 256 / local1056;
            local998 = local998 * 256 / local1056;
            local980 = local980 * 256 / local1056;
            @Pc(1089) byte local1089 = arg1.shadingType == null ? 0 : arg1.shadingType[local674];
            if (local1089 == 0) {
                @Pc(1095) Class185 local1095 = local575[local897];
                local1095.anInt4581 += local980;
                local1095.anInt4580 += local989;
                local1095.anInt4578++;
                local1095.anInt4579 += local998;
                @Pc(1123) Class185 local1123 = local575[local902];
                local1123.anInt4580 += local989;
                local1123.anInt4578++;
                local1123.anInt4581 += local980;
                local1123.anInt4579 += local998;
                @Pc(1151) Class185 local1151 = local575[local907];
                local1151.anInt4581 += local980;
                local1151.anInt4578++;
                local1151.anInt4580 += local989;
                local1151.anInt4579 += local998;
            } else if (local1089 == 1) {
                @Pc(1192) Class166 local1192 = local886[local674] = new Class166();
                local1192.anInt3967 = local980;
                local1192.anInt3963 = local989;
                local1192.anInt3970 = local998;
            }
        }
        @Pc(1216) int local1216;
        @Pc(1260) short local1260;
        for (local291 = 0; local291 < this.anInt5560; local291++) {
            local1216 = local120[local291];
            @Pc(1223) int local1223 = arg1.faceColour[local1216] & 0xFFFF;
            if (arg1.faceTexSpace == null) {
                local918 = -1;
            } else {
                local918 = arg1.faceTexSpace[local1216];
            }
            if (arg1.faceAlpha == null) {
                local928 = 0;
            } else {
                local928 = arg1.faceAlpha[local1216] & 0xFF;
            }
            local1260 = arg1.faceTexture == null ? -1 : arg1.faceTexture[local1216];
            if (local1260 != -1 && (this.anInt5529 & 0x40) != 0) {
                @Pc(1278) TextureMetrics local1278 = local116.getMetrics(local1260 & 0xFFFF);
                if (local1278.disableable) {
                    local1260 = -1;
                }
            }
            @Pc(1285) float local1285 = 0.0F;
            @Pc(1287) float local1287 = 0.0F;
            @Pc(1289) float local1289 = 0.0F;
            @Pc(1291) float local1291 = 0.0F;
            @Pc(1293) float local1293 = 0.0F;
            @Pc(1295) float local1295 = 0.0F;
            @Pc(1297) byte local1297 = 0;
            @Pc(1299) byte local1299 = 0;
            @Pc(1301) int local1301 = 0;
            @Pc(1338) byte local1338;
            @Pc(1355) short local1355;
            @Pc(2170) short local2170;
            @Pc(2175) short local2175;
            if (local1260 != -1) {
                if (local918 == -1) {
                    local1289 = 1.0F;
                    local1297 = 1;
                    local1295 = 0.0F;
                    local1293 = 0.0F;
                    local1285 = 0.0F;
                    local1291 = 1.0F;
                    local1299 = 2;
                    local1287 = 1.0F;
                } else {
                    local918 &= 0xFF;
                    local1338 = arg1.texMappingType[local918];
                    @Pc(1345) short local1345;
                    @Pc(1350) short local1350;
                    @Pc(1388) float local1388;
                    @Pc(1626) float local1626;
                    @Pc(1634) float local1634;
                    @Pc(1733) float local1733;
                    @Pc(1741) float local1741;
                    @Pc(1749) float local1749;
                    @Pc(1772) float local1772;
                    @Pc(1795) float local1795;
                    @Pc(1818) float local1818;
                    if (local1338 == 0) {
                        local1345 = arg1.faceA[local1216];
                        local1350 = arg1.faceB[local1216];
                        local1355 = arg1.faceC[local1216];
                        local2170 = arg1.texSpaceDefA[local918];
                        local2175 = arg1.texSpaceDefB[local918];
                        @Pc(2180) short local2180 = arg1.texSpaceDefC[local918];
                        @Pc(2186) float local2186 = (float) arg1.vertexX[local2170];
                        @Pc(2192) float local2192 = (float) arg1.vertexY[local2170];
                        local1388 = (float) arg1.vertexZ[local2170];
                        local1626 = (float) arg1.vertexX[local2175] - local2186;
                        local1634 = (float) arg1.vertexY[local2175] - local2192;
                        @Pc(2224) float local2224 = (float) arg1.vertexZ[local2175] - local1388;
                        @Pc(2233) float local2233 = (float) arg1.vertexX[local2180] - local2186;
                        @Pc(2242) float local2242 = (float) arg1.vertexY[local2180] - local2192;
                        @Pc(2251) float local2251 = (float) arg1.vertexZ[local2180] - local1388;
                        @Pc(2260) float local2260 = (float) arg1.vertexX[local1345] - local2186;
                        @Pc(2269) float local2269 = (float) arg1.vertexY[local1345] - local2192;
                        @Pc(2277) float local2277 = (float) arg1.vertexZ[local1345] - local1388;
                        @Pc(2286) float local2286 = (float) arg1.vertexX[local1350] - local2186;
                        @Pc(2294) float local2294 = (float) arg1.vertexY[local1350] - local2192;
                        local1733 = (float) arg1.vertexZ[local1350] - local1388;
                        local1741 = (float) arg1.vertexX[local1355] - local2186;
                        local1749 = (float) arg1.vertexY[local1355] - local2192;
                        local1772 = (float) arg1.vertexZ[local1355] - local1388;
                        local1795 = local2251 * local1634 - local2224 * local2242;
                        local1818 = local2233 * local2224 - local2251 * local1626;
                        @Pc(2355) float local2355 = local1626 * local2242 - local1634 * local2233;
                        @Pc(2364) float local2364 = local2242 * local2355 - local2251 * local1818;
                        @Pc(2373) float local2373 = local2251 * local1795 - local2355 * local2233;
                        @Pc(2381) float local2381 = local1818 * local2233 - local2242 * local1795;
                        @Pc(2395) float local2395 = 1.0F / (local1634 * local2373 + local2364 * local1626 + local2381 * local2224);
                        local1293 = (local1749 * local2373 + local1741 * local2364 + local2381 * local1772) * local2395;
                        local1285 = local2395 * (local2277 * local2381 + local2373 * local2269 + local2364 * local2260);
                        local1289 = local2395 * (local1733 * local2381 + local2294 * local2373 + local2286 * local2364);
                        @Pc(2446) float local2446 = local2355 * local1634 - local1818 * local2224;
                        @Pc(2454) float local2454 = local1795 * local2224 - local1626 * local2355;
                        @Pc(2462) float local2462 = local1818 * local1626 - local1795 * local1634;
                        @Pc(2476) float local2476 = 1.0F / (local2251 * local2462 + local2233 * local2446 + local2454 * local2242);
                        local1295 = (local1772 * local2462 + local2454 * local1749 + local1741 * local2446) * local2476;
                        local1291 = local2476 * (local2446 * local2286 + local2294 * local2454 + local2462 * local1733);
                        local1287 = (local2446 * local2260 + local2269 * local2454 + local2462 * local2277) * local2476;
                    } else {
                        local1345 = arg1.faceA[local1216];
                        local1350 = arg1.faceB[local1216];
                        local1355 = arg1.faceC[local1216];
                        @Pc(1360) int local1360 = local882.originX[local918];
                        @Pc(1365) int local1365 = local882.originY[local918];
                        @Pc(1370) int local1370 = local882.originZ[local918];
                        @Pc(1375) float[] local1375 = local882.matrices[local918];
                        @Pc(1380) byte local1380 = arg1.texDirection[local918];
                        local1388 = (float) arg1.texOffsetX[local918] / 256.0F;
                        if (local1338 == 1) {
                            local1626 = (float) arg1.texSpaceScaleZ[local918] / 1024.0F;
                            TextureMapping.cylinderMap(arg1.vertexX[local1345], arg1.vertexY[local1345], arg1.vertexZ[local1345], local1360, local1365, local1370, local1375, local1626, local1388, local1380, Static414.aFloatArray43);
                            local1285 = Static414.aFloatArray43[0];
                            local1287 = Static414.aFloatArray43[1];
                            TextureMapping.cylinderMap(arg1.vertexX[local1350], arg1.vertexY[local1350], arg1.vertexZ[local1350], local1360, local1365, local1370, local1375, local1626, local1388, local1380, Static414.aFloatArray43);
                            local1289 = Static414.aFloatArray43[0];
                            local1291 = Static414.aFloatArray43[1];
                            TextureMapping.cylinderMap(arg1.vertexX[local1355], arg1.vertexY[local1355], arg1.vertexZ[local1355], local1360, local1365, local1370, local1375, local1626, local1388, local1380, Static414.aFloatArray43);
                            local1293 = Static414.aFloatArray43[0];
                            local1295 = Static414.aFloatArray43[1];
                            local1634 = local1626 / 2.0F;
                            if ((local1380 & 0x1) == 0) {
                                if (local1289 - local1285 > local1634) {
                                    local1297 = 1;
                                    local1289 -= local1626;
                                } else if (local1285 - local1289 > local1634) {
                                    local1297 = 2;
                                    local1289 += local1626;
                                }
                                if (local1293 - local1285 > local1634) {
                                    local1293 -= local1626;
                                    local1299 = 1;
                                } else if (local1634 < local1285 - local1293) {
                                    local1299 = 2;
                                    local1293 += local1626;
                                }
                            } else {
                                if (local1634 < local1295 - local1287) {
                                    local1299 = 1;
                                    local1295 -= local1626;
                                } else if (local1634 < local1287 - local1295) {
                                    local1299 = 2;
                                    local1295 += local1626;
                                }
                                if (local1291 - local1287 > local1634) {
                                    local1297 = 1;
                                    local1291 -= local1626;
                                } else if (local1287 - local1291 > local1634) {
                                    local1297 = 2;
                                    local1291 += local1626;
                                }
                            }
                        } else if (local1338 == 2) {
                            local1626 = (float) arg1.texOffsetY[local918] / 256.0F;
                            local1634 = (float) arg1.texOffsetZ[local918] / 256.0F;
                            @Pc(1645) int local1645 = arg1.vertexX[local1350] - arg1.vertexX[local1345];
                            @Pc(1655) int local1655 = arg1.vertexY[local1350] - arg1.vertexY[local1345];
                            @Pc(1665) int local1665 = arg1.vertexZ[local1350] - arg1.vertexZ[local1345];
                            @Pc(1676) int local1676 = arg1.vertexX[local1355] - arg1.vertexX[local1345];
                            @Pc(1687) int local1687 = arg1.vertexY[local1355] - arg1.vertexY[local1345];
                            @Pc(1698) int local1698 = arg1.vertexZ[local1355] - arg1.vertexZ[local1345];
                            @Pc(1707) int local1707 = local1655 * local1698 - local1665 * local1687;
                            @Pc(1716) int local1716 = local1676 * local1665 - local1645 * local1698;
                            @Pc(1725) int local1725 = local1645 * local1687 - local1655 * local1676;
                            local1733 = 64.0F / (float) arg1.texSpaceScaleX[local918];
                            local1741 = 64.0F / (float) arg1.texSpaceScaleY[local918];
                            local1749 = 64.0F / (float) arg1.texSpaceScaleZ[local918];
                            local1772 = ((float) local1707 * local1375[0] + local1375[1] * (float) local1716 + local1375[2] * (float) local1725) / local1733;
                            local1795 = (local1375[5] * (float) local1725 + local1375[3] * (float) local1707 + (float) local1716 * local1375[4]) / local1741;
                            local1818 = ((float) local1725 * local1375[8] + local1375[7] * (float) local1716 + (float) local1707 * local1375[6]) / local1749;
                            local1301 = TextureMapping.cubeFace(local1772, local1795, local1818);
                            TextureMapping.cubeMap(arg1.vertexX[local1345], arg1.vertexY[local1345], arg1.vertexZ[local1345], local1360, local1365, local1370, local1375, local1388, local1626, local1634, local1301, local1380, Static414.aFloatArray43);
                            local1287 = Static414.aFloatArray43[1];
                            local1285 = Static414.aFloatArray43[0];
                            TextureMapping.cubeMap(arg1.vertexX[local1350], arg1.vertexY[local1350], arg1.vertexZ[local1350], local1360, local1365, local1370, local1375, local1388, local1626, local1634, local1301, local1380, Static414.aFloatArray43);
                            local1289 = Static414.aFloatArray43[0];
                            local1291 = Static414.aFloatArray43[1];
                            TextureMapping.cubeMap(arg1.vertexX[local1355], arg1.vertexY[local1355], arg1.vertexZ[local1355], local1360, local1365, local1370, local1375, local1388, local1626, local1634, local1301, local1380, Static414.aFloatArray43);
                            local1293 = Static414.aFloatArray43[0];
                            local1295 = Static414.aFloatArray43[1];
                        } else if (local1338 == 3) {
                            TextureMapping.sphereMap(arg1.vertexX[local1345], arg1.vertexY[local1345], arg1.vertexZ[local1345], local1360, local1365, local1370, local1375, local1388, local1380, Static414.aFloatArray43);
                            local1287 = Static414.aFloatArray43[1];
                            local1285 = Static414.aFloatArray43[0];
                            TextureMapping.sphereMap(arg1.vertexX[local1350], arg1.vertexY[local1350], arg1.vertexZ[local1350], local1360, local1365, local1370, local1375, local1388, local1380, Static414.aFloatArray43);
                            local1291 = Static414.aFloatArray43[1];
                            local1289 = Static414.aFloatArray43[0];
                            TextureMapping.sphereMap(arg1.vertexX[local1355], arg1.vertexY[local1355], arg1.vertexZ[local1355], local1360, local1365, local1370, local1375, local1388, local1380, Static414.aFloatArray43);
                            local1295 = Static414.aFloatArray43[1];
                            local1293 = Static414.aFloatArray43[0];
                            if ((local1380 & 0x1) == 0) {
                                if (local1293 - local1285 > 0.5F) {
                                    local1299 = 1;
                                    local1293--;
                                } else if (local1285 - local1293 > 0.5F) {
                                    local1299 = 2;
                                    local1293++;
                                }
                                if (local1289 - local1285 > 0.5F) {
                                    local1297 = 1;
                                    local1289--;
                                } else if (local1285 - local1289 > 0.5F) {
                                    local1289++;
                                    local1297 = 2;
                                }
                            } else {
                                if (local1291 - local1287 > 0.5F) {
                                    local1291--;
                                    local1297 = 1;
                                } else if (local1287 - local1291 > 0.5F) {
                                    local1291++;
                                    local1297 = 2;
                                }
                                if (local1295 - local1287 > 0.5F) {
                                    local1295--;
                                    local1299 = 1;
                                } else if (local1287 - local1295 > 0.5F) {
                                    local1295++;
                                    local1299 = 2;
                                }
                            }
                        }
                    }
                }
            }
            if (arg1.shadingType == null) {
                local1338 = 0;
            } else {
                local1338 = arg1.shadingType[local1216];
            }
            if (local1338 == 0) {
                @Pc(2685) long local2685 = ((long) local928 + (long) (local1223 << 8) + (long) (local1301 << 24) << 32) + (long) (local918 << 2);
                local1355 = arg1.faceA[local1216];
                local2170 = arg1.faceB[local1216];
                local2175 = arg1.faceC[local1216];
                @Pc(2704) Class185 local2704 = local575[local1355];
                this.aShortArray67[local291] = this.method4993(local2685, local2704.anInt4581, local1285, local1287, local2704.anInt4580, local2704.anInt4578, arg1, local2704.anInt4579, local1355);
                @Pc(2728) Class185 local2728 = local575[local2170];
                this.aShortArray71[local291] = this.method4993(local2685 + (long) local1297, local2728.anInt4581, local1289, local1291, local2728.anInt4580, local2728.anInt4578, arg1, local2728.anInt4579, local2170);
                @Pc(2755) Class185 local2755 = local575[local2175];
                this.aShortArray76[local291] = this.method4993((long) local1299 + local2685, local2755.anInt4581, local1293, local1295, local2755.anInt4580, local2755.anInt4578, arg1, local2755.anInt4579, local2175);
            } else if (local1338 == 1) {
                @Pc(2544) Class166 local2544 = local886[local1216];
                @Pc(2591) long local2591 = ((long) (local1223 << 8) + (long) (local1301 << 24) + (long) local928 << 32) + (long) ((local2544.anInt3963 + 256 << 12) + ((local918 << 2) - (-(local2544.anInt3967 <= 0 ? 2048 : 1024) - (local2544.anInt3970 + 256 << 22))));
                this.aShortArray67[local291] = this.method4993(local2591, local2544.anInt3967, local1285, local1287, local2544.anInt3963, 0, arg1, local2544.anInt3970, arg1.faceA[local1216]);
                this.aShortArray71[local291] = this.method4993((long) local1297 + local2591, local2544.anInt3967, local1289, local1291, local2544.anInt3963, 0, arg1, local2544.anInt3970, arg1.faceB[local1216]);
                this.aShortArray76[local291] = this.method4993(local2591 + (long) local1299, local2544.anInt3967, local1293, local1295, local2544.anInt3963, 0, arg1, local2544.anInt3970, arg1.faceC[local1216]);
            }
            if (arg1.faceAlpha != null) {
                this.aByteArray54[local291] = arg1.faceAlpha[local1216];
            }
            if (arg1.aShortArray20 != null) {
                this.aShortArray68[local291] = arg1.aShortArray20[local1216];
            }
            this.aShortArray74[local291] = arg1.faceColour[local1216];
            this.aShortArray75[local291] = local1260;
        }
        local1216 = 0;
        local907 = -10000;
        for (local918 = 0; local918 < this.anInt5506; local918++) {
            @Pc(2837) short local2837 = this.aShortArray75[local918];
            if (local907 != local2837) {
                local1216++;
                local907 = local2837;
            }
        }
        this.anIntArray413 = new int[local1216 + 1];
        local907 = -10000;
        local1216 = 0;
        for (local928 = 0; local928 < this.anInt5506; local928++) {
            local1260 = this.aShortArray75[local928];
            if (local907 != local1260) {
                local907 = local1260;
                this.anIntArray413[local1216++] = local928;
            }
        }
        this.anIntArray413[local1216] = this.anInt5506;
        Static350.aLongArray9 = null;
        this.aShortArray69 = Static358.method9173(this.anInt5538, this.aShortArray69);
        this.aShortArray73 = Static358.method9173(this.anInt5538, this.aShortArray73);
        this.aShortArray72 = Static358.method9173(this.anInt5538, this.aShortArray72);
        this.aByteArray53 = Static597.method7823(this.aByteArray53, this.anInt5538);
        this.aFloatArray37 = TextureOp.method9420(false, this.aFloatArray37, this.anInt5538);
        this.aFloatArray39 = TextureOp.method9420(false, this.aFloatArray39, this.anInt5538);
        if (arg1.vertexLabel != null && Static685.method8950(-60, arg2, this.anInt5529)) {
            this.anIntArrayArray131 = arg1.getVertexLabels(false);
        }
        if (arg1.billboards != null && Static90.method1732(this.anInt5529, arg2)) {
            this.anIntArrayArray130 = arg1.getBillboardGroups();
        }
        if (arg1.faceLabel != null && Static596.method7814(arg2, this.anInt5529)) {
            local939 = 0;
            @Pc(3012) int[] local3012 = new int[256];
            for (local961 = 0; local961 < this.anInt5560; local961++) {
                local971 = arg1.faceLabel[local120[local961]];
                if (local971 >= 0) {
                    if (local971 > local939) {
                        local939 = local971;
                    }
                    @Pc(3040) int local3040 = local3012[local971]++;
                }
            }
            this.anIntArrayArray129 = new int[local939 + 1][];
            for (local971 = 0; local971 <= local939; local971++) {
                this.anIntArrayArray129[local971] = new int[local3012[local971]];
                local3012[local971] = 0;
            }
            for (local980 = 0; local980 < this.anInt5560; local980++) {
                local989 = arg1.faceLabel[local120[local980]];
                if (local989 >= 0) {
                    this.anIntArrayArray129[local989][local3012[local989]++] = local980;
                }
            }
        }
    }

    @OriginalMember(owner = "client!ml", name = "a", descriptor = "(III)Z")
    public static boolean method5577(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x70000) != 0 | Static58.method1257(arg0, arg1) || Static598.method7828(arg1, arg0);
    }

    @OriginalMember(owner = "client!kla", name = "aa", descriptor = "(SS)V")
    @Override
    public void aa(@OriginalArg(0) short src, @OriginalArg(1) short dest) {
        @Pc(9) TextureSource local9 = this.aClass19_Sub3_24.textureSource;
        for (@Pc(11) int local11 = 0; local11 < this.anInt5560; local11++) {
            if (src == this.aShortArray75[local11]) {
                this.aShortArray75[local11] = dest;
            }
        }
        @Pc(39) byte local39 = 0;
        @Pc(41) byte local41 = 0;
        if (src != -1) {
            @Pc(53) TextureMetrics local53 = local9.getMetrics(src & 0xFFFF);
            local39 = local53.alpha;
            local41 = local53.aByte57;
        }
        @Pc(61) byte local61 = 0;
        @Pc(63) byte local63 = 0;
        if (dest != -1) {
            @Pc(73) TextureMetrics local73 = local9.getMetrics(dest & 0xFFFF);
            local61 = local73.alpha;
            local63 = local73.aByte57;
            if (local73.speedU != 0 || local73.speedV != 0) {
                this.aBoolean423 = true;
            }
        }
        if (!(local61 != local39 | local41 != local63)) {
            return;
        }
        if (this.billboardFaces != null) {
            for (@Pc(118) int local118 = 0; local118 < this.billboardCount; local118++) {
                @Pc(125) GlBillboardFace local125 = this.billboardFaces[local118];
                @Pc(130) GlBillboardAttributes local130 = this.billboardAttributes[local118];
                local130.colour = local130.colour & 0xFF000000 | ColourUtils.HSL_TO_RGB[this.aShortArray74[local125.face] & 0xFFFF] & 0xFFFFFF;
            }
        }
        if (this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
            return;
        }
    }

    @OriginalMember(owner = "client!kla", name = "HA", descriptor = "()I")
    @Override
    public int HA() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort61;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(IILclient!tt;ZI)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift) {
        return this.method4981(matrix, quick, y, x, -1, sizeShift);
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!tt;ZIIIII)Z")
    public boolean method4981(@OriginalArg(0) Matrix arg0, @OriginalArg(1) boolean arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        @Pc(8) Matrix_Sub3 local8 = (Matrix_Sub3) arg0;
        @Pc(12) Matrix_Sub3 local12 = this.aClass19_Sub3_24.aClass73_Sub3_3;
        @Pc(33) float local33 = local12.aFloat152 + local8.aFloat158 * local12.aFloat157 + local8.aFloat152 * local12.aFloat153 + local12.aFloat160 * local8.aFloat159;
        @Pc(54) float local54 = local12.aFloat158 + local12.aFloat156 * local8.aFloat158 + local8.aFloat152 * local12.aFloat161 + local8.aFloat159 * local12.aFloat150;
        Static188.aFloat65 = local8.aFloat156 * local12.aFloat151 + local8.aFloat157 * local12.aFloat155 + local8.aFloat151 * local12.aFloat154;
        Static430.aFloat120 = local12.aFloat161 * local8.aFloat153 + local12.aFloat156 * local8.aFloat161 + local12.aFloat150 * local8.aFloat155;
        Static238.aFloat73 = local8.aFloat151 * local12.aFloat160 + local12.aFloat157 * local8.aFloat156 + local12.aFloat153 * local8.aFloat157;
        Static393.aFloat117 = local8.aFloat161 * local12.aFloat157 + local8.aFloat153 * local12.aFloat153 + local8.aFloat155 * local12.aFloat160;
        Static190.aFloat66 = local12.aFloat151 * local8.aFloat150 + local8.aFloat160 * local12.aFloat155 + local8.aFloat154 * local12.aFloat154;
        Static409.aFloat118 = local8.aFloat150 * local12.aFloat156 + local8.aFloat160 * local12.aFloat161 + local8.aFloat154 * local12.aFloat150;
        Static376.aFloat113 = local12.aFloat160 * local8.aFloat154 + local12.aFloat157 * local8.aFloat150 + local8.aFloat160 * local12.aFloat153;
        Static91.aFloat208 = local12.aFloat161 * local8.aFloat157 + local12.aFloat156 * local8.aFloat156 + local8.aFloat151 * local12.aFloat150;
        @Pc(219) float local219 = local12.aFloat159 + local12.aFloat155 * local8.aFloat152 + local8.aFloat158 * local12.aFloat151 + local8.aFloat159 * local12.aFloat154;
        Static289.aFloat84 = local8.aFloat161 * local12.aFloat151 + local12.aFloat155 * local8.aFloat153 + local8.aFloat155 * local12.aFloat154;
        @Pc(239) boolean local239 = false;
        @Pc(241) float local241 = Float.MAX_VALUE;
        @Pc(243) float local243 = -3.4028235E38F;
        @Pc(245) float local245 = Float.MAX_VALUE;
        @Pc(247) float local247 = -3.4028235E38F;
        @Pc(251) int local251 = this.aClass19_Sub3_24.anInt8001;
        @Pc(255) int local255 = this.aClass19_Sub3_24.anInt8025;
        if (!this.aBoolean424) {
            this.method4982();
        }
        @Pc(271) int local271 = this.aShort62 - this.aShort66 >> 1;
        @Pc(280) int local280 = this.aShort68 - this.aShort63 >> 1;
        @Pc(288) int local288 = this.aShort69 - this.aShort61 >> 1;
        @Pc(301) int local301 = this.aShort66 + local271;
        @Pc(306) int local306 = this.aShort63 + local280;
        @Pc(312) int local312 = this.aShort61 + local288;
        @Pc(319) int local319 = local301 - (local271 << arg5);
        @Pc(326) int local326 = local306 - (local280 << arg5);
        @Pc(333) int local333 = local312 - (local288 << arg5);
        @Pc(339) int local339 = (local271 << arg5) + local301;
        @Pc(345) int local345 = (local280 << arg5) + local306;
        @Pc(351) int local351 = (local288 << arg5) + local312;
        Static421.anIntArray501[0] = local319;
        Static351.anIntArray437[0] = local326;
        Static421.anIntArray501[1] = local339;
        Static648.anIntArray754[0] = local333;
        Static351.anIntArray437[1] = local326;
        Static648.anIntArray754[1] = local333;
        Static421.anIntArray501[2] = local319;
        Static351.anIntArray437[2] = local345;
        Static421.anIntArray501[3] = local339;
        Static648.anIntArray754[2] = local333;
        Static351.anIntArray437[3] = local345;
        Static421.anIntArray501[4] = local319;
        Static648.anIntArray754[3] = local333;
        Static351.anIntArray437[4] = local326;
        Static421.anIntArray501[5] = local339;
        Static648.anIntArray754[4] = local351;
        Static351.anIntArray437[5] = local326;
        Static421.anIntArray501[6] = local319;
        Static648.anIntArray754[5] = local351;
        Static351.anIntArray437[6] = local345;
        Static421.anIntArray501[7] = local339;
        Static648.anIntArray754[6] = local351;
        Static351.anIntArray437[7] = local345;
        Static648.anIntArray754[7] = local351;
        @Pc(482) float local482;
        @Pc(510) float local510;
        @Pc(496) float local496;
        @Pc(458) float local458;
        @Pc(463) float local463;
        @Pc(468) float local468;
        for (@Pc(449) int local449 = 0; local449 < 8; local449++) {
            local458 = (float) Static421.anIntArray501[local449];
            local463 = (float) Static351.anIntArray437[local449];
            local468 = (float) Static648.anIntArray754[local449];
            local482 = local33 + local458 * Static393.aFloat117 + Static238.aFloat73 * local463 + local468 * Static376.aFloat113;
            local496 = Static188.aFloat65 * local463 + local458 * Static289.aFloat84 + local468 * Static190.aFloat66 + local219;
            local510 = local54 + local468 * Static409.aFloat118 + Static430.aFloat120 * local458 + Static91.aFloat208 * local463;
            if (local496 >= (float) this.aClass19_Sub3_24.anInt8010) {
                if (arg4 > 0) {
                    local496 = (float) arg4;
                }
                @Pc(536) float local536 = (float) this.aClass19_Sub3_24.anInt8021 + (float) local251 * local482 / local496;
                @Pc(548) float local548 = (float) local255 * local510 / local496 + (float) this.aClass19_Sub3_24.anInt8016;
                if (local241 > local536) {
                    local241 = local536;
                }
                if (local243 < local536) {
                    local243 = local536;
                }
                local239 = true;
                if (local548 > local247) {
                    local247 = local548;
                }
                if (local548 < local245) {
                    local245 = local548;
                }
            }
        }
        if (local239 && (float) arg3 > local241 && (float) arg3 < local243 && local245 < (float) arg2 && (float) arg2 < local247) {
            if (arg1) {
                return true;
            }
            if (this.aClass19_Sub3_24.anIntArray623.length < this.anInt5538) {
                this.aClass19_Sub3_24.anIntArray625 = new int[this.anInt5538];
                this.aClass19_Sub3_24.anIntArray623 = new int[this.anInt5538];
            }
            @Pc(648) int[] local648 = this.aClass19_Sub3_24.anIntArray623;
            @Pc(652) int[] local652 = this.aClass19_Sub3_24.anIntArray625;
            @Pc(745) int local745;
            for (@Pc(654) int local654 = 0; local654 < this.anInt5543; local654++) {
                local458 = (float) this.anIntArray410[local654];
                local468 = (float) this.anIntArray412[local654];
                local463 = (float) this.anIntArray409[local654];
                local496 = local219 + Static190.aFloat66 * local468 + local463 * Static188.aFloat65 + local458 * Static289.aFloat84;
                local482 = local463 * Static238.aFloat73 + Static393.aFloat117 * local458 + local468 * Static376.aFloat113 + local33;
                local510 = Static430.aFloat120 * local458 + Static91.aFloat208 * local463 + local468 * Static409.aFloat118 + local54;
                @Pc(758) int local758;
                @Pc(763) int local763;
                @Pc(770) int local770;
                if (local496 >= (float) this.aClass19_Sub3_24.anInt8010) {
                    if (arg4 > 0) {
                        local496 = (float) arg4;
                    }
                    local745 = (int) ((float) local251 * local482 / local496 + (float) this.aClass19_Sub3_24.anInt8021);
                    local758 = (int) ((float) this.aClass19_Sub3_24.anInt8016 + (float) local255 * local510 / local496);
                    local763 = this.anIntArray411[local654];
                    local770 = this.anIntArray411[local654 + 1];
                    for (@Pc(772) int local772 = local763; local772 < local770; local772++) {
                        @Pc(781) int local781 = this.aShortArray70[local772] - 1;
                        if (local781 == -1) {
                            break;
                        }
                        local648[local781] = local745;
                        local652[local781] = local758;
                    }
                } else {
                    local745 = this.anIntArray411[local654];
                    local758 = this.anIntArray411[local654 + 1];
                    for (local763 = local745; local763 < local758; local763++) {
                        local770 = this.aShortArray70[local763] - 1;
                        if (local770 == -1) {
                            break;
                        }
                        local648[this.aShortArray70[local763] - 1] = -999999;
                    }
                }
            }
            for (local745 = 0; local745 < this.anInt5560; local745++) {
                if (local648[this.aShortArray67[local745]] != -999999 && local648[this.aShortArray71[local745]] != -999999 && local648[this.aShortArray76[local745]] != -999999 && this.method4989(local648[this.aShortArray71[local745]], local648[this.aShortArray67[local745]], local652[this.aShortArray76[local745]], arg3, local652[this.aShortArray71[local745]], local648[this.aShortArray76[local745]], local652[this.aShortArray67[local745]], arg2)) {
                    return true;
                }
            }
        }
        return false;
    }

    @OriginalMember(owner = "client!kla", name = "c", descriptor = "(B)V")
    public void method4982() {
        @Pc(7) int local7 = 32767;
        @Pc(9) int local9 = 32767;
        @Pc(11) int local11 = 32767;
        @Pc(13) int local13 = -32768;
        @Pc(15) int local15 = -32768;
        @Pc(25) int local25 = -32768;
        @Pc(27) int local27 = 0;
        @Pc(29) int local29 = 0;
        for (@Pc(31) int local31 = 0; local31 < this.anInt5543; local31++) {
            @Pc(40) int local40 = this.anIntArray410[local31];
            @Pc(45) int local45 = this.anIntArray409[local31];
            if (local15 < local45) {
                local15 = local45;
            }
            if (local40 < local7) {
                local7 = local40;
            }
            @Pc(68) int local68 = this.anIntArray412[local31];
            if (local45 < local9) {
                local9 = local45;
            }
            if (local40 > local13) {
                local13 = local40;
            }
            if (local68 > local25) {
                local25 = local68;
            }
            if (local68 < local11) {
                local11 = local68;
            }
            @Pc(100) int local100 = local68 * local68 + local40 * local40;
            if (local27 < local100) {
                local27 = local100;
            }
            local100 = local68 * local68 + local40 * local40 + local45 * local45;
            if (local29 < local100) {
                local29 = local100;
            }
        }
        this.aShort68 = (short) local15;
        this.aShort63 = (short) local9;
        this.aShort61 = (short) local11;
        this.aShort69 = (short) local25;
        this.aShort66 = (short) local7;
        this.aShort62 = (short) local13;
        this.aShort70 = (short) (int) (Math.sqrt(local27) + 0.99D);
        this.aShort65 = (short) (int) (Math.sqrt(local29) + 0.99D);
        this.aBoolean424 = true;
    }

    @OriginalMember(owner = "client!kla", name = "O", descriptor = "(III)V")
    @Override
    public void O(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        for (@Pc(3) int local3 = 0; local3 < this.anInt5543; local3++) {
            if (arg0 != 128) {
                this.anIntArray410[local3] = this.anIntArray410[local3] * arg0 >> 7;
            }
            if (arg1 != 128) {
                this.anIntArray409[local3] = arg1 * this.anIntArray409[local3] >> 7;
            }
            if (arg2 != 128) {
                this.anIntArray412[local3] = this.anIntArray412[local3] * arg2 >> 7;
            }
        }
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
        this.aBoolean424 = false;
    }

    @OriginalMember(owner = "client!kla", name = "ba", descriptor = "(Lclient!r;)Lclient!r;")
    @Override
    public Shadow ba(@OriginalArg(0) Shadow shadow) {
        if (this.anInt5538 == 0) {
            return null;
        }
        if (!this.aBoolean424) {
            this.method4982();
        }
        @Pc(43) int local43;
        @Pc(60) int local60;
        if (this.aClass19_Sub3_24.anInt8027 > 0) {
            local43 = this.aShort66 - (this.aShort68 * this.aClass19_Sub3_24.anInt8027 >> 8) >> this.aClass19_Sub3_24.anInt7989;
            local60 = this.aShort62 - (this.aClass19_Sub3_24.anInt8027 * this.aShort63 >> 8) >> this.aClass19_Sub3_24.anInt7989;
        } else {
            local43 = this.aShort66 - (this.aShort63 * this.aClass19_Sub3_24.anInt8027 >> 8) >> this.aClass19_Sub3_24.anInt7989;
            local60 = this.aShort62 - (this.aClass19_Sub3_24.anInt8027 * this.aShort68 >> 8) >> this.aClass19_Sub3_24.anInt7989;
        }
        @Pc(116) int local116;
        @Pc(132) int local132;
        if (this.aClass19_Sub3_24.anInt8023 <= 0) {
            local116 = this.aShort61 - (this.aShort63 * this.aClass19_Sub3_24.anInt8023 >> 8) >> this.aClass19_Sub3_24.anInt7989;
            local132 = this.aShort69 - (this.aShort68 * this.aClass19_Sub3_24.anInt8023 >> 8) >> this.aClass19_Sub3_24.anInt7989;
        } else {
            local116 = this.aShort61 - (this.aShort68 * this.aClass19_Sub3_24.anInt8023 >> 8) >> this.aClass19_Sub3_24.anInt7989;
            local132 = this.aShort69 - (this.aClass19_Sub3_24.anInt8023 * this.aShort63 >> 8) >> this.aClass19_Sub3_24.anInt7989;
        }
        @Pc(175) int local175 = local60 + 1 - local43;
        @Pc(182) int local182 = local132 + 1 - local116;
        @Pc(185) DoublyLinkedNode_Sub2_Sub9_ local185 = (DoublyLinkedNode_Sub2_Sub9_) shadow;
        @Pc(197) DoublyLinkedNode_Sub2_Sub9_ local197;
        if (local185 != null && local185.method6545(local182, local175)) {
            local197 = local185;
            local185.method6546();
        } else {
            local197 = new DoublyLinkedNode_Sub2_Sub9_(this.aClass19_Sub3_24, local175, local182);
        }
        local197.method6544(local60, local116, local132, local43);
        this.method4987(local197);
        return local197;
    }

    @OriginalMember(owner = "client!kla", name = "b", descriptor = "(B)V")
    public void method4983() {
        if (!this.aBoolean422) {
            return;
        }
        this.aBoolean422 = false;
        if (this.aModelParticleEmitterArray4 == null && this.aModelParticleEffectorArray4 == null && this.billboardFaces == null && !Static298.method4387(this.anInt5529, this.anInt5520)) {
            @Pc(37) boolean local37 = false;
            @Pc(39) boolean local39 = false;
            @Pc(41) boolean local41 = false;
            if (this.anIntArray410 != null && !Static437.method5917(-29939 - 122, this.anInt5529, this.anInt5520)) {
                if (this.aClass94_8 != null && this.aClass94_8.anInterface12_2 == null) {
                    this.aBoolean422 = true;
                } else {
                    if (!this.aBoolean424) {
                        this.method4982();
                    }
                    local37 = true;
                }
            }
            if (this.anIntArray409 != null && !method5577(this.anInt5529, this.anInt5520)) {
                if (this.aClass94_8 != null && this.aClass94_8.anInterface12_2 == null) {
                    this.aBoolean422 = true;
                } else {
                    local39 = true;
                    if (!this.aBoolean424) {
                        this.method4982();
                    }
                }
            }
            if (this.anIntArray412 != null && !Static19.method264(this.anInt5529, this.anInt5520)) {
                if (this.aClass94_8 != null && this.aClass94_8.anInterface12_2 == null) {
                    this.aBoolean422 = true;
                } else {
                    local41 = true;
                    if (!this.aBoolean424) {
                        this.method4982();
                    }
                }
            }
            if (local37) {
                this.anIntArray410 = null;
            }
            if (local39) {
                this.anIntArray409 = null;
            }
            if (local41) {
                this.anIntArray412 = null;
            }
        }
        if (this.aShortArray70 != null && this.anIntArray410 == null && this.anIntArray409 == null && this.anIntArray412 == null) {
            this.anIntArray411 = null;
            this.aShortArray70 = null;
        }
        if (this.aByteArray53 != null && !Static88.method1705(this.anInt5529, this.anInt5520)) {
            if (this.aClass94_7 == null) {
                if (this.aClass94_6 != null && this.aClass94_6.anInterface12_2 == null) {
                    this.aBoolean422 = true;
                } else {
                    this.aShortArray69 = this.aShortArray73 = this.aShortArray72 = null;
                    this.aByteArray53 = null;
                }
            } else if (this.aClass94_7.anInterface12_2 == null) {
                this.aBoolean422 = true;
            } else {
                this.aShortArray69 = this.aShortArray73 = this.aShortArray72 = null;
                this.aByteArray53 = null;
            }
        }
        if (this.aShortArray74 != null && !Static438.method5919(this.anInt5520, this.anInt5529)) {
            if (this.aClass94_6 != null && this.aClass94_6.anInterface12_2 == null) {
                this.aBoolean422 = true;
            } else {
                this.aShortArray74 = null;
            }
        }
        if (this.aByteArray54 != null && !Static637.method8387(this.anInt5520, this.anInt5529)) {
            if (this.aClass94_6 != null && this.aClass94_6.anInterface12_2 == null) {
                this.aBoolean422 = true;
            } else {
                this.aByteArray54 = null;
            }
        }
        if (this.aFloatArray37 != null && !Static643.method8442(this.anInt5520, this.anInt5529)) {
            if (this.aClass94_9 != null && this.aClass94_9.anInterface12_2 == null) {
                this.aBoolean422 = true;
            } else {
                this.aFloatArray37 = this.aFloatArray39 = null;
            }
        }
        if (this.aShortArray75 != null && !Static632.method8364(this.anInt5520, this.anInt5529)) {
            if (this.aClass94_6 != null && this.aClass94_6.anInterface12_2 == null) {
                this.aBoolean422 = true;
            } else {
                this.aShortArray75 = null;
            }
        }
        if (this.aShortArray67 != null && !Static390.method5495(this.anInt5520, this.anInt5529)) {
            if (this.aClass136_1 != null && this.aClass136_1.anInterface1_1 == null || this.aClass94_6 != null && this.aClass94_6.anInterface12_2 == null) {
                this.aBoolean422 = true;
            } else {
                this.aShortArray67 = this.aShortArray71 = this.aShortArray76 = null;
            }
        }
        if (this.anIntArrayArray129 != null && !Static596.method7814(this.anInt5520, this.anInt5529)) {
            this.anIntArrayArray129 = null;
            this.aShortArray68 = null;
        }
        if (this.anIntArrayArray131 != null && !Static685.method8950(0xFFFFFF86 ^ 0x27, this.anInt5520, this.anInt5529)) {
            this.aShortArray66 = null;
            this.anIntArrayArray131 = null;
        }
        if (this.anIntArrayArray130 != null && !Static90.method1732(this.anInt5529, this.anInt5520)) {
            this.anIntArrayArray130 = null;
        }
        if (this.anIntArray413 != null && (this.anInt5520 & 0x800) == 0 && (this.anInt5520 & 0x40000) == 0) {
            this.anIntArray413 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "WA", descriptor = "()I")
    @Override
    public int WA() {
        return this.aShort64;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(BIZ)Lclient!ka;")
    @Override
    public Model copy(@OriginalArg(0) byte arg0, @OriginalArg(1) int functionMask, @OriginalArg(2) boolean arg2) {
        @Pc(5) boolean local5 = false;
        @Pc(25) Model_Sub2 local25;
        @Pc(24) Model_Sub2 local24;
        if (arg0 > 0 && arg0 <= 7) {
            local25 = this.aClass19_Sub3_24.aClass114_Sub2Array2[arg0 - 1];
            local5 = true;
            local24 = this.aClass19_Sub3_24.aClass114_Sub2Array1[arg0 - 1];
        } else {
            local25 = local24 = new Model_Sub2(this.aClass19_Sub3_24);
        }
        return this.method4992(local24, local5, arg2, functionMask, local25);
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(B)V")
    public void method4984() {
        if (this.billboardFaces == null) {
            return;
        }
        @Pc(16) Matrix_Sub3 local16 = this.aClass19_Sub3_24.aClass73_Sub3_2;
        this.aClass19_Sub3_24.method7011();
        this.aClass19_Sub3_24.C(!this.aBoolean421);
        this.aClass19_Sub3_24.method7008(false);
        this.aClass19_Sub3_24.method7039(null, null, this.aClass19_Sub3_24.aClass94_15, this.aClass19_Sub3_24.aClass94_16);
        for (@Pc(49) int local49 = 0; local49 < this.billboardCount; local49++) {
            @Pc(58) GlBillboardFace local58 = this.billboardFaces[local49];
            @Pc(63) GlBillboardAttributes local63 = this.billboardAttributes[local49];
            if (!local58.hideWithBloom || !this.aClass19_Sub3_24.bloom()) {
                @Pc(94) float local94 = (float) (this.anIntArray410[local58.vertexC] + this.anIntArray410[local58.vertexA] + this.anIntArray410[local58.vertexB]) * 0.3333333F;
                @Pc(115) float local115 = (float) (this.anIntArray409[local58.vertexA] + this.anIntArray409[local58.vertexB] + this.anIntArray409[local58.vertexC]) * 0.3333333F;
                @Pc(138) float local138 = (float) (this.anIntArray412[local58.vertexA] + this.anIntArray412[local58.vertexB] + this.anIntArray412[local58.vertexC]) * 0.3333333F;
                @Pc(152) float local152 = Static9.aFloat1 + local138 * Static376.aFloat113 + Static393.aFloat117 * local94 + local115 * Static238.aFloat73;
                @Pc(166) float local166 = Static209.aFloat70 + Static409.aFloat118 * local138 + Static91.aFloat208 * local115 + Static430.aFloat120 * local94;
                @Pc(180) float local180 = local115 * Static188.aFloat65 + Static289.aFloat84 * local94 + Static190.aFloat66 * local138 + Static24.aFloat20;
                @Pc(201) float local201 = (float) (1.0D / Math.sqrt(local180 * local180 + local152 * local152 + local166 * local166)) * (float) local58.distance;
                local16.method7144(local63.scaleX * local58.width >> 7, (float) local63.offsetX + local152 - local201 * local152, local63.angle, local58.height * local63.scaleY >> 7, local201 * local180 - local180, local166 * local201 + ((float) local63.offsetY - local166));
                this.aClass19_Sub3_24.method7012(local16);
                @Pc(253) int local253 = local63.colour;
                OpenGL.glColor4ub((byte) (local253 >> 16), (byte) (local253 >> 8), (byte) local253, (byte) (local253 >> 24));
                this.aClass19_Sub3_24.method7046(local58.texture);
                this.aClass19_Sub3_24.setBlendMode(local58.blendMode);
                this.aClass19_Sub3_24.method6998(4);
            }
        }
        this.aClass19_Sub3_24.C(true);
        this.aClass19_Sub3_24.method6996();
    }

    @OriginalMember(owner = "client!kla", name = "b", descriptor = "()[B")
    @Override
    public byte[] getFaceAlphas() {
        return this.aByteArray54;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(ZBIIS)I")
    public int method4985(@OriginalArg(1) byte arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) short arg3) {
        @Pc(20) int local20 = ColourUtils.HSL_TO_RGB[Static468.method7648(arg1, arg2)];
        if (arg3 != -1) {
            @Pc(33) TextureMetrics local33 = this.aClass19_Sub3_24.textureSource.getMetrics(arg3 & 0xFFFF);
            @Pc(38) int local38 = local33.alpha & 0xFF;
            @Pc(49) int local49;
            @Pc(78) int local78;
            if (local38 != 0) {
                if (arg2 < 0) {
                    local49 = 0;
                } else if (arg2 <= 127) {
                    local49 = arg2 * 131586;
                } else {
                    local49 = 16777215;
                }
                if (local38 == 256) {
                    local20 = local49;
                } else {
                    local78 = 256 - local38;
                    local20 = ((local20 & 0xFF00) * local78 + local38 * (local49 & 0xFF00) & 0xFF0000) + ((local20 & 0xFF00FF) * local78 + (local49 & 0xFF00FF) * local38 & 0xFF00FF00) >> 8;
                }
            }
            local49 = local33.aByte57 & 0xFF;
            if (local49 != 0) {
                local49 += 256;
                @Pc(124) int local124 = local49 * (local20 >> 16 & 0xFF);
                if (local124 > 65535) {
                    local124 = 65535;
                }
                local78 = local49 * (local20 >> 8 & 0xFF);
                if (local78 > 65535) {
                    local78 = 65535;
                }
                @Pc(152) int local152 = local49 * (local20 & 0xFF);
                if (local152 > 65535) {
                    local152 = 65535;
                }
                local20 = (local78 & 0xFF00) + (local124 << 8 & 0xFF00B1) + (local152 >> 8);
            }
        }
        return (local20 << 8) + 255 - (arg0 & 0xFF);
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(I)V")
    @Override
    public void a(@OriginalArg(0) int arg0) {
        @Pc(9) int local9 = Trig1.SIN[arg0];
        @Pc(13) int local13 = Trig1.COS[arg0];
        for (@Pc(15) int local15 = 0; local15 < this.anInt5543; local15++) {
            @Pc(36) int local36 = this.anIntArray412[local15] * local9 + this.anIntArray410[local15] * local13 >> 14;
            this.anIntArray412[local15] = this.anIntArray412[local15] * local13 - this.anIntArray410[local15] * local9 >> 14;
            this.anIntArray410[local15] = local36;
        }
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
        this.aBoolean424 = false;
    }

    @OriginalMember(owner = "client!kla", name = "wa", descriptor = "()V")
    @Override
    protected void wa() {
        for (@Pc(3) int local3 = 0; local3 < this.anInt5523; local3++) {
            this.anIntArray410[local3] = this.anIntArray410[local3] + 7 >> 4;
            this.anIntArray409[local3] = this.anIntArray409[local3] + 7 >> 4;
            this.anIntArray412[local3] = this.anIntArray412[local3] + 7 >> 4;
        }
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
        this.aBoolean424 = false;
    }

    @OriginalMember(owner = "client!kla", name = "d", descriptor = "()V")
    @Override
    protected void method7494() {
    }

    @OriginalMember(owner = "client!kla", name = "NA", descriptor = "()Z")
    @Override
    protected boolean NA() {
        if (this.anIntArrayArray131 == null) {
            return false;
        }
        for (@Pc(13) int local13 = 0; local13 < this.anInt5523; local13++) {
            this.anIntArray410[local13] <<= 0x4;
            this.anIntArray409[local13] <<= 0x4;
            this.anIntArray412[local13] <<= 0x4;
        }
        Static711.anInt10684 = 0;
        Static564.anInt8464 = 0;
        Static78.anInt1624 = 0;
        return true;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!ph;Z)V")
    public void method4987(@OriginalArg(0) DoublyLinkedNode_Sub2_Sub9_ arg0) {
        if (this.anInt5538 > this.aClass19_Sub3_24.anIntArray623.length) {
            this.aClass19_Sub3_24.anIntArray625 = new int[this.anInt5538];
            this.aClass19_Sub3_24.anIntArray623 = new int[this.anInt5538];
        }
        @Pc(30) int[] local30 = this.aClass19_Sub3_24.anIntArray623;
        @Pc(34) int[] local34 = this.aClass19_Sub3_24.anIntArray625;
        @Pc(64) int local64;
        @Pc(103) int local103;
        @Pc(112) int local112;
        for (@Pc(36) int local36 = 0; local36 < this.anInt5543; local36++) {
            local64 = (this.anIntArray410[local36] - (this.anIntArray409[local36] * this.aClass19_Sub3_24.anInt8027 >> 8) >> this.aClass19_Sub3_24.anInt7989) - arg0.anInt7350;
            @Pc(89) int local89 = (this.anIntArray412[local36] - (this.aClass19_Sub3_24.anInt8023 * this.anIntArray409[local36] >> 8) >> this.aClass19_Sub3_24.anInt7989) - arg0.anInt7353;
            @Pc(94) int local94 = this.anIntArray411[local36];
            @Pc(101) int local101 = this.anIntArray411[local36 + 1];
            for (local103 = local94; local103 < local101; local103++) {
                local112 = this.aShortArray70[local103] - 1;
                if (local112 == -1) {
                    break;
                }
                local30[local112] = local64;
                local34[local112] = local89;
            }
        }
        for (local64 = 0; local64 < this.anInt5506; local64++) {
            if (this.aByteArray54 == null || this.aByteArray54[local64] <= 128) {
                @Pc(186) short local186 = this.aShortArray67[local64];
                @Pc(191) short local191 = this.aShortArray71[local64];
                @Pc(196) short local196 = this.aShortArray76[local64];
                local103 = local30[local186];
                local112 = local30[local191];
                @Pc(208) int local208 = local30[local196];
                @Pc(212) int local212 = local34[local186];
                @Pc(216) int local216 = local34[local191];
                @Pc(220) int local220 = local34[local196];
                if (-((local208 - local112) * (-local212 + local216)) + (local103 - local112) * (local216 - local220) > 0) {
                    arg0.method6542(local103, local212, local112, local216, local220, local208);
                }
            }
        }
    }

    @OriginalMember(owner = "client!kla", name = "c", descriptor = "()[Lclient!mn;")
    @Override
    public ModelParticleEffector[] particleEffectors() {
        return this.aModelParticleEffectorArray4;
    }

    @OriginalMember(owner = "client!kla", name = "H", descriptor = "(III)V")
    @Override
    public void H(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z) {
        for (@Pc(7) int local7 = 0; local7 < this.anInt5543; local7++) {
            if (x != 0) {
                this.anIntArray410[local7] += x;
            }
            if (y != 0) {
                this.anIntArray409[local7] += y;
            }
            if (z != 0) {
                this.anIntArray412[local7] += z;
            }
        }
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
        this.aBoolean424 = false;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Z)V")
    public void method4988() {
        if (this.anInt5506 == 0) {
            return;
        }
        if (this.aByte89 != 0) {
            this.method4991(true);
        }
        this.method4991(false);
        if (this.aClass136_1 != null) {
            if (this.aClass136_1.anInterface1_1 == null) {
                this.method4990((this.aByte89 & 0x10) != 0);
            }
            if (this.aClass136_1.anInterface1_1 != null) {
                this.aClass19_Sub3_24.method7008(this.aClass94_7 != null);
                this.aClass19_Sub3_24.method7039(this.aClass94_6, this.aClass94_7, this.aClass94_8, this.aClass94_9);
                @Pc(84) int local84 = this.anIntArray413.length - 1;
                for (@Pc(86) int local86 = 0; local86 < local84; local86++) {
                    @Pc(93) int local93 = this.anIntArray413[local86];
                    @Pc(100) int local100 = this.anIntArray413[local86 + 1];
                    @Pc(107) int local107 = this.aShortArray75[local93] & 0xFFFF;
                    if (local107 == 65535) {
                        local107 = -1;
                    }
                    this.aClass19_Sub3_24.method6975(this.aClass94_7 != null, local107);
                    this.aClass19_Sub3_24.method7034(local93 * 3, this.aClass136_1.anInterface1_1, (local100 - local93) * 3);
                }
            }
        }
        this.method4983();
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(IIIIIIIII)Z")
    public boolean method4989(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7) {
        if (arg6 > arg7 && arg4 > arg7 && arg7 < arg2) {
            return false;
        } else if (arg6 < arg7 && arg4 < arg7 && arg7 > arg2) {
            return false;
        } else if (arg1 > arg3 && arg0 > arg3 && arg3 < arg5) {
            return false;
        } else {
            return arg1 >= arg3 || arg0 >= arg3 || arg5 >= arg3;
        }
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void apply(@OriginalArg(0) Matrix arg0) {
        @Pc(8) Matrix_Sub3 local8 = (Matrix_Sub3) arg0;
        @Pc(13) int local13;
        if (this.aModelParticleEmitterArray4 != null) {
            for (local13 = 0; local13 < this.aModelParticleEmitterArray4.length; local13++) {
                @Pc(20) ModelParticleEmitter local20 = this.aModelParticleEmitterArray4[local13];
                @Pc(22) ModelParticleEmitter local22 = local20;
                if (local20.next != null) {
                    local22 = local20.next;
                }
                local22.anInt8518 = (int) (local8.aFloat153 * (float) this.anIntArray410[local20.anInt8514] + local8.aFloat157 * (float) this.anIntArray409[local20.anInt8514] + (float) this.anIntArray412[local20.anInt8514] * local8.aFloat160 + local8.aFloat152);
                local22.anInt8502 = (int) ((float) this.anIntArray412[local20.anInt8514] * local8.aFloat150 + (float) this.anIntArray409[local20.anInt8514] * local8.aFloat156 + local8.aFloat161 * (float) this.anIntArray410[local20.anInt8514] + local8.aFloat158);
                local22.anInt8504 = (int) (local8.aFloat154 * (float) this.anIntArray412[local20.anInt8514] + (float) this.anIntArray410[local20.anInt8514] * local8.aFloat155 + local8.aFloat151 * (float) this.anIntArray409[local20.anInt8514] + local8.aFloat159);
                local22.anInt8516 = (int) (local8.aFloat160 * (float) this.anIntArray412[local20.anInt8508] + (float) this.anIntArray409[local20.anInt8508] * local8.aFloat157 + (float) this.anIntArray410[local20.anInt8508] * local8.aFloat153 + local8.aFloat152);
                local22.anInt8507 = (int) (local8.aFloat158 + local8.aFloat150 * (float) this.anIntArray412[local20.anInt8508] + (float) this.anIntArray410[local20.anInt8508] * local8.aFloat161 + local8.aFloat156 * (float) this.anIntArray409[local20.anInt8508]);
                local22.anInt8509 = (int) (local8.aFloat159 + (float) this.anIntArray409[local20.anInt8508] * local8.aFloat151 + local8.aFloat155 * (float) this.anIntArray410[local20.anInt8508] + local8.aFloat154 * (float) this.anIntArray412[local20.anInt8508]);
                local22.anInt8512 = (int) (local8.aFloat152 + local8.aFloat160 * (float) this.anIntArray412[local20.anInt8505] + (float) this.anIntArray409[local20.anInt8505] * local8.aFloat157 + (float) this.anIntArray410[local20.anInt8505] * local8.aFloat153);
                local22.anInt8503 = (int) ((float) this.anIntArray410[local20.anInt8505] * local8.aFloat161 + (float) this.anIntArray409[local20.anInt8505] * local8.aFloat156 + (float) this.anIntArray412[local20.anInt8505] * local8.aFloat150 + local8.aFloat158);
                local22.anInt8520 = (int) (local8.aFloat159 + local8.aFloat154 * (float) this.anIntArray412[local20.anInt8505] + (float) this.anIntArray410[local20.anInt8505] * local8.aFloat155 + local8.aFloat151 * (float) this.anIntArray409[local20.anInt8505]);
            }
        }
        if (this.aModelParticleEffectorArray4 == null) {
            return;
        }
        for (local13 = 0; local13 < this.aModelParticleEffectorArray4.length; local13++) {
            @Pc(367) ModelParticleEffector local367 = this.aModelParticleEffectorArray4[local13];
            @Pc(369) ModelParticleEffector local369 = local367;
            if (local367.next != null) {
                local369 = local367.next;
            }
            if (local367.matrix == null) {
                local367.matrix = local8.copy();
            } else {
                local367.matrix.apply(local8);
            }
            local369.x = (int) (local8.aFloat157 * (float) this.anIntArray409[local367.vertex] + (float) this.anIntArray410[local367.vertex] * local8.aFloat153 + local8.aFloat160 * (float) this.anIntArray412[local367.vertex] + local8.aFloat152);
            local369.y = (int) ((float) this.anIntArray410[local367.vertex] * local8.aFloat161 + (float) this.anIntArray409[local367.vertex] * local8.aFloat156 + local8.aFloat150 * (float) this.anIntArray412[local367.vertex] + local8.aFloat158);
            local369.z = (int) (local8.aFloat155 * (float) this.anIntArray410[local367.vertex] + local8.aFloat151 * (float) this.anIntArray409[local367.vertex] + (float) this.anIntArray412[local367.vertex] * local8.aFloat154 + local8.aFloat159);
        }
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!tt;IZ)V")
    @Override
    public void transform(@OriginalArg(0) Matrix matrix, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2) {
        if (this.aShortArray66 == null) {
            return;
        }
        @Pc(13) int[] local13 = new int[3];
        for (@Pc(15) int local15 = 0; local15 < this.anInt5543; local15++) {
            if ((arg1 & this.aShortArray66[local15]) != 0) {
                if (arg2) {
                    matrix.projectRelative(this.anIntArray410[local15], this.anIntArray409[local15], this.anIntArray412[local15], local13);
                } else {
                    matrix.project(this.anIntArray410[local15], this.anIntArray409[local15], this.anIntArray412[local15], local13);
                }
                this.anIntArray410[local15] = local13[0];
                this.anIntArray409[local15] = local13[1];
                this.anIntArray412[local15] = local13[2];
            }
        }
    }

    @OriginalMember(owner = "client!kla", name = "C", descriptor = "(I)V")
    @Override
    public void C(@OriginalArg(0) int ambient) {
        if (this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
        }
        this.aShort64 = (short) ambient;
    }

    @OriginalMember(owner = "client!kla", name = "ua", descriptor = "()I")
    @Override
    public int ua() {
        return this.anInt5520;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!tt;Lclient!ima;I)V")
    @Override
    public void render(@OriginalArg(0) Matrix matrix, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int flags) {
        if (this.anInt5538 == 0) {
            return;
        }
        @Pc(17) Matrix_Sub3 local17 = this.aClass19_Sub3_24.aClass73_Sub3_3;
        @Pc(20) Matrix_Sub3 local20 = (Matrix_Sub3) matrix;
        if (!this.aBoolean424) {
            this.method4982();
        }
        Static188.aFloat65 = local17.aFloat155 * local20.aFloat157 + local17.aFloat151 * local20.aFloat156 + local20.aFloat151 * local17.aFloat154;
        Static24.aFloat20 = local17.aFloat159 + local17.aFloat151 * local20.aFloat158 + local17.aFloat155 * local20.aFloat152 + local20.aFloat159 * local17.aFloat154;
        @Pc(75) float local75 = (float) this.aShort63 * Static188.aFloat65 + Static24.aFloat20;
        @Pc(83) float local83 = Static24.aFloat20 + Static188.aFloat65 * (float) this.aShort68;
        @Pc(100) float local100;
        @Pc(93) float local93;
        if (local83 < local75) {
            local93 = local75 + (float) this.aShort70;
            local100 = (float) -this.aShort70 + local83;
        } else {
            local100 = (float) -this.aShort70 + local75;
            local93 = local83 + (float) this.aShort70;
        }
        if (local100 >= this.aClass19_Sub3_24.aFloat144 || local93 <= (float) this.aClass19_Sub3_24.anInt8010) {
            return;
        }
        Static9.aFloat1 = local17.aFloat157 * local20.aFloat158 + local17.aFloat153 * local20.aFloat152 + local17.aFloat160 * local20.aFloat159 + local17.aFloat152;
        Static238.aFloat73 = local20.aFloat157 * local17.aFloat153 + local20.aFloat156 * local17.aFloat157 + local17.aFloat160 * local20.aFloat151;
        @Pc(179) float local179 = Static9.aFloat1 + (float) this.aShort63 * Static238.aFloat73;
        @Pc(187) float local187 = (float) this.aShort68 * Static238.aFloat73 + Static9.aFloat1;
        @Pc(202) float local202;
        @Pc(213) float local213;
        if (local179 > local187) {
            local202 = (float) this.aClass19_Sub3_24.anInt8001 * ((float) -this.aShort70 + local187);
            local213 = (float) this.aClass19_Sub3_24.anInt8001 * ((float) this.aShort70 + local179);
        } else {
            local202 = (float) this.aClass19_Sub3_24.anInt8001 * (local179 - (float) this.aShort70);
            local213 = ((float) this.aShort70 + local187) * (float) this.aClass19_Sub3_24.anInt8001;
        }
        if (this.aClass19_Sub3_24.aFloat138 <= local202 / local93 || local213 / local93 <= this.aClass19_Sub3_24.aFloat145) {
            return;
        }
        Static209.aFloat70 = local20.aFloat152 * local17.aFloat161 + local17.aFloat156 * local20.aFloat158 + local20.aFloat159 * local17.aFloat150 + local17.aFloat158;
        Static91.aFloat208 = local20.aFloat151 * local17.aFloat150 + local20.aFloat156 * local17.aFloat156 + local20.aFloat157 * local17.aFloat161;
        @Pc(305) float local305 = Static209.aFloat70 + (float) this.aShort63 * Static91.aFloat208;
        @Pc(313) float local313 = Static91.aFloat208 * (float) this.aShort68 + Static209.aFloat70;
        @Pc(328) float local328;
        @Pc(339) float local339;
        if (local305 > local313) {
            local339 = (float) this.aClass19_Sub3_24.anInt8025 * (local305 + (float) this.aShort70);
            local328 = (local313 - (float) this.aShort70) * (float) this.aClass19_Sub3_24.anInt8025;
        } else {
            local328 = (local305 - (float) this.aShort70) * (float) this.aClass19_Sub3_24.anInt8025;
            local339 = (float) this.aClass19_Sub3_24.anInt8025 * (local313 + (float) this.aShort70);
        }
        if (local328 / local93 >= this.aClass19_Sub3_24.aFloat133 || local339 / local93 <= this.aClass19_Sub3_24.aFloat141) {
            return;
        }
        if (cylinder != null || this.billboardFaces != null) {
            Static376.aFloat113 = local17.aFloat160 * local20.aFloat154 + local17.aFloat157 * local20.aFloat150 + local17.aFloat153 * local20.aFloat160;
            Static430.aFloat120 = local17.aFloat161 * local20.aFloat153 + local17.aFloat156 * local20.aFloat161 + local17.aFloat150 * local20.aFloat155;
            Static393.aFloat117 = local17.aFloat157 * local20.aFloat161 + local17.aFloat153 * local20.aFloat153 + local17.aFloat160 * local20.aFloat155;
            Static190.aFloat66 = local17.aFloat154 * local20.aFloat154 + local17.aFloat151 * local20.aFloat150 + local20.aFloat160 * local17.aFloat155;
            Static409.aFloat118 = local17.aFloat150 * local20.aFloat154 + local20.aFloat160 * local17.aFloat161 + local17.aFloat156 * local20.aFloat150;
            Static289.aFloat84 = local20.aFloat161 * local17.aFloat151 + local20.aFloat153 * local17.aFloat155 + local17.aFloat154 * local20.aFloat155;
        }
        if (cylinder != null) {
            @Pc(504) boolean local504 = false;
            @Pc(506) boolean local506 = true;
            @Pc(514) int local514 = this.aShort62 + this.aShort66 >> 1;
            @Pc(523) int local523 = this.aShort61 + this.aShort69 >> 1;
            @Pc(542) int local542 = (int) ((float) local523 * Static376.aFloat113 + (float) this.aShort63 * Static238.aFloat73 + (float) local514 * Static393.aFloat117 + Static9.aFloat1);
            @Pc(561) int local561 = (int) ((float) local523 * Static409.aFloat118 + (float) this.aShort63 * Static91.aFloat208 + Static430.aFloat120 * (float) local514 + Static209.aFloat70);
            @Pc(580) int local580 = (int) ((float) this.aShort63 * Static188.aFloat65 + Static24.aFloat20 + (float) local514 * Static289.aFloat84 + Static190.aFloat66 * (float) local523);
            if (local580 >= this.aClass19_Sub3_24.anInt8010) {
                cylinder.anInt4504 = local542 * this.aClass19_Sub3_24.anInt8001 / local580 + this.aClass19_Sub3_24.anInt8021;
                cylinder.anInt4505 = this.aClass19_Sub3_24.anInt8025 * local561 / local580 + this.aClass19_Sub3_24.anInt8016;
            } else {
                local504 = true;
            }
            @Pc(636) int local636 = (int) ((float) local523 * Static376.aFloat113 + (float) this.aShort68 * Static238.aFloat73 + Static9.aFloat1 + (float) local514 * Static393.aFloat117);
            @Pc(655) int local655 = (int) (Static91.aFloat208 * (float) this.aShort68 + Static209.aFloat70 + Static430.aFloat120 * (float) local514 + (float) local523 * Static409.aFloat118);
            @Pc(674) int local674 = (int) (Static24.aFloat20 + Static289.aFloat84 * (float) local514 + (float) this.aShort68 * Static188.aFloat65 + (float) local523 * Static190.aFloat66);
            if (this.aClass19_Sub3_24.anInt8010 <= local674) {
                cylinder.anInt4503 = this.aClass19_Sub3_24.anInt8025 * local655 / local674 + this.aClass19_Sub3_24.anInt8016;
                cylinder.anInt4501 = local636 * this.aClass19_Sub3_24.anInt8001 / local674 + this.aClass19_Sub3_24.anInt8021;
            } else {
                local504 = true;
            }
            if (local504) {
                if (this.aClass19_Sub3_24.anInt8010 > local580 && local674 < this.aClass19_Sub3_24.anInt8010) {
                    local506 = false;
                } else {
                    @Pc(754) int local754;
                    @Pc(765) int local765;
                    @Pc(776) int local776;
                    if (this.aClass19_Sub3_24.anInt8010 > local580) {
                        local754 = (local674 - this.aClass19_Sub3_24.anInt8010 << 16) / (local674 - local580);
                        local765 = ((local636 - local542) * local754 >> 16) + local636;
                        local776 = local655 + (local754 * (local655 - local561) >> 16);
                        cylinder.anInt4504 = local765 * this.aClass19_Sub3_24.anInt8001 / this.aClass19_Sub3_24.anInt8010 + this.aClass19_Sub3_24.anInt8021;
                        cylinder.anInt4505 = this.aClass19_Sub3_24.anInt8016 + this.aClass19_Sub3_24.anInt8025 * local776 / this.aClass19_Sub3_24.anInt8010;
                    } else if (this.aClass19_Sub3_24.anInt8010 > local674) {
                        local754 = (local580 - this.aClass19_Sub3_24.anInt8010 << 16) / (local580 - local674);
                        local765 = (local754 * (local542 - local636) >> 16) + local542;
                        local776 = ((local561 - local655) * local754 >> 16) + local561;
                        cylinder.anInt4504 = this.aClass19_Sub3_24.anInt8021 + this.aClass19_Sub3_24.anInt8001 * local765 / this.aClass19_Sub3_24.anInt8010;
                        cylinder.anInt4505 = local776 * this.aClass19_Sub3_24.anInt8025 / this.aClass19_Sub3_24.anInt8010 + this.aClass19_Sub3_24.anInt8016;
                    }
                }
            }
            if (local506) {
                if (local580 > local674) {
                    cylinder.anInt4502 = this.aClass19_Sub3_24.anInt8021 + this.aClass19_Sub3_24.anInt8001 * (this.aShort70 + local542) / local580 - cylinder.anInt4504;
                } else {
                    cylinder.anInt4502 = this.aClass19_Sub3_24.anInt8021 + (local636 + this.aShort70) * this.aClass19_Sub3_24.anInt8001 / local674 - cylinder.anInt4501;
                }
                cylinder.aBoolean352 = true;
            }
        }
        this.aClass19_Sub3_24.method7027();
        this.aClass19_Sub3_24.method7020(local20);
        this.method4988();
        this.aClass19_Sub3_24.method6996();
        this.method4984();
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(ZI)V")
    public void method4990(@OriginalArg(0) boolean arg0) {
        if (this.anInt5506 * 6 <= this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3.data.length) {
            this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3.pos = 0;
        } else {
            this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3 = new Node_Sub21_Sub1((this.anInt5506 + 100) * 6);
        }
        @Pc(46) Node_Sub21_Sub1 local46 = this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3;
        @Pc(52) int local52;
        if (this.aClass19_Sub3_24.aBoolean618) {
            for (local52 = 0; local52 < this.anInt5506; local52++) {
                local46.p2(this.aShortArray67[local52]);
                local46.p2(this.aShortArray71[local52]);
                local46.p2(this.aShortArray76[local52]);
            }
        } else {
            for (local52 = 0; local52 < this.anInt5506; local52++) {
                local46.ip2(this.aShortArray67[local52]);
                local46.ip2(this.aShortArray71[local52]);
                local46.ip2(this.aShortArray76[local52]);
            }
        }
        if (5123 != 5123) {
            this.anInterface12_4 = null;
        }
        if (local46.pos == 0) {
            return;
        }
        if (arg0) {
            if (this.anInterface1_3 == null) {
                this.anInterface1_3 = this.aClass19_Sub3_24.method7010(true, local46.pos, local46.data);
            } else {
                this.anInterface1_3.method9355(local46.pos, local46.data);
            }
            this.aClass136_1.anInterface1_1 = this.anInterface1_3;
        } else {
            this.aClass136_1.anInterface1_1 = this.aClass19_Sub3_24.method7010(false, local46.pos, local46.data);
        }
        if (!arg0) {
            this.aBoolean422 = true;
        }
    }

    @OriginalMember(owner = "client!kla", name = "fa", descriptor = "()I")
    @Override
    public int fa() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort63;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!ka;IIIZ)V")
    @Override
    public void method7481(@OriginalArg(0) Model arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        @Pc(8) Model_Sub2 local8 = (Model_Sub2) arg0;
        if (this.anInt5560 == 0 || local8.anInt5560 == 0) {
            return;
        }
        @Pc(24) int local24 = local8.anInt5543;
        @Pc(27) int[] local27 = local8.anIntArray410;
        @Pc(30) int[] local30 = local8.anIntArray409;
        @Pc(33) int[] local33 = local8.anIntArray412;
        @Pc(36) short[] local36 = local8.aShortArray69;
        @Pc(39) short[] local39 = local8.aShortArray73;
        @Pc(42) short[] local42 = local8.aShortArray72;
        @Pc(45) byte[] local45 = local8.aByteArray53;
        @Pc(60) short[] local60;
        @Pc(56) short[] local56;
        @Pc(52) short[] local52;
        @Pc(64) byte[] local64;
        if (this.aClass346_1 == null) {
            local56 = null;
            local52 = null;
            local60 = null;
            local64 = null;
        } else {
            local52 = this.aClass346_1.aShortArray128;
            local56 = this.aClass346_1.aShortArray130;
            local60 = this.aClass346_1.aShortArray129;
            local64 = this.aClass346_1.aByteArray99;
        }
        @Pc(85) short[] local85;
        @Pc(83) short[] local83;
        @Pc(81) short[] local81;
        @Pc(79) byte[] local79;
        if (local8.aClass346_1 == null) {
            local79 = null;
            local81 = null;
            local83 = null;
            local85 = null;
        } else {
            local85 = local8.aClass346_1.aShortArray129;
            local81 = local8.aClass346_1.aShortArray128;
            local83 = local8.aClass346_1.aShortArray130;
            local79 = local8.aClass346_1.aByteArray99;
        }
        @Pc(106) int[] local106 = local8.anIntArray411;
        @Pc(109) short[] local109 = local8.aShortArray70;
        if (!local8.aBoolean424) {
            local8.method4982();
        }
        @Pc(120) short local120 = local8.aShort63;
        @Pc(123) short local123 = local8.aShort68;
        @Pc(126) short local126 = local8.aShort66;
        @Pc(129) short local129 = local8.aShort62;
        @Pc(132) short local132 = local8.aShort61;
        @Pc(135) short local135 = local8.aShort69;
        for (@Pc(137) int local137 = 0; local137 < this.anInt5543; local137++) {
            @Pc(147) int local147 = this.anIntArray409[local137] - arg2;
            if (local147 >= local120 && local147 <= local123) {
                @Pc(166) int local166 = this.anIntArray410[local137] - arg1;
                if (local126 <= local166 && local166 <= local129) {
                    @Pc(190) int local190 = this.anIntArray412[local137] - arg3;
                    if (local190 >= local132 && local190 <= local135) {
                        @Pc(208) int local208 = -1;
                        @Pc(213) int local213 = this.anIntArray411[local137];
                        @Pc(220) int local220 = this.anIntArray411[local137 + 1];
                        for (@Pc(222) int local222 = local213; local222 < local220; local222++) {
                            local208 = this.aShortArray70[local222] - 1;
                            if (local208 == -1 || this.aByteArray53[local208] != 0) {
                                break;
                            }
                        }
                        if (local208 != -1) {
                            for (@Pc(258) int local258 = 0; local258 < local24; local258++) {
                                if (local27[local258] == local166 && local33[local258] == local190 && local30[local258] == local147) {
                                    local213 = local106[local258];
                                    local220 = local106[local258 + 1];
                                    @Pc(297) int local297 = -1;
                                    for (@Pc(299) int local299 = local213; local299 < local220; local299++) {
                                        local297 = local109[local299] - 1;
                                        if (local297 == -1 || local45[local297] != 0) {
                                            break;
                                        }
                                    }
                                    if (local297 != -1) {
                                        if (local60 == null) {
                                            this.aClass346_1 = new Class346();
                                            local60 = this.aClass346_1.aShortArray129 = Static57.method1230(this.aShortArray69);
                                            local56 = this.aClass346_1.aShortArray130 = Static57.method1230(this.aShortArray73);
                                            local52 = this.aClass346_1.aShortArray128 = Static57.method1230(this.aShortArray72);
                                            local64 = this.aClass346_1.aByteArray99 = Static134.method8955(this.aByteArray53);
                                        }
                                        if (local85 == null) {
                                            @Pc(389) Class346 local389 = local8.aClass346_1 = new Class346();
                                            local85 = local389.aShortArray129 = Static57.method1230(local36);
                                            local83 = local389.aShortArray130 = Static57.method1230(local39);
                                            local81 = local389.aShortArray128 = Static57.method1230(local42);
                                            local79 = local389.aByteArray99 = Static134.method8955(local45);
                                        }
                                        @Pc(422) short local422 = this.aShortArray69[local208];
                                        @Pc(427) short local427 = this.aShortArray73[local208];
                                        @Pc(432) short local432 = this.aShortArray72[local208];
                                        local213 = local106[local258];
                                        @Pc(441) byte local441 = this.aByteArray53[local208];
                                        local220 = local106[local258 + 1];
                                        @Pc(457) int local457;
                                        for (@Pc(449) int local449 = local213; local449 < local220; local449++) {
                                            local457 = local109[local449] - 1;
                                            if (local457 == -1) {
                                                break;
                                            }
                                            if (local79[local457] != 0) {
                                                local85[local457] += local422;
                                                local83[local457] += local427;
                                                local81[local457] += local432;
                                                local79[local457] += local441;
                                            }
                                        }
                                        local213 = this.anIntArray411[local137];
                                        local432 = local42[local297];
                                        local441 = local45[local297];
                                        local427 = local39[local297];
                                        local220 = this.anIntArray411[local137 + 1];
                                        local422 = local36[local297];
                                        for (local457 = local213; local457 < local220; local457++) {
                                            @Pc(550) int local550 = this.aShortArray70[local457] - 1;
                                            if (local550 == -1) {
                                                break;
                                            }
                                            if (local64[local550] != 0) {
                                                local60[local550] += local422;
                                                local56[local550] += local427;
                                                local52[local550] += local432;
                                                local64[local550] += local441;
                                            }
                                        }
                                        if (this.aClass94_7 == null && this.aClass94_6 != null) {
                                            this.aClass94_6.anInterface12_2 = null;
                                        }
                                        if (this.aClass94_7 != null) {
                                            this.aClass94_7.anInterface12_2 = null;
                                        }
                                        if (local8.aClass94_7 == null && local8.aClass94_6 != null) {
                                            local8.aClass94_6.anInterface12_2 = null;
                                        }
                                        if (local8.aClass94_7 != null) {
                                            local8.aClass94_7.anInterface12_2 = null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!kla", name = "RA", descriptor = "()I")
    @Override
    public int RA() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort62;
    }

    @OriginalMember(owner = "client!kla", name = "e", descriptor = "()V")
    @Override
    public void method7479() {
        if (this.anInt5538 <= 0 || this.anInt5506 <= 0) {
            return;
        }
        this.method4991(false);
        if ((this.aByte89 & 0x10) == 0 && this.aClass136_1.anInterface1_1 == null) {
            this.method4990(false);
        }
        this.method4983();
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!tt;Lclient!ima;II)V")
    @Override
    public void renderOrtho(@OriginalArg(0) Matrix arg0, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        if (this.anInt5538 == 0) {
            return;
        }
        @Pc(14) Matrix_Sub3 local14 = this.aClass19_Sub3_24.aClass73_Sub3_3;
        @Pc(17) Matrix_Sub3 local17 = (Matrix_Sub3) arg0;
        if (!this.aBoolean424) {
            this.method4982();
        }
        Static188.aFloat65 = local17.aFloat156 * local14.aFloat151 + local14.aFloat155 * local17.aFloat157 + local14.aFloat154 * local17.aFloat151;
        Static24.aFloat20 = local14.aFloat159 + local14.aFloat155 * local17.aFloat152 + local17.aFloat158 * local14.aFloat151 + local17.aFloat159 * local14.aFloat154;
        @Pc(72) float local72 = (float) this.aShort63 * Static188.aFloat65 + Static24.aFloat20;
        @Pc(80) float local80 = Static24.aFloat20 + (float) this.aShort68 * Static188.aFloat65;
        @Pc(90) float local90;
        @Pc(96) float local96;
        if (local72 > local80) {
            local90 = local80 - (float) this.aShort70;
            local96 = (float) this.aShort70 + local72;
        } else {
            local96 = (float) this.aShort70 + local80;
            local90 = (float) -this.aShort70 + local72;
        }
        if (this.aClass19_Sub3_24.aFloat142 <= local90 || (float) this.aClass19_Sub3_24.anInt8010 >= local96) {
            return;
        }
        Static238.aFloat73 = local17.aFloat156 * local14.aFloat157 + local17.aFloat157 * local14.aFloat153 + local14.aFloat160 * local17.aFloat151;
        Static9.aFloat1 = local14.aFloat152 + local17.aFloat159 * local14.aFloat160 + local14.aFloat157 * local17.aFloat158 + local17.aFloat152 * local14.aFloat153;
        @Pc(175) float local175 = (float) this.aShort63 * Static238.aFloat73 + Static9.aFloat1;
        @Pc(183) float local183 = Static9.aFloat1 + (float) this.aShort68 * Static238.aFloat73;
        @Pc(210) float local210;
        @Pc(198) float local198;
        if (local183 < local175) {
            local198 = (float) this.aClass19_Sub3_24.anInt8001 * ((float) this.aShort70 + local175);
            local210 = (float) this.aClass19_Sub3_24.anInt8001 * ((float) -this.aShort70 + local183);
        } else {
            local210 = (float) this.aClass19_Sub3_24.anInt8001 * (local175 - (float) this.aShort70);
            local198 = ((float) this.aShort70 + local183) * (float) this.aClass19_Sub3_24.anInt8001;
        }
        if (local210 / (float) arg2 >= this.aClass19_Sub3_24.aFloat138 || this.aClass19_Sub3_24.aFloat145 >= local198 / (float) arg2) {
            return;
        }
        Static91.aFloat208 = local17.aFloat157 * local14.aFloat161 + local17.aFloat156 * local14.aFloat156 + local17.aFloat151 * local14.aFloat150;
        Static209.aFloat70 = local14.aFloat150 * local17.aFloat159 + local14.aFloat156 * local17.aFloat158 + local14.aFloat161 * local17.aFloat152 + local14.aFloat158;
        @Pc(303) float local303 = Static209.aFloat70 + (float) this.aShort63 * Static91.aFloat208;
        @Pc(311) float local311 = Static91.aFloat208 * (float) this.aShort68 + Static209.aFloat70;
        @Pc(326) float local326;
        @Pc(337) float local337;
        if (local311 < local303) {
            local326 = (local311 - (float) this.aShort70) * (float) this.aClass19_Sub3_24.anInt8025;
            local337 = (local303 + (float) this.aShort70) * (float) this.aClass19_Sub3_24.anInt8025;
        } else {
            local337 = (float) this.aClass19_Sub3_24.anInt8025 * ((float) this.aShort70 + local311);
            local326 = ((float) -this.aShort70 + local303) * (float) this.aClass19_Sub3_24.anInt8025;
        }
        if (local326 / (float) arg2 >= this.aClass19_Sub3_24.aFloat133 || local337 / (float) arg2 <= this.aClass19_Sub3_24.aFloat141) {
            return;
        }
        if (cylinder != null || this.billboardFaces != null) {
            Static409.aFloat118 = local17.aFloat160 * local14.aFloat161 + local17.aFloat150 * local14.aFloat156 + local17.aFloat154 * local14.aFloat150;
            Static289.aFloat84 = local14.aFloat151 * local17.aFloat161 + local14.aFloat155 * local17.aFloat153 + local17.aFloat155 * local14.aFloat154;
            Static376.aFloat113 = local17.aFloat154 * local14.aFloat160 + local17.aFloat160 * local14.aFloat153 + local14.aFloat157 * local17.aFloat150;
            Static393.aFloat117 = local14.aFloat153 * local17.aFloat153 + local14.aFloat157 * local17.aFloat161 + local14.aFloat160 * local17.aFloat155;
            Static190.aFloat66 = local17.aFloat154 * local14.aFloat154 + local14.aFloat155 * local17.aFloat160 + local17.aFloat150 * local14.aFloat151;
            Static430.aFloat120 = local14.aFloat156 * local17.aFloat161 + local17.aFloat153 * local14.aFloat161 + local14.aFloat150 * local17.aFloat155;
        }
        if (cylinder != null) {
            @Pc(511) int local511 = this.aShort66 + this.aShort62 >> 1;
            @Pc(519) int local519 = this.aShort61 + this.aShort69 >> 1;
            @Pc(538) int local538 = (int) ((float) this.aShort63 * Static238.aFloat73 + Static393.aFloat117 * (float) local511 + Static9.aFloat1 + Static376.aFloat113 * (float) local519);
            @Pc(557) int local557 = (int) (Static91.aFloat208 * (float) this.aShort63 + Static430.aFloat120 * (float) local511 + Static209.aFloat70 + Static409.aFloat118 * (float) local519);
            @Pc(576) int local576 = (int) (Static190.aFloat66 * (float) local519 + Static188.aFloat65 * (float) this.aShort63 + Static24.aFloat20 + (float) local511 * Static289.aFloat84);
            @Pc(595) int local595 = (int) (Static376.aFloat113 * (float) local519 + Static238.aFloat73 * (float) this.aShort68 + Static9.aFloat1 + Static393.aFloat117 * (float) local511);
            @Pc(614) int local614 = (int) ((float) this.aShort68 * Static91.aFloat208 + Static209.aFloat70 + (float) local511 * Static430.aFloat120 + Static409.aFloat118 * (float) local519);
            @Pc(633) int local633 = (int) (Static24.aFloat20 + Static289.aFloat84 * (float) local511 + Static188.aFloat65 * (float) this.aShort68 + (float) local519 * Static190.aFloat66);
            cylinder.anInt4503 = this.aClass19_Sub3_24.anInt8016 + local614 * this.aClass19_Sub3_24.anInt8025 / arg2;
            cylinder.anInt4501 = this.aClass19_Sub3_24.anInt8001 * local595 / arg2 + this.aClass19_Sub3_24.anInt8021;
            cylinder.anInt4504 = this.aClass19_Sub3_24.anInt8021 + this.aClass19_Sub3_24.anInt8001 * local538 / arg2;
            cylinder.anInt4505 = this.aClass19_Sub3_24.anInt8025 * local557 / arg2 + this.aClass19_Sub3_24.anInt8016;
            if (local576 >= this.aClass19_Sub3_24.anInt8010 || local633 >= this.aClass19_Sub3_24.anInt8010) {
                cylinder.aBoolean352 = true;
                cylinder.anInt4502 = (local538 + this.aShort70) * this.aClass19_Sub3_24.anInt8001 / arg2 + this.aClass19_Sub3_24.anInt8021 - cylinder.anInt4504;
            }
        }
        this.aClass19_Sub3_24.method6964((float) arg2);
        this.aClass19_Sub3_24.method6978();
        this.aClass19_Sub3_24.method7020(local17);
        this.method4988();
        this.aClass19_Sub3_24.method6996();
        this.method4984();
    }

    @OriginalMember(owner = "client!kla", name = "V", descriptor = "()I")
    @Override
    public int V() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort66;
    }

    @OriginalMember(owner = "client!kla", name = "P", descriptor = "(IIII)V")
    @Override
    protected void P(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(14) int local14;
        @Pc(20) int local20;
        if (arg0 == 0) {
            Static78.anInt1624 = 0;
            local14 = 0;
            Static711.anInt10684 = 0;
            Static564.anInt8464 = 0;
            for (local20 = 0; local20 < this.anInt5543; local20++) {
                Static711.anInt10684 += this.anIntArray410[local20];
                Static564.anInt8464 += this.anIntArray409[local20];
                Static78.anInt1624 += this.anIntArray412[local20];
                local14++;
            }
            if (local14 <= 0) {
                Static564.anInt8464 = arg2;
                Static711.anInt10684 = arg1;
                Static78.anInt1624 = arg3;
            } else {
                Static711.anInt10684 = arg1 + Static711.anInt10684 / local14;
                Static78.anInt1624 = arg3 + Static78.anInt1624 / local14;
                Static564.anInt8464 = Static564.anInt8464 / local14 + arg2;
            }
        } else if (arg0 == 1) {
            for (local14 = 0; local14 < this.anInt5543; local14++) {
                this.anIntArray410[local14] += arg1;
                this.anIntArray409[local14] += arg2;
                this.anIntArray412[local14] += arg3;
            }
        } else {
            @Pc(185) int local185;
            @Pc(203) int local203;
            if (arg0 == 2) {
                for (local14 = 0; local14 < this.anInt5543; local14++) {
                    this.anIntArray410[local14] -= Static711.anInt10684;
                    this.anIntArray409[local14] -= Static564.anInt8464;
                    this.anIntArray412[local14] -= Static78.anInt1624;
                    if (arg3 != 0) {
                        local20 = Trig1.SIN[arg3];
                        local185 = Trig1.COS[arg3];
                        local203 = this.anIntArray410[local14] * local185 + local20 * this.anIntArray409[local14] + 16383 >> 14;
                        this.anIntArray409[local14] = local185 * this.anIntArray409[local14] + 16383 - this.anIntArray410[local14] * local20 >> 14;
                        this.anIntArray410[local14] = local203;
                    }
                    if (arg1 != 0) {
                        local20 = Trig1.SIN[arg1];
                        local185 = Trig1.COS[arg1];
                        local203 = this.anIntArray409[local14] * local185 + 16383 - local20 * this.anIntArray412[local14] >> 14;
                        this.anIntArray412[local14] = local185 * this.anIntArray412[local14] + this.anIntArray409[local14] * local20 + 16383 >> 14;
                        this.anIntArray409[local14] = local203;
                    }
                    if (arg2 != 0) {
                        local20 = Trig1.SIN[arg2];
                        local185 = Trig1.COS[arg2];
                        local203 = this.anIntArray410[local14] * local185 + local20 * this.anIntArray412[local14] + 16383 >> 14;
                        this.anIntArray412[local14] = this.anIntArray412[local14] * local185 + 16383 - local20 * this.anIntArray410[local14] >> 14;
                        this.anIntArray410[local14] = local203;
                    }
                    this.anIntArray410[local14] += Static711.anInt10684;
                    this.anIntArray409[local14] += Static564.anInt8464;
                    this.anIntArray412[local14] += Static78.anInt1624;
                }
            } else if (arg0 == 3) {
                for (local14 = 0; local14 < this.anInt5543; local14++) {
                    this.anIntArray410[local14] -= Static711.anInt10684;
                    this.anIntArray409[local14] -= Static564.anInt8464;
                    this.anIntArray412[local14] -= Static78.anInt1624;
                    this.anIntArray410[local14] = arg1 * this.anIntArray410[local14] / 128;
                    this.anIntArray409[local14] = this.anIntArray409[local14] * arg2 / 128;
                    this.anIntArray412[local14] = arg3 * this.anIntArray412[local14] / 128;
                    this.anIntArray410[local14] += Static711.anInt10684;
                    this.anIntArray409[local14] += Static564.anInt8464;
                    this.anIntArray412[local14] += Static78.anInt1624;
                }
            } else {
                @Pc(557) GlBillboardFace local557;
                @Pc(562) GlBillboardAttributes local562;
                if (arg0 == 5) {
                    for (local14 = 0; local14 < this.anInt5560; local14++) {
                        local20 = (this.aByteArray54[local14] & 0xFF) + (arg1 * 8);
                        if (local20 < 0) {
                            local20 = 0;
                        } else if (local20 > 255) {
                            local20 = 255;
                        }
                        this.aByteArray54[local14] = (byte) local20;
                    }
                    if (this.aClass94_6 != null) {
                        this.aClass94_6.anInterface12_2 = null;
                    }
                    if (this.billboardFaces != null) {
                        for (local20 = 0; local20 < this.billboardCount; local20++) {
                            local557 = this.billboardFaces[local20];
                            local562 = this.billboardAttributes[local20];
                            local562.colour = local562.colour & 0xFFFFFF | 255 - (this.aByteArray54[local557.face] & 0xFF) << 24;
                        }
                    }
                } else if (arg0 == 7) {
                    for (local14 = 0; local14 < this.anInt5560; local14++) {
                        local20 = this.aShortArray74[local14] & 0xFFFF;
                        local185 = local20 >> 10 & 0x3F;
                        local203 = local20 >> 7 & 0x7;
                        @Pc(626) int local626 = local185 + arg1 & 0x3F;
                        @Pc(630) int local630 = local20 & 0x7F;
                        local203 += arg2 / 4;
                        if (local203 < 0) {
                            local203 = 0;
                        } else if (local203 > 7) {
                            local203 = 7;
                        }
                        local630 += arg3;
                        if (local630 < 0) {
                            local630 = 0;
                        } else if (local630 > 127) {
                            local630 = 127;
                        }
                        this.aShortArray74[local14] = (short) (local630 | local626 << 10 | local203 << 7);
                    }
                    if (this.aClass94_6 != null) {
                        this.aClass94_6.anInterface12_2 = null;
                    }
                    if (this.billboardFaces != null) {
                        for (local20 = 0; local20 < this.billboardCount; local20++) {
                            local557 = this.billboardFaces[local20];
                            local562 = this.billboardAttributes[local20];
                            local562.colour = local562.colour & 0xFF000000 | ColourUtils.HSL_TO_RGB[this.aShortArray74[local557.face] & 0xFFFF] & 0xFFFFFF;
                        }
                    }
                } else {
                    @Pc(763) GlBillboardAttributes local763;
                    if (arg0 == 8) {
                        for (local14 = 0; local14 < this.billboardCount; local14++) {
                            local763 = this.billboardAttributes[local14];
                            local763.offsetX += arg1;
                            local763.offsetY += arg2;
                        }
                    } else if (arg0 == 10) {
                        for (local14 = 0; local14 < this.billboardCount; local14++) {
                            local763 = this.billboardAttributes[local14];
                            local763.scaleX = local763.scaleX * arg1 >> 7;
                            local763.scaleY = arg2 * local763.scaleY >> 7;
                        }
                    } else if (arg0 == 9) {
                        for (local14 = 0; local14 < this.billboardCount; local14++) {
                            local763 = this.billboardAttributes[local14];
                            local763.angle = arg1 + local763.angle & 0x3FFF;
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!kla", name = "G", descriptor = "()I")
    @Override
    public int G() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort69;
    }

    @OriginalMember(owner = "client!kla", name = "VA", descriptor = "(I)V")
    @Override
    public void VA(@OriginalArg(0) int arg0) {
        @Pc(9) int local9 = Trig1.SIN[arg0];
        @Pc(13) int local13 = Trig1.COS[arg0];
        for (@Pc(15) int local15 = 0; local15 < this.anInt5543; local15++) {
            @Pc(33) int local33 = local9 * this.anIntArray409[local15] + local13 * this.anIntArray410[local15] >> 14;
            this.anIntArray409[local15] = this.anIntArray409[local15] * local13 - this.anIntArray410[local15] * local9 >> 14;
            this.anIntArray410[local15] = local33;
        }
        this.aBoolean424 = false;
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "na", descriptor = "()I")
    @Override
    public int na() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort70;
    }

    @OriginalMember(owner = "client!kla", name = "I", descriptor = "(I[IIIIZI[I)V")
    @Override
    protected void I(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int[] arg7) {
        @Pc(8) int local8 = arg1.length;
        @Pc(24) int local24;
        @Pc(32) int local32;
        @Pc(62) int local62;
        @Pc(40) int local40;
        @Pc(56) int local56;
        @Pc(54) int[] local54;
        if (arg0 == 0) {
            arg3 <<= 0x4;
            arg2 <<= 0x4;
            arg4 <<= 0x4;
            local24 = 0;
            Static564.anInt8464 = 0;
            Static711.anInt10684 = 0;
            Static78.anInt1624 = 0;
            for (local32 = 0; local32 < local8; local32++) {
                local40 = arg1[local32];
                if (local40 < this.anIntArrayArray131.length) {
                    local54 = this.anIntArrayArray131[local40];
                    for (local56 = 0; local56 < local54.length; local56++) {
                        local62 = local54[local56];
                        if (this.aShortArray66 == null || (this.aShortArray66[local62] & arg6) != 0) {
                            Static711.anInt10684 += this.anIntArray410[local62];
                            Static564.anInt8464 += this.anIntArray409[local62];
                            local24++;
                            Static78.anInt1624 += this.anIntArray412[local62];
                        }
                    }
                }
            }
            if (local24 <= 0) {
                Static78.anInt1624 = arg4;
                Static711.anInt10684 = arg2;
                Static564.anInt8464 = arg3;
            } else {
                Static143.aBoolean315 = true;
                Static78.anInt1624 = Static78.anInt1624 / local24 + arg4;
                Static711.anInt10684 = Static711.anInt10684 / local24 + arg2;
                Static564.anInt8464 = arg3 + Static564.anInt8464 / local24;
            }
            return;
        }
        @Pc(273) int[] local273;
        @Pc(275) int local275;
        if (arg0 == 1) {
            if (arg7 != null) {
                local24 = arg7[1] * arg3 + arg2 * arg7[0] + arg7[2] * arg4 + 8192 >> 14;
                local32 = arg4 * arg7[5] + arg3 * arg7[4] + arg2 * arg7[3] + 8192 >> 14;
                local40 = arg4 * arg7[8] + arg2 * arg7[6] + arg3 * arg7[7] + 8192 >> 14;
                arg4 = local40;
                arg2 = local24;
                arg3 = local32;
            }
            arg2 <<= 0x4;
            arg4 <<= 0x4;
            arg3 <<= 0x4;
            for (local24 = 0; local24 < local8; local24++) {
                local32 = arg1[local24];
                if (this.anIntArrayArray131.length > local32) {
                    local273 = this.anIntArrayArray131[local32];
                    for (local275 = 0; local275 < local273.length; local275++) {
                        local56 = local273[local275];
                        if (this.aShortArray66 == null || (arg6 & this.aShortArray66[local56]) != 0) {
                            this.anIntArray410[local56] += arg2;
                            this.anIntArray409[local56] += arg3;
                            this.anIntArray412[local56] += arg4;
                        }
                    }
                }
            }
            return;
        }
        @Pc(401) int local401;
        @Pc(423) int local423;
        @Pc(455) int local455;
        @Pc(482) int local482;
        @Pc(486) int local486;
        @Pc(490) int local490;
        @Pc(494) int local494;
        @Pc(502) int local502;
        @Pc(510) int local510;
        @Pc(667) int local667;
        @Pc(693) int local693;
        @Pc(697) int local697;
        @Pc(705) int local705;
        @Pc(710) int local710;
        @Pc(714) int local714;
        @Pc(718) int local718;
        @Pc(720) int local720;
        @Pc(854) int[] local854;
        @Pc(856) int local856;
        @Pc(862) int local862;
        @Pc(866) int local866;
        @Pc(868) int local868;
        @Pc(1002) int local1002;
        if (arg0 == 2) {
            if (arg7 == null) {
                for (local24 = 0; local24 < local8; local24++) {
                    local32 = arg1[local24];
                    if (this.anIntArrayArray131.length > local32) {
                        local273 = this.anIntArrayArray131[local32];
                        for (local275 = 0; local275 < local273.length; local275++) {
                            local56 = local273[local275];
                            if (this.aShortArray66 == null || (arg6 & this.aShortArray66[local56]) != 0) {
                                this.anIntArray410[local56] -= Static711.anInt10684;
                                this.anIntArray409[local56] -= Static564.anInt8464;
                                this.anIntArray412[local56] -= Static78.anInt1624;
                                if (arg4 != 0) {
                                    local62 = Trig1.SIN[arg4];
                                    local401 = Trig1.COS[arg4];
                                    local423 = local401 * this.anIntArray410[local56] + local62 * this.anIntArray409[local56] + 16383 >> 14;
                                    this.anIntArray409[local56] = local401 * this.anIntArray409[local56] + 16383 - local62 * this.anIntArray410[local56] >> 14;
                                    this.anIntArray410[local56] = local423;
                                }
                                if (arg2 != 0) {
                                    local62 = Trig1.SIN[arg2];
                                    local401 = Trig1.COS[arg2];
                                    local423 = local401 * this.anIntArray409[local56] + 16383 - local62 * this.anIntArray412[local56] >> 14;
                                    this.anIntArray412[local56] = this.anIntArray412[local56] * local401 + local62 * this.anIntArray409[local56] + 16383 >> 14;
                                    this.anIntArray409[local56] = local423;
                                }
                                if (arg3 != 0) {
                                    local62 = Trig1.SIN[arg3];
                                    local401 = Trig1.COS[arg3];
                                    local423 = this.anIntArray410[local56] * local401 + local62 * this.anIntArray412[local56] + 16383 >> 14;
                                    this.anIntArray412[local56] = this.anIntArray412[local56] * local401 + 16383 - local62 * this.anIntArray410[local56] >> 14;
                                    this.anIntArray410[local56] = local423;
                                }
                                this.anIntArray410[local56] += Static711.anInt10684;
                                this.anIntArray409[local56] += Static564.anInt8464;
                                this.anIntArray412[local56] += Static78.anInt1624;
                            }
                        }
                    }
                }
                if (arg5) {
                    for (local32 = 0; local32 < local8; local32++) {
                        local40 = arg1[local32];
                        if (this.anIntArrayArray131.length > local40) {
                            local54 = this.anIntArrayArray131[local40];
                            for (local56 = 0; local56 < local54.length; local56++) {
                                local62 = local54[local56];
                                if (this.aShortArray66 == null || (this.aShortArray66[local62] & arg6) != 0) {
                                    local401 = this.anIntArray411[local62];
                                    local423 = this.anIntArray411[local62 + 1];
                                    for (local455 = local401; local455 < local423; local455++) {
                                        local482 = this.aShortArray70[local455] - 1;
                                        if (local482 == -1) {
                                            break;
                                        }
                                        if (arg4 != 0) {
                                            local486 = Trig1.SIN[arg4];
                                            local490 = Trig1.COS[arg4];
                                            local494 = this.aShortArray69[local482] * local490 + local486 * this.aShortArray73[local482] + 16383 >> 14;
                                            this.aShortArray73[local482] = (short) (local490 * this.aShortArray73[local482] + 16383 - local486 * this.aShortArray69[local482] >> 14);
                                            this.aShortArray69[local482] = (short) local494;
                                        }
                                        if (arg2 != 0) {
                                            local486 = Trig1.SIN[arg2];
                                            local490 = Trig1.COS[arg2];
                                            local494 = local490 * this.aShortArray73[local482] + 16383 - this.aShortArray72[local482] * local486 >> 14;
                                            this.aShortArray72[local482] = (short) (this.aShortArray72[local482] * local490 + this.aShortArray73[local482] * local486 + 16383 >> 14);
                                            this.aShortArray73[local482] = (short) local494;
                                        }
                                        if (arg3 != 0) {
                                            local486 = Trig1.SIN[arg3];
                                            local490 = Trig1.COS[arg3];
                                            local494 = this.aShortArray69[local482] * local490 + local486 * this.aShortArray72[local482] + 16383 >> 14;
                                            this.aShortArray72[local482] = (short) (this.aShortArray72[local482] * local490 + 16383 - local486 * this.aShortArray69[local482] >> 14);
                                            this.aShortArray69[local482] = (short) local494;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (this.aClass94_7 == null && this.aClass94_6 != null) {
                        this.aClass94_6.anInterface12_2 = null;
                    }
                    if (this.aClass94_7 != null) {
                        this.aClass94_7.anInterface12_2 = null;
                    }
                }
            } else {
                local24 = arg7[9] << 4;
                local32 = arg7[10] << 4;
                local40 = arg7[11] << 4;
                local275 = arg7[12] << 4;
                local56 = arg7[13] << 4;
                local62 = arg7[14] << 4;
                if (Static143.aBoolean315) {
                    local401 = Static711.anInt10684 * arg7[0] + Static564.anInt8464 * arg7[3] + Static78.anInt1624 * arg7[6] + 8192 >> 14;
                    local423 = arg7[7] * Static78.anInt1624 + arg7[4] * Static564.anInt8464 + arg7[1] * Static711.anInt10684 + 8192 >> 14;
                    local401 += local275;
                    local423 += local56;
                    local455 = Static711.anInt10684 * arg7[2] + Static564.anInt8464 * arg7[5] + arg7[8] * Static78.anInt1624 + 8192 >> 14;
                    local455 += local62;
                    Static711.anInt10684 = local401;
                    Static564.anInt8464 = local423;
                    Static78.anInt1624 = local455;
                    Static143.aBoolean315 = false;
                }
                @Pc(470) int[] local470 = new int[9];
                local423 = Trig1.COS[arg2];
                local455 = Trig1.SIN[arg2];
                local482 = Trig1.COS[arg3];
                local486 = Trig1.SIN[arg3];
                local490 = Trig1.COS[arg4];
                local494 = Trig1.SIN[arg4];
                local502 = local490 * local455 + 8192 >> 14;
                local510 = local494 * local455 + 8192 >> 14;
                local470[0] = local510 * local486 + local482 * local490 + 8192 >> 14;
                local470[4] = local490 * local423 + 8192 >> 14;
                local470[1] = local486 * local502 + local494 * -local482 + 8192 >> 14;
                local470[8] = local423 * local482 + 8192 >> 14;
                local470[2] = local423 * local486 + 8192 >> 14;
                local470[7] = local486 * local494 + local482 * local502 + 8192 >> 14;
                local470[6] = local490 * -local486 + local482 * local510 + 8192 >> 14;
                local470[3] = local494 * local423 + 8192 >> 14;
                local470[5] = -local455;
                @Pc(640) int local640 = local470[1] * -Static564.anInt8464 + -Static711.anInt10684 * local470[0] + -Static78.anInt1624 * local470[2] + 8192 >> 14;
                local667 = -Static711.anInt10684 * local470[3] + -Static564.anInt8464 * local470[4] + -Static78.anInt1624 * local470[5] + 8192 >> 14;
                local693 = local470[6] * -Static711.anInt10684 + -Static564.anInt8464 * local470[7] + -Static78.anInt1624 * local470[8] + 8192 >> 14;
                local697 = local640 + Static711.anInt10684;
                @Pc(701) int local701 = Static564.anInt8464 + local667;
                local705 = Static78.anInt1624 + local693;
                @Pc(708) int[] local708 = new int[9];
                for (local710 = 0; local710 < 3; local710++) {
                    for (local714 = 0; local714 < 3; local714++) {
                        local718 = 0;
                        for (local720 = 0; local720 < 3; local720++) {
                            local718 += arg7[local714 * 3 + local720] * local470[local720 + local710 * 3];
                        }
                        local708[local714 + local710 * 3] = local718 + 8192 >> 14;
                    }
                }
                local714 = local62 * local470[2] + local470[1] * local56 + local470[0] * local275 + 8192 >> 14;
                local718 = local470[4] * local56 + local275 * local470[3] + local62 * local470[5] + 8192 >> 14;
                local714 += local697;
                local720 = local470[6] * local275 + local470[7] * local56 + local62 * local470[8] + 8192 >> 14;
                local718 += local701;
                local720 += local705;
                local854 = new int[9];
                for (local856 = 0; local856 < 3; local856++) {
                    for (local862 = 0; local862 < 3; local862++) {
                        local866 = 0;
                        for (local868 = 0; local868 < 3; local868++) {
                            local866 += local708[local868 * 3 + local862] * arg7[local868 + local856 * 3];
                        }
                        local854[local862 + local856 * 3] = local866 + 8192 >> 14;
                    }
                }
                local862 = arg7[1] * local718 + arg7[0] * local714 + local720 * arg7[2] + 8192 >> 14;
                local866 = arg7[5] * local720 + arg7[3] * local714 + arg7[4] * local718 + 8192 >> 14;
                local868 = arg7[8] * local720 + local714 * arg7[6] + arg7[7] * local718 + 8192 >> 14;
                local862 += local24;
                local866 += local32;
                local868 += local40;
                for (local1002 = 0; local1002 < local8; local1002++) {
                    @Pc(1010) int local1010 = arg1[local1002];
                    if (this.anIntArrayArray131.length > local1010) {
                        @Pc(1024) int[] local1024 = this.anIntArrayArray131[local1010];
                        for (@Pc(1026) int local1026 = 0; local1026 < local1024.length; local1026++) {
                            @Pc(1032) int local1032 = local1024[local1026];
                            if (this.aShortArray66 == null || (this.aShortArray66[local1032] & arg6) != 0) {
                                @Pc(1078) int local1078 = this.anIntArray409[local1032] * local854[1] + local854[0] * this.anIntArray410[local1032] + this.anIntArray412[local1032] * local854[2] + 8192 >> 14;
                                @Pc(1109) int local1109 = local854[4] * this.anIntArray409[local1032] + local854[3] * this.anIntArray410[local1032] + this.anIntArray412[local1032] * local854[5] + 8192 >> 14;
                                @Pc(1113) int local1113 = local1109 + local866;
                                @Pc(1117) int local1117 = local1078 + local862;
                                @Pc(1148) int local1148 = this.anIntArray409[local1032] * local854[7] + local854[6] * this.anIntArray410[local1032] + local854[8] * this.anIntArray412[local1032] + 8192 >> 14;
                                this.anIntArray410[local1032] = local1117;
                                @Pc(1157) int local1157 = local1148 + local868;
                                this.anIntArray409[local1032] = local1113;
                                this.anIntArray412[local1032] = local1157;
                            }
                        }
                    }
                }
            }
        } else if (arg0 != 3) {
            @Pc(2692) GlBillboardFace local2692;
            @Pc(2697) GlBillboardAttributes local2697;
            if (arg0 == 5) {
                if (this.anIntArrayArray129 != null) {
                    for (local24 = 0; local24 < local8; local24++) {
                        local32 = arg1[local24];
                        if (this.anIntArrayArray129.length > local32) {
                            local273 = this.anIntArrayArray129[local32];
                            for (local275 = 0; local275 < local273.length; local275++) {
                                local56 = local273[local275];
                                if (this.aShortArray68 == null || (this.aShortArray68[local56] & arg6) != 0) {
                                    local62 = arg2 * 8 + (this.aByteArray54[local56] & 0xFF);
                                    if (local62 < 0) {
                                        local62 = 0;
                                    } else if (local62 > 255) {
                                        local62 = 255;
                                    }
                                    this.aByteArray54[local56] = (byte) local62;
                                    if (this.aClass94_6 != null) {
                                        this.aClass94_6.anInterface12_2 = null;
                                    }
                                }
                            }
                        }
                    }
                    if (this.billboardFaces != null) {
                        for (local32 = 0; local32 < this.billboardCount; local32++) {
                            local2692 = this.billboardFaces[local32];
                            local2697 = this.billboardAttributes[local32];
                            local2697.colour = 255 - (this.aByteArray54[local2692.face] & 0xFF) << 24 | local2697.colour & 0xFFFFFF;
                        }
                    }
                }
            } else if (arg0 != 7) {
                @Pc(2952) GlBillboardAttributes local2952;
                if (arg0 == 8) {
                    if (this.anIntArrayArray130 != null) {
                        for (local24 = 0; local24 < local8; local24++) {
                            local32 = arg1[local24];
                            if (this.anIntArrayArray130.length > local32) {
                                local273 = this.anIntArrayArray130[local32];
                                for (local275 = 0; local275 < local273.length; local275++) {
                                    local2952 = this.billboardAttributes[local273[local275]];
                                    local2952.offsetY += arg3;
                                    local2952.offsetX += arg2;
                                }
                            }
                        }
                    }
                } else if (arg0 == 10) {
                    if (this.anIntArrayArray130 != null) {
                        for (local24 = 0; local24 < local8; local24++) {
                            local32 = arg1[local24];
                            if (local32 < this.anIntArrayArray130.length) {
                                local273 = this.anIntArrayArray130[local32];
                                for (local275 = 0; local275 < local273.length; local275++) {
                                    local2952 = this.billboardAttributes[local273[local275]];
                                    local2952.scaleX = arg2 * local2952.scaleX >> 7;
                                    local2952.scaleY = arg3 * local2952.scaleY >> 7;
                                }
                            }
                        }
                    }
                } else if (arg0 == 9 && this.anIntArrayArray130 != null) {
                    for (local24 = 0; local24 < local8; local24++) {
                        local32 = arg1[local24];
                        if (local32 < this.anIntArrayArray130.length) {
                            local273 = this.anIntArrayArray130[local32];
                            for (local275 = 0; local275 < local273.length; local275++) {
                                local2952 = this.billboardAttributes[local273[local275]];
                                local2952.angle = local2952.angle + arg2 & 0x3FFF;
                            }
                        }
                    }
                }
            } else if (this.anIntArrayArray129 != null) {
                for (local24 = 0; local24 < local8; local24++) {
                    local32 = arg1[local24];
                    if (this.anIntArrayArray129.length > local32) {
                        local273 = this.anIntArrayArray129[local32];
                        for (local275 = 0; local275 < local273.length; local275++) {
                            local56 = local273[local275];
                            if (this.aShortArray68 == null || (this.aShortArray68[local56] & arg6) != 0) {
                                local62 = this.aShortArray74[local56] & 0xFFFF;
                                local401 = local62 >> 10 & 0x3F;
                                local423 = local62 >> 7 & 0x7;
                                local455 = local62 & 0x7F;
                                @Pc(2798) int local2798 = arg2 + local401 & 0x3F;
                                local423 += arg3 / 4;
                                if (local423 < 0) {
                                    local423 = 0;
                                } else if (local423 > 7) {
                                    local423 = 7;
                                }
                                local455 += arg4;
                                if (local455 < 0) {
                                    local455 = 0;
                                } else if (local455 > 127) {
                                    local455 = 127;
                                }
                                this.aShortArray74[local56] = (short) (local2798 << 10 | local423 << 7 | local455);
                                if (this.aClass94_6 != null) {
                                    this.aClass94_6.anInterface12_2 = null;
                                }
                            }
                        }
                    }
                }
                if (this.billboardFaces != null) {
                    for (local32 = 0; local32 < this.billboardCount; local32++) {
                        local2692 = this.billboardFaces[local32];
                        local2697 = this.billboardAttributes[local32];
                        local2697.colour = local2697.colour & 0xFF000000 | ColourUtils.HSL_TO_RGB[this.aShortArray74[local2692.face] & 0xFFFF] & 0xFFFFFF;
                    }
                }
            }
        } else if (arg7 == null) {
            for (local24 = 0; local24 < local8; local24++) {
                local32 = arg1[local24];
                if (local32 < this.anIntArrayArray131.length) {
                    local273 = this.anIntArrayArray131[local32];
                    for (local275 = 0; local275 < local273.length; local275++) {
                        local56 = local273[local275];
                        if (this.aShortArray66 == null || (arg6 & this.aShortArray66[local56]) != 0) {
                            this.anIntArray410[local56] -= Static711.anInt10684;
                            this.anIntArray409[local56] -= Static564.anInt8464;
                            this.anIntArray412[local56] -= Static78.anInt1624;
                            this.anIntArray410[local56] = arg2 * this.anIntArray410[local56] >> 7;
                            this.anIntArray409[local56] = this.anIntArray409[local56] * arg3 >> 7;
                            this.anIntArray412[local56] = arg4 * this.anIntArray412[local56] >> 7;
                            this.anIntArray410[local56] += Static711.anInt10684;
                            this.anIntArray409[local56] += Static564.anInt8464;
                            this.anIntArray412[local56] += Static78.anInt1624;
                        }
                    }
                }
            }
        } else {
            local24 = arg7[9] << 4;
            local32 = arg7[10] << 4;
            local40 = arg7[11] << 4;
            local275 = arg7[12] << 4;
            local56 = arg7[13] << 4;
            local62 = arg7[14] << 4;
            if (Static143.aBoolean315) {
                local401 = Static564.anInt8464 * arg7[3] + arg7[0] * Static711.anInt10684 + arg7[6] * Static78.anInt1624 + 8192 >> 14;
                local423 = arg7[7] * Static78.anInt1624 + arg7[1] * Static711.anInt10684 + arg7[4] * Static564.anInt8464 + 8192 >> 14;
                local455 = Static78.anInt1624 * arg7[8] + Static564.anInt8464 * arg7[5] + Static711.anInt10684 * arg7[2] + 8192 >> 14;
                local401 += local275;
                local423 += local56;
                Static564.anInt8464 = local423;
                local455 += local62;
                Static711.anInt10684 = local401;
                Static143.aBoolean315 = false;
                Static78.anInt1624 = local455;
            }
            local401 = arg2 << 15 >> 7;
            local423 = arg3 << 15 >> 7;
            local455 = arg4 << 15 >> 7;
            local482 = -Static711.anInt10684 * local401 + 8192 >> 14;
            local486 = local423 * -Static564.anInt8464 + 8192 >> 14;
            local490 = -Static78.anInt1624 * local455 + 8192 >> 14;
            local494 = local482 + Static711.anInt10684;
            local502 = Static564.anInt8464 + local486;
            local510 = Static78.anInt1624 + local490;
            @Pc(2092) int[] local2092 = new int[]{local401 * arg7[0] + 8192 >> 14, arg7[3] * local401 + 8192 >> 14, local401 * arg7[6] + 8192 >> 14, local423 * arg7[1] + 8192 >> 14, arg7[4] * local423 + 8192 >> 14, local423 * arg7[7] + 8192 >> 14, arg7[2] * local455 + 8192 >> 14, local455 * arg7[5] + 8192 >> 14, local455 * arg7[8] + 8192 >> 14};
            local667 = local401 * local275 + 8192 >> 14;
            local693 = local423 * local56 + 8192 >> 14;
            @Pc(2220) int local2220 = local667 + local494;
            local697 = local455 * local62 + 8192 >> 14;
            @Pc(2232) int local2232 = local693 + local502;
            @Pc(2236) int local2236 = local697 + local510;
            @Pc(2239) int[] local2239 = new int[9];
            @Pc(2245) int local2245;
            for (local705 = 0; local705 < 3; local705++) {
                for (local2245 = 0; local2245 < 3; local2245++) {
                    local710 = 0;
                    for (local714 = 0; local714 < 3; local714++) {
                        local710 += local2092[local2245 + local714 * 3] * arg7[local714 + local705 * 3];
                    }
                    local2239[local2245 + local705 * 3] = local710 + 8192 >> 14;
                }
            }
            local2245 = local2232 * arg7[1] + local2220 * arg7[0] + arg7[2] * local2236 + 8192 >> 14;
            local710 = local2236 * arg7[5] + arg7[4] * local2232 + local2220 * arg7[3] + 8192 >> 14;
            local2245 += local24;
            local714 = local2236 * arg7[8] + arg7[7] * local2232 + local2220 * arg7[6] + 8192 >> 14;
            local710 += local32;
            local714 += local40;
            for (local718 = 0; local718 < local8; local718++) {
                local720 = arg1[local718];
                if (local720 < this.anIntArrayArray131.length) {
                    local854 = this.anIntArrayArray131[local720];
                    for (local856 = 0; local856 < local854.length; local856++) {
                        local862 = local854[local856];
                        if (this.aShortArray66 == null || (arg6 & this.aShortArray66[local862]) != 0) {
                            local866 = this.anIntArray410[local862] * local2239[0] + local2239[1] * this.anIntArray409[local862] + local2239[2] * this.anIntArray412[local862] + 8192 >> 14;
                            local868 = local2239[3] * this.anIntArray410[local862] + this.anIntArray409[local862] * local2239[4] + this.anIntArray412[local862] * local2239[5] + 8192 >> 14;
                            @Pc(2497) int local2497 = local866 + local2245;
                            @Pc(2501) int local2501 = local868 + local710;
                            local1002 = local2239[6] * this.anIntArray410[local862] + this.anIntArray409[local862] * local2239[7] + this.anIntArray412[local862] * local2239[8] + 8192 >> 14;
                            this.anIntArray410[local862] = local2497;
                            @Pc(2543) int local2543 = local1002 + local714;
                            this.anIntArray409[local862] = local2501;
                            this.anIntArray412[local862] = local2543;
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "()Z")
    @Override
    public boolean loadedTextures() {
        if (this.aShortArray75 == null) {
            return true;
        }
        for (@Pc(13) int local13 = 0; local13 < this.aShortArray75.length; local13++) {
            if (this.aShortArray75[local13] != -1 && !this.aClass19_Sub3_24.textureSource.textureAvailable(this.aShortArray75[local13])) {
                return false;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!kla", name = "k", descriptor = "(I)V")
    @Override
    public void k(@OriginalArg(0) int arg0) {
        @Pc(9) int local9 = Trig1.SIN[arg0];
        @Pc(13) int local13 = Trig1.COS[arg0];
        @Pc(33) int local33;
        for (@Pc(15) int local15 = 0; local15 < this.anInt5543; local15++) {
            local33 = this.anIntArray410[local15] * local13 + this.anIntArray412[local15] * local9 >> 14;
            this.anIntArray412[local15] = local13 * this.anIntArray412[local15] - local9 * this.anIntArray410[local15] >> 14;
            this.anIntArray410[local15] = local33;
        }
        for (local33 = 0; local33 < this.anInt5538; local33++) {
            @Pc(91) int local91 = this.aShortArray72[local33] * local9 + local13 * this.aShortArray69[local33] >> 14;
            this.aShortArray72[local33] = (short) (local13 * this.aShortArray72[local33] - this.aShortArray69[local33] * local9 >> 14);
            this.aShortArray69[local33] = (short) local91;
        }
        if (this.aClass94_7 == null && this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
        }
        if (this.aClass94_7 != null) {
            this.aClass94_7.anInterface12_2 = null;
        }
        this.aBoolean424 = false;
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(IIII)V")
    @Override
    public void adjustColours(@OriginalArg(0) int hue, @OriginalArg(1) int saturation, @OriginalArg(2) int lightness, @OriginalArg(3) int scale) {
        @Pc(18) int local18;
        for (@Pc(7) int local7 = 0; local7 < this.anInt5560; local7++) {
            local18 = this.aShortArray74[local7] & 0xFFFF;
            @Pc(24) int local24 = local18 >> 10 & 0x3F;
            @Pc(30) int local30 = local18 >> 7 & 0x7;
            if (hue != -1) {
                local24 += (hue - local24) * scale >> 7;
            }
            @Pc(49) int local49 = local18 & 0x7F;
            if (saturation != -1) {
                local30 += (saturation - local30) * scale >> 7;
            }
            if (lightness != -1) {
                local49 += scale * (lightness - local49) >> 7;
            }
            this.aShortArray74[local7] = (short) (local24 << 10 | local30 << 7 | local49);
        }
        if (this.billboardFaces != null) {
            for (local18 = 0; local18 < this.billboardCount; local18++) {
                @Pc(116) GlBillboardFace local116 = this.billboardFaces[local18];
                @Pc(121) GlBillboardAttributes local121 = this.billboardAttributes[local18];
                local121.colour = local121.colour & 0xFF000000 | ColourUtils.HSL_TO_RGB[this.aShortArray74[local116.face] & 0xFFFF] & 0xFFFFFF;
            }
        }
        if (this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(ZB)V")
    public void method4991(@OriginalArg(0) boolean arg0) {
        @Pc(19) boolean local19 = this.aClass94_6 != null && this.aClass94_6.anInterface12_2 == null;
        @Pc(33) boolean local33 = this.aClass94_7 != null && this.aClass94_7.anInterface12_2 == null;
        @Pc(47) boolean local47 = this.aClass94_8 != null && this.aClass94_8.anInterface12_2 == null;
        @Pc(61) boolean local61 = this.aClass94_9 != null && this.aClass94_9.anInterface12_2 == null;
        if (arg0) {
            local33 &= (this.aByte89 & 0x4) != 0;
            local47 &= (this.aByte89 & 0x1) != 0;
            local19 &= (this.aByte89 & 0x2) != 0;
            local61 &= (this.aByte89 & 0x8) != 0;
        }
        @Pc(118) byte local118 = 0;
        @Pc(122) byte local122 = 0;
        @Pc(124) byte local124 = 0;
        @Pc(126) byte local126 = 0;
        if (local47) {
            local118 = 12;
        }
        if (local19) {
            local122 = local118;
            local118 = (byte) (local118 + 4);
        }
        if (local33) {
            local124 = local118;
            local118 = (byte) (local118 + 12);
        }
        if (local61) {
            local126 = local118;
            local118 = (byte) (local118 + 8);
        }
        if (local118 == 0) {
            return;
        }
        if (this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3.data.length >= local118 * this.anInt5538) {
            this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3.pos = 0;
        } else {
            this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3 = new Node_Sub21_Sub1(local118 * (this.anInt5538 + 100));
        }
        @Pc(203) Node_Sub21_Sub1 local203 = this.aClass19_Sub3_24.aClass2_Sub21_Sub1_3;
        @Pc(213) int local213;
        @Pc(222) int local222;
        @Pc(250) int local250;
        @Pc(259) int local259;
        if (local47) {
            @Pc(229) int local229;
            @Pc(236) int local236;
            @Pc(241) int local241;
            @Pc(248) int local248;
            if (this.aClass19_Sub3_24.aBoolean618) {
                for (local213 = 0; local213 < this.anInt5543; local213++) {
                    local222 = Stream.floatToRawIntBits((float) this.anIntArray410[local213]);
                    local229 = Stream.floatToRawIntBits((float) this.anIntArray409[local213]);
                    local236 = Stream.floatToRawIntBits((float) this.anIntArray412[local213]);
                    local241 = this.anIntArray411[local213];
                    local248 = this.anIntArray411[local213 + 1];
                    for (local250 = local241; local250 < local248; local250++) {
                        local259 = this.aShortArray70[local250] - 1;
                        if (local259 == -1) {
                            break;
                        }
                        local203.pos = local118 * local259;
                        local203.p4(local222);
                        local203.p4(local229);
                        local203.p4(local236);
                    }
                }
            } else {
                for (local213 = 0; local213 < this.anInt5543; local213++) {
                    local222 = Stream.floatToRawIntBits((float) this.anIntArray410[local213]);
                    local229 = Stream.floatToRawIntBits((float) this.anIntArray409[local213]);
                    local236 = Stream.floatToRawIntBits((float) this.anIntArray412[local213]);
                    local241 = this.anIntArray411[local213];
                    local248 = this.anIntArray411[local213 + 1];
                    for (local250 = local241; local250 < local248; local250++) {
                        local259 = this.aShortArray70[local250] - 1;
                        if (local259 == -1) {
                            break;
                        }
                        local203.pos = local259 * local118;
                        local203.ip4(local222);
                        local203.ip4(local229);
                        local203.ip4(local236);
                    }
                }
            }
        }
        @Pc(516) float local516;
        @Pc(410) short[] local410;
        @Pc(414) short[] local414;
        @Pc(418) short[] local418;
        @Pc(422) byte[] local422;
        @Pc(569) float local569;
        if (local19) {
            if (this.aClass94_7 == null) {
                if (this.aClass346_1 == null) {
                    local422 = this.aByteArray53;
                    local418 = this.aShortArray72;
                    local414 = this.aShortArray73;
                    local410 = this.aShortArray69;
                } else {
                    local410 = this.aClass346_1.aShortArray129;
                    local414 = this.aClass346_1.aShortArray130;
                    local418 = this.aClass346_1.aShortArray128;
                    local422 = this.aClass346_1.aByteArray99;
                }
                @Pc(442) float local442 = this.aClass19_Sub3_24.aFloatArray51[0];
                @Pc(448) float local448 = this.aClass19_Sub3_24.aFloatArray51[1];
                @Pc(454) float local454 = this.aClass19_Sub3_24.aFloatArray51[2];
                @Pc(458) float local458 = this.aClass19_Sub3_24.aFloat149;
                @Pc(468) float local468 = this.aClass19_Sub3_24.aFloat129 * 768.0F / (float) this.aShort67;
                @Pc(478) float local478 = this.aClass19_Sub3_24.aFloat130 * 768.0F / (float) this.aShort67;
                for (@Pc(480) int local480 = 0; local480 < this.anInt5560; local480++) {
                    @Pc(500) int local500 = this.method4985(this.aByteArray54[local480], this.aShortArray74[local480], this.aShort64, this.aShortArray75[local480]);
                    @Pc(505) short local505 = this.aShortArray67[local480];
                    local516 = (float) (local500 >> 16 & 0xFF) * this.aClass19_Sub3_24.aFloat143;
                    @Pc(527) float local527 = (float) (local500 >> 8 & 0xFF) * this.aClass19_Sub3_24.aFloat137;
                    @Pc(536) float local536 = (float) (local500 >>> 24) * this.aClass19_Sub3_24.aFloat148;
                    @Pc(541) short local541 = local422[local505];
                    if (local541 == 0) {
                        local569 = (local454 * (float) local418[local505] + local442 * (float) local410[local505] + local448 * (float) local414[local505]) * 0.0026041667F;
                    } else {
                        local569 = (local454 * (float) local418[local505] + local448 * (float) local414[local505] + (float) local410[local505] * local442) / (float) (local541 * 256);
                    }
                    @Pc(610) float local610 = (local569 < 0.0F ? local478 : local468) * local569 + local458;
                    @Pc(615) int local615 = (int) (local610 * local536);
                    if (local615 < 0) {
                        local615 = 0;
                    } else if (local615 > 255) {
                        local615 = 255;
                    }
                    @Pc(635) int local635 = (int) (local516 * local610);
                    if (local635 < 0) {
                        local635 = 0;
                    } else if (local635 > 255) {
                        local635 = 255;
                    }
                    @Pc(656) int local656 = (int) (local610 * local527);
                    if (local656 < 0) {
                        local656 = 0;
                    } else if (local656 > 255) {
                        local656 = 255;
                    }
                    local203.pos = local122 + local505 * local118;
                    local203.p1(local615);
                    local203.p1(local635);
                    local203.p1(local656);
                    local203.p1(255 - (this.aByteArray54[local480] & 0xFF));
                    local505 = this.aShortArray71[local480];
                    local541 = local422[local505];
                    if (local541 == 0) {
                        local569 = (local454 * (float) local418[local505] + (float) local414[local505] * local448 + (float) local410[local505] * local442) * 0.0026041667F;
                    } else {
                        local569 = (local442 * (float) local410[local505] + (float) local414[local505] * local448 + local454 * (float) local418[local505]) / (float) (local541 * 256);
                    }
                    local610 = local458 + (local569 < 0.0F ? local478 : local468) * local569;
                    local615 = (int) (local610 * local536);
                    if (local615 < 0) {
                        local615 = 0;
                    } else if (local615 > 255) {
                        local615 = 255;
                    }
                    local635 = (int) (local610 * local516);
                    local656 = (int) (local610 * local527);
                    if (local635 < 0) {
                        local635 = 0;
                    } else if (local635 > 255) {
                        local635 = 255;
                    }
                    local203.pos = local505 * local118 + local122;
                    if (local656 < 0) {
                        local656 = 0;
                    } else if (local656 > 255) {
                        local656 = 255;
                    }
                    local203.p1(local615);
                    local203.p1(local635);
                    local203.p1(local656);
                    local203.p1(255 - (this.aByteArray54[local480] & 0xFF));
                    local505 = this.aShortArray76[local480];
                    local541 = local422[local505];
                    if (local541 == 0) {
                        local569 = (local454 * (float) local418[local505] + (float) local410[local505] * local442 + local448 * (float) local414[local505]) * 0.0026041667F;
                    } else {
                        local569 = ((float) local410[local505] * local442 + local448 * (float) local414[local505] + local454 * (float) local418[local505]) / (float) (local541 * 256);
                    }
                    local610 = local458 + (local569 < 0.0F ? local478 : local468) * local569;
                    local615 = (int) (local610 * local536);
                    if (local615 < 0) {
                        local615 = 0;
                    } else if (local615 > 255) {
                        local615 = 255;
                    }
                    local635 = (int) (local610 * local516);
                    if (local635 < 0) {
                        local635 = 0;
                    } else if (local635 > 255) {
                        local635 = 255;
                    }
                    local656 = (int) (local527 * local610);
                    local203.pos = local118 * local505 + local122;
                    if (local656 < 0) {
                        local656 = 0;
                    } else if (local656 > 255) {
                        local656 = 255;
                    }
                    local203.p1(local615);
                    local203.p1(local635);
                    local203.p1(local656);
                    local203.p1(255 - (this.aByteArray54[local480] & 0xFF));
                }
            } else {
                for (local213 = 0; local213 < this.anInt5560; local213++) {
                    local222 = this.method4985(this.aByteArray54[local213], this.aShortArray74[local213], this.aShort64, this.aShortArray75[local213]);
                    local203.pos = local118 * this.aShortArray67[local213] + local122;
                    local203.p4(local222);
                    local203.pos = local118 * this.aShortArray71[local213] + local122;
                    local203.p4(local222);
                    local203.pos = this.aShortArray76[local213] * local118 + local122;
                    local203.p4(local222);
                }
            }
        }
        if (local33) {
            if (this.aClass346_1 == null) {
                local414 = this.aShortArray73;
                local410 = this.aShortArray69;
                local418 = this.aShortArray72;
                local422 = this.aByteArray53;
            } else {
                local422 = this.aClass346_1.aByteArray99;
                local414 = this.aClass346_1.aShortArray130;
                local418 = this.aClass346_1.aShortArray128;
                local410 = this.aClass346_1.aShortArray129;
            }
            @Pc(1163) float local1163 = 3.0F / (float) this.aShort67;
            local203.pos = local124;
            local569 = 3.0F / (float) (this.aShort67 / 2 + this.aShort67);
            if (this.aClass19_Sub3_24.aBoolean618) {
                for (local250 = 0; local250 < this.anInt5538; local250++) {
                    local259 = local422[local250] & 0xFF;
                    if (local259 == 0) {
                        local203.method4336((float) local410[local250] * local569);
                        local203.method4336(local569 * (float) local414[local250]);
                        local203.method4336(local569 * (float) local418[local250]);
                    } else {
                        local516 = local1163 / (float) local259;
                        local203.method4336(local516 * (float) local410[local250]);
                        local203.method4336(local516 * (float) local414[local250]);
                        local203.method4336((float) local418[local250] * local516);
                    }
                    local203.pos += local118 - 12;
                }
            } else {
                for (local250 = 0; local250 < this.anInt5538; local250++) {
                    local259 = local422[local250] & 0xFF;
                    if (local259 == 0) {
                        local203.method4337(local569 * (float) local410[local250]);
                        local203.method4337(local569 * (float) local414[local250]);
                        local203.method4337(local569 * (float) local418[local250]);
                    } else {
                        local516 = local1163 / (float) local259;
                        local203.method4337(local516 * (float) local410[local250]);
                        local203.method4337(local516 * (float) local414[local250]);
                        local203.method4337(local516 * (float) local418[local250]);
                    }
                    local203.pos += local118 - 12;
                }
            }
        }
        if (local61) {
            local203.pos = local126;
            if (this.aClass19_Sub3_24.aBoolean618) {
                for (local213 = 0; local213 < this.anInt5538; local213++) {
                    local203.method4336(this.aFloatArray37[local213]);
                    local203.method4336(this.aFloatArray39[local213]);
                    local203.pos += local118 - 8;
                }
            } else {
                for (local213 = 0; local213 < this.anInt5538; local213++) {
                    local203.method4337(this.aFloatArray37[local213]);
                    local203.method4337(this.aFloatArray39[local213]);
                    local203.pos += local118 - 8;
                }
            }
        }
        local203.pos = this.anInt5538 * local118;
        @Pc(1516) Interface12 local1516;
        if (arg0) {
            if (this.anInterface12_4 == null) {
                this.anInterface12_4 = this.aClass19_Sub3_24.method7024(true, local203.pos, local203.data, local118);
            } else {
                this.anInterface12_4.method5002(local203.data, local203.pos, local118);
            }
            local1516 = this.anInterface12_4;
            this.aByte89 = 0;
        } else {
            local1516 = this.aClass19_Sub3_24.method7024(false, local203.pos, local203.data, local118);
            this.aBoolean422 = true;
        }
        if (local47) {
            this.aClass94_8.anInterface12_2 = local1516;
            this.aClass94_8.aByte46 = 0;
        }
        if (local61) {
            this.aClass94_9.aByte46 = local126;
            this.aClass94_9.anInterface12_2 = local1516;
        }
        if (local19) {
            this.aClass94_6.aByte46 = local122;
            this.aClass94_6.anInterface12_2 = local1516;
        }
        if (local33) {
            this.aClass94_7.aByte46 = local124;
            this.aClass94_7.anInterface12_2 = local1516;
        }
    }

    @OriginalMember(owner = "client!kla", name = "f", descriptor = "()[Lclient!rv;")
    @Override
    public ModelParticleEmitter[] particleEmitters() {
        return this.aModelParticleEmitterArray4;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(B[B)V")
    @Override
    public void updateAlphas(@OriginalArg(0) byte arg0, @OriginalArg(1) byte[] arg1) {
        @Pc(5) int local5;
        if (arg1 == null) {
            for (local5 = 0; local5 < this.anInt5560; local5++) {
                this.aByteArray54[local5] = arg0;
            }
        } else {
            for (local5 = 0; local5 < this.anInt5560; local5++) {
                @Pc(28) int local28 = 255 - (255 - (arg0 & 0xFF)) * (-(arg1[local5] & 0xFF) + 255) / 255;
                this.aByteArray54[local5] = (byte) local28;
            }
        }
        if (this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(Lclient!kla;IZZILclient!kla;)Lclient!ka;")
    public Model method4992(@OriginalArg(0) Model_Sub2 arg0, @OriginalArg(2) boolean arg1, @OriginalArg(3) boolean arg2, @OriginalArg(4) int arg3, @OriginalArg(5) Model_Sub2 arg4) {
        arg4.anInt5520 = arg3;
        arg4.anInt5538 = this.anInt5538;
        arg4.anInt5506 = this.anInt5506;
        arg4.aShort64 = this.aShort64;
        arg4.anInt5529 = this.anInt5529;
        arg4.anInt5560 = this.anInt5560;
        arg4.aShort67 = this.aShort67;
        arg4.billboardCount = this.billboardCount;
        arg4.anInt5543 = this.anInt5543;
        arg4.aByte89 = 0;
        arg4.anInt5523 = this.anInt5523;
        arg4.aBoolean423 = this.aBoolean423;
        if ((arg3 & 0x100) == 0) {
            arg4.aBoolean421 = this.aBoolean421;
        } else {
            arg4.aBoolean421 = true;
        }
        @Pc(85) boolean local85 = Static646.method8457(this.anInt5529, arg3);
        @Pc(93) boolean local93 = Static58.method1257(this.anInt5529, arg3);
        @Pc(99) boolean local99 = Static340.method5011(arg3, this.anInt5529);
        @Pc(105) boolean local105 = local99 | local93 | local85;
        @Pc(258) int local258;
        if (local105) {
            if (!local85) {
                arg4.anIntArray410 = this.anIntArray410;
            } else if (arg0.anIntArray410 == null || arg0.anIntArray410.length < this.anInt5523) {
                arg4.anIntArray410 = arg0.anIntArray410 = new int[this.anInt5523];
            } else {
                arg4.anIntArray410 = arg0.anIntArray410;
            }
            if (!local93) {
                arg4.anIntArray409 = this.anIntArray409;
            } else if (arg0.anIntArray409 == null || this.anInt5523 > arg0.anIntArray409.length) {
                arg4.anIntArray409 = arg0.anIntArray409 = new int[this.anInt5523];
            } else {
                arg4.anIntArray409 = arg0.anIntArray409;
            }
            if (!local99) {
                arg4.anIntArray412 = this.anIntArray412;
            } else if (arg0.anIntArray412 == null || arg0.anIntArray412.length < this.anInt5523) {
                arg4.anIntArray412 = arg0.anIntArray412 = new int[this.anInt5523];
            } else {
                arg4.anIntArray412 = arg0.anIntArray412;
            }
            for (local258 = 0; local258 < this.anInt5523; local258++) {
                if (local85) {
                    arg4.anIntArray410[local258] = this.anIntArray410[local258];
                }
                if (local93) {
                    arg4.anIntArray409[local258] = this.anIntArray409[local258];
                }
                if (local99) {
                    arg4.anIntArray412[local258] = this.anIntArray412[local258];
                }
            }
        } else {
            arg4.anIntArray410 = this.anIntArray410;
            arg4.anIntArray409 = this.anIntArray409;
            arg4.anIntArray412 = this.anIntArray412;
        }
        if (Static598.method7828(arg3, this.anInt5529)) {
            arg4.aClass94_8 = arg0.aClass94_8;
            if (arg1) {
                arg4.aByte89 = (byte) (arg4.aByte89 | 0x1);
            }
            arg4.aClass94_8.anInterface12_2 = this.aClass94_8.anInterface12_2;
            arg4.aClass94_8.aByte46 = this.aClass94_8.aByte46;
        } else if (Static277.method4044(arg3, this.anInt5529)) {
            arg4.aClass94_8 = this.aClass94_8;
        } else {
            arg4.aClass94_8 = null;
        }
        if (Static54.method1183(this.anInt5529, arg3)) {
            if (arg0.aShortArray74 == null || this.anInt5560 > arg0.aShortArray74.length) {
                arg4.aShortArray74 = arg0.aShortArray74 = new short[this.anInt5560];
            } else {
                arg4.aShortArray74 = arg0.aShortArray74;
            }
            for (local258 = 0; local258 < this.anInt5560; local258++) {
                arg4.aShortArray74[local258] = this.aShortArray74[local258];
            }
        } else {
            arg4.aShortArray74 = this.aShortArray74;
        }
        if (Static194.method2908(arg3, this.anInt5529)) {
            if (arg0.aByteArray54 == null || arg0.aByteArray54.length < this.anInt5560) {
                arg4.aByteArray54 = arg0.aByteArray54 = new byte[this.anInt5560];
            } else {
                arg4.aByteArray54 = arg0.aByteArray54;
            }
            for (local258 = 0; local258 < this.anInt5560; local258++) {
                arg4.aByteArray54[local258] = this.aByteArray54[local258];
            }
        } else {
            arg4.aByteArray54 = this.aByteArray54;
        }
        if (Static198.method2957(arg3, this.anInt5529)) {
            arg4.aClass94_6 = arg0.aClass94_6;
            if (arg1) {
                arg4.aByte89 = (byte) (arg4.aByte89 | 0x2);
            }
            arg4.aClass94_6.anInterface12_2 = this.aClass94_6.anInterface12_2;
            arg4.aClass94_6.aByte46 = this.aClass94_6.aByte46;
        } else if (Static116.method2142(arg3, this.anInt5529)) {
            arg4.aClass94_6 = this.aClass94_6;
        } else {
            arg4.aClass94_6 = null;
        }
        @Pc(689) int local689;
        if (Static139.method2358(arg3, this.anInt5529)) {
            if (arg0.aShortArray69 == null || this.anInt5538 > arg0.aShortArray69.length) {
                local258 = this.anInt5538;
                arg4.aShortArray69 = arg0.aShortArray69 = new short[local258];
                arg4.aShortArray73 = arg0.aShortArray73 = new short[local258];
                arg4.aShortArray72 = arg0.aShortArray72 = new short[local258];
            } else {
                arg4.aShortArray73 = arg0.aShortArray73;
                arg4.aShortArray72 = arg0.aShortArray72;
                arg4.aShortArray69 = arg0.aShortArray69;
            }
            if (this.aClass346_1 == null) {
                for (local258 = 0; local258 < this.anInt5538; local258++) {
                    arg4.aShortArray69[local258] = this.aShortArray69[local258];
                    arg4.aShortArray73[local258] = this.aShortArray73[local258];
                    arg4.aShortArray72[local258] = this.aShortArray72[local258];
                }
            } else {
                if (arg0.aClass346_1 == null) {
                    arg0.aClass346_1 = new Class346();
                }
                @Pc(671) Class346 local671 = arg4.aClass346_1 = arg0.aClass346_1;
                if (local671.aShortArray129 == null || this.anInt5538 > local671.aShortArray129.length) {
                    local689 = this.anInt5538;
                    local671.aShortArray128 = new short[local689];
                    local671.aShortArray129 = new short[local689];
                    local671.aShortArray130 = new short[local689];
                    local671.aByteArray99 = new byte[local689];
                }
                for (local689 = 0; local689 < this.anInt5538; local689++) {
                    arg4.aShortArray69[local689] = this.aShortArray69[local689];
                    arg4.aShortArray73[local689] = this.aShortArray73[local689];
                    arg4.aShortArray72[local689] = this.aShortArray72[local689];
                    local671.aShortArray129[local689] = this.aClass346_1.aShortArray129[local689];
                    local671.aShortArray130[local689] = this.aClass346_1.aShortArray130[local689];
                    local671.aShortArray128[local689] = this.aClass346_1.aShortArray128[local689];
                    local671.aByteArray99[local689] = this.aClass346_1.aByteArray99[local689];
                }
            }
            arg4.aByteArray53 = this.aByteArray53;
        } else {
            arg4.aShortArray73 = this.aShortArray73;
            arg4.aClass346_1 = this.aClass346_1;
            arg4.aByteArray53 = this.aByteArray53;
            arg4.aShortArray69 = this.aShortArray69;
            arg4.aShortArray72 = this.aShortArray72;
        }
        if (Static293.method4333(arg3, this.anInt5529)) {
            arg4.aClass94_7 = arg0.aClass94_7;
            if (arg1) {
                arg4.aByte89 = (byte) (arg4.aByte89 | 0x4);
            }
            arg4.aClass94_7.anInterface12_2 = this.aClass94_7.anInterface12_2;
            arg4.aClass94_7.aByte46 = this.aClass94_7.aByte46;
        } else if (Static279.method4074(this.anInt5529, arg3)) {
            arg4.aClass94_7 = this.aClass94_7;
        } else {
            arg4.aClass94_7 = null;
        }
        if (Static710.method6713(this.anInt5529, arg3)) {
            if (arg0.aFloatArray37 == null || this.anInt5560 > arg0.aFloatArray37.length) {
                local258 = this.anInt5538;
                arg4.aFloatArray37 = arg0.aFloatArray37 = new float[local258];
                arg4.aFloatArray39 = arg0.aFloatArray39 = new float[local258];
            } else {
                arg4.aFloatArray37 = arg0.aFloatArray37;
                arg4.aFloatArray39 = arg0.aFloatArray39;
            }
            for (local258 = 0; local258 < this.anInt5538; local258++) {
                arg4.aFloatArray37[local258] = this.aFloatArray37[local258];
                arg4.aFloatArray39[local258] = this.aFloatArray39[local258];
            }
        } else {
            arg4.aFloatArray37 = this.aFloatArray37;
            arg4.aFloatArray39 = this.aFloatArray39;
        }
        if (Static286.method4110(this.anInt5529, arg3)) {
            arg4.aClass94_9 = arg0.aClass94_9;
            if (arg1) {
                arg4.aByte89 = (byte) (arg4.aByte89 | 0x8);
            }
            arg4.aClass94_9.aByte46 = this.aClass94_9.aByte46;
            arg4.aClass94_9.anInterface12_2 = this.aClass94_9.anInterface12_2;
        } else if (Static240.method3483(arg3, this.anInt5529)) {
            arg4.aClass94_9 = this.aClass94_9;
        } else {
            arg4.aClass94_9 = null;
        }
        if (Static483.method6488(this.anInt5529, arg3)) {
            if (arg0.aShortArray67 == null || this.anInt5560 > arg0.aShortArray67.length) {
                local258 = this.anInt5560;
                arg4.aShortArray71 = arg0.aShortArray71 = new short[local258];
                arg4.aShortArray76 = arg0.aShortArray76 = new short[local258];
                arg4.aShortArray67 = arg0.aShortArray67 = new short[local258];
            } else {
                arg4.aShortArray76 = arg0.aShortArray76;
                arg4.aShortArray71 = arg0.aShortArray71;
                arg4.aShortArray67 = arg0.aShortArray67;
            }
            for (local258 = 0; local258 < this.anInt5560; local258++) {
                arg4.aShortArray67[local258] = this.aShortArray67[local258];
                arg4.aShortArray71[local258] = this.aShortArray71[local258];
                arg4.aShortArray76[local258] = this.aShortArray76[local258];
            }
        } else {
            arg4.aShortArray67 = this.aShortArray67;
            arg4.aShortArray76 = this.aShortArray76;
            arg4.aShortArray71 = this.aShortArray71;
        }
        if (Static319.method4594(arg3, this.anInt5529)) {
            if (arg1) {
                arg4.aByte89 = (byte) (arg4.aByte89 | 0x10);
            }
            arg4.aClass136_1 = arg0.aClass136_1;
            arg4.aClass136_1.anInterface1_1 = this.aClass136_1.anInterface1_1;
        } else if (Static340.method5012(this.anInt5529, arg3)) {
            arg4.aClass136_1 = this.aClass136_1;
        } else {
            arg4.aClass136_1 = null;
        }
        if (Static558.method2895(arg3, this.anInt5529)) {
            if (arg0.aShortArray75 == null || arg0.aShortArray75.length < this.anInt5560) {
                local258 = this.anInt5560;
                arg4.aShortArray75 = arg0.aShortArray75 = new short[local258];
            } else {
                arg4.aShortArray75 = arg0.aShortArray75;
            }
            for (local258 = 0; local258 < this.anInt5560; local258++) {
                arg4.aShortArray75[local258] = this.aShortArray75[local258];
            }
        } else {
            arg4.aShortArray75 = this.aShortArray75;
        }
        if (!Static137.method2350(this.anInt5529, arg3)) {
            arg4.billboardAttributes = this.billboardAttributes;
        } else if (arg0.billboardAttributes == null || this.billboardCount > arg0.billboardAttributes.length) {
            local258 = this.billboardCount;
            arg4.billboardAttributes = arg0.billboardAttributes = new GlBillboardAttributes[local258];
            for (local689 = 0; local689 < this.billboardCount; local689++) {
                arg4.billboardAttributes[local689] = this.billboardAttributes[local689].copy();
            }
        } else {
            arg4.billboardAttributes = arg0.billboardAttributes;
            for (local258 = 0; local258 < this.billboardCount; local258++) {
                arg4.billboardAttributes[local258].set(this.billboardAttributes[local258]);
            }
        }
        arg4.anIntArrayArray131 = this.anIntArrayArray131;
        arg4.aShortArray68 = this.aShortArray68;
        arg4.anIntArrayArray129 = this.anIntArrayArray129;
        arg4.anIntArrayArray130 = this.anIntArrayArray130;
        arg4.aModelParticleEffectorArray4 = this.aModelParticleEffectorArray4;
        arg4.anIntArray413 = this.anIntArray413;
        arg4.anIntArray411 = this.anIntArray411;
        if (this.aBoolean424) {
            arg4.aShort69 = this.aShort69;
            arg4.aShort61 = this.aShort61;
            arg4.aShort70 = this.aShort70;
            arg4.aShort65 = this.aShort65;
            arg4.aBoolean424 = true;
            arg4.aShort68 = this.aShort68;
            arg4.aShort63 = this.aShort63;
            arg4.aShort66 = this.aShort66;
            arg4.aShort62 = this.aShort62;
        } else {
            arg4.aBoolean424 = false;
        }
        arg4.aModelParticleEmitterArray4 = this.aModelParticleEmitterArray4;
        arg4.aShortArray66 = this.aShortArray66;
        arg4.billboardFaces = this.billboardFaces;
        arg4.aShortArray70 = this.aShortArray70;
        return arg4;
    }

    @OriginalMember(owner = "client!kla", name = "ia", descriptor = "(SS)V")
    @Override
    public void ia(@OriginalArg(0) short src, @OriginalArg(1) short dest) {
        for (@Pc(3) int local3 = 0; local3 < this.anInt5560; local3++) {
            if (src == this.aShortArray74[local3]) {
                this.aShortArray74[local3] = dest;
            }
        }
        if (this.billboardFaces != null) {
            for (@Pc(42) int local42 = 0; local42 < this.billboardCount; local42++) {
                @Pc(49) GlBillboardFace local49 = this.billboardFaces[local42];
                @Pc(54) GlBillboardAttributes local54 = this.billboardAttributes[local42];
                local54.colour = ColourUtils.HSL_TO_RGB[this.aShortArray74[local49.face] & 0xFFFF] & 0xFFFFFF | local54.colour & 0xFF000000;
            }
        }
        if (this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(I[IIIIIZ)V")
    @Override
    protected void method7499(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) boolean arg6) {
        @Pc(8) int local8 = arg1.length;
        @Pc(27) int local27;
        @Pc(35) int local35;
        @Pc(61) int local61;
        @Pc(21) int local21;
        @Pc(17) int local17;
        @Pc(25) int local25;
        @Pc(43) int local43;
        @Pc(55) int local55;
        @Pc(53) int[] local53;
        if (arg0 == 0) {
            local17 = arg3 << 4;
            local21 = arg2 << 4;
            local25 = arg4 << 4;
            local27 = 0;
            Static78.anInt1624 = 0;
            Static711.anInt10684 = 0;
            Static564.anInt8464 = 0;
            for (local35 = 0; local35 < local8; local35++) {
                local43 = arg1[local35];
                if (this.anIntArrayArray131.length > local43) {
                    local53 = this.anIntArrayArray131[local43];
                    for (local55 = 0; local55 < local53.length; local55++) {
                        local61 = local53[local55];
                        Static711.anInt10684 += this.anIntArray410[local61];
                        Static564.anInt8464 += this.anIntArray409[local61];
                        local27++;
                        Static78.anInt1624 += this.anIntArray412[local61];
                    }
                }
            }
            if (local27 > 0) {
                Static564.anInt8464 = local17 + Static564.anInt8464 / local27;
                Static78.anInt1624 = local25 + Static78.anInt1624 / local27;
                Static711.anInt10684 = local21 + Static711.anInt10684 / local27;
            } else {
                Static78.anInt1624 = local25;
                Static711.anInt10684 = local21;
                Static564.anInt8464 = local17;
            }
            return;
        }
        @Pc(165) int[] local165;
        @Pc(167) int local167;
        if (arg0 == 1) {
            local25 = arg4 << 4;
            local17 = arg3 << 4;
            local21 = arg2 << 4;
            for (local27 = 0; local27 < local8; local27++) {
                local35 = arg1[local27];
                if (local35 < this.anIntArrayArray131.length) {
                    local165 = this.anIntArrayArray131[local35];
                    for (local167 = 0; local167 < local165.length; local167++) {
                        local55 = local165[local167];
                        this.anIntArray410[local55] += local21;
                        this.anIntArray409[local55] += local17;
                        this.anIntArray412[local55] += local25;
                    }
                }
            }
            return;
        }
        @Pc(286) int local286;
        @Pc(304) int local304;
        @Pc(771) int local771;
        if (arg0 == 2) {
            for (local27 = 0; local27 < local8; local27++) {
                local35 = arg1[local27];
                if (local35 < this.anIntArrayArray131.length) {
                    local165 = this.anIntArrayArray131[local35];
                    if ((arg5 & 0x1) == 0) {
                        for (local167 = 0; local167 < local165.length; local167++) {
                            local55 = local165[local167];
                            this.anIntArray410[local55] -= Static711.anInt10684;
                            this.anIntArray409[local55] -= Static564.anInt8464;
                            this.anIntArray412[local55] -= Static78.anInt1624;
                            if (arg4 != 0) {
                                local61 = Trig1.SIN[arg4];
                                local286 = Trig1.COS[arg4];
                                local304 = this.anIntArray410[local55] * local286 + this.anIntArray409[local55] * local61 + 16383 >> 14;
                                this.anIntArray409[local55] = this.anIntArray409[local55] * local286 + 16383 - this.anIntArray410[local55] * local61 >> 14;
                                this.anIntArray410[local55] = local304;
                            }
                            if (arg2 != 0) {
                                local61 = Trig1.SIN[arg2];
                                local286 = Trig1.COS[arg2];
                                local304 = local286 * this.anIntArray409[local55] + 16383 - this.anIntArray412[local55] * local61 >> 14;
                                this.anIntArray412[local55] = local61 * this.anIntArray409[local55] + local286 * this.anIntArray412[local55] + 16383 >> 14;
                                this.anIntArray409[local55] = local304;
                            }
                            if (arg3 != 0) {
                                local61 = Trig1.SIN[arg3];
                                local286 = Trig1.COS[arg3];
                                local304 = local286 * this.anIntArray410[local55] + local61 * this.anIntArray412[local55] + 16383 >> 14;
                                this.anIntArray412[local55] = this.anIntArray412[local55] * local286 + 16383 - local61 * this.anIntArray410[local55] >> 14;
                                this.anIntArray410[local55] = local304;
                            }
                            this.anIntArray410[local55] += Static711.anInt10684;
                            this.anIntArray409[local55] += Static564.anInt8464;
                            this.anIntArray412[local55] += Static78.anInt1624;
                        }
                    } else {
                        for (local167 = 0; local167 < local165.length; local167++) {
                            local55 = local165[local167];
                            this.anIntArray410[local55] -= Static711.anInt10684;
                            this.anIntArray409[local55] -= Static564.anInt8464;
                            this.anIntArray412[local55] -= Static78.anInt1624;
                            if (arg2 != 0) {
                                local61 = Trig1.SIN[arg2];
                                local286 = Trig1.COS[arg2];
                                local304 = this.anIntArray409[local55] * local286 + 16383 - local61 * this.anIntArray412[local55] >> 14;
                                this.anIntArray412[local55] = this.anIntArray412[local55] * local286 + this.anIntArray409[local55] * local61 + 16383 >> 14;
                                this.anIntArray409[local55] = local304;
                            }
                            if (arg4 != 0) {
                                local61 = Trig1.SIN[arg4];
                                local286 = Trig1.COS[arg4];
                                local304 = local286 * this.anIntArray410[local55] + this.anIntArray409[local55] * local61 + 16383 >> 14;
                                this.anIntArray409[local55] = this.anIntArray409[local55] * local286 + 16383 - local61 * this.anIntArray410[local55] >> 14;
                                this.anIntArray410[local55] = local304;
                            }
                            if (arg3 != 0) {
                                local61 = Trig1.SIN[arg3];
                                local286 = Trig1.COS[arg3];
                                local304 = this.anIntArray412[local55] * local61 + local286 * this.anIntArray410[local55] + 16383 >> 14;
                                this.anIntArray412[local55] = local286 * this.anIntArray412[local55] + 16383 - local61 * this.anIntArray410[local55] >> 14;
                                this.anIntArray410[local55] = local304;
                            }
                            this.anIntArray410[local55] += Static711.anInt10684;
                            this.anIntArray409[local55] += Static564.anInt8464;
                            this.anIntArray412[local55] += Static78.anInt1624;
                        }
                    }
                }
            }
            if (arg6) {
                for (local35 = 0; local35 < local8; local35++) {
                    local43 = arg1[local35];
                    if (this.anIntArrayArray131.length > local43) {
                        local53 = this.anIntArrayArray131[local43];
                        for (local55 = 0; local55 < local53.length; local55++) {
                            local61 = local53[local55];
                            local286 = this.anIntArray411[local61];
                            local304 = this.anIntArray411[local61 + 1];
                            for (local771 = local286; local771 < local304; local771++) {
                                @Pc(780) int local780 = this.aShortArray70[local771] - 1;
                                if (local780 == -1) {
                                    break;
                                }
                                @Pc(795) int local795;
                                @Pc(799) int local799;
                                @Pc(818) int local818;
                                if (arg4 != 0) {
                                    local795 = Trig1.SIN[arg4];
                                    local799 = Trig1.COS[arg4];
                                    local818 = local795 * this.aShortArray73[local780] + this.aShortArray69[local780] * local799 + 16383 >> 14;
                                    this.aShortArray73[local780] = (short) (this.aShortArray73[local780] * local799 + 16383 - this.aShortArray69[local780] * local795 >> 14);
                                    this.aShortArray69[local780] = (short) local818;
                                }
                                if (arg2 != 0) {
                                    local795 = Trig1.SIN[arg2];
                                    local799 = Trig1.COS[arg2];
                                    local818 = local799 * this.aShortArray73[local780] + 16383 - local795 * this.aShortArray72[local780] >> 14;
                                    this.aShortArray72[local780] = (short) (this.aShortArray72[local780] * local799 + this.aShortArray73[local780] * local795 + 16383 >> 14);
                                    this.aShortArray73[local780] = (short) local818;
                                }
                                if (arg3 != 0) {
                                    local795 = Trig1.SIN[arg3];
                                    local799 = Trig1.COS[arg3];
                                    local818 = local799 * this.aShortArray69[local780] + this.aShortArray72[local780] * local795 + 16383 >> 14;
                                    this.aShortArray72[local780] = (short) (this.aShortArray72[local780] * local799 + 16383 - local795 * this.aShortArray69[local780] >> 14);
                                    this.aShortArray69[local780] = (short) local818;
                                }
                            }
                        }
                    }
                }
                if (this.aClass94_7 == null && this.aClass94_6 != null) {
                    this.aClass94_6.anInterface12_2 = null;
                }
                if (this.aClass94_7 != null) {
                    this.aClass94_7.anInterface12_2 = null;
                }
            }
        } else if (arg0 == 3) {
            for (local27 = 0; local27 < local8; local27++) {
                local35 = arg1[local27];
                if (this.anIntArrayArray131.length > local35) {
                    local165 = this.anIntArrayArray131[local35];
                    for (local167 = 0; local167 < local165.length; local167++) {
                        local55 = local165[local167];
                        this.anIntArray410[local55] -= Static711.anInt10684;
                        this.anIntArray409[local55] -= Static564.anInt8464;
                        this.anIntArray412[local55] -= Static78.anInt1624;
                        this.anIntArray410[local55] = this.anIntArray410[local55] * arg2 >> 7;
                        this.anIntArray409[local55] = arg3 * this.anIntArray409[local55] >> 7;
                        this.anIntArray412[local55] = this.anIntArray412[local55] * arg4 >> 7;
                        this.anIntArray410[local55] += Static711.anInt10684;
                        this.anIntArray409[local55] += Static564.anInt8464;
                        this.anIntArray412[local55] += Static78.anInt1624;
                    }
                }
            }
        } else {
            @Pc(1268) GlBillboardFace local1268;
            @Pc(1273) GlBillboardAttributes local1273;
            if (arg0 == 5) {
                if (this.anIntArrayArray129 != null) {
                    for (local27 = 0; local27 < local8; local27++) {
                        local35 = arg1[local27];
                        if (local35 < this.anIntArrayArray129.length) {
                            local165 = this.anIntArrayArray129[local35];
                            for (local167 = 0; local167 < local165.length; local167++) {
                                local55 = local165[local167];
                                local61 = arg2 * 8 + (this.aByteArray54[local55] & 0xFF);
                                if (local61 < 0) {
                                    local61 = 0;
                                } else if (local61 > 255) {
                                    local61 = 255;
                                }
                                this.aByteArray54[local55] = (byte) local61;
                            }
                            if (local165.length > 0 && this.aClass94_6 != null) {
                                this.aClass94_6.anInterface12_2 = null;
                            }
                        }
                    }
                    if (this.billboardFaces != null) {
                        for (local35 = 0; local35 < this.billboardCount; local35++) {
                            local1268 = this.billboardFaces[local35];
                            local1273 = this.billboardAttributes[local35];
                            local1273.colour = local1273.colour & 0xFFFFFF | 255 - (this.aByteArray54[local1268.face] & 0xFF) << 24;
                        }
                    }
                }
            } else if (arg0 != 7) {
                @Pc(1546) GlBillboardAttributes local1546;
                if (arg0 == 8) {
                    if (this.anIntArrayArray130 != null) {
                        for (local27 = 0; local27 < local8; local27++) {
                            local35 = arg1[local27];
                            if (local35 < this.anIntArrayArray130.length) {
                                local165 = this.anIntArrayArray130[local35];
                                for (local167 = 0; local167 < local165.length; local167++) {
                                    local1546 = this.billboardAttributes[local165[local167]];
                                    local1546.offsetY += arg3;
                                    local1546.offsetX += arg2;
                                }
                            }
                        }
                    }
                } else if (arg0 == 10) {
                    if (this.anIntArrayArray130 != null) {
                        for (local27 = 0; local27 < local8; local27++) {
                            local35 = arg1[local27];
                            if (local35 < this.anIntArrayArray130.length) {
                                local165 = this.anIntArrayArray130[local35];
                                for (local167 = 0; local167 < local165.length; local167++) {
                                    local1546 = this.billboardAttributes[local165[local167]];
                                    local1546.scaleY = local1546.scaleY * arg3 >> 7;
                                    local1546.scaleX = arg2 * local1546.scaleX >> 7;
                                }
                            }
                        }
                    }
                } else if (arg0 == 9 && this.anIntArrayArray130 != null) {
                    for (local27 = 0; local27 < local8; local27++) {
                        local35 = arg1[local27];
                        if (this.anIntArrayArray130.length > local35) {
                            local165 = this.anIntArrayArray130[local35];
                            for (local167 = 0; local167 < local165.length; local167++) {
                                local1546 = this.billboardAttributes[local165[local167]];
                                local1546.angle = local1546.angle + arg2 & 0x3FFF;
                            }
                        }
                    }
                }
            } else if (this.anIntArrayArray129 != null) {
                for (local27 = 0; local27 < local8; local27++) {
                    local35 = arg1[local27];
                    if (local35 < this.anIntArrayArray129.length) {
                        local165 = this.anIntArrayArray129[local35];
                        for (local167 = 0; local167 < local165.length; local167++) {
                            local55 = local165[local167];
                            local61 = this.aShortArray74[local55] & 0xFFFF;
                            local286 = local61 >> 10 & 0x3F;
                            local304 = local61 >> 7 & 0x7;
                            @Pc(1364) int local1364 = arg2 + local286 & 0x3F;
                            local304 += arg3 / 4;
                            local771 = local61 & 0x7F;
                            local771 += arg4;
                            if (local304 < 0) {
                                local304 = 0;
                            } else if (local304 > 7) {
                                local304 = 7;
                            }
                            if (local771 < 0) {
                                local771 = 0;
                            } else if (local771 > 127) {
                                local771 = 127;
                            }
                            this.aShortArray74[local55] = (short) (local1364 << 10 | local304 << 7 | local771);
                        }
                        if (local165.length > 0 && this.aClass94_6 != null) {
                            this.aClass94_6.anInterface12_2 = null;
                        }
                    }
                }
                if (this.billboardFaces != null) {
                    for (local35 = 0; local35 < this.billboardCount; local35++) {
                        local1268 = this.billboardFaces[local35];
                        local1273 = this.billboardAttributes[local35];
                        local1273.colour = local1273.colour & 0xFF000000 | ColourUtils.HSL_TO_RGB[this.aShortArray74[local1268.face] & 0xFFFF] & 0xFFFFFF;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!kla", name = "p", descriptor = "(IILclient!s;Lclient!s;III)V")
    @Override
    public void p(@OriginalArg(0) int hillType, @OriginalArg(1) int hillValue, @OriginalArg(2) Ground floor, @OriginalArg(3) Ground ceiling, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z) {
        if (!this.aBoolean424) {
            this.method4982();
        }
        @Pc(18) int local18 = this.aShort66 + x;
        @Pc(24) int local24 = x + this.aShort62;
        @Pc(29) int local29 = this.aShort61 + z;
        @Pc(34) int local34 = this.aShort69 + z;
        if ((hillType == 1 || hillType == 2 || hillType == 3 || hillType == 5) && (local18 < 0 || floor.sizeX <= floor.tileSize + local24 >> floor.tileSizeShift || local29 < 0 || local34 + floor.tileSize >> floor.tileSizeShift >= floor.sizeZ)) {
            return;
        }
        if (hillType == 4 || hillType == 5) {
            if (ceiling == null) {
                return;
            }
            if (local18 < 0 || ceiling.sizeX <= local24 + ceiling.tileSize >> ceiling.tileSizeShift || local29 < 0 || ceiling.sizeZ <= ceiling.tileSize + local34 >> ceiling.tileSizeShift) {
                return;
            }
        } else {
            local18 >>= floor.tileSizeShift;
            local24 = local24 + floor.tileSize - 1 >> floor.tileSizeShift;
            local29 >>= floor.tileSizeShift;
            local34 = floor.tileSize + local34 - 1 >> floor.tileSizeShift;
            if (floor.getHeight(local18, local29) == y && y == floor.getHeight(local24, local29) && floor.getHeight(local18, local34) == y && y == floor.getHeight(local24, local34)) {
                return;
            }
        }
        @Pc(314) int local314;
        if (hillType == 1) {
            for (local314 = 0; local314 < this.anInt5543; local314++) {
                this.anIntArray409[local314] = this.anIntArray409[local314] + floor.averageHeight(x + this.anIntArray410[local314], z + this.anIntArray412[local314]) - y;
            }
        } else {
            @Pc(240) int local240;
            @Pc(251) int local251;
            if (hillType == 2) {
                @Pc(231) short local231 = this.aShort63;
                if (local231 == 0) {
                    return;
                }
                for (local240 = 0; local240 < this.anInt5543; local240++) {
                    local251 = (this.anIntArray409[local240] << 16) / local231;
                    if (local251 < hillValue) {
                        this.anIntArray409[local240] += (floor.averageHeight(this.anIntArray410[local240] + x, this.anIntArray412[local240] + z) - y) * (hillValue - local251) / hillValue;
                    }
                }
            } else {
                @Pc(338) int local338;
                if (hillType == 3) {
                    local314 = (hillValue & 0xFF) * 4;
                    local240 = (hillValue >> 8 & 0xFF) * 4;
                    local251 = (hillValue >> 16 & 0xFF) << 6;
                    local338 = (hillValue >> 24 & 0xFF) << 6;
                    if (x - (local314 >> 1) < 0 || floor.sizeX << floor.tileSizeShift <= (local314 >> 1) + x + floor.tileSize || z - (local240 >> 1) < 0 || z + (local240 >> 1) + floor.tileSize >= floor.sizeZ << floor.tileSizeShift) {
                        return;
                    }
                    this.method7490(y, local251, x, local314, z, local240, floor, local338);
                } else if (hillType == 4) {
                    local314 = this.aShort68 - this.aShort63;
                    for (local240 = 0; local240 < this.anInt5543; local240++) {
                        this.anIntArray409[local240] = local314 + this.anIntArray409[local240] + ceiling.averageHeight(x + this.anIntArray410[local240], this.anIntArray412[local240] + z) - y;
                    }
                } else if (hillType == 5) {
                    local314 = this.aShort68 - this.aShort63;
                    for (local240 = 0; local240 < this.anInt5543; local240++) {
                        local251 = x + this.anIntArray410[local240];
                        local338 = this.anIntArray412[local240] + z;
                        @Pc(454) int local454 = floor.averageHeight(local251, local338);
                        @Pc(460) int local460 = ceiling.averageHeight(local251, local338);
                        @Pc(468) int local468 = local454 - local460 - hillValue;
                        this.anIntArray409[local240] = (local468 * ((this.anIntArray409[local240] << 8) / local314) >> 8) + local454 - y;
                    }
                }
            }
        }
        this.aBoolean424 = false;
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "s", descriptor = "(I)V")
    @Override
    public void s(@OriginalArg(0) int functionMask) {
        this.anInt5520 = functionMask;
        if (this.aClass346_1 != null && (this.anInt5520 & 0x10000) == 0) {
            this.aShortArray73 = this.aClass346_1.aShortArray130;
            this.aByteArray53 = this.aClass346_1.aByteArray99;
            this.aShortArray72 = this.aClass346_1.aShortArray128;
            this.aShortArray69 = this.aClass346_1.aShortArray129;
            this.aClass346_1 = null;
        }
        this.aBoolean422 = true;
        this.method4983();
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(IILclient!tt;ZII)Z")
    @Override
    public boolean pickedOrtho(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift, @OriginalArg(5) int angle) {
        return this.method4981(matrix, quick, y, x, angle, sizeShift);
    }

    @OriginalMember(owner = "client!kla", name = "LA", descriptor = "(I)V")
    @Override
    public void LA(@OriginalArg(0) int contrast) {
        this.aShort67 = (short) contrast;
        if (this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
        }
        if (this.aClass94_7 != null) {
            this.aClass94_7.anInterface12_2 = null;
        }
    }

    @OriginalMember(owner = "client!kla", name = "ma", descriptor = "()I")
    @Override
    public int ma() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort65;
    }

    @OriginalMember(owner = "client!kla", name = "r", descriptor = "()Z")
    @Override
    public boolean r() {
        return this.aBoolean423;
    }

    @OriginalMember(owner = "client!kla", name = "g", descriptor = "()V")
    @Override
    protected void method7491() {
    }

    @OriginalMember(owner = "client!kla", name = "EA", descriptor = "()I")
    @Override
    public int EA() {
        if (!this.aBoolean424) {
            this.method4982();
        }
        return this.aShort68;
    }

    @OriginalMember(owner = "client!kla", name = "a", descriptor = "(JIFFIIILclient!dv;II)S")
    public short method4993(@OriginalArg(0) long arg0, @OriginalArg(1) int arg1, @OriginalArg(2) float arg2, @OriginalArg(3) float arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) Mesh arg6, @OriginalArg(8) int arg7, @OriginalArg(9) int arg8) {
        @Pc(25) int local25 = this.anIntArray411[arg8];
        @Pc(32) int local32 = this.anIntArray411[arg8 + 1];
        @Pc(34) int local34 = 0;
        for (@Pc(36) int local36 = local25; local36 < local32; local36++) {
            @Pc(45) short local45 = this.aShortArray70[local36];
            if (local45 == 0) {
                local34 = local36;
                break;
            }
            if (arg0 == Static350.aLongArray9[local36]) {
                return (short) (local45 - 1);
            }
        }
        this.aShortArray70[local34] = (short) (this.anInt5538 + 1);
        Static350.aLongArray9[local34] = arg0;
        this.aShortArray69[this.anInt5538] = (short) arg1;
        this.aShortArray73[this.anInt5538] = (short) arg4;
        this.aShortArray72[this.anInt5538] = (short) arg7;
        this.aByteArray53[this.anInt5538] = (byte) arg5;
        this.aFloatArray37[this.anInt5538] = arg2;
        this.aFloatArray39[this.anInt5538] = arg3;
        return (short) this.anInt5538++;
    }

    @OriginalMember(owner = "client!kla", name = "F", descriptor = "()Z")
    @Override
    public boolean F() {
        return this.aBoolean421;
    }

    @OriginalMember(owner = "client!kla", name = "FA", descriptor = "(I)V")
    @Override
    public void FA(@OriginalArg(0) int arg0) {
        @Pc(9) int local9 = Trig1.SIN[arg0];
        @Pc(13) int local13 = Trig1.COS[arg0];
        for (@Pc(15) int local15 = 0; local15 < this.anInt5543; local15++) {
            @Pc(35) int local35 = this.anIntArray409[local15] * local13 - local9 * this.anIntArray412[local15] >> 14;
            this.anIntArray412[local15] = local9 * this.anIntArray409[local15] + local13 * this.anIntArray412[local15] >> 14;
            this.anIntArray409[local15] = local35;
        }
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
        this.aBoolean424 = false;
    }

    @OriginalMember(owner = "client!kla", name = "da", descriptor = "()I")
    @Override
    public int da() {
        return this.aShort67;
    }

    @OriginalMember(owner = "client!kla", name = "v", descriptor = "()V")
    @Override
    public void v() {
        for (@Pc(7) int local7 = 0; local7 < this.anInt5543; local7++) {
            this.anIntArray412[local7] = -this.anIntArray412[local7];
        }
        for (@Pc(27) int local27 = 0; local27 < this.anInt5538; local27++) {
            this.aShortArray72[local27] = (short) -this.aShortArray72[local27];
        }
        for (@Pc(50) int local50 = 0; local50 < this.anInt5560; local50++) {
            @Pc(61) short local61 = this.aShortArray67[local50];
            this.aShortArray67[local50] = this.aShortArray76[local50];
            this.aShortArray76[local50] = local61;
        }
        if (this.aClass94_7 == null && this.aClass94_6 != null) {
            this.aClass94_6.anInterface12_2 = null;
        }
        if (this.aClass94_7 != null) {
            this.aClass94_7.anInterface12_2 = null;
        }
        if (this.aClass136_1 != null) {
            this.aClass136_1.anInterface1_1 = null;
        }
        this.aBoolean424 = false;
        if (this.aClass94_8 != null) {
            this.aClass94_8.anInterface12_2 = null;
        }
    }
}

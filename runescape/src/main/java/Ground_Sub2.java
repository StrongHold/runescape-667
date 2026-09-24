import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.Node;
import com.jagex.graphics.Ground;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.skybox.SkyBoxSphere;
import com.jagex.math.ColourUtils;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeapBuffer;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pn")
public final class Ground_Sub2 extends Ground {

    @OriginalMember(owner = "client!pn", name = "Q", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray10;

    @OriginalMember(owner = "client!pn", name = "A", descriptor = "I")
    public int anInt7491;

    @OriginalMember(owner = "client!pn", name = "G", descriptor = "I")
    public int anInt7498;

    @OriginalMember(owner = "client!pn", name = "ob", descriptor = "I")
    public int anInt7507;

    @OriginalMember(owner = "client!pn", name = "nb", descriptor = "Lclient!ed;")
    public Class94 aClass94_11;

    @OriginalMember(owner = "client!pn", name = "N", descriptor = "I")
    public int anInt7508;

    @OriginalMember(owner = "client!pn", name = "B", descriptor = "Lclient!ed;")
    public Class94 aClass94_12;

    @OriginalMember(owner = "client!pn", name = "U", descriptor = "Lclient!ed;")
    public Class94 aClass94_13;

    @OriginalMember(owner = "client!pn", name = "V", descriptor = "Lclient!ed;")
    public Class94 aClass94_14;

    @OriginalMember(owner = "client!pn", name = "gb", descriptor = "[Lclient!nm;")
    public Node_Sub39[] aClass2_Sub39Array1;

    @OriginalMember(owner = "client!pn", name = "mb", descriptor = "Lclient!jc;")
    public Interface12 anInterface12_5;

    @OriginalMember(owner = "client!pn", name = "I", descriptor = "Lclient!sia;")
    public final Deque aDeque_43 = new Deque();

    @OriginalMember(owner = "client!pn", name = "J", descriptor = "I")
    public final int anInt7488 = this.tileSizeShift - 2;

    @OriginalMember(owner = "client!pn", name = "O", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_33;

    @OriginalMember(owner = "client!pn", name = "W", descriptor = "[[B")
    public byte[][] aByteArrayArray26;

    @OriginalMember(owner = "client!pn", name = "tb", descriptor = "[[[Lclient!nm;")
    public Node_Sub39[][][] aClass2_Sub39ArrayArrayArray1;

    @OriginalMember(owner = "client!pn", name = "T", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray12;

    @OriginalMember(owner = "client!pn", name = "S", descriptor = "I")
    public final int anInt7486;

    @OriginalMember(owner = "client!pn", name = "C", descriptor = "[[S")
    public final short[][] aShortArrayArray6;

    @OriginalMember(owner = "client!pn", name = "rb", descriptor = "[[F")
    public float[][] aFloatArrayArray14;

    @OriginalMember(owner = "client!pn", name = "hb", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray11;

    @OriginalMember(owner = "client!pn", name = "fb", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray14;

    @OriginalMember(owner = "client!pn", name = "qb", descriptor = "[[F")
    public float[][] aFloatArrayArray15;

    @OriginalMember(owner = "client!pn", name = "cb", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray15;

    @OriginalMember(owner = "client!pn", name = "Y", descriptor = "[[B")
    public final byte[][] aByteArrayArray25;

    @OriginalMember(owner = "client!pn", name = "D", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray13;

    @OriginalMember(owner = "client!pn", name = "wb", descriptor = "[[F")
    public float[][] aFloatArrayArray16;

    @OriginalMember(owner = "client!pn", name = "E", descriptor = "I")
    public final int anInt7499;

    @OriginalMember(owner = "client!pn", name = "M", descriptor = "Lclient!av;")
    public IterableHashTable aIterableHashTable_37;

    @OriginalMember(owner = "client!pn", name = "xb", descriptor = "Lclient!hn;")
    public Class170 aClass170_1;

    @OriginalMember(owner = "client!pn", name = "<init>", descriptor = "(Lclient!qha;IIII[[I[[II)V")
    public Ground_Sub2(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int[][] arg5, @OriginalArg(6) int[][] arg6, @OriginalArg(7) int arg7) {
        super(arg3, arg4, arg7, arg5);
        this.aClass19_Sub3_33 = arg0;
        this.aByteArrayArray26 = new byte[arg3 + 1][arg4 + 1];
        this.aClass2_Sub39ArrayArrayArray1 = new Node_Sub39[arg3][arg4][];
        this.anIntArrayArrayArray12 = new int[arg3][arg4][];
        this.anInt7486 = arg2;
        this.aShortArrayArray6 = new short[arg3 * arg4][];
        this.aFloatArrayArray14 = new float[super.sizeX + 1][super.sizeZ + 1];
        this.anIntArrayArrayArray11 = new int[arg3][arg4][];
        this.anIntArrayArrayArray14 = new int[arg3][arg4][];
        this.aFloatArrayArray15 = new float[super.sizeX + 1][super.sizeZ + 1];
        this.anIntArrayArrayArray15 = new int[arg3][arg4][];
        this.aByteArrayArray25 = new byte[arg3][arg4];
        this.anIntArrayArrayArray13 = new int[arg3][arg4][];
        this.aFloatArrayArray16 = new float[super.sizeX + 1][super.sizeZ + 1];
        this.anInt7499 = 0x1 << this.anInt7488;
        for (@Pc(115) int local115 = 1; super.sizeZ > local115; local115++) {
            for (@Pc(121) int local121 = 1; local121 < super.sizeX; local121++) {
                @Pc(140) int local140 = arg6[local121 + 1][local115] - arg6[local121 - 1][local115];
                @Pc(157) int local157 = arg6[local121][local115 + 1] - arg6[local121][local115 - 1];
                @Pc(176) float local176 = (float) (1.0D / Math.sqrt(local157 * local157 + arg7 * 4 * arg7 + local140 * local140));
                this.aFloatArrayArray14[local121][local115] = (float) local140 * local176;
                this.aFloatArrayArray15[local121][local115] = (float) (-arg7 * 2) * local176;
                this.aFloatArrayArray16[local121][local115] = local176 * (float) local157;
            }
        }
        this.aIterableHashTable_37 = new IterableHashTable(128);
        if ((this.anInt7486 & 0x10) != 0) {
            this.aClass170_1 = new Class170(this.aClass19_Sub3_33, this);
        }
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(III[[ZZI)V")
    @Override
    public void renderTiles(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean[][] arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5) {
        this.method6668(arg4, arg3, arg0, -1, arg2, arg1, arg5);
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(Lclient!lca;[I)V")
    @Override
    public void method7868(@OriginalArg(0) PointLight arg0, @OriginalArg(1) int[] arg1) {
        this.aDeque_43.addLast(new Node_Sub33(this.aClass19_Sub3_33, this, arg0, arg1));
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(Lclient!ph;IBI)V")
    public void method6667(@OriginalArg(0) DoublyLinkedNode_Sub2_Sub9_ arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2) {
        @Pc(12) int[] local12 = this.anIntArrayArrayArray14[arg1][arg2];
        @Pc(19) int[] local19 = this.anIntArrayArrayArray11[arg1][arg2];
        @Pc(27) int local27 = local12.length;
        if (this.aClass19_Sub3_33.anIntArray623.length < local27) {
            this.aClass19_Sub3_33.anIntArray623 = new int[local27];
            this.aClass19_Sub3_33.anIntArray625 = new int[local27];
        }
        @Pc(53) int[] local53 = this.aClass19_Sub3_33.anIntArray623;
        @Pc(57) int[] local57 = this.aClass19_Sub3_33.anIntArray625;
        for (@Pc(59) int local59 = 0; local59 < local27; local59++) {
            local53[local59] = local12[local59] >> this.aClass19_Sub3_33.anInt7989;
            local57[local59] = local19[local59] >> this.aClass19_Sub3_33.anInt7989;
        }
        @Pc(93) int local93 = 0;
        while (local93 < local27) {
            @Pc(101) int local101 = local53[local93];
            @Pc(106) int local106 = local57[local93++];
            @Pc(110) int local110 = local53[local93];
            @Pc(115) int local115 = local57[local93++];
            @Pc(119) int local119 = local53[local93];
            @Pc(124) int local124 = local57[local93++];
            if ((local115 - local124) * (local101 - local110) - (local115 - local106) * (-local110 + local119) > 0) {
                arg0.method6542(local101, local106, local110, local115, local124, local119);
            }
        }
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(IIIIIII[[Z)V")
    @Override
    public void drawMinimap(@OriginalArg(3) int x1, @OriginalArg(4) int y1, @OriginalArg(5) int x2, @OriginalArg(6) int y2, @OriginalArg(7) boolean[][] visibility) {
        if (this.anInt7508 <= 0) {
            return;
        }
        this.aClass19_Sub3_33.method6981();
        this.aClass19_Sub3_33.method7035(false);
        this.aClass19_Sub3_33.method7008(false);
        this.aClass19_Sub3_33.method7006(false);
        this.aClass19_Sub3_33.method6972(false);
        this.aClass19_Sub3_33.setBlendMode(0);
        this.aClass19_Sub3_33.method7046(-2);
        this.aClass19_Sub3_33.method7001(null);
        Static699.aFloatArray81[5] = (float) 1024 / ((float) super.tileSize * 128.0F * (float) this.aClass19_Sub3_33.anInt7956);
        Static699.aFloatArray81[0] = (float) 1024 / ((float) this.aClass19_Sub3_33.anInt7869 * (128.0F * (float) super.tileSize));
        Static699.aFloatArray81[2] = 0.0F;
        Static699.aFloatArray81[15] = 1.0F;
        Static699.aFloatArray81[8] = 0.0F;
        Static699.aFloatArray81[9] = 0.0F;
        Static699.aFloatArray81[13] = 1.0F - ((float) (y2 * 1024) / 128.0F + (float) 0) / (float) this.aClass19_Sub3_33.anInt7956;
        Static699.aFloatArray81[14] = 0.0F;
        Static699.aFloatArray81[6] = 0.0F;
        Static699.aFloatArray81[11] = 0.0F;
        Static699.aFloatArray81[3] = 0.0F;
        Static699.aFloatArray81[12] = -(((float) 0 + (float) (x1 * 1024) / 128.0F) / (float) this.aClass19_Sub3_33.anInt7869) - 1.0F;
        Static699.aFloatArray81[1] = 0.0F;
        Static699.aFloatArray81[10] = 0.0F;
        Static699.aFloatArray81[4] = 0.0F;
        Static699.aFloatArray81[7] = 0.0F;
        OpenGL.glMatrixMode(OpenGL.GL_PROJECTION);
        OpenGL.glLoadMatrixf(Static699.aFloatArray81, 0);
        Static699.aFloatArray81[12] = 0.0F;
        Static699.aFloatArray81[15] = 1.0F;
        Static699.aFloatArray81[10] = 0.0F;
        Static699.aFloatArray81[9] = 1.0F;
        Static699.aFloatArray81[14] = 0.0F;
        Static699.aFloatArray81[8] = 0.0F;
        Static699.aFloatArray81[0] = 1.0F;
        Static699.aFloatArray81[5] = 0.0F;
        Static699.aFloatArray81[7] = 0.0F;
        Static699.aFloatArray81[6] = 1.0F;
        Static699.aFloatArray81[4] = 0.0F;
        Static699.aFloatArray81[11] = 0.0F;
        Static699.aFloatArray81[2] = 0.0F;
        Static699.aFloatArray81[1] = 0.0F;
        Static699.aFloatArray81[3] = 0.0F;
        Static699.aFloatArray81[13] = 0.0F;
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        OpenGL.glLoadMatrixf(Static699.aFloatArray81, 0);
        if ((this.anInt7486 & 0x7) == 0) {
            this.aClass19_Sub3_33.method7008(false);
        } else {
            this.aClass19_Sub3_33.method7008(true);
            this.aClass19_Sub3_33.method6987();
        }
        this.aClass19_Sub3_33.method7039(this.aClass94_12, this.aClass94_13, this.aClass94_11, this.aClass94_14);
        if (this.aClass19_Sub3_33.aClass2_Sub21_Sub1_3.data.length >= this.anInt7491 * 2) {
            this.aClass19_Sub3_33.aClass2_Sub21_Sub1_3.pos = 0;
        } else {
            this.aClass19_Sub3_33.aClass2_Sub21_Sub1_3 = new Node_Sub21_Sub1(this.anInt7491 * 2);
        }
        @Pc(317) int local317 = 0;
        @Pc(321) Node_Sub21_Sub1 local321 = this.aClass19_Sub3_33.aClass2_Sub21_Sub1_3;
        @Pc(327) int local327;
        @Pc(337) int local337;
        @Pc(339) int local339;
        @Pc(360) short[] local360;
        @Pc(364) int local364;
        if (this.aClass19_Sub3_33.aBoolean618) {
            for (local327 = y1; local327 < y2; local327++) {
                local337 = local327 * super.sizeX + x1;
                for (local339 = x1; local339 < x2; local339++) {
                    if (visibility[local339 - x1][local327 - y1]) {
                        local360 = this.aShortArrayArray6[local337];
                        if (local360 != null) {
                            for (local364 = 0; local364 < local360.length; local364++) {
                                local317++;
                                local321.p2(local360[local364] & 0xFFFF);
                            }
                        }
                    }
                    local337++;
                }
            }
        } else {
            for (local327 = y1; local327 < y2; local327++) {
                local337 = local327 * super.sizeX + x1;
                for (local339 = x1; local339 < x2; local339++) {
                    if (visibility[local339 - x1][local327 - y1]) {
                        local360 = this.aShortArrayArray6[local337];
                        if (local360 != null) {
                            for (local364 = 0; local364 < local360.length; local364++) {
                                local321.ip2(local360[local364] & 0xFFFF);
                                local317++;
                            }
                        }
                    }
                    local337++;
                }
            }
        }
        if (local317 > 0) {
            @Pc(502) Class134_Sub2 local502 = new Class134_Sub2(this.aClass19_Sub3_33, 5123, local321.data, local321.pos);
            this.aClass19_Sub3_33.method7034(0, local502, local317);
        }
    }

    @OriginalMember(owner = "client!pn", name = "CA", descriptor = "(Lclient!r;IIIIZ)V")
    @Override
    public void CA(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5) {
        if (this.aClass170_1 != null && arg0 != null) {
            @Pc(27) int local27 = arg1 - (arg2 * this.aClass19_Sub3_33.anInt8027 >> 8) >> this.aClass19_Sub3_33.anInt7989;
            @Pc(42) int local42 = arg3 - (arg2 * this.aClass19_Sub3_33.anInt8023 >> 8) >> this.aClass19_Sub3_33.anInt7989;
            this.aClass170_1.method3541(local27, local42, arg0);
        }
    }

    @OriginalMember(owner = "client!pn", name = "YA", descriptor = "()V")
    @Override
    public void YA() {
        if (this.anInt7508 <= 0) {
            this.aClass170_1 = null;
        } else {
            @Pc(25) byte[][] local25 = new byte[super.sizeX + 1][super.sizeZ + 1];
            @Pc(31) int local31;
            for (@Pc(27) int local27 = 1; local27 < super.sizeX; local27++) {
                for (local31 = 1; local31 < super.sizeZ; local31++) {
                    local25[local27][local31] = (byte) ((this.aByteArrayArray26[local27][local31] >> 1) + (this.aByteArrayArray26[local27 + 1][local31] >> 3) + (this.aByteArrayArray26[local27 - 1][local31] >> 2) + (this.aByteArrayArray26[local27][local31 + -1] >> 2) + (this.aByteArrayArray26[local27][local31 + 1] >> 3));
                }
            }
            this.aClass2_Sub39Array1 = new Node_Sub39[this.aIterableHashTable_37.size()];
            this.aIterableHashTable_37.copyTo(this.aClass2_Sub39Array1);
            for (local31 = 0; local31 < this.aClass2_Sub39Array1.length; local31++) {
                this.aClass2_Sub39Array1[local31].method5868(this.anInt7508);
            }
            @Pc(149) int local149 = 24;
            if (this.anIntArrayArrayArray10 != null) {
                local149 += 4;
            }
            if ((this.anInt7486 & 0x7) != 0) {
                local149 += 12;
            }
            @Pc(178) NativeHeapBuffer local178 = this.aClass19_Sub3_33.lb.a(this.anInt7508 * local149, false);
            @Pc(183) Stream local183 = new Stream(local178);
            @Pc(187) Node_Sub39[] local187 = new Node_Sub39[this.anInt7508];
            @Pc(194) int local194 = SkyBoxSphere.method5587(this.anInt7508 / 4);
            if (local194 < 1) {
                local194 = 1;
            }
            @Pc(206) IterableHashTable local206 = new IterableHashTable(local194);
            @Pc(210) Node_Sub39[] local210 = new Node_Sub39[this.anInt7507];
            @Pc(216) int local216;
            for (@Pc(212) int local212 = 0; super.sizeX > local212; local212++) {
                for (local216 = 0; super.sizeZ > local216; local216++) {
                    if (this.anIntArrayArrayArray15[local212][local216] != null) {
                        @Pc(234) Node_Sub39[] local234 = this.aClass2_Sub39ArrayArrayArray1[local212][local216];
                        @Pc(241) int[] local241 = this.anIntArrayArrayArray14[local212][local216];
                        @Pc(248) int[] local248 = this.anIntArrayArrayArray11[local212][local216];
                        @Pc(255) int[] local255 = this.anIntArrayArrayArray12[local212][local216];
                        @Pc(262) int[] local262 = this.anIntArrayArrayArray15[local212][local216];
                        @Pc(275) int[] local275 = this.anIntArrayArrayArray13 == null ? null : this.anIntArrayArrayArray13[local212][local216];
                        @Pc(288) int[] local288 = this.anIntArrayArrayArray10 == null ? null : this.anIntArrayArrayArray10[local212][local216];
                        if (local255 == null) {
                            local255 = local262;
                        }
                        @Pc(299) float local299 = this.aFloatArrayArray14[local212][local216];
                        @Pc(306) float local306 = this.aFloatArrayArray15[local212][local216];
                        @Pc(313) float local313 = this.aFloatArrayArray16[local212][local216];
                        @Pc(322) float local322 = this.aFloatArrayArray14[local212][local216 + 1];
                        @Pc(331) float local331 = this.aFloatArrayArray15[local212][local216 + 1];
                        @Pc(340) float local340 = this.aFloatArrayArray16[local212][local216 + 1];
                        @Pc(351) float local351 = this.aFloatArrayArray14[local212 + 1][local216 + 1];
                        @Pc(362) float local362 = this.aFloatArrayArray15[local212 + 1][local216 + 1];
                        @Pc(373) float local373 = this.aFloatArrayArray16[local212 + 1][local216 + 1];
                        @Pc(382) float local382 = this.aFloatArrayArray14[local212 + 1][local216];
                        @Pc(391) float local391 = this.aFloatArrayArray15[local212 + 1][local216];
                        @Pc(400) float local400 = this.aFloatArrayArray16[local212 + 1][local216];
                        @Pc(408) int local408 = local25[local212][local216] & 0xFF;
                        @Pc(418) int local418 = local25[local212][local216 + 1] & 0xFF;
                        @Pc(430) int local430 = local25[local212 + 1][local216 + 1] & 0xFF;
                        @Pc(440) int local440 = local25[local212 + 1][local216] & 0xFF;
                        @Pc(442) int local442 = 0;
                        @Pc(454) int local454;
                        label335:
                        for (@Pc(444) int local444 = 0; local444 < local262.length; local444++) {
                            @Pc(452) Node_Sub39 local452 = local234[local444];
                            for (local454 = 0; local454 < local442; local454++) {
                                if (local452 == local210[local454]) {
                                    continue label335;
                                }
                            }
                            local210[local442++] = local452;
                        }
                        @Pc(499) short[] local499 = this.aShortArrayArray6[local212 + super.sizeX * local216] = new short[local262.length];
                        for (local454 = 0; local454 < local262.length; local454++) {
                            @Pc(512) int local512 = (local212 << super.tileSizeShift) + local241[local454];
                            @Pc(522) int local522 = (local216 << super.tileSizeShift) + local248[local454];
                            @Pc(527) int local527 = local512 >> this.anInt7488;
                            @Pc(532) int local532 = local522 >> this.anInt7488;
                            @Pc(536) int local536 = local262[local454];
                            @Pc(540) int local540 = local255[local454];
                            @Pc(549) int local549 = local275 == null ? 0 : local275[local454];
                            @Pc(567) long local567 = (long) local540 << 48 | (long) local536 << 32 | (long) (local527 << 16) | (long) local532;
                            @Pc(571) int local571 = local241[local454];
                            @Pc(575) int local575 = local248[local454];
                            @Pc(577) byte local577 = 74;
                            @Pc(579) int local579 = 0;
                            @Pc(581) float local581 = 1.0F;
                            @Pc(610) float local610;
                            @Pc(608) float local608;
                            @Pc(602) float local602;
                            @Pc(702) float local702;
                            @Pc(606) int local606;
                            if (local571 == 0 && local575 == 0) {
                                local608 = local306;
                                local606 = local577 - local408;
                                local602 = local313;
                                local610 = local299;
                            } else if (local571 == 0 && local575 == super.tileSize) {
                                local602 = local340;
                                local606 = local577 - local418;
                                local608 = local331;
                                local610 = local322;
                            } else if (super.tileSize == local571 && super.tileSize == local575) {
                                local606 = local577 - local430;
                                local602 = local373;
                                local608 = local362;
                                local610 = local351;
                            } else if (super.tileSize == local571 && local575 == 0) {
                                local606 = local577 - local440;
                                local608 = local391;
                                local610 = local382;
                                local602 = local400;
                            } else {
                                @Pc(677) float local677 = (float) local571 / (float) super.tileSize;
                                @Pc(684) float local684 = (float) local575 / (float) super.tileSize;
                                @Pc(693) float local693 = local299 + local677 * (local382 - local299);
                                local702 = local677 * (local391 - local306) + local306;
                                @Pc(711) float local711 = local313 + (local400 - local313) * local677;
                                @Pc(720) float local720 = local322 + (local351 - local322) * local677;
                                @Pc(728) float local728 = local331 + (local362 - local331) * local677;
                                local610 = local693 + local684 * (local720 - local693);
                                @Pc(746) float local746 = (local373 - local340) * local677 + local340;
                                local608 = (local728 - local702) * local684 + local702;
                                local602 = (local746 - local711) * local684 + local711;
                                @Pc(776) int local776 = ((local440 - local408) * local571 >> super.tileSizeShift) + local408;
                                @Pc(788) int local788 = local418 + (local571 * (local430 - local418) >> super.tileSizeShift);
                                local606 = local577 - (local575 * (local788 - local776) >> super.tileSizeShift) - local776;
                            }
                            if (local536 != -1) {
                                @Pc(826) int local826 = local606 * (local536 & 0x7F) >> 7;
                                if (local826 < 2) {
                                    local826 = 2;
                                } else if (local826 > 126) {
                                    local826 = 126;
                                }
                                local579 = ColourUtils.HSL_TO_RGB[local536 & 0xFF80 | local826];
                                if ((this.anInt7486 & 0x7) == 0) {
                                    local581 = local602 * this.aClass19_Sub3_33.aFloatArray51[2] + (local608 * this.aClass19_Sub3_33.aFloatArray51[1] + local610 * this.aClass19_Sub3_33.aFloatArray51[0]);
                                    local581 = this.aClass19_Sub3_33.aFloat149 + local581 * (local581 > 0.0F ? this.aClass19_Sub3_33.aFloat129 : this.aClass19_Sub3_33.aFloat130);
                                }
                            }
                            @Pc(903) Node local903 = null;
                            if ((this.anInt7499 - 1 & local512) == 0 && (this.anInt7499 - 1 & local522) == 0) {
                                local903 = local206.get(local567);
                            }
                            @Pc(1293) int local1293;
                            @Pc(972) int local972;
                            if (local903 == null) {
                                if (local536 == local540) {
                                    local972 = local579;
                                } else {
                                    @Pc(946) int local946 = (local540 & 0x7F) * local606 >> 7;
                                    if (local946 < 2) {
                                        local946 = 2;
                                    } else if (local946 > 126) {
                                        local946 = 126;
                                    }
                                    local972 = ColourUtils.HSL_TO_RGB[local540 & 0xFF80 | local946];
                                    if ((this.anInt7486 & 0x7) == 0) {
                                        local702 = this.aClass19_Sub3_33.aFloatArray51[1] * local608 + local610 * this.aClass19_Sub3_33.aFloatArray51[0] + local602 * this.aClass19_Sub3_33.aFloatArray51[2];
                                        local702 = (local581 > 0.0F ? this.aClass19_Sub3_33.aFloat129 : this.aClass19_Sub3_33.aFloat130) * local581 + this.aClass19_Sub3_33.aFloat149;
                                        @Pc(1026) int local1026 = local972 >> 16 & 0xFF;
                                        @Pc(1032) int local1032 = local972 >> 8 & 0xFF;
                                        local1026 = (int) ((float) local1026 * local702);
                                        @Pc(1042) int local1042 = local972 & 0xFF;
                                        if (local1026 < 0) {
                                            local1026 = 0;
                                        } else if (local1026 > 255) {
                                            local1026 = 255;
                                        }
                                        local1032 = (int) ((float) local1032 * local702);
                                        if (local1032 < 0) {
                                            local1032 = 0;
                                        } else if (local1032 > 255) {
                                            local1032 = 255;
                                        }
                                        local1042 = (int) ((float) local1042 * local702);
                                        if (local1042 < 0) {
                                            local1042 = 0;
                                        } else if (local1042 > 255) {
                                            local1042 = 255;
                                        }
                                        local972 = local1026 << 16 | local1032 << 8 | local1042;
                                    }
                                }
                                if (this.aClass19_Sub3_33.aBoolean618) {
                                    local183.a((float) local512);
                                    local183.a((float) (local549 + this.averageHeight(local512, local522)));
                                    local183.a((float) local522);
                                    local183.d((byte) (local972 >> 16));
                                    local183.d((byte) (local972 >> 8));
                                    local183.d((byte) local972);
                                    local183.d(-1);
                                    local183.a((float) local512);
                                    local183.a((float) local522);
                                    if (this.anIntArrayArrayArray10 != null) {
                                        local183.a((float) (local288 == null ? 0 : local288[local454] - 1));
                                    }
                                    if ((this.anInt7486 & 0x7) != 0) {
                                        local183.a(local610);
                                        local183.a(local608);
                                        local183.a(local602);
                                    }
                                } else {
                                    local183.b((float) local512);
                                    local183.b((float) (local549 + this.averageHeight(local512, local522)));
                                    local183.b((float) local522);
                                    local183.d((byte) (local972 >> 16));
                                    local183.d((byte) (local972 >> 8));
                                    local183.d((byte) local972);
                                    local183.d(-1);
                                    local183.b((float) local512);
                                    local183.b((float) local522);
                                    if (this.anIntArrayArrayArray10 != null) {
                                        local183.b((float) (local288 == null ? 0 : local288[local454] - 1));
                                    }
                                    if ((this.anInt7486 & 0x7) != 0) {
                                        local183.b(local610);
                                        local183.b(local608);
                                        local183.b(local602);
                                    }
                                }
                                local1293 = this.anInt7498++;
                                local499[local454] = (short) local1293;
                                if (local536 != -1) {
                                    local187[local1293] = local234[local454];
                                }
                                local206.put(local567, new Node_Sub50(local499[local454]));
                            } else {
                                local499[local454] = ((Node_Sub50) local903).aShort119;
                                local1293 = local499[local454] & 0xFFFF;
                                if (local536 != -1 && local187[local1293].key > local234[local454].key) {
                                    local187[local1293] = local234[local454];
                                }
                            }
                            for (local972 = 0; local972 < local442; local972++) {
                                local210[local972].method5863(local579, local606, local581, local1293);
                            }
                            this.anInt7491++;
                        }
                    }
                }
            }
            for (local216 = 0; local216 < this.anInt7498; local216++) {
                @Pc(1419) Node_Sub39 local1419 = local187[local216];
                if (local1419 != null) {
                    local1419.method5866(local216);
                }
            }
            @Pc(1458) int local1458;
            for (@Pc(1438) int local1438 = 0; local1438 < super.sizeX; local1438++) {
                for (@Pc(1442) int local1442 = 0; local1442 < super.sizeZ; local1442++) {
                    @Pc(1454) short[] local1454 = this.aShortArrayArray6[local1438 + local1442 * super.sizeX];
                    if (local1454 != null) {
                        local1458 = 0;
                        @Pc(1460) int local1460 = 0;
                        while (local1454.length > local1460) {
                            @Pc(1469) int local1469 = local1454[local1460++] & 0xFFFF;
                            @Pc(1476) int local1476 = local1454[local1460++] & 0xFFFF;
                            @Pc(1483) int local1483 = local1454[local1460++] & 0xFFFF;
                            @Pc(1487) Node_Sub39 local1487 = local187[local1469];
                            @Pc(1491) Node_Sub39 local1491 = local187[local1476];
                            @Pc(1495) Node_Sub39 local1495 = local187[local1483];
                            @Pc(1497) Node_Sub39 local1497 = null;
                            if (local1487 != null) {
                                local1487.method5867(local1438, local1458, local1442);
                                local1497 = local1487;
                            }
                            if (local1491 != null) {
                                local1491.method5867(local1438, local1458, local1442);
                                if (local1497 == null || local1497.key > local1491.key) {
                                    local1497 = local1491;
                                }
                            }
                            if (local1495 != null) {
                                local1495.method5867(local1438, local1458, local1442);
                                if (local1497 == null || local1497.key > local1495.key) {
                                    local1497 = local1495;
                                }
                            }
                            if (local1497 != null) {
                                if (local1487 != null) {
                                    local1497.method5866(local1469);
                                }
                                if (local1491 != null) {
                                    local1497.method5866(local1476);
                                }
                                if (local1495 != null) {
                                    local1497.method5866(local1483);
                                }
                                local1497.method5867(local1438, local1458, local1442);
                            }
                            local1458++;
                        }
                    }
                }
            }
            local183.c();
            this.anInterface12_5 = this.aClass19_Sub3_33.method7016(local149, local178, local183.a());
            this.aClass94_11 = new Class94(this.anInterface12_5, 5126, 3, 0);
            this.aClass94_12 = new Class94(this.anInterface12_5, 5121, 4, 12);
            @Pc(1656) byte local1656;
            if (this.anIntArrayArrayArray10 == null) {
                local1656 = 24;
                this.aClass94_14 = new Class94(this.anInterface12_5, 5126, 2, 16);
            } else {
                local1656 = 28;
                this.aClass94_14 = new Class94(this.anInterface12_5, 5126, 3, 16);
            }
            if ((this.anInt7486 & 0x7) != 0) {
                this.aClass94_13 = new Class94(this.anInterface12_5, 5126, 3, local1656);
            }
            @Pc(1702) long[] local1702 = new long[this.aClass2_Sub39Array1.length];
            for (local1458 = 0; local1458 < this.aClass2_Sub39Array1.length; local1458++) {
                @Pc(1713) Node_Sub39 local1713 = this.aClass2_Sub39Array1[local1458];
                local1702[local1458] = local1713.key;
                local1713.method5864(this.anInt7498);
            }
            Static61.sortByKey(local1702, this.aClass2_Sub39Array1);
            if (this.aClass170_1 != null) {
                this.aClass170_1.method3540();
            }
        }
        this.anIntArrayArrayArray14 = this.anIntArrayArrayArray11 = null;
        this.aIterableHashTable_37 = null;
        this.anIntArrayArrayArray10 = null;
        this.anIntArrayArrayArray13 = null;
        this.anIntArrayArrayArray12 = null;
        this.aByteArrayArray26 = null;
        this.anIntArrayArrayArray15 = null;
        this.aClass2_Sub39ArrayArrayArray1 = null;
        this.aFloatArrayArray14 = this.aFloatArrayArray15 = this.aFloatArrayArray16 = null;
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(II)V")
    @Override
    public void renderTile(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
    }

    @OriginalMember(owner = "client!pn", name = "ka", descriptor = "(III)V")
    @Override
    public void ka(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (arg2 > (this.aByteArrayArray26[arg0][arg1] & 0xFF)) {
            this.aByteArrayArray26[arg0][arg1] = (byte) arg2;
        }
    }

    @OriginalMember(owner = "client!pn", name = "wa", descriptor = "(Lclient!r;IIIIZ)V")
    @Override
    public void wa(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5) {
        if (this.aClass170_1 != null && arg0 != null) {
            @Pc(27) int local27 = arg1 - (arg2 * this.aClass19_Sub3_33.anInt8027 >> 8) >> this.aClass19_Sub3_33.anInt7989;
            @Pc(42) int local42 = arg3 - (this.aClass19_Sub3_33.anInt8023 * arg2 >> 8) >> this.aClass19_Sub3_33.anInt7989;
            this.aClass170_1.method3544(local42, arg0, local27);
        }
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(Lclient!r;IIIIZ)Z")
    @Override
    public boolean method7874(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        if (this.aClass170_1 == null || arg0 == null) {
            return false;
        } else {
            @Pc(28) int local28 = arg1 - (arg2 * this.aClass19_Sub3_33.anInt8027 >> 8) >> this.aClass19_Sub3_33.anInt7989;
            @Pc(43) int local43 = arg3 - (arg2 * this.aClass19_Sub3_33.anInt8023 >> 8) >> this.aClass19_Sub3_33.anInt7989;
            return this.aClass170_1.method3543(local28, arg0, local43);
        }
    }

    @OriginalMember(owner = "client!pn", name = "U", descriptor = "(II[I[I[I[I[I[I[I[IIIIZ)V")
    @Override
    public void U(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int[] arg4, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int[] arg7, @OriginalArg(8) int[] arg8, @OriginalArg(9) int[] arg9, @OriginalArg(10) int arg10, @OriginalArg(11) int arg11, @OriginalArg(12) int arg12, @OriginalArg(13) boolean arg13) {
        if (arg3 != null && this.anIntArrayArrayArray13 == null) {
            this.anIntArrayArrayArray13 = new int[super.sizeX][super.sizeZ][];
        }
        if (arg5 != null && this.anIntArrayArrayArray10 == null) {
            this.anIntArrayArrayArray10 = new int[super.sizeX][super.sizeZ][];
        }
        @Pc(41) TextureSource local41 = this.aClass19_Sub3_33.textureSource;
        this.anIntArrayArrayArray14[arg0][arg1] = arg2;
        this.anIntArrayArrayArray11[arg0][arg1] = arg4;
        this.anIntArrayArrayArray15[arg0][arg1] = arg6;
        this.anIntArrayArrayArray12[arg0][arg1] = arg7;
        if (this.anIntArrayArrayArray10 != null) {
            this.anIntArrayArrayArray10[arg0][arg1] = arg5;
        }
        if (this.anIntArrayArrayArray13 != null) {
            this.anIntArrayArrayArray13[arg0][arg1] = arg3;
        }
        @Pc(104) Node_Sub39[] local104 = this.aClass2_Sub39ArrayArrayArray1[arg0][arg1] = new Node_Sub39[arg6.length];
        for (@Pc(106) int local106 = 0; local106 < arg6.length; local106++) {
            @Pc(114) int local114 = arg8[local106];
            @Pc(118) int local118 = arg9[local106];
            if ((this.anInt7486 & 0x20) != 0 && local114 != -1 && local41.getMetrics(local114).disableable) {
                local114 = -1;
                local118 = 128;
            }
            @Pc(164) long local164 = (long) local114 | (long) arg12 << 48 | (long) arg11 << 42 | (long) arg10 << 28 | (long) (local118 << 14);
            @Pc(170) Node local170;
            for (local170 = this.aIterableHashTable_37.get(local164); local170 != null; local170 = this.aIterableHashTable_37.nextWithSameKey()) {
                @Pc(177) Node_Sub39 local177 = (Node_Sub39) local170;
                if (local177.anInt6592 == local114 && (float) local118 == local177.aFloat121 && arg10 == local177.anInt6593 && local177.anInt6595 == arg11 && local177.anInt6585 == arg12) {
                    break;
                }
            }
            if (local170 == null) {
                local104[local106] = new Node_Sub39(this, local114, local118, arg10, arg11, arg12);
                this.aIterableHashTable_37.put(local164, local104[local106]);
            } else {
                local104[local106] = (Node_Sub39) local170;
            }
        }
        if (arg13) {
            this.aByteArrayArray25[arg0][arg1] = (byte) (this.aByteArrayArray25[arg0][arg1] | 0x1);
        }
        if (this.anInt7507 < arg6.length) {
            this.anInt7507 = arg6.length;
        }
        this.anInt7508 += arg6.length;
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(III[[ZZII)V")
    @Override
    public void renderTilesAtDepth(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean[][] arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        this.method6668(arg4, arg3, arg0, arg5, arg2, arg1, arg6);
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(Z[[ZIIIIII)V")
    public void method6668(@OriginalArg(0) boolean arg0, @OriginalArg(1) boolean[][] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6) {
        if (this.aClass2_Sub39Array1 == null) {
            return;
        }
        @Pc(14) int local14 = arg4 + arg4 + 1;
        @Pc(18) int local18 = local14 * local14;
        if (local18 > this.aClass19_Sub3_33.anIntArray624.length) {
            this.aClass19_Sub3_33.anIntArray624 = new int[local18];
        }
        if (this.aClass19_Sub3_33.aClass2_Sub21_Sub1_3.data.length < this.anInt7491 * 2) {
            this.aClass19_Sub3_33.aClass2_Sub21_Sub1_3 = new Node_Sub21_Sub1(this.anInt7491 * 2);
        }
        @Pc(61) int local61 = arg2 - arg4;
        @Pc(63) int local63 = local61;
        if (local61 < 0) {
            local61 = 0;
        }
        @Pc(75) int local75 = arg5 - arg4;
        @Pc(77) int local77 = local75;
        if (local75 < 0) {
            local75 = 0;
        }
        @Pc(86) int local86 = arg2 + arg4;
        if (super.sizeX - 1 < local86) {
            local86 = super.sizeX - 1;
        }
        @Pc(105) int local105 = arg4 + arg5;
        if (local105 > super.sizeZ - 1) {
            local105 = super.sizeZ - 1;
        }
        @Pc(122) int local122 = 0;
        @Pc(126) int[] local126 = this.aClass19_Sub3_33.anIntArray624;
        @Pc(138) int local138;
        for (@Pc(128) int local128 = local61; local128 <= local86; local128++) {
            @Pc(136) boolean[] local136 = arg1[local128 - local63];
            for (local138 = local75; local138 <= local105; local138++) {
                if (local136[local138 - local77]) {
                    local126[local122++] = local128 + local138 * super.sizeX;
                }
            }
        }
        if (arg3 == -1) {
            this.aClass19_Sub3_33.method7027();
        } else {
            this.aClass19_Sub3_33.method6964((float) arg3);
            this.aClass19_Sub3_33.method6978();
        }
        this.aClass19_Sub3_33.method7008((this.anInt7486 & 0x7) != 0);
        for (@Pc(213) int local213 = 0; local213 < this.aClass2_Sub39Array1.length; local213++) {
            this.aClass2_Sub39Array1[local213].method5861(local126, local122);
        }
        if (!this.aDeque_43.isEmpty()) {
            local138 = this.aClass19_Sub3_33.anInt8002;
            @Pc(252) int local252 = this.aClass19_Sub3_33.anInt8008;
            this.aClass19_Sub3_33.L(0, local252, this.aClass19_Sub3_33.anInt8009);
            this.aClass19_Sub3_33.method7008(false);
            this.aClass19_Sub3_33.method6972(false);
            this.aClass19_Sub3_33.setBlendMode(128);
            this.aClass19_Sub3_33.method7046(-2);
            this.aClass19_Sub3_33.method7001(this.aClass19_Sub3_33.aClass93_Sub2_5);
            this.aClass19_Sub3_33.method7031(7681, 8448);
            this.aClass19_Sub3_33.method7021(34166, 770, 0);
            this.aClass19_Sub3_33.method7029(0, 34167);
            for (@Pc(312) Node local312 = this.aDeque_43.first(); local312 != null; local312 = this.aDeque_43.next()) {
                @Pc(317) Node_Sub33 local317 = (Node_Sub33) local312;
                local317.method4933(arg2, arg1, arg4, arg5);
            }
            this.aClass19_Sub3_33.method7021(5890, 768, 0);
            this.aClass19_Sub3_33.method7029(0, 5890);
            this.aClass19_Sub3_33.method7001(null);
            this.aClass19_Sub3_33.L(local138, local252, this.aClass19_Sub3_33.anInt8009);
        }
        if (this.aClass170_1 == null) {
            return;
        }
        OpenGL.glPushMatrix();
        OpenGL.glTranslatef(0.0F, -1.0F, 0.0F);
        this.aClass19_Sub3_33.method7039(null, null, this.aClass94_11, this.aClass94_14);
        this.aClass170_1.method3542(arg2, arg0, arg5, arg1, arg4);
        OpenGL.glPopMatrix();
    }

    @OriginalMember(owner = "client!pn", name = "fa", descriptor = "(IILclient!r;)Lclient!r;")
    @Override
    public Shadow fa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Shadow arg2) {
        if ((this.aByteArrayArray25[arg0][arg1] & 0x1) == 0) {
            return null;
        }
        @Pc(22) int local22 = super.tileSize >> this.aClass19_Sub3_33.anInt7989;
        @Pc(25) DoublyLinkedNode_Sub2_Sub9_ local25 = (DoublyLinkedNode_Sub2_Sub9_) arg2;
        @Pc(43) DoublyLinkedNode_Sub2_Sub9_ local43;
        if (local25 != null && local25.method6545(local22, local22)) {
            local43 = local25;
            local25.method6546();
        } else {
            local43 = new DoublyLinkedNode_Sub2_Sub9_(this.aClass19_Sub3_33, local22, local22);
        }
        local43.method6544(local22, 0, local22, 0);
        this.method6667(local43, arg0, arg1);
        return local43;
    }

    @OriginalMember(owner = "client!pn", name = "a", descriptor = "(II[I[I[I[I[I[I[I[I[I[I[IIIIZ)V")
    @Override
    public void addTile(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int[] arg4, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int[] arg7, @OriginalArg(8) int[] arg8, @OriginalArg(9) int[] arg9, @OriginalArg(10) int[] arg10, @OriginalArg(11) int[] arg11, @OriginalArg(12) int[] arg12, @OriginalArg(13) int arg13, @OriginalArg(14) int arg14, @OriginalArg(15) int arg15) {
        @Pc(8) int local8 = arg9.length;
        @Pc(13) int[] local13 = new int[local8 * 3];
        @Pc(18) int[] local18 = new int[local8 * 3];
        @Pc(23) int[] local23 = new int[local8 * 3];
        @Pc(28) int[] local28 = new int[local8 * 3];
        @Pc(33) int[] local33 = new int[local8 * 3];
        @Pc(38) int[] local38 = new int[local8 * 3];
        @Pc(48) int[] local48 = arg3 == null ? null : new int[local8 * 3];
        @Pc(58) int[] local58 = arg5 == null ? null : new int[local8 * 3];
        @Pc(60) int local60 = 0;
        for (@Pc(62) int local62 = 0; local62 < local8; local62++) {
            @Pc(68) int local68 = arg6[local62];
            @Pc(72) int local72 = arg7[local62];
            @Pc(76) int local76 = arg8[local62];
            local13[local60] = arg2[local68];
            local18[local60] = arg4[local68];
            local23[local60] = arg9[local62];
            local33[local60] = arg11[local62];
            local38[local60] = arg12[local62];
            local28[local60] = arg10 == null ? arg9[local62] : arg10[local62];
            if (arg3 != null) {
                local48[local60] = arg3[local68];
            }
            if (arg5 != null) {
                local58[local60] = arg5[local68];
            }
            local60++;
            local13[local60] = arg2[local72];
            local18[local60] = arg4[local72];
            local23[local60] = arg9[local62];
            local33[local60] = arg11[local62];
            local38[local60] = arg12[local62];
            local28[local60] = arg10 == null ? arg9[local62] : arg10[local62];
            if (arg3 != null) {
                local48[local60] = arg3[local72];
            }
            if (arg5 != null) {
                local58[local60] = arg5[local72];
            }
            local60++;
            local13[local60] = arg2[local76];
            local18[local60] = arg4[local76];
            local23[local60] = arg9[local62];
            local33[local60] = arg11[local62];
            local38[local60] = arg12[local62];
            local28[local60] = arg10 == null ? arg9[local62] : arg10[local62];
            if (arg3 != null) {
                local48[local60] = arg3[local76];
            }
            if (arg5 != null) {
                local58[local60] = arg5[local76];
            }
            local60++;
        }
        this.U(arg0, arg1, local13, local48, local18, local58, local23, local28, local33, local38, arg13, arg14, arg15, false);
    }
}

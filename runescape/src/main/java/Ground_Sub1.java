import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.Node;
import com.jagex.graphics.Ground;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.skybox.SkyBoxSphere;
import com.jagex.math.ColourUtils;
import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeapBuffer;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!iga")
public final class Ground_Sub1 extends Ground {

    @OriginalMember(owner = "client!iga", name = "L", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray4;

    @OriginalMember(owner = "client!iga", name = "ob", descriptor = "I")
    public int anInt4316;

    @OriginalMember(owner = "client!iga", name = "lb", descriptor = "I")
    public int lb;

    @OriginalMember(owner = "client!iga", name = "bb", descriptor = "I")
    public int anInt4321;

    @OriginalMember(owner = "client!iga", name = "sb", descriptor = "Lclient!mk;")
    public Class244 aClass244_8;

    @OriginalMember(owner = "client!iga", name = "ib", descriptor = "Lclient!mg;")
    public Interface16 anInterface16_5;

    @OriginalMember(owner = "client!iga", name = "K", descriptor = "Lclient!mg;")
    public Interface16 anInterface16_6;

    @OriginalMember(owner = "client!iga", name = "G", descriptor = "I")
    public int anInt4322;

    @OriginalMember(owner = "client!iga", name = "R", descriptor = "[Lclient!wr;")
    public Node_Sub58[] aClass2_Sub58Array1;

    @OriginalMember(owner = "client!iga", name = "rb", descriptor = "F")
    public float aFloat76 = -3.4028235E38F;

    @OriginalMember(owner = "client!iga", name = "gb", descriptor = "F")
    public float aFloat77 = Float.MAX_VALUE;

    @OriginalMember(owner = "client!iga", name = "J", descriptor = "Lclient!sia;")
    public final Deque aDeque_24 = new Deque();

    @OriginalMember(owner = "client!iga", name = "qb", descriptor = "Lclient!am;")
    public final NativeToolkit aClass19_Sub1_9;

    @OriginalMember(owner = "client!iga", name = "N", descriptor = "I")
    public final int anInt4308;

    @OriginalMember(owner = "client!iga", name = "kb", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray6;

    @OriginalMember(owner = "client!iga", name = "P", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray1;

    @OriginalMember(owner = "client!iga", name = "B", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray5;

    @OriginalMember(owner = "client!iga", name = "tb", descriptor = "[[B")
    public byte[][] aByteArrayArray13;

    @OriginalMember(owner = "client!iga", name = "O", descriptor = "I")
    public final int anInt4303;

    @OriginalMember(owner = "client!iga", name = "I", descriptor = "[[S")
    public final short[][] aShortArrayArray4;

    @OriginalMember(owner = "client!iga", name = "T", descriptor = "[[F")
    public float[][] aFloatArrayArray4;

    @OriginalMember(owner = "client!iga", name = "W", descriptor = "[[F")
    public float[][] aFloatArrayArray3;

    @OriginalMember(owner = "client!iga", name = "pb", descriptor = "[[B")
    public final byte[][] aByteArrayArray12;

    @OriginalMember(owner = "client!iga", name = "Q", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray2;

    @OriginalMember(owner = "client!iga", name = "mb", descriptor = "I")
    public final int anInt4302;

    @OriginalMember(owner = "client!iga", name = "ub", descriptor = "[[[I")
    public int[][][] anIntArrayArrayArray3;

    @OriginalMember(owner = "client!iga", name = "H", descriptor = "[[[Lclient!wr;")
    public Node_Sub58[][][] aClass2_Sub58ArrayArrayArray1;

    @OriginalMember(owner = "client!iga", name = "eb", descriptor = "[[F")
    public float[][] aFloatArrayArray5;

    @OriginalMember(owner = "client!iga", name = "V", descriptor = "Lclient!av;")
    public IterableHashTable aIterableHashTable_21;

    @OriginalMember(owner = "client!iga", name = "Y", descriptor = "Lclient!pk;")
    public Class293 aClass293_1;

    @OriginalMember(owner = "client!iga", name = "<init>", descriptor = "(Lclient!am;IIII[[I[[II)V")
    public Ground_Sub1(@OriginalArg(0) NativeToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int[][] arg5, @OriginalArg(6) int[][] arg6, @OriginalArg(7) int arg7) {
        super(arg3, arg4, arg7, arg5);
        this.aClass19_Sub1_9 = arg0;
        this.anInt4308 = super.tileSizeShift - 2;
        this.anIntArrayArrayArray6 = new int[arg3][arg4][];
        this.anIntArrayArrayArray1 = new int[arg3][arg4][];
        this.anIntArrayArrayArray5 = new int[arg3][arg4][];
        this.aByteArrayArray13 = new byte[arg3 + 1][arg4 + 1];
        this.anInt4303 = 0x1 << this.anInt4308;
        this.aShortArrayArray4 = new short[arg4 * arg3][];
        this.aFloatArrayArray4 = new float[super.sizeX + 1][super.sizeZ + 1];
        this.aFloatArrayArray3 = new float[super.sizeX + 1][super.sizeZ + 1];
        this.aByteArrayArray12 = new byte[arg3][arg4];
        this.anIntArrayArrayArray2 = new int[arg3][arg4][];
        this.anInt4302 = arg2;
        this.anIntArrayArrayArray3 = new int[arg3][arg4][];
        this.aClass2_Sub58ArrayArrayArray1 = new Node_Sub58[arg3][arg4][];
        this.aFloatArrayArray5 = new float[super.sizeX + 1][super.sizeZ + 1];
        for (@Pc(121) int local121 = 0; local121 <= super.sizeZ; local121++) {
            for (@Pc(127) int local127 = 0; local127 <= super.sizeX; local127++) {
                @Pc(136) int local136 = super.tileHeights[local127][local121];
                if (this.aFloat77 > (float) local136) {
                    this.aFloat77 = (float) local136;
                }
                if ((float) local136 > this.aFloat76) {
                    this.aFloat76 = (float) local136;
                }
                if (local127 > 0 && local121 > 0 && local127 < super.sizeX && local121 < super.sizeZ) {
                    @Pc(202) int local202 = arg6[local127 + 1][local121] - arg6[local127 - 1][local121];
                    @Pc(218) int local218 = arg6[local127][local121 + 1] - arg6[local127][local121 - 1];
                    @Pc(237) float local237 = (float) (1.0D / Math.sqrt(local218 * local218 + local202 * local202 + arg7 * 4 * arg7));
                    this.aFloatArrayArray3[local127][local121] = local237 * (float) local202;
                    this.aFloatArrayArray4[local127][local121] = local237 * (float) (-arg7 * 2);
                    this.aFloatArrayArray5[local127][local121] = local237 * (float) local218;
                }
            }
        }
        this.aFloat77--;
        this.aFloat76++;
        this.aIterableHashTable_21 = new IterableHashTable(128);
        if ((this.anInt4302 & 0x10) != 0) {
            this.aClass293_1 = new Class293(this.aClass19_Sub1_9, this);
        }
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(III[[ZZI)V")
    @Override
    public void renderTiles(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean[][] arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5) {
        this.method3885(arg3, arg4, arg1, -1, arg2, arg5, arg0);
    }

    @OriginalMember(owner = "client!iga", name = "U", descriptor = "(II[I[I[I[I[I[I[I[IIIIZ)V")
    @Override
    public void U(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int[] arg4, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int[] arg7, @OriginalArg(8) int[] arg8, @OriginalArg(9) int[] arg9, @OriginalArg(10) int arg10, @OriginalArg(11) int arg11, @OriginalArg(12) int arg12, @OriginalArg(13) boolean arg13) {
        if (arg5 != null && this.anIntArrayArrayArray4 == null) {
            this.anIntArrayArrayArray4 = new int[super.sizeX][super.sizeZ][];
        }
        if (arg3 != null && this.anIntArrayArrayArray2 == null) {
            this.anIntArrayArrayArray2 = new int[super.sizeX][super.sizeZ][];
        }
        @Pc(41) TextureSource local41 = this.aClass19_Sub1_9.textureSource;
        this.anIntArrayArrayArray1[arg0][arg1] = arg2;
        this.anIntArrayArrayArray3[arg0][arg1] = arg4;
        this.anIntArrayArrayArray5[arg0][arg1] = arg6;
        this.anIntArrayArrayArray6[arg0][arg1] = arg7;
        if (this.anIntArrayArrayArray4 != null) {
            this.anIntArrayArrayArray4[arg0][arg1] = arg5;
        }
        if (this.anIntArrayArrayArray2 != null) {
            this.anIntArrayArrayArray2[arg0][arg1] = arg3;
        }
        @Pc(104) Node_Sub58[] local104 = this.aClass2_Sub58ArrayArrayArray1[arg0][arg1] = new Node_Sub58[arg6.length];
        for (@Pc(106) int local106 = 0; local106 < arg6.length; local106++) {
            @Pc(114) int local114 = arg8[local106];
            @Pc(118) int local118 = arg9[local106];
            if ((this.anInt4302 & 0x20) != 0 && local114 != -1 && local41.getMetrics(local114).disableable) {
                local118 = 128;
                local114 = -1;
            }
            @Pc(167) long local167 = (long) local114 | (long) arg10 << 28 | (long) arg12 << 48 | (long) arg11 << 42 | (long) (local118 << 14);
            @Pc(173) Node local173;
            for (local173 = this.aIterableHashTable_21.get(local167); local173 != null; local173 = this.aIterableHashTable_21.nextWithSameKey()) {
                @Pc(180) Node_Sub58 local180 = (Node_Sub58) local173;
                if (local180.anInt10870 == local114 && (float) local118 == local180.aFloat219 && local180.anInt10863 == arg10 && arg11 == local180.anInt10860 && arg12 == local180.anInt10868) {
                    break;
                }
            }
            if (local173 == null) {
                local104[local106] = new Node_Sub58(this, local114, local118, arg10, arg11, arg12);
                this.aIterableHashTable_21.put(local167, local104[local106]);
            } else {
                local104[local106] = (Node_Sub58) local173;
            }
        }
        if (arg13) {
            this.aByteArrayArray12[arg0][arg1] = (byte) (this.aByteArrayArray12[arg0][arg1] | 0x1);
        }
        if (this.anInt4321 < arg6.length) {
            this.anInt4321 = arg6.length;
        }
        this.anInt4322 += arg6.length;
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(III[[ZZII)V")
    @Override
    public void renderTilesAtDepth(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean[][] arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        this.method3885(arg3, arg4, arg1, arg5, arg2, arg6, arg0);
    }

    @OriginalMember(owner = "client!iga", name = "ka", descriptor = "(III)V")
    @Override
    public void ka(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if ((this.aByteArrayArray13[arg0][arg1] & 0xFF) < arg2) {
            this.aByteArrayArray13[arg0][arg1] = (byte) arg2;
        }
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "([[ZZIIIIII)V")
    public void method3885(@OriginalArg(0) boolean[][] arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6) {
        if (this.aClass2_Sub58Array1 == null) {
            return;
        }
        @Pc(27) int local27 = arg4 + arg4 + 1;
        @Pc(31) int local31 = local27 * local27;
        if (Static80.anIntArray153.length < local31) {
            Static80.anIntArray153 = new int[local31];
        }
        @Pc(49) int local49 = arg6 - arg4;
        @Pc(51) int local51 = local49;
        if (local49 < 0) {
            local49 = 0;
        }
        @Pc(60) int local60 = arg2 - arg4;
        @Pc(62) int local62 = local60;
        if (local60 < 0) {
            local60 = 0;
        }
        @Pc(70) int local70 = arg4 + arg6;
        if (super.sizeX - 1 < local70) {
            local70 = super.sizeX - 1;
        }
        @Pc(85) int local85 = arg2 + arg4;
        Static236.anInt3893 = 0;
        if (super.sizeZ - 1 < local85) {
            local85 = super.sizeZ - 1;
        }
        for (@Pc(100) int local100 = local49; local100 <= local70; local100++) {
            @Pc(109) boolean[] local109 = arg0[local100 - local51];
            for (@Pc(111) int local111 = local60; local111 <= local85; local111++) {
                if (local109[local111 - local62]) {
                    Static80.anIntArray153[Static236.anInt3893++] = super.sizeX * local111 + local100;
                }
            }
        }
        if (arg3 == -1) {
            this.aClass19_Sub1_9.method8044();
        } else {
            this.aClass19_Sub1_9.method8046((float) arg3);
            this.aClass19_Sub1_9.method8111();
        }
        this.aClass19_Sub1_9.method8096();
        this.aClass19_Sub1_9.method8072((this.anInt4302 & 0x7) != 0);
        this.aClass19_Sub1_9.method8108(false, -1, false);
        this.aClass19_Sub1_9.method8130(0, this.anInterface16_5);
        for (@Pc(219) int local219 = 0; local219 < this.aClass2_Sub58Array1.length; local219++) {
            this.aClass2_Sub58Array1[local219].method9400(Static80.anIntArray153, Static236.anInt3893);
        }
        @Pc(246) Matrix_Sub1 local246 = this.aClass19_Sub1_9.method8040();
        local246.applyTranslation(0, -1, 0);
        this.aClass19_Sub1_9.method8042();
        if (!this.aDeque_24.isEmpty()) {
            @Pc(268) int local268 = this.aClass19_Sub1_9.anInt9146;
            @Pc(272) int local272 = this.aClass19_Sub1_9.anInt9175;
            this.aClass19_Sub1_9.L(0, local272, this.aClass19_Sub1_9.anInt9185);
            this.aClass19_Sub1_9.method8072(false);
            this.aClass19_Sub1_9.method8083(false);
            this.aClass19_Sub1_9.method8053(128);
            this.aClass19_Sub1_9.method8108(false, -2, false);
            this.aClass19_Sub1_9.method8088(this.aClass19_Sub1_9.anInterface17_3);
            this.aClass19_Sub1_9.method8094(Static185.aClass121_3, Static209.aClass121_4);
            this.aClass19_Sub1_9.method8080(0, Static454.aClass168_5);
            this.aClass19_Sub1_9.method8142(Static207.aClass168_4, 0);
            for (@Pc(342) Node local342 = this.aDeque_24.first(); local342 != null; local342 = this.aDeque_24.next()) {
                @Pc(347) Node_Sub32 local347 = (Node_Sub32) local342;
                local347.method4564(arg6, arg2, (byte) 51, arg4, arg0);
            }
            this.aClass19_Sub1_9.method8080(0, Static189.aClass168_2);
            this.aClass19_Sub1_9.method8142(Static189.aClass168_2, 0);
            this.aClass19_Sub1_9.method8088(null);
            this.aClass19_Sub1_9.L(local268, local272, this.aClass19_Sub1_9.anInt9185);
        }
        if (this.aClass293_1 == null) {
            return;
        }
        this.aClass19_Sub1_9.method8130(0, this.anInterface16_5);
        this.aClass19_Sub1_9.method8130(1, this.anInterface16_6);
        this.aClass19_Sub1_9.method8114(this.aClass244_8);
        this.aClass293_1.method6605(arg0, arg4, arg2, arg6, arg1);
        return;
    }

    @OriginalMember(owner = "client!iga", name = "YA", descriptor = "()V")
    @Override
    public void YA() {
        if (this.anInt4322 <= 0) {
            this.aClass293_1 = null;
        } else {
            @Pc(28) byte[][] local28 = new byte[super.sizeX + 1][super.sizeZ + 1];
            for (@Pc(30) int local30 = 1; local30 < super.sizeX; local30++) {
                for (@Pc(36) int local36 = 1; local36 < super.sizeZ; local36++) {
                    local28[local30][local36] = (byte) ((this.aByteArrayArray13[local30][local36 + 1] >> 3) + (this.aByteArrayArray13[local30 - 1][local36] >> 2) + (this.aByteArrayArray13[local30 - -1][local36] >> 3) + (this.aByteArrayArray13[local30][local36 - 1] >> 2) + (this.aByteArrayArray13[local30][local36] >> 1));
                }
            }
            @Pc(122) Node_Sub58[] local122 = new Node_Sub58[this.aIterableHashTable_21.size()];
            this.aIterableHashTable_21.copyTo(local122);
            for (@Pc(132) int local132 = 0; local132 < local122.length; local132++) {
                local122[local132].method9399(this.anInt4322);
            }
            @Pc(154) int local154 = 20;
            if (this.anIntArrayArrayArray4 != null) {
                local154 += 4;
            }
            if ((this.anInt4302 & 0x7) != 0) {
                local154 += 12;
            }
            @Pc(183) NativeHeapBuffer local183 = this.aClass19_Sub1_9.aNativeHeap5.a(this.anInt4322 * 4, false);
            @Pc(193) NativeHeapBuffer local193 = this.aClass19_Sub1_9.aNativeHeap5.a(this.anInt4322 * local154, false);
            @Pc(198) Stream local198 = new Stream(local193);
            @Pc(203) Stream local203 = new Stream(local183);
            @Pc(207) Node_Sub58[] local207 = new Node_Sub58[this.anInt4322];
            @Pc(214) int local214 = SkyBoxSphere.method5587(this.anInt4322 / 4);
            if (local214 < 1) {
                local214 = 1;
            }
            @Pc(224) IterableHashTable local224 = new IterableHashTable(local214);
            @Pc(228) Node_Sub58[] local228 = new Node_Sub58[this.anInt4321];
            @Pc(234) int local234;
            for (@Pc(230) int local230 = 0; super.sizeX > local230; local230++) {
                for (local234 = 0; super.sizeZ > local234; local234++) {
                    if (this.anIntArrayArrayArray5[local230][local234] != null) {
                        @Pc(252) Node_Sub58[] local252 = this.aClass2_Sub58ArrayArrayArray1[local230][local234];
                        @Pc(259) int[] local259 = this.anIntArrayArrayArray1[local230][local234];
                        @Pc(266) int[] local266 = this.anIntArrayArrayArray3[local230][local234];
                        @Pc(273) int[] local273 = this.anIntArrayArrayArray6[local230][local234];
                        @Pc(280) int[] local280 = this.anIntArrayArrayArray5[local230][local234];
                        @Pc(293) int[] local293 = this.anIntArrayArrayArray2 == null ? null : this.anIntArrayArrayArray2[local230][local234];
                        if (local273 == null) {
                            local273 = local280;
                        }
                        @Pc(310) int[] local310 = this.anIntArrayArrayArray4 == null ? null : this.anIntArrayArrayArray4[local230][local234];
                        @Pc(317) float local317 = this.aFloatArrayArray3[local230][local234];
                        @Pc(324) float local324 = this.aFloatArrayArray4[local230][local234];
                        @Pc(331) float local331 = this.aFloatArrayArray5[local230][local234];
                        @Pc(340) float local340 = this.aFloatArrayArray3[local230][local234 + 1];
                        @Pc(349) float local349 = this.aFloatArrayArray4[local230][local234 + 1];
                        @Pc(358) float local358 = this.aFloatArrayArray5[local230][local234 + 1];
                        @Pc(369) float local369 = this.aFloatArrayArray3[local230 + 1][local234 + 1];
                        @Pc(380) float local380 = this.aFloatArrayArray4[local230 + 1][local234 + 1];
                        @Pc(391) float local391 = this.aFloatArrayArray5[local230 + 1][local234 + 1];
                        @Pc(400) float local400 = this.aFloatArrayArray3[local230 + 1][local234];
                        @Pc(409) float local409 = this.aFloatArrayArray4[local230 + 1][local234];
                        @Pc(418) float local418 = this.aFloatArrayArray5[local230 + 1][local234];
                        @Pc(426) int local426 = local28[local230][local234] & 0xFF;
                        @Pc(436) int local436 = local28[local230][local234 + 1] & 0xFF;
                        @Pc(448) int local448 = local28[local230 + 1][local234 + 1] & 0xFF;
                        @Pc(458) int local458 = local28[local230 + 1][local234] & 0xFF;
                        @Pc(460) int local460 = 0;
                        @Pc(472) int local472;
                        label355:
                        for (@Pc(462) int local462 = 0; local462 < local280.length; local462++) {
                            @Pc(470) Node_Sub58 local470 = local252[local462];
                            for (local472 = 0; local472 < local460; local472++) {
                                if (local470 == local228[local472]) {
                                    continue label355;
                                }
                            }
                            local228[local460++] = local470;
                        }
                        @Pc(521) short[] local521 = this.aShortArrayArray4[local230 + super.sizeX * local234] = new short[local280.length];
                        for (local472 = 0; local472 < local280.length; local472++) {
                            @Pc(535) int local535 = (local230 << super.tileSizeShift) + local259[local472];
                            @Pc(545) int local545 = (local234 << super.tileSizeShift) + local266[local472];
                            @Pc(550) int local550 = local535 >> this.anInt4308;
                            @Pc(555) int local555 = local545 >> this.anInt4308;
                            @Pc(559) int local559 = local280[local472];
                            @Pc(563) int local563 = local273[local472];
                            @Pc(572) int local572 = local293 == null ? 0 : local293[local472];
                            @Pc(590) long local590 = (long) local563 << 48 | (long) local559 << 32 | (long) (local550 << 16) | (long) local555;
                            @Pc(594) int local594 = local259[local472];
                            @Pc(598) int local598 = local266[local472];
                            @Pc(600) byte local600 = 74;
                            @Pc(602) int local602 = 0;
                            @Pc(604) float local604 = 1.0F;
                            @Pc(622) float local622;
                            @Pc(618) float local618;
                            @Pc(620) float local620;
                            @Pc(724) float local724;
                            @Pc(626) int local626;
                            if (local594 == 0 && local598 == 0) {
                                local618 = local324;
                                local620 = local331;
                                local622 = local317;
                                local626 = local600 - local426;
                            } else if (local594 == 0 && super.tileSize == local598) {
                                local618 = local349;
                                local626 = local600 - local436;
                                local622 = local340;
                                local620 = local358;
                            } else if (super.tileSize == local594 && super.tileSize == local598) {
                                local618 = local380;
                                local622 = local369;
                                local620 = local391;
                                local626 = local600 - local448;
                            } else if (local594 == super.tileSize && local598 == 0) {
                                local620 = local418;
                                local626 = local600 - local458;
                                local618 = local409;
                                local622 = local400;
                            } else {
                                @Pc(699) float local699 = (float) local594 / (float) super.tileSize;
                                @Pc(706) float local706 = (float) local598 / (float) super.tileSize;
                                @Pc(715) float local715 = (local400 - local317) * local699 + local317;
                                local724 = local699 * (local409 - local324) + local324;
                                @Pc(732) float local732 = local699 * (local418 - local331) + local331;
                                @Pc(741) float local741 = local340 + local699 * (local369 - local340);
                                @Pc(749) float local749 = local699 * (local380 - local349) + local349;
                                local618 = local724 + (local749 - local724) * local706;
                                @Pc(766) float local766 = (local391 - local358) * local699 + local358;
                                local622 = (local741 - local715) * local706 + local715;
                                local620 = local732 + local706 * (local766 - local732);
                                @Pc(796) int local796 = (local594 * (local458 - local426) >> super.tileSizeShift) + local426;
                                @Pc(808) int local808 = local436 + ((local448 - local436) * local594 >> super.tileSizeShift);
                                local626 = local600 - local796 - (local598 * (local808 - local796) >> super.tileSizeShift);
                            }
                            if (local559 != -1) {
                                @Pc(846) int local846 = local626 * (local559 & 0x7F) >> 7;
                                if (local846 < 2) {
                                    local846 = 2;
                                } else if (local846 > 126) {
                                    local846 = 126;
                                }
                                local602 = ColourUtils.HSL_TO_RGB[local559 & 0xFF80 | local846];
                                if ((this.anInt4302 & 0x7) == 0) {
                                    local604 = this.aClass19_Sub1_9.aFloatArray60[0] * local622 + local618 * this.aClass19_Sub1_9.aFloatArray60[1] + this.aClass19_Sub1_9.aFloatArray60[2] * local620;
                                    local604 = local604 * (local604 > 0.0F ? this.aClass19_Sub1_9.aFloat181 : this.aClass19_Sub1_9.aFloat180) + this.aClass19_Sub1_9.aFloat186;
                                }
                            }
                            @Pc(923) Node local923 = null;
                            if ((this.anInt4303 - 1 & local535) == 0 && (this.anInt4303 - 1 & local545) == 0) {
                                local923 = local224.get(local590);
                            }
                            @Pc(964) int local964;
                            @Pc(1004) int local1004;
                            if (local923 == null) {
                                if (local563 == local559) {
                                    local1004 = local602;
                                } else {
                                    @Pc(1014) int local1014 = (local563 & 0x7F) * local626 >> 7;
                                    if (local1014 < 2) {
                                        local1014 = 2;
                                    } else if (local1014 > 126) {
                                        local1014 = 126;
                                    }
                                    local1004 = ColourUtils.HSL_TO_RGB[local1014 | local563 & 0xFF80];
                                    if ((this.anInt4302 & 0x7) == 0) {
                                        local724 = this.aClass19_Sub1_9.aFloatArray60[2] * local620 + this.aClass19_Sub1_9.aFloatArray60[1] * local618 + local622 * this.aClass19_Sub1_9.aFloatArray60[0];
                                        local724 = this.aClass19_Sub1_9.aFloat186 + (local604 > 0.0F ? this.aClass19_Sub1_9.aFloat181 : this.aClass19_Sub1_9.aFloat180) * local604;
                                        @Pc(1090) int local1090 = local1004 >> 16 & 0xFF;
                                        @Pc(1096) int local1096 = local1004 >> 8 & 0xFF;
                                        local1090 = (int) ((float) local1090 * local724);
                                        @Pc(1106) int local1106 = local1004 & 0xFF;
                                        if (local1090 < 0) {
                                            local1090 = 0;
                                        } else if (local1090 > 255) {
                                            local1090 = 255;
                                        }
                                        local1096 = (int) ((float) local1096 * local724);
                                        if (local1096 < 0) {
                                            local1096 = 0;
                                        } else if (local1096 > 255) {
                                            local1096 = 255;
                                        }
                                        local1106 = (int) ((float) local1106 * local724);
                                        if (local1106 < 0) {
                                            local1106 = 0;
                                        } else if (local1106 > 255) {
                                            local1106 = 255;
                                        }
                                        local1004 = local1106 | local1090 << 16 | local1096 << 8;
                                    }
                                }
                                if (Stream.b()) {
                                    local198.a((float) local535);
                                    local198.a((float) (local572 + this.averageHeight(local535, local545)));
                                    local198.a((float) local545);
                                    local198.a((float) local535);
                                    local198.a((float) local545);
                                    if (this.anIntArrayArrayArray4 != null) {
                                        local198.a((float) (local310 == null ? 0 : local310[local472] - 1));
                                    }
                                    if ((this.anInt4302 & 0x7) != 0) {
                                        local198.a(local622);
                                        local198.a(local618);
                                        local198.a(local620);
                                    }
                                } else {
                                    local198.b((float) local535);
                                    local198.b((float) (this.averageHeight(local535, local545) + local572));
                                    local198.b((float) local545);
                                    local198.b((float) local535);
                                    local198.b((float) local545);
                                    if (this.anIntArrayArrayArray4 != null) {
                                        local198.b((float) (local310 == null ? 0 : local310[local472] - 1));
                                    }
                                    if ((this.anInt4302 & 0x7) != 0) {
                                        local198.b(local622);
                                        local198.b(local618);
                                        local198.b(local620);
                                    }
                                }
                                if (this.aClass19_Sub1_9.anInt9178 == 0) {
                                    local203.a(local1004 | 0xFF000000);
                                } else {
                                    local203.e(local1004 | 0xFF000000);
                                }
                                local964 = this.lb++;
                                local521[local472] = (short) local964;
                                if (local559 != -1) {
                                    local207[local964] = local252[local472];
                                }
                                local224.put(local590, new Node_Sub50(local521[local472]));
                            } else {
                                local521[local472] = ((Node_Sub50) local923).aShort119;
                                local964 = local521[local472] & 0xFFFF;
                                if (local559 != -1 && local207[local964].key > local252[local472].key) {
                                    local207[local964] = local252[local472];
                                }
                            }
                            for (local1004 = 0; local1004 < local460; local1004++) {
                                local228[local1004].method9404(local602, local626, local604, local964);
                            }
                            this.anInt4316++;
                        }
                    }
                }
            }
            for (local234 = 0; local234 < this.lb; local234++) {
                @Pc(1423) Node_Sub58 local1423 = local207[local234];
                if (local1423 != null) {
                    local1423.method9395(local234);
                }
            }
            @Pc(1444) int local1444;
            @Pc(1462) int local1462;
            for (@Pc(1438) int local1438 = 0; super.sizeX > local1438; local1438++) {
                for (local1444 = 0; local1444 < super.sizeZ; local1444++) {
                    @Pc(1456) short[] local1456 = this.aShortArrayArray4[local1444 * super.sizeX + local1438];
                    if (local1456 != null) {
                        @Pc(1460) int local1460 = 0;
                        local1462 = 0;
                        while (local1456.length > local1462) {
                            @Pc(1471) int local1471 = local1456[local1462++] & 0xFFFF;
                            @Pc(1478) int local1478 = local1456[local1462++] & 0xFFFF;
                            @Pc(1485) int local1485 = local1456[local1462++] & 0xFFFF;
                            @Pc(1489) Node_Sub58 local1489 = local207[local1471];
                            @Pc(1493) Node_Sub58 local1493 = local207[local1478];
                            @Pc(1497) Node_Sub58 local1497 = local207[local1485];
                            @Pc(1499) Node_Sub58 local1499 = null;
                            if (local1489 != null) {
                                local1499 = local1489;
                                local1489.method9401(local1438, local1460, local1444);
                            }
                            if (local1493 != null) {
                                local1493.method9401(local1438, local1460, local1444);
                                if (local1499 == null || local1493.key < local1499.key) {
                                    local1499 = local1493;
                                }
                            }
                            if (local1497 != null) {
                                local1497.method9401(local1438, local1460, local1444);
                                if (local1499 == null || local1499.key > local1497.key) {
                                    local1499 = local1497;
                                }
                            }
                            if (local1499 != null) {
                                if (local1489 != null) {
                                    local1499.method9395(local1471);
                                }
                                if (local1493 != null) {
                                    local1499.method9395(local1478);
                                }
                                if (local1497 != null) {
                                    local1499.method9395(local1485);
                                }
                                local1499.method9401(local1438, local1460, local1444);
                            }
                            local1460++;
                        }
                    }
                }
            }
            local198.c();
            local203.c();
            this.anInterface16_6 = this.aClass19_Sub1_9.method8156(false);
            this.anInterface16_6.method3152(4, this.lb * 4, local183);
            this.anInterface16_5 = this.aClass19_Sub1_9.method8156(false);
            this.anInterface16_5.method3152(local154, this.lb * local154, local193);
            if ((this.anInt4302 & 0x7) == 0) {
                if (this.anIntArrayArrayArray4 == null) {
                    this.aClass244_8 = this.aClass19_Sub1_9.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_5}), new Class237(Static231.aClass157_3)});
                } else {
                    this.aClass244_8 = this.aClass19_Sub1_9.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_5, Static231.aClass157_4}), new Class237(Static231.aClass157_3)});
                }
            } else if (this.anIntArrayArrayArray4 == null) {
                this.aClass244_8 = this.aClass19_Sub1_9.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_5, Static231.aClass157_2}), new Class237(Static231.aClass157_3)});
            } else {
                this.aClass244_8 = this.aClass19_Sub1_9.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_5, Static231.aClass157_4, Static231.aClass157_2}), new Class237(Static231.aClass157_3)});
            }
            local1444 = 0;
            for (@Pc(1827) int local1827 = 0; local1827 < local122.length; local1827++) {
                if (local122[local1827].anInt10871 > 0) {
                    local122[local1444++] = local122[local1827];
                }
            }
            this.aClass2_Sub58Array1 = new Node_Sub58[local1444];
            @Pc(1861) long[] local1861 = new long[local1444];
            for (local1462 = 0; local1462 < local1444; local1462++) {
                @Pc(1871) Node_Sub58 local1871 = local122[local1462];
                local1861[local1462] = local1871.key;
                this.aClass2_Sub58Array1[local1462] = local1871;
                local1871.method9403(this.lb);
            }
            Static61.method1308(local1861, this.aClass2_Sub58Array1);
            if (this.aClass293_1 != null) {
                this.aClass293_1.method6611();
            }
        }
        this.aClass2_Sub58ArrayArrayArray1 = null;
        this.anIntArrayArrayArray4 = null;
        this.aByteArrayArray13 = null;
        this.anIntArrayArrayArray1 = this.anIntArrayArrayArray3 = null;
        this.anIntArrayArrayArray5 = null;
        this.anIntArrayArrayArray6 = null;
        this.aFloatArrayArray3 = this.aFloatArrayArray4 = this.aFloatArrayArray5 = null;
        this.anIntArrayArrayArray2 = null;
        this.aIterableHashTable_21 = null;
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(II)V")
    @Override
    public void renderTile(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(Lclient!lca;[I)V")
    @Override
    public void method7868(@OriginalArg(0) PointLight arg0, @OriginalArg(1) int[] arg1) {
        this.aDeque_24.addLast(new Node_Sub32(this.aClass19_Sub1_9, this, arg0, arg1));
    }

    @OriginalMember(owner = "client!iga", name = "wa", descriptor = "(Lclient!r;IIIIZ)V")
    @Override
    public void wa(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5) {
        if (this.aClass293_1 != null && arg0 != null) {
            @Pc(27) int local27 = arg1 - (arg2 * this.aClass19_Sub1_9.anInt9168 >> 8) >> this.aClass19_Sub1_9.anInt9173;
            @Pc(42) int local42 = arg3 - (this.aClass19_Sub1_9.anInt9149 * arg2 >> 8) >> this.aClass19_Sub1_9.anInt9173;
            this.aClass293_1.method6607(arg0, local27, local42);
        }
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(IIIIIII[[Z)V")
    @Override
    public void drawMinimap(@OriginalArg(3) int x1, @OriginalArg(4) int y1, @OriginalArg(5) int x2, @OriginalArg(6) int y2, @OriginalArg(7) boolean[][] visibility) {
        if (this.anInt4322 <= 0) {
            return;
        }
        @Pc(20) Interface20 local20 = this.aClass19_Sub1_9.method8074(this.anInt4316);
        @Pc(22) int local22 = 0;
        @Pc(24) int local24 = 32767;
        @Pc(26) int local26 = -32768;
        for (@Pc(28) int local28 = 0; local28 < 4; local28++) {
            local22 = 0;
            @Pc(39) Buffer local39 = local20.method8547();
            if (local39 != null) {
                @Pc(47) Stream local47 = this.aClass19_Sub1_9.method8100(local39);
                @Pc(51) int local51;
                @Pc(60) int local60;
                @Pc(62) int local62;
                @Pc(83) short[] local83;
                @Pc(87) int local87;
                @Pc(95) int local95;
                if (Stream.b()) {
                    for (local51 = y1; local51 < y2; local51++) {
                        local60 = x1 + local51 * super.sizeX;
                        for (local62 = x1; local62 < x2; local62++) {
                            if (visibility[local62 - x1][local51 - y1]) {
                                local83 = this.aShortArrayArray4[local60];
                                if (local83 != null) {
                                    for (local87 = 0; local87 < local83.length; local87++) {
                                        local95 = local83[local87] & 0xFFFF;
                                        if (local95 > local26) {
                                            local26 = local95;
                                        }
                                        local22++;
                                        if (local24 > local95) {
                                            local24 = local95;
                                        }
                                        local47.b(local95);
                                    }
                                }
                            }
                            local60++;
                        }
                    }
                } else {
                    for (local51 = y1; local51 < y2; local51++) {
                        local60 = x1 + super.sizeX * local51;
                        for (local62 = x1; local62 < x2; local62++) {
                            if (visibility[local62 - x1][local51 - y1]) {
                                local83 = this.aShortArrayArray4[local60];
                                if (local83 != null) {
                                    for (local87 = 0; local87 < local83.length; local87++) {
                                        local95 = local83[local87] & 0xFFFF;
                                        local47.c(local95);
                                        local22++;
                                        if (local95 > local26) {
                                            local26 = local95;
                                        }
                                        if (local24 > local95) {
                                            local24 = local95;
                                        }
                                    }
                                }
                            }
                            local60++;
                        }
                    }
                }
                local47.c();
                if (local20.method8546()) {
                    break;
                }
            }
        }
        if (local22 <= 0) {
            return;
        }
        this.aClass19_Sub1_9.method8152();
        this.aClass19_Sub1_9.method8147(false);
        this.aClass19_Sub1_9.method8072(false);
        this.aClass19_Sub1_9.method8123(false);
        this.aClass19_Sub1_9.method8083(false);
        this.aClass19_Sub1_9.method8053(0);
        this.aClass19_Sub1_9.method8108(false, -2, false);
        this.aClass19_Sub1_9.method8088(null);
        @Pc(306) Matrix_Sub1 local306 = this.aClass19_Sub1_9.method8040();
        @Pc(311) float[] local311 = this.aClass19_Sub1_9.method8133();
        local311[13] = 1.0F - ((float) 0 + (float) (y2 * 1024) / 128.0F) / (float) this.aClass19_Sub1_9.anInt9013;
        local311[2] = 0.0F;
        local311[4] = 0.0F;
        local311[9] = 0.0F;
        local311[8] = 0.0F;
        local311[7] = 0.0F;
        local311[11] = 0.0F;
        local311[5] = (float) 1024 / ((float) super.tileSize * 128.0F * (float) this.aClass19_Sub1_9.anInt9013);
        local311[0] = (float) 1024 / ((float) super.tileSize * 128.0F * (float) this.aClass19_Sub1_9.anInt9122);
        local311[12] = -1.0F - ((float) (x1 * 1024) / 128.0F - (float) 0) / (float) this.aClass19_Sub1_9.anInt9122;
        local311[1] = 0.0F;
        local311[14] = -this.aFloat77 / (this.aFloat76 - this.aFloat77);
        local311[6] = 0.0F;
        local311[10] = 1.0F / (this.aFloat76 - this.aFloat77);
        local311[3] = 0.0F;
        local311[15] = 1.0F;
        local306.method1887(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F);
        this.aClass19_Sub1_9.method8115();
        this.aClass19_Sub1_9.method8042();
        if ((this.anInt4302 & 0x7) == 0) {
            this.aClass19_Sub1_9.method8072(false);
        } else {
            this.aClass19_Sub1_9.method8072(true);
            this.aClass19_Sub1_9.method8158();
        }
        this.aClass19_Sub1_9.method8141(false);
        this.aClass19_Sub1_9.method8130(0, this.anInterface16_5);
        this.aClass19_Sub1_9.method8130(1, this.anInterface16_6);
        this.aClass19_Sub1_9.method8114(this.aClass244_8);
        this.aClass19_Sub1_9.method8052(local24, 0, local20, local26 + 1 - local24, Static104.aClass131_2, local22 / 3);
        this.aClass19_Sub1_9.method8141(true);
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(Lclient!r;IIIIZ)Z")
    @Override
    public boolean method7874(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        if (this.aClass293_1 == null || arg0 == null) {
            return false;
        } else {
            @Pc(28) int local28 = arg1 - (this.aClass19_Sub1_9.anInt9168 * arg2 >> 8) >> this.aClass19_Sub1_9.anInt9173;
            @Pc(43) int local43 = arg3 - (arg2 * this.aClass19_Sub1_9.anInt9149 >> 8) >> this.aClass19_Sub1_9.anInt9173;
            return this.aClass293_1.method6608(local28, arg0, local43);
        }
    }

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(II[I[I[I[I[I[I[I[I[I[I[IIIIZ)V")
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

    @OriginalMember(owner = "client!iga", name = "a", descriptor = "(IILclient!dn;I)V")
    public void method3887(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) DoublyLinkedNode_Sub2_Sub9_Sub1 arg2) {
        @Pc(12) int[] local12 = this.anIntArrayArrayArray1[arg0][arg1];
        @Pc(19) int[] local19 = this.anIntArrayArrayArray3[arg0][arg1];
        @Pc(22) int local22 = local12.length;
        if (Static292.anIntArray387.length < local22) {
            Static292.anIntArray387 = new int[local22];
            Static595.anIntArray702 = new int[local22];
        }
        for (@Pc(40) int local40 = 0; local40 < local22; local40++) {
            Static292.anIntArray387[local40] = local12[local40] >> this.aClass19_Sub1_9.anInt9173;
            Static595.anIntArray702[local40] = local19[local40] >> this.aClass19_Sub1_9.anInt9173;
        }
        @Pc(70) int local70 = 0;
        if (6778 != 6778) {
            this.aFloat76 = -0.91155654F;
        }
        while (local22 > local70) {
            @Pc(86) int local86 = Static292.anIntArray387[local70];
            @Pc(91) int local91 = Static595.anIntArray702[local70++];
            @Pc(95) int local95 = Static292.anIntArray387[local70];
            @Pc(100) int local100 = Static595.anIntArray702[local70++];
            @Pc(104) int local104 = Static292.anIntArray387[local70];
            @Pc(109) int local109 = Static595.anIntArray702[local70++];
            if (-((local104 - local95) * (-local91 + local100)) + (local86 - local95) * (local100 + -local109) > 0) {
                arg2.method2197(local104, local95, local86, local100, local91, local109);
            }
        }
    }

    @OriginalMember(owner = "client!iga", name = "CA", descriptor = "(Lclient!r;IIIIZ)V")
    @Override
    public void CA(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5) {
        if (this.aClass293_1 != null && arg0 != null) {
            @Pc(27) int local27 = arg1 - (this.aClass19_Sub1_9.anInt9168 * arg2 >> 8) >> this.aClass19_Sub1_9.anInt9173;
            @Pc(42) int local42 = arg3 - (arg2 * this.aClass19_Sub1_9.anInt9149 >> 8) >> this.aClass19_Sub1_9.anInt9173;
            this.aClass293_1.method6609(arg0, local42, local27);
        }
    }

    @OriginalMember(owner = "client!iga", name = "fa", descriptor = "(IILclient!r;)Lclient!r;")
    @Override
    public Shadow fa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Shadow arg2) {
        if ((this.aByteArrayArray12[arg0][arg1] & 0x1) == 0) {
            return null;
        }
        @Pc(22) int local22 = super.tileSize >> this.aClass19_Sub1_9.anInt9173;
        @Pc(25) DoublyLinkedNode_Sub2_Sub9_Sub1 local25 = (DoublyLinkedNode_Sub2_Sub9_Sub1) arg2;
        @Pc(43) DoublyLinkedNode_Sub2_Sub9_Sub1 local43;
        if (local25 != null && local25.method2192(local22, local22)) {
            local43 = local25;
            local25.method2196();
        } else {
            local43 = new DoublyLinkedNode_Sub2_Sub9_Sub1(this.aClass19_Sub1_9, local22, local22);
        }
        local43.method2194(0, local22, local22, 0);
        this.method3887(arg0, arg1, local43);
        return local43;
    }
}

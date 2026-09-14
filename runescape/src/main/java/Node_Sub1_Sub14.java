import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kg")
public final class Node_Sub1_Sub14 extends TextureOp {

    @OriginalMember(owner = "client!kg", name = "Q", descriptor = "[[I")
    public int[][] anIntArrayArray125;

    @OriginalMember(owner = "client!kg", name = "G", descriptor = "[I")
    public final int[] anIntArray398 = new int[257];

    @OriginalMember(owner = "client!kg", name = "<init>", descriptor = "()V")
    public Node_Sub1_Sub14() {
        super(1, false);
    }

    @OriginalMember(owner = "client!kg", name = "a", descriptor = "(IB)V")
    public void method4893(@OriginalArg(0) int arg0) {
        if (arg0 == 0) {
            return;
        }
        if (arg0 == 1) {
            this.anIntArrayArray125 = new int[2][4];
            this.anIntArrayArray125[0][1] = 0;
            this.anIntArrayArray125[0][3] = 0;
            this.anIntArrayArray125[0][0] = 0;
            this.anIntArrayArray125[0][2] = 0;
            this.anIntArrayArray125[1][3] = 4096;
            this.anIntArrayArray125[1][1] = 4096;
            this.anIntArrayArray125[1][0] = 4096;
            this.anIntArrayArray125[1][2] = 4096;
        } else if (arg0 == 2) {
            this.anIntArrayArray125 = new int[8][4];
            this.anIntArrayArray125[0][3] = 2361;
            this.anIntArrayArray125[0][1] = 2650;
            this.anIntArrayArray125[0][2] = 2602;
            this.anIntArrayArray125[0][0] = 0;
            this.anIntArrayArray125[1][1] = 2313;
            this.anIntArrayArray125[1][3] = 1558;
            this.anIntArrayArray125[1][0] = 2867;
            this.anIntArrayArray125[1][2] = 1799;
            this.anIntArrayArray125[2][3] = 1413;
            this.anIntArrayArray125[2][0] = 3072;
            this.anIntArrayArray125[2][2] = 1734;
            this.anIntArrayArray125[2][1] = 2618;
            this.anIntArrayArray125[3][2] = 1220;
            this.anIntArrayArray125[3][3] = 947;
            this.anIntArrayArray125[3][1] = 2296;
            this.anIntArrayArray125[3][0] = 3276;
            this.anIntArrayArray125[4][2] = 963;
            this.anIntArrayArray125[4][1] = 2072;
            this.anIntArrayArray125[4][3] = 722;
            this.anIntArrayArray125[4][0] = 3481;
            this.anIntArrayArray125[5][1] = 2730;
            this.anIntArrayArray125[5][2] = 2152;
            this.anIntArrayArray125[5][0] = 3686;
            this.anIntArrayArray125[5][3] = 1766;
            this.anIntArrayArray125[6][2] = 1060;
            this.anIntArrayArray125[6][3] = 915;
            this.anIntArrayArray125[6][0] = 3891;
            this.anIntArrayArray125[6][1] = 2232;
            this.anIntArrayArray125[7][2] = 1413;
            this.anIntArrayArray125[7][0] = 4096;
            this.anIntArrayArray125[7][3] = 1140;
            this.anIntArrayArray125[7][1] = 1686;
        } else if (arg0 == 3) {
            this.anIntArrayArray125 = new int[7][4];
            this.anIntArrayArray125[0][2] = 0;
            this.anIntArrayArray125[0][3] = 4096;
            this.anIntArrayArray125[0][1] = 0;
            this.anIntArrayArray125[0][0] = 0;
            this.anIntArrayArray125[1][2] = 4096;
            this.anIntArrayArray125[1][0] = 663;
            this.anIntArrayArray125[1][1] = 0;
            this.anIntArrayArray125[1][3] = 4096;
            this.anIntArrayArray125[2][3] = 0;
            this.anIntArrayArray125[2][0] = 1363;
            this.anIntArrayArray125[2][2] = 4096;
            this.anIntArrayArray125[2][1] = 0;
            this.anIntArrayArray125[3][1] = 4096;
            this.anIntArrayArray125[3][3] = 0;
            this.anIntArrayArray125[3][0] = 2048;
            this.anIntArrayArray125[3][2] = 4096;
            this.anIntArrayArray125[4][0] = 2727;
            this.anIntArrayArray125[4][3] = 0;
            this.anIntArrayArray125[4][2] = 0;
            this.anIntArrayArray125[4][1] = 4096;
            this.anIntArrayArray125[5][2] = 0;
            this.anIntArrayArray125[5][0] = 3411;
            this.anIntArrayArray125[5][3] = 4096;
            this.anIntArrayArray125[5][1] = 4096;
            this.anIntArrayArray125[6][2] = 0;
            this.anIntArrayArray125[6][3] = 4096;
            this.anIntArrayArray125[6][1] = 0;
            this.anIntArrayArray125[6][0] = 4096;
        } else if (arg0 == 4) {
            this.anIntArrayArray125 = new int[6][4];
            this.anIntArrayArray125[0][0] = 0;
            this.anIntArrayArray125[0][1] = 0;
            this.anIntArrayArray125[0][3] = 0;
            this.anIntArrayArray125[0][2] = 0;
            this.anIntArrayArray125[1][3] = 1493;
            this.anIntArrayArray125[1][0] = 1843;
            this.anIntArrayArray125[1][1] = 0;
            this.anIntArrayArray125[1][2] = 0;
            this.anIntArrayArray125[2][0] = 2457;
            this.anIntArrayArray125[2][3] = 2939;
            this.anIntArrayArray125[2][2] = 0;
            this.anIntArrayArray125[2][1] = 0;
            this.anIntArrayArray125[3][0] = 2781;
            this.anIntArrayArray125[3][3] = 3565;
            this.anIntArrayArray125[3][1] = 0;
            this.anIntArrayArray125[3][2] = 1124;
            this.anIntArrayArray125[4][3] = 4031;
            this.anIntArrayArray125[4][2] = 3084;
            this.anIntArrayArray125[4][1] = 546;
            this.anIntArrayArray125[4][0] = 3481;
            this.anIntArrayArray125[5][1] = 4096;
            this.anIntArrayArray125[5][2] = 4096;
            this.anIntArrayArray125[5][3] = 4096;
            this.anIntArrayArray125[5][0] = 4096;
        } else if (arg0 == 5) {
            this.anIntArrayArray125 = new int[16][4];
            this.anIntArrayArray125[0][1] = 80;
            this.anIntArrayArray125[0][0] = 0;
            this.anIntArrayArray125[0][2] = 192;
            this.anIntArrayArray125[0][3] = 321;
            this.anIntArrayArray125[1][3] = 562;
            this.anIntArrayArray125[1][1] = 321;
            this.anIntArrayArray125[1][0] = 155;
            this.anIntArrayArray125[1][2] = 449;
            this.anIntArrayArray125[2][0] = 389;
            this.anIntArrayArray125[2][3] = 803;
            this.anIntArrayArray125[2][2] = 690;
            this.anIntArrayArray125[2][1] = 578;
            this.anIntArrayArray125[3][3] = 1140;
            this.anIntArrayArray125[3][2] = 995;
            this.anIntArrayArray125[3][0] = 671;
            this.anIntArrayArray125[3][1] = 947;
            this.anIntArrayArray125[4][2] = 1397;
            this.anIntArrayArray125[4][0] = 897;
            this.anIntArrayArray125[4][3] = 1509;
            this.anIntArrayArray125[4][1] = 1285;
            this.anIntArrayArray125[5][0] = 1175;
            this.anIntArrayArray125[5][2] = 1429;
            this.anIntArrayArray125[5][3] = 1413;
            this.anIntArrayArray125[5][1] = 1525;
            this.anIntArrayArray125[6][0] = 1368;
            this.anIntArrayArray125[6][3] = 1333;
            this.anIntArrayArray125[6][2] = 1461;
            this.anIntArrayArray125[6][1] = 1734;
            this.anIntArrayArray125[7][3] = 1702;
            this.anIntArrayArray125[7][1] = 1413;
            this.anIntArrayArray125[7][0] = 1507;
            this.anIntArrayArray125[7][2] = 1525;
            this.anIntArrayArray125[8][1] = 1108;
            this.anIntArrayArray125[8][0] = 1736;
            this.anIntArrayArray125[8][2] = 1590;
            this.anIntArrayArray125[8][3] = 2056;
            this.anIntArrayArray125[9][1] = 1766;
            this.anIntArrayArray125[9][2] = 2056;
            this.anIntArrayArray125[9][3] = 2666;
            this.anIntArrayArray125[9][0] = 2088;
            this.anIntArrayArray125[10][0] = 2355;
            this.anIntArrayArray125[10][1] = 2409;
            this.anIntArrayArray125[10][2] = 2586;
            this.anIntArrayArray125[10][3] = 3276;
            this.anIntArrayArray125[11][3] = 3228;
            this.anIntArrayArray125[11][1] = 3116;
            this.anIntArrayArray125[11][0] = 2691;
            this.anIntArrayArray125[11][2] = 3148;
            this.anIntArrayArray125[12][2] = 3710;
            this.anIntArrayArray125[12][3] = 3196;
            this.anIntArrayArray125[12][1] = 3806;
            this.anIntArrayArray125[12][0] = 3031;
            this.anIntArrayArray125[13][0] = 3522;
            this.anIntArrayArray125[13][1] = 3437;
            this.anIntArrayArray125[13][3] = 3019;
            this.anIntArrayArray125[13][2] = 3421;
            this.anIntArrayArray125[14][3] = 3228;
            this.anIntArrayArray125[14][2] = 3148;
            this.anIntArrayArray125[14][0] = 3727;
            this.anIntArrayArray125[14][1] = 3116;
            this.anIntArrayArray125[15][1] = 2377;
            this.anIntArrayArray125[15][0] = 4096;
            this.anIntArrayArray125[15][3] = 2746;
            this.anIntArrayArray125[15][2] = 2505;
        } else if (arg0 == 6) {
            this.anIntArrayArray125 = new int[4][4];
            this.anIntArrayArray125[0][3] = 0;
            this.anIntArrayArray125[0][2] = 4096;
            this.anIntArrayArray125[0][0] = 2048;
            this.anIntArrayArray125[0][1] = 0;
            this.anIntArrayArray125[1][2] = 4096;
            this.anIntArrayArray125[1][0] = 2867;
            this.anIntArrayArray125[1][1] = 4096;
            this.anIntArrayArray125[1][3] = 0;
            this.anIntArrayArray125[2][1] = 4096;
            this.anIntArrayArray125[2][0] = 3276;
            this.anIntArrayArray125[2][2] = 4096;
            this.anIntArrayArray125[2][3] = 0;
            this.anIntArrayArray125[3][3] = 0;
            this.anIntArrayArray125[3][2] = 0;
            this.anIntArrayArray125[3][0] = 4096;
            this.anIntArrayArray125[3][1] = 4096;
        } else {
            throw new RuntimeException("Invalid gradient preset");
        }
    }

    @OriginalMember(owner = "client!kg", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int arg0) {
        @Pc(19) int[][] local19 = super.colourCache.get(arg0);
        if (super.colourCache.dirty) {
            @Pc(29) int[] local29 = this.method9422(arg0, 0);
            @Pc(33) int[] local33 = local19[0];
            @Pc(37) int[] local37 = local19[1];
            @Pc(41) int[] local41 = local19[2];
            for (@Pc(43) int local43 = 0; local43 < EnvironmentLight.anInt9289; local43++) {
                @Pc(51) int local51 = local29[local43] >> 4;
                if (local51 < 0) {
                    local51 = 0;
                }
                if (local51 > 256) {
                    local51 = 256;
                }
                local51 = this.anIntArray398[local51];
                local33[local43] = local51 >> 12 & 0xFF0;
                local37[local43] = local51 >> 4 & 0xFF0;
                local41[local43] = (local51 & 0xFF) << 4;
            }
        }
        return local19;
    }

    @OriginalMember(owner = "client!kg", name = "d", descriptor = "(B)V")
    public void method4896() {
        @Pc(16) int local16 = this.anIntArrayArray125.length;
        if (local16 <= 0) {
            return;
        }
        for (@Pc(20) int local20 = 0; local20 < 257; local20++) {
            @Pc(24) int local24 = 0;
            @Pc(28) int local28 = local20 << 4;
            for (@Pc(30) int local30 = 0; local30 < local16 && local28 >= this.anIntArrayArray125[local30][0]; local30++) {
                local24++;
            }
            @Pc(137) int local137;
            @Pc(122) int local122;
            @Pc(107) int local107;
            @Pc(63) int[] local63;
            if (local16 > local24) {
                local63 = this.anIntArrayArray125[local24];
                if (local24 > 0) {
                    @Pc(72) int[] local72 = this.anIntArrayArray125[local24 - 1];
                    @Pc(89) int local89 = (local28 - local72[0] << 12) / (local63[0] - local72[0]);
                    @Pc(93) int local93 = 4096 - local89;
                    local107 = local72[3] * local93 + local89 * local63[3] >> 12;
                    local122 = local63[2] * local89 + local72[2] * local93 >> 12;
                    local137 = local89 * local63[1] + local72[1] * local93 >> 12;
                } else {
                    local122 = local63[2];
                    local137 = local63[1];
                    local107 = local63[3];
                }
            } else {
                local63 = this.anIntArrayArray125[local16 - 1];
                local122 = local63[2];
                local107 = local63[3];
                local137 = local63[1];
            }
            local137 >>= 0x4;
            local107 >>= 0x4;
            local122 >>= 0x4;
            if (local107 < 0) {
                local107 = 0;
            } else if (local107 > 255) {
                local107 = 255;
            }
            if (local137 < 0) {
                local137 = 0;
            } else if (local137 > 255) {
                local137 = 255;
            }
            if (local122 < 0) {
                local122 = 0;
            } else if (local122 > 255) {
                local122 = 255;
            }
            this.anIntArray398[local20] = local107 | local137 << 16 | local122 << 8;
        }
    }

    @OriginalMember(owner = "client!kg", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        if (this.anIntArrayArray125 == null) {
            this.method4893(1);
        }
        this.method4896();
    }

    @OriginalMember(owner = "client!kg", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            @Pc(14) int local14 = arg1.g1();
            if (local14 == 0) {
                this.anIntArrayArray125 = new int[arg1.g1()][4];
                for (@Pc(28) int local28 = 0; local28 < this.anIntArrayArray125.length; local28++) {
                    this.anIntArrayArray125[local28][0] = arg1.g2();
                    this.anIntArrayArray125[local28][1] = arg1.g1() << 4;
                    this.anIntArrayArray125[local28][2] = arg1.g1() << 4;
                    this.anIntArrayArray125[local28][3] = arg1.g1() << 4;
                }
            } else {
                this.method4893(local14);
            }
        }
        if (arg0) {
            this.anIntArrayArray125 = null;
        }
    }
}

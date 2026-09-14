import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.graphics.PointLight;
import com.jagex.math.IntMath;
import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeapBuffer;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!jw")
public final class Node_Sub32 extends Node {

    @OriginalMember(owner = "client!jw", name = "I", descriptor = "I")
    public int anInt5028;

    @OriginalMember(owner = "client!jw", name = "s", descriptor = "Lclient!am;")
    public final NativeToolkit aClass19_Sub1_11;

    @OriginalMember(owner = "client!jw", name = "o", descriptor = "Lclient!iga;")
    public final Ground_Sub1 aClass178_Sub1_1;

    @OriginalMember(owner = "client!jw", name = "K", descriptor = "Lclient!lca;")
    public final PointLight aClass2_Sub7_1;

    @OriginalMember(owner = "client!jw", name = "n", descriptor = "I")
    public final int anInt5022;

    @OriginalMember(owner = "client!jw", name = "A", descriptor = "I")
    public final int anInt5025;

    @OriginalMember(owner = "client!jw", name = "G", descriptor = "I")
    public final int anInt5024;

    @OriginalMember(owner = "client!jw", name = "q", descriptor = "I")
    public final int anInt5027;

    @OriginalMember(owner = "client!jw", name = "w", descriptor = "[[F")
    public float[][] aFloatArrayArray7;

    @OriginalMember(owner = "client!jw", name = "E", descriptor = "[[F")
    public float[][] aFloatArrayArray8;

    @OriginalMember(owner = "client!jw", name = "p", descriptor = "[[F")
    public float[][] aFloatArrayArray6;

    @OriginalMember(owner = "client!jw", name = "H", descriptor = "I")
    public int anInt5023;

    @OriginalMember(owner = "client!jw", name = "C", descriptor = "Lclient!mg;")
    public final Interface16 anInterface16_7;

    @OriginalMember(owner = "client!jw", name = "J", descriptor = "Lclient!ri;")
    public final Interface20 anInterface20_2;

    @OriginalMember(owner = "client!jw", name = "z", descriptor = "Lclient!av;")
    public IterableHashTable aIterableHashTable_25;

    @OriginalMember(owner = "client!jw", name = "x", descriptor = "Lclient!jaclib/memory/Stream;")
    public Stream aStream3;

    @OriginalMember(owner = "client!jw", name = "y", descriptor = "Lclient!jaclib/memory/Stream;")
    public Stream aStream2;

    @OriginalMember(owner = "client!jw", name = "<init>", descriptor = "(Lclient!am;Lclient!iga;Lclient!lca;[I)V")
    public Node_Sub32(@OriginalArg(0) NativeToolkit arg0, @OriginalArg(1) Ground_Sub1 arg1, @OriginalArg(2) PointLight arg2, @OriginalArg(3) int[] arg3) {
        this.aClass19_Sub1_11 = arg0;
        this.aClass178_Sub1_1 = arg1;
        this.aClass2_Sub7_1 = arg2;
        @Pc(23) int local23 = this.aClass2_Sub7_1.getRange() - (arg1.tileSize >> 1);
        this.anInt5022 = this.aClass2_Sub7_1.getX() - local23 >> arg1.tileSizeShift;
        this.anInt5025 = local23 + this.aClass2_Sub7_1.getX() >> arg1.tileSizeShift;
        this.anInt5024 = this.aClass2_Sub7_1.getZ() - local23 >> arg1.tileSizeShift;
        this.anInt5027 = local23 + this.aClass2_Sub7_1.getZ() >> arg1.tileSizeShift;
        @Pc(78) int local78 = this.anInt5025 + 1 - this.anInt5022;
        @Pc(87) int local87 = this.anInt5027 + 1 - this.anInt5024;
        this.aFloatArrayArray7 = new float[local78 + 1][local87 + 1];
        this.aFloatArrayArray8 = new float[local78 + 1][local87 + 1];
        this.aFloatArrayArray6 = new float[local78 + 1][local87 + 1];
        @Pc(123) int local123;
        @Pc(139) int local139;
        @Pc(146) int local146;
        @Pc(178) int local178;
        @Pc(195) int local195;
        for (@Pc(116) int local116 = 0; local116 <= local87; local116++) {
            local123 = local116 + this.anInt5024;
            if (local123 > 0 && local123 < this.aClass178_Sub1_1.sizeZ - 1) {
                for (local139 = 0; local139 <= local78; local139++) {
                    local146 = this.anInt5022 + local139;
                    if (local146 > 0 && local146 < this.aClass178_Sub1_1.sizeX - 1) {
                        local178 = arg1.getHeight(local146 + 1, local123) - arg1.getHeight(local146 - 1, local123);
                        local195 = arg1.getHeight(local146, local123 + 1) - arg1.getHeight(local146, local123 - 1);
                        @Pc(210) float local210 = (float) (1.0D / Math.sqrt(local195 * local195 + local178 * local178 + 65536));
                        this.aFloatArrayArray8[local139][local116] = (float) local178 * local210;
                        this.aFloatArrayArray7[local139][local116] = local210 * -256.0F;
                        this.aFloatArrayArray6[local139][local116] = local210 * (float) local195;
                    }
                }
            }
        }
        local123 = 0;
        @Pc(321) int local321;
        for (local139 = this.anInt5024; local139 <= this.anInt5027; local139++) {
            if (local139 >= 0 && local139 < arg1.sizeZ) {
                for (local146 = this.anInt5022; local146 <= this.anInt5025; local146++) {
                    if (local146 >= 0 && arg1.sizeX > local146) {
                        local178 = arg3[local123];
                        @Pc(308) int[] local308 = arg1.anIntArrayArrayArray5[local146][local139];
                        if (local308 != null && local178 != 0) {
                            if (local178 == 1) {
                                local321 = 0;
                                while (local308.length > local321) {
                                    if (local308[local321++] != -1 && local308[local321++] != -1 && local308[local321++] != -1) {
                                        this.anInt5023 += 3;
                                    }
                                }
                            } else {
                                this.anInt5023 += 3;
                            }
                        }
                    }
                    local123++;
                }
            } else {
                local123 += this.anInt5025 - this.anInt5022;
            }
        }
        if (this.anInt5023 <= 0) {
            this.anInterface16_7 = null;
            this.anInterface20_2 = null;
        } else {
            this.aIterableHashTable_25 = new IterableHashTable(IntMath.nextPow2(this.anInt5023));
            this.anInterface20_2 = this.aClass19_Sub1_11.method8122(false);
            this.anInterface20_2.method8543(this.anInt5023);
            @Pc(448) NativeHeapBuffer local448 = this.aClass19_Sub1_11.method8106(false, this.anInt5023 * 16);
            this.aStream3 = new Stream(local448);
            while (true) {
                @Pc(460) Buffer local460;
                do {
                    local460 = this.anInterface20_2.method8547();
                } while (local460 == null);
                this.aStream2 = new Stream(local460);
                local195 = 0;
                local123 = 0;
                for (local321 = this.anInt5024; local321 <= this.anInt5027; local321++) {
                    if (local321 >= 0 && local321 < arg1.sizeZ) {
                        @Pc(504) int local504 = 0;
                        for (@Pc(507) int local507 = this.anInt5022; local507 <= this.anInt5025; local507++) {
                            if (local507 >= 0 && arg1.sizeX > local507) {
                                @Pc(530) int local530 = arg3[local123];
                                @Pc(537) int[] local537 = arg1.anIntArrayArrayArray5[local507][local321];
                                if (local537 != null && local530 != 0) {
                                    if (local530 == 1) {
                                        @Pc(719) int[] local719 = arg1.anIntArrayArrayArray1[local507][local321];
                                        @Pc(726) int[] local726 = arg1.anIntArrayArrayArray3[local507][local321];
                                        @Pc(728) int local728 = 0;
                                        label119:
                                        while (true) {
                                            while (true) {
                                                if (local537.length <= local728) {
                                                    break label119;
                                                }
                                                if (local537[local728] == -1 || local537[local728 + 1] == -1 || local537[local728 + 2] == -1) {
                                                    local728 += 3;
                                                } else {
                                                    this.method4563(local504, local321, local726[local728], local507, local719[local728], local195);
                                                    local728++;
                                                    this.method4563(local504, local321, local726[local728], local507, local719[local728], local195);
                                                    local728++;
                                                    this.method4563(local504, local321, local726[local728], local507, local719[local728], local195);
                                                    local728++;
                                                }
                                            }
                                        }
                                    } else if (local530 == 3) {
                                        this.method4563(local504, local321, 0, local507, 0, local195);
                                        this.method4563(local504, local321, 0, local507, arg1.tileSize, local195);
                                        this.method4563(local504, local321, arg1.tileSize, local507, 0, local195);
                                    } else if (local530 == 2) {
                                        this.method4563(local504, local321, 0, local507, arg1.tileSize, local195);
                                        this.method4563(local504, local321, arg1.tileSize, local507, arg1.tileSize, local195);
                                        this.method4563(local504, local321, 0, local507, 0, local195);
                                    } else if (local530 == 5) {
                                        this.method4563(local504, local321, arg1.tileSize, local507, arg1.tileSize, local195);
                                        this.method4563(local504, local321, arg1.tileSize, local507, 0, local195);
                                        this.method4563(local504, local321, 0, local507, arg1.tileSize, local195);
                                    } else if (local530 == 4) {
                                        this.method4563(local504, local321, arg1.tileSize, local507, 0, local195);
                                        this.method4563(local504, local321, 0, local507, 0, local195);
                                        this.method4563(local504, local321, arg1.tileSize, local507, arg1.tileSize, local195);
                                    }
                                }
                            }
                            local123++;
                            local504++;
                        }
                    } else {
                        local123 += this.anInt5025 - this.anInt5022;
                    }
                    local195++;
                }
                this.aStream2.c();
                if (this.anInterface20_2.method8546()) {
                    this.aStream3.c();
                    this.anInterface16_7 = this.aClass19_Sub1_11.method8156(false);
                    this.anInterface16_7.method3152(16, this.anInt5028 * 16, local448);
                    break;
                }
                this.aStream3.f(0);
                this.aIterableHashTable_25.clear();
            }
        }
        this.aIterableHashTable_25 = null;
        this.aFloatArrayArray8 = this.aFloatArrayArray7 = this.aFloatArrayArray6 = null;
        this.aStream2 = null;
        this.aStream3 = null;
    }

    @OriginalMember(owner = "client!jw", name = "a", descriptor = "(BS)V")
    public void method4561(@OriginalArg(1) short arg0) {
        if (Stream.b()) {
            this.aStream2.b(arg0);
        } else {
            this.aStream2.c(arg0);
        }
    }

    @OriginalMember(owner = "client!jw", name = "a", descriptor = "(IIIIIII)V")
    public void method4563(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        @Pc(7) long local7 = -1L;
        @Pc(15) int local15 = (arg3 << this.aClass178_Sub1_1.tileSizeShift) + arg4;
        @Pc(23) int local23 = (arg1 << this.aClass178_Sub1_1.tileSizeShift) + arg2;
        @Pc(30) int local30 = this.aClass178_Sub1_1.averageHeight(local15, local23);
        if ((arg4 & 0x7F) == 0 || (arg2 & 0x7F) == 0) {
            local7 = (long) local15 & 0xFFFFL | 0xFFFFL << 16 & (long) local23 << 16;
            @Pc(63) Node local63 = this.aIterableHashTable_25.get(local7);
            if (local63 != null) {
                this.method4561(((Node_Sub50) local63).aShort119);
                return;
            }
        }
        @Pc(82) short local82 = (short) this.anInt5028++;
        if (local7 != -1L) {
            this.aIterableHashTable_25.put(local7, new Node_Sub50(local82));
        }
        @Pc(124) float local124;
        @Pc(110) float local110;
        @Pc(117) float local117;
        @Pc(214) float local214;
        @Pc(222) float local222;
        @Pc(229) float local229;
        @Pc(236) float local236;
        @Pc(243) float local243;
        @Pc(252) float local252;
        @Pc(261) float local261;
        @Pc(322) float local322;
        if (arg4 == 0 && arg2 == 0) {
            local110 = this.aFloatArrayArray7[arg0][arg5];
            local117 = this.aFloatArrayArray6[arg0][arg5];
            local124 = this.aFloatArrayArray8[arg0][arg5];
        } else if (this.aClass178_Sub1_1.tileSize == arg4 && arg2 == 0) {
            local124 = this.aFloatArrayArray8[arg0 + 1][arg5];
            local117 = this.aFloatArrayArray6[arg0 + 1][arg5];
            local110 = this.aFloatArrayArray7[arg0 + 1][arg5];
        } else if (this.aClass178_Sub1_1.tileSize == arg4 && arg2 == this.aClass178_Sub1_1.tileSize) {
            local124 = this.aFloatArrayArray8[arg0 + 1][arg5 + 1];
            local110 = this.aFloatArrayArray7[arg0 + 1][arg5 + 1];
            local117 = this.aFloatArrayArray6[arg0 + 1][arg5 + 1];
        } else if (arg4 == 0 && arg2 == this.aClass178_Sub1_1.tileSize) {
            local117 = this.aFloatArrayArray6[arg0][arg5 + 1];
            local124 = this.aFloatArrayArray8[arg0][arg5 + 1];
            local110 = this.aFloatArrayArray7[arg0][arg5 + 1];
        } else {
            local214 = (float) arg4 / (float) this.aClass178_Sub1_1.tileSize;
            local222 = (float) arg2 / (float) this.aClass178_Sub1_1.tileSize;
            local229 = this.aFloatArrayArray8[arg0][arg5];
            local236 = this.aFloatArrayArray7[arg0][arg5];
            local243 = this.aFloatArrayArray6[arg0][arg5];
            local252 = this.aFloatArrayArray8[arg0 + 1][arg5];
            local261 = this.aFloatArrayArray7[arg0 + 1][arg5];
            @Pc(279) float local279 = local261 + (this.aFloatArrayArray7[arg0 + 1][arg5 + 1] - local261) * local214;
            @Pc(297) float local297 = local252 + (this.aFloatArrayArray8[arg0 + 1][arg5 + 1] - local252) * local214;
            @Pc(313) float local313 = local243 + local214 * (this.aFloatArrayArray6[arg0][arg5 + 1] - local243);
            local322 = this.aFloatArrayArray6[arg0 + 1][arg5];
            @Pc(337) float local337 = local229 + (this.aFloatArrayArray8[arg0][arg5 + 1] - local229) * local214;
            @Pc(353) float local353 = local236 + (this.aFloatArrayArray7[arg0][arg5 + 1] - local236) * local214;
            @Pc(370) float local370 = local322 + local214 * (this.aFloatArrayArray6[arg0 + 1][arg5 + 1] - local322);
            local110 = (local279 - local353) * local222 + local353;
            local124 = (local297 - local337) * local222 + local337;
            local117 = (local370 - local313) * local222 + local313;
        }
        local214 = (float) (this.aClass2_Sub7_1.getX() - local15);
        local222 = (float) (this.aClass2_Sub7_1.getY() - local30);
        local229 = (float) (this.aClass2_Sub7_1.getZ() - local23);
        local236 = (float) Math.sqrt(local222 * local222 + local214 * local214 + local229 * local229);
        local243 = 1.0F / local236;
        local214 *= local243;
        local222 *= local243;
        local229 *= local243;
        local252 = local236 / (float) this.aClass2_Sub7_1.getRange();
        local261 = 1.0F - local252 * local252;
        if (local261 < 0.0F) {
            local261 = 0.0F;
        }
        local322 = local222 * local110 + local214 * local124 + local117 * local229;
        if (local322 < 0.0F) {
            local322 = 0.0F;
        }
        @Pc(572) float local572 = local322 * local261 * 2.0F;
        if (local572 > 1.0F) {
            local572 = 1.0F;
        }
        @Pc(583) int local583 = this.aClass2_Sub7_1.method8431();
        @Pc(593) int local593 = (int) ((float) (local583 >> 16 & 0xFF) * local572);
        if (local593 > 255) {
            local593 = 255;
        }
        @Pc(608) int local608 = (int) ((float) (local583 >> 8 & 0xFF) * local572);
        if (local608 > 255) {
            local608 = 255;
        }
        @Pc(623) int local623 = (int) ((float) (local583 & 0xFF) * local572);
        if (Stream.b()) {
            this.aStream3.a((float) local15);
            this.aStream3.a((float) local30);
            this.aStream3.a((float) local23);
        } else {
            this.aStream3.b((float) local15);
            this.aStream3.b((float) local30);
            this.aStream3.b((float) local23);
        }
        if (local623 > 255) {
            local623 = 255;
        }
        if (this.aClass19_Sub1_11.anInt9178 == 0) {
            this.aStream3.d(local623);
            this.aStream3.d(local608);
            this.aStream3.d(local593);
        } else {
            this.aStream3.d(local593);
            this.aStream3.d(local608);
            this.aStream3.d(local623);
        }
        this.aStream3.d(255);
        this.method4561(local82);
    }

    @OriginalMember(owner = "client!jw", name = "a", descriptor = "(IIBI[[Z)V")
    public void method4564(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) byte arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean[][] arg4) {
        if (this.anInterface20_2 == null || (arg3 + arg0 < this.anInt5022 || (arg0 - arg3 > this.anInt5025 || (arg1 + arg3 < this.anInt5024 || arg1 - arg3 > this.anInt5027)))) {
            return;
        }
        for (@Pc(56) int local56 = this.anInt5024; local56 <= this.anInt5027; local56++) {
            for (@Pc(63) int local63 = this.anInt5022; local63 <= this.anInt5025; local63++) {
                @Pc(70) int local70 = local63 - arg0;
                @Pc(75) int local75 = local56 - arg1;
                if (-arg3 < local70 && local70 < arg3 && -arg3 < local75 && local75 < arg3 && arg4[arg3 + local70][local75 + arg3]) {
                    this.aClass19_Sub1_11.method8050((byte) (int) (this.aClass2_Sub7_1.getIntensity() * 255.0F));
                    this.aClass19_Sub1_11.method8130(0, this.anInterface16_7);
                    this.aClass19_Sub1_11.method8114(this.aClass19_Sub1_11.aClass244_21);
                    this.aClass19_Sub1_11.method8052(0, 0, this.anInterface20_2, this.anInt5028, Static104.aClass131_2, this.anInt5023 / 3);
                    return;
                }
            }
        }
        if (arg2 <= 43) {
            this.method4564(119, 109, (byte) 93, 25, null);
        }
    }
}

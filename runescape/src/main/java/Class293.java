import com.jagex.graphics.Shadow;
import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pk")
public final class Class293 {

    @OriginalMember(owner = "client!pk", name = "b", descriptor = "[[Lclient!oh;")
    public Class274[][] aClass274ArrayArray1;

    @OriginalMember(owner = "client!pk", name = "p", descriptor = "Lclient!iga;")
    public final Ground_Sub1 aClass178_Sub1_2;

    @OriginalMember(owner = "client!pk", name = "q", descriptor = "Lclient!am;")
    public final NativeToolkit aClass19_Sub1_17;

    @OriginalMember(owner = "client!pk", name = "d", descriptor = "I")
    public final int anInt7428;

    @OriginalMember(owner = "client!pk", name = "s", descriptor = "I")
    public final int anInt7425;

    @OriginalMember(owner = "client!pk", name = "a", descriptor = "[B")
    public final byte[] aByteArray87;

    @OriginalMember(owner = "client!pk", name = "i", descriptor = "I")
    public final int anInt7417;

    @OriginalMember(owner = "client!pk", name = "h", descriptor = "I")
    public final int anInt7420;

    @OriginalMember(owner = "client!pk", name = "j", descriptor = "I")
    public final int anInt7426;

    @OriginalMember(owner = "client!pk", name = "<init>", descriptor = "(Lclient!am;Lclient!iga;)V")
    public Class293(@OriginalArg(0) NativeToolkit arg0, @OriginalArg(1) Ground_Sub1 arg1) {
        this.aClass178_Sub1_2 = arg1;
        this.aClass19_Sub1_17 = arg0;
        this.anInt7428 = (this.aClass178_Sub1_2.sizeX * this.aClass178_Sub1_2.tileSize >> this.aClass19_Sub1_17.anInt9173) + 2;
        this.anInt7425 = (this.aClass178_Sub1_2.tileSize * this.aClass178_Sub1_2.sizeZ >> this.aClass19_Sub1_17.anInt9173) + 2;
        this.aByteArray87 = new byte[this.anInt7428 * this.anInt7425];
        this.anInt7417 = this.aClass19_Sub1_17.anInt9173 + 7 - this.aClass178_Sub1_2.tileSizeShift;
        this.anInt7420 = this.aClass178_Sub1_2.sizeX >> this.anInt7417;
        this.anInt7426 = this.aClass178_Sub1_2.sizeZ >> this.anInt7417;
    }

    @OriginalMember(owner = "client!pk", name = "a", descriptor = "(IIIII)V")
    public void method6604(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        if (this.aClass274ArrayArray1 == null) {
            return;
        }
        @Pc(20) int local20 = arg2 - 1 >> 7;
        @Pc(30) int local30 = arg3 + arg2 - 1 - 1 >> 7;
        @Pc(36) int local36 = arg1 - 1 >> 7;
        @Pc(46) int local46 = arg0 + arg1 - 1 - 1 >> 7;
        for (@Pc(48) int local48 = local20; local48 <= local30; local48++) {
            @Pc(55) Class274[] local55 = this.aClass274ArrayArray1[local48];
            for (@Pc(57) int local57 = local36; local57 <= local46; local57++) {
                if (local55[local57] != null) {
                    local55[local57].aBoolean520 = true;
                }
            }
        }
    }

    @OriginalMember(owner = "client!pk", name = "a", descriptor = "([[ZIIIZI)V")
    public void method6605(@OriginalArg(0) boolean[][] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        this.aClass19_Sub1_17.method8072(false);
        this.aClass19_Sub1_17.method8083(false);
        this.aClass19_Sub1_17.method8053(1);
        this.aClass19_Sub1_17.method8054(1);
        this.aClass19_Sub1_17.method8108(false, -2, false);
        @Pc(54) float local54 = 1.0F / (float) (this.aClass19_Sub1_17.anInt9159 * 128);
        @Pc(58) int local58;
        @Pc(65) int local65;
        @Pc(72) int local72;
        @Pc(74) int local74;
        if (arg4) {
            for (local58 = 0; local58 < this.anInt7426; local58++) {
                local65 = local58 << this.anInt7417;
                local72 = local58 + 1 << this.anInt7417;
                label138:
                for (local74 = 0; local74 < this.anInt7420; local74++) {
                    if (this.aClass274ArrayArray1[local74][local58] != null) {
                        @Pc(90) int local90 = local74 << this.anInt7417;
                        @Pc(97) int local97 = local74 + 1 << this.anInt7417;
                        for (@Pc(99) int local99 = local90; local99 < local97; local99++) {
                            if (local99 - arg3 >= -arg1 && arg1 >= local99 - arg3) {
                                for (@Pc(127) int local127 = local65; local127 < local72; local127++) {
                                    if (-arg1 <= local127 - arg2 && local127 - arg2 <= arg1 && arg0[arg1 + local99 - arg3][local127 + arg1 - arg2]) {
                                        @Pc(169) Matrix_Sub1 local169 = this.aClass19_Sub1_17.method8082();
                                        local169.method1884(1.0F, local54, local54);
                                        local169.translate(-local74, -local58, 0);
                                        this.aClass19_Sub1_17.method8073(Static431.aClass370_5);
                                        this.aClass274ArrayArray1[local74][local58].method6229();
                                        continue label138;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (local58 = 0; local58 < this.anInt7426; local58++) {
                local65 = local58 << this.anInt7417;
                local72 = local58 + 1 << this.anInt7417;
                for (local74 = 0; local74 < this.anInt7420; local74++) {
                    @Pc(266) Class274 local266 = this.aClass274ArrayArray1[local74][local58];
                    if (local266 != null) {
                        @Pc(279) Interface20 local279 = this.aClass19_Sub1_17.method8074(local266.anInt6928 * 3);
                        @Pc(284) Buffer local284 = local279.method8547();
                        if (local284 != null) {
                            @Pc(292) Stream local292 = this.aClass19_Sub1_17.method8100(local284);
                            @Pc(294) int local294 = 0;
                            @Pc(299) int local299 = local74 << this.anInt7417;
                            @Pc(306) int local306 = local74 + 1 << this.anInt7417;
                            for (@Pc(308) int local308 = local65; local308 < local72; local308++) {
                                if (local308 - arg2 >= -arg1 && arg1 >= local308 - arg2) {
                                    @Pc(342) int local342 = local299 + this.aClass178_Sub1_2.sizeX * local308;
                                    for (@Pc(344) int local344 = local299; local344 < local306; local344++) {
                                        if (-arg1 <= local344 - arg3 && local344 - arg3 <= arg1 && arg0[arg1 + local344 - arg3][local308 + arg1 - arg2]) {
                                            @Pc(393) short[] local393 = this.aClass178_Sub1_2.aShortArrayArray4[local342];
                                            if (local393 != null) {
                                                @Pc(401) int local401;
                                                if (Stream.b()) {
                                                    for (local401 = 0; local401 < local393.length; local401++) {
                                                        local292.b(local393[local401] & 0xFFFF);
                                                        local294++;
                                                    }
                                                } else {
                                                    for (local401 = 0; local401 < local393.length; local401++) {
                                                        local292.c(local393[local401] & 0xFFFF);
                                                        local294++;
                                                    }
                                                }
                                            }
                                        }
                                        local342++;
                                    }
                                }
                            }
                            local292.c();
                            if (local279.method8546() && local294 > 0) {
                                @Pc(490) Matrix_Sub1 local490 = this.aClass19_Sub1_17.method8082();
                                local490.method1884(1.0F, local54, local54);
                                local490.translate(-local74, -local58, 0);
                                this.aClass19_Sub1_17.method8073(Static431.aClass370_5);
                                local266.method6226(local279, local294 / 3);
                            }
                        }
                    }
                }
            }
        }
        this.aClass19_Sub1_17.method8031();
    }

    @OriginalMember(owner = "client!pk", name = "b", descriptor = "(Lclient!r;III)V")
    public void method6607(@OriginalArg(0) Shadow arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        @Pc(17) DoublyLinkedNode_Sub2_Sub9_Sub1 local17 = (DoublyLinkedNode_Sub2_Sub9_Sub1) arg0;
        arg2 += local17.anInt2328 + 1;
        arg1 += local17.anInt2326 + 1;
        @Pc(38) int local38 = arg1 + arg2 * this.anInt7428;
        @Pc(40) int local40 = 0;
        @Pc(43) int local43 = local17.anInt2325;
        @Pc(46) int local46 = local17.anInt2320;
        @Pc(52) int local52 = this.anInt7428 - local46;
        @Pc(54) int local54 = 0;
        @Pc(61) int local61;
        if (arg2 <= 0) {
            local61 = 1 - arg2;
            local40 = local46 * local61;
            arg2 = 1;
            local43 -= local61;
            local38 += this.anInt7428 * local61;
        }
        if (this.anInt7425 <= local43 + arg2) {
            local61 = local43 + arg2 + 1 - this.anInt7425;
            local43 -= local61;
        }
        if (arg1 <= 0) {
            local61 = 1 - arg1;
            local38 += local61;
            local52 += local61;
            arg1 = 1;
            local46 -= local61;
            local54 = local61;
            local40 += local61;
        }
        if (this.anInt7428 <= local46 + arg1) {
            local61 = local46 + arg1 + 1 - this.anInt7428;
            local54 += local61;
            local46 -= local61;
            local52 += local61;
        }
        if (local46 > 0 && local43 > 0) {
            Static714.method9341(local52, local40, this.aByteArray87, local43, local38, local17.aByteArray22, local46, local54);
            this.method6604(local43, arg2, arg1, local46);
        }
    }

    @OriginalMember(owner = "client!pk", name = "a", descriptor = "(ILclient!r;IB)Z")
    public boolean method6608(@OriginalArg(0) int arg0, @OriginalArg(1) Shadow arg1, @OriginalArg(2) int arg2) {
        @Pc(6) DoublyLinkedNode_Sub2_Sub9_Sub1 local6 = (DoublyLinkedNode_Sub2_Sub9_Sub1) arg1;
        arg2 += local6.anInt2328 + 1;
        arg0 += local6.anInt2326 + 1;
        @Pc(27) int local27 = arg2 * this.anInt7428 + arg0;
        @Pc(30) int local30 = local6.anInt2325;
        @Pc(40) int local40 = local6.anInt2320;
        @Pc(45) int local45 = this.anInt7428 - local40;
        @Pc(55) int local55;
        if (arg2 <= 0) {
            local55 = 1 - arg2;
            local30 -= local55;
            local27 += local55 * this.anInt7428;
            arg2 = 1;
        }
        if (this.anInt7425 <= arg2 + local30) {
            local55 = local30 + arg2 + 1 - this.anInt7425;
            local30 -= local55;
        }
        if (arg0 <= 0) {
            local55 = 1 - arg0;
            local45 += local55;
            arg0 = 1;
            local40 -= local55;
            local27 += local55;
        }
        if (arg0 + local40 >= this.anInt7428) {
            local55 = local40 + arg0 + 1 - this.anInt7428;
            local40 -= local55;
            local45 += local55;
        }
        if (local40 > 0 && local30 > 0) {
            local45 += this.anInt7428 * 7;
            return Static105.method2042(local30, local27, local45, this.aByteArray87, local40);
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!pk", name = "a", descriptor = "(Lclient!r;III)V")
    public void method6609(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(6) DoublyLinkedNode_Sub2_Sub9_Sub1 local6 = (DoublyLinkedNode_Sub2_Sub9_Sub1) arg0;
        arg2 += local6.anInt2326 + 1;
        arg1 += local6.anInt2328 + 1;
        @Pc(27) int local27 = arg1 * this.anInt7428 + arg2;
        @Pc(29) int local29 = 0;
        @Pc(32) int local32 = local6.anInt2325;
        @Pc(35) int local35 = local6.anInt2320;
        @Pc(40) int local40 = this.anInt7428 - local35;
        @Pc(49) int local49;
        if (arg1 <= 0) {
            local49 = 1 - arg1;
            arg1 = 1;
            local32 -= local49;
            local29 = local49 * local35;
            local27 += local49 * this.anInt7428;
        }
        @Pc(70) int local70 = 0;
        if (arg1 + local32 >= this.anInt7425) {
            local49 = local32 + arg1 + 1 - this.anInt7425;
            local32 -= local49;
        }
        if (arg2 <= 0) {
            local49 = 1 - arg2;
            arg2 = 1;
            local70 = local49;
            local40 += local49;
            local35 -= local49;
            local27 += local49;
            local29 += local49;
        }
        if (arg2 + local35 >= this.anInt7428) {
            local49 = arg2 + local35 + 1 - this.anInt7428;
            local35 -= local49;
            local40 += local49;
            local70 += local49;
        }
        if (local35 > 0 && local32 > 0) {
            Static217.method3185(this.aByteArray87, local6.aByteArray22, local27, local40, local70, local29, local35, local32);
            this.method6604(local32, arg1, arg2, local35);
        }
    }

    @OriginalMember(owner = "client!pk", name = "a", descriptor = "(B)V")
    public void method6611() {
        this.aClass274ArrayArray1 = new Class274[this.anInt7420][this.anInt7426];
        for (@Pc(14) int local14 = 0; local14 < this.anInt7426; local14++) {
            for (@Pc(21) int local21 = 0; local21 < this.anInt7420; local21++) {
                this.aClass274ArrayArray1[local21][local14] = new Class274(this.aClass19_Sub1_17, this, this.aClass178_Sub1_2, local21, local14, this.anInt7417, local21 * 128 + 1, local14 * 128 - -1);
                if (this.aClass274ArrayArray1[local21][local14].anInt6928 == 0) {
                    this.aClass274ArrayArray1[local21][local14] = null;
                }
            }
        }
    }
}

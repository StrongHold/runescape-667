import com.jagex.graphics.Shadow;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!hn")
public final class Class170 {

    @OriginalMember(owner = "client!hn", name = "d", descriptor = "[[Lclient!uga;")
    public Class373[][] aClass373ArrayArray1;

    @OriginalMember(owner = "client!hn", name = "k", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_18;

    @OriginalMember(owner = "client!hn", name = "p", descriptor = "Lclient!pn;")
    public final Ground_Sub2 aClass178_Sub2_1;

    @OriginalMember(owner = "client!hn", name = "g", descriptor = "I")
    public final int anInt4022;

    @OriginalMember(owner = "client!hn", name = "l", descriptor = "I")
    public final int anInt4026;

    @OriginalMember(owner = "client!hn", name = "n", descriptor = "I")
    public final int anInt4025;

    @OriginalMember(owner = "client!hn", name = "o", descriptor = "[B")
    public final byte[] aByteArray41;

    @OriginalMember(owner = "client!hn", name = "a", descriptor = "I")
    public final int anInt4020;

    @OriginalMember(owner = "client!hn", name = "e", descriptor = "I")
    public final int anInt4023;

    @OriginalMember(owner = "client!hn", name = "<init>", descriptor = "(Lclient!qha;Lclient!pn;)V")
    public Class170(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) Ground_Sub2 arg1) {
        this.aClass19_Sub3_18 = arg0;
        this.aClass178_Sub2_1 = arg1;
        this.anInt4022 = (this.aClass178_Sub2_1.sizeX * this.aClass178_Sub2_1.tileSize >> this.aClass19_Sub3_18.anInt7989) + 2;
        this.anInt4026 = (this.aClass178_Sub2_1.sizeZ * this.aClass178_Sub2_1.tileSize >> this.aClass19_Sub3_18.anInt7989) + 2;
        this.anInt4025 = this.aClass19_Sub3_18.anInt7989 + 7 - this.aClass178_Sub2_1.tileSizeShift;
        this.aByteArray41 = new byte[this.anInt4026 * this.anInt4022];
        this.anInt4020 = this.aClass178_Sub2_1.sizeX >> this.anInt4025;
        this.anInt4023 = this.aClass178_Sub2_1.sizeZ >> this.anInt4025;
    }

    @OriginalMember(owner = "client!hn", name = "a", descriptor = "(B)V")
    public void method3540() {
        this.aClass373ArrayArray1 = new Class373[this.anInt4020][this.anInt4023];
        for (@Pc(18) int local18 = 0; local18 < this.anInt4023; local18++) {
            for (@Pc(22) int local22 = 0; local22 < this.anInt4020; local22++) {
                this.aClass373ArrayArray1[local22][local18] = new Class373(this.aClass19_Sub3_18, this, this.aClass178_Sub2_1, local22, local18, this.anInt4025, local22 * 128 + 1, local18 * 128 + 1);
            }
        }
    }

    @OriginalMember(owner = "client!hn", name = "a", descriptor = "(BIILclient!r;)V")
    public void method3541(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) Shadow arg2) {
        @Pc(6) DoublyLinkedNode_Sub2_Sub9_ local6 = (DoublyLinkedNode_Sub2_Sub9_) arg2;
        arg0 += local6.anInt7350 + 1;
        arg1 += local6.anInt7353 + 1;
        @Pc(27) int local27 = arg1 * this.anInt4022 + arg0;
        @Pc(29) int local29 = 0;
        @Pc(32) int local32 = local6.anInt7344;
        @Pc(35) int local35 = local6.anInt7345;
        @Pc(48) int local48 = this.anInt4022 - local35;
        @Pc(58) int local58;
        if (arg1 <= 0) {
            local58 = 1 - arg1;
            arg1 = 1;
            local27 += local58 * this.anInt4022;
            local32 -= local58;
            local29 = local58 * local35;
        }
        @Pc(79) int local79 = 0;
        if (arg1 + local32 >= this.anInt4026) {
            local58 = arg1 + local32 + 1 - this.anInt4026;
            local32 -= local58;
        }
        if (arg0 <= 0) {
            local58 = 1 - arg0;
            local48 += local58;
            local35 -= local58;
            local79 = local58;
            arg0 = 1;
            local27 += local58;
            local29 += local58;
        }
        if (arg0 + local35 >= this.anInt4022) {
            local58 = local35 + arg0 + 1 - this.anInt4022;
            local48 += local58;
            local79 += local58;
            local35 -= local58;
        }
        if (local35 > 0 && local32 > 0) {
            Static607.method8167(this.aByteArray41, local79, local27, local35, local32, local48, local6.aByteArray85, local29);
            this.method3545(arg1, local32, local35, arg0);
        }
    }

    @OriginalMember(owner = "client!hn", name = "a", descriptor = "(IZZI[[ZI)V")
    public void method3542(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(3) int arg2, @OriginalArg(4) boolean[][] arg3, @OriginalArg(5) int arg4) {
        this.aClass19_Sub3_18.method7008(false);
        this.aClass19_Sub3_18.method6972(false);
        this.aClass19_Sub3_18.method7046(-2);
        this.aClass19_Sub3_18.method6991(1);
        this.aClass19_Sub3_18.setBlendMode(1);
        @Pc(50) float local50 = 1.0F / (float) (this.aClass19_Sub3_18.anInt7988 * 128);
        @Pc(54) int local54;
        @Pc(61) int local61;
        @Pc(68) int local68;
        @Pc(70) int local70;
        @Pc(77) int local77;
        @Pc(84) int local84;
        @Pc(86) int local86;
        if (arg1) {
            for (local54 = 0; local54 < this.anInt4023; local54++) {
                local61 = local54 << this.anInt4025;
                local68 = local54 + 1 << this.anInt4025;
                label128:
                for (local70 = 0; local70 < this.anInt4020; local70++) {
                    local77 = local70 << this.anInt4025;
                    local84 = local70 + 1 << this.anInt4025;
                    for (local86 = local77; local86 < local84; local86++) {
                        if (-arg4 <= local86 - arg0 && local86 - arg0 <= arg4) {
                            for (@Pc(107) int local107 = local61; local107 < local68; local107++) {
                                if (-arg4 <= local107 - arg2 && arg4 >= local107 - arg2 && arg3[arg4 + local86 - arg0][arg4 + local107 - arg2]) {
                                    OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
                                    OpenGL.glLoadIdentity();
                                    OpenGL.glScalef(local50, local50, 1.0F);
                                    OpenGL.glTranslatef((float) -local70 / local50, (float) -local54 / local50, 1.0F);
                                    OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
                                    this.aClass373ArrayArray1[local70][local54].method8456();
                                    continue label128;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (local54 = 0; local54 < this.anInt4023; local54++) {
                local61 = local54 << this.anInt4025;
                local68 = local54 + 1 << this.anInt4025;
                for (local70 = 0; local70 < this.anInt4020; local70++) {
                    local77 = 0;
                    local84 = local70 << this.anInt4025;
                    local86 = local70 + 1 << this.anInt4025;
                    @Pc(262) Node_Sub21_Sub1 local262 = this.aClass19_Sub3_18.aClass2_Sub21_Sub1_3;
                    local262.pos = 0;
                    for (@Pc(267) int local267 = local61; local267 < local68; local267++) {
                        if (-arg4 <= local267 - arg2 && local267 - arg2 <= arg4) {
                            @Pc(298) int local298 = local84 + local267 * this.aClass178_Sub2_1.sizeX;
                            for (@Pc(300) int local300 = local84; local300 < local86; local300++) {
                                if (local300 - arg0 >= -arg4 && local300 - arg0 <= arg4 && arg3[arg4 + local300 - arg0][local267 + arg4 - arg2]) {
                                    @Pc(350) short[] local350 = this.aClass178_Sub2_1.aShortArrayArray6[local298];
                                    if (local350 != null) {
                                        @Pc(360) int local360;
                                        if (this.aClass19_Sub3_18.aBoolean618) {
                                            for (local360 = 0; local360 < local350.length; local360++) {
                                                local77++;
                                                local262.p2(local350[local360] & 0xFFFF);
                                            }
                                        } else {
                                            for (local360 = 0; local360 < local350.length; local360++) {
                                                local262.ip2(local350[local360] & 0xFFFF);
                                                local77++;
                                            }
                                        }
                                    }
                                }
                                local298++;
                            }
                        }
                    }
                    if (local77 > 0) {
                        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
                        OpenGL.glLoadIdentity();
                        OpenGL.glScalef(local50, local50, 1.0F);
                        OpenGL.glTranslatef((float) -local70 / local50, (float) -local54 / local50, 1.0F);
                        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
                        this.aClass373ArrayArray1[local70][local54].method8459(local262.data, local77);
                    }
                }
            }
        }
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glLoadIdentity();
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
    }

    @OriginalMember(owner = "client!hn", name = "a", descriptor = "(IILclient!r;I)Z")
    public boolean method3543(@OriginalArg(1) int arg0, @OriginalArg(2) Shadow arg1, @OriginalArg(3) int arg2) {
        @Pc(6) DoublyLinkedNode_Sub2_Sub9_ local6 = (DoublyLinkedNode_Sub2_Sub9_) arg1;
        arg2 += local6.anInt7353 + 1;
        arg0 += local6.anInt7350 + 1;
        @Pc(27) int local27 = arg2 * this.anInt4022 + arg0;
        @Pc(30) int local30 = local6.anInt7344;
        @Pc(33) int local33 = local6.anInt7345;
        @Pc(39) int local39 = this.anInt4022 - local33;
        @Pc(48) int local48;
        if (arg2 <= 0) {
            local48 = 1 - arg2;
            arg2 = 1;
            local30 -= local48;
            local27 += this.anInt4022 * local48;
        }
        if (local30 + arg2 >= this.anInt4026) {
            local48 = local30 + arg2 + 1 - this.anInt4026;
            local30 -= local48;
        }
        if (arg0 <= 0) {
            local48 = 1 - arg0;
            arg0 = 1;
            local33 -= local48;
            local39 += local48;
            local27 += local48;
        }
        if (this.anInt4022 <= arg0 + local33) {
            local48 = arg0 + local33 + 1 - this.anInt4022;
            local33 -= local48;
            local39 += local48;
        }
        if (local33 > 0 && local30 > 0) {
            local39 += this.anInt4022 * 7;
            return Static417.method5723(local27, this.aByteArray41, local33, local30, local39);
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!hn", name = "a", descriptor = "(ILclient!r;IB)V")
    public void method3544(@OriginalArg(0) int arg0, @OriginalArg(1) Shadow arg1, @OriginalArg(2) int arg2) {
        @Pc(6) DoublyLinkedNode_Sub2_Sub9_ local6 = (DoublyLinkedNode_Sub2_Sub9_) arg1;
        arg0 += local6.anInt7353 + 1;
        arg2 += local6.anInt7350 + 1;
        @Pc(27) int local27 = this.anInt4022 * arg0 + arg2;
        @Pc(36) int local36 = 0;
        @Pc(39) int local39 = local6.anInt7344;
        @Pc(42) int local42 = local6.anInt7345;
        @Pc(48) int local48 = this.anInt4022 - local42;
        @Pc(50) int local50 = 0;
        @Pc(60) int local60;
        if (arg0 <= 0) {
            local60 = 1 - arg0;
            local39 -= local60;
            arg0 = 1;
            local36 = local42 * local60;
            local27 += local60 * this.anInt4022;
        }
        if (this.anInt4026 <= arg0 + local39) {
            local60 = local39 + arg0 + 1 - this.anInt4026;
            local39 -= local60;
        }
        if (arg2 <= 0) {
            local60 = 1 - arg2;
            local42 -= local60;
            local36 += local60;
            local48 += local60;
            local27 += local60;
            arg2 = 1;
            local50 = local60;
        }
        if (arg2 + local42 >= this.anInt4022) {
            local60 = local42 + arg2 + 1 - this.anInt4022;
            local48 += local60;
            local50 += local60;
            local42 -= local60;
        }
        if (local42 > 0 && local39 > 0) {
            Static672.method8765(local50, local48, local27, this.aByteArray41, local36, local6.aByteArray85, local39, local42);
            this.method3545(arg0, local39, local42, arg2);
        }
    }

    @OriginalMember(owner = "client!hn", name = "a", descriptor = "(IIIII)V")
    public void method3545(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        if (this.aClass373ArrayArray1 == null) {
            return;
        }
        @Pc(16) int local16 = arg3 - 1 >> 7;
        @Pc(26) int local26 = arg3 + arg2 - 1 - 1 >> 7;
        @Pc(32) int local32 = arg0 - 1 >> 7;
        @Pc(42) int local42 = arg0 + arg1 - 1 - 1 >> 7;
        for (@Pc(44) int local44 = local16; local44 <= local26; local44++) {
            @Pc(51) Class373[] local51 = this.aClass373ArrayArray1[local44];
            for (@Pc(53) int local53 = local32; local53 <= local42; local53++) {
                local51[local53].aBoolean738 = true;
            }
        }
    }
}

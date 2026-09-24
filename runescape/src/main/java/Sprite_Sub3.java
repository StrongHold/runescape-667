import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Sprite;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!np")
public final class Sprite_Sub3 extends Sprite {

    @OriginalMember(owner = "client!np", name = "C", descriptor = "I")
    public int anInt6612 = 0;

    @OriginalMember(owner = "client!np", name = "k", descriptor = "I")
    public int anInt6624 = 0;

    @OriginalMember(owner = "client!np", name = "l", descriptor = "Z")
    public boolean aBoolean493 = false;

    @OriginalMember(owner = "client!np", name = "q", descriptor = "I")
    public int anInt6617 = 0;

    @OriginalMember(owner = "client!np", name = "w", descriptor = "I")
    public int anInt6625 = 0;

    @OriginalMember(owner = "client!np", name = "e", descriptor = "I")
    public final int anInt6606;

    @OriginalMember(owner = "client!np", name = "p", descriptor = "I")
    public final int anInt6626;

    @OriginalMember(owner = "client!np", name = "i", descriptor = "Lclient!am;")
    public final NativeToolkit aClass19_Sub1_12;

    @OriginalMember(owner = "client!np", name = "t", descriptor = "Lclient!og;")
    public final Interface18 anInterface18_3;

    @OriginalMember(owner = "client!np", name = "B", descriptor = "Z")
    public final boolean aBoolean494;

    @OriginalMember(owner = "client!np", name = "g", descriptor = "Z")
    public final boolean aBoolean495;

    @OriginalMember(owner = "client!np", name = "u", descriptor = "Z")
    public final boolean aBoolean492;

    @OriginalMember(owner = "client!np", name = "d", descriptor = "Z")
    public final boolean aBoolean496;

    @OriginalMember(owner = "client!np", name = "<init>", descriptor = "(Lclient!am;IIZ)V")
    public Sprite_Sub3(@OriginalArg(0) NativeToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean arg3) {
        this.anInt6606 = arg2;
        this.anInt6626 = arg1;
        this.aClass19_Sub1_12 = arg0;
        this.anInterface18_3 = arg0.method8067(arg3 ? Static172.aClass92_8 : Static685.aClass92_16, Static702.aClass397_16, arg2, arg1);
        this.anInterface18_3.method9052(true, true);
        this.aBoolean494 = this.anInterface18_3.method9045() != arg1;
        this.aBoolean495 = this.anInterface18_3.method9047() != arg2;
        this.aBoolean492 = !this.aBoolean494 && this.anInterface18_3.method9049();
        this.aBoolean496 = !this.aBoolean495 && this.anInterface18_3.method9049();
    }

    @OriginalMember(owner = "client!np", name = "<init>", descriptor = "(Lclient!am;II[III)V")
    public Sprite_Sub3(@OriginalArg(0) NativeToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.anInt6626 = arg1;
        this.anInt6606 = arg2;
        this.aClass19_Sub1_12 = arg0;
        this.anInterface18_3 = arg0.method8143(arg1, arg3, arg4, arg5, false, arg2);
        this.anInterface18_3.method9052(true, true);
        this.aBoolean494 = this.anInterface18_3.method9045() != arg1;
        this.aBoolean495 = arg2 != this.anInterface18_3.method9047();
        this.aBoolean492 = !this.aBoolean494 && this.anInterface18_3.method9049();
        this.aBoolean496 = !this.aBoolean495 && this.anInterface18_3.method9049();
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(IIII[III)V")
    public void method5914(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int[] arg4, @OriginalArg(6) int arg5) {
        this.anInterface18_3.method9048(arg4, arg0, arg1, arg3, arg2, arg5);
    }

    @OriginalMember(owner = "client!np", name = "c", descriptor = "(IIII)V")
    @Override
    public void setOffsets(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2) {
        this.anInt6624 = y1;
        this.anInt6625 = x2;
        this.anInt6612 = y2;
        this.anInt6617 = x1;
        this.aBoolean493 = this.anInt6617 != 0 || this.anInt6624 != 0 || this.anInt6625 != 0 || this.anInt6612 != 0;
    }

    @OriginalMember(owner = "client!np", name = "c", descriptor = "()I")
    @Override
    public int getWidth() {
        return this.anInt6626;
    }

    @OriginalMember(owner = "client!np", name = "d", descriptor = "()I")
    @Override
    public int getHeight() {
        return this.anInt6606;
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(IILclient!aa;II)V")
    @Override
    public void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) ClippingMask mask, @OriginalArg(3) int maskX, @OriginalArg(4) int maskY) {
        @Pc(6) ClippingMask_Sub2 local6 = (ClippingMask_Sub2) mask;
        @Pc(9) Interface18 local9 = local6.anInterface18_2;
        @Pc(14) int local14 = x + this.anInt6617;
        @Pc(19) int local19 = y + this.anInt6624;
        this.anInterface18_3.method9041(Static363.aClass259_14);
        this.aClass19_Sub1_12.method8099();
        this.aClass19_Sub1_12.method8088(this.anInterface18_3);
        this.aClass19_Sub1_12.method8053(1);
        this.aClass19_Sub1_12.method8054(1);
        @Pc(49) Matrix_Sub1 local49 = this.aClass19_Sub1_12.method8040();
        local49.method1884(0.0F, (float) this.anInt6606, (float) this.anInt6626);
        local49.translate(local14, local19, 0);
        this.aClass19_Sub1_12.method8042();
        @Pc(73) Matrix_Sub1 local73 = this.aClass19_Sub1_12.method8082();
        local73.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) this.anInt6626));
        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_12.method8138(1);
        this.aClass19_Sub1_12.method8088(local9);
        this.aClass19_Sub1_12.method8094(Static209.aClass121_4, Static185.aClass121_3);
        this.aClass19_Sub1_12.method8080(0, Static188.aClass168_1);
        @Pc(123) Matrix_Sub1 local123 = this.aClass19_Sub1_12.method8082();
        local123.method1884(1.0F, local9.method9050((float) this.anInt6606), local9.method9046((float) this.anInt6626));
        local123.method1882(0.0F, local9.method9050((float) (local19 - maskY)), local9.method9046((float) (local14 - maskX)));
        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_12.method8059();
        this.aClass19_Sub1_12.method8031();
        this.aClass19_Sub1_12.method8080(0, Static189.aClass168_2);
        this.aClass19_Sub1_12.method8094(Static209.aClass121_4, Static209.aClass121_4);
        this.aClass19_Sub1_12.method8088(null);
        this.aClass19_Sub1_12.method8138(0);
        this.aClass19_Sub1_12.method8031();
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(IIIII)V")
    @Override
    public void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int op, @OriginalArg(3) int color, @OriginalArg(4) int mode) {
        @Pc(8) Matrix_Sub1 local8 = this.aClass19_Sub1_12.method8040();
        @Pc(13) Matrix_Sub1 local13 = this.aClass19_Sub1_12.method8082();
        @Pc(18) int local18 = y + this.anInt6624;
        @Pc(23) int local23 = x + this.anInt6617;
        this.anInterface18_3.method9041(Static363.aClass259_14);
        this.aClass19_Sub1_12.method8099();
        this.aClass19_Sub1_12.method8088(this.anInterface18_3);
        this.aClass19_Sub1_12.method8053(mode);
        this.aClass19_Sub1_12.method8054(op);
        this.aClass19_Sub1_12.method8080(1, Static454.aClass168_5);
        this.aClass19_Sub1_12.method8142(Static454.aClass168_5, 1);
        this.aClass19_Sub1_12.method8112(color);
        local8.method1884(0.0F, (float) this.anInt6606, (float) this.anInt6626);
        local8.translate(local23, local18, 0);
        local13.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) this.anInt6626));
        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_12.method8042();
        this.aClass19_Sub1_12.method8059();
        this.aClass19_Sub1_12.method8031();
        this.aClass19_Sub1_12.method8080(1, Static188.aClass168_1);
        this.aClass19_Sub1_12.method8142(Static188.aClass168_1, 1);
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void copyRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour) {
        @Pc(13) int[] local13 = this.aClass19_Sub1_12.na(op, colour, width, height);
        if (local13 != null) {
            for (@Pc(17) int local17 = 0; local17 < local13.length; local17++) {
                local13[local17] |= 0xFF000000;
            }
            this.method5914(x, y, width, height, local13, width);
        }
    }

    @OriginalMember(owner = "client!np", name = "b", descriptor = "()I")
    @Override
    public int scaleWidth() {
        return this.anInt6625 + this.anInt6626 + this.anInt6617;
    }

    @OriginalMember(owner = "client!np", name = "b", descriptor = "(IIIIIII)V")
    @Override
    public void renderTiled(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode) {
        @Pc(10) Matrix_Sub1 local10 = this.aClass19_Sub1_12.method8040();
        @Pc(15) Matrix_Sub1 local15 = this.aClass19_Sub1_12.method8082();
        this.anInterface18_3.method9041(Static363.aClass259_14);
        this.aClass19_Sub1_12.method8099();
        this.aClass19_Sub1_12.method8088(this.anInterface18_3);
        this.aClass19_Sub1_12.method8053(mode);
        this.aClass19_Sub1_12.method8054(op);
        this.aClass19_Sub1_12.method8080(1, Static454.aClass168_5);
        this.aClass19_Sub1_12.method8142(Static454.aClass168_5, 1);
        this.aClass19_Sub1_12.method8112(colour);
        @Pc(75) boolean local75 = this.aBoolean496 && this.anInt6624 == 0 && this.anInt6612 == 0;
        @Pc(96) boolean local96 = this.aBoolean492 && this.anInt6617 == 0 && this.anInt6625 == 0;
        if (local96 & local75) {
            local15.method1884(1.0F, this.anInterface18_3.method9050((float) height), this.anInterface18_3.method9046((float) width));
            local10.method1884(0.0F, (float) height, (float) width);
            local10.translate(x, y, 0);
            this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
            this.aClass19_Sub1_12.method8042();
            this.aClass19_Sub1_12.method8059();
        } else {
            @Pc(155) int local155;
            @Pc(158) int local158;
            @Pc(185) int local185;
            @Pc(191) int local191;
            @Pc(246) int local246;
            if (local96) {
                local155 = y + height;
                local158 = this.scaleHeight();
                local15.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) width));
                this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                local185 = this.anInt6624 + y;
                local191 = local185 + this.anInt6606;
                while (local191 <= local155) {
                    local10.method1884(0.0F, (float) this.anInt6606, (float) width);
                    local10.translate(x, local185, 0);
                    this.aClass19_Sub1_12.method8042();
                    local191 += local158;
                    local185 += local158;
                    this.aClass19_Sub1_12.method8059();
                }
                if (local155 > local185) {
                    local246 = local155 - local185;
                    local15.method1884(1.0F, this.anInterface18_3.method9050((float) local246), this.anInterface18_3.method9046((float) width));
                    this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                    local10.method1884(0.0F, (float) local246, (float) width);
                    local10.translate(x, local185, 0);
                    this.aClass19_Sub1_12.method8042();
                    this.aClass19_Sub1_12.method8059();
                }
            } else if (local75) {
                local155 = x + width;
                local158 = this.scaleWidth();
                local15.method1884(1.0F, this.anInterface18_3.method9050((float) height), this.anInterface18_3.method9046((float) this.anInt6626));
                this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                local185 = x + this.anInt6617;
                local191 = this.anInt6626 + local185;
                while (local191 <= local155) {
                    local10.method1884(0.0F, (float) height, (float) this.anInt6626);
                    local10.translate(local185, y, 0);
                    this.aClass19_Sub1_12.method8042();
                    local185 += local158;
                    local191 += local158;
                    this.aClass19_Sub1_12.method8059();
                }
                if (local185 < local155) {
                    local246 = local155 - local185;
                    local15.method1884(1.0F, this.anInterface18_3.method9050((float) height), this.anInterface18_3.method9046((float) local246));
                    this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                    local10.method1884(0.0F, (float) height, (float) local246);
                    local10.translate(local185, y, 0);
                    this.aClass19_Sub1_12.method8042();
                    this.aClass19_Sub1_12.method8059();
                }
            } else {
                local155 = y + height;
                local158 = x + width;
                local185 = this.scaleWidth();
                local191 = this.scaleHeight();
                local246 = this.anInt6624 + y;
                @Pc(351) int local351;
                @Pc(356) int local356;
                @Pc(404) int local404;
                for (@Pc(319) int local319 = local246 + this.anInt6606; local319 <= local155; local319 += local191) {
                    local15.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) this.anInt6626));
                    this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                    local351 = x + this.anInt6617;
                    for (local356 = this.anInt6626 + local351; local356 <= local158; local356 += local185) {
                        local10.method1884(0.0F, (float) this.anInt6606, (float) this.anInt6626);
                        local10.translate(local351, local246, 0);
                        this.aClass19_Sub1_12.method8042();
                        local351 += local185;
                        this.aClass19_Sub1_12.method8059();
                    }
                    if (local158 > local351) {
                        local404 = local158 - local351;
                        local15.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) local404));
                        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                        local10.method1884(0.0F, (float) this.anInt6606, (float) local404);
                        local10.translate(local351, local246, 0);
                        this.aClass19_Sub1_12.method8042();
                        this.aClass19_Sub1_12.method8059();
                    }
                    local246 += local191;
                }
                if (local155 > local246) {
                    local351 = local155 - local246;
                    local15.method1884(1.0F, this.anInterface18_3.method9050((float) local351), this.anInterface18_3.method9046((float) this.anInt6626));
                    this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                    local356 = this.anInt6617 + x;
                    local404 = this.anInt6626 + local356;
                    while (local158 >= local404) {
                        local10.method1884(0.0F, (float) local351, (float) this.anInt6626);
                        local10.translate(local356, local246, 0);
                        this.aClass19_Sub1_12.method8042();
                        this.aClass19_Sub1_12.method8059();
                        local404 += local185;
                        local356 += local185;
                    }
                    if (local158 > local356) {
                        @Pc(558) int local558 = local158 - local356;
                        local15.method1884(1.0F, this.anInterface18_3.method9050((float) local351), this.anInterface18_3.method9046((float) local558));
                        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
                        local10.method1884(0.0F, (float) local351, (float) local558);
                        local10.translate(local356, local246, 0);
                        this.aClass19_Sub1_12.method8042();
                        this.aClass19_Sub1_12.method8059();
                    }
                }
            }
        }
        this.aClass19_Sub1_12.method8031();
        this.aClass19_Sub1_12.method8080(1, Static188.aClass168_1);
        this.aClass19_Sub1_12.method8142(Static188.aClass168_1, 1);
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "([I)V")
    @Override
    public void projectOffsets(@OriginalArg(0) int[] destination) {
        destination[2] = this.anInt6625;
        destination[0] = this.anInt6617;
        destination[3] = this.anInt6612;
        destination[1] = this.anInt6624;
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "()I")
    @Override
    public int scaleHeight() {
        return this.anInt6624 + this.anInt6606 + this.anInt6612;
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(IIIIIIII)V")
    @Override
    protected void renderImpl(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode) {
        @Pc(8) Matrix_Sub1 local8 = this.aClass19_Sub1_12.method8040();
        @Pc(13) Matrix_Sub1 local13 = this.aClass19_Sub1_12.method8082();
        this.anInterface18_3.method9041(this.aBoolean494 || this.aBoolean495 || !(true & true) ? Static363.aClass259_14 : Static60.aClass259_3);
        this.aClass19_Sub1_12.method8099();
        this.aClass19_Sub1_12.method8088(this.anInterface18_3);
        this.aClass19_Sub1_12.method8053(mode);
        this.aClass19_Sub1_12.method8054(op);
        this.aClass19_Sub1_12.method8080(1, Static454.aClass168_5);
        this.aClass19_Sub1_12.method8142(Static454.aClass168_5, 1);
        this.aClass19_Sub1_12.method8112(colour);
        local13.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) this.anInt6626));
        if (this.aBoolean493) {
            width = this.anInt6626 * width / this.scaleWidth();
            height = this.anInt6606 * height / this.scaleHeight();
            x += width * this.anInt6617 / this.anInt6626;
            y += this.anInt6624 * height / this.anInt6606;
        }
        local8.method1884(0.0F, (float) height, (float) width);
        local8.translate(x, y, 0);
        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_12.method8042();
        this.aClass19_Sub1_12.method8059();
        this.aClass19_Sub1_12.method8031();
        this.aClass19_Sub1_12.method8080(1, Static188.aClass168_1);
        this.aClass19_Sub1_12.method8142(Static188.aClass168_1, 1);
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(III)V")
    @Override
    public void copyAlpha(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int channel) {
        @Pc(15) int[] local15 = this.aClass19_Sub1_12.na(0, 0, this.anInt6626, this.anInt6606);
        @Pc(22) int[] local22 = new int[this.anInt6626 * this.anInt6606];
        this.anInterface18_3.method9044(local22, this.anInt6626, this.anInt6606);
        for (@Pc(180) int local180 = 0; local180 < this.anInt6606; local180++) {
            @Pc(187) int local187 = local180 * this.anInt6626;
            for (@Pc(189) int local189 = 0; local189 < this.anInt6626; local189++) {
                local22[local189 + local187] = local22[local187 + local189] & 0xFFFFFF | (local15[local189 + local187] == 0 ? 0 : -16777216);
            }
        }
        this.method5914(0, 0, this.anInt6626, this.anInt6606, local22, this.anInt6626);
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(FFFFFFILclient!aa;II)V")
    @Override
    protected void renderParallelogramImpl(@OriginalArg(0) float centerX, @OriginalArg(1) float centerY, @OriginalArg(2) float x1, @OriginalArg(3) float y1, @OriginalArg(4) float x2, @OriginalArg(5) float y2, @OriginalArg(7) ClippingMask mask, @OriginalArg(8) int maskX, @OriginalArg(9) int maskY) {
        @Pc(8) Matrix_Sub1 local8 = this.aClass19_Sub1_12.method8040();
        @Pc(13) Matrix_Sub1 local13 = this.aClass19_Sub1_12.method8082();
        @Pc(16) ClippingMask_Sub2 local16 = (ClippingMask_Sub2) mask;
        @Pc(19) Interface18 local19 = local16.anInterface18_2;
        this.anInterface18_3.method9041(this.aBoolean494 || this.aBoolean495 || (0x1 & 0x1) == 0 ? Static363.aClass259_14 : Static60.aClass259_3);
        this.aClass19_Sub1_12.method8099();
        this.aClass19_Sub1_12.method8088(this.anInterface18_3);
        this.aClass19_Sub1_12.method8053(1);
        this.aClass19_Sub1_12.method8054(1);
        if (this.aBoolean493) {
            @Pc(75) float local75 = (float) this.anInt6626 / (float) this.scaleWidth();
            @Pc(83) float local83 = (float) this.anInt6606 / (float) this.scaleHeight();
            local8.method1887(local83 * (x2 - centerX), (y1 - centerY) * local75, 1.0F, local75 * (x1 - centerX), 0.0F, 0.0F, 0.0F, 0.0F, local83 * (y2 - centerY));
            local8.method1882(0.0F, (centerY + (float) this.anInt6624) * local83, local75 * ((float) this.anInt6617 + centerX));
        } else {
            local8.method1887(x2 - centerX, -centerY + y1, 1.0F, x1 - centerX, 0.0F, 0.0F, 0.0F, 0.0F, y2 - centerY);
            local8.method1882(0.0F, centerY, centerX);
        }
        local13.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) this.anInt6626));
        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_12.method8138(1);
        this.aClass19_Sub1_12.method8088(local19);
        this.aClass19_Sub1_12.method8094(Static209.aClass121_4, Static185.aClass121_3);
        this.aClass19_Sub1_12.method8080(0, Static188.aClass168_1);
        @Pc(213) Matrix_Sub1 local213 = this.aClass19_Sub1_12.method8082();
        local213.apply(local8);
        local213.translate(-maskX, -maskY, 0);
        local213.method1896(local19.method9050(1.0F), local19.method9046(1.0F), 1.0F);
        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_12.method8042();
        this.aClass19_Sub1_12.method8059();
        this.aClass19_Sub1_12.method8031();
        this.aClass19_Sub1_12.method8080(0, Static189.aClass168_2);
        this.aClass19_Sub1_12.method8094(Static209.aClass121_4, Static209.aClass121_4);
        this.aClass19_Sub1_12.method8088(null);
        this.aClass19_Sub1_12.method8138(0);
        this.aClass19_Sub1_12.method8031();
    }

    @OriginalMember(owner = "client!np", name = "a", descriptor = "(FFFFFFIIII)V")
    @Override
    protected void renderImpl(@OriginalArg(0) float x, @OriginalArg(1) float y, @OriginalArg(2) float width, @OriginalArg(3) float height, @OriginalArg(4) float op, @OriginalArg(5) float colour, @OriginalArg(6) int mode, @OriginalArg(7) int filter) {
        @Pc(8) Matrix_Sub1 local8 = this.aClass19_Sub1_12.method8040();
        @Pc(13) Matrix_Sub1 local13 = this.aClass19_Sub1_12.method8082();
        this.anInterface18_3.method9041(this.aBoolean494 || this.aBoolean495 || (0x1 & 0x1) == 0 ? Static363.aClass259_14 : Static60.aClass259_3);
        this.aClass19_Sub1_12.method8099();
        this.aClass19_Sub1_12.method8088(this.anInterface18_3);
        this.aClass19_Sub1_12.method8053(1);
        this.aClass19_Sub1_12.method8054(mode);
        this.aClass19_Sub1_12.method8080(1, Static454.aClass168_5);
        this.aClass19_Sub1_12.method8142(Static454.aClass168_5, 1);
        this.aClass19_Sub1_12.method8112(filter);
        if (this.aBoolean493) {
            @Pc(82) float local82 = (float) this.scaleWidth();
            @Pc(86) float local86 = (float) this.scaleHeight();
            @Pc(92) float local92 = (width - x) / local82;
            @Pc(99) float local99 = (height - y) / local82;
            @Pc(105) float local105 = (op - x) / local86;
            @Pc(111) float local111 = (colour - y) / local86;
            @Pc(117) float local117 = local105 * (float) this.anInt6624;
            @Pc(123) float local123 = (float) this.anInt6624 * local111;
            @Pc(129) float local129 = local92 * (float) this.anInt6617;
            @Pc(135) float local135 = local99 * (float) this.anInt6617;
            @Pc(142) float local142 = (float) this.anInt6625 * -local92;
            @Pc(149) float local149 = -local99 * (float) this.anInt6625;
            @Pc(156) float local156 = -local105 * (float) this.anInt6612;
            @Pc(163) float local163 = (float) this.anInt6612 * -local111;
            x = x + local129 + local117;
            op = op + local129 + local156;
            height = local123 + (local149 + height);
            y = local123 + (local135 + y);
            width = local142 + width + local117;
            colour = colour + local135 + local163;
        }
        local8.method1887(op - x, -y + height, 1.0F, width - x, 0.0F, 0.0F, 0.0F, 0.0F, colour - y);
        local8.method1882(0.0F, y, x);
        local13.method1884(1.0F, this.anInterface18_3.method9050((float) this.anInt6606), this.anInterface18_3.method9046((float) this.anInt6626));
        this.aClass19_Sub1_12.method8073(Static431.aClass370_5);
        this.aClass19_Sub1_12.method8042();
        this.aClass19_Sub1_12.method8059();
        this.aClass19_Sub1_12.method8031();
        this.aClass19_Sub1_12.method8080(1, Static188.aClass168_1);
        this.aClass19_Sub1_12.method8142(Static188.aClass168_1, 1);
    }
}

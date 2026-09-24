import com.jagex.graphics.Matrix;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qr")
public final class Matrix_Sub3 extends Matrix {

    @OriginalMember(owner = "client!qr", name = "C", descriptor = "F")
    public float aFloat150;

    @OriginalMember(owner = "client!qr", name = "F", descriptor = "F")
    public float aFloat151;

    @OriginalMember(owner = "client!qr", name = "u", descriptor = "F")
    public float aFloat152;

    @OriginalMember(owner = "client!qr", name = "E", descriptor = "F")
    public float aFloat153;

    @OriginalMember(owner = "client!qr", name = "z", descriptor = "F")
    public float aFloat154;

    @OriginalMember(owner = "client!qr", name = "r", descriptor = "F")
    public float aFloat155;

    @OriginalMember(owner = "client!qr", name = "e", descriptor = "F")
    public float aFloat156;

    @OriginalMember(owner = "client!qr", name = "o", descriptor = "F")
    public float aFloat157;

    @OriginalMember(owner = "client!qr", name = "i", descriptor = "F")
    public float aFloat158;

    @OriginalMember(owner = "client!qr", name = "A", descriptor = "F")
    public float aFloat159;

    @OriginalMember(owner = "client!qr", name = "j", descriptor = "F")
    public float aFloat160;

    @OriginalMember(owner = "client!qr", name = "G", descriptor = "F")
    public float aFloat161;

    @OriginalMember(owner = "client!qr", name = "<init>", descriptor = "()V")
    public Matrix_Sub3() {
        this.makeIdentity();
    }

    @OriginalMember(owner = "client!qr", name = "f", descriptor = "(I)V")
    @Override
    public void rotateAxisY(@OriginalArg(0) int arg0) {
        @Pc(9) float local9 = GlToolkit.COS[arg0 & 0x3FFF];
        @Pc(15) float local15 = GlToolkit.SIN[arg0 & 0x3FFF];
        @Pc(18) float local18 = this.aFloat153;
        @Pc(21) float local21 = this.aFloat157;
        @Pc(24) float local24 = this.aFloat160;
        this.aFloat153 = local15 * this.aFloat155 + local9 * local18;
        @Pc(37) float local37 = this.aFloat152;
        this.aFloat155 = local9 * this.aFloat155 - local15 * local18;
        this.aFloat157 = this.aFloat151 * local15 + local21 * local9;
        this.aFloat151 = local9 * this.aFloat151 - local15 * local21;
        this.aFloat160 = this.aFloat154 * local15 + local9 * local24;
        this.aFloat152 = this.aFloat159 * local15 + local9 * local37;
        this.aFloat154 = this.aFloat154 * local9 - local24 * local15;
        this.aFloat159 = local9 * this.aFloat159 - local15 * local37;
    }

    @OriginalMember(owner = "client!qr", name = "c", descriptor = "(III[I)V")
    @Override
    public void project(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int[] destination) {
        destination[0] = (int) (this.aFloat152 + ((float) x * this.aFloat153 + this.aFloat157 * (float) y + this.aFloat160 * (float) z));
        destination[2] = (int) (this.aFloat159 + ((float) x * this.aFloat155 + this.aFloat151 * (float) y + this.aFloat154 * (float) z));
        destination[1] = (int) (this.aFloat161 * (float) x + (float) y * this.aFloat156 + (float) z * this.aFloat150 + this.aFloat158);
    }

    @OriginalMember(owner = "client!qr", name = "i", descriptor = "(I)V")
    public void method7141() {
        this.aFloat155 = -this.aFloat155;
        this.aFloat154 = -this.aFloat154;
        this.aFloat150 = -this.aFloat150;
        this.aFloat161 = -this.aFloat161;
        this.aFloat158 = -this.aFloat158;
        this.aFloat151 = -this.aFloat151;
        this.aFloat156 = -this.aFloat156;
        this.aFloat159 = -this.aFloat159;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(BLclient!tt;)V")
    public void method7142(@OriginalArg(1) Matrix arg0) {
        @Pc(6) Matrix_Sub3 local6 = (Matrix_Sub3) arg0;
        this.aFloat160 = local6.aFloat155;
        this.aFloat153 = local6.aFloat153;
        this.aFloat157 = local6.aFloat161;
        this.aFloat156 = local6.aFloat156;
        this.aFloat161 = local6.aFloat157;
        this.aFloat150 = local6.aFloat151;
        this.aFloat155 = local6.aFloat160;
        this.aFloat152 = -(this.aFloat160 * local6.aFloat159 + (this.aFloat153 * local6.aFloat152 + local6.aFloat158 * this.aFloat157));
        this.aFloat151 = local6.aFloat150;
        this.aFloat154 = local6.aFloat154;
        this.aFloat158 = -(local6.aFloat159 * this.aFloat150 + (local6.aFloat152 * this.aFloat161 + this.aFloat156 * local6.aFloat158));
        this.aFloat159 = -(local6.aFloat152 * this.aFloat155 + local6.aFloat158 * this.aFloat151 + this.aFloat154 * local6.aFloat159);
    }

    @OriginalMember(owner = "client!qr", name = "b", descriptor = "()Lclient!tt;")
    @Override
    public Matrix copy() {
        @Pc(7) Matrix_Sub3 local7 = new Matrix_Sub3();
        local7.aFloat158 = this.aFloat158;
        local7.aFloat156 = this.aFloat156;
        local7.aFloat151 = this.aFloat151;
        local7.aFloat155 = this.aFloat155;
        local7.aFloat161 = this.aFloat161;
        local7.aFloat160 = this.aFloat160;
        local7.aFloat157 = this.aFloat157;
        local7.aFloat154 = this.aFloat154;
        local7.aFloat150 = this.aFloat150;
        local7.aFloat152 = this.aFloat152;
        local7.aFloat159 = this.aFloat159;
        local7.aFloat153 = this.aFloat153;
        return local7;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(III[I)V")
    @Override
    public void projectRelative(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int[] destination) {
        @Pc(6) int local6 = (int) ((float) y - this.aFloat158);
        @Pc(13) int local13 = (int) ((float) x - this.aFloat152);
        @Pc(24) int local24 = (int) ((float) z - this.aFloat159);
        destination[1] = (int) ((float) local24 * this.aFloat151 + ((float) local6 * this.aFloat156 + this.aFloat157 * (float) local13));
        destination[2] = (int) ((float) local13 * this.aFloat160 + this.aFloat150 * (float) local6 + this.aFloat154 * (float) local24);
        destination[0] = (int) ((float) local24 * this.aFloat155 + ((float) local6 * this.aFloat161 + (float) local13 * this.aFloat153));
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "()V")
    @Override
    public void makeIdentity() {
        this.aFloat153 = this.aFloat156 = this.aFloat154 = 1.0F;
        this.aFloat161 = this.aFloat155 = this.aFloat157 = this.aFloat151 = this.aFloat160 = this.aFloat150 = this.aFloat152 = this.aFloat158 = this.aFloat159 = 0.0F;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "([I)V")
    @Override
    public void project(@OriginalArg(0) int[] destination) {
        @Pc(12) float local12 = (float) destination[0] - this.aFloat152;
        @Pc(20) float local20 = (float) destination[1] - this.aFloat158;
        @Pc(29) float local29 = (float) destination[2] - this.aFloat159;
        destination[0] = (int) (this.aFloat161 * local20 + this.aFloat153 * local12 + local29 * this.aFloat155);
        destination[1] = (int) (this.aFloat151 * local29 + (this.aFloat157 * local12 + this.aFloat156 * local20));
        destination[2] = (int) (local29 * this.aFloat154 + (local20 * this.aFloat150 + this.aFloat160 * local12));
    }

    @OriginalMember(owner = "client!qr", name = "b", descriptor = "(I)V")
    @Override
    public void rotateAxisZ(@OriginalArg(0) int arg0) {
        @Pc(9) float local9 = GlToolkit.COS[arg0 & 0x3FFF];
        @Pc(15) float local15 = GlToolkit.SIN[arg0 & 0x3FFF];
        @Pc(18) float local18 = this.aFloat153;
        @Pc(21) float local21 = this.aFloat157;
        @Pc(24) float local24 = this.aFloat160;
        this.aFloat153 = local18 * local9 - local15 * this.aFloat161;
        @Pc(38) float local38 = this.aFloat152;
        this.aFloat157 = local21 * local9 - this.aFloat156 * local15;
        this.aFloat161 = local18 * local15 + this.aFloat161 * local9;
        this.aFloat156 = local21 * local15 + local9 * this.aFloat156;
        this.aFloat160 = local24 * local9 - local15 * this.aFloat150;
        this.aFloat150 = local9 * this.aFloat150 + local24 * local15;
        this.aFloat152 = local38 * local9 - local15 * this.aFloat158;
        this.aFloat158 = local9 * this.aFloat158 + local15 * local38;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(FFFF[FB)V")
    public void method7143(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2, @OriginalArg(3) float arg3, @OriginalArg(4) float[] arg4) {
        arg4[0] = arg3 * this.aFloat153 + this.aFloat157 * arg2 + this.aFloat160 * arg0;
        arg4[1] = this.aFloat161 * arg3 + arg2 * this.aFloat156 + this.aFloat150 * arg0;
        @Pc(74) float local74;
        @Pc(90) float local90;
        @Pc(82) float local82;
        @Pc(66) float local66;
        if (arg3 > 0.00390625F || arg3 < -0.00390625F) {
            local66 = -arg1 / arg3;
            local82 = local66 * this.aFloat155 + this.aFloat159;
            local90 = this.aFloat158 + local66 * this.aFloat161;
            local74 = this.aFloat152 + this.aFloat153 * local66;
        } else if (arg2 > 0.00390625F || arg2 < -0.00390625F) {
            local66 = -arg1 / arg2;
            local90 = this.aFloat156 * local66 + this.aFloat158;
            local82 = this.aFloat159 + local66 * this.aFloat151;
            local74 = this.aFloat157 * local66 + this.aFloat152;
        } else {
            local66 = -arg1 / arg0;
            local74 = local66 * this.aFloat160 + this.aFloat152;
            local82 = this.aFloat154 * local66 + this.aFloat159;
            local90 = this.aFloat158 + this.aFloat150 * local66;
        }
        arg4[2] = arg0 * this.aFloat154 + (this.aFloat155 * arg3 + this.aFloat151 * arg2);
        arg4[3] = -(arg4[1] * local90 + local74 * arg4[0] + local82 * arg4[2]);
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void createCamera(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int rotateX, @OriginalArg(4) int rotateY, @OriginalArg(5) int rotateZ) {
        @Pc(9) float local9 = GlToolkit.COS[rotateX & 0x3FFF];
        @Pc(15) float local15 = GlToolkit.SIN[rotateX & 0x3FFF];
        @Pc(21) float local21 = GlToolkit.COS[rotateY & 0x3FFF];
        @Pc(27) float local27 = GlToolkit.SIN[rotateY & 0x3FFF];
        @Pc(33) float local33 = GlToolkit.COS[rotateZ & 0x3FFF];
        @Pc(39) float local39 = GlToolkit.SIN[rotateZ & 0x3FFF];
        @Pc(43) float local43 = local33 * local15;
        @Pc(47) float local47 = local15 * local39;
        this.aFloat151 = -local15;
        this.aFloat150 = local27 * local39 + local21 * local43;
        this.aFloat161 = local27 * local43 + -local21 * local39;
        this.aFloat155 = local27 * local9;
        this.aFloat160 = local47 * local21 + -local27 * local33;
        this.aFloat157 = local9 * local39;
        this.aFloat154 = local21 * local9;
        this.aFloat156 = local9 * local33;
        this.aFloat153 = local27 * local47 + local21 * local33;
        this.aFloat158 = this.aFloat161 * (float) -x - this.aFloat156 * (float) y - this.aFloat150 * (float) z;
        this.aFloat152 = (float) -x * this.aFloat153 - (float) y * this.aFloat157 - this.aFloat160 * (float) z;
        this.aFloat159 = this.aFloat155 * (float) -x - (float) y * this.aFloat151 - (float) z * this.aFloat154;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(BIFIIFF)V")
    public void method7144(@OriginalArg(1) int arg0, @OriginalArg(2) float arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) float arg4, @OriginalArg(6) float arg5) {
        if (arg2 == 0) {
            this.aFloat154 = 1.0F;
            this.aFloat153 = (float) arg0;
            this.aFloat161 = this.aFloat155 = this.aFloat157 = this.aFloat151 = this.aFloat160 = this.aFloat150 = 0.0F;
            this.aFloat156 = (float) arg3;
        } else {
            @Pc(10) float local10 = GlToolkit.COS[arg2 & 0x3FFF];
            @Pc(16) float local16 = GlToolkit.SIN[arg2 & 0x3FFF];
            this.aFloat156 = local10 * (float) arg3;
            this.aFloat153 = (float) arg0 * local10;
            this.aFloat161 = (float) arg0 * local16;
            this.aFloat155 = this.aFloat151 = this.aFloat160 = this.aFloat150 = 0.0F;
            this.aFloat157 = -local16 * (float) arg3;
            this.aFloat154 = 1.0F;
        }
        this.aFloat159 = arg4;
        this.aFloat158 = arg5;
        this.aFloat152 = arg1;
    }

    @OriginalMember(owner = "client!qr", name = "b", descriptor = "(III)V")
    @Override
    public void translate(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z) {
        this.aFloat159 += (float) z;
        this.aFloat152 += (float) x;
        this.aFloat158 += (float) y;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(B)[F")
    public float[] method7145() {
        Static614.aFloatArray67[0] = this.aFloat153;
        Static614.aFloatArray67[2] = this.aFloat155;
        Static614.aFloatArray67[13] = 0.0F;
        Static614.aFloatArray67[1] = this.aFloat161;
        Static614.aFloatArray67[14] = 0.0F;
        Static614.aFloatArray67[6] = this.aFloat151;
        Static614.aFloatArray67[4] = this.aFloat157;
        Static614.aFloatArray67[10] = this.aFloat154;
        Static614.aFloatArray67[12] = 0.0F;
        Static614.aFloatArray67[9] = this.aFloat150;
        Static614.aFloatArray67[8] = this.aFloat160;
        Static614.aFloatArray67[5] = this.aFloat156;
        return Static614.aFloatArray67;
    }

    @OriginalMember(owner = "client!qr", name = "e", descriptor = "(I)V")
    @Override
    public void makeRotationZ(@OriginalArg(0) int arg0) {
        this.aFloat154 = 1.0F;
        this.aFloat153 = this.aFloat156 = GlToolkit.COS[arg0 & 0x3FFF];
        this.aFloat161 = GlToolkit.SIN[arg0 & 0x3FFF];
        this.aFloat160 = this.aFloat152 = this.aFloat150 = this.aFloat158 = this.aFloat155 = this.aFloat151 = this.aFloat159 = 0.0F;
        this.aFloat157 = -this.aFloat161;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(I)V")
    @Override
    public void makeRotationX(@OriginalArg(0) int arg0) {
        this.aFloat153 = 1.0F;
        this.aFloat156 = this.aFloat154 = GlToolkit.COS[arg0 & 0x3FFF];
        this.aFloat151 = GlToolkit.SIN[arg0 & 0x3FFF];
        this.aFloat150 = -this.aFloat151;
        this.aFloat157 = this.aFloat160 = this.aFloat152 = this.aFloat161 = this.aFloat158 = this.aFloat155 = this.aFloat159 = 0.0F;
    }

    @OriginalMember(owner = "client!qr", name = "c", descriptor = "(I)V")
    @Override
    public void rotateAxisX(@OriginalArg(0) int arg0) {
        @Pc(9) float local9 = GlToolkit.COS[arg0 & 0x3FFF];
        @Pc(15) float local15 = GlToolkit.SIN[arg0 & 0x3FFF];
        @Pc(18) float local18 = this.aFloat161;
        @Pc(21) float local21 = this.aFloat156;
        @Pc(24) float local24 = this.aFloat150;
        this.aFloat161 = local18 * local9 - local15 * this.aFloat155;
        @Pc(37) float local37 = this.aFloat158;
        this.aFloat155 = local9 * this.aFloat155 + local18 * local15;
        this.aFloat156 = local21 * local9 - this.aFloat151 * local15;
        this.aFloat150 = local24 * local9 - local15 * this.aFloat154;
        this.aFloat151 = this.aFloat151 * local9 + local15 * local21;
        this.aFloat154 = local9 * this.aFloat154 + local15 * local24;
        this.aFloat158 = local9 * local37 - this.aFloat159 * local15;
        this.aFloat159 = this.aFloat159 * local9 + local15 * local37;
    }

    @OriginalMember(owner = "client!qr", name = "j", descriptor = "(I)[F")
    public float[] method7146() {
        Static614.aFloatArray67[12] = this.aFloat152;
        Static614.aFloatArray67[0] = this.aFloat153;
        Static614.aFloatArray67[5] = this.aFloat156;
        Static614.aFloatArray67[1] = this.aFloat161;
        Static614.aFloatArray67[2] = this.aFloat155;
        Static614.aFloatArray67[8] = this.aFloat160;
        Static614.aFloatArray67[10] = this.aFloat154;
        Static614.aFloatArray67[4] = this.aFloat157;
        Static614.aFloatArray67[9] = this.aFloat150;
        Static614.aFloatArray67[14] = this.aFloat159;
        Static614.aFloatArray67[13] = this.aFloat158;
        Static614.aFloatArray67[6] = this.aFloat151;
        return Static614.aFloatArray67;
    }

    @OriginalMember(owner = "client!qr", name = "b", descriptor = "(III[I)V")
    @Override
    public void projectDirection(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int[] destination) {
        destination[2] = (int) ((float) x * this.aFloat155 + this.aFloat151 * (float) y + this.aFloat154 * (float) z);
        destination[0] = (int) (this.aFloat160 * (float) z + ((float) x * this.aFloat153 + (float) y * this.aFloat157));
        destination[1] = (int) ((float) y * this.aFloat156 + this.aFloat161 * (float) x + this.aFloat150 * (float) z);
    }

    @OriginalMember(owner = "client!qr", name = "g", descriptor = "(I)V")
    @Override
    public void rotate(@OriginalArg(0) int angle) {
        this.aFloat156 = 1.0F;
        this.aFloat153 = this.aFloat154 = GlToolkit.COS[angle & 0x3FFF];
        this.aFloat160 = GlToolkit.SIN[angle & 0x3FFF];
        this.aFloat157 = this.aFloat152 = this.aFloat161 = this.aFloat150 = this.aFloat158 = this.aFloat151 = this.aFloat159 = 0.0F;
        this.aFloat155 = -this.aFloat160;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(III)V")
    @Override
    public void applyTranslation(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        this.aFloat159 = (float) arg2;
        this.aFloat161 = this.aFloat155 = this.aFloat157 = this.aFloat151 = this.aFloat160 = this.aFloat150 = 0.0F;
        this.aFloat158 = (float) arg1;
        this.aFloat153 = this.aFloat156 = this.aFloat154 = 1.0F;
        this.aFloat152 = (float) arg0;
    }

    @OriginalMember(owner = "client!qr", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void apply(@OriginalArg(0) Matrix other) {
        @Pc(6) Matrix_Sub3 local6 = (Matrix_Sub3) other;
        this.aFloat155 = local6.aFloat155;
        this.aFloat160 = local6.aFloat160;
        this.aFloat158 = local6.aFloat158;
        this.aFloat154 = local6.aFloat154;
        this.aFloat161 = local6.aFloat161;
        this.aFloat152 = local6.aFloat152;
        this.aFloat151 = local6.aFloat151;
        this.aFloat150 = local6.aFloat150;
        this.aFloat157 = local6.aFloat157;
        this.aFloat159 = local6.aFloat159;
        this.aFloat156 = local6.aFloat156;
        this.aFloat153 = local6.aFloat153;
    }
}

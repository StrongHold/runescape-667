import com.jagex.graphics.Matrix;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!eaa")
public final class JavaMatrix extends Matrix {

    @OriginalMember(owner = "client!eaa", name = "y", descriptor = "F")
    public float e2_3;

    @OriginalMember(owner = "client!eaa", name = "l", descriptor = "F")
    public float e2_2;

    @OriginalMember(owner = "client!eaa", name = "H", descriptor = "F")
    public float e1_3;

    @OriginalMember(owner = "client!eaa", name = "q", descriptor = "F")
    public float e3_2;

    @OriginalMember(owner = "client!eaa", name = "e", descriptor = "F")
    public float e1_2;

    @OriginalMember(owner = "client!eaa", name = "A", descriptor = "F")
    public float e3_1;

    @OriginalMember(owner = "client!eaa", name = "r", descriptor = "F")
    public float e2_1;

    @OriginalMember(owner = "client!eaa", name = "m", descriptor = "F")
    public float ty;

    @OriginalMember(owner = "client!eaa", name = "I", descriptor = "F")
    public float e1_1;

    @OriginalMember(owner = "client!eaa", name = "E", descriptor = "F")
    public float tx;

    @OriginalMember(owner = "client!eaa", name = "t", descriptor = "F")
    public float e3_3;

    @OriginalMember(owner = "client!eaa", name = "F", descriptor = "F")
    public float tz;

    @OriginalMember(owner = "client!eaa", name = "<init>", descriptor = "()V")
    public JavaMatrix() {
        this.makeIdentity();
    }

    @OriginalMember(owner = "client!eaa", name = "b", descriptor = "(III)V")
    @Override
    public void translate(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z) {
        this.ty += (float) y;
        this.tz += (float) z;
        this.tx += (float) x;
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "([I)V")
    @Override
    public void project(@OriginalArg(0) int[] destination) {
        @Pc(11) float local11 = (float) destination[0] - this.tx;
        @Pc(19) float local19 = (float) destination[1] - this.ty;
        @Pc(27) float local27 = (float) destination[2] - this.tz;
        destination[0] = (int) (this.e3_1 * local27 + (this.e2_1 * local19 + local11 * this.e1_1));
        destination[1] = (int) (local27 * this.e3_2 + (this.e2_2 * local19 + this.e1_2 * local11));
        destination[2] = (int) (this.e3_3 * local27 + (local11 * this.e1_3 + this.e2_3 * local19));
    }

    @OriginalMember(owner = "client!eaa", name = "e", descriptor = "(I)V")
    @Override
    public void makeRotationZ(@OriginalArg(0) int arg0) {
        this.e3_3 = 1.0F;
        this.e1_1 = this.e2_2 = Matrix.COS[arg0 & 0x3FFF];
        this.e2_1 = Matrix.SIN[arg0 & 0x3FFF];
        this.e1_3 = this.tx = this.e2_3 = this.ty = this.e3_1 = this.e3_2 = this.tz = 0.0F;
        this.e1_2 = -this.e2_1;
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "(III)V")
    @Override
    public void applyTranslation(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        this.e2_1 = this.e3_1 = this.e1_2 = this.e3_2 = this.e1_3 = this.e2_3 = 0.0F;
        this.tz = (float) arg2;
        this.e1_1 = this.e2_2 = this.e3_3 = 1.0F;
        this.ty = (float) arg1;
        this.tx = (float) arg0;
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "(III[I)V")
    @Override
    public void projectRelative(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int[] destination) {
        @Pc(6) int local6 = (int) ((float) z - this.tz);
        @Pc(17) int local17 = (int) ((float) y - this.ty);
        @Pc(24) int local24 = (int) ((float) x - this.tx);
        destination[2] = (int) (this.e1_3 * (float) local24 + (float) local17 * this.e2_3 + (float) local6 * this.e3_3);
        destination[1] = (int) (this.e3_2 * (float) local6 + (this.e1_2 * (float) local24 + this.e2_2 * (float) local17));
        destination[0] = (int) ((float) local6 * this.e3_1 + (this.e1_1 * (float) local24 + this.e2_1 * (float) local17));
    }

    @OriginalMember(owner = "client!eaa", name = "c", descriptor = "(I)V")
    @Override
    public void rotateAxisX(@OriginalArg(0) int arg0) {
        @Pc(9) float local9 = Matrix.COS[arg0 & 0x3FFF];
        @Pc(15) float local15 = Matrix.SIN[arg0 & 0x3FFF];
        @Pc(18) float local18 = this.e2_1;
        @Pc(21) float local21 = this.e2_2;
        @Pc(24) float local24 = this.e2_3;
        @Pc(27) float local27 = this.ty;
        this.e2_1 = local18 * local9 - this.e3_1 * local15;
        this.e3_1 = local15 * local18 + local9 * this.e3_1;
        this.e2_2 = local9 * local21 - this.e3_2 * local15;
        this.e3_2 = this.e3_2 * local9 + local21 * local15;
        this.e2_3 = local9 * local24 - this.e3_3 * local15;
        this.e3_3 = local9 * this.e3_3 + local15 * local24;
        this.ty = local9 * local27 - this.tz * local15;
        this.tz = local9 * this.tz + local15 * local27;
    }

    @OriginalMember(owner = "client!eaa", name = "c", descriptor = "(III[I)V")
    @Override
    public void project(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int[] destination) {
        destination[0] = (int) (this.e1_2 * (float) y + this.e1_1 * (float) x + (float) z * this.e1_3 + this.tx);
        destination[1] = (int) (this.e2_3 * (float) z + (this.e2_1 * (float) x + (float) y * this.e2_2) + this.ty);
        destination[2] = (int) ((float) z * this.e3_3 + ((float) x * this.e3_1 + (float) y * this.e3_2) + this.tz);
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void createCamera(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int rotateX, @OriginalArg(4) int rotateY, @OriginalArg(5) int rotateZ) {
        @Pc(9) float local9 = Matrix.COS[rotateX & 0x3FFF];
        @Pc(15) float local15 = Matrix.SIN[rotateX & 0x3FFF];
        @Pc(21) float local21 = Matrix.COS[rotateY & 0x3FFF];
        @Pc(27) float local27 = Matrix.SIN[rotateY & 0x3FFF];
        @Pc(33) float local33 = Matrix.COS[rotateZ & 0x3FFF];
        @Pc(39) float local39 = Matrix.SIN[rotateZ & 0x3FFF];
        @Pc(43) float local43 = local33 * local15;
        @Pc(47) float local47 = local39 * local15;
        this.e1_2 = local9 * local39;
        this.e2_3 = local39 * local27 + local21 * local43;
        this.e3_2 = -local15;
        this.e3_3 = local9 * local21;
        this.e1_3 = -local27 * local33 + local21 * local47;
        this.e3_1 = local9 * local27;
        this.e2_2 = local9 * local33;
        this.e2_1 = local39 * -local21 + local43 * local27;
        this.e1_1 = local21 * local33 + local47 * local27;
        this.tz = (float) -x * this.e3_1 - (float) y * this.e3_2 - (float) z * this.e3_3;
        this.tx = this.e1_1 * (float) -x - this.e1_2 * (float) y - this.e1_3 * (float) z;
        this.ty = -((float) y * this.e2_2) + this.e2_1 * (float) -x - (float) z * this.e2_3;
    }

    @OriginalMember(owner = "client!eaa", name = "b", descriptor = "(III[I)V")
    @Override
    public void projectDirection(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int[] destination) {
        destination[1] = (int) (this.e2_2 * (float) y + this.e2_1 * (float) x + (float) z * this.e2_3);
        destination[0] = (int) ((float) x * this.e1_1 + (float) y * this.e1_2 + this.e1_3 * (float) z);
        destination[2] = (int) ((float) z * this.e3_3 + (this.e3_2 * (float) y + (float) x * this.e3_1));
    }

    @OriginalMember(owner = "client!eaa", name = "b", descriptor = "()Lclient!tt;")
    @Override
    public Matrix copy() {
        @Pc(7) JavaMatrix local7 = new JavaMatrix();
        local7.tz = this.tz;
        local7.e1_3 = this.e1_3;
        local7.e2_2 = this.e2_2;
        local7.e2_3 = this.e2_3;
        local7.e3_3 = this.e3_3;
        local7.tx = this.tx;
        local7.ty = this.ty;
        local7.e1_2 = this.e1_2;
        local7.e3_2 = this.e3_2;
        local7.e1_1 = this.e1_1;
        local7.e3_1 = this.e3_1;
        local7.e2_1 = this.e2_1;
        return local7;
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "()V")
    @Override
    public void makeIdentity() {
        this.e1_1 = this.e2_2 = this.e3_3 = 1.0F;
        this.e2_1 = this.e3_1 = this.e1_2 = this.e3_2 = this.e1_3 = this.e2_3 = this.tx = this.ty = this.tz = 0.0F;
    }

    @OriginalMember(owner = "client!eaa", name = "g", descriptor = "(I)V")
    @Override
    public void rotate(@OriginalArg(0) int angle) {
        this.e2_2 = 1.0F;
        this.e1_1 = this.e3_3 = Matrix.COS[angle & 0x3FFF];
        this.e1_3 = Matrix.SIN[angle & 0x3FFF];
        this.e1_2 = this.tx = this.e2_1 = this.e2_3 = this.ty = this.e3_2 = this.tz = 0.0F;
        this.e3_1 = -this.e1_3;
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "(I)V")
    @Override
    public void makeRotationX(@OriginalArg(0) int arg0) {
        this.e1_1 = 1.0F;
        this.e2_2 = this.e3_3 = Matrix.COS[arg0 & 0x3FFF];
        this.e3_2 = Matrix.SIN[arg0 & 0x3FFF];
        this.e2_3 = -this.e3_2;
        this.e1_2 = this.e1_3 = this.tx = this.e2_1 = this.ty = this.e3_1 = this.tz = 0.0F;
    }

    @OriginalMember(owner = "client!eaa", name = "b", descriptor = "(I)V")
    @Override
    public void rotateAxisZ(@OriginalArg(0) int arg0) {
        @Pc(9) float local9 = Matrix.COS[arg0 & 0x3FFF];
        @Pc(15) float local15 = Matrix.SIN[arg0 & 0x3FFF];
        @Pc(18) float local18 = this.e1_1;
        @Pc(21) float local21 = this.e1_2;
        @Pc(24) float local24 = this.e1_3;
        @Pc(27) float local27 = this.tx;
        this.e1_1 = local9 * local18 - local15 * this.e2_1;
        this.e1_2 = local9 * local21 - this.e2_2 * local15;
        this.e2_1 = this.e2_1 * local9 + local15 * local18;
        this.e2_2 = this.e2_2 * local9 + local15 * local21;
        this.e1_3 = local24 * local9 - this.e2_3 * local15;
        this.e2_3 = local15 * local24 + local9 * this.e2_3;
        this.tx = local9 * local27 - this.ty * local15;
        this.ty = this.ty * local9 + local15 * local27;
    }

    @OriginalMember(owner = "client!eaa", name = "f", descriptor = "(I)V")
    @Override
    public void rotateAxisY(@OriginalArg(0) int arg0) {
        @Pc(9) float local9 = Matrix.COS[arg0 & 0x3FFF];
        @Pc(15) float local15 = Matrix.SIN[arg0 & 0x3FFF];
        @Pc(18) float local18 = this.e1_1;
        @Pc(21) float local21 = this.e1_2;
        @Pc(24) float local24 = this.e1_3;
        this.e1_1 = local18 * local9 + local15 * this.e3_1;
        @Pc(37) float local37 = this.tx;
        this.e3_1 = local9 * this.e3_1 - local18 * local15;
        this.e1_2 = local15 * this.e3_2 + local21 * local9;
        this.e3_2 = this.e3_2 * local9 - local21 * local15;
        this.e1_3 = local9 * local24 + local15 * this.e3_3;
        this.tx = local37 * local9 + local15 * this.tz;
        this.e3_3 = local9 * this.e3_3 - local24 * local15;
        this.tz = local9 * this.tz - local37 * local15;
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void apply(@OriginalArg(0) Matrix other) {
        @Pc(6) JavaMatrix local6 = (JavaMatrix) other;
        this.e3_1 = local6.e3_1;
        this.e1_3 = local6.e1_3;
        this.e2_2 = local6.e2_2;
        this.e3_2 = local6.e3_2;
        this.e2_1 = local6.e2_1;
        this.tz = local6.tz;
        this.e1_1 = local6.e1_1;
        this.e1_2 = local6.e1_2;
        this.e3_3 = local6.e3_3;
        this.ty = local6.ty;
        this.e2_3 = local6.e2_3;
        this.tx = local6.tx;
    }
}

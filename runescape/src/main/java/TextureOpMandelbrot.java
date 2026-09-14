import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.MonochromeImageCache;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Plots the Mandelbrot set, taking each texel as a point on the complex plane and shading it by how
 * many iterations the sequence survives before it escapes. Points that never escape come out black.
 */
@OriginalClass("client!via")
public final class TextureOpMandelbrot extends TextureOp {

    @OriginalMember(owner = "client!via", name = "K", descriptor = "I")
    public int scale = 1365;

    @OriginalMember(owner = "client!via", name = "I", descriptor = "I")
    public int iterations = 20;

    @OriginalMember(owner = "client!via", name = "G", descriptor = "I")
    public int offsetX = 0;

    @OriginalMember(owner = "client!via", name = "L", descriptor = "I")
    public int offsetY = 0;

    @OriginalMember(owner = "client!via", name = "<init>", descriptor = "()V")
    public TextureOpMandelbrot() {
        super(0, true);
    }

    @OriginalMember(owner = "client!via", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.scale = arg1.g2();
        } else if (arg2 == 1) {
            this.iterations = arg1.g2();
        } else if (arg2 == 2) {
            this.offsetX = arg1.g2();
        } else if (arg2 == 3) {
            this.offsetY = arg1.g2();
        }
        if (arg0) {
            WorldList.decodePopulations(null);
        }
    }

    @OriginalMember(owner = "client!via", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            for (@Pc(17) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(31) int constantReal = this.offsetX + (EnvironmentLight.anIntArray92[x] << 12) / this.scale;
                @Pc(43) int constantImaginary = (MonochromeImageCache.anIntArray341[y] << 12) / this.scale + this.offsetY;
                @Pc(49) int real = constantReal;
                @Pc(51) int imaginary = constantImaginary;
                @Pc(57) int realSquared = constantReal * constantReal >> 12;
                @Pc(63) int imaginarySquared = constantImaginary * constantImaginary >> 12;
                @Pc(65) int iteration = 0;
                while (realSquared + imaginarySquared < 16384 && this.iterations > iteration) {
                    imaginary = constantImaginary + (imaginary * real >> 12) * 2;
                    real = constantReal + realSquared - imaginarySquared;
                    iteration++;
                    realSquared = real * real >> 12;
                    imaginarySquared = imaginary * imaginary >> 12;
                }
                output[x] = iteration >= this.iterations - 1 ? 0 : (iteration << 12) / this.iterations;
            }
        }
        if (arg0 < 107) {
            WorldList.decodePopulations(null);
        }
        return output;
    }
}

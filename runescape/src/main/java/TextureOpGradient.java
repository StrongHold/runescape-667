import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Maps the intensity of a source layer onto a colour gradient, either one built from decoded stops
 * or one of the six built in presets.
 */
@OriginalClass("client!kg")
public final class TextureOpGradient extends TextureOp {

    /**
     * The gradient stops, each held as {position, red, green, blue} in 12 bit fixed point.
     */
    @OriginalMember(owner = "client!kg", name = "Q", descriptor = "[[I")
    public int[][] stops;

    /**
     * The gradient sampled into 257 packed 0xRRGGBB entries, built from the stops by buildRamp.
     */
    @OriginalMember(owner = "client!kg", name = "G", descriptor = "[I")
    public final int[] ramp = new int[257];

    @OriginalMember(owner = "client!kg", name = "<init>", descriptor = "()V")
    public TextureOpGradient() {
        super(1, false);
    }

    @OriginalMember(owner = "client!kg", name = "a", descriptor = "(IB)V")
    public void setPreset(@OriginalArg(0) int preset) {
        if (preset == 0) {
            return;
        }
        if (preset == 1) {
            this.stops = new int[2][4];
            this.stops[0][1] = 0;
            this.stops[0][3] = 0;
            this.stops[0][0] = 0;
            this.stops[0][2] = 0;
            this.stops[1][3] = 4096;
            this.stops[1][1] = 4096;
            this.stops[1][0] = 4096;
            this.stops[1][2] = 4096;
        } else if (preset == 2) {
            this.stops = new int[8][4];
            this.stops[0][3] = 2361;
            this.stops[0][1] = 2650;
            this.stops[0][2] = 2602;
            this.stops[0][0] = 0;
            this.stops[1][1] = 2313;
            this.stops[1][3] = 1558;
            this.stops[1][0] = 2867;
            this.stops[1][2] = 1799;
            this.stops[2][3] = 1413;
            this.stops[2][0] = 3072;
            this.stops[2][2] = 1734;
            this.stops[2][1] = 2618;
            this.stops[3][2] = 1220;
            this.stops[3][3] = 947;
            this.stops[3][1] = 2296;
            this.stops[3][0] = 3276;
            this.stops[4][2] = 963;
            this.stops[4][1] = 2072;
            this.stops[4][3] = 722;
            this.stops[4][0] = 3481;
            this.stops[5][1] = 2730;
            this.stops[5][2] = 2152;
            this.stops[5][0] = 3686;
            this.stops[5][3] = 1766;
            this.stops[6][2] = 1060;
            this.stops[6][3] = 915;
            this.stops[6][0] = 3891;
            this.stops[6][1] = 2232;
            this.stops[7][2] = 1413;
            this.stops[7][0] = 4096;
            this.stops[7][3] = 1140;
            this.stops[7][1] = 1686;
        } else if (preset == 3) {
            this.stops = new int[7][4];
            this.stops[0][2] = 0;
            this.stops[0][3] = 4096;
            this.stops[0][1] = 0;
            this.stops[0][0] = 0;
            this.stops[1][2] = 4096;
            this.stops[1][0] = 663;
            this.stops[1][1] = 0;
            this.stops[1][3] = 4096;
            this.stops[2][3] = 0;
            this.stops[2][0] = 1363;
            this.stops[2][2] = 4096;
            this.stops[2][1] = 0;
            this.stops[3][1] = 4096;
            this.stops[3][3] = 0;
            this.stops[3][0] = 2048;
            this.stops[3][2] = 4096;
            this.stops[4][0] = 2727;
            this.stops[4][3] = 0;
            this.stops[4][2] = 0;
            this.stops[4][1] = 4096;
            this.stops[5][2] = 0;
            this.stops[5][0] = 3411;
            this.stops[5][3] = 4096;
            this.stops[5][1] = 4096;
            this.stops[6][2] = 0;
            this.stops[6][3] = 4096;
            this.stops[6][1] = 0;
            this.stops[6][0] = 4096;
        } else if (preset == 4) {
            this.stops = new int[6][4];
            this.stops[0][0] = 0;
            this.stops[0][1] = 0;
            this.stops[0][3] = 0;
            this.stops[0][2] = 0;
            this.stops[1][3] = 1493;
            this.stops[1][0] = 1843;
            this.stops[1][1] = 0;
            this.stops[1][2] = 0;
            this.stops[2][0] = 2457;
            this.stops[2][3] = 2939;
            this.stops[2][2] = 0;
            this.stops[2][1] = 0;
            this.stops[3][0] = 2781;
            this.stops[3][3] = 3565;
            this.stops[3][1] = 0;
            this.stops[3][2] = 1124;
            this.stops[4][3] = 4031;
            this.stops[4][2] = 3084;
            this.stops[4][1] = 546;
            this.stops[4][0] = 3481;
            this.stops[5][1] = 4096;
            this.stops[5][2] = 4096;
            this.stops[5][3] = 4096;
            this.stops[5][0] = 4096;
        } else if (preset == 5) {
            this.stops = new int[16][4];
            this.stops[0][1] = 80;
            this.stops[0][0] = 0;
            this.stops[0][2] = 192;
            this.stops[0][3] = 321;
            this.stops[1][3] = 562;
            this.stops[1][1] = 321;
            this.stops[1][0] = 155;
            this.stops[1][2] = 449;
            this.stops[2][0] = 389;
            this.stops[2][3] = 803;
            this.stops[2][2] = 690;
            this.stops[2][1] = 578;
            this.stops[3][3] = 1140;
            this.stops[3][2] = 995;
            this.stops[3][0] = 671;
            this.stops[3][1] = 947;
            this.stops[4][2] = 1397;
            this.stops[4][0] = 897;
            this.stops[4][3] = 1509;
            this.stops[4][1] = 1285;
            this.stops[5][0] = 1175;
            this.stops[5][2] = 1429;
            this.stops[5][3] = 1413;
            this.stops[5][1] = 1525;
            this.stops[6][0] = 1368;
            this.stops[6][3] = 1333;
            this.stops[6][2] = 1461;
            this.stops[6][1] = 1734;
            this.stops[7][3] = 1702;
            this.stops[7][1] = 1413;
            this.stops[7][0] = 1507;
            this.stops[7][2] = 1525;
            this.stops[8][1] = 1108;
            this.stops[8][0] = 1736;
            this.stops[8][2] = 1590;
            this.stops[8][3] = 2056;
            this.stops[9][1] = 1766;
            this.stops[9][2] = 2056;
            this.stops[9][3] = 2666;
            this.stops[9][0] = 2088;
            this.stops[10][0] = 2355;
            this.stops[10][1] = 2409;
            this.stops[10][2] = 2586;
            this.stops[10][3] = 3276;
            this.stops[11][3] = 3228;
            this.stops[11][1] = 3116;
            this.stops[11][0] = 2691;
            this.stops[11][2] = 3148;
            this.stops[12][2] = 3710;
            this.stops[12][3] = 3196;
            this.stops[12][1] = 3806;
            this.stops[12][0] = 3031;
            this.stops[13][0] = 3522;
            this.stops[13][1] = 3437;
            this.stops[13][3] = 3019;
            this.stops[13][2] = 3421;
            this.stops[14][3] = 3228;
            this.stops[14][2] = 3148;
            this.stops[14][0] = 3727;
            this.stops[14][1] = 3116;
            this.stops[15][1] = 2377;
            this.stops[15][0] = 4096;
            this.stops[15][3] = 2746;
            this.stops[15][2] = 2505;
        } else if (preset == 6) {
            this.stops = new int[4][4];
            this.stops[0][3] = 0;
            this.stops[0][2] = 4096;
            this.stops[0][0] = 2048;
            this.stops[0][1] = 0;
            this.stops[1][2] = 4096;
            this.stops[1][0] = 2867;
            this.stops[1][1] = 4096;
            this.stops[1][3] = 0;
            this.stops[2][1] = 4096;
            this.stops[2][0] = 3276;
            this.stops[2][2] = 4096;
            this.stops[2][3] = 0;
            this.stops[3][3] = 0;
            this.stops[3][2] = 0;
            this.stops[3][0] = 4096;
            this.stops[3][1] = 4096;
        } else {
            throw new RuntimeException("Invalid gradient preset");
        }
    }

    @OriginalMember(owner = "client!kg", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(19) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(29) int[] source = this.method9422(y, 0);
            @Pc(33) int[] outputRed = output[0];
            @Pc(37) int[] outputGreen = output[1];
            @Pc(41) int[] outputBlue = output[2];
            for (@Pc(43) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(51) int value = source[x] >> 4;
                if (value < 0) {
                    value = 0;
                }
                if (value > 256) {
                    value = 256;
                }
                value = this.ramp[value];
                outputRed[x] = value >> 12 & 0xFF0;
                outputGreen[x] = value >> 4 & 0xFF0;
                outputBlue[x] = (value & 0xFF) << 4;
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!kg", name = "d", descriptor = "(B)V")
    public void buildRamp() {
        @Pc(16) int stopCount = this.stops.length;
        if (stopCount <= 0) {
            return;
        }
        for (@Pc(20) int index = 0; index < 257; index++) {
            @Pc(24) int upperIndex = 0;
            @Pc(28) int position = index << 4;
            for (@Pc(30) int i = 0; i < stopCount && position >= this.stops[i][0]; i++) {
                upperIndex++;
            }
            @Pc(137) int red;
            @Pc(122) int green;
            @Pc(107) int blue;
            @Pc(63) int[] upper;
            if (stopCount > upperIndex) {
                upper = this.stops[upperIndex];
                if (upperIndex > 0) {
                    @Pc(72) int[] lower = this.stops[upperIndex - 1];
                    @Pc(89) int weight = (position - lower[0] << 12) / (upper[0] - lower[0]);
                    @Pc(93) int inverse = 4096 - weight;
                    blue = lower[3] * inverse + weight * upper[3] >> 12;
                    green = upper[2] * weight + lower[2] * inverse >> 12;
                    red = weight * upper[1] + lower[1] * inverse >> 12;
                } else {
                    green = upper[2];
                    red = upper[1];
                    blue = upper[3];
                }
            } else {
                upper = this.stops[stopCount - 1];
                green = upper[2];
                blue = upper[3];
                red = upper[1];
            }
            red >>= 0x4;
            blue >>= 0x4;
            green >>= 0x4;
            if (blue < 0) {
                blue = 0;
            } else if (blue > 255) {
                blue = 255;
            }
            if (red < 0) {
                red = 0;
            } else if (red > 255) {
                red = 255;
            }
            if (green < 0) {
                green = 0;
            } else if (green > 255) {
                green = 255;
            }
            this.ramp[index] = blue | red << 16 | green << 8;
        }
    }

    @OriginalMember(owner = "client!kg", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        if (this.stops == null) {
            this.setPreset(1);
        }
        this.buildRamp();
    }

    @OriginalMember(owner = "client!kg", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            @Pc(14) int preset = arg1.g1();
            if (preset == 0) {
                this.stops = new int[arg1.g1()][4];
                for (@Pc(28) int stopIndex = 0; stopIndex < this.stops.length; stopIndex++) {
                    this.stops[stopIndex][0] = arg1.g2();
                    this.stops[stopIndex][1] = arg1.g1() << 4;
                    this.stops[stopIndex][2] = arg1.g1() << 4;
                    this.stops[stopIndex][3] = arg1.g1() << 4;
                }
            } else {
                this.setPreset(preset);
            }
        }
        if (arg0) {
            this.stops = null;
        }
    }
}

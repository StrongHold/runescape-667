import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Treats a source layer as a height field and shades it, taking the slope at each texel as a
 * surface normal and lighting it from a direction given by an azimuth and an elevation.
 */
@OriginalClass("client!vda")
public final class TextureOpEmboss extends TextureOp {

    @OriginalMember(owner = "client!vda", name = "F", descriptor = "I")
    public int azimuth = 3216;

    @OriginalMember(owner = "client!vda", name = "G", descriptor = "I")
    public int elevation = 3216;

    @OriginalMember(owner = "client!vda", name = "P", descriptor = "I")
    public int depth = 4096;

    @OriginalMember(owner = "client!vda", name = "S", descriptor = "[I")
    public final int[] lightDirection = new int[3];

    @OriginalMember(owner = "client!vda", name = "<init>", descriptor = "()V")
    public TextureOpEmboss() {
        super(1, true);
    }

    @OriginalMember(owner = "client!vda", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            this.method9416(false, null, 34);
        }
        if (arg2 == 0) {
            this.depth = arg1.g2();
        } else if (arg2 == 1) {
            this.azimuth = arg1.g2();
        } else if (arg2 == 2) {
            this.elevation = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!vda", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(22) int scale = EnvironmentLight.anInt10157 * this.depth >> 12;
            @Pc(32) int[] above = this.method9422(EnvironmentLight.anInt7343 & y - 1, 0);
            @Pc(38) int[] row = this.method9422(y, 0);
            @Pc(48) int[] below = this.method9422(y + 1 & EnvironmentLight.anInt7343, 0);
            for (@Pc(50) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(65) int gradientY = scale * (below[x] - above[x]) >> 12;
                @Pc(86) int gradientX = (row[EnvironmentLight.anInt8580 & x - 1] - row[EnvironmentLight.anInt8580 & x + 1]) * scale >> 12;
                @Pc(90) int magnitudeX = gradientX >> 4;
                if (magnitudeX < 0) {
                    magnitudeX = -magnitudeX;
                }
                @Pc(99) int magnitudeY = gradientY >> 4;
                if (magnitudeX > 255) {
                    magnitudeX = 255;
                }
                if (magnitudeY < 0) {
                    magnitudeY = -magnitudeY;
                }
                if (magnitudeY > 255) {
                    magnitudeY = 255;
                }
                @Pc(133) int normaliser = Static204.aByteArray103[magnitudeX + ((magnitudeY + 1) * magnitudeY >> 1)] & 0xFF;
                @Pc(139) int normalX = gradientX * normaliser >> 8;
                @Pc(145) int normalY = gradientY * normaliser >> 8;
                @Pc(151) int normalZ = normaliser * 4096 >> 8;
                @Pc(160) int dotX = this.lightDirection[0] * normalX >> 12;
                @Pc(169) int dotY = this.lightDirection[1] * normalY >> 12;
                @Pc(178) int dotZ = normalZ * this.lightDirection[2] >> 12;
                output[x] = dotZ + dotY + dotX;
            }
        }
        if (arg0 < 107) {
            this.updateLightDirection((byte) 87);
        }
        return output;
    }

    @OriginalMember(owner = "client!vda", name = "c", descriptor = "(B)V")
    public void updateLightDirection(@OriginalArg(0) byte arg0) {
        @Pc(11) double cosElevation = Math.cos((float) this.elevation / 4096.0F);
        this.lightDirection[0] = (int) (cosElevation * Math.sin((float) this.azimuth / 4096.0F) * 4096.0D);
        this.lightDirection[1] = (int) (4096.0D * (cosElevation * Math.cos((float) this.azimuth / 4096.0F)));
        this.lightDirection[2] = (int) (Math.sin((float) this.elevation / 4096.0F) * 4096.0D);
        @Pc(76) int squareX = this.lightDirection[0] * this.lightDirection[0] >> 12;
        @Pc(88) int squareY = this.lightDirection[1] * this.lightDirection[1] >> 12;
        @Pc(100) int squareZ = this.lightDirection[2] * this.lightDirection[2] >> 12;
        @Pc(113) int length = (int) (Math.sqrt(squareX + squareY + squareZ >> 12) * 4096.0D);
        if (length != 0) {
            this.lightDirection[0] = (this.lightDirection[0] << 12) / length;
            this.lightDirection[1] = (this.lightDirection[1] << 12) / length;
            this.lightDirection[2] = (this.lightDirection[2] << 12) / length;
        }
    }

    @OriginalMember(owner = "client!vda", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        this.updateLightDirection((byte) -119);
    }
}

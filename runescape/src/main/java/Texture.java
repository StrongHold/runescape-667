import com.jagex.core.datastruct.key.Node2;
import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.texture.TextureOp;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * A procedural texture, held as a graph of {@link TextureOp} nodes with one root per channel.
 * <p>
 * {@link #colourOp} produces the red, green and blue rows, {@link #alphaOp} the opacity, and
 * {@link #hdrOp} a per pixel multiplier between 1 and 32 that only the floating point output
 * applies.
 */
@OriginalClass("client!vm")
public final class Texture extends Node2 {

    @OriginalMember(owner = "client!vm", name = "E", descriptor = "[I")
    public final int[] spriteIds;

    @OriginalMember(owner = "client!vm", name = "A", descriptor = "[I")
    public final int[] textureIds;

    @OriginalMember(owner = "client!vm", name = "t", descriptor = "Lclient!pf;")
    public final TextureOp hdrOp;

    @OriginalMember(owner = "client!vm", name = "D", descriptor = "Lclient!pf;")
    public final TextureOp colourOp;

    @OriginalMember(owner = "client!vm", name = "v", descriptor = "Lclient!pf;")
    public final TextureOp alphaOp;

    @OriginalMember(owner = "client!vm", name = "F", descriptor = "[Lclient!pf;")
    public final TextureOp[] ops;

    @OriginalMember(owner = "client!vm", name = "<init>", descriptor = "()V")
    public Texture() {
        this.spriteIds = new int[0];
        this.textureIds = new int[0];
        this.hdrOp = new TextureOpConstant(0);
        this.hdrOp.cacheSize = 1;
        this.colourOp = new TextureOpConstant();
        this.colourOp.cacheSize = 1;
        this.alphaOp = new TextureOpConstant();
        this.alphaOp.cacheSize = 1;
        this.ops = new TextureOp[]{this.colourOp, this.alphaOp, this.hdrOp};
    }

    @OriginalMember(owner = "client!vm", name = "<init>", descriptor = "(Lclient!ge;)V")
    public Texture(@OriginalArg(0) Packet packet) {
        @Pc(7) int count = packet.g1();
        @Pc(9) int spriteCount = 0;
        @Pc(11) int textureCount = 0;
        @Pc(14) int[][] childIndices = new int[count][];
        this.ops = new TextureOp[count];
        for (@Pc(20) int i = 0; i < count; i++) {
            @Pc(28) TextureOp op = Static294.readTextureOp(packet);
            if (op.getSpriteId() >= 0) {
                spriteCount++;
            }
            if (op.getTextureId() >= 0) {
                textureCount++;
            }
            @Pc(49) int childCount = op.ops.length;
            childIndices[i] = new int[childCount];
            for (@Pc(56) int child = 0; child < childCount; child++) {
                childIndices[i][child] = packet.g1();
            }
            this.ops[i] = op;
        }
        this.spriteIds = new int[spriteCount];
        this.textureIds = new int[textureCount];
        spriteCount = 0;
        textureCount = 0;
        for (@Pc(105) int i = 0; i < count; i++) {
            @Pc(114) TextureOp op = this.ops[i];
            for (@Pc(120) int child = 0; child < op.ops.length; child++) {
                op.ops[child] = this.ops[childIndices[i][child]];
            }
            @Pc(148) int spriteId = op.getSpriteId();
            @Pc(152) int textureId = op.getTextureId();
            if (spriteId > 0) {
                this.spriteIds[spriteCount++] = spriteId;
            }
            if (textureId > 0) {
                this.textureIds[textureCount++] = textureId;
            }
            childIndices[i] = null;
        }
        this.colourOp = this.ops[packet.g1()];
        this.alphaOp = this.ops[packet.g1()];
        this.hdrOp = this.ops[packet.g1()];
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(BILclient!d;Lclient!sb;ZI)[F")
    public float[] method8946(@OriginalArg(1) int height, @OriginalArg(2) TextureSource source, @OriginalArg(3) js5 sprites, @OriginalArg(4) boolean transpose, @OriginalArg(5) int width) {
        Static582.aJs5_108 = sprites;
        Static677.anTextureSource_11 = source;
        for (@Pc(25) int i = 0; i < this.ops.length; i++) {
            this.ops[i].initCache(height, width);
        }
        EnvironmentLight.method2313(height, width);
        @Pc(54) float[] pixels = new float[width * 4 * height];
        @Pc(56) int index = 0;
        for (@Pc(58) int y = 0; y < height; y++) {
            @Pc(78) int[] redRow;
            @Pc(80) int[] greenRow;
            @Pc(76) int[] blueRow;
            if (this.colourOp.monochrome) {
                @Pc(74) int[] greyRow = this.colourOp.monochromeOutput(117, y);
                blueRow = greyRow;
                redRow = greyRow;
                greenRow = greyRow;
            } else {
                @Pc(88) int[][] rows = this.colourOp.method9414(y);
                redRow = rows[0];
                blueRow = rows[2];
                greenRow = rows[1];
            }
            @Pc(110) int[] alphaRow;
            if (this.alphaOp.monochrome) {
                alphaRow = this.alphaOp.monochromeOutput(114, y);
            } else {
                alphaRow = this.alphaOp.method9414(y)[0];
            }
            if (transpose) {
                index = y << 2;
            }
            @Pc(136) int[] hdrRow;
            if (this.hdrOp.monochrome) {
                hdrRow = this.hdrOp.monochromeOutput(115, y);
            } else {
                hdrRow = this.hdrOp.method9414(y)[0];
            }
            for (@Pc(150) int x = width - 1; x >= 0; x--) {
                @Pc(159) float alpha = (float) alphaRow[x] / 4096.0F;
                if (alpha < 0.0F) {
                    alpha = 0.0F;
                } else if (alpha > 1.0F) {
                    alpha = 1.0F;
                }
                @Pc(188) float scale = ((float) hdrRow[x] * 31.0F / 4096.0F + 1.0F) / 4096.0F;
                pixels[index++] = scale * (float) redRow[x];
                pixels[index++] = scale * (float) greenRow[x];
                pixels[index++] = scale * (float) blueRow[x];
                pixels[index++] = alpha;
                if (transpose) {
                    index += (width << 2) - 4;
                }
            }
        }
        for (@Pc(244) int i = 0; i < this.ops.length; i++) {
            this.ops[i].cacheReset();
        }
        return pixels;
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(Lclient!sb;Lclient!d;B)Z")
    public boolean available(@OriginalArg(0) js5 sprites, @OriginalArg(1) TextureSource source) {
        @Pc(12) int i;
        if (Static426.anInt940 < 0) {
            for (i = 0; i < this.spriteIds.length; i++) {
                if (!sprites.fileready(this.spriteIds[i])) {
                    return false;
                }
            }
        } else {
            for (i = 0; i < this.spriteIds.length; i++) {
                if (!sprites.requestdownload(this.spriteIds[i], Static426.anInt940)) {
                    return false;
                }
            }
        }
        if (4 != 4) {
            return true;
        }
        for (i = 0; i < this.textureIds.length; i++) {
            if (!source.textureAvailable(this.textureIds[i])) {
                return false;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(IDZIBLclient!d;Lclient!sb;)[I")
    public int[] method8948(@OriginalArg(0) int width, @OriginalArg(1) double gamma, @OriginalArg(2) boolean transpose, @OriginalArg(3) int height, @OriginalArg(5) TextureSource source, @OriginalArg(6) js5 sprites) {
        Static582.aJs5_108 = sprites;
        Static677.anTextureSource_11 = source;
        for (@Pc(11) int i = 0; i < this.ops.length; i++) {
            this.ops[i].initCache(height, width);
        }
        Static725.setGamma(gamma);
        EnvironmentLight.method2313(height, width);
        @Pc(53) int[] pixels = new int[width * height];
        @Pc(55) int index = 0;
        for (@Pc(57) int y = 0; y < height; y++) {
            @Pc(77) int[] redRow;
            @Pc(85) int[] greenRow;
            @Pc(81) int[] blueRow;
            @Pc(93) int[] alphaRow;
            if (this.colourOp.monochrome) {
                redRow = greenRow = blueRow = this.colourOp.monochromeOutput(117, y);
            } else {
                @Pc(73) int[][] rows = this.colourOp.method9414(y);
                redRow = rows[0];
                blueRow = rows[2];
                greenRow = rows[1];
            }
            if (this.alphaOp.monochrome) {
                alphaRow = this.alphaOp.monochromeOutput(111, y);
            } else {
                alphaRow = this.alphaOp.method9414(y)[0];
            }
            if (transpose) {
                index = y;
            }
            for (@Pc(127) int x = width - 1; x >= 0; x--) {
                @Pc(135) int red = redRow[x] >> 4;
                if (red > 255) {
                    red = 255;
                }
                if (red < 0) {
                    red = 0;
                }
                @Pc(150) int green = greenRow[x] >> 4;
                if (green > 255) {
                    green = 255;
                }
                if (green < 0) {
                    green = 0;
                }
                @Pc(165) int blue = blueRow[x] >> 4;
                if (blue > 255) {
                    blue = 255;
                }
                green = Static609.anIntArray716[green];
                if (blue < 0) {
                    blue = 0;
                }
                red = Static609.anIntArray716[red];
                blue = Static609.anIntArray716[blue];
                @Pc(209) int alpha;
                if (red == 0 && green == 0 && blue == 0) {
                    alpha = 0;
                } else {
                    alpha = alphaRow[x] >> 4;
                    if (alpha > 255) {
                        alpha = 255;
                    }
                    if (alpha < 0) {
                        alpha = 0;
                    }
                }
                pixels[index++] = (green << 8) + (alpha << 24) + (red << 16) + blue;
                if (transpose) {
                    index += width - 1;
                }
            }
        }
        for (@Pc(268) int i = 0; i < this.ops.length; i++) {
            this.ops[i].cacheReset();
        }
        return pixels;
    }

    @OriginalMember(owner = "client!vm", name = "a", descriptor = "(Lclient!sb;DZLclient!d;ZIII)[I")
    public int[] method8951(@OriginalArg(0) js5 sprites, @OriginalArg(1) double gamma, @OriginalArg(2) boolean transpose, @OriginalArg(3) TextureSource source, @OriginalArg(4) boolean reverse, @OriginalArg(5) int height, @OriginalArg(6) int width) {
        Static677.anTextureSource_11 = source;
        Static582.aJs5_108 = sprites;
        for (@Pc(11) int i = 0; i < this.ops.length; i++) {
            this.ops[i].initCache(height, width);
        }
        Static725.setGamma(gamma);
        EnvironmentLight.method2313(height, width);
        @Pc(41) int[] pixels = new int[height * width];
        @Pc(49) int start;
        @Pc(47) int end;
        @Pc(51) byte step;
        if (reverse) {
            end = -1;
            start = width - 1;
            step = -1;
        } else {
            end = width;
            start = 0;
            step = 1;
        }
        @Pc(63) int index = 0;
        for (@Pc(65) int y = 0; y < height; y++) {
            @Pc(85) int[] redRow;
            @Pc(81) int[] greenRow;
            @Pc(83) int[] blueRow;
            if (this.colourOp.monochrome) {
                @Pc(79) int[] greyRow = this.colourOp.monochromeOutput(127, y);
                greenRow = greyRow;
                blueRow = greyRow;
                redRow = greyRow;
            } else {
                @Pc(93) int[][] rows = this.colourOp.method9414(y);
                blueRow = rows[2];
                greenRow = rows[1];
                redRow = rows[0];
            }
            if (transpose) {
                index = y;
            }
            for (@Pc(111) int x = start; x != end; x += step) {
                @Pc(119) int red = redRow[x] >> 4;
                if (red > 255) {
                    red = 255;
                }
                if (red < 0) {
                    red = 0;
                }
                @Pc(137) int green = greenRow[x] >> 4;
                if (green > 255) {
                    green = 255;
                }
                if (green < 0) {
                    green = 0;
                }
                @Pc(154) int blue = blueRow[x] >> 4;
                if (blue > 255) {
                    blue = 255;
                }
                green = Static609.anIntArray716[green];
                red = Static609.anIntArray716[red];
                if (blue < 0) {
                    blue = 0;
                }
                blue = Static609.anIntArray716[blue];
                @Pc(189) int rgb = (red << 16) + (green << 8) + blue;
                if (rgb != 0) {
                    rgb |= 0xFF000000;
                }
                pixels[index++] = rgb;
                if (transpose) {
                    index += width - 1;
                }
            }
        }
        for (@Pc(230) int i = 0; i < this.ops.length; i++) {
            this.ops[i].cacheReset();
        }
        return pixels;
    }
}

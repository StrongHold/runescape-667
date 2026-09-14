import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Rasterises a list of vector primitives into a texture layer.
 */
@OriginalClass("client!ot")
public final class TextureOpShape extends TextureOp {

    @OriginalMember(owner = "client!ot", name = "M", descriptor = "[Lclient!ifa;")
    public TextureShape[] shapes;

    @OriginalMember(owner = "client!ot", name = "<init>", descriptor = "()V")
    public TextureOpShape() {
        super(0, true);
    }

    @OriginalMember(owner = "client!ot", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        if (arg0 < 107) {
            Sprites.mapdots = null;
        }
        @Pc(16) int[] output = super.monochromeCache.get(arg1);
        if (super.monochromeCache.dirty) {
            this.render(super.monochromeCache.get());
        }
        return output;
    }

    @OriginalMember(owner = "client!ot", name = "a", descriptor = "(I[[I)V")
    public void render(@OriginalArg(1) int[][] dest) {
        @Pc(7) int width = EnvironmentLight.anInt9289;
        @Pc(9) int height = EnvironmentLight.anInt53;
        Static430.method5815(dest);
        Static96.setClipBounds(EnvironmentLight.anInt7343, EnvironmentLight.anInt8580);
        if (this.shapes == null) {
            return;
        }
        for (@Pc(23) int index = 0; index < this.shapes.length; index++) {
            @Pc(30) TextureShape shape = this.shapes[index];
            @Pc(33) int fillColour = shape.fillColour;
            @Pc(36) int lineColour = shape.lineColour;
            if (fillColour >= 0) {
                if (lineColour < 0) {
                    shape.fill(width, height);
                } else {
                    shape.fillAndOutline(height, width);
                }
            } else if (lineColour >= 0) {
                shape.outline(height, width);
            }
        }
    }

    @OriginalMember(owner = "client!ot", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int arg0) {
        @Pc(17) int[][] output = super.colourCache.get(arg0);
        if (super.colourCache.dirty) {
            @Pc(23) int width = EnvironmentLight.anInt9289;
            @Pc(25) int height = EnvironmentLight.anInt53;
            @Pc(29) int[][] raster = new int[height][width];
            @Pc(34) int[][][] rows = super.colourCache.get();
            this.render(raster);
            for (@Pc(40) int y = 0; y < EnvironmentLight.anInt53; y++) {
                @Pc(46) int[] rasterRow = raster[y];
                @Pc(50) int[][] row = rows[y];
                @Pc(54) int[] red = row[0];
                @Pc(58) int[] green = row[1];
                @Pc(62) int[] blue = row[2];
                for (@Pc(64) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                    @Pc(70) int rgb = rasterRow[x];
                    blue[x] = (rgb & 0xFF) << 4;
                    green[x] = rgb >> 4 & 0xFF0;
                    red[x] = rgb >> 12 & 0xFF0;
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!ot", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet packet, @OriginalArg(2) int opcode) {
        if (opcode == 0) {
            this.shapes = new TextureShape[packet.g1()];
            for (@Pc(33) int index = 0; index < this.shapes.length; index++) {
                @Pc(39) int type = packet.g1();
                if (type == 0) {
                    this.shapes[index] = Static305.readLine(packet);
                } else if (type == 1) {
                    this.shapes[index] = Static396.readCurve(packet);
                } else if (type == 2) {
                    this.shapes[index] = Static150.readRectangle(packet);
                } else if (type == 3) {
                    this.shapes[index] = Static75.readEllipse(packet);
                }
            }
        } else if (opcode == 1) {
            super.monochrome = packet.g1() == 1;
        }
        if (arg0) {
            this.method9416(false, null, -67);
        }
    }
}

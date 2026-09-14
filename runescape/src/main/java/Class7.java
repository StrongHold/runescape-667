import com.jagex.core.io.ByteArrayWrapper;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ae")
public final class Class7 {

    private static final int TEXTURE_WIDTH = 128;

    private static final int TEXTURE_HEIGHT = 128;

    private static final int FRAME_COUNT = 16;

    private static final int BYTES_PER_TEXEL = 2;

    private static final int FRAME_SIZE = TEXTURE_WIDTH * TEXTURE_HEIGHT * BYTES_PER_TEXEL;

    private static final int HEIGHT_MASK = TEXTURE_HEIGHT - 1;

    private static final int WIDTH_MASK = TEXTURE_WIDTH - 1;

    /**
     * The height field is sampled as if one unit of height spanned 128 texels, so the cross product that turns the
     * two central differences into a normal has a fixed up component of 128 and a length of at least 128 squared.
     */
    private static final float HEIGHT_SCALE = 128.0F;

    @OriginalMember(owner = "client!ae", name = "g", descriptor = "Lclient!bga;")
    public Interface2 anInterface2_1 = null;

    @OriginalMember(owner = "client!ae", name = "c", descriptor = "[Lclient!og;")
    public Interface18[] anInterface18Array2 = null;

    @OriginalMember(owner = "client!ae", name = "h", descriptor = "Lclient!bga;")
    public Interface2 anInterface2_2 = null;

    @OriginalMember(owner = "client!ae", name = "a", descriptor = "[Lclient!og;")
    public Interface18[] flowTextures = null;

    @OriginalMember(owner = "client!ae", name = "f", descriptor = "Lclient!am;")
    public final NativeToolkit toolkit;

    @OriginalMember(owner = "client!ae", name = "d", descriptor = "Z")
    public boolean aBoolean7;

    @OriginalMember(owner = "client!ae", name = "<init>", descriptor = "(Lclient!am;)V")
    public Class7(@OriginalArg(0) NativeToolkit toolkit) {
        this.toolkit = toolkit;
        this.aBoolean7 = this.toolkit.aBoolean696;
        if (this.aBoolean7 && !this.toolkit.method8153(Static702.aClass397_16, Static482.aClass92_13)) {
            this.aBoolean7 = false;
        }
        if (this.aBoolean7 || this.toolkit.method8071(Static482.aClass92_13, Static702.aClass397_16)) {
            NativeWaterNoise.ensureGenerated();
            if (this.aBoolean7) {
                @Pc(60) byte[] rippleTexels = ByteArrayWrapper.unwrap(false, Static177.anObject6);
                this.anInterface2_2 = this.toolkit.method8038(Static482.aClass92_13, rippleTexels);
                @Pc(76) byte[] flowTexels = ByteArrayWrapper.unwrap(false, Static644.anObject18);
                this.toolkit.method8038(Static482.aClass92_13, flowTexels);
            } else {
                this.anInterface18Array2 = new Interface18[FRAME_COUNT];
                for (@Pc(93) int frame = 0; frame < FRAME_COUNT; frame++) {
                    @Pc(104) byte[] rippleTexels = ByteArrayWrapper.unwrap(FRAME_SIZE, Static177.anObject6, frame * FRAME_SIZE);
                    this.anInterface18Array2[frame] = this.toolkit.method8028(TEXTURE_WIDTH, rippleTexels, Static482.aClass92_13, TEXTURE_HEIGHT, true);
                }
                this.flowTextures = new Interface18[FRAME_COUNT];
                for (@Pc(129) int frame = 0; frame < FRAME_COUNT; frame++) {
                    @Pc(140) byte[] flowTexels = ByteArrayWrapper.unwrap(FRAME_SIZE, Static644.anObject18, frame * FRAME_SIZE);
                    this.flowTextures[frame] = this.toolkit.method8028(TEXTURE_WIDTH, flowTexels, Static482.aClass92_13, TEXTURE_HEIGHT, true);
                }
            }
        }
    }

    @OriginalMember(owner = "client!ae", name = "a", descriptor = "(I)Z")
    public boolean method115() {
        if (this.anInterface2_1 == null) {
            @Pc(26) byte[] heights;
            if (Static186.anObject7 == null) {
                heights = Static448.generateNoiseVolume(4.0F, 4.0F, 0.5F, 16.0F, 0.6F, new PerlinNoiseGenerator(419684));
                Static186.anObject7 = ByteArrayWrapper.wrap(heights);
            }
            heights = ByteArrayWrapper.unwrap(false, Static186.anObject7);
            @Pc(42) byte[] normals = new byte[heights.length * 4];
            @Pc(44) int normalIndex = 0;
            for (@Pc(46) int frame = 0; frame < FRAME_COUNT; frame++) {
                @Pc(54) int heightIndex = frame * TEXTURE_WIDTH * TEXTURE_HEIGHT;
                @Pc(56) int frameStart = heightIndex;
                for (@Pc(58) int y = 0; y < TEXTURE_HEIGHT; y++) {
                    @Pc(67) int rowStart = frameStart + y * TEXTURE_WIDTH;
                    @Pc(78) int aboveStart = frameStart + (y - 1 & HEIGHT_MASK) * TEXTURE_WIDTH;
                    @Pc(88) int belowStart = (y + 1 & HEIGHT_MASK) * TEXTURE_WIDTH + frameStart;
                    for (@Pc(90) int x = 0; x < TEXTURE_WIDTH; x++) {
                        @Pc(111) float slopeY = (float) ((heights[aboveStart + x] & 0xFF) - (heights[x + belowStart] & 0xFF));
                        @Pc(138) float slopeX = (float) ((heights[rowStart + (x - 1 & WIDTH_MASK)] & 0xFF) - (heights[rowStart + (x + 1 & WIDTH_MASK)] & 0xFF));
                        @Pc(153) float scale = (float) (HEIGHT_SCALE / Math.sqrt(slopeY * slopeY + slopeX * slopeX + HEIGHT_SCALE * HEIGHT_SCALE));
                        normals[normalIndex++] = (byte) (int) (slopeX * scale + 127.0F);
                        normals[normalIndex++] = (byte) (int) (scale * HEIGHT_SCALE + 127.0F);
                        normals[normalIndex++] = (byte) (int) (slopeY * scale + 127.0F);
                        normals[normalIndex++] = heights[heightIndex++];
                    }
                }
            }
            this.anInterface2_1 = this.toolkit.method8038(Static172.aClass92_8, normals);
        }
        return this.anInterface2_1 != null;
    }

    @OriginalMember(owner = "client!ae", name = "b", descriptor = "(I)Z")
    public boolean method116() {
        if (this.aBoolean7) {
            return this.anInterface2_2 != null;
        } else {
            return this.anInterface18Array2 != null;
        }
    }
}

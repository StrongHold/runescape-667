import com.jagex.core.io.ByteArrayWrapper;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!sa")
public final class Class329 {

    private static final int TEXTURE_WIDTH = 128;

    private static final int TEXTURE_HEIGHT = 128;

    private static final int FRAME_COUNT = 16;

    private static final int BYTES_PER_TEXEL = 2;

    private static final int FRAME_SIZE = TEXTURE_WIDTH * TEXTURE_HEIGHT * BYTES_PER_TEXEL;

    @OriginalMember(owner = "client!sa", name = "f", descriptor = "[Lclient!rq;")
    public Class93_Sub2[] aClass93_Sub2Array3 = null;

    @OriginalMember(owner = "client!sa", name = "b", descriptor = "Lclient!vv;")
    public Class93_Sub3 aClass93_Sub3_3 = null;

    @OriginalMember(owner = "client!sa", name = "c", descriptor = "Lclient!vv;")
    public Class93_Sub3 aClass93_Sub3_2 = null;

    @OriginalMember(owner = "client!sa", name = "e", descriptor = "[Lclient!rq;")
    public Class93_Sub2[] aClass93_Sub2Array4 = null;

    @OriginalMember(owner = "client!sa", name = "g", descriptor = "Lclient!vv;")
    public Class93_Sub3 aClass93_Sub3_1 = null;

    @OriginalMember(owner = "client!sa", name = "a", descriptor = "Z")
    public final boolean aBoolean655;

    @OriginalMember(owner = "client!sa", name = "<init>", descriptor = "(Lclient!qha;)V")
    public Class329(@OriginalArg(0) GlToolkit toolkit) {
        this.aBoolean655 = toolkit.aBoolean606;
        GlWaterNoise.ensureGenerated(toolkit);
        if (this.aBoolean655) {
            @Pc(31) byte[] rippleTexels = ByteArrayWrapper.unwrap(false, Static599.anObject14);
            this.aClass93_Sub3_2 = new Class93_Sub3(toolkit, 6410, TEXTURE_WIDTH, TEXTURE_HEIGHT, FRAME_COUNT, rippleTexels, 6410);
            @Pc(48) byte[] flowTexels = ByteArrayWrapper.unwrap(false, Static158.anObject5);
            this.aClass93_Sub3_1 = new Class93_Sub3(toolkit, 6410, TEXTURE_WIDTH, TEXTURE_HEIGHT, FRAME_COUNT, flowTexels, 6410);
            @Pc(63) Class202 normalMapper = toolkit.aClass202_1;
            if (normalMapper.method4582()) {
                rippleTexels = ByteArrayWrapper.unwrap(false, Static71.anObject4);
                this.aClass93_Sub3_3 = new Class93_Sub3(toolkit, 6408, TEXTURE_WIDTH, TEXTURE_HEIGHT, FRAME_COUNT);
                @Pc(93) Class93_Sub3 heightMap = new Class93_Sub3(toolkit, 6409, TEXTURE_WIDTH, TEXTURE_HEIGHT, FRAME_COUNT, rippleTexels, 6409);
                if (normalMapper.method4580(2.0F, this.aClass93_Sub3_3, heightMap)) {
                    this.aClass93_Sub3_3.method9446();
                } else {
                    this.aClass93_Sub3_3.method9442();
                    this.aClass93_Sub3_3 = null;
                }
                heightMap.method9442();
                return;
            }
        } else {
            this.aClass93_Sub2Array3 = new Class93_Sub2[FRAME_COUNT];
            for (@Pc(125) int frame = 0; frame < FRAME_COUNT; frame++) {
                @Pc(138) byte[] rippleTexels = ByteArrayWrapper.unwrap(FRAME_SIZE, Static599.anObject14, frame * FRAME_SIZE);
                this.aClass93_Sub2Array3[frame] = new Class93_Sub2(toolkit, 3553, 6410, TEXTURE_WIDTH, TEXTURE_HEIGHT, true, rippleTexels, 6410, false);
            }
            this.aClass93_Sub2Array4 = new Class93_Sub2[FRAME_COUNT];
            for (@Pc(164) int frame = 0; frame < FRAME_COUNT; frame++) {
                @Pc(175) byte[] flowTexels = ByteArrayWrapper.unwrap(FRAME_SIZE, Static158.anObject5, frame * FRAME_SIZE);
                this.aClass93_Sub2Array4[frame] = new Class93_Sub2(toolkit, 3553, 6410, TEXTURE_WIDTH, TEXTURE_HEIGHT, true, flowTexels, 6410, false);
            }
        }
    }
}

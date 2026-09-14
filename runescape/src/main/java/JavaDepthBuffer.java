import com.jagex.DepthBuffer;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!hia")
public final class JavaDepthBuffer implements DepthBuffer {

    @OriginalMember(owner = "client!hia", name = "e", descriptor = "I")
    public final int width;

    @OriginalMember(owner = "client!hia", name = "b", descriptor = "[F")
    public final float[] depths;

    @OriginalMember(owner = "client!hia", name = "c", descriptor = "I")
    public final int height;

    @OriginalMember(owner = "client!hia", name = "<init>", descriptor = "(II)V")
    public JavaDepthBuffer(@OriginalArg(0) int width, @OriginalArg(1) int height) {
        this.width = width;
        this.depths = new float[width * height];
        this.height = height;
    }
}

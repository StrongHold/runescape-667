import com.jagex.core.datastruct.Node;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!ru")
public final class GroundRenderTask extends Node {

    /**
     * One-based level, so that the ground of level n is Static246.ground[level - 1].
     */
    @OriginalMember(owner = "client!ru", name = "g", descriptor = "I")
    public final int level;

    @OriginalMember(owner = "client!ru", name = "<init>", descriptor = "(I)V")
    public GroundRenderTask(@OriginalArg(0) int level) {
        this.level = level;
    }
}

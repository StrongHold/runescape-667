import com.jagex.graphics.Ground;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static429 {

    @OriginalMember(owner = "client!nj", name = "a", descriptor = "(ILclient!s;)V")
    public static void setGround(@OriginalArg(0) int level, @OriginalArg(1) Ground ground) {
        Static246.ground[level] = ground;
    }
}

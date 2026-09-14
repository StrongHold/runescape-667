import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static10 {

    @OriginalMember(owner = "client!afa", name = "a", descriptor = "(IIILjava/lang/Class;)V")
    public static void method130(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) Class arg3) {
        @Pc(7) Tile local7 = Static334.activeTiles[arg0][arg1][arg2];
        if (local7 == null) {
            return;
        }
        for (@Pc(14) PositionEntityNode local14 = local7.head; local14 != null; local14 = local14.node) {
            @Pc(18) PositionEntity local18 = local14.entity;
            if (arg3.isAssignableFrom(local18.getClass()) && local18.x1 == arg1 && local18.z1 == arg2) {
                Static549.removePositionEntity(local18, false);
                return;
            }
        }
    }
}

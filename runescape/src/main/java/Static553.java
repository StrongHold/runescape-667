import com.jagex.ChangeLocationRequest;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static553 {

    @OriginalMember(owner = "client!rga", name = "a", descriptor = "(IIIIIIIZ)V")
    public static void changeLocation(@OriginalArg(3) int x, @OriginalArg(6) int z, @OriginalArg(4) int level, @OriginalArg(5) int shape, @OriginalArg(0) int rotation, @OriginalArg(2) int layer, @OriginalArg(1) int id) {
        @Pc(7) ChangeLocationRequest local7 = null;
        for (@Pc(12) ChangeLocationRequest local12 = (ChangeLocationRequest) Static159.changes.first(); local12 != null; local12 = (ChangeLocationRequest) Static159.changes.next()) {
            if (level == local12.level && local12.x == x && z == local12.z && local12.layer == layer) {
                local7 = local12;
                break;
            }
        }
        if (local7 == null) {
            local7 = new ChangeLocationRequest();
            local7.layer = layer;
            local7.level = level;
            local7.z = z;
            local7.x = x;
            if (x >= 0 && z >= 0 && Static720.mapWidth > x && z < Static501.mapLength) {
                Static293.snapshotOriginal(local7);
            }
            Static159.changes.addLast(local7);
        }
        local7.pendingApply = true;
        local7.rotation = rotation;
        local7.shape = shape;
        local7.pendingRemoval = false;
        local7.id = id;
    }

}

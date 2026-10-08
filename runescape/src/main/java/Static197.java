import com.jagex.ChangeLocationRequest;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static197 {

    @OriginalMember(owner = "client!gba", name = "a", descriptor = "(Z)V")
    public static void requeueLocChanges() {
        @Pc(8) ChangeLocationRequest local8;
        for (local8 = (ChangeLocationRequest) Static159.changes.first(); local8 != null; local8 = (ChangeLocationRequest) Static159.changes.next()) {
            if (local8.pendingRemoval) {
                local8.unlink();
            } else {
                local8.pendingApply = true;
                if (local8.x >= 0 && local8.z >= 0 && Static720.mapWidth > local8.x && Static501.mapLength > local8.z) {
                    Static293.snapshotOriginal(local8);
                }
            }
        }
        for (local8 = (ChangeLocationRequest) Static227.customisations.first(); local8 != null; local8 = (ChangeLocationRequest) Static227.customisations.next()) {
            if (local8.pendingRemoval) {
                local8.unlink();
            } else {
                local8.pendingApply = true;
            }
        }
    }
}

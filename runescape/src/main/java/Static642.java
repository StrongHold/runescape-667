import com.jagex.PickableEntity;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.graphics.PickingCylinder;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static642 {

    @OriginalMember(owner = "client!uea", name = "a", descriptor = "(ZBI)Lclient!pea;")
    public static PickableEntity allocatePickableEntity(@OriginalArg(0) boolean interactive, @OriginalArg(2) int cylinderCount) {
        @Pc(7) LinkedList[] lock = PickableEntityPool.FREE_LISTS;
        synchronized (PickableEntityPool.FREE_LISTS) {
            @Pc(37) PickableEntity entity;
            if (PickableEntityPool.FREE_LISTS.length <= cylinderCount || PickableEntityPool.FREE_LISTS[cylinderCount].isEmpty()) {
                entity = new PickableEntity();
                entity.pickingCylinders = new PickingCylinder[cylinderCount];
                for (@Pc(43) int index = 0; index < cylinderCount; index++) {
                    entity.pickingCylinders[index] = new PickingCylinder();
                }
            } else {
                entity = (PickableEntity) PickableEntityPool.FREE_LISTS[cylinderCount].last();
                entity.unlink();
                @Pc(78) int pooled = Static159.pooledCounts[cylinderCount]--;
            }
            entity.interactive = interactive;
            return entity;
        }
    }
}

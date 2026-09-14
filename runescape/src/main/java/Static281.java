import com.jagex.PickableEntity;
import com.jagex.core.datastruct.LinkedList;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static281 {

    @OriginalMember(owner = "client!iq", name = "a", descriptor = "(ILclient!pea;)V")
    public static void recycle(@OriginalArg(1) PickableEntity pickable) {
        pickable.aEntity_18 = null;
        @Pc(10) int cylinderCount = pickable.pickingCylinders.length;
        for (@Pc(12) int index = 0; index < cylinderCount; index++) {
            pickable.pickingCylinders[index].aBoolean352 = false;
        }
        @Pc(25) LinkedList[] lock = PickableEntityPool.FREE_LISTS;
        synchronized (PickableEntityPool.FREE_LISTS) {
            if (cylinderCount < PickableEntityPool.FREE_LISTS.length && Static159.anIntArray245[cylinderCount] < 200) {
                PickableEntityPool.FREE_LISTS[cylinderCount].add(pickable);
                @Pc(48) int pooled = Static159.anIntArray245[cylinderCount]++;
            }
        }
    }
}

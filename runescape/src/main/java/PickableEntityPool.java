import com.jagex.core.datastruct.LinkedList;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Holds the recycled {@link com.jagex.PickableEntity} instances, bucketed by the number of picking
 * cylinders each one carries, so that a request for a given cylinder count can reuse an entity
 * whose cylinder array is already the right length.
 */
@OriginalClass("client!bja")
public final class PickableEntityPool {

    @OriginalMember(owner = "client!bja", name = "b", descriptor = "[Lclient!fla;")
    public static final LinkedList[] FREE_LISTS = new LinkedList[5];

    static {
        for (@Pc(25) int index = 0; index < FREE_LISTS.length; index++) {
            FREE_LISTS[index] = new LinkedList();
        }
    }
}

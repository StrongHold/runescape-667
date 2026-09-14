import com.jagex.Entity;
import com.jagex.PickableEntity;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.core.datastruct.Node;
import com.jagex.graphics.PickingCylinder;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * The entities the cursor can pick this frame, ordered front to back by entity depth so that the
 * mini menu offers the nearest entity first.
 */
@OriginalClass("client!kn")
public final class PickList {

    @OriginalMember(owner = "client!kn", name = "a", descriptor = "Lclient!fla;")
    public final LinkedList entities = new LinkedList();

    /**
     * Whether the list survives a scene draw instead of being emptied by it. Orthographic mode
     * draws the scene once per viewport tile into a single list, so an entity that straddles two
     * tiles reaches this list twice and the earlier entry has to be dropped on the second add.
     */
    @OriginalMember(owner = "client!kn", name = "f", descriptor = "Z")
    public boolean retained = false;

    @OriginalMember(owner = "client!kn", name = "<init>", descriptor = "(Z)V")
    public PickList(@OriginalArg(0) boolean retained) {
        this.retained = retained;
    }

    @OriginalMember(owner = "client!kn", name = "a", descriptor = "(ILclient!pea;)V")
    public void add(@OriginalArg(1) PickableEntity pickable) {
        @Pc(6) Entity entity = pickable.aEntity_18;
        @Pc(8) boolean unpickable = true;
        @Pc(11) PickingCylinder[] cylinders = pickable.pickingCylinders;
        for (@Pc(13) int index = 0; index < cylinders.length; index++) {
            if (cylinders[index].aBoolean352) {
                unpickable = false;
                break;
            }
        }
        if (unpickable) {
            return;
        }
        @Pc(42) PickableEntity existing;
        if (this.retained) {
            for (existing = (PickableEntity) this.entities.first(); existing != null; existing = (PickableEntity) this.entities.next()) {
                if (existing.aEntity_18 == entity) {
                    existing.unlink();
                    Static281.recycle(existing);
                }
            }
        }
        for (existing = (PickableEntity) this.entities.first(); existing != null; existing = (PickableEntity) this.entities.next()) {
            if (entity.anInt10697 >= existing.aEntity_18.anInt10697) {
                Node.addBefore(existing, pickable);
                return;
            }
        }
        this.entities.add(pickable);
    }

    @OriginalMember(owner = "client!kn", name = "a", descriptor = "(B)V")
    public void method5010() {
        while (true) {
            @Pc(5) PickableEntity entity = (PickableEntity) this.entities.removeFirst();
            if (entity == null) {
                return;
            }
            entity.unlink();
            Static281.recycle(entity);
        }
    }
}

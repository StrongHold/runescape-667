import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!ie")
public class Node {

    @OriginalMember(owner = "client!ie", name = "f", descriptor = "J")
    public long key;

    @OriginalMember(owner = "client!ie", name = "c", descriptor = "Lclient!ie;")
    public Node prev;

    @OriginalMember(owner = "client!ie", name = "i", descriptor = "Lclient!ie;")
    public Node next;

    @OriginalMember(owner = "client!ie", name = "a", descriptor = "(B)V")
    public final void remove() {
        if (this.prev != null) {
            this.prev.next = this.next;
            this.next.prev = this.prev;
            this.next = null;
            this.prev = null;
        }
    }

    @OriginalMember(owner = "client!ie", name = "e", descriptor = "(I)Z")
    public final boolean hasPrev() {
        return this.prev != null;
    }
}
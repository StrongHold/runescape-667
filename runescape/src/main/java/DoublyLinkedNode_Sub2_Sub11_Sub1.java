import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

import java.lang.ref.SoftReference;

@OriginalClass("client!fja")
public final class DoublyLinkedNode_Sub2_Sub11_Sub1 extends DoublyLinkedNode_Sub2_Sub11 {

    @OriginalMember(owner = "client!fja", name = "y", descriptor = "Ljava/lang/ref/SoftReference;")
    public final SoftReference aSoftReference1;

    @OriginalMember(owner = "client!fja", name = "<init>", descriptor = "(Lclient!uq;Ljava/lang/Object;I)V")
    public DoublyLinkedNode_Sub2_Sub11_Sub1(@OriginalArg(0) Interface24 arg0, @OriginalArg(1) Object arg1, @OriginalArg(2) int arg2) {
        super(arg0, arg2);
        this.aSoftReference1 = new SoftReference(arg1);
    }

    @OriginalMember(owner = "client!fja", name = "a", descriptor = "(I)Ljava/lang/Object;")
    @Override
    public Object method8311() {
        return this.aSoftReference1.get();
    }

    @OriginalMember(owner = "client!fja", name = "c", descriptor = "(B)Z")
    @Override
    public boolean method8314() {
        return true;
    }
}
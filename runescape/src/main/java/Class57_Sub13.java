import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!jha")
public final class Class57_Sub13 extends Class57 {

    @OriginalMember(owner = "client!jha", name = "<init>", descriptor = "(Lclient!kv;)V")
    public Class57_Sub13(@OriginalArg(0) ClientOptions arg0) {
        super(arg0);
    }

    @OriginalMember(owner = "client!jha", name = "<init>", descriptor = "(ILclient!kv;)V")
    public Class57_Sub13(@OriginalArg(0) int arg0, @OriginalArg(1) ClientOptions arg1) {
        super(arg0, arg1);
    }

    @OriginalMember(owner = "client!jha", name = "a", descriptor = "(ZI)V")
    @Override
    protected void method8353(@OriginalArg(1) int arg0) {
        super.anInt9489 = arg0;
    }

    @OriginalMember(owner = "client!jha", name = "a", descriptor = "(IB)I")
    @Override
    public int method8352(@OriginalArg(0) int arg0) {
        return Static140.method2367(super.aClass2_Sub34_34.toolkit.value()) ? 1 : 3;
    }

    @OriginalMember(owner = "client!jha", name = "a", descriptor = "(B)V")
    @Override
    public void method8350() {
        if (super.aClass2_Sub34_34.toolkit.method7913() && !Static140.method2367(super.aClass2_Sub34_34.toolkit.value())) {
            super.anInt9489 = 0;
        }
        if (super.anInt9489 < 0 || super.anInt9489 > 2) {
            super.anInt9489 = this.method8354();
        }
    }

    @OriginalMember(owner = "client!jha", name = "c", descriptor = "(B)Z")
    public boolean method4372() {
        return Static140.method2367(super.aClass2_Sub34_34.toolkit.value());
    }

    @OriginalMember(owner = "client!jha", name = "a", descriptor = "(Z)I")
    public int value() {
        return super.anInt9489;
    }

    @OriginalMember(owner = "client!jha", name = "a", descriptor = "(I)I")
    @Override
    protected int method8354() {
        return 0;
    }
}

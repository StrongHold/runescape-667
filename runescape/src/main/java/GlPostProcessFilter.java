import com.jagex.core.datastruct.key.Node;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!jea")
public abstract class GlPostProcessFilter extends Node {

    @OriginalMember(owner = "client!jea", name = "p", descriptor = "Z")
    public boolean aBoolean387;

    @OriginalMember(owner = "client!jea", name = "k", descriptor = "Lclient!qha;")
    protected final GlToolkit aClass19_Sub3_22;

    @OriginalMember(owner = "client!jea", name = "<init>", descriptor = "(Lclient!qha;)V")
    public GlPostProcessFilter(@OriginalArg(0) GlToolkit arg0) {
        this.aClass19_Sub3_22 = arg0;
    }

    @OriginalMember(owner = "client!jea", name = "d", descriptor = "(I)I")
    public final int method4605() {
        return 1;
    }

    @OriginalMember(owner = "client!jea", name = "a", descriptor = "(BI)V")
    public abstract void method4607(@OriginalArg(1) int arg0);

    @OriginalMember(owner = "client!jea", name = "c", descriptor = "(B)V")
    public abstract void method4608();

    @OriginalMember(owner = "client!jea", name = "b", descriptor = "(B)Z")
    public abstract boolean method4609();

    @OriginalMember(owner = "client!jea", name = "a", descriptor = "(Lclient!rq;Lclient!rq;II)V")
    public abstract void method4611(@OriginalArg(0) Class93_Sub2 arg0, @OriginalArg(1) Class93_Sub2 arg1, @OriginalArg(3) int arg2);

    @OriginalMember(owner = "client!jea", name = "c", descriptor = "(I)I")
    public int method4612() {
        return 0;
    }

    @OriginalMember(owner = "client!jea", name = "d", descriptor = "(B)Z")
    public final boolean method4613() {
        return false;
    }

    @OriginalMember(owner = "client!jea", name = "a", descriptor = "(III)V")
    public abstract void method4614(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1);

    @OriginalMember(owner = "client!jea", name = "a", descriptor = "(I)Z")
    public final boolean method4616() {
        return this.aBoolean387;
    }

    @OriginalMember(owner = "client!jea", name = "e", descriptor = "(B)Z")
    public abstract boolean method4617();
}

import com.jagex.collect.ref.ReferenceCache;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nv")
public final class Class267 {

    @OriginalMember(owner = "client!nv", name = "e", descriptor = "Lclient!dla;")
    public final ReferenceCache aReferenceCache_142 = new ReferenceCache(64);

    @OriginalMember(owner = "client!nv", name = "g", descriptor = "Lclient!dla;")
    public final ReferenceCache aReferenceCache_143 = new ReferenceCache(2);

    @OriginalMember(owner = "client!nv", name = "b", descriptor = "Lclient!sb;")
    public final Class330 aClass330_88;

    @OriginalMember(owner = "client!nv", name = "d", descriptor = "Lclient!sb;")
    public final Class330 aClass330_89;

    @OriginalMember(owner = "client!nv", name = "<init>", descriptor = "(Lclient!ul;ILclient!sb;Lclient!sb;)V")
    public Class267(@OriginalArg(0) Class377 arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Class330 arg2, @OriginalArg(3) Class330 arg3) {
        this.aClass330_88 = arg3;
        this.aClass330_89 = arg2;
        this.aClass330_89.method7608(33);
    }

    @OriginalMember(owner = "client!nv", name = "b", descriptor = "(II)V")
    public void method5970() {
        @Pc(11) ReferenceCache local11 = this.aReferenceCache_142;
        synchronized (this.aReferenceCache_142) {
            this.aReferenceCache_142.clean(5);
        }
        local11 = this.aReferenceCache_143;
        synchronized (this.aReferenceCache_143) {
            this.aReferenceCache_143.clean(5);
        }
    }

    @OriginalMember(owner = "client!nv", name = "a", descriptor = "(B)V")
    public void method5972() {
        @Pc(9) ReferenceCache local9 = this.aReferenceCache_142;
        synchronized (this.aReferenceCache_142) {
            this.aReferenceCache_142.removeSoftReferences();
        }
        local9 = this.aReferenceCache_143;
        synchronized (this.aReferenceCache_143) {
            this.aReferenceCache_143.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!nv", name = "a", descriptor = "(II)Lclient!vla;")
    public Class389 method5973(@OriginalArg(1) int arg0) {
        @Pc(6) ReferenceCache local6 = this.aReferenceCache_142;
        @Pc(18) Class389 local18;
        synchronized (this.aReferenceCache_142) {
            local18 = (Class389) this.aReferenceCache_142.get((long) arg0);
        }
        if (local18 != null) {
            return local18;
        }
        @Pc(32) Class330 local32 = this.aClass330_89;
        @Pc(41) byte[] local41;
        synchronized (this.aClass330_89) {
            local41 = this.aClass330_89.method7595(arg0, 33);
        }
        local18 = new Class389();
        local18.aClass267_2 = this;
        if (local41 != null) {
            local18.method8935(new Packet(local41));
        }
        @Pc(70) ReferenceCache local70 = this.aReferenceCache_142;
        synchronized (this.aReferenceCache_142) {
            this.aReferenceCache_142.put(local18, (long) arg0);
            return local18;
        }
    }

    @OriginalMember(owner = "client!nv", name = "b", descriptor = "(B)V")
    public void method5974() {
        @Pc(7) ReferenceCache local7 = this.aReferenceCache_142;
        synchronized (this.aReferenceCache_142) {
            this.aReferenceCache_142.reset();
        }
        local7 = this.aReferenceCache_143;
        synchronized (this.aReferenceCache_143) {
            this.aReferenceCache_143.reset();
        }
    }
}

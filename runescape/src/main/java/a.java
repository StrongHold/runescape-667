import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.sw.SoftwareMemoryManager;
import com.jagex.graphics.sw.SoftwareObject;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!a")
public final class a implements SoftwareObject {

    @OriginalMember(owner = "client!a", name = "nativeid", descriptor = "J")
    public long nativeid;

    @OriginalMember(owner = "client!a", name = "d", descriptor = "Ljava/lang/Runnable;")
    public Runnable aRunnable1;

    @OriginalMember(owner = "client!a", name = "b", descriptor = "[Lclient!i;")
    public final i[] anIArray1 = new i[7];

    @OriginalMember(owner = "client!a", name = "a", descriptor = "[Lclient!i;")
    public final i[] anIArray2 = new i[7];

    @OriginalMember(owner = "client!a", name = "c", descriptor = "Lclient!oa;")
    public final oa anOa1;

    @OriginalMember(owner = "client!a", name = "<init>", descriptor = "(Lclient!oa;II)V")
    public a(@OriginalArg(0) oa arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        this.anOa1 = arg0;
        for (@Pc(14) int local14 = 0; local14 < 7; local14++) {
            this.anIArray2[local14] = new i(this.anOa1);
            this.anIArray1[local14] = new i(this.anOa1);
        }
        this.HA(this.nativeid, arg0, arg1, arg2);
    }

    @OriginalMember(owner = "client!a", name = "na", descriptor = "(JLclient!ha;IIIIIII)V")
    public native void na(@OriginalArg(0) long arg0, @OriginalArg(1) Toolkit arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8);

    @OriginalMember(owner = "client!a", name = "O", descriptor = "(JLclient!ha;[I[I[I[SI)V")
    public native void O(@OriginalArg(0) long arg0, @OriginalArg(1) Toolkit arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int[] arg4, @OriginalArg(5) short[] arg5, @OriginalArg(6) int arg6);

    @OriginalMember(owner = "client!a", name = "Z", descriptor = "(JJIII)V")
    public native void Z(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4);

    @OriginalMember(owner = "client!a", name = "b", descriptor = "()V")
    public void method1() {
        this.aRunnable1 = Thread.currentThread();
        this.method7();
    }

    @OriginalMember(owner = "client!a", name = "f", descriptor = "(JJJ[III)V")
    public native void f(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) long arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5);

    @OriginalMember(owner = "client!a", name = "w", descriptor = "(Z)V")
    @Override
    public void w(@OriginalArg(0) boolean arg0) {
        this.E(this.nativeid, arg0);
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!s;II)V")
    public void method2(@OriginalArg(0) Ground arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        this.H(this.nativeid, ((t) arg0).nativeid, arg1, arg2);
    }

    @OriginalMember(owner = "client!a", name = "r", descriptor = "(JJJIIIZ)V")
    public native void r(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) long arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) boolean arg6);

    @OriginalMember(owner = "client!a", name = "ta", descriptor = "(JJIIIIIII[[Z)V")
    public native void ta(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) boolean[][] arg9);

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!i;BIZ)Lclient!ka;")
    public Model method3(@OriginalArg(0) i arg0, @OriginalArg(1) byte arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean arg3) {
        @Pc(1) boolean local1 = false;
        @Pc(20) i local20;
        @Pc(13) i local13;
        if (arg1 > 0 && arg1 <= 7) {
            local13 = this.anIArray1[arg1 - 1];
            local20 = this.anIArray2[arg1 - 1];
            local1 = true;
        } else {
            local20 = local13 = new i(this.anOa1);
        }
        arg0.ZA(local20, local13, arg2, local1, arg3);
        local20.aModelParticleEmitterArray3 = arg0.aModelParticleEmitterArray3;
        local20.aModelParticleEffectorArray3 = arg0.aModelParticleEffectorArray3;
        return local20;
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ha;[I[I[I[SI)V")
    public void renderParticles(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) short[] arg4, @OriginalArg(5) int arg5) {
        this.O(this.nativeid, arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ka;IILclient!tt;ZI)Z")
    public boolean method5(@OriginalArg(0) Model arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) Matrix arg3, @OriginalArg(4) boolean arg4, @OriginalArg(5) int arg5) {
        return this.n(this.nativeid, ((i) arg0).nativeid, arg1, arg2, ((ja) arg3).nativeid, arg4, arg5);
    }

    @OriginalMember(owner = "client!a", name = "H", descriptor = "(JJII)V")
    public native void H(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!a", name = "finalize", descriptor = "()V")
    @Override
    public void finalize() {
        if (this.nativeid != 0L) {
            SoftwareMemoryManager.free(this);
        }
    }

    @OriginalMember(owner = "client!a", name = "UA", descriptor = "(JJJ[II)V")
    public native void UA(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) long arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int arg4);

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!s;III)V")
    public void method6(@OriginalArg(0) Ground arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        this.Z(this.nativeid, ((t) arg0).nativeid, arg1, arg2, arg3);
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "()V")
    public void method7() {
        this.M(this.nativeid);
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ka;Lclient!tt;[II)V")
    public void method8(@OriginalArg(0) Model arg0, @OriginalArg(1) Matrix arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int arg3) {
        this.UA(this.nativeid, ((i) arg0).nativeid, ((ja) arg1).nativeid, arg2, arg3);
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ka;Lclient!tt;[III)V")
    public void method9(@OriginalArg(0) Model arg0, @OriginalArg(1) Matrix arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4) {
        this.f(this.nativeid, ((i) arg0).nativeid, ((ja) arg1).nativeid, arg2, arg3, arg4);
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ka;Lclient!ka;IIIZ)V")
    public void method10(@OriginalArg(0) Model arg0, @OriginalArg(1) Model arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5) {
        this.r(this.nativeid, ((i) arg0).nativeid, ((i) arg1).nativeid, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!a", name = "n", descriptor = "(JJIIJZI)Z")
    public native boolean n(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) long arg4, @OriginalArg(5) boolean arg5, @OriginalArg(6) int arg6);

    @OriginalMember(owner = "client!a", name = "E", descriptor = "(JZ)V")
    public native void E(@OriginalArg(0) long arg0, @OriginalArg(1) boolean arg1);

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ka;IILclient!tt;Z)Z")
    public boolean method11(@OriginalArg(0) Model arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) Matrix arg3, @OriginalArg(4) boolean arg4) {
        return this.R(this.nativeid, ((i) arg0).nativeid, arg1, arg2, ((ja) arg3).nativeid, arg4);
    }

    @OriginalMember(owner = "client!a", name = "d", descriptor = "()V")
    public void method12() {
        this.W(this.nativeid);
    }

    @OriginalMember(owner = "client!a", name = "e", descriptor = "(JJ[IJ)V")
    public native void e(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) long arg3);

    @OriginalMember(owner = "client!a", name = "HA", descriptor = "(JLclient!ha;II)V")
    public native void HA(@OriginalArg(0) long arg0, @OriginalArg(1) Toolkit arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!s;IIIIIII[[Z)V")
    public void method13(@OriginalArg(0) Ground arg0, @OriginalArg(4) int arg1, @OriginalArg(5) int arg2, @OriginalArg(6) int arg3, @OriginalArg(7) int arg4, @OriginalArg(8) boolean[][] arg5) {
        this.ta(this.nativeid, ((t) arg0).nativeid, 0, 0, 1024, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!a", name = "R", descriptor = "(JJIIJZ)Z")
    public native boolean R(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) long arg4, @OriginalArg(5) boolean arg5);

    @OriginalMember(owner = "client!a", name = "W", descriptor = "(J)V")
    public native void W(@OriginalArg(0) long arg0);

    @OriginalMember(owner = "client!a", name = "M", descriptor = "(J)V")
    public native void M(@OriginalArg(0) long arg0);

    @OriginalMember(owner = "client!a", name = "c", descriptor = "()V")
    public void method14() {
        this.aRunnable1 = null;
        this.method12();
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ka;[ILclient!tt;)V")
    public void method15(@OriginalArg(0) Model arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) Matrix arg2) {
        this.e(this.nativeid, ((i) arg0).nativeid, arg1, ((ja) arg2).nativeid);
    }

    @OriginalMember(owner = "client!a", name = "a", descriptor = "(Lclient!ha;IIIIIII)V")
    public void method16(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int mode) {
        this.na(this.nativeid, arg0, arg1, arg2, arg3, arg4, arg5, arg6, mode);
    }
}

import com.jagex.DepthBuffer;
import com.jagex.graphics.sw.SoftwareMemoryManager;
import com.jagex.graphics.sw.SoftwareObject;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!xa")
public final class xa implements DepthBuffer, SoftwareObject {

    @OriginalMember(owner = "client!xa", name = "nativeid", descriptor = "J")
    public long nativeid = 0L;

    @OriginalMember(owner = "client!xa", name = "<init>", descriptor = "(II)V")
    public xa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        this.r(arg0, arg1);
    }

    @OriginalMember(owner = "client!xa", name = "finalize", descriptor = "()V")
    @Override
    public void finalize() {
        if (this.nativeid != 0L) {
            SoftwareMemoryManager.free(this);
        }
    }

    @OriginalMember(owner = "client!xa", name = "r", descriptor = "(II)V")
    public native void r(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1);

    @OriginalMember(owner = "client!xa", name = "va", descriptor = "(JZ)V")
    public native void va(@OriginalArg(0) long arg0, @OriginalArg(1) boolean arg1);

    @OriginalMember(owner = "client!xa", name = "w", descriptor = "(Z)V")
    @Override
    public void w(@OriginalArg(0) boolean arg0) {
        this.va(this.nativeid, arg0);
    }
}

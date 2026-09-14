import com.jagex.core.datastruct.key.Node;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!cja")
public final class PingRequest extends Node {

    @OriginalMember(owner = "client!cja", name = "o", descriptor = "I")
    public volatile int ping = -1;

    @OriginalMember(owner = "client!cja", name = "n", descriptor = "Ljava/lang/String;")
    public final String address;

    @OriginalMember(owner = "client!cja", name = "<init>", descriptor = "(Ljava/lang/String;)V")
    public PingRequest(@OriginalArg(0) String address) {
        this.address = address;
    }
}

import com.jagex.core.datastruct.Node2;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!up")
public class Particle extends Node2 {

    @OriginalMember(owner = "client!up", name = "w", descriptor = "Z")
    public boolean preserveAmbient;

    @OriginalMember(owner = "client!up", name = "n", descriptor = "I")
    public int y;

    @OriginalMember(owner = "client!up", name = "v", descriptor = "I")
    public int size;

    @OriginalMember(owner = "client!up", name = "m", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!up", name = "p", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!up", name = "t", descriptor = "I")
    public int colour;

    @OriginalMember(owner = "client!up", name = "o", descriptor = "I")
    public int texture;

    @OriginalMember(owner = "client!up", name = "s", descriptor = "B")
    public byte aByte122 = 5;

    @OriginalMember(owner = "client!up", name = "<init>", descriptor = "()V")
    protected Particle() {
    }
}

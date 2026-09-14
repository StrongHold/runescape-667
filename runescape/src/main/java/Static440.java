import com.jagex.sound.AudioBuss;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static440 {

    @OriginalMember(owner = "client!nt", name = "j", descriptor = "I")
    public static int anInt6680 = 0;

    @OriginalMember(owner = "client!nt", name = "h", descriptor = "I")
    public static int anInt6683 = 1;

    @OriginalMember(owner = "client!nt", name = "f", descriptor = "[I")
    public static final int[] anIntArray529 = new int[2];

    @OriginalMember(owner = "client!nt", name = "a", descriptor = "(ILclient!dea;)V")
    public static void method5964(@OriginalArg(1) AudioBuss arg0) {
        if (arg0.aClass2_Sub49_6 != null) {
            arg0.aClass2_Sub49_6.anInt8817 = 0;
        }
        arg0.active = false;
        for (@Pc(25) AudioBuss local25 = arg0.firstSubStream(); local25 != null; local25 = arg0.nextSubStream()) {
            method5964(local25);
        }
    }
}

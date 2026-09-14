import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static483 {

    @OriginalMember(owner = "client!pe", name = "k", descriptor = "Lclient!uf;")
    public static final Class370 aClass370_7 = new Class370();

    @OriginalMember(owner = "client!pe", name = "a", descriptor = "(IIB)Z")
    public static boolean method6488(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return (arg1 & 0x10) != 0;
    }

    @OriginalMember(owner = "client!pe", name = "c", descriptor = "(I)V")
    public static void method6490(@OriginalArg(0) int w2debug) {
        MapArea.w2debug = w2debug;
    }

    @OriginalMember(owner = "client!pe", name = "a", descriptor = "(I[BIBII)V")
    public static void method6491(@OriginalArg(0) int end, @OriginalArg(1) byte[] data, @OriginalArg(2) int offset, @OriginalArg(5) int start) {
        if (start >= end) {
            return;
        }
        offset += start;
        @Pc(20) int remaining = end - start >> 2;
        while (true) {
            remaining--;
            if (remaining < 0) {
                remaining = end - start & 0x3;
                while (true) {
                    remaining--;
                    if (remaining < 0) {
                        return;
                    }
                    data[offset++] = 1;
                }
            }
            @Pc(35) int next1 = offset + 1;
            data[offset] = 1;
            @Pc(40) int next2 = next1 + 1;
            data[next1] = 1;
            @Pc(45) int next3 = next2 + 1;
            data[next2] = 1;
            offset = next3 + 1;
            data[next3] = 1;
        }
    }
}

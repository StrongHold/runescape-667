import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

import java.awt.Component;

@OriginalClass("client!ei")
public interface PcmDevice {

    @OriginalMember(owner = "client!ei", name = "a", descriptor = "(II)I")
    int position(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!ei", name = "a", descriptor = "(III)V")
    void open(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) throws Exception;

    @OriginalMember(owner = "client!ei", name = "a", descriptor = "(IZLjava/awt/Component;B)V")
    void init(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Component arg2) throws Exception;

    @OriginalMember(owner = "client!ei", name = "a", descriptor = "(IB)V")
    void discardBuffer(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!ei", name = "a", descriptor = "(I[I)V")
    void write(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1);

    @OriginalMember(owner = "client!ei", name = "a", descriptor = "(IZ)V")
    void close(@OriginalArg(0) int arg0);
}

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static119 {

    @OriginalMember(owner = "client!dma", name = "l", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___11 = new CutsceneActionType(41);

    @OriginalMember(owner = "client!dma", name = "h", descriptor = "[I")
    public static final int[] anIntArray199 = new int[64];

    @OriginalMember(owner = "client!dma", name = "a", descriptor = "(Lclient!kn;)V")
    public static void setActivePickList(@OriginalArg(0) PickList pickList) {
        Static514.activePickList = pickList;
    }

    @OriginalMember(owner = "client!dma", name = "b", descriptor = "(I)Lclient!fk;")
    public static ClientMessage method2174() {
        return Static76.anInt1604 == 0 ? new ClientMessage() : Static372.aClass2_Sub19Array1[--Static76.anInt1604];
    }

    @OriginalMember(owner = "client!dma", name = "a", descriptor = "(IBI)Z")
    public static boolean method2175(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x21) != 0;
    }
}

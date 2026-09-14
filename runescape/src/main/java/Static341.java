import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static341 {

    @OriginalMember(owner = "client!ko", name = "J", descriptor = "[[I")
    public static int[][] entityDrawPriorities;

    @OriginalMember(owner = "client!ko", name = "g", descriptor = "(I)V")
    public static void method5033(@OriginalArg(0) int arg0) {
        if (arg0 == 0) {
            if (Static32.anInt772 == 2) {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[0]);
                Static226.aClass46Array7[1].method1105(MapArea.renderQueues[1]);
            } else if (Static32.anInt772 == 3) {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[0]);
                Static226.aClass46Array7[1].method1105(MapArea.renderQueues[1]);
                Static226.aClass46Array7[2].method1105(MapArea.renderQueues[2]);
            } else {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[0]);
                Static226.aClass46Array7[1].method1105(MapArea.renderQueues[1]);
                Static226.aClass46Array7[2].method1105(MapArea.renderQueues[2]);
                Static226.aClass46Array7[3].method1105(MapArea.renderQueues[3]);
            }
        } else if (arg0 == 1) {
            if (Static32.anInt772 == 2) {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[2]);
            } else if (Static32.anInt772 == 3) {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[3]);
                Static226.aClass46Array7[1].method1105(MapArea.renderQueues[4]);
            } else {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[4]);
                Static226.aClass46Array7[1].method1105(MapArea.renderQueues[5]);
                Static226.aClass46Array7[2].method1105(MapArea.renderQueues[6]);
            }
        } else if (arg0 == 2) {
            if (Static32.anInt772 == 2) {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[3]);
                return;
            }
            if (Static32.anInt772 == 3) {
                Static226.aClass46Array7[0].method1105(MapArea.renderQueues[5]);
                return;
            }
            Static226.aClass46Array7[0].method1105(MapArea.renderQueues[7]);
        }
    }

}

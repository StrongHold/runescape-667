import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!hc")
public final class CutsceneActionType {

    @OriginalMember(owner = "client!lea", name = "h", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___30 = new CutsceneActionType(12);

    @OriginalMember(owner = "client!bv", name = "q", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___25 = new CutsceneActionType(43);

    @OriginalMember(owner = "client!kha", name = "h", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___23 = new CutsceneActionType(70);

    @OriginalMember(owner = "client!rja", name = "C", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___37 = new CutsceneActionType(0);

    @OriginalMember(owner = "client!sha", name = "h", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___40 = new CutsceneActionType(30);

    @OriginalMember(owner = "client!he", name = "c", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___17 = new CutsceneActionType(16);

    @OriginalMember(owner = "client!vka", name = "f", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___43 = new CutsceneActionType(61);

    @OriginalMember(owner = "client!ah", name = "f", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___24 = new CutsceneActionType(13);

    @OriginalMember(owner = "client!gka", name = "v", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___16 = new CutsceneActionType(255);

    @OriginalMember(owner = "client!cp", name = "h", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___9 = new CutsceneActionType(2);

    @OriginalMember(owner = "client!ot", name = "b", descriptor = "(II)Lclient!hc;")
    public static CutsceneActionType fromId(@OriginalArg(0) int id) {
        @Pc(8) CutsceneActionType[] values = values();
        for (@Pc(19) int i = 0; i < values.length; i++) {
            @Pc(25) CutsceneActionType type = values[i];
            if (id == type.id) {
                return type;
            }
        }
        return null;
    }

    @OriginalMember(owner = "client!pfa", name = "b", descriptor = "(B)[Lclient!hc;")
    public static CutsceneActionType[] values() {
        return new CutsceneActionType[]{A_CUTSCENE_ACTION_TYPE___37, Static289.A_CUTSCENE_ACTION_TYPE___19, A_CUTSCENE_ACTION_TYPE___9, Static582.A_CUTSCENE_ACTION_TYPE___39, Static512.A_CUTSCENE_ACTION_TYPE___34, Static65.A_CUTSCENE_ACTION_TYPE___8, A_CUTSCENE_ACTION_TYPE___30, A_CUTSCENE_ACTION_TYPE___24, Static384.A_CUTSCENE_ACTION_TYPE___26, Static517.HIT, A_CUTSCENE_ACTION_TYPE___17, Static418.A_CUTSCENE_ACTION_TYPE___41, Static458.LOC_ADD, Static569.A_CUTSCENE_ACTION_TYPE___38, Static204.A_CUTSCENE_ACTION_TYPE___14, A_CUTSCENE_ACTION_TYPE___40, Static432.A_CUTSCENE_ACTION_TYPE___3, Static177.A_CUTSCENE_ACTION_TYPE___13, Static418.A_CUTSCENE_ACTION_TYPE___42, Static512.A_CUTSCENE_ACTION_TYPE___35, Static119.A_CUTSCENE_ACTION_TYPE___11, Static205.A_CUTSCENE_ACTION_TYPE___15, A_CUTSCENE_ACTION_TYPE___25, Static470.A_CUTSCENE_ACTION_TYPE___32, Static488.A_CUTSCENE_ACTION_TYPE___33, Static126.A_CUTSCENE_ACTION_TYPE___12, Static308.A_CUTSCENE_ACTION_TYPE___21, Static719.A_CUTSCENE_ACTION_TYPE___45, A_CUTSCENE_ACTION_TYPE___43, A_CUTSCENE_ACTION_TYPE___23, A_CUTSCENE_ACTION_TYPE___16};
    }


    @OriginalMember(owner = "client!hc", name = "c", descriptor = "I")
    public final int id;

    @OriginalMember(owner = "client!hc", name = "<init>", descriptor = "(I)V")
    public CutsceneActionType(@OriginalArg(0) int id) {
        this.id = id;
    }

    @OriginalMember(owner = "client!hc", name = "toString", descriptor = "()Ljava/lang/String;")
    @Override
    public String toString() {
        throw new IllegalStateException();
    }
}

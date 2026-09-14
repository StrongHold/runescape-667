import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pi")
public abstract class CutsceneAction {

    @OriginalMember(owner = "client!hf", name = "a", descriptor = "(Lclient!ge;B)Lclient!pi;")
    public static CutsceneAction decode(@OriginalArg(0) Packet packet) {
        @Pc(7) int typeId = packet.g1();
        @Pc(19) CutsceneActionType type = CutsceneActionType.fromId(typeId);

        @Pc(21) CutsceneAction action = null;
        if (Static458.LOC_ADD == type) {
            action = new CutsceneAction_Sub16(packet);
        } else if (Static569.A_CUTSCENE_ACTION_TYPE___38 == type) {
            action = new CutsceneAction_Sub3(packet);
        } else if (Static204.A_CUTSCENE_ACTION_TYPE___14 == type) {
            action = new CutsceneAction_Sub7(packet);
        } else if (type == CutsceneActionType.A_CUTSCENE_ACTION_TYPE___40) {
            action = new CutsceneAction_Sub13(packet);
        } else if (Static432.A_CUTSCENE_ACTION_TYPE___3 == type) {
            action = new CutsceneAction_Sub14(packet);
        } else if (Static177.A_CUTSCENE_ACTION_TYPE___13 == type) {
            action = new CutsceneAction_Sub9(packet);
        } else if (type == Static418.A_CUTSCENE_ACTION_TYPE___42) {
            action = new CutsceneAction_Sub15(packet);
        } else if (type == Static205.A_CUTSCENE_ACTION_TYPE___15) {
            action = new CutsceneAction_Sub1_Sub1(packet);
        } else if (type == Static119.A_CUTSCENE_ACTION_TYPE___11) {
            action = new CutsceneAction_Sub6(packet);
        } else if (type == Static512.A_CUTSCENE_ACTION_TYPE___35) {
            action = new CutsceneAction_Sub5(packet);
        } else if (Static289.A_CUTSCENE_ACTION_TYPE___19 == type) {
            action = new CameraSpline(packet);
        } else if (type == CutsceneActionType.A_CUTSCENE_ACTION_TYPE___37) {
            action = new CutsceneAction_Sub11(packet);
        } else if (Static512.A_CUTSCENE_ACTION_TYPE___34 == type) {
            action = new CutsceneAction_Sub4(packet);
        } else if (type == Static65.A_CUTSCENE_ACTION_TYPE___8) {
            action = new CutsceneAction_Sub20(packet);
        } else if (CutsceneActionType.A_CUTSCENE_ACTION_TYPE___30 == type) {
            action = new CutsceneAction_Sub21(packet);
        } else if (type == CutsceneActionType.A_CUTSCENE_ACTION_TYPE___24) {
            action = new CutsceneAction_Sub2(packet);
        } else if (type == Static384.A_CUTSCENE_ACTION_TYPE___26) {
            action = new CutsceneAction_Sub17(packet);
        } else if (type == Static517.HIT) {
            action = new CutsceneAction_Sub10(packet);
        } else if (CutsceneActionType.A_CUTSCENE_ACTION_TYPE___17 == type) {
            action = new CutsceneAction_Sub12(packet);
        } else if (Static418.A_CUTSCENE_ACTION_TYPE___41 == type) {
            action = new CutsceneAction_Sub1_Sub2(packet);
        } else if (Static470.A_CUTSCENE_ACTION_TYPE___32 == type) {
            action = new CutsceneAction_Sub23(packet, 1, 1);
        } else if (type == Static488.A_CUTSCENE_ACTION_TYPE___33) {
            action = new CutsceneAction_Sub23(packet, 0, 1);
        } else if (type == Static126.A_CUTSCENE_ACTION_TYPE___12) {
            action = new CutsceneAction_Sub23(packet, 0, 0);
        } else if (type == Static308.A_CUTSCENE_ACTION_TYPE___21) {
            action = new CutsceneAction_Sub23(packet, 1, 0);
        } else if (Static719.A_CUTSCENE_ACTION_TYPE___45 == type) {
            action = new VarUpdate(packet, false);
        } else if (CutsceneActionType.A_CUTSCENE_ACTION_TYPE___43 == type) {
            action = new VarUpdate(packet, true);
        } else if (CutsceneActionType.A_CUTSCENE_ACTION_TYPE___23 == type) {
            action = new CutsceneAction_Sub8(packet);
        } else if (type == CutsceneActionType.A_CUTSCENE_ACTION_TYPE___16) {
            action = new CutsceneAction_Sub22(packet);
        }

        return action;
    }

    @OriginalMember(owner = "client!pi", name = "f", descriptor = "I")
    public final int startTime;

    @OriginalMember(owner = "client!pi", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction(@OriginalArg(0) Packet packet) {
        this.startTime = packet.g2();
    }

    @OriginalMember(owner = "client!pi", name = "a", descriptor = "(Z)Z")
    public boolean ready() {
        return true;
    }

    @OriginalMember(owner = "client!pi", name = "b", descriptor = "(I)V")
    public abstract void execute();
}

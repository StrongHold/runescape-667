import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.vartype.VarcTypeList;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static618 {

    @OriginalMember(owner = "client!tja", name = "B", descriptor = "I")
    public static int anInt9449;

    @OriginalMember(owner = "client!tja", name = "G", descriptor = "Lclient!sia;")
    public static final Deque A_DEQUE___68 = new Deque();

    @OriginalMember(owner = "client!tja", name = "a", descriptor = "(III)Z")
    public static boolean retainFaceIndices(@OriginalArg(0) int arg0, @OriginalArg(1) int functionMask, @OriginalArg(2) int flags) {
        return Static335.faceIndicesMutable(flags, functionMask) | (functionMask & 0x60000) != 0 || Static69.indexBufferMutable(functionMask, flags);
    }

    @OriginalMember(owner = "client!tja", name = "a", descriptor = "(BLclient!ge;)V")
    public static void decodeVarcs(@OriginalArg(1) Packet packet) {
        if (packet.data.length - packet.pos < 1) {
            return;
        }

        @Pc(21) int version = packet.g1();
        if (version < 0 || version > 1 || packet.data.length - packet.pos < 2) {
            return;
        }

        @Pc(62) int count = packet.g2();
        if ((count * 6) > (packet.data.length - packet.pos)) {
            return;
        }

        for (@Pc(80) int i = 0; i < count; i++) {
            @Pc(86) int id = packet.g2();
            @Pc(90) int value = packet.g4();

            if (id < Static511.varcs.length && Static118.permVarcs[id] && (VarcTypeList.instance.list(id).dataType != '1' || value >= -1 && value <= 1)) {
                Static511.varcs[id] = value;
            }
        }
    }

    @OriginalMember(owner = "client!tja", name = "a", descriptor = "(Lclient!sb;IZIIIZ)V")
    public static void playSongWithFade(@OriginalArg(0) js5 midiSongs, @OriginalArg(1) int volume, @OriginalArg(4) int id, @OriginalArg(5) int arg3) {
        if (arg3 <= 0) {
            SongManager.method8229(id, volume, midiSongs);
            return;
        }
        SongManager.volume = volume;
        SongManager.anInt10171 = 1;
        SongManager.midiSongs = midiSongs;
        SongManager.aBoolean564 = false;
        SongManager.aClass2_Sub6_Sub1_2 = null;
        SongManager.groupId = id;
        SongManager.midiFileId = 0;
        SongManager.anInt3112 = Static581.mixBuss.getVolume() / arg3;
        if (SongManager.anInt3112 < 1) {
            SongManager.anInt3112 = 1;
        }
    }

}

import com.jagex.sound.midi.MidiSong;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static522 {

    @OriginalMember(owner = "client!qha", name = "Xd", descriptor = "Lclient!cja;")
    public static PingRequest pingRequest = null;

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ILclient!bn;)V")
    public static void playSongNow(@OriginalArg(1) MidiSong arg0) {
        Static581.mixBuss.method934(arg0, false);
        if (Static426.aPcmPlayer_2 != null) {
            Static426.aPcmPlayer_2.method3582(Static581.mixBuss);
        }
        Static62.aClass2_Sub8_3 = null;
        SongManager.anInt10171 = 0;
        SongManager.aClass2_Sub6_Sub1_2 = null;
        Static12.pendingSongSoundCache = null;
        SongManager.midiSongs = null;
    }
}

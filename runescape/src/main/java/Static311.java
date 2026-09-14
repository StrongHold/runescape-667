import com.jagex.sound.SoundCache;
import com.jagex.sound.midi.MidiSong;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static311 {

    @OriginalMember(owner = "client!js", name = "a", descriptor = "(Lclient!bn;I)Z")
    public static boolean method4537(@OriginalArg(0) MidiSong arg0) {
        if (arg0 == null) {
            return true;
        }
        Static62.aClass2_Sub8_3 = null;
        SongManager.anInt10171 = 0;
        Static12.aClass123_4 = null;
        SongManager.aClass2_Sub6_Sub1_2 = null;
        if (arg0 != Static62.aClass2_Sub8_2) {
            Static159.anInt2788 = 0;
            Static62.aClass2_Sub8_2 = arg0;
        }
        SongManager.midiSongs = null;
        if (Static159.anInt2788 == 0) {
            Static270.soundCache = new SoundCache(Static91.synthSoundsJs5, Static296.vorbisJs5);
            Static581.mixBuss.method927();
            Static159.anInt2788 = 1;
        }
        if (Static159.anInt2788 == 1) {
            if (!Static581.mixBuss.method944(Static270.soundCache, Static86.js5_15, arg0)) {
                return false;
            }
            Static270.soundCache = null;
            Static62.aClass2_Sub8_2 = null;
            Static159.anInt2788 = 0;
        }
        return true;
    }

}

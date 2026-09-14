import com.jagex.sign.SignedResourceStatus;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static232 {

    @OriginalMember(owner = "client!hda", name = "Tc", descriptor = "Lclient!faa;")
    public static PcmPlayerThread pcmPlayerThread;

    @OriginalMember(owner = "client!hda", name = "id", descriptor = "I")
    public static int anInt3829;

    @OriginalMember(owner = "client!hda", name = "sd", descriptor = "I")
    public static int cameraNotifyDelay = 0;

    @OriginalMember(owner = "client!hda", name = "c", descriptor = "(I)V")
    public static void method3392() {
        try {
            @Pc(14) int volume;
            if (SongManager.anInt10171 == 1) {
                volume = Static581.mixBuss.getVolume();
                if (volume > 0 && Static581.mixBuss.isPlaying()) {
                    volume -= SongManager.anInt3112;
                    if (volume < 0) {
                        volume = 0;
                    }
                    Static581.mixBuss.setVolume(volume);
                    return;
                }

                Static581.mixBuss.method912();
                Static581.mixBuss.method927();

                Static12.aClass123_4 = null;
                Static62.aClass2_Sub8_3 = null;

                if (SongManager.midiSongs == null) {
                    SongManager.anInt10171 = 0;
                } else {
                    SongManager.anInt10171 = 2;
                }
            }

            if (SongManager.anInt10171 == 3) {
                volume = Static581.mixBuss.getVolume();

                if (volume < SongManager.volume && Static581.mixBuss.isPlaying()) {
                    volume += SongManager.anInt9335;

                    if (volume > SongManager.volume) {
                        volume = SongManager.volume;
                    }

                    Static581.mixBuss.setVolume(volume);
                } else {
                    SongManager.anInt9335 = 0;
                    SongManager.anInt10171 = 0;
                }
            }
        } catch (@Pc(103) Exception local103) {
            local103.printStackTrace();
            Static581.mixBuss.method912();
            SongManager.aClass2_Sub6_Sub1_2 = null;
            Static62.aClass2_Sub8_3 = null;
            SongManager.anInt10171 = 0;
            SongManager.midiSongs = null;
            Static12.aClass123_4 = null;
        }
    }

    @OriginalMember(owner = "client!hda", name = "a", descriptor = "(I)Z")
    public static boolean reflectionCheckRunning() {
        @Pc(10) ReflectionCheck check = (ReflectionCheck) Static631.reflectionChecks.first();
        if (check == null) {
            return false;
        }

        for (@Pc(23) int i = 0; i < check.memberCount; i++) {
            if (check.field[i] != null && check.field[i].status == SignedResourceStatus.IDLE) {
                return false;
            }

            if (check.methods[i] != null && check.methods[i].status == SignedResourceStatus.IDLE) {
                return false;
            }
        }

        return true;
    }
}

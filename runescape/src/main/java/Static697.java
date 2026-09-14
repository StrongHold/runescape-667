import com.jagex.js5.js5;
import com.jagex.sound.MixBuss;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static697 {

    @OriginalMember(owner = "client!wb", name = "a", descriptor = "(ZILclient!bd;)V")
    public static void method9120(@OriginalArg(2) MixBuss arg0) {
        Static719.aPcmPlayer_5.method3582(arg0);
        Static220.initSoundSystem(Static719.aPcmPlayer_5, js5.VORBIS, js5.SYNTH_SOUNDS, js5.js5_15, arg0);
    }
}

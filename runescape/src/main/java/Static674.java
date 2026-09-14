import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.sound.Audio;
import com.jagex.sound.AudioBussMixer;
import com.jagex.sound.SampleRateConverter;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static674 {

    @OriginalMember(owner = "client!vfa", name = "b", descriptor = "(B)V")
    public static void method8806() {
        Static517.configureAudio(ClientOptions.instance.stereoSound.getValue() == 1);
        Static719.aPcmPlayer_5 = Static638.method8394(GameShell.signLink, 0, 22050, GameShell.canvas);
        Static697.method9120(Static48.method1100(null));
        Static559.aPcmPlayer_3 = Static638.method8394(GameShell.signLink, 1, 2048, GameShell.canvas);
        SoundManager.activeStreams = new AudioBussMixer();
        Static559.aPcmPlayer_3.method3582(SoundManager.activeStreams);
        Static681.aSampleRateConverter_2 = new SampleRateConverter(22050, Audio.sampleRate);
        AudioRenderer.mixBussReset();
    }
}

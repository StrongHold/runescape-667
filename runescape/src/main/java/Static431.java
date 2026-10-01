import com.jagex.Entity;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.sound.Sound;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static431 {

    @OriginalMember(owner = "client!nk", name = "v", descriptor = "Lclient!uf;")
    public static final Class370 aClass370_5 = new Class370();

    @OriginalMember(owner = "client!nk", name = "a", descriptor = "(Lclient!cka;ILclient!eo;B)V")
    public static void method5827(@OriginalArg(0) SeqType arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Entity arg2) {
        if (SoundManager.count >= 50 || (arg0 == null || arg0.soundInfo == null || arg0.soundInfo.length <= arg1 || arg0.soundInfo[arg1] == null)) {
            return;
        }
        @Pc(51) int local51 = arg0.soundInfo[arg1][0];
        @Pc(55) int local55 = local51 >> 8;
        @Pc(61) int local61 = local51 >> 5 & 0x7;
        @Pc(80) int local80;
        if (arg0.soundInfo[arg1].length > 1) {
            local80 = (int) (Math.random() * (double) arg0.soundInfo[arg1].length);
            if (local80 > 0) {
                local55 = arg0.soundInfo[arg1][local80];
            }
        }
        @Pc(93) int local93 = local51 & 0x1F;
        local80 = 256;
        if (arg0.soundRateMin != null && arg0.soundRateMax != null) {
            local80 = arg0.soundRateMin[arg1] + (int) ((double) (arg0.soundRateMax[arg1] - arg0.soundRateMin[arg1]) * Math.random());
        }
        @Pc(134) int local134 = arg0.soundVolumes == null ? 255 : arg0.soundVolumes[arg1];
        if (local93 == 0) {
            if (arg2 == PlayerEntity.self) {
                if (!arg0.vorbisSound) {
                    SoundManager.playSynthSound(local55, local61, 0, local134, local80);
                    return;
                }
                SoundManager.playVorbisSound(local55, local61, 0, local134, local80, false);
            }
        } else if (ClientOptions.instance.backgroundSoundVolume.getValue() != 0) {
            @Pc(184) int local184 = arg2.x - 256 >> 9;
            @Pc(191) int local191 = arg2.z - 256 >> 9;
            @Pc(212) int local212 = arg2 == PlayerEntity.self ? 0 : local93 + (local191 << 8) + (local184 << 16) + (arg2.level << 24);
            SoundManager.sounds[SoundManager.count++] = new Sound((byte) (arg0.vorbisSound ? 2 : 1), local55, local61, 0, local134, local80, local212, arg2);
        }
    }
}

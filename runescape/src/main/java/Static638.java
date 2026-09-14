import com.jagex.sign.SignLink;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.game.runetek6.sound.Audio;
import com.jagex.sound.QueueBuss;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Component;

public final class Static638 {

    @OriginalMember(owner = "client!uca", name = "c", descriptor = "(I)V")
    public static void method8393() {
        @Pc(5) LinkedList[] local5 = Class43.A_ENTITY_LIST_ARRAY_1;
        synchronized (Class43.A_ENTITY_LIST_ARRAY_1) {
            for (@Pc(9) int local9 = 0; local9 < Class43.A_ENTITY_LIST_ARRAY_1.length; local9++) {
                Class43.A_ENTITY_LIST_ARRAY_1[local9] = new LinkedList();
                Static159.anIntArray245[local9] = 0;
            }
        }
    }

    @OriginalMember(owner = "client!uca", name = "a", descriptor = "(Lclient!vq;BIILjava/awt/Component;)Lclient!cd;")
    public static PcmPlayer method8394(@OriginalArg(0) SignLink arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) Component arg3) {
        if (Audio.sampleRate == 0) {
            throw new IllegalStateException();
        } else if (arg1 >= 2) {
            throw new IllegalArgumentException();
        } else {
            try {
                @Pc(41) PcmPlayer local41 = (PcmPlayer) Class.forName("PcmPlayer_Sub1").getDeclaredConstructor().newInstance();
                local41.anIntArray315 = new int[(QueueBuss.stereo ? 2 : 1) * 256];
                local41.anInt4098 = arg2;
                local41.method3593(arg3);
                local41.anInt4097 = (-1024 & arg2) + 1024;
                if (local41.anInt4097 > 16384) {
                    local41.anInt4097 = 16384;
                }
                local41.method3588(local41.anInt4097);
                if (Static156.soundThreadPriority > 0 && Static232.pcmPlayerThread == null) {
                    Static232.pcmPlayerThread = new PcmPlayerThread();
                    Static232.pcmPlayerThread.signLink = arg0;
                    arg0.startThread(Static232.pcmPlayerThread, Static156.soundThreadPriority);
                }
                if (Static232.pcmPlayerThread != null) {
                    if (Static232.pcmPlayerThread.players[arg1] != null) {
                        throw new IllegalArgumentException();
                    }
                    Static232.pcmPlayerThread.players[arg1] = local41;
                }
                return local41;
            } catch (@Pc(135) Throwable local135) {
                try {
                    @Pc(141) PcmPlayer_Sub2 local141 = new PcmPlayer_Sub2(arg0, arg1);
                    local141.anIntArray315 = new int[(QueueBuss.stereo ? 2 : 1) * 256];
                    local141.anInt4098 = arg2;
                    local141.method3593(arg3);
                    local141.anInt4097 = 16384;
                    local141.method3588(local141.anInt4097);
                    if (Static156.soundThreadPriority > 0 && Static232.pcmPlayerThread == null) {
                        Static232.pcmPlayerThread = new PcmPlayerThread();
                        Static232.pcmPlayerThread.signLink = arg0;
                        arg0.startThread(Static232.pcmPlayerThread, Static156.soundThreadPriority);
                    }
                    if (Static232.pcmPlayerThread != null) {
                        if (Static232.pcmPlayerThread.players[arg1] != null) {
                            throw new IllegalArgumentException();
                        }
                        Static232.pcmPlayerThread.players[arg1] = local141;
                    }
                    return local141;
                } catch (@Pc(211) Throwable local211) {
                    return new PcmPlayer();
                }
            }
        }
    }

    @OriginalMember(owner = "client!uca", name = "a", descriptor = "(III)Lclient!nda;")
    public static Class8_Sub2_Sub5 method8398(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile local7 = Static334.activeTiles[level][x][z];

        if (local7 == null) {
            return null;
        } else {
            @Pc(15) Class8_Sub2_Sub5 local15 = local7.aClass8_Sub2_Sub5_1;
            local7.aClass8_Sub2_Sub5_1 = null;
            Static109.hide(local15);
            return local15;
        }
    }
}

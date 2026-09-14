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
    public static void clearPickableEntityPool() {
        @Pc(5) LinkedList[] lock = PickableEntityPool.FREE_LISTS;
        synchronized (PickableEntityPool.FREE_LISTS) {
            for (@Pc(9) int cylinderCount = 0; cylinderCount < PickableEntityPool.FREE_LISTS.length; cylinderCount++) {
                PickableEntityPool.FREE_LISTS[cylinderCount] = new LinkedList();
                Static159.pooledCounts[cylinderCount] = 0;
            }
        }
    }

    @OriginalMember(owner = "client!uca", name = "a", descriptor = "(Lclient!vq;BIILjava/awt/Component;)Lclient!cd;")
    public static PcmPlayer method8394(@OriginalArg(0) SignLink signLink, @OriginalArg(2) int index, @OriginalArg(3) int delay, @OriginalArg(4) Component component) {
        if (Audio.sampleRate == 0) {
            throw new IllegalStateException();
        } else if (index >= 2) {
            throw new IllegalArgumentException();
        } else {
            try {
                @Pc(41) PcmPlayer player = (PcmPlayer) Class.forName("PcmPlayer_Sub1").getDeclaredConstructor().newInstance();
                player.anIntArray315 = new int[(QueueBuss.stereo ? 2 : 1) * 256];
                player.anInt4098 = delay;
                player.method3593(component);
                player.anInt4097 = (-1024 & delay) + 1024;
                if (player.anInt4097 > 16384) {
                    player.anInt4097 = 16384;
                }
                player.method3588(player.anInt4097);
                if (Static156.soundThreadPriority > 0 && Static232.pcmPlayerThread == null) {
                    Static232.pcmPlayerThread = new PcmPlayerThread();
                    Static232.pcmPlayerThread.signLink = signLink;
                    signLink.startThread(Static232.pcmPlayerThread, Static156.soundThreadPriority);
                }
                if (Static232.pcmPlayerThread != null) {
                    if (Static232.pcmPlayerThread.players[index] != null) {
                        throw new IllegalArgumentException();
                    }
                    Static232.pcmPlayerThread.players[index] = player;
                }
                return player;
            } catch (@Pc(135) Throwable exception) {
                try {
                    @Pc(141) PcmPlayer_Sub2 player = new PcmPlayer_Sub2(signLink, index);
                    player.anIntArray315 = new int[(QueueBuss.stereo ? 2 : 1) * 256];
                    player.anInt4098 = delay;
                    player.method3593(component);
                    player.anInt4097 = 16384;
                    player.method3588(player.anInt4097);
                    if (Static156.soundThreadPriority > 0 && Static232.pcmPlayerThread == null) {
                        Static232.pcmPlayerThread = new PcmPlayerThread();
                        Static232.pcmPlayerThread.signLink = signLink;
                        signLink.startThread(Static232.pcmPlayerThread, Static156.soundThreadPriority);
                    }
                    if (Static232.pcmPlayerThread != null) {
                        if (Static232.pcmPlayerThread.players[index] != null) {
                            throw new IllegalArgumentException();
                        }
                        Static232.pcmPlayerThread.players[index] = player;
                    }
                    return player;
                } catch (@Pc(211) Throwable fallbackException) {
                    return new PcmPlayer();
                }
            }
        }
    }

    @OriginalMember(owner = "client!uca", name = "a", descriptor = "(III)Lclient!nda;")
    public static Class8_Sub2_Sub5 removeObjStack(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];

        if (tile == null) {
            return null;
        } else {
            @Pc(15) Class8_Sub2_Sub5 entity = tile.aClass8_Sub2_Sub5_1;
            tile.aClass8_Sub2_Sub5_1 = null;
            Static109.hide(entity);
            return entity;
        }
    }
}

import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.js5.js5;
import com.jagex.sound.MixBuss;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static220 {

    @OriginalMember(owner = "client!gq", name = "d", descriptor = "I")
    public static int anInt3562;

    @OriginalMember(owner = "client!gq", name = "a", descriptor = "(III)Z")
    public static boolean method3197(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        return (arg0 & 0x10000) != 0;
    }

    @OriginalMember(owner = "client!gq", name = "a", descriptor = "(IBIII)V")
    public static void method3198(@OriginalArg(0) int currentTick, @OriginalArg(2) int z, @OriginalArg(3) int x, @OriginalArg(4) int level) {
        for (@Pc(6) PositionedSound sound = (PositionedSound) SoundManager.locSounds.first(); sound != null; sound = (PositionedSound) SoundManager.locSounds.next()) {
            SoundManager.method8366(currentTick, sound, level, x, z);
        }

        for (@Pc(42) PositionedSound sound = (PositionedSound) SoundManager.npcSounds.first(); sound != null; sound = (PositionedSound) SoundManager.npcSounds.next()) {
            @Pc(51) BASType basType = sound.npc.getBASType();
            @Pc(57) int animation = sound.npc.animator.getAnimationId();

            @Pc(46) byte movementSpeed = 1;
            if (animation == -1 || sound.npc.ready) {
                movementSpeed = 0;
            } else if (animation == basType.run || animation == basType.runFollowTurn180 || animation == basType.runFollowTurnCw || animation == basType.runFollowTurnCcw) {
                movementSpeed = 2;
            } else if (animation == basType.crawl || animation == basType.crawlFollowTurn180 || animation == basType.crawlFollowTurnCw || animation == basType.crawlFollowTurnCcw) {
                movementSpeed = 3;
            }

            if (sound.movementSpeed != movementSpeed) {
                @Pc(154) int currentSound = NPCEntity.currentSound(sound.npc);

                @Pc(158) NPCType npcType = sound.npc.type;
                if (npcType.multinpcs != null) {
                    npcType = npcType.getMultiNPC(TimedVarDomain.instance);
                }

                if (npcType == null || currentSound == -1) {
                    sound.movementSpeed = movementSpeed;
                    sound.vorbis = false;
                    sound.id = -1;
                } else if (currentSound == sound.id && sound.vorbis == npcType.vorbis) {
                    sound.movementSpeed = movementSpeed;
                    sound.volume = npcType.soundVolume;
                } else {
                    @Pc(198) boolean restart = false;

                    if (sound.stream == null) {
                        restart = true;
                    } else {
                        sound.volume -= 512;

                        if (sound.volume <= 0) {
                            SoundManager.activeStreams.remove(sound.stream);
                            restart = true;
                            sound.stream = null;
                        }
                    }

                    if (restart) {
                        sound.vorbisSound = null;
                        sound.packet = null;
                        sound.vorbis = npcType.vorbis;
                        sound.volume = npcType.soundVolume;
                        sound.movementSpeed = movementSpeed;
                        sound.id = currentSound;
                    }
                }
            }

            sound.x1 = sound.npc.x;
            sound.x2 = sound.npc.x + (sound.npc.getSize() << 8);
            sound.z1 = sound.npc.z;
            sound.z2 = sound.npc.z + (sound.npc.getSize() << 8);

            SoundManager.method8366(currentTick, sound, level, x, z);
        }

        for (@Pc(329) PositionedSound sound = (PositionedSound) SoundManager.playerSounds.first(); sound != null; sound = (PositionedSound) SoundManager.playerSounds.next()) {
            @Pc(338) BASType basType = sound.player.getBASType();
            @Pc(154) int animation = sound.player.animator.getAnimationId();

            @Pc(333) byte movementSpeed = 1;
            if (animation == -1 || sound.player.ready) {
                movementSpeed = 0;
            } else if (animation == basType.run || animation == basType.runFollowTurn180 || basType.runFollowTurnCw == animation || animation == basType.runFollowTurnCcw) {
                movementSpeed = 2;
            } else if (basType.crawl == animation || animation == basType.crawlFollowTurn180 || basType.crawlFollowTurnCw == animation || animation == basType.crawlFollowTurnCcw) {
                movementSpeed = 3;
            }

            if (movementSpeed != sound.movementSpeed) {
                @Pc(448) int currentSound = PlayerEntity.currentSound(sound.player);

                if (sound.id == currentSound && sound.player.vorbis == sound.vorbis) {
                    sound.movementSpeed = movementSpeed;
                    sound.volume = sound.player.soundVolume;
                } else {
                    @Pc(198) boolean restart = false;

                    if (sound.stream == null) {
                        restart = true;
                    } else {
                        sound.volume -= 512;

                        if (sound.volume <= 0) {
                            SoundManager.activeStreams.remove(sound.stream);
                            sound.stream = null;
                            restart = true;
                        }
                    }

                    if (restart) {
                        sound.vorbis = sound.player.vorbis;
                        sound.id = currentSound;
                        sound.volume = sound.player.soundVolume;
                        sound.packet = null;
                        sound.movementSpeed = movementSpeed;
                        sound.vorbisSound = null;
                    }
                }
            }

            sound.x1 = sound.player.x;
            sound.x2 = sound.player.x + (sound.player.getSize() << 8);
            sound.z1 = sound.player.z;
            sound.z2 = sound.player.z + (sound.player.getSize() << 8);

            SoundManager.method8366(currentTick, sound, level, x, z);
        }
    }

    @OriginalMember(owner = "client!gq", name = "a", descriptor = "(Lclient!cd;Lclient!sb;Lclient!sb;Lclient!sb;ILclient!bd;)Z")
    public static boolean method3201(@OriginalArg(0) PcmPlayer pcmPlayer, @OriginalArg(1) js5 vorbis, @OriginalArg(2) js5 synthSounds, @OriginalArg(3) js5 js5_15, @OriginalArg(5) MixBuss mixBuss) {
        Static581.mixBuss = mixBuss;
        Static296.vorbisJs5 = vorbis;
        Static91.synthSoundsJs5 = synthSounds;
        Static286.anIntArray358 = new int[16];
        Static426.aPcmPlayer_2 = pcmPlayer;
        Static86.js5_15 = js5_15;
        for (@Pc(25) int channel = 0; channel < 16; channel++) {
            Static286.anIntArray358[channel] = 255;
        }
        return true;
    }
}

import com.jagex.ChangeLocationRequest;
import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.PrivateChatMode;
import com.jagex.ServerProt;
import com.jagex.StockmarketOffer;
import com.jagex.ZoneProt;
import com.jagex.core.constants.AreaMode;
import com.jagex.core.constants.ChatLineType;
import com.jagex.core.constants.HintArrowType;
import com.jagex.core.constants.LocLayer;
import com.jagex.core.constants.LocShapes;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.io.BitPacket;
import com.jagex.core.io.ConnectionInfo;
import com.jagex.core.io.Packet;
import com.jagex.core.io.connection.Connection;
import com.jagex.core.stringtools.general.Base37;
import com.jagex.core.stringtools.general.Cp1252;
import com.jagex.core.stringtools.general.NameTools;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.core.util.JagException;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.DelayedStateChange;
import com.jagex.game.LocalisedText;
import com.jagex.game.camera.Shake;
import com.jagex.game.compression.huffman.WordPack;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.iftype.ServerActiveProperties;
import com.jagex.game.runetek6.config.iftype.SubInterface;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeCustomisation;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.seqtype.SeqReplayMode;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.game.runetek6.config.vartype.clan.VarClanSettingTypeList;
import com.jagex.game.world.World;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.js5;
import com.jagex.sign.SignedResourceStatus;
import com.jagex.trigger.ClientTriggerType;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.clan.channel.ClanChannel;
import rs2.client.clan.channel.delta.ClanChannelDelta;
import rs2.client.clan.settings.ClanSettings;
import rs2.client.clan.settings.delta.ClanSettingsDelta;
import rs2.client.web.ClientURLTools;
import rs2.client.web.OpenUrlType;

import java.io.IOException;

public final class ServerConnectionReader {

    @OriginalMember(owner = "client!jr", name = "I", descriptor = "[I")
    public static final int[] LOC_LAYERS_BY_SHAPE = {
        /* 00 */ LocLayer.WALL,
        /* 01 */ LocLayer.WALL,
        /* 02 */ LocLayer.WALL,
        /* 03 */ LocLayer.WALL,
        /* 04 */ LocLayer.WALLDECOR,
        /* 05 */ LocLayer.WALLDECOR,
        /* 06 */ LocLayer.WALLDECOR,
        /* 07 */ LocLayer.WALLDECOR,
        /* 08 */ LocLayer.WALLDECOR,
        /* 09 */ LocLayer.GROUND,
        /* 10 */ LocLayer.GROUND,
        /* 11 */ LocLayer.GROUND,
        /* 12 */ LocLayer.GROUND,
        /* 13 */ LocLayer.GROUND,
        /* 14 */ LocLayer.GROUND,
        /* 15 */ LocLayer.GROUND,
        /* 16 */ LocLayer.GROUND,
        /* 17 */ LocLayer.GROUND,
        /* 18 */ LocLayer.GROUND,
        /* 19 */ LocLayer.GROUND,
        /* 20 */ LocLayer.GROUND,
        /* 21 */ LocLayer.GROUND,
        /* 22 */ LocLayer.GROUNDDECOR
    };

    @OriginalMember(owner = "client!jfa", name = "a", descriptor = "(Lclient!gw;I)Z")
    public static boolean decodeServerProt(@OriginalArg(0) ServerConnection context) throws IOException {
        @Pc(8) Connection connection = context.connection;
        @Pc(11) BitPacket bitPacket = context.bitPacket;
        if (connection == null) {
            return false;
        }

        if (context.currentProt == null) {
            if (context.needsOpcode) {
                if (!connection.hasAvailable(1)) {
                    return false;
                }

                connection.read(context.bitPacket.data, 1, 0);
                context.idleReadTicks = 0;
                context.read++;
                context.needsOpcode = false;
            }

            bitPacket.pos = 0;

            if (bitPacket.largeOpcode()) {
                if (!connection.hasAvailable(1)) {
                    return false;
                }

                connection.read(context.bitPacket.data, 1, 1);
                context.read++;
                context.idleReadTicks = 0;
            }

            context.needsOpcode = true;

            @Pc(96) ServerProt[] prots = ServerProt.values();
            @Pc(100) int opcode = bitPacket.readOpcode();
            if (opcode < 0 || prots.length <= opcode) {
                throw new IOException("invo:" + opcode + " ip:" + bitPacket.pos);
            }

            context.currentProt = prots[opcode];
            context.currentPacketSize = context.currentProt.size;
        }

        if (context.currentPacketSize == -1) {
            if (!connection.hasAvailable(1)) {
                return false;
            }

            connection.read(bitPacket.data, 1, 0);
            context.currentPacketSize = bitPacket.data[0] & 0xFF;
            context.read++;
            context.idleReadTicks = 0;
        }

        if (context.currentPacketSize == -2) {
            if (!connection.hasAvailable(2)) {
                return false;
            }

            connection.read(bitPacket.data, 2, 0);
            bitPacket.pos = 0;
            context.currentPacketSize = bitPacket.g2();
            context.read += 2;
            context.idleReadTicks = 0;
        }

        if (context.currentPacketSize > 0) {
            if (!connection.hasAvailable(context.currentPacketSize)) {
                return false;
            }

            bitPacket.pos = 0;
            connection.read(bitPacket.data, context.currentPacketSize, 0);
            context.read += context.currentPacketSize;
            context.idleReadTicks = 0;
        }

        context.antepenultimateProt = context.penultimateProt;
        context.penultimateProt = context.lastProt;
        context.lastProt = context.currentProt;

        if (context.currentProt == ServerProt.IF_CLOSESUB) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt1();
            VerifyId.incrementAndTransmit();

            @Pc(287) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.get(idAndSlot);
            if (sub != null) {
                InterfaceManager.closeSubInterface(sub, true, false);
            }

            if (InterfaceManager.dialog != null) {
                InterfaceManager.redraw(InterfaceManager.dialog);
                InterfaceManager.dialog = null;
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MAP_PROJANIM_HALFSQ) {
            decodeZoneProt(ZoneProt.MAP_PROJANIM_HALFSQ);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.OBJ_REVEAL) {
            decodeZoneProt(ZoneProt.OBJ_REVEAL);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETHIDE) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt2();
            @Pc(100) int visible = bitPacket.g1_alt1();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetHide(idAndSlot, visible);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOC_ANIM) {
            decodeZoneProt(ZoneProt.LOC_ANIM);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SET_MOVEACTION) {
            Static331.moveText = context.currentPacketSize <= 2 ? LocalisedText.WALKHERE.localise(Client.language) : bitPacket.gjstr();
            Static331.moveCursor = context.currentPacketSize <= 0 ? -1 : bitPacket.g2();
            if (Static331.moveCursor == 65535) {
                Static331.moveCursor = -1;
            }
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.OBJ_DEL) {
            decodeZoneProt(ZoneProt.OBJ_DEL);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETCLICKMASK) {
            @Pc(446) boolean clickmask = bitPacket.g1_alt2() == 1;
            @Pc(100) int idAndSlot = bitPacket.g4_alt3();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetClickMask(idAndSlot, clickmask);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETSCROLLPOS) {
            @Pc(277) int position = bitPacket.g2_alt2();
            @Pc(100) int idAndSlot = bitPacket.g4_alt1();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetScrollPosition(idAndSlot, position);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETVIDEO) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt1();
            @Pc(100) int video = bitPacket.g2_alt2();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetVideo(idAndSlot, video);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETPOSITION) {
            @Pc(277) int y = bitPacket.g2s_alt1();
            @Pc(100) int x = bitPacket.g2s_alt1();
            @Pc(526) int idAndSlot = bitPacket.g4_alt3();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetPosition(idAndSlot, x, y);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CLANSETTINGS_FULL) {
            Static400.lastClanSettingsTransmit = World.tick;
            @Pc(446) boolean affined = bitPacket.g1() == 1;

            if (context.currentPacketSize != 1) {
                if (affined) {
                    ClanSettings.affined = new ClanSettings(bitPacket);
                } else {
                    ClanSettings.listened = new ClanSettings(bitPacket);
                }
                context.currentProt = null;
                return true;
            }

            context.currentProt = null;

            if (affined) {
                ClanSettings.affined = null;
            } else {
                ClanSettings.listened = null;
            }
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_QUICKCHAT_FRIENDCHAT) {
            @Pc(446) boolean filtered = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(629) String nameUnfiltered = name;
            if (filtered) {
                nameUnfiltered = bitPacket.gjstr();
            }
            @Pc(639) long channel = bitPacket.g8();
            @Pc(644) long idHi = bitPacket.g2();
            @Pc(649) long idLo = bitPacket.g3();
            @Pc(653) int rank = bitPacket.g1();
            @Pc(657) int quickchatId = bitPacket.g2();
            @Pc(663) long id = (idHi << 32) + idLo;

            @Pc(665) boolean blocked = false;
            @Pc(667) int index = 0;
            while (true) {
                if (index >= 100) {
                    if (rank <= 1 && IgnoreList.contains(nameUnfiltered)) {
                        blocked = true;
                    }
                    break;
                }

                if (Static511.aLongArray17[index] == id) {
                    blocked = true;
                    break;
                }
                index++;
            }

            if (!blocked && Static659.blockChat == 0) {
                Static511.aLongArray17[Static97.anInt2001] = id;
                Static97.anInt2001 = (Static97.anInt2001 + 1) % 100;
                @Pc(737) String message = QuickChatPhraseTypeList.instance.get(quickchatId).decodeText(bitPacket);

                if (rank == 2) {
                    ChatHistory.add(ChatLineType.QUICKCHAT_FRIENDCHAT, 0, "<img=1>" + name, "<img=1>" + nameUnfiltered, name, Base37.decodeName(channel), quickchatId, message);
                } else if (rank == 1) {
                    ChatHistory.add(ChatLineType.QUICKCHAT_FRIENDCHAT, 0, "<img=0>" + name, "<img=0>" + nameUnfiltered, name, Base37.decodeName(channel), quickchatId, message);
                } else {
                    ChatHistory.add(ChatLineType.QUICKCHAT_FRIENDCHAT, 0, name, nameUnfiltered, name, Base37.decodeName(channel), quickchatId, message);
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.WORLDLIST_FETCH_REPLY) {
            @Pc(446) boolean update = bitPacket.g1() == 1;
            @Pc(854) byte[] data = new byte[context.currentPacketSize - 1];
            bitPacket.gdata(0, context.currentPacketSize - 1, data);
            WorldList.decodeWorldList(data, update);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOYALTY_UPDATE) {
            @Pc(277) int balance = bitPacket.g4();
            @Pc(892) boolean loyalty = bitPacket.g1() == 1;
            if (UserDetail.lobbyLoyalty != loyalty || UserDetail.lobbyLoyaltyBalance != balance) {
                UserDetail.lobbyLoyaltyBalance = balance;
                UserDetail.lobbyLoyalty = loyalty;
                ScriptRunner.executeTrigger(ClientTriggerType.LOYALTY_UPDATED, -1, -1);
            }
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARP_SMALL) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(931) byte value = bitPacket.g1b_alt1();
            // g.trace("Received small varp variable: " + var18 + " value:" + var78);
            TimedVarDomain.instance.updateVarp(id, value);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.RESET_CLIENT_VARCACHE) {
            TimedVarDomain.instance.reset();
            Static635.varpUpdateCount += 32;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_QUICKCHAT_CLANCHANNEL) {
            @Pc(446) boolean affined = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(983) long idHi = bitPacket.g2();
            @Pc(988) long idLo = bitPacket.g3();
            @Pc(992) int rank = bitPacket.g1();
            @Pc(996) int quickchatId = bitPacket.g2();
            @Pc(1002) long id = (idHi << 32) + idLo;

            @Pc(1004) boolean blocked = false;
            @Pc(1013) ClanChannel channel = affined ? ClanChannel.affined : ClanChannel.listened;
            if (channel == null) {
                blocked = true;
            } else {
                label2238:
                {
                    for (@Pc(1021) int i = 0; i < 100; i++) {
                        if (Static511.aLongArray17[i] == id) {
                            blocked = true;
                            break label2238;
                        }
                    }

                    if (rank <= 1 && IgnoreList.contains(name)) {
                        blocked = true;
                    }
                }
            }

            if (!blocked && Static659.blockChat == 0) {
                Static511.aLongArray17[Static97.anInt2001] = id;
                Static97.anInt2001 = (Static97.anInt2001 + 1) % 100;
                @Pc(1090) String message = QuickChatPhraseTypeList.instance.get(quickchatId).decodeText(bitPacket);
                @Pc(1097) int type = affined ? ChatLineType.QUICKCHAT_CLANCHANNEL_AFFINED : ChatLineType.QUICKCHAT_CLANCHANNEL_UNAFFINED;

                if (rank == 2 || rank == 3) {
                    ChatHistory.add(type, 0, "<img=1>" + name, "<img=1>" + name, name, channel.clanName, quickchatId, message);
                } else if (rank == 1) {
                    ChatHistory.add(type, 0, "<img=0>" + name, "<img=0>" + name, name, channel.clanName, quickchatId, message);
                } else {
                    ChatHistory.add(type, 0, name, name, name, channel.clanName, quickchatId, message);
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.RESET_ANIMS) {
            for (@Pc(277) int i = 0; i < PlayerList.highResolutionPlayers.length; i++) {
                if (PlayerList.highResolutionPlayers[i] != null) {
                    PlayerList.highResolutionPlayers[i].actionAnimations = null;
                    PlayerList.highResolutionPlayers[i].actionAnimator.update(true, -1);
                }
            }

            for (@Pc(100) int i = 0; i < NPCList.newSize; i++) {
                NPCList.entities[i].npc.actionAnimations = null;
                NPCList.entities[i].npc.actionAnimator.update(true, -1);
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARCLAN_INIT) {
            Static279.clanVars = new Object[VarClanSettingTypeList.instance.num];
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.REDUCE_ATTACK_PRIORITY) {
            Static324.reduceAttackPriority = bitPacket.g1_alt3() == 1;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MINIMAP_TOGGLE) {
            Minimap.toggle = bitPacket.g1();
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETMODEL) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt2();
            @Pc(100) int model = bitPacket.g2_alt2();
            if (model == 65535) {
                model = -1;
            }

            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModel(idAndSlot, Component.OBJ_TYPE_MODEL, model, -1);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.TEXT_COORD) {
            decodeZoneProt(ZoneProt.TEXT_COORD);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOGOUT) {
            LoginManager.logout(InterfaceManager.lobbyOpened);
            context.currentProt = null;
            return false;
        } else if (context.currentProt == ServerProt.UPDATE_FRIENDLIST) {
            while (bitPacket.pos < context.currentPacketSize) {
                @Pc(446) boolean useFormerName = bitPacket.g1() == 1;
                @Pc(627) String name = bitPacket.gjstr();
                @Pc(629) String formerName = bitPacket.gjstr();
                @Pc(1409) int world = bitPacket.g2();
                @Pc(1413) int rank = bitPacket.g1();
                @Pc(1425) boolean referred = bitPacket.g1() == 1;

                @Pc(1427) String worldName = "";
                @Pc(1429) boolean sameGame = false;
                if (world > 0) {
                    worldName = bitPacket.gjstr();
                    sameGame = bitPacket.g1() == 1;
                }

                for (@Pc(1449) int i = 0; i < FriendsList.count; i++) {
                    if (useFormerName) {
                        if (formerName.equals(FriendsList.names[i])) {
                            FriendsList.names[i] = name;
                            name = null;
                            FriendsList.formerNames[i] = formerName;
                            break;
                        }
                    } else if (name.equals(FriendsList.names[i])) {
                        if (FriendsList.worlds[i] != world) {
                            @Pc(1491) boolean notify = true;

                            for (@Pc(1496) FriendNotification notification = (FriendNotification) FriendsList.notifications.first(); notification != null; notification = (FriendNotification) FriendsList.notifications.next()) {
                                if (notification.name.equals(name)) {
                                    if (world != 0 && notification.world == 0) {
                                        notify = false;
                                        notification.unlink();
                                    } else if (world == 0 && notification.world != 0) {
                                        notify = false;
                                        notification.unlink();
                                    }
                                }
                            }

                            if (notify) {
                                FriendsList.notifications.add(new FriendNotification(name, world));
                            }

                            FriendsList.worlds[i] = world;
                        }

                        FriendsList.formerNames[i] = formerName;
                        FriendsList.worldNames[i] = worldName;
                        FriendsList.ranks[i] = rank;
                        FriendsList.sameGameFlags[i] = sameGame;
                        name = null;
                        FriendsList.referredFlags[i] = referred;
                        break;
                    }
                }

                if (name != null && FriendsList.count < 200) {
                    FriendsList.names[FriendsList.count] = name;
                    FriendsList.formerNames[FriendsList.count] = formerName;
                    FriendsList.worlds[FriendsList.count] = world;
                    FriendsList.worldNames[FriendsList.count] = worldName;
                    FriendsList.ranks[FriendsList.count] = rank;
                    FriendsList.sameGameFlags[FriendsList.count] = sameGame;
                    FriendsList.referredFlags[FriendsList.count] = referred;
                    FriendsList.count++;
                }
            }

            FriendsList.lastTransmit = World.tick;
            FriendsList.status = 2;

            @Pc(100) int end = FriendsList.count;
            while (end > 0) {
                @Pc(226) boolean sorted = true;

                end--;

                for (@Pc(526) int i = 0; i < end; i++) {
                    @Pc(1665) boolean swap = false;

                    if (FriendsList.worlds[i] != ConnectionInfo.login.world && FriendsList.worlds[i + 1] == ConnectionInfo.login.world) {
                        swap = true;
                    }
                    if (!swap && FriendsList.worlds[i] == 0 && FriendsList.worlds[i + 1] != 0) {
                        swap = true;
                    }
                    if (!swap && !FriendsList.referredFlags[i] && FriendsList.referredFlags[i + 1]) {
                        swap = true;
                    }

                    if (swap) {
                        @Pc(1413) int worlds = FriendsList.worlds[i];
                        FriendsList.worlds[i] = FriendsList.worlds[i + 1];
                        FriendsList.worlds[i + 1] = worlds;

                        @Pc(1750) String worldNames = FriendsList.worldNames[i];
                        FriendsList.worldNames[i] = FriendsList.worldNames[i + 1];
                        FriendsList.worldNames[i + 1] = worldNames;

                        @Pc(1427) String names = FriendsList.names[i];
                        FriendsList.names[i] = FriendsList.names[i + 1];
                        FriendsList.names[i + 1] = names;

                        @Pc(1786) String formerNames = FriendsList.formerNames[i];
                        FriendsList.formerNames[i] = FriendsList.formerNames[i + 1];
                        FriendsList.formerNames[i + 1] = formerNames;

                        @Pc(1449) int ranks = FriendsList.ranks[i];
                        FriendsList.ranks[i] = FriendsList.ranks[i + 1];
                        FriendsList.ranks[i + 1] = ranks;

                        @Pc(1491) boolean sameGame = FriendsList.sameGameFlags[i];
                        FriendsList.sameGameFlags[i] = FriendsList.sameGameFlags[i + 1];
                        FriendsList.sameGameFlags[i + 1] = sameGame;

                        @Pc(1004) boolean referred = FriendsList.referredFlags[i];
                        FriendsList.referredFlags[i] = FriendsList.referredFlags[i + 1];
                        FriendsList.referredFlags[i + 1] = referred;

                        sorted = false;
                    }
                }

                if (sorted) {
                    break;
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SOUND_MIXBUSS_SETLEVEL) {
            @Pc(277) int level = bitPacket.g1_alt3();
            @Pc(931) byte channel = bitPacket.g1b_alt3();
            VerifyId.incrementAndTransmit();
            Static711.mixBussSetLevel(channel, level);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARBIT_LARGE) {
            @Pc(277) int value = bitPacket.g4();
            @Pc(100) int id = bitPacket.g2();
            // g.trace("Received big varbit variable: " + var18 + " value:" + var4);
            TimedVarDomain.instance.updateVarBitValue(id, value);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_QUICKCHAT_PRIVATE_ECHO) {
            @Pc(1937) String name = bitPacket.gjstr();
            @Pc(100) int quickchatId = bitPacket.g2();
            @Pc(629) String message = QuickChatPhraseTypeList.instance.get(quickchatId).decodeText(bitPacket);
            ChatHistory.add(ChatLineType.QUICKCHAT_PRIVATE_ECHO, 0, name, name, name, null, quickchatId, message);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VORBIS_SOUND) {
            @Pc(277) int id = bitPacket.g2();
            if (id == 65535) {
                id = -1;
            }
            @Pc(100) int loops = bitPacket.g1();
            @Pc(526) int delay = bitPacket.g2();
            @Pc(1409) int volume = bitPacket.g1();
            @Pc(1413) int rate = bitPacket.g2();
            SoundManager.playVorbisSound(id, loops, delay, volume, rate, false);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SERVER_PROT_141) {
            @Pc(277) int id = bitPacket.ig2();
            VerifyId.incrementAndTransmit();
            VideoTypeList.method9267(id);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETTEXTFONT) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt2();
            @Pc(100) int font = bitPacket.g2_alt3();
            if (font == 65535) {
                font = -1;
            }
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetTextFont(idAndSlot, font);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_INV_FULL) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(100) int flags = bitPacket.g1();
            @Pc(2080) boolean otherPlayer = (flags & 0x1) == 1;
            // // g.trace("Received full inventory for inventory: " + var18 + " other player?: " + var31);

            ClientInventory.empty(id, otherPlayer);

            @Pc(1409) int size = bitPacket.g2();
            for (@Pc(1413) int slot = 0; slot < size; slot++) {
                @Pc(2098) int count = bitPacket.g1();
                if (count == 255) {
                    count = bitPacket.g4();
                }
                @Pc(992) int objId = bitPacket.ig2();
                ClientInventory.setSlot(otherPlayer, count, slot, objId - 1, id);
            }

            ClientInventory.updates[ClientInventory.updateCount++ & 0x1F] = id;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CHANGE_LOBBY) {
            @Pc(277) int ip = bitPacket.g4();
            Static439.hostnameResource = GameShell.signLink.lookupHostname(ip);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CLIENT_SETVARC_LARGE) {
            @Pc(277) int id = bitPacket.ig2();
            @Pc(100) int value = bitPacket.g4();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.setVarc(id, value);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETPLAYERHEAD_IGNOREWORN) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt3();
            @Pc(100) int kit3 = bitPacket.g2();
            @Pc(526) int kit2 = bitPacket.g2_alt3();
            @Pc(1409) int kit1 = bitPacket.g2_alt2();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModel(idAndSlot, Component.OBJ_TYPE_PLAYERHEAD_IGNOREWORN, (kit1 << 16) | kit2, kit3);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETPLAYERMODEL_SELF) {
            @Pc(277) int idAndSlot = bitPacket.g4();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModel(idAndSlot, Component.OBJ_TYPE_PLAYERMODEL, PlayerList.activePlayerSlot, 0);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOGOUT_FULL) {
            LoginManager.logout(false);
            context.currentProt = null;
            return false;
        } else if (context.currentProt == ServerProt.REBUILD_REGION) {
            RebuildRegion.rebuildRegion();
            context.currentProt = null;
            return false;
        } else if (context.currentProt == ServerProt.UPDATE_INV_PARTIAL) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(100) int flags = bitPacket.g1();
            @Pc(2080) boolean otherPlayer = (flags & 0x1) == 1;

            while (context.currentPacketSize > bitPacket.pos) {
                @Pc(1409) int slot = bitPacket.gsmart();
                @Pc(1413) int objId = bitPacket.g2();
                @Pc(2098) int count = 0;

                if (objId != 0) {
                    count = bitPacket.g1();

                    if (count == 255) {
                        count = bitPacket.g4();
                    }
                }

                ClientInventory.setSlot(otherPlayer, count, slot, objId - 1, id);
            }

            ClientInventory.updates[ClientInventory.updateCount++ & 0x1F] = id;
            context.currentProt = null;
            return true;
        } else if (ServerProt.RUNCLIENTSCRIPT == context.currentProt) {
            @Pc(1937) String string = bitPacket.gjstr();

            @Pc(2379) Object[] arguments = new Object[string.length() + 1];
            for (@Pc(526) int i = string.length() - 1; i >= 0; i--) {
                if (string.charAt(i) == 's') {
                    arguments[i + 1] = bitPacket.gjstr();
                } else {
                    arguments[i + 1] = Integer.valueOf(bitPacket.g4());
                }
            }

            arguments[0] = Integer.valueOf(bitPacket.g4());
            VerifyId.incrementAndTransmit();

            @Pc(2442) HookRequest hook = new HookRequest();
            hook.arguments = arguments;
            ScriptRunner.executeHookInner(hook);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETGRAPHIC) {
            @Pc(277) int graphic = bitPacket.ig2();
            @Pc(100) int idAndSlot = bitPacket.g4();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetGraphic(idAndSlot, graphic);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_RUNWEIGHT) {
            Static494.runWeight = bitPacket.g2s();
            context.currentProt = null;
            Static321.lastMiscTransmit = World.tick;
            return true;
        } else if (ServerProt.IF_OPENTOP == context.currentProt) {
            @Pc(277) int id = bitPacket.g2_alt3();
            @Pc(100) int type = bitPacket.g1_alt3();
            VerifyId.incrementAndTransmit();
            if (type == 2) {
                WorldMap.close();
            }
            InterfaceManager.topLevelInterface = id;
            InterfaceManager.restartInterfaceAnims(id);
            InterfaceManager.refreshTopLevelInterface(false);
            ScriptRunner.executeOnLoad(InterfaceManager.topLevelInterface);
            for (@Pc(526) int i = 0; i < 100; i++) {
                InterfaceManager.dirtyRectangles[i] = true;
            }
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_SITESETTINGS) {
            Static708.updateSiteSettings(bitPacket.gjstr());
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.NPC_ANIM_SPECIFIC) {
            @Pc(277) int delay = bitPacket.g1_alt2();
            @Pc(2579) int[] animations = new int[4];
            for (@Pc(526) int i = 0; i < 4; i++) {
                animations[i] = bitPacket.g2_alt3();
            }

            @Pc(1409) int id = bitPacket.g2_alt2();
            @Pc(2608) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(id);
            if (node != null) {
                PathingEntity.animate(animations, delay, true, node.npc);
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_REBOOT_TIMER) {
            if (MainLogicStep.isAtLobbyScreen(MainLogicManager.step)) {
                Static249.rebootTimer = (int) ((float) bitPacket.g2() * 2.5F);
            } else {
                Static249.rebootTimer = bitPacket.g2() * 30;
            }

            context.currentProt = null;
            Static321.lastMiscTransmit = World.tick;
            return true;
        } else if (context.currentProt == ServerProt.SERVER_PROT_100) {
            Static486.aByte115 = bitPacket.g1b();
            context.currentProt = null;
            if (Static486.aByte115 == 0 || Static486.aByte115 == 1) {
                Static587.aBoolean663 = true;
            }
            return true;
        } else if (context.currentProt == ServerProt.VARP_LARGE) {
            @Pc(277) int value = bitPacket.g4_alt3();
            @Pc(100) int id = bitPacket.g2_alt2();
            // g.trace("Received big varp variable: " + var18 + " value:" + var4);
            TimedVarDomain.instance.updateVarp(id, value);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CLANCHANNEL_DELTA) {
            Static39.lastClanChannelTransmit = World.tick;
            @Pc(446) boolean affined = bitPacket.g1() == 1;
            @Pc(2736) ClanChannelDelta delta = new ClanChannelDelta(bitPacket);
            @Pc(2740) ClanChannel channel;
            if (affined) {
                channel = ClanChannel.affined;
            } else {
                channel = ClanChannel.listened;
            }
            delta.applyToClanChannel(channel);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETCOLOUR) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt2();
            @Pc(100) int colour = bitPacket.g2();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetColour(idAndSlot, colour);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SERVER_PROT_92) {
            @Pc(277) int coord = bitPacket.g4_alt1();
            VerifyId.incrementAndTransmit();

            if (coord == -1) {
                Camera.anInt10383 = -1;
                Camera.anInt10376 = -1;
            } else {
                @Pc(100) int x = (coord >> 14) & 0x3FFF;
                @Pc(526) int z = coord & 0x3FFF;

                x -= WorldMap.areaBaseX;
                if (x < 0) {
                    x = 0;
                } else if (Static720.mapWidth <= x) {
                    x = Static720.mapWidth;
                }
                z -= WorldMap.areaBaseZ;

                Camera.anInt10376 = (x << 9) + 256;
                if (z < 0) {
                    z = 0;
                } else if (z >= Static501.mapLength) {
                    z = Static501.mapLength;
                }
                Camera.anInt10383 = (z << 9) + 256;
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VORBIS_SPEECH_STOP) {
            SoundManager.stopVorbisSpeech();
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARCLAN_BYTE) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(931) byte value = bitPacket.g1b();
            if (Static279.clanVars == null) {
                Static279.clanVars = new Object[VarClanSettingTypeList.instance.num];
            }
            Static279.clanVars[id] = Integer.valueOf(value);
            Static265.varclanUpdates[Static710.varclanUpdateCount++ & 0x1F] = id;
            context.currentProt = null;
            return true;
        } else if (ServerProt.CUTSCENE == context.currentProt) {
            @Pc(277) int id = bitPacket.g2();
            CutsceneManager.clock = -1;
            CutsceneManager.id = id;
            CutsceneManager.state = 1;
            js5.CUTSCENES.fileready(CutsceneManager.id);

            @Pc(100) int count = bitPacket.g2();
            CutsceneManager.anIntArrayArray265 = new int[count][4];
            for (@Pc(526) int i = 0; i < count; i++) {
                for (@Pc(1409) int j = 0; j < 4; j++) {
                    CutsceneManager.anIntArrayArray265[i][j] = bitPacket.g4();
                }
            }

            @Pc(1409) int size = bitPacket.g1();
            CutsceneManager.packet = new Packet(size);
            CutsceneManager.packet.pdata(size, bitPacket.data, bitPacket.pos);
            bitPacket.pos += size;
            context.currentProt = null;
            return false;
        } else if (context.currentProt == ServerProt.URL_OPEN) {
            if (GameShell.fsframe != null) {
                InterfaceManager.changeWindowMode(ClientOptions.instance.screenSizeDefault.getValue(), -1, -1, false);
            }
            @Pc(3044) byte[] data = new byte[context.currentPacketSize];
            bitPacket.readEncrypted(data, context.currentPacketSize);
            @Pc(627) String url = Cp1252.decode(0, data, context.currentPacketSize);
            Static664.openjs(ClientOptions.instance.toolkit.getValue() == ToolkitType.GL, url, true, GameShell.signLink);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_QUICKCHAT_PRIVATE) {
            @Pc(446) boolean filtered = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(629) String nameUnfiltered = name;
            if (filtered) {
                nameUnfiltered = bitPacket.gjstr();
            }

            @Pc(639) long idHi = bitPacket.g2();
            @Pc(644) long idLo = bitPacket.g3();
            @Pc(996) int rank = bitPacket.g1();
            @Pc(1449) int quickChatId = bitPacket.g2();
            @Pc(3134) long id = (idHi << 32) + idLo;
            @Pc(3136) boolean blocked = false;

            @Pc(1021) int i = 0;
            while (true) {
                if (i >= 100) {
                    if (rank <= 1 && IgnoreList.contains(nameUnfiltered)) {
                        blocked = true;
                    }
                    break;
                }
                if (id == Static511.aLongArray17[i]) {
                    blocked = true;
                    break;
                }
                i++;
            }

            if (!blocked && Static659.blockChat == 0) {
                Static511.aLongArray17[Static97.anInt2001] = id;
                Static97.anInt2001 = (Static97.anInt2001 + 1) % 100;
                @Pc(1090) String message = QuickChatPhraseTypeList.instance.get(quickChatId).decodeText(bitPacket);

                if (rank == 2) {
                    ChatHistory.add(ChatLineType.QUICKCHAT_PRIVATE, 0, "<img=1>" + name, "<img=1>" + nameUnfiltered, name, null, quickChatId, message);
                } else if (rank == 1) {
                    ChatHistory.add(ChatLineType.QUICKCHAT_PRIVATE, 0, "<img=0>" + name, "<img=0>" + nameUnfiltered, name, null, quickChatId, message);
                } else {
                    ChatHistory.add(ChatLineType.QUICKCHAT_PRIVATE, 0, name, nameUnfiltered, name, null, quickChatId, message);
                }
            }

            context.currentProt = null;
            return true;
        } else if (ServerProt.VARBIT_SMALL == context.currentProt) {
            @Pc(277) int value = bitPacket.g1_alt3();
            @Pc(100) int id = bitPacket.g2_alt2();
            // g.trace("Received small varbit variable: " + var18 + " value:" + var4);
            TimedVarDomain.instance.updateVarBitValue(id, value);
            context.currentProt = null;
            return true;
        } else if (ServerProt.UPDATE_STAT == context.currentProt) {
            @Pc(277) int level = bitPacket.g1_alt3();
            @Pc(100) int skill = bitPacket.g1_alt1();
            @Pc(526) int xp = bitPacket.g4_alt1();
            Static237.statXps[skill] = xp;
            Static581.statLevels[skill] = level;
            Static498.statBaseLevels[skill] = 1;

            @Pc(1409) int maxLevel = Static245.MAX_LEVELS[skill] - 1;
            for (@Pc(1413) int i = 0; i < maxLevel; i++) {
                if (Static293.XP_TABLE[i] <= xp) {
                    Static498.statBaseLevels[skill] = i + 2;
                }
            }

            Static395.statUpdates[Static366.statUpdateCount++ & 0x1F] = skill;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CHAT_FILTER_SETTINGS_PRIVATECHAT) {
            Static726.privateChatMode = PrivateChatMode.fromId(bitPacket.g1());
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETRECOL) {
            @Pc(277) int index = bitPacket.g1_alt3();
            @Pc(100) int idAndSlot = bitPacket.g4();
            @Pc(526) int source = bitPacket.ig2();
            @Pc(1409) int destination = bitPacket.ig2();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetRecol(idAndSlot, index, source, destination);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_CLANCHANNEL) {
            @Pc(446) boolean affined = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(983) long idHi = bitPacket.g2();
            @Pc(988) long idLo = bitPacket.g3();
            @Pc(992) int rank = bitPacket.g1();
            @Pc(649) long id = (idHi << 32) + idLo;

            @Pc(1491) boolean blocked = false;
            @Pc(3494) ClanChannel channel = affined ? ClanChannel.affined : ClanChannel.listened;
            if (channel == null) {
                blocked = true;
            } else {
                label2266:
                {
                    for (@Pc(3502) int i = 0; i < 100; i++) {
                        if (id == Static511.aLongArray17[i]) {
                            blocked = true;
                            break label2266;
                        }
                    }

                    if (rank <= 1) {
                        if ((UserDetail.underage && !UserDetail.parentalChatConsent) || Static617.quickChatWorld) {
                            blocked = true;
                        } else if (IgnoreList.contains(name)) {
                            blocked = true;
                        }
                    }
                }
            }

            if (!blocked && Static659.blockChat == 0) {
                Static511.aLongArray17[Static97.anInt2001] = id;
                Static97.anInt2001 = (Static97.anInt2001 + 1) % 100;
                @Pc(3582) String message = StringTools.escapeBrackets(WordPack.decode(bitPacket));
                @Pc(1021) int type = affined ? ChatLineType.CLANCHANNEL_AFFINED : ChatLineType.CLANCHANNEL_UNAFFINED;

                if (rank == 2 || rank == 3) {
                    ChatHistory.add(type, 0, "<img=1>" + name, "<img=1>" + name, name, channel.clanName, -1, message);
                } else if (rank == 1) {
                    ChatHistory.add(type, 0, "<img=0>" + name, "<img=0>" + name, name, channel.clanName, -1, message);
                } else {
                    ChatHistory.add(type, 0, name, name, name, channel.clanName, -1, message);
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.HINT_ARROW) {
            @Pc(277) int data = bitPacket.g1();
            @Pc(100) int index = data >> 5;
            @Pc(526) int type = data & 0x1F;
            if (type == HintArrowType.CLEAR) {
                Static527.hintArrows[index] = null;
                context.currentProt = null;
                return true;
            }

            @Pc(3721) HintArrow hintArrow = new HintArrow();
            hintArrow.type = type;
            hintArrow.sprite = bitPacket.g1();

            if (hintArrow.sprite >= 0 && hintArrow.sprite < Sprites.hintHeadicons.length) {
                if (hintArrow.type == HintArrowType.NPC || hintArrow.type == HintArrowType.PLAYER) {
                    hintArrow.entity = bitPacket.g2();
                    hintArrow.flashRate = bitPacket.g2();
                    bitPacket.pos += 4;
                } else if (hintArrow.type >= HintArrowType.TILE_CENTRE && hintArrow.type <= HintArrowType.TILE_NORTH) {
                    if (hintArrow.type == HintArrowType.TILE_CENTRE) {
                        hintArrow.z = 256;
                        hintArrow.x = 256;
                    }
                    if (hintArrow.type == HintArrowType.TILE_WEST) {
                        hintArrow.x = 0;
                        hintArrow.z = 256;
                    }
                    if (hintArrow.type == HintArrowType.TILE_EAST) {
                        hintArrow.x = 512;
                        hintArrow.z = 256;
                    }
                    if (hintArrow.type == HintArrowType.TILE_SOUTH) {
                        hintArrow.z = 0;
                        hintArrow.x = 256;
                    }
                    if (hintArrow.type == HintArrowType.TILE_NORTH) {
                        hintArrow.z = 512;
                        hintArrow.x = 256;
                    }

                    hintArrow.type = 2;
                    hintArrow.level = bitPacket.g1();
                    hintArrow.x += bitPacket.g2() - WorldMap.areaBaseX << 9;
                    hintArrow.z += bitPacket.g2() - WorldMap.areaBaseZ << 9;
                    hintArrow.y = bitPacket.g1() << 2;
                    hintArrow.drawDistance = bitPacket.g2();
                }

                hintArrow.model = bitPacket.g2();
                if (hintArrow.model == 65535) {
                    hintArrow.model = -1;
                }

                Static527.hintArrows[index] = hintArrow;
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SETDRAWORDER) {
            @Pc(277) int drawOrder = bitPacket.g1_alt3();
            VerifyId.incrementAndTransmit();
            context.currentProt = null;
            Static150.drawOrder = drawOrder;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_UID192) {
            bitPacket.pos += 28;
            if (bitPacket.checkcrc()) {
                Static83.writeUid(bitPacket.pos - 28, bitPacket);
            }
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETOBJECT) {
            @Pc(277) int objId = bitPacket.ig2();
            if (objId == 65535) {
                objId = -1;
            }
            @Pc(100) int count = bitPacket.g4();
            @Pc(526) int idAndSlot = bitPacket.g4_alt3();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetObject(idAndSlot, count, objId);
            @Pc(4005) ObjType objType = ObjTypeList.instance.list(objId);
            DelayedStateChange.interfaceSetModelAngle(idAndSlot, objType.xan2d, objType.yan2d, objType.zoom2d);
            DelayedStateChange.interfaceSetModelOffset(idAndSlot, objType.xof2d, objType.yof2d, objType.zan2d);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SERVER_PROT_113) {
            @Pc(277) int volume = bitPacket.g1_alt3();
            @Pc(100) int id = bitPacket.g2();
            VerifyId.incrementAndTransmit();
            VideoTypeList.method6802(true, id, volume);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SHOW_FACE_HERE) {
            @Pc(446) boolean showFaceHere = bitPacket.g1_alt2() == 1;
            VerifyId.incrementAndTransmit();
            MiniMenu.showFaceHere = showFaceHere;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_STOCKMARKET_SLOT) {
            @Pc(277) int slot = bitPacket.g1();
            if (bitPacket.g1() == 0) {
                StockmarketManager.offers[slot] = new StockmarketOffer();
            } else {
                bitPacket.pos--;
                StockmarketManager.offers[slot] = new StockmarketOffer(bitPacket);
            }

            StockmarketManager.lastTransmit = World.tick;
            context.currentProt = null;
            return true;
        } else if (ServerProt.VIDEO_STOP == context.currentProt) {
            @Pc(277) int id = bitPacket.g2_alt3();
            VerifyId.incrementAndTransmit();
            VideoManager.stop(id);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MAP_ANIM) {
            decodeZoneProt(ZoneProt.MAP_ANIM);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_GAME) {
            @Pc(277) int type = bitPacket.gsmart();
            @Pc(100) int flags = bitPacket.g4();
            @Pc(526) int data = bitPacket.g1();

            @Pc(4175) String name = "";
            @Pc(4177) String nameUnfiltered = name;
            if ((data & 0x1) != 0) {
                name = bitPacket.gjstr();

                if ((data & 0x2) != 0) {
                    nameUnfiltered = bitPacket.gjstr();
                } else {
                    nameUnfiltered = name;
                }
            }

            @Pc(1750) String message = bitPacket.gjstr();
            if (type == ChatLineType.CONSOLE_PRINT) {
                debugconsole.addline(message);
            } else if (type == ChatLineType.CONSOLE_SET) {
                debugconsole.set(message);
            } else if (nameUnfiltered.equals("") || !IgnoreList.contains(nameUnfiltered)) {
                ChatHistory.add(type, flags, name, nameUnfiltered, name, message);
            } else {
                context.currentProt = null;
                return true;
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARCLAN_CLEAR) {
            Static279.clanVars = null;
            context.currentProt = null;
            return true;
        } else if (ServerProt.CLIENT_SETVARCSTR_SMALL == context.currentProt) {
            @Pc(277) int id = bitPacket.g2_alt3();
            @Pc(627) String value = bitPacket.gjstr();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.setVarcstr(id, value);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CAM_SHAKE) {
            @Pc(277) int frequency = bitPacket.g1_alt3();
            @Pc(100) int index = bitPacket.g1_alt1();
            @Pc(526) int time = bitPacket.g2_alt3();
            @Pc(1409) int center = bitPacket.g1_alt3();
            @Pc(1413) int amplitude = bitPacket.g1_alt3();
            VerifyId.incrementAndTransmit();
            Shake.enabled[index] = true;
            Shake.center[index] = center;
            Shake.amplitude[index] = amplitude;
            Shake.frequency[index] = frequency;
            Shake.time[index] = time;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SOUND_AREA) {
            decodeZoneProt(ZoneProt.SOUND_AREA);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_IGNORELIST) {
            IgnoreList.count = bitPacket.g1();

            for (@Pc(277) int i = 0; i < IgnoreList.count; i++) {
                IgnoreList.names[i] = bitPacket.gjstr();
                IgnoreList.namesUnfiltered[i] = bitPacket.gjstr();
                if (IgnoreList.namesUnfiltered[i].equals("")) {
                    IgnoreList.namesUnfiltered[i] = IgnoreList.names[i];
                }

                IgnoreList.formerNames[i] = bitPacket.gjstr();
                IgnoreList.formerNamesUnfiltered[i] = bitPacket.gjstr();
                if (IgnoreList.formerNamesUnfiltered[i].equals("")) {
                    IgnoreList.formerNamesUnfiltered[i] = IgnoreList.formerNames[i];
                }

                IgnoreList.temporary[i] = false;
            }

            FriendsList.lastTransmit = World.tick;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOC_CUSTOMISE) {
            decodeZoneProt(ZoneProt.LOC_CUSTOMISE);
            context.currentProt = null;
            return true;
        } else if (ServerProt.SET_PLAYER_OP == context.currentProt) {
            @Pc(277) int reducedPriority = bitPacket.g1_alt1();
            @Pc(100) int cursor = bitPacket.ig2();
            if (cursor == 65535) {
                cursor = -1;
            }
            @Pc(629) String text = bitPacket.gjstr();
            @Pc(1409) int op = bitPacket.g1_alt2();
            if (op >= 1 && op <= 8) {
                if (text.equalsIgnoreCase("null")) {
                    text = null;
                }
                MiniMenu.playerOps[op - 1] = text;
                MiniMenu.playerOpCursors[op - 1] = cursor;
                MiniMenu.playerOpsReducedPriority[op - 1] = reducedPriority == 0;
            }
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETTARGETPARAM) {
            @Pc(277) int start = bitPacket.g2_alt2();
            if (start == 65535) {
                start = -1;
            }

            @Pc(100) int idAndSlot = bitPacket.g4_alt3();

            @Pc(526) int end = bitPacket.g2();
            if (end == 65535) {
                end = -1;
            }

            @Pc(1409) int targetParam = bitPacket.ig2();
            VerifyId.incrementAndTransmit();

            for (@Pc(1413) int component = start; component <= end; component++) {
                @Pc(644) long key = ((long) idAndSlot << 32) + (long) component;
                @Pc(4597) ServerActiveProperties oldProperties = (ServerActiveProperties) InterfaceManager.serverActiveProperties.get(key);
                @Pc(4611) ServerActiveProperties newProperties;

                if (oldProperties != null) {
                    newProperties = new ServerActiveProperties(oldProperties.events, targetParam);
                    oldProperties.unlink();
                } else if (component == -1) {
                    newProperties = new ServerActiveProperties(InterfaceList.list(idAndSlot).serverActiveProperties.events, targetParam);
                } else {
                    newProperties = new ServerActiveProperties(0, targetParam);
                }

                InterfaceManager.serverActiveProperties.put(key, newProperties);
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARCLAN_LONG) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(4669) long value = bitPacket.g8();
            if (Static279.clanVars == null) {
                Static279.clanVars = new Object[VarClanSettingTypeList.instance.num];
            }
            Static279.clanVars[id] = Long.valueOf(value);
            Static265.varclanUpdates[Static710.varclanUpdateCount++ & 0x1F] = id;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SERVER_PROT_115) {
            @Pc(277) int valueLow = bitPacket.ig2();
            @Pc(100) int valueHigh = bitPacket.g2_alt3();
            @Pc(526) int idAndSlot = bitPacket.g4();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.method4347(idAndSlot, (valueHigh << 16) + valueLow);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOC_PREFETCH) {
            decodeZoneProt(ZoneProt.LOC_PREFETCH);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SPOTANIM_SPECIFIC) {
            @Pc(277) int delay = bitPacket.g2_alt2();
            @Pc(100) int position = bitPacket.g4_alt3();
            @Pc(526) int index = bitPacket.g1_alt1();
            @Pc(1409) int wornSlotAndRotation = bitPacket.g1_alt1();
            @Pc(1413) int height = bitPacket.g2();

            @Pc(2098) int id = bitPacket.ig2();
            if (id == 65535) {
                id = -1;
            }

            @Pc(992) int rotation = wornSlotAndRotation & 0x7;

            @Pc(996) int wornSlot = (wornSlotAndRotation >> 3) & 0xF;
            if (wornSlot == 15) {
                wornSlot = -1;
            }

            @Pc(4806) boolean multipleAnims = (wornSlotAndRotation >> 7 & 0x1) == 1;
            if ((position >> 30) != 0) {
                @Pc(653) int level = (position >> 28) & 0x3;
                @Pc(657) int localX = (position >> 14 & 0x3FFF) - WorldMap.areaBaseX;
                @Pc(3502) int localZ = (position & 0x3FFF) - WorldMap.areaBaseZ;

                if (localX >= 0 && localZ >= 0 && localX < Static720.mapWidth && localZ < Static501.mapLength) {
                    if (id == -1) {
                        @Pc(5270) SpotAnimationNode node = (SpotAnimationNode) Static346.spotAnimations.get((localX << 16) | localZ);

                        if (node != null) {
                            node.spotAnimation.stopParticleSystem();
                            node.unlink();
                        }
                    } else {
                        @Pc(1021) int x = (localX * 512) + 256;
                        @Pc(1097) int z = (localZ * 512) + 256;

                        @Pc(667) int virtualLevel = level;
                        if (level < 3 && Static441.isBridgeAt(localZ, localX)) {
                            virtualLevel = level + 1;
                        }

                        @Pc(5334) SpotAnimation spotAnimation = new SpotAnimation(id, delay, level, virtualLevel, x, Static102.averageHeight(level, x, z) - height, z, localX, localX, localZ, localZ, rotation, multipleAnims);
                        Static346.spotAnimations.put((localX << 16) | localZ, new SpotAnimationNode(spotAnimation));
                    }
                }
            } else if (position >> 29 != 0) {
                @Pc(653) int npcIndex = position & 0xFFFF;
                @Pc(5032) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(npcIndex);

                if (node != null) {
                    @Pc(5037) NPCEntity npc = node.npc;
                    @Pc(5042) EntitySpotAnimation current = npc.spotAnims[index];
                    if (id == 65535) {
                        id = -1;
                    }

                    @Pc(665) boolean replace = true;
                    @Pc(667) int currentId = current.id;
                    if (id != -1 && currentId != -1) {
                        if (currentId == id) {
                            @Pc(4888) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);

                            if (type.loopSeq && type.seq != -1) {
                                @Pc(4905) SeqType seqType = SeqTypeList.instance.list(type.seq);
                                @Pc(5134) int replayMode = seqType.replayMode;

                                if (replayMode == SeqReplayMode.STOP || replayMode == SeqReplayMode.RESTART_LOOP) {
                                    replace = false;
                                } else if (replayMode == SeqReplayMode.RESET) {
                                    replace = true;
                                }
                            }
                        } else {
                            @Pc(4888) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);
                            @Pc(5078) SpotAnimationType currentType = SpotAnimationTypeList.instance.list(currentId);

                            if (type.seq != -1 && currentType.seq != -1) {
                                @Pc(4911) SeqType seqType = SeqTypeList.instance.list(type.seq);
                                @Pc(5102) SeqType currentSeqType = SeqTypeList.instance.list(currentType.seq);

                                if (currentSeqType.priority > seqType.priority) {
                                    replace = false;
                                }
                            }
                        }
                    }

                    if (replace) {
                        current.wornSlot = wornSlot;
                        current.height = height;
                        current.id = id;

                        if (id == -1) {
                            current.animator.update(true, -1);
                        } else {
                            @Pc(4888) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);

                            @Pc(4943) int loopMode = type.loopSeq ? 0 : 2;
                            if (multipleAnims) {
                                loopMode = 1;
                            }

                            current.animator.update(type.seq, delay, loopMode, false);
                        }
                    }
                }
            } else if (position >> 28 != 0) {
                @Pc(653) int playerId = position & 0xFFFF;

                @Pc(4839) PlayerEntity player;
                if (playerId == PlayerList.activePlayerSlot) {
                    player = PlayerEntity.self;
                } else {
                    player = PlayerList.highResolutionPlayers[playerId];
                }

                if (player != null) {
                    @Pc(4850) EntitySpotAnimation current = player.spotAnims[index];
                    if (id == 0xFFFF) {
                        id = -1;
                    }

                    @Pc(4857) boolean replace = true;
                    @Pc(1097) int currentId = current.id;

                    if (id != -1 && currentId != -1) {
                        if (id == currentId) {
                            @Pc(4883) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);

                            if (type.loopSeq && type.seq != -1) {
                                @Pc(4940) SeqType seqType = SeqTypeList.instance.list(type.seq);
                                @Pc(4943) int replayMode = seqType.replayMode;

                                if (replayMode == SeqReplayMode.STOP || replayMode == SeqReplayMode.RESTART_LOOP) {
                                    replace = false;
                                } else if (replayMode == SeqReplayMode.RESET) {
                                    replace = true;
                                }
                            }
                        } else {
                            @Pc(4883) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);
                            @Pc(4888) SpotAnimationType currentType = SpotAnimationTypeList.instance.list(currentId);

                            if (type.seq != -1 && currentType.seq != -1) {
                                @Pc(4905) SeqType seqType = SeqTypeList.instance.list(type.seq);
                                @Pc(4911) SeqType currentSeqType = SeqTypeList.instance.list(currentType.seq);

                                if (currentSeqType.priority > seqType.priority) {
                                    replace = false;
                                }
                            }
                        }
                    }

                    if (replace) {
                        current.wornSlot = wornSlot;
                        current.height = height;
                        current.rotation = rotation;
                        current.id = id;

                        if (id == -1) {
                            current.animator.update(true, -1);
                        } else {
                            @Pc(4883) SpotAnimationType type = SpotAnimationTypeList.instance.list(id);

                            @Pc(5006) int loopMode = type.loopSeq ? 0 : 2;
                            if (multipleAnims) {
                                loopMode = 1;
                            }

                            current.animator.update(type.seq, delay, loopMode, false);
                        }
                    }
                }
            }
            context.currentProt = null;
            return true;
        } else if (ServerProt.IF_SETNPCHEAD == context.currentProt) {
            @Pc(277) int idAndSlot = bitPacket.g4();
            @Pc(100) int npc = bitPacket.ig2();
            if (npc == 65535) {
                npc = -1;
            }
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModel(idAndSlot, Component.OBJ_TYPE_NPCHEAD, npc, -1);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.REFLECTION_CHECKER) {
            Static480.runReflectionChecker(bitPacket, GameShell.signLink, context.currentPacketSize);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CAM_RESET) {
            VerifyId.incrementAndTransmit();
            Camera.reset();
            context.currentProt = null;
            return true;
        } else if (ServerProt.IF_MOVESUB == context.currentProt) {
            @Pc(277) int idAndSlot_d = bitPacket.g4_alt2();
            @Pc(100) int idAndSlot_s = bitPacket.g4_alt3();
            VerifyId.incrementAndTransmit();

            @Pc(5438) SubInterface sub_s = (SubInterface) InterfaceManager.subInterfaces.get(idAndSlot_s);
            @Pc(5445) SubInterface sub_d = (SubInterface) InterfaceManager.subInterfaces.get(idAndSlot_d);
            if (sub_d != null) {
                InterfaceManager.closeSubInterface(sub_d, sub_s == null || sub_s.id != sub_d.id, false);
            }
            if (sub_s != null) {
                sub_s.unlink();
                InterfaceManager.subInterfaces.put(idAndSlot_d, sub_s);
            }

            @Pc(5487) Component component = InterfaceList.list(idAndSlot_s);
            if (component != null) {
                InterfaceManager.redraw(component);
            }

            component = InterfaceList.list(idAndSlot_d);
            if (component != null) {
                InterfaceManager.redraw(component);
                InterfaceManager.calculateLayerDimensions(component, true);
            }

            if (InterfaceManager.topLevelInterface != -1) {
                InterfaceManager.runHookImmediate(InterfaceManager.IMMEDIATE_HOOK_TYPE_SUBCHANGE, InterfaceManager.topLevelInterface);
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_OPENSUB) {
            @Pc(277) int subId = bitPacket.g2_alt3();
            @Pc(100) int idAndSlot = bitPacket.g4_alt1();
            @Pc(526) int type = bitPacket.g1();
            VerifyId.incrementAndTransmit();

            @Pc(5445) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.get(idAndSlot);
            if (sub != null) {
                InterfaceManager.closeSubInterface(sub, sub.id != subId, false);
            }

            InterfaceManager.openSubInterface(type, subId, idAndSlot, false);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CAM_LOOKAT) {
            @Pc(277) int z = bitPacket.g1_alt2();
            @Pc(100) int y = bitPacket.g2_alt3() << 2;
            @Pc(526) int x = bitPacket.g1_alt3();
            @Pc(1409) int step = bitPacket.g1();
            @Pc(1413) int speed = bitPacket.g1_alt3();
            VerifyId.incrementAndTransmit();
            Camera.lookAt(x, y, z, step, speed);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_RUNENERGY) {
            Static703.runEnergy = bitPacket.g1();
            context.currentProt = null;
            Static321.lastMiscTransmit = World.tick;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETANGLE) {
            @Pc(277) int zoom2d = bitPacket.g2_alt2();
            @Pc(100) int yan2d = bitPacket.g2_alt3();
            @Pc(526) int idAndSlot = bitPacket.g4_alt1();
            @Pc(1409) int xan2d = bitPacket.g2_alt3();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModelAngle(idAndSlot, xan2d, yan2d, zoom2d);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CLIENT_SETVARCSTR_LARGE) {
            @Pc(1937) String value = bitPacket.gjstr();
            @Pc(100) int id = bitPacket.g2_alt2();
            // g.trace("client_setvarcstr (large) - var:" + var18 + " val:" + var26);
            VerifyId.incrementAndTransmit();
            DelayedStateChange.setVarcstr(id, value);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_PRIVATE) {
            @Pc(446) boolean filtered = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(629) String nameUnfiltered = name;
            if (filtered) {
                nameUnfiltered = bitPacket.gjstr();
            }
            @Pc(639) long idHi = bitPacket.g2();
            @Pc(644) long idLo = bitPacket.g3();
            @Pc(996) int rank = bitPacket.g1();
            @Pc(1002) long id = (idHi << 32) + idLo;

            @Pc(1004) boolean blocked = false;
            @Pc(3502) int i = 0;
            while (true) {
                if (i >= 100) {
                    if (rank <= 1) {
                        if ((UserDetail.underage && !UserDetail.parentalChatConsent) || Static617.quickChatWorld) {
                            blocked = true;
                        } else if (IgnoreList.contains(nameUnfiltered)) {
                            blocked = true;
                        }
                    }
                    break;
                }

                if (Static511.aLongArray17[i] == id) {
                    blocked = true;
                    break;
                }
                i++;
            }

            if (!blocked && Static659.blockChat == 0) {
                Static511.aLongArray17[Static97.anInt2001] = id;
                Static97.anInt2001 = (Static97.anInt2001 + 1) % 100;
                @Pc(3582) String message = StringTools.escapeBrackets(WordPack.decode(bitPacket));

                if (rank == 2) {
                    ChatHistory.add(ChatLineType.PRIVATE_RANK, 0, "<img=1>" + name, "<img=1>" + nameUnfiltered, name, null, -1, message);
                } else if (rank == 1) {
                    ChatHistory.add(ChatLineType.PRIVATE_RANK, 0, "<img=0>" + name, "<img=0>" + nameUnfiltered, name, null, -1, message);
                } else {
                    ChatHistory.add(ChatLineType.PRIVATE, 0, name, nameUnfiltered, name, null, -1, message);
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MIDI_SONG) {
            @Pc(277) int delay = bitPacket.g1_alt3();
            @Pc(100) int id = bitPacket.ig2();
            if (id == 65535) {
                id = -1;
            }
            @Pc(526) int volume = bitPacket.g1_alt2();
            SoundManager.playMidiSong(id, volume, delay);
            context.currentProt = null;
            return true;
        } else if (ServerProt.MIDI_JINGLE == context.currentProt) {
            @Pc(277) int id = bitPacket.g2_alt2();
            if (id == 65535) {
                id = -1;
            }
            @Pc(100) int delay = bitPacket.g3_alt1();
            @Pc(526) int volume = bitPacket.g1_alt2();
            SoundManager.playMidiJingle(id, delay, volume);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.JAVASCRIPT_RUN) {
            if (GameShell.fsframe != null) {
                InterfaceManager.changeWindowMode(ClientOptions.instance.screenSizeDefault.getValue(), -1, -1, false);
            }

            @Pc(3044) byte[] data = new byte[context.currentPacketSize];
            bitPacket.readEncrypted(data, context.currentPacketSize);
            @Pc(627) String path = Cp1252.decode(0, data, context.currentPacketSize);
            @Pc(629) String function = "opensn";
            if (!Client.js || ClientURLTools.openURL(GameShell.signLink, path, function, OpenUrlType.CALL).status == SignedResourceStatus.ERROR) {
                ClientURLTools.openURL(path, function, GameShell.signLink, ClientOptions.instance.toolkit.getValue() == ToolkitType.GL, true);
            }
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.DEBUG_SERVER_TRIGGERS) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(100) int start = bitPacket.g2();
            @Pc(526) int end = bitPacket.g2();
            VerifyId.incrementAndTransmit();

            if (InterfaceList.interfaces[id] != null) {
                for (@Pc(1409) int slot = start; slot < end; slot++) {
                    @Pc(1413) int triggers = bitPacket.g3();

                    if (InterfaceList.interfaces[id].length > slot && InterfaceList.interfaces[id][slot] != null) {
                        InterfaceList.interfaces[id][slot].serverTriggers = triggers;
                    }
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETPLAYERMODEL_OTHER) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt2();
            @Pc(100) int otherPlayer = bitPacket.g2();
            @Pc(526) int model = bitPacket.g4_alt3();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModel(idAndSlot, Component.OBJ_TYPE_PLAYERMODEL, otherPlayer, model);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SOUND_VORBIS_AREA) {
            decodeZoneProt(ZoneProt.SOUND_VORBIS_AREA);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VORBIS_SPEECH_SOUND) {
            @Pc(277) int id = bitPacket.g2();
            if (id == 65535) {
                id = -1;
            }
            @Pc(100) int volume = bitPacket.g1();
            @Pc(526) int range = bitPacket.g2();
            @Pc(1409) int loops = bitPacket.g1();
            SoundManager.playVorbisSound(id, volume, range, loops, 256, true);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CAM_SMOOTH_RESET) {
            VerifyId.incrementAndTransmit();
            Camera.smoothReset();
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.PLAYER_INFO) {
            PlayerList.iteratePlayers(bitPacket, context.currentPacketSize);
            context.currentProt = null;
            return true;
        } else if (ServerProt.CHAT_FILTER_SETTINGS == context.currentProt) {
            Static133.publicChatFilter = bitPacket.g1();
            Static87.tradeChatFilter = bitPacket.g1();
            context.currentProt = null;
            return true;
        } else if (ServerProt.UPDATE_ZONE_FULL_FOLLOWS == context.currentProt) {
            Static626.updateZoneX = bitPacket.g1b() << 3;
            Static270.updateZoneZ = bitPacket.g1b_alt3() << 3;
            Static87.updateZoneLevel = bitPacket.g1();

            for (@Pc(6277) ObjStack stack = (ObjStack) Static497.objStacks.first(); stack != null; stack = (ObjStack) Static497.objStacks.next()) {
                @Pc(100) int level = (int) ((stack.key >> 28) & 0x3L);
                @Pc(526) int x = (int) (stack.key & 0x3FFFL);
                @Pc(1409) int zoneX = x - WorldMap.areaBaseX;
                @Pc(1413) int z = (int) (stack.key >> 14 & 0x3FFFL);
                @Pc(2098) int zoneZ = z - WorldMap.areaBaseZ;

                if (Static87.updateZoneLevel == level && zoneX >= Static626.updateZoneX && zoneX < Static626.updateZoneX + 8 && zoneZ >= Static270.updateZoneZ && zoneZ < Static270.updateZoneZ + 8) {
                    stack.unlink();

                    if (zoneX >= 0 && zoneZ >= 0 && zoneX < Static720.mapWidth && zoneZ < Static501.mapLength) {
                        Static468.updateObjCount(Static87.updateZoneLevel, zoneX, zoneZ);
                    }
                }
            }

            @Pc(6385) ChangeLocationRequest request;
            for (request = (ChangeLocationRequest) Static159.changes.first(); request != null; request = (ChangeLocationRequest) Static159.changes.next()) {
                if (request.x >= Static626.updateZoneX && request.x < Static626.updateZoneX + 8 && request.z >= Static270.updateZoneZ && request.z < Static270.updateZoneZ + 8 && request.level == Static87.updateZoneLevel) {
                    request.pendingRemoval = true;
                }
            }

            for (request = (ChangeLocationRequest) Static227.customisations.first(); request != null; request = (ChangeLocationRequest) Static227.customisations.next()) {
                if (request.x >= Static626.updateZoneX && request.x < Static626.updateZoneX + 8 && request.z >= Static270.updateZoneZ && request.z < Static270.updateZoneZ + 8 && request.level == Static87.updateZoneLevel) {
                    request.pendingRemoval = true;
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETPLAYERHEAD) {
            @Pc(277) int idAndSlot = bitPacket.g4_alt1();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModel(idAndSlot, Component.OBJ_TYPE_PLAYERHEAD, -1, -1);
            context.currentProt = null;
            return true;
        } else if (ServerProt.MESSAGE_PLAYER_GROUP == context.currentProt) {
            @Pc(446) boolean filtered = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(629) String nameUnfiltered = name;
            if (filtered) {
                nameUnfiltered = bitPacket.gjstr();
            }
            @Pc(1409) int rank = bitPacket.g1();

            @Pc(6565) boolean blocked = false;
            if (rank <= 1) {
                if ((UserDetail.underage && !UserDetail.parentalChatConsent) || Static617.quickChatWorld) {
                    blocked = true;
                } else if (rank <= 1 && IgnoreList.contains(nameUnfiltered)) {
                    blocked = true;
                }
            }

            if (!blocked && Static659.blockChat == 0) {
                @Pc(1750) String message = StringTools.escapeBrackets(WordPack.decode(bitPacket));

                if (rank == 2) {
                    ChatHistory.add(ChatLineType.PLAYER_GROUP, 0, "<img=1>" + name, "<img=1>" + nameUnfiltered, name, null, -1, message);
                } else if (rank == 1) {
                    ChatHistory.add(ChatLineType.PLAYER_GROUP, 0, "<img=0>" + name, "<img=0>" + nameUnfiltered, name, null, -1, message);
                } else {
                    ChatHistory.add(ChatLineType.PLAYER_GROUP, 0, name, nameUnfiltered, name, null, -1, message);
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOGOUT_TRANSFER) {
            @Pc(277) int world = bitPacket.g2();
            @Pc(627) String address = bitPacket.gjstr();
            // g.trace("logout transfer to world " + var18 + " on " + var26);
            @Pc(2080) boolean reconnect = bitPacket.g1() == 1;
            LoginManager.reconnectToPrevious = reconnect;
            ConnectionInfo.previous = ConnectionInfo.login;
            client.connectTo(world, address);
            MainLogicManager.setStep(MainLogicStep.STEP_SWITCH_WORLD);
            context.currentProt = null;
            return false;
        } else if (context.currentProt == ServerProt.TRIGGER_ONDIALOGABORT) {
            if (InterfaceManager.topLevelInterface != -1) {
                InterfaceManager.runHookImmediate(InterfaceManager.IMMEDIATE_HOOK_TYPE_DIALOGABORT, InterfaceManager.topLevelInterface);
            }
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.FRIENDLIST_LOADED) {
            FriendsList.status = 1;
            FriendsList.lastTransmit = World.tick;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CLANCHANNEL_FULL) {
            Static39.lastClanChannelTransmit = World.tick;
            @Pc(446) boolean affined = bitPacket.g1() == 1;

            if (context.currentPacketSize != 1) {
                if (affined) {
                    ClanChannel.affined = new ClanChannel(bitPacket);
                } else {
                    ClanChannel.listened = new ClanChannel(bitPacket);
                }

                context.currentProt = null;
                return true;
            }

            if (affined) {
                ClanChannel.affined = null;
            } else {
                ClanChannel.listened = null;
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_ZONE_PARTIAL_ENCLOSED) {
            Static626.updateZoneX = bitPacket.g1b_alt2() << 3;
            Static87.updateZoneLevel = bitPacket.g1_alt1();
            Static270.updateZoneZ = bitPacket.g1b() << 3;

            while (bitPacket.pos < context.currentPacketSize) {
                @Pc(6873) ZoneProt zoneProt = ZoneProt.values()[bitPacket.g1()];
                decodeZoneProt(zoneProt);
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.REBUILD_NORMAL) {
            Static434.rebuildNormal();
            context.currentProt = null;
            return false;
        } else if (context.currentProt == ServerProt.LOC_ANIM_SPECIFIC) {
            @Pc(277) int coord = bitPacket.g4_alt3();
            @Pc(100) int level = (coord >> 28) & 0x3;
            @Pc(526) int x = (coord >> 14) & 0x3FFF;
            @Pc(1409) int z = coord & 0x3FFF;

            @Pc(1413) int animation = bitPacket.g2_alt2();
            if (animation == 65535) {
                animation = -1;
            }

            @Pc(2098) int shapeAndRotation = bitPacket.g1_alt3();
            @Pc(992) int shape = shapeAndRotation >> 2;
            @Pc(996) int rotation = shapeAndRotation & 0x3;
            @Pc(1449) int layer = LOC_LAYERS_BY_SHAPE[shape];

            z -= WorldMap.areaBaseZ;
            x -= WorldMap.areaBaseX;
            Static198.animateLocation(level, x, z, shape, rotation, layer, animation);

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IGNORELIST_ADD) {
            @Pc(277) int flags = bitPacket.g1();
            @Pc(892) boolean update = (flags & 0x1) == 1;

            @Pc(629) String name = bitPacket.gjstr();
            @Pc(4175) String nameUnfiltered = bitPacket.gjstr();
            if (nameUnfiltered.equals("")) {
                nameUnfiltered = name;
            }

            @Pc(4177) String formerName = bitPacket.gjstr();
            @Pc(1750) String formerNameUnfiltered = bitPacket.gjstr();
            if (formerNameUnfiltered.equals("")) {
                formerNameUnfiltered = formerName;
            }

            if (update) {
                for (@Pc(992) int i = 0; i < IgnoreList.count; i++) {
                    if (IgnoreList.namesUnfiltered[i].equals(formerNameUnfiltered)) {
                        IgnoreList.names[i] = name;
                        IgnoreList.namesUnfiltered[i] = nameUnfiltered;
                        IgnoreList.formerNames[i] = formerName;
                        IgnoreList.formerNamesUnfiltered[i] = formerNameUnfiltered;
                        break;
                    }
                }
            } else {
                IgnoreList.names[IgnoreList.count] = name;
                IgnoreList.namesUnfiltered[IgnoreList.count] = nameUnfiltered;
                IgnoreList.formerNames[IgnoreList.count] = formerName;
                IgnoreList.formerNamesUnfiltered[IgnoreList.count] = formerNameUnfiltered;
                IgnoreList.temporary[IgnoreList.count] = (flags & 0x2) == 2;
                IgnoreList.count++;
            }

            FriendsList.lastTransmit = World.tick;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.JCOINS_UPDATE) {
            @Pc(277) int jcoins = bitPacket.g4_alt2();
            if (jcoins != UserDetail.lobbyJcoinsBalance) {
                UserDetail.lobbyJcoinsBalance = jcoins;
                ScriptRunner.executeTrigger(ClientTriggerType.JCOINS_UPDATE, -1, -1);
            }
            context.currentProt = null;
            return true;
        } else if (ServerProt.MESSAGE_QUICKCHAT_PLAYER_GROUP == context.currentProt) {
            @Pc(446) boolean filtered = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(629) String nameUnfiltered = name;
            if (filtered) {
                nameUnfiltered = bitPacket.gjstr();
            }
            @Pc(1409) int rank = bitPacket.g1();
            @Pc(1413) int quickchatId = bitPacket.g2();

            @Pc(1425) boolean blocked = false;
            if (rank <= 1 && IgnoreList.contains(nameUnfiltered)) {
                blocked = true;
            }

            if (!blocked && Static659.blockChat == 0) {
                @Pc(1427) String message = QuickChatPhraseTypeList.instance.get(quickchatId).decodeText(bitPacket);

                if (rank == 2) {
                    ChatHistory.add(ChatLineType.QUICKCHAT_PLAYER_GROUP, 0, "<img=1>" + name, "<img=1>" + nameUnfiltered, name, null, quickchatId, message);
                } else if (rank == 1) {
                    ChatHistory.add(ChatLineType.QUICKCHAT_PLAYER_GROUP, 0, "<img=0>" + name, "<img=0>" + nameUnfiltered, name, null, quickchatId, message);
                } else {
                    ChatHistory.add(ChatLineType.QUICKCHAT_PLAYER_GROUP, 0, name, nameUnfiltered, name, null, quickchatId, message);
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SEND_PING) {
            @Pc(277) int value1 = bitPacket.g4();
            @Pc(100) int value2 = bitPacket.g4();
            @Pc(7309) ClientMessage message = ClientMessage.create(ClientProt.SEND_PING_REPLY, context.isaac);
            message.bitPacket.p4(value1);
            message.bitPacket.p4(value2);
            context.send(message);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOC_ADD_CHANGE) {
            decodeZoneProt(ZoneProt.LOC_ADD_CHANGE);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_FRIENDCHAT_CHANNEL_SINGLEUSER) {
            @Pc(1937) String name = bitPacket.gjstr();
            @Pc(2080) boolean filtered = bitPacket.g1() == 1;
            @Pc(627) String nameUnfiltered;
            if (filtered) {
                nameUnfiltered = bitPacket.gjstr();
            } else {
                nameUnfiltered = name;
            }
            @Pc(1409) int world = bitPacket.g2();
            @Pc(7377) byte rank = bitPacket.g1b();

            @Pc(1425) boolean remove = false;
            if (rank == -128) {
                remove = true;
            }

            if (remove) {
                if (FriendChat.count == 0) {
                    context.currentProt = null;
                    return true;
                }

                @Pc(992) int index;
                for (index = 0; index < FriendChat.count; index++) {
                    if (FriendChat.users[index].usernameUnfiltered.equals(nameUnfiltered) && world == FriendChat.users[index].world) {
                        break;
                    }
                }

                if (index < FriendChat.count) {
                    while (FriendChat.count - 1 > index) {
                        FriendChat.users[index] = FriendChat.users[index + 1];
                        index++;
                    }

                    FriendChat.count--;
                    FriendChat.users[FriendChat.count] = null;
                }
            } else {
                @Pc(1427) String worldName = bitPacket.gjstr();
                @Pc(7394) FriendChatUser user = new FriendChatUser();
                user.name = name;
                user.usernameUnfiltered = nameUnfiltered;
                user.usernameFormatted = NameTools.format(user.usernameUnfiltered);
                user.world = world;
                user.worldName = worldName;
                user.rank = rank;

                @Pc(1449) int index;
                for (index = FriendChat.count - 1; index >= 0; index--) {
                    @Pc(653) int comparison = FriendChat.users[index].usernameFormatted.compareTo(user.usernameFormatted);

                    if (comparison == 0) {
                        FriendChat.users[index].world = world;
                        FriendChat.users[index].rank = rank;
                        FriendChat.users[index].worldName = worldName;

                        if (nameUnfiltered.equals(PlayerEntity.self.nameUnfiltered)) {
                            FriendChat.rank = rank;
                        }

                        // g.trace("-=-=- CLANCHAT INCR+ CHANNEL UPDATE -=-=-");
                        // g.trace("usercount={}", (Object)Integer.valueOf(km));

                        // for(int var13 = 0; var13 < km; ++var13) {
                        //     g.trace("{} (w{})", (Object)kg[var13].d, (Object)Integer.valueOf(kg[var13].j));
                        // }

                        Static352.lastClanTransmit = World.tick;
                        context.currentProt = null;
                        return true;
                    }

                    if (comparison < 0) {
                        break;
                    }
                }

                if (FriendChat.count >= FriendChat.users.length) {
                    context.currentProt = null;
                    return true;
                }

                for (@Pc(653) int i = FriendChat.count - 1; i > index; i--) {
                    FriendChat.users[i + 1] = FriendChat.users[i];
                }

                if (FriendChat.count == 0) {
                    FriendChat.users = new FriendChatUser[100];
                }

                FriendChat.users[index + 1] = user;
                FriendChat.count++;

                if (nameUnfiltered.equals(PlayerEntity.self.nameUnfiltered)) {
                    FriendChat.rank = rank;
                }
            }

            Static352.lastClanTransmit = World.tick;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARCLAN_INT) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(100) int value = bitPacket.g4();
            if (Static279.clanVars == null) {
                Static279.clanVars = new Object[VarClanSettingTypeList.instance.num];
            }
            Static279.clanVars[id] = Integer.valueOf(value);
            Static265.varclanUpdates[Static710.varclanUpdateCount++ & 0x1F] = id;
            context.currentProt = null;
            return true;
        } else if (ServerProt.MESSAGE_PUBLIC == context.currentProt) {
            @Pc(277) int slot = bitPacket.g2();

            @Pc(7724) PlayerEntity player;
            if (slot == PlayerList.activePlayerSlot) {
                player = PlayerEntity.self;
            } else {
                player = PlayerList.highResolutionPlayers[slot];
            }

            if (player == null) {
                context.currentProt = null;
                return true;
            }

            @Pc(526) int flags = bitPacket.g2();
            @Pc(1409) int rank = bitPacket.g1();
            @Pc(6565) boolean quickChat = (flags & 0x8000) != 0;

            if (player.nameUnfiltered != null && player.playerModel != null) {
                @Pc(1425) boolean blocked = false;
                if (rank <= 1) {
                    if (!quickChat && ((UserDetail.underage && !UserDetail.parentalChatConsent) || Static617.quickChatWorld)) {
                        blocked = true;
                    } else if (IgnoreList.contains(player.nameUnfiltered)) {
                        blocked = true;
                    }
                }

                if (!blocked && Static659.blockChat == 0) {
                    @Pc(996) int quickChatId = -1;
                    @Pc(1427) String quickChatText;
                    if (quickChat) {
                        flags &= 0x7FFF;
                        @Pc(7827) QuickChatPhrase phrase = QuickChatPhrase.decode(bitPacket);
                        quickChatId = phrase.id;
                        quickChatText = phrase.type.decodeText(bitPacket);
                    } else {
                        quickChatText = StringTools.escapeBrackets(WordPack.decode(bitPacket));
                    }

                    player.setChatLine(quickChatText.trim(), flags >> 8, flags & 0xFF);

                    @Pc(1449) int type;
                    if (rank == 1 || rank == 2) {
                        type = quickChat ? ChatLineType.QUICKCHAT_PUBLIC : ChatLineType.PUBLIC_RANK;
                    } else {
                        type = quickChat ? ChatLineType.QUICKCHAT_PUBLIC : ChatLineType.PUBLIC;
                    }

                    if (rank == 2) {
                        ChatHistory.add(type, 0, "<img=1>" + player.getName(false, true), "<img=1>" + player.getNameUnfiltered(), player.displayName, null, quickChatId, quickChatText);
                    } else if (rank == 1) {
                        ChatHistory.add(type, 0, "<img=0>" + player.getName(false, true), "<img=0>" + player.getNameUnfiltered(), player.displayName, null, quickChatId, quickChatText);
                    } else {
                        ChatHistory.add(type, 0, player.getName(false, true), player.getNameUnfiltered(), player.displayName, null, quickChatId, quickChatText);
                    }
                }
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SYNTH_SOUND) {
            @Pc(277) int id = bitPacket.g2();
            if (id == 65535) {
                id = -1;
            }
            @Pc(100) int loops = bitPacket.g1();
            @Pc(526) int delay = bitPacket.g2();
            @Pc(1409) int volume = bitPacket.g1();
            @Pc(1413) int rate = bitPacket.g2();
            SoundManager.playSynthSound(id, loops, delay, volume, rate);
            context.currentProt = null;
            return true;
        } else if (ServerProt.SERVER_TICK_END == context.currentProt) {
            context.currentProt = null;
            return false;
        } else if (ServerProt.IF_SETEVENTS == context.currentProt) {
            @Pc(277) int start = bitPacket.ig2();
            if (start == 65535) {
                start = -1;
            }

            @Pc(100) int idAndSlot = bitPacket.g4_alt3();

            @Pc(526) int end = bitPacket.g2_alt2();
            if (end == 65535) {
                end = -1;
            }

            @Pc(1409) int events = bitPacket.g4_alt1();
            VerifyId.incrementAndTransmit();

            for (@Pc(1413) int component = start; component <= end; component++) {
                @Pc(644) long key = ((long) idAndSlot << 32) + (long) component;
                @Pc(4597) ServerActiveProperties oldProperties = (ServerActiveProperties) InterfaceManager.serverActiveProperties.get(key);
                @Pc(4611) ServerActiveProperties newProperties;

                if (oldProperties != null) {
                    newProperties = new ServerActiveProperties(events, oldProperties.targetParam);
                    oldProperties.unlink();
                } else if (component == -1) {
                    newProperties = new ServerActiveProperties(events, InterfaceList.list(idAndSlot).serverActiveProperties.targetParam);
                } else {
                    newProperties = new ServerActiveProperties(events, -1);
                }

                InterfaceManager.serverActiveProperties.put(key, newProperties);
            }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETRETEX) {
            @Pc(277) int destination = bitPacket.g2();
            @Pc(100) int idAndSlot = bitPacket.g4();
            @Pc(526) int source = bitPacket.g2();
            @Pc(1409) int index = bitPacket.g1_alt3();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetRetex(idAndSlot, index, source, destination);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_ZONE_PARTIAL_FOLLOWS) {
            Static626.updateZoneX = bitPacket.g1b_alt1() << 3;
            Static270.updateZoneZ = bitPacket.g1b() << 3;
            Static87.updateZoneLevel = bitPacket.g1_alt3();
            context.currentProt = null;
            return true;
        } else if (ServerProt.OBJ_ADD == context.currentProt) {
            decodeZoneProt(ZoneProt.OBJ_ADD);
            context.currentProt = null;
            return true;
        } else if (ServerProt.IF_SETANIM == context.currentProt) {
            @Pc(277) int anim = bitPacket.g2s_alt3();
            @Pc(100) int idAndSlot = bitPacket.g4_alt2();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetModelAnim(idAndSlot, anim);
            context.currentProt = null;
            return true;
        } else if (ServerProt.CAM_FORCEANGLE == context.currentProt) {
            @Pc(277) int pitch = bitPacket.g2_alt3();
            @Pc(100) int yaw = bitPacket.g2_alt2();
            VerifyId.incrementAndTransmit();
            Camera.forceAngle(pitch, yaw, 0);
            context.currentProt = null;
            return true;
        } else if (ServerProt.UPDATE_DOB == context.currentProt) {
            UserDetail.dob = bitPacket.g3s();
            UserDetail.underage = bitPacket.g1() == 1;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CLIENT_SETVARC_SMALL) {
            @Pc(277) int id = bitPacket.g2_alt3();
            @Pc(931) byte value = bitPacket.g1b_alt3();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.setVarc(id, value);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_PRIVATE_ECHO) {
            @Pc(1937) String name = bitPacket.gjstr();
            @Pc(627) String message = StringTools.escapeBrackets(WordPack.decode(bitPacket));
            ChatHistory.add(ChatLineType.PRIVATE_ECHO, 0, name, name, name, message);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.CLANSETTINGS_DELTA) {
            Static400.lastClanSettingsTransmit = World.tick;
            @Pc(446) boolean affined = bitPacket.g1() == 1;
            @Pc(8376) ClanSettingsDelta delta = new ClanSettingsDelta(bitPacket);

            @Pc(8380) ClanSettings settings;
            if (affined) {
                settings = ClanSettings.affined;
            } else {
                settings = ClanSettings.listened;
            }

            delta.applyToClanSettings(settings);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.IF_SETTEXT) {
            @Pc(277) int idAndSlot = bitPacket.g4();
            @Pc(627) String text = bitPacket.gjstr();
            VerifyId.incrementAndTransmit();
            DelayedStateChange.interfaceSetText(idAndSlot, text);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_INV_STOP_TRANSMIT) {
            @Pc(277) int invId = bitPacket.ig2();
            @Pc(100) int flags = bitPacket.g1_alt2();
            @Pc(2080) boolean otherPlayer = (flags & 0x1) == 1;
            ClientInventory.delete(invId, otherPlayer);
            ClientInventory.updates[ClientInventory.updateCount++ & 0x1F] = invId;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.LOC_DEL) {
            decodeZoneProt(ZoneProt.LOC_DEL);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.NPC_INFO) {
            NPCList.updateNpcs();
            context.currentProt = null;
            return true;
        } else if (ServerProt.CAM_MOVETO == context.currentProt) {
            @Pc(277) int rate = bitPacket.g1_alt3();
            @Pc(100) int z = bitPacket.g1_alt2();
            @Pc(526) int x = bitPacket.g1_alt2();
            @Pc(1409) int step = bitPacket.g1_alt2();
            @Pc(1413) int y = bitPacket.g2_alt3() << 2;
            VerifyId.incrementAndTransmit();
            Camera.moveTo(x, y, z, step, rate, true);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.UPDATE_FRIENDCHAT_CHANNEL_FULL) {
            Static352.lastClanTransmit = World.tick;
            if (context.currentPacketSize == 0) {
                context.currentProt = null;
                FriendChat.count = 0;
                FriendChat.users = null;
                FriendChat.name = null;
                FriendChat.owner = null;
                return true;
            }

            FriendChat.owner = bitPacket.gjstr();
            @Pc(446) boolean filtered = bitPacket.g1() == 1;
            if (filtered) {
                bitPacket.gjstr();
            }

            @Pc(4669) long channel = bitPacket.g8();
            FriendChat.name = Base37.decode(channel);
            FriendChat.kickRank = bitPacket.g1b();
            // g.trace("CLANCHAT: Received full channel list for channel: {}, kickrank: {}", (Object) ju, (Object) Byte.valueOf(kr));

            @Pc(1409) int count = bitPacket.g1();
            if (count == 255) {
                context.currentProt = null;
                return true;
            }

            FriendChat.count = count;
            @Pc(8611) FriendChatUser[] users = new FriendChatUser[100];
            for (@Pc(2098) int i = 0; i < FriendChat.count; i++) {
                users[i] = new FriendChatUser();
                users[i].name = bitPacket.gjstr();

                filtered = bitPacket.g1() == 1;
                if (filtered) {
                    users[i].usernameUnfiltered = bitPacket.gjstr();
                } else {
                    users[i].usernameUnfiltered = users[i].name;
                }

                users[i].usernameFormatted = NameTools.format(users[i].usernameUnfiltered);
                users[i].world = bitPacket.g2();
                users[i].rank = bitPacket.g1b();
                users[i].worldName = bitPacket.gjstr();

                if (users[i].usernameUnfiltered.equals(PlayerEntity.self.nameUnfiltered)) {
                    FriendChat.rank = users[i].rank;
                }
            }

            @Pc(1449) int end = FriendChat.count;
            while (end > 0) {
                @Pc(8729) boolean sorted = true;
                end--;

                for (@Pc(653) int i = 0; i < end; i++) {
                    if (users[i].usernameFormatted.compareTo(users[i + 1].usernameFormatted) > 0) {
                        @Pc(7394) FriendChatUser user = users[i];
                        users[i] = users[i + 1];
                        users[i + 1] = user;
                        sorted = false;
                    }
                }

                if (sorted) {
                    break;
                }
            }

            FriendChat.users = users;
            // g.trace("-=-=- CLANCHAT FULL CHANNEL UPDATE -=-=-");
            // g.trace("usercount={}", (Object)Integer.valueOf(km));

            // for(var11 = 0; var11 < km; ++var11) {
            // 	g.trace("{} (w{})", (Object)kg[var11].d, (Object)Integer.valueOf(kg[var11].j));
            // }

            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MAP_PROJANIM) {
            decodeZoneProt(ZoneProt.MAP_PROJANIM);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.SET_MAP_FLAG) {
            @Pc(277) int x = bitPacket.g1();
            @Pc(100) int y = bitPacket.g1_alt1();
            if (x == 255) {
                y = -1;
                x = -1;
            }
            DelayedStateChange.setMapFlag(x, y);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.OBJ_COUNT) {
            decodeZoneProt(ZoneProt.OBJ_COUNT);
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.VARCLAN_STRING) {
            @Pc(277) int id = bitPacket.g2();
            @Pc(627) String value = bitPacket.gjstr();
            if (Static279.clanVars == null) {
                Static279.clanVars = new Object[VarClanSettingTypeList.instance.num];
            }
            Static279.clanVars[id] = value;
            Static265.varclanUpdates[Static710.varclanUpdateCount++ & 0x1F] = id;
            context.currentProt = null;
            return true;
        } else if (context.currentProt == ServerProt.MESSAGE_FRIENDCHANNEL) {
            @Pc(446) boolean filtered = bitPacket.g1() == 1;
            @Pc(627) String name = bitPacket.gjstr();
            @Pc(629) String nameUnfiltered = name;
            if (filtered) {
                nameUnfiltered = bitPacket.gjstr();
            }
            @Pc(639) long channel = bitPacket.g8();
            @Pc(644) long idHi = bitPacket.g2();
            @Pc(649) long idLo = bitPacket.g3();
            @Pc(653) int rank = bitPacket.g1();
            @Pc(8945) long id = (idHi << 32) + idLo;

            @Pc(4857) boolean blocked = false;
            @Pc(1097) int i = 0;
            while (true) {
                if (i >= 100) {
                    if (rank <= 1) {
                        if ((UserDetail.underage && !UserDetail.parentalChatConsent) || Static617.quickChatWorld) {
                            blocked = true;
                        } else if (IgnoreList.contains(nameUnfiltered)) {
                            blocked = true;
                        }
                    }
                    break;
                }

                if (Static511.aLongArray17[i] == id) {
                    blocked = true;
                    break;
                }

                i++;
            }

            if (!blocked && Static659.blockChat == 0) {
                Static511.aLongArray17[Static97.anInt2001] = id;
                Static97.anInt2001 = (Static97.anInt2001 + 1) % 100;
                @Pc(9032) String message = StringTools.escapeBrackets(WordPack.decode(bitPacket));

                if (rank == 2 || rank == 3) {
                    ChatHistory.add(ChatLineType.FRIENDCHANNEL, 0, "<img=1>" + name, "<img=1>" + nameUnfiltered, name, Base37.decodeName(channel), -1, message);
                } else if (rank == 1) {
                    ChatHistory.add(ChatLineType.FRIENDCHANNEL, 0, "<img=0>" + name, "<img=0>" + nameUnfiltered, name, Base37.decodeName(channel), -1, message);
                } else {
                    ChatHistory.add(ChatLineType.FRIENDCHANNEL, 0, name, nameUnfiltered, name, Base37.decodeName(channel), -1, message);
                }
            }

            context.currentProt = null;
            return true;
        } else {
            JagException.sendTrace(null, "T1 - " + (context.currentProt == null ? -1 : context.currentProt.getOpcode()) + "," + (context.penultimateProt == null ? -1 : context.penultimateProt.getOpcode()) + "," + (context.antepenultimateProt == null ? -1 : context.antepenultimateProt.getOpcode()) + " - " + context.currentPacketSize);
            LoginManager.logout(false);
            return true;
        }
    }

    @OriginalMember(owner = "client!lma", name = "b", descriptor = "(I)V")
    public static void sendWindowStatus() {
        @Pc(22) ClientMessage message = ClientMessage.create(ClientProt.WINDOW_STATUS, ServerConnection.GAME.isaac);
        message.bitPacket.p1(InterfaceManager.getWindowMode());
        message.bitPacket.p2(GameShell.canvasWid);
        message.bitPacket.p2(GameShell.canvasHei);
        message.bitPacket.p1(ClientOptions.instance.antialiasingQuality.getValue());
        ServerConnection.GAME.send(message);
    }

    @OriginalMember(owner = "client!tba", name = "a", descriptor = "(ILclient!pc;)V")
    public static void decodeZoneProt(@OriginalArg(1) ZoneProt prot) {
        @Pc(8) BitPacket bitPacket = ServerConnection.GAME.bitPacket;

        if (prot == ZoneProt.OBJ_COUNT) {
            @Pc(15) int coord = bitPacket.g1();

            @Pc(21) int zoneZ = (coord & 0x7) + Static270.updateZoneZ;
            @Pc(25) int z = WorldMap.areaBaseZ + zoneZ;

            @Pc(34) int zoneX = Static626.updateZoneX + ((coord >> 4) & 0x7);
            @Pc(38) int x = WorldMap.areaBaseX + zoneX;

            @Pc(42) int objId = bitPacket.g2();
            @Pc(46) int oldCount = bitPacket.g2();
            @Pc(52) int newCount = bitPacket.g2();

            if (Static497.objStacks != null) {
                @Pc(69) ObjStack stack = (ObjStack) Static497.objStacks.get((Static87.updateZoneLevel << 28) | (z << 14) | x);

                if (stack != null) {
                    for (@Pc(77) ObjStackEntry entry = (ObjStackEntry) stack.objs.first(); entry != null; entry = (ObjStackEntry) stack.objs.next()) {
                        if ((objId & 0x7FFF) == entry.id && entry.count == oldCount) {
                            entry.unlink();
                            entry.count = newCount;
                            Static2.sortAllObjs(Static87.updateZoneLevel, x, z, entry);
                            break;
                        }
                    }

                    if (zoneX >= 0 && zoneZ >= 0 && Static720.mapWidth > zoneX && Static501.mapLength > zoneZ) {
                        Static468.updateObjCount(Static87.updateZoneLevel, zoneX, zoneZ);
                    }
                }
            }

            return;
        } else if (ZoneProt.LOC_CUSTOMISE == prot) {
            @Pc(15) int shapeAndRotation = bitPacket.g1_alt1();
            @Pc(21) int shape = shapeAndRotation >> 2;
            @Pc(25) int layer = LOC_LAYERS_BY_SHAPE[shape];
            @Pc(34) int flags = bitPacket.g1();
            @Pc(38) int coord = bitPacket.g1_alt3();
            @Pc(42) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(46) int zoneY = (coord & 0x7) + Static270.updateZoneZ;
            @Pc(52) int id = bitPacket.ig2();

            if (shape == LocShapes.CENTREPIECE_DIAGONAL) {
                shape = LocShapes.CENTREPIECE_STRAIGHT;
            }

            @Pc(210) LocType locType = LocTypeList.instance.list(id);
            @Pc(212) int remodelLen = 0;
            if (locType.modelShapes != null) {
                @Pc(217) int modelIndex = -1;

                for (@Pc(219) int index = 0; index < locType.modelShapes.length; index++) {
                    if (locType.modelShapes[index] == shape) {
                        modelIndex = index;
                        break;
                    }
                }

                remodelLen = locType.models[modelIndex].length;
            }

            @Pc(217) int recolLength = 0;
            if (locType.recol_d != null) {
                recolLength = locType.recol_d.length;
            }

            @Pc(219) int retexLength = 0;
            if (locType.retex_d != null) {
                retexLength = locType.retex_d.length;
            }

            if ((flags & 0x1) == 1) {
                Static296.customiseLocation(null, Static87.updateZoneLevel, layer, zoneX, zoneY);
            } else {
                @Pc(278) int[] remodel = null;
                if ((flags & 0x2) == 2) {
                    remodel = new int[remodelLen];
                    for (@Pc(290) int i = 0; i < remodelLen; i++) {
                        remodel[i] = bitPacket.g2();
                    }
                }

                @Pc(310) short[] recol = null;
                if ((flags & 0x4) == 4) {
                    recol = new short[recolLength];
                    for (@Pc(322) int i = 0; i < recolLength; i++) {
                        recol[i] = (short) bitPacket.g2();
                    }
                }

                @Pc(343) short[] retex = null;
                if ((flags & 0x8) == 8) {
                    retex = new short[retexLength];
                    for (@Pc(353) int i = 0; i < retexLength; i++) {
                        retex[i] = (short) bitPacket.g2();
                    }
                }

                Static296.customiseLocation(new LocTypeCustomisation(LocTypeCustomisation.uid++, remodel, recol, retex), Static87.updateZoneLevel, layer, zoneX, zoneY);
            }
        } else if (prot == ZoneProt.LOC_ANIM) {
            @Pc(15) int animation = bitPacket.g2_alt3();
            if (animation == 65535) {
                animation = -1;
            }

            @Pc(21) int coord = bitPacket.g1_alt1();
            @Pc(25) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(34) int zoneZ = (coord & 0x7) + Static270.updateZoneZ;

            @Pc(38) int shapeAndRotation = bitPacket.g1();
            @Pc(42) int shape = shapeAndRotation >> 2;
            @Pc(46) int rotation = shapeAndRotation & 0x3;
            @Pc(52) int layer = LOC_LAYERS_BY_SHAPE[shape];

            Static198.animateLocation(Static87.updateZoneLevel, zoneX, zoneZ, shape, rotation, layer, animation);
        } else if (prot == ZoneProt.LOC_PREFETCH) {
            @Pc(15) int id = bitPacket.g2();
            @Pc(21) int shape = bitPacket.g1();
            LocTypeList.instance.list(id).loadedModels(shape);
        } else if (prot == ZoneProt.SOUND_AREA) {
            @Pc(15) int coord = bitPacket.g1();
            @Pc(21) int zoneX = Static626.updateZoneX + ((coord >> 4) & 0x7);
            @Pc(25) int zoneZ = (coord & 0x7) + Static270.updateZoneZ;

            @Pc(34) int synth = bitPacket.g2();
            if (synth == 65535) {
                synth = -1;
            }

            @Pc(38) int rangeAndLoops = bitPacket.g1();
            @Pc(42) int range = rangeAndLoops >> 4 & 0xF;
            @Pc(46) int loops = rangeAndLoops & 0x7;
            @Pc(52) int delay = bitPacket.g1();
            @Pc(537) int volume = bitPacket.g1();
            @Pc(212) int rate = bitPacket.g2();

            if (zoneX >= 0 && zoneZ >= 0 && zoneX < Static720.mapWidth && Static501.mapLength > zoneZ) {
                @Pc(217) int rangePlusOne = range + 1;

                if (zoneX - rangePlusOne <= PlayerEntity.self.pathX[0] && rangePlusOne + zoneX >= PlayerEntity.self.pathX[0] && PlayerEntity.self.pathZ[0] >= zoneZ - rangePlusOne && rangePlusOne + zoneZ >= PlayerEntity.self.pathZ[0]) {
                    SoundManager.playSynthSoundArea(synth, loops, delay, volume, rate, range + (Static87.updateZoneLevel << 24) + (zoneX << 16) + (zoneZ << 8));
                }
            }
        } else if (prot == ZoneProt.LOC_ADD_CHANGE) {
            @Pc(15) int coord = bitPacket.g1();
            @Pc(21) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(25) int zoneZ = (coord & 0x7) + Static270.updateZoneZ;
            @Pc(34) int shapeAndRotation = bitPacket.g1();
            @Pc(38) int shape = shapeAndRotation >> 2;
            @Pc(42) int rotation = shapeAndRotation & 0x3;
            @Pc(46) int layer = LOC_LAYERS_BY_SHAPE[shape];
            @Pc(52) int id = bitPacket.g2_alt2();

            if (AreaMode.isOutOfBounds(Static117.areaMode) || zoneX >= 0 && zoneZ >= 0 && Static720.mapWidth > zoneX && Static501.mapLength > zoneZ) {
                Static553.changeLocation(zoneX, zoneZ, Static87.updateZoneLevel, shape, rotation, layer, id);
            }
        } else if (prot == ZoneProt.MAP_PROJANIM_HALFSQ) {
            @Pc(15) int coord = bitPacket.g1();
            @Pc(21) int x1 = (Static626.updateZoneX * 2) + ((coord >> 4) & 0xF);
            @Pc(25) int z1 = (coord & 0xF) + Static270.updateZoneZ * 2;
            @Pc(34) int flags = bitPacket.g1();
            @Pc(764) boolean groundRelative = (flags & 0x1) != 0;
            @Pc(773) boolean wornRelative = (flags & 0x2) != 0;
            @Pc(46) int wornSlot = wornRelative ? flags >> 2 : -1;
            @Pc(52) int x2 = bitPacket.g1b() + x1;
            @Pc(537) int z2 = z1 + bitPacket.g1b();
            @Pc(212) int entity1 = bitPacket.g2s();
            @Pc(217) int entity2 = bitPacket.g2s();
            @Pc(219) int id = bitPacket.g2();

            @Pc(812) int y1 = bitPacket.g1();
            if (wornRelative) {
                y1 = (byte) y1;
            } else {
                y1 *= 4;
            }

            @Pc(290) int y2 = bitPacket.g1() * 4;
            @Pc(322) int t1 = bitPacket.g2();
            @Pc(353) int t2 = bitPacket.g2();
            @Pc(843) int verticalAngle = bitPacket.g1();
            @Pc(847) int displacement = bitPacket.g2();
            if (verticalAngle == 255) {
                verticalAngle = -1;
            }

            if (x1 >= 0 && z1 >= 0 && x1 < Static720.mapWidth * 2 && z1 < Static720.mapWidth * 2 && x2 >= 0 && z2 >= 0 && Static501.mapLength * 2 > x2 && Static501.mapLength * 2 > z2 && id != 65535) {
                x2 = x2 * 256;
                displacement <<= 0x2;
                z1 = z1 * 256;
                z2 *= 256;
                y1 <<= 0x2;
                y2 <<= 0x2;
                x1 *= 256;

                if (entity1 != 0 && wornSlot != -1) {
                    @Pc(948) PathingEntity source = null;

                    if (entity1 >= 0) {
                        @Pc(957) int slot = entity1 - 1;
                        @Pc(964) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(slot);

                        if (node != null) {
                            source = node.npc;
                        }
                    } else {
                        @Pc(957) int slot = -entity1 - 1;

                        if (slot == PlayerList.activePlayerSlot) {
                            source = PlayerEntity.self;
                        } else {
                            source = PlayerList.highResolutionPlayers[slot];
                        }
                    }

                    if (source != null) {
                        @Pc(991) BASType basType = source.getBASType();
                        if (basType.wornTransformations != null && basType.wornTransformations[wornSlot] != null) {
                            y1 -= basType.wornTransformations[wornSlot][1];
                        }
                        if (basType.graphicOffsets != null && basType.graphicOffsets[wornSlot] != null) {
                            y1 -= basType.graphicOffsets[wornSlot][1];
                        }
                    }
                }

                @Pc(1053) ProjectileAnimation projectile = new ProjectileAnimation(id, Static87.updateZoneLevel, Static87.updateZoneLevel, x1, z1, entity1, entity2, y1, y2, t1 + TimeUtils.clock, t2 + TimeUtils.clock, verticalAngle, displacement, groundRelative, wornSlot);
                projectile.target(Static102.averageHeight(Static87.updateZoneLevel, x2, z2) - y2, t1 + TimeUtils.clock, z2, x2);
                Static505.projectiles.addLast(new ProjectileAnimationNode(projectile));
            }
        } else if (ZoneProt.OBJ_REVEAL == prot) {
            @Pc(15) int owner = bitPacket.g2_alt2();
            @Pc(21) int coord = bitPacket.g1_alt1();

            @Pc(25) int zoneZ = Static270.updateZoneZ + (coord & 0x7);
            @Pc(34) int z = zoneZ + WorldMap.areaBaseZ;

            @Pc(38) int zoneX = Static626.updateZoneX + ((coord >> 4) & 0x7);
            @Pc(42) int x = WorldMap.areaBaseX + zoneX;

            @Pc(46) int objId = bitPacket.ig2();
            @Pc(52) int objCount = bitPacket.g2();

            if (owner != PlayerList.activePlayerSlot) {
                @Pc(1151) boolean inBounds = zoneX >= 0 && zoneZ >= 0 && zoneX < Static720.mapWidth && zoneZ < Static501.mapLength;

                if (inBounds || AreaMode.isOutOfBounds(Static117.areaMode)) {
                    Static2.sortAllObjs(Static87.updateZoneLevel, x, z, new ObjStackEntry(objId, objCount));

                    if (inBounds) {
                        Static468.updateObjCount(Static87.updateZoneLevel, zoneX, zoneZ);
                    }
                }
            }
        } else if (ZoneProt.SOUND_VORBIS_AREA == prot) {
            @Pc(15) int coord = bitPacket.g1();
            @Pc(21) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(25) int zoneY = (coord & 0x7) + Static270.updateZoneZ;
            @Pc(34) int id = bitPacket.g2();
            if (id == 65535) {
                id = -1;
            }
            @Pc(38) int rangeAndLoops = bitPacket.g1();
            @Pc(42) int range = rangeAndLoops >> 4 & 0xF;
            @Pc(46) int loops = rangeAndLoops & 0x7;
            @Pc(52) int delay = bitPacket.g1();
            @Pc(537) int volume = bitPacket.g1();
            @Pc(212) int rate = bitPacket.g2();

            if (zoneX >= 0 && zoneY >= 0 && Static720.mapWidth > zoneX && Static501.mapLength > zoneY) {
                @Pc(217) int rangePlusOne = range + 1;

                if (PlayerEntity.self.pathX[0] >= zoneX - rangePlusOne && rangePlusOne + zoneX >= PlayerEntity.self.pathX[0] && PlayerEntity.self.pathZ[0] >= zoneY - rangePlusOne && PlayerEntity.self.pathZ[0] <= zoneY + rangePlusOne) {
                    SoundManager.playVorbisSoundArea(id, loops, delay, volume, rate, range + (zoneY << 8) + (Static87.updateZoneLevel << 24) + (zoneX << 16));
                }
            }
        } else if (prot == ZoneProt.OBJ_DEL) {
            @Pc(15) int objId = bitPacket.g2();
            @Pc(21) int coord = bitPacket.g1();
            @Pc(25) int zoneZ = (coord & 0x7) + Static270.updateZoneZ;
            @Pc(34) int z = zoneZ + WorldMap.areaBaseZ;
            @Pc(38) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(42) int x = zoneX + WorldMap.areaBaseX;

            @Pc(1389) ObjStack stack = (ObjStack) Static497.objStacks.get((Static87.updateZoneLevel << 28) | (z << 14) | x);
            if (stack != null) {
                for (@Pc(1399) ObjStackEntry entry = (ObjStackEntry) stack.objs.first(); entry != null; entry = (ObjStackEntry) stack.objs.next()) {
                    if (entry.id == (objId & 0x7FFF)) {
                        entry.unlink();
                        break;
                    }
                }

                if (stack.objs.isEmpty()) {
                    stack.unlink();
                }

                if (zoneX >= 0 && zoneZ >= 0 && zoneX < Static720.mapWidth && zoneZ < Static501.mapLength) {
                    Static468.updateObjCount(Static87.updateZoneLevel, zoneX, zoneZ);
                }
            }
        } else if (ZoneProt.TEXT_COORD == prot) {
            bitPacket.g1();
            @Pc(15) int coord = bitPacket.g1();
            @Pc(21) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(25) int zoneZ = (coord & 0x7) + Static270.updateZoneZ;
            @Pc(34) int duration = bitPacket.g2();
            @Pc(38) int y = bitPacket.g1();
            @Pc(42) int colour = bitPacket.g3();
            @Pc(1511) String text = bitPacket.gjstr();
            TextCoordList.add(Static87.updateZoneLevel, zoneX, y, zoneZ, text, colour, duration);
        } else if (ZoneProt.MAP_PROJANIM == prot) {
            @Pc(15) int position = bitPacket.g1();
            @Pc(1540) boolean groundRelative = (position & 0x80) != 0;
            @Pc(25) int x1 = (position >> 3 & 0x7) + Static626.updateZoneX;
            @Pc(34) int z1 = (position & 0x7) + Static270.updateZoneZ;
            @Pc(38) int x2 = x1 + bitPacket.g1b();
            @Pc(42) int z2 = z1 + bitPacket.g1b();
            @Pc(46) int entity = bitPacket.g2s();
            @Pc(52) int id = bitPacket.g2();
            @Pc(537) int y1 = bitPacket.g1() * 4;
            @Pc(212) int y2 = bitPacket.g1() * 4;
            @Pc(217) int t1 = bitPacket.g2();
            @Pc(219) int t2 = bitPacket.g2();
            @Pc(812) int verticalAngle = bitPacket.g1();
            @Pc(290) int displacement = bitPacket.g2();

            if (verticalAngle == 255) {
                verticalAngle = -1;
            }

            if (x1 >= 0 && z1 >= 0 && x1 < Static720.mapWidth && z1 < Static501.mapLength && x2 >= 0 && z2 >= 0 && x2 < Static720.mapWidth && z2 < Static501.mapLength && id != 65535) {
                z2 = z2 * 512 + 256;
                displacement <<= 0x2;
                y1 <<= 0x2;
                x1 = x1 * 512 + 256;
                y2 <<= 0x2;
                x2 = x2 * 512 + 256;
                z1 = z1 * 512 + 256;

                @Pc(1728) ProjectileAnimation projectile = new ProjectileAnimation(id, Static87.updateZoneLevel, Static87.updateZoneLevel, x1, z1, 0, entity, y1, y2, t1 + TimeUtils.clock, t2 + TimeUtils.clock, verticalAngle, displacement, groundRelative, -1);
                projectile.target(Static102.averageHeight(Static87.updateZoneLevel, x2, z2) - y2, TimeUtils.clock + t1, z2, x2);
                Static505.projectiles.addLast(new ProjectileAnimationNode(projectile));
            }
        } else if (ZoneProt.LOC_DEL == prot) {
            @Pc(15) int coord = bitPacket.g1_alt2();
            @Pc(21) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(25) int zoneZ = (coord & 0x7) + Static270.updateZoneZ;
            @Pc(34) int shapeAndRotation = bitPacket.g1();
            @Pc(38) int shape = shapeAndRotation >> 2;
            @Pc(42) int rotation = shapeAndRotation & 0x3;
            @Pc(46) int layer = LOC_LAYERS_BY_SHAPE[shape];

            if (AreaMode.isOutOfBounds(Static117.areaMode) || zoneX >= 0 && zoneZ >= 0 && zoneX < Static720.mapWidth && zoneZ < Static501.mapLength) {
                Static553.changeLocation(zoneX, zoneZ, Static87.updateZoneLevel, shape, rotation, layer, -1);
            }
        } else if (ZoneProt.OBJ_ADD == prot) {
            @Pc(15) int coord = bitPacket.g1_alt2();
            @Pc(21) int zoneZ = Static270.updateZoneZ + (coord & 0x7);
            @Pc(25) int z = zoneZ + WorldMap.areaBaseZ;
            @Pc(34) int zoneX = ((coord >> 4) & 0x7) + Static626.updateZoneX;
            @Pc(38) int x = WorldMap.areaBaseX + zoneX;
            @Pc(42) int objId = bitPacket.g2_alt2();
            @Pc(46) int objCount = bitPacket.g2();
            @Pc(1886) boolean inBounds = zoneX >= 0 && zoneZ >= 0 && zoneX < Static720.mapWidth && zoneZ < Static501.mapLength;

            if (inBounds || AreaMode.isOutOfBounds(Static117.areaMode)) {
                Static2.sortAllObjs(Static87.updateZoneLevel, x, z, new ObjStackEntry(objId, objCount));

                if (inBounds) {
                    Static468.updateObjCount(Static87.updateZoneLevel, zoneX, zoneZ);
                }
            }
        } else if (prot == ZoneProt.MAP_ANIM) {
            @Pc(15) int coord = bitPacket.g1();
            @Pc(21) int zoneX = Static626.updateZoneX + ((coord >> 4) & 0x7);
            @Pc(25) int zoneZ = Static270.updateZoneZ + (coord & 0x7);
            @Pc(34) int id = bitPacket.g2();
            if (id == 65535) {
                id = -1;
            }
            @Pc(38) int y = bitPacket.g1();
            @Pc(42) int delay = bitPacket.g2();
            @Pc(46) int rotation = bitPacket.g1();

            if (zoneX >= 0 && zoneZ >= 0 && zoneX < Static720.mapWidth && Static501.mapLength > zoneZ) {
                if (id == -1) {
                    @Pc(2004) SpotAnimationNode node = (SpotAnimationNode) Static346.spotAnimations.get(zoneX << 16 | zoneZ);

                    if (node != null) {
                        node.spotAnimation.stopParticleSystem();
                        node.unlink();
                        return;
                    }
                } else {
                    @Pc(52) int x = (zoneX * 512) + 256;
                    @Pc(537) int z = (zoneZ * 512) + 256;
                    @Pc(212) int virtualLevel = Static87.updateZoneLevel;
                    if (virtualLevel < 3 && Static441.isBridgeAt(zoneZ, zoneX)) {
                        virtualLevel++;
                    }

                    @Pc(2065) SpotAnimation spotAnimation = new SpotAnimation(id, delay, Static87.updateZoneLevel, virtualLevel, x, Static102.averageHeight(Static87.updateZoneLevel, x, z) - y, z, zoneX, zoneX, zoneZ, zoneZ, rotation, false);
                    Static346.spotAnimations.put((zoneX << 16) | zoneZ, new SpotAnimationNode(spotAnimation));
                }
            }
        } else {
            JagException.sendTrace(null, "T3 - " + prot);
            LoginManager.logout(false);
        }
    }

    private ServerConnectionReader() {
        /* empty */
    }
}

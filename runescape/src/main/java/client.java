import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.LoginProt;
import com.jagex.core.constants.LoginResponseCode;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.constants.ModeWhat;
import com.jagex.core.constants.ModeWhere;
import com.jagex.core.constants.Ports;
import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.BufferedSocket;
import com.jagex.core.io.ConnectionInfo;
import com.jagex.core.io.Packet;
import com.jagex.core.stringtools.general.Base64;
import com.jagex.core.stringtools.general.WebTools;
import com.jagex.core.util.JagException;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.LocalisedText;
import com.jagex.game.MoveSpeed;
import com.jagex.game.PathFinder;
import com.jagex.game.PlayerModel;
import com.jagex.game.camera.CameraMode;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.game.runetek6.config.cursortype.CursorTypeList;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorTypeList;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterTypeList;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayTypeList;
import com.jagex.game.runetek6.config.fonttype.FontTypeList;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkTypeList;
import com.jagex.game.runetek6.config.idktype.IDKTypeList;
import com.jagex.game.runetek6.config.lighttype.LightTypeList;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.meltype.MapElementTypeList;
import com.jagex.game.runetek6.config.msitype.MSITypeList;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.paramtype.ParamTypeList;
import com.jagex.game.runetek6.config.questtype.QuestTypeList;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereTypeList;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxTypeList;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.game.runetek6.config.structtype.StructTypeList;
import com.jagex.game.runetek6.config.vartype.bit.VarBitTypeListClient;
import com.jagex.game.runetek6.config.vartype.clan.VarClanSettingTypeList;
import com.jagex.game.runetek6.config.vartype.clan.VarClanTypeList;
import com.jagex.game.runetek6.config.vartype.player.VarPlayerTypeListClient;
import com.jagex.game.world.World;
import com.jagex.graphics.FlipException;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.CachedResourceWorker;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5ResponseCode;
import com.jagex.js5.Js5State;
import com.jagex.js5.Js5WorkerThread;
import com.jagex.js5.js5;
import com.jagex.sign.SignedResourceStatus;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.keyboard.KeyLog;
import rs2.client.event.keyboard.KeyboardMonitor;
import rs2.client.event.mouse.MouseLog;
import rs2.client.event.mouse.MouseMonitor;
import rs2.client.web.ClientURLTools;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

@OriginalClass("client!client")
public final class client extends GameShell {

    @OriginalMember(owner = "client!jga", name = "k", descriptor = "Lclient!client;")
    public static client aClient1;

    @OriginalMember(owner = "client!fha", name = "c", descriptor = "Ljava/awt/datatransfer/Clipboard;")
    public static Clipboard clipboard;

    @OriginalMember(owner = "client!client", name = "main", descriptor = "([Ljava/lang/String;)V")
    public static void main(@OriginalArg(0) String[] arg0) {
        try {
            if (arg0.length != 6) {
                Client.error("Argument count");
            }
            ConnectionInfo.game = new ConnectionInfo();
            ConnectionInfo.game.world = Integer.parseInt(arg0[0]);
            ConnectionInfo.lobby = new ConnectionInfo();
            ConnectionInfo.lobby.world = Integer.parseInt(arg0[1]);
            Client.modeWhere = ModeWhere.LOCAL;
            if (arg0[3].equals("live")) {
                Client.modeWhat = ModeWhat.LIVE;
            } else if (arg0[3].equals("rc")) {
                Client.modeWhat = ModeWhat.RC;
            } else if (arg0[3].equals("wip")) {
                Client.modeWhat = ModeWhat.WIP;
            } else {
                Client.error("modewhat");
            }
            Client.language = Client.languageIndex(arg0[4]);
            if (Client.language == -1) {
                if (arg0[4].equals("english")) {
                    Client.language = 0;
                } else if (arg0[4].equals("german")) {
                    Client.language = 1;
                } else {
                    Client.error("language");
                }
            }
            Client.objectTag = false;
            Client.js = false;
            if (arg0[5].equals("game0")) {
                Client.modeGame = ModeGame.RUNESCAPE;
            } else if (arg0[5].equals("game1")) {
                Client.modeGame = ModeGame.STELLAR_DAWN;
            } else if (arg0[5].equals("game2")) {
                Client.modeGame = ModeGame.GAME3;
            } else if (arg0[5].equals("game3")) {
                Client.modeGame = ModeGame.GAME4;
            } else {
                Client.error("game");
            }
            Client.affid = 0;
            Client.worldFlags = 0;
            Client.fromBilling = false;
            Client.sitesettingsMember = true;
            Client.isMember = true;
            Client.force64mb = false;
            Client.additionalInfo = null;
            Client.colourId = Client.modeGame.id;
            Client.settings = "";
            Client.ssKey = null;
            Client.country = 0;
            Client.userFlow = 0L;
            @Pc(241) client local241 = new client();
            aClient1 = local241;
            local241.method1635(Client.modeWhat.getId() + 32, Client.modeGame.domainName);
            frame.setLocation(40, 40);
        } catch (@Pc(265) Exception local265) {
            JagException.sendTrace(local265, null);
        }
    }

    @OriginalMember(owner = "client!dh", name = "a", descriptor = "(ZIII)Lclient!sb;")
    public static js5 createJs5(@OriginalArg(0) boolean discardpacked, @OriginalArg(1) int archiveId, @OriginalArg(2) int discardunpacked) {
        @Pc(5) FileSystem_Client fileSystem = null;
        if (Client.cacheDat != null) {
            fileSystem = new FileSystem_Client(archiveId, Client.cacheDat, Client.cacheIndexFiles[archiveId], 1000000);
        }
        Client.js5ResourceProviders[archiveId] = Static228.js5MasterIndex.getProvider(fileSystem, archiveId, Client.metaCache);
        Client.js5ResourceProviders[archiveId].requestMissing();
        return new js5(Client.js5ResourceProviders[archiveId], discardpacked, discardunpacked);
    }

    @OriginalMember(owner = "client!th", name = "b", descriptor = "(I)V")
    public static void cacheClean() {
        FloorOverlayTypeList.instance.cacheClean(5);
        FloorUnderlayTypeList.instance.cacheClean(5);
        IDKTypeList.instance.cacheClean(5);
        LocTypeList.instance.cacheClean(5);
        NPCTypeList.instance.cacheClean(5);
        ObjTypeList.instance.cacheClean(5);
        SeqTypeList.instance.cacheClean(5);
        SpotAnimationTypeList.instance.cacheClean(5);
        VarBitTypeListClient.instance.cacheClean(5);
        VarPlayerTypeListClient.instance.cacheClean(5);
        VarClanSettingTypeList.instance.cacheClean(5);
        VarClanTypeList.instance.cacheClean(5);
        BASTypeList.instance.cacheClean(5);
        MapElementTypeList.instance.cacheClean(5);
        MSITypeList.instance.cacheClean(5);
        ParamTypeList.instance.cacheClean(5);
        QuestTypeList.instance.cacheClean(5);
        SkyBoxTypeList.instance.cacheClean(5);
        SkyBoxSphereTypeList.instance.cacheClean(5);
        LightTypeList.instance.cacheClean(5);
        CursorTypeList.instance.cacheClean(5);
        StructTypeList.instance.cacheClean(5);
        HitmarkTypeList.instance.cacheClean(5);
        PlayerModel.cacheClean(5);
        Component.cacheClean(50);
        FontTypeList.cacheClean(50);
        PlayerEntity.cacheClean(5);
        ShadowList.cacheClean(5);
        Sprites.hitbarCache.clean(5);
        Sprites.timerbarCache.clean(5);
        Sprites.mobilisingArmiesCache.clean(5);
        MiniMenu.questCache.clean(5);
        ScriptRunner.A_WEIGHTED_CACHE___156.clean(5);
    }

    @OriginalMember(owner = "client!lc", name = "c", descriptor = "(B)V")
    public static void cacheReset() {
        FloorOverlayTypeList.instance.cacheReset();
        FloorUnderlayTypeList.instance.cacheReset();
        IDKTypeList.instance.cacheReset();
        LocTypeList.instance.cacheReset();
        NPCTypeList.instance.cacheReset();
        ObjTypeList.instance.cacheReset();
        SeqTypeList.instance.cacheReset();
        SpotAnimationTypeList.instance.cacheReset();
        VarBitTypeListClient.instance.cacheReset();
        VarPlayerTypeListClient.instance.cacheReset();
        VarClanSettingTypeList.instance.cacheReset();
        VarClanTypeList.instance.cacheReset();
        BASTypeList.instance.cacheReset();
        MSITypeList.instance.cacheReset();
        MapElementTypeList.instance.cacheReset();
        QuestTypeList.instance.cacheReset();
        ParamTypeList.instance.cacheReset();
        SkyBoxTypeList.instance.cacheReset();
        SkyBoxSphereTypeList.instance.cacheReset();
        LightTypeList.instance.cacheReset();
        CursorTypeList.instance.cacheReset();
        StructTypeList.instance.cacheReset();
        HitmarkTypeList.instance.cacheReset();
        PlayerModel.cacheReset();
        Component.cacheReset();
        FontTypeList.cacheReset();
        ClientInventory.cacheReset();
        if (Client.modeWhere != ModeWhere.LIVE) {
            for (@Pc(92) int i = 0; i < Static163.aByteArrayArray36.length; i++) {
                Static163.aByteArrayArray36[i] = null;
            }
            Static107.anInt2161 = 0;
        }
        Environment.cacheReset();
        PlayerEntity.cacheReset();
        ShadowList.cacheReset();
        ParticleEmitterTypeList.cacheReset();
        ParticleEffectorTypeList.cacheReset();
        ScriptRunner.A_WEIGHTED_CACHE___156.reset();
        Toolkit.active.cacheReset();
        ClientScriptList.cacheReset();
        Static112.method2109();
        js5.ANIMS.discardUnpacked();
        js5.BASES.discardUnpacked();
        js5.CONFIG.discardUnpacked();
        js5.INTERFACES.discardUnpacked();
        js5.SYNTH_SOUNDS.discardUnpacked();
        js5.MAPS.discardUnpacked();
        js5.MIDI_SONGS.discardUnpacked();
        js5.MODELS.discardUnpacked();
        js5.SPRITES.discardUnpacked();
        js5.TEXTURES.discardUnpacked();
        js5.BINARY.discardUnpacked();
        js5.MIDI_JINGLES.discardUnpacked();
        js5.CLIENTSCRIPTS.discardUnpacked();
        js5.FONTMETRICS.discardUnpacked();
        js5.VORBIS.discardUnpacked();
        js5.js5_15.discardUnpacked();
        js5.CONFIG_LOC.discardUnpacked();
        js5.CONFIG_ENUM.discardUnpacked();
        js5.CONFIG_NPC.discardUnpacked();
        js5.CONFIG_OBJ.discardUnpacked();
        js5.CONFIG_SEQ.discardUnpacked();
        js5.CONFIG_SPOT.discardUnpacked();
        js5.CONFIG_STRUCT.discardUnpacked();
        js5.WORLDMAPDATA.discardUnpacked();
        js5.QUICKCHAT.discardUnpacked();
        js5.QUICKCHAT_GLOBAL.discardUnpacked();
        js5.MATERIALS.discardUnpacked();
        js5.CONFIG_PARTICLE.discardUnpacked();
        js5.DEFAULTS.discardUnpacked();
        js5.CUTSCENES.discardUnpacked();
        js5.CONFIG_BILLBOARD.discardUnpacked();
        js5.DLLS.discardUnpacked();
        js5.SHADERS.discardUnpacked();
        js5.VIDEOS.discardUnpacked();
        Sprites.hitbarCache.reset();
        Sprites.timerbarCache.reset();
        Sprites.mobilisingArmiesCache.reset();
        MiniMenu.questCache.reset();
    }

    @OriginalMember(owner = "client!nja", name = "a", descriptor = "(IBLjava/lang/String;)Z")
    public static boolean connectTo(@OriginalArg(0) int world, @OriginalArg(2) String address) {
        if (signLink.signed) {
            ConnectionInfo.login = new ConnectionInfo();
            ConnectionInfo.login.world = world;
            ConnectionInfo.login.address = address;

            if (Client.modeWhere != ModeWhere.LIVE) {
                ConnectionInfo.login.secondaryPort = ConnectionInfo.login.world + Ports.SECONDARY_OFFSET;
                ConnectionInfo.login.primaryPort = ConnectionInfo.login.world + Ports.PRIMARY_OFFSET;
            }

            for (@Pc(45) int i = 0; i < WorldList.activeWorlds.length; i++) {
                if (WorldList.activeWorlds[i].id == world) {
                    Client.worldFlags = WorldList.activeWorlds[i].flags;
                }
            }
            return true;
        }

        @Pc(73) String port = "";
        if (ModeWhere.LIVE != Client.modeWhere) {
            port = ":" + (world + Ports.HTTP_OFFSET);
        }

        @Pc(88) String settings = "";
        if (Client.settings != null) {
            settings = "/p=" + Client.settings;
        }

        @Pc(152) String url = "http://" + address + port + "/l=" + Client.language + "/a=" + Client.affid + settings + "/j" + (Client.js ? "1" : "0") + ",o" + (Client.objectTag ? "1" : "0") + ",a2";
        try {
            aClient1.getAppletContext().showDocument(new URL(url), "_self");
            return true;
        } catch (@Pc(164) Exception local164) {
            return false;
        }
    }

    @OriginalMember(owner = "client!client", name = "i", descriptor = "(I)V")
    @Override
    public synchronized void addcanvas() {
        if (GameShell.loaderApplet != null && GameShell.canvas == null && !GameShell.signLink.microsoftjava) {
            try {
                @Pc(25) Class local25 = GameShell.loaderApplet.getClass();
                @Pc(31) Field local31 = local25.getDeclaredField("canvas");
                GameShell.canvas = (Canvas) local31.get(GameShell.loaderApplet);
                local31.set(GameShell.loaderApplet, null);
                if (GameShell.canvas != null) {
                    return;
                }
            } catch (@Pc(45) Exception local45) {
            }
        }
        super.addcanvas();
    }

    @OriginalMember(owner = "client!client", name = "j", descriptor = "(I)V")
    @Override
    protected void mainquit() {
        if (Static624.varcSaveRecommended) {
            Static266.saveVarcs();
        }

        Static419.method5757();

        if (Toolkit.active != null) {
            Toolkit.active.free();
        }

        if (GameShell.fsframe != null) {
            InterfaceManager.exitFullscreen(GameShell.signLink, GameShell.fsframe);
            GameShell.fsframe = null;
        }

        ServerConnection.GAME.close();
        ServerConnection.LOBBY.close();
        Static173.method2690();
        Client.js5WorkerThread.close();
        Static66.aCachedResourceWorker_1.close();

        if (Static151.aClass226_20 != null) {
            Static151.aClass226_20.method5243();
            Static151.aClass226_20 = null;
        }

        try {
            Client.cacheDat.close();
            for (@Pc(66) int i = 0; i < Js5Archive.ID_LIMIT; i++) {
                Client.cacheIndexFiles[i].close();
            }
            Client.metaFile.close();
            GameShell.uidDat.close();
            Static314.method4567();
        } catch (@Pc(91) Exception local91) {
        }
    }

    @OriginalMember(owner = "client!client", name = "q", descriptor = "(I)V")
    public void js5Tick() {
        if (Client.js5Errors < Client.js5WorkerThread.errors) {
            ConnectionInfo.login.rotateMethods();

            Client.netWorkerDelay = ((Client.js5WorkerThread.errors * 50) - 50) * 5;
            if (Client.netWorkerDelay > 3000) {
                Client.netWorkerDelay = 3000;
            }

            if (Client.js5WorkerThread.errors >= 2 && Client.js5WorkerThread.response == Js5ResponseCode.CONNECT_OUTOFDATE) {
                this.error("js5connect_outofdate");
                MainLogicManager.step = 16;
                return;
            }

            if (Client.js5WorkerThread.errors >= 4 && Client.js5WorkerThread.response == Js5ResponseCode.DISCONNECTED) {
                this.error("js5crc");
                MainLogicManager.step = 16;
                return;
            }

            if (Client.js5WorkerThread.errors >= 4 && MainLogicStep.isLoading(MainLogicManager.step)) {
                if (Client.js5WorkerThread.response == Js5ResponseCode.CONNECT_FULL1 || Client.js5WorkerThread.response == Js5ResponseCode.CONNECT_FULL2) {
                    this.error("js5connect_full");
                } else if (Client.js5WorkerThread.response <= 0) {
                    this.error("js5io");
                } else if (Client.netProxyError == null) {
                    this.error("js5connect");
                } else {
                    this.error("js5proxy_" + Client.netProxyError.trim());
                }
                MainLogicManager.step = MainLogicStep.STEP_ERROR;
                return;
            }
        }

        Client.js5Errors = Client.js5WorkerThread.errors;

        if (Client.netWorkerDelay > 0) {
            Client.netWorkerDelay--;
            return;
        }

        try {
            if (Client.js5State == Js5State.INIT) {
                Client.js5Socket = ConnectionInfo.login.openSocket(GameShell.signLink);
                Client.js5State++;
            }

            if (Client.js5State == Js5State.WAITING_CONNECTION_OPENED) {
                if (Client.js5Socket.status == SignedResourceStatus.ERROR) {
                    if (Client.js5Socket.result != null) {
                        Client.netProxyError = (String) Client.js5Socket.result;
                    }

                    this.updateJs5Response(1000);
                    return;
                }

                if (Client.js5Socket.status == SignedResourceStatus.SUCCESS) {
                    Client.js5State++;
                }
            }

            if (Client.js5State == Js5State.SEND_HEADER) {
                Client.js5BufferedSocket = new BufferedSocket((Socket) Client.js5Socket.result, GameShell.signLink, 25000);
                @Pc(251) Packet packet = new Packet(5);
                packet.p1(LoginProt.INIT_JS5REMOTE_CONNECTION.opcode);
                packet.p4(667);
                Client.js5BufferedSocket.write(5, packet.data);
                Client.js5State++;
                Client.js5HandshakeTime = SystemTimer.safetime();
            }

            if (Client.js5State == Js5State.WAITING_FIRST_RESPONSE) {
                if (MainLogicStep.isLoading(MainLogicManager.step) || Client.js5BufferedSocket.available() > 0) {
                    @Pc(296) int response = Client.js5BufferedSocket.read();
                    if (response != 0) {
                        this.updateJs5Response(response);
                        return;
                    }

                    Client.js5State++;
                } else if (SystemTimer.safetime() - Client.js5HandshakeTime > 30000L) {
                    this.updateJs5Response(1001);
                    return;
                }
            }

            if (Client.js5State == Js5State.WAITING_LOADING_REQUIREMENTS) {
                @Pc(356) boolean loggedOut = MainLogicStep.isLoading(MainLogicManager.step) || MainLogicStep.isLoggedOut(MainLogicManager.step) || MainLogicStep.isAtLobbyScreen(MainLogicManager.step);
                @Pc(359) LoadingRequirement[] requirements = LoadingRequirement.values();
                @Pc(367) Packet packet = new Packet(requirements.length * 4);
                Client.js5BufferedSocket.read(packet.data.length, 0, packet.data);
                for (@Pc(378) int i = 0; i < requirements.length; i++) {
                    requirements[i].setSize(packet.g4());
                }
                Client.js5WorkerThread.setSocket(!loggedOut, Client.js5BufferedSocket);
                Client.js5State = Js5State.INIT;
                Client.js5BufferedSocket = null;
                Client.js5Socket = null;
            }
        } catch (@Pc(417) IOException ignored) {
            this.updateJs5Response(1002);
        }
    }

    @OriginalMember(owner = "client!client", name = "p", descriptor = "(I)V")
    public void lobbyTick() {
        if (MainLogicManager.step == 7 && !LoginManager.inProgress() || MainLogicManager.step == 9 && LoginManager.gameLoginResponse == 42) {
            if (Static249.rebootTimer > 1) {
                Static249.rebootTimer--;
                Static321.lastMiscTransmit = World.tick;
            }

            if (!MiniMenu.open) {
                MiniMenu.reset();
            }

            for (@Pc(46) int local46 = 0; local46 < 100; local46++) {
                if (!Static236.readPacket(ServerConnection.LOBBY)) {
                    break;
                }
            }
        }

        Static35.currentTick++;
        InterfaceManager.setOptions(-1, -1, null);
        WorldMap.setOptions(-1, -1, null);
        Static443.method5981();
        World.tick++;

        for (@Pc(46) int i = 0; i < NPCList.newSize; i++) {
            @Pc(97) NPCEntity npc = NPCList.entities[i].npc;

            if (npc != null) {
                @Pc(103) byte movementCapabilities = npc.type.movementCapabilities;

                if ((movementCapabilities & 0x1) != 0) {
                    @Pc(114) int size = npc.getSize();

                    if ((movementCapabilities & 0x2) != 0 && npc.pathPointer == 0 && Math.random() * 1000.0D < 10.0D) {
                        @Pc(142) int deltaX = (int) Math.round(Math.random() * 10.0D - 5.0D);
                        @Pc(150) int deltaZ = (int) Math.round(Math.random() * 10.0D - 5.0D);
                        if (deltaX != 0 || deltaZ != 0) {
                            @Pc(166) int destX = deltaX + npc.pathX[0];
                            if (destX < 0) {
                                destX = 0;
                            } else if (destX > Static720.mapWidth - size - 1) {
                                destX = Static720.mapWidth - size - 1;
                            }

                            @Pc(203) int destZ = deltaZ + npc.pathZ[0];
                            if (destZ < 0) {
                                destZ = 0;
                            } else if (destZ > Static501.mapLength - size - 1) {
                                destZ = Static501.mapLength - size - 1;
                            }

                            @Pc(258) int pathLength = PathFinder.findPath(Client.collisionMaps[npc.level], PlayerEntity.runZ, PlayerEntity.runX, npc.pathX[0], npc.pathZ[0], size, destX, destZ, size, size, -1, 0, 0, true);
                            if (pathLength > 0) {
                                if (pathLength > 9) {
                                    pathLength = 9;
                                }

                                for (@Pc(274) int j = 0; j < pathLength; j++) {
                                    npc.pathX[j] = PlayerEntity.runX[pathLength - j - 1];
                                    npc.pathZ[j] = PlayerEntity.runZ[pathLength - j - 1];
                                    npc.pathSpeed[j] = MoveSpeed.WALK;
                                }

                                npc.pathPointer = pathLength;
                            }
                        }
                    }

                    Static256.movementTick(npc, true);
                    @Pc(142) int deltaYaw = Static112.turnTick(npc);
                    Static145.wornTargetTick(npc);
                    Static651.basTick(Static521.entityMoveSpeed, deltaYaw, Static524.entityMoveFlags, npc);
                    PathingEntity.updateActionAnimator(npc, Static521.entityMoveSpeed);
                    Static50.animationTick(npc);
                }
            }
        }

        if (((MainLogicManager.step == MainLogicStep.STEP_LOGIN_SCREEN) || (MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME) || (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN)) && (!LoginManager.inProgress() || ((MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME) && (LoginManager.gameLoginResponse == LoginResponseCode.IN_QUEUE))) && (LobbyManager.step == 0)) {
            if (Camera.mode == CameraMode.MODE_FOLLOWPLAYER) {
                Camera.moveToTick();
            } else {
                Camera.splineTick();
            }

            if (((Camera.x >> 9) < 14) || ((Camera.x >> 9) >= (Static720.mapWidth - 14)) || ((Camera.z >> 9) < 14) || ((Camera.z >> 9) >= (Static501.mapLength - 14))) {
                Static54.method1179();
            }
        }

        while (true) {
            @Pc(453) HookRequest local453;
            @Pc(458) Component local458;
            @Pc(470) Component local470;
            do {
                local453 = (HookRequest) Static618.A_DEQUE___68.removeFirst();
                if (local453 == null) {
                    while (true) {
                        do {
                            local453 = (HookRequest) Static59.A_DEQUE___33.removeFirst();
                            if (local453 == null) {
                                while (true) {
                                    do {
                                        local453 = (HookRequest) InterfaceManager.hookRequests.removeFirst();
                                        if (local453 == null) {
                                            if (InterfaceManager.dragSource != null) {
                                                Static603.method7899();
                                            }
                                            if (TimeUtils.clock % 1500 == 0) {
                                                Static314.tbrefresh();
                                            }
                                            if (MainLogicManager.step == 7 && !LoginManager.inProgress() || MainLogicManager.step == 9 && LoginManager.gameLoginResponse == 42) {
                                                MiniMenu.update();
                                            }
                                            Static587.method7704();
                                            if (Static624.varcSaveRecommended && SystemTimer.safetime() - TimeUtils.MILLISECONDS_PER_MINUTE > Static98.lastVarcSave) {
                                                Static266.saveVarcs();
                                            }
                                            for (@Pc(672) FriendNotification local672 = (FriendNotification) FriendsList.notifications.first(); local672 != null; local672 = (FriendNotification) FriendsList.notifications.next()) {
                                                if ((long) local672.arrivalTime < SystemTimer.safetime() / 1000L - 5L) {
                                                    if (local672.world > 0) {
                                                        ChatHistory.add(5, 0, "", "", "", local672.name + LocalisedText.FRIENDLOGIN.localise(Client.language));
                                                    }
                                                    if (local672.world == 0) {
                                                        ChatHistory.add(5, 0, "", "", "", local672.name + LocalisedText.FRIENDLOGOUT.localise(Client.language));
                                                    }
                                                    local672.unlink();
                                                }
                                            }
                                            if (MainLogicManager.step == 7 && !LoginManager.inProgress() || MainLogicManager.step == 9 && LoginManager.gameLoginResponse == 42) {
                                                if (MainLogicManager.step != 9 && ServerConnection.LOBBY.connection == null) {
                                                    LoginManager.logout(false);
                                                    return;
                                                }
                                                if (ServerConnection.LOBBY != null) {
                                                    ServerConnection.LOBBY.idleWriteTicks++;
                                                    if (ServerConnection.LOBBY.idleWriteTicks > 50) {
                                                        @Pc(823) ClientMessage local823 = ClientMessage.create(ClientProt.NO_TIMEOUT, ServerConnection.LOBBY.isaac);
                                                        ServerConnection.LOBBY.send(local823);
                                                    }
                                                    try {
                                                        ServerConnection.LOBBY.flush();
                                                        return;
                                                    } catch (@Pc(832) IOException local832) {
                                                        if (MainLogicManager.step != 9) {
                                                            LoginManager.logout(false);
                                                            return;
                                                        }
                                                        ServerConnection.LOBBY.close();
                                                        return;
                                                    }
                                                }
                                            }
                                            return;
                                        }
                                        local458 = local453.source;
                                        if (local458.id < 0) {
                                            break;
                                        }
                                        local470 = InterfaceList.list(local458.layer);
                                    } while (local470 == null || local470.staticComponents == null || local470.staticComponents.length <= local458.id || local470.staticComponents[local458.id] != local458);
                                    ScriptRunner.executeHookInner(local453);
                                }
                            }
                            local458 = local453.source;
                            if (local458.id < 0) {
                                break;
                            }
                            local470 = InterfaceList.list(local458.layer);
                        } while (local470 == null || local470.staticComponents == null || local470.staticComponents.length <= local458.id || local458 != local470.staticComponents[local458.id]);
                        ScriptRunner.executeHookInner(local453);
                    }
                }
                local458 = local453.source;
                if (local458.id < 0) {
                    break;
                }
                local470 = InterfaceList.list(local458.layer);
            } while (local470 == null || local470.staticComponents == null || local470.staticComponents.length <= local458.id || local458 != local470.staticComponents[local458.id]);
            ScriptRunner.executeHookInner(local453);
        }
    }

    @OriginalMember(owner = "client!client", name = "m", descriptor = "(I)V")
    public void tickJs5() {
        @Pc(7) boolean workerTick = Client.js5WorkerThread.tick();

        if (!workerTick) {
            this.js5Tick();
        }
    }

    @OriginalMember(owner = "client!client", name = "e", descriptor = "(B)V")
    @Override
    protected void cleanup() {
        /* empty */
    }

    @OriginalMember(owner = "client!client", name = "f", descriptor = "(B)V")
    public void mainredraw() {
        if (MainLogicManager.step == MainLogicStep.STEP_ERROR) {
            return;
        }

        @Pc(20) long local20 = (Static271.method3929() / 1000000L) - Static206.aLong114;
        Static206.aLong114 = Static271.method3929() / 1000000L;

        @Pc(28) boolean local28 = Static576.method7611();
        if (local28 && Static501.aBoolean575 && Static719.aPcmPlayer_5 != null) {
            Static719.aPcmPlayer_5.method3592();
        }

        if (MainLogicStep.method6445(MainLogicManager.step)) {
            if (Client.nextWindowModeChange != 0L && SystemTimer.safetime() > Client.nextWindowModeChange) {
                InterfaceManager.changeWindowMode(InterfaceManager.getWindowMode(), Client.nextWindowModeWidth, Client.nextWindowModeHeight, false);
            } else if (!Toolkit.active.method8001() && GameShell.canvasReplaceRecommended) {
                Static574.method7572();
            }
        }

        if (GameShell.fsframe == null) {
            @Pc(98) Container topContainer;
            if (frame != null) {
                topContainer = frame;
            } else if (GameShell.loaderApplet == null) {
                topContainer = GameShell.instance;
            } else {
                topContainer = GameShell.loaderApplet;
            }

            @Pc(110) int width = topContainer.getSize().width;
            @Pc(114) int height = topContainer.getSize().height;
            if (topContainer == frame) {
                @Pc(120) Insets insets = frame.getInsets();
                height -= insets.bottom + insets.top;
                width -= insets.right + insets.left;
            }

            if (GameShell.frameWid != width || height != GameShell.frameHei || Client.changingWindowMode) {
                if (Toolkit.active == null || Toolkit.active.method7983()) {
                    Static712.method9329((byte) 11);
                } else {
                    GameShell.frameWid = width;
                    GameShell.frameHei = height;
                }

                Client.nextWindowModeChange = SystemTimer.safetime() + 500L;
                Client.changingWindowMode = false;
            }
        }

        if (GameShell.fsframe != null && !GameShell.focus && MainLogicStep.method6445(MainLogicManager.step)) {
            InterfaceManager.changeWindowMode(ClientOptions.instance.screenSizeDefault.getValue(), -1, -1, false);
        }

        @Pc(209) boolean local209 = false;
        if (fullredraw) {
            local209 = true;
            fullredraw = false;
        }

        if (local209) {
            Static288.repaintMargins();
        }

        if (Toolkit.active != null && Toolkit.active.method8001() || InterfaceManager.getWindowMode() != 1) {
            InterfaceManager.redrawAll();
        }

        if (MainLogicStep.isLoading(MainLogicManager.step)) {
            Static523.method3447(local209);
        } else if (MainLogicStep.isAtLoadingScreen(MainLogicManager.step)) {
            InterfaceManager.method680();
        } else if (MainLogicStep.method5393(MainLogicManager.step)) {
            InterfaceManager.method680();
        } else if (MainLogicStep.isBuildingMap(MainLogicManager.step)) {
            if (Static213.anInt3472 == 1) {
                if (Static593.anInt8763 > Static357.anInt6508) {
                    Static357.anInt6508 = Static593.anInt8763;
                }

                @Pc(110) int local110 = (Static357.anInt6508 - Static593.anInt8763) * 50 / Static357.anInt6508;
                MessageBox.draw(Toolkit.active, LocalisedText.LOADING.localise(Client.language) + "<br>(" + local110 + "%)", true, Fonts.p12Metrics, Fonts.p12);
            } else if (Static213.anInt3472 == 2) {
                if (Static13.anInt150 > Static440.anInt6683) {
                    Static440.anInt6683 = Static13.anInt150;
                }

                @Pc(110) int local110 = (Static440.anInt6683 - Static13.anInt150) * 50 / Static440.anInt6683 + 50;
                MessageBox.draw(Toolkit.active, LocalisedText.LOADING.localise(Client.language) + "<br>(" + local110 + "%)", true, Fonts.p12Metrics, Fonts.p12);
            } else {
                MessageBox.draw(Toolkit.active, LocalisedText.LOADING.localise(Client.language), true, Fonts.p12Metrics, Fonts.p12);
            }
        } else if (MainLogicManager.step == 11) {
            InterfaceManager.method7930(local20);
        } else if (MainLogicManager.step == 14) {
            MessageBox.draw(Toolkit.active, LocalisedText.CONLOST.localise(Client.language) + "<br>" + LocalisedText.ATTEMPTING_TO_REESTABLISH.localise(Client.language), false, Fonts.p12Metrics, Fonts.p12);
        } else if (MainLogicManager.step == 15) {
            MessageBox.draw(Toolkit.active, LocalisedText.PLEASEWAIT.localise(Client.language), false, Fonts.p12Metrics, Fonts.p12);
        }

        if (InterfaceManager.rectDebug == 3) {
            for (@Pc(110) int local110 = 0; local110 < InterfaceManager.rectangleCount; local110++) {
                @Pc(478) Rectangle rectangle = InterfaceManager.rectangles[local110];

                if (InterfaceManager.currentlyDirtyRect[local110]) {
                    Toolkit.active.outlineRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 0xFFFF00FF);
                } else if (InterfaceManager.flipDirtyRect[local110]) {
                    Toolkit.active.outlineRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 0xFFFF0000);
                } else {
                    Toolkit.active.outlineRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 0xFF00FF00);
                }
            }
        }

        if (debugconsole.isOpen()) {
            debugconsole.draw(Toolkit.active);
        }

        if (GameShell.signLink.microsoftjava && MainLogicStep.method6445(MainLogicManager.step) && InterfaceManager.rectDebug == 0 && InterfaceManager.getWindowMode() == 1 && !local209) {
            @Pc(110) int rectangle = 0;

            for (@Pc(114) int i = 0; i < InterfaceManager.rectangleCount; i++) {
                if (InterfaceManager.flipDirtyRect[i]) {
                    InterfaceManager.flipDirtyRect[i] = false;
                    InterfaceManager.flippedDirtyRects[rectangle++] = InterfaceManager.rectangles[i];
                }
            }

            try {
                if (OrthoMode.toolkitActive) {
                    OrthoMode.flipDirtyRect(rectangle, InterfaceManager.flippedDirtyRects);
                } else {
                    Toolkit.active.flipDirtyRect(InterfaceManager.flippedDirtyRects, rectangle);
                }
            } catch (@Pc(629) FlipException ignored) {
                /* empty */
            }
        } else if (!MainLogicStep.isLoading(MainLogicManager.step)) {
            for (@Pc(110) int i = 0; i < InterfaceManager.rectangleCount; i++) {
                InterfaceManager.flipDirtyRect[i] = false;
            }

            try {
                if (OrthoMode.toolkitActive) {
                    OrthoMode.flip();
                } else {
                    Toolkit.active.flip();
                }
            } catch (@Pc(666) FlipException cause) {
                JagException.sendTrace(cause, cause.getMessage() + " (Recovered) " + this.getErrorTrace());
                Static32.setToolkit(ToolkitType.JAVA, false);
            }
        }

        VideoManager.unpause();

        @Pc(110) int cpuUsage = ClientOptions.instance.cpuUsage.value();
        if (cpuUsage == 0) {
            TimeUtils.sleep(15L);
        } else if (cpuUsage == 1) {
            TimeUtils.sleep(10L);
        } else if (cpuUsage == 2) {
            TimeUtils.sleep(5L);
        } else if (cpuUsage == 3) {
            TimeUtils.sleep(2L);
        }

        if (Client.cleanCaches) {
            cacheClean();
        }

        if (ClientOptions.instance.safeMode.getValue() == 1 && MainLogicManager.step == 3 && InterfaceManager.topLevelInterface != -1) {
            ClientOptions.instance.update(0, ClientOptions.instance.safeMode);
            ClientOptions.save();
        }
    }

    @OriginalMember(owner = "client!client", name = "a", descriptor = "(ZI)V")
    public void updateJs5Response(@OriginalArg(1) int response) {
        Client.js5Socket = null;
        Client.js5WorkerThread.response = response;
        Client.js5BufferedSocket = null;
        Client.js5WorkerThread.errors++;
        Client.js5State = 0;
    }

    @OriginalMember(owner = "client!client", name = "k", descriptor = "(I)V")
    @Override
    protected void mainloop() {
        if (ClientOptions.instance.toolkit.getValue() != ToolkitType.SSE) {
            this.gameTick();
            return;
        }

        try {
            this.gameTick();
        } catch (@Pc(21) ThreadDeath death) {
            throw death;
        } catch (@Pc(24) Throwable cause) {
            JagException.sendTrace(cause, cause.getMessage() + " (Recovered) " + this.getErrorTrace());
            Static171.graphicsError = true;
            Static32.setToolkit(ToolkitType.JAVA, false);
        }
    }

    @OriginalMember(owner = "client!client", name = "o", descriptor = "(I)V")
    public void gameTick() {
        if (MainLogicManager.step == 16) {
            return;
        }

        TimeUtils.clock++;

        if ((TimeUtils.clock % 1000) == 1) {
            @Pc(27) GregorianCalendar calendar = new GregorianCalendar();
            MiniMenu.randomSeed = (calendar.get(Calendar.HOUR_OF_DAY) * 600) + (calendar.get(Calendar.MINUTE) * 10) + (calendar.get(Calendar.SECOND) / 6);
            MiniMenu.random.setSeed(MiniMenu.randomSeed);
        }

        ServerConnection.GAME.recordStats();
        ServerConnection.LOBBY.recordStats();
        this.tickJs5();

        if (Static228.js5MasterIndex != null) {
            Static228.js5MasterIndex.process();
        }

        Static601.method7865();
        VideoManager.tick();
        KeyboardMonitor.instance.record();
        MouseMonitor.instance.record();

        if (Toolkit.active != null) {
            Toolkit.active.tick((int) SystemTimer.safetime());
        }

        ClientURLTools.tick();
        Static671.anInt10026 = 0;
        Static216.keyPressCount = 0;

        for (@Pc(94) KeyLog log = KeyboardMonitor.instance.removeFirstRecorded(); log != null; log = KeyboardMonitor.instance.removeFirstRecorded()) {
            @Pc(102) int type = log.getType();

            if (type == KeyLog.TYPE_HELD || type == KeyLog.TYPE_TYPED) {
                @Pc(118) char keyChar = log.getKeyChar();

                if (MainLogicManager.isNotLoading() && (keyChar == '`' || keyChar == '§' || keyChar == '²')) {
                    if (debugconsole.isOpen()) {
                        debugconsole.close();
                    } else {
                        debugconsole.open();
                    }
                } else if (Static671.anInt10026 < 128) {
                    Static194.AN_KEYBOARD_EVENT_ARRAY_1[Static671.anInt10026] = log;
                    Static671.anInt10026++;
                }
            } else if (type == 0 && Static216.keyPressCount < 75) {
                Static591.AN_KEYBOARD_EVENT_ARRAY_2[Static216.keyPressCount] = log;
                Static216.keyPressCount++;
            }
        }

        Static611.mouseWheelRotation = 0;
        for (@Pc(214) MouseLog log = MouseMonitor.instance.removeFirstLog(); log != null; log = MouseMonitor.instance.removeFirstLog()) {
            @Pc(222) int type = log.getType();

            if (type == MouseLog.TYPE_RESET) {
                Static677.mouseMovements.addLast(log);
            } else if (type == MouseLog.TYPE_SCROLL) {
                Static611.mouseWheelRotation += log.getExtra();
            } else if (MouseLog.isPress(type)) {
                Static226.mouseLogs.addLast(log);

                if (Static226.mouseLogs.size() > 10) {
                    Static226.mouseLogs.removeFirst();
                }
            }
        }

        if (debugconsole.isOpen()) {
            Static668.method8703();
        }

        if (MainLogicStep.isLoading(MainLogicManager.step)) {
            Loading.update();
            Static199.doneslowupdate();
        } else if (MainLogicStep.isBuildingMap(MainLogicManager.step)) {
            Static489.method6548();
        }

        if (MainLogicStep.isLoggedOut(MainLogicManager.step) && !MainLogicStep.isBuildingMap(MainLogicManager.step)) {
            this.lobbyTick();
            LobbyManager.changeMainState();
            LoginManager.changeMainState();
        } else if (MainLogicStep.isAtLobbyScreen(MainLogicManager.step) && !MainLogicStep.isBuildingMap(MainLogicManager.step)) {
            this.lobbyTick();
            LoginManager.changeMainState();
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_GAMESCREEN_TO_LOBBY) {
            LoginManager.changeMainState();
        } else if (MainLogicStep.isAtGameScreen(MainLogicManager.step) && !MainLogicStep.isBuildingMap(MainLogicManager.step)) {
            MainLogicManager.changeMainState();
        } else if (MainLogicManager.step == MainLogicStep.STEP_RECONNECTING || MainLogicManager.step == MainLogicStep.STEP_SWITCH_WORLD) {
            LoginManager.changeMainState();

            if (LoginManager.gameLoginResponse != LoginResponseCode.LOGGING_IN && LoginManager.gameLoginResponse != LoginResponseCode.OK && LoginManager.gameLoginResponse != LoginResponseCode.RECONNECT_OK) {
                if (MainLogicManager.step == MainLogicStep.STEP_SWITCH_WORLD) {
                    LoginManager.lastGameLoginResponse = LoginManager.gameLoginResponse;
                    LoginManager.lastDisallowTrigger = LoginManager.disallowTrigger;
                    LoginManager.lastDisallowResult = LoginManager.disallowResult;

                    if (LoginManager.reconnectToPrevious) {
                        connectTo(ConnectionInfo.previous.world, ConnectionInfo.previous.address);
                        ServerConnection.GAME.connection = null;
                        MainLogicManager.setStep(MainLogicStep.STEP_RECONNECTING);
                    } else {
                        LoginManager.logout(InterfaceManager.lobbyOpened);
                    }
                } else {
                    LoginManager.logout(false);
                }
            }
        }

        Static369.updateObjSprites(Toolkit.active);
        Static226.mouseLogs.removeFirst();
    }

    @OriginalMember(owner = "client!client", name = "h", descriptor = "(I)V")
    @Override
    protected void maininit() {
        if (Client.force64mb) {
            GameShell.maxmemory = 64;
        }

        @Pc(18) Frame frame = new Frame("Jagex");
        frame.pack();
        frame.dispose();

        Static712.method9329((byte) 11);
        Static66.aCachedResourceWorker_1 = new CachedResourceWorker(GameShell.signLink);
        Client.js5WorkerThread = new Js5WorkerThread();
        Static545.method7241(new int[]{20, 260}, new int[]{1000, 100});

        if (ModeWhere.LIVE != Client.modeWhere) {
            Static163.aByteArrayArray36 = new byte[50][];
        }

        ClientOptions.instance = Static720.method9398();

        if (Client.modeWhere == ModeWhere.LIVE) {
            ConnectionInfo.game.address = this.getCodeBase().getHost();
        } else if (ModeWhere.isPrivate(Client.modeWhere)) {
            ConnectionInfo.game.address = this.getCodeBase().getHost();
            ConnectionInfo.game.secondaryPort = ConnectionInfo.game.world + Ports.SECONDARY_OFFSET;
            ConnectionInfo.game.primaryPort = ConnectionInfo.game.world + Ports.PRIMARY_OFFSET;
            ConnectionInfo.lobby.secondaryPort = ConnectionInfo.lobby.world + Ports.SECONDARY_OFFSET;
            ConnectionInfo.lobby.primaryPort = ConnectionInfo.lobby.world + Ports.PRIMARY_OFFSET;
        } else if (ModeWhere.LOCAL == Client.modeWhere) {
            ConnectionInfo.game.address = "127.0.0.1";
            ConnectionInfo.game.secondaryPort = ConnectionInfo.game.world + Ports.SECONDARY_OFFSET;
            ConnectionInfo.lobby.address = "127.0.0.1";
            ConnectionInfo.game.primaryPort = ConnectionInfo.game.world + Ports.PRIMARY_OFFSET;
            ConnectionInfo.lobby.secondaryPort = ConnectionInfo.lobby.world + Ports.SECONDARY_OFFSET;
            ConnectionInfo.lobby.primaryPort = ConnectionInfo.lobby.world + Ports.PRIMARY_OFFSET;
        }

        ConnectionInfo.login = ConnectionInfo.game;
        Client.clientpalette = LocType.clientpalette = NPCType.clientpalette = ObjType.clientpalette = new short[256];

        if (Client.modeGame == ModeGame.RUNESCAPE) {
            MiniMenu.ignorePlayerLevels = false;
        }

        try {
            clipboard = aClient1.getToolkit().getSystemClipboard();
        } catch (@Pc(183) Exception ignored) {
            /* empty */
        }

        KeyboardMonitor.instance = KeyboardMonitor.create(GameShell.canvas);
        MouseMonitor.instance = MouseMonitor.create(GameShell.canvas);

        try {
            if (GameShell.signLink.cacheDat != null) {
                Client.cacheDat = new BufferedFile(GameShell.signLink.cacheDat, 5200, 0);
                for (@Pc(205) int i = 0; i < 37; i++) {
                    Client.cacheIndexFiles[i] = new BufferedFile(GameShell.signLink.cacheIndex[i], 6000, 0);
                }
                Client.metaFile = new BufferedFile(GameShell.signLink.masterIndex, 6000, 0);
                Client.metaCache = new FileSystem_Client(Js5Archive.ARCHIVESET, Client.cacheDat, Client.metaFile, 500000);
                GameShell.uidDat = new BufferedFile(GameShell.signLink.uidFile, 24, 0);
                GameShell.signLink.masterIndex = null;
                GameShell.signLink.cacheIndex = null;
                GameShell.signLink.cacheDat = null;
                GameShell.signLink.uidFile = null;
            }
        } catch (@Pc(275) IOException ignored) {
            Client.metaFile = null;
            Client.metaCache = null;
            GameShell.uidDat = null;
            Client.cacheDat = null;
        }

        if (ModeWhere.LIVE != Client.modeWhere) {
            Client.displayFps = true;
        }

        GameShell.loadingTitle = LocalisedText.LOADING.localise(Client.language);
    }

    @OriginalMember(owner = "client!client", name = "a", descriptor = "(I)Ljava/lang/String;")
    @Override
    public String getErrorTrace() {
        @Pc(5) String trace = null;
        try {
            trace = "[1)" + WorldMap.areaBaseX + "," + WorldMap.areaBaseZ + "," + Static720.mapWidth + "," + Static501.mapLength + "|";

            if (PlayerEntity.self != null) {
                trace = trace + "2)" + Camera.renderingLevel + "," + (PlayerEntity.self.pathX[0] + WorldMap.areaBaseX) + "," + (WorldMap.areaBaseZ + PlayerEntity.self.pathZ[0]) + "|";
            }

            trace = trace + "3)" + ClientOptions.instance.toolkit.getValue() + "|4)" + ClientOptions.instance.antialiasingMode.getValue() + "|5)" + InterfaceManager.getWindowMode() + "|6)" + GameShell.canvasWid + "," + GameShell.canvasHei + "|";
            trace = trace + "7)" + ClientOptions.instance.lightDetail.getValue() + "|";
            trace = trace + "8)" + ClientOptions.instance.hardShadows.getValue() + "|";
            trace = trace + "9)" + ClientOptions.instance.waterDetail.getValue() + "|";
            trace = trace + "10)" + ClientOptions.instance.textures.getValue() + "|";
            trace = trace + "11)" + ClientOptions.instance.bloom.getValue() + "|";
            trace = trace + "12)" + ClientOptions.instance.animateBackground.getValue() + "|";
            trace = trace + "13)" + GameShell.maxmemory + "|";
            trace = trace + "14)" + MainLogicManager.step;

            if (SystemInfo.instance != null) {
                trace = trace + "|15)" + SystemInfo.instance.totalMemory;
            }

            try {
                if (ClientOptions.instance.toolkit.getValue() == ToolkitType.SSE) {
                    @Pc(273) Class local273 = Class.forName("java.lang.ClassLoader");
                    @Pc(279) Field local279 = local273.getDeclaredField("nativeLibraries");
                    @Pc(284) Class local284 = Class.forName("java.lang.reflect.AccessibleObject");
                    @Pc(296) Method local296 = local284.getDeclaredMethod("setAccessible", Boolean.TYPE);
                    local296.invoke(local279, Boolean.TRUE);
                    @Pc(322) Vector local322 = (Vector) local279.get((Static84.aClass5 == null ? (Static84.aClass5 = Class.forName("client")) : Static84.aClass5).getClassLoader());
                    for (@Pc(324) int local324 = 0; local324 < local322.size(); local324++) {
                        try {
                            @Pc(329) Object local329 = local322.elementAt(local324);
                            @Pc(336) Field local336 = local329.getClass().getDeclaredField("name");
                            local296.invoke(local336, Boolean.TRUE);
                            try {
                                @Pc(351) String local351 = (String) local336.get(local329);
                                if (local351 != null && local351.indexOf("sw3d.dll") != -1) {
                                    @Pc(369) Field local369 = local329.getClass().getDeclaredField("handle");
                                    local296.invoke(local369, Boolean.TRUE);
                                    trace = trace + "|16)" + Long.toHexString(local369.getLong(local329));
                                    local296.invoke(local369, Boolean.FALSE);
                                }
                            } catch (@Pc(407) Throwable local407) {
                            }
                            local296.invoke(local336, Boolean.FALSE);
                        } catch (@Pc(419) Throwable local419) {
                        }
                    }
                }
            } catch (@Pc(431) Throwable local431) {
            }
            trace = trace + "]";
        } catch (@Pc(442) Throwable local442) {
        }
        return trace;
    }

    @OriginalMember(owner = "client!client", name = "init", descriptor = "()V")
    @Override
    public void init() {
        if (!this.checkhost()) {
            return;
        }

        ConnectionInfo.game = new ConnectionInfo();
        ConnectionInfo.game.world = Integer.parseInt(this.getParameter("worldid"));

        ConnectionInfo.lobby = new ConnectionInfo();
        ConnectionInfo.lobby.world = Integer.parseInt(this.getParameter("lobbyid"));
        ConnectionInfo.lobby.address = this.getParameter("lobbyaddress");

        Client.modeWhere = ModeWhere.fromId(Integer.parseInt(this.getParameter("modewhere")));
        if (ModeWhere.LOCAL == Client.modeWhere) {
            Client.modeWhere = ModeWhere.WIP;
        } else if (!ModeWhere.isPrivate(Client.modeWhere) && ModeWhere.LIVE != Client.modeWhere) {
            Client.modeWhere = ModeWhere.LIVE;
        }

        Client.modeWhat = ModeWhat.fromId(Integer.parseInt(this.getParameter("modewhat")));
        if (ModeWhat.WIP != Client.modeWhat && Client.modeWhat != ModeWhat.RC && ModeWhat.LIVE != Client.modeWhat) {
            Client.modeWhat = ModeWhat.LIVE;
        }

        try {
            Client.language = Integer.parseInt(this.getParameter("lang"));
        } catch (@Pc(110) Exception local110) {
            Client.language = 0;
        }

        @Pc(118) String objectTagParam = this.getParameter("objecttag");
        if (objectTagParam != null && objectTagParam.equals("1")) {
            Client.objectTag = true;
        } else {
            Client.objectTag = false;
        }

        @Pc(142) String jsParam = this.getParameter("js");
        if (jsParam != null && jsParam.equals("1")) {
            Client.js = true;
        } else {
            Client.js = false;
        }

        @Pc(166) String advertParam = this.getParameter("advert");
        if (advertParam != null && advertParam.equals("1")) {
            Client.advert = true;
        } else {
            Client.advert = false;
        }

        @Pc(190) String gameParam = this.getParameter("game");
        if (gameParam != null) {
            if (gameParam.equals("0")) {
                Client.modeGame = ModeGame.RUNESCAPE;
            } else if (gameParam.equals("1")) {
                Client.modeGame = ModeGame.STELLAR_DAWN;
            } else if (gameParam.equals("2")) {
                Client.modeGame = ModeGame.GAME3;
            } else if (gameParam.equals("3")) {
                Client.modeGame = ModeGame.GAME4;
            }
        }

        try {
            Client.affid = Integer.parseInt(this.getParameter("affid"));
        } catch (@Pc(247) Exception ignored) {
            Client.affid = 0;
        }

        Client.quitUrl = this.getParameter("quiturl");

        Client.settings = this.getParameter("settings");
        if (Client.settings == null) {
            Client.settings = "";
        }

        Client.under13 = "1".equals(this.getParameter("under"));

        @Pc(281) String countryParam = this.getParameter("country");
        if (countryParam != null) {
            try {
                Client.country = Integer.parseInt(countryParam);
            } catch (@Pc(288) Exception ignored) {
                Client.country = 0;
            }
        }

        Client.colourId = Integer.parseInt(this.getParameter("colourid"));
        if (Client.colourId < 0 || Client.colourId >= Client.FILL_COLOURS.length) {
            Client.colourId = 0;
        }

        if (Integer.parseInt(this.getParameter("sitesettings_member")) == 1) {
            Client.sitesettingsMember = true;
            Client.isMember = true;
        }

        @Pc(336) String fromBillingParam = this.getParameter("frombilling");
        if (fromBillingParam != null && fromBillingParam.equals("true")) {
            Client.fromBilling = true;
        }

        @Pc(356) String ssKeyParam = this.getParameter("sskey");
        if (ssKeyParam != null) {
            Client.ssKey = Base64.encode(WebTools.urlEncode(ssKeyParam));

            if (Client.ssKey.length < 16) {
                Client.ssKey = null;
            }
        }

        @Pc(382) String force64mbParam = this.getParameter("force64mb");
        if (force64mbParam != null && force64mbParam.equals("true")) {
            Client.force64mb = true;
        }

        @Pc(402) String worldflagsParam = this.getParameter("worldflags");
        if (worldflagsParam != null) {
            try {
                Client.worldFlags = Integer.parseInt(worldflagsParam);
            } catch (@Pc(409) Exception ignored) {
                /* empty */
            }
        }

        @Pc(416) String userFlowParam = this.getParameter("userFlow");
        if (userFlowParam != null) {
            try {
                Client.userFlow = Long.parseLong(userFlowParam);
            } catch (@Pc(424) NumberFormatException ignored) {
                /* empty */
            }
        }

        Client.additionalInfo = this.getParameter("additionalInfo");
        if (Client.additionalInfo != null && Client.additionalInfo.length() > 50) {
            Client.additionalInfo = null;
        }

        if (ModeGame.RUNESCAPE == Client.modeGame) {
            Client.loadingScreenWidth = 765;
            Client.loadingScreenHeight = 503;
        } else if (Client.modeGame == ModeGame.STELLAR_DAWN) {
            Client.loadingScreenHeight = 480;
            Client.loadingScreenWidth = 640;
        }

        @Pc(473) String hcParam = this.getParameter("hc");
        if (hcParam != null && hcParam.equals("1")) {
            Client.hc = true;
        }

        aClient1 = this;
        this.startApplet(Client.BUILD, Client.loadingScreenWidth, Client.modeWhat.getId() + 32, Client.modeGame.domainName, Js5Archive.ID_LIMIT, Client.loadingScreenHeight);
    }

    @OriginalMember(owner = "client!client", name = "c", descriptor = "(I)V")
    @Override
    protected void draw() {
        if (ClientOptions.instance.toolkit.getValue() != ToolkitType.SSE) {
            this.mainredraw();
            return;
        }

        try {
            this.mainredraw();
        } catch (@Pc(21) ThreadDeath death) {
            throw death;
        } catch (@Pc(24) Throwable cause) {
            JagException.sendTrace(cause, cause.getMessage() + " (Recovered) " + this.getErrorTrace());
            Static171.graphicsError = true;
            Static32.setToolkit(ToolkitType.JAVA, false);
        }
    }
}

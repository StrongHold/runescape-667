import com.jagex.Client;
import com.jagex.LoginProt;
import com.jagex.ServerProt;
import com.jagex.StockmarketOffer;
import com.jagex.core.constants.LoginResponseCode;
import com.jagex.core.constants.LoginStep;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.constants.ModeWhere;
import com.jagex.core.constants.Ports;
import com.jagex.core.crypto.Isaac;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.io.BitPacket;
import com.jagex.core.io.ConnectionInfo;
import com.jagex.core.io.Packet;
import com.jagex.core.io.connection.Connection;
import com.jagex.core.stringtools.general.Base37;
import com.jagex.core.util.JavaScript;
import com.jagex.core.util.SystemTimer;
import com.jagex.game.DelayedStateChange;
import com.jagex.game.LocalisedText;
import com.jagex.game.camera.CameraMode;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.iftype.SubInterface;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.js5;
import com.jagex.sign.SignedResourceStatus;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.loading.library.LibraryManager;
import rs2.client.web.ClientURLTools;
import rs2.client.web.OpenUrlType;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.URL;

public final class LoginManager {

    private static final int TYPE_LOBBY = 1;

    private static final int TYPE_GAME = 2;

    @OriginalMember(owner = "client!rw", name = "B", descriptor = "J")
    public static final long clientSessionKey = (long) (Math.random() * 9.999999999E9D);

    @OriginalMember(owner = "client!ee", name = "M", descriptor = "I")
    public static int step = LoginStep.DELAY;

    @OriginalMember(owner = "client!iu", name = "b", descriptor = "I")
    public static int lobbyLoginResponse = LoginResponseCode.DEFAULT;

    @OriginalMember(owner = "client!fe", name = "p", descriptor = "I")
    public static int gameLoginResponse = LoginResponseCode.DEFAULT;

    @OriginalMember(owner = "client!or", name = "Z", descriptor = "I")
    public static int anInt7113 = -1;

    @OriginalMember(owner = "client!bv", name = "l", descriptor = "Ljava/lang/String;")
    public static String password = "";

    @OriginalMember(owner = "client!od", name = "d", descriptor = "Ljava/lang/String;")
    public static String username = "";

    @OriginalMember(owner = "client!eaa", name = "o", descriptor = "I")
    public static int socialNetworkId = -1;

    @OriginalMember(owner = "client!kba", name = "P", descriptor = "Ljava/lang/String;")
    public static String previousUsername = "";

    @OriginalMember(owner = "client!cja", name = "l", descriptor = "I")
    public static int pendingResponse;

    @OriginalMember(owner = "client!nja", name = "N", descriptor = "J")
    public static long ssoKey = 0L;

    @OriginalMember(owner = "client!cha", name = "h", descriptor = "I")
    public static int errors = 0;

    @OriginalMember(owner = "client!jia", name = "a", descriptor = "I")
    public static int type;

    @OriginalMember(owner = "client!kha", name = "m", descriptor = "I")
    public static int ticks = 0;

    @OriginalMember(owner = "client!js", name = "P", descriptor = "Z")
    public static boolean socialNetworkLogin = false;

    @OriginalMember(owner = "client!lp", name = "b", descriptor = "Ljava/math/BigInteger;")
    public static BigInteger RSA_MODULUS = new BigInteger("a76cba054be8a8cb683bf47c5e5b4950b60647f74da5ea7d87f0ba7d24bb6580dec4809afa07e26db0d0c88ca41bdb697fc6ae0def8afc0bacd841bb57fb8851", 16);

    @OriginalMember(owner = "client!ica", name = "j", descriptor = "Ljava/math/BigInteger;")
    public static BigInteger RSA_EXPONENT = new BigInteger("10001", 16);

    @OriginalMember(owner = "client!dm", name = "h", descriptor = "I")
    public static int profileTransferTicks = 0;

    @OriginalMember(owner = "client!kh", name = "hb", descriptor = "I")
    public static int disallowResult = -1;

    @OriginalMember(owner = "client!le", name = "i", descriptor = "I")
    public static int disallowTrigger = -1;

    @OriginalMember(owner = "client!cv", name = "k", descriptor = "I")
    public static int okLength;

    @OriginalMember(owner = "client!rla", name = "c", descriptor = "Z")
    public static boolean aBoolean640 = false;

    @OriginalMember(owner = "client!cj", name = "o", descriptor = "I")
    public static int lastGameLoginResponse = -2;

    @OriginalMember(owner = "client!vf", name = "F", descriptor = "I")
    public static int lastDisallowTrigger = -1;

    @OriginalMember(owner = "client!ma", name = "o", descriptor = "I")
    public static int lastDisallowResult = -1;

    @OriginalMember(owner = "client!wo", name = "x", descriptor = "Z")
    public static boolean reconnectToPrevious;

    @OriginalMember(owner = "client!uu", name = "q", descriptor = "I")
    public static int positionInQueue = 0;

    @OriginalMember(owner = "client!he", name = "a", descriptor = "(IZ)V")
    public static void logout(@OriginalArg(1) boolean toLobby) {
        @Pc(12) ServerConnection[] connections = ServerConnection.VALUES;
        for (@Pc(14) int i = 0; i < connections.length; i++) {
            @Pc(19) ServerConnection connection = connections[i];
            connection.close();
        }

        reset();
        client.cacheReset();
        Static563.method7461();

        for (@Pc(36) int level = 0; level < 4; level++) {
            Client.collisionMaps[level].reset();
        }

        WorldMap.reset(false);
        System.gc();

        SongManager.stop();
        SongManager.playing = -1;
        Static501.aBoolean575 = false;
        AudioRenderer.mixBussReset();
        SoundManager.removeActiveStreams(true);
        WorldMap.resetoreToolkit();
        WorldMap.resetDisabledElements();
        Static187.method2842();

        if (toLobby) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_GAMESCREEN_TO_LOBBY);
        } else {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGIN_SCREEN);

            try {
                JavaScript.call("loggedout", GameShell.loaderApplet);
            } catch (@Pc(86) Throwable ignored) {
                /* empty */
            }
        }
    }

    @OriginalMember(owner = "client!eo", name = "a", descriptor = "(III)V")
    public static void requestLoginFromSocialNetwork(@OriginalArg(0) int socialNetworkId, @OriginalArg(1) int anInt7113) {
        if (!isAtLoginScreen()) {
            return;
        }
        LoginManager.anInt7113 = anInt7113;
        if (LoginManager.socialNetworkId != socialNetworkId) {
            previousUsername = "";
        }
        LoginManager.socialNetworkId = socialNetworkId;
        MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_GAME);
    }

    @OriginalMember(owner = "client!bt", name = "a", descriptor = "(ILjava/lang/String;Ljava/lang/String;I)V")
    public static void requestLoginWithUsername(@OriginalArg(0) int anInt7113, @OriginalArg(1) String password, @OriginalArg(2) String username) {
        if (username.length() > 320 || !isAtLoginScreen()) {
            return;
        }

        resetSocialNetwork();
        LoginManager.anInt7113 = anInt7113;
        LoginManager.password = password;
        LoginManager.username = username;
        MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_GAME);
    }

    @OriginalMember(owner = "client!wg", name = "a", descriptor = "(B)V")
    public static void reset() {
        lobbyLoginResponse = LoginResponseCode.DEFAULT;
        gameLoginResponse = LoginResponseCode.DEFAULT;
        step = LoginStep.DELAY;
    }

    @OriginalMember(owner = "client!or", name = "a", descriptor = "(Z)Z")
    public static boolean isAtLoginScreen() {
        if (MainLogicManager.step == MainLogicStep.STEP_LOGIN_SCREEN) {
            return step == LoginStep.DELAY && LobbyManager.step == LoginStep.DELAY;
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!lk", name = "a", descriptor = "(B)V")
    public static void resetSocialNetwork() {
        socialNetworkId = -1;
        ssoKey = 0L;
        previousUsername = "";
    }

    @OriginalMember(owner = "client!hj", name = "a", descriptor = "(I)Z")
    public static boolean inProgress() {
        return step != LoginStep.DELAY;
    }

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "(B)V")
    public static void changeMainState() {
        if (step == LoginStep.DELAY || step == LoginStep.WAIT_FOR_VIDEO_ADVERTISEMENT) {
            return;
        }

        try {
            @Pc(25) short maxTicks;
            if (errors == 0) {
                maxTicks = 250;
            } else {
                maxTicks = 2000;
            }

            if (type != TYPE_GAME || step != LoginStep.WAIT_FOR_QUEUE_POSITION && gameLoginResponse != LoginResponseCode.IN_QUEUE) {
                ticks++;
            }

            if (socialNetworkLogin && step >= LoginStep.WAIT_FOR_SSO_KEY_RESPONSE) {
                maxTicks = 6000;
            }

            if (maxTicks < ticks) {
                ServerConnection.active.close();

                if (errors >= 3) {
                    step = LoginStep.DELAY;
                    setLoginResponse(LoginResponseCode.CONNECTION_TIMED_OUT);
                    return;
                }

                if (type == TYPE_GAME) {
                    ConnectionInfo.login.rotateMethods();
                } else {
                    ConnectionInfo.lobby.rotateMethods();
                }

                step = LoginStep.CONNECT;
                ticks = 0;
                errors++;
            }

            if (step == LoginStep.CONNECT) {
                if (type == TYPE_GAME) {
                    ServerConnection.active.gameSocketRequest = ConnectionInfo.login.openSocket(GameShell.signLink);
                } else {
                    ServerConnection.active.gameSocketRequest = ConnectionInfo.lobby.openSocket(GameShell.signLink);
                }

                step = LoginStep.WAIT_FOR_CONNECTION_ACK;
            }

            if (step == LoginStep.WAIT_FOR_CONNECTION_ACK) {
                if (ServerConnection.active.gameSocketRequest.status == SignedResourceStatus.ERROR) {
                    throw new IOException();
                }
                if (ServerConnection.active.gameSocketRequest.status != SignedResourceStatus.SUCCESS) {
                    return;
                }

                ServerConnection.active.connection = Connection.create((Socket) ServerConnection.active.gameSocketRequest.result);
                ServerConnection.active.gameSocketRequest = null;
                ServerConnection.active.clear();

                @Pc(186) ClientMessage message = ClientMessage.createRaw();
                if (socialNetworkLogin) {
                    message.bitPacket.p1(LoginProt.INIT_SOCIAL_NETWORK_CONNECTION.opcode);
                    message.bitPacket.p2(0);

                    @Pc(203) int pos = message.bitPacket.pos;
                    message.bitPacket.p4(Client.BUILD);
                    if (type == TYPE_GAME) {
                        message.bitPacket.p1(MainLogicManager.step == MainLogicStep.STEP_RECONNECTING ? 1 : 0);
                    }

                    @Pc(229) Packet packet = Packet.createXtea();
                    packet.p1(socialNetworkId);
                    packet.p2((int) (Math.random() * 9.9999999E7D));
                    packet.p1(Client.language);
                    packet.p4(Client.affid);
                    for (@Pc(250) int i = 0; i < 6; i++) {
                        packet.p4((int) (Math.random() * 9.9999999E7D));
                    }
                    packet.p8(clientSessionKey);
                    packet.p1(Client.modeGame.id);
                    packet.p1((int) (Math.random() * 9.9999999E7D));
                    packet.rsaenc(RSA_MODULUS, RSA_EXPONENT);
                    message.bitPacket.pdata(packet.pos, packet.data, 0);
                    message.bitPacket.psize2(message.bitPacket.pos - pos);
                } else {
                    message.bitPacket.p1(LoginProt.INIT_GAME_CONNECTION.opcode);
                }

                ServerConnection.active.send(message);
                ServerConnection.active.flush();
                step = LoginStep.SEND_LOGIN_REQUEST;
            }

            if (step == LoginStep.SEND_LOGIN_REQUEST) {
                if (!ServerConnection.active.connection.hasAvailable(1)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 1, 0);

                @Pc(360) int responseCode = ServerConnection.active.bitPacket.data[0] & 0xFF;
                if (responseCode != LoginResponseCode.EXCHANGE_KEYS) {
                    step = LoginStep.DELAY;
                    setLoginResponse(responseCode);
                    ServerConnection.active.close();
                    updateMainState();
                    return;
                }

                if (socialNetworkLogin) {
                    step = LoginStep.WAIT_FOR_SOCIAL_NETWORK_TOKEN_LENGTH;
                } else {
                    step = LoginStep.SEND_LOGIN_PACKET;
                }
            }

            if (step == LoginStep.WAIT_FOR_SOCIAL_NETWORK_TOKEN_LENGTH) {
                if (!ServerConnection.active.connection.hasAvailable(2)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 2, 0);
                ServerConnection.active.bitPacket.pos = 0;
                ServerConnection.active.bitPacket.pos = ServerConnection.active.bitPacket.g2();
                step = LoginStep.WAIT_FOR_SOCIAL_NETWORK_TOKEN;
            }

            if (step == LoginStep.WAIT_FOR_SOCIAL_NETWORK_TOKEN) {
                if (!ServerConnection.active.connection.hasAvailable(ServerConnection.active.bitPacket.pos)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, ServerConnection.active.bitPacket.pos, 0);
                ServerConnection.active.bitPacket.tinydec(Packet.xteaKey);
                ServerConnection.active.bitPacket.pos = 0;
                @Pc(465) String path = ServerConnection.active.bitPacket.gjstr2();
                ServerConnection.active.bitPacket.pos = 0;
                @Pc(473) String function = "opensn";
                if (!Client.js || ClientURLTools.openURL(GameShell.signLink, path, function, OpenUrlType.CALL).status == SignedResourceStatus.ERROR) {
                    ClientURLTools.openURL(path, function, GameShell.signLink, ClientOptions.instance.toolkit.getValue() == ToolkitType.GL, true);
                }
                step = LoginStep.WAIT_FOR_SSO_KEY_RESPONSE;
            }

            if (step == LoginStep.WAIT_FOR_SSO_KEY_RESPONSE) {
                if (!ServerConnection.active.connection.hasAvailable(1)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 1, 0);
                if ((ServerConnection.active.bitPacket.data[0] & 0xFF) == 1) {
                    step = LoginStep.WAIT_FOR_SSO_KEY;
                }
            }

            if (step == LoginStep.WAIT_FOR_SSO_KEY) {
                if (!ServerConnection.active.connection.hasAvailable(16)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 16, 0);
                ServerConnection.active.bitPacket.pos = 16;
                ServerConnection.active.bitPacket.tinydec(Packet.xteaKey);
                ServerConnection.active.bitPacket.pos = 0;
                username = previousUsername = Base37.decode(ServerConnection.active.bitPacket.g8());
                ssoKey = ServerConnection.active.bitPacket.g8();
                step = LoginStep.SEND_LOGIN_PACKET;
            }

            if (step == LoginStep.SEND_LOGIN_PACKET) {
                ServerConnection.active.bitPacket.pos = 0;
                ServerConnection.active.clear();

                @Pc(186) ClientMessage message = ClientMessage.createRaw();
                @Pc(618) BitPacket bitPacket = message.bitPacket;

                if (type == TYPE_GAME) {
                    @Pc(627) LoginProt prot;
                    if (socialNetworkLogin) {
                        prot = LoginProt.SOCIAL_NETWORK_LOGIN;
                    } else {
                        prot = LoginProt.GAMELOGIN;
                    }

                    bitPacket.p1(prot.opcode);
                    bitPacket.p2(0);

                    @Pc(250) int startPos = bitPacket.pos;
                    @Pc(646) int credentialsPos = bitPacket.pos;
                    if (!socialNetworkLogin) {
                        bitPacket.p4(Client.BUILD);
                        bitPacket.p1(MainLogicManager.step == MainLogicStep.STEP_RECONNECTING ? 1 : 0);
                        credentialsPos = bitPacket.pos;

                        @Pc(672) Packet credentials = credentialsPacket();
                        bitPacket.pdata(credentials.pos, credentials.data, 0);
                        credentialsPos = bitPacket.pos;
                        bitPacket.pjstr(username);
                    }

                    bitPacket.p1(anInt7113);
                    bitPacket.p1(InterfaceManager.getWindowMode());
                    bitPacket.p2(GameShell.canvasWid);
                    bitPacket.p2(GameShell.canvasHei);
                    bitPacket.p1(ClientOptions.instance.antialiasingQuality.getValue());
                    GameShell.pushUID192(bitPacket);
                    bitPacket.pjstr(Client.settings);
                    bitPacket.p4(Client.affid);

                    @Pc(672) Packet clientOptions = ClientOptions.instance.encode();
                    bitPacket.p1(clientOptions.pos);
                    bitPacket.pdata(clientOptions.pos, clientOptions.data, 0);
                    Static503.sentPreferences = true;

                    @Pc(748) Packet systemInfo = new Packet(SystemInfo.instance.size());
                    SystemInfo.instance.encode(systemInfo);
                    bitPacket.pdata(systemInfo.data.length, systemInfo.data, 0);
                    bitPacket.p4(VerifyId.getValue());
                    bitPacket.p8(Client.userFlow);
                    bitPacket.p1(Client.additionalInfo != null ? 1 : 0);
                    if (Client.additionalInfo != null) {
                        bitPacket.pjstr(Client.additionalInfo);
                    }
                    bitPacket.p1(LibraryManager.isNativeLoaded("jagtheora") ? 1 : 0);
                    bitPacket.p1(Client.js ? 1 : 0);
                    bitPacket.p1(Client.hc ? 1 : 0);
                    pushJs5Checksums(bitPacket);
                    bitPacket.tinyenc(Packet.xteaKey, credentialsPos, bitPacket.pos);
                    bitPacket.psize2(bitPacket.pos - startPos);
                } else {
                    @Pc(627) LoginProt prot;
                    if (socialNetworkLogin) {
                        prot = LoginProt.SOCIAL_NETWORK_LOGIN;
                    } else {
                        prot = LoginProt.LOBBYLOGIN;
                    }

                    bitPacket.p1(prot.opcode);
                    bitPacket.p2(0);

                    @Pc(250) int startPos = bitPacket.pos;
                    @Pc(646) int credentialsPos = bitPacket.pos;
                    if (!socialNetworkLogin) {
                        bitPacket.p4(Client.BUILD);
                        @Pc(672) Packet credentials = credentialsPacket();
                        bitPacket.pdata(credentials.pos, credentials.data, 0);
                        credentialsPos = bitPacket.pos;
                        bitPacket.pjstr(username);
                    }

                    bitPacket.p1(Client.modeGame.id);
                    bitPacket.p1(Client.language);
                    GameShell.pushUID192(bitPacket);
                    bitPacket.pjstr(Client.settings);
                    bitPacket.p4(Client.affid);
                    pushJs5Checksums(bitPacket);
                    bitPacket.tinyenc(Packet.xteaKey, credentialsPos, bitPacket.pos);
                    bitPacket.psize2(bitPacket.pos - startPos);
                }

                ServerConnection.active.send(message);
                ServerConnection.active.flush();
                ServerConnection.active.isaac = new Isaac(Packet.xteaKey);

                for (@Pc(938) int i = 0; i < 4; i++) {
                    Packet.xteaKey[i] += 50;
                }

                ServerConnection.active.bitPacket.initIsaac(Packet.xteaKey);
                Packet.xteaKey = null;
                step = LoginStep.WAIT_FOR_LOGIN_RESPONSE;
            }

            if (step == LoginStep.WAIT_FOR_LOGIN_RESPONSE) {
                if (!ServerConnection.active.connection.hasAvailable(1)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 1, 0);

                @Pc(360) int responseCode = ServerConnection.active.bitPacket.data[0] & 0xFF;
                if (responseCode == LoginResponseCode.HOP_BLOCKED) {
                    step = LoginStep.WAIT_FOR_HOP_BLOCK_DURATION;
                } else if (responseCode == LoginResponseCode.DISALLOWED_BY_SCRIPT || responseCode == LoginResponseCode.CANNOT_ENTER_INSTANCE) {
                    step = LoginStep.WAIT_FOR_SCRIPT_DISALLOW_REASON;
                    pendingResponse = responseCode;
                } else if (responseCode == LoginResponseCode.VIDEO_ADVERTISEMENT) {
                    step = LoginStep.WAIT_FOR_VIDEO_ADVERTISEMENT;
                    setLoginResponse(responseCode);
                    return;
                } else if (responseCode == LoginResponseCode.OK) {
                    step = LoginStep.WAIT_FOR_LOGIN_OK_LENGTH;
                } else if (responseCode == LoginResponseCode.RECONNECT_OK) {
                    step = LoginStep.WAIT_FOR_LOGIN_RECONNECT;
                    ServerConnection.active.currentPacketSize = -2;
                } else if (responseCode == LoginResponseCode.LOGIN_SERVER_NO_REPLY && errors < 3) {
                    ticks = 0;
                    step = LoginStep.CONNECT;
                    errors++;
                    ServerConnection.active.connection.close();
                    ServerConnection.active.connection = null;
                    return;
                } else if (responseCode == LoginResponseCode.IN_QUEUE) {
                    step = LoginStep.WAIT_FOR_QUEUE_POSITION;
                    setLoginResponse(responseCode);
                    return;
                } else if (!aBoolean640 || socialNetworkLogin || socialNetworkId == -1 || responseCode != LoginResponseCode.INVALID_SINGLE_SIGNON) {
                    step = LoginStep.DELAY;
                    setLoginResponse(responseCode);
                    ServerConnection.active.connection.close();
                    ServerConnection.active.connection = null;
                    updateMainState();
                    return;
                } else {
                    socialNetworkLogin = true;
                    step = LoginStep.CONNECT;
                    ticks = 0;
                    ServerConnection.active.connection.close();
                    ServerConnection.active.connection = null;
                    return;
                }
            }

            if (step == LoginStep.VIDEO_ADVERTISEMENT_FINISHED) {
                ServerConnection.active.clear();
                @Pc(186) ClientMessage message = ClientMessage.createRaw();
                @Pc(618) BitPacket bitPacket = message.bitPacket;
                bitPacket.setIsaac(ServerConnection.active.isaac);
                bitPacket.startPacket(LoginProt.GAMELOGIN_CONTINUE.opcode);
                ServerConnection.active.send(message);
                ServerConnection.active.flush();
                step = LoginStep.WAIT_FOR_LOGIN_RESPONSE;
            } else if (step == LoginStep.WAIT_FOR_HOP_BLOCK_DURATION) {
                if (!ServerConnection.active.connection.hasAvailable(1)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 1, 0);
                @Pc(360) int seconds = ServerConnection.active.bitPacket.data[0] & 0xFF;
                step = LoginStep.DELAY;
                profileTransferTicks = seconds * 50;
                setLoginResponse(LoginResponseCode.HOP_BLOCKED);
                ServerConnection.active.connection.close();
                ServerConnection.active.connection = null;
                updateMainState();
            } else if (step == LoginStep.WAIT_FOR_QUEUE_POSITION) {
                if (!ServerConnection.active.connection.hasAvailable(2)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 2, 0);
                positionInQueue = (ServerConnection.active.bitPacket.data[1] & 0xFF) + ((ServerConnection.active.bitPacket.data[0] & 0xFF) << 8);
                step = LoginStep.WAIT_FOR_LOGIN_RESPONSE;
            } else if (step == LoginStep.WAIT_FOR_SCRIPT_DISALLOW_REASON) {
                if (pendingResponse == LoginResponseCode.DISALLOWED_BY_SCRIPT) {
                    if (!ServerConnection.active.connection.hasAvailable(1)) {
                        return;
                    }

                    ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 1, 0);
                    disallowResult = ServerConnection.active.bitPacket.data[0] & 0xFF;
                } else if (pendingResponse == LoginResponseCode.CANNOT_ENTER_INSTANCE) {
                    if (!ServerConnection.active.connection.hasAvailable(3)) {
                        return;
                    }

                    ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 3, 0);
                    disallowTrigger = (ServerConnection.active.bitPacket.data[2] & 0xFF) + ((ServerConnection.active.bitPacket.data[1] & 0xFF) << 8);
                    disallowResult = ServerConnection.active.bitPacket.data[0] & 0xFF;
                } else {
                    throw new IllegalStateException("Login step 18 not valid for pendingResponse=" + pendingResponse);
                }

                step = LoginStep.DELAY;
                setLoginResponse(pendingResponse);
                ServerConnection.active.connection.close();
                ServerConnection.active.connection = null;
                updateMainState();
            } else if (step == LoginStep.WAIT_FOR_LOGIN_OK_LENGTH) {
                if (!ServerConnection.active.connection.hasAvailable(1)) {
                    return;
                }

                ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 1, 0);
                okLength = ServerConnection.active.bitPacket.data[0] & 0xFF;
                step = LoginStep.WAIT_FOR_LOGIN_DETAILS;
            } else {
                if (step == LoginStep.WAIT_FOR_LOGIN_DETAILS) {
                    @Pc(1435) BitPacket bitPacket = ServerConnection.active.bitPacket;

                    if (type == TYPE_GAME) {
                        if (!ServerConnection.active.connection.hasAvailable(okLength)) {
                            return;
                        }

                        ServerConnection.active.connection.read(bitPacket.data, okLength, 0);
                        bitPacket.pos = 0;
                        Client.staffModLevel = bitPacket.g1();
                        Static38.playerModLevel = bitPacket.g1();
                        UserDetail.underage = bitPacket.g1() == 1;
                        UserDetail.parentalChatConsent = bitPacket.g1() == 1;
                        Static298.parentalAdvertConsent = bitPacket.g1() == 1;
                        Static617.quickChatWorld = bitPacket.g1() == 1;
                        PlayerList.activePlayerSlot = bitPacket.g2();
                        Client.isMember = bitPacket.g1() == 1;
                        UserDetail.dob = bitPacket.g3s();
                        Static174.mapMembers = bitPacket.g1() == 1;
                        Static416.mapOwner = bitPacket.gjstr();
                        LocTypeList.instance.setAllowMembers(Static174.mapMembers);
                        ObjTypeList.instance.setAllowMembers(Static174.mapMembers);
                        NPCTypeList.instance.setAllowMembers(Static174.mapMembers);
                    } else {
                        if (!ServerConnection.active.connection.hasAvailable(okLength)) {
                            return;
                        }

                        ServerConnection.active.connection.read(bitPacket.data, okLength, 0);
                        bitPacket.pos = 0;
                        Client.staffModLevel = bitPacket.g1();
                        Static38.playerModLevel = bitPacket.g1();
                        UserDetail.underage = bitPacket.g1() == 1;
                        UserDetail.parentalChatConsent = bitPacket.g1() == 1;
                        Static298.parentalAdvertConsent = bitPacket.g1() == 1;
                        Static416.subscriptionExpiration = bitPacket.g8();
                        Static94.remainingSubscription = Static416.subscriptionExpiration - SystemTimer.safetime() - bitPacket.g5();
                        @Pc(203) int membershipInfo = bitPacket.g1();
                        Client.isMember = (membershipInfo & 0x1) != 0;
                        UserDetail.activeSubscription = (membershipInfo & 0x2) != 0;
                        UserDetail.lobbyJcoinsBalance = bitPacket.g4();
                        UserDetail.lobbyLoyalty = bitPacket.g1() == 1;
                        UserDetail.lobbyLoyaltyBalance = bitPacket.g4();
                        UserDetail.lobbyRecoveryDay = bitPacket.g2();
                        UserDetail.lobbyUnreadMessages = bitPacket.g2();
                        UserDetail.lobbyLastLoginDay = bitPacket.g2();
                        UserDetail.lastLoginAddress = bitPacket.g4();
                        Static439.hostnameResource = GameShell.signLink.lookupHostname(UserDetail.lastLoginAddress);
                        UserDetail.lobbyEmailStatus = bitPacket.g1();
                        UserDetail.lobbyCCExpiry = bitPacket.g2();
                        UserDetail.lobbyGraceExpiry = bitPacket.g2();
                        UserDetail.lobbyDOBRequested = bitPacket.g1() == 1;
                        PlayerEntity.self.nameUnfiltered = PlayerEntity.self.displayName = Client.playerDisplayName = bitPacket.gjstr2();
                        UserDetail.lobbyMembersStats = bitPacket.g1();
                        UserDetail.lobbyPlayAge = bitPacket.g4();
                        Static587.aBoolean663 = bitPacket.g1() == 1;

                        ConnectionInfo.auto = new ConnectionInfo();
                        ConnectionInfo.auto.world = bitPacket.g2();
                        if (ConnectionInfo.auto.world == 65535) {
                            ConnectionInfo.auto.world = -1;
                        }
                        ConnectionInfo.auto.address = bitPacket.gjstr2();

                        if (ModeWhere.LIVE != Client.modeWhere) {
                            ConnectionInfo.auto.secondaryPort = ConnectionInfo.auto.world + Ports.SECONDARY_OFFSET;
                            ConnectionInfo.auto.primaryPort = ConnectionInfo.auto.world + Ports.PRIMARY_OFFSET;
                        }

                        if ((ModeWhere.LOCAL != Client.modeWhere) && ((Client.modeWhere != ModeWhere.WTQA) || (Client.staffModLevel < 2)) && ConnectionInfo.login.equalTo(ConnectionInfo.game)) {
                            WorldList.selectAutoWorld();
                        }
                    }

                    if (UserDetail.underage && !Static298.parentalAdvertConsent || Client.isMember) {
                        try {
                            JavaScript.call("zap", GameShell.loaderApplet);
                        } catch (@Pc(1850) Throwable ignored) {
                            if (Client.advert) {
                                try {
                                    GameShell.loaderApplet.getAppletContext().showDocument(new URL(GameShell.loaderApplet.getCodeBase(), "blank.ws"), "tbi");
                                } catch (@Pc(1868) Exception ignored2) {
                                    /* empty */
                                }
                            }
                        }
                    } else {
                        try {
                            JavaScript.call("unzap", GameShell.loaderApplet);
                        } catch (@Pc(1879) Throwable ignored) {
                            /* empty */
                        }
                    }

                    if (Client.modeWhere == ModeWhere.LIVE) {
                        try {
                            JavaScript.call("loggedin", GameShell.loaderApplet);
                        } catch (@Pc(1892) Throwable ignored) {
                            /* empty */
                        }
                    }

                    if (type != TYPE_GAME) {
                        step = LoginStep.DELAY;
                        setLoginResponse(LoginResponseCode.OK);
                        Static249.method3538();
                        MainLogicManager.setStep(MainLogicStep.STEP_LOBBY_SCREEN);
                        ServerConnection.active.currentProt = null;
                        return;
                    }

                    step = LoginStep.WAIT_FOR_PLAYER_PACKET_LENGTH1;
                }

                if (step == LoginStep.WAIT_FOR_PLAYER_PACKET_LENGTH1) {
                    if (!ServerConnection.active.connection.hasAvailable(3)) {
                        return;
                    }

                    ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 3, 0);
                    step = LoginStep.WAIT_FOR_PLAYER_PACKET_LENGTH2;
                }

                if (step == LoginStep.WAIT_FOR_PLAYER_PACKET_LENGTH2) {
                    @Pc(1435) BitPacket bitPacket = ServerConnection.active.bitPacket;
                    bitPacket.pos = 0;

                    if (bitPacket.largeOpcode()) {
                        if (!ServerConnection.active.connection.hasAvailable(1)) {
                            return;
                        }

                        ServerConnection.active.connection.read(bitPacket.data, 1, 3);
                    }

                    ServerConnection.active.currentProt = ServerProt.values()[bitPacket.readOpcode()];
                    ServerConnection.active.currentPacketSize = bitPacket.g2();
                    step = LoginStep.WAIT_FOR_PLAYER_PACKET;
                }

                if (step == LoginStep.WAIT_FOR_PLAYER_PACKET) {
                    if (!ServerConnection.active.connection.hasAvailable(ServerConnection.active.currentPacketSize)) {
                        return;
                    }

                    ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, ServerConnection.active.currentPacketSize, 0);
                    ServerConnection.active.bitPacket.pos = 0;

                    @Pc(360) int size = ServerConnection.active.currentPacketSize;
                    step = LoginStep.DELAY;
                    setLoginResponse(LoginResponseCode.OK);
                    connected();
                    PlayerList.getSnapShotPlayer(ServerConnection.active.bitPacket);
                    Static62.areaCenterX = -1;

                    if (ServerConnection.active.currentProt == ServerProt.REBUILD_REGION) {
                        Static466.rebuildRegion();
                    } else {
                        Static434.rebuildNormal();
                    }

                    if (ServerConnection.active.bitPacket.pos != size) {
                        throw new RuntimeException("lswp pos:" + ServerConnection.active.bitPacket.pos + " psize:" + size);
                    }

                    ServerConnection.active.currentProt = null;
                } else if (step == LoginStep.WAIT_FOR_LOGIN_RECONNECT) {
                    if (ServerConnection.active.currentPacketSize == -2) {
                        if (!ServerConnection.active.connection.hasAvailable(2)) {
                            return;
                        }

                        ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, 2, 0);
                        ServerConnection.active.bitPacket.pos = 0;
                        ServerConnection.active.currentPacketSize = ServerConnection.active.bitPacket.g2();
                    }

                    if (ServerConnection.active.connection.hasAvailable(ServerConnection.active.currentPacketSize)) {
                        ServerConnection.active.connection.read(ServerConnection.active.bitPacket.data, ServerConnection.active.currentPacketSize, 0);
                        ServerConnection.active.bitPacket.pos = 0;
                        step = LoginStep.DELAY;

                        @Pc(360) int size = ServerConnection.active.currentPacketSize;
                        setLoginResponse(LoginResponseCode.RECONNECT_OK);
                        reconnected();
                        PlayerList.getSnapShotPlayer(ServerConnection.active.bitPacket);
                        if (size != ServerConnection.active.bitPacket.pos) {
                            throw new RuntimeException("lswpr pos:" + ServerConnection.active.bitPacket.pos + " psize:" + size);
                        }
                        ServerConnection.active.currentProt = null;
                    }
                }
            }
        } catch (@Pc(2184) IOException exception) {
            ServerConnection.active.close();

            if (errors < 3) {
                if (type == TYPE_GAME) {
                    ConnectionInfo.login.rotateMethods();
                } else {
                    ConnectionInfo.lobby.rotateMethods();
                }

                step = LoginStep.CONNECT;
                errors++;
                ticks = 0;
            } else {
                step = LoginStep.DELAY;
                setLoginResponse(LoginResponseCode.ERROR_CONNECTING_TO_SERVER);
                updateMainState();
            }
        }
    }

    @OriginalMember(owner = "client!bs", name = "b", descriptor = "(I)V")
    public static void cancel() {
        if (step != LoginStep.DELAY) {
            ServerConnection.active.close();
            reset();
            updateMainState();
        }
    }

    @OriginalMember(owner = "client!cc", name = "b", descriptor = "(I)V")
    public static void videoAdvertisementFinished() {
        if (step == LoginStep.WAIT_FOR_VIDEO_ADVERTISEMENT) {
            step = LoginStep.VIDEO_ADVERTISEMENT_FINISHED;
        }
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(II)V")
    public static void loginToGame(@OriginalArg(0) int arg0) {
        if (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN && (step == LoginStep.DELAY && LobbyManager.step == LoginStep.DELAY)) {
            anInt7113 = arg0;
            MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME);
        }
    }

    @OriginalMember(owner = "client!hh", name = "a", descriptor = "(ZLjava/lang/String;ZZLjava/lang/String;)V")
    public static void doLogin(@OriginalArg(0) boolean socialNetworkLogin, @OriginalArg(1) String username, @OriginalArg(3) boolean aBoolean640, @OriginalArg(4) String password) {
        if (!aBoolean640) {
            socialNetworkId = -1;
        }

        LoginManager.password = password;
        LoginManager.username = username;
        LoginManager.socialNetworkLogin = socialNetworkLogin;
        LoginManager.aBoolean640 = aBoolean640;

        if (!LoginManager.aBoolean640 && (LoginManager.username.equals("") || LoginManager.password.equals(""))) {
            setLoginResponse(LoginResponseCode.INVALID_USERNAME_OR_PASSWORD);
            return;
        }

        ServerConnection.active.errored = false;
        if (type != TYPE_LOBBY) {
            profileTransferTicks = 0;
            disallowResult = -1;
            disallowTrigger = -1;
        }

        setLoginResponse(LoginResponseCode.LOGGING_IN);
        step = LoginStep.CONNECT;
        ticks = 0;
        errors = 0;
    }

    @OriginalMember(owner = "client!kp", name = "a", descriptor = "(IZ)V")
    public static void setLoginResponse(@OriginalArg(0) int response) {
        if (type == TYPE_LOBBY) {
            lobbyLoginResponse = response;
        } else if (type == TYPE_GAME) {
            gameLoginResponse = response;
        }
    }

    @OriginalMember(owner = "client!ut", name = "d", descriptor = "(B)Lclient!ge;")
    public static Packet credentialsPacket() {
        @Pc(6) Packet packet = Packet.createXtea();
        packet.p8(0L);
        packet.pjstr(password);
        packet.p8(ssoKey);
        packet.p8(clientSessionKey);
        packet.rsaenc(RSA_MODULUS, RSA_EXPONENT);
        return packet;
    }

    @OriginalMember(owner = "client!fe", name = "a", descriptor = "(ILclient!rka;)V")
    public static void pushJs5Checksums(@OriginalArg(1) BitPacket bitPacket) {
        bitPacket.p4(js5.ANIMS.indexCrc());
        bitPacket.p4(js5.BASES.indexCrc());
        bitPacket.p4(js5.CONFIG.indexCrc());
        bitPacket.p4(js5.INTERFACES.indexCrc());
        bitPacket.p4(js5.SYNTH_SOUNDS.indexCrc());
        bitPacket.p4(js5.MAPS.indexCrc());
        bitPacket.p4(js5.MIDI_SONGS.indexCrc());
        bitPacket.p4(js5.MODELS.indexCrc());
        bitPacket.p4(js5.SPRITES.indexCrc());
        bitPacket.p4(js5.TEXTURES.indexCrc());
        bitPacket.p4(js5.BINARY.indexCrc());
        bitPacket.p4(js5.MIDI_JINGLES.indexCrc());
        bitPacket.p4(js5.CLIENTSCRIPTS.indexCrc());
        bitPacket.p4(js5.FONTMETRICS.indexCrc());
        bitPacket.p4(js5.VORBIS.indexCrc());
        bitPacket.p4(js5.js5_15.indexCrc());
        bitPacket.p4(js5.CONFIG_LOC.indexCrc());
        bitPacket.p4(js5.CONFIG_ENUM.indexCrc());
        bitPacket.p4(js5.CONFIG_NPC.indexCrc());
        bitPacket.p4(js5.CONFIG_OBJ.indexCrc());
        bitPacket.p4(js5.CONFIG_SEQ.indexCrc());
        bitPacket.p4(js5.CONFIG_SPOT.indexCrc());
        bitPacket.p4(js5.CONFIG_STRUCT.indexCrc());
        bitPacket.p4(js5.WORLDMAPDATA.indexCrc());
        bitPacket.p4(js5.QUICKCHAT.indexCrc());
        bitPacket.p4(js5.QUICKCHAT_GLOBAL.indexCrc());
        bitPacket.p4(js5.MATERIALS.indexCrc());
        bitPacket.p4(js5.CONFIG_PARTICLE.indexCrc());
        bitPacket.p4(js5.DEFAULTS.indexCrc());
        bitPacket.p4(js5.CONFIG_BILLBOARD.indexCrc());
        bitPacket.p4(js5.DLLS.indexCrc());
        bitPacket.p4(js5.SHADERS.indexCrc());
        bitPacket.p4(js5.CUTSCENES.indexCrc());
        bitPacket.p4(Loading.getSpritesCrc());
        bitPacket.p4(Loading.getScreensCrc());
        bitPacket.p4(js5.VIDEOS.indexCrc());
    }

    @OriginalMember(owner = "client!hr", name = "c", descriptor = "(I)V")
    public static void connected() {
        js5.CONFIG.discardunpacked = 1;
        if (MainLogicManager.step == MainLogicStep.STEP_SWITCH_WORLD) {
            Static187.method2842();
        }
        Static408.method5632();
        Static694.anInt10405 = 0;
        Static618.anInt9449 = 0;
        ObjType.shadowCount = 0;
        Static373.anInt5903 = 0;
        ServerConnection.LOBBY.close();
        Static50.previousFocus = true;
        GameShell.focus = true;
        Static230.method3374();
        for (@Pc(8628) int i = 0; i < Static527.hintArrows.length; i++) {
            Static527.hintArrows[i] = null;
        }
        InterfaceManager.targetMode = false;
        SoundManager.reset();
        Camera.yawOffset = (int) (Math.random() * 120.0D) - 60;
        Static288.anInt4621 = (int) (Math.random() * 80.0D) - 40;
        Static145.anInt2561 = (int) (Math.random() * 110.0D) - 55;
        Camera.scaleOffset = (int) (Math.random() * 30.0D) - 20;
        Static508.anInt7627 = (int) (Math.random() * 100.0D) - 50;
        Camera.playerCameraYaw = (float) ((int) (Math.random() * 160.0D) - 80 & 0x3FFF);
        Minimap.resetToggle();
        for (@Pc(8697) int local8697 = 0; local8697 < PlayerList.COUNT; local8697++) {
            PlayerList.highResolutionPlayers[local8697] = null;
        }
        NPCList.size = 0;
        NPCList.newSize = 0;
        NPCList.local.clear();
        Static505.projectiles.clear();
        Static346.spotAnimations.clear();
        TextCoordList.textCoords.clear();
        Static497.objStacks.clear();
        Static159.changes = new Deque();
        Static227.customisations = new Deque();
        TimedVarDomain.instance.reset();
        DelayedStateChange.clear();
        Camera.moveToX = 0;
        Camera.lookY = 0;
        Camera.lookZ = 0;
        Camera.lookX = 0;
        Camera.lookStep = 0;
        Camera.moveToRate = 0;
        Camera.moveToY = 0;
        Camera.lookSpeed = 0;
        Camera.moveToZ = 0;
        Camera.moveToStep = 0;

        for (@Pc(8765) int i = 0; i < Static511.varcs.length; i++) {
            if (!Static118.permVarcs[i]) {
                Static511.varcs[i] = -1;
            }
        }

        if (InterfaceManager.topLevelInterface != -1) {
            InterfaceManager.discard(InterfaceManager.topLevelInterface);
        }

        for (@Pc(8803) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.first(); sub != null; sub = (SubInterface) InterfaceManager.subInterfaces.next()) {
            if (!sub.isLinked()) {
                sub = (SubInterface) InterfaceManager.subInterfaces.first();

                if (sub == null) {
                    break;
                }
            }

            InterfaceManager.closeSubInterface(sub, true, false);
        }

        InterfaceManager.topLevelInterface = -1;
        InterfaceManager.subInterfaces = new IterableHashTable(8);
        InterfaceList.reset();
        InterfaceManager.dialog = null;
        for (@Pc(8849) int i = 0; i < 8; i++) {
            MiniMenu.playerOps[i] = null;
            MiniMenu.playerOpsReducedPriority[i] = false;
            MiniMenu.playerOpCursors[i] = -1;
        }

        ClientInventory.cacheClear();
        Static426.aBoolean72 = true;

        for (@Pc(8877) int i = 0; i < 100; i++) {
            InterfaceManager.dirtyRectangles[i] = true;
        }

        for (@Pc(8893) int i = 0; i < 6; i++) {
            StockmarketManager.offers[i] = new StockmarketOffer();
        }

        for (@Pc(8911) int i = 0; i < 25; i++) {
            Static581.statLevels[i] = 0;
            Static498.statBaseLevels[i] = 0;
            Static237.statXps[i] = 0;
        }

        InterfaceManager.loginOpened();
        Camera.angleUpdated = true;
        Client.clientpalette = LocType.clientpalette = NPCType.clientpalette = ObjType.clientpalette = new short[256];
        Static331.moveText = LocalisedText.WALKHERE.localise(Client.language);
        ClientOptions.instance.update(ClientOptions.instance.removeRoofs.getValue(), ClientOptions.instance.removeRoofsOverride);
        ClientOptions.instance.update(ClientOptions.instance.animateBackgroundDefault.getValue(), ClientOptions.instance.animateBackground);
        VerifyId.reset();
        MiniMenu.resetAndClose();
        ServerConnectionReader.sendWindowStatus();
        Static211.pingRequest = null;
        Static675.nextPing = 0L;
        js5.CONFIG.discardunpacked = 2;
    }

    @OriginalMember(owner = "client!cv", name = "b", descriptor = "(I)V")
    public static void reconnected() {
        ServerConnection.active.clear();
        ServerConnection.active.currentProt = null;
        ServerConnection.active.bitPacket.pos = 0;
        ServerConnection.active.idleReadTicks = 0;
        ServerConnection.active.antepenultimateProt = null;
        ServerConnection.active.penultimateProt = null;
        ServerConnection.active.currentPacketSize = 0;
        Static249.rebootTimer = 0;
        ServerConnection.active.lastProt = null;
        MiniMenu.resetAndClose();
        Minimap.resetFlag();

        for (@Pc(36) int i = 0; i < PlayerList.COUNT; i++) {
            PlayerList.highResolutionPlayers[i] = null;
        }
        PlayerEntity.self = null;

        for (@Pc(49) int i = 0; i < NPCList.newSize; i++) {
            @Pc(55) NPCEntity npc = NPCList.entities[i].npc;

            if (npc != null) {
                npc.target = -1;
            }
        }

        ClientInventory.cacheClear();
        Camera.mode = CameraMode.MODE_DEFAULT;
        Camera.anInt10383 = -1;
        Camera.anInt10376 = -1;

        MainLogicManager.setStep(MainLogicStep.STEP_GAME_SCREEN);

        for (@Pc(79) int i = 0; i < 100; i++) {
            InterfaceManager.dirtyRectangles[i] = true;
        }

        ServerConnectionReader.sendWindowStatus();
        Static675.nextPing = 0L;
        Static211.pingRequest = null;
    }

    @OriginalMember(owner = "client!fu", name = "a", descriptor = "(Ljava/lang/String;ILjava/lang/String;I)V")
    public static void doGameLogin(@OriginalArg(0) String password, @OriginalArg(1) int arg1, @OriginalArg(2) String username) {
        ServerConnection.active = ServerConnection.GAME;
        type = TYPE_GAME;
        anInt7113 = arg1;
        doLogin(false, username, false, password);
    }

    @OriginalMember(owner = "client!tia", name = "a", descriptor = "(Ljava/lang/String;Ljava/lang/String;B)V")
    public static void doLobbyLogin(@OriginalArg(0) String password, @OriginalArg(1) String username) {
        anInt7113 = -1;
        ServerConnection.active = ServerConnection.LOBBY;
        type = TYPE_LOBBY;
        doLogin(false, username, false, password);
    }

    @OriginalMember(owner = "client!tk", name = "a", descriptor = "(II)V")
    public static void checkGameSession(@OriginalArg(0) int arg0) {
        type = TYPE_GAME;
        ServerConnection.active = ServerConnection.GAME;
        anInt7113 = arg0;

        @Pc(18) String key = null;
        if (Client.ssKey != null) {
            @Pc(25) Packet packet = new Packet(Client.ssKey);
            key = Base37.decode(packet.g8());
            ssoKey = packet.g8();
        }

        if (key == null) {
            setLoginResponse(LoginResponseCode.INVALID_SINGLE_SIGNON);
        } else {
            doLogin(false, key, true, "");
        }
    }

    @OriginalMember(owner = "client!jea", name = "b", descriptor = "(I)V")
    public static void checkLobbySession() {
        anInt7113 = -1;
        ServerConnection.active = ServerConnection.LOBBY;
        type = TYPE_LOBBY;

        @Pc(16) String key = null;
        if (Client.ssKey != null) {
            @Pc(23) Packet packet = new Packet(Client.ssKey);
            key = Base37.decode(packet.g8());
            ssoKey = packet.g8();
        }

        if (key == null) {
            setLoginResponse(LoginResponseCode.INVALID_SINGLE_SIGNON);
        } else {
            doLogin(false, key, true, "");
        }
    }

    @OriginalMember(owner = "client!vw", name = "a", descriptor = "(I)V")
    public static void doLobbySnLogin() {
        anInt7113 = -1;
        type = TYPE_LOBBY;
        ServerConnection.active = ServerConnection.LOBBY;
        doLogin(previousUsername.equals(""), previousUsername, true, "");
    }

    @OriginalMember(owner = "client!dja", name = "b", descriptor = "(II)V")
    public static void doGameSnLogin(@OriginalArg(1) int arg0) {
        ServerConnection.active = ServerConnection.GAME;
        type = TYPE_GAME;
        anInt7113 = arg0;
        doLogin(previousUsername.equals(""), previousUsername, true, "");
    }

    @OriginalMember(owner = "client!vda", name = "g", descriptor = "(I)V")
    public static void loginToGame() {
        if (Client.ssKey != null) {
            checkGameSession(anInt7113);
        } else if (socialNetworkId == -1) {
            doGameLogin(password, anInt7113, username);
        } else {
            doGameSnLogin(anInt7113);
        }
    }

    @OriginalMember(owner = "client!vaa", name = "a", descriptor = "(ILclient!fk;)[I")
    public static int[] pushXtea(@OriginalArg(1) ClientMessage message) {
        @Pc(8) Packet packet = new Packet(518);
        @Pc(11) int[] parts = new int[4];
        for (@Pc(13) int i = 0; i < 4; i++) {
            parts[i] = (int) (Math.random() * 9.9999999E7D);
        }

        packet.p1(10);
        packet.p4(parts[0]);
        packet.p4(parts[1]);
        packet.p4(parts[2]);
        packet.p4(parts[3]);
        for (@Pc(70) int i = 0; i < 10; i++) {
            packet.p4((int) (Math.random() * 9.9999999E7D));
        }
        packet.p2((int) (Math.random() * 9.9999999E7D));
        packet.rsaenc(RSA_MODULUS, RSA_EXPONENT);
        message.bitPacket.pdata(packet.pos, packet.data, 0);
        return parts;
    }

    private LoginManager() {
        /* empty */
    }

    @OriginalMember(owner = "client!rp", name = "b", descriptor = "(B)V")
    public static void updateMainState() {
        if (Static656.method6691(MainLogicManager.step)) {
            if (ServerConnection.LOBBY.connection == null) {
                MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_LOBBY);
            } else {
                MainLogicManager.setStep(MainLogicStep.STEP_LOBBY_SCREEN);
            }
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_LOBBY || MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_GAME) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGIN_SCREEN);
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_GAMESCREEN_TO_LOBBY) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGIN_SCREEN);
        }
    }

    @OriginalMember(owner = "client!jka", name = "a", descriptor = "(IB)V")
    public static void loginToLobby(@OriginalArg(0) int arg0) {
        if (!isAtLoginScreen()) {
            return;
        }
        if (socialNetworkId != arg0) {
            previousUsername = "";
        }
        socialNetworkId = arg0;
        ServerConnection.LOBBY.close();
        MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_LOBBY);
    }

    @OriginalMember(owner = "client!go", name = "a", descriptor = "(Ljava/lang/String;Ljava/lang/String;B)V")
    public static void enterLobby(@OriginalArg(1) String username, @OriginalArg(0) String password) {
        if (username.length() > 320 || !isAtLoginScreen()) {
            return;
        }
        ServerConnection.LOBBY.close();
        resetSocialNetwork();
        LoginManager.password = password;
        LoginManager.username = username;
        MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_LOBBY);
    }
}

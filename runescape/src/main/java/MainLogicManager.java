import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.io.ConnectionInfo;
import com.jagex.core.io.Packet;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.DelayedStateChange;
import com.jagex.game.LocalisedText;
import com.jagex.game.camera.CameraMode;
import com.jagex.game.camera.Shake;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.defaults.AudioDefaults;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.game.runetek6.config.vartype.player.VarPlayerTypeListClient;
import com.jagex.game.world.World;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.keyboard.KeyLog;
import rs2.client.event.keyboard.KeyboardMonitor;
import rs2.client.event.mouse.MouseLog;

import java.io.IOException;

public final class MainLogicManager {

    @OriginalMember(owner = "client!it", name = "g", descriptor = "I")
    public static int step = MainLogicStep.STEP_LOADING;

    @OriginalMember(owner = "client!cka", name = "a", descriptor = "(IB)V")
    public static void setStep(@OriginalArg(0) int newStep) {
        if (step == newStep) {
            return;
        }

        if (newStep == MainLogicStep.STEP_RECONNECTING || newStep == MainLogicStep.STEP_SWITCH_WORLD) {
            // g.warn("Starting reconnection login");
            LoginManager.loginToGame();
        }

        if (newStep != MainLogicStep.STEP_RECONNECTING && ConnectionManager.reconnect != null) {
            ConnectionManager.reconnect.close();
            ConnectionManager.reconnect = null;
        }

        if (newStep == MainLogicStep.STEP_LOGIN_SCREEN) {
            InterfaceManager.openLogin(GraphicsDefaults.instance.login_interface != InterfaceManager.topLevelInterface);
        }

        if (newStep == MainLogicStep.STEP_LOBBY_SCREEN) {
            InterfaceManager.openLobby(GraphicsDefaults.instance.lobby_interface != InterfaceManager.topLevelInterface);
        }

        if (newStep == MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_LOBBY || newStep == MainLogicStep.STEP_LOGGING_IN_FROM_GAMESCREEN_TO_LOBBY) {
            Static369.method3852();
        } else if (newStep == MainLogicStep.STEP_LOGGING_IN_FROM_LOGINSCREEN_TO_GAME || newStep == MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME && step != MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME_MAP_BUILD) {
            LoginManager.loginToGame();
        }

        if (MainLogicStep.isBuildingMap(step)) {
            js5.CONFIG.discardunpacked = 2;
            js5.CONFIG_ENUM.discardunpacked = 2;
            js5.CONFIG_LOC.discardunpacked = 2;
            js5.CONFIG_NPC.discardunpacked = 2;
            js5.CONFIG_OBJ.discardunpacked = 2;
            js5.CONFIG_SEQ.discardunpacked = 2;
            js5.CONFIG_SPOT.discardunpacked = 2;
        }

        if (MainLogicStep.isBuildingMap(newStep)) {
            Static593.anInt8763 = 0;
            Static357.anInt6508 = 1;
            Static440.anInt6683 = 1;
            Static213.anInt3472 = 0;
            Static13.anInt150 = 0;
            WorldMap.reset(true);
            js5.CONFIG.discardunpacked = 1;
            js5.CONFIG_ENUM.discardunpacked = 1;
            js5.CONFIG_LOC.discardunpacked = 1;
            js5.CONFIG_NPC.discardunpacked = 1;
            js5.CONFIG_OBJ.discardunpacked = 1;
            js5.CONFIG_SEQ.discardunpacked = 1;
            js5.CONFIG_SPOT.discardunpacked = 1;
        }

        if (newStep == MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD || newStep == MainLogicStep.STEP_LOGIN_SCREEN) {
            Static314.tbrefresh();
        }

        @Pc(213) boolean loggingOut = newStep == MainLogicStep.STEP_LOADING_2 || MainLogicStep.isLoggedOut(newStep) || MainLogicStep.isAtLobbyScreen(newStep);
        @Pc(235) boolean loggedOut = step == 2 || MainLogicStep.isLoggedOut(step) || MainLogicStep.isAtLobbyScreen(step);

        if (loggedOut != loggingOut) {
            if (loggingOut) {
                SongManager.playing = AudioDefaults.themeMusic;

                if (ClientOptions.instance.loginVolume.getValue() == 0) {
                    SongManager.stop();
                } else {
                    SongManager.reset(AudioDefaults.themeMusic, ClientOptions.instance.loginVolume.getValue(), js5.MIDI_SONGS);
                    AudioRenderer.mixBussReset();
                }

                Client.js5WorkerThread.loggedIn(false);
            } else {
                SongManager.stop();
                Client.js5WorkerThread.loggedIn(true);
            }
        }

        if (MainLogicStep.isBuildingMap(newStep) || newStep == MainLogicStep.STEP_RECONNECTING || newStep == MainLogicStep.STEP_SWITCH_WORLD) {
            Toolkit.active.method7969();
        }

        step = newStep;
    }

    @OriginalMember(owner = "client!od", name = "b", descriptor = "(I)V")
    public static void mapBuild() {
        if (step == MainLogicStep.STEP_LOGIN_SCREEN) {
            setStep(MainLogicStep.STEP_LOGIN_SCREEN_MAP_BUILD);
        } else if (step == MainLogicStep.STEP_LOBBY_SCREEN) {
            setStep(MainLogicStep.STEP_LOBBY_SCREEN_MAP_BUILD);
        } else if (step == MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME) {
            setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME_MAP_BUILD);
        } else if (step == MainLogicStep.STEP_GAME_SCREEN) {
            setStep(MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD);
        }
    }

    @OriginalMember(owner = "client!bda", name = "a", descriptor = "(IB)V")
    public static void method977(@OriginalArg(0) int id) {
        SoundManager.method2000();

        @Pc(16) int clientCode = VarPlayerTypeListClient.instance.list(id).clientCode;
        if (clientCode == 0) {
            return;
        }

        @Pc(25) int value = TimedVarDomain.instance.varValues[id];
        if (clientCode == 5) {
            Client.mouseButtons = value;
        }
        if (clientCode == 6) {
            Client.disableChatEffects = value;
        }
    }

    @OriginalMember(owner = "client!wh", name = "i", descriptor = "(I)V")
    public static void changeMainState() {
        if (Static249.rebootTimer > 1) {
            Static249.rebootTimer--;
            Static321.lastMiscTransmit = World.tick;
        }

        if (ServerConnection.GAME.errored) {
            ServerConnection.GAME.errored = false;
            ConnectionManager.disconnect();
            return;
        }

        if (!MiniMenu.open) {
            MiniMenu.reset();
        }

        for (@Pc(34) int i = 0; i < 100; i++) {
            if (!Static236.readPacket(ServerConnection.GAME)) {
                break;
            }
        }

        if (step != MainLogicStep.STEP_GAME_SCREEN) {
            return;
        }

        while (Static232.reflectionCheckRunning()) {
            @Pc(71) ClientMessage message = ClientMessage.create(ClientProt.REFLECTION_CHECK_REPLY, ServerConnection.GAME.isaac);
            message.bitPacket.p1(0);
            @Pc(80) int pos = message.bitPacket.pos;
            Static437.sendReflectionCheckReply(message.bitPacket);
            message.bitPacket.psize1(message.bitPacket.pos - pos);
            ServerConnection.GAME.send(message);
        }

        if (Static211.pingRequest == null) {
            if (Static675.nextPing <= SystemTimer.safetime()) {
                Static211.pingRequest = Static151.pingWorker.ping(ConnectionInfo.login.address);
            }
        } else if (Static211.pingRequest.ping != -1) {
            @Pc(71) ClientMessage message = ClientMessage.create(ClientProt.PING_STATISTICS, ServerConnection.GAME.isaac);
            message.bitPacket.p2(Static211.pingRequest.ping);
            ServerConnection.GAME.send(message);
            Static211.pingRequest = null;
            Static675.nextPing = SystemTimer.safetime() + 30000L;
        }

        @Pc(166) MouseLog mouseLog = (MouseLog) Static226.mouseLogs.first();
        if (mouseLog != null || Static56.lastMouseMovement < SystemTimer.safetime() - 2000L) {
            @Pc(179) ClientMessage message = null;
            @Pc(181) int pos = 0;

            for (@Pc(186) MouseLog local186 = (MouseLog) Static677.mouseMovements.first(); local186 != null && (message == null || message.bitPacket.pos - pos < 240); local186 = (MouseLog) Static677.mouseMovements.next()) {
                local186.unlink();

                @Pc(208) int y = local186.getY();
                if (y < -1) {
                    y = -1;
                } else if (y > 65534) {
                    y = 65534;
                }

                @Pc(226) int x = local186.getX();
                if (x < -1) {
                    x = -1;
                } else if (x > 65534) {
                    x = 65534;
                }

                if (Static172.lastMouseX != x || y != Static634.lastMouseY) {
                    if (message == null) {
                        message = ClientMessage.create(ClientProt.EVENT_MOUSE_MOVE, ServerConnection.GAME.isaac);
                        message.bitPacket.p1(0);
                        pos = message.bitPacket.pos;
                    }

                    @Pc(282) int deltaX = x - Static172.lastMouseX;
                    Static172.lastMouseX = x;

                    @Pc(288) int deltaY = y - Static634.lastMouseY;
                    Static634.lastMouseY = y;

                    @Pc(300) int deltaTime = (int) ((local186.getTime() - Static56.lastMouseMovement) / 20L);
                    if (deltaTime < 8 && deltaX >= -32 && deltaX <= 31 && deltaY >= -32 && deltaY <= 31) {
                        deltaX += 32;
                        deltaY += 32;
                        message.bitPacket.p2((deltaTime << 12) + (deltaX << 6) + deltaY);
                    } else if (deltaTime < 32 && deltaX >= -128 && deltaX <= 127 && deltaY >= -128 && deltaY <= 127) {
                        message.bitPacket.p1(deltaTime + 128);
                        deltaX += 128;
                        deltaY += 128;
                        message.bitPacket.p2(deltaY + (deltaX << 8));
                    } else if (deltaTime >= 32) {
                        message.bitPacket.p2(deltaTime + 57344);
                        if (x == 1 || y == -1) {
                            message.bitPacket.p4(Integer.MIN_VALUE);
                        } else {
                            message.bitPacket.p4(x | y << 16);
                        }
                    } else {
                        message.bitPacket.p1(deltaTime + 192);
                        if (x == 1 || y == -1) {
                            message.bitPacket.p4(Integer.MIN_VALUE);
                        } else {
                            message.bitPacket.p4(y << 16 | x);
                        }
                    }

                    Static56.lastMouseMovement = local186.getTime();
                }
            }

            if (message != null) {
                message.bitPacket.psize1(message.bitPacket.pos - pos);
                ServerConnection.GAME.send(message);
            }
        }

        if (mouseLog != null) {
            @Pc(527) long deltaTime = (mouseLog.getTime() - Static180.lastMouseClick) / 50L;
            if (deltaTime > 32767L) {
                deltaTime = 32767L;
            }

            Static180.lastMouseClick = mouseLog.getTime();

            @Pc(541) int y = mouseLog.getY();
            if (y < 0) {
                y = 0;
            } else if (y > 65535) {
                y = 65535;
            }

            @Pc(208) int x = mouseLog.getX();
            if (x < 0) {
                x = 0;
            } else if (x > 65535) {
                x = 65535;
            }

            @Pc(581) byte rightClick = 0;
            if (mouseLog.getType() == MouseLog.TYPE_PRESS_RIGHT) {
                rightClick = 1;
            }

            @Pc(282) int time = (int) deltaTime;
            @Pc(603) ClientMessage local603 = ClientMessage.create(ClientProt.EVENT_MOUSE_CLICK, ServerConnection.GAME.isaac);
            local603.bitPacket.p2_alt3((rightClick << 15) | time);
            local603.bitPacket.p4_alt2(x | (y << 16));
            ServerConnection.GAME.send(local603);
        }

        if (Static216.keyPressCount > 0) {
            @Pc(179) ClientMessage message = ClientMessage.create(ClientProt.EVENT_KEYBOARD, ServerConnection.GAME.isaac);
            message.bitPacket.p1(Static216.keyPressCount * 3);

            for (@Pc(181) int i = 0; i < Static216.keyPressCount; i++) {
                @Pc(652) KeyLog keyLog = Static591.AN_KEYBOARD_EVENT_ARRAY_2[i];

                @Pc(660) long deltaTime = (keyLog.getTime() - Static351.lastKeyPress) / 50L;
                if (deltaTime > 65535L) {
                    deltaTime = 65535L;
                }

                Static351.lastKeyPress = keyLog.getTime();
                message.bitPacket.p1(keyLog.getKeyCode());
                message.bitPacket.p2((int) deltaTime);
            }

            ServerConnection.GAME.send(message);
        }

        if (Static232.cameraNotifyDelay > 0) {
            Static232.cameraNotifyDelay--;
        }

        if (Camera.angleUpdated && Static232.cameraNotifyDelay <= 0) {
            Camera.angleUpdated = false;
            Static232.cameraNotifyDelay = 20;
            @Pc(179) ClientMessage message = ClientMessage.create(ClientProt.EVENT_CAMERA_POSITION, ServerConnection.GAME.isaac);
            message.bitPacket.p2((int) Camera.playerCameraPitch >> 3);
            message.bitPacket.p2((int) Camera.playerCameraYaw >> 3);
            ServerConnection.GAME.send(message);
        }

        if (GameShell.focus != Static50.previousFocus) {
            Static50.previousFocus = GameShell.focus;
            @Pc(179) ClientMessage message = ClientMessage.create(ClientProt.EVENT_APPLET_FOCUS, ServerConnection.GAME.isaac);
            message.bitPacket.p1(GameShell.focus ? 1 : 0);
            ServerConnection.GAME.send(message);
        }

        if (!Static503.sentPreferences) {
            @Pc(179) ClientMessage message = ClientMessage.create(ClientProt.CLIENT_DETAILOPTIONS_STATUS, ServerConnection.GAME.isaac);
            message.bitPacket.p1(0);
            @Pc(181) int pos = message.bitPacket.pos;
            @Pc(810) Packet packet = ClientOptions.instance.encode();
            message.bitPacket.pdata(packet.pos, packet.data, 0);
            message.bitPacket.psize1(message.bitPacket.pos - pos);
            ServerConnection.GAME.send(message);
            Static503.sentPreferences = true;
        }

        if (Static334.activeTiles != null) {
            if (Camera.mode == CameraMode.MODE_FOLLOWPLAYER) {
                Camera.moveToTick();
            } else if (Camera.mode == CameraMode.MODE_SPLINE) {
                Camera.splineTick();
            }
        }

        if (Camera.angleChangingY) {
            Camera.angleChangingY = false;
        } else {
            Camera.angleAxisY /= 2.0F;
        }

        if (Camera.angleChangingX) {
            Camera.angleChangingX = false;
        } else {
            Camera.angleAxisX /= 2.0F;
        }

        Static630.method8358();

        if (step != MainLogicStep.STEP_GAME_SCREEN) {
            return;
        }

        Static159.method2575();
        Static271.processLocChanges();
        AudioRenderer.render();
        ServerConnection.GAME.idleReadTicks++;

        if (ServerConnection.GAME.idleReadTicks > 750) {
            ConnectionManager.disconnect();
            return;
        }

        if (CutsceneManager.state == 0) {
            EntityTick.tickPlayers();
            EntityTick.tickNpcs();
        } else {
            if (CutsceneManager.state == 1 && Static360.loadCutscene(CutsceneManager.id)) {
                Static266.method6774();
                CutsceneManager.state = 2;
            }

            if (CutsceneManager.state == 2 && step != 12) {
                CutsceneVarDomain.cache.clear();
                Static440.anInt6680 = 0;
                CutsceneManager.clock = TimeUtils.clock;
                CutsceneManager.state = 3;
                Static457.applyCutsceneFovClamps();
            }

            if (CutsceneManager.state == 3) {
                @Pc(80) int local80 = TimeUtils.clock - CutsceneManager.clock;
                if (Static401.aCutsceneActionArray1.length > Static440.anInt6680) {
                    do {
                        @Pc(982) CutsceneAction local982 = Static401.aCutsceneActionArray1[Static440.anInt6680];
                        if (local982.startTime > local80) {
                            break;
                        }
                        local982.execute();
                    } while (CutsceneManager.state == 3 && ++Static440.anInt6680 < Static401.aCutsceneActionArray1.length);
                }

                if (CutsceneManager.state == 3) {
                    for (@Pc(181) int local181 = 0; local181 < CutsceneManager.actors.length; local181++) {
                        @Pc(1027) Actor actor = CutsceneManager.actors[local181];
                        if (actor.initialised) {
                            @Pc(1034) PathingEntity entity = actor.entity();
                            EntityTick.tick(true, entity);
                        }
                    }
                }
            }
        }

        Static90.method1733();

        if (!Static288.aBoolean356) {
            Static598.updateSpotAnimations();
            Static288.aBoolean356 = true;
        }

        for (@Pc(80) int local80 = TimedVarDomain.instance.removeNext(true); local80 != -1; local80 = TimedVarDomain.instance.removeNext(false)) {
            method977(local80);
            Static142.anIntArray225[Static635.varpUpdateCount++ & 0x1F] = local80;
        }

        for (@Pc(1099) DelayedStateChange change = DelayedStateChange.removeFirst(); change != null; change = DelayedStateChange.removeFirst()) {
            @Pc(541) int type = change.getType();
            @Pc(660) long value = change.getValue();

            if (type == DelayedStateChange.TYPE_SETVARC) {
                Static511.varcs[(int) value] = change.primaryData;
                Static624.varcSaveRecommended |= Static118.permVarcs[(int) value];
                Static278.anIntArray350[Static52.varcUpdateCount++ & 0x1F] = (int) value;
            } else if (type == DelayedStateChange.TYPE_SETVARCSTR) {
                Static37.varcstrs[(int) value] = change.stringData;
                Static268.anIntArray332[Static455.varcstrUpdateCount++ & 0x1F] = (int) value;
            } else if (type == DelayedStateChange.TYPE_IF_SETTEXT) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                if (!change.stringData.equals(component.text)) {
                    component.text = change.stringData;
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETMODEL) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                @Pc(288) int objType = change.primaryData;
                @Pc(300) int obj = change.secondaryData;
                @Pc(1739) int objData = change.tertiaryData;
                if (objType != component.objType || obj != component.obj || objData != component.objData) {
                    component.objData = objData;
                    component.objType = objType;
                    component.obj = obj;
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETMODELANIM) {
                @Pc(1143) Component component = InterfaceList.list((int) value);

                if (component.modelAnimation != change.primaryData) {
                    if (change.primaryData == -1) {
                        component.animator = null;
                    } else {
                        if (component.animator == null) {
                            component.animator = new ComponentAnimator();
                        }

                        component.animator.update(true, change.primaryData);
                    }

                    component.modelAnimation = change.primaryData;
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETCOLOUR) {
                @Pc(282) int rgb15 = change.primaryData;
                @Pc(288) int red = rgb15 >> 10 & 0x1F;
                @Pc(300) int green = rgb15 >> 5 & 0x1F;
                @Pc(1739) int blue = rgb15 & 0x1F;
                @Pc(1751) int rgb24 = (red << 19) + (green << 11) + (blue << 3);
                @Pc(1756) Component component = InterfaceList.list((int) value);
                if (component.colour != rgb24) {
                    component.colour = rgb24;
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETHIDE) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                @Pc(1701) boolean hidden = change.primaryData == 1;
                if (hidden != component.hidden) {
                    component.hidden = hidden;
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETMODELANGLE) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                if (change.primaryData != component.xan2d || change.secondaryData != component.yan2d || component.zoom2d != change.tertiaryData) {
                    component.xan2d = change.primaryData;
                    component.yan2d = change.secondaryData;
                    component.zoom2d = change.tertiaryData;
                    if (component.invObject != -1) {
                        if (component.modelAspectRatioX > 0) {
                            component.zoom2d = component.zoom2d * 32 / component.modelAspectRatioX;
                        } else if (component.originalWidth > 0) {
                            component.zoom2d = component.zoom2d * 32 / component.originalWidth;
                        }
                    }
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETOBJECT) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                if (change.primaryData != component.invObject || change.secondaryData != component.invCount) {
                    component.invCount = change.secondaryData;
                    component.invObject = change.primaryData;
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETMODELOFFSET) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                if (change.primaryData != component.xof2d || component.yof2d != change.secondaryData || change.tertiaryData != component.zan2d) {
                    component.xof2d = change.primaryData;
                    component.yof2d = change.secondaryData;
                    component.zan2d = change.tertiaryData;
                    InterfaceManager.redraw(component);
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETPOSITION) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                component.reposModeY = 0;
                component.y = component.originalY = change.secondaryData;
                component.x = component.originalX = change.primaryData;
                component.reposModeX = 0;
                InterfaceManager.redraw(component);
            } else if (type == DelayedStateChange.TYPE_IF_SETSCROLLPOS) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                @Pc(288) int scrollY = change.primaryData;
                if (component != null && component.type == 0) {
                    if (scrollY > component.scrollHeight - component.height) {
                        scrollY = component.scrollHeight - component.height;
                    }
                    if (scrollY < 0) {
                        scrollY = 0;
                    }
                    if (component.scrollY != scrollY) {
                        component.scrollY = scrollY;
                        InterfaceManager.redraw(component);
                    }
                }
            } else if (type == DelayedStateChange.TYPE_IF_SETGRAPHIC) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                component.graphic = change.primaryData;
            } else if (type == DelayedStateChange.TYPE_SETMAPFLAG) {
                Minimap.flagY = change.secondaryData;
                Minimap.flagSet = true;
                Minimap.flagX = change.primaryData;
            } else if (type == DelayedStateChange.TYPE_IF_SETTEXTFONT) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                component.fontGraphic = change.primaryData;
            } else if (type == DelayedStateChange.TYPE_IF_SETFONTMONO) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                component.fontMonospaced = change.primaryData == 1;
            } else if (type == DelayedStateChange.TYPE_IF_SETCLICKMASK) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                component.clickMask = change.primaryData == 1;
            } else if (type == DelayedStateChange.TYPE_IF_SETVIDEO) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                component.video = change.primaryData;
            } else if (type == DelayedStateChange.TYPE_IF_SETRECOL) {
                @Pc(1143) Component local1143 = InterfaceList.list((int) value);
                @Pc(288) int index = (int) (value >> 32);
                local1143.setRecol((short) change.secondaryData, index, (short) change.primaryData);
            } else if (type == DelayedStateChange.TYPE_IF_SETRETEX) {
                @Pc(1143) Component component = InterfaceList.list((int) value);
                @Pc(288) int index = (int) (value >> 32);
                component.setRetex((short) change.primaryData, index, (short) change.secondaryData);
            }
        }

        Static35.currentTick++;

        if (Static616.crossType != 0) {
            Static481.crossDuration += 20;
            if (Static481.crossDuration >= 400) {
                Static616.crossType = 0;
            }
        }

        if (Static67.aComponent_10 != null) {
            Static499.anInt7501++;

            if (Static499.anInt7501 >= 15) {
                InterfaceManager.redraw(Static67.aComponent_10);
                Static67.aComponent_10 = null;
            }
        }

        WorldMap.component = null;
        InterfaceManager.aBoolean428 = false;
        Static702.aBoolean797 = false;
        InterfaceManager.dragTarget = null;
        WorldMap.setOptions(-1, -1, null);

        if (!InterfaceManager.targetMode) {
            InterfaceManager.targetEndCursor = -1;
        }

        Static443.method5981();
        World.tick++;

        if (WorldMap.clicked) {
            @Pc(1980) ClientMessage message = ClientMessage.create(ClientProt.CLICKWORLDMAP, ServerConnection.GAME.isaac);
            message.bitPacket.p4_alt3(WorldMap.clickedY | WorldMap.clickedLevel << 28 | WorldMap.clickedX << 14);
            ServerConnection.GAME.send(message);
            WorldMap.clicked = false;
        }

        while (true) {
            @Pc(2006) HookRequest local2006;
            @Pc(2026) Component local2026;
            @Pc(2011) Component local2011;
            do {
                local2006 = (HookRequest) Static618.A_DEQUE___68.removeFirst();
                if (local2006 == null) {
                    while (true) {
                        do {
                            local2006 = (HookRequest) Static59.A_DEQUE___33.removeFirst();
                            if (local2006 == null) {
                                while (true) {
                                    do {
                                        local2006 = (HookRequest) InterfaceManager.hookRequests.removeFirst();
                                        if (local2006 == null) {
                                            if (WorldMap.component == null) {
                                                MiniMenu.anInt6964 = 0;
                                            }
                                            if (InterfaceManager.dragSource != null) {
                                                InterfaceDrag.tick();
                                            }
                                            if (Client.staffModLevel > 0 && KeyboardMonitor.instance.isPressed(82) && KeyboardMonitor.instance.isPressed(81) && Static611.mouseWheelRotation != 0) {
                                                @Pc(541) int local541 = PlayerEntity.self.level - Static611.mouseWheelRotation;
                                                if (local541 < 0) {
                                                    local541 = 0;
                                                } else if (local541 > 3) {
                                                    local541 = 3;
                                                }
                                                Static624.teleport(local541, PlayerEntity.self.pathZ[0] + WorldMap.areaBaseZ, WorldMap.areaBaseX - -PlayerEntity.self.pathX[0]);
                                            }
                                            MiniMenu.update();
                                            for (@Pc(541) int local541 = 0; local541 < 5; local541++) {
                                                @Pc(2246) int local2246 = Shake.time[local541]++;
                                            }
                                            if (Static624.varcSaveRecommended && Static98.lastVarcSave < SystemTimer.safetime() - TimeUtils.MILLISECONDS_PER_MINUTE) {
                                                Static266.saveVarcs();
                                            }
                                            for (@Pc(2281) FriendNotification local2281 = (FriendNotification) FriendsList.notifications.first(); local2281 != null; local2281 = (FriendNotification) FriendsList.notifications.next()) {
                                                if (SystemTimer.safetime() / 1000L - 5L > (long) local2281.arrivalTime) {
                                                    if (local2281.world > 0) {
                                                        ChatHistory.add(5, 0, "", "", "", local2281.name + LocalisedText.FRIENDLOGIN.localise(Client.language));
                                                    }
                                                    if (local2281.world == 0) {
                                                        ChatHistory.add(5, 0, "", "", "", local2281.name + LocalisedText.FRIENDLOGOUT.localise(Client.language));
                                                    }
                                                    local2281.unlink();
                                                }
                                            }
                                            if (14590 != 14590) {
                                                Camera.moveToZ = -107;
                                            }
                                            Static392.anInt6143++;
                                            if (Static392.anInt6143 > 500) {
                                                Static392.anInt6143 = 0;
                                                @Pc(226) int local226 = (int) (Math.random() * 8.0D);
                                                if ((local226 & 0x2) == 2) {
                                                    Static145.anInt2561 += Static374.anInt5906;
                                                }
                                                if ((local226 & 0x1) == 1) {
                                                    Static508.anInt7627 += Static555.anInt8305;
                                                }
                                                if ((local226 & 0x4) == 4) {
                                                    Static288.anInt4621 += Static709.anInt10669;
                                                }
                                            }
                                            if (Static508.anInt7627 < -50) {
                                                Static555.anInt8305 = 2;
                                            }
                                            if (Static508.anInt7627 > 50) {
                                                Static555.anInt8305 = -2;
                                            }
                                            if (Static145.anInt2561 < -55) {
                                                Static374.anInt5906 = 2;
                                            }
                                            if (Static288.anInt4621 < -40) {
                                                Static709.anInt10669 = 1;
                                            }
                                            if (Static145.anInt2561 > 55) {
                                                Static374.anInt5906 = -2;
                                            }
                                            Static439.anInt6675++;
                                            if (Static288.anInt4621 > 40) {
                                                Static709.anInt10669 = -1;
                                            }
                                            if (Static439.anInt6675 > 500) {
                                                Static439.anInt6675 = 0;
                                                @Pc(226) int local226 = (int) (Math.random() * 8.0D);
                                                if ((local226 & 0x1) == 1) {
                                                    Camera.yawOffset += Static653.anInt9718;
                                                }
                                                if ((local226 & 0x2) == 2) {
                                                    Camera.scaleOffset += Static171.anInt2887;
                                                }
                                            }
                                            if (Camera.yawOffset < -60) {
                                                Static653.anInt9718 = 2;
                                            }
                                            if (Camera.yawOffset > 60) {
                                                Static653.anInt9718 = -2;
                                            }
                                            if (Camera.scaleOffset < -20) {
                                                Static171.anInt2887 = 1;
                                            }
                                            ServerConnection.GAME.idleWriteTicks++;
                                            if (Camera.scaleOffset > 10) {
                                                Static171.anInt2887 = -1;
                                            }
                                            if (ServerConnection.GAME.idleWriteTicks > 50) {
                                                @Pc(2571) ClientMessage local2571 = ClientMessage.create(ClientProt.NO_TIMEOUT, ServerConnection.GAME.isaac);
                                                ServerConnection.GAME.send(local2571);
                                            }
                                            if (VerifyId.isTransmitRequested()) {
                                                VerifyId.transmit();
                                                VerifyId.cancelTransmitRequest();
                                            }
                                            try {
                                                ServerConnection.GAME.flush();
                                                return;
                                            } catch (@Pc(2588) IOException local2588) {
                                                ConnectionManager.disconnect();
                                                return;
                                            }
                                        }
                                        local2011 = local2006.source;
                                        if (local2011.id < 0) {
                                            break;
                                        }
                                        local2026 = InterfaceList.list(local2011.layer);
                                    } while (local2026 == null || local2026.staticComponents == null || local2026.staticComponents.length <= local2011.id || local2011 != local2026.staticComponents[local2011.id]);
                                    ScriptRunner.executeHookInner(local2006);
                                }
                            }
                            local2011 = local2006.source;
                            if (local2011.id < 0) {
                                break;
                            }
                            local2026 = InterfaceList.list(local2011.layer);
                        } while (local2026 == null || local2026.staticComponents == null || local2011.id >= local2026.staticComponents.length || local2026.staticComponents[local2011.id] != local2011);
                        ScriptRunner.executeHookInner(local2006);
                    }
                }
                local2011 = local2006.source;
                if (local2011.id < 0) {
                    break;
                }
                local2026 = InterfaceList.list(local2011.layer);
            } while (local2026 == null || local2026.staticComponents == null || local2026.staticComponents.length <= local2011.id || local2026.staticComponents[local2011.id] != local2011);
            ScriptRunner.executeHookInner(local2006);
        }
    }

    @OriginalMember(owner = "client!uh", name = "a", descriptor = "(B)Z")
    public static boolean isNotLoading() {
        return step >= MainLogicStep.STEP_LOADING_1;
    }

}

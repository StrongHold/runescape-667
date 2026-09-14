import com.jagex.core.constants.MainLogicStep;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Renderer;
import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.core.io.ConnectionInfo;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.sign.SignLink;
import com.jagex.core.constants.ModeWhere;
import com.jagex.core.io.Files;
import com.jagex.core.stringtools.general.Cp1252;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import com.jagex.game.LocalisedText;
import com.jagex.game.compression.huffman.WordPack;
import com.jagex.game.runetek6.config.meltype.MapElementTypeList;
import com.jagex.game.runetek6.config.msitype.MSITypeList;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.js5;
import jagex3.jagmisc.jagmisc;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class debugconsole {

    @OriginalMember(owner = "client!oia", name = "a", descriptor = "Ljava/util/Calendar;")
    public static final Calendar GMT_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    @OriginalMember(owner = "client!dha", name = "v", descriptor = "Ljava/lang/String;")
    public static String currententry = "";

    @OriginalMember(owner = "client!sn", name = "h", descriptor = "I")
    public static int currententryLength = 0;

    @OriginalMember(owner = "client!mfa", name = "b", descriptor = "[Ljava/lang/String;")
    public static String[] lines;

    @OriginalMember(owner = "client!qba", name = "l", descriptor = "I")
    public static int lineCount = 0;

    @OriginalMember(owner = "client!eda", name = "k", descriptor = "Ljava/io/FileOutputStream;")
    public static FileOutputStream output;

    @OriginalMember(owner = "client!gl", name = "a", descriptor = "I")
    public static int anInt3471 = 0;

    @OriginalMember(owner = "client!hfa", name = "u", descriptor = "Z")
    public static boolean open = false;

    @OriginalMember(owner = "client!fa", name = "s", descriptor = "I")
    public static int p12VerticalPadding;

    @OriginalMember(owner = "client!ufa", name = "h", descriptor = "I")
    public static int b12VerticalPadding;

    @OriginalMember(owner = "client!rr", name = "j", descriptor = "I")
    public static int anInt8472 = 0;

    @OriginalMember(owner = "client!cn", name = "a", descriptor = "(Ljava/lang/String;B)V")
    public static void set(@OriginalArg(0) String currententry) {
        debugconsole.currententry = currententry;
        debugconsole.currententryLength = debugconsole.currententry.length();
    }

    @OriginalMember(owner = "client!cja", name = "a", descriptor = "(BLjava/lang/String;)V")
    public static void addline(@OriginalArg(1) String text) {
        if (lines == null) {
            reset();
        }

        GMT_CALENDAR.setTime(new Date(SystemTimer.safetime()));
        @Pc(20) int hour = GMT_CALENDAR.get(Calendar.HOUR_OF_DAY);
        @Pc(31) int minute = GMT_CALENDAR.get(Calendar.MINUTE);
        @Pc(35) int second = GMT_CALENDAR.get(Calendar.SECOND);
        @Pc(69) String time = Integer.toString(hour / 10) + hour % 10 + ":" + minute / 10 + minute % 10 + ":" + second / 10 + second % 10;

        @Pc(74) String[] lines = StringTools.split(text, '\n');
        for (@Pc(76) int i = 0; i < lines.length; i++) {
            for (@Pc(79) int j = lineCount; j > 0; j--) {
                debugconsole.lines[j] = debugconsole.lines[j - 1];
            }

            debugconsole.lines[0] = time + ": " + lines[i];

            if (output != null) {
                try {
                    output.write(Cp1252.encode(debugconsole.lines[0] + "\n"));
                } catch (@Pc(129) IOException ignored) {
                    /* empty */
                }
            }

            if (lineCount < debugconsole.lines.length - 1) {
                lineCount++;

                if (anInt3471 > 0) {
                    anInt3471++;
                }
            }
        }
    }

    @OriginalMember(owner = "client!io", name = "a", descriptor = "(I)V")
    public static void reset() {
        p12VerticalPadding = Fonts.p12Metrics.paddingTop + Fonts.p12Metrics.paddingBottom + 2;
        lines = new String[500];
        b12VerticalPadding = Fonts.b12Metrics.paddingBottom + Fonts.b12Metrics.paddingTop + 2;

        for (@Pc(35) int i = 0; i < lines.length; i++) {
            lines[i] = "";
        }

        addline(LocalisedText.DEBUG_CONSOLE_INFO.localise(Client.language));
    }

    @OriginalMember(owner = "client!tca", name = "bb", descriptor = "(I)Z")
    public static boolean isOpen() {
        return open;
    }

    @OriginalMember(owner = "client!rd", name = "a", descriptor = "(Lclient!ha;I)V")
    public static void draw(@OriginalArg(0) Toolkit toolkit) {
        @Pc(7) int local7 = 0;
        @Pc(9) int local9 = 0;
        if (OrthoMode.toolkitActive) {
            local7 = OrthoMode.method2283();
            local9 = Static422.method5771();
        }
        toolkit.KA(local7, local9, GameShell.canvasWid + local7, local9 + 350);
        toolkit.aa(local7, local9, GameShell.canvasWid, 350, anInt8472 << 24 | 0x332277, 1);
        OrthoMode.method8927(local7, GameShell.canvasWid + local7, local9, local9 + 350);

        @Pc(54) int local54 = 350 / p12VerticalPadding;

        if (lineCount > 0) {
            @Pc(65) int local65 = 346 - p12VerticalPadding - 4;
            @Pc(75) int local75 = local54 * local65 / (local54 + lineCount - 1);
            @Pc(77) int local77 = 4;
            if (lineCount > 1) {
                local77 = (lineCount - anInt3471 - 1) * (local65 + -local75) / (lineCount - 1) + 4;
            }
            toolkit.aa(local7 + GameShell.canvasWid - 16, local9 + local77, 12, local75, anInt8472 << 24 | 0x332277, 2);

            for (@Pc(119) int local119 = anInt3471; anInt3471 + local54 > local119 && lineCount > local119; local119++) {
                @Pc(128) String[] local128 = StringTools.split(lines[local119], '\b');
                @Pc(137) int local137 = (GameShell.canvasWid - 8 - 16) / local128.length;
                for (@Pc(139) int local139 = 0; local139 < local128.length; local139++) {
                    @Pc(147) int local147 = local139 * local137 + 8;
                    toolkit.KA(local147 + local7, local9, local147 + local7 + local137 - 8, local9 + 350);
                    Fonts.p12.render(Static386.method5439(local128[local139]), local7 + local147, -Fonts.p12Metrics.paddingBottom + -2 + local9 + 350 + -b12VerticalPadding + -((local119 + -anInt3471) * p12VerticalPadding), -16777216, -1);
                }
            }
        }

        Fonts.p11.renderRight("Build: 667", GameShell.canvasWid + local7 - 25, local9 + 350 - 20, -1, -16777216);
        toolkit.KA(local7, local9, GameShell.canvasWid + local7, local9 + 350);
        toolkit.horizontalLine(local7, local9 + 350 - b12VerticalPadding, GameShell.canvasWid, -1);
        Fonts.b12.render("--> " + Static386.method5439(currententry), local7 + 10, local9 - (Fonts.b12Metrics.paddingBottom + -350 + 1), -16777216, -1);
        if (!GameShell.focus) {
            return;
        }

        @Pc(65) int colour = -1;
        if (TimeUtils.clock % 30 > 15) {
            colour = 0xFFFFFF;
        }

        toolkit.verticalLine(Fonts.b12Metrics.stringWidth("--> " + Static386.method5439(currententry).substring(0, currententryLength)) + local7 + 10, local9 + 350 - Fonts.b12Metrics.paddingBottom - 11, 12, colour);
    }

    @OriginalMember(owner = "client!hd", name = "a", descriptor = "(IZZLjava/lang/String;)V")
    public static void executeComand(@OriginalArg(1) boolean automatic, @OriginalArg(2) boolean retainText, @OriginalArg(3) String command) {
        try {
            if (command.equalsIgnoreCase("commands") || command.equalsIgnoreCase("help")) {
                addline("commands - This command");
                addline("cls - Clear console");
                addline("displayfps - Toggle FPS and other information");
                addline("renderer - Print graphics renderer information");
                addline("heap - Print java memory information");
                addline("getcamerapos - Print location and direction of camera for use in bug reports");
                return;
            }
            if (command.equalsIgnoreCase("cls")) {
                anInt3471 = 0;
                lineCount = 0;
                return;
            }
            if (command.equalsIgnoreCase("displayfps")) {
                Client.displayFps = !Client.displayFps;
                if (!Client.displayFps) {
                    addline("FPS off");
                    return;
                }
                addline("FPS on");
                return;
            }
            if (command.equals("renderer")) {
                @Pc(103) Renderer renderer = Toolkit.active.renderer();
                addline("Vendor: " + renderer.vendor);
                addline("Name: " + renderer.name);
                addline("Version: " + renderer.version);
                addline("Device: " + renderer.device);
                addline("Driver Version: " + renderer.driverVersion);
                return;
            }
            if (command.equals("heap")) {
                addline("Heap: " + GameShell.maxmemory + "MB");
                return;
            }
            if (command.equalsIgnoreCase("getcamerapos")) {
                addline("Pos: " + PlayerEntity.self.level + "," + ((Camera.x >> 9) + WorldMap.areaBaseX >> 6) + "," + ((Camera.z >> 9) + WorldMap.areaBaseZ >> 6) + "," + ((Camera.x >> 9) + WorldMap.areaBaseX & 0x3F) + "," + ((Camera.z >> 9) + WorldMap.areaBaseZ & 0x3F) + " Height: " + (Static102.averageHeight(PlayerEntity.self.level, Camera.x, Camera.z) - Camera.y));
                addline("Look: " + PlayerEntity.self.level + "," + (Camera.lookX + WorldMap.areaBaseX >> 6) + "," + (WorldMap.areaBaseZ + Camera.lookZ >> 6) + "," + (WorldMap.areaBaseX + Camera.lookX & 0x3F) + "," + (WorldMap.areaBaseZ + Camera.lookZ & 0x3F) + " Height: " + (Static102.averageHeight(PlayerEntity.self.level, Camera.lookX, Camera.lookZ) - Camera.lookY));
                return;
            }
        } catch (@Pc(323) Exception ignored) {
            addline(LocalisedText.DEBUG_CONSOLE_ERROR.localise(Client.language));
            return;
        }

        if (ModeWhere.LIVE != Client.modeWhere || Client.staffModLevel >= 2) {
            if (command.equalsIgnoreCase("errortest")) {
                throw new RuntimeException();
            }

            if (command.equals("nativememerror")) {
                throw new OutOfMemoryError("native(MPR");
            }

            try {
                if (command.equalsIgnoreCase("printfps")) {
                    addline("FPS: " + GameShell.currentFps);
                    return;
                }
                if (command.equalsIgnoreCase("occlude")) {
                    Static18.occlude = !Static18.occlude;

                    if (Static18.occlude) {
                        addline("Occlsion now on!");
                        return;
                    } else {
                        addline("Occlsion now off!");
                        return;
                    }
                }
                if (command.equalsIgnoreCase("fpson")) {
                    Client.displayFps = true;
                    addline("fps debug enabled");
                    return;
                }
                if (command.equalsIgnoreCase("fpsoff")) {
                    Client.displayFps = false;
                    addline("fps debug disabled");
                    return;
                }
                if (command.equals("systemmem")) {
                    try {
                        addline("System memory: " + jagmisc.getAvailablePhysicalMemory() / 0x100000L + "/" + SystemInfo.instance.totalMemory + "Mb");
                        return;
                    } catch (@Pc(474) Throwable ignored) {
                        return;
                    }
                }
                if (command.equalsIgnoreCase("cleartext")) {
                    TextCoordList.textCoords.clear();
                    addline("Text coords cleared");
                    return;
                }
                if (command.equalsIgnoreCase("gc")) {
                    Static664.cacheRemoveSoftReferences();
                    for (@Pc(501) int local501 = 0; local501 < 10; local501++) {
                        System.gc();
                    }
                    @Pc(511) Runtime local511 = Runtime.getRuntime();
                    @Pc(521) int local521 = (int) ((local511.totalMemory() - local511.freeMemory()) / 1024L);
                    addline("mem=" + local521 + "k");
                    return;
                }
                if (command.equalsIgnoreCase("compact")) {
                    Static664.cacheRemoveSoftReferences();
                    for (@Pc(501) int local501 = 0; local501 < 10; local501++) {
                        System.gc();
                    }
                    @Pc(511) Runtime local511 = Runtime.getRuntime();
                    @Pc(521) int local521 = (int) ((local511.totalMemory() - local511.freeMemory()) / 1024L);
                    addline("Memory before cleanup=" + local521 + "k");
                    Static358.method9191();
                    Static664.cacheRemoveSoftReferences();
                    for (@Pc(582) int local582 = 0; local582 < 10; local582++) {
                        System.gc();
                    }
                    local521 = (int) ((local511.totalMemory() - local511.freeMemory()) / 1024L);
                    addline("Memory after cleanup=" + local521 + "k");
                    return;
                }
                if (command.equalsIgnoreCase("unloadnatives")) {
                    addline(GameShell.unloadNatives() ? "Libraries unloaded" : "Library unloading failed!");
                    return;
                }
                if (command.equalsIgnoreCase("clientdrop")) {
                    addline("Dropped client connection");
                    if (MainLogicManager.step == MainLogicStep.STEP_GAME_SCREEN) {
                        ConnectionManager.disconnect();
                    } else if (MainLogicManager.step == MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD) {
                        ServerConnection.GAME.errored = true;
                        return;
                    }
                    return;
                }
                if (command.equalsIgnoreCase("rotateconnectmethods")) {
                    ConnectionInfo.login.rotateMethods();
                    addline("Rotated connection methods");
                    return;
                }
                if (command.equalsIgnoreCase("clientjs5drop")) {
                    Client.js5WorkerThread.close();
                    addline("Dropped client js5 net queue");
                    return;
                }
                if (command.equalsIgnoreCase("serverjs5drop")) {
                    Client.js5WorkerThread.disconnect();
                    addline("Dropped server js5 net queue");
                    return;
                }
                if (command.equalsIgnoreCase("breakcon")) {
                    GameShell.signLink.timeout();
                    @Pc(723) ServerConnection[] connections = ServerConnection.VALUES;
                    for (@Pc(725) int i = 0; i < connections.length; i++) {
                        @Pc(730) ServerConnection connection = connections[i];
                        if (connection.connection != null) {
                            connection.connection.breakConnection();
                        }
                    }
                    Client.js5WorkerThread.stop();
                    addline("Breaking new connections for 5 seconds");
                    return;
                }
                if (command.equalsIgnoreCase("rebuild")) {
                    MainLogicManager.mapBuild();
                    Minimap.reset();
                    addline("Rebuilding map");
                    return;
                }
                if (command.equalsIgnoreCase("rebuildprofile")) {
                    Static690.aLong318 = SystemTimer.safetime();
                    Static28.aBoolean43 = true;
                    MainLogicManager.mapBuild();
                    Minimap.reset();
                    addline("Rebuilding map (with profiling)");
                    return;
                }
                if (command.equalsIgnoreCase("wm1")) {
                    InterfaceManager.changeWindowMode(1, -1, -1, false);
                    if (InterfaceManager.getWindowMode() != 1) {
                        addline("wm1 failed");
                        return;
                    }
                    addline("wm1 succeeded");
                    return;
                }
                if (command.equalsIgnoreCase("wm2")) {
                    InterfaceManager.changeWindowMode(2, -1, -1, false);
                    if (InterfaceManager.getWindowMode() != 2) {
                        addline("wm2 failed");
                        return;
                    }
                    addline("wm2 succeeded");
                    return;
                }
                if (command.equalsIgnoreCase("wm3")) {
                    InterfaceManager.changeWindowMode(3, 1024, 768, false);
                    if (InterfaceManager.getWindowMode() != 3) {
                        addline("wm3 failed");
                        return;
                    }
                    addline("wm3 succeeded");
                    return;
                }
                if (command.equalsIgnoreCase("tk0")) {
                    Static32.setToolkit(ToolkitType.JAVA, false);
                    if (ClientOptions.instance.toolkit.getValue() == ToolkitType.JAVA) {
                        addline("Entered tk0");
                        ClientOptions.instance.update(0, ClientOptions.instance.toolkitDefault);
                        ClientOptions.save();
                        Static503.sentPreferences = false;
                        return;
                    }
                    addline("Failed to enter tk0");
                    return;
                }
                if (command.equalsIgnoreCase("tk1")) {
                    Static32.setToolkit(ToolkitType.GL, false);
                    if (ClientOptions.instance.toolkit.getValue() == ToolkitType.GL) {
                        addline("Entered tk1");
                        ClientOptions.instance.update(1, ClientOptions.instance.toolkitDefault);
                        ClientOptions.save();
                        Static503.sentPreferences = false;
                        return;
                    }
                    addline("Failed to enter tk1");
                    return;
                }
                if (command.equalsIgnoreCase("tk2")) {
                    Static32.setToolkit(ToolkitType.SSE, false);
                    if (ClientOptions.instance.toolkit.getValue() == ToolkitType.SSE) {
                        addline("Entered tk2");
                        ClientOptions.instance.update(2, ClientOptions.instance.toolkitDefault);
                        ClientOptions.save();
                        Static503.sentPreferences = false;
                        return;
                    }
                    addline("Failed to enter tk2");
                    return;
                }
                if (command.equalsIgnoreCase("tk3")) {
                    Static32.setToolkit(ToolkitType.D3D, false);
                    if (ClientOptions.instance.toolkit.getValue() == ToolkitType.D3D) {
                        addline("Entered tk3");
                        ClientOptions.instance.update(3, ClientOptions.instance.toolkitDefault);
                        ClientOptions.save();
                        Static503.sentPreferences = false;
                        return;
                    }
                    addline("Failed to enter tk3");
                    return;
                }
                if (command.equalsIgnoreCase("tk5")) {
                    Static32.setToolkit(ToolkitType.GLX, false);
                    if (ClientOptions.instance.toolkit.getValue() == ToolkitType.GLX) {
                        addline("Entered tk5");
                        ClientOptions.instance.update(5, ClientOptions.instance.toolkitDefault);
                        ClientOptions.save();
                        Static503.sentPreferences = false;
                        return;
                    }
                    addline("Failed to enter tk5");
                    return;
                }
                if (command.startsWith("setba")) {
                    if (command.length() < 6) {
                        addline("Invalid buildarea value");
                        return;
                    }
                    @Pc(501) int buildArea = StringTools.parseDecimal(command.substring(6));
                    if (buildArea >= 0 && buildArea <= Static461.method6268(GameShell.maxmemory)) {
                        ClientOptions.instance.update(buildArea, ClientOptions.instance.buildArea);
                        ClientOptions.save();
                        Static503.sentPreferences = false;
                        addline("maxbuildarea=" + ClientOptions.instance.buildArea.getValue());
                        return;
                    }
                    addline("Invalid buildarea value");
                    return;
                }
                if (command.startsWith("rect_debug")) {
                    if (command.length() < 10) {
                        addline("Invalid rect_debug value");
                        return;
                    }
                    InterfaceManager.rectDebug = StringTools.parseDecimal(command.substring(10).trim());
                    addline("rect_debug=" + InterfaceManager.rectDebug);
                    return;
                }
                if (command.equalsIgnoreCase("qa_op_test")) {
                    InterfaceManager.testOpacity = true;
                    addline("qa_op_test=" + InterfaceManager.testOpacity);
                    return;
                }
                if (command.equalsIgnoreCase("clipcomponents")) {
                    InterfaceManager.clipComponents = !InterfaceManager.clipComponents;
                    addline("clipcomponents=" + InterfaceManager.clipComponents);
                    return;
                }
                if (command.startsWith("bloom")) {
                    @Pc(1264) boolean bloom = Toolkit.active.bloom();
                    if (Static249.setBloom(!bloom)) {
                        if (bloom) {
                            addline("Bloom disabled");
                            return;
                        }
                        addline("Bloom enabled");
                        return;
                    }
                    addline("Failed to enable bloom");
                    return;
                }
                if (command.equalsIgnoreCase("tween")) {
                    if (!Animator.forceTweening) {
                        Animator.forceTweening = true;
                        addline("Forced tweening ENABLED!");
                        return;
                    }
                    Animator.forceTweening = false;
                    addline("Forced tweening disabled.");
                    return;
                }
                if (command.equalsIgnoreCase("shiftclick")) {
                    if (MiniMenu.shiftClick) {
                        addline("Shift-click disabled.");
                        MiniMenu.shiftClick = false;
                        return;
                    }
                    addline("Shift-click ENABLED!");
                    MiniMenu.shiftClick = true;
                    return;
                }
                if (command.equalsIgnoreCase("getcgcoord")) {
                    addline("x:" + (PlayerEntity.self.x >> 9) + " z:" + (PlayerEntity.self.z >> 9));
                    return;
                }
                if (command.equalsIgnoreCase("getheight")) {
                    addline("Height: " + Static246.ground[PlayerEntity.self.level].getHeight(PlayerEntity.self.x >> 9, PlayerEntity.self.z >> 9));
                    return;
                }
                if (command.equalsIgnoreCase("resetminimap")) {
                    js5.SPRITES.discardPacked();
                    js5.SPRITES.discardUnpacked();
                    MSITypeList.instance.cacheReset();
                    MapElementTypeList.instance.cacheReset();
                    Minimap.reset();
                    addline("Minimap reset");
                    return;
                }
                if (command.startsWith("mc")) {
                    if (Toolkit.active.method7979()) {
                        @Pc(501) int local501 = Integer.parseInt(command.substring(3));
                        if (local501 < 1) {
                            local501 = 1;
                        } else if (local501 > 4) {
                            local501 = 4;
                        }
                        Static455.anInt6915 = local501;
                        MainLogicManager.mapBuild();
                        addline("Render cores now: " + Static455.anInt6915);
                        return;
                    }
                    addline("Current toolkit doesn't support multiple cores");
                    return;
                }
                if (command.startsWith("cachespace")) {
                    addline("I(s): " + Component.spriteCache.remaining() + "/" + Component.spriteCache.capacity());
                    addline("I(m): " + Component.modelCache.remaining() + "/" + Component.modelCache.capacity());
                    addline("O(s): " + ObjTypeList.instance.spriteCache.remaining() + "/" + ObjTypeList.instance.spriteCache.capacity());
                    return;
                }
                if (command.equals("renderprofile") || command.equals("rp")) {
                    Static354.showProfiling = !Static354.showProfiling;
                    Toolkit.active.method8018(Static354.showProfiling);
                    Static126.method2228();
                    addline("showprofiling=" + Static354.showProfiling);
                    return;
                }
                if (command.startsWith("performancetest")) {
                    @Pc(501) int toolkit = -1;
                    @Pc(725) int iterations = 1000;

                    if (command.length() > "performancetest".length()) {
                        @Pc(1621) String[] args = StringTools.split(command, ' ');

                        try {
                            if (args.length > 1) {
                                iterations = Integer.parseInt(args[1]);
                            }
                        } catch (@Pc(1634) Throwable ignored) {
                            /* empty */
                        }

                        try {
                            if (args.length > 2) {
                                toolkit = Integer.parseInt(args[2]);
                            }
                        } catch (@Pc(1645) Throwable ignored) {
                            /* empty */
                        }
                    }

                    if (toolkit != -1) {
                        addline("Performance: " + Static363.profileToolkit(iterations, toolkit));
                        return;
                    }

                    addline("Java toolkit: " + Static363.profileToolkit(iterations, ToolkitType.JAVA));
                    addline("SSE toolkit:  " + Static363.profileToolkit(iterations, ToolkitType.SSE));
                    addline("D3D toolkit:  " + Static363.profileToolkit(iterations, ToolkitType.D3D));
                    addline("GL toolkit:   " + Static363.profileToolkit(iterations, ToolkitType.GL));
                    addline("GLX toolkit:  " + Static363.profileToolkit(iterations, ToolkitType.GLX));
                    return;
                }

                if (command.equals("nonpcs")) {
                    Static353.noNpcs = !Static353.noNpcs;
                    addline("nonpcs=" + Static353.noNpcs);
                    return;
                }

                if (command.equals("autoworld")) {
                    WorldList.selectAutoWorld();
                    addline("auto world selected");
                    return;
                }


                if (command.startsWith("switchworld")) {
                    @Pc(501) int id = Integer.parseInt(command.substring("switchworld ".length()));
                    client.connectTo(id, WorldList.list(id).address);
                    addline("switched");
                    return;
                }

                if (command.equals("getworld")) {
                    addline("w: " + ConnectionInfo.login.world);
                    return;
                }

                if (command.startsWith("pc")) {
                    @Pc(1833) ServerConnection connection = ConnectionManager.active();
                    @Pc(1839) ClientMessage message = ClientMessage.create(ClientProt.MESSAGE_PRIVATE, connection.isaac);
                    message.bitPacket.p1(0);
                    @Pc(521) int pos = message.bitPacket.pos;
                    @Pc(582) int local582 = command.indexOf(" ", 4);
                    message.bitPacket.pjstr(command.substring(3, local582));
                    WordPack.encode(message.bitPacket, command.substring(local582));
                    message.bitPacket.psize1(message.bitPacket.pos - pos);
                    connection.send(message);
                    return;
                }

                if (command.equals("savevarcs")) {
                    Static266.saveVarcs();
                    addline("perm varcs saved");
                    return;
                }

                if (command.equals("scramblevarcs")) {
                    for (@Pc(501) int i = 0; i < Static511.varcs.length; i++) {
                        if (Static118.permVarcs[i]) {
                            Static511.varcs[i] = (int) (Math.random() * 99999.0D);
                            if (Math.random() > 0.5D) {
                                Static511.varcs[i] *= -1;
                            }
                        }
                    }

                    Static266.saveVarcs();
                    addline("perm varcs scrambled");
                    return;
                }

                if (command.equals("showcolmap")) {
                    Minimap.drawCollisionMap = true;
                    Minimap.reset();
                    addline("colmap is shown");
                    return;
                }

                if (command.equals("hidecolmap")) {
                    Minimap.drawCollisionMap = false;
                    Minimap.reset();
                    addline("colmap is hidden");
                    return;
                }

                if (command.equals("resetcache")) {
                    client.cacheReset();
                    addline("Caches reset");
                    return;
                }

                if (command.equals("profilecpu")) {
                    addline(Static65.profileCpu() + "ms");
                    return;
                }

                if (command.startsWith("getclientvarpbit")) {
                    @Pc(501) int id = Integer.parseInt(command.substring("getclientvarpbit ".length()));
                    addline("varpbit=" + TimedVarDomain.instance.getVarBitValue(id));
                    return;
                }

                if (command.startsWith("getclientvarp")) {
                    @Pc(501) int id = Integer.parseInt(command.substring("getclientvarp ".length()));
                    addline("varp=" + TimedVarDomain.instance.getVarValueInt(id));
                    return;
                }

                if (command.startsWith("directlogin")) {
                    @Pc(2083) String[] local2083 = StringTools.split(command.substring("directlogin ".length()), ' ');
                    if (local2083.length >= 2) {
                        @Pc(725) int local725 = local2083.length > 2 ? Integer.parseInt(local2083[2]) : 0;
                        LoginManager.requestLoginWithUsername(local725, local2083[1], local2083[0]);
                        return;
                    }
                }

                if (command.startsWith("snlogin ")) {
                    @Pc(2083) String[] local2083 = StringTools.split(command.substring("snlogin ".length()), ' ');
                    @Pc(725) int local725 = Integer.parseInt(local2083[0]);
                    @Pc(521) int local521 = local2083.length == 2 ? Integer.parseInt(local2083[1]) : 0;
                    LoginManager.requestLoginFromSocialNetwork(local725, local521);
                    return;
                }

                if (command.startsWith("csprofileclear")) {
                    ScriptRunner.profileClear();
                    return;
                }

                if (command.startsWith("csprofileoutputc")) {
                    ScriptRunner.profileOutput();
                    return;
                }

                if (command.startsWith("csprofileoutputt")) {
                    ScriptRunner.profileOutput();
                    return;
                }

                if (command.startsWith("texsize")) {
                    @Pc(501) int size = Integer.parseInt(command.substring("texsize ".length()));
                    Toolkit.active.setTextureSize(size);
                    return;
                }

                if (command.equals("soundstreamcount")) {
                    addline("Active streams: " + SoundManager.activeStreams.count());
                    return;
                }

                if (command.equals("autosetup")) {
                    Static519.autosetup();
                    addline("Complete. Toolkit now: " + ClientOptions.instance.toolkit.getValue());
                    return;
                }

                if (command.equals("errormessage")) {
                    addline(client.aClient1.getErrorTrace());
                    return;
                }

                if (command.equals("heapdump")) {
                    if (SignLink.osNameLower.startsWith("win")) {
                        Static21.dumpHeap(new File("C:\\Temp\\heap.dump"));
                    } else {
                        Static21.dumpHeap(new File("/tmp/heap.dump"));
                    }
                    addline("Done");
                    return;
                }

                if (command.equals("os")) {
                    addline("Name: " + SignLink.osNameLower);
                    addline("Arch: " + SignLink.osArchRaw);
                    addline("Ver: " + SignLink.osVersionRaw);
                    return;
                }

                if (command.startsWith("w2debug")) {
                    @Pc(501) int mode = Integer.parseInt(command.substring(8, 9));
                    Static699.w2Debug = mode;
                    MainLogicManager.mapBuild();
                    addline("Toggled!");
                    return;
                }

                if (command.startsWith("ortho ")) {
                    @Pc(501) int space = command.indexOf(' ');
                    if (space < 0) {
                        addline("Syntax: ortho <n>");
                        return;
                    }

                    @Pc(725) int mode = StringTools.parseDecimal(command.substring(space + 1));
                    ClientOptions.instance.update(mode, ClientOptions.instance.orthographic);
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    OrthoMode.enter();

                    if (mode != ClientOptions.instance.orthographic.getValue()) {
                        addline("Failed to change ortho mode");
                        return;
                    }

                    addline("Successfully changed ortho mode");
                    return;
                }

                if (command.startsWith("orthozoom ")) {
                    if (ClientOptions.instance.orthographic.getValue() == 0) {
                        addline("enable ortho mode first (use 'ortho <n>')");
                        return;
                    }

                    @Pc(501) int local501 = StringTools.parseDecimal(command.substring(command.indexOf(' ') + 1));
                    OrthoMode.zoom = local501;
                    addline("orthozoom=" + OrthoMode.zoom);
                    return;
                }

                if (command.startsWith("orthotilesize ")) {
                    @Pc(501) int size = StringTools.parseDecimal(command.substring(command.indexOf(' ') + 1));
                    OrthoMode.tileSizeY = size;
                    OrthoMode.tileSizeX = size;
                    addline("ortho tile size=" + size);
                    OrthoMode.enter();
                    return;
                }

                if (command.equals("orthocamlock")) {
                    Static129.orthoCameraLock = !Static129.orthoCameraLock;
                    addline("ortho camera lock is " + (Static129.orthoCameraLock ? "on" : "off"));
                    return;
                }

                if (command.startsWith("skydetail ")) {
                    @Pc(501) int value = StringTools.parseDecimal(command.substring(command.indexOf(' ') + 1));
                    ClientOptions.instance.update(value, ClientOptions.instance.skydetail);
                    addline("skydetail is " + (ClientOptions.instance.skydetail.getValue() == 0 ? "low" : "high"));
                    return;
                }

                if (command.startsWith("setoutput ")) {
                    @Pc(2592) File file = new File(command.substring(10));

                    if (file.exists()) {
                        file = new File(command.substring(10) + "." + SystemTimer.safetime() + ".log");

                        if (file.exists()) {
                            addline("file already exists!");
                            return;
                        }
                    }

                    if (output != null) {
                        output.close();
                        output = null;
                    }

                    try {
                        output = new FileOutputStream(file);
                        return;
                    } catch (@Pc(2641) FileNotFoundException ignored) {
                        addline("Could not create " + file.getName());
                        return;
                    } catch (@Pc(2655) SecurityException ignored) {
                        addline("Cannot write to " + file.getName());
                        return;
                    }
                }

                if (command.equals("closeoutput")) {
                    if (output != null) {
                        output.close();
                    }
                    output = null;
                    return;
                }

                if (command.startsWith("runscript ")) {
                    @Pc(2592) File local2592 = new File(command.substring(10));
                    if (!local2592.exists()) {
                        addline("No such file");
                        return;
                    }
                    @Pc(2712) byte[] local2712 = Files.read(local2592);
                    if (local2712 == null) {
                        addline("Failed to read file");
                        return;
                    }
                    @Pc(1621) String[] local1621 = StringTools.split(StringTools.replace(Cp1252.decode(local2712), "", '\r'), '\n');
                    Static363.method6234(local1621);
                }

                if (command.startsWith("zoom ")) {
                    @Pc(2748) short zoom = (short) StringTools.parseDecimal(command.substring("zoom ".length()));
                    if (zoom > 0) {
                        Camera.zoom = zoom;
                    }
                    return;
                }

                if (command.startsWith("cs2debug")) {
                    if (command.length() > "cs2debug ".length() && command.charAt("cs2debug".length()) == ' ') {
                        ScriptRunner.debugName = command.substring(9);
                        ScriptRunner.debug = true;
                        addline("cs2debug: (" + ScriptRunner.debugName + ")");
                        return;
                    }
                    ScriptRunner.debugName = null;
                    ScriptRunner.debug = !ScriptRunner.debug;
                    addline("cs2debug:" + ScriptRunner.debug);
                    return;
                }

                if (MainLogicManager.step == MainLogicStep.STEP_GAME_SCREEN) {
                    @Pc(2836) ClientMessage local2836 = ClientMessage.create(ClientProt.CLIENT_CHEAT, ServerConnection.GAME.isaac);
                    local2836.bitPacket.p1(command.length() + 3);
                    local2836.bitPacket.p1(automatic ? 1 : 0);
                    local2836.bitPacket.p1(retainText ? 1 : 0);
                    local2836.bitPacket.pjstr(command);
                    ServerConnection.GAME.send(local2836);
                }

                if (command.startsWith("fps ") && Client.modeWhere != ModeWhere.LIVE) {
                    GameShell.setspeed(StringTools.parseDecimal(command.substring(4)));
                    return;
                }
            } catch (@Pc(2894) Exception ignored) {
                addline(LocalisedText.DEBUG_CONSOLE_ERROR.localise(Client.language));
                return;
            }
        }

        if (MainLogicManager.step != MainLogicStep.STEP_GAME_SCREEN) {
            addline(LocalisedText.DEBUG_CONSOLE_UNKNOWNCOMMAND.localise(Client.language) + command);
        }
    }

    @OriginalMember(owner = "client!eaa", name = "a", descriptor = "(B)V")
    public static void close() {
        open = false;
        InterfaceManager.redrawAll();
    }

    @OriginalMember(owner = "client!oga", name = "a", descriptor = "(I)V")
    public static void open() {
        if (!MainLogicManager.isNotLoading()) {
            return;
        }

        if (lines == null) {
            reset();
        }

        anInt8472 = 0;
        open = true;
    }

    @OriginalMember(owner = "client!iia", name = "a", descriptor = "(ZI)V")
    public static void method3920(@OriginalArg(0) boolean arg0) {
        if (currententry.length() == 0) {
            return;
        }
        addline("--> " + currententry);
        executeComand(false, arg0, currententry);
        Static625.anInt9472 = 0;
        if (!arg0) {
            currententryLength = 0;
            currententry = "";
        }
    }

    @OriginalMember(owner = "client!fb", name = "a", descriptor = "(JI)Ljava/lang/String;")
    public static String method8846(@OriginalArg(0) long arg0) {
        GMT_CALENDAR.setTime(new Date(arg0));
        @Pc(19) int local19 = GMT_CALENDAR.get(7);
        @Pc(23) int local23 = GMT_CALENDAR.get(5);
        @Pc(27) int local27 = GMT_CALENDAR.get(2);
        @Pc(31) int local31 = GMT_CALENDAR.get(1);
        @Pc(35) int local35 = GMT_CALENDAR.get(11);
        @Pc(39) int local39 = GMT_CALENDAR.get(12);
        @Pc(43) int local43 = GMT_CALENDAR.get(13);
        return Static146.aStringArray8[local19 - 1] + ", " + local23 / 10 + local23 % 10 + "-" + Static361.aStringArray29[local27] + "-" + local31 + " " + local35 / 10 + local35 % 10 + ":" + local39 / 10 + local39 % 10 + ":" + local43 / 10 + local43 % 10 + " GMT";
    }
}

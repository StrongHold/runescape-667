import com.jagex.Client;
import com.jagex.IndexedImage;
import com.jagex.game.runetek6.config.quickchatcattype.QuickChatCatTypeList;
import rs2.client.loading.library.LibraryManager;
import com.jagex.core.compress.GzipDecompressor;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TickScheduler;
import com.jagex.game.Animator;
import com.jagex.graphics.Fonts;
import rs2.client.loading.screen.LoadingScreen;
import com.jagex.game.PlayerModel;
import com.jagex.game.SimpleFillerDecoder;
import com.jagex.game.collision.CollisionMap;
import com.jagex.game.compression.huffman.Huffman;
import com.jagex.game.compression.huffman.WordPack;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.game.runetek6.config.cursortype.CursorTypeList;
import com.jagex.game.runetek6.config.defaults.AudioDefaults;
import com.jagex.game.runetek6.config.defaults.DefaultsGroup;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.game.runetek6.config.defaults.MapDefaults;
import com.jagex.game.runetek6.config.defaults.WearposDefaults;
import com.jagex.game.runetek6.config.enumtype.EnumTypeList;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayTypeList;
import com.jagex.game.runetek6.config.fonttype.FontTypeList;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkTypeList;
import com.jagex.game.runetek6.config.idktype.IDKTypeList;
import com.jagex.game.runetek6.config.invtype.InvTypeList;
import com.jagex.game.runetek6.config.lighttype.LightTypeList;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.meltype.MapElementTypeList;
import com.jagex.game.runetek6.config.msitype.MSITypeList;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.paramtype.ParamTypeList;
import com.jagex.game.runetek6.config.questtype.QuestTypeList;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereTypeList;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxTypeList;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.game.runetek6.config.structtype.StructTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.game.runetek6.config.vartype.VarcTypeList;
import com.jagex.game.runetek6.config.vartype.VarcstrTypeList;
import com.jagex.game.runetek6.config.vartype.bit.VarBitTypeListClient;
import com.jagex.game.runetek6.config.vartype.clan.VarClanSettingTypeList;
import com.jagex.game.runetek6.config.vartype.clan.VarClanTypeList;
import com.jagex.game.runetek6.config.vartype.player.VarPlayerTypeListClient;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5MasterIndex;
import com.jagex.js5.js5;
import jagex3.jagmisc.jagmisc;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.loading.LoadState;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.PixelGrabber;

public final class Loading {

    @OriginalMember(owner = "client!sn", name = "g", descriptor = "Lclient!dla;")
    public static final ReferenceCache spriteCache = new ReferenceCache(128, 4);

    @OriginalMember(owner = "client!av", name = "j", descriptor = "[B")
    public static final byte[] COMPRESSED_SPRITE = {
        31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -5, 127, -29, -1, 109, 6, 103, 6, 14,
        54, 54, 118, 54, 86, 14, 118, 118, 118, 78, 78, 14, 46, 30, 17, 94, 30,
        110, 110, 30, 73, 33, 97, 126, 17, 89, 41, 121, 57, 89, 41, 25, 25, 5,
        21, 61, 117, 5, 37, 29, 101, 25, 25, 13, 115, 77, 29, 3, 67, 19, 19,
        19, 121, 117, 75, 91, 11, 35, 27, 61, 99, 19, 35, -112, 33, -116, -100,
        -100, -100, 60, -36, 60, 18, -68, -68, 18, 70, -118, 50, -118, 70, 36,
        -125, -1, 7, 24, 68, 56, 24, 24, 25, 24, 89, 24, -107, 24, -104, 4, 25,
        -103, 5, 25, 89, -108, 24, -2, 31, 97, -112, 103, 96, 96, 100, 101, 4,
        3, 6, 40, 96, 100, 98, 102, 97, 101, 99, -25, -32, -28, -30, 6, 42,
        -40, 42, -64, -64, -60, -56, -52, -52, -60, -62, -52, -54, -54, -62, 2,
        -108, -83, 5, -54, 51, -80, 8, -78, 10, 41, 26, 58, -78, 9, 7, 38, -78,
        43, 21, -118, 24, 53, 78, 92, -56, -95, -20, -76, -15, -96, 104, -48,
        -59, 15, 42, -58, 73, 69, 77, -100, 92, 98, -30, 18, -110, 82, -86,
        106, -22, 26, -102, 90, 38, -90, 102, -26, 22, -106, 86, -50, 46, -82,
        110, -18, 30, -98, 94, -63, 33, -95, 97, -31, 17, -111, 81, -55, 41,
        -87, 105, -23, 25, -103, 89, -59, 37, -91, 101, -27, 21, -107, 85, -51,
        45, -83, 109, -19, 29, -99, 93, -109, 38, 79, -103, 58, 109, -6, -116,
        -103, -77, 22, 45, 94, -78, 116, -39, -14, 21, 43, 87, 109, -38, -68,
        101, -21, -74, -19, 59, 118, -18, 58, 116, -8, -56, -47, 99, -57, 79,
        -100, 60, 117, -23, -14, -107, -85, -41, -82, -33, -72, 121, -21, -31,
        -93, -57, 79, -98, 62, 123, -2, -30, -27, -85, -113, -97, 62, 127, -7,
        -6, -19, -5, -113, -97, -65, 64, -2, 98, 100, 96, 102, -124, 1, -84,
        -2, 18, 4, -6, -117, -119, -123, -123, -103, -123, 29, -28, 47, 70,
        -90, 114, -112, 2, 65, 22, 86, 69, 67, 54, 33, -57, 64, -10, -60, 66,
        97, 37, -93, 70, 14, 17, -89, -119, 11, 55, 30, -28, 84, 54, 14, -6,
        32, -102, 84, 116, -111, 75, 76, -59, -28, -95, -22, 71, -112, -41,
        -64, 62, 35, -50, 99, 77, 100, -7, 12, -18, 49, -124, -65, 110, 49,
        -16, -79, 48, 2, -29, 15, -24, 78, 6, 6, 123, -122, -97, -1, -70, 52,
        22, -16, -49, 127, -79, -88, -21, -1, 77, 0, 38, 27, -28, 10, 110, 2,
        0, 0
    };

    @OriginalMember(owner = "client!ok", name = "g", descriptor = "[I")
    public static final int[] JS5_PERCENTAGES = {
        4,
        4,
        1,
        2,
        6,
        4,
        2,
        44,
        2,
        2,
        2,
        2,
        2,
        1,
        2,
        2,
        2,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        0,
        0,
        0,
        0,
        1,
    };

    @OriginalMember(owner = "client!ke", name = "l", descriptor = "I")
    public static int spritesCrc;

    @OriginalMember(owner = "client!om", name = "b", descriptor = "I")
    public static int screensCrc;

    @OriginalMember(owner = "client!jha", name = "n", descriptor = "Z")
    public static boolean loadingSpritesRaw;

    @OriginalMember(owner = "client!ad", name = "e", descriptor = "J")
    public static long lastMemoryCheck = 0L;

    @OriginalMember(owner = "client!rea", name = "f", descriptor = "J")
    public static long lastGc = 0L;

    @OriginalMember(owner = "client!hea", name = "r", descriptor = "[Lclient!uha;")
    public static LoadingScreen[] screens;

    @OriginalMember(owner = "client!ov", name = "a", descriptor = "Lclient!gca;")
    public static LoadState state;

    @OriginalMember(owner = "client!vd", name = "m", descriptor = "I")
    public static int js5IndexStartPercentage = -1;

    @OriginalMember(owner = "client!od", name = "h", descriptor = "Lclient!uaa;")
    public static LoadingScreenRenderer renderer;

    @OriginalMember(owner = "client!hj", name = "b", descriptor = "Ljava/lang/Thread;")
    public static Thread rendererThread;

    @OriginalMember(owner = "client!cf", name = "c", descriptor = "J")
    public static long lastRendererUpdate;

    @OriginalMember(owner = "client!un", name = "K", descriptor = "[Lclient!gca;")
    public static LoadState[] states;

    @OriginalMember(owner = "client!se", name = "q", descriptor = "Ljava/lang/String;")
    public static String text;

    @OriginalMember(owner = "client!lr", name = "f", descriptor = "I")
    public static int percentage;

    @OriginalMember(owner = "client!gla", name = "I", descriptor = "I")
    public static int screen = -1;

    @OriginalMember(owner = "client!fs", name = "a", descriptor = "(I)I")
    public static int getSpritesCrc() {
        return spritesCrc;
    }

    @OriginalMember(owner = "client!ega", name = "c", descriptor = "(I)I")
    public static int getScreensCrc() {
        return screensCrc;
    }

    @OriginalMember(owner = "client!af", name = "a", descriptor = "(B)V")
    public static void cacheReset() {
        spriteCache.reset();
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "(BILclient!sb;)Lclient!st;")
    public static Sprite sprite(@OriginalArg(1) int id, @OriginalArg(2) js5 loadingSprites) {
        @Pc(16) Sprite sprite = (Sprite) spriteCache.get(id);
        if (sprite == null) {
            if (loadingSpritesRaw) {
                sprite = Toolkit.active.createSprite(IndexedImage.loadFirst(loadingSprites, id), true);
            } else {
                sprite = decodeSprite(loadingSprites.getfile(id));
            }
            spriteCache.put(sprite, id);
        }
        return sprite;
    }

    @OriginalMember(owner = "client!fda", name = "a", descriptor = "(I[B)Lclient!st;")
    public static Sprite decodeSprite(@OriginalArg(1) byte[] data) {
        if (data == null) {
            throw new RuntimeException("");
        }

        while (true) {
            try {
                @Pc(22) Image image = java.awt.Toolkit.getDefaultToolkit().createImage(data);
                @Pc(27) MediaTracker tracker = new MediaTracker(client.aClient1);
                tracker.addImage(image, 0);
                tracker.waitForAll();
                @Pc(37) int width = image.getWidth(client.aClient1);
                @Pc(41) int height = image.getHeight(client.aClient1);
                if (!tracker.isErrorAny() && width >= 0 && height >= 0) {
                    @Pc(66) int[] pixels = new int[height * width];
                    @Pc(78) PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
                    grabber.grabPixels();
                    return Toolkit.active.createSprite(width, width, height, pixels);
                }
                throw new RuntimeException("");
            } catch (@Pc(91) InterruptedException ignored) {
                /* empty */
            }
        }
    }

    @OriginalMember(owner = "client!sk", name = "b", descriptor = "(I)Z")
    public static boolean canDecodeSprites() {
        try {
            @Pc(7) GzipDecompressor gzipDecompressor = new GzipDecompressor();
            @Pc(12) byte[] data = gzipDecompressor.decompress(COMPRESSED_SPRITE);
            decodeSprite(data);
            return true;
        } catch (@Pc(28) Exception ignored) {
            return false;
        }
    }

    @OriginalMember(owner = "client!qi", name = "a", descriptor = "(I)I")
    public static int essentials() {
        if (ClientOptions.instance.safeMode.getValue() == 0) {
            for (@Pc(12) int i = 0; i < Static671.anInt10026; i++) {
                if (Static194.AN_KEYBOARD_EVENT_ARRAY_1[i].getKeyChar() == 's' || Static194.AN_KEYBOARD_EVENT_ARRAY_1[i].getKeyChar() == 'S') {
                    ClientOptions.instance.update(1, ClientOptions.instance.safeMode);
                    Static416.aBoolean472 = true;
                    break;
                }
            }
        }

        if (LoadState.WAIT_FOR_MEMORY == state) {
            @Pc(65) Runtime runtime = Runtime.getRuntime();
            @Pc(74) int usedMemory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
            @Pc(77) long now = SystemTimer.safetime();
            if (lastMemoryCheck == 0L) {
                lastMemoryCheck = now;
            }

            if (usedMemory > 16384 && now - lastMemoryCheck < 5000L) {
                if (now - lastGc > 1000L) {
                    System.gc();
                    lastGc = now;
                }

                return 0;
            }
        }

        if (LoadState.INIT_LOADING_SCREEN_ARCHIVES == state) {
            if (Static228.js5MasterIndex == null) {
                Static228.js5MasterIndex = new Js5MasterIndex(Client.js5WorkerThread, Static66.aCachedResourceWorker_1, Static442.JS5_RSA_EXPONENT, Static670.JS5_RSA_MODULUS);
            }

            if (!Static228.js5MasterIndex.method5800()) {
                return 0;
            }

            Static595.setToolkit(null, true, 0);
            loadingSpritesRaw = !canDecodeSprites();
            js5.LOADING_SPRITES = client.createJs5(false, loadingSpritesRaw ? Js5Archive.LOADING_SPRITES_RAW : Js5Archive.LOADING_SPRITES, 1);
            js5.LOADING_SCREENS = client.createJs5(false, Js5Archive.LOADING_SCREENS, 1);
            js5.FONTMETRICS = client.createJs5(false, Js5Archive.FONTMETRICS, 1);
        }

        if (LoadState.FETCH_LOADING_SCREEN_RESOURCES == state) {
            @Pc(184) boolean complete = js5.LOADING_SCREENS.isComplete();

            @Pc(74) int totalPercentage = Client.js5ResourceProviders[33].indexPercentage();
            totalPercentage += Client.js5ResourceProviders[loadingSpritesRaw ? Js5Archive.LOADING_SPRITES_RAW : Js5Archive.LOADING_SPRITES].indexPercentage();
            totalPercentage += Client.js5ResourceProviders[Js5Archive.FONTMETRICS].indexPercentage();
            totalPercentage += complete ? 100 : js5.LOADING_SCREENS.completePercentage();
            if (totalPercentage != 400) {
                return totalPercentage / 4;
            }

            spritesCrc = js5.LOADING_SPRITES.indexCrc();
            screensCrc = js5.LOADING_SCREENS.indexCrc();
            Fonts.load(js5.LOADING_SPRITES);

            @Pc(250) int loadingSequence = ClientOptions.instance.loadingSequence.getValue();
            LoadingScreenManager.instance = new LoadingScreenManager(Client.modeGame, Client.language, js5.LOADING_SCREENS);
            @Pc(262) int[] sequence = LoadingScreenManager.instance.getSequence(loadingSequence);
            if (sequence.length == 0) {
                sequence = LoadingScreenManager.instance.getSequence(0);
            }

            @Pc(276) LoadingScreenOpFactory opFactory = new LoadingScreenOpFactory(js5.LOADING_SPRITES, js5.FONTMETRICS);
            if (sequence.length > 0) {
                screens = new LoadingScreen[sequence.length];

                for (@Pc(285) int i = 0; i < screens.length; i++) {
                    screens[i] = new ProceduralLoadingScreen(LoadingScreenManager.instance.get(sequence[i]), opFactory);
                }
            }
        }

        if (LoadState.INIT_FONT_INFO == state) {
            FontTypeList.init(js5.FONTMETRICS, js5.LOADING_SPRITES, Fonts.groups());
        }

        if (LoadState.LOAD_FONTS == state) {
            @Pc(12) int ready = FontTypeList.readyCount();
            @Pc(74) int total = FontTypeList.totalCount();

            if (total > ready) {
                return (ready * 100) / total;
            }
        }

        if (LoadState.FETCH_FONTS == state) {
            if (screens != null && screens.length > 0) {
                if (screens[0].percentage() < 100) {
                    return 0;
                }
                if (screens.length > 1 && LoadingScreenManager.instance.hasDefault() && screens[1].percentage() < 100) {
                    return 0;
                }
            }

            FontTypeList.method7549(Toolkit.active);
            Fonts.init(Toolkit.active);
            MainLogicManager.setStep(MainLogicStep.STEP_LOADING_1);
        }

        if (LoadState.CREATE_COLLISION_MAPS == state) {
            for (@Pc(12) int level = 0; level < 4; level++) {
                Client.collisionMaps[level] = CollisionMap.create(Static720.mapWidth, Static501.mapLength);
            }
        }

        if (state == LoadState.OPEN_JS5_ARCHIVES) {
            js5.SPRITES = client.createJs5(false, Js5Archive.SPRITES, 1);
            js5.ANIMS = client.createJs5(false, Js5Archive.ANIMS, 1);
            js5.BASES = client.createJs5(false, Js5Archive.BASES, 1);
            js5.CONFIG = client.createJs5(false, Js5Archive.CONFIG, 1);
            js5.INTERFACES = client.createJs5(false, Js5Archive.INTERFACES, 1);
            js5.SYNTH_SOUNDS = client.createJs5(false, Js5Archive.SYNTH_SOUNDS, 1);
            js5.MAPS = client.createJs5(true, Js5Archive.MAPS, 1);
            js5.MIDI_SONGS = client.createJs5(true, Js5Archive.MIDI_SONGS, 1);
            js5.MODELS = client.createJs5(false, Js5Archive.MODELS, 1);
            js5.TEXTURES = client.createJs5(false, Js5Archive.TEXTURES, 1);
            js5.BINARY = client.createJs5(false, Js5Archive.BINARY, 1);
            js5.MIDI_JINGLES = client.createJs5(false, Js5Archive.MIDI_JINGLES, 1);
            js5.CLIENTSCRIPTS = client.createJs5(false, Js5Archive.CLIENTSCRIPTS, 1);
            js5.VORBIS = client.createJs5(false, Js5Archive.VORBIS, 1);
            js5.js5_15 = client.createJs5(false, 15, 1);
            js5.CONFIG_LOC = client.createJs5(false, Js5Archive.CONFIG_LOC, 1);
            js5.CONFIG_ENUM = client.createJs5(false, Js5Archive.CONFIG_ENUM, 1);
            js5.CONFIG_NPC = client.createJs5(false, Js5Archive.CONFIG_NPC, 1);
            js5.CONFIG_OBJ = client.createJs5(false, Js5Archive.CONFIG_OBJ, 1);
            js5.CONFIG_SEQ = client.createJs5(false, Js5Archive.CONFIG_SEQ, 1);
            js5.CONFIG_SPOT = client.createJs5(false, Js5Archive.CONFIG_SPOT, 1);
            js5.CONFIG_STRUCT = client.createJs5(false, Js5Archive.CONFIG_STRUCT, 1);
            js5.WORLDMAPDATA = client.createJs5(true, Js5Archive.WORLDMAPDATA, 1);
            js5.QUICKCHAT = client.createJs5(false, Js5Archive.QUICKCHAT, 1);
            js5.QUICKCHAT_GLOBAL = client.createJs5(false, Js5Archive.QUICKCHAT_GLOBAL, 1);
            js5.MATERIALS = client.createJs5(true, Js5Archive.MATERIALS, 1);
            js5.CONFIG_PARTICLE = client.createJs5(false, Js5Archive.CONFIG_PARTICLE, 1);
            js5.DEFAULTS = client.createJs5(true, Js5Archive.DEFAULTS, 1);
            js5.CONFIG_BILLBOARD = client.createJs5(false, Js5Archive.CONFIG_BILLBOARD, 1);
            js5.CUTSCENES = client.createJs5(true, Js5Archive.CUTSCENES, 1);
            js5.DLLS = client.createJs5(true, Js5Archive.DLLS, 1);
            js5.SHADERS = client.createJs5(true, Js5Archive.SHADERS, 1);
            js5.VIDEOS = client.createJs5(true, 36, 2);
        }

        if (state == LoadState.GET_JS5_INDEXES) {
            @Pc(12) int percentage = 0;
            for (@Pc(74) int i = 0; i < Js5Archive.ID_LIMIT; i++) {
                if (Client.js5ResourceProviders[i] != null) {
                    percentage += (Client.js5ResourceProviders[i].indexPercentage() * JS5_PERCENTAGES[i]) / 100;
                }
            }

            if (percentage != 100) {
                if (js5IndexStartPercentage < 0) {
                    js5IndexStartPercentage = percentage;
                }
                return (percentage - js5IndexStartPercentage) * 100 / (100 - js5IndexStartPercentage);
            }

            Sprites.getJs5Indexes(js5.SPRITES);
            FontTypeList.init(js5.FONTMETRICS, js5.SPRITES, Fonts.groups());
        }

        if (state == LoadState.PLAY_THEME_MUSIC) {
            @Pc(746) byte[] data = js5.DEFAULTS.getfile(DefaultsGroup.AUDIO);
            if (data == null) {
                return 0;
            }
            AudioDefaults.decode(data);

            Static674.method8806();
            MainLogicManager.setStep(MainLogicStep.STEP_LOADING_2);
        }

        if (LoadState.SETUP_LIB_PATH == state) {
            LibraryManager.init(js5.DLLS, GameShell.signLink);
        }

        if (LoadState.DOWNLOAD_STUFF == state) {
            @Pc(12) int percentage = LoadingManager.changeMainState();
            if (percentage < 100) {
                return percentage;
            }

            MapDefaults.decode(js5.DEFAULTS.getfile(DefaultsGroup.MAP));
            GraphicsDefaults.instance = new GraphicsDefaults(js5.DEFAULTS);
            PlayerModel.recol_s = GraphicsDefaults.instance.recol_s;
            PlayerModel.recol_d = GraphicsDefaults.instance.recol_d;
            WearposDefaults.instance = new WearposDefaults(js5.DEFAULTS);
        }

        if (LoadState.SETUP_CONFIG_DECODERS == state) {
            if (GraphicsDefaults.instance.profilingModel != -1 && !js5.MODELS.requestdownload(0, GraphicsDefaults.instance.profilingModel)) {
                return 99;
            }

            Js5TextureSource.instance = new Js5TextureSource(js5.MATERIALS, js5.TEXTURES, js5.SPRITES);
            ParamTypeList.instance = new ParamTypeList(Client.modeGame, Client.language, js5.CONFIG);
            BASTypeList.instance = new BASTypeList(Client.modeGame, Client.language, js5.CONFIG, WearposDefaults.instance);
            CursorTypeList.instance = new CursorTypeList(Client.modeGame, Client.language, js5.CONFIG, js5.SPRITES);
            EnumTypeList.instance = new EnumTypeList(Client.modeGame, Client.language, js5.CONFIG_ENUM);
            FloorOverlayTypeList.instance = new FloorOverlayTypeList(Client.modeGame, Client.language, js5.CONFIG);
            FloorUnderlayTypeList.instance = new FloorUnderlayTypeList(Client.modeGame, Client.language, js5.CONFIG);
            HitmarkTypeList.instance = new HitmarkTypeList(Client.modeGame, Client.language, js5.CONFIG, js5.SPRITES);
            IDKTypeList.instance = new IDKTypeList(Client.modeGame, Client.language, js5.CONFIG, js5.MODELS);
            InvTypeList.instance = new InvTypeList(Client.modeGame, Client.language, js5.CONFIG);
            LightTypeList.instance = new LightTypeList(Client.modeGame, Client.language, js5.CONFIG);
            LocTypeList.instance = new LocTypeList(Client.modeGame, Client.language, true, js5.CONFIG_LOC, js5.MODELS);
            MapElementTypeList.instance = new MapElementTypeList(Client.modeGame, Client.language, js5.CONFIG, js5.SPRITES);
            MSITypeList.instance = new MSITypeList(Client.modeGame, Client.language, js5.CONFIG, js5.SPRITES);
            NPCTypeList.instance = new NPCTypeList(Client.modeGame, Client.language, true, js5.CONFIG_NPC, js5.MODELS);
            ObjTypeList.instance = new ObjTypeList(Client.modeGame, Client.language, true, ParamTypeList.instance, js5.CONFIG_OBJ, js5.MODELS);
            QuestTypeList.instance = new QuestTypeList(Client.modeGame, Client.language, js5.CONFIG);
            SeqTypeList.instance = new SeqTypeList(Client.modeGame, Client.language, js5.CONFIG_SEQ, js5.ANIMS, js5.BASES);
            SkyBoxTypeList.instance = new SkyBoxTypeList(Client.modeGame, Client.language, js5.CONFIG);
            SkyBoxSphereTypeList.instance = new SkyBoxSphereTypeList(Client.modeGame, Client.language, js5.CONFIG);
            SpotAnimationTypeList.instance = new SpotAnimationTypeList(Client.modeGame, Client.language, js5.CONFIG_SPOT, js5.MODELS);
            StructTypeList.instance = new StructTypeList(Client.modeGame, Client.language, js5.CONFIG);
            VarcstrTypeList.instance = new VarcstrTypeList(Client.modeGame, Client.language, js5.CONFIG);
            VarcTypeList.instance = new VarcTypeList(Client.modeGame, Client.language, js5.CONFIG);
            VarBitTypeListClient.instance = new VarBitTypeListClient(Client.modeGame, Client.language, js5.CONFIG_STRUCT);
            VarPlayerTypeListClient.instance = new VarPlayerTypeListClient(Client.modeGame, Client.language, js5.CONFIG);
            VarClanSettingTypeList.instance = new VarClanSettingTypeList(Client.modeGame, Client.language, js5.CONFIG);
            VarClanTypeList.instance = new VarClanTypeList(Client.modeGame, Client.language, js5.CONFIG);
            InterfaceManager.init(js5.INTERFACES, js5.FONTMETRICS, js5.SPRITES, js5.MODELS);
            Static110.setBillboardJs5(js5.CONFIG_BILLBOARD);
            QuickChatCatTypeList.instance = new QuickChatCatTypeList(Client.language, js5.QUICKCHAT, js5.QUICKCHAT_GLOBAL);
            QuickChatPhraseTypeList.instance = new QuickChatPhraseTypeList(Client.language, js5.QUICKCHAT, js5.QUICKCHAT_GLOBAL, new SimpleFillerDecoder());
            PlayerEntity.initWornObjIds();
            LocTypeList.instance.setAnimateBackground(ClientOptions.instance.animateBackground.getValue() == 0);
            TimedVarDomain.instance = new TimedVarDomain();
            Static296.updateFeatureMask();
            Animator.setSeqTL(SeqTypeList.instance);
            ParticleManager.init(js5.CONFIG_PARTICLE);
            Static405.method5592(Js5TextureSource.instance, js5.MODELS);
            @Pc(1119) Huffman codec = new Huffman(js5.BINARY.getfile("huffman", ""));
            WordPack.setHuffman(codec);

            try {
                jagmisc.init();
            } catch (@Pc(1126) Throwable ignored) {
                /* empty */
            }

            GameShell.tickScheduler = TickScheduler.create();
            SystemInfo.instance = new SystemInfo(true, GameShell.signLink);
        }

        if (LoadState.SETUP_STATIC_SPRITES == state) {
            @Pc(12) int ready = Sprites.readyCount(js5.SPRITES) + FontTypeList.readyCount(true);
            @Pc(74) int total = Sprites.totalCount() + FontTypeList.totalCount();
            if (ready < total) {
                return (ready * 100) / total;
            }
        }

        if (LoadState.SETUP_WORLD_MAP == state) {
            WorldMap.init(js5.WORLDMAPDATA, FloorOverlayTypeList.instance, FloorUnderlayTypeList.instance, LocTypeList.instance, MapElementTypeList.instance, MSITypeList.instance, TimedVarDomain.instance);
        }

        if (LoadState.SETUP_VARC_SYSTEM == state) {
            Static37.varcstrs = new String[VarcstrTypeList.instance.num];
            Static511.varcs = new int[VarcTypeList.instance.num];
            Static118.permVarcs = new boolean[VarcTypeList.instance.num];
            for (@Pc(12) int i = 0; i < VarcTypeList.instance.num; i++) {
                if (VarcTypeList.instance.list(i).temporary == 0) {
                    Static118.permVarcs[i] = true;
                    Static319.permVarcCount++;
                }
                Static511.varcs[i] = -1;
            }
            Static218.readVarcs();
            js5.MAPS.clearNames(false, true);
            js5.MIDI_SONGS.clearNames(true, true);
            js5.SPRITES.clearNames(true, true);
            js5.FONTMETRICS.clearNames(true, true);
            js5.BINARY.clearNames(true, true);
            js5.CONFIG.discardunpacked = 2;
            Client.cleanCaches = true;
            js5.CONFIG_ENUM.discardunpacked = 2;
            js5.CONFIG_LOC.discardunpacked = 2;
            js5.CONFIG_NPC.discardunpacked = 2;
            js5.CONFIG_OBJ.discardunpacked = 2;
            js5.CONFIG_SEQ.discardunpacked = 2;
            js5.CONFIG_SPOT.discardunpacked = 2;
        }

        if (state == LoadState.LOAD_LOGIN_WINDOW) {
            if (!InterfaceList.load(GraphicsDefaults.instance.login_interface)) {
                return 0;
            }

            @Pc(184) boolean ready = true;
            for (@Pc(74) int i = 0; i < InterfaceList.interfaces[GraphicsDefaults.instance.login_interface].length; i++) {
                @Pc(1315) Component component = InterfaceList.interfaces[GraphicsDefaults.instance.login_interface][i];

                if (component.type == Component.TYPE_GRAPHIC && component.graphic != -1 && !js5.SPRITES.requestdownload(0, component.graphic)) {
                    ready = false;
                }
            }

            if (!ready) {
                return 0;
            }
        }

        if (LoadState.SHOW_LOGIN_WINDOW == state) {
            InterfaceManager.openLogin(true);
        }

        if (LoadState.CLEANUP == state) {
            renderer.complete();

            try {
                rendererThread.join();
            } catch (@Pc(1370) InterruptedException ignored) {
                return 0;
            }

            screens = null;
            renderer = null;
            js5.LOADING_SPRITES = null;
            js5.LOADING_SCREENS = null;
            rendererThread = null;
            LoadingScreenManager.instance = null;
            cacheReset();
            Static3.chooseSafeMode = ClientOptions.instance.safeMode.getValue() == 1;
            ClientOptions.instance.update(1, ClientOptions.instance.safeMode);
            if (Static3.chooseSafeMode) {
                ClientOptions.instance.update(0, ClientOptions.instance.toolkitDefault);
            } else if (ClientOptions.instance.toolkitDefault.dflt && SystemInfo.instance.totalMemory < 512 && SystemInfo.instance.totalMemory != 0) {
                ClientOptions.instance.update(0, ClientOptions.instance.toolkitDefault);
            }
            ClientOptions.save();
            if (Static3.chooseSafeMode) {
                Static32.setToolkit(ToolkitType.JAVA, false);
            } else {
                Static32.setToolkit(ClientOptions.instance.toolkitDefault.getValue(), false);
            }
            InterfaceManager.changeWindowMode(ClientOptions.instance.screenSizeDefault.getValue(), -1, -1, false);
            FontTypeList.method7549(Toolkit.active);
            Fonts.init(Toolkit.active);
            Sprites.init(js5.SPRITES, Toolkit.active);
            MiniMenu.setIcons(Sprites.nameIcons);
        }

        return Static694.method9030();
    }

    @OriginalMember(owner = "client!hc", name = "a", descriptor = "(I)V")
    public static void startRenderer() {
        if (state != null) {
            renderer = new LoadingScreenRenderer();
            renderer.updateState(state.startPercentage, state.stalledText.localise(Client.language), state, lastRendererUpdate);

            rendererThread = new Thread(renderer, "");
            rendererThread.start();
        }
    }

    @OriginalMember(owner = "client!wh", name = "g", descriptor = "(I)V")
    public static void update() {
        if (states == null) {
            states = LoadState.values();
            state = states[0];
            lastRendererUpdate = SystemTimer.safetime();
        }

        if (renderer == null) {
            startRenderer();
        }

        @Pc(27) LoadState oldState = state;
        @Pc(35) int essentials = essentials();

        if (state == oldState) {
            text = state.stalledText.localise(Client.language);

            if (state.updatePercentage) {
                percentage = state.startPercentage + ((essentials * (state.endPercentage - state.startPercentage)) / 100);
            }

            if (state.displayPercentage) {
                text = text + percentage + "%";
            }
        } else if (state == LoadState.COMPLETE) {
            renderer = null;
            MainLogicManager.setStep(MainLogicStep.STEP_LOGIN_SCREEN);
        } else {
            text = oldState.changedText.localise(Client.language);
            if (state.displayPercentage) {
                text = text + oldState.endPercentage + "%";
            }

            percentage = oldState.endPercentage;
            if (state.updatePercentage || oldState.updatePercentage) {
                lastRendererUpdate = SystemTimer.safetime();
            }
        }

        if (renderer == null) {
            return;
        }

        renderer.updateState(percentage, text, state, lastRendererUpdate);

        if (screens == null) {
            return;
        }

        for (@Pc(157) int i = screen + 1; i < screens.length; i++) {
            if (screens[i].percentage() >= 100 && screen == i - 1 && MainLogicManager.step >= MainLogicStep.STEP_LOADING_1 && renderer.canChangeScreen()) {
                try {
                    screens[i].init();
                } catch (@Pc(197) Exception ignored) {
                    screens = null;
                    return;
                }

                renderer.change(screens[i]);

                screen++;

                if (screen >= screens.length - 1 && screens.length > 1) {
                    screen = LoadingScreenManager.instance.hasDefault() ? 0 : -1;
                }
            }
        }
    }
}

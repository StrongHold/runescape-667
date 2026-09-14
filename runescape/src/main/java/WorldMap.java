import com.jagex.Client;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.constants.MiniMenuAction;
import com.jagex.core.datastruct.key.DequeIterator;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.Queue;
import com.jagex.core.io.Packet;
import com.jagex.game.LocalisedText;
import com.jagex.game.runetek6.config.flotype.FloorOverlayType;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayType;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayTypeList;
import com.jagex.game.runetek6.config.meltype.MapElementType;
import com.jagex.game.runetek6.config.meltype.MapElementTypeList;
import com.jagex.game.runetek6.config.msitype.MSIType;
import com.jagex.game.runetek6.config.msitype.MSITypeList;
import com.jagex.game.runetek6.config.vartype.VarDomain;
import com.jagex.game.runetek6.config.loctype.LocInteractivity;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.vartype.bit.VarBitTypeListClient;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.js5;
import com.jagex.math.ColourUtils;
import com.jagex.trigger.ClientTriggerType;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!baa")
public final class WorldMap {

    @OriginalMember(owner = "client!baa", name = "D", descriptor = "Lclient!sia;")
    public static final Deque elements = new Deque();

    @OriginalMember(owner = "client!baa", name = "e", descriptor = "Lclient!av;")
    public static final IterableHashTable areas = new IterableHashTable(16);

    @OriginalMember(owner = "client!baa", name = "J", descriptor = "[B")
    public static final byte[] singleLocShapes = new byte[1];

    @OriginalMember(owner = "client!baa", name = "B", descriptor = "[S")
    public static final short[] singleLocIds = new short[1];

    @OriginalMember(owner = "client!o", name = "jb", descriptor = "Lclient!jg;")
    public static final DequeIterator elementIterator = new DequeIterator();

    @OriginalMember(owner = "client!hda", name = "ob", descriptor = "Lclient!av;")
    public static final IterableHashTable disabledElementCategories = new IterableHashTable(8);

    @OriginalMember(owner = "client!ih", name = "D", descriptor = "Lclient!av;")
    public static final IterableHashTable disabledElements = new IterableHashTable(8);

    @OriginalMember(owner = "client!be", name = "L", descriptor = "[Ljava/lang/String;")
    public static final String[] mapElementTextLines = new String[5];

    @OriginalMember(owner = "client!gia", name = "s", descriptor = "Lclient!hda;")
    public static Component component;

    @OriginalMember(owner = "client!cw", name = "C", descriptor = "Z")
    public static boolean hovered = false;

    @OriginalMember(owner = "client!lja", name = "l", descriptor = "I")
    public static int optionsX = -1;

    @OriginalMember(owner = "client!tba", name = "g", descriptor = "Lclient!hda;")
    public static Component optionsComponent = null;

    @OriginalMember(owner = "client!eu", name = "ic", descriptor = "I")
    public static int optionsY = -1;

    @OriginalMember(owner = "client!dm", name = "c", descriptor = "Z")
    public static boolean clicked = false;

    @OriginalMember(owner = "client!vp", name = "K", descriptor = "I")
    public static int clickedLevel;

    @OriginalMember(owner = "client!ps", name = "c", descriptor = "I")
    public static int clickedX;

    @OriginalMember(owner = "client!th", name = "p", descriptor = "I")
    public static int clickedY;

    @OriginalMember(owner = "client!ik", name = "t", descriptor = "I")
    public static int loadingPercent = 0;

    @OriginalMember(owner = "client!baa", name = "j", descriptor = "F")
    public static float currentZoom;

    @OriginalMember(owner = "client!kh", name = "ib", descriptor = "I")
    public static int width;

    @OriginalMember(owner = "client!sj", name = "b", descriptor = "I")
    public static int height;

    @OriginalMember(owner = "client!baa", name = "f", descriptor = "I")
    public static int areaHeight;

    @OriginalMember(owner = "client!baa", name = "G", descriptor = "I")
    public static int areaWidth;

    @OriginalMember(owner = "client!baa", name = "A", descriptor = "Lclient!ml;")
    public static MapElementTypeList mapElementTypeList;

    @OriginalMember(owner = "client!baa", name = "m", descriptor = "Lclient!ip;")
    public static WorldMapArea area;

    @OriginalMember(owner = "client!baa", name = "w", descriptor = "Lclient!sb;")
    public static js5 data;

    @OriginalMember(owner = "client!baa", name = "t", descriptor = "I")
    public static int areaX;

    @OriginalMember(owner = "client!baa", name = "F", descriptor = "I")
    public static int areaZ;

    @OriginalMember(owner = "client!dl", name = "k", descriptor = "I")
    public static int areaBaseZ;

    @OriginalMember(owner = "client!vs", name = "o", descriptor = "I")
    public static int areaBaseX;

    @OriginalMember(owner = "client!baa", name = "x", descriptor = "F")
    public static float targetZoom;

    @OriginalMember(owner = "client!baa", name = "u", descriptor = "I")
    public static int tileSize;

    @OriginalMember(owner = "client!baa", name = "k", descriptor = "[[[B")
    public static byte[][][] tileShapes;

    @OriginalMember(owner = "client!rfa", name = "y", descriptor = "Lclient!sia;")
    public static Deque boundedEntries;

    @OriginalMember(owner = "client!baa", name = "g", descriptor = "I")
    public static int mapDl = (int) (Math.random() * 17.0D) - 8;

    @OriginalMember(owner = "client!baa", name = "q", descriptor = "I")
    public static int mapDh = (int) (Math.random() * 11.0D) - 5;

    @OriginalMember(owner = "client!baa", name = "y", descriptor = "Lclient!u;")
    public static MSITypeList msiTypeList;

    @OriginalMember(owner = "client!baa", name = "H", descriptor = "Lclient!gea;")
    public static LocTypeList locTypeList;

    @OriginalMember(owner = "client!baa", name = "M", descriptor = "Lclient!nc;")
    public static MapElementList staticElements;

    @OriginalMember(owner = "client!baa", name = "I", descriptor = "Lclient!uk;")
    public static VarDomain varDomain;

    @OriginalMember(owner = "client!baa", name = "b", descriptor = "Lclient!ef;")
    public static FloorOverlayTypeList floorOverlayTypeList;

    @OriginalMember(owner = "client!baa", name = "O", descriptor = "Lclient!dh;")
    public static FloorUnderlayTypeList floorUnderlayTypeList;

    @OriginalMember(owner = "client!baa", name = "d", descriptor = "[S")
    public static short[] tileLocIds;

    @OriginalMember(owner = "client!baa", name = "z", descriptor = "[B")
    public static byte[] underlayColoursHigh;

    @OriginalMember(owner = "client!baa", name = "p", descriptor = "[B")
    public static byte[] underlays;

    @OriginalMember(owner = "client!baa", name = "v", descriptor = "I")
    public static int viewZ2;

    @OriginalMember(owner = "client!baa", name = "h", descriptor = "[S")
    public static short[] underlayColoursLow;

    @OriginalMember(owner = "client!baa", name = "C", descriptor = "I")
    public static int screenY2;

    @OriginalMember(owner = "client!baa", name = "E", descriptor = "[B")
    public static byte[] tileLocShapes;

    @OriginalMember(owner = "client!baa", name = "s", descriptor = "I")
    public static int viewX2;

    @OriginalMember(owner = "client!baa", name = "o", descriptor = "[I")
    public static int[] overlayColours;

    @OriginalMember(owner = "client!baa", name = "c", descriptor = "I")
    public static int screenX1;

    @OriginalMember(owner = "client!baa", name = "i", descriptor = "[B")
    public static byte[] overlayShapes;

    @OriginalMember(owner = "client!baa", name = "l", descriptor = "Lclient!av;")
    public static IterableHashTable multiLocs;

    @OriginalMember(owner = "client!baa", name = "r", descriptor = "I")
    public static int screenX2;

    @OriginalMember(owner = "client!baa", name = "K", descriptor = "I")
    public static int viewX1;

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "I")
    public static int screenY1;

    @OriginalMember(owner = "client!baa", name = "N", descriptor = "[B")
    public static byte[] overlays;

    @OriginalMember(owner = "client!baa", name = "n", descriptor = "I")
    public static int viewZ1;

    @OriginalMember(owner = "client!baa", name = "L", descriptor = "[[[Lclient!fla;")
    public static LinkedList[][][] tiles;

    @OriginalMember(owner = "client!rk", name = "w", descriptor = "I")
    public static int jumpZ = -1;

    @OriginalMember(owner = "client!fba", name = "c", descriptor = "I")
    public static int displayX;

    @OriginalMember(owner = "client!tha", name = "e", descriptor = "I")
    public static int displayZ;

    @OriginalMember(owner = "client!lea", name = "c", descriptor = "I")
    public static int lastAreaId;

    @OriginalMember(owner = "client!fka", name = "g", descriptor = "I")
    public static int jumpX = -1;

    @OriginalMember(owner = "client!fj", name = "C", descriptor = "Z")
    public static boolean disableElements = false;

    @OriginalMember(owner = "client!mt", name = "G", descriptor = "I")
    public static int flashingElementCategory = -1;

    @OriginalMember(owner = "client!pa", name = "a", descriptor = "I")
    public static int flashingElement = -1;

    @OriginalMember(owner = "client!kc", name = "f", descriptor = "I")
    public static int flashCycles;

    @OriginalMember(owner = "client!gka", name = "m", descriptor = "I")
    public static int flashTimer;

    @OriginalMember(owner = "client!dk", name = "v", descriptor = "I")
    public static int toolkitType = -1;

    @OriginalMember(owner = "client!rka", name = "Ub", descriptor = "Lclient!rt;")
    public static WorldMapFont font11;

    @OriginalMember(owner = "client!pea", name = "l", descriptor = "Lclient!rt;")
    public static WorldMapFont font12;

    @OriginalMember(owner = "client!eha", name = "d", descriptor = "Lclient!rt;")
    public static WorldMapFont font14;

    @OriginalMember(owner = "client!uja", name = "j", descriptor = "Lclient!rt;")
    public static WorldMapFont font17;

    @OriginalMember(owner = "client!il", name = "v", descriptor = "Lclient!rt;")
    public static WorldMapFont font19;

    @OriginalMember(owner = "client!mda", name = "P", descriptor = "Lclient!rt;")
    public static WorldMapFont font22;

    @OriginalMember(owner = "client!lia", name = "r", descriptor = "Lclient!rt;")
    public static WorldMapFont font26;

    @OriginalMember(owner = "client!lfa", name = "k", descriptor = "Lclient!rt;")
    public static WorldMapFont font30;

    @OriginalMember(owner = "client!aha", name = "k", descriptor = "Lclient!st;")
    public static Sprite overviewSprite;

    @OriginalMember(owner = "client!qaa", name = "c", descriptor = "I")
    public static int viewportZ;

    @OriginalMember(owner = "client!qq", name = "c", descriptor = "I")
    public static int viewportX;

    @OriginalMember(owner = "client!w", name = "i", descriptor = "Z")
    public static boolean mapOverride = false;

    @OriginalMember(owner = "client!qla", name = "d", descriptor = "I")
    public static int mapZ = -1;

    @OriginalMember(owner = "client!hb", name = "g", descriptor = "I")
    public static int mapX = -1;

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!sb;Lclient!ef;Lclient!dh;Lclient!gea;Lclient!ml;Lclient!u;Lclient!uk;)V")
    public static void init(@OriginalArg(0) js5 data, @OriginalArg(1) FloorOverlayTypeList floorOverlayTypeList, @OriginalArg(2) FloorUnderlayTypeList floorUnderlayTypeList, @OriginalArg(3) LocTypeList locTypeList, @OriginalArg(4) MapElementTypeList mapElementTypeList, @OriginalArg(5) MSITypeList msiTypeList, @OriginalArg(6) VarDomain varDomain) {
        WorldMap.data = data;
        WorldMap.floorOverlayTypeList = floorOverlayTypeList;
        WorldMap.floorUnderlayTypeList = floorUnderlayTypeList;
        WorldMap.locTypeList = locTypeList;
        WorldMap.mapElementTypeList = mapElementTypeList;
        WorldMap.msiTypeList = msiTypeList;
        WorldMap.varDomain = varDomain;

        areas.clear();

        @Pc(23) int detailsGroup = WorldMap.data.getgroupid("details");
        @Pc(28) int[] files = WorldMap.data.fileIds(detailsGroup);
        if (files != null) {
            for (@Pc(32) int i = 0; i < files.length; i++) {
                @Pc(41) WorldMapArea area = WorldMapArea.decode(WorldMap.data, detailsGroup, files[i]);
                areas.put(area.id, area);
            }
        }

        ColourUtils.init(false, true);
    }

    @OriginalMember(owner = "client!cu", name = "a", descriptor = "(IIIILclient!d;Lclient!ha;I)V")
    public static void draw(@OriginalArg(1) int childHeight, @OriginalArg(2) int childX, @OriginalArg(3) int childY, @OriginalArg(4) TextureSource source, @OriginalArg(5) Toolkit toolkit, @OriginalArg(6) int childWidth) {
        if (loadingPercent < 100) {
            load(source, toolkit);
        }

        toolkit.KA(childX, childY, childX + childWidth, childY + childHeight);

        if (loadingPercent < 100) {
            @Pc(38) int x = (childWidth / 2) + childX;
            toolkit.aa(childX, childY, childWidth, childHeight, 0xFF000000, 0);
            @Pc(57) int y = (childY + (childHeight / 2)) - 20 - 18;
            toolkit.outlineRect(x - 152, y, 304, 34, Client.OUTLINE_COLOURS[Client.colourId].getRGB(), 0);
            toolkit.aa(x - 150, y + 2, loadingPercent * 3, 30, Client.FILL_COLOURS[Client.colourId].getRGB(), 0);
            Fonts.b12.renderCentre(LocalisedText.LOADINGDOTDOTDOT.localise(Client.language), x, y + 20, Client.TEXT_COLOURS[Client.colourId].getRGB(), -1);
        } else {
            @Pc(114) int x1 = displayX - (int) ((float) childWidth / currentZoom);
            @Pc(155) int y1 = displayZ - (int) ((float) childHeight / currentZoom);
            @Pc(57) int x2 = displayX + (int) ((float) childWidth / currentZoom);
            @Pc(38) int y2 = displayZ + (int) ((float) childHeight / currentZoom);

            width = (int) ((float) (childWidth * 2) / currentZoom);
            height = (int) ((float) (childHeight * 2) / currentZoom);

            viewportZ = displayZ - (int) ((float) childHeight / currentZoom);
            viewportX = displayX - (int) ((float) childWidth / currentZoom);

            setView(x1 + areaX, y1 + areaZ, x2 + areaX, y2 + areaZ, childX, childY, childWidth + childX, childHeight + childY + 1);
            drawTiles(toolkit);

            @Pc(203) Deque entries = positionElements(toolkit);
            renderElements(entries, toolkit);

            if (flashCycles > 0) {
                flashTimer--;
                if (flashTimer == 0) {
                    flashCycles--;
                    flashTimer = 20;
                }
            }

            if (Client.displayFps) {
                @Pc(250) int textX = childWidth + childX - 5;
                @Pc(256) int textY = childHeight + childY - 8;
                Fonts.p12.renderRight("Fps:" + GameShell.currentFps, textX, textY, 0xFFFF00, -1);
                @Pc(273) int memoryTextY = textY - 15;

                @Pc(275) Runtime runtime = Runtime.getRuntime();
                @Pc(285) int memKb = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
                @Pc(287) int colour = 0xFFFF00;
                if (memKb > 65536) {
                    colour = 0xFF0000;
                }
                Fonts.p12.renderRight("Mem:" + memKb + "k", textX, memoryTextY, colour, -1);
                textY = memoryTextY - 15;
            }
        }
    }

    @OriginalMember(owner = "client!qda", name = "a", descriptor = "(BILclient!ha;III)V")
    public static void drawOverview(@OriginalArg(1) int width, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) int height, @OriginalArg(4) int x, @OriginalArg(5) int z) {
        toolkit.KA(x, z, x + width, height + z);
        toolkit.fillRect(x, z, width, height, 0xFF000000);

        if (loadingPercent < 100) {
            return;
        }

        @Pc(44) float aspectRatio = (float) areaHeight / (float) areaWidth;

        @Pc(46) int newWidth = width;
        @Pc(48) int newHeight = height;
        if (aspectRatio < 1.0F) {
            newHeight = (int) (aspectRatio * (float) width);
        } else {
            newWidth = (int) ((float) height / aspectRatio);
        }

        @Pc(75) int newX = x + ((width - newWidth) / 2);
        @Pc(84) int newY = z + ((height - newHeight) / 2);

        if (overviewSprite == null || overviewSprite.getWidth() != width || overviewSprite.getHeight() != height) {
            setView(areaX, areaZ, areaWidth + areaX, areaZ + areaHeight, newX, newY, newX + newWidth, newY - -newHeight);
            drawTiles(toolkit);
            overviewSprite = toolkit.createSprite(newX, newY, newWidth, newHeight, false);
        }

        overviewSprite.render(newX, newY);

        @Pc(138) int rectWidth = (newWidth * WorldMap.width) / areaWidth;
        @Pc(144) int rectHeight = (newHeight * WorldMap.height) / areaHeight;
        @Pc(152) int rectX = viewportX * newWidth / areaWidth + newX;
        @Pc(166) int rectY = newHeight + newY - rectHeight - viewportZ * newHeight / areaHeight;

        @Pc(168) int colour = 0x88FF0000;
        if (Client.modeGame == ModeGame.STELLAR_DAWN) {
            colour = 0x88FFFFFF;
        }

        toolkit.aa(rectX, rectY, rectWidth, rectHeight, colour, 1);
        toolkit.outlineRect(rectX, rectY, rectWidth, rectHeight, colour, 0);

        if (flashCycles <= 0) {
            return;
        }

        @Pc(202) int alpha;
        if (flashTimer > 50) {
            alpha = (100 - flashTimer) * 5;
        } else {
            alpha = flashTimer * 5;
        }

        for (@Pc(213) MapElementListEntry entry = (MapElementListEntry) elements.first(); entry != null; entry = (MapElementListEntry) elements.next()) {
            @Pc(221) MapElementType elementType = mapElementTypeList.list(entry.id);

            if (isEnabled(elementType)) {
                if (flashingElement == entry.id) {
                    @Pc(256) int drawX = newX + ((newWidth * entry.x) / areaWidth);
                    @Pc(269) int drawY = newY + ((newHeight * (areaHeight - entry.z)) / areaHeight);
                    toolkit.fillRect(drawX - 2, drawY - 2, 4, 4, (alpha << 24) | 0xFFFF00);
                } else if (flashingElementCategory != -1 && flashingElementCategory == elementType.category) {
                    @Pc(256) int drawX = newX + ((newWidth * entry.x) / areaWidth);
                    @Pc(269) int drawY = newY + (((areaHeight - entry.z) * newHeight) / areaHeight);
                    toolkit.fillRect(drawX + -2, drawY - 2, 4, 4, (alpha << 24) | 0xFFFF00);
                }
            }
        }
    }

    @OriginalMember(owner = "client!fo", name = "d", descriptor = "(I)Lclient!ip;")
    public static WorldMapArea getArea() {
        return area;
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(I)V")
    public static void setArea(@OriginalArg(0) int id) {
        area = (WorldMapArea) areas.get(id);
    }

    @OriginalMember(owner = "client!gf", name = "a", descriptor = "(IIIBI)V")
    public static void clickedOverview(@OriginalArg(0) int width, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(4) int height) {
        @Pc(16) float aspectRatio = (float) areaHeight / (float) areaWidth;

        @Pc(18) int newWidth = width;
        @Pc(20) int newHeight = height;
        if (aspectRatio < 1.0F) {
            newHeight = (int) ((float) width * aspectRatio);
        } else {
            newWidth = (int) ((float) height / aspectRatio);
        }

        @Pc(47) int newX = y - (height - newHeight) / 2;
        @Pc(56) int newY = x - (width - newWidth) / 2;
        displayX = (newY * areaWidth) / newWidth;
        displayZ = areaHeight - ((areaHeight * newX) / newHeight);
        jumpZ = -1;
        jumpX = -1;
        checkJump();
    }

    @OriginalMember(owner = "client!cba", name = "a", descriptor = "(IZILclient!hda;)V")
    public static void setOptions(@OriginalArg(0) int optionsX, @OriginalArg(2) int optionsY, @OriginalArg(3) Component optionsComponent) {
        WorldMap.optionsX = optionsX;
        WorldMap.optionsComponent = optionsComponent;
        WorldMap.optionsY = optionsY;
    }

    @OriginalMember(owner = "client!wq", name = "a", descriptor = "(Lclient!d;Lclient!ha;Z)V")
    public static void load(@OriginalArg(0) TextureSource textureSource, @OriginalArg(1) Toolkit toolkit) {
        if (area == null) {
            return;
        }

        if (loadingPercent < 10) {
            if (!data.requestgroupdownload(area.file)) {
                loadingPercent = js5.WORLDMAPDATA.completePercentage(area.file) / 10;
                return;
            }

            Static700.method9152();
            loadingPercent = 10;
        }

        if (loadingPercent == 10) {
            areaX = (area.chunkMinX >> 6) << 6;
            areaZ = (area.chunkMinZ >> 6) << 6;

            areaWidth = ((area.chunkMaxX >> 6) << 6) - areaX + 64;
            areaHeight = ((area.chunkMaxZ >> 6) << 6) - areaZ + 64;

            @Pc(77) int[] coord = new int[3];
            @Pc(79) int relativeX = -1;
            @Pc(81) int relativeY = -1;
            if (area.projectDisplay(coord, PlayerEntity.self.level, (PlayerEntity.self.x >> 9) + areaBaseX, areaBaseZ + (PlayerEntity.self.z >> 9))) {
                relativeX = coord[1] - areaX;
                relativeY = coord[2] - areaZ;
            }

            if (!mapOverride && relativeX >= 0 && relativeX < areaWidth && relativeY >= 0 && relativeY < areaHeight) {
                relativeY += (int) (Math.random() * 10.0D) - 5;
                relativeX += (int) (Math.random() * 10.0D) - 5;
                displayX = relativeX;
                displayZ = relativeY;
            } else if (mapX != -1 && mapZ != -1) {
                area.projectDisplay(coord, mapX, mapZ);

                if (coord != null) {
                    displayX = coord[1] - areaX;
                    displayZ = coord[2] - areaZ;
                }

                mapOverride = false;
                mapZ = -1;
                mapX = -1;
            } else {
                area.projectDisplay(coord, (area.origin >> 14) & 0x3FFF, area.origin & 0x3FFF);
                displayZ = coord[2] - areaZ;
                displayX = coord[1] - areaX;
            }

            if (area.zoom == 37) {
                currentZoom = 3.0F;
                targetZoom = 3.0F;
            } else if (area.zoom == 50) {
                currentZoom = 4.0F;
                targetZoom = 4.0F;
            } else if (area.zoom == 75) {
                currentZoom = 6.0F;
                targetZoom = 6.0F;
            } else if (area.zoom == 100) {
                currentZoom = 8.0F;
                targetZoom = 8.0F;
            } else if (area.zoom == 200) {
                currentZoom = 16.0F;
                targetZoom = 16.0F;
            } else {
                currentZoom = 8.0F;
                targetZoom = 8.0F;
            }

            tileSize = (int) currentZoom >> 1;
            tileShapes = Static640.createTileShapeMasks(tileSize);

            checkJump();
            allocateBuffers();

            boundedEntries = new Deque();

            mapDh += (int) (Math.random() * 5.0D) - 2;
            if (mapDh < -8) {
                mapDh = -8;
            }

            mapDl += (int) (Math.random() * 5.0D) - 2;
            if (mapDh > 8) {
                mapDh = 8;
            }

            if (mapDl < -16) {
                mapDl = -16;
            }
            if (mapDl > 16) {
                mapDl = 16;
            }

            initOverlayColours(textureSource, mapDh >> 2 << 10, mapDl >> 1);
            mapElementTypeList.setCaches(1024, 256);
            msiTypeList.setCache(256, 256);
            locTypeList.setRecentUse(4096);
            VarBitTypeListClient.instance.cacheReset(256);
            loadingPercent = 20;
        } else if (loadingPercent == 20) {
            Static314.noTimeout(true);
            decodeArea(toolkit, mapDh, mapDl);
            loadingPercent = 60;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 60) {
            if (data.groupExists(area.file + "_staticelements")) {
                if (!data.requestgroupdownload(area.file + "_staticelements")) {
                    return;
                }

                staticElements = MapElementList.load(Static174.mapMembers, data, area.file + "_staticelements");
            } else {
                staticElements = new MapElementList(0);
            }

            loadStaticElements();
            loadingPercent = 70;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 70) {
            font11 = new WorldMapFont(toolkit, 11, true, GameShell.canvas);
            loadingPercent = 73;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 73) {
            font12 = new WorldMapFont(toolkit, 12, true, GameShell.canvas);
            loadingPercent = 76;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 76) {
            font14 = new WorldMapFont(toolkit, 14, true, GameShell.canvas);
            loadingPercent = 79;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 79) {
            font17 = new WorldMapFont(toolkit, 17, true, GameShell.canvas);
            loadingPercent = 82;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 82) {
            font19 = new WorldMapFont(toolkit, 19, true, GameShell.canvas);
            loadingPercent = 85;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 85) {
            font22 = new WorldMapFont(toolkit, 22, true, GameShell.canvas);
            loadingPercent = 88;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else if (loadingPercent == 88) {
            font26 = new WorldMapFont(toolkit, 26, true, GameShell.canvas);
            loadingPercent = 91;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
        } else {
            font30 = new WorldMapFont(toolkit, 30, true, GameShell.canvas);
            loadingPercent = 100;
            Static314.noTimeout(true);
            Static199.doneslowupdate();
            System.gc();
        }
    }

    @OriginalMember(owner = "client!mc", name = "b", descriptor = "(I)V")
    public static void checkJump() {
        if (displayX < 0) {
            jumpX = -1;
            jumpZ = -1;
            displayX = 0;
        }

        if (displayX > areaWidth) {
            jumpX = -1;
            displayX = areaWidth;
            jumpZ = -1;
        }

        if (displayZ < 0) {
            jumpZ = -1;
            jumpX = -1;
            displayZ = 0;
        }

        if (displayZ > areaHeight) {
            jumpZ = -1;
            jumpX = -1;
            displayZ = areaHeight;
        }
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!d;II)V")
    public static void initOverlayColours(@OriginalArg(0) TextureSource textureSource, @OriginalArg(1) int hueOffset, @OriginalArg(2) int lightnessOffset) {
        for (@Pc(1) int i = 0; i < floorOverlayTypeList.num; i++) {
            overlayColours[i + 1] = overlayColour(textureSource, i, hueOffset, lightnessOffset);
        }
    }

    @OriginalMember(owner = "client!baa", name = "b", descriptor = "(I)Lclient!ip;")
    public static WorldMapArea getArea(@OriginalArg(0) int id) {
        return (WorldMapArea) areas.get(id);
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;)V")
    public static void drawTiles(@OriginalArg(0) Toolkit toolkit) {
        @Pc(3) int viewWidth = viewX2 - viewX1;
        @Pc(7) int viewHeight = viewZ2 - viewZ1;
        @Pc(15) int scaleX = (screenX2 - screenX1 << 16) / viewWidth;
        @Pc(23) int scaleZ = (screenY2 - screenY1 << 16) / viewHeight;
        drawTiles(toolkit, scaleX, scaleZ);
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;IIIIIII[S[BZ)V")
    public static void drawTile(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int underlayColour, @OriginalArg(6) int overlay, @OriginalArg(7) int overlayShape, @OriginalArg(8) short[] locIds, @OriginalArg(9) byte[] locShapes, @OriginalArg(10) boolean opaque) {
        if (opaque || underlayColour != 0 || overlay > 0) {
            if (overlay == 0) {
                toolkit.aa(x, y, width, height, underlayColour, 0);
            } else {
                int shape = overlayShape & 0x3F;
                if (shape == 0 || width <= 1 || height <= 1) {
                    int colour = overlayColours[overlay];
                    if (opaque || colour != 0) {
                        toolkit.aa(x, y, width, height, colour, 0);
                    }
                } else {
                    Static339.method5007(height, tileShapes, overlayColours[overlay], width, tileSize, x, toolkit, opaque ? 0 : 1, overlayShape >> 6 & 0x3, y, underlayColour, shape);
                }
            }
        }
        if (locIds == null) {
            return;
        }
        @Pc(20) int right = width == 1 ? x : x + width - 1;
        @Pc(32) int bottom = height == 1 ? y : y + height - 1;
        for (@Pc(100) int i = 0; i < locIds.length; i++) {
            @Pc(107) int shape = locShapes[i] & 0x3F;
            if (shape == 0 || shape == 2 || shape == 3 || shape == 9) {
                @Pc(127) LocType locType = locTypeList.list(locIds[i] & 0xFFFF);
                if (locType.msi == -1) {
                    @Pc(133) int wallColour = -3355444;
                    if (locType.active == LocInteractivity.INTERACTIVE) {
                        wallColour = -3407872;
                    }
                    @Pc(147) int rotation = locShapes[i] >> 6 & 0x3;
                    if (shape == 0) {
                        if (rotation == 0) {
                            toolkit.P(x, y, height, wallColour, 0);
                        } else if (rotation == 1) {
                            toolkit.U(x, y, width, wallColour, 0);
                        } else if (rotation == 2) {
                            toolkit.P(right, y, height, wallColour, 0);
                        } else {
                            toolkit.U(x, bottom, width, wallColour, 0);
                        }
                    } else if (shape == 2) {
                        if (rotation == 0) {
                            toolkit.P(x, y, height, -1, 0);
                            toolkit.U(x, y, width, wallColour, 0);
                        } else if (rotation == 1) {
                            toolkit.P(right, y, height, -1, 0);
                            toolkit.U(x, y, width, wallColour, 0);
                        } else if (rotation == 2) {
                            toolkit.P(right, y, height, -1, 0);
                            toolkit.U(x, bottom, width, wallColour, 0);
                        } else {
                            toolkit.P(x, y, height, -1, 0);
                            toolkit.U(x, bottom, width, wallColour, 0);
                        }
                    } else if (shape == 3) {
                        if (rotation == 0) {
                            toolkit.U(x, y, 1, wallColour, 0);
                        } else if (rotation == 1) {
                            toolkit.U(right, y, 1, wallColour, 0);
                        } else if (rotation == 2) {
                            toolkit.U(right, bottom, 1, wallColour, 0);
                        } else {
                            toolkit.U(x, bottom, 1, wallColour, 0);
                        }
                    } else if (shape == 9) {
                        @Pc(313) int step;
                        if (rotation == 0 || rotation == 2) {
                            for (step = 0; step < height; step++) {
                                toolkit.U(x + step, bottom - step, 1, wallColour, 0);
                            }
                        } else {
                            for (step = 0; step < height; step++) {
                                toolkit.U(x + step, y + step, 1, wallColour, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(IIIIIIII)V")
    public static void setView(@OriginalArg(0) int x1, @OriginalArg(3) int z1, @OriginalArg(2) int x2, @OriginalArg(1) int z2, @OriginalArg(4) int left, @OriginalArg(5) int top, @OriginalArg(6) int right, @OriginalArg(7) int bottom) {
        viewX1 = x1 - areaX;
        viewZ2 = z2 - areaZ;
        viewX2 = x2 - areaX;
        viewZ1 = z1 - areaZ;
        screenX1 = left;
        screenY1 = top;
        screenX2 = right;
        screenY2 = bottom;
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "([B[B[SII)V")
    public static void blendUnderlays(@OriginalArg(0) byte[] underlayIds, @OriginalArg(1) byte[] coloursHigh, @OriginalArg(2) short[] coloursLow, @OriginalArg(3) int hueOffset, @OriginalArg(4) int lightnessOffset) {
        @Pc(2) int[] hueColumns = new int[areaHeight];
        @Pc(5) int[] saturationColumns = new int[areaHeight];
        @Pc(8) int[] lightnessColumns = new int[areaHeight];
        @Pc(11) int[] weightColumns = new int[areaHeight];
        @Pc(14) int[] countColumns = new int[areaHeight];
        for (@Pc(16) int x = -5; x < areaWidth; x++) {
            @Pc(21) int addX = x + 5;
            @Pc(25) int removeX = x - 5;
            @Pc(41) int hueSum;
            for (@Pc(27) int z = 0; z < areaHeight; z++) {
                @Pc(86) int previousCount;

                if (addX < areaWidth) {
                    int underlay = underlayIds[addX + z * areaWidth] & 0xFF;
                    if (underlay > 0) {
                        @Pc(50) FloorUnderlayType type = floorUnderlayTypeList.list(underlay - 1);
                        hueColumns[z] += type.anInt6630;
                        saturationColumns[z] += type.anInt6637;
                        lightnessColumns[z] += type.anInt6639;
                        weightColumns[z] += type.anInt6632;
                        previousCount = countColumns[z]++;
                    }
                }

                if (removeX >= 0) {
                    int underlay = underlayIds[removeX + z * areaWidth] & 0xFF;
                    if (underlay > 0) {
                        @Pc(50) FloorUnderlayType type = floorUnderlayTypeList.list(underlay - 1);
                        hueColumns[z] -= type.anInt6630;
                        saturationColumns[z] -= type.anInt6637;
                        lightnessColumns[z] -= type.anInt6639;
                        weightColumns[z] -= type.anInt6632;
                        previousCount = countColumns[z]--;
                    }
                }
            }

            if (x >= 0) {
                hueSum = 0;
                @Pc(159) int saturationSum = 0;
                @Pc(161) int lightnessSum = 0;
                @Pc(163) int weightSum = 0;
                @Pc(165) int count = 0;
                for (@Pc(167) int z = -5; z < areaHeight; z++) {
                    @Pc(172) int addZ = z + 5;
                    if (addZ < areaHeight) {
                        hueSum += hueColumns[addZ];
                        saturationSum += saturationColumns[addZ];
                        lightnessSum += lightnessColumns[addZ];
                        weightSum += weightColumns[addZ];
                        count += countColumns[addZ];
                    }
                    @Pc(209) int removeZ = z - 5;
                    if (removeZ >= 0) {
                        hueSum -= hueColumns[removeZ];
                        saturationSum -= saturationColumns[removeZ];
                        lightnessSum -= lightnessColumns[removeZ];
                        weightSum -= weightColumns[removeZ];
                        count -= countColumns[removeZ];
                    }
                    if (z >= 0 && count > 0) {
                        if ((underlayIds[x + z * areaWidth] & 0xFF) == 0) {
                            int index = x + z * areaWidth;
                            coloursHigh[index] = 0;
                            coloursLow[index] = 0;
                        } else {
                            @Pc(261) int hsl = weightSum == 0 ? 0 : Static318.method8555(lightnessSum / count, saturationSum / count, hueSum * 256 / weightSum);
                            @Pc(294) int lightness = (hsl & 0x7F) + lightnessOffset;
                            if (lightness < 0) {
                                lightness = 0;
                            } else if (lightness > 127) {
                                lightness = 127;
                            }
                            @Pc(316) int blendedHsl = (hsl + hueOffset & 0xFC00) + (hsl & 0x380) + lightness;
                            @Pc(322) int index = x + z * areaWidth;
                            @Pc(333) int rgb = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(Static75.method6238(blendedHsl)) & 0xFFFF];
                            coloursHigh[index] = (byte) (rgb >> 16 & 0xFF);
                            coloursLow[index] = (short) (rgb & 0xFFFF);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;IIII)V")
    public static void drawTiles(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int scaleX, @OriginalArg(2) int scaleZ) {
        @Pc(3) int tileCountX = viewX2 - viewX1;
        @Pc(7) int tileCountZ = viewZ2 - viewZ1;
        if (viewX2 < areaWidth) {
            tileCountX++;
        }
        if (viewZ2 < areaHeight) {
            tileCountZ++;
        }
        @Pc(28) int local28;
        @Pc(40) int local40;
        @Pc(44) int local44;
        @Pc(50) int local50;
        @Pc(57) int local57;
        @Pc(70) int local70;
        @Pc(80) int local80;
        @Pc(84) int local84;
        @Pc(93) int local93;
        @Pc(173) int local173;
        @Pc(175) int local175;
        @Pc(177) int local177;
        @Pc(179) int local179;
        for (@Pc(17) int column = 0; column < tileCountX; column++) {
            local28 = (scaleX * column >> 16) + screenX1;
            local40 = (scaleX * (column + 1) >> 16) + screenX1;
            local44 = local40 - local28;
            if (local44 > 0) {
                local50 = viewX1 + column;
                if (local50 >= 0 && local50 < areaWidth) {
                    for (local57 = 0; local57 < tileCountZ; local57++) {
                        local70 = screenY2 - (scaleZ * (local57 + 1) >> 16);
                        local80 = screenY2 - (scaleZ * local57 >> 16);
                        local84 = local80 - local70;
                        if (local84 > 0) {
                            local93 = local57 + viewZ1;
                            local173 = local50 + local93 * areaWidth;
                            local175 = 0;
                            local177 = 0;
                            local179 = 0;
                            if (local93 >= 0 && local93 < areaHeight) {
                                local175 = (underlayColoursHigh[local173] & 0xFF) << 16 | underlayColoursLow[local173] & 0xFFFF;
                                if (local175 != 0) {
                                    local175 |= 0xFF000000;
                                }
                                local177 = overlays[local173] & 0xFF;
                                local179 = tileLocIds[local173] & 0xFFFF;
                            }
                            if (local175 == 0 && local177 == 0 && local179 == 0) {
                                if (area.anInt4561 != -1) {
                                    local175 = area.anInt4561 | 0xFF000000;
                                } else if ((column + viewX1 & 0x4) == (local57 + viewZ2 & 0x4)) {
                                    local175 = overlayColours[floorOverlayTypeList.dflt + 1];
                                } else {
                                    local175 = 0xFF4B5368;
                                }
                                if (local175 == 0) {
                                    local175 = 0xFF000000;
                                }
                                toolkit.aa(local28, local70, local44, local84, local175, 0);
                            } else if (local179 <= 0) {
                                drawTile(toolkit, local28, local70, local44, local84, local175, local177, overlayShapes[local173], null, null, true);
                            } else if (local179 == 65535) {
                                @Pc(282) Node_Sub23 locList = (Node_Sub23) multiLocs.get(local50 << 16 | local93);
                                if (locList != null) {
                                    drawTile(toolkit, local28, local70, local44, local84, local175, local177, overlayShapes[local173], locList.aShortArray59, locList.aByteArray38, true);
                                }
                            } else {
                                singleLocIds[0] = (short) (local179 - 1);
                                singleLocShapes[0] = tileLocShapes[local173];
                                drawTile(toolkit, local28, local70, local44, local84, local175, local177, overlayShapes[local173], singleLocIds, singleLocShapes, true);
                            }
                        }
                    }
                } else {
                    for (local57 = 0; local57 < tileCountZ; local57++) {
                        local70 = screenY2 - (scaleZ * (local57 + 1) >> 16);
                        local80 = screenY2 - (scaleZ * local57 >> 16);
                        local84 = local80 - local70;
                        if (area.anInt4561 != -1) {
                            local93 = area.anInt4561 | 0xFF000000;
                        } else if ((column + viewX1 & 0x4) == (local57 + viewZ2 & 0x4)) {
                            local93 = overlayColours[floorOverlayTypeList.dflt + 1];
                        } else {
                            local93 = -11840664;
                        }
                        if (local93 == 0) {
                            local93 = -16777216;
                        }
                        toolkit.aa(local28, local70, local44, local84, local93, 0);
                    }
                }
            }
        }
        for (local28 = -16; local28 < tileCountX + 16; local28++) {
            local40 = (scaleX * local28 >> 16) + screenX1;
            local44 = (scaleX * (local28 + 1) >> 16) + screenX1;
            local50 = local44 - local40;
            if (local50 > 0) {
                local57 = local28 + viewX1;
                if (local57 >= 0 && local57 < areaWidth) {
                    for (local70 = -16; local70 < tileCountZ + 16; local70++) {
                        local80 = screenY2 - (scaleZ * (local70 + 1) >> 16);
                        local84 = screenY2 - (scaleZ * local70 >> 16);
                        local93 = local84 - local80;
                        if (local93 > 0) {
                            local173 = local70 + viewZ1;
                            if (local173 >= 0 && local173 < areaHeight) {
                                local175 = tileLocIds[local57 + local173 * areaWidth] & 0xFFFF;
                                if (local175 <= 0) {
                                    drawMsiMultiple(toolkit, local40, local80, local50, local93, null, null);
                                } else if (local175 == 65535) {
                                    @Pc(459) Node_Sub23 locList = (Node_Sub23) multiLocs.get(local57 << 16 | local173);
                                    if (locList != null) {
                                        drawMsiMultiple(toolkit, local40, local80, local50, local93, locList.aShortArray59, locList.aByteArray38);
                                    }
                                } else {
                                    singleLocIds[0] = (short) (local175 - 1);
                                    singleLocShapes[0] = tileLocShapes[local57 + local173 * areaWidth];
                                    drawMsiMultiple(toolkit, local40, local80, local50, local93, singleLocIds, singleLocShapes);
                                }
                            }
                        }
                    }
                }
            }
        }
        local40 = viewX1 >> 6;
        local44 = viewZ1 >> 6;
        if (local40 < 0) {
            local40 = 0;
        }
        if (local44 < 0) {
            local44 = 0;
        }
        local50 = viewX2 >> 6;
        local57 = viewZ2 >> 6;
        if (local50 >= tiles[0].length) {
            local50 = tiles[0].length - 1;
        }
        if (local57 >= tiles[0][0].length) {
            local57 = tiles[0][0].length - 1;
        }
        for (local70 = 0; local70 < 3; local70++) {
            @Pc(641) int local641;
            @Pc(653) int local653;
            @Pc(665) int local665;
            @Pc(675) int local675;
            @Pc(631) int local631;
            for (local80 = local40; local80 <= local50; local80++) {
                for (local84 = local44; local84 <= local57; local84++) {
                    @Pc(589) LinkedList chunkTiles = tiles[local70][local80][local84];
                    if (chunkTiles != null) {
                        local173 = (local80 + (areaX >> 6)) * 64;
                        local175 = (local84 + (areaZ >> 6)) * 64;
                        for (@Pc(612) WorldMapTile tile = (WorldMapTile) chunkTiles.first(); tile != null; tile = (WorldMapTile) chunkTiles.next()) {
                            local179 = local173 + tile.aByte138 - areaX - viewX1;
                            local631 = local175 + tile.aByte139 - areaZ - viewZ1;
                            local641 = (scaleX * local179 >> 16) + screenX1;
                            local653 = (scaleX * (local179 + 1) >> 16) + screenX1;
                            local665 = screenY2 - (scaleZ * (local631 + 1) >> 16);
                            local675 = screenY2 - (scaleZ * local631 >> 16);
                            drawTile(toolkit, local641, local665, local653 - local641, local675 - local665, tile.anInt9770, tile.aByte137 & 0xFF, tile.aByte136, tile.aShortArray133, tile.aByteArray104, false);
                        }
                    }
                }
            }
            for (local84 = local40; local84 <= local50; local84++) {
                for (local93 = local44; local93 <= local57; local93++) {
                    @Pc(727) LinkedList chunkTiles = tiles[local70][local84][local93];
                    if (chunkTiles != null) {
                        local175 = (local84 + (areaX >> 6)) * 64;
                        local177 = (local93 + (areaZ >> 6)) * 64;
                        for (@Pc(750) WorldMapTile tile = (WorldMapTile) chunkTiles.first(); tile != null; tile = (WorldMapTile) chunkTiles.next()) {
                            local631 = local175 + tile.aByte138 - areaX - viewX1;
                            local641 = local177 + tile.aByte139 - areaZ - viewZ1;
                            local653 = (scaleX * local631 >> 16) + screenX1;
                            local665 = (scaleX * (local631 + 1) >> 16) + screenX1;
                            local675 = screenY2 - (scaleZ * (local641 + 1) >> 16);
                            @Pc(813) int bottom = screenY2 - (scaleZ * local641 >> 16);
                            drawMsiMultiple(toolkit, local653, local675, local665 - local653, bottom - local675, tile.aShortArray133, tile.aByteArray104);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!d;III)I")
    public static int overlayColour(@OriginalArg(0) TextureSource textureSource, @OriginalArg(1) int id, @OriginalArg(2) int hueOffset, @OriginalArg(3) int lightnessOffset) {
        @Pc(4) FloorOverlayType type = floorOverlayTypeList.list(id);
        if (type == null) {
            return 0;
        }

        @Pc(11) int texture = type.texture;
        if (texture >= 0 && textureSource.getMetrics(texture).disableable) {
            texture = -1;
        }

        @Pc(68) int colour;
        if (type.blendColour >= 0) {
            @Pc(27) int hsl = type.blendColour;

            @Pc(33) int lightness = (hsl & 0x7F) + lightnessOffset;
            if (lightness < 0) {
                lightness = 0;
            } else if (lightness > 127) {
                lightness = 127;
            }

            @Pc(55) int blendedHsl = (hsl + hueOffset & 0xFC00) + (hsl & 0x380) + lightness;
            colour = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(ColourUtils.method3066(blendedHsl)) & 0xFFFF] | 0xFF000000;
        } else if (texture >= 0) {
            colour = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(ColourUtils.method3066(textureSource.getMetrics(texture).aShort37)) & 0xFFFF] | 0xFF000000;
        } else if (type.colour == -1) {
            colour = 0;
        } else {
            @Pc(27) int hsl = type.colour;
            @Pc(33) int lightness = (hsl & 0x7F) + lightnessOffset;
            if (lightness < 0) {
                lightness = 0;
            } else if (lightness > 127) {
                lightness = 127;
            }
            @Pc(55) int blendedHsl = (hsl + hueOffset & 0xFC00) + (hsl & 0x380) + lightness;
            colour = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(ColourUtils.method3066(blendedHsl)) & 0xFFFF] | 0xFF000000;
        }

        return colour;
    }

    @OriginalMember(owner = "client!baa", name = "c", descriptor = "()V")
    public static void allocateBuffers() {
        underlays = new byte[areaWidth * areaHeight];
        overlays = new byte[areaWidth * areaHeight];
        overlayShapes = new byte[areaWidth * areaHeight];
        tileLocIds = new short[areaWidth * areaHeight];
        tileLocShapes = new byte[areaWidth * areaHeight];
        multiLocs = new IterableHashTable(1024);
        tiles = new LinkedList[3][areaWidth >> 6][areaHeight >> 6];
        overlayColours = new int[floorOverlayTypeList.num + 1];
    }

    @OriginalMember(owner = "client!baa", name = "d", descriptor = "()V")
    public static void freeBuffers() {
        underlays = null;
        underlayColoursHigh = null;
        underlayColoursLow = null;
        overlays = null;
        overlayShapes = null;
        tileLocIds = null;
        tileLocShapes = null;
        multiLocs = null;
        tiles = null;
        overlayColours = null;
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;Lclient!fu;Lclient!el;)V")
    public static void drawLandmark(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) MapElementListEntry entry, @OriginalArg(2) MapElementType type) {
        if (type.landmarkPolygons == null) {
            return;
        }
        @Pc(7) int[] points = new int[type.landmarkPolygons.length];
        @Pc(20) int edge;
        @Pc(32) int x1;
        for (@Pc(9) int i = 0; i < points.length / 2; i++) {
            int pointX = type.landmarkPolygons[i * 2] + entry.x;
            int pointZ = type.landmarkPolygons[i * 2 + 1] + entry.z;
            points[i * 2] = screenX1 + (screenX2 - screenX1) * (pointX - viewX1) / (viewX2 - viewX1);
            points[i * 2 + 1] = screenY2 - (screenY2 - screenY1) * (pointZ - viewZ1) / (viewZ2 - viewZ1);
        }
        Minimap.method2371(toolkit, points, type.landmarkBackground);
        if (type.anInt2603 > 0) {
            @Pc(102) int y1;
            @Pc(110) int x2;
            @Pc(120) int y2;
            @Pc(125) int tempX;
            @Pc(127) int tempY;
            for (edge = 0; edge < points.length / 2 - 1; edge++) {
                x1 = points[edge * 2];
                y1 = points[edge * 2 + 1];
                x2 = points[(edge + 1) * 2];
                y2 = points[(edge + 1) * 2 + 1];
                if (x2 < x1) {
                    tempX = x1;
                    tempY = y1;
                    x1 = x2;
                    y1 = y2;
                    x2 = tempX;
                    y2 = tempY;
                } else if (x2 == x1 && y2 < y1) {
                    tempX = y1;
                    y1 = y2;
                    y2 = tempX;
                }
                toolkit.method7995(x1, y1, x2, y2, type.landmarkPalette[type.landmarkColorIndices[edge] & 0xFF], type.anInt2603, type.anInt2587, type.anInt2607);
            }
            x1 = points[points.length - 2];
            y1 = points[points.length - 1];
            x2 = points[0];
            y2 = points[1];
            if (x2 < x1) {
                tempX = x1;
                tempY = y1;
                x1 = x2;
                y1 = y2;
                x2 = tempX;
                y2 = tempY;
            } else if (x2 == x1 && y2 < y1) {
                tempX = y1;
                y1 = y2;
                y2 = tempX;
            }
            toolkit.method7995(x1, y1, x2, y2, type.landmarkPalette[type.landmarkColorIndices[type.landmarkColorIndices.length - 1] & 0xFF], type.anInt2603, type.anInt2587, type.anInt2607);
            return;
        }
        for (edge = 0; edge < points.length / 2 - 1; edge++) {
            toolkit.line(points[(edge + 1) * 2 + 1], points[edge * 2 + 1], points[(edge + 1) * 2], type.landmarkPalette[type.landmarkColorIndices[edge] & 0xFF], points[edge * 2]);
        }
        toolkit.line(points[1], points[points.length - 1], points[0], type.landmarkPalette[type.landmarkColorIndices[type.landmarkColorIndices.length - 1] & 0xFF], points[points.length - 2]);
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;Lclient!fu;IIII)V")
    public static void positionElement(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) MapElementListEntry entry, @OriginalArg(2) int scaleX, @OriginalArg(3) int scaleZ) {
        entry.spriteX = screenX1 + (scaleX * (entry.x - viewX1) >> 16);
        entry.spriteY = screenY2 - (scaleZ * (entry.z - viewZ1) >> 16);
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;Lclient!ge;IIII[I[I)V")
    public static void decodeTile(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) Packet packet, @OriginalArg(2) int chunkX, @OriginalArg(3) int chunkZ, @OriginalArg(4) int x, @OriginalArg(5) int z, @OriginalArg(6) int[] underlayPalette, @OriginalArg(7) int[] overlayPalette) {
        @Pc(3) int data = packet.g1();
        if ((data & 0x1) == 0) {
            @Pc(15) boolean underlay = (data & 0x2) == 0;
            @Pc(21) int id = data >> 2 & 0x3F;
            if (id != 62) {
                if (id == 63) {
                    id = packet.g1();
                } else if (underlay) {
                    id = underlayPalette[id];
                } else {
                    id = overlayPalette[id];
                }
                if (underlay) {
                    underlays[x + z * areaWidth] = (byte) id;
                    overlays[x + z * areaWidth] = 0;
                } else {
                    overlays[x + z * areaWidth] = (byte) id;
                    overlayShapes[x + z * areaWidth] = 0;
                    underlays[x + z * areaWidth] = packet.g1b();
                }
            }
            return;
        }

        @Pc(100) int levels = (data >> 1 & 0x3) + 1;
        @Pc(108) boolean hasOverlay = (data & 0x8) != 0;
        @Pc(116) boolean hasLocs = (data & 0x10) != 0;
        for (@Pc(118) int level = 0; level < levels; level++) {
            @Pc(123) int underlayId = packet.g1();
            @Pc(125) int overlayId = 0;
            @Pc(127) int shape = 0;
            if (hasOverlay) {
                overlayId = packet.g1();
                shape = packet.g1();
            }
            @Pc(139) int locCount = 0;
            if (hasLocs) {
                locCount = packet.g1();
            }
            @Pc(215) short[] levelLocIds;
            @Pc(218) byte[] levelLocShapes;
            @Pc(220) int i;
            if (level == 0) {
                underlays[x + z * areaWidth] = (byte) underlayId;
                overlays[x + z * areaWidth] = (byte) overlayId;
                overlayShapes[x + z * areaWidth] = (byte) shape;
                if (locCount == 1) {
                    tileLocIds[x + z * areaWidth] = (short) (packet.g2() + 1);
                    tileLocShapes[x + z * areaWidth] = packet.g1b();
                } else if (locCount > 1) {
                    tileLocIds[x + z * areaWidth] = -1;
                    levelLocIds = new short[locCount];
                    levelLocShapes = new byte[locCount];
                    for (i = 0; i < locCount; i++) {
                        levelLocIds[i] = (short) packet.g2();
                        levelLocShapes[i] = packet.g1b();
                    }
                    multiLocs.put(x << 16 | z, new Node_Sub23(levelLocIds, levelLocShapes));
                }
            } else {
                levelLocIds = null;
                levelLocShapes = null;
                if (locCount > 0) {
                    levelLocIds = new short[locCount];
                    levelLocShapes = new byte[locCount];
                    for (i = 0; i < locCount; i++) {
                        levelLocIds[i] = (short) packet.g2();
                        levelLocShapes[i] = packet.g1b();
                    }
                }
                if (tiles[level - 1][chunkX - (areaX >> 6)][chunkZ - (areaZ >> 6)] == null) {
                    tiles[level - 1][chunkX - (areaX >> 6)][chunkZ - (areaZ >> 6)] = new LinkedList();
                }
                @Pc(338) WorldMapTile tile = new WorldMapTile(x & 0x3F, z & 0x3F, underlayId, overlayId, shape, levelLocIds, levelLocShapes);
                tiles[level - 1][chunkX - (areaX >> 6)][chunkZ - (areaZ >> 6)].add(tile);
            }
        }
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "()V")
    public static void loadLocElements() {
        @Pc(4) int z;
        @Pc(15) int locId;
        @Pc(49) int mapelement;
        for (@Pc(1) int x = 0; x < areaWidth; x++) {
            for (z = 0; z < areaHeight; z++) {
                locId = tileLocIds[x + z * areaWidth] & 0xFFFF;
                if (locId != 0) {
                    @Pc(35) int locIndex;
                    if (locId == 65535) {
                        @Pc(31) Node_Sub23 locList = (Node_Sub23) multiLocs.get(x << 16 | z);
                        if (locList != null) {
                            for (locIndex = 0; locIndex < locList.aShortArray59.length; locIndex++) {
                                @Pc(46) LocType locType = locTypeList.list(locList.aShortArray59[locIndex] & 0xFFFF);
                                mapelement = locType.mapelement;
                                if (locType.multiloc != null) {
                                    locType = locType.getMultiLoc(varDomain);
                                    if (locType != null) {
                                        mapelement = locType.mapelement;
                                    }
                                }
                                if (mapelement != -1) {
                                    @Pc(70) MapElementListEntry element = new MapElementListEntry(mapelement);
                                    element.x = x;
                                    element.z = z;
                                    elements.addLast(element);
                                }
                            }
                        }
                    } else {
                        @Pc(94) LocType locType = locTypeList.list(locId - 1);
                        int elementId = locType.mapelement;
                        if (locType.multiloc != null) {
                            locType = locType.getMultiLoc(varDomain);
                            if (locType != null) {
                                elementId = locType.mapelement;
                            }
                        }
                        if (elementId != -1) {
                            @Pc(118) MapElementListEntry element = new MapElementListEntry(elementId);
                            element.x = x;
                            element.z = z;
                            elements.addLast(element);
                        }
                    }
                }
            }
        }
        for (int level = 0; level < 3; level++) {
            for (int chunkX = 0; chunkX < tiles[0].length; chunkX++) {
                for (@Pc(144) int chunkZ = 0; chunkZ < tiles[0][0].length; chunkZ++) {
                    @Pc(153) LinkedList chunkTiles = tiles[level][chunkX][chunkZ];
                    if (chunkTiles != null) {
                        for (@Pc(160) WorldMapTile tile = (WorldMapTile) chunkTiles.first(); tile != null; tile = (WorldMapTile) chunkTiles.next()) {
                            if (tile.aShortArray133 != null) {
                                for (int locSlot = 0; locSlot < tile.aShortArray133.length; locSlot++) {
                                    @Pc(177) LocType locType = locTypeList.list(tile.aShortArray133[locSlot] & 0xFFFF);
                                    @Pc(180) int elementId = locType.mapelement;
                                    if (locType.multiloc != null) {
                                        locType = locType.getMultiLoc(varDomain);
                                        if (locType != null) {
                                            elementId = locType.mapelement;
                                        }
                                    }
                                    if (elementId != -1) {
                                        @Pc(201) MapElementListEntry element = new MapElementListEntry(elementId);
                                        element.x = (chunkX + (areaX >> 6)) * 64 + tile.aByte138 - areaX;
                                        element.z = (chunkZ + (areaZ >> 6)) * 64 + tile.aByte139 - areaZ;
                                        elements.addLast(element);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!baa", name = "b", descriptor = "(II)Lclient!jga;")
    public static Queue findAreas(@OriginalArg(0) int x, @OriginalArg(1) int z) {
        @Pc(3) Queue queue = new Queue();
        for (@Pc(8) WorldMapArea area = (WorldMapArea) areas.first(); area != null; area = (WorldMapArea) areas.next()) {
            if (area.aBoolean354 && area.sourceContains(x, z)) {
                queue.add(area);
            }
        }
        return queue;
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(II)Lclient!ip;")
    public static WorldMapArea getMap(@OriginalArg(0) int x, @OriginalArg(1) int z) {
        for (@Pc(4) WorldMapArea area = (WorldMapArea) areas.first(); area != null; area = (WorldMapArea) areas.next()) {
            if (area.aBoolean354 && area.sourceContains(x, z)) {
                return area;
            }
        }
        return null;
    }

    @OriginalMember(owner = "client!baa", name = "e", descriptor = "()V")
    public static void loadStaticElements() {
        @Pc(2) int[] coord = new int[3];

        for (@Pc(4) int i = 0; i < staticElements.size; i++) {
            @Pc(32) boolean projected = area.projectDisplay(coord, (staticElements.coords[i] >> 28) & 0x3, (staticElements.coords[i] >> 14) & 0x3FFF, staticElements.coords[i] & 0x3FFF);

            if (projected) {
                @Pc(42) MapElementListEntry entry = new MapElementListEntry(staticElements.elements[i]);
                entry.x = coord[1] - areaX;
                entry.z = coord[2] - areaZ;
                elements.addLast(entry);
            }
        }
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;II)V")
    public static void decodeArea(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int hueOffset, @OriginalArg(2) int lightnessOffset) {
        @Pc(11) Packet packet = new Packet(data.getfile(area.file, "area"));
        @Pc(15) int underlayCount = packet.g1();
        @Pc(18) int[] underlayPalette = new int[underlayCount];
        for (@Pc(20) int i = 0; i < underlayCount; i++) {
            underlayPalette[i] = packet.g1();
        }
        @Pc(35) int overlayCount = packet.g1();
        @Pc(38) int[] overlayPalette = new int[overlayCount];
        for (@Pc(40) int i = 0; i < overlayCount; i++) {
            overlayPalette[i] = packet.g1();
        }
        @Pc(60) int chunkX;
        @Pc(66) int offsetX;
        @Pc(69) int offsetZ;
        @Pc(78) int tileX;
        @Pc(150) int subTileZ;
        while (packet.pos < packet.data.length) {
            @Pc(64) int chunkZ;
            @Pc(86) int tileZ;
            if (packet.g1() == 0) {
                chunkX = packet.g1();
                chunkZ = packet.g1();
                for (offsetX = 0; offsetX < 64; offsetX++) {
                    for (offsetZ = 0; offsetZ < 64; offsetZ++) {
                        tileX = chunkX * 64 + offsetX - areaX;
                        tileZ = chunkZ * 64 + offsetZ - areaZ;
                        decodeTile(toolkit, packet, chunkX, chunkZ, tileX, tileZ, underlayPalette, overlayPalette);
                    }
                }
            } else {
                chunkX = packet.g1();
                chunkZ = packet.g1();
                offsetX = packet.g1();
                offsetZ = packet.g1();
                for (int blockX = 0; blockX < 8; blockX++) {
                    for (int blockZ = 0; blockZ < 8; blockZ++) {
                        @Pc(138) int subTileX = chunkX * 64 + offsetX * 8 + blockX - areaX;
                        subTileZ = chunkZ * 64 + offsetZ * 8 + blockZ - areaZ;
                        decodeTile(toolkit, packet, chunkX, chunkZ, subTileX, subTileZ, underlayPalette, overlayPalette);
                    }
                }
            }
        }
        underlayColoursHigh = new byte[areaWidth * areaHeight];
        underlayColoursLow = new short[areaWidth * areaHeight];
        for (int level = 0; level < 3; level++) {
            @Pc(193) byte[] levelUnderlays = new byte[areaWidth * areaHeight];
            for (int chunkIndexX = 0; chunkIndexX < tiles[level].length; chunkIndexX++) {
                for (int chunkIndexZ = 0; chunkIndexZ < tiles[level][0].length; chunkIndexZ++) {
                    @Pc(207) LinkedList chunkTiles = tiles[level][chunkIndexX][chunkIndexZ];
                    if (chunkTiles != null) {
                        for (@Pc(214) WorldMapTile tile = (WorldMapTile) chunkTiles.first(); tile != null; tile = (WorldMapTile) chunkTiles.next()) {
                            levelUnderlays[chunkIndexX * 64 + tile.aByte138 + (chunkIndexZ * 64 + tile.aByte139) * areaWidth] = (byte) tile.anInt9770;
                        }
                    }
                }
            }
            blendUnderlays(levelUnderlays, underlayColoursHigh, underlayColoursLow, hueOffset, lightnessOffset);
            for (int chunkIndexX = 0; chunkIndexX < tiles[level].length; chunkIndexX++) {
                for (int chunkIndexZ = 0; chunkIndexZ < tiles[level][0].length; chunkIndexZ++) {
                    @Pc(278) LinkedList chunkTiles = tiles[level][chunkIndexX][chunkIndexZ];
                    if (chunkTiles != null) {
                        for (@Pc(285) WorldMapTile tile = (WorldMapTile) chunkTiles.first(); tile != null; tile = (WorldMapTile) chunkTiles.next()) {
                            int index = chunkIndexX * 64 + tile.aByte138 + (chunkIndexZ * 64 + tile.aByte139) * areaWidth;
                            tile.anInt9770 = (underlayColoursHigh[index] & 0xFF) << 16 | underlayColoursLow[index] & 0xFFFF;
                            if (tile.anInt9770 != 0) {
                                tile.anInt9770 |= 0xFF000000;
                            }
                        }
                    }
                }
            }
        }
        blendUnderlays(underlays, underlayColoursHigh, underlayColoursLow, hueOffset, lightnessOffset);
        underlays = null;
        loadLocElements();
    }

    @OriginalMember(owner = "client!baa", name = "b", descriptor = "(Lclient!ha;)Lclient!sia;")
    public static Deque positionElements(@OriginalArg(0) Toolkit toolkit) {
        @Pc(3) int viewWidth = viewX2 - viewX1;
        @Pc(7) int viewHeight = viewZ2 - viewZ1;
        @Pc(15) int scaleX = (screenX2 - screenX1 << 16) / viewWidth;
        @Pc(23) int scaleZ = (screenY2 - screenY1 << 16) / viewHeight;
        return positionElements(toolkit, scaleX, scaleZ);
    }

    @OriginalMember(owner = "client!baa", name = "b", descriptor = "(Lclient!ha;IIII)Lclient!sia;")
    public static Deque positionElements(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int scaleX, @OriginalArg(2) int scaleZ) {
        for (@Pc(4) MapElementListEntry entry = (MapElementListEntry) elements.first(); entry != null; entry = (MapElementListEntry) elements.next()) {
            positionElement(toolkit, entry, scaleX, scaleZ);
        }
        return elements;
    }

    @OriginalMember(owner = "client!aaa", name = "b", descriptor = "(II)V")
    public static void setZoomPercentage(@OriginalArg(1) int percentage) {
        if (percentage == 37) {
            targetZoom = 3.0F;
        } else if (percentage == 50) {
            targetZoom = 4.0F;
        } else if (percentage == 75) {
            targetZoom = 6.0F;
        } else if (percentage == 100) {
            targetZoom = 8.0F;
        } else if (percentage == 200) {
            targetZoom = 16.0F;
        }

        jumpZ = -1;
        jumpZ = -1;
    }

    @OriginalMember(owner = "client!om", name = "a", descriptor = "(Z)I")
    public static int getZoom() {
        if ((double) targetZoom == 3.0D) {
            return 37;
        } else if ((double) targetZoom == 4.0D) {
            return 50;
        } else if ((double) targetZoom == 6.0D) {
            return 75;
        } else if ((double) targetZoom == 8.0D) {
            return 100;
        } else {
            return 200;
        }
    }

    @OriginalMember(owner = "client!bw", name = "a", descriptor = "(IZIII)V")
    public static void setMap(@OriginalArg(0) int id, @OriginalArg(1) boolean override, @OriginalArg(2) int z, @OriginalArg(3) int x) {
        if (ClientOptions.instance.toolkit.getValue() == ToolkitType.JAVA) {
            reset(false);
        } else {
            toolkitType = ClientOptions.instance.toolkit.getValue();
            Static32.setToolkit(ToolkitType.JAVA, true);
        }
        mapOverride = override;
        mapZ = z;
        mapX = x;
        setArea(id);
    }

    private WorldMap() {
        /* empty */
    }

    @OriginalMember(owner = "client!vca", name = "a", descriptor = "(ZI)V")
    public static void reset(@OriginalArg(0) boolean saveArea) {
        if (saveArea && area != null) {
            lastAreaId = area.id;
        } else {
            lastAreaId = -1;
        }

        boundedEntries = null;
        component = null;
        area = null;
        loadingPercent = 0;
        freeBuffers();
        elements.clear();
        font30 = null;
        font12 = null;
        staticElements = null;
        font17 = null;
        jumpX = -1;
        overviewSprite = null;
        font14 = null;
        font22 = null;
        font11 = null;
        font26 = null;
        font19 = null;
        jumpZ = -1;
        if (mapElementTypeList != null) {
            mapElementTypeList.cacheReset();
            mapElementTypeList.setCaches(128, 64);
        }
        if (msiTypeList != null) {
            msiTypeList.setCache(64, 64);
        }
        if (locTypeList != null) {
            locTypeList.setRecentUse(64);
        }
        VarBitTypeListClient.instance.cacheReset(64);
    }

    @OriginalMember(owner = "client!tc", name = "e", descriptor = "(I)V")
    public static void method7934() {
        if (lastAreaId != -1) {
            setMap(lastAreaId, false, -1, -1);
            lastAreaId = -1;
        }
    }

    @OriginalMember(owner = "client!vd", name = "a", descriptor = "(II)V")
    public static void method8711(@OriginalArg(0) int x) {
        jumpX = -1;
        jumpZ = -1;
        displayX = x;
        checkJump();
    }

    @OriginalMember(owner = "client!dq", name = "b", descriptor = "(B)Lclient!fu;")
    public static MapElementListEntry startElement() {
        if (elements == null || elementIterator == null) {
            return null;
        }

        elementIterator.setDeque(elements);

        @Pc(23) MapElementListEntry entry = (MapElementListEntry) elementIterator.first();
        if (entry == null) {
            return null;
        } else {
            @Pc(42) MapElementType type = mapElementTypeList.list(entry.id);
            return type != null && type.aBoolean217 && type.variableTest(varDomain) ? entry : nextElement();
        }
    }

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "(Z)Lclient!fu;")
    public static MapElementListEntry nextElement() {
        if (elements == null || elementIterator == null) {
            return null;
        }

        for (@Pc(17) MapElementListEntry entry = (MapElementListEntry) elementIterator.next(); entry != null; entry = (MapElementListEntry) elementIterator.next()) {
            @Pc(30) MapElementType type = mapElementTypeList.list(entry.id);

            if (type != null && type.aBoolean217 && type.variableTest(varDomain)) {
                return entry;
            }
        }

        return null;
    }

    @OriginalMember(owner = "client!dfa", name = "a", descriptor = "(ZII)V")
    public static void jumpToDisplayCoord(@OriginalArg(1) int x, @OriginalArg(2) int z) {
        jumpX = x - areaX;
        jumpZ = z - areaZ;
    }

    @OriginalMember(owner = "client!vp", name = "a", descriptor = "(BI)V")
    public static void flashElement(@OriginalArg(1) int element) {
        flashingElement = element;
        flashCycles = 3;
        flashingElementCategory = -1;
        flashTimer = 100;
    }

    @OriginalMember(owner = "client!fea", name = "a", descriptor = "(II)V")
    public static void flashElementCategory(@OriginalArg(0) int category) {
        flashTimer = 100;
        flashingElementCategory = category;
        flashCycles = 3;
        flashingElement = -1;
    }

    @OriginalMember(owner = "client!ms", name = "a", descriptor = "(ZLclient!el;)Z")
    public static boolean isEnabled(@OriginalArg(1) MapElementType type) {
        if (type == null) {
            return false;
        } else if (!type.enabled) {
            return false;
        } else if (!type.variableTest(varDomain)) {
            return false;
        } else if (disabledElements.get(type.id) != null) {
            return false;
        } else if (disabledElementCategories.get(type.category) != null) {
            return false;
        } else {
            return true;
        }
    }

    @OriginalMember(owner = "client!wu", name = "e", descriptor = "(I)V")
    public static void resetDisabledElements() {
        disabledElements.clear();
        disabledElementCategories.clear();
    }

    @OriginalMember(owner = "client!vo", name = "a", descriptor = "(IIII)I")
    public static int findNearestElement(@OriginalArg(0) int id, @OriginalArg(1) int z, @OriginalArg(3) int x) {
        if (loadingPercent < 100) {
            return -2;
        }

        @Pc(13) int bestCoord = -2;
        @Pc(15) int bestDistance = Integer.MAX_VALUE;

        @Pc(19) int x1 = x - areaX;
        @Pc(23) int z1 = z - areaZ;

        for (@Pc(34) MapElementListEntry entry = (MapElementListEntry) elements.first(); entry != null; entry = (MapElementListEntry) elements.next()) {
            if (entry.id == id) {
                @Pc(46) int x2 = entry.x;
                @Pc(49) int z2 = entry.z;

                @Pc(59) int coord = ((areaX + x2) << 14) | (areaZ + z2);
                @Pc(78) int distance = (z1 - z2) * (z1 - z2) + (x1 - x2) * (x1 - x2);

                if (bestCoord < 0 || distance < bestDistance) {
                    bestDistance = distance;
                    bestCoord = coord;
                }
            }
        }

        return bestCoord;
    }

    @OriginalMember(owner = "client!jj", name = "a", descriptor = "(I)V")
    public static void resetoreToolkit() {
        reset(false);

        if (toolkitType >= 0 && toolkitType != 0) {
            Static32.setToolkit(toolkitType, false);
            toolkitType = -1;
        }
    }

    @OriginalMember(owner = "client!kd", name = "a", descriptor = "(Z)V")
    public static void close() {
        MainLogicManager.setStep(MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD);
        resetoreToolkit();
        System.gc();
    }

    @OriginalMember(owner = "client!laa", name = "a", descriptor = "(Lclient!ha;ILclient!el;ILclient!fu;I)Z")
    public static boolean renderElement(@OriginalArg(0) Toolkit toolkit, @OriginalArg(2) MapElementType element, @OriginalArg(4) MapElementListEntry entry) {
        @Pc(7) int minRectX = Integer.MAX_VALUE;
        @Pc(9) int maxRectX = Integer.MIN_VALUE;
        @Pc(11) int minRectY = Integer.MAX_VALUE;
        @Pc(13) int maxRectY = Integer.MIN_VALUE;
        if (element.landmarkPolygons != null) {
            maxRectY = screenY2 - (entry.z + element.maxZ - viewZ1) * (screenY2 - screenY1) / (viewZ2 - viewZ1);
            minRectY = screenY2 - (entry.z + element.minZ - viewZ1) * (screenY2 + -screenY1) / (viewZ2 - viewZ1);
            minRectX = screenX1 + (screenX2 - screenX1) * (-viewX1 + element.maxX - -entry.x) / (viewX2 - viewX1);
            maxRectX = screenX1 + (element.minX + entry.x - viewX1) * (screenX2 - screenX1) / (viewX2 - viewX1);
        }

        @Pc(102) Sprite sprite = null;
        @Pc(104) int textX1 = 0;
        @Pc(106) int textX2 = 0;
        @Pc(108) int textY2 = 0;
        @Pc(110) int textY1 = 0;
        if (element.sprite != -1) {
            if (entry.mouseOver && element.hoverSprite != -1) {
                sprite = element.sprite(toolkit, true);
            } else {
                sprite = element.sprite(toolkit, false);
            }

            if (sprite != null) {
                textX1 = entry.spriteX - (sprite.scaleWidth() + 1 >> 1);
                textX2 = entry.spriteX + (sprite.scaleWidth() + 1 >> 1);
                if (minRectX > textX1) {
                    minRectX = textX1;
                }
                if (textX2 > maxRectX) {
                    maxRectX = textX2;
                }

                textY2 = entry.spriteY - (sprite.scaleHeight() + 1 >> 1);
                if (minRectY > textY2) {
                    minRectY = textY2;
                }

                textY1 = entry.spriteY + (sprite.scaleHeight() + 1 >> 1);
                if (textY1 > maxRectY) {
                    maxRectY = textY1;
                }
            }
        }

        @Pc(209) WorldMapFont font = null;
        @Pc(211) int lineCount = 0;
        @Pc(213) int textX = 0;
        @Pc(215) int textY = 0;
        @Pc(217) int maxLineWidth = 0;
        @Pc(227) int rectX1 = 0;
        @Pc(229) int rectX2 = 0;
        @Pc(231) int rectY1 = 0;
        @Pc(233) int rectY2 = 0;
        if (element.text != null) {
            font = getFont(element.textSize);

            if (font != null) {
                lineCount = Fonts.p11Metrics.splitLines(mapElementTextLines, null, null, element.text);
                textY = entry.spriteY - element.anInt2617 * (screenY2 - screenY1) / (viewZ2 - viewZ1);
                textX = element.anInt2600 * (screenX2 - screenX1) / (viewX2 - viewX1) + entry.spriteX;

                if (sprite != null) {
                    textY -= (sprite.scaleHeight() >> 1) + (font.getHeight() * lineCount);
                } else {
                    textY -= (lineCount * font.getWidth()) / 2;
                }

                for (@Pc(312) int i = 0; i < lineCount; i++) {
                    @Pc(318) String line = mapElementTextLines[i];
                    if (i < lineCount - 1) {
                        line = line.substring(0, line.length() - 4);
                    }

                    @Pc(335) int lineWidth = font.totalWidth(line);
                    if (lineWidth > maxLineWidth) {
                        maxLineWidth = lineWidth;
                    }
                }

                rectX1 = textX - maxLineWidth / 2;
                rectX2 = maxLineWidth / 2 + textX;
                if (rectX1 < minRectX) {
                    minRectX = rectX1;
                }
                if (rectX2 > maxRectX) {
                    maxRectX = rectX2;
                }

                rectY1 = textY;
                rectY2 = textY + lineCount * font.getHeight();
                if (rectY1 < minRectY) {
                    minRectY = rectY1;
                }
                if (rectY2 > maxRectY) {
                    maxRectY = rectY2;
                }
            }
        }

        if (screenX1 <= maxRectX && screenX2 >= minRectX && screenY1 <= maxRectY && screenY2 >= minRectY) {
            drawLandmark(toolkit, entry, element);

            if (sprite != null) {
                if ((flashCycles > 0) && ((flashingElement != -1 && entry.id == flashingElement) || (flashingElementCategory != -1 && element.category == flashingElementCategory))) {
                    @Pc(312) int alpha;
                    if (flashTimer > 50) {
                        alpha = 200 - flashTimer * 2;
                    } else {
                        alpha = flashTimer * 2;
                    }

                    @Pc(495) int colour = (alpha << 24) | 0xFFFF00;
                    toolkit.fillCircle(entry.spriteX, entry.spriteY, sprite.getWidth() / 2 + 7, colour);
                    toolkit.fillCircle(entry.spriteX, entry.spriteY, sprite.getWidth() / 2 + 5, colour);
                    toolkit.fillCircle(entry.spriteX, entry.spriteY, sprite.getWidth() / 2 + 3, colour);
                    toolkit.fillCircle(entry.spriteX, entry.spriteY, sprite.getWidth() / 2 + 1, colour);
                    toolkit.fillCircle(entry.spriteX, entry.spriteY, sprite.getWidth() / 2, colour);
                }

                sprite.render(entry.spriteX - (sprite.scaleWidth() >> 1), entry.spriteY - (sprite.scaleHeight() >> 1));
            }

            if (element.text != null && font != null) {
                renderText(toolkit, element, entry, font, textX, textY, maxLineWidth, lineCount);
            }

            if (element.sprite != -1 || element.text != null) {
                @Pc(612) BoundedMapElementListEntry boundedEntry = new BoundedMapElementListEntry(entry);
                boundedEntry.rectX2 = rectX2;
                boundedEntry.textY2 = textY2;
                boundedEntry.rectX1 = rectX1;
                boundedEntry.textX2 = textX2;
                boundedEntry.textY1 = textY1;
                boundedEntry.rectY1 = rectY1;
                boundedEntry.rectY2 = rectY2;
                boundedEntry.textX1 = textX1;
                boundedEntries.addLast(boundedEntry);
            }

            return false;
        } else {
            return true;
        }
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(BLclient!fu;Lclient!ha;Lclient!el;)V")
    public static void renderElementOffscreen(@OriginalArg(2) Toolkit toolkit, @OriginalArg(3) MapElementType element, @OriginalArg(1) MapElementListEntry entry) {
        @Pc(8) Sprite sprite = element.worldMapSprite(toolkit);
        if (sprite == null) {
            return;
        }

        @Pc(15) int size = sprite.getWidth();
        if (sprite.getHeight() > size) {
            size = sprite.getHeight();
        }

        @Pc(31) int textX1 = entry.spriteX;
        @Pc(34) int textY1 = entry.spriteY;
        @Pc(36) int lineCount = 0;
        @Pc(38) int maxTotalWidth = 0;
        @Pc(40) int textHeight = 0;

        if (element.text != null) {
            lineCount = Fonts.p11Metrics.splitLines(mapElementTextLines, null, null, element.text);

            for (@Pc(56) int i = 0; i < lineCount; i++) {
                @Pc(61) String line = mapElementTextLines[i];
                if (i < lineCount - 1) {
                    line = line.substring(0, line.length() - 4);
                }

                @Pc(78) int totalWidth = font14.totalWidth(line);
                if (totalWidth > maxTotalWidth) {
                    maxTotalWidth = totalWidth;
                }
            }

            textHeight = font14.getHeight() * lineCount + font14.getWidth() / 2;
        }

        @Pc(56) int centerX = entry.spriteX + size / 2;
        if (textX1 < screenX1 + size) {
            centerX = size / 2 + screenX1 + maxTotalWidth / 2 + 15;
            textX1 = screenX1;
        } else if (screenX2 - size < textX1) {
            textX1 = screenX2 - size;
            centerX = screenX2 - size / 2 - maxTotalWidth / 2 - 10 - 5;
        }

        @Pc(163) int centerY = entry.spriteY;
        if (size + screenY1 > textY1) {
            centerY = screenY1 + size / 2 + 10;
            textY1 = screenY1;
        } else if (screenY2 - size < textY1) {
            centerY = screenY2 - size / 2 - textHeight - 10;
            textY1 = screenY2 - size;
        }

        @Pc(78) int angle = (int) (Math.atan2(textX1 - entry.spriteX, textY1 - entry.spriteY) / 3.141592653589793D * 32767.0D) & 0xFFFF;
        sprite.renderRotated((float) textX1 + (float) size / 2.0F, (float) textY1 + (float) size / 2.0F, 4096, angle);

        @Pc(246) int rectX1 = -2;
        @Pc(248) int rectY1 = -2;
        @Pc(257) int rectX2 = -2;
        @Pc(259) int rectY2 = -2;
        if (element.text != null) {
            rectX1 = centerX - maxTotalWidth / 2 - 5;
            rectY1 = centerY;
            rectX2 = maxTotalWidth + rectX1 + 10;
            rectY2 = font14.getHeight() * lineCount + centerY + 3;

            if (element.fillColour != 0) {
                toolkit.fillRect(rectX1, centerY, rectX2 - rectX1, rectY2 - centerY, element.fillColour);
            }
            if (element.outlineColour != 0) {
                toolkit.outlineRect(rectX1, centerY, rectX2 - rectX1, rectY2 - centerY, element.outlineColour);
            }

            for (@Pc(333) int i = 0; i < lineCount; i++) {
                @Pc(338) String line = mapElementTextLines[i];

                if (lineCount - 1 > i) {
                    line = line.substring(0, line.length() - 4);
                }

                font14.renderCenter(toolkit, line, centerX, centerY, element.textColour);
                centerY += font14.getHeight();
            }
        }

        if (element.sprite != -1 || element.text != null) {
            size >>= 0x1;
            @Pc(393) BoundedMapElementListEntry boundedEntry = new BoundedMapElementListEntry(entry);
            boundedEntry.textX2 = textX1 + size;
            boundedEntry.rectY2 = rectY2;
            boundedEntry.rectX1 = rectX1;
            boundedEntry.textX1 = textX1 - size;
            boundedEntry.textY1 = textY1 + size;
            boundedEntry.textY2 = textY1 - size;
            boundedEntry.rectY1 = rectY1;
            boundedEntry.rectX2 = rectX2;
            boundedEntries.addLast(boundedEntry);
        }
    }

    @OriginalMember(owner = "client!qa", name = "a", descriptor = "(BII)V")
    public static void method6759(@OriginalArg(1) int x, @OriginalArg(2) int y) {
        if (currentZoom < targetZoom) {
            currentZoom = (float) ((double) currentZoom + (double) currentZoom / 30.0D);
            if (currentZoom > targetZoom) {
                currentZoom = targetZoom;
            }

            checkJump();
            tileSize = (int) currentZoom >> 1;
            tileShapes = Static640.createTileShapeMasks(tileSize);
        } else if (currentZoom > targetZoom) {
            currentZoom = (float) ((double) currentZoom - (double) currentZoom / 30.0D);
            if (currentZoom < targetZoom) {
                currentZoom = targetZoom;
            }

            checkJump();
            tileSize = (int) currentZoom >> 1;
            tileShapes = Static640.createTileShapeMasks(tileSize);
        }

        if (jumpX != -1 && jumpZ != -1) {
            @Pc(101) int deltaX = jumpX - displayX;
            if (deltaX < 2 || deltaX > 2) {
                deltaX /= 8;
            }

            @Pc(120) int deltaZ = jumpZ - displayZ;
            displayX += deltaX;
            if (deltaZ < 2 || deltaZ > 2) {
                deltaZ /= 8;
            }

            if (deltaX == 0 && deltaZ == 0) {
                jumpZ = -1;
                jumpX = -1;
            }

            displayZ -= -deltaZ;
            checkJump();
        }

        if (flashCycles > 0) {
            flashTimer--;
            if (flashTimer == 0) {
                flashCycles--;
                flashTimer = 100;
            }
        } else {
            flashingElement = -1;
            flashingElementCategory = -1;
        }

        if (hovered && boundedEntries != null) {
            for (@Pc(197) BoundedMapElementListEntry entry = (BoundedMapElementListEntry) boundedEntries.first(); entry != null; entry = (BoundedMapElementListEntry) boundedEntries.next()) {
                @Pc(206) MapElementType elementType = mapElementTypeList.list(entry.entry.id);

                if (entry.contains(x, y)) {
                    if (elementType.ops != null) {
                        if (elementType.ops[4] != null) {
                            MiniMenu.addEntryInner(false, -1, entry.entry.id, elementType.category, 0, elementType.ops[4], MiniMenuAction.OP_MAPELEMENT5, true, -1, elementType.opBase, entry.entry.id, false);
                        }
                        if (elementType.ops[3] != null) {
                            MiniMenu.addEntryInner(false, -1, entry.entry.id, elementType.category, 0, elementType.ops[3], MiniMenuAction.OP_MAPELEMENT4, true, -1, elementType.opBase, entry.entry.id, false);
                        }
                        if (elementType.ops[2] != null) {
                            MiniMenu.addEntryInner(false, -1, entry.entry.id, elementType.category, 0, elementType.ops[2], MiniMenuAction.OP_MAPELEMENT3, true, -1, elementType.opBase, entry.entry.id, false);
                        }
                        if (elementType.ops[1] != null) {
                            MiniMenu.addEntryInner(false, -1, entry.entry.id, elementType.category, 0, elementType.ops[1], MiniMenuAction.OP_MAPELEMENT2, true, -1, elementType.opBase, entry.entry.id, false);
                        }
                        if (elementType.ops[0] != null) {
                            MiniMenu.addEntryInner(false, -1, entry.entry.id, elementType.category, 0, elementType.ops[0], MiniMenuAction.OP_MAPELEMENT1, true, -1, elementType.opBase, entry.entry.id, false);
                        }
                    }

                    if (!entry.entry.mouseOver) {
                        entry.entry.mouseOver = true;
                        ScriptRunner.executeTrigger(ClientTriggerType.MAP_ELEMENT_MOUSEOVER, entry.entry.id, elementType.category);
                    }

                    if (entry.entry.mouseOver) {
                        ScriptRunner.executeTrigger(ClientTriggerType.MAP_ELEMENT_MOUSEREPEAT, entry.entry.id, elementType.category);
                    }
                } else if (entry.entry.mouseOver) {
                    entry.entry.mouseOver = false;
                    ScriptRunner.executeTrigger(ClientTriggerType.MAP_ELEMENT_MOUSELEAVE, entry.entry.id, elementType.category);
                }
            }
        }
    }

    @OriginalMember(owner = "client!mda", name = "a", descriptor = "(ZI)Lclient!rt;")
    public static WorldMapFont getFont(@OriginalArg(1) int textSize) {
        if (textSize == 0) {
            if ((double) currentZoom == 3.0D) {
                return font11;
            }
            if ((double) currentZoom == 4.0D) {
                return font12;
            }
            if ((double) currentZoom == 6.0D) {
                return font14;
            }
            if ((double) currentZoom >= 8.0D) {
                return font17;
            }
        } else if (textSize == 1) {
            if ((double) currentZoom == 3.0D) {
                return font14;
            }
            if ((double) currentZoom == 4.0D) {
                return font17;
            }
            if ((double) currentZoom == 6.0D) {
                return font19;
            }
            if ((double) currentZoom >= 8.0D) {
                return font22;
            }
        } else if (textSize == 2) {
            if ((double) currentZoom == 3.0D) {
                return font19;
            }
            if ((double) currentZoom == 4.0D) {
                return font22;
            }
            if ((double) currentZoom == 6.0D) {
                return font26;
            }
            if ((double) currentZoom >= 8.0D) {
                return font30;
            }
        }
        return null;
    }

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(ILclient!fu;ILclient!el;Lclient!rt;IBLclient!ha;I)V")
    public static void renderText(@OriginalArg(7) Toolkit toolkit, @OriginalArg(3) MapElementType type, @OriginalArg(1) MapElementListEntry entry, @OriginalArg(4) WorldMapFont font, @OriginalArg(2) int x, @OriginalArg(8) int y, @OriginalArg(5) int width, @OriginalArg(0) int lineCount) {
        @Pc(14) int boxX = x - (width / 2) - 5;
        @Pc(18) int boxY = y + 2;
        if (type.fillColour != 0) {
            toolkit.fillRect(boxX, boxY, width + 10, lineCount * font.getHeight() + 1 + y + -boxY, type.fillColour);
        }
        if (type.outlineColour != 0) {
            toolkit.outlineRect(boxX, boxY, width + 10, font.getHeight() * lineCount + 1 + y + -boxY, type.outlineColour);
        }

        @Pc(73) int textColour = type.textColour;
        if (entry.mouseOver && type.hoverTextColour != -1) {
            textColour = type.hoverTextColour;
        }

        for (@Pc(87) int i = 0; i < lineCount; i++) {
            @Pc(93) String line = mapElementTextLines[i];
            if (i < lineCount - 1) {
                line = line.substring(0, line.length() - 4);
            }

            font.renderCenter(toolkit, line, x, y, textColour);
            y += font.getHeight();
        }
    }

    @OriginalMember(owner = "client!lka", name = "a", descriptor = "(ILclient!sia;ILclient!ha;I)V")
    public static void renderElements(@OriginalArg(1) Deque entries, @OriginalArg(3) Toolkit toolkit) {
        boundedEntries.clear();

        if (disableElements) {
            return;
        }

        for (@Pc(27) MapElementListEntry entry = (MapElementListEntry) entries.first(); entry != null; entry = (MapElementListEntry) entries.next()) {
            @Pc(35) MapElementType type = mapElementTypeList.list(entry.id);

            if (isEnabled(type)) {
                @Pc(47) boolean offscreen = renderElement(toolkit, type, entry);

                if (offscreen) {
                    renderElementOffscreen(toolkit, type, entry);
                }
            }
        }
    }

    @OriginalMember(owner = "client!baa", name = "a", descriptor = "(Lclient!ha;IIII[S[B)V")
    public static void drawMsiMultiple(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int drawX, @OriginalArg(2) int drawY, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) short[] locIds, @OriginalArg(6) byte[] rotations) {
        if (locIds == null) {
            return;
        }

        for (@Pc(4) int i = 0; i < locIds.length; i++) {
            @Pc(14) LocType locType = locTypeList.list(locIds[i] & 0xFFFF);

            @Pc(17) int msi = locType.msi;
            if (msi == -1) {
                continue;
            }

            @Pc(25) MSIType msiType = msiTypeList.list(msi);
            @Pc(49) Sprite sprite = msiType.sprite(locType.msirotate ? ((rotations[i] >> 6) & 0x3) : 0, toolkit, locType.msiflip ? locType.mirror : false);

            if (sprite != null) {
                @Pc(58) int spriteWidth = (width * sprite.scaleWidth()) >> 2;
                @Pc(65) int spriteHeight = (height * sprite.scaleHeight()) >> 2;

                if (msiType.enlarge) {
                    @Pc(71) int locWidth = locType.width;
                    @Pc(74) int locLength = locType.length;

                    if ((rotations[i] >> 6 & 0x1) == 1) {
                        @Pc(85) int temp = locWidth;
                        locWidth = locLength;
                        locLength = temp;
                    }

                    spriteWidth = locWidth * width;
                    spriteHeight = locLength * height;
                }

                if (spriteWidth != 0 && spriteHeight != 0) {
                    if (msiType.colour != 0) {
                        sprite.render(drawX, drawY + height - spriteHeight, spriteWidth, spriteHeight, 0, msiType.colour | 0xFF000000, 1);
                    } else {
                        sprite.render(drawX, drawY + height - spriteHeight, spriteWidth, spriteHeight);
                    }
                }
            }
        }
    }
}

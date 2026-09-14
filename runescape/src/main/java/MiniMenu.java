import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.IndexedImage;
import com.jagex.PickableEntity;
import com.jagex.core.constants.MiniMenuAction;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.DequeIterator;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.Node2;
import com.jagex.core.datastruct.key.Queue;
import com.jagex.core.datastruct.key.QueueIterator;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.util.Arrays;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.LocalisedText;
import com.jagex.game.Location;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.iftype.ServerActiveProperties;
import com.jagex.game.runetek6.config.iftype.TargetMask;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.paramtype.ParamType;
import com.jagex.game.runetek6.config.paramtype.ParamTypeList;
import com.jagex.game.runetek6.config.questtype.QuestType;
import com.jagex.game.runetek6.config.questtype.QuestTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.Font;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.keyboard.KeyboardMonitor;
import rs2.client.event.keyboard.SimpleKeyboardMonitor;
import rs2.client.event.mouse.MouseLog;
import rs2.client.event.mouse.MouseMonitor;

import java.util.Random;

public final class MiniMenu {

    @OriginalMember(owner = "client!sw", name = "b", descriptor = "[Z")
    public static final boolean[] playerOpsReducedPriority = new boolean[8];

    @OriginalMember(owner = "client!eka", name = "h", descriptor = "[I")
    public static final int[] playerOpCursors = new int[8];

    @OriginalMember(owner = "client!pj", name = "f", descriptor = "Ljava/util/Random;")
    public static final Random random = new Random();

    private static final int QUEST_ICON_COUNT = 10;

    private static final int QUEST_ICON_HEIGHT = 12;

    private static final int SPRITE_TOP_HEIGHT = 33;

    private static final int TOP_HEIGHT = 31;

    private static final int ENTRY_HEIGHT = 16;

    @OriginalMember(owner = "client!gfa", name = "u", descriptor = "Lclient!sia;")
    public static final Deque otherInnerEntries = new Deque();

    @OriginalMember(owner = "client!hha", name = "b", descriptor = "Lclient!sia;")
    public static final Deque targetInnerEntries = new Deque();

    @OriginalMember(owner = "client!vu", name = "f", descriptor = "Lclient!sia;")
    public static final Deque innerEntryQueue = new Deque();

    @OriginalMember(owner = "client!pha", name = "m", descriptor = "Lclient!av;")
    public static final IterableHashTable entryTable = new IterableHashTable(16);

    @OriginalMember(owner = "client!wn", name = "k", descriptor = "Lclient!dla;")
    public static final ReferenceCache cache = new ReferenceCache(30);

    @OriginalMember(owner = "client!oea", name = "w", descriptor = "Lclient!dla;")
    public static final ReferenceCache questCache = new ReferenceCache(8);

    @OriginalMember(owner = "client!la", name = "v", descriptor = "Lclient!jga;")
    public static final Queue entryQueue = new Queue();

    @OriginalMember(owner = "client!fp", name = "T", descriptor = "Z")
    public static final boolean debugOps = false;

    @OriginalMember(owner = "client!jha", name = "k", descriptor = "[Ljava/lang/String;")
    public static final String[] playerOps = new String[8];

    @OriginalMember(owner = "client!mk", name = "c", descriptor = "Z")
    public static boolean open = false;

    @OriginalMember(owner = "client!sn", name = "j", descriptor = "I")
    public static int innerEntryCount = 0;

    @OriginalMember(owner = "client!bb", name = "c", descriptor = "I")
    public static int entryCount = 0;

    @OriginalMember(owner = "client!qja", name = "c", descriptor = "Lclient!pg;")
    public static MiniMenuEntryInner cancelEntry;

    @OriginalMember(owner = "client!da", name = "o", descriptor = "Lclient!pg;")
    public static MiniMenuEntryInner topEntry;

    @OriginalMember(owner = "client!or", name = "F", descriptor = "Lclient!pg;")
    public static MiniMenuEntryInner activeEntry;

    @OriginalMember(owner = "client!fo", name = "a", descriptor = "[Lclient!st;")
    public static Sprite[] icons;

    @OriginalMember(owner = "client!ki", name = "c", descriptor = "I")
    public static int nameIconsCount;

    @OriginalMember(owner = "client!oj", name = "j", descriptor = "[I")
    public static int[] iconHeights;

    @OriginalMember(owner = "client!vka", name = "c", descriptor = "I")
    public static int width;

    @OriginalMember(owner = "client!cea", name = "p", descriptor = "I")
    public static int x;

    @OriginalMember(owner = "client!mr", name = "b", descriptor = "I")
    public static int height;

    @OriginalMember(owner = "client!client", name = "wb", descriptor = "I")
    public static int y;

    @OriginalMember(owner = "client!eg", name = "j", descriptor = "Lclient!cba;")
    public static MiniMenuEntry openedEntry = null;

    @OriginalMember(owner = "client!pj", name = "p", descriptor = "I")
    public static int openedEntryY;

    @OriginalMember(owner = "client!cm", name = "o", descriptor = "I")
    public static int openedEntryWidth;

    @OriginalMember(owner = "client!vt", name = "c", descriptor = "I")
    public static int openedEntryX;

    @OriginalMember(owner = "client!as", name = "g", descriptor = "I")
    public static int openedEntryHeight;

    @OriginalMember(owner = "client!ik", name = "w", descriptor = "Z")
    public static boolean ignorePlayerLevels = true;

    @OriginalMember(owner = "client!bw", name = "I", descriptor = "Z")
    public static boolean useSprites = false;

    @OriginalMember(owner = "client!fi", name = "h", descriptor = "I")
    public static int verticalBorderSpriteId;

    @OriginalMember(owner = "client!caa", name = "b", descriptor = "Lclient!st;")
    public static Sprite leftBorderSprite;

    @OriginalMember(owner = "client!caa", name = "c", descriptor = "I")
    public static int separatorSpriteId;

    @OriginalMember(owner = "client!rb", name = "a", descriptor = "Lclient!st;")
    public static Sprite rightBorderSprite;

    @OriginalMember(owner = "client!at", name = "j", descriptor = "Lclient!st;")
    public static Sprite bottomBorderSprite;

    @OriginalMember(owner = "client!ic", name = "a", descriptor = "Lclient!st;")
    public static Sprite bottomLeftCornerSprite;

    @OriginalMember(owner = "client!rla", name = "f", descriptor = "Lclient!st;")
    public static Sprite bottomRightCornerSprite;

    @OriginalMember(owner = "client!wq", name = "T", descriptor = "I")
    public static int topColour;

    @OriginalMember(owner = "client!qca", name = "w", descriptor = "I")
    public static int topOpacity;

    @OriginalMember(owner = "client!fm", name = "l", descriptor = "I")
    public static int spriteBodyColour;

    @OriginalMember(owner = "client!mn", name = "m", descriptor = "I")
    public static int spriteBodyOpacity;

    @OriginalMember(owner = "client!is", name = "f", descriptor = "I")
    public static int topCornerSpriteId;

    @OriginalMember(owner = "client!is", name = "m", descriptor = "I")
    public static int bottomCornerSpriteId;

    @OriginalMember(owner = "client!kla", name = "Hc", descriptor = "I")
    public static int horizontalBorderSpriteId;

    @OriginalMember(owner = "client!ro", name = "f", descriptor = "I")
    public static int textColour;

    @OriginalMember(owner = "client!uaa", name = "c", descriptor = "I")
    public static int spriteHighlightColour;

    @OriginalMember(owner = "client!oia", name = "f", descriptor = "Lclient!st;")
    public static Sprite separatorSprite;

    @OriginalMember(owner = "client!aa", name = "b", descriptor = "Lclient!st;")
    public static Sprite topLeftCornerSprite;

    @OriginalMember(owner = "client!td", name = "r", descriptor = "Lclient!st;")
    public static Sprite topRightCornerSprite;

    @OriginalMember(owner = "client!hfa", name = "r", descriptor = "Z")
    public static boolean collapsed = false;

    @OriginalMember(owner = "client!eia", name = "y", descriptor = "I")
    public static int subMenuMinLength = -1;

    @OriginalMember(owner = "client!gi", name = "d", descriptor = "Z")
    public static boolean shiftClick = false;

    @OriginalMember(owner = "client!qt", name = "c", descriptor = "I")
    public static int anInt8149 = 0;

    @OriginalMember(owner = "client!ch", name = "j", descriptor = "Lclient!pg;")
    public static MiniMenuEntryInner draggedEntry;

    @OriginalMember(owner = "client!oj", name = "t", descriptor = "I")
    public static int anInt6964 = 0;

    @OriginalMember(owner = "client!pq", name = "x", descriptor = "Z")
    public static boolean showFaceHere;

    @OriginalMember(owner = "client!fj", name = "z", descriptor = "I")
    public static int randomSeed;

    @OriginalMember(owner = "client!cja", name = "b", descriptor = "(B)V")
    public static void reset() {
        for (@Pc(10) MiniMenuEntry entry = (MiniMenuEntry) entryQueue.first(); entry != null; entry = (MiniMenuEntry) entryQueue.next()) {
            if (entry.size > 1) {
                entry.size = 0;
                cache.put(entry, ((MiniMenuEntryInner) entry.innerEntries.sentinel.next2).entryKey);
                entry.innerEntries.clear();
            }
        }
        innerEntryCount = 0;
        entryCount = 0;
        innerEntryQueue.clear();
        entryTable.clear();
        entryQueue.clear();
        addEntryInner(cancelEntry);
    }

    @OriginalMember(owner = "client!eka", name = "a", descriptor = "(IIILclient!ha;)V")
    public static void addEntries3DView(@OriginalArg(3) Toolkit toolkit, @OriginalArg(2) int mouseX, @OriginalArg(0) int mouseY) {
        if (mouseX < 0 || mouseY < 0 || Static240.anInt3955 == 0 || Static275.anInt4424 == 0) {
            return;
        }

        @Pc(38) Matrix local38;
        @Pc(57) int local57;
        @Pc(45) int local45;
        @Pc(49) int local49;
        @Pc(53) int local53;
        @Pc(63) int local63;
        @Pc(69) int local69;
        if (OrthoMode.toolkitActive) {
            OrthoMode.method9331(false);
            local38 = toolkit.camera();
            @Pc(41) int[] local41 = toolkit.Y();
            local45 = local41[1];
            local49 = local41[2];
            local53 = local41[3];
            local57 = local41[0];
            local63 = OrthoMode.method3503(false) + mouseX;
            local69 = OrthoMode.method7649(false) + mouseY;
        } else {
            toolkit.DA(Static168.anInt2842, Static232.anInt3829, Static240.anInt3955, Static275.anInt4424);
            local49 = Static240.anInt3955;
            local53 = Static275.anInt4424;
            local45 = Static232.anInt3829;
            local57 = Static168.anInt2842;
            toolkit.KA(InterfaceManager.optionsX, InterfaceManager.optionsY, Static240.anInt3955, Static275.anInt4424);
            local38 = toolkit.createMatrix();
            local38.createCamera(Static428.anInt6487, Static427.anInt6480, Static523.pickCameraZ, Static524.anInt8044, Static271.anInt4363, Static707.anInt10641);
            local63 = mouseX;
            toolkit.setCamera(local38);
            local69 = mouseY;
        }
        Static501.method6716(true);
        if (local53 == 0) {
            local53 = 1;
        }
        if (local49 == 0) {
            local49 = 1;
        }
        @Pc(148) int local148;
        @Pc(177) int local177;
        @Pc(186) int local186;
        @Pc(286) int local286;
        @Pc(295) int local295;
        @Pc(306) int local306;
        @Pc(317) int local317;
        @Pc(140) int local140;
        @Pc(370) int local370;
        if (Static706.floor != null && (!InterfaceManager.targetMode || (InterfaceManager.targetMask & 0x40) != 0)) {
            local140 = -1;
            @Pc(142) int local142 = -1;
            @Pc(145) int local145 = toolkit.i();
            local148 = toolkit.XA();
            @Pc(159) int local159;
            @Pc(168) int local168;
            if (OrthoMode.enabled) {
                local177 = local168 = OrthoMode.renderZoom * (local63 - local57) / local49;
                local159 = local186 = OrthoMode.renderZoom * (local69 - local45) / local53;
            } else {
                local159 = (local69 - local45) * local145 / local53;
                local168 = local148 * (local63 - local57) / local49;
                local177 = (local63 - local57) * local145 / local49;
                local186 = (local69 - local45) * local148 / local53;
            }
            @Pc(224) int[] local224 = new int[]{local177, local159, local145};
            @Pc(239) int[] local239 = new int[]{local168, local186, local148};
            local38.project(local224);
            local38.project(local239);
            @Pc(273) float local273 = Static394.method5543((float) local224[0], (float) local239[2], 4, (float) local224[2], (float) local239[1], (float) local224[1], (float) local239[0]);
            if (local273 > 0.0F) {
                local286 = local239[0] - local224[0];
                local295 = local239[2] - local224[2];
                local306 = (int) ((float) local224[0] + local273 * (float) local286);
                local317 = (int) ((float) local224[2] + local273 * (float) local295);
                local140 = local306 + (PlayerEntity.self.getSize() - 1 << 8) >> 9;
                local142 = local317 + (PlayerEntity.self.getSize() - 1 << 8) >> 9;
                @Pc(345) byte local345 = PlayerEntity.self.level;
                if (local345 < 3 && (Static280.tileFlags[1][local306 >> 9][local317 >> 9] & 0x2) != 0) {
                    local370 = local345 + 1;
                }
            }
            if (local140 != -1 && local142 != -1) {
                if (InterfaceManager.targetMode && (InterfaceManager.targetMask & 0x40) != 0) {
                    @Pc(453) Component local453 = InterfaceList.getComponent(InterfaceManager.targetSlot, InterfaceManager.targetComponent);
                    if (local453 == null) {
                        InterfaceManager.endTargetMode();
                    } else {
                        addEntryInner(false, -1, 0L, local140, local142, InterfaceManager.targetVerb, 21, true, InterfaceManager.targetEnterCursor, " ->", local140 << 0 | local142, true);
                    }
                } else {
                    if (showFaceHere) {
                        addEntryInner(false, -1, 0L, local140, local142, LocalisedText.FACEHERE.localise(Client.language), 11, true, -1, "", local142 | local140 << 0, true);
                    }
                    addEntryInner(false, -1, 0L, local140, local142, Static331.moveText, 58, true, Static331.moveCursor, "", local142 | local140 << 0, true);
                }
            }
        }
        if (OrthoMode.toolkitActive) {
            Static480.method6469();
        }
        for (local140 = 0; local140 < (OrthoMode.toolkitActive ? 2 : 1); local140++) {
            @Pc(503) boolean local503 = local140 == 0;
            @Pc(510) PickList local510 = local503 ? Static514.activePickList : OrthoMode.orthoPickList;
            local148 = mouseX;
            local177 = mouseY;
            if (OrthoMode.toolkitActive) {
                OrthoMode.method9331(local503);
                local148 = mouseX + OrthoMode.method3503(local503);
                local177 = mouseY + OrthoMode.method7649(local503);
            }
            @Pc(538) LinkedList local538 = local510.entities;
            for (@Pc(543) PickableEntity local543 = (PickableEntity) local538.first(); local543 != null; local543 = (PickableEntity) local538.next()) {
                if ((ignorePlayerLevels || local543.aEntity_18.level == PlayerEntity.self.level) && local543.method6496(toolkit, local177, local148)) {
                    @Pc(584) int local584;
                    if (local543.aEntity_18 instanceof PositionEntity) {
                        local186 = ((PositionEntity) local543.aEntity_18).x1;
                        local584 = ((PositionEntity) local543.aEntity_18).z1;
                    } else {
                        local584 = local543.aEntity_18.z >> 9;
                        local186 = local543.aEntity_18.x >> 9;
                    }
                    @Pc(723) int local723;
                    @Pc(735) int local735;
                    @Pc(864) int local864;
                    @Pc(614) int local614;
                    if (local543.aEntity_18 instanceof PlayerEntity) {
                        @Pc(610) PlayerEntity local610 = (PlayerEntity) local543.aEntity_18;
                        local614 = local610.getSize();
                        if ((local614 & 0x1) == 0 && (local610.x & 0x1FF) == 0 && (local610.z & 0x1FF) == 0 || (local614 & 0x1) == 1 && (local610.x & 0x1FF) == 256 && (local610.z & 0x1FF) == 256) {
                            local286 = local610.x - (local610.getSize() - 1 << 8);
                            local295 = local610.z - (local610.getSize() - 1 << 8);
                            for (local306 = 0; local306 < NPCList.size; local306++) {
                                @Pc(690) NPCEntityNode local690 = (NPCEntityNode) NPCList.local.get(NPCList.slots[local306]);
                                if (local690 != null) {
                                    @Pc(695) NPCEntity local695 = local690.npc;
                                    if (TimeUtils.clock != local695.anInt10743 && local695.visible) {
                                        local723 = local695.x - (local695.type.size - 1 << 8);
                                        local735 = local695.z - (local695.type.size - 1 << 8);
                                        if (local286 <= local723 && local695.type.size <= local610.getSize() - (local723 - local286 >> 9) && local735 >= local295 && local695.type.size <= local610.getSize() - (local735 - local295 >> 9)) {
                                            addNpcEntries(local543.aEntity_18.level != PlayerEntity.self.level, local695);
                                            local695.anInt10743 = TimeUtils.clock;
                                        }
                                    }
                                }
                            }
                            local317 = PlayerList.highResolutionCount;
                            @Pc(820) int[] local820 = PlayerList.highResolutionSlots;
                            for (local723 = 0; local723 < local317; local723++) {
                                @Pc(830) PlayerEntity local830 = PlayerList.highResolutionPlayers[local820[local723]];
                                if (local830 != null && local830.anInt10743 != TimeUtils.clock && local830 != local610 && local830.visible) {
                                    local864 = local830.x - (local830.getSize() - 1 << 8);
                                    @Pc(876) int local876 = local830.z - (local830.getSize() - 1 << 8);
                                    if (local864 >= local286 && local830.getSize() <= local610.getSize() - (local864 - local286 >> 9) && local876 >= local295 && local830.getSize() <= local610.getSize() - (local876 - local295 >> 9)) {
                                        addPlayerEntries(local543.aEntity_18.level != PlayerEntity.self.level, local830);
                                        local830.anInt10743 = TimeUtils.clock;
                                    }
                                }
                            }
                        }
                        if (TimeUtils.clock == local610.anInt10743) {
                            continue;
                        }
                        addPlayerEntries(PlayerEntity.self.level != local543.aEntity_18.level, local610);
                        local610.anInt10743 = TimeUtils.clock;
                    }
                    if (local543.aEntity_18 instanceof NPCEntity) {
                        @Pc(988) NPCEntity local988 = (NPCEntity) local543.aEntity_18;
                        if (local988.type != null) {
                            if ((local988.type.size & 0x1) == 0 && (local988.x & 0x1FF) == 0 && (local988.z & 0x1FF) == 0 || (local988.type.size & 0x1) == 1 && (local988.x & 0x1FF) == 256 && (local988.z & 0x1FF) == 256) {
                                local614 = local988.x - (local988.type.size - 1 << 8);
                                local286 = local988.z - (local988.type.size - 1 << 8);
                                for (local295 = 0; local295 < NPCList.size; local295++) {
                                    @Pc(1081) NPCEntityNode local1081 = (NPCEntityNode) NPCList.local.get(NPCList.slots[local295]);
                                    if (local1081 != null) {
                                        @Pc(1086) NPCEntity local1086 = local1081.npc;
                                        if (local1086.anInt10743 != TimeUtils.clock && local1086 != local988 && local1086.visible) {
                                            local370 = local1086.x - (local1086.type.size - 1 << 8);
                                            local723 = local1086.z - (local1086.type.size - 1 << 8);
                                            if (local614 <= local370 && local988.type.size - (local370 - local614 >> 9) >= local1086.type.size && local723 >= local286 && local1086.type.size <= local988.type.size - (local723 - local286 >> 9)) {
                                                addNpcEntries(PlayerEntity.self.level != local543.aEntity_18.level, local1086);
                                                local1086.anInt10743 = TimeUtils.clock;
                                            }
                                        }
                                    }
                                }
                                local306 = PlayerList.highResolutionCount;
                                @Pc(1216) int[] local1216 = PlayerList.highResolutionSlots;
                                for (local370 = 0; local370 < local306; local370++) {
                                    @Pc(1226) PlayerEntity local1226 = PlayerList.highResolutionPlayers[local1216[local370]];
                                    if (local1226 != null && local1226.anInt10743 != TimeUtils.clock && local1226.visible) {
                                        local735 = local1226.x - (local1226.getSize() - 1 << 8);
                                        local864 = local1226.z - (local1226.getSize() - 1 << 8);
                                        if (local614 <= local735 && local1226.getSize() <= local988.type.size - (local735 - local614 >> 9) && local286 <= local864 && local1226.getSize() <= local988.type.size - (local864 - local286 >> 9)) {
                                            addPlayerEntries(PlayerEntity.self.level != local543.aEntity_18.level, local1226);
                                            local1226.anInt10743 = TimeUtils.clock;
                                        }
                                    }
                                }
                            }
                            if (TimeUtils.clock == local988.anInt10743) {
                                continue;
                            }
                            addNpcEntries(PlayerEntity.self.level != local543.aEntity_18.level, local988);
                            local988.anInt10743 = TimeUtils.clock;
                        }
                    }
                    if (local543.aEntity_18 instanceof ObjStackEntity) {
                        @Pc(1385) int local1385 = local186 + WorldMap.areaBaseX;
                        local614 = WorldMap.areaBaseZ + local584;
                        @Pc(1406) ObjStack local1406 = (ObjStack) Static497.objStacks.get(local614 << 14 | local543.aEntity_18.level << 28 | local1385);
                        if (local1406 != null) {
                            local295 = 0;
                            for (@Pc(1416) ObjStackEntry local1416 = (ObjStackEntry) local1406.objs.last(); local1416 != null; local1416 = (ObjStackEntry) local1406.objs.previous()) {
                                @Pc(1424) ObjType local1424 = ObjTypeList.instance.list(local1416.id);
                                if (InterfaceManager.targetMode && PlayerEntity.self.level == local543.aEntity_18.level) {
                                    @Pc(1451) ParamType local1451 = InterfaceManager.targetParam == -1 ? null : ParamTypeList.instance.list(InterfaceManager.targetParam);
                                    if ((InterfaceManager.targetMask & 0x1) != 0 && (local1451 == null || local1424.param(InterfaceManager.targetParam, local1451.defaultint) != local1451.defaultint)) {
                                        addEntryInner(false, -1, local1416.id, local186, local584, InterfaceManager.targetVerb, 17, true, InterfaceManager.targetEnterCursor, InterfaceManager.targetedVerb + " -> <col=ff9040>" + local1424.name, local295, false);
                                    }
                                }
                                if (local543.aEntity_18.level == PlayerEntity.self.level) {
                                    @Pc(1525) String[] local1525 = local1424.op;
                                    for (local723 = local1525.length - 1; local723 >= 0; local723--) {
                                        if (local1525[local723] != null) {
                                            @Pc(1540) short local1540 = 0;
                                            local864 = Cursor.interaction;
                                            if (local723 == 0) {
                                                local1540 = 25;
                                            }
                                            if (local723 == 1) {
                                                local1540 = 5;
                                            }
                                            if (local723 == 2) {
                                                local1540 = 50;
                                            }
                                            if (local723 == 3) {
                                                local1540 = 6;
                                            }
                                            if (local723 == 4) {
                                                local1540 = 45;
                                            }
                                            if (local723 == 5) {
                                                local1540 = 1007;
                                            }
                                            if (local723 == local1424.cursor1op) {
                                                local864 = local1424.cursor1;
                                            }
                                            if (local723 == local1424.cursor2op) {
                                                local864 = local1424.cursor2;
                                            }
                                            addEntryInner(false, -1, local1416.id, local186, local584, local1525[local723], local1540, true, local864, "<col=ff9040>" + local1424.name, local295, false);
                                        }
                                    }
                                }
                                local295++;
                            }
                        }
                    }
                    if (local543.aEntity_18 instanceof Location) {
                        @Pc(1654) Location local1654 = (Location) local543.aEntity_18;
                        @Pc(1661) LocType local1661 = LocTypeList.instance.list(local1654.getId());
                        if (local1661.multiloc != null) {
                            local1661 = local1661.getMultiLoc(TimedVarDomain.instance);
                        }
                        if (local1661 != null) {
                            if (InterfaceManager.targetMode && PlayerEntity.self.level == local543.aEntity_18.level) {
                                @Pc(1697) ParamType local1697 = InterfaceManager.targetParam == -1 ? null : ParamTypeList.instance.list(InterfaceManager.targetParam);
                                if ((InterfaceManager.targetMask & 0x4) != 0 && (local1697 == null || local1661.param(local1697.defaultint, InterfaceManager.targetParam) != local1697.defaultint)) {
                                    addEntryInner(false, -1, Static277.method4042(local1654, local584, local186), local186, local584, InterfaceManager.targetVerb, 60, true, InterfaceManager.targetEnterCursor, InterfaceManager.targetedVerb + " -> <col=00ffff>" + local1661.name, local1654.hashCode(), false);
                                }
                            }
                            if (PlayerEntity.self.level == local543.aEntity_18.level) {
                                @Pc(1763) String[] local1763 = local1661.ops;
                                if (local1763 != null) {
                                    for (local295 = local1763.length - 1; local295 >= 0; local295--) {
                                        if (local1763[local295] != null) {
                                            @Pc(1780) short local1780 = 0;
                                            local317 = Cursor.interaction;
                                            if (local295 == 0) {
                                                local1780 = 19;
                                            }
                                            if (local295 == 1) {
                                                local1780 = 13;
                                            }
                                            if (local295 == 2) {
                                                local1780 = 46;
                                            }
                                            if (local295 == 3) {
                                                local1780 = 8;
                                            }
                                            if (local295 == 4) {
                                                local1780 = 1010;
                                            }
                                            if (local295 == local1661.cursor1Op) {
                                                local317 = local1661.cursor1;
                                            }
                                            if (local295 == 5) {
                                                local1780 = 1008;
                                            }
                                            if (local1661.cursor2Op == local295) {
                                                local317 = local1661.cursor2;
                                            }
                                            addEntryInner(false, -1, Static277.method4042(local1654, local584, local186), local186, local584, local1763[local295], local1780, true, local317, "<col=00ffff>" + local1661.name, local1654.hashCode(), false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (OrthoMode.toolkitActive) {
                Static480.method6469();
            }
        }
        Static501.method6716(false);
    }

    @OriginalMember(owner = "client!om", name = "a", descriptor = "(Lclient!cg;I)V")
    public static void addEntityEntries(@OriginalArg(0) PathingEntity entity) {
        if (entity instanceof NPCEntity) {
            @Pc(5) NPCEntity npc = (NPCEntity) entity;
            if (npc.type != null) {
                addNpcEntries(PlayerEntity.self.level != npc.level, npc);
            }
        } else if (entity instanceof PlayerEntity) {
            @Pc(33) PlayerEntity player = (PlayerEntity) entity;
            addPlayerEntries(player.level != PlayerEntity.self.level, player);
        }
    }

    @OriginalMember(owner = "client!nca", name = "a", descriptor = "(ZIJIILjava/lang/String;IZILjava/lang/String;JBZ)V")
    public static void addEntryInner(@OriginalArg(0) boolean differentLevel, @OriginalArg(1) int invObject, @OriginalArg(2) long v1, @OriginalArg(3) int v2, @OriginalArg(4) int v3, @OriginalArg(5) String targetVerb, @OriginalArg(6) int action, @OriginalArg(7) boolean arg7, @OriginalArg(8) int cursor, @OriginalArg(9) String opBase, @OriginalArg(10) long key, @OriginalArg(12) boolean independent) {
        if (!open && innerEntryCount < 500) {
            @Pc(20) int targetEndCursor = cursor != -1 ? cursor : InterfaceManager.targetEndCursor;
            @Pc(36) MiniMenuEntryInner inner = new MiniMenuEntryInner(targetVerb, opBase, targetEndCursor, action, invObject, v1, v2, v3, arg7, differentLevel, key, independent);
            addEntryInner(inner);
        }
    }

    @OriginalMember(owner = "client!gfa", name = "a", descriptor = "(Z)Z")
    public static boolean topEntryIsIfButtonX1() {
        if (topEntry == null) {
            return false;
        } else {
            if (topEntry.action >= 2000) {
                topEntry.action -= 2000;
            }
            return topEntry.action == MiniMenuAction.IF_BUTTONX2;
        }
    }

    @OriginalMember(owner = "client!ci", name = "a", descriptor = "(I)Z")
    public static boolean isPopulated() {
        return innerEntryCount > 0;
    }

    @OriginalMember(owner = "client!cv", name = "b", descriptor = "(B)V")
    public static void openButtons() {
        for (@Pc(4) MiniMenuEntryInner entry = (MiniMenuEntryInner) innerEntryQueue.first(); entry != null; entry = (MiniMenuEntryInner) innerEntryQueue.next()) {
            if (MiniMenuAction.isButtonOp(entry.action)) {
                openInner(entry);
            }
        }
    }

    @OriginalMember(owner = "client!client", name = "a", descriptor = "(BLclient!pg;)V")
    public static void addEntryInner(@OriginalArg(1) MiniMenuEntryInner inner) {
        if (inner == null) {
            return;
        }

        innerEntryQueue.addLast(inner);
        innerEntryCount++;

        @Pc(33) MiniMenuEntry entry;
        if (inner.independent || "".equals(inner.opBase)) {
            entry = new MiniMenuEntry(inner.opBase);
            entryCount++;
        } else {
            @Pc(41) long key = inner.entryKey;
            for (entry = (MiniMenuEntry) entryTable.get(key); entry != null && !entry.title.equals(inner.opBase); entry = (MiniMenuEntry) entryTable.nextWithSameKey()) {
            }

            if (entry == null) {
                entry = (MiniMenuEntry) cache.get(key);
                if (entry != null && !entry.title.equals(inner.opBase)) {
                    entry = null;
                }
                if (entry == null) {
                    entry = new MiniMenuEntry(inner.opBase);
                }

                entryTable.put(key, entry);
                entryCount++;
            }
        }

        if (entry.add(inner)) {
            reposition(entry);
        }
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(Lclient!cba;B)V")
    public static void reposition(@OriginalArg(0) MiniMenuEntry entry) {
        @Pc(5) boolean inserted = false;
        entry.unlink2();

        for (@Pc(21) MiniMenuEntry other = (MiniMenuEntry) entryQueue.first(); other != null; other = (MiniMenuEntry) entryQueue.next()) {
            if (isActionBefore(entry.getAction(), other.getAction())) {
                inserted = true;
                Node2.addAfter(other, entry);
                break;
            }
        }

        if (!inserted) {
            entryQueue.add(entry);
        }
    }

    @OriginalMember(owner = "client!rd", name = "a", descriptor = "(BII)Z")
    public static boolean isActionBefore(@OriginalArg(1) int curr, @OriginalArg(2) int prev) {
        if (prev >= 1000 && curr < 1000) {
            return true;
        } else if (prev >= 1000 || curr >= 1000) {
            return prev >= 1000 && curr >= 1000;
        } else if (MiniMenuAction.isTarget(curr)) {
            return true;
        } else if (!MiniMenuAction.isTarget(prev)) {
            return true;
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!hj", name = "c", descriptor = "(I)V")
    public static void setCancelEntry() {
        cancelEntry = new MiniMenuEntryInner(LocalisedText.CANCEL.localise(Client.language), "", InterfaceManager.targetEndCursor, 1012, -1, 0L, 0, 0, true, false, 0L, true);
    }

    @OriginalMember(owner = "client!vj", name = "a", descriptor = "(ILclient!pg;)V")
    public static void openInner(@OriginalArg(1) MiniMenuEntryInner inner) {
        if (open) {
            return;
        }

        inner.unlink();
        innerEntryCount--;

        if (inner.independent) {
            for (@Pc(22) MiniMenuEntry entry = (MiniMenuEntry) entryQueue.first(); entry != null; entry = (MiniMenuEntry) entryQueue.next()) {
                if (!entry.title.equals(inner.opBase)) {
                    continue;
                }

                @Pc(31) boolean found = false;
                for (@Pc(37) MiniMenuEntryInner other = (MiniMenuEntryInner) entry.innerEntries.first(); other != null; other = (MiniMenuEntryInner) entry.innerEntries.next()) {
                    if (other == inner) {
                        found = true;

                        if (entry.remove(inner)) {
                            reposition(entry);
                        }

                        break;
                    }
                }

                if (found) {
                    break;
                }
            }
        } else {
            @Pc(79) long key = inner.entryKey;
            @Pc(85) MiniMenuEntry entry;

            for (entry = (MiniMenuEntry) entryTable.get(key); entry != null; entry = (MiniMenuEntry) entryTable.nextWithSameKey()) {
                if (entry.title.equals(inner.opBase)) {
                    break;
                }
            }

            if (entry != null && entry.remove(inner)) {
                reposition(entry);
            }
        }
    }

    @OriginalMember(owner = "client!uja", name = "a", descriptor = "(IZLclient!wj;)V")
    public static void addNpcEntries(@OriginalArg(1) boolean differentLevel, @OriginalArg(2) NPCEntity npc) {
        if (innerEntryCount >= 400) {
            return;
        }

        @Pc(21) NPCType type = npc.type;
        @Pc(24) String npcName = npc.name;
        if (type.multinpcs != null) {
            type = type.getMultiNPC(TimedVarDomain.instance);
            if (type == null) {
                return;
            }
            npcName = type.name;
        }

        if (!type.interactive) {
            return;
        }

        if (npc.combatLevel != 0) {
            @Pc(67) String text = ModeGame.STELLAR_DAWN == Client.modeGame ? LocalisedText.RATING.localise(Client.language) : LocalisedText.LEVEL.localise(Client.language);
            npcName = npcName + colourCode(PlayerEntity.self.combatLevel, npc.combatLevel) + " (" + text + npc.combatLevel + ")";
        }

        if (InterfaceManager.targetMode && !differentLevel) {
            @Pc(113) ParamType param = InterfaceManager.targetParam != -1 ? ParamTypeList.instance.list(InterfaceManager.targetParam) : null;

            if ((InterfaceManager.targetMask & TargetMask.TGT_NPC) != 0 && (param == null || type.param(InterfaceManager.targetParam, param.defaultint) != param.defaultint)) {
                addEntryInner(false, -1, npc.slot, 0, 0, InterfaceManager.targetVerb, MiniMenuAction.TGT_NPC, true, InterfaceManager.targetEnterCursor, InterfaceManager.targetedVerb + " -> <col=ffff00>" + npcName, npc.slot, false);
            }
        }

        if (differentLevel) {
            return;
        }

        @Pc(176) String[] ops = type.op;
        if (debugOps) {
            ops = prefixWithNum(ops);
        }
        if (ops == null) {
            return;
        }

        for (@Pc(189) int op = ops.length - 1; op >= 0; op--) {
            if (ops[op] != null && (type.aByte107 == 0 || !ops[op].equalsIgnoreCase(LocalisedText.ATTACK.localise(Client.language)) && !ops[op].equalsIgnoreCase(LocalisedText.EXAMINE.localise(Client.language)))) {
                @Pc(226) short action = 0;
                @Pc(228) int cursor = Cursor.interaction;

                if (op == 0) {
                    action = MiniMenuAction.OPNPC1;
                }
                if (op == 1) {
                    action = MiniMenuAction.OPNPC2;
                }
                if (op == 2) {
                    action = MiniMenuAction.OPNPC3;
                }
                if (op == 3) {
                    action = MiniMenuAction.OPNPC4;
                }
                if (op == 4) {
                    action = MiniMenuAction.OPNPC5;
                }
                if (op == 5) {
                    action = MiniMenuAction.OPNPC6;
                }

                if (op == type.cursor1Op) {
                    cursor = type.cursor1;
                }
                if (type.cursor2Op == op) {
                    cursor = type.cursor2;
                }

                addEntryInner(false, -1, npc.slot, 0, 0, ops[op], action, true, ops[op].equalsIgnoreCase(LocalisedText.ATTACK.localise(Client.language)) ? type.attackCursor : cursor, "<col=ffff00>" + npcName, npc.slot, false);
            }
        }

        if (type.aByte107 == 1) {
            for (@Pc(341) int op = 0; op < ops.length; op++) {
                if (ops[op] != null && (ops[op].equalsIgnoreCase(LocalisedText.ATTACK.localise(Client.language)) || ops[op].equalsIgnoreCase(LocalisedText.EXAMINE.localise(Client.language)))) {
                    @Pc(372) short offset = 0;
                    if (npc.combatLevel > PlayerEntity.self.combatLevel) {
                        offset = 2000;
                    }

                    @Pc(385) short action = 0;
                    @Pc(387) int cursor = Cursor.interaction;

                    if (op == 0) {
                        action = MiniMenuAction.OPNPC1;
                    }
                    if (op == 1) {
                        action = MiniMenuAction.OPNPC2;
                    }
                    if (op == 2) {
                        action = MiniMenuAction.OPNPC3;
                    }
                    if (op == 3) {
                        action = MiniMenuAction.OPNPC4;
                    }
                    if (op == 4) {
                        action = MiniMenuAction.OPNPC5;
                    }
                    if (op == 5) {
                        action = MiniMenuAction.OPNPC6;
                    }

                    if (type.cursor1Op == op) {
                        cursor = type.cursor1;
                    }
                    if (action != 0) {
                        action += offset;
                    }
                    if (type.cursor2Op == op) {
                        cursor = type.cursor2;
                    }

                    addEntryInner(false, -1, npc.slot, 0, 0, ops[op], action, true, ops[op].equalsIgnoreCase(LocalisedText.ATTACK.localise(Client.language)) ? type.attackCursor : cursor, "<col=ffff00>" + npcName, npc.slot, false);
                }
            }
        }
    }

    @OriginalMember(owner = "client!vu", name = "b", descriptor = "(III)Ljava/lang/String;")
    public static String colourCode(@OriginalArg(1) int ourLevel, @OriginalArg(2) int theirLevel) {
        @Pc(8) int delta = ourLevel - theirLevel;

        if (delta < -9) {
            return "<col=ff0000>";
        } else if (delta < -6) {
            return "<col=ff3000>";
        } else if (delta < -3) {
            return "<col=ff7000>";
        } else if (delta < 0) {
            return "<col=ffb000>";
        } else if (delta > 9) {
            return "<col=00ff00>";
        } else if (delta > 6) {
            return "<col=40ff00>";
        } else if (delta > 3) {
            return "<col=80ff00>";
        } else if (delta > 0) {
            return "<col=c0ff00>";
        } else {
            return "<col=ffff00>";
        }
    }

    @OriginalMember(owner = "client!hl", name = "a", descriptor = "(I[Ljava/lang/String;)[Ljava/lang/String;")
    public static String[] prefixWithNum(@OriginalArg(1) String[] ops) {
        @Pc(6) String[] prefixed = new String[5];
        for (@Pc(8) int i = 0; i < 5; i++) {
            prefixed[i] = i + ": ";

            if (ops != null && ops[i] != null) {
                prefixed[i] = prefixed[i] + ops[i];
            }
        }
        return prefixed;
    }

    @OriginalMember(owner = "client!rj", name = "a", descriptor = "(Lclient!ha;I)V")
    public static void drawPreview(@OriginalArg(0) Toolkit toolkit) {
        if ((innerEntryCount < 2 && !InterfaceManager.targetMode) || InterfaceManager.dragSource != null) {
            return;
        }

        @Pc(63) String text;
        if (InterfaceManager.targetMode && innerEntryCount < 2) {
            text = InterfaceManager.targetVerb + LocalisedText.MINISEPARATOR.localise(Client.language) + InterfaceManager.targetedVerb + " ->";
        } else if (shiftClick && KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_SHIFT) && innerEntryCount > 2) {
            text = getLineText(activeEntry);
        } else {
            @Pc(55) MiniMenuEntryInner entry = activeEntry;
            if (entry == null) {
                return;
            }

            text = getLineText(entry);

            @Pc(65) int[] quests = null;
            if (MiniMenuAction.isObjOp(entry.action)) {
                quests = ObjTypeList.instance.list((int) entry.v1).quests;
            } else if (entry.objId != -1) {
                quests = ObjTypeList.instance.list(entry.objId).quests;
            } else if (MiniMenuAction.isNpcOp(entry.action)) {
                @Pc(93) NPCEntityNode node = (NPCEntityNode) NPCList.local.get((int) entry.v1);

                if (node != null) {
                    @Pc(98) NPCEntity entity = node.npc;

                    @Pc(101) NPCType type = entity.type;
                    if (type.multinpcs != null) {
                        type = type.getMultiNPC(TimedVarDomain.instance);
                    }

                    if (type != null) {
                        quests = type.quests;
                    }
                }
            } else if (MiniMenuAction.isLocOp(entry.action)) {
                @Pc(131) LocType type = LocTypeList.instance.list((int) ((entry.v1 >>> 32) & 0x7FFFFFFFL));
                if (type.multiloc != null) {
                    type = type.getMultiLoc(TimedVarDomain.instance);
                }

                if (type != null) {
                    quests = type.quests;
                }
            }

            if (quests != null) {
                text = text + questIcon(quests);
            }
        }

        if (innerEntryCount > 2) {
            text = text + "<col=ffffff> / " + (innerEntryCount - 2) + LocalisedText.MOREOPTIONS.localise(Client.language);
        }

        if (WorldMap.optionsComponent != null) {
            @Pc(232) Font font = WorldMap.optionsComponent.font(toolkit);
            if (font == null) {
                font = Fonts.b12;
            }

            font.renderRandom(Static329.anIntArray163, WorldMap.optionsComponent.textAlignX, WorldMap.optionsComponent.width, iconHeights, WorldMap.optionsComponent.colour, WorldMap.optionsComponent.height, random, text, WorldMap.optionsX, WorldMap.optionsComponent.graphicShadow, icons, randomSeed, WorldMap.optionsY, WorldMap.optionsComponent.textAlignY);
            InterfaceManager.redrawWithin(Static329.anIntArray163[0], Static329.anIntArray163[1], Static329.anIntArray163[2], Static329.anIntArray163[3]);
        } else if (InterfaceManager.optionsComponent != null && Client.modeGame == ModeGame.RUNESCAPE) {
            @Pc(299) int width = Fonts.b12.renderRandom(icons, randomSeed, 0xFFFFFF, InterfaceManager.optionsY + 16, text, iconHeights, 0, random, InterfaceManager.optionsX + 4);
            InterfaceManager.redrawWithin(InterfaceManager.optionsX + 4, InterfaceManager.optionsY, width + Fonts.b12Metrics.stringWidth(text), 16);
        }
    }

    @OriginalMember(owner = "client!qf", name = "a", descriptor = "(Lclient!pg;B)Ljava/lang/String;")
    public static String getLineText(@OriginalArg(0) MiniMenuEntryInner entry) {
        if (entry.second == null || entry.second.length() == 0) {
            return entry.opBase == null || entry.opBase.length() <= 0 ? entry.op : entry.op + LocalisedText.MINISEPARATOR.localise(Client.language) + entry.opBase;
        } else if (entry.opBase == null || entry.opBase.length() <= 0) {
            return entry.op + LocalisedText.MINISEPARATOR.localise(Client.language) + entry.second;
        } else {
            return entry.op + LocalisedText.MINISEPARATOR.localise(Client.language) + entry.opBase + LocalisedText.MINISEPARATOR.localise(Client.language) + entry.second;
        }
    }

    @OriginalMember(owner = "client!hda", name = "a", descriptor = "(Lclient!ha;IIIIILclient!pg;IIIII)V")
    public static void drawEntryInner(@OriginalArg(0) Toolkit toolkit, @OriginalArg(6) MiniMenuEntryInner entry, @OriginalArg(9) int x, @OriginalArg(3) int y, @OriginalArg(1) int width, @OriginalArg(2) int height, @OriginalArg(10) int mouseX, @OriginalArg(8) int mouseY, @OriginalArg(7) int textColour, @OriginalArg(5) int highlightColour, @OriginalArg(4) int innerY) {
        if (mouseX > x && mouseX < width + x && mouseY > innerY - 13 && mouseY < innerY + 3 && entry.aBoolean552) {
            textColour = highlightColour;
        }

        @Pc(49) int[] quests = null;
        if (MiniMenuAction.isObjOp(entry.action)) {
            quests = ObjTypeList.instance.list((int) entry.v1).quests;
        } else if (entry.objId != -1) {
            quests = ObjTypeList.instance.list(entry.objId).quests;
        } else if (MiniMenuAction.isNpcOp(entry.action)) {
            @Pc(110) NPCEntityNode node = (NPCEntityNode) NPCList.local.get((int) entry.v1);

            if (node != null) {
                @Pc(115) NPCEntity npc = node.npc;
                @Pc(118) NPCType type = npc.type;
                if (type.multinpcs != null) {
                    type = type.getMultiNPC(TimedVarDomain.instance);
                }

                if (type != null) {
                    quests = type.quests;
                }
            }
        } else if (MiniMenuAction.isLocOp(entry.action)) {
            @Pc(87) LocType type = LocTypeList.instance.list((int) (entry.v1 >>> 32 & 0x7FFFFFFFL));
            if (type.multiloc != null) {
                type = type.getMultiLoc(TimedVarDomain.instance);
            }
            if (type != null) {
                quests = type.quests;
            }
        }

        @Pc(154) String text = getLineText(entry);
        if (quests != null) {
            text = text + questIcon(quests);
        }

        Fonts.b12.render(text, x + 3, innerY, textColour, 0, icons, iconHeights);

        if (entry.differentLevel) {
            Sprites.otherlevel.render(x + Fonts.b12Metrics.stringWidth(text) + 5, innerY - 12);
        }
    }

    @OriginalMember(owner = "client!hma", name = "a", descriptor = "(BLclient!pg;)I")
    public static int getLineWidth(@OriginalArg(1) MiniMenuEntryInner entry) {
        @Pc(15) String text = getLineText(entry);
        @Pc(17) int[] quests = null;

        if (MiniMenuAction.isObjOp(entry.action)) {
            quests = ObjTypeList.instance.list((int) entry.v1).quests;
        } else if (entry.objId != -1) {
            quests = ObjTypeList.instance.list(entry.objId).quests;
        } else if (MiniMenuAction.isNpcOp(entry.action)) {
            @Pc(51) NPCEntityNode node = (NPCEntityNode) NPCList.local.get((int) entry.v1);

            if (node != null) {
                @Pc(56) NPCEntity entity = node.npc;
                @Pc(59) NPCType type = entity.type;
                if (type.multinpcs != null) {
                    type = type.getMultiNPC(TimedVarDomain.instance);
                }

                if (type != null) {
                    quests = type.quests;
                }
            }
        } else if (MiniMenuAction.isLocOp(entry.action)) {
            @Pc(89) LocType type = LocTypeList.instance.list((int) ((entry.v1 >>> 32) & 0x7FFFFFFFL));
            if (type.multiloc != null) {
                type = type.getMultiLoc(TimedVarDomain.instance);
            }

            if (type != null) {
                quests = type.quests;
            }
        }

        if (quests != null) {
            text = text + questIcon(quests);
        }

        @Pc(130) int width = Fonts.b12Metrics.stringWidth(icons, text);
        if (entry.differentLevel) {
            width += Sprites.otherlevel.getWidth() + 4;
        }
        return width;
    }

    @OriginalMember(owner = "client!cf", name = "a", descriptor = "(I[I)Ljava/lang/String;")
    public static String questIcon(@OriginalArg(1) int[] quests) {
        @Pc(7) StringBuffer buffer = new StringBuffer();
        @Pc(9) int id = nameIconsCount;

        for (@Pc(11) int i = 0; i < quests.length; i++) {
            @Pc(19) QuestType type = QuestTypeList.instance.list(quests[i]);

            if (type.icon != -1) {
                @Pc(34) Sprite icon = (Sprite) questCache.get(type.icon);

                if (icon == null) {
                    @Pc(42) IndexedImage image = IndexedImage.loadFirst(js5.SPRITES, type.icon, 0);

                    if (image != null) {
                        icon = Toolkit.active.createSprite(image, true);
                        questCache.put(icon, type.icon);
                    }
                }

                if (icon != null) {
                    icons[id] = icon;
                    buffer.append(" <img=").append(id).append(">");
                    id++;
                }
            }
        }

        return buffer.toString();
    }

    @OriginalMember(owner = "client!ki", name = "a", descriptor = "(B[Lclient!st;)V")
    public static void setIcons(@OriginalArg(1) Sprite[] nameIcons) {
        nameIconsCount = nameIcons.length;
        icons = new Sprite[nameIconsCount + QUEST_ICON_COUNT];
        iconHeights = new int[nameIconsCount + QUEST_ICON_COUNT];
        Arrays.copy(nameIcons, 0, icons, 0, nameIconsCount);
        for (@Pc(32) int i = 0; i < nameIconsCount; i++) {
            iconHeights[i] = icons[i].scaleHeight();
        }
        for (@Pc(50) int i = nameIconsCount; i < icons.length; i++) {
            iconHeights[i] = QUEST_ICON_HEIGHT;
        }
    }

    @OriginalMember(owner = "client!nba", name = "a", descriptor = "(ZLclient!ca;I)V")
    public static void addPlayerEntries(@OriginalArg(0) boolean diffentLevel, @OriginalArg(1) PlayerEntity player) {
        if (innerEntryCount >= 400) {
            return;
        }

        if (player == PlayerEntity.self) {
            if (InterfaceManager.targetMode && (InterfaceManager.targetMask & TargetMask.TGT_SELF) != 0) {
                addEntryInner(false, -1, 0L, 0, 0, InterfaceManager.targetVerb, 4, true, InterfaceManager.targetEnterCursor, InterfaceManager.targetedVerb + " -> <col=ffffff>" + LocalisedText.SELF.localise(Client.language), player.slot, false);
            }
        } else {
            @Pc(177) String name;
            if (player.skillRating != 0) {
                if (player.skillRating != -1) {
                    name = player.getName(false, true) + " (" + LocalisedText.SKILL.localise(Client.language) + player.skillRating + ")";
                } else {
                    name = player.getName(false, true);
                }
            } else {
                @Pc(63) boolean outOfRange = true;
                if (PlayerEntity.self.combatRange != -1 && player.combatRange != -1) {
                    @Pc(91) int minRange = PlayerEntity.self.combatRange < player.combatRange ? PlayerEntity.self.combatRange : player.combatRange;
                    @Pc(98) int delta = PlayerEntity.self.combatLevel - player.combatLevel;
                    if (delta < 0) {
                        delta = -delta;
                    }
                    if (delta > minRange) {
                        outOfRange = false;
                    }
                }

                @Pc(129) String prefix = ModeGame.STELLAR_DAWN == Client.modeGame ? LocalisedText.RATING.localise(Client.language) : LocalisedText.LEVEL.localise(Client.language);
                if (player.combatLevel >= player.maxCombatLevel) {
                    name = player.getName(false, true) + (outOfRange ? colourCode(PlayerEntity.self.combatLevel, player.combatLevel) : "<col=ffffff>") + " (" + prefix + player.combatLevel + ")";
                } else {
                    name = player.getName(false, true) + (outOfRange ? colourCode(PlayerEntity.self.combatLevel, player.combatLevel) : "<col=ffffff>") + " (" + prefix + player.combatLevel + "+" + (player.maxCombatLevel - player.combatLevel) + ")";
                }
            }

            if (InterfaceManager.targetMode && !diffentLevel && (InterfaceManager.targetMask & 0x8) != 0) {
                addEntryInner(false, -1, player.slot, 0, 0, InterfaceManager.targetVerb, MiniMenuAction.TGT_PLAYER, true, InterfaceManager.targetEnterCursor, InterfaceManager.targetedVerb + " -> <col=ffffff>" + name, player.slot, false);
            }

            if (diffentLevel) {
                addEntryInner(true, 0, 0L, 0, 0, "<col=cccccc>" + name, -1, false, -1, "", player.slot, false);
            } else {
                for (@Pc(318) int op = 7; op >= 0; op--) {
                    if (playerOps[op] != null) {
                        @Pc(325) short offset = 0;
                        if (Client.modeGame == ModeGame.RUNESCAPE && playerOps[op].equalsIgnoreCase(LocalisedText.ATTACK.localise(Client.language))) {
                            if (Static324.reduceAttackPriority && PlayerEntity.self.combatLevel < player.combatLevel) {
                                offset = 2000;
                            }

                            if (PlayerEntity.self.team == 0 || player.team == 0) {
                                if (player.clanmate) {
                                    offset = 2000;
                                }
                            } else if (player.team == PlayerEntity.self.team) {
                                offset = 2000;
                            } else {
                                offset = 0;
                            }
                        } else if (playerOpsReducedPriority[op]) {
                            offset = 2000;
                        }

                        @Pc(403) short action = (short) (offset + MiniMenuAction.PLAYER_OPS[op]);
                        @Pc(416) int cursor = playerOpCursors[op] != -1 ? playerOpCursors[op] : Cursor.interaction;
                        addEntryInner(false, -1, player.slot, 0, 0, playerOps[op], action, true, cursor, "<col=ffffff>" + name, player.slot, false);
                    }
                }
            }

            if (!diffentLevel) {
                for (@Pc(484) MiniMenuEntryInner entry = (MiniMenuEntryInner) innerEntryQueue.first(); entry != null; entry = (MiniMenuEntryInner) innerEntryQueue.next()) {
                    if (entry.action == MiniMenuAction.WALK) {
                        entry.second = "<col=ffffff>" + name;
                        return;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!br", name = "a", descriptor = "(IILclient!pg;I)V")
    public static void doAction(@OriginalArg(0) int clickY, @OriginalArg(2) MiniMenuEntryInner entry, @OriginalArg(3) int clickX) {
        if (entry == null || entry == innerEntryQueue.sentinel) {
            return;
        }

        @Pc(16) int v2 = entry.v2;
        @Pc(19) int v3 = entry.v3;
        @Pc(22) int action = entry.action;
        @Pc(26) int v1 = (int) entry.v1;
        if (action >= 2000) {
            action -= 2000;
        }
        @Pc(35) long v1Long = entry.v1;

        if (action == MiniMenuAction.TGT_PLAYER) {
            @Pc(44) PlayerEntity player = PlayerList.highResolutionPlayers[v1];

            if (player != null) {
                Static481.crossDuration = 0;
                Static676.crossX = clickX;
                Static616.crossType = 2;
                Static305.crossY = clickY;

                @Pc(64) ClientMessage message = ClientMessage.create(ClientProt.OPPLAYERT, ServerConnection.GAME.isaac);
                message.bitPacket.p2_alt1(v1);
                message.bitPacket.p4_alt1(InterfaceManager.targetSlot);
                message.bitPacket.p2(InterfaceManager.targetInvObj);
                message.bitPacket.p1_alt3(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
                message.bitPacket.p2_alt3(InterfaceManager.targetComponent);
                ServerConnection.GAME.send(message);

                Static147.findPath(0, player.pathZ[0], player.getSize(), true, player.pathX[0], 0, -2, player.getSize());
            }
        }

        if (action == MiniMenuAction.TGT_SELF) {
            Static616.crossType = 2;
            Static305.crossY = clickY;
            Static481.crossDuration = 0;
            Static676.crossX = clickX;

            @Pc(147) ClientMessage message = ClientMessage.create(ClientProt.OPPLAYERT, ServerConnection.GAME.isaac);
            message.bitPacket.p2_alt1(PlayerEntity.self.slot);
            message.bitPacket.p4_alt1(InterfaceManager.targetSlot);
            message.bitPacket.p2(InterfaceManager.targetInvObj);
            message.bitPacket.p1_alt3(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
            message.bitPacket.p2_alt3(InterfaceManager.targetComponent);
            ServerConnection.GAME.send(message);
        }

        if (action == MiniMenuAction.TGT_BUTTON) {
            @Pc(197) Component button = InterfaceList.getComponent(v3, v2);

            if (button != null) {
                InterfaceManager.endTargetMode();

                @Pc(206) ServerActiveProperties properties = InterfaceManager.serverActiveProperties(button);
                InterfaceManager.enterTargetMode(properties.getTargetMask(), button, properties.targetParam);
                InterfaceManager.targetVerb = InterfaceManager.getComponentTargetVerb(button);
                InterfaceManager.targetedVerb = button.opBase + "<col=ffffff>";
                if (InterfaceManager.targetVerb == null) {
                    InterfaceManager.targetVerb = "Null";
                }
            }

            return;
        }

        if (action == MiniMenuAction.WALK) {
            if (Client.staffModLevel > 0 && KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) && KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_SHIFT)) {
                Static624.teleport(PlayerEntity.self.level, WorldMap.areaBaseZ + v3, WorldMap.areaBaseX + v2);
            } else {
                @Pc(147) ClientMessage message = Static32.moveMessage(v2, v3, v1);

                if (v1 == 1) {
                    message.bitPacket.p1(-1);
                    message.bitPacket.p1(-1);
                    message.bitPacket.p2((int) Camera.playerCameraYaw);
                    message.bitPacket.p1(57);
                    message.bitPacket.p1(Camera.yawOffset);
                    message.bitPacket.p1(Camera.scaleOffset);
                    message.bitPacket.p1(89);
                    message.bitPacket.p2(PlayerEntity.self.x);
                    message.bitPacket.p2(PlayerEntity.self.z);
                    message.bitPacket.p1(63);
                } else {
                    Static305.crossY = clickY;
                    Static616.crossType = 1;
                    Static481.crossDuration = 0;
                    Static676.crossX = clickX;
                }

                ServerConnection.GAME.send(message);

                Static147.findPath(0, v3, 1, true, v2, 0, -4, 1);
            }
        }

        if (action == MiniMenuAction.PAUSE_BUTTON && InterfaceManager.dialog == null) {
            sendResumePauseButton(v2, v3);
            InterfaceManager.dialog = InterfaceList.getComponent(v3, v2);
            InterfaceManager.redraw(InterfaceManager.dialog);
        }

        @Pc(389) ClientProt opPlayer = null;
        if (action == MiniMenuAction.OPPLAYER1) {
            opPlayer = ClientProt.OPPLAYER1;
        } else if (action == MiniMenuAction.OPPLAYER2) {
            opPlayer = ClientProt.OPPPLAYER2;
        } else if (action == MiniMenuAction.OPPLAYER3) {
            opPlayer = ClientProt.OPPLAYER3;
        } else if (action == MiniMenuAction.OPPLAYER4) {
            opPlayer = ClientProt.OPPLAYER4;
        } else if (action == MiniMenuAction.OPPLAYER5) {
            opPlayer = ClientProt.OPPLAYER5;
        } else if (action == MiniMenuAction.OPPLAYER6) {
            opPlayer = ClientProt.OPPLAYER6;
        } else if (action == MiniMenuAction.OPPLAYER7) {
            opPlayer = ClientProt.OPPLAYER7;
        } else if (action == MiniMenuAction.OPPLAYER8) {
            opPlayer = ClientProt.OPPLAYER8;
        } else if (action == MiniMenuAction.OPPLAYER9) {
            opPlayer = ClientProt.OPPLAYER9;
        } else if (action == MiniMenuAction.OPPLAYER10) {
            opPlayer = ClientProt.OPPLAYER10;
        }

        if (opPlayer != null) {
            @Pc(474) PlayerEntity player = PlayerList.highResolutionPlayers[v1];

            if (player != null) {
                Static481.crossDuration = 0;
                Static305.crossY = clickY;
                Static616.crossType = 2;
                Static676.crossX = clickX;

                @Pc(494) ClientMessage message = ClientMessage.create(opPlayer, ServerConnection.GAME.isaac);
                message.bitPacket.p1(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
                message.bitPacket.p2(v1);
                ServerConnection.GAME.send(message);

                Static147.findPath(0, player.pathZ[0], player.getSize(), true, player.pathX[0], 0, -2, player.getSize());
            }
        }

        @Pc(548) ClientProt opObj = null;
        if (action == MiniMenuAction.OPOBJ1) {
            opObj = ClientProt.OPOBJ1;
        } else if (action == MiniMenuAction.OPOBJ2) {
            opObj = ClientProt.OPOBJ2;
        } else if (action == MiniMenuAction.OPOBJ3) {
            opObj = ClientProt.OPOBJ3;
        } else if (action == MiniMenuAction.OPOBJ4) {
            opObj = ClientProt.OPOBJ4;
        } else if (action == MiniMenuAction.OPOBJ5) {
            opObj = ClientProt.OPOBJ5;
        } else if (action == MiniMenuAction.OPOBJ6) {
            opObj = ClientProt.OPOBJ6;
        }

        if (opObj != null) {
            Static305.crossY = clickY;
            Static481.crossDuration = 0;
            Static616.crossType = 2;
            Static676.crossX = clickX;

            @Pc(494) ClientMessage message = ClientMessage.create(opObj, ServerConnection.GAME.isaac);
            message.bitPacket.p2_alt2(v1);
            message.bitPacket.p1(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
            message.bitPacket.p2(v3 + WorldMap.areaBaseZ);
            message.bitPacket.p2_alt1(WorldMap.areaBaseX + v2);
            ServerConnection.GAME.send(message);

            Static414.findPathToObj(v3, v2);
        }

        if (action == MiniMenuAction.FACE_SQUARE) {
            if (Client.staffModLevel > 0 && KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) && KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_SHIFT)) {
                Static624.teleport(PlayerEntity.self.level, WorldMap.areaBaseZ + v3, WorldMap.areaBaseX + v2);
            } else {
                Static481.crossDuration = 0;
                Static676.crossX = clickX;
                Static616.crossType = 1;
                Static305.crossY = clickY;

                @Pc(494) ClientMessage message = ClientMessage.create(ClientProt.FACE_SQUARE, ServerConnection.GAME.isaac);
                message.bitPacket.p2_alt3(v3 + WorldMap.areaBaseZ);
                message.bitPacket.p2_alt1(WorldMap.areaBaseX + v2);
                ServerConnection.GAME.send(message);
            }
        }

        if (action == MiniMenuAction.IF_BUTTONT) {
            @Pc(741) Component button = InterfaceList.getComponent(v3, v2);

            if (button != null) {
                sendTargetButton(button);
            }
        }

        @Pc(750) ClientProt opNpc = null;
        if (action == MiniMenuAction.OPNPC1) {
            opNpc = ClientProt.OPNPC1;
        } else if (action == MiniMenuAction.OPNPC2) {
            opNpc = ClientProt.OPNPC2;
        } else if (action == MiniMenuAction.OPNPC3) {
            opNpc = ClientProt.OPNPC3;
        } else if (action == MiniMenuAction.OPNPC4) {
            opNpc = ClientProt.OPNPC4;
        } else if (action == MiniMenuAction.OPNPC5) {
            opNpc = ClientProt.OPNPC5;
        } else if (action == MiniMenuAction.OPNPC6) {
            opNpc = ClientProt.OPNPC6;
        }

        if (opNpc != null) {
            @Pc(806) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(v1);

            if (node != null) {
                Static676.crossX = clickX;
                @Pc(813) NPCEntity npc = node.npc;
                Static616.crossType = 2;
                Static305.crossY = clickY;
                Static481.crossDuration = 0;
                @Pc(831) ClientMessage message = ClientMessage.create(opNpc, ServerConnection.GAME.isaac);
                message.bitPacket.p1_alt1(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
                message.bitPacket.p2_alt2(v1);
                ServerConnection.GAME.send(message);

                Static147.findPath(0, npc.pathZ[0], npc.getSize(), true, npc.pathX[0], 0, -2, npc.getSize());
            }
        }

        @Pc(878) ClientProt opLoc = null;
        if (action == MiniMenuAction.OPLOC1) {
            opLoc = ClientProt.OPLOC1;
        } else if (action == MiniMenuAction.OPLOC2) {
            opLoc = ClientProt.OPLOC2;
        } else if (action == MiniMenuAction.OPLOC3) {
            opLoc = ClientProt.OPLOC3;
        } else if (action == MiniMenuAction.OPLOC4) {
            opLoc = ClientProt.OPLOC4;
        } else if (action == MiniMenuAction.OPLOC5) {
            opLoc = ClientProt.OPLOC5;
        } else if (action == MiniMenuAction.OPLOC6) {
            opLoc = ClientProt.OPLOC6;
        }

        if (opLoc != null) {
            Static305.crossY = clickY;
            Static616.crossType = 2;
            Static481.crossDuration = 0;
            Static676.crossX = clickX;

            @Pc(949) ClientMessage message = ClientMessage.create(opLoc, ServerConnection.GAME.isaac);
            message.bitPacket.p1_alt1(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
            message.bitPacket.p2_alt2(WorldMap.areaBaseX + v2);
            message.bitPacket.p2_alt3((int) (v1Long >>> 32) & Integer.MAX_VALUE);
            message.bitPacket.p2_alt1(v3 + WorldMap.areaBaseZ);
            ServerConnection.GAME.send(message);

            Static38.findPathToLoc(v2, v1Long, v3);
        }

        if (action == MiniMenuAction.OP_MAPELEMENT1 || action == MiniMenuAction.OP_MAPELEMENT2 || action == MiniMenuAction.OP_MAPELEMENT3 || action == MiniMenuAction.OP_MAPELEMENT4 || action == MiniMenuAction.OP_MAPELEMENT5) {
            ScriptRunner.executeMapElementTrigger(v2, v1, action);
        }

        if (action == MiniMenuAction.TGT_GROUND) {
            Static676.crossX = clickX;
            Static616.crossType = 1;
            Static305.crossY = clickY;
            Static481.crossDuration = 0;

            @Pc(949) ClientMessage message = ClientMessage.create(ClientProt.APCOORDT, ServerConnection.GAME.isaac);
            message.bitPacket.p2_alt2(WorldMap.areaBaseX + v2);
            message.bitPacket.p2_alt1(InterfaceManager.targetInvObj);
            message.bitPacket.p4_alt2(InterfaceManager.targetSlot);
            message.bitPacket.p2_alt2(WorldMap.areaBaseZ + v3);
            message.bitPacket.p2(InterfaceManager.targetComponent);
            ServerConnection.GAME.send(message);

            Static147.findPath(0, v3, 1, true, v2, 0, -4, 1);
        }

        if (action == MiniMenuAction.TGT_OBJ) {
            Static616.crossType = 2;
            Static481.crossDuration = 0;
            Static305.crossY = clickY;
            Static676.crossX = clickX;

            @Pc(949) ClientMessage message = ClientMessage.create(ClientProt.OPOBJT, ServerConnection.GAME.isaac);
            message.bitPacket.p2(v2 + WorldMap.areaBaseX);
            message.bitPacket.p2(WorldMap.areaBaseZ + v3);
            message.bitPacket.p2_alt3(InterfaceManager.targetInvObj);
            message.bitPacket.p4_alt3(InterfaceManager.targetSlot);
            message.bitPacket.p2_alt1(InterfaceManager.targetComponent);
            message.bitPacket.p1(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
            message.bitPacket.p2_alt1(v1);
            ServerConnection.GAME.send(message);

            Static414.findPathToObj(v3, v2);
        }

        if (action == MiniMenuAction.TGT_NPC) {
            @Pc(1200) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(v1);

            if (node != null) {
                @Pc(1205) NPCEntity npc = node.npc;

                Static481.crossDuration = 0;
                Static616.crossType = 2;
                Static676.crossX = clickX;
                Static305.crossY = clickY;

                @Pc(1223) ClientMessage message = ClientMessage.create(ClientProt.OPNPCT, ServerConnection.GAME.isaac);
                message.bitPacket.p2_alt3(InterfaceManager.targetComponent);
                message.bitPacket.p2_alt1(InterfaceManager.targetInvObj);
                message.bitPacket.p2_alt1(v1);
                message.bitPacket.p4_alt3(InterfaceManager.targetSlot);
                message.bitPacket.p1(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
                ServerConnection.GAME.send(message);

                Static147.findPath(0, npc.pathZ[0], npc.getSize(), true, npc.pathX[0], 0, -2, npc.getSize());
            }
        }

        if (action == MiniMenuAction.IF_BUTTONX1 || action == MiniMenuAction.IF_BUTTONX2) {
            InterfaceManager.ifButtonXSend(v3, v2, entry.opBase, v1);
        }

        if (action == MiniMenuAction.TGT_LOC) {
            Static676.crossX = clickX;
            Static481.crossDuration = 0;
            Static305.crossY = clickY;
            Static616.crossType = 2;

            @Pc(949) ClientMessage message = ClientMessage.create(ClientProt.OPLOCT, ServerConnection.GAME.isaac);
            message.bitPacket.p1_alt2(KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL) ? 1 : 0);
            message.bitPacket.p2_alt1(WorldMap.areaBaseZ + v3);
            message.bitPacket.p2_alt1(InterfaceManager.targetComponent);
            message.bitPacket.p4_alt1(InterfaceManager.targetSlot);
            message.bitPacket.p2_alt3(InterfaceManager.targetInvObj);
            message.bitPacket.p2_alt1(v2 + WorldMap.areaBaseX);
            message.bitPacket.p2_alt2(Integer.MAX_VALUE & (int) (v1Long >>> 32));
            ServerConnection.GAME.send(message);

            Static38.findPathToLoc(v2, v1Long, v3);
        }

        if (InterfaceManager.targetMode) {
            InterfaceManager.endTargetMode();
        }

        if (Static67.aComponent_10 != null && Static499.anInt7501 == 0) {
            InterfaceManager.redraw(Static67.aComponent_10);
        }
    }

    @OriginalMember(owner = "client!pc", name = "a", descriptor = "(IZI)V")
    public static void sendResumePauseButton(@OriginalArg(0) int slot, @OriginalArg(2) int id) {
        @Pc(13) ClientMessage message = ClientMessage.create(ClientProt.RESUME_PAUSEBUTTON, ServerConnection.GAME.isaac);
        message.bitPacket.p4_alt3(id);
        message.bitPacket.p2_alt3(slot);
        ServerConnection.GAME.send(message);
    }

    @OriginalMember(owner = "client!jt", name = "a", descriptor = "(BLclient!hda;)V")
    public static void sendTargetButton(@OriginalArg(1) Component button) {
        if (!InterfaceManager.targetMode) {
            return;
        }

        if (button.onOpT != null) {
            @Pc(17) Component target = InterfaceList.getComponent(InterfaceManager.targetSlot, InterfaceManager.targetComponent);

            if (target != null) {
                @Pc(23) HookRequest request = new HookRequest();
                request.arguments = button.onOpT;
                request.source = button;
                request.target = target;
                ScriptRunner.executeHookInner(request);
            }
        }

        @Pc(45) ClientMessage message = ClientMessage.create(ClientProt.IF_BUTTONT, ServerConnection.GAME.isaac);
        message.bitPacket.p4_alt2(button.slot);
        message.bitPacket.p2_alt2(InterfaceManager.targetInvObj);
        message.bitPacket.p2_alt3(InterfaceManager.targetComponent);
        message.bitPacket.p4_alt3(InterfaceManager.targetSlot);
        message.bitPacket.p2_alt2(button.invObject);
        message.bitPacket.p2_alt1(button.id);
        ServerConnection.GAME.send(message);
    }

    @OriginalMember(owner = "client!kca", name = "a", descriptor = "(II)Z")
    public static boolean hasNpcOp(@OriginalArg(0) int slot) {
        for (@Pc(8) MiniMenuEntryInner entry = (MiniMenuEntryInner) innerEntryQueue.first(); entry != null; entry = (MiniMenuEntryInner) innerEntryQueue.next()) {
            if (MiniMenuAction.isNpcOp(entry.action) && entry.v1 == (long) slot) {
                return true;
            }
        }
        return false;
    }

    @OriginalMember(owner = "client!pga", name = "c", descriptor = "(I)V")
    public static void close() {
        closeOpenedEntry();
        open = false;
        InterfaceManager.redrawWithin(x, y, width, height);
    }

    @OriginalMember(owner = "client!kh", name = "b", descriptor = "(B)V")
    public static void closeOpenedEntry() {
        if (openedEntry != null) {
            openedEntry = null;
            InterfaceManager.redrawWithin(openedEntryX, openedEntryY, openedEntryWidth, openedEntryHeight);
        }
    }

    @OriginalMember(owner = "client!vha", name = "a", descriptor = "(Lclient!ha;Z)V")
    public static void draw(@OriginalArg(0) Toolkit toolkit) {
        if (open) {
            drawFull(toolkit);
        } else {
            drawPreview(toolkit);
        }
    }

    @OriginalMember(owner = "client!bf", name = "a", descriptor = "(ILclient!ha;)V")
    public static void drawFull(@OriginalArg(1) Toolkit toolkit) {
        if (useSprites) {
            drawWithSprites(toolkit);
        } else {
            drawWithoutSprites(toolkit);
        }
    }

    @OriginalMember(owner = "client!ema", name = "a", descriptor = "(Lclient!ha;I)V")
    public static void drawWithSprites(@OriginalArg(0) Toolkit toolkit) {
        @Pc(5) int offsetX = 0;
        @Pc(7) int offsetY = 0;
        if (OrthoMode.toolkitActive) {
            offsetX = OrthoMode.method2283();
            offsetY = Static422.method5771();
        }

        @Pc(19) int menuX = x + offsetX;
        @Pc(23) int menuY = y + offsetY;
        @Pc(25) int menuWidth = width;
        @Pc(29) int menuHeight = height - 3;

        drawTop(width, height, LocalisedText.CHOOSEOPTION.localise(Client.language), toolkit, offsetY + y, x + offsetX);

        @Pc(55) int mouseX = MouseMonitor.instance.getRecordedX() + offsetX;
        @Pc(66) int mouseY = MouseMonitor.instance.getRecordedY() + offsetY;

        if (collapsed) {
            @Pc(70) int count = 0;

            for (@Pc(77) MiniMenuEntry entry = (MiniMenuEntry) entryQueue.first(); entry != null; entry = (MiniMenuEntry) entryQueue.next()) {
                @Pc(89) int entryY = menuY + (count * ENTRY_HEIGHT) + 13 + 20;

                if (mouseX > offsetX + x && mouseX < offsetX + x + width && mouseY > entryY - 13 && mouseY < entryY + 4 && (entry.size > 1 || ((MiniMenuEntryInner) entry.innerEntries.sentinel.next2).aBoolean552)) {
                    toolkit.aa(offsetX + x, entryY - 12, width, ENTRY_HEIGHT, ((255 - spriteBodyOpacity) << 24) | spriteBodyColour, 1);
                }

                count++;
            }

            if (openedEntry != null) {
                drawTop(openedEntryWidth, openedEntryHeight, openedEntry.title, toolkit, openedEntryY, openedEntryX);

                count = 0;
                for (@Pc(190) MiniMenuEntryInner inner = (MiniMenuEntryInner) openedEntry.innerEntries.first(); inner != null; inner = (MiniMenuEntryInner) openedEntry.innerEntries.next()) {
                    @Pc(202) int innerY = openedEntryY + (count * ENTRY_HEIGHT) + 13 + 20;

                    if (openedEntryX < mouseX && openedEntryX + openedEntryWidth > mouseX && mouseY > innerY - 13 && mouseY < innerY + 4 && inner.aBoolean552) {
                        toolkit.aa(openedEntryX, innerY - 12, openedEntryWidth, ENTRY_HEIGHT, 255 - spriteBodyOpacity << 24 | spriteBodyColour, 1);
                    }

                    count++;
                }

                drawBorder(openedEntryY, toolkit, openedEntryX, openedEntryWidth, openedEntryHeight);
            }
        } else {
            @Pc(70) int count = 0;

            for (@Pc(281) MiniMenuEntryInner inner = (MiniMenuEntryInner) innerEntryQueue.first(); inner != null; inner = (MiniMenuEntryInner) innerEntryQueue.next()) {
                @Pc(202) int innerY = ((innerEntryCount - count - 1) * ENTRY_HEIGHT) + menuY + SPRITE_TOP_HEIGHT;
                count++;

                if (offsetX + x < mouseX && offsetX + x + width > mouseX && mouseY > innerY - 13 && innerY + 4 > mouseY && inner.aBoolean552) {
                    toolkit.aa(offsetX + x, innerY - 12, width, ENTRY_HEIGHT, spriteBodyColour | 255 - spriteBodyOpacity << 24, 1);
                }
            }
        }

        drawBorder(offsetY + y, toolkit, x + offsetX, width, height);

        if (collapsed) {
            @Pc(70) int count = 0;

            for (@Pc(77) MiniMenuEntry entry = (MiniMenuEntry) entryQueue.first(); entry != null; entry = (MiniMenuEntry) entryQueue.next()) {
                @Pc(202) int entryY = (count * ENTRY_HEIGHT) + offsetY + y + SPRITE_TOP_HEIGHT;

                if (entry.size == 1) {
                    drawEntryInner(toolkit, (MiniMenuEntryInner) entry.innerEntries.sentinel.next2, x + offsetX, y + offsetY, width, height, mouseX, mouseY, textColour | 0xFF000000, spriteHighlightColour | 0xFF000000, entryY);
                } else {
                    drawEntry(toolkit, entry, offsetX + x, entryY, width, height, mouseX, mouseY, textColour | 0xFF000000, spriteHighlightColour | 0xFF000000, offsetY + y);
                }

                count++;
            }

            if (openedEntry != null) {
                count = 0;

                for (@Pc(190) MiniMenuEntryInner inner = (MiniMenuEntryInner) openedEntry.innerEntries.first(); inner != null; inner = (MiniMenuEntryInner) openedEntry.innerEntries.next()) {
                    @Pc(202) int innerY = openedEntryY + (count * ENTRY_HEIGHT) + 20 + 13;
                    drawEntryInner(toolkit, inner, openedEntryX, openedEntryY, openedEntryWidth, openedEntryHeight, mouseX, mouseY, textColour | 0xFF000000, spriteHighlightColour | 0xFF000000, innerY);
                    count++;
                }

                InterfaceManager.flipDirtyRectWithin(openedEntryX, openedEntryY, openedEntryWidth, openedEntryHeight);
            }
        } else {
            @Pc(70) int count = 0;

            for (@Pc(281) MiniMenuEntryInner inner = (MiniMenuEntryInner) innerEntryQueue.first(); inner != null; inner = (MiniMenuEntryInner) innerEntryQueue.next()) {
                @Pc(89) int innerY = menuY + (innerEntryCount - count - 1) * ENTRY_HEIGHT + 13 + 20;
                count++;
                drawEntryInner(toolkit, inner, menuX, menuY, menuWidth, menuHeight, mouseX, mouseY, textColour | 0xFF000000, spriteHighlightColour | 0xFF000000, innerY);
            }
        }

        InterfaceManager.flipDirtyRectWithin(x + offsetX, y + offsetY, width, height);
    }

    @OriginalMember(owner = "client!ew", name = "a", descriptor = "(Lclient!ha;B)V")
    public static void drawWithoutSprites(@OriginalArg(0) Toolkit toolkit) {
        @Pc(7) int offsetX = 0;
        @Pc(9) int offsetY = 0;
        if (OrthoMode.toolkitActive) {
            offsetX = OrthoMode.method2283();
            offsetY = Static422.method5771();
        }

        drawFrame(toolkit, x + offsetX, y + offsetY, width, height);
        Fonts.b12.render(LocalisedText.CHOOSEOPTION.localise(Client.language), x + offsetX + 3, y + offsetY + 14, 0xFFFFFFFF, 0xFF5D5447);

        @Pc(69) int mouseX = MouseMonitor.instance.getRecordedX() + offsetX;
        @Pc(76) int mouseY = MouseMonitor.instance.getRecordedY() + offsetY;

        if (collapsed) {
            @Pc(80) int count = 0;

            for (@Pc(137) MiniMenuEntry inner = (MiniMenuEntry) entryQueue.first(); inner != null; inner = (MiniMenuEntry) entryQueue.next()) {
                @Pc(101) int innerY = offsetY + y + (count * ENTRY_HEIGHT) + 31;

                if (inner.size == 1) {
                    drawEntryInner(toolkit, (MiniMenuEntryInner) inner.innerEntries.sentinel.next2, offsetX + x, y + offsetY, width, height, mouseX, mouseY, -1, -256, innerY);
                } else {
                    drawEntry(toolkit, inner, offsetX + x, innerY, width, height, mouseX, mouseY, -1, -256, y + offsetY);
                }

                count++;
            }

            if (openedEntry != null) {
                drawFrame(toolkit, openedEntryX, openedEntryY, openedEntryWidth, openedEntryHeight);

                count = 0;
                Fonts.b12.render(openedEntry.title, openedEntryX + 3, openedEntryY + 14, -1, 0xFF5D5447);

                for (@Pc(239) MiniMenuEntryInner inner = (MiniMenuEntryInner) openedEntry.innerEntries.first(); inner != null; inner = (MiniMenuEntryInner) openedEntry.innerEntries.next()) {
                    @Pc(251) int innerY = (count * ENTRY_HEIGHT) + openedEntryY + 31;
                    count++;
                    drawEntryInner(toolkit, inner, openedEntryX, openedEntryY, openedEntryWidth, openedEntryHeight, mouseX, mouseY, -1, -256, innerY);
                }

                InterfaceManager.flipDirtyRectWithin(openedEntryX, openedEntryY, openedEntryWidth, openedEntryHeight);
            }
        } else {
            @Pc(80) int count = 0;

            for (@Pc(85) MiniMenuEntryInner inner = (MiniMenuEntryInner) innerEntryQueue.first(); inner != null; inner = (MiniMenuEntryInner) innerEntryQueue.next()) {
                @Pc(101) int innerY = ((innerEntryCount - count - 1) * ENTRY_HEIGHT) + offsetY + y + 31;
                count++;
                drawEntryInner(toolkit, inner, offsetX + x, y + offsetY, width, height, mouseX, mouseY, -1, -256, innerY);
            }
        }

        InterfaceManager.flipDirtyRectWithin(offsetX + x, offsetY + y, width, height);
    }

    @OriginalMember(owner = "client!qda", name = "a", descriptor = "(IIIIILclient!ha;IIIIILclient!cba;)V")
    public static void drawEntry(@OriginalArg(5) Toolkit toolkit, @OriginalArg(11) MiniMenuEntry inner, @OriginalArg(9) int x, @OriginalArg(2) int y, @OriginalArg(1) int width, @OriginalArg(10) int height, @OriginalArg(6) int mouseX, @OriginalArg(0) int mouseY, @OriginalArg(7) int colour, @OriginalArg(4) int highlightColour, @OriginalArg(8) int entryHeight) {
        if (mouseX > x && mouseX < x + width && mouseY > y - 13 && mouseY < y + 3) {
            colour = highlightColour;
        }

        @Pc(41) String text = entryTitle(inner);
        Fonts.b12.render(text, x + 3, y, colour, 0, icons, iconHeights);
    }

    @OriginalMember(owner = "client!fu", name = "a", descriptor = "(Lclient!cba;B)Ljava/lang/String;")
    public static String entryTitle(@OriginalArg(0) MiniMenuEntry entry) {
        return entry.title + " <col=ffffff>>";
    }

    @OriginalMember(owner = "client!cn", name = "a", descriptor = "(IILjava/lang/String;Lclient!ha;BIII)V")
    public static void drawTop(@OriginalArg(0) int width, @OriginalArg(1) int height, @OriginalArg(2) String text, @OriginalArg(3) Toolkit toolkit, @OriginalArg(5) int y, @OriginalArg(6) int x) {
        if (separatorSprite == null || topLeftCornerSprite == null) {
            if (js5.SPRITES.fileready(separatorSpriteId) && js5.SPRITES.fileready(topCornerSpriteId)) {
                separatorSprite = toolkit.createSprite(IndexedImage.loadFirst(js5.SPRITES, separatorSpriteId, 0), true);

                @Pc(49) IndexedImage corner = IndexedImage.loadFirst(js5.SPRITES, topCornerSpriteId, 0);
                topLeftCornerSprite = toolkit.createSprite(corner, true);
                corner.flipVertically();
                topRightCornerSprite = toolkit.createSprite(corner, true);
            } else {
                toolkit.aa(x, y, width, 20, topColour | ((255 - topOpacity) << 24), 1);
            }
        }

        if (separatorSprite != null && topLeftCornerSprite != null) {
            @Pc(82) int tiles = (width - topLeftCornerSprite.getWidth() * 2) / separatorSprite.getWidth();
            for (@Pc(84) int i = 0; i < tiles; i++) {
                separatorSprite.render(x + topLeftCornerSprite.getWidth() + i * separatorSprite.getWidth(), y);
            }

            topLeftCornerSprite.render(x, y);
            topRightCornerSprite.render(width + x - topRightCornerSprite.getWidth(), y);
        }

        Fonts.b12.render(text, x + 3, y + 14, -1, 0xFF000000 | textColour);
        toolkit.aa(x, y + 20, width, height - 20, ((0xFF - topOpacity) << 24) | topColour, 1);
    }

    @OriginalMember(owner = "client!jea", name = "a", descriptor = "(ILclient!ha;IIIBI)V")
    public static void drawBorder(@OriginalArg(0) int y, @OriginalArg(1) Toolkit toolkit, @OriginalArg(2) int x, @OriginalArg(3) int width, @OriginalArg(6) int height) {
        if ((bottomBorderSprite == null || leftBorderSprite == null || bottomLeftCornerSprite == null) && js5.SPRITES.fileready(horizontalBorderSpriteId) && js5.SPRITES.fileready(verticalBorderSpriteId) && js5.SPRITES.fileready(bottomCornerSpriteId)) {
            @Pc(46) IndexedImage border = IndexedImage.loadFirst(js5.SPRITES, verticalBorderSpriteId, 0);
            leftBorderSprite = toolkit.createSprite(border, true);
            border.flipVertically();
            rightBorderSprite = toolkit.createSprite(border, true);

            bottomBorderSprite = toolkit.createSprite(IndexedImage.loadFirst(js5.SPRITES, horizontalBorderSpriteId, 0), true);

            @Pc(71) IndexedImage corner = IndexedImage.loadFirst(js5.SPRITES, bottomCornerSpriteId, 0);
            bottomLeftCornerSprite = toolkit.createSprite(corner, true);
            corner.flipVertically();
            bottomRightCornerSprite = toolkit.createSprite(corner, true);
        }

        if (bottomBorderSprite != null && leftBorderSprite != null && bottomLeftCornerSprite != null) {
            @Pc(103) int bottomTiles = (width - bottomLeftCornerSprite.getWidth() * 2) / bottomBorderSprite.getWidth();
            for (@Pc(105) int i = 0; i < bottomTiles; i++) {
                bottomBorderSprite.render(bottomLeftCornerSprite.getWidth() + x + bottomBorderSprite.getWidth() * i, -bottomBorderSprite.getHeight() + height + y);
            }

            @Pc(145) int sideTiles = (height - bottomLeftCornerSprite.getHeight() - 20) / leftBorderSprite.getHeight();
            for (@Pc(147) int i = 0; i < sideTiles; i++) {
                leftBorderSprite.render(x, y + leftBorderSprite.getHeight() * i + 20);
                rightBorderSprite.render(width + x - rightBorderSprite.getWidth(), y + 20 + leftBorderSprite.getHeight() * i);
            }

            bottomLeftCornerSprite.render(x, y + height - bottomLeftCornerSprite.getHeight());
            bottomRightCornerSprite.render(width + x - bottomLeftCornerSprite.getWidth(), height + (y - bottomLeftCornerSprite.getHeight()));
        }
    }

    @OriginalMember(owner = "client!daa", name = "a", descriptor = "(IZIIILclient!ha;II)V")
    public static void drawFrame(@OriginalArg(5) Toolkit toolkit, @OriginalArg(2) int x, @OriginalArg(0) int y, @OriginalArg(7) int width, @OriginalArg(3) int height) {
        toolkit.fillRect(x, y, width, height, 0xFF5D5447);
        toolkit.fillRect(x + 1, y + 1, width - 2, ENTRY_HEIGHT, 0xFF000000);
        toolkit.outlineRect(x + 1, y + ENTRY_HEIGHT + 2, width - 2, height - ENTRY_HEIGHT - 3, 0xFF000000);
    }

    @OriginalMember(owner = "client!kc", name = "a", descriptor = "(Z)V")
    public static void update() {
        if (!open) {
            collapsed = ((subMenuMinLength != -1) && (innerEntryCount >= subMenuMinLength)) || (((innerEntryCount * ENTRY_HEIGHT) + (useSprites ? 26 : 22)) > GameShell.canvasHei);
        }

        otherInnerEntries.clear();
        targetInnerEntries.clear();

        for (@Pc(57) MiniMenuEntryInner inner = (MiniMenuEntryInner) innerEntryQueue.first(); inner != null; inner = (MiniMenuEntryInner) innerEntryQueue.next()) {
            @Pc(64) int action = inner.action;
            if (action < 1000) {
                inner.unlink();

                if (action == MiniMenuAction.TGT_GROUND || action == MiniMenuAction.TGT_LOC || action == MiniMenuAction.TGT_NPC || action == MiniMenuAction.TGT_OBJ || action == MiniMenuAction.TGT_PLAYER || action == MiniMenuAction.TGT_SELF || action == MiniMenuAction.IF_BUTTONT) {
                    targetInnerEntries.addLast(inner);
                } else {
                    otherInnerEntries.addLast(inner);
                }
            }
        }

        otherInnerEntries.appendTo(innerEntryQueue);
        targetInnerEntries.appendTo(innerEntryQueue);

        if (innerEntryCount <= 1) {
            activeEntry = null;
            topEntry = null;
        } else {
            if (shiftClick && KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_SHIFT) && innerEntryCount > 2) {
                activeEntry = (MiniMenuEntryInner) innerEntryQueue.sentinel.prev.prev;
            } else {
                activeEntry = (MiniMenuEntryInner) innerEntryQueue.sentinel.prev;
            }

            topEntry = (MiniMenuEntryInner) innerEntryQueue.sentinel.prev;
        }

        @Pc(64) int mouseLogType = -1;
        @Pc(204) MouseLog log = (MouseLog) Static226.mouseLogs.first();
        if (log != null) {
            mouseLogType = log.getType();
        }

        if (!open) {
            if (mouseLogType == MouseLog.TYPE_PRESS_LEFT && (Client.mouseButtons == 1 && innerEntryCount > 2 || topEntryIsIfButtonX1())) {
                mouseLogType = MouseLog.TYPE_PRESS_RIGHT;
            }

            if (mouseLogType == MouseLog.TYPE_PRESS_RIGHT && innerEntryCount > 0 && log != null) {
                if (InterfaceManager.dragSource == null && anInt6964 == 0) {
                    openAt(log.getX(), log.getY());
                } else {
                    anInt8149 = 2;
                }
            }

            if (mouseLogType == MouseLog.TYPE_PRESS_LEFT) {
                if (activeEntry != null) {
                    method5628();
                } else if (InterfaceManager.targetMode) {
                    InterfaceManager.endTargetMode();
                }
            }

            if (InterfaceManager.dragSource == null && anInt6964 == 0) {
                draggedEntry = null;
                anInt8149 = 0;
            }
        } else if (mouseLogType == MouseLog.TYPE_RESET) {
            @Pc(317) int mouseX = MouseMonitor.instance.getRecordedX();
            @Pc(321) int mouseY = MouseMonitor.instance.getRecordedY();

            @Pc(323) boolean openEntry = false;
            if (openedEntry != null) {
                if (mouseX >= openedEntryX - 10 && mouseX <= openedEntryX + openedEntryWidth + 10 && openedEntryY - 10 <= mouseY && mouseY <= openedEntryY + openedEntryHeight + 10) {
                    openEntry = true;
                } else {
                    closeOpenedEntry();
                }
            }

            if (!openEntry) {
                if (x - 10 > mouseX || x + width + 10 < mouseX || y - 10 > mouseY || mouseY > height + y + 10) {
                    close();
                } else if (collapsed) {
                    @Pc(426) int openEntryIndex = -1;
                    @Pc(428) int openEntryY = -1;

                    for (@Pc(430) int i = 0; i < entryCount; i++) {
                        if (useSprites) {
                            @Pc(444) int entryY = (i * ENTRY_HEIGHT) + y + SPRITE_TOP_HEIGHT;

                            if (mouseY > entryY - 13 && entryY + 4 > mouseY) {
                                openEntryIndex = i;
                                openEntryY = entryY - 13;
                                break;
                            }
                        } else {
                            @Pc(444) int entryY = (i * ENTRY_HEIGHT) + y + TOP_HEIGHT;

                            if (entryY - 13 < mouseY && mouseY < entryY + 3) {
                                openEntryY = entryY - 13;
                                openEntryIndex = i;
                                break;
                            }
                        }
                    }

                    if (openEntryIndex != -1) {
                        @Pc(444) int index = 0;
                        @Pc(525) QueueIterator iterator = new QueueIterator(entryQueue);

                        for (@Pc(530) MiniMenuEntry entry = (MiniMenuEntry) iterator.first(); entry != null; entry = (MiniMenuEntry) iterator.next()) {
                            if (index == openEntryIndex) {
                                if (entry.size > 1) {
                                    openEntry(entry, openEntryY, mouseY);
                                }
                                break;
                            }

                            index++;
                        }
                    }
                }
            }
        } else if (mouseLogType == MouseLog.TYPE_PRESS_LEFT) {
            @Pc(317) int mouseX = log.getX();
            @Pc(321) int mouseY = log.getY();

            if (openedEntry != null && openedEntryX <= mouseX && openedEntryWidth + openedEntryX >= mouseX && mouseY >= openedEntryY && openedEntryY + openedEntryHeight >= mouseY) {
                @Pc(661) int openEntryIndex = -1;

                for (@Pc(426) int i = 0; i < openedEntry.size; i++) {
                    if (useSprites) {
                        @Pc(428) int entryY = (i * ENTRY_HEIGHT) + openedEntryY + SPRITE_TOP_HEIGHT;

                        if (mouseY > entryY - 13 && mouseY < entryY + 4) {
                            openEntryIndex = i;
                        }
                    } else {
                        @Pc(428) int openEntryY = (i * ENTRY_HEIGHT) + openedEntryY + TOP_HEIGHT;

                        if (mouseY > openEntryY - 13 && mouseY < openEntryY + 3) {
                            openEntryIndex = i;
                        }
                    }
                }

                if (openEntryIndex != -1) {
                    @Pc(428) int index = 0;
                    @Pc(886) QueueIterator iterator = new QueueIterator(openedEntry.innerEntries);

                    for (@Pc(762) MiniMenuEntryInner inner = (MiniMenuEntryInner) iterator.first(); inner != null; inner = (MiniMenuEntryInner) iterator.next()) {
                        if (openEntryIndex == index) {
                            doAction(mouseY, inner, mouseX);
                            break;
                        }

                        index++;
                    }
                }

                close();
                return;
            }

            if (x > mouseX || x + width < mouseX || y > mouseY || mouseY > height + y) {
                return;
            }

            if (!collapsed) {
                @Pc(661) int innerEntryIndex = -1;
                for (@Pc(426) int i = 0; i < innerEntryCount; i++) {
                    if (useSprites) {
                        @Pc(428) int innerEntryY = y + SPRITE_TOP_HEIGHT + ((innerEntryCount - i - 1) * ENTRY_HEIGHT);

                        if (mouseY > innerEntryY - 13 && mouseY < innerEntryY + 4) {
                            innerEntryIndex = i;
                        }
                    } else {
                        @Pc(428) int innerEntryY = y + TOP_HEIGHT + ((innerEntryCount - i - 1) * ENTRY_HEIGHT);

                        if (mouseY > innerEntryY - 13 && mouseY < innerEntryY + 3) {
                            innerEntryIndex = i;
                        }
                    }
                }

                if (innerEntryIndex != -1) {
                    @Pc(428) int index = 0;
                    @Pc(757) DequeIterator iterator = new DequeIterator(innerEntryQueue);

                    for (@Pc(762) MiniMenuEntryInner inner = (MiniMenuEntryInner) iterator.first(); inner != null; inner = (MiniMenuEntryInner) iterator.next()) {
                        if (innerEntryIndex == index) {
                            doAction(mouseY, inner, mouseX);
                            break;
                        }
                        index++;
                    }
                }

                close();
            } else {
                @Pc(661) int entryIndex = -1;

                for (@Pc(426) int i = 0; i < entryCount; i++) {
                    if (useSprites) {
                        @Pc(428) int entryY = y + SPRITE_TOP_HEIGHT + (i * ENTRY_HEIGHT);

                        if (entryY - 13 < mouseY && mouseY < entryY + 4) {
                            entryIndex = i;
                            break;
                        }
                    } else {
                        @Pc(428) int entryY = y + TOP_HEIGHT + (i * ENTRY_HEIGHT);

                        if (mouseY > entryY - 13 && mouseY < entryY + 3) {
                            entryIndex = i;
                            break;
                        }
                    }
                }

                if (entryIndex != -1) {
                    @Pc(428) int index = 0;
                    @Pc(886) QueueIterator iterator = new QueueIterator(entryQueue);

                    for (@Pc(891) MiniMenuEntry entry = (MiniMenuEntry) iterator.first(); entry != null; entry = (MiniMenuEntry) iterator.next()) {
                        if (index == entryIndex) {
                            doAction(mouseY, (MiniMenuEntryInner) entry.innerEntries.sentinel.next2, mouseX);
                            close();
                            break;
                        }

                        index++;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!s", name = "b", descriptor = "(III)V")
    public static void openAt(@OriginalArg(2) int x, @OriginalArg(1) int y) {
        @Pc(11) int menuWidth = Fonts.b12Metrics.stringWidth(LocalisedText.CHOOSEOPTION.localise(Client.language));
        @Pc(68) int menuHeight;

        if (collapsed) {
            for (@Pc(18) MiniMenuEntry entry = (MiniMenuEntry) entryQueue.first(); entry != null; entry = (MiniMenuEntry) entryQueue.next()) {
                @Pc(27) int width;
                if (entry.size == 1) {
                    width = getLineWidth((MiniMenuEntryInner) entry.innerEntries.sentinel.next2);
                } else {
                    width = getEntryWidth(entry);
                }

                if (width > menuWidth) {
                    menuWidth = width;
                }
            }

            menuWidth += 8;
            height = (useSprites ? 26 : 22) + (entryCount * ENTRY_HEIGHT);
            menuHeight = (entryCount * ENTRY_HEIGHT) + 21;
        } else {
            for (@Pc(74) MiniMenuEntryInner inner = (MiniMenuEntryInner) innerEntryQueue.first(); inner != null; inner = (MiniMenuEntryInner) innerEntryQueue.next()) {
                @Pc(27) int local27 = getLineWidth(inner);
                if (menuWidth < local27) {
                    menuWidth = local27;
                }
            }

            menuWidth += 8;
            height = (useSprites ? 26 : 22) + (innerEntryCount * ENTRY_HEIGHT);
            menuHeight = (innerEntryCount * ENTRY_HEIGHT) + 21;
        }

        @Pc(118) int menuX = x - menuWidth / 2;
        if (menuWidth + menuX > GameShell.canvasWid) {
            menuX = GameShell.canvasWid - menuWidth;
        }
        if (menuX < 0) {
            menuX = 0;
        }

        @Pc(146) int menuY = y;
        if (y + menuHeight > GameShell.canvasHei) {
            menuY = GameShell.canvasHei - menuHeight;
        }
        if (menuY < 0) {
            menuY = 0;
        }

        MiniMenu.x = menuX;
        MiniMenu.open = true;
        MiniMenu.y = menuY;
        MiniMenu.width = menuWidth;
    }

    @OriginalMember(owner = "client!fu", name = "a", descriptor = "(Lclient!cba;I)I")
    public static int getEntryWidth(@OriginalArg(0) MiniMenuEntry entry) {
        @Pc(14) String text = entryTitle(entry);
        return Fonts.b12Metrics.stringWidth(icons, text);
    }

    @OriginalMember(owner = "client!tea", name = "a", descriptor = "(IIILclient!cba;)V")
    public static void openEntry(@OriginalArg(3) MiniMenuEntry entry, @OriginalArg(0) int y, @OriginalArg(2) int mouseY) {
        if (!open) {
            return;
        }

        @Pc(11) int entryWidth = 0;
        for (@Pc(17) MiniMenuEntryInner inner = (MiniMenuEntryInner) entry.innerEntries.first(); inner != null; inner = (MiniMenuEntryInner) entry.innerEntries.next()) {
            @Pc(23) int lineWidth = getLineWidth(inner);

            if (lineWidth > entryWidth) {
                entryWidth = lineWidth;
            }
        }
        entryWidth += 8;

        @Pc(23) int entryHeight = (entry.size * ENTRY_HEIGHT) + 21;
        openedEntryHeight = (useSprites ? 26 : 22) + (entry.size * ENTRY_HEIGHT);

        @Pc(71) int entryX = entryWidth + x;
        if (entryX + entryWidth > GameShell.canvasWid) {
            entryX = x - entryWidth;
        }
        if (entryX < 0) {
            entryX = 0;
        }

        @Pc(91) int topHeight = useSprites ? SPRITE_TOP_HEIGHT : TOP_HEIGHT;
        @Pc(98) int entryY = (y + 13) - topHeight;
        if (GameShell.canvasHei < entryHeight + entryY) {
            entryY = GameShell.canvasHei - entryHeight;
        }
        if (entryY < 0) {
            entryY = 0;
        }

        openedEntryX = entryX;
        openedEntryWidth = entryWidth;
        openedEntry = entry;
        openedEntryY = entryY;
    }

    @OriginalMember(owner = "client!vj", name = "a", descriptor = "(Z)I")
    public static int cursor() {
        if (InterfaceManager.dragSource != null) {
            return -1;
        }

        if (!open && activeEntry != null) {
            return activeEntry.cursor;
        }

        @Pc(28) int mouseX = MouseMonitor.instance.getRecordedX();
        @Pc(37) int mouseY = MouseMonitor.instance.getRecordedY();

        if (collapsed) {
            if (mouseX > x && mouseX < x + width) {
                @Pc(53) int entryIndex = -1;

                for (@Pc(55) int i = 0; i < entryCount; i++) {
                    if (useSprites) {
                        @Pc(71) int entryY = y + SPRITE_TOP_HEIGHT + (i * ENTRY_HEIGHT);

                        if (mouseY > entryY - 13 && mouseY <= entryY + 3) {
                            entryIndex = i;
                        }
                    } else {
                        @Pc(71) int entryY = y + SPRITE_TOP_HEIGHT + (i * ENTRY_HEIGHT);

                        if (mouseY > entryY - 13 && mouseY <= entryY + 3) {
                            entryIndex = i;
                        }
                    }
                }

                if (entryIndex != -1) {
                    @Pc(71) int index = 0;
                    @Pc(262) QueueIterator iterator = new QueueIterator(entryQueue);

                    for (@Pc(368) MiniMenuEntry entry = (MiniMenuEntry) iterator.first(); entry != null; entry = (MiniMenuEntry) iterator.next()) {
                        if (index++ == entryIndex) {
                            return ((MiniMenuEntryInner) entry.innerEntries.sentinel.next2).cursor;
                        }
                    }
                }
            } else if (openedEntry != null && mouseX > openedEntryX && mouseX < openedEntryWidth + openedEntryX) {
                @Pc(53) int openEntryIndex = -1;

                for (@Pc(55) int i = 0; i < openedEntry.size; i++) {
                    if (useSprites) {
                        @Pc(71) int openEntryY = openedEntryY + SPRITE_TOP_HEIGHT + (i * ENTRY_HEIGHT);

                        if (openEntryY - 13 < mouseY && openEntryY + 3 >= mouseY) {
                            openEntryIndex = i;
                        }
                    } else {
                        @Pc(71) int openEntryY = openedEntryY + TOP_HEIGHT + (i * ENTRY_HEIGHT);

                        if (mouseY > openEntryY - 13 && mouseY <= openEntryY + 3) {
                            openEntryIndex = i;
                        }
                    }
                }

                if (openEntryIndex != -1) {
                    @Pc(71) int index = 0;
                    @Pc(262) QueueIterator iterator = new QueueIterator(openedEntry.innerEntries);

                    for (@Pc(134) MiniMenuEntryInner inner = (MiniMenuEntryInner) iterator.first(); inner != null; inner = (MiniMenuEntryInner) iterator.next()) {
                        if (index++ == openEntryIndex) {
                            return inner.cursor;
                        }
                    }
                }
            }
        } else {
            if (mouseX > x && mouseX < x + width) {
                @Pc(53) int innerEntryIndex = -1;

                for (@Pc(55) int i = 0; i < innerEntryCount; i++) {
                    if (useSprites) {
                        @Pc(71) int innerEntryY = y + SPRITE_TOP_HEIGHT + ((innerEntryCount - i - 1) * ENTRY_HEIGHT);

                        if (mouseY > innerEntryY - 13 && innerEntryY + 3 >= mouseY) {
                            innerEntryIndex = i;
                        }
                    } else {
                        @Pc(71) int innerEntryY = y + TOP_HEIGHT + ((innerEntryCount - i - 1) * ENTRY_HEIGHT);

                        if (mouseY > innerEntryY - 13 && innerEntryY + 3 >= mouseY) {
                            innerEntryIndex = i;
                        }
                    }
                }

                if (innerEntryIndex != -1) {
                    @Pc(71) int index = 0;
                    @Pc(129) DequeIterator iterator = new DequeIterator(innerEntryQueue);

                    for (@Pc(134) MiniMenuEntryInner inner = (MiniMenuEntryInner) iterator.first(); inner != null; inner = (MiniMenuEntryInner) iterator.next()) {
                        if (index++ == innerEntryIndex) {
                            return inner.cursor;
                        }
                    }
                }
            }
        }

        return -1;
    }

    @OriginalMember(owner = "client!oga", name = "a", descriptor = "(BII)V")
    public static void method6223(@OriginalArg(1) int x, @OriginalArg(2) int y) {
        if (anInt8149 == 1) {
            doAction(y, draggedEntry, x);
        } else if (anInt8149 == 2) {
            if (OrthoMode.toolkitActive) {
                openAt(x + OrthoMode.method2283(), y + Static422.method5771());
            } else {
                openAt(x, y);
            }
        }

        draggedEntry = null;
        anInt8149 = 0;
    }

    @OriginalMember(owner = "client!mr", name = "a", descriptor = "(B)V")
    public static void method5628() {
        @Pc(16) MouseLog log = (MouseLog) Static226.mouseLogs.first();
        @Pc(30) boolean dragging = InterfaceManager.dragSource != null || anInt6964 > 0;
        @Pc(34) int mouseX = log.getX();
        @Pc(38) int mouseY = log.getY();

        if (dragging) {
            anInt8149 = 1;
        }

        if (dragging) {
            draggedEntry = activeEntry;
        } else {
            doAction(mouseY, activeEntry, mouseX);
        }
    }

    @OriginalMember(owner = "client!vr", name = "a", descriptor = "(Z)Ljava/lang/String;")
    public static String secondEntry() {
        return open || activeEntry == null ? "" : activeEntry.op;
    }

    @OriginalMember(owner = "client!eb", name = "a", descriptor = "(B)Ljava/lang/String;")
    public static String activeEntry() {
        if (open || activeEntry == null) {
            return "";
        } else if ((activeEntry.opBase == null || activeEntry.opBase.length() == 0) && activeEntry.second != null && activeEntry.second.length() > 0) {
            return activeEntry.second;
        } else {
            return activeEntry.opBase;
        }
    }

    @OriginalMember(owner = "client!km", name = "a", descriptor = "(I)I")
    public static int length() {
        if (open) {
            return 6;
        } else if (activeEntry == null) {
            return 0;
        } else {
            @Pc(23) int action = activeEntry.action;

            if (MiniMenuAction.isButtonOp(action)) {
                return 1;
            } else if (MiniMenuAction.isObjOp(action)) {
                return 2;
            } else if (MiniMenuAction.isLocOp(action)) {
                return 3;
            } else if (MiniMenuAction.isNpcOp(action)) {
                return 4;
            } else if (MiniMenuAction.isPlayerOp(action)) {
                return 7;
            } else if (action == MiniMenuAction.TGT_SELF) {
                return 8;
            } else {
                return 5;
            }
        }
    }

    @OriginalMember(owner = "client!kh", name = "g", descriptor = "(I)V")
    public static void resetSprites() {
        rightBorderSprite = null;
        topRightCornerSprite = null;
        topLeftCornerSprite = null;
        bottomRightCornerSprite = null;
        bottomBorderSprite = null;
        separatorSprite = null;
        icons = null;
        bottomLeftCornerSprite = null;
        leftBorderSprite = null;
    }

    @OriginalMember(owner = "client!qp", name = "a", descriptor = "(B)V")
    public static void resetAndClose() {
        for (@Pc(8) MiniMenuEntry entry = (MiniMenuEntry) entryQueue.first(); entry != null; entry = (MiniMenuEntry) entryQueue.next()) {
            if (entry.size > 1) {
                entry.size = 0;
                cache.put(entry, ((MiniMenuEntryInner) entry.innerEntries.sentinel.next2).entryKey);
                entry.innerEntries.clear();
            }
        }
        entryCount = 0;
        innerEntryCount = 0;
        innerEntryQueue.clear();
        entryTable.clear();
        entryQueue.clear();
        open = false;
    }

    @OriginalMember(owner = "client!ho", name = "a", descriptor = "(BII)Z")
    public static boolean isOpen(@OriginalArg(1) int idAndSlot, @OriginalArg(2) int arg1) {
        if (!open) {
            return false;
        }

        @Pc(12) int id = idAndSlot >> 16;
        @Pc(23) int slot = idAndSlot & 0xFFFF;
        if (InterfaceList.interfaces[id] == null || InterfaceList.interfaces[id][slot] == null) {
            return false;
        }

        @Pc(44) Component component = InterfaceList.interfaces[id][slot];
        @Pc(57) MiniMenuEntryInner inner;
        if (arg1 == -1 && component.type == 0) {
            for (inner = (MiniMenuEntryInner) innerEntryQueue.first(); inner != null; inner = (MiniMenuEntryInner) innerEntryQueue.next()) {
                if (inner.action == MiniMenuAction.IF_BUTTONT || inner.action == MiniMenuAction.IF_BUTTONX2 || inner.action == MiniMenuAction.TGT_BUTTON || inner.action == MiniMenuAction.IF_BUTTONX1 || inner.action == MiniMenuAction.PAUSE_BUTTON) {
                    for (@Pc(160) Component parent = InterfaceList.list(inner.v3); parent != null; parent = InterfaceManager.getParentLayer(parent)) {
                        if (parent.slot == component.slot) {
                            return true;
                        }
                    }
                }
            }
        } else {
            for (inner = (MiniMenuEntryInner) innerEntryQueue.first(); inner != null; inner = (MiniMenuEntryInner) innerEntryQueue.next()) {
                if (inner.v2 == arg1 && component.slot == inner.v3 && (inner.action == MiniMenuAction.IF_BUTTONT || inner.action == MiniMenuAction.IF_BUTTONX2 || inner.action == MiniMenuAction.TGT_BUTTON || inner.action == MiniMenuAction.IF_BUTTONX1 || inner.action == MiniMenuAction.PAUSE_BUTTON)) {
                    return true;
                }
            }
        }
        return false;
    }

    private MiniMenu() {
        /* empty */
    }
}

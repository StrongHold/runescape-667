import com.jagex.Client;
import com.jagex.core.constants.HintArrowType;
import com.jagex.core.constants.LocShapes;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.Location;
import com.jagex.game.camera.CameraMode;
import com.jagex.game.collision.CollisionMap;
import com.jagex.game.runetek6.config.loctype.LocInteractivity;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.meltype.MapElementType;
import com.jagex.game.runetek6.config.meltype.MapElementTypeList;
import com.jagex.game.runetek6.config.msitype.MSIType;
import com.jagex.game.runetek6.config.msitype.MSITypeList;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import static com.jagex.game.collision.CollisionFlag.BLOCKWALK;
import static com.jagex.game.collision.CollisionFlag.GROUND_DECOR;
import static com.jagex.game.collision.CollisionFlag.LOCATION;
import static com.jagex.game.collision.CollisionFlag.LOCATION_BREAKROUTEFINDING;
import static com.jagex.game.collision.CollisionFlag.UNKNOWN;
import static com.jagex.game.collision.CollisionFlag.WALL_EAST;
import static com.jagex.game.collision.CollisionFlag.WALL_EAST_BREAKROUTEFINDING;
import static com.jagex.game.collision.CollisionFlag.WALL_NORTH;
import static com.jagex.game.collision.CollisionFlag.WALL_NORTH_BREAKROUTEFINDING;
import static com.jagex.game.collision.CollisionFlag.WALL_SOUTH;
import static com.jagex.game.collision.CollisionFlag.WALL_SOUTH_BREAKROUTEFINDING;
import static com.jagex.game.collision.CollisionFlag.WALL_WEST;
import static com.jagex.game.collision.CollisionFlag.WALL_WEST_BLOCK_BREAKROUTEFINDING;

public final class Minimap {

    @OriginalMember(owner = "client!rr", name = "F", descriptor = "[I")
    public static final int[] locX = new int[1000];

    @OriginalMember(owner = "client!tg", name = "n", descriptor = "Lclient!sia;")
    public static final Deque elementCoords = new Deque();

    @OriginalMember(owner = "client!qp", name = "f", descriptor = "[I")
    public static final int[] locId = new int[1000];

    @OriginalMember(owner = "client!la", name = "u", descriptor = "[I")
    public static final int[] locZ = new int[1000];

    @OriginalMember(owner = "client!gda", name = "e", descriptor = "I")
    public static final int BLOCK_SIZE = 52;

    @OriginalMember(owner = "client!sda", name = "g", descriptor = "I")
    public static int toggle = 0;

    @OriginalMember(owner = "client!vga", name = "j", descriptor = "I")
    public static int flagX = -1;

    @OriginalMember(owner = "client!gka", name = "n", descriptor = "I")
    public static int flagY = -1;

    @OriginalMember(owner = "client!ah", name = "i", descriptor = "Lclient!st;")
    public static Sprite sprite;

    @OriginalMember(owner = "client!uf", name = "f", descriptor = "I")
    public static int level = -1;

    @OriginalMember(owner = "client!ifa", name = "h", descriptor = "Z")
    public static boolean flagSet = true;

    @OriginalMember(owner = "client!bh", name = "u", descriptor = "Lclient!nc;")
    public static MapElementList elements;

    @OriginalMember(owner = "client!dja", name = "n", descriptor = "Z")
    public static boolean drawCollisionMap = false;

    @OriginalMember(owner = "client!qt", name = "b", descriptor = "I")
    public static int locCount = 0;

    @OriginalMember(owner = "client!aw", name = "a", descriptor = "(ILclient!ha;ILclient!hda;I)V")
    public static void draw(@OriginalArg(3) Component component, @OriginalArg(1) Toolkit toolkit, @OriginalArg(4) int screenX, @OriginalArg(0) int screenY) {
        @Pc(23) Graphic graphic = component.graphic(toolkit);
        if (graphic == null) {
            return;
        }

        @Pc(30) ClippingMask clippingMask = graphic.clippingMask;
        toolkit.KA(screenX, screenY, screenX + component.width, screenY + component.height);

        if (toggle == 2 || toggle == 5 || sprite == null) {
            toolkit.A(0xFF000000, clippingMask, screenX, screenY);
            return;
        }

        @Pc(90) int selfX;
        @Pc(93) int selfZ;
        @Pc(87) int yaw;
        @Pc(79) int scale;
        if (Camera.mode == CameraMode.MODE_FOLLOWCOORD) {
            yaw = (int) -Camera.playerCameraYaw & 0x3FFF;
            scale = 4096;
            selfZ = Camera.anInt4018;
            selfX = Camera.anInt6262;
        } else {
            scale = 4096 - Camera.scaleOffset * 16;
            yaw = (int) -Camera.playerCameraYaw + Camera.yawOffset & 0x3FFF;
            selfX = PlayerEntity.self.x;
            selfZ = PlayerEntity.self.z;
        }

        @Pc(120) int x = ((selfX / 128) + 208 + 48) - (Static720.mapWidth * 2);
        @Pc(137) int z = ((Static501.mapLength * 4) + 48) - (selfZ / 128) - ((Static501.mapLength - 104) * 2);
        sprite.renderRotated((float) screenX + ((float) component.width / 2.0F), ((float) component.height / 2.0F) + (float) screenY, (float) x, (float) z, scale, yaw << 2, clippingMask, screenX, screenY);

        for (@Pc(171) IntNode node = (IntNode) elementCoords.first(); node != null; node = (IntNode) elementCoords.next()) {
            @Pc(178) int coord = node.value;

            @Pc(190) int elementX = (elements.coords[coord] >> 14 & 0x3FFF) - WorldMap.areaBaseX;
            @Pc(200) int elementZ = (elements.coords[coord] & 0x3FFF) - WorldMap.areaBaseZ;

            @Pc(211) int elementDrawX = ((elementX * 4) + 2) - (selfX / 128);
            @Pc(222) int elementDrawZ = ((elementZ * 4) + 2) - (selfZ / 128);

            drawMapElement(elementDrawZ, screenX, clippingMask, toolkit, elements.elements[coord], screenY, elementDrawX, component);
        }

        for (@Pc(190) int i = 0; i < locCount; i++) {
            @Pc(200) int locX = Minimap.locX[i] * 4 + 2 - selfX / 128;
            @Pc(211) int locZ = Minimap.locZ[i] * 4 + 2 - selfZ / 128;

            @Pc(287) LocType type = LocTypeList.instance.list(locId[i]);
            if (type.multiloc != null) {
                type = type.getMultiLoc(TimedVarDomain.instance);

                if (type == null || type.mapelement == -1) {
                    continue;
                }
            }

            drawMapElement(locZ, screenX, clippingMask, toolkit, type.mapelement, screenY, locX, component);
        }

        for (@Pc(334) ObjStack stack = (ObjStack) Static497.objStacks.first(); stack != null; stack = (ObjStack) Static497.objStacks.next()) {
            @Pc(211) int stackLevel = (int) (stack.key >> 28 & 0x3L);

            if (level == stackLevel) {
                @Pc(222) int objX = (int) (stack.key & 0x3FFFL) - WorldMap.areaBaseX;
                @Pc(370) int objZ = (int) ((stack.key >> 14) & 0x3FFFL) - WorldMap.areaBaseZ;
                @Pc(381) int stackX = ((objX * 4) + 2) - (selfX / 128);
                @Pc(392) int stackZ = ((objZ * 4) + 2) - (selfZ / 128);
                drawDot(screenY, clippingMask, Sprites.mapdots[0], stackZ, stackX, component, screenX);
            }
        }

        for (@Pc(211) int i = 0; i < NPCList.size; i++) {
            @Pc(427) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(NPCList.slots[i]);
            if (node == null) {
                continue;
            }

            @Pc(432) NPCEntity npc = node.npc;
            if (npc.hasType() && npc.level == PlayerEntity.self.level) {
                @Pc(446) NPCType type = npc.type;
                if (type != null && type.multinpcs != null) {
                    type = type.getMultiNPC(TimedVarDomain.instance);
                }

                if (type != null && type.displayOnMiniMap && type.interactive) {
                    @Pc(392) int npcX = (npc.x / 128) - (selfX / 128);
                    @Pc(490) int npcZ = (npc.z / 128) - (selfZ / 128);

                    if (type.mapElement != -1) {
                        drawMapElement(npcZ, screenX, clippingMask, toolkit, type.mapElement, screenY, npcX, component);
                    } else {
                        drawDot(screenY, clippingMask, Sprites.mapdots[1], npcZ, npcX, component, screenX);
                    }
                }
            }
        }

        @Pc(222) int playerCount = PlayerList.highResolutionCount;
        @Pc(531) int[] playerIndices = PlayerList.highResolutionSlots;
        for (@Pc(381) int i = 0; i < playerCount; i++) {
            @Pc(541) PlayerEntity player = PlayerList.highResolutionPlayers[playerIndices[i]];
            if (player == null || !player.hasModel() || player.hideOnMap || PlayerEntity.self == player || player.level != PlayerEntity.self.level) {
                continue;
            }

            @Pc(490) int playerX = (player.x / 128) - (selfX / 128);
            @Pc(585) int playerZ = (player.z / 128) - (selfZ / 128);
            @Pc(587) boolean friend = false;
            for (@Pc(589) int j = 0; j < FriendsList.count; j++) {
                if (player.nameUnfiltered.equals(FriendsList.names[j]) && FriendsList.worlds[j] != 0) {
                    friend = true;
                    break;
                }
            }

            @Pc(620) boolean chatmate = false;
            for (@Pc(622) int j = 0; j < FriendChat.count; j++) {
                if (player.nameUnfiltered.equals(FriendChat.users[j].usernameUnfiltered)) {
                    chatmate = true;
                    break;
                }
            }

            @Pc(652) boolean teammate = false;
            if (PlayerEntity.self.team != 0 && player.team != 0 && PlayerEntity.self.team == player.team) {
                teammate = true;
            }

            if (player.showPIcon) {
                drawDot(screenY, clippingMask, Sprites.mapdots[6], playerZ, playerX, component, screenX);
            } else if (teammate) {
                drawDot(screenY, clippingMask, Sprites.mapdots[4], playerZ, playerX, component, screenX);
            } else if (player.clanmate) {
                drawDot(screenY, clippingMask, Sprites.mapdots[7], playerZ, playerX, component, screenX);
            } else if (friend) {
                drawDot(screenY, clippingMask, Sprites.mapdots[3], playerZ, playerX, component, screenX);
            } else if (chatmate) {
                drawDot(screenY, clippingMask, Sprites.mapdots[5], playerZ, playerX, component, screenX);
            } else {
                drawDot(screenY, clippingMask, Sprites.mapdots[2], playerZ, playerX, component, screenX);
            }
        }

        @Pc(788) HintArrow[] hintArrows = Static527.hintArrows;

        for (@Pc(490) int i = 0; i < hintArrows.length; i++) {
            @Pc(796) HintArrow hintArrow = hintArrows[i];
            if (hintArrow == null || hintArrow.type == HintArrowType.CLEAR || TimeUtils.clock % 20 >= 10) {
                continue;
            }

            if (hintArrow.type == HintArrowType.NPC) {
                @Pc(828) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(hintArrow.entity);

                if (node != null) {
                    @Pc(833) NPCEntity npc = node.npc;
                    @Pc(843) int arrowX = (npc.x / 128) - (selfX / 128);
                    @Pc(622) int arrowZ = (npc.z / 128) - (selfZ / 128);
                    drawHintMapedge(arrowX, screenX, 360000L, clippingMask, hintArrow.sprite, screenY, arrowZ, component);
                }
            }

            if (hintArrow.type == HintArrowType.TILE_CENTRE) {
                @Pc(878) int arrowX = (hintArrow.x / 128) - (selfX / 128);
                @Pc(589) int arrowZ = (hintArrow.z / 128) - (selfZ / 128);
                @Pc(893) long distance = hintArrow.drawDistance << 7;
                @Pc(897) long distanceSquared = distance * distance;
                drawHintMapedge(arrowX, screenX, distanceSquared, clippingMask, hintArrow.sprite, screenY, arrowZ, component);
            }

            if (hintArrow.type == HintArrowType.PLAYER && hintArrow.entity >= 0 && hintArrow.entity < PlayerList.highResolutionPlayers.length) {
                @Pc(932) PlayerEntity player = PlayerList.highResolutionPlayers[hintArrow.entity];

                if (player != null) {
                    @Pc(589) int arrowX = (player.x / 128) - (selfX / 128);
                    @Pc(843) int arrowZ = (player.z / 128) - (selfZ / 128);
                    drawHintMapedge(arrowX, screenX, 360000L, clippingMask, hintArrow.sprite, screenY, arrowZ, component);
                }
            }
        }

        if (Camera.mode != CameraMode.MODE_FOLLOWCOORD) {
            if (flagX != 0) {
                @Pc(585) int arrowX = ((flagX * 4) + ((PlayerEntity.self.getSize() - 1) * 2) + 2) - (selfX / 128);
                @Pc(878) int arrowZ = ((flagY * 4) + (PlayerEntity.self.getSize() * 2) + 2) - (selfZ / 128) - 2;
                drawDot(screenY, clippingMask, Sprites.mapflag[flagSet ? 1 : 0], arrowZ, arrowX, component, screenX);
            }

            if (!PlayerEntity.self.hideOnMap) {
                toolkit.fillRect((component.width / 2) + screenX - 1, (screenY + (component.height / 2)) - 1, 3, 3, 0xFFFFFFFF);
            }
        }
    }

    @OriginalMember(owner = "client!uga", name = "a", descriptor = "(Lclient!hda;III)V")
    public static void drawCompass(@OriginalArg(0) Component component, @OriginalArg(1) int screenX, @OriginalArg(3) int screenY) {
        @Pc(8) Graphic graphic = component.graphic(Toolkit.active);
        if (graphic == null) {
            return;
        }

        Toolkit.active.KA(screenX, screenY, screenX + component.width, screenY + component.height);
        if (toggle >= 3) {
            Toolkit.active.A(-16777216, graphic.clippingMask, screenX, screenY);
        } else {
            Sprites.compass.renderRotated((float) component.width / 2.0F + (float) screenX, (float) component.height / 2.0F + (float) screenY, ((int) -Camera.playerCameraYaw & 0x3FFF) << 2, graphic.clippingMask, screenX, screenY);
        }
    }

    @OriginalMember(owner = "client!hk", name = "a", descriptor = "(I)V")
    public static void reset() {
        sprite = null;
        level = -1;
    }

    @OriginalMember(owner = "client!fn", name = "a", descriptor = "(Lclient!ha;IIIIIIII)V")
    public static void drawLocs(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int wallColour, @OriginalArg(2) int tileX, @OriginalArg(3) int tileZ, @OriginalArg(4) int drawX, @OriginalArg(5) int drawY, @OriginalArg(7) int level, @OriginalArg(8) int doorColour) {
        @Pc(9) Location loc = (Location) Static302.getWall(level, tileX, tileZ);

        if (loc != null) {
            @Pc(20) LocType type = LocTypeList.instance.list(loc.getId());
            @Pc(26) int rotation = loc.getRotation() & 0x3;
            @Pc(30) int shape = loc.getShape();

            if (type.msi == -1) {
                @Pc(45) int colour = wallColour;
                if (type.active > LocInteractivity.NONINTERACTIVE) {
                    colour = doorColour;
                }

                if (shape == LocShapes.WALL_STRAIGHT || shape == LocShapes.WALL_L) {
                    if (rotation == 0) {
                        toolkit.verticalLine(drawX, drawY, 4, colour);
                    } else if (rotation == 1) {
                        toolkit.horizontalLine(drawX, drawY, 4, colour);
                    } else if (rotation == 2) {
                        toolkit.verticalLine(drawX + 3, drawY, 4, colour);
                    } else if (rotation == 3) {
                        toolkit.horizontalLine(drawX, drawY + 3, 4, colour);
                    }
                }

                if (shape == LocShapes.WALL_SQUARECORNER) {
                    if (rotation == 0) {
                        toolkit.fillRect(drawX, drawY, 1, 1, colour);
                    } else if (rotation == 1) {
                        toolkit.fillRect(drawX + 3, drawY, 1, 1, colour);
                    } else if (rotation == 2) {
                        toolkit.fillRect(drawX + 3, drawY + 3, 1, 1, colour);
                    } else if (rotation == 3) {
                        toolkit.fillRect(drawX, drawY + 3, 1, 1, colour);
                    }
                }

                if (shape == LocShapes.WALL_L) {
                    if (rotation == 0) {
                        toolkit.horizontalLine(drawX, drawY, 4, colour);
                    } else if (rotation == 1) {
                        toolkit.verticalLine(drawX + 3, drawY, 4, colour);
                    } else if (rotation == 2) {
                        toolkit.horizontalLine(drawX, drawY + 3, 4, colour);
                    } else if (rotation == 3) {
                        toolkit.verticalLine(drawX, drawY, 4, colour);
                    }
                }
            } else {
                drawMsi(type, rotation, toolkit, drawX, drawY);
            }
        }

        loc = (Location) Static578.getEntity(level, tileX, tileZ, Static185.locClass == null ? (Static185.locClass = Static185.getClass("com.jagex.game.Location")) : Static185.locClass);
        if (loc != null) {
            @Pc(20) LocType type = LocTypeList.instance.list(loc.getId());
            @Pc(26) int rotation = loc.getRotation() & 0x3;
            @Pc(30) int shape = loc.getShape();

            if (type.msi != -1) {
                drawMsi(type, rotation, toolkit, drawX, drawY);
            } else if (shape == LocShapes.WALL_DIAGONAL) {
                @Pc(45) int colour = 0xffEEEEEE;
                if (type.active > 0) {
                    colour = 0xFFEE0000;
                }

                if (rotation == 0 || rotation == 2) {
                    toolkit.line(drawY, drawY + 3, drawX + 3, colour, drawX);
                } else {
                    toolkit.line(drawY + 3, drawY, drawX + 3, colour, drawX);
                }
            }
        }

        loc = (Location) Static687.getGroundDecor(level, tileX, tileZ);
        if (loc != null) {
            @Pc(20) LocType type = LocTypeList.instance.list(loc.getId());
            @Pc(26) int rotation = loc.getRotation() & 0x3;

            if (type.msi != -1) {
                drawMsi(type, rotation, toolkit, drawX, drawY);
            }
        }
    }

    @OriginalMember(owner = "client!oea", name = "a", descriptor = "(Lclient!c;BILclient!ha;II)V")
    public static void drawMsi(@OriginalArg(0) LocType locType, @OriginalArg(2) int rotation, @OriginalArg(3) Toolkit toolkit, @OriginalArg(4) int drawX, @OriginalArg(5) int drawY) {
        @Pc(9) MSIType msiType = MSITypeList.instance.list(locType.msi);
        if (msiType.image == -1) {
            return;
        }

        if (locType.msirotate) {
            @Pc(24) int msiRotation = rotation + locType.msiRotateOffset;
            rotation = msiRotation & 0x3;
        } else {
            rotation = 0;
        }

        @Pc(39) Sprite sprite = msiType.sprite(rotation, toolkit, locType.msiflip);
        if (sprite == null) {
            return;
        }

        @Pc(46) int width = locType.width;
        @Pc(49) int length = locType.length;
        if ((rotation & 0x1) == 1) {
            width = locType.length;
            length = locType.width;
        }

        @Pc(72) int spriteWidth = sprite.scaleWidth();
        @Pc(75) int spriteHeight = sprite.scaleHeight();
        if (msiType.enlarge) {
            spriteHeight = length * 4;
            spriteWidth = width * 4;
        }

        if (msiType.colour != 0) {
            sprite.render(drawX, drawY - (length - 1) * 4, spriteWidth, spriteHeight, 0, msiType.colour | 0xFF000000, 1);
        } else {
            sprite.render(drawX, drawY + 4 - length * 4, spriteWidth, spriteHeight);
        }
    }

    @OriginalMember(owner = "client!tka", name = "a", descriptor = "(IIILclient!aa;Lclient!ha;IIILclient!hda;)V")
    public static void drawMapElement(@OriginalArg(0) int drawY, @OriginalArg(2) int screenX, @OriginalArg(3) ClippingMask mask, @OriginalArg(4) Toolkit toolkit, @OriginalArg(5) int id, @OriginalArg(6) int screenY, @OriginalArg(7) int drawX, @OriginalArg(8) Component component) {
        @Pc(10) MapElementType elementType = MapElementTypeList.instance.list(id);
        if (elementType == null || !elementType.aBoolean218 || !elementType.variableTest(TimedVarDomain.instance)) {
            return;
        }

        if (elementType.landmarkPolygons != null) {
            @Pc(34) int[] polygon = new int[elementType.landmarkPolygons.length];

            for (@Pc(36) int i = 0; i < polygon.length / 2; i++) {
                @Pc(51) int yaw;
                if (Camera.mode == CameraMode.MODE_FOLLOWCOORD) {
                    yaw = (int) Camera.playerCameraYaw & 0x3FFF;
                } else {
                    yaw = (int) Camera.playerCameraYaw + Camera.yawOffset & 0x3FFF;
                }

                @Pc(62) int sin = Trig1.SIN[yaw];
                @Pc(66) int cos = Trig1.COS[yaw];

                if (Camera.mode != CameraMode.MODE_FOLLOWCOORD) {
                    sin = sin * 256 / (Camera.scaleOffset + 256);
                    cos = cos * 256 / (Camera.scaleOffset + 256);
                }

                polygon[i * 2] = component.width / 2 + screenX + (cos * (elementType.landmarkPolygons[i * 2] * 4 + drawX) + sin * (drawY + elementType.landmarkPolygons[i * 2 + 1] * 4) >> 14);
                polygon[(i * 2) + 1] = screenY + component.height / 2 - (cos * (drawY + elementType.landmarkPolygons[i * 2 + 1] * 4) - sin * (drawX + elementType.landmarkPolygons[i * 2] * 4) >> 14);
            }

            @Pc(187) Graphic graphic = component.graphic(toolkit);
            if (graphic != null) {
                PolygonFiller.fillPolygon(toolkit, polygon, elementType.landmarkBackground, graphic.lineOffsets, graphic.lineWidths);
            }

            if (elementType.anInt2603 > 0) {
                @Pc(250) int swapX;
                @Pc(252) int swapY;

                for (@Pc(62) int i = 0; i < polygon.length / 2 - 1; i++) {
                    @Pc(66) int x1 = polygon[i * 2];
                    @Pc(223) int y1 = polygon[(i * 2) + 1];

                    @Pc(231) int x2 = polygon[(i * 2) + 2];
                    @Pc(241) int y2 = polygon[(i * 2) + 2 + 1];

                    if (x1 > x2) {
                        swapX = x1;
                        swapY = y1;
                        x1 = x2;
                        x2 = swapX;
                        y1 = y2;
                        y2 = swapY;
                    } else if (x1 == x2 && y1 > y2) {
                        swapY = y1;
                        y1 = y2;
                        y2 = swapY;
                    }

                    toolkit.method7942(x1, y1, x2, y2, elementType.landmarkPalette[elementType.landmarkColorIndices[i] & 0xFF], 1, mask, screenX, screenY, elementType.anInt2603, elementType.anInt2587, elementType.anInt2607);
                }

                @Pc(66) int x1 = polygon[polygon.length - 2];
                @Pc(223) int y1 = polygon[polygon.length - 1];

                @Pc(231) int x2 = polygon[0];
                @Pc(241) int y2 = polygon[1];

                if (x2 < x1) {
                    swapX = x1;
                    swapY = y1;
                    x1 = x2;
                    x2 = swapX;
                    y1 = y2;
                    y2 = swapY;
                } else if (x1 == x2 && y1 > y2) {
                    swapY = y1;
                    y1 = y2;
                    y2 = swapY;
                }

                toolkit.method7942(x1, y1, x2, y2, elementType.landmarkPalette[elementType.landmarkColorIndices[elementType.landmarkColorIndices.length - 1] & 0xFF], 1, mask, screenX, screenY, elementType.anInt2603, elementType.anInt2587, elementType.anInt2607);
            } else {
                for (@Pc(62) int i = 0; i < polygon.length / 2 - 1; i++) {
                    toolkit.line(polygon[i * 2], polygon[i * 2 + 1], polygon[i * 2 + 2], polygon[(i + 1) * 2 + 1], elementType.landmarkPalette[elementType.landmarkColorIndices[i] & 0xFF], 0, mask, screenX, screenY);
                }

                toolkit.line(polygon[polygon.length - 2], polygon[polygon.length - 1], polygon[0], polygon[1], elementType.landmarkPalette[elementType.landmarkColorIndices[elementType.landmarkColorIndices.length - 1] & 0xFF], 0, mask, screenX, screenY);
            }
        }

        @Pc(517) Sprite sprite = null;
        if (elementType.sprite != -1) {
            sprite = elementType.sprite(toolkit, false);

            if (sprite != null) {
                drawDot(screenY, mask, sprite, drawY, drawX, component, screenX);
            }
        }

        if (elementType.text != null) {
            @Pc(36) int height = 0;
            if (sprite != null) {
                height = sprite.getHeight();
            }

            @Pc(553) Font font = Fonts.p11;
            @Pc(555) FontMetrics metrics = Fonts.p11Metrics;
            if (elementType.textSize == 1) {
                font = Fonts.p12;
                metrics = Fonts.p12Metrics;
            }
            if (elementType.textSize == 2) {
                metrics = Fonts.b12Metrics;
                font = Fonts.b12;
            }

            Static256.drawMapElementText(font, elementType.text, metrics, screenX, elementType.textColour, height, component, mask, drawY, screenY, drawX);
        }
    }

    @OriginalMember(owner = "client!pea", name = "a", descriptor = "(Lclient!ha;II)Z")
    public static boolean drawLevel(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int mapLevel) {
        @Pc(9) int mapX = (Static720.mapWidth - 104) / 2;
        @Pc(15) int mapZ = (Static501.mapLength - 104) / 2;
        @Pc(17) boolean hasMsi = true;

        for (@Pc(19) int x = mapX; x < mapX + 104; x++) {
            for (@Pc(22) int z = mapZ; z < mapZ + 104; z++) {
                for (@Pc(25) int level = mapLevel; level <= 3; level++) {
                    if (Static696.isTileVisibleFrom(z, mapLevel, x, level)) {
                        @Pc(37) int actualLevel = level;
                        if (Static441.isBridgeAt(z, x)) {
                            actualLevel = level - 1;
                        }
                        if (actualLevel >= 0) {
                            hasMsi &= Static561.hasMsi(x, z, actualLevel);
                        }
                    }
                }
            }
        }

        if (!hasMsi) {
            return false;
        }

        @Pc(95) int[] colours = new int[262144];
        for (@Pc(25) int i = 0; i < colours.length; i++) {
            colours[i] = 0xFF000000;
        }

        sprite = toolkit.createSprite(512, 512, 512, colours);
        Static104.resetLighting();

        @Pc(37) int randomWallColour = (int) (Math.random() * 20.0D) + ((int) (Math.random() * 20.0D) + 238 - 10 << 8) + ((int) (Math.random() * 20.0D) + -10 + 238 << 16) + 238 - 10 | 0xFF000000;
        @Pc(177) int randomDoorColour = ((int) (Math.random() * 20.0D) + 238 - 10 | 0x9E04FF00) << 16;
        @Pc(196) int randomFloorColour = (int) (Math.random() * 8.0D) << 16 | (int) (Math.random() * 8.0D) << 8 | (int) (Math.random() * 8.0D);
        @Pc(206) boolean[][] visibility = new boolean[BLOCK_SIZE + 1 + 2][BLOCK_SIZE + 3];

        for (@Pc(208) int x = mapX; x < mapX + 104; x += BLOCK_SIZE) {
            for (@Pc(211) int z = mapZ; z < mapZ + 104; z += BLOCK_SIZE) {
                @Pc(214) int padX = 0;
                @Pc(216) int padZ = 0;

                @Pc(218) int x1 = x;
                if (x > 0) {
                    x1 = x - 1;
                    padX += 4;
                }

                @Pc(229) int z1 = z;
                if (z > 0) {
                    z1 = z - 1;
                }

                @Pc(238) int x2 = x + BLOCK_SIZE;
                if (x2 < 104) {
                    x2++;
                }

                @Pc(249) int z2 = z + BLOCK_SIZE;
                if (z2 < 104) {
                    z2++;
                    padZ += 4;
                }

                toolkit.KA(0, 0, (BLOCK_SIZE * 4) + padX, padZ + (BLOCK_SIZE * 4));
                toolkit.GA(0xFF000000);

                for (@Pc(278) int level = mapLevel; level <= 3; level++) {
                    for (@Pc(281) int offsetX = 0; offsetX <= BLOCK_SIZE; offsetX++) {
                        for (@Pc(284) int offsetZ = 0; offsetZ <= BLOCK_SIZE; offsetZ++) {
                            visibility[offsetX][offsetZ] = Static696.isTileVisibleFrom(offsetZ + z1, mapLevel, x1 + offsetX, level);
                        }
                    }

                    Static706.floor[level].drawMinimap(x1, z1, x2, z2, visibility);

                    if (!drawCollisionMap) {
                        for (@Pc(284) int offsetX = -4; offsetX < BLOCK_SIZE; offsetX++) {
                            for (@Pc(331) int offsetZ = -4; offsetZ < BLOCK_SIZE; offsetZ++) {
                                @Pc(336) int tileX = offsetX + x;
                                @Pc(340) int tileZ = offsetZ + z;
                                if (mapX <= tileX && tileZ >= mapZ && Static696.isTileVisibleFrom(tileZ, mapLevel, tileX, level)) {
                                    @Pc(365) int actualLevel = level;
                                    if (Static441.isBridgeAt(tileZ, tileX)) {
                                        actualLevel = level - 1;
                                    }
                                    if (actualLevel >= 0) {
                                        drawLocs(toolkit, randomWallColour, tileX, tileZ, padX + offsetX * 4, (-offsetZ + BLOCK_SIZE) * 4 + padZ + -4, actualLevel, randomDoorColour);
                                    }
                                }
                            }
                        }
                    }
                }

                if (drawCollisionMap) {
                    @Pc(435) CollisionMap map = Client.collisionMaps[mapLevel];

                    for (@Pc(284) int offsetX = 0; offsetX < BLOCK_SIZE; offsetX++) {
                        for (@Pc(331) int offsetZ = 0; offsetZ < BLOCK_SIZE; offsetZ++) {
                            @Pc(336) int tileX = x + offsetX;
                            @Pc(340) int tileZ = z + offsetZ;
                            @Pc(365) int flags = map.flags[tileX - map.x][tileZ - map.z];

                            if ((flags & (LOCATION_BREAKROUTEFINDING | BLOCKWALK | GROUND_DECOR)) != 0) {
                                toolkit.fillRect(padX + (offsetX * 4), (((BLOCK_SIZE - offsetZ) * 4) + padZ) - 4, 4, 4, 0x99DD00AA);
                            } else if ((flags & WALL_NORTH_BREAKROUTEFINDING) != 0) {
                                toolkit.horizontalLine(padX + (offsetX * 4), (((BLOCK_SIZE - offsetZ) * 4) + padZ) - 4, 4, 0x99DD00AA);
                            } else if ((flags & WALL_EAST_BREAKROUTEFINDING) != 0) {
                                toolkit.verticalLine((offsetX * 4) + padX + 3, (padZ + ((-offsetZ + BLOCK_SIZE) * 4)) - 4, 4, 0x99DD00AA);
                            } else if ((flags & WALL_SOUTH_BREAKROUTEFINDING) != 0) {
                                toolkit.horizontalLine((offsetX * 4) + padX, (((BLOCK_SIZE - offsetZ) * 4) + padZ + 3) - 4, 4, 0x99DD00AA);
                            } else if ((flags & WALL_WEST_BLOCK_BREAKROUTEFINDING) != 0) {
                                toolkit.verticalLine((offsetX * 4) + padX, (((BLOCK_SIZE - offsetZ) * 4) + padZ) - 4, 4, 0x99DD00AA);
                            }
                        }
                    }
                }

                toolkit.aa(padX, padZ, BLOCK_SIZE * 4, BLOCK_SIZE * 4, randomFloorColour, 2);
                sprite.copyRect((x - mapX) * 4 + 48, -(BLOCK_SIZE * 4) + -((z + -mapZ) * 4) + 464, BLOCK_SIZE * 4, BLOCK_SIZE * 4, padX, padZ);
            }
        }

        toolkit.la();
        toolkit.GA(0xFF000001);
        InterfaceManager.redrawAll();
        locCount = 0;
        elementCoords.clear();

        if (!drawCollisionMap) {
            for (@Pc(211) int x = mapX; x < mapX + 104; x++) {
                for (@Pc(214) int z = mapZ; z < mapZ + 104; z++) {
                    for (@Pc(216) int level = mapLevel; level <= mapLevel + 1 && level <= 3; level++) {
                        if (Static696.isTileVisibleFrom(z, mapLevel, x, level)) {
                            @Pc(730) Location loc = (Location) Static687.getGroundDecor(level, x, z);
                            if (loc == null) {
                                loc = (Location) Static578.getEntity(level, x, z, Static484.locClass == null ? (Static484.locClass = Static484.getClass("com.jagex.game.Location")) : Static484.locClass);
                            }
                            if (loc == null) {
                                loc = (Location) Static302.getWall(level, x, z);
                            }
                            if (loc == null) {
                                loc = Static114.getWallDecor(level, x, z);
                            }
                            if (loc == null) {
                                continue;
                            }

                            @Pc(776) LocType locType = LocTypeList.instance.list(loc.getId());
                            if (!locType.members || Static174.mapMembers) {
                                @Pc(238) int mapelement = locType.mapelement;

                                if (locType.multiloc != null) {
                                    for (@Pc(249) int i = 0; i < locType.multiloc.length; i++) {
                                        if (locType.multiloc[i] != -1) {
                                            @Pc(808) LocType multiLocType = LocTypeList.instance.list(locType.multiloc[i]);

                                            if (multiLocType.mapelement >= 0) {
                                                mapelement = multiLocType.mapelement;
                                            }
                                        }
                                    }
                                }

                                if (mapelement >= 0) {
                                    @Pc(832) boolean randomise = false;
                                    if (mapelement >= 0) {
                                        @Pc(842) MapElementType elementType = MapElementTypeList.instance.list(mapelement);
                                        if (elementType != null && elementType.randomise) {
                                            randomise = true;
                                        }
                                    }

                                    @Pc(278) int newX = x;
                                    @Pc(281) int newZ = z;

                                    if (randomise) {
                                        @Pc(862) int[][] collisionFlags = Client.collisionMaps[level].flags;
                                        @Pc(331) int collisionX = Client.collisionMaps[level].x;
                                        @Pc(336) int collisionZ = Client.collisionMaps[level].z;

                                        for (@Pc(340) int i = 0; i < 10; i++) {
                                            @Pc(25) int random = (int) (Math.random() * 4.0D);
                                            if (random == 0 && mapX < newX && x - 3 < newX && (collisionFlags[newX - collisionX - 1][newZ - collisionZ] & (BLOCKWALK | UNKNOWN | GROUND_DECOR | LOCATION | WALL_EAST)) == 0) {
                                                newX--;
                                            }
                                            if (random == 1 && mapX + 104 - 1 > newX && x + 3 > newX && (collisionFlags[newX + 1 - collisionX][newZ - collisionZ] & (BLOCKWALK | UNKNOWN | GROUND_DECOR | LOCATION | WALL_WEST)) == 0) {
                                                newX++;
                                            }
                                            if (random == 2 && newZ > mapZ && z - 3 < newZ && (collisionFlags[newX - collisionX][newZ - collisionZ - 1] & (BLOCKWALK | UNKNOWN | GROUND_DECOR | LOCATION | WALL_NORTH)) == 0) {
                                                newZ--;
                                            }
                                            if (random == 3 && newZ < mapZ + 104 - 1 && z + 3 > newZ && (collisionFlags[newX - collisionX][newZ + 1 - collisionZ] & (BLOCKWALK | UNKNOWN | GROUND_DECOR | LOCATION | WALL_SOUTH)) == 0) {
                                                newZ++;
                                            }
                                        }
                                    }

                                    locId[locCount] = locType.id;
                                    locX[locCount] = newX;
                                    locZ[locCount] = newZ;
                                    locCount++;
                                }
                            }
                        }
                    }
                }
            }

            if (elements != null) {
                js5.CONFIG.discardunpacked = 1;
                MapElementTypeList.instance.setCaches(1024, 64);

                for (@Pc(214) int i = 0; i < elements.size; i++) {
                    @Pc(216) int coord = elements.coords[i];

                    if (PlayerEntity.self.level == coord >> 28) {
                        @Pc(218) int x = (coord >> 14 & 0x3FFF) - WorldMap.areaBaseX;
                        @Pc(229) int z = (coord & 0x3FFF) - WorldMap.areaBaseZ;

                        if (x >= 0 && x < Static720.mapWidth && z >= 0 && z < Static501.mapLength) {
                            elementCoords.addLast(new IntNode(i));
                        } else {
                            @Pc(1199) MapElementType elementType = MapElementTypeList.instance.list(elements.elements[i]);

                            if (elementType.landmarkPolygons != null && elementType.minX + x >= 0 && x + elementType.maxX < Static720.mapWidth && elementType.minZ + z >= 0 && z + elementType.maxZ < Static501.mapLength) {
                                elementCoords.addLast(new IntNode(i));
                            }
                        }
                    }
                }

                MapElementTypeList.instance.setCaches(MapElementTypeList.DEFAULT_CACHE_SIZE, 64);
                js5.CONFIG.discardunpacked = 2;
                js5.CONFIG.discardUnpacked();
            }
        }

        return true;
    }

    @OriginalMember(owner = "client!ad", name = "a", descriptor = "(BILclient!aa;Lclient!st;IILclient!hda;I)V")
    public static void drawDot(@OriginalArg(1) int offsetY, @OriginalArg(2) ClippingMask mask, @OriginalArg(3) Sprite sprite, @OriginalArg(4) int drawY, @OriginalArg(5) int drawX, @OriginalArg(6) Component component, @OriginalArg(7) int offsetX) {
        if (sprite == null) {
            return;
        }

        @Pc(15) int yaw;
        if (Camera.mode == CameraMode.MODE_FOLLOWCOORD) {
            yaw = (int) Camera.playerCameraYaw & 0x3FFF;
        } else {
            yaw = (int) Camera.playerCameraYaw + Camera.yawOffset & 0x3FFF;
        }

        @Pc(37) int radius = Math.max(component.width / 2, component.height / 2) + 10;
        @Pc(45) int distanceSquared = (drawY * drawY) + (drawX * drawX);
        if ((radius * radius) < distanceSquared) {
            return;
        }

        @Pc(60) int sin = Trig1.SIN[yaw];
        @Pc(64) int cos = Trig1.COS[yaw];
        if (Camera.mode != CameraMode.MODE_FOLLOWCOORD) {
            sin = (sin * 256) / (Camera.scaleOffset + 256);
            cos = (cos * 256) / (Camera.scaleOffset + 256);
        }

        @Pc(98) int rotatedX = ((drawX * cos) + (drawY * sin)) >> 14;
        @Pc(109) int rotatedZ = ((drawY * cos) - (drawX * sin)) >> 14;
        sprite.render((rotatedX + (component.width / 2) + offsetX) - (sprite.scaleWidth() / 2), ((component.height / 2) + offsetY) - rotatedZ - (sprite.scaleHeight() / 2), mask, offsetX, offsetY);
    }

    @OriginalMember(owner = "client!dk", name = "a", descriptor = "(IIBJLclient!aa;IIILclient!hda;)V")
    public static void drawHintMapedge(@OriginalArg(0) int arrowX, @OriginalArg(1) int offsetX, @OriginalArg(3) long maxDistance, @OriginalArg(4) ClippingMask mask, @OriginalArg(5) int sprite, @OriginalArg(6) int offsetY, @OriginalArg(7) int arrowY, @OriginalArg(8) Component component) {
        @Pc(16) int distance = (arrowY * arrowY) + (arrowX * arrowX);
        if ((long) distance > maxDistance) {
            return;
        }

        @Pc(37) int radius = Math.min(component.width / 2, component.height / 2);
        if (distance <= (radius * radius)) {
            drawDot(offsetY, mask, Sprites.hintMapmarkers[sprite], arrowY, arrowX, component, offsetX);
            return;
        }

        radius -= 10;

        @Pc(64) int yaw;
        if (Camera.mode == CameraMode.MODE_FOLLOWCOORD) {
            yaw = (int) Camera.playerCameraYaw & 0x3FFF;
        } else {
            yaw = Camera.yawOffset + (int) Camera.playerCameraYaw & 0x3FFF;
        }

        @Pc(77) int sin = Trig1.SIN[yaw];
        @Pc(81) int cos = Trig1.COS[yaw];
        if (Camera.mode != CameraMode.MODE_FOLLOWCOORD) {
            cos = cos * 256 / (Camera.scaleOffset + 256);
            sin = sin * 256 / (Camera.scaleOffset + 256);
        }

        @Pc(112) int rotatedX = ((arrowX * cos) + (arrowY * sin)) >> 14;
        @Pc(123) int rotatedZ = ((arrowY * cos) - (arrowX * sin)) >> 14;
        @Pc(129) double angle = Math.atan2(rotatedX, rotatedZ);
        @Pc(136) int edgeX = (int) ((double) radius * Math.sin(angle));
        @Pc(143) int edgeZ = (int) ((double) radius * Math.cos(angle));
        Sprites.hintMapedge[sprite].renderRotated((float) edgeX + (float) component.width / 2.0F + (float) offsetX, (float) -edgeZ + (float) component.height / 2.0F + (float) offsetY, 4096, (int) ((-angle / 6.283185307179586D) * 65535.0D));
    }

    @OriginalMember(owner = "client!ns", name = "a", descriptor = "(B)V")
    public static void resetToggle() {
        flagY = -1;
        level = -1;
        flagX = -1;
        toggle = 0;
    }

    @OriginalMember(owner = "client!tba", name = "b", descriptor = "(B)V")
    public static void resetFlag() {
        flagX = -1;
        toggle = 0;
        flagY = -1;
    }

    @OriginalMember(owner = "client!dfa", name = "a", descriptor = "(ILclient!ha;)V")
    public static void redrawIfLevelChanged(@OriginalArg(1) Toolkit toolkit) {
        if (level != PlayerEntity.self.level && (Static334.activeTiles != null && drawLevel(toolkit, PlayerEntity.self.level))) {
            level = PlayerEntity.self.level;
        }
    }

    @OriginalMember(owner = "client!eh", name = "a", descriptor = "(Lclient!ha;[II)V")
    public static void fillLandmarkPolygon(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int[] vertices, @OriginalArg(2) int colour) {
        PolygonFiller.fillPolygon(toolkit, vertices, vertices.length, colour, null, null);
    }
}

import com.jagex.core.io.Packet;
import com.jagex.game.collision.CollisionMap;
import com.jagex.game.runetek6.config.flotype.FloorOverlayType;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayType;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayTypeList;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qja")
public class Terrain {

    @OriginalMember(owner = "client!ji", name = "I", descriptor = "[I")
    public static final int[] OVERLAY_FACE_COUNT = {2, 1, 1, 1, 2, 2, 2, 1, 3, 3, 3, 2, 0, 4, 0};

    @OriginalMember(owner = "client!iba", name = "i", descriptor = "[[I")
    public static final int[][] TILE_FACE_A = {{0, 2}, {0, 2}, {0, 0, 2}, {2, 0, 0}, {0, 2, 0}, {0, 0, 2}, {0, 5, 1, 4}, {0, 4, 4, 4}, {4, 4, 4, 0}, {6, 6, 6, 2, 2, 2}, {2, 2, 2, 6, 6, 6}, {0, 11, 6, 6, 6, 4}, {0, 2}, {0, 4, 4, 4}, {0, 4, 4, 4}};

    @OriginalMember(owner = "client!rfa", name = "w", descriptor = "[[I")
    public static final int[][] TILE_FACE_C = {{6, 6}, {6, 6}, {6, 5, 5}, {5, 6, 5}, {5, 5, 6}, {6, 5, 5}, {5, 0, 4, 1}, {7, 7, 1, 2}, {7, 1, 2, 7}, {8, 9, 4, 0, 8, 9}, {0, 8, 9, 8, 9, 4}, {11, 0, 10, 11, 4, 2}, {6, 6}, {7, 7, 1, 2}, {7, 7, 1, 2}};

    @OriginalMember(owner = "client!qb", name = "o", descriptor = "[[I")
    public static final int[][] BLENDED_FACE_B = {{2, 4, 6, 0}, {0, 2, 3, 5, 6, 4}, {0, 1, 4, 5}, {4, 6, 0, 2}, {2, 4, 0}, {0, 2, 4}, {6, 0, 1, 2, 4, 5}, {0, 1, 2, 4, 6, 7}, {4, 7, 6, 0}, {0, 8, 6, 1, 9, 2, 9, 4}, {2, 9, 4, 0, 8, 6}, {2, 11, 3, 7, 10, 10, 6, 6}, {2, 4, 6, 0}};

    @OriginalMember(owner = "client!he", name = "f", descriptor = "[[I")
    public static final int[][] BLENDED_FACE_A = {{0, 2, 4, 6}, {6, 0, 2, 3, 5, 3}, {6, 0, 2, 4}, {2, 5, 6, 1}, {0, 2, 6}, {6, 0, 2}, {5, 6, 0, 1, 2, 4}, {7, 7, 1, 2, 4, 6}, {2, 4, 4, 7}, {6, 6, 4, 0, 1, 1, 3, 3}, {0, 2, 2, 6, 6, 4}, {0, 2, 2, 3, 7, 0, 4, 3}, {0, 2, 4, 6}};

    @OriginalMember(owner = "client!gka", name = "q", descriptor = "[[I")
    public static final int[][] BLENDED_FACE_C = {{12, 12, 12, 12}, {12, 12, 12, 12, 12, 5}, {5, 5, 1, 1}, {5, 1, 1, 5}, {5, 5, 5}, {5, 5, 5}, {12, 12, 12, 12, 12, 12}, {1, 12, 12, 12, 12, 12}, {1, 1, 7, 1}, {8, 9, 9, 8, 8, 3, 1, 9}, {8, 8, 9, 8, 9, 9}, {10, 10, 11, 11, 11, 7, 3, 7}, {12, 12, 12, 12}};

    @OriginalMember(owner = "client!kv", name = "C", descriptor = "[I")
    public static final int[] BLENDED_OVERLAY_FACE_COUNT = {4, 2, 1, 1, 2, 2, 3, 1, 3, 3, 3, 2, 0};

    @OriginalMember(owner = "client!dd", name = "F", descriptor = "[I")
    public static final int[] UNDERLAY_FACE_COUNT = {0, 1, 2, 2, 1, 1, 2, 3, 1, 3, 3, 4, 2, 0, 4};

    @OriginalMember(owner = "client!uw", name = "y", descriptor = "[I")
    public static final int[] BLENDED_UNDERLAY_FACE_COUNT = {0, 4, 3, 3, 1, 1, 3, 5, 1, 5, 3, 6, 4};

    @OriginalMember(owner = "client!rga", name = "j", descriptor = "[[I")
    public static final int[][] BLENDED_EDGE_FACE = {{0, 1, 2, 3}, {1, -1, -1, 0}, {-1, 2, -1, 0}, {-1, 0, -1, 2}, {0, 1, -1, 2}, {1, 2, -1, 0}, {-1, 4, -1, 1}, {-1, 3, 4, -1}, {-1, 0, 2, -1}, {-1, -1, 2, 0}, {0, 2, 5, 3}, {0, -1, 6, -1}, {0, 1, 2, 3}};

    @OriginalMember(owner = "client!lw", name = "j", descriptor = "[I")
    public static final int[] SPLIT_UNDERLAY_FACE_COUNT = {0, 2, 2, 2, 1, 1, 3, 3, 1, 3, 3, 4, 4};

    @OriginalMember(owner = "client!sha", name = "k", descriptor = "[[I")
    public static final int[][] TILE_FACE_B = new int[][]{{2, 4}, {2, 4}, {5, 2, 4}, {4, 5, 2}, {2, 4, 5}, {5, 2, 4}, {1, 6, 2, 5}, {1, 6, 7, 1}, {6, 7, 1, 1}, {0, 8, 9, 8, 9, 4}, {8, 9, 4, 0, 8, 9}, {2, 10, 0, 10, 11, 11}, {2, 4}, {1, 6, 7, 1}, {1, 6, 7, 1}};

    @OriginalMember(owner = "client!tha", name = "f", descriptor = "[I")
    public static final int[] OVERLAY_BLEND_PRIORITIES = new int[13];

    @OriginalMember(owner = "client!oka", name = "c", descriptor = "[[Z")
    public static final boolean[][] BLENDED_EDGE_SPLITS = new boolean[][]{new boolean[4], {false, true, true, false}, {true, false, true, false}, {true, false, true, false}, {false, false, true, false}, {false, false, true, false}, {true, false, true, false}, {true, false, false, true}, {true, false, false, true}, {true, true, false, false}, new boolean[4], {false, true, false, true}, new boolean[4]};

    @OriginalMember(owner = "client!nm", name = "B", descriptor = "[[Z")
    public static final boolean[][] UNBLENDED_EDGE_SPLITS = new boolean[][]{new boolean[4], new boolean[4], {false, false, true, false}, {false, false, true, false}, {false, false, true, false}, {false, false, true, false}, {true, false, true, false}, {true, false, false, true}, {true, false, false, true}, new boolean[4], new boolean[4], new boolean[4], new boolean[4]};

    @OriginalMember(owner = "client!kba", name = "H", descriptor = "[I")
    public static final int[] OVERLAY_COLOURS = new int[13];

    @OriginalMember(owner = "client!ica", name = "n", descriptor = "[I")
    public static final int[] OVERLAY_BLEND_COLOURS = new int[13];

    @OriginalMember(owner = "client!eq", name = "m", descriptor = "[I")
    public static final int[] OVERLAY_TEXTURES = new int[13];

    @OriginalMember(owner = "client!pn", name = "db", descriptor = "[[Z")
    public static final boolean[][] OVERLAY_VERTICES = new boolean[][]{{true, true, true, true, true, true, true, true, true, true, true, true, true}, {true, true, true, false, false, false, true, true, false, false, false, false, true}, {true, false, false, false, false, true, true, true, false, false, false, false, false}, {false, false, true, true, true, true, false, false, false, false, false, false, false}, {true, true, true, true, true, true, false, false, false, false, false, false, false}, {true, true, true, false, false, true, true, true, false, false, false, false, false}, {true, true, false, false, false, true, true, true, false, false, false, false, true}, {true, true, false, false, false, false, false, true, false, false, false, false, false}, {false, true, true, true, true, true, true, true, false, false, false, false, false}, {true, false, false, false, true, true, true, true, true, true, false, false, false}, {true, true, true, true, true, false, false, false, true, true, false, false, false}, {true, true, true, false, false, false, false, false, false, false, true, true, false}, new boolean[13], {true, true, true, true, true, true, true, true, true, true, true, true, true}, new boolean[13]};

    @OriginalMember(owner = "client!hm", name = "b", descriptor = "[I")
    public static final int[] OVERLAY_BLEND_SOURCES = new int[13];

    @OriginalMember(owner = "client!pka", name = "d", descriptor = "[I")
    public static final int[] SPLIT_OVERLAY_FACE_COUNT = new int[]{4, 2, 1, 1, 2, 2, 3, 1, 3, 3, 3, 2, 0};

    @OriginalMember(owner = "client!pi", name = "c", descriptor = "[[I")
    public static final int[][] SPLIT_FACE_C = new int[][]{{12, 12, 12, 12}, {12, 12, 12, 12}, {5, 5, 5}, {5, 5, 5}, {5, 5, 5}, {5, 5, 5}, {12, 12, 12, 12, 12, 12}, {1, 1, 1, 7}, {1, 1, 7, 1}, {8, 9, 9, 8, 8, 9}, {8, 8, 9, 8, 9, 9}, {10, 10, 11, 11, 11, 10}, {12, 12, 12, 12}};

    @OriginalMember(owner = "client!ww", name = "c", descriptor = "[I")
    public static final int[] OVERLAY_SIZES = new int[13];

    @OriginalMember(owner = "client!ska", name = "H", descriptor = "[I")
    public static int[] faceVertices = new int[6];

    /**
     * Half-width of the box filter that averages neighbouring underlay colours, in tiles.
     */
    private static final int UNDERLAY_BLEND_RADIUS = 5;

    @OriginalMember(owner = "client!qja", name = "e", descriptor = "[[[B")
    public byte[][][] aByteArrayArrayArray12;

    @OriginalMember(owner = "client!qja", name = "j", descriptor = "[I")
    public final int[] tileOffsetY = {0, 0, 0, 256, 512, 512, 512, 256, 256, 384, 128, 128, 256};

    @OriginalMember(owner = "client!qja", name = "h", descriptor = "[I")
    public final int[] tileOffsetX = {0, 256, 512, 512, 512, 256, 0, 0, 128, 256, 128, 384, 256};

    @OriginalMember(owner = "client!qja", name = "k", descriptor = "Lclient!dh;")
    public final FloorUnderlayTypeList underlayTypeList;

    @OriginalMember(owner = "client!qja", name = "q", descriptor = "I")
    protected final int length;

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "I")
    public final int levels;

    @OriginalMember(owner = "client!qja", name = "m", descriptor = "Z")
    public final boolean underwater;

    @OriginalMember(owner = "client!qja", name = "n", descriptor = "I")
    protected final int width;

    @OriginalMember(owner = "client!qja", name = "l", descriptor = "Lclient!ef;")
    public final FloorOverlayTypeList floorOverlayTypeList;

    @OriginalMember(owner = "client!qja", name = "x", descriptor = "[[[B")
    public final byte[][][] tileDirections;

    @OriginalMember(owner = "client!qja", name = "g", descriptor = "[[[B")
    public final byte[][][] underlay;

    @OriginalMember(owner = "client!qja", name = "u", descriptor = "[[[B")
    protected byte[][][] occluderFlags;

    @OriginalMember(owner = "client!qja", name = "s", descriptor = "[[[B")
    public final byte[][][] overlay;

    @OriginalMember(owner = "client!qja", name = "C", descriptor = "[[[B")
    public final byte[][][] tileShapes;

    @OriginalMember(owner = "client!qja", name = "A", descriptor = "[[[I")
    public final int[][][] tileHeights;

    @OriginalMember(owner = "client!qja", name = "<init>", descriptor = "(IIIZLclient!ef;Lclient!dh;)V")
    protected Terrain(@OriginalArg(0) int levels, @OriginalArg(1) int width, @OriginalArg(2) int length, @OriginalArg(3) boolean underwater, @OriginalArg(4) FloorOverlayTypeList floorOverlayTypeList, @OriginalArg(5) FloorUnderlayTypeList underlayTypeList) {
        this.underlayTypeList = underlayTypeList;
        this.length = length;
        this.levels = levels;
        this.underwater = underwater;
        this.width = width;
        this.floorOverlayTypeList = floorOverlayTypeList;
        this.tileDirections = new byte[this.levels][this.width][this.length];
        this.underlay = new byte[this.levels][this.width][this.length];
        this.occluderFlags = new byte[this.levels][this.width + 1][this.length + 1];
        this.overlay = new byte[this.levels][this.width][this.length];
        this.tileShapes = new byte[this.levels][this.width][this.length];
        this.tileHeights = new int[this.levels][this.width + 1][this.length + 1];
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(Lclient!ge;I[Lclient!eq;IBII)V")
    public final void decodeMapSquare(@OriginalArg(0) Packet packet, @OriginalArg(2) CollisionMap[] collisionMaps, @OriginalArg(6) int absX, @OriginalArg(1) int absZ, @OriginalArg(5) int areaBaseX, @OriginalArg(3) int areaBaseZ) {
        if (!this.underwater) {
            for (@Pc(4) int level = 0; level < 4; level++) {
                @Pc(9) CollisionMap collisionMap = collisionMaps[level];

                for (@Pc(11) int localX = 0; localX < 64; localX++) {
                    for (@Pc(14) int localZ = 0; localZ < 64; localZ++) {
                        @Pc(19) int x = localX + absX;
                        @Pc(23) int z = localZ + absZ;

                        if (x >= 0 && this.width > x && z >= 0 && z < this.length) {
                            collisionMap.unflagBlocked(x, z);
                        }
                    }
                }
            }
        }

        @Pc(4) int x = absX + areaBaseX;
        @Pc(84) int z = absZ + areaBaseZ;
        for (@Pc(11) int level = 0; level < this.levels; level++) {
            for (@Pc(14) int localX = 0; localX < 64; localX++) {
                for (@Pc(19) int localZ = 0; localZ < 64; localZ++) {
                    this.decodeTile(packet, localX + absX, absZ + localZ, x + localX, z + localZ, 0, 0, level, 0, false);
                }
            }
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(IIIII)V")
    public final void setMapSquareHeights(@OriginalArg(2) int x, @OriginalArg(4) int z) {
        for (@Pc(1) int level = 0; level < this.levels; level++) {
            this.setTileHeights(x, z, level, 64, 64);
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(I[[[ILclient!ha;[Lclient!eq;)V")
    public final void createGrounds(@OriginalArg(1) int[][][] waterHeights, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) CollisionMap[] collisionMaps) {
        @Pc(4) int level;
        @Pc(7) int groundFlags;
        @Pc(10) int featureFlags;
        if (!this.underwater) {
            for (level = 0; level < 4; level++) {
                for (int x = 0; x < this.width; x++) {
                    for (int z = 0; z < this.length; z++) {
                        if ((Static280.tileFlags[level][x][z] & 0x1) != 0) {
                            @Pc(26) int blockedLevel = level;
                            if ((Static280.tileFlags[1][x][z] & 0x2) != 0) {
                                blockedLevel = level - 1;
                            }
                            if (blockedLevel >= 0) {
                                collisionMaps[blockedLevel].flagBlocked(z, x);
                            }
                        }
                    }
                }
            }
        }
        for (level = 0; level < this.levels; level++) {
            groundFlags = 0;
            featureFlags = 0;
            if (!this.underwater) {
                if (Static50.aBoolean566) {
                    featureFlags = 8;
                }
                if (Static305.aBoolean371) {
                    groundFlags = 2;
                }
                if (Static439.anInt6674 != 0) {
                    groundFlags |= 0x1;
                    if (level == 0 | Static428.aBoolean487) {
                        featureFlags |= 0x10;
                    }
                }
            }
            if (Static305.aBoolean371) {
                featureFlags |= 0x7;
            }
            if (!Static196.aBoolean262) {
                featureFlags |= 0x20;
            }
            @Pc(165) int[][] heights = waterHeights == null || level >= waterHeights.length ? this.tileHeights[level] : waterHeights[level];
            Static429.method5805(level, toolkit.createGround(this.width, this.length, this.tileHeights[level], heights, groundFlags, featureFlags));
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(IBLclient!ha;[[ILclient!s;Lclient!s;Lclient!s;)V")
    public void loadUnblended(@OriginalArg(0) int level, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) int[][] colour, @OriginalArg(4) Ground surfaceGround, @OriginalArg(5) Ground ground, @OriginalArg(6) Ground underwaterGround) {
        for (@Pc(1) int x = 0; x < this.width; x++) {
            for (@Pc(4) int z = 0; z < this.length; z++) {

                if (AnimatedBackground.level == -1 || Static696.isTileVisibleFrom(z, AnimatedBackground.level, x, level)) {
                    @Pc(28) byte shape = this.tileShapes[level][x][z];
                    @Pc(37) byte direction = this.tileDirections[level][x][z];
                    @Pc(48) int overlay = this.overlay[level][x][z] & 0xFF;
                    @Pc(59) int underlay = this.underlay[level][x][z] & 0xFF;

                    @Pc(72) FloorOverlayType overlayType = overlay == 0 ? null : this.floorOverlayTypeList.list(overlay - 1);
                    if (shape == 0 && overlayType == null) {
                        shape = 12;
                    }

                    @Pc(93) FloorUnderlayType underlayType = underlay == 0 ? null : this.underlayTypeList.list(underlay - 1);
                    @Pc(95) FloorOverlayType occluderOverlayType = overlayType;
                    if (overlayType != null && overlayType.colour == -1 && overlayType.blendColour == -1) {
                        occluderOverlayType = overlayType;
                        overlayType = null;
                    }

                    if (overlayType != null || underlayType != null) {
                        @Pc(125) int underlayFaces = UNDERLAY_FACE_COUNT[shape];
                        @Pc(129) int overlayFaces = OVERLAY_FACE_COUNT[shape];
                        @Pc(143) int faces = (underlayType == null ? 0 : underlayFaces) + (overlayType == null ? 0 : overlayFaces);
                        @Pc(145) int faceIndex = 0;
                        @Pc(147) int vertexIndex = 0;
                        @Pc(155) int overlayTexture = overlayType == null ? -1 : overlayType.texture;
                        @Pc(163) int underlayTexture = underlayType == null ? -1 : underlayType.texture;
                        @Pc(166) int[] faceA = new int[faces];
                        @Pc(169) int[] faceB = new int[faces];
                        @Pc(172) int[] faceC = new int[faces];
                        @Pc(175) int[] colours = new int[faces];
                        @Pc(178) int[] textures = new int[faces];
                        @Pc(181) int[] sizes = new int[faces];
                        @Pc(195) int[] blendedColours = overlayType == null || overlayType.blendColour == -1 ? null : new int[faces];

                        if (overlayType == null) {
                            vertexIndex = overlayFaces;
                        } else {
                            for (@Pc(199) int i = 0; i < overlayFaces; i++) {
                                faceA[faceIndex] = TILE_FACE_A[shape][vertexIndex];
                                faceB[faceIndex] = TILE_FACE_B[shape][vertexIndex];
                                faceC[faceIndex] = TILE_FACE_C[shape][vertexIndex];
                                textures[faceIndex] = overlayTexture;
                                sizes[faceIndex] = overlayType.size;
                                colours[faceIndex] = overlayType.colour;
                                if (blendedColours != null) {
                                    blendedColours[faceIndex] = overlayType.blendColour;
                                }
                                faceIndex++;
                                vertexIndex++;
                            }

                            if (!this.underwater && level == 0) {
                                Static295.setWaterParams(x, z, overlayType.waterColour, overlayType.waterDepth * 8, overlayType.waterBias);
                            }
                        }

                        if (underlayType != null) {
                            for (@Pc(199) int i = 0; i < underlayFaces; i++) {
                                faceA[faceIndex] = TILE_FACE_A[shape][vertexIndex];
                                faceB[faceIndex] = TILE_FACE_B[shape][vertexIndex];
                                faceC[faceIndex] = TILE_FACE_C[shape][vertexIndex];
                                textures[faceIndex] = underlayTexture;
                                sizes[faceIndex] = underlayType.size;
                                colours[faceIndex] = colour[x][z];

                                if (blendedColours != null) {
                                    blendedColours[faceIndex] = colours[faceIndex];
                                }

                                vertexIndex++;
                                faceIndex++;
                            }
                        }

                        @Pc(199) int vertices = this.tileOffsetX.length;
                        @Pc(352) int[] offsetX = new int[vertices];
                        @Pc(355) int[] offsetY = new int[vertices];
                        @Pc(363) int[] offsetLevel = surfaceGround == null ? null : new int[vertices];
                        @Pc(375) int[] depths = surfaceGround == null && underwaterGround == null ? null : new int[vertices];

                        for (@Pc(377) int i = 0; i < vertices; i++) {
                            @Pc(383) int deltaX = this.tileOffsetX[i];
                            @Pc(388) int deltaY = this.tileOffsetY[i];

                            if (direction == 0) {
                                offsetX[i] = deltaX;
                                offsetY[i] = deltaY;
                            } else if (direction == 1) {
                                offsetX[i] = deltaY;
                                offsetY[i] = 512 - deltaX;
                            } else if (direction == 2) {
                                offsetX[i] = 512 - deltaX;
                                offsetY[i] = 512 - deltaY;
                            } else if (direction == 3) {
                                offsetX[i] = 512 - deltaY;
                                offsetY[i] = deltaX;
                            }

                            if (offsetLevel != null && OVERLAY_VERTICES[shape][i]) {
                                @Pc(477) int waterX = offsetX[i] + (x << 9);
                                @Pc(485) int waterY = (z << 9) + offsetY[i];
                                offsetLevel[i] = surfaceGround.averageHeight(waterX, waterY) - ground.averageHeight(waterX, waterY);
                            }

                            if (depths != null) {
                                if (surfaceGround != null && !OVERLAY_VERTICES[shape][i]) {
                                    @Pc(477) int waterX = offsetX[i] + (x << 9);
                                    @Pc(485) int waterY = (z << 9) + offsetY[i];
                                    depths[i] = ground.averageHeight(waterX, waterY) - surfaceGround.averageHeight(waterX, waterY);
                                } else if (underwaterGround != null && !Static355.aBooleanArrayArray4[shape][i]) {
                                    @Pc(477) int waterX = (x << 9) + offsetX[i];
                                    @Pc(485) int waterY = offsetY[i] + (z << 9);
                                    depths[i] = underwaterGround.averageHeight(waterX, waterY) - ground.averageHeight(waterX, waterY);
                                }
                            }
                        }

                        @Pc(383) int heightSW = ground.getHeight(x, z);
                        @Pc(388) int heightSE = ground.getHeight(x + 1, z);
                        @Pc(477) int heightNE = ground.getHeight(x - -1, z + 1);
                        @Pc(485) int heightNW = ground.getHeight(x, z + 1);

                        @Pc(633) boolean bridge = Static441.isBridgeAt(z, x);
                        if (bridge && level > 1 || !bridge && level > 0) {
                            @Pc(652) boolean occlused = true;
                            if (underlayType != null && !underlayType.occludes) {
                                occlused = false;
                            } else if (underlay == 0 && shape != 0) {
                                occlused = false;
                            } else if (overlay > 0 && occluderOverlayType != null && !occluderOverlayType.occludes) {
                                occlused = false;
                            }

                            if (occlused && heightSE == heightSW && heightNE == heightSW && heightSW == heightNW) {
                                this.occluderFlags[level][x][z] = (byte) (this.occluderFlags[level][x][z] | 0x4);
                            }
                        }

                        @Pc(740) int waterColour = 0;
                        @Pc(742) int waterDepth = 0;
                        @Pc(744) int waterBias = 0;

                        if (this.underwater) {
                            waterColour = Static100.getWaterColour(x, z);
                            waterDepth = Static350.getWaterDepth(x, z);
                            waterBias = Static339.getWaterBias(x, z);
                        }

                        ground.addTile(x, z, offsetX, offsetLevel, offsetY, depths, faceA, faceB, faceC, colours, blendedColours, textures, sizes, waterColour, waterDepth, waterBias);
                        Static527.method7084(level, x, z);
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(ILclient!re;[[BIIIIILclient!ha;I[ZLclient!nq;[[B[[B)V")
    public void blendOverlay(@OriginalArg(0) int shape, @OriginalArg(1) FloorOverlayType overlayType, @OriginalArg(2) byte[][] directions, @OriginalArg(3) int width, @OriginalArg(5) int x, @OriginalArg(6) int direction, @OriginalArg(7) int length, @OriginalArg(8) Toolkit toolkit, @OriginalArg(9) int z, @OriginalArg(10) boolean[] edgeSplits, @OriginalArg(11) FloorUnderlayType underlayType, @OriginalArg(12) byte[][] shapes, @OriginalArg(13) byte[][] overlays) {
        @Pc(19) boolean[] tileEdgeSplits = overlayType != null && overlayType.blendable ? BLENDED_EDGE_SPLITS[shape] : UNBLENDED_EDGE_SPLITS[shape];

        if (z > 0) {
            if (x > 0) {
                @Pc(37) int overlaySW = overlays[x - 1][z - 1] & 0xFF;

                if (overlaySW > 0) {
                    @Pc(50) FloorOverlayType overlayTypeSW = this.floorOverlayTypeList.list(overlaySW - 1);
                    if (overlayTypeSW.colour != -1 && overlayTypeSW.blendable) {
                        @Pc(70) byte shapeSW = shapes[x - 1][z - 1];
                        @Pc(86) int directionSW = ((directions[x - 1][z - 1] * 2) + 4) & 0x7;
                        @Pc(91) int colourSW = Static718.blendColour(overlayTypeSW, toolkit);

                        if (OVERLAY_VERTICES[shapeSW][directionSW]) {
                            OVERLAY_COLOURS[0] = overlayTypeSW.colour;
                            OVERLAY_BLEND_COLOURS[0] = colourSW;
                            OVERLAY_TEXTURES[0] = overlayTypeSW.texture;
                            OVERLAY_SIZES[0] = overlayTypeSW.size;
                            OVERLAY_BLEND_PRIORITIES[0] = overlayTypeSW.blendPriority;
                            OVERLAY_BLEND_SOURCES[0] = 256;
                        }
                    }
                }
            }

            if (x < width - 1) {
                @Pc(37) int overlaySE = overlays[x + 1][z - 1] & 0xFF;

                if (overlaySE > 0) {
                    @Pc(50) FloorOverlayType overlayTypeSE = this.floorOverlayTypeList.list(overlaySE - 1);

                    if (overlayTypeSE.colour != -1 && overlayTypeSE.blendable) {
                        @Pc(70) byte shapeSE = shapes[x + 1][z - 1];
                        @Pc(86) int directionSE = ((directions[x + 1][z - 1] * 2) + 6) & 0x7;
                        @Pc(91) int colourSE = Static718.blendColour(overlayTypeSE, toolkit);
                        if (OVERLAY_VERTICES[shapeSE][directionSE]) {
                            OVERLAY_COLOURS[2] = overlayTypeSE.colour;
                            OVERLAY_BLEND_COLOURS[2] = colourSE;
                            OVERLAY_TEXTURES[2] = overlayTypeSE.texture;
                            OVERLAY_SIZES[2] = overlayTypeSE.size;
                            OVERLAY_BLEND_PRIORITIES[2] = overlayTypeSE.blendPriority;
                            OVERLAY_BLEND_SOURCES[2] = 512;
                        }
                    }
                }
            }
        }

        if (z < length - 1) {
            if (x > 0) {
                @Pc(37) int overlayNW = overlays[x - 1][z + 1] & 0xFF;

                if (overlayNW > 0) {
                    @Pc(50) FloorOverlayType overlayTypeNW = this.floorOverlayTypeList.list(overlayNW - 1);

                    if (overlayTypeNW.colour != -1 && overlayTypeNW.blendable) {
                        @Pc(70) byte shapeNW = shapes[x - 1][z + 1];
                        @Pc(86) int directionNW = directions[x - 1][z + 1] * 2 + 2 & 0x7;
                        @Pc(91) int colourNW = Static718.blendColour(overlayTypeNW, toolkit);

                        if (OVERLAY_VERTICES[shapeNW][directionNW]) {
                            OVERLAY_COLOURS[6] = overlayTypeNW.colour;
                            OVERLAY_BLEND_COLOURS[6] = colourNW;
                            OVERLAY_TEXTURES[6] = overlayTypeNW.texture;
                            OVERLAY_SIZES[6] = overlayTypeNW.size;
                            OVERLAY_BLEND_PRIORITIES[6] = overlayTypeNW.blendPriority;
                            OVERLAY_BLEND_SOURCES[6] = 64;
                        }
                    }
                }
            }

            if (width - 1 > x) {
                @Pc(37) int overlayNE = overlays[x + 1][z + 1] & 0xFF;

                if (overlayNE > 0) {
                    @Pc(50) FloorOverlayType overlayTypeNE = this.floorOverlayTypeList.list(overlayNE - 1);

                    if (overlayTypeNE.colour != -1 && overlayTypeNE.blendable) {
                        @Pc(70) byte shapeNE = shapes[x + 1][z + 1];
                        @Pc(86) int directionNE = (directions[x + 1][z + 1] * 2) & 0x7;
                        @Pc(91) int colourNE = Static718.blendColour(overlayTypeNE, toolkit);

                        if (OVERLAY_VERTICES[shapeNE][directionNE]) {
                            OVERLAY_COLOURS[4] = overlayTypeNE.colour;
                            OVERLAY_BLEND_COLOURS[4] = colourNE;
                            OVERLAY_TEXTURES[4] = overlayTypeNE.texture;
                            OVERLAY_SIZES[4] = overlayTypeNE.size;
                            OVERLAY_BLEND_PRIORITIES[4] = overlayTypeNE.blendPriority;
                            OVERLAY_BLEND_SOURCES[4] = 128;
                        }
                    }
                }
            }
        }

        if (z > 0) {
            @Pc(37) int overlaySouth = overlays[x][z - 1] & 0xFF;

            if (overlaySouth > 0) {
                @Pc(50) FloorOverlayType overlayTypeSouth = this.floorOverlayTypeList.list(overlaySouth - 1);

                if (overlayTypeSouth.colour != -1) {
                    @Pc(70) byte shapeSouth = shapes[x][z - 1];
                    @Pc(498) byte directionSouth = directions[x][z - 1];

                    if (overlayTypeSouth.blendable) {
                        @Pc(91) int vertex = 2;
                        @Pc(509) int neighbourVertex = (directionSouth * 2) + 4;
                        @Pc(514) int blendColour = Static718.blendColour(overlayTypeSouth, toolkit);

                        for (@Pc(516) int i = 0; i < 3; i++) {
                            vertex &= 0x7;
                            neighbourVertex &= 0x7;

                            if (OVERLAY_VERTICES[shapeSouth][neighbourVertex] && overlayTypeSouth.blendPriority >= OVERLAY_BLEND_PRIORITIES[vertex]) {
                                OVERLAY_COLOURS[vertex] = overlayTypeSouth.colour;
                                OVERLAY_BLEND_COLOURS[vertex] = blendColour;
                                OVERLAY_TEXTURES[vertex] = overlayTypeSouth.texture;
                                OVERLAY_SIZES[vertex] = overlayTypeSouth.size;

                                if (OVERLAY_BLEND_PRIORITIES[vertex] == overlayTypeSouth.blendPriority) {
                                    OVERLAY_BLEND_SOURCES[vertex] |= 0x20;
                                } else {
                                    OVERLAY_BLEND_SOURCES[vertex] = 0x20;
                                }

                                OVERLAY_BLEND_PRIORITIES[vertex] = overlayTypeSouth.blendPriority;
                            }

                            vertex--;
                            neighbourVertex++;
                        }

                        if (!tileEdgeSplits[direction & 0x3]) {
                            edgeSplits[0] = BLENDED_EDGE_SPLITS[shapeSouth][(directionSouth + 2) & 0x3];
                        }
                    } else if (!tileEdgeSplits[direction & 0x3]) {
                        edgeSplits[0] = UNBLENDED_EDGE_SPLITS[shapeSouth][(directionSouth + 2) & 0x3];
                    }
                }
            }
        }

        if (z < length - 1) {
            @Pc(37) int overlayNorth = overlays[x][z + 1] & 0xFF;

            if (overlayNorth > 0) {
                @Pc(50) FloorOverlayType overlayTypeNorth = this.floorOverlayTypeList.list(overlayNorth - 1);

                if (overlayTypeNorth.colour != -1) {
                    @Pc(70) byte shapeNorth = shapes[x][z + 1];
                    @Pc(498) byte directionNorth = directions[x][z + 1];

                    if (overlayTypeNorth.blendable) {
                        @Pc(91) int vertex = 4;
                        @Pc(509) int neighbourVertex = directionNorth * 2 + 2;
                        @Pc(514) int blendColour = Static718.blendColour(overlayTypeNorth, toolkit);

                        for (@Pc(516) int i = 0; i < 3; i++) {
                            vertex &= 0x7;
                            neighbourVertex &= 0x7;

                            if (OVERLAY_VERTICES[shapeNorth][neighbourVertex] && OVERLAY_BLEND_PRIORITIES[vertex] <= overlayTypeNorth.blendPriority) {
                                OVERLAY_COLOURS[vertex] = overlayTypeNorth.colour;
                                OVERLAY_BLEND_COLOURS[vertex] = blendColour;
                                OVERLAY_TEXTURES[vertex] = overlayTypeNorth.texture;
                                OVERLAY_SIZES[vertex] = overlayTypeNorth.size;

                                if (OVERLAY_BLEND_PRIORITIES[vertex] == overlayTypeNorth.blendPriority) {
                                    OVERLAY_BLEND_SOURCES[vertex] |= 0x10;
                                } else {
                                    OVERLAY_BLEND_SOURCES[vertex] = 0x10;
                                }

                                OVERLAY_BLEND_PRIORITIES[vertex] = overlayTypeNorth.blendPriority;
                            }

                            vertex++;
                            neighbourVertex--;
                        }

                        if (!tileEdgeSplits[(direction + 2) & 0x3]) {
                            edgeSplits[2] = BLENDED_EDGE_SPLITS[shapeNorth][--directionNorth & 0x3];
                        }
                    } else if (!tileEdgeSplits[(direction + 2) & 0x3]) {
                        edgeSplits[2] = UNBLENDED_EDGE_SPLITS[shapeNorth][directionNorth & 0x3];
                    }
                }
            }
        }

        if (x > 0) {
            @Pc(37) int overlayWest = overlays[x - 1][z] & 0xFF;

            if (overlayWest > 0) {
                @Pc(50) FloorOverlayType overlayTypeWest = this.floorOverlayTypeList.list(overlayWest - 1);

                if (overlayTypeWest.colour != -1) {
                    @Pc(70) byte shapeWest = shapes[x - 1][z];
                    @Pc(498) byte directionWest = directions[x - 1][z];

                    if (overlayTypeWest.blendable) {
                        @Pc(91) int vertex = 6;
                        @Pc(509) int neighbourVertex = directionWest * 2 + 4;
                        @Pc(514) int blendColour = Static718.blendColour(overlayTypeWest, toolkit);

                        for (@Pc(516) int i = 0; i < 3; i++) {
                            vertex &= 0x7;
                            neighbourVertex &= 0x7;

                            if (OVERLAY_VERTICES[shapeWest][neighbourVertex] && OVERLAY_BLEND_PRIORITIES[vertex] <= overlayTypeWest.blendPriority) {
                                OVERLAY_COLOURS[vertex] = overlayTypeWest.colour;
                                OVERLAY_BLEND_COLOURS[vertex] = blendColour;
                                OVERLAY_TEXTURES[vertex] = overlayTypeWest.texture;
                                OVERLAY_SIZES[vertex] = overlayTypeWest.size;

                                if (overlayTypeWest.blendPriority == OVERLAY_BLEND_PRIORITIES[vertex]) {
                                    OVERLAY_BLEND_SOURCES[vertex] |= 0x8;
                                } else {
                                    OVERLAY_BLEND_SOURCES[vertex] = 0x8;
                                }

                                OVERLAY_BLEND_PRIORITIES[vertex] = overlayTypeWest.blendPriority;
                            }

                            vertex++;
                            neighbourVertex--;
                        }

                        if (!tileEdgeSplits[(direction + 3) & 0x3]) {
                            edgeSplits[3] = BLENDED_EDGE_SPLITS[shapeWest][(directionWest + 1) & 0x3];
                        }
                    } else if (!tileEdgeSplits[(direction + 3) & 0x3]) {
                        edgeSplits[3] = UNBLENDED_EDGE_SPLITS[shapeWest][(directionWest + 1) & 0x3];
                    }
                }
            }
        }

        if (x < width - 1) {
            @Pc(37) int overlayEast = overlays[x + 1][z] & 0xFF;

            if (overlayEast > 0) {
                @Pc(50) FloorOverlayType overlayTypeEast = this.floorOverlayTypeList.list(overlayEast - 1);

                if (overlayTypeEast.colour != -1) {
                    @Pc(70) byte shapeEast = shapes[x + 1][z];
                    @Pc(498) byte directionEast = directions[x + 1][z];

                    if (overlayTypeEast.blendable) {
                        @Pc(91) int vertex = 4;
                        @Pc(509) int neighbourVertex = directionEast * 2 + 6;
                        @Pc(514) int blendColour = Static718.blendColour(overlayTypeEast, toolkit);

                        for (@Pc(516) int i = 0; i < 3; i++) {
                            neighbourVertex &= 0x7;
                            vertex &= 0x7;

                            if (OVERLAY_VERTICES[shapeEast][neighbourVertex] && overlayTypeEast.blendPriority >= OVERLAY_BLEND_PRIORITIES[vertex]) {
                                OVERLAY_COLOURS[vertex] = overlayTypeEast.colour;
                                OVERLAY_BLEND_COLOURS[vertex] = blendColour;
                                OVERLAY_TEXTURES[vertex] = overlayTypeEast.texture;
                                OVERLAY_SIZES[vertex] = overlayTypeEast.size;

                                if (OVERLAY_BLEND_PRIORITIES[vertex] == overlayTypeEast.blendPriority) {
                                    OVERLAY_BLEND_SOURCES[vertex] |= 0x4;
                                } else {
                                    OVERLAY_BLEND_SOURCES[vertex] = 0x4;
                                }

                                OVERLAY_BLEND_PRIORITIES[vertex] = overlayTypeEast.blendPriority;
                            }

                            vertex--;
                            neighbourVertex++;
                        }

                        if (!tileEdgeSplits[(direction + 1) & 0x3]) {
                            edgeSplits[1] = BLENDED_EDGE_SPLITS[shapeEast][(directionEast + 3) & 0x3];
                        }

                    } else if (!tileEdgeSplits[(direction + 1) & 0x3]) {
                        edgeSplits[1] = UNBLENDED_EDGE_SPLITS[shapeEast][(directionEast + 3) & 0x3];
                    }
                }
            }
        }

        if (overlayType != null && overlayType.blendable) {
            @Pc(37) int blendColour = Static718.blendColour(overlayType, toolkit);

            for (@Pc(1245) int i = 0; i < 8; i++) {
                @Pc(1255) int blendVertex = (i - (direction * 2)) & 0x7;

                if (OVERLAY_VERTICES[shape][i] && overlayType.blendPriority >= OVERLAY_BLEND_PRIORITIES[blendVertex]) {
                    OVERLAY_COLOURS[blendVertex] = overlayType.colour;
                    OVERLAY_BLEND_COLOURS[blendVertex] = blendColour;
                    OVERLAY_TEXTURES[blendVertex] = overlayType.texture;
                    OVERLAY_SIZES[blendVertex] = overlayType.size;

                    if (overlayType.blendPriority == OVERLAY_BLEND_PRIORITIES[blendVertex]) {
                        OVERLAY_BLEND_SOURCES[blendVertex] |= 0x2;
                    } else {
                        OVERLAY_BLEND_SOURCES[blendVertex] = 2;
                    }

                    OVERLAY_BLEND_PRIORITIES[blendVertex] = overlayType.blendPriority;
                }
            }
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "([[IBI)V")
    public final void addHeightOffsets(@OriginalArg(0) int[][] heightOffsets) {
        @Pc(16) int[][] heights = this.tileHeights[0];
        for (@Pc(18) int x = 0; x < this.width + 1; x++) {
            for (@Pc(21) int z = 0; z < this.length + 1; z++) {
                heights[x][z] += heightOffsets[x][z];
            }
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(IIIIILclient!ge;IIIIZ)V")
    public void decodeTile(@OriginalArg(5) Packet packet, @OriginalArg(3) int x, @OriginalArg(1) int z, @OriginalArg(4) int localX, @OriginalArg(7) int localZ, @OriginalArg(6) int offsetX, @OriginalArg(2) int offsetZ, @OriginalArg(8) int level, @OriginalArg(0) int rotation, @OriginalArg(10) boolean heightsOnly) {
        if (rotation == 1) {
            offsetZ = 1;
        } else if (rotation == 2) {
            offsetX = 1;
            offsetZ = 1;
        } else if (rotation == 3) {
            offsetX = 1;
        }

        if (x < 0 || x >= this.width || z < 0 || z >= this.length) {
            while (true) {
                @Pc(50) int code = packet.g1();
                if (code == 0) {
                    break;
                }

                if (code == 1) {
                    packet.g1();
                    break;
                }
                if (code <= 49) {
                    packet.g1();
                }
            }
            return;
        }

        if (!this.underwater && !heightsOnly) {
            Static280.tileFlags[level][x][z] = 0;
        }

        while (true) {
            @Pc(50) int code = packet.g1();
            if (code == 0) {
                if (this.underwater) {
                    this.tileHeights[0][x + offsetX][z + offsetZ] = 0;
                } else if (level == 0) {
                    this.tileHeights[0][x + offsetX][z + offsetZ] = -Static144.method2406(localX + 932731, localZ + 556238) * 8 << 2;
                } else {
                    this.tileHeights[level][x + offsetX][z + offsetZ] = this.tileHeights[level - 1][x + offsetX][z + offsetZ] - 960;
                }
                break;
            }

            if (code == 1) {
                @Pc(194) int height = packet.g1();
                if (this.underwater) {
                    this.tileHeights[0][offsetX + x][offsetZ + z] = height * 8 << 2;
                } else {
                    if (height == 1) {
                        height = 0;
                    }
                    if (level == 0) {
                        this.tileHeights[0][offsetX + x][offsetZ + z] = -height * 8 << 2;
                    } else {
                        this.tileHeights[level][offsetX + x][z + offsetZ] = this.tileHeights[level - 1][x + offsetX][offsetZ + z] - (height * 8 << 2);
                    }
                }
                break;
            }

            if (code <= 49) {
                if (heightsOnly) {
                    packet.g1();
                } else {
                    this.overlay[level][x][z] = packet.g1b();
                    this.tileShapes[level][x][z] = (byte) ((code - 2) / 4);
                    this.tileDirections[level][x][z] = (byte) (code + rotation - 2 & 0x3);
                }
            } else if (code <= 81) {
                if (!this.underwater && !heightsOnly) {
                    Static280.tileFlags[level][x][z] = (byte) (code - 49);
                }
            } else if (!heightsOnly) {
                this.underlay[level][x][z] = (byte) (code - 81);
            }
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(BLclient!ha;Lclient!s;Lclient!s;)V")
    public final void load(@OriginalArg(1) Toolkit toolkit, @OriginalArg(2) Ground underwaterGround, @OriginalArg(3) Ground surfaceGround) {
        if (Static397.anIntArray482 == null || this.length != Static397.anIntArray482.length) {
            Static501.anIntArray606 = new int[this.length];
            Static418.anIntArray704 = new int[this.length];
            Static397.anIntArray482 = new int[this.length];
            Static359.anIntArray449 = new int[this.length];
            Static467.anIntArray568 = new int[this.length];
        }
        @Pc(45) int[][] colour = new int[this.width][this.length];
        for (@Pc(47) int level = 0; level < this.levels; level++) {
            for (@Pc(50) int z = 0; z < this.length; z++) {
                Static397.anIntArray482[z] = 0;
                Static467.anIntArray568[z] = 0;
                Static501.anIntArray606[z] = 0;
                Static359.anIntArray449[z] = 0;
                Static418.anIntArray704[z] = 0;
            }
            for (@Pc(78) int x = -UNDERLAY_BLEND_RADIUS; x < this.width; x++) {
                for (@Pc(81) int columnZ = 0; columnZ < this.length; columnZ++) {
                    @Pc(86) int addedX = x + UNDERLAY_BLEND_RADIUS;
                    if (addedX < this.width) {
                        @Pc(101) int addedUnderlay = this.underlay[level][addedX][columnZ] & 0xFF;
                        if (addedUnderlay > 0) {
                            @Pc(114) FloorUnderlayType addedType = this.underlayTypeList.list(addedUnderlay - 1);
                            Static397.anIntArray482[columnZ] += addedType.anInt6630;
                            Static467.anIntArray568[columnZ] += addedType.anInt6637;
                            Static501.anIntArray606[columnZ] += addedType.anInt6639;
                            Static359.anIntArray449[columnZ] += addedType.anInt6632;
                            Static418.anIntArray704[columnZ]++;
                        }
                    }
                    @Pc(101) int removedX = x - UNDERLAY_BLEND_RADIUS;
                    if (removedX >= 0) {
                        @Pc(170) int removedUnderlay = this.underlay[level][removedX][columnZ] & 0xFF;
                        if (removedUnderlay > 0) {
                            @Pc(180) FloorUnderlayType removedType = this.underlayTypeList.list(removedUnderlay - 1);
                            Static397.anIntArray482[columnZ] -= removedType.anInt6630;
                            Static467.anIntArray568[columnZ] -= removedType.anInt6637;
                            Static501.anIntArray606[columnZ] -= removedType.anInt6639;
                            Static359.anIntArray449[columnZ] -= removedType.anInt6632;
                            Static418.anIntArray704[columnZ]--;
                        }
                    }
                }
                if (x >= 0) {
                    @Pc(86) int hue = 0;
                    @Pc(101) int saturation = 0;
                    @Pc(170) int lightness = 0;
                    @Pc(240) int hueWeight = 0;
                    @Pc(242) int count = 0;
                    for (@Pc(244) int z = -UNDERLAY_BLEND_RADIUS; z < this.length; z++) {
                        @Pc(249) int addedZ = z + UNDERLAY_BLEND_RADIUS;
                        if (this.length > addedZ) {
                            saturation += Static467.anIntArray568[addedZ];
                            hue += Static397.anIntArray482[addedZ];
                            hueWeight += Static359.anIntArray449[addedZ];
                            lightness += Static501.anIntArray606[addedZ];
                            count += Static418.anIntArray704[addedZ];
                        }
                        @Pc(291) int removedZ = z - UNDERLAY_BLEND_RADIUS;
                        if (removedZ >= 0) {
                            lightness -= Static501.anIntArray606[removedZ];
                            hueWeight -= Static359.anIntArray449[removedZ];
                            hue -= Static397.anIntArray482[removedZ];
                            count -= Static418.anIntArray704[removedZ];
                            saturation -= Static467.anIntArray568[removedZ];
                        }
                        if (z >= 0 && hueWeight > 0 && count > 0) {
                            colour[x][z] = Static318.method8555(lightness / count, saturation / count, hue * 256 / hueWeight);
                        }
                    }
                }
            }
            if (Static718.groundBlending) {
                this.loadBlended(level == 0 ? surfaceGround : null, colour, level == 0 ? underwaterGround : null, Static246.ground[level], toolkit, level);
            } else {
                this.loadUnblended(level, toolkit, colour, level == 0 ? surfaceGround : null, Static246.ground[level], level == 0 ? underwaterGround : null);
            }
            this.underlay[level] = null;
            this.overlay[level] = null;
            this.tileShapes[level] = null;
            this.tileDirections[level] = null;
        }
        if (!this.underwater) {
            if (Static439.anInt6674 != 0) {
                Static176.method6688();
            }
            if (Static305.aBoolean371) {
                Static358.method9182();
            }
        }
        for (@Pc(50) int level = 0; level < this.levels; level++) {
            Static246.ground[level].YA();
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(IIIIII)V")
    public final void setTileHeights(@OriginalArg(3) int x, @OriginalArg(2) int z, @OriginalArg(4) int level, @OriginalArg(5) int width, @OriginalArg(1) int length) {
        for (@Pc(5) int tileZ = z; tileZ < length + z; tileZ++) {
            for (@Pc(8) int tileX = x; tileX < x + width; tileX++) {
                if (tileX >= 0 && this.width > tileX && tileZ >= 0 && tileZ < this.length) {
                    this.tileHeights[level][tileX][tileZ] = level <= 0 ? 0 : this.tileHeights[level - 1][tileX][tileZ] - 960;
                }
            }
        }

        if (x > 0 && x < this.width) {
            for (@Pc(8) int localZ = z + 1; localZ < z + length; localZ++) {
                if (localZ >= 0 && localZ < this.length) {
                    this.tileHeights[level][x][localZ] = this.tileHeights[level][x - 1][localZ];
                }
            }
        }

        if (z > 0 && z < this.length) {
            for (@Pc(8) int localX = x + 1; localX < x + width; localX++) {
                if (localX >= 0 && localX < this.width) {
                    this.tileHeights[level][localX][z] = this.tileHeights[level][localX][z - 1];
                }
            }
        }

        if (x >= 0 && z >= 0 && x < this.width && z < this.length) {
            if (level == 0) {
                if (x > 0 && this.tileHeights[level][x - 1][z] != 0) {
                    this.tileHeights[level][x][z] = this.tileHeights[level][x - 1][z];
                } else if (z > 0 && this.tileHeights[level][x][z - 1] != 0) {
                    this.tileHeights[level][x][z] = this.tileHeights[level][x][z - 1];
                } else if (x > 0 && z > 0 && this.tileHeights[level][x - 1][z - 1] != 0) {
                    this.tileHeights[level][x][z] = this.tileHeights[level][x - 1][z - 1];
                }
            } else if (x > 0 && this.tileHeights[level][x - 1][z] != this.tileHeights[level - 1][x - 1][z]) {
                this.tileHeights[level][x][z] = this.tileHeights[level][x - 1][z];
            } else if (z > 0 && this.tileHeights[level][x][z - 1] != this.tileHeights[level - 1][x][z - 1]) {
                this.tileHeights[level][x][z] = this.tileHeights[level][x][z - 1];
            } else if (x > 0 && z > 0 && this.tileHeights[level - 1][x - 1][z - 1] != this.tileHeights[level][x - 1][z - 1]) {
                this.tileHeights[level][x][z] = this.tileHeights[level][x - 1][z - 1];
            }
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(Lclient!s;Z[[ILclient!s;Lclient!s;Lclient!ha;I)V")
    public void loadBlended(@OriginalArg(0) Ground surfaceGround, @OriginalArg(2) int[][] colours, @OriginalArg(3) Ground underwaterGround, @OriginalArg(4) Ground ground, @OriginalArg(5) Toolkit toolkit, @OriginalArg(6) int level) {
        @Pc(8) byte[][] shapes = this.tileShapes[level];
        @Pc(13) byte[][] directions = this.tileDirections[level];
        @Pc(26) byte[][] underlay = this.underlay[level];
        @Pc(31) byte[][] overlay = this.overlay[level];

        for (@Pc(33) int x = 0; x < this.width; x++) {
            @Pc(47) int nextX = this.width - 1 > x ? x + 1 : x;

            for (@Pc(49) int z = 0; z < this.length; z++) {
                @Pc(67) int nextZ = z < this.length - 1 ? z + 1 : z;

                if (AnimatedBackground.level == -1 || Static696.isTileVisibleFrom(z, AnimatedBackground.level, x, level)) {
                    @Pc(83) boolean allowShadow = false;
                    @Pc(85) boolean blendable = false;
                    @Pc(88) boolean[] edgeSplits = new boolean[4];

                    @Pc(94) int shape = shapes[x][z];
                    @Pc(100) int direction = directions[x][z];
                    @Pc(108) int overlaySW = overlay[x][z] & 0xFF;

                    @Pc(116) int underlaySW = underlay[x][z] & 0xFF;
                    @Pc(124) int underlayNW = underlay[x][nextZ] & 0xFF;
                    @Pc(132) int underlayNE = underlay[nextX][nextZ] & 0xFF;
                    @Pc(140) int underlaySE = underlay[nextX][z] & 0xFF;

                    if (overlaySW != 0 || underlaySW != 0) {
                        @Pc(164) FloorOverlayType overlayType = overlaySW == 0 ? null : this.floorOverlayTypeList.list(overlaySW - 1);
                        @Pc(177) FloorUnderlayType underlayType = underlaySW == 0 ? null : this.underlayTypeList.list(underlaySW - 1);
                        if (shape == 0 && overlayType == null) {
                            shape = 12;
                        }

                        @Pc(187) FloorOverlayType occluderOverlayType = overlayType;
                        if (overlayType != null) {
                            if (overlayType.colour != -1 || overlayType.blendColour != -1) {
                                if (underlayType != null && shape != 0) {
                                    blendable = overlayType.blendable;
                                }
                            } else {
                                occluderOverlayType = overlayType;
                                overlayType = null;
                            }
                        }

                        if ((shape == 0 || shape == 12) && x > 0 && z > 0 && x < this.width && this.length > z) {
                            @Pc(276) int matchSE = underlaySW == underlay[nextX][z - 1] ? 1 : -1;
                            @Pc(294) int matchSW = underlay[x - 1][z - 1] == underlaySW ? 1 : -1;
                            @Pc(308) int matchNE = underlaySW == underlay[nextX][nextZ] ? 1 : -1;

                            if (underlay[x][z - 1] == underlaySW) {
                                matchSE++;
                                matchSW++;
                            } else {
                                matchSE--;
                                matchSW--;
                            }

                            @Pc(345) int matchNW = underlay[x - 1][nextZ] == underlaySW ? 1 : -1;

                            if (underlaySW == underlay[nextX][z]) {
                                matchNE++;
                                matchSE++;
                            } else {
                                matchSE--;
                                matchNE--;
                            }

                            if (underlaySW == underlay[x][nextZ]) {
                                matchNE++;
                                matchNW++;
                            } else {
                                matchNE--;
                                matchNW--;
                            }

                            if (underlaySW == underlay[x - 1][z]) {
                                matchSW++;
                                matchNW++;
                            } else {
                                matchSW--;
                                matchNW--;
                            }

                            @Pc(391) int diagonalSWNE = matchSW - matchNE;
                            if (diagonalSWNE < 0) {
                                diagonalSWNE = -diagonalSWNE;
                            }

                            @Pc(403) int diagonalSENW = matchSE - matchNW;
                            if (diagonalSENW < 0) {
                                diagonalSENW = -diagonalSENW;
                            }

                            if (diagonalSWNE == diagonalSENW) {
                                diagonalSWNE = ground.getHeight(x, z) - ground.getHeight(nextX, nextZ);
                                if (diagonalSWNE < 0) {
                                    diagonalSWNE = -diagonalSWNE;
                                }

                                diagonalSENW = ground.getHeight(nextX, z) - ground.getHeight(x, nextZ);
                                if (diagonalSENW < 0) {
                                    diagonalSENW = -diagonalSENW;
                                }
                            }

                            direction = diagonalSENW > diagonalSWNE ? 1 : 0;
                        }

                        for (@Pc(294) int i = 0; i < 13; i++) {
                            OVERLAY_BLEND_PRIORITIES[i] = -1;
                            OVERLAY_BLEND_SOURCES[i] = 1;
                        }

                        @Pc(496) boolean[] tileEdgeSplits = overlayType != null && overlayType.blendable ? BLENDED_EDGE_SPLITS[shape] : UNBLENDED_EDGE_SPLITS[shape];
                        this.blendOverlay(shape, overlayType, directions, this.width, x, direction, this.length, toolkit, z, edgeSplits, underlayType, shapes, overlay);

                        @Pc(532) boolean blendOverlay = overlayType != null && overlayType.colour != overlayType.blendColour;
                        for (@Pc(345) int vertex = 0; vertex < 8 && !blendOverlay; vertex++) {
                            blendOverlay = OVERLAY_BLEND_PRIORITIES[vertex] >= 0 && OVERLAY_BLEND_COLOURS[vertex] != OVERLAY_COLOURS[vertex];
                        }

                        if (!tileEdgeSplits[(direction + 1) & 0x3]) {
                            edgeSplits[1] = Static588.or(edgeSplits[1], (OVERLAY_BLEND_SOURCES[2] & OVERLAY_BLEND_SOURCES[4]) == 0);
                        }
                        if (!tileEdgeSplits[(direction + 3) & 0x3]) {
                            edgeSplits[3] = Static588.or(edgeSplits[3], (OVERLAY_BLEND_SOURCES[6] & OVERLAY_BLEND_SOURCES[0]) == 0);
                        }
                        if (!tileEdgeSplits[direction & 0x3]) {
                            edgeSplits[0] = Static588.or(edgeSplits[0], (OVERLAY_BLEND_SOURCES[0] & OVERLAY_BLEND_SOURCES[2]) == 0);
                        }
                        if (!tileEdgeSplits[(direction + 2) & 0x3]) {
                            edgeSplits[2] = Static588.or(edgeSplits[2], (OVERLAY_BLEND_SOURCES[6] & OVERLAY_BLEND_SOURCES[4]) == 0);
                        }

                        if (!blendable && (shape == 0 || shape == 12)) {
                            if (edgeSplits[0] && !edgeSplits[1] && !edgeSplits[2] && edgeSplits[3]) {
                                edgeSplits[0] = edgeSplits[3] = false;
                                shape = shape == 0 ? 13 : 14;
                                direction = 0;
                            } else if (edgeSplits[0] && edgeSplits[1] && !edgeSplits[2] && !edgeSplits[3]) {
                                shape = shape == 0 ? 13 : 14;
                                direction = 3;
                                edgeSplits[0] = edgeSplits[1] = false;
                            } else if (!edgeSplits[0] && edgeSplits[1] && edgeSplits[2] && !edgeSplits[3]) {
                                shape = shape == 0 ? 13 : 14;
                                edgeSplits[1] = edgeSplits[2] = false;
                                direction = 2;
                            } else if (!edgeSplits[0] && !edgeSplits[1] && edgeSplits[2] && edgeSplits[3]) {
                                shape = shape == 0 ? 13 : 14;
                                direction = 1;
                                edgeSplits[2] = edgeSplits[3] = false;
                            }
                        }

                        @Pc(909) boolean simpleFaces = !blendable && !edgeSplits[0] && !edgeSplits[2] && !edgeSplits[1] && !edgeSplits[3];
                        @Pc(911) int[] edgeFaces = null;
                        @Pc(917) int[] faceA;
                        @Pc(934) int[] faceB;
                        @Pc(930) int[] faceC;
                        @Pc(391) int underlayFaces;
                        @Pc(403) int overlayFaces;
                        if (simpleFaces) {
                            faceA = TILE_FACE_A[shape];
                            overlayFaces = overlayType == null ? 0 : OVERLAY_FACE_COUNT[shape];
                            faceC = TILE_FACE_C[shape];
                            faceB = TILE_FACE_B[shape];
                            underlayFaces = underlayType == null ? 0 : UNDERLAY_FACE_COUNT[shape];
                        } else if (blendable) {
                            faceC = BLENDED_FACE_C[shape];
                            overlayFaces = overlayType == null ? 0 : BLENDED_OVERLAY_FACE_COUNT[shape];
                            faceA = BLENDED_FACE_A[shape];
                            edgeFaces = BLENDED_EDGE_FACE[shape];
                            faceB = BLENDED_FACE_B[shape];
                            underlayFaces = underlayType == null ? 0 : BLENDED_UNDERLAY_FACE_COUNT[shape];
                        } else {
                            underlayFaces = underlayType == null ? 0 : SPLIT_UNDERLAY_FACE_COUNT[shape];
                            faceC = SPLIT_FACE_C[shape];
                            overlayFaces = overlayType == null ? 0 : SPLIT_OVERLAY_FACE_COUNT[shape];
                            faceA = Static115.SPLIT_FACE_A[shape];
                            edgeFaces = Static264.SPLIT_EDGE_FACE[shape];
                            faceB = Static206.SPLIT_FACE_B[shape];
                        }

                        @Pc(1021) int faceCount = overlayFaces + underlayFaces;
                        if (faceCount <= 0) {
                            Static527.method7084(level, x, z);
                        } else {
                            if (edgeSplits[0]) {
                                faceCount++;
                            }
                            if (edgeSplits[2]) {
                                faceCount++;
                            }
                            if (edgeSplits[1]) {
                                faceCount++;
                            }
                            if (edgeSplits[3]) {
                                faceCount++;
                            }

                            @Pc(1062) int face = 0;
                            @Pc(1064) int vertexIndex = 0;
                            @Pc(1068) int vertexCount = faceCount * 3;
                            @Pc(1076) int[] overlayBlendColours = blendOverlay ? new int[vertexCount] : null;
                            @Pc(1079) int[] offsetX = new int[vertexCount];
                            @Pc(1082) int[] offsetY = new int[vertexCount];
                            @Pc(1085) int[] blendedColours = new int[vertexCount];
                            @Pc(1088) int[] blendedTextures = new int[vertexCount];
                            @Pc(1091) int[] blendedSizes = new int[vertexCount];
                            @Pc(1099) int[] offsetLevel = surfaceGround == null ? null : new int[vertexCount];
                            @Pc(1111) int[] waterDepths = surfaceGround == null && underwaterGround == null ? null : new int[vertexCount];

                            @Pc(1113) int colour = -1;
                            @Pc(1115) int texture = -1;
                            @Pc(1117) int size = 256;

                            if (overlayType == null) {
                                if (simpleFaces) {
                                    face = OVERLAY_FACE_COUNT[shape];
                                } else if (blendable) {
                                    face = BLENDED_OVERLAY_FACE_COUNT[shape];
                                } else {
                                    face = SPLIT_OVERLAY_FACE_COUNT[shape];
                                }
                            } else {
                                size = overlayType.size;
                                colour = overlayType.colour;
                                texture = overlayType.texture;

                                @Pc(1162) int overlayBlendColour = Static718.blendColour(overlayType, toolkit);

                                for (@Pc(1164) int i = 0; i < overlayFaces; i++) {
                                    @Pc(1277) byte faceVertexCount;

                                    if (edgeSplits[-direction & 0x3] && edgeFaces[0] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 1;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 1;
                                        faceVertices[4] = faceB[face];
                                        faceVertices[5] = faceC[face];
                                        faceVertexCount = 6;
                                    } else if (edgeSplits[(2 - direction) & 0x3] && edgeFaces[2] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 5;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 5;
                                        faceVertices[4] = faceB[face];
                                        faceVertices[5] = faceC[face];
                                        faceVertexCount = 6;
                                    } else if (edgeSplits[(1 - direction) & 0x3] && edgeFaces[1] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 3;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 3;
                                        faceVertices[4] = faceB[face];
                                        faceVertexCount = 6;
                                        faceVertices[5] = faceC[face];
                                    } else if (edgeSplits[(3 - direction) & 0x3] && edgeFaces[3] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 7;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 7;
                                        faceVertices[4] = faceB[face];
                                        faceVertexCount = 6;
                                        faceVertices[5] = faceC[face];
                                    } else {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = faceB[face];
                                        faceVertices[2] = faceC[face];
                                        faceVertexCount = 3;
                                    }

                                    for (@Pc(1411) int faceVertex = 0; faceVertex < faceVertexCount; faceVertex++) {
                                        @Pc(1416) int vertex = faceVertices[faceVertex];
                                        @Pc(1425) int blendVertex = (vertex - (direction * 2)) & 0x7;
                                        @Pc(1430) int deltaX = this.tileOffsetX[vertex];
                                        @Pc(1435) int deltaY = this.tileOffsetY[vertex];

                                        @Pc(1452) int rotatedX;
                                        @Pc(1448) int rotatedY;
                                        if (direction == 1) {
                                            rotatedY = 512 - deltaX;
                                            rotatedX = deltaY;
                                        } else if (direction == 2) {
                                            rotatedY = 512 - deltaY;
                                            rotatedX = 512 - deltaX;
                                        } else if (direction == 3) {
                                            rotatedX = 512 - deltaY;
                                            rotatedY = deltaX;
                                        } else {
                                            rotatedX = deltaX;
                                            rotatedY = deltaY;
                                        }

                                        offsetX[vertexIndex] = rotatedX;
                                        offsetY[vertexIndex] = rotatedY;
                                        if (offsetLevel != null && OVERLAY_VERTICES[shape][vertex]) {
                                            @Pc(1501) int worldX = rotatedX + (x << 9);
                                            @Pc(1508) int worldY = (z << 9) + rotatedY;
                                            offsetLevel[vertexIndex] = surfaceGround.averageHeight(worldX, worldY) - ground.averageHeight(worldX, worldY);
                                        }

                                        if (waterDepths != null) {
                                            if (surfaceGround != null && !OVERLAY_VERTICES[shape][vertex]) {
                                                @Pc(1501) int worldX = rotatedX + (x << 9);
                                                @Pc(1508) int worldY = (z << 9) + rotatedY;
                                                waterDepths[vertexIndex] = ground.averageHeight(worldX, worldY) - surfaceGround.averageHeight(worldX, worldY);
                                            } else if (underwaterGround != null && !Static355.aBooleanArrayArray4[shape][vertex]) {
                                                @Pc(1501) int worldX = rotatedX + (x << 9);
                                                @Pc(1508) int worldY = rotatedY + (z << 9);
                                                waterDepths[vertexIndex] = underwaterGround.averageHeight(worldX, worldY) - ground.averageHeight(worldX, worldY);
                                            }
                                        }

                                        if (vertex < 8 && OVERLAY_BLEND_PRIORITIES[blendVertex] > overlayType.blendPriority) {
                                            if (overlayBlendColours != null) {
                                                overlayBlendColours[vertexIndex] = OVERLAY_BLEND_COLOURS[blendVertex];
                                            }

                                            blendedSizes[vertexIndex] = OVERLAY_SIZES[blendVertex];
                                            blendedTextures[vertexIndex] = OVERLAY_TEXTURES[blendVertex];
                                            blendedColours[vertexIndex] = OVERLAY_COLOURS[blendVertex];
                                        } else {
                                            if (overlayBlendColours != null) {
                                                overlayBlendColours[vertexIndex] = overlayBlendColour;
                                            }

                                            blendedTextures[vertexIndex] = overlayType.texture;
                                            blendedSizes[vertexIndex] = overlayType.size;
                                            blendedColours[vertexIndex] = colour;
                                        }

                                        vertexIndex++;
                                    }

                                    face++;
                                }

                                if (!this.underwater && level == 0) {
                                    Static295.setWaterParams(x, z, overlayType.waterColour, overlayType.waterDepth * 8, overlayType.waterBias);
                                }

                                if (shape != 12 && overlayType.colour != -1 && overlayType.blockShadow) {
                                    allowShadow = true;
                                }
                            }

                            if (underlayType != null) {
                                if (underlaySE == 0) {
                                    underlaySE = underlaySW;
                                }

                                if (underlayNE == 0) {
                                    underlayNE = underlaySW;
                                }

                                if (underlayNW == 0) {
                                    underlayNW = underlaySW;
                                }

                                @Pc(1750) FloorUnderlayType underlayTypeSW = this.underlayTypeList.list(underlaySW - 1);
                                @Pc(1758) FloorUnderlayType underlayTypeNW = this.underlayTypeList.list(underlayNW - 1);
                                @Pc(1766) FloorUnderlayType underlayTypeNE = this.underlayTypeList.list(underlayNE - 1);
                                @Pc(1774) FloorUnderlayType underlayTypeSE = this.underlayTypeList.list(underlaySE - 1);

                                for (@Pc(1425) int i = 0; i < underlayFaces; i++) {
                                    @Pc(1277) byte faceVertexCount;

                                    if (edgeSplits[-direction & 0x3] && edgeFaces[0] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 1;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 1;
                                        faceVertices[4] = faceB[face];
                                        faceVertexCount = 6;
                                        faceVertices[5] = faceC[face];
                                    } else if (edgeSplits[2 - direction & 0x3] && edgeFaces[2] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 5;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 5;
                                        faceVertices[4] = faceB[face];
                                        faceVertexCount = 6;
                                        faceVertices[5] = faceC[face];
                                    } else if (edgeSplits[1 - direction & 0x3] && edgeFaces[1] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 3;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 3;
                                        faceVertices[4] = faceB[face];
                                        faceVertexCount = 6;
                                        faceVertices[5] = faceC[face];
                                    } else if (edgeSplits[3 - direction & 0x3] && edgeFaces[3] == face) {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = 7;
                                        faceVertices[2] = faceC[face];
                                        faceVertices[3] = 7;
                                        faceVertices[4] = faceB[face];
                                        faceVertices[5] = faceC[face];
                                        faceVertexCount = 6;
                                    } else {
                                        faceVertices[0] = faceA[face];
                                        faceVertices[1] = faceB[face];
                                        faceVertexCount = 3;
                                        faceVertices[2] = faceC[face];
                                    }

                                    face++;

                                    for (@Pc(1430) int faceVertex = 0; faceVertex < faceVertexCount; faceVertex++) {
                                        @Pc(1452) int vertex = faceVertices[faceVertex];
                                        @Pc(1435) int blendVertex = (vertex - (direction * 2)) & 0x7;
                                        @Pc(1448) int deltaX = this.tileOffsetX[vertex];
                                        @Pc(1508) int deltaY = this.tileOffsetY[vertex];

                                        @Pc(2056) int rotatedY;
                                        @Pc(1501) int rotatedX;
                                        if (direction == 1) {
                                            rotatedX = deltaY;
                                            rotatedY = 512 - deltaX;
                                        } else if (direction == 2) {
                                            rotatedX = 512 - deltaX;
                                            rotatedY = 512 - deltaY;
                                        } else if (direction == 3) {
                                            rotatedY = deltaX;
                                            rotatedX = 512 - deltaY;
                                        } else {
                                            rotatedX = deltaX;
                                            rotatedY = deltaY;
                                        }

                                        offsetX[vertexIndex] = rotatedX;
                                        offsetY[vertexIndex] = rotatedY;

                                        @Pc(2106) int worldX;
                                        @Pc(2112) int worldY;
                                        if (offsetLevel != null && OVERLAY_VERTICES[shape][vertex]) {
                                            worldX = rotatedX + (x << 9);
                                            worldY = rotatedY + (z << 9);
                                            offsetLevel[vertexIndex] = surfaceGround.averageHeight(worldX, worldY) - ground.averageHeight(worldX, worldY);
                                        }

                                        if (waterDepths != null) {
                                            if (surfaceGround != null && !OVERLAY_VERTICES[shape][vertex]) {
                                                worldX = (x << 9) + rotatedX;
                                                worldY = rotatedY + (z << 9);
                                                waterDepths[vertexIndex] = ground.averageHeight(worldX, worldY) - surfaceGround.averageHeight(worldX, worldY);
                                            } else if (underwaterGround != null && !Static355.aBooleanArrayArray4[shape][vertex]) {
                                                worldX = rotatedX + (x << 9);
                                                worldY = (z << 9) + rotatedY;
                                                waterDepths[vertexIndex] = underwaterGround.averageHeight(worldX, worldY) - ground.averageHeight(worldX, worldY);
                                            }
                                        }

                                        if (vertex < 8 && OVERLAY_BLEND_PRIORITIES[blendVertex] >= 0) {
                                            if (overlayBlendColours != null) {
                                                overlayBlendColours[vertexIndex] = OVERLAY_BLEND_COLOURS[blendVertex];
                                            }
                                            blendedSizes[vertexIndex] = OVERLAY_SIZES[blendVertex];
                                            blendedTextures[vertexIndex] = OVERLAY_TEXTURES[blendVertex];
                                            blendedColours[vertexIndex] = OVERLAY_COLOURS[blendVertex];
                                        } else {
                                            if (blendable && OVERLAY_VERTICES[shape][vertex]) {
                                                blendedTextures[vertexIndex] = texture;
                                                blendedSizes[vertexIndex] = size;
                                                blendedColours[vertexIndex] = colour;
                                            } else if (rotatedX == 0 && rotatedY == 0) {
                                                blendedColours[vertexIndex] = colours[x][z];
                                                blendedTextures[vertexIndex] = underlayTypeSW.texture;
                                                blendedSizes[vertexIndex] = underlayTypeSW.size;
                                            } else if (rotatedX == 0 && rotatedY == 512) {
                                                blendedColours[vertexIndex] = colours[x][nextZ];
                                                blendedTextures[vertexIndex] = underlayTypeNW.texture;
                                                blendedSizes[vertexIndex] = underlayTypeNW.size;
                                            } else if (rotatedX == 512 && rotatedY == 512) {
                                                blendedColours[vertexIndex] = colours[nextX][nextZ];
                                                blendedTextures[vertexIndex] = underlayTypeNE.texture;
                                                blendedSizes[vertexIndex] = underlayTypeNE.size;
                                            } else if (rotatedX == 512 && rotatedY == 0) {
                                                blendedColours[vertexIndex] = colours[nextX][z];
                                                blendedTextures[vertexIndex] = underlayTypeSE.texture;
                                                blendedSizes[vertexIndex] = underlayTypeSE.size;
                                            } else {
                                                if (rotatedX >= 256) {
                                                    if (rotatedY < 256) {
                                                        blendedTextures[vertexIndex] = underlayTypeSE.texture;
                                                        blendedSizes[vertexIndex] = underlayTypeSE.size;
                                                    } else {
                                                        blendedTextures[vertexIndex] = underlayTypeNE.texture;
                                                        blendedSizes[vertexIndex] = underlayTypeNE.size;
                                                    }
                                                } else if (rotatedY < 256) {
                                                    blendedTextures[vertexIndex] = underlayTypeSW.texture;
                                                    blendedSizes[vertexIndex] = underlayTypeSW.size;
                                                } else {
                                                    blendedTextures[vertexIndex] = underlayTypeNW.texture;
                                                    blendedSizes[vertexIndex] = underlayTypeNW.size;
                                                }
                                                int colourSouth = Static273.method3966(colours[nextX][z], rotatedX << 7 >> 9, colours[x][z]);
                                                int colourNorth = Static273.method3966(colours[nextX][nextZ], rotatedX << 7 >> 9, colours[x][nextZ]);
                                                blendedColours[vertexIndex] = Static273.method3966(colourNorth, rotatedY << 7 >> 9, colourSouth);
                                            }

                                            if (overlayBlendColours != null) {
                                                overlayBlendColours[vertexIndex] = blendedColours[vertexIndex];
                                            }
                                        }

                                        vertexIndex++;
                                    }
                                }

                                if (shape != 0 && underlayType.allowShadow) {
                                    allowShadow = true;
                                }
                            }

                            @Pc(1162) int heightSW = ground.getHeight(x, z);
                            @Pc(1164) int heightSE = ground.getHeight(nextX, z);
                            @Pc(1411) int heightNE = ground.getHeight(nextX, nextZ);
                            @Pc(1416) int heightNW = ground.getHeight(x, nextZ);

                            @Pc(2560) boolean bridge = Static441.isBridgeAt(z, x);
                            if (bridge && level > 1 || !bridge && level > 0) {
                                @Pc(2579) boolean occludes = true;

                                if (underlayType != null && !underlayType.occludes) {
                                    occludes = false;
                                } else if (underlaySW == 0 && shape != 0) {
                                    occludes = false;
                                } else if (overlaySW > 0 && occluderOverlayType != null && !occluderOverlayType.occludes) {
                                    occludes = false;
                                }

                                if (occludes && heightSE == heightSW && heightNE == heightSW && heightSW == heightNW) {
                                    this.occluderFlags[level][x][z] = (byte) (this.occluderFlags[level][x][z] | 0x4);
                                }
                            }

                            @Pc(1430) int waterColour = 0;
                            @Pc(1452) int waterDepth = 0;
                            @Pc(1435) int waterBias = 0;
                            if (this.underwater) {
                                waterColour = Static100.getWaterColour(x, z);
                                waterDepth = Static350.getWaterDepth(x, z);
                                waterBias = Static339.getWaterBias(x, z);
                            }

                            ground.U(x, z, offsetX, offsetLevel, offsetY, waterDepths, blendedColours, overlayBlendColours, blendedTextures, blendedSizes, waterColour, waterDepth, waterBias, allowShadow);
                            Static527.method7084(level, x, z);
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!qja", name = "a", descriptor = "(IIIIILclient!ge;II[Lclient!eq;B)V")
    public final void decodeZone(@OriginalArg(2) int x, @OriginalArg(4) int z, @OriginalArg(0) int level, @OriginalArg(7) int pointerX, @OriginalArg(3) int pointerZ, @OriginalArg(1) int pointerLevel, @OriginalArg(6) int pointerRotation, @OriginalArg(5) Packet packet, @OriginalArg(8) CollisionMap[] collisionMaps) {
        @Pc(17) int pointerSquareX = (pointerX & 0x7) * 8;
        @Pc(23) int pointerSquareZ = (pointerZ & 0x7) * 8;

        if (!this.underwater) {
            @Pc(30) CollisionMap collisionMap = collisionMaps[level];
            for (@Pc(32) int zoneX = 0; zoneX < 8; zoneX++) {
                for (@Pc(35) int zoneZ = 0; zoneZ < 8; zoneZ++) {
                    @Pc(49) int tileX = x + MapRegion.rotateZoneX(zoneX & 0x7, zoneZ & 0x7, pointerRotation);
                    @Pc(61) int tileZ = z + MapRegion.rotateZoneZ(zoneX & 0x7, zoneZ & 0x7, pointerRotation);

                    if (tileX > 0 && this.width - 1 > tileX && tileZ > 0 && tileZ < this.length - 1) {
                        collisionMap.unflagBlocked(tileX, tileZ);
                    }
                }
            }
        }

        @Pc(117) int baseX = (pointerX & ~0x7) << 3;
        @Pc(32) int baseZ = (pointerZ & ~0x7) << 3;
        @Pc(125) byte offsetX = 0;
        @Pc(127) byte offsetZ = 0;
        if (pointerRotation == 1) {
            offsetZ = 1;
        } else if (pointerRotation == 2) {
            offsetZ = 1;
            offsetX = 1;
        } else if (pointerRotation == 3) {
            offsetX = 1;
        }

        for (@Pc(61) int squareLevel = 0; squareLevel < this.levels; squareLevel++) {
            for (@Pc(153) int squareX = 0; squareX < 64; squareX++) {
                for (@Pc(156) int squareZ = 0; squareZ < 64; squareZ++) {
                    if (pointerLevel == squareLevel && squareX >= pointerSquareX && squareX <= pointerSquareX + 8 && pointerSquareZ <= squareZ && squareZ <= pointerSquareZ + 8) {
                        @Pc(243) int zoneX;
                        @Pc(252) int zoneZ;

                        if (pointerSquareX + 8 == squareX || squareZ == pointerSquareZ + 8) {
                            if (pointerRotation == 0) {
                                zoneX = x + squareX - pointerSquareX;
                                zoneZ = z + squareZ - pointerSquareZ;
                            } else if (pointerRotation == 1) {
                                zoneX = x + squareZ - pointerSquareZ;
                                zoneZ = pointerSquareX + z + 8 - squareX;
                            } else if (pointerRotation == 2) {
                                zoneX = x + pointerSquareX + 8 - squareX;
                                zoneZ = pointerSquareZ + z + 8 - squareZ;
                            } else {
                                zoneZ = squareX + z - pointerSquareX;
                                zoneX = x + pointerSquareZ + 8 - squareZ;
                            }

                            this.decodeTile(packet, zoneX, zoneZ, squareX + baseX, squareZ + baseZ, 0, 0, level, 0, true);
                        } else {
                            zoneX = x + MapRegion.rotateZoneX(squareX & 0x7, squareZ & 0x7, pointerRotation);
                            zoneZ = MapRegion.rotateZoneZ(squareX & 0x7, squareZ & 0x7, pointerRotation) + z;
                            this.decodeTile(packet, zoneX, zoneZ, squareX + baseX, squareZ + baseZ, offsetX, offsetZ, level, pointerRotation, false);
                        }

                        if (squareX == 63 || squareZ == 63) {
                            @Pc(376) byte rotations = 1;
                            if (squareX == 63 && squareZ == 63) {
                                rotations = 3;
                            }

                            for (@Pc(390) int rotation = 0; rotation < rotations; rotation++) {
                                @Pc(393) int x2 = squareX;
                                @Pc(395) int z2 = squareZ;
                                if (rotation == 0) {
                                    z2 = squareZ == 63 ? 64 : squareZ;
                                    x2 = squareX == 63 ? 64 : squareX;
                                } else if (rotation == 1) {
                                    x2 = 64;
                                } else if (rotation == 2) {
                                    z2 = 64;
                                }

                                @Pc(450) int x1;
                                @Pc(442) int z1;
                                if (pointerRotation == 0) {
                                    z1 = z + z2 - pointerSquareZ;
                                    x1 = x2 + x - pointerSquareX;
                                } else if (pointerRotation == 1) {
                                    x1 = z2 + x - pointerSquareZ;
                                    z1 = z + pointerSquareX + 8 - x2;
                                } else if (pointerRotation == 2) {
                                    z1 = pointerSquareZ + z + 8 - z2;
                                    x1 = pointerSquareX + x + 8 - x2;
                                } else {
                                    z1 = z + x2 - pointerSquareX;
                                    x1 = x + pointerSquareZ + 8 - z2;
                                }

                                if (x1 >= 0 && x1 < this.width && z1 >= 0 && z1 < this.length) {
                                    this.tileHeights[level][x1][z1] = this.tileHeights[level][offsetX + zoneX][zoneZ + offsetZ];
                                }
                            }
                        }
                    } else {
                        this.decodeTile(packet, -1, -1, 0, 0, 0, 0, 0, 0, false);
                    }
                }
            }
        }
    }
}

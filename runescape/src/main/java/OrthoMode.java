import com.jagex.PickableEntity;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.graphics.FlipException;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.OffscreenSurface;
import com.jagex.graphics.PickingCylinder;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * Draws the world through an oversized surface, so that a camera which only pans can reuse the
 * pixels it drew on the previous frame.
 * <p>
 * The field {@link #mode} selects how that surface is held: mode 0 keeps a single buffer with a
 * {@link #borderX} by {@link #borderY} margin around the view, mode 1 keeps a wrapping grid of
 * {@link #tileWidth} by {@link #tileHeight} tiles, and mode 2 draws straight to the display. The
 * retained pixels are scrolled by {@link #scrollX} and {@link #scrollY} and only the strips that
 * came into view are drawn again. The opaque pass into that surface uses the {@code cachedViewport}
 * fields, the transparent pass over the top of it uses the {@code screenViewport} fields.
 */
public final class OrthoMode {

    @OriginalMember(owner = "client!afa", name = "p", descriptor = "Lclient!kn;")
    public static final PickList orthoPickList = new PickList(true);

    @OriginalMember(owner = "client!rw", name = "v", descriptor = "I")
    public static int surfaceWidth;

    @OriginalMember(owner = "client!sba", name = "d", descriptor = "I")
    public static int surfaceHeight;

    @OriginalMember(owner = "client!bba", name = "L", descriptor = "I")
    public static int tileSizeX = 100;

    @OriginalMember(owner = "client!jb", name = "E", descriptor = "I")
    public static int tileSizeY = 100;

    @OriginalMember(owner = "client!pt", name = "r", descriptor = "Z")
    public static boolean enabled = false;

    @OriginalMember(owner = "client!ef", name = "d", descriptor = "Z")
    public static boolean toolkitActive = false;

    @OriginalMember(owner = "client!oc", name = "j", descriptor = "I")
    public static int mode;

    @OriginalMember(owner = "client!cga", name = "b", descriptor = "Lclient!ha;")
    public static Toolkit toolkit;

    @OriginalMember(owner = "client!nea", name = "b", descriptor = "Lclient!tt;")
    public static Matrix cameraMatrix;

    @OriginalMember(owner = "client!gf", name = "j", descriptor = "Lclient!tt;")
    public static Matrix zCameraMatrix;

    @OriginalMember(owner = "client!wj", name = "Lc", descriptor = "Lclient!tt;")
    public static Matrix savedCamera;

    @OriginalMember(owner = "client!np", name = "v", descriptor = "I")
    public static int tileWidth;

    @OriginalMember(owner = "client!dl", name = "f", descriptor = "I")
    public static int extraTilesX;

    @OriginalMember(owner = "client!wl", name = "g", descriptor = "I")
    public static int tileHeight;

    @OriginalMember(owner = "client!oda", name = "r", descriptor = "Lclient!gaa;")
    public static OffscreenSurface surface;

    @OriginalMember(owner = "client!pda", name = "w", descriptor = "I")
    public static int tileOffsetY;

    @OriginalMember(owner = "client!u", name = "k", descriptor = "I")
    public static int tileOffsetX;

    @OriginalMember(owner = "client!ol", name = "F", descriptor = "I")
    public static int extraTilesY;

    @OriginalMember(owner = "client!fga", name = "t", descriptor = "[I")
    public static int[] tileStamps;

    @OriginalMember(owner = "client!bla", name = "C", descriptor = "I")
    public static int cameraRotateZ = -1;

    @OriginalMember(owner = "client!efa", name = "c", descriptor = "I")
    public static int cameraRotateY = -1;

    @OriginalMember(owner = "client!vu", name = "e", descriptor = "I")
    public static int cameraRotateX = -1;

    @OriginalMember(owner = "client!uja", name = "p", descriptor = "[Lclient!gaa;")
    public static OffscreenSurface[] tiles;

    @OriginalMember(owner = "client!kca", name = "V", descriptor = "I")
    public static int borderX;

    @OriginalMember(owner = "client!wda", name = "n", descriptor = "I")
    public static int borderY;

    @OriginalMember(owner = "client!gj", name = "f", descriptor = "I")
    public static int tilesX;

    @OriginalMember(owner = "client!tj", name = "F", descriptor = "I")
    public static int tilesY;

    @OriginalMember(owner = "client!hp", name = "db", descriptor = "I")
    public static int orthoWidth;

    @OriginalMember(owner = "client!iq", name = "c", descriptor = "I")
    public static int orthoHeight;

    @OriginalMember(owner = "client!aaa", name = "U", descriptor = "I")
    public static int drawX;

    @OriginalMember(owner = "client!jt", name = "f", descriptor = "I")
    public static int drawY;

    @OriginalMember(owner = "client!ld", name = "a", descriptor = "I")
    public static int cameraX;

    @OriginalMember(owner = "client!sw", name = "c", descriptor = "I")
    public static int scrollX;

    @OriginalMember(owner = "client!uf", name = "a", descriptor = "D")
    public static double scrollZ;

    @OriginalMember(owner = "client!ema", name = "k", descriptor = "I")
    public static int cameraY;

    @OriginalMember(owner = "client!sia", name = "h", descriptor = "I")
    public static int scrollY;

    @OriginalMember(owner = "client!af", name = "o", descriptor = "I")
    public static int cameraZ;

    @OriginalMember(owner = "client!hl", name = "h", descriptor = "D")
    public static double cachedCameraZ;

    @OriginalMember(owner = "client!nea", name = "a", descriptor = "I")
    public static int stamp = 1;

    @OriginalMember(owner = "client!jg", name = "d", descriptor = "D")
    public static double screenCameraZ;

    @OriginalMember(owner = "client!bja", name = "a", descriptor = "I")
    public static int screenViewportWidth;

    @OriginalMember(owner = "client!qia", name = "e", descriptor = "I")
    public static int screenViewportY;

    @OriginalMember(owner = "client!gl", name = "e", descriptor = "I")
    public static int screenViewportX;

    @OriginalMember(owner = "client!hl", name = "e", descriptor = "I")
    public static int screenViewportHeight;

    @OriginalMember(owner = "client!km", name = "d", descriptor = "I")
    public static int savedViewportX;

    @OriginalMember(owner = "client!uc", name = "r", descriptor = "I")
    public static int savedViewportY;

    @OriginalMember(owner = "client!ffa", name = "g", descriptor = "I")
    public static int savedViewportWidth;

    @OriginalMember(owner = "client!tv", name = "e", descriptor = "I")
    public static int savedViewportHeight;

    @OriginalMember(owner = "client!rfa", name = "u", descriptor = "I")
    public static int backgroundColour = 0;

    @OriginalMember(owner = "client!ld", name = "m", descriptor = "I")
    public static int cachedViewportX;

    @OriginalMember(owner = "client!mea", name = "d", descriptor = "I")
    public static int cachedViewportY;

    @OriginalMember(owner = "client!nla", name = "Q", descriptor = "I")
    public static int cachedViewportWidth;

    @OriginalMember(owner = "client!uga", name = "c", descriptor = "I")
    public static int cachedViewportHeight;

    @OriginalMember(owner = "client!sfa", name = "g", descriptor = "I")
    public static int zoom = 7000;

    @OriginalMember(owner = "client!sfa", name = "e", descriptor = "I")
    public static int renderZoom = zoom;

    @OriginalMember(owner = "client!sb", name = "a", descriptor = "(Ljava/awt/Canvas;Z)V")
    public static void method7606(@OriginalArg(0) Canvas canvas) {
        @Pc(6) Dimension dimension = canvas.getSize();
        method5454(dimension.height, dimension.width);

        if (mode == 1) {
            toolkit.resizeCanvas(canvas, surfaceWidth, surfaceHeight);
        } else {
            toolkit.resizeCanvas(canvas, orthoWidth, orthoHeight);
        }
    }

    @OriginalMember(owner = "client!du", name = "a", descriptor = "(Lclient!ha;IIIIIBI)V")
    public static void initTiles(@OriginalArg(0) Toolkit toolkit, @OriginalArg(2) int width, @OriginalArg(3) int tileWidth, @OriginalArg(4) int tileHeight, @OriginalArg(7) int height) {
        OrthoMode.toolkit = toolkit;
        cameraMatrix = OrthoMode.toolkit.createMatrix();
        zCameraMatrix = OrthoMode.toolkit.createMatrix();
        savedCamera = OrthoMode.toolkit.createMatrix();
        OrthoMode.tileWidth = tileWidth;
        extraTilesX = 2;
        mode = 1;
        tileOffsetY = 0;
        tileOffsetX = 0;
        extraTilesY = 2;
        OrthoMode.tileHeight = tileHeight;
        surface = null;
        method5454(height, width);
    }

    @OriginalMember(owner = "client!cm", name = "a", descriptor = "(IIIIILclient!ha;)V")
    public static void initBuffer(@OriginalArg(1) int height, @OriginalArg(3) int width, @OriginalArg(5) Toolkit toolkit) {
        OrthoMode.toolkit = toolkit;
        cameraMatrix = toolkit.createMatrix();
        zCameraMatrix = toolkit.createMatrix();
        savedCamera = toolkit.createMatrix();
        tileStamps = null;
        borderX = 100;
        borderY = 100;
        tiles = null;
        mode = 0;
        method5454(height, width);
        cameraRotateZ = -1;
        cameraRotateX = -1;
        cameraRotateY = -1;
    }

    @OriginalMember(owner = "client!ee", name = "g", descriptor = "(I)V")
    public static void reset() {
        zCameraMatrix = null;
        surface = null;
        cameraRotateZ = -1;
        tileStamps = null;
        cameraMatrix = null;
        cameraRotateY = -1;
        mode = -1;
        toolkit = null;
        cameraRotateX = -1;
        tiles = null;
        savedCamera = null;
        orthoPickList.clear();
    }

    @OriginalMember(owner = "client!pm", name = "b", descriptor = "(I)V")
    public static void enter() {
        reset();

        @Pc(19) int value = ClientOptions.instance.orthographic.getValue();
        if (value == 2) {
            initBuffer(GameShell.canvasHei, GameShell.canvasWid, Toolkit.active);
        } else if (value == 3) {
            initTiles(Toolkit.active, GameShell.canvasWid, tileSizeX, tileSizeY, GameShell.canvasHei);
        }

        if (ClientOptions.instance.orthographic.isToolkitCompatible()) {
            method7606(GameShell.canvas);
        }

        if (Toolkit.active != null) {
            Static209.method3110();
        }

        enabled = ClientOptions.instance.orthographic.getValue() != 0;
        toolkitActive = ClientOptions.instance.orthographic.isToolkitCompatible();
    }

    @OriginalMember(owner = "client!nja", name = "d", descriptor = "(B)V")
    public static void flip() throws FlipException {
        if (mode == 1) {
            toolkit.flip(drawX, drawY);
        } else {
            toolkit.flip(0, 0);
        }
    }

    @OriginalMember(owner = "client!eb", name = "a", descriptor = "(I)I")
    public static int method2283() {
        return mode == 1 ? drawX : 0;
    }

    @OriginalMember(owner = "client!mca", name = "a", descriptor = "(III)V")
    public static void method5454(@OriginalArg(0) int height, @OriginalArg(1) int width) {
        if (toolkit == null) {
            return;
        }

        @Pc(9) int oldTilesX = tilesX;
        @Pc(16) int oldTilesY = tilesY;
        updateSurfaceSize(height, width);
        if (mode == 0) {
            surface = null;
            surface = toolkit.createOffscreenSurface(toolkit.method7962(surfaceWidth, surfaceHeight), toolkit.method7986(surfaceWidth, surfaceHeight));
        } else if (mode == 1 && (tiles == null || oldTilesX != tilesX || oldTilesY != tilesY)) {
            tiles = new OffscreenSurface[tilesX * tilesY];
            for (@Pc(74) int i = 0; i < tiles.length; i++) {
                tiles[i] = toolkit.createOffscreenSurface(toolkit.method7962(tileWidth, tileHeight), toolkit.method7986(tileWidth, tileHeight));
            }
            stamp = 1;
            tileStamps = new int[tilesX * tilesY];
        }
        Static75.hasOpaqueStationaryEntities = true;
    }

    @OriginalMember(owner = "client!vka", name = "a", descriptor = "(IIIBI)V")
    public static void method8927(@OriginalArg(2) int x1, @OriginalArg(4) int x2, @OriginalArg(0) int y1, @OriginalArg(1) int y2) {
        if (mode != 1) {
            return;
        }
        @Pc(14) int tileX1 = x1 / tileWidth;
        @Pc(18) int tileX2 = x2 / tileWidth;
        @Pc(22) int tileY1 = y1 / tileHeight;
        @Pc(26) int tileY2 = y2 / tileHeight;
        if (tileX1 >= tilesX || tileX2 < 0 || tileY1 >= tilesY || tileY2 < 0) {
            return;
        }
        if (tileX1 < 0) {
            tileX1 = 0;
        }
        if (tileY1 < 0) {
            tileY1 = 0;
        }
        if (tileY2 >= tilesY) {
            tileY2 = tilesY - 1;
        }
        if (tileX2 >= tilesX) {
            tileX2 = tilesX - 1;
        }
        for (@Pc(94) int tileY = tileY1; tileY <= tileY2; tileY++) {
            @Pc(105) int rowOffset = Node_Sub1_Sub27.method9118(tileY + tileOffsetY, tilesY) * tilesX;
            for (@Pc(107) int tileX = tileX1; tileX <= tileX2; tileX++) {
                @Pc(117) int index = Node_Sub1_Sub27.method9118(tileOffsetX + tileX, tilesX) + rowOffset;
                tileStamps[index] = stamp;
            }
        }
    }

    @OriginalMember(owner = "client!vv", name = "e", descriptor = "(I)V")
    public static void drawDirtyTiles() {
        for (@Pc(10) int y = 0; y < tilesY; y++) {
            @Pc(23) int rowOffset = Node_Sub1_Sub27.method9118(tileOffsetY + y, tilesY) * tilesX;
            for (@Pc(25) int x = 0; x < tilesX; x++) {
                @Pc(36) int index = Node_Sub1_Sub27.method9118(tileOffsetX + x, tilesX) + rowOffset;
                if (tileStamps[index] == stamp) {
                    tiles[index].method9040(0, 0, tileWidth, tileHeight, tileWidth * x, tileHeight * y);
                }
            }
        }
        Static694.anInt10405++;
    }

    @OriginalMember(owner = "client!om", name = "a", descriptor = "(BIIII[I[III[IZZI[I[[[B[III)V")
    public static void method6324(@OriginalArg(0) byte roofStamp, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) int y, @OriginalArg(4) int playerTileZ, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int clock, @OriginalArg(8) int levels, @OriginalArg(9) int[] arg9, @OriginalArg(10) boolean flickerDisabled, @OriginalArg(12) int orthoZoom, @OriginalArg(13) int[] arg12, @OriginalArg(14) byte[][][] roofStamps, @OriginalArg(15) int[] arg14, @OriginalArg(16) int playerTileX) {
        if (mode == -1) {
            return;
        }
        @Pc(13) int[] viewport = toolkit.Y();
        @Pc(17) int projectionCenterX = viewport[0];
        @Pc(21) int projectionCenterY = viewport[1];
        @Pc(25) int projectionScaleX = viewport[2];
        @Pc(29) int projectionScaleY = viewport[3];

        @Pc(31) int scaleX = projectionScaleX;
        @Pc(33) int scaleY = projectionScaleY;
        if (mode == 1) {
            scaleY = (int) ((double) orthoHeight * (double) projectionScaleY / (double) surfaceHeight);
            scaleX = (int) ((double) orthoHeight * (double) projectionScaleX / (double) surfaceHeight);
        }

        if (!Static75.hasOpaqueStationaryEntities) {
            if (mode == 1) {
                drawDirtyTiles();
            }
            @Pc(76) int dx = x - cameraX;
            @Pc(81) int dy = y - cameraY;
            @Pc(86) int dz = z - cameraZ;
            @Pc(108) int screenDx = (int) ((Static364.aDouble17 * (double) dz + Static398.aDouble20 * (double) dy + Static683.aDouble24 * (double) dx) * (double) scaleX / (double) orthoZoom);
            @Pc(130) int screenDy = (int) (((double) dz * Static614.aDouble22 + (double) dy * Static118.aDouble11 + (double) dx * Static98.aDouble9) * (double) scaleY / (double) orthoZoom);
            @Pc(145) double depth = Static177.aDouble12 * (double) dx + (double) dy * Static309.aDouble16 + Static534.aDouble21 * (double) dz;
            @Pc(152) int newDrawX = screenDx + borderX - scrollX;
            @Pc(159) int newDrawY = screenDy + borderY - scrollY;
            @Pc(163) int rightEdge = newDrawX + orthoWidth;
            @Pc(167) int bottomEdge = orthoHeight + newDrawY;
            if (newDrawX >= 0 && newDrawY >= 0 && surfaceWidth >= rightEdge && bottomEdge <= surfaceHeight || mode == 2) {
                if (mode == 2) {
                    scrollZ = -depth;
                }
                drawY = newDrawY;
                drawX = newDrawX;
            } else if (rightEdge > 0 && bottomEdge > 0 && surfaceWidth > newDrawX && surfaceHeight > newDrawY) {
                @Pc(244) int rawShiftX = newDrawX - borderX;
                @Pc(248) int rawShiftY = newDrawY - borderY;
                @Pc(250) int shiftX = 0;
                @Pc(252) int shiftY = 0;
                @Pc(254) int tileShiftX = 0;
                @Pc(256) int tileShiftY = 0;
                @Pc(258) double shiftZ = 0.0D;
                if (mode == 0) {
                    shiftX = rawShiftX;
                    shiftY = rawShiftY;
                    shiftZ = depth + scrollZ;
                } else if (mode == 1) {
                    tileShiftY = rawShiftY / tileHeight;
                    tileShiftX = rawShiftX / tileWidth;
                    shiftY = tileShiftY * tileHeight;
                    shiftX = tileWidth * tileShiftX;
                    shiftZ = (double) (rawShiftX * shiftX + shiftY * rawShiftY) * (depth + scrollZ) / (double) (rawShiftY * rawShiftY + rawShiftX * rawShiftX);
                }
                shiftZ = -shiftZ;
                @Pc(319) int newRowStart = 0;
                @Pc(321) int newRowCount = 0;
                @Pc(323) int newColStart = 0;
                @Pc(325) int keptRowStart = 0;
                @Pc(327) int newColCount = 0;
                @Pc(329) int keptRowCount = 0;
                @Pc(340) int copyX;
                @Pc(338) int copyWidth;
                @Pc(344) int colStripX;
                @Pc(342) int colStripWidth;
                if (shiftX >= 0) {
                    copyWidth = surfaceWidth - shiftX;
                    copyX = 0;
                    colStripWidth = shiftX;
                    colStripX = copyWidth;
                    if (mode == 1) {
                        newColCount = tileShiftX;
                        newColStart = tilesX - tileShiftX;
                    }
                } else {
                    copyX = -shiftX;
                    copyWidth = shiftX + surfaceWidth;
                    colStripX = 0;
                    colStripWidth = copyX;
                    if (mode == 1) {
                        newColStart = 0;
                        newColCount = -tileShiftX;
                    }
                }
                @Pc(386) int copyY;
                @Pc(393) int copyHeight;
                @Pc(388) int rowStripY;
                @Pc(395) int rowStripHeight;
                @Pc(397) int colStripY;
                if (shiftY < 0) {
                    copyY = -shiftY;
                    rowStripY = 0;
                    copyHeight = surfaceHeight + shiftY;
                    rowStripHeight = copyY;
                    colStripY = copyY;
                    if (mode == 1) {
                        newRowStart = 0;
                        newRowCount = -tileShiftY;
                        keptRowStart = newRowCount;
                        keptRowCount = tilesY + tileShiftY;
                    }
                } else {
                    copyHeight = surfaceHeight - shiftY;
                    copyY = 0;
                    if (mode == 1) {
                        keptRowStart = 0;
                        newRowCount = tileShiftY;
                        newRowStart = tilesY - tileShiftY;
                        keptRowCount = newRowStart;
                    }
                    colStripY = 0;
                    rowStripHeight = shiftY;
                    rowStripY = copyHeight;
                }
                @Pc(451) LinkedList entities = orthoPickList.entities;
                @Pc(465) int i;
                for (@Pc(456) PickableEntity entity = (PickableEntity) entities.first(); entity != null; entity = (PickableEntity) entities.next()) {
                    @Pc(461) PickingCylinder[] cylinders = entity.pickingCylinders;
                    @Pc(463) boolean cull = true;
                    for (i = 0; i < cylinders.length; i++) {
                        @Pc(471) PickingCylinder cylinder = cylinders[i];
                        @Pc(474) int x1 = cylinder.anInt4504;
                        @Pc(477) int y1 = cylinder.anInt4505;
                        @Pc(480) int x2 = cylinder.anInt4501;
                        @Pc(483) int y2 = cylinder.anInt4503;
                        @Pc(486) int radius = cylinder.anInt4502;
                        @Pc(493) int newY1;
                        cylinder.anInt4505 = newY1 = y1 - shiftY;
                        @Pc(501) int newY2;
                        cylinder.anInt4503 = newY2 = y2 - shiftY;
                        @Pc(509) int newX1;
                        cylinder.anInt4504 = newX1 = x1 - shiftX;
                        @Pc(517) int newX2;
                        cylinder.anInt4501 = newX2 = x2 - shiftX;
                        if (cull) {
                            @Pc(537) int left = (newX1 >= newX2 ? newX2 : newX1) - radius;
                            if (surfaceWidth >= left) {
                                @Pc(557) int top = (newY1 < newY2 ? newY1 : newY2) - radius;
                                if (top <= surfaceHeight) {
                                    @Pc(573) int right = (newX1 < newX2 ? newX2 : newX1) + radius;
                                    if (right >= 0) {
                                        @Pc(592) int bottom = (newY1 >= newY2 ? newY1 : newY2) + radius;
                                        if (bottom >= 0) {
                                            cull = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (cull) {
                        entity.unlink();
                        Static281.recycle(entity);
                    }
                }
                if (mode == 0) {
                    toolkit.swapSurface(surface);
                }
                toolkit.F(-shiftX, -shiftY);
                toolkit.b(copyX, copyY, copyWidth, copyHeight, shiftZ);
                translateCameraZ(scrollZ + shiftZ);
                cachedCameraZ = shiftZ + scrollZ;
                if (mode == 1) {
                    cachedViewportY = projectionCenterY - scrollY - shiftY;
                    cachedViewportWidth = scaleX;
                    cachedViewportHeight = scaleY;
                    cachedViewportX = projectionCenterX - shiftX - scrollX;
                    toolkit.DA(cachedViewportX, cachedViewportY, cachedViewportWidth, cachedViewportHeight);
                } else {
                    cachedViewportHeight = scaleY;
                    cachedViewportY = projectionCenterY + borderY - shiftY - scrollY;
                    cachedViewportX = borderX + projectionCenterX - shiftX - scrollX;
                    cachedViewportWidth = scaleX;
                    toolkit.DA(cachedViewportX, cachedViewportY, cachedViewportWidth, cachedViewportHeight);
                }
                Static119.setActivePickList(orthoPickList);
                if (rowStripHeight > 0) {
                    toolkit.KA(0, rowStripY, surfaceWidth, rowStripY + rowStripHeight);
                    toolkit.ya();
                    toolkit.GA(backgroundColour);
                    SceneRenderer.renderScene(clock, x, y, z, roofStamps, arg9, arg12, arg5, arg14, arg6, levels, roofStamp, playerTileX, playerTileZ, flickerDisabled, orthoZoom, 1, false);
                }
                if (colStripWidth > 0) {
                    toolkit.KA(colStripX, colStripY, colStripWidth + colStripX, copyHeight + colStripY);
                    toolkit.ya();
                    toolkit.GA(backgroundColour);
                    SceneRenderer.renderScene(clock, x, y, z, roofStamps, arg9, arg12, arg5, arg14, arg6, levels, roofStamp, playerTileX, playerTileZ, flickerDisabled, orthoZoom, 1, false);
                }
                toolkit.la();
                Static102.useScenePickList();
                if (mode == 0) {
                    toolkit.restoreSurface();
                }
                scrollY += shiftY;
                scrollX += shiftX;
                scrollZ += shiftZ;
                drawY = borderY + screenDy - scrollY;
                drawX = screenDx + borderX - scrollX;
                if (mode == 1) {
                    tileOffsetX += tileShiftX;
                    tileOffsetY += tileShiftY;
                    for (@Pc(855) int tileY = 0; tileY < tilesY; tileY++) {
                        @Pc(868) int rowOffset = Node_Sub1_Sub27.method9118(tileOffsetY + tileY, tilesY) * tilesX;
                        for (i = 0; i < tilesX; i++) {
                            @Pc(881) int index = Node_Sub1_Sub27.method9118(i + tileOffsetX, tilesX) + rowOffset;
                            @Pc(936) boolean exposed = tileY >= newRowStart && tileY < newRowStart + newRowCount || keptRowStart <= tileY && tileY < keptRowCount + keptRowStart && i >= newColStart && i < newColCount + newColStart;
                            tiles[index].method9039(i * tileWidth, tileHeight * tileY, tileWidth, tileHeight, exposed);
                        }
                    }
                }
            } else {
                Static75.hasOpaqueStationaryEntities = true;
            }
        }

        if (Static75.hasOpaqueStationaryEntities) {
            cameraX = x;
            scrollX = 0;
            cameraZ = z;
            scrollZ = 0.0D;
            drawX = borderX;
            cameraY = y;
            scrollY = 0;
            drawY = borderY;
            if (mode == 0) {
                toolkit.swapSurface(surface);
            }
            toolkit.la();
            toolkit.ya();
            toolkit.GA(backgroundColour);
            cameraMatrix.createCamera(cameraX, cameraY, cameraZ, cameraRotateX, cameraRotateY, cameraRotateZ);
            toolkit.setCamera(cameraMatrix);
            if (mode == 1) {
                cachedViewportX = projectionCenterX;
                cachedViewportY = projectionCenterY;
                cachedViewportWidth = scaleX;
                cachedViewportHeight = scaleY;
                toolkit.DA(cachedViewportX, cachedViewportY, cachedViewportWidth, cachedViewportHeight);
            } else {
                cachedViewportX = borderX + projectionCenterX;
                cachedViewportWidth = scaleX;
                cachedViewportHeight = scaleY;
                cachedViewportY = projectionCenterY + borderY;
                toolkit.DA(cachedViewportX, cachedViewportY, cachedViewportWidth, cachedViewportHeight);
            }
            cachedCameraZ = 0.0D;
            orthoPickList.clear();
            Static119.setActivePickList(orthoPickList);
            SceneRenderer.renderScene(clock, x, y, z, roofStamps, arg9, arg12, arg5, arg14, arg6, levels, roofStamp, playerTileX, playerTileZ, flickerDisabled, orthoZoom, 1, false);
            Static102.useScenePickList();
            Static75.hasOpaqueStationaryEntities = false;
            if (mode == 0) {
                toolkit.restoreSurface();
            }
            if (mode == 1) {
                drawAllTiles();
            }
        }
        if (mode == 0) {
            surface.method9040(drawX, drawY, orthoWidth, orthoHeight, 0, 0);
        }
        stamp++;
        translateCameraZ(scrollZ);
        screenCameraZ = scrollZ;
        if (mode == 0 || mode == 2) {
            if (mode == 2) {
                toolkit.GA(backgroundColour);
                toolkit.ya();
            }
            screenViewportWidth = scaleX;
            screenViewportY = projectionCenterY + borderY - drawY - scrollY;
            screenViewportX = projectionCenterX + borderX - drawX - scrollX;
            screenViewportHeight = scaleY;
            toolkit.DA(screenViewportX, screenViewportY, screenViewportWidth, screenViewportHeight);
        } else if (mode == 1) {
            screenViewportWidth = scaleX;
            screenViewportX = projectionCenterX - scrollX;
            screenViewportY = projectionCenterY - scrollY;
            screenViewportHeight = scaleY;
            toolkit.DA(screenViewportX, screenViewportY, screenViewportWidth, screenViewportHeight);
            toolkit.KA(drawX, drawY, drawX + orthoWidth, orthoHeight + drawY);
        }
        SceneRenderer.renderScene(clock, x, y, z, roofStamps, arg9, arg12, arg5, arg14, arg6, levels, roofStamp, playerTileX, playerTileZ, flickerDisabled, orthoZoom, mode == 2 ? 0 : 2, mode == 1);
        toolkit.la();
        toolkit.DA(projectionCenterX, projectionCenterY, projectionScaleX, projectionScaleY);
    }

    @OriginalMember(owner = "client!wca", name = "a", descriptor = "(II[Ljava/awt/Rectangle;)V")
    public static void flipDirtyRect(@OriginalArg(1) int count, @OriginalArg(2) Rectangle[] rectangles) throws FlipException {
        if (mode == 1) {
            toolkit.flipDirtyRect(rectangles, count, drawX, drawY);
        } else {
            toolkit.flipDirtyRect(rectangles, count, 0, 0);
        }
    }

    @OriginalMember(owner = "client!fl", name = "a", descriptor = "(B)V")
    public static void drawAllTiles() {
        tileOffsetX = 0;
        tileOffsetY = 0;
        for (@Pc(27) int y = 0; y < tilesY; y++) {
            @Pc(33) int rowOffset = tilesX * y;
            for (@Pc(35) int x = 0; x < tilesX; x++) {
                @Pc(41) int index = x + rowOffset;
                tiles[index].method9039(x * tileWidth, tileHeight * y, tileWidth, tileHeight, true);
            }
        }
    }

    @OriginalMember(owner = "client!fo", name = "a", descriptor = "(III)V")
    public static void updateSurfaceSize(@OriginalArg(0) int height, @OriginalArg(1) int width) {
        orthoHeight = height;
        orthoWidth = width;

        if (mode == 0) {
            surfaceWidth = orthoWidth + borderX * 2;
            surfaceHeight = orthoHeight + borderY * 2;
        } else if (mode == 1) {
            tilesX = (orthoWidth / tileWidth) + extraTilesX + 2;
            tilesY = (orthoHeight / tileHeight) + extraTilesY + 2;
            surfaceHeight = tilesY * tileHeight;
            surfaceWidth = tilesX * tileWidth;
            borderX = surfaceWidth - orthoWidth >> 1;
            borderY = surfaceHeight - orthoHeight >> 1;
        } else if (mode == 2) {
            surfaceWidth = orthoWidth;
            surfaceHeight = orthoHeight;
        }
    }

    @OriginalMember(owner = "client!hj", name = "a", descriptor = "(IZ)I")
    public static int method3503(@OriginalArg(1) boolean screen) {
        @Pc(5) int mode = OrthoMode.mode;
        if (mode == 0) {
            return screen ? 0 : drawX;
        } else if (mode == 1) {
            return drawX;
        } else if (mode == 2) {
            return 0;
        } else {
            return 0;
        }
    }

    @OriginalMember(owner = "client!in", name = "a", descriptor = "(ZD)V")
    public static void translateCameraZ(@OriginalArg(1) double z) {
        zCameraMatrix.apply(cameraMatrix);
        zCameraMatrix.translate(0, 0, (int) z);
        toolkit.setCamera(zCameraMatrix);
    }

    @OriginalMember(owner = "client!wk", name = "a", descriptor = "(IZ)V")
    public static void method9331(@OriginalArg(1) boolean screen) {
        savedCamera.apply(toolkit.camera());
        @Pc(10) int[] viewport = toolkit.Y();
        savedViewportX = viewport[0];
        savedViewportY = viewport[1];
        savedViewportWidth = viewport[2];
        savedViewportHeight = viewport[3];
        if (screen) {
            toolkit.DA(screenViewportX, screenViewportY, screenViewportWidth, screenViewportHeight);
            translateCameraZ(screenCameraZ);
        } else {
            toolkit.DA(cachedViewportX, cachedViewportY, cachedViewportWidth, cachedViewportHeight);
            translateCameraZ(cachedCameraZ);
        }
    }

    @OriginalMember(owner = "client!sm", name = "i", descriptor = "(I)I")
    public static int method7779() {
        return mode == 1 ? surfaceWidth : orthoWidth;
    }

    @OriginalMember(owner = "client!sea", name = "a", descriptor = "(IZ)I")
    public static int method7649(@OriginalArg(1) boolean screen) {
        @Pc(13) int mode = OrthoMode.mode;
        if (mode == 0) {
            return screen ? 0 : drawY;
        } else if (mode == 1) {
            return drawY;
        } else if (mode == 2) {
            return 0;
        } else {
            return 0;
        }
    }

    @OriginalMember(owner = "client!paa", name = "a", descriptor = "(II)V")
    public static void method6448(@OriginalArg(0) int colour) {
        backgroundColour = colour;
    }

    @OriginalMember(owner = "client!bu", name = "d", descriptor = "(B)I")
    public static int method1260() {
        return mode == 1 ? surfaceHeight : orthoHeight;
    }
}

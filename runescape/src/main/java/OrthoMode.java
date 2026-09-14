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

public final class OrthoMode {

    @OriginalMember(owner = "client!afa", name = "p", descriptor = "Lclient!kn;")
    public static final PickList orthoPickList = new PickList(true);

    @OriginalMember(owner = "client!rw", name = "v", descriptor = "I")
    public static int anInt8534;

    @OriginalMember(owner = "client!sba", name = "d", descriptor = "I")
    public static int anInt8585;

    @OriginalMember(owner = "client!bba", name = "L", descriptor = "I")
    public static int tileSizeX = 100;

    @OriginalMember(owner = "client!jb", name = "E", descriptor = "I")
    public static int tileSizeY = 100;

    @OriginalMember(owner = "client!pt", name = "r", descriptor = "Z")
    public static boolean enabled = false;

    @OriginalMember(owner = "client!ef", name = "d", descriptor = "Z")
    public static boolean toolkitActive = false;

    @OriginalMember(owner = "client!oc", name = "j", descriptor = "I")
    public static int anInt6796;

    @OriginalMember(owner = "client!cga", name = "b", descriptor = "Lclient!ha;")
    public static Toolkit toolkit;

    @OriginalMember(owner = "client!nea", name = "b", descriptor = "Lclient!tt;")
    public static Matrix cameraMatrix;

    @OriginalMember(owner = "client!gf", name = "j", descriptor = "Lclient!tt;")
    public static Matrix zCameraMatrix;

    @OriginalMember(owner = "client!wj", name = "Lc", descriptor = "Lclient!tt;")
    public static Matrix aMatrix_11;

    @OriginalMember(owner = "client!np", name = "v", descriptor = "I")
    public static int horizontalAspectRatio;

    @OriginalMember(owner = "client!dl", name = "f", descriptor = "I")
    public static int anInt2268;

    @OriginalMember(owner = "client!wl", name = "g", descriptor = "I")
    public static int verticalAspectRatio;

    @OriginalMember(owner = "client!oda", name = "r", descriptor = "Lclient!gaa;")
    public static OffscreenSurface anOffscreenSurface_1;

    @OriginalMember(owner = "client!pda", name = "w", descriptor = "I")
    public static int anInt7265;

    @OriginalMember(owner = "client!u", name = "k", descriptor = "I")
    public static int anInt9503;

    @OriginalMember(owner = "client!ol", name = "F", descriptor = "I")
    public static int anInt7013;

    @OriginalMember(owner = "client!fga", name = "t", descriptor = "[I")
    public static int[] anIntArray252;

    @OriginalMember(owner = "client!bla", name = "C", descriptor = "I")
    public static int cameraRotateZ = -1;

    @OriginalMember(owner = "client!efa", name = "c", descriptor = "I")
    public static int cameraRotateY = -1;

    @OriginalMember(owner = "client!vu", name = "e", descriptor = "I")
    public static int cameraRotateX = -1;

    @OriginalMember(owner = "client!uja", name = "p", descriptor = "[Lclient!gaa;")
    public static OffscreenSurface[] anOffscreenSurfaceArray1;

    @OriginalMember(owner = "client!kca", name = "V", descriptor = "I")
    public static int anInt5113;

    @OriginalMember(owner = "client!wda", name = "n", descriptor = "I")
    public static int anInt10569;

    @OriginalMember(owner = "client!gj", name = "f", descriptor = "I")
    public static int maxX;

    @OriginalMember(owner = "client!tj", name = "F", descriptor = "I")
    public static int maxY;

    @OriginalMember(owner = "client!hp", name = "db", descriptor = "I")
    public static int orthoWidth;

    @OriginalMember(owner = "client!iq", name = "c", descriptor = "I")
    public static int orthoHeight;

    @OriginalMember(owner = "client!aaa", name = "U", descriptor = "I")
    public static int anInt45;

    @OriginalMember(owner = "client!jt", name = "f", descriptor = "I")
    public static int anInt5001;

    @OriginalMember(owner = "client!ld", name = "a", descriptor = "I")
    public static int cameraX;

    @OriginalMember(owner = "client!sw", name = "c", descriptor = "I")
    public static int anInt8887;

    @OriginalMember(owner = "client!uf", name = "a", descriptor = "D")
    public static double aDouble23;

    @OriginalMember(owner = "client!ema", name = "k", descriptor = "I")
    public static int cameraY;

    @OriginalMember(owner = "client!sia", name = "h", descriptor = "I")
    public static int anInt8688;

    @OriginalMember(owner = "client!af", name = "o", descriptor = "I")
    public static int camaraZ;

    @OriginalMember(owner = "client!hl", name = "h", descriptor = "D")
    public static double aDouble13;

    @OriginalMember(owner = "client!nea", name = "a", descriptor = "I")
    public static int anInt6436 = 1;

    @OriginalMember(owner = "client!jg", name = "d", descriptor = "D")
    public static double aDouble15;

    @OriginalMember(owner = "client!bja", name = "a", descriptor = "I")
    public static int anInt1001;

    @OriginalMember(owner = "client!qia", name = "e", descriptor = "I")
    public static int anInt8043;

    @OriginalMember(owner = "client!gl", name = "e", descriptor = "I")
    public static int anInt3469;

    @OriginalMember(owner = "client!hl", name = "e", descriptor = "I")
    public static int anInt3986;

    @OriginalMember(owner = "client!km", name = "d", descriptor = "I")
    public static int anInt5563;

    @OriginalMember(owner = "client!uc", name = "r", descriptor = "I")
    public static int anInt9536;

    @OriginalMember(owner = "client!ffa", name = "g", descriptor = "I")
    public static int anInt2888;

    @OriginalMember(owner = "client!tv", name = "e", descriptor = "I")
    public static int anInt9494;

    @OriginalMember(owner = "client!rfa", name = "u", descriptor = "I")
    public static int backgroundColour = 0;

    @OriginalMember(owner = "client!ld", name = "m", descriptor = "I")
    public static int anInt5759;

    @OriginalMember(owner = "client!mea", name = "d", descriptor = "I")
    public static int anInt6134;

    @OriginalMember(owner = "client!nla", name = "Q", descriptor = "I")
    public static int anInt6565;

    @OriginalMember(owner = "client!uga", name = "c", descriptor = "I")
    public static int anInt9621;

    @OriginalMember(owner = "client!sfa", name = "g", descriptor = "I")
    public static int zoom = 7000;

    @OriginalMember(owner = "client!sfa", name = "e", descriptor = "I")
    public static int renderZoom = zoom;

    @OriginalMember(owner = "client!sb", name = "a", descriptor = "(Ljava/awt/Canvas;Z)V")
    public static void method7606(@OriginalArg(0) Canvas canvas) {
        @Pc(6) Dimension dimension = canvas.getSize();
        method5454(dimension.height, dimension.width);

        if (anInt6796 == 1) {
            toolkit.resizeCanvas(canvas, anInt8534, anInt8585);
        } else {
            toolkit.resizeCanvas(canvas, orthoWidth, orthoHeight);
        }
    }

    @OriginalMember(owner = "client!du", name = "a", descriptor = "(Lclient!ha;IIIIIBI)V")
    public static void method2227(@OriginalArg(0) Toolkit toolkit, @OriginalArg(2) int width, @OriginalArg(3) int horizontalAspectRatio, @OriginalArg(4) int verticalAspectRatio, @OriginalArg(7) int height) {
        OrthoMode.toolkit = toolkit;
        cameraMatrix = OrthoMode.toolkit.createMatrix();
        zCameraMatrix = OrthoMode.toolkit.createMatrix();
        aMatrix_11 = OrthoMode.toolkit.createMatrix();
        OrthoMode.horizontalAspectRatio = horizontalAspectRatio;
        anInt2268 = 2;
        anInt6796 = 1;
        anInt7265 = 0;
        anInt9503 = 0;
        anInt7013 = 2;
        OrthoMode.verticalAspectRatio = verticalAspectRatio;
        anOffscreenSurface_1 = null;
        method5454(height, width);
    }

    @OriginalMember(owner = "client!cm", name = "a", descriptor = "(IIIIILclient!ha;)V")
    public static void method9264(@OriginalArg(1) int arg0, @OriginalArg(3) int arg1, @OriginalArg(5) Toolkit arg2) {
        toolkit = arg2;
        cameraMatrix = toolkit.createMatrix();
        zCameraMatrix = toolkit.createMatrix();
        aMatrix_11 = toolkit.createMatrix();
        anIntArray252 = null;
        anInt5113 = 100;
        anInt10569 = 100;
        anOffscreenSurfaceArray1 = null;
        anInt6796 = 0;
        method5454(arg0, arg1);
        cameraRotateZ = -1;
        cameraRotateX = -1;
        cameraRotateY = -1;
    }

    @OriginalMember(owner = "client!ee", name = "g", descriptor = "(I)V")
    public static void reset() {
        zCameraMatrix = null;
        anOffscreenSurface_1 = null;
        cameraRotateZ = -1;
        anIntArray252 = null;
        cameraMatrix = null;
        cameraRotateY = -1;
        anInt6796 = -1;
        toolkit = null;
        cameraRotateX = -1;
        anOffscreenSurfaceArray1 = null;
        aMatrix_11 = null;
        orthoPickList.method5010();
    }

    @OriginalMember(owner = "client!pm", name = "b", descriptor = "(I)V")
    public static void enter() {
        reset();

        @Pc(19) int value = ClientOptions.instance.orthographic.getValue();
        if (value == 2) {
            method9264(GameShell.canvasHei, GameShell.canvasWid, Toolkit.active);
        } else if (value == 3) {
            method2227(Toolkit.active, GameShell.canvasWid, tileSizeX, tileSizeY, GameShell.canvasHei);
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
        if (anInt6796 == 1) {
            toolkit.flip(anInt45, anInt5001);
        } else {
            toolkit.flip(0, 0);
        }
    }

    @OriginalMember(owner = "client!eb", name = "a", descriptor = "(I)I")
    public static int method2283() {
        return anInt6796 == 1 ? anInt45 : 0;
    }

    @OriginalMember(owner = "client!mca", name = "a", descriptor = "(III)V")
    public static void method5454(@OriginalArg(0) int height, @OriginalArg(1) int width) {
        if (toolkit == null) {
            return;
        }

        @Pc(9) int local9 = maxX;
        @Pc(16) int local16 = maxY;
        method2821(height, width);
        if (anInt6796 == 0) {
            anOffscreenSurface_1 = null;
            anOffscreenSurface_1 = toolkit.createOffscreenSurface(toolkit.method7962(anInt8534, anInt8585), toolkit.method7986(anInt8534, anInt8585));
        } else if (anInt6796 == 1 && (anOffscreenSurfaceArray1 == null || local9 != maxX || local16 != maxY)) {
            anOffscreenSurfaceArray1 = new OffscreenSurface[maxX * maxY];
            for (@Pc(74) int local74 = 0; local74 < anOffscreenSurfaceArray1.length; local74++) {
                anOffscreenSurfaceArray1[local74] = toolkit.createOffscreenSurface(toolkit.method7962(horizontalAspectRatio, verticalAspectRatio), toolkit.method7986(horizontalAspectRatio, verticalAspectRatio));
            }
            anInt6436 = 1;
            anIntArray252 = new int[maxX * maxY];
        }
        Static75.hasOpaqueStationaryEntities = true;
    }

    @OriginalMember(owner = "client!vka", name = "a", descriptor = "(IIIBI)V")
    public static void method8927(@OriginalArg(2) int x1, @OriginalArg(4) int x2, @OriginalArg(0) int y1, @OriginalArg(1) int y2) {
        if (anInt6796 != 1) {
            return;
        }
        @Pc(14) int local14 = x1 / horizontalAspectRatio;
        @Pc(18) int local18 = x2 / horizontalAspectRatio;
        @Pc(22) int local22 = y1 / verticalAspectRatio;
        @Pc(26) int local26 = y2 / verticalAspectRatio;
        if (local14 >= maxX || local18 < 0 || local22 >= maxY || local26 < 0) {
            return;
        }
        if (local14 < 0) {
            local14 = 0;
        }
        if (local22 < 0) {
            local22 = 0;
        }
        if (local26 >= maxY) {
            local26 = maxY - 1;
        }
        if (local18 >= maxX) {
            local18 = maxX - 1;
        }
        for (@Pc(94) int local94 = local22; local94 <= local26; local94++) {
            @Pc(105) int local105 = Node_Sub1_Sub27.method9118(local94 + anInt7265, maxY) * maxX;
            for (@Pc(107) int local107 = local14; local107 <= local18; local107++) {
                @Pc(117) int local117 = Node_Sub1_Sub27.method9118(anInt9503 + local107, maxX) + local105;
                anIntArray252[local117] = anInt6436;
            }
        }
    }

    @OriginalMember(owner = "client!vv", name = "e", descriptor = "(I)V")
    public static void method9031() {
        for (@Pc(10) int local10 = 0; local10 < maxY; local10++) {
            @Pc(23) int local23 = Node_Sub1_Sub27.method9118(anInt7265 + local10, maxY) * maxX;
            for (@Pc(25) int local25 = 0; local25 < maxX; local25++) {
                @Pc(36) int local36 = Node_Sub1_Sub27.method9118(anInt9503 + local25, maxX) + local23;
                if (anIntArray252[local36] == anInt6436) {
                    anOffscreenSurfaceArray1[local36].method9040(0, 0, horizontalAspectRatio, verticalAspectRatio, horizontalAspectRatio * local25, verticalAspectRatio * local10);
                }
            }
        }
        Static694.anInt10405++;
    }

    @OriginalMember(owner = "client!om", name = "a", descriptor = "(BIIII[I[III[IZZI[I[[[B[III)V")
    public static void method6324(@OriginalArg(0) byte arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) int[] arg9, @OriginalArg(10) boolean arg10, @OriginalArg(12) int arg11, @OriginalArg(13) int[] arg12, @OriginalArg(14) byte[][][] arg13, @OriginalArg(15) int[] arg14, @OriginalArg(16) int arg15) {
        if (anInt6796 == -1) {
            return;
        }
        @Pc(13) int[] local13 = toolkit.Y();
        @Pc(17) int projectionCenterX = local13[0];
        @Pc(21) int projectionCenterY = local13[1];
        @Pc(25) int projectionScaleX = local13[2];
        @Pc(29) int projectionScaleY = local13[3];

        @Pc(31) int scaleX = projectionScaleX;
        @Pc(33) int scaleY = projectionScaleY;
        if (anInt6796 == 1) {
            scaleY = (int) ((double) orthoHeight * (double) projectionScaleY / (double) anInt8585);
            scaleX = (int) ((double) orthoHeight * (double) projectionScaleX / (double) anInt8585);
        }

        if (!Static75.hasOpaqueStationaryEntities) {
            if (anInt6796 == 1) {
                method9031();
            }
            @Pc(76) int local76 = arg1 - cameraX;
            @Pc(81) int local81 = arg3 - cameraY;
            @Pc(86) int local86 = arg2 - camaraZ;
            @Pc(108) int local108 = (int) ((Static364.aDouble17 * (double) local86 + Static398.aDouble20 * (double) local81 + Static683.aDouble24 * (double) local76) * (double) scaleX / (double) arg11);
            @Pc(130) int local130 = (int) (((double) local86 * Static614.aDouble22 + (double) local81 * Static118.aDouble11 + (double) local76 * Static98.aDouble9) * (double) scaleY / (double) arg11);
            @Pc(145) double local145 = Static177.aDouble12 * (double) local76 + (double) local81 * Static309.aDouble16 + Static534.aDouble21 * (double) local86;
            @Pc(152) int local152 = local108 + anInt5113 - anInt8887;
            @Pc(159) int local159 = local130 + anInt10569 - anInt8688;
            @Pc(163) int local163 = local152 + orthoWidth;
            @Pc(167) int local167 = orthoHeight + local159;
            if (local152 >= 0 && local159 >= 0 && anInt8534 >= local163 && local167 <= anInt8585 || anInt6796 == 2) {
                if (anInt6796 == 2) {
                    aDouble23 = -local145;
                }
                anInt5001 = local159;
                anInt45 = local152;
            } else if (local163 > 0 && local167 > 0 && anInt8534 > local152 && anInt8585 > local159) {
                @Pc(244) int local244 = local152 - anInt5113;
                @Pc(248) int local248 = local159 - anInt10569;
                @Pc(250) int local250 = 0;
                @Pc(252) int local252 = 0;
                @Pc(254) int local254 = 0;
                @Pc(256) int local256 = 0;
                @Pc(258) double local258 = 0.0D;
                if (anInt6796 == 0) {
                    local250 = local244;
                    local252 = local248;
                    local258 = local145 + aDouble23;
                } else if (anInt6796 == 1) {
                    local256 = local248 / verticalAspectRatio;
                    local254 = local244 / horizontalAspectRatio;
                    local252 = local256 * verticalAspectRatio;
                    local250 = horizontalAspectRatio * local254;
                    local258 = (double) (local244 * local250 + local252 * local248) * (local145 + aDouble23) / (double) (local248 * local248 + local244 * local244);
                }
                local258 = -local258;
                @Pc(319) int local319 = 0;
                @Pc(321) int local321 = 0;
                @Pc(323) int local323 = 0;
                @Pc(325) int local325 = 0;
                @Pc(327) int local327 = 0;
                @Pc(329) int local329 = 0;
                @Pc(340) int local340;
                @Pc(338) int local338;
                @Pc(344) int local344;
                @Pc(342) int local342;
                if (local250 >= 0) {
                    local338 = anInt8534 - local250;
                    local340 = 0;
                    local342 = local250;
                    local344 = local338;
                    if (anInt6796 == 1) {
                        local327 = local254;
                        local323 = maxX - local254;
                    }
                } else {
                    local340 = -local250;
                    local338 = local250 + anInt8534;
                    local344 = 0;
                    local342 = local340;
                    if (anInt6796 == 1) {
                        local323 = 0;
                        local327 = -local254;
                    }
                }
                @Pc(386) int local386;
                @Pc(393) int local393;
                @Pc(388) int local388;
                @Pc(395) int local395;
                @Pc(397) int local397;
                if (local252 < 0) {
                    local386 = -local252;
                    local388 = 0;
                    local393 = anInt8585 + local252;
                    local395 = local386;
                    local397 = local386;
                    if (anInt6796 == 1) {
                        local319 = 0;
                        local321 = -local256;
                        local325 = local321;
                        local329 = maxY + local256;
                    }
                } else {
                    local393 = anInt8585 - local252;
                    local386 = 0;
                    if (anInt6796 == 1) {
                        local325 = 0;
                        local321 = local256;
                        local319 = maxY - local256;
                        local329 = local319;
                    }
                    local397 = 0;
                    local395 = local252;
                    local388 = local393;
                }
                @Pc(451) LinkedList local451 = orthoPickList.entities;
                @Pc(465) int local465;
                for (@Pc(456) PickableEntity local456 = (PickableEntity) local451.first(); local456 != null; local456 = (PickableEntity) local451.next()) {
                    @Pc(461) PickingCylinder[] local461 = local456.pickingCylinders;
                    @Pc(463) boolean local463 = true;
                    for (local465 = 0; local465 < local461.length; local465++) {
                        @Pc(471) PickingCylinder local471 = local461[local465];
                        @Pc(474) int local474 = local471.anInt4504;
                        @Pc(477) int local477 = local471.anInt4505;
                        @Pc(480) int local480 = local471.anInt4501;
                        @Pc(483) int local483 = local471.anInt4503;
                        @Pc(486) int local486 = local471.anInt4502;
                        @Pc(493) int local493;
                        local471.anInt4505 = local493 = local477 - local252;
                        @Pc(501) int local501;
                        local471.anInt4503 = local501 = local483 - local252;
                        @Pc(509) int local509;
                        local471.anInt4504 = local509 = local474 - local250;
                        @Pc(517) int local517;
                        local471.anInt4501 = local517 = local480 - local250;
                        if (local463) {
                            @Pc(537) int local537 = (local509 >= local517 ? local517 : local509) - local486;
                            if (anInt8534 >= local537) {
                                @Pc(557) int local557 = (local493 < local501 ? local493 : local501) - local486;
                                if (local557 <= anInt8585) {
                                    @Pc(573) int local573 = (local509 < local517 ? local517 : local509) + local486;
                                    if (local573 >= 0) {
                                        @Pc(592) int local592 = (local493 >= local501 ? local493 : local501) + local486;
                                        if (local592 >= 0) {
                                            local463 = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (local463) {
                        local456.unlink();
                        Static281.recycle(local456);
                    }
                }
                if (anInt6796 == 0) {
                    toolkit.swapSurface(anOffscreenSurface_1);
                }
                toolkit.F(-local250, -local252);
                toolkit.b(local340, local386, local338, local393, local258);
                translateCameraZ(aDouble23 + local258);
                aDouble13 = local258 + aDouble23;
                if (anInt6796 == 1) {
                    anInt6134 = projectionCenterY - anInt8688 - local252;
                    anInt6565 = scaleX;
                    anInt9621 = scaleY;
                    anInt5759 = projectionCenterX - local250 - anInt8887;
                    toolkit.DA(anInt5759, anInt6134, anInt6565, anInt9621);
                } else {
                    anInt9621 = scaleY;
                    anInt6134 = projectionCenterY + anInt10569 - local252 - anInt8688;
                    anInt5759 = anInt5113 + projectionCenterX - local250 - anInt8887;
                    anInt6565 = scaleX;
                    toolkit.DA(anInt5759, anInt6134, anInt6565, anInt9621);
                }
                Static119.setActivePickList(orthoPickList);
                if (local395 > 0) {
                    toolkit.KA(0, local388, anInt8534, local388 + local395);
                    toolkit.ya();
                    toolkit.GA(backgroundColour);
                    Static283.renderScene(arg7, arg1, arg3, arg2, arg13, arg9, arg12, arg5, arg14, arg6, arg8, arg0, arg15, arg4, arg10, arg11, 1, false);
                }
                if (local342 > 0) {
                    toolkit.KA(local344, local397, local342 + local344, local393 + local397);
                    toolkit.ya();
                    toolkit.GA(backgroundColour);
                    Static283.renderScene(arg7, arg1, arg3, arg2, arg13, arg9, arg12, arg5, arg14, arg6, arg8, arg0, arg15, arg4, arg10, arg11, 1, false);
                }
                toolkit.la();
                Static102.method2021();
                if (anInt6796 == 0) {
                    toolkit.restoreSurface();
                }
                anInt8688 += local252;
                anInt8887 += local250;
                aDouble23 += local258;
                anInt5001 = anInt10569 + local130 - anInt8688;
                anInt45 = local108 + anInt5113 - anInt8887;
                if (anInt6796 == 1) {
                    anInt9503 += local254;
                    anInt7265 += local256;
                    for (@Pc(855) int local855 = 0; local855 < maxY; local855++) {
                        @Pc(868) int local868 = Node_Sub1_Sub27.method9118(anInt7265 + local855, maxY) * maxX;
                        for (local465 = 0; local465 < maxX; local465++) {
                            @Pc(881) int local881 = Node_Sub1_Sub27.method9118(local465 + anInt9503, maxX) + local868;
                            @Pc(936) boolean local936 = local855 >= local319 && local855 < local319 + local321 || local325 <= local855 && local855 < local329 + local325 && local465 >= local323 && local465 < local327 + local323;
                            anOffscreenSurfaceArray1[local881].method9039(local465 * horizontalAspectRatio, verticalAspectRatio * local855, horizontalAspectRatio, verticalAspectRatio, local936);
                        }
                    }
                }
            } else {
                Static75.hasOpaqueStationaryEntities = true;
            }
        }

        if (Static75.hasOpaqueStationaryEntities) {
            cameraX = arg1;
            anInt8887 = 0;
            camaraZ = arg2;
            aDouble23 = 0.0D;
            anInt45 = anInt5113;
            cameraY = arg3;
            anInt8688 = 0;
            anInt5001 = anInt10569;
            if (anInt6796 == 0) {
                toolkit.swapSurface(anOffscreenSurface_1);
            }
            toolkit.la();
            toolkit.ya();
            toolkit.GA(backgroundColour);
            cameraMatrix.createCamera(cameraX, cameraY, camaraZ, cameraRotateX, cameraRotateY, cameraRotateZ);
            toolkit.setCamera(cameraMatrix);
            if (anInt6796 == 1) {
                anInt5759 = projectionCenterX;
                anInt6134 = projectionCenterY;
                anInt6565 = scaleX;
                anInt9621 = scaleY;
                toolkit.DA(anInt5759, anInt6134, anInt6565, anInt9621);
            } else {
                anInt5759 = anInt5113 + projectionCenterX;
                anInt6565 = scaleX;
                anInt9621 = scaleY;
                anInt6134 = projectionCenterY + anInt10569;
                toolkit.DA(anInt5759, anInt6134, anInt6565, anInt9621);
            }
            aDouble13 = 0.0D;
            orthoPickList.method5010();
            Static119.setActivePickList(orthoPickList);
            Static283.renderScene(arg7, arg1, arg3, arg2, arg13, arg9, arg12, arg5, arg14, arg6, arg8, arg0, arg15, arg4, arg10, arg11, 1, false);
            Static102.method2021();
            Static75.hasOpaqueStationaryEntities = false;
            if (anInt6796 == 0) {
                toolkit.restoreSurface();
            }
            if (anInt6796 == 1) {
                method2781();
            }
        }
        if (anInt6796 == 0) {
            anOffscreenSurface_1.method9040(anInt45, anInt5001, orthoWidth, orthoHeight, 0, 0);
        }
        anInt6436++;
        translateCameraZ(aDouble23);
        aDouble15 = aDouble23;
        if (anInt6796 == 0 || anInt6796 == 2) {
            if (anInt6796 == 2) {
                toolkit.GA(backgroundColour);
                toolkit.ya();
            }
            anInt1001 = scaleX;
            anInt8043 = projectionCenterY + anInt10569 - anInt5001 - anInt8688;
            anInt3469 = projectionCenterX + anInt5113 - anInt45 - anInt8887;
            anInt3986 = scaleY;
            toolkit.DA(anInt3469, anInt8043, anInt1001, anInt3986);
        } else if (anInt6796 == 1) {
            anInt1001 = scaleX;
            anInt3469 = projectionCenterX - anInt8887;
            anInt8043 = projectionCenterY - anInt8688;
            anInt3986 = scaleY;
            toolkit.DA(anInt3469, anInt8043, anInt1001, anInt3986);
            toolkit.KA(anInt45, anInt5001, anInt45 + orthoWidth, orthoHeight + anInt5001);
        }
        Static283.renderScene(arg7, arg1, arg3, arg2, arg13, arg9, arg12, arg5, arg14, arg6, arg8, arg0, arg15, arg4, arg10, arg11, anInt6796 == 2 ? 0 : 2, anInt6796 == 1);
        toolkit.la();
        toolkit.DA(projectionCenterX, projectionCenterY, projectionScaleX, projectionScaleY);
    }

    @OriginalMember(owner = "client!wca", name = "a", descriptor = "(II[Ljava/awt/Rectangle;)V")
    public static void flipDirtyRect(@OriginalArg(1) int arg0, @OriginalArg(2) Rectangle[] rectangles) throws FlipException {
        if (anInt6796 == 1) {
            toolkit.flipDirtyRect(rectangles, arg0, anInt45, anInt5001);
        } else {
            toolkit.flipDirtyRect(rectangles, arg0, 0, 0);
        }
    }

    @OriginalMember(owner = "client!fl", name = "a", descriptor = "(B)V")
    public static void method2781() {
        anInt9503 = 0;
        anInt7265 = 0;
        for (@Pc(27) int local27 = 0; local27 < maxY; local27++) {
            @Pc(33) int local33 = maxX * local27;
            for (@Pc(35) int local35 = 0; local35 < maxX; local35++) {
                @Pc(41) int local41 = local35 + local33;
                anOffscreenSurfaceArray1[local41].method9039(local35 * horizontalAspectRatio, verticalAspectRatio * local27, horizontalAspectRatio, verticalAspectRatio, true);
            }
        }
    }

    @OriginalMember(owner = "client!fo", name = "a", descriptor = "(III)V")
    public static void method2821(@OriginalArg(0) int height, @OriginalArg(1) int width) {
        orthoHeight = height;
        orthoWidth = width;

        if (anInt6796 == 0) {
            anInt8534 = orthoWidth + anInt5113 * 2;
            anInt8585 = orthoHeight + anInt10569 * 2;
        } else if (anInt6796 == 1) {
            maxX = (orthoWidth / horizontalAspectRatio) + anInt2268 + 2;
            maxY = (orthoHeight / verticalAspectRatio) + anInt7013 + 2;
            anInt8585 = maxY * verticalAspectRatio;
            anInt8534 = maxX * horizontalAspectRatio;
            anInt5113 = anInt8534 - orthoWidth >> 1;
            anInt10569 = anInt8585 - orthoHeight >> 1;
        } else if (anInt6796 == 2) {
            anInt8534 = orthoWidth;
            anInt8585 = orthoHeight;
        }
    }

    @OriginalMember(owner = "client!hj", name = "a", descriptor = "(IZ)I")
    public static int method3503(@OriginalArg(1) boolean arg0) {
        @Pc(5) int local5 = anInt6796;
        if (local5 == 0) {
            return arg0 ? 0 : anInt45;
        } else if (local5 == 1) {
            return anInt45;
        } else if (local5 == 2) {
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
    public static void method9331(@OriginalArg(1) boolean arg0) {
        aMatrix_11.apply(toolkit.camera());
        @Pc(10) int[] local10 = toolkit.Y();
        anInt5563 = local10[0];
        anInt9536 = local10[1];
        anInt2888 = local10[2];
        anInt9494 = local10[3];
        if (arg0) {
            toolkit.DA(anInt3469, anInt8043, anInt1001, anInt3986);
            translateCameraZ(aDouble15);
        } else {
            toolkit.DA(anInt5759, anInt6134, anInt6565, anInt9621);
            translateCameraZ(aDouble13);
        }
    }

    @OriginalMember(owner = "client!sm", name = "i", descriptor = "(I)I")
    public static int method7779() {
        return anInt6796 == 1 ? anInt8534 : orthoWidth;
    }

    @OriginalMember(owner = "client!sea", name = "a", descriptor = "(IZ)I")
    public static int method7649(@OriginalArg(1) boolean arg0) {
        @Pc(13) int local13 = anInt6796;
        if (local13 == 0) {
            return arg0 ? 0 : anInt5001;
        } else if (local13 == 1) {
            return anInt5001;
        } else if (local13 == 2) {
            return 0;
        } else {
            return 0;
        }
    }

    @OriginalMember(owner = "client!paa", name = "a", descriptor = "(II)V")
    public static void method6448(@OriginalArg(0) int arg0) {
        backgroundColour = arg0;
    }

    @OriginalMember(owner = "client!bu", name = "d", descriptor = "(B)I")
    public static int method1260() {
        return anInt6796 == 1 ? anInt8585 : orthoHeight;
    }
}

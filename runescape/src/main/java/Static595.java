import com.jagex.Client;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.IndexedImage;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.MemoryPool;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static595 {

    @OriginalMember(owner = "client!so", name = "S", descriptor = "[I")
    public static int[] anIntArray702 = new int[1];

    @OriginalMember(owner = "client!so", name = "a", descriptor = "(ILjava/lang/String;ZI)V")
    public static void setToolkit(@OriginalArg(1) String message, @OriginalArg(2) boolean inactive, @OriginalArg(3) int toolkit) {
        Static164.method2606();
        if (toolkit == ToolkitType.JAVA) {
            Toolkit.active = Static255.create(ToolkitType.JAVA, js5.SHADERS, GameShell.canvas, Js5TextureSource.instance, ClientOptions.instance.antialiasingQuality.getValue() * 2);
            if (message != null) {
                Toolkit.active.GA(0);
                @Pc(36) FontMetrics metrics = FontMetrics.loadGroup(js5.FONTMETRICS, Fonts.p12FullGroup);
                @Pc(45) Font font = Toolkit.active.createFont(metrics, IndexedImage.load(js5.SPRITES, Fonts.p12FullGroup, 0), true);
                Static288.repaintMargins();
                MessageBox.draw(Toolkit.active, message, true, metrics, font);
            }
        } else {
            @Pc(57) Toolkit messageToolkit = null;
            @Pc(85) Font font;
            if (message != null) {
                messageToolkit = Static255.create(ToolkitType.JAVA, js5.SHADERS, GameShell.canvas, Js5TextureSource.instance, 0);
                messageToolkit.GA(0);
                @Pc(76) FontMetrics metrics = FontMetrics.loadGroup(js5.FONTMETRICS, Fonts.p12FullGroup);
                font = messageToolkit.createFont(metrics, IndexedImage.load(js5.SPRITES, Fonts.p12FullGroup, 0), true);
                Static288.repaintMargins();
                MessageBox.draw(messageToolkit, message, true, metrics, font);
            }
            boolean unwinding = false;
            label216:
            {
                try {
                    unwinding = true;
                    Toolkit.active = Static255.create(toolkit, js5.SHADERS, GameShell.canvas, Js5TextureSource.instance, ClientOptions.instance.antialiasingQuality.getValue() * 2);
                    if (message != null) {
                        messageToolkit.GA(0);
                        @Pc(118) FontMetrics p12Metrics = FontMetrics.loadGroup(js5.FONTMETRICS, Fonts.p12FullGroup);
                        @Pc(127) Font p12 = messageToolkit.createFont(p12Metrics, IndexedImage.load(js5.SPRITES, Fonts.p12FullGroup, 0), true);
                        Static288.repaintMargins();
                        MessageBox.draw(messageToolkit, message, true, p12Metrics, p12);
                    }
                    if (Toolkit.active.method7949()) {
                        @Pc(141) boolean largeHeap = true;
                        try {
                            largeHeap = SystemInfo.instance.totalMemory > 256;
                        } catch (@Pc(152) Throwable ignored) {
                            /* empty */
                        }
                        @Pc(158) MemoryPool heap;
                        if (largeHeap) {
                            heap = Toolkit.active.createHeap(0x8C00000);
                        } else {
                            heap = Toolkit.active.createHeap(0x6400000);
                        }
                        Toolkit.active.method7938(heap);
                        unwinding = false;
                    } else {
                        unwinding = false;
                    }
                    break label216;
                } catch (@Pc(168) Throwable ex) {
                    @Pc(173) int previousToolkit = ClientOptions.instance.toolkit.getValue();
                    if (previousToolkit == 2) {
                        Static171.graphicsError = true;
                    }
                    ClientOptions.instance.update(0, ClientOptions.instance.toolkit);
                    setToolkit(message, inactive, previousToolkit);
                    @Pc(194) Object unused = null;
                    unwinding = false;
                } finally {
                    if (unwinding) {
                        font = null;
                        if (messageToolkit != null) {
                            try {
                                messageToolkit.free();
                            } catch (@Pc(359) Throwable ignored) {
                                /* empty */
                            }
                        }
                    }
                }
                if (messageToolkit != null) {
                    try {
                        messageToolkit.free();
                    } catch (@Pc(339) Throwable ignored) {
                        /* empty */
                    }
                }
                return;
            }
            font = null;
            if (messageToolkit != null) {
                try {
                    messageToolkit.free();
                } catch (@Pc(349) Throwable ignored) {
                    /* empty */
                }
            }
        }
        ClientOptions.instance.toolkit.setActive(!inactive);
        ClientOptions.instance.update(toolkit, ClientOptions.instance.toolkit);
        Static112.method2109();
        Toolkit.active.method8003();
        Toolkit.active.X(32);
        Static460.aMatrix_10 = Toolkit.active.createMatrix();
        Static59.aMatrix_5 = Toolkit.active.createMatrix();
        Static209.method3110();
        Toolkit.active.setShrinkTextures(ClientOptions.instance.smallTextures.getValue() == 1);
        if (Toolkit.active.supportsBloom()) {
            Static249.setBloom(ClientOptions.instance.bloom.getValue() == 1);
        }
        Static613.method8239(Toolkit.active, Static501.mapLength >> 3, Static720.mapWidth >> 3);
        InterfaceManager.loginOpened();
        Client.changingWindowMode = true;
        Fullscreen.modes = null;
        Static503.sentPreferences = false;
        OrthoMode.enter();
    }

    @OriginalMember(owner = "client!so", name = "a", descriptor = "(IBIII)V")
    public static void expandDirtyRect(@OriginalArg(0) int bottom, @OriginalArg(2) int right, @OriginalArg(3) int top, @OriginalArg(4) int left) {
        @Pc(5) int mode = Static691.anInt10368;
        if (mode == 0) {
            return;
        }
        if (mode == 1) {
            Static599.anInt8837 = right;
            Static691.anInt10368 = 2;
            Static435.anInt6597 = left;
            Static320.anInt5085 = bottom;
            Static582.anInt8629 = top;
        } else if (mode == 2) {
            if (Static320.anInt5085 > bottom) {
                Static320.anInt5085 = bottom;
            }
            if (Static435.anInt6597 > left) {
                Static435.anInt6597 = left;
            }
            if (Static599.anInt8837 < right) {
                Static599.anInt8837 = right;
            }
            if (Static582.anInt8629 < top) {
                Static582.anInt8629 = top;
            }
        }
    }

}

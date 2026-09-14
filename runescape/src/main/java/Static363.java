import com.jagex.Client;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.core.util.SystemTimer;
import com.jagex.game.LocalisedText;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Dimension;

public final class Static363 {

    @OriginalMember(owner = "client!li", name = "h", descriptor = "[[B")
    public static byte[][] aByteArrayArray22;

    @OriginalMember(owner = "client!li", name = "c", descriptor = "Lclient!nga;")
    public static final Class259 aClass259_14 = new Class259();

    @OriginalMember(owner = "client!li", name = "a", descriptor = "(I[Ljava/lang/String;)V")
    public static void method6234(@OriginalArg(1) String[] lines) {
        if (lines.length <= 1) {
            debugconsole.currententry = debugconsole.currententry + lines[0];
            debugconsole.currententryLength += lines[0].length();
            return;
        }
        for (@Pc(41) int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("pause")) {
                @Pc(61) int pauseSeconds = 5;
                try {
                    pauseSeconds = Integer.parseInt(lines[i].substring(6));
                } catch (@Pc(70) Exception ignored) {
                    /* empty */
                }
                debugconsole.addline("Pausing for " + pauseSeconds + " seconds...");
                Static144.aStringArray7 = lines;
                Static523.consoleScriptLine = i + 1;
                Static305.aLong157 = (long) (pauseSeconds * 1000) + SystemTimer.safetime();
                return;
            }
            debugconsole.currententry = lines[i];
            debugconsole.method3920(false);
        }
    }

    @OriginalMember(owner = "client!li", name = "a", descriptor = "(III)I")
    public static int profileToolkit(@OriginalArg(0) int timeLimit, @OriginalArg(1) int toolkit) {
        if (GraphicsDefaults.instance.profilingModel == -1) {
            return 1;
        }

        if (toolkit != ClientOptions.instance.toolkit.getValue()) {
            Static667.setToolkit(true, LocalisedText.PROFILING.localise(Client.language), toolkit);

            if (toolkit != ClientOptions.instance.toolkit.getValue()) {
                return -1;
            }
        }

        try {
            @Pc(43) Dimension size = GameShell.canvas.getSize();
            MessageBox.draw(Toolkit.active, LocalisedText.PROFILING.localise(Client.language), true, Fonts.p12Metrics, Fonts.p12);
            @Pc(67) Mesh mesh = Mesh.load(GraphicsDefaults.instance.profilingModel, js5.MODELS);
            @Pc(70) long start = SystemTimer.safetime();
            Toolkit.active.la();
            Static460.aMatrix_10.applyTranslation(0, EnvironmentLight.anInt3993, 0);
            Toolkit.active.setCamera(Static460.aMatrix_10);
            Toolkit.active.DA(size.width / 2, size.height / 2, 512, 512);
            Toolkit.active.xa(1.0F);
            Toolkit.active.ZA(16777215, 0.5F, 0.5F, 20.0F, -50.0F, 30.0F);
            @Pc(111) Model model = Toolkit.active.createModel(mesh, 2048, 64, 64, 768);
            @Pc(113) int renderCount = 0;
            label41:
            for (@Pc(115) int frame = 0; frame < 500; frame++) {
                Toolkit.active.GA(0);
                Toolkit.active.ya();
                for (@Pc(123) int row = 15; row >= 0; row--) {
                    for (@Pc(126) int column = 0; column <= row; column++) {
                        Static59.aMatrix_5.applyTranslation((int) ((float) Static340.anInt5586 * (-((float) row / 2.0F) + (float) column)), 0, (row + 1) * Static340.anInt5586);
                        model.render(Static59.aMatrix_5, null, 0);
                        renderCount++;
                        if ((long) timeLimit <= SystemTimer.safetime() - start) {
                            break label41;
                        }
                    }
                }
            }
            Toolkit.active.method7950();
            @Pc(195) long rendersPerSecond = (long) (renderCount * 1000) / (SystemTimer.safetime() - start);
            Toolkit.active.GA(0);
            Toolkit.active.ya();
            return (int) rendersPerSecond;
        } catch (@Pc(204) Throwable ex) {
            ex.printStackTrace();
            return -1;
        }
    }

}

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.jagex.AppletParameters;
import com.jagex.Jawt;
import com.jagex.KeyRing;
import com.jagex.KeyRingReader;
import com.jagex.NativeLibraries;
import com.jagex.awt.GameApplet;
import com.jagex.awt.GameFrame;
import com.jagex.awt.ImageLoader;
import com.jagex.game.runetek6.client.GameShell;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public final class Application implements AppletStub {

    private static final List<String> ICON_RESOURCES = List.of(
        "/icon16.png",
        "/icon32.png",
        "/icon48.png"
    );

    public static void main(String[] args) {
        try {
            var parsed = new ApplicationArgs();

            var jcommander = JCommander.newBuilder()
                .programName("client")
                .addObject(parsed)
                .build();

            jcommander.parse(args);

            if (parsed.printHelp()) {
                jcommander.usage();
            } else {
                from(parsed).start();
            }
        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
            ex.getJCommander().usage();
            System.exit(1);
        } catch (Throwable t) {
            System.out.println("Failed to start client:");
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static Application from(ApplicationArgs args) throws IOException {
        var keys = KeyRingReader.read(
            args.getJs5PublicKeyPath(),
            args.getLoginPublicKeyPath()
        );

        return new Application(
            new URL(args.getDocumentBase()),
            AppletParameters.createDefault(),
            keys
        );
    }

    private final URL documentBase;

    private final Map<String, String> parameters;

    private final KeyRing keys;

    public Application(URL documentBase, Map<String, String> parameters, KeyRing keys) {
        this.documentBase = documentBase;
        this.parameters = parameters;
        this.keys = keys;
    }

    private void start() {
        StallReport.watchIfAsked();
        installKeys();
        NativeLibraries.install();
        Jawt.tryLoad();

        var applet = GameApplet.create(this);
        GameShell.provideLoaderApplet(applet);

        var game = new client();
        game.init();
        game.start();

        var toolkit = Toolkit.getDefaultToolkit();
        var loader = new ImageLoader(toolkit);
        var icons = loader.loadAll(ICON_RESOURCES);

        new GameFrame("Jagex", icons).open(applet);
    }

    private void installKeys() {
        Static442.JS5_RSA_EXPONENT = keys.getJs5().getExponent();
        Static670.JS5_RSA_MODULUS = keys.getJs5().getModulus();

        LoginManager.RSA_EXPONENT = keys.getLogin().getExponent();
        LoginManager.RSA_MODULUS = keys.getLogin().getModulus();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return documentBase;
    }

    @Override
    public URL getCodeBase() {
        return documentBase;
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(int width, int height) {
        /* empty */
    }
}

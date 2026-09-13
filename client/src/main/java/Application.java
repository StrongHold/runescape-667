import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.jagex.Parameters;
import com.jagex.awt.CloseWindowListener;
import com.jagex.crypto.rsa.RsaPublicKeyReader;
import com.jagex.game.runetek6.client.GameShell;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jagex.Messages.JAWT_FAILED;
import static com.jagex.Messages.JS5_KEY_READING;
import static com.jagex.Messages.LOGIN_KEY_READING;
import static com.jagex.Messages.formatAbsolute;
import static com.jagex.awt.Dimensions.MINIMUM_SIZE;
import static com.jagex.awt.Dimensions.PREFERRED_SIZE;

public final class Application implements AppletStub {

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
                var documentBase = new URL(parsed.getDocumentBase());
                var parameters = Parameters.createDefault();
                var application = new Application(documentBase, parsed.getJs5PublicKeyPath(), parsed.getLoginPublicKeyPath(), parameters);
                application.start();
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

    private final URL documentBase;

    private final Path js5PublicKey;

    private final Path loginPublicKey;

    private final Map<String, String> parameters;

    public Application(URL documentBase, Path js5PublicKey, Path loginPublicKey, Map<String, String> parameters) {
        this.documentBase = documentBase;
        this.js5PublicKey = js5PublicKey;
        this.loginPublicKey = loginPublicKey;
        this.parameters = parameters;
    }

    private void start() throws IOException {
        setJs5PublicKey();
        setLoginPublicKey();
        tryLoadJawt();

        var applet = new Applet();
        applet.setStub(this);
        applet.setBackground(Color.BLACK);

        applet.setMinimumSize(MINIMUM_SIZE);
        applet.setPreferredSize(MINIMUM_SIZE);
        applet.setSize(MINIMUM_SIZE);

        GameShell.provideLoaderApplet(applet);

        var client = new client();
        client.init();
        client.start();

        var images = Stream.of("icon16.png", "icon32.png", "icon48.png")
            .map(file -> Toolkit.getDefaultToolkit().getImage(getClass().getResource(file)))
            .collect(Collectors.toList());

        var frame = new Frame("Jagex");
        frame.setIconImages(images);

        frame.add(applet);
        frame.addWindowListener(new CloseWindowListener());
        frame.pack();

        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.toFront();

        frame.setMinimumSize(frame.getSize());
        frame.setPreferredSize(PREFERRED_SIZE);
        frame.setSize(PREFERRED_SIZE);
    }

    private void setJs5PublicKey() throws IOException {
        var js5Msg = formatAbsolute(JS5_KEY_READING, js5PublicKey);
        System.out.println(js5Msg);

        var key = RsaPublicKeyReader.read(js5PublicKey);
        Static442.JS5_RSA_EXPONENT = key.getExponent();
        Static670.JS5_RSA_MODULUS = key.getModulus();
    }

    private void setLoginPublicKey() throws IOException {
        var js5Msg = formatAbsolute(LOGIN_KEY_READING, loginPublicKey);
        System.out.println(js5Msg);

        var key = RsaPublicKeyReader.read(loginPublicKey);
        LoginManager.RSA_EXPONENT = key.getExponent();
        LoginManager.RSA_MODULUS = key.getModulus();
    }

    private void tryLoadJawt() {
        try {
            System.loadLibrary("jawt");
        } catch (Throwable t) {
            var os = System.getProperty("os.name");

            if (os.toLowerCase().contains("windows")) {
                System.err.println(JAWT_FAILED);
                t.printStackTrace();
            }
        }
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

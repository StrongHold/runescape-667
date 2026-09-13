import com.beust.jcommander.Parameter;

import java.nio.file.Path;

public final class ApplicationArgs {

    @Parameter(
        names = "--base",
        description = "The URL of the document in which this applet is embedded"
    )
    private String documentBase = "http://127.0.0.1/";

    public String getDocumentBase() {
        return documentBase;
    }

    @Parameter(
        names = "--js5",
        description = "The path to the JS5 public key file in PEM",
        required = true
    )
    private Path js5PublicKeyPath;

    public Path getJs5PublicKeyPath() {
        return js5PublicKeyPath;
    }

    @Parameter(
        names = "--login",
        description = "The path to the login public key file",
        required = true
    )
    private Path loginPublicKeyPath;

    public Path getLoginPublicKeyPath() {
        return loginPublicKeyPath;
    }

    @Parameter(
        names = "--help",
        help = true,
        description = "print this help message to the output stream"
    )
    private boolean help;

    public boolean printHelp() {
        return help;
    }
}

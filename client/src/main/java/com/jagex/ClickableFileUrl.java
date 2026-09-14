package com.jagex;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * Renders a path as a {@code file:} URL, which terminals show as a clickable link.
 */
public final class ClickableFileUrl {

    public static String of(Path path) {
        var absolute = path.toAbsolutePath().normalize();

        try {
            return new URI(
                /* scheme = */ "file",
                /* authority = */ "",
                /* path = */ absolute.toUri().getPath(),
                /* query = */ null,
                /* fragment = */ null
            ).toASCIIString();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ClickableFileUrl() {
        /* empty */
    }
}

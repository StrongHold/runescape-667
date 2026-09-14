package com.jagex;

import com.jagex.crypto.rsa.RsaPublicKey;
import com.jagex.crypto.rsa.RsaPublicKeyReader;

import java.io.IOException;
import java.nio.file.Path;

public final class KeyRingReader {

    private static final String JS5_KEY_READING = "Reading JS5 public key from: %s";

    private static final String LOGIN_KEY_READING = "Reading login public key from: %s";

    public static KeyRing read(Path js5, Path login) throws IOException {
        return new KeyRing(
            read(JS5_KEY_READING, js5),
            read(LOGIN_KEY_READING, login)
        );
    }

    private static RsaPublicKey read(String message, Path path) throws IOException {
        System.out.println(String.format(message, ClickableFileUrl.of(path)));
        return RsaPublicKeyReader.read(path);
    }

    private KeyRingReader() {
        /* empty */
    }
}

package com.jagex;

import java.nio.file.Path;

public final class Messages {

    public static final String FIRST_PATH_MISSING = "Provide path to public RSA key as first program argument";

    public static final String PUBLIC_KEY_NOT_FOUND = "No public key file found at: %s";

    public static final String JS5_KEY_READING = "JS5 public key reading from: %s";

    public static final String LOGIN_KEY_READING = "Login public key reading from: %s";

    public static final String LOGIN_KEY_REUSING_JS5 = "Login public key reusing JS5 public key";

    public static final String JAWT_FAILED = "Failed to load jawt.dll - only safe mode will function. Try reinstalling Java.";

    public static String formatAbsolute(String format, Path path) {
        return String.format(format, absolutePath(path));
    }

    private static String absolutePath(Path path) {
        return "file://" + path.toAbsolutePath().normalize();
    }

    private Messages() {
        /* empty */
    }
}
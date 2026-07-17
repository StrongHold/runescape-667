package com.jagex.core.constants;

/**
 * The ports the client dials, and the offsets it derives them from outside LIVE mode.
 */
public final class Ports {

    /**
     * The default port for game, lobby and js5 connections
     * ({@link com.jagex.core.io.ConnectionInfo#defaultPort}). Outside LIVE mode the
     * default port is instead derived from the world or lobby id via
     * {@link #DEFAULT_OFFSET}.
     */
    public static final int DEFAULT = 43594;

    /**
     * The https port, serving two purposes: it is the alternate port for game, lobby and
     * js5 connections ({@link com.jagex.core.io.ConnectionInfo#alternatePort}), chosen
     * because networks
     * that block arbitrary ports usually pass https traffic (the protocol spoken over it
     * is still the game protocol, not TLS); and connecting to it through a proxy makes
     * the client ask the system proxy selector for https proxies first. Outside LIVE mode
     * the alternate port is instead derived from the world or lobby id via
     * {@link #ALTERNATE_OFFSET}.
     */
    public static final int HTTPS = 443;

    /**
     * The http port a LIVE world's website (loader page, news feed) is served on. Outside
     * LIVE mode the website port is instead derived from the world id via
     * {@link #HTTP_OFFSET}.
     */
    public static final int HTTP = 80;

    /**
     * Added to a world or lobby id to derive its default port outside LIVE mode: world 1
     * listens on 40001 and lobby 1000 on 41000. Worlds and lobbies share one id space so
     * that these derivations never collide.
     */
    public static final int DEFAULT_OFFSET = 40000;

    /**
     * Added to a world or lobby id to derive its alternate port outside LIVE mode,
     * standing in for {@link #HTTPS}: world 1 falls back to 50001 and lobby 1000 to
     * 51000.
     */
    public static final int ALTERNATE_OFFSET = 50000;

    /**
     * Added to a world id to derive its website port outside LIVE mode, standing in for
     * {@link #HTTP}: world 1 serves its loader page and news feed on 7001.
     */
    public static final int HTTP_OFFSET = 7000;

    private Ports() {
        /* empty */
    }
}

package com.jagex.core.constants;

/**
 * The ports the client dials, and the offsets it derives them from outside LIVE mode.
 */
public final class Ports {

    /**
     * The https port: the primary port for game, lobby and js5 connections
     * ({@link com.jagex.core.io.ConnectionInfo#primaryPort}), dialled first because
     * networks that block arbitrary ports usually pass https traffic (the protocol
     * spoken over it is still the game protocol, not TLS). Connecting to it through a
     * proxy also makes the client ask the system proxy selector for https proxies
     * first. Outside LIVE mode the primary port is instead derived from the world or
     * lobby id via {@link #PRIMARY_OFFSET}.
     */
    public static final int HTTPS = 443;

    /**
     * The well-known RuneScape port: the secondary port for game, lobby and js5
     * connections ({@link com.jagex.core.io.ConnectionInfo#secondaryPort}), dialled
     * after a failed dial on the primary port. Outside LIVE mode the secondary port is
     * instead derived from the world or lobby id via {@link #SECONDARY_OFFSET}.
     */
    public static final int SECONDARY = 43594;

    /**
     * The http port a LIVE world's website (loader page, news feed) is served on.
     * Outside LIVE mode the website port is instead derived from the world id via
     * {@link #HTTP_OFFSET}.
     */
    public static final int HTTP = 80;

    /**
     * Added to a world or lobby id to derive its primary port outside LIVE mode,
     * standing in for {@link #HTTPS}: world 1 is dialled first on 50001 and lobby 1000
     * on 51000.
     */
    public static final int PRIMARY_OFFSET = 50000;

    /**
     * Added to a world or lobby id to derive its secondary port outside LIVE mode,
     * standing in for {@link #SECONDARY}: world 1 falls back to 40001 and lobby 1000 to
     * 41000. Worlds and lobbies share one id space so that these derivations never
     * collide.
     */
    public static final int SECONDARY_OFFSET = 40000;

    /**
     * Added to a world id to derive its website port outside LIVE mode, standing in for
     * {@link #HTTP}: world 1 serves its loader page and news feed on 7001.
     */
    public static final int HTTP_OFFSET = 7000;

    private Ports() {
        /* empty */
    }
}

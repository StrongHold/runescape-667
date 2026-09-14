package com.jagex;

import com.jagex.crypto.rsa.RsaPublicKey;

/**
 * The public keys the client verifies its two server connections with.
 */
public final class KeyRing {

    private final RsaPublicKey js5;

    private final RsaPublicKey login;

    public KeyRing(RsaPublicKey js5, RsaPublicKey login) {
        this.js5 = js5;
        this.login = login;
    }

    public RsaPublicKey getJs5() {
        return js5;
    }

    public RsaPublicKey getLogin() {
        return login;
    }
}

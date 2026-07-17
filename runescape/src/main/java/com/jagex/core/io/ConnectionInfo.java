package com.jagex.core.io;

import com.jagex.core.constants.Ports;
import com.jagex.sign.SignLink;
import com.jagex.sign.SignedResource;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!lja")
public final class ConnectionInfo {

    @OriginalMember(owner = "client!kr", name = "f", descriptor = "Lclient!lja;")
    public static ConnectionInfo login;

    @OriginalMember(owner = "client!qka", name = "f", descriptor = "Lclient!lja;")
    public static ConnectionInfo game;

    @OriginalMember(owner = "client!uu", name = "o", descriptor = "Lclient!lja;")
    public static ConnectionInfo lobby;

    @OriginalMember(owner = "client!vca", name = "r", descriptor = "Lclient!lja;")
    public static ConnectionInfo auto;

    @OriginalMember(owner = "client!oia", name = "n", descriptor = "Lclient!lja;")
    public static ConnectionInfo previous;

    @OriginalMember(owner = "client!lja", name = "e", descriptor = "I")
    public int world;

    @OriginalMember(owner = "client!lja", name = "i", descriptor = "Ljava/lang/String;")
    public String address;

    @OriginalMember(owner = "client!lja", name = "m", descriptor = "I")
    public int defaultPort = Ports.DEFAULT;

    @OriginalMember(owner = "client!lja", name = "k", descriptor = "I")
    public int alternatePort = Ports.HTTPS;

    @OriginalMember(owner = "client!lja", name = "d", descriptor = "Z")
    public boolean proxy = false;

    @OriginalMember(owner = "client!lja", name = "f", descriptor = "Z")
    public boolean alternate = true;

    @OriginalMember(owner = "client!lja", name = "a", descriptor = "(I)V")
    public void rotateMethods() {
        if (!this.alternate) {
            this.proxy = true;
            this.alternate = true;
        } else if (this.proxy) {
            this.proxy = false;
        } else {
            this.alternate = false;
        }
    }

    @OriginalMember(owner = "client!lja", name = "a", descriptor = "(BLclient!vq;)Lclient!oba;")
    public SignedResource openSocket(@OriginalArg(1) SignLink signLink) {
        return signLink.openSocket(this.address, this.alternate ? this.alternatePort : this.defaultPort, this.proxy);
    }

    @OriginalMember(owner = "client!lja", name = "a", descriptor = "(ILclient!lja;)Z")
    public boolean equalTo(@OriginalArg(1) ConnectionInfo other) {
        if (other == null) {
            return false;
        } else {
            return other.world == this.world && this.address.equals(other.address);
        }
    }
}

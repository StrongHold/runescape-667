package com.jagex.game.runetek6.config.idktype;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kr")
public final class IDKTypeList {

    private static final int DEFAULT_CACHE_SIZE = 64;

    @OriginalMember(owner = "client!cd", name = "r", descriptor = "Lclient!kr;")
    public static IDKTypeList instance;

    @OriginalMember(owner = "client!kr", name = "n", descriptor = "Lclient!dla;")
    public final ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    public final ModeGame game;

    public final int languageId;

    @OriginalMember(owner = "client!kr", name = "b", descriptor = "Lclient!sb;")
    public final js5 meshes;

    @OriginalMember(owner = "client!kr", name = "k", descriptor = "Lclient!sb;")
    public final js5 configClient;

    public final int num;

    @OriginalMember(owner = "client!kr", name = "<init>", descriptor = "(Lclient!ul;ILclient!sb;Lclient!sb;)V")
    public IDKTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) js5 configClient, @OriginalArg(3) js5 meshes) {
        this.game = game;
        this.languageId = languageId;
        this.configClient = configClient;
        this.meshes = meshes;
        this.num = this.configClient.fileLimit(Js5ConfigGroup.IDKTYPE);
    }

    @OriginalMember(owner = "client!kr", name = "d", descriptor = "(I)V")
    public void cacheRemoveSoftReferences() {
        @Pc(6) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!kr", name = "a", descriptor = "(IB)Lclient!pka;")
    public IDKType list(@OriginalArg(0) int id) {
        @Pc(6) ReferenceCache lookupLock = this.recentUse;
        @Pc(24) IDKType type;
        synchronized (this.recentUse) {
            type = (IDKType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(38) js5 configLock = this.configClient;
        @Pc(47) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(id, Js5ConfigGroup.IDKTYPE);
        }

        type = new IDKType();
        type.typeList = this;
        if (data != null) {
            type.decode(new Packet(data));
        }

        @Pc(74) ReferenceCache storeLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }

    @OriginalMember(owner = "client!kr", name = "a", descriptor = "(I)V")
    public void cacheReset() {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }
    }

    @OriginalMember(owner = "client!kr", name = "a", descriptor = "(II)V")
    public void cacheClean(@OriginalArg(0) int maxAge) {
        @Pc(7) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(maxAge);
        }
    }
}

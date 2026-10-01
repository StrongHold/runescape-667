package com.jagex.game.runetek6.config.flutype;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!dh")
public final class FloorUnderlayTypeList {

    private static final int DEFAULT_CACHE_SIZE = 128;

    @OriginalMember(owner = "client!r", name = "z", descriptor = "Lclient!dh;")
    public static FloorUnderlayTypeList instance;

    @OriginalMember(owner = "client!dh", name = "i", descriptor = "Lclient!dla;")
    public final ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    private final ModeGame game;

    private final int languageId;

    @OriginalMember(owner = "client!dh", name = "b", descriptor = "Lclient!sb;")
    public final js5 configClient;

    private final int num;

    @OriginalMember(owner = "client!dh", name = "<init>", descriptor = "(Lclient!ul;ILclient!sb;)V")
    public FloorUnderlayTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) js5 configClient) {
        this.game = game;
        this.languageId = languageId;
        this.configClient = configClient;
        this.num = this.configClient.fileLimit(1);
    }

    @OriginalMember(owner = "client!dh", name = "a", descriptor = "(II)Lclient!nq;")
    public FloorUnderlayType list(@OriginalArg(1) int id) {
        @Pc(6) ReferenceCache lookupLock = this.recentUse;
        @Pc(18) FloorUnderlayType type;
        synchronized (this.recentUse) {
            type = (FloorUnderlayType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(32) js5 configLock = this.configClient;
        @Pc(41) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(id, 1);
        }

        type = new FloorUnderlayType();
        if (data != null) {
            type.decode(new Packet(data));
        }

        @Pc(67) ReferenceCache storeLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }

    @OriginalMember(owner = "client!dh", name = "a", descriptor = "(I)V")
    public void cacheReset() {
        @Pc(17) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }
    }

    @OriginalMember(owner = "client!dh", name = "a", descriptor = "(Z)V")
    public void cacheRemoveSoftReferences() {
        @Pc(6) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!dh", name = "c", descriptor = "(II)V")
    public void cacheClean(@OriginalArg(1) int maxAge) {
        @Pc(14) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(5);
        }
    }
}

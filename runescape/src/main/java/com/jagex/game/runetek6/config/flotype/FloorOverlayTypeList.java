package com.jagex.game.runetek6.config.flotype;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ef")
public final class FloorOverlayTypeList {

    private static final int DEFAULT_CACHE_SIZE = 64;

    @OriginalMember(owner = "client!oo", name = "p", descriptor = "Lclient!ef;")
    public static FloorOverlayTypeList instance;

    @OriginalMember(owner = "client!ef", name = "b", descriptor = "Lclient!dla;")
    public final ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    @OriginalMember(owner = "client!ef", name = "i", descriptor = "I")
    public int dflt = 0;

    private final ModeGame game;

    private final int languageId;

    @OriginalMember(owner = "client!ef", name = "j", descriptor = "Lclient!sb;")
    public final js5 configClient;

    @OriginalMember(owner = "client!ef", name = "q", descriptor = "I")
    public final int num;

    @OriginalMember(owner = "client!ef", name = "<init>", descriptor = "(Lclient!ul;ILclient!sb;)V")
    public FloorOverlayTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) js5 configClient) {
        this.game = game;
        this.languageId = languageId;
        this.configClient = configClient;
        this.num = this.configClient.fileLimit(Js5ConfigGroup.FLOTYPE);
    }

    @OriginalMember(owner = "client!ef", name = "a", descriptor = "(B)V")
    public void cacheReset() {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }
    }

    @OriginalMember(owner = "client!ef", name = "b", descriptor = "(I)V")
    public void cacheRemoveSoftReferences() {
        @Pc(9) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!ef", name = "a", descriptor = "(IB)Lclient!re;")
    public FloorOverlayType list(@OriginalArg(0) int id) {
        @Pc(15) ReferenceCache lookupLock = this.recentUse;
        @Pc(25) FloorOverlayType type;
        synchronized (this.recentUse) {
            type = (FloorOverlayType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(39) js5 configLock = this.configClient;
        @Pc(48) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(id, 4);
        }

        type = new FloorOverlayType();
        type.myList = this;
        type.id = id;
        if (data != null) {
            type.decode(new Packet(data));
        }
        type.postDecode();

        @Pc(81) ReferenceCache storeLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }

    @OriginalMember(owner = "client!ef", name = "a", descriptor = "(II)V")
    public void cacheClean(@OriginalArg(0) int maxAge) {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(maxAge);
        }
    }
}

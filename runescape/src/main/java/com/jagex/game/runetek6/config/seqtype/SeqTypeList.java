package com.jagex.game.runetek6.config.seqtype;

import com.jagex.AnimFrameset;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!bp")
public final class SeqTypeList {

    private static final int DEFAULT_CACHE_SIZE = 64;

    @OriginalMember(owner = "client!as", name = "d", descriptor = "Lclient!bp;")
    public static SeqTypeList instance;

    @OriginalMember(owner = "client!vca", name = "a", descriptor = "(II)I")
    private static int fileId(@OriginalArg(0) int id) {
        return id & 0x7F;
    }

    @OriginalMember(owner = "client!je", name = "b", descriptor = "(II)I")
    private static int groupId(@OriginalArg(1) int id) {
        return id >>> 7;
    }

    @OriginalMember(owner = "client!bp", name = "a", descriptor = "Lclient!dla;")
    public final ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    @OriginalMember(owner = "client!bp", name = "f", descriptor = "Lclient!dla;")
    public final ReferenceCache framesetCache = new ReferenceCache(100);

    private final ModeGame game;

    private final int languageId;

    @OriginalMember(owner = "client!bp", name = "j", descriptor = "Lclient!sb;")
    public final js5 configClient;

    private final int num;

    @OriginalMember(owner = "client!bp", name = "<init>", descriptor = "(Lclient!ul;ILclient!sb;Lclient!sb;Lclient!sb;)V")
    public SeqTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) js5 configClient, @OriginalArg(3) js5 anims, @OriginalArg(4) js5 bases) {
        this.game = game;
        this.languageId = languageId;
        this.configClient = configClient;

        if (this.configClient != null) {
            @Pc(26) int lastGroup = this.configClient.groupSize() - 1;
            this.num = this.configClient.fileLimit(lastGroup);
        } else {
            this.num = 0;
        }

        AnimFrameset.init(bases, anims);
    }

    @OriginalMember(owner = "client!bp", name = "a", descriptor = "(IB)Lclient!cka;")
    public SeqType list(@OriginalArg(0) int id) {
        @Pc(6) ReferenceCache lock = this.recentUse;
        @Pc(16) SeqType type;
        synchronized (this.recentUse) {
            type = (SeqType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(30) js5 configLock = this.configClient;
        @Pc(43) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(fileId(id), groupId(id));
        }

        type = new SeqType();
        type.id = id;
        if (data != null) {
            type.decode(new Packet(data));
        }
        type.postDecode();

        @Pc(73) ReferenceCache putLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }

    @OriginalMember(owner = "client!bp", name = "a", descriptor = "(II)V")
    public void cacheClean(@OriginalArg(1) int maxAge) {
        @Pc(6) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(maxAge);
        }
        lock = this.framesetCache;
        synchronized (this.framesetCache) {
            this.framesetCache.clean(maxAge);
        }
    }

    @OriginalMember(owner = "client!bp", name = "b", descriptor = "(I)V")
    public void cacheRemoveSoftReferences() {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }
        lock = this.framesetCache;
        synchronized (this.framesetCache) {
            this.framesetCache.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!bp", name = "a", descriptor = "(B)V")
    public void cacheReset() {
        @Pc(7) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }
        lock = this.framesetCache;
        synchronized (this.framesetCache) {
            this.framesetCache.reset();
        }
    }

    @OriginalMember(owner = "client!bp", name = "b", descriptor = "(II)Lclient!rw;")
    public AnimFrameset getFrameset(@OriginalArg(0) int id) {
        @Pc(12) ReferenceCache lock = this.framesetCache;
        synchronized (this.framesetCache) {
            @Pc(22) AnimFrameset frameset = (AnimFrameset) this.framesetCache.get(id);
            if (frameset == null) {
                frameset = new AnimFrameset(id);
                this.framesetCache.put(frameset, id);
            }

            return frameset.isReady() ? frameset : null;
        }
    }
}

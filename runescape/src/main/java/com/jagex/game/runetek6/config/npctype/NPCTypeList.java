package com.jagex.game.runetek6.config.npctype;

import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.LocalisedText;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ql")
public final class NPCTypeList {

    private static final int DEFAULT_CACHE_SIZE = 64;

    @OriginalMember(owner = "client!vr", name = "b", descriptor = "Lclient!ql;")
    public static NPCTypeList instance;

    @OriginalMember(owner = "client!wk", name = "a", descriptor = "(IB)I")
    private static int groupId(@OriginalArg(0) int id) {
        return id >>> 7;
    }

    @OriginalMember(owner = "client!tg", name = "a", descriptor = "(ZI)I")
    private static int fileId(@OriginalArg(1) int id) {
        return id & 0x7F;
    }

    @OriginalMember(owner = "client!ql", name = "q", descriptor = "I")
    public int featureMask;

    @OriginalMember(owner = "client!ql", name = "j", descriptor = "Lclient!dla;")
    public final ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    @OriginalMember(owner = "client!ql", name = "i", descriptor = "Lclient!dla;")
    public final ReferenceCache models = new ReferenceCache(50);

    @OriginalMember(owner = "client!ql", name = "r", descriptor = "Lclient!dla;")
    public final ReferenceCache headModels = new ReferenceCache(5);

    @OriginalMember(owner = "client!ql", name = "g", descriptor = "Lclient!sb;")
    public final js5 meshes;

    @OriginalMember(owner = "client!ql", name = "a", descriptor = "Lclient!sb;")
    public final js5 configClient;

    @OriginalMember(owner = "client!ql", name = "k", descriptor = "Z")
    public boolean allowMembers;

    @OriginalMember(owner = "client!ql", name = "t", descriptor = "I")
    public final int languageId;

    @OriginalMember(owner = "client!ql", name = "o", descriptor = "Lclient!ul;")
    public final ModeGame game;

    private final int num;

    @OriginalMember(owner = "client!ql", name = "s", descriptor = "[Ljava/lang/String;")
    public final String[] defaultOps;

    @OriginalMember(owner = "client!ql", name = "<init>", descriptor = "(Lclient!ul;IZLclient!sb;Lclient!sb;)V")
    public NPCTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) boolean allowMembers, @OriginalArg(3) js5 configClient, @OriginalArg(4) js5 meshes) {
        this.meshes = meshes;
        this.configClient = configClient;
        this.allowMembers = allowMembers;
        this.languageId = languageId;
        this.game = game;

        if (this.configClient != null) {
            @Pc(44) int lastGroup = this.configClient.groupSize() - 1;
            this.num = this.configClient.fileLimit(lastGroup);
        } else {
            this.num = 0;
        }

        if (ModeGame.RUNESCAPE == this.game) {
            this.defaultOps = new String[]{
                /* 1 */ null,
                /* 2 */ null,
                /* 3 */ null,
                /* 4 */ null,
                /* 5 */ null,
                /* 6 */ LocalisedText.EXAMINE.localise(this.languageId)
            };
        } else {
            this.defaultOps = new String[]{
                /* 1 */ null,
                /* 2 */ null,
                /* 3 */ null,
                /* 4 */ null,
                /* 5 */ null,
                /* 6 */ null
            };
        }
    }

    @OriginalMember(owner = "client!ql", name = "a", descriptor = "(II)V")
    public void cacheClean(@OriginalArg(0) int maxAge) {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(maxAge);
        }
        lock = this.models;
        synchronized (this.models) {
            this.models.clean(maxAge);
        }
        lock = this.headModels;
        synchronized (this.headModels) {
            this.headModels.clean(maxAge);
        }
    }

    @OriginalMember(owner = "client!ql", name = "b", descriptor = "(B)V")
    public void cacheReset() {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }
        lock = this.models;
        synchronized (this.models) {
            this.models.reset();
        }
        lock = this.headModels;
        synchronized (this.headModels) {
            this.headModels.reset();
        }
    }

    @OriginalMember(owner = "client!ql", name = "a", descriptor = "(B)V")
    public void cacheRemoveSoftReferences() {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }
        lock = this.models;
        synchronized (this.models) {
            this.models.removeSoftReferences();
        }
        lock = this.headModels;
        synchronized (this.headModels) {
            this.headModels.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!ql", name = "a", descriptor = "(BZ)V")
    public void setAllowMembers(@OriginalArg(1) boolean allowMembers) {
        if (allowMembers != this.allowMembers) {
            this.allowMembers = allowMembers;
            this.cacheReset();
        }
    }

    @OriginalMember(owner = "client!ql", name = "c", descriptor = "(B)V")
    public void modelCacheReset() {
        @Pc(2) ReferenceCache lock = this.models;
        synchronized (this.models) {
            this.models.reset();
        }
        lock = this.headModels;
        synchronized (this.headModels) {
            this.headModels.reset();
        }
    }

    @OriginalMember(owner = "client!ql", name = "a", descriptor = "(IB)Lclient!o;")
    public NPCType list(@OriginalArg(0) int id) {
        @Pc(14) ReferenceCache lookupLock = this.recentUse;
        @Pc(24) NPCType type;
        synchronized (this.recentUse) {
            type = (NPCType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(38) js5 configLock = this.configClient;
        @Pc(51) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(fileId(id), groupId(id));
        }

        type = new NPCType();
        type.typeList = this;
        type.id = id;
        type.op = this.defaultOps.clone();
        if (data != null) {
            type.decode(new Packet(data));
        }
        type.postDecode();

        @Pc(90) ReferenceCache storeLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }

    @OriginalMember(owner = "client!ql", name = "a", descriptor = "(ZI)V")
    public void setFeatureMask(@OriginalArg(1) int featureMask) {
        this.featureMask = featureMask;

        @Pc(9) ReferenceCache lock = this.models;
        synchronized (this.models) {
            this.models.reset();
        }
        lock = this.headModels;
        synchronized (this.headModels) {
            this.headModels.reset();
        }
    }
}

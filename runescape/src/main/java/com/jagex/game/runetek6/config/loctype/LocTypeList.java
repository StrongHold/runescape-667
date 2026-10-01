package com.jagex.game.runetek6.config.loctype;

import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.LocalisedText;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!gea")
public final class LocTypeList {

    private static final int DEFAULT_CACHE_SIZE = 64;

    @OriginalMember(owner = "client!ld", name = "b", descriptor = "Lclient!gea;")
    public static LocTypeList instance;

    @OriginalMember(owner = "client!wf", name = "a", descriptor = "(II)I")
    private static int groupId(@OriginalArg(1) int id) {
        return id >>> 8;
    }

    @OriginalMember(owner = "client!rv", name = "a", descriptor = "(IB)I")
    private static int fileId(@OriginalArg(0) int id) {
        return id & 0xFF;
    }

    @OriginalMember(owner = "client!gea", name = "r", descriptor = "I")
    public int featureMask;

    @OriginalMember(owner = "client!gea", name = "c", descriptor = "Z")
    public boolean animateBackground = false;

    @OriginalMember(owner = "client!gea", name = "l", descriptor = "Lclient!dla;")
    public ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    @OriginalMember(owner = "client!gea", name = "f", descriptor = "Lclient!dla;")
    public final ReferenceCache models = new ReferenceCache(500);

    @OriginalMember(owner = "client!gea", name = "k", descriptor = "Lclient!dla;")
    public final ReferenceCache modelAndShadows = new ReferenceCache(30);

    @OriginalMember(owner = "client!gea", name = "q", descriptor = "Lclient!dla;")
    public final ReferenceCache wallModels = new ReferenceCache(50);

    @OriginalMember(owner = "client!gea", name = "b", descriptor = "Lclient!sb;")
    public final js5 meshes;

    @OriginalMember(owner = "client!gea", name = "j", descriptor = "Lclient!ul;")
    public final ModeGame game;

    @OriginalMember(owner = "client!gea", name = "h", descriptor = "I")
    public final int languageId;

    @OriginalMember(owner = "client!gea", name = "p", descriptor = "Lclient!sb;")
    public final js5 configClient;

    @OriginalMember(owner = "client!gea", name = "d", descriptor = "Z")
    public boolean allowMembers;

    private final int num;

    @OriginalMember(owner = "client!gea", name = "i", descriptor = "[Ljava/lang/String;")
    public final String[] defaultOps;

    @OriginalMember(owner = "client!gea", name = "<init>", descriptor = "(Lclient!ul;IZLclient!sb;Lclient!sb;)V")
    public LocTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) boolean allowMembers, @OriginalArg(3) js5 configClient, @OriginalArg(4) js5 meshes) {
        this.meshes = meshes;
        this.game = game;
        this.languageId = languageId;
        this.configClient = configClient;
        this.allowMembers = allowMembers;

        if (this.configClient != null) {
            @Pc(53) int lastGroup = this.configClient.groupSize() - 1;
            this.num = this.configClient.fileLimit(lastGroup);
        } else {
            this.num = 0;
        }

        if (ModeGame.RUNESCAPE == this.game) {
            this.defaultOps = new String[]{
                /* 0 */ null,
                /* 1 */ null,
                /* 2 */ null,
                /* 3 */ null,
                /* 4 */ null,
                /* 5 */ LocalisedText.EXAMINE.localise(this.languageId)
            };
        } else {
            this.defaultOps = new String[]{
                /* 0 */ null,
                /* 1 */ null,
                /* 2 */ null,
                /* 3 */ null,
                /* 4 */ null,
                /* 5 */ null
            };
        }
    }

    @OriginalMember(owner = "client!gea", name = "a", descriptor = "(I)V")
    public void cacheRemoveSoftReferences() {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }
        lock = this.models;
        synchronized (this.models) {
            this.models.removeSoftReferences();
        }
        lock = this.modelAndShadows;
        synchronized (this.modelAndShadows) {
            this.modelAndShadows.removeSoftReferences();
        }
        lock = this.wallModels;
        synchronized (this.wallModels) {
            this.wallModels.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!gea", name = "a", descriptor = "(ZI)V")
    public void setAllowMembers(@OriginalArg(0) boolean allowMembers) {
        if (allowMembers != this.allowMembers) {
            this.allowMembers = allowMembers;
            this.cacheReset();
        }
    }

    @OriginalMember(owner = "client!gea", name = "b", descriptor = "(I)V")
    public void cacheReset() {
        @Pc(14) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }
        lock = this.models;
        synchronized (this.models) {
            this.models.reset();
        }
        lock = this.modelAndShadows;
        synchronized (this.modelAndShadows) {
            this.modelAndShadows.reset();
        }
        lock = this.wallModels;
        synchronized (this.wallModels) {
            this.wallModels.reset();
        }
    }

    @OriginalMember(owner = "client!gea", name = "a", descriptor = "(IZ)V")
    public void setAnimateBackground(@OriginalArg(1) boolean animateBackground) {
        if (animateBackground != this.animateBackground) {
            this.animateBackground = animateBackground;
            this.cacheReset();
        }
    }

    @OriginalMember(owner = "client!gea", name = "a", descriptor = "(II)V")
    public void setRecentUse(@OriginalArg(1) int size) {
        this.recentUse = new ReferenceCache(size);
    }

    @OriginalMember(owner = "client!gea", name = "d", descriptor = "(II)Lclient!c;")
    public LocType list(@OriginalArg(0) int id) {
        @Pc(12) ReferenceCache getLock = this.recentUse;
        @Pc(22) LocType type;
        synchronized (this.recentUse) {
            type = (LocType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(36) js5 fileLock = this.configClient;
        @Pc(49) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(fileId(id), groupId(id));
        }

        type = new LocType();
        type.id = id;
        type.typeList = this;
        type.ops = this.defaultOps.clone();
        if (data != null) {
            type.decode(new Packet(data));
        }
        type.postDecode();

        if (type.breakroutefinding) {
            type.blockwalk = 0;
            type.blockrange = false;
        }

        if (!this.allowMembers && type.members) {
            type.ops = null;
            type.quests = null;
        }

        @Pc(115) ReferenceCache putLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }

    @OriginalMember(owner = "client!gea", name = "c", descriptor = "(II)V")
    public void cacheClean(@OriginalArg(0) int maxAge) {
        @Pc(11) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(maxAge);
        }
        lock = this.models;
        synchronized (this.models) {
            this.models.clean(maxAge);
        }
        lock = this.modelAndShadows;
        synchronized (this.modelAndShadows) {
            this.modelAndShadows.clean(maxAge);
        }
        lock = this.wallModels;
        synchronized (this.wallModels) {
            this.wallModels.clean(maxAge);
        }
    }

    @OriginalMember(owner = "client!gea", name = "b", descriptor = "(II)V")
    public void setFeatureMask(@OriginalArg(0) int featureMask) {
        this.featureMask = featureMask;
        @Pc(9) ReferenceCache lock = this.models;
        synchronized (this.models) {
            this.models.reset();
        }
        lock = this.modelAndShadows;
        synchronized (this.modelAndShadows) {
            this.modelAndShadows.reset();
        }
        lock = this.wallModels;
        synchronized (this.wallModels) {
            this.wallModels.reset();
        }
    }
}

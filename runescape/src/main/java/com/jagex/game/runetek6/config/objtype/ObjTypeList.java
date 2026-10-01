package com.jagex.game.runetek6.config.objtype;

import com.jagex.ObjSpriteCacheKey;
import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.datastruct.ref.key.KeyedReferenceCache;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.graphics.Font;
import com.jagex.game.LocalisedText;
import com.jagex.game.PlayerModel;
import com.jagex.game.runetek6.config.paramtype.ParamType;
import com.jagex.game.runetek6.config.paramtype.ParamTypeList;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!es")
public final class ObjTypeList {

    private static final int DEFAULT_CACHE_SIZE = 64;

    @OriginalMember(owner = "client!sm", name = "a", descriptor = "(II)I")
    private static int groupId(@OriginalArg(1) int id) {
        return id >>> 8;
    }

    @OriginalMember(owner = "client!gu", name = "b", descriptor = "(ZI)I")
    private static int fileId(@OriginalArg(1) int id) {
        return id & 0xFF;
    }

    @OriginalMember(owner = "client!ne", name = "n", descriptor = "Lclient!es;")
    public static ObjTypeList instance;

    @OriginalMember(owner = "client!es", name = "p", descriptor = "I")
    public int featureMask;

    @OriginalMember(owner = "client!es", name = "o", descriptor = "Lclient!dla;")
    public final ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    @OriginalMember(owner = "client!es", name = "b", descriptor = "Lclient!dla;")
    public final ReferenceCache modelCache = new ReferenceCache(50);

    @OriginalMember(owner = "client!es", name = "f", descriptor = "Lclient!aka;")
    public final KeyedReferenceCache spriteCache = new KeyedReferenceCache(250);

    @OriginalMember(owner = "client!es", name = "j", descriptor = "Lclient!rla;")
    public final ObjSpriteCacheKey spriteCacheKey = new ObjSpriteCacheKey();

    @OriginalMember(owner = "client!es", name = "m", descriptor = "I")
    public final int languageId;

    @OriginalMember(owner = "client!es", name = "w", descriptor = "Lclient!ul;")
    public final ModeGame game;

    @OriginalMember(owner = "client!es", name = "l", descriptor = "Lclient!sb;")
    public final js5 meshes;

    @OriginalMember(owner = "client!es", name = "q", descriptor = "Lclient!bo;")
    public final ParamTypeList paramsTL;

    @OriginalMember(owner = "client!es", name = "x", descriptor = "Lclient!sb;")
    public final js5 configClient;

    @OriginalMember(owner = "client!es", name = "i", descriptor = "Z")
    public boolean allowMembers;

    @OriginalMember(owner = "client!es", name = "n", descriptor = "I")
    public final int num;

    @OriginalMember(owner = "client!es", name = "g", descriptor = "[Ljava/lang/String;")
    public final String[] defaultOps;

    @OriginalMember(owner = "client!es", name = "y", descriptor = "[Ljava/lang/String;")
    public final String[] defaultIops;

    @OriginalMember(owner = "client!es", name = "<init>", descriptor = "(Lclient!ul;IZLclient!bo;Lclient!sb;Lclient!sb;)V")
    public ObjTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) boolean allowMembers, @OriginalArg(3) ParamTypeList paramsTL, @OriginalArg(4) js5 configClient, @OriginalArg(5) js5 meshes) {
        this.languageId = languageId;
        this.game = game;
        this.meshes = meshes;
        this.paramsTL = paramsTL;
        this.configClient = configClient;
        this.allowMembers = allowMembers;

        if (this.configClient == null) {
            this.num = 0;
        } else {
            @Pc(54) int lastGroup = this.configClient.groupSize() - 1;
            this.num = this.configClient.fileLimit(lastGroup) + (lastGroup * 256);
        }

        if (this.game == ModeGame.RUNESCAPE) {
            this.defaultOps = new String[]{
                /* 0 */ null,
                /* 1 */ null,
                /* 2 */ LocalisedText.TAKE.localise(this.languageId),
                /* 3 */ null,
                /* 4 */ null,
                /* 5 */ LocalisedText.EXAMINE.localise(this.languageId)
            };
        } else {
            this.defaultOps = new String[]{
                /* 0 */ null,
                /* 1 */ null,
                /* 2 */ LocalisedText.TAKE.localise(this.languageId),
                /* 3 */ null,
                /* 4 */ null,
                /* 5 */ null
            };
        }

        this.defaultIops = new String[]{
            /* 0 */ null,
            /* 1 */ null,
            /* 2 */ null,
            /* 3 */ null,
            /* 4 */ LocalisedText.DROP.localise(this.languageId)
        };
    }

    @OriginalMember(owner = "client!es", name = "b", descriptor = "(B)V")
    public void cacheReset() {
        @Pc(6) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }

        lock = this.modelCache;
        synchronized (this.modelCache) {
            this.modelCache.reset();
        }

        @Pc(44) KeyedReferenceCache spriteLock = this.spriteCache;
        synchronized (this.spriteCache) {
            this.spriteCache.reset();
        }
    }

    @OriginalMember(owner = "client!es", name = "a", descriptor = "(ILclient!ha;Lclient!ha;Lclient!ju;ZIIZILclient!da;II)Lclient!st;")
    public Sprite sprite(@OriginalArg(0) int outline, @OriginalArg(1) Toolkit scratchToolkit, @OriginalArg(2) Toolkit realToolkit, @OriginalArg(3) PlayerModel playerModel, @OriginalArg(4) boolean temporary, @OriginalArg(5) int graphicShadow, @OriginalArg(6) int invCount, @OriginalArg(7) boolean zoomOut, @OriginalArg(8) int objNumMode, @OriginalArg(9) Font font, @OriginalArg(10) int objId) {
        @Pc(24) Sprite cachedSprite = this.getCachedSprite(playerModel, realToolkit, objNumMode, objId, outline, invCount, graphicShadow);
        if (cachedSprite != null) {
            return cachedSprite;
        }

        @Pc(34) ObjType type = this.list(objId);
        if (invCount > 1 && type.countobj != null) {
            @Pc(44) int stackId = -1;
            for (@Pc(46) int i = 0; i < 10; i++) {
                if (type.countco[i] <= invCount && type.countco[i] != 0) {
                    stackId = type.countobj[i];
                }
            }

            if (stackId != -1) {
                type = this.list(stackId);
            }
        }

        @Pc(101) int[] image = type.sprite(objNumMode, realToolkit, invCount, graphicShadow, zoomOut, playerModel, scratchToolkit, font, outline);
        if (image == null) {
            return null;
        }

        @Pc(119) Sprite sprite;
        if (temporary) {
            sprite = scratchToolkit.createSprite(36, 36, 32, image);
        } else {
            sprite = realToolkit.createSprite(36, 36, 32, image);
        }

        if (!temporary) {
            @Pc(136) ObjSpriteCacheKey cacheKey = new ObjSpriteCacheKey();
            cacheKey.objNumMode = objNumMode;
            cacheKey.objWearCol = playerModel != null;
            cacheKey.toolkitIndex = realToolkit.index;
            cacheKey.invCount = invCount;
            cacheKey.outline = outline;
            cacheKey.objId = objId;
            cacheKey.graphicShadow = graphicShadow;
            this.spriteCache.put(sprite, cacheKey);
        }

        return sprite;
    }

    @OriginalMember(owner = "client!es", name = "c", descriptor = "(II)V")
    public void cacheClean(int maxAge) {
        @Pc(14) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(maxAge);
        }

        lock = this.modelCache;
        synchronized (this.modelCache) {
            this.modelCache.clean(maxAge);
        }

        @Pc(48) KeyedReferenceCache spriteLock = this.spriteCache;
        synchronized (this.spriteCache) {
            this.spriteCache.clean(maxAge);
        }
    }

    @OriginalMember(owner = "client!es", name = "a", descriptor = "(B)V")
    public void cacheRemoveSoftReferences() {
        @Pc(6) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }

        lock = this.modelCache;
        synchronized (this.modelCache) {
            this.modelCache.removeSoftReferences();
        }

        @Pc(44) KeyedReferenceCache spriteLock = this.spriteCache;
        synchronized (this.spriteCache) {
            this.spriteCache.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!es", name = "b", descriptor = "(II)V")
    public void setFeatureMask(@OriginalArg(0) int featureMask) {
        this.featureMask = featureMask;

        @Pc(17) ReferenceCache lock = this.modelCache;
        synchronized (this.modelCache) {
            this.modelCache.reset();
        }
    }

    @OriginalMember(owner = "client!es", name = "a", descriptor = "(I)V")
    public void modelCacheReset() {
        @Pc(2) ReferenceCache lock = this.modelCache;
        synchronized (this.modelCache) {
            this.modelCache.reset();
        }
    }

    @OriginalMember(owner = "client!es", name = "a", descriptor = "(Lclient!ju;BLclient!ha;IIIII)Lclient!st;")
    public Sprite getCachedSprite(@OriginalArg(0) PlayerModel playerModel, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) int objNumMode, @OriginalArg(4) int objId, @OriginalArg(5) int outline, @OriginalArg(6) int invCount, @OriginalArg(7) int graphicShadow) {
        this.spriteCacheKey.invCount = invCount;
        this.spriteCacheKey.toolkitIndex = toolkit.index;
        this.spriteCacheKey.objNumMode = objNumMode;
        this.spriteCacheKey.graphicShadow = graphicShadow;
        this.spriteCacheKey.objWearCol = playerModel != null;
        this.spriteCacheKey.outline = outline;
        this.spriteCacheKey.objId = objId;
        return (Sprite) this.spriteCache.get(this.spriteCacheKey);
    }

    @OriginalMember(owner = "client!es", name = "c", descriptor = "(I)V")
    public void spriteCacheReset() {
        @Pc(6) KeyedReferenceCache lock = this.spriteCache;
        synchronized (this.spriteCache) {
            this.spriteCache.reset();
        }
    }

    @OriginalMember(owner = "client!es", name = "a", descriptor = "(BZ)V")
    public void setAllowMembers(@OriginalArg(1) boolean allowMembers) {
        if (this.allowMembers != allowMembers) {
            this.allowMembers = allowMembers;
            this.cacheReset();
        }
    }

    @OriginalMember(owner = "client!es", name = "a", descriptor = "(II)Lclient!vfa;")
    public ObjType list(@OriginalArg(0) int id) {
        @Pc(14) ReferenceCache lookupLock = this.recentUse;
        @Pc(26) ObjType type;

        synchronized (this.recentUse) {
            type = (ObjType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(40) js5 configLock = this.configClient;
        @Pc(53) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(fileId(id), groupId(id));
        }

        type = new ObjType();
        type.myid = id;
        type.myList = this;
        type.op = this.defaultOps.clone();
        type.iop = this.defaultIops.clone();
        if (data != null) {
            type.decode(new Packet(data));
        }
        type.postDecode();

        if (type.certtemplate != -1) {
            type.genCert(this.list(type.certtemplate), this.list(type.certlink));
        }

        if (type.lenttemplate != -1) {
            type.genLent(this.list(type.lentlink), this.list(type.lenttemplate));
        }

        if (type.boughttemplate != -1) {
            type.genBought(this.list(type.boughttemplate), this.list(type.boughtlink));
        }

        if (!this.allowMembers && type.members) {
            type.name = LocalisedText.MEMBERS_OBJECT.localise(this.languageId);
            type.op = this.defaultOps;
            type.iop = this.defaultIops;
            type.quests = null;
            type.team = 0;
            type.stockmarket = false;

            if (type.params != null) {
                @Pc(195) boolean disable = false;

                for (@Pc(200) Node node = type.params.first(); node != null; node = type.params.next()) {
                    @Pc(209) ParamType param = this.paramsTL.list((int) node.key);

                    if (param.autodisable) {
                        node.unlink();
                    } else {
                        disable = true;
                    }
                }

                if (!disable) {
                    type.params = null;
                }
            }
        }

        @Pc(238) ReferenceCache storeLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }
}

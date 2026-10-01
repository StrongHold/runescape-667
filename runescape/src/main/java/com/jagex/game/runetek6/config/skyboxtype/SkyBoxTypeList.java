package com.jagex.game.runetek6.config.skyboxtype;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereType;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereTypeList;
import com.jagex.graphics.skybox.SkyBox;
import com.jagex.graphics.skybox.SkyBoxSphere;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qk")
public final class SkyBoxTypeList {

    private static final int DEFAULT_CACHE_SIZE = 16;

    @OriginalMember(owner = "client!ke", name = "k", descriptor = "Lclient!qk;")
    public static SkyBoxTypeList instance;

    @OriginalMember(owner = "client!qk", name = "c", descriptor = "Lclient!dla;")
    public final ReferenceCache recentUse = new ReferenceCache(DEFAULT_CACHE_SIZE);

    public final ModeGame game;

    public final int languageId;

    @OriginalMember(owner = "client!qk", name = "j", descriptor = "Lclient!sb;")
    public final js5 configClient;

    public final int num;

    @OriginalMember(owner = "client!qk", name = "<init>", descriptor = "(Lclient!ul;ILclient!sb;)V")
    public SkyBoxTypeList(@OriginalArg(0) ModeGame game, @OriginalArg(1) int languageId, @OriginalArg(2) js5 configClient) {
        this.game = game;
        this.languageId = languageId;
        this.configClient = configClient;
        this.num = this.configClient.fileLimit(Js5ConfigGroup.SKYBOXTYPE);
    }

    @OriginalMember(owner = "client!qk", name = "b", descriptor = "(II)V")
    public void cacheClean(@OriginalArg(0) int maxAge) {
        @Pc(13) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.clean(maxAge);
        }
    }

    @OriginalMember(owner = "client!qk", name = "a", descriptor = "(I)V")
    public void cacheRemoveSoftReferences() {
        @Pc(10) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!qk", name = "a", descriptor = "(II)Lclient!ema;")
    public SkyBoxType list(@OriginalArg(0) int id) {
        @Pc(6) ReferenceCache lookupLock = this.recentUse;
        @Pc(22) SkyBoxType type;
        synchronized (this.recentUse) {
            type = (SkyBoxType) this.recentUse.get(id);
        }
        if (type != null) {
            return type;
        }

        @Pc(36) js5 configLock = this.configClient;
        @Pc(45) byte[] data;
        synchronized (this.configClient) {
            data = this.configClient.getfile(id, 29);
        }

        type = new SkyBoxType();
        if (data != null) {
            type.decode(new Packet(data));
        }

        @Pc(69) ReferenceCache storeLock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.put(type, id);
            return type;
        }
    }

    @OriginalMember(owner = "client!qk", name = "c", descriptor = "(I)V")
    public void cacheReset() {
        @Pc(2) ReferenceCache lock = this.recentUse;
        synchronized (this.recentUse) {
            this.recentUse.reset();
        }
    }

    @OriginalMember(owner = "client!qk", name = "a", descriptor = "(Lclient!dg;IIIIZ)Lclient!gm;")
    public SkyBox skyBox(@OriginalArg(0) SkyBoxSphereTypeList typeList, @OriginalArg(1) int sphereOffsetZ, @OriginalArg(2) int id, @OriginalArg(3) int sphereOffsetY, @OriginalArg(4) int sphereOffsetX) {
        @Pc(13) SkyBoxSphere[] spheres = null;
        @Pc(18) SkyBoxType type = this.list(id);

        if (type.sphereIds != null) {
            spheres = new SkyBoxSphere[type.sphereIds.length];

            for (@Pc(28) int i = 0; i < spheres.length; i++) {
                @Pc(37) SkyBoxSphereType sphereType = typeList.list(type.sphereIds[i]);
                spheres[i] = new SkyBoxSphere(sphereType.renderType, sphereType.contentId, sphereType.x, sphereType.y, sphereType.z, sphereType.size, sphereType.colour, sphereType.infinite, sphereType.rotateX, sphereType.rotateY, sphereType.rotateZ);
            }
        }

        return new SkyBox(type.texture, spheres, type.lightSphereIndex, sphereOffsetX, sphereOffsetY, sphereOffsetZ, type.tileMode, type.meshId);
    }
}

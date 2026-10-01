package com.jagex.game.runetek6.config.idktype;

import com.jagex.core.io.Packet;
import com.jagex.graphics.Mesh;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pka")
public final class IDKType {

    @OriginalMember(owner = "client!pka", name = "f", descriptor = "[S")
    public short[] recol_s;

    @OriginalMember(owner = "client!pka", name = "i", descriptor = "[S")
    public short[] retex_d;

    @OriginalMember(owner = "client!pka", name = "j", descriptor = "[S")
    public short[] retex_s;

    @OriginalMember(owner = "client!pka", name = "n", descriptor = "[I")
    public int[] meshes;

    @OriginalMember(owner = "client!pka", name = "e", descriptor = "[S")
    public short[] recol_d;

    @OriginalMember(owner = "client!pka", name = "a", descriptor = "Lclient!kr;")
    public IDKTypeList typeList;

    @OriginalMember(owner = "client!pka", name = "c", descriptor = "[I")
    public final int[] headMeshes = new int[]{-1, -1, -1, -1, -1};

    @OriginalMember(owner = "client!pka", name = "a", descriptor = "(BILclient!ge;)V")
    public void decode(@OriginalArg(1) int code, @OriginalArg(2) Packet packet) {
        if (code == 1) {
            packet.g1();
        } else if (code == 2) {
            @Pc(59) int count = packet.g1();
            this.meshes = new int[count];

            for (@Pc(69) int i = 0; i < count; i++) {
                this.meshes[i] = packet.g2();
            }
        } else if (code == 3) {
            /* empty */
        } else if (code == 40) {
            @Pc(59) int count = packet.g1();
            this.recol_d = new short[count];
            this.recol_s = new short[count];

            for (@Pc(69) int i = 0; i < count; i++) {
                this.recol_s[i] = (short) packet.g2();
                this.recol_d[i] = (short) packet.g2();
            }
        } else if (code == 41) {
            @Pc(59) int count = packet.g1();
            this.retex_d = new short[count];
            this.retex_s = new short[count];

            for (@Pc(69) int i = 0; i < count; i++) {
                this.retex_s[i] = (short) packet.g2();
                this.retex_d[i] = (short) packet.g2();
            }
        } else if (code >= 60 && code < 70) {
            this.headMeshes[code - 60] = packet.g2();
        }
    }

    @OriginalMember(owner = "client!pka", name = "a", descriptor = "(Lclient!ge;B)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(9) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(code, packet);
        }
    }

    @OriginalMember(owner = "client!pka", name = "a", descriptor = "(B)Lclient!dv;")
    public Mesh headModel() {
        @Pc(8) Mesh[] meshes = new Mesh[5];
        @Pc(10) int count = 0;

        @Pc(22) js5 lock = this.typeList.meshes;
        synchronized (this.typeList.meshes) {
            for (@Pc(26) int i = 0; i < 5; i++) {
                if (this.headMeshes[i] != -1) {
                    meshes[count++] = Mesh.load(this.headMeshes[i], this.typeList.meshes);
                }
            }
        }

        for (@Pc(66) int i = 0; i < 5; i++) {
            if (meshes[i] != null && meshes[i].version < 13) {
                meshes[i].upscale();
            }
        }

        @Pc(102) Mesh mesh = new Mesh(meshes, count);
        if (this.recol_s != null) {
            for (@Pc(26) int i = 0; i < this.recol_s.length; i++) {
                mesh.recolour(this.recol_s[i], this.recol_d[i]);
            }
        }
        if (this.retex_s != null) {
            for (@Pc(26) int i = 0; i < this.retex_s.length; i++) {
                mesh.retexture(this.retex_s[i], this.retex_d[i]);
            }
        }

        return mesh;
    }

    @OriginalMember(owner = "client!pka", name = "a", descriptor = "(Z)Z")
    public boolean isHeadLoaded() {
        @Pc(7) boolean loaded = true;
        @Pc(11) js5 lock = this.typeList.meshes;
        synchronized (this.typeList.meshes) {
            for (@Pc(15) int i = 0; i < 5; i++) {
                if (this.headMeshes[i] != -1 && !this.typeList.meshes.requestdownload(0, this.headMeshes[i])) {
                    loaded = false;
                }
            }
            return loaded;
        }
    }

    @OriginalMember(owner = "client!pka", name = "a", descriptor = "(I)Z")
    public boolean isModelLoaded() {
        if (this.meshes == null) {
            return true;
        }

        @Pc(11) boolean loaded = true;
        @Pc(15) js5 lock = this.typeList.meshes;
        synchronized (this.typeList.meshes) {
            for (@Pc(19) int i = 0; i < this.meshes.length; i++) {
                if (!this.typeList.meshes.requestdownload(0, this.meshes[i])) {
                    loaded = false;
                }
            }
            return loaded;
        }
    }

    @OriginalMember(owner = "client!pka", name = "b", descriptor = "(I)Lclient!dv;")
    public Mesh model() {
        if (this.meshes == null) {
            return null;
        }

        @Pc(14) Mesh[] parts = new Mesh[this.meshes.length];
        @Pc(18) js5 lock = this.typeList.meshes;
        synchronized (typeList.meshes) {
            for (@Pc(22) int i = 0; i < this.meshes.length; i++) {
                parts[i] = Mesh.load(this.meshes[i], this.typeList.meshes);
            }
        }

        for (@Pc(56) int i = 0; i < this.meshes.length; i++) {
            if (parts[i].version < 13) {
                parts[i].upscale();
            }
        }

        @Pc(93) Mesh mesh;
        if (parts.length == 1) {
            mesh = parts[0];
        } else {
            mesh = new Mesh(parts, parts.length);
        }

        if (mesh == null) {
            return null;
        }

        if (this.recol_s != null) {
            for (@Pc(22) int i = 0; i < this.recol_s.length; i++) {
                mesh.recolour(this.recol_s[i], this.recol_d[i]);
            }
        }
        if (this.retex_s != null) {
            for (@Pc(22) int i = 0; i < this.retex_s.length; i++) {
                mesh.retexture(this.retex_s[i], this.retex_d[i]);
            }
        }

        return mesh;
    }
}

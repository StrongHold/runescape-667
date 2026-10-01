package com.jagex.game.runetek6.config.spotanimationtype;

import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.game.Animator;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!lia")
public final class SpotAnimationType {

    @OriginalMember(owner = "client!lia", name = "m", descriptor = "I")
    public int id;

    @OriginalMember(owner = "client!lia", name = "s", descriptor = "I")
    public int model;

    @OriginalMember(owner = "client!lia", name = "l", descriptor = "[S")
    public short[] recol_d;

    @OriginalMember(owner = "client!lia", name = "d", descriptor = "[S")
    public short[] recol_s;

    @OriginalMember(owner = "client!lia", name = "y", descriptor = "[S")
    public short[] retex_s;

    @OriginalMember(owner = "client!lia", name = "e", descriptor = "Lclient!fh;")
    public SpotAnimationTypeList myList;

    @OriginalMember(owner = "client!lia", name = "c", descriptor = "[S")
    public short[] retex_d;

    @OriginalMember(owner = "client!lia", name = "g", descriptor = "I")
    public int ambient = 0;

    @OriginalMember(owner = "client!lia", name = "q", descriptor = "B")
    public byte hillType = 0;

    @OriginalMember(owner = "client!lia", name = "t", descriptor = "I")
    public int rotation = 0;

    @OriginalMember(owner = "client!lia", name = "z", descriptor = "I")
    public int seq = -1;

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "I")
    public int contrast = 0;

    @OriginalMember(owner = "client!lia", name = "v", descriptor = "I")
    public int scaleXZ = 128;

    @OriginalMember(owner = "client!lia", name = "f", descriptor = "I")
    public int scaleY = 128;

    @OriginalMember(owner = "client!lia", name = "k", descriptor = "I")
    public int hillValue = -1;

    @OriginalMember(owner = "client!lia", name = "h", descriptor = "Z")
    public boolean loopSeq = false;

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "(Lclient!ge;Z)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(12) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(code, packet);
        }
    }

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "(BZILclient!gu;Lclient!s;BILclient!s;IILclient!ha;)Lclient!ka;")
    public Model model(@OriginalArg(0) byte slot, @OriginalArg(1) boolean hillChange, @OriginalArg(2) int z, @OriginalArg(3) Animator animator, @OriginalArg(4) Ground ceiling, @OriginalArg(6) int x, @OriginalArg(7) Ground floor, @OriginalArg(8) int functionMask, @OriginalArg(9) int y, @OriginalArg(10) Toolkit toolkit) {
        @Pc(13) boolean hillChanged = hillChange & this.hillType != 0;

        @Pc(15) int newFunctionMask = functionMask;
        if (animator != null) {
            newFunctionMask = functionMask | animator.functionMask();
        }
        if (hillChanged) {
            newFunctionMask |= this.hillType == 3 ? 7 : 2;
        }
        if (this.scaleY != 128) {
            newFunctionMask |= 0x2;
        }
        if (this.scaleXZ != 128 || this.rotation != 0) {
            newFunctionMask |= 0x5;
        }

        @Pc(65) ReferenceCache lookupLock = this.myList.modelCache;
        @Pc(85) Model model;
        synchronized (this.myList.modelCache) {
            model = (Model) this.myList.modelCache.get(this.id |= toolkit.index << 29);
        }

        if (model == null || toolkit.compareFunctionMasks(model.ua(), newFunctionMask) != 0) {
            if (model != null) {
                newFunctionMask = toolkit.combineFunctionMasks(newFunctionMask, model.ua());
            }

            @Pc(121) int innerFunctionMask = newFunctionMask;
            if (this.recol_s != null) {
                innerFunctionMask = newFunctionMask | 0x4000;
            }
            if (this.retex_s != null) {
                innerFunctionMask |= 0x8000;
            }

            @Pc(144) Mesh mesh = Mesh.load(this.model, this.myList.models);
            if (mesh == null) {
                return null;
            }
            if (mesh.version < 13) {
                mesh.upscale();
            }

            model = toolkit.createModel(mesh, innerFunctionMask, this.myList.featureMask, this.ambient + 64, this.contrast + 850);

            if (this.recol_s != null) {
                for (@Pc(180) int i = 0; i < this.recol_s.length; i++) {
                    model.ia(this.recol_s[i], this.recol_d[i]);
                }
            }
            if (this.retex_s != null) {
                for (@Pc(180) int i = 0; i < this.retex_s.length; i++) {
                    model.aa(this.retex_s[i], this.retex_d[i]);
                }
            }

            model.s(newFunctionMask);

            @Pc(232) ReferenceCache storeLock = this.myList.modelCache;
            synchronized (this.myList.modelCache) {
                this.myList.modelCache.put(model, this.id |= toolkit.index << 29);
            }
        }

        @Pc(263) Model result = model.copy(slot, newFunctionMask, true);
        if (animator != null) {
            animator.animate(result, 0);
        }

        if (this.scaleXZ != 128 || this.scaleY != 128) {
            result.O(this.scaleXZ, this.scaleY, this.scaleXZ);
        }

        if (this.rotation != 0) {
            if (this.rotation == 90) {
                result.a(0x1000);
            }
            if (this.rotation == 180) {
                result.a(0x2000);
            }
            if (this.rotation == 270) {
                result.a(0x3000);
            }
        }

        if (hillChanged) {
            result.p(this.hillType, this.hillValue, floor, ceiling, x, y, z);
        }

        result.s(functionMask);
        return result;
    }

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "(ILclient!ge;I)V")
    public void decode(@OriginalArg(0) int code, @OriginalArg(1) Packet packet) {
        if (code == 1) {
            this.model = packet.g2();
        } else if (code == 2) {
            this.seq = packet.g2();
        } else if (code == 4) {
            this.scaleXZ = packet.g2();
        } else if (code == 5) {
            this.scaleY = packet.g2();
        } else if (code == 6) {
            this.rotation = packet.g2();
        } else if (code == 7) {
            this.ambient = packet.g1();
        } else if (code == 8) {
            this.contrast = packet.g1();
        } else if (code == 9) {
            this.hillType = 3;
            this.hillValue = 8224;
        } else if (code == 10) {
            this.loopSeq = true;
        } else if (code == 11) {
            this.hillType = 1;
        } else if (code == 12) {
            this.hillType = 4;
        } else if (code == 13) {
            this.hillType = 5;
        } else if (code == 14) {
            this.hillType = 2;
            this.hillValue = packet.g1() * 256;
        } else if (code == 15) {
            this.hillType = 3;
            this.hillValue = packet.g2();
        } else if (code == 16) {
            this.hillType = 3;
            this.hillValue = packet.g4();
        } else if (code == 40) {
            @Pc(132) int count = packet.g1();
            this.recol_d = new short[count];
            this.recol_s = new short[count];
            for (@Pc(142) int i = 0; i < count; i++) {
                this.recol_s[i] = (short) packet.g2();
                this.recol_d[i] = (short) packet.g2();
            }
        } else if (code == 41) {
            @Pc(132) int count = packet.g1();
            this.retex_d = new short[count];
            this.retex_s = new short[count];
            for (@Pc(142) int i = 0; i < count; i++) {
                this.retex_s[i] = (short) packet.g2();
                this.retex_d[i] = (short) packet.g2();
            }
        }
    }

    @OriginalMember(owner = "client!lia", name = "a", descriptor = "(Lclient!gu;BIBLclient!ha;)Lclient!ka;")
    public Model model(@OriginalArg(0) Animator animator, @OriginalArg(1) byte slot, @OriginalArg(2) int functionMask, @OriginalArg(4) Toolkit toolkit) {
        return this.model(slot, false, 0, animator, null, 0, null, functionMask, 0, toolkit);
    }
}

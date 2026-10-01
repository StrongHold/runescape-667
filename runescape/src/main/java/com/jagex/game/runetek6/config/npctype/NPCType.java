package com.jagex.game.runetek6.config.npctype;

import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.StringNode;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.vartype.VarDomain;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import com.jagex.math.IntMath;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!o")
public final class NPCType {

    private static final int INVISIBLE_ID = -1;

    @OriginalMember(owner = "client!rga", name = "g", descriptor = "[S")
    public static short[] clientpalette = new short[256];

    @OriginalMember(owner = "client!o", name = "tb", descriptor = "Lclient!av;")
    public IterableHashTable params;

    @OriginalMember(owner = "client!o", name = "gb", descriptor = "[B")
    public byte[] recol_d_palette;

    @OriginalMember(owner = "client!o", name = "I", descriptor = "[S")
    public short[] recol_s;

    @OriginalMember(owner = "client!o", name = "B", descriptor = "[I")
    public int[] multinpcs;

    @OriginalMember(owner = "client!o", name = "e", descriptor = "[S")
    public short[] recol_d;

    @OriginalMember(owner = "client!o", name = "P", descriptor = "[I")
    public int[] quests;

    @OriginalMember(owner = "client!o", name = "b", descriptor = "[Ljava/lang/String;")
    public String[] op;

    @OriginalMember(owner = "client!o", name = "Bb", descriptor = "B")
    public byte colourHue;

    @OriginalMember(owner = "client!o", name = "a", descriptor = "[S")
    public short[] retex_s;

    @OriginalMember(owner = "client!o", name = "s", descriptor = "[[I")
    public int[][] translations;

    @OriginalMember(owner = "client!o", name = "ob", descriptor = "[I")
    public int[] headModels;

    @OriginalMember(owner = "client!o", name = "k", descriptor = "B")
    public byte colourSaturation;

    @OriginalMember(owner = "client!o", name = "Q", descriptor = "B")
    public byte colourLightness;

    @OriginalMember(owner = "client!o", name = "A", descriptor = "Lclient!ql;")
    public NPCTypeList typeList;

    @OriginalMember(owner = "client!o", name = "Z", descriptor = "[I")
    public int[] models;

    @OriginalMember(owner = "client!o", name = "N", descriptor = "I")
    public int id;

    @OriginalMember(owner = "client!o", name = "X", descriptor = "[S")
    public short[] retex_d;

    @OriginalMember(owner = "client!o", name = "g", descriptor = "Z")
    public boolean vorbis;

    @OriginalMember(owner = "client!o", name = "ub", descriptor = "I")
    public int mobilisingArmiesIcon = -1;

    @OriginalMember(owner = "client!o", name = "eb", descriptor = "I")
    public int walkSound = -1;

    @OriginalMember(owner = "client!o", name = "Ab", descriptor = "I")
    public int size = 1;

    @OriginalMember(owner = "client!o", name = "L", descriptor = "I")
    public int quickPick = -1;

    @OriginalMember(owner = "client!o", name = "d", descriptor = "Z")
    public boolean crawl = true;

    @OriginalMember(owner = "client!o", name = "l", descriptor = "I")
    public int height = -1;

    @OriginalMember(owner = "client!o", name = "hb", descriptor = "Z")
    public boolean isFollower = false;

    @OriginalMember(owner = "client!o", name = "Eb", descriptor = "I")
    public int scaleV = 128;

    @OriginalMember(owner = "client!o", name = "nb", descriptor = "I")
    public int crawlSound = -1;

    @OriginalMember(owner = "client!o", name = "r", descriptor = "Z")
    public boolean renderLowPriority = false;

    @OriginalMember(owner = "client!o", name = "m", descriptor = "I")
    public int combatLevel = -1;

    @OriginalMember(owner = "client!o", name = "H", descriptor = "I")
    public int timerbarSprite = -1;

    @OriginalMember(owner = "client!o", name = "xb", descriptor = "Z")
    public boolean renderHighPriority = false;

    @OriginalMember(owner = "client!o", name = "w", descriptor = "B")
    public byte lowPriorityAttackOps = -1;

    @OriginalMember(owner = "client!o", name = "n", descriptor = "B")
    public byte movementCapabilities = 0;

    @OriginalMember(owner = "client!o", name = "Y", descriptor = "B")
    public byte colourScale = 0;

    @OriginalMember(owner = "client!o", name = "u", descriptor = "I")
    public int readySound = -1;

    @OriginalMember(owner = "client!o", name = "K", descriptor = "I")
    public int soundRateMin = 256;

    @OriginalMember(owner = "client!o", name = "E", descriptor = "Z")
    public boolean interactive = true;

    @OriginalMember(owner = "client!o", name = "i", descriptor = "Ljava/lang/String;")
    public String name = "null";

    @OriginalMember(owner = "client!o", name = "U", descriptor = "I")
    public int scaleH = 128;

    @OriginalMember(owner = "client!o", name = "h", descriptor = "I")
    public int runSound = -1;

    @OriginalMember(owner = "client!o", name = "db", descriptor = "I")
    public int headIcon = -1;

    @OriginalMember(owner = "client!o", name = "Db", descriptor = "I")
    public int soundRateMax = 256;

    @OriginalMember(owner = "client!o", name = "v", descriptor = "I")
    public int attackCursor = -1;

    @OriginalMember(owner = "client!o", name = "z", descriptor = "I")
    public int cursor1 = -1;

    @OriginalMember(owner = "client!o", name = "f", descriptor = "I")
    public int pickSizeShift = 0;

    @OriginalMember(owner = "client!o", name = "y", descriptor = "I")
    public int basId = -1;

    @OriginalMember(owner = "client!o", name = "zb", descriptor = "I")
    public int soundRangeMin = 0;

    @OriginalMember(owner = "client!o", name = "t", descriptor = "S")
    public short shadowOuterColour = 0;

    @OriginalMember(owner = "client!o", name = "Fb", descriptor = "I")
    public int soundRangeMax = 0;

    @OriginalMember(owner = "client!o", name = "D", descriptor = "B")
    public byte shadowInnerAlpha = -16;

    @OriginalMember(owner = "client!o", name = "cb", descriptor = "I")
    public int ambient = 0;

    @OriginalMember(owner = "client!o", name = "ab", descriptor = "I")
    public int mapElement = -1;

    @OriginalMember(owner = "client!o", name = "V", descriptor = "I")
    public int cursor2Op = -1;

    @OriginalMember(owner = "client!o", name = "wb", descriptor = "Z")
    public boolean hasShadow = true;

    @OriginalMember(owner = "client!o", name = "pb", descriptor = "S")
    public short shadowInnerColour = 0;

    @OriginalMember(owner = "client!o", name = "o", descriptor = "I")
    public int diffusion = 0;

    @OriginalMember(owner = "client!o", name = "R", descriptor = "I")
    public int soundVolume = 255;

    @OriginalMember(owner = "client!o", name = "c", descriptor = "I")
    public int multinpcVarbit = -1;

    @OriginalMember(owner = "client!o", name = "q", descriptor = "B")
    public byte shadowOuterAlpha = -96;

    @OriginalMember(owner = "client!o", name = "qb", descriptor = "Z")
    public boolean displayOnMiniMap = true;

    @OriginalMember(owner = "client!o", name = "W", descriptor = "I")
    public int healthBarSprite = -1;

    @OriginalMember(owner = "client!o", name = "yb", descriptor = "B")
    public byte spawnDirection = 4;

    @OriginalMember(owner = "client!o", name = "S", descriptor = "I")
    public int multinpcVarp = -1;

    @OriginalMember(owner = "client!o", name = "T", descriptor = "I")
    public int cursor1Op = -1;

    @OriginalMember(owner = "client!o", name = "lb", descriptor = "I")
    public int yawSpeed = 32;

    @OriginalMember(owner = "client!o", name = "C", descriptor = "I")
    public int cursor2 = -1;

    @OriginalMember(owner = "client!o", name = "b", descriptor = "(B)V")
    public void postDecode() {
        if (this.models == null) {
            this.models = new int[0];
        }

        if (this.lowPriorityAttackOps == -1) {
            if (this.typeList.game == ModeGame.RUNESCAPE) {
                this.lowPriorityAttackOps = 1;
            } else {
                this.lowPriorityAttackOps = 0;
            }
        }
    }

    @OriginalMember(owner = "client!o", name = "c", descriptor = "(B)Z")
    public boolean hasSounds() {
        if (this.multinpcs == null) {
            return this.readySound != -1 || this.walkSound != -1 || this.runSound != -1;
        }

        for (@Pc(35) int i = 0; i < this.multinpcs.length; i++) {
            if (this.multinpcs[i] != -1) {
                @Pc(62) NPCType type = this.typeList.list(this.multinpcs[i]);
                if (type.readySound != -1 || type.walkSound != -1 || type.runSound != -1) {
                    return true;
                }
            }
        }

        return false;
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(ILclient!uk;)Lclient!o;")
    public NPCType getMultiNPC(@OriginalArg(1) VarDomain domain) {
        @Pc(11) int index = -1;
        if (this.multinpcVarbit != -1) {
            index = domain.getVarBitValue(this.multinpcVarbit);
        } else if (this.multinpcVarp != -1) {
            index = domain.getVarValueInt(this.multinpcVarp);
        }

        if (index >= 0 && index < this.multinpcs.length - 1 && this.multinpcs[index] != INVISIBLE_ID) {
            return this.typeList.list(this.multinpcs[index]);
        } else {
            @Pc(66) int id = this.multinpcs[this.multinpcs.length - 1];
            return id == INVISIBLE_ID ? null : this.typeList.list(id);
        }
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(BLclient!ge;)V")
    public void decode(@OriginalArg(1) Packet packet) {
        while (true) {
            @Pc(15) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(code, packet);
        }
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(ILclient!ge;B)V")
    public void decode(@OriginalArg(0) int code, @OriginalArg(1) Packet packet) {
        if (code == 1) {
            @Pc(12) int count = packet.g1();
            this.models = new int[count];

            for (@Pc(18) int i = 0; i < count; i++) {
                this.models[i] = packet.g2();

                if (this.models[i] == 65535) {
                    this.models[i] = -1;
                }
            }
        } else if (code == 2) {
            this.name = packet.gjstr();
        } else if (code == 12) {
            this.size = packet.g1();
        } else if (code >= 30 && code < 35) {
            this.op[code - 30] = packet.gjstr();
        } else if (code == 40) {
            @Pc(12) int count = packet.g1();
            this.recol_d = new short[count];
            this.recol_s = new short[count];

            for (@Pc(18) int i = 0; i < count; i++) {
                this.recol_s[i] = (short) packet.g2();
                this.recol_d[i] = (short) packet.g2();
            }
        } else if (code == 41) {
            @Pc(12) int count = packet.g1();
            this.retex_d = new short[count];
            this.retex_s = new short[count];

            for (@Pc(18) int i = 0; i < count; i++) {
                this.retex_s[i] = (short) packet.g2();
                this.retex_d[i] = (short) packet.g2();
            }
        } else if (code == 42) {
            @Pc(12) int count = packet.g1();
            this.recol_d_palette = new byte[count];

            for (@Pc(18) int i = 0; i < count; i++) {
                this.recol_d_palette[i] = packet.g1b();
            }
        } else if (code == 60) {
            @Pc(12) int count = packet.g1();
            this.headModels = new int[count];

            for (@Pc(18) int i = 0; i < count; i++) {
                this.headModels[i] = packet.g2();
            }
        } else if (code == 93) {
            this.displayOnMiniMap = false;
        } else if (code == 95) {
            this.combatLevel = packet.g2();
        } else if (code == 97) {
            this.scaleH = packet.g2();
        } else if (code == 98) {
            this.scaleV = packet.g2();
        } else if (code == 99) {
            this.renderHighPriority = true;
        } else if (code == 100) {
            this.ambient = packet.g1b();
        } else if (code == 101) {
            this.diffusion = packet.g1b() * 5;
        } else if (code == 102) {
            this.headIcon = packet.g2();
        } else if (code == 103) {
            this.yawSpeed = packet.g2();
        } else if (code == 106 || code == 118) {
            this.multinpcVarbit = packet.g2();
            if (this.multinpcVarbit == 65535) {
                this.multinpcVarbit = -1;
            }

            this.multinpcVarp = packet.g2();
            if (this.multinpcVarp == 65535) {
                this.multinpcVarp = -1;
            }

            @Pc(12) int defaultId = INVISIBLE_ID;
            if (code == 118) {
                defaultId = packet.g2();
                if (defaultId == 65535) {
                    defaultId = INVISIBLE_ID;
                }
            }

            @Pc(18) int count = packet.g1();
            this.multinpcs = new int[count + 2];
            for (@Pc(306) int i = 0; i <= count; i++) {
                this.multinpcs[i] = packet.g2();

                if (this.multinpcs[i] == 65535) {
                    this.multinpcs[i] = INVISIBLE_ID;
                }
            }

            this.multinpcs[count + 1] = defaultId;
        } else if (code == 107) {
            this.interactive = false;
        } else if (code == 109) {
            this.crawl = false;
        } else if (code == 111) {
            this.hasShadow = false;
        } else if (code == 113) {
            this.shadowOuterColour = (short) packet.g2();
            this.shadowInnerColour = (short) packet.g2();
        } else if (code == 114) {
            this.shadowOuterAlpha = packet.g1b();
            this.shadowInnerAlpha = packet.g1b();
        } else if (code == 119) {
            this.movementCapabilities = packet.g1b();
        } else if (code == 121) {
            this.translations = new int[this.models.length][];
            @Pc(12) int count = packet.g1();

            for (@Pc(18) int i = 0; i < count; i++) {
                @Pc(306) int model = packet.g1();
                @Pc(914) int[] translations = this.translations[model] = new int[3];
                translations[0] = packet.g1b();
                translations[1] = packet.g1b();
                translations[2] = packet.g1b();
            }
        } else if (code == 122) {
            this.healthBarSprite = packet.g2();
        } else if (code == 123) {
            this.height = packet.g2();
        } else if (code == 125) {
            this.spawnDirection = packet.g1b();
        } else if (code == 127) {
            this.basId = packet.g2();
        } else if (code == 128) {
            packet.g1(); // TODO: 865 client has this as MoveSpeed?
        } else if (code == 134) {
            this.readySound = packet.g2();
            if (this.readySound == 65535) {
                this.readySound = -1;
            }

            this.crawlSound = packet.g2();
            if (this.crawlSound == 65535) {
                this.crawlSound = -1;
            }

            this.walkSound = packet.g2();
            if (this.walkSound == 65535) {
                this.walkSound = -1;
            }

            this.runSound = packet.g2();
            if (this.runSound == 65535) {
                this.runSound = -1;
            }

            this.soundRangeMax = packet.g1();
        } else if (code == 135) {
            this.cursor1Op = packet.g1();
            this.cursor1 = packet.g2();
        } else if (code == 136) {
            this.cursor2Op = packet.g1();
            this.cursor2 = packet.g2();
        } else if (code == 137) {
            this.attackCursor = packet.g2();
        } else if (code == 138) {
            this.mobilisingArmiesIcon = packet.g2();
        } else if (code == 139) {
            this.timerbarSprite = packet.g2();
        } else if (code == 140) {
            this.soundVolume = packet.g1();
        } else if (code == 141) {
            this.isFollower = true;
        } else if (code == 142) {
            this.mapElement = packet.g2();
        } else if (code == 143) {
            this.renderLowPriority = true;
        } else if (code >= 150 && code < 155) {
            this.op[code - 150] = packet.gjstr();

            if (!this.typeList.allowMembers) {
                this.op[code - 150] = null;
            }
        } else if (code == 155) {
            this.colourHue = packet.g1b();
            this.colourSaturation = packet.g1b();
            this.colourLightness = packet.g1b();
            this.colourScale = packet.g1b();
        } else if (code == 158) {
            this.lowPriorityAttackOps = 1;
        } else if (code == 159) {
            this.lowPriorityAttackOps = 0;
        } else if (code == 160) {
            @Pc(12) int count = packet.g1();
            this.quests = new int[count];

            for (@Pc(18) int i = 0; i < count; i++) {
                this.quests[i] = packet.g2();
            }
        } else if (code == 162) {
            this.vorbis = true;
        } else if (code == 163) {
            this.quickPick = packet.g1();
        } else if (code == 164) {
            this.soundRateMin = packet.g2();
            this.soundRateMax = packet.g2();
        } else if (code == 165) {
            this.pickSizeShift = packet.g1();
        } else if (code == 168) {
            this.soundRangeMin = packet.g1();
        } else if (code == 249) {
            @Pc(12) int count = packet.g1();

            if (this.params == null) {
                @Pc(18) int size = IntMath.nextPow2(count);
                this.params = new IterableHashTable(size);
            }

            for (@Pc(18) int i = 0; i < count; i++) {
                @Pc(761) boolean string = packet.g1() == 1;
                @Pc(765) int id = packet.g3();

                @Pc(774) Node param;
                if (string) {
                    param = new StringNode(packet.gjstr());
                } else {
                    param = new IntNode(packet.g4());
                }

                this.params.put(id, param);
            }
        }
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(BLjava/lang/String;I)Ljava/lang/String;")
    public String param(@OriginalArg(1) String dflt, @OriginalArg(2) int id) {
        if (this.params == null) {
            return dflt;
        } else {
            @Pc(23) StringNode param = (StringNode) this.params.get(id);
            return param == null ? dflt : param.value;
        }
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(ILclient!gu;Lclient!vk;ILclient!ha;Lclient!uk;)Lclient!ka;")
    public Model headModel(@OriginalArg(0) int functionMask, @OriginalArg(1) Animator animator, @OriginalArg(2) NPCTypeCustomisation customisation, @OriginalArg(4) Toolkit toolkit, @OriginalArg(5) VarDomain varDomain) {
        if (this.multinpcs != null) {
            @Pc(15) NPCType multiNpc = this.getMultiNPC(varDomain);
            return multiNpc == null ? null : multiNpc.headModel(functionMask, animator, customisation, toolkit, varDomain);
        }

        if (this.headModels == null && (customisation == null || customisation.remodel_d == null)) {
            return null;
        }

        @Pc(54) int newFunctionMask = functionMask;
        if (animator != null) {
            newFunctionMask = animator.functionMask() | 0x800;
        }

        @Pc(71) long key = (toolkit.index << 16) | this.id;
        if (customisation != null) {
            key |= customisation.id << 24;
        }

        @Pc(84) ReferenceCache lookupLock = this.typeList.headModels;
        @Pc(94) Model model;
        synchronized (this.typeList.headModels) {
            model = (Model) this.typeList.headModels.get(key);
        }

        if (model == null || (model.ua() & newFunctionMask) != newFunctionMask) {
            if (model != null) {
                newFunctionMask |= model.ua();
            }

            @Pc(125) int innerFunctionMask = newFunctionMask;
            if (this.recol_s != null) {
                innerFunctionMask = newFunctionMask | 0x4000;
            }
            if (this.retex_s != null) {
                innerFunctionMask |= 0x8000;
            }
            if (this.colourScale != 0) {
                innerFunctionMask |= 0x80000;
            }

            @Pc(163) int[] modelIds = customisation == null || customisation.remodel_d == null ? this.headModels : customisation.remodel_d;
            @Pc(165) boolean notReady = false;
            @Pc(169) js5 meshLock = this.typeList.meshes;
            synchronized (this.typeList.meshes) {
                for (@Pc(173) int i = 0; i < modelIds.length; i++) {
                    if (!this.typeList.meshes.requestdownload(0, modelIds[i])) {
                        notReady = true;
                    }
                }
            }

            if (notReady) {
                return null;
            }

            @Pc(215) Mesh[] msehes = new Mesh[modelIds.length];
            @Pc(219) js5 loadLock = this.typeList.meshes;
            synchronized (this.typeList.meshes) {
                @Pc(223) int i = 0;
                while (true) {
                    if (i >= modelIds.length) {
                        break;
                    }
                    msehes[i] = Mesh.load(modelIds[i], this.typeList.meshes);
                    i++;
                }
            }

            for (@Pc(258) int i = 0; i < modelIds.length; i++) {
                if (msehes[i] != null && msehes[i].version < 13) {
                    msehes[i].upscale();
                }
            }

            @Pc(304) Mesh mesh;
            if (msehes.length == 1) {
                mesh = msehes[0];
            } else {
                mesh = new Mesh(msehes, msehes.length);
            }

            model = toolkit.createModel(mesh, innerFunctionMask, this.typeList.featureMask, 64, 768);
            if (this.recol_s != null) {
                @Pc(334) short[] recol_d;
                if (customisation == null || customisation.recol_d == null) {
                    recol_d = this.recol_d;
                } else {
                    recol_d = customisation.recol_d;
                }

                for (@Pc(340) int i = 0; i < this.recol_s.length; i++) {
                    if (this.recol_d_palette != null && i < this.recol_d_palette.length) {
                        model.ia(this.recol_s[i], clientpalette[this.recol_d_palette[i] & 0xFF]);
                    } else {
                        model.ia(this.recol_s[i], recol_d[i]);
                    }
                }
            }

            if (this.retex_s != null) {
                @Pc(334) short[] retex_d;
                if (customisation == null || customisation.retex_d == null) {
                    retex_d = this.retex_d;
                } else {
                    retex_d = customisation.retex_d;
                }

                for (@Pc(340) int i = 0; i < this.retex_s.length; i++) {
                    model.aa(this.retex_s[i], retex_d[i]);
                }
            }

            if (this.colourScale != 0) {
                model.adjustColours(this.colourHue, this.colourSaturation, this.colourLightness, this.colourScale & 0xFF);
            }

            model.s(newFunctionMask);

            @Pc(469) ReferenceCache storeLock = this.typeList.headModels;
            synchronized (this.typeList.headModels) {
                this.typeList.headModels.put(model, key);
            }
        }

        if (animator != null) {
            model = model.copy((byte) 1, newFunctionMask, true);
            animator.animate(model, 0);
        }

        model.s(functionMask);
        return model;
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(Lclient!uk;Lclient!ha;Lclient!qp;BLclient!gu;I[ILclient!vk;Lclient!gu;I[Lclient!gu;)Lclient!ka;")
    public Model getModel(@OriginalArg(0) VarDomain varDomain, @OriginalArg(1) Toolkit toolkit, @OriginalArg(2) BASTypeList basTypeList, @OriginalArg(4) Animator actionAnimator, @OriginalArg(5) int yaw, @OriginalArg(6) int[] wornRotation, @OriginalArg(7) NPCTypeCustomisation customisation, @OriginalArg(8) Animator movementAnimator, @OriginalArg(9) int functionMask, @OriginalArg(10) Animator[] animators) {
        if (this.multinpcs != null) {
            @Pc(11) NPCType type = this.getMultiNPC(varDomain);
            return type == null ? null : type.getModel(varDomain, toolkit, basTypeList, actionAnimator, yaw, wornRotation, customisation, movementAnimator, functionMask, animators);
        }

        @Pc(32) int newFunctionMask = functionMask;
        if (this.scaleV != 128) {
            newFunctionMask = functionMask | 0x2;
        }
        if (this.scaleH != 128) {
            newFunctionMask |= 0x5;
        }

        @Pc(52) boolean animated = false;
        @Pc(60) int animCount = animators == null ? 0 : animators.length;
        for (@Pc(62) int i = 0; i < animCount; i++) {
            if (animators[i] != null) {
                newFunctionMask |= animators[i].functionMask();
                animated = true;
            }
        }

        if (actionAnimator != null) {
            animated = true;
            newFunctionMask |= actionAnimator.functionMask();
        }

        if (movementAnimator != null) {
            newFunctionMask |= movementAnimator.functionMask();
            animated = true;
        }

        @Pc(116) long key = this.id | toolkit.index << 16;
        if (customisation != null) {
            key |= customisation.id << 24;
        }

        @Pc(129) ReferenceCache lookupLock = this.typeList.models;
        @Pc(139) Model model;
        synchronized (this.typeList.models) {
            model = (Model) this.typeList.models.get(key);
        }

        @Pc(147) BASType basType = null;
        if (this.basId != -1) {
            basType = basTypeList.list(this.basId);
        }

        if (model == null || newFunctionMask != (model.ua() & newFunctionMask)) {
            if (model != null) {
                newFunctionMask |= model.ua();
            }

            @Pc(178) int innerFunctionMask = newFunctionMask;
            if (this.recol_s != null) {
                innerFunctionMask = newFunctionMask | 0x4000;
            }
            if (this.retex_s != null) {
                innerFunctionMask |= 0x8000;
            }
            if (this.colourScale != 0) {
                innerFunctionMask |= 0x80000;
            }

            @Pc(216) int[] modelIds = (customisation != null && customisation.remodel_d != null) ? customisation.remodel_d : this.models;
            @Pc(218) boolean notReady = false;
            @Pc(222) js5 meshLock = this.typeList.meshes;
            synchronized (this.typeList.meshes) {
                for (@Pc(226) int i = 0; i < modelIds.length; i++) {
                    if (modelIds[i] != -1 && !this.typeList.meshes.requestdownload(0, modelIds[i])) {
                        notReady = true;
                    }
                }
            }

            if (notReady) {
                return null;
            }

            @Pc(267) Mesh[] meshes = new Mesh[modelIds.length];
            for (@Pc(226) int i = 0; i < modelIds.length; i++) {
                if (modelIds[i] != -1) {
                    @Pc(280) js5 loadLock = this.typeList.meshes;
                    synchronized (this.typeList.meshes) {
                        meshes[i] = Mesh.load(modelIds[i], this.typeList.meshes);
                    }

                    if (meshes[i] != null) {
                        if (meshes[i].version < 13) {
                            meshes[i].upscale();
                        }
                        if (this.translations != null && this.translations[i] != null) {
                            meshes[i].translate(this.translations[i][0], this.translations[i][1], this.translations[i][2]);
                        }
                    }
                }
            }

            if (basType != null && basType.wornTransformations != null) {
                for (@Pc(377) int i = 0; i < basType.wornTransformations.length; i++) {
                    if (meshes.length > i && meshes[i] != null) {
                        @Pc(396) int translateX = 0;
                        @Pc(398) int translateY = 0;
                        @Pc(400) int translateZ = 0;
                        @Pc(402) int rotateX = 0;
                        @Pc(404) int rotateY = 0;
                        @Pc(406) int rotateZ = 0;

                        if (basType.wornTransformations[i] != null) {
                            translateX = basType.wornTransformations[i][0];
                            translateY = basType.wornTransformations[i][1];
                            translateZ = basType.wornTransformations[i][2];
                            rotateX = basType.wornTransformations[i][3] << 3;
                            rotateY = basType.wornTransformations[i][4] << 3;
                            rotateZ = basType.wornTransformations[i][5] << 3;
                        }

                        if (rotateX != 0 || rotateY != 0 || rotateZ != 0) {
                            meshes[i].rotate(rotateZ, rotateX, rotateY);
                        }

                        if (translateX != 0 || translateY != 0 || translateZ != 0) {
                            meshes[i].translate(translateX, translateY, translateZ);
                        }
                    }
                }
            }

            @Pc(529) Mesh mesh;
            if (meshes.length == 1) {
                mesh = meshes[0];
            } else {
                mesh = new Mesh(meshes, meshes.length);
            }

            model = toolkit.createModel(mesh, innerFunctionMask, this.typeList.featureMask, this.ambient + 64, this.diffusion + 850);

            if (this.recol_s != null) {
                @Pc(568) short[] recol_d;
                if (customisation == null || customisation.recol_d == null) {
                    recol_d = this.recol_d;
                } else {
                    recol_d = customisation.recol_d;
                }

                for (@Pc(396) int i = 0; i < this.recol_s.length; i++) {
                    if (this.recol_d_palette != null && i < this.recol_d_palette.length) {
                        model.ia(this.recol_s[i], clientpalette[this.recol_d_palette[i] & 0xFF]);
                    } else {
                        model.ia(this.recol_s[i], recol_d[i]);
                    }
                }
            }

            if (this.retex_s != null) {
                @Pc(568) short[] retex_d;
                if (customisation == null || customisation.retex_d == null) {
                    retex_d = this.retex_d;
                } else {
                    retex_d = customisation.retex_d;
                }

                for (@Pc(396) int i = 0; i < this.retex_s.length; i++) {
                    model.aa(this.retex_s[i], retex_d[i]);
                }
            }

            if (this.colourScale != 0) {
                model.adjustColours(this.colourHue, this.colourSaturation, this.colourLightness, this.colourScale & 0xFF);
            }

            model.s(newFunctionMask);
            @Pc(685) ReferenceCache storeLock = this.typeList.models;
            synchronized (this.typeList.models) {
                this.typeList.models.put(model, key);
            }
        }

        @Pc(706) Model result = model.copy((byte) 4, newFunctionMask, true);

        @Pc(716) boolean rotated = false;
        if (wornRotation != null) {
            for (@Pc(720) int i = 0; i < 12; i++) {
                if (wornRotation[i] != -1) {
                    rotated = true;
                }
            }
        }

        if (!animated && !rotated) {
            return result;
        }

        @Pc(747) Matrix[] matrices = null;
        if (basType != null) {
            matrices = basType.transformMatrices(toolkit);
        }

        if (rotated && matrices != null) {
            for (@Pc(762) int i = 0; i < 12; i++) {
                if (matrices[i] != null) {
                    result.transform(matrices[i], 0x1 << i, true);
                }
            }
        }

        @Pc(762) int animPtr = 0;
        @Pc(226) int modelFlag = 1;
        while (animPtr < animCount) {
            if (animators[animPtr] != null) {
                animators[animPtr].animatePartial(modelFlag, result);
            }
            animPtr++;
            modelFlag <<= 0x1;
        }

        if (rotated) {
            for (@Pc(377) int i = 0; i < 12; i++) {
                if (wornRotation[i] != -1) {
                    @Pc(396) int angle = wornRotation[i] - yaw;
                    angle &= 0x3FFF;
                    @Pc(838) Matrix matrix = toolkit.createMatrix();
                    matrix.rotate(angle);
                    result.transform(matrix, 0x1 << i, false);
                }
            }
        }

        if (rotated && matrices != null) {
            for (@Pc(377) int i = 0; i < 12; i++) {
                if (matrices[i] != null) {
                    result.transform(matrices[i], 0x1 << i, false);
                }
            }
        }

        if (actionAnimator != null && movementAnimator != null) {
            Animator.blend(actionAnimator, result, movementAnimator);
        } else if (actionAnimator != null) {
            actionAnimator.animate(result, 0);
        } else if (movementAnimator != null) {
            movementAnimator.animate(result, 0);
        }

        if (this.scaleH != 128 || this.scaleV != 128) {
            result.O(this.scaleH, this.scaleV, this.scaleH);
        }

        result.s(functionMask);
        return result;
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(III)I")
    public int param(@OriginalArg(1) int id, @OriginalArg(2) int dflt) {
        if (this.params == null) {
            return dflt;
        } else {
            @Pc(25) IntNode param = (IntNode) this.params.get(id);
            return param == null ? dflt : param.value;
        }
    }

    @OriginalMember(owner = "client!o", name = "a", descriptor = "(BLclient!uk;)Z")
    public boolean isVisible(@OriginalArg(1) VarDomain varDomain) {
        if (this.multinpcs == null) {
            return true;
        }

        @Pc(19) int index = -1;
        if (this.multinpcVarbit != -1) {
            index = varDomain.getVarBitValue(this.multinpcVarbit);
        } else if (this.multinpcVarp != -1) {
            index = varDomain.getVarValueInt(this.multinpcVarp);
        }

        if (index >= 0 && index < this.multinpcs.length - 1 && this.multinpcs[index] != INVISIBLE_ID) {
            return true;
        } else {
            @Pc(71) int lastId = this.multinpcs[this.multinpcs.length - 1];
            return lastId != INVISIBLE_ID;
        }
    }
}

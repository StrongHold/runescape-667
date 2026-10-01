package com.jagex.game.runetek6.config.objtype;

import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.StringNode;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.graphics.Font;
import com.jagex.game.PlayerModel;
import com.jagex.game.Animator;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Sprite;
import com.jagex.game.LocalisedText;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.Model;
import com.jagex.math.IntMath;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!vfa")
public final class ObjType {

    private static final int MAX_OP_COUNT = 5;
    private static final int MAX_IOP_COUNT = 5;

    @OriginalMember(owner = "client!hh", name = "b", descriptor = "[S")
    public static short[] clientpalette = new short[256];

    @OriginalMember(owner = "client!vfa", name = "X", descriptor = "I")
    public static int shadowCount;

    @OriginalMember(owner = "client!vfa", name = "Cb", descriptor = "[S")
    public short[] retex_s;

    @OriginalMember(owner = "client!vfa", name = "t", descriptor = "[S")
    public short[] recol_s;

    @OriginalMember(owner = "client!vfa", name = "I", descriptor = "[S")
    public short[] recol_d;

    @OriginalMember(owner = "client!vfa", name = "j", descriptor = "Lclient!es;")
    public ObjTypeList myList;

    @OriginalMember(owner = "client!vfa", name = "Hb", descriptor = "[I")
    public int[] countco;

    @OriginalMember(owner = "client!vfa", name = "nb", descriptor = "Lclient!av;")
    public IterableHashTable params;

    @OriginalMember(owner = "client!vfa", name = "n", descriptor = "[B")
    public byte[] recol_d_palette;

    @OriginalMember(owner = "client!vfa", name = "f", descriptor = "I")
    public int mesh;

    @OriginalMember(owner = "client!vfa", name = "ib", descriptor = "[Ljava/lang/String;")
    public String[] iop;

    @OriginalMember(owner = "client!vfa", name = "vb", descriptor = "[S")
    public short[] retex_d;

    @OriginalMember(owner = "client!vfa", name = "O", descriptor = "I")
    public int myid;

    @OriginalMember(owner = "client!vfa", name = "db", descriptor = "[I")
    public int[] countobj;

    @OriginalMember(owner = "client!vfa", name = "G", descriptor = "[Ljava/lang/String;")
    public String[] op;

    @OriginalMember(owner = "client!vfa", name = "H", descriptor = "[I")
    public int[] quests;

    @OriginalMember(owner = "client!vfa", name = "v", descriptor = "I")
    public int cursor2 = -1;

    @OriginalMember(owner = "client!vfa", name = "cb", descriptor = "I")
    public int cursor2op = -1;

    @OriginalMember(owner = "client!vfa", name = "W", descriptor = "I")
    public int manwear = -1;

    @OriginalMember(owner = "client!vfa", name = "J", descriptor = "I")
    public int cursor2iop = -1;

    @OriginalMember(owner = "client!vfa", name = "wb", descriptor = "I")
    public int manwear2 = -1;

    @OriginalMember(owner = "client!vfa", name = "U", descriptor = "I")
    public int zan2d = 0;

    @OriginalMember(owner = "client!vfa", name = "N", descriptor = "I")
    public int lenttemplate = -1;

    @OriginalMember(owner = "client!vfa", name = "ob", descriptor = "I")
    public int icursor2 = -1;

    @OriginalMember(owner = "client!vfa", name = "Q", descriptor = "I")
    public int boughtlink = -1;

    @OriginalMember(owner = "client!vfa", name = "y", descriptor = "I")
    public int manwear3 = -1;

    @OriginalMember(owner = "client!vfa", name = "M", descriptor = "I")
    public int ambient = 0;

    @OriginalMember(owner = "client!vfa", name = "Ab", descriptor = "I")
    public int contrast = 0;

    @OriginalMember(owner = "client!vfa", name = "w", descriptor = "I")
    public int womanhead = -1;

    @OriginalMember(owner = "client!vfa", name = "F", descriptor = "I")
    public int zoom2d = 2000;

    @OriginalMember(owner = "client!vfa", name = "A", descriptor = "I")
    public int multistacksize = -1;

    @OriginalMember(owner = "client!vfa", name = "D", descriptor = "I")
    public int team = 0;

    @OriginalMember(owner = "client!vfa", name = "z", descriptor = "Z")
    public boolean members = false;

    @OriginalMember(owner = "client!vfa", name = "Db", descriptor = "I")
    public int resizey = 128;

    @OriginalMember(owner = "client!vfa", name = "yb", descriptor = "I")
    public int xof2d = 0;

    @OriginalMember(owner = "client!vfa", name = "xb", descriptor = "Ljava/lang/String;")
    public String name = "null";

    @OriginalMember(owner = "client!vfa", name = "ub", descriptor = "I")
    public int icursor1 = -1;

    @OriginalMember(owner = "client!vfa", name = "Z", descriptor = "I")
    public int resizex = 128;

    @OriginalMember(owner = "client!vfa", name = "Y", descriptor = "I")
    public int manhead2 = -1;

    @OriginalMember(owner = "client!vfa", name = "jb", descriptor = "I")
    public int womanhead2 = -1;

    @OriginalMember(owner = "client!vfa", name = "x", descriptor = "I")
    public int cursor1op = -1;

    @OriginalMember(owner = "client!vfa", name = "s", descriptor = "I")
    public int yan2d = 0;

    @OriginalMember(owner = "client!vfa", name = "mb", descriptor = "I")
    public int manwearzoff = 0;

    @OriginalMember(owner = "client!vfa", name = "R", descriptor = "I")
    public int cursor1 = -1;

    @OriginalMember(owner = "client!vfa", name = "k", descriptor = "I")
    public int cursor1iop = -1;

    @OriginalMember(owner = "client!vfa", name = "b", descriptor = "I")
    public int womanwearyoff = 0;

    @OriginalMember(owner = "client!vfa", name = "Eb", descriptor = "I")
    public int womanwear2 = -1;

    @OriginalMember(owner = "client!vfa", name = "P", descriptor = "I")
    public int yof2d = 0;

    @OriginalMember(owner = "client!vfa", name = "zb", descriptor = "I")
    public int womanwearxoff = 0;

    @OriginalMember(owner = "client!vfa", name = "tb", descriptor = "I")
    public int manhead = -1;

    @OriginalMember(owner = "client!vfa", name = "fb", descriptor = "I")
    public int picksizeshift = 0;

    @OriginalMember(owner = "client!vfa", name = "T", descriptor = "I")
    public int stackable = ObjStackability.SOMETIMES;

    @OriginalMember(owner = "client!vfa", name = "lb", descriptor = "I")
    public int resizez = 128;

    @OriginalMember(owner = "client!vfa", name = "pb", descriptor = "I")
    public int womanwear3 = -1;

    @OriginalMember(owner = "client!vfa", name = "K", descriptor = "I")
    public int certtemplate = -1;

    @OriginalMember(owner = "client!vfa", name = "rb", descriptor = "I")
    public int certlink = -1;

    @OriginalMember(owner = "client!vfa", name = "r", descriptor = "I")
    public int cost = 1;

    @OriginalMember(owner = "client!vfa", name = "p", descriptor = "I")
    public int dummyitem = 0;

    @OriginalMember(owner = "client!vfa", name = "l", descriptor = "I")
    public int boughttemplate = -1;

    @OriginalMember(owner = "client!vfa", name = "qb", descriptor = "I")
    public int xan2d = 0;

    @OriginalMember(owner = "client!vfa", name = "Bb", descriptor = "I")
    public int lentlink = -1;

    @OriginalMember(owner = "client!vfa", name = "S", descriptor = "I")
    public int womanwear = -1;

    @OriginalMember(owner = "client!vfa", name = "q", descriptor = "I")
    public int manwearyoff = 0;

    @OriginalMember(owner = "client!vfa", name = "eb", descriptor = "I")
    public int womanwearzoff = 0;

    @OriginalMember(owner = "client!vfa", name = "gb", descriptor = "I")
    public int manwearxoff = 0;

    @OriginalMember(owner = "client!vfa", name = "L", descriptor = "Z")
    public boolean stockmarket = false;

    @OriginalMember(owner = "client!vfa", name = "b", descriptor = "(II)Lclient!vfa;")
    public ObjType getStacked(@OriginalArg(0) int count) {
        if (this.countobj != null && count > 1) {
            @Pc(23) int id = -1;
            for (@Pc(25) int i = 0; i < 10; i++) {
                if (this.countco[i] <= count && this.countco[i] != 0) {
                    id = this.countobj[i];
                }
            }
            if (id != -1) {
                return this.myList.list(id);
            }
        }
        return this;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(Lclient!ge;B)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(15) int code = packet.g1();
            if (code == 0) {
                return;
            }
            this.decode(packet, code);
        }
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(BLclient!vfa;Lclient!vfa;)V")
    public void genLent(@OriginalArg(1) ObjType original, @OriginalArg(2) ObjType template) {
        this.recol_d = original.recol_d;
        this.manhead = original.manhead;
        this.manhead2 = original.manhead2;
        this.manwear3 = original.manwear3;
        this.team = original.team;
        this.params = original.params;
        this.members = original.members;
        this.retex_d = original.retex_d;
        this.manwearyoff = original.manwearyoff;
        this.womanwear2 = original.womanwear2;
        this.zan2d = template.zan2d;
        this.op = original.op;
        this.womanhead2 = original.womanhead2;
        this.recol_d_palette = original.recol_d_palette;
        this.womanwearyoff = original.womanwearyoff;
        this.xan2d = template.xan2d;
        this.womanwear = original.womanwear;
        this.mesh = template.mesh;
        this.iop = new String[MAX_IOP_COUNT];
        this.yan2d = template.yan2d;
        this.yof2d = template.yof2d;
        this.retex_s = original.retex_s;
        this.womanwearxoff = original.womanwearxoff;
        this.manwear2 = original.manwear2;
        this.cost = 0;
        this.manwearzoff = original.manwearzoff;
        this.recol_s = original.recol_s;
        this.xof2d = template.xof2d;
        this.womanwearzoff = original.womanwearzoff;
        this.womanhead = original.womanhead;
        this.zoom2d = template.zoom2d;
        this.name = original.name;
        this.womanwear3 = original.womanwear3;
        this.manwear = original.manwear;
        this.manwearxoff = original.manwearxoff;

        if (original.iop != null) {
            for (@Pc(155) int i = 0; i < 4; i++) {
                this.iop[i] = original.iop[i];
            }
        }

        this.iop[4] = LocalisedText.LENT_ITEM_RETURN.localise(this.myList.languageId);
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(Lclient!vfa;ILclient!vfa;)V")
    public void genCert(@OriginalArg(0) ObjType template, @OriginalArg(2) ObjType original) {
        this.yof2d = template.yof2d;
        this.recol_s = template.recol_s;
        this.cost = original.cost;
        this.name = original.name;
        this.retex_d = template.retex_d;
        this.yan2d = template.yan2d;
        this.zan2d = template.zan2d;
        this.retex_s = template.retex_s;
        this.mesh = template.mesh;
        this.zoom2d = template.zoom2d;
        this.recol_d_palette = template.recol_d_palette;
        this.stackable = 1;
        this.xan2d = template.xan2d;
        this.xof2d = template.xof2d;
        this.members = original.members;
        this.recol_d = template.recol_d;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(III)I")
    public int param(@OriginalArg(0) int id, @OriginalArg(1) int dflt) {
        if (this.params == null) {
            return dflt;
        } else {
            @Pc(19) IntNode param = (IntNode) this.params.get(id);
            return param == null ? dflt : param.value;
        }
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(II[I)[I")
    public int[] colourBorder(@OriginalArg(1) int c, @OriginalArg(2) int[] p) {
        @Pc(8) int[] out = new int[1152];
        @Pc(10) int pos = 0;

        for (@Pc(12) int y = 0; y < 32; y++) {
            for (@Pc(18) int x = 0; x < 36; x++) {
                @Pc(24) int colour = p[pos];
                if (colour == 0) {
                    if (x > 0 && p[pos - 1] != 0) {
                        colour = c;
                    } else if (y > 0 && p[pos - 36] != 0) {
                        colour = c;
                    } else if (x < 35 && p[pos + 1] != 0) {
                        colour = c;
                    } else if (y < 31 && p[pos + 36] != 0) {
                        colour = c;
                    }
                }
                out[pos++] = colour;
            }
        }
        return out;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(Lclient!ge;ZI)V")
    public void decode(@OriginalArg(0) Packet packet, @OriginalArg(2) int code) {
        if (code == 1) {
            this.mesh = packet.g2();
        } else if (code == 2) {
            this.name = packet.gjstr();
        } else if (code == 4) {
            this.zoom2d = packet.g2();
        } else if (code == 5) {
            this.xan2d = packet.g2();
        } else if (code == 6) {
            this.yan2d = packet.g2();
        } else if (code == 7) {
            this.xof2d = packet.g2();
            if (this.xof2d > Short.MAX_VALUE) {
                this.xof2d -= 65536;
            }
        } else if (code == 8) {
            this.yof2d = packet.g2();
            if (this.yof2d > Short.MAX_VALUE) {
                this.yof2d -= 65536;
            }
        } else if (code == 11) {
            this.stackable = 1;
        } else if (code == 12) {
            this.cost = packet.g4();
        } else if (code == 16) {
            this.members = true;
        } else if (code == 18) {
            this.multistacksize = packet.g2();
        } else if (code == 23) {
            this.manwear = packet.g2();
        } else if (code == 24) {
            this.manwear2 = packet.g2();
        } else if (code == 25) {
            this.womanwear = packet.g2();
        } else if (code == 26) {
            this.womanwear2 = packet.g2();
        } else if (code >= 30 && code < 35) {
            this.op[code - 30] = packet.gjstr();
        } else if (code >= 35 && code < 40) {
            this.iop[code - 35] = packet.gjstr();
        } else if (code == 40) {
            @Pc(202) int count = packet.g1();
            this.recol_s = new short[count];
            this.recol_d = new short[count];
            for (@Pc(212) int i = 0; i < count; i++) {
                this.recol_s[i] = (short) packet.g2();
                this.recol_d[i] = (short) packet.g2();
            }
        } else if (code == 41) {
            @Pc(202) int count = packet.g1();
            this.retex_s = new short[count];
            this.retex_d = new short[count];
            for (@Pc(212) int i = 0; i < count; i++) {
                this.retex_s[i] = (short) packet.g2();
                this.retex_d[i] = (short) packet.g2();
            }
        } else if (code == 42) {
            @Pc(202) int count = packet.g1();
            this.recol_d_palette = new byte[count];
            for (@Pc(212) int i = 0; i < count; i++) {
                this.recol_d_palette[i] = packet.g1b();
            }
        } else if (code == 65) {
            this.stockmarket = true;
        } else if (code == 78) {
            this.manwear3 = packet.g2();
        } else if (code == 79) {
            this.womanwear3 = packet.g2();
        } else if (code == 90) {
            this.manhead = packet.g2();
        } else if (code == 91) {
            this.womanhead = packet.g2();
        } else if (code == 92) {
            this.manhead2 = packet.g2();
        } else if (code == 93) {
            this.womanhead2 = packet.g2();
        } else if (code == 95) {
            this.zan2d = packet.g2();
        } else if (code == 96) {
            this.dummyitem = packet.g1();
        } else if (code == 97) {
            this.certlink = packet.g2();
        } else if (code == 98) {
            this.certtemplate = packet.g2();
        } else if (code >= 100 && code < 110) {
            if (this.countobj == null) {
                this.countco = new int[10];
                this.countobj = new int[10];
            }
            this.countobj[code - 100] = packet.g2();
            this.countco[code - 100] = packet.g2();
        } else if (code == 110) {
            this.resizex = packet.g2();
        } else if (code == 111) {
            this.resizey = packet.g2();
        } else if (code == 112) {
            this.resizez = packet.g2();
        } else if (code == 113) {
            this.ambient = packet.g1b();
        } else if (code == 114) {
            this.contrast = packet.g1b() * 5;
        } else if (code == 115) {
            this.team = packet.g1();
        } else if (code == 121) {
            this.lentlink = packet.g2();
        } else if (code == 122) {
            this.lenttemplate = packet.g2();
        } else if (code == 125) {
            this.manwearxoff = packet.g1b() << 2;
            this.manwearyoff = packet.g1b() << 2;
            this.manwearzoff = packet.g1b() << 2;
        } else if (code == 126) {
            this.womanwearxoff = packet.g1b() << 2;
            this.womanwearyoff = packet.g1b() << 2;
            this.womanwearzoff = packet.g1b() << 2;
        } else if (code == 127) {
            this.cursor1op = packet.g1();
            this.cursor1 = packet.g2();
        } else if (code == 128) {
            this.cursor2op = packet.g1();
            this.cursor2 = packet.g2();
        } else if (code == 129) {
            this.cursor1iop = packet.g1();
            this.icursor1 = packet.g2();
        } else if (code == 130) {
            this.cursor2iop = packet.g1();
            this.icursor2 = packet.g2();
        } else if (code == 132) {
            @Pc(202) int count = packet.g1();
            this.quests = new int[count];
            for (@Pc(212) int i = 0; i < count; i++) {
                this.quests[i] = packet.g2();
            }
        } else if (code == 134) {
            this.picksizeshift = packet.g1();
        } else if (code == 139) {
            this.boughtlink = packet.g2();
        } else if (code == 140) {
            this.boughttemplate = packet.g2();
        } else if (code == 249) {
            @Pc(202) int count = packet.g1();

            if (this.params == null) {
                @Pc(212) int size = IntMath.nextPow2(count);
                this.params = new IterableHashTable(size);
            }

            for (@Pc(212) int i = 0; i < count; i++) {
                @Pc(554) boolean string = packet.g1() == 1;
                @Pc(558) int id = packet.g3();

                @Pc(567) Node param;
                if (string) {
                    param = new StringNode(packet.gjstr());
                } else {
                    param = new IntNode(packet.g4());
                }

                this.params.put(id, param);
            }
        }
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(ILclient!ha;IBIZLclient!ju;Lclient!ha;Lclient!da;I)[I")
    public int[] sprite(@OriginalArg(0) int objNumMode, @OriginalArg(1) Toolkit toolkit, @OriginalArg(2) int invCount, @OriginalArg(4) int graphicShadow, @OriginalArg(5) boolean zoomOut, @OriginalArg(6) PlayerModel playerModel, @OriginalArg(7) Toolkit scratchToolkit, @OriginalArg(8) Font font, @OriginalArg(9) int outline) {
        @Pc(14) Mesh mesh = Mesh.load(this.mesh, this.myList.meshes);
        if (mesh == null) {
            return null;
        }

        if (mesh.version < 13) {
            mesh.upscale();
        }

        if (this.recol_s != null) {
            for (@Pc(36) int i = 0; i < this.recol_s.length; i++) {
                if (this.recol_d_palette == null || i >= this.recol_d_palette.length) {
                    mesh.recolour(this.recol_s[i], this.recol_d[i]);
                } else {
                    mesh.recolour(this.recol_s[i], clientpalette[this.recol_d_palette[i] & 0xFF]);
                }
            }
        }

        if (this.retex_s != null) {
            for (@Pc(36) int i = 0; i < this.retex_s.length; i++) {
                mesh.retexture(this.retex_s[i], this.retex_d[i]);
            }
        }

        if (playerModel != null) {
            for (@Pc(36) int i = 0; i < 10; i++) {
                for (@Pc(138) int j = 0; j < PlayerModel.recol_s[j].length; j++) {
                    if (playerModel.clientpalette[j] < PlayerModel.recol_d[j][j].length) {
                        mesh.recolour(PlayerModel.recol_s[j][j], PlayerModel.recol_d[j][j][playerModel.clientpalette[j]]);
                    }
                }
            }
        }

        @Pc(198) short functionMask = 2048;
        @Pc(200) boolean scaled = false;
        if (this.resizex != 128 || this.resizey != 128 || this.resizez != 128) {
            scaled = true;
            functionMask = 2055;
        }

        @Pc(244) Model model = scratchToolkit.createModel(mesh, functionMask, 64, this.ambient + 64, this.contrast + 768);
        if (!model.loadedTextures()) {
            return null;
        }

        if (scaled) {
            model.O(this.resizex, this.resizey, this.resizez);
        }

        @Pc(272) Sprite sprite = null;
        if (this.certtemplate != -1) {
            sprite = this.myList.sprite(1, scratchToolkit, toolkit, playerModel, true, 0, 10, true, ObjNumMode.SHOWCOUNT_NEVER, font, this.certlink);
            if (sprite == null) {
                return null;
            }
        } else if (this.lenttemplate != -1) {
            sprite = this.myList.sprite(outline, scratchToolkit, toolkit, playerModel, true, graphicShadow, invCount, false, ObjNumMode.SHOWCOUNT_NEVER, font, this.lentlink);
            if (sprite == null) {
                return null;
            }
        } else if (this.boughttemplate != -1) {
            sprite = this.myList.sprite(outline, scratchToolkit, toolkit, playerModel, true, graphicShadow, invCount, false, ObjNumMode.SHOWCOUNT_NEVER, font, this.boughtlink);
            if (sprite == null) {
                return null;
            }
        }

        @Pc(363) int zoom;
        if (zoomOut) {
            zoom = (int) ((double) this.zoom2d * 1.5D) << 2;
        } else if (outline == 2) {
            zoom = (int) ((double) this.zoom2d * 1.04D) << 2;
        } else {
            zoom = this.zoom2d << 2;
        }

        scratchToolkit.DA(16, 16, 512, 512);

        @Pc(395) Matrix matrix = scratchToolkit.createMatrix();
        matrix.makeIdentity();
        scratchToolkit.setCamera(matrix);
        scratchToolkit.xa(1.0F);
        scratchToolkit.ZA(0xFFFFFF, 1.0F, 1.0F, -50.0F, -10.0F, -50.0F);

        @Pc(414) Matrix scratch = scratchToolkit.scratchMatrix();
        scratch.makeRotationZ(-this.zan2d << 3);
        scratch.rotateAxisY(this.yan2d << 3);
        scratch.translate(this.xof2d << 2, (Trig1.SIN[this.xan2d << 3] * zoom >> 14) + (this.yof2d << 2) - (model.fa() / 2), (Trig1.COS[this.xan2d << 3] * zoom >> 14) - -(this.yof2d << 2));
        scratch.rotateAxisX(this.xan2d << 3);

        @Pc(480) int zNear = scratchToolkit.i();
        @Pc(483) int zFar = scratchToolkit.XA();
        scratchToolkit.f(50, Integer.MAX_VALUE);
        scratchToolkit.ya();
        scratchToolkit.la();
        scratchToolkit.aa(0, 0, 36, 32, 0, 0);
        model.render(scratch, null, 1);
        scratchToolkit.f(zNear, zFar);

        @Pc(515) int[] image = scratchToolkit.na(0, 0, 36, 32);
        if (outline >= 1) {
            image = this.colourBorder(0xff000002, image);
            if (outline >= 2) {
                image = this.colourBorder(-1, image);
            }
        }

        if (graphicShadow != 0) {
            this.applyShadow(graphicShadow, image);
        }

        scratchToolkit.createSprite(36, 36, 32, image).render(0, 0);

        if (this.certtemplate != -1) {
            sprite.render(0, 0);
        } else if (this.lenttemplate != -1) {
            sprite.render(0, 0);
        } else if (this.boughttemplate != -1) {
            sprite.render(0, 0);
        }

        if (objNumMode == ObjNumMode.SHOWCOUNT_ALWAYS || objNumMode == ObjNumMode.SHOWCOUNT_IFNOT1 && (this.stackable == ObjStackability.ALWAYS || invCount != 1) && invCount != -1) {
            font.render(this.formatAmount(invCount), 0, 9, 0xFF000001, 0xFFFFFF00);
        }

        image = scratchToolkit.na(0, 0, 36, 32);

        for (@Pc(652) int j = 0; j < image.length; j++) {
            if ((image[j] & 0xFFFFFF) == 0) {
                image[j] = 0;
            } else {
                image[j] |= 0xFF000000;
            }
        }

        return image;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(Lclient!bs;ZI)Lclient!dv;")
    public Mesh playerModel(@OriginalArg(0) ObjTypeCustomisation customisation, @OriginalArg(1) boolean female) {
        @Pc(19) int model;
        @Pc(24) int model2;
        @Pc(29) int model3;

        if (female) {
            if (customisation == null || customisation.womanwear == null) {
                model = this.womanwear;
                model2 = this.womanwear2;
                model3 = this.womanwear3;
            } else {
                model = customisation.womanwear[0];
                model2 = customisation.womanwear[1];
                model3 = customisation.womanwear[2];
            }
        } else {
            if (customisation == null || customisation.manwear == null) {
                model = this.manwear;
                model2 = this.manwear2;
                model3 = this.manwear3;
            } else {
                model = customisation.manwear[0];
                model2 = customisation.manwear[1];
                model3 = customisation.manwear[2];
            }
        }

        if (model == -1) {
            return null;
        }

        @Pc(86) Mesh mesh = Mesh.load(model, this.myList.meshes);
        if (mesh == null) {
            return null;
        }

        if (mesh.version < 13) {
            mesh.upscale();
        }

        if (model2 != -1) {
            @Pc(113) Mesh mesh2 = Mesh.load(model2, this.myList.meshes);
            if (mesh2.version < 13) {
                mesh2.upscale();
            }

            if (model3 != -1) {
                @Pc(137) Mesh mesh3 = Mesh.load(model3, this.myList.meshes);
                if (mesh3.version < 13) {
                    mesh3.upscale();
                }

                @Pc(162) Mesh[] meshes = {mesh, mesh2, mesh3};
                mesh = new Mesh(meshes, 3);
            } else {
                @Pc(180) Mesh[] meshes = {mesh, mesh2};
                mesh = new Mesh(meshes, 2);
            }
        }

        if (!female && (this.manwearxoff != 0 || this.manwearyoff != 0 || this.manwearzoff != 0)) {
            mesh.translate(this.manwearxoff, this.manwearyoff, this.manwearzoff);
        }

        if (female && (this.womanwearxoff != 0 || this.womanwearyoff != 0 || this.womanwearzoff != 0)) {
            mesh.translate(this.womanwearxoff, this.womanwearyoff, this.womanwearzoff);
        }

        if (this.recol_s != null) {
            @Pc(269) short[] recol_d;
            if (customisation == null || customisation.recol_d == null) {
                recol_d = this.recol_d;
            } else {
                recol_d = customisation.recol_d;
            }

            for (@Pc(275) int i = 0; i < this.recol_s.length; i++) {
                mesh.recolour(this.recol_s[i], recol_d[i]);
            }
        }

        if (this.retex_s != null) {
            @Pc(269) short[] retex_d;
            if (customisation == null || customisation.retex_d == null) {
                retex_d = this.retex_d;
            } else {
                retex_d = customisation.retex_d;
            }

            for (@Pc(275) int i = 0; i < this.retex_s.length; i++) {
                mesh.retexture(this.retex_s[i], retex_d[i]);
            }
        }

        return mesh;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(Ljava/lang/String;II)Ljava/lang/String;")
    public String param(@OriginalArg(0) String dflt, @OriginalArg(2) int id) {
        if (this.params == null) {
            return dflt;
        } else {
            @Pc(17) StringNode param = (StringNode) this.params.get(id);
            return param == null ? dflt : param.value;
        }
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(ZLclient!bs;I)Lclient!dv;")
    public Mesh headModel(@OriginalArg(0) boolean female, @OriginalArg(1) ObjTypeCustomisation customisation) {
        @Pc(21) int model;
        @Pc(26) int model2;

        if (female) {
            if (customisation == null || customisation.womanhead == null) {
                model = this.womanhead;
                model2 = this.womanhead2;
            } else {
                model = customisation.womanhead[0];
                model2 = customisation.womanhead[1];
            }
        } else {
            if (customisation == null || customisation.manhead == null) {
                model = this.manhead;
                model2 = this.manhead2;
            } else {
                model = customisation.manhead[0];
                model2 = customisation.manhead[1];
            }
        }

        if (model == -1) {
            return null;
        }

        @Pc(84) Mesh mesh = Mesh.load(model, this.myList.meshes);
        if (mesh.version < 13) {
            mesh.upscale();
        }

        if (model2 != -1) {
            @Pc(105) Mesh mesh2 = Mesh.load(model2, this.myList.meshes);
            if (mesh2.version < 13) {
                mesh2.upscale();
            }
            @Pc(128) Mesh[] meshes = {mesh, mesh2};
            mesh = new Mesh(meshes, 2);
        }

        if (this.recol_s != null) {
            @Pc(149) short[] recol_d;
            if (customisation == null || customisation.recol_d == null) {
                recol_d = this.recol_d;
            } else {
                recol_d = customisation.recol_d;
            }

            for (@Pc(156) int i = 0; i < this.recol_s.length; i++) {
                mesh.recolour(this.recol_s[i], recol_d[i]);
            }
        }

        if (this.retex_s != null) {
            @Pc(149) short[] retex_d;
            if (customisation == null || customisation.retex_d == null) {
                retex_d = this.retex_d;
            } else {
                retex_d = customisation.retex_d;
            }

            for (@Pc(156) int i = 0; i < this.retex_s.length; i++) {
                mesh.retexture(this.retex_s[i], retex_d[i]);
            }
        }

        return mesh;
    }

    @OriginalMember(owner = "client!vfa", name = "b", descriptor = "(ZLclient!bs;I)Z")
    public boolean loadedModels(@OriginalArg(0) boolean female, @OriginalArg(1) ObjTypeCustomisation customisation) {
        @Pc(17) int model;
        @Pc(23) int model2;
        @Pc(20) int model3;

        if (female) {
            if (customisation == null || customisation.womanwear == null) {
                model = this.womanwear;
                model3 = this.womanwear3;
                model2 = this.womanwear2;
            } else {
                model = customisation.womanwear[0];
                model2 = customisation.womanwear[1];
                model3 = customisation.womanwear[2];
            }
        } else {
            if (customisation == null || customisation.manwear == null) {
                model3 = this.manwear3;
                model = this.manwear;
                model2 = this.manwear2;
            } else {
                model2 = customisation.manwear[1];
                model3 = customisation.manwear[2];
                model = customisation.manwear[0];
            }
        }

        if (model == -1) {
            return true;
        }

        @Pc(88) boolean loaded = true;
        if (!this.myList.meshes.requestdownload(0, model)) {
            loaded = false;
        }
        if (model2 != -1 && !this.myList.meshes.requestdownload(0, model2)) {
            loaded = false;
        }
        if (model3 != -1 && !this.myList.meshes.requestdownload(0, model3)) {
            loaded = false;
        }
        return loaded;
    }

    @OriginalMember(owner = "client!vfa", name = "c", descriptor = "(II)Ljava/lang/String;")
    public String formatAmount(@OriginalArg(1) int amount) {
        if (amount < 100000) {
            return "<col=ffff00>" + amount + "</col>";
        } else if (amount < 10000000) {
            return "<col=ffffff>" + amount / 1000 + LocalisedText.THOUSAND_SHORT.localise(this.myList.languageId) + "</col>";
        } else {
            return "<col=00ff80>" + amount / 1000000 + LocalisedText.MILLION_SHORT.localise(this.myList.languageId) + "</col>";
        }
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(I[II)V")
    public void applyShadow(@OriginalArg(0) int graphicShadow, @OriginalArg(1) int[] image) {
        for (@Pc(11) int y = 31; y > 0; y--) {
            @Pc(19) int pos = y * 36;
            for (@Pc(21) int x = 35; x > 0; x--) {
                if (image[x + pos] == 0 && image[pos + x - 1 - 36] != 0) {
                    image[x + pos] = graphicShadow;
                }
            }
        }
        shadowCount++;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(Lclient!gu;ILclient!ju;ILclient!ha;I)Lclient!ka;")
    public Model model(@OriginalArg(0) Animator animator, @OriginalArg(1) int functionMask, @OriginalArg(2) PlayerModel playerModel, @OriginalArg(3) int amount, @OriginalArg(4) Toolkit toolkit) {
        if (this.countobj != null && amount > 1) {
            @Pc(17) int id = -1;

            for (@Pc(19) int i = 0; i < 10; i++) {
                if (amount >= this.countco[i] && this.countco[i] != 0) {
                    id = this.countobj[i];
                }
            }

            if (id != -1) {
                return this.myList.list(id).model(animator, functionMask, playerModel, 1, toolkit);
            }
        }

        @Pc(17) int newFunctionMask = functionMask;
        if (animator != null) {
            newFunctionMask = functionMask | animator.functionMask();
        }

        @Pc(87) ReferenceCache lookupLock = this.myList.modelCache;
        @Pc(104) Model model;
        synchronized (this.myList.modelCache) {
            model = (Model) this.myList.modelCache.get(this.myid | toolkit.index << 29);
        }

        if (model == null || toolkit.compareFunctionMasks(model.ua(), newFunctionMask) != 0) {
            if (model != null) {
                newFunctionMask = toolkit.combineFunctionMasks(newFunctionMask, model.ua());
            }

            @Pc(141) int baseFunctionMask = newFunctionMask;
            if (this.retex_s != null) {
                baseFunctionMask = newFunctionMask | 0x8000;
            }
            if (this.recol_s != null || playerModel != null) {
                baseFunctionMask |= 0x4000;
            }
            if (this.resizex != 128) {
                baseFunctionMask |= 0x1;
            }
            if (this.resizex != 128) {
                baseFunctionMask |= 0x2;
            }
            if (this.resizex != 128) {
                baseFunctionMask |= 0x4;
            }

            @Pc(196) Mesh mesh = Mesh.load(this.mesh, this.myList.meshes);
            if (mesh == null) {
                return null;
            }
            if (mesh.version < 13) {
                mesh.upscale();
            }

            model = toolkit.createModel(mesh, baseFunctionMask, this.myList.featureMask, this.ambient + 64, 850 - -this.contrast);

            if (this.resizex != 128 || this.resizey != 128 || this.resizez != 128) {
                model.O(this.resizex, this.resizey, this.resizez);
            }

            if (this.recol_s != null) {
                for (@Pc(265) int i = 0; i < this.recol_s.length; i++) {
                    if (this.recol_d_palette == null || this.recol_d_palette.length <= i) {
                        model.ia(this.recol_s[i], this.recol_d[i]);
                    } else {
                        model.ia(this.recol_s[i], clientpalette[this.recol_d_palette[i] & 0xFF]);
                    }
                }
            }

            if (this.retex_s != null) {
                for (@Pc(265) int i = 0; i < this.retex_s.length; i++) {
                    model.aa(this.retex_s[i], this.retex_d[i]);
                }
            }

            if (playerModel != null) {
                for (@Pc(265) int i = 0; i < 10; i++) {
                    for (@Pc(360) int j = 0; j < PlayerModel.recol_s[i].length; j++) {
                        if (PlayerModel.recol_d[i][j].length > playerModel.clientpalette[i]) {
                            model.ia(PlayerModel.recol_s[i][j], PlayerModel.recol_d[i][j][playerModel.clientpalette[i]]);
                        }
                    }
                }
            }

            model.s(newFunctionMask);
            @Pc(426) ReferenceCache storeLock = this.myList.modelCache;
            synchronized (this.myList.modelCache) {
                this.myList.modelCache.put(model, this.myid | toolkit.index << 29);
            }
        }

        if (animator != null) {
            model = model.copy((byte) 1, newFunctionMask, true);
            animator.animate(model, 0);
        }

        model.s(functionMask);
        return model;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(B)V")
    public void postDecode() {
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(ILclient!bs;Z)Z")
    public boolean loadedHeadModels(@OriginalArg(1) ObjTypeCustomisation customisation, @OriginalArg(2) boolean female) {
        @Pc(19) int model;
        @Pc(22) int model2;

        if (female) {
            if (customisation == null || customisation.womanhead == null) {
                model = this.womanhead;
                model2 = this.womanhead2;
            } else {
                model = customisation.womanhead[0];
                model2 = customisation.womanhead[1];
            }
        } else {
            if (customisation == null || customisation.manhead == null) {
                model = this.manhead;
                model2 = this.manhead2;
            } else {
                model = customisation.manhead[0];
                model2 = customisation.manhead[1];
            }
        }

        if (model == -1) {
            return true;
        }

        @Pc(71) boolean loaded = true;
        if (!this.myList.meshes.requestdownload(0, model)) {
            loaded = false;
        }
        if (model2 != -1 && !this.myList.meshes.requestdownload(0, model2)) {
            loaded = false;
        }
        return loaded;
    }

    @OriginalMember(owner = "client!vfa", name = "a", descriptor = "(Lclient!vfa;BLclient!vfa;)V")
    public void genBought(@OriginalArg(0) ObjType template, @OriginalArg(2) ObjType original) {
        this.cost = 0;
        this.manwear3 = original.manwear3;
        this.stackable = original.stackable;
        this.members = original.members;
        this.recol_d_palette = original.recol_d_palette;
        this.yof2d = template.yof2d;
        this.team = original.team;
        this.manwear2 = original.manwear2;
        this.iop = new String[MAX_IOP_COUNT];
        this.op = original.op;
        this.manwearyoff = original.manwearyoff;
        this.manhead = original.manhead;
        this.womanwearyoff = original.womanwearyoff;
        this.name = original.name;
        this.zoom2d = template.zoom2d;
        this.recol_s = original.recol_s;
        this.womanhead2 = original.womanhead2;
        this.params = original.params;
        this.womanwear = original.womanwear;
        this.xan2d = template.xan2d;
        this.yan2d = template.yan2d;
        this.womanwearxoff = original.womanwearxoff;
        this.manhead2 = original.manhead2;
        this.womanwear3 = original.womanwear3;
        this.retex_d = original.retex_d;
        this.manwearxoff = original.manwearxoff;
        this.womanhead = original.womanhead;
        this.mesh = template.mesh;
        this.recol_d = original.recol_d;
        this.womanwear2 = original.womanwear2;
        this.xof2d = template.xof2d;
        this.zan2d = template.zan2d;
        this.manwear = original.manwear;
        this.womanwearzoff = original.womanwearzoff;
        this.manwearzoff = original.manwearzoff;
        this.retex_s = original.retex_s;

        if (original.iop != null) {
            for (@Pc(161) int i = 0; i < 4; i++) {
                this.iop[i] = original.iop[i];
            }
        }

        this.iop[4] = LocalisedText.BOUGHT_ITEM_DISCARD.localise(this.myList.languageId);
    }
}

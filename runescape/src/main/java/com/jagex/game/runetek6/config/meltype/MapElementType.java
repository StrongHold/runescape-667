package com.jagex.game.runetek6.config.meltype;

import com.jagex.IndexedImage;
import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.StringNode;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.vartype.VarDomain;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.math.IntMath;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!el")
public final class MapElementType {

    @OriginalMember(owner = "client!el", name = "B", descriptor = "I")
    public int landmarkVarEnd;

    @OriginalMember(owner = "client!el", name = "k", descriptor = "Ljava/lang/String;")
    public String opBase;

    @OriginalMember(owner = "client!el", name = "s", descriptor = "[I")
    public int[] landmarkPolygons;

    @OriginalMember(owner = "client!el", name = "z", descriptor = "Lclient!av;")
    public IterableHashTable params;

    @OriginalMember(owner = "client!el", name = "ab", descriptor = "I")
    public int id;

    @OriginalMember(owner = "client!el", name = "o", descriptor = "[B")
    public byte[] landmarkColorIndices;

    @OriginalMember(owner = "client!el", name = "e", descriptor = "I")
    public int textColour;

    @OriginalMember(owner = "client!el", name = "I", descriptor = "[I")
    public int[] landmarkPalette;

    @OriginalMember(owner = "client!el", name = "h", descriptor = "I")
    public int textOffsetX;

    @OriginalMember(owner = "client!el", name = "v", descriptor = "I")
    public int secondaryVarEnd;

    @OriginalMember(owner = "client!el", name = "M", descriptor = "Lclient!ml;")
    public MapElementTypeList myList;

    @OriginalMember(owner = "client!el", name = "g", descriptor = "I")
    public int fillColour;

    @OriginalMember(owner = "client!el", name = "Q", descriptor = "I")
    public int secondaryVarStart;

    @OriginalMember(owner = "client!el", name = "G", descriptor = "I")
    public int outlineColour;

    @OriginalMember(owner = "client!el", name = "t", descriptor = "I")
    public int landmarkBackground;

    @OriginalMember(owner = "client!el", name = "Y", descriptor = "Ljava/lang/String;")
    public String text;

    @OriginalMember(owner = "client!el", name = "W", descriptor = "I")
    public int landmarkVarStart;

    @OriginalMember(owner = "client!el", name = "q", descriptor = "I")
    public int textOffsetY;

    @OriginalMember(owner = "client!el", name = "x", descriptor = "I")
    public int secondaryVarp = -1;

    @OriginalMember(owner = "client!el", name = "d", descriptor = "I")
    public int gapLength = -1;

    @OriginalMember(owner = "client!el", name = "H", descriptor = "I")
    public int secondaryVarBit = -1;

    @OriginalMember(owner = "client!el", name = "m", descriptor = "I")
    public int sprite = -1;

    @OriginalMember(owner = "client!el", name = "A", descriptor = "I")
    public int minZ = Integer.MIN_VALUE;

    @OriginalMember(owner = "client!el", name = "r", descriptor = "I")
    public int hoverSprite = -1;

    @OriginalMember(owner = "client!el", name = "K", descriptor = "Z")
    public boolean enabled = true;

    @OriginalMember(owner = "client!el", name = "j", descriptor = "Z")
    public boolean randomise = true;

    @OriginalMember(owner = "client!el", name = "u", descriptor = "I")
    public int multiVarp = -1;

    @OriginalMember(owner = "client!el", name = "X", descriptor = "I")
    public int maxZ = Integer.MAX_VALUE;

    @OriginalMember(owner = "client!el", name = "L", descriptor = "I")
    public int category = -1;

    @OriginalMember(owner = "client!el", name = "p", descriptor = "[Ljava/lang/String;")
    public final String[] ops = new String[5];

    @OriginalMember(owner = "client!el", name = "f", descriptor = "I")
    public int hoverTextColour = -1;

    @OriginalMember(owner = "client!el", name = "n", descriptor = "I")
    public int dashLength = -1;

    @OriginalMember(owner = "client!el", name = "U", descriptor = "Z")
    public boolean showOnWorldMap = true;

    @OriginalMember(owner = "client!el", name = "D", descriptor = "Z")
    public boolean showOnMinimap = false;

    @OriginalMember(owner = "client!el", name = "b", descriptor = "I")
    public int maxX = Integer.MAX_VALUE;

    @OriginalMember(owner = "client!el", name = "C", descriptor = "I")
    public int minX = Integer.MIN_VALUE;

    @OriginalMember(owner = "client!el", name = "a", descriptor = "I")
    public int multiVarBit = -1;

    @OriginalMember(owner = "client!el", name = "i", descriptor = "I")
    public int dashPhase = -1;

    @OriginalMember(owner = "client!el", name = "w", descriptor = "I")
    public int worldMapSprite = -1;

    @OriginalMember(owner = "client!el", name = "N", descriptor = "I")
    public int textSize = 0;

    @OriginalMember(owner = "client!el", name = "a", descriptor = "(BLclient!ge;)V")
    public void decode(@OriginalArg(1) Packet packet) {
        while (true) {
            @Pc(12) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(packet, code);
        }
    }

    @OriginalMember(owner = "client!el", name = "a", descriptor = "(BLclient!uk;)Z")
    public boolean variableTest(@OriginalArg(1) VarDomain varDomain) {
        @Pc(31) int primaryIndex;
        if (this.multiVarBit == -1) {
            if (this.multiVarp == -1) {
                return true;
            }

            primaryIndex = varDomain.getVarBitValue(this.multiVarp);
        } else {
            primaryIndex = varDomain.getVarValueInt(this.multiVarBit);
        }

        if (primaryIndex < this.landmarkVarStart || primaryIndex > this.landmarkVarEnd) {
            return false;
        }

        @Pc(78) int secondaryIndex;
        if (this.secondaryVarp == -1) {
            if (this.secondaryVarBit == -1) {
                return true;
            }

            secondaryIndex = varDomain.getVarBitValue(this.secondaryVarBit);
        } else {
            secondaryIndex = varDomain.getVarValueInt(this.secondaryVarp);
        }

        return secondaryIndex >= this.secondaryVarStart && this.secondaryVarEnd >= secondaryIndex;
    }

    @OriginalMember(owner = "client!el", name = "a", descriptor = "(IILjava/lang/String;)Ljava/lang/String;")
    public String param(@OriginalArg(0) int id, @OriginalArg(2) String dflt) {
        if (this.params == null) {
            return dflt;
        } else {
            @Pc(17) StringNode param = (StringNode) this.params.get(id);
            return param != null ? param.value : dflt;
        }
    }

    @OriginalMember(owner = "client!el", name = "a", descriptor = "(III)I")
    public int param(@OriginalArg(1) int dflt, @OriginalArg(2) int paramId) {
        if (this.params == null) {
            return dflt;
        } else {
            @Pc(27) IntNode param = (IntNode) this.params.get(paramId);
            return param != null ? param.value : dflt;
        }
    }

    @OriginalMember(owner = "client!el", name = "a", descriptor = "(ILclient!ha;)Lclient!st;")
    public Sprite worldMapSprite(@OriginalArg(1) Toolkit toolkit) {
        @Pc(28) Sprite sprite = (Sprite) this.myList.spriteCache.get(this.worldMapSprite | 0x20000 | (toolkit.index << 29));
        if (sprite != null) {
            return sprite;
        }

        this.myList.sprites.fileready(this.worldMapSprite);

        @Pc(49) IndexedImage image = IndexedImage.loadFirst(this.myList.sprites, this.worldMapSprite, 0);
        if (image != null) {
            sprite = toolkit.createSprite(image, true);
            this.myList.spriteCache.put(sprite, this.worldMapSprite | 0x20000 | (toolkit.index << 29));
        }

        return sprite;
    }

    @OriginalMember(owner = "client!el", name = "b", descriptor = "(Z)V")
    public void postDecode() {
        if (this.landmarkPolygons == null) {
            return;
        }

        for (@Pc(16) int i = 0; i < this.landmarkPolygons.length; i += 2) {
            if (this.landmarkPolygons[i] < this.maxX) {
                this.maxX = this.landmarkPolygons[i];
            } else if (this.minX < this.landmarkPolygons[i]) {
                this.minX = this.landmarkPolygons[i];
            }

            if (this.maxZ > this.landmarkPolygons[i + 1]) {
                this.maxZ = this.landmarkPolygons[i + 1];
            } else if (this.minZ < this.landmarkPolygons[i + 1]) {
                this.minZ = this.landmarkPolygons[i + 1];
            }
        }
    }

    @OriginalMember(owner = "client!el", name = "a", descriptor = "(ZBLclient!ha;)Lclient!st;")
    public Sprite sprite(@OriginalArg(2) Toolkit toolkit, @OriginalArg(0) boolean hovered) {
        @Pc(22) int id = hovered ? this.hoverSprite : this.sprite;
        @Pc(29) int key = toolkit.index << 29 | id;

        @Pc(38) Sprite sprite = (Sprite) this.myList.spriteCache.get(key);
        if (sprite != null) {
            return sprite;
        }

        if (this.myList.sprites.fileready(id)) {
            @Pc(60) IndexedImage image = IndexedImage.loadFirst(this.myList.sprites, id, 0);
            if (image != null) {
                sprite = toolkit.createSprite(image, true);
                this.myList.spriteCache.put(sprite, key);
            }

            return sprite;
        }

        return null;
    }

    @OriginalMember(owner = "client!el", name = "a", descriptor = "(BLclient!ge;I)V")
    public void decode(@OriginalArg(1) Packet packet, @OriginalArg(2) int code) {
        if (code == 1) {
            this.sprite = packet.g2();
        } else if (code == 2) {
            this.hoverSprite = packet.g2();
        } else if (code == 3) {
            this.text = packet.gjstr();
        } else if (code == 4) {
            this.textColour = packet.g3();
        } else if (code == 5) {
            this.hoverTextColour = packet.g3();
        } else if (code == 6) {
            this.textSize = packet.g1();
        } else if (code == 7) {
            @Pc(74) int flags = packet.g1();
            if ((flags & 0x1) == 0) {
                this.enabled = false;
            }
            if ((flags & 0x2) == 2) {
                this.showOnMinimap = true;
            }
        } else if (code == 8) {
            this.randomise = packet.g1() == 1;
        } else if (code == 9) {
            this.multiVarp = packet.g2();
            if (this.multiVarp == 65535) {
                this.multiVarp = -1;
            }

            this.multiVarBit = packet.g2();
            if (this.multiVarBit == 65535) {
                this.multiVarBit = -1;
            }

            this.landmarkVarStart = packet.g4();
            this.landmarkVarEnd = packet.g4();
        } else if (code >= 10 && code <= 14) {
            this.ops[code - 10] = packet.gjstr();
        } else if (code == 15) {
            @Pc(74) int polygonCount = packet.g1();
            this.landmarkPolygons = new int[polygonCount * 2];
            for (@Pc(291) int i = 0; i < polygonCount * 2; i++) {
                this.landmarkPolygons[i] = packet.g2s();
            }

            this.landmarkBackground = packet.g4();

            @Pc(389) int colourCount = packet.g1();
            this.landmarkPalette = new int[colourCount];
            for (@Pc(314) int i = 0; i < this.landmarkPalette.length; i++) {
                this.landmarkPalette[i] = packet.g4();
            }

            this.landmarkColorIndices = new byte[polygonCount];
            for (@Pc(419) int i = 0; i < polygonCount; i++) {
                this.landmarkColorIndices[i] = packet.g1b();
            }
        } else if (code == 16) {
            this.showOnWorldMap = false;
        } else if (code == 17) {
            this.opBase = packet.gjstr();
        } else if (code == 18) {
            this.worldMapSprite = packet.g2();
        } else if (code == 19) {
            this.category = packet.g2();
        } else if (code == 20) {
            this.secondaryVarBit = packet.g2();
            if (this.secondaryVarBit == 65535) {
                this.secondaryVarBit = -1;
            }
            this.secondaryVarp = packet.g2();
            if (this.secondaryVarp == 65535) {
                this.secondaryVarp = -1;
            }
            this.secondaryVarStart = packet.g4();
            this.secondaryVarEnd = packet.g4();
        } else if (code == 21) {
            this.outlineColour = packet.g4();
        } else if (code == 22) {
            this.fillColour = packet.g4();
        } else if (code == 23) {
            this.dashLength = packet.g1();
            this.gapLength = packet.g1();
            this.dashPhase = packet.g1();
        } else if (code == 24) {
            this.textOffsetX = packet.g2s();
            this.textOffsetY = packet.g2s();
        } else if (code == 249) {
            @Pc(74) int count = packet.g1();
            if (this.params == null) {
                @Pc(291) int bucketCount = IntMath.nextPow2(count);
                this.params = new IterableHashTable(bucketCount);
            }

            for (@Pc(291) int i = 0; i < count; i++) {
                @Pc(310) boolean string = packet.g1() == 1;
                @Pc(314) int id = packet.g3();

                @Pc(323) Node param;
                if (string) {
                    param = new StringNode(packet.gjstr());
                } else {
                    param = new IntNode(packet.g4());
                }

                this.params.put(id, param);
            }
        }
    }
}

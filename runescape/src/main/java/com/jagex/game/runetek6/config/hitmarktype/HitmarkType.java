package com.jagex.game.runetek6.config.hitmarktype;

import com.jagex.IndexedImage;
import com.jagex.core.io.Packet;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pb")
public final class HitmarkType {

    @OriginalMember(owner = "client!pb", name = "b", descriptor = "Lclient!ld;")
    public HitmarkTypeList myList;

    @OriginalMember(owner = "client!pb", name = "d", descriptor = "I")
    public int fadeTime = -1;

    @OriginalMember(owner = "client!pb", name = "r", descriptor = "Ljava/lang/String;")
    public String amountString = "";

    @OriginalMember(owner = "client!pb", name = "f", descriptor = "I")
    public int icon = -1;

    @OriginalMember(owner = "client!pb", name = "h", descriptor = "I")
    public int offsetX = 0;

    @OriginalMember(owner = "client!pb", name = "m", descriptor = "I")
    public int right = -1;

    @OriginalMember(owner = "client!pb", name = "w", descriptor = "I")
    public int left = -1;

    @OriginalMember(owner = "client!pb", name = "k", descriptor = "I")
    public int offsetY = 0;

    @OriginalMember(owner = "client!pb", name = "o", descriptor = "I")
    public int textColour = 0xFFFFFF;

    @OriginalMember(owner = "client!pb", name = "j", descriptor = "I")
    public int duration = 70;

    @OriginalMember(owner = "client!pb", name = "t", descriptor = "I")
    public int textOffsetY = 0;

    @OriginalMember(owner = "client!pb", name = "a", descriptor = "I")
    public int inner = -1;

    @OriginalMember(owner = "client!pb", name = "g", descriptor = "I")
    public int font = -1;

    @OriginalMember(owner = "client!pb", name = "n", descriptor = "I")
    public int comparisonType = -1;

    @OriginalMember(owner = "client!pb", name = "a", descriptor = "(Lclient!ha;I)Lclient!st;")
    public Sprite getInner(@OriginalArg(0) Toolkit toolkit) {
        if (this.inner < 0) {
            return null;
        }

        @Pc(22) Sprite inner = (Sprite) this.myList.hitmarkCache.get(this.inner);
        if (inner == null) {
            this.loadSprites(toolkit);
            inner = (Sprite) this.myList.hitmarkCache.get(this.inner);
        }
        return inner;
    }

    @OriginalMember(owner = "client!pb", name = "c", descriptor = "(ILclient!ha;)Lclient!st;")
    public Sprite getLeft(@OriginalArg(1) Toolkit toolkit) {
        if (this.left < 0) {
            return null;
        }
        @Pc(29) Sprite left = (Sprite) this.myList.hitmarkCache.get(this.left);
        if (left == null) {
            this.loadSprites(toolkit);
            left = (Sprite) this.myList.hitmarkCache.get(this.left);
        }
        return left;
    }

    @OriginalMember(owner = "client!pb", name = "a", descriptor = "(Lclient!ha;B)Lclient!st;")
    public Sprite getRight(@OriginalArg(0) Toolkit toolkit) {
        if (this.right < 0) {
            return null;
        }
        @Pc(27) Sprite right = (Sprite) this.myList.hitmarkCache.get(this.right);
        if (right == null) {
            this.loadSprites(toolkit);
            right = (Sprite) this.myList.hitmarkCache.get(this.right);
        }
        return right;
    }

    @OriginalMember(owner = "client!pb", name = "a", descriptor = "(ILclient!ha;)Lclient!st;")
    public Sprite getIcon(@OriginalArg(1) Toolkit toolkit) {
        if (this.icon < 0) {
            return null;
        }
        @Pc(20) Sprite icon = (Sprite) this.myList.hitmarkCache.get(this.icon);
        if (icon == null) {
            this.loadSprites(toolkit);
            icon = (Sprite) this.myList.hitmarkCache.get(this.icon);
        }
        return icon;
    }

    @OriginalMember(owner = "client!pb", name = "b", descriptor = "(ILclient!ha;)V")
    public void loadSprites(@OriginalArg(1) Toolkit toolkit) {
        @Pc(7) js5 sprites = this.myList.sprites;
        @Pc(34) IndexedImage image;
        if (this.icon >= 0 && this.myList.hitmarkCache.get(this.icon) == null && sprites.fileready(this.icon)) {
            image = IndexedImage.loadFirst(sprites, this.icon);
            this.myList.hitmarkCache.put(toolkit.createSprite(image, true), this.icon);
        }
        if (this.inner >= 0 && this.myList.hitmarkCache.get(this.inner) == null && sprites.fileready(this.inner)) {
            image = IndexedImage.loadFirst(sprites, this.inner);
            this.myList.hitmarkCache.put(toolkit.createSprite(image, true), this.inner);
        }
        if (this.left >= 0 && this.myList.hitmarkCache.get(this.left) == null && sprites.fileready(this.left)) {
            image = IndexedImage.loadFirst(sprites, this.left);
            this.myList.hitmarkCache.put(toolkit.createSprite(image, true), this.left);
        }
        if (this.right >= 0 && this.myList.hitmarkCache.get(this.right) == null && sprites.fileready(this.right)) {
            image = IndexedImage.loadFirst(sprites, this.right);
            this.myList.hitmarkCache.put(toolkit.createSprite(image, true), this.right);
        }
    }

    @OriginalMember(owner = "client!pb", name = "a", descriptor = "(Lclient!ge;BI)V")
    public void decode(@OriginalArg(0) Packet packet, @OriginalArg(2) int code) {
        if (code == 1) {
            this.font = packet.g2();
        } else if (code == 2) {
            this.textColour = packet.g3();
        } else if (code == 3) {
            this.icon = packet.g2();
        } else if (code == 4) {
            this.left = packet.g2();
        } else if (code == 5) {
            this.inner = packet.g2();
        } else if (code == 6) {
            this.right = packet.g2();
        } else if (code == 7) {
            this.offsetX = packet.g2s();
        } else if (code == 8) {
            this.amountString = packet.gjstr2();
        } else if (code == 9) {
            this.duration = packet.g2();
        } else if (code == 10) {
            this.offsetY = packet.g2s();
        } else if (code == 11) {
            this.fadeTime = 0;
        } else if (code == 12) {
            this.comparisonType = packet.g1();
        } else if (code == 13) {
            this.textOffsetY = packet.g2s();
        } else if (code == 14) {
            this.fadeTime = packet.g2();
        }
    }

    @OriginalMember(owner = "client!pb", name = "a", descriptor = "(IZ)Ljava/lang/String;")
    public String method6457(@OriginalArg(0) int arg0) {
        @Pc(8) String local8 = this.amountString;
        while (true) {
            @Pc(14) int local14 = local8.indexOf("%1");
            if (local14 < 0) {
                return local8;
            }
            local8 = local8.substring(0, local14) + StringTools.decimalWithSign(false, arg0) + local8.substring(local14 + 2);
        }
    }

    @OriginalMember(owner = "client!pb", name = "a", descriptor = "(ZLclient!ge;)V")
    public void decode(@OriginalArg(1) Packet packet) {
        while (true) {
            @Pc(3) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(packet, code);
        }
    }
}

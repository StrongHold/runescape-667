import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static96 {

    @OriginalMember(owner = "client!da", name = "a", descriptor = "(II)Lclient!pf;")
    public static TextureOp newTextureOp(@OriginalArg(0) int type) {
        if (type == 0) {
            return new Node_Sub1_Sub17();
        } else if (type == 1) {
            return new Node_Sub1_Sub26();
        } else if (type == 2) {
            return new Node_Sub1_Sub33();
        } else if (type == 3) {
            return new Node_Sub1_Sub25();
        } else if (type == 4) {
            return new TextureOpBrick();
        } else if (type == 5) {
            return new TextureOpBlur();
        } else if (type == 6) {
            return new Node_Sub1_Sub35();
        } else if (type == 7) {
            return new Node_Sub1_Sub18();
        } else if (type == 8) {
            return new Node_Sub1_Sub37();
        } else if (type == 9) {
            return new Node_Sub1_Sub38();
        } else if (type == 10) {
            return new Node_Sub1_Sub14();
        } else if (type == 11) {
            return new TextureOpColourise();
        } else if (type == 12) {
            return new TextureOpWave();
        } else if (type == 13) {
            return new Node_Sub1_Sub34();
        } else if (type == 14) {
            return new Node_Sub1_Sub29();
        } else if (type == 15) {
            return new Node_Sub1_Sub23();
        } else if (type == 16) {
            return new Node_Sub1_Sub39();
        } else if (type == 17) {
            return new TextureOpHsl();
        } else if (type == 18) {
            return new TextureOpSpriteTiled();
        } else if (type == 19) {
            return new TextureOpDisplace();
        } else if (type == 20) {
            return new Node_Sub1_Sub30();
        } else if (type == 21) {
            return new TextureOpBlend();
        } else if (type == 22) {
            return new Node_Sub1_Sub16();
        } else if (type == 23) {
            return new Node_Sub1_Sub31();
        } else if (type == 24) {
            return new Node_Sub1_Sub21();
        } else if (type == 25) {
            return new TextureOpRecolour();
        } else if (type == 26) {
            return new TextureOpThreshold();
        } else if (type == 27) {
            return new Node_Sub1_Sub28();
        } else if (type == 28) {
            return new TextureOpStoneWall();
        } else if (type == 29) {
            return new TextureOpShape();
        } else if (type == 30) {
            return new TextureOpRange();
        } else if (type == 31) {
            return new Node_Sub1_Sub36();
        } else if (type == 32) {
            return new Node_Sub1_Sub32();
        } else if (type == 33) {
            return new Node_Sub1_Sub19();
        } else if (type == 34) {
            return new Node_Sub1_Sub27();
        } else if (type == 35) {
            return new Node_Sub1_Sub22();
        } else if (type == 36) {
            return new TextureOpTexture();
        } else if (type == 37) {
            return new Node_Sub1_Sub20();
        } else if (type == 38) {
            return new Node_Sub1_Sub15();
        } else if (type == 39) {
            return new TextureOpSprite();
        } else {
            return null;
        }
    }

    @OriginalMember(owner = "client!da", name = "a", descriptor = "(IIIIB)V")
    public static void setClipBounds(@OriginalArg(0) int maxY, @OriginalArg(2) int maxX) {
        Static111.anInt2219 = maxX;
        Static273.anInt4395 = maxY;
        Static180.anInt2995 = 0;
        Static724.anInt10930 = 0;
    }
}

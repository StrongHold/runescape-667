import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static96 {

    @OriginalMember(owner = "client!da", name = "a", descriptor = "(II)Lclient!pf;")
    public static TextureOp method8821(@OriginalArg(0) int arg0) {
        if (arg0 == 0) {
            return new Node_Sub1_Sub17();
        } else if (arg0 == 1) {
            return new Node_Sub1_Sub26();
        } else if (arg0 == 2) {
            return new Node_Sub1_Sub33();
        } else if (arg0 == 3) {
            return new Node_Sub1_Sub25();
        } else if (arg0 == 4) {
            return new TextureOpBrick();
        } else if (arg0 == 5) {
            return new TextureOpBlur();
        } else if (arg0 == 6) {
            return new Node_Sub1_Sub35();
        } else if (arg0 == 7) {
            return new Node_Sub1_Sub18();
        } else if (arg0 == 8) {
            return new Node_Sub1_Sub37();
        } else if (arg0 == 9) {
            return new Node_Sub1_Sub38();
        } else if (arg0 == 10) {
            return new Node_Sub1_Sub14();
        } else if (arg0 == 11) {
            return new TextureOpColourise();
        } else if (arg0 == 12) {
            return new TextureOpWave();
        } else if (arg0 == 13) {
            return new Node_Sub1_Sub34();
        } else if (arg0 == 14) {
            return new Node_Sub1_Sub29();
        } else if (arg0 == 15) {
            return new Node_Sub1_Sub23();
        } else if (arg0 == 16) {
            return new Node_Sub1_Sub39();
        } else if (arg0 == 17) {
            return new TextureOpHsl();
        } else if (arg0 == 18) {
            return new TextureOpSpriteTiled();
        } else if (arg0 == 19) {
            return new TextureOpDisplace();
        } else if (arg0 == 20) {
            return new Node_Sub1_Sub30();
        } else if (arg0 == 21) {
            return new TextureOpBlend();
        } else if (arg0 == 22) {
            return new Node_Sub1_Sub16();
        } else if (arg0 == 23) {
            return new Node_Sub1_Sub31();
        } else if (arg0 == 24) {
            return new Node_Sub1_Sub21();
        } else if (arg0 == 25) {
            return new TextureOpRecolour();
        } else if (arg0 == 26) {
            return new TextureOpThreshold();
        } else if (arg0 == 27) {
            return new Node_Sub1_Sub28();
        } else if (arg0 == 28) {
            return new TextureOpStoneWall();
        } else if (arg0 == 29) {
            return new TextureOpShape();
        } else if (arg0 == 30) {
            return new TextureOpRange();
        } else if (arg0 == 31) {
            return new Node_Sub1_Sub36();
        } else if (arg0 == 32) {
            return new Node_Sub1_Sub32();
        } else if (arg0 == 33) {
            return new Node_Sub1_Sub19();
        } else if (arg0 == 34) {
            return new Node_Sub1_Sub27();
        } else if (arg0 == 35) {
            return new Node_Sub1_Sub22();
        } else if (arg0 == 36) {
            return new TextureOpTexture();
        } else if (arg0 == 37) {
            return new Node_Sub1_Sub20();
        } else if (arg0 == 38) {
            return new Node_Sub1_Sub15();
        } else if (arg0 == 39) {
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

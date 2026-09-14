import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static96 {

    @OriginalMember(owner = "client!da", name = "a", descriptor = "(II)Lclient!pf;")
    public static TextureOp newTextureOp(@OriginalArg(0) int type) {
        if (type == 0) {
            return new TextureOpConstant();
        } else if (type == 1) {
            return new TextureOpColourFill();
        } else if (type == 2) {
            return new TextureOpHorizontalGradient();
        } else if (type == 3) {
            return new TextureOpVerticalGradient();
        } else if (type == 4) {
            return new TextureOpBrick();
        } else if (type == 5) {
            return new TextureOpBlur();
        } else if (type == 6) {
            return new TextureOpClamp();
        } else if (type == 7) {
            return new TextureOpCombine();
        } else if (type == 8) {
            return new TextureOpCurve();
        } else if (type == 9) {
            return new TextureOpFlip();
        } else if (type == 10) {
            return new TextureOpGradient();
        } else if (type == 11) {
            return new TextureOpColourise();
        } else if (type == 12) {
            return new TextureOpWave();
        } else if (type == 13) {
            return new TextureOpWhiteNoise();
        } else if (type == 14) {
            return new TextureOpWeave();
        } else if (type == 15) {
            return new TextureOpCellularNoise();
        } else if (type == 16) {
            return new TextureOpHerringbone();
        } else if (type == 17) {
            return new TextureOpHsl();
        } else if (type == 18) {
            return new TextureOpSpriteTiled();
        } else if (type == 19) {
            return new TextureOpDisplace();
        } else if (type == 20) {
            return new TextureOpTile();
        } else if (type == 21) {
            return new TextureOpBlend();
        } else if (type == 22) {
            return new TextureOpInvert();
        } else if (type == 23) {
            return new TextureOpKaleidoscope();
        } else if (type == 24) {
            return new TextureOpGreyscale();
        } else if (type == 25) {
            return new TextureOpRecolour();
        } else if (type == 26) {
            return new TextureOpThreshold();
        } else if (type == 27) {
            return new TextureOpSquareWave();
        } else if (type == 28) {
            return new TextureOpStoneWall();
        } else if (type == 29) {
            return new TextureOpShape();
        } else if (type == 30) {
            return new TextureOpRange();
        } else if (type == 31) {
            return new TextureOpMandelbrot();
        } else if (type == 32) {
            return new TextureOpEmboss();
        } else if (type == 33) {
            return new TextureOpNormalMap();
        } else if (type == 34) {
            return new Node_Sub1_Sub27();
        } else if (type == 35) {
            return new Node_Sub1_Sub22();
        } else if (type == 36) {
            return new TextureOpTexture();
        } else if (type == 37) {
            return new Node_Sub1_Sub20();
        } else if (type == 38) {
            return new TextureOpScratch();
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

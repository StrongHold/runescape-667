import com.jagex.IndexedImage;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.js5.js5;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static397 {

    @OriginalMember(owner = "client!mia", name = "e", descriptor = "[I")
    public static int[] hueColumns;

    @OriginalMember(owner = "client!mia", name = "a", descriptor = "(IIIIBI[B)V")
    public static void method5554(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) byte[] arg5) {
        if (arg3 > 0 && !Node_Sub1_Sub27.method9150(arg3)) {
            throw new IllegalArgumentException("");
        } else if (arg4 <= 0 || Node_Sub1_Sub27.method9150(arg4)) {
            @Pc(35) int local35 = Static646.method8458(arg2);
            @Pc(37) int local37 = 0;
            @Pc(45) int local45 = arg4 <= arg3 ? arg4 : arg3;
            @Pc(49) int local49 = arg3 >> 1;
            @Pc(53) int local53 = arg4 >> 1;
            @Pc(55) byte[] local55 = arg5;
            @Pc(62) byte[] local62 = new byte[local35 * local49 * local53];
            while (true) {
                OpenGL.glTexImage2Dub(arg1, local37, arg0, arg3, arg4, 0, arg2, OpenGL.GL_UNSIGNED_BYTE, local55, 0);
                if (local45 <= 1) {
                    return;
                }
                @Pc(82) int local82 = local35 * arg3;
                for (@Pc(84) int local84 = 0; local84 < local35; local84++) {
                    @Pc(87) int local87 = local84;
                    @Pc(89) int local89 = local84;
                    @Pc(93) int local93 = local82 + local84;
                    for (@Pc(95) int local95 = 0; local95 < local53; local95++) {
                        for (@Pc(98) int local98 = 0; local98 < local49; local98++) {
                            @Pc(103) byte local103 = local55[local89];
                            local89 += local35;
                            @Pc(113) int local113 = local103 + local55[local89];
                            local89 += local35;
                            @Pc(123) int local123 = local113 + local55[local93];
                            local93 += local35;
                            @Pc(133) int local133 = local123 + local55[local93];
                            local93 += local35;
                            local62[local87] = (byte) (local133 >> 2);
                            local87 += local35;
                        }
                        local93 += local82;
                        local89 += local82;
                    }
                }
                @Pc(184) byte[] local184 = local62;
                local62 = local55;
                arg3 = local49;
                arg4 = local53;
                local55 = local184;
                local37++;
                local45 >>= 0x1;
                local49 >>= 0x1;
                local53 >>= 0x1;
            }
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!mia", name = "a", descriptor = "(BILclient!cg;ILclient!cg;IIIII)V")
    public static void method5557(@OriginalArg(1) int arg0, @OriginalArg(2) PathingEntity arg1, @OriginalArg(4) PathingEntity arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4, @OriginalArg(8) int arg5, @OriginalArg(9) int arg6) {
        @Pc(7) int local7 = arg2.method9304((byte) -71);
        if (local7 == -1) {
            return;
        }
        @Pc(27) Sprite local27 = (Sprite) Sprites.mobilisingArmiesCache.get(local7);
        if (local27 == null) {
            @Pc(34) IndexedImage[] local34 = IndexedImage.load(js5.SPRITES, local7, 0);
            if (local34 == null) {
                return;
            }
            local27 = Toolkit.active.createSprite(local34[0], true);
            Sprites.mobilisingArmiesCache.put(local27, local7);
        }
        Static418.projectFromGround(arg1.level, arg1.z, 0, arg1.x, arg1.getSize() * 256);
        @Pc(75) int local75 = arg0 + OverlayManager.hitmarkpos[0] - 18;
        @Pc(83) int local83 = local75 + arg6 / 4 * 18;
        @Pc(94) int local94 = arg5 + OverlayManager.hitmarkpos[1] - 54 - 16;
        @Pc(102) int local102 = local94 + arg6 % 4 * 18;
        local27.render(local83, local102);
        if (arg2 == arg1) {
            Toolkit.active.outlineRect(local83 - 1, local102 - 1, 18, 18, -256);
        }
        OrthoMode.method8927(local83 - 1, local83 - -18, local102 - 1, local102 - -18);
        @Pc(140) Class8_Sub1 local140 = Static192.method2876();
        local140.anInt108 = local83;
        local140.aClass8_Sub2_Sub1_Sub2_1 = arg2;
        local140.anInt112 = local102;
        local140.anInt109 = local83 + 16;
        local140.anInt111 = local102 + 16;
        Static149.A_ENTITY_LIST___4.add(local140);
    }
}

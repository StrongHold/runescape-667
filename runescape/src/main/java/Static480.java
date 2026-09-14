import com.jagex.core.io.Packet;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.sign.SignLink;
import com.jagex.sign.SignedResource;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static480 {

    @OriginalMember(owner = "client!pca", name = "a", descriptor = "I")
    public static int npcCount = 0;

    @OriginalMember(owner = "client!pca", name = "a", descriptor = "(II[IIIIII)V")
    public static void method6466(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[] arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(7) int arg5) {
        if (arg0 > 0 && !Node_Sub1_Sub27.method9150(arg0)) {
            throw new IllegalArgumentException("");
        } else if (arg4 <= 0 || Node_Sub1_Sub27.method9150(arg4)) {
            @Pc(47) int local47 = 0;
            @Pc(55) int local55 = arg4 <= arg0 ? arg4 : arg0;
            @Pc(59) int local59 = arg0 >> 1;
            @Pc(70) int local70 = arg4 >> 1;
            @Pc(72) int[] local72 = arg2;
            @Pc(77) int[] local77 = new int[local70 * local59];
            while (true) {
                OpenGL.glTexImage2Di(arg1, local47, arg3, arg0, arg4, 0, OpenGL.GL_BGRA, arg5, local72, 0);
                if (local55 <= 1) {
                    return;
                }
                @Pc(97) int local97 = 0;
                @Pc(99) int local99 = 0;
                @Pc(104) int local104 = arg0;
                @Pc(106) int[] local106 = local77;
                for (@Pc(108) int local108 = 0; local108 < local70; local108++) {
                    for (@Pc(111) int local111 = 0; local111 < local59; local111++) {
                        @Pc(117) int local117 = local72[local99++];
                        @Pc(122) int local122 = local72[local104++];
                        @Pc(127) int local127 = local72[local99++];
                        @Pc(133) int local133 = local117 >> 24 & 0xFF;
                        @Pc(137) int local137 = local117 & 0xFF;
                        @Pc(142) int local142 = local72[local104++];
                        @Pc(148) int local148 = local117 >> 8 & 0xFF;
                        @Pc(154) int local154 = local117 >> 16 & 0xFF;
                        @Pc(162) int local162 = local154 + (local127 >> 16 & 0xFF);
                        @Pc(170) int local170 = local148 + (local127 >> 8 & 0xFF);
                        @Pc(176) int local176 = local137 + (local127 & 0xFF);
                        @Pc(184) int local184 = local133 + (local127 >> 24 & 0xFF);
                        @Pc(192) int local192 = local184 + (local122 >> 24 & 0xFF);
                        @Pc(198) int local198 = local176 + (local122 & 0xFF);
                        @Pc(206) int local206 = local162 + (local122 >> 16 & 0xFF);
                        @Pc(214) int local214 = local170 + (local122 >> 8 & 0xFF);
                        @Pc(222) int local222 = local192 + (local142 >> 24 & 0xFF);
                        @Pc(228) int local228 = local198 + (local142 & 0xFF);
                        @Pc(236) int local236 = local206 + (local142 >> 16 & 0xFF);
                        @Pc(244) int local244 = local214 + (local142 >> 8 & 0xFF);
                        local77[local97++] = (local236 & 0x3FC) << 14 | (local222 & 0x3FC) << 22 | (local244 & 0x3FC) << 6 | local228 >> 2 & 0xFF;
                    }
                    local104 += arg0;
                    local99 += arg0;
                }
                local77 = local72;
                arg4 = local70;
                arg0 = local59;
                local72 = local106;
                local70 >>= 0x1;
                local59 >>= 0x1;
                local55 >>= 0x1;
                local47++;
            }
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!pca", name = "a", descriptor = "(ILclient!ge;Lclient!vq;I)V")
    public static void runReflectionChecker(@OriginalArg(1) Packet arg0, @OriginalArg(2) SignLink arg1, @OriginalArg(3) int arg2) {
        @Pc(23) ReflectionCheck local23 = new ReflectionCheck();
        local23.memberCount = arg0.g1();
        local23.magic = arg0.g4();
        local23.status = new int[local23.memberCount];
        local23.fieldValues = new int[local23.memberCount];
        local23.field = new SignedResource[local23.memberCount];
        local23.arguments = new byte[local23.memberCount][][];
        local23.memberTypes = new int[local23.memberCount];
        local23.methods = new SignedResource[local23.memberCount];
        for (@Pc(65) int local65 = 0; local65 < local23.memberCount; local65++) {
            try {
                @Pc(73) int local73 = arg0.g1();
                @Pc(106) String local106;
                @Pc(110) String local110;
                @Pc(114) int local114;
                if (local73 == 0 || local73 == 1 || local73 == 2) {
                    local106 = arg0.gjstr();
                    local110 = arg0.gjstr();
                    local114 = 0;
                    if (local73 == 1) {
                        local114 = arg0.g4();
                    }
                    local23.memberTypes[local65] = local73;
                    local23.fieldValues[local65] = local114;
                    local23.field[local65] = arg1.getField(Static488.method6524(local106), local110);
                } else if (local73 == 3 || local73 == 4) {
                    local106 = arg0.gjstr();
                    local110 = arg0.gjstr();
                    local114 = arg0.g1();
                    @Pc(117) String[] local117 = new String[local114];
                    for (@Pc(119) int local119 = 0; local119 < local114; local119++) {
                        local117[local119] = arg0.gjstr();
                    }
                    @Pc(138) byte[][] local138 = new byte[local114][];
                    @Pc(153) int local153;
                    if (local73 == 3) {
                        for (@Pc(147) int local147 = 0; local147 < local114; local147++) {
                            local153 = arg0.g4();
                            local138[local147] = new byte[local153];
                            arg0.gdata(0, local153, local138[local147]);
                        }
                    }
                    local23.memberTypes[local65] = local73;
                    @Pc(180) Class[] local180 = new Class[local114];
                    for (local153 = 0; local153 < local114; local153++) {
                        local180[local153] = Static488.method6524(local117[local153]);
                    }
                    local23.methods[local65] = arg1.getMethod(Static488.method6524(local106), local110, local180);
                    local23.arguments[local65] = local138;
                }
            } catch (@Pc(271) ClassNotFoundException local271) {
                local23.status[local65] = -1;
            } catch (@Pc(278) SecurityException local278) {
                local23.status[local65] = -2;
            } catch (@Pc(285) NullPointerException local285) {
                local23.status[local65] = -3;
            } catch (@Pc(292) Exception local292) {
                local23.status[local65] = -4;
            } catch (@Pc(299) Throwable local299) {
                local23.status[local65] = -5;
            }
        }
        Static631.reflectionChecks.addLast(local23);
    }

    @OriginalMember(owner = "client!pca", name = "c", descriptor = "(B)V")
    public static void method6469() {
        OrthoMode.toolkit.setCamera(OrthoMode.savedCamera);
        OrthoMode.toolkit.DA(OrthoMode.savedViewportX, OrthoMode.savedViewportY, OrthoMode.savedViewportWidth, OrthoMode.savedViewportHeight);
    }
}

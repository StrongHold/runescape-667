import com.jagex.core.util.Arrays;
import com.jagex.core.util.JavaScript;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.PointLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static358 {

    // $FF: synthetic field
    @OriginalMember(owner = "client!lf", name = "d", descriptor = "Ljava/lang/Class;")
    public static Class positionEntityClass;

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "(BI[S)[S")
    public static short[] method9173(@OriginalArg(1) int length, @OriginalArg(2) short[] src) {
        @Pc(14) short[] copy = new short[length];
        Arrays.copy(src, 0, copy, 0, length);
        return copy;
    }

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "()V")
    public static void method9182() {
        for (@Pc(1) int local1 = 0; local1 < Static319.anInt5080; local1++) {
            if (!Static279.aBooleanArray11[local1]) {
                @Pc(10) EnvironmentLight local10 = EnvironmentLight.aEnvironmentLightArray1[local1];
                @Pc(13) PointLight local13 = local10.light;
                @Pc(16) int local16 = local10.level;
                @Pc(22) int local22 = local13.getRange() - EnvironmentLight.anInt3993;
                @Pc(30) int local30 = (local22 * 2 >> EnvironmentLight.anInt1066) + 1;
                @Pc(32) int local32 = 0;
                @Pc(37) int[] local37 = new int[local30 * local30];
                @Pc(45) int local45 = local13.getX() - local22 >> EnvironmentLight.anInt1066;
                @Pc(53) int local53 = local13.getZ() - local22 >> EnvironmentLight.anInt1066;
                @Pc(61) int local61 = local13.getZ() + local22 >> EnvironmentLight.anInt1066;
                if (local53 < 0) {
                    local32 = -local53;
                    local53 = 0;
                }
                if (local61 >= Static662.tileMaxZ) {
                    local61 = Static662.tileMaxZ - 1;
                }
                for (@Pc(78) int local78 = local53; local78 <= local61; local78++) {
                    @Pc(84) short local84 = local10.aShortArray131[local32];
                    @Pc(88) int local88 = local84 >>> 8;
                    @Pc(94) int local94 = local32 * local30 + local88;
                    @Pc(100) int local100 = local45 + (local84 >>> 8);
                    @Pc(108) int local108 = local100 + (local84 & 0xFF) - 1;
                    if (local100 < 0) {
                        local94 -= local100;
                        local100 = 0;
                    }
                    if (local108 >= Static619.tileMaxX) {
                        local108 = Static619.tileMaxX - 1;
                    }
                    for (@Pc(125) int local125 = local100; local125 <= local108; local125++) {
                        @Pc(128) byte local128 = 1;
                        @Pc(144) PositionEntity local144 = Static578.getEntity(local16, local125, local78, positionEntityClass == null ? (positionEntityClass = getClass("PositionEntity")) : positionEntityClass);
                        if (local144 != null && local144.aByte145 != 0) {
                            @Pc(169) boolean local169;
                            @Pc(179) boolean local179;
                            @Pc(195) short local195;
                            @Pc(201) int local201;
                            @Pc(207) int local207;
                            if (local144.aByte145 == 1) {
                                local169 = local125 - 1 >= local100;
                                local179 = local125 + 1 <= local108;
                                if (!local169 && local78 + 1 <= local61) {
                                    local195 = local10.aShortArray131[local32 + 1];
                                    local201 = local45 + (local195 >>> 8);
                                    local207 = local201 + (local195 & 0xFF);
                                    local169 = local125 > local201 && local125 < local207;
                                }
                                if (!local179 && local78 - 1 >= local53) {
                                    local195 = local10.aShortArray131[local32 - 1];
                                    local201 = local45 + (local195 >>> 8);
                                    local207 = local201 + (local195 & 0xFF);
                                    local179 = local125 > local201 && local125 < local207;
                                }
                                if (local169 && !local179) {
                                    local128 = 4;
                                } else if (local179 && !local169) {
                                    local128 = 2;
                                }
                            } else {
                                local169 = local125 - 1 >= local100;
                                local179 = local125 + 1 <= local108;
                                if (!local169 && local78 - 1 >= local53) {
                                    local195 = local10.aShortArray131[local32 - 1];
                                    local201 = local45 + (local195 >>> 8);
                                    local207 = local201 + (local195 & 0xFF);
                                    local169 = local125 > local201 && local125 < local207;
                                }
                                if (!local179 && local78 + 1 <= local61) {
                                    local195 = local10.aShortArray131[local32 + 1];
                                    local201 = local45 + (local195 >>> 8);
                                    local207 = local201 + (local195 & 0xFF);
                                    local179 = local125 > local201 && local125 < local207;
                                }
                                if (local169 && !local179) {
                                    local128 = 3;
                                } else if (local179 && !local169) {
                                    local128 = 5;
                                }
                            }
                        }
                        local37[local94++] = local128;
                    }
                    local32++;
                }
                Static279.aBooleanArray11[local1] = true;
                Static246.ground[local16].method7868(local13, local37);
            }
        }
    }

    static Class getClass(String name) {
        Class instance;
        try {
            instance = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw (NoClassDefFoundError) new NoClassDefFoundError().initCause(ex);
        }
        return instance;
    }

    @OriginalMember(owner = "client!lf", name = "e", descriptor = "(I)V")
    public static void setUnderageCookie() {
        if (GameShell.loaderApplet == null) {
            return;
        }

        try {
            @Pc(23) String domain = GameShell.loaderApplet.getParameter("cookiehost");
            @Pc(31) int dob = (int) (SystemTimer.safetime() / TimeUtils.MILLISECONDS_PER_DAY) - TimeUtils.RUNEDAYS_SINCE_UNIX_EPOCH;
            @Pc(47) String cookie = "usrdob=" + dob + "; version=1; path=/; domain=" + domain;
            JavaScript.eval("document.cookie=\"" + cookie + "\"", GameShell.loaderApplet);
        } catch (@Pc(63) Throwable ignored) {
            /* empty */
        }
    }

    @OriginalMember(owner = "client!lf", name = "d", descriptor = "(B)V")
    public static void method9191() {
        if (Static112.aBoolean197) {
            return;
        }
        Static458.uploadLocationModels(Static478.aTileArrayArrayArray3);
        if (Static420.aTileArrayArrayArray2 != null) {
            Static458.uploadLocationModels(Static420.aTileArrayArrayArray2);
        }
        Static112.aBoolean197 = true;
    }
}

import com.jagex.Entity;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static498 {

    @OriginalMember(owner = "client!pm", name = "p", descriptor = "[I")
    public static final int[] statBaseLevels = new int[25];

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(IZIIBI)V")
    public static void method6643(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(5) int arg4) {
        if (arg4 < 1) {
            arg4 = 1;
        }
        if (arg3 < 1) {
            arg3 = 1;
        }
        @Pc(26) int local26 = arg3 - 334;
        if (local26 < 0) {
            local26 = 0;
        } else if (local26 > 100) {
            local26 = 100;
        }
        @Pc(49) int local49 = (Static640.wideFov - Static640.fov) * local26 / 100 + Static640.fov;
        if (Static25.aShort1 > local49) {
            local49 = Static25.aShort1;
        } else if (Static598.aShort120 < local49) {
            local49 = Static598.aShort120;
        }
        @Pc(72) int local72 = arg3 * local49 * 512 / (arg4 * 334);
        @Pc(115) int local115;
        @Pc(122) int local122;
        @Pc(86) short local86;
        if (local72 < Static552.aShort123) {
            local86 = Static552.aShort123;
            local49 = local86 * arg4 * 334 / (arg3 * 512);
            if (Static598.aShort120 < local49) {
                local49 = Static598.aShort120;
                local115 = local49 * arg3 * 512 / (local86 * 334);
                local122 = (arg4 - local115) / 2;
                if (arg1) {
                    Toolkit.active.la();
                    Toolkit.active.fillRect(arg0, arg2, local122, arg3, -16777216);
                    Toolkit.active.fillRect(arg4 + arg0 - local122, arg2, local122, arg3, -16777216);
                }
                arg0 += local122;
                arg4 -= local122 * 2;
            }
        } else if (Static306.aShort59 < local72) {
            local86 = Static306.aShort59;
            local49 = local86 * 334 * arg4 / (arg3 * 512);
            if (Static25.aShort1 > local49) {
                local49 = Static25.aShort1;
                local115 = arg4 * 334 * local86 / (local49 * 512);
                local122 = (arg3 - local115) / 2;
                if (arg1) {
                    Toolkit.active.la();
                    Toolkit.active.fillRect(arg0, arg2, arg4, local122, -16777216);
                    Toolkit.active.fillRect(arg0, arg2 + arg3 - local122, arg4, local122, -16777216);
                }
                arg3 -= local122 * 2;
                arg2 += local122;
            }
        }
        Static32.anInt773 = arg0;
        Static200.anInt3305 = (short) arg3;
        Static428.anInt6495 = local49 * arg3 / 334;
        Static290.anInt4657 = arg2;
        Static242.anInt3971 = (short) arg4;
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "([Lclient!eo;II)V")
    public static void method6650(@OriginalArg(0) Entity[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (arg1 >= arg2) {
            return;
        }
        @Pc(8) int local8 = (arg1 + arg2) / 2;
        @Pc(10) int local10 = arg1;
        @Pc(14) Entity local14 = arg0[local8];
        arg0[local8] = arg0[arg2];
        arg0[arg2] = local14;
        @Pc(27) int local27 = local14.anInt10697;
        for (@Pc(29) int local29 = arg1; local29 < arg2; local29++) {
            if (arg0[local29].anInt10697 > local27 + (local29 & 0x1)) {
                @Pc(44) Entity local44 = arg0[local29];
                arg0[local29] = arg0[local10];
                arg0[local10++] = local44;
            }
        }
        arg0[arg2] = arg0[local10];
        arg0[local10] = local14;
        method6650(arg0, arg1, local10 - 1);
        method6650(arg0, local10 + 1, arg2);
    }
}

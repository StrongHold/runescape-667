import com.jagex.Entity;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static498 {

    @OriginalMember(owner = "client!pm", name = "p", descriptor = "[I")
    public static final int[] statBaseLevels = new int[25];

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(IZIIBI)V")
    public static void method6643(@OriginalArg(0) int x, @OriginalArg(1) boolean letterbox, @OriginalArg(2) int y, @OriginalArg(3) int height, @OriginalArg(5) int width) {
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        @Pc(26) int widePercent = height - 334;
        if (widePercent < 0) {
            widePercent = 0;
        } else if (widePercent > 100) {
            widePercent = 100;
        }
        @Pc(49) int fov = (Static640.wideFov - Static640.fov) * widePercent / 100 + Static640.fov;
        if (Static25.minFov > fov) {
            fov = Static25.minFov;
        } else if (Static598.maxFov < fov) {
            fov = Static598.maxFov;
        }
        @Pc(72) int horizontalFov = height * fov * 512 / (width * 334);
        @Pc(115) int fittedSize;
        @Pc(122) int barSize;
        @Pc(86) short clampedHorizontalFov;
        if (horizontalFov < Static552.aShort123) {
            clampedHorizontalFov = Static552.aShort123;
            fov = clampedHorizontalFov * width * 334 / (height * 512);
            if (Static598.maxFov < fov) {
                fov = Static598.maxFov;
                fittedSize = fov * height * 512 / (clampedHorizontalFov * 334);
                barSize = (width - fittedSize) / 2;
                if (letterbox) {
                    Toolkit.active.la();
                    Toolkit.active.fillRect(x, y, barSize, height, -16777216);
                    Toolkit.active.fillRect(width + x - barSize, y, barSize, height, -16777216);
                }
                x += barSize;
                width -= barSize * 2;
            }
        } else if (Static306.aShort59 < horizontalFov) {
            clampedHorizontalFov = Static306.aShort59;
            fov = clampedHorizontalFov * 334 * width / (height * 512);
            if (Static25.minFov > fov) {
                fov = Static25.minFov;
                fittedSize = width * 334 * clampedHorizontalFov / (fov * 512);
                barSize = (height - fittedSize) / 2;
                if (letterbox) {
                    Toolkit.active.la();
                    Toolkit.active.fillRect(x, y, width, barSize, -16777216);
                    Toolkit.active.fillRect(x, y + height - barSize, width, barSize, -16777216);
                }
                height -= barSize * 2;
                y += barSize;
            }
        }
        Static32.anInt773 = x;
        Static200.anInt3305 = (short) height;
        Static428.anInt6495 = fov * height / 334;
        Static290.anInt4657 = y;
        Static242.anInt3971 = (short) width;
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "([Lclient!eo;II)V")
    public static void method6650(@OriginalArg(0) Entity[] entities, @OriginalArg(1) int from, @OriginalArg(2) int to) {
        if (from >= to) {
            return;
        }
        @Pc(8) int pivotIndex = (from + to) / 2;
        @Pc(10) int boundary = from;
        @Pc(14) Entity pivot = entities[pivotIndex];
        entities[pivotIndex] = entities[to];
        entities[to] = pivot;
        @Pc(27) int pivotKey = pivot.anInt10697;
        for (@Pc(29) int i = from; i < to; i++) {
            if (entities[i].anInt10697 > pivotKey + (i & 0x1)) {
                @Pc(44) Entity entity = entities[i];
                entities[i] = entities[boundary];
                entities[boundary++] = entity;
            }
        }
        entities[to] = entities[boundary];
        entities[boundary] = pivot;
        method6650(entities, from, boundary - 1);
        method6650(entities, boundary + 1, to);
    }
}

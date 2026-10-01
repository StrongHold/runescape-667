import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static411 {

    @OriginalMember(owner = "client!mv", name = "a", descriptor = "(IIII)V")
    public static void scaleWallDecorOffsets(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) int offset) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        if (tile == null) {
            return;
        }
        @Pc(14) WallDecor wallDecor = tile.wallDecor;
        @Pc(17) WallDecor wallDecor2 = tile.wallDecor2;
        if (wallDecor != null) {
            wallDecor.aShort101 = (short) (wallDecor.aShort101 * offset / (0x10 << EnvironmentLight.anInt1066 - 7));
            wallDecor.aShort102 = (short) (wallDecor.aShort102 * offset / (0x10 << EnvironmentLight.anInt1066 - 7));
        }
        if (wallDecor2 != null) {
            wallDecor2.aShort101 = (short) (wallDecor2.aShort101 * offset / (0x10 << EnvironmentLight.anInt1066 - 7));
            wallDecor2.aShort102 = (short) (wallDecor2.aShort102 * offset / (0x10 << EnvironmentLight.anInt1066 - 7));
        }
    }
}

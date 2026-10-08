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
            wallDecor.offsetX = (short) (wallDecor.offsetX * offset / (0x10 << EnvironmentLight.tileShift - 7));
            wallDecor.offsetZ = (short) (wallDecor.offsetZ * offset / (0x10 << EnvironmentLight.tileShift - 7));
        }
        if (wallDecor2 != null) {
            wallDecor2.offsetX = (short) (wallDecor2.offsetX * offset / (0x10 << EnvironmentLight.tileShift - 7));
            wallDecor2.offsetZ = (short) (wallDecor2.offsetZ * offset / (0x10 << EnvironmentLight.tileShift - 7));
        }
    }
}

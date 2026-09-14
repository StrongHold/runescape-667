import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static305 {

    @OriginalMember(owner = "client!jm", name = "m", descriptor = "F")
    public static float aFloat86;

    @OriginalMember(owner = "client!jm", name = "n", descriptor = "J")
    public static long aLong157;

    @OriginalMember(owner = "client!jm", name = "a", descriptor = "I")
    public static int crossY = 0;

    @OriginalMember(owner = "client!jm", name = "p", descriptor = "Z")
    public static boolean aBoolean371 = false;

    @OriginalMember(owner = "client!jm", name = "a", descriptor = "(Lclient!ge;B)Lclient!qba;")
    public static TextureShapeLine readLine(@OriginalArg(0) Packet packet) {
        return new TextureShapeLine(packet.g2s(), packet.g2s(), packet.g2s(), packet.g2s(), packet.g3(), packet.g1());
    }

    @OriginalMember(owner = "client!jm", name = "a", descriptor = "(I)V")
    public static void resetTileFlags() {
        for (@Pc(12) int level = 0; level < Static280.tileFlags.length; level++) {
            for (@Pc(15) int x = 0; x < Static280.tileFlags[0].length; x++) {
                for (@Pc(18) int z = 0; z < Static280.tileFlags[0][0].length; z++) {
                    Static280.tileFlags[level][x][z] = 0;
                }
            }
        }
    }
}

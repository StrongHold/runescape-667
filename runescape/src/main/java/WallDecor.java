import com.jagex.Entity;
import com.jagex.game.Location;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!tla")
public abstract class WallDecor extends Entity implements Location {

    @OriginalMember(owner = "client!tla", name = "B", descriptor = "S")
    public short aShort101;

    @OriginalMember(owner = "client!tla", name = "y", descriptor = "S")
    public short aShort102;

    @OriginalMember(owner = "client!tla", name = "<init>", descriptor = "(IIIIIII)V")
    protected WallDecor(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int level, @OriginalArg(4) int virtualLevel, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        super.level = (byte) level;
        this.aShort101 = (short) arg5;
        super.z = z;
        this.aShort102 = (short) arg6;
        super.x = x;
        super.virtualLevel = (byte) virtualLevel;
        super.y = y;
    }

    @OriginalMember(owner = "client!tla", name = "a", descriptor = "(BLclient!ha;)Z")
    @Override
    public final boolean method9284(@OriginalArg(0) byte arg0, @OriginalArg(1) Toolkit arg1) {
        if (arg0 != 59) {
            this.aShort101 = -126;
        }
        return Static282.method3976(this.getMinY(2), super.virtualLevel, super.x >> EnvironmentLight.tileShift, super.z >> EnvironmentLight.tileShift);
    }

    @OriginalMember(owner = "client!tla", name = "j", descriptor = "(I)V")
    @Override
    public final void stopSharingLight(@OriginalArg(0) int arg0) {
        if (arg0 == 27811) {
            throw new IllegalStateException();
        }
    }

    @OriginalMember(owner = "client!tla", name = "a", descriptor = "(IZLclient!ha;IBILclient!eo;)V")
    @Override
    public final void shareLight(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) Entity arg6) {
        if (arg4 < 101) {
            CutsceneManager.cutsceneFadeGreen = -26;
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!tla", name = "i", descriptor = "(I)Z")
    @Override
    public final boolean method9290(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            Static622.method6854(null, 109, null, (byte) 91);
        }
        return false;
    }

    @OriginalMember(owner = "client!tla", name = "a", descriptor = "([Lclient!lca;I)I")
    @Override
    public final int method9288(@OriginalArg(0) PointLight[] arg0) {
        return this.findLightsAt(arg0, super.z >> EnvironmentLight.tileShift, super.x >> EnvironmentLight.tileShift);
    }

    @OriginalMember(owner = "client!tla", name = "g", descriptor = "(I)Z")
    @Override
    public final boolean method9275() {
        return Static258.aBooleanArrayArray3[(super.x >> EnvironmentLight.tileShift) + Static35.anInt813 - Static403.anInt6246][(super.z >> EnvironmentLight.tileShift) + Static35.anInt813 - Static550.anInt8271];
    }
}

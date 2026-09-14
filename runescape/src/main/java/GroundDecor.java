import com.jagex.Entity;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!eia")
public abstract class GroundDecor extends Entity {

    @OriginalMember(owner = "client!eia", name = "z", descriptor = "S")
    public short offsetY;

    @OriginalMember(owner = "client!eia", name = "<init>", descriptor = "(IIIIII)V")
    protected GroundDecor(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int level, @OriginalArg(4) int virtualLevel, @OriginalArg(5) int offsetY) {
        super.level = (byte) level;
        super.y = y;
        super.z = z;
        this.offsetY = (short) offsetY;
        super.virtualLevel = (byte) virtualLevel;
        super.x = x;
    }

    @OriginalMember(owner = "client!eia", name = "a", descriptor = "(BLclient!ha;)Z")
    @Override
    public final boolean method9284(@OriginalArg(0) byte arg0, @OriginalArg(1) Toolkit arg1) {
        if (arg0 != 59) {
            this.offsetY = -95;
        }
        return Static588.isTileOccluded(super.z >> EnvironmentLight.anInt1066, super.virtualLevel, super.x >> EnvironmentLight.anInt1066);
    }

    @OriginalMember(owner = "client!eia", name = "g", descriptor = "(I)Z")
    @Override
    public final boolean method9275() {
        return Static258.aBooleanArrayArray3[Static35.anInt813 + (super.x >> EnvironmentLight.anInt1066) - Static403.anInt6246][Static35.anInt813 + (super.z >> EnvironmentLight.anInt1066) - Static550.anInt8271];
    }

    @OriginalMember(owner = "client!eia", name = "a", descriptor = "([Lclient!lca;I)I")
    @Override
    public final int method9288(@OriginalArg(0) PointLight[] arg0) {
        return this.findLightsAt(arg0, super.z >> EnvironmentLight.anInt1066, super.x >> EnvironmentLight.anInt1066);
    }
}

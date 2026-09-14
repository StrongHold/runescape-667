import com.jagex.Entity;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kp")
public abstract class Wall extends Entity {

    @OriginalMember(owner = "client!kp", name = "D", descriptor = "S")
    public short sideMask;

    @OriginalMember(owner = "client!kp", name = "<init>", descriptor = "(IIIIII)V")
    protected Wall(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int level, @OriginalArg(4) int virtualLevel, @OriginalArg(5) int sideMask) {
        this.sideMask = (short) sideMask;
        super.y = y;
        super.level = (byte) level;
        super.z = z;
        super.x = x;
        super.virtualLevel = (byte) virtualLevel;
    }

    @OriginalMember(owner = "client!kp", name = "a", descriptor = "(BLclient!ha;)Z")
    @Override
    public final boolean method9284(@OriginalArg(0) byte arg0, @OriginalArg(1) Toolkit toolkit) {
        if (arg0 != 59) {
            this.sideMask = -17;
        }
        return Static73.isWallOccluded(super.x >> EnvironmentLight.anInt1066, super.z >> EnvironmentLight.anInt1066, this, super.virtualLevel);
    }

    @OriginalMember(owner = "client!kp", name = "a", descriptor = "([Lclient!lca;I)I")
    @Override
    public final int method9288(@OriginalArg(0) PointLight[] lights) {
        @Pc(10) int localX = super.x >> EnvironmentLight.anInt1066;
        @Pc(21) int localZ = super.z >> EnvironmentLight.anInt1066;
        @Pc(23) int directionIndex = 0;
        if (Static403.anInt6246 == localX) {
            directionIndex++;
        } else if (Static403.anInt6246 < localX) {
            directionIndex += 2;
        }
        if (localZ == Static550.anInt8271) {
            directionIndex += 3;
        } else if (Static550.anInt8271 > localZ) {
            directionIndex += 6;
        }
        @Pc(71) int facingSides = Static4.anIntArray15[directionIndex];
        if ((this.sideMask & facingSides) != 0) {
            return this.findLightsAt(lights, localZ, localX);
        } else if (this.sideMask == 1 && localX > 0) {
            return this.findLightsAt(lights, localZ, localX - 1);
        } else if (this.sideMask == 4 && Static619.tileMaxX >= localX) {
            return this.findLightsAt(lights, localZ, localX + 1);
        } else if (this.sideMask == 8 && localZ > 0) {
            return this.findLightsAt(lights, localZ - 1, localX);
        } else if (this.sideMask == 2 && Static662.tileMaxZ >= localZ) {
            return this.findLightsAt(lights, localZ + 1, localX);
        } else if (this.sideMask == 16 && localX > 0 && localZ <= Static662.tileMaxZ) {
            return this.findLightsAt(lights, localZ + 1, localX + -1);
        } else if (this.sideMask == 32 && localX <= Static619.tileMaxX && Static662.tileMaxZ >= localZ) {
            return this.findLightsAt(lights, localZ + 1, localX + 1);
        } else if (this.sideMask == 128 && localX > 0 && localZ > 0) {
            return this.findLightsAt(lights, localZ - 1, localX + -1);
        } else if (this.sideMask == 64 && Static619.tileMaxX >= localX && localZ > 0) {
            return this.findLightsAt(lights, localZ - 1, localX + 1);
        } else {
            throw new RuntimeException("");
        }
    }

    @OriginalMember(owner = "client!kp", name = "g", descriptor = "(I)Z")
    @Override
    public final boolean method9275() {
        return Static258.aBooleanArrayArray3[(super.x >> EnvironmentLight.anInt1066) + Static35.anInt813 - Static403.anInt6246][Static35.anInt813 + (super.z >> EnvironmentLight.anInt1066) - Static550.anInt8271];
    }
}

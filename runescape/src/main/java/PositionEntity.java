import com.jagex.Client;
import com.jagex.Entity;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qf")
public abstract class PositionEntity extends Entity {

    @OriginalMember(owner = "client!qf", name = "y", descriptor = "S")
    public short z1;

    @OriginalMember(owner = "client!qf", name = "E", descriptor = "S")
    public short z2;

    @OriginalMember(owner = "client!qf", name = "z", descriptor = "Z")
    public final boolean aBoolean815;

    @OriginalMember(owner = "client!qf", name = "I", descriptor = "B")
    public final byte aByte145;

    @OriginalMember(owner = "client!qf", name = "w", descriptor = "S")
    public short x2;

    @OriginalMember(owner = "client!qf", name = "A", descriptor = "S")
    public short x1;

    @OriginalMember(owner = "client!qf", name = "<init>", descriptor = "(IIIIIIIIIZB)V")
    protected PositionEntity(@OriginalArg(0) int level, @OriginalArg(1) int virtualLevel, @OriginalArg(2) int x, @OriginalArg(3) int y, @OriginalArg(4) int z, @OriginalArg(5) int x1, @OriginalArg(6) int x2, @OriginalArg(7) int z1, @OriginalArg(8) int z2, @OriginalArg(9) boolean arg9, @OriginalArg(10) byte arg10) {
        this.z1 = (short) z1;
        super.y = y;
        super.level = (byte) level;
        this.z2 = (short) z2;
        super.virtualLevel = (byte) virtualLevel;
        super.z = z;
        super.x = x;
        this.aBoolean815 = arg9;
        this.aByte145 = arg10;
        this.x2 = (short) x2;
        this.x1 = (short) x1;
    }

    @OriginalMember(owner = "client!qf", name = "a", descriptor = "([Lclient!lca;I)I")
    @Override
    public final int method9288(@OriginalArg(0) PointLight[] arg0) {
        @Pc(7) int local7 = 0;
        @Pc(21) int local21;
        @Pc(44) int local44;
        @Pc(57) int local57;
        label112:
        for (@Pc(16) int local16 = this.x1; local16 <= this.x2; local16++) {
            label110:
            for (local21 = this.z1; local21 <= this.z2; local21++) {
                @Pc(32) long local32 = Client.tileLightFlags[super.level][local16][local21];
                @Pc(34) long local34 = 0L;
                while (true) {
                    label105:
                    while (true) {
                        if (local34 > 48L) {
                            continue label110;
                        }
                        local44 = (int) (local32 >>> (int) local34 & 0xFFFFL);
                        if (local44 <= 0) {
                            continue label110;
                        }
                        @Pc(55) EnvironmentLight local55 = EnvironmentLight.aEnvironmentLightArray1[local44 - 1];
                        for (local57 = 0; local57 < local7; local57++) {
                            if (arg0[local57] == local55.light) {
                                local34 += 16L;
                                continue label105;
                            }
                        }
                        arg0[local7++] = local55.light;
                        if (local7 == 4) {
                            break label112;
                        }
                        local34 += 16L;
                    }
                }
            }
        }
        for (local21 = local7; local21 < 4; local21++) {
            arg0[local21] = null;
        }
        if (this.aByte145 != 0) {
            local44 = this.x1 - Static403.anInt6246;
            @Pc(163) int local163 = this.z1 - Static550.anInt8271;
            @Pc(188) int local188;
            @Pc(191) short local191;
            @Pc(183) short local183;
            @Pc(180) int local180;
            if (this.aByte145 == 1) {
                if (local44 >= local163) {
                    local188 = this.z1 + 1;
                    local183 = this.x1;
                    local180 = this.x1 - 1;
                    local191 = this.z1;
                } else {
                    local188 = this.z1 - 1;
                    local183 = this.x1;
                    local191 = this.z1;
                    local180 = this.x1 + 1;
                }
            } else if (local163 > -local44) {
                local180 = this.x1 - 1;
                local183 = this.x1;
                local188 = this.z1 - 1;
                local191 = this.z1;
            } else {
                local180 = this.x1 + 1;
                local183 = this.x1;
                local188 = this.z1 + 1;
                local191 = this.z1;
            }
            label76:
            for (local57 = 0; local57 < local7; local57++) {
                @Pc(267) long local267 = Client.tileLightFlags[super.level][local183][local188];
                @Pc(278) EnvironmentLight local278;
                while (local267 != 0L) {
                    local278 = EnvironmentLight.aEnvironmentLightArray1[(int) ((local267 & 0xFFFFL) - 1L)];
                    local267 >>>= 0x10;
                    if (arg0[local57] == local278.light) {
                        continue label76;
                    }
                }
                local267 = Client.tileLightFlags[super.level][local180][local191];
                while (local267 != 0L) {
                    local278 = EnvironmentLight.aEnvironmentLightArray1[(int) ((local267 & 0xFFFFL) - 1L)];
                    local267 >>>= 0x10;
                    if (local278.light == arg0[local57]) {
                        continue label76;
                    }
                }
                for (@Pc(342) int local342 = local57; local342 < local7 - 1; local342++) {
                    arg0[local342] = arg0[local342 + 1];
                }
                local7--;
            }
        }
        return local7;
    }

    @OriginalMember(owner = "client!qf", name = "a", descriptor = "(BLclient!ha;)Z")
    @Override
    public final boolean method9284(@OriginalArg(0) byte arg0, @OriginalArg(1) Toolkit arg1) {
        if (arg0 != 59) {
            this.method9284((byte) -4, null);
        }
        return Static15.isTileRangeOccluded(this.z1, this.x2, this.getCylinder(arg1, -115), this.z2, this.x1, super.virtualLevel);
    }

    @OriginalMember(owner = "client!qf", name = "g", descriptor = "(I)Z")
    @Override
    public final boolean method9275() {
        for (@Pc(16) int local16 = this.x1; local16 <= this.x2; local16++) {
            for (@Pc(23) int local23 = this.z1; local23 <= this.z2; local23++) {
                @Pc(32) int local32 = Static35.anInt813 + local16 - Static403.anInt6246;
                if (local32 >= 0 && local32 < Static258.aBooleanArrayArray3.length) {
                    @Pc(58) int local58 = Static35.anInt813 + local23 - Static550.anInt8271;
                    if (local58 >= 0 && local58 < Static258.aBooleanArrayArray3.length && Static258.aBooleanArrayArray3[local32][local58]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @OriginalMember(owner = "client!qf", name = "l", descriptor = "(I)V")
    public void updateBounds() {
    }
}

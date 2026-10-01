import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!dda")
public final class LocOccluder {

    @OriginalMember(owner = "client!dda", name = "e", descriptor = "[I")
    public final int[] anIntArray188 = new int[4];

    @OriginalMember(owner = "client!dda", name = "m", descriptor = "B")
    public final byte aByte44;

    @OriginalMember(owner = "client!dda", name = "h", descriptor = "B")
    public final byte aByte43;

    @OriginalMember(owner = "client!dda", name = "k", descriptor = "[I")
    public final int[] anIntArray186;

    @OriginalMember(owner = "client!dda", name = "j", descriptor = "[I")
    public final int[] anIntArray185;

    @OriginalMember(owner = "client!dda", name = "f", descriptor = "S")
    public final short aShort26;

    @OriginalMember(owner = "client!dda", name = "b", descriptor = "S")
    public final short aShort24;

    @OriginalMember(owner = "client!dda", name = "d", descriptor = "S")
    public final short aShort23;

    @OriginalMember(owner = "client!dda", name = "a", descriptor = "[S")
    public final short[] aShortArray17;

    @OriginalMember(owner = "client!dda", name = "i", descriptor = "S")
    public final short aShort25;

    @OriginalMember(owner = "client!dda", name = "g", descriptor = "[S")
    public final short[] aShortArray18;

    @OriginalMember(owner = "client!dda", name = "n", descriptor = "[S")
    public final short[] aShortArray19;

    @OriginalMember(owner = "client!dda", name = "<init>", descriptor = "(IIIIIIIIIIIIII)V")
    public LocOccluder(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8, @OriginalArg(9) int arg9, @OriginalArg(10) int arg10, @OriginalArg(11) int arg11, @OriginalArg(12) int arg12, @OriginalArg(13) int arg13) {
        this.aByte44 = (byte) arg1;
        this.aByte43 = (byte) arg0;
        this.anIntArray186 = new int[4];
        this.anIntArray185 = new int[4];
        this.anIntArray185[3] = arg5;
        this.anIntArray185[0] = arg2;
        this.anIntArray185[2] = arg4;
        this.anIntArray185[1] = arg3;
        this.anIntArray186[3] = arg9;
        this.anIntArray186[0] = arg6;
        this.anIntArray186[2] = arg8;
        this.anIntArray186[1] = arg7;
        this.anIntArray188[0] = arg10;
        this.anIntArray188[2] = arg12;
        this.anIntArray188[1] = arg11;
        this.aShort26 = (short) (arg2 >> EnvironmentLight.tileShift);
        this.anIntArray188[3] = arg13;
        this.aShort24 = (short) (arg4 >> EnvironmentLight.tileShift);
        this.aShort23 = (short) (arg10 >> EnvironmentLight.tileShift);
        this.aShortArray17 = new short[4];
        this.aShort25 = (short) (arg12 >> EnvironmentLight.tileShift);
        this.aShortArray18 = new short[4];
        this.aShortArray19 = new short[4];
    }
}

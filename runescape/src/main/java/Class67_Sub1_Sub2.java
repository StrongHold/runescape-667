import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!us")
public final class Class67_Sub1_Sub2 extends Class67_Sub1 {

    @OriginalMember(owner = "client!us", name = "i", descriptor = "Lclient!ec;")
    public Class93_Sub1 aClass93_Sub1_2;

    @OriginalMember(owner = "client!us", name = "o", descriptor = "I")
    public final int anInt9796;

    @OriginalMember(owner = "client!us", name = "p", descriptor = "I")
    public final int anInt9802;

    @OriginalMember(owner = "client!us", name = "s", descriptor = "I")
    public final int anInt9799;

    @OriginalMember(owner = "client!us", name = "j", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_37;

    @OriginalMember(owner = "client!us", name = "t", descriptor = "I")
    public final int anInt9804;

    @OriginalMember(owner = "client!us", name = "l", descriptor = "I")
    public final int anInt9803;

    @OriginalMember(owner = "client!us", name = "r", descriptor = "I")
    public final int anInt9798;

    @OriginalMember(owner = "client!us", name = "<init>", descriptor = "(Lclient!qha;IIIIII)V")
    public Class67_Sub1_Sub2(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        this.anInt9796 = arg3;
        this.anInt9802 = arg2;
        this.anInt9799 = arg4;
        this.aClass19_Sub3_37 = arg0;
        this.anInt9804 = arg6;
        this.anInt9803 = arg1;
        this.anInt9798 = arg5;
    }

    @OriginalMember(owner = "client!us", name = "a", descriptor = "(Z)Lclient!ec;")
    @Override
    public Class93_Sub1 method8589() {
        if (this.aClass93_Sub1_2 == null) {
            Static319.anIntArray384[0] = this.anInt9803;
            Static319.anIntArray384[2] = this.anInt9796;
            Static319.anIntArray384[3] = this.anInt9799;
            Static319.anIntArray384[5] = this.anInt9804;
            Static319.anIntArray384[4] = this.anInt9798;
            @Pc(37) TextureSource local37 = this.aClass19_Sub3_37.textureSource;
            Static319.anIntArray384[1] = this.anInt9802;
            @Pc(44) boolean local44 = false;
            @Pc(46) int local46 = 0;
            for (@Pc(48) int local48 = 0; local48 < 6; local48++) {
                if (!local37.textureAvailable(Static319.anIntArray384[local48])) {
                    return null;
                }
                @Pc(69) TextureMetrics local69 = local37.getMetrics(Static319.anIntArray384[local48]);
                @Pc(77) int local77 = local69.small ? 64 : 128;
                if (local69.mipmap > 0) {
                    local44 = true;
                }
                if (local46 < local77) {
                    local46 = local77;
                }
            }
            for (@Pc(99) int local99 = 0; local99 < 6; local99++) {
                Static304.anIntArrayArray119[local99] = local37.rgbOutput(local46, false, local46, Static319.anIntArray384[local99], 1.0F);
            }
            this.aClass93_Sub1_2 = new Class93_Sub1(this.aClass19_Sub3_37, 6407, local46, local44, Static304.anIntArrayArray119);
        }
        return this.aClass93_Sub1_2;
    }
}

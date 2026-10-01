import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!hm")
public final class Class169 {

    @OriginalMember(owner = "client!hm", name = "f", descriptor = "Lclient!dla;")
    public final ReferenceCache aReferenceCache_85 = new ReferenceCache(256);

    @OriginalMember(owner = "client!hm", name = "a", descriptor = "Lclient!d;")
    public final TextureSource anTextureSource_4;

    @OriginalMember(owner = "client!hm", name = "c", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_17;

    @OriginalMember(owner = "client!hm", name = "<init>", descriptor = "(Lclient!qha;Lclient!d;)V")
    public Class169(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) TextureSource arg1) {
        this.anTextureSource_4 = arg1;
        this.aClass19_Sub3_17 = arg0;
    }

    @OriginalMember(owner = "client!hm", name = "b", descriptor = "(I)V")
    public void method3527() {
        this.aReferenceCache_85.reset();
    }

    @OriginalMember(owner = "client!hm", name = "a", descriptor = "(Z)V")
    public void method3528() {
        this.aReferenceCache_85.clean(5);
    }

    @OriginalMember(owner = "client!hm", name = "a", descriptor = "(BI)Lclient!rq;")
    public Class93_Sub2 method3529(@OriginalArg(1) int arg0) {
        @Pc(10) Object local10 = this.aReferenceCache_85.get(arg0);
        if (local10 != null) {
            return (Class93_Sub2) local10;
        } else if (this.anTextureSource_4.textureAvailable(arg0)) {
            @Pc(33) TextureMetrics local33 = this.anTextureSource_4.getMetrics(arg0);
            @Pc(52) int local52 = local33.small ? 64 : this.aClass19_Sub3_17.anInt7981;
            @Pc(90) Class93_Sub2 local90;
            if (local33.hdr && this.aClass19_Sub3_17.bloom()) {
                @Pc(71) float[] local71 = this.anTextureSource_4.floatArgbOutput(local52, arg0, 0.7F, local52);
                local90 = new Class93_Sub2(this.aClass19_Sub3_17, 3553, 34842, local52, local52, local33.mipmap != 0, local71, 6408);
            } else {
                @Pc(114) int[] local114;
                if (local33.alphaBlendMode != 2 && Static501.method6715(local33.effectType)) {
                    local114 = this.anTextureSource_4.rgbOutput(local52, true, local52, arg0, 0.7F);
                } else {
                    local114 = this.anTextureSource_4.argbOutput(0.7F, arg0, local52, local52);
                }
                local90 = new Class93_Sub2(this.aClass19_Sub3_17, 3553, 6408, local52, local52, local33.mipmap != 0, local114, 0, 0, false);
            }
            local90.method2946(local33.repeatsU, local33.repeatsV);
            this.aReferenceCache_85.put(local90, arg0);
            return local90;
        } else {
            return null;
        }
    }
}

import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!sf")
public final class Class334 {

    @OriginalMember(owner = "client!sf", name = "f", descriptor = "Lclient!dla;")
    public final ReferenceCache aReferenceCache_187 = new ReferenceCache(256);

    @OriginalMember(owner = "client!sf", name = "c", descriptor = "Lclient!d;")
    public final TextureSource anTextureSource_9;

    @OriginalMember(owner = "client!sf", name = "j", descriptor = "Lclient!am;")
    public final NativeToolkit aClass19_Sub1_20;

    @OriginalMember(owner = "client!sf", name = "<init>", descriptor = "(Lclient!am;Lclient!d;)V")
    public Class334(@OriginalArg(0) NativeToolkit arg0, @OriginalArg(1) TextureSource arg1) {
        this.anTextureSource_9 = arg1;
        this.aClass19_Sub1_20 = arg0;
    }

    @OriginalMember(owner = "client!sf", name = "b", descriptor = "(B)V")
    public void method7653() {
        this.aReferenceCache_187.reset();
    }

    @OriginalMember(owner = "client!sf", name = "a", descriptor = "(BI)Lclient!og;")
    public Interface18 method7654(@OriginalArg(1) int arg0) {
        @Pc(12) Object local12 = this.aReferenceCache_187.get(arg0);
        if (local12 != null) {
            return (Interface18) local12;
        } else if (this.anTextureSource_9.textureAvailable(arg0)) {
            @Pc(35) TextureMetrics local35 = this.anTextureSource_9.getMetrics(arg0);
            @Pc(45) int local45 = local35.small ? 64 : this.aClass19_Sub1_20.anInt9183;
            @Pc(105) Interface18 local105;
            if (local35.hdr && this.aClass19_Sub1_20.bloom()) {
                @Pc(116) float[] local116 = this.anTextureSource_9.floatArgbOutput(local45, arg0, 0.7F, local45);
                local105 = this.aClass19_Sub1_20.method8121(local45, local116, Static172.aClass92_8, local35.mipmap != 0, local45);
            } else {
                @Pc(79) int[] local79;
                if (local35.alphaBlendMode != 2 && Static501.method6715(local35.effectType)) {
                    local79 = this.anTextureSource_9.rgbOutput(local45, true, local45, arg0, 0.7F);
                } else {
                    local79 = this.anTextureSource_9.argbOutput(0.7F, arg0, local45, local45);
                }
                local105 = this.aClass19_Sub1_20.method8034(local35.mipmap != 0, local45, local45, local79);
            }
            local105.method9052(local35.repeatsU, local35.repeatsV);
            this.aReferenceCache_187.put(local105, arg0);
            return local105;
        } else {
            return null;
        }
    }

    @OriginalMember(owner = "client!sf", name = "a", descriptor = "(B)V")
    public void method7655() {
        this.aReferenceCache_187.clean(5);
    }
}

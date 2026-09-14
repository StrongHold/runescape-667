import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!su")
public final class ParticlesOption extends Option {

    @OriginalMember(owner = "client!su", name = "<init>", descriptor = "(Lclient!kv;)V")
    public ParticlesOption(@OriginalArg(0) ClientOptions options) {
        super(options);
        ParticleManager.setOption(super.value);
    }

    @OriginalMember(owner = "client!su", name = "<init>", descriptor = "(ILclient!kv;)V")
    public ParticlesOption(@OriginalArg(0) int value, @OriginalArg(1) ClientOptions options) {
        super(value, options);
        ParticleManager.setOption(super.value);
    }

    @OriginalMember(owner = "client!su", name = "a", descriptor = "(I)I")
    @Override
    protected int getDefaultValue() {
        return super.options.getEnvironment().getHeapSize() < 96 ? 0 : 2;
    }

    @OriginalMember(owner = "client!su", name = "a", descriptor = "(IB)I")
    @Override
    public int canSet(@OriginalArg(0) int value) {
        return super.options.getEnvironment().getHeapSize() < 96 ? 3 : 1;
    }

    @OriginalMember(owner = "client!su", name = "a", descriptor = "(ZI)V")
    @Override
    protected void setValue(@OriginalArg(1) int value) {
        super.value = value;
        ParticleManager.setOption(super.value);
    }

    @OriginalMember(owner = "client!su", name = "c", descriptor = "(B)Z")
    public boolean canMod() {
        return super.options.getEnvironment().getHeapSize() >= 96;
    }

    @OriginalMember(owner = "client!su", name = "a", descriptor = "(Z)I")
    public int getValue() {
        return super.value;
    }

    @OriginalMember(owner = "client!su", name = "a", descriptor = "(B)V")
    @Override
    public void validate() {
        if (super.options.getEnvironment().getHeapSize() < 96) {
            super.value = 0;
        }
        if (super.value < 0 || super.value > 2) {
            super.value = this.getDefaultValue();
        }
    }
}

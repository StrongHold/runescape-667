import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Owns one {@link TextureEffect} per {@code TextureMetrics.effectType} and switches between them as textures change.
 */
@OriginalClass("client!eg")
public final class Class98 {

    private static final int EFFECT_NONE = 0;

    private static final int EFFECT_NORMAL_MAP_SPECULAR = 1;

    private static final int EFFECT_TURBULENT_WATER = 2;

    private static final int EFFECT_UNDERWATER = 3;

    private static final int EFFECT_FIXED_FUNCTION_WATER = 4;

    private static final int EFFECT_FLOWING_WATER = 5;

    private static final int EFFECT_UNLIT = 6;

    private static final int EFFECT_REFLECTION_MAP = 7;

    private static final int EFFECT_SHADER_WATER = 8;

    private static final int EFFECT_SHADER_WAVE_WATER = 9;

    private static final int EFFECT_COUNT = 10;

    /**
     * Packed into the active effect alongside the effect type so that a change of lighting restarts the effect.
     */
    private static final int LIT_FLAG = Integer.MIN_VALUE;

    private static final int EFFECT_MASK = Integer.MAX_VALUE;

    @OriginalMember(owner = "client!eg", name = "a", descriptor = "I")
    public int activeEffect = EFFECT_NONE;

    @OriginalMember(owner = "client!eg", name = "c", descriptor = "I")
    public int activeParam2 = 0;

    @OriginalMember(owner = "client!eg", name = "k", descriptor = "I")
    public int activeParam1 = 0;

    @OriginalMember(owner = "client!eg", name = "g", descriptor = "Lclient!qha;")
    public final GlToolkit toolkit;

    @OriginalMember(owner = "client!eg", name = "f", descriptor = "Lclient!sa;")
    public final Class329 textures;

    @OriginalMember(owner = "client!eg", name = "l", descriptor = "[Lclient!ua;")
    public final TextureEffect[] effects;

    @OriginalMember(owner = "client!eg", name = "h", descriptor = "Lclient!nia;")
    public final UnderwaterEffect aClass101_Sub6_1;

    @OriginalMember(owner = "client!eg", name = "<init>", descriptor = "(Lclient!qha;)V")
    public Class98(@OriginalArg(0) GlToolkit toolkit) {
        this.toolkit = toolkit;
        this.textures = new Class329(toolkit);
        this.effects = new TextureEffect[EFFECT_COUNT];
        this.effects[EFFECT_NORMAL_MAP_SPECULAR] = new NormalMapSpecularEffect(toolkit);
        this.effects[EFFECT_TURBULENT_WATER] = new TurbulentWaterEffect(toolkit, this.textures);
        this.effects[EFFECT_FIXED_FUNCTION_WATER] = new FixedFunctionWaterEffect(toolkit, this.textures);
        this.effects[EFFECT_FLOWING_WATER] = new FlowingWaterEffect(toolkit, this.textures);
        this.effects[EFFECT_UNLIT] = new UnlitEffect(toolkit);
        this.effects[EFFECT_REFLECTION_MAP] = new ReflectionMapEffect(toolkit);
        this.effects[EFFECT_UNDERWATER] = this.aClass101_Sub6_1 = new UnderwaterEffect(toolkit);
        this.effects[EFFECT_SHADER_WATER] = new ShaderWaterEffect(toolkit, this.textures);
        this.effects[EFFECT_SHADER_WAVE_WATER] = new ShaderWaveWaterEffect(toolkit, this.textures);
        if (!this.effects[EFFECT_SHADER_WATER].isSupported()) {
            this.effects[EFFECT_SHADER_WATER] = this.effects[EFFECT_FIXED_FUNCTION_WATER];
        }
        if (!this.effects[EFFECT_SHADER_WAVE_WATER].isSupported()) {
            this.effects[EFFECT_SHADER_WAVE_WATER] = this.effects[EFFECT_SHADER_WATER];
        }
    }

    @OriginalMember(owner = "client!eg", name = "a", descriptor = "(IB)Z")
    public boolean method2357() {
        return this.effects[EFFECT_UNDERWATER].isSupported();
    }

    @OriginalMember(owner = "client!eg", name = "a", descriptor = "(BILclient!kd;)Z")
    public boolean method2359(@OriginalArg(1) int colourOp, @OriginalArg(2) Class93 texture) {
        if (this.activeEffect == EFFECT_NONE) {
            return false;
        } else {
            this.effects[EFFECT_MASK & this.activeEffect].bindTexture(texture, colourOp);
            return true;
        }
    }

    @OriginalMember(owner = "client!eg", name = "a", descriptor = "(IIZZII)V")
    public void method2360(@OriginalArg(1) int effectParam2, @OriginalArg(2) boolean waterPlaneActive, @OriginalArg(3) boolean lit, @OriginalArg(4) int effectParam1, @OriginalArg(5) int effectType) {
        @Pc(9) boolean waterSupported = waterPlaneActive & this.toolkit.method7990();
        boolean fallback = !waterSupported && (effectType == EFFECT_FIXED_FUNCTION_WATER || effectType == EFFECT_SHADER_WATER || effectType == EFFECT_SHADER_WAVE_WATER);
        int param2 = fallback && effectType == EFFECT_FIXED_FUNCTION_WATER ? effectParam1 : effectParam2;
        int effect = fallback ? EFFECT_TURBULENT_WATER : effectType;
        int requested = effect != EFFECT_NONE && lit ? effect | LIT_FLAG : effect;
        if (requested != this.activeEffect) {
            if (this.activeEffect != EFFECT_NONE) {
                this.effects[this.activeEffect & EFFECT_MASK].disable();
            }
            if (requested != EFFECT_NONE) {
                this.effects[EFFECT_MASK & requested].enable(lit);
                this.effects[requested & EFFECT_MASK].applyTextureCombine(lit);
                this.effects[requested & EFFECT_MASK].setEffectParams(param2, effectParam1);
            }
            this.activeParam2 = param2;
            this.activeParam1 = effectParam1;
            this.activeEffect = requested;
        } else if (this.activeEffect != EFFECT_NONE) {
            this.effects[this.activeEffect & EFFECT_MASK].applyTextureCombine(lit);
            if (this.activeParam1 != effectParam1 || this.activeParam2 != param2) {
                this.effects[this.activeEffect & EFFECT_MASK].setEffectParams(param2, effectParam1);
                this.activeParam2 = param2;
                this.activeParam1 = effectParam1;
            }
        }
    }
}

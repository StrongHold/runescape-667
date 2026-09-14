import com.jagex.js5.js5;
import jagdx.IDirect3DDevice;
import jagdx.IDirect3DPixelShader;
import jagdx.IDirect3DVertexShader;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!rn")
public final class D3DEnvironmentMappedWaterPass extends RenderPass {

    @OriginalMember(owner = "client!rn", name = "p", descriptor = "Z")
    public boolean active;

    @OriginalMember(owner = "client!rn", name = "l", descriptor = "Lclient!kea;")
    public final D3DToolkit d3dToolkit;

    @OriginalMember(owner = "client!rn", name = "n", descriptor = "Lclient!ae;")
    public final Class7 water;

    @OriginalMember(owner = "client!rn", name = "j", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public final IDirect3DVertexShader vertexShader;

    @OriginalMember(owner = "client!rn", name = "k", descriptor = "Lclient!jagdx/IDirect3DPixelShader;")
    public final IDirect3DPixelShader pixelShader;

    @OriginalMember(owner = "client!rn", name = "o", descriptor = "Z")
    public final boolean supported;

    /**
     * Minor version 1.1 of the vertex shader model, the lowest the water shaders assemble against.
     */
    private static final int MIN_VERTEX_SHADER_VERSION = 257;

    private static final int VERTEX_SHADER_VERSION_MASK = 0xFFFF;

    private static final int VS_CONST_WORLD_VIEW_PROJECTION = 0;

    private static final int VS_CONST_VIEW = 4;

    private static final int VS_CONST_PROJECTION = 8;

    private static final int VS_CONST_TEXTURE_MATRIX = 12;

    private static final int VS_CONST_WAVE_TIME = 14;

    private static final int VS_CONST_WAVE_SCALE = 15;

    private static final int VS_CONST_FOG = 16;

    private static final int PS_CONST_FOG_COLOUR = 0;

    private static final int PS_CONST_SUN_DIRECTION = 1;

    private static final int PS_CONST_SUN_COLOUR = 2;

    private static final int PS_CONST_SUN_EXPONENT = 3;

    private static final int PS_CONST_BREAK_DEPTH = 4;

    private static final int PS_CONST_BREAK_OFFSET = 5;

    private static final int ENV_MAP_TEXTURE_UNIT = 1;

    private static final int NORMAL_TEXTURE_UNIT = 0;

    private static final int WAVE_CYCLE_MILLIS = 40000;

    private static final int WAVE_SPEED_MASK = 0x3;

    private static final int WAVE_SCALE_SHIFT = 3;

    private static final int WAVE_SCALE_MASK = 0x7;

    private static final float WAVE_SCALE_UNIT = 32.0F;

    private static final int BREAK_DEPTH_MASK = 0xFFFF;

    private static final int BREAK_OFFSET_SHIFT = 16;

    private static final int BREAK_OFFSET_MASK = 0x3;

    private static final float BREAK_OFFSET_UNIT = 8.0F;

    private static final float SUN_EXPONENT_MIN = 96.0F;

    private static final float SUN_EXPONENT_RANGE = 928.0F;

    private static final float MAX_BYTE = 255.0F;

    @OriginalMember(owner = "client!rn", name = "<init>", descriptor = "(Lclient!kea;Lclient!sb;Lclient!ae;)V")
    public D3DEnvironmentMappedWaterPass(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) js5 shaders, @OriginalArg(2) Class7 water) {
        super(toolkit);
        this.d3dToolkit = toolkit;
        this.water = water;
        if (shaders != null && this.toolkit.aBoolean685 && this.toolkit.aBoolean696 && (this.d3dToolkit.aD3DCAPS1.VertexShaderVersion & VERTEX_SHADER_VERSION_MASK) >= MIN_VERTEX_SHADER_VERSION) {
            this.vertexShader = this.d3dToolkit.anIDirect3DDevice1.b(shaders.getfile("dx", "environment_mapped_water_v"));
            this.pixelShader = this.d3dToolkit.anIDirect3DDevice1.a(shaders.getfile("dx", "environment_mapped_water_f"));
            this.supported = this.vertexShader != null && this.pixelShader != null && this.water.method115();
        } else {
            this.supported = false;
            this.pixelShader = null;
            this.vertexShader = null;
        }
    }

    @OriginalMember(owner = "client!rn", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        @Pc(4) Interface8 envMap = this.toolkit.method8145();
        if (this.supported && envMap != null) {
            @Pc(15) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            this.d3dToolkit.method4856(this.vertexShader);
            this.d3dToolkit.method4858(this.pixelShader);
            this.toolkit.method8138(ENV_MAP_TEXTURE_UNIT);
            this.toolkit.method8088(envMap);
            this.toolkit.method8138(NORMAL_TEXTURE_UNIT);
            this.toolkit.method8088(this.water.anInterface2_1);
            this.active = true;
            this.onCameraChanged();
            this.onTextureMatrixChanged();
            this.onProjectionChanged();
            this.onFogChanged();
            device.b(PS_CONST_SUN_DIRECTION, -this.toolkit.aFloatArray60[0], -this.toolkit.aFloatArray60[1], -this.toolkit.aFloatArray60[2], 0.0F);
            device.b(PS_CONST_SUN_COLOUR, this.toolkit.aFloat191, this.toolkit.aFloat184, this.toolkit.aFloat195, 1.0F);
            device.b(PS_CONST_SUN_EXPONENT, Math.abs(this.toolkit.aFloatArray60[1]) * SUN_EXPONENT_RANGE + SUN_EXPONENT_MIN, 0.0F, 0.0F, 0.0F);
        }
    }

    @OriginalMember(owner = "client!rn", name = "b", descriptor = "(I)V")
    @Override
    public void onCameraChanged() {
        if (this.active) {
            @Pc(8) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(13) Matrix_Sub1 view = this.d3dToolkit.method8118();
            @Pc(18) Matrix_Sub1 worldViewProjection = this.d3dToolkit.method8154();
            device.a(VS_CONST_WORLD_VIEW_PROJECTION, worldViewProjection.method1888(Static562.aFloatArray57));
            device.a(VS_CONST_VIEW, view.method1892(Static562.aFloatArray57));
        }
    }

    @OriginalMember(owner = "client!rn", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
    }

    @OriginalMember(owner = "client!rn", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.active) {
            this.d3dToolkit.method4856(null);
            this.d3dToolkit.method4858(null);
            this.toolkit.method8138(ENV_MAP_TEXTURE_UNIT);
            this.toolkit.method8088(null);
            this.toolkit.method8138(NORMAL_TEXTURE_UNIT);
            this.toolkit.method8088(null);
            this.active = false;
        }
    }

    @OriginalMember(owner = "client!rn", name = "c", descriptor = "(I)V")
    @Override
    public void onModelMatrixChanged() {
        if (this.active) {
            @Pc(6) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(11) Matrix_Sub1 worldViewProjection = this.d3dToolkit.method8154();
            device.a(VS_CONST_WORLD_VIEW_PROJECTION, worldViewProjection.method1888(Static562.aFloatArray57));
        }
    }

    @OriginalMember(owner = "client!rn", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
    }

    @OriginalMember(owner = "client!rn", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
        if (this.active) {
            @Pc(6) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(12) int waveSpeed = 0x1 << (effectParam1 & WAVE_SPEED_MASK);
            @Pc(23) float waveScale = (float) (0x1 << (effectParam1 >> WAVE_SCALE_SHIFT & WAVE_SCALE_MASK)) / WAVE_SCALE_UNIT;
            @Pc(27) int breakDepth = effectParam2 & BREAK_DEPTH_MASK;
            @Pc(36) float breakOffset = (float) (effectParam2 >> BREAK_OFFSET_SHIFT & BREAK_OFFSET_MASK) / BREAK_OFFSET_UNIT;
            device.a(VS_CONST_WAVE_TIME, (float) (waveSpeed * this.toolkit.anInt9164 % WAVE_CYCLE_MILLIS) / (float) WAVE_CYCLE_MILLIS, 0.0F, 0.0F, 0.0F);
            device.a(VS_CONST_WAVE_SCALE, waveScale, 0.0F, 0.0F, 0.0F);
            device.b(PS_CONST_BREAK_DEPTH, (float) breakDepth, 0.0F, 0.0F, 0.0F);
            device.b(PS_CONST_BREAK_OFFSET, breakOffset, 0.0F, 0.0F, 0.0F);
        }
    }

    @OriginalMember(owner = "client!rn", name = "d", descriptor = "(I)V")
    @Override
    public void onProjectionChanged() {
        if (this.active) {
            @Pc(14) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            device.a(VS_CONST_PROJECTION, this.toolkit.method8137(Static562.aFloatArray57));
        }
    }

    @OriginalMember(owner = "client!rn", name = "a", descriptor = "(B)V")
    @Override
    public void onFogChanged() {
        if (this.active) {
            @Pc(6) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            if (this.toolkit.anInt9175 > 0) {
                @Pc(17) float fogEnd = this.toolkit.aFloat192;
                @Pc(21) float fogStart = this.toolkit.aFloat189;
                device.a(VS_CONST_FOG, fogEnd, 1.0F / (fogEnd - fogStart), 0.0F, 0.0F);
            } else {
                device.a(VS_CONST_FOG, 0.0F, 0.0F, 0.0F, 0.0F);
            }
            device.b(PS_CONST_FOG_COLOUR, (float) (this.toolkit.anInt9146 >> 16 & 0xFF) / MAX_BYTE, (float) (this.toolkit.anInt9146 >> 8 & 0xFF) / MAX_BYTE, (float) (this.toolkit.anInt9146 & 0xFF) / MAX_BYTE, 0.0F);
        }
    }

    @OriginalMember(owner = "client!rn", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.supported;
    }

    @OriginalMember(owner = "client!rn", name = "a", descriptor = "(Z)V")
    @Override
    public void onTextureMatrixChanged() {
        if (this.active) {
            @Pc(8) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(13) Matrix_Sub1 textureMatrix = this.toolkit.method8066();
            device.SetVertexShaderConstantF(VS_CONST_TEXTURE_MATRIX, textureMatrix.method1898(Static562.aFloatArray57), 2);
        }
    }
}

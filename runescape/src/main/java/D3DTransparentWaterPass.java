import com.jagex.js5.js5;
import jagdx.IDirect3DDevice;
import jagdx.IDirect3DVertexShader;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qn")
public final class D3DTransparentWaterPass extends RenderPass {

    @OriginalMember(owner = "client!qn", name = "j", descriptor = "Lclient!kea;")
    public final D3DToolkit d3dToolkit;

    @OriginalMember(owner = "client!qn", name = "l", descriptor = "Lclient!ae;")
    public final Class7 water;

    @OriginalMember(owner = "client!qn", name = "m", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public final IDirect3DVertexShader vertexShader;

    /**
     * Minor version 1.1 of the vertex shader model, the lowest the water shader assembles against.
     */
    private static final int MIN_VERTEX_SHADER_VERSION = 257;

    private static final int VERTEX_SHADER_VERSION_MASK = 0xFFFF;

    private static final int VS_CONST_WORLD_VIEW_PROJECTION = 0;

    private static final int VS_CONST_PROJECTION = 4;

    private static final int VS_CONST_TEXTURE_MATRIX = 8;

    private static final int VS_CONST_FOG = 10;

    private static final int VS_CONST_WAVE_PHASE = 11;

    private static final int TEXTURE_MATRIX_FLOATS = 8;

    private static final float WAVE_TEXTURE_SCALE = 0.25F;

    private static final float UNDERWATER_FOG_DEPTH = 512.0F;

    private static final int WAVE_CYCLE_MILLIS = 4000;

    private static final int WAVE_FRAME_COUNT = 16;

    @OriginalMember(owner = "client!qn", name = "<init>", descriptor = "(Lclient!kea;Lclient!sb;Lclient!ae;)V")
    public D3DTransparentWaterPass(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) js5 shaders, @OriginalArg(2) Class7 water) {
        super(toolkit);
        this.d3dToolkit = toolkit;
        this.water = water;
        if (shaders != null && this.water.method116() && (this.d3dToolkit.aD3DCAPS1.VertexShaderVersion & VERTEX_SHADER_VERSION_MASK) >= MIN_VERTEX_SHADER_VERSION) {
            this.vertexShader = this.d3dToolkit.anIDirect3DDevice1.b(shaders.getfile("dx", "transparent_water"));
        } else {
            this.vertexShader = null;
        }
    }

    @OriginalMember(owner = "client!qn", name = "a", descriptor = "(B)V")
    @Override
    public void onFogChanged() {
        if (this.vertexShader != null) {
            @Pc(16) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            if (this.toolkit.anInt9175 > 0) {
                @Pc(27) float fogEnd = this.toolkit.aFloat192;
                @Pc(31) float fogStart = this.toolkit.aFloat189;
                @Pc(35) float underwaterStart = fogStart - UNDERWATER_FOG_DEPTH;
                device.a(VS_CONST_FOG, underwaterStart, 1.0F / (fogStart - underwaterStart), fogStart, 1.0F / (fogEnd - fogStart));
            } else {
                device.a(VS_CONST_FOG, 0.0F, 0.0F, 0.0F, 0.0F);
            }
            this.toolkit.method8112(this.toolkit.anInt9146);
        }
    }

    @OriginalMember(owner = "client!qn", name = "c", descriptor = "(I)V")
    @Override
    public void onModelMatrixChanged() {
        if (this.vertexShader != null) {
            @Pc(8) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(13) Matrix_Sub1 worldViewProjection = this.d3dToolkit.method8154();
            device.a(VS_CONST_WORLD_VIEW_PROJECTION, worldViewProjection.method1888(Static531.aFloatArray55));
        }
    }

    @OriginalMember(owner = "client!qn", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        this.toolkit.method8080(0, Static189.aClass168_2);
        this.toolkit.method8080(1, Static454.aClass168_5);
        this.toolkit.method8125(Static207.aClass168_4, false, true, 2);
        this.toolkit.method8140(false);
        this.d3dToolkit.method4856(this.vertexShader);
        this.onModelMatrixChanged();
        this.onTextureMatrixChanged();
        this.onProjectionChanged();
        this.onFogChanged();
    }

    @OriginalMember(owner = "client!qn", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
        this.toolkit.method8094(Static438.aClass121_5, Static725.aClass121_6);
    }

    @OriginalMember(owner = "client!qn", name = "d", descriptor = "(I)V")
    @Override
    public void onProjectionChanged() {
        if (this.vertexShader != null) {
            @Pc(7) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            device.a(VS_CONST_PROJECTION, this.toolkit.method8137(Static531.aFloatArray55));
        }
    }

    @OriginalMember(owner = "client!qn", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        this.d3dToolkit.method4856(null);
        this.toolkit.method8080(0, Static189.aClass168_2);
        this.toolkit.method8080(1, Static207.aClass168_4);
        this.toolkit.method8080(2, Static454.aClass168_5);
        this.toolkit.method8140(true);
    }

    @OriginalMember(owner = "client!qn", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
        @Pc(3) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
        if (this.water.aBoolean7) {
            @Pc(24) float phase = (float) (this.toolkit.anInt9164 % WAVE_CYCLE_MILLIS) / (float) WAVE_CYCLE_MILLIS;
            this.toolkit.method8088(this.water.anInterface2_2);
            device.a(VS_CONST_WAVE_PHASE, phase, 0.0F, 0.0F, 0.0F);
        } else {
            @Pc(50) int frame = this.toolkit.anInt9164 % WAVE_CYCLE_MILLIS * WAVE_FRAME_COUNT / WAVE_CYCLE_MILLIS;
            this.toolkit.method8088(this.water.anInterface18Array2[frame]);
            device.a(VS_CONST_WAVE_PHASE, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @OriginalMember(owner = "client!qn", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
    }

    @OriginalMember(owner = "client!qn", name = "a", descriptor = "(Z)V")
    @Override
    public void onTextureMatrixChanged() {
        if (this.vertexShader != null) {
            @Pc(16) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(21) Matrix_Sub1 textureMatrix = this.toolkit.method8066();
            textureMatrix.method1898(Static531.aFloatArray55);
            for (int index = 0; index < TEXTURE_MATRIX_FLOATS; index++) {
                Static531.aFloatArray55[index] *= WAVE_TEXTURE_SCALE;
            }
            device.SetVertexShaderConstantF(VS_CONST_TEXTURE_MATRIX, Static531.aFloatArray55, 2);
        }
    }

    @OriginalMember(owner = "client!qn", name = "b", descriptor = "(I)V")
    @Override
    public void onCameraChanged() {
        if (this.vertexShader != null) {
            @Pc(6) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(11) Matrix_Sub1 worldViewProjection = this.d3dToolkit.method8154();
            device.a(VS_CONST_WORLD_VIEW_PROJECTION, worldViewProjection.method1888(Static531.aFloatArray55));
        }
    }

    @OriginalMember(owner = "client!qn", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.vertexShader != null;
    }
}

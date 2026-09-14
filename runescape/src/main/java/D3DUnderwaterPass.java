import com.jagex.js5.js5;
import jagdx.IDirect3DDevice;
import jagdx.IDirect3DVertexShader;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pia")
public final class D3DUnderwaterPass extends RenderPass {

    @OriginalMember(owner = "client!pia", name = "k", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public IDirect3DVertexShader boundShader;

    @OriginalMember(owner = "client!pia", name = "m", descriptor = "Z")
    public boolean lit;

    @OriginalMember(owner = "client!pia", name = "n", descriptor = "Z")
    public boolean whiteTextureBound = false;

    @OriginalMember(owner = "client!pia", name = "j", descriptor = "Lclient!kea;")
    public final D3DToolkit d3dToolkit;

    @OriginalMember(owner = "client!pia", name = "l", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public IDirect3DVertexShader groundUnlitShader;

    @OriginalMember(owner = "client!pia", name = "r", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public IDirect3DVertexShader groundLitShader;

    @OriginalMember(owner = "client!pia", name = "q", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public IDirect3DVertexShader modelUnlitShader;

    @OriginalMember(owner = "client!pia", name = "s", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public IDirect3DVertexShader modelLitShader;

    @OriginalMember(owner = "client!pia", name = "o", descriptor = "Z")
    public final boolean supported;

    @OriginalMember(owner = "client!pia", name = "p", descriptor = "Lclient!og;")
    public Interface18 depthRamp;

    /**
     * Minor version 1.1 of the vertex shader model, the lowest the underwater shaders assemble against.
     */
    private static final int MIN_VERTEX_SHADER_VERSION = 257;

    private static final int VERTEX_SHADER_VERSION_MASK = 0xFFFF;

    private static final int VS_CONST_WORLD_VIEW_PROJECTION = 0;

    private static final int VS_CONST_PROJECTION = 4;

    private static final int VS_CONST_TEXTURE_MATRIX = 8;

    private static final int VS_CONST_FOG = 10;

    private static final int VS_CONST_UNDERWATER = 11;

    private static final int VS_CONST_WATER_PLANE = 12;

    private static final int VS_CONST_AMBIENT_COLOUR = 13;

    private static final int VS_CONST_LIGHT_COLOUR = 14;

    private static final int VS_CONST_LIGHT_DIRECTION = 15;

    private static final int VS_CONST_BACK_LIGHT_COLOUR = 16;

    private static final int VS_CONST_BACK_LIGHT_DIRECTION = 17;

    private static final float FOG_NEAR_FRACTION = 0.25F;

    private static final float FOG_FAR_FRACTION = 0.125F;

    private static final float MAX_BYTE = 255.0F;

    private static final int RAMP_TEXTURE_UNIT = 1;

    private static final int BASE_TEXTURE_UNIT = 0;

    @OriginalMember(owner = "client!pia", name = "<init>", descriptor = "(Lclient!kea;Lclient!sb;)V")
    public D3DUnderwaterPass(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) js5 shaders) {
        super(toolkit);
        this.d3dToolkit = toolkit;
        if (shaders == null || (this.d3dToolkit.aD3DCAPS1.VertexShaderVersion & VERTEX_SHADER_VERSION_MASK) < MIN_VERTEX_SHADER_VERSION) {
            this.supported = false;
        } else {
            this.groundUnlitShader = this.d3dToolkit.anIDirect3DDevice1.b(shaders.getfile("dx", "uw_ground_unlit"));
            this.groundLitShader = this.d3dToolkit.anIDirect3DDevice1.b(shaders.getfile("dx", "uw_ground_lit"));
            this.modelUnlitShader = this.d3dToolkit.anIDirect3DDevice1.b(shaders.getfile("dx", "uw_model_unlit"));
            this.modelLitShader = this.d3dToolkit.anIDirect3DDevice1.b(shaders.getfile("dx", "uw_model_lit"));
            if (this.modelLitShader != null & this.modelUnlitShader != null & this.groundLitShader != null & this.groundUnlitShader != null) {
                this.depthRamp = this.toolkit.method8034(false, 1, 2, new int[]{0, -1});
                this.depthRamp.method9052(false, false);
                this.supported = true;
            } else {
                this.supported = false;
            }
        }
    }

    @OriginalMember(owner = "client!pia", name = "f", descriptor = "(I)V")
    public void uploadLighting() {
        if (this.boundShader != null && this.lit) {
            @Pc(15) Matrix_Sub1 viewRotation = this.toolkit.method8068();
            @Pc(19) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            device.a(VS_CONST_AMBIENT_COLOUR, this.toolkit.aFloat191 * this.toolkit.aFloat186, this.toolkit.aFloat184 * this.toolkit.aFloat186, this.toolkit.aFloat186 * this.toolkit.aFloat195, 1.0F);
            device.a(VS_CONST_LIGHT_COLOUR, this.toolkit.aFloat181 * this.toolkit.aFloat191, this.toolkit.aFloat181 * this.toolkit.aFloat184, this.toolkit.aFloat195 * this.toolkit.aFloat181, 1.0F);
            device.a(VS_CONST_BACK_LIGHT_COLOUR, this.toolkit.aFloat191 * this.toolkit.aFloat180, this.toolkit.aFloat184 * this.toolkit.aFloat180, this.toolkit.aFloat195 * this.toolkit.aFloat180, 1.0F);
            viewRotation.method1897(this.toolkit.aFloatArray60[0], Static492.aFloatArray48, this.toolkit.aFloatArray60[1], this.toolkit.aFloatArray60[2]);
            device.SetVertexShaderConstantF(VS_CONST_LIGHT_DIRECTION, Static492.aFloatArray48, 1);
            viewRotation.method1897(this.toolkit.aFloatArray61[0], Static492.aFloatArray48, this.toolkit.aFloatArray61[1], this.toolkit.aFloatArray61[2]);
            device.SetVertexShaderConstantF(VS_CONST_BACK_LIGHT_DIRECTION, Static492.aFloatArray48, 1);
        }
    }

    @OriginalMember(owner = "client!pia", name = "a", descriptor = "(B)V")
    @Override
    public void onFogChanged() {
        if (this.boundShader != null) {
            @Pc(12) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(16) int far = this.toolkit.XA();
            @Pc(20) int near = this.toolkit.i();
            @Pc(32) float fogFar = -((float) (far - near) * FOG_FAR_FRACTION) + (float) far;
            @Pc(43) float fogNear = (float) far - (float) (far - near) * FOG_NEAR_FRACTION;
            device.a(VS_CONST_FOG, fogNear, 1.0F / (fogFar - fogNear), fogFar, 1.0F / ((float) far - fogFar));
            device.a(VS_CONST_UNDERWATER, 1.0F / (float) this.toolkit.method8105(), (float) this.toolkit.method8120() / MAX_BYTE, this.toolkit.aFloat192, 1.0F / (this.toolkit.aFloat192 - this.toolkit.aFloat189));
            this.toolkit.method8112(this.toolkit.method8025());
        }
    }

    @OriginalMember(owner = "client!pia", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.supported;
    }

    @OriginalMember(owner = "client!pia", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
    }

    @OriginalMember(owner = "client!pia", name = "d", descriptor = "(I)V")
    @Override
    public void onProjectionChanged() {
        if (this.boundShader != null) {
            @Pc(16) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            device.a(VS_CONST_PROJECTION, this.toolkit.method8137(Static492.aFloatArray47));
        }
    }

    @OriginalMember(owner = "client!pia", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        if (texture != null) {
            if (this.whiteTextureBound) {
                this.toolkit.method8080(0, Static189.aClass168_2);
                this.toolkit.method8142(Static189.aClass168_2, 0);
                this.whiteTextureBound = false;
            }
            this.toolkit.method8088(texture);
            this.toolkit.method8054(colourOp);
        } else if (!this.whiteTextureBound) {
            this.toolkit.method8088(this.toolkit.anInterface17_3);
            this.toolkit.method8054(1);
            this.toolkit.method8080(0, Static188.aClass168_1);
            this.toolkit.method8142(Static188.aClass168_1, 0);
            this.whiteTextureBound = true;
        }
    }

    @OriginalMember(owner = "client!pia", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
    }

    @OriginalMember(owner = "client!pia", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        this.lit = lit;
        this.toolkit.method8138(RAMP_TEXTURE_UNIT);
        this.toolkit.method8088(this.depthRamp);
        this.toolkit.method8094(Static185.aClass121_3, Static725.aClass121_6);
        this.toolkit.method8080(0, Static454.aClass168_5);
        this.toolkit.method8125(Static189.aClass168_2, true, false, 2);
        this.toolkit.method8142(Static188.aClass168_1, 0);
        this.toolkit.method8138(BASE_TEXTURE_UNIT);
        this.onUnderwaterSettingsChanged();
    }

    @OriginalMember(owner = "client!pia", name = "a", descriptor = "(Z)V")
    @Override
    public void onTextureMatrixChanged() {
        if (this.boundShader != null) {
            @Pc(7) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(12) Matrix_Sub1 textureMatrix = this.toolkit.method8066();
            device.SetVertexShaderConstantF(VS_CONST_TEXTURE_MATRIX, textureMatrix.method1898(Static492.aFloatArray47), 2);
        }
    }

    @OriginalMember(owner = "client!pia", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        this.toolkit.method8138(RAMP_TEXTURE_UNIT);
        this.toolkit.method8088(null);
        this.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
        this.toolkit.method8080(0, Static189.aClass168_2);
        this.toolkit.method8080(2, Static454.aClass168_5);
        this.toolkit.method8142(Static189.aClass168_2, 0);
        this.toolkit.method8138(BASE_TEXTURE_UNIT);
        if (this.whiteTextureBound) {
            this.toolkit.method8080(0, Static189.aClass168_2);
            this.toolkit.method8142(Static189.aClass168_2, 0);
            this.whiteTextureBound = false;
        }
        if (this.boundShader != null) {
            this.d3dToolkit.method4856(null);
            this.boundShader = null;
        }
    }

    @OriginalMember(owner = "client!pia", name = "c", descriptor = "(I)V")
    @Override
    public void onModelMatrixChanged() {
        if (this.boundShader != null) {
            @Pc(7) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(12) Matrix_Sub1 worldViewProjection = this.d3dToolkit.method8154();
            device.a(VS_CONST_WORLD_VIEW_PROJECTION, worldViewProjection.method1888(Static492.aFloatArray47));
        }
    }

    @OriginalMember(owner = "client!pia", name = "b", descriptor = "(I)V")
    @Override
    public void onCameraChanged() {
        if (this.boundShader != null) {
            @Pc(9) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
            @Pc(14) Matrix_Sub1 worldViewProjection = this.d3dToolkit.method8154();
            device.a(VS_CONST_WORLD_VIEW_PROJECTION, worldViewProjection.method1888(Static492.aFloatArray47));
        }
    }

    @OriginalMember(owner = "client!pia", name = "a", descriptor = "(I)V")
    @Override
    public void onUnderwaterSettingsChanged() {
        @Pc(3) IDirect3DDevice device = this.d3dToolkit.anIDirect3DDevice1;
        @Pc(8) int waterHeight = this.toolkit.method8092();
        @Pc(13) Matrix_Sub1 view = this.toolkit.method8118();
        @Pc(30) IDirect3DVertexShader shader;
        if (this.lit) {
            shader = ~waterHeight == Integer.MIN_VALUE ? this.groundLitShader : this.modelLitShader;
        } else {
            shader = waterHeight == Integer.MAX_VALUE ? this.groundUnlitShader : this.modelUnlitShader;
        }
        if (this.boundShader != shader) {
            this.boundShader = shader;
            this.d3dToolkit.method4856(shader);
            this.uploadLighting();
            this.onProjectionChanged();
            this.onTextureMatrixChanged();
            this.onModelMatrixChanged();
            this.onCameraChanged();
            this.onFogChanged();
        }
        view.method1879((float) waterHeight, -1.0F, 0.0F, Static492.aFloatArray48, 0.0F);
        device.a(VS_CONST_WATER_PLANE, Static492.aFloatArray48);
    }
}

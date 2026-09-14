import com.jagex.Class67;
import com.jagex.graphics.Renderer;
import com.jagex.DepthBuffer;
import com.jagex.Static14;
import com.jagex.graphics.FlipException;
import com.jagex.graphics.OffscreenSurface;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Surface;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import jaclib.peer.ti;
import jagdx.D3DADAPTER_IDENTIFIER;
import jagdx.D3DCAPS;
import jagdx.D3DDISPLAYMODE;
import jagdx.D3DLIGHT;
import jagdx.D3DPRESENT_PARAMETERS;
import jagdx.GeometryBuffer;
import jagdx.IDirect3D;
import jagdx.IDirect3DDevice;
import jagdx.IDirect3DEventQuery;
import jagdx.IDirect3DPixelShader;
import jagdx.IDirect3DSurface;
import jagdx.IDirect3DVertexShader;
import jagdx.PixelBuffer;
import jagdx.lh;
import jagdx.sja;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.reflect.Method;

@OriginalClass("client!kea")
public final class D3DToolkit extends NativeToolkit {

    @OriginalMember(owner = "client!sd", name = "a", descriptor = "(Lclient!sb;IILclient!d;Ljava/awt/Canvas;)Lclient!ha;")
    public static Toolkit create(@OriginalArg(0) js5 shaders, @OriginalArg(4) Canvas canvas, @OriginalArg(3) TextureSource textureSource, @OriginalArg(1) int antialiasing) {
        try {
            if (!Static651.method8516()) {
                throw new RuntimeException("");
            } else if (Static14.loadNativeLibrary("jagdx")) {
                @Pc(26) Class local26 = Class.forName("D3DToolkit");
                @Pc(68) Method local68 = local26.getDeclaredMethod("createToolkit", Class.forName("java.awt.Canvas"), Class.forName("com.jagex.graphics.TextureSource"), Class.forName("com.jagex.js5.js5"), Class.forName("java.lang.Integer"));
                return (Toolkit) local68.invoke(null, canvas, textureSource, shaders, Integer.valueOf(antialiasing));
            } else {
                throw new RuntimeException("");
            }
        } catch (@Pc(98) Throwable local98) {
            throw new RuntimeException("");
        }
    }

    @OriginalMember(owner = "client!kea", name = "Og", descriptor = "Lclient!jagdx/IDirect3DVertexShader;")
    public IDirect3DVertexShader anIDirect3DVertexShader1;

    @OriginalMember(owner = "client!kea", name = "Ag", descriptor = "Z")
    public boolean aBoolean411 = false;

    @OriginalMember(owner = "client!kea", name = "Hg", descriptor = "I")
    public int anInt5370 = 0;

    @OriginalMember(owner = "client!kea", name = "zg", descriptor = "Lclient!jagdx/D3DCAPS;")
    public final D3DCAPS aD3DCAPS1;

    @OriginalMember(owner = "client!kea", name = "Yg", descriptor = "I")
    public final int anInt5368;

    @OriginalMember(owner = "client!kea", name = "xg", descriptor = "Lclient!jagdx/IDirect3D;")
    public final IDirect3D anIDirect3D1;

    @OriginalMember(owner = "client!kea", name = "Ig", descriptor = "Lclient!jagdx/D3DPRESENT_PARAMETERS;")
    public final D3DPRESENT_PARAMETERS aD3DPRESENT_PARAMETERS1;

    @OriginalMember(owner = "client!kea", name = "Pg", descriptor = "Lclient!tda;")
    public Class349 aClass349_1;

    @OriginalMember(owner = "client!kea", name = "Fg", descriptor = "Lclient!jaclib/peer/ti;")
    public final ti aTi1;

    @OriginalMember(owner = "client!kea", name = "Sg", descriptor = "Lclient!jagdx/IDirect3DDevice;")
    public final IDirect3DDevice anIDirect3DDevice1;

    @OriginalMember(owner = "client!kea", name = "Ug", descriptor = "I")
    public final int anInt5369;

    @OriginalMember(owner = "client!kea", name = "Mg", descriptor = "Lclient!jagdx/D3DLIGHT;")
    public final D3DLIGHT aD3DLIGHT1;

    @OriginalMember(owner = "client!kea", name = "Wg", descriptor = "Lclient!jagdx/D3DLIGHT;")
    public final D3DLIGHT aD3DLIGHT3;

    @OriginalMember(owner = "client!kea", name = "Vg", descriptor = "Lclient!jagdx/D3DLIGHT;")
    public final D3DLIGHT aD3DLIGHT2;

    @OriginalMember(owner = "client!kea", name = "Kg", descriptor = "Lclient!jagdx/PixelBuffer;")
    public final PixelBuffer aPixelBuffer1;

    @OriginalMember(owner = "client!kea", name = "Qg", descriptor = "Lclient!jagdx/GeometryBuffer;")
    public final GeometryBuffer aGeometryBuffer1;

    @OriginalMember(owner = "client!kea", name = "Ng", descriptor = "Z")
    public final boolean mipmappedCubeMapSupported;

    @OriginalMember(owner = "client!kea", name = "Jg", descriptor = "Z")
    public final boolean mipmapSupported;

    @OriginalMember(owner = "client!kea", name = "Lg", descriptor = "Z")
    public final boolean nonPowerOfTwoSupported;

    @OriginalMember(owner = "client!kea", name = "Tg", descriptor = "[I")
    public final int[] anIntArray391;

    @OriginalMember(owner = "client!kea", name = "Eg", descriptor = "[Z")
    public final boolean[] aBooleanArray13;

    @OriginalMember(owner = "client!kea", name = "Gg", descriptor = "[Z")
    public final boolean[] aBooleanArray15;

    @OriginalMember(owner = "client!kea", name = "Cg", descriptor = "[Z")
    public final boolean[] aBooleanArray14;

    @OriginalMember(owner = "client!kea", name = "Bg", descriptor = "[Lclient!nga;")
    public final Class259[] aClass259Array1;

    @OriginalMember(owner = "client!kea", name = "Xg", descriptor = "[Z")
    public final boolean[] aBooleanArray16;

    @OriginalMember(owner = "client!kea", name = "createToolkit", descriptor = "(Ljava/awt/Canvas;Lclient!d;Lclient!sb;Ljava/lang/Integer;)Lclient!ha;")
    public static Toolkit createToolkit(@OriginalArg(0) Canvas arg0, @OriginalArg(1) TextureSource arg1, @OriginalArg(2) js5 arg2, @OriginalArg(3) Integer arg3) {
        @Pc(1) D3DToolkit local1 = null;
        try {
            @Pc(3) byte local3 = 0;
            @Pc(5) byte local5 = 1;
            @Pc(9) ti local9 = new ti();
            @Pc(13) IDirect3D local13 = IDirect3D.a(-2147483616, local9);
            @Pc(18) D3DCAPS local18 = local13.a(local3, local5);
            if ((local18.RasterCaps & 0x1000000) == 0) {
                throw new RuntimeException("");
            } else if (local18.MaxSimultaneousTextures < 2) {
                throw new RuntimeException("");
            } else if ((local18.TextureOpCaps & 0x2) == 0) {
                throw new RuntimeException("");
            } else if ((local18.TextureOpCaps & 0x8) == 0) {
                throw new RuntimeException("");
            } else if ((local18.TextureOpCaps & 0x40) == 0) {
                throw new RuntimeException("");
            } else if ((local18.TextureOpCaps & 0x200) == 0) {
                throw new RuntimeException("");
            } else if ((local18.TextureOpCaps & 0x2000000) == 0) {
                throw new RuntimeException("");
            } else if ((local18.DestBlendCaps & 0x10 & local18.SrcBlendCaps) == 0) {
                throw new RuntimeException("");
            } else if ((local18.SrcBlendCaps & local18.DestBlendCaps & 0x20) == 0) {
                throw new RuntimeException("");
            } else if ((local18.DestBlendCaps & 0x2 & local18.SrcBlendCaps) == 0) {
                throw new RuntimeException("");
            } else if (local18.MaxActiveLights > 0 && local18.MaxActiveLights < 2) {
                throw new RuntimeException("");
            } else if (local18.MaxStreams < 5) {
                throw new RuntimeException("");
            } else {
                @Pc(186) D3DPRESENT_PARAMETERS local186 = new D3DPRESENT_PARAMETERS(arg0);
                if (!Static325.method4859(local186, local3, local13, local5, arg3)) {
                    throw new RuntimeException("");
                }
                local186.EnableAutoDepthStencil = true;
                local186.Windowed = true;
                local186.PresentationInterval = Integer.MIN_VALUE;
                @Pc(213) int local213 = 2;
                if ((local18.DevCaps & 0x100000) != 0) {
                    local213 |= 0x10;
                }
                @Pc(237) IDirect3DDevice local237;
                try {
                    local237 = local13.a(local3, local5, arg0, local213 | 0x40, local186);
                } catch (@Pc(239) sja local239) {
                    local237 = local13.a(local3, local5, arg0, local213 | 0x20, local186);
                }
                @Pc(258) Class349 local258 = new Class349(local237.b(0), local237.c());
                local1 = new D3DToolkit(local3, local5, arg0, local9, local13, local237, local258, local186, local18, arg1, arg2, arg3);
                local1.method8134();
                return local1;
            }
        } catch (@Pc(281) RuntimeException local281) {
            if (local1 != null) {
                local1.stop();
            }
            throw local281;
        }
    }

    @OriginalMember(owner = "client!kea", name = "<init>", descriptor = "(IILjava/awt/Canvas;Lclient!jaclib/peer/ti;Lclient!jagdx/IDirect3D;Lclient!jagdx/IDirect3DDevice;Lclient!tda;Lclient!jagdx/D3DPRESENT_PARAMETERS;Lclient!jagdx/D3DCAPS;Lclient!d;Lclient!sb;I)V")
    public D3DToolkit(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Canvas arg2, @OriginalArg(3) ti arg3, @OriginalArg(4) IDirect3D arg4, @OriginalArg(5) IDirect3DDevice arg5, @OriginalArg(6) Class349 arg6, @OriginalArg(7) D3DPRESENT_PARAMETERS arg7, @OriginalArg(8) D3DCAPS arg8, @OriginalArg(9) TextureSource arg9, @OriginalArg(10) js5 arg10, @OriginalArg(11) int arg11) {
        super(arg2, arg6, arg9, arg10, arg11, 0);
        try {
            this.aD3DCAPS1 = arg8;
            this.anInt5368 = arg1;
            this.anIDirect3D1 = arg4;
            this.aD3DPRESENT_PARAMETERS1 = arg7;
            this.aClass349_1 = arg6;
            this.aTi1 = arg3;
            this.anIDirect3DDevice1 = arg5;
            this.anInt5369 = arg0;
            this.aD3DLIGHT1 = new D3DLIGHT(this.aTi1);
            this.aD3DLIGHT3 = new D3DLIGHT(this.aTi1);
            this.aD3DLIGHT2 = new D3DLIGHT(this.aTi1);
            this.aPixelBuffer1 = new PixelBuffer(this.aTi1);
            this.aGeometryBuffer1 = new GeometryBuffer(this.aTi1);
            new GeometryBuffer(this.aTi1);
            this.anInt9184 = this.aD3DCAPS1.MaxSimultaneousTextures;
            this.mipmappedCubeMapSupported = (this.aD3DCAPS1.TextureCaps & 0x10000) != 0;
            this.mipmapSupported = (this.aD3DCAPS1.TextureCaps & 0x4000) != 0;
            this.anInt9182 = this.aD3DCAPS1.MaxActiveLights > 0 ? this.aD3DCAPS1.MaxActiveLights : 8;
            this.aBoolean685 = (this.aD3DCAPS1.TextureCaps & 0x800) != 0;
            this.aBoolean696 = (this.aD3DCAPS1.TextureCaps & 0x2000) != 0;
            this.nonPowerOfTwoSupported = (this.aD3DCAPS1.TextureCaps & 0x2) == 0;
            this.aBoolean692 = this.anInt9158 > 0 || this.anIDirect3D1.CheckDeviceMultiSampleType(this.anInt5369, this.anInt5368, this.aD3DPRESENT_PARAMETERS1.BackBufferFormat, true, 2) == 0;
            this.anIntArray391 = new int[this.anInt9184];
            this.aBooleanArray13 = new boolean[this.anInt9184];
            this.aBooleanArray15 = new boolean[this.anInt9184];
            this.aBooleanArray14 = new boolean[this.anInt9184];
            this.aClass259Array1 = new Class259[this.anInt9184];
            this.aBooleanArray16 = new boolean[this.anInt9184];
            this.anIDirect3DDevice1.BeginScene();
        } catch (@Pc(228) Throwable local228) {
            local228.printStackTrace();
            this.free();
            throw new RuntimeException("");
        }
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!eba;Lclient!wda;III)Lclient!og;")
    @Override
    public Interface18 method8067(@OriginalArg(0) Class92 arg0, @OriginalArg(1) Class397 arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        return new D3DTexture2D(this, arg0, arg1, arg3, arg2);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!eba;[BBIII)Lclient!bga;")
    @Override
    public Interface2 method8038(@OriginalArg(0) Class92 arg0, @OriginalArg(1) byte[] arg1) {
        return new D3DTexture3D(this, arg0, 128, 128, 16, arg1);
    }

    @OriginalMember(owner = "client!kea", name = "Y", descriptor = "(I)V")
    @Override
    protected void method8085() {
    }

    @OriginalMember(owner = "client!kea", name = "e", descriptor = "(I)V")
    @Override
    public synchronized void tick(@OriginalArg(0) int time) {
        this.aTi1.c(-104);
        super.tick(time);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(IIII)V")
    @Override
    public void method7959(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(BLclient!jagdx/IDirect3DVertexShader;)V")
    public void method4856(@OriginalArg(1) IDirect3DVertexShader arg0) {
        this.anIDirect3DVertexShader1 = arg0;
        this.anIDirect3DDevice1.SetVertexShader(arg0);
        this.method8086();
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Ljava/awt/Canvas;Ljava/lang/Object;I)V")
    @Override
    protected void method8041(@OriginalArg(0) Canvas arg0, @OriginalArg(1) Object arg1) {
        if (arg0 != this.aCanvas13) {
            return;
        }
        @Pc(6) Dimension local6 = arg0.getSize();
        if (local6.width > 0 && local6.height > 0) {
            this.anIDirect3DDevice1.EndScene();
            this.method4861();
            this.anIDirect3DDevice1.BeginScene();
        }
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(IILclient!mg;)V")
    @Override
    public void method8130(@OriginalArg(0) int arg0, @OriginalArg(2) Interface16 arg1) {
        @Pc(6) Class106 local6 = (Class106) arg1;
        this.anIDirect3DDevice1.SetStreamSource(arg0, local6.anIDirect3DVertexBuffer1, 0, local6.method2445());
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(ILclient!jagdx/IDirect3DPixelShader;)V")
    public void method4858(@OriginalArg(1) IDirect3DPixelShader arg0) {
        this.anIDirect3DDevice1.SetPixelShader(arg0);
    }

    @OriginalMember(owner = "client!kea", name = "v", descriptor = "()V")
    @Override
    public void restoreSurface() {
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(I[[IZI)Lclient!fv;")
    @Override
    public Interface8 method8063(@OriginalArg(0) int arg0, @OriginalArg(1) int[][] arg1, @OriginalArg(2) boolean arg2) {
        return new D3DCubeMapTexture(this, arg0, arg2, arg1);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!wda;Lclient!eba;B)Z")
    @Override
    public boolean method8153(@OriginalArg(0) Class397 arg0, @OriginalArg(1) Class92 arg1) {
        @Pc(3) D3DDISPLAYMODE local3 = new D3DDISPLAYMODE();
        return lh.a((byte) 75, this.anIDirect3D1.a(this.anInt5369, local3)) && lh.a((byte) 89, this.anIDirect3D1.CheckDeviceFormat(this.anInt5369, this.anInt5368, local3.Format, 0, 4, Static325.method4868(arg0, arg1)));
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!gaa;)V")
    @Override
    public void swapSurface(@OriginalArg(0) OffscreenSurface surface) {
    }

    @OriginalMember(owner = "client!kea", name = "d", descriptor = "()V")
    @Override
    public void method7974() {
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!eba;Lclient!wda;I)Z")
    @Override
    public boolean method8071(@OriginalArg(0) Class92 arg0, @OriginalArg(1) Class397 arg1) {
        @Pc(8) D3DDISPLAYMODE local8 = new D3DDISPLAYMODE();
        return lh.a((byte) 98, this.anIDirect3D1.a(this.anInt5369, local8)) && lh.a((byte) 58, this.anIDirect3D1.CheckDeviceFormat(this.anInt5369, this.anInt5368, local8.Format, 0, 3, Static325.method4868(arg1, arg0)));
    }

    @OriginalMember(owner = "client!kea", name = "c", descriptor = "()Lclient!dp;")
    @Override
    public Renderer renderer() {
        @Pc(6) D3DADAPTER_IDENTIFIER identifier = this.anIDirect3D1.b(this.anInt5369, 0);
        return new Renderer(identifier.VendorID, "Direct3D", 9, identifier.Description, identifier.DriverVersion);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(IBLclient!hla;Z)V")
    @Override
    protected void method8139(@OriginalArg(0) int arg0, @OriginalArg(2) Class168 arg1) {
        @Pc(17) byte local17;
        if (arg0 == 1) {
            local17 = 6;
        } else if (arg0 == 2) {
            local17 = 27;
        } else {
            local17 = 5;
        }
        this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, local17, Static325.method4862(arg1) | 0x0);
    }

    @OriginalMember(owner = "client!kea", name = "c", descriptor = "(B)V")
    @Override
    protected void method8084() {
        this.anIDirect3DDevice1.a(28, this.aBoolean690 && this.aBoolean683 && this.anInt9175 >= 0);
    }

    @OriginalMember(owner = "client!kea", name = "j", descriptor = "()V")
    @Override
    public void method7950() {
        @Pc(3) IDirect3DEventQuery local3 = this.anIDirect3DDevice1.b();
        if (lh.a((byte) 95, local3.Issue())) {
            while (true) {
                @Pc(11) int local11 = local3.IsSignaled();
                if (local11 != 1) {
                    break;
                }
                Thread.yield();
            }
        }
        local3.a(-65);
    }

    @OriginalMember(owner = "client!kea", name = "s", descriptor = "(I)V")
    @Override
    protected void method8078() {
        this.anIDirect3DDevice1.a(137, this.aBoolean691 && !this.aBoolean680);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(II)Lclient!eca;")
    @Override
    public Surface method7962(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return null;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!eba;ZIIB[FII)Lclient!og;")
    @Override
    protected Interface18 method8098(@OriginalArg(0) Class92 arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) int arg2, @OriginalArg(5) float[] arg3, @OriginalArg(7) int arg4) {
        return null;
    }

    @OriginalMember(owner = "client!kea", name = "b", descriptor = "(IIIID)V")
    @Override
    public void b(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) double zDepth) {
    }

    @OriginalMember(owner = "client!kea", name = "i", descriptor = "(II)Lclient!rea;")
    @Override
    protected RenderPass method8087(@OriginalArg(1) int arg0) {
        if (arg0 == 3) {
            return new D3DUnderwaterPass(this, this.aJs5_112);
        } else if (arg0 == 4) {
            return new D3DTransparentWaterPass(this, this.aJs5_112, this.aClass7_8);
        } else if (arg0 == 8) {
            return new D3DEnvironmentMappedWaterPass(this, this.aJs5_112, this.aClass7_8);
        } else {
            return super.method8087(arg0);
        }
    }

    @OriginalMember(owner = "client!kea", name = "C", descriptor = "(B)Z")
    public boolean method4861() {
        @Pc(3) int local3 = this.anIDirect3DDevice1.TestCooperativeLevel();
        if (local3 == 0 || local3 == -2005530519) {
            @Pc(26) Class349 local26 = (Class349) this.anObject16;
            this.method8061();
            local26.method8178();
            this.aD3DPRESENT_PARAMETERS1.BackBufferWidth = 0;
            this.aD3DPRESENT_PARAMETERS1.BackBufferHeight = 0;
            if (Static325.method4859(this.aD3DPRESENT_PARAMETERS1, this.anInt5369, this.anIDirect3D1, this.anInt5368, this.anInt9158)) {
                @Pc(59) int local59 = this.anIDirect3DDevice1.Reset(this.aD3DPRESENT_PARAMETERS1);
                if (lh.a((byte) 95, local59)) {
                    local26.method8180(this.anIDirect3DDevice1.c(), this.anIDirect3DDevice1.b(0));
                    this.method8101();
                    this.method8102();
                    return true;
                }
            }
        }
        return false;
    }

    @OriginalMember(owner = "client!kea", name = "N", descriptor = "(I)V")
    @Override
    protected void method8076() {
        this.anIDirect3DDevice1.SetScissorRect(this.anInt9147 + this.anInt9154, this.anInt9161 + this.anInt9155, this.anInt9153, this.anInt9165);
    }

    @OriginalMember(owner = "client!kea", name = "Z", descriptor = "(I)V")
    @Override
    protected void method8146() {
        this.anIDirect3DDevice1.a(14, this.aBoolean677 && this.aBoolean686);
    }

    @OriginalMember(owner = "client!kea", name = "v", descriptor = "(I)V")
    @Override
    protected void method8035() {
        if (Static80.aClass38_2 == this.aClass38_7) {
            this.anIDirect3DDevice1.SetRenderState(19, 5);
            this.anIDirect3DDevice1.SetRenderState(20, 6);
        } else if (this.aClass38_7 == Static217.aClass38_3) {
            this.anIDirect3DDevice1.SetRenderState(19, 2);
            this.anIDirect3DDevice1.SetRenderState(20, 2);
        } else if (this.aClass38_7 == Static355.aClass38_5) {
            this.anIDirect3DDevice1.SetRenderState(19, 9);
            this.anIDirect3DDevice1.SetRenderState(20, 2);
        }
    }

    @OriginalMember(owner = "client!kea", name = "h", descriptor = "(Z)V")
    @Override
    protected void method8039() {
        @Pc(23) int local23 = this.aBooleanArray16[this.anInt9148] ? Static325.method4857(this.aClass121Array6[this.anInt9148]) : 1;
        this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, 1, local23);
    }

    @OriginalMember(owner = "client!kea", name = "u", descriptor = "()V")
    @Override
    protected void stop() {
        this.aTi1.b(-23417);
        super.stop();
    }

    @OriginalMember(owner = "client!kea", name = "I", descriptor = "()I")
    @Override
    public int I() {
        return 0;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(ZB)Lclient!ri;")
    @Override
    public Interface20 method8122(@OriginalArg(0) boolean arg0) {
        return new Class11(this, Static702.aClass397_17, arg0);
    }

    @OriginalMember(owner = "client!kea", name = "o", descriptor = "(B)V")
    @Override
    protected void method8037() {
        this.anIDirect3DDevice1.SetViewport(this.anInt9147, this.anInt9155, this.anInt9122, this.anInt9013, 0.0F, 1.0F);
    }

    @OriginalMember(owner = "client!kea", name = "d", descriptor = "(II)Lclient!wja;")
    @Override
    public DepthBuffer method7986(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return null;
    }

    @OriginalMember(owner = "client!kea", name = "na", descriptor = "(IIII)[I")
    @Override
    public int[] na(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height) {
        @Pc(1) int[] local1 = null;
        @Pc(6) IDirect3DSurface local6 = this.anIDirect3DDevice1.c(0);
        @Pc(16) IDirect3DSurface local16 = this.anIDirect3DDevice1.a(width, height, 21, 0, 0, true);
        if (lh.a((byte) 78, this.anIDirect3DDevice1.StretchRect(local6, x, y, width, height, local16, 0, 0, width, height, 0)) && lh.a((byte) 65, local16.LockRect(0, 0, width, height, 16, this.aPixelBuffer1))) {
            local1 = new int[width * height];
            @Pc(56) int local56 = this.aPixelBuffer1.getRowPitch();
            if (local56 == width * 4) {
                this.aPixelBuffer1.a(local1, 0, 0, height * width);
            } else {
                for (@Pc(74) int local74 = 0; local74 < height; local74++) {
                    this.aPixelBuffer1.a(local1, width * local74, local74 * local56, width);
                }
            }
            local16.UnlockRect();
        }
        local6.a(-90);
        local16.a(104);
        return local1;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Ljava/awt/Canvas;I)Ljava/lang/Object;")
    @Override
    protected Object method8127(@OriginalArg(0) Canvas arg0) {
        return null;
    }

    @OriginalMember(owner = "client!kea", name = "i", descriptor = "(Z)V")
    @Override
    protected void method8102() {
        for (@Pc(1) int local1 = 0; local1 < this.anInt9184; local1++) {
            this.anIDirect3DDevice1.SetSamplerState(local1, 7, 0);
            this.anIDirect3DDevice1.SetSamplerState(local1, 6, 2);
            this.anIDirect3DDevice1.SetSamplerState(local1, 5, 2);
            this.anIDirect3DDevice1.SetSamplerState(local1, 1, 1);
            this.anIDirect3DDevice1.SetSamplerState(local1, 2, 1);
            this.aClass259Array1[local1] = Static60.aClass259_3;
            this.aBooleanArray15[local1] = this.aBooleanArray14[local1] = true;
            this.aBooleanArray13[local1] = false;
            this.anIntArray391[local1] = 0;
        }
        this.anIDirect3DDevice1.SetTextureStageState(0, 6, 1);
        this.anIDirect3DDevice1.SetRenderState(9, 2);
        this.anIDirect3DDevice1.SetRenderState(23, 4);
        this.anIDirect3DDevice1.SetRenderState(25, 5);
        this.anIDirect3DDevice1.SetRenderState(24, 0);
        this.anIDirect3DDevice1.SetRenderState(22, 2);
        this.anIDirect3DDevice1.SetRenderState(147, 1);
        this.anIDirect3DDevice1.SetRenderState(145, 1);
        this.anIDirect3DDevice1.a(38, 0.95F);
        this.anIDirect3DDevice1.SetRenderState(140, 3);
        this.aD3DLIGHT1.SetType(3);
        this.aD3DLIGHT3.SetType(3);
        this.aD3DLIGHT2.SetType(1);
        this.aBoolean411 = false;
        super.method8102();
    }

    @OriginalMember(owner = "client!kea", name = "F", descriptor = "(II)V")
    @Override
    public void F(@OriginalArg(0) int x, @OriginalArg(1) int y) {
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(II[IIIZI)Lclient!og;")
    @Override
    public Interface18 method8143(@OriginalArg(1) int arg0, @OriginalArg(2) int[] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) boolean arg4, @OriginalArg(6) int arg5) {
        return new D3DTexture2D(this, arg0, arg5, arg4, arg1, 0, arg3);
    }

    @OriginalMember(owner = "client!kea", name = "l", descriptor = "(Z)V")
    @Override
    protected void method8065() {
        @Pc(8) float local8 = this.aBoolean684 ? this.aFloat181 : 0.0F;
        @Pc(18) float local18 = this.aBoolean684 ? -this.aFloat180 : 0.0F;
        this.aD3DLIGHT1.SetDiffuse(local8 * this.aFloat191, this.aFloat184 * local8, this.aFloat195 * local8, 0.0F);
        this.aD3DLIGHT3.SetDiffuse(this.aFloat191 * local18, local18 * this.aFloat184, this.aFloat195 * local18, 0.0F);
        this.aBoolean411 = false;
    }

    @OriginalMember(owner = "client!kea", name = "t", descriptor = "(B)V")
    @Override
    protected void method8150() {
        this.aFloat192 = (float) (this.anInt9167 - this.anInt9185);
        this.aFloat189 = (float) -this.anInt9175 + this.aFloat192;
        if (this.aFloat189 < (float) this.anInt9181) {
            this.aFloat189 = (float) this.anInt9181;
        }
        this.anIDirect3DDevice1.a(36, this.aFloat189);
        this.anIDirect3DDevice1.a(37, this.aFloat192);
        this.anIDirect3DDevice1.SetRenderState(34, this.anInt9146);
    }

    @OriginalMember(owner = "client!kea", name = "B", descriptor = "(I)V")
    @Override
    protected void method8060() {
        this.anIDirect3DDevice1.a(15, this.aBoolean679);
    }

    @OriginalMember(owner = "client!kea", name = "v", descriptor = "(B)V")
    @Override
    protected void method8045() {
        this.anIDirect3DDevice1.a(27, this.aBoolean689);
    }

    @OriginalMember(owner = "client!kea", name = "h", descriptor = "()V")
    @Override
    public void method7969() {
    }

    @OriginalMember(owner = "client!kea", name = "d", descriptor = "(B)V")
    @Override
    protected void method8136() {
        if (this.aBoolean676) {
            Static325.aFloatArray36[9] = 0.0F;
            Static325.aFloatArray36[2] = 0.0F;
            Static325.aFloatArray36[7] = 0.0F;
            Static325.aFloatArray36[15] = 1.0F;
            Static325.aFloatArray36[11] = 0.0F;
            Static325.aFloatArray36[1] = 0.0F;
            Static325.aFloatArray36[6] = 0.0F;
            Static325.aFloatArray36[14] = 0.0F;
            Static325.aFloatArray36[3] = 0.0F;
            Static325.aFloatArray36[12] = 0.0F;
            Static325.aFloatArray36[8] = 0.0F;
            Static325.aFloatArray36[13] = 0.0F;
            Static325.aFloatArray36[0] = 1.0F;
            Static325.aFloatArray36[5] = 1.0F;
            Static325.aFloatArray36[10] = 1.0F;
            Static325.aFloatArray36[4] = 0.0F;
        } else {
            this.aClass73_Sub1_15.method1891(Static325.aFloatArray36);
        }
        this.anIDirect3DDevice1.SetTransform(256, Static325.aFloatArray36);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(FFF)V")
    @Override
    public void method7993(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2) {
    }

    @OriginalMember(owner = "client!kea", name = "b", descriptor = "(ZB)V")
    @Override
    public void method8141(@OriginalArg(0) boolean arg0) {
        this.anIDirect3DDevice1.a(161, arg0);
    }

    @OriginalMember(owner = "client!kea", name = "k", descriptor = "(B)V")
    @Override
    protected void method8086() {
        if (this.anIDirect3DVertexShader1 != null || Static215.aClass370_2 == this.aClass370Array3[this.anInt9148]) {
            this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, 24, 0);
            this.anIntArray391[this.anInt9148] = 0;
            return;
        }
        if (this.aClass370Array3[this.anInt9148] == Static431.aClass370_5) {
            this.anIDirect3DDevice1.SetTransform(this.anInt9148 + 16, this.aClass73_Sub1Array3[this.anInt9148].method1881(Static325.aFloatArray36));
        } else {
            this.anIDirect3DDevice1.SetTransform(this.anInt9148 + 16, this.aClass73_Sub1Array3[this.anInt9148].method1891(Static325.aFloatArray36));
        }
        @Pc(63) int local63 = Static325.method4863(this.aClass370Array3[this.anInt9148]);
        if (local63 != this.anIntArray391[this.anInt9148]) {
            this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, 24, local63);
            this.anIntArray391[this.anInt9148] = local63;
        }
    }

    @OriginalMember(owner = "client!kea", name = "ya", descriptor = "()V")
    @Override
    public void ya() {
        this.method8083(true);
        this.anIDirect3DDevice1.Clear(2, 0, 1.0F, 0);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(B[Lclient!mc;)Lclient!mk;")
    @Override
    public Class244 method8148(@OriginalArg(1) Class237[] arg0) {
        return new dxVertexLayout(this, arg0);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!eba;IZIIIZ[B)Lclient!og;")
    @Override
    protected Interface18 method8103(@OriginalArg(0) Class92 arg0, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2, @OriginalArg(5) int arg3, @OriginalArg(7) byte[] arg4) {
        return new D3DTexture2D(this, arg0, arg1, arg3, arg2, arg4, 0, 0);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!eca;Lclient!wja;)Lclient!gaa;")
    @Override
    public OffscreenSurface createOffscreenSurface(@OriginalArg(0) Surface surface, @OriginalArg(1) DepthBuffer buffer) {
        return null;
    }

    @OriginalMember(owner = "client!kea", name = "b", descriptor = "(Z)V")
    @Override
    public void method8018(@OriginalArg(0) boolean arg0) {
    }

    @OriginalMember(owner = "client!kea", name = "D", descriptor = "(I)F")
    @Override
    protected float method8155() {
        return -0.5F;
    }

    @OriginalMember(owner = "client!kea", name = "e", descriptor = "(II)V")
    @Override
    public void flip(@OriginalArg(0) int x, @OriginalArg(1) int y) throws FlipException {
        this.anIDirect3DDevice1.EndScene();
        if (this.aClass349_1.method8181()) {
            this.anInt5370 = 0;
            if (lh.a(this.aClass349_1.method8179(), true)) {
                this.method4861();
            }
        } else if (++this.anInt5370 <= 50) {
            this.method4861();
        } else {
            throw new FlipException();
        }
        this.anIDirect3DDevice1.BeginScene();
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(ILclient!waa;)V")
    public void bindTexture2D(@OriginalArg(1) D3DTexture2D texture) {
        this.bindTexture(texture);
        if (texture.repeatU != this.aBooleanArray15[this.anInt9148]) {
            this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 1, texture.repeatU ? 1 : 3);
            this.aBooleanArray15[this.anInt9148] = texture.repeatU;
        }
        if (texture.repeatV != this.aBooleanArray14[this.anInt9148]) {
            this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 2, texture.repeatV ? 1 : 3);
            this.aBooleanArray14[this.anInt9148] = texture.repeatV;
        }
    }

    @OriginalMember(owner = "client!kea", name = "m", descriptor = "()Z")
    @Override
    public boolean hasBloom() {
        return false;
    }

    @OriginalMember(owner = "client!kea", name = "p", descriptor = "(I)V")
    @Override
    protected void method8113() {
        this.anIDirect3DDevice1.SetRenderState(60, this.anInt9157);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Ljava/lang/Object;Ljava/awt/Canvas;I)V")
    @Override
    protected void method8095(@OriginalArg(0) Object arg0, @OriginalArg(1) Canvas arg1) {
        this.aClass349_1 = (Class349) arg0;
    }

    @OriginalMember(owner = "client!kea", name = "k", descriptor = "(Z)V")
    @Override
    protected void method8129() {
        this.aD3DLIGHT1.SetAmbient(this.aFloat186 * this.aFloat191, this.aFloat186 * this.aFloat184, this.aFloat195 * this.aFloat186, 0.0F);
        this.aBoolean411 = false;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(B)V")
    @Override
    protected void method8116() {
        this.anIDirect3DDevice1.a(7, this.aBoolean678);
    }

    @OriginalMember(owner = "client!kea", name = "u", descriptor = "(B)V")
    @Override
    protected void method8027() {
        if (this.aBooleanArray16[this.anInt9148]) {
            this.aBooleanArray16[this.anInt9148] = false;
            this.anIDirect3DDevice1.SetTexture(this.anInt9148, null);
            this.method8030();
            this.method8039();
        }
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!fma;III)V")
    @Override
    public void method8079(@OriginalArg(0) Class131 arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        this.anIDirect3DDevice1.DrawPrimitive(Static325.method4865(arg0), arg1, arg2);
    }

    @OriginalMember(owner = "client!kea", name = "M", descriptor = "(I)V")
    @Override
    protected void method8049() {
        @Pc(1) int local1;
        for (local1 = 0; local1 < this.anInt9177; local1++) {
            @Pc(10) PointLight local10 = this.aClass2_Sub7Array6[local1];
            @Pc(14) int local14 = local1 + 2;
            @Pc(18) int local18 = local10.method8431();
            @Pc(24) float local24 = local10.getIntensity() / 255.0F;
            this.aD3DLIGHT2.SetPosition((float) local10.getX(), (float) local10.getY(), (float) local10.getZ());
            this.aD3DLIGHT2.SetDiffuse((float) (local18 >> 16 & 0xFF) * local24, local24 * (float) (local18 >> 8 & 0xFF), local24 * (float) (local18 & 0xFF), 0.0F);
            this.aD3DLIGHT2.SetAttenuation(0.0F, 0.0F, 1.0F / (float) (local10.getRange() * local10.getRange()));
            this.aD3DLIGHT2.SetRange((float) local10.getRange());
            this.anIDirect3DDevice1.SetLight(local14, this.aD3DLIGHT2);
            this.anIDirect3DDevice1.LightEnable(local14, true);
        }
        while (this.anInt9176 > local1) {
            this.anIDirect3DDevice1.LightEnable(local1 + 2, false);
            local1++;
        }
        super.method8049();
    }

    @OriginalMember(owner = "client!kea", name = "f", descriptor = "()V")
    @Override
    public void stopBloom() {
    }

    @OriginalMember(owner = "client!kea", name = "u", descriptor = "(I)V")
    @Override
    protected void method8058() {
        this.anIDirect3DDevice1.a(174, this.aBoolean697);
    }

    @OriginalMember(owner = "client!kea", name = "GA", descriptor = "(I)V")
    @Override
    public void GA(@OriginalArg(0) int colour) {
        this.anIDirect3DDevice1.Clear(1, colour, 0.0F, 0);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(IZ)Lclient!mg;")
    @Override
    public Interface16 method8156(@OriginalArg(1) boolean arg0) {
        return new Class106(this, arg0);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(BLclient!hw;)V")
    @Override
    public void method8097(@OriginalArg(1) Class172 arg0) {
        @Pc(1) int local1 = 0;
        if (Static360.aClass172_3 == arg0) {
            local1 = 65536;
        } else if (arg0 == Static285.aClass172_1) {
            local1 = 131072;
        } else if (arg0 == Static320.aClass172_2) {
            local1 = 196608;
        }
        this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, 11, this.anInt9148 | local1);
    }

    @OriginalMember(owner = "client!kea", name = "S", descriptor = "(I)V")
    @Override
    protected void method8069() {
        if (this.aClass77_6.method2049()) {
            this.aClass73_Sub1_18.method1891(Static325.aFloatArray36);
        } else {
            Static325.aFloatArray36[3] = 0.0F;
            Static325.aFloatArray36[8] = 0.0F;
            Static325.aFloatArray36[15] = 1.0F;
            Static325.aFloatArray36[12] = 0.0F;
            Static325.aFloatArray36[11] = 0.0F;
            Static325.aFloatArray36[13] = 0.0F;
            Static325.aFloatArray36[14] = 0.0F;
            Static325.aFloatArray36[1] = 0.0F;
            Static325.aFloatArray36[4] = 0.0F;
            Static325.aFloatArray36[5] = 1.0F;
            Static325.aFloatArray36[0] = 1.0F;
            Static325.aFloatArray36[6] = 0.0F;
            Static325.aFloatArray36[7] = 0.0F;
            Static325.aFloatArray36[9] = 0.0F;
            Static325.aFloatArray36[10] = 1.0F;
            Static325.aFloatArray36[2] = 0.0F;
        }
        this.anIDirect3DDevice1.SetTransform(2, Static325.aFloatArray36);
    }

    @OriginalMember(owner = "client!kea", name = "d", descriptor = "(Z)V")
    @Override
    protected void method8109() {
        if (this.aBoolean411) {
            return;
        }
        this.anIDirect3DDevice1.LightEnable(0, false);
        this.anIDirect3DDevice1.LightEnable(1, false);
        this.anIDirect3DDevice1.SetLight(0, this.aD3DLIGHT1);
        this.anIDirect3DDevice1.SetLight(1, this.aD3DLIGHT3);
        this.anIDirect3DDevice1.LightEnable(0, true);
        this.anIDirect3DDevice1.LightEnable(1, true);
        this.aBoolean411 = true;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "([Ljava/awt/Rectangle;III)V")
    @Override
    public void flipDirtyRect(@OriginalArg(0) Rectangle[] rectangles, @OriginalArg(1) int count, @OriginalArg(2) int x, @OriginalArg(3) int y) throws FlipException {
        this.flip(x, y);
    }

    @OriginalMember(owner = "client!kea", name = "U", descriptor = "(I)V")
    @Override
    public void method8158() {
        this.aD3DLIGHT1.SetDirection(-this.aFloatArray60[0], -this.aFloatArray60[1], -this.aFloatArray60[2]);
        this.aD3DLIGHT3.SetDirection(-this.aFloatArray61[0], -this.aFloatArray61[1], -this.aFloatArray61[2]);
        this.aBoolean411 = false;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!cfa;I)V")
    public void bindTexture3D(@OriginalArg(0) D3DTexture3D texture) {
        this.bindTexture(texture);
        if (!this.aBooleanArray15[this.anInt9148]) {
            this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 1, 1);
            this.aBooleanArray15[this.anInt9148] = true;
        }
        if (!this.aBooleanArray14[this.anInt9148]) {
            this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 2, 1);
            this.aBooleanArray14[this.anInt9148] = true;
        }
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(IB)V")
    @Override
    public void method8055(@OriginalArg(0) int arg0) {
        this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, 11, arg0);
    }

    @OriginalMember(owner = "client!kea", name = "I", descriptor = "(I)V")
    @Override
    protected void method8110() {
        this.anIDirect3DDevice1.SetTransform(3, this.aFloatArray63);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(IBILclient!ri;ILclient!fma;I)V")
    @Override
    public void method8052(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) Interface20 arg2, @OriginalArg(4) int arg3, @OriginalArg(5) Class131 arg4, @OriginalArg(6) int arg5) {
        this.anIDirect3DDevice1.SetIndices(((Class11) arg2).anIDirect3DIndexBuffer1);
        this.anIDirect3DDevice1.DrawIndexedPrimitive(Static325.method4865(arg4), 0, arg0, arg3, arg1, arg5);
    }

    @OriginalMember(owner = "client!kea", name = "w", descriptor = "()Z")
    @Override
    public boolean bloom() {
        return false;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!mk;I)V")
    @Override
    public void method8114(@OriginalArg(0) Class244 arg0) {
        @Pc(2) dxVertexLayout local2 = (dxVertexLayout) arg0;
        this.anIDirect3DDevice1.SetVertexDeclaration(local2.anIDirect3DVertexDeclaration1);
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!pl;I)V")
    public void bindTexture(@OriginalArg(0) D3DTexture texture) {
        this.anIDirect3DDevice1.SetTexture(this.anInt9148, texture.getTexture());
        if (texture.filter != this.aClass259Array1[this.anInt9148]) {
            @Pc(63) int local63 = Static325.method4860(texture.filter);
            this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 6, local63);
            this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 5, local63);
            this.aClass259Array1[this.anInt9148] = texture.filter;
            if (texture.mipmapped != this.aBooleanArray13[this.anInt9148]) {
                this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 7, texture.mipmapped ? Static325.method4860(texture.filter) : 0);
                this.aBooleanArray13[this.anInt9148] = texture.mipmapped;
            }
        } else if (texture.mipmapped != this.aBooleanArray13[this.anInt9148]) {
            this.anIDirect3DDevice1.SetSamplerState(this.anInt9148, 7, texture.mipmapped ? Static325.method4860(texture.filter) : 0);
            this.aBooleanArray13[this.anInt9148] = texture.mipmapped;
        }
        if (!this.aBooleanArray16[this.anInt9148]) {
            this.aBooleanArray16[this.anInt9148] = true;
            this.method8030();
            this.method8039();
        }
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(ILjava/awt/Canvas;Ljava/lang/Object;)V")
    @Override
    protected void method8051(@OriginalArg(1) Canvas arg0, @OriginalArg(2) Object arg1) {
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!pu;Lclient!pu;FLclient!pu;)Lclient!pu;")
    @Override
    public Class67 method8007(@OriginalArg(0) Class67 arg0, @OriginalArg(1) Class67 arg1, @OriginalArg(2) float arg2, @OriginalArg(3) Class67 arg3) {
        return arg2 < 0.5F ? arg0 : arg1;
    }

    @OriginalMember(owner = "client!kea", name = "a", descriptor = "(Lclient!hla;BZZI)V")
    @Override
    public void method8125(@OriginalArg(0) Class168 arg0, @OriginalArg(2) boolean arg1, @OriginalArg(3) boolean arg2, @OriginalArg(4) int arg3) {
        @Pc(26) byte local26;
        if (arg3 == 1) {
            local26 = 3;
        } else if (arg3 == 2) {
            local26 = 26;
        } else {
            local26 = 2;
        }
        @Pc(34) int local34 = 0;
        if (arg1) {
            local34 = 32;
        }
        if (arg2) {
            local34 |= 0x10;
        }
        this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, local26, Static325.method4862(arg0) | local34);
    }

    @OriginalMember(owner = "client!kea", name = "m", descriptor = "(B)V")
    @Override
    protected void method8089() {
        this.method8065();
        this.method8109();
    }

    @OriginalMember(owner = "client!kea", name = "c", descriptor = "(Z)V")
    @Override
    protected void method8030() {
        @Pc(16) int local16 = this.aBooleanArray16[this.anInt9148] ? Static325.method4857(this.aClass121Array5[this.anInt9148]) : 1;
        this.anIDirect3DDevice1.SetTextureStageState(this.anInt9148, 4, local16);
    }
}

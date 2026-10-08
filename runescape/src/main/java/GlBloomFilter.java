import com.jagex.graphics.skybox.SkyBoxSphere;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kca")
public final class GlBloomFilter extends GlPostProcessFilter {

    @OriginalMember(owner = "client!kca", name = "N", descriptor = "Lclient!iha;")
    public Class179 aClass179_2;

    @OriginalMember(owner = "client!kca", name = "E", descriptor = "I")
    public int anInt5098;

    @OriginalMember(owner = "client!kca", name = "W", descriptor = "Lclient!wo;")
    public Class406 aClass406_1;

    @OriginalMember(owner = "client!kca", name = "L", descriptor = "Lclient!wo;")
    public Class406 aClass406_2;

    @OriginalMember(owner = "client!kca", name = "B", descriptor = "Lclient!iha;")
    public Class179 aClass179_3;

    @OriginalMember(owner = "client!kca", name = "G", descriptor = "I")
    public int anInt5100;

    @OriginalMember(owner = "client!kca", name = "U", descriptor = "Lclient!iha;")
    public Class179 aClass179_4;

    @OriginalMember(owner = "client!kca", name = "D", descriptor = "I")
    public int anInt5104;

    @OriginalMember(owner = "client!kca", name = "T", descriptor = "Lclient!iha;")
    public Class179 aClass179_5;

    @OriginalMember(owner = "client!kca", name = "H", descriptor = "I")
    public int anInt5106;

    @OriginalMember(owner = "client!kca", name = "I", descriptor = "[Lclient!rq;")
    public Class93_Sub2[] aClass93_Sub2Array1;

    @OriginalMember(owner = "client!kca", name = "K", descriptor = "Lclient!rq;")
    public Class93_Sub2 aClass93_Sub2_1;

    @OriginalMember(owner = "client!kca", name = "R", descriptor = "Lclient!rq;")
    public Class93_Sub2 aClass93_Sub2_2;

    @OriginalMember(owner = "client!kca", name = "<init>", descriptor = "(Lclient!qha;)V")
    public GlBloomFilter(@OriginalArg(0) GlToolkit arg0) {
        super(arg0);
    }

    @OriginalMember(owner = "client!kca", name = "g", descriptor = "(I)Z")
    public boolean method4623() {
        return super.aClass19_Sub3_22.aBoolean613 && super.aClass19_Sub3_22.aBoolean619 && super.aClass19_Sub3_22.aBoolean603;
    }

    @OriginalMember(owner = "client!kca", name = "c", descriptor = "(B)V")
    @Override
    public void method4608() {
        this.aClass93_Sub2Array1 = null;
        this.aClass179_4 = null;
        this.aClass93_Sub2_1 = null;
        this.aClass406_2 = null;
        this.aClass406_1 = null;
        this.aClass179_2 = null;
        this.aClass93_Sub2_2 = null;
        this.aClass179_3 = null;
        this.aClass179_5 = null;
    }

    @OriginalMember(owner = "client!kca", name = "e", descriptor = "(B)Z")
    @Override
    public boolean method4617() {
        if (!super.aClass19_Sub3_22.aBoolean613 || !super.aClass19_Sub3_22.aBoolean619 || !super.aClass19_Sub3_22.aBoolean603) {
            return false;
        }
        this.aClass406_1 = new Class406(super.aClass19_Sub3_22);
        this.aClass93_Sub2_2 = new Class93_Sub2(super.aClass19_Sub3_22, 3553, 34842, 256, 256);
        this.aClass93_Sub2_2.method2946(false, false);
        this.aClass93_Sub2_1 = new Class93_Sub2(super.aClass19_Sub3_22, 3553, 34842, 256, 256);
        this.aClass93_Sub2_1.method2946(false, false);
        super.aClass19_Sub3_22.method6967(this.aClass406_1);
        this.aClass406_1.method9369(this.aClass93_Sub2_2, 0);
        this.aClass406_1.method9369(this.aClass93_Sub2_1, 1);
        this.aClass406_1.method9371(0);
        if (!this.aClass406_1.method9368()) {
            super.aClass19_Sub3_22.method6993(this.aClass406_1);
            return false;
        }
        super.aClass19_Sub3_22.method6993(this.aClass406_1);
        this.aClass179_2 = Static173.method2691(super.aClass19_Sub3_22, new Class240[]{Static245.method8629("#extension GL_ARB_texture_rectangle : enable\nuniform vec3 params;\nuniform sampler2DRect sceneTex;\nconst vec3 lumCoef = vec3(0.2126, 0.7152, 0.0722);\nvoid main() {\n    vec4 col = texture2DRect(sceneTex, gl_TexCoord[0].xy);\n    gl_FragColor = col*step(params.x, dot(lumCoef, col.rgb));\n}\n", super.aClass19_Sub3_22, 35632)});
        this.aClass179_5 = Static173.method2691(super.aClass19_Sub3_22, new Class240[]{Static245.method8629("uniform vec3 params;\nuniform sampler2D sceneTex;\nconst vec3 lumCoef = vec3(0.2126, 0.7152, 0.0722);\nvoid main() {\n    vec4 col = texture2D(sceneTex, gl_TexCoord[0].xy);\n    gl_FragColor = col*step(params.x, dot(lumCoef, col.rgb));\n}\n", super.aClass19_Sub3_22, 35632)});
        this.aClass179_3 = Static173.method2691(super.aClass19_Sub3_22, new Class240[]{Static245.method8629("#extension GL_ARB_texture_rectangle : enable\nuniform vec3 params;\nuniform vec3 dimScale;\nuniform sampler2D bloomTex;\nuniform sampler2DRect sceneTex;\nconst vec3 lumCoef = vec3(0.2126, 0.7152, 0.0722);\nvoid main() {\n\t vec4 bloomCol = texture2D(bloomTex, gl_TexCoord[1].xy);\n\t vec4 sceneCol = texture2DRect(sceneTex, gl_TexCoord[0].xy);\n\t float preLum = 0.99*dot(lumCoef, sceneCol.rgb)+0.01;\n    float postLum = preLum*(1.0+(preLum/params.y))/(preLum+1.0);\n\t gl_FragColor = sceneCol*(postLum/preLum)+bloomCol*params.x;\n}\n", super.aClass19_Sub3_22, 35632)});
        this.aClass179_4 = Static173.method2691(super.aClass19_Sub3_22, new Class240[]{Static245.method8629("uniform vec3 step;\nuniform sampler2D baseTex;\nvoid main() {\n\tvec4 fragCol = texture2D(baseTex, gl_TexCoord[0].xy)*0.091396265;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-1.0*step.xy))*0.088584304;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 1.0*step.xy))*0.088584304;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-2.0*step.xy))*0.08065692;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 2.0*step.xy))*0.08065692;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-3.0*step.xy))*0.068989515;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 3.0*step.xy))*0.068989515;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-4.0*step.xy))*0.055434637;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 4.0*step.xy))*0.055434637;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-5.0*step.xy))*0.04184426;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 5.0*step.xy))*0.04184426;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-6.0*step.xy))*0.029672023;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 6.0*step.xy))*0.029672023;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-7.0*step.xy))*0.019765828;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 7.0*step.xy))*0.019765828;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+(-8.0*step.xy))*0.012369139;\n\tfragCol += texture2D(baseTex, gl_TexCoord[0].xy+( 8.0*step.xy))*0.012369139;\n\tgl_FragColor = fragCol;\n}\n", super.aClass19_Sub3_22, 35632)});
        return this.aClass179_5 != null && this.aClass179_2 != null && this.aClass179_3 != null && this.aClass179_4 != null;
    }

    @OriginalMember(owner = "client!kca", name = "a", descriptor = "(Lclient!rq;Lclient!rq;II)V")
    @Override
    public void method4611(@OriginalArg(0) Class93_Sub2 arg0, @OriginalArg(1) Class93_Sub2 arg1, @OriginalArg(3) int arg2) {
        OpenGL.glPushAttrib(OpenGL.GL_VIEWPORT_BIT);
        OpenGL.glMatrixMode(OpenGL.GL_PROJECTION);
        OpenGL.glPushMatrix();
        OpenGL.glLoadIdentity();
        OpenGL.glOrtho(0.0D, 1.0D, 0.0D, 1.0D, -1.0D, 1.0D);
        @Pc(46) long local46;
        if (this.aClass93_Sub2Array1 == null) {
            super.aClass19_Sub3_22.method7001(arg1);
            super.aClass19_Sub3_22.method6967(this.aClass406_1);
            this.aClass406_1.method9371(0);
            OpenGL.glViewport(0, 0, 256, 256);
            local46 = this.aClass179_2.aLong136;
            OpenGL.glUseProgramObjectARB(local46);
            OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(local46, "sceneTex"), 0);
            OpenGL.glUniform3fARB(OpenGL.glGetUniformLocationARB(local46, "params"), Static228.aFloat72, 0.0F, 0.0F);
            OpenGL.glBegin(OpenGL.GL_QUADS);
            OpenGL.glTexCoord2f(0.0F, 0.0F);
            OpenGL.glVertex2i(0, 0);
            OpenGL.glTexCoord2f((float) this.anInt5104, 0.0F);
            OpenGL.glVertex2i(1, 0);
            OpenGL.glTexCoord2f((float) this.anInt5104, (float) this.anInt5098);
            OpenGL.glVertex2i(1, 1);
            OpenGL.glTexCoord2f(0.0F, (float) this.anInt5098);
            OpenGL.glVertex2i(0, 1);
            OpenGL.glEnd();
        } else {
            super.aClass19_Sub3_22.method6967(this.aClass406_2);
            @Pc(112) int local112 = SkyBoxSphere.method5587(this.anInt5104);
            @Pc(117) int local117 = SkyBoxSphere.method5587(this.anInt5098);
            @Pc(119) int local119 = 0;
            while (local112 > 256 || local117 > 256) {
                OpenGL.glViewport(0, 0, local112, local117);
                this.aClass406_2.method9369(this.aClass93_Sub2Array1[local119], 0);
                if (local119 == 0) {
                    super.aClass19_Sub3_22.method7001(arg1);
                    OpenGL.glBegin(OpenGL.GL_QUADS);
                    OpenGL.glTexCoord2f(0.0F, 0.0F);
                    OpenGL.glVertex2i(0, 0);
                    OpenGL.glTexCoord2f((float) this.anInt5104, 0.0F);
                    OpenGL.glVertex2i(1, 0);
                    OpenGL.glTexCoord2f((float) this.anInt5104, (float) this.anInt5098);
                    OpenGL.glVertex2i(1, 1);
                    OpenGL.glTexCoord2f(0.0F, (float) this.anInt5098);
                    OpenGL.glVertex2i(0, 1);
                    OpenGL.glEnd();
                } else {
                    super.aClass19_Sub3_22.method7001(this.aClass93_Sub2Array1[local119 - 1]);
                    OpenGL.glBegin(OpenGL.GL_QUADS);
                    OpenGL.glTexCoord2f(0.0F, 0.0F);
                    OpenGL.glVertex2i(0, 0);
                    OpenGL.glTexCoord2f(1.0F, 0.0F);
                    OpenGL.glVertex2i(1, 0);
                    OpenGL.glTexCoord2f(1.0F, 1.0F);
                    OpenGL.glVertex2i(1, 1);
                    OpenGL.glTexCoord2f(0.0F, 1.0F);
                    OpenGL.glVertex2i(0, 1);
                    OpenGL.glEnd();
                }
                if (local117 > 256) {
                    local117 >>= 0x1;
                }
                if (local112 > 256) {
                    local112 >>= 0x1;
                }
                local119++;
            }
            super.aClass19_Sub3_22.method6993(this.aClass406_2);
            super.aClass19_Sub3_22.method7001(this.aClass93_Sub2Array1[local119 - 1]);
            super.aClass19_Sub3_22.method6967(this.aClass406_1);
            this.aClass406_1.method9371(0);
            OpenGL.glViewport(0, 0, 256, 256);
            @Pc(291) long local291 = this.aClass179_5.aLong136;
            OpenGL.glUseProgramObjectARB(local291);
            OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(local291, "sceneTex"), 0);
            OpenGL.glUniform3fARB(OpenGL.glGetUniformLocationARB(local291, "params"), Static228.aFloat72, 0.0F, 0.0F);
            OpenGL.glBegin(OpenGL.GL_QUADS);
            OpenGL.glTexCoord2f(0.0F, 0.0F);
            OpenGL.glVertex2i(0, 0);
            OpenGL.glTexCoord2f(1.0F, 0.0F);
            OpenGL.glVertex2i(1, 0);
            OpenGL.glTexCoord2f(1.0F, 1.0F);
            OpenGL.glVertex2i(1, 1);
            OpenGL.glTexCoord2f(0.0F, 1.0F);
            OpenGL.glVertex2i(0, 1);
            OpenGL.glEnd();
        }
        this.aClass406_1.method9371(1);
        super.aClass19_Sub3_22.method7001(this.aClass93_Sub2_2);
        local46 = this.aClass179_4.aLong136;
        OpenGL.glUseProgramObjectARB(local46);
        OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(local46, "baseTex"), 0);
        OpenGL.glUniform3fARB(OpenGL.glGetUniformLocationARB(local46, "step"), 0.00390625F, 0.0F, 0.0F);
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glTexCoord2f(0.0F, 0.0F);
        OpenGL.glVertex2i(0, 0);
        OpenGL.glTexCoord2f(1.0F, 0.0F);
        OpenGL.glVertex2i(1, 0);
        OpenGL.glTexCoord2f(1.0F, 1.0F);
        OpenGL.glVertex2i(1, 1);
        OpenGL.glTexCoord2f(0.0F, 1.0F);
        OpenGL.glVertex2i(0, 1);
        OpenGL.glEnd();
        this.aClass406_1.method9371(0);
        super.aClass19_Sub3_22.method7001(this.aClass93_Sub2_1);
        OpenGL.glUniform3fARB(OpenGL.glGetUniformLocationARB(local46, "step"), 0.0F, 0.00390625F, 0.0F);
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glTexCoord2f(0.0F, 0.0F);
        OpenGL.glVertex2i(0, 0);
        OpenGL.glTexCoord2f(1.0F, 0.0F);
        OpenGL.glVertex2i(1, 0);
        OpenGL.glTexCoord2f(1.0F, 1.0F);
        OpenGL.glVertex2i(1, 1);
        OpenGL.glTexCoord2f(0.0F, 1.0F);
        OpenGL.glVertex2i(0, 1);
        OpenGL.glEnd();
        OpenGL.glPopAttrib();
        OpenGL.glPopMatrix();
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        super.aClass19_Sub3_22.method6993(this.aClass406_1);
        @Pc(459) long local459 = this.aClass179_3.aLong136;
        OpenGL.glUseProgramObjectARB(local459);
        OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(local459, "sceneTex"), 0);
        OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(local459, "bloomTex"), 1);
        OpenGL.glUniform3fARB(OpenGL.glGetUniformLocationARB(local459, "params"), Static656.aFloat127, Static626.aFloat199, 0.0F);
        super.aClass19_Sub3_22.method7014(1);
        super.aClass19_Sub3_22.method7001(this.aClass93_Sub2_2);
        super.aClass19_Sub3_22.method7014(0);
        super.aClass19_Sub3_22.method7001(arg1);
    }

    @OriginalMember(owner = "client!kca", name = "a", descriptor = "(BI)V")
    @Override
    public void method4607(@OriginalArg(1) int arg0) {
        OpenGL.glUseProgramObjectARB(0L);
        super.aClass19_Sub3_22.method7014(1);
        super.aClass19_Sub3_22.method7001(null);
        super.aClass19_Sub3_22.method7014(0);
    }

    @OriginalMember(owner = "client!kca", name = "b", descriptor = "(B)Z")
    @Override
    public boolean method4609() {
        return this.aClass406_1 != null;
    }

    @OriginalMember(owner = "client!kca", name = "c", descriptor = "(I)I")
    @Override
    public int method4612() {
        return 1;
    }

    @OriginalMember(owner = "client!kca", name = "a", descriptor = "(III)V")
    @Override
    public void method4614(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        this.anInt5104 = arg0;
        this.anInt5098 = arg1;
        @Pc(23) int local23 = SkyBoxSphere.method5587(this.anInt5104);
        @Pc(28) int local28 = SkyBoxSphere.method5587(this.anInt5098);
        if (this.anInt5100 == local23 && this.anInt5106 == local28) {
            return;
        }
        @Pc(49) int local49;
        if (this.aClass93_Sub2Array1 != null) {
            for (local49 = 0; local49 < this.aClass93_Sub2Array1.length; local49++) {
                this.aClass93_Sub2Array1[local49].method9442();
            }
            this.aClass93_Sub2Array1 = null;
        }
        if (local23 <= 256 && local28 <= 256) {
            this.aClass406_2 = null;
        } else {
            local49 = local23;
            @Pc(99) int local99 = local28;
            @Pc(101) int local101 = 0;
            if (this.aClass406_2 == null) {
                this.aClass406_2 = new Class406(super.aClass19_Sub3_22);
            }
            label54:
            while (true) {
                if (local49 <= 256 && local99 <= 256) {
                    local99 = local28;
                    this.aClass93_Sub2Array1 = new Class93_Sub2[local101];
                    local49 = local23;
                    local101 = 0;
                    while (true) {
                        if (local49 <= 256 && local99 <= 256) {
                            break label54;
                        }
                        this.aClass93_Sub2Array1[local101++] = new Class93_Sub2(super.aClass19_Sub3_22, 3553, 34842, local49, local99);
                        if (local49 > 256) {
                            local49 >>= 0x1;
                        }
                        if (local99 > 256) {
                            local99 >>= 0x1;
                        }
                    }
                }
                local101++;
                if (local49 > 256) {
                    local49 >>= 0x1;
                }
                if (local99 > 256) {
                    local99 >>= 0x1;
                }
            }
        }
        this.anInt5100 = local23;
        this.anInt5106 = local28;
    }
}

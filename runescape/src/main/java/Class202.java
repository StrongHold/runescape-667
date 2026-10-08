import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kaa")
public final class Class202 {

    @OriginalMember(owner = "client!kaa", name = "d", descriptor = "Lclient!iha;")
    public Class179 aClass179_1;

    @OriginalMember(owner = "client!kaa", name = "e", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_21;

    @OriginalMember(owner = "client!kaa", name = "<init>", descriptor = "(Lclient!qha;)V")
    public Class202(@OriginalArg(0) GlToolkit arg0) {
        this.aClass19_Sub3_21 = arg0;
    }

    @OriginalMember(owner = "client!kaa", name = "a", descriptor = "(BFLclient!vv;Lclient!vv;)Z")
    public boolean method4580(@OriginalArg(1) float arg0, @OriginalArg(2) GlVolumeTexture arg1, @OriginalArg(3) GlVolumeTexture arg2) {
        if (!this.method4582()) {
            return false;
        }
        @Pc(14) Class406 local14 = this.aClass19_Sub3_21.aClass406_7;
        @Pc(25) DoublyLinkedNode_Sub2_Sub19 local25 = new DoublyLinkedNode_Sub2_Sub19(this.aClass19_Sub3_21, 6408, arg1.anInt10410, arg1.anInt10401);
        @Pc(35) boolean local35 = false;
        this.aClass19_Sub3_21.method6967(local14);
        local14.method9366(local25, 0);
        if (local14.method9368()) {
            OpenGL.glPushMatrix();
            OpenGL.glLoadIdentity();
            OpenGL.glMatrixMode(OpenGL.GL_PROJECTION);
            OpenGL.glPushMatrix();
            OpenGL.glLoadIdentity();
            OpenGL.glOrtho(0.0D, 1.0D, 0.0D, 1.0D, -1.0D, 1.0D);
            OpenGL.glPushAttrib(OpenGL.GL_VIEWPORT_BIT);
            OpenGL.glViewport(0, 0, arg1.anInt10410, arg1.anInt10401);
            OpenGL.glUseProgramObjectARB(this.aClass179_1.aLong136);
            OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(this.aClass179_1.aLong136, "heightMap"), 0);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(this.aClass179_1.aLong136, "rcpRelief"), 1.0F / arg0);
            OpenGL.glUniform2fARB(OpenGL.glGetUniformLocationARB(this.aClass179_1.aLong136, "sampleSize"), 1.0F / (float) arg2.anInt10410, 1.0F / (float) arg2.anInt10401);
            for (@Pc(115) int local115 = 0; local115 < arg1.anInt10403; local115++) {
                @Pc(123) float local123 = (float) local115 / (float) arg1.anInt10403;
                this.aClass19_Sub3_21.method7001(arg2);
                OpenGL.glBegin(OpenGL.GL_QUADS);
                OpenGL.glTexCoord3f(0.0F, 0.0F, local123);
                OpenGL.glVertex2f(0.0F, 0.0F);
                OpenGL.glTexCoord3f(1.0F, 0.0F, local123);
                OpenGL.glVertex2f(1.0F, 0.0F);
                OpenGL.glTexCoord3f(1.0F, 1.0F, local123);
                OpenGL.glVertex2f(1.0F, 1.0F);
                OpenGL.glTexCoord3f(0.0F, 1.0F, local123);
                OpenGL.glVertex2f(0.0F, 1.0F);
                OpenGL.glEnd();
                arg1.method9032(local115, arg1.anInt10410, arg1.anInt10401);
            }
            OpenGL.glUseProgramObjectARB(0L);
            OpenGL.glPopAttrib();
            OpenGL.glPopMatrix();
            OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
            local35 = true;
            OpenGL.glPopMatrix();
        }
        local14.method9363(0);
        this.aClass19_Sub3_21.method6993(local14);
        return local35;
    }

    @OriginalMember(owner = "client!kaa", name = "b", descriptor = "(I)Z")
    public boolean method4582() {
        if (this.aClass19_Sub3_21.aBoolean613 && this.aClass19_Sub3_21.aBoolean619 && this.aClass179_1 == null) {
            @Pc(23) Class240 local23 = Static245.method8629("uniform float rcpRelief;\nuniform vec2 sampleSize;\nuniform sampler3D heightMap;\nvoid main() {\nfloat dx = texture3D(heightMap, vec3(-sampleSize.x, 0.0, 0.0)+gl_TexCoord[0].xyz).r - texture3D(heightMap, vec3(sampleSize.x, 0.0, 0.0)+gl_TexCoord[0].xyz).r;\nfloat dy = texture3D(heightMap, vec3(0.0, -sampleSize.y, 0.0)+gl_TexCoord[0].xyz).r - texture3D(heightMap, vec3(0.0, sampleSize.y, 0.0)+gl_TexCoord[0].xyz).r;\ngl_FragColor = vec4(0.5+normalize(vec3(dx, dy, rcpRelief))*0.5, texture3D(heightMap, gl_TexCoord[0].xyz).r);\n}\n", this.aClass19_Sub3_21, 35632);
            if (local23 != null) {
                this.aClass179_1 = Static173.method2691(this.aClass19_Sub3_21, new Class240[]{local23});
            }
        }
        return this.aClass179_1 != null;
    }
}

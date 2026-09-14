import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Reflects the scene environment cube map off the surface using {@code GL_REFLECTION_MAP} texture coordinate
 * generation, with the inverse view rotation loaded into the texture matrix so the reflection stays world aligned.
 */
@OriginalClass("client!gn")
public final class ReflectionMapEffect extends TextureEffect {

    @OriginalMember(owner = "client!gn", name = "m", descriptor = "Z")
    public boolean active = false;

    @OriginalMember(owner = "client!gn", name = "k", descriptor = "Lclient!bea;")
    public Class36 displayList;

    @OriginalMember(owner = "client!gn", name = "<init>", descriptor = "(Lclient!qha;)V")
    public ReflectionMapEffect(@OriginalArg(0) GlToolkit toolkit) {
        super(toolkit);
        if (toolkit.aBoolean598) {
            this.displayList = new Class36(toolkit, DISPLAY_LIST_COUNT);
            this.displayList.method1002(ENABLE_LIST);
            super.toolkit.method7014(1);
            super.toolkit.method7031(GL_REPLACE, GL_INTERPOLATE);
            super.toolkit.method7021(OpenGL.GL_PREVIOUS, OpenGL.GL_SRC_ALPHA, 2);
            super.toolkit.method7029(0, GL_PRIMARY_COLOR);
            OpenGL.glTexGeni(OpenGL.GL_S, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_REFLECTION_MAP);
            OpenGL.glTexGeni(OpenGL.GL_T, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_REFLECTION_MAP);
            OpenGL.glTexGeni(OpenGL.GL_R, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_REFLECTION_MAP);
            OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_S);
            OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_T);
            OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_R);
            super.toolkit.method7014(0);
            this.displayList.method1004();
            this.displayList.method1002(DISABLE_LIST);
            super.toolkit.method7014(1);
            super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
            super.toolkit.method7021(OpenGL.GL_CONSTANT, OpenGL.GL_SRC_ALPHA, 2);
            super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
            OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_S);
            OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_T);
            OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_R);
            OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
            OpenGL.glLoadIdentity();
            OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
            super.toolkit.method7014(0);
            this.displayList.method1004();
        }
    }

    @OriginalMember(owner = "client!gn", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.active) {
            this.displayList.method1005(DISABLE_LIST);
            super.toolkit.method7014(1);
            super.toolkit.method7001(null);
            super.toolkit.method7014(0);
        } else {
            super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
        }
        super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
        this.active = false;
    }

    @OriginalMember(owner = "client!gn", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        @Pc(8) Class93_Sub1 environmentMap = super.toolkit.method6963();
        if (this.displayList == null || environmentMap == null || !lit) {
            super.toolkit.method7029(0, OpenGL.GL_PREVIOUS);
        } else {
            this.displayList.method1005(ENABLE_LIST);
            super.toolkit.method7014(1);
            super.toolkit.method7001(environmentMap);
            OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
            OpenGL.glLoadMatrixf(super.toolkit.aClass73_Sub3_5.method7145(), 0);
            OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
            super.toolkit.method7014(0);
            this.active = true;
        }
    }

    @OriginalMember(owner = "client!gn", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!gn", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method7001(texture);
        super.toolkit.method6991(colourOp);
    }

    @OriginalMember(owner = "client!gn", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
    }

    @OriginalMember(owner = "client!gn", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
        super.toolkit.method7031(GL_REPLACE, OpenGL.GL_MODULATE);
    }
}

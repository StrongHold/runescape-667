import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!br")
public final class GlTexture3D extends GlTexture implements Interface2 {

    private static final int GL_TEXTURE_3D = 0x806F;

    @OriginalMember(owner = "client!br", name = "y", descriptor = "I")
    public final int depth;

    @OriginalMember(owner = "client!br", name = "E", descriptor = "I")
    public final int width;

    @OriginalMember(owner = "client!br", name = "A", descriptor = "I")
    public final int height;

    @OriginalMember(owner = "client!br", name = "<init>", descriptor = "(Lclient!tca;Lclient!eba;III[B)V")
    public GlTexture3D(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int depth, @OriginalArg(5) byte[] voxels) {
        super(toolkit, GL_TEXTURE_3D, format, Static702.aClass397_16, width * height * depth, false);
        this.depth = depth;
        this.width = width;
        this.height = height;
        super.toolkit.method8088(this);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ALIGNMENT, BYTE_UNPACK_ALIGNMENT);
        OpenGL.glTexImage3Dub(super.target, BASE_LEVEL, this.getInternalFormat(), this.width, this.height, this.depth, NO_BORDER, Static468.toGlPixelFormat(super.format), OpenGL.GL_UNSIGNED_BYTE, voxels, 0);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ALIGNMENT, DEFAULT_UNPACK_ALIGNMENT);
    }
}

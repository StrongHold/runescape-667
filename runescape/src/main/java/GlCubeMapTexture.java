import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nd")
public final class GlCubeMapTexture extends GlTexture implements Interface8 {

    private static final int GL_TEXTURE_CUBE_MAP = 0x8513;

    private static final int FACE_COUNT = 6;

    @OriginalMember(owner = "client!nd", name = "<init>", descriptor = "(Lclient!tca;IZ[[I)V")
    public GlCubeMapTexture(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) int size, @OriginalArg(2) boolean mipmapped, @OriginalArg(3) int[][] faces) {
        super(toolkit, GL_TEXTURE_CUBE_MAP, Static172.aClass92_8, Static702.aClass397_16, size * FACE_COUNT * size, mipmapped);
        super.toolkit.method8088(this);
        @Pc(22) int face;
        if (mipmapped) {
            for (face = 0; face < FACE_COUNT; face++) {
                this.uploadWithMipmaps(face + OpenGL.GL_TEXTURE_CUBE_MAP_POSITIVE_X, faces[face], size, size);
            }
        } else {
            for (face = 0; face < FACE_COUNT; face++) {
                OpenGL.glTexImage2Di(face + OpenGL.GL_TEXTURE_CUBE_MAP_POSITIVE_X, BASE_LEVEL, this.getInternalFormat(), size, size, NO_BORDER, Static468.method7644(super.format), super.toolkit.anInt9277, faces[face], 0);
            }
        }
    }
}

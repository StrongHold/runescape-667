import com.jagex.core.util.Arrays;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!of")
public final class GlRectangleTexture extends GlTexture implements Interface18 {

    private static final int GL_TEXTURE_RECTANGLE = 0x84F5;

    private static final int NO_OFFSET = 0;

    @OriginalMember(owner = "client!of", name = "I", descriptor = "I")
    public final int height;

    @OriginalMember(owner = "client!of", name = "E", descriptor = "I")
    public final int width;

    @OriginalMember(owner = "client!of", name = "<init>", descriptor = "(Lclient!tca;Lclient!eba;II[FII)V")
    public GlRectangleTexture(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) float[] texels, @OriginalArg(5) int offset, @OriginalArg(6) int stride) {
        super(toolkit, GL_TEXTURE_RECTANGLE, format, Static702.aClass397_20, height * width, false);
        this.height = height;
        this.width = width;
        super.toolkit.method8088(this);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, stride);
        OpenGL.glTexImage2Df(super.target, BASE_LEVEL, this.getInternalFormat(), width, height, NO_BORDER, Static468.toGlPixelFormat(super.format), OpenGL.GL_FLOAT, texels, offset * Float.BYTES);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, PACKED_ROW_LENGTH);
    }

    @OriginalMember(owner = "client!of", name = "<init>", descriptor = "(Lclient!tca;II[III)V")
    public GlRectangleTexture(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) int width, @OriginalArg(2) int height, @OriginalArg(3) int[] pixels, @OriginalArg(4) int offset, @OriginalArg(5) int stride) {
        super(toolkit, GL_TEXTURE_RECTANGLE, Static172.aClass92_8, Static702.aClass397_16, height * width, false);
        this.width = width;
        this.height = height;
        super.toolkit.method8088(this);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, stride);
        OpenGL.glTexImage2Di(super.target, BASE_LEVEL, OpenGL.GL_RGBA, this.width, this.height, NO_BORDER, OpenGL.GL_BGRA, super.toolkit.anInt9277, pixels, offset * Integer.BYTES);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, PACKED_ROW_LENGTH);
    }

    @OriginalMember(owner = "client!of", name = "<init>", descriptor = "(Lclient!tca;Lclient!eba;Lclient!wda;II)V")
    public GlRectangleTexture(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) Class397 type, @OriginalArg(3) int width, @OriginalArg(4) int height) {
        super(toolkit, GL_TEXTURE_RECTANGLE, format, type, width * height, false);
        this.width = width;
        this.height = height;
        super.toolkit.method8088(this);
        OpenGL.glTexImage2Dub(super.target, BASE_LEVEL, this.getInternalFormat(), width, height, NO_BORDER, Static468.toGlPixelFormat(super.format), Static248.method3526(super.type), null, 0);
    }

    @OriginalMember(owner = "client!of", name = "<init>", descriptor = "(Lclient!tca;Lclient!eba;II[BII)V")
    public GlRectangleTexture(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) byte[] texels, @OriginalArg(5) int offset, @OriginalArg(6) int stride) {
        super(toolkit, GL_TEXTURE_RECTANGLE, format, Static702.aClass397_16, width * height, false);
        this.width = width;
        this.height = height;
        super.toolkit.method8088(this);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ALIGNMENT, BYTE_UNPACK_ALIGNMENT);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, stride);
        OpenGL.glTexImage2Dub(super.target, BASE_LEVEL, this.getInternalFormat(), width, height, NO_BORDER, Static468.toGlPixelFormat(super.format), OpenGL.GL_UNSIGNED_BYTE, texels, offset);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, PACKED_ROW_LENGTH);
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ALIGNMENT, DEFAULT_UNPACK_ALIGNMENT);
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "(BII[IIII)V")
    @Override
    public void method9044(@OriginalArg(3) int[] pixels, @OriginalArg(5) int width, @OriginalArg(6) int height) {
        @Pc(12) int[] texels = new int[this.width * this.height];
        super.toolkit.method8088(this);
        OpenGL.glGetTexImagei(super.target, BASE_LEVEL, OpenGL.GL_BGRA, OpenGL.GL_UNSIGNED_BYTE, texels, 0);
        for (@Pc(32) int row = 0; row < height; row++) {
            Arrays.copy(texels, (height - row - 1) * this.width, pixels, width * row, width);
        }
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "(I)Z")
    @Override
    public boolean method9049() {
        return false;
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "(FI)F")
    @Override
    public float method9046(@OriginalArg(0) float x) {
        return x;
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "(B)I")
    @Override
    public int method9047() {
        return this.height;
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "([IIIIIIII)V")
    @Override
    public void method9048(@OriginalArg(0) int[] pixels, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int height, @OriginalArg(5) int width, @OriginalArg(7) int stride) {
        if (stride == 0) {
            stride = width;
        }
        super.toolkit.method8088(this);
        if (width != stride) {
            OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, stride);
        }
        OpenGL.glTexSubImage2Di(super.target, BASE_LEVEL, x, y, width, height, OpenGL.GL_BGRA, super.toolkit.anInt9277, pixels, 0);
        if (width != stride) {
            OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, PACKED_ROW_LENGTH);
        }
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "(ZZZ)V")
    @Override
    public void method9052(@OriginalArg(0) boolean repeatU, @OriginalArg(1) boolean repeatV) {
    }

    @OriginalMember(owner = "client!of", name = "b", descriptor = "(I)I")
    @Override
    public int method9045() {
        return this.width;
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "(IIII[BILclient!eba;II)V")
    @Override
    public void method9051(@OriginalArg(1) int stride, @OriginalArg(2) int height, @OriginalArg(4) byte[] texels, @OriginalArg(6) Class92 format, @OriginalArg(8) int width) {
        super.toolkit.method8088(this);
        if (stride == 0) {
            stride = width;
        }
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ALIGNMENT, BYTE_UNPACK_ALIGNMENT);
        if (width != stride) {
            OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, stride);
        }
        OpenGL.glTexSubImage2Dub(super.target, BASE_LEVEL, NO_OFFSET, NO_OFFSET, width, height, Static468.toGlPixelFormat(format), OpenGL.GL_UNSIGNED_BYTE, texels, 0);
        if (stride != width) {
            OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ROW_LENGTH, PACKED_ROW_LENGTH);
        }
        OpenGL.glPixelStorei(OpenGL.GL_UNPACK_ALIGNMENT, DEFAULT_UNPACK_ALIGNMENT);
    }

    @OriginalMember(owner = "client!of", name = "a", descriptor = "(FB)F")
    @Override
    public float method9050(@OriginalArg(0) float y) {
        return y;
    }
}

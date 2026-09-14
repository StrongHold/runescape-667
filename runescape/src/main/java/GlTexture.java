import com.jagex.graphics.texture.Node_Sub1_Sub27;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!hka")
public abstract class GlTexture implements Interface17 {

    protected static final int BASE_LEVEL = 0;

    protected static final int NO_BORDER = 0;

    protected static final int PACKED_ROW_LENGTH = 0;

    protected static final int BYTE_UNPACK_ALIGNMENT = 1;

    protected static final int DEFAULT_UNPACK_ALIGNMENT = 4;

    private static final int GL_RGB = 0x1907;

    private static final int GL_LUMINANCE = 0x1909;

    private static final int GL_LUMINANCE_ALPHA = 0x190A;

    private static final int GL_DEPTH = 0x1801;

    private static final int GL_RGBA32F = 0x8814;

    private static final int GL_RGB32F = 0x8815;

    private static final int GL_ALPHA32F = 0x8816;

    private static final int GL_LUMINANCE32F = 0x8818;

    private static final int GL_LUMINANCE_ALPHA32F = 0x8819;

    private static final int GL_RGB16F = 0x881B;

    private static final int GL_ALPHA16F = 0x881C;

    private static final int GL_LUMINANCE16F = 0x881E;

    private static final int GL_LUMINANCE_ALPHA16F = 0x881F;

    /**
     * A full mipmap chain adds a third again on top of the base level.
     */
    private static final int MIPMAP_SIZE_NUMERATOR = 4;

    private static final int MIPMAP_SIZE_DENOMINATOR = 3;

    @OriginalMember(owner = "client!hka", name = "g", descriptor = "Lclient!nga;")
    public Class259 filter = Static60.aClass259_3;

    @OriginalMember(owner = "client!hka", name = "e", descriptor = "Lclient!eba;")
    protected final Class92 format;

    @OriginalMember(owner = "client!hka", name = "d", descriptor = "I")
    protected final int target;

    @OriginalMember(owner = "client!hka", name = "u", descriptor = "Lclient!wda;")
    protected final Class397 type;

    @OriginalMember(owner = "client!hka", name = "m", descriptor = "Lclient!tca;")
    protected final GlxToolkit toolkit;

    @OriginalMember(owner = "client!hka", name = "c", descriptor = "I")
    public final int texelCount;

    @OriginalMember(owner = "client!hka", name = "w", descriptor = "Z")
    public final boolean mipmapped;

    @OriginalMember(owner = "client!hka", name = "k", descriptor = "I")
    public int id;

    @OriginalMember(owner = "client!hka", name = "<init>", descriptor = "(Lclient!tca;ILclient!eba;Lclient!wda;IZ)V")
    protected GlTexture(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) int target, @OriginalArg(2) Class92 format, @OriginalArg(3) Class397 type, @OriginalArg(4) int texelCount, @OriginalArg(5) boolean mipmapped) {
        this.format = format;
        this.target = target;
        this.type = type;
        this.toolkit = toolkit;
        this.texelCount = texelCount;
        this.mipmapped = mipmapped;
        OpenGL.glGenTextures(1, Static374.anIntArray457, 0);
        this.id = Static374.anIntArray457[0];
        this.applyFilter();
        this.trackAllocation();
    }

    @OriginalMember(owner = "client!hka", name = "a", descriptor = "(Z)V")
    @Override
    public final void method9043() {
        @Pc(8) int unit = this.toolkit.method8026();
        @Pc(21) int enabled = this.toolkit.anIntArray712[unit];
        if (this.target != enabled) {
            if (enabled != 0) {
                OpenGL.glBindTexture(enabled, 0);
                OpenGL.glDisable(enabled);
            }
            OpenGL.glEnable(this.target);
            this.toolkit.anIntArray712[unit] = this.target;
        }
        OpenGL.glBindTexture(this.target, this.id);
    }

    @OriginalMember(owner = "client!hka", name = "g", descriptor = "(I)I")
    protected final int getInternalFormat() {
        if (Static702.aClass397_16 == this.type) {
            if (this.format == Static685.aClass92_16) {
                return GL_RGB;
            } else if (this.format == Static172.aClass92_8) {
                return OpenGL.GL_RGBA;
            } else if (this.format == Static679.aClass92_15) {
                return OpenGL.GL_ALPHA;
            } else if (Static661.aClass92_10 == this.format) {
                return GL_LUMINANCE;
            } else if (Static482.aClass92_13 == this.format) {
                return GL_LUMINANCE_ALPHA;
            } else if (Static42.aClass92_3 == this.format) {
                return GL_DEPTH;
            }
        } else if (Static702.aClass397_19 == this.type) {
            if (Static685.aClass92_16 == this.format) {
                return GL_RGB16F;
            } else if (this.format == Static172.aClass92_8) {
                return OpenGL.GL_RGBA16F;
            } else if (Static679.aClass92_15 == this.format) {
                return GL_ALPHA16F;
            } else if (Static661.aClass92_10 == this.format) {
                return GL_LUMINANCE16F;
            } else if (this.format == Static482.aClass92_13) {
                return GL_LUMINANCE_ALPHA16F;
            } else if (Static42.aClass92_3 == this.format) {
                return GL_DEPTH;
            }
        } else if (Static702.aClass397_20 == this.type) {
            if (this.format == Static685.aClass92_16) {
                return GL_RGB32F;
            } else if (this.format == Static172.aClass92_8) {
                return GL_RGBA32F;
            } else if (this.format == Static679.aClass92_15) {
                return GL_ALPHA32F;
            } else if (this.format == Static661.aClass92_10) {
                return GL_LUMINANCE32F;
            } else if (this.format == Static482.aClass92_13) {
                return GL_LUMINANCE_ALPHA32F;
            } else if (Static42.aClass92_3 == this.format) {
                return GL_DEPTH;
            }
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!hka", name = "finalize", descriptor = "()V")
    @Override
    public final void finalize() throws Throwable {
        this.delete();
        super.finalize();
    }

    @OriginalMember(owner = "client!hka", name = "e", descriptor = "(I)I")
    public int getSizeInBytes() {
        @Pc(20) int size = this.texelCount * this.type.anInt10568 * this.format.anInt2416;
        return this.mipmapped ? size * MIPMAP_SIZE_NUMERATOR / MIPMAP_SIZE_DENOMINATOR : size;
    }

    @OriginalMember(owner = "client!hka", name = "c", descriptor = "(I)V")
    public void applyFilter() {
        this.toolkit.method8088(this);
        if (Static60.aClass259_3 == this.filter) {
            OpenGL.glTexParameteri(this.target, OpenGL.GL_TEXTURE_MIN_FILTER, this.mipmapped ? OpenGL.GL_LINEAR_MIPMAP_LINEAR : OpenGL.GL_LINEAR);
            OpenGL.glTexParameteri(this.target, OpenGL.GL_TEXTURE_MAG_FILTER, OpenGL.GL_LINEAR);
        } else {
            OpenGL.glTexParameteri(this.target, OpenGL.GL_TEXTURE_MIN_FILTER, this.mipmapped ? OpenGL.GL_NEAREST_MIPMAP_NEAREST : OpenGL.GL_NEAREST);
            OpenGL.glTexParameteri(this.target, OpenGL.GL_TEXTURE_MAG_FILTER, OpenGL.GL_NEAREST);
        }
    }

    @OriginalMember(owner = "client!hka", name = "a", descriptor = "(BLclient!nga;)V")
    @Override
    public final void method9041(@OriginalArg(1) Class259 filter) {
        if (this.filter != filter) {
            this.filter = filter;
            this.applyFilter();
        }
    }

    @OriginalMember(owner = "client!hka", name = "a", descriptor = "(I[IIII)V")
    protected final void uploadWithMipmaps(@OriginalArg(0) int target, @OriginalArg(1) int[] pixels, @OriginalArg(3) int height, @OriginalArg(4) int width) {
        if (width > 0 && !Node_Sub1_Sub27.method9150(width)) {
            throw new IllegalArgumentException("");
        } else if (height > 0 && !Node_Sub1_Sub27.method9150(height)) {
            throw new IllegalArgumentException("");
        } else if (this.format == Static172.aClass92_8) {
            @Pc(45) int level = 0;
            @Pc(53) int smallestSide = width < height ? width : height;
            @Pc(57) int halfWidth = width >> 1;
            @Pc(61) int halfHeight = height >> 1;
            @Pc(71) int[] source = pixels;
            @Pc(76) int[] scratch = new int[halfHeight * halfWidth];
            while (true) {
                OpenGL.glTexImage2Di(target, level, this.getInternalFormat(), width, height, NO_BORDER, OpenGL.GL_BGRA, this.toolkit.anInt9277, source, 0);
                if (smallestSide <= 1) {
                    return;
                }
                @Pc(96) int destIndex = 0;
                @Pc(98) int topIndex = 0;
                @Pc(102) int bottomIndex = width;
                @Pc(104) int[] downsampled = scratch;
                for (@Pc(106) int y = 0; y < halfHeight; y++) {
                    for (@Pc(112) int x = 0; x < halfWidth; x++) {
                        @Pc(121) int topLeft = source[topIndex++];
                        @Pc(126) int topRight = source[topIndex++];
                        @Pc(131) int bottomLeft = source[bottomIndex++];
                        @Pc(137) int greenTopLeft = topLeft >> 8 & 0xFF;
                        @Pc(143) int alphaTopLeft = topLeft >> 24 & 0xFF;
                        @Pc(149) int redTopLeft = topLeft >> 16 & 0xFF;
                        @Pc(154) int bottomRight = source[bottomIndex++];
                        @Pc(158) int blueTopLeft = topLeft & 0xFF;
                        @Pc(166) int alphaTop = alphaTopLeft + (topRight >> 24 & 0xFF);
                        @Pc(174) int redTop = redTopLeft + (topRight >> 16 & 0xFF);
                        @Pc(180) int blueTop = blueTopLeft + (topRight & 0xFF);
                        @Pc(188) int greenTop = greenTopLeft + (topRight >> 8 & 0xFF);
                        @Pc(196) int greenPartial = greenTop + (bottomLeft >> 8 & 0xFF);
                        @Pc(204) int alphaPartial = alphaTop + (bottomLeft >> 24 & 0xFF);
                        @Pc(212) int redPartial = redTop + (bottomLeft >> 16 & 0xFF);
                        @Pc(218) int bluePartial = blueTop + (bottomLeft & 0xFF);
                        @Pc(226) int redSum = redPartial + (bottomRight >> 16 & 0xFF);
                        @Pc(234) int alphaSum = alphaPartial + (bottomRight >> 24 & 0xFF);
                        @Pc(240) int blueSum = bluePartial + (bottomRight & 0xFF);
                        @Pc(248) int greenSum = greenPartial + (bottomRight >> 8 & 0xFF);
                        scratch[destIndex++] = (redSum & 0x3FC) << 14 | alphaSum << 22 & 0xFF000000 | (greenSum & 0x3FC) << 6 | blueSum >> 2 & 0xFF;
                    }
                    topIndex += width;
                    bottomIndex += width;
                }
                scratch = source;
                height = halfHeight;
                source = downsampled;
                width = halfWidth;
                halfWidth >>= 0x1;
                smallestSide >>= 0x1;
                halfHeight >>= 0x1;
                level++;
            }
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!hka", name = "a", descriptor = "(II)V")
    public void trackAllocation() {
        this.toolkit.anInt9145 += this.getSizeInBytes();
    }

    @OriginalMember(owner = "client!hka", name = "a", descriptor = "(III[BI)V")
    protected final void uploadWithMipmaps(@OriginalArg(0) int height, @OriginalArg(1) int target, @OriginalArg(3) byte[] pixels, @OriginalArg(4) int width) {
        if (width > 0 && !Node_Sub1_Sub27.method9150(width)) {
            throw new IllegalArgumentException("");
        } else if (height <= 0 || Node_Sub1_Sub27.method9150(height)) {
            @Pc(40) int components = this.format.anInt2416;
            @Pc(49) int level = 0;
            @Pc(61) int smallestSide = width >= height ? height : width;
            @Pc(65) int halfWidth = width >> 1;
            @Pc(69) int halfHeight = height >> 1;
            @Pc(71) byte[] source = pixels;
            @Pc(78) byte[] scratch = new byte[halfHeight * halfWidth * components];
            while (true) {
                OpenGL.glTexImage2Dub(target, level, this.getInternalFormat(), width, height, NO_BORDER, Static468.toGlPixelFormat(this.format), OpenGL.GL_UNSIGNED_BYTE, source, 0);
                if (smallestSide <= 1) {
                    return;
                }
                @Pc(105) int rowStride = width * components;
                @Pc(107) byte[] downsampled = scratch;
                for (@Pc(109) int component = 0; component < components; component++) {
                    @Pc(115) int destIndex = component;
                    @Pc(117) int topIndex = component;
                    @Pc(121) int bottomIndex = rowStride + component;
                    for (@Pc(123) int y = 0; y < halfHeight; y++) {
                        for (@Pc(127) int x = 0; x < halfWidth; x++) {
                            @Pc(135) byte topLeft = source[topIndex];
                            topIndex += components;
                            @Pc(145) int topSum = topLeft + source[topIndex];
                            @Pc(151) int partialSum = topSum + source[bottomIndex];
                            topIndex += components;
                            bottomIndex += components;
                            @Pc(165) int totalSum = partialSum + source[bottomIndex];
                            scratch[destIndex] = (byte) (totalSum >> 2);
                            bottomIndex += components;
                            destIndex += components;
                        }
                        topIndex += rowStride;
                        bottomIndex += rowStride;
                    }
                }
                scratch = source;
                width = halfWidth;
                height = halfHeight;
                source = downsampled;
                halfHeight >>= 0x1;
                smallestSide >>= 0x1;
                level++;
                halfWidth >>= 0x1;
            }
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!hka", name = "a", descriptor = "([FIIZI)V")
    protected final void uploadWithMipmaps(@OriginalArg(0) float[] pixels, @OriginalArg(1) int width, @OriginalArg(2) int height, @OriginalArg(4) int target) {
        if (width > 0 && !Node_Sub1_Sub27.method9150(width)) {
            throw new IllegalArgumentException("");
        } else if (height <= 0 || Node_Sub1_Sub27.method9150(height)) {
            @Pc(40) int components = this.format.anInt2416;
            @Pc(42) int level = 0;
            @Pc(50) int smallestSide = height <= width ? height : width;
            @Pc(54) int halfWidth = width >> 1;
            @Pc(58) int halfHeight = height >> 1;
            @Pc(60) float[] source = pixels;
            @Pc(72) float[] scratch = new float[halfHeight * halfWidth * components];
            while (true) {
                OpenGL.glTexImage2Df(target, level, this.getInternalFormat(), width, height, NO_BORDER, Static468.toGlPixelFormat(this.format), OpenGL.GL_FLOAT, source, 0);
                if (smallestSide <= 1) {
                    return;
                }
                @Pc(95) int rowStride = width * components;
                for (@Pc(97) int component = 0; component < components; component++) {
                    @Pc(103) int destIndex = component;
                    @Pc(105) int topIndex = component;
                    @Pc(109) int bottomIndex = rowStride + component;
                    for (@Pc(111) int y = 0; y < halfHeight; y++) {
                        for (@Pc(115) int x = 0; x < halfWidth; x++) {
                            @Pc(121) float topLeft = source[topIndex];
                            topIndex += components;
                            @Pc(131) float topSum = topLeft + source[topIndex];
                            @Pc(137) float partialSum = topSum + source[bottomIndex];
                            topIndex += components;
                            bottomIndex += components;
                            @Pc(151) float totalSum = partialSum + source[bottomIndex];
                            bottomIndex += components;
                            scratch[destIndex] = totalSum * 0.25F;
                            destIndex += components;
                        }
                        bottomIndex += rowStride;
                        topIndex += rowStride;
                    }
                }
                @Pc(197) float[] downsampled = scratch;
                scratch = source;
                width = halfWidth;
                source = downsampled;
                height = halfHeight;
                smallestSide >>= 0x1;
                halfWidth >>= 0x1;
                halfHeight >>= 0x1;
                level++;
            }
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!hka", name = "d", descriptor = "(I)V")
    public void delete() {
        if (this.id > 0) {
            this.toolkit.method8160(this.getSizeInBytes(), this.id);
            this.id = 0;
        }
    }
}

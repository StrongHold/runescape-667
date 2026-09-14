import com.jagex.math.IntMath;
import jagdx.IDirect3DBaseTexture;
import jagdx.IDirect3DTexture;
import jagdx.PixelBuffer;
import jagdx.lh;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!waa")
public final class D3DTexture2D extends D3DTexture implements Interface18 {

    @OriginalMember(owner = "client!waa", name = "i", descriptor = "Z")
    public boolean repeatV;

    @OriginalMember(owner = "client!waa", name = "f", descriptor = "Z")
    public boolean repeatU;

    @OriginalMember(owner = "client!waa", name = "j", descriptor = "I")
    public final int width;

    @OriginalMember(owner = "client!waa", name = "h", descriptor = "I")
    public final int height;

    @OriginalMember(owner = "client!waa", name = "g", descriptor = "Lclient!jagdx/IDirect3DTexture;")
    public final IDirect3DTexture texture;

    @OriginalMember(owner = "client!waa", name = "<init>", descriptor = "(Lclient!kea;IIZ[III)V")
    public D3DTexture2D(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) int width, @OriginalArg(2) int height, @OriginalArg(3) boolean mipmapped, @OriginalArg(4) int[] pixels, @OriginalArg(5) int offset, @OriginalArg(6) int stride) {
        super(toolkit, Static172.aClass92_8, Static702.aClass397_16, mipmapped && toolkit.mipmapSupported, height * width);
        if (this.toolkit.nonPowerOfTwoSupported) {
            this.width = width;
            this.height = height;
        } else {
            this.width = IntMath.nextPow2(width);
            this.height = IntMath.nextPow2(height);
        }
        if (mipmapped) {
            this.texture = this.toolkit.anIDirect3DDevice1.a(this.width, this.height, ALL_LEVELS, D3DUSAGE_AUTOGENMIPMAP, D3DFMT_A8R8G8B8, D3DPOOL_MANAGED);
        } else {
            this.texture = this.toolkit.anIDirect3DDevice1.a(this.width, this.height, SINGLE_LEVEL, NO_USAGE, D3DFMT_A8R8G8B8, D3DPOOL_MANAGED);
        }
        @Pc(72) PixelBuffer buffer = this.toolkit.aPixelBuffer1;
        @Pc(83) int result = this.texture.LockRect(BASE_LEVEL, 0, 0, width, height, NO_LOCK_FLAGS, buffer);
        if (lh.a((byte) 84, result)) {
            if (stride == 0) {
                stride = width;
            }
            @Pc(99) int destRowPitch = buffer.getRowPitch();
            if (width * Integer.BYTES == destRowPitch && stride == width) {
                buffer.b(pixels, offset, 0, width * height);
            } else {
                for (@Pc(125) int row = 0; row < height; row++) {
                    buffer.b(pixels, offset + row * stride, row * destRowPitch, width);
                }
            }
            this.texture.UnlockRect(BASE_LEVEL);
        }
    }

    @OriginalMember(owner = "client!waa", name = "<init>", descriptor = "(Lclient!kea;Lclient!eba;Lclient!wda;II)V")
    public D3DTexture2D(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) Class397 type, @OriginalArg(3) int width, @OriginalArg(4) int height) {
        super(toolkit, format, type, false, height * width);
        if (this.toolkit.nonPowerOfTwoSupported) {
            this.height = height;
            this.width = width;
        } else {
            this.width = IntMath.nextPow2(width);
            this.height = IntMath.nextPow2(height);
        }
        this.texture = this.toolkit.anIDirect3DDevice1.a(width, height, SINGLE_LEVEL, NO_USAGE, Static325.method4868(this.type, this.format), D3DPOOL_MANAGED);
    }

    @OriginalMember(owner = "client!waa", name = "<init>", descriptor = "(Lclient!kea;Lclient!eba;IIZ[BII)V")
    public D3DTexture2D(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) boolean mipmapped, @OriginalArg(5) byte[] texels, @OriginalArg(6) int offset, @OriginalArg(7) int stride) {
        super(toolkit, format, Static702.aClass397_16, mipmapped && toolkit.mipmapSupported, height * width);
        if (this.toolkit.nonPowerOfTwoSupported) {
            this.height = height;
            this.width = width;
        } else {
            this.width = IntMath.nextPow2(width);
            this.height = IntMath.nextPow2(height);
        }
        if (mipmapped) {
            this.texture = this.toolkit.anIDirect3DDevice1.a(this.width, this.height, ALL_LEVELS, D3DUSAGE_AUTOGENMIPMAP, Static325.method4868(Static702.aClass397_16, this.format), D3DPOOL_MANAGED);
        } else {
            this.texture = this.toolkit.anIDirect3DDevice1.a(this.width, this.height, SINGLE_LEVEL, NO_USAGE, Static325.method4868(Static702.aClass397_16, this.format), D3DPOOL_MANAGED);
        }
        @Pc(80) PixelBuffer buffer = this.toolkit.aPixelBuffer1;
        @Pc(91) int result = this.texture.LockRect(BASE_LEVEL, 0, 0, width, height, NO_LOCK_FLAGS, buffer);
        if (lh.a((byte) 104, result)) {
            @Pc(108) int rowBytes = width * this.format.anInt2416;
            @Pc(114) int sourceRowPitch = width * this.format.anInt2416;
            @Pc(117) int destRowPitch = buffer.getRowPitch();
            if (rowBytes == destRowPitch && rowBytes == sourceRowPitch) {
                buffer.a(texels, offset, 0, rowBytes * height);
            } else {
                for (@Pc(145) int row = 0; row < height; row++) {
                    buffer.a(texels, sourceRowPitch * row + offset, row * destRowPitch, rowBytes);
                }
            }
            this.texture.UnlockRect(BASE_LEVEL);
        }
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(B)I")
    @Override
    public int method9047() {
        return this.height;
    }

    @OriginalMember(owner = "client!waa", name = "c", descriptor = "(I)Lclient!jagdx/IDirect3DBaseTexture;")
    @Override
    public IDirect3DBaseTexture getTexture() {
        return this.texture;
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(ZZZ)V")
    @Override
    public void method9052(@OriginalArg(0) boolean repeatU, @OriginalArg(1) boolean repeatV) {
        this.repeatV = repeatV;
        this.repeatU = repeatU;
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(IIII[BILclient!eba;II)V")
    @Override
    public void method9051(@OriginalArg(1) int stride, @OriginalArg(2) int height, @OriginalArg(4) byte[] texels, @OriginalArg(6) Class92 format, @OriginalArg(8) int width) {
        if (format != this.format || Static702.aClass397_16 != this.type) {
            throw new RuntimeException();
        }
        @Pc(19) PixelBuffer buffer = this.toolkit.aPixelBuffer1;
        @Pc(30) int result = this.texture.LockRect(BASE_LEVEL, 0, 0, width, height, NO_LOCK_FLAGS, buffer);
        if (!lh.a((byte) 106, result)) {
            return;
        }
        @Pc(40) int sourceRowPitch = stride * this.format.anInt2416;
        @Pc(46) int rowBytes = width * this.format.anInt2416;
        @Pc(49) int destRowPitch = buffer.getRowPitch();
        if (rowBytes == destRowPitch && rowBytes == sourceRowPitch) {
            buffer.a(texels, 0, 0, rowBytes * height);
        } else {
            for (@Pc(63) int row = 0; row < height; row++) {
                buffer.a(texels, row * sourceRowPitch, destRowPitch * row, rowBytes);
            }
        }
        this.texture.UnlockRect(BASE_LEVEL);
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(BLclient!nga;)V")
    @Override
    public void method9041(@OriginalArg(1) Class259 filter) {
        super.method9041(filter);
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(FI)F")
    @Override
    public float method9046(@OriginalArg(0) float x) {
        return x / (float) this.width;
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(Z)V")
    @Override
    public void method9043() {
        this.toolkit.bindTexture2D(this);
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(BII[IIII)V")
    @Override
    public void method9044(@OriginalArg(3) int[] pixels, @OriginalArg(5) int width, @OriginalArg(6) int height) {
        if (Static172.aClass92_8 != this.format || Static702.aClass397_16 != this.type) {
            throw new RuntimeException();
        }
        @Pc(28) PixelBuffer buffer = this.toolkit.aPixelBuffer1;
        @Pc(39) int result = this.texture.LockRect(BASE_LEVEL, 0, 0, width, height, D3DLOCK_READONLY, buffer);
        if (!lh.a((byte) 63, result)) {
            return;
        }
        @Pc(48) int sourceRowPitch = buffer.getRowPitch();
        if (width * Integer.BYTES == sourceRowPitch) {
            buffer.a(pixels, 0, 0, pixels.length);
        } else {
            for (@Pc(64) int row = 0; row < height; row++) {
                buffer.a(pixels, width * row, row * sourceRowPitch, width);
            }
        }
        this.texture.UnlockRect(BASE_LEVEL);
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(I)Z")
    @Override
    public boolean method9049() {
        return true;
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "([IIIIIIII)V")
    @Override
    public void method9048(@OriginalArg(0) int[] pixels, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int height, @OriginalArg(5) int width, @OriginalArg(7) int stride) {
        if (Static172.aClass92_8 != this.format || Static702.aClass397_16 != this.type) {
            throw new RuntimeException();
        }
        @Pc(18) PixelBuffer buffer = this.toolkit.aPixelBuffer1;
        @Pc(29) int result = this.texture.LockRect(BASE_LEVEL, x, y, width, height, NO_LOCK_FLAGS, buffer);
        if (!lh.a((byte) 123, result)) {
            return;
        }
        @Pc(48) int destRowPitch = buffer.getRowPitch();
        if (destRowPitch == width * Integer.BYTES && width == stride) {
            buffer.b(pixels, 0, 0, width * height);
        } else {
            for (@Pc(64) int row = 0; row < height; row++) {
                buffer.b(pixels, stride * row, row * destRowPitch, width);
            }
        }
        this.texture.UnlockRect(BASE_LEVEL);
    }

    @OriginalMember(owner = "client!waa", name = "a", descriptor = "(FB)F")
    @Override
    public float method9050(@OriginalArg(0) float y) {
        return y / (float) this.height;
    }

    @OriginalMember(owner = "client!waa", name = "b", descriptor = "(I)I")
    @Override
    public int method9045() {
        return this.width;
    }
}

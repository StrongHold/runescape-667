import jagdx.IDirect3DBaseTexture;
import jagdx.IDirect3DVolumeTexture;
import jagdx.PixelBuffer;
import jagdx.lh;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!cfa")
public final class D3DTexture3D extends D3DTexture implements Interface2 {

    @OriginalMember(owner = "client!cfa", name = "f", descriptor = "I")
    public final int depth;

    @OriginalMember(owner = "client!cfa", name = "g", descriptor = "I")
    public final int width;

    @OriginalMember(owner = "client!cfa", name = "h", descriptor = "I")
    public final int height;

    @OriginalMember(owner = "client!cfa", name = "i", descriptor = "Lclient!jagdx/IDirect3DVolumeTexture;")
    public final IDirect3DVolumeTexture texture;

    @OriginalMember(owner = "client!cfa", name = "<init>", descriptor = "(Lclient!kea;Lclient!eba;III[B)V")
    public D3DTexture3D(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int depth, @OriginalArg(5) byte[] voxels) {
        super(toolkit, format, Static702.aClass397_16, false, depth * width * height);
        this.depth = depth;
        this.width = width;
        this.height = height;
        this.texture = this.toolkit.anIDirect3DDevice1.a(width, height, depth, SINGLE_LEVEL, NO_USAGE, Static325.method4868(this.type, format), D3DPOOL_MANAGED);
        @Pc(40) PixelBuffer buffer = this.toolkit.aPixelBuffer1;
        @Pc(53) int result = this.texture.LockBox(BASE_LEVEL, 0, 0, 0, width, height, depth, NO_LOCK_FLAGS, buffer);
        if (lh.a((byte) 107, result)) {
            @Pc(66) int rowBytes = this.width * this.format.anInt2416;
            @Pc(71) int sourceSlicePitch = rowBytes * this.height;
            @Pc(74) int destSlicePitch = buffer.getSlicePitch();
            if (sourceSlicePitch == destSlicePitch) {
                buffer.a(voxels, 0, 0, this.height * rowBytes * this.depth);
            } else {
                @Pc(80) int destRowPitch = buffer.getRowPitch();
                @Pc(89) int slice;
                if (rowBytes == destRowPitch) {
                    for (slice = 0; slice < this.depth; slice++) {
                        buffer.a(voxels, sourceSlicePitch * slice, slice * destSlicePitch, sourceSlicePitch);
                    }
                } else {
                    for (slice = 0; slice < this.depth; slice++) {
                        for (@Pc(95) int row = 0; row < this.height; row++) {
                            buffer.a(voxels, sourceSlicePitch * slice + rowBytes * row, row * destRowPitch + destSlicePitch * slice, rowBytes);
                        }
                    }
                }
            }
            this.texture.UnlockBox(BASE_LEVEL);
        }
    }

    @OriginalMember(owner = "client!cfa", name = "a", descriptor = "(BLclient!nga;)V")
    @Override
    public void method9041(@OriginalArg(1) Class259 filter) {
        super.method9041(filter);
    }

    @OriginalMember(owner = "client!cfa", name = "a", descriptor = "(Z)V")
    @Override
    public void method9043() {
        this.toolkit.bindTexture3D(this);
    }

    @OriginalMember(owner = "client!cfa", name = "c", descriptor = "(I)Lclient!jagdx/IDirect3DBaseTexture;")
    @Override
    public IDirect3DBaseTexture getTexture() {
        return this.texture;
    }
}

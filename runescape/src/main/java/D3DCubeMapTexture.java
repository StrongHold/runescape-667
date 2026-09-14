import jagdx.IDirect3DBaseTexture;
import jagdx.IDirect3DCubeTexture;
import jagdx.PixelBuffer;
import jagdx.lh;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kfa")
public final class D3DCubeMapTexture extends D3DTexture implements Interface8 {

    private static final int FACE_COUNT = 6;

    @OriginalMember(owner = "client!kfa", name = "f", descriptor = "I")
    public final int size;

    @OriginalMember(owner = "client!kfa", name = "g", descriptor = "Lclient!jagdx/IDirect3DCubeTexture;")
    public final IDirect3DCubeTexture texture;

    @OriginalMember(owner = "client!kfa", name = "<init>", descriptor = "(Lclient!kea;IZ[[I)V")
    public D3DCubeMapTexture(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) int size, @OriginalArg(2) boolean mipmapped, @OriginalArg(3) int[][] faces) {
        super(toolkit, Static172.aClass92_8, Static702.aClass397_16, mipmapped && toolkit.mipmappedCubeMapSupported, size * FACE_COUNT * size);
        this.size = size;
        if (this.mipmapped) {
            this.texture = this.toolkit.anIDirect3DDevice1.a(this.size, ALL_LEVELS, D3DUSAGE_AUTOGENMIPMAP, D3DFMT_A8R8G8B8, D3DPOOL_MANAGED);
        } else {
            this.texture = this.toolkit.anIDirect3DDevice1.a(this.size, SINGLE_LEVEL, NO_USAGE, D3DFMT_A8R8G8B8, D3DPOOL_MANAGED);
        }
        @Pc(52) PixelBuffer buffer = this.toolkit.aPixelBuffer1;
        for (@Pc(54) int face = 0; face < FACE_COUNT; face++) {
            @Pc(73) int result = this.texture.LockRect(face, BASE_LEVEL, 0, 0, this.size, this.size, NO_LOCK_FLAGS, buffer);
            if (lh.a((byte) 84, result)) {
                @Pc(82) int destRowPitch = buffer.getRowPitch();
                if (destRowPitch == this.size * Integer.BYTES) {
                    buffer.b(faces[face], 0, 0, this.size * this.size);
                } else {
                    for (@Pc(94) int row = 0; row < this.size; row++) {
                        buffer.b(faces[face], this.size * row, destRowPitch * row, this.size);
                    }
                }
                this.texture.UnlockRect(face, BASE_LEVEL);
            }
        }
    }

    @OriginalMember(owner = "client!kfa", name = "a", descriptor = "(Z)V")
    @Override
    public void method9043() {
        this.toolkit.bindTexture(this);
    }

    @OriginalMember(owner = "client!kfa", name = "a", descriptor = "(BLclient!nga;)V")
    @Override
    public void method9041(@OriginalArg(1) Class259 filter) {
        super.method9041(filter);
    }

    @OriginalMember(owner = "client!kfa", name = "c", descriptor = "(I)Lclient!jagdx/IDirect3DBaseTexture;")
    @Override
    public IDirect3DBaseTexture getTexture() {
        return this.texture;
    }
}

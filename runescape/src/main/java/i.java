import com.jagex.game.runetek6.config.billboardtype.BillboardType;
import com.jagex.game.runetek6.config.billboardtype.BillboardTypeList;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.MeshBillboard;
import com.jagex.graphics.Model;
import com.jagex.graphics.PickingCylinder;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.graphics.sw.SoftwareMemoryManager;
import com.jagex.graphics.sw.SoftwareObject;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!i")
public final class i extends Model implements SoftwareObject {

    @OriginalMember(owner = "client!i", name = "nativeid", descriptor = "J")
    public long nativeid;

    @OriginalMember(owner = "client!i", name = "o", descriptor = "Lclient!oa;")
    public final oa anOa2;

    @OriginalMember(owner = "client!i", name = "n", descriptor = "Lclient!ya;")
    public final ya aYa1;

    @OriginalMember(owner = "client!i", name = "q", descriptor = "[Lclient!rv;")
    public ModelParticleEmitter[] aModelParticleEmitterArray3;

    @OriginalMember(owner = "client!i", name = "p", descriptor = "[Lclient!mn;")
    public ModelParticleEffector[] aModelParticleEffectorArray3;

    @OriginalMember(owner = "client!i", name = "<init>", descriptor = "(Lclient!oa;Lclient!ya;Lclient!dv;IIII)V")
    public i(@OriginalArg(0) oa arg0, @OriginalArg(1) ya arg1, @OriginalArg(2) Mesh arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        this.anOa2 = arg0;
        this.aYa1 = arg1;
        this.aModelParticleEmitterArray3 = arg2.emitters;
        this.aModelParticleEffectorArray3 = arg2.effectors;
        @Pc(24) int local24 = arg2.emitters == null ? 0 : arg2.emitters.length;
        @Pc(34) int local34 = arg2.effectors == null ? 0 : arg2.effectors.length;
        @Pc(36) int local36 = 0;
        @Pc(43) int[] local43 = new int[local24 * 3 + local34];
        for (@Pc(45) int local45 = 0; local45 < local24; local45++) {
            local43[local36++] = this.aModelParticleEmitterArray3[local45].vertexA;
            local43[local36++] = this.aModelParticleEmitterArray3[local45].vertexB;
            local43[local36++] = this.aModelParticleEmitterArray3[local45].vertexC;
        }
        for (@Pc(79) int local79 = 0; local79 < local34; local79++) {
            local43[local36++] = this.aModelParticleEffectorArray3[local79].vertex;
        }
        @Pc(105) int local105 = arg2.billboards == null ? 0 : arg2.billboards.length;
        @Pc(110) int[] local110 = new int[local105 * 8];
        @Pc(112) int local112 = 0;
        for (@Pc(114) int local114 = 0; local114 < local105; local114++) {
            @Pc(120) MeshBillboard local120 = arg2.billboards[local114];
            @Pc(125) BillboardType local125 = BillboardTypeList.list(local120.id);
            local110[local112++] = local120.face;
            local110[local112++] = local125.width;
            local110[local112++] = local125.height;
            local110[local112++] = local125.texture;
            local110[local112++] = local125.colourOp;
            local110[local112++] = local125.blendMode;
            local110[local112++] = local125.hideFace ? -1 : 0;
        }
        for (@Pc(178) int local178 = 0; local178 < local105; local178++) {
            @Pc(184) MeshBillboard local184 = arg2.billboards[local178];
            local110[local112++] = local184.group;
        }
        this.R(this.anOa2, this.aYa1, arg2.vertexCount, arg2.maxVertex, arg2.vertexX, arg2.vertexY, arg2.vertexZ, arg2.vertexLabel, arg2.originModels, arg2.faceCount, arg2.faceA, arg2.faceB, arg2.faceC, arg2.shadingType, arg2.facePriority, arg2.faceAlpha, arg2.faceTexSpace, arg2.faceColour, arg2.faceTexture, arg2.faceLabel, arg2.globalPriority, arg2.faceOriginModels, arg2.texSpaceCount, arg2.texMappingType, arg2.texSpaceDefA, arg2.texSpaceDefB, arg2.texSpaceDefC, arg2.texSpaceScaleX, arg2.texSpaceScaleY, arg2.texSpaceScaleZ, arg2.texRotation, arg2.texDirection, arg2.texOffsetX, arg2.texOffsetY, arg2.texOffsetZ, local43, local24, local34, arg3, arg4, arg5, arg6, local110);
    }

    @OriginalMember(owner = "client!i", name = "<init>", descriptor = "(Lclient!oa;)V")
    public i(@OriginalArg(0) oa arg0) {
        this.anOa2 = arg0;
        this.aYa1 = null;
        this.oa(arg0);
    }

    @OriginalMember(owner = "client!i", name = "p", descriptor = "(IILclient!s;Lclient!s;III)V")
    public native void p(@OriginalArg(0) int hillType, @OriginalArg(1) int hillValue, @OriginalArg(2) Ground floor, @OriginalArg(3) Ground ceiling, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z);

    @OriginalMember(owner = "client!i", name = "LA", descriptor = "(I)V")
    public native void LA(@OriginalArg(0) int contrast);

    @OriginalMember(owner = "client!i", name = "ia", descriptor = "(SS)V")
    public native void ia(@OriginalArg(0) short src, @OriginalArg(1) short dest);

    @OriginalMember(owner = "client!i", name = "ua", descriptor = "()I")
    public native int ua();

    @OriginalMember(owner = "client!i", name = "F", descriptor = "()Z")
    public native boolean F();

    @OriginalMember(owner = "client!i", name = "fa", descriptor = "()I")
    public native int fa();

    @OriginalMember(owner = "client!i", name = "g", descriptor = "()V")
    @Override
    protected void lock() {
        if (this.anOa2.anInt6770 <= 1) {
            return;
        }
        synchronized (this) {
            while (super.locked) {
                try {
                    this.wait();
                } catch (@Pc(13) InterruptedException local13) {
                }
            }
            super.locked = true;
        }
    }

    @OriginalMember(owner = "client!i", name = "C", descriptor = "(I)V")
    public native void C(@OriginalArg(0) int ambient);

    @OriginalMember(owner = "client!i", name = "k", descriptor = "(I)V")
    public native void k(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(IILclient!tt;ZII)Z")
    @Override
    public boolean pickedOrtho(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift, @OriginalArg(5) int angle) {
        return this.anOa2.threadResource().method5(this, x, y, matrix, quick, angle);
    }

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(B[B)V")
    @Override
    public void updateAlphas(@OriginalArg(0) byte arg0, @OriginalArg(1) byte[] arg1) {
        throw new RuntimeException();
    }

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(IIII)V")
    @Override
    public void adjustColours(@OriginalArg(0) int hue, @OriginalArg(1) int saturation, @OriginalArg(2) int lightness, @OriginalArg(3) int scale) {
    }

    @OriginalMember(owner = "client!i", name = "I", descriptor = "(I[IIIIZI[I)V")
    protected native void I(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int[] arg7);

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(I[IIIIIZ)V")
    @Override
    protected void applyTransformUnmasked(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) boolean arg6) {
        this.l(this.nativeid, arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(Lclient!tt;IZ)V")
    @Override
    public void transform(@OriginalArg(0) Matrix matrix, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2) {
        this.J(((ja) matrix).nativeid, arg1, arg2);
    }

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(IILclient!tt;ZI)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) Matrix matrix, @OriginalArg(3) boolean quick, @OriginalArg(4) int sizeShift) {
        return this.anOa2.threadResource().method11(this, x, y, matrix, quick);
    }

    @OriginalMember(owner = "client!i", name = "RA", descriptor = "()I")
    public native int RA();

    @OriginalMember(owner = "client!i", name = "da", descriptor = "()I")
    public native int da();

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(I)V")
    public native void a(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!i", name = "V", descriptor = "()I")
    public native int V();

    @OriginalMember(owner = "client!i", name = "J", descriptor = "(JIZ)V")
    public native void J(@OriginalArg(0) long arg0, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2);

    @OriginalMember(owner = "client!i", name = "na", descriptor = "()I")
    public native int na();

    @OriginalMember(owner = "client!i", name = "P", descriptor = "(IIII)V")
    protected native void P(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void apply(@OriginalArg(0) Matrix arg0) {
        this.transformPoints(Static445.anIntArray537, arg0);
        @Pc(5) int local5 = 0;
        @Pc(10) int local10;
        if (this.aModelParticleEmitterArray3 != null) {
            for (local10 = 0; local10 < this.aModelParticleEmitterArray3.length; local10++) {
                @Pc(16) ModelParticleEmitter local16 = this.aModelParticleEmitterArray3[local10];
                local16.transformedAX = Static445.anIntArray537[local5++];
                local16.transformedAY = Static445.anIntArray537[local5++];
                local16.transformedAZ = Static445.anIntArray537[local5++];
                local16.transformedBX = Static445.anIntArray537[local5++];
                local16.transformedBY = Static445.anIntArray537[local5++];
                local16.transformedBZ = Static445.anIntArray537[local5++];
                local16.transformedCX = Static445.anIntArray537[local5++];
                local16.transformedCY = Static445.anIntArray537[local5++];
                local16.transformedCZ = Static445.anIntArray537[local5++];
            }
        }
        if (this.aModelParticleEffectorArray3 == null) {
            return;
        }
        for (local10 = 0; local10 < this.aModelParticleEffectorArray3.length; local10++) {
            @Pc(89) ModelParticleEffector local89 = this.aModelParticleEffectorArray3[local10];
            @Pc(91) ModelParticleEffector local91 = local89;
            if (local89.next != null) {
                local91 = local89.next;
            }
            if (local89.matrix == null) {
                local89.matrix = arg0.copy();
            } else {
                local89.matrix.apply(arg0);
            }
            local91.x = Static445.anIntArray537[local5++];
            local91.y = Static445.anIntArray537[local5++];
            local91.z = Static445.anIntArray537[local5++];
        }
    }

    @OriginalMember(owner = "client!i", name = "R", descriptor = "(Lclient!oa;Lclient!ya;II[I[I[I[I[SI[S[S[S[B[B[B[B[S[S[IB[SI[B[S[S[S[I[I[I[B[B[I[I[I[IIIIIII[I)V")
    public native void R(@OriginalArg(0) oa arg0, @OriginalArg(1) ya arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int[] arg4, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int[] arg7, @OriginalArg(8) short[] arg8, @OriginalArg(9) int arg9, @OriginalArg(10) short[] arg10, @OriginalArg(11) short[] arg11, @OriginalArg(12) short[] arg12, @OriginalArg(13) byte[] arg13, @OriginalArg(14) byte[] arg14, @OriginalArg(15) byte[] arg15, @OriginalArg(16) byte[] arg16, @OriginalArg(17) short[] arg17, @OriginalArg(18) short[] arg18, @OriginalArg(19) int[] arg19, @OriginalArg(20) byte arg20, @OriginalArg(21) short[] arg21, @OriginalArg(22) int arg22, @OriginalArg(23) byte[] arg23, @OriginalArg(24) short[] arg24, @OriginalArg(25) short[] arg25, @OriginalArg(26) short[] arg26, @OriginalArg(27) int[] arg27, @OriginalArg(28) int[] arg28, @OriginalArg(29) int[] arg29, @OriginalArg(30) byte[] arg30, @OriginalArg(31) byte[] arg31, @OriginalArg(32) int[] arg32, @OriginalArg(33) int[] arg33, @OriginalArg(34) int[] arg34, @OriginalArg(35) int[] arg35, @OriginalArg(36) int arg36, @OriginalArg(37) int arg37, @OriginalArg(38) int arg38, @OriginalArg(39) int arg39, @OriginalArg(40) int arg40, @OriginalArg(41) int arg41, @OriginalArg(42) int[] arg42);

    @OriginalMember(owner = "client!i", name = "w", descriptor = "(Z)V")
    public native void w(@OriginalArg(0) boolean arg0);

    @OriginalMember(owner = "client!i", name = "c", descriptor = "()[Lclient!mn;")
    @Override
    public ModelParticleEffector[] particleEffectors() {
        return this.aModelParticleEffectorArray3;
    }

    @OriginalMember(owner = "client!i", name = "G", descriptor = "()I")
    public native int G();

    @OriginalMember(owner = "client!i", name = "H", descriptor = "(III)V")
    public native void H(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z);

    @OriginalMember(owner = "client!i", name = "l", descriptor = "(JI[IIIIIZ)V")
    public native void l(@OriginalArg(0) long arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[] arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) boolean arg7);

    @OriginalMember(owner = "client!i", name = "b", descriptor = "()[B")
    @Override
    public byte[] getFaceAlphas() {
        throw new RuntimeException();
    }

    @OriginalMember(owner = "client!i", name = "aa", descriptor = "(SS)V")
    public native void aa(@OriginalArg(0) short src, @OriginalArg(1) short dest);

    @OriginalMember(owner = "client!i", name = "wa", descriptor = "()V")
    protected native void wa();

    @OriginalMember(owner = "client!i", name = "e", descriptor = "()V")
    @Override
    public void method7479() {
    }

    @OriginalMember(owner = "client!i", name = "HA", descriptor = "()I")
    public native int HA();

    @OriginalMember(owner = "client!i", name = "ba", descriptor = "(Lclient!r;)Lclient!r;")
    public native Shadow ba(@OriginalArg(0) Shadow shadow);

    @OriginalMember(owner = "client!i", name = "FA", descriptor = "(I)V")
    public native void FA(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(Lclient!tt;Lclient!ima;II)V")
    @Override
    public void renderOrtho(@OriginalArg(0) Matrix arg0, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        if (cylinder == null) {
            this.anOa2.threadResource().method9(this, arg0, null, arg2, arg3);
            return;
        }
        Static445.anIntArray542[5] = 0;
        this.anOa2.threadResource().method9(this, arg0, Static445.anIntArray542, arg2, arg3);
        cylinder.topX = Static445.anIntArray542[0];
        cylinder.topY = Static445.anIntArray542[1];
        cylinder.bottomX = Static445.anIntArray542[2];
        cylinder.bottomY = Static445.anIntArray542[3];
        cylinder.radiusPixels = Static445.anIntArray542[4];
        cylinder.visible = Static445.anIntArray542[5] != 0;
    }

    @OriginalMember(owner = "client!i", name = "r", descriptor = "()Z")
    public native boolean r();

    @OriginalMember(owner = "client!i", name = "a", descriptor = "([ILclient!tt;)V")
    public void transformPoints(@OriginalArg(0) int[] arg0, @OriginalArg(1) Matrix arg1) {
        this.anOa2.threadResource().method15(this, arg0, arg1);
    }

    @OriginalMember(owner = "client!i", name = "d", descriptor = "()V")
    @Override
    protected void unlock() {
        if (this.anOa2.anInt6770 > 1) {
            synchronized (this) {
                super.locked = false;
                this.notifyAll();
            }
        }
    }

    @OriginalMember(owner = "client!i", name = "ZA", descriptor = "(Lclient!i;Lclient!i;IZZ)V")
    public native void ZA(@OriginalArg(0) i arg0, @OriginalArg(1) i arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean arg3, @OriginalArg(4) boolean arg4);

    @OriginalMember(owner = "client!i", name = "NA", descriptor = "()Z")
    protected native boolean NA();

    @OriginalMember(owner = "client!i", name = "WA", descriptor = "()I")
    public native int WA();

    @OriginalMember(owner = "client!i", name = "s", descriptor = "(I)V")
    public native void s(@OriginalArg(0) int functionMask);

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(BIZ)Lclient!ka;")
    @Override
    public Model copy(@OriginalArg(0) byte arg0, @OriginalArg(1) int functionMask, @OriginalArg(2) boolean arg2) {
        return this.anOa2.threadResource().method3(this, arg0, functionMask, arg2);
    }

    @OriginalMember(owner = "client!i", name = "finalize", descriptor = "()V")
    @Override
    public void finalize() {
        if (this.nativeid != 0L) {
            SoftwareMemoryManager.free(this);
        }
    }

    @OriginalMember(owner = "client!i", name = "EA", descriptor = "()I")
    public native int EA();

    @OriginalMember(owner = "client!i", name = "a", descriptor = "()Z")
    @Override
    public boolean loadedTextures() {
        return true;
    }

    @OriginalMember(owner = "client!i", name = "O", descriptor = "(III)V")
    public native void O(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2);

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(Lclient!tt;Lclient!ima;I)V")
    @Override
    public void render(@OriginalArg(0) Matrix matrix, @OriginalArg(1) PickingCylinder cylinder, @OriginalArg(2) int flags) {
        if (cylinder == null) {
            this.anOa2.threadResource().method8(this, matrix, null, flags);
            return;
        }
        Static445.anIntArray542[5] = 0;
        this.anOa2.threadResource().method8(this, matrix, Static445.anIntArray542, flags);
        cylinder.topX = Static445.anIntArray542[0];
        cylinder.topY = Static445.anIntArray542[1];
        cylinder.bottomX = Static445.anIntArray542[2];
        cylinder.bottomY = Static445.anIntArray542[3];
        cylinder.radiusPixels = Static445.anIntArray542[4];
        cylinder.visible = Static445.anIntArray542[5] != 0;
    }

    @OriginalMember(owner = "client!i", name = "ma", descriptor = "()I")
    public native int ma();

    @OriginalMember(owner = "client!i", name = "oa", descriptor = "(Lclient!oa;)V")
    public native void oa(@OriginalArg(0) oa arg0);

    @OriginalMember(owner = "client!i", name = "v", descriptor = "()V")
    public native void v();

    @OriginalMember(owner = "client!i", name = "VA", descriptor = "(I)V")
    public native void VA(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!i", name = "a", descriptor = "(Lclient!ka;IIIZ)V")
    @Override
    public void shareLight(@OriginalArg(0) Model arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        this.anOa2.threadResource().method10(this, arg0, arg1, arg2, arg3, arg4);
    }

    @OriginalMember(owner = "client!i", name = "f", descriptor = "()[Lclient!rv;")
    @Override
    public ModelParticleEmitter[] particleEmitters() {
        return this.aModelParticleEmitterArray3;
    }
}

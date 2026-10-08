import com.jagex.Static14;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!wo")
public final class GlFramebuffer implements Interface14 {

    @OriginalMember(owner = "client!wo", name = "l", descriptor = "I")
    public int anInt10829;

    @OriginalMember(owner = "client!wo", name = "w", descriptor = "I")
    public int anInt10842;

    @OriginalMember(owner = "client!wo", name = "m", descriptor = "I")
    public int anInt10844;

    @OriginalMember(owner = "client!wo", name = "v", descriptor = "I")
    public int anInt10825 = -1;

    @OriginalMember(owner = "client!wo", name = "b", descriptor = "I")
    public int anInt10830 = 0;

    @OriginalMember(owner = "client!wo", name = "p", descriptor = "[Lclient!bi;")
    public final Interface3[] anInterface3Array1 = new Interface3[9];

    @OriginalMember(owner = "client!wo", name = "t", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_41;

    @OriginalMember(owner = "client!wo", name = "k", descriptor = "I")
    public final int anInt10832;

    @OriginalMember(owner = "client!wo", name = "<init>", descriptor = "(Lclient!qha;)V")
    public GlFramebuffer(@OriginalArg(0) GlToolkit arg0) {
        if (!arg0.aBoolean613) {
            throw new IllegalStateException("");
        }
        this.aClass19_Sub3_41 = arg0;
        OpenGL.glGenFramebuffersEXT(1, Static604.anIntArray708, 0);
        this.anInt10832 = Static604.anIntArray708[0];
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(II)V")
    public void method9363(@OriginalArg(0) int arg0) {
        if (this.anInterface3Array1[arg0] != null) {
            this.anInterface3Array1[arg0].method9435();
        }
        this.anInt10844 &= ~(0x1 << arg0);
        this.anInterface3Array1[arg0] = null;
    }

    @OriginalMember(owner = "client!wo", name = "b", descriptor = "(I)V")
    @Override
    public void method9362() {
        OpenGL.glBindFramebufferEXT(OpenGL.GL_DRAW_FRAMEBUFFER, this.anInt10832);
        this.anInt10830 |= 0x2;
        this.anInt10825 = this.method9365();
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(ILclient!ec;IIZ)V")
    public void method9364(@OriginalArg(0) int arg0, @OriginalArg(1) Class93_Sub1 arg1) {
        if (this.anInt10825 == -1) {
            throw new RuntimeException();
        }
        if ((this.anInt10844 & 0xFFFFFFFE) == 0) {
            this.anInt10829 = arg1.anInt2456;
            this.anInt10842 = arg1.anInt2456;
        } else if (this.anInt10842 != arg1.anInt2456 || this.anInt10829 != arg1.anInt2456) {
            throw new RuntimeException();
        }
        arg1.method2311(arg0, Static14.anIntArray25[0], this.anInt10825);
        this.anInterface3Array1[0] = arg1;
        this.anInt10844 |= 0x1;
    }

    @OriginalMember(owner = "client!wo", name = "c", descriptor = "(I)V")
    @Override
    public void method9358() {
        OpenGL.glBindFramebufferEXT(OpenGL.GL_FRAMEBUFFER, 0);
        this.anInt10830 &= 0xFFFFFFFB;
        this.anInt10825 = this.method9365();
    }

    @OriginalMember(owner = "client!wo", name = "e", descriptor = "(I)I")
    public int method9365() {
        if ((this.anInt10830 & 0x4) != 0) {
            return 36160;
        } else if ((this.anInt10830 & 0x2) == 0) {
            return (this.anInt10830 & 0x1) == 0 ? -1 : 36008;
        } else {
            return 36009;
        }
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(Lclient!ut;II)V")
    public void method9366(@OriginalArg(0) DoublyLinkedNode_Sub2_Sub19 arg0, @OriginalArg(1) int arg1) {
        if (this.anInt10825 == -1) {
            throw new RuntimeException();
        }
        @Pc(17) int local17 = 0x1 << arg1;
        if ((this.anInt10844 & ~local17) == 0) {
            this.anInt10829 = arg0.anInt9828;
            this.anInt10842 = arg0.anInt9818;
        } else if (this.anInt10842 != arg0.anInt9818 || arg0.anInt9828 != this.anInt10829) {
            throw new RuntimeException();
        }
        arg0.method8607(Static14.anIntArray25[arg1], this.anInt10825);
        this.anInterface3Array1[arg1] = arg0;
        this.anInt10844 |= local17;
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(B)V")
    @Override
    public void method9357() {
        OpenGL.glBindFramebufferEXT(OpenGL.GL_DRAW_FRAMEBUFFER, 0);
        this.anInt10830 &= 0xFFFFFFFD;
        this.anInt10825 = this.method9365();
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(I)V")
    @Override
    public void method9361() {
        OpenGL.glBindFramebufferEXT(OpenGL.GL_READ_FRAMEBUFFER, 0);
        this.anInt10830 &= 0xFFFFFFFE;
        this.anInt10825 = this.method9365();
    }

    @OriginalMember(owner = "client!wo", name = "f", descriptor = "(I)Z")
    public boolean method9368() {
        @Pc(14) int local14 = OpenGL.glCheckFramebufferStatusEXT(this.anInt10825);
        return local14 == OpenGL.GL_FRAMEBUFFER_COMPLETE;
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(ILclient!rq;I)V")
    public void method9369(@OriginalArg(1) Class93_Sub2 arg0, @OriginalArg(2) int arg1) {
        this.method9372(arg1, arg0);
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(ILclient!ec;II)V")
    public void method9370(@OriginalArg(1) Class93_Sub1 arg0, @OriginalArg(2) int arg1) {
        this.method9364(arg1, arg0);
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(IB)V")
    public void method9371(@OriginalArg(0) int arg0) {
        if (this.anInt10825 == -1) {
            throw new RuntimeException();
        }
        OpenGL.glDrawBuffer(Static14.anIntArray25[arg0]);
    }

    @OriginalMember(owner = "client!wo", name = "finalize", descriptor = "()V")
    @Override
    public void finalize() throws Throwable {
        this.aClass19_Sub3_41.method6968(this.anInt10832);
        super.finalize();
    }

    @OriginalMember(owner = "client!wo", name = "a", descriptor = "(IIILclient!rq;)V")
    public void method9372(@OriginalArg(0) int arg0, @OriginalArg(3) Class93_Sub2 arg1) {
        if (this.anInt10825 == -1) {
            throw new RuntimeException();
        }
        @Pc(16) int local16 = 0x1 << arg0;
        if ((~local16 & this.anInt10844) == 0) {
            this.anInt10842 = arg1.anInt3248;
            this.anInt10829 = arg1.anInt3255;
        } else if (this.anInt10842 != arg1.anInt3248 || this.anInt10829 != arg1.anInt3255) {
            throw new RuntimeException();
        }
        arg1.method2944(this.anInt10825, Static14.anIntArray25[arg0]);
        this.anInterface3Array1[arg0] = arg1;
        this.anInt10844 |= local16;
    }

    @OriginalMember(owner = "client!wo", name = "b", descriptor = "(IB)V")
    public void method9374() {
        if (this.anInt10825 == -1) {
            throw new RuntimeException();
        }
        OpenGL.glReadBuffer(Static14.anIntArray25[0]);
    }

    @OriginalMember(owner = "client!wo", name = "d", descriptor = "(I)V")
    @Override
    public void method9359() {
        OpenGL.glBindFramebufferEXT(OpenGL.GL_FRAMEBUFFER, this.anInt10832);
        this.anInt10830 |= 0x4;
        this.anInt10825 = this.method9365();
    }

    @OriginalMember(owner = "client!wo", name = "b", descriptor = "(B)V")
    @Override
    public void method9360() {
        OpenGL.glBindFramebufferEXT(OpenGL.GL_READ_FRAMEBUFFER, this.anInt10832);
        this.anInt10830 |= 0x1;
        this.anInt10825 = this.method9365();
    }
}

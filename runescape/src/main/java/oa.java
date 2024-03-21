import com.jagex.ParticleList;
import com.jagex.IndexedImage;
import com.jagex.Class67;
import com.jagex.Class84;
import com.jagex.Interface26;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.HashTable;
import com.jagex.game.Class14;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Exception_Sub1;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Interface9;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Node_Sub13;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Surface;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.reflect.Method;

@OriginalClass("client!oa")
public final class oa extends Toolkit implements Interface5 {

    @OriginalMember(owner = "client!oa", name = "O", descriptor = "Lclient!ya;")
    public ya aYa2;

    @OriginalMember(owner = "client!oa", name = "B", descriptor = "Lclient!tt;")
    public Matrix aMatrix_9;

    @OriginalMember(owner = "client!oa", name = "w", descriptor = "Lclient!p;")
    public p aP1;

    @OriginalMember(owner = "client!oa", name = "M", descriptor = "[Lclient!a;")
    public a[] anAArray1;

    @OriginalMember(owner = "client!oa", name = "A", descriptor = "I")
    public int anInt6770;

    @OriginalMember(owner = "client!oa", name = "nativeid", descriptor = "J")
    public final long nativeid = 0L;

    @OriginalMember(owner = "client!oa", name = "F", descriptor = "Z")
    public boolean aBoolean509 = false;

    @OriginalMember(owner = "client!oa", name = "E", descriptor = "Lclient!sia;")
    public final Deque aDeque_38 = new Deque();

    @OriginalMember(owner = "client!oa", name = "I", descriptor = "I")
    public int anInt6768 = 4096;

    @OriginalMember(owner = "client!oa", name = "N", descriptor = "I")
    public int anInt6769 = 4096;

    @OriginalMember(owner = "client!oa", name = "x", descriptor = "Lclient!av;")
    public final HashTable aHashTable_33 = new HashTable(4);

    @OriginalMember(owner = "client!oa", name = "G", descriptor = "Z")
    public boolean aBoolean510 = false;

    @OriginalMember(owner = "client!oa", name = "D", descriptor = "Lclient!tt;")
    public Matrix aMatrix_8;

    @OriginalMember(owner = "client!oa", name = "<init>", descriptor = "(Ljava/awt/Canvas;Lclient!d;II)V")
    public oa(@OriginalArg(0) Canvas arg0, @OriginalArg(1) TextureSource arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        super(arg1);
        try {
            if (!Static14.method179("sw3d")) {
                throw new RuntimeException("");
            }
            Static307.method4479();
            this.MA(super.textureSource, 0, 0);
            Static198.method2954(true, false);
            this.aBoolean510 = true;
            this.aMatrix_8 = new ja();
            this.setCamera(new ja());
            this.method7956(1);
            this.method8020(0);
            if (arg0 != null) {
                this.method8022(arg0, arg2, arg3);
                this.method8019(arg0);
            }
        } catch (@Pc(82) Throwable local82) {
            this.free();
            throw new RuntimeException();
        }
    }

    @OriginalMember(owner = "client!oa", name = "d", descriptor = "(I)V")
    public native void d(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!oa", name = "Y", descriptor = "()[I")
    public native int[] Y();

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!pu;)V")
    @Override
    public void method7973(@OriginalArg(0) Class67 arg0) {
    }

    @OriginalMember(owner = "client!oa", name = "f", descriptor = "(II)V")
    public native void f(@OriginalArg(0) int near, @OriginalArg(1) int far);

    @OriginalMember(owner = "client!oa", name = "ra", descriptor = "(IIII)V")
    public native void ra(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!oa", name = "e", descriptor = "(I)V")
    @Override
    public void method7977(@OriginalArg(0) int arg0) {
        Static307.method4478();
        this.d(arg0);
        for (@Pc(10) ya local10 = (ya) this.aDeque_38.first(); local10 != null; local10 = (ya) this.aDeque_38.next()) {
            local10.r();
        }
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "([IIIIIZ)Lclient!st;")
    @Override
    public Sprite method7958(@OriginalArg(0) int[] arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) boolean arg4) {
        return new j(this, arg0, 0, arg1, arg2, arg3, false);
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "()Z")
    @Override
    public boolean method7992() {
        return false;
    }

    @OriginalMember(owner = "client!oa", name = "j", descriptor = "(I)V")
    @Override
    public void method7956(@OriginalArg(0) int arg0) {
        this.anInt6770 = arg0;
        this.anAArray1 = new a[this.anInt6770];
        for (@Pc(9) int local9 = 0; local9 < this.anInt6770; local9++) {
            this.anAArray1[local9] = new a(this, this.anInt6768, this.anInt6769);
        }
    }

    @OriginalMember(owner = "client!oa", name = "e", descriptor = "()I")
    @Override
    public int getMaxLights() {
        return 4;
    }

    @OriginalMember(owner = "client!oa", name = "MA", descriptor = "(Lclient!d;II)V")
    public native void MA(@OriginalArg(0) TextureSource arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!lk;Z)V")
    public void method6085(@OriginalArg(0) ParticleList arg0) {
        @Pc(1) int local1 = 0;
        @Pc(3) int local3 = 0;
        @Pc(5) int local5 = 0;
        @Pc(7) int local7 = 0;
        for (@Pc(15) Particle local15 = (Particle) arg0.particles.first(); local15 != null; local15 = (Particle) arg0.particles.next()) {
            Static445.anIntArray539[local1++] = local15.anInt7537;
            Static445.anIntArray539[local1++] = local15.anInt7534;
            Static445.anIntArray539[local1++] = local15.anInt7536;
            Static445.anIntArray541[local3++] = local15.anInt7539;
            Static445.aShortArray103[local7++] = (short) local15.anInt7540;
            Static445.anIntArray538[local5++] = local15.anInt7535;
        }
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(II)Lclient!eca;")
    @Override
    public Surface method7962(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return this.method7963(arg0, arg1, false);
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public void method8019(@OriginalArg(0) Canvas arg0) {
        if (arg0 == null) {
            this.aP1 = null;
            this.t((p) null);
        } else {
            @Pc(10) p local10 = (p) this.aHashTable_33.get((long) arg0.hashCode());
            this.aP1 = local10;
            this.t(local10);
        }
    }

    @OriginalMember(owner = "client!oa", name = "E", descriptor = "()I")
    public native int E();

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public void method7935(@OriginalArg(0) Canvas arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(8) p local8 = (p) this.aHashTable_33.get((long) arg0.hashCode());
        local8.method6439(arg0, arg1, arg2);
        if (arg0 != null && arg0 == this.aP1.aCanvas9) {
            this.method8019(arg0);
        }
    }

    @OriginalMember(owner = "client!oa", name = "c", descriptor = "(II)I")
    @Override
    public int combineFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        return maskA | maskB;
    }

    @OriginalMember(owner = "client!oa", name = "FA", descriptor = "()V")
    public native void FA();

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIIIIII)V")
    @Override
    public void method7995(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7) {
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!ve;[Lclient!wp;Z)Lclient!da;")
    @Override
    public Class14 method8010(@OriginalArg(0) FontMetrics arg0, @OriginalArg(1) IndexedImage[] arg1, @OriginalArg(2) boolean arg2) {
        @Pc(3) int[] local3 = new int[arg1.length];
        @Pc(7) int[] local7 = new int[arg1.length];
        @Pc(9) boolean local9 = false;
        for (@Pc(11) int local11 = 0; local11 < arg1.length; local11++) {
            local3[local11] = arg1[local11].width;
            local7[local11] = arg1[local11].height;
            if (arg1[local11].alpha != null) {
                local9 = true;
            }
        }
        if (arg2) {
            if (local9) {
                throw new IllegalArgumentException("Cannot specify alpha with non-mono font unless someone writes it");
            }
            return new h(this, this.aYa2, arg0, arg1, (Sprite[]) null);
        } else if (local9) {
            throw new IllegalArgumentException("Cannot specify alpha with non-mono font unless someone writes it");
        } else {
            return new n(this, this.aYa2, arg0, arg1, (Sprite[]) null);
        }
    }

    @OriginalMember(owner = "client!oa", name = "c", descriptor = "(I)V")
    @Override
    public void method8016(@OriginalArg(0) int arg0) {
        this.anAArray1[arg0].method14();
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!pu;Lclient!pu;FLclient!pu;)Lclient!pu;")
    @Override
    public Class67 method8007(@OriginalArg(0) Class67 arg0, @OriginalArg(1) Class67 arg1, @OriginalArg(2) float arg2, @OriginalArg(3) Class67 arg3) {
        return null;
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIIII)V")
    @Override
    public void method7947(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.method6087().method16(this, arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!oa", name = "k", descriptor = "()Z")
    @Override
    public boolean method7949() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!za;)V")
    @Override
    public void method7938(@OriginalArg(0) Node_Sub13 arg0) {
        this.aYa2 = (ya) arg0;
        this.va(arg0);
    }

    @OriginalMember(owner = "client!oa", name = "w", descriptor = "()Z")
    @Override
    public boolean method8014() {
        return false;
    }

    @OriginalMember(owner = "client!oa", name = "v", descriptor = "()V")
    @Override
    public void method7943() {
        this.method8019(this.aP1.aCanvas9);
    }

    @OriginalMember(owner = "client!oa", name = "M", descriptor = "()I")
    public native int M();

    @OriginalMember(owner = "client!oa", name = "H", descriptor = "(III[I)V")
    public native void H(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3);

    @OriginalMember(owner = "client!oa", name = "j", descriptor = "()V")
    @Override
    public void method7950() {
    }

    @OriginalMember(owner = "client!oa", name = "m", descriptor = "()Z")
    @Override
    public boolean method7970() {
        return false;
    }

    @OriginalMember(owner = "client!oa", name = "r", descriptor = "(IIIIIII)I")
    public native int r(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6);

    @OriginalMember(owner = "client!oa", name = "b", descriptor = "(Z)V")
    @Override
    public void method8018(@OriginalArg(0) boolean arg0) {
    }

    @OriginalMember(owner = "client!oa", name = "b", descriptor = "(I)V")
    @Override
    public void method8003() {
        this.anInt6768 = this.anInt6769 = 10000;
        if (this.anInt6770 > 1) {
            throw new IllegalStateException("No MT");
        }
        this.method7956(this.anInt6770);
        this.method8020(0);
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIIF)Lclient!lca;")
    @Override
    public PointLight method7941(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) float arg5) {
        return new Node_Sub7_Sub3(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!oa", name = "KA", descriptor = "(IIII)V")
    public native void KA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!oa", name = "c", descriptor = "(S)Z")
    public boolean c(@OriginalArg(0) short arg0) {
        synchronized (this) {
            @Pc(9) TextureMetrics local9 = super.textureSource.getMetrics(arg0);
            if (local9 == null) {
                return false;
            } else {
                this.AA(arg0, local9.aShort37, local9.alphaBlendMode, local9.effectType, local9.effectParam1, local9.effectParam2, local9.small, local9.alpha, local9.aByte57, local9.speedU, local9.speedV, local9.aBoolean240, local9.aBoolean234, local9.aBoolean239, local9.aBoolean236, local9.aBoolean235, local9.aByte53, local9.aBoolean237, local9.aBoolean238, local9.colorOp);
                return true;
            }
        }
    }

    @OriginalMember(owner = "client!oa", name = "F", descriptor = "(II)V")
    public native void F(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1);

    @OriginalMember(owner = "client!oa", name = "b", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public void method7972(@OriginalArg(0) Canvas arg0) {
        if (this.aP1.aCanvas9 == arg0) {
            this.method8019((Canvas) null);
        }
        @Pc(18) p local18 = (p) this.aHashTable_33.get((long) arg0.hashCode());
        if (local18 != null) {
            local18.unlink();
            local18.method6442();
        }
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIIILclient!aa;II)V")
    @Override
    public void method7965(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) ClippingMask arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7) {
        this.Z(arg0, arg1, arg2, arg3, arg4, 1, arg5, arg6, arg7);
    }

    @OriginalMember(owner = "client!oa", name = "s", descriptor = "()Z")
    @Override
    public boolean method7979() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIII)V")
    @Override
    public void method7959(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
    }

    @OriginalMember(owner = "client!oa", name = "JA", descriptor = "(IIIIII)I")
    public native int JA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5);

    @OriginalMember(owner = "client!oa", name = "d", descriptor = "(IIIIII)V")
    @Override
    public void method7976(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.U(arg0, arg1, arg2, arg4, arg5);
        this.U(arg0, arg1 + arg3 - 1, arg2, arg4, arg5);
        this.P(arg0, arg1 + 1, arg3 - 1, arg4, arg5);
        this.P(arg0 + arg2 - 1, arg1 + 1, arg3 - 1, arg4, arg5);
    }

    @OriginalMember(owner = "client!oa", name = "DA", descriptor = "(IIII)V")
    public native void DA(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(I)Lclient!za;")
    @Override
    public Node_Sub13 method7961(@OriginalArg(0) int arg0) {
        @Pc(5) ya local5 = new ya(this, arg0);
        this.aDeque_38.addLast(local5);
        return local5;
    }

    @OriginalMember(owner = "client!oa", name = "AA", descriptor = "(SSIBBIZBBBBZZZZZBZZI)V")
    public native void AA(@OriginalArg(0) short arg0, @OriginalArg(1) short arg1, @OriginalArg(2) int arg2, @OriginalArg(3) byte arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) boolean arg6, @OriginalArg(7) byte arg7, @OriginalArg(8) byte arg8, @OriginalArg(9) byte arg9, @OriginalArg(10) byte arg10, @OriginalArg(11) boolean arg11, @OriginalArg(12) boolean arg12, @OriginalArg(13) boolean arg13, @OriginalArg(14) boolean arg14, @OriginalArg(15) boolean arg15, @OriginalArg(16) byte arg16, @OriginalArg(17) boolean arg17, @OriginalArg(18) boolean arg18, @OriginalArg(19) int arg19);

    @OriginalMember(owner = "client!oa", name = "k", descriptor = "(I)V")
    @Override
    public void method8020(@OriginalArg(0) int arg0) {
        this.anAArray1[arg0].method1();
    }

    @OriginalMember(owner = "client!oa", name = "i", descriptor = "()I")
    public native int i();

    @OriginalMember(owner = "client!oa", name = "p", descriptor = "()Z")
    @Override
    public boolean method7968() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "([I)V")
    @Override
    public void method7944(@OriginalArg(0) int[] arg0) {
        @Pc(4) Dimension local4 = this.aP1.aCanvas9.getSize();
        arg0[0] = local4.width;
        arg0[1] = local4.height;
    }

    @OriginalMember(owner = "client!oa", name = "ZA", descriptor = "(IFFFFF)V")
    public native void ZA(@OriginalArg(0) int arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2, @OriginalArg(3) float arg3, @OriginalArg(4) float arg4, @OriginalArg(5) float arg5);

    @OriginalMember(owner = "client!oa", name = "q", descriptor = "()V")
    @Override
    public void method8012() {
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIIILclient!aa;IIIII)V")
    @Override
    public void method7942(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) ClippingMask arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7, @OriginalArg(9) int arg8, @OriginalArg(10) int arg9, @OriginalArg(11) int arg10) {
    }

    @OriginalMember(owner = "client!oa", name = "za", descriptor = "(IIIII)V")
    protected native void za(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int radius, @OriginalArg(3) int colour, @OriginalArg(4) int mode);

    @OriginalMember(owner = "client!oa", name = "HA", descriptor = "(IIII[I)V")
    public native void HA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int[] arg4);

    @OriginalMember(owner = "client!oa", name = "Q", descriptor = "(IIIIII[BII)V")
    public native void Q(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) byte[] arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8);

    @OriginalMember(owner = "client!oa", name = "X", descriptor = "(I)V")
    public native void X(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!oa", name = "d", descriptor = "()V")
    @Override
    public void method7974() {
    }

    @OriginalMember(owner = "client!oa", name = "da", descriptor = "(III[I)V")
    public native void da(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!gaa;)V")
    @Override
    public void method7939(@OriginalArg(0) Interface9 arg0) {
        @Pc(2) wa local2 = (wa) arg0;
        this.n(local2.aJ1.nativeid, local2.aXa1.nativeid);
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIIIIIIIIII)V")
    @Override
    public void method7994() {
    }

    @OriginalMember(owner = "client!oa", name = "o", descriptor = "()Z")
    @Override
    public boolean method7936() {
        return false;
    }

    @OriginalMember(owner = "client!oa", name = "x", descriptor = "()Z")
    @Override
    public boolean method7937() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "z", descriptor = "()Z")
    @Override
    public boolean method7990() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!dv;IIII)Lclient!ka;")
    @Override
    public Model createModel(@OriginalArg(0) Mesh mesh, @OriginalArg(1) int functionMask, @OriginalArg(2) int featureMask, @OriginalArg(3) int ambient, @OriginalArg(4) int contrast) {
        return new i(this, this.aYa2, mesh, functionMask, featureMask, ambient, contrast);
    }

    @OriginalMember(owner = "client!oa", name = "b", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public void method8022(@OriginalArg(0) Canvas arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(8) p local8 = (p) this.aHashTable_33.get((long) arg0.hashCode());
        if (local8 == null) {
            try {
                @Pc(15) Class local15 = Class.forName("java.awt.Canvas");
                @Pc(27) Method local27 = local15.getMethod("setIgnoreRepaint", Boolean.TYPE);
                local27.invoke(arg0, Boolean.TRUE);
            } catch (@Pc(39) Exception local39) {
            }
            local8 = new p(this, arg0, arg1, arg2);
            this.aHashTable_33.put((long) arg0.hashCode(), local8);
        } else if (local8.anInt7161 != arg1 || local8.anInt7162 != arg2) {
            local8.method6439(arg0, arg1, arg2);
        }
    }

    @OriginalMember(owner = "client!oa", name = "b", descriptor = "(IIIID)V")
    public native void b(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) double arg4);

    @OriginalMember(owner = "client!oa", name = "w", descriptor = "(Z)V")
    public native void w(@OriginalArg(0) boolean arg0);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(II[I[I)Lclient!aa;")
    @Override
    public ClippingMask createMask(@OriginalArg(0) int width, @OriginalArg(1) int height, @OriginalArg(2) int[] offsets, @OriginalArg(3) int[] widths) {
        return new na(this, this.aYa2, width, height, offsets, widths);
    }

    @OriginalMember(owner = "client!oa", name = "c", descriptor = "()Lclient!dp;")
    @Override
    public Class84 method7981() {
        return new Class84(0, "SSE", 1, "CPU", 0L);
    }

    @OriginalMember(owner = "client!oa", name = "t", descriptor = "()Z")
    @Override
    public boolean method8015() {
        return false;
    }

    @OriginalMember(owner = "client!oa", name = "y", descriptor = "()Lclient!tt;")
    @Override
    public Matrix createMatrix() {
        return new ja();
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIZ)Lclient!st;")
    @Override
    public Sprite method7964(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        return new j(this, arg0, arg1, arg2, arg3, !arg4);
    }

    @OriginalMember(owner = "client!oa", name = "l", descriptor = "()Z")
    @Override
    public boolean method7978() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "n", descriptor = "(JJ)V")
    public native void n(@OriginalArg(0) long arg0, @OriginalArg(1) long arg1);

    @OriginalMember(owner = "client!oa", name = "h", descriptor = "()V")
    @Override
    public void method7969() {
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIZ)Lclient!st;")
    @Override
    public Sprite method7963(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2) {
        return new j(this, arg0, arg1);
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void method7951(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.wa(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!oa", name = "ma", descriptor = "(J)V")
    public native void ma(@OriginalArg(0) long arg0);

    @OriginalMember(owner = "client!oa", name = "ya", descriptor = "()V")
    public native void ya();

    @OriginalMember(owner = "client!oa", name = "C", descriptor = "()Lclient!a;")
    public a method6087() {
        for (@Pc(1) int local1 = 0; local1 < this.anInt6770; local1++) {
            if (this.anAArray1[local1].aRunnable1 == Thread.currentThread()) {
                return this.anAArray1[local1];
            }
        }
        return null;
    }

    @OriginalMember(owner = "client!oa", name = "pa", descriptor = "()V")
    public native void pa();

    @OriginalMember(owner = "client!oa", name = "xa", descriptor = "(F)V")
    public native void xa(@OriginalArg(0) float globalAmbient);

    @OriginalMember(owner = "client!oa", name = "L", descriptor = "(III)V")
    public native void L(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2);

    @OriginalMember(owner = "client!oa", name = "wa", descriptor = "(IIIIII)V")
    public native void wa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5);

    @OriginalMember(owner = "client!oa", name = "WA", descriptor = "(S)Z")
    public boolean WA(@OriginalArg(0) short arg0) {
        @Pc(2) TextureSource local2 = super.textureSource;
        synchronized (super.textureSource) {
            if (!super.textureSource.textureAvailable(arg0)) {
                return false;
            }
            @Pc(22) TextureMetrics local22 = super.textureSource.getMetrics(arg0);
            if (local22 == null) {
                return false;
            }
            @Pc(44) int[] local44;
            if (local22.alphaBlendMode == 2) {
                local44 = super.textureSource.argbOutput(0.7F, arg0, 128, 128);
            } else {
                local44 = super.textureSource.rgbOutput(128, true, 128, arg0, 0.7F);
            }
            this.CA(arg0, local44, local22.aShort37, local22.alphaBlendMode, local22.effectType, local22.effectParam1, local22.effectParam2, local22.small, local22.alpha, local22.aByte57, local22.speedU, local22.speedV, local22.aBoolean240, local22.aBoolean234, local22.aBoolean239, local22.aBoolean236, local22.aBoolean235, local22.aByte53, local22.aBoolean237, local22.aBoolean238, local22.colorOp);
            return true;
        }
    }

    @OriginalMember(owner = "client!oa", name = "CA", descriptor = "(S[ISIBBIZBBBBZZZZZBZZI)V")
    public native void CA(@OriginalArg(0) short arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) short arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) byte arg5, @OriginalArg(6) int arg6, @OriginalArg(7) boolean arg7, @OriginalArg(8) byte arg8, @OriginalArg(9) byte arg9, @OriginalArg(10) byte arg10, @OriginalArg(11) byte arg11, @OriginalArg(12) boolean arg12, @OriginalArg(13) boolean arg13, @OriginalArg(14) boolean arg14, @OriginalArg(15) boolean arg15, @OriginalArg(16) boolean arg16, @OriginalArg(17) byte arg17, @OriginalArg(18) boolean arg18, @OriginalArg(19) boolean arg19, @OriginalArg(20) int arg20);

    @OriginalMember(owner = "client!oa", name = "c", descriptor = "(IIIIII)Lclient!pu;")
    @Override
    public Class67 method8008(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        return null;
    }

    @OriginalMember(owner = "client!oa", name = "finalize", descriptor = "()V")
    @Override
    public synchronized void finalize() {
        this.free();
        if (this.nativeid != 0L) {
            Static307.method4477(this);
        }
    }

    @OriginalMember(owner = "client!oa", name = "f", descriptor = "(I)V")
    @Override
    public void method7989(@OriginalArg(0) int arg0) {
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(I[Lclient!lca;)V")
    @Override
    public void method8009(@OriginalArg(0) int arg0, @OriginalArg(1) PointLight[] arg1) {
        @Pc(1) int local1 = 0;
        for (@Pc(3) int local3 = 0; local3 < arg0; local3++) {
            Static445.anIntArray540[local1++] = arg1[local3].getX();
            Static445.anIntArray540[local1++] = arg1[local3].getY();
            Static445.anIntArray540[local1++] = arg1[local3].getZ();
            Static445.anIntArray540[local1++] = arg1[local3].getRange();
            Static445.aFloatArray44[local3] = arg1[local3].getIntensity();
            Static445.anIntArray540[local1++] = arg1[local3].method8431();
        }
        this.N(arg0, Static445.anIntArray540, Static445.aFloatArray44);
    }

    @OriginalMember(owner = "client!oa", name = "t", descriptor = "(Lclient!p;)V")
    public native void t(@OriginalArg(0) p arg0);

    @OriginalMember(owner = "client!oa", name = "b", descriptor = "(II)I")
    @Override
    public int compareFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        @Pc(3) int local3 = maskA & 0xFFFFF;
        @Pc(7) int local7 = maskB & 0xFFFFF;
        return local3 & local7 ^ local7;
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "([Ljava/awt/Rectangle;III)V")
    @Override
    public void method8011(@OriginalArg(0) Rectangle[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) throws Exception_Sub1 {
        if (this.aP1 == null) {
            throw new IllegalStateException("off");
        }
        this.aP1.method6441(arg0, arg1, arg2, arg3);
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(II[[I[[IIII)Lclient!s;")
    @Override
    public Ground createGround(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[][] arg2, @OriginalArg(3) int[][] arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        return new t(this, this.aYa2, arg0, arg1, arg2, arg3, 512, arg4, arg5);
    }

    @OriginalMember(owner = "client!oa", name = "n", descriptor = "()Lclient!tt;")
    @Override
    public Matrix method8017() {
        return this.aMatrix_9;
    }

    @OriginalMember(owner = "client!oa", name = "OA", descriptor = "()Ljava/lang/Object;")
    public Object OA() {
        return new ba(this);
    }

    @OriginalMember(owner = "client!oa", name = "U", descriptor = "(IIIII)V")
    public native void U(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4);

    @OriginalMember(owner = "client!oa", name = "I", descriptor = "()I")
    public native int I();

    @OriginalMember(owner = "client!oa", name = "EA", descriptor = "(IIII)V")
    public native void EA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!oa", name = "na", descriptor = "(IIII)[I")
    public native int[] na(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!oa", name = "K", descriptor = "([I)V")
    public native void K(@OriginalArg(0) int[] arg0);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!lk;I)V")
    @Override
    public void method7967(@OriginalArg(0) ParticleList arg0, @OriginalArg(1) int arg1) {
        this.method6085(arg0);
        this.method6087().method4(this, Static445.anIntArray539, Static445.anIntArray541, Static445.anIntArray538, Static445.aShortArray103, arg0.particles.size());
    }

    @OriginalMember(owner = "client!oa", name = "T", descriptor = "(IIII)V")
    public native void T(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void setCamera(@OriginalArg(0) Matrix matrix) {
        this.aMatrix_9 = matrix;
        this.ma(((ja) matrix).nativeid);
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!lk;)V")
    @Override
    public void method8021(@OriginalArg(0) ParticleList arg0) {
        if (arg0.particles.size() != 0) {
            this.method6085(arg0);
            this.method6087().method4(this, Static445.anIntArray539, Static445.anIntArray541, Static445.anIntArray538, Static445.aShortArray103, arg0.particles.size());
        }
    }

    @OriginalMember(owner = "client!oa", name = "P", descriptor = "(IIIII)V")
    public native void P(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!wp;Z)Lclient!st;")
    @Override
    public Sprite method7948(@OriginalArg(0) IndexedImage arg0, @OriginalArg(1) boolean arg1) {
        @Pc(17) j local17 = new j(this, arg0.palette, arg0.raster, arg0.alpha, 0, arg0.width, arg0.width, arg0.height);
        local17.method8184(arg0.offX1, arg0.offY1, arg0.offX2, arg0.offY2);
        return local17;
    }

    @OriginalMember(owner = "client!oa", name = "g", descriptor = "()V")
    public void g() {
        System.gc();
        System.runFinalization();
        Static307.method4478();
    }

    @OriginalMember(owner = "client!oa", name = "f", descriptor = "()V")
    @Override
    public void method7980() {
    }

    @OriginalMember(owner = "client!oa", name = "e", descriptor = "(II)V")
    @Override
    public void method7975(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) throws Exception_Sub1 {
        if (this.aP1 == null) {
            throw new IllegalStateException("off");
        }
        this.aP1.method6438(arg0, arg1);
    }

    @OriginalMember(owner = "client!oa", name = "N", descriptor = "(I[I[F)V")
    public native void N(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1, @OriginalArg(2) float[] arg2);

    @OriginalMember(owner = "client!oa", name = "b", descriptor = "()Z")
    @Override
    public boolean method7983() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "Z", descriptor = "(IIIIIILclient!aa;II)V")
    public native void Z(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) ClippingMask arg6, @OriginalArg(7) int arg7, @OriginalArg(8) int arg8);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(FFF)V")
    @Override
    public void method7993(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2) {
    }

    @OriginalMember(owner = "client!oa", name = "u", descriptor = "()V")
    @Override
    protected void method7987() {
        if (this.aBoolean509) {
            return;
        }
        this.anAArray1 = null;
        this.aP1 = null;
        this.aYa2 = null;
        this.aMatrix_8 = null;
        this.aHashTable_33.clear();
        for (@Pc(26) ya local26 = (ya) this.aDeque_38.first(); local26 != null; local26 = (ya) this.aDeque_38.next()) {
            local26.ga();
        }
        this.aDeque_38.clear();
        this.FA();
        if (this.aBoolean510) {
            Static300.method4390(false, true);
            this.aBoolean510 = false;
        }
        this.g();
        Static307.method4481();
        this.aBoolean509 = true;
    }

    @OriginalMember(owner = "client!oa", name = "r", descriptor = "()Z")
    @Override
    public boolean method8006() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "d", descriptor = "(II)Lclient!wja;")
    @Override
    public Interface26 method7986(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return new xa(arg0, arg1);
    }

    @OriginalMember(owner = "client!oa", name = "la", descriptor = "()V")
    public native void la();

    @OriginalMember(owner = "client!oa", name = "va", descriptor = "(Lclient!za;)V")
    public native void va(@OriginalArg(0) Node_Sub13 arg0);

    @OriginalMember(owner = "client!oa", name = "C", descriptor = "(Z)V")
    public native void C(@OriginalArg(0) boolean arg0);

    @OriginalMember(owner = "client!oa", name = "aa", descriptor = "(IIIIII)V")
    public native void aa(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int colour, @OriginalArg(5) int mode);

    @OriginalMember(owner = "client!oa", name = "B", descriptor = "()Z")
    @Override
    public boolean method8001() {
        return true;
    }

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Lclient!eca;Lclient!wja;)Lclient!gaa;")
    @Override
    public Interface9 method7988(@OriginalArg(0) Surface arg0, @OriginalArg(1) Interface26 arg1) {
        return new wa(this, (j) arg0, (xa) arg1);
    }

    @OriginalMember(owner = "client!oa", name = "A", descriptor = "()Lclient!tt;")
    @Override
    public Matrix scratchMatrix() {
        return this.aMatrix_8;
    }

    @OriginalMember(owner = "client!oa", name = "XA", descriptor = "()I")
    public native int XA();

    @OriginalMember(owner = "client!oa", name = "A", descriptor = "(ILclient!aa;II)V")
    public native void A(@OriginalArg(0) int colour, @OriginalArg(1) ClippingMask clippingMask, @OriginalArg(2) int x, @OriginalArg(3) int y);

    @OriginalMember(owner = "client!oa", name = "GA", descriptor = "(I)V")
    public native void GA(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!oa", name = "a", descriptor = "(Z)V")
    @Override
    public void method7997(@OriginalArg(0) boolean arg0) {
    }
}

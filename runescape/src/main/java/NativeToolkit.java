import com.jagex.ParticleList;
import com.jagex.IndexedImage;
import com.jagex.Class67;
import com.jagex.math.ColourUtils;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.Node;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.MemoryPool;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeap;
import jaclib.memory.heap.NativeHeapBuffer;
import jagex3.graphics2.hw.NativeInterface;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

@OriginalClass("client!am")
public abstract class NativeToolkit extends Toolkit {

    @OriginalMember(owner = "client!am", name = "J", descriptor = "Lclient!jaclib/memory/heap/NativeHeap;")
    public NativeHeap aNativeHeap5;

    @OriginalMember(owner = "client!am", name = "Hd", descriptor = "Ljava/util/Hashtable;")
    public Hashtable aHashtable6;

    @OriginalMember(owner = "client!am", name = "peer", descriptor = "J")
    public long peer;

    @OriginalMember(owner = "client!am", name = "ae", descriptor = "Lclient!jaclib/memory/heap/NativeHeapBuffer;")
    public NativeHeapBuffer aNativeHeapBuffer6;

    @OriginalMember(owner = "client!am", name = "nd", descriptor = "Lclient!ae;")
    protected Class7 aClass7_8;

    @OriginalMember(owner = "client!am", name = "Lf", descriptor = "I")
    public int anInt9141;

    @OriginalMember(owner = "client!am", name = "Hb", descriptor = "I")
    protected int anInt9143;

    @OriginalMember(owner = "client!am", name = "mf", descriptor = "I")
    public int anInt9145;

    @OriginalMember(owner = "client!am", name = "Je", descriptor = "F")
    public float aFloat182;

    @OriginalMember(owner = "client!am", name = "Ce", descriptor = "Lclient!rea;")
    public RenderPass aClass26_3;

    @OriginalMember(owner = "client!am", name = "pc", descriptor = "F")
    public float aFloat183;

    @OriginalMember(owner = "client!am", name = "Cf", descriptor = "I")
    public int anInt9149;

    @OriginalMember(owner = "client!am", name = "sf", descriptor = "Z")
    public boolean aBoolean681;

    @OriginalMember(owner = "client!am", name = "id", descriptor = "[Lclient!fba;")
    protected Class121[] aClass121Array5;

    @OriginalMember(owner = "client!am", name = "le", descriptor = "F")
    public float aFloat185;

    @OriginalMember(owner = "client!am", name = "tg", descriptor = "Z")
    protected boolean aBoolean683;

    @OriginalMember(owner = "client!am", name = "uc", descriptor = "F")
    public float aFloat188;

    @OriginalMember(owner = "client!am", name = "Me", descriptor = "[Lclient!mw;")
    public Interface17[] anInterface17Array3;

    @OriginalMember(owner = "client!am", name = "jd", descriptor = "Z")
    public boolean aBoolean685;

    @OriginalMember(owner = "client!am", name = "Tc", descriptor = "F")
    public float aFloat189;

    @OriginalMember(owner = "client!am", name = "Ne", descriptor = "F")
    public float aFloat190;

    @OriginalMember(owner = "client!am", name = "Db", descriptor = "I")
    protected int anInt9157;

    @OriginalMember(owner = "client!am", name = "xb", descriptor = "I")
    public int anInt9160;

    @OriginalMember(owner = "client!am", name = "kc", descriptor = "[Lclient!cw;")
    protected Matrix_Sub1[] aClass73_Sub1Array3;

    @OriginalMember(owner = "client!am", name = "Gf", descriptor = "F")
    public float aFloat192;

    @OriginalMember(owner = "client!am", name = "If", descriptor = "I")
    public int anInt9164;

    @OriginalMember(owner = "client!am", name = "Id", descriptor = "I")
    public int anInt9166;

    @OriginalMember(owner = "client!am", name = "Ze", descriptor = "F")
    public float aFloat193;

    @OriginalMember(owner = "client!am", name = "Se", descriptor = "I")
    public int anInt9168;

    @OriginalMember(owner = "client!am", name = "Zd", descriptor = "Z")
    protected boolean aBoolean692;

    @OriginalMember(owner = "client!am", name = "ic", descriptor = "[Lclient!uf;")
    protected Class370[] aClass370Array3;

    @OriginalMember(owner = "client!am", name = "Hf", descriptor = "[Lclient!lca;")
    protected PointLight[] aClass2_Sub7Array6;

    @OriginalMember(owner = "client!am", name = "df", descriptor = "Lclient!eda;")
    public Class67_Sub2 aClass67_Sub2_3;

    @OriginalMember(owner = "client!am", name = "Hc", descriptor = "[Lclient!fba;")
    protected Class121[] aClass121Array6;

    @OriginalMember(owner = "client!am", name = "bc", descriptor = "Lclient!rc;")
    public NativeParticleRenderer particleRenderer;

    @OriginalMember(owner = "client!am", name = "Cd", descriptor = "I")
    protected int anInt9176;

    @OriginalMember(owner = "client!am", name = "ld", descriptor = "Lclient!mw;")
    public Interface17 anInterface17_3;

    @OriginalMember(owner = "client!am", name = "He", descriptor = "I")
    protected int anInt9177;

    @OriginalMember(owner = "client!am", name = "Pe", descriptor = "Z")
    public boolean aBoolean696;

    @OriginalMember(owner = "client!am", name = "R", descriptor = "I")
    protected int anInt9182;

    @OriginalMember(owner = "client!am", name = "me", descriptor = "I")
    public int anInt9184;

    @OriginalMember(owner = "client!am", name = "ye", descriptor = "Lclient!mk;")
    public Class244 aClass244_17;

    @OriginalMember(owner = "client!am", name = "ng", descriptor = "Lclient!mg;")
    public Interface16 anInterface16_13;

    @OriginalMember(owner = "client!am", name = "bb", descriptor = "Lclient!mg;")
    public Interface16 anInterface16_14;

    @OriginalMember(owner = "client!am", name = "vg", descriptor = "Lclient!mk;")
    public Class244 aClass244_18;

    @OriginalMember(owner = "client!am", name = "be", descriptor = "Lclient!mk;")
    public Class244 aClass244_19;

    @OriginalMember(owner = "client!am", name = "Be", descriptor = "Lclient!ri;")
    public Interface20 anInterface20_6;

    @OriginalMember(owner = "client!am", name = "tf", descriptor = "Lclient!mk;")
    public Class244 aClass244_20;

    @OriginalMember(owner = "client!am", name = "pb", descriptor = "Lclient!mk;")
    public Class244 aClass244_21;

    @OriginalMember(owner = "client!am", name = "kd", descriptor = "Lclient!mg;")
    public Interface16 anInterface16_15;

    @OriginalMember(owner = "client!am", name = "Gd", descriptor = "Lclient!mk;")
    public Class244 aClass244_22;

    @OriginalMember(owner = "client!am", name = "M", descriptor = "Lclient!mk;")
    public Class244 aClass244_23;

    @OriginalMember(owner = "client!am", name = "Qc", descriptor = "Z")
    protected boolean aBoolean697;

    @OriginalMember(owner = "client!am", name = "md", descriptor = "I")
    public int anInt9186;

    @OriginalMember(owner = "client!am", name = "vb", descriptor = "Lclient!sia;")
    public final Deque aDeque_59 = new Deque();

    @OriginalMember(owner = "client!am", name = "jg", descriptor = "Z")
    protected boolean aBoolean676 = true;

    @OriginalMember(owner = "client!am", name = "Xc", descriptor = "Lclient!cw;")
    protected final Matrix_Sub1 aClass73_Sub1_15 = new Matrix_Sub1();

    @OriginalMember(owner = "client!am", name = "wg", descriptor = "Lclient!cw;")
    public Matrix_Sub1 aClass73_Sub1_16 = new Matrix_Sub1();

    @OriginalMember(owner = "client!am", name = "O", descriptor = "Lclient!cw;")
    public final Matrix_Sub1 aClass73_Sub1_17 = new Matrix_Sub1();

    @OriginalMember(owner = "client!am", name = "C", descriptor = "Lclient!cw;")
    protected final Matrix_Sub1 aClass73_Sub1_18 = new Matrix_Sub1();

    @OriginalMember(owner = "client!am", name = "Le", descriptor = "Lclient!cw;")
    public final Matrix_Sub1 aClass73_Sub1_19 = new Matrix_Sub1();

    @OriginalMember(owner = "client!am", name = "ee", descriptor = "Lclient!cw;")
    public final Matrix_Sub1 aClass73_Sub1_20 = new Matrix_Sub1();

    @OriginalMember(owner = "client!am", name = "Ie", descriptor = "I")
    protected int anInt9148 = 0;

    @OriginalMember(owner = "client!am", name = "V", descriptor = "Z")
    protected boolean aBoolean677 = true;

    @OriginalMember(owner = "client!am", name = "wf", descriptor = "F")
    public float aFloat180 = -1.0F;

    @OriginalMember(owner = "client!am", name = "oe", descriptor = "I")
    public int anInt9146 = -1;

    @OriginalMember(owner = "client!am", name = "rd", descriptor = "I")
    public int anInt9151 = 512;

    @OriginalMember(owner = "client!am", name = "Qe", descriptor = "Z")
    protected boolean aBoolean679 = true;

    @OriginalMember(owner = "client!am", name = "gg", descriptor = "F")
    public float aFloat187 = 3584.0F;

    @OriginalMember(owner = "client!am", name = "se", descriptor = "[F")
    public final float[] aFloatArray58 = new float[]{0.0F, 0.0F, 1.0F, 0.0F};

    @OriginalMember(owner = "client!am", name = "de", descriptor = "Z")
    protected boolean aBoolean680 = false;

    @OriginalMember(owner = "client!am", name = "Te", descriptor = "I")
    public int anInt9150 = -1;

    @OriginalMember(owner = "client!am", name = "y", descriptor = "I")
    protected int anInt9154 = 0;

    @OriginalMember(owner = "client!am", name = "rb", descriptor = "I")
    public int anInt9159 = 8;

    @OriginalMember(owner = "client!am", name = "Cb", descriptor = "[F")
    public final float[] aFloatArray61 = new float[]{0.0F, 0.0F, -1.0F, 0.0F};

    @OriginalMember(owner = "client!am", name = "Ld", descriptor = "Z")
    public boolean aBoolean687 = false;

    @OriginalMember(owner = "client!am", name = "Rc", descriptor = "F")
    public float aFloat186 = 1.0F;

    @OriginalMember(owner = "client!am", name = "De", descriptor = "Z")
    protected boolean aBoolean678 = false;

    @OriginalMember(owner = "client!am", name = "eg", descriptor = "F")
    public float aFloat184 = 1.0F;

    @OriginalMember(owner = "client!am", name = "ab", descriptor = "I")
    public int anInt9162 = 16777215;

    @OriginalMember(owner = "client!am", name = "eb", descriptor = "I")
    public int anInt9152 = 0;

    @OriginalMember(owner = "client!am", name = "rc", descriptor = "[F")
    public final float[] aFloatArray60 = new float[]{0.0F, 0.0F, 1.0F, 0.0F};

    @OriginalMember(owner = "client!am", name = "jb", descriptor = "I")
    protected int anInt9161 = 0;

    @OriginalMember(owner = "client!am", name = "Ac", descriptor = "Z")
    protected boolean aBoolean689 = true;

    @OriginalMember(owner = "client!am", name = "lg", descriptor = "I")
    protected final int anInt9155 = 0;

    @OriginalMember(owner = "client!am", name = "vd", descriptor = "I")
    public int anInt9163 = 0;

    @OriginalMember(owner = "client!am", name = "Ud", descriptor = "Z")
    protected boolean aBoolean684 = true;

    @OriginalMember(owner = "client!am", name = "Ee", descriptor = "F")
    public float aFloat191 = 1.0F;

    @OriginalMember(owner = "client!am", name = "Oc", descriptor = "I")
    protected final int anInt9147 = 0;

    @OriginalMember(owner = "client!am", name = "we", descriptor = "[F")
    public final float[] aFloatArray59 = new float[]{0.0F, 0.0F, 1.0F, 0.0F};

    @OriginalMember(owner = "client!am", name = "Fe", descriptor = "I")
    public int anInt9170 = 0;

    @OriginalMember(owner = "client!am", name = "Uc", descriptor = "[F")
    public final float[] aFloatArray65 = new float[16];

    @OriginalMember(owner = "client!am", name = "mc", descriptor = "Z")
    protected boolean aBoolean691 = false;

    @OriginalMember(owner = "client!am", name = "Qd", descriptor = "[F")
    public final float[] aFloatArray62 = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F};

    @OriginalMember(owner = "client!am", name = "nf", descriptor = "Z")
    public boolean aBoolean694 = false;

    @OriginalMember(owner = "client!am", name = "E", descriptor = "I")
    public int anInt9156 = -1;

    @OriginalMember(owner = "client!am", name = "Ae", descriptor = "Z")
    protected boolean aBoolean690 = true;

    @OriginalMember(owner = "client!am", name = "xf", descriptor = "I")
    public int anInt9175 = -1;

    @OriginalMember(owner = "client!am", name = "Of", descriptor = "[F")
    protected float[] aFloatArray63 = this.aFloatArray62;

    @OriginalMember(owner = "client!am", name = "vf", descriptor = "I")
    public int anInt9180 = 512;

    @OriginalMember(owner = "client!am", name = "ob", descriptor = "I")
    protected int anInt9167 = 3584;

    @OriginalMember(owner = "client!am", name = "Xd", descriptor = "Lclient!bg;")
    protected Class38 aClass38_7 = Static80.aClass38_2;

    @OriginalMember(owner = "client!am", name = "je", descriptor = "Lclient!dfa;")
    protected Class77 aClass77_6 = Static243.aClass77_3;

    @OriginalMember(owner = "client!am", name = "ff", descriptor = "Z")
    public boolean aBoolean693 = false;

    @OriginalMember(owner = "client!am", name = "Mc", descriptor = "[F")
    public final float[] aFloatArray64 = new float[16];

    @OriginalMember(owner = "client!am", name = "Z", descriptor = "I")
    public int anInt9174 = 0;

    @OriginalMember(owner = "client!am", name = "qg", descriptor = "I")
    public int anInt9169 = 0;

    @OriginalMember(owner = "client!am", name = "pd", descriptor = "I")
    public int anInt9179 = -1;

    @OriginalMember(owner = "client!am", name = "gf", descriptor = "Z")
    public boolean aBoolean688 = false;

    @OriginalMember(owner = "client!am", name = "Nf", descriptor = "I")
    public int anInt9172 = 0;

    @OriginalMember(owner = "client!am", name = "I", descriptor = "I")
    public int anInt9181 = 50;

    @OriginalMember(owner = "client!am", name = "dg", descriptor = "F")
    public float aFloat195 = 1.0F;

    @OriginalMember(owner = "client!am", name = "Re", descriptor = "I")
    public int anInt9173 = 3;

    @OriginalMember(owner = "client!am", name = "sc", descriptor = "I")
    protected int anInt9153 = 0;

    @OriginalMember(owner = "client!am", name = "Qb", descriptor = "I")
    protected int anInt9165 = 0;

    @OriginalMember(owner = "client!am", name = "Eb", descriptor = "I")
    public int anInt9183 = 128;

    @OriginalMember(owner = "client!am", name = "hc", descriptor = "Z")
    public boolean aBoolean682 = false;

    @OriginalMember(owner = "client!am", name = "pf", descriptor = "[Lclient!rea;")
    public final RenderPass[] aClass26Array3 = new RenderPass[10];

    @OriginalMember(owner = "client!am", name = "Fc", descriptor = "F")
    public float aFloat194 = 1.0F;

    @OriginalMember(owner = "client!am", name = "ib", descriptor = "Z")
    protected boolean aBoolean686 = false;

    @OriginalMember(owner = "client!am", name = "Vf", descriptor = "F")
    public float aFloat181 = -1.0F;

    @OriginalMember(owner = "client!am", name = "hb", descriptor = "I")
    public int anInt9171 = 1;

    @OriginalMember(owner = "client!am", name = "re", descriptor = "[F")
    public final float[] aFloatArray66 = new float[16];

    @OriginalMember(owner = "client!am", name = "fg", descriptor = "Z")
    public boolean aBoolean695 = false;

    @OriginalMember(owner = "client!am", name = "Ke", descriptor = "F")
    public float aFloat196 = 3584.0F;

    @OriginalMember(owner = "client!am", name = "Oe", descriptor = "I")
    public int anInt9185 = 0;

    @OriginalMember(owner = "client!am", name = "v", descriptor = "Lclient!jaclib/memory/Stream;")
    public final Stream aStream6 = new Stream();

    @OriginalMember(owner = "client!am", name = "bd", descriptor = "[Lclient!eu;")
    public final Model_Sub1[] aClass114_Sub1Array5 = new Model_Sub1[7];

    @OriginalMember(owner = "client!am", name = "ug", descriptor = "[Lclient!eu;")
    public final Model_Sub1[] aClass114_Sub1Array6 = new Model_Sub1[7];

    @OriginalMember(owner = "client!am", name = "B", descriptor = "Lclient!cw;")
    public final Matrix_Sub1 aClass73_Sub1_21 = new Matrix_Sub1();

    @OriginalMember(owner = "client!am", name = "Td", descriptor = "Lclient!sb;")
    protected final js5 aJs5_112;

    @OriginalMember(owner = "client!am", name = "T", descriptor = "Ljava/lang/Object;")
    protected final Object anObject16;

    @OriginalMember(owner = "client!am", name = "P", descriptor = "Ljava/lang/Object;")
    public Object anObject15;

    @OriginalMember(owner = "client!am", name = "Af", descriptor = "I")
    protected final int anInt9158;

    @OriginalMember(owner = "client!am", name = "rg", descriptor = "Ljava/awt/Canvas;")
    protected final Canvas aCanvas13;

    @OriginalMember(owner = "client!am", name = "D", descriptor = "Ljava/awt/Canvas;")
    public Canvas aCanvas12;

    @OriginalMember(owner = "client!am", name = "uf", descriptor = "I")
    public final int anInt9178;

    @OriginalMember(owner = "client!am", name = "wc", descriptor = "I")
    public int anInt9021;

    @OriginalMember(owner = "client!am", name = "mb", descriptor = "I")
    public int anInt9013;

    @OriginalMember(owner = "client!am", name = "kf", descriptor = "I")
    public int anInt8991;

    @OriginalMember(owner = "client!am", name = "wb", descriptor = "I")
    public int anInt9122;

    @OriginalMember(owner = "client!am", name = "ze", descriptor = "Lclient!jagex3/graphics2/hw/NativeInterface;")
    public final NativeInterface aNativeInterface3;

    @OriginalMember(owner = "client!am", name = "Ye", descriptor = "Lclient!sf;")
    public final Class334 aClass334_3;

    @OriginalMember(owner = "client!am", name = "<init>", descriptor = "(Ljava/awt/Canvas;Ljava/lang/Object;Lclient!d;Lclient!sb;II)V")
    protected NativeToolkit(@OriginalArg(0) Canvas arg0, @OriginalArg(1) Object arg1, @OriginalArg(2) TextureSource arg2, @OriginalArg(3) js5 arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        super(arg2);
        try {
            this.aJs5_112 = arg3;
            this.anObject15 = this.anObject16 = arg1;
            this.anInt9158 = arg4;
            this.aCanvas12 = this.aCanvas13 = arg0;
            @Pc(313) Dimension local313 = arg0.getSize();
            this.anInt9178 = arg5;
            this.anInt9013 = this.anInt9021 = local313.height;
            this.anInt9122 = this.anInt8991 = local313.width;
            ColourUtils.init(true, false);
            if (super.textureSource == null) {
                this.aNativeInterface3 = new NativeInterface(0, this.anInt9178);
                this.aClass334_3 = null;
            } else {
                this.aClass334_3 = new Class334(this, super.textureSource);
                this.aNativeInterface3 = new NativeInterface(super.textureSource.textureCount(), this.anInt9178);
                for (@Pc(371) int local371 = 0; local371 < super.textureSource.textureCount(); local371++) {
                    @Pc(378) TextureMetrics local378 = super.textureSource.getMetrics(local371);
                    if (local378 != null) {
                        this.aNativeInterface3.initTextureMetrics(local371, local378.alpha, local378.aByte57);
                    }
                }
            }
        } catch (@Pc(399) Throwable local399) {
            local399.printStackTrace();
            this.free();
            throw new RuntimeException("");
        }
    }

    @OriginalMember(owner = "client!am", name = "k", descriptor = "(I)V")
    @Override
    public final void linkThreads(@OriginalArg(0) int arg0) {
    }

    @OriginalMember(owner = "client!am", name = "K", descriptor = "(I)V")
    public void method8023() {
        this.method8129();
        this.method8065();
        this.method8078();
        this.method8049();
        this.method8158();
        this.method8109();
        this.method8089();
        this.method8116();
        this.method8146();
        this.method8084();
        this.method8150();
        this.method8045();
        this.method8035();
        this.method8060();
        for (@Pc(60) int local60 = this.anInt9184 - 1; local60 >= 0; local60--) {
            this.method8138(local60);
            this.method8039();
            this.method8030();
            this.method8031();
        }
        this.method8113();
        this.method8037();
        this.method8110();
        this.method8136();
        this.method8069();
    }

    @OriginalMember(owner = "client!am", name = "E", descriptor = "()I")
    @Override
    public final int E() {
        return this.anInt9141 + this.anInt9145 + this.anInt9143;
    }

    @OriginalMember(owner = "client!am", name = "ZA", descriptor = "(IFFFFF)V")
    @Override
    public final void ZA(@OriginalArg(0) int colour, @OriginalArg(1) float intensity, @OriginalArg(2) float reverseIntensity, @OriginalArg(3) float x, @OriginalArg(4) float y, @OriginalArg(5) float z) {
        @Pc(12) boolean local12 = colour != this.anInt9162;
        if (local12 || this.aFloat181 != intensity || reverseIntensity != this.aFloat180) {
            this.anInt9162 = colour;
            this.aFloat180 = reverseIntensity;
            this.aFloat181 = intensity;
            if (local12) {
                this.aFloat191 = (float) (this.anInt9162 & 0xFF0000) / 1.671168E7F;
                this.aFloat184 = (float) (this.anInt9162 & 0xFF00) / 65280.0F;
                this.aFloat195 = (float) (this.anInt9162 & 0xFF) / 255.0F;
                this.method8129();
            }
            this.aNativeInterface3.setSunColour(this.aFloat191, this.aFloat184, this.aFloat195, intensity, reverseIntensity);
            this.method8065();
        }
        if (x != this.aFloatArray59[0] || y != this.aFloatArray59[1] || this.aFloatArray59[2] != z) {
            this.aFloatArray59[2] = z;
            this.aFloatArray59[1] = y;
            this.aFloatArray59[0] = x;
            this.aFloatArray58[2] = -z;
            this.aFloatArray58[0] = -x;
            this.aFloatArray58[1] = -y;
            @Pc(162) float local162 = (float) (1.0D / Math.sqrt(y * y + x * x + z * z));
            this.aFloatArray60[2] = local162 * z;
            this.aFloatArray60[0] = x * local162;
            this.aFloatArray60[1] = y * local162;
            this.aFloatArray61[1] = -this.aFloatArray60[1];
            this.aFloatArray61[0] = -this.aFloatArray60[0];
            this.aFloatArray61[2] = -this.aFloatArray60[2];
            this.aNativeInterface3.setSunDirection(this.aFloatArray60[0], this.aFloatArray60[1], this.aFloatArray60[2]);
            this.method8158();
            this.anInt9149 = (int) (z * 256.0F / y);
            this.anInt9168 = (int) (x * 256.0F / y);
        }
        this.method8109();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIIF)Lclient!lca;")
    @Override
    public final PointLight method7941(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) float arg5) {
        return new Node_Sub7_Sub2(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!am", name = "za", descriptor = "(IIIII)V")
    @Override
    protected final void za(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int radius, @OriginalArg(3) int colour, @OriginalArg(4) int mode) {
        this.method8149();
        this.method8112(colour);
        this.method8080(0, Static454.aClass168_5);
        this.method8142(Static454.aClass168_5, 0);
        this.method8053(mode);
        this.aClass73_Sub1_15.method1884(1.0F, (float) radius, (float) radius);
        this.aClass73_Sub1_15.translate(x, y, 0);
        this.method8042();
        this.method8141(false);
        this.method8130(0, this.anInterface16_15);
        this.method8114(this.aClass244_18);
        this.method8079(Static694.aClass131_6, 0, 256);
        this.method8141(true);
        this.method8142(Static189.aClass168_2, 0);
        this.method8080(0, Static189.aClass168_2);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!cw;Z)V")
    public final void method8024(@OriginalArg(0) Matrix_Sub1 arg0) {
        this.aClass73_Sub1_15.apply(arg0);
        this.aBoolean676 = false;
        this.method8091();
    }

    @OriginalMember(owner = "client!am", name = "T", descriptor = "(IIII)V")
    @Override
    public final void T(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(5) boolean local5 = false;
        if (this.anInt9154 < arg0) {
            this.anInt9154 = arg0;
            local5 = true;
        }
        if (this.anInt9153 > arg2) {
            this.anInt9153 = arg2;
            local5 = true;
        }
        if (this.anInt9161 < arg1) {
            local5 = true;
            this.anInt9161 = arg1;
        }
        if (this.anInt9165 > arg3) {
            this.anInt9165 = arg3;
            local5 = true;
        }
        if (!local5) {
            return;
        }
        if (!this.aBoolean697) {
            this.aBoolean697 = true;
            this.method8058();
        }
        this.method8076();
        this.method8151();
    }

    @OriginalMember(owner = "client!am", name = "Q", descriptor = "(IIIIII[BII)V")
    @Override
    public final void Q(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int overlayColour, @OriginalArg(5) int underlayColour, @OriginalArg(6) byte[] shape, @OriginalArg(7) int size, @OriginalArg(8) int mode) {
    }

    @OriginalMember(owner = "client!am", name = "l", descriptor = "()Z")
    @Override
    public final boolean method7978() {
        return false;
    }

    @OriginalMember(owner = "client!am", name = "h", descriptor = "(B)I")
    public final int method8025() {
        return this.anInt9156;
    }

    @OriginalMember(owner = "client!am", name = "x", descriptor = "()Z")
    @Override
    public final boolean method7937() {
        return true;
    }

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(II)I")
    @Override
    public final int compareFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        return maskB ^ maskA & maskB;
    }

    @OriginalMember(owner = "client!am", name = "o", descriptor = "(I)I")
    public final int method8026() {
        return this.anInt9148;
    }

    @OriginalMember(owner = "client!am", name = "u", descriptor = "(B)V")
    protected abstract void method8027();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(I[BLclient!eba;IBZ)Lclient!og;")
    public final Interface18 method8028(@OriginalArg(0) int arg0, @OriginalArg(1) byte[] arg1, @OriginalArg(2) Class92 arg2, @OriginalArg(3) int arg3, @OriginalArg(5) boolean arg4) {
        return this.method8103(arg2, arg3, arg4, arg0, arg1);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIZ)Lclient!st;")
    @Override
    public final Sprite createSprite(@OriginalArg(0) int with, @OriginalArg(1) int height, @OriginalArg(2) boolean transparent) {
        return new Sprite_Sub3(this, with, height, transparent);
    }

    @OriginalMember(owner = "client!am", name = "M", descriptor = "()I")
    @Override
    public final int M() {
        return this.anInt9186;
    }

    @OriginalMember(owner = "client!am", name = "q", descriptor = "(B)V")
    public void method8029() {
        if (this.aBoolean695) {
            return;
        }
        @Pc(10) float[] local10 = this.aFloatArray65;
        @Pc(14) float local14 = (float) this.anInt9181;
        @Pc(18) float local18 = (float) this.anInt9167;
        @Pc(30) float local30 = this.aFloat194 * (float) -this.anInt9163 / (float) this.anInt9180;
        @Pc(42) float local42 = this.aFloat194 * (float) -this.anInt9170 / (float) this.anInt9151;
        @Pc(57) float local57 = (float) (this.anInt9122 - this.anInt9170) * this.aFloat194 / (float) this.anInt9151;
        @Pc(72) float local72 = this.aFloat194 * (float) (this.anInt9013 - this.anInt9163) / (float) this.anInt9180;
        if (local57 == local42 || local72 == local30) {
            local10[11] = 0.0F;
            local10[13] = 0.0F;
            local10[1] = 0.0F;
            local10[6] = 0.0F;
            local10[15] = 1.0F;
            local10[8] = 0.0F;
            local10[9] = 0.0F;
            local10[10] = 1.0F;
            local10[5] = 1.0F;
            local10[0] = 1.0F;
            local10[14] = 0.0F;
            local10[12] = 0.0F;
            local10[3] = 0.0F;
            local10[4] = 0.0F;
            local10[7] = 0.0F;
            local10[2] = 0.0F;
        } else {
            local10[11] = 0.0F;
            local10[10] = 1.0F / (local14 - local18);
            local10[7] = 0.0F;
            local10[4] = 0.0F;
            local10[12] = (local42 + local57) / (local42 - local57);
            local10[9] = 0.0F;
            local10[15] = 1.0F;
            local10[13] = (local72 + local30) / (local72 - local30);
            local10[3] = 0.0F;
            local10[14] = local14 / (local14 - local18);
            local10[0] = 2.0F / (local57 - local42);
            local10[6] = 0.0F;
            local10[2] = 0.0F;
            local10[8] = 0.0F;
            local10[5] = 2.0F / (local72 - local30);
            local10[1] = 0.0F;
        }
        this.method8128();
        this.aBoolean695 = true;
    }

    @OriginalMember(owner = "client!am", name = "c", descriptor = "(Z)V")
    protected abstract void method8030();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIIILclient!aa;IIIII)V")
    @Override
    public final void method7942(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int mode, @OriginalArg(6) ClippingMask mask, @OriginalArg(7) int maskX, @OriginalArg(8) int maskY, @OriginalArg(9) int arg8, @OriginalArg(10) int arg9, @OriginalArg(11) int arg10) {
    }

    @OriginalMember(owner = "client!am", name = "g", descriptor = "(B)V")
    public final void method8031() {
        if (this.aClass370Array3[this.anInt9148] != Static215.aClass370_2) {
            this.aClass370Array3[this.anInt9148] = Static215.aClass370_2;
            this.aClass73_Sub1Array3[this.anInt9148].makeIdentity();
            this.method8033();
        }
    }

    @OriginalMember(owner = "client!am", name = "B", descriptor = "(B)V")
    public void method8032() {
        if (this.aBoolean693) {
            return;
        }
        @Pc(17) float[] local17 = this.aFloatArray66;
        @Pc(29) float local29 = (float) (this.anInt9181 * -this.anInt9170) / (float) this.anInt9151;
        @Pc(44) float local44 = (float) (this.anInt9181 * (this.anInt9122 - this.anInt9170)) / (float) this.anInt9151;
        @Pc(55) float local55 = (float) (this.anInt9163 * this.anInt9181) / (float) this.anInt9180;
        @Pc(70) float local70 = (float) ((this.anInt9163 - this.anInt9013) * this.anInt9181) / (float) this.anInt9180;
        if (local29 == local44 || local55 == local70) {
            local17[13] = 0.0F;
            local17[15] = 1.0F;
            local17[4] = 0.0F;
            local17[3] = 0.0F;
            local17[6] = 0.0F;
            local17[12] = 0.0F;
            local17[10] = 1.0F;
            local17[5] = 1.0F;
            local17[1] = 0.0F;
            local17[7] = 0.0F;
            local17[11] = 0.0F;
            local17[9] = 0.0F;
            local17[8] = 0.0F;
            local17[0] = 1.0F;
            local17[14] = 0.0F;
            local17[2] = 0.0F;
        } else {
            @Pc(86) float local86 = (float) this.anInt9181 * 2.0F;
            local17[0] = local86 / (local44 - local29);
            local17[6] = 0.0F;
            local17[1] = 0.0F;
            local17[3] = 0.0F;
            local17[13] = 0.0F;
            local17[12] = 0.0F;
            local17[14] = this.aFloat193 = (float) (this.anInt9167 * this.anInt9181) / (float) (this.anInt9181 - this.anInt9167);
            local17[7] = 0.0F;
            local17[9] = (local70 + local55) / (local55 - local70);
            local17[10] = this.aFloat182 = (float) this.anInt9167 / (float) (this.anInt9181 - this.anInt9167);
            local17[15] = 0.0F;
            local17[2] = 0.0F;
            local17[4] = 0.0F;
            local17[8] = (local29 + local44) / (-local29 + local44);
            local17[11] = -1.0F;
            local17[5] = local86 / (local55 - local70);
        }
        this.method8104();
        this.aBoolean693 = true;
    }

    @OriginalMember(owner = "client!am", name = "z", descriptor = "(I)V")
    public void method8033() {
        this.method8086();
        if (this.aClass26_3 != null) {
            this.aClass26_3.onTextureMatrixChanged();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ZIZI[I)Lclient!og;")
    public final Interface18 method8034(@OriginalArg(0) boolean arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int[] arg3) {
        return this.method8143(arg2, arg3, 0, 0, arg0, arg1);
    }

    @OriginalMember(owner = "client!am", name = "v", descriptor = "(I)V")
    protected abstract void method8035();

    @OriginalMember(owner = "client!am", name = "c", descriptor = "(IIIIII)Lclient!pu;")
    @Override
    public final Class67 method8008(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        return new Class67_Sub2_Sub1(this, arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!am", name = "n", descriptor = "(B)V")
    public void method8036() {
        if (this.aBoolean688) {
            return;
        }
        @Pc(9) float[] local9 = this.aFloatArray64;
        this.aBoolean688 = true;
        if (this.anInt9122 != 0 && this.anInt9013 != 0) {
            local9[6] = 0.0F;
            local9[2] = 0.0F;
            local9[10] = 0.5F;
            local9[11] = 0.0F;
            local9[15] = 1.0F;
            local9[8] = 0.0F;
            local9[5] = -2.0F / (float) this.anInt9013;
            local9[13] = 1.0F;
            local9[1] = 0.0F;
            local9[9] = 0.0F;
            local9[7] = 0.0F;
            local9[12] = -1.0F;
            local9[3] = 0.0F;
            local9[4] = 0.0F;
            local9[14] = 0.5F;
            local9[0] = 2.0F / (float) this.anInt9122;
            return;
        }
        local9[10] = 1.0F;
        local9[9] = 0.0F;
        local9[1] = 0.0F;
        local9[4] = 0.0F;
        local9[12] = 0.0F;
        local9[5] = 1.0F;
        local9[6] = 0.0F;
        local9[14] = 0.0F;
        local9[15] = 1.0F;
        local9[11] = 0.0F;
        local9[13] = 0.0F;
        local9[8] = 0.0F;
        local9[3] = 0.0F;
        local9[2] = 0.0F;
        local9[7] = 0.0F;
        local9[0] = 1.0F;
    }

    @OriginalMember(owner = "client!am", name = "o", descriptor = "(B)V")
    protected abstract void method8037();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIIIIII)V")
    @Override
    public final void method7995(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7) {
        @Pc(12) float local12 = (float) -arg0 + (float) arg2;
        @Pc(18) float local18 = (float) arg3 - (float) arg1;
        @Pc(41) float local41;
        if (local12 == 0.0F && local18 == 0.0F) {
            local12 = 1.0F;
        } else {
            local41 = (float) (1.0D / Math.sqrt(local12 * local12 + local18 * local18));
            local18 *= local41;
            local12 *= local41;
        }
        this.method8149();
        this.method8112(arg4);
        this.method8080(0, Static454.aClass168_5);
        this.method8142(Static454.aClass168_5, 0);
        this.method8053(1);
        this.method8096();
        this.method8141(false);
        @Pc(87) int local87 = arg7 % (arg5 + arg6);
        local41 = (float) arg5 * local12;
        @Pc(97) float local97 = (float) arg5 * local18;
        @Pc(99) float local99 = 0.0F;
        @Pc(101) float local101 = 0.0F;
        @Pc(103) float local103 = local41;
        @Pc(105) float local105 = local97;
        if (arg5 >= local87) {
            local105 = (float) (arg5 - local87) * local18;
            local103 = local12 * (float) (arg5 - local87);
        } else {
            local101 = local18 * (float) (arg5 + arg6 - local87);
            local99 = local12 * (float) (arg5 + arg6 - local87);
        }
        @Pc(156) float local156 = local99 + (float) arg0;
        @Pc(161) float local161 = local101 + (float) arg1;
        @Pc(166) float local166 = (float) arg6 * local12;
        @Pc(171) float local171 = local18 * (float) arg6;
        while (true) {
            if (arg2 > arg0) {
                if ((float) arg2 < local156) {
                    break;
                }
                if (local156 + local103 > (float) arg2) {
                    local103 = (float) arg2 - local156;
                }
            } else {
                if (local156 < (float) arg2) {
                    break;
                }
                if (local103 + local156 < (float) arg2) {
                    local103 = (float) arg2 - local156;
                }
            }
            if (arg3 > arg1) {
                if (local161 > (float) arg3) {
                    break;
                }
                if ((float) arg3 < local161 + local105) {
                    local105 = (float) arg3 - local161;
                }
            } else {
                if (local161 < (float) arg3) {
                    break;
                }
                if ((float) arg3 > local161 + local105) {
                    local105 = (float) arg3 - local161;
                }
            }
            if (!this.method8157(local156 + local103, 0.0F, local156, local105 + local161, local161, 0.0F)) {
                return;
            }
            local156 += local166 + local103;
            this.method8124();
            local161 += local105 + local171;
            local103 = local41;
            local105 = local97;
        }
        this.method8141(true);
        this.method8142(Static189.aClass168_2, 0);
        this.method8080(0, Static189.aClass168_2);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!eba;[BBIII)Lclient!bga;")
    public abstract Interface2 method8038(@OriginalArg(0) Class92 arg0, @OriginalArg(1) byte[] arg1);

    @OriginalMember(owner = "client!am", name = "y", descriptor = "()Lclient!tt;")
    @Override
    public final Matrix createMatrix() {
        return new Matrix_Sub1();
    }

    @OriginalMember(owner = "client!am", name = "h", descriptor = "(Z)V")
    protected abstract void method8039();

    @OriginalMember(owner = "client!am", name = "g", descriptor = "(Z)Lclient!cw;")
    public final Matrix_Sub1 method8040() {
        return this.aClass73_Sub1_15;
    }

    @OriginalMember(owner = "client!am", name = "z", descriptor = "()Z")
    @Override
    public final boolean method7990() {
        return this.aClass26Array3[3].isSupported();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Ljava/awt/Canvas;Ljava/lang/Object;I)V")
    protected abstract void method8041(@OriginalArg(0) Canvas arg0, @OriginalArg(1) Object arg1);

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(B)V")
    public final void method8042() {
        this.aBoolean676 = false;
        this.method8091();
    }

    @OriginalMember(owner = "client!am", name = "DA", descriptor = "(IIII)V")
    @Override
    public final void DA(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height) {
        this.anInt9163 = y;
        this.anInt9151 = width;
        this.anInt9180 = height;
        this.anInt9170 = x;
        this.method8135();
        this.method8062();
        this.method8152();
        this.method8151();
    }

    @OriginalMember(owner = "client!am", name = "L", descriptor = "(I)V")
    public final void method8044() {
        if (this.anInt9166 == 8) {
            return;
        }
        this.method8144();
        this.method8147(true);
        this.method8123(true);
        this.method8083(true);
        this.method8053(1);
        this.anInt9166 = 8;
    }

    @OriginalMember(owner = "client!am", name = "v", descriptor = "(B)V")
    protected abstract void method8045();

    @OriginalMember(owner = "client!am", name = "c", descriptor = "(II)I")
    @Override
    public final int combineFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        return maskB | maskA;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(BF)V")
    public final void method8046(@OriginalArg(1) float arg0) {
        if (this.aFloat194 != arg0) {
            this.aFloat194 = arg0;
            this.method8135();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IILclient!fma;)V")
    public void method8047(@OriginalArg(1) int arg0, @OriginalArg(2) Class131 arg1) {
        this.method8130(0, this.anInterface16_13);
        this.method8114(this.aClass244_19);
        this.method8079(arg1, 0, arg0);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIZ)Lclient!st;")
    @Override
    public final Sprite createSprite(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        @Pc(11) Sprite_Sub3 local11 = new Sprite_Sub3(this, arg2, arg3, arg4);
        local11.copyRect(0, 0, arg2, arg3, arg0, arg1);
        return local11;
    }

    @OriginalMember(owner = "client!am", name = "M", descriptor = "(I)V")
    protected void method8049() {
        this.anInt9176 = this.anInt9177;
        this.anInt9177 = 0;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!lk;I)V")
    @Override
    public final void renderOrtho(@OriginalArg(0) ParticleList particleList, @OriginalArg(1) int zoom) {
        this.particleRenderer.render(particleList, zoom, this);
    }

    @OriginalMember(owner = "client!am", name = "k", descriptor = "()Z")
    @Override
    public final boolean method7949() {
        return true;
    }

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(BZ)V")
    public final void method8050(@OriginalArg(0) byte arg0) {
        this.method8112(arg0 | arg0 << 24 | arg0 << 16 | arg0 << 8);
    }

    @OriginalMember(owner = "client!am", name = "u", descriptor = "()V")
    @Override
    protected void stop() {
        if (this.aBoolean687) {
            return;
        }
        for (@Pc(9) Node local9 = this.aDeque_59.first(); local9 != null; local9 = this.aDeque_59.next()) {
            ((NativeMemoryPool) local9).deallocate();
        }
        @Pc(28) Enumeration local28 = this.aHashtable6.keys();
        while (local28.hasMoreElements()) {
            @Pc(38) Canvas local38 = (Canvas) local28.nextElement();
            this.method8051(local38, this.aHashtable6.get(local38));
        }
        ColourUtils.destroy(false, true);
        this.aNativeInterface3.release();
        this.aBoolean687 = true;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ILjava/awt/Canvas;Ljava/lang/Object;)V")
    protected abstract void method8051(@OriginalArg(1) Canvas arg0, @OriginalArg(2) Object arg1);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IBILclient!ri;ILclient!fma;I)V")
    public abstract void method8052(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) Interface20 arg2, @OriginalArg(4) int arg3, @OriginalArg(5) Class131 arg4, @OriginalArg(6) int arg5);

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(BI)V")
    public final void method8053(@OriginalArg(1) int arg0) {
        if (this.anInt9171 == arg0) {
            return;
        }
        @Pc(24) Class38 local24;
        @Pc(28) boolean local28;
        @Pc(26) boolean local26;
        if (arg0 == 1) {
            local26 = true;
            local24 = Static80.aClass38_2;
            local28 = true;
        } else if (arg0 == 2) {
            local24 = Static217.aClass38_3;
            local26 = false;
            local28 = true;
        } else if (arg0 == 128) {
            local26 = true;
            local24 = Static355.aClass38_5;
            local28 = true;
        } else {
            local24 = Static427.aClass38_6;
            local26 = false;
            local28 = false;
        }
        if (this.aBoolean679 != local26) {
            this.aBoolean679 = local26;
            this.method8060();
        }
        if (local28 != this.aBoolean689) {
            this.aBoolean689 = local28;
            this.method8045();
        }
        if (local24 != this.aClass38_7) {
            this.aClass38_7 = local24;
            this.method8035();
        }
        this.anInt9171 = arg0;
        this.anInt9166 &= 0xFFFFFFE3;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(BI)V")
    public final void method8054(@OriginalArg(1) int arg0) {
        if (arg0 == 1) {
            this.method8094(Static185.aClass121_3, Static185.aClass121_3);
        } else if (arg0 == 0) {
            this.method8094(Static209.aClass121_4, Static209.aClass121_4);
        } else if (arg0 == 2) {
            this.method8094(Static185.aClass121_3, Static725.aClass121_6);
        } else if (arg0 == 3) {
            this.method8094(Static209.aClass121_4, Static438.aClass121_5);
        } else if (arg0 == 4) {
            this.method8094(Static329.aClass121_2, Static329.aClass121_2);
        }
    }

    @OriginalMember(owner = "client!am", name = "n", descriptor = "()Lclient!tt;")
    @Override
    public final Matrix camera() {
        return this.aClass73_Sub1_16;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!pu;)V")
    @Override
    public final void method7973(@OriginalArg(0) Class67 arg0) {
        this.aClass67_Sub2_3 = (Class67_Sub2) arg0;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IB)V")
    public abstract void method8055(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!am", name = "s", descriptor = "(B)V")
    public void method8056() {
        this.anInterface16_15 = this.method8156(false);
        this.anInterface16_15.method3153(12, 3096);
        for (@Pc(18) int local18 = 0; local18 < 4; local18++) {
            @Pc(25) Buffer local25 = this.anInterface16_15.method3155();
            if (local25 != null) {
                @Pc(32) Stream local32 = this.method8100(local25);
                local32.a(0.0F);
                local32.a(0.0F);
                local32.a(0.0F);
                for (@Pc(43) int local43 = 0; local43 <= 256; local43++) {
                    @Pc(53) double local53 = (double) (local43 * 2) * 3.141592653589793D / 256.0D;
                    @Pc(57) float local57 = (float) Math.cos(local53);
                    @Pc(61) float local61 = (float) Math.sin(local53);
                    if (Stream.b()) {
                        local32.a(local61);
                        local32.a(local57);
                        local32.a(0.0F);
                    } else {
                        local32.b(local61);
                        local32.b(local57);
                        local32.b(0.0F);
                    }
                }
                local32.c();
                if (this.anInterface16_15.method3154()) {
                    break;
                }
            }
        }
        this.aClass244_18 = this.method8148(new Class237[]{new Class237(Static231.aClass157_1)});
    }

    @OriginalMember(owner = "client!am", name = "P", descriptor = "(I)V")
    public void method8057() {
        if (this.aClass77_6 == Static690.aClass77_7) {
            return;
        }
        @Pc(10) Class77 local10 = this.aClass77_6;
        this.aClass77_6 = Static690.aClass77_7;
        if (!local10.method2049()) {
            this.method8081();
        }
        this.method8029();
        this.aFloatArray63 = this.aFloatArray65;
        this.method8126();
        this.anInt9166 &= 0xFFFFFFF8;
    }

    @OriginalMember(owner = "client!am", name = "u", descriptor = "(I)V")
    protected abstract void method8058();

    @OriginalMember(owner = "client!am", name = "e", descriptor = "()I")
    @Override
    public final int getMaxLights() {
        return this.anInt9182 - 2;
    }

    @OriginalMember(owner = "client!am", name = "XA", descriptor = "()I")
    @Override
    public final int XA() {
        return this.anInt9167;
    }

    @OriginalMember(owner = "client!am", name = "j", descriptor = "(B)V")
    public final void method8059() {
        this.method8047(2, Static694.aClass131_6);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!za;)V")
    @Override
    public final void method7938(@OriginalArg(0) MemoryPool arg0) {
        this.aNativeHeap5 = ((NativeMemoryPool) arg0).heap;
        this.aNativeHeapBuffer6 = this.aNativeHeap5.a(32768, false);
    }

    @OriginalMember(owner = "client!am", name = "r", descriptor = "(IIIIIII)I")
    @Override
    public final int r(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        @Pc(7) int local7 = 0;
        @Pc(32) float local32 = this.aClass73_Sub1_16.aFloat43 + (this.aClass73_Sub1_16.aFloat47 * (float) arg2 + (this.aClass73_Sub1_16.aFloat36 * (float) arg1 + (float) arg0 * this.aClass73_Sub1_16.aFloat45));
        @Pc(57) float local57 = this.aClass73_Sub1_16.aFloat43 + ((float) arg5 * this.aClass73_Sub1_16.aFloat47 + (this.aClass73_Sub1_16.aFloat45 * (float) arg3 + (float) arg4 * this.aClass73_Sub1_16.aFloat36));
        if ((float) this.anInt9181 > local32 && (float) this.anInt9181 > local57) {
            local7 |= 0x10;
        } else if ((float) this.anInt9167 < local32 && (float) this.anInt9167 < local57) {
            local7 |= 0x20;
        }
        @Pc(130) int local130 = (int) ((this.aClass73_Sub1_16.aFloat39 * (float) arg0 + (float) arg1 * this.aClass73_Sub1_16.aFloat40 + this.aClass73_Sub1_16.aFloat38 * (float) arg2 + this.aClass73_Sub1_16.aFloat46) * (float) this.anInt9151 / (float) arg6);
        @Pc(163) int local163 = (int) ((float) this.anInt9151 * (this.aClass73_Sub1_16.aFloat46 + (this.aClass73_Sub1_16.aFloat39 * (float) arg3 + (float) arg4 * this.aClass73_Sub1_16.aFloat40 + this.aClass73_Sub1_16.aFloat38 * (float) arg5)) / (float) arg6);
        if ((float) local130 < this.aFloat185 && this.aFloat185 > (float) local163) {
            local7 |= 0x1;
        } else if ((float) local130 > this.aFloat190 && (float) local163 > this.aFloat190) {
            local7 |= 0x2;
        }
        @Pc(236) int local236 = (int) ((float) this.anInt9180 * (this.aClass73_Sub1_16.aFloat42 * (float) arg1 + this.aClass73_Sub1_16.aFloat44 * (float) arg0 + (float) arg2 * this.aClass73_Sub1_16.aFloat41 + this.aClass73_Sub1_16.aFloat37) / (float) arg6);
        @Pc(269) int local269 = (int) ((this.aClass73_Sub1_16.aFloat41 * (float) arg5 + ((float) arg3 * this.aClass73_Sub1_16.aFloat44 + this.aClass73_Sub1_16.aFloat42 * (float) arg4) + this.aClass73_Sub1_16.aFloat37) * (float) this.anInt9180 / (float) arg6);
        if (this.aFloat183 > (float) local236 && (float) local269 < this.aFloat183) {
            local7 |= 0x4;
        } else if ((float) local236 > this.aFloat188 && this.aFloat188 < (float) local269) {
            local7 |= 0x8;
        }
        return local7;
    }

    @OriginalMember(owner = "client!am", name = "B", descriptor = "(I)V")
    protected abstract void method8060();

    @OriginalMember(owner = "client!am", name = "A", descriptor = "(I)V")
    protected final void method8061() {
        @Pc(9) Enumeration local9 = this.aHashtable6.keys();
        while (local9.hasMoreElements()) {
            @Pc(17) Canvas local17 = (Canvas) local9.nextElement();
            this.method8051(local17, this.aHashtable6.get(local17));
        }
        this.anInterface16_13.method8538();
        this.anInterface16_14.method8538();
        this.anInterface16_15.method8538();
        for (@Pc(46) int local46 = 0; local46 < 7; local46++) {
            this.aClass114_Sub1Array5[local46].method2550();
        }
        this.particleRenderer.release();
        this.anInterface20_6.method8538();
    }

    @OriginalMember(owner = "client!am", name = "R", descriptor = "(I)V")
    public void method8062() {
        this.aBoolean693 = false;
        this.method8032();
        if (this.aClass77_6 == Static517.aClass77_5) {
            this.method8126();
        }
    }

    @OriginalMember(owner = "client!am", name = "L", descriptor = "(III)V")
    @Override
    public final void L(@OriginalArg(0) int colour, @OriginalArg(1) int range, @OriginalArg(2) int offset) {
        if (this.anInt9146 == colour && this.anInt9175 == range && offset == this.anInt9185) {
            return;
        }
        this.anInt9146 = colour;
        this.anInt9175 = range;
        this.anInt9185 = offset;
        this.method8093();
        this.method8084();
    }

    @OriginalMember(owner = "client!am", name = "EA", descriptor = "(IIII)V")
    @Override
    public final void EA(@OriginalArg(0) int height, @OriginalArg(1) int colour, @OriginalArg(2) int depth, @OriginalArg(3) int bias) {
        if (!this.aBoolean681) {
            throw new RuntimeException("");
        }
        this.anInt9156 = colour;
        this.anInt9150 = depth;
        this.anInt9160 = height;
        this.anInt9169 = bias;
        if (this.aBoolean694) {
            this.aClass26Array3[3].onUnderwaterSettingsChanged();
            this.aClass26Array3[3].onFogChanged();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(I[[IZI)Lclient!fv;")
    public abstract Interface8 method8063(@OriginalArg(0) int arg0, @OriginalArg(1) int[][] arg1, @OriginalArg(2) boolean arg2);

    @OriginalMember(owner = "client!am", name = "y", descriptor = "(I)V")
    public final void method8064() {
        if (this.anInt9166 == 4) {
            return;
        }
        this.method8107();
        this.method8147(false);
        this.method8072(false);
        this.method8123(false);
        this.method8083(false);
        this.method8108(false, -2, false);
        this.method8053(1);
        this.method8054(0);
        this.anInt9166 = 4;
    }

    @OriginalMember(owner = "client!am", name = "l", descriptor = "(Z)V")
    protected abstract void method8065();

    @OriginalMember(owner = "client!am", name = "w", descriptor = "(B)Lclient!cw;")
    public final Matrix_Sub1 method8066() {
        return this.aClass73_Sub1Array3[this.anInt9148];
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!eba;Lclient!wda;III)Lclient!og;")
    public abstract Interface18 method8067(@OriginalArg(0) Class92 arg0, @OriginalArg(1) Class397 arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!am", name = "U", descriptor = "(IIIII)V")
    @Override
    public final void U(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int strength, @OriginalArg(3) int colour, @OriginalArg(4) int arg4) {
        this.line(x, y, x + strength, y, colour, arg4);
    }

    @OriginalMember(owner = "client!am", name = "l", descriptor = "(B)Lclient!cw;")
    public final Matrix_Sub1 method8068() {
        return this.aClass73_Sub1_19;
    }

    @OriginalMember(owner = "client!am", name = "S", descriptor = "(I)V")
    protected abstract void method8069();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(I)Lclient!za;")
    @Override
    public final MemoryPool createHeap(@OriginalArg(0) int size) {
        @Pc(8) NativeMemoryPool local8 = new NativeMemoryPool(size);
        this.aDeque_59.addLast(local8);
        return local8;
    }

    @OriginalMember(owner = "client!am", name = "r", descriptor = "()Z")
    @Override
    public final boolean hardShadow() {
        return true;
    }

    @OriginalMember(owner = "client!am", name = "A", descriptor = "(B)V")
    public void method8070() {
        this.aBoolean688 = false;
        if (this.aClass77_6 == Static103.aClass77_2) {
            this.method8036();
            this.method8126();
        }
    }

    @OriginalMember(owner = "client!am", name = "aa", descriptor = "(IIIIII)V")
    @Override
    public final void aa(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        this.method8149();
        this.method8112(colour);
        this.method8080(0, Static454.aClass168_5);
        this.method8142(Static454.aClass168_5, 0);
        this.method8053(mode);
        this.aClass73_Sub1_15.method1884(1.0F, (float) height, (float) width);
        this.aClass73_Sub1_15.translate(x, y, 0);
        this.method8042();
        this.method8141(false);
        this.method8059();
        this.method8141(true);
        this.method8142(Static189.aClass168_2, 0);
        this.method8080(0, Static189.aClass168_2);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!eba;Lclient!wda;I)Z")
    public abstract boolean method8071(@OriginalArg(0) Class92 arg0, @OriginalArg(1) Class397 arg1);

    @OriginalMember(owner = "client!am", name = "f", descriptor = "(IZ)V")
    public final void method8072(@OriginalArg(1) boolean arg0) {
        if (this.aBoolean691 != arg0) {
            this.aBoolean691 = arg0;
            this.method8078();
            this.anInt9166 &= 0xFFFFFFF8;
        }
    }

    @OriginalMember(owner = "client!am", name = "f", descriptor = "(II)V")
    @Override
    public final void f(@OriginalArg(0) int near, @OriginalArg(1) int far) {
        if (near == this.anInt9181 && far == this.anInt9167) {
            return;
        }
        this.anInt9167 = far;
        this.anInt9181 = near;
        this.method8062();
        this.method8135();
        this.method8093();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ILclient!uf;)V")
    public final void method8073(@OriginalArg(1) Class370 arg0) {
        this.aClass370Array3[this.anInt9148] = arg0;
        this.method8033();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!wp;Z)Lclient!st;")
    @Override
    public final Sprite createSprite(@OriginalArg(0) IndexedImage arg0, @OriginalArg(1) boolean arg1) {
        @Pc(141) Sprite local141;
        if (arg0.width == 0 || arg0.height == 0) {
            local141 = this.createSprite(1, 1, 1, new int[1]);
        } else {
            @Pc(23) int[] local23 = new int[arg0.width * arg0.height];
            @Pc(25) int local25 = 0;
            @Pc(27) int local27 = 0;
            @Pc(32) int local32;
            @Pc(36) int local36;
            if (arg0.alpha == null) {
                for (local32 = 0; local32 < arg0.height; local32++) {
                    for (local36 = 0; local36 < arg0.width; local36++) {
                        @Pc(98) int local98 = arg0.palette[arg0.raster[local25++] & 0xFF];
                        local23[local27++] = local98 == 0 ? 0 : local98 | 0xFF000000;
                    }
                }
            } else {
                for (local32 = 0; local32 < arg0.height; local32++) {
                    for (local36 = 0; local36 < arg0.width; local36++) {
                        local23[local27++] = arg0.palette[arg0.raster[local25] & 0xFF] | arg0.alpha[local25] << 24;
                        local25++;
                    }
                }
            }
            local141 = this.createSprite(arg0.width, arg0.width, arg0.height, local23);
        }
        local141.setOffsets(arg0.offX1, arg0.offY1, arg0.offX2, arg0.offY2);
        return local141;
    }

    @OriginalMember(owner = "client!am", name = "g", descriptor = "(II)Lclient!ri;")
    public final Interface20 method8074(@OriginalArg(1) int arg0) {
        if (arg0 * 2 > this.anInterface20_6.method8536()) {
            this.anInterface20_6.method8543(arg0);
        }
        return this.anInterface20_6;
    }

    @OriginalMember(owner = "client!am", name = "J", descriptor = "(I)V")
    public void method8075() {
        if (this.aCanvas12 == null) {
            this.anInt8991 = this.anInt9021 = 1;
        } else {
            @Pc(14) Dimension local14 = this.aCanvas12.getSize();
            this.anInt9021 = local14.height;
            this.anInt8991 = local14.width;
        }
        this.anInt9013 = this.anInt9021;
        this.anInt9122 = this.anInt8991;
        this.method8070();
        this.method8062();
        this.method8135();
        this.method8037();
        this.method8151();
        this.method8152();
        this.la();
    }

    @OriginalMember(owner = "client!am", name = "N", descriptor = "(I)V")
    protected abstract void method8076();

    @OriginalMember(owner = "client!am", name = "O", descriptor = "(I)V")
    public void method8077() {
        this.anInterface16_13 = this.method8156(false);
        this.anInterface16_13.method3153(28, 140);
        for (@Pc(18) int local18 = 0; local18 < 4; local18++) {
            @Pc(25) Buffer local25 = this.anInterface16_13.method3155();
            if (local25 != null) {
                @Pc(32) Stream local32 = this.method8100(local25);
                if (Stream.b()) {
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(1.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(1.0F);
                    local32.a(0.0F);
                    local32.a(1.0F);
                    local32.a(1.0F);
                    local32.a(1.0F);
                    local32.a(0.0F);
                    local32.a(1.0F);
                    local32.a(1.0F);
                    local32.a(1.0F);
                    local32.a(1.0F);
                    local32.a(1.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(1.0F);
                    local32.a(0.0F);
                    local32.a(1.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                    local32.a(0.0F);
                } else {
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(1.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(1.0F);
                    local32.b(0.0F);
                    local32.b(1.0F);
                    local32.b(1.0F);
                    local32.b(1.0F);
                    local32.b(0.0F);
                    local32.b(1.0F);
                    local32.b(1.0F);
                    local32.b(1.0F);
                    local32.b(1.0F);
                    local32.b(1.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(1.0F);
                    local32.b(0.0F);
                    local32.b(1.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                    local32.b(0.0F);
                }
                local32.c();
                if (this.anInterface16_13.method3154()) {
                    break;
                }
            }
        }
        this.aClass244_19 = this.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_5, Static231.aClass157_5})});
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public final void resizeCanvas(@OriginalArg(0) Canvas arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(5) Object local5 = null;
        if (arg0 == null || this.aCanvas13 == arg0) {
            local5 = this.anObject15;
        } else if (this.aHashtable6.containsKey(arg0)) {
            local5 = this.aHashtable6.get(arg0);
        }
        if (local5 == null) {
            throw new RuntimeException();
        }
        this.method8041(arg0, local5);
        if (this.aCanvas12 == arg0) {
            this.method8075();
        }
    }

    @OriginalMember(owner = "client!am", name = "s", descriptor = "(I)V")
    protected abstract void method8078();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!fma;III)V")
    public abstract void method8079(@OriginalArg(0) Class131 arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2);

    @OriginalMember(owner = "client!am", name = "C", descriptor = "(Z)V")
    @Override
    public final void C(@OriginalArg(0) boolean zWrite) {
        this.aBoolean677 = zWrite;
        this.method8146();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IILclient!hla;)V")
    public final void method8080(@OriginalArg(0) int arg0, @OriginalArg(2) Class168 arg1) {
        this.method8125(arg1, false, false, arg0);
    }

    @OriginalMember(owner = "client!am", name = "j", descriptor = "(Z)V")
    public void method8081() {
        this.aBoolean682 = false;
        if (this.aClass26_3 != null) {
            this.aClass26_3.onCameraChanged();
        }
        this.method8069();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public final void setCanvas(@OriginalArg(0) Canvas arg0) {
        this.anObject15 = null;
        this.aCanvas12 = null;
        if (arg0 == null || this.aCanvas13 == arg0) {
            this.aCanvas12 = this.aCanvas13;
            this.anObject15 = this.anObject16;
        } else if (this.aHashtable6.containsKey(arg0)) {
            this.anObject15 = this.aHashtable6.get(arg0);
            this.aCanvas12 = arg0;
        }
        if (this.aCanvas12 == null || this.anObject15 == null) {
            throw new RuntimeException();
        }
        this.method8095(this.anObject15, this.aCanvas12);
        this.method8075();
    }

    @OriginalMember(owner = "client!am", name = "E", descriptor = "(I)Lclient!cw;")
    public final Matrix_Sub1 method8082() {
        return this.aClass73_Sub1Array3[this.anInt9148];
    }

    @OriginalMember(owner = "client!am", name = "A", descriptor = "()Lclient!tt;")
    @Override
    public final Matrix scratchMatrix() {
        return this.aClass73_Sub1_21;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public final void setCamera(@OriginalArg(0) Matrix matrix) {
        this.aClass73_Sub1_16 = (Matrix_Sub1) matrix;
        this.aClass73_Sub1_18.apply(this.aClass73_Sub1_16);
        this.aClass73_Sub1_18.method1895();
        this.aClass73_Sub1_19.method1880(this.aClass73_Sub1_18);
        this.aClass73_Sub1_17.method1880(this.aClass73_Sub1_16);
        if (this.aClass77_6.method2049()) {
            this.method8081();
        }
    }

    @OriginalMember(owner = "client!am", name = "e", descriptor = "(IZ)V")
    public final void method8083(@OriginalArg(1) boolean arg0) {
        if (this.aBoolean686 != arg0) {
            this.aBoolean686 = arg0;
            this.method8146();
            this.anInt9166 &= 0xFFFFFFE0;
        }
    }

    @OriginalMember(owner = "client!am", name = "c", descriptor = "(B)V")
    protected abstract void method8084();

    @OriginalMember(owner = "client!am", name = "P", descriptor = "(IIIII)V")
    @Override
    public final void P(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int strength, @OriginalArg(3) int colour, @OriginalArg(4) int mode) {
        this.line(x, y, x, strength + y, colour, mode);
    }

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(I)V")
    @Override
    public final void method8003() {
    }

    @OriginalMember(owner = "client!am", name = "Y", descriptor = "(I)V")
    protected abstract void method8085();

    @OriginalMember(owner = "client!am", name = "k", descriptor = "(B)V")
    protected abstract void method8086();

    @OriginalMember(owner = "client!am", name = "i", descriptor = "(II)Lclient!rea;")
    protected RenderPass method8087(@OriginalArg(1) int arg0) {
        if (arg0 == 6) {
            return new UnlitPass(this);
        } else if (arg0 == 1) {
            return new FixedFunctionSpecularPass(this);
        } else if (arg0 == 2) {
            return new FixedFunctionWaterPass(this, this.aClass7_8);
        } else if (arg0 == 7) {
            return new FixedFunctionEnvironmentMapPass(this);
        } else {
            return new NoEffectPass(this);
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(BLclient!mw;)V")
    public final void method8088(@OriginalArg(1) Interface17 arg0) {
        if (arg0 == this.anInterface17Array3[this.anInt9148]) {
            return;
        }
        this.anInterface17Array3[this.anInt9148] = arg0;
        if (arg0 == null) {
            this.method8027();
        } else {
            arg0.method9043();
        }
        this.anInt9166 &= 0xFFFFFFFE;
    }

    @OriginalMember(owner = "client!am", name = "m", descriptor = "(B)V")
    protected abstract void method8089();

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public final void releaseSurface(@OriginalArg(0) Canvas canvas) {
        if (this.aCanvas13 == canvas) {
            throw new RuntimeException();
        } else if (this.aHashtable6.containsKey(canvas)) {
            this.method8051(canvas, this.aHashtable6.get(canvas));
            this.aHashtable6.remove(canvas);
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IZBIIZ)V")
    public void method8090(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) boolean arg4) {
        @Pc(4) boolean local4 = arg1 & this.method7990();
        if (!local4 && (arg0 == 4 || arg0 == 8 || arg0 == 9)) {
            arg0 = 2;
            arg3 = 1;
            arg2 = 0;
        }
        if (arg0 != 0 && arg4) {
            arg0 |= Integer.MIN_VALUE;
        }
        if (arg0 != this.anInt9174) {
            if (this.anInt9174 != 0) {
                this.aClass26Array3[Integer.MAX_VALUE & this.anInt9174].disable();
            }
            if (arg0 == 0) {
                this.aClass26_3 = null;
            } else {
                this.aClass26_3 = this.aClass26Array3[arg0 & Integer.MAX_VALUE];
                this.aClass26_3.enable(arg4);
                this.aClass26_3.applyTextureCombine(arg4);
                this.aClass26_3.setEffectParams(arg2, arg3);
            }
            this.anInt9172 = arg2;
            this.anInt9152 = arg3;
            this.anInt9174 = arg0;
        } else if (this.anInt9174 != 0) {
            this.aClass26Array3[this.anInt9174 & Integer.MAX_VALUE].applyTextureCombine(arg4);
            if (this.anInt9172 != arg2 || this.anInt9152 != arg3) {
                this.aClass26Array3[Integer.MAX_VALUE & this.anInt9174].setEffectParams(arg2, arg3);
                this.anInt9152 = arg3;
                this.anInt9172 = arg2;
            }
        }
    }

    @OriginalMember(owner = "client!am", name = "j", descriptor = "(I)V")
    @Override
    public final void allocateThreads(@OriginalArg(0) int arg0) {
        if (arg0 != 1) {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!am", name = "e", descriptor = "(B)V")
    public void method8091() {
        if (Static103.aClass77_2 == this.aClass77_6) {
            @Pc(24) float local24 = this.method8155();
            this.aClass73_Sub1_15.method1882(0.0F, local24, local24);
        }
        this.aBoolean682 = false;
        this.method8136();
        if (this.aClass26_3 != null) {
            this.aClass26_3.onModelMatrixChanged();
        }
    }

    @OriginalMember(owner = "client!am", name = "KA", descriptor = "(IIII)V")
    @Override
    public final void KA(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2) {
        if (x1 <= 0 && this.anInt9122 - 1 <= x2 && y1 <= 0 && this.anInt9013 - 1 <= y2) {
            this.la();
            return;
        }
        this.anInt9153 = this.anInt9122 >= x2 ? x2 : 0;
        this.anInt9154 = x1 < 0 ? 0 : x1;
        this.anInt9161 = y1 < 0 ? 0 : y1;
        this.anInt9165 = this.anInt9122 < y2 ? 0 : y2;
        if (!this.aBoolean697) {
            this.aBoolean697 = true;
            this.method8058();
        }
        this.method8076();
        this.method8151();
    }

    @OriginalMember(owner = "client!am", name = "r", descriptor = "(I)I")
    public final int method8092() {
        return this.anInt9160;
    }

    @OriginalMember(owner = "client!am", name = "y", descriptor = "(B)V")
    public void method8093() {
        if (this.aClass26_3 != null) {
            this.aClass26_3.onFogChanged();
        }
        this.method8150();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ILclient!fba;Lclient!fba;)V")
    public final void method8094(@OriginalArg(1) Class121 arg0, @OriginalArg(2) Class121 arg1) {
        @Pc(13) boolean local13 = false;
        if (this.aClass121Array6[this.anInt9148] != arg1) {
            this.aClass121Array6[this.anInt9148] = arg1;
            this.method8039();
            local13 = true;
        }
        if (this.aClass121Array5[this.anInt9148] != arg0) {
            this.aClass121Array5[this.anInt9148] = arg0;
            this.method8030();
            local13 = true;
        }
        if (local13) {
            this.anInt9166 &= 0xFFFFFFE2;
        }
    }

    @OriginalMember(owner = "client!am", name = "ra", descriptor = "(IIII)V")
    @Override
    public final void ra(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        this.aBoolean681 = true;
        this.anInt9169 = arg3;
        this.anInt9150 = arg2;
        this.anInt9160 = arg0;
        this.anInt9156 = arg1;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Ljava/lang/Object;Ljava/awt/Canvas;I)V")
    protected abstract void method8095(@OriginalArg(0) Object arg0, @OriginalArg(1) Canvas arg1);

    @OriginalMember(owner = "client!am", name = "o", descriptor = "()Z")
    @Override
    public final boolean supportsBloom() {
        return false;
    }

    @OriginalMember(owner = "client!am", name = "p", descriptor = "(B)V")
    public final void method8096() {
        this.aClass73_Sub1_15.makeIdentity();
        this.aBoolean676 = true;
        this.method8091();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIIII)V")
    @Override
    public final void strongLine(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int width, @OriginalArg(6) int mode) {
        @Pc(10) float local10 = (float) -x1 + (float) x2;
        @Pc(16) float local16 = (float) y2 - (float) y1;
        @Pc(18) float local18 = 0.0F;
        @Pc(20) float local20 = 1.0F;
        if (local10 != 0.0F || local16 != 0.0F) {
            local20 = (float) Math.sqrt(local16 * local16 + local10 * local10);
            local18 = (float) Math.atan2(local16, local10);
        }
        this.method8149();
        this.method8112(colour);
        this.method8080(0, Static454.aClass168_5);
        this.method8142(Static454.aClass168_5, 0);
        this.method8053(mode);
        this.aClass73_Sub1_15.method1884(1.0F, (float) width, local20);
        this.aClass73_Sub1_15.translate(0, -width / 2, 0);
        this.aClass73_Sub1_15.rotateAxisZ((int) ((double) local18 * 2607.5945876176133D) & 0x3FFF);
        this.aClass73_Sub1_15.translate(x1, y1, 0);
        this.method8042();
        this.method8141(false);
        this.method8059();
        this.method8141(true);
        this.method8142(Static189.aClass168_2, 0);
        this.method8080(0, Static189.aClass168_2);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(I[Lclient!lca;)V")
    @Override
    public final void method8009(@OriginalArg(0) int arg0, @OriginalArg(1) PointLight[] arg1) {
        for (@Pc(7) int local7 = 0; local7 < arg0; local7++) {
            this.aClass2_Sub7Array6[local7] = arg1[local7];
        }
        this.anInt9177 = arg0;
        if (this.aClass77_6.method2049()) {
            this.method8049();
        }
    }

    @OriginalMember(owner = "client!am", name = "xa", descriptor = "(F)V")
    @Override
    public final void xa(@OriginalArg(0) float globalAmbient) {
        if (this.aFloat186 != globalAmbient) {
            this.aFloat186 = globalAmbient;
            this.aNativeInterface3.setAmbient(globalAmbient);
            this.method8129();
            this.method8109();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(BLclient!hw;)V")
    public abstract void method8097(@OriginalArg(1) Class172 arg0);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIII)V")
    @Override
    public final void line(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        @Pc(10) float local10 = (float) -x1 + (float) x2;
        @Pc(16) float local16 = (float) y2 - (float) y1;
        if (local10 == 0.0F && local16 == 0.0F) {
            local10 = 1.0F;
        } else {
            @Pc(43) float local43 = (float) (1.0D / Math.sqrt(local10 * local10 + local16 * local16));
            local10 *= local43;
            local16 *= local43;
        }
        if (!this.method8157(local10 + (float) x2, 0.0F, (float) x1, (float) y2 + local16, (float) y1, 0.0F)) {
            return;
        }
        this.method8149();
        this.method8112(colour);
        this.method8080(0, Static454.aClass168_5);
        this.method8142(Static454.aClass168_5, 0);
        this.method8053(mode);
        this.method8096();
        this.method8141(false);
        this.method8124();
        this.method8141(true);
        this.method8142(Static189.aClass168_2, 0);
        this.method8080(0, Static189.aClass168_2);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!eba;ZIIB[FII)Lclient!og;")
    protected abstract Interface18 method8098(@OriginalArg(0) Class92 arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) int arg2, @OriginalArg(5) float[] arg3, @OriginalArg(7) int arg4);

    @OriginalMember(owner = "client!am", name = "i", descriptor = "(B)V")
    public final void method8099() {
        if (this.anInt9166 == 2) {
            return;
        }
        this.method8107();
        this.method8147(false);
        this.method8072(false);
        this.method8123(false);
        this.method8083(false);
        this.method8108(false, -2, false);
        this.anInt9166 = 2;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ZLclient!jaclib/memory/Buffer;)Lclient!jaclib/memory/Stream;")
    public final Stream method8100(@OriginalArg(1) Buffer arg0) {
        this.aStream6.a(arg0);
        return this.aStream6;
    }

    @OriginalMember(owner = "client!am", name = "H", descriptor = "(I)V")
    protected final void method8101() {
        @Pc(7) Hashtable local7 = new Hashtable();
        if (this.aHashtable6 != null && !this.aHashtable6.isEmpty()) {
            @Pc(20) Enumeration local20 = this.aHashtable6.keys();
            while (local20.hasMoreElements()) {
                @Pc(25) Canvas local25 = (Canvas) local20.nextElement();
                local7.put(local25, this.method8127(local25));
            }
        }
        this.aHashtable6 = local7;
        this.method8077();
        this.method8131();
        this.method8056();
        this.particleRenderer.createVertexBuffer(this);
    }

    @OriginalMember(owner = "client!am", name = "s", descriptor = "()Z")
    @Override
    public final boolean method7979() {
        return false;
    }

    @OriginalMember(owner = "client!am", name = "la", descriptor = "()V")
    @Override
    public final void la() {
        this.anInt9153 = this.anInt9122;
        this.anInt9154 = 0;
        this.anInt9161 = 0;
        this.anInt9165 = this.anInt9013;
        if (this.aBoolean697) {
            this.aBoolean697 = false;
            this.method8058();
        }
        this.method8151();
    }

    @OriginalMember(owner = "client!am", name = "i", descriptor = "(Z)V")
    protected void method8102() {
        this.method8023();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!eba;IZIIIZ[B)Lclient!og;")
    protected abstract Interface18 method8103(@OriginalArg(0) Class92 arg0, @OriginalArg(1) int arg1, @OriginalArg(2) boolean arg2, @OriginalArg(5) int arg3, @OriginalArg(7) byte[] arg4);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIIIIIIIIII)V")
    @Override
    public final void drawTriangle(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int z1, @OriginalArg(3) int x2, @OriginalArg(4) int y2, @OriginalArg(5) int z2, @OriginalArg(6) int x3, @OriginalArg(7) int y3, @OriginalArg(8) int z3, @OriginalArg(9) int c1, @OriginalArg(10) int c2, @OriginalArg(11) int c3, @OriginalArg(12) int mode) {
    }

    @OriginalMember(owner = "client!am", name = "d", descriptor = "(I)V")
    public void method8104() {
        this.aFloatArray66[14] = this.aFloat193;
        this.aFloatArray66[10] = this.aFloat182;
        this.aFloat187 = ((float) -this.anInt9167 + this.aFloatArray66[14]) / this.aFloatArray66[10];
    }

    @OriginalMember(owner = "client!am", name = "m", descriptor = "(I)I")
    public final int method8105() {
        return this.anInt9150;
    }

    @OriginalMember(owner = "client!am", name = "t", descriptor = "()Z")
    @Override
    public final boolean supportsAntiAliasing() {
        return this.aBoolean692;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ZII)Lclient!jaclib/memory/heap/NativeHeapBuffer;")
    public final NativeHeapBuffer method8106(@OriginalArg(0) boolean arg0, @OriginalArg(2) int arg1) {
        return this.aNativeHeap5.a(arg1, arg0);
    }

    @OriginalMember(owner = "client!am", name = "w", descriptor = "(I)V")
    public void method8107() {
        if (this.aClass77_6 == Static103.aClass77_2) {
            return;
        }
        @Pc(18) Class77 local18 = this.aClass77_6;
        this.aClass77_6 = Static103.aClass77_2;
        if (local18.method2049()) {
            this.method8081();
        }
        this.method8036();
        this.aFloatArray63 = this.aFloatArray64;
        this.method8126();
        this.anInt9166 &= 0xFFFFFFE7;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IZIZ)V")
    public final void method8108(@OriginalArg(1) boolean arg0, @OriginalArg(2) int arg1, @OriginalArg(3) boolean arg2) {
        if (this.anInt9179 != arg1 || this.aBoolean694 != this.aBoolean681) {
            @Pc(29) Interface18 local29 = null;
            @Pc(31) int local31 = 0;
            @Pc(33) byte local33 = 0;
            @Pc(35) int local35 = 0;
            @Pc(43) int local43 = this.aBoolean681 ? 3 : 0;
            if (arg1 < 0) {
                this.method8031();
            } else {
                local29 = this.aClass334_3.method7654(arg1);
                @Pc(60) TextureMetrics local60 = super.textureSource.getMetrics(arg1);
                if (local60.speedU == 0 && local60.speedV == 0) {
                    this.method8031();
                } else {
                    @Pc(87) int local87 = local60.small ? 64 : 128;
                    @Pc(91) int local91 = local87 * 50;
                    @Pc(95) Matrix_Sub1 local95 = this.method8082();
                    local95.method1877(0.0F, (float) (this.anInt9164 % local91 * local60.speedV) / (float) local91, (float) (local60.speedU * (this.anInt9164 % local91)) / (float) local91);
                    this.method8073(Static431.aClass370_5);
                }
                local31 = local60.colorOp;
                if (!this.aBoolean681) {
                    local33 = local60.effectParam1;
                    local43 = local60.effectType;
                    local35 = local60.effectParam2;
                }
            }
            this.method8090(local43, arg2, local33, local35, arg0);
            if (this.aClass26_3 == null) {
                this.method8088(local29);
                this.method8054(local31);
            } else {
                this.aClass26_3.bindTexture(local29, local31);
            }
            this.aBoolean694 = this.aBoolean681;
            this.anInt9179 = arg1;
        }
        this.anInt9166 &= 0xFFFFFFF8;
    }

    @OriginalMember(owner = "client!am", name = "d", descriptor = "(Z)V")
    protected abstract void method8109();

    @OriginalMember(owner = "client!am", name = "H", descriptor = "(III[I)V")
    @Override
    public final void H(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3) {
        @Pc(14) float local14 = this.aClass73_Sub1_16.method1889((float) arg2, (float) arg1, (float) arg0);
        @Pc(42) int local42;
        @Pc(60) int local60;
        if (local14 < -0.0078125F || local14 > 0.0078125F) {
            local42 = (int) ((float) this.anInt9151 * this.aClass73_Sub1_16.method1885((float) arg1, (float) arg2, (float) arg0) / local14);
            local60 = (int) ((float) this.anInt9180 * this.aClass73_Sub1_16.method1883((float) arg2, (float) arg0, (float) arg1) / local14);
        } else {
            local60 = this.anInt9163;
            local42 = this.anInt9170;
        }
        arg3[1] = (int) ((float) local60 - this.aFloat183);
        arg3[0] = (int) ((float) local42 - this.aFloat185);
        arg3[2] = (int) local14;
    }

    @OriginalMember(owner = "client!am", name = "I", descriptor = "(I)V")
    protected abstract void method8110();

    @OriginalMember(owner = "client!am", name = "f", descriptor = "(Z)V")
    public final void method8111() {
        if (this.anInt9166 == 16) {
            return;
        }
        this.method8057();
        this.method8147(true);
        this.method8123(true);
        this.method8083(true);
        this.method8053(1);
        this.anInt9166 = 16;
    }

    @OriginalMember(owner = "client!am", name = "d", descriptor = "(IIIIII)V")
    @Override
    public final void outlineRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        @Pc(7) float local7 = this.method8155();
        this.method8149();
        this.method8112(colour);
        this.method8080(0, Static454.aClass168_5);
        this.method8142(Static454.aClass168_5, 0);
        this.method8053(mode);
        this.aClass73_Sub1_15.method1884(1.0F, (float) (height - 1), (float) (width - 1));
        this.aClass73_Sub1_15.method1882(0.0F, (float) y - local7, (float) x - local7);
        this.method8042();
        this.method8141(false);
        this.method8047(4, Static495.aClass131_5);
        this.method8141(true);
        this.method8142(Static189.aClass168_2, 0);
        this.method8080(0, Static189.aClass168_2);
    }

    @OriginalMember(owner = "client!am", name = "j", descriptor = "(II)V")
    public final void method8112(@OriginalArg(1) int arg0) {
        if (arg0 != this.anInt9157) {
            this.anInt9157 = arg0;
            this.method8113();
        }
    }

    @OriginalMember(owner = "client!am", name = "p", descriptor = "(I)V")
    protected abstract void method8113();

    @OriginalMember(owner = "client!am", name = "X", descriptor = "(I)V")
    @Override
    public final void X(@OriginalArg(0) int arg0) {
        this.anInt9173 = 0;
        while (arg0 > 1) {
            this.anInt9173++;
            arg0 >>= 0x1;
        }
        this.anInt9159 = 0x1 << this.anInt9173;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!mk;I)V")
    public abstract void method8114(@OriginalArg(0) Class244 arg0);

    @OriginalMember(owner = "client!am", name = "A", descriptor = "(ILclient!aa;II)V")
    @Override
    public final void A(@OriginalArg(0) int colour, @OriginalArg(1) ClippingMask clippingMask, @OriginalArg(2) int x, @OriginalArg(3) int y) {
        @Pc(6) ClippingMask_Sub2 local6 = (ClippingMask_Sub2) clippingMask;
        @Pc(9) Interface18 local9 = local6.anInterface18_2;
        this.method8099();
        this.method8088(local9);
        this.method8053(1);
        this.method8094(Static185.aClass121_3, Static185.aClass121_3);
        this.method8080(0, Static454.aClass168_5);
        this.method8112(colour);
        this.aClass73_Sub1_15.method1884(0.0F, (float) this.anInt9013, (float) this.anInt9122);
        this.method8042();
        this.aClass73_Sub1Array3[0].method1884(1.0F, local9.method9050((float) this.anInt9013), local9.method9046((float) this.anInt9122));
        this.aClass73_Sub1Array3[0].method1882(0.0F, local9.method9050((float) -y), local9.method9046((float) -x));
        this.aClass370Array3[0] = Static431.aClass370_5;
        this.method8033();
        this.method8059();
        this.method8031();
        this.method8080(0, Static189.aClass168_2);
    }

    @OriginalMember(owner = "client!am", name = "T", descriptor = "(I)V")
    public final void method8115() {
        this.method8152();
        this.method8126();
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(B)V")
    protected abstract void method8116();

    @OriginalMember(owner = "client!am", name = "HA", descriptor = "(IIII[I)V")
    @Override
    public final void HA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int[] arg4) {
        @Pc(14) float local14 = this.aClass73_Sub1_16.method1889((float) arg2, (float) arg1, (float) arg0);
        if ((float) this.anInt9181 > local14 || local14 > (float) this.anInt9167) {
            arg4[0] = arg4[1] = arg4[2] = -1;
            return;
        }
        @Pc(61) int local61 = (int) ((float) this.anInt9151 * this.aClass73_Sub1_16.method1885((float) arg1, (float) arg2, (float) arg0) / (float) arg3);
        @Pc(80) int local80 = (int) ((float) this.anInt9180 * this.aClass73_Sub1_16.method1883((float) arg2, (float) arg0, (float) arg1) / (float) arg3);
        arg4[1] = (int) ((float) local80 - this.aFloat183);
        arg4[2] = (int) local14;
        arg4[0] = (int) ((float) local61 - this.aFloat185);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(BZ)V")
    public final void method8117(@OriginalArg(1) boolean arg0) {
        if (this.aBoolean684 != arg0) {
            this.aBoolean684 = arg0;
            this.method8089();
        }
    }

    @OriginalMember(owner = "client!am", name = "C", descriptor = "(I)Lclient!cw;")
    public final Matrix_Sub1 method8118() {
        return this.aClass73_Sub1_18;
    }

    @OriginalMember(owner = "client!am", name = "t", descriptor = "(I)I")
    public final int method8120() {
        return this.anInt9169;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(I[FLclient!eba;ZZI)Lclient!og;")
    public final Interface18 method8121(@OriginalArg(0) int arg0, @OriginalArg(1) float[] arg1, @OriginalArg(2) Class92 arg2, @OriginalArg(4) boolean arg3, @OriginalArg(5) int arg4) {
        return this.method8098(arg2, arg3, arg4, arg1, arg0);
    }

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public final void addCanvas(@OriginalArg(0) Canvas canvas, @OriginalArg(1) int width, @OriginalArg(2) int height) {
        if (canvas == this.aCanvas13) {
            throw new RuntimeException();
        } else if (!this.aHashtable6.containsKey(canvas)) {
            if (!canvas.isShowing()) {
                throw new RuntimeException();
            }
            try {
                @Pc(32) Class local32 = Class.forName("java.awt.Canvas");
                @Pc(44) Method local44 = local32.getMethod("setIgnoreRepaint", Boolean.TYPE);
                local44.invoke(canvas, Boolean.TRUE);
            } catch (@Pc(56) Exception local56) {
            }
            @Pc(61) Object local61 = this.method8127(canvas);
            if (local61 == null) {
                throw new RuntimeException();
            }
            this.aHashtable6.put(canvas, local61);
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "()Z")
    @Override
    public final boolean method7992() {
        return true;
    }

    @OriginalMember(owner = "client!am", name = "p", descriptor = "()Z")
    @Override
    public final boolean increaseRenderDistance() {
        return true;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ZB)Lclient!ri;")
    public abstract Interface20 method8122(@OriginalArg(0) boolean arg0);

    @OriginalMember(owner = "client!am", name = "K", descriptor = "([I)V")
    @Override
    public final void K(@OriginalArg(0) int[] destination) {
        destination[1] = this.anInt9161;
        destination[2] = this.anInt9153;
        destination[3] = this.anInt9165;
        destination[0] = this.anInt9154;
    }

    @OriginalMember(owner = "client!am", name = "e", descriptor = "(I)V")
    @Override
    public void tick(@OriginalArg(0) int time) {
        if (this.aClass334_3 != null) {
            this.aClass334_3.method7655();
        }
        this.anInt9164 = time & Integer.MAX_VALUE;
    }

    @OriginalMember(owner = "client!am", name = "d", descriptor = "(IZ)V")
    public final void method8123(@OriginalArg(1) boolean arg0) {
        if (this.aBoolean678 != arg0) {
            this.aBoolean678 = arg0;
            this.method8116();
            this.anInt9166 &= 0xFFFFFFE0;
        }
    }

    @OriginalMember(owner = "client!am", name = "F", descriptor = "(I)V")
    public void method8124() {
        this.method8130(0, this.anInterface16_14);
        this.method8114(this.aClass244_23);
        this.method8079(Static437.aClass131_4, 0, 1);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!hla;BZZI)V")
    public abstract void method8125(@OriginalArg(0) Class168 arg0, @OriginalArg(2) boolean arg1, @OriginalArg(3) boolean arg2, @OriginalArg(4) int arg3);

    @OriginalMember(owner = "client!am", name = "q", descriptor = "()V")
    @Override
    public final void cacheReset() {
        if (this.aClass334_3 != null) {
            this.aClass334_3.method7653();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "([I)V")
    @Override
    public final void method7944(@OriginalArg(0) int[] arg0) {
        arg0[0] = this.anInt9122;
        arg0[1] = this.anInt9013;
    }

    @OriginalMember(owner = "client!am", name = "e", descriptor = "(Z)V")
    public void method8126() {
        this.method8110();
        if (this.aClass26_3 != null) {
            this.aClass26_3.onProjectionChanged();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Ljava/awt/Canvas;I)Ljava/lang/Object;")
    protected abstract Object method8127(@OriginalArg(0) Canvas arg0);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "([IIIIIZ)Lclient!st;")
    @Override
    public final Sprite method7958(@OriginalArg(0) int[] arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) boolean arg4) {
        return new Sprite_Sub3(this, arg2, arg3, arg0, 0, arg1);
    }

    @OriginalMember(owner = "client!am", name = "z", descriptor = "(B)V")
    public void method8128() {
        this.aFloat196 = (float) this.anInt9167;
    }

    @OriginalMember(owner = "client!am", name = "k", descriptor = "(Z)V")
    protected abstract void method8129();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IILclient!mg;)V")
    public abstract void method8130(@OriginalArg(0) int arg0, @OriginalArg(2) Interface16 arg1);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!ve;[Lclient!wp;Z)Lclient!da;")
    @Override
    public final Font createFont(@OriginalArg(0) FontMetrics metrics, @OriginalArg(1) IndexedImage[] image, @OriginalArg(2) boolean monospaced) {
        return new Font_Sub1(this, metrics, image, monospaced);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(II[[I[[IIII)Lclient!s;")
    @Override
    public final Ground createGround(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[][] arg2, @OriginalArg(3) int[][] arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        return new Ground_Sub1(this, arg4, arg5, arg0, arg1, arg2, arg3, 512);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IIIIIILclient!aa;II)V")
    @Override
    public final void line(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int mode, @OriginalArg(6) ClippingMask mask, @OriginalArg(7) int maskX, @OriginalArg(8) int maskY) {
    }

    @OriginalMember(owner = "client!am", name = "Q", descriptor = "(I)V")
    public void method8131() {
        this.anInterface16_14 = this.method8156(true);
        this.anInterface16_14.method3153(12, 24);
        this.aClass244_23 = this.method8148(new Class237[]{new Class237(Static231.aClass157_1)});
    }

    @OriginalMember(owner = "client!am", name = "c", descriptor = "(IZ)V")
    public final void method8132(@OriginalArg(1) boolean arg0) {
        if (this.aBoolean680 != arg0) {
            this.aBoolean680 = arg0;
            this.method8078();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Z)V")
    @Override
    public final void setShrinkTextures(@OriginalArg(0) boolean arg0) {
    }

    @OriginalMember(owner = "client!am", name = "i", descriptor = "()I")
    @Override
    public final int i() {
        return this.anInt9181;
    }

    @OriginalMember(owner = "client!am", name = "x", descriptor = "(B)[F")
    public final float[] method8133() {
        return this.aFloatArray62;
    }

    @OriginalMember(owner = "client!am", name = "c", descriptor = "(I)V")
    @Override
    public final void method8016(@OriginalArg(0) int arg0) {
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!lk;)V")
    @Override
    public final void render(@OriginalArg(0) ParticleList particleList) {
        this.particleRenderer.render(particleList, -1, this);
    }

    @OriginalMember(owner = "client!am", name = "W", descriptor = "(I)V")
    public final void method8134() {
        this.aClass370Array3 = new Class370[this.anInt9184];
        this.aClass121Array5 = new Class121[this.anInt9184];
        this.aClass121Array6 = new Class121[this.anInt9184];
        this.aClass73_Sub1Array3 = new Matrix_Sub1[this.anInt9184];
        this.anInterface17Array3 = new Interface17[this.anInt9184];
        for (@Pc(38) int local38 = 0; local38 < this.anInt9184; local38++) {
            this.aClass121Array5[local38] = Static209.aClass121_4;
            this.aClass121Array6[local38] = Static209.aClass121_4;
            this.aClass370Array3[local38] = Static215.aClass370_2;
            this.aClass73_Sub1Array3[local38] = new Matrix_Sub1();
        }
        this.aClass2_Sub7Array6 = new PointLight[this.anInt9182 - 2];
        this.anInterface17_3 = this.method8067(Static172.aClass92_8, Static702.aClass397_16, 1, 1);
        this.method7938(new NativeMemoryPool(262144));
        this.aClass244_17 = this.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_5})});
        this.aClass244_21 = this.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_3})});
        this.aClass244_20 = this.method8148(new Class237[]{new Class237(Static231.aClass157_1), new Class237(Static231.aClass157_3), new Class237(Static231.aClass157_5), new Class237(Static231.aClass157_2)});
        this.aClass244_22 = this.method8148(new Class237[]{new Class237(Static231.aClass157_1), new Class237(Static231.aClass157_3), new Class237(Static231.aClass157_5)});
        for (@Pc(203) int local203 = 0; local203 < 7; local203++) {
            this.aClass114_Sub1Array6[local203] = new Model_Sub1(this, 0, 0, false, false);
            this.aClass114_Sub1Array5[local203] = new Model_Sub1(this, 0, 0, true, true);
        }
        this.particleRenderer = new NativeParticleRenderer(this);
        this.anInterface20_6 = this.method8122(true);
        this.method8101();
        this.aClass7_8 = new Class7(this);
        this.aClass26Array3[1] = this.method8087(1);
        this.aClass26Array3[2] = this.method8087(2);
        this.aClass26Array3[4] = this.method8087(4);
        this.aClass26Array3[5] = this.method8087(5);
        this.aClass26Array3[6] = this.method8087(6);
        this.aClass26Array3[7] = this.method8087(7);
        this.aClass26Array3[3] = this.method8087(3);
        this.aClass26Array3[8] = this.method8087(8);
        this.aClass26Array3[9] = this.method8087(9);
        if (!this.aClass26Array3[2].isSupported()) {
            this.aClass26Array3[2] = this.method8087(0);
        }
        if (!this.aClass26Array3[4].isSupported()) {
            this.aClass26Array3[4] = this.aClass26Array3[2];
        }
        if (!this.aClass26Array3[8].isSupported()) {
            this.aClass26Array3[8] = this.aClass26Array3[4];
        }
        if (!this.aClass26Array3[9].isSupported()) {
            this.aClass26Array3[9] = this.aClass26Array3[8];
        }
        this.method8102();
        this.la();
        this.method7969();
    }

    @OriginalMember(owner = "client!am", name = "V", descriptor = "(I)V")
    public void method8135() {
        this.aBoolean695 = false;
        this.method8029();
        if (this.aClass77_6 == Static690.aClass77_7) {
            this.method8126();
        }
    }

    @OriginalMember(owner = "client!am", name = "d", descriptor = "(B)V")
    protected abstract void method8136();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "([FI)[F")
    public final float[] method8137(@OriginalArg(0) float[] arg0) {
        arg0[8] = this.aFloatArray63[2];
        arg0[12] = this.aFloatArray63[3];
        arg0[0] = this.aFloatArray63[0];
        arg0[4] = this.aFloatArray63[1];
        arg0[5] = this.aFloatArray63[5];
        arg0[13] = this.aFloatArray63[7];
        arg0[9] = this.aFloatArray63[6];
        arg0[1] = this.aFloatArray63[4];
        arg0[2] = this.aFloatArray63[8];
        arg0[14] = this.aFloatArray63[11];
        arg0[3] = this.aFloatArray63[12];
        arg0[7] = this.aFloatArray63[13];
        arg0[6] = this.aFloatArray63[9];
        arg0[10] = this.aFloatArray63[10];
        arg0[11] = this.aFloatArray63[14];
        arg0[15] = this.aFloatArray63[15];
        return arg0;
    }

    @OriginalMember(owner = "client!am", name = "h", descriptor = "(II)V")
    public final void method8138(@OriginalArg(1) int arg0) {
        if (arg0 != this.anInt9148) {
            this.anInt9148 = arg0;
            this.method8085();
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IBLclient!hla;Z)V")
    protected abstract void method8139(@OriginalArg(0) int arg0, @OriginalArg(2) Class168 arg1);

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(IZ)V")
    public final void method8140(@OriginalArg(1) boolean arg0) {
        if (this.aBoolean690 != arg0) {
            this.aBoolean690 = arg0;
            this.method8084();
        }
    }

    @OriginalMember(owner = "client!am", name = "b", descriptor = "(ZB)V")
    public abstract void method8141(@OriginalArg(0) boolean arg0);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(BLclient!hla;I)V")
    public final void method8142(@OriginalArg(1) Class168 arg0, @OriginalArg(2) int arg1) {
        this.method8139(arg1, arg0);
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(II[IIIZI)Lclient!og;")
    public abstract Interface18 method8143(@OriginalArg(1) int arg0, @OriginalArg(2) int[] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) boolean arg4, @OriginalArg(6) int arg5);

    @OriginalMember(owner = "client!am", name = "r", descriptor = "(B)V")
    public void method8144() {
        if (this.aClass77_6 == Static517.aClass77_5) {
            return;
        }
        @Pc(18) Class77 local18 = this.aClass77_6;
        this.aClass77_6 = Static517.aClass77_5;
        if (!local18.method2049()) {
            this.method8081();
        }
        this.method8032();
        this.aFloatArray63 = this.aFloatArray66;
        this.method8126();
        this.anInt9166 &= 0xFFFFFFF8;
    }

    @OriginalMember(owner = "client!am", name = "B", descriptor = "()Z")
    @Override
    public final boolean method8001() {
        return true;
    }

    @OriginalMember(owner = "client!am", name = "b", descriptor = "()Z")
    @Override
    public final boolean method7983() {
        return true;
    }

    @OriginalMember(owner = "client!am", name = "pa", descriptor = "()V")
    @Override
    public final void pa() {
        this.aBoolean681 = false;
    }

    @OriginalMember(owner = "client!am", name = "x", descriptor = "(I)Lclient!fv;")
    public final Interface8 method8145() {
        return this.aClass67_Sub2_3 == null ? null : this.aClass67_Sub2_3.method8954();
    }

    @OriginalMember(owner = "client!am", name = "da", descriptor = "(III[I)V")
    @Override
    public final void da(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3) {
        @Pc(14) float local14 = this.aClass73_Sub1_16.method1889((float) arg2, (float) arg1, (float) arg0);
        @Pc(27) int local27;
        @Pc(30) int local30;
        if (local14 < -0.0078125F || local14 > 0.0078125F) {
            local27 = (int) ((float) this.anInt9151 * this.aClass73_Sub1_16.method1885((float) arg1, (float) arg2, (float) arg0) / local14);
            local30 = (int) ((float) this.anInt9180 * this.aClass73_Sub1_16.method1883((float) arg2, (float) arg0, (float) arg1) / local14);
        } else {
            local27 = this.anInt9170;
            local30 = this.anInt9163;
        }
        arg3[0] = (int) ((float) local27 - this.aFloat185);
        arg3[2] = (int) local14;
        arg3[1] = (int) ((float) local30 - this.aFloat183);
    }

    @OriginalMember(owner = "client!am", name = "Z", descriptor = "(I)V")
    protected abstract void method8146();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(ZZ)V")
    public final void method8147(@OriginalArg(0) boolean arg0) {
        if (this.aBoolean683 != arg0) {
            this.aBoolean683 = arg0;
            this.method8084();
            this.anInt9166 &= 0xFFFFFFE0;
        }
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(B[Lclient!mc;)Lclient!mk;")
    public abstract Class244 method8148(@OriginalArg(1) Class237[] arg0);

    @OriginalMember(owner = "client!am", name = "q", descriptor = "(I)V")
    public void method8149() {
        if (this.anInt9166 == 1) {
            return;
        }
        this.method8107();
        this.method8147(false);
        this.method8072(false);
        this.method8123(false);
        this.method8083(false);
        this.method8108(false, -2, false);
        this.method8054(1);
        this.method8088(this.anInterface17_3);
        this.anInt9166 = 1;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(II[I[I)Lclient!aa;")
    @Override
    public final ClippingMask createMask(@OriginalArg(0) int width, @OriginalArg(1) int height, @OriginalArg(2) int[] offsets, @OriginalArg(3) int[] widths) {
        return Static532.method7113(offsets, widths, width, this, height);
    }

    @OriginalMember(owner = "client!am", name = "t", descriptor = "(B)V")
    protected abstract void method8150();

    @OriginalMember(owner = "client!am", name = "l", descriptor = "(I)V")
    public void method8151() {
        this.aFloat190 = (float) (this.anInt9153 - this.anInt9170);
        this.aFloat185 = (float) (this.anInt9154 - this.anInt9170);
        this.aFloat188 = (float) (this.anInt9165 - this.anInt9163);
        this.aFloat183 = (float) (this.anInt9161 - this.anInt9163);
    }

    @OriginalMember(owner = "client!am", name = "G", descriptor = "(I)V")
    public final void method8152() {
        if (Static243.aClass77_3 == this.aClass77_6) {
            return;
        }
        @Pc(6) Class77 local6 = this.aClass77_6;
        this.aClass77_6 = Static243.aClass77_3;
        if (local6.method2049()) {
            this.method8081();
        }
        this.aFloatArray63 = this.aFloatArray62;
        this.anInt9166 &= 0xFFFFFFE0;
    }

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!wda;Lclient!eba;B)Z")
    public abstract boolean method8153(@OriginalArg(0) Class397 arg0, @OriginalArg(1) Class92 arg1);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(Lclient!dv;IIII)Lclient!ka;")
    @Override
    public final Model createModel(@OriginalArg(0) Mesh mesh, @OriginalArg(1) int functionMask, @OriginalArg(2) int featureMask, @OriginalArg(3) int ambient, @OriginalArg(4) int contrast) {
        return new Model_Sub1(this, mesh, functionMask, ambient, contrast, featureMask);
    }

    @OriginalMember(owner = "client!am", name = "Y", descriptor = "()[I")
    @Override
    public final int[] Y() {
        return new int[]{this.anInt9170, this.anInt9163, this.anInt9151, this.anInt9180};
    }

    @OriginalMember(owner = "client!am", name = "n", descriptor = "(I)Lclient!cw;")
    public final Matrix_Sub1 method8154() {
        if (!this.aBoolean682) {
            this.aClass73_Sub1_20.method1890(this.aClass73_Sub1_18, this.aClass73_Sub1_15);
            this.aBoolean682 = true;
        }
        return this.aClass73_Sub1_20;
    }

    @OriginalMember(owner = "client!am", name = "D", descriptor = "(I)F")
    protected abstract float method8155();

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(IZ)Lclient!mg;")
    public abstract Interface16 method8156(@OriginalArg(1) boolean arg0);

    @OriginalMember(owner = "client!am", name = "a", descriptor = "(FFFFFFI)Z")
    public boolean method8157(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2, @OriginalArg(3) float arg3, @OriginalArg(4) float arg4, @OriginalArg(5) float arg5) {
        @Pc(9) Buffer local9 = this.anInterface16_14.method3155();
        if (local9 == null) {
            return false;
        }
        @Pc(19) Stream local19 = this.method8100(local9);
        if (Stream.b()) {
            local19.a(arg2);
            local19.a(arg4);
            local19.a(arg5);
            local19.a(arg0);
            local19.a(arg3);
            local19.a(arg1);
        } else {
            local19.b(arg2);
            local19.b(arg4);
            local19.b(arg5);
            local19.b(arg0);
            local19.b(arg3);
            local19.b(arg1);
        }
        local19.c();
        return this.anInterface16_14.method3154();
    }

    @OriginalMember(owner = "client!am", name = "JA", descriptor = "(IIIIII)I")
    @Override
    public final int JA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(7) int local7 = 0;
        @Pc(32) float local32 = this.aClass73_Sub1_16.aFloat36 * (float) arg1 + this.aClass73_Sub1_16.aFloat45 * (float) arg0 + this.aClass73_Sub1_16.aFloat47 * (float) arg2 + this.aClass73_Sub1_16.aFloat43;
        if (local32 < 1.0F) {
            local32 = 1.0F;
        }
        @Pc(63) float local63 = this.aClass73_Sub1_16.aFloat43 + (this.aClass73_Sub1_16.aFloat45 * (float) arg3 + (float) arg4 * this.aClass73_Sub1_16.aFloat36 + this.aClass73_Sub1_16.aFloat47 * (float) arg5);
        if (local63 < 1.0F) {
            local63 = 1.0F;
        }
        if ((float) this.anInt9181 > local32 && local63 < (float) this.anInt9181) {
            local7 |= 0x10;
        } else if ((float) this.anInt9167 < local32 && (float) this.anInt9167 < local63) {
            local7 |= 0x20;
        }
        @Pc(141) int local141 = (int) ((float) this.anInt9151 * ((float) arg2 * this.aClass73_Sub1_16.aFloat38 + ((float) arg0 * this.aClass73_Sub1_16.aFloat39 + (float) arg1 * this.aClass73_Sub1_16.aFloat40) + this.aClass73_Sub1_16.aFloat46) / local32);
        @Pc(173) int local173 = (int) ((float) this.anInt9151 * (this.aClass73_Sub1_16.aFloat46 + (this.aClass73_Sub1_16.aFloat39 * (float) arg3 + this.aClass73_Sub1_16.aFloat40 * (float) arg4 + (float) arg5 * this.aClass73_Sub1_16.aFloat38)) / local63);
        if (this.aFloat185 > (float) local141 && this.aFloat185 > (float) local173) {
            local7 |= 0x1;
        } else if (this.aFloat190 < (float) local141 && (float) local173 > this.aFloat190) {
            local7 |= 0x2;
        }
        @Pc(245) int local245 = (int) ((this.aClass73_Sub1_16.aFloat37 + ((float) arg0 * this.aClass73_Sub1_16.aFloat44 + this.aClass73_Sub1_16.aFloat42 * (float) arg1 + this.aClass73_Sub1_16.aFloat41 * (float) arg2)) * (float) this.anInt9180 / local32);
        @Pc(277) int local277 = (int) ((this.aClass73_Sub1_16.aFloat44 * (float) arg3 + this.aClass73_Sub1_16.aFloat42 * (float) arg4 + (float) arg5 * this.aClass73_Sub1_16.aFloat41 + this.aClass73_Sub1_16.aFloat37) * (float) this.anInt9180 / local63);
        if ((float) local245 < this.aFloat183 && this.aFloat183 > (float) local277) {
            local7 |= 0x4;
        } else if ((float) local245 > this.aFloat188 && (float) local277 > this.aFloat188) {
            local7 |= 0x8;
        }
        return local7;
    }

    @OriginalMember(owner = "client!am", name = "U", descriptor = "(I)V")
    public abstract void method8158();

    @OriginalMember(owner = "client!am", name = "f", descriptor = "(I)V")
    @Override
    public final void setTextureSize(@OriginalArg(0) int size) {
        if (size < 128 || size > 1024) {
            throw new IllegalArgumentException();
        }
        if (this.aClass334_3 != null) {
            this.aClass334_3.method7653();
        }
        this.anInt9183 = size;
    }
}

import com.jagex.ParticleList;
import com.jagex.IndexedImage;
import com.jagex.Class67;
import com.jagex.core.constants.PciVendorId;
import com.jagex.graphics.Renderer;
import com.jagex.DepthBuffer;
import com.jagex.Static14;
import com.jagex.math.ColourUtils;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.Queue;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.FlipException;
import com.jagex.graphics.Ground;
import com.jagex.graphics.OffscreenSurface;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.MemoryPool;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.skybox.SkyBoxSphere;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Surface;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeap;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.ClientInfo;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

@OriginalClass("client!qha")
public final class GlToolkit extends Toolkit {

    @OriginalMember(owner = "client!kh", name = "N", descriptor = "[F")
    public static final float[] SIN = new float[16384];

    @OriginalMember(owner = "client!kh", name = "f", descriptor = "[F")
    public static final float[] COS = new float[16384];

    static {
        @Pc(433) double d = 3.834951969714103E-4D;
        for (@Pc(435) int i = 0; i < 16384; i++) {
            SIN[i] = (float) Math.sin(d * (double) i);
            COS[i] = (float) Math.cos((double) i * d);
        }
    }

    @OriginalMember(owner = "client!gga", name = "a", descriptor = "(Lclient!d;Ljava/awt/Canvas;II)Lclient!ha;")
    public static Toolkit create(@OriginalArg(1) Canvas canvas, @OriginalArg(0) TextureSource textureSource, @OriginalArg(2) int antialiasing) {
        return new GlToolkit(canvas, textureSource, antialiasing);
    }

    @OriginalMember(owner = "client!qha", name = "Mf", descriptor = "I")
    public int anInt7864;

    @OriginalMember(owner = "client!qha", name = "Jg", descriptor = "I")
    public int anInt7869;

    @OriginalMember(owner = "client!qha", name = "jg", descriptor = "I")
    public int anInt7904;

    @OriginalMember(owner = "client!qha", name = "ih", descriptor = "I")
    public int anInt7956;

    @OriginalMember(owner = "client!qha", name = "Gf", descriptor = "I")
    public int anInt7987;

    @OriginalMember(owner = "client!qha", name = "lb", descriptor = "Lclient!jaclib/memory/heap/NativeHeap;")
    public NativeHeap lb;

    @OriginalMember(owner = "client!qha", name = "zg", descriptor = "Lclient!wo;")
    public Class406 aClass406_6;

    @OriginalMember(owner = "client!qha", name = "qh", descriptor = "Lclient!jf;")
    public Interface14 anInterface14_1;

    @OriginalMember(owner = "client!qha", name = "u", descriptor = "Lclient!mb;")
    public Sprite_Sub2 aClass23_Sub2_1;

    @OriginalMember(owner = "client!qha", name = "Lc", descriptor = "Lclient!jf;")
    public Interface14 anInterface14_2;

    @OriginalMember(owner = "client!qha", name = "D", descriptor = "I")
    public int anInt7993;

    @OriginalMember(owner = "client!qha", name = "lc", descriptor = "I")
    public int anInt7994;

    @OriginalMember(owner = "client!qha", name = "Vg", descriptor = "I")
    public int anInt7995;

    @OriginalMember(owner = "client!qha", name = "pe", descriptor = "I")
    public int anInt7996;

    @OriginalMember(owner = "client!qha", name = "Bb", descriptor = "Z")
    public boolean aBoolean590;

    @OriginalMember(owner = "client!qha", name = "Bc", descriptor = "Z")
    public boolean aBoolean591;

    @OriginalMember(owner = "client!qha", name = "Wf", descriptor = "Z")
    public boolean aBoolean592;

    @OriginalMember(owner = "client!qha", name = "ab", descriptor = "I")
    public int anInt7997;

    @OriginalMember(owner = "client!qha", name = "Of", descriptor = "I")
    public int anInt7998;

    @OriginalMember(owner = "client!qha", name = "bh", descriptor = "Z")
    public boolean aBoolean593;

    @OriginalMember(owner = "client!qha", name = "eb", descriptor = "J")
    public long aLong248;

    @OriginalMember(owner = "client!qha", name = "gc", descriptor = "I")
    public int anInt7999;

    @OriginalMember(owner = "client!qha", name = "Me", descriptor = "Z")
    public boolean aBoolean594;

    @OriginalMember(owner = "client!qha", name = "Oe", descriptor = "Lclient!jc;")
    public Interface12 anInterface12_6;

    @OriginalMember(owner = "client!qha", name = "x", descriptor = "Z")
    public boolean aBoolean597;

    @OriginalMember(owner = "client!qha", name = "uc", descriptor = "Ljava/lang/String;")
    public String aString98;

    @OriginalMember(owner = "client!qha", name = "Wc", descriptor = "Z")
    public boolean aBoolean598;

    @OriginalMember(owner = "client!qha", name = "Y", descriptor = "F")
    public float aFloat131;

    @OriginalMember(owner = "client!qha", name = "jc", descriptor = "Z")
    public boolean aBoolean599;

    @OriginalMember(owner = "client!qha", name = "ng", descriptor = "F")
    public float aFloat132;

    @OriginalMember(owner = "client!qha", name = "Bd", descriptor = "Z")
    public boolean aBoolean601;

    @OriginalMember(owner = "client!qha", name = "lf", descriptor = "Z")
    public boolean aBoolean602;

    @OriginalMember(owner = "client!qha", name = "se", descriptor = "F")
    public float aFloat133;

    @OriginalMember(owner = "client!qha", name = "kb", descriptor = "Z")
    public boolean aBoolean604;

    @OriginalMember(owner = "client!qha", name = "gg", descriptor = "F")
    public float aFloat134;

    @OriginalMember(owner = "client!qha", name = "G", descriptor = "Ljava/lang/String;")
    public String aString99;

    @OriginalMember(owner = "client!qha", name = "Zd", descriptor = "F")
    public float aFloat138;

    @OriginalMember(owner = "client!qha", name = "md", descriptor = "Z")
    public boolean aBoolean605;

    @OriginalMember(owner = "client!qha", name = "Wg", descriptor = "F")
    public float aFloat140;

    @OriginalMember(owner = "client!qha", name = "wg", descriptor = "I")
    public int anInt8003;

    @OriginalMember(owner = "client!qha", name = "bb", descriptor = "Lclient!ed;")
    public Class94 aClass94_15;

    @OriginalMember(owner = "client!qha", name = "Mg", descriptor = "I")
    public int anInt8004;

    @OriginalMember(owner = "client!qha", name = "Sg", descriptor = "Lclient!rq;")
    public Class93_Sub2 aClass93_Sub2_5;

    @OriginalMember(owner = "client!qha", name = "ah", descriptor = "I")
    public int anInt8005;

    @OriginalMember(owner = "client!qha", name = "Pc", descriptor = "F")
    public float aFloat141;

    @OriginalMember(owner = "client!qha", name = "Pb", descriptor = "I")
    public int anInt8006;

    @OriginalMember(owner = "client!qha", name = "Fc", descriptor = "Lclient!ed;")
    public Class94 aClass94_16;

    @OriginalMember(owner = "client!qha", name = "Ke", descriptor = "Lclient!wo;")
    public Class406 aClass406_7;

    @OriginalMember(owner = "client!qha", name = "Eb", descriptor = "Z")
    public boolean aBoolean608;

    @OriginalMember(owner = "client!qha", name = "Rc", descriptor = "I")
    public int anInt8017;

    @OriginalMember(owner = "client!qha", name = "Pf", descriptor = "I")
    public int anInt8019;

    @OriginalMember(owner = "client!qha", name = "Df", descriptor = "Lclient!ar;")
    public Interface1 anInterface1_4;

    @OriginalMember(owner = "client!qha", name = "Tg", descriptor = "F")
    public float aFloat145;

    @OriginalMember(owner = "client!qha", name = "sb", descriptor = "I")
    public int anInt8020;

    @OriginalMember(owner = "client!qha", name = "C", descriptor = "Z")
    public boolean aBoolean611;

    @OriginalMember(owner = "client!qha", name = "Z", descriptor = "I")
    public int anInt8023;

    @OriginalMember(owner = "client!qha", name = "Ud", descriptor = "Z")
    public boolean aBoolean612;

    @OriginalMember(owner = "client!qha", name = "Ld", descriptor = "Z")
    public boolean aBoolean614;

    @OriginalMember(owner = "client!qha", name = "ue", descriptor = "Lclient!gb;")
    public Class93_Sub2_Sub1 aClass93_Sub2_Sub1_4;

    @OriginalMember(owner = "client!qha", name = "dg", descriptor = "Z")
    public boolean aBoolean615;

    @OriginalMember(owner = "client!qha", name = "Kd", descriptor = "Lclient!tl;")
    public Class67_Sub1 aClass67_Sub1_1;

    @OriginalMember(owner = "client!qha", name = "bf", descriptor = "Z")
    public boolean bf;

    @OriginalMember(owner = "client!qha", name = "Vc", descriptor = "I")
    public int anInt8024;

    @OriginalMember(owner = "client!qha", name = "Ac", descriptor = "Lclient!jc;")
    public Interface12 anInterface12_7;

    @OriginalMember(owner = "client!qha", name = "Zc", descriptor = "I")
    public int anInt8027;

    @OriginalMember(owner = "client!qha", name = "Rg", descriptor = "Z")
    public boolean aBoolean617;

    @OriginalMember(owner = "client!qha", name = "mg", descriptor = "Z")
    public boolean aBoolean618;

    @OriginalMember(owner = "client!qha", name = "zb", descriptor = "F")
    public float aFloat149;

    @OriginalMember(owner = "client!qha", name = "nh", descriptor = "Z")
    public boolean aBoolean619;

    @OriginalMember(owner = "client!qha", name = "Db", descriptor = "[Lclient!kd;")
    public Class93[] aClass93Array1;

    @OriginalMember(owner = "client!qha", name = "eg", descriptor = "I")
    public int anInt8031;

    @OriginalMember(owner = "client!qha", name = "Tf", descriptor = "I")
    public int anInt8033;

    @OriginalMember(owner = "client!qha", name = "Oc", descriptor = "I")
    public int anInt8034;

    @OriginalMember(owner = "client!qha", name = "Re", descriptor = "Ljava/util/Hashtable;")
    public final Hashtable aHashtable5 = new Hashtable();

    @OriginalMember(owner = "client!qha", name = "Sc", descriptor = "I")
    public int anInt7981 = 128;

    @OriginalMember(owner = "client!qha", name = "I", descriptor = "Lclient!bj;")
    public final GlParticleRenderer particleRenderer = new GlParticleRenderer();

    @OriginalMember(owner = "client!qha", name = "Dd", descriptor = "Lclient!qr;")
    public final Matrix_Sub3 aClass73_Sub3_1 = new Matrix_Sub3();

    @OriginalMember(owner = "client!qha", name = "Fb", descriptor = "Lclient!qr;")
    public final Matrix_Sub3 aClass73_Sub3_2 = new Matrix_Sub3();

    @OriginalMember(owner = "client!qha", name = "Ee", descriptor = "Z")
    public boolean aBoolean589 = false;

    @OriginalMember(owner = "client!qha", name = "db", descriptor = "I")
    public int anInt7988 = 8;

    @OriginalMember(owner = "client!qha", name = "fe", descriptor = "I")
    public int anInt7989 = 3;

    @OriginalMember(owner = "client!qha", name = "sf", descriptor = "Lclient!sia;")
    public final Deque aDeque_46 = new Deque();

    @OriginalMember(owner = "client!qha", name = "Yc", descriptor = "[Lclient!jf;")
    public final Interface14[] anInterface14Array1 = new Interface14[4];

    @OriginalMember(owner = "client!qha", name = "ze", descriptor = "I")
    public int anInt7991 = -1;

    @OriginalMember(owner = "client!qha", name = "ke", descriptor = "I")
    public int anInt7990 = -1;

    @OriginalMember(owner = "client!qha", name = "F", descriptor = "[Lclient!jf;")
    public final Interface14[] anInterface14Array2 = new Interface14[4];

    @OriginalMember(owner = "client!qha", name = "Qe", descriptor = "I")
    public int anInt7992 = -1;

    @OriginalMember(owner = "client!qha", name = "Ed", descriptor = "[Lclient!jf;")
    public final Interface14[] anInterface14Array3 = new Interface14[4];

    @OriginalMember(owner = "client!qha", name = "rh", descriptor = "Lclient!sia;")
    public final Deque aDeque_47;

    @OriginalMember(owner = "client!qha", name = "Zf", descriptor = "Lclient!sia;")
    public final Deque aDeque_48;

    @OriginalMember(owner = "client!qha", name = "gf", descriptor = "Lclient!sia;")
    public final Deque aDeque_49;

    @OriginalMember(owner = "client!qha", name = "Pg", descriptor = "Lclient!sia;")
    public final Deque aDeque_50;

    @OriginalMember(owner = "client!qha", name = "ed", descriptor = "Lclient!sia;")
    public final Deque aDeque_51;

    @OriginalMember(owner = "client!qha", name = "Zb", descriptor = "Lclient!sia;")
    public final Deque aDeque_52;

    @OriginalMember(owner = "client!qha", name = "Hg", descriptor = "Lclient!sia;")
    public final Deque aDeque_53;

    @OriginalMember(owner = "client!qha", name = "hh", descriptor = "Lclient!qr;")
    public final Matrix_Sub3 aClass73_Sub3_3;

    @OriginalMember(owner = "client!qha", name = "Og", descriptor = "Lclient!qr;")
    public final Matrix_Sub3 aClass73_Sub3_4;

    @OriginalMember(owner = "client!qha", name = "nf", descriptor = "Lclient!qr;")
    public final Matrix_Sub3 aClass73_Sub3_5;

    @OriginalMember(owner = "client!qha", name = "Mc", descriptor = "[Lclient!lca;")
    public final PointLight[] aClass2_Sub7Array5;

    @OriginalMember(owner = "client!qha", name = "E", descriptor = "F")
    public float aFloat129;

    @OriginalMember(owner = "client!qha", name = "Ib", descriptor = "[F")
    public final float[] aFloatArray52;

    @OriginalMember(owner = "client!qha", name = "Yg", descriptor = "I")
    public int anInt8002;

    @OriginalMember(owner = "client!qha", name = "Kc", descriptor = "F")
    public float aFloat135;

    @OriginalMember(owner = "client!qha", name = "R", descriptor = "F")
    public float aFloat136;

    @OriginalMember(owner = "client!qha", name = "af", descriptor = "I")
    public int anInt8007;

    @OriginalMember(owner = "client!qha", name = "rd", descriptor = "I")
    public int anInt8000;

    @OriginalMember(owner = "client!qha", name = "Nb", descriptor = "I")
    public int anInt8001;

    @OriginalMember(owner = "client!qha", name = "nc", descriptor = "I")
    public int anInt8014;

    @OriginalMember(owner = "client!qha", name = "Pe", descriptor = "I")
    public int anInt8013;

    @OriginalMember(owner = "client!qha", name = "cg", descriptor = "I")
    public int anInt8015;

    @OriginalMember(owner = "client!qha", name = "ge", descriptor = "I")
    public int anInt8008;

    @OriginalMember(owner = "client!qha", name = "Ad", descriptor = "I")
    public int anInt8010;

    @OriginalMember(owner = "client!qha", name = "te", descriptor = "[Lclient!kla;")
    public final Model_Sub2[] aClass114_Sub2Array1;

    @OriginalMember(owner = "client!qha", name = "If", descriptor = "F")
    public float aFloat143;

    @OriginalMember(owner = "client!qha", name = "fh", descriptor = "F")
    public float aFloat137;

    @OriginalMember(owner = "client!qha", name = "Qg", descriptor = "F")
    public float aFloat130;

    @OriginalMember(owner = "client!qha", name = "ob", descriptor = "Z")
    public boolean aBoolean609;

    @OriginalMember(owner = "client!qha", name = "Lb", descriptor = "I")
    public int anInt8022;

    @OriginalMember(owner = "client!qha", name = "pc", descriptor = "I")
    public int anInt8011;

    @OriginalMember(owner = "client!qha", name = "dc", descriptor = "I")
    public int anInt8012;

    @OriginalMember(owner = "client!qha", name = "zc", descriptor = "F")
    public float aFloat146;

    @OriginalMember(owner = "client!qha", name = "Hb", descriptor = "F")
    public float aFloat142;

    @OriginalMember(owner = "client!qha", name = "ne", descriptor = "I")
    public int anInt8026;

    @OriginalMember(owner = "client!qha", name = "Gg", descriptor = "I")
    public int anInt8018;

    @OriginalMember(owner = "client!qha", name = "T", descriptor = "[F")
    public final float[] aFloatArray53;

    @OriginalMember(owner = "client!qha", name = "sc", descriptor = "F")
    public float aFloat148;

    @OriginalMember(owner = "client!qha", name = "Fe", descriptor = "F")
    public float aFloat139;

    @OriginalMember(owner = "client!qha", name = "Wb", descriptor = "I")
    public int anInt8021;

    @OriginalMember(owner = "client!qha", name = "N", descriptor = "[F")
    public final float[] aFloatArray50;

    @OriginalMember(owner = "client!qha", name = "xd", descriptor = "I")
    public int anInt8028;

    @OriginalMember(owner = "client!qha", name = "kd", descriptor = "I")
    public int anInt8029;

    @OriginalMember(owner = "client!qha", name = "Ze", descriptor = "F")
    public float aFloat144;

    @OriginalMember(owner = "client!qha", name = "ud", descriptor = "F")
    public float aFloat147;

    @OriginalMember(owner = "client!qha", name = "Ub", descriptor = "Z")
    public boolean aBoolean596;

    @OriginalMember(owner = "client!qha", name = "wb", descriptor = "[F")
    public final float[] aFloatArray51;

    @OriginalMember(owner = "client!qha", name = "Uc", descriptor = "[Lclient!kla;")
    public final Model_Sub2[] aClass114_Sub2Array2;

    @OriginalMember(owner = "client!qha", name = "yf", descriptor = "I")
    public int anInt8025;

    @OriginalMember(owner = "client!qha", name = "Lg", descriptor = "I")
    public int anInt8009;

    @OriginalMember(owner = "client!qha", name = "M", descriptor = "I")
    public int anInt8032;

    @OriginalMember(owner = "client!qha", name = "Dg", descriptor = "I")
    public int anInt8016;

    @OriginalMember(owner = "client!qha", name = "pb", descriptor = "[F")
    public final float[] aFloatArray54;

    @OriginalMember(owner = "client!qha", name = "qb", descriptor = "Lclient!jfa;")
    public Node_Sub21_Sub1 aClass2_Sub21_Sub1_3;

    @OriginalMember(owner = "client!qha", name = "W", descriptor = "[B")
    public final byte[] aByteArray90;

    @OriginalMember(owner = "client!qha", name = "Cg", descriptor = "[I")
    public int[] anIntArray624;

    @OriginalMember(owner = "client!qha", name = "tf", descriptor = "[I")
    public int[] anIntArray625;

    @OriginalMember(owner = "client!qha", name = "Nd", descriptor = "[I")
    public int[] anIntArray623;

    @OriginalMember(owner = "client!qha", name = "vg", descriptor = "I")
    public final int anInt7986;

    @OriginalMember(owner = "client!qha", name = "Ng", descriptor = "Ljava/awt/Canvas;")
    public final Canvas aCanvas10;

    @OriginalMember(owner = "client!qha", name = "Xg", descriptor = "Ljava/awt/Canvas;")
    public Canvas aCanvas11;

    @OriginalMember(owner = "client!qha", name = "K", descriptor = "Lclient!jaggl/OpenGL;")
    public OpenGL anOpenGL1;

    @OriginalMember(owner = "client!qha", name = "vd", descriptor = "J")
    public final long aLong247;

    @OriginalMember(owner = "client!qha", name = "yg", descriptor = "J")
    public long aLong246;

    @OriginalMember(owner = "client!qha", name = "O", descriptor = "I")
    public final int anInt8030;

    @OriginalMember(owner = "client!qha", name = "gd", descriptor = "Z")
    public boolean aBoolean603;

    @OriginalMember(owner = "client!qha", name = "Kg", descriptor = "Z")
    public boolean aBoolean606;

    @OriginalMember(owner = "client!qha", name = "tb", descriptor = "Z")
    public boolean aBoolean600;

    @OriginalMember(owner = "client!qha", name = "Rf", descriptor = "Z")
    public boolean aBoolean595;

    @OriginalMember(owner = "client!qha", name = "Uf", descriptor = "Z")
    public boolean aBoolean616;

    @OriginalMember(owner = "client!qha", name = "kg", descriptor = "Z")
    public boolean aBoolean610;

    @OriginalMember(owner = "client!qha", name = "Xe", descriptor = "Z")
    public boolean aBoolean613;

    @OriginalMember(owner = "client!qha", name = "Ug", descriptor = "Z")
    public final boolean aBoolean607;

    @OriginalMember(owner = "client!qha", name = "Qd", descriptor = "Lclient!hm;")
    public final Class169 aClass169_1;

    @OriginalMember(owner = "client!qha", name = "Qb", descriptor = "Lclient!kaa;")
    public final Class202 aClass202_1;

    @OriginalMember(owner = "client!qha", name = "rf", descriptor = "Lclient!oia;")
    public final Class276 aClass276_1;

    @OriginalMember(owner = "client!qha", name = "ub", descriptor = "Lclient!kca;")
    public Node_Sub31_Sub1 aClass2_Sub31_Sub1_1;

    @OriginalMember(owner = "client!qha", name = "vf", descriptor = "Lclient!eg;")
    public final Class98 aClass98_1;

    @OriginalMember(owner = "client!qha", name = "<init>", descriptor = "(Ljava/awt/Canvas;Lclient!d;I)V")
    public GlToolkit(@OriginalArg(0) Canvas arg0, @OriginalArg(1) TextureSource arg1, @OriginalArg(2) int arg2) {
        super(arg1);
        new Queue();
        new IterableHashTable(16);
        this.aDeque_47 = new Deque();
        this.aDeque_48 = new Deque();
        this.aDeque_49 = new Deque();
        this.aDeque_50 = new Deque();
        this.aDeque_51 = new Deque();
        this.aDeque_52 = new Deque();
        this.aDeque_53 = new Deque();
        this.aClass73_Sub3_3 = new Matrix_Sub3();
        this.aClass73_Sub3_4 = new Matrix_Sub3();
        this.aClass73_Sub3_5 = new Matrix_Sub3();
        this.aClass2_Sub7Array5 = new PointLight[Static509.anInt7634];
        this.aFloat129 = -1.0F;
        this.aFloatArray52 = new float[4];
        this.anInt8002 = -1;
        this.aFloat135 = -1.0F;
        this.aFloat136 = -1.0F;
        this.anInt8007 = 0;
        this.anInt8000 = 0;
        this.anInt8001 = 512;
        this.anInt8014 = 0;
        this.anInt8013 = -1;
        this.anInt8015 = 8448;
        this.anInt8008 = -1;
        this.anInt8010 = 50;
        this.aClass114_Sub2Array1 = new Model_Sub2[7];
        this.aFloat143 = 1.0F;
        this.aFloat137 = 1.0F;
        this.aFloat130 = -1.0F;
        this.aBoolean609 = false;
        this.anInt8022 = 8448;
        this.anInt8011 = -1;
        this.anInt8012 = 0;
        this.aFloat146 = 1.0F;
        this.aFloat142 = 3584.0F;
        this.anInt8026 = -1;
        this.anInt8018 = 3584;
        this.aFloatArray53 = new float[16];
        this.aFloat148 = 1.0F;
        this.aFloat139 = 1.0F;
        this.anInt8021 = 0;
        this.aFloatArray50 = new float[4];
        this.anInt8028 = 0;
        this.anInt8029 = 0;
        this.aFloat144 = 3584.0F;
        this.aFloat147 = 0.0F;
        this.aBoolean596 = true;
        this.aFloatArray51 = new float[4];
        this.aClass114_Sub2Array2 = new Model_Sub2[7];
        this.anInt8025 = 512;
        this.anInt8009 = 0;
        this.anInt8032 = 0;
        this.anInt8016 = 0;
        this.aFloatArray54 = new float[4];
        this.aClass2_Sub21_Sub1_3 = new Node_Sub21_Sub1(8192);
        this.aByteArray90 = new byte[16384];
        this.anIntArray624 = new int[1];
        this.anIntArray625 = new int[1];
        this.anIntArray623 = new int[1];
        this.anInt7986 = arg2;
        this.aCanvas11 = this.aCanvas10 = arg0;
        if (!Static14.loadNativeLibrary("jaclib")) {
            throw new RuntimeException("");
        } else if (Static14.loadNativeLibrary("jaggl")) {
            try {
                this.anOpenGL1 = new OpenGL();
                this.aLong246 = this.aLong247 = this.anOpenGL1.init(arg0, 8, 8, 8, 24, 0, this.anInt7986);
                if (this.aLong247 == 0L) {
                    throw new RuntimeException("");
                }
                this.method6982();
                @Pc(347) int local347 = this.method6997();
                if (local347 != 0) {
                    throw new RuntimeException("");
                }
                this.anInt8030 = this.aBoolean618 ? 33639 : 5121;
                if (this.aString99.indexOf("radeon") != -1) {
                    @Pc(375) int local375 = 0;
                    @Pc(377) boolean local377 = false;
                    @Pc(379) boolean local379 = false;
                    @Pc(388) String[] local388 = StringTools.split(this.aString99.replace('/', ' '), ' ');
                    for (@Pc(390) int local390 = 0; local390 < local388.length; local390++) {
                        @Pc(395) String local395 = local388[local390];
                        try {
                            if (local395.length() > 0) {
                                if (local395.charAt(0) == 'x' && local395.length() >= 3 && StringTools.isDecimal(local395.substring(1, 3))) {
                                    local395 = local395.substring(1);
                                    local379 = true;
                                }
                                if (local395.equals("hd")) {
                                    local377 = true;
                                } else {
                                    if (local395.startsWith("hd")) {
                                        local395 = local395.substring(2);
                                        local377 = true;
                                    }
                                    if (local395.length() >= 4 && StringTools.isDecimal(local395.substring(0, 4))) {
                                        local375 = StringTools.parseDecimal(local395.substring(0, 4));
                                        break;
                                    }
                                }
                            }
                        } catch (@Pc(472) Exception local472) {
                        }
                    }
                    if (!local377 || local375 < 4000) {
                        this.aBoolean603 = false;
                    }
                    if (!local379 && !local377) {
                        if (local375 >= 7000 && local375 <= 9250) {
                            this.aBoolean606 = false;
                        }
                        if (local375 >= 7000 && local375 <= 7999) {
                            this.aBoolean600 = false;
                        }
                    }
                    this.aBoolean595 &= this.anOpenGL1.a("GL_ARB_half_float_pixel");
                    this.aBoolean616 = this.aBoolean600;
                    this.aBoolean610 = true;
                }
                if (this.aString98.indexOf("intel") != -1) {
                    this.aBoolean613 = false;
                }
                this.aBoolean607 = !this.aString98.equals("s3 graphics");
                if (this.aBoolean600) {
                    try {
                        @Pc(582) int[] local582 = new int[1];
                        OpenGL.glGenBuffersARB(1, local582, 0);
                    } catch (@Pc(588) Throwable local588) {
                        throw new RuntimeException("");
                    }
                }
                ColourUtils.init(true, false);
                this.aBoolean589 = true;
                this.aClass169_1 = new Class169(this, super.textureSource);
                this.method6992();
                this.aClass202_1 = new Class202(this);
                this.aClass276_1 = new Class276(this);
                if (this.aClass276_1.method6248()) {
                    this.aClass2_Sub31_Sub1_1 = new Node_Sub31_Sub1(this);
                    if (!this.aClass2_Sub31_Sub1_1.method4623()) {
                        this.aClass2_Sub31_Sub1_1.method4608();
                        this.aClass2_Sub31_Sub1_1 = null;
                    }
                }
                this.aClass98_1 = new Class98(this);
                this.method6984();
                this.method7013();
                this.method7969();
            } catch (@Pc(666) Throwable local666) {
                local666.printStackTrace();
                this.stop();
                throw new RuntimeException("");
            }
        } else {
            throw new RuntimeException("");
        }
    }

    @OriginalMember(owner = "client!qha", name = "h", descriptor = "(B)Lclient!ec;")
    public Class93_Sub1 method6963() {
        return this.aClass67_Sub1_1 == null ? null : this.aClass67_Sub1_1.method8589();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(BF)V")
    public void method6964(@OriginalArg(1) float arg0) {
        if (this.aFloat146 != arg0) {
            this.aFloat146 = arg0;
            if (this.anInt8005 == 3) {
                this.method6976();
            }
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ILclient!jc;)V")
    public void method6965(@OriginalArg(1) Interface12 arg0) {
        if (this.anInterface12_6 != arg0) {
            if (this.aBoolean600) {
                OpenGL.glBindBufferARB(OpenGL.GL_ARRAY_BUFFER, arg0.method5004());
            }
            this.anInterface12_6 = arg0;
        }
    }

    @OriginalMember(owner = "client!qha", name = "n", descriptor = "(I)V")
    public void method6966() {
        Static476.aFloatArray46[1] = this.aFloat129 * this.aFloat143;
        Static476.aFloatArray46[2] = this.aFloat137 * this.aFloat129;
        Static476.aFloatArray46[3] = 1.0F;
        Static476.aFloatArray46[0] = this.aFloat129 * this.aFloat148;
        OpenGL.glLightfv(OpenGL.GL_LIGHT0, OpenGL.GL_DIFFUSE, Static476.aFloatArray46, 0);
        Static476.aFloatArray46[0] = -this.aFloat130 * this.aFloat148;
        Static476.aFloatArray46[2] = this.aFloat137 * -this.aFloat130;
        Static476.aFloatArray46[1] = this.aFloat143 * -this.aFloat130;
        Static476.aFloatArray46[3] = 1.0F;
        OpenGL.glLightfv(OpenGL.GL_LIGHT1, OpenGL.GL_DIFFUSE, Static476.aFloatArray46, 0);
    }

    @OriginalMember(owner = "client!qha", name = "ra", descriptor = "(IIII)V")
    @Override
    public void ra(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        this.aBoolean605 = true;
        this.anInt8026 = arg1;
        this.anInt8006 = arg0;
        this.anInt8013 = arg2;
        this.anInt8029 = arg3;
    }

    @OriginalMember(owner = "client!qha", name = "o", descriptor = "()Z")
    @Override
    public boolean supportsBloom() {
        return this.aClass2_Sub31_Sub1_1 != null && (this.anInt7986 <= 1 || this.aBoolean614);
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(ILclient!jf;)V")
    public void method6967(@OriginalArg(1) Interface14 arg0) {
        if (this.aBoolean602) {
            this.method7004(arg0);
            this.method7036(arg0);
        } else if (this.anInt7991 >= 3) {
            throw new RuntimeException();
        } else {
            if (this.anInt7991 >= 0) {
                this.anInterface14Array3[this.anInt7991].method9358();
            }
            this.anInterface14_1 = this.anInterface14_2 = this.anInterface14Array3[++this.anInt7991] = arg0;
            this.anInterface14_1.method9359();
        }
    }

    @OriginalMember(owner = "client!qha", name = "i", descriptor = "(II)V")
    public synchronized void method6968(@OriginalArg(0) int arg0) {
        @Pc(19) IntNode local19 = new IntNode(arg0);
        this.aDeque_50.addLast(local19);
    }

    @OriginalMember(owner = "client!qha", name = "v", descriptor = "(I)V")
    public void method6969() {
        this.aFloat134 = (float) (this.anInt8018 - this.anInt8009) - this.aFloat147;
        this.aFloat132 = this.aFloat134 - this.aFloat139 * (float) this.anInt8008;
        if (this.aFloat132 < (float) this.anInt8010) {
            this.aFloat132 = (float) this.anInt8010;
        }
        OpenGL.glFogf(OpenGL.GL_FOG_START, this.aFloat132);
        OpenGL.glFogf(OpenGL.GL_FOG_END, this.aFloat134);
        Static476.aFloatArray46[0] = (float) (this.anInt8002 & 0xFF0000) / 1.671168E7F;
        Static476.aFloatArray46[2] = (float) (this.anInt8002 & 0xFF) / 255.0F;
        Static476.aFloatArray46[1] = (float) (this.anInt8002 & 0xFF00) / 65280.0F;
        OpenGL.glFogfv(OpenGL.GL_FOG_COLOR, Static476.aFloatArray46, 0);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(FFIF)V")
    public void method6970(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(3) float arg2) {
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        if (this.aBoolean599) {
            OpenGL.glLoadIdentity();
        }
        OpenGL.glTranslatef(arg2, arg1, arg0);
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        this.aBoolean599 = true;
    }

    @OriginalMember(owner = "client!qha", name = "d", descriptor = "(III)V")
    public synchronized void method6971(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(8) IntNode local8 = new IntNode(arg0);
        local8.key = arg1;
        this.aDeque_48.addLast(local8);
    }

    @OriginalMember(owner = "client!qha", name = "KA", descriptor = "(IIII)V")
    @Override
    public void KA(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2) {
        if (x1 < 0) {
            x1 = 0;
        }
        if (this.anInt7869 < x2) {
            x2 = this.anInt7869;
        }
        if (y2 > this.anInt7956) {
            y2 = this.anInt7956;
        }
        if (y1 < 0) {
            y1 = 0;
        }
        this.anInt8000 = x1;
        this.anInt8028 = x2;
        this.anInt8012 = y2;
        this.anInt8032 = y1;
        OpenGL.glEnable(OpenGL.GL_SCISSOR_TEST);
        this.method7037();
        this.method7000();
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public void addCanvas(@OriginalArg(0) Canvas canvas, @OriginalArg(1) int width, @OriginalArg(2) int height) {
        if (this.aCanvas10 == canvas) {
            throw new RuntimeException();
        } else if (!this.aHashtable5.containsKey(canvas)) {
            if (!canvas.isShowing()) {
                throw new RuntimeException();
            }
            try {
                @Pc(32) Class local32 = Class.forName("java.awt.Canvas");
                @Pc(44) Method local44 = local32.getMethod("setIgnoreRepaint", Boolean.TYPE);
                local44.invoke(canvas, Boolean.TRUE);
            } catch (@Pc(56) Exception local56) {
            }
            @Pc(61) long local61 = this.anOpenGL1.prepareSurface(canvas);
            if (local61 == -1L) {
                throw new RuntimeException();
            }
            this.aHashtable5.put(canvas, Long.valueOf(local61));
        }
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(IZ)V")
    public void method6972(@OriginalArg(1) boolean arg0) {
        if (arg0 != this.aBoolean590) {
            this.aBoolean590 = arg0;
            this.method7032();
            this.anInt7997 &= 0xFFFFFFE0;
        }
    }

    @OriginalMember(owner = "client!qha", name = "r", descriptor = "(I)V")
    public void method6974() {
        if (this.anInt7997 == 4) {
            return;
        }
        this.method7003();
        this.method7035(false);
        this.method7008(false);
        this.method7006(false);
        this.method6972(false);
        this.method7046(-2);
        this.setBlendMode(1);
        this.anInt7997 = 4;
    }

    @OriginalMember(owner = "client!qha", name = "x", descriptor = "()Z")
    @Override
    public boolean method7937() {
        return true;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void line(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        this.enter2dMode();
        this.setBlendMode(mode);
        @Pc(16) float local16 = (float) x2 - (float) x1;
        @Pc(23) float local23 = (float) -y1 + (float) y2;
        if (local16 == 0.0F && local23 == 0.0F) {
            local16 = 1.0F;
        } else {
            @Pc(46) float local46 = (float) (1.0D / Math.sqrt(local23 * local23 + local16 * local16));
            local16 *= local46;
            local23 *= local46;
        }
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        OpenGL.glBegin(OpenGL.GL_LINES);
        OpenGL.glVertex2f((float) x1 + 0.35F, (float) y1 + 0.35F);
        OpenGL.glVertex2f(local16 + (float) x2 + 0.35F, (float) y2 + local23 + 0.35F);
        OpenGL.glEnd();
    }

    @OriginalMember(owner = "client!qha", name = "xa", descriptor = "(F)V")
    @Override
    public void xa(@OriginalArg(0) float globalAmbient) {
        if (this.aFloat149 != globalAmbient) {
            this.aFloat149 = globalAmbient;
            this.method7025();
        }
    }

    @OriginalMember(owner = "client!qha", name = "q", descriptor = "()V")
    @Override
    public void cacheReset() {
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ZZI)V")
    public void method6975(@OriginalArg(0) boolean arg0, @OriginalArg(2) int arg1) {
        this.method7015(arg1, arg0, true);
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(Z)V")
    public void method6976() {
        @Pc(27) float local27 = (float) -this.anInt8021 * this.aFloat146 / (float) this.anInt8001;
        @Pc(39) float local39 = (float) -this.anInt8016 * this.aFloat146 / (float) this.anInt8025;
        @Pc(54) float local54 = (float) (this.anInt7869 - this.anInt8021) * this.aFloat146 / (float) this.anInt8001;
        @Pc(68) float local68 = (float) (this.anInt7956 - this.anInt8016) * this.aFloat146 / (float) this.anInt8025;
        OpenGL.glMatrixMode(OpenGL.GL_PROJECTION);
        OpenGL.glLoadIdentity();
        if (local54 != local27 && local68 != local39) {
            OpenGL.glOrtho(local27, local54, -local68, -local39, this.anInt8010, this.anInt8018);
        }
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
    }

    @OriginalMember(owner = "client!qha", name = "j", descriptor = "(I)V")
    @Override
    public void allocateThreads(@OriginalArg(0) int arg0) {
        if (arg0 != 1) {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!qha", name = "d", descriptor = "()V")
    @Override
    public void method7974() {
        this.aClass276_1.method6245();
    }

    @OriginalMember(owner = "client!qha", name = "XA", descriptor = "()I")
    @Override
    public int XA() {
        return this.anInt8018;
    }

    @OriginalMember(owner = "client!qha", name = "f", descriptor = "(I)V")
    @Override
    public void setTextureSize(@OriginalArg(0) int size) {
        if (size < 128 || size > 1024) {
            throw new IllegalArgumentException();
        }
        this.anInt7981 = size;
        this.aClass169_1.method3527();
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(IIIID)V")
    @Override
    public void b(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) double zDepth) {
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(II)I")
    @Override
    public int compareFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        return maskB ^ maskB & maskA;
    }

    @OriginalMember(owner = "client!qha", name = "Y", descriptor = "()[I")
    @Override
    public int[] Y() {
        return new int[]{this.anInt8021, this.anInt8016, this.anInt8001, this.anInt8025};
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIZ)Lclient!st;")
    @Override
    public Sprite createSprite(@OriginalArg(0) int with, @OriginalArg(1) int height, @OriginalArg(2) boolean transparent) {
        return new Sprite_Sub2(this, with, height, transparent);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIZ)Lclient!st;")
    @Override
    public Sprite createSprite(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) boolean arg4) {
        return new Sprite_Sub2(this, arg0, arg1, arg2, arg3);
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(IZ)I")
    public int method6977(@OriginalArg(0) int arg0) {
        if (arg0 == 1) {
            return 7681;
        } else if (arg0 == 0) {
            return 8448;
        } else if (arg0 == 2) {
            return 34165;
        } else if (arg0 == 3) {
            return 260;
        } else if (arg0 == 4) {
            return 34023;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!qha", name = "JA", descriptor = "(IIIIII)I")
    @Override
    public int JA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(7) int local7 = 0;
        @Pc(32) float local32 = this.aClass73_Sub3_3.aFloat159 + ((float) arg1 * this.aClass73_Sub3_3.aFloat151 + (float) arg0 * this.aClass73_Sub3_3.aFloat155 + this.aClass73_Sub3_3.aFloat154 * (float) arg2);
        if (local32 < 1.0F) {
            local32 = 1.0F;
        }
        @Pc(63) float local63 = (float) arg3 * this.aClass73_Sub3_3.aFloat155 + (float) arg4 * this.aClass73_Sub3_3.aFloat151 + (float) arg5 * this.aClass73_Sub3_3.aFloat154 + this.aClass73_Sub3_3.aFloat159;
        if (local63 < 1.0F) {
            local63 = 1.0F;
        }
        if ((float) this.anInt8010 > local32 && local63 < (float) this.anInt8010) {
            local7 |= 0x10;
        } else if (local32 > (float) this.anInt8018 && local63 > (float) this.anInt8018) {
            local7 |= 0x20;
        }
        @Pc(141) int local141 = (int) ((float) this.anInt8001 * (this.aClass73_Sub3_3.aFloat152 + ((float) arg0 * this.aClass73_Sub3_3.aFloat153 + (float) arg1 * this.aClass73_Sub3_3.aFloat157 + this.aClass73_Sub3_3.aFloat160 * (float) arg2)) / local32);
        @Pc(173) int local173 = (int) ((float) this.anInt8001 * (this.aClass73_Sub3_3.aFloat152 + (this.aClass73_Sub3_3.aFloat153 * (float) arg3 + this.aClass73_Sub3_3.aFloat157 * (float) arg4 + this.aClass73_Sub3_3.aFloat160 * (float) arg5)) / local63);
        if (this.aFloat145 > (float) local141 && this.aFloat145 > (float) local173) {
            local7 |= 0x1;
        } else if (this.aFloat138 < (float) local141 && this.aFloat138 < (float) local173) {
            local7 |= 0x2;
        }
        @Pc(245) int local245 = (int) (((float) arg0 * this.aClass73_Sub3_3.aFloat161 + (float) arg1 * this.aClass73_Sub3_3.aFloat156 + this.aClass73_Sub3_3.aFloat150 * (float) arg2 + this.aClass73_Sub3_3.aFloat158) * (float) this.anInt8025 / local32);
        @Pc(277) int local277 = (int) (((float) arg3 * this.aClass73_Sub3_3.aFloat161 + this.aClass73_Sub3_3.aFloat156 * (float) arg4 + (float) arg5 * this.aClass73_Sub3_3.aFloat150 + this.aClass73_Sub3_3.aFloat158) * (float) this.anInt8025 / local63);
        if ((float) local245 < this.aFloat141 && (float) local277 < this.aFloat141) {
            local7 |= 0x4;
        } else if ((float) local245 > this.aFloat133 && this.aFloat133 < (float) local277) {
            local7 |= 0x8;
        }
        return local7;
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(I)V")
    @Override
    public void method8003() {
    }

    @OriginalMember(owner = "client!qha", name = "u", descriptor = "(I)V")
    public void method6978() {
        if (this.anInt7997 == 16) {
            return;
        }
        this.method7043();
        this.method7035(true);
        this.method7006(true);
        this.method6972(true);
        this.setBlendMode(1);
        this.anInt7997 = 16;
    }

    @OriginalMember(owner = "client!qha", name = "aa", descriptor = "(IIIIII)V")
    @Override
    public void aa(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        @Pc(8) float local8 = (float) x + 0.35F;
        @Pc(13) float local13 = (float) y + 0.35F;
        @Pc(18) float local18 = local8 + (float) width;
        this.enter2dMode();
        @Pc(26) float local26 = (float) height + local13;
        this.setBlendMode(mode);
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        if (this.aBoolean615) {
            OpenGL.glDisable(OpenGL.GL_MULTISAMPLE);
        }
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glVertex2f(local8, local13);
        OpenGL.glVertex2f(local8, local26);
        OpenGL.glVertex2f(local18, local26);
        OpenGL.glVertex2f(local18, local13);
        OpenGL.glEnd();
        if (this.aBoolean615) {
            OpenGL.glEnable(OpenGL.GL_MULTISAMPLE);
        }
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(II)I")
    @Override
    public int combineFunctionMasks(@OriginalArg(0) int maskA, @OriginalArg(1) int maskB) {
        return maskA | maskB;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!lk;I)V")
    @Override
    public void renderOrtho(@OriginalArg(0) ParticleList particleList, @OriginalArg(1) int zoom) {
        this.particleRenderer.render(this, particleList, zoom);
    }

    @OriginalMember(owner = "client!qha", name = "w", descriptor = "()Z")
    @Override
    public boolean bloom() {
        return this.aClass2_Sub31_Sub1_1 != null && this.aClass2_Sub31_Sub1_1.method4616();
    }

    @OriginalMember(owner = "client!qha", name = "A", descriptor = "(ILclient!aa;II)V")
    @Override
    public void A(@OriginalArg(0) int colour, @OriginalArg(1) ClippingMask clippingMask, @OriginalArg(2) int x, @OriginalArg(3) int y) {
        @Pc(6) ClippingMask_Sub3 local6 = (ClippingMask_Sub3) clippingMask;
        @Pc(9) Class93_Sub2_Sub1 local9 = local6.aClass93_Sub2_Sub1_5;
        this.method7018();
        this.method7001(local6.aClass93_Sub2_Sub1_5);
        this.setBlendMode(1);
        this.method7031(8448, 7681);
        this.method7021(34167, 768, 0);
        @Pc(39) float local39 = local9.aFloat67 / (float) local9.anInt3259;
        @Pc(46) float local46 = local9.aFloat68 / (float) local9.anInt3257;
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glTexCoord2f((float) (this.anInt8000 - x) * local39, (float) (this.anInt8032 - y) * local46);
        OpenGL.glVertex2i(this.anInt8000, this.anInt8032);
        OpenGL.glTexCoord2f(local39 * (float) (this.anInt8000 - x), (float) (this.anInt8012 - y) * local46);
        OpenGL.glVertex2i(this.anInt8000, this.anInt8012);
        OpenGL.glTexCoord2f(local39 * (float) (this.anInt8028 - x), (float) (this.anInt8012 - y) * local46);
        OpenGL.glVertex2i(this.anInt8028, this.anInt8012);
        OpenGL.glTexCoord2f(local39 * (float) (this.anInt8028 - x), local46 * (float) (this.anInt8032 - y));
        OpenGL.glVertex2i(this.anInt8028, this.anInt8032);
        OpenGL.glEnd();
        this.method7021(5890, 768, 0);
    }

    @OriginalMember(owner = "client!qha", name = "u", descriptor = "()V")
    @Override
    protected void stop() {
        for (@Pc(8) Node local8 = this.aDeque_46.first(); local8 != null; local8 = this.aDeque_46.next()) {
            ((GlMemoryPool) local8).deallocate();
        }
        if (this.aClass276_1 != null) {
            this.aClass276_1.method6249();
        }
        if (this.anOpenGL1 != null) {
            this.method7045();
            @Pc(41) Enumeration local41 = this.aHashtable5.keys();
            while (local41.hasMoreElements()) {
                @Pc(46) Canvas local46 = (Canvas) local41.nextElement();
                @Pc(52) Long local52 = (Long) this.aHashtable5.get(local46);
                this.anOpenGL1.releaseSurface(local46, local52);
            }
            this.anOpenGL1.release();
            this.anOpenGL1 = null;
        }
        if (this.aBoolean589) {
            ColourUtils.destroy(false, true);
            this.aBoolean589 = false;
        }
    }

    @OriginalMember(owner = "client!qha", name = "h", descriptor = "()V")
    @Override
    public void method7969() {
        if (!this.aBoolean607 || this.anInt7869 <= 0 || this.anInt7956 <= 0) {
            return;
        }
        @Pc(24) int local24 = this.anInt8000;
        @Pc(27) int local27 = this.anInt8028;
        @Pc(30) int local30 = this.anInt8032;
        @Pc(33) int local33 = this.anInt8012;
        this.la();
        OpenGL.glReadBuffer(OpenGL.GL_FRONT);
        OpenGL.glDrawBuffer(OpenGL.GL_BACK);
        this.method6981();
        this.method7035(false);
        this.method7008(false);
        this.method7006(false);
        this.method6972(false);
        this.method7001(null);
        this.method7046(-2);
        this.method6991(1);
        this.setBlendMode(0);
        OpenGL.glMatrixMode(OpenGL.GL_PROJECTION);
        OpenGL.glLoadIdentity();
        OpenGL.glOrtho(0.0D, 1.0D, 0.0D, 1.0D, -1.0D, 1.0D);
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        OpenGL.glLoadIdentity();
        OpenGL.glRasterPos2i(0, 0);
        OpenGL.glCopyPixels(0, 0, this.anInt7869, this.anInt7956, OpenGL.GL_COLOR);
        OpenGL.glFlush();
        OpenGL.glReadBuffer(OpenGL.GL_BACK);
        OpenGL.glDrawBuffer(OpenGL.GL_BACK);
        this.KA(local24, local30, local27, local33);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(FFFFI)V")
    public void method6979(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2, @OriginalArg(3) float arg3) {
        Static476.aFloatArray46[3] = arg2;
        Static476.aFloatArray46[1] = arg3;
        Static476.aFloatArray46[0] = arg1;
        Static476.aFloatArray46[2] = arg0;
        OpenGL.glTexEnvfv(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_TEXTURE_ENV_COLOR, Static476.aFloatArray46, 0);
    }

    @OriginalMember(owner = "client!qha", name = "f", descriptor = "(B)V")
    public void method6980() {
        @Pc(7) int local7;
        for (local7 = 0; local7 < this.anInt8031; local7++) {
            @Pc(14) PointLight local14 = this.aClass2_Sub7Array5[local7];
            @Pc(18) int local18 = local7 + 16386;
            Static376.aFloatArray42[0] = (float) local14.getX();
            Static376.aFloatArray42[1] = (float) local14.getY();
            Static376.aFloatArray42[2] = (float) local14.getZ();
            Static376.aFloatArray42[3] = 1.0F;
            OpenGL.glLightfv(local18, OpenGL.GL_POSITION, Static376.aFloatArray42, 0);
            @Pc(52) int local52 = local14.method8431();
            @Pc(60) float local60 = local14.getIntensity() / 255.0F;
            Static376.aFloatArray42[0] = (float) (local52 >> 16 & 0xFF) * local60;
            Static376.aFloatArray42[2] = (float) (local52 & 0xFF) * local60;
            Static376.aFloatArray42[1] = local60 * (float) (local52 >> 8 & 0xFF);
            OpenGL.glLightfv(local18, OpenGL.GL_DIFFUSE, Static376.aFloatArray42, 0);
            OpenGL.glLightf(local18, OpenGL.GL_QUADRATIC_ATTENUATION, 1.0F / (float) (local14.getRange() * local14.getRange()));
            OpenGL.glEnable(local18);
        }
        while (this.anInt8017 > local7) {
            OpenGL.glDisable(local7 + OpenGL.GL_LIGHT2);
            local7++;
        }
        if (85 == 85) {
            this.anInt8017 = this.anInt8031;
        }
    }

    @OriginalMember(owner = "client!qha", name = "ya", descriptor = "()V")
    @Override
    public void ya() {
        this.method6972(true);
        OpenGL.glClear(OpenGL.GL_DEPTH_BUFFER_BIT);
    }

    @OriginalMember(owner = "client!qha", name = "d", descriptor = "(I)V")
    public void method6981() {
        if (this.anInt8005 != 0) {
            this.anInt7997 &= 0xFFFFFFE0;
            this.anInt8005 = 0;
        }
    }

    @OriginalMember(owner = "client!qha", name = "x", descriptor = "(I)V")
    public void method6982() {
        @Pc(13) int local13 = 0;
        while (!this.anOpenGL1.b()) {
            if (local13++ > 5) {
                throw new RuntimeException("");
            }
            TimeUtils.sleep(1000L);
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!wp;Z)Lclient!st;")
    @Override
    public Sprite createSprite(@OriginalArg(0) IndexedImage arg0, @OriginalArg(1) boolean arg1) {
        @Pc(12) int[] local12 = new int[arg0.height * arg0.width];
        @Pc(14) int local14 = 0;
        @Pc(16) int local16 = 0;
        @Pc(21) int local21;
        @Pc(25) int local25;
        if (arg0.alpha == null) {
            for (local21 = 0; local21 < arg0.height; local21++) {
                for (local25 = 0; local25 < arg0.width; local25++) {
                    @Pc(91) int local91 = arg0.palette[arg0.raster[local14++] & 0xFF];
                    local12[local16++] = local91 == 0 ? 0 : local91 | 0xFF000000;
                }
            }
        } else {
            for (local21 = 0; local21 < arg0.height; local21++) {
                for (local25 = 0; local25 < arg0.width; local25++) {
                    local12[local16++] = arg0.palette[arg0.raster[local14] & 0xFF] | arg0.alpha[local14] << 24;
                    local14++;
                }
            }
        }
        @Pc(127) Sprite local127 = this.createSprite(arg0.width, arg0.width, arg0.height, local12);
        local127.setOffsets(arg0.offX1, arg0.offY1, arg0.offX2, arg0.offY2);
        return local127;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IBI)V")
    public synchronized void method6983(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        @Pc(18) IntNode local18 = new IntNode(arg1);
        local18.key = arg0;
        this.aDeque_51.addLast(local18);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!dv;IIII)Lclient!ka;")
    @Override
    public Model createModel(@OriginalArg(0) Mesh mesh, @OriginalArg(1) int functionMask, @OriginalArg(2) int featureMask, @OriginalArg(3) int ambient, @OriginalArg(4) int contrast) {
        return new Model_Sub2(this, mesh, functionMask, ambient, contrast, featureMask);
    }

    @OriginalMember(owner = "client!qha", name = "e", descriptor = "()I")
    @Override
    public int getMaxLights() {
        return 4;
    }

    @OriginalMember(owner = "client!qha", name = "l", descriptor = "(I)V")
    public void method6984() {
        this.method7046(-2);
        for (@Pc(12) int local12 = this.anInt8003 - 1; local12 >= 0; local12--) {
            this.method7014(local12);
            this.method7001(null);
            OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_TEXTURE_ENV_MODE, OpenGL.GL_COMBINE);
        }
        this.method7031(8448, 8448);
        this.method7021(34168, 770, 2);
        this.method6999();
        this.anInt7998 = 1;
        OpenGL.glEnable(OpenGL.GL_BLEND);
        OpenGL.glBlendFunc(OpenGL.GL_SRC_ALPHA, OpenGL.GL_ONE_MINUS_SRC_ALPHA);
        this.anInt7996 = 1;
        OpenGL.glEnable(OpenGL.GL_ALPHA_TEST);
        OpenGL.glAlphaFunc(OpenGL.GL_GREATER, 0.0F);
        this.aBoolean592 = true;
        OpenGL.glColorMask(true, true, true, true);
        this.aBoolean593 = true;
        this.method7035(true);
        this.method7008(true);
        this.method7006(true);
        this.method6972(true);
        this.method6981();
        this.anOpenGL1.setSwapInterval(0);
        OpenGL.glShadeModel(OpenGL.GL_SMOOTH);
        OpenGL.glClearDepth(1.0F);
        OpenGL.glDepthFunc(OpenGL.GL_LEQUAL);
        OpenGL.glPolygonMode(OpenGL.GL_FRONT, OpenGL.GL_FILL);
        OpenGL.glEnable(OpenGL.GL_CULL_FACE);
        OpenGL.glCullFace(OpenGL.GL_BACK);
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        OpenGL.glLoadIdentity();
        OpenGL.glColorMaterial(OpenGL.GL_FRONT, OpenGL.GL_AMBIENT_AND_DIFFUSE);
        OpenGL.glEnable(OpenGL.GL_COLOR_MATERIAL);
        @Pc(124) float[] local124 = new float[]{0.0F, 0.0F, 0.0F, 1.0F};
        for (@Pc(126) int local126 = 0; local126 < 8; local126++) {
            @Pc(131) int local131 = local126 + 16384;
            OpenGL.glLightfv(local131, OpenGL.GL_AMBIENT, local124, 0);
            OpenGL.glLightf(local131, OpenGL.GL_CONSTANT_ATTENUATION, 0.0F);
            OpenGL.glLightf(local131, OpenGL.GL_LINEAR_ATTENUATION, 0.0F);
        }
        OpenGL.glEnable(OpenGL.GL_LIGHT0);
        OpenGL.glEnable(OpenGL.GL_LIGHT1);
        OpenGL.glFogf(OpenGL.GL_FOG_DENSITY, 0.95F);
        OpenGL.glFogi(OpenGL.GL_FOG_MODE, OpenGL.GL_LINEAR);
        OpenGL.glHint(OpenGL.GL_FOG_HINT, OpenGL.GL_FASTEST);
        this.anInt8011 = this.anInt8002 = -1;
        this.la();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IB)V")
    public void method6985(@OriginalArg(0) int arg0) {
        Static476.aFloatArray46[1] = (float) (arg0 & 0xFF00) / 65280.0F;
        Static476.aFloatArray46[0] = (float) (arg0 & 0xFF0000) / 1.671168E7F;
        Static476.aFloatArray46[3] = (float) (arg0 >>> 24) / 255.0F;
        Static476.aFloatArray46[2] = (float) (arg0 & 0xFF) / 255.0F;
        OpenGL.glTexEnvfv(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_TEXTURE_ENV_COLOR, Static476.aFloatArray46, 0);
    }

    @OriginalMember(owner = "client!qha", name = "d", descriptor = "(II)Lclient!wja;")
    @Override
    public DepthBuffer method7986(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return null;
    }

    @OriginalMember(owner = "client!qha", name = "A", descriptor = "(I)V")
    public void method6986() {
        OpenGL.glViewport(this.anInt8007, this.anInt8014, this.anInt7869, this.anInt7956);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(B)V")
    public void method6987() {
        OpenGL.glLightfv(OpenGL.GL_LIGHT0, OpenGL.GL_POSITION, this.aFloatArray51, 0);
        OpenGL.glLightfv(OpenGL.GL_LIGHT1, OpenGL.GL_POSITION, this.aFloatArray54, 0);
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(ILclient!jf;)V")
    public void method6988(@OriginalArg(1) Interface14 arg0) {
        if (this.anInt7992 < 0 || this.anInterface14Array1[this.anInt7992] != arg0) {
            throw new RuntimeException();
        }
        this.anInterface14Array1[this.anInt7992--] = null;
        arg0.method9361();
        if (this.anInt7992 >= 0) {
            this.anInterface14_1 = this.anInterface14Array1[this.anInt7992];
            this.anInterface14_1.method9360();
        } else {
            this.anInterface14_1 = null;
        }
    }

    @OriginalMember(owner = "client!qha", name = "GA", descriptor = "(I)V")
    @Override
    public void GA(@OriginalArg(0) int colour) {
        this.setBlendMode(0);
        OpenGL.glClearColor((float) (colour & 0xFF0000) / 1.671168E7F, (float) (colour & 0xFF00) / 65280.0F, (float) (colour & 0xFF) / 255.0F, (float) (colour >>> 24) / 255.0F);
        OpenGL.glClear(OpenGL.GL_COLOR_BUFFER_BIT);
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(I)V")
    @Override
    public void method8016(@OriginalArg(0) int arg0) {
        this.method7045();
    }

    @OriginalMember(owner = "client!qha", name = "m", descriptor = "()Z")
    @Override
    public boolean hasBloom() {
        if (this.aClass2_Sub31_Sub1_1 == null) {
            return false;
        }
        if (!this.aClass2_Sub31_Sub1_1.method4616()) {
            if (!this.aClass276_1.method6246(this.aClass2_Sub31_Sub1_1)) {
                return false;
            }
            this.aClass169_1.method3527();
        }
        return true;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!za;)V")
    @Override
    public void method7938(@OriginalArg(0) MemoryPool arg0) {
        this.lb = ((GlMemoryPool) arg0).heap;
        if (this.anInterface12_7 != null) {
            return;
        }
        @Pc(16) Node_Sub21_Sub1 local16 = new Node_Sub21_Sub1(80);
        if (this.aBoolean618) {
            local16.method4336(-1.0F);
            local16.method4336(-1.0F);
            local16.method4336(0.0F);
            local16.method4336(0.0F);
            local16.method4336(1.0F);
            local16.method4336(1.0F);
            local16.method4336(-1.0F);
            local16.method4336(0.0F);
            local16.method4336(1.0F);
            local16.method4336(1.0F);
            local16.method4336(1.0F);
            local16.method4336(1.0F);
            local16.method4336(0.0F);
            local16.method4336(1.0F);
            local16.method4336(0.0F);
            local16.method4336(-1.0F);
            local16.method4336(1.0F);
            local16.method4336(0.0F);
            local16.method4336(0.0F);
            local16.method4336(0.0F);
        } else {
            local16.method4337(-1.0F);
            local16.method4337(-1.0F);
            local16.method4337(0.0F);
            local16.method4337(0.0F);
            local16.method4337(1.0F);
            local16.method4337(1.0F);
            local16.method4337(-1.0F);
            local16.method4337(0.0F);
            local16.method4337(1.0F);
            local16.method4337(1.0F);
            local16.method4337(1.0F);
            local16.method4337(1.0F);
            local16.method4337(0.0F);
            local16.method4337(1.0F);
            local16.method4337(0.0F);
            local16.method4337(-1.0F);
            local16.method4337(1.0F);
            local16.method4337(0.0F);
            local16.method4337(0.0F);
            local16.method4337(0.0F);
        }
        this.anInterface12_7 = this.method7024(false, local16.pos, local16.data, 20);
        this.aClass94_15 = new Class94(this.anInterface12_7, 5126, 3, 0);
        this.aClass94_16 = new Class94(this.anInterface12_7, 5126, 2, 12);
        this.particleRenderer.createVertexBuffer(this);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(FFF)V")
    @Override
    public void method7993(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2) {
        Static228.aFloat72 = arg0;
        Static656.aFloat127 = arg2;
        Static626.aFloat199 = arg1;
    }

    @OriginalMember(owner = "client!qha", name = "d", descriptor = "(Z)V")
    public void method6989() {
        this.aFloatArray53[10] = this.aFloat131;
        this.aFloatArray53[14] = this.aFloat140;
        this.aFloat144 = (this.aFloatArray53[14] - (float) this.anInt8018) / this.aFloatArray53[10];
        this.aFloat142 = (float) this.anInt8018;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!pu;)V")
    @Override
    public void method7973(@OriginalArg(0) Class67 arg0) {
        this.aClass67_Sub1_1 = (Class67_Sub1) arg0;
    }

    @OriginalMember(owner = "client!qha", name = "p", descriptor = "(I)V")
    public void method6990() {
        @Pc(6) float[] local6 = this.aFloatArray53;
        @Pc(18) float local18 = (float) (this.anInt8010 * -this.anInt8021) / (float) this.anInt8001;
        @Pc(33) float local33 = (float) (this.anInt8010 * (this.anInt7869 - this.anInt8021)) / (float) this.anInt8001;
        @Pc(52) float local52 = (float) (this.anInt8016 * this.anInt8010) / (float) this.anInt8025;
        @Pc(66) float local66 = (float) (this.anInt8010 * (this.anInt8016 - this.anInt7956)) / (float) this.anInt8025;
        if (local33 == local18 || local66 == local52) {
            local6[7] = 0.0F;
            local6[0] = 1.0F;
            local6[5] = 1.0F;
            local6[15] = 1.0F;
            local6[1] = 0.0F;
            local6[4] = 0.0F;
            local6[10] = 1.0F;
            local6[9] = 0.0F;
            local6[12] = 0.0F;
            local6[8] = 0.0F;
            local6[14] = 0.0F;
            local6[3] = 0.0F;
            local6[2] = 0.0F;
            local6[11] = 0.0F;
            local6[6] = 0.0F;
            local6[13] = 0.0F;
        } else {
            @Pc(82) float local82 = (float) this.anInt8010 * 2.0F;
            local6[4] = 0.0F;
            local6[13] = 0.0F;
            local6[10] = this.aFloat131 = (float) -(this.anInt8010 + this.anInt8018) / (float) (this.anInt8018 - this.anInt8010);
            local6[6] = 0.0F;
            local6[11] = -1.0F;
            local6[1] = 0.0F;
            local6[0] = local82 / (local33 - local18);
            local6[12] = 0.0F;
            local6[8] = (local33 + local18) / (local33 - local18);
            local6[15] = 0.0F;
            local6[9] = (local52 + local66) / (-local66 + local52);
            local6[2] = 0.0F;
            local6[7] = 0.0F;
            local6[3] = 0.0F;
            local6[14] = this.aFloat140 = -(local82 * (float) this.anInt8018) / (float) (this.anInt8018 - this.anInt8010);
            local6[5] = local82 / (local52 - local66);
        }
        this.method6989();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(BI)V")
    public void method6991(@OriginalArg(1) int arg0) {
        if (arg0 == 1) {
            this.method7031(7681, 7681);
        } else if (arg0 == 0) {
            this.method7031(8448, 8448);
        } else if (arg0 == 2) {
            this.method7031(7681, 34165);
        } else if (arg0 == 3) {
            this.method7031(8448, 260);
        } else if (arg0 == 4) {
            this.method7031(34023, 34023);
        }
    }

    @OriginalMember(owner = "client!qha", name = "pa", descriptor = "()V")
    @Override
    public void pa() {
        this.aBoolean605 = false;
    }

    @OriginalMember(owner = "client!qha", name = "t", descriptor = "(I)V")
    public void method6992() {
        this.aClass93Array1 = new Class93[this.anInt8003];
        this.aClass93_Sub2_5 = new Class93_Sub2(this, 3553, 6408, 1, 1);
        new Class93_Sub2(this, 3553, 6408, 1, 1);
        new Class93_Sub2(this, 3553, 6408, 1, 1);
        for (@Pc(42) int local42 = 0; local42 < 7; local42++) {
            this.aClass114_Sub2Array2[local42] = new Model_Sub2(this);
            this.aClass114_Sub2Array1[local42] = new Model_Sub2(this);
        }
        if (this.aBoolean613) {
            this.aClass406_7 = new Class406(this);
            new Class406(this);
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!jf;B)V")
    public void method6993(@OriginalArg(0) Interface14 arg0) {
        if (this.aBoolean602) {
            this.method6988(arg0);
            this.method7038(arg0);
        } else if (this.anInt7991 >= 0 && arg0 == this.anInterface14Array3[this.anInt7991]) {
            this.anInterface14Array3[this.anInt7991--] = null;
            arg0.method9358();
            if (this.anInt7991 < 0) {
                this.anInterface14_1 = this.anInterface14_2 = null;
            } else {
                this.anInterface14_1 = this.anInterface14_2 = this.anInterface14Array3[this.anInt7991];
                this.anInterface14_1.method9359();
            }
        } else {
            throw new RuntimeException();
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!lk;)V")
    @Override
    public void render(@OriginalArg(0) ParticleList particleList) {
        this.particleRenderer.render(this, particleList, -1);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(BFF)V")
    public void method6995(@OriginalArg(1) float arg0, @OriginalArg(2) float arg1) {
        this.aFloat147 = arg1;
        this.aFloat139 = arg0;
        this.method6969();
    }

    @OriginalMember(owner = "client!qha", name = "D", descriptor = "(I)V")
    public void method6996() {
        OpenGL.glPopMatrix();
    }

    @OriginalMember(owner = "client!qha", name = "z", descriptor = "()Z")
    @Override
    public boolean method7990() {
        return this.aClass98_1.method2357();
    }

    @OriginalMember(owner = "client!qha", name = "q", descriptor = "(I)I")
    public int method6997() {
        this.aString98 = OpenGL.glGetString(OpenGL.GL_VENDOR).toLowerCase();
        @Pc(10) int local10 = 0;
        this.aString99 = OpenGL.glGetString(OpenGL.GL_RENDERER).toLowerCase();
        if (this.aString98.indexOf("microsoft") != -1) {
            local10 = 1;
        }
        if (this.aString98.indexOf("brian paul") != -1 || this.aString98.indexOf("mesa") != -1) {
            local10 |= 0x1;
        }
        @Pc(55) String local55 = OpenGL.glGetString(OpenGL.GL_VERSION);
        @Pc(63) String[] local63 = StringTools.split(local55.replace('.', ' '), ' ');
        if (local63.length >= 2) {
            try {
                @Pc(73) int local73 = StringTools.parseDecimal(local63[0]);
                @Pc(79) int local79 = StringTools.parseDecimal(local63[1]);
                this.anInt8020 = local73 * 10 + local79;
            } catch (@Pc(88) NumberFormatException local88) {
                local10 |= 0x4;
            }
        } else {
            local10 |= 0x4;
        }
        if (this.anInt8020 < 12) {
            local10 |= 0x2;
        }
        if (!this.anOpenGL1.a("GL_ARB_multitexture")) {
            local10 |= 0x8;
        }
        if (!this.anOpenGL1.a("GL_ARB_texture_env_combine")) {
            local10 |= 0x20;
        }
        @Pc(130) int[] local130 = new int[1];
        OpenGL.glGetIntegerv(OpenGL.GL_MAX_TEXTURE_UNITS, local130, 0);
        this.anInt8003 = local130[0];
        OpenGL.glGetIntegerv(OpenGL.GL_MAX_TEXTURE_COORDS, local130, 0);
        this.anInt7999 = local130[0];
        OpenGL.glGetIntegerv(OpenGL.GL_MAX_TEXTURE_IMAGE_UNITS, local130, 0);
        this.anInt8004 = local130[0];
        if (this.anInt8003 < 2 || this.anInt7999 < 2 || this.anInt8004 < 2) {
            local10 |= 0x10;
        }
        this.aBoolean618 = Stream.b();
        this.aBoolean612 = this.anOpenGL1.arePbuffersAvailable();
        this.aBoolean600 = this.anOpenGL1.a("GL_ARB_vertex_buffer_object");
        this.aBoolean615 = this.anOpenGL1.a("GL_ARB_multisample");
        this.aBoolean608 = this.anOpenGL1.a("GL_ARB_vertex_program");
        this.anOpenGL1.a("GL_ARB_fragment_program");
        this.bf = this.anOpenGL1.a("GL_ARB_vertex_shader");
        this.aBoolean619 = this.anOpenGL1.a("GL_ARB_fragment_shader");
        this.aBoolean606 = this.anOpenGL1.a("GL_EXT_texture3D");
        this.aBoolean595 = this.anOpenGL1.a("GL_ARB_texture_rectangle");
        this.aBoolean598 = this.anOpenGL1.a("GL_ARB_texture_cube_map");
        this.aBoolean603 = this.anOpenGL1.a("GL_ARB_texture_float");
        this.aBoolean597 = false;
        this.aBoolean613 = this.anOpenGL1.a("GL_EXT_framebuffer_object");
        this.aBoolean602 = this.anOpenGL1.a("GL_EXT_framebuffer_blit");
        this.aBoolean617 = this.anOpenGL1.a("GL_EXT_framebuffer_multisample");
        this.aBoolean614 = this.aBoolean617 & this.aBoolean602;
        this.aBoolean604 = ClientInfo.osName.startsWith("mac");
        OpenGL.glGetFloatv(OpenGL.GL_POINT_SIZE_RANGE, Static476.aFloatArray46, 0);
        this.aFloat136 = Static476.aFloatArray46[0];
        this.aFloat135 = Static476.aFloatArray46[1];
        return local10 == 0 ? 0 : local10;
    }

    @OriginalMember(owner = "client!qha", name = "H", descriptor = "(III[I)V")
    @Override
    public void H(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3) {
        @Pc(28) float local28 = this.aClass73_Sub3_3.aFloat159 + (this.aClass73_Sub3_3.aFloat151 * (float) arg1 + (float) arg0 * this.aClass73_Sub3_3.aFloat155 + (float) arg2 * this.aClass73_Sub3_3.aFloat154);
        if (local28 == 0.0F) {
            arg3[0] = arg3[1] = arg3[2] = -1;
            return;
        }
        @Pc(78) int local78 = (int) ((this.aClass73_Sub3_3.aFloat152 + (this.aClass73_Sub3_3.aFloat157 * (float) arg1 + this.aClass73_Sub3_3.aFloat153 * (float) arg0 + (float) arg2 * this.aClass73_Sub3_3.aFloat160)) * (float) this.anInt8001 / local28);
        arg3[0] = (int) ((float) local78 - this.aFloat145);
        @Pc(119) int local119 = (int) ((float) this.anInt8025 * (this.aClass73_Sub3_3.aFloat161 * (float) arg0 + (float) arg1 * this.aClass73_Sub3_3.aFloat156 + (float) arg2 * this.aClass73_Sub3_3.aFloat150 + this.aClass73_Sub3_3.aFloat158) / local28);
        arg3[2] = (int) local28;
        arg3[1] = (int) ((float) local119 - this.aFloat141);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!eca;Lclient!wja;)Lclient!gaa;")
    @Override
    public OffscreenSurface createOffscreenSurface(@OriginalArg(0) Surface surface, @OriginalArg(1) DepthBuffer buffer) {
        return null;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIB)V")
    public void method6998(@OriginalArg(2) int arg0) {
        OpenGL.glDrawArrays(OpenGL.GL_QUADS, 0, arg0);
    }

    @OriginalMember(owner = "client!qha", name = "DA", descriptor = "(IIII)V")
    @Override
    public void DA(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height) {
        this.anInt8001 = width;
        this.anInt8021 = x;
        this.anInt8025 = height;
        this.anInt8016 = y;
        this.method6990();
        this.method7037();
        if (this.anInt8005 == 3) {
            this.method6976();
        } else if (this.anInt8005 == 2) {
            this.method7005();
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIIILclient!aa;II)V")
    @Override
    public void line(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int mode, @OriginalArg(6) ClippingMask mask, @OriginalArg(7) int maskX, @OriginalArg(8) int maskY) {
        @Pc(6) ClippingMask_Sub3 local6 = (ClippingMask_Sub3) mask;
        @Pc(9) Class93_Sub2_Sub1 local9 = local6.aClass93_Sub2_Sub1_5;
        this.method7018();
        this.method7001(local6.aClass93_Sub2_Sub1_5);
        this.setBlendMode(mode);
        this.method7031(8448, 7681);
        this.method7021(34167, 768, 0);
        @Pc(39) float local39 = local9.aFloat67 / (float) local9.anInt3259;
        @Pc(46) float local46 = local9.aFloat68 / (float) local9.anInt3257;
        @Pc(53) float local53 = (float) -x1 + (float) x2;
        @Pc(59) float local59 = (float) y2 - (float) y1;
        @Pc(72) float local72 = (float) (1.0D / Math.sqrt(local59 * local59 + local53 * local53));
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        @Pc(91) float local91 = local53 * local72;
        @Pc(95) float local95 = local59 * local72;
        OpenGL.glBegin(OpenGL.GL_LINES);
        OpenGL.glTexCoord2f((float) (x1 - maskX) * local39, local46 * (float) (y1 - maskY));
        OpenGL.glVertex2f((float) x1 + 0.35F, (float) y1 + 0.35F);
        OpenGL.glTexCoord2f(local39 * (float) (x2 - maskX), local46 * (float) (y2 - maskY));
        OpenGL.glVertex2f(local91 + (float) x2 + 0.35F, local95 + (float) y2 + 0.35F);
        OpenGL.glEnd();
        this.method7021(5890, 768, 0);
    }

    @OriginalMember(owner = "client!qha", name = "m", descriptor = "(I)V")
    public void method6999() {
        if (this.aBoolean599) {
            OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
            OpenGL.glLoadIdentity();
            OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
            this.aBoolean599 = false;
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(I)Lclient!za;")
    @Override
    public MemoryPool createHeap(@OriginalArg(0) int size) {
        @Pc(8) GlMemoryPool local8 = new GlMemoryPool(size);
        this.aDeque_46.addLast(local8);
        return local8;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public void setCanvas(@OriginalArg(0) Canvas arg0) {
        this.aLong246 = 0L;
        this.aCanvas11 = null;
        if (arg0 == null || arg0 == this.aCanvas10) {
            this.aLong246 = this.aLong247;
            this.aCanvas11 = this.aCanvas10;
        } else if (this.aHashtable5.containsKey(arg0)) {
            @Pc(42) Long local42 = (Long) this.aHashtable5.get(arg0);
            this.aLong246 = local42;
            this.aCanvas11 = arg0;
        }
        if (this.aCanvas11 == null || this.aLong246 == 0L) {
            throw new RuntimeException();
        }
        this.anOpenGL1.setSurface(this.aLong246);
        this.method7013();
    }

    @OriginalMember(owner = "client!qha", name = "EA", descriptor = "(IIII)V")
    @Override
    public void EA(@OriginalArg(0) int height, @OriginalArg(1) int colour, @OriginalArg(2) int depth, @OriginalArg(3) int bias) {
        if (!this.aBoolean605) {
            throw new RuntimeException("");
        }
        this.anInt8006 = height;
        this.anInt8026 = colour;
        this.anInt8013 = depth;
        this.anInt8029 = bias;
        if (this.aBoolean609) {
            this.aClass98_1.aClass101_Sub6_1.method5797();
            this.aClass98_1.aClass101_Sub6_1.method5798();
        }
    }

    @OriginalMember(owner = "client!qha", name = "j", descriptor = "()V")
    @Override
    public void method7950() {
        OpenGL.glFinish();
    }

    @OriginalMember(owner = "client!qha", name = "j", descriptor = "(B)V")
    public void method7000() {
        if (this.anInt8028 >= this.anInt8000 && this.anInt8012 >= this.anInt8032) {
            OpenGL.glScissor(this.anInt8000 + this.anInt8007, this.anInt8014 - (-this.anInt7956 - -this.anInt8012), this.anInt8028 - this.anInt8000, this.anInt8012 - this.anInt8032);
        } else {
            OpenGL.glScissor(0, 0, 0, 0);
        }
    }

    @OriginalMember(owner = "client!qha", name = "M", descriptor = "()I")
    @Override
    public int M() {
        @Pc(6) int local6 = this.anInt8033;
        this.anInt8033 = 0;
        return local6;
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(Z)V")
    @Override
    public void method8018(@OriginalArg(0) boolean arg0) {
    }

    @OriginalMember(owner = "client!qha", name = "da", descriptor = "(III[I)V")
    @Override
    public void da(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3) {
        @Pc(28) float local28 = this.aClass73_Sub3_3.aFloat159 + (this.aClass73_Sub3_3.aFloat155 * (float) arg0 + this.aClass73_Sub3_3.aFloat151 * (float) arg1 + (float) arg2 * this.aClass73_Sub3_3.aFloat154);
        if ((float) this.anInt8010 > local28 || local28 > (float) this.anInt8018) {
            arg3[0] = arg3[1] = arg3[2] = -1;
            return;
        }
        @Pc(88) int local88 = (int) ((float) this.anInt8001 * (this.aClass73_Sub3_3.aFloat152 + ((float) arg0 * this.aClass73_Sub3_3.aFloat153 + (float) arg1 * this.aClass73_Sub3_3.aFloat157 + (float) arg2 * this.aClass73_Sub3_3.aFloat160)) / local28);
        @Pc(120) int local120 = (int) ((float) this.anInt8025 * ((float) arg0 * this.aClass73_Sub3_3.aFloat161 + (float) arg1 * this.aClass73_Sub3_3.aFloat156 + this.aClass73_Sub3_3.aFloat150 * (float) arg2 + this.aClass73_Sub3_3.aFloat158) / local28);
        if (this.aFloat145 <= (float) local88 && (float) local88 <= this.aFloat138 && (float) local120 >= this.aFloat141 && this.aFloat133 >= (float) local120) {
            arg3[0] = (int) ((float) local88 - this.aFloat145);
            arg3[1] = (int) ((float) local120 - this.aFloat141);
            arg3[2] = (int) local28;
        } else {
            arg3[0] = arg3[1] = arg3[2] = -1;
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ILclient!kd;)V")
    public void method7001(@OriginalArg(1) Class93 arg0) {
        @Pc(9) Class93 local9 = this.aClass93Array1[this.anInt8024];
        if (arg0 != local9) {
            if (arg0 == null) {
                OpenGL.glDisable(local9.anInt10912);
            } else {
                if (local9 == null) {
                    OpenGL.glEnable(arg0.anInt10912);
                } else if (arg0.anInt10912 != local9.anInt10912) {
                    OpenGL.glDisable(local9.anInt10912);
                    OpenGL.glEnable(arg0.anInt10912);
                }
                OpenGL.glBindTexture(arg0.anInt10912, arg0.method9440());
            }
            this.aClass93Array1[this.anInt8024] = arg0;
        }
        this.anInt7997 &= 0xFFFFFFFE;
    }

    @OriginalMember(owner = "client!qha", name = "l", descriptor = "()Z")
    @Override
    public boolean method7978() {
        return false;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ILclient!ar;)V")
    public void method7002(@OriginalArg(1) Interface1 arg0) {
        if (this.anInterface1_4 != arg0) {
            if (this.aBoolean600) {
                OpenGL.glBindBufferARB(OpenGL.GL_ELEMENT_ARRAY_BUFFER, arg0.method9353());
            }
            this.anInterface1_4 = arg0;
        }
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(B)V")
    public void method7003() {
        if (this.anInt8005 == 1) {
            return;
        }
        OpenGL.glMatrixMode(OpenGL.GL_PROJECTION);
        OpenGL.glLoadIdentity();
        if (this.anInt7869 > 0 && this.anInt7956 > 0) {
            OpenGL.glOrtho(0.0D, this.anInt7869, this.anInt7956, 0.0D, -1.0D, 1.0D);
        }
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        OpenGL.glLoadIdentity();
        this.anInt8005 = 1;
        this.anInt7997 &= 0xFFFFFFE7;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!jf;I)V")
    public void method7004(@OriginalArg(0) Interface14 arg0) {
        if (this.anInt7992 >= 3) {
            throw new RuntimeException();
        }
        if (this.anInt7992 >= 0) {
            this.anInterface14Array1[this.anInt7992].method9361();
        }
        this.anInterface14_1 = this.anInterface14Array1[++this.anInt7992] = arg0;
        this.anInterface14_1.method9360();
    }

    @OriginalMember(owner = "client!qha", name = "y", descriptor = "(I)V")
    public void method7005() {
        OpenGL.glMatrixMode(OpenGL.GL_PROJECTION);
        OpenGL.glLoadMatrixf(this.aFloatArray53, 0);
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "()Lclient!dp;")
    @Override
    public Renderer renderer() {
        @Pc(7) int vendor = -1;
        if (this.aString98.indexOf("nvidia") != -1) {
            vendor = PciVendorId.NVIDIA;
        } else if (this.aString98.indexOf("intel") != -1) {
            vendor = PciVendorId.INTEL;
        } else if (this.aString98.indexOf("ati") != -1) {
            vendor = PciVendorId.AMD;
        }
        return new Renderer(vendor, "OpenGL", this.anInt8020, this.aString99, 0L);
    }

    @OriginalMember(owner = "client!qha", name = "T", descriptor = "(IIII)V")
    @Override
    public void T(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        if (this.anInt8028 > arg2) {
            this.anInt8028 = arg2;
        }
        if (arg0 > this.anInt8000) {
            this.anInt8000 = arg0;
        }
        if (this.anInt8012 > arg3) {
            this.anInt8012 = arg3;
        }
        if (arg1 > this.anInt8032) {
            this.anInt8032 = arg1;
        }
        OpenGL.glEnable(OpenGL.GL_SCISSOR_TEST);
        this.method7037();
        this.method7000();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IZ)V")
    public void method7006(@OriginalArg(1) boolean arg0) {
        if (arg0 == this.aBoolean591) {
            return;
        }
        if (arg0) {
            OpenGL.glEnable(OpenGL.GL_DEPTH_TEST);
        } else {
            OpenGL.glDisable(OpenGL.GL_DEPTH_TEST);
        }
        this.anInt7997 &= 0xFFFFFFE0;
        this.aBoolean591 = arg0;
    }

    @OriginalMember(owner = "client!qha", name = "A", descriptor = "()Lclient!tt;")
    @Override
    public Matrix scratchMatrix() {
        return this.aClass73_Sub3_1;
    }

    @OriginalMember(owner = "client!qha", name = "d", descriptor = "(IIIIII)V")
    @Override
    public void outlineRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int colour, @OriginalArg(5) int mode) {
        @Pc(8) float local8 = (float) x + 0.35F;
        @Pc(13) float local13 = (float) y + 0.35F;
        @Pc(20) float local20 = (float) width + local8 - 1.0F;
        @Pc(27) float local27 = (float) height + local13 - 1.0F;
        this.enter2dMode();
        this.setBlendMode(mode);
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        if (this.aBoolean615) {
            OpenGL.glDisable(OpenGL.GL_MULTISAMPLE);
        }
        OpenGL.glBegin(OpenGL.GL_LINE_LOOP);
        OpenGL.glVertex2f(local8, local13);
        OpenGL.glVertex2f(local8, local27);
        OpenGL.glVertex2f(local20, local27);
        OpenGL.glVertex2f(local20, local13);
        OpenGL.glEnd();
        if (this.aBoolean615) {
            OpenGL.glEnable(OpenGL.GL_MULTISAMPLE);
        }
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(BI)V")
    public synchronized void method7007(@OriginalArg(1) int arg0) {
        @Pc(12) Node local12 = new Node();
        local12.key = arg0;
        this.aDeque_52.addLast(local12);
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(ZI)V")
    public void method7008(@OriginalArg(0) boolean arg0) {
        if (this.aBoolean601 != arg0) {
            this.aBoolean601 = arg0;
            this.method7009();
            this.anInt7997 &= 0xFFFFFFF8;
        }
    }

    @OriginalMember(owner = "client!qha", name = "s", descriptor = "(I)V")
    public void method7009() {
        if (this.aBoolean601 && !this.aBoolean611) {
            OpenGL.glEnable(OpenGL.GL_LIGHTING);
        } else {
            OpenGL.glDisable(OpenGL.GL_LIGHTING);
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IZII[B)Lclient!ar;")
    public Interface1 method7010(@OriginalArg(1) boolean arg0, @OriginalArg(2) int arg1, @OriginalArg(4) byte[] arg2) {
        return this.aBoolean600 && (!arg0 || this.aBoolean616) ? new Class132_Sub1(this, 5123, arg2, arg1, arg0) : new Class134_Sub2(this, 5123, arg2, arg1);
    }

    @OriginalMember(owner = "client!qha", name = "e", descriptor = "(B)V")
    public void method7011() {
        OpenGL.glPushMatrix();
    }

    @OriginalMember(owner = "client!qha", name = "B", descriptor = "()Z")
    @Override
    public boolean method8001() {
        return true;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIIII)V")
    @Override
    public void strongLine(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int width, @OriginalArg(6) int mode) {
        OpenGL.glLineWidth((float) width);
        this.line(x1, y1, x2, y2, colour, mode);
        OpenGL.glLineWidth(1.0F);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!qr;B)V")
    public void method7012(@OriginalArg(0) Matrix_Sub3 arg0) {
        OpenGL.glLoadMatrixf(arg0.method7146(), 0);
    }

    @OriginalMember(owner = "client!qha", name = "E", descriptor = "(I)V")
    public void method7013() {
        if (this.aCanvas11 == null) {
            this.anInt7864 = this.anInt7904 = 0;
        } else {
            @Pc(18) Dimension local18 = this.aCanvas11.getSize();
            this.anInt7864 = local18.width;
            this.anInt7904 = local18.height;
        }
        if (this.anInterface14_2 == null) {
            this.anInt7869 = this.anInt7864;
            this.anInt7956 = this.anInt7904;
            this.method6986();
        }
        this.method6981();
        this.la();
    }

    @OriginalMember(owner = "client!qha", name = "k", descriptor = "(II)V")
    public void method7014(@OriginalArg(1) int arg0) {
        if (this.anInt8024 != arg0) {
            OpenGL.glActiveTexture(arg0 + OpenGL.GL_TEXTURE0);
            this.anInt8024 = arg0;
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IZZZ)V")
    public void method7015(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) boolean arg2) {
        if (this.anInt8019 != arg0 || this.aBoolean605 != this.aBoolean609) {
            @Pc(33) Class93_Sub2 local33 = null;
            @Pc(35) int local35 = 0;
            @Pc(37) byte local37 = 0;
            @Pc(39) int local39 = 0;
            @Pc(47) int local47 = this.aBoolean605 ? 3 : 0;
            if (arg0 < 0) {
                this.method6999();
            } else {
                local33 = this.aClass169_1.method3529(arg0);
                @Pc(69) TextureMetrics local69 = super.textureSource.getMetrics(arg0);
                if (local69.speedU == 0 && local69.speedV == 0) {
                    this.method6999();
                } else {
                    @Pc(93) int local93 = local69.small ? 64 : 128;
                    @Pc(97) int local97 = local93 * 50;
                    this.method6970(0.0F, (float) (local69.speedV * (this.anInt7987 % local97)) / (float) local97, (float) (this.anInt7987 % local97 * local69.speedU) / (float) local97);
                }
                if (!this.aBoolean605) {
                    local47 = local69.effectType;
                    local39 = local69.effectParam2;
                    local37 = local69.effectParam1;
                }
                local35 = local69.colorOp;
            }
            this.aClass98_1.method2360(local39, arg2, arg1, local37, local47);
            if (!this.aClass98_1.method2359(local35, local33)) {
                this.method7001(local33);
                this.method6991(local35);
            }
            this.anInt8019 = arg0;
            this.aBoolean609 = this.aBoolean605;
        }
        this.anInt7997 &= 0xFFFFFFF8;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Ljava/awt/Canvas;II)V")
    @Override
    public void resizeCanvas(@OriginalArg(0) Canvas arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(5) long local5 = 0L;
        if (arg0 == null || this.aCanvas10 == arg0) {
            local5 = this.aLong247;
        } else if (this.aHashtable5.containsKey(arg0)) {
            @Pc(28) Long local28 = (Long) this.aHashtable5.get(arg0);
            local5 = local28;
        }
        if (local5 == 0L) {
            throw new RuntimeException();
        }
        this.anOpenGL1.surfaceResized(local5);
        if (arg0 == this.aCanvas11) {
            this.method7013();
        }
    }

    @OriginalMember(owner = "client!qha", name = "U", descriptor = "(IIIII)V")
    @Override
    public void U(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int strength, @OriginalArg(3) int colour, @OriginalArg(4) int arg4) {
        this.enter2dMode();
        this.setBlendMode(arg4);
        @Pc(15) float local15 = (float) x + 0.35F;
        @Pc(20) float local20 = (float) y + 0.35F;
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        OpenGL.glBegin(OpenGL.GL_LINES);
        OpenGL.glVertex2f(local15, local20);
        OpenGL.glVertex2f((float) strength + local15, local20);
        OpenGL.glEnd();
    }

    @OriginalMember(owner = "client!qha", name = "s", descriptor = "()Z")
    @Override
    public boolean method7979() {
        return false;
    }

    @OriginalMember(owner = "client!qha", name = "r", descriptor = "(IIIIIII)I")
    @Override
    public int r(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6) {
        @Pc(30) float local30 = this.aClass73_Sub3_3.aFloat155 * (float) arg0 + (float) arg1 * this.aClass73_Sub3_3.aFloat151 + (float) arg2 * this.aClass73_Sub3_3.aFloat154 + this.aClass73_Sub3_3.aFloat159;
        @Pc(55) float local55 = (float) arg3 * this.aClass73_Sub3_3.aFloat155 + this.aClass73_Sub3_3.aFloat151 * (float) arg4 + this.aClass73_Sub3_3.aFloat154 * (float) arg5 + this.aClass73_Sub3_3.aFloat159;
        @Pc(57) int local57 = 0;
        if ((float) this.anInt8010 > local30 && local55 < (float) this.anInt8010) {
            local57 |= 0x10;
        } else if (local30 > (float) this.anInt8018 && local55 > (float) this.anInt8018) {
            local57 |= 0x20;
        }
        @Pc(130) int local130 = (int) ((float) this.anInt8001 * (this.aClass73_Sub3_3.aFloat152 + (this.aClass73_Sub3_3.aFloat153 * (float) arg0 + this.aClass73_Sub3_3.aFloat157 * (float) arg1 + (float) arg2 * this.aClass73_Sub3_3.aFloat160)) / (float) arg6);
        @Pc(163) int local163 = (int) ((float) this.anInt8001 * (this.aClass73_Sub3_3.aFloat152 + ((float) arg4 * this.aClass73_Sub3_3.aFloat157 + (float) arg3 * this.aClass73_Sub3_3.aFloat153 + (float) arg5 * this.aClass73_Sub3_3.aFloat160)) / (float) arg6);
        if (this.aFloat145 > (float) local130 && this.aFloat145 > (float) local163) {
            local57 |= 0x1;
        } else if ((float) local130 > this.aFloat138 && (float) local163 > this.aFloat138) {
            local57 |= 0x2;
        }
        @Pc(236) int local236 = (int) ((float) this.anInt8025 * (this.aClass73_Sub3_3.aFloat161 * (float) arg0 + (float) arg1 * this.aClass73_Sub3_3.aFloat156 + (float) arg2 * this.aClass73_Sub3_3.aFloat150 + this.aClass73_Sub3_3.aFloat158) / (float) arg6);
        @Pc(269) int local269 = (int) ((float) this.anInt8025 * ((float) arg3 * this.aClass73_Sub3_3.aFloat161 + (float) arg4 * this.aClass73_Sub3_3.aFloat156 + (float) arg5 * this.aClass73_Sub3_3.aFloat150 + this.aClass73_Sub3_3.aFloat158) / (float) arg6);
        if ((float) local236 < this.aFloat141 && this.aFloat141 > (float) local269) {
            local57 |= 0x4;
        } else if ((float) local236 > this.aFloat133 && this.aFloat133 < (float) local269) {
            local57 |= 0x8;
        }
        return local57;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IILclient!jaclib/memory/Buffer;ZI)Lclient!jc;")
    public Interface12 method7016(@OriginalArg(1) int arg0, @OriginalArg(2) Buffer arg1, @OriginalArg(4) int arg2) {
        return this.aBoolean600 && (this.aBoolean616 || true) ? new Class132_Sub2(this, arg0, arg1, arg2, false) : new Class134_Sub1(this, arg0, arg1);
    }

    @OriginalMember(owner = "client!qha", name = "ZA", descriptor = "(IFFFFF)V")
    @Override
    public void ZA(@OriginalArg(0) int colour, @OriginalArg(1) float intensity, @OriginalArg(2) float reverseIntensity, @OriginalArg(3) float x, @OriginalArg(4) float y, @OriginalArg(5) float z) {
        @Pc(16) boolean local16 = colour != this.anInt8011;
        if (local16 || this.aFloat129 != intensity || reverseIntensity != this.aFloat130) {
            this.aFloat129 = intensity;
            this.anInt8011 = colour;
            this.aFloat130 = reverseIntensity;
            if (local16) {
                this.aFloat137 = (float) (this.anInt8011 & 0xFF) / 255.0F;
                this.aFloat143 = (float) (this.anInt8011 & 0xFF00) / 65280.0F;
                this.aFloat148 = (float) (this.anInt8011 & 0xFF0000) / 1.671168E7F;
                this.method7025();
            }
            this.method6966();
        }
        if (this.aFloatArray52[0] == x && this.aFloatArray52[1] == y && this.aFloatArray52[2] == z) {
            return;
        }
        this.aFloatArray52[1] = y;
        this.aFloatArray52[2] = z;
        this.aFloatArray52[0] = x;
        this.aFloatArray50[0] = -x;
        this.aFloatArray50[2] = -z;
        this.aFloatArray50[1] = -y;
        @Pc(155) float local155 = (float) (1.0D / Math.sqrt(x * x + y * y + z * z));
        this.aFloatArray51[0] = x * local155;
        this.aFloatArray51[1] = local155 * y;
        this.aFloatArray51[2] = z * local155;
        this.aFloatArray54[0] = -this.aFloatArray51[0];
        this.aFloatArray54[1] = -this.aFloatArray51[1];
        this.aFloatArray54[2] = -this.aFloatArray51[2];
        this.method6987();
        this.anInt8023 = (int) (z * 256.0F / y);
        this.anInt8027 = (int) (x * 256.0F / y);
    }

    @OriginalMember(owner = "client!qha", name = "h", descriptor = "(II)V")
    public void setBlendMode(@OriginalArg(0) int arg0) {
        if (this.anInt7998 == arg0) {
            return;
        }
        @Pc(24) boolean local24;
        @Pc(22) byte local22;
        if (arg0 == 1) {
            local22 = 1;
            local24 = true;
        } else if (arg0 == 2) {
            local22 = 2;
            local24 = false;
        } else if (arg0 == 128) {
            local22 = 3;
            local24 = true;
        } else {
            local24 = false;
            local22 = 0;
        }
        if (!this.aBoolean593) {
            OpenGL.glColorMask(true, true, true, true);
            this.aBoolean593 = true;
        }
        if (this.aBoolean592 != local24) {
            if (local24) {
                OpenGL.glEnable(OpenGL.GL_ALPHA_TEST);
            } else {
                OpenGL.glDisable(OpenGL.GL_ALPHA_TEST);
            }
            this.aBoolean592 = local24;
        }
        if (this.anInt7996 != local22) {
            if (local22 == 1) {
                OpenGL.glEnable(OpenGL.GL_BLEND);
                OpenGL.glBlendFunc(OpenGL.GL_SRC_ALPHA, OpenGL.GL_ONE_MINUS_SRC_ALPHA);
            } else if (local22 == 2) {
                OpenGL.glEnable(OpenGL.GL_BLEND);
                OpenGL.glBlendFunc(OpenGL.GL_ONE, OpenGL.GL_ONE);
            } else if (local22 == 3) {
                OpenGL.glEnable(OpenGL.GL_BLEND);
                OpenGL.glBlendFunc(OpenGL.GL_DST_COLOR, OpenGL.GL_ONE);
            } else {
                OpenGL.glDisable(OpenGL.GL_BLEND);
            }
            this.anInt7996 = local22;
        }
        this.anInt7998 = arg0;
        this.anInt7997 &= 0xFFFFFFE3;
    }

    @OriginalMember(owner = "client!qha", name = "i", descriptor = "()I")
    @Override
    public int i() {
        return this.anInt8010;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "([IIIIIZ)Lclient!st;")
    @Override
    public Sprite method7958(@OriginalArg(0) int[] arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) boolean arg4) {
        return new Sprite_Sub2(this, arg2, arg3, arg0, 0, arg1);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!pu;Lclient!pu;FLclient!pu;)Lclient!pu;")
    @Override
    public Class67 method8007(@OriginalArg(0) Class67 arg0, @OriginalArg(1) Class67 arg1, @OriginalArg(2) float arg2, @OriginalArg(3) Class67 arg3) {
        if (arg0 != null && arg1 != null && this.aBoolean598 && this.aBoolean613) {
            @Pc(21) Class67_Sub1_Sub1 local21 = null;
            @Pc(24) Class67_Sub1 local24 = (Class67_Sub1) arg0;
            @Pc(27) Class67_Sub1 local27 = (Class67_Sub1) arg1;
            @Pc(31) Class93_Sub1 local31 = local24.method8589();
            @Pc(35) Class93_Sub1 local35 = local27.method8589();
            if (local31 != null && local35 != null) {
                @Pc(55) int local55 = local35.anInt2456 >= local31.anInt2456 ? local35.anInt2456 : local31.anInt2456;
                if (arg3 != arg0 && arg1 != arg3 && arg3 instanceof Class67_Sub1_Sub1) {
                    @Pc(71) Class67_Sub1_Sub1 local71 = (Class67_Sub1_Sub1) arg3;
                    if (local71.method1571() == local55) {
                        local21 = local71;
                    }
                }
                if (local21 == null) {
                    local21 = new Class67_Sub1_Sub1(this, local55);
                }
                if (local21.method1572(local35, local31, arg2)) {
                    return local21;
                }
            }
        }
        return arg2 < 0.5F ? arg0 : arg1;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(II[[I[[IIII)Lclient!s;")
    @Override
    public Ground createGround(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int[][] arg2, @OriginalArg(3) int[][] arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        return new Ground_Sub2(this, arg4, arg5, arg0, arg1, arg2, arg3, 512);
    }

    @OriginalMember(owner = "client!qha", name = "B", descriptor = "(I)V")
    public void method7018() {
        if (this.anInt7997 == 2) {
            return;
        }
        this.method7003();
        this.method7035(false);
        this.method7008(false);
        this.method7006(false);
        this.method6972(false);
        this.method7046(-2);
        this.anInt7997 = 2;
    }

    @OriginalMember(owner = "client!qha", name = "y", descriptor = "()Lclient!tt;")
    @Override
    public Matrix createMatrix() {
        return new Matrix_Sub3();
    }

    @OriginalMember(owner = "client!qha", name = "o", descriptor = "(I)V")
    public void method7019() {
        OpenGL.glLoadIdentity();
        OpenGL.glMultMatrixf(this.aClass73_Sub3_4.method7146(), 0);
        if (this.aBoolean609) {
            this.aClass98_1.aClass101_Sub6_1.method5797();
        }
        this.method6987();
        this.method6980();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!qr;I)V")
    public void method7020(@OriginalArg(0) Matrix_Sub3 arg0) {
        OpenGL.glPushMatrix();
        OpenGL.glMultMatrixf(arg0.method7146(), 0);
    }

    @OriginalMember(owner = "client!qha", name = "f", descriptor = "(II)V")
    @Override
    public void f(@OriginalArg(0) int near, @OriginalArg(1) int far) {
        if (near == this.anInt8010 && this.anInt8018 == far) {
            return;
        }
        this.anInt8010 = near;
        this.anInt8018 = far;
        this.method6990();
        this.method6969();
        if (this.anInt8005 == 3) {
            this.method6976();
        } else if (this.anInt8005 == 2) {
            this.method7005();
        }
    }

    @OriginalMember(owner = "client!qha", name = "v", descriptor = "()V")
    @Override
    public void restoreSurface() {
        if (this.aBoolean613) {
            if (this.anInterface14_2 != this.aClass406_6) {
                throw new RuntimeException();
            }
            this.aClass406_6.method9363(0);
            this.aClass406_6.method9363(8);
            this.method6993(this.aClass406_6);
        } else if (this.aBoolean612) {
            this.aClass23_Sub2_1.copyRect(0, 0, this.anInt7869, this.anInt7956, 0, 0);
            this.anOpenGL1.setSurface(this.aLong246);
        } else {
            throw new RuntimeException("");
        }
        this.aClass23_Sub2_1 = null;
        this.anInt7869 = this.anInt7864;
        this.anInt7956 = this.anInt7904;
        this.method6981();
        this.method6986();
        this.la();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIIIIII)V")
    @Override
    public void method7995(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7) {
        if (arg2 == arg0 && arg3 == arg1) {
            return;
        }
        this.enter2dMode();
        this.setBlendMode(1);
        @Pc(37) float local37 = (float) -arg0 + (float) arg2;
        @Pc(44) float local44 = (float) -arg1 + (float) arg3;
        @Pc(57) float local57 = (float) (1.0D / Math.sqrt(local37 * local37 + local44 * local44));
        @Pc(63) int local63 = arg7 % (arg5 + arg6);
        OpenGL.glColor4ub((byte) (arg4 >> 16), (byte) (arg4 >> 8), (byte) arg4, (byte) (arg4 >> 24));
        @Pc(82) float local82 = local37 * local57;
        @Pc(86) float local86 = local44 * local57;
        @Pc(91) float local91 = local82 * (float) arg5;
        @Pc(96) float local96 = (float) arg5 * local86;
        @Pc(98) float local98 = 0.0F;
        @Pc(100) float local100 = 0.0F;
        @Pc(102) float local102 = local91;
        @Pc(104) float local104 = local96;
        if (local63 <= arg5) {
            local104 = (float) (arg5 - local63) * local86;
            local102 = (float) (arg5 - local63) * local82;
        } else {
            local100 = (float) (arg5 + arg6 - local63) * local86;
            local98 = local82 * (float) (arg6 + arg5 - local63);
        }
        @Pc(152) float local152 = (float) arg0 + 0.35F + local98;
        @Pc(159) float local159 = local100 + ((float) arg1 + 0.35F);
        @Pc(164) float local164 = (float) arg6 * local82;
        @Pc(169) float local169 = local86 * (float) arg6;
        while (true) {
            if (arg0 >= arg2) {
                if (local152 < (float) arg2 + 0.35F) {
                    break;
                }
                if ((float) arg2 > local102 + local152) {
                    local102 = (float) arg2 - local152;
                }
            } else {
                if (local152 > (float) arg2 + 0.35F) {
                    break;
                }
                if ((float) arg2 < local102 + local152) {
                    local102 = (float) arg2 - local152;
                }
            }
            if (arg3 <= arg1) {
                if ((float) arg3 + 0.35F > local159) {
                    break;
                }
                if (local104 + local159 < (float) arg3) {
                    local104 = (float) arg3 - local159;
                }
            } else {
                if (local159 > (float) arg3 + 0.35F) {
                    break;
                }
                if ((float) arg3 < local104 + local159) {
                    local104 = (float) arg3 - local159;
                }
            }
            OpenGL.glBegin(OpenGL.GL_LINES);
            OpenGL.glVertex2f(local152, local159);
            OpenGL.glVertex2f(local102 + local152, local104 + local159);
            OpenGL.glEnd();
            local152 += local164 + local102;
            local159 += local169 + local104;
            local102 = local91;
            local104 = local96;
        }
    }

    @OriginalMember(owner = "client!qha", name = "X", descriptor = "(I)V")
    @Override
    public void X(@OriginalArg(0) int arg0) {
        this.anInt7989 = 0;
        while (arg0 > 1) {
            arg0 >>= 0x1;
            this.anInt7989++;
        }
        this.anInt7988 = 0x1 << this.anInt7989;
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(IIIB)V")
    public void method7021(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, arg2 + OpenGL.GL_SRC0_RGB, arg0);
        OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, arg2 + OpenGL.GL_OPERAND0_RGB, arg1);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIIIIIIIIII)V")
    @Override
    public void drawTriangle(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int z1, @OriginalArg(3) int x2, @OriginalArg(4) int y2, @OriginalArg(5) int z2, @OriginalArg(6) int x3, @OriginalArg(7) int y3, @OriginalArg(8) int z3, @OriginalArg(9) int c1, @OriginalArg(10) int c2, @OriginalArg(11) int c3, @OriginalArg(12) int mode) {
        this.enter2dMode();
        this.setBlendMode(mode);
        OpenGL.glBegin(OpenGL.GL_TRIANGLES);
        OpenGL.glColor4ub((byte) (c1 >> 16), (byte) (c1 >> 8), (byte) c1, (byte) (c1 >> 24));
        OpenGL.glVertex3f((float) x1 + 0.35F, (float) y1 + 0.35F, (float) z1);
        OpenGL.glColor4ub((byte) (c2 >> 16), (byte) (c2 >> 8), (byte) c2, (byte) (c2 >> 24));
        OpenGL.glVertex3f((float) x2 + 0.35F, (float) y2 + 0.35F, (float) z2);
        OpenGL.glColor4ub((byte) (c3 >> 16), (byte) (c3 >> 8), (byte) c3, (byte) (c3 >> 24));
        OpenGL.glVertex3f((float) x3 + 0.35F, (float) y3 + 0.35F, (float) z3);
        OpenGL.glEnd();
    }

    @OriginalMember(owner = "client!qha", name = "L", descriptor = "(III)V")
    @Override
    public void L(@OriginalArg(0) int colour, @OriginalArg(1) int range, @OriginalArg(2) int offset) {
        if (colour == this.anInt8002 && this.anInt8008 == range && offset == this.anInt8009) {
            return;
        }
        this.anInt8008 = range;
        this.anInt8009 = offset;
        this.anInt8002 = colour;
        this.method6969();
        this.method7044();
    }

    @OriginalMember(owner = "client!qha", name = "f", descriptor = "()V")
    @Override
    public void stopBloom() {
        if (this.aClass2_Sub31_Sub1_1 != null && this.aClass2_Sub31_Sub1_1.method4616()) {
            this.aClass276_1.method6251(this.aClass2_Sub31_Sub1_1);
            this.aClass169_1.method3527();
        }
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(III)V")
    public void method7022(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        this.anInt8007 = arg1;
        this.anInt8014 = arg0;
        this.method6986();
        this.method7000();
    }

    @OriginalMember(owner = "client!qha", name = "l", descriptor = "(II)I")
    public int method7023(@OriginalArg(1) int arg0) {
        if (arg0 == 5121 || arg0 == 5120) {
            return 1;
        } else if (arg0 == 5123 || arg0 == 5122) {
            return 2;
        } else if (arg0 == 5125 || arg0 == 5124 || arg0 == 5126) {
            return 4;
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIIF)Lclient!lca;")
    @Override
    public PointLight method7941(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) float arg5) {
        return new Node_Sub7_Sub1(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @OriginalMember(owner = "client!qha", name = "HA", descriptor = "(IIII[I)V")
    @Override
    public void HA(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int[] arg4) {
        @Pc(28) float local28 = (float) arg0 * this.aClass73_Sub3_3.aFloat155 + this.aClass73_Sub3_3.aFloat151 * (float) arg1 + this.aClass73_Sub3_3.aFloat154 * (float) arg2 + this.aClass73_Sub3_3.aFloat159;
        if ((float) this.anInt8010 > local28 || (float) this.anInt8018 < local28) {
            arg4[0] = arg4[1] = arg4[2] = -1;
            return;
        }
        @Pc(89) int local89 = (int) ((float) this.anInt8001 * (this.aClass73_Sub3_3.aFloat152 + (this.aClass73_Sub3_3.aFloat157 * (float) arg1 + this.aClass73_Sub3_3.aFloat153 * (float) arg0 + (float) arg2 * this.aClass73_Sub3_3.aFloat160)) / (float) arg3);
        @Pc(122) int local122 = (int) ((this.aClass73_Sub3_3.aFloat156 * (float) arg1 + (float) arg0 * this.aClass73_Sub3_3.aFloat161 + this.aClass73_Sub3_3.aFloat150 * (float) arg2 + this.aClass73_Sub3_3.aFloat158) * (float) this.anInt8025 / (float) arg3);
        if ((float) local89 >= this.aFloat145 && this.aFloat138 >= (float) local89 && this.aFloat141 <= (float) local122 && this.aFloat133 >= (float) local122) {
            arg4[1] = (int) ((float) local122 - this.aFloat141);
            arg4[0] = (int) ((float) local89 - this.aFloat145);
            arg4[2] = (int) local28;
        } else {
            arg4[0] = arg4[1] = arg4[2] = -1;
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ZI[BII)Lclient!jc;")
    public Interface12 method7024(@OriginalArg(0) boolean arg0, @OriginalArg(1) int arg1, @OriginalArg(2) byte[] arg2, @OriginalArg(3) int arg3) {
        return this.aBoolean600 && (!arg0 || this.aBoolean616) ? new Class132_Sub2(this, arg3, arg2, arg1, arg0) : new Class134_Sub1(this, arg3, arg2, arg1);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "([Ljava/awt/Rectangle;III)V")
    @Override
    public void flipDirtyRect(@OriginalArg(0) Rectangle[] rectangles, @OriginalArg(1) int count, @OriginalArg(2) int x, @OriginalArg(3) int y) throws FlipException {
        this.flip(x, y);
    }

    @OriginalMember(owner = "client!qha", name = "la", descriptor = "()V")
    @Override
    public void la() {
        this.anInt8028 = this.anInt7869;
        this.anInt8000 = 0;
        this.anInt8032 = 0;
        this.anInt8012 = this.anInt7956;
        OpenGL.glDisable(OpenGL.GL_SCISSOR_TEST);
        this.method7037();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Z)V")
    @Override
    public void setShrinkTextures(@OriginalArg(0) boolean arg0) {
    }

    @OriginalMember(owner = "client!qha", name = "C", descriptor = "(I)V")
    public void method7025() {
        Static476.aFloatArray46[0] = this.aFloat148 * this.aFloat149;
        Static476.aFloatArray46[2] = this.aFloat149 * this.aFloat137;
        Static476.aFloatArray46[1] = this.aFloat143 * this.aFloat149;
        Static476.aFloatArray46[3] = 1.0F;
        OpenGL.glLightModelfv(OpenGL.GL_LIGHT_MODEL_AMBIENT, Static476.aFloatArray46, 0);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(I[Lclient!lca;)V")
    @Override
    public void method8009(@OriginalArg(0) int arg0, @OriginalArg(1) PointLight[] arg1) {
        for (@Pc(7) int local7 = 0; local7 < arg0; local7++) {
            this.aClass2_Sub7Array5[local7] = arg1[local7];
        }
        this.anInt8031 = arg0;
        if (this.anInt8005 != 1) {
            this.method6980();
        }
    }

    @OriginalMember(owner = "client!qha", name = "e", descriptor = "(I)V")
    @Override
    public synchronized void tick(@OriginalArg(0) int time) {
        @Pc(9) int local9 = time & Integer.MAX_VALUE;
        @Pc(11) int local11 = 0;
        @Pc(19) IntNode local19;
        while (!this.aDeque_48.isEmpty()) {
            local19 = (IntNode) this.aDeque_48.removeFirst();
            Static154.anIntArray236[local11++] = (int) local19.key;
            this.anInt7993 -= local19.value;
            if (local11 == 1000) {
                OpenGL.glDeleteBuffersARB(local11, Static154.anIntArray236, 0);
                local11 = 0;
            }
        }
        if (local11 > 0) {
            OpenGL.glDeleteBuffersARB(local11, Static154.anIntArray236, 0);
            local11 = 0;
        }
        while (!this.aDeque_49.isEmpty()) {
            local19 = (IntNode) this.aDeque_49.removeFirst();
            Static154.anIntArray236[local11++] = (int) local19.key;
            this.anInt7994 -= local19.value;
            if (local11 == 1000) {
                OpenGL.glDeleteTextures(local11, Static154.anIntArray236, 0);
                local11 = 0;
            }
        }
        if (local11 > 0) {
            OpenGL.glDeleteTextures(local11, Static154.anIntArray236, 0);
            local11 = 0;
        }
        while (!this.aDeque_50.isEmpty()) {
            local19 = (IntNode) this.aDeque_50.removeFirst();
            Static154.anIntArray236[local11++] = local19.value;
            if (local11 == 1000) {
                OpenGL.glDeleteFramebuffersEXT(local11, Static154.anIntArray236, 0);
                local11 = 0;
            }
        }
        if (local11 > 0) {
            OpenGL.glDeleteFramebuffersEXT(local11, Static154.anIntArray236, 0);
            local11 = 0;
        }
        while (!this.aDeque_51.isEmpty()) {
            local19 = (IntNode) this.aDeque_51.removeFirst();
            Static154.anIntArray236[local11++] = (int) local19.key;
            this.anInt7995 -= local19.value;
            if (local11 == 1000) {
                OpenGL.glDeleteRenderbuffersEXT(local11, Static154.anIntArray236, 0);
                local11 = 0;
            }
        }
        if (local11 > 0) {
            OpenGL.glDeleteRenderbuffersEXT(local11, Static154.anIntArray236, 0);
        }
        while (!this.aDeque_47.isEmpty()) {
            local19 = (IntNode) this.aDeque_47.removeFirst();
            OpenGL.glDeleteLists((int) local19.key, local19.value);
        }
        @Pc(227) Node local227;
        while (!this.aDeque_52.isEmpty()) {
            local227 = this.aDeque_52.removeFirst();
            OpenGL.glDeleteProgramARB((int) local227.key);
        }
        while (!this.aDeque_53.isEmpty()) {
            local227 = this.aDeque_53.removeFirst();
            OpenGL.glDeleteObjectARB(local227.key);
        }
        while (!this.aDeque_47.isEmpty()) {
            local19 = (IntNode) this.aDeque_47.removeFirst();
            OpenGL.glDeleteLists((int) local19.key, local19.value);
        }
        this.aClass169_1.method3528();
        if (this.E() > 100663296 && this.aLong248 + TimeUtils.MILLISECONDS_PER_MINUTE < SystemTimer.safetime()) {
            System.gc();
            this.aLong248 = SystemTimer.safetime();
        }
        this.anInt7987 = local9;
    }

    @OriginalMember(owner = "client!qha", name = "i", descriptor = "(B)V")
    public void enter2dMode() {
        if (this.anInt7997 == 1) {
            return;
        }
        this.method7003();
        this.method7035(false);
        this.method7008(false);
        this.method7006(false);
        this.method6972(false);
        this.method7001(null);
        this.method7046(-2);
        this.method6991(1);
        this.anInt7997 = 1;
    }

    @OriginalMember(owner = "client!qha", name = "K", descriptor = "([I)V")
    @Override
    public void K(@OriginalArg(0) int[] destination) {
        destination[1] = this.anInt8032;
        destination[3] = this.anInt8012;
        destination[0] = this.anInt8000;
        destination[2] = this.anInt8028;
    }

    @OriginalMember(owner = "client!qha", name = "H", descriptor = "(I)V")
    public void method7027() {
        if (this.anInt7997 == 8) {
            return;
        }
        this.method7033();
        this.method7035(true);
        this.method7006(true);
        this.method6972(true);
        this.setBlendMode(1);
        this.anInt7997 = 8;
    }

    @OriginalMember(owner = "client!qha", name = "r", descriptor = "()Z")
    @Override
    public boolean hardShadow() {
        return true;
    }

    @OriginalMember(owner = "client!qha", name = "g", descriptor = "(II)I")
    public int method7028(@OriginalArg(0) int arg0) {
        if (arg0 == 6406 || arg0 == 6409) {
            return 1;
        } else if (arg0 == 6410 || arg0 == 34846 || arg0 == 34844) {
            return 2;
        } else if (arg0 == 6407) {
            return 3;
        } else if (arg0 == 6408 || arg0 == 34847) {
            return 4;
        } else if (arg0 == 34843) {
            return 6;
        } else if (arg0 == 34842) {
            return 8;
        } else if (arg0 == 6402) {
            return 3;
        } else if (arg0 == 6401) {
            return 1;
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(IIII)V")
    public void method7029(@OriginalArg(1) int arg0, @OriginalArg(3) int arg1) {
        OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, arg0 + OpenGL.GL_SRC0_ALPHA, arg1);
        OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, arg0 + OpenGL.GL_OPERAND0_ALPHA, OpenGL.GL_SRC_ALPHA);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(III)V")
    public synchronized void method7030(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(8) IntNode local8 = new IntNode(arg1);
        local8.key = arg0;
        this.aDeque_49.addLast(local8);
    }

    @OriginalMember(owner = "client!qha", name = "C", descriptor = "(Z)V")
    @Override
    public void C(@OriginalArg(0) boolean zWrite) {
        this.aBoolean596 = zWrite;
        this.method7032();
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(III)V")
    public void method7031(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1) {
        if (this.anInt8024 != 0) {
            OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_COMBINE_RGB, arg1);
            OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_COMBINE_ALPHA, arg0);
            return;
        }
        @Pc(15) boolean local15 = false;
        if (arg1 != this.anInt8022) {
            OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_COMBINE_RGB, arg1);
            this.anInt8022 = arg1;
            local15 = true;
        }
        if (this.anInt8015 != arg0) {
            OpenGL.glTexEnvi(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_COMBINE_ALPHA, arg0);
            local15 = true;
            this.anInt8015 = arg0;
        }
        if (local15) {
            this.anInt7997 &= 0xFFFFFFE2;
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIIIIILclient!aa;IIIII)V")
    @Override
    public void method7942(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2, @OriginalArg(4) int colour, @OriginalArg(5) int mode, @OriginalArg(6) ClippingMask mask, @OriginalArg(7) int maskX, @OriginalArg(8) int maskY, @OriginalArg(9) int arg8, @OriginalArg(10) int arg9, @OriginalArg(11) int arg10) {
        if (x2 == x1 && y1 == y2) {
            return;
        }
        @Pc(22) ClippingMask_Sub3 local22 = (ClippingMask_Sub3) mask;
        @Pc(25) Class93_Sub2_Sub1 local25 = local22.aClass93_Sub2_Sub1_5;
        this.method7018();
        this.method7001(local22.aClass93_Sub2_Sub1_5);
        this.setBlendMode(mode);
        this.method7031(8448, 7681);
        this.method7021(34167, 768, 0);
        @Pc(55) float local55 = local25.aFloat67 / (float) local25.anInt3259;
        @Pc(62) float local62 = local25.aFloat68 / (float) local25.anInt3257;
        @Pc(69) float local69 = (float) -x1 + (float) x2;
        @Pc(76) float local76 = (float) -y1 + (float) y2;
        @Pc(89) float local89 = (float) (1.0D / Math.sqrt(local69 * local69 + local76 * local76));
        @Pc(95) int local95 = arg10 % (arg8 + arg9);
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        @Pc(114) float local114 = local69 * local89;
        @Pc(118) float local118 = local76 * local89;
        @Pc(123) float local123 = (float) arg8 * local114;
        @Pc(128) float local128 = local118 * (float) arg8;
        @Pc(130) float local130 = 0.0F;
        @Pc(132) float local132 = 0.0F;
        @Pc(134) float local134 = local123;
        @Pc(136) float local136 = local128;
        if (local95 > arg8) {
            local132 = (float) (arg8 + arg9 - local95) * local118;
            local130 = local114 * (float) (arg9 + arg8 - local95);
        } else {
            local136 = local118 * (float) (arg8 - local95);
            local134 = (float) (arg8 - local95) * local114;
        }
        @Pc(187) float local187 = (float) x1 + 0.35F + local130;
        @Pc(194) float local194 = local132 + ((float) y1 + 0.35F);
        @Pc(199) float local199 = local114 * (float) arg9;
        @Pc(204) float local204 = (float) arg9 * local118;
        while (true) {
            if (x1 >= x2) {
                if ((float) x2 + 0.35F > local187) {
                    break;
                }
                if (local134 + local187 < (float) x2) {
                    local134 = (float) x2 - local187;
                }
            } else {
                if (local187 > (float) x2 + 0.35F) {
                    break;
                }
                if ((float) x2 < local187 + local134) {
                    local134 = (float) x2 - local187;
                }
            }
            if (y1 >= y2) {
                if ((float) y2 + 0.35F > local194) {
                    break;
                }
                if ((float) y2 > local136 + local194) {
                    local136 = (float) y2 - local194;
                }
            } else {
                if ((float) y2 + 0.35F < local194) {
                    break;
                }
                if ((float) y2 < local136 + local194) {
                    local136 = (float) y2 - local194;
                }
            }
            OpenGL.glBegin(OpenGL.GL_LINES);
            OpenGL.glTexCoord2f(local55 * (local187 - (float) maskX), local62 * ((float) -maskY + local194));
            OpenGL.glVertex2f(local187, local194);
            OpenGL.glTexCoord2f(((float) -maskX + (local187 + local134)) * local55, local62 * (local136 + local194 - (float) maskY));
            OpenGL.glVertex2f(local134 + local187, local194 + local136);
            local187 += local134 + local199;
            OpenGL.glEnd();
            local194 += local204 + local136;
            local134 = local123;
            local136 = local128;
        }
        this.method7021(5890, 768, 0);
    }

    @OriginalMember(owner = "client!qha", name = "k", descriptor = "(I)V")
    @Override
    public void linkThreads(@OriginalArg(0) int arg0) {
        this.method6982();
    }

    @OriginalMember(owner = "client!qha", name = "E", descriptor = "()I")
    @Override
    public int E() {
        return this.anInt7993 + this.anInt7994 + this.anInt7995;
    }

    @OriginalMember(owner = "client!qha", name = "za", descriptor = "(IIIII)V")
    @Override
    protected void za(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int radius, @OriginalArg(3) int colour, @OriginalArg(4) int mode) {
        if (radius < 0) {
            radius = -radius;
        }
        if (x + radius < this.anInt8000 || x - radius > this.anInt8028 || this.anInt8032 > y + radius || this.anInt8012 < y - radius) {
            return;
        }
        this.enter2dMode();
        this.setBlendMode(mode);
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        @Pc(83) float local83 = (float) x + 0.35F;
        @Pc(88) float local88 = (float) y + 0.35F;
        @Pc(92) int local92 = radius << 1;
        if (this.aFloat136 > (float) local92) {
            OpenGL.glBegin(OpenGL.GL_QUADS);
            OpenGL.glVertex2f(local83 + 1.0F, local88 + 1.0F);
            OpenGL.glVertex2f(local83 + 1.0F, -1.0F + local88);
            OpenGL.glVertex2f(local83 - 1.0F, local88 - 1.0F);
            OpenGL.glVertex2f(local83 - 1.0F, local88 + 1.0F);
            OpenGL.glEnd();
        } else if (this.aFloat135 >= (float) local92) {
            OpenGL.glEnable(OpenGL.GL_POINT_SMOOTH);
            OpenGL.glPointSize((float) local92);
            OpenGL.glBegin(OpenGL.GL_POINTS);
            OpenGL.glVertex2f(local83, local88);
            OpenGL.glEnd();
            OpenGL.glDisable(OpenGL.GL_POINT_SMOOTH);
        } else {
            OpenGL.glBegin(OpenGL.GL_TRIANGLE_FAN);
            OpenGL.glVertex2f(local83, local88);
            @Pc(148) int local148 = 262144 / (radius * 6);
            if (local148 <= 64) {
                local148 = 64;
            } else if (local148 > 512) {
                local148 = 512;
            }
            local148 = SkyBoxSphere.method5587(local148);
            OpenGL.glVertex2f((float) radius + local83, local88);
            for (@Pc(178) int local178 = 16384 - local148; local178 > 0; local178 -= local148) {
                OpenGL.glVertex2f(COS[local178] * (float) radius + local83, SIN[local178] * (float) radius + local88);
            }
            OpenGL.glVertex2f(local83 + (float) radius, local88);
            OpenGL.glEnd();
        }
    }

    @OriginalMember(owner = "client!qha", name = "I", descriptor = "()I")
    @Override
    public int I() {
        @Pc(6) int local6 = this.anInt8034;
        this.anInt8034 = 0;
        return local6;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!tt;)V")
    @Override
    public void setCamera(@OriginalArg(0) Matrix matrix) {
        this.aClass73_Sub3_3.apply(matrix);
        this.aClass73_Sub3_4.apply(this.aClass73_Sub3_3);
        this.aClass73_Sub3_4.method7141();
        this.aClass73_Sub3_5.method7142(this.aClass73_Sub3_4);
        if (this.anInt8005 != 1) {
            this.method7019();
        }
    }

    @OriginalMember(owner = "client!qha", name = "F", descriptor = "(I)V")
    public void method7032() {
        OpenGL.glDepthMask(this.aBoolean590 && this.aBoolean596);
    }

    @OriginalMember(owner = "client!qha", name = "G", descriptor = "(I)V")
    public void method7033() {
        if (this.anInt8005 != 2) {
            this.anInt8005 = 2;
            this.method7005();
            this.method7019();
            this.anInt7997 &= 0xFFFFFFF8;
        }
    }

    @OriginalMember(owner = "client!qha", name = "c", descriptor = "(IIIIII)Lclient!pu;")
    @Override
    public Class67 method8008(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        return this.aBoolean598 ? new Class67_Sub1_Sub2(this, arg0, arg1, arg2, arg3, arg4, arg5) : null;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(BIILclient!ar;I)V")
    public void method7034(@OriginalArg(1) int arg0, @OriginalArg(3) Interface1 arg1, @OriginalArg(4) int arg2) {
        @Pc(17) int local17 = arg1.method9354();
        @Pc(24) int local24 = arg0 * this.method7023(local17);
        this.method7002(arg1);
        OpenGL.glDrawElements(OpenGL.GL_TRIANGLES, arg2, local17, arg1.method9352() + (long) local24);
    }

    @OriginalMember(owner = "client!qha", name = "P", descriptor = "(IIIII)V")
    @Override
    public void P(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int strength, @OriginalArg(3) int colour, @OriginalArg(4) int mode) {
        this.enter2dMode();
        this.setBlendMode(mode);
        @Pc(15) float local15 = (float) x + 0.35F;
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        @Pc(35) float local35 = (float) y + 0.35F;
        OpenGL.glBegin(OpenGL.GL_LINES);
        OpenGL.glVertex2f(local15, local35);
        OpenGL.glVertex2f(local15, local35 + (float) strength);
        OpenGL.glEnd();
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ZB)V")
    public void method7035(@OriginalArg(0) boolean arg0) {
        if (arg0 != this.aBoolean594) {
            this.aBoolean594 = arg0;
            this.method7044();
            this.anInt7997 &= 0xFFFFFFE0;
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ILclient!jf;)V")
    public void method7036(@OriginalArg(1) Interface14 arg0) {
        if (this.anInt7990 >= 3) {
            throw new RuntimeException();
        }
        if (this.anInt7990 >= 0) {
            this.anInterface14Array2[this.anInt7990].method9357();
        }
        this.anInterface14_2 = this.anInterface14Array2[++this.anInt7990] = arg0;
        this.anInterface14_2.method9362();
    }

    @OriginalMember(owner = "client!qha", name = "z", descriptor = "(I)V")
    public void method7037() {
        this.aFloat145 = (float) (this.anInt8000 - this.anInt8021);
        this.aFloat133 = (float) (this.anInt8012 - this.anInt8016);
        this.aFloat138 = (float) (this.anInt8028 - this.anInt8021);
        this.aFloat141 = (float) (this.anInt8032 - this.anInt8016);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(II)Lclient!eca;")
    @Override
    public Surface method7962(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return null;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!jf;Z)V")
    public void method7038(@OriginalArg(0) Interface14 arg0) {
        if (this.anInt7990 < 0 || this.anInterface14Array2[this.anInt7990] != arg0) {
            throw new RuntimeException();
        }
        this.anInterface14Array2[this.anInt7990--] = null;
        arg0.method9357();
        if (this.anInt7990 >= 0) {
            this.anInterface14_2 = this.anInterface14Array2[this.anInt7990];
            this.anInterface14_2.method9362();
        } else {
            this.anInterface14_2 = null;
        }
    }

    @OriginalMember(owner = "client!qha", name = "Q", descriptor = "(IIIIII[BII)V")
    @Override
    public void Q(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int overlayColour, @OriginalArg(5) int underlayColour, @OriginalArg(6) byte[] shape, @OriginalArg(7) int size, @OriginalArg(8) int mode) {
        @Pc(45) float local45;
        @Pc(49) float local49;
        if (this.aClass93_Sub2_Sub1_4 == null || width > this.aClass93_Sub2_Sub1_4.anInt3248 || height > this.aClass93_Sub2_Sub1_4.anInt3255) {
            this.aClass93_Sub2_Sub1_4 = Static469.method6359(height, shape, this, width);
            this.aClass93_Sub2_Sub1_4.method2946(false, false);
            local45 = this.aClass93_Sub2_Sub1_4.aFloat68;
            local49 = this.aClass93_Sub2_Sub1_4.aFloat67;
        } else {
            this.aClass93_Sub2_Sub1_4.method2945(6406, height, width, shape, false);
            local49 = (float) width * this.aClass93_Sub2_Sub1_4.aFloat67 / (float) this.aClass93_Sub2_Sub1_4.anInt3248;
            local45 = (float) height * this.aClass93_Sub2_Sub1_4.aFloat68 / (float) this.aClass93_Sub2_Sub1_4.anInt3255;
        }
        this.method7018();
        this.method7001(this.aClass93_Sub2_Sub1_4);
        this.setBlendMode(mode);
        OpenGL.glColor4ub((byte) (overlayColour >> 16), (byte) (overlayColour >> 8), (byte) overlayColour, (byte) (overlayColour >> 24));
        this.method6985(underlayColour);
        this.method7031(34165, 34165);
        this.method7021(34166, 768, 0);
        this.method7021(5890, 770, 2);
        this.method7029(0, 34166);
        this.method7029(2, 5890);
        @Pc(151) float local151 = (float) x;
        @Pc(154) float local154 = (float) y;
        @Pc(159) float local159 = local151 + (float) width;
        OpenGL.glBegin(OpenGL.GL_QUADS);
        @Pc(166) float local166 = (float) height + local154;
        OpenGL.glTexCoord2f(0.0F, 0.0F);
        OpenGL.glVertex2f(local151, local154);
        OpenGL.glTexCoord2f(0.0F, local49);
        OpenGL.glVertex2f(local151, local166);
        OpenGL.glTexCoord2f(local45, local49);
        OpenGL.glVertex2f(local159, local166);
        OpenGL.glTexCoord2f(local45, 0.0F);
        OpenGL.glVertex2f(local159, local154);
        OpenGL.glEnd();
        this.method7021(5890, 768, 0);
        this.method7021(34166, 770, 2);
        this.method7029(0, 5890);
        this.method7029(2, 34166);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(BLclient!ed;Lclient!ed;Lclient!ed;Lclient!ed;)V")
    public void method7039(@OriginalArg(1) Class94 arg0, @OriginalArg(2) Class94 arg1, @OriginalArg(3) Class94 arg2, @OriginalArg(4) Class94 arg3) {
        if (arg2 == null) {
            OpenGL.glDisableClientState(OpenGL.GL_VERTEX_ARRAY);
        } else {
            this.method6965(arg2.anInterface12_2);
            OpenGL.glVertexPointer(arg2.aByte47, arg2.aShort29, this.anInterface12_6.method5001(), this.anInterface12_6.method5003() + (long) arg2.aByte46);
            OpenGL.glEnableClientState(OpenGL.GL_VERTEX_ARRAY);
        }
        if (arg1 == null) {
            OpenGL.glDisableClientState(OpenGL.GL_NORMAL_ARRAY);
        } else {
            this.method6965(arg1.anInterface12_2);
            OpenGL.glNormalPointer(arg1.aShort29, this.anInterface12_6.method5001(), this.anInterface12_6.method5003() + (long) arg1.aByte46);
            OpenGL.glEnableClientState(OpenGL.GL_NORMAL_ARRAY);
        }
        if (arg0 == null) {
            OpenGL.glDisableClientState(OpenGL.GL_COLOR_ARRAY);
        } else {
            this.method6965(arg0.anInterface12_2);
            OpenGL.glColorPointer(arg0.aByte47, arg0.aShort29, this.anInterface12_6.method5001(), this.anInterface12_6.method5003() + (long) arg0.aByte46);
            OpenGL.glEnableClientState(OpenGL.GL_COLOR_ARRAY);
        }
        if (arg3 == null) {
            OpenGL.glDisableClientState(OpenGL.GL_TEXTURE_COORD_ARRAY);
        } else {
            this.method6965(arg3.anInterface12_2);
            OpenGL.glTexCoordPointer(arg3.aByte47, arg3.aShort29, this.anInterface12_6.method5001(), this.anInterface12_6.method5003() + (long) arg3.aByte46);
            OpenGL.glEnableClientState(OpenGL.GL_TEXTURE_COORD_ARRAY);
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "([I)V")
    @Override
    public void method7944(@OriginalArg(0) int[] arg0) {
        arg0[0] = this.anInt7869;
        arg0[1] = this.anInt7956;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "()Z")
    @Override
    public boolean method7992() {
        return true;
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "()Z")
    @Override
    public boolean method7983() {
        return true;
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(ZI)V")
    public void method7040(@OriginalArg(0) boolean arg0) {
        if (arg0 != this.aBoolean611) {
            this.aBoolean611 = arg0;
            this.method7009();
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(II[I[I)Lclient!aa;")
    @Override
    public ClippingMask createMask(@OriginalArg(0) int width, @OriginalArg(1) int height, @OriginalArg(2) int[] offsets, @OriginalArg(3) int[] widths) {
        return Static707.method9228(widths, this, offsets, height, width);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!ve;[Lclient!wp;Z)Lclient!da;")
    @Override
    public Font createFont(@OriginalArg(0) FontMetrics metrics, @OriginalArg(1) IndexedImage[] image, @OriginalArg(2) boolean monospaced) {
        return new Font_Sub2(this, metrics, image, monospaced);
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(Ljava/awt/Canvas;)V")
    @Override
    public void releaseSurface(@OriginalArg(0) Canvas canvas) {
        if (canvas == this.aCanvas10) {
            throw new RuntimeException();
        } else if (this.aHashtable5.containsKey(canvas)) {
            @Pc(23) Long local23 = (Long) this.aHashtable5.get(canvas);
            this.anOpenGL1.releaseSurface(canvas, local23);
            this.aHashtable5.remove(canvas);
        }
    }

    @OriginalMember(owner = "client!qha", name = "t", descriptor = "()Z")
    @Override
    public boolean supportsAntiAliasing() {
        return this.aBoolean615 && (!this.bloom() || this.aBoolean614);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(IIII)V")
    @Override
    public void method7959(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        this.aClass276_1.method6250(arg2, arg0, arg3, arg1);
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(JI)V")
    public synchronized void method7042(@OriginalArg(0) long arg0) {
        @Pc(7) Node local7 = new Node();
        local7.key = arg0;
        this.aDeque_53.addLast(local7);
    }

    @OriginalMember(owner = "client!qha", name = "b", descriptor = "(B)V")
    public void method7043() {
        if (this.anInt8005 != 3) {
            this.anInt8005 = 3;
            this.method6976();
            this.method7019();
            this.anInt7997 &= 0xFFFFFFF8;
        }
    }

    @OriginalMember(owner = "client!qha", name = "F", descriptor = "(II)V")
    @Override
    public void F(@OriginalArg(0) int x, @OriginalArg(1) int y) {
    }

    @OriginalMember(owner = "client!qha", name = "e", descriptor = "(II)V")
    @Override
    public void flip(@OriginalArg(0) int x, @OriginalArg(1) int y) throws FlipException {
        try {
            this.anOpenGL1.swapBuffers();
        } catch (@Pc(8) Exception local8) {
        }
    }

    @OriginalMember(owner = "client!qha", name = "w", descriptor = "(I)V")
    public void method7044() {
        if (this.aBoolean594 && this.anInt8008 >= 0) {
            OpenGL.glEnable(OpenGL.GL_FOG);
        } else {
            OpenGL.glDisable(OpenGL.GL_FOG);
        }
    }

    @OriginalMember(owner = "client!qha", name = "a", descriptor = "(Lclient!gaa;)V")
    @Override
    public void swapSurface(@OriginalArg(0) OffscreenSurface surface) {
    }

    @OriginalMember(owner = "client!qha", name = "k", descriptor = "()Z")
    @Override
    public boolean method7949() {
        return true;
    }

    @OriginalMember(owner = "client!qha", name = "d", descriptor = "(B)V")
    public void method7045() {
        this.anOpenGL1.a();
    }

    @OriginalMember(owner = "client!qha", name = "j", descriptor = "(II)V")
    public void method7046(@OriginalArg(0) int arg0) {
        this.method6975(true, arg0);
    }

    @OriginalMember(owner = "client!qha", name = "n", descriptor = "()Lclient!tt;")
    @Override
    public Matrix camera() {
        return this.aClass73_Sub3_3;
    }

    @OriginalMember(owner = "client!qha", name = "na", descriptor = "(IIII)[I")
    @Override
    public int[] na(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height) {
        @Pc(10) int[] local10 = new int[width * height];
        for (@Pc(12) int local12 = 0; local12 < height; local12++) {
            OpenGL.glReadPixelsi(x, this.anInt7956 - y - local12, width, 1, OpenGL.GL_BGRA, this.anInt8030, local10, local12 * width);
        }
        return local10;
    }

    @OriginalMember(owner = "client!qha", name = "p", descriptor = "()Z")
    @Override
    public boolean increaseRenderDistance() {
        return true;
    }
}

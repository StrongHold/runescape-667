import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!wf")
public final class JavaThreadResource {

    @OriginalMember(owner = "client!wf", name = "v", descriptor = "Z")
    public boolean fogActive;

    @OriginalMember(owner = "client!wf", name = "R", descriptor = "Ljava/lang/Runnable;")
    public Runnable thread;

    @OriginalMember(owner = "client!wf", name = "N", descriptor = "I")
    public int anInt10606;

    @OriginalMember(owner = "client!wf", name = "l", descriptor = "I")
    public int anInt10607;

    @OriginalMember(owner = "client!wf", name = "t", descriptor = "I")
    public int anInt10608;

    @OriginalMember(owner = "client!wf", name = "k", descriptor = "I")
    public int fogColour = 0;

    @OriginalMember(owner = "client!wf", name = "H", descriptor = "Z")
    public boolean zWrite = true;

    @OriginalMember(owner = "client!wf", name = "P", descriptor = "I")
    public int waterDepth = 0;

    @OriginalMember(owner = "client!wf", name = "z", descriptor = "I")
    public int waterHeight = 0;

    @OriginalMember(owner = "client!wf", name = "x", descriptor = "Z")
    public boolean water = false;

    @OriginalMember(owner = "client!wf", name = "j", descriptor = "I")
    public int anInt10600 = 0;

    @OriginalMember(owner = "client!wf", name = "M", descriptor = "Lclient!eaa;")
    public final JavaMatrix scratchMatrix = new JavaMatrix();

    @OriginalMember(owner = "client!wf", name = "h", descriptor = "[I")
    public final int[] anIntArray842 = new int[8];

    @OriginalMember(owner = "client!wf", name = "f", descriptor = "[I")
    public final int[] anIntArray838 = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "O", descriptor = "[I")
    public final int[] screenX = new int[64];

    @OriginalMember(owner = "client!wf", name = "o", descriptor = "[I")
    public final int[] anIntArray837 = new int[8];

    @OriginalMember(owner = "client!wf", name = "w", descriptor = "[I")
    public final int[] anIntArray844 = new int[8];

    @OriginalMember(owner = "client!wf", name = "C", descriptor = "[I")
    public final int[] anIntArray835 = new int[10000];

    @OriginalMember(owner = "client!wf", name = "i", descriptor = "[I")
    public final int[] anIntArray845 = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "d", descriptor = "[F")
    public final float[] aFloatArray82 = new float[2];

    @OriginalMember(owner = "client!wf", name = "s", descriptor = "[I")
    public final int[] anIntArray846 = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "e", descriptor = "[I")
    public final int[] anIntArray836 = new int[10000];

    @OriginalMember(owner = "client!wf", name = "F", descriptor = "[I")
    public final int[] screenY = new int[64];

    @OriginalMember(owner = "client!wf", name = "D", descriptor = "[I")
    public final int[] anIntArray848 = new int[10];

    @OriginalMember(owner = "client!wf", name = "E", descriptor = "[I")
    public final int[] anIntArray847 = new int[10];

    @OriginalMember(owner = "client!wf", name = "A", descriptor = "[I")
    public final int[] anIntArray850 = new int[10];

    @OriginalMember(owner = "client!wf", name = "m", descriptor = "[I")
    public final int[] anIntArray849 = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "n", descriptor = "[I")
    public final int[] anIntArray851 = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "G", descriptor = "[I")
    public final int[] anIntArray854 = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "c", descriptor = "[I")
    public final int[] anIntArray839 = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "r", descriptor = "[I")
    public final int[] fogLevels = new int[64];

    @OriginalMember(owner = "client!wf", name = "b", descriptor = "[I")
    public final int[] depths = new int[64];

    @OriginalMember(owner = "client!wf", name = "u", descriptor = "[I")
    public final int[] anIntArray841 = new int[10];

    @OriginalMember(owner = "client!wf", name = "g", descriptor = "[Lclient!rs;")
    public final JavaModel[] aClass114_Sub3Array4 = new JavaModel[7];

    @OriginalMember(owner = "client!wf", name = "Q", descriptor = "[Lclient!rs;")
    public final JavaModel[] aClass114_Sub3Array3 = new JavaModel[7];

    @OriginalMember(owner = "client!wf", name = "y", descriptor = "Lclient!iaa;")
    public final JavaToolkit aClass19_Sub2_12;

    @OriginalMember(owner = "client!wf", name = "I", descriptor = "I")
    public int fogPlane;

    @OriginalMember(owner = "client!wf", name = "p", descriptor = "Lclient!lb;")
    public Rasterizer rasterizer;

    @OriginalMember(owner = "client!wf", name = "B", descriptor = "[I")
    public final int[] anIntArray853;

    @OriginalMember(owner = "client!wf", name = "<init>", descriptor = "(Lclient!iaa;)V")
    public JavaThreadResource(@OriginalArg(0) JavaToolkit arg0) {
        this.aClass19_Sub2_12 = arg0;
        this.fogPlane = this.aClass19_Sub2_12.zFar - 255;
        this.rasterizer = new Rasterizer(arg0, this);
        for (@Pc(135) int local135 = 0; local135 < 7; local135++) {
            this.aClass114_Sub3Array4[local135] = new JavaModel(this.aClass19_Sub2_12);
            this.aClass114_Sub3Array3[local135] = new JavaModel(this.aClass19_Sub2_12);
        }
        this.anIntArray853 = new int[Static567.anInt8486];
        for (@Pc(166) int local166 = 0; local166 < Static567.anInt8486; local166++) {
            this.anIntArray853[local166] = -1;
        }
    }

    @OriginalMember(owner = "client!wf", name = "a", descriptor = "(Z)V")
    public void method9194() {
        this.rasterizer = new Rasterizer(this.aClass19_Sub2_12, this);
    }

    @OriginalMember(owner = "client!wf", name = "a", descriptor = "(Ljava/lang/Runnable;I)V")
    public void method9196(@OriginalArg(0) Runnable arg0) {
        this.thread = arg0;
    }
}

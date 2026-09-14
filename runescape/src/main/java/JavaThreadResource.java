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
    public int rasterMinX;

    @OriginalMember(owner = "client!wf", name = "l", descriptor = "I")
    public int rasterWidth;

    @OriginalMember(owner = "client!wf", name = "t", descriptor = "I")
    public int rasterMinY;

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
    public int savedFogColour = 0;

    @OriginalMember(owner = "client!wf", name = "M", descriptor = "Lclient!eaa;")
    public final JavaMatrix scratchMatrix = new JavaMatrix();

    @OriginalMember(owner = "client!wf", name = "h", descriptor = "[I")
    public final int[] boundsCornerY = new int[8];

    @OriginalMember(owner = "client!wf", name = "f", descriptor = "[I")
    public final int[] vertexScreenZ = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "O", descriptor = "[I")
    public final int[] screenX = new int[64];

    @OriginalMember(owner = "client!wf", name = "o", descriptor = "[I")
    public final int[] boundsCornerZ = new int[8];

    @OriginalMember(owner = "client!wf", name = "w", descriptor = "[I")
    public final int[] boundsCornerX = new int[8];

    @OriginalMember(owner = "client!wf", name = "C", descriptor = "[I")
    public final int[] mergeStamps = new int[10000];

    @OriginalMember(owner = "client!wf", name = "i", descriptor = "[I")
    public final int[] worldY = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "d", descriptor = "[F")
    public final float[] texCoordScratch = new float[2];

    @OriginalMember(owner = "client!wf", name = "s", descriptor = "[I")
    public final int[] vertexScreenY = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "e", descriptor = "[I")
    public final int[] otherMergeStamps = new int[10000];

    @OriginalMember(owner = "client!wf", name = "F", descriptor = "[I")
    public final int[] screenY = new int[64];

    @OriginalMember(owner = "client!wf", name = "D", descriptor = "[I")
    public final int[] clippedColour = new int[10];

    @OriginalMember(owner = "client!wf", name = "E", descriptor = "[I")
    public final int[] clippedX = new int[10];

    @OriginalMember(owner = "client!wf", name = "A", descriptor = "[I")
    public final int[] clippedY = new int[10];

    @OriginalMember(owner = "client!wf", name = "m", descriptor = "[I")
    public final int[] cameraY = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "n", descriptor = "[I")
    public final int[] vertexScreenX = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "G", descriptor = "[I")
    public final int[] cameraX = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "c", descriptor = "[I")
    public final int[] cameraZ = new int[Static567.anInt8484];

    @OriginalMember(owner = "client!wf", name = "r", descriptor = "[I")
    public final int[] fogLevels = new int[64];

    @OriginalMember(owner = "client!wf", name = "b", descriptor = "[I")
    public final int[] depths = new int[64];

    @OriginalMember(owner = "client!wf", name = "u", descriptor = "[I")
    public final int[] clippedZ = new int[10];

    @OriginalMember(owner = "client!wf", name = "g", descriptor = "[Lclient!rs;")
    public final JavaModel[] copyTargetPool = new JavaModel[7];

    @OriginalMember(owner = "client!wf", name = "Q", descriptor = "[Lclient!rs;")
    public final JavaModel[] copyBufferPool = new JavaModel[7];

    @OriginalMember(owner = "client!wf", name = "y", descriptor = "Lclient!iaa;")
    public final JavaToolkit toolkit;

    @OriginalMember(owner = "client!wf", name = "I", descriptor = "I")
    public int fogPlane;

    @OriginalMember(owner = "client!wf", name = "p", descriptor = "Lclient!lb;")
    public Rasterizer rasterizer;

    @OriginalMember(owner = "client!wf", name = "B", descriptor = "[I")
    public final int[] faceBillboard;

    @OriginalMember(owner = "client!wf", name = "<init>", descriptor = "(Lclient!iaa;)V")
    public JavaThreadResource(@OriginalArg(0) JavaToolkit toolkit) {
        this.toolkit = toolkit;
        this.fogPlane = this.toolkit.zFar - 255;
        this.rasterizer = new Rasterizer(toolkit, this);
        for (@Pc(135) int i = 0; i < 7; i++) {
            this.copyTargetPool[i] = new JavaModel(this.toolkit);
            this.copyBufferPool[i] = new JavaModel(this.toolkit);
        }
        this.faceBillboard = new int[Static567.anInt8486];
        for (@Pc(166) int i = 0; i < Static567.anInt8486; i++) {
            this.faceBillboard[i] = -1;
        }
    }

    @OriginalMember(owner = "client!wf", name = "a", descriptor = "(Z)V")
    public void resetRasterizer() {
        this.rasterizer = new Rasterizer(this.toolkit, this);
    }

    @OriginalMember(owner = "client!wf", name = "a", descriptor = "(Ljava/lang/Runnable;I)V")
    public void bindThread(@OriginalArg(0) Runnable thread) {
        this.thread = thread;
    }
}

import com.jagex.core.datastruct.key.Node2;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorType;
import com.jagex.graphics.particles.ModelParticleEffector;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!co")
public final class ParticleEffector extends Node2 {

    @OriginalMember(owner = "client!pf", name = "x", descriptor = "[I")
    private static final int[] PROJECTION = new int[3];

    @OriginalMember(owner = "client!co", name = "z", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!co", name = "x", descriptor = "I")
    public int directionZ;

    @OriginalMember(owner = "client!co", name = "y", descriptor = "I")
    public int y;

    @OriginalMember(owner = "client!co", name = "C", descriptor = "I")
    public int directionX;

    @OriginalMember(owner = "client!co", name = "D", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!co", name = "A", descriptor = "Lclient!mn;")
    public final ModelParticleEffector model;

    @OriginalMember(owner = "client!co", name = "w", descriptor = "Lclient!ok;")
    public final ParticleEffectorType type;

    @OriginalMember(owner = "client!co", name = "<init>", descriptor = "(Lclient!mn;Lclient!hv;)V")
    public ParticleEffector(@OriginalArg(0) ModelParticleEffector model, @OriginalArg(1) ParticleSystem system) {
        this.model = model;
        this.type = this.model.type();
        this.update();
    }

    @OriginalMember(owner = "client!co", name = "c", descriptor = "(B)V")
    public void update() {
        this.x = this.model.x;
        this.y = this.model.y;
        this.z = this.model.z;
        if (this.model.matrix != null) {
            this.model.matrix.projectDirection(this.type.dirX, this.type.dirY, this.type.dirZ, PROJECTION);
        }
        this.directionZ = PROJECTION[2];
        this.directionX = PROJECTION[0];
    }
}

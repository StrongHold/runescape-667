import com.jagex.core.io.Packet;
import com.jagex.game.MoveSpeed;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!lka")
public final class CutscenePath {

    @OriginalMember(owner = "client!lka", name = "c", descriptor = "[I")
    public final int[] waypoints;

    @OriginalMember(owner = "client!lka", name = "d", descriptor = "[I")
    public final int[] moveSpeeds;

    @OriginalMember(owner = "client!lka", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutscenePath(@OriginalArg(0) Packet packet) {
        @Pc(7) int count = packet.gsmart();
        this.waypoints = new int[count];
        this.moveSpeeds = new int[count];
        for (@Pc(17) int i = 0; i < count; i++) {
            @Pc(23) int moveSpeed = packet.g1();
            this.moveSpeeds[i] = moveSpeed;
            @Pc(32) int x = packet.g2();
            @Pc(36) int z = packet.g2();
            this.waypoints[i] = z + (x << 16);
        }
    }

    @OriginalMember(owner = "client!lka", name = "a", descriptor = "(Lclient!lw;II)V")
    public void walk(@OriginalArg(0) Actor actor, @OriginalArg(2) int level) {
        @Pc(10) int start = this.waypoints[0];
        actor.teleport(start >>> 16, level, start & 0xFFFF);
        @Pc(24) PathingEntity entity = actor.entity();
        entity.pathPointer = 0;
        for (@Pc(41) int i = this.moveSpeeds.length - 1; i >= 0; i--) {
            @Pc(48) int moveSpeed = this.moveSpeeds[i];
            @Pc(53) int waypoint = this.waypoints[i];
            entity.pathX[entity.pathPointer] = waypoint >> 16;
            entity.pathZ[entity.pathPointer] = waypoint & 0xFFFF;
            @Pc(71) byte speed = MoveSpeed.WALK;
            if (moveSpeed == MoveSpeed.CRAWL) {
                speed = MoveSpeed.CRAWL;
            } else if (moveSpeed == MoveSpeed.RUN) {
                speed = MoveSpeed.RUN;
            }
            entity.pathSpeed[entity.pathPointer] = speed;
            entity.pathPointer++;
        }
    }
}

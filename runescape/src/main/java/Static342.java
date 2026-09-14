import com.jagex.Client;
import com.jagex.Static148;
import com.jagex.game.collision.CollisionMap;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static342 {

    @OriginalMember(owner = "client!kp", name = "a", descriptor = "(Lclient!ke;B)Z")
    public static boolean method4463(@OriginalArg(0) BoundingCylinder arg0) {
        return arg0 == null ? false : Static318.isBoxOccluded(arg0.x2 - arg0.x1, -arg0.y1 + arg0.y2, arg0.x1, arg0.z1, arg0.y1, arg0.z2 - arg0.z1);
    }

    @OriginalMember(owner = "client!kp", name = "a", descriptor = "(BI)V")
    public static void setBuildArea(@OriginalArg(1) int buildArea) {
        if (buildArea == Static537.buildArea) {
            return;
        }

        Static720.mapWidth = Static501.mapLength = Static238.BUILD_AREAS[buildArea];
        Static209.method3110();
        Static623.zonePointers = new int[4][Static720.mapWidth >> 3][Static501.mapLength >> 3];
        Static148.anIntArrayArray64 = new int[Static720.mapWidth][Static501.mapLength];
        Static341.entityDrawPriorities = new int[Static720.mapWidth][Static501.mapLength];
        for (@Pc(35) int local35 = 0; local35 < 4; local35++) {
            Client.collisionMaps[local35] = CollisionMap.create(Static720.mapWidth, Static501.mapLength);
        }
        Static328.aByteArrayArrayArray4 = new byte[4][Static720.mapWidth][Static501.mapLength];
        Static708.resetTileFlags(Static720.mapWidth, Static501.mapLength);
        Static613.method8239(Toolkit.active, Static501.mapLength >> 3, Static720.mapWidth >> 3);
        Static537.buildArea = buildArea;
    }
}

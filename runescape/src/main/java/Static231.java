import com.jagex.graphics.Toolkit;
import com.jagex.math.IntMath;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static231 {

    @OriginalMember(owner = "client!hd", name = "t", descriptor = "I")
    public static int anInt3734;

    @OriginalMember(owner = "client!hd", name = "u", descriptor = "Lclient!hd;")
    public static final Class157 aClass157_1 = new Class157(0, 3, Static702.aClass397_20);

    @OriginalMember(owner = "client!hd", name = "f", descriptor = "Lclient!hd;")
    public static final Class157 aClass157_2 = new Class157(1, 3, Static702.aClass397_20);

    @OriginalMember(owner = "client!hd", name = "b", descriptor = "Lclient!hd;")
    public static final Class157 aClass157_3 = new Class157(2, 4, Static702.aClass397_16);

    @OriginalMember(owner = "client!hd", name = "a", descriptor = "Lclient!hd;")
    public static final Class157 aClass157_4 = new Class157(3, 1, Static702.aClass397_20);

    @OriginalMember(owner = "client!hd", name = "v", descriptor = "Lclient!hd;")
    public static final Class157 aClass157_5 = new Class157(4, 2, Static702.aClass397_20);

    @OriginalMember(owner = "client!hd", name = "d", descriptor = "Lclient!hd;")
    public static final Class157 aClass157_6 = new Class157(5, 3, Static702.aClass397_20);

    @OriginalMember(owner = "client!hd", name = "l", descriptor = "Lclient!hd;")
    public static final Class157 aClass157_7 = new Class157(6, 4, Static702.aClass397_20);

    @OriginalMember(owner = "client!hd", name = "c", descriptor = "I")
    public static final int anInt3733 = IntMath.countBits(16);

    @OriginalMember(owner = "client!hd", name = "b", descriptor = "(I)V")
    public static void method3375() {
        if (Toolkit.active == null) {
            return;
        }
        if (OrthoMode.toolkitActive) {
            Static164.method2606();
        }
        Static514.activePickList.clear();
        Static563.method7461();
        Minimap.reset();
        MiniMenu.resetSprites();
        Static638.method8393();
        Static65.method1472();
        Static81.method1589();
        client.cacheReset();
        Static203.resetStaticSprites();
        Static143.method3572();
        WorldMap.reset(false);
        for (@Pc(34) int local34 = 0; local34 < 2048; local34++) {
            @Pc(39) PlayerEntity local39 = PlayerList.highResolutionPlayers[local34];
            if (local39 != null) {
                for (@Pc(43) int local43 = 0; local43 < local39.aModelArray3.length; local43++) {
                    local39.aModelArray3[local43] = null;
                }
            }
        }
        for (@Pc(65) int local65 = 0; local65 < NPCList.newSize; local65++) {
            @Pc(71) NPCEntity entity = NPCList.entities[local65].npc;
            if (entity != null) {
                for (@Pc(75) int local75 = 0; local75 < entity.aModelArray3.length; local75++) {
                    entity.aModelArray3[local75] = null;
                }
            }
        }
        Static59.aMatrix_5 = null;
        Static460.aMatrix_10 = null;
        Toolkit.active.free();
        Toolkit.active = null;
    }

    @OriginalMember(owner = "client!hd", name = "a", descriptor = "(II)Lclient!hd;")
    public static Class157 method3377(@OriginalArg(0) int arg0) {
        if (arg0 == 0) {
            return aClass157_1;
        } else if (arg0 == 1) {
            return aClass157_2;
        } else if (arg0 == 2) {
            return aClass157_3;
        } else if (arg0 == 3) {
            return aClass157_4;
        } else if (arg0 == 4) {
            return aClass157_5;
        } else if (arg0 == 5) {
            return aClass157_6;
        } else if (arg0 == 6) {
            return aClass157_7;
        } else {
            return null;
        }
    }

}

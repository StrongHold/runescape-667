import com.jagex.game.runetek6.config.meltype.MapElementType;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static122 {

    @OriginalMember(owner = "client!dq", name = "b", descriptor = "(B)Lclient!fu;")
    public static MapElementListEntry method2207() {
        if (WorldMap.elements == null || Static444.A_DEQUE_ITERATOR___1 == null) {
            return null;
        }
        Static444.A_DEQUE_ITERATOR___1.setDeque(WorldMap.elements);
        @Pc(23) MapElementListEntry local23 = (MapElementListEntry) Static444.A_DEQUE_ITERATOR___1.first();
        if (local23 == null) {
            return null;
        } else {
            @Pc(42) MapElementType local42 = WorldMap.mapElementTypeList.list(local23.id);
            return local42 != null && local42.aBoolean217 && local42.variableTest(WorldMap.varDomain) ? local23 : Static364.method5248();
        }
    }

}

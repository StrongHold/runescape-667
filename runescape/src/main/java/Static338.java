import com.jagex.Client;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static338 {

    @OriginalMember(owner = "client!km", name = "a", descriptor = "(I[[BLclient!taa;)V")
    public static void loadStaticLocations(@OriginalArg(1) byte[][] locations, @OriginalArg(2) MapRegion region) {
        @Pc(6) int length = Static319.aByteArrayArray16.length;
        for (@Pc(8) int i = 0; i < length; i++) {
            @Pc(13) byte[] data = locations[i];
            if (data != null) {
                @Pc(26) int x = (Static89.zoneIds[i] >> 8) * 64 - WorldMap.areaBaseX;
                @Pc(36) int z = (Static89.zoneIds[i] & 0xFF) * 64 - WorldMap.areaBaseZ;
                Static557.updatePcmPlayers();
                region.loadLocations(x, z, Client.collisionMaps, Toolkit.active, data);
            }
        }
    }

}

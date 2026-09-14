import com.jagex.core.constants.AreaMode;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.io.BitPacket;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static434 {

    @OriginalMember(owner = "client!nla", name = "g", descriptor = "(I)V")
    public static void rebuildNormal() {
        Static117.areaMode = AreaMode.STATIC_AREA;
        @Pc(8) BitPacket bitPacket = ServerConnection.GAME.bitPacket;
        @Pc(12) int buildArea = bitPacket.g1_alt2();
        @Pc(24) boolean forceUpdate = bitPacket.g1() == 1;
        @Pc(28) int centerX = bitPacket.ig2();
        @Pc(32) int centerZ = bitPacket.g2();
        Static165.updateLastAreaMode();
        Static342.setBuildArea(buildArea);
        @Pc(46) int mapsquares = (ServerConnection.GAME.currentPacketSize - bitPacket.pos) / 16;
        Static22.anIntArrayArray11 = new int[mapsquares][4];
        @Pc(55) int local55;
        for (@Pc(52) int mapsquare = 0; mapsquare < mapsquares; mapsquare++) {
            for (local55 = 0; local55 < 4; local55++) {
                Static22.anIntArrayArray11[mapsquare][local55] = bitPacket.g4();
            }
        }
        Static363.aByteArrayArray22 = null;
        Static319.aByteArrayArray16 = new byte[mapsquares][];
        Static118.aByteArrayArray3 = new byte[mapsquares][];
        Static266.locationGroups = new int[mapsquares];
        Static177.aByteArrayArray5 = new byte[mapsquares][];
        Static267.mapGroups = new int[mapsquares];
        Static68.underwaterMapGroups = new int[mapsquares];
        Static298.underwaterLocationGroups = new int[mapsquares];
        Static376.npcGroups = null;
        Static89.zoneIds = new int[mapsquares];
        Static421.aByteArrayArray19 = new byte[mapsquares][];
        mapsquares = 0;
        for (local55 = (centerX - (Static720.mapWidth >> 4)) / 8; local55 <= ((Static720.mapWidth >> 4) + centerX) / 8; local55++) {
            for (@Pc(137) int local137 = (centerZ - (Static501.mapLength >> 4)) / 8; local137 <= (centerZ + (Static501.mapLength >> 4)) / 8; local137++) {
                Static89.zoneIds[mapsquares] = (local55 << 8) + local137;
                Static267.mapGroups[mapsquares] = js5.MAPS.getgroupid("m" + local55 + "_" + local137);
                Static266.locationGroups[mapsquares] = js5.MAPS.getgroupid("l" + local55 + "_" + local137);
                Static68.underwaterMapGroups[mapsquares] = js5.MAPS.getgroupid("um" + local55 + "_" + local137);
                Static298.underwaterLocationGroups[mapsquares] = js5.MAPS.getgroupid("ul" + local55 + "_" + local137);
                mapsquares++;
            }
        }
        MapArea.updateMapArea(forceUpdate, centerX, MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD, centerZ);
    }
}

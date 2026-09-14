import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.io.BitPacket;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class RebuildRegion {

    private static final int XTEA_KEY_INTS = 4;

    private static final int XTEA_KEY_BYTES = XTEA_KEY_INTS * 4;

    @OriginalMember(owner = "client!om", name = "b", descriptor = "(Z)V")
    public static void rebuildRegion() {
        @Pc(8) BitPacket bitPacket = ServerConnection.GAME.bitPacket;
        @Pc(12) int zoneZ = bitPacket.g2();
        @Pc(16) int buildArea = bitPacket.g1();
        @Pc(28) boolean forceUpdate = bitPacket.g1_alt3() == 1;
        Static117.areaMode = bitPacket.g1_alt3();
        @Pc(36) int zoneX = bitPacket.ig2();
        Static165.updateLastAreaMode();
        Static342.setBuildArea(buildArea);
        bitPacket.enterBitMode();
        for (@Pc(46) int level = 0; level < 4; level++) {
            for (@Pc(50) int x = 0; x < Static720.mapWidth >> 3; x++) {
                for (@Pc(54) int z = 0; z < Static501.mapLength >> 3; z++) {
                    @Pc(61) int present = bitPacket.gbit(1);
                    if (present == 1) {
                        Static623.zonePointers[level][x][z] = bitPacket.gbit(26);
                    } else {
                        Static623.zonePointers[level][x][z] = -1;
                    }
                }
            }
        }
        bitPacket.exitBitMode();

        int mapsquareCount = (ServerConnection.GAME.currentPacketSize - bitPacket.pos) / XTEA_KEY_BYTES;
        Static22.anIntArrayArray11 = new int[mapsquareCount][XTEA_KEY_INTS];
        for (int mapsquare = 0; mapsquare < mapsquareCount; mapsquare++) {
            for (int part = 0; part < XTEA_KEY_INTS; part++) {
                Static22.anIntArrayArray11[mapsquare][part] = bitPacket.g4();
            }
        }

        Static118.aByteArrayArray3 = new byte[mapsquareCount][];
        Static177.aByteArrayArray5 = new byte[mapsquareCount][];
        Static266.locationGroups = new int[mapsquareCount];
        Static376.npcGroups = null;
        Static89.zoneIds = new int[mapsquareCount];
        Static298.underwaterLocationGroups = new int[mapsquareCount];
        Static421.aByteArrayArray19 = new byte[mapsquareCount][];
        Static267.mapGroups = new int[mapsquareCount];
        Static319.aByteArrayArray16 = new byte[mapsquareCount][];
        Static363.aByteArrayArray22 = null;
        Static68.underwaterMapGroups = new int[mapsquareCount];

        int mapsquares = 0;
        for (int level = 0; level < 4; level++) {
            for (@Pc(221) int x = 0; x < Static720.mapWidth >> 3; x++) {
                for (@Pc(225) int z = 0; z < Static501.mapLength >> 3; z++) {
                    @Pc(235) int pointer = Static623.zonePointers[level][x][z];
                    if (pointer != -1) {
                        @Pc(245) int sourceZoneX = pointer >> 14 & 0x3FF;
                        @Pc(251) int sourceZoneZ = pointer >> 3 & 0x7FF;
                        @Pc(261) int mapsquareId = sourceZoneZ / 8 + (sourceZoneX / 8 << 8);
                        for (@Pc(263) int seen = 0; seen < mapsquares; seen++) {
                            if (Static89.zoneIds[seen] == mapsquareId) {
                                mapsquareId = -1;
                                break;
                            }
                        }
                        if (mapsquareId != -1) {
                            Static89.zoneIds[mapsquares] = mapsquareId;
                            @Pc(299) int mapsquareX = mapsquareId >> 8 & 0xFF;
                            @Pc(303) int mapsquareZ = mapsquareId & 0xFF;
                            Static267.mapGroups[mapsquares] = js5.MAPS.getgroupid("m" + mapsquareX + "_" + mapsquareZ);
                            Static266.locationGroups[mapsquares] = js5.MAPS.getgroupid("l" + mapsquareX + "_" + mapsquareZ);
                            Static68.underwaterMapGroups[mapsquares] = js5.MAPS.getgroupid("um" + mapsquareX + "_" + mapsquareZ);
                            Static298.underwaterLocationGroups[mapsquares] = js5.MAPS.getgroupid("ul" + mapsquareX + "_" + mapsquareZ);
                            mapsquares++;
                        }
                    }
                }
            }
        }
        Static684.updateMapArea(forceUpdate, zoneX, MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD, zoneZ);
    }

}

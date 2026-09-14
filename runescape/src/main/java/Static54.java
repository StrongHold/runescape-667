import com.jagex.core.constants.MainLogicStep;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static54 {

    @OriginalMember(owner = "client!bq", name = "a", descriptor = "(I)V")
    public static void method1179() {
        Static342.setBuildArea(ClientOptions.instance.buildArea.getValue());
        @Pc(19) int centerX = (WorldMap.areaBaseX >> 3) + (Camera.x >> 12);
        @Pc(28) int centerZ = (Camera.z >> 12) + (WorldMap.areaBaseZ >> 3);
        Camera.renderingLevel = PlayerEntity.self.level = 0;
        PlayerEntity.self.teleport(8, 8);
        Static22.anIntArrayArray11 = new int[18][4];
        Static363.aByteArrayArray22 = new byte[18][];
        Static177.aByteArrayArray5 = new byte[18][];
        Static267.mapGroups = new int[18];
        Static68.underwaterMapGroups = new int[18];
        Static266.locationGroups = new int[18];
        Static89.zoneIds = new int[18];
        Static421.aByteArrayArray19 = new byte[18][];
        Static298.underwaterLocationGroups = new int[18];
        Static376.npcGroups = new int[18];
        Static319.aByteArrayArray16 = new byte[18][];
        Static118.aByteArrayArray3 = new byte[18][];

        @Pc(79) int count = 0;
        for (@Pc(88) int x = (centerX - (Static720.mapWidth >> 4)) / 8; x <= (centerX + (Static720.mapWidth >> 4)) / 8; x++) {
            for (@Pc(99) int z = (centerZ - (Static501.mapLength >> 4)) / 8; z <= (centerZ + (Static501.mapLength >> 4)) / 8; z++) {
                @Pc(107) int zoneId = (x << 8) + z;

                Static89.zoneIds[count] = zoneId;
                Static267.mapGroups[count] = js5.MAPS.getgroupid("m" + x + "_" + z);
                Static266.locationGroups[count] = js5.MAPS.getgroupid("l" + x + "_" + z);
                Static376.npcGroups[count] = js5.MAPS.getgroupid("n" + x + "_" + z);
                Static68.underwaterMapGroups[count] = js5.MAPS.getgroupid("um" + x + "_" + z);
                Static298.underwaterLocationGroups[count] = js5.MAPS.getgroupid("ul" + x + "_" + z);

                if (Static376.npcGroups[count] == -1) {
                    Static267.mapGroups[count] = -1;
                    Static266.locationGroups[count] = -1;
                    Static68.underwaterMapGroups[count] = -1;
                    Static298.underwaterLocationGroups[count] = -1;
                }

                count++;
            }
        }

        for (@Pc(99) int i = count; i < Static376.npcGroups.length; i++) {
            Static376.npcGroups[i] = -1;
            Static267.mapGroups[i] = -1;
            Static266.locationGroups[i] = -1;
            Static68.underwaterMapGroups[i] = -1;
            Static298.underwaterLocationGroups[i] = -1;
        }

        @Pc(309) byte buildStep;
        if (MainLogicManager.step == MainLogicStep.STEP_LOGIN_SCREEN) {
            buildStep = MainLogicStep.STEP_LOGIN_SCREEN_MAP_BUILD;
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME) {
            buildStep = MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME_MAP_BUILD;
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN) {
            buildStep = MainLogicStep.STEP_LOBBY_SCREEN_MAP_BUILD;
        } else {
            throw new RuntimeException(String.valueOf(MainLogicManager.step));
        }

        MapArea.updateMapArea(false, centerX, buildStep, centerZ);
    }

    @OriginalMember(owner = "client!bq", name = "a", descriptor = "(IBI)Z")
    public static boolean method1183(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x84080) != 0;
    }
}

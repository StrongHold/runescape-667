import com.jagex.core.constants.AreaMode;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.io.FileOnDisk;
import com.jagex.core.io.Packet;
import com.jagex.core.util.SystemTimer;
import com.jagex.js5.js5;
import com.jagex.sign.SignLink;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static266 {

    @OriginalMember(owner = "client!ifa", name = "a", descriptor = "[I")
    public static int[] locationGroups;

    @OriginalMember(owner = "client!ifa", name = "a", descriptor = "(I)V")
    public static void method6774() {
        Static117.areaMode = AreaMode.DEFAULT;
        Static102.lastAreaMode = AreaMode.DEFAULT;
        Static342.setBuildArea(0);
        @Pc(22) int local22;
        for (@Pc(14) int level = 0; level < 4; level++) {
            for (@Pc(18) int zoneX = 0; zoneX < Static720.mapWidth >> 3; zoneX++) {
                for (local22 = 0; local22 < Static501.mapLength >> 3; local22++) {
                    Static623.zonePointers[level][zoneX][local22] = -1;
                }
            }
        }
        @Pc(93) int srcZoneX;
        @Pc(98) int srcZoneZ;
        @Pc(101) int local101;
        @Pc(104) int local104;
        @Pc(107) int local107;
        @Pc(110) int local110;
        @Pc(113) int local113;
        @Pc(116) int local116;
        for (@Pc(71) Node_Sub35 area = (Node_Sub35) Static391.A_DEQUE___34.first(); area != null; area = (Node_Sub35) Static391.A_DEQUE___34.next()) {
            local22 = area.anInt5689;
            @Pc(88) boolean swapAxes = (local22 & 0x1) == 1;
            srcZoneX = area.anInt5691 >> 3;
            srcZoneZ = area.anInt5692 >> 3;
            local101 = area.anInt5685;
            local104 = area.anInt5696;
            local107 = area.anInt5695;
            local110 = area.anInt5690;
            local113 = area.anInt5686;
            local116 = area.anInt5694;
            @Pc(118) int offsetZ = 0;
            @Pc(120) int startOffsetX = 0;
            @Pc(122) byte stepX = 1;
            @Pc(124) byte stepZ = 1;
            if (local22 == 1) {
                stepX = -1;
                startOffsetX = local113 - 1;
            } else if (local22 == 2) {
                stepZ = -1;
                stepX = -1;
                startOffsetX = local113 - 1;
                offsetZ = local116 - 1;
            } else if (local22 == 3) {
                stepX = 1;
                offsetZ = local116 - 1;
                stepZ = -1;
            }
            for (@Pc(171) int srcZ = srcZoneZ; srcZ < local116 + srcZoneZ; srcZ++) {
                @Pc(175) int offsetX = startOffsetX;
                @Pc(177) int srcX = srcZoneX;
                while (srcX < srcZoneX + local113) {
                    if (swapAxes) {
                        Static623.zonePointers[local110][offsetZ + local101][offsetX + local104] = (srcZ << 3) + (srcX << 14) + (local107 << 24) + (local22 << 1);
                    } else {
                        Static623.zonePointers[local110][offsetX + local101][local104 + offsetZ] = (local22 << 1) + (srcZ << 3) + (local107 << 24) + (srcX << 14);
                    }
                    srcX++;
                    offsetX += stepX;
                }
                offsetZ += stepZ;
            }
        }
        local22 = CutsceneManager.anIntArrayArray265.length;
        locationGroups = new int[local22];
        Static89.zoneIds = new int[local22];
        Static319.aByteArrayArray16 = new byte[local22][];
        Static298.underwaterLocationGroups = new int[local22];
        Static363.aByteArrayArray22 = null;
        Static118.aByteArrayArray3 = new byte[local22][];
        Static177.aByteArrayArray5 = new byte[local22][];
        Static267.mapGroups = new int[local22];
        Static68.underwaterMapGroups = new int[local22];
        Static376.npcGroups = null;
        Static421.aByteArrayArray19 = new byte[local22][];
        local22 = 0;
        for (@Pc(312) Node_Sub35 area = (Node_Sub35) Static391.A_DEQUE___34.first(); area != null; area = (Node_Sub35) Static391.A_DEQUE___34.next()) {
            srcZoneX = area.anInt5691 >>> -2127211805;
            srcZoneZ = area.anInt5692 >>> 3;
            local101 = area.anInt5686 + srcZoneX;
            if ((local101 & 0x7) == 0) {
                local101--;
            }
            local101 >>>= 0x3;
            local104 = srcZoneZ + area.anInt5694;
            if ((local104 & 0x7) == 0) {
                local104--;
            }
            local104 >>>= 0x3;
            for (local107 = srcZoneX >>> 3; local107 <= local101; local107++) {
                label82:
                for (local110 = srcZoneZ >>> 3; local110 <= local104; local110++) {
                    local113 = local107 << 8 | local110;
                    for (local116 = 0; local116 < local22; local116++) {
                        if (local113 == Static89.zoneIds[local116]) {
                            continue label82;
                        }
                    }
                    Static89.zoneIds[local22] = local113;
                    Static267.mapGroups[local22] = js5.MAPS.getgroupid("m" + local107 + "_" + local110);
                    locationGroups[local22] = js5.MAPS.getgroupid("l" + local107 + "_" + local110);
                    Static68.underwaterMapGroups[local22] = js5.MAPS.getgroupid("um" + local107 + "_" + local110);
                    Static298.underwaterLocationGroups[local22] = js5.MAPS.getgroupid("ul" + local107 + "_" + local110);
                    local22++;
                }
            }
        }
        Static22.anIntArrayArray11 = CutsceneManager.anIntArrayArray265;
        CutsceneManager.anIntArrayArray265 = null;
        MapArea.updateMapArea(false, Static720.mapWidth >> 4, MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD, Static501.mapLength >> 4);
    }

    @OriginalMember(owner = "client!ifa", name = "a", descriptor = "(B)V")
    public static void saveVarcs() {
        @Pc(5) FileOnDisk file = null;
        try {
            file = SignLink.openPrefs("2");

            @Pc(25) Packet packet = new Packet((Static319.permVarcCount * 6) + 3);
            packet.p1(1);
            packet.p2(Static319.permVarcCount);

            for (@Pc(35) int i = 0; i < Static511.varcs.length; i++) {
                if (Static118.permVarcs[i]) {
                    packet.p2(i);
                    packet.p4(Static511.varcs[i]);
                }
            }

            file.write(packet.data, 0, packet.pos);
        } catch (@Pc(67) Exception ex) {
            // g.error("Unable to save varcs", (Throwable)var15);
        }

        try {
            if (file != null) {
                file.close();
            }
        } catch (@Pc(74) Exception ex) {
        }

        Static98.lastVarcSave = SystemTimer.safetime();
        Static624.varcSaveRecommended = false;
    }
}

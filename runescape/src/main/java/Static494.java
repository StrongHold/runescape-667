import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static494 {

    @OriginalMember(owner = "client!pja", name = "kb", descriptor = "I")
    public static int anInt7409;

    @OriginalMember(owner = "client!pja", name = "rb", descriptor = "I")
    public static int runWeight = 0;

    @OriginalMember(owner = "client!pja", name = "e", descriptor = "(B)V")
    public static void method6597() {
        Static659.blockChat = 0;
        @Pc(17) int worldX = (PlayerEntity.self.x >> 9) + WorldMap.areaBaseX;
        @Pc(24) int worldZ = WorldMap.areaBaseZ + (PlayerEntity.self.z >> 9);
        if (worldX >= 3053 && worldX <= 3156 && worldZ >= 3056 && worldZ <= 3136) {
            Static659.blockChat = 1;
        }
        if (worldX >= 3072 && worldX <= 3118 && worldZ >= 9492 && worldZ <= 9535) {
            Static659.blockChat = 1;
        }
        if (Static659.blockChat == 1 && worldX >= 3139 && worldX <= 3199 && worldZ >= 3008 && worldZ <= 3062) {
            Static659.blockChat = 0;
        }
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(ILjava/lang/String;Z)V")
    public static void method6599(@OriginalArg(1) String query, @OriginalArg(2) boolean global) {
        @Pc(6) String lower = query.toLowerCase();
        @Pc(9) short[] matches = new short[16];
        @Pc(11) int count = 0;
        @Pc(18) int base = global ? 32768 : 0;
        @Pc(29) int limit = (global ? QuickChatPhraseTypeList.instance.anInt3261 : QuickChatPhraseTypeList.instance.anInt3264) + base;
        @Pc(80) int i;
        for (@Pc(31) int id = base; id < limit; id++) {
            @Pc(37) QuickChatPhraseType phrase = QuickChatPhraseTypeList.instance.get(id);
            if (phrase.searchable && phrase.getText().toLowerCase().indexOf(lower) != -1) {
                if (count >= 50) {
                    ObjFinder.results = null;
                    ObjFinder.resultCount = -1;
                    return;
                }
                if (count >= matches.length) {
                    @Pc(78) short[] grown = new short[matches.length * 2];
                    for (i = 0; i < count; i++) {
                        grown[i] = matches[i];
                    }
                    matches = grown;
                }
                matches[count++] = (short) id;
            }
        }
        ObjFinder.pointer = 0;
        ObjFinder.resultCount = count;
        ObjFinder.results = matches;
        @Pc(120) String[] texts = new String[ObjFinder.resultCount];
        for (i = 0; i < ObjFinder.resultCount; i++) {
            texts[i] = QuickChatPhraseTypeList.instance.get(matches[i]).getText();
        }
        ObjFinder.quicksort(texts, ObjFinder.results);
    }

    @OriginalMember(owner = "client!pja", name = "a", descriptor = "(ILclient!dda;ILclient!ha;)V")
    public static void method6601(@OriginalArg(0) int level, @OriginalArg(1) LocOccluder occluder, @OriginalArg(3) Toolkit toolkit) {
        @Pc(15) int local15;
        if (Static617.roofMaxY != null && level <= occluder.aByte44) {
            for (local15 = 0; local15 < Static617.roofMaxY.length; local15++) {
                if (Static617.roofMaxY[local15] != -1000000 && (occluder.anIntArray186[0] <= Static617.roofMaxY[local15] || Static617.roofMaxY[local15] >= occluder.anIntArray186[1] || Static617.roofMaxY[local15] >= occluder.anIntArray186[2] || Static617.roofMaxY[local15] >= occluder.anIntArray186[3]) && (Static419.roofMaxX[local15] >= occluder.anIntArray185[0] || occluder.anIntArray185[1] <= Static419.roofMaxX[local15] || occluder.anIntArray185[2] <= Static419.roofMaxX[local15] || Static419.roofMaxX[local15] >= occluder.anIntArray185[3]) && (occluder.anIntArray185[0] >= Static714.roofMinX[local15] || Static714.roofMinX[local15] <= occluder.anIntArray185[1] || occluder.anIntArray185[2] >= Static714.roofMinX[local15] || Static714.roofMinX[local15] <= occluder.anIntArray185[3]) && (occluder.anIntArray188[0] <= Static219.roofMaxZ[local15] || occluder.anIntArray188[1] <= Static219.roofMaxZ[local15] || Static219.roofMaxZ[local15] >= occluder.anIntArray188[2] || Static219.roofMaxZ[local15] >= occluder.anIntArray188[3]) && (Static665.roofMinZ[local15] <= occluder.anIntArray188[0] || Static665.roofMinZ[local15] <= occluder.anIntArray188[1] || Static665.roofMinZ[local15] <= occluder.anIntArray188[2] || Static665.roofMinZ[local15] <= occluder.anIntArray188[3])) {
                    return;
                }
            }
        }
        @Pc(323) int spanFrom;
        @Pc(353) int spanTo;
        @Pc(375) boolean spanVisible;
        @Pc(410) float distance;
        if (occluder.aByte43 == 1) {
            local15 = Static35.anInt813 + occluder.aShort26 - Static403.anInt6246;
            if (local15 >= 0 && local15 <= Static35.anInt813 + Static35.anInt813) {
                spanFrom = occluder.aShort23 + Static35.anInt813 - Static550.anInt8271;
                if (spanFrom < 0) {
                    spanFrom = 0;
                } else if (spanFrom > Static35.anInt813 + Static35.anInt813) {
                    return;
                }
                spanTo = Static35.anInt813 + occluder.aShort25 - Static550.anInt8271;
                if (Static35.anInt813 + Static35.anInt813 < spanTo) {
                    spanTo = Static35.anInt813 + Static35.anInt813;
                } else if (spanTo < 0) {
                    return;
                }
                spanVisible = false;
                while (spanFrom <= spanTo) {
                    if (Static258.aBooleanArrayArray3[local15][spanFrom++]) {
                        spanVisible = true;
                        break;
                    }
                }
                if (spanVisible) {
                    distance = (float) (Static499.cameraX - occluder.anIntArray185[0]);
                    if (distance < 0.0F) {
                        distance *= -1.0F;
                    }
                    if (!((float) Static86.anInt1803 > distance) && (Static219.method3190(0, occluder) && (Static219.method3190(1, occluder) && (Static219.method3190(2, occluder) && Static219.method3190(3, occluder))))) {
                        Static560.aLocOccluderArray3[Static469.activeOccluderCount++] = occluder;
                    }
                }
            }
        } else if (occluder.aByte43 == 2) {
            local15 = Static35.anInt813 + occluder.aShort23 - Static550.anInt8271;
            if (local15 >= 0 && local15 <= Static35.anInt813 + Static35.anInt813) {
                spanFrom = Static35.anInt813 + occluder.aShort26 - Static403.anInt6246;
                if (spanFrom < 0) {
                    spanFrom = 0;
                } else if (spanFrom > Static35.anInt813 + Static35.anInt813) {
                    return;
                }
                spanTo = Static35.anInt813 + occluder.aShort24 - Static403.anInt6246;
                if (Static35.anInt813 + Static35.anInt813 < spanTo) {
                    spanTo = Static35.anInt813 + Static35.anInt813;
                } else if (spanTo < 0) {
                    return;
                }
                spanVisible = false;
                while (spanFrom <= spanTo) {
                    if (Static258.aBooleanArrayArray3[spanFrom++][local15]) {
                        spanVisible = true;
                        break;
                    }
                }
                if (spanVisible) {
                    distance = (float) (Static715.cameraZ - occluder.anIntArray188[0]);
                    if (distance < 0.0F) {
                        distance *= -1.0F;
                    }
                    if (!(distance < (float) Static86.anInt1803) && (Static219.method3190(0, occluder) && (Static219.method3190(1, occluder) && (Static219.method3190(2, occluder) && Static219.method3190(3, occluder))))) {
                        Static560.aLocOccluderArray3[Static469.activeOccluderCount++] = occluder;
                    }
                }
            }
        } else if (occluder.aByte43 == 16 || occluder.aByte43 == 8) {
            local15 = occluder.aShort26 + Static35.anInt813 - Static403.anInt6246;
            if (local15 >= 0 && Static35.anInt813 + Static35.anInt813 >= local15) {
                spanFrom = occluder.aShort23 + Static35.anInt813 - Static550.anInt8271;
                if (spanFrom >= 0 && Static35.anInt813 + Static35.anInt813 >= spanFrom && Static258.aBooleanArrayArray3[local15][spanFrom]) {
                    @Pc(697) float distanceX = (float) (Static499.cameraX - occluder.anIntArray185[0]);
                    if (distanceX < 0.0F) {
                        distanceX *= -1.0F;
                    }
                    @Pc(714) float distanceZ = (float) (Static715.cameraZ - occluder.anIntArray188[0]);
                    if (distanceZ < 0.0F) {
                        distanceZ *= -1.0F;
                    }
                    if ((!((float) Static86.anInt1803 > distanceX) || !((float) Static86.anInt1803 > distanceZ)) && (Static219.method3190(0, occluder) && (Static219.method3190(1, occluder) && (Static219.method3190(2, occluder) && Static219.method3190(3, occluder))))) {
                        Static560.aLocOccluderArray3[Static469.activeOccluderCount++] = occluder;
                    }
                }
            }
        } else if (occluder.aByte43 == 4) {
            @Pc(787) float distanceY = (float) (occluder.anIntArray186[0] - Static523.cameraY);
            if (!(distanceY <= (float) Static663.anInt9874)) {
                spanFrom = occluder.aShort23 + Static35.anInt813 - Static550.anInt8271;
                if (spanFrom < 0) {
                    spanFrom = 0;
                } else if (spanFrom > Static35.anInt813 + Static35.anInt813) {
                    return;
                }
                spanTo = Static35.anInt813 + occluder.aShort25 - Static550.anInt8271;
                if (Static35.anInt813 + Static35.anInt813 < spanTo) {
                    spanTo = Static35.anInt813 + Static35.anInt813;
                } else if (spanTo < 0) {
                    return;
                }
                @Pc(856) int tileXFrom = occluder.aShort26 + Static35.anInt813 - Static403.anInt6246;
                if (tileXFrom < 0) {
                    tileXFrom = 0;
                } else if (Static35.anInt813 + Static35.anInt813 < tileXFrom) {
                    return;
                }
                @Pc(881) int tileXTo = Static35.anInt813 + occluder.aShort24 - Static403.anInt6246;
                if (Static35.anInt813 + Static35.anInt813 < tileXTo) {
                    tileXTo = Static35.anInt813 + Static35.anInt813;
                } else if (tileXTo < 0) {
                    return;
                }
                @Pc(900) boolean boxVisible = false;
                label283:
                for (@Pc(902) int tileX = tileXFrom; tileX <= tileXTo; tileX++) {
                    for (@Pc(908) int tileZ = spanFrom; tileZ <= spanTo; tileZ++) {
                        if (Static258.aBooleanArrayArray3[tileX][tileZ]) {
                            boxVisible = true;
                            break label283;
                        }
                    }
                }
                if (boxVisible && (Static219.method3190(0, occluder) && (Static219.method3190(1, occluder) && (Static219.method3190(2, occluder) && Static219.method3190(3, occluder))))) {
                    Static560.aLocOccluderArray3[Static469.activeOccluderCount++] = occluder;
                }
            }
        }
    }
}

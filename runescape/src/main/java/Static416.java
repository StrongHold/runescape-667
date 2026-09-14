import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static416 {

    @OriginalMember(owner = "client!nca", name = "t", descriptor = "Ljava/lang/String;")
    public static String mapOwner;

    @OriginalMember(owner = "client!nca", name = "n", descriptor = "J")
    public static long subscriptionExpiration;

    @OriginalMember(owner = "client!nca", name = "o", descriptor = "Z")
    public static boolean aBoolean472 = false;

    @OriginalMember(owner = "client!nca", name = "a", descriptor = "(Z)V")
    public static void rebuildActiveOccluders() {
        for (@Pc(5) int local5 = 0; local5 < Static444.anInt6751; local5++) {
            Static607.aLocOccluderArray4[local5] = null;
        }
        Static444.anInt6751 = 0;
        @Pc(25) int local25;
        @Pc(28) int local28;
        for (@Pc(22) int local22 = 0; local22 < Static299.tileMaxLevel; local22++) {
            for (local25 = 0; local25 < Static619.tileMaxX; local25++) {
                for (local28 = 0; local28 < Static662.tileMaxZ; local28++) {
                    @Pc(37) Tile local37 = Static334.activeTiles[local22][local28][local25];
                    if (local37 != null) {
                        if (local37.aShort83 > 0) {
                            local37.aShort83 = (short) (local37.aShort83 * -1);
                        }
                        if (local37.aShort84 > 0) {
                            local37.aShort84 = (short) (local37.aShort84 * -1);
                        }
                    }
                }
            }
        }
        for (local25 = 0; local25 < Static299.tileMaxLevel; local25++) {
            for (local28 = 0; local28 < Static619.tileMaxX; local28++) {
                for (@Pc(92) int local92 = 0; local92 < Static662.tileMaxZ; local92++) {
                    @Pc(101) Tile local101 = Static334.activeTiles[local25][local92][local28];
                    if (local101 != null) {
                        @Pc(129) boolean local129 = Static334.activeTiles[0][local92][local28] != null && Static334.activeTiles[0][local92][local28].tile != null;
                        @Pc(137) int local137;
                        @Pc(139) int local139;
                        @Pc(153) Tile local153;
                        @Pc(161) int local161;
                        @Pc(328) int local328;
                        @Pc(343) int local343;
                        @Pc(350) int local350;
                        @Pc(367) int local367;
                        @Pc(375) int local375;
                        @Pc(379) int local379;
                        @Pc(383) int local383;
                        @Pc(389) int local389;
                        @Pc(428) int local428;
                        @Pc(431) int local431;
                        if (local101.aShort84 < 0) {
                            local137 = local28;
                            local139 = local28;
                            local153 = Static334.activeTiles[local25][local92][local28 - 1];
                            local161 = Static706.floor[local25].getHeight(local92, local28);
                            while (local137 > 0 && local153 != null && local153.aShort84 < 0 && local101.aShort84 == local153.aShort84 && local101.aShort86 == local153.aShort86 && Static706.floor[local25].getHeight(local92, local137 - 1) == local161 && (local137 - 1 <= 0 || local161 == Static706.floor[local25].getHeight(local92, local137 - 2))) {
                                local137--;
                                local153 = Static334.activeTiles[local25][local92][local137 - 1];
                            }
                            for (local153 = Static334.activeTiles[local25][local92][local28 + 1]; Static662.tileMaxZ > local139 && local153 != null && local153.aShort84 < 0 && local101.aShort84 == local153.aShort84 && local101.aShort86 == local153.aShort86 && local161 == Static706.floor[local25].getHeight(local92, local139 + 1) && (Static662.tileMaxZ <= local139 + 1 || local161 == Static706.floor[local25].getHeight(local92, local139 + 2)); local153 = Static334.activeTiles[local25][local92][local139 + 1]) {
                                local139++;
                            }
                            local328 = local25 + 1 - local25;
                            local343 = Static706.floor[local129 ? local25 + 1 : local25].getHeight(local92, local137);
                            local350 = local343 + local101.aShort84 * local328;
                            local367 = Static706.floor[local129 ? local25 + 1 : local25].getHeight(local92, local139 + 1);
                            local375 = local367 + local101.aShort84 * local328;
                            local379 = local92 << EnvironmentLight.anInt1066;
                            local383 = local137 << EnvironmentLight.anInt1066;
                            local389 = (local139 << EnvironmentLight.anInt1066) + Static340.anInt5586;
                            Static607.aLocOccluderArray4[Static444.anInt6751++] = new LocOccluder(1, local25, local101.aShort86 + local379, local379 - -local101.aShort86, local101.aShort86 + local379, local101.aShort86 + local379, local343, local367, local375, local350, local383, local389, local389, local383);
                            for (local428 = local25; local428 <= local25; local428++) {
                                for (local431 = local137; local431 <= local139; local431++) {
                                    Static334.activeTiles[local428][local92][local431].aShort84 = (short) (Static334.activeTiles[local428][local92][local431].aShort84 * -1);
                                }
                            }
                        }
                        if (local101.aShort83 < 0) {
                            local137 = local92;
                            local139 = local92;
                            local153 = Static334.activeTiles[local25][local92 - 1][local28];
                            local161 = Static706.floor[local25].getHeight(local92, local28);
                            while (local137 > 0 && local153 != null && local153.aShort83 < 0 && local153.aShort83 == local101.aShort83 && local153.aShort85 == local101.aShort85 && local161 == Static706.floor[local25].getHeight(local137 - 1, local28) && (local137 - 1 <= 0 || local161 == Static706.floor[local25].getHeight(local137 - 2, local28))) {
                                local137--;
                                local153 = Static334.activeTiles[local25][local137 - 1][local28];
                            }
                            for (local153 = Static334.activeTiles[local25][local92 + 1][local28]; local139 < Static619.tileMaxX && local153 != null && local153.aShort83 < 0 && local101.aShort83 == local153.aShort83 && local101.aShort85 == local153.aShort85 && Static706.floor[local25].getHeight(local139 + 1, local28) == local161 && (local139 + 1 >= Static619.tileMaxX || local161 == Static706.floor[local25].getHeight(local139 + 2, local28)); local153 = Static334.activeTiles[local25][local139 + 1][local28]) {
                                local139++;
                            }
                            local328 = local25 + 1 - local25;
                            local343 = Static706.floor[local129 ? local25 + 1 : local25].getHeight(local137, local28);
                            local350 = local328 * local101.aShort83 + local343;
                            local367 = Static706.floor[local129 ? local25 + 1 : local25].getHeight(local139 + 1, local28);
                            local375 = local101.aShort83 * local328 + local367;
                            local379 = local137 << EnvironmentLight.anInt1066;
                            local383 = Static340.anInt5586 + (local139 << EnvironmentLight.anInt1066);
                            local389 = local28 << EnvironmentLight.anInt1066;
                            Static607.aLocOccluderArray4[Static444.anInt6751++] = new LocOccluder(2, local25, local379, local383, local383, local379, local343, local367, local375, local350, local389 + local101.aShort85, local101.aShort85 + local389, local101.aShort85 + local389, local101.aShort85 + local389);
                            for (local428 = local25; local428 <= local25; local428++) {
                                for (local431 = local137; local431 <= local139; local431++) {
                                    Static334.activeTiles[local428][local431][local28].aShort83 = (short) (Static334.activeTiles[local428][local431][local28].aShort83 * -1);
                                }
                            }
                        }
                    }
                }
            }
        }
        Static442.occludersDirty = true;
    }

}

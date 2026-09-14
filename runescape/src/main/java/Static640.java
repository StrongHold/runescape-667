import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static640 {

    @OriginalMember(owner = "client!uda", name = "H", descriptor = "S")
    public static short wideFov = 205;

    @OriginalMember(owner = "client!uda", name = "B", descriptor = "S")
    public static short fov = 256;

    @OriginalMember(owner = "client!uda", name = "a", descriptor = "(II)[[[B")
    public static byte[][][] createTileShapeMasks(@OriginalArg(1) int size) {
        @Pc(9) byte[][][] masks = new byte[8][4][];
        @Pc(18) byte[] mask = new byte[size * size];
        @Pc(20) int index = 0;
        @Pc(26) int local26;
        for (@Pc(22) int local22 = 0; local22 < size; local22++) {
            for (local26 = 0; local26 < size; local26++) {
                if (local26 <= local22) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][0] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(79) int local79;
        for (local26 = size - 1; local26 >= 0; local26--) {
            for (local79 = 0; local79 < size; local79++) {
                if (local79 <= local26) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][1] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(133) int local133;
        for (local79 = 0; local79 < size; local79++) {
            for (local133 = 0; local133 < size; local133++) {
                if (local133 >= local79) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][2] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(186) int local186;
        for (local133 = size - 1; local133 >= 0; local133--) {
            for (local186 = 0; local186 < size; local186++) {
                if (local186 >= local133) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][3] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(242) int local242;
        for (local186 = size - 1; local186 >= 0; local186--) {
            for (local242 = 0; local242 < size; local242++) {
                if (local242 <= local186 >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[1][0] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(298) int local298;
        for (local242 = 0; local242 < size; local242++) {
            for (local298 = 0; local298 < size; local298++) {
                if (index >= 0 && mask.length > index) {
                    if (local242 << 1 <= local298) {
                        mask[index] = -1;
                    }
                    index++;
                } else {
                    index++;
                }
            }
        }
        masks[1][1] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(372) int local372;
        for (local298 = 0; local298 < size; local298++) {
            for (local372 = size - 1; local372 >= 0; local372--) {
                if (local372 <= local298 >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[1][2] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(428) int local428;
        for (local372 = size - 1; local372 >= 0; local372--) {
            for (local428 = size - 1; local428 >= 0; local428--) {
                if (local428 >= local372 << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[1][3] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(483) int local483;
        for (local428 = size - 1; local428 >= 0; local428--) {
            for (local483 = size - 1; local483 >= 0; local483--) {
                if (local428 >> 1 >= local483) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][0] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(538) int local538;
        for (local483 = size - 1; local483 >= 0; local483--) {
            for (local538 = 0; local538 < size; local538++) {
                if (local483 << 1 <= local538) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][1] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(590) int local590;
        for (local538 = 0; local538 < size; local538++) {
            for (local590 = 0; local590 < size; local590++) {
                if (local590 <= local538 >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][2] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(649) int local649;
        for (local590 = 0; local590 < size; local590++) {
            for (local649 = size - 1; local649 >= 0; local649--) {
                if (local590 << 1 <= local649) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][3] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(702) int local702;
        for (local649 = size - 1; local649 >= 0; local649--) {
            for (local702 = 0; local702 < size; local702++) {
                if (local649 >> 1 <= local702) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][0] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(758) int local758;
        for (local702 = 0; local702 < size; local702++) {
            for (local758 = 0; local758 < size; local758++) {
                if (local758 <= local702 << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][1] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(813) int local813;
        for (local758 = 0; local758 < size; local758++) {
            for (local813 = size - 1; local813 >= 0; local813--) {
                if (local758 >> 1 <= local813) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][2] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(869) int local869;
        for (local813 = size - 1; local813 >= 0; local813--) {
            for (local869 = size - 1; local869 >= 0; local869--) {
                if (local869 <= local813 << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][3] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(923) int local923;
        for (local869 = size - 1; local869 >= 0; local869--) {
            for (local923 = size - 1; local923 >= 0; local923--) {
                if (local869 >> 1 <= local923) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][0] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(978) int local978;
        for (local923 = size - 1; local923 >= 0; local923--) {
            for (local978 = 0; local978 < size; local978++) {
                if (local978 <= local923 << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][1] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1026) int local1026;
        for (local978 = 0; local978 < size; local978++) {
            for (local1026 = 0; local1026 < size; local1026++) {
                if (local1026 >= local978 >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][2] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(1083) int local1083;
        for (local1026 = 0; local1026 < size; local1026++) {
            for (local1083 = size - 1; local1083 >= 0; local1083--) {
                if (local1026 << 1 >= local1083) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][3] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(1147) int local1147;
        for (local1083 = 0; local1083 < size; local1083++) {
            for (local1147 = 0; local1147 < size; local1147++) {
                if (local1147 <= size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][0] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1200) int local1200;
        for (local1147 = 0; local1147 < size; local1147++) {
            for (local1200 = 0; local1200 < size; local1200++) {
                if (size / 2 >= local1147) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][1] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1261) int local1261;
        for (local1200 = 0; local1200 < size; local1200++) {
            for (local1261 = 0; local1261 < size; local1261++) {
                if (size / 2 <= local1261) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][2] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(1310) int local1310;
        for (local1261 = 0; local1261 < size; local1261++) {
            for (local1310 = 0; local1310 < size; local1310++) {
                if (size / 2 <= local1261) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][3] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1363) int local1363;
        for (local1310 = 0; local1310 < size; local1310++) {
            for (local1363 = 0; local1363 < size; local1363++) {
                if (local1310 - size / 2 >= local1363) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][0] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1429) int local1429;
        for (local1363 = size - 1; local1363 >= 0; local1363--) {
            for (local1429 = 0; local1429 < size; local1429++) {
                if (local1429 <= local1363 - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][1] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1491) int local1491;
        for (local1429 = size - 1; local1429 >= 0; local1429--) {
            for (local1491 = size - 1; local1491 >= 0; local1491--) {
                if (local1491 <= local1429 - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][2] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1553) int local1553;
        for (local1491 = 0; local1491 < size; local1491++) {
            for (local1553 = size - 1; local1553 >= 0; local1553--) {
                if (local1491 - size / 2 >= local1553) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][3] = mask;
        mask = new byte[size * size];
        index = 0;
        @Pc(1607) int local1607;
        for (local1553 = 0; local1553 < size; local1553++) {
            for (local1607 = 0; local1607 < size; local1607++) {
                if (local1553 - size / 2 <= local1607) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][0] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1668) int local1668;
        for (local1607 = size - 1; local1607 >= 0; local1607--) {
            for (local1668 = 0; local1668 < size; local1668++) {
                if (local1668 >= local1607 - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][1] = mask;
        index = 0;
        mask = new byte[size * size];
        @Pc(1726) int local1726;
        for (local1668 = size - 1; local1668 >= 0; local1668--) {
            for (local1726 = size - 1; local1726 >= 0; local1726--) {
                if (local1726 >= local1668 - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][2] = mask;
        index = 0;
        mask = new byte[size * size];
        for (local1726 = 0; local1726 < size; local1726++) {
            for (@Pc(1785) int local1785 = size - 1; local1785 >= 0; local1785--) {
                if (local1785 >= local1726 - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][3] = mask;
        return masks;
    }
}

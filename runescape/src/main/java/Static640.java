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
        for (@Pc(22) int row = 0; row < size; row++) {
            for (@Pc(26) int col = 0; col < size; col++) {
                if (col <= row) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][0] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(79) int col = 0; col < size; col++) {
                if (col <= row) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][1] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = 0; row < size; row++) {
            for (@Pc(133) int col = 0; col < size; col++) {
                if (col >= row) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][2] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(186) int col = 0; col < size; col++) {
                if (col >= row) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[0][3] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(242) int col = 0; col < size; col++) {
                if (col <= row >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[1][0] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(298) int col = 0; col < size; col++) {
                if (index >= 0 && mask.length > index) {
                    if (row << 1 <= col) {
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
        for (int row = 0; row < size; row++) {
            for (@Pc(372) int col = size - 1; col >= 0; col--) {
                if (col <= row >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[1][2] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(428) int col = size - 1; col >= 0; col--) {
                if (col >= row << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[1][3] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(483) int col = size - 1; col >= 0; col--) {
                if (row >> 1 >= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][0] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(538) int col = 0; col < size; col++) {
                if (row << 1 <= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][1] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = 0; row < size; row++) {
            for (@Pc(590) int col = 0; col < size; col++) {
                if (col <= row >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][2] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = 0; row < size; row++) {
            for (@Pc(649) int col = size - 1; col >= 0; col--) {
                if (row << 1 <= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[2][3] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(702) int col = 0; col < size; col++) {
                if (row >> 1 <= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][0] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(758) int col = 0; col < size; col++) {
                if (col <= row << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][1] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(813) int col = size - 1; col >= 0; col--) {
                if (row >> 1 <= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][2] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(869) int col = size - 1; col >= 0; col--) {
                if (col <= row << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[3][3] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(923) int col = size - 1; col >= 0; col--) {
                if (row >> 1 <= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][0] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(978) int col = 0; col < size; col++) {
                if (col <= row << 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][1] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(1026) int col = 0; col < size; col++) {
                if (col >= row >> 1) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][2] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = 0; row < size; row++) {
            for (@Pc(1083) int col = size - 1; col >= 0; col--) {
                if (row << 1 >= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[4][3] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = 0; row < size; row++) {
            for (@Pc(1147) int col = 0; col < size; col++) {
                if (col <= size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][0] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(1200) int col = 0; col < size; col++) {
                if (size / 2 >= row) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][1] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(1261) int col = 0; col < size; col++) {
                if (size / 2 <= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][2] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = 0; row < size; row++) {
            for (@Pc(1310) int col = 0; col < size; col++) {
                if (size / 2 <= row) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[5][3] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(1363) int col = 0; col < size; col++) {
                if (row - size / 2 >= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][0] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(1429) int col = 0; col < size; col++) {
                if (col <= row - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][1] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(1491) int col = size - 1; col >= 0; col--) {
                if (col <= row - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][2] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(1553) int col = size - 1; col >= 0; col--) {
                if (row - size / 2 >= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[6][3] = mask;
        mask = new byte[size * size];
        index = 0;
        for (int row = 0; row < size; row++) {
            for (@Pc(1607) int col = 0; col < size; col++) {
                if (row - size / 2 <= col) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][0] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(1668) int col = 0; col < size; col++) {
                if (col >= row - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][1] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = size - 1; row >= 0; row--) {
            for (@Pc(1726) int col = size - 1; col >= 0; col--) {
                if (col >= row - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][2] = mask;
        index = 0;
        mask = new byte[size * size];
        for (int row = 0; row < size; row++) {
            for (@Pc(1785) int col = size - 1; col >= 0; col--) {
                if (col >= row - size / 2) {
                    mask[index] = -1;
                }
                index++;
            }
        }
        masks[7][3] = mask;
        return masks;
    }
}

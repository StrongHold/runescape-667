import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static264 {

    @OriginalMember(owner = "client!ie", name = "b", descriptor = "[[I")
    public static final int[][] SPLIT_EDGE_FACE = new int[][]{{0, 1, 2, 3}, {1, 2, 3, 0}, {1, 2, -1, 0}, {2, 0, -1, 1}, {0, 1, -1, 2}, {1, 2, -1, 0}, {-1, 4, -1, 1}, {-1, 1, 3, -1}, {-1, 0, 2, -1}, {3, 5, 2, 0}, {0, 2, 5, 3}, {0, 2, 3, 5}, {0, 1, 2, 3}};

    @OriginalMember(owner = "client!ie", name = "a", descriptor = "(IIIIIIII)V")
    public static void fillAndOutlineRect(@OriginalArg(0) int fillColour, @OriginalArg(1) int x1, @OriginalArg(2) int lineColour, @OriginalArg(4) int y0, @OriginalArg(5) int y1, @OriginalArg(6) int x0, @OriginalArg(7) int lineWidth) {
        if (Static180.anInt2995 <= x0 && x1 <= Static111.anInt2219 && Static724.anInt10930 <= y0 && y1 <= Static273.anInt4395) {
            Static446.method6094(y1, x1, lineColour, fillColour, x0, lineWidth, y0);
        } else {
            Static547.method7252(y1, x0, lineColour, y0, x1, lineWidth, fillColour);
        }
    }

    /**
     * Scan converts a screen space triangle against the software occlusion depth buffer held in
     * {@link Static485#anIntArray886}. {@link Static254#anInt4115} selects the mode: 1 writes the
     * triangle's depth into the buffer and always answers true, 2 leaves the buffer alone and answers
     * whether every pixel of the triangle lies behind the depth already recorded there, so true means
     * the triangle is entirely hidden by the occluders drawn in mode 1.
     */
    @OriginalMember(owner = "client!ie", name = "a", descriptor = "(IIIBIIIIII)Z")
    public static boolean rasteriseTriangle(@OriginalArg(0) int xA, @OriginalArg(1) int yA, @OriginalArg(2) int zC, @OriginalArg(4) int zB, @OriginalArg(5) int yC, @OriginalArg(6) int xC, @OriginalArg(7) int yB, @OriginalArg(8) int zA, @OriginalArg(9) int xB) {
        if (yA > 2000 || yB > 2000 || yC > 2000 || xA > 2000 || xB > 2000 || xC > 2000) {
            return false;
        } else if (yA >= -2000 && yB >= -2000 && yC >= -2000 && xA >= -2000 && xB >= -2000 && xC >= -2000) {
            if (Static254.anInt4115 == 2) {
                int index = Static228.anInt3709 * yA + xA;
                if (index >= 0 && index < Static485.anIntArray886.length && Static485.anIntArray886[index] > (zA << 8) - 38400) {
                    return false;
                }
                index = Static228.anInt3709 * yB + xB;
                if (index >= 0 && Static485.anIntArray886.length > index && (zB << 8) - 38400 < Static485.anIntArray886[index]) {
                    return false;
                }
                index = yC * Static228.anInt3709 + xC;
                if (index >= 0 && Static485.anIntArray886.length > index && (zC << 8) - 38400 < Static485.anIntArray886[index]) {
                    return false;
                }
            }
            @Pc(88) int dxAB = xB - xA;
            @Pc(191) int dyAB = yB - yA;
            @Pc(196) int dxAC = xC - xA;
            @Pc(201) int dyAC = yC - yA;
            @Pc(206) int dzAB = zB - zA;
            if (yB > yA && yC > yC) {
                if (yB <= yC) {
                    yC++;
                } else {
                    yB++;
                }
                yA--;
            } else if (yC <= yB) {
                if (yB < yA) {
                    yA++;
                } else {
                    yB++;
                }
                yC--;
            } else {
                if (yA <= yC) {
                    yC++;
                } else {
                    yA++;
                }
                yB--;
            }
            @Pc(272) int dzAC = zC - zA;
            @Pc(274) int gradientAB = 0;
            if (yA != yB) {
                gradientAB = (xB - xA << 12) / (yB - yA);
            }
            @Pc(291) int gradientBC = 0;
            if (yC != yB) {
                gradientBC = (xC - xB << 12) / (yC - yB);
            }
            @Pc(312) int gradientCA = 0;
            if (yC != yA) {
                gradientCA = (xA - xC << 12) / (yA - yC);
            }
            @Pc(352) int area = dyAC * dxAB - dyAB * dxAC;
            if (area == 0) {
                return false;
            }
            @Pc(369) int dzdx = (dyAC * dzAB - dzAC * dyAB << 8) / area;
            @Pc(381) int dzdy = (dzAC * dxAB - dzAB * dxAC << 8) / area;
            if (yA <= yB && yC >= yA) {
                if (Static624.anInt9461 <= yA) {
                    return true;
                }
                if (yC > Static624.anInt9461) {
                    yC = Static624.anInt9461;
                }
                zA = dzdx + (zA << 8) - dzdx * xA;
                if (Static624.anInt9461 < yB) {
                    yB = Static624.anInt9461;
                }
                if (yC > yB) {
                    xC = xA <<= 0xC;
                    xB <<= 0xC;
                    if (yA < 0) {
                        zA -= yA * dzdy;
                        xA -= gradientAB * yA;
                        xC -= yA * gradientCA;
                        yA = 0;
                    }
                    if (yB < 0) {
                        xB -= yB * gradientBC;
                        yB = 0;
                    }
                    if ((yA == yB || gradientAB <= gradientCA) && (yA != yB || gradientCA <= gradientBC)) {
                        yC -= yB;
                        yB -= yA;
                        yA *= Static228.anInt3709;
                        while (true) {
                            yB--;
                            if (yB < 0) {
                                while (true) {
                                    yC--;
                                    if (yC < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xB >> 12) - 1, (xC >> 12) + 1, zA, Static485.anIntArray886, dzdx, yA)) {
                                        return false;
                                    }
                                    xC += gradientCA;
                                    zA += dzdy;
                                    yA += Static228.anInt3709;
                                    xB += gradientBC;
                                }
                            }
                            if (!Static34.method885((xA >> 12) - 1, (xC >> 12) + 1, zA, Static485.anIntArray886, dzdx, yA)) {
                                return false;
                            }
                            zA += dzdy;
                            yA += Static228.anInt3709;
                            xA += gradientAB;
                            xC += gradientCA;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = Static228.anInt3709 * yA;
                        while (true) {
                            yB--;
                            if (yB < 0) {
                                while (true) {
                                    yC--;
                                    if (yC < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xC >> 12) - 1, (xB >> 12) + 1, zA, Static485.anIntArray886, dzdx, yA)) {
                                        return false;
                                    }
                                    xC += gradientCA;
                                    xB += gradientBC;
                                    zA += dzdy;
                                    yA += Static228.anInt3709;
                                }
                            }
                            if (!Static34.method885((xC >> 12) - 1, (xA >> 12) + 1, zA, Static485.anIntArray886, dzdx, yA)) {
                                return false;
                            }
                            xA += gradientAB;
                            zA += dzdy;
                            yA += Static228.anInt3709;
                            xC += gradientCA;
                        }
                    }
                } else {
                    xB = xA <<= 0xC;
                    xC <<= 0xC;
                    if (yA < 0) {
                        zA -= yA * dzdy;
                        xA -= yA * gradientAB;
                        xB -= gradientCA * yA;
                        yA = 0;
                    }
                    if (yC < 0) {
                        xC -= yC * gradientBC;
                        yC = 0;
                    }
                    if (yA != yC && gradientCA < gradientAB || yA == yC && gradientBC > gradientAB) {
                        yB -= yC;
                        yC -= yA;
                        yA = Static228.anInt3709 * yA;
                        while (true) {
                            yC--;
                            if (yC < 0) {
                                while (true) {
                                    yB--;
                                    if (yB < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xC >> 12) - 1, (xA >> 12) + 1, zA, Static485.anIntArray886, dzdx, yA)) {
                                        return false;
                                    }
                                    xC += gradientBC;
                                    xA += gradientAB;
                                    zA += dzdy;
                                    yA += Static228.anInt3709;
                                }
                            }
                            if (!Static34.method885((xB >> 12) - 1, (xA >> 12) + 1, zA, Static485.anIntArray886, dzdx, yA)) {
                                return false;
                            }
                            xA += gradientAB;
                            zA += dzdy;
                            xB += gradientCA;
                            yA += Static228.anInt3709;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = Static228.anInt3709 * yA;
                        while (true) {
                            yC--;
                            if (yC < 0) {
                                while (true) {
                                    yB--;
                                    if (yB < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xA >> 12) - 1, (xC >> 12) + 1, zA, Static485.anIntArray886, dzdx, yA)) {
                                        return false;
                                    }
                                    zA += dzdy;
                                    xA += gradientAB;
                                    yA += Static228.anInt3709;
                                    xC += gradientBC;
                                }
                            }
                            if (!Static34.method885((xA >> 12) - 1, (xB >> 12) - -1, zA, Static485.anIntArray886, dzdx, yA)) {
                                return false;
                            }
                            xA += gradientAB;
                            yA += Static228.anInt3709;
                            xB += gradientCA;
                            zA += dzdy;
                        }
                    }
                }
            } else if (yC < yB) {
                if (Static624.anInt9461 <= yC) {
                    return true;
                }
                if (Static624.anInt9461 < yA) {
                    yA = Static624.anInt9461;
                }
                if (yB > Static624.anInt9461) {
                    yB = Static624.anInt9461;
                }
                zC = dzdx + (zC << 8) - xC * dzdx;
                if (yB > yA) {
                    xB = xC <<= 0xC;
                    if (yC < 0) {
                        xC -= gradientCA * yC;
                        xB -= yC * gradientBC;
                        zC -= yC * dzdy;
                        yC = 0;
                    }
                    xA <<= 0xC;
                    if (yA < 0) {
                        xA -= yA * gradientAB;
                        yA = 0;
                    }
                    if (gradientCA <= gradientBC) {
                        yB -= yA;
                        yA -= yC;
                        yC *= Static228.anInt3709;
                        while (true) {
                            yA--;
                            if (yA < 0) {
                                while (true) {
                                    yB--;
                                    if (yB < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xA >> 12) - 1, (xB >> 12) + 1, zC, Static485.anIntArray886, dzdx, yC)) {
                                        return false;
                                    }
                                    zC += dzdy;
                                    xB += gradientBC;
                                    yC += Static228.anInt3709;
                                    xA += gradientAB;
                                }
                            }
                            if (!Static34.method885((xC >> 12) - 1, (xB >> 12) + 1, zC, Static485.anIntArray886, dzdx, yC)) {
                                return false;
                            }
                            yC += Static228.anInt3709;
                            xC += gradientCA;
                            zC += dzdy;
                            xB += gradientBC;
                        }
                    } else {
                        yB -= yA;
                        yA -= yC;
                        yC *= Static228.anInt3709;
                        while (true) {
                            yA--;
                            if (yA < 0) {
                                while (true) {
                                    yB--;
                                    if (yB < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xB >> 12) - 1, (xA >> 12) + 1, zC, Static485.anIntArray886, dzdx, yC)) {
                                        return false;
                                    }
                                    zC += dzdy;
                                    xB += gradientBC;
                                    xA += gradientAB;
                                    yC += Static228.anInt3709;
                                }
                            }
                            if (!Static34.method885((xB >> 12) - 1, (xC >> 12) + 1, zC, Static485.anIntArray886, dzdx, yC)) {
                                return false;
                            }
                            xC += gradientCA;
                            yC += Static228.anInt3709;
                            zC += dzdy;
                            xB += gradientBC;
                        }
                    }
                } else {
                    xA = xC <<= 0xC;
                    if (yC < 0) {
                        xA -= gradientBC * yC;
                        zC -= dzdy * yC;
                        xC -= gradientCA * yC;
                        yC = 0;
                    }
                    xB <<= 0xC;
                    if (yB < 0) {
                        xB -= gradientAB * yB;
                        yB = 0;
                    }
                    if (gradientCA <= gradientBC) {
                        yA -= yB;
                        yB -= yC;
                        yC = Static228.anInt3709 * yC;
                        while (true) {
                            yB--;
                            if (yB < 0) {
                                while (true) {
                                    yA--;
                                    if (yA < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xC >> 12) - 1, (xB >> 12) - -1, zC, Static485.anIntArray886, dzdx, yC)) {
                                        return false;
                                    }
                                    xB += gradientAB;
                                    yC += Static228.anInt3709;
                                    xC += gradientCA;
                                    zC += dzdy;
                                }
                            }
                            if (!Static34.method885((xC >> 12) - 1, (xA >> 12) + 1, zC, Static485.anIntArray886, dzdx, yC)) {
                                return false;
                            }
                            xA += gradientBC;
                            yC += Static228.anInt3709;
                            zC += dzdy;
                            xC += gradientCA;
                        }
                    } else {
                        yA -= yB;
                        yB -= yC;
                        yC *= Static228.anInt3709;
                        while (true) {
                            yB--;
                            if (yB < 0) {
                                while (true) {
                                    yA--;
                                    if (yA < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xB >> 12) - 1, (xC >> 12) + 1, zC, Static485.anIntArray886, dzdx, yC)) {
                                        return false;
                                    }
                                    zC += dzdy;
                                    yC += Static228.anInt3709;
                                    xC += gradientCA;
                                    xB += gradientAB;
                                }
                            }
                            if (!Static34.method885((xA >> 12) - 1, (xC >> 12) + 1, zC, Static485.anIntArray886, dzdx, yC)) {
                                return false;
                            }
                            zC += dzdy;
                            xA += gradientBC;
                            xC += gradientCA;
                            yC += Static228.anInt3709;
                        }
                    }
                }
            } else if (Static624.anInt9461 <= yB) {
                return true;
            } else {
                if (Static624.anInt9461 < yC) {
                    yC = Static624.anInt9461;
                }
                if (yA > Static624.anInt9461) {
                    yA = Static624.anInt9461;
                }
                zB = (zB << 8) - (xB * dzdx - dzdx);
                if (yC >= yA) {
                    xC = xB <<= 0xC;
                    if (yB < 0) {
                        xB -= yB * gradientBC;
                        zB -= dzdy * yB;
                        xC -= gradientAB * yB;
                        yB = 0;
                    }
                    xA <<= 0xC;
                    if (yA < 0) {
                        xA -= yA * gradientCA;
                        yA = 0;
                    }
                    if (gradientBC > gradientAB) {
                        yC -= yA;
                        yA -= yB;
                        yB = Static228.anInt3709 * yB;
                        while (true) {
                            yA--;
                            if (yA < 0) {
                                while (true) {
                                    yC--;
                                    if (yC < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xA >> 12) - 1, (xB >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                        return false;
                                    }
                                    xB += gradientBC;
                                    xA += gradientCA;
                                    zB += dzdy;
                                    yB += Static228.anInt3709;
                                }
                            }
                            if (!Static34.method885((xC >> 12) - 1, (xB >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                return false;
                            }
                            xB += gradientBC;
                            yB += Static228.anInt3709;
                            zB += dzdy;
                            xC += gradientAB;
                        }
                    } else {
                        yC -= yA;
                        yA -= yB;
                        yB *= Static228.anInt3709;
                        while (true) {
                            yA--;
                            if (yA < 0) {
                                while (true) {
                                    yC--;
                                    if (yC < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xB >> 12) - 1, (xA >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                        return false;
                                    }
                                    xB += gradientBC;
                                    xA += gradientCA;
                                    yB += Static228.anInt3709;
                                    zB += dzdy;
                                }
                            }
                            if (!Static34.method885((xB >> 12) - 1, (xC >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                return false;
                            }
                            yB += Static228.anInt3709;
                            xC += gradientAB;
                            zB += dzdy;
                            xB += gradientBC;
                        }
                    }
                } else {
                    xA = xB <<= 0xC;
                    if (yB < 0) {
                        zB -= dzdy * yB;
                        xB -= yB * gradientBC;
                        xA -= yB * gradientAB;
                        yB = 0;
                    }
                    xC <<= 0xC;
                    if (yC < 0) {
                        xC -= gradientCA * yC;
                        yC = 0;
                    }
                    if (yB != yC && gradientBC > gradientAB || yC == yB && gradientCA < gradientAB) {
                        yA -= yC;
                        yC -= yB;
                        yB = Static228.anInt3709 * yB;
                        while (true) {
                            yC--;
                            if (yC < 0) {
                                while (true) {
                                    yA--;
                                    if (yA < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xA >> 12) - 1, (xC >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                        return false;
                                    }
                                    xA += gradientAB;
                                    zB += dzdy;
                                    yB += Static228.anInt3709;
                                    xC += gradientCA;
                                }
                            }
                            if (!Static34.method885((xA >> 12) - 1, (xB >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                return false;
                            }
                            xA += gradientAB;
                            zB += dzdy;
                            yB += Static228.anInt3709;
                            xB += gradientBC;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB *= Static228.anInt3709;
                        while (true) {
                            yC--;
                            if (yC < 0) {
                                while (true) {
                                    yA--;
                                    if (yA < 0) {
                                        return true;
                                    }
                                    if (!Static34.method885((xC >> 12) - 1, (xA >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                        return false;
                                    }
                                    zB += dzdy;
                                    yB += Static228.anInt3709;
                                    xA += gradientAB;
                                    xC += gradientCA;
                                }
                            }
                            if (!Static34.method885((xB >> 12) - 1, (xA >> 12) + 1, zB, Static485.anIntArray886, dzdx, yB)) {
                                return false;
                            }
                            zB += dzdy;
                            xA += gradientAB;
                            yB += Static228.anInt3709;
                            xB += gradientBC;
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }
}

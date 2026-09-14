import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static51 {

    /**
     * Fills a circle of {@code radius} with {@code fillColour} and rings it with a band of
     * {@code lineWidth} pixels of {@code lineColour}, clipping every span against the clip bounds.
     */
    @OriginalMember(owner = "client!bma", name = "a", descriptor = "(IIIIIII)V")
    public static void fillAndOutlineCircleClipped(@OriginalArg(0) int lineWidth, @OriginalArg(1) int fillColour, @OriginalArg(2) int lineColour, @OriginalArg(3) int centreX, @OriginalArg(5) int radius, @OriginalArg(6) int centreY) {
        Static315.method4574(radius);
        @Pc(10) int minorOffset = 0;
        @Pc(15) int innerRadius = radius - lineWidth;
        if (innerRadius < 0) {
            innerRadius = 0;
        }
        @Pc(24) int outerOffset = radius;
        @Pc(27) int outerError = -radius;
        @Pc(29) int innerOffset = innerRadius;
        @Pc(32) int innerError = -innerRadius;
        @Pc(34) int outerStep = -1;
        @Pc(59) int local59;
        @Pc(69) int local69;
        @Pc(78) int local78;
        @Pc(86) int local86;
        if (centreY >= Static724.anInt10930 && Static273.anInt4395 >= centreY) {
            @Pc(50) int[] centreRow = Static723.anIntArrayArray266[centreY];
            local59 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX - radius);
            local69 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, radius + centreX);
            local78 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX - innerRadius);
            local86 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX + innerRadius);
            Static696.fillHorizontalSpan(local78, lineColour, local59, centreRow);
            Static696.fillHorizontalSpan(local86, fillColour, local78, centreRow);
            Static696.fillHorizontalSpan(local69, lineColour, local86, centreRow);
        }
        @Pc(112) int innerStep = -1;
        while (outerOffset > minorOffset) {
            outerStep += 2;
            innerStep += 2;
            innerError += innerStep;
            outerError += outerStep;
            if (innerError >= 0 && innerOffset >= 1) {
                innerOffset--;
                Static430.anIntArray519[innerOffset] = minorOffset;
                innerError -= innerOffset << 1;
            }
            minorOffset++;
            @Pc(213) int innerRight;
            @Pc(222) int innerLeft;
            @Pc(233) int[] row;
            @Pc(161) int topRow;
            if (outerError >= 0) {
                outerOffset--;
                outerError -= outerOffset << 1;
                topRow = centreY - outerOffset;
                local59 = outerOffset + centreY;
                if (Static724.anInt10930 <= local59 && Static273.anInt4395 >= topRow) {
                    if (innerRadius > outerOffset) {
                        local69 = Static430.anIntArray519[outerOffset];
                        local78 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX + minorOffset);
                        local86 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX - minorOffset);
                        innerRight = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX + local69);
                        innerLeft = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX - local69);
                        if (local59 <= Static273.anInt4395) {
                            row = Static723.anIntArrayArray266[local59];
                            Static696.fillHorizontalSpan(innerLeft, lineColour, local86, row);
                            Static696.fillHorizontalSpan(innerRight, fillColour, innerLeft, row);
                            Static696.fillHorizontalSpan(local78, lineColour, innerRight, row);
                        }
                        if (topRow >= Static724.anInt10930) {
                            row = Static723.anIntArrayArray266[topRow];
                            Static696.fillHorizontalSpan(innerLeft, lineColour, local86, row);
                            Static696.fillHorizontalSpan(innerRight, fillColour, innerLeft, row);
                            Static696.fillHorizontalSpan(local78, lineColour, innerRight, row);
                        }
                    } else {
                        local69 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX + minorOffset);
                        local78 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX - minorOffset);
                        if (local59 <= Static273.anInt4395) {
                            Static696.fillHorizontalSpan(local69, lineColour, local78, Static723.anIntArrayArray266[local59]);
                        }
                        if (topRow >= Static724.anInt10930) {
                            Static696.fillHorizontalSpan(local69, lineColour, local78, Static723.anIntArrayArray266[topRow]);
                        }
                    }
                }
            }
            topRow = centreY - minorOffset;
            local59 = minorOffset + centreY;
            if (local59 >= Static724.anInt10930 && topRow <= Static273.anInt4395) {
                local69 = centreX + outerOffset;
                local78 = centreX - outerOffset;
                if (Static180.anInt2995 <= local69 && Static111.anInt2219 >= local78) {
                    local69 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, local69);
                    local78 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, local78);
                    if (innerRadius <= minorOffset) {
                        if (Static273.anInt4395 >= local59) {
                            Static696.fillHorizontalSpan(local69, lineColour, local78, Static723.anIntArrayArray266[local59]);
                        }
                        if (topRow >= Static724.anInt10930) {
                            Static696.fillHorizontalSpan(local69, lineColour, local78, Static723.anIntArrayArray266[topRow]);
                        }
                    } else {
                        local86 = minorOffset <= innerOffset ? innerOffset : Static430.anIntArray519[minorOffset];
                        innerRight = Static670.method8732(Static180.anInt2995, Static111.anInt2219, local86 + centreX);
                        innerLeft = Static670.method8732(Static180.anInt2995, Static111.anInt2219, centreX - local86);
                        if (Static273.anInt4395 >= local59) {
                            row = Static723.anIntArrayArray266[local59];
                            Static696.fillHorizontalSpan(innerLeft, lineColour, local78, row);
                            Static696.fillHorizontalSpan(innerRight, fillColour, innerLeft, row);
                            Static696.fillHorizontalSpan(local69, lineColour, innerRight, row);
                        }
                        if (topRow >= Static724.anInt10930) {
                            row = Static723.anIntArrayArray266[topRow];
                            Static696.fillHorizontalSpan(innerLeft, lineColour, local78, row);
                            Static696.fillHorizontalSpan(innerRight, fillColour, innerLeft, row);
                            Static696.fillHorizontalSpan(local69, lineColour, innerRight, row);
                        }
                    }
                }
            }
        }
    }
}

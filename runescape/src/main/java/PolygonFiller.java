import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Scanline fill for arbitrary polygons.
 * <p>
 * Edges are held in {@link #edges} as a flat run of four ints each. Before an edge becomes active
 * those four ints are the two endpoints (x1, y1, x2, y2) with y1 below y2; once the scanline reaches
 * y1 the pair is converted in place to a 16.16 fixed point x and a per-scanline x step, so walking
 * the edge costs one add.
 */
public final class PolygonFiller {

    @OriginalMember(owner = "client!eh", name = "c", descriptor = "I")
    public static int spanCursor;

    @OriginalMember(owner = "client!eh", name = "e", descriptor = "I")
    public static int activeEnd;

    @OriginalMember(owner = "client!eh", name = "g", descriptor = "I")
    public static int edgeEnd;

    @OriginalMember(owner = "client!eh", name = "h", descriptor = "I")
    public static int spanStartX;

    @OriginalMember(owner = "client!eh", name = "d", descriptor = "[I")
    public static int[] edges;

    @OriginalMember(owner = "client!eh", name = "b", descriptor = "I")
    public static int activeStart;

    @OriginalMember(owner = "client!eh", name = "a", descriptor = "I")
    public static int spanEndX;

    @OriginalMember(owner = "client!eh", name = "f", descriptor = "I")
    public static int scanlineY;

    @OriginalMember(owner = "client!eh", name = "b", descriptor = "()V")
    public static void clearEdges() {
        edgeEnd = 0;
    }

    @OriginalMember(owner = "client!eh", name = "b", descriptor = "(I)Z")
    public static boolean nextSpan(@OriginalArg(0) int maxY) {
        @Pc(1) int end = activeEnd;
        @Pc(3) int cursor = spanCursor;
        @Pc(5) int y = scanlineY;
        while (cursor >= end) {
            y++;
            scanlineY = y;
            if (y >= maxY) {
                return false;
            }
            @Pc(17) int start = activeStart;
            @Pc(24) int local24;
            @Pc(31) int local31;
            while (end < edgeEnd) {
                local24 = edges[end + 1];
                if (y < local24) {
                    break;
                }
                local31 = edges[end];
                @Pc(37) int x2 = edges[end + 2];
                @Pc(43) int y2 = edges[end + 3];
                @Pc(53) int step = (x2 - local31 << 16) / (y2 - local24);
                @Pc(59) int x = (local31 << 16) + 32768;
                edges[end] = x;
                edges[end + 2] = step;
                end += 4;
            }
            for (local24 = start; local24 < end; local24 += 4) {
                local31 = edges[local24 + 3];
                if (y >= local31) {
                    edges[local24] = edges[start];
                    edges[local24 + 1] = edges[start + 1];
                    edges[local24 + 2] = edges[start + 2];
                    edges[local24 + 3] = edges[start + 3];
                    start += 4;
                }
            }
            if (start == edgeEnd) {
                edgeEnd = 0;
                return false;
            }
            sortActiveEdgesByX(start, end);
            activeStart = start;
            activeEnd = end;
            cursor = start;
        }
        spanStartX = edges[cursor] >> 16;
        spanEndX = edges[cursor + 4] >> 16;
        edges[cursor] += edges[cursor + 2];
        edges[cursor + 4] += edges[cursor + 6];
        cursor += 8;
        spanCursor = cursor;
        return true;
    }

    @OriginalMember(owner = "client!eh", name = "b", descriptor = "(II)V")
    public static void sortEdgesByY(@OriginalArg(0) int from, @OriginalArg(1) int to) {
        if (to <= from + 4) {
            return;
        }
        @Pc(8) int store = from;
        @Pc(12) int pivotX1 = edges[from];
        @Pc(18) int pivotY1 = edges[from + 1];
        @Pc(24) int pivotX2 = edges[from + 2];
        @Pc(30) int pivotY2 = edges[from + 3];
        for (@Pc(34) int edge = from + 4; edge < to; edge += 4) {
            @Pc(41) int startY = edges[edge + 1];
            if (startY < pivotY1) {
                edges[store] = edges[edge];
                edges[store + 1] = startY;
                edges[store + 2] = edges[edge + 2];
                edges[store + 3] = edges[edge + 3];
                store += 4;
                edges[edge] = edges[store];
                edges[edge + 1] = edges[store + 1];
                edges[edge + 2] = edges[store + 2];
                edges[edge + 3] = edges[store + 3];
            }
        }
        edges[store] = pivotX1;
        edges[store + 1] = pivotY1;
        edges[store + 2] = pivotX2;
        edges[store + 3] = pivotY2;
        sortEdgesByY(from, store);
        sortEdgesByY(store + 4, to);
    }

    @OriginalMember(owner = "client!eh", name = "a", descriptor = "([III)V")
    public static void addEdges(@OriginalArg(0) int[] vertices, @OriginalArg(2) int count) {
        @Pc(5) int required = edgeEnd + (count << 1);
        @Pc(18) int index;
        if (edges == null || edges.length < required) {
            @Pc(16) int[] grown = new int[required];
            for (index = 0; index < edgeEnd; index++) {
                grown[index] = edges[index];
            }
            edges = grown;
        }
        @Pc(37) int length = count;
        @Pc(41) int previous = length - 2;
        for (index = 0; index < length; index += 2) {
            @Pc(50) int previousY = vertices[previous + 1];
            @Pc(56) int y = vertices[index + 1];
            if (previousY < y) {
                edges[edgeEnd++] = vertices[previous];
                edges[edgeEnd++] = previousY;
                edges[edgeEnd++] = vertices[index];
                edges[edgeEnd++] = y;
            } else if (y < previousY) {
                edges[edgeEnd++] = vertices[index];
                edges[edgeEnd++] = y;
                edges[edgeEnd++] = vertices[previous];
                edges[edgeEnd++] = previousY;
            }
            previous = index;
        }
    }

    @OriginalMember(owner = "client!eh", name = "a", descriptor = "(II)V")
    public static void sortActiveEdgesByX(@OriginalArg(0) int from, @OriginalArg(1) int to) {
        while (true) {
            if (to >= from + 8) {
                @Pc(2) boolean sorted = true;
                for (@Pc(6) int edge = from + 4; edge < to; edge += 4) {
                    @Pc(13) int left = edges[edge - 4];
                    @Pc(17) int right = edges[edge];
                    if (left > right) {
                        sorted = false;
                        edges[edge - 4] = right;
                        edges[edge] = left;
                        left = edges[edge - 2];
                        edges[edge - 2] = edges[edge + 2];
                        edges[edge + 2] = left;
                        left = edges[edge - 1];
                        edges[edge - 1] = edges[edge + 3];
                        edges[edge + 3] = left;
                    }
                }
                if (!sorted) {
                    to -= 4;
                    continue;
                }
            }
            return;
        }
    }

    @OriginalMember(owner = "client!eh", name = "a", descriptor = "(Lclient!ha;[IIII[I[I)V")
    public static void fillPolygon(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int[] vertices, @OriginalArg(3) int count, @OriginalArg(4) int colour, @OriginalArg(5) int[] lineOffsets, @OriginalArg(6) int[] lineWidths) {
        @Pc(2) int[] clip = new int[4];
        toolkit.K(clip);
        if (lineOffsets != null && clip[3] - clip[1] != lineOffsets.length) {
            throw new IllegalStateException();
        }

        clearEdges();
        addEdges(vertices, count);
        beginScan(clip[1]);

        while (true) {
            @Pc(36) int startX;
            @Pc(38) int endX;
            @Pc(40) int y;
            do {
                if (!nextSpan(clip[3])) {
                    return;
                }

                startX = spanStartX;
                endX = spanEndX;
                y = scanlineY;

                if (lineOffsets == null) {
                    break;
                }

                @Pc(48) int line = y - clip[1];
                if (startX < lineOffsets[line] + clip[0]) {
                    startX = lineOffsets[line] + clip[0];
                }

                if (endX > lineOffsets[line] + lineWidths[line] + clip[0]) {
                    endX = lineOffsets[line] + lineWidths[line] + clip[0];
                }
            } while (endX - startX <= 0);

            toolkit.U(startX, y, endX - startX, colour, 1);
        }
    }

    @OriginalMember(owner = "client!eh", name = "a", descriptor = "(Lclient!ha;[II[I[I)V")
    public static void fillPolygon(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int[] vertices, @OriginalArg(2) int colour, @OriginalArg(3) int[] lineOffsets, @OriginalArg(4) int[] lineWidths) {
        fillPolygon(toolkit, vertices, vertices.length, colour, lineOffsets, lineWidths);
    }

    @OriginalMember(owner = "client!eh", name = "a", descriptor = "(I)V")
    public static void beginScan(@OriginalArg(0) int minY) {
        if (edgeEnd < 0) {
            spanCursor = 0;
            activeEnd = 0;
            activeStart = 0;
            scanlineY = 2147483646;
            return;
        }
        sortEdgesByY(0, edgeEnd);
        @Pc(18) int y = edges[1];
        if (y < minY) {
            y = minY;
        }
        @Pc(27) int edge;
        for (edge = 0; edge < edgeEnd; edge += 4) {
            @Pc(34) int startY = edges[edge + 1];
            if (y < startY) {
                break;
            }
            @Pc(41) int x1 = edges[edge];
            @Pc(47) int x2 = edges[edge + 2];
            @Pc(53) int y2 = edges[edge + 3];
            @Pc(63) int step = (x2 - x1 << 16) / (y2 - startY);
            @Pc(69) int x = (x1 << 16) + 32768;
            edges[edge] = x + (y - startY) * step;
            edges[edge + 2] = step;
        }
        activeStart = 0;
        activeEnd = edge;
        spanCursor = edge;
        scanlineY = y - 1;
    }
}

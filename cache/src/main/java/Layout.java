import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * How one item's bytes are cut into sections, and how far each section was read.
 *
 * The client reads a model or an animation with several packets at once, each one started at the
 * beginning of its own section and moved on only by what it reads. A section that is read short
 * leaves bytes nobody looks at, and one that is read long eats into the next, but the client sees
 * neither. Here every section is given its own cursor, so that where each one stops can be set
 * against where the next one begins.
 *
 * A footer is cut from the end of the data before anything else, because a format that keeps its
 * header at the end can only be read from there. The body sections then follow one another from
 * the first byte, and the last of them has to finish exactly where the footer starts.
 */
public final class Layout {

    /**
     * What went wrong with one item: a short name for the kind of fault, so that items that fail
     * the same way are counted together, and the positions that show it.
     */
    public record Mismatch(String kind, String detail) {
        /* empty */
    }

    /**
     * What walking one item found: the counts that shape it, for setting against what the
     * client's own reader makes of it, and the first fault, or null when every byte was read once.
     *
     * The counts are those reached before any fault, so an item that faults early has fewer.
     */
    public record Walk(Map<String, Integer> counts, Mismatch mismatch) {

        public Walk {
            counts = Collections.unmodifiableMap(new LinkedHashMap<>(counts));
        }

        public boolean whole() {
            return mismatch == null;
        }
    }

    /**
     * Thrown when a cursor reads past the end of the data, which the client would also do and
     * fail on.
     */
    public static final class Overrun extends RuntimeException {

        private final String section;

        private Overrun(String section, int at, int length) {
            super("the " + section + " section reads byte " + at + " of " + length);
            this.section = section;
        }

        public Mismatch mismatch() {
            return new Mismatch(section + " reads past the end of the data", getMessage());
        }
    }

    /**
     * One stretch of the data, and the cursor that walks it.
     */
    public final class Cursor {

        private final String name;
        private final int start;
        private final int end;
        private int pos;

        private Cursor(String name, int start, int end) {
            this.name = name;
            this.start = start;
            this.end = end;
            this.pos = start;
        }

        public int g1() {
            return byteAt(pos++) & 0xFF;
        }

        public int g1b() {
            return byteAt(pos++);
        }

        public int g2() {
            return g1() << 8 | g1();
        }

        public int g3() {
            return g1() << 16 | g2();
        }

        /**
         * A value held in one byte when it is small and two when it is not, which the top bit of
         * the first byte says.
         */
        public void gsmarts() {
            if ((byteAt(pos) & 0x80) == 0) {
                pos += 1;
            } else {
                pos += 2;
            }
            if (pos > data.length) {
                throw new Overrun(name, pos - 1, data.length);
            }
        }

        private byte byteAt(int at) {
            if (at < 0 || at >= data.length) {
                throw new Overrun(name, at, data.length);
            }
            return data[at];
        }
    }

    private final byte[] data;
    private final List<Cursor> body = new ArrayList<>();
    private final List<Cursor> footer = new ArrayList<>();

    /**
     * Where the next body section starts.
     */
    private int next = 0;

    /**
     * Where the footer starts, which is where the body has to end.
     */
    private int footerStart;

    public Layout(byte[] data) {
        this.data = data;
        this.footerStart = data.length;
    }

    /**
     * Cuts a section of a fixed size from the end of the data, in front of any cut before it.
     */
    public Cursor footer(String name, int size) {
        var cursor = new Cursor(name, footerStart - size, footerStart);
        footerStart -= size;
        footer.add(cursor);
        return cursor;
    }

    /**
     * Adds a body section of a size the item declares, straight after the last one.
     */
    public Cursor section(String name, int size) {
        var cursor = new Cursor(name, next, next + size);
        next += size;
        body.add(cursor);
        return cursor;
    }

    /**
     * Adds a body section that declares no size of its own and so runs up to the footer. What its
     * cursor reads is then the only thing that says whether it really ends there.
     */
    public Cursor rest(String name) {
        return section(name, footerStart - next);
    }

    /**
     * The first thing wrong with the item: a footer longer than the data, a section that is
     * declared to run past the footer, a cursor that did not stop at the end of its section, or
     * body sections that stop short of the footer. Answers nothing when every byte was read once.
     */
    public Optional<Mismatch> check() {
        if (footerStart < 0) {
            return Optional.of(new Mismatch("footer longer than the data",
                "the footer needs " + (data.length - footerStart) + " bytes of " + data.length));
        }

        for (var cursor : footer) {
            if (cursor.pos != cursor.end) {
                return Optional.of(endsElsewhere(cursor));
            }
        }

        for (var cursor : body) {
            if (cursor.end > footerStart) {
                return Optional.of(new Mismatch(cursor.name + " runs into the footer",
                    "declared from " + cursor.start + " to " + cursor.end + ", footer starts at " + footerStart));
            } else if (cursor.pos != cursor.end) {
                return Optional.of(endsElsewhere(cursor));
            }
        }

        if (next != footerStart) {
            return Optional.of(new Mismatch("bytes before the footer belong to no section",
                "sections end at " + next + ", footer starts at " + footerStart));
        }
        return Optional.empty();
    }

    private static Mismatch endsElsewhere(Cursor cursor) {
        var how = cursor.pos < cursor.end ? " stops short" : " reads on into the next section";
        return new Mismatch(cursor.name + how,
            "starts at " + cursor.start + ", expected end " + cursor.end + ", actual end " + cursor.pos);
    }
}

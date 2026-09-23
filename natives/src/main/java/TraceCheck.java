import com.beust.jcommander.Parameter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

/**
 * Lines what the shipped toolkit worked with at each pixel up against what ours worked with, and
 * says which value differs first where the two pictures differ.
 *
 * Comparing pictures says where two toolkits disagree but not why. A pixel is the end of a long
 * chain: how far away it is, where on its texture it reads, the light and the water it has reached,
 * and for a blended face how much of each texture shows. Both toolkits can write every one of
 * those down, the shipped one through the watcher and ours through its trace, and once each
 * shipped pixel is matched to ours the first value in the chain that differs is where to look.
 *
 * The shipped toolkit names a pixel by its address in the colour buffer and ours by where it is on
 * the picture. The two are tied together by the pixels whose distance and place on the texture
 * agree to the last bit, which are nearly all of them, and the offset most of those agree on is
 * where the buffer starts.
 */
public final class TraceCheck {

    /**
     * Where the body of a span keeps what it works with for its four pixels, read off its
     * disassembly. A lane's shares are not kept in lane order: the four pairs run 0, 2, 1, 3.
     */
    private static final int COLOUR_ADDRESS = 0x00;
    private static final int DRAWN = 0x20;
    private static final int DISTANCE = 0x50;
    private static final int LIGHT = 0x60;
    private static final int WATER = 0x90;
    private static final int PLACE = 0xC0;
    private static final int SHARES = 0xF0;
    private static final int[] SHARE_PAIR = {0, 2, 1, 3};

    /**
     * Where a span keeps the run it walks: the address of its first pixel, how many pixels it
     * covers, how far away the first is and how far a step moves that, and the light it starts
     * with and steps by. Read off the span that draws a face with no texture.
     */
    private static final int RUN_ADDRESS = 0x00;
    private static final int RUN_COUNT = 0x28;
    private static final int RUN_DEPTH = 0x2C;
    private static final int RUN_DEPTH_STEP = 0x30;
    private static final int RUN_LIGHT = 0x60;
    private static final int RUN_LIGHT_STEP = 0x80;

    private static final int LANES = 4;
    private static final int PIXEL_BYTES = 4;

    /**
     * The light and the water are kept for four channels, and the fourth is never written.
     */
    private static final int CHANNELS_SEEN = 3;

    private static final int EXAMPLES = 8;

    private static final class Args implements Helpable {

        @Parameter(
            names = "--scene",
            description = "The scene both traces were drawn with",
            required = true
        )
        private String scene;

        @Parameter(
            names = "--shipped",
            description = "The shipped toolkit's trace",
            required = true
        )
        private Path shipped;

        @Parameter(
            names = "--ours",
            description = "Our toolkit's trace",
            required = true
        )
        private Path ours;

        @Parameter(
            names = "--routines",
            description = "The list the watcher was given",
            required = true
        )
        private Path routines;

        @Parameter(
            names = "--shipped-frames",
            description = "The frames the shipped toolkit drew",
            required = true
        )
        private Path shippedFrames;

        @Parameter(
            names = "--own-frames",
            description = "The frames our toolkit drew",
            required = true
        )
        private Path ownFrames;

        @Parameter(
            names = "--at",
            splitter = Whole.class,
            description = "A pixel to print in full, as x,y; repeat for more"
        )
        private List<String> at = List.of();

        @Parameter(
            names = "--ignore",
            splitter = Whole.class,
            description = "A value to leave out of the comparison: distance, place, light, water "
                + "or shares; repeat for more"
        )
        private List<String> ignored = List.of();

        @Parameter(
            names = "--dump",
            description = "How many calls to print of each routine that is not the body of a span"
        )
        private int dumped;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /**
     * What a toolkit worked with at one pixel.
     */
    record Pixel(int x, int y, float distance, float across, float down, List<Integer> light,
                 List<Integer> water, List<Integer> shares) {

        Pixel {
            light = List.copyOf(light);
            water = List.copyOf(water);
            shares = List.copyOf(shares);
        }

        Pixel at(int placedX, int placedY) {
            return new Pixel(placedX, placedY, distance, across, down, light, water, shares);
        }

        Place place() {
            return new Place(Float.floatToIntBits(distance), Float.floatToIntBits(across),
                Float.floatToIntBits(down));
        }

        Position position() {
            return new Position(x, y);
        }

        @Override
        public String toString() {
            return "distance " + distance + ", place " + across + " " + down + ", light " + light
                + ", water " + water + ", shares " + shares;
        }
    }

    /**
     * A pixel's distance and place on its texture, to the bit, which is what ties a shipped pixel
     * to one of ours.
     */
    record Place(int distance, int across, int down) {
    }

    record Position(int x, int y) {

        static Position parse(String written) {
            var parts = written.split(",");
            var x = Integer.parseInt(parts[0].trim());
            var y = Integer.parseInt(parts[1].trim());
            return new Position(x, y);
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }

    /**
     * A pixel of the shipped toolkit's, known only by where it sits in the colour buffer.
     */
    record Lane(long address, Pixel values) {
    }

    /**
     * One run of a row as a toolkit set out to walk it.
     */
    record Run(int x, int y, int count, float depth, float depthStep, List<Integer> light,
               List<Integer> lightStep) {

        Run {
            light = List.copyOf(light);
            lightStep = List.copyOf(lightStep);
        }

        float depthAt(Position position) {
            return depth + (position.x() - x) * depthStep;
        }

        boolean covers(Position position) {
            return position.y() == y && position.x() >= x && position.x() < x + count;
        }

        /**
         * Names what differs between two runs, the first pixel and the count before anything the
         * run carries.
         */
        String differences(Run other) {
            var differing = new ArrayList<String>();
            if (x != other.x || count != other.count) {
                differing.add("extent");
            }
            if (Float.compare(depth, other.depth) != 0 || Float.compare(depthStep, other.depthStep) != 0) {
                differing.add("depth");
            }
            if (!light.equals(other.light)) {
                differing.add("light");
            }
            if (!lightStep.equals(other.lightStep)) {
                differing.add("light step");
            }
            return differing.isEmpty() ? "nothing traced" : String.join(", ", differing);
        }

        @Override
        public String toString() {
            return "from " + x + " for " + count + ", depth " + depth + " by " + depthStep
                + ", light " + light + " by " + lightStep;
        }
    }

    /**
     * One call the watcher wrote down, with the bytes of each argument.
     */
    record Call(int slot, ByteBuffer first, ByteBuffer second) {
    }

    /**
     * The values that can differ between the two toolkits, in the order a pixel is worked out.
     */
    enum Field {
        DISTANCE,
        PLACE,
        LIGHT,
        WATER,
        SHARES;

        boolean differs(Pixel ours, Pixel shipped) {
            return switch (this) {
                case DISTANCE -> Float.compare(ours.distance(), shipped.distance()) != 0;
                case PLACE -> Float.compare(ours.across(), shipped.across()) != 0
                    || Float.compare(ours.down(), shipped.down()) != 0;
                case LIGHT -> !ours.light().equals(shipped.light());
                case WATER -> !ours.water().equals(shipped.water());
                case SHARES -> !ours.shares().equals(shipped.shares());
            };
        }

        static Field named(String name) {
            return valueOf(name.trim().toUpperCase());
        }
    }

    public static void main(String[] arguments) throws IOException {
        var args = new Args();

        if (!CommandLine.parsed("compareTraces", args, arguments)) {
            return;
        }

        var ignored = args.ignored.stream().map(Field::named).collect(Collectors.toSet());
        var compared = Arrays.stream(Field.values())
            .filter(field -> !ignored.contains(field))
            .toList();

        var families = Files.readAllLines(args.routines).stream()
            .filter(line -> !line.isBlank())
            .map(line -> ShippedRoutine.Family.ofSymbol(line.split(" ")[0]))
            .toList();
        var calls = Trace.linesOf(args.shipped, args.scene, 0).stream()
            .filter(line -> line.startsWith("W "))
            .map(TraceCheck::call)
            .toList();
        var ours = ownPixels(Trace.linesOf(args.ours, args.scene, 0));
        var lanes = calls.stream()
            .filter(call -> families.get(call.slot()) == ShippedRoutine.Family.BODY)
            .flatMap(call -> lanesOf(call).stream())
            .toList();

        System.out.println(args.scene + ", drawn once by each toolkit");
        System.out.println("  our trace holds " + ours.size() + " textured pixels, and the shipped "
            + "trace " + lanes.size() + " drawn pixels from the body of a span");

        if (lanes.isEmpty() || ours.isEmpty()) {
            System.out.println("  there is nothing to line up");
        } else {
            var start = bufferStart(ours, lanes);
            var shipped = placed(lanes, start);
            var both = shipped.keySet().stream().filter(ours::containsKey).count();
            System.out.printf("  lined up with the colour buffer starting at 0x%x: %d pixels "
                + "traced on both sides%n", start, both);

            var different = differingPixels(args.shippedFrames, args.ownFrames);
            report("on the " + different.size() + " pixels the two frames differ at",
                different, ours, shipped, compared);
            report("on every pixel the shipped toolkit traced", shipped.keySet(), ours, shipped,
                compared);

            var ownRuns = ownRuns(Trace.linesOf(args.ours, args.scene, 0));
            var shippedRuns = calls.stream()
                .filter(call -> families.get(call.slot()) == ShippedRoutine.Family.SPAN)
                .map(call -> runOf(call, start))
                .toList();
            if (!shippedRuns.isEmpty()) {
                reportRuns(different, ownRuns, shippedRuns);
            }

            for (var written : args.at) {
                var position = Position.parse(written);
                System.out.println("  at " + position);
                System.out.println("    ours     " + ours.get(position));
                System.out.println("    shipped  " + shipped.get(position));
                System.out.println("    our run      " + drawnIn(ownRuns, position));
                System.out.println("    shipped run  " + drawnIn(shippedRuns, position));
            }
        }

        dump(calls, families, args.dumped);
    }

    /**
     * Counts which of the compared values differ, pixel by pixel, over the given pixels, and shows
     * a few of each kind.
     */
    private static void report(String over, Set<Position> positions, Map<Position, Pixel> ours,
                               Map<Position, Pixel> shipped, List<Field> compared) {
        var counted = new TreeMap<String, Integer>();
        var examples = new TreeMap<String, List<Position>>();

        for (var position : positions) {
            var named = difference(ours.get(position), shipped.get(position), compared);
            counted.merge(named, 1, Integer::sum);
            examples.computeIfAbsent(named, ignored -> new ArrayList<>());
            if (examples.get(named).size() < EXAMPLES) {
                examples.get(named).add(position);
            }
        }

        System.out.println("  " + over + ", what differs:");
        for (var entry : counted.entrySet()) {
            System.out.printf("    %-36s %7d   for example %s%n", entry.getKey(), entry.getValue(),
                examples.get(entry.getKey()));
        }
    }

    /**
     * Names what differs at one pixel, or which side has no trace of it.
     *
     * A pixel only one side traced was drawn by something the trace does not cover there: a face
     * that wears no texture on our side, or a routine that was not watched on the shipped side.
     */
    private static String difference(Pixel own, Pixel theirs, List<Field> compared) {
        if (own == null && theirs == null) {
            return "traced by neither";
        } else if (own == null) {
            return "traced by the shipped toolkit alone";
        } else if (theirs == null) {
            return "traced by ours alone";
        } else {
            var differing = compared.stream()
                .filter(field -> field.differs(own, theirs))
                .map(field -> field.name().toLowerCase())
                .collect(Collectors.joining(", "));
            return differing.isEmpty() ? "nothing traced" : differing;
        }
    }

    /**
     * Says what differs between the run each toolkit drew each of the given pixels in. A pixel
     * covered twice is put down to the run the picture shows.
     */
    private static void reportRuns(Set<Position> positions, List<Run> ours, List<Run> shipped) {
        var counted = new TreeMap<String, Integer>();
        var examples = new TreeMap<String, List<Position>>();

        for (var position : positions) {
            var own = drawnIn(ours, position);
            var theirs = drawnIn(shipped, position);
            String named;
            if (own == null || theirs == null) {
                named = own == null ? "no run of ours" : "no shipped run watched";
            } else {
                named = own.differences(theirs);
            }
            counted.merge(named, 1, Integer::sum);
            examples.computeIfAbsent(named, ignored -> new ArrayList<>());
            if (examples.get(named).size() < EXAMPLES) {
                examples.get(named).add(position);
            }
        }

        System.out.println("  on the runs those pixels were drawn in, what differs:");
        for (var entry : counted.entrySet()) {
            System.out.printf("    %-36s %7d   for example %s%n", entry.getKey(), entry.getValue(),
                examples.get(entry.getKey()));
        }
    }

    /**
     * The run a pixel shows: of every run that covers it, the nearest, and of two as near as each
     * other the later, which is what a buffer that keeps whatever is no further away ends up with.
     */
    private static Run drawnIn(List<Run> runs, Position position) {
        Run found = null;
        for (var run : runs) {
            if (run.covers(position)
                && (found == null || run.depthAt(position) <= found.depthAt(position))) {
                found = run;
            }
        }
        return found;
    }

    private static Run runOf(Call call, long start) {
        var line = call.first();
        var index = (line.getLong(RUN_ADDRESS) - start) / PIXEL_BYTES;
        return new Run((int) (index % Scene.WIDTH), (int) (index / Scene.WIDTH),
            line.getInt(RUN_COUNT), line.getFloat(RUN_DEPTH), line.getFloat(RUN_DEPTH_STEP),
            channels(line, RUN_LIGHT), signedChannels(line, RUN_LIGHT_STEP));
    }

    private static List<Integer> signedChannels(ByteBuffer from, int offset) {
        return IntStream.range(0, CHANNELS_SEEN)
            .mapToObj(channel -> (int) from.getShort(offset + channel * 2))
            .toList();
    }

    private static List<Run> ownRuns(List<String> lines) {
        var runs = new ArrayList<Run>();

        for (var line : lines) {
            if (line.startsWith("R ")) {
                var parts = line.split(" ");
                runs.add(new Run(
                    Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
                    Float.parseFloat(parts[4]), Float.parseFloat(parts[5]),
                    numbers(parts, 6, CHANNELS_SEEN), numbers(parts, 10, CHANNELS_SEEN)));
            }
        }
        return List.copyOf(runs);
    }

    private static Call call(String line) {
        var parts = line.split(" ");
        return new Call(Integer.parseInt(parts[1]), bytes(parts[2]), bytes(parts[3]));
    }

    private static ByteBuffer bytes(String hex) {
        var held = "-".equals(hex) ? new byte[0] : HexFormat.of().parseHex(hex);
        return ByteBuffer.wrap(held).order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * The pixels one call to the body of a span drew. A lane the span masked off is left out, since
     * what the body worked out there was thrown away.
     */
    private static List<Lane> lanesOf(Call call) {
        var group = call.second();
        var address = group.getLong(COLOUR_ADDRESS);
        var lanes = new ArrayList<Lane>();

        for (var lane = 0; lane < LANES; lane++) {
            if (group.getInt(DRAWN + lane * 4) != 0) {
                var pair = SHARES + SHARE_PAIR[lane] * 4;
                var values = new Pixel(-1, -1,
                    group.getFloat(DISTANCE + lane * 4),
                    group.getFloat(PLACE + lane * 8),
                    group.getFloat(PLACE + lane * 8 + 4),
                    channels(group, LIGHT + lane * 8),
                    channels(group, WATER + lane * 8),
                    List.of(word(group, pair), word(group, pair + 2)));
                lanes.add(new Lane(address + (long) lane * PIXEL_BYTES, values));
            }
        }
        return lanes;
    }

    private static List<Integer> channels(ByteBuffer group, int offset) {
        return IntStream.range(0, CHANNELS_SEEN)
            .mapToObj(channel -> word(group, offset + channel * 2))
            .toList();
    }

    private static int word(ByteBuffer group, int offset) {
        return Short.toUnsignedInt(group.getShort(offset));
    }

    /**
     * Our pixels by where they are. A pixel drawn over is kept as it was last drawn, which is what
     * the picture shows.
     */
    private static Map<Position, Pixel> ownPixels(List<String> lines) {
        var pixels = new HashMap<Position, Pixel>();

        for (var line : lines) {
            if (line.startsWith("P ")) {
                var parts = line.split(" ");
                var pixel = new Pixel(
                    Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]),
                    Float.parseFloat(parts[4]),
                    Float.parseFloat(parts[5]),
                    numbers(parts, 6, CHANNELS_SEEN), numbers(parts, 10, CHANNELS_SEEN),
                    numbers(parts, 14, 2));
                pixels.put(pixel.position(), pixel);
            }
        }
        return pixels;
    }

    private static List<Integer> numbers(String[] parts, int from, int count) {
        return Arrays.stream(parts, from, from + count).map(Integer::parseInt).toList();
    }

    /**
     * Where the shipped toolkit's colour buffer starts, taken as the start most pixels agree on
     * once each shipped pixel is matched to ours by its distance and place.
     */
    private static long bufferStart(Map<Position, Pixel> ours, List<Lane> lanes) {
        var byPlace = new HashMap<Place, List<Position>>();
        for (var pixel : ours.values()) {
            byPlace.computeIfAbsent(pixel.place(), ignored -> new ArrayList<>())
                .add(pixel.position());
        }

        var votes = new HashMap<Long, Integer>();
        for (var lane : lanes) {
            for (var position : byPlace.getOrDefault(lane.values().place(), List.of())) {
                var offset = (long) (position.y() * Scene.WIDTH + position.x()) * PIXEL_BYTES;
                votes.merge(lane.address() - offset, 1, Integer::sum);
            }
        }

        return votes.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow(() -> new IllegalStateException(
                "Not one shipped pixel has the distance and place of one of ours."));
    }

    private static Map<Position, Pixel> placed(List<Lane> lanes, long start) {
        var pixels = new HashMap<Position, Pixel>();

        for (var lane : lanes) {
            var index = (lane.address() - start) / PIXEL_BYTES;
            var x = (int) (index % Scene.WIDTH);
            var y = (int) (index / Scene.WIDTH);
            if (index >= 0 && y < Scene.HEIGHT) {
                pixels.put(new Position(x, y), lane.values().at(x, y));
            }
        }
        return pixels;
    }

    private static Set<Position> differingPixels(Path shippedFrames, Path ownFrames)
        throws IOException {
        var shipped = firstFrame(shippedFrames);
        var ours = firstFrame(ownFrames);
        var different = new HashSet<Position>();

        for (var y = 0; y < shipped.getHeight(); y++) {
            for (var x = 0; x < shipped.getWidth(); x++) {
                if ((shipped.getRGB(x, y) & 0xFFFFFF) != (ours.getRGB(x, y) & 0xFFFFFF)) {
                    different.add(new Position(x, y));
                }
            }
        }
        return different;
    }

    private static BufferedImage firstFrame(Path frames) throws IOException {
        return ImageIO.read(frames.resolve("frame-0000.png").toFile());
    }

    /**
     * Prints the first calls to each routine that is not the body of a span, as the words of its
     * first argument. What those routines keep where is not known ahead of time, so they are shown
     * as floats and as pairs of shorts both, and whichever reads as sense is what is kept there.
     */
    private static void dump(List<Call> calls, List<ShippedRoutine.Family> families, int count) {
        var shown = new HashMap<Integer, Integer>();

        for (var call : calls) {
            var family = families.get(call.slot());
            var already = shown.getOrDefault(call.slot(), 0);

            if (family != ShippedRoutine.Family.BODY && already < count) {
                shown.put(call.slot(), already + 1);
                System.out.println("  call " + (already + 1) + " to routine " + call.slot()
                    + ", " + family.name().toLowerCase());

                var first = call.first();
                for (var offset = 0; offset + 4 <= first.capacity(); offset += 4) {
                    if (first.getInt(offset) != 0) {
                        var asFloat = first.getFloat(offset);
                        System.out.printf("    0x%03x  %-16s %6d %6d%n", offset, asFloat,
                            first.getShort(offset), first.getShort(offset + 2));
                    }
                }
            }
        }
    }

    private TraceCheck() {
        /* empty */
    }
}

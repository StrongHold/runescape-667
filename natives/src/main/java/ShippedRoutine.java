import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * One routine of the shipped software rasteriser, named the way the watcher asks for it.
 *
 * The rasteriser is a C++ template, and every routine is one instantiation of it: a shade mode, the
 * way a texture is addressed each way round, and a run of switches for fog, blending, shadow and
 * the rest. The symbol for each is the template's name with those parameters spelled out, so a
 * routine can be named here by its family and its parameters, the way the disassembly prints them,
 * rather than by a symbol three hundred characters long.
 *
 * Written on the command line as the family and the parameters, for example
 * {@code body:2,0,0,false,true,3,false,false}. A switch may be written as 0 or 1 as well.
 */
public record ShippedRoutine(Family family, List<Integer> parameters) {

    private static final String PREFIX = "__ZN12Rasterizer_n";

    /**
     * What one template parameter is, which decides how the symbol spells it.
     */
    enum Kind {
        SHADE("LNS_11ShadeMode_eE"),
        ADDRESS_ACROSS("LNS_16TexAddressMode_eE"),
        ADDRESS_DOWN("LS2_"),
        SWITCH("Lb"),
        FOG("LNS_9FogMode_eE"),
        BLEND("LNS_11BlendMode_eE");

        private final String spelled;

        Kind(String spelled) {
            this.spelled = spelled;
        }

        /**
         * A negative number is spelled with an n in front rather than a minus sign.
         */
        String spell(int value) {
            return spelled + (value < 0 ? "n" + -value : Integer.toString(value)) + "E";
        }
    }

    /**
     * A family of routines, and what the watcher writes down about each call to one of them.
     *
     * The sizes are how many bytes of each argument are kept. They cover every field the routine
     * reads, with room to spare, because what lies past the fields read is only ever noise.
     */
    enum Family {
        SPAN("span", "11RenderHLineI", "EEvRKNS_16HorizontalLine_sE", When.BEFORE, 0x140, 0,
            Kind.SHADE, Kind.ADDRESS_ACROSS, Kind.ADDRESS_DOWN, Kind.SWITCH, Kind.SWITCH,
            Kind.BLEND, Kind.SWITCH, Kind.SWITCH, Kind.SWITCH),
        BODY("body", "18HLineIterationBodyI",
            "EEvRKNS_16HorizontalLine_sERNS_20HLineIterationData_sE", When.AFTER, 0x140, 0x100,
            Kind.SHADE, Kind.ADDRESS_ACROSS, Kind.ADDRESS_DOWN, Kind.SWITCH, Kind.SWITCH,
            Kind.BLEND, Kind.SWITCH, Kind.SWITCH),
        HALF("half", "18RenderHalfTriangleI", "EEvRNS_14HalfTriangle_sE", When.BEFORE, 0x140, 0,
            Kind.SHADE, Kind.ADDRESS_ACROSS, Kind.ADDRESS_DOWN, Kind.SWITCH, Kind.SWITCH,
            Kind.BLEND, Kind.SWITCH, Kind.SWITCH, Kind.SWITCH),
        TRIANGLE("triangle", "14RenderTriangleI", "EEvRKNS_10Triangle_sE", When.BEFORE, 0x140, 0,
            Kind.SHADE, Kind.ADDRESS_ACROSS, Kind.ADDRESS_DOWN, Kind.SWITCH, Kind.FOG,
            Kind.BLEND, Kind.SWITCH, Kind.SWITCH, Kind.SWITCH);

        private final String word;
        private final String opening;
        private final String closing;
        private final When when;
        private final int firstBytes;
        private final int secondBytes;
        private final List<Kind> kinds;

        Family(String word, String opening, String closing, When when, int firstBytes,
               int secondBytes, Kind... kinds) {
            this.word = word;
            this.opening = opening;
            this.closing = closing;
            this.when = when;
            this.firstBytes = firstBytes;
            this.secondBytes = secondBytes;
            this.kinds = List.of(kinds);
        }

        /**
         * The family a symbol written by {@link #symbol()} belongs to.
         */
        static Family ofSymbol(String symbol) {
            return Arrays.stream(values())
                .filter(family -> symbol.startsWith(PREFIX + family.opening))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    symbol + " is not a routine of any family watched here."));
        }

        static Family named(String word) {
            return Arrays.stream(values())
                .filter(family -> family.word.equals(word))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "No family of routines is called " + word + ". The families are "
                        + Arrays.stream(values()).map(family -> family.word).toList() + "."));
        }
    }

    /**
     * Whether a call is written down as it is made or once it returns. The body of a span writes
     * the pixels it worked out into its second argument, so it is read afterwards.
     */
    enum When {
        BEFORE,
        AFTER
    }

    public ShippedRoutine {
        parameters = List.copyOf(parameters);
        if (parameters.size() != family.kinds.size()) {
            throw new IllegalArgumentException("A " + family.word + " takes "
                + family.kinds.size() + " parameters, not " + parameters.size() + ".");
        }
    }

    /**
     * Reads a routine as it is written on the command line.
     */
    public static ShippedRoutine parse(String written) {
        var split = written.split(":", 2);
        if (split.length != 2) {
            throw new IllegalArgumentException("A routine is written as family:parameters, not "
                + written + ".");
        }

        var parameters = Arrays.stream(split[1].split(","))
            .map(String::trim)
            .map(ShippedRoutine::parameter)
            .toList();
        return new ShippedRoutine(Family.named(split[0]), parameters);
    }

    private static int parameter(String written) {
        return switch (written) {
            case "true" -> 1;
            case "false" -> 0;
            default -> Integer.parseInt(written);
        };
    }

    public String symbol() {
        var spelled = new StringBuilder(PREFIX).append(family.opening);
        for (var at = 0; at < parameters.size(); at++) {
            spelled.append(family.kinds.get(at).spell(parameters.get(at)));
        }
        return spelled.append(family.closing).toString();
    }

    /**
     * The line the watcher reads to know this routine is to be watched, and how.
     */
    public String watchLine() {
        var when = family.when == When.AFTER ? "after" : "before";
        return symbol() + " " + when + " " + family.firstBytes + " " + family.secondBytes;
    }

    @Override
    public String toString() {
        return family.word + ":" + parameters.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
    }
}

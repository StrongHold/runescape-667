import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.billboardtype.BillboardType;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorType;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterType;
import com.jagex.game.runetek6.config.flotype.FloorOverlayType;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayType;
import com.jagex.game.runetek6.config.idktype.IDKType;
import com.jagex.game.runetek6.config.lighttype.LightType;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.meltype.MapElementType;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereType;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.js5.Js5Archive;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.IntFunction;

/**
 * Lists, for every config type an exporter reads, what the cache actually holds.
 *
 * Each item is decoded by the client's own reader, one opcode at a time, and the type's fields
 * are compared before and after every opcode. That gives, for each opcode found anywhere in the
 * cache, how often it appears and which fields it sets. An opcode that sets nothing is data the
 * client reads and throws away. An item whose bytes are not all read is a format that is not
 * fully understood. A field that no opcode ever sets is either worked out after decoding or never
 * used by this cache.
 */
public final class CacheCensus {

    /**
     * Where the items of a type are kept: either every file of one group of an archive, numbered
     * by file, or every file of every group of an archive, numbered by group and file together.
     */
    private sealed interface Source {

        /** Every file of every group, with the file number in the low bits of the item's id. */
        record EveryGroup(int archive, int fileBits) implements Source {
            /* empty */
        }

        /** Every file of one group, numbered by file. */
        record OneGroup(int archive, int group) implements Source {
            /* empty */
        }
    }

    private record Kind(String name, Source source, IntFunction<Object> fresh) {
        /* empty */
    }

    /** How many things a player may be offered to do with an NPC, an object or a location. */
    private static final int OPS = 5;

    private static final List<Kind> KINDS = List.of(
        new Kind("npc", new Source.EveryGroup(Js5Archive.CONFIG_NPC, 7), CacheCensus::npc),
        new Kind("obj", new Source.EveryGroup(Js5Archive.CONFIG_OBJ, 8), CacheCensus::obj),
        new Kind("loc", new Source.EveryGroup(Js5Archive.CONFIG_LOC, 8), CacheCensus::loc),
        new Kind("seq", new Source.EveryGroup(Js5Archive.CONFIG_SEQ, 7), CacheCensus::seq),
        new Kind("spotanim", new Source.EveryGroup(Js5Archive.CONFIG_SPOT, 8), CacheCensus::spotAnimation),
        new Kind("bas", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.BASTYPE), id -> new BASType()),
        new Kind("idk", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.IDKTYPE), id -> new IDKType()),
        new Kind("flo", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.FLOTYPE), CacheCensus::overlay),
        new Kind("flu", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.FLUTYPE), id -> new FloorUnderlayType()),
        new Kind("light", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.LIGHTTYPE), id -> new LightType()),
        new Kind("skybox", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.SKYBOXTYPE), id -> new SkyBoxType()),
        new Kind("skyboxsphere", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.SKYBOXSPHERETYPE),
            id -> new SkyBoxSphereType()),
        new Kind("mel", new Source.OneGroup(Js5Archive.CONFIG, Js5ConfigGroup.MELTYPE), CacheCensus::mapElement),
        new Kind("billboard", new Source.OneGroup(Js5Archive.CONFIG_BILLBOARD, 0), id -> new BillboardType()),
        new Kind("emitter", new Source.OneGroup(Js5Archive.CONFIG_PARTICLE, 0), id -> new ParticleEmitterType()),
        new Kind("effector", new Source.OneGroup(Js5Archive.CONFIG_PARTICLE, 1), CacheCensus::effector)
    );

    /** How many failing items of one type are named before the rest are only counted. */
    private static final int NAMED_FAILURES = 5;

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs cache = new CacheArgs();

        @Parameter(names = "--type", description = "A type to take a census of; repeat for several, omit for all")
        private List<String> types = new ArrayList<>();

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /** What one opcode came to across every item of a type. */
    private static final class Opcode {
        private int items;
        private final Set<String> sets = new TreeSet<>();

        /** The bytes each item gave this opcode, as hex, and how many items gave each. */
        private final Map<String, Integer> values = new TreeMap<>();
    }

    /** How many of the values an opcode that sets nothing was given are written out. */
    private static final int SHOWN_VALUES = 20;

    private record Failure(int id, String why) {
        /* empty */
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("censusTypes", new Args(), arguments);
        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(Args args) throws Exception {
        var unknown = args.types.stream()
            .filter(name -> KINDS.stream().noneMatch(kind -> kind.name().equals(name)))
            .toList();
        if (!unknown.isEmpty()) {
            throw new IllegalArgumentException("No type is called " + unknown + ". The types are "
                + KINDS.stream().map(Kind::name).toList() + ".");
        }

        for (var kind : KINDS) {
            if (args.types.isEmpty() || args.types.contains(kind.name())) {
                census(args.cache.cache(), kind);
            }
        }
    }

    private static void census(File cache, Kind kind) throws Exception {
        var opcodes = new TreeMap<Integer, Opcode>();
        var failures = new ArrayList<Failure>();
        var items = 0;
        var step = stepFor(kind.fresh().apply(0).getClass());

        for (var item : itemsOf(cache, kind.source()).entrySet()) {
            items++;
            var failure = decode(kind, item.getKey(), item.getValue(), step, opcodes);
            if (failure != null) {
                failures.add(failure);
            }
        }

        report(kind, items, opcodes, failures);
    }

    /**
     * Decodes one item an opcode at a time, as the type's own loop does, and notes what each
     * opcode changed. Answers why the item could not be read in full, or null when it was.
     */
    private static Failure decode(Kind kind, int id, byte[] data, Method step, Map<Integer, Opcode> opcodes)
            throws IllegalAccessException {
        var type = kind.fresh().apply(id);
        var fields = fieldsOf(type.getClass());
        var packet = new Packet(data);

        try {
            for (var code = packet.g1(); code != 0; code = packet.g1()) {
                var before = snapshot(type, fields);
                var from = packet.pos;
                invoke(step, type, packet, id, code);
                var opcode = opcodes.computeIfAbsent(code, unused -> new Opcode());
                opcode.items++;
                opcode.sets.addAll(changed(before, snapshot(type, fields), fields));
                opcode.values.merge(HexFormat.of().formatHex(data, from, packet.pos), 1, Integer::sum);
            }
        } catch (InvocationTargetException | RuntimeException failure) {
            var cause = failure instanceof InvocationTargetException thrown ? thrown.getCause() : failure;
            return new Failure(id, "throws " + cause + " at byte " + packet.pos);
        }

        return packet.pos == data.length
            ? null
            : new Failure(id, "stops at byte " + packet.pos + " of " + data.length);
    }

    private static void report(Kind kind, int items, Map<Integer, Opcode> opcodes, List<Failure> failures) {
        System.out.println(kind.name() + ": " + items + " items, " + failures.size() + " not read in full");
        failures.stream().limit(NAMED_FAILURES)
            .forEach(failure -> System.out.println("  item " + failure.id() + " " + failure.why()));

        System.out.println("  opcode   items  sets");
        opcodes.forEach((code, opcode) -> {
            System.out.printf("  %6d %7d  %s%n", code, opcode.items,
                opcode.sets.isEmpty() ? "NOTHING" : String.join(", ", opcode.sets));
            if (opcode.sets.isEmpty()) {
                System.out.println("                  given " + valuesOf(opcode));
            }
        });

        var set = new TreeSet<String>();
        opcodes.values().forEach(opcode -> set.addAll(opcode.sets));
        var never = fieldsOf(kind.fresh().apply(0).getClass()).stream()
            .map(Field::getName)
            .filter(name -> !set.contains(name))
            .toList();
        System.out.println("  set by no opcode: " + (never.isEmpty() ? "none" : String.join(", ", never)));
        System.out.println();
    }

    /** The values an opcode was given, most common first, as hex with how many items gave each. */
    private static String valuesOf(Opcode opcode) {
        var shown = opcode.values.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(SHOWN_VALUES)
            .map(entry -> (entry.getKey().isEmpty() ? "nothing" : entry.getKey()) + " x" + entry.getValue())
            .toList();
        var rest = opcode.values.size() - shown.size();
        return String.join(", ", shown) + (rest > 0 ? ", and " + rest + " more" : "");
    }

    /** Every item of a type, by its id, in the order the cache holds them. */
    private static Map<Integer, byte[]> itemsOf(File cache, Source source) throws Exception {
        var items = new TreeMap<Integer, byte[]>();

        switch (source) {
            case Source.EveryGroup every -> {
                var index = Cache.index(cache, every.archive());
                for (var group : Cache.groupsOf(index)) {
                    var files = Cache.split(Cache.group(cache, every.archive(), group), index, group);
                    files.forEach((file, data) -> items.put(group << every.fileBits() | file, data));
                }
            }
            case Source.OneGroup one -> {
                var index = Cache.index(cache, one.archive());
                if (Arrays.stream(Cache.groupsOf(index)).anyMatch(group -> group == one.group())) {
                    items.putAll(Cache.split(Cache.group(cache, one.archive(), one.group()), index, one.group()));
                }
            }
        }

        return items;
    }

    /**
     * The method a type decodes one opcode with. It takes the packet and the opcode, in either
     * order, and a billboard's takes its own id between them.
     */
    private static Method stepFor(Class<?> type) {
        for (var method : type.getDeclaredMethods()) {
            var parameters = method.getParameterTypes();
            var packets = 0;
            var ints = 0;
            for (var parameter : parameters) {
                packets += parameter == Packet.class ? 1 : 0;
                ints += parameter == int.class ? 1 : 0;
            }

            if (method.getName().equals("decode") && packets == 1 && ints == parameters.length - 1 && ints >= 1
                    && !isLoop(type, method)) {
                method.setAccessible(true);
                return method;
            }
        }

        throw new IllegalStateException(type.getSimpleName() + " has no method that decodes one opcode.");
    }

    /** Whether this is the type's loop over every opcode, which takes the packet and at most an id. */
    private static boolean isLoop(Class<?> type, Method method) {
        return type == BillboardType.class ? method.getParameterCount() == 2 : method.getParameterCount() == 1;
    }

    private static void invoke(Method step, Object type, Packet packet, int id, int code)
            throws InvocationTargetException, IllegalAccessException {
        var parameters = step.getParameterTypes();
        var arguments = new Object[parameters.length];
        var ints = List.of(id, code).subList(parameters.length == 3 ? 0 : 1, 2);
        var next = 0;

        for (var at = 0; at < parameters.length; at++) {
            arguments[at] = parameters[at] == Packet.class ? packet : ints.get(next++);
        }

        step.invoke(type, arguments);
    }

    /** Every instance field the type declares, and those of the config classes it extends. */
    private static List<Field> fieldsOf(Class<?> type) {
        var fields = new ArrayList<Field>();
        for (var at = type; at != null && at.getName().startsWith("com.jagex.game.runetek6.config"); at = at.getSuperclass()) {
            for (var field : at.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private static List<Object> snapshot(Object type, List<Field> fields) throws IllegalAccessException {
        var values = new ArrayList<>(fields.size());
        for (var field : fields) {
            values.add(copyOf(field.get(type)));
        }
        return values;
    }

    /** A copy that a later change to the type cannot reach, for arrays; anything else as it is. */
    private static Object copyOf(Object value) {
        if (value == null || !value.getClass().isArray()) {
            return value;
        }

        var length = Array.getLength(value);
        var copy = Array.newInstance(value.getClass().getComponentType(), length);
        for (var at = 0; at < length; at++) {
            Array.set(copy, at, copyOf(Array.get(value, at)));
        }
        return copy;
    }

    private static List<String> changed(List<Object> before, List<Object> after, List<Field> fields) {
        var names = new ArrayList<String>();
        for (var at = 0; at < fields.size(); at++) {
            if (!Objects.deepEquals(before.get(at), after.get(at))) {
                names.add(fields.get(at).getName());
            }
        }
        return names;
    }

    /*
     * A new type of each kind, set up as its list sets it up before decoding: given its id, and
     * given the arrays of options that its opcodes fill in.
     */

    /*
     * The lists the types read from while decoding. Nothing asks them for another type, so they
     * are given no cache.
     */
    private static final NPCTypeList NPC_LIST = new NPCTypeList(ModeGame.RUNESCAPE, 0, true, null, null);
    private static final LocTypeList LOC_LIST = new LocTypeList(ModeGame.RUNESCAPE, 0, true, null, null);

    /**
     * The overlay list reads the cache as soon as it is built, so it is made without its
     * constructor. An overlay only writes its default colour onto it.
     */
    private static final FloorOverlayTypeList OVERLAY_LIST = allocated(FloorOverlayTypeList.class);

    private static NPCType npc(int id) {
        var type = new NPCType();
        type.id = id;
        type.typeList = NPC_LIST;
        type.op = new String[OPS];
        return type;
    }

    private static ObjType obj(int id) {
        var type = new ObjType();
        type.myid = id;
        type.op = new String[OPS];
        type.iop = new String[OPS];
        return type;
    }

    private static LocType loc(int id) {
        var type = new LocType();
        type.id = id;
        type.typeList = LOC_LIST;
        type.ops = new String[OPS];
        return type;
    }

    private static SeqType seq(int id) {
        var type = new SeqType();
        type.id = id;
        return type;
    }

    private static SpotAnimationType spotAnimation(int id) {
        var type = new SpotAnimationType();
        type.id = id;
        return type;
    }

    private static FloorOverlayType overlay(int id) {
        var type = new FloorOverlayType();
        type.id = id;
        type.myList = OVERLAY_LIST;
        return type;
    }

    private static MapElementType mapElement(int id) {
        var type = new MapElementType();
        type.id = id;
        return type;
    }

    private static ParticleEffectorType effector(int id) {
        var type = new ParticleEffectorType();
        type.id = id;
        return type;
    }

    private static <T> T allocated(Class<T> type) {
        try {
            var field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return type.cast(((Unsafe) field.get(null)).allocateInstance(type));
        } catch (ReflectiveOperationException failure) {
            throw new IllegalStateException("Could not allocate a " + type.getSimpleName(), failure);
        }
    }

    private CacheCensus() {
        /* empty */
    }
}

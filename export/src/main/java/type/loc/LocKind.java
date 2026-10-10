package type.loc;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.loctype.LocInteractivity;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.js5.Js5Archive;
import type.Archives;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;
import type.Lists;
import type.Values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The location types ({@code LocTypeList}): the type's data, first what placing a location needs,
 * as the export's README describes, then every other field of the type under the name the client
 * gives it, as the decoder leaves it.
 *
 * <p>The type list's {@code postDecode} works out {@code active} where the entry leaves it -1,
 * {@code raiseobject} where it leaves it -1, and makes a type that animates or changes its look
 * {@code dynamic}. The file holds each as the entry gives it, and {@code interactive}, what
 * {@code active} comes to. The type list also clears {@code blockwalk} and {@code blockrange} of a
 * type that breaks route finding once it has decoded it, which the file leaves to the reader.
 * Codes 150 to 154 set an option as 30 to 34 do, but only in a members' world, which the file holds
 * as {@code membersOps}. Code 5 gives the models twice, the first for a client that leaves its
 * backgrounds still and the second for one that animates them ({@code LocTypeList.animateBackground}),
 * and the decoder skips the one it does not use: the file holds the first as {@code models} and the
 * second as {@code animateBackgroundModelShapes} and {@code animateBackgroundModels}, null where the
 * entry gives only one. Code 106 gives each sequence a weight, which the decoder scales to a share of
 * 65,535. The file holds the weights as given, {@code animWeights}.
 */
public final class LocKind implements ConfigKind<LocType> {

    private static final int LANGUAGE = 0;

    private static final int FILE_BITS = 8;

    /**
     * How many options a location's data can set, in the order the client offers them.
     */
    public static final int OPTION_SLOTS = 5;

    private static final int OPS = 30;

    private static final int MEMBERS_OPS = 150;

    /**
     * The scale the client builds a location at when its type does not change it.
     */
    private static final int FULL_SCALE = 128;

    /**
     * The weight the client gives the one sequence of a type that names only one.
     */
    private static final int SOLE_WEIGHT = 65535;

    private static final Codes CODES = Codes.of()
        .code(1, "modelShapes", "models")
        .code(2, "name")
        .code(5, "modelShapes", "models", "animateBackgroundModelShapes", "animateBackgroundModels")
        .code(14, "width")
        .code(15, "length")
        .code(17, "blockrange", "blockwalk")
        .code(18, "blockrange")
        .code(19, "active")
        .code(21, "hillchange")
        .code(22, "sharelight")
        .code(23, "occlude")
        .code(24, "anim")
        .code(27, "blockwalk")
        .code(28, "walloff")
        .code(29, "ambient")
        .codes(OPS, OPS + OPTION_SLOTS - 1, "ops", "membersOps")
        .code(39, "contrast")
        .code(40, "recol_s", "recol_d")
        .code(41, "retex_s", "retex_d")
        .code(42, "recol_d_palette")
        .code(62, "mirror")
        .code(64, "shadow")
        .code(65, "resizex")
        .code(66, "resizey")
        .code(67, "resizez")
        .code(69, "forceapproach")
        .code(70, "xoff")
        .code(71, "yoff")
        .code(72, "zoff")
        .code(73, "forcedecor")
        .code(74, "breakroutefinding")
        .code(75, "raiseobject")
        .code(77, "multivarbit", "multivarp", "multiloc")
        .code(78, "sound", "soundRange")
        .code(79, "soundDelayMin", "soundDelayMax", "soundRange", "randomSoundIds")
        .code(81, "hillchange", "hillskew")
        .code(82, "istexture")
        .code(88, "hardshadow")
        .code(89, "randomanimframe")
        .code(91, "members")
        .code(92, "multivarbit", "multivarp", "multiloc")
        .code(93, "hillchange", "hillskew")
        .code(94, "hillchange")
        .code(95, "hillchange", "hillskew")
        .code(97, "msirotate")
        .code(98, "animated")
        .code(99, "cursor1Op", "cursor1")
        .code(100, "cursor2Op", "cursor2")
        .code(101, "msiRotateOffset")
        .code(102, "msi")
        .code(103, "occlude")
        .code(104, "soundVolume")
        .code(105, "msiflip")
        .code(106, "anim", "anim_weight")
        .code(107, "mapelement")
        .codes(MEMBERS_OPS, MEMBERS_OPS + OPTION_SLOTS - 1, "ops", "membersOps")
        .code(160, "quests")
        .code(162, "hillchange", "hillskew")
        .code(163, "targetHue", "targetSaturation", "targetLightness", "colourShiftPercentage")
        .code(164, "translateX")
        .code(165, "translateY")
        .code(166, "translateZ")
        .code(167, "offsetY")
        .code(168, "vorbis")
        .code(169, "randomsound")
        .code(170, "occlusionHeight")
        .code(171, "occlusionOffset")
        .code(173, "soundRateMin", "soundRateMax")
        .code(177, "dynamic")
        .code(178, "soundSize")
        .code(249, "params");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "typeList", "The type list the location belongs to.",
        "id", "The location's own id, which the file's name gives."
    );

    private static final Map<String, String> WRITTEN_AS = Map.ofEntries(
        Map.entry("mirror", "mirrored"),
        Map.entry("resizex", "resize"),
        Map.entry("resizey", "resize"),
        Map.entry("resizez", "resize"),
        Map.entry("xoff", "offset"),
        Map.entry("yoff", "offset"),
        Map.entry("zoff", "offset"),
        Map.entry("translateX", "translate"),
        Map.entry("translateY", "translate"),
        Map.entry("translateZ", "translate"),
        Map.entry("width", "sizeTiles"),
        Map.entry("length", "sizeTiles"),
        Map.entry("hardshadow", "hardShadow"),
        Map.entry("randomanimframe", "randomStartFrame"),
        Map.entry("anim_weight", "animWeights"),
        Map.entry("recol_s", "recolours"),
        Map.entry("recol_d", "recolours"),
        Map.entry("recol_d_palette", "recolourPalette"),
        Map.entry("retex_s", "retextures"),
        Map.entry("retex_d", "retextures"),
        Map.entry("targetHue", "tint"),
        Map.entry("targetSaturation", "tint"),
        Map.entry("targetLightness", "tint"),
        Map.entry("colourShiftPercentage", "tint")
    );

    private final LocTypeList list;

    public LocKind(Archives archives) {
        this.list = new LocTypeList(ModeGame.RUNESCAPE, LANGUAGE, true, archives.js5(Js5Archive.CONFIG_LOC),
            archives.js5(Js5Archive.MODELS));
    }

    @Override
    public String directory() {
        return "locs";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.everyGroup(Js5Archive.CONFIG_LOC, FILE_BITS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public LocType create(int id) {
        var type = new LocType();
        type.id = id;
        type.typeList = list;
        type.ops = list.defaultOps.clone();
        return type;
    }

    @Override
    public void decode(LocType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public void postDecode(LocType type, int id) {
        type.postDecode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 5) {
            modelsOf(payload);
            var shapes = new ArrayList<Integer>();
            captured.put("animateBackgroundModels", modelsOf(payload, shapes));
            captured.put("animateBackgroundModelShapes", shapes);
        } else if (code == 24) {
            captured.remove("animWeights");
        } else if (code == 106) {
            var weights = new ArrayList<Integer>();
            var count = payload.g1();
            for (var i = 0; i < count; i++) {
                payload.g2();
                weights.add(payload.g1());
            }
            captured.put("animWeights", weights);
        } else if (code >= OPS && code < OPS + OPTION_SLOTS) {
            ((List<Boolean>) captured.computeIfAbsent("membersOps", key -> noMembersOps())).set(code - OPS, false);
        } else if (code >= MEMBERS_OPS && code < MEMBERS_OPS + OPTION_SLOTS) {
            ((List<Boolean>) captured.computeIfAbsent("membersOps", key -> noMembersOps())).set(code - MEMBERS_OPS, true);
        }
    }

    /**
     * Reads one list of models of each shape, as code 1 gives it, into shapes and the models of
     * each.
     */
    private static List<List<Integer>> modelsOf(Packet payload, List<Integer> shapes) {
        var models = new ArrayList<List<Integer>>();
        var count = payload.g1();
        for (var i = 0; i < count; i++) {
            shapes.add((int) payload.g1b());
            var shape = new ArrayList<Integer>();
            var modelCount = payload.g1();
            for (var j = 0; j < modelCount; j++) {
                shape.add(payload.g2());
            }
            models.add(shape);
        }
        return models;
    }

    private static void modelsOf(Packet payload) {
        modelsOf(payload, new ArrayList<>());
    }

    private static List<Boolean> noMembersOps() {
        return new ArrayList<>(Collections.nCopies(OPTION_SLOTS, false));
    }

    @Override
    public Map<String, Object> json(Decoded<LocType> decoded) {
        var type = decoded.decoded();
        var captured = decoded.captured();
        var extras = new LinkedHashMap<String, Object>();
        extras.put("name", type.name);
        extras.put("shapes", shapes(type));
        extras.put("mirrored", type.mirror);
        if (scaledInAsset(type)) {
            extras.put("resize", List.of(FULL_SCALE, FULL_SCALE, FULL_SCALE));
            extras.put("scaledInAsset", List.of(type.resizex, type.resizey, type.resizez));
        } else {
            extras.put("resize", List.of(type.resizex, type.resizey, type.resizez));
        }
        extras.put("offset", List.of(type.xoff, type.yoff, type.zoff));
        extras.put("translate", List.of(type.translateX, type.translateY, type.translateZ));
        extras.put("hillchange", (int) type.hillchange);
        extras.put("hillskew", type.hillskew);
        extras.put("sizeTiles", List.of(type.width, type.length));
        extras.put("shadow", type.shadow);
        extras.put("hardShadow", type.hardshadow);
        extras.put("interactive", decoded.held().active != LocInteractivity.NONINTERACTIVE);
        extras.put("active", type.active);
        extras.put("ops", Lists.options(type.ops, OPTION_SLOTS));
        extras.put("membersOps", captured.getOrDefault("membersOps", noMembersOps()));
        if (type.hasAnimations()) {
            var sequences = new ArrayList<Integer>();
            var weights = new ArrayList<Integer>();
            for (var i = 0; i < type.anim.length; i++) {
                if (type.anim[i] != -1) {
                    sequences.add(type.anim[i]);
                    weights.add(type.anim.length > 1 ? type.anim_weight[i] : SOLE_WEIGHT);
                }
            }
            extras.put("sequences", sequences);
            if (sequences.size() > 1) {
                extras.put("sequenceWeights", weights);
            }
        }
        extras.put("randomStartFrame", type.randomanimframe);
        extras.put("anim", Values.of(type.anim));
        extras.put("animWeights", captured.get("animWeights"));
        extras.put("models", models(type));
        extras.put("modelShapes", Lists.bytes(type.modelShapes));
        extras.put("animateBackgroundModels", captured.get("animateBackgroundModels"));
        extras.put("animateBackgroundModelShapes", captured.get("animateBackgroundModelShapes"));
        extras.put("recolours", Lists.swaps(type.recol_s, type.recol_d));
        extras.put("recolourPalette", Lists.bytes(type.recol_d_palette));
        extras.put("retextures", Lists.swaps(type.retex_s, type.retex_d));
        extras.put("ambient", type.ambient);
        extras.put("contrast", type.contrast);
        extras.put("tint", List.of((int) type.targetHue, (int) type.targetSaturation, (int) type.targetLightness,
            (int) type.colourShiftPercentage));
        extras.put("sharelight", type.sharelight);
        extras.put("offsetY", type.offsetY);
        extras.put("walloff", type.walloff);
        extras.put("blockwalk", type.blockwalk);
        extras.put("blockrange", type.blockrange);
        extras.put("breakroutefinding", type.breakroutefinding);
        extras.put("forceapproach", type.forceapproach);
        extras.put("forcedecor", type.forcedecor);
        extras.put("raiseobject", type.raiseobject);
        extras.put("occlude", type.occlude);
        extras.put("occlusionHeight", type.occlusionHeight);
        extras.put("occlusionOffset", type.occlusionOffset);
        extras.put("istexture", type.istexture);
        extras.put("dynamic", type.dynamic);
        extras.put("animated", type.animated);
        extras.put("members", type.members);
        extras.put("mapelement", type.mapelement);
        extras.put("msi", type.msi);
        extras.put("msiflip", type.msiflip);
        extras.put("msirotate", type.msirotate);
        extras.put("msiRotateOffset", type.msiRotateOffset);
        extras.put("cursor1Op", type.cursor1Op);
        extras.put("cursor1", type.cursor1);
        extras.put("cursor2Op", type.cursor2Op);
        extras.put("cursor2", type.cursor2);
        extras.put("sound", type.sound);
        extras.put("soundRange", type.soundRange);
        extras.put("soundSize", type.soundSize);
        extras.put("soundVolume", type.soundVolume);
        extras.put("soundDelayMin", type.soundDelayMin);
        extras.put("soundDelayMax", type.soundDelayMax);
        extras.put("soundRateMin", type.soundRateMin);
        extras.put("soundRateMax", type.soundRateMax);
        extras.put("randomsound", type.randomsound);
        extras.put("randomSoundIds", Lists.ints(type.randomSoundIds));
        extras.put("vorbis", type.vorbis);
        extras.put("multivarbit", type.multivarbit);
        extras.put("multivarp", type.multivarp);
        extras.put("multiloc", Lists.ints(type.multiloc));
        extras.put("quests", Lists.ints(type.quests));
        extras.put("params", type.params == null ? Map.of() : Values.of(type.params));
        Fields.putShadowed(extras, "params", type.params);
        return extras;
    }

    /**
     * The shapes the type has models for, in the order of its `modelShapes`.
     */
    public static List<Integer> shapes(LocType type) {
        return Lists.bytes(type.modelShapes);
    }

    /**
     * Whether the type's meshes are scaled when they are built, which is so for a type that
     * animates, as the client scales a location before it poses it and a frame's move is not
     * scaled with it.
     */
    public static boolean scaledInAsset(LocType type) {
        return type.hasAnimations();
    }

    /**
     * The meshes of each shape the type lists in `modelShapes`, by id, as the client's `models`
     * holds them.
     */
    private static List<List<Integer>> models(LocType type) {
        var list = new ArrayList<List<Integer>>();
        if (type.models != null) {
            for (var shape : type.models) {
                list.add(Lists.ints(shape));
            }
        }
        return list;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }

    @Override
    public Map<String, String> writtenAs() {
        return WRITTEN_AS;
    }

    @Override
    public Set<String> settled() {
        return Set.of("active", "raiseobject", "dynamic");
    }
}

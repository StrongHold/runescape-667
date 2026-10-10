package type.entity;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
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
 * The NPC types ({@code NPCTypeList}): every field of the type under the name the client gives it,
 * as the decoder leaves it, with the colour and texture swaps as pairs, the tint as one list, the
 * base animation set as {@code bas} and the type's own id as {@code npc}.
 *
 * <p>The type list's {@code postDecode} gives a type that names no models an empty list, which the
 * file holds anyway, and {@code lowPriorityAttackOps} a default where the entry leaves it -1: 1 in
 * this game. Codes 150 to 154 set an option as 30 to 34 do, but only in a members' world, which the
 * file holds as {@code membersOps}, one flag for each of the five options. The decoder reads a byte
 * for code 128 and drops it, which the file holds as {@code ignored128}, null where the entry gives
 * none.
 */
public final class NpcKind implements ConfigKind<NPCType> {

    private static final int LANGUAGE = 0;

    private static final int FILE_BITS = 7;

    /**
     * How many options an NPC's data can set, in the order the client offers them.
     */
    public static final int OPTION_SLOTS = 5;

    private static final int OPS = 30;

    private static final int MEMBERS_OPS = 150;

    private static final Codes CODES = Codes.of()
        .code(1, "models")
        .code(2, "name")
        .code(12, "size")
        .codes(OPS, OPS + OPTION_SLOTS - 1, "op", "membersOps")
        .code(40, "recol_s", "recol_d")
        .code(41, "retex_s", "retex_d")
        .code(42, "recol_d_palette")
        .code(60, "headModels")
        .code(93, "displayOnMiniMap")
        .code(95, "combatLevel")
        .code(97, "scaleH")
        .code(98, "scaleV")
        .code(99, "renderHighPriority")
        .code(100, "ambient")
        .code(101, "diffusion")
        .code(102, "headIcon")
        .code(103, "yawSpeed")
        .code(106, "multinpcVarbit", "multinpcVarp", "multinpcs")
        .code(107, "interactive")
        .code(109, "crawl")
        .code(111, "hasShadow")
        .code(113, "shadowOuterColour", "shadowInnerColour")
        .code(114, "shadowOuterAlpha", "shadowInnerAlpha")
        .code(118, "multinpcVarbit", "multinpcVarp", "multinpcs")
        .code(119, "movementCapabilities")
        .code(121, "translations")
        .code(122, "healthBarSprite")
        .code(123, "height")
        .code(125, "spawnDirection")
        .code(127, "basId")
        .code(128, "ignored128")
        .code(134, "readySound", "crawlSound", "walkSound", "runSound", "soundRangeMax")
        .code(135, "cursor1Op", "cursor1")
        .code(136, "cursor2Op", "cursor2")
        .code(137, "attackCursor")
        .code(138, "mobilisingArmiesIcon")
        .code(139, "timerbarSprite")
        .code(140, "soundVolume")
        .code(141, "isFollower")
        .code(142, "mapElement")
        .code(143, "renderLowPriority")
        .codes(MEMBERS_OPS, MEMBERS_OPS + OPTION_SLOTS - 1, "op", "membersOps")
        .code(155, "colourHue", "colourSaturation", "colourLightness", "colourScale")
        .code(158, "lowPriorityAttackOps")
        .code(159, "lowPriorityAttackOps")
        .code(160, "quests")
        .code(162, "vorbis")
        .code(163, "quickPick")
        .code(164, "soundRateMin", "soundRateMax")
        .code(165, "pickSizeShift")
        .code(168, "soundRangeMin")
        .code(249, "params");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "typeList", "The type list the NPC belongs to."
    );

    private static final Map<String, String> WRITTEN_AS = Map.ofEntries(
        Map.entry("id", "npc"),
        Map.entry("op", "ops"),
        Map.entry("basId", "bas"),
        Map.entry("recol_s", "recolours"),
        Map.entry("recol_d", "recolours"),
        Map.entry("recol_d_palette", "recolourPalette"),
        Map.entry("retex_s", "retextures"),
        Map.entry("retex_d", "retextures"),
        Map.entry("colourHue", "tint"),
        Map.entry("colourSaturation", "tint"),
        Map.entry("colourLightness", "tint"),
        Map.entry("colourScale", "tint")
    );

    private final NPCTypeList list;

    public NpcKind(Archives archives) {
        this.list = new NPCTypeList(ModeGame.RUNESCAPE, LANGUAGE, true, archives.js5(Js5Archive.CONFIG_NPC),
            archives.js5(Js5Archive.MODELS));
    }

    @Override
    public String directory() {
        return "npcs";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.everyGroup(Js5Archive.CONFIG_NPC, FILE_BITS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public NPCType create(int id) {
        var type = new NPCType();
        type.typeList = list;
        type.id = id;
        type.op = list.defaultOps.clone();
        return type;
    }

    @Override
    public void decode(NPCType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public void postDecode(NPCType type, int id) {
        type.postDecode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 128) {
            captured.put("ignored128", payload.g1());
        } else if (code >= OPS && code < OPS + OPTION_SLOTS) {
            ((List<Boolean>) captured.computeIfAbsent("membersOps", key -> noMembersOps())).set(code - OPS, false);
        } else if (code >= MEMBERS_OPS && code < MEMBERS_OPS + OPTION_SLOTS) {
            ((List<Boolean>) captured.computeIfAbsent("membersOps", key -> noMembersOps())).set(code - MEMBERS_OPS, true);
        }
    }

    private static List<Boolean> noMembersOps() {
        return new ArrayList<>(Collections.nCopies(OPTION_SLOTS, false));
    }

    @Override
    public Map<String, Object> json(Decoded<NPCType> decoded) {
        var type = decoded.decoded();
        var data = new LinkedHashMap<String, Object>();
        data.put("npc", type.id);
        data.put("name", type.name);
        data.put("size", type.size);
        data.put("bas", type.basId);
        data.put("interactive", type.interactive);
        data.put("ops", Lists.options(type.op, OPTION_SLOTS));
        data.put("membersOps", decoded.captured().getOrDefault("membersOps", noMembersOps()));
        data.put("pickSizeShift", type.pickSizeShift);
        data.put("quickPick", type.quickPick);
        data.put("models", Lists.ints(type.models));
        data.put("headModels", Lists.ints(type.headModels));
        data.put("recolours", Lists.swaps(type.recol_s, type.recol_d));
        data.put("recolourPalette", Lists.bytes(type.recol_d_palette));
        data.put("retextures", Lists.swaps(type.retex_s, type.retex_d));
        data.put("translations", translations(type));
        data.put("scaleH", type.scaleH);
        data.put("scaleV", type.scaleV);
        data.put("ambient", type.ambient);
        data.put("diffusion", type.diffusion);
        data.put("tint", List.of((int) type.colourHue, (int) type.colourSaturation, (int) type.colourLightness,
            type.colourScale & 0xFF));
        data.put("hasShadow", type.hasShadow);
        data.put("shadowInnerColour", type.shadowInnerColour & 0xFFFF);
        data.put("shadowOuterColour", type.shadowOuterColour & 0xFFFF);
        data.put("shadowInnerAlpha", (int) type.shadowInnerAlpha);
        data.put("shadowOuterAlpha", (int) type.shadowOuterAlpha);
        data.put("height", type.height);
        data.put("spawnDirection", (int) type.spawnDirection);
        data.put("yawSpeed", type.yawSpeed);
        data.put("crawl", type.crawl);
        data.put("movementCapabilities", (int) type.movementCapabilities);
        data.put("combatLevel", type.combatLevel);
        data.put("displayOnMiniMap", type.displayOnMiniMap);
        data.put("mapElement", type.mapElement);
        data.put("headIcon", type.headIcon);
        data.put("healthBarSprite", type.healthBarSprite);
        data.put("timerbarSprite", type.timerbarSprite);
        data.put("mobilisingArmiesIcon", type.mobilisingArmiesIcon);
        data.put("renderHighPriority", type.renderHighPriority);
        data.put("renderLowPriority", type.renderLowPriority);
        data.put("lowPriorityAttackOps", (int) type.lowPriorityAttackOps);
        data.put("isFollower", type.isFollower);
        data.put("cursor1Op", type.cursor1Op);
        data.put("cursor1", type.cursor1);
        data.put("cursor2Op", type.cursor2Op);
        data.put("cursor2", type.cursor2);
        data.put("attackCursor", type.attackCursor);
        data.put("readySound", type.readySound);
        data.put("crawlSound", type.crawlSound);
        data.put("walkSound", type.walkSound);
        data.put("runSound", type.runSound);
        data.put("soundRangeMin", type.soundRangeMin);
        data.put("soundRangeMax", type.soundRangeMax);
        data.put("soundVolume", type.soundVolume);
        data.put("soundRateMin", type.soundRateMin);
        data.put("soundRateMax", type.soundRateMax);
        data.put("vorbis", type.vorbis);
        data.put("multinpcVarbit", type.multinpcVarbit);
        data.put("multinpcVarp", type.multinpcVarp);
        data.put("multinpcs", Lists.ints(type.multinpcs));
        data.put("quests", Lists.ints(type.quests));
        data.put("params", type.params == null ? Map.of() : Values.of(type.params));
        Fields.putShadowed(data, "params", type.params);
        data.put("ignored128", decoded.captured().get("ignored128"));
        return data;
    }

    /**
     * How far the type moves each of its models before it merges them, one `[x, y, z]` for each
     * model, `[0, 0, 0]` for a model it leaves in place.
     */
    private static List<List<Integer>> translations(NPCType type) {
        var list = new ArrayList<List<Integer>>();
        var models = type.models == null ? 0 : type.models.length;
        for (var model = 0; model < models; model++) {
            var moved = type.translations == null ? null : type.translations[model];
            list.add(moved == null ? List.of(0, 0, 0) : Lists.ints(moved));
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
        return Set.of("models", "lowPriorityAttackOps");
    }
}

package type.data;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.questtype.QuestType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The quests ({@code QuestTypeList}). The client reads only a quest's {@code icon}, for the mini
 * menu, and decodes the rest without reading it again: the name and the name it sorts by, the
 * player's variables and run of bits that hold its progress, each as the variable, its value once
 * the quest is started and its value once it is complete ({@code progressVarps},
 * {@code progressVarbits}), the quests it needs ({@code requiredQuests}) and the skills it needs,
 * each a skill and a level ({@code requiredStats}), and its parameters.
 *
 * <p>The client gives no use to the values of codes 10, 18 and 19, so the file holds them by the
 * code: {@code code10} a list of numbers, and {@code code18} and {@code code19} a list of entries,
 * each three numbers and a string as the decoder reads them. The decoder reads and drops codes 5,
 * 6, 7, 9, 12 and 15, and reads nothing for code 8, which the file holds as {@code ignored5} and
 * so on, null where the entry gives none, and {@code ignored8}, whether it holds code 8. The type
 * list's {@code postDecode} gives a quest without a sort name its name, and the file holds the sort
 * name as the entry gives it.
 */
public final class QuestKind implements ConfigKind<QuestType> {

    private static final Codes CODES = Codes.of()
        .code(1, "name")
        .code(2, "sortedName")
        .code(3, "progressVarps")
        .code(4, "progressVarbits")
        .code(5, "ignored5")
        .code(6, "ignored6")
        .code(7, "ignored7")
        .code(8, "ignored8")
        .code(9, "ignored9")
        .code(10, "code10")
        .code(12, "ignored12")
        .code(13, "requiredQuests")
        .code(14, "requiredStats")
        .code(15, "ignored15")
        .code(17, "icon")
        .code(18, "code18")
        .code(19, "code19")
        .code(249, "params");

    private static final Map<String, String> WRITTEN_AS = Map.of(
        "anIntArray436", "code10",
        "anIntArray432", "code18",
        "anIntArray429", "code18",
        "anIntArray431", "code18",
        "aStringArray28", "code18",
        "anIntArray434", "code19",
        "anIntArray430", "code19",
        "anIntArray428", "code19",
        "aStringArray27", "code19"
    );

    /**
     * The codes whose payload the decoder drops, each one number, by the size it is read at.
     */
    private static final Set<Integer> DROPPED_BYTES = Set.of(6, 7, 9);
    private static final Set<Integer> DROPPED_SHORTS = Set.of(5, 15);
    private static final int DROPPED_INT = 12;
    private static final int DROPPED_FLAG = 8;

    @Override
    public String directory() {
        return "quests";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.QUESTTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public QuestType create(int id) {
        return new QuestType();
    }

    @Override
    public void decode(QuestType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public void postDecode(QuestType type, int id) {
        type.postDecode();
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (DROPPED_BYTES.contains(code)) {
            captured.put("ignored" + code, payload.g1());
        } else if (DROPPED_SHORTS.contains(code)) {
            captured.put("ignored" + code, payload.g2());
        } else if (code == DROPPED_INT) {
            captured.put("ignored" + code, payload.g4());
        } else if (code == DROPPED_FLAG) {
            captured.put("ignored" + code, true);
        }
    }

    @Override
    public Map<String, Object> json(Decoded<QuestType> decoded) {
        var type = decoded.decoded();
        var written = Fields.written(this, decoded);
        written.put("code18", entries(type.anIntArray432, type.anIntArray429, type.anIntArray431, type.aStringArray28));
        written.put("code19", entries(type.anIntArray434, type.anIntArray430, type.anIntArray428, type.aStringArray27));
        for (var code : List.of(5, 6, 7, 9, 12, 15)) {
            written.putIfAbsent("ignored" + code, null);
        }
        written.putIfAbsent("ignored" + DROPPED_FLAG, false);
        return written;
    }

    /**
     * The entries of code 18 or 19, each its three numbers and its string in the order the
     * decoder reads them, none where the entry gives none.
     */
    private static List<List<Object>> entries(int[] first, int[] second, int[] third, String[] texts) {
        var entries = new ArrayList<List<Object>>();
        for (var i = 0; texts != null && i < texts.length; i++) {
            entries.add(List.of(first[i], second[i], third[i], texts[i]));
        }
        return entries;
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }

    @Override
    public Map<String, String> writtenAs() {
        return WRITTEN_AS;
    }

    @Override
    public Set<String> settled() {
        return Set.of("sortedName");
    }
}

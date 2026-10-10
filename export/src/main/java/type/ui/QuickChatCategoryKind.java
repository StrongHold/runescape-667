package type.ui;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.quickchatcattype.QuickChatCatType;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;
import java.util.Set;

/**
 * The categories of the quick chat menu ({@code QuickChatCatTypeList}): the text, and the
 * subcategories and phrases it lists, each with the key that picks it, a string of one character
 * or of the character 0 for none. The list reads the categories below 32,768 from the quick chat
 * archive and the rest from the global quick chat archive, numbered from 32,768, and adds 32,768
 * to each subcategory and phrase a global category lists, which the file holds as added.
 */
public final class QuickChatCategoryKind implements ConfigKind<QuickChatCatType> {

    /**
     * The first id of the global quick chat archive's entries.
     */
    public static final int GLOBAL = 32768;

    private static final int CATEGORIES = 0;

    private static final Codes CODES = Codes.of()
        .code(1, "desc")
        .code(2, "subcategories", "subcategoryShortcuts")
        .code(3, "phrases", "phraseShortcuts")
        .code(4, "ignored4");

    private static final Set<String> DERIVED = Set.of("subcategories", "phrases");

    private final int archive;

    private final int firstId;

    /**
     * The categories of one archive, numbered from its first id.
     */
    public QuickChatCategoryKind(int archive, int firstId) {
        this.archive = archive;
        this.firstId = firstId;
    }

    @Override
    public String directory() {
        return "quickchatcats";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(archive, CATEGORIES).from(firstId);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public QuickChatCatType create(int id) {
        return new QuickChatCatType();
    }

    @Override
    public void decode(QuickChatCatType type, int id, int code, Packet packet) {
        type.packet(packet, code);
    }

    @Override
    public void postDecode(QuickChatCatType type, int id) {
        if (id >= GLOBAL) {
            type.postDecode();
        }
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 4) {
            captured.put("ignored4", true);
        }
    }

    @Override
    public Map<String, Object> json(Decoded<QuickChatCatType> type) {
        var written = Fields.written(this, type);
        written.putIfAbsent("ignored4", false);
        return written;
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }

    @Override
    public Set<String> derived() {
        return DERIVED;
    }
}

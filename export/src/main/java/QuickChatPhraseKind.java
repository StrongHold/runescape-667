
import com.jagex.core.io.Packet;
import com.jagex.game.QuickChatDynamicCommand;
import type.ui.QuickChatCategoryKind;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;
import java.util.Set;

/**
 * The phrases of the quick chat menu ({@code QuickChatPhraseTypeList}): the text as the pieces
 * between the values a player fills in ({@code lines}, the text the client splits at each
 * {@code <}), the commands that fill each value in with their numbers, the phrases the menu offers
 * as replies, and whether a search finds the phrase. As for the categories, the list reads the
 * phrases from 32,768 from the global archive and adds 32,768 to each reply a global phrase lists,
 * which the file holds as added.
 */
public final class QuickChatPhraseKind implements ConfigKind<QuickChatPhraseType> {

    private static final int PHRASES = 1;

    private static final Codes CODES = Codes.of()
        .code(1, "lines")
        .code(2, "autoResponses")
        .code(3, "dynamicCommands", "dynamicCommandParams")
        .code(4, "searchable");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "typeList", "The type list the phrase belongs to."
    );

    private static final Set<String> DERIVED = Set.of("autoResponses");

    private final int archive;

    private final int firstId;

    /**
     * The phrases of one archive, numbered from its first id.
     */
    public QuickChatPhraseKind(int archive, int firstId) {
        this.archive = archive;
        this.firstId = firstId;
    }

    @Override
    public String directory() {
        return "quickchatphrases";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(archive, PHRASES).from(firstId);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public QuickChatPhraseType create(int id) {
        return new QuickChatPhraseType();
    }

    @Override
    public void decode(QuickChatPhraseType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public void postDecode(QuickChatPhraseType type, int id) {
        if (id >= QuickChatCategoryKind.GLOBAL) {
            type.method3902();
        }
    }

    /**
     * Stops at a command the client does not know, whose id the decoder drops.
     */
    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 3) {
            var count = payload.g1();
            for (var i = 0; i < count; i++) {
                var id = payload.g2();
                var command = QuickChatDynamicCommand.fromId(id);
                if (command == null) {
                    throw new IllegalStateException("A quick chat phrase names command " + id + ", which the client does not know.");
                }
                payload.pos += command.varCount * 2;
            }
        }
    }

    @Override
    public Map<String, Object> json(Decoded<QuickChatPhraseType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }

    @Override
    public Set<String> derived() {
        return DERIVED;
    }
}

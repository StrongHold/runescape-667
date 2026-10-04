import com.jagex.core.stringtools.general.StringTools;
import com.jagex.js5.Js5Index;

import java.util.List;
import java.util.Optional;

/**
 * The names of the client's fonts.
 *
 * The sprite archive keeps a hash of each group's name, not the name, and the client finds a font
 * by hashing the name it wants ({@code js5.getgroupid}). A name here is only given to a font whose
 * group carries its hash, so a wrong name is never written, it is only not found.
 */
public final class FontNames {

    /**
     * The client asks for {@code p11_full}, {@code p12_full} and {@code b12_full} by name
     * ({@code Fonts.load}). The rest were found by hashing candidate names.
     */
    private static final List<String> KNOWN = List.of(
        "p11_full",
        "p12_full",
        "b12_full",
        "q8_full",
        "tutorial_font",
        "lunar_alphabet",
        "menu_font_small",
        "verdana_11pt_regular",
        "verdana_13pt_regular",
        "verdana_15pt_regular",
        "palatino_linotype_14pt_regular",
        "palatino_linotype_18pt_regular"
    );

    public static Optional<String> name(Js5Index sprites, int group) {
        if (sprites.groupNames == null || group >= sprites.groupNames.length) {
            return Optional.empty();
        }
        var hash = sprites.groupNames[group];
        return KNOWN.stream().filter(name -> StringTools.intHashCp1252(name) == hash).findFirst();
    }

    private FontNames() {
        /* empty */
    }
}

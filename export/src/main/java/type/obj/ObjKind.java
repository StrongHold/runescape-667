package type.obj;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.paramtype.ParamTypeList;
import com.jagex.js5.Js5Archive;
import type.Archives;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Arrays;
import java.util.Map;

/**
 * The object types ({@code ObjTypeList}): every field under the client's name, as the decoder
 * leaves it: the inventory model ({@code mesh}) and how the inventory draws it, the models a
 * player wears and shows in conversation, the options on the ground ({@code op}, the five the data
 * can set) and in an inventory ({@code iop}), the stack variants, the colour and texture swaps, and
 * the links to the types a certificate, a lent object and a bought object are made from.
 *
 * <p>The type list builds a certificate, a lent and a bought object once it has decoded one, by
 * copying fields of the template and of the linked object over it ({@code ObjType.genCert},
 * {@code genLent}, {@code genBought}), and changes a members' object in a free world. The file holds
 * the entry as it is, and a reader builds those as the client does.
 */
public final class ObjKind implements ConfigKind<ObjType> {

    private static final int LANGUAGE = 0;

    private static final int FILE_BITS = 8;

    /**
     * How many ground options an object's data can set. The type list adds Examine as a sixth.
     */
    private static final int OPTION_SLOTS = 5;

    private static final Codes CODES = Codes.of()
        .code(1, "mesh")
        .code(2, "name")
        .code(4, "zoom2d")
        .code(5, "xan2d")
        .code(6, "yan2d")
        .code(7, "xof2d")
        .code(8, "yof2d")
        .code(11, "stackable")
        .code(12, "cost")
        .code(16, "members")
        .code(18, "multistacksize")
        .code(23, "manwear")
        .code(24, "manwear2")
        .code(25, "womanwear")
        .code(26, "womanwear2")
        .codes(30, 34, "op")
        .codes(35, 39, "iop")
        .code(40, "recol_s", "recol_d")
        .code(41, "retex_s", "retex_d")
        .code(42, "recol_d_palette")
        .code(65, "stockmarket")
        .code(78, "manwear3")
        .code(79, "womanwear3")
        .code(90, "manhead")
        .code(91, "womanhead")
        .code(92, "manhead2")
        .code(93, "womanhead2")
        .code(95, "zan2d")
        .code(96, "dummyitem")
        .code(97, "certlink")
        .code(98, "certtemplate")
        .codes(100, 109, "countobj", "countco")
        .code(110, "resizex")
        .code(111, "resizey")
        .code(112, "resizez")
        .code(113, "ambient")
        .code(114, "contrast")
        .code(115, "team")
        .code(121, "lentlink")
        .code(122, "lenttemplate")
        .code(125, "manwearxoff", "manwearyoff", "manwearzoff")
        .code(126, "womanwearxoff", "womanwearyoff", "womanwearzoff")
        .code(127, "cursor1op", "cursor1")
        .code(128, "cursor2op", "cursor2")
        .code(129, "cursor1iop", "icursor1")
        .code(130, "cursor2iop", "icursor2")
        .code(132, "quests")
        .code(134, "picksizeshift")
        .code(139, "boughtlink")
        .code(140, "boughttemplate")
        .code(249, "params");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "myList", "The type list the object belongs to.",
        "myid", "The object's own id, which the file's name gives."
    );

    private final ObjTypeList list;

    public ObjKind(Archives archives) {
        var config = archives.js5(Js5Archive.CONFIG);
        this.list = new ObjTypeList(ModeGame.RUNESCAPE, LANGUAGE, true, new ParamTypeList(ModeGame.RUNESCAPE, LANGUAGE,
            config), archives.js5(Js5Archive.CONFIG_OBJ), archives.js5(Js5Archive.MODELS));
    }

    @Override
    public String directory() {
        return "objs";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.everyGroup(Js5Archive.CONFIG_OBJ, FILE_BITS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public ObjType create(int id) {
        var type = new ObjType();
        type.myid = id;
        type.myList = list;
        type.op = list.defaultOps.clone();
        type.iop = list.defaultIops.clone();
        return type;
    }

    @Override
    public void decode(ObjType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public void postDecode(ObjType type, int id) {
        type.postDecode();
    }

    @Override
    public Map<String, Object> json(Decoded<ObjType> type) {
        var written = Fields.written(this, type);
        written.put("op", Arrays.asList(type.decoded().op).subList(0, OPTION_SLOTS));
        return written;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}

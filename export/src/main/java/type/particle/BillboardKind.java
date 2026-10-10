package type.particle;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.billboardtype.BillboardType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The billboards a model's faces can carry, a sprite the client draws facing the camera
 * ({@code BillboardTypeList}): the texture, its size, how it blends and colours, and whether it
 * hides its face and where bloom is on. The decoder reads a signed byte for code 3 and drops it,
 * which the file holds as {@code ignored3}, null where the entry gives none.
 */
public final class BillboardKind implements ConfigKind<BillboardType> {

    private static final int BILLBOARDS = 0;

    private static final Codes CODES = Codes.of()
        .code(1, "texture")
        .code(2, "width", "height")
        .code(3, "ignored3")
        .code(4, "blendMode")
        .code(5, "colourOp")
        .code(6, "hideWithBloom")
        .code(7, "hideFace");

    @Override
    public String directory() {
        return "billboards";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG_BILLBOARD, BILLBOARDS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public BillboardType create(int id) {
        return new BillboardType();
    }

    @Override
    public void decode(BillboardType type, int id, int code, Packet packet) {
        type.decode(packet, id, code);
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 3) {
            captured.put("ignored3", (int) payload.g1b());
        }
    }

    @Override
    public Map<String, Object> json(Decoded<BillboardType> type) {
        var written = Fields.written(this, type);
        written.putIfAbsent("ignored3", null);
        return written;
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}

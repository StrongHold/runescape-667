package type.ground;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The sky boxes a map square's environment names ({@code SkyBoxTypeList}), which
 * {@code exportSkyBoxes} writes into one file with their spheres.
 */
public final class SkyBoxKind implements ConfigKind<SkyBoxType> {

    private static final Codes CODES = Codes.of()
        .code(1, "texture")
        .code(2, "sphereIds")
        .code(3, "lightSphereIndex")
        .code(4, "tileMode")
        .code(5, "meshId");

    @Override
    public String directory() {
        return "skyboxes";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.SKYBOXTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public SkyBoxType create(int id) {
        return new SkyBoxType();
    }

    @Override
    public void decode(SkyBoxType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<SkyBoxType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}

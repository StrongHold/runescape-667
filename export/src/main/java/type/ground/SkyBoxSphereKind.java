package type.ground;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The spheres a sky box draws ({@code SkyBoxSphereTypeList}), which {@code exportSkyBoxes} writes
 * inside the sky boxes that name them.
 */
public final class SkyBoxSphereKind implements ConfigKind<SkyBoxSphereType> {

    private static final Codes CODES = Codes.of()
        .code(1, "size")
        .code(2, "infinite")
        .code(3, "x", "y", "z")
        .code(4, "renderType")
        .code(5, "contentId")
        .code(6, "colour")
        .code(7, "rotateX", "rotateY", "rotateZ");

    @Override
    public String directory() {
        return "skyboxspheres";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.SKYBOXSPHERETYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public SkyBoxSphereType create(int id) {
        return new SkyBoxSphereType();
    }

    @Override
    public void decode(SkyBoxSphereType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<SkyBoxSphereType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}

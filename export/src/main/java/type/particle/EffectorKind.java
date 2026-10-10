package type.particle;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;
import java.util.Set;

/**
 * The particle effector types ({@code ParticleEffectorTypeList}), which the texture export writes
 * into one file beside the textures, each as the client holds it after {@code postDecode}: it works
 * out the cosine of the angle, the length of the direction, negative for an effector that
 * attracts, and the range, and makes a strength of 0 into 1. The decoder reads a byte for code 2
 * and drops it, which this kind holds as {@code ignored2}. No entry of this cache holds it.
 */
public final class EffectorKind implements ConfigKind<ParticleEffectorType> {

    private static final int EFFECTORS = 1;

    private static final Codes CODES = Codes.of()
        .code(1, "angle")
        .code(2, "ignored2")
        .code(3, "dirX", "dirY", "dirZ")
        .code(4, "effectType", "strength")
        .code(6, "visibility")
        .code(8, "constantSpeed")
        .code(9, "constantStrength")
        .code(10, "attract");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "id", "The effector's own id, which its key gives."
    );

    @Override
    public String directory() {
        return "effectors";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG_PARTICLE, EFFECTORS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public ParticleEffectorType create(int id) {
        var type = new ParticleEffectorType();
        type.id = id;
        return type;
    }

    @Override
    public void decode(ParticleEffectorType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public void postDecode(ParticleEffectorType type, int id) {
        type.postDecode();
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 2) {
            captured.put("ignored2", payload.g1());
        }
    }

    @Override
    public Map<String, Object> json(Decoded<ParticleEffectorType> type) {
        var written = Fields.written(this, type);
        written.putIfAbsent("ignored2", null);
        return written;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }

    @Override
    public Set<String> derived() {
        return Set.of("cosTheta", "dirLength", "maxRange", "strength");
    }
}

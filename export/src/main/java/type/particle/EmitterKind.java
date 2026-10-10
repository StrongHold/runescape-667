package type.particle;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;
import java.util.Set;

/**
 * The particle emitter types ({@code ParticleEmitterTypeList}), which the texture export writes
 * into one file beside the textures, each as the client holds it after {@code postDecode}, with
 * the colour channels, ranges, durations and steps it works out. The decoder reads a byte for code
 * 2 and a signed short for code 29 and drops them, which this kind holds as {@code ignored2} and
 * {@code ignored29}. No entry of this cache holds either.
 */
public final class EmitterKind implements ConfigKind<ParticleEmitterType> {

    private static final int EMITTERS = 0;

    private static final Codes CODES = Codes.of()
        .code(1, "minAngleH", "maxAngleH", "minAngleV", "maxAngleV")
        .code(2, "ignored2")
        .code(3, "minSpeed", "maxSpeed")
        .code(4, "decelerationType", "decelerationRate")
        .code(5, "minSize", "maxSize")
        .code(6, "minStartColour", "maxStartColour")
        .code(7, "minLifetime", "maxLifetime")
        .code(8, "minParticleRate", "maxParticleRate")
        .code(9, "localEffectors")
        .code(10, "globalEffectors")
        .code(12, "minHeightLevel")
        .code(13, "maxHeightLevel")
        .code(14, "startupTicks")
        .code(15, "texture")
        .code(16, "activeFirst", "activationAge", "lifetime", "periodic")
        .code(17, "untextured")
        .code(18, "fadeColour")
        .code(19, "minSetting")
        .code(20, "colourFadePercentage")
        .code(21, "alphaFadePercentage")
        .code(22, "endSpeed")
        .code(23, "speedChangePercentage")
        .code(24, "uniformColourVariance")
        .code(25, "generalEffectors")
        .code(26, "disableHdLighting")
        .code(27, "endSize")
        .code(28, "sizeChangePercentage")
        .code(29, "ignored29")
        .code(30, "softwareTextured")
        .code(31, "minSize", "maxSize")
        .code(32, "preserveAmbient")
        .code(33, "collidesWithLocations")
        .code(34, "collidesWithGround");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "globalEffectorIndices", "The particles find the client's slot of each global effector the first time they move."
    );

    private static final Set<String> DERIVED = Set.of("minStartRed", "maxStartRed", "startRedRange", "minStartGreen",
        "maxStartGreen", "startGreenRange", "minStartBlue", "maxStartBlue", "startBlueRange", "minStartAlpha",
        "maxStartAlpha", "startAlphaRange", "hasHeightLevelBounds", "sizeChangeDuration", "sizeChangeStep",
        "speedChangeDuration", "speedChangeStep", "alphaFadeDuration", "colourFadeDuration", "redFadeStep",
        "greenFadeStep", "blueFadeStep", "alphaFadeStep");

    @Override
    public String directory() {
        return "emitters";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG_PARTICLE, EMITTERS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public ParticleEmitterType create(int id) {
        return new ParticleEmitterType();
    }

    @Override
    public void decode(ParticleEmitterType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public void postDecode(ParticleEmitterType type, int id) {
        type.postDecode();
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 2) {
            captured.put("ignored2", payload.g1());
        } else if (code == 29) {
            captured.put("ignored29", payload.g2s());
        }
    }

    @Override
    public Map<String, Object> json(Decoded<ParticleEmitterType> type) {
        var written = Fields.written(this, type);
        written.putIfAbsent("ignored2", null);
        written.putIfAbsent("ignored29", null);
        return written;
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

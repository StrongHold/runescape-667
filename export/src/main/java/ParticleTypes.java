import com.jagex.game.runetek6.config.effectortype.ParticleEffectorType;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorTypeList;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterType;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterTypeList;
import com.jagex.js5.js5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Writes the client's particle emitter and effector types beside the textures, each as the
 * client holds it after decoding: every public field of {@code ParticleEmitterType} and
 * {@code ParticleEffectorType} by its name, with the values {@code postDecode} derives, in a
 * list indexed by the type's id, null where the cache has no type of that id. An effector's own
 * {@code id} is left out, as its place in the list gives it.
 */
public final class ParticleTypes {

    private static final String OWN_ID_FIELD = "id";

    private static final int EMITTER_GROUP = 0;
    private static final int EFFECTOR_GROUP = 1;

    public static void write(js5 particles, Path textures) throws IOException {
        var directory = textures.resolve("particle");
        Files.createDirectories(directory);
        ParticleEmitterTypeList.setConfigClient(particles);
        ParticleEffectorTypeList.setConfigClient(particles);
        var emitters = new ArrayList<Object>();
        for (var id : ids(particles, EMITTER_GROUP)) {
            pad(emitters, id);
            emitters.add(PublicFields.of(ParticleEmitterTypeList.get(id)));
        }
        var effectors = new ArrayList<Object>();
        for (var id : ids(particles, EFFECTOR_GROUP)) {
            pad(effectors, id);
            var fields = PublicFields.of(ParticleEffectorTypeList.get(id));
            fields.remove(OWN_ID_FIELD);
            effectors.add(fields);
        }
        Files.writeString(directory.resolve("emitters.json"), Json.write(emitters));
        Files.writeString(directory.resolve("effectors.json"), Json.write(effectors));
    }

    public static int count(js5 particles, boolean emitters) {
        return ids(particles, emitters ? EMITTER_GROUP : EFFECTOR_GROUP).length;
    }

    private static int[] ids(js5 particles, int group) {
        var ids = particles.fileIds(group);
        var sorted = ids == null ? new int[0] : ids.clone();
        Arrays.sort(sorted);
        return sorted;
    }

    private static void pad(List<Object> list, int id) {
        while (list.size() < id) {
            list.add(null);
        }
    }

    private ParticleTypes() {
        /* empty */
    }
}

import com.jagex.game.runetek6.config.effectortype.ParticleEffectorType;
import com.jagex.game.runetek6.config.effectortype.ParticleEffectorTypeList;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterType;
import com.jagex.game.runetek6.config.emittertype.ParticleEmitterTypeList;
import com.jagex.js5.js5;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes the client's particle emitter and effector types beside the textures, each as the
 * client holds it after decoding: every public field of {@code ParticleEmitterType} and
 * {@code ParticleEffectorType} by its name, with the values {@code postDecode} derives, in a
 * list indexed by the type's id, null where the cache has no type of that id.
 */
public final class ParticleTypes {

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
            emitters.add(fields(ParticleEmitterTypeList.get(id)));
        }
        var effectors = new ArrayList<Object>();
        for (var id : ids(particles, EFFECTOR_GROUP)) {
            pad(effectors, id);
            effectors.add(fields(ParticleEffectorTypeList.get(id)));
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

    /** A type's public instance fields by name: numbers, flags and lists of numbers. */
    private static Map<String, Object> fields(Object type) {
        var values = new LinkedHashMap<String, Object>();
        var fields = type.getClass().getFields();
        Arrays.sort(fields, (a, b) -> a.getName().compareTo(b.getName()));
        for (var field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                var value = field.get(type);
                if (value instanceof int[] numbers) {
                    values.put(field.getName(), Arrays.stream(numbers).boxed().toList());
                } else if (value instanceof Number || value instanceof Boolean) {
                    values.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return values;
    }

    private ParticleTypes() {
        /* empty */
    }
}

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Where a model's particles come from, for a shape's {@code extras}: each emitter's type, the
 * draw priority of its particles, and the three corners of the face it spawns them over, and
 * each effector's type and the vertex it stands at. The points are the client's vertices, in its
 * units and its frame, before the file's turn, so an engine places them as it places the
 * model's vertices.
 */
public final class ParticleSources {

    public static List<Map<String, Object>> emitters(JavaModel model) {
        var written = new ArrayList<Map<String, Object>>();
        for (var emitter : model.emitters) {
            var entry = new LinkedHashMap<String, Object>();
            entry.put("type", emitter.id);
            entry.put("priority", (int) emitter.priority);
            entry.put("a", vertex(model, emitter.vertexA));
            entry.put("b", vertex(model, emitter.vertexB));
            entry.put("c", vertex(model, emitter.vertexC));
            written.add(entry);
        }
        return written;
    }

    public static List<Map<String, Object>> effectors(JavaModel model) {
        var written = new ArrayList<Map<String, Object>>();
        for (var effector : model.effectors) {
            var entry = new LinkedHashMap<String, Object>();
            entry.put("type", effector.type);
            entry.put("at", vertex(model, effector.vertex));
            written.add(entry);
        }
        return written;
    }

    private static List<Integer> vertex(JavaModel model, int vertex) {
        return List.of(model.vertexX[vertex], model.vertexY[vertex], model.vertexZ[vertex]);
    }

    private ParticleSources() {
        /* empty */
    }
}

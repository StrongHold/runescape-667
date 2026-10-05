import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The sets of sprites found for one widget: each set once, under the names of its parts, with the
 * scripts and the interfaces that give it. A part a set does not have is null and is not written.
 */
final class WidgetSets {

    /**
     * Where a set was found: the scripts that give it, and the interfaces whose components do.
     */
    private record Found(TreeSet<Integer> scripts, TreeSet<Integer> interfaces) {
    }

    private final List<String> fields;

    private final Map<List<Integer>, Found> sets = new LinkedHashMap<>();

    WidgetSets(List<String> fields) {
        this.fields = fields;
    }

    /**
     * Adds a set, found in a script, an interface, or both, where either may be null.
     */
    void add(List<Integer> sprites, Integer script, Integer interfaceId) {
        var found = sets.computeIfAbsent(Collections.unmodifiableList(new ArrayList<>(sprites)), key -> new Found(new TreeSet<>(), new TreeSet<>()));
        if (script != null) {
            found.scripts().add(script);
        }
        if (interfaceId != null) {
            found.interfaces().add(interfaceId);
        }
    }

    int size() {
        return sets.size();
    }

    /**
     * Each set found, as its sprites in the order of its parts.
     */
    Set<List<Integer>> keys() {
        return sets.keySet();
    }

    /**
     * The sets as the file holds them. A sprite that is not in the cache stops the export, since the
     * widget could not be drawn.
     */
    List<Map<String, Object>> written(String widget, Set<Integer> cached) {
        var written = new ArrayList<Map<String, Object>>();
        for (var set : sets.entrySet()) {
            var entry = new LinkedHashMap<String, Object>();
            for (var at = 0; at < fields.size(); at++) {
                var sprite = set.getKey().get(at);
                if (sprite != null) {
                    if (!cached.contains(sprite)) {
                        System.out.println(widget + " name sprite " + sprite + ", which is not in the cache");
                        System.exit(1);
                    }
                    entry.put(fields.get(at), sprite);
                }
            }
            if (!set.getValue().scripts().isEmpty()) {
                entry.put("scripts", new ArrayList<>(set.getValue().scripts()));
            }
            if (!set.getValue().interfaces().isEmpty()) {
                entry.put("interfaces", new ArrayList<>(set.getValue().interfaces()));
            }
            written.add(entry);
        }
        return written;
    }
}

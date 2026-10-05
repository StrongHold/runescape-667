import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The script hooks of every component of every interface in the cache, decoded with the client's
 * own decoder ({@code Component.decode}): which script each hook runs, and with what arguments.
 */
final class WidgetHooks {

    /**
     * A component with hooks: its interface, its sprite, or -1 for none, and each hook by the name
     * of the client's field that keeps it, as the script it runs followed by its arguments.
     */
    record Hooked(int interfaceId, int graphic, Map<String, Object[]> hooks) {
    }

    private final List<Hooked> components;

    private WidgetHooks(List<Hooked> components) {
        this.components = components;
    }

    static WidgetHooks read(File cache) throws Exception {
        var index = Cache.index(cache, Js5Archive.INTERFACES);
        var components = new ArrayList<Hooked>();
        for (var group : Cache.groupsOf(index)) {
            var data = Cache.group(cache, Js5Archive.INTERFACES, group);
            if (data != null) {
                for (var file : Cache.split(data, index, group).values()) {
                    var component = new Component();
                    component.decode(new Packet(file));
                    var hooks = hooksOf(component);
                    if (!hooks.isEmpty()) {
                        components.add(new Hooked(group, component.graphic, hooks));
                    }
                }
            }
        }
        return new WidgetHooks(components);
    }

    private static Map<String, Object[]> hooksOf(Component component) throws IllegalAccessException {
        var hooks = new LinkedHashMap<String, Object[]>();
        for (var field : Component.class.getFields()) {
            if (field.getType() == Object[].class && field.getName().startsWith("on")) {
                var hook = (Object[]) field.get(component);
                if (hook != null && hook.length > 0 && hook[0] instanceof Integer) {
                    hooks.put(field.getName(), hook);
                }
            }
        }
        return hooks;
    }

    int size() {
        return components.size();
    }

    /**
     * Each component with a hook that runs a script, with the arguments of each of its hooks that
     * runs it, by hook.
     */
    List<Calling> calling(int script) {
        var found = new ArrayList<Calling>();
        for (var component : components) {
            var arguments = new LinkedHashMap<String, Object[]>();
            for (var hook : component.hooks().entrySet()) {
                if ((Integer) hook.getValue()[0] == script) {
                    var given = new Object[hook.getValue().length - 1];
                    System.arraycopy(hook.getValue(), 1, given, 0, given.length);
                    arguments.put(hook.getKey(), given);
                }
            }
            if (!arguments.isEmpty()) {
                found.add(new Calling(component, arguments));
            }
        }
        return found;
    }

    /**
     * A component whose hooks run a script, and the arguments each of those hooks gives it.
     */
    record Calling(Hooked component, Map<String, Object[]> arguments) {

        /**
         * The number a hook gives at an argument, or null where it gives none there.
         */
        Integer integer(String hook, int at) {
            var given = arguments.get(hook);
            return given != null && at < given.length && given[at] instanceof Integer value ? value : null;
        }

        /**
         * The sprite a hook gives at an argument, or null where it gives none, or -1 for no sprite.
         */
        Integer sprite(String hook, int at) {
            var value = integer(hook, at);
            return value == null || value < 0 ? null : value;
        }
    }
}

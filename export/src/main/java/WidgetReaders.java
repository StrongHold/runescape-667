import com.jagex.core.constants.ClientScriptOpCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The widgets whose sprites are given by interface hooks or held in a script's own instructions,
 * rather than passed to a widget script by its callers. Each reader checks that its script still
 * has the shape it reads, and stops the export where it does not.
 */
final class WidgetReaders {

    /**
     * The client runs an interface command on a component it is given, rather than the one a hook
     * runs on, under the command's own number plus this ({@code ScriptRunner}).
     */
    private static final int ON_GIVEN_COMPONENT = 1000;

    private static final int IF_SETGRAPHIC = ClientScriptOpCode.CC_IF_SETGRAPHIC + ON_GIVEN_COMPONENT;

    /**
     * Script 2975 sets the plate of a button, its edges and its middle, to one of two sets as the
     * pointer moves over and off it; script 4588 sets the three pieces of a row to sprites its hooks
     * give it.
     */
    private static final int HOVER_PLATE = 2975;
    private static final int HOVER_ROW = 4588;

    /**
     * Script 44 sets the sprite of the component its hook runs on, script 4587 the sprite of a
     * component its hook names, and script 4782 sets one of four tabs to its sprite or the sprite
     * under the pointer.
     */
    private static final int SWAP_SPRITE = 44;
    private static final int SWAP_CHILD_SPRITE = 4587;
    private static final int HOVER_TAB = 4782;

    /**
     * Scripts 1422 and 1423 select one of four and of twelve radio buttons: the first component
     * they are given takes the selected sprite and the others the plain one.
     */
    private static final List<Integer> RADIO_GROUPS = List.of(1422, 1423);

    /**
     * Script 1436 builds a dropdown: its background, its arrow and the arrow under the pointer, the
     * background of its open list, the colours of its text, plain, for the other options and under
     * the pointer, its font, and the sprites of the scrollbar of its list, in that order from its
     * eighth argument.
     */
    private static final int DROPDOWN = 1436;
    private static final int DROPDOWN_FIRST_SPRITE = 7;
    private static final List<String> DROPDOWN_FIELDS = List.of(
        "background",
        "arrow",
        "hoverArrow",
        "listBackground",
        "textColour",
        "otherTextColour",
        "hoverTextColour",
        "font"
    );
    private static final List<String> SCROLLBAR_FIELDS = List.of("track", "draggerTop", "draggerMiddle", "draggerBottom", "upArrow", "downArrow");

    private static final String OVER = "onMouseOver";
    private static final String LEAVE = "onMouseLeave";

    /**
     * The hooks that put a button's sprite back, show it under the pointer, and show it pressed, in
     * the order each is looked for.
     */
    private static final List<String> NORMAL_HOOKS = List.of(LEAVE, "onRelease");
    private static final List<String> HOVER_HOOKS = List.of(OVER, "onMouseRepeat");
    private static final List<String> PRESSED_HOOKS = List.of("onClick", "onHold", "onClickRepeat");

    static void plateButtons(Map<Integer, ClientScript> scripts, WidgetHooks hooks, WidgetSets sets) {
        var graphics = graphicsOf(scripts.get(HOVER_PLATE));
        if (graphics.size() != 6 || !targets(graphics.subList(0, 3), 0, 2, 1) || !targets(graphics.subList(3, 6), 0, 2, 1)
            || !graphics.get(0)[1].equals(graphics.get(1)[1]) || !graphics.get(3)[1].equals(graphics.get(4)[1])) {
            stop(HOVER_PLATE, "set the edges and the middle of a plate in two sets, under the pointer first");
        }
        var plate = Arrays.asList(graphics.get(3)[1], graphics.get(5)[1], graphics.get(0)[1], graphics.get(2)[1]);
        sets.add(plate, HOVER_PLATE, null);
        for (var calling : hooks.calling(HOVER_PLATE)) {
            sets.add(plate, HOVER_PLATE, calling.component().interfaceId());
        }

        for (var calling : hooks.calling(HOVER_ROW)) {
            var normal = rowOf(calling, LEAVE);
            var hover = rowOf(calling, OVER);
            if (normal != null && hover != null) {
                sets.add(Arrays.asList(normal[0], normal[1], hover[0], hover[1]), null, calling.component().interfaceId());
            }
        }
    }

    static void spriteButtons(Map<Integer, ClientScript> scripts, WidgetHooks hooks, WidgetSets sets) {
        for (var calling : hooks.calling(SWAP_SPRITE)) {
            var own = calling.component().graphic();
            var sprite = first(calling, NORMAL_HOOKS, 1);
            var hover = first(calling, HOVER_HOOKS, 1);
            var pressed = first(calling, PRESSED_HOOKS, 1);
            var shown = sprite != null ? sprite : own >= 0 ? Integer.valueOf(own) : null;
            if (hover != null || pressed != null) {
                sets.add(Arrays.asList(shown, hover, pressed), null, calling.component().interfaceId());
            }
        }

        for (var calling : hooks.calling(SWAP_CHILD_SPRITE)) {
            var sprite = calling.sprite(LEAVE, 1);
            var hover = calling.sprite(OVER, 1);
            var target = calling.integer(LEAVE, 0);
            if (hover != null && target != null && target.equals(calling.integer(OVER, 0))) {
                sets.add(Arrays.asList(sprite, hover, null), null, calling.component().interfaceId());
            }
        }

        var tabs = tabsOf(scripts.get(HOVER_TAB));
        var interfaces = hooks.calling(HOVER_TAB).stream().map(calling -> calling.component().interfaceId()).distinct().toList();
        for (var tab : tabs) {
            var set = Arrays.asList(tab[1], tab[0], null);
            sets.add(set, HOVER_TAB, null);
            for (var interfaceId : interfaces) {
                sets.add(set, HOVER_TAB, interfaceId);
            }
        }
    }

    /**
     * The fewest scripts that must select between two sprites, and never the other way round, for
     * them to be taken for a radio button.
     */
    private static final int RADIO_SCRIPTS = 2;

    /**
     * The fewest components a script must set the plain sprite on in a row for the sprite it then
     * sets on one of them to be taken for a radio button's selected sprite, which marks a group
     * apart from a mark set on one or two.
     */
    private static final int RADIO_GROUP = 3;

    static void radioButtons(Map<Integer, ClientScript> scripts, WidgetHooks hooks, WidgetSets sets, Map<Integer, int[]> sizes) {
        selectedPairs(scripts, sizes, sets);
        for (var script : RADIO_GROUPS) {
            var graphics = graphicsOf(scripts.get(script));
            var selected = graphics.isEmpty() ? null : graphics.get(0);
            var plain = graphics.size() < 2 ? null : graphics.get(1)[1];
            var shaped = selected != null && selected[0] == 0 && plain != null
                && graphics.subList(1, graphics.size()).stream().allMatch(graphic -> graphic[0] != 0 && graphic[1].equals(plain));
            if (!shaped) {
                stop(script, "give the first component it is given one sprite and every other component another");
            }
            var set = Arrays.asList(plain, selected[1]);
            sets.add(set, script, null);
            for (var calling : hooks.calling(script)) {
                sets.add(set, script, calling.component().interfaceId());
            }
        }
    }

    /**
     * Each dropdown that a call of script 1436 builds, once, where the call gives all of it as
     * constants. The colour of the other options is written only where it differs from the plain
     * colour.
     */
    static List<Map<String, Object>> dropdowns(WidgetExport.CallReader calls, java.util.Set<Integer> cached) {
        var found = new java.util.LinkedHashMap<String, Map<String, Object>>();
        for (var given : calls.argumentsOf(DROPDOWN)) {
            var named = Arrays.asList(given).subList(DROPDOWN_FIRST_SPRITE, DROPDOWN_FIRST_SPRITE + DROPDOWN_FIELDS.size() + SCROLLBAR_FIELDS.size());
            if (!named.contains(null)) {
                var dropdown = new java.util.LinkedHashMap<String, Object>();
                for (var at = 0; at < DROPDOWN_FIELDS.size(); at++) {
                    dropdown.put(DROPDOWN_FIELDS.get(at), named.get(at));
                }
                if (dropdown.get("otherTextColour").equals(dropdown.get("textColour"))) {
                    dropdown.remove("otherTextColour");
                }
                var scrollbar = new java.util.LinkedHashMap<String, Object>();
                for (var at = 0; at < SCROLLBAR_FIELDS.size(); at++) {
                    scrollbar.put(SCROLLBAR_FIELDS.get(at), named.get(DROPDOWN_FIELDS.size() + at));
                }
                dropdown.put("scrollbar", scrollbar);
                for (var sprite : List.of("background", "arrow", "hoverArrow", "listBackground")) {
                    if (!cached.contains((Integer) dropdown.get(sprite))) {
                        stop(DROPDOWN, "give its dropdown sprites that are in the cache");
                    }
                }
                found.putIfAbsent(Json.write(dropdown), dropdown);
            }
        }
        return new ArrayList<>(found.values());
    }

    /**
     * Adds the radio buttons that scripts select as a group: a script that sets one sprite on three
     * components or more, the plain sprite, and then another on one of them, the selected sprite.
     * Two sprites are taken for a radio button where they are the same square size, at least two
     * scripts select between them that way, and none the other way, which marks them apart from the
     * tabs and plates scripts swap.
     */
    private static void selectedPairs(Map<Integer, ClientScript> scripts, Map<Integer, int[]> sizes, WidgetSets sets) {
        var found = new java.util.TreeMap<List<Integer>, List<Integer>>(java.util.Comparator.comparing(Object::toString));
        for (var script : scripts.entrySet()) {
            var pair = selectedPairOf(script.getValue());
            if (pair != null) {
                found.computeIfAbsent(pair, key -> new ArrayList<>()).add(script.getKey());
            }
        }
        for (var pair : found.entrySet()) {
            var plain = pair.getKey().get(0);
            var selected = pair.getKey().get(1);
            var reversed = found.containsKey(List.of(selected, plain));
            var plainSize = sizes.get(plain);
            var selectedSize = sizes.get(selected);
            var square = plainSize != null && selectedSize != null && Arrays.equals(plainSize, selectedSize) && plainSize[0] == plainSize[1];
            if (pair.getValue().size() >= RADIO_SCRIPTS && !reversed && square) {
                for (var script : pair.getValue()) {
                    sets.add(Arrays.asList(plain, selected), script, null);
                }
            }
        }
    }

    /**
     * The plain and selected sprites a script selects between, or null where it does not: the first
     * sprite it sets on two components or more in a row, and the one other sprite it sets on any of
     * them after.
     */
    private static List<Integer> selectedPairOf(ClientScript script) {
        var targets = new ArrayList<String>();
        var sprites = new ArrayList<Integer>();
        for (var at = 2; at < script.opcodes.length; at++) {
            var target = targetOf(script, at - 1);
            if (script.opcodes[at] == IF_SETGRAPHIC && script.opcodes[at - 2] == ClientScriptOpCode.PUSH_CONSTANT_INT && target != null) {
                targets.add(target);
                sprites.add(script.intOperands[at - 2]);
            }
        }
        for (var start = 0; start < sprites.size(); start++) {
            var end = start;
            var plainTargets = new java.util.HashSet<String>();
            while (end < sprites.size() && sprites.get(end).equals(sprites.get(start))) {
                plainTargets.add(targets.get(end));
                end++;
            }
            if (plainTargets.size() >= RADIO_GROUP) {
                var selected = new java.util.TreeSet<Integer>();
                for (var at = end; at < sprites.size(); at++) {
                    if (!sprites.get(at).equals(sprites.get(start)) && plainTargets.contains(targets.get(at))) {
                        selected.add(sprites.get(at));
                    }
                }
                return selected.size() == 1 && sprites.get(start) >= 0 ? List.of(sprites.get(start), selected.first()) : null;
            }
        }
        return null;
    }

    /**
     * The component an instruction pushes for a command, as text: an argument of the script by its
     * number, or a component named by a constant, or null where it is neither.
     */
    private static String targetOf(ClientScript script, int at) {
        if (script.opcodes[at] == ClientScriptOpCode.PUSH_INT_LOCAL) {
            return "argument " + script.intOperands[at];
        } else if (script.opcodes[at] == ClientScriptOpCode.PUSH_CONSTANT_INT) {
            return "component " + script.intOperands[at];
        } else {
            return null;
        }
    }

    /**
     * The sprites a script sets on the components it is given, in the order of its instructions:
     * each the argument it is set on and the sprite, pushed as a constant just before.
     */
    private static List<Integer[]> graphicsOf(ClientScript script) {
        var graphics = new ArrayList<Integer[]>();
        for (var at = 2; at < script.opcodes.length; at++) {
            if (script.opcodes[at] == IF_SETGRAPHIC
                && script.opcodes[at - 1] == ClientScriptOpCode.PUSH_INT_LOCAL
                && script.opcodes[at - 2] == ClientScriptOpCode.PUSH_CONSTANT_INT) {
                graphics.add(new Integer[] {script.intOperands[at - 1], script.intOperands[at - 2]});
            }
        }
        return graphics;
    }

    private static boolean targets(List<Integer[]> graphics, int... locals) {
        for (var at = 0; at < locals.length; at++) {
            if (graphics.get(at)[0] != locals[at]) {
                return false;
            }
        }
        return true;
    }

    /**
     * The edge and the middle a row's hook gives, where it gives the same edge to both ends.
     */
    private static Integer[] rowOf(WidgetHooks.Calling calling, String hook) {
        var edge = calling.sprite(hook, 1);
        var middle = calling.sprite(hook, 3);
        var otherEdge = calling.sprite(hook, 5);
        return edge != null && middle != null && edge.equals(otherEdge) ? new Integer[] {edge, middle} : null;
    }

    private static Integer first(WidgetHooks.Calling calling, List<String> hooks, int at) {
        for (var hook : hooks) {
            var value = calling.sprite(hook, at);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Each tab of script 4782, as the sprite under the pointer and the plain sprite that a case of
     * its switch keeps in its fourth and fifth locals, before it sets one of them by its third
     * argument. The -1 it keeps in both before the switch is no tab.
     */
    private static List<Integer[]> tabsOf(ClientScript script) {
        var tabs = new ArrayList<Integer[]>();
        for (var at = 3; at < script.opcodes.length; at++) {
            if (script.opcodes[at - 3] == ClientScriptOpCode.PUSH_CONSTANT_INT
                && script.opcodes[at - 2] == ClientScriptOpCode.POP_INT_LOCAL && script.intOperands[at - 2] == 3
                && script.opcodes[at - 1] == ClientScriptOpCode.PUSH_CONSTANT_INT
                && script.opcodes[at] == ClientScriptOpCode.POP_INT_LOCAL && script.intOperands[at] == 4
                && script.intOperands[at - 3] >= 0 && script.intOperands[at - 1] >= 0) {
                tabs.add(new Integer[] {script.intOperands[at - 3], script.intOperands[at - 1]});
            }
        }
        if (tabs.isEmpty()) {
            stop(HOVER_TAB, "keep a tab's sprite under the pointer and its plain sprite in two locals");
        }
        return tabs;
    }

    private static void stop(int script, String shape) {
        System.out.println("script " + script + " no longer has the shape the export reads: it does not " + shape);
        System.exit(1);
    }

    private WidgetReaders() {
        /* empty */
    }
}

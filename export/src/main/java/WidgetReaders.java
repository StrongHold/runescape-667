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

    static void radioButtons(Map<Integer, ClientScript> scripts, WidgetHooks hooks, WidgetSets sets) {
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

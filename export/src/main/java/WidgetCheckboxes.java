import com.jagex.core.constants.ClientScriptOpCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * The checkboxes of the interfaces, each a ticked state and an empty state.
 *
 * Script 4521 builds a checkbox in one state, a box with a box under the pointer and a box held
 * down over it, from the sprites its callers give it: script 4519 gives the ticked state and script
 * 4520 the empty one.
 *
 * Other scripts tick a box by setting one sprite on it where a test holds and another where it
 * does not, in the two arms of the test. Two sprites are taken for a checkbox where they are the
 * same square size, at least three scripts set them that way, they are not a radio button, and the
 * empty one is not the ticked one of another checkbox, which marks them apart from the other marks
 * scripts swap.
 */
final class WidgetCheckboxes {

    private static final int BOX_SCRIPT = 4521;
    private static final int TICKED_SCRIPT = 4519;
    private static final int EMPTY_SCRIPT = 4520;

    /**
     * Script 4521 takes its box, the box under the pointer and the box held down from its second
     * argument.
     */
    private static final int FIRST_BOX = 1;
    private static final List<String> BOX_FIELDS = List.of("box", "hoverBox", "pressedBox");

    private static final int IF_SETGRAPHIC = ClientScriptOpCode.CC_IF_SETGRAPHIC + 1000;

    private static final int CHECKBOX_SCRIPTS = 3;

    private static final Set<Integer> TESTS = Set.of(
        ClientScriptOpCode.BRANCH_IF_TRUE,
        ClientScriptOpCode.BRANCH_IF_FALSE,
        ClientScriptOpCode.BRANCH_EQUALS,
        ClientScriptOpCode.BRANCH_NOT,
        ClientScriptOpCode.BRANCH_LESS_THAN,
        ClientScriptOpCode.BRANCH_GREATER_THAN,
        ClientScriptOpCode.BRANCH_LESS_THAN_OR_EQUALS,
        ClientScriptOpCode.BRANCH_GREATER_THAN_OR_EQUALS
    );

    static List<Map<String, Object>> read(
        Map<Integer, ClientScript> scripts,
        WidgetExport.CallReader calls,
        Set<List<Integer>> radios,
        Map<Integer, int[]> sizes
    ) {
        var checkboxes = new ArrayList<Map<String, Object>>();
        var ticked = boxOf(calls, TICKED_SCRIPT);
        var empty = boxOf(calls, EMPTY_SCRIPT);
        if (ticked != null && empty != null) {
            checkboxes.add(checkboxOf(ticked, empty, List.of(TICKED_SCRIPT, EMPTY_SCRIPT)));
        }

        var pairs = new TreeMap<List<Integer>, TreeSet<Integer>>(java.util.Comparator.comparing(Object::toString));
        for (var script : scripts.entrySet()) {
            for (var pair : eitherOrPairs(script.getValue())) {
                pairs.computeIfAbsent(pair, key -> new TreeSet<>()).add(script.getKey());
            }
        }
        var tickedSprites = new HashSet<Integer>();
        var kept = new ArrayList<Map.Entry<List<Integer>, TreeSet<Integer>>>();
        for (var pair : pairs.entrySet()) {
            var tick = pair.getKey().get(0);
            var blank = pair.getKey().get(1);
            var tickSize = sizes.get(tick);
            var blankSize = sizes.get(blank);
            var square = tickSize != null && blankSize != null && Arrays.equals(tickSize, blankSize) && tickSize[0] == tickSize[1];
            var radio = radios.contains(List.of(tick, blank)) || radios.contains(List.of(blank, tick));
            if (pair.getValue().size() >= CHECKBOX_SCRIPTS && square && !radio) {
                kept.add(pair);
                tickedSprites.add(tick);
            }
        }
        for (var pair : kept) {
            if (!tickedSprites.contains(pair.getKey().get(1))) {
                var box = new LinkedHashMap<String, Object>();
                box.put("box", pair.getKey().get(0));
                var blank = new LinkedHashMap<String, Object>();
                blank.put("box", pair.getKey().get(1));
                checkboxes.add(checkboxOf(box, blank, new ArrayList<>(pair.getValue())));
            }
        }
        return checkboxes;
    }

    /**
     * The state of a checkbox that a script gives script 4521, or null where it gives none as
     * constants.
     */
    private static Map<String, Object> boxOf(WidgetExport.CallReader calls, int caller) {
        var arguments = calls.argumentsOf(BOX_SCRIPT);
        var callers = calls.callersOf(BOX_SCRIPT);
        for (var at = 0; at < arguments.size(); at++) {
            var given = Arrays.asList(arguments.get(at)).subList(FIRST_BOX, FIRST_BOX + BOX_FIELDS.size());
            if (callers.get(at) == caller && !given.contains(null)) {
                var box = new LinkedHashMap<String, Object>();
                for (var field = 0; field < BOX_FIELDS.size(); field++) {
                    box.put(BOX_FIELDS.get(field), given.get(field));
                }
                return box;
            }
        }
        return null;
    }

    private static Map<String, Object> checkboxOf(Map<String, Object> ticked, Map<String, Object> empty, List<Integer> scripts) {
        var checkbox = new LinkedHashMap<String, Object>();
        checkbox.put("ticked", ticked);
        checkbox.put("empty", empty);
        checkbox.put("scripts", scripts);
        return checkbox;
    }

    /**
     * Each pair of sprites a script sets on one component in the two arms of a test: the sprite
     * where the test holds, then the sprite where it does not. A test jumps over the jump that
     * follows it where it holds, so its arm starts after that jump, and the other arm where the
     * jump lands.
     */
    private static List<List<Integer>> eitherOrPairs(ClientScript script) {
        var pairs = new ArrayList<List<Integer>>();
        for (var at = 0; at + 4 < script.opcodes.length; at++) {
            if (TESTS.contains(script.opcodes[at]) && script.intOperands[at] == 1 && script.opcodes[at + 1] == ClientScriptOpCode.BRANCH) {
                var otherArm = at + 1 + script.intOperands[at + 1] + 1;
                var holds = setAt(script, at + 2);
                var fails = setAt(script, otherArm);
                var sameComponent = holds != null && fails != null && holds[0] == fails[0] && holds[1] == fails[1];
                if (sameComponent && holds[2] >= 0 && fails[2] >= 0 && holds[2] != fails[2]) {
                    pairs.add(List.of(holds[2], fails[2]));
                }
            }
        }
        return pairs;
    }

    /**
     * The sprite an arm sets first, as {how the component is pushed, which component, sprite}, or
     * null where the arm does not start by setting a sprite pushed as a constant.
     */
    private static int[] setAt(ClientScript script, int at) {
        if (at < 0 || at + 2 >= script.opcodes.length) {
            return null;
        } else if (script.opcodes[at] == ClientScriptOpCode.PUSH_CONSTANT_INT && script.opcodes[at + 2] == IF_SETGRAPHIC) {
            return new int[] {script.opcodes[at + 1], script.intOperands[at + 1], script.intOperands[at]};
        } else {
            return null;
        }
    }

    private WidgetCheckboxes() {
        /* empty */
    }
}

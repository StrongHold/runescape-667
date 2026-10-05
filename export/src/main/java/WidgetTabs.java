import com.jagex.core.constants.ClientScriptOpCode;
import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The tabs of the interfaces, and the buttons whose sprites a hover script swaps.
 *
 * A hover setter is a script that sets one sprite on the component it is given where its second
 * argument says the pointer is over it, and another where it is not, as script 2462 does for the
 * tabs of the game's frame. A tab is a component that shows the plain sprite of a hover setter,
 * with another component of the same interface at its very place, in a layer of its own, that
 * shows its selected sprite when the game unhides it.
 *
 * A hover plate is a layer of an interface built as a button on a plate, an edge at its left, a
 * middle that stretches and an edge at its right, whose pieces a script that the hover hooks of
 * the interface run sets to other sprites by constant, as scripts 4003, 4004 and 4010 do.
 */
final class WidgetTabs {

    private static final int IF_SETGRAPHIC = ClientScriptOpCode.CC_IF_SETGRAPHIC + 1000;

    private static final Set<String> HOVER_HOOKS = Set.of("onMouseOver", "onMouseLeave");

    /**
     * The components of an interface stand under their interface's number in the high half of
     * their id, and their own number in the low half ({@code Component.id}).
     */
    private static final int INTERFACE_SHIFT = 16;
    private static final int CHILD_MASK = 0xFFFF;

    /**
     * How the client lays a component out: where it is, how it keeps its place, and its size.
     */
    private record Placed(int id, Component component) {
    }

    static void read(File cache, Map<Integer, ClientScript> scripts, WidgetSets tabs, WidgetSets spriteButtons, WidgetSets plateButtons) throws Exception {
        var setters = hoverSetters(scripts);
        for (var setter : setters.entrySet()) {
            spriteButtons.add(Arrays.asList(setter.getValue()[0], setter.getValue()[1], null), setter.getKey(), null);
        }
        var plainToHover = new HashMap<Integer, Integer>();
        for (var setter : setters.values()) {
            plainToHover.put(setter[0], setter[1]);
        }

        var hoverScripts = new HashSet<Integer>();
        var index = Cache.index(cache, Js5Archive.INTERFACES);
        var interfaces = new LinkedHashMap<Integer, List<Placed>>();
        for (var group : Cache.groupsOf(index)) {
            var data = Cache.group(cache, Js5Archive.INTERFACES, group);
            if (data != null) {
                var placed = new ArrayList<Placed>();
                for (var file : Cache.split(data, index, group).entrySet()) {
                    var component = new Component();
                    component.decode(new Packet(file.getValue()));
                    placed.add(new Placed((group << INTERFACE_SHIFT) | file.getKey(), component));
                    hoverScripts.addAll(hoverScriptsOf(component));
                }
                interfaces.put(group, placed);
            }
        }

        var swapped = constantSwaps(scripts, hoverScripts);
        for (var entry : interfaces.entrySet()) {
            addTabs(entry.getKey(), entry.getValue(), plainToHover, tabs);
            addHoverPlates(entry.getKey(), entry.getValue(), swapped, plateButtons);
        }
    }

    /**
     * Each hover setter, by its id, as its plain sprite and the sprite under the pointer.
     */
    private static Map<Integer, Integer[]> hoverSetters(Map<Integer, ClientScript> scripts) {
        var setters = new LinkedHashMap<Integer, Integer[]>();
        for (var entry : scripts.entrySet()) {
            var script = entry.getValue();
            var sprites = new ArrayList<Integer>();
            var others = false;
            for (var at = 2; at < script.opcodes.length; at++) {
                if (script.opcodes[at] == IF_SETGRAPHIC) {
                    var onGiven = script.opcodes[at - 2] == ClientScriptOpCode.PUSH_CONSTANT_INT
                        && script.opcodes[at - 1] == ClientScriptOpCode.PUSH_INT_LOCAL
                        && script.intOperands[at - 1] == 0;
                    if (onGiven) {
                        sprites.add(script.intOperands[at - 2]);
                    } else {
                        others = true;
                    }
                }
            }
            var flagged = script.intArgCount == 2 && script.opcodes.length > 1
                && script.opcodes[0] == ClientScriptOpCode.PUSH_INT_LOCAL && script.intOperands[0] == 1
                && script.opcodes[1] == ClientScriptOpCode.BRANCH_IF_TRUE;
            if (!others && flagged && sprites.size() == 2 && sprites.get(0) >= 0 && sprites.get(1) >= 0) {
                setters.put(entry.getKey(), new Integer[] {sprites.get(1), sprites.get(0)});
            }
        }
        return setters;
    }

    private static Set<Integer> hoverScriptsOf(Component component) throws IllegalAccessException {
        var found = new HashSet<Integer>();
        for (var hook : HOVER_HOOKS) {
            try {
                var arguments = (Object[]) Component.class.getField(hook).get(component);
                if (arguments != null && arguments.length > 0 && arguments[0] instanceof Integer script) {
                    found.add(script);
                }
            } catch (NoSuchFieldException absent) {
                throw new IllegalStateException(absent);
            }
        }
        return found;
    }

    /**
     * The sprites the hover scripts set on each component by constant, by the component's id.
     */
    private static Map<Integer, Set<Integer>> constantSwaps(Map<Integer, ClientScript> scripts, Set<Integer> hoverScripts) {
        var swaps = new HashMap<Integer, Set<Integer>>();
        for (var id : hoverScripts) {
            var script = scripts.get(id);
            if (script != null) {
                for (var at = 2; at < script.opcodes.length; at++) {
                    if (script.opcodes[at] == IF_SETGRAPHIC
                        && script.opcodes[at - 2] == ClientScriptOpCode.PUSH_CONSTANT_INT
                        && script.opcodes[at - 1] == ClientScriptOpCode.PUSH_CONSTANT_INT) {
                        swaps.computeIfAbsent(script.intOperands[at - 1], key -> new HashSet<>()).add(script.intOperands[at - 2]);
                    }
                }
            }
        }
        return swaps;
    }

    /**
     * Adds each tab of an interface: a component that shows a hover setter's plain sprite, with the
     * sprite of the component at its very place in another layer as its selected sprite.
     */
    private static void addTabs(int interfaceId, List<Placed> placed, Map<Integer, Integer> plainToHover, WidgetSets tabs) {
        for (var tab : placed) {
            var hover = plainToHover.get(tab.component().graphic);
            if (hover != null) {
                for (var other : placed) {
                    if (isOverlay(other.component(), tab.component(), hover)) {
                        tabs.add(Arrays.asList(tab.component().graphic, hover, other.component().graphic), null, interfaceId);
                    }
                }
            }
        }
    }

    private static boolean isOverlay(Component other, Component tab, int hover) {
        var shown = other != tab && other.type == Component.TYPE_GRAPHIC && other.graphic >= 0;
        var another = other.graphic != tab.graphic && other.graphic != hover && other.layer != tab.layer;
        var samePlace = other.originalX == tab.originalX && other.originalY == tab.originalY
            && other.originalWidth == tab.originalWidth && other.originalHeight == tab.originalHeight
            && other.reposModeX == tab.reposModeX && other.reposModeY == tab.reposModeY;
        return shown && another && samePlace;
    }

    /**
     * Adds each hover plate of an interface: a layer with an edge at its left, a middle that
     * stretches across and an edge at its right, each of whose pieces a hover script sets to one
     * other sprite.
     */
    private static void addHoverPlates(int interfaceId, List<Placed> placed, Map<Integer, Set<Integer>> swapped, WidgetSets plateButtons) {
        var layers = new LinkedHashMap<Integer, List<Placed>>();
        for (var piece : placed) {
            if (piece.component().type == Component.TYPE_GRAPHIC && piece.component().graphic >= 0) {
                layers.computeIfAbsent(piece.component().layer, layer -> new ArrayList<>()).add(piece);
            }
        }
        for (var pieces : layers.values()) {
            Placed left = null;
            Placed middle = null;
            Placed right = null;
            for (var piece : pieces) {
                var component = piece.component();
                if (component.resizeModeX == 1) {
                    middle = piece;
                } else if (component.reposModeX == 0 && component.originalX == 0) {
                    left = piece;
                } else if (component.reposModeX == 2 && component.originalX == 0) {
                    right = piece;
                }
            }
            if (left != null && middle != null && right != null && left.component().graphic == right.component().graphic) {
                var hoverEdge = otherSprite(swapped, left);
                var hoverMiddle = otherSprite(swapped, middle);
                if (hoverEdge != null && hoverMiddle != null) {
                    plateButtons.add(Arrays.asList(left.component().graphic, middle.component().graphic, hoverEdge, hoverMiddle), null, interfaceId);
                }
            }
        }
    }

    /**
     * The one sprite other than its own that a hover script sets on a component, or null where there
     * is not one.
     */
    private static Integer otherSprite(Map<Integer, Set<Integer>> swapped, Placed piece) {
        var others = new HashSet<>(swapped.getOrDefault(piece.id(), Set.of()));
        others.remove(piece.component().graphic);
        return others.size() == 1 ? others.iterator().next() : null;
    }

    private WidgetTabs() {
        /* empty */
    }
}

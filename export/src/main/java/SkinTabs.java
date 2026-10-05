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
 * with another component of the same interface at its very place, in a layer the client draws
 * after the tab's, that shows its selected sprite: the game's scripts hide each such component but
 * the one over the chosen tab.
 *
 * A hover plate is a layer of an interface built as a button on a plate, an edge at its left, a
 * middle that stretches and an edge at its right, whose pieces a script that the hover hooks of
 * the interface run sets to other sprites by constant, as scripts 4003, 4004 and 4010 do.
 */
final class SkinTabs {

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

    static List<Map<String, Object>> read(File cache, Map<Integer, ClientScript> scripts, SkinSets spriteButtons, SkinSets plateButtons) throws Exception {
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
        var decorations = decorationsBySelected(scripts);
        var tabs = new LinkedHashMap<String, Map<String, Object>>();
        var tabInterfaces = new LinkedHashMap<String, java.util.TreeSet<Integer>>();
        for (var entry : interfaces.entrySet()) {
            addTabs(entry.getKey(), entry.getValue(), plainToHover, decorations, tabs, tabInterfaces);
            addHoverPlates(entry.getKey(), entry.getValue(), swapped, plateButtons);
        }

        var written = new ArrayList<Map<String, Object>>();
        for (var tab : tabs.entrySet()) {
            var entry = new LinkedHashMap<String, Object>(tab.getValue());
            entry.put("interfaces", new ArrayList<>(tabInterfaces.get(tab.getKey())));
            written.add(entry);
        }
        return written;
    }

    /**
     * The sprites a script sets by constant right after it sets a sprite on a component, in the same
     * arm, before its next jump, by the component and the sprite first set: what it decorates a
     * selected tab with, as script 1387 sets a glow and a frame over the tab it selects. A sprite of
     * -1, which another script sets to take the decoration off, is passed over.
     */
    private static Map<List<Integer>, List<int[]>> decorationsBySelected(Map<Integer, ClientScript> scripts) {
        var decorations = new HashMap<List<Integer>, List<int[]>>();
        for (var script : scripts.values()) {
            for (var at = 2; at < script.opcodes.length; at++) {
                var set = constantSetAt(script, at);
                var key = set == null ? null : List.of(set[0], set[1]);
                if (key != null && !decorations.containsKey(key)) {
                    var following = new ArrayList<int[]>();
                    for (var next = at + 1; next < script.opcodes.length && script.opcodes[next] != ClientScriptOpCode.BRANCH; next++) {
                        var decoration = constantSetAt(script, next);
                        if (decoration != null && decoration[1] >= 0) {
                            following.add(decoration);
                        }
                    }
                    if (!following.isEmpty()) {
                        decorations.put(key, following);
                    }
                }
            }
        }
        return decorations;
    }

    /**
     * The component and the sprite an instruction sets where both are pushed as constants, as
     * {component, sprite}, or null where it sets none so.
     */
    private static int[] constantSetAt(ClientScript script, int at) {
        var set = script.opcodes[at] == IF_SETGRAPHIC
            && script.opcodes[at - 2] == ClientScriptOpCode.PUSH_CONSTANT_INT
            && script.opcodes[at - 1] == ClientScriptOpCode.PUSH_CONSTANT_INT;
        return set ? new int[] {script.intOperands[at - 1], script.intOperands[at - 2]} : null;
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
     * sprite of the component over it, at its very place in a layer drawn after it, as its selected
     * sprite, and the sprites a script sets right after it selects the tab as what the selected tab
     * is decorated with.
     */
    private static void addTabs(
        int interfaceId,
        List<Placed> placed,
        Map<Integer, Integer> plainToHover,
        Map<List<Integer>, List<int[]>> decorations,
        Map<String, Map<String, Object>> tabs,
        Map<String, java.util.TreeSet<Integer>> tabInterfaces
    ) {
        var byId = new HashMap<Integer, Component>();
        for (var piece : placed) {
            byId.put(piece.id(), piece.component());
        }
        for (var tab : placed) {
            var hover = plainToHover.get(tab.component().graphic);
            if (hover != null) {
                for (var other : placed) {
                    if (isOverlay(other.component(), tab.component(), hover)) {
                        var written = new LinkedHashMap<String, Object>();
                        written.put("sprite", tab.component().graphic);
                        written.put("hover", hover);
                        written.put("selected", other.component().graphic);
                        var parts = decorationParts(decorations.getOrDefault(List.of(other.id(), other.component().graphic), List.of()), tab.id(), byId);
                        if (!parts.isEmpty()) {
                            written.put("selectedParts", parts);
                        }
                        var key = Json.write(written);
                        tabs.putIfAbsent(key, written);
                        tabInterfaces.computeIfAbsent(key, k -> new java.util.TreeSet<>()).add(interfaceId);
                    }
                }
            }
        }
    }

    /**
     * The sprites a selected tab is decorated with, each where it stands from the tab's corner and
     * its size, laid out through their layers in the game's window, where each is a component of the
     * tab's own interface.
     */
    private static List<Map<String, Object>> decorationParts(List<int[]> decorations, int tabId, Map<Integer, Component> byId) {
        var parts = new ArrayList<Map<String, Object>>();
        var layout = new ComponentLayout(byId);
        var tabBox = layout.boxOf(tabId);
        for (var decoration : decorations) {
            var box = byId.containsKey(decoration[0]) ? layout.boxOf(decoration[0]) : null;
            if (box != null && tabBox != null && decoration[1] >= 0) {
                var part = new LinkedHashMap<String, Object>();
                part.put("sprite", decoration[1]);
                part.put("x", box.x() - tabBox.x());
                part.put("y", box.y() - tabBox.y());
                part.put("width", box.width());
                part.put("height", box.height());
                parts.add(part);
            }
        }
        return parts;
    }


    /**
     * Whether a component covers a tab: it shows another sprite at the tab's very place, in a layer
     * the client draws after the tab's, as the layers of an interface are drawn in the order of
     * their numbers.
     */
    private static boolean isOverlay(Component other, Component tab, int hover) {
        var shown = other != tab && other.type == Component.TYPE_GRAPHIC && other.graphic >= 0;
        var another = other.graphic != tab.graphic && other.graphic != hover && other.layer > tab.layer;
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
    private static void addHoverPlates(int interfaceId, List<Placed> placed, Map<Integer, Set<Integer>> swapped, SkinSets plateButtons) {
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

    private SkinTabs() {
        /* empty */
    }
}

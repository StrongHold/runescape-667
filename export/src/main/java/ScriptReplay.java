import com.jagex.core.constants.ClientScriptOpCode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * A script that builds components, played back for a component of a given size: the components it
 * creates, each as the client would hold it after the script ran. Only the instructions such a
 * script uses are played: constants and locals, adding and subtracting, the size of the component,
 * calls of other scripts with their arguments, and creating components and setting their place,
 * size, sprite, colour, fill, transparency, tiling and flips ({@code ScriptRunner}). The playback
 * stops the task at any other instruction, so a script that no longer has this shape is noticed.
 */
final class ScriptReplay {

    /**
     * A component the script created, as the client holds one: its type, place and size with the
     * modes that say how they follow the component it is in, and how it is drawn.
     */
    static final class Created {
        final int type;
        int x;
        int y;
        int width;
        int height;
        int reposX;
        int reposY;
        int resizeX;
        int resizeY;
        int graphic = -1;
        int colour;
        boolean filled;
        int transparency;
        boolean tiled;
        boolean horizontalFlip;
        boolean verticalFlip;

        Created(int type) {
            this.type = type;
        }
    }

    private final Map<Integer, ClientScript> scripts;
    private final int width;
    private final int height;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<Created> created = new ArrayList<>();

    private ScriptReplay(Map<Integer, ClientScript> scripts, int width, int height) {
        this.scripts = scripts;
        this.width = width;
        this.height = height;
    }

    /**
     * The components a script creates in a component of this size.
     */
    static List<Created> run(Map<Integer, ClientScript> scripts, int script, int width, int height) {
        var replay = new ScriptReplay(scripts, width, height);
        replay.play(script, new int[0]);
        return replay.created;
    }

    private void play(int id, int[] arguments) {
        var script = scripts.get(id);
        if (script == null) {
            stop("script " + id + " is not in the cache");
        }
        var locals = new int[Math.max(script.intVarCount, arguments.length)];
        System.arraycopy(arguments, 0, locals, 0, arguments.length);
        for (var at = 0; at < script.opcodes.length; at++) {
            var opcode = script.opcodes[at];
            var operand = script.intOperands[at];
            switch (opcode) {
                case ClientScriptOpCode.PUSH_CONSTANT_INT -> stack.push(operand);
                case ClientScriptOpCode.PUSH_INT_LOCAL -> stack.push(locals[operand]);
                case ClientScriptOpCode.POP_INT_LOCAL -> locals[operand] = stack.pop();
                case ClientScriptOpCode.ADD -> {
                    var right = stack.pop();
                    stack.push(stack.pop() + right);
                }
                case ClientScriptOpCode.SUB -> {
                    var right = stack.pop();
                    stack.push(stack.pop() - right);
                }
                case ClientScriptOpCode.IF_GETWIDTH -> {
                    stack.pop();
                    stack.push(width);
                }
                case ClientScriptOpCode.IF_GETHEIGHT -> {
                    stack.pop();
                    stack.push(height);
                }
                case ClientScriptOpCode.IF_GETNEXTSUBID -> {
                    stack.pop();
                    stack.push(created.size());
                }
                case ClientScriptOpCode.CC_DELETEALL -> {
                    stack.pop();
                    created.clear();
                }
                case ClientScriptOpCode.CC_CREATE -> {
                    stack.pop();
                    var type = stack.pop();
                    stack.pop();
                    created.add(new Created(type));
                }
                case ClientScriptOpCode.CC_IF_SETPOSITION -> {
                    var component = active(id);
                    component.reposY = stack.pop();
                    component.reposX = stack.pop();
                    component.y = stack.pop();
                    component.x = stack.pop();
                }
                case ClientScriptOpCode.CC_IF_SETSIZE -> {
                    var component = active(id);
                    component.resizeY = stack.pop();
                    component.resizeX = stack.pop();
                    component.height = stack.pop();
                    component.width = stack.pop();
                }
                case ClientScriptOpCode.CC_IF_SETGRAPHIC -> active(id).graphic = stack.pop();
                case ClientScriptOpCode.CC_IF_SETCOLOUR -> active(id).colour = stack.pop();
                case ClientScriptOpCode.CC_IF_SETFILL -> active(id).filled = stack.pop() == 1;
                case ClientScriptOpCode.CC_IF_SETTTRANS -> active(id).transparency = stack.pop();
                case ClientScriptOpCode.CC_IF_SETTILING -> active(id).tiled = stack.pop() == 1;
                case ClientScriptOpCode.CC_IF_SETHFLIP -> active(id).horizontalFlip = stack.pop() == 1;
                case ClientScriptOpCode.CC_IF_SETVFLIP -> active(id).verticalFlip = stack.pop() == 1;
                case ClientScriptOpCode.GOSUB_WITH_PARAMS -> {
                    var callee = scripts.get(operand);
                    if (callee == null) {
                        stop("script " + id + " calls script " + operand + ", which is not in the cache");
                    }
                    var given = new int[callee.intArgCount];
                    for (var argument = given.length - 1; argument >= 0; argument--) {
                        given[argument] = stack.pop();
                    }
                    play(operand, given);
                }
                case ClientScriptOpCode.RETURN -> {
                    return;
                }
                default -> stop("script " + id + " has instruction " + opcode + " at " + at + ", which the playback does not play");
            }
        }
    }

    private Created active(int script) {
        if (created.isEmpty()) {
            stop("script " + script + " sets a component before it creates one");
        }
        return created.get(created.size() - 1);
    }

    private static void stop(String why) {
        System.out.println("a script that builds components no longer has the shape the playback reads: " + why);
        System.exit(1);
    }
}

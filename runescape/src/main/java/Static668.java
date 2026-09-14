import com.jagex.core.stringtools.general.StringTools;
import com.jagex.core.util.SystemTimer;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.keyboard.KeyLog;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public final class Static668 {

    /**
     * Advances the debug console for one frame: runs the next lines of a queued script, applies the
     * mouse wheel to the scrollback, and folds this frame's key events into the current entry.
     */
    @OriginalMember(owner = "client!vca", name = "c", descriptor = "(I)V")
    public static void updateDebugConsole() {
        if (debugconsole.anInt8472 < 102) {
            debugconsole.anInt8472 += 6;
        }
        @Pc(26) int i;
        if (Static523.consoleScriptLine != -1 && Static305.aLong157 < SystemTimer.safetime()) {
            for (i = Static523.consoleScriptLine; i < Static144.aStringArray7.length; i++) {
                if (Static144.aStringArray7[i].startsWith("pause")) {
                    @Pc(40) int pauseSeconds = 5;
                    try {
                        pauseSeconds = Integer.parseInt(Static144.aStringArray7[i].substring(6));
                    } catch (@Pc(49) Exception ignored) {
                        /* empty */
                    }
                    debugconsole.addline("Pausing for " + pauseSeconds + " seconds...");
                    Static523.consoleScriptLine = i + 1;
                    Static305.aLong157 = (long) (pauseSeconds * 1000) + SystemTimer.safetime();
                    return;
                }
                debugconsole.currententry = Static144.aStringArray7[i];
                debugconsole.method3920(false);
            }
            Static523.consoleScriptLine = -1;
        }
        if (Static611.mouseWheelRotation != 0) {
            debugconsole.anInt3471 -= Static611.mouseWheelRotation * 5;
            if (debugconsole.anInt3471 >= debugconsole.lineCount) {
                debugconsole.anInt3471 = debugconsole.lineCount - 1;
            }
            Static611.mouseWheelRotation = 0;
            if (debugconsole.anInt3471 < 0) {
                debugconsole.anInt3471 = 0;
            }
        }
        for (i = 0; i < Static671.anInt10026; i++) {
            @Pc(147) KeyLog event = Static194.AN_KEYBOARD_EVENT_ARRAY_1[i];
            @Pc(151) int keyCode = event.getKeyCode();
            @Pc(155) char keyChar = event.getKeyChar();
            @Pc(159) int modifiers = event.getModifierFlags();
            if (keyCode == 84) {
                debugconsole.method3920(false);
            }
            if (keyCode == 80) {
                debugconsole.method3920(true);
            } else if (keyCode == 66 && (modifiers & 0x4) != 0) {
                if (client.clipboard != null) {
                    @Pc(467) String log = "";
                    for (@Pc(472) int lineIndex = debugconsole.lines.length - 1; lineIndex >= 0; lineIndex--) {
                        if (debugconsole.lines[lineIndex] != null && debugconsole.lines[lineIndex].length() > 0) {
                            log = log + debugconsole.lines[lineIndex] + '\n';
                        }
                    }
                    client.clipboard.setContents(new StringSelection(log), null);
                }
            } else if (keyCode == 67 && (modifiers & 0x4) != 0) {
                if (client.clipboard != null) {
                    try {
                        @Pc(207) Transferable contents = client.clipboard.getContents(null);
                        if (contents != null) {
                            @Pc(214) String pasted = (String) contents.getTransferData(DataFlavor.stringFlavor);
                            if (pasted != null) {
                                @Pc(221) String[] pastedLines = StringTools.split(pasted, '\n');
                                Static363.method6234(pastedLines);
                            }
                        }
                    } catch (@Pc(226) Exception ignored) {
                        /* empty */
                    }
                }
            } else if (keyCode == 85 && debugconsole.currententryLength > 0) {
                debugconsole.currententry = debugconsole.currententry.substring(0, debugconsole.currententryLength - 1) + debugconsole.currententry.substring(debugconsole.currententryLength);
                debugconsole.currententryLength--;
            } else if (keyCode == 101 && debugconsole.currententryLength < debugconsole.currententry.length()) {
                debugconsole.currententry = debugconsole.currententry.substring(0, debugconsole.currententryLength) + debugconsole.currententry.substring(debugconsole.currententryLength + 1);
            } else if (keyCode == 96 && debugconsole.currententryLength > 0) {
                debugconsole.currententryLength--;
            } else if (keyCode == 97 && debugconsole.currententryLength < debugconsole.currententry.length()) {
                debugconsole.currententryLength++;
            } else if (keyCode == 102) {
                debugconsole.currententryLength = 0;
            } else if (keyCode == 103) {
                debugconsole.currententryLength = debugconsole.currententry.length();
            } else if (keyCode == 104 && debugconsole.lines.length > Static625.anInt9472) {
                Static625.anInt9472++;
                Static344.method5046();
                debugconsole.currententryLength = debugconsole.currententry.length();
            } else if (keyCode == 105 && Static625.anInt9472 > 0) {
                Static625.anInt9472--;
                Static344.method5046();
                debugconsole.currententryLength = debugconsole.currententry.length();
            } else if (StringTools.isAlphanumeric(keyChar) || "\\/.:, _-+[]~@".indexOf(keyChar) != -1) {
                debugconsole.currententry = debugconsole.currententry.substring(0, debugconsole.currententryLength) + Static194.AN_KEYBOARD_EVENT_ARRAY_1[i].getKeyChar() + debugconsole.currententry.substring(debugconsole.currententryLength);
                debugconsole.currententryLength++;
            }
        }
        Static216.keyPressCount = 0;
        Static671.anInt10026 = 0;
        InterfaceManager.redrawAll();
    }
}

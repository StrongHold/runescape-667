import rs2.client.loading.screen.LoadingScreen;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.core.util.JagException;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.graphics.FlipException;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.loading.LoadState;

import java.awt.Container;

@OriginalClass("client!uaa")
public final class LoadingScreenRenderer implements Runnable {

    @OriginalMember(owner = "client!uaa", name = "u", descriptor = "Z")
    public boolean fullRepaint;

    @OriginalMember(owner = "client!uaa", name = "o", descriptor = "Z")
    public volatile boolean stopped;

    @OriginalMember(owner = "client!uaa", name = "w", descriptor = "J")
    public long transitionStart;

    @OriginalMember(owner = "client!uaa", name = "e", descriptor = "Lclient!gca;")
    public LoadState state;

    @OriginalMember(owner = "client!uaa", name = "g", descriptor = "J")
    public long lastUpdate;

    @OriginalMember(owner = "client!uaa", name = "h", descriptor = "Ljava/lang/String;")
    public String text;

    @OriginalMember(owner = "client!uaa", name = "x", descriptor = "I")
    public int tick;

    @OriginalMember(owner = "client!uaa", name = "m", descriptor = "I")
    public int percentage;

    @OriginalMember(owner = "client!uaa", name = "n", descriptor = "Lclient!uha;")
    public LoadingScreen currentScreen = new AwtLoadingScreen();

    @OriginalMember(owner = "client!uaa", name = "k", descriptor = "Lclient!uha;")
    public LoadingScreen lastScreen = null;

    @OriginalMember(owner = "client!uaa", name = "a", descriptor = "(Z)Lclient!gca;")
    public LoadState getState() {
        return this.state;
    }

    @OriginalMember(owner = "client!uaa", name = "b", descriptor = "(B)J")
    public long getLastUpdate() {
        return this.lastUpdate;
    }

    @OriginalMember(owner = "client!uaa", name = "a", descriptor = "(B)V")
    public synchronized void repaint() {
        this.fullRepaint = true;
    }

    @OriginalMember(owner = "client!uaa", name = "c", descriptor = "(I)V")
    public void complete() {
        this.stopped = true;
    }

    @OriginalMember(owner = "client!uaa", name = "a", descriptor = "(BLclient!uha;)V")
    public synchronized void change(@OriginalArg(1) LoadingScreen screen) {
        this.lastScreen = this.currentScreen;
        this.currentScreen = screen;
        this.transitionStart = SystemTimer.safetime();
    }

    @OriginalMember(owner = "client!uaa", name = "a", descriptor = "(ILjava/lang/String;Lclient!gca;IJ)V")
    public synchronized void updateState(@OriginalArg(0) int percentage, @OriginalArg(1) String text, @OriginalArg(2) LoadState state, @OriginalArg(4) long lastUpdate) {
        this.percentage = percentage;
        this.state = state;
        this.lastUpdate = lastUpdate;
        this.text = text;
    }

    @OriginalMember(owner = "client!uaa", name = "d", descriptor = "(I)I")
    public int nextPercentage() {
        if (this.state == null) {
            return 0;
        }
        @Pc(22) int step = this.state.getStep();
        if (this.state.updatePercentage && this.state.endPercentage > this.percentage) {
            return this.percentage + 1;
        } else if (step >= 0 && step < Loading.states.length - 1) {
            return this.percentage == this.state.startPercentage ? this.state.endPercentage : this.state.startPercentage;
        } else {
            return 100;
        }
    }

    @OriginalMember(owner = "client!uaa", name = "a", descriptor = "(I)Z")
    public synchronized boolean canChangeScreen() {
        return this.currentScreen.method8463(this.transitionStart);
    }

    @OriginalMember(owner = "client!uaa", name = "d", descriptor = "(B)Ljava/lang/String;")
    public String getText() {
        return this.text;
    }

    @OriginalMember(owner = "client!uaa", name = "c", descriptor = "(B)I")
    public int getPercentage() {
        return this.percentage;
    }

    @OriginalMember(owner = "client!uaa", name = "run", descriptor = "()V")
    @Override
    public void run() {
        while (!this.stopped) {
            @Pc(8) long start = SystemTimer.safetime();

            synchronized (this) {
                try {
                    this.tick++;

                    if (this.currentScreen instanceof AwtLoadingScreen) {
                        this.currentScreen.render(this.fullRepaint);
                    } else {
                        @Pc(25) long now = SystemTimer.safetime();

                        if (Toolkit.active == null || this.lastScreen == null || this.lastScreen.getFadeDuration() == 0 || this.transitionStart < now - (long) this.lastScreen.getFadeDuration()) {
                            if (this.lastScreen != null) {
                                this.fullRepaint = true;
                                this.lastScreen.cleanup();
                                this.lastScreen = null;
                            }

                            if (this.fullRepaint) {
                                Static288.repaintMargins();

                                if (Toolkit.active != null) {
                                    Toolkit.active.GA(0);
                                }
                            }

                            this.currentScreen.render(this.fullRepaint || Toolkit.active != null && Toolkit.active.method8001());
                        } else {
                            @Pc(72) int alpha = (int) ((now - this.transitionStart) * 255L / (long) this.lastScreen.getFadeDuration());
                            @Pc(77) int previousAlpha = 255 - alpha;
                            Static288.repaintMargins();
                            @Pc(85) int previousTint = (previousAlpha << 24) | 0xFFFFFF;
                            @Pc(91) int tint = (alpha << 24) | 0xFFFFFF;
                            Toolkit.active.GA(0x0);

                            @Pc(100) Sprite sprite = Toolkit.active.createSprite(GameShell.canvasWid, GameShell.canvasHei, true);

                            Toolkit.active.method8002(sprite);
                            this.lastScreen.render(true);

                            Toolkit.active.restoreSurface();
                            sprite.render(0, 0, 0, previousTint, 1);

                            Toolkit.active.method8002(sprite);
                            Toolkit.active.GA(0x0);
                            this.currentScreen.render(true);

                            Toolkit.active.restoreSurface();
                            sprite.render(0, 0, 0, tint, 1);
                        }
                        try {
                            if (Toolkit.active != null && !(this.currentScreen instanceof AwtLoadingScreen)) {
                                Toolkit.active.flip();
                            }
                        } catch (@Pc(205) FlipException ex) {
                            JagException.sendTrace(ex, ex.getMessage() + " (Recovered) " + client.aClient1.getErrorTrace());
                            Static32.setToolkit(ToolkitType.JAVA, true);
                        }
                    }

                    @Pc(245) Container topContainer;
                    if (GameShell.frame != null) {
                        topContainer = GameShell.frame;
                    } else if (GameShell.loaderApplet == null) {
                        topContainer = GameShell.instance;
                    } else {
                        topContainer = GameShell.loaderApplet;
                    }

                    topContainer.getSize();
                    topContainer.getSize();

                    if (topContainer == GameShell.frame) {
                        GameShell.frame.getInsets();
                    }

                    this.fullRepaint = false;

                    if (Toolkit.active != null && !(this.currentScreen instanceof AwtLoadingScreen) && this.state.getStep() < LoadState.SHOW_LOGIN_WINDOW.getStep()) {
                        Static712.method9329((byte) 11);
                    }
                } catch (@Pc(292) Exception ignored) {
                    continue;
                }
            }

            @Pc(304) long end = SystemTimer.safetime();
            @Pc(312) int delay = (int) (start + 20L - end);
            if (delay > 0) {
                TimeUtils.sleep(delay);
            }
        }
    }

    @OriginalMember(owner = "client!uaa", name = "b", descriptor = "(I)I")
    public int getTick() {
        return this.tick;
    }
}

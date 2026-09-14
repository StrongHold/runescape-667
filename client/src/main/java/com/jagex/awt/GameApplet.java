package com.jagex.awt;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.Color;
import java.awt.Dimension;

/**
 * The applet the game renders into. It is fixed at the smallest viewport the game supports, which
 * the window around it then adopts as its own minimum size once packed.
 */
public final class GameApplet {

    private static final Dimension VIEWPORT_SIZE = new Dimension(765, 503);

    public static Applet create(AppletStub stub) {
        var applet = new Applet();
        applet.setStub(stub);
        applet.setBackground(Color.BLACK);

        applet.setMinimumSize(viewportSize());
        applet.setPreferredSize(viewportSize());
        applet.setSize(viewportSize());

        return applet;
    }

    /**
     * AWT keeps the {@link Dimension} it is handed rather than copying it, so each setter gets one
     * of its own to own.
     */
    private static Dimension viewportSize() {
        return new Dimension(VIEWPORT_SIZE);
    }

    private GameApplet() {
        /* empty */
    }
}

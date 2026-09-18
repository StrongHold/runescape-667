package com.jagex.awt;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

/**
 * The applet the game renders into. It is fixed at the smallest viewport the game supports, which
 * the window around it then adopts as its own minimum size once packed.
 */
public final class GameApplet {

    private static final Dimension VIEWPORT_SIZE = new Dimension(765, 503);

    public static Applet create(AppletStub stub) {
        var applet = new Viewport();
        applet.setStub(stub);
        applet.setBackground(Color.BLACK);

        applet.setMinimumSize(viewportSize());
        applet.setPreferredSize(viewportSize());
        applet.setSize(viewportSize());

        return applet;
    }

    /**
     * A viewport that lays itself out again whenever the game swaps what it draws into.
     *
     * The game throws away its canvas and builds another whenever it changes renderer, which it
     * does on its own account: opening the world map is one such moment. A component added to a
     * container that is already on screen is given nothing to draw on until the container is laid
     * out again, so a canvas swapped in without that is never painted and never sees the mouse or
     * the keyboard. The window stays up and the game keeps running behind a picture that stopped.
     */
    private static final class Viewport extends Applet {

        @Override
        protected void addImpl(Component child, Object constraints, int index) {
            super.addImpl(child, constraints, index);
            validate();
        }

        @Override
        public void remove(Component child) {
            super.remove(child);
            validate();
        }
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

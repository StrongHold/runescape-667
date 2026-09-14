package com.jagex.awt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.util.List;

public final class GameFrame {

    private static final Dimension INITIAL_SIZE = new Dimension(1024, 768);

    private final Frame frame;

    public GameFrame(String title, List<Image> icons) {
        frame = new Frame(title);
        frame.setIconImages(icons);
        frame.addWindowListener(new ExitOnCloseListener());
    }

    public void open(Component content) {
        frame.add(content);
        frame.pack();

        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.toFront();

        growToInitialSize();
    }

    /**
     * Pins the minimum size to the packed layout before growing the window, so the user cannot
     * shrink it below the size the content needs.
     */
    private void growToInitialSize() {
        frame.setMinimumSize(frame.getSize());
        frame.setPreferredSize(initialSize());
        frame.setSize(initialSize());
    }

    private static Dimension initialSize() {
        return new Dimension(INITIAL_SIZE);
    }
}

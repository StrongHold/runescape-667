package com.jagex.awt;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ExitOnCloseListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent event) {
        System.exit(0);
    }
}

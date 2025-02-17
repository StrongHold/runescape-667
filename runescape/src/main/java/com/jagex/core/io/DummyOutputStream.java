package com.jagex.core.io;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

import java.io.IOException;
import java.io.OutputStream;

@OriginalClass("client!naa")
public final class DummyOutputStream extends OutputStream {

    @OriginalMember(owner = "client!naa", name = "write", descriptor = "(I)V")
    @Override
    public void write(@OriginalArg(0) int arg0) throws IOException {
        throw new IOException();
    }
}

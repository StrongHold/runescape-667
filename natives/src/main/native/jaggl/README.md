# jaggl

The OpenGL binding. A hundred and eighty natives on `jaggl.OpenGL` and `jaggl.MapBuffer`.

It is not the hardware renderer. The renderer is Java and is already in this repository, about
sixteen and a half thousand lines across `GlToolkit`, `Class93`, `Class406`, `GlTexture`,
`GlUnderwaterPass` and the rest. This carries their calls across to the driver and carries the
answers back, and that is all it does.

## What it stands in for

`libjaggl.dylib`, which the game's file store holds for `macos/x86`, `macos/x86_64`,
`macos/universal` and `macos/ppc`.

`GlToolkit` reaches neither `hw3d` nor `jagex3.graphics2.hw.NativeInterface`. It needs this and
`jaclib` and nothing else, which is why the OpenGL path runs here while the Direct3D path does not.

## How far along it is

All hundred and eighty, written two ways.

A hundred and fifty three are generated at build time from the client's own JNI headers, by
`generateOpenGlBinding`. They take their arguments straight to the call of the same name, so the
only thing that varies between them is the types, and a generator makes a better job of that than
a hand does. The generator writes what it could not handle to
`build/generated/jaggl-outstanding.txt`.

The twenty nine on that list are in `jaggl.m`: the ones carrying a string or an array with an
offset into it, and the ones that get a context, a surface or a pbuffer, which are Cocoa work
rather than a call forwarded.

## How it is checked

    ./gradlew :natives:verifyOpenGlBinding

Both bindings are driven through the same script and everything either carries is compared. Forty
six answers, all identical.

Every switch is enabled, disabled and read back. Every whole number and fraction is set and read
back. Arrays are written into the middle of a larger one, so an offset that is ignored shows as the
wrong cells changing rather than as nothing at all. Two matrices are built and compared, and a
texture is uploaded and bound. Each answer is also held to what was asked for, so a binding driven
on its own still fails on the spot rather than recording a wrong value.

That is a narrower question than the renderer is asked and it is close to the whole question here.
A binding has no opinions. Both reach the same driver on the same machine, so nothing either does
can differ except the carrying: an argument put in the wrong place, an array read from the wrong
offset, a value widened the wrong way.

The shipped binding has to be thinned to its x86_64 slice and its JNI import pointed at the shim,
as the memory library does, and both sides need `JAWTSHIM_WAIT_FOR_VIEW` set. The shipped one
builds its context out of `NSOpenGLContext` and gives it a view, and the call that takes a view
makes the context current on whichever thread runs it. The shim hands that call to the main thread
without waiting, which is what the client needs and which leaves a harness drawing into a context
that is current on another thread. With that set the shim waits and then takes the context back
onto the thread that asked for it.

## Where it does not agree, and why

What each binding carries is identical. What sort of context each builds is not:

| | shipped | this one |
|---|---|---|
| sample buffers | 1 | 0 |
| samples | 2 | 0 |
| depth bits | 32 | 24 |
| stencil bits | 0 | 8 |

The client asks for eight bits a channel, twenty four of depth, no stencil and no antialiasing.
The shipped binding asks OpenGL for this:

```
73 5 55 1 56 [0] 8 24 12 24 [0]
```

Reading it: accelerated, double buffered, one sample buffer, and a sample count. The sample count
is the antialiasing the client asked for, which is zero, and a zero ends an attribute list. The
eight bits of colour and twenty four of depth that follow are written down and never read. Asking
for a sample buffer with no count then gets two samples from the driver.

So on this system the client has always drawn multisampled with antialiasing turned off, into
whatever depth buffer the system chose rather than the one it asked for. This one asks for what
the client asked for and gets it.

The pixels differ for that reason and no other, which is why the picture drawn at the end of the
probe is reported beside the context rather than compared. Two triangles over a cleared background
come out the same either way except at their edges, which one context softens and the other does
not.

Matching the shipped binding here would mean writing an attribute list that discards its own
request. It is not done, and nothing about it is accidental.

## Antialiasing is not honoured, and what it would take

Turning antialiasing up does nothing here. The client asks for none, two samples
a pixel or four, and this library draws every one of them with one.

The reason is the drawable. The client is given a framebuffer of this library's
own rather than the one the window carries, because the picture is shown through
a layer. How finely a pixel is drawn is decided by the buffers hung on the
framebuffer that is bound, so choosing a pixel format with four samples changes
nothing while those buffers are plain.

Hanging buffers of four samples on it instead is not the answer, and was tried:

    samples=4 setSurface=true GL_SAMPLES=4 middlePixel=0 error=1282

`1282` is `GL_INVALID_OPERATION`. Reading pixels back out of a framebuffer object
of more than one sample a pixel is not allowed, the client reads its framebuffer
back, and every read failing leaves the screen black. The shipped library does
not meet this because it draws into the window's own drawable, where the window
system resolves a read for it.

So doing it properly means every way the client reads that framebuffer going
through a plain buffer first: the two `glReadPixels`, the `glCopyTexImage` and
`glCopyTexSubImage` calls, and anything else that takes a picture out of it.
Each would have to bring the samples down and read from the result instead, and
each is a pass-through today. That is the work, and it is not small.

Worth knowing while it is undone: the shipped library answers two samples when
asked for none, four when asked for four, and two when asked for two. So the
antialiasing setting does something there and nothing here, and the setting can
never be turned fully off there.

None of this touches the software toolkit. The client never hands it the setting,
and `AntialiasingMode.validate` forces the setting to zero whenever the toolkit
in use is not a hardware one.

## Faults in the original

The shipped macOS binding does not have all hundred and eighty. It exports a hundred and seventy
six, and one of those is under the wrong name:

| declared in Java | in the shipped library |
|---|---|
| `glDeleteProgramARB` | exported as `glDeleteProgram` |
| `glGetTexImagei` | missing |
| `glGetTexImageub` | missing |
| `glStencilFunc` | missing |
| `glStencilOp` | missing |

`glDeleteProgramARB` is called from `GlToolkit` and `GlxToolkit`, and `glGetTexImagei` from
`GlTexture2D` and `GlRectangleTexture`, so two of the five are on paths the client reaches and
throw `UnsatisfiedLinkError` against the shipped library. This one has all hundred and eighty.

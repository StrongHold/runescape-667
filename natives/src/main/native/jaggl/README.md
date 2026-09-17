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

Not against the shipped binding. Against what goes in coming back out.

That is a weaker claim than the renderer's and it is closer to the whole claim than it sounds. A
binding has no opinions. Both it and the shipped one reach the same driver on the same machine, so
nothing it does can be right or wrong except the carrying: an argument put in the wrong place, an
array read from the wrong offset, a value widened the wrong way. Setting a piece of state and
reading it back asks exactly that question, and it asks it of the call itself rather than of
anything behind it.

Fifty answers. Every switch enabled and disabled and read back, every whole number and fraction
set and read back, arrays written into the middle of a larger one so that an ignored offset shows
as the wrong cells changing, two matrices built and compared, a texture uploaded and bound, and a
triangle and a textured triangle drawn and read out of the framebuffer, because a vertex and a
colour set nothing that can be asked for.

Each answer is held to what was asked for, so the probe fails on the spot rather than recording a
wrong value. The answers are also written to `build/answers/binding-ours.txt`, which is what a
comparison would read when one becomes possible.

## Why there is no comparison yet

The shipped binding cannot be driven here. It loads, it takes a surface from the shim, and it
returns a handle from `init` and `true` from `setSurface`. No context becomes current:

```
[jawtshim] JAWT_GetAWT 0x00010004
[jawtshim] made a 256x256 view for the hardware toolkit
[jawtshim] getDrawingSurfaceInfo 256x256
init = 140301592318384
version after init = null
setSurface = true
version after setSurface = null
error = 0
```

`glGetString` answering null is a GL call made with nothing current. Every value after it is zero,
so a comparison would be comparing our answers against fifty nothings and calling it a difference.
Tried on the main thread and on the event thread, and through `init` and through `prepareSurface`
and `setSurface` separately. The shim's own log shows it hands over the view it was asked for.

This one builds its context with CGL. The shipped one builds an `NSOpenGLContext` and hands it a
view, which is why the shim already moves `setView:`, `update` and `clearDrawable` to the main
thread for it. What is not yet known is whether the context is never created, or created and never
made current. That is the next thing to find out, and it is the same shape of problem as the
`JAWT_VERSION_1_3` one the shim was written for.

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

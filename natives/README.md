# The client's native libraries

The client loads six native libraries out of its own file store and calls into them for drawing,
sound, video and memory. They ship as prebuilt binaries for Windows, macOS and Linux, they cannot
be rebuilt from anything in this repository, and each new operating system release is a chance for
one of them to stop loading. This directory is where they are being replaced.

Owning them also unfreezes the deobfuscation. The rule that no class declaring a native may be
renamed or repackaged exists only because these binaries resolve their fields and methods by
literal name. Nothing in `a`, `h`, `i`, `j`, `ja`, `n`, `oa`, `t`, `wa`, `xa`, `jaggl`, `jagdx`,
`jagtheora`, `jaclib` or `jagex3` can be touched until the binary that binds to it is ours.

## What there is to do

| library | what it does | natives | written |
|---|---|---|---|
| `sw3d` | the software renderer | 171 | all of them |
| `jaggl` | the OpenGL binding | 180 | 29 |
| `jagdx` | the Direct3D binding | 97 | none |
| `jagtheora` | Theora video and Vorbis sound | 67 | none |
| `jaclib` | memory the client manages itself | 19 | 16 |
| `jagex3` | `jagmisc`, and the hardware toolkit's own interface | 17 | none |

The store holds them for `windows/x86`, `windows/x86_64`, `macos/x86`, `macos/x86_64`,
`macos/universal`, `macos/ppc`, `linux/x86` and `linux/x86_64`. Everything written here is built
as a universal binary for arm64 and x86_64; the other platforms are a later problem, and a Windows
machine is where `jagdx` will have to be finished.

## How a library is known to be right

The shipped binary is the specification, so the way to know is to drive both and compare. For a
renderer that means drawing the same scene through each and comparing the pictures pixel by pixel,
and for everything else it means asking both the same questions and comparing the answers.

    ./gradlew :natives:verifyNatives

Whatever cannot be shown this way does not go in, however sure the reasoning. There is a comment
in `sw3d/render.c` on `Java_a_Z` marking a change that was written, checked against the client's
own renderer, and then taken out again because no picture could judge it.

This cannot run on CI yet. It measures against the shipped binaries, which belong to whoever ran
the client rather than to this repository, and the renderer needs macOS and an x86_64 virtual
machine. The game data the checks need is small enough to keep: `natives/models` holds the eight
models the scenes are drawn with. The binaries are the part still to solve.

`natives/outstanding.txt` records how far out each unfinished scene is allowed to be. A scene that
grows past its line fails the build. Without it an unfinished scene could drift from a few pixels
out to half a picture and nothing would say so.

## Where each one stands

Each library has a README of its own saying what it replaces, how far along it is, where it
differs from the shipped binary on purpose, and what is known to be wrong with the original. See
`sw3d/README.md`.

`jawtshim` is not one of the six and is not on its way out. It lets the shipped toolkits obtain a
drawing surface on a current JDK, which they cannot do by themselves, so without it neither could
be driven and nothing could be compared against them. The client never loads it. It lives as long
as the checks do, which is as long as the shipped binaries are the specification. See
`jawtshim/README.md`.

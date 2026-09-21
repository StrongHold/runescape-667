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
| `jaggl` | the OpenGL binding | 180 | all of them |
| `jagdx` | the Direct3D binding | 97 | none |
| `jagtheora` | Theora video and Vorbis sound | 67 | none |
| `jaclib` | memory the client manages itself | 19 | 16, which is all of them |
| `jagmisc` | a clock, the size of memory, a ping | 6 | all of them |
| `hw3d` | what the hardware toolkit hands its models to | 11 | none |

`jagmisc` and `hw3d` are counted apart although both are declared under `jagex3`. They are two
unrelated libraries that share a package and nothing else, and they are checked in two different
ways.

`jaggl`'s hundred and eighty are written in two ways. A hundred and fifty three are generated
from the client's own JNI headers, because they pass their arguments straight to the call of the
same name and nothing but the types varies. The twenty nine the generator cannot write are in
`jaggl/jaggl.m`: the ones that carry a string or an array with an offset, and the ones that get a
context, a surface or a pbuffer. The generator lists what it left in
`build/generated/jaggl-outstanding.txt`.

`jaclib`'s sixteen are all of its nineteen. The shipped macOS library exports the same sixteen and
not the other three, which are Direct3D and cannot be reached on any platform that has none. See
`jaclib/README.md`.

The store holds these for `windows/x86`, `windows/x86_64`, `macos/x86`, `macos/x86_64`,
`macos/universal`, `macos/ppc`, `linux/x86` and `linux/x86_64`, but not evenly. Two of them are
not held for every platform, which decides how each can be checked:

- `jagmisc` is held for Windows alone, so there is no copy of it here to measure against. It is
  measured against the machine instead. See `jagmisc/README.md`.
- `jagtheora` is not held for any platform. The archive names 36 groups and every one of them is
  accounted for, and none is jagtheora, so the client has never played a video from this cache.
  Writing it would be writing against nothing at all.

Everything written here is built as a universal binary for arm64 and x86_64; the other platforms
are a later problem, and a Windows machine is where `jagdx` will have to be finished.

## How a library is known to be right

The shipped binary is the specification, so the way to know is to drive both and compare. For a
renderer that means drawing the same scene through each and comparing the pictures pixel by pixel,
and for everything else it means asking both the same questions and comparing the answers.

    ./gradlew :natives:verifyNatives

Whatever cannot be shown this way does not go in, however sure the reasoning. There is a comment
in `sw3d/render.c` on `Java_a_Z` marking a change that was written, checked against the client's
own renderer, and then taken out again because no picture could judge it.

That check needs the shipped binaries, which belong to whoever ran the client rather than to this
repository, so it only runs where the client has been run. What the shipped toolkit drew is kept
instead, one frame per scene under `natives/goldens`, filed under the scene's own name so that a
change to one says in the file name which scene changed.

    ./gradlew :natives:updateGoldens     # here, where the shipped toolkit is
    ./gradlew :natives:verifyGoldens     # anywhere, and on CI

Both checks hold the same record, so a scene allowed to be a certain distance out is allowed the
same distance either way. The game data the checks need is small enough to keep as well:
`natives/models` holds the eight models the scenes are drawn with.

The renderer used to need an x86_64 virtual machine, because it divided by the processor's
approximate reciprocal and no two processors answer that alike. It now reads those answers from a
table it carries, so it draws the same picture on either architecture and the kept frames are
worth keeping. It still needs macOS, because the surface it hands its pixels to is written against
this platform and nothing else.

`natives/outstanding.txt` records how far out each unfinished scene is allowed to be. A scene that
grows past its line fails the build. Without it an unfinished scene could drift from a few pixels
out to half a picture and nothing would say so.

## Where each one stands

Each library has a README of its own saying what it replaces, how far along it is, where it
differs from the shipped binary on purpose, and what is known to be wrong with the original. See
`sw3d/README.md`, `jaggl/README.md`, `jaclib/README.md` and `jagmisc/README.md`.

`jawtshim` is not one of the six and is not on its way out. It lets the shipped toolkits obtain a
drawing surface on a current JDK, which they cannot do by themselves, so without it neither could
be driven and nothing could be compared against them. The client never loads it. It lives as long
as the checks do, which is as long as the shipped binaries are the specification. See
`jawtshim/README.md`.

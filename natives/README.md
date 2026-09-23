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

## Libraries and tools

This subproject holds two kinds of code. The libraries replace what the client loads. The tools
drive the libraries and the shipped binaries, and compare the two. The client never loads a tool.

The libraries and most of the tools are in this one subproject for now. Each library will get a
subproject of its own later, and the tools will then move apart from them. The cache reader and
the shared command line code have already moved out, into `cache` and `cli`.

### The libraries

The build turns these into the libraries the client loads, in place of the shipped binaries.

| directory | replaces |
|---|---|
| `src/main/native/sw3d` | the software renderer |
| `src/main/native/jaggl` | the OpenGL binding |
| `src/main/native/jaclib` | the memory the client manages itself |
| `src/main/native/jagmisc` | the clock, the size of memory and the ping |

### The native tools

These are C because each one must sit inside a process beside a shipped binary, or must ask the
processor a question that Java cannot ask.

| directory | what it does |
|---|---|
| `src/main/native/jawtshim` | gives the shipped toolkits a drawing surface on a current JDK. They cannot get one by themselves, so nothing could drive them without it. See `jawtshim/README.md`. |
| `src/main/native/watch` | watches the shipped software toolkit call its own routines, and writes down what each call gets. See "Watching the shipped toolkit" below. |
| `src/main/native/reciprocal` | reads the answers this processor gives for its approximate reciprocals into the table that `sw3d` carries. Run it by hand only when the table must be made again. |

These live as long as the checks do. The checks live as long as the shipped binaries are the
specification.

### The Java tools

Everything under `src/main/java` is a tool, and a Gradle task runs each one.

| files | what they do |
|---|---|
| `FrameCapture`, `Scene`, `SceneModel`, `KeepModels`, `Hand*`, `*Mesh`, `GradientSprite`, `IndexedGlyph` | draw the scenes through a software toolkit and keep the frames |
| `FrameCheck`, `GoldenFrames` | compare the frames of the two toolkits, and keep the shipped frames under `goldens` |
| `*Probe`, `AnswerCheck` | ask a library a fixed set of questions, and compare the answers of two libraries |
| `GlSamples`, `MemoryHeap`, `Jagmisc`, `ToolkitLifetime`, `ToolkitSkeleton`, `CanvasHandover`, `SpriteLiftCheck` | check one behaviour of one library |
| `WatchShipped`, `ShippedRoutine`, `Trace`, `TraceCheck` | watch the shipped toolkit and line what it did up against ours |
| `*Args`, `Watchdog` | read the arguments of a tool, and stop a tool that hangs |

Two subprojects beside this one hold what these tools share with others:

- `cache` reads the game's cache. The scenes are drawn with models it reads, and it has tools of its
  own that print what the cache holds, such as `:cache:listCacheLibraries` and
  `:cache:describeModel`. `:natives:keepModels` copies the models the scenes use out of the
  cache into `natives/models`, and `SceneModel` names them.
- `cli` holds `CommandLine`, `Arguments` and `Whole`, which every tool uses to read its arguments.

## Watching the shipped toolkit

A difference in a frame says where the two toolkits disagree, but not why. To find out why, watch
the shipped toolkit draw the same scene and compare the values it works with to ours.

    ./gradlew :natives:compareTraces -Pscene=FoggedHorizon \
        -Pwatch='body:2,0,0,0,1,3,0,0;span:1,0,0,0,1,0,0,0,1' -Pignore=shares -Pat='258,309'

The task does three things:

1. It draws the scene through the shipped toolkit with the watcher inserted. The watcher records
   each call to the routines that `-Pwatch` names.
2. It draws the scene through our toolkit with its trace switched on. The trace records every
   textured pixel and every run of a row.
3. It lines the two traces up and prints which value differs, first on the pixels where the two
   frames differ, then on every pixel.

Name a routine by its family and its template parameters, as the disassembly prints them. The
families are `span` (`RenderHLine`), `body` (`HLineIterationBody`), `half` (`RenderHalfTriangle`)
and `triangle` (`RenderTriangle`). A textured span does its per-pixel work in a `body` routine, so
watch the body to compare pixels. An untextured span does its per-pixel work inline, so watch the
`span` to compare runs. The tool finds where the shipped colour buffer starts from the pixels of a
`body` when one is watched, and from runs that agree on their depth and count when none is. List the routines of the shipped
library with `nm` and `c++filt`.

`-Pignore` removes a value that a face does not use, for example the shares of a face that is not
blended. `-Pat` prints pixels in full. `-Pdump` prints the first calls to each watched routine that
is not a `body`, as raw words. Use it to learn where a routine keeps a value before the tool reads
that value by name.

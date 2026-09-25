# sw3d

The software renderer. This is a reimplementation of `libsw3d.dylib`, the toolkit the client
falls back to when it cannot use the hardware ones, written so that the client no longer depends
on a 2011 binary that each macOS release may stop loading.

It is the toolkit the client actually runs on here: the hardware paths cannot be driven on this
machine, so everything drawn on screen comes through this code.

## What it stands in for

`libsw3d.dylib` is downloaded from the game's own file store and is not in this repository. Its
symbols are the contract: 171 native methods across `a`, `h`, `i`, `j`, `ja`, `n`, `oa`, `t`, `wa`
and `xa`, all of which have a definition here. Nothing is left as a stub.

The original is C++ built from `sw3d/native/`, which the symbol names still carry: `ground`,
`genericground`, `complexground`, `model`, `texturecache`, `Rasterizer_n`. Where a decision here
looks arbitrary it is usually because the original made it, and the comment nearby says so.

## How it is checked

Every claim of correctness comes from drawing the same scene through both toolkits and comparing
the pictures pixel by pixel. Run:

    ./gradlew :natives:verifyNatives

It answers for the pictures, for the answers the probes ask that never reach a picture, and for
the lifetime of a handle, the skeleton, the memory library and the sprite lift. All eighty
scenes are identical to the shipped toolkit, pixel for pixel, and so are 64479 matrix answers,
1185 projection answers and 431 model answers.

No scene is outstanding, so `natives/outstanding.txt` holds no lines. It is kept for the next
scene that does not match yet. A scene listed there that grows past its line fails the build. A
scene that comes in is reported with the number to bring the line down to. Without that record an
unfinished scene could drift from a few pixels out to half the picture and nothing would notice.

This is not part of `check` and cannot run on CI: it measures against the shipped library, which
belongs to whoever ran the client, and it needs macOS and an x86_64 virtual machine. The eight
models the scenes are drawn with are kept in `natives/models`, so nothing but that library is
wanted from the cache.

## What the checks reach

A picture that matches says nothing about code that no scene runs. To see which lines the checks
reach, build the toolkit with coverage and run them:

    ./gradlew -Psw3dCoverage :natives:captureOwnFrames :natives:captureOwnMatrices \
        :natives:captureOwnPoints :natives:captureOwnModels :natives:verifyOwnToolkitLifetime
    ./gradlew :natives:reportToolkitCoverage

`-Psw3dCoverage` works with any task that loads the toolkit, the client included. Each run adds a
profile under `natives/build/coverage/raw`. Delete that directory to start again. The report is
written as HTML under `natives/build/coverage/html`.

The checks reach 83% of the lines. A client that was played for two and a half hours with
coverage reached 68%. Every line that the client reached and that can change a pixel is also
reached by a scene. The lines that only the client reaches free memory, ask how much memory is in
use, resize the window, or find what is under the mouse.

## What is not finished

**A tile drawn from above comes out in perspective.** The client names a distance in ortho mode,
and every corner should be laid down through it. `Java_a_Z` reads the distance and drops it. The
change is written out in the comment there and was withdrawn. The shipped toolkit refuses to draw
a tile from above at all for any patch this harness can build, so no picture can show that the
change is right.

## Faults in the original

Each of these either takes the virtual machine with them or changes from one run to the next, so
none can be measured against:

- `i.p` asked for way 2 divides by the height of the model's top, so a model whose top is at
  nought stops the virtual machine. The same way reads the ground under every vertex without
  asking whether the vertex is over the ground.
- Building model 32421 walks off the end of something in `Java_i_R`.
- A face shaded black takes its colour out of memory the toolkit never wrote, so it comes out a
  different colour on each run. When every face of a model is black it comes out black, which is
  what the Java toolkit draws, so that is what this draws.

## Reading the source

`render.c` is the rasteriser and the ground and model draws. `model.c` is what a model holds and
what may be done to it. `ground.c` is what a tile holds, how it is lit and how a shadow falls on
it. `texture.c`, `sprite.c`, `font.c`, `shape.c` and `colour.c` are what their names say.
`surface.m` is the only file that talks to the window server, and it asks JAWT for the modern
version rather than the 1.3 the original asked for, which is why the client no longer needs the
shim that made the original load.

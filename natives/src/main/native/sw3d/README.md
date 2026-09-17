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
the lifetime of a handle, the skeleton, the memory library and the sprite lift. As of the last
change: 116 frames identical, 64479 matrix answers, 1185 projection answers and 431 model
answers identical.

Thirteen scenes do not match and are listed in `natives/outstanding.txt` with how far out each is
allowed to be. A scene that grows past its line fails the build; a scene that comes in is reported
with the number to bring the line down to. Without that record an unfinished scene could drift
from a few pixels out to half the picture and nothing would notice.

This is not part of `check` and cannot run on CI: it measures against the shipped library, which
belongs to whoever ran the client, and it needs macOS and an x86_64 virtual machine. The eight
models the scenes are drawn with are kept in `natives/models`, so nothing but that library is
wanted from the cache.

## What is not finished

Three things, all of them known rather than suspected:

- **A tile drawn from above comes out in perspective.** The client names a distance in ortho mode
  and every corner should be laid down through it. `Java_a_Z` reads the distance and drops it. The
  change is written out in the comment there and was withdrawn, because the shipped toolkit
  refuses to draw a tile from above at all for any patch this harness can build, so no picture can
  show the change is right.
- **A particle wearing a texture is drawn smaller than it should be.** What goes in the square is
  close; how big the client makes the square is not worked out.
- **Thirteen scenes differ.** Four of those differ on purpose, described below. Two share one
  cause: a texture laid smaller than the tile it covers, read over and over across it. The rest
  are a shade out at the dark end of the range, which is how a span is rounded rather than what it
  was given.

## Where it differs on purpose

The shipped toolkit is wrong in two places and this one follows the client's own renderer instead.
Both are recorded as outstanding scenes so the difference stays measured rather than forgotten.

- **A floor opening is not given a black lid.** The client gives a face no colour where the floor
  opens onto the level below, and the shipped toolkit paints such a face black, which puts a black
  square over every stairwell. Nothing of it is drawn here.
- **The map is lit from the sun alone and keeps each colour's own hue.** The shipped toolkit draws
  the map in the colour the ground is drawn in as the world is seen, carried through what a
  texture does to a colour and through the colour the sun shines in. A texture carries every
  colour towards the same grey, so a map drawn that way comes out in one colour however many kinds
  of ground it covers.

## Two faults in the original

Both take the virtual machine with them, so neither can be measured against:

- `ground.p` asked for way 2 walks off the end of something in `model::hillchange`.
- Building model 32421 walks off the end of something in `Java_i_R`.

## Reading the source

`render.c` is the rasteriser and the ground and model draws. `model.c` is what a model holds and
what may be done to it. `ground.c` is what a tile holds, how it is lit and how a shadow falls on
it. `texture.c`, `sprite.c`, `font.c`, `shape.c` and `colour.c` are what their names say.
`surface.m` is the only file that talks to the window server, and it asks JAWT for the modern
version rather than the 1.3 the original asked for, which is why the client no longer needs the
shim that made the original load.

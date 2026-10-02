# viewer

Shows the models, NPCs and map squares that the `export` module writes, in a browser.

    ./gradlew :viewer:dev

This serves the viewer and opens it. It lists every `.glb` under `export/build/models`,
`export/build/npcs` and `export/build/squares`, read afresh on each reload, so a file exported
while the viewer is open shows up when the page is reloaded. Any other `.glb` can be dropped on the
page.

The model can be turned with the mouse and zoomed with the wheel. One square of the grid is one
tile, up is +y and north is -z, as the exporter writes them. A file with animations, such as an
NPC, plays its first one, and a list chooses another. A map square's animations are the
sequences of its locations, which the game plays all at once, so a square starts with every
animation playing, and the list can single one out. A location the exporter marks as starting
at a random frame is started at one, as the game does, so the flags of one kind do not move in
step.

A map square is 64 tiles across, so it is seen from high over its south edge, and the sun's shadow
is fitted to it with a larger shadow map than a single model gets. Its own ground takes the
shadows in place of the plane a model stands on. The grid of a square has a line for every tile
and is drawn at the square's lowest point, and as the ground is seldom flat it starts hidden. A
square is known by what its root node carries, so one dropped on the page is shown the same way.
Faces that are blended over what is behind them, such as water, cast no shadow.

    ./gradlew :viewer:validate

This runs the Khronos glTF validator over every file under `export/build`. The validator has no
command line of its own, so `etc/validate.ts` drives it. It counts every issue by its code and
fails on any error or warning.

The viewer is a Vite project in TypeScript, with three.js installed from npm. Gradle runs it
through the node plugin, using the Node version that `mise.toml` pins, and installs its packages
with `npm ci` from the lockfile. `./gradlew :viewer:build` runs the TypeScript compiler over it and builds it. The
page is wired together in `src/main.ts` from a module for each part of it: the stage, the
lighting, the scenery, the framing, the animation player, the file list, the backdrop and the
drop target.

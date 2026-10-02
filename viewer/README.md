# viewer

Shows the models and NPCs that the `export` module writes, in a browser.

    ./gradlew :viewer:dev

This serves the viewer and opens it. It lists every `.glb` under `export/build/models` and
`export/build/npcs`, read afresh on each reload, so a file exported while the viewer is open shows
up when the page is reloaded. Any other `.glb` can be dropped on the page.

The model can be turned with the mouse and zoomed with the wheel. One square of the grid is one
tile, up is +y and north is -z, as the exporter writes them. A file with animations, such as an
NPC, plays its first one, and a list chooses another.

The viewer is a Vite project in TypeScript, with three.js installed from npm. Gradle runs it
through the node plugin, using the Node version that `mise.toml` pins, and installs its packages
with `npm ci` from the lockfile. `./gradlew :viewer:build` checks the types and builds it.

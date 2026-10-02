# export

Writes models out of the game's cache in a form that other engines can import. A model is
written as binary glTF (`.glb`), which Godot 4, Blender, three.js and most other tools read as
it is.

    ./gradlew :export:exportModel --args="--model 32421"
    ./gradlew :export:exportModel --args="--model 8 --out /tmp/hood.glb"
    ./gradlew :export:exportModel --args="--model 8 --cache /path/to/another/cache"

A model is named by its group in the models archive. Without `--out` it is written to
`export/build/models/<model>.glb`, and a relative `--out` is taken from the `export` directory,
because that is where Gradle runs the tool. The cache is read from where the client keeps it
unless `--cache` names another.

The tool prints how many faces it wrote and how many it left out, and why.


## How a model is built

The model is built the way the client builds one it is about to draw. The mesh is read with the
client's own reader, an old mesh is scaled up as the client scales it, and the software toolkit
builds the model from it with textures turned on. The toolkit needs no window, and it is given the
client's own texture source, so every textured face gets the texture coordinates the client
works out for it.

The client's world is x east, y down and z north, in 1/512ths of a tile. The model is turned half
a turn about x, so y is up and north is along -z, and one tile is one metre. That turn keeps the
model the same way round, so the faces keep their order of corners, and the faces the client
draws are the front faces in glTF too.

Every corner of every face is a vertex of its own, so each face keeps its own colour exactly. A
face's colour comes from the client's palette and is written as a vertex colour in linear light.
A textured face is tinted the way the client tints it, and its texture is drawn by the client's
texture source and kept in the file as a PNG. Faces are grouped into one primitive for each
texture and way of blending, so a face that is drawn through what is behind it, or a texture with
holes in it, gets a material that blends or cuts out. Normals are the client's own: a smooth face
takes the normals of the faces it meets at each corner, and a flat face takes its own.

Faces that the client never draws are left out: a face hidden at a join, a face the rasteriser
smears instead of drawing, a face whose texture says that its faces are skipped, and a face that
a billboard hides. The billboards themselves, particles and moving textures are not written.


## Looking at a model

`viewer/index.html` is a page that shows a `.glb` file. Open it in a browser, then choose a file
or drop one on the page. It loads three.js from a CDN, so it needs a network connection. One square
of its grid is one tile.

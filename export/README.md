# export

Writes models, NPCs and map squares out of the game's cache in a form that other engines can
import. Each one is written as binary glTF (`.glb`), which Godot 4, Blender, three.js and most
other tools read as it is.

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

Two face corners share a vertex only where they agree on the client's vertex, the normal, the
colour, the opacity and the texture coordinate, so each face keeps its own colour exactly. A
face's colour comes from the client's palette and is written as a vertex colour in linear light.
A textured face is tinted the way the client tints it, and its texture is drawn by the client's
texture source and kept in the file as a PNG. Faces are grouped into one primitive for each
texture and way of blending, so a face that is drawn through what is behind it, or a texture with
holes in it, gets a material that blends or cuts out. Normals are the client's own: a smooth face
takes the normals of the faces it meets at each corner, and a flat face takes its own.

Faces that the client never draws are left out: a face hidden at a join, a face the rasteriser
smears instead of drawing, a face whose texture says that its faces are skipped, and a face that
a billboard hides. The billboards themselves, particles and moving textures are not written.


## Writing an NPC

    ./gradlew :export:exportNpc --args="--npc 9"
    ./gradlew :export:exportNpc --args="--npc 81 --out /tmp/cow.glb"

An NPC is named by its id. Without `--out` it is written to `export/build/npcs/<npc>.glb`, and
`--out` and `--cache` work as they do for a model. The tool prints the NPC's name, how many
vertices and faces it has, and each animation it wrote with how many frames it has and how long
each frame is shown. An NPC that takes the look of another NPC by a variable is refused, and the
tool names the NPCs to write instead.

The NPC is built by the client's own `NPCType.getModel`, on type lists read from the cache and
with the same software toolkit as a model. That method reads each mesh the NPC is made of, moves
each one as its base animation set says, merges them, swaps the NPC's colours and textures, and
scales and poses the result. The model is then written as a model is, so everything above about
coordinates, colours, textures and faces holds for an NPC too. The node carries the NPC's id,
name, size and base animation set in its `extras`.

The base animation set names the sequences the NPC stands, idles, turns, walks, runs and crawls
with. Every frame of each of them is posed by the client's own animation code: the sequence is
given to `getModel` as the NPC's movement animator, held at the start of the frame, and the
vertices are read back from the model it returns. Each distinct frame becomes one morph target,
which holds how far each corner has moved from the model with no sequence playing, so frames that
several sequences share are written once. The targets are named after the frameset and frame
they come from.

Each sequence becomes one animation, named after what the set uses it for and the sequence's id,
such as `stand 808` or `walk 819`. A sequence used for two things, such as one sequence for both
ways of turning on the spot, is written once for each. The animation sets the weight of one
target at a time, and shows each frame for as long as the client does: the client moves an
animation on by one cycle every 20 ms, and a sequence gives each frame a number of cycles. A
frame of no cycles is never shown, and is left out. Its `extras` hold the role, the sequence id,
whether the client tweens it, and, for a sequence that loops over only its last frames, the time
the loop starts at.

Some of this is not what the client does. The animation jumps from frame to frame, where the
client tweens a sequence that asks for it, moving each part a little further towards the next
frame every cycle. Most stand and walk sequences ask for that, so they move more smoothly in the
client. The normals are those of the model with no sequence playing, because the software
toolkit lights a model once, before it poses it, and never turns the normals as the model moves.
The hardware toolkits do turn them with the parts when a sequence asks them to, and that is not
written.

A frame can also change the colour or alpha of faces, or move a billboard, and only the movement
of the vertices is written. A sequence is written in full and loops as a whole, where the client
plays the frames before a sequence's loop once, stops a sequence after its greatest number of
loops, and picks between idle sequences at random by their weights. The NPC's head model, the
sounds a sequence plays, its particles and its billboards are not written, and nor are the
action sequences an NPC plays when the game tells it to, such as an attack.


## Writing a map square

    ./gradlew :export:exportSquare --args="--x 50 --z 50"
    ./gradlew :export:exportSquare --args="--x 52 --z 47 --out /tmp/desert.glb"
    ./gradlew :export:exportSquare --args="--x 50 --z 50 --no-locations"

A square is 64 tiles by 64, named by where it is in squares: square 50_50 holds tiles 3200,3200
to 3263,3263, which is Lumbridge. Without `--out` it is written to
`export/build/squares/<x>_<z>.glb`, and `--out` and `--cache` work as they do for a model. The
locations of a square are locked with a key, which is read from `--keys`, or else as the `cache`
module's census reads it. A square with no key that opens it is written with its ground alone, and
the tool says why. `--no-locations` writes the ground alone on purpose.

The square is built the way the client builds the world around the player, by the client's own
code. The client never builds one square on its own: the colour of a tile at the edge of a square
is smoothed with the squares beside it, and the heights of its corners are theirs too. So the
square is built as the middle of a region of three squares by three, and only the middle one is
written. The tiles of each square are read with `Terrain.decodeMapSquare`, and the toolkit is
given a ground for each level by `Terrain.createGrounds`. The square's locations are placed with
`MapRegion.loadLocations`, and then `Terrain.load` smooths the underlays across their neighbours,
blends the overlays into them, and hands every tile to the ground with its shape cut, its colours
and its textures. Everything runs on the software toolkit, with the options of a player on high
detail: ground blending, textures, ground decorations and high water detail on, and every
location placed whatever level the player stands on.

The ground the toolkit is left with holds each tile as the rasteriser draws it, as triangles with
a height, a colour, a texture and a texture size at each corner, and that is what is written. The
ground of each level that has tiles is a node of its own, named for the level. The ground is
turned and scaled as a model is, with the square's south west corner at the origin and the
client's heights kept, so a level of ground lies where the client draws it and a location placed
on it stands on it. Tiles meet corner to corner, and a corner that two tiles share is one vertex.

The client lights the ground as it builds it, from the slope of each corner and the toolkit's sun,
and that light is turned up to full here, so the colours written are the ground's own, as a
model's are. Each corner is given a normal from the same slope the client lights it by, and a
point within a tile blends the normals of its corners as the client blends their light. The
texture coordinates are the client's too: where a corner is in the world over the size the floor
type gives its texture, so a texture runs on from tile to tile, and from one square to the next.

A face the rasteriser skips because its first corner has no colour is left out. Where the corners
of a face name different textures, the rasteriser blends them across the face, each weighted by
how near its corner a pixel is. glTF has no way to say that, so such a face is written as layers:
the first corner's texture, and over it each other texture, blended in by an alpha that is 1 at
the corners that name it and 0 at the others. Where two textures meet on a face that is exactly
what the rasteriser does. Where three meet, the last layer also covers part of the second, which
is close but not exact. A face of several textures where one of them is water, which blends by its
own alpha, is drawn with its first corner's texture. On high water detail the toolkit makes water
see-through, and it is written as a face that blends by the alpha the toolkit gives it.

Where the region has a world under its water, the client reads it as a region of its own and
builds it beneath the land, against the land's heights. Its ground is written as the node
`underwater bed`, and its locations, which the client reads without a key, with the rest.

Each location is read back from the tiles the client placed it on, as a wall, a corner's second
wall, a wall decoration, a ground decoration, or a location that stands on its tiles. Its model is
built by the method the client builds it with when it places it, `LocType.modelAndShadow`, which
swaps its colours and textures, mirrors, turns, scales and moves it as its type says, and bends it
to fit the ground under it where its type asks for that. It is placed where the client draws it,
and a wall decoration is moved off its wall as the client moves it. Each location is a node named
after the location and its id, such as `Oak 38739`, with its id, shape, turn, level and what the
client keeps it as in its `extras`. The locations of each level hang from a node of their own.
Each distinct model is written as one mesh that every node of it wears, so a tree planted many
times costs one mesh. A location bent to fit the ground is the same model only where the ground
under it is the same shape, so on a slope each is a mesh of its own.

Some of this is not what the client draws. A location that the client animates is written in the
model it has while nothing plays, and a location that takes the look of another by a variable
takes the one it has with every variable at 0, which is how the client stands before the server
sends any. The particles, billboards and sounds of a location are not written, and nor are the
NPCs and items the server puts on the square. The light the client bakes into the ground and the
shadows locations cast on it are left out, as the ground is lit where it is shown. So are the
square's sun, fog, point lights and sky box, the water's moving textures, and the way the client
darkens what it sees through water by its depth. Every roof is written, where the client hides
those above the player. A bridge keeps the level its tiles are given in the map, where the
client draws it with the level below.

The tool prints how many tiles and faces each level has, how many faces are written as layers or
left out and why, and how many locations it placed and how many distinct meshes they wear.


## Looking at a model

The `viewer` subproject shows everything written here, map squares too. See its README.

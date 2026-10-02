# export

Writes models and NPCs out of the game's cache in a form that other engines can import. Each
one is written as binary glTF (`.glb`), which Godot 4, Blender, three.js and most other tools
read as it is.

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


## Looking at a model

The `viewer` subproject shows everything written here. See its README.

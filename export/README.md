# export

Writes models, NPCs, map squares, sprites, fonts, the mini menu's style and the hit splats out of the game's cache
in a form that other engines can import. Every file is a standard text format where one exists, so that it can be read
and compared as text. A model, an NPC or a map square is written as glTF, a `.gltf` JSON file with
its vertex data in a `.bin` file beside it, which the JSON names by a relative path. Godot 4,
Blender, three.js and most other tools read it as it is. Only the vertex data and the images
(PNG) are binary, because no text format holds them. The sprites the client draws its
interfaces with are written as one PNG for each frame, its fonts as BDF text files, and the mini
menu's style and the hit splats as JSON, all described below.

    ./gradlew :export:exportModel --args="--model 32421"
    ./gradlew :export:exportModel --args="--model 8 --out /tmp/hood.gltf"
    ./gradlew :export:exportModel --args="--model 8 --cache /path/to/another/cache"

A model is named by its group in the models archive. Without `--out` it is written to
`export/build/models/<model>.gltf` with `<model>.bin`, and a relative `--out` is taken from the
`export` directory, because that is where Gradle runs the tool. The cache is read from where the
client keeps it unless `--cache` names another.

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
face's colour is the colour the GL toolkit gives its vertices before its lights fall on them
(`GlModel.shadeRgba`): the face's HSL colour with its lightness scaled by the model's ambient,
64 plus the type's own, out of 128, and then taken through the client's palette, whose gamma of
0.7 comes after the scaling, so the result is not the palette colour dimmed. It is written as a
vertex colour in linear light, with the face's opacity, 255 less its alpha out of 255, as the GL toolkit writes it for every face; the toolkit multiplies a texture's own alpha by it. A textured face is tinted as the GL toolkit tints it: that colour
pulled towards a grey of the ambient alone by the texture's `alpha`, out of 256, and brightened
by its `brightness`, which the material's `extras` carry with its `effectType`, `effectParam1` and
`effectParam2`, for an engine that lights a texture itself or draws the texture's effect. Most textures have an alpha of 0 and keep the face's colour. A
textured face refers to its texture in the shared texture library below. Faces are grouped into one primitive for each texture and way of
blending, so a face that is drawn through what is behind it, or a texture with holes in it, gets
a material that blends or cuts out. The faces are written in the order the GL toolkit draws them
(`GlModel`): it sorts them once, as it builds the model, and draws them in that order with depth
writes on. The opaque faces come first, then the see-through faces (a face with an alpha, or one
whose texture blends or cuts out) by their draw priority, and within those by the effect of their
texture, by texture, a face of no texture first, and last in the model's own order. A blended face
drawn before a blended face behind it hides that face, so an engine draws a model's blended faces
in the order written. A blended primitive holds one run of faces of its texture in that order, and
where the order comes back to a texture after another, the run starts a primitive of its own, as
the toolkit draws a range of faces for each texture it comes to. Normals are the client's own: a smooth face takes the normals
of the faces it meets at each corner, and a flat face takes its own. They are written unit, as
glTF asks, and each vertex also carries `_SHADE`, a float: how strongly the sun lights it, out
of 1. The GL toolkit never normalises a normal (`GlModel`): a smooth corner's is the sum of
the normals of the faces at the vertex, each 256 long, times 3 over the model's contrast (768
plus the type's own) and the count of faces summed, and a flat face's is its own, 256 long,
times 2 over the contrast. So a vertex whose faces face away from each other, as the two sides
of a blade of grass do, sums to almost nothing and stays in the ambient, and `_SHADE` is the
length of that normal, which an engine multiplies the unit normal by before the sun's dot
product. The ground's vertices have no `_SHADE`: the GL ground's normals are unit.


## The texture library

    ./gradlew :export:exportTextures
    ./gradlew :export:exportModel --args="--model 8 --textures /path/to/textures"

The library also holds what the GL toolkit's water effect draws with, under `water/`:
`ripple.png`, the sixteen 128 by 128 frames of rippling noise the client bakes
(`GlRippleNoiseTexture`), one below the other with the luminance in each colour channel and the
alpha as the client's, both of them the noise times three over 32, so at most 23 of 255. A water
texture is one whose `effectType` is 4, 8 or 9. On a player's GL client with high water detail,
whose toolkit supports the water plane, the surface is drawn in the normal pass by the fixed
function water effect (`FixedFunctionWaterEffect`): the texture is never bound, and in its place
the ripple of the moment, at a quarter of the texture coordinate, is added to the lit vertex
colour and the sum doubled (`GL_RGB_SCALE` 2), with the
vertex's alpha, which the GL ground writes as opaque, times the frame's alpha, so the surface
is nearly see-through. Where the GL driver has 3D textures (`GL_EXT_texture3D`, which the
client turns off only for some old ATI drivers), the sixteen frames are the slices of one volume
(`GlVolumeTexture`), filtered linearly and repeating on every axis, and the third coordinate is the
part of four seconds gone (`lastTickMillis % 4000 / 4000`), so each frame blends smoothly into the
next and the last into the first. Without 3D textures the client binds the frame of the moment
alone, a new one every quarter second, which looks choppy; an importer should take the volume.
A second unit adds an alpha that fades the surface to opaque with eye
depth, from the fog's start to a fog range further, where the fog for the water starts a range
and a half before the far plane and ends a range before it (`GlToolkit.adjustFog`). What shows
through is the bed, drawn before it in the underwater pass, described with the ground below.

Under `particle/`, `emitters.json` and `effectors.json` hold the client's particle emitter and
effector types, each list indexed by the type's id with null where the cache has none: every
public field of the client's `ParticleEmitterType` and `ParticleEffectorType` by its name, with
the values the client derives after decoding, such as the colour ranges, fade steps and
durations, so an engine can run the client's own emitter and particle arithmetic
(`ParticleEmitter`, `MovingParticle`) without reading the cache.

Under `light/`, `flicker.json` holds the 2048 values of noise the client's lights flicker by
(`EnvironmentLight.generateNoise` at a persistence of 0.4), out of 4096. A light whose flicker
pattern is 3 reads it at its phase; the other patterns are a sine, a sawtooth, a square and a
triangle, and need no table.

`metrics.json` holds the metrics of every texture, a list indexed by the texture's id with null
where the cache has none: every public field of the client's `TextureMetrics` by its name, as the
client holds it (a byte such as `alpha` or `brightness` is signed, so -1 is 255), but
`unusedFlag`, which no Java code of the client reads and which it hands only to the native
toolkit, and whether the
texture source can draw the texture, as `available`. An engine that builds a type's model from the
model library reads from it how each texture blends, how it tints its faces and whether it skips
them, for a texture a type retextures a face to as for any other. Every export writes it where the
library lacks it.

Every file written here refers to its textures by a relative path, such as `../textures/128.png`,
and carries no copy of them. The textures live in one directory, `export/build/textures` unless
`--textures` names another, as one PNG for each texture id. An engine then loads each texture
once, however many models, NPCs and map squares use it, and a texture can be replaced by a better one
by replacing one file.

Each texture is drawn by the client's own texture source, at 128 texels a side or 64 for a small
one, with the gamma the client draws with, and the alpha the client's rasteriser reads from it:
its own alpha where it blends, a hole where it is cut out, and opaque otherwise. A texture that
the client slides over time is drawn still. `exportTextures` writes every texture the cache
holds, and every other export writes any texture it refers to that the library lacks. A texture
that is already in the library is left as it is, so a replaced texture survives a re-export.

Faces that the client never draws are left out: a face hidden at a join, a face the rasteriser
smears instead of drawing, a face whose texture says that its faces are skipped, and a face that
a billboard hides. The billboards themselves, particles and moving textures are not written.

When the player turns textures off, the client draws a model's textured face in the face's own
colour, and builds the ground with no textures, so that a tile shows the colour that tints its
texture. A model's textured primitive carries the face colour as `COLOR_1`, next to the
`COLOR_0` that tints the texture; the ground's `COLOR_0` serves both. Not every texture goes:
water and others the client marks stay on, and a textured material says which in its `extras`,
as `disableable`. The same `extras` give the texture's `colourOp`, which is how the GL client
combines a texel with the lit vertex colour: 0 multiplies them, 1 shows the texel alone, 2
interpolates, 3 adds them and 4 takes a dot product.


## The model library

`exportModel` writes a model into the model library, `export/build/models`, as one file of its
own, and every map square writes there each model that a location it places names, unless the
library holds it already. A type names its models by id, as the client's types do, so a model
that many types use is held once, and an engine builds a type's model from its models as the
client does (`LocType.model`, `NPCType.getModel`): it merges them, mirrors, recolours,
retextures, tints, lights and scales the result. The file's `asset.extras.version` is the
version of its format, 1 now.

The model is built as above, with the ambient of 64 and the contrast of 768 most models are built
with, and its standard attributes (`POSITION`, `NORMAL`, `COLOR_0`, `TEXCOORD_0` for a textured
primitive) show it as the client draws it then, so a tool shows it as it is. The attributes whose
names start with an underscore are the client's own. Each but `_HSL` is a float, which holds a
whole number exactly, as glTF asks that each element of a vertex attribute start on four bytes:

- `_HSL`: the face's colour as the client holds it, which a type recolours and tints, and which
  the palette turns into a colour under the type's ambient, as for `COLOR_0` above. It is four
  unsigned bytes, as the ground writes it: the hue of 64, the saturation of 8, the lightness of
  128, and a spare.
- `_ALPHA`: the face's alpha, 0 opaque and 255 invisible.
- `_SHADING`: 0 smooth, 1 flat, 2 hidden, 3 black, or another the client has no case for.
- `_FACE_LABEL`: the face's label, which the colour and alpha transforms of a frame act on, or -1.
- `_PRIORITY`: the face's draw priority: its own where the model gives each face one, else the one
  the model gives all its faces, as a merge fills them in. The GL toolkit sorts the faces of the
  model a type builds as above, the see-through faces by priority first, so an engine sorts the
  faces of the merged model by it. Within the faces of one texture, a primitive keeps the model's
  own order.
- `_VERTEX`: the client's vertex, which a merge joins with the vertices of the other models at
  the same position, as the client merges meshes (`Mesh(Mesh[], int)`).

The client works out a model's normals once a type has merged, mirrored, turned and scaled it,
from every face, and finds the pivot of a frame from every vertex, drawn or not. So the file holds
every face and every vertex. The faces the client never draws whatever the type, one hidden at a
join that no frame can show, one smeared, one of a shading the client has no case for, and one a
billboard hides, are in a primitive of their own whose material, `hidden`, is wholly see-through,
and a vertex no face uses is a point of a primitive of points in that material. A face whose
texture skips its faces is drawn by some types, as a type can retexture it, so it is not hidden.
`NORMAL` is for a tool alone: an engine works out the normals and the light on each vertex from
the positions of the model it builds, as the client does (`JavaModel.calculateNormals`).

Each vertex is bound wholly to a joint for its label, a node named `label <n>`, with the first
joint, `unlabelled`, for the vertices of no label, so a sequence from the sequence library moves
the model by its labels. The node's `extras` carry `maxVertex`, how many of the client's vertices
come before those that only a texture space or a particle names, the client's `minY` and `maxY`
over the vertices before it (`JavaModel.calculateBounds`), `textureSpaceVertices`, the vertices
its texture spaces of the first kind name, in the order the client's merge adds them, and the
model's `emitters`, `effectors` and `billboards`, as a location's shape does. A merge joins the
vertices each model's faces name, then those its emitters and effectors name, and those come
before `maxVertex`; once every model's are in, it joins the texture spaces' vertices, after it.


## The sequence library

Every map square writes into `export/build/sequences` each sequence that a location it places
plays, as one glTF file a sequence, unless the library holds it already. The file holds a node for
each label the sequence's frames move, named `label <n>` as a model's joints are, and one
animation that carries the extension `RS_client_frames`:

```json
{
  "base": {
    "labels": [[0, 1], [2, 0, 3], [2, 0, 3]],
    "shadowed": [false, false, false],
    "types": [0, 2, 2]
  },
  "keys": [
    {
      "cycles": 6,
      "transforms": [[0, 0, -236, 0, -1, 0]]
    },
    {
      "cycles": 6,
      "transforms": [[0, 0, -236, 0, -1, 0], [1, 0, 0, 128, -1, 0], [2, 0, 0, 112, -1, 0]]
    }
  ],
  "loopOffset": 8,
  "sequence": 3511,
  "tweened": true,
  "version": 1
}
```

(the first two of the eight keys of sequence 3511)

That is what the client plays. `keys` are the frames it shows, for one cycle or more, each with
its transforms as the client reads them, six numbers each: the group of the base, x, y and z, the
pivot group it applies first or -1, and its tween bits, where 1 means the client does not tween
into the transform and 2 that it does not tween out of it. `base` gives the kind of transform of
each group (0 a pivot, 1 a move, 2 a turn, 3 a scale, 5 an alpha, 7 a colour), the labels it
names, and whether it also moves an entity's spot shadow. `tweened` says whether the client
tweens the sequence, and `loopOffset` how many frames from the end it loops back to, -1 for
none. An engine replays the frames on the model, as the client does.

The animation's own channels move each label's node by steps, one key a frame, for a tool that
knows nothing of the extension. A frame turns and scales a label about the centre of the vertices
it names in the posed model, so no node's transform is right for every model: the channels are
worked out on the model the sequence is first written for, and are exact for that model alone. A
sequence that moves no vertex has one channel that holds its first node still, as glTF asks every
animation for a channel. A map square's description names both libraries by `models` and
`sequences`, relative to it, as it names `locs`.


## Writing an NPC

    ./gradlew :export:exportNpc --args="--npc 9"
    ./gradlew :export:exportNpc --args="--npc 81 --baked /tmp/cow.gltf"

An NPC is named by its id. It is written to `export/build/npcs/<npc>.json`, unless `--npcs`
names another directory, with its base animation set in the set library beside it as
`bas/<id>.json`, the models it names (its body's and its head's) in the model library and the
sequences its set names in the sequence library, each written where its library lacks it, so a
set or a model that many NPCs share is held once; `--cache` works as it does for a model. An NPC that takes the look of
another NPC by a variable is refused, and the tool names the NPCs to write instead.

An engine builds the NPC as the client's `NPCType.getModel` does: each model moved by the type's
`translations`, then turned and moved as the base animation set wears it
(the set's `wornTransformations`, a move along x, y and z and a turn about x, y and z in eighths of
the client's units, the turn about z first, then x, then y), all merged where the type names more
than one, recoloured, retextured and tinted, lit under the type's `ambient` plus 64 and `diffusion`
plus 850, posed by the set's sequences, and scaled by `scaleH` across and `scaleV` up once posed.

The JSON file holds every field of the type under the name the client gives it, so that nothing
the client decodes is lost: its `npc` id, `name`, `size` in tiles and base animation set `bas`;
whether the mouse can pick it, `interactive`; its five `ops`, the options its data sets on the
mini menu in the client's order, with null for an empty slot (the client's type list adds a sixth,
Examine, to every type, and the data cannot change it, so it is not written); and how the client
picks it (`NPCEntity.picked`): `pickSizeShift`, by which it grows the picking cylinder and the
model's box, and `quickPick`, which is 1 for a pick by the box on the screen alone, 0 for a pick by
the model's triangles, and -1 for the default, the box for an NPC one tile across and the triangles
for a larger one, which a `pickSizeShift` above 0 also makes the box. An id the type leaves unset
is -1, a list it leaves unset is empty, and its `params` are an object keyed by the parameter's id.
The `models`, `headModels`, `recolours` and `retextures` as pairs of the value in the mesh and the
value it becomes, `recolourPalette`, `translations`, `scaleH`, `scaleV`, `ambient`, `diffusion` and
`tint` (hue, saturation, lightness and scale) are what the model is built from. The type names its base animation set by id, `bas`, and the set's
file holds every field of the set under the names the client gives them.

`--baked` also writes the NPC's model as the client builds it into a glTF file of its own, with its
head beside it as `<file>.head.gltf`, the reference an engine's own building is checked against.
What follows describes what the client builds, which that file holds and an engine builds.

Where the type casts a shadow (`hasShadow`), the file holds a second mesh beside the NPC's, on a
root node named `spot shadow` whose `extras` say `spotShadow`. It is the disc the client draws
under the NPC when its spot shadows option is on (`ShadowList.model`): three rings about a middle
vertex, each ring one colour and one alpha between the type's `shadowInnerColour` and
`shadowOuterColour` and its `shadowInnerAlpha` and `shadowOuterAlpha`, with more sides for a
larger NPC, stretched to the NPC's still model across and along and moved to its middle. The
node stands 15 units above the NPC's origin, because the client draws the NPC 5 units above the
ground and the shadow 20 units above it (`NPCEntity.render`). The client draws the shadow first,
blended and without writing depth, so the NPC covers it, and only where the base animation set's
`animateShadow` is true. The client also poses the disc with the NPC's sequence
(`Animator.animateShadow`), and that is not written.

Where the type has a head, the model the client shows while the NPC talks, its `headModels` are in
the model library; the client builds it (`NPCType.headModel`) with the same colour and texture
swaps, as one model that is not posed. It is apart from the NPC's own model, because an engine
needs one without the other: most NPCs are never talked to.

The base animation set names the sequences the NPC stands, idles, turns, walks, runs and crawls
with. Every frame of each of them is posed by the client's own animation code: the sequence is
given to `getModel` as the NPC's movement animator, held at the start of the frame, and the
vertices are read back from the model it returns.

The frames are written as bones. The client animates a model label by label: every vertex carries
one label, a frame lists transforms, and each names the labels it moves, as a pivot, a move, a
turn about the pivot or a scale about it. Each is affine, so what a frame does to a label is one
affine transform, and `Skinning` works it out by following the frame's transforms on the vertices
as the client does. Every label a vertex carries becomes a joint, and every vertex is bound to
the joint of its label alone. A joint holds a move, a turn and a scale, and a label that a frame
turns and then scales unevenly cannot be held so, so such a label gets a chain of three nodes: a
move and a turn, a scale, and a turn again, which holds any affine transform by its singular
value decomposition. Each key of an animation sets every joint, and a joint that stands still
through a sequence gets no channel for it.

The frames are also written as the client reads them, for an engine that would rather follow the
client's transforms itself, as it must to tween them. The skin's `extras` hold, for each joint,
the `labels` it carries (-1 for the joint of no label), where the label's vertices are centred in
the still model before any scale (`labelCentres`, in the client's units and frame), how many
there are (`labelCounts`), which a pivot over several labels is weighed by, and the `poseScale`
the client applies after posing. Each animation's `extras` hold `frames`, one list per key of the
frame's transforms in order, six numbers each: the group, its x, y and z values, the pivot group
the client applies first or -1, and the tween bits, where 1 means the client does not tween into
the transform and 2 that it does not tween out of it; `groupTypes` and `groupLabels` are the
frames' base, the kind of transform each group holds (0 a pivot, 1 a move, 2 a turn, 3 a scale)
and the labels it names; `groupShadowed` says whether each group also moves the entity's spot
shadow, which the client moves as a whole by those groups' transforms about its middle
(`Model.animateShadow`); `loopOffset` is how many frames from the end the sequence loops back to,
or -1 for one that plays once; and `tweened` says whether the client tweens the sequence. The
client tweens by moving each group's values part way towards the next frame's, by the share of
the current frame's cycles that have passed (`Model.applyFrame`): a turn goes the short way round
its 16384 units, and a transform either frame's bits hold is not tweened. Past the last frame it
tweens towards the frame the sequence loops back to, and not at all when it plays once.

The client works in integers, a sixteenth of a unit at a time, and the bones are floating
point, so a vertex the bones place lands within about a unit of where the client puts it, and
`exportNpc` prints the worst. Where it would land more than 2.5 units off, the frames are kept
as morph targets instead, each holding how far every corner has moved, which is exact and large.
A frame that changes the colour or alpha of faces keeps a morph target for that in either case,
holding only the colours. The client scales an NPC after it has posed it, so the frames are
followed on the unscaled model and each label's transform is taken into the scaled one.

Each sequence becomes one animation, named after what the set uses it for and the sequence's id,
such as `stand 808` or `walk 819`. A sequence used for two things, such as one sequence for both
ways of turning on the spot, is written once for each. The animation sets the weight of one
target at a time, and shows each frame for as long as the client does: the client moves an
animation on by one cycle every 20 ms, and a sequence gives each frame a number of cycles. A
frame of no cycles is never shown, and is left out. Its `extras` hold the role, the sequence id,
whether the client tweens it, and, for a sequence that loops over only its last frames, the time
the loop starts at, `loopStartSeconds`, in seconds.

Some of this is not what the client does. The glTF animation jumps from frame to frame, where the
client tweens a sequence that asks for it, moving each part a little further towards the next
frame every cycle. Most stand and walk sequences ask for that, so they move more smoothly in the
client. An engine that wants that follows the `frames` in the extras instead of the channels. The normals are those of the model with no sequence playing, because the software
toolkit lights a model once, before it poses it, and never turns the normals as the model moves.
The hardware toolkits do turn them with the parts when a sequence asks them to, and that is not
written.

A frame can also change the colour or the alpha of the faces of a part, which is how a flame burns
in place: every tongue of it is in the mesh, and the frames fade them in and out in turn. Where any
frame does that, each morph target also holds how far each corner's colour and alpha have moved,
and a face that any frame makes see-through is put in a material that blends, so that it can be.
A face the mesh itself makes invisible is kept where a frame fades it in. A frame can also move a
billboard, and that is not written. A sequence is written in full and loops as a whole, where the client
plays the frames before a sequence's loop once, stops a sequence after its greatest number of
loops, and picks between idle sequences at random by their weights. The sounds a sequence
plays, its particles and its billboards are not written, and nor are the action sequences an
NPC plays when the game tells it to, such as an attack.


## Writing a map square

    ./gradlew :export:exportMapSquare --args="--x 50 --z 50"
    ./gradlew :export:exportMapSquare --args="--x 52 --z 47 --out /tmp/desert.gltf"
    ./gradlew :export:exportMapSquare --args="--x 50 --z 50 --no-locations"

A map square is 64 tiles by 64, named by where it is in map squares: map square 50_50 holds tiles
3200,3200 to 3263,3263, which is Lumbridge. It is written as two files: its ground, as glTF,
and a description of where each of its locations stands, as JSON beside it. Without `--out` they
are `export/build/mapsquares/<x>_<z>.gltf` and `<x>_<z>.json`, and `--out`, `--cache` and
`--textures` work as they do for a model. The locations of a map square are locked with a key,
which is read from `--keys`, or else as the `cache` module's census reads it. A map square with no
key that opens it is written with its ground alone, and the tool says why. `--no-locations` writes
the ground alone on purpose.

The map square is built the way the client builds the world around the player, by the client's own
code. The client never builds one map square on its own: the colour of a tile at the edge of a map
square is smoothed with the map squares beside it, and the heights of its corners are theirs too. So
the map square is built as the middle of a region of three map squares by three, and only the middle
one is written. The tiles of each map square are read with `Terrain.decodeMapSquare`, and the
toolkit is given a ground for each level by `Terrain.createGrounds`. The map square's locations are
placed with `MapRegion.loadLocations`, and then `Terrain.load` smooths the underlays across their
neighbours, blends the overlays into them, and hands every tile to the ground with its shape cut,
its colours and its textures. Everything runs on the software toolkit, with the options of a player
on high detail: ground blending, textures, ground decorations and high water detail on, and every
location placed whatever level the player stands on.


### The ground

The ground the toolkit is left with holds each tile as the rasteriser draws it, as triangles with
a height, a colour, a texture and a texture size at each corner, and that is what is written. The
ground of each level that has tiles is a node of its own, named for the level. The ground is
turned and scaled as a model is, with the map square's south west corner at the origin and the
client's heights kept, so a level of ground lies where the client draws it and a location placed
on it stands on it. Tiles meet corner to corner, and a corner that two tiles share is one vertex.

The client lights the ground as it builds it, from the slope of each corner and the toolkit's sun,
and that light is turned up to full here, so the colours written are the ground's own, as a
model's are. `COLOR_0` is that colour as the software toolkit holds it. The GL toolkit colours a
ground vertex another way (`GlGround`, `GlGroundLayer.setVertexColour`): it takes the vertex's HSL
colour, scales its lightness by the light on the tile, 74 less the shadow the locations cast on
the corner, out of 128, holds it between 2 and 126, and takes it through the palette, whose
gamma comes after the scaling; a textured vertex is then pulled towards a grey of that light, two
steps a step, by its texture's `alpha`, and brightened by its `brightness`. Since the shadow
depends on the locations an engine places, the HSL is written too, as `_HSL`, four unsigned
bytes a vertex: the hue of 64, the saturation of 8, the lightness of 128, and a spare. A
`RecordingGround` keeps it as the terrain hands each tile over, before the software ground turns
it into RGB in place. Each corner is given a normal from the same slope the client lights it by, and a
point within a tile blends the normals of its corners as the client blends their light. The
texture coordinates are the client's too: where a corner is in the world over the size the floor
type gives its texture, so a texture runs on from tile to tile, and from one map square to the next.

A face the rasteriser skips because its first corner has no colour is left out. Where the corners
of a face name different textures, the rasteriser blends them across the face, each weighted by
how near its corner a pixel is. glTF has no way to say that, so such a face is written as layers:
the first corner's texture, and over it each other texture, blended in by an alpha that is 1 at
the corners that name it and 0 at the others. Where two textures meet on a face that is exactly
what the rasteriser does. Where three meet, the last layer also covers part of the second, which
is close but not exact. A texture that cuts out by its alpha where it is drawn alone has no holes
on such a face, as the rasteriser's blend never reads a texel's alpha: the first layer is opaque,
and a layer over it is clear only where its texel is black, where the rasteriser blends in black. A
face of several textures where one of them is water, which blends by its own alpha, is drawn with
its first corner's texture. A corner whose texture has a size of 0, as a
few floor types give in the map squares that have sky boxes, is drawn by neither toolkit: each
divides by the size. It is written untextured, in its colour alone, and the task counts such faces.
On high water detail the toolkit makes water
see-through, and it is written as a face that blends by the alpha the toolkit gives it.

Where the region has a world under its water, the client reads it as a region of its own and
builds it beneath the land, against the land's heights. Its ground is written as the node
`underwater bed`, and its locations, which the client reads without a key, with the rest.

Each ground node's `extras` give its `level`, how many `tiles` it holds, whether it is
`underwater`, and whether it is `blended`.

### The ground with ground blending off

The player's ground blending option (`ClientOptions.groundBlending`) decides how `Terrain.load`
hands the tiles over. With it on, as above, each corner of a tile takes its colour from the tiles
around it, so colours and textures fade from tile to tile (`Terrain.loadBlended`). With it off,
each face of a tile is in one colour, the tile's own smoothed underlay colour or its overlay's
colour, and its texture stops at the tile's edge (`Terrain.loadUnblended`), which gives the hard
edged tiles of the original look. The map square is read a second time with the option off, and
that ground is written beside the blended one, in nodes named as the blended ones with
` unblended` after the name, whose `extras` have `blended` false. An engine draws the blended
nodes or the unblended ones, never both. Every vertex of an unblended face has the face's HSL as
`_HSL`, and as `COLOR_0` that HSL through the palette at full light, mixed towards the water's
colour as deep as the vertex lies, as the software ground makes a blended vertex's colour. The GL toolkit draws the bed in a pass of its own (`UnderwaterEffect`), tinting
each vertex towards the water's colour by how deep under the surface it lies, and the bed carries
what that needs. Each bed vertex has `_WATER_DEPTH`, a float: how far under the water's surface it
lies, in the client's units, as the terrain hands it to the ground, which writes one less into
the vertex. Each bed material's `extras` give the water over its tiles, from the overlay's
`FloorOverlayType`: `waterColour`, packed 0xRRGGBB, `waterDepth`, the depth in the client's
units at which the tint is whole, and `waterBias`, a bias on it out of 255. A bed tile is
batched by them, as the GL ground batches its tiles. The tint is the lit, textured colour mixed
towards `waterColour` by a two texel alpha ramp, read linearly and clamped, at the greater of two
terms held to one: the depth over `waterDepth` plus the bias over 255, and that times how far
past a quarter of the view before the far plane the vertex is, over 512 units. From an eighth
of the view before the far plane the vertex also rises to the surface, by its whole depth over
256 units of eye depth. A land tile's material carries no water.


### The location library

    ./gradlew :export:exportLoc --args="--loc 33799"
    ./gradlew :export:exportLoc --args="--loc 33799 --baked /tmp/33799.gltf"

A location type is written once, as `export/build/locs/<id>.json`, unless `--locs` names another
directory, and every map square that places it refers to it by its id. The file is the type's data;
it names the models of each shape, which are in the model library, and the sequences the type plays,
which are in the sequence library, and a map square writes any of them that its library lacks. A map
square writes any location it names that the library lacks, and leaves one that is there as it is,
as it does with textures.

An engine builds a mesh for each shape the type has a model for, as the client builds a location's
model (`LocType.model`): the shape's models merged, mirrored where the type says so, recoloured,
retextured and tinted, and lit under the type's `ambient` plus 64 and `contrast` plus 850. That is
the part of what the client builds that is the same wherever the location stands; the client turns,
scales, moves and bends the model after that, and all of it depends on the placement. A type that
animates is scaled when it is built, by `scaledInAsset`, because the client scales a location before
it poses it and a frame's move is not scaled with it; its `resize` is then 128. Its mesh is bound to
a joint for each label and posed by its sequences' frames, and an animated wall decoration has a
second mesh, turned 45 degrees, for a diagonal placement, as the client turns such a decoration
before the frames move it. The client's own top and bottom of the model, `minY` and `maxY`, which
the bend measures it by, are over the merged model's vertices before `maxVertex`.

A model's particles and billboards are in its node's `extras` in the model library. Each emitter has
its `type`, the draw `priority` of its particles, and the three corners `a`, `b` and `c` of the face
it spawns them over, and each effector its `type` and the vertex it stands `at`. The points are the
client's vertices, in its units and frame before the file's turn, so an engine places them as it
places the model's vertices, through the same merge, mirror, scale and turn. The types are in the
texture library, under `particle/`. Each billboard, a sprite the client draws on a face, is resolved
from its type: the `centre` of its face in the same frame, the `distance` it is pulled towards the
camera in the client's units, its `halfWidth` and `halfHeight` in those units, its `texture`, its
`blendMode` (1 by alpha, 2 added, 128 multiplied in), the face's `colour` from the palette, and its
`alpha` out of 255. The GL toolkit draws each as a square facing the camera at that size, in that
colour, unlit, blended that way (`GlModel.renderBillboards`); the face under a billboard whose type
hides it is never drawn.

`--baked` on `exportLoc`, and `--baked-locs` on `exportMapSquare`, also write each location's meshes
as the client builds them into a glTF file of their own, a node named `shape <n>` for each shape,
with the extras above, a skin and an animation for each sequence for one that animates, and its
colour changes as morph targets. These are the reference an engine's own building is checked against.

Of 59,434 types with a model, 55,604 have one shape, and the
rest, such as walls and fences, name a different mesh for each shape. Each mesh is the part of what
the client builds that is the same wherever the location stands: the shape's meshes merged, mirrored
where the type says so, and recoloured and retextured. The client turns, scales, moves and bends
the model after that, and all of it depends on the placement, so none of it is in the mesh. The
type's data in the JSON file carries what that needs, along with the
type's `loc` id, its `name` and its `shapes`: the type's `resize`, `offset`, `translate`, `hillchange` and
`hillskew`, whether the mesh is `mirrored`, and the `sequences` the location plays, their weights
and whether the client starts at a random frame. They also carry the type's `sizeTiles`, its size in tiles as
width and length before any turn, and whether it casts a `shadow`: when the client builds a map
square it darkens the ground's tile corners under each location that does, by the location's
radius over four up to 30 for one that stands on its tiles, and by 50 for the two corners of a
straight wall and the one corner of a corner wall (`MapRegion.loadLocation`, `Ground.ka`). An
engine reads the placements and does the same, since a location spawned later casts no less.
They also say whether it casts a `hardShadow`, the GL toolkit's shadow of the model's faces
projected along the sun onto the ground (`GlModel.drawShadow`, `GlShadowMap`), which walls and
locations cast and decorations do not, 32 units a texel, darkening the ground by 68 of 255 with
a one texel rim at a quarter of that for each covered neighbour. They say whether the location
is `interactive`, which the client decides once it reads the type (`LocType.postDecode`): as the
type says, or, where it says nothing, when the type offers an option or its only shape is 10. The
client lets the mouse pick only an interactive location, and never one under water. They also
carry the type's five `ops`, the options its data sets on the mini menu in the client's order,
with null for an empty slot; the client adds Examine as a sixth to every type, as for an NPC. The JSON file holds every other field of the type too, under
the name the client gives it, as an NPC's does: the `models` of each shape in `modelShapes`, the
colour and texture swaps (`recolours` as pairs of the client's colours, signed, `retextures` as
pairs of texture ids), `ambient`, `contrast` and `tint` that the meshes are built with, and the rest,
such as how the location blocks movement and sight, its sounds, its map icon and its `params`, for
an engine that needs them.


### The environment

After its tiles, a map square's file says how the map square is lit and what lights stand on
it. On the software toolkit the client throws most of that away, so the export reads the bytes
again as the client reads them, and the reading must end exactly where the file does or the
export fails: an unread field was planted and failed the export at once.

The description's `environment` holds the sun's direction in the client's frame, where its
light comes from with y down, its colour and its two strengths, for faces that look at it and
away from it, the ambient factor every face gets, the fog's colour and range, the GL toolkit's
`bloom` (its `threshold`, the luminance below which a pixel adds no glow; its `strength`, how much
of the blurred glow is added; and its `whitePoint`, the luminance its tone map takes to white, as
`GlBloomFilter`'s shaders use them), and the sky box and reflection cube map where the file
names them. A map square whose file says nothing gets the client's defaults, which are a sun
from (-50, -60, -50) at 0.7, an ambient of 1.15, a fog of 13156520 and a bloom of threshold 1,
strength 0.25 and white point 1. The hardware toolkits
start the fog `(fogRange + 256) * 4` units before the far plane.

Its `lights` list every light placed on the map square: its level and whether it lights the
levels above and below, where it stands in the client's units from the map square's corner,
with its height as the client places it, the ground's height at its tile less the height the
file gives, how many tiles it reaches, `radiusTiles`, and which tiles of each row it lights, its colour, and
its flicker. The flicker is one of the client's presets, or a light type from the config where
the preset is 31, and either way it is written resolved as the least the light falls to, the
pattern it follows, and how far and how fast it swings, out of 2048. The same lights are in
the ground file as `KHR_lights_punctual` point lights, one node each, so that an engine that
reads the extension places them without reading the description. Each reaches its radius of
tiles and half a tile more, at the strength of 1 the client gives every light before its
flicker, with its level and flicker in the node's `extras`.

### The description

The description names the ground file and the library, and lists every placement in the client's
units: 512 to a tile, x east, y down and z north, which the glTF frame takes as (x, -y, -z) over
512. Each placement names its `loc`, the `shape` the client builds its model as, its `rotation`,
its `level`, the `virtualLevel` whose ground it is bent against, whether it is `underwater`, where
it stands as `x`, `y` and `z` from the map square's south west corner, and what the client keeps
it as, its `part`. A location that takes
the look of another by a variable is named as the look it has taken, with every variable at 0,
which is how the client stands before the server sends any, and `sequencesOf` names the location
whose sequences it plays where that is not the same one.

The description holds `flags`: the client's flags for every tile of each level, as
`[level][x][z]` over the map square, which decide what the client shows from where the player
stands. 1 blocks movement, 2 marks a bridge, 4 marks a tile whose roof is removed when the
player is under it, 8 marks a tile whose contents count as level 0 whatever level they are on,
16 marks a tile that is never drawn, and 128 marks water. The client loads a location on level L
at a tile only when the tile is a bridge on level 0, or when it is not hidden and its effective
level is the player's: 0 for a tile flagged 8, one less for a tile above a bridge, and L
otherwise (`Static696.isTileVisibleFrom`, `Static705.getMapLevel`).

The description holds `floorShadows`: for every tile of each level, as `[level][x][z]` over the
map square like `flags`, 1 where the tile's floor casts a hard shadow and 0 where it does not.
The blended terrain decides it as it builds a tile (`Terrain.loadBlended`): the floor casts one
where it has an overlay that is not shape 12, has a colour and blocks shadow, or an underlay that
is not shape 0 and allows shadow. It hands the answer to the ground (`GlGround.U`), and the GL
toolkit makes a shadow only of the tiles marked so (`GlGround.fa`), casting each one of levels 1
to 3 onto every level below. With ground blending off the client marks no tile, so no floor
casts a shadow.

The description holds `cameraHeights`: for each level, `[]` or a 16 by 16 grid, `[x][z]`, of
one value for each four tiles square of the map square, in steps of 32 of the client's units.
The client reads them from the environment that follows the tiles (`MapRegion`, code 129) and
its camera keeps its pitch above whatever stands around the point it looks at by them
(`Static723.clampPlayerCamera`): the least pitch is raised by the greatest of a tile's ground plus
its camera height over the ground at the point, across the nine by nine tiles about it. A
level the file leaves out counts as 0 everywhere, and a level the file says to copy takes the
level below.

The description also holds `heights`: the height of every tile corner of each level, and of the
bed under the water where there is one, from eight tiles before the map square to eight tiles
after, which is what a location is bent against. A large location on the edge of the map square
reaches well into the neighbour.

An importer places a location by these steps, in order, in the client's units:

1. It takes the mesh of the placement's shape, or the turned one for a wall decoration that
   animates placed with a rotation above 3.
2. An L-shaped wall placed with a rotation above 3 is mirrored along z.
3. A wall decoration placed with a rotation above 3 is turned 45 degrees about y, unless the
   mesh is the turned one, and moved by (180, 0, -180).
4. The model is turned about y by a quarter turn for each of the rotation's low two bits, which
   takes (x, z) to (z, -x) in the client's frame.
5. It is scaled by `resize` over 128 along each axis of the world.
6. It is moved by `offset`.
7. A centrepiece placed with a rotation above 3, which the map placed as a diagonal, is turned 45
   degrees about y.
8. Where `hillchange` is not 0, it is bent to the ground under it as `JavaModel.p` bends it, from
   the heights of the tile corners it covers, measured from the placement's `y`, with the model's
   top and bottom taken from the mesh's `minY` and `maxY` scaled as the model was.
9. It is moved by `translate`.
10. It stands at the placement's position.

The client does every step in integer arithmetic, and an importer that wants the same vertices
to the unit does too: a turn of 45 degrees uses the client's sine and cosine of 11585 out of
16384 and shifts the products right by 14, and a scale multiplies by `resize` and shifts right by
7. The `viewer` module's import does so for every bent placement, and places the rest with a
floating point transform, which lands within a unit.

`LocPlacing` writes those steps with the client's own model operations, and every placement of a
map square is checked against them: the location's asset, placed that way, is compared vertex for
vertex with the model the client builds for the placement, and a map square with any that differ
is reported and fails. A location that animates is also checked at the first frame of its first
sequence, to within one unit, because the client turns a frame's angles to suit the placement
before it poses and an importer turns the posed result, and the two round differently by at most
one unit. Every placement of Lumbridge passes, and a planted missing turn fails 23 of the
desert's 44.

Some of this is not what the client draws. The particles, billboards and sounds of a location are
not written, and nor are the NPCs and items the server puts on the map square. The light the
client bakes into the ground and the shadows locations cast on it are left out, as the ground is
lit where it is shown. So are the map square's sun, fog, point lights and sky box, and the
water's moving textures; the water's tint on the bed is given as data, above, for the importer
to draw. Every roof is written, where the client hides those above the player. A bridge keeps the level its tiles
are given in the map, where the client draws it with the level below.

The tool prints how many tiles and faces each level has, how many faces are written as layers or
left out and why, how many locations it placed and of how many kinds, and how many placements the
importer's steps reproduce.


## Writing the sprites

    ./gradlew :export:exportSprites
    ./gradlew :export:exportSprites --args="--sprite 169 --out /tmp/compass"

A sprite is one group of the sprites archive, named by the group's id, and is what the client
draws its interfaces, icons, cursors, map furniture and fonts with. Without `--out` every sprite
is written to `export/build/sprites`, and `--cache` works as it does for a model. `--sprite`
writes only the sprites it names, and can be given more than once.

Each frame of a sprite is written as `<id>/<n>.png`, where `n` is its place in the sprite from
0, which is the index the client uses. The PNG is the frame on its whole canvas, so it holds
everything the client needs to draw it, and no other file describes it. The client stores only
the rectangle of a frame that it draws, with its left and top margins on a canvas that all the
frames of a sprite share (`IndexedImage.load`), and draws a frame at a point by putting the
canvas's top left corner there. The PNG holds the canvas with the rectangle at its margins and
clear pixels around it, so an engine draws the PNG's top left corner at the point. A frame whose
canvas has no pixels is one clear pixel, as a PNG cannot be smaller.

The colours are the ones both toolkits make from the palette (`JavaToolkit.createSprite`,
`GlToolkit.createSprite`). A frame that carries no alpha shows its palette entry 0 as a clear
pixel and every other entry opaque. A frame that carries alpha takes each pixel's alpha as it is,
whatever its palette entry. Blending a pixel whose alpha is 0 or 255 gives what cutting it out
gives, so an engine blends every frame. The PNG holds straight alpha, which is not multiplied into
the colour, as the client blends it. The client turns a palette colour of 0 into 1 as it reads the
palette, so that only entry 0 is ever clear, and so a black pixel of an opaque frame is 0x000001.

The client asks for a few sprites by name, by the hash of the name that the archive's index
keeps for each group (`Sprites.init`, `Fonts.init`). Those names are in `names.json`, an object
from each name to the sprite's id, and each is written only where the hash finds a group:
`compass`, `mapflag`, `scrollbar`, the head icons, hit bars, map dots and the three fonts
`p11_full`, `p12_full` and `b12_full`, among others. The names of the other fonts that `FontNames`
finds by their hashes, such as `q8_full` and the lobby's `verdana_13pt_regular`, are there as well. Every other sprite the client finds by an id
that a config type or an interface holds. A run with `--sprite` writes no names.

Every frame is checked against the client as it is written. The PNG is read back and must match
pixel for pixel the canvas the client lays out from the same image (`IndexedImage.toArgb`).
The sprite the software toolkit builds from the image, laid on a clear canvas of the toolkit's
size at the toolkit's margins, must match it too. A sprite that differs fails the export. A
planted dropped left margin fails 4,053 of the 14,904 frames, and a planted swap of the red and
blue channels fails 7,820.


## Writing the mini menu's style

    ./gradlew :export:exportMiniMenu

The client draws its mini menu in one of two ways. By default it draws a plain frame in fixed
colours (`MiniMenu.drawWithoutSprites`). A script can switch it to a frame built from sprites,
in colours the script chooses, with the command `FORMATMINIMENU`, and back again with
`DEFAULTMINIMENU` (`ScriptRunner`). The task decodes every script in the cache with the client's
own decoder (`ClientScript.decode`) and writes what those commands are given to
`export/build/minimenu.json`. `--out` names another file, and `--cache` works as it does for a
model.

`formats` is an object from the id of each script that calls `FORMATMINIMENU` to the eleven
numbers it gives, under the names of the client's fields that keep them. `topColour` and
`topOpacity` fill the band behind "Choose Option" and the body below it; `spriteBodyColour` and
`spriteBodyOpacity` fill the band behind the entry under the pointer. An opacity is how far the
fill lets what is behind it show, out of 255, so 0 is opaque. `separatorSpriteId` is tiled along
the top, between the two corners of `topCornerSpriteId`, whose right corner is the sprite flipped.
`horizontalBorderSpriteId` is tiled along the bottom, `verticalBorderSpriteId` down the left side,
and flipped down the right, and `bottomCornerSpriteId` makes the bottom corners the same way
(`MiniMenu.drawTop`, `drawBorder`). `textColour` is the colour of the entries, and
`spriteHighlightColour` is the colour of the entry under the pointer. Each sprite id is a sprite
that the sprite export writes. `defaults` lists the scripts that call `DEFAULTMINIMENU`.

A script pushes the command's arguments as constants just before it calls it. Where an argument
is not a constant, the style is known only while the game runs, so the task names the script and
writes nothing. A planted check that takes a local variable for a constant fails on the one call
there is. In this cache, script 51 is the only script that calls `FORMATMINIMENU`, scripts 1299
and 1433 call script 51, and no script calls `DEFAULTMINIMENU`.

## Writing the sky boxes

    ./gradlew :export:exportSkyBoxes

A map square's environment can name a sky box, which the client draws behind its scene
(`Environment.decodeSkyBox`, `SkyBox.renderLayer`); the map square's description gives it as
`environment.skyBox`, with its `rotation` and `sphereOffset`. The task decodes every sky box type
with the client's own type lists (`SkyBoxTypeList.list`, `SkyBoxSphereTypeList.list`) and writes
them to `export/build/skyboxes.json`. `--out` names another file, and `--cache` works as it does for
a model.

`types` is an object from each sky box type's id to its fields, under the client's names, with null
where the client keeps -1 to name nothing. `texture` is the panorama, a texture that the texture
export writes, which the client draws without a mesh, and `tileMode` says how: 1 repeats it across
and fills above and below it with its top and bottom pixels, 0 repeats it both ways. `meshId` is a
model that the hardware toolkits draw instead, with a camera that turns but does not move, where
the sky detail option is on; the model export writes it. `lightSphereIndex` and `spheres` are the
suns and moons drawn over it, each sphere written in full inside its sky box, as only sky boxes use
them. A texture or a model that a type names must be in the cache, or the task stops; a planted check
that looks for each texture 100000 ids on stops it. In this cache there are two sky boxes, each a
panorama and a mesh (models 43756 and 43748) and no spheres, and 488 map squares name one, between
map squares 61 and 92 across and 60 and 77 up.

## Writing the combat styles

    ./gradlew :export:exportCombatStyles

The combat styles tab (interface 884) shows two to four tiles, one for each way to fight with the
weapon the player wears. Script 1142 runs as the tab loads and as the worn items change. With no
weapon worn, or a members' weapon on a free world, it offers the unarmed styles. Otherwise it
switches on the weapon's category, its param 686, and each case calls script 1143 with four styles,
each a label, an icon and a tooltip, pushed as constants just before the call, with the tooltip
joined from its lines. The task works the calls out from the script's instructions and writes them
to `export/build/combatstyles.json`. `--out` names another file, and `--cache` works as it does for
a model.

`unarmed` is the list of unarmed styles, and `categories` is a list of each category the switch
names, as `category` and its `styles`. A style is its `label`, its `icon`, a sprite that the sprite
export writes, and its `tooltip`, in the client's text markup. A style with an empty label is a tile
the tab hides, and is not written. The tiles stand in the order of the list, two to a row. The task
stops where the script no longer has this shape; a planted check that looks for calls of script 1144
stops it. In this cache, 27 categories have styles.

## Writing the hit splats

    ./gradlew :export:exportHitmarks

The client draws a hit splat over an entity that takes a hit, from a hitmark type the server names
(`OverlayManager.render`). The task decodes every hitmark type with the client's own type list
(`HitmarkTypeList.list`), and the graphics defaults with the client's own decoder
(`GraphicsDefaults`), and writes both to `export/build/hitmarks.json`. `--out` names another file,
and `--cache` works as it does for a model.

`types` is an object from each hitmark type's id to its fields, under the client's names, with null
where the client keeps -1 to name nothing. A splat is a row of sprites with the amount written over
it. `icon` is drawn first, at the left. `left` follows it, then `inner`, repeated as many times as
it takes to be wider than the amount, then `right`. Each is a sprite that the sprite export writes.
A type with a second hit, a soak, draws the soak type's row after the first, 2 pixels to the right.
The client writes the amount in the font `font`, or in `p11_full` where the type names none, in
`textColour`, centred over the run of `inner`, on a baseline 15 pixels below the top of the row and
`textOffsetY` pixels lower still. Its text is `amountString` with each `%1` in it replaced by the
amount in decimal, with a minus sign when it is negative (`HitmarkType.getAmountText`). A type with an
empty `amountString` writes no text.

A splat lasts `duration` client cycles of 20 milliseconds. Over that time it moves steadily from
where it starts to `offsetX` pixels to the right and `offsetY` pixels up. Where `fadeTime` is not
null, the splat is opaque for its first `fadeTime` cycles and then fades out: its alpha is the time
left, times 256, over `duration` less `fadeTime`. `comparisonType` says what a new splat does when
every place for one is in use (`PathingEntity.hit`): with null it is not shown, with 0 it takes the
place of the splat that ends first, and with 1 it takes the place of the splat with the smallest
amount, if its own amount is larger.

`defaults` holds every field of the graphics defaults. `maxhitmarks` is how many splats an entity
shows at once. Splat number i stands `hitmarkpos_x[i]` pixels right and `hitmarkpos_y[i]` pixels
down from the point the client projects at half the entity's height. The middle of the row stands at
that point across, and its top 12 pixels above it. `npcShouldDisplayChat` and
`playerShouldDisplayChat` say whether NPCs and players show overhead chat, and `npcChatTimeout` and
`playerChatTimeout` are numbers the client keeps for how long it stays up. `profilingModel`,
`login_interface` and `lobby_interface` name a model and two interfaces, and `recol_s` and `recol_d`
are the tables of colours the client recolours players' kit with, null where the cache gives none.

Every sprite and font a type names must be in the cache, or the task names each one that is not and
writes nothing. A planted check that looks for each sprite under an id 100,000 higher fails on all
97 of the sprites the types name. In this cache there are 28 types, which draw with the fonts 307
(`tutorial_font`) and 591 (`menu_font_small`), and an entity shows 6 splats at once.

## Writing the fonts

    ./gradlew :export:exportFonts
    ./gradlew :export:exportFonts --args="--out /path/to/fonts"

Every font the cache holds is written as `<id>.bdf`, in the Glyph Bitmap Distribution Format
(BDF 2.1), a text file that holds each glyph's bitmap with the font's metrics, which FreeType,
X11, Pillow and many font tools read. Without `--out` the fonts go to `export/build/fonts`, and
`--cache` works as it does for a model.

The client keeps a font as two halves under one id. The sprite archive holds 256 glyph images,
one for each byte of code page 1252, and the font metrics archive holds the advance of each
glyph, the line spacing, and how far the text reaches above and below its baseline. A font is
every group of the metrics archive, with the sprite group of the same id, and both halves are
read with the client's own readers (`FontMetrics`, `IndexedImage.load`).

`FONT` is the font's name where it is known, and otherwise its id. The archive keeps only a hash
of each name, so a name is given where a known name hashes to the group's hash. The client asks
for `p11_full`, `p12_full` and `b12_full` by name (`Fonts.load`). The other names were found by
hashing candidate names, so one may be a chance match: `palatino_linotype_18pt_regular` holds
nearly the glyphs of `verdana_15pt_regular`. The cache holds two fonts twice: 5631 is `q8_full`
again.

`SIZE` is the client's line spacing, the distance from one line's baseline to the next, at 72
dots an inch, where a point is a pixel. `FONT_ASCENT` and `FONT_DESCENT` are how far the text
reaches above and below the baseline: the client puts the top of a line at the baseline less
the ascent (`Font.renderLines`), and most of its text leaves the ascent above the first line and
the descent below the last. `DEFAULT_CHAR` is the question mark, which the client draws for a
character it has no byte for. `GLYPH_COLOUR` is the one colour of every glyph, as hex RGB, which
a component that asks for the glyphs' own colours draws them in; otherwise the client draws them
in the text's colour. Every glyph in this cache is white or nearly white, with no channel below
253.

The characters are Unicode. The client turns each character of a string into a byte of code page
1252 (`Cp1252.encode`) and draws the glyph of that byte, so each glyph is written under the
lowest character that reaches it. A character with no byte becomes a question mark, so byte 0
and the five bytes code page 1252 leaves undefined are never drawn and are not written. The
client draws a glyph at the baseline less the line spacing, plus the offset its image carries
(`Font.render`). BDF places a glyph's bitmap by its bottom left corner from the pen on the
baseline, upwards, so a glyph's `BBX` offset across is the image's own offset, and its offset
up is the line spacing less the image's offset and its height. `DWIDTH` is the advance from the
metrics, and `SWIDTH` is the same in thousandths of the size, which BDF asks for. The space glyph
has no bitmap, because the client only moves on by its advance. A pixel is set wherever the glyph
image is not 0.

BDF holds one bit a pixel, no colour of each pixel and no kerning. No font in this cache has a
kerning table or glyphs with their own alpha, and every glyph of a font has the one colour, so
nothing is lost. A font that does not fit is refused, and the export fails.

Every font is checked as it is written. The BDF file is read back as a BDF reader reads it, and a
line of every glyph and a line of ordinary text are drawn with it by the BDF rules. The client's
software toolkit draws the same text with its own fonts, built from the cache as the client
builds them, and the two pictures are compared texel by texel, in the text's colour and in the
glyphs' own. A font that differs fails the export. All 27 fonts match. A planted offset of one
pixel up in every glyph fails all 27, and so does a planted advance one pixel too wide.

## Looking at a model

The `viewer` subproject shows everything written here, map squares too. See its README.

## Writing the sprites of the interface components

    ./gradlew :export:exportSkins

The client has no scrollbar, button or checkbox of its own. Its scripts build each one from sprite
components, and the hooks of interface components change those sprites as the pointer moves over
them. The task decodes every script in the cache with the client's own decoder
(`ClientScript.decode`) and every interface component with the client's own decoder
(`Component.decode`), and writes each set of sprites that a component is drawn in to
`export/build/skins.json`. `--out` names another file, and `--cache` works as it does for a
model.

Each set lists its sprites under the names of their parts, `scripts`, the scripts that give it,
and `interfaces`, the interfaces whose components give it; either is left out where there are
none. A part that a set does not have is left out.

- `scrollbars` are the sets that scripts give script 31. A scrollbar is 16 pixels wide:
  `upArrow` and `downArrow` are 16 by 16 at its ends, `track` is tiled down between them, and
  the dragger is `draggerTop` and `draggerBottom`, 5 pixels high, with `draggerMiddle` tiled
  between them.
- `plateButtons` are buttons on a plate: `edge` at the left, the same sprite flipped at the
  right, and `middle` between, with `hoverEdge` and `hoverMiddle` in their place under the
  pointer. Scripts give script 3077 its sets; script 2975 holds its set as constants and sets it
  as its hooks say the pointer moves over the button and off it; and the hooks that run script
  4588 give the three pieces of a row, the sprites under the pointer on `onMouseOver` and the
  others on `onMouseLeave`. A layer built as a plate, an edge at its left, a middle that stretches
  across and the same edge at its right, is a plate button too where a script that the hover hooks
  of its interface run sets each piece to one other sprite by constant, as scripts 4003, 4004 and
  4010 do.
- `spriteButtons` are buttons that are one sprite, swapped for another: `sprite`, `hover` under
  the pointer and `pressed` while held, where it has them. The hooks of a component that run
  script 44 give the sprite it shows on `onMouseLeave` or `onRelease`, or its own sprite, the one
  under the pointer on `onMouseOver` or `onMouseRepeat`, and the one held on `onClick`, `onHold` or
  `onClickRepeat`. The hooks that run script 4587 do the same for a component they name, and script
  4782 holds the plain sprite and the sprite under the pointer of four tabs as constants. A hover
  setter, as `tabs` describes, gives a plain sprite and the sprite under the pointer too. A hook
  that gives -1 gives no sprite.
- `tabs` are the tabs of the interfaces: `sprite`, `hover` under the pointer, and `selected`. A
  hover setter is a script that sets one sprite on the component it is given where its second
  argument says the pointer is over it, and another where it is not, as script 2462 does for the
  tabs of the game's frame. A tab is a component that shows a hover setter's plain sprite, with
  another component of the same interface at its very place, in a layer that the client draws
  after the tab's, whose sprite is `selected`: the game's scripts hide each such component but the
  one over the chosen tab. Where the script that sets the selected sprite goes on to set more
  sprites by constant in the same arm, as script 1387 sets a glow and a frame, those are
  `selectedParts`, drawn in that order over the selected tab, each a `sprite` `x` and `y` from the
  tab's corner, `width` by `height`, worked out by laying out the components through their layers
  in a window of 765 by 503.
- `checkboxes` are the checkboxes of the interfaces, each a `ticked` state and an `empty` state,
  with the scripts that set them. Script 4521 builds a checkbox in one state, `box` with
  `hoverBox` over it under the pointer and `pressedBox` over it while it is held down, from the
  sprites its callers give it: script 4519 gives the ticked state and script 4520 the empty one.
  Other scripts tick a box by setting one sprite on it where a test holds and another where it
  does not, in the two arms of the test; their states have a `box` alone. Two sprites are taken
  for a checkbox where they are the same square size, at least three scripts set them that way,
  they are not a radio button, and the empty one is not the ticked one of another checkbox.
- `radioButtons` are the radio buttons that scripts select as a group, each a `sprite` and the
  `selected` sprite. Scripts 1422 and 1423 hold theirs as constants: the first component they are
  given, the one clicked, takes `selected`, and the others take `sprite`. Other scripts set one
  sprite on three components or more in a row and then another on one of them; two sprites are taken for a
  radio button where they are the same square size, at least two scripts select between them that
  way, and none the other way, which marks them apart from the tabs and plates that scripts swap.
- `radioTiles` are the tiles of a radio group, such as the combat styles (884): a plate `width` by
  `height` that is `sprite` where it is not the one picked and `selected` where it is, an `icon`
  (`y`, `width`, `height`, centred across, as 884 places its icons 18 to 20 pixels in by hand) and
  a `label` (its box, `font`, `colour` and `shadow`), which scripts fill in as the game runs. Script
  1134 sets the plate to one sprite where a variable holds the value it is given and to the other
  where it does not, and each plate runs it as it loads and as the variable changes. The tile is
  the layer the plate stands in; the one other sprite and the one text in it are the icon and the
  label.
- `sliders` are the sliders of the interfaces: a `knob`, `knobWidth` by `knobHeight`, that the
  player drags along a box `width` by `height`, over its `track`. Scripts 1764 and 1215 move a
  knob whose drag hook runs them: they keep it inside the layer it stands in, its box, and take as
  far along the box as it is, out of the box's width less the knob's, for the value. The knob's
  sprite is its own, or where the knob is a layer, the one sprite in it. The track is every other
  sprite of the interface whose place, laid out through its layers in a window of 765 by 503,
  lies within the box's height and crosses it, left to right, each a `sprite`, `tiled` where it
  is tiled, `x` and `y` from the box's corner, `width` and `height`.
- `dropdowns` are the dropdowns that calls of script 1436 build, each once, where the call gives
  all of it as constants: `background`, tiled over the box, `arrow`, a 16 pixel wide button at
  its right, stretched to its height, `hoverArrow` in its place under the pointer, and
  `listBackground`, tiled over the open list; the colours of the text, `textColour`, with
  `otherTextColour` where the script colours some options apart from the others, and
  `hoverTextColour` under the pointer, as 0xRRGGBB; `font`, the font of the text by its id; and
  `scrollbar`, the sprites of the list's scrollbar under the names of `scrollbars`. The script
  draws a black line around the box and around the open list, writes the chosen option 5 pixels in
  from the left and centred down, and lays the list out one option every 15 pixels, scrolled by
  script 31. Script 1348 opens the list and turns the arrow upside down, and script 1349 closes it.
- `frames` are the boxes of the interfaces drawn as a frame: sprite components at its corners,
  along its sides and, where it has them, over its middle. A box is a layer with the layers in it
  that the client draws as part of it: a layer the player cannot use, shown, and the size of the
  layer it is in less a fixed amount each way (`Component.resizeModeX` and `resizeModeY` 1), so
  that what it holds keeps its place at every size, as the background of 975 and the inner frame
  of 1099 stand in layers of their own. Its components are taken in the order the client draws
  them (`InterfaceManager.draw`): the components of a layer in the order the interface holds them,
  each layer followed at once by what it holds. The task lays a box's components out at two sizes with the client's rules
  (`InterfaceManager.resize`, `reposition`): a component that keeps its size and its place
  against two sides is a corner, one that grows along a side is an edge, and one that grows both
  ways is a fill. A component the player can use, one with a hook or an option such as a close
  button in a corner, is not part of the frame (an option whose name is blank is not one, as the
  client offers none for it, `InterfaceManager.getOp`), nor is a component hidden until a script
  shows it (`Component.hidden`), as the highlight under the pointer of 995 is, and a component in none of these places, such as
  one centred on a side, is passed over. A layer holds more than its frame: icons, rows of slots,
  banners and pictures stay at a corner as a corner does, and dividers and rules stretch along a
  side as an edge does. So a component at a corner is a corner only where the corner across from
  it (top left and top right, bottom left and bottom right) has a component of the same width and
  height within 8 pixels of the same place, each measured from its own sides. Some layers were
  drawn by hand at one size, as the client never resizes them: a book (937) and a picture with
  pillars at its sides (27) keep their lower pieces against the top. Where the size of the layer is
  known, a sprite that keeps to the start of a side but stands in its far half (or to the end but
  stands in its near half), inside the layer, is such a piece, and a corner is not a corner where
  a piece of its size stands so at the other end of one of its sides, the same distance in from
  that side. Such a piece tells this only along the side it keeps to the wrong end of: the cap of
  the divider of 87, which keeps to the right of its layer while it stands at the left, says
  nothing of the corners below it. An edge is kept only where its `inset` is less than the furthest that the corners of
  its side reach in from that side, where it starts and ends within 8 pixels of what the corners at
  each end reach along the side, and where it lies against its side: the outermost edge of a side,
  then each edge no further in than the edges outside it are `seen`, or one that each of the other
  sides has an edge as far in as, to within 8 pixels, as a gold line round the inside of a frame
  (935) has. A rule under the heading of a table (1004, 1121) and a divider under a title (405)
  are on one side only, with a gap outside them, and are left out. A divider with a cap at each
  end is kept: at both of its ends a corner stands beside it, over at least half of how thick it
  is, further in from the side than the outermost corners at that end, as the line under the title
  of a stone window (828) ends in two caps (829 and 830) that cover the frame's sides where it
  meets them. A fill, of sprites or of one
  colour, is kept only where it comes out on each side to within 8 pixels of the furthest that
  the corners or the edges of that side reach, as the page of the book and a dark bar in the middle
  of a box (1099) do not. A filled rectangle that grows along a side is an edge of one colour, and
  is kept only where it reaches in no further than the corners of its side, as the dark band under
  the title of 890 does and a dark panel over 902 does not. Where the size of the layer is known,
  two more kinds of piece are read as the client draws them at that size. A grid of copies of one
  sprite at its own size, drawn alike and kept to the top left of their layer, standing edge to
  edge in full rows and columns, is one tiled fill that keeps the grid's distance from each side of
  the layer at that size, as 890 covers its middle with fifteen 100 pixel squares of sprite 4079.
  A piece of a fixed length centred on a side that is at least as long as the side at that size,
  which the client cuts to the layer, runs the length of the side and past both ends by as much, as
  the 428 pixel bottom edge of 924 does in a box 334 wide. The task places a piece by all the
  client's position modes (`InterfaceManager.reposition`, 0 to 5), and a piece it cannot place,
  in a layer or not, has no place. Where the size of the layer is known, a piece the client lays
  out to no width or no height at that size is not drawn and is not part of the frame, as the
  sides of the stone panels of 913 and 914 are 144 pixels less than the height of their box, in
  boxes 110 and 122 high; those sides are also the wrong way round, which no player saw. An edge
  that leaves a gap between itself and the corners at an end is run on to them where, at that
  size, other pieces cover each gap across the band the edge covers, as the bottom of 104 is
  drawn in two pieces round a joint (839) and the line under its title in two pieces round
  another (842); it runs on as far as the corners reach, or as far as a piece of its own sprite
  in the gap reaches where that is further. An edge across the band of an edge of its side that
  already meets its corners is not run on, as an ornament over a side is not a side. A layer without a corner at each corner and an edge along each side is
  then not written. A layer whose components take a share of its size (`Component.resizeModeX` or
  `resizeModeY` 2, a share in 16384ths) has no one shape at every size, so the task measures it at
  the size its layer has when the interface is laid out through its layers in a window of 765 by
  503, and does not write it where that size is not known. A frame of a layer is measured from the
  box its corners stand in: where the outermost corners on a side stand in from it, as a frame in
  the middle of a larger layer does (902), each part is moved out by that much. A frame is its
  `parts`, so an ornate frame may have several in one place. Parts that cover each other keep the
  order the client draws them in, and the others are put in the order of how the file writes them,
  so a frame that a layer holds in another order is written once. A frame that is the frame of one
  of the `windows`, both measured from the box their corners stand in, is not written again here.
  Each part has its `place`, its `sprite`,
  `mirrored` where it is drawn mirrored left to right (`Component.verticalFlip`), `flipped` where
  it is drawn upside down (`horizontalFlip`), `turned` where it is then turned a quarter turn
  anticlockwise about its middle, and `tiled` where the sprite is tiled over it rather than
  stretched. The client turns a sprite by its angle (`Component.angle2d`, in 65536ths of a turn,
  `Sprite.renderRotated`) after it mirrors and flips it, as the right corners of the stone panels
  of 933, 948, 949, 993 and 995 are a left corner turned. A half turn is written as a mirror and a
  flip, so a part is turned by a quarter turn or not at all. A sprite turned by any other angle, or
  a tiled sprite turned by a quarter turn, which the client turns tile by tile, is not part of a
  frame. A part at `topLeft`, `topRight`, `bottomLeft` or `bottomRight` is `x` and `y`
  from its two sides, `width` wide and `height` high. A part at `top`, `bottom`, `left` or
  `right` runs from `start` after the first end of its side to `end` before the other, `inset` in
  from the side, `thickness` thick, and is `seen` from its side as far as the part of its sprite the
  cache holds pixels for reaches, the canvas less the sprite's offsets (`IndexedImage.offX1`,
  `offX2`, `offY1`, `offY2`), mirrored, flipped or turned as it is drawn: what the frame holds
  stands inside that. A stretched edge is seen as far as that part reaches scaled to how thick it
  is; a tiled one is not scaled, as the client repeats the sprite at its own size from the top
  left of the edge and cuts the last copy (`Sprite.renderTiled`), so an edge thinner than its
  sprite shows the first rows or columns of the sprite whichever side it lies on. An edge of one colour has its `colour` and `transparency` in place of a
  `sprite` and is `seen` as far as it reaches. A part at `centre` keeps `left`, `top`, `right` and `bottom`
  from the sides. A filled rectangle component that grows both ways is a fill of one colour: in
  place of a `sprite` it has its `colour` as 0xRRGGBB and, where it is not solid, its
  `transparency`, 0 solid to 255 unseen (`Component.transparency`). A planted defect in each
  of these rules changes the file: with no pairing of corners, 125 frames are written and 98 parts
  come back, such as the row of icons in 933 and the grid in 1052; with no pieces laid out by hand,
  the pillars of 27 and the top corners of the stone frame round the book come back; with edges
  that need not meet their corners, 121 frames are written and the page of the book and a strip at
  the bottom of 19 that never meets its corners come back; with no reach for edges, 4 edges come
  back; with edges that need not lie against their side, the rules of the tables of 1004 and 1121
  come back; with no rings, the gold line of 935 is lost; with fills that need not fill, the page
  of the book and the dark bar of 1099 come back; with each layer measured at one size, 115 frames
  are written and the strip at the bottom of 19 comes back; with a frame measured from the layer's
  sides, the frame of 902 stands 60 pixels in again; with a piece outside the layer taken as laid
  out by hand, a frame of 994 is lost; with the parts in the order of the layer, 122 frames are
  written, 3 of them twice; with windows compared in the layer they were found in, 122 frames are
  written; and with the frames of windows kept, 142 frames are written. These counts are of the
  file before the rules that follow, when it had 118 frames; those rules make it 125. With the
  angle not read, 12 frames
  change, the right corners of the stone panels of 933 and 948 among them; with a half turn not
  written as a mirror and a flip, 10 frames change; with every sprite read as turned by 1092 of
  65536, no frame and no window is written; with hidden components drawn, 126 frames are written
  and the highlight of 995 and the fill of 911 come back; with no edges of one colour, the band of
  890, the dark ring of 680 and a line of 313 are lost; with edges of one colour that may reach
  past the corners, the dark panel over 902 comes back; with no layers drawn as part of the layer
  they are in, 119 frames are written and 975 loses its background; with hidden layers drawn as
  part of their layer, 128 frames are written; with layers the player can use drawn as part of
  their layer, 127 frames are written; and with the components in the order the interface holds
  them, the background of 975 covers its corners and 3 frames change. The rules after those make
  126 frames of 125: with no grids made one fill, 890 loses the middle it now has; with no pieces
  that run past the ends of their side, 924 loses its bottom edge; with the position modes 3 to 5
  not read, 124 frames are written and the shade between the bands of 72, 924 and others is lost;
  and with those modes not read and a piece of no place moved by its layer as well, 7 frames take a
  fill measured from a number of no place, as they did before these rules. No grid in this cache
  has copies of another size than their sprite's, or a gap between them, so the checks for those
  change nothing here.
  Where an interface writes a title over a frame, the frame has a `heading`: the room it has for a
  heading above what it holds, `left`, `top` and `right` from the sides of the box its corners stand
  in, and `height`, measured at the layer's real size as the other parts are. The task lays the
  interface out through its layers in the client's window and reads what the layer around the box
  holds over the frame, in the box's own layers or beside them: what is shown, but for the box itself,
  which a script may show with its frame. A title is a text component, centred across, that stands
  as far in from the one side of the frame as from the other (to within 8 pixels), starts no more
  than 8 pixels above the frame, ends within the reach of the frame's top corners, and stands above
  every other text over the frame. So the title of 890 in the dark band between its stone border and
  its divider is a title, and so is the title of 405, which has no divider; a label in a column, a
  message in the middle of a box and a line of text in a thin frame are not. Frames are written as
  one only where their headings are the same as well. A box in no layer is laid out in the client's
  window, 765 by 503, as the client lays it out, and not in the layer of its first piece. A frame
  with a close button over it is a window, as `windows` tells, and is not written here. 15 frames
  have a heading, among them 890, 1028, 883 and 924. A planted defect in each of these rules changes
  the file: with titles that need not be centred, the window of 34 takes a title 0 and 20 pixels in
  from its sides; with titles that may end past the reach of the top corners, 55 frames have a
  heading and 122 frames are written, as messages, labels and the text of buttons (596, 948, 1144)
  become headings; with titles that need not stand above the rest, 766, 1004 and 1053 take a
  heading, and the windows of 1094 and 1122 take a line of text below their title bars as their
  title; with a hidden box that hides what stands over it, 109, 549 and 1028 lose their headings and
  1094, 1100 and 1103 are not windows; with only the box read, and not the layer around it, 1122 is
  not a window and 1100 loses its title; and with a box in no layer laid out in the layer of its
  first piece, 72 is not a window and the windows of 555, 623, 1123, 1126 and 1141 change. No two
  boxes in this cache have the same parts and different headings, so frames merged whatever their
  headings change nothing here; with a heading planted one pixel lower in 1079, 103 frames are
  written, as 897 and 1079 are written apart, and with that defect as well, 102.
- `windows` are the windows of the interfaces: a frame of sprites with a title across its top and
  a button that closes it, laid out at a fixed size in a layer, the ones the most interfaces use
  first. The frame is the layer's sprite components the player cannot use, in the box they
  cover. The window is drawn at that one size, so each sprite is read as one that keeps to the
  sides it is nearest: by the third of the box its middle stands in, across and down, it keeps its
  distance from the start of the side, from its end, or in the middle third from both, growing
  with the box. A sprite in the left or right third that stands more than 16 pixels in from that
  side is read as centred across the box, as the corners of a frame and the caps of its dividers
  keep to its sides and an icon beside the title (327) or a joint of the rules of a table does not.
  The frame's rules of `frames` then read the sprites at that size, with the corners of a pair as
  much as 16 pixels apart, as the top corners of 327 stand 13 and 3 pixels in from the sides of a
  box whose right edge is thinner than its left; and the outermost corners must stand within 16
  pixels of each side of the box, as a layer whose sprites reach further out than its frame (267,
  438) holds more than a window. Its `parts` are written as a frame's are, measured from the sides
  of the box, so the window takes any size. Its `title` is the centred line of text near the top,
  as its `colour`, `font` by id, `shadow`, and its place, `left` and `right` from the sides, `top`
  from the top and `height`. Its `close` button is the sprite near the top right whose hover hook
  runs script 44, the nearest the top right corner where there are several (913, 915 and 979 have
  a help button beside it), as its `sprite`, `hover` under the pointer, its place `right` and
  `top` from the box's sides, `width` and `height`. A window whose sprites follow the size of their
  layer, as the graphics options window's do (interface 742), is laid out as the frames are, at
  two sizes with the client's rules, so a place may have several parts and a fill may be of one
  colour, and the rules that need the layer's real size read it where the layer is laid out in the
  client's window, as the bottom edge of 924 runs past both ends of its side; its title and close
  button are measured where they stand in the larger of the two sizes. A component that names
  itself as its layer is laid out nowhere.
  A frame of `frames` with a close button over it is a window as well, as the ornate windows are
  (20, 554, 555, 1099, 1102, 1111, 1122 and others), which hold their frame, their title and their
  close button each in a layer of its own. Its title is the frame's title, found as `frames` tells,
  and is left out where the interface writes none, as 1111 writes none in its title bar. Its close
  button is the sprite nearest the top right corner of the frame that stands inside the frame, in the
  right half of its top border (it starts less than 32 pixels down and ends within the reach of the
  top corners), and that either swaps itself for another sprite by script 44 under the pointer, or
  is the first of a stack of sprites at one place, one of which has an option. In a stack, another
  sprite whose hover hook shows it is `hover`, as scripts 4209 and 4214 fade it in, and another whose
  click hook shows it, where there is one, is `pressed` (script 4207). A box where the rules above
  found a window, in a layer that holds its pieces, is not read again, so those windows stay as they
  were. A planted defect in each of these rules changes the file: with no stacks, 12 windows are
  lost, the ornate windows among them; with the first button found and not the nearest, 1111 takes
  its zoom button as its close button; with buttons that may end below the top border, 4 more
  windows are written and the thin frame of 18 interfaces is lost, as 1097 has a button to delete
  each of its rows; with buttons that may stand outside the frame, 6 more windows are written from
  those buttons of 1097; and with the boxes of the windows above read again, 16 windows are written
  twice.
  The rules of dividers, of edges run on over a gap, of pieces of no size and of tiled edges, and
  the reading of a window drawn at one size by the frame's rules, make 113 windows and 98 frames
  of 110 and 102. The windows of the stone frames gain the line under their title and its caps (52
  windows, 104, 109, 332 and 382 among them), 104 its whole bottom edge, 301 its line in place of a
  fill of the line's sprite, 924 its bottom edge, and 913, 915 and 979 their close button in place
  of a help button; 913 and 914 lose the window of their small panel, whose sides the client never
  draws, and 596, 906, 994 and 1097 lose a frame so; 935 gets the bottom of its gold line, and 993
  the dark line under its heading. 626, 655, 862, 931, 979 and 985 are new windows. A planted
  defect in each of these rules changes the file: with no caps, 52 windows lose their divider; with
  no edges run on, 104 is no window and 935 loses the bottom of its gold line; with edges run on
  across an edge that meets its corners, 115 windows and 99 frames are written, as the rows of 643
  and 626 and the pages of the book (937) are run on into lines; with edges run on only to the
  corners, the line under the title of 104 stops short of its cap; with caps beside any part of
  the band, 110 takes a picture's rules as dividers; with pieces of no size drawn, 116 windows and
  102 frames are written, 913 and 914 with their sides the wrong way round; with windows not read
  at the real size of their layer, 924 loses its bottom edge and 913 and 914 come back; with the
  first close button and not the nearest, 915 and 979 take their help button; with the corners of
  a pair of a window drawn at one size no further apart than a frame's, 97 windows are written and
  the most used stone window (327) is lost; with sprites that stand in from their side kept to it,
  the icons of 327 and the column joints of 643 become corners; with the outermost corners
  anywhere, 267 and 438 come back with their frame inside a larger box; with a piece laid out by
  hand telling of corners on any side, 87 is lost; with tiled edges seen as stretched, 57 windows
  and 10 frames change how far in their edges are seen; and with a component that names itself as
  its layer laid out, the task does not end.
- `frameButtons` are the buttons a script builds as a frame: the stone button that 151 components
  have (script 92, proc 679, and under the pointer script 94, proc 1360) and the bevelled button
  of the question that accepts a graphics setting, interface 883 (procs 1151 and 1166). Each has
  its `frame` and its `hoverFrame`, in the form of `frames`, and its two `scripts`. The task plays
  each script back for a button of two sizes, following its constants, locals, sums, calls and the
  components it creates, and lays the components out as the frames are; it stops where a script
  uses an instruction the playback does not play.
- `hoverFrames` are the frames that scripts 4155 and 4158 build over a component, in the same
  form, read by playing back the components the script creates with the sprites each call gives
  it. Their pieces are clear until the pointer moves over the component, when script 4160 makes
  them opaque by 22 of 255 a client tick.

A script pushes a call's arguments just before it calls. Where it passes on one of its own
arguments, the task reads the calls of that script in turn, so a script that only forwards the
sprites does not hide them. A call whose sprites are known only while the game runs is counted and
not written. Each script that holds its sprites as constants is read by its shape, and the task
stops where a script no longer has the shape it reads. In this cache there are ten scrollbars, seven
plate buttons, 110 sprite buttons, two tabs, five checkboxes, two sets of radio buttons, one
radio tile, four sliders, one dropdown, 98 frames, 113 windows, two frame buttons and two hover frames, and six calls of the scrollbar
script are known only while the game runs. A planted check that stops reading passed-on arguments
leaves seven such calls, one more than the task finds, and a planted check that expects five sprites
in script 2975 stops the task.

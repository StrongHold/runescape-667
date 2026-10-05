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
(`Model_Sub2.method4985`): the face's HSL colour with its lightness scaled by the model's ambient,
64 plus the type's own, out of 128, and then taken through the client's palette, whose gamma of
0.7 comes after the scaling, so the result is not the palette colour dimmed. It is written as a
vertex colour in linear light. A textured face is tinted as the GL toolkit tints it: that colour
pulled towards a grey of the ambient alone by the texture's `alpha`, out of 256, and brightened
by its `brightness`, which the material's `extras` carry with its `effectType`, `effectParam1` and
`effectParam2`, for an engine that lights a texture itself or draws the texture's effect. Most textures have an alpha of 0 and keep the face's colour. A
textured face refers to its texture in the shared texture library below. Faces are grouped into one primitive for each texture and way of
blending, so a face that is drawn through what is behind it, or a texture with holes in it, gets
a material that blends or cuts out. Normals are the client's own: a smooth face takes the normals
of the faces it meets at each corner, and a flat face takes its own. They are written unit, as
glTF asks, and each vertex also carries `_SHADE`, a float: how strongly the sun lights it, out
of 1. The GL toolkit never normalises a normal (`Model_Sub2`): a smooth corner's is the sum of
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
the ripple frame of the moment, the sixteen frames over four seconds at a quarter of the texture
coordinate, is added to the lit vertex colour and the sum doubled (`GL_RGB_SCALE` 2), with the
vertex's alpha, which the GL ground writes as opaque, times the frame's alpha, so the surface
is nearly see-through; a second unit adds an alpha that fades the surface to opaque with eye
depth, from the fog's start to a fog range further, where the fog for the water starts a range
and a half before the far plane and ends a range before it (`GlToolkit.method6995`). What shows
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


## Writing an NPC

    ./gradlew :export:exportNpc --args="--npc 9"
    ./gradlew :export:exportNpc --args="--npc 81 --out /tmp/cow.gltf"

An NPC is named by its id. Without `--out` it is written to `export/build/npcs/<npc>.gltf`, and
`--out` and `--cache` work as they do for a model. The tool prints the NPC's name, how many
vertices and faces it has, and each animation it wrote with how many frames it has and how long
each frame is shown. An NPC that takes the look of another NPC by a variable is refused, and the
tool names the NPCs to write instead.

The NPC is built by the client's own `NPCType.getModel`, on type lists read from the cache and
with the same software toolkit as a model. That method reads each mesh the NPC is made of, moves
each one as its base animation set says, merges them, swaps the NPC's colours and textures, and
scales and poses the result. The model is then written as a model is, so everything above about
coordinates, colours, textures and faces holds for an NPC too. The NPC type's own data is
beside the mesh file as `<npc>.json`: its `npc` id, `name`, `size` in tiles and base animation set
`bas`; whether the mouse can pick it, `interactive`; its five `ops`, the options its data sets on
the mini menu in the client's order, with null for an empty slot (the client's type list adds a
sixth, Examine, to every type, and the data cannot change it, so it is not written); and how the client picks
it (`NPCEntity.picked`): `pickSizeShift`, by which it grows the picking cylinder and the model's
box, and `quickPick`, which is 1 for a pick by the box on the screen alone, 0 for a pick by the
model's triangles, and -1 for the default, the box for an NPC one tile across and the triangles
for a larger one, which a `pickSizeShift` above 0 also makes the box.

The JSON file holds every other field of the type too, under the name the client gives it, so
that nothing the client decodes is lost: an id the type leaves unset is -1, a list it leaves
unset is empty, and its `params` are an object keyed by the parameter's id. The `models`,
`recolours` and `retextures` as pairs of the value in the mesh and the value it becomes,
`recolourPalette`, `translations`, `scaleH`, `scaleV`, `ambient`, `diffusion` and `tint` (hue,
saturation, lightness and scale) are already applied to the mesh, and are listed so that a
reader can see what the mesh is made of. Where the type names a base animation set, every
field of that set is in `basType`, under the names the client gives them.

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
(`Animator.method9105`), and that is not written.

Where the type has a head, the model the client shows while the NPC talks, the head is written
as a file of its own, `<npc>.head.gltf`, built by the client's `NPCType.headModel` with the same
colour and texture swaps, as one mesh that is not posed. It is not in the NPC's own file, because
an engine needs one without the other: most NPCs are never talked to.

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
the loop starts at.

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
ground vertex another way (`Ground_Sub2`, `Node_Sub39.method5863`): it takes the vertex's HSL
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
is close but not exact. A face of several textures where one of them is water, which blends by its
own alpha, is drawn with its first corner's texture. On high water detail the toolkit makes water
see-through, and it is written as a face that blends by the alpha the toolkit gives it.

Where the region has a world under its water, the client reads it as a region of its own and
builds it beneath the land, against the land's heights. Its ground is written as the node
`underwater bed`, and its locations, which the client reads without a key, with the rest.

Each ground node's `extras` give its `level`, how many `tiles` it holds, and whether it is
`underwater`. The GL toolkit draws the bed in a pass of its own (`UnderwaterEffect`), tinting
each vertex towards the water's colour by how deep under the surface it lies, and the bed carries
what that needs. Each bed vertex has `_WATER`, a float: how far under the water's surface it
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

A location is written once, as `export/build/locs/<id>.gltf` with its type's data beside it as
`<id>.json`, unless `--locs` names another directory, and every map square that places it refers to it by its id. A map square writes any
location it names that the library lacks, and leaves one that is there as it is, as it does with
textures.

The file holds a mesh for each shape the location's type has a model for, as a node named
`shape <n>`, and most types have one. Each mesh's `extras` carry the client's own top and bottom
of the model, `minY` and `maxY`, which the bend measures the model by: the client takes them over
every vertex, and the mesh holds only the faces the client draws. Where the model has particles,
the `extras` also carry its `emitters`, each with its `type`, the draw `priority` of its
particles, and the three corners `a`, `b` and `c` of the face it spawns them over, and its
`effectors`, each with its `type` and the vertex it stands `at`. The points are the client's
vertices, in its units and frame before the file's turn, so an engine places them as it places
the model's vertices. The types are in the texture library, under `particle/`. Where the model has
billboards, the sprites the client draws on a face, the `extras` carry them too, each resolved
from its type: the `centre` of its face in the same frame, the `distance` it is pulled towards
the camera in the client's units, its half `width` and `height` in those units, its `texture`,
its `blendMode` (1 by alpha, 2 added, 128 multiplied in), the face's `colour` from the palette,
and its `alpha` out of 255. The GL toolkit draws each as a square facing the camera at that
size, in that colour, unlit, blended that way (`Model_Sub2.method4984`); the face under a
billboard whose type hides it is left out of the mesh. Of 59,434 types with a model, 55,604 have one shape, and the
rest, such as walls and fences, name a different mesh for each shape. Each mesh is the part of what
the client builds that is the same wherever the location stands: the shape's meshes merged, mirrored
where the type says so, and recoloured and retextured. The client turns, scales, moves and bends
the model after that, and all of it depends on the placement, so none of it is in the mesh. The
type's data is in the JSON file beside the mesh, which carries what that needs, along with the
type's `loc` id, its `name` and its `shapes`: the type's `resize`, `offset`, `translate`, `hillchange` and
`hillskew`, whether the mesh is `mirrored`, and the `sequences` the location plays, their weights
and whether the client starts at a random frame. They also carry the type's `size` in tiles, as
width and length before any turn, and whether it casts a `shadow`: when the client builds a map
square it darkens the ground's tile corners under each location that does, by the location's
radius over four up to 30 for one that stands on its tiles, and by 50 for the two corners of a
straight wall and the one corner of a corner wall (`MapRegion.loadLocation`, `Ground.ka`). An
engine reads the placements and does the same, since a location spawned later casts no less.
They also say whether it casts a `hardShadow`, the GL toolkit's shadow of the model's faces
projected along the sun onto the ground (`Model_Sub2.method4987`, `Class170`), which walls and
locations cast and decorations do not, 32 units a texel, darkening the ground by 68 of 255 with
a one texel rim at a quarter of that for each covered neighbour. They say whether the location
is `interactive`, which the client decides once it reads the type (`LocType.postDecode`): as the
type says, or, where it says nothing, when the type offers an option or its only shape is 10. The
client lets the mouse pick only an interactive location, and never one under water. They also
carry the type's five `ops`, the options its data sets on the mini menu in the client's order,
with null for an empty slot; the client adds Examine as a sixth to every type, as for an NPC. The JSON file holds every other field of the type too, under
the name the client gives it, as an NPC's does: the `models` of each shape in `modelShapes`, the
swaps, `ambient`, `contrast` and `tint` already applied to the meshes, and the rest, such as how
the location blocks movement and sight, its sounds, its map icon and its `params`, for an engine
that needs them. A location that animates has bones and an
animation for every sequence, as an NPC has, and is scaled in its asset, because the client
scales a location before it poses it and a frame's move is not scaled with it; its JSON then
says `resize` is 128 and names the scale in `scaledInAsset`. A wall decoration that animates
has a second mesh, `shape 4 turned`, for a diagonal placement: the client turns such a decoration
45 degrees before the frames of its sequence move it, and the frames are not turned with it, so
that mesh is turned already and an importer does not turn it again.


### The environment

After its tiles, a map square's file says how the map square is lit and what lights stand on
it. On the software toolkit the client throws most of that away, so the export reads the bytes
again as the client reads them, and the reading must end exactly where the file does or the
export fails: an unread field was planted and failed the export at once.

The description's `environment` holds the sun's direction in the client's frame, where its
light comes from with y down, its colour and its two strengths, for faces that look at it and
away from it, the ambient factor every face gets, the fog's colour and range, the bloom
settings of the hardware toolkits, and the sky box and reflection cube map where the file
names them. A map square whose file says nothing gets the client's defaults, which are a sun
from (-50, -60, -50) at 0.7, an ambient of 1.15 and a fog of 13156520. The hardware toolkits
start the fog `(fogRange + 256) * 4` units before the far plane.

Its `lights` list every light placed on the map square: its level and whether it lights the
levels above and below, where it stands in the client's units from the map square's corner,
with its height as the client places it, the ground's height at its tile less the height the
file gives, how many tiles it reaches and which tiles of each row it lights, its colour, and
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
otherwise (`Static696.isTileVisibleFrom`, `Static705.method9198`).

The description holds `cameraHeights`: for each level, `[]` or a 16 by 16 grid, `[x][z]`, of
one value for each four tiles square of the map square, in steps of 32 of the client's units.
The client reads them from the environment that follows the tiles (`MapRegion`, code 129) and
its camera keeps its pitch above whatever stands around the point it looks at by them
(`Static723.method9451`): the least pitch is raised by the greatest of a tile's ground plus
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
`p11_full`, `p12_full` and `b12_full`, among others. Every other sprite the client finds by an id
that a config type or an interface holds. A run with `--sprite` writes no names.

Every frame is checked against the client as it is written. The PNG is read back and must match
pixel for pixel the canvas the client lays out from the same image (`IndexedImage.method9383`).
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
`anInt7178` pixels lower still. Its text is `amountString` with each `%1` in it replaced by the
amount in decimal, with a minus sign when it is negative (`HitmarkType.method6457`). A type with an
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

## Writing the widgets' sprites

    ./gradlew :export:exportWidgets

The client has no scrollbar, button or checkbox of its own. Its scripts build each one from sprite
components, and the hooks of interface components change those sprites as the pointer moves over
them. The task decodes every script in the cache with the client's own decoder
(`ClientScript.decode`) and every interface component with the client's own decoder
(`Component.decode`), and writes each set of sprites that a widget is drawn in to
`export/build/widgets.json`. `--out` names another file, and `--cache` works as it does for a
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
  others on `onMouseLeave`.
- `spriteButtons` are buttons that are one sprite, swapped for another: `sprite`, `hover` under
  the pointer and `pressed` while held, where it has them. The hooks of a component that run
  script 44 give the sprite it shows on `onMouseLeave` or `onRelease`, or its own sprite, the one
  under the pointer on `onMouseOver` or `onMouseRepeat`, and the one held on `onClick`, `onHold` or
  `onClickRepeat`. The hooks that run script 4587 do the same for a component they name, and script
  4782 holds the plain sprite and the sprite under the pointer of four tabs as constants. A hook
  that gives -1 gives no sprite.
- `checkboxes` are the sets that scripts give script 4521: `box`, with `hoverBox` over it under
  the pointer and `pressedBox` over it while it is held down. Script 4519 gives the checkbox a
  tick and script 4520 leaves it empty, so a box that the player ticks is drawn from the set of
  the first and an empty one from the set of the second.
- `radioButtons` are the sets that scripts 1422 and 1423 hold as constants: the first component
  they are given, the one clicked, takes `selected`, and the others take `sprite`.
- `frames` are the boxes of the interfaces drawn as a frame: a sprite component at each corner,
  one along each side and, where there is one, one over the middle, each of a layer's own
  components. The task lays a layer's components out at two sizes with the client's rules
  (`InterfaceManager.resize`, `reposition`): a component that keeps its size and its place
  against two sides is a corner, one that grows along a side is an edge, and one that grows both
  ways is the fill. A layer with a corner or an edge missing, or with two components in one place,
  is not written. Each piece has its `sprite`, `mirrored` where it is drawn mirrored left to right
  (`Component.verticalFlip`), `flipped` where it is drawn upside down (`horizontalFlip`), and
  `tiled` where the sprite is tiled over it rather than stretched. `topLeft`, `topRight`,
  `bottomLeft` and `bottomRight` are each `x` and `y` from their two sides, `width` wide and
  `height` high. `top`, `bottom`, `left` and `right` each run from `start` after the first end of
  their side to `end` before the other, `inset` in from the side, `thickness` thick. `centre`
  keeps `left`, `top`, `right` and `bottom` from the sides.
- `hoverFrames` are the frames that scripts 4155 and 4158 build over a component, in the same
  form, read by playing back the components the script creates with the sprites each call gives
  it. Their pieces are clear until the pointer moves over the component, when script 4160 makes
  them opaque by 22 of 255 a client tick.

A script pushes a call's arguments just before it calls. Where it passes on one of its own
arguments, the task reads the calls of that script in turn, so a script that only forwards the
sprites does not hide them. A call whose sprites are known only while the game runs is counted
and not written. Each script that holds its sprites as constants is read by its shape, and the
task stops where a script no longer has the shape it reads. In this cache there are ten
scrollbars, four plate buttons, 107 sprite buttons, two checkboxes, one set of radio buttons,
70 frames and two hover frames,
and six calls of the scrollbar script are known only while the game runs. A planted check that
stops reading passed-on arguments leaves seven such calls, one more than the task finds, and a
planted check that expects five sprites in script 2975 stops the task.

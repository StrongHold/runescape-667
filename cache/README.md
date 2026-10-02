# cache

Reads the game's cache. It is a library as well as a set of tools. The scenes in `natives` are
drawn with models read from here, and the tools print what the cache holds for a person to read.


## What the cache holds

    ./gradlew :cache:censusTypes
    ./gradlew :cache:censusTypes --args="--type npc --type seq"

The census decodes every item of each config type that an exporter reads, with the client's own
reader, one opcode at a time. For each opcode it gives how many items hold it and which fields it
sets. An opcode that sets nothing is data the client reads and throws away. It also names any
item whose bytes are not all read, and the fields that no opcode sets, which the client works out
after decoding or which this cache never uses.

    ./gradlew :cache:censusLayouts
    ./gradlew :cache:censusLayouts --args="--format model"
    ./gradlew :cache:censusLayouts --args="--format location --keys /path/to/location-keys"

Models, animation bases and animation frames are not lists of opcodes, so they are checked a
different way. Each format is cut into the sections the client's reader works through, using the
sizes and counts its header gives, and a walker reads only what decides how far each section
goes: how many vertex deltas the vertex flags call for, how many face indices the face types call
for, which faces carry a texture space, and how many labels or transform values follow. Every
section has to stop exactly where the next one starts, and the last has to stop where the footer
starts, so nothing is left unread and nothing is read twice. The client's own reader decodes the
same item, and the counts it comes to are set against the walker's. The census names every item
that fails, with the section that did not end where it should and where it did end instead.

Map squares and the table of texture metrics are checked the same way. A square's terrain is
every tile of its four levels, each a run of codes ended by a code of 0 or by a height, and then
records of lighting, lights, bloom, sky box and camera heights that run on to the end of the data
with nothing to end them. A square's locations are runs of location numbers and the places each
one stands, each run ended by a step of 0, and the last step of 0 has to be the last byte. The
table of texture metrics is one column for each metric, with a value for each texture the table
holds, so how many it holds says how long every column is.

The client's own readers read each square as well. The tiles and the records after them are read
into regions of the client's own, with the software toolkit and the light and sky box types from
the cache, and what each tile is left holding is set against what the walker found. The reader of
locations keeps its position to itself, so it is given the locations cut off where the walker
stopped, which it has to read without failing, and cut one byte shorter, which it has to fail on.

The locations of a square are locked with a key that only the server holds, and are read from a
directory of one file of four numbers per square, named for the square, as `--keys` gives or else
`SW3D_LOCATION_KEYS`, or else the `game/share/location-keys` of a checkout of the server beside
this one. A square with no key is read as the client reads one it is handed a key of nothing for,
and a square that does not open that way is counted apart rather than failed. A locked group still
says in the open how long it is, so every stored map group is also checked to be its container and
its two byte version and nothing more.

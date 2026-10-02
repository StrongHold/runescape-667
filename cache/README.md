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

Models, animation bases and animation frames are not lists of opcodes, so they are checked a
different way. Each format is cut into the sections the client's reader works through, using the
sizes and counts its header gives, and a walker reads only what decides how far each section
goes: how many vertex deltas the vertex flags call for, how many face indices the face types call
for, which faces carry a texture space, and how many labels or transform values follow. Every
section has to stop exactly where the next one starts, and the last has to stop where the footer
starts, so nothing is left unread and nothing is read twice. The client's own reader decodes the
same item, and the counts it comes to are set against the walker's. The census names every item
that fails, with the section that did not end where it should and where it did end instead.

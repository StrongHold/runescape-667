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

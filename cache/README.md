# cache

Reads the game's cache. It is a library as well as a set of tools. The scenes in `natives` are
drawn with models read from here, and the tools print what the cache holds for a person to read.

| task | what it does |
|---|---|
| `listCacheLibraries` | Lists the native libraries the cache holds, for every platform. |
| `extractCacheLibrary` | Writes one named native library out of the cache. |
| `listTerrain` | Says what the map is made of on one tile. |
| `describeModel` | Says what one model out of the cache is made of. |
| `listLocType` | Lists the models one kind of location is built from. |
| `listLocations` | Lists the locations standing on one tile of the world. |

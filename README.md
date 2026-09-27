<p align="center">
    <a href="https://github.com/StrongHold/runescape-667?tab=readme-ov-file#readme" rel="noopener noreferrer">
        <img src="https://github.com/StrongHold/runescape-667/raw/master/logo.png" alt="RuneScape Logo" />
    </a>
    <br />
    <a href="https://github.com/StrongHold/runescape-667?tab=readme-ov-file#readme" rel="noopener noreferrer">
        <img src="https://github.com/StrongHold/runescape-667/raw/master/preview.png" alt="Preview" />
    </a>
</p>

# runescape-667

Client build [667][build] of [RuneScape 2][rs2], originally released on [2011-10-04][update].

## Getting Started

Run the client either directly via Gradle:

```bash
./gradlew client:run --args="--js5 '/path/to/js5.public.key' --login '/path/to/login.public.key'"
```

Or via the built distributable:

```bash
./gradlew installDist
./client/build/install/client/bin/client --js5 "/path/to/js5.public.key" --login "/path/to/login.public.key"
```

## Naming

Where possible, Jagex's canonical naming scheme is used for naming and packaging. These are sourced from various leaks
such as the partially-obfuscated Transformers Universe client and the NXT Beta client that contained debug symbols.

Original exception messages from these leaks have been restored, often shedding light on original naming schemes.

Native libraries are stored in the cache, which prevents various classes from being repackaged from the root or renamed.
These are classes that typically have `native` methods and one or two letter classnames.

## Changes from the original client

The `runescape` module computes what the 2011 jar computed. The code below is the only code in it
that is not in the jar, and each piece says so in its Javadoc or in a comment where it is called.
Keep this list up to date when you add or remove one.

| where | what it does | why |
|---|---|---|
| `rs2.client.loading.library.LibraryOverride`, `LibrarySource`, called from `Static14.loadNativeLibrary` | lets the application supply a native library in place of the one the client downloaded | loads the libraries in `natives`, which are the only ones that draw on current macOS |
| `com.jagex.graphics.sw.SoftwareToolkitLifetime`, called from `Static226.create` and `SoftwareMemoryManager.free` | keeps software toolkits alive instead of releasing them on the collector's thread | the shipped macOS software toolkit ends the process when it is released on another thread |
| `JavaScript.invokeOnWindow`, called from the three methods of `JavaScript` | reaches the browser window by reflection | the `JSObject` in current JDKs has no `getWindow`, so the direct call does not compile |

Some code differs from what the decompiler wrote so that it computes what the jar did. None of it
adds anything:

- Sums of floating point numbers are grouped as the jar grouped them. The decompiler dropped the
  parentheses, and floating point addition is not associative. `./gradlew :fidelity:verifyExpressions`
  checks every method against the jar.
- `Terrain.blendOverlay` reads the north edge split at `directionNorth & 3`. The jar negated the
  direction twice, and the decompiler wrote that as a decrement.
- `nativeid` on `oa` and `xa` is not `final`, as in the jar. As a `final` field given a constant,
  every read of it was the constant.

## Noteworthy

Below is a list of noteworthy parts of the client that have been refactored.

### CS2

The CS2 [`ScriptRunner`](runescape/src/main/java/ScriptRunner.java) is almost fully refactored, with most
[`ClientScriptOpcodes`](runescape/src/main/java/com/jagex/core/constants/ClientScriptOpCode.java) identified.

### JS5

The JS5 [networking layer](runescape/src/main/java/com/jagex/js5) is fully refactored, alongside all
major [JS5 config groups](runescape/src/main/java/com/jagex/game/runetek6/config).

### Audio

Both the [MIDI](runescape/src/main/java/com/jagex/sound/midi) & [Vorbis](runescape/src/main/java/com/jagex/sound/vorbis)
audio layers are fully refactored.

### Protocol

The [`ClientProt`](runescape/src/main/java/com/jagex/ClientProt.java),
[`ServerProt`](runescape/src/main/java/com/jagex/ServerProt.java), and
[`ZoneProt`](runescape/src/main/java/com/jagex/ZoneProt.java) are all refactored, identifying all packets sent across
the networking protocol.

### Path-finding and Collision

The [`PathFinder`](runescape/src/main/java/com/jagex/game/PathFinder.java) is fully refactored, with all
[`CollisionFlags`](runescape/src/main/java/com/jagex/game/collision/CollisionFlag.java) that affect path-finding
identified and utilised in the [`CollisionMap`](runescape/src/main/java/com/jagex/game/collision/CollisionMap.java).

### Updating Procedures

The player & npc updating procedures are refactored in [`PlayerList`](runescape/src/main/java/PlayerList.java) &
[`NpcList`](runescape/src/main/java/NPCList.java) respectively.

### UI

The [`Component`](runescape/src/main/java/Component.java) &
[`InterfaceManager`](runescape/src/main/java/InterfaceManager.java) are almost fully refactored.

Standalone components of the UI are also refactored:

- [`ClientInventory`](runescape/src/main/java/ClientInventory.java)
- [`debugconsole`](runescape/src/main/java/debugconsole.java)
- [`MiniMenu`](runescape/src/main/java/MiniMenu.java)
- [`Minimap`](runescape/src/main/java/Minimap.java)
- [`WorldMap`](runescape/src/main/java/WorldMap.java)

## Docs

1. [Command-line Interface](docs/cli.md)
2. [Applet](docs/applet.md)
3. [Parameters](docs/parameters.md)

## Credits

- The [Openrs2 Team](https://github.com/openrs2).
- Pazaz for fixing Openrs2's deobfuscator for 667, providing the basis of the deobfuscated client, and helping to fix
  various issues.
- Method for a refactored 666 client.
- Kris for various canonical Jagex terminology.
- Polar for various canonical Jagex terminology.

[rs2]: https://www.runescape.com/

[build]: https://runescape.wiki/w/Build_number

[update]: https://runescape.wiki/w/Update:Chat_Changes_%26_Camera_Controls

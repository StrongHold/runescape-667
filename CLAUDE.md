# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a deobfuscated and refactored RuneScape 2 client (build 667), originally released on 2011-10-04. The deobfuscation work continues from the OpenRS2 team. Jagex's canonical naming scheme is used where possible, sourced from leaks like the Transformers Universe client and NXT Beta client with debug symbols.

## Build Commands

```bash
# Run the client (requires RSA key files for connecting to a server)
./gradlew client:run --args="--js5 '/path/to/js5.public.key' --login '/path/to/login.public.key'"

# Build distributable
./gradlew installDist

# Run the runescape module directly (with default local args)
./gradlew runescape:run
```

The default run args are: `1 1000 local live english game0` (worldid, lobbyid, modewhere, modewhat, lang, game).

## Project Structure

Multi-module Gradle project with three modules:

- **client** - Application entry point (`Application.java`). Handles RSA key loading, argument parsing (JCommander), and window creation. Depends on `runescape` module.
- **loader** - Bootstrap applet (`loader.java`) that loads and decompresses the main client using Pack200/Zip.
- **runescape** - Core game code (~1000+ Java files). Contains all game logic, rendering, networking, and audio.

### Key Packages in runescape module

- `com/jagex/core/` - Core game functionality and constants
- `com/jagex/game/` - Game logic including pathfinding, collision, entities
- `com/jagex/graphics/` - Rendering pipeline
- `com/jagex/js5/` - JS5 file format and networking (fully refactored)
- `com/jagex/sound/midi/` and `com/jagex/sound/vorbis/` - Audio (fully refactored)
- `jaclib/`, `jagdx/`, `jaggl/` - Native library wrappers (hardware info, DirectX, OpenGL)

### Key Files

- `ScriptRunner.java` - CS2 script execution engine (almost fully refactored)
- `ClientProt.java`, `ServerProt.java`, `ZoneProt.java` - Network protocol definitions (fully refactored)
- `PathFinder.java`, `CollisionMap.java` - Pathfinding system (fully refactored)
- `PlayerList.java`, `NPCList.java` - Entity update procedures (fully refactored)
- `Component.java`, `InterfaceManager.java` - UI system (almost fully refactored)
- `ClientScriptOpCode.java` - CS2 opcode definitions

## Deobfuscation Rules

This client is deobfuscated incrementally. Renaming is the main activity, so these rules constrain what may be renamed.

### JNI bindings are frozen

The native libraries (`jaggl`, `jagdx`, `jaclib`, `jagtheora`, `jagex3.jagmisc`, `jagex3.graphics2.hw`) ship as
prebuilt binaries downloaded from the JS5 cache. They are not built from this repository, so their JNI symbols cannot be
regenerated. A JNI symbol encodes the package, the class name, and the method name of the Java declaration
(`Java_jaggl_OpenGL_glBindTexture`), and native code resolves fields and methods by literal string name through
`GetFieldID` and `GetMethodID`. Renaming or repackaging anything the binary binds to breaks the link at runtime, not at
compile time, so the build stays green and the client dies on first use.

Never rename, repackage, or change the signature of:

- Any class declaring a `native` method, and any package it lives in.
- Any `native` method, and the class that declares it.
- Any field a native library reads or writes, most notably the `peer` handle fields on `jaclib.peer.Peer`,
  `jagtheora.misc.SimplePeer`, and `jagex3.graphics2.hw.NativeInterface`.
- Any root-package class with a one or two letter name (`a`, `ba`, `oa`, `xa`, ...). These are native-bound and must
  stay in the default package under their original names.

Calling code is not frozen. A variable that holds an `IDirect3DDevice` may be renamed freely; the type itself may not.
When a rename would require touching a frozen declaration, leave the declaration alone and rename around it.

### The `@Original*` annotations are the source of truth

`@OriginalClass`, `@OriginalMember`, `@OriginalArg` and `@Pc` record the pre-deobfuscation identity of every element and
are what makes a rename auditable against the original bytecode. Renaming a Java element never changes its annotation.
Never delete, edit, or reorder the annotation strings.

### Renaming scope

Before renaming any class or any member reachable from outside its own file, confirm the full reference set first:

```bash
grep -rlw <name> --include=*.java runescape/src/main/java
```

Rename only when every reference is a file you are already changing. Names are recovered from the leaks described in the
README, so prefer a canonical Jagex name over an invented one, and leave a name obfuscated rather than guess at it.

### Existing bugs are preserved, never fixed

This client is a record of what Jagex shipped in 2011. A bug in the original code is part of that
record, so leave it exactly as it is. This holds for every kind of work in this repo, not only for
renaming waves.

Never "correct" any of the following, however obvious the intent looks:

- a tautological or contradictory test, such as `if (yB > yA && yC > yC)`
- an index, axis or argument that is plainly the wrong one
- arithmetic that extrapolates in the wrong direction, or an off-by-one
- a branch that no input can reach

A fix silently changes what the client computes, and the behaviour of the 2011 build is the only
thing that makes a refactor auditable. It also breaks the bytecode diff that proves a wave changed
names and nothing else.

You may record a suspected bug in a comment. Keep it to a statement of what the code does and why
it looks wrong, put it on the declaration rather than inline where possible, and never change the
code around it. Report it to the user as well, so they can decide.

**Every comment must read as if a client developer wrote it.** Write for someone who is reading this
source with no knowledge of how it was produced. Never mention this file, these rules, the
deobfuscation process, a rename, an `@Original*` annotation used as an audit trail, or what the code
looked like before. Say "this is kept on purpose, do not correct it", never "kept per the rule in
CLAUDE.md". This holds for every comment in the repo, not only a bug marker, and it compounds the
global rule that a comment never narrates the history of the code.

Note that an apparent bug is often an artefact of the decompiler or of OpenRS2's control flow
restructuring, not of the original. Check the pre-rename source with `git show <baseline>:<file>`
before you call anything a bug.

## Which modules are a record, and which are ours

`runescape` and `loader` are the 2011 client. They are a record of what Jagex shipped, so they
keep the idiom they were decompiled into: explicit types, no `var`, no records, no streams, no
enhanced `switch`. The `@Pc` annotations record real bytecode offsets against those declarations,
and a rewrite in a newer idiom breaks the bytecode diff that proves a change renamed things and
did nothing else. Improving the style of that code is not a goal and is not wanted.

`client`, `natives` and the build scripts are ours. Write them as current Java: `var` for locals
whose type is evident, records for data carriers, sealed interfaces, enhanced `switch`, text
blocks, the `java.nio.file` API. Follow the style rules that apply to any other new Java in any
other project.

The dividing line is the module, not the file. A new class added under `runescape` to support this
work still lives among the 2011 code and matches it.

## Technical Notes

- **Java version**: Targets Java 21, via the Gradle toolchain declared in each module
- **Native code**: Some classes are frozen by the shipped native binaries, see "JNI bindings are frozen" above
- **No tests**: This is reverse-engineered code without unit tests
- **Documentation**: See `docs/` for CLI args, applet params, and detailed parameter documentation
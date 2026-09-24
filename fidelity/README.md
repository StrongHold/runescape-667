# fidelity

Checks that the recompiled client computes what the 2011 jar computed.

The client in `runescape` was decompiled, recompiled, and then renamed one wave at a time. None of
those steps is meant to change what a method computes. The checks here read class files, not
source, so they compare what the virtual machine runs.

## verifyExpressions

    ./gradlew :fidelity:verifyExpressions
    ./gradlew :fidelity:verifyExpressions -Pclasses=JavaGround,Rasterizer

This runs each method on names instead of numbers. Every value that leaves the method becomes the
tree of operations that made it: a value put in a field, an array, a local or a call, a value
compared, or a value returned. The same happens to the method's original in the jar. The
`@OriginalMember` and `@OriginalArg` annotations give every field, call and parameter the name the
jar gave it, so the two sides use one set of names.

The two sides are put into one form before they are compared:

- Integer arithmetic is exact, so chains of additions, multiplications and bitwise operations are
  flattened and sorted.
- Floating point addition and multiplication are commutative but not associative. The two operands
  of each one are sorted, and the grouping stays as it was.
- Negation is exact, so `a + -b` and `a - b` are the same tree, and so are `-a * b` and `-(a * b)`.
- Comparisons that the decompiler turned round are turned back.
- Integer arithmetic wraps round, so a subtraction is the addition of a negation, constants in a
  sum are added up, and two negations cancel. A shift uses only the low bits of its distance, and a
  shift of a masked value is written as a mask of the shifted value when the two are the same.
- A floating point constant is exact to negate, so `x - 1.0F` and `x + -1.0F` are the same tree.
- A call to a method of the jar that only applies one instruction to its two parameters is written
  as that instruction. The obfuscator moved some ands into methods of their own.
- A value that reaches a use by more than one path on one side, because the jar reused a local,
  may stand for any value on the other side that rounds nothing.

The report lists, for each method, the lowest floating point nodes at which the two sides part.
The check fails when a method differs that `fidelity/expressions-outstanding.txt` does not list.
Each line there names a method known to compute the same as the jar, and says why. CI runs the
check on every push.
Pass `-Pwhole` to write out every tree that differs instead, and `-Pevery` with it to include
integer arithmetic.

A value that reaches an instruction by more than one path is written as `merged`. The tree does not
follow a loop round, so a difference inside a loop is found where the loop body computes it.

## verifyOpcodes

    ./gradlew :fidelity:verifyOpcodes -Pclasses=Terrain,Rasterizer

This compares only which opcodes that make a value each method uses. It is quicker to read than
the trees, but it cannot tell a harmless reordering of statements from a change in grouping.

## What it found

The decompiler dropped parentheses from sums of floating point numbers. The jar computed
`t + (a + b + c)`, and the source said `t + a + b + c`, which Java evaluates as `((t + a) + b) + c`.
For integers the two are the same. For floating point numbers they round differently, so a corner
of a tile could land one unit in the last place away from where the jar put it.

`verifyOpcodes` compares only which opcodes each method uses. It cannot tell this
change from a harmless reordering of statements. This check can.

## Differences that are not changes

Some differences are the deobfuscator's own work, and they compute the same answer:

- A parameter that every caller gave the same constant was removed, and the constant written in its
  place. The jar reads a variable where the source has a number.
- An `| 0xff000000` whose bits a later shift and mask discard was removed.

## The trace agent

    ./gradlew :native-trace:jar
    ./gradlew client:run -PnativeTrace=/tmp/ours.trace,classes=none,methods=JavaGround.U(II[I[I[I[I[I[I[I[IIIIZ)V
    ./gradlew :fidelity:diffNativeTraces -Pjar=/tmp/jar.trace -Pours=/tmp/ours.trace

The agent writes down calls a client makes, with every argument, to compare the recompiled client
with the jar while both run. Pass it to the jar's client with `-javaagent:` in the same way. The jar
needs Java 11, because its loader uses Pack200, so the agent is built for Java 11.

After the file, the settings are:

- `classes=t:ja` watches every native method of the classes named. This is the default.
- `methods=<class>.<name><descriptor>;...` watches Java methods by name. The jar and the recompiled
  client name the same method differently, so each run names its own.
- `every=<class>` watches every method of a class.
- `calls=<n>` sets how many calls of one method are written down. The default is 20000.

A Java method's record names the object, the thread and the caller. It also has the object's
simple fields whenever they change, and any object or array the method hands back.

Compare what is built once, such as the tiles of the ground. Two runs look from two cameras, so
calls made every frame seldom match one for one.

## What the agent found

The client drew lines of gaps between tiles, and the jar did not. The two clients built about 200
tiles in an area differently. The jar read an edge split at `-(-direction) & 3`. The decompiler
wrote that as `--direction & 3`, which in Java is a decrement. A double negation in the jar is worth
checking in the source wherever it appears.

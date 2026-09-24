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

## Comparing two running clients

`diffNativeTraces` compares two records written by the agent in `native-trace`. See its README.

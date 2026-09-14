---
name: verify-refactor
description: Prove that a deobfuscation wave changed only names, by comparing the compiled bytecode of every changed class against a baseline commit. Use for "did we break anything", "is the refactor safe", "verify the rename", "check the last wave", or before pushing deobfuscation commits.
---

# Verify a refactor

```bash
java .claude/skills/verify-refactor/Verify.java <baseline-ref>   # default HEAD~1
```

One Java source file, run by the JDK source launcher, so there is nothing to build
and nothing to install. It resolves `javap` and Gradle's JDK from `java.home`, which
is whichever JDK mise activated to launch it, so the pinned toolchain applies and the
disassembler always matches the compiler.

Prints one line per changed class: `IDENTICAL`, `CHANGED`, or `MISSING`.

## What this proves, and what it does not

A deobfuscation wave is meant to rename things and nothing else. A rename cannot
change what the JVM does, so the bytecode of a renamed class must stay
semantically identical to the bytecode of that class before the rename. This
compiles both and compares them.

It proves the code still computes what it computed at the baseline. It does
**not** prove the new names are correct, and it does **not** prove JNI safety: a
rename that breaks a native symbol produces perfectly valid bytecode and dies at
run time. Only the user running the client tests that. See "JNI bindings are
frozen" in `CLAUDE.md`.

## Why a commit, not the original jar

`lib/runescape.jar` holds the real 2011 classes, and the `@OriginalClass` and
`@OriginalMember` annotations map every element back to them. That is tempting
but it does not work for this purpose: OpenRS2 already restructured the control
flow before this repo's first commit, so the original bytecode never matched the
repo's bytecode and a diff against it is all noise. The jar can confirm a
signature; it cannot confirm a body.

A baseline commit does work, because a pure rename is expected to compile to the
same bytecode, and equality composes. If every wave is identical to the wave
before it, the newest tree is equivalent to the first commit.

## What gets normalised

Four differences follow from a rename and are removed before comparing:

- identifier text, in declarations and in method and field references
- constant pool indices, and the `ldc` / `ldc_w` / `goto` / `goto_w` width that
  follows from them
- absolute bytecode offsets, including branch targets, which shift whenever an
  instruction above them changes width
- local variable slot numbers, which javac assigns by declaration order

Slot numbers are renumbered by first use rather than erased, in `load`, `store`,
`iinc` and `ret` alike. That keeps the aliasing structure, so a rename that
swapped two variables still shows up as a difference. Erasing them instead is a
real trap: an earlier draft of this tool did that and reported a class as
identical when the new build had an extra instruction in it.

## Reading a CHANGED result

`CHANGED` means the edit was more than a rename. It is not proof of a bug, and
it needs a person to read the source diff. The two innocent causes seen so far:

- **A declaration moved into a narrower scope.** The obfuscator often gave one
  bytecode slot to the inner counter of one loop and the outer counter of the
  next, so naming them means splitting the declaration. Confirm every loop sets
  its counter in the `for` statement, which proves no value carried across.
- **A redundant statement removed**, such as a `return` that was already the last
  statement, or a test nested directly inside an identical test. Confirm nothing
  between the two tests changed the value.

Anything else, treat as a defect until it is explained.

## Where it fits

Run it in step 4 of the `deobfuscate` skill, after the annotation and grep
checks and before the commit. The annotation check catches an edited
`@Original*` string; this catches a changed body. Neither catches the other.

On a wave of 25 changed files it takes about 3 minutes, most of it the two
Gradle builds.

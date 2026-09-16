---
name: deobfuscate
description: "Run a parallel deobfuscation wave over this client. Picks clusters of obfuscated classes with disjoint reference sets, gives each to its own subagent, verifies the results against the JNI and annotation rules, and commits one cluster per commit. Use for 'deobfuscate', 'refactor some classes', 'do another refactoring wave', 'name some more classes'. Takes a cluster count and an optional token ceiling."
version: 1.0.0
---

# Deobfuscation Wave

Run N independent refactoring agents over N clusters of obfuscated classes, then verify and commit.

## Arguments

`$ARGUMENTS` is optional and free-form. Parse these out of it:

| Form | Meaning | Default |
|---|---|---|
| a bare number (`4`) | how many clusters to do | 4 |
| `--budget 800k` / `--budget 2M` | stop launching new waves once cumulative subagent tokens pass this | none |
| `--area <hint>` | restrict candidate clusters to an area (`graphics`, `audio`, `ui`, `net`, `scene`) | whole module |
| `--dry-run` | survey and propose clusters, then stop without spawning | off |

Read a bare number as clusters, never as tokens or minutes.

## Why clusters, not tokens or time

Tokens and wall-clock are outcomes you can only measure after an agent finishes, so neither can be
targeted up front. Cluster count is the only dial that is both controllable and meaningful, because
one cluster is also one commit. Calibration from the first wave of this repo:

| Cluster | Files | Subagent tokens | Wall clock |
|---|---|---|---|
| `Class51` texture family | 5 | 138k | 10 min |
| `Class26` render passes + 3 toolkits | 15 | 197k | 15 min |
| `Class101` texture effects | 11 | 218k | 20 min |
| `MapRegion` terrain + scene locs | 7 | 274k | 18 min |

So one cluster costs roughly 200k tokens and 15 minutes, and the wave runs in the time of its
slowest agent. Four clusters is a comfortable default. If the user gives a token or dollar budget
anyway, convert it at 200k tokens per cluster, say you did, and proceed.

## Procedure

### 1. Baseline

Confirm the tree is clean and the module compiles before touching anything. A later failure is only
attributable if the start was green.

```bash
git status --porcelain
zsh -lc 'eval "$(/opt/homebrew/bin/mise activate zsh)"; ./gradlew runescape:compileJava --console=plain'
```

### 2. Pick clusters with disjoint file sets

This is the step that makes or breaks the wave. Two agents editing one file concurrently will clobber
each other, so **every cluster must own a set of files no other cluster touches.**

**Exclude the hardware renderers first, before looking at anything else.** Development happens on
macOS, where the client offers only software rendering, so `JavaToolkit` is the only toolkit that
ever loads. A rename in GL or D3D code compiles green, passes every static check, and cannot be
exercised by the user at all. Drop any candidate reachable only from `GlToolkit`, `D3DToolkit` or
`NativeToolkit`: the GL and D3D passes and effects, the hardware texture and buffer wrappers, and the
hardware water path (the `Native*` and `Gl*` leaf classes). Note `D3DToolkit extends NativeToolkit`,
while `GlToolkit` and `JavaToolkit` extend `Toolkit` directly, so `NativeToolkit` is the D3D base and
not a general hardware base.

Everything renderer-agnostic stays in scope, which is most of the module: the texture op pipeline
(`Texture.java` drives it for every renderer), model and scene data, protocol, UI, scripts,
pathfinding and audio.

Find candidates:

```bash
cd runescape/src/main/java
for f in Class*.java Static*.java; do echo "$(wc -l < $f) $f"; done | sort -rn | head -40
```

Then compute the full reference set of each candidate and check the sets do not intersect:

```bash
grep -rlw <ClassName> --include=*.java runescape/src/main/java
```

Watch for these traps, all of which cost time in the first wave:

- `grep -w Class26` does **not** match `Class26_Sub1`, because `_` is a word character. Always grep each subclass separately or the reference set looks smaller than it is.
- A family that looks self-contained is often constructed from a toolkit or a manager. That constructing file joins the cluster.
- If two clusters both need one shared file, either merge them into one cluster or drop one. Do not split a file between agents.

A good cluster is an inheritance family (a base plus its `_Sub` classes) plus whatever constructs it.
5 to 15 files is the right size.

### 3. Brief the agents

Launch all agents in a single message so they run concurrently. Name each agent after what it is
looking into, not after the class number. Every prompt must carry:

1. **The exclusive file list**, and a statement that editing anything outside it is forbidden.
2. **A note that other agents are working concurrently on other files.**
3. **The JNI rule**, pointing at `## Deobfuscation Rules` in `CLAUDE.md`, and the fact that a green build is not evidence of JNI safety because a bad rename fails only at runtime.
4. **The annotation rule**: `@OriginalClass`, `@OriginalMember`, `@OriginalArg` and `@Pc` strings are never edited, reordered or deleted; a rename leaves them untouched.
5. **The reference-set rule**: run the grep above before renaming any class or any member reachable from outside its own file, and rename only when every hit is a file the agent owns. Leave a name obfuscated rather than guess.
6. **No git writes.** Read-only git only. This session commits.
7. **Hot-path warning**: rendering and scene code must not gain allocations, streams or boxing. Renaming is the deliverable; restructuring hot loops is not.
8. **Build sparingly**: at most 3 or 4 compiles, at the end, because concurrent agents queue on the Gradle lock.
9. **Existing bugs stay**, pointing at `### Existing bugs are preserved, never fixed` in `CLAUDE.md`. A tautological test, a wrong index or axis, an off-by-one: leave every one of them alone. A comment recording the suspicion is allowed, a code change is not. Say that a fix breaks the bytecode diff that proves the wave renamed and nothing else.
   Add that any comment it writes must read as if a client developer wrote it: no mention of `CLAUDE.md`, of this wave, of a rename, or of what the code looked like before. The reader must not be able to tell that tooling touched the file.
10. **What to report**: old to new name mapping, one line on what the family does, build result, anything left alone because it crossed a file boundary, and any suspected original bug found on the way.

The style rules in `CLAUDE.md` and `~/.claude/rules/java.md` load on their own. Do not paste them
into the prompt beyond the hot-path carve-out, which is specific to this repo.

### 4. Verify before believing

Agents report their own success. Check it independently. Run all of these over the changed files:

```bash
# Annotations byte-identical to HEAD, in content and in order, for every changed file.
# Match the whole annotation. Anchoring on '("' sees only @OriginalClass, because
# @OriginalMember writes (owner = "...") with the argument name before the quote, and
# @Pc(35) and @OriginalArg(0) hold no string at all.
# Do not sort: the rule forbids reordering, and sorting hides exactly that.
ANN='@(OriginalClass|OriginalMember|OriginalArg|Pc)\([^)]*\)'
for f in $(git status --porcelain | grep -E '^ ?M' | awk '{print $2}'); do
  a=$(git show HEAD:$f | grep -oE "$ANN" | md5)
  b=$(grep -oE "$ANN" $f | md5)
  [ "$a" = "$b" ] || echo "ANNOTATION MISMATCH: $f"
done

# A renamed file has no path of its own at HEAD, so pair it with its old name by hand
a=$(git show HEAD:$OLD | grep -oE "$ANN" | md5)
b=$(grep -oE "$ANN" $NEW | md5)
[ "$a" = "$b" ] || echo "ANNOTATION MISMATCH: $OLD -> $NEW"

# Guard: d41d8cd98f00b204e9800998ecf8427e is the md5 of empty input. If both sides
# hash to it the pattern matched nothing, so the check passed without proving anything.

# Nothing in a frozen package changed
git status --porcelain | grep -E 'jaggl|jagdx|jaclib|jagtheora|jagex3'

# No new native declarations, no labels, no dashes
grep -Hn ' native ' $CHANGED
grep -Hn -E '^\s*[a-zA-Z]+[0-9]+:\s*$' $CHANGED
grep -lP '[\x{2013}\x{2014}]' $CHANGED

# No duplicate class names, in case two agents chose the same one
ls runescape/src/main/java/*.java | xargs -n1 basename | sort | uniq -d
```

Then **read every semantic rewrite by hand.** A compiler cannot tell you these are equivalent, and
agents do attempt them:

- a labelled `break` replaced by a flag
- scrambled statements collapsed into a loop (check the index set covers exactly the same elements)
- a dead local removed (check any side effect in the dropped expression survived, such as `x = arr[i]++`)
- **an original bug quietly corrected.** Agents do this without being asked, because the intent looks
  obvious. Any change that makes the code more sensible is a defect here. Check it against
  `git show <baseline>:<file>` and restore the original behaviour. The bytecode diff in step 4 catches
  these, which is the main reason to run it.

If an agent left a handover, because a rename crossed into a file another agent owned, apply it
yourself once both agents have finished.

**`sed -E` on macOS does not support `\b` and silently does nothing.** Use `perl -pi -e` for
word-boundary renames.

### 5. Commit

One commit per cluster. When two clusters share a file and cannot be separated, squash them into one
commit rather than leaving a commit that does not build.

Verify bisectability rather than assuming it:

```bash
git worktree add -q --detach /tmp/bisect <sha>
# compile there, then: git worktree remove --force /tmp/bisect
```

Write commit messages with the `asd-ste100` skill in STE-flavored mode, per the global rules. Title
is a plain sentence, no conventional-commit prefix, no `Co-Authored-By`. Body: what the family is,
the old to new mapping as a list, and what was deliberately left alone.

Do not push. Renaming past the JNI line compiles green and dies at runtime, so the only real test is
the user running the client. Report that the commits are local and let them push.

## Reporting back

Give the user, in this order: what each cluster became, the verification table, anything left
obfuscated on purpose, and the count of `Class*`/`Static*` files still at the repo root so they can
judge the next wave:

```bash
ls runescape/src/main/java/*.java | grep -cE '/(Class|Static)[0-9]'
```

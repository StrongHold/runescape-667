"""
Compares recompiled classes against the same classes in the jar this source was recovered from.

This answers one question: did a wave of renaming, or the round trip through a decompiler, change
what the code computes? Nothing else in the build can answer it. The source no longer resembles
the jar, so the two cannot be matched by name, and the obfuscator's dummy parameters were removed,
so they cannot be matched by descriptor either. The @OriginalMember annotations record what every
member used to be called, and they are the only thing that still relates the two.

Only the opcodes that decide a value are compared. The deobfuscator inverts branches, reorders
independent statements, removes dead arithmetic on dummy parameters and unwinds the obfuscator's
comparison tricks. None of that changes what a method computes and all of it shows up in a plain
diff, which is why a plain diff of these classes says nothing.

Two things this cannot tell apart, so read a report with them in mind:

  - Independent statements evaluated in a different order look the same as arithmetic that has
    been re-associated. The first is harmless, the second is not, and both appear as a hunk with
    the same number of opcodes on either side.
  - A method the annotations cannot match is not compared at all. The count of those is reported
    rather than hidden.

Usage: fidelity.py -- <source.java> <original.class> <recompiled.class> [...]
"""

import collections
import difflib
import re
import subprocess
import sys

# The opcodes that decide a value. Loads, stores, branches and stack shuffling are all
# restructured without changing an answer, so they are left out.
VALUE = frozenset("""
    idiv ishr iushr ishl irem imul isub iadd ineg iand ior ixor
    i2s i2c i2b i2l i2f i2d f2i f2l f2d d2i d2f l2i
    fadd fsub fmul fdiv fneg dadd dsub dmul ddiv
    ladd lsub lmul ldiv lshl lshr lushr
""".split())

MEMBER = re.compile(r'^  \S.*;\s*$')
OPCODE = re.compile(r'^\s+\d+: (\w+)')
NAME = re.compile(r'([\w$<>]+)\(')
ANNOTATED = re.compile(
    r'@OriginalMember\(owner = "[^"]+", name = "([^"]+)", descriptor = "([^"]+)"\)'
    r'(.*?)\n\s*(?:public|private|protected|static|final|abstract|native|synchronized|\w)'
    r'[^\n(]*?([\w$<>]+)\s*\(',
    re.S)

SAMPLE = 30


def methods(path):
    """Every method of a class, as the sequence of value opcodes it runs."""
    text = subprocess.run(['javap', '-p', '-c', '-s', path],
                          capture_output=True, text=True, check=True).stdout
    found = []
    held = None

    for line in text.splitlines():
        if MEMBER.match(line):
            held = {'signature': line.strip(), 'descriptor': None, 'opcodes': [],
                    'method': '(' in line}
            found.append(held)
        elif held is None:
            continue
        elif line.strip().startswith('descriptor:') and held['descriptor'] is None:
            held['descriptor'] = line.split('descriptor:', 1)[1].strip()
        else:
            opcode = OPCODE.match(line)
            if opcode is not None and opcode.group(1) in VALUE:
                held['opcodes'].append(opcode.group(1))

    return [one for one in found if one['method']]


def byName(found):
    held = collections.defaultdict(list)
    for one in found:
        name = NAME.search(one['signature'])
        held[(name.group(1) if name else '?', one['descriptor'])].append(one)
    return held


def compare(source, original, recompiled):
    """Reports every method whose arithmetic changed. Answers how many did."""
    text = open(source).read()

    wanted = {}
    for held in ANNOTATED.finditer(text):
        if '(' in held.group(2):
            wanted[held.group(4)] = (held.group(1), held.group(2))

    before = byName(methods(original))
    after = byName(methods(recompiled))

    same = differ = unmatched = 0
    lines = []

    for ours, (was, descriptor) in sorted(wanted.items()):
        mine = [one for (name, _), held in after.items() if name == ours for one in held]
        theirs = before.get((was, descriptor), [])

        if len(mine) != 1 or len(theirs) != 1:
            unmatched += 1
        elif theirs[0]['opcodes'] == mine[0]['opcodes']:
            same += 1
        else:
            differ += 1
            hunks = list(difflib.unified_diff(theirs[0]['opcodes'], mine[0]['opcodes'],
                                              lineterm='', n=0))[2:]
            lines.append('    %s, was %s%s' % (ours, was, descriptor))
            lines.append('      ' + ' '.join(hunks[:SAMPLE]))

    return same, differ, unmatched, lines


def main(arguments):
    if arguments[:1] == ['--']:
        arguments = arguments[1:]

    if not arguments or len(arguments) % 3 != 0:
        print(__doc__)
        return 2

    same = differ = unmatched = 0

    for at in range(0, len(arguments), 3):
        source, original, recompiled = arguments[at:at + 3]
        theseSame, theseDiffer, theseUnmatched, lines = compare(source, original, recompiled)

        same += theseSame
        differ += theseDiffer
        unmatched += theseUnmatched

        if lines:
            print('%s:' % source.rsplit('/', 1)[-1])
            print('\n'.join(lines))

    print('%d methods compute what they did, %d differ, %d could not be matched'
          % (same, differ, unmatched))
    return 0


if __name__ == '__main__':
    sys.exit(main(sys.argv[1:]))

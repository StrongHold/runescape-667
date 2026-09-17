# jagmisc

The odds and ends library. Six natives on `jagex3.jagmisc.jagmisc`: a monotonic clock, the total
and available size of physical memory, a ping, and the pair that start and stop the library.

Nothing in it belongs with anything else in it. It is where the client put the three questions
that need the operating system and have no other home.

## What it stands in for

Nothing that runs here. The game's own file store holds jagmisc for `windows/x86`,
`windows/x86_64` and `windows/msjava`, and for no other platform, so there has never been a copy
of this library for macOS or for Linux. The client has always run without it on both.

It runs without it because it is built to. Every call is made inside a catch of `Throwable`:

- `TickScheduler.create` builds a `NativeTickScheduler` on the first reading of the clock and
  falls back to one built on `System.nanoTime` when that throws.
- `Loading` calls `init` and discards what it says.
- `PingWorker` turns any exception from a ping into a flat thousand.
- The debug console is the only reader of either memory size.

So this is not a replacement for a library that was failing. It is the first build of jagmisc for
this platform, and what the client gains by it is a clock it can pace ticks against and a world
list it can order by round trip.

## What each one answers

`nanoTime` is `CLOCK_MONOTONIC`. Zero is reserved: it is how a machine with no such clock says so,
and the client's scheduler throws on a first reading of zero and goes back to the virtual
machine's own clock. A clock that works counts from the last boot, so it will not read zero again
this side of one.

`init` reports whether that clock can be read. `Quit0` releases nothing, because nothing here
outlives the call that made it.

`getTotalPhysicalMemory` is `hw.memsize`.

`getAvailablePhysicalMemory` counts free, inactive and purgeable pages. Free pages alone would be
wrong: this system keeps what it has read from disk in pages it marks inactive and gives them back
the moment anything asks, so a machine that has been up an hour has almost nothing free while most
of memory is in fact available.

`ping0` sends one ICMP echo and returns the round trip in milliseconds. The socket is a datagram
one rather than a raw one, so it needs no privilege. Two things follow from that: the kernel puts
its own identifier into the packet, so the reply is matched on its sequence number alone, and the
reply arrives with the IPv4 header left on by some systems and stripped by others, so a header is
skipped where one is found rather than assumed either way.

A value below zero means there was no round trip. Which value is ours to choose, because all the
client reads is the sign, and the original's codes cannot be recovered from a Windows DLL that
does not run here. The two are apart only so that a machine which cannot open the socket can be
told from one that waited and heard nothing.

## How it is checked

    ./gradlew :natives:verifyOddsAndEnds

Every other library here is held against the shipped one by driving both and comparing. This one
has no shipped copy to drive, so each answer is held against a second way of asking the machine
the same question instead:

| answer | held against |
|---|---|
| `nanoTime` | `System.nanoTime` over the same interval, and itself, for going backwards |
| `getTotalPhysicalMemory` | `sysctl -n hw.memsize` |
| `getAvailablePhysicalMemory` | above zero and no larger than the total |
| `ping0` | the loopback address answers, and an address routed nowhere does not |

The address routed nowhere is `192.0.2.1`, the first of TEST-NET-1, which is set aside for
documentation. How long that ping took is checked as well as what it said, because a ping that
gives up has to do so of its own accord and at the time it was asked to.

This is weaker than a picture compared pixel by pixel and it is as strong as this library allows.
A second reading of the same clock is a real check on the first; it just cannot say that the
original answered the same way.

## Carrying it to other platforms

The clock and the ping are POSIX and should build for Linux unchanged. The two memory sizes are
the part written for this system: `sysctl` and `host_statistics64` become `/proc/meminfo` on Linux
and `GlobalMemoryStatusEx` on Windows. They are the only two, and each is a handful of lines.

Windows is also the one platform where the original can finally be measured against, because it is
the only one it was ever built for.

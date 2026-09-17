# jaclib

Memory the client manages itself, and the survey it sends about the machine. Nineteen natives are
declared across `jaclib.memory`, `jaclib.memory.heap`, `jaclib.peer` and `jaclib.hardware_info`.

## What it stands in for

`libjaclib.dylib`, which the game's file store holds for `macos/x86`, `macos/x86_64`,
`macos/universal` and `macos/ppc`. It is the library the hardware toolkits cut their vertex and
index buffers out of, and it is loaded before either of them: `hw3d` will not load without it.

## How far along it is

Sixteen of the nineteen are written, and that is all of them. The shipped macOS library exports
sixteen JNI symbols, the same sixteen, and the other three are not among them:

| not written | reached through |
|---|---|
| `IUnknown.AddRef` | every `jagdx` interface, which all extend `IUnknown` |
| `IUnknownReference.releasePeer` | the same |
| `NativeHeapPeerReference.releasePeer` | `jaclib.peer.os`, whose four subclasses are all `jagdx` |

All three are Direct3D. `PeerReference` calls `releasePeer` only where a peer was set, and nothing
outside `jagdx` ever sets one, so on this platform none of the three can be reached. The shipped
library agrees, by not defining them.

So jaclib is finished for everything that is not Windows. What is left is not a gap in this work.
It is the shape of the library.

## How it is checked

    ./gradlew :natives:verifyMemoryAnswers

Both libraries are driven through the same script and every answer compared. The shipped one is
thinned to its x86_64 slice and its JNI import pointed at the shim first, for two reasons: it asks
JavaVM.framework for its entry points exactly as the software toolkit does, and `install_name_tool`
cannot read the ppc slice it still carries. Both sides then run under the x86_64 virtual machine,
because only one of them has a slice for anything else.

An address cannot be compared, because no two libraries malloc in the same place. What is compared
instead is where a buffer sits inside its heap, measured from the first buffer of that heap. That
records the size of a buffer's header, how one is aligned, where the next begins, and what moves
when the holes are closed, and none of it depends on where the heap landed. Seventy one answers,
all identical.

`verifyMemoryLibrary` is separate and still runs. It exercises ours on its own, on this machine's
own architecture, which the comparison cannot do.

## Where it does not agree, and why

**The survey the client sends at login.** `getCPUInfo` returns seven fields. Six describe an x86
processor and are zero here, because there is none. The seventh is the size of memory, and the
shipped library's answer for it cannot be matched, because there is no answer there to match:

```
mib[0] = 6     CTL_HW
mib[1] = 0x18  HW_MEMSIZE
oldlen = 2                     <- the size of the name, not of the answer
callq  _sysctl
movq   -0x30(%rbp), %rax       <- read regardless
shrq   $0x14, %rax             <- bytes to megabytes
```

It declares an output buffer of two bytes where eight are wanted, so `sysctl` fails with `ENOMEM`
and writes nothing, and the shift is applied to whatever the stack happened to hold. The number
changes on every run and bears no relation to the machine: three runs in a row gave 133708224,
133781352 and 133975824 megabytes on a machine with 36864. The client sends that to the server as
its memory size and prints it in the debug console.

This one reports what the kernel says. The check therefore records only how many fields come back
and never what is in them, because demanding equality here would be demanding it of uninitialised
memory.

**A heap deallocated twice.** The shipped library frees the same block a second time and the
process aborts. This one leaves the second call alone and returns. A crash is not a behaviour to
reproduce, and the client never makes the call: `NativeHeap.b` sets its own flag before calling
through, so the path is closed on the Java side. It is out of the comparison because a shipped
side that aborts records no answers at all.

## Faults in the original

Two, both above: the memory size read through a buffer declared too small, and the double free.
Neither is reachable from the client as it stands. The first is only sent to the server, and the
second is guarded before it is called.

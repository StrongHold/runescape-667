# jawtshim

Not one of the libraries being replaced. The client never loads this, and nothing that ships to a
player contains it. It exists so that the shipped toolkits can still be driven by the checks.

## Why it has to exist

Both shipped toolkits ask `JavaVM.framework` for a drawing surface, the software one at
`JAWT_VERSION_1_3` and the hardware one at `JAWT_VERSION_1_4`. That framework now serves only
`JAWT_VERSION_1_7`, so both requests fail and neither toolkit can obtain a surface on a current
JDK. Each imports `JAWT_GetAWT` and nothing else from the framework, so the build copies the
shipped library and repoints that one import at this shim:

    install_name_tool -change <JavaVM> @loader_path/libjawtshim.dylib <the copy>

The shipped library is then loadable, and the checks can draw the same scene through it and
through ours and compare the two pictures.

## How long it stays

Indefinitely. It is easy to read this as scaffolding from the port and assume it comes out when
the renderer is finished, but that has the relationship backwards. Our renderer is finished when it
matches the shipped one, and it goes on being finished only for as long as something keeps
checking that it still matches. The shim is what makes that check possible, so it lives as long as
the check does.

It would go only if the shipped binaries stopped being the specification: if every native were
written and the comparison retired in favour of some other way of knowing the toolkits are right.
Nothing here plans that.

## What the client uses instead

Nothing. `sw3d/surface.m` asks the JDK's own JAWT for the modern version through the running
virtual machine, rather than linking against the framework the way the 2011 binary did. That is
why the client on Apple Silicon no longer needs a shim at all, and why this one is only ever
loaded by a copy of a binary that is not ours.

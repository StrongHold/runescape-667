# native-trace

A Java agent that writes down the calls a client makes, with every argument. Run the recompiled
client and the jar with it, then compare the two records.

    ./gradlew :native-trace:jar
    ./gradlew client:run -PnativeTrace=/tmp/ours.trace,classes=none,methods=JavaGround.U(II[I[I[I[I[I[I[I[IIIIZ)V
    ./gradlew :fidelity:diffNativeTraces -Pjar=/tmp/jar.trace -Pours=/tmp/ours.trace

`-PnativeTrace` adds the agent to every task that runs Java. Pass it to the jar's client with
`-javaagent:native-trace/build/libs/native-trace.jar=<file>[,settings]`. The jar needs Java 11,
because its loader uses Pack200, so the agent is built for Java 11.

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

## What it found

The client drew lines of gaps between tiles, and the jar did not. The two clients built about 200
tiles in an area differently. The jar read an edge split at `-(-direction) & 3`. The decompiler
wrote that as `--direction & 3`, which in Java is a decrement. A double negation in the jar is worth
checking in the source wherever it appears.

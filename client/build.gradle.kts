plugins {
    application
}

dependencies {
    implementation(project(":runescape"))
    implementation(libs.bouncycastle.bcpkix)
    implementation(libs.jcommander)
}

/**
 * How much heap the client is given, and whether it says what the collector is doing.
 *
 * The applet was given two hundred and fifty six megabytes and that is what it gets here, because
 * what it does with what it has is part of what is being kept. A collection of a heap that small
 * stops the client for as long as it takes, which is long enough to see, so `-PclientHeap=1g`
 * raises it and `-PclientGcLog` says when a collection happens and how long it took. Between them
 * a freeze can be laid at the collector's door or taken away from it.
 */
val clientHeap = providers.gradleProperty("clientHeap").getOrElse("256m")
val clientGcLog = providers.gradleProperty("clientGcLog").isPresent

application {
    mainClass = "Application"
    applicationDefaultJvmArgs = buildList {
        add("-Xmx$clientHeap")
        add("-Dsun.java2d.noddraw=true")
        if (clientGcLog) {
            add("-Xlog:gc")
        }
        add("--add-opens")
        add("java.base/java.lang=ALL-UNNAMED")
    }
}

/**
 * Switches that change what the renderers do, so that what draws a given pixel can be found by
 * elimination. Each is forwarded from the shell that starts the build rather than inherited,
 * because the build daemon outlives the shell and keeps the environment it started with.
 *
 * The first five take a layer of the software renderer out of the picture. The last two belong to
 * the OpenGL binding: one asks it to say what it is doing, and the other to say where a frame's
 * time went.
 */
val rendererSwitches = listOf(
    "SW3D_NO_GROUND",
    "SW3D_NO_MODELS",
    "SW3D_NO_GROUND_SHADOW",
    "SW3D_GROUND_UNTEXTURED",
    "SW3D_GROUND_TALLY",
    "JAGGL_VERBOSE",
    "JAGGL_TIMING",
)

tasks.named<JavaExec>("run") {
    for (name in rendererSwitches) {
        val said = providers.environmentVariable(name)
        if (said.isPresent) {
            environment(name, said.get())
        }
    }
}

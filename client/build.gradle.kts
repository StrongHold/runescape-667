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
 * elimination.
 *
 * Every variable whose name starts with one of the prefixes below is forwarded to the client,
 * rather than a fixed list of them. A switch written into the renderer and left out of a list here
 * is a switch that does nothing, and a run that quietly measures the unswitched renderer is worse
 * than no run at all.
 *
 * They are forwarded rather than inherited because the build daemon outlives the shell that
 * started it and keeps the environment it was started with. `-Pswitch=NAME,NAME` says the same
 * thing through the build itself, for a daemon that cannot be persuaded to see the shell.
 */
val switchPrefixes = listOf("SW3D_", "JAGGL_")

val switchesFromShell = switchPrefixes.map { providers.environmentVariablesPrefixedBy(it) }

val switchesAsked = providers.gradleProperty("switch").map { said ->
    said.split(",").map(String::trim).filter(String::isNotEmpty)
}.getOrElse(emptyList())

tasks.named<JavaExec>("run") {
    for (found in switchesFromShell) {
        for ((name, said) in found.get()) {
            if (said != null) {
                environment(name, said)
            }
        }
    }

    for (name in switchesAsked) {
        environment(name, "1")
    }
}

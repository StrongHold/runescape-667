plugins {
    application
}

dependencies {
    implementation(project(":runescape"))
    implementation(libs.bouncycastle.bcpkix)
    implementation(libs.jcommander)
}

application {
    mainClass = "Application"
    applicationDefaultJvmArgs = listOf(
        "-Xmx256m",
        "-Dsun.java2d.noddraw=true",
        "--add-opens",
        "java.base/java.lang=ALL-UNNAMED",
    )
}

/**
 * Switches that change what the renderers do, so that what draws a given pixel can be found by
 * elimination. Each is forwarded from the shell that starts the build rather than inherited,
 * because the build daemon outlives the shell and keeps the environment it started with.
 *
 * The first five take a layer of the software renderer out of the picture. The last two belong to
 * the OpenGL binding: one asks it to draw with the samples a pixel the client asked for, which is
 * not switched on by itself yet, and the other asks it to say what it is doing.
 */
val rendererSwitches = listOf(
    "SW3D_NO_GROUND",
    "SW3D_NO_MODELS",
    "SW3D_NO_GROUND_SHADOW",
    "SW3D_GROUND_UNTEXTURED",
    "SW3D_GROUND_TALLY",
    "JAGGL_SAMPLES",
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

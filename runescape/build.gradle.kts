plugins {
    application
}

dependencies {
    implementation(files("lib/stub.jar"))
    implementation(libs.openrs2.deob.annotations)
}

/**
 * The native declarations are the contract any implementation of the software toolkit has to meet,
 * so the build emits their JNI headers rather than leaving the signatures to be transcribed.
 */
tasks.compileJava {
    options.headerOutputDirectory = layout.buildDirectory.dir("generated/jni")
}

application {
    mainClass = "client"
    applicationDefaultJvmArgs = listOf(
        "-Xmx256m",
        "-Dsun.java2d.noddraw=true",
        "--add-opens",
        "java.base/java.lang=ALL-UNNAMED",
    )
}

tasks.run.configure {
    args = listOf("1", "1000", "local", "live", "english", "game0")
}

/**
 * The jar this source was recovered from, unpacked so that single classes can be read out of it.
 */
val unpackOriginal = tasks.register<Copy>("unpackOriginal") {
    description = "Unpacks the jar this source was recovered from."
    from(zipTree(layout.projectDirectory.file("../lib/runescape.jar")))
    into(layout.buildDirectory.dir("original"))
}

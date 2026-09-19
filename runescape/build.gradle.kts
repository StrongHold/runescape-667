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

/**
 * Checks what the recompiled classes compute against what they computed in that jar.
 *
 * Renaming is meant to change names and nothing else, and a decompiler is not obliged to preserve
 * what it could not prove. Every class here is matched to its original through the
 * `@OriginalClass` and `@OriginalMember` annotations, which are the only thing that still relates
 * the two once the names have gone.
 *
 * Pass `-Pfidelity=Terrain,Rasterizer` to check named classes. The default is every class whose
 * original is still in the jar, which takes a while.
 */
val verifyFidelity = tasks.register<Exec>("verifyFidelity") {
    description = "Checks what the recompiled classes compute against the original jar."
    dependsOn(tasks.compileJava, unpackOriginal)

    val sources = layout.projectDirectory.dir("src/main/java")
    val classes = layout.buildDirectory.dir("classes/java/main")
    val originals = layout.buildDirectory.dir("original")
    val asked = providers.gradleProperty("fidelity").getOrElse("")
    val script = rootProject.layout.projectDirectory.file("tools/fidelity.py")
    val owner = Regex("""@OriginalClass\("[^!]+!([^"]+)"\)""")

    executable = "python3"

    /*
     * The classes to check are worked out when the task runs rather than when it is configured,
     * so that a class added to the module is checked rather than being silently left out of a
     * cached file list.
     */
    doFirst {
        val wanted = if (asked.isEmpty()) {
            sources.asFile.listFiles().orEmpty()
                .filter { it.name.endsWith(".java") }
                .map { it.name.removeSuffix(".java") }
                .sorted()
        } else {
            asked.split(',')
        }

        val triples = wanted.flatMap { name ->
            val source = sources.file("$name.java").asFile
            val recompiled = classes.get().file("$name.class").asFile
            val was = source.takeIf { it.isFile }
                ?.useLines { lines -> lines.firstNotNullOfOrNull { owner.find(it) } }
                ?.groupValues?.get(1)
                ?.let { originals.get().file("$it.class").asFile }

            if (was != null && was.isFile && recompiled.isFile) {
                listOf(source.absolutePath, was.absolutePath, recompiled.absolutePath)
            } else {
                emptyList()
            }
        }

        require(triples.isNotEmpty()) { "No class in $sources matched a class in the original jar." }
        setArgs(listOf(script.asFile.absolutePath, "--") + triples)
    }
}

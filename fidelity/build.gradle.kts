plugins {
    java
}

/**
 * Checks the recompiled client against the jar it was recovered from.
 *
 * The client in `runescape` was decompiled, recompiled and then renamed a wave at a time. None of
 * that is meant to change what any method computes, and the tools here are how that is checked.
 * They read class files, never source, so what they compare is what the virtual machine runs.
 */
dependencies {
    implementation(project(":cli"))
    implementation(libs.asm.tree)
    implementation(libs.asm.analysis)
    implementation(libs.asm.util)
    implementation(libs.asm.commons)
}

val runescape = project(":runescape")
val recompiledClasses = runescape.layout.buildDirectory.dir("classes/java/main")
val originalClasses = runescape.layout.buildDirectory.dir("original")
val runescapeSources = runescape.layout.projectDirectory.dir("src/main/java")

/**
 * Compares which value opcodes each method uses, between the jar and the recompiled classes.
 *
 * Pass `-Pclasses=Terrain,Rasterizer` to check named classes. The default is every class whose
 * original is still in the jar, which takes a while.
 */
tasks.register<JavaExec>("verifyOpcodes") {
    description = "Compares the value opcodes of each recompiled method with the original jar."
    dependsOn(":runescape:compileJava", ":runescape:unpackOriginal")
    mainClass = "OpcodeCheck"
    classpath = sourceSets["main"].runtimeClasspath

    val asked = providers.gradleProperty("classes").getOrElse("")
    val sources = runescapeSources.asFile
    val recompiled = recompiledClasses.get().asFile
    val original = originalClasses.get().asFile
    val owner = Regex("""@OriginalClass\("[^!]+!([^"]+)"\)""")

    /*
     * The classes are listed when the task runs, so that a class added to the module is checked
     * instead of being left out of a list made when the build was configured.
     */
    argumentProviders.add(CommandLineArgumentProvider {
        val wanted = if (asked.isEmpty()) {
            sources.listFiles().orEmpty()
                .filter { it.name.endsWith(".java") }
                .map { it.name.removeSuffix(".java") }
                .sorted()
        } else {
            asked.split(',')
        }

        val triples = wanted.flatMap { name ->
            val source = File(sources, "$name.java")
            val compiled = File(recompiled, "$name.class")
            val was = source.takeIf { it.isFile }
                ?.useLines { lines -> lines.firstNotNullOfOrNull { owner.find(it) } }
                ?.groupValues?.get(1)
                ?.let { File(original, "$it.class") }

            if (was != null && was.isFile && compiled.isFile) {
                listOf(source.absolutePath, was.absolutePath, compiled.absolutePath)
            } else {
                emptyList()
            }
        }

        require(triples.isNotEmpty()) { "No class in $sources matched a class in the original jar." }
        triples
    })
}

/**
 * Compares what each method computes, as trees of arithmetic, between the jar and the recompiled
 * classes.
 *
 * It fails when a method differs that `expressions-outstanding.txt` does not list, since that may be
 * damage the decompiler did. Pass `-Pclasses=Terrain,Rasterizer` to check named classes. The report
 * says where the floating
 * point arithmetic of the two sides parts. Pass `-Pwhole` to write out every tree that differs
 * instead, and `-Pevery` with it to include integer arithmetic.
 */
tasks.register<JavaExec>("verifyExpressions") {
    description = "Compares the arithmetic of each recompiled method with the original jar."
    dependsOn(":runescape:compileJava", ":runescape:unpackOriginal")
    mainClass = "ExpressionCheck"
    classpath = sourceSets["main"].runtimeClasspath

    val named = providers.gradleProperty("classes").getOrElse("")
    val every = providers.gradleProperty("every").isPresent
    val whole = providers.gradleProperty("whole").isPresent
    val recompiled = recompiledClasses.get().asFile.absolutePath
    val original = originalClasses.get().asFile.absolutePath
    val outstanding = layout.projectDirectory.file("expressions-outstanding.txt").asFile.absolutePath
    inputs.file(outstanding)

    argumentProviders.add(CommandLineArgumentProvider {
        val chosen = named.split(',').filter { it.isNotBlank() }.flatMap { listOf("--class", it) }
        val wide = if (every) listOf("--every") else emptyList()
        val written = if (whole) listOf("--whole") else emptyList()
        listOf("--recompiled", recompiled, "--original", original, "--outstanding", outstanding) +
            chosen + wide + written
    })
}

/**
 * Sets the records two clients wrote with the agent side by side. Pass -Pjar=<file> for the jar's
 * client and -Pours=<file> for the recompiled one.
 */
tasks.register<JavaExec>("diffNativeTraces") {
    description = "Compares what two clients handed the native software toolkit."
    mainClass = "NativeTraceDiff"
    classpath = sourceSets["main"].runtimeClasspath
    val jar = providers.gradleProperty("jar")
    val ours = providers.gradleProperty("ours")
    argumentProviders.add(CommandLineArgumentProvider {
        listOf("--jar", File(jar.get()).absolutePath, "--ours", File(ours.get()).absolutePath)
    })
}

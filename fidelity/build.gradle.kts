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

/**
 * Compares what each method computes, as trees of arithmetic, between the jar and the recompiled
 * classes.
 *
 * Pass `-Pclasses=Terrain,Rasterizer` to check named classes. The report says where the floating
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

    argumentProviders.add(CommandLineArgumentProvider {
        val chosen = named.split(',').filter { it.isNotBlank() }.flatMap { listOf("--class", it) }
        val wide = if (every) listOf("--every") else emptyList()
        val written = if (whole) listOf("--whole") else emptyList()
        listOf("--recompiled", recompiled, "--original", original) + chosen + wide + written
    })
}

/**
 * The agent that writes down every call a client makes into the native software toolkit, with
 * ASM packed inside it, so that it can be handed to any client with -javaagent.
 *
 * The jar the client came from and the recompiled client can both be run with it, and the two
 * records compared. See NativeTrace.
 */
tasks.register<Jar>("nativeTraceAgent") {
    description = "Builds the agent that records calls into the native software toolkit."
    archiveFileName = "native-trace.jar"
    from(sourceSets["main"].output)
    from(configurations["runtimeClasspath"].filter { it.name.startsWith("asm") }.map { zipTree(it) }) {
        exclude("module-info.class", "META-INF/**")
    }
    manifest {
        attributes(
            "Premain-Class" to "NativeTrace",
            "Can-Set-Native-Method-Prefix" to "true",
        )
    }
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

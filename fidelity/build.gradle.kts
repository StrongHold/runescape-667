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

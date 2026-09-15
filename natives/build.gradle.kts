plugins {
    java
}

dependencies {
    implementation(project(":runescape"))
}

/**
 * The native pieces the client needs on macOS, and the harness that exercises them.
 *
 * The shim is built for x86_64 because it is loaded next to the shipped software toolkit, which
 * has no arm64 slice.
 */
val jdkHome = rootProject.layout.projectDirectory.dir(".gradle/jdk-x64/unpacked/Home")
val shimSource = layout.projectDirectory.file("src/main/objc/jawtshim.m")
val shimLibrary = layout.buildDirectory.file("natives/libjawtshim.dylib")

val compileJawtShim by tasks.registering(Exec::class) {
    description = "Builds the drawing surface the software toolkit gets instead of JavaVM.framework."
    dependsOn(":unpackX64Jdk")
    inputs.file(shimSource)
    outputs.file(shimLibrary)
    executable = "clang"
    args(
        "-arch", "x86_64",
        "-dynamiclib",
        "-fobjc-arc",
        "-Wall",
        "-Werror",
        // The toolkits are from 2011 and the surfaces they ask for are deprecated by design.
        "-Wno-deprecated-declarations",
        "-I", jdkHome.dir("include").asFile.absolutePath,
        "-I", jdkHome.dir("include/darwin").asFile.absolutePath,
        "-framework", "Cocoa",
        "-framework", "QuartzCore",
        "-framework", "ImageIO",
        "-install_name", "@loader_path/libjawtshim.dylib",
        "-o", shimLibrary.get().asFile.absolutePath,
        shimSource.asFile.absolutePath,
    )
    val outputDirectory = shimLibrary.get().asFile.parentFile
    doFirst {
        outputDirectory.mkdirs()
    }
}

tasks.register("assembleNatives") {
    description = "Builds every native this module owns."
    dependsOn(compileJawtShim)
}

/**
 * A copy of the shipped toolkit whose only JavaVM.framework import points at the shim instead. The
 * shipped copy is left alone, and the replacement path is shorter than the original, so this is a
 * straight edit of one load command.
 */
val shippedToolkit = providers.gradleProperty("sw3dLibrary")
    .orElse(providers.systemProperty("user.home").map { "$it/.jagex_cache_32/runescape/libsw3d.dylib" })
val patchedToolkit = layout.buildDirectory.file("natives/libsw3d-patched.dylib")

val patchToolkit by tasks.registering(Exec::class) {
    description = "Copies the shipped software toolkit and points its JAWT import at the shim."
    dependsOn(compileJawtShim)
    outputs.file(patchedToolkit)

    val source = shippedToolkit.get()
    val target = patchedToolkit.get().asFile
    val shimName = "@loader_path/libjawtshim.dylib"
    val javaVm = "/System/Library/Frameworks/JavaVM.framework/Versions/A/JavaVM"

    executable = "sh"
    args("-c", listOf(
        "cp '$source' '$target'",
        "install_name_tool -change '$javaVm' '$shimName' '$target'",
        "codesign -f -s - '$target'",
    ).joinToString(" && "))

    doFirst {
        require(File(source).isFile) { "No software toolkit at $source. Point -Psw3dLibrary at one." }
    }
}

val captureFrames by tasks.registering(JavaExec::class) {
    description = "Renders a fixed scene through the software toolkit and writes each frame as a PNG."
    dependsOn(patchToolkit)
    mainClass = "FrameCapture"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(rootProject.layout.projectDirectory.file(".gradle/jdk-x64/unpacked/Home/bin/java").asFile.absolutePath)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")

    val frames = layout.buildDirectory.dir("frames").get().asFile
    environment("JAWTSHIM_DUMP", frames.absolutePath)
    args(patchedToolkit.get().asFile.absolutePath)

    doFirst {
        frames.mkdirs()
    }
}

/**
 * A static scene must produce the same bytes every frame. The toolkit concatenates its transforms
 * onto whatever context it is handed, so anything the surface fails to reset accumulates and shows
 * up as the picture changing between frames of an unchanging scene.
 */
val verifyToolkitLifetime by tasks.registering(JavaExec::class) {
    description = "Builds and discards software toolkits to prove none is torn down by the collector."
    dependsOn(patchToolkit)
    mainClass = "ToolkitLifetime"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(rootProject.layout.projectDirectory.file(".gradle/jdk-x64/unpacked/Home/bin/java").asFile.absolutePath)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    systemProperty("toolkit.surface.library", shimLibrary.get().asFile.absolutePath)
    args(patchedToolkit.get().asFile.absolutePath)
}

val verifyFrames by tasks.registering {
    description = "Checks that every captured frame of the fixed scene is identical."
    dependsOn(captureFrames)

    val frames = layout.buildDirectory.dir("frames")
    inputs.dir(frames)

    doLast {
        val captured = frames.get().asFile.listFiles { file -> file.extension == "png" }
            ?.sortedBy { it.name }
            ?: emptyList()

        require(captured.size >= 2) { "Expected at least two frames, found ${captured.size}" }

        val digests = captured.associate { it.name to it.readBytes().toList().hashCode() }
        val distinct = digests.values.distinct()
        require(distinct.size == 1) {
            "The fixed scene rendered differently between frames, so some drawing state is not " +
                "being reset: $digests"
        }

        logger.lifecycle("${captured.size} frames identical")
    }
}

/**
 * The classes the software toolkit binds to. Their native declarations are the whole contract an
 * implementation has to meet.
 */
val toolkitClasses = listOf("a", "ba", "h", "i", "j", "ja", "n", "na", "oa", "p", "t", "wa", "xa", "ya")

val skeletonSource = layout.buildDirectory.file("generated/sw3d-skeleton.c")

/**
 * Writes an implementation of every native the toolkit classes declare, each one doing nothing.
 *
 * The point is the surface rather than the behaviour. A skeleton that loads and resolves every
 * call proves the contract is complete and gives each real implementation somewhere to land.
 */
val generateToolkitSkeleton by tasks.registering {
    description = "Writes a do-nothing implementation of every native the toolkit declares."
    dependsOn(":runescape:compileJava")

    val headerDirectory = project(":runescape").layout.buildDirectory.dir("generated/jni")
    val target = skeletonSource
    val classes = toolkitClasses
    inputs.dir(headerDirectory)
    outputs.file(target)

    doLast {
        val declaration = Regex("""JNIEXPORT\s+(\S+)\s+JNICALL\s+(\w+)\s*\(([^)]*)\)\s*;""")
        val body = StringBuilder()
        var count = 0

        classes.sorted().forEach { name ->
            val header = headerDirectory.get().file("$name.h").asFile
            require(header.isFile) { "No JNI header for $name. Did the class stop declaring natives?" }

            declaration.findAll(header.readText().replace(Regex("""\s+"""), " ")).forEach { match ->
                val (returns, symbol, parameters) = match.destructured
                val typed = parameters.split(",").map(String::trim).filter(String::isNotEmpty)
                val named = typed.mapIndexed { index, type ->
                    when (index) {
                        0 -> "JNIEnv *env"
                        1 -> "$type self"
                        else -> "$type a$index"
                    }
                }
                body.append("\nJNIEXPORT $returns JNICALL $symbol(${named.joinToString(", ")}) {\n")
                body.append("    unimplemented(\"$symbol\");\n")
                if (returns != "void") {
                    body.append("    return 0;\n")
                }
                body.append("}\n")
                count++
            }
        }

        val file = target.get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            /*
             * Every native the software toolkit classes declare, implemented as nothing.
             *
             * This file is generated from the JNI headers the client build emits, so the set of
             * entry points always matches what the Java side declares.
             */
            #include <stdio.h>
            #include <stdlib.h>
            #include <jni.h>

            static void unimplemented(const char *name) {
                if (getenv("SW3D_SKELETON_VERBOSE") != NULL) {
                    fprintf(stderr, "[sw3d-skeleton] %s\n", name);
                }
            }
            """.trimIndent() + "\n" + body
        )
        logger.lifecycle("generated $count native entry points")
    }
}

val skeletonLibrary = layout.buildDirectory.file("natives/libsw3d-skeleton.dylib")

/**
 * Builds the skeleton for arm64, the architecture the shipped toolkit does not have. Nothing it
 * produces is drawn yet, so its value is that it loads and that every call the client makes
 * resolves.
 */
val compileToolkitSkeleton by tasks.registering(Exec::class) {
    description = "Builds the arm64 skeleton of the software toolkit."
    dependsOn(generateToolkitSkeleton, ":unpackX64Jdk")
    inputs.file(skeletonSource)
    outputs.file(skeletonLibrary)

    val source = skeletonSource.get().asFile
    val target = skeletonLibrary.get().asFile
    val outputDirectory = target.parentFile

    executable = "clang"
    args(
        "-arch", "arm64",
        "-dynamiclib",
        "-Wall",
        "-Werror",
        "-I", jdkHome.dir("include").asFile.absolutePath,
        "-I", jdkHome.dir("include/darwin").asFile.absolutePath,
        "-install_name", "@loader_path/libsw3d-skeleton.dylib",
        "-o", target.absolutePath,
        source.absolutePath,
    )

    doFirst {
        outputDirectory.mkdirs()
    }
}

/**
 * Runs on the machine's own architecture rather than the translated one, because the skeleton is
 * the arm64 implementation and the point is that it needs no translation.
 */
val verifyToolkitSkeleton by tasks.registering(JavaExec::class) {
    description = "Builds the software toolkit against the arm64 skeleton."
    dependsOn(compileToolkitSkeleton)
    mainClass = "ToolkitSkeleton"
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    args(skeletonLibrary.get().asFile.absolutePath)
}

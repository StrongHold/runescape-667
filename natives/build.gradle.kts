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
val shimSource = layout.projectDirectory.file("src/main/native/jawtshim.m")
val shimLibrary = layout.buildDirectory.file("natives/libjawtshim.dylib")

val compileJawtShim by tasks.registering(Exec::class) {
    description = "Builds the drawing surface the software toolkit gets instead of JavaVM.framework."
    dependsOn(":unpackX64Jdk")
    inputs.file(shimSource)
    outputs.file(shimLibrary)
    executable = "clang"
    args(
        // Universal, so each library can be replaced on its own rather than in lockstep with
        // every other native on the same path.
        "-arch", "arm64",
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
        "-arch", "x86_64",
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

val openGlSource = layout.buildDirectory.file("generated/jaggl-opengl.c")
val openGlReport = layout.buildDirectory.file("generated/jaggl-outstanding.txt")

/**
 * Writes the part of the OpenGL binding that needs no judgement, and lists what is left.
 *
 * The binding is not a renderer. Almost every native is named after the OpenGL entry point it
 * calls and passes its arguments straight through, so most of it is generated from the JNI headers
 * the client build emits. What remains is the handful that marshals arrays or strings and the
 * platform calls that own the context, and those are written by hand.
 */
val generateOpenGlBinding by tasks.registering {
    description = "Writes the mechanical part of the OpenGL binding from the client's JNI headers."
    dependsOn(":runescape:compileJava")

    val header = project(":runescape").layout.buildDirectory.file("generated/jni/jaggl_OpenGL.h")
    val target = openGlSource
    val report = openGlReport
    inputs.file(header)
    outputs.files(target, report)

    val glue = setOf(
        "init", "release", "surfaceResized", "setPbuffer", "swapBuffers", "releaseSurface",
        "prepareSurface", "createPbuffer", "setSurface", "releasePbuffer", "detachPeer",
        "attachPeer", "setSwapInterval", "arePbuffersAvailable",
    )

    /**
     * A native whose name carries a tag the client added to tell two of its own overloads apart,
     * and the entry point it actually means.
     */
    val renamed = mapOf(
        "glBufferDataARBa" to "glBufferDataARB",
        "glBufferDataARBub" to "glBufferDataARB",
        "glBufferSubDataARBa" to "glBufferSubDataARB",
        "glBufferSubDataARBub" to "glBufferSubDataARB",
        "glDrawPixelsi" to "glDrawPixels",
        "glDrawPixelsub" to "glDrawPixels",
        "glGetTexImagei" to "glGetTexImage",
        "glGetTexImageub" to "glGetTexImage",
        "glReadPixelsi" to "glReadPixels",
        "glReadPixelsub" to "glReadPixels",
        "glTexImage1Dub" to "glTexImage1D",
        "glTexImage2Df" to "glTexImage2D",
        "glTexImage2Di" to "glTexImage2D",
        "glTexImage2Dub" to "glTexImage2D",
        "glTexImage3Dub" to "glTexImage3D",
        "glTexSubImage2Df" to "glTexSubImage2D",
        "glTexSubImage2Di" to "glTexSubImage2D",
        "glTexSubImage2Dub" to "glTexSubImage2D",
    )

    /**
     * Natives with no OpenGL entry point of their own. Two are named in the singular by the client
     * and take a count and an address in OpenGL, and two take raw bytes where OpenGL takes a list of
     * strings. Each needs a body rather than a pass-through.
     */
    val plural = setOf(
        "glDeleteProgramARB",
        "glGenProgramARB",
        "glProgramRawARB",
        "glShaderSourceRawARB",
    )

    doLast {
        val declaration = Regex("""JNIEXPORT\s+(\S+)\s+JNICALL\s+(\w+)\s*\(([^)]*)\)\s*;""")
        val text = header.get().asFile.readText().replace(Regex("""\s+"""), " ")

        val body = StringBuilder()
        val outstanding = mutableListOf<String>()
        var written = 0

        declaration.findAll(text).forEach { match ->
            val (returns, symbol, parameters) = match.destructured
            val name = symbol.removePrefix("Java_jaggl_OpenGL_")
            val types = parameters.split(",").map(String::trim).filter(String::isNotEmpty).drop(2)

            /*
             * Every native that takes an array takes it as the last pair of arguments, an array
             * and an offset into it, and OpenGL takes one address in their place.
             */
            val arrayed = types.size >= 2 &&
                types[types.size - 2].endsWith("Array") &&
                types.last() == "jint" &&
                types.dropLast(2).none { it.endsWith("Array") || it == "jstring" }

            val needsHands = name in glue || name in plural ||
                returns == "jstring" ||
                types.any { it == "jstring" } ||
                (types.any { it.contains("Array") } && !arrayed)

            if (needsHands) {
                outstanding.add("$returns $name(${types.joinToString(", ")})")
            } else if (arrayed) {
                val leading = types.dropLast(2)
                val element = types[types.size - 2].removeSuffix("Array")
                val named = leading.mapIndexed { index, type -> ", $type a$index" }.joinToString("") +
                    ", ${types[types.size - 2]} elements, jint offset"
                val arguments = leading.mapIndexed { index, type ->
                    if (type == "jlong") "(GLhandleARB) a$index" else "a$index"
                } + "(void *) (address + offset)"

                body.append("\nJNIEXPORT $returns JNICALL $symbol(JNIEnv *env, jclass owner$named) {\n")
                body.append("    $element *address = (*env)->GetPrimitiveArrayCritical(env, elements, NULL);\n")
                if (returns != "void") {
                    body.append("    $returns result = ")
                } else {
                    body.append("    ")
                }
                body.append("${renamed[name] ?: name}(${arguments.joinToString(", ")});\n")
                body.append("    (*env)->ReleasePrimitiveArrayCritical(env, elements, address, 0);\n")
                if (returns != "void") {
                    body.append("    return result;\n")
                }
                body.append("}\n")
                written++
            } else {
                val named = types.mapIndexed { index, type -> "$type a$index" }
                val arguments = types.mapIndexed { index, type ->
                    if (type == "jlong") "(GLhandleARB) a$index" else "a$index"
                }
                body.append("\nJNIEXPORT $returns JNICALL $symbol(JNIEnv *env, jclass owner")
                body.append(named.joinToString("") { ", $it" })
                body.append(") {\n    ")
                if (returns == "jlong") {
                    body.append("return (jlong) (intptr_t) ")
                } else if (returns != "void") {
                    body.append("return ")
                }
                body.append("${renamed[name] ?: name}(${arguments.joinToString(", ")});\n}\n")
                written++
            }
        }

        val file = target.get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            /*
             * The part of the OpenGL binding that passes its arguments straight through.
             *
             * Generated from the JNI headers the client build emits, so the entry points cannot
             * drift from what the Java side declares. Every native here is named after the OpenGL
             * entry point it calls, which is why it can be generated at all.
             */
            #include <OpenGL/gl.h>
            #include <OpenGL/glext.h>
            #include <stdint.h>
            #include <jni.h>
            """.trimIndent() + "\n" + body
        )

        report.get().asFile.writeText(outstanding.sorted().joinToString("\n") + "\n")
        logger.lifecycle("generated $written entry points, ${outstanding.size} left to write by hand")
    }
}

val openGlLibrary = layout.buildDirectory.file("natives/libjaggl.dylib")

/**
 * Builds the OpenGL binding.
 *
 * It is incomplete: the natives that marshal arrays or strings and the platform calls that own the
 * context are not written yet, so the client cannot use this. What it does show is that the
 * mechanical part maps onto the OpenGL this machine has, which is most of the binding.
 */
val compileOpenGlBinding by tasks.registering(Exec::class) {
    description = "Builds the part of the OpenGL binding that is written."
    dependsOn(generateOpenGlBinding, ":unpackX64Jdk")
    inputs.file(openGlSource)
    outputs.file(openGlLibrary)

    val source = openGlSource.get().asFile
    val target = openGlLibrary.get().asFile
    val outputDirectory = target.parentFile

    executable = "clang"
    args(
        "-arch", "arm64",
        "-arch", "x86_64",
        "-dynamiclib",
        "-Wall",
        "-Werror",
        // The binding is from 2011 and every surface it asks for is deprecated by design.
        "-Wno-deprecated-declarations",
        "-I", jdkHome.dir("include").asFile.absolutePath,
        "-I", jdkHome.dir("include/darwin").asFile.absolutePath,
        "-framework", "OpenGL",
        "-install_name", "@loader_path/libjaggl.dylib",
        "-o", target.absolutePath,
        source.absolutePath,
    )

    doFirst {
        outputDirectory.mkdirs()
    }
}

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
val x64JavaExecutable = jdkHome.file("bin/java").asFile.absolutePath
val shimSource = layout.projectDirectory.file("src/main/native/jawtshim/jawtshim.m")
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
        "-framework", "OpenGL",
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
    dependsOn(compileJawtShim, compileOpenGlBinding, compileMemoryLibrary, compileMiscLibrary)
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

/**
 * What a scene reads out of the environment, and what it reads when the environment says nothing.
 *
 * Both capture tasks hand the same set through, because a setting that reaches one side and not
 * the other shows up as a difference between the two toolkits rather than as a broken experiment.
 */
val sceneSettings = mapOf(
    "SW3D_FACE_COLOUR" to "",
    "SW3D_AMBIENT" to "64",
    "SW3D_SUN_TENTHS" to "5",
    "SW3D_GROUND_TEXTURE" to "-1",
    "SW3D_GROUND_OVERLAY" to "0",
    "SW3D_GROUND_LEVELS" to "0",
    "SW3D_GROUND_FLAGS" to "0",
    "SW3D_GROUND_FEATURES" to "0",
    "SW3D_GROUND_COLOUR" to "-1",
    "SW3D_GROUND_FLAT" to "0",
    "SW3D_WATER_COLOUR" to "",
    "SW3D_WATER_REACHES" to "",
    "SW3D_WATER_EYE" to "1",
    "SW3D_WATER_FOG" to "",
    "SW3D_WATER_DEPTH" to "",
    "SW3D_WATER_ONE_GRID" to ""
)

val captureFrames by tasks.registering(JavaExec::class) {
    description = "Renders a fixed scene through the software toolkit and writes each frame as a PNG."
    dependsOn(patchToolkit)
    mainClass = "FrameCapture"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(rootProject.layout.projectDirectory.file(".gradle/jdk-x64/unpacked/Home/bin/java").asFile.absolutePath)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")

    val frames = layout.buildDirectory.dir("frames").get().asFile
    environment("JAWTSHIM_DUMP", frames.absolutePath)
    sceneSettings.forEach { (name, fallback) ->
        val held = providers.environmentVariable(name).getOrElse(fallback)
        environment(name, held)
        inputs.property(name, held)
    }
    args(patchedToolkit.get().asFile.absolutePath)
    outputs.dir(frames)

    doFirst {
        frames.deleteRecursively()
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
        // Neither of these is in the Mac build of the shipped library, under any name.
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
             * and an offset into it, and OpenGL takes one address in their place. The array may be
             * absent, which is how the client asks OpenGL to set storage aside without filling it,
             * so the address it is given is absent too rather than taken from nothing.
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
                } + "address == NULL ? NULL : (void *) (address + offset)"

                body.append("\nJNIEXPORT $returns JNICALL $symbol(JNIEnv *env, jclass owner$named) {\n")
                body.append("    $element *address = elements == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, elements, NULL);\n")
                if (returns != "void") {
                    body.append("    $returns result = ")
                } else {
                    body.append("    ")
                }
                body.append("${renamed[name] ?: name}(${arguments.joinToString(", ")});\n")
                body.append("    if (address != NULL) {\n")
                body.append("        (*env)->ReleasePrimitiveArrayCritical(env, elements, address, 0);\n")
                body.append("    }\n")
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
 * The mechanical half is generated from the client's JNI headers and the platform half, which
 * owns the context and the layer that presents it, is written by hand beside it.
 */
val compileOpenGlBinding by tasks.registering(Exec::class) {
    description = "Builds the OpenGL binding."
    dependsOn(generateOpenGlBinding, ":unpackX64Jdk")
    inputs.file(openGlSource)
    outputs.file(openGlLibrary)

    val generated = openGlSource.get().asFile
    val platform = layout.projectDirectory.file("src/main/native/jaggl/jaggl.m").asFile
    val target = openGlLibrary.get().asFile
    val outputDirectory = target.parentFile

    inputs.file(platform)

    executable = "clang"
    args(
        "-arch", "arm64",
        "-arch", "x86_64",
        "-dynamiclib",
        "-fobjc-arc",
        "-Wall",
        "-Werror",
        // The binding is from 2011 and every surface it asks for is deprecated by design.
        "-Wno-deprecated-declarations",
        "-I", jdkHome.dir("include").asFile.absolutePath,
        "-I", jdkHome.dir("include/darwin").asFile.absolutePath,
        "-framework", "Cocoa",
        "-framework", "QuartzCore",
        "-framework", "OpenGL",
        "-install_name", "@loader_path/libjaggl.dylib",
        "-o", target.absolutePath,
        generated.absolutePath,
        platform.absolutePath,
    )

    doFirst {
        outputDirectory.mkdirs()
    }
}

val shippedOpenGlBinding = providers.gradleProperty("jagglLibrary")
    .orElse(providers.systemProperty("user.home").map { "$it/.jagex_cache_32/runescape/libjaggl.dylib" })
val patchedOpenGlBinding = layout.buildDirectory.file("natives/libjaggl-patched.dylib")

val patchOpenGlBinding by tasks.registering(Exec::class) {
    description = "Copies the shipped OpenGL binding and points its JNI import at the shim."
    dependsOn(compileJawtShim)
    outputs.file(patchedOpenGlBinding)

    val source = shippedOpenGlBinding.get()
    val target = patchedOpenGlBinding.get().asFile
    val shimName = "@loader_path/libjawtshim.dylib"
    val javaVm = "/System/Library/Frameworks/JavaVM.framework/Versions/A/JavaVM"

    executable = "sh"
    args("-c", listOf(
        "lipo -thin x86_64 '$source' -output '$target'",
        "install_name_tool -change '$javaVm' '$shimName' '$target'",
        "codesign -f -s - '$target'",
    ).joinToString(" && "))

    doFirst {
        require(File(source).isFile) { "No OpenGL binding at $source. Point -PjagglLibrary at one." }
        target.parentFile.mkdirs()
    }
}

val bindingAnswers = layout.buildDirectory.file("answers/binding-shipped.txt")
val ownBindingAnswers = layout.buildDirectory.file("answers/binding-ours.txt")

/**
 * Drives a binding through the same script and writes down what it carried back.
 *
 * Both sides need the shim to wait for the view it hands over. The shipped binding builds its
 * context out of NSOpenGLContext and gives it a view, and the call that takes a view makes the
 * context current on whichever thread runs it. The shim hands that call to the main thread and
 * does not wait, which is what the client needs and what leaves a harness drawing into a context
 * current somewhere else.
 */
fun registerBindingCapture(name: String, library: Provider<RegularFile>, answers: Provider<RegularFile>,
                           after: TaskProvider<*>) =
    tasks.register<JavaExec>(name) {
        dependsOn(after, ":unpackX64Jdk")

        val written = answers.get().asFile

        mainClass = "GlProbe"
        classpath = sourceSets["main"].runtimeClasspath
        setExecutable(x64JavaExecutable)
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
        environment("JAWTSHIM_WAIT_FOR_VIEW", "1")
        args(library.get().asFile.absolutePath, written.absolutePath)
        inputs.file(library)
        outputs.file(answers)
        doFirst { written.parentFile.mkdirs() }
    }

val captureBinding = registerBindingCapture(
    "captureBinding", patchedOpenGlBinding, bindingAnswers, patchOpenGlBinding)
val captureOwnBinding = registerBindingCapture(
    "captureOwnBinding", openGlLibrary, ownBindingAnswers, compileOpenGlBinding)

/**
 * The samples are not compared against the shipped binding, because the two do not agree on them
 * and cannot: the shipped one gives two where none are asked for. What is held here is that this
 * one draws with as many as it was asked for and can still be read back afterwards.
 */
val verifyOpenGlSamples by tasks.registering(JavaExec::class) {
    description = "Draws through the OpenGL binding at each sample count and reads the picture back."
    dependsOn(compileOpenGlBinding, ":unpackX64Jdk")

    mainClass = "GlSamples"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(x64JavaExecutable)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    environment("JAWTSHIM_WAIT_FOR_VIEW", "1")
    args(openGlLibrary.get().asFile.absolutePath)
    inputs.file(openGlLibrary)
}

/**
 * What a binding carries is held to being identical. What sort of context it built is not, and is
 * reported instead: the two do not build the same one, and `jaggl/README.md` says why.
 */
val verifyOpenGlBinding by tasks.registering(JavaExec::class) {
    description = "Checks our OpenGL binding against the shipped one, answer for answer."
    dependsOn(captureBinding, captureOwnBinding)
    mainClass = "AnswerCheck"
    classpath = sourceSets["main"].runtimeClasspath
    args(
        bindingAnswers.get().asFile.absolutePath,
        ownBindingAnswers.get().asFile.absolutePath,
        "OpenGL binding answers",
    )
}

val memorySource = layout.projectDirectory.file("src/main/native/jaclib/jaclib.c")
val memoryLibrary = layout.buildDirectory.file("natives/libjaclib.dylib")

/**
 * Builds the native memory library.
 *
 * The client's own JNI headers are on the include path and the source includes them, so a
 * signature that does not match the Java declaration fails the compile rather than the client.
 */
val compileMemoryLibrary by tasks.registering(Exec::class) {
    description = "Builds the native memory library the hardware toolkits allocate from."
    dependsOn(":unpackX64Jdk", ":runescape:compileJava")

    val headers = project(":runescape").layout.buildDirectory.dir("generated/jni")
    val target = memoryLibrary.get().asFile

    inputs.file(memorySource)
    inputs.dir(headers)
    outputs.file(memoryLibrary)

    executable = "clang"
    args(
        "-arch", "arm64",
        "-arch", "x86_64",
        "-dynamiclib",
        "-Wall",
        "-Werror",
        "-I", jdkHome.dir("include").asFile.absolutePath,
        "-I", jdkHome.dir("include/darwin").asFile.absolutePath,
        "-I", headers.get().asFile.absolutePath,
        "-install_name", "@loader_path/libjaclib.dylib",
        "-o", target.absolutePath,
        memorySource.asFile.absolutePath,
    )

    doFirst {
        target.parentFile.mkdirs()
    }
}

val verifyMemoryLibrary by tasks.registering(JavaExec::class) {
    description = "Allocates from the native memory library and forces it to compact."
    dependsOn(compileMemoryLibrary)
    mainClass = "MemoryHeap"
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    args(memoryLibrary.get().asFile.absolutePath)
}

/**
 * The shipped memory library, thinned to the one slice a virtual machine here can run and pointed
 * at the shim.
 *
 * It asks JavaVM.framework for its JNI entry points exactly as the software toolkit does, so it
 * needs the same shim, and it is thinned first because install_name_tool cannot read the ppc slice
 * this one still carries.
 */
val shippedMemoryLibrary = providers.gradleProperty("jaclibLibrary")
    .orElse(providers.systemProperty("user.home").map { "$it/.jagex_cache_32/runescape/libjaclib.dylib" })
val patchedMemoryLibrary = layout.buildDirectory.file("natives/libjaclib-patched.dylib")

val patchMemoryLibrary by tasks.registering(Exec::class) {
    description = "Copies the shipped memory library and points its JNI import at the shim."
    dependsOn(compileJawtShim)
    outputs.file(patchedMemoryLibrary)

    val source = shippedMemoryLibrary.get()
    val target = patchedMemoryLibrary.get().asFile
    val shimName = "@loader_path/libjawtshim.dylib"
    val javaVm = "/System/Library/Frameworks/JavaVM.framework/Versions/A/JavaVM"

    executable = "sh"
    args("-c", listOf(
        "lipo -thin x86_64 '$source' -output '$target'",
        "install_name_tool -change '$javaVm' '$shimName' '$target'",
        "codesign -f -s - '$target'",
    ).joinToString(" && "))

    doFirst {
        require(File(source).isFile) { "No memory library at $source. Point -PjaclibLibrary at one." }
        target.parentFile.mkdirs()
    }
}

val memoryAnswers = layout.buildDirectory.file("answers/memory-shipped.txt")
val ownMemoryAnswers = layout.buildDirectory.file("answers/memory-ours.txt")

val captureMemory by tasks.registering(JavaExec::class) {
    description = "Records what the shipped memory library answers."
    dependsOn(patchMemoryLibrary)

    val written = memoryAnswers.get().asFile

    mainClass = "MemoryProbe"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(x64JavaExecutable)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    args(patchedMemoryLibrary.get().asFile.absolutePath, written.absolutePath)
    outputs.file(memoryAnswers)
    doFirst { written.parentFile.mkdirs() }
}

val captureOwnMemory by tasks.registering(JavaExec::class) {
    description = "Records what our memory library answers."
    dependsOn(compileMemoryLibrary, ":unpackX64Jdk")

    val written = ownMemoryAnswers.get().asFile

    mainClass = "MemoryProbe"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(x64JavaExecutable)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    args(memoryLibrary.get().asFile.absolutePath, written.absolutePath)
    inputs.file(memoryLibrary)
    inputs.files(sourceSets["main"].runtimeClasspath)
    outputs.file(ownMemoryAnswers)
    doFirst { written.parentFile.mkdirs() }
}

/**
 * Both are driven through the x86_64 virtual machine, because the shipped library has no slice
 * for anything else and the two have to be asked the same questions on the same machine.
 */
val verifyMemoryAnswers by tasks.registering(JavaExec::class) {
    description = "Checks our memory library against the shipped one, answer for answer."
    dependsOn(captureMemory, captureOwnMemory)
    mainClass = "AnswerCheck"
    classpath = sourceSets["main"].runtimeClasspath
    args(
        memoryAnswers.get().asFile.absolutePath,
        ownMemoryAnswers.get().asFile.absolutePath,
        "memory library answers",
    )
}

val miscSource = layout.projectDirectory.file("src/main/native/jagmisc/jagmisc.c")
val miscLibrary = layout.buildDirectory.file("natives/libjagmisc.dylib")

val compileMiscLibrary by tasks.registering(Exec::class) {
    description = "Builds the clock, the memory sizes and the ping the client asks jagmisc for."
    dependsOn(":unpackX64Jdk", ":runescape:compileJava")

    val headers = project(":runescape").layout.buildDirectory.dir("generated/jni")
    val target = miscLibrary.get().asFile

    inputs.file(miscSource)
    inputs.dir(headers)
    outputs.file(miscLibrary)

    executable = "clang"
    args(
        "-arch", "arm64",
        "-arch", "x86_64",
        "-dynamiclib",
        "-Wall",
        "-Werror",
        "-O2",
        "-I", jdkHome.dir("include").asFile.absolutePath,
        "-I", jdkHome.dir("include/darwin").asFile.absolutePath,
        "-I", headers.get().asFile.absolutePath,
        "-install_name", "@loader_path/libjagmisc.dylib",
        "-o", target.absolutePath,
        miscSource.asFile.absolutePath,
    )

    doFirst {
        target.parentFile.mkdirs()
    }
}

/**
 * The game's file store holds jagmisc for Windows and for nothing else, so there is no shipped
 * library here to measure against. Each answer is held against a second way of asking the machine
 * the same question instead.
 */
val verifyMiscLibrary by tasks.registering(JavaExec::class) {
    description = "Holds the clock, the memory sizes and the ping against what the machine says."
    dependsOn(compileMiscLibrary)
    mainClass = "Jagmisc"
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    args(miscLibrary.get().asFile.absolutePath)
}

val cacheDirectory = providers.gradleProperty("cache")
    .orElse(providers.systemProperty("user.home").map { "$it/.jagex_cache_32/runescape" })

val listCacheLibraries by tasks.registering(JavaExec::class) {
    description = "Lists the native libraries the cache holds, for every platform."
    mainClass = "CacheLibraries"
    classpath = sourceSets["main"].runtimeClasspath
    args(cacheDirectory.get())
}

val toolkitTrace = layout.buildDirectory.file("generated/sw3d-trace.txt")

/**
 * Records which natives the toolkit reaches, and in what order, while a frame is drawn.
 *
 * The skeleton answers every call with nothing, so no frame comes out and the run is expected to
 * fail. What it leaves behind is the order the client asks for things in, which is the order they
 * are worth implementing in.
 */
val traceToolkit by tasks.registering(JavaExec::class) {
    description = "Records the natives a frame reaches, in the order the client asks for them."
    dependsOn(compileToolkitSkeleton)
    mainClass = "FrameCapture"
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    args(skeletonLibrary.get().asFile.absolutePath)
    environment("SW3D_SKELETON_VERBOSE", "1")
    environment("JAWTSHIM_DUMP", layout.buildDirectory.dir("trace-frames").get().asFile.absolutePath)
    isIgnoreExitValue = true

    val report = toolkitTrace.get().asFile
    outputs.file(report)

    doFirst {
        report.parentFile.mkdirs()
        errorOutput = report.outputStream()
    }

    doLast {
        val calls = report.readLines()
            .filter { it.startsWith("[sw3d-skeleton] ") }
            .map { it.removePrefix("[sw3d-skeleton] ") }

        val order = LinkedHashMap<String, Int>()
        calls.forEach { order[it] = (order[it] ?: 0) + 1 }

        report.writeText(
            order.entries.joinToString("\n") { (symbol, count) -> "%-40s %d".format(symbol, count) } + "\n"
        )
        logger.lifecycle("${order.size} natives reached, ${calls.size} calls, written to ${report.name}")
    }
}

val toolkitDirectory = layout.projectDirectory.dir("src/main/native/sw3d")
val toolkitStubs = layout.buildDirectory.file("generated/sw3d-stubs.c")
val toolkitOutstanding = layout.buildDirectory.file("generated/sw3d-outstanding.txt")

/**
 * Writes a do-nothing implementation of every native the toolkit declares and this module does
 * not yet answer, so the library always exports the whole surface while it is being filled in.
 *
 * Which natives are written is read from the sources rather than listed here, so the two cannot
 * drift apart.
 */
val generateToolkitStubs by tasks.registering {
    description = "Stubs every toolkit native that is not written yet, and lists what is left."
    dependsOn(":runescape:compileJava")

    val headerDirectory = project(":runescape").layout.buildDirectory.dir("generated/jni")
    val sources = toolkitDirectory
    val classes = toolkitClasses
    val stubs = toolkitStubs
    val outstanding = toolkitOutstanding

    inputs.dir(headerDirectory)
    inputs.dir(sources)
    outputs.file(stubs)
    outputs.file(outstanding)

    doLast {
        val symbol = Regex("""JNIEXPORT\s+\S+\s+JNICALL\s+(Java_\w+)\s*\(""")
        val written = sources.asFile.walkTopDown()
            .filter { it.isFile && (it.extension == "c" || it.extension == "m") }
            .flatMap { file -> symbol.findAll(file.readText()).map { it.groupValues[1] } }
            .toSet()

        val declaration = Regex("""JNIEXPORT\s+(\S+)\s+JNICALL\s+(\w+)\s*\(([^)]*)\)\s*;""")
        val body = StringBuilder()
        val left = mutableListOf<String>()

        classes.sorted().forEach { name ->
            val header = headerDirectory.get().file("$name.h").asFile
            require(header.isFile) { "No JNI header for $name. Did the class stop declaring natives?" }

            declaration.findAll(header.readText().replace(Regex("""\s+"""), " ")).forEach { match ->
                val (returns, entry, parameters) = match.destructured
                if (entry !in written) {
                    val named = parameters.split(",").map(String::trim).filter(String::isNotEmpty)
                        .mapIndexed { index, type ->
                            when (index) {
                                0 -> "JNIEnv *env"
                                1 -> "$type self"
                                else -> "$type a$index"
                            }
                        }
                    body.append("\nJNIEXPORT $returns JNICALL $entry(${named.joinToString(", ")}) {\n")
                    body.append("    unimplemented(\"$entry\");\n")
                    if (returns != "void") {
                        body.append("    return 0;\n")
                    }
                    body.append("}\n")
                    left += entry
                }
            }
        }

        val preamble = if (left.isEmpty()) {
            """
            /*
             * Every native the software toolkit declares that is not written yet.
             *
             * There are none left, so this file stands empty. It is still compiled and linked, so
             * that a native taken back out of the sources shows up here rather than as a missing
             * symbol at run time.
             */
            """.trimIndent() + "\n"
        } else {
            """
            /*
             * Every native the software toolkit declares that is not written yet.
             *
             * This file is generated from the JNI headers the client build emits and from which
             * entry points the sources beside it define, so the library exports the whole surface
             * however much of it is real.
             */
            #include <stdio.h>
            #include <stdlib.h>
            #include <jni.h>

            static void unimplemented(const char *name) {
                if (getenv("SW3D_VERBOSE") != NULL) {
                    fprintf(stderr, "[sw3d] %s\n", name);
                }
            }
            """.trimIndent() + "\n"
        }

        stubs.get().asFile.also { it.parentFile.mkdirs() }.writeText(preamble + body)

        outstanding.get().asFile.writeText(left.sorted().joinToString("\n") + "\n")
        logger.lifecycle("${written.size} natives written, ${left.size} left")
    }
}

val toolkitLibrary = layout.buildDirectory.file("natives/libsw3d.dylib")

/**
 * Builds the software toolkit.
 *
 * The sources are listed when the task runs rather than when it is configured, so that adding a
 * file to the directory rebuilds rather than being silently left out of the link.
 */
val compileSoftwareToolkit by tasks.registering(Exec::class) {
    description = "Builds the software toolkit."
    dependsOn(generateToolkitStubs, ":unpackX64Jdk")

    val target = toolkitLibrary.get().asFile
    val written = fileTree(toolkitDirectory) { include("**/*.c", "**/*.m") }

    /*
     * The headers are not compiled, but a change to one changes what the sources compile to, so
     * the build has to know about them. Without this a change to a header alone leaves the last
     * library in place and the checks compare the new sources against the old build.
     */
    val headers = fileTree(toolkitDirectory) { include("**/*.h") }
    val stubs = toolkitStubs.get().asFile
    val includes = listOf(
        jdkHome.dir("include").asFile.absolutePath,
        jdkHome.dir("include/darwin").asFile.absolutePath,
        toolkitDirectory.asFile.absolutePath,
    )

    inputs.files(written)
    inputs.files(headers)
    inputs.file(toolkitStubs)
    outputs.file(toolkitLibrary)

    executable = "clang"

    doFirst {
        target.parentFile.mkdirs()
        setArgs(
            listOf(
                "-arch", "arm64",
                "-arch", "x86_64",
                "-dynamiclib",
                "-fobjc-arc",
                "-Wall",
                "-Werror",
                // The surfaces a 2011 toolkit needs are deprecated by design.
                "-Wno-deprecated-declarations",
                "-O2",
                // The toolkit's arithmetic is done a multiply and an add at a time. Letting the
                // compiler fuse the pair rounds once instead of twice and moves the answer.
                "-ffp-contract=off",
            )
                + includes.flatMap { listOf("-I", it) }
                + listOf(
                    "-framework", "Cocoa",
                    "-framework", "QuartzCore",
                    "-framework", "ImageIO",
                    "-install_name", "@loader_path/libsw3d.dylib",
                    "-o", target.absolutePath,
                    stubs.absolutePath,
                )
                + written.files.map { it.absolutePath }.sorted()
        )
    }
}

val ownFrames = layout.buildDirectory.dir("own-frames")

/**
 * Renders the fixed scene through our own toolkit and leaves the frames beside the ones the
 * shipped toolkit produced, so the two can be compared.
 *
 * This runs on the same x86_64 virtual machine the shipped toolkit needs, so that both sides are
 * compared running the same instructions. The rasteriser divides by the processor's approximate
 * reciprocal rather than by a true division, and how close that approximation is belongs to the
 * instruction set, so a comparison across two of them would be measuring the processor.
 */
val captureOwnFrames by tasks.registering(JavaExec::class) {
    description = "Renders the fixed scene through our own software toolkit."
    dependsOn(compileSoftwareToolkit, ":unpackX64Jdk")
    mainClass = "FrameCapture"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(rootProject.layout.projectDirectory.file(".gradle/jdk-x64/unpacked/Home/bin/java").asFile.absolutePath)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    val directory = ownFrames.get().asFile

    args(toolkitLibrary.get().asFile.absolutePath)
    environment("SW3D_DUMP", directory.absolutePath)
    environment("SW3D_VERBOSE", providers.environmentVariable("SW3D_VERBOSE").getOrElse(""))
    sceneSettings.forEach { (name, fallback) ->
        val held = providers.environmentVariable(name).getOrElse(fallback)
        environment(name, held)
        inputs.property(name, held)
    }
    inputs.file(toolkitLibrary)
    inputs.files(sourceSets["main"].runtimeClasspath)
    outputs.dir(ownFrames)

    doFirst {
        directory.deleteRecursively()
        directory.mkdirs()
    }
}

/**
 * Checks both toolkits against the scenes and against each other.
 *
 * This is the oracle the rest of the toolkit is written against. Both sides draw the same scenes
 * through the same harness and dump their frames the same way, so a scene that disagrees with
 * itself is state left behind, and a scene that disagrees with the other side is a difference in
 * the rasteriser and nothing else.
 */
val verifyToolkit by tasks.registering(JavaExec::class) {
    description = "Checks our toolkit against the shipped one, scene by scene and pixel by pixel."
    dependsOn(captureFrames, captureOwnFrames)
    mainClass = "FrameCheck"
    classpath = sourceSets["main"].runtimeClasspath
    args(
        layout.buildDirectory.dir("frames").get().asFile.absolutePath,
        ownFrames.get().asFile.absolutePath,
        layout.buildDirectory.dir("frame-differences").get().asFile.absolutePath,
        layout.projectDirectory.file("outstanding.txt").asFile.absolutePath,
    )
    inputs.file(layout.projectDirectory.file("outstanding.txt"))
}

/**
 * Registers a probe that asks both toolkits the same questions and compares the answers.
 *
 * Not everything a toolkit does ends up on the screen, and what does not cannot be checked by
 * comparing pictures. A probe drives one named class through each toolkit, writes every answer as
 * a line of text, and the check reads the two files back. Adding one is three tasks, so they are
 * written once here rather than three more times for each new family of natives.
 *
 * The shipped side runs on the x86_64 virtual machine it needs, and ours runs there too, so both
 * sides are asked on the same instruction set.
 */
fun registerProbe(name: String, probe: String, what: String): TaskProvider<JavaExec> {
    val shippedAnswers = layout.buildDirectory.file("answers/$name-shipped.txt")
    val ownAnswers = layout.buildDirectory.file("answers/$name-ours.txt")
    val x64Java = rootProject.layout.projectDirectory
        .file(".gradle/jdk-x64/unpacked/Home/bin/java").asFile.absolutePath

    val captureShipped = tasks.register<JavaExec>("capture${name.replaceFirstChar(Char::uppercase)}") {
        description = "Records what the shipped toolkit answers for $what."
        dependsOn(patchToolkit)
        mainClass = probe
        environment("SW3D_SURVEY", providers.environmentVariable("SW3D_SURVEY").getOrElse(""))
        classpath = sourceSets["main"].runtimeClasspath
        setExecutable(x64Java)
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
        args(patchedToolkit.get().asFile.absolutePath, shippedAnswers.get().asFile.absolutePath)
        outputs.file(shippedAnswers)
        doFirst { shippedAnswers.get().asFile.parentFile.mkdirs() }
    }

    val captureOurs = tasks.register<JavaExec>("captureOwn${name.replaceFirstChar(Char::uppercase)}") {
        description = "Records what our toolkit answers for $what."
        dependsOn(compileSoftwareToolkit, ":unpackX64Jdk")
        mainClass = probe
        environment("SW3D_SURVEY", providers.environmentVariable("SW3D_SURVEY").getOrElse(""))
        classpath = sourceSets["main"].runtimeClasspath
        setExecutable(x64Java)
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
        args(toolkitLibrary.get().asFile.absolutePath, ownAnswers.get().asFile.absolutePath)
        inputs.file(toolkitLibrary)
        inputs.files(sourceSets["main"].runtimeClasspath)
        outputs.file(ownAnswers)
        doFirst { ownAnswers.get().asFile.parentFile.mkdirs() }
    }

    return tasks.register<JavaExec>("verify${name.replaceFirstChar(Char::uppercase)}") {
        description = "Checks our $what against the shipped toolkit's, answer for answer."
        dependsOn(captureShipped, captureOurs)
        mainClass = "AnswerCheck"
        classpath = sourceSets["main"].runtimeClasspath
        args(shippedAnswers.get().asFile.absolutePath, ownAnswers.get().asFile.absolutePath, what)
    }
}

/**
 * The one native the shipped toolkit exports under a C++ name, which no virtual machine can
 * find, so it cannot be driven and there is no answer to compare against. What can be checked
 * is that it agrees with the two written natives that do the same thing in two steps.
 */
val verifySpriteLift by tasks.registering(JavaExec::class) {
    description = "Checks a sprite lifted straight out of the buffer against the same in two steps."
    dependsOn(compileSoftwareToolkit, ":unpackX64Jdk")
    mainClass = "SpriteLiftCheck"
    classpath = sourceSets["main"].runtimeClasspath
    setExecutable(rootProject.layout.projectDirectory.file(".gradle/jdk-x64/unpacked/Home/bin/java").asFile.absolutePath)
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    args(toolkitLibrary.get().asFile.absolutePath)
}

val verifyMatrices = registerProbe("matrices", "MatrixProbe", "matrix answers")
val verifyPoints = registerProbe("points", "PointProbe", "projection answers")
val verifyModels = registerProbe("models", "ModelProbe", "model answers")

/**
 * Every check the toolkit is held to, in one place.
 *
 * Run this before changing anything under the toolkit. It is not part of `check`, because what it
 * measures against is the shipped library out of the game's own cache, which is on a developer's
 * machine and not in this repository: a `check` that depended on it would fail for anyone who has
 * not run the client. Nor can it run anywhere but macOS, because it loads a dylib through a shim
 * built against the window server, and it needs the x86_64 virtual machine that library was built
 * for.
 */
val verifyNatives by tasks.registering {
    group = "verification"
    description = "Runs every check the toolkit is held to against the shipped library."
    dependsOn(
        verifyToolkit,
        verifyToolkitLifetime,
        verifyToolkitSkeleton,
        verifyMemoryLibrary,
        verifyMemoryAnswers,
        verifyOpenGlBinding,
        verifyOpenGlSamples,
        verifyMiscLibrary,
        verifySpriteLift,
        verifyMatrices,
        verifyPoints,
        verifyModels,
    )
}

/**
 * Lists what the map says stands on one tile, so that a place the client draws wrongly can be
 * turned into the models standing there.
 */
val listTerrain by tasks.registering(JavaExec::class) {
    description = "Says what the map is made of on one tile."
    mainClass = "CacheTerrain"
    classpath = sourceSets["main"].runtimeClasspath
    args((providers.gradleProperty("tile").getOrElse("3175 3495 0")).split(" "))
}

val keepModels by tasks.registering(JavaExec::class) {
    description = "Writes the models the scenes are drawn with beside the source."
    mainClass = "CacheModel"
    classpath = sourceSets["main"].runtimeClasspath
    args("keep", layout.projectDirectory.dir("models").asFile.absolutePath)
}

val describeModel by tasks.registering(JavaExec::class) {
    description = "Says what one model out of the cache is made of."
    mainClass = "CacheModel"
    classpath = sourceSets["main"].runtimeClasspath
    args(providers.gradleProperty("model").getOrElse("32421"))
}

val listLocType by tasks.registering(JavaExec::class) {
    description = "Lists the models one kind of location is built from."
    mainClass = "CacheLocType"
    classpath = sourceSets["main"].runtimeClasspath
    args(providers.gradleProperty("loc").getOrElse("29592"))
}

val listLocations by tasks.registering(JavaExec::class) {
    description = "Lists the locations standing on one tile of the world."
    mainClass = "CacheLocations"
    classpath = sourceSets["main"].runtimeClasspath
    args((providers.gradleProperty("tile").getOrElse("3084 3452 0")).split(" "))
    environment("SW3D_LOCATION_KEYS",
        providers.environmentVariable("SW3D_LOCATION_KEYS").getOrElse(""))
}

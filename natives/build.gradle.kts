/**
 * The native pieces the client needs on macOS. There is no Java here.
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

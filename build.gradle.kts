import java.net.URI
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * The software toolkit binaries shipped for macOS carry only i386 and x86_64 slices, so the client
 * has to run on an x86_64 JVM to load them. The build fetches that JVM itself rather than depending
 * on whichever JDKs a given machine happens to have installed.
 *
 * The download lives outside the build directory so that `clean` does not discard 200 MB.
 */
val x64JdkUrl = "https://cdn.azul.com/zulu/bin/zulu21.52.203-ca-jdk21.0.12.1-macosx_x64.tar.gz"
val x64JdkSha256 = "6edaf4b72ec6c23d86a46d8b88d0cfed2aaf81645fee13fd852d37af23c4b9fb"
val x64JdkDir = layout.projectDirectory.dir(".gradle/jdk-x64")
val x64Java = x64JdkDir.file("unpacked/Home/bin/java")
val surfaceLibrary = layout.projectDirectory.file("natives/build/natives/libjawtshim.dylib")
val openGlLibrary = layout.projectDirectory.file("natives/build/natives/libjaggl.dylib")

val onMacOs = providers.systemProperty("os.name").map { it.startsWith("Mac") }.getOrElse(false)

abstract class DownloadVerified : DefaultTask() {

    @get:Input
    abstract val url: Property<String>

    @get:Input
    abstract val sha256: Property<String>

    @get:OutputFile
    abstract val target: RegularFileProperty

    @TaskAction
    fun download() {
        val file = target.get().asFile
        file.parentFile.mkdirs()

        val digest = MessageDigest.getInstance("SHA-256")
        URI(url.get()).toURL().openStream().use { source ->
            DigestInputStream(source, digest).use { hashed ->
                file.outputStream().use(hashed::copyTo)
            }
        }

        val actual = digest.digest().joinToString("") { String.format("%02x", it) }
        if (actual != sha256.get()) {
            file.delete()
            throw GradleException("Checksum mismatch for ${url.get()}, expected ${sha256.get()} but got $actual")
        }
    }
}

val downloadX64Jdk by tasks.registering(DownloadVerified::class) {
    description = "Downloads the x86_64 JDK the macOS software toolkit needs."
    url = x64JdkUrl
    sha256 = x64JdkSha256
    target = x64JdkDir.file(x64JdkUrl.substringAfterLast('/'))
}

val unpackX64Jdk by tasks.registering(Exec::class) {
    description = "Unpacks the x86_64 JDK into .gradle/jdk-x64/unpacked."
    val archive = downloadX64Jdk.flatMap(DownloadVerified::target)
    val into = x64JdkDir.dir("unpacked")
    inputs.file(archive)
    outputs.dir(into)
    executable = "tar"
    args("-xzf", archive.get().asFile.absolutePath, "-C", into.asFile.absolutePath, "--strip-components=2")
    doFirst {
        into.asFile.mkdirs()
    }
}

subprojects {
    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of(21)
            }
        }

        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
        }
    }

    if (onMacOs) {
        plugins.withType<ApplicationPlugin> {
            tasks.named<JavaExec>("run") {
                dependsOn(unpackX64Jdk, ":natives:compileJawtShim", ":natives:compileOpenGlBinding")
                setExecutable(x64Java.asFile.absolutePath)
                systemProperty("toolkit.surface.library", surfaceLibrary.asFile.absolutePath)
                systemProperty("toolkit.jaggl.library", openGlLibrary.asFile.absolutePath)
            }
        }
    }
}

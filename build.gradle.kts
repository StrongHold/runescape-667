import java.net.URI
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * The software toolkit shipped for macOS carries only i386 and x86_64 slices, so anything that
 * loads it has to run on an x86_64 JVM. The build fetches that JVM itself rather than depending on
 * whichever JDKs a given machine happens to have installed.
 *
 * The client no longer needs it. Every library the client loads on macOS is now built here and
 * carries both slices, so the client runs on whatever JVM the toolchain provides. What still needs
 * it is the checking: the shipped toolkit is what our toolkit is measured against, and both sides
 * are driven on the same instruction set so that the comparison is not measuring the processor.
 *
 * The download lives outside the build directory so that `clean` does not discard 200 MB.
 */
val x64JdkUrl = "https://cdn.azul.com/zulu/bin/zulu21.52.203-ca-jdk21.0.12.1-macosx_x64.tar.gz"
val x64JdkSha256 = "6edaf4b72ec6c23d86a46d8b88d0cfed2aaf81645fee13fd852d37af23c4b9fb"
val x64JdkDir = layout.projectDirectory.dir(".gradle/jdk-x64")
val x64Java = x64JdkDir.file("unpacked/Home/bin/java")
val surfaceLibrary = layout.projectDirectory.file("natives/build/natives/libjawtshim.dylib")
val openGlLibrary = layout.projectDirectory.file("natives/build/natives/libjaggl.dylib")
val memoryLibrary = layout.projectDirectory.file("natives/build/natives/libjaclib.dylib")
val softwareToolkit = layout.projectDirectory.file("natives/build/natives/libsw3d.dylib")
val miscLibrary = layout.projectDirectory.file("natives/build/natives/libjagmisc.dylib")

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
                dependsOn(
                    ":natives:compileJawtShim",
                    ":natives:compileOpenGlBinding",
                    ":natives:compileMemoryLibrary",
                    ":natives:compileSoftwareToolkit",
                    ":natives:compileMiscLibrary",
                )
                systemProperty("toolkit.surface.library", surfaceLibrary.asFile.absolutePath)
                systemProperty("toolkit.jaclib.library", memoryLibrary.asFile.absolutePath)
                systemProperty("toolkit.jagmisc.library", miscLibrary.asFile.absolutePath)

                /*
                 * A shipped toolkit is x86_64 and this machine is not, so a run that asks for one
                 * is run on the virtual machine it was built for. Everything is slower under that,
                 * the client included, which is why it is only done when asked.
                 */
                val shipped = providers.gradleProperty("shippedSw3d").isPresent
                    || providers.gradleProperty("shippedJaggl").isPresent

                if (shipped) {
                    dependsOn(":unpackX64Jdk")
                    setExecutable(x64Java.asFile.absolutePath)
                }

                /*
                 * Which toolkit the client draws with is ours unless the shipped one is asked for.
                 *
                 * A library not named here is loaded as it was downloaded, which is what makes the
                 * two comparable in the client rather than only in the harness: -PshippedSw3d and
                 * -PshippedJaggl put the original back for one run, so that anything that looks
                 * wrong can be looked at side by side with what it is meant to look like.
                 *
                 * The shipped software toolkit needs the surface named above, and the shipped
                 * OpenGL binding needs it as well, which is why that one is named whatever is asked
                 * for.
                 */
                if (!providers.gradleProperty("shippedSw3d").isPresent) {
                    systemProperty("toolkit.sw3d.library", softwareToolkit.asFile.absolutePath)
                }

                if (!providers.gradleProperty("shippedJaggl").isPresent) {
                    systemProperty("toolkit.jaggl.library", openGlLibrary.asFile.absolutePath)
                }

                /*
                 * Passed through so that a client which stops responding can be asked where it
                 * stopped, with -Dclient.stalls=<seconds> on the command line, and so that one
                 * which only catches for a moment can be asked the same with
                 * -Dclient.freezes=<milliseconds>.
                 */
                providers.systemProperty("client.stalls").orNull?.let {
                    systemProperty("client.stalls", it)
                }

                providers.systemProperty("client.freezes").orNull?.let {
                    systemProperty("client.freezes", it)
                }

                providers.systemProperty("client.renderer").orNull?.let {
                    systemProperty("client.renderer", it)
                }
            }
        }
    }
}

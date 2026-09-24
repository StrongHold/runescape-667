plugins {
    java
}

/**
 * An agent that writes down the calls a client makes, to compare the recompiled client with the
 * jar while both run. See NativeTrace.
 *
 * It is handed to clients that run on Java 11 as well as 21, since the jar the client came from
 * needs Pack200, which Java 14 removed.
 */
tasks.compileJava {
    options.release = 11
}

dependencies {
    implementation(libs.asm.tree)
    implementation(libs.asm.commons)
}

/**
 * The agent is handed to a client with -javaagent alone, so ASM is packed inside it.
 */
tasks.jar {
    archiveFileName = "native-trace.jar"
    from(configurations.runtimeClasspath.map { classpath -> classpath.map { zipTree(it) } }) {
        exclude("module-info.class", "META-INF/**")
    }
    manifest {
        attributes(
            "Premain-Class" to "NativeTrace",
            "Can-Set-Native-Method-Prefix" to "true",
            // The jar's classes may be loaded by a class loader that cannot see the application's
            // classes, so the record is kept where every class loader can reach it.
            "Boot-Class-Path" to "native-trace.jar",
        )
    }
}

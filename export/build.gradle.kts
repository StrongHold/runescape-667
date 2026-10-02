plugins {
    java
}

dependencies {
    implementation(project(":cache"))
}

tasks.register<JavaExec>("exportModel") {
    description = "Writes one model out of the cache as a binary glTF file."
    mainClass = "ModelExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportNpc") {
    description = "Writes one NPC out of the cache as a binary glTF file, with its stand, turn and walk sequences as animations."
    mainClass = "NpcExport"
    classpath = sourceSets["main"].runtimeClasspath
}

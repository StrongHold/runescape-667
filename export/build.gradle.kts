plugins {
    java
}

dependencies {
    implementation(project(":cache"))
}

tasks.register<JavaExec>("exportTextures") {
    description = "Writes every texture out of the cache as a PNG, into the library the other exports refer to."
    mainClass = "TextureExport"
    classpath = sourceSets["main"].runtimeClasspath
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

tasks.register<JavaExec>("exportSquare") {
    description = "Writes one map square out of the cache as a binary glTF file: its ground on every level and the locations standing on it."
    mainClass = "SquareExport"
    classpath = sourceSets["main"].runtimeClasspath
    environment("SW3D_LOCATION_KEYS",
        providers.environmentVariable("SW3D_LOCATION_KEYS").getOrElse(""))
}

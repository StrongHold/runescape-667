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
    description = "Writes one model out of the cache as a glTF file with its buffer beside it."
    mainClass = "ModelExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportNpc") {
    description = "Writes one NPC out of the cache as a glTF file with its buffer beside it, with its stand, turn and walk sequences as animations."
    mainClass = "NpcExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportLoc") {
    description = "Writes one location type out of the cache as a glTF file with its buffer beside it, into the library the map squares refer to."
    mainClass = "LocExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportMapSquare") {
    description = "Writes one map square out of the cache as a glTF file with its buffer beside it: its ground on every level and the locations standing on it."
    mainClass = "MapSquareExport"
    classpath = sourceSets["main"].runtimeClasspath
    environment("SW3D_LOCATION_KEYS",
        providers.environmentVariable("SW3D_LOCATION_KEYS").getOrElse(""))
}

tasks.register<JavaExec>("exportSprites") {
    description = "Writes every frame of every sprite out of the cache as a PNG of its whole canvas, with the names the client asks for sprites by, and checks each one against the client."
    mainClass = "SpriteExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportFonts") {
    description = "Writes every font out of the cache as a BDF text file, and checks that each draws as the client draws it."
    mainClass = "FontExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportMiniMenu") {
    description = "Writes the colours and sprites the client's scripts style the mini menu with, and the scripts that switch it back to the plain menu."
    mainClass = "MiniMenuExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportHitmarks") {
    description = "Writes every hitmark type and the graphics defaults the client draws hit splats with."
    mainClass = "HitmarkExport"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("exportWidgets") {
    description = "Writes the sprites the client's scripts and interfaces build their scrollbars, buttons, checkboxes, radio buttons, sliders, dropdowns and frames from."
    mainClass = "WidgetExport"
    classpath = sourceSets["main"].runtimeClasspath
}

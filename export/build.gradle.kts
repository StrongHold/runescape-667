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

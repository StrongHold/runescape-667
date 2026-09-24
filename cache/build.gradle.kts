plugins {
    `java-library`
}

dependencies {
    api(project(":cli"))
    api(project(":runescape"))
}

tasks.register<JavaExec>("listCacheLibraries") {
    description = "Lists the native libraries the cache holds, for every platform."
    mainClass = "CacheLibraries"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("extractCacheLibrary") {
    description = "Writes one named native library out of the cache."
    mainClass = "CacheLibrary"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("listTerrain") {
    description = "Says what the map is made of on one tile."
    mainClass = "CacheTerrain"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("describeModel") {
    description = "Says what one model out of the cache is made of."
    mainClass = "CacheModel"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("listLocType") {
    description = "Lists the models one kind of location is built from."
    mainClass = "CacheLocType"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("listLocations") {
    description = "Lists the locations standing on one tile of the world."
    mainClass = "CacheLocations"
    classpath = sourceSets["main"].runtimeClasspath
    environment("SW3D_LOCATION_KEYS",
        providers.environmentVariable("SW3D_LOCATION_KEYS").getOrElse(""))
}

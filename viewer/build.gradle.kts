import com.github.gradle.node.npm.task.NpmTask

plugins {
    base
    alias(libs.plugins.node)
}

node {
    download = false
    npmInstallCommand = "ci"
}

val viteBuild = tasks.register<NpmTask>("viteBuild") {
    dependsOn(tasks.npmInstall)
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Type-checks the viewer and builds it with Vite."
    args = listOf("run", "build")

    inputs.file(layout.projectDirectory.file("package.json"))
    inputs.file(layout.projectDirectory.file("package-lock.json"))
    inputs.file(layout.projectDirectory.file("tsconfig.json"))
    inputs.dir(layout.projectDirectory.dir("etc"))
    inputs.dir(layout.projectDirectory.dir("src"))
    outputs.dir(layout.buildDirectory.dir("dist"))
}

tasks.assemble {
    dependsOn(viteBuild)
}

tasks.register<NpmTask>("dev") {
    dependsOn(tasks.npmInstall)
    group = "application"
    description = "Serves the viewer with every model and NPC under export/build, and opens it."
    args = listOf("run", "dev")
}

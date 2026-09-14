plugins {
    application
}

dependencies {
    implementation(libs.openrs2.deob.annotations)
}

application {
    mainClass = "loader"
}

plugins {
    `java-library`
}

/**
 * What every command line tool in this build shares: reading its arguments, printing its usage,
 * and failing when it is given the wrong ones.
 */
dependencies {
    api(libs.jcommander)
}

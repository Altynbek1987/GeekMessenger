// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.agp.application) apply false
    alias(libs.plugins.agp.library) apply false
    alias(libs.plugins.kotlin.gradle) apply false
    alias(libs.plugins.navigation.safeArgs) apply false
    alias(libs.plugins.hilt.android) apply false
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
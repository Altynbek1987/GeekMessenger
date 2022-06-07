pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
            maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("bla-bla")
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "GeekMessenger"
include(":app")
enableFeaturePreview("VERSION_CATALOGS")
include(":feature-auth")
include(":core")
include(":feature-main")
include(":common")

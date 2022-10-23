plugins {
    // Application
    id("com.android.library")

    // Kotlin
    id("kotlin-android")

    // Kapt
    id("kotlin-kapt")

    // Navigation SafeArgs
    id(libs.plugins.navigation.safeArgs.get().pluginId)

    // Hilt
    id(libs.plugins.hilt.android.get().pluginId)

    // Google Services
    id(libs.plugins.google.services.get().pluginId)

    // Kotlin Serialization
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(project(":core"))

    // Algolia
    implementation(libs.bundles.algolia)

    // Legacy Support
    implementation(libs.legacySupport.legacySupport)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //Paging 3
    implementation(libs.paging.paging)

    // Room with coroutines
    api(libs.bundles.room)
    kapt(libs.room.compiler)
}
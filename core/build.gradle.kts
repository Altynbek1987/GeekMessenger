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
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

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

    api(project(":common"))
    api(libs.bundles.uiComponents)

    // Core
    api(libs.android.core)

    // Coroutines
    api(libs.coroutines.android)
    api(libs.coroutines.core)

    // Lifecycle
    api(libs.bundles.lifecycle)

    // Navigation
    api(libs.bundles.navigation)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Paging 3
    api(libs.paging.paging)

    //FireBase
    api(libs.bundles.firebaseNoAdMobAndCrashlytics)
    api(platform(libs.firebase.platform))

    //Glide
    api(libs.glide.glide)
}
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

    id("kotlin-parcelize")
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
    api(project(":common"))
    api(project(":core"))

    // Algolia
    implementation(libs.bundles.algolia)

    // Legacy Support
    implementation(libs.legacySupport.legacySupport)

    // Hilt
    implementation(libs.hilt.android)
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.google.firebase:firebase-storage:20.1.0")
    kapt(libs.hilt.compiler)

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    //Paging 3
    implementation(libs.paging.paging)

    // Room with coroutines
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)

    // Sendbird
//    implementation(libs.sendbird.sendbird)

}
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
    id(libs.plugins.google.services.get().pluginId)
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

    // Hilt
    implementation(libs.hilt.android)
    implementation("com.google.firebase:firebase-messaging-ktx:23.0.6")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.0.5")
    kapt(libs.hilt.compiler)

    //Paging 3
    implementation(libs.paging.paging)

    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.github.avito-tech:krop:0.64")
    implementation("com.squareup.picasso:picasso:2.71828")

    // Room with coroutines
    api(libs.bundles.room)
    kapt(libs.room.compiler)

}
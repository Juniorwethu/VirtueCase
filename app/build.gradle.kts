plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services") version "4.4.2" // Apply Google Services plugin
}

android {
    namespace = "com.example.virtuecase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.virtuecase"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17 // Use Java 17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17" // Use JVM target 17 for Kotlin
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Ensure this matches your Compose version
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.androidx.activity.compose.v172)

    // Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom.v20240100))
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    //Material 3
    implementation(libs.material.v190)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Additional dependencies
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.storage.ktx)

    // Unit testing dependencies
    testImplementation(libs.junit)

    // Android UI testing dependencies
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(libs.ui.test.junit4)

    // Debugging tools for Jetpack Compose
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}

// Apply Google Services plugin at the end
apply(plugin = "com.google.gms.google-services")

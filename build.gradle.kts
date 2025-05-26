plugins {
    id("com.android.application") version "8.6.1" apply false
    id("com.android.library") version "8.6.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.gradle)
    }
    repositories {
        google()
        mavenCentral()
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
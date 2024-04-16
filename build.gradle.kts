// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.services)
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    id(Classpath.hilt) version Versions.hilt apply false
    id(Plugins.safeArgs) version Versions.safeArgs apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

}
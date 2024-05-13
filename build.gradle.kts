// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    repositories{
        google()
        mavenCentral()
    }
    dependencies{
        // here wee can add dependacies
        classpath("com.journeyapps:zxing-android-embedded:4.3.0")
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}
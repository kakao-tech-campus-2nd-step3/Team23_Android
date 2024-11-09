plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.kappzzang.jeongsan"
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation(project(":common:androidutil"))
    kapt("com.google.dagger:hilt-compiler:2.48.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation(project(":build-config"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}

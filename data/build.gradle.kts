plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") apply false
}

android {
    namespace = "com.kappzzang.jeongsan"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

subprojects {
    apply {
        plugin("com.android.library")
        plugin("org.jetbrains.kotlin.android")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("kotlin-kapt")
        plugin("com.google.dagger.hilt.android")
    }

    dependencies {
        implementation("com.google.dagger:hilt-android:2.48.1")
        kapt("com.google.dagger:hilt-compiler:2.48.1")
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.room:room-runtime:2.6.1")
        kapt("androidx.room:room-compiler:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        implementation(project(":common:util"))
        implementation(project(":domain"))
        implementation(project(":domain:group"))

    }

    android {
        compileSdk = 34

        defaultConfig {
            minSdk = 26

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

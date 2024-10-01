plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.kappzzang.jeongsan"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        plugin("kotlin-kapt")
    }

    dependencies {
        implementation(project(":common:resource"))
        project(":common:androidutil")
        project(":common:util")

        implementation("com.google.dagger:hilt-android:2.48.1")
        kapt("com.google.dagger:hilt-compiler:2.48.1")
    }

    android {
        compileSdk = 34

        defaultConfig {
            minSdk = 26

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
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

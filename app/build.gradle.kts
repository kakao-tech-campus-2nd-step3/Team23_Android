plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.kappzzang.jeongsan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kappzzang.jeongsan"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    project(":ui:main")
    project(":ui:login")
    project(":ui:main")
    project(":ui:expenselist")
    project(":ui:expensedetail")
    project(":ui:creategroup")
    project(":ui:camera")
    project(":ui:addexpense")
    project(":ui:data")
    project(":domain:expense")
    project(":domain:group")
    project(":domain:ocr")
    project(":domain:user")
    project(":data:data-expense")
    project(":data:data-group")
    project(":data:data-ocr")
    project(":data:data-user")
    project(":common:resource")
    project(":common:util")
    project(":common:androidutil")
    implementation(project(":common:navigation"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
}

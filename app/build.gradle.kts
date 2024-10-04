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
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(":ui:main"))
    implementation(project(":ui:login"))
    implementation(project(":ui:main"))
    implementation(project(":ui:expenselist"))
    implementation(project(":ui:expensedetail"))
    implementation(project(":ui:creategroup"))
    implementation(project(":ui:camera"))
    implementation(project(":ui:addexpense"))
    implementation(project(":ui:data"))
    implementation(project(":domain:user"))
    implementation(project(":domain:group"))
    implementation(project(":domain:expense"))
    implementation(project(":domain:ocr"))
    implementation(project(":data:data-expense"))
    implementation(project(":data:data-group"))
    implementation(project(":data:data-ocr"))
    implementation(project(":data:data-user"))
    project(":common:util")
    project(":common:androidutil")
    implementation(project(":common:navigation"))
    implementation(project(":common:resource"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
}

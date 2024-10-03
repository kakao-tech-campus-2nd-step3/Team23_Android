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
        plugin("com.google.dagger.hilt.android")
    }

    dependencies {
        implementation(project(":common:androidutil"))
        implementation(project(":common:util"))
        implementation(project(":common:resource"))

        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.activity:activity-ktx:1.9.0")
        implementation("androidx.test:core-ktx:1.6.1")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
        implementation("androidx.activity:activity:1.9.2")
        implementation("androidx.activity:activity-ktx:1.9.0")
        implementation("nl.dionsegijn:konfetti-xml:2.0.4")
        implementation("com.github.bumptech.glide:glide:4.14.2")
        annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

        implementation("com.google.dagger:hilt-android:2.48.1")
        kapt("com.google.dagger:hilt-compiler:2.48.1")
    }

    android {
        compileSdk = 34

        defaultConfig {
            minSdk = 26
            targetSdk = 34

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
}

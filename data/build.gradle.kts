import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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
        jvmTarget = "17"
    }
}

subprojects {
    fun getApiKey(key: String): String = gradleLocalProperties(rootDir, providers).getProperty(key)
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

        // Test Dependencies
        testImplementation("org.assertj:assertj-core:3.25.3")
        testImplementation("androidx.room:room-testing:2.6.1")
        testImplementation("junit:junit:4.13.2")
        testImplementation("io.mockk:mockk-android:1.13.11")
        testImplementation("io.mockk:mockk-agent:1.13.11")

        testImplementation("androidx.arch.core:core-testing:2.2.0")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")
    }

    android {
        compileSdk = 34

        defaultConfig {
            minSdk = 26

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            buildConfigField("String", "KAKAO_REST_API_KEY", getApiKey("KAKAO_REST_API_KEY"))
            buildConfigField("String", "KAKAO_API_KEY", getApiKey("KAKAO_API_KEY"))
            buildConfigField("String", "KEYSTORE_NAME", getApiKey("KEYSTORE_NAME"))
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlinOptions {
            jvmTarget = "17"
        }
        buildFeatures {
            buildConfig = true
        }
    }
}

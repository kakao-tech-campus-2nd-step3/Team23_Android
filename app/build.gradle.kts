plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

ktlint {
    version.set("1.3.1")
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
    project(":ui:main")
    project(":ui:login")
    project(":ui:main")
    project(":ui:expenselist")
    project(":ui:expensedetail")
    project(":ui:creategroup")
    project(":ui:camera")
    project(":ui:addexpense")
    project(":domain:expense")
    project(":domain:group")
    project(":domain:ocr")
    project(":domain:user")
    project(":data:expense")
    project(":data:group")
    project(":data:ocr")
    project(":data:user")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    //implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    //implementation("androidx.test:core-ktx:1.6.1")
    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    //implementation("androidx.room:room-runtime:2.6.1")
    //implementation("androidx.camera:camera-view:1.3.4")
    //implementation("androidx.camera:camera-camera2:1.3.4")
    //implementation("androidx.camera:camera-lifecycle:1.3.4")
    //kapt("androidx.room:room-compiler:2.6.1")
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
    //implementation("androidx.room:room-ktx:2.6.1")
    //testImplementation("androidx.room:room-testing:2.6.1")
    //testImplementation("junit:junit:4.13.2")
    //testImplementation("io.mockk:mockk-android:1.13.11")
    //testImplementation("io.mockk:mockk-agent:1.13.11")
    //testImplementation("androidx.arch.core:core-testing:2.2.0")
    //testImplementation("org.robolectric:robolectric:4.11.1")
    //testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    //androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    //androidTestImplementation("androidx.test.ext:junit:1.2.1")
    //androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    //androidTestImplementation("androidx.test:rules:1.6.1")
    //androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    //androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    //kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")

    //implementation("nl.dionsegijn:konfetti-xml:2.0.4")

    //implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    //implementation("androidx.navigation:navigation-ui-ktx:2.8.1")
    //implementation("com.github.bumptech.glide:glide:4.14.2")
    //annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
}

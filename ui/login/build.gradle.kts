import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.kappzzang.jeongsan.login"
}

dependencies {
    implementation(project(":domain:user"))
    implementation("com.kakao.sdk:v2-user:2.20.6")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.2")
}

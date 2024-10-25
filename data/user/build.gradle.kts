plugins {
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.kappzzang.jeongsan.user"
}
dependencies {
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.kakao.sdk:v2-user:2.20.6")
    implementation(project(":domain:common-user"))
    implementation(project(":common:androidutil"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation(project(":common:retrofit"))
}

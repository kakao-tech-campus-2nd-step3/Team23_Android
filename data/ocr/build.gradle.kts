plugins {
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.kappzzang.jeongsan.ocr"
}
dependencies {
    implementation(project(":domain:ocr"))
    implementation(project(":common:retrofit"))
}

plugins {
    id("kotlin-parcelize")
}

android {
    namespace = "com.kappzzang.jeongsan.ocr"
}
dependencies {
    implementation(project(":common:androidutil"))
}

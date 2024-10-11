plugins {
    id("kotlin-parcelize")
}

android {
    namespace = "com.kappzzang.jeongsan.expense"
}
dependencies {
    implementation(project(":domain:user"))
}
